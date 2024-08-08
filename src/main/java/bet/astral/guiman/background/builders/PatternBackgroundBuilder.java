package bet.astral.guiman.background.builders;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.PatternBackground;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;

public class PatternBackgroundBuilder extends BackgroundBuilder {
	private ClickableLike empty = Clickable.EMPTY;
	private final Map<Character, ClickableLike> clickables = new HashMap<>();
	private final Map<Integer, Character> slots = new HashMap<>();

	public PatternBackgroundBuilder() {
	}

	/**
	 * Marks given character to use given clickable when building the background pattern
	 * @param character character
	 * @param clickable clickable
	 * @return this
	 */
	@Contract("_, _ -> this")
	public PatternBackgroundBuilder clickable(char character, @NotNull ClickableLike clickable){
		this.clickables.put(character, clickable);
		return this;
	}

	/**
	 * Marks given character to use given material as the clickable when building the background pattern
	 * @param character character
	 * @param material material
	 * @return this
	 */
	@Contract("_, _ -> this")
	public PatternBackgroundBuilder clickable(char character, @NotNull Material material){
		if (material==Material.AIR){
			this.clickables.put(character, Clickable.EMPTY);
			return this;
		}
		this.clickables.put(character, Clickable.empty(material));
		return this;
	}

	/**
	 * Makes the slots given with the pattern use given pattern; other slots are ignored
	 * @param pattern patterns
	 * @return this
	 */
	@Contract("_ -> this")
	public PatternBackgroundBuilder pattern(String... pattern){
		for (int i = 0; i < pattern.length; i++){
			handlePattern(i, pattern[i]);
		}
		return this;
	}

	/**
	 * Makes given row of slots use given the pattern
	 * @param row row
	 * @param pattern pattern
	 * @return this
	 */
	@Contract("_, _ -> this")
	public PatternBackgroundBuilder pattern(@Range(from = 0, to = 6) int row, @NotNull String pattern){
		handlePattern(row, pattern);
		return this;
	}

	/**
	 * Makes given row of slots use given the pattern
	 * @param row row (up -> down)
	 * @param col col (left -> right)
	 * @param character char
	 * @return this
	 */
	@Contract("_, _, _ -> this")
	public PatternBackgroundBuilder pattern(@Range(from = 0, to = 6) int row, @Range(from = 0, to = 9) int col, char character){
		slots.put((row*9)+col, character);
		return this;
	}

	private void handlePattern(int row, @NotNull String pattern){
		int slot = row*9;
		String[] split = pattern.split("");
		for (String val : split){
			slots.put(slot, val.charAt(0));
			slot++;
		}
	}


	/**
	 * Sets material, which is used when no item is found in the pattern
	 * @param clickable clickable
	 * @return this
	 */
	@Contract("_, -> this")
	public PatternBackgroundBuilder empty(@NotNull ClickableLike clickable){
		this.empty = clickable;
		return this;
	}
	/**
	 * Sets material, which is used when no item is found in the pattern
	 * @param material material
	 * @return this
	 */
	@Contract("_, -> this")
	public PatternBackgroundBuilder empty(Material material){
		if (material == Material.AIR){
			empty = Clickable.EMPTY;
		} else {
			this.empty = Clickable.empty(material);
		}
		return this;
	}

	@Override
	public Background build() {
		return new PatternBackground(empty, clickables, slots);
	}
}