package bet.astral.guiman.api.inventory.gui;

import bet.astral.guiman.api.annotations.UseMessenger;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.jetbrains.annotations.Nullable;

public interface MInventoryGUI extends InventoryGUI{
	/**
	 * Translation based title
	 * @return translation key, nullable
	 */
	@Nullable
	@UseMessenger
	TranslationKey getTitleTranslation();
	/**
	 * Checks if the {@link #getTitleTranslation()} is set.
	 * @return true if the translation based title is set
	 */
	boolean hasTranslationKeyTitle();


	/**
	 * Returns the placeholder generator used for this GUI
	 * @return placeholder generator
	 */
	@UseMessenger
	PlaceholderGenerator getPlaceholderGenerator();

	/**
	 * Returns true, if this inventory has placeholder generator
	 * @return placeholder generator
	 */
	@UseMessenger
	boolean hasPlaceholderGenerator();
}
