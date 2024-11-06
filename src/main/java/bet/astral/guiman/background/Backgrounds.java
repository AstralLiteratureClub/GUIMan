package bet.astral.guiman.background;

import bet.astral.guiman.utils.ChestRows;
import bet.astral.guiman.background.builders.BorderPatternBuilder;
import bet.astral.guiman.background.builders.CheckeredPatternBuilder;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Provides default backgrounds for guiman inventory GUIs
 * @deprecated Use {@link Background}
 */
@Deprecated(forRemoval = true)
public class Backgrounds {
	private Backgrounds() {
	}

	/**
	 * Empty background air as the background item
	 */
	public static Background EMPTY = new StaticBackground(Material.AIR);

	/**
	 * Returns single item background with given material.
	 *
	 * @param material material
	 * @return single item background
	 */
	public static Background single(Material material) {
		return new StaticBackground(material);
	}

	/**
	 * Returns single item background with given material.
	 *
	 * @param clickable material
	 * @return single item background
	 */
	public static Background single(ClickableLike clickable) {
		return new StaticBackground(clickable);
	}

	/**
	 * Creates a check board style background with chunk size being chunkSize*chunkSize,
	 * first clickable being "black" square and second being "white" square
	 *
	 * @param chunkSize square size
	 * @param first     fist clickable
	 * @param second    second clickable
	 * @return background
	 */
	public static Background checkered(int chunkSize, Material first, Material second) {
		return new CheckeredPatternBuilder().chunkSize(chunkSize).first(first).second(second).build();
	}

	/**
	 * Creates a check board style background with chunk size being chunkSize*chunkSize,
	 * first clickable being "black" square and second being "white" square
	 *
	 * @param chunkSize square size
	 * @param first     fist clickable
	 * @param second    second clickable
	 * @return background
	 */
	public static Background checkered(int chunkSize, ClickableLike first, ClickableLike second) {
		return new CheckeredPatternBuilder().chunkSize(chunkSize).first(first).second(second).build();
	}

	/**
	 * Creates a pattern for chest GUIs which have borders.
	 * For more customization of border pattern use {@link BorderPatternBuilder}.
	 * @param rows rows
	 * @param borderClickable border
	 * @param nonBorderClickable non border, empty slots
	 * @return new background
	 */
	public static Background border(@Range(from = 3, to = 6) int rows, @NotNull ClickableLike borderClickable, @NotNull ClickableLike nonBorderClickable){
		return new BorderPatternBuilder().rows(rows).border(borderClickable).nonBorder(nonBorderClickable).build();
	}

	/**
	 * Creates a pattern for chest GUIs which have borders.
	 * For more customization of border pattern use {@link BorderPatternBuilder}.
	 * @param rows rows
	 * @param borderClickable border
	 * @param nonBorderClickable non border, empty slots
	 * @return new background
	 */
	public static Background border(@Range(from = 3, to = 6) int rows, @NotNull Material borderClickable, @NotNull Material nonBorderClickable){
		return new BorderPatternBuilder().rows(rows).border(borderClickable).nonBorder(nonBorderClickable).build();
	}

	/**
	 * Creates a pattern for chest GUIs which have borders.
	 * For more customization of border pattern use {@link BorderPatternBuilder}.
	 * @param rows rows
	 * @param borderClickable border
	 * @param nonBorderClickable non border, empty slots
	 * @return new background
	 */
	public static Background border(@NotNull ChestRows rows, @NotNull ClickableLike borderClickable, @NotNull ClickableLike nonBorderClickable){
		return new BorderPatternBuilder().rows(rows).border(borderClickable).nonBorder(nonBorderClickable).build();
	}

	/**
	 * Creates a pattern for chest GUIs which have borders.
	 * For more customization of border pattern use {@link BorderPatternBuilder}.
	 * @param rows rows
	 * @param borderClickable border
	 * @param nonBorderClickable non border, empty slots
	 * @return new background
	 */
	public static Background border(@NotNull ChestRows rows, @NotNull Material borderClickable, @NotNull Material nonBorderClickable){
		return new BorderPatternBuilder().rows(rows).border(borderClickable).nonBorder(nonBorderClickable).build();
	}
}