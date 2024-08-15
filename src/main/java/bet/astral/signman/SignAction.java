package bet.astral.signman;

import bet.astral.guiman.gui.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@FunctionalInterface
public interface SignAction {
	default SignAction openInventory(InventoryGUI gui) {
		return (player, lines) -> gui.open(player);
	}
	default SignAction openInventory(Inventory inventory) {
		return (player, lines) -> player.getScheduler().run(SignGUI.packetHandler.getPlugin(), t->player.openInventory(inventory), null);
	}
	default SignAction openSign(SignGUI gui) {
		return (player, lines) -> gui.open(player);
	}

	default SignAction run(BiFunction<Player, SignResult, ?> function){
		return (function::apply);
	}
	default SignAction run(BiConsumer<Player, SignResult> consumer){
		return (consumer::accept);
	}

	void run(Player player, SignResult result);
}
