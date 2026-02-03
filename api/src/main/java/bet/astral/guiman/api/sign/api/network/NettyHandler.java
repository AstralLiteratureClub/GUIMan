package bet.astral.guiman.api.sign.api.network;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Netty handler used internally by signman
 */
public interface NettyHandler {
	/**
	 * Returns the plugin providing sign man
	 * @return plugin
	 */
	JavaPlugin getPlugin();

	/**
	 * True if this is async
	 * @return async
	 */
	boolean isASync();

	/**
	 * Returns the player who this handler is made for
	 * @return player
	 */
	Player getPlayer();
}
