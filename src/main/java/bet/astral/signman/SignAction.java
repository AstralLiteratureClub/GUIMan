package bet.astral.signman;

import bet.astral.guiman.InventoryGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

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

	void run(Player player, List<Component> lines);
}
