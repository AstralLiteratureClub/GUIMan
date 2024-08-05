package bet.astral.signman.wrapped;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class PacketHandler extends ChannelDuplexHandler {
	@Getter
	private final JavaPlugin plugin;
	private final boolean async;
	protected static final Map<UUID, Predicate<Packet<?>>> PACKET_HANDLERS = new HashMap<>();
	private final Player p;
	public

	PacketHandler(JavaPlugin plugin, boolean async, Player p) {
		this.plugin = plugin;
		this.async = async;
		this.p = p;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!(msg instanceof Packet<?> packet)) { // Utilize Java 17 features for pattern matching; Only intercept Packet Data
			super.channelRead(ctx, msg);
			return;
		}

		Predicate<Packet<?>> handler = PACKET_HANDLERS.get(p.getUniqueId());
		if (handler != null) {
			BukkitRunnable runnable = new BukkitRunnable() {
				public void run() {
					boolean success = handler.test(packet); // Check to make sure that the predicate works
					if (success)
						PACKET_HANDLERS.remove(p.getUniqueId()); // If successful, remove the packet handler
				}
			};
			if (async()) {
				runnable.runTaskAsynchronously(getPlugin());
			} else {
				runnable.runTask(getPlugin());
			}
		}

		super.channelRead(ctx, msg); // Perform default actions done by the duplex handler
	}

	public boolean async() {
		return async;
	}

	public Player getTarget() {
		return p;
	}
}
