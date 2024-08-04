package bet.astral.signman;

import bet.astral.guiman.InventoryGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@FunctionalInterface
public interface SignAction {
	default SignAction openInventory(InventoryGUI gui) {
		return (player, lines) -> gui.generateInventory(player);
	}
	default SignAction openInventory(Inventory inventory) {
		return (player, lines) -> player.getScheduler().run(SignGUI.packetHandler.getPlugin(), t->player.openInventory(inventory), null);
	}
	default SignAction openSign(SignGUI gui) {
		return (player, lines) -> gui.open(player);
	}

	default SignAction run(BiFunction<Player, List<Component>, ?> function){
		return (function::apply);
	}
	default SignAction run(BiConsumer<Player, List<Component>> consumer){
		return (consumer::accept);
	}

	void run(Player player, List<Component> lines);
}
