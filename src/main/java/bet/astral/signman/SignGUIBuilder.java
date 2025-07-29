package bet.astral.signman;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class SignGUIBuilder {
	private static final Random random = new SecureRandom();
	private SignMaterial material = SignMaterial.OAK;
	private SignSize signSize = SignSize.NORMAL;
	private Map<Integer, Component> lines = new HashMap<>(4);
	private DyeColor color = DyeColor.BLACK;
	private SignHandler handler;
	private final ComponentSerializer<? extends Component, ? extends Component, String> componentSerializer;
	private Consumer<Player> openConsumer = null;

	public SignGUIBuilder(ComponentSerializer<? extends Component, ? extends Component, String> componentSerializer) {
		this.componentSerializer = componentSerializer;
	}

	public SignGUIBuilder() {
		this(PlainTextComponentSerializer.plainText());
	}

	public SignGUIBuilder setMaterial(SignMaterial material) {
		this.material = material;
		return this;
	}

	/**
	 * Sets the sign material to a random sign material out of all materials
	 * @param signMaterial sign material
	 * @return this
	 */
	public SignGUIBuilder setRandomMaterial(SignMaterial signMaterial) {
		this.material = SignMaterial.values()[random.nextInt(SignMaterial.values().length)];
		return this;
	}

	public SignGUIBuilder setSignSize(SignSize signSize) {
		this.signSize = signSize;
		return this;
	}

	public SignGUIBuilder setLinesStr(List<String> lines) {
		int i = 0;
		for (String line : lines){
			this.lines.put(i, componentSerializer.deserialize(line));
			i++;
		}
		return this;
	}
	public SignGUIBuilder setLines(String... lines) {
		int i = 0;
		for (String line : lines){
			this.lines.put(i, componentSerializer.deserialize(line));
			i++;
		}
		return this;
	}

	public SignGUIBuilder setLine(int line, String string) {
		this.lines.put(line, componentSerializer.deserialize(string));
		return this;
	}
	public SignGUIBuilder setLines(List<Component> lines) {
		int i = 0;
		for (Component line : lines){
			this.lines.put(i, line);
			i++;
		}
		return this;
	}
	public SignGUIBuilder setLines(Component... lines) {
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

	public SignGUI build() {
		return new SignGUI(material, signSize, lines.values().stream().toList(), color, handler, componentSerializer, openConsumer);
	}
}