package bet.astral.guiman.utils;

import org.jetbrains.annotations.Range;

/**
 * Chests contain a maximum of {@code six} rows.
 * Helps decide how many rows to display in a chest inventory
 */
public enum ChestRows {
	/**
	 * Represents one row in a chest inventory
	 */
	ONE(),
	/**
	 * Represents two rows in a chest inventory
	 */
	TWO(),
	/**
	 * Represents three rows in a chest inventory
	 */
	THREE(),
	/**
	 * Represents four rows in a chest inventory
	 */
	FOUR(),
	/**
	 * Represents five rows in a chest inventory
	 */
	FIVE(),
	/**
	 * Represents six rows in a chest inventory
	 */
	SIX(),

	;
	ChestRows() {
	}

	/**
	 * Converts integer to a chest inventory row, if over {@code 6}, returns always {@link #SIX}
	 * @param rows rows
	 * @return rows converted to enum
	 */
	public static ChestRows rows(@Range(from = 1, to = Integer.MAX_VALUE) int rows){
		if (rows==1){
			return ONE;
		}
		if (rows==2){
			return TWO;
		}
		if (rows==3){
			return THREE;
		}
		if (rows==4){
			return FOUR;
		}
		if (rows==5){
			return FIVE;
		}
		return SIX;
	}

	/**
	 * Returns how many rows given enum means in a chest inventory
	 * @return rows {@code (1-6)}
	 */
	public int getRows(){
		return ordinal()+1;
	}

	/**
	 * Returns how many slots given enum means in a chest inventory
	 * @return slots {@code (rows*9)}
	 */
	public int getSlots(){
		return 9*getRows();
	}
}
