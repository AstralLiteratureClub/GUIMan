package bet.astral.signman;

import bet.astral.signman.wrapped.Handler;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.Consumer;

@Getter
public class SignGUI {
	protected static Handler packetHandler;
	public static void init(JavaPlugin plugin, boolean async){
		packetHandler = new Handler(plugin, async);
	}
	private final SignMaterial material;
	private final SignSize signSize;
	private final List<Component> lines;
	private final DyeColor color;
	private final SignHandler handler;
	private final ComponentSerializer<? extends Component, ? extends Component, String> serializer;
	private final Consumer<Player> openConsumer;

	public SignGUI(SignMaterial material, SignSize signSize, List<Component> lines, DyeColor color, SignHandler handler, ComponentSerializer<? extends Component, ? extends Component, String> serializer, Consumer<Player> openConsumer) {
		this.material = material;
		this.signSize = signSize;
		this.lines = lines;
		this.color = color;
		this.handler = handler;
		this.serializer = serializer;
		this.openConsumer = openConsumer;
	}

	public void open(Player player) {
		packetHandler.send(this, player);
	}

	public void accept(Player player, SignResult signResult) {
		handler.handle().forEach(action->action.run(player, signResult));
	}
}
