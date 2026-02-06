package bet.astral.guiman.api;

import bet.astral.guiman.api.annotations.UseMessenger;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.Messenger;

@UseMessenger
public class MessengerConfig {
	private final Messenger messenger;;
	private final PlaceholderGenerator globalPlaceholderGenerator;

	@UseMessenger
	public MessengerConfig(Messenger messenger, PlaceholderGenerator globalPlaceholderGenerator) {
		this.messenger = messenger;
		this.globalPlaceholderGenerator = globalPlaceholderGenerator;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public PlaceholderGenerator getPlaceholderGenerator() {
		return globalPlaceholderGenerator;
	}
}
