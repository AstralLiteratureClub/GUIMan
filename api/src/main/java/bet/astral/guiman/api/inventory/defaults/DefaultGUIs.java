package bet.astral.guiman.api.inventory.defaults;

import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.clickable.Clickable;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DefaultGUIs {
	public static InventoryGUI SLOTS_CHEST = InventoryGUI
		.builder()
		.setTitle(Component.text("Slots"))
		.setChestRows(ChestRows.SIX)
		.setShared(true)
		.setGeneratesItemsEachOpen(false)
		.generateClickables(() -> {
			Map<Integer, Collection<ClickableLike>> slots = new HashMap<>();
			for (int i = 0; i < ChestRows.SIX.getSlots(); i++) {
				int slot = i;
				slots.put(i, List.of(
					Clickable.builder(
						i % 2 == 0 ?
							Material.LIGHT_BLUE_STAINED_GLASS_PANE
							: Material.PINK_STAINED_GLASS_PANE
					).addGeneralAction(context -> {
						context.getPlayer().sendMessage(Component.text("You clicked on slot ", NamedTextColor.YELLOW).append(Component.text(slot, NamedTextColor.AQUA)));
					})
				));
			}
			return slots;
		})
		.build();
}
