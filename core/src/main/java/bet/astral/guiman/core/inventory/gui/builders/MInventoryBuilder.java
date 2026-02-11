package bet.astral.guiman.core.inventory.gui.builders;

import bet.astral.guiman.api.inventory.gui.InventoryGUIBuilder;
import bet.astral.guiman.api.inventory.gui.MInventoryGUIBuilder;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.jetbrains.annotations.NotNull;

public class MInventoryBuilder extends InventoryBuilder  implements MInventoryGUIBuilder {
	@Override
	public @NotNull InventoryGUIBuilder setTitleTranslationKey(TranslationKey titleTranslationKey) {
		return null;
	}

	@Override
	public @NotNull InventoryGUIBuilder setPlaceholderGenerator(PlaceholderGenerator placeholderGenerator) {
		return null;
	}

}
