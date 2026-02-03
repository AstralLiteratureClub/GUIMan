package bet.astral.signman;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SignMan {
    private static JavaPlugin plugin;
    public static void init(JavaPlugin plugin) {
        SignMan.plugin = plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
