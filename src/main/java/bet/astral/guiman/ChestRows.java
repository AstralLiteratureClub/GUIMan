package bet.astral.guiman;

import org.jetbrains.annotations.Range;

/**
 * Chests contain a maximum of six rows, and ChestRows is only a helper enum
 * to display the correct number of rows in a chest inventory.
 */
public enum ChestRows {
	ONE(),
	TWO(),
	THREE(),
	FOUR(),
	FIVE(),
	SIX(),

	;
	ChestRows() {
	}

	public static ChestRows rows(@Range(from = 1, to = 6) int rows){
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

	public int getRows(){
		return ordinal()+1;
	}

	public int getSlots(){
		return 9*getRows();
	}
}
