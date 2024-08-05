package bet.astral.signman;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SignResult {
	private final List<Component> lines;
	private final List<String> plainLines;

	public SignResult(@NotNull List<Component> components) {
		this.lines = components;
		this.plainLines = new ArrayList<>();
		for (Component component : components){
			plainLines.add(PlainTextComponentSerializer.plainText().serialize(component));
		}
	}

	@NotNull
	public Component getFirst(){
		return lines.getFirst();
	}
	@NotNull
	public Component getSecond(){
		return lines.get(1);
	}
	@NotNull
	public Component getThird(){
		return lines.get(2);
	}
	@NotNull
	public Component getFourth(){
		return lines.get(3);
	}

	@NotNull
	public String getFirstPlain(){
		return plainLines.getFirst();
	}
	@NotNull
	public String getSecondPlain(){
		return plainLines.get(1);
	}
	@NotNull
	public String getThirdPlain(){
		return plainLines.get(2);
	}
	@NotNull
	public String getFourthPlain(){
		return plainLines.get(3);
	}
}