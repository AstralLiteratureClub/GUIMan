package bet.astral.guiman.api.inventory.gui;

import bet.astral.guiman.api.DataLike;
import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents inventory gui with actions stored within it.
 */
public interface InventoryGUI extends DataLike {
	/**
	 * Returns a new inventory gui builder
	 * @return new instance
	 */
	static @NotNull InventoryGUIBuilder builder() {
		return GUIMan.getGUIMan().inventoryBuilder();
	}
	/**
	 * Returns the title of the GUI. This is used as a second priority if {@link MInventoryGUI#getTitleTranslation()} is not set
	 * @return title, nullable
	 */
	@Nullable
	Component getTitle();

	/**
	 * Checks if the {@link #getTitle()} is set
	 * @return true if it has title
	 */
	boolean hasComponentTitle();

	/**
	 * Returns the type of this inventory.
	 * @return type of this inventory
	 */
	InventoryType getInventoryType();

	/**
	 * Returns true if this inventory is a chest
	 * @return chest
	 */
	boolean isChest();

	/**
	 * Returns the size of this GUI
	 * @return rows, defaults to {@link ChestRows#THREE}
	 */
	ChestRows getChestRows();

	/**
	 * Returns the size of this GUI
	 * @return rows, defaults to 3
	 */
	int getRows();

	/**
	 * Returns the amount of slots in this inventory
	 * @return slots
	 */
	int getSlots();

	/**
	 * Returns all clickable in the inventory
	 * @return clickables
	 */
	Map<Integer, Collection<ClickableLike>> getClickables();

	/**
	 * Returns all clickables in the given slot
	 * @param slot slot
	 * @return clickables
	 */
	Collection<ClickableLike> getClickable(int slot);

	/**
	 * Returns the background of this inventory
	 * @return background
	 */
	Background getBackground();

	/**
	 * Returns true, if this inventory regenerates items each time it is opened
	 * @return regenerates items
	 */
	boolean regeneratesItemsEachOpen();

	/**
	 * Returns true, if this inventory is shared between the entire server
	 * @return true if shared
	 */
	boolean isShared();

	/**
	 * Returns true, if this inventory contains messenger actions
	 * @return true if, messenger based inventory
	 */
	default boolean isMessengerEnabledInventory() {
		return this instanceof MInventoryGUI;
	}

	/**
	 * Returns the action ran, when a player opens this inventory
	 * @return action
	 */
	Consumer<Player> getOpenAction();

	/**
	 * Returns the action ran, when a player closes this inventory
	 * @return action
	 */
	Consumer<Player> getCloseAction();

	/**
	 * Returns the action ran, when an exception occurs in this inventory
	 * @return action
	 */
	Consumer<Player> getExceptionHandler();

	/**
	 * Opens the GUI to the given player
	 * @param player player to open gui to
	 */
    void open(Player player);
}
