package bet.astral.guiman.internals;

import bet.astral.guiman.GUIMan;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.clickable.ClickableProvider;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.more4j.function.function.TriFunction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.List;

/**
 * Interactable GUI which players click on.
 * Uses {@link InventoryGUI} as base and {@link InventoryClickListener} converts clicks to actions represented by {@link Clickable}
 */
@ApiStatus.Internal
public class InteractableGUI implements InventoryHolder {
	public static final TriFunction<InventoryHolder, Object, Component, Inventory> createFunction = (holder, t, name)-> {
		if (t instanceof Integer size){
			if (name == null)
				return Bukkit.createInventory(holder, size);
			return Bukkit.createInventory(holder, size, name);
		} else {
			if (name == null)
				return Bukkit.createInventory(holder, (InventoryType) t);
			return Bukkit.createInventory(holder, (InventoryType) t, name);
		}};

	private final InventoryGUI gui;
	private final Inventory inventory;

	/**
	 * Creates a new interactable gui for given player using the given gui. Does not support shared GUIs
	 * @param gui inventory base
	 * @param player player to open to
	 */
	@ApiStatus.Internal
	public InteractableGUI(@NotNull InventoryGUI gui, @NotNull Player player) {
		this.gui = gui;

		Object obj = gui.getType() == InventoryType.CHEST ? gui.getSlots() : gui.getType();
		// Return default inventory, if inventory name is null
		if (gui.getNameTranslation() == null && gui.getName() == null) {
			inventory = createFunction.apply(this, obj, null);
		} else {
			Component name = null;
			// Return translation key as name, if messenger is null
			if (gui.getNameTranslation() != null && gui.getMessenger() == null){
				inventory = createFunction.apply(this, obj, Component.translatable(gui.getNameTranslation()));
                GUIMan.GUIMAN.getPlugin().getSLF4JLogger().warn("Messenger is not set for inventory {}, {}", gui.getNameTranslation().translationKey(), new Throwable());
			} else if (gui.getNameTranslation() != null){
				// load messenger transltion for the GUI title
				Locale locale = gui.getMessenger().getLocaleFromReceiver(player);

				// Build the info to simplify the later parsing
				MessageInfoBuilder messageInfo = new MessageInfoBuilder(gui.getNameTranslation())
						.withPlaceholders(gui.getPlaceholderGenerator().apply(player));
				if (locale != null){
					messageInfo.withLocale(locale);
				}

				Receiver receiver = gui.getMessenger().convertReceiver(player);
				assert receiver != null;

				// Parse the gui name
				name = gui.getMessenger()
						.disablePrefixForNextParse()
						.parseComponent(messageInfo.build(), ComponentType.CHAT, receiver);
				inventory = createFunction.apply(this, obj, name);
			} else {
				name = gui.getName();
				inventory = createFunction.apply(this, obj, name);
			}
		}
		generate(player, gui.getMessenger());
	}

	public void generate(@NotNull Player player, @Nullable Messenger messenger){
		try {
			deployBackground(player, messenger);
			deployClickables(player, messenger);
		} catch (Exception e) {
			if (gui.getGenerationExceptionPlayerHandler() != null) {
				gui.getGenerationExceptionPlayerHandler().accept(player);
			}
			throw e;
		}
	}

	public void deployBackground(@NotNull Player player, @Nullable Messenger messenger){
		// Check if no background | background is empty -> return
		if (gui.getBackground() == null || gui.getBackground().isEmpty()){
			return;
		}
		inventory.clear();
		Background background = gui.getBackground();
		// Loop every slot
		for (int i = 0; i < inventory.getSize(); i++){
			if (i > inventory.getSize()){
				return;
			}

			// Get default clickable
			ClickableLike clickableLike = background.getSlotOrEmpty(i);
			// Make sure clickable is a real clickable and not a provider
			Clickable clickable = clickableLike instanceof ClickableProvider provider ? provider.provide(player) : clickableLike.asClickable();
			// Register background to be sure it's real clickable in the systems
			gui.registerClickable(clickable, player);

			// Generate a new item, as clickable(s) are made for each player separately. Because of messenger allows multiple languages
			ItemStack itemStack = clickable.generate(messenger, player);
			inventory.setItem(i, itemStack);
		}
	}

	public void deployClickables(@NotNull Player player, @Nullable Messenger messenger){
		// Check if there are ANY clickables
		if (gui.getClickables().isEmpty()){
			// None -> Return
			return;
		}


		Map<Integer, Collection<ClickableLike>> clickablesBySlot = gui.getClickables();
		// Loop every slot
		for (int i = 0; i < inventory.getSize(); i++){
			if (i > inventory.getSize()){
				return;
			}

			// Don't continue to stream if the list is an empty // null one
			if (clickablesBySlot.get(i)==null || clickablesBySlot.get(i).isEmpty()){
				continue;
			}

			// Get clickables
			List<ClickableLike> clickableLikes = new LinkedList<>(clickablesBySlot.get(i));
			List<Clickable> clickables = new ArrayList<>(clickableLikes
                    .stream()
                    // Make all clickable likes clickables if they are providers
                    .map(clickableLike ->
                            clickableLike instanceof ClickableProvider provider ? provider.provide(player) : clickableLike.asClickable())
                    // sort clickables (priority low -> high)
                    .distinct()
                    .sorted()
                    // Return
                    .toList());
			// Turn clickables around high -> low
			clickables.reversed();

			clickables.removeIf(clickable->!clickable.getPermission().hasPermission(player, gui) && !clickable.isDisplayIfNoPermissions());

			// empty -> return
			if (clickables.isEmpty()){
				continue;
			}

			// Resort everything
			clickables.sort(Clickable::compareTo);
			clickables.reversed();

			// Register
			Clickable clickable = clickables.getFirst();
			gui.registerClickable(clickable, player);

			// display
			ItemStack itemStack = clickable.generate(messenger, player);
			inventory.setItem(i, itemStack);
		}
	}

	/**
	 * Returns the base for this inventory gui
	 * @return GUI
	 */
	public InventoryGUI getGUI(){
		return gui;
	}

	/**
	 * Returns the bukkit inventory for this interactable gui
	 * @return inventory
	 */
	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}
}