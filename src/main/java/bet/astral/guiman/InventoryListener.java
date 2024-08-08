package bet.astral.guiman;


import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.permission.Permission;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
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
			InventoryGUI gui = interactableGUI.getGUI();
			int id = gui.getId(itemStack);
			if (id == Clickable.EMPTY.getId()) {
				return;
			}

			Clickable clickable = gui.getIds().get(id);
			if (clickable == null){
				clickable = gui.getBackground().getSlotOrEmpty(event.getSlot()).asClickable();
				if (clickable.getId() == Clickable.EMPTY.getId()){
					return;
				}
			}
			if (!hasPermission(player, clickable.getPermission())){
				clickable.sendPermissionMessage(player, gui.getMessenger());
				return;
			}
			Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions = clickable.getActions();
			TriConsumer<Clickable, ItemStack, Player> consumer = clickable.getActions().get(event.getClick());
			if (consumer == null){
				return;
			}
			if (clickable.isAsync()){
				final Clickable finalClickable = clickable;
				CompletableFuture.runAsync(()->{
					try {
						consumer.accept(finalClickable, itemStack, player);
					} catch (Exception e){
						InventoryGUI.PLUGIN.getSLF4JLogger().error("Error accorded while trying to handle clickable", e);
					}
				});
				return;
			}
			consumer.accept(clickable, itemStack, player);
		}
	}

	private boolean hasPermission(@Nullable Player player, Permission permission){
		if (permission==null){
			return true;
		}
		if (player == null){
			return true;
		}
		return permission.hasPermission(player);
	}

}
