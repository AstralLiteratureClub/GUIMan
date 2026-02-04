package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.core.inventory.clickable.Clickable;
import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface GlobalGUIClickAction {
     GlobalClickActionResult click(@NotNull InventoryGUI openGUI, @NotNull Inventory clickedInventory, int slot, int rawSlot, @Nullable Clickable clickable);
}
