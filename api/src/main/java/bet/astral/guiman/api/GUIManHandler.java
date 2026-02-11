package bet.astral.guiman.api;

import bet.astral.guiman.api.inventory.clickable.ClickableBuilder;
import bet.astral.guiman.api.inventory.gui.InventoryGUIBuilder;
import org.jetbrains.annotations.NotNull;

public interface GUIManHandler {
	@NotNull
	InventoryGUIBuilder createInventoryBuilder();

	@NotNull
	ClickableBuilder createClickableBuilder();
}
