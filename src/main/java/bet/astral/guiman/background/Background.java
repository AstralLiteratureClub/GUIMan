package bet.astral.guiman.background;

import bet.astral.guiman.clickable.ClickableLike;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@ApiStatus.OverrideOnly
@SuppressWarnings("JavadocDeclaration")
public abstract class Background {
	/**
	 *  Returns the clickable associated with empty slots.
	 * @return clickable as
	 */
	@NotNull
	private final ClickableLike empty;

	/**
	 * Creates a new background instance
	 * @param empty empty slot clickable
	 */
	protected Background(@NotNull ClickableLike empty) {
		this.empty = empty;
	}

	/**
	 * Returns clickable associated with given slot
	 * @param slot slot
	 * @return clickable
	 */
	@NotNull
	public abstract Optional<@Nullable ClickableLike> getSlot(int slot);

	/**
	 * Returns background associated with given slot, or {@link Background#getEmpty()}
	 * @param slot slot
	 * @return clickable
	 */
	@NotNull
	public abstract ClickableLike getSlotOrEmpty(int slot);
}
