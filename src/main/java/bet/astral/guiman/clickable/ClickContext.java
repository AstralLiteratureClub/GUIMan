package bet.astral.guiman.clickable;

import bet.astral.guiman.InventoryGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public final class ClickContext {
	private final InventoryGUI gui;
	private final Inventory inventory;
	private final ItemStack itemStack;
	private final ClickType clickType;
	private final Player who;
	private final Clickable clickable;
}
