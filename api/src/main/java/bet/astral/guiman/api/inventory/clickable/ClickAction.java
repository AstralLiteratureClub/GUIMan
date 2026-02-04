package bet.astral.guiman.api.inventory.clickable;

import org.jetbrains.annotations.NotNull;

/**
 * Action ran when a player clicks a {@link Clickable clickable}
 */
@FunctionalInterface
public interface ClickAction {
	/**
	 * Action to run when clickable is clicked
	 * @param clickContext context of the click
	 */
    void run(@NotNull ClickContext clickContext);
}
