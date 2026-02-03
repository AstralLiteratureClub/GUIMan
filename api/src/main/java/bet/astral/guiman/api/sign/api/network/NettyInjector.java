package bet.astral.guiman.api.sign.api.network;

import bet.astral.guiman.api.sign.api.Sign;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Internal injector of signman to handle packet based signing of signs.
 */
public interface NettyInjector {
	/**
	 * The plugin which is providing sign man
	 * @return plugin
	 */
	JavaPlugin getPlugin();

	/**
	 * Returns true if this async
	 * @return async
	 */
	boolean isASync();

	/**
	 * Returns the key used for the stream
	 * @return key
	 */
	NamespacedKey getKey();

	/**
	 * Adds a new injector to the player
	 * @param player who to inject
	 */
	void addInjector(Player player);

	/**
	 * Removes the injector from the player
	 * @param player player
	 */
	void removeInjector(Player player);

	/**
	 * Sends a sign to a player
	 * @param player player
	 * @param sign sign
	 */
	void sendSign(Player player, Sign sign);
}
