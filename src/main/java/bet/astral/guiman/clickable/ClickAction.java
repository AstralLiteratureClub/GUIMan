package bet.astral.guiman.clickable;

import bet.astral.messenger.v2.utils.MessageSender;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {
	static ClickAction message(MessageSender messageSender, TranslationKey translationKey){
		return clickContext -> messageSender.message(clickContext.getWho(), translationKey);
	}

	void run(@NotNull ClickContext clickContext);
}
