package bet.astral.guiman.api.inventory.clickable;

/**
 * Something is like a clickable and can be converted to a clickable
 */
public interface ClickableLike {
	/**
	 * Creates a new clickable instance from this
	 * @return clickable instance
	 */
	Clickable asClickable();
}
