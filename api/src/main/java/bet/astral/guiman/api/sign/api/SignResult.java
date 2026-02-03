package bet.astral.guiman.api.sign.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result when a player has signed the sign
 */
public class SignResult {
	private final List<Component> lines;
	private final List<String> plainLines;

	/**
	 * Creates a new instance using component lines
	 * @param lines lines
	 */
	public SignResult(@NotNull List<Component> lines) {
		this.lines = lines;
		this.plainLines = new ArrayList<>();
		for (Component component : lines){
			plainLines.add(PlainTextComponentSerializer.plainText().serialize(component));
		}
	}

	/**
	 * Returns the lines in the format they were given by the player.
	 * @return lines
	 */
	public List<Component> getLines() {
		return lines;
	}

	/**
	 * Returns the lines in a stripped fashion with no formatting codes
	 * @return lines
	 */
	public List<String> getPlainLines() {
		return plainLines;
	}

	/**
	 * Returns the first line
	 * @return first line
	 */
	@NotNull
	public Component getFirst(){
		return lines.getFirst();
	}
	/**
	 * Returns the second line
	 * @return second line
	 */
	@NotNull
	public Component getSecond(){
		return lines.get(1);
	}
	/**
	 * Returns the fourth line
	 * @return fourth line
	 */
	@NotNull
	public Component getThird(){
		return lines.get(2);
	}
	/**
	 * Returns the fourth line
	 * @return fourth line
	 */
	@NotNull
	public Component getFourth(){
		return lines.get(3);
	}
	/**
	 * Returns the first line which is stripped from formatting
	 * @return first line
	 */
	@NotNull
	public String getFirstPlain(){
		return plainLines.getFirst();
	}
	/**
	 * Returns the second line which is stripped from formatting
	 * @return second line
	 */
	@NotNull
	public String getSecondPlain(){
		return plainLines.get(1);
	}
	/**
	 * Returns the third line which is stripped from formatting
	 * @return third line
	 */
	@NotNull
	public String getThirdPlain(){
		return plainLines.get(2);
	}
	/**
	 * Returns the fourth line which is stripped from formatting
	 * @return fourth line
	 */
	@NotNull
	public String getFourthPlain(){
		return plainLines.get(3);
	}
}