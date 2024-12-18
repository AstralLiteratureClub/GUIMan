package bet.astral.guiman.internals;


import bet.astral.guiman.GUIMan;
import bet.astral.guiman.gui.GlobalClickActionResult;
import bet.astral.guiman.gui.GlobalGUIClickAction;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.clickable.ClickAction;
import bet.astral.guiman.clickable.ClickContext;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
@ApiStatus.Internal
public class InventoryClickListener implements Listener {
	@EventHandler
	private void onClick(InventoryClickEvent event){
		if (!(event.getWhoClicked() instanceof Player player)){
			return;
		}
		if (event.getInventory().getHolder() instanceof InteractableGUI interactableGUI){
			InventoryGUI gui = interactableGUI.getGUI();
			event.setCancelled(true);


			ItemStack itemStack = event.getCurrentItem();
			int id = gui.getId(itemStack);
			if (id == Clickable.EMPTY.getId()) {
				run(gui, event, null);
				return;
			}

			Clickable clickable = gui.getIds().get(id);
			if (clickable == null){
				clickable = gui.getBackground().getSlotOrEmpty(event.getSlot()).asClickable();
				if (clickable.getId() == Clickable.EMPTY.getId()){
					run(gui, event, clickable);
					return;
				}
			}
			if (!hasPermission(player, clickable.getPermission(), gui)){
				clickable.sendPermissionMessage(player, gui.getMessenger());
				run(gui, event, clickable);
				return;
			}
			Map<ClickType, ClickAction> actions = clickable.getActions();
			ClickAction action = actions.get(event.getClick());
			if (action == null){
				run(gui, event, clickable);
				return;
			}
			ClickContext context = new ClickContext(gui, event.getInventory(), itemStack, event.getClick(), player, clickable,
					new ClickContext.MessengerInfo(gui.getMessenger(), gui.getMessenger() != null ? gui.getMessenger().convertReceiver(player) : null, gui.getMessenger() != null));
			if (clickable.isAsync()){
				CompletableFuture.runAsync(()->{
					try {
						action.run(context);
					} catch (Exception e){
						GUIMan.GUIMAN.getPlugin().getSLF4JLogger().error("Error accorded while trying to handle clickable", e);
					}
				});

				run(gui, event, clickable);
				return;
			}
			run(gui, event, clickable);
			action.run(context);
		}
	}

	private void run(InventoryGUI gui, InventoryClickEvent event, @Nullable Clickable clickable){
		if (gui.getGuiClickActions().get(event.getSlot())!=null){
			Map<ClickType, GlobalGUIClickAction> clickActionMap = gui.getGuiClickActions().get(event.getSlot());
			ClickType clickType = event.getClick();
			GlobalGUIClickAction action = clickActionMap.get(clickType);
			if (action != null){
				boolean isSameItem = event.getCursor().isSimilar(event.getCurrentItem());

				GlobalClickActionResult result = action.click(gui, event.getInventory(), event.getSlot(), event.getRawSlot(), clickable);
				if (result==GlobalClickActionResult.ALLOW_PICKUP_ALLOW_DROP){
					event.setCancelled(false);
				} else if (result==GlobalClickActionResult.ALLOW_PICKUP_DENY_DROP){
					event.setCancelled(false);
					if (!event.getCursor().isEmpty()){
						event.setCancelled(true);
					}
				} else if (result==GlobalClickActionResult.DENY_PICKUP_ALLOW_DROP){
					if (event.getCurrentItem() != null || isSameItem){
						event.setCancelled(true);
					}
				} else if (result==GlobalClickActionResult.DENY_PICKUP_DENY_DROP){
					event.setCancelled(true);
				}
			}
		}
	}

	private boolean hasPermission(@Nullable Player player, Permission permission, InventoryGUI gui){
		if (permission==null){
			return true;
		}
		if (player == null){
			return false;
		}
		return permission.hasPermission(player, gui);
	}

}
