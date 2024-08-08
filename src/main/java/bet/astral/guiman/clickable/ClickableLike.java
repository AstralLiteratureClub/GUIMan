package bet.astral.guiman.clickable;

import bet.astral.messenger.v2.Messenger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Clickable like which only generates item stack for GUI
 */
public interface ClickableLike {
	/**
	 * Generates item stack for inventories
	 * @param player player
	 * @return item
	 */
	ItemStack generate(Messenger messenger, Player player);

	default Clickable asClickable() { return null; }
}
