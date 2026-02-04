package bet.astral.guiman.api.inventory.gui;

import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.jetbrains.annotations.NotNull;

/**
 * Builder used to configure and create an {@link InventoryGUI}.
 */
public interface MInventoryGUIBuilder extends InventoryGUIBuilder {
	/**
	 * Returns a new inventory gui builder
	 * @return new instance
	 */
	static @NotNull MInventoryGUIBuilder builder() {
		return (MInventoryGUIBuilder) GUIMan.getGUIMan().inventoryBuilder();
	}

	/**
	 * Sets a translation key used to resolve the inventory title.
	 *
	 * @param titleTranslationKey title translation key
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setTitleTranslationKey(TranslationKey titleTranslationKey);

	/**
	 * Sets a placeholder generator used for translated components.
	 * <p>
	 * Requires Messenger support to be effective.
	 *
	 * @param placeholderGenerator placeholder generator
	 * @return this builder
	 */
	@NotNull
	InventoryGUIBuilder setPlaceholderGenerator(PlaceholderGenerator placeholderGenerator);
}
