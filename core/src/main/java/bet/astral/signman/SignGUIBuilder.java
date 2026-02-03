package bet.astral.signman;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.guiman.api.sign.api.Sign;
import bet.astral.guiman.api.sign.api.SignHandler;
import bet.astral.guiman.api.sign.api.SignSize;
import bet.astral.guiman.api.sign.api.builder.SignBuilder;
import bet.astral.guiman.api.sign.api.material.ISignMaterial;
import bet.astral.guiman.api.sign.api.material.SignMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class SignGUIBuilder implements SignBuilder {
	private static final Random random = new SecureRandom();
	private ISignMaterial material = SignMaterial.OAK;
	private Map<Integer, Component> lines = new HashMap<>(4);
	private Map<Integer, TranslationKey> translationLines = new HashMap<>(4);
	private DyeColor color = DyeColor.BLACK;
	private SignHandler handler;
	private final ComponentSerializer<? extends Component, ? extends Component, String> componentSerializer;
	private Consumer<Player> openConsumer = null;

	private Messenger messenger = null;
	private Function<Player, PlaceholderCollection> placeholderGenerator = null;

	public SignGUIBuilder(ComponentSerializer<? extends Component, ? extends Component, String> componentSerializer) {
		this.componentSerializer = componentSerializer;
	}

	public SignGUIBuilder() {
		this(PlainTextComponentSerializer.plainText());
	}

	@Override
	public SignBuilder setMaterial(ISignMaterial material) {
		this.material = material;
		return this;
	}

	@Override
	public SignBuilder setRandomMaterial(SignSize size) {
		// TODO -> Complete this
//		this.material = SignMaterialReg.values()[random.nextInt(SignMaterialReg.values().length)];
		return this;
	}

	@Override
	public SignBuilder setRandomMaterial() {
		// TODO -> Complete this
		return this;
	}

	/**
	 * Sets the messenger used, when generating this sign and translating lines
	 * @param messenger messenger
	 * @return translator
	 */
	public SignGUIBuilder setMessenger(Messenger messenger) {
		this.messenger = messenger;
		return this;
	}

	/**
	 * Sets the placeholder generator used, when trying to generate the sign with messenger
	 * @param placeholderGenerator placeholder generator
	 * @return this
	 */
	public SignGUIBuilder setPlaceholderGenerator(Function<Player, PlaceholderCollection> placeholderGenerator) {
		this.placeholderGenerator = placeholderGenerator;
		return this;
	}

	@Override
	public SignBuilder setLinesPlain(@NotNull List<String> lines) {
		int i = 0;
		for (String line : lines){
			this.lines.put(i, componentSerializer.deserialize(line));
			i++;
		}
		return this;
	}

	@Override
	public SignBuilder setLinesPlain(String @NotNull ... lines) {
		int i = 0;
		for (String line : lines){
			this.lines.put(i, componentSerializer.deserialize(line));
			i++;
		}
		return this;
	}

	/**
	 * Sets the given lines to the given translations which are translated by the messenger
	 * @param lines lines
	 * @return this
	 */
	public SignGUIBuilder setLines(TranslationKey @NotNull ... lines) {
		int i = 0;
		for (TranslationKey line : lines){
			this.translationLines.put(i, line);
			i++;
		}
		return this;
	}

	/**
	 * Sets the `line` to the given translation which is translated using the provided messenger
	 * @param line line
	 * @param translationKey translation key
	 * @return this
	 */
	public SignGUIBuilder setLine(int line, TranslationKey translationKey) {
		this.translationLines.put(line, translationKey);
		return this;
	}

	@Override
	public SignBuilder setLinePlain(@Range(from = 1, to = 4) int line, String string) {
		this.lines.put(line, componentSerializer.deserialize(string));
		return this;
	}
	public SignGUIBuilder setLines(@NotNull List<Component> lines) {
		int i = 0;
		for (Component line : lines){
			this.lines.put(i, line);
			i++;
		}
		return this;
	}
	public SignGUIBuilder setLines(Component @NotNull ... lines) {
		int i = 0;
		for (Component line : lines){
			this.lines.put(i, line);
			i++;
		}
		return this;
	}

	public SignGUIBuilder setLine(int line, Component text) {
		this.lines.put(line, text);
		return this;
	}

	public SignGUIBuilder setColor(DyeColor color) {
		this.color = color;
		return this;
	}

	public SignGUIBuilder setHandler(SignHandler handler) {
		this.handler = handler;
		return this;
	}

	public SignGUIBuilder setOpenConsumer(Consumer<Player> openConsumer) {
		this.openConsumer = openConsumer;
		return this;
	}

	public Sign build() {
		return new SignGUI(
				material,
				new ArrayList<>(lines.values()),
				new ArrayList<>(translationLines.values()),
				color,
				handler,
				componentSerializer,
				openConsumer,
				messenger,
				placeholderGenerator
		);
	}
}