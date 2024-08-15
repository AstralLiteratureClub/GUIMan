package bet.astral.guiman.clickable;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {
	void run(@NotNull ClickContext clickContext);
}
