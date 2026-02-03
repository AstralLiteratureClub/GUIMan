package bet.astral.guiman.api.sign.api;

import java.util.List;

/**
 * Handler used when the sign is signed by the player
 */
@FunctionalInterface
public interface SignHandler {
	/**
	 * Handles the sign completion
	 * @return actions for signing the sign
	 */
	List<SignAction> handle();
}
