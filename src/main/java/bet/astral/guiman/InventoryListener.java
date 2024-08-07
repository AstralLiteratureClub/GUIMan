package bet.astral.guiman;


import bet.astral.guiman.permission.Permission;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class InventoryListener implements Listener {
	@EventHandler
	private void onClick(InventoryClickEvent event){
		if (!(event.getWhoClicked() instanceof Player player)){
			return;
		}
		if (event.getInventory().getHolder() instanceof InteractableGUI interactableGUI){
			event.setCancelled(true);
			ItemStack itemStack = event.getCurrentItem();
			InventoryGUI gui = interactableGUI.getCore();
			int id = gui.getId(itemStack);
			if (id == Clickable.EMPTY.getId()){
				return;
			}


			Clickable clickable = gui.getIds().get(id);
			if (clickable == null){
				return;
			}
			if (!hasPermission(player, clickable.getPermission())){
				player.sendMessage(Clickable.permissionMessage);
				return;
			}
			TriConsumer<Clickable, ItemStack, Player> consumer = clickable.getActions().get(event.getClick());
			if (consumer == null){
				return;
			}
			if (clickable.isAsync()){
				CompletableFuture.runAsync(()->{
					consumer.accept(clickable, itemStack, player);
				});
				return;
			}
			consumer.accept(clickable, itemStack, player);
		}
	}

	private boolean hasPermission(@Nullable Player player, Permission permission){
		if (player == null){
			return true;
		}
		return permission.hasPermission(player);
	}

}
