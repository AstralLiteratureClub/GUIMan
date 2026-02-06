package bet.astral.guiman.api.inventory.background;

import bet.astral.guiman.api.inventory.clickable.ClickableLike;

public interface Background {
    boolean isEmpty();

	ClickableLike getSlotOrEmpty(int i);
}
