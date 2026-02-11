package bet.astral.guiman.core;

import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.GUIManHandler;
import bet.astral.guiman.api.inventory.ItemUtils;
import bet.astral.guiman.core.inventory.listeners.InventoryClickListener;
import bet.astral.guiman.core.inventory.utils.LItemUtils;
import bet.astral.guiman.core.sign.SignGUI;
import bet.astral.guiman.core.sign.SignMaterialReg;
import bet.astral.guiman.api.sign.api.network.NetworkHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GUIManInitializer {
	private static ItemUtils utils;
	public static boolean isModernItemSupport() {
		String version = Bukkit.getServer().getMinecraftVersion();
		String[] split = version.split("\\.");

		try {
			int major = Integer.parseInt(split[0]);

			// Year-based versioning: 26.x, 27.x etc.
			if (major >= 20) return true;

			// Legacy versioning
			if (major == 1) {
				int minor = Integer.parseInt(split[1]);

				// Any 1.21+ is modern
				if (minor > 20) return true;

				// 1.20.5 and 1.20.6 -> New itemstack components in paper
				if (minor == 20 && split.length > 2) {
					int patch = Integer.parseInt(split[2]);
					return patch >= 5;
				}
			}

		} catch (NumberFormatException e) {
			// New minecraft version due to unknown versioning
			return true;
		}

		return false;
	}

	public static boolean isOrNever(String targetVersion) {
		String current = Bukkit.getServer().getMinecraftVersion();

		try {
			int[] cur = parseVersion(current);
			int[] tgt = parseVersion(targetVersion);

			for (int i = 0; i < 3; i++) {
				if (cur[i] > tgt[i]) return true;
				if (cur[i] < tgt[i]) return false;
			}

			return true;

		} catch (Exception e) {
			return true;
		}
	}

	private static int @NotNull [] parseVersion(@NotNull String version) {
		String[] split = version.split("\\.");
		int[] out = new int[] {0, 0, 0};

		for (int i = 0; i < Math.min(split.length, 3); i++) {
			out[i] = Integer.parseInt(split[i]);
		}

		return out;
	}


	public static ItemUtils getItemUtils() {
		if (utils == null) {
			if (isModernItemSupport()) {
                try {
                    utils = (ItemUtils) Class.forName("bet.astral.guiman.modern.utils.MItemUtils").getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
				utils = new LItemUtils();
			}
		}
		return utils;
	}

	private static void initSignMan(JavaPlugin plugin) {
		boolean async = false;

		String clazzName = "bet.astral.signman.%s.network.NettyInjector_%s";
		String version = switch (Bukkit.getMinecraftVersion()) {
			case "1.20.5", "1.20.6",
				 "1.21.1", "1.21.3", "1.21.4",
				 "1.21.5", "1.21.6", "1.21.7",
				 "1.21.8", "1.21.9", "1.21.10",
				 "1.21.11" -> "v1_20_5";
			default -> throw new RuntimeException("Unsupported minecraft version");
		};
        try {
            NetworkHandler.registerInjectorClass(classForName(clazzName.formatted(version, version)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

		SignGUI.init(plugin, false);
		SignMaterialReg.init();
	}

	private static <T> @NotNull Constructor<? extends T> constructor(@NotNull Constructor<T> constructor, Object... values) {
		try {
			return (Constructor<? extends T>) constructor.newInstance(values);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
    }

	private static <T> @NotNull Constructor<? extends T> constructor(@NotNull Class<T> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
	private static <T> @NotNull Class<? extends T> classForName(String name) throws ClassNotFoundException {
		return (Class<? extends T>) Class.forName(name);
	}

	public static GUIManInitializer GUIMAN;
	@Getter
	private JavaPlugin plugin;
	public static void init(bet.astral.guiman.api.@NotNull GUIMan guiMan) {
		guiMan.getPlugin().getServer().getPluginManager().registerEvents(new InventoryClickListener(), guiMan.getPlugin());
		initSignMan(guiMan.getPlugin());

		try {
			Class.forName("bet.astral.messenger.v2.utils.MessageSender");
			GUIMan.register(new GUIManHandlerImpl());
		} catch (ClassNotFoundException e) {
			GUIMan.register(new GUIManMessengerHandlerImpl());
		}
	}
	private GUIManInitializer(JavaPlugin javaPlugin) {
		this.plugin = javaPlugin;
	}
}
