package bet.astral.guiman.clickable;

import bet.astral.messenger.v2.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Provides clickable when required by inventory GUI
 */
@FunctionalInterface
public interface ClickableProvider extends ClickableLike {
	/**
	 * Generates clickable
	 * @return clickable
	 */
	@NotNull
	Clickable provide(Player player);

	/**
	 * Generates clickable for player and generates item
	 * @param messenger messenger
	 * @param player player
	 * @return item stack
	 */
	@Override
	default ItemStack generate(Messenger messenger, Player player) {
		return provide(player).generate(messenger, player);
	}

	/**
	 * Returns NULL, use {@link #provide(Player)}
	 * @return null
	 */
	@Override
	default Clickable asClickable() {
		return null;
	}
}
