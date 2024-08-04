package bet.astral.signman.wrapped;

import bet.astral.signman.SignGUI;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Handler {
	private final JavaPlugin plugin;
	private final PacketInjector packetInjector;
	private final boolean async;

	public Handler(JavaPlugin plugin, boolean async) {
		this.plugin = plugin;
		this.async = async;
		this.packetInjector = new PacketInjector(plugin, async);
	}

	public void send(SignGUI gui, Player player){
		packetInjector.sendSign(player, gui);
	}
}
