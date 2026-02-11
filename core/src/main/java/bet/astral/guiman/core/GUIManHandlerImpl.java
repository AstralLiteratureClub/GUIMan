package bet.astral.guiman.core;

import bet.astral.guiman.api.GUIManHandler;
import bet.astral.guiman.api.inventory.clickable.ClickableBuilder;
import bet.astral.guiman.api.inventory.gui.InventoryGUIBuilder;
import bet.astral.guiman.core.inventory.gui.builders.InventoryBuilder;
import org.jetbrains.annotations.NotNull;

public class GUIManHandlerImpl implements GUIManHandler {
	@Override
	public @NotNull InventoryGUIBuilder createInventoryBuilder() {
		return new InventoryBuilder();
	}

	@Override
	public @NotNull ClickableBuilder createClickableBuilder() {
		return new bet.astral.guiman.core.inventory.clickable.ClickableBuilder();
	}
}
