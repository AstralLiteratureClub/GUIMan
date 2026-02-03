package bet.astral.signman.v1_20_5.network;

import bet.astral.guiman.api.sign.api.network.NettyHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class NettyHandler_v1_20_5 extends ChannelDuplexHandler implements NettyHandler {
	private final JavaPlugin plugin;
	private final boolean async;
	protected static final Map<UUID, Predicate<Packet<?>>> PACKET_HANDLERS = new HashMap<>();
	private final Player p;

	public NettyHandler_v1_20_5(JavaPlugin plugin, boolean async, Player p) {
		this.plugin = plugin;
		this.async = async;
		this.p = p;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!(msg instanceof Packet<?> packet)) {
			super.channelRead(ctx, msg);
			return;
		}

		Predicate<Packet<?>> handler = PACKET_HANDLERS.get(p.getUniqueId());
		if (handler != null) {
			BukkitRunnable runnable = new BukkitRunnable() {
				public void run() {
					boolean success = handler.test(packet);
					if (success)
						PACKET_HANDLERS.remove(p.getUniqueId());
				}
			};
			if (async()) {
				runnable.runTaskAsynchronously(getPlugin());
			} else {
				runnable.runTask(getPlugin());
			}
		}

		super.channelRead(ctx, msg);
	}

	public boolean async() {
		return async;
	}

	public Player getTarget() {
		return p;
	}

	@Override
	public JavaPlugin getPlugin() {
		return plugin;
	}

	@Override
	public boolean isASync() {
		return async;
	}

	@Override
	public Player getPlayer() {
		return p;
	}
}
