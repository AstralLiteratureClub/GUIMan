package bet.astral.guiman.background;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Background implementation with only one item as the background.
 */
public class StaticBackground extends Background{
	/**
	 * Creates a new instance of a static background.
	 * Creates clickable using {@link Clickable#empty(Material)} and with first param.
	 * @param clickable clickable
	 */
	public StaticBackground(@NotNull Material clickable) {
		super(new ClickableBuilder(clickable, meta->{
			if (!clickable.isAir()) {
				meta.setHideTooltip(true);
			}
		}).build());
	}
	/**
	 * Creates a new instance of a static background.
	 * @param clickable clickable
	 */
	public StaticBackground(@NotNull ClickableLike clickable) {
		super(clickable);
	}

	@Override
	public @NotNull Optional<@Nullable ClickableLike> getSlot(int slot) {
		return Optional.of(getEmpty());
	}

	@Override
	public @NotNull ClickableLike getSlotOrEmpty(int slot) {
		return getEmpty();
	}
}
