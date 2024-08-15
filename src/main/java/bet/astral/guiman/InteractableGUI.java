package bet.astral.guiman;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.clickable.ClickableProvider;
import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interactable GUI which players click on.
 * Uses {@link InventoryGUI} as base and {@link InventoryListener} converts clicks to actions represented by {@link Clickable}
 */
@ApiStatus.Internal
public class InteractableGUI implements InventoryHolder {
	private final InventoryGUI gui;
	private final Inventory inventory;

	/**
	 * Creates a new interactable gui for given player using the given gui. Does not support shared GUIs
	 * @param gui inventory base
	 * @param player player to open to
	 */
	public InteractableGUI(@NotNull InventoryGUI gui, @NotNull Player player) {
		this.gui = gui;
		if (gui.getType() == InventoryType.CHEST) {
			if (gui.getName() == null && gui.getNameTranslation() == null) {
				inventory = Bukkit.createInventory(this, gui.getSlots());
			} else {
				inventory = Bukkit.createInventory(this, gui.getSlots(), gui.getNameTranslation() != null ?
						gui.getMessenger().parseComponent(new MessageInfoBuilder(gui.getNameTranslation())
								.addPlaceholders(gui.getPlaceholderGenerator().apply(player)).create(), ComponentType.CHAT, gui.getMessenger().convertReceiver(player)) : gui.getName());
			}
		} else {
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getType());
			} else {
				inventory = Bukkit.createInventory(this, gui.getType(), gui.getNameTranslation() != null ?
						gui.getMessenger().parseComponent(new MessageInfoBuilder(gui.getNameTranslation())
								.addPlaceholders(gui.getPlaceholderGenerator().apply(player)).create(), ComponentType.CHAT, gui.getMessenger().convertReceiver(player)) : gui.getName());
			}
		}
		generate(player, gui.getMessenger());
	}

	/**
	 * Generates the chest inventory view to the inventory.
	 * Allows usage of messenger to set name of titles and lore of items and the inventory itself
	 * @param player player
	 * @param messenger (Nullable) messenger
	 */
	public void generate(@NotNull Player player, @Nullable Messenger messenger) {
		try {
			inventory.clear();
			Background background = gui.getBackground();
			for (int i = 0; i < inventory.getSize(); i++) {
				if (i > inventory.getSize()) {
					return;
				}

				boolean isBackground = false;
				Collection<ClickableLike> clickables = gui.getClickables().get(i);
				if (clickables == null || clickables.isEmpty()) {
					if (gui.getBackground() != null) {
						ClickableLike clickableLike = background.getSlotOrEmpty(i);
						clickables = List.of(clickableLike);
						isBackground = true;
					} else {
						return;
					}
				}

				List<Clickable> clickableList =
						clickables.
								stream().
								map(clickableLike ->
										clickableLike instanceof ClickableProvider provider
												? provider.provide(player)
												: clickableLike.asClickable())
								.distinct()
								.sorted()
								.collect(Collectors.toList());
				Collections.reverse(clickableList);

				for (Clickable clickable : clickableList) {
					if (clickable == null) {
						clickable = gui.registerClickable(background.getSlotOrEmpty(i), player);
						isBackground = true;
					}
					gui.registerClickable(clickable, player);
					if (!isBackground && hasPermission(player, clickable.getPermission(), gui)) {
						inventory.setItem(i, clickable.generate(messenger, player));
						break;
					} else if (!isBackground && !hasPermission(player, clickable.getPermission(), gui) && clickable.isDisplayIfNoPermissions()) {
						inventory.setItem(i, clickable.generate(messenger, player));
						break;
					} else if (isBackground){
						inventory.setItem(i, clickable.generate(messenger, player));
						break;
					}
				}
			}
		} catch (Exception e){
			if (gui.getGenerationExceptionPlayerHandler() != null){
				gui.getGenerationExceptionPlayerHandler().accept(player);
			}
			throw new RuntimeException(e);
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
	 * Returns if player has permission to use given permission. Returns true if player or permission is null
	 * @param player player
	 * @param permission permission
	 * @param gui the gui
	 * @return has permission
	 */
	private boolean hasPermission(@Nullable Player player, Permission permission, InventoryGUI gui){
		if (permission==null){
			return true;
		}
		if (player == null){
			return true;
		}
		return permission.hasPermission(player, gui);
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