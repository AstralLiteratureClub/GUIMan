package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.Clickable;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.inventory.clickable.ClickableProvider;
import bet.astral.more4j.function.function.TriFunction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

/**
 * Interactable GUI which players click on.
 * Uses {@link InventoryGUI} as base and {@link bet.astral.guiman.core.inventory.listeners.InventoryClickListener} converts clicks to actions represented by {@link bet.astral.guiman.api.inventory.clickable.Clickable}
 */
@ApiStatus.Internal
public class PlayerInventory implements InventoryHolder {
	protected static TriFunction<InventoryHolder, Object, Component, org.bukkit.inventory.Inventory> createInventory;

	public static void setup() {
		createInventory = (holder, t, name)-> {
			if (t instanceof Integer size){
				if (name == null)
					return Bukkit.createInventory(holder, size);
				return Bukkit.createInventory(holder, size, name);
			} else {
				if (name == null)
					return Bukkit.createInventory(holder, (InventoryType) t);
				return Bukkit.createInventory(holder, (InventoryType) t, name);
			}};
	}

	private final bet.astral.guiman.core.inventory.gui.Inventory gui;
	private final org.bukkit.inventory.Inventory inventory;

	/**
	 * Creates a new interactable gui for given player using the given gui. Does not support shared GUIs
	 * @param gui inventory base
	 * @param player player to open to
	 */
	@ApiStatus.Internal
	public PlayerInventory(@NotNull Inventory gui, @NotNull Player player) {
		this.gui = gui;

		Object obj = gui.getInventoryType() == InventoryType.CHEST ? gui.getSlots() : gui.getInventoryType();

		// Create a new inventory
		inventory = createInventory.apply(this, obj, getName(gui, player));
		generate(player);
	}

	public void generate(@NotNull Player player){
		try {
			deployBackground(player);
			deployClickables(player);
		} catch (Exception e) {
			if (gui.getExceptionHandler() != null){
				gui.getExceptionHandler().accept(player, e);
			} else {
				throw e;
			}
		}
	}

	public void deployBackground(@NotNull Player player){
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

			// Generate a new item, as clickable(s) are made for each player separately.
			ItemStack itemStack = clickable.generate(player);
			inventory.setItem(i, itemStack);
		}
	}

	public void deployClickables(@NotNull Player player){
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
			ItemStack itemStack = clickable.generate(player);
			inventory.setItem(i, itemStack);
		}
	}

	protected Component getName(@NotNull Inventory gui, Player player) {
		if (gui.getTitle() == null) {
			return null;
		}
		return gui.getTitle();
	}

	/**
	 * Returns the base for this inventory gui
	 * @return GUI
	 */
	public Inventory getGUI(){
		return gui;
	}

	/**
	 * Returns the bukkit inventory for this interactable gui
	 * @return inventory
	 */
	@Override
	public @NotNull org.bukkit.inventory.Inventory getInventory() {
		return inventory;
	}
}
