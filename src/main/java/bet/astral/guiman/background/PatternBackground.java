package bet.astral.guiman.background;

import bet.astral.guiman.clickable.ClickableLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Background implementation with a pattern which can be defined by {@link bet.astral.guiman.background.builders.PatternBackgroundBuilder}
 */
public class PatternBackground extends Background{
	@NotNull
	private final Map<Character, ClickableLike> clickables;
	@NotNull
	private final Map<Integer, Character> slots;

	/**
	 * Creates a new instance of the pattern background. For easy implementation use {@link bet.astral.guiman.background.builders.PatternBackgroundBuilder}
	 * @param empty air slot, clickable
	 * @param clickables char definitions
	 * @param slots slot definitions
	 */
	public PatternBackground(@NotNull ClickableLike empty, @NotNull Map<Character, ClickableLike> clickables, @NotNull Map<Integer, Character> slots) {
		super(empty);
		this.clickables = clickables;
		this.slots = slots;
	}

	@Override
	public @NotNull Optional<@Nullable ClickableLike> getSlot(int slot) {
		return Optional.empty();
	}

	@Override
	public @NotNull ClickableLike getSlotOrEmpty(int slot) {
		Character character = slots.get(slot);
		ClickableLike clickable = clickables.get(character);
		return clickable != null ? clickable : getEmpty();
	}
}
