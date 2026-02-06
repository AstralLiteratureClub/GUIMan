package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.inventory.gui.MInventoryGUI;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MInventory extends Inventory implements MInventoryGUI {
	protected final TranslationKey title;
	protected final PlaceholderGenerator placeholderGenerator;
	public MInventory(Component titleComponent, Map<String, Object> data, InventoryType inventoryType, ChestRows chestRows, Map<Integer, Collection<ClickableLike>> clickables, Background background, boolean generatesItemsEachOpen, boolean shared, Consumer<Player> openAction, Consumer<Player> closeAction, BiConsumer<Player, Exception> exceptionHandler, TranslationKey title, PlaceholderGenerator placeholderGenerator) {
		super(titleComponent, data, inventoryType, chestRows, clickables, background, generatesItemsEachOpen, shared, openAction, closeAction, exceptionHandler);
		this.title = title;
		this.placeholderGenerator = placeholderGenerator;
	}

	@Override
	public @Nullable TranslationKey getTitleTranslation() {
		return title;
	}

	@Override
	public boolean hasTranslationKeyTitle() {
		return title != null;
	}

	@Override
	public PlaceholderGenerator getPlaceholderGenerator() {
		return placeholderGenerator;
	}

	@Override
	public boolean hasPlaceholderGenerator() {
		return placeholderGenerator != null;
	}
}
