package bet.astral.guiman.api.inventory.clickable;

import bet.astral.messenger.v2.Messenger;

/**
 * Messenger enhanced clickable context
 */
public interface MClickContext extends ClickContext {
	/**
	 * Returns the messenger used in GUIMan
	 * @return this
	 */
	Messenger getMessenger();
}
