package bet.astral.guiman.api.inventory.modernized;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemUtils {
    @NotNull
    ItemStack hideTooltip(@NotNull ItemStack item);

    @NotNull
    ItemStack hideFlags(@NotNull ItemStack item);

    @NotNull
    ItemStack hideDurability(@NotNull ItemStack item);
}
