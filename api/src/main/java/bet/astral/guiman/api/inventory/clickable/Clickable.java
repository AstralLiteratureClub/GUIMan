package bet.astral.guiman.api.inventory.clickable;

import bet.astral.guiman.api.DataLike;
import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.Permission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Clickable context in inventory. Works like a button
 */
public interface Clickable extends DataLike, Comparable<Clickable> {
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
	 * Returns the priority to use this in the inventory. Higher priority = more present in the GUI
	 *
	 * @return priority
	 */
	int getPriority();

	/**
	 * Returns the item to use in this clickable
	 *
	 * @return item
	 */
	@NotNull
	ItemStack getItem();

	/**
	 * Returns the permission to see and use this clickable
	 *
	 * @return permission
	 */
	@NotNull
	Permission getPermission();

	/**
	 * Checks if this clickable has a permission
	 *
	 * @return has permission
	 */
	boolean hasPermission();

	/**
	 * Sends the permission message to the player
	 *
	 * @param player player
	 */
	void sendPermissionMessage(Player player);

	/**
	 * Returns if this clickable action(s) is/are run async
	 *
	 * @return true if async
	 */
	boolean isAsync();

	/**
	 * Generates the clickable as an item for the GUI
	 *
	 * @param player player
	 * @return item
	 */
	@NotNull
	ItemStack generate(@NotNull Player player);

	int compareTo(@NotNull Clickable clickable);
}
