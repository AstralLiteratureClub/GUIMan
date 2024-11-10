package bet.astral.guiman.background;

import bet.astral.guiman.background.builders.BorderPatternBuilder;
import bet.astral.guiman.background.builders.CheckeredPatternBuilder;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.utils.ChestRows;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;

@Getter
public abstract class Background implements Iterable<ClickableLike> {
	/**
	 *  Returns the clickable associated with empty slots.
	 */
	@NotNull
	@Deprecated
	private final ClickableLike empty;
	private final ClickableLike emptySlot;

	/**
	 * Creates a new background instance
	 * @param empty empty slot clickable
	 */
	protected Background(@NotNull ClickableLike empty) {
		this.empty = empty;
		this.emptySlot = empty;
	}

	/**
	 * Returns clickable associated with given slot
	 * @param slot slot
	 * @return clickable
	 */
	@NotNull
	public abstract Optional<@Nullable ClickableLike> getSlot(int slot);

	/**
	 * Returns background associated with given slot, or {@link Background#getEmpty()}
	 * @param slot slot
	 * @return clickable
	 */
	@NotNull
	public abstract ClickableLike getSlotOrEmpty(int slot);

	/**
	 * Empty background air as the background item
	 */
	public static Background EMPTY = noTooltip(Material.AIR);

	/**
	 * Returns single item background with given material.
	 *
	 * @param material material
	 * @return single item background
	 */
	public static Background tooltip(Material material) {
		return new StaticBackground(material);
	}
	/**
	 * Returns single item background with given material.
	 *
	 * @param material material
	 * @return single item background
	 */
	public static Background tooltip(ItemStack material) {
		return new StaticBackground(Clickable.builder(material), material.isEmpty());
	}

	/**
	 * Returns single item background with given material.
	 *
	 * @param clickable material
	 * @return single item background
	 */
	public static Background tooltip(ClickableLike clickable) {
		return new StaticBackground(clickable);
	}

	/**
	 * Returns single item background with given material and without hover tooltip.
	 *
	 * @param material material
	 * @return single item background
	 */
	public static Background noTooltip(Material material) {
		return noTooltip(ItemStack.of(material));
	}
	/**
	 * Returns single item background with given material and without hover tooltip.
	 *
	 * @param material material
	 * @return single item background
	 */
	public static Background noTooltip(ItemStack material) {
		return new StaticBackground(Clickable.noTooltip(material), material.isEmpty());
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

	/**
	 * Returns true if the inventory is made of air and contains nothing else.
	 * @return true if air and no actions found
	 */
	public abstract boolean isEmpty();
}
