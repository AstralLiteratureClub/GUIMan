package bet.astral.guiman.api.sign.api;

import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import bet.astral.signman.SignMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * An action executed when the sign is signed by the player
 */
@FunctionalInterface
public interface SignAction {
	/**
	 * Opens an inventory GUI made using GUIMan.
	 * @param gui gui
	 * @return new action
	 */
	@Contract(pure = true)
	static @NotNull SignAction openInventory(InventoryGUI gui) {
		return (player, lines) -> gui.open(player);
	}

	/**
	 * Opens an inventory after the sign is signed. Using this method ensures the server does not have weird bugs with the inventory
	 * @param inventory inventory to open
	 * @return new action
	 */
	@Contract(pure = true)
	static @NotNull SignAction openInventory(Inventory inventory) {
		return (player, lines) -> Bukkit.getScheduler().runTask(SignMan.getPlugin(), t->player.openInventory(inventory));
	}

	/**
	 * Opens a new sign to the player
	 * @param gui gui
	 * @return new action
	 */
	@Contract(pure = true)
	static @NotNull SignAction openSign(Sign gui) {
		return (player, lines) -> gui.open(player);
	}

	/**
	 * Runs a bifunction when the sign is signed
	 * @param function function
	 * @return new action
	 */
	@Contract(pure = true)
	static @NotNull SignAction run(@NotNull BiFunction<Player, SignResult, ?> function){
		return (function::apply);
	}

	/**
	 * Runs a biconsumer when the sign is signed
	 * @param consumer consumer
	 * @return new action
	 */
	@Contract(pure = true)
	static @NotNull SignAction run(@NotNull BiConsumer<Player, SignResult> consumer){
		return (consumer::accept);
	}

	/**
	 * Action to run when the sign is signed by the player
	 * @param player player that signed it
	 * @param result result of the signing
	 */
	void run(Player player, SignResult result);
}
