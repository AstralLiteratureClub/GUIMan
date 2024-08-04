package bet.astral.guiman;

import org.bukkit.event.inventory.InventoryType;

public class GUIBuilder extends InventoryGUIBuilder{
	public GUIBuilder(int rows) {
		super(rows);
	}

	public GUIBuilder(InventoryType type) {
		super(type);
	}
}
