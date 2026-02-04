package bet.astral.guiman.api.inventory.clickable;

import bet.astral.guiman.api.annotations.UseMessenger;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;

import java.util.List;

/**
 * Messenger enhanced clickable builder
 */
@UseMessenger
public interface MClickableBuilder extends ClickableBuilder {
	/**
	 * Sets the name of the item with translation key parsed with messenger
	 * @param name name
	 * @return this
	 */
	MClickableBuilder setName(TranslationKey name);

	/**
	 * Sets the lore of the item with translation key list parsed with messenger to be lore
	 * @param lore lore
	 * @return this
	 */
	MClickableBuilder setLoreTranslation(List<TranslationKey> lore);
	/**
	 * Sets the lore of the item with translation key list parsed with messenger to be lore
	 * @param lore lore
	 * @return this
	 */
	MClickableBuilder setLoreTranslation(TranslationKey... lore);
	/**
	 * Adds to the lore of the item translation keys which are parsed with messenger
	 * @param lore lore
	 * @return this
	 */
	MClickableBuilder addLoreTranslation(List<TranslationKey> lore);
	/**
	 * Adds to the lore of the item translation keys which are parsed with messenger
	 * @param lore lore
	 * @return this
	 */
	MClickableBuilder addLoreTranslation(TranslationKey... lore);

	/**
	 * Sets the placeholder generator of the item
	 * @param placeholderGenerator placeholder generator
	 * @return this
	 */
	MClickableBuilder setPlaceholderGenerator(PlaceholderGenerator placeholderGenerator);
}
