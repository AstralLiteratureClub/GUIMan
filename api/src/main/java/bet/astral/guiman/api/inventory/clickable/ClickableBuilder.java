package bet.astral.guiman.api.inventory.clickable;

import bet.astral.guiman.api.DataLike;
import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Helper class to build a new {@link Clickable} instance
 */
public interface ClickableBuilder extends ClickableLike {
	/**
	 * Returns a new clickable builder instance with material as {@link Material#STONE}
	 * @return new builder
	 */
	@NotNull
	static ClickableBuilder builder() {
		return builder(Material.STONE);
	}

	/**
	 * Returns a new clickable builder instance with the item as clickable item
	 * @param itemStack item to display
	 * @return this
	 */
	@NotNull
	static ClickableBuilder builder(@NotNull ItemStack itemStack) {
		return GUIMan.getGUIMan().clickableBuilder(itemStack);
	}

	/**
	 * Returns a new clickable builder instance with the item as clickable item
	 * @param material item to display
	 * @return this
	 */
	@NotNull
	static ClickableBuilder builder(@NotNull Material material) {
		return builder(new ItemStack(material));
	}

	/**
	 * Returns a new clickable builder instance. {@link ItemStack} is built using the material and consumer is after accepted to modify the item
	 * @param material item to display
	 * @param itemEditor item editor consumer
	 * @return this
	 */
	static @NotNull ClickableBuilder builder(@NotNull Material material, @NotNull Consumer<ItemStack> itemEditor) {
		ItemStack item = new ItemStack(material);
		itemEditor.accept(item);
		return builder(item);
	}
	/**
	 * Sets the item material of the clickable
	 * @param material material
	 * @return this
	 */
	@NotNull
	ClickableBuilder setMaterial(Material material);

	/**
	 * Sets the item of the clickable
	 * @param item item
	 * @return this
	 */
	@NotNull
	ClickableBuilder setItem(ItemStack item);

	/**
	 * Sets the name of the clickable
	 * @param name name
	 * @return this
	 */
	@NotNull
	ClickableBuilder setName(Component name);

	/**
	 * Sets the lore of the item
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	ClickableBuilder setLore(List<Component> lore);

	/**
	 * Sets the lore of the item
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	ClickableBuilder setLore(Component... lore);

	/**
	 * Adds the given lore to the existing lore of the item
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	ClickableBuilder addLore(List<Component> lore);
	/**
	 * Adds the given lore to the existing lore of the item
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	ClickableBuilder addLore(Component... lore);

	/**
	 * Hides tooltip from the item stack
	 * @return this
	 */
	@NotNull
	ClickableBuilder hideTooltip();

	/**
	 * Hides item flags from the item stack
	 * @return this
	 */
	@NotNull
	ClickableBuilder hideItemFlags();

	/**
	 * Hides the item's durability
	 * @return this
	 */
	@NotNull
	ClickableBuilder hideDurability();

	/**
	 * Sets the permission message of this clickable
	 * @param message message
	 * @return this
	 */
	@NotNull
	ClickableBuilder setPermissionMessage(Component message);

	/**
	 * Sets the permission of this clickable
	 * @param permission permission
	 * @return this
	 */
	@NotNull
	ClickableBuilder setPermission(Permission permission);

	/**
	 * Sets the permission of this clickable as predicate
	 * @param predicate prdicate
	 * @return this
	 */
	@NotNull
	ClickableBuilder setPermission(Predicate<Player> predicate);

	/**
	 * Hides this clickable if player does not have the permission to see it.
	 * <p>{@link Boolean#TRUE } = HIDE</p>
	 * <p>{@link Boolean#FALSE } = REVEAL</p>
	 * @param v true, if hide
	 * @return this
	 */
	@NotNull
	ClickableBuilder hideIfPNoPermission(boolean v);

	/**
	 * Sets a data value in the clickable
	 *
	 * @param key   key
	 * @param value value
	 * @return this
	 */
	ClickableBuilder setData(@NotNull String key, Object value);


	/**
	 * Makes the clickable actions ran async from the bukkit thread
	 * @return this
	 */
	@NotNull
	ClickableBuilder setASync();

	/**
	 * Sets the display priority of this clickable
	 * @param priority priority
	 * @return this
	 */
	@NotNull
	ClickableBuilder setPriority(@Range(from=0, to=Integer.MAX_VALUE) int priority);

	/**
	 * Sets the click actions of this clickable
	 * @param actions actions
	 * @return this
	 */
	@NotNull
	ClickableBuilder setActions(Map<ClickType, ClickAction> actions);

	/**
	 * Adds a new click action to the clickable when player clicks with given click type
	 * @param clickType click type
	 * @param clickAction action to run when clicked
	 * @return this
	 */
	@NotNull
	ClickableBuilder addAction(ClickType clickType, ClickAction clickAction);

	/**
	 * Adds a new click action to the clickable when player clicks with given click types
	 * @param clickTypes click types
	 * @param clickAction action to run when clicked
	 * @return this
	 */
	@NotNull
	ClickableBuilder addAction(List<ClickType> clickTypes, ClickAction clickAction);

	/**
	 * Adds a general click action to the clickable when player clicks with standard mouse click
	 * @param clickAction click action
	 * @return this
	 */
	@NotNull
	ClickableBuilder addGeneralAction(ClickAction clickAction);

	/**
	 * Converts this to a messenger based clickable builder.
	 * @see MClickableBuilder
	 * @return messenger based clickable builder
	 */
	@NotNull
	MClickableBuilder asMessengerCompatible();

	/**
	 * Builds this clickable
	 * @return this as a new clickable
	 */
	@NotNull
	Clickable build();

	/**
	 * Builds this clickable
	 * @return this as a new clickable
	 */
	@Override
	default Clickable asClickable() {
		return build();
	}
}
