package bet.astral.guiman;

import bet.astral.guiman.internals.InventoryClickListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIMan {
	public static GUIMan GUIMAN;
	@Getter
	private JavaPlugin plugin;
	public static void init(JavaPlugin javaPlugin){
		GUIMAN = new GUIMan(javaPlugin);
		javaPlugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(), javaPlugin);
	}

	private GUIMan(JavaPlugin javaPlugin) {
		this.plugin = javaPlugin;
	}
}
