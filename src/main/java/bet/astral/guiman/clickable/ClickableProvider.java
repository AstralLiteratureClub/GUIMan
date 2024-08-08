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

	@Override
	default ItemStack generate(Messenger messenger, Player player) {
		return asClickable().generate(messenger, player);
	}
}
