package bet.astral.guiman.api.sign.api.network;

import bet.astral.guiman.api.sign.api.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NetworkHandler {
	private static Class<? extends NettyInjector> nettyInjector;
	public static void registerInjectorClass(Class<? extends NettyInjector> clazz) {
		nettyInjector = clazz;
	}
	private final JavaPlugin plugin;
	private final NettyInjector packetInjector;
	private final boolean async;

	public NetworkHandler(JavaPlugin plugin, boolean async) {
		this.plugin = plugin;
		this.async = async;
        try {
            Constructor<? extends NettyInjector> constructor = nettyInjector.getConstructor(JavaPlugin.class, boolean.class);
			this.packetInjector = constructor.newInstance(plugin, async);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
	}

	public void send(Sign gui, Player player){
		packetInjector.sendSign(player, gui);
	}
}
