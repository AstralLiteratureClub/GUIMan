package bet.astral.signman;

import bet.astral.signman.wrapped.Handler;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

@Getter
public class SignGUI {
	private static Handler packetHandler;
	public static void init(JavaPlugin plugin, boolean async){
		packetHandler = new Handler(plugin, async);
	}
	private final SignMaterial material;
	private final SignSize signSize;
	private final List<Component> lines;
	private final DyeColor color;
	private final SignHandler handler;
	private final ComponentSerializer<? extends Component, ? extends Component, String> serializer;

	public SignGUI(SignMaterial material, SignSize signSize, List<Component> lines, DyeColor color, SignHandler handler, ComponentSerializer<? extends Component, ? extends Component, String> serializer) {
		this.material = material;
		this.signSize = signSize;
		this.lines = lines;
		this.color = color;
		this.handler = handler;
		this.serializer = serializer;
	}

	public void open(Player player) {
		packetHandler.send(this, player);
	}

	public void accept(Player player, SignResult signResult) {
		handler.handle(player, signResult).forEach(action->action.run(player, Arrays.asList(signResult.lines())));
	}
}