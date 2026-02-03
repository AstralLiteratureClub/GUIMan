package bet.astral.guiman.background.builders;

import bet.astral.guiman.background.Background;

/**
 * Abstract background builder, to support multiple background builder types
 */
public abstract class BackgroundBuilder {
	/**
	 * Builds background using child class' implementation
	 * @return built background
	 */
	public abstract Background build();
}
