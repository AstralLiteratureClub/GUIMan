package bet.astral.guiman.api.inventory.clickable;

import bet.astral.guiman.api.annotations.UseMessenger;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.jetbrains.annotations.Nullable;

/**
 * Extended clickable including new messenger based methods to allow translated GUIs
 */
@UseMessenger
public interface MClickable extends Clickable {
	/**
	 * Returns the name translation key of the clickable
	 * @return name translation key, nullable
	 */
	@Nullable
	@UseMessenger
	TranslationKey getNameTranslation();

	/**
	 * Checks if this clickable has a name translation key
	 * @return true, if this has name translation
	 */
	boolean hasNameTranslation();

	/**
	 * Retuns the lore translation key of the clickable
	 * @return lore translation key, nullable
	 */
	@Nullable
	@UseMessenger
	TranslationKey getLoreTranslation();
	/**
	 * Checks if this clickable has a lore translation key
	 * @return true, if this has lore translation
	 */
	boolean hasLoreTranslation();

	/**
	 * Returns the permission message translation key
	 * @return permission message translation key
	 */
	@Nullable
	@UseMessenger
	TranslationKey getPermissionMessageTranslation();

	/**
	 * Checks if this clickable has permission message translation key
	 * @return true if this has permission message translation
	 */
	boolean hasPermissionMessageTranslation();

	@Nullable
	@UseMessenger
	PlaceholderGenerator getPlaceholderGenerator();

	/**
	 * Checks if this clickable has a placeholder generator
	 * @return true if this has placeholder generator
	 */
	boolean hasPlaceholderGenerator();
}
