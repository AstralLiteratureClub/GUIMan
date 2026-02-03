package bet.astral.guiman;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.signman.SignGUIBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        GUIMan.init(this);

        getServer().getCommandMap()
                .register(
                        "guiman",
                        new BukkitCommand("inventory") {
                            @Override
                            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
                                InventoryGUI.builder(ChestRows.SIX)
                                        .clickable(15, Clickable.builder(Material.STONE).actionGeneral(context->context.getWho().sendMessage("Hey!")))
                                        .build()
                                        .open((Player) sender);
                                return true;
                            }
                        }
                );

        getServer().getCommandMap()
                .register(
                        "guiman",
                        new BukkitCommand("sign") {
                            @Override
                            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
                                new SignGUIBuilder()
                                        .setLinesPlain(
                                                "",
                                                "^^^^^^^^",
                                                "Type something",
                                                ";)))")
                                        .setColor(DyeColor.RED)
                                        .setHandler(() -> List.of(
                                                (p, result) ->
                                                        p.sendMessage(Component.text("You have ").append(Component.text("signed", NamedTextColor.GREEN)
                                                                        .append(Component.text(" a sign!")))),
                                                (p, result) -> p.sendMessage(""),
                                                (p, result)->{
                                                    for (int i = 0; i < 4; i++){
                                                        p.sendMessage(Component.text((i+1) + ": ").append(result.getLines().get(i)));
                                                    }
                                                }
                                        )).build().open((Player) sender);
                                return true;
                            }
                        }
                );

    }
}
