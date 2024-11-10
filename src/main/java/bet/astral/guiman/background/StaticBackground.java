package bet.astral.guiman.background;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * Background implementation with only one item as the background.
 */
public class StaticBackground extends Background{
	private final boolean isEmpty;
	/**
	 * Creates a new instance of a static background.
	 * Creates clickable using {@link Clickable#noTooltip(Material)} and with first param.
	 * @param clickable clickable
	 */
	public StaticBackground(@NotNull Material clickable) {
		super(Clickable.builder(clickable, meta->{
			if (!clickable.isAir()) {
				meta.setHideTooltip(true);
			}
		}).build());
		isEmpty = clickable.isEmpty();
	}
	/**
	 * Creates a new instance of a static background.
	 * @param clickable clickable
	 */
	protected StaticBackground(@NotNull ClickableLike clickable) {
		super(clickable);
		isEmpty = false;
	}

	/**
	 * Creates a new instance of a static background.
	 * @param clickable clickable
	 */
	protected StaticBackground(@NotNull ClickableLike clickable, boolean isEmpty) {
		super(clickable);
		this.isEmpty = isEmpty;
	}

	@Override
	public @NotNull Optional<@Nullable ClickableLike> getSlot(int slot) {
		return Optional.of(getEmpty());
	}

	@Override
	public @NotNull ClickableLike getSlotOrEmpty(int slot) {
		return getEmpty();
	}

	@Override
	public @NotNull Iterator<ClickableLike> iterator() {
		return Collections.nCopies(54, super.getEmptySlot()).iterator();
	}

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}
}
