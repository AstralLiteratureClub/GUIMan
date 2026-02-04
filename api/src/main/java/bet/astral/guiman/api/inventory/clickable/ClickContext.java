package bet.astral.guiman.api.inventory.clickable;

import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Click context when a player clicks a clickable
 */
public interface ClickContext {
	/**
	 * Returns the inventory GUI
	 * @return gui
	 */
	InventoryGUI getGUI();

	/**
	 * Returns the inventory instance
	 * @return inventory
	 */
	Inventory getInventory();

	/**
	 * Returns the item stack generated from the clickable
	 * @return item
	 */
	ItemStack getItemStack();

	/**
	 * Returns the click type
	 * @return
	 */
	ClickType getClickType();

	/**
	 * Returns who clicked the clickable
	 * @return who clicked
	 */
	Player getPlayer();

	/**
	 * Returns the clickable clicked
	 * @return clickable
	 */
	Clickable getClickable();

	/**
	 * Returns true if this class is an instance of {@link MClickContext}
	 * @return true if is an instance
	 */
	default boolean isMessengerContext() {
		return this instanceof MClickContext;
	}
}
