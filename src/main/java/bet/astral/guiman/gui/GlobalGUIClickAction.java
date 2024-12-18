package bet.astral.guiman.gui;

import bet.astral.guiman.clickable.Clickable;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface GlobalGUIClickAction {
     GlobalClickActionResult click(@NotNull InventoryGUI openGUI, @NotNull Inventory clickedInventory, int slot, int rawSlot, @Nullable Clickable clickable);
}
