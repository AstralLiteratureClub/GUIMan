package bet.astral.guiman.api.inventory.annotations;

import java.lang.annotation.Documented;

/**
 * Methods, classes, fields annotated with this annotation use messenger.
 * {@link bet.astral.messenger.v2.Messenger} is message translation and messaging system built for modern minecraft.
 * It reads messages from a file or from a message source defined by the user.
 */
@Documented
public @interface UseMessenger {
}
