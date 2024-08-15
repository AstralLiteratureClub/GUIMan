package bet.astral.guiman.background.builders;

import bet.astral.guiman.utils.ChestRows;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.ClickableLike;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Background builder for easier border backgrounds for chest GUIs
 */
public class BorderPatternBuilder extends BackgroundBuilder{
	private ChestRows rows = ChestRows.SIX;
	private int inwards = 0;
	private boolean topBorder = true;
	private boolean leftBorder = true;
	private boolean rightBorder = true;
	private boolean bottomBorder = true;
	private ClickableLike border = Clickable.EMPTY;
	@Nullable
	private ClickableLike borderSecond;
	private ClickableLike other = Clickable.EMPTY;

	/**
	 * Sets the clickable for border item
	 *
	 * @param border the new value of border
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder border(@NotNull ClickableLike border) {
		this.border = border;
		return this;
	}

	/**
	 * Sets the clickable for border item
	 *
	 * @param border the new value of border
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder border(@NotNull Material border) {
		if (border==Material.AIR){
			this.border = Clickable.EMPTY;
			return this;
		}
		this.border = Clickable.noTooltip(border);
		return this;
	}

	/**
	 * Sets the clickable for border item
	 *
	 * @param border the new value of border
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder borderSecond(@NotNull ClickableLike border) {
		this.borderSecond = border;
		return this;
	}

	/**
	 * Sets the clickable for border item
	 *
	 * @param border the new value of border
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder borderSecond(@NotNull Material border) {
		if (border==Material.AIR){
			this.borderSecond = Clickable.EMPTY;
			return this;
		}
		this.borderSecond = Clickable.noTooltip(border);
		return this;
	}


	/**
	 * Sets the clickable for non-border slots
	 *
	 * @param other the new value of other
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder nonBorder(@NotNull ClickableLike other) {
		this.other = other;
		return this;
	}

	/**
	 * Sets the clickable for non-border slots
	 *
	 * @param other the new value of other
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder nonBorder(@NotNull Material other) {
		if (other == Material.AIR){
			this.other = Clickable.EMPTY;
			return this;
		}
		this.other = Clickable.noTooltip(other);
		return this;
	}

	/**
	 * Sets how much the border will go inwards from the border, 0 to render border at the real border
	 *
	 * @param inwards the new value of inwards
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder inwards(int inwards) {
		this.inwards = inwards;
		return this;
	}

	/**
	 * Sets if the top border should be rendered or not
	 *
	 * @param topBorder the new value of topBorder
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder topBorder(boolean topBorder) {
		this.topBorder = topBorder;
		return this;
	}

	/**
	 * Sets if the left border should be rendered or not
	 *
	 * @param leftBorder the new value of leftBorder
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder leftBorder(boolean leftBorder) {
		this.leftBorder = leftBorder;
		return this;
	}

	/**
	 * Sets if the right border should be rendered or not
	 *
	 * @param rightBorder the new value of rightBorder
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder rightBorder(boolean rightBorder) {
		this.rightBorder = rightBorder;
		return this;
	}

	/**
	 * Sets the value of the bottomBorder.
	 *
	 * @param bottomBorder the new value of bottomBorder
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder bottomBorder(boolean bottomBorder) {
		this.bottomBorder = bottomBorder;
		return this;
	}

	/**
	 * Sets the size of the chest gui. Depends on each GUI, generates only properly for properly given gui sizes
	 * @param rows rows of gui
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder rows(@NotNull ChestRows rows){
		this.rows = rows;
		return this;
	}
	/**
	 * Sets the size of the chest gui. Depends on each GUI, generates only properly for properly given gui sizes
	 * @param rows rows of gui
	 * @return this
	 */
	@NotNull
	public BorderPatternBuilder rows(@Range(from = 3, to = 6) int rows){
		this.rows = ChestRows.rows(rows);
		return this;
	}

	@Override
	@NotNull
	public Background build() {
		PatternBackgroundBuilder builder = new PatternBackgroundBuilder();
		int col = 0;
		int row = 0;
		int color = 0;
		for (int i = 0; i < rows.getSlots(); i++){
			if (col == inwards && leftBorder){
				if (color==0 || borderSecond == null) {
					builder.pattern(row, col, 'a');
				} else {
					builder.pattern(row, col, 'b');
				}
				color++;
			} else if (col == 8-inwards && rightBorder){
				if (color==0 || borderSecond == null) {
					builder.pattern(row, col, 'a');
				} else {
					builder.pattern(row, col, 'b');
				}
				color++;
			} else if (row == 0 && topBorder){
				if (color==0 || borderSecond == null) {
					builder.pattern(row, col, 'a');
				} else {
					builder.pattern(row, col, 'b');
				}
				color++;
			} else if (row+1>= rows.getRows() && bottomBorder){
				if (color==0 || borderSecond == null) {
					builder.pattern(row, col, 'a');
				} else {
					builder.pattern(row, col, 'b');
				}
				color++;
			} else {
				builder.pattern(row, col, 'c');
			}

			if (color == 2){
				color = 0;
			}

			col++;
			if (col==9){
				col = 0;
				row++;
			}
		}
		return builder.clickable('a', border).clickable('b', borderSecond != null ? borderSecond : Clickable.EMPTY).clickable('c', other).build();
	}
}
