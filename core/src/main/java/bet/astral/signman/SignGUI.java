package bet.astral.signman;

import bet.astral.guiman.GUIMan;
import bet.astral.guiman.api.inventory.annotations.UseMessenger;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.guiman.api.sign.api.Sign;
import bet.astral.guiman.api.sign.api.material.ISignMaterial;
import bet.astral.guiman.api.sign.api.SignHandler;
import bet.astral.guiman.api.sign.api.SignResult;
import bet.astral.guiman.api.sign.api.material.SignMaterial;
import bet.astral.guiman.api.sign.api.material.SmallSignMaterial;
import bet.astral.guiman.api.sign.api.network.NetworkHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SignGUI implements Sign {
	protected static NetworkHandler packetHandler;
	public static void init(JavaPlugin plugin, boolean async){
		packetHandler = new NetworkHandler(plugin, async);
	}
	private final ISignMaterial material;
	private final List<Component> lines;
	@UseMessenger
	private final List<TranslationKey> translationKeyLines;
	private final DyeColor color;
	private final SignHandler handler;
	private final ComponentSerializer<? extends Component, ? extends Component, String> serializer;
	private final Consumer<Player> openConsumer;
	@UseMessenger
	private final Messenger messenger;
	@UseMessenger
	private final Function<Player, PlaceholderCollection> placeholderGenerator;

	public SignGUI(ISignMaterial material, List<Component> lines, List<TranslationKey> translationKeyLines, DyeColor color, SignHandler handler, ComponentSerializer<? extends Component, ? extends Component, String> serializer, Consumer<Player> openConsumer, Messenger messenger, Function<Player, PlaceholderCollection> placeholderGenerator) {
		this.material = material;
		this.lines = lines;
		this.translationKeyLines = translationKeyLines;
		this.color = color;
		this.handler = handler;
		this.serializer = serializer;
		this.openConsumer = openConsumer;
		this.messenger = messenger;
		this.placeholderGenerator = placeholderGenerator;
	}

	@Override
	public Consumer<Player> getOpenConsumer() {
		return openConsumer;
	}

	@Override
	public ISignMaterial getMaterial() {
		return material;
	}

	@Override
	public DyeColor getTextColor() {
		return color;
	}

	@Override
	public boolean isLargeSign() {
		return this.getMaterial() instanceof SignMaterial;
	}

	@Override
	public boolean isSmallSign() {
		if (GUIMan.isOrNever("1.20")) {
            return this.getMaterial() instanceof SmallSignMaterial;
        }
		return false;
	}

	@Override
	public List<Component> getLines() {
		return lines;
	}

	@Override
	public List<TranslationKey> getTranslationKeyLines() {
		return translationKeyLines;
	}

	@Override
	public SignHandler getHandler() {
		return handler;
	}

	@Override
	public ComponentSerializer<? extends Component, ? extends Component, String> getSerializer() {
		return serializer;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	@Override
	public Function<Player, PlaceholderCollection> getPlaceholderGenerator() {
		return placeholderGenerator;
	}

	@Override
	public void open(Player player) {
		packetHandler.send(this, player);
	}

	@Override
	public void sign(Player player, SignResult signResult) {
		handler.handle().forEach(action->action.run(player, signResult));
	}
}
