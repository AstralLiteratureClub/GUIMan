package bet.astral.signman;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record SignResult(Component[] lines) {
	@NotNull
	@Contract(pure = true)
	Component line0() {
		return lines[0];
	}
	@NotNull
	@Contract(pure = true)
	Component line1(){
		return lines[1];
	}
	@NotNull
	@Contract(pure = true)
	Component line2(){
		return lines[2];
	}
	@NotNull
	@Contract(pure = true)
	Component line3(){
		return lines[3];
	}
}
