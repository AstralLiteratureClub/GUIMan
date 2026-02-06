package bet.astral.guiman.api.inventory.clickable;

import org.bukkit.entity.Player;
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
	 * Returns NULL, use {@link #provide(Player)}
	 * @return null
	 */
	@Override
	default Clickable asClickable() {
		return null;
	}
}
