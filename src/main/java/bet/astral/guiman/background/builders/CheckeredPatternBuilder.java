package bet.astral.guiman.background.builders;

import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class CheckeredPatternBuilder extends BackgroundBuilder {
	private int size = 1;
	private ClickableLike first = Clickable.EMPTY;
	private ClickableLike second = Clickable.EMPTY;

	public CheckeredPatternBuilder(){ }

	public CheckeredPatternBuilder chunkSize(int chunkSize){
		this.size = chunkSize;
		return this;
	}

	/**
	 * Makes the first chunk in checkered pattern use given clickable
	 * @param clickable clickable
	 * @return this
	 */
	@NotNull
	public CheckeredPatternBuilder first(@NotNull ClickableLike clickable){
		this.first = clickable;
		return this;
	}

	/**
	 * Makes the first chunk in checkered pattern use given material
	 * @param material material
	 * @return this
	 */
	@NotNull
	public CheckeredPatternBuilder first(@NotNull Material material){
		if (material == Material.AIR){
			this.first = Clickable.EMPTY;
			return this;
		}
		this.first = Clickable.empty(material);
		return this;
	}

	/**
	 * Makes the second chunk in checkered pattern use given clickable
	 * @param clickable clickable
	 * @return this
	 */
	@NotNull
	public CheckeredPatternBuilder second(@NotNull ClickableLike clickable){
		this.second = clickable;
		return this;
	}

	/**
	 * Makes the second chunk in checkered pattern use given material
	 * @param material material
	 * @return this
	 */
	@NotNull
	public CheckeredPatternBuilder second(@NotNull Material material){
		if (material == Material.AIR){
			this.second = Clickable.EMPTY;
			return this;
		}
		this.second = Clickable.empty(material);
		return this;
	}

	@Override
	public Background build() {
		PatternBackgroundBuilder backgroundBuilder = new PatternBackgroundBuilder()
				.clickable('a', first)
				.clickable('b', second);
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				if ((row / size + col / size) % 2 == 0) {
					backgroundBuilder.pattern(row, col, 'a');
				} else {
					backgroundBuilder.pattern(row, col, 'b');
				}
			}
		}
		return backgroundBuilder.build();
	}
}
