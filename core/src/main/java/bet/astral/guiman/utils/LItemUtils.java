package bet.astral.guiman.utils;

import bet.astral.guiman.api.inventory.modernized.ItemUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class LItemUtils implements ItemUtils {
    @Override
    public @NotNull ItemStack hideTooltip(@NotNull ItemStack item) {
        return item;
    }

    @Override
    public @NotNull ItemStack hideFlags(@NotNull ItemStack item) {
        item.addItemFlags(ItemFlag.values());
        return item;
    }

    @Override
    public @NotNull ItemStack hideDurability(@NotNull ItemStack item) {
        item.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
}
