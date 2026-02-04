package bet.astral.guiman.api;

import bet.astral.guiman.api.annotations.UseMessenger;
import bet.astral.guiman.api.inventory.clickable.ClickableBuilder;
import bet.astral.guiman.api.inventory.gui.InventoryGUIBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GUIMan {
	protected static GUIMan GUIMAN;
	public static GUIMan getGUIMan() {
		return GUIMAN;
	}
	private final JavaPlugin plugin;
	private final boolean supportMessenger;
	@UseMessenger
	private final MessengerConfig messengerConfig;
	private final boolean disableMessengerWarning;

	public GUIMan(JavaPlugin plugin, boolean supportMessenger, MessengerConfig messengerConfig, boolean disableMessengerWarning) {
		this.plugin = plugin;
		this.supportMessenger = supportMessenger;
		this.messengerConfig = messengerConfig;
		this.disableMessengerWarning = disableMessengerWarning;
	}

	public void initialize() {
		plugin.getLogger().info("Enabling GUIMan...");
		initGUIMan();
		warnMessenger();

		plugin.getLogger().info("Enabled GUIMan!");
	}

	public void warnMessenger() {
		if (messengerConfig != null) {
			return;
		}
		if (disableMessengerWarning) {
			return;
		}
		plugin.getLogger().warning("--- GUIMAN ---");
		plugin.getLogger().warning("Messenger configuration is currently NULL in the GUIMan configuration page!");
		plugin.getLogger().warning("Any feature provided by messenger cannot be used!");
		plugin.getLogger().warning("Is messenger found: ");
		plugin.getLogger().warning("--------------");
	}

	private void initGUIMan() {
		try {
			Class<?> guimanInit = Class.forName("bet.astral.guiman.core.GUIManInitializer");
			Method method = guimanInit.getMethod("init", GUIMan.class);
			method.setAccessible(true);
			method.invoke(this);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public ClickableBuilder clickableBuilder(ItemStack itemStack) {
		return null;
	}

	public @NotNull InventoryGUIBuilder inventoryBuilder() {
		return null;
	}
}
