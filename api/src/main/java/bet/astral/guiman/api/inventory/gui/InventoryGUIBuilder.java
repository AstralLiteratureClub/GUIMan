package bet.astral.guiman.api.inventory.gui;

import bet.astral.guiman.api.DataLike;
import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableBuilder;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Builder used to configure and create an {@link InventoryGUI}.
 */
public interface InventoryGUIBuilder extends DataLike {
	/**
	 * Returns a new inventory gui builder
	 *
	 * @return new instance
	 */
	static @NotNull InventoryGUIBuilder builder() {
		return GUIMan.getGUIMan().inventoryBuilder();
	}

	/**
	 * Sets a static title component for the inventory.
	 * <p>
	 * If a translation title is also set, the translated title may take priority.
	 *
	 * @param titleComponent static title component
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setTitle(Component titleComponent);

	/**
	 * Sets the inventory type.
	 *
	 * @param inventoryType inventory type
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setInventoryType(InventoryType inventoryType);

	/**
	 * Sets the number of chest rows for chest-based inventories.
	 *
	 * @param chestRows number of rows
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setChestRows(ChestRows chestRows);

	/**
	 * Sets the clickables mapped to inventory slots.
	 *
	 * @param clickables slot-to-clickable mapping
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setClickables(Map<Integer, Collection<ClickableLike>> clickables);

	/**
	 * Sets the clickable the given slot to the given clickable
	 * @param slot slot
	 * @param clickableLike clickable
	 * @return this
	 */
	InventoryGUIBuilder setClickable(int slot, ClickableLike clickableLike);

	/**
	 * Sets the clickable the given slot to the given clickables
	 * @param slot slot
	 * @param clickableLikes clickables
	 * @return this
	 */
	InventoryGUIBuilder setClickables(int slot, ClickableLike... clickableLikes);

	/**
	 * Adds the clickable the given slot
	 * @param slot slot
	 * @param clickableLike clickable
	 * @return this
	 */
	InventoryGUIBuilder addClickable(int slot, ClickableLike clickableLike);

	/**
	 * Adds the clickables the given slot
	 * @param slot slot
	 * @param clickableLike clickable
	 * @return this
	 */
	InventoryGUIBuilder addClickables(int slot, ClickableLike... clickableLike);

	/**
	 * Generates the clickabels for the given slot
	 * @param slot slot
	 * @param generator generator
	 * @return this
	 */
	InventoryGUIBuilder generateClickables(int slot, SlotClickableGenerator generator);
	/**
	 * Generates the clickables of this GUI
	 * @param generator generator
	 * @return this
	 */
	InventoryGUIBuilder generateClickables(FullClickableGenerator generator);

	/**
	 * Sets the background used to fill empty slots.
	 *
	 * @param background background definition
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setBackground(Background background);

	/**
	 * Controls whether clickable items are regenerated every time the inventory is opened.
	 *
	 * @param generatesItemsEachOpen true to regenerate items on each open
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setGeneratesItemsEachOpen(boolean generatesItemsEachOpen);

	/**
	 * Sets whether this inventory instance is shared between viewers.
	 *
	 * @param shared true if the inventory should be shared
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setShared(boolean shared);

	/**
	 * Sets an action executed when a player opens the inventory.
	 *
	 * @param openAction open action
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setOpenAction(Consumer<Player> openAction);

	/**
	 * Sets an action executed when a player closes the inventory.
	 *
	 * @param closeAction close action
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setCloseAction(Consumer<Player> closeAction);

	/**
	 * Sets an exception handler executed when an error occurs during inventory interaction.
	 *
	 * @param exceptionHandler exception handler
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setExceptionHandler(Consumer<Player> exceptionHandler);

	/**
	 * Sets a data value in the clickable
	 *
	 * @param key   key
	 * @param value value
	 * @return this
	 */
	InventoryGUIBuilder setData(@NotNull String key, Object value);

	/**
	 * Builds the {@link InventoryGUI} using the current configuration.
	 *
	 * @return this builder
	 */
	@NotNull
	InventoryGUI build();

	@FunctionalInterface
	interface FullClickableGenerator {
		Map<Integer, Collection<ClickableLike>> generate();
	}
	@FunctionalInterface
	interface SlotClickableGenerator {
		Collection<ClickableLike> generate();
	}
}
