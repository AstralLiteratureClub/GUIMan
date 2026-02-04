package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryMessengerBuilder {
	private Component titleComponent;
	private TranslationKey titleTranslationKey;
	private InventoryType inventoryType;
	private ChestRows chestRows;
	private Map<Integer, Collection<ClickableLike>> clickables;
	private Background background;
	private boolean generatesItemsEachOpen;
	private boolean shared;
	private PlaceholderGenerator placeholderGenerator;
	private Consumer<Player> openAction;
	private Consumer<Player> closeAction;
	private Consumer<Player> exceptionHandler;

	public InventoryMessengerBuilder setTitleComponent(Component titleComponent) {
		this.titleComponent = titleComponent;
		return this;
	}

	public InventoryMessengerBuilder setTitleTranslationKey(TranslationKey titleTranslationKey) {
		this.titleTranslationKey = titleTranslationKey;
		return this;
	}

	public InventoryMessengerBuilder setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
		return this;
	}

	public InventoryMessengerBuilder setChestRows(ChestRows chestRows) {
		this.chestRows = chestRows;
		return this;
	}

	public InventoryMessengerBuilder setClickables(Map<Integer, Collection<ClickableLike>> clickables) {
		this.clickables = clickables;
		return this;
	}

	public InventoryMessengerBuilder setBackground(Background background) {
		this.background = background;
		return this;
	}

	public InventoryMessengerBuilder setGeneratesItemsEachOpen(boolean generatesItemsEachOpen) {
		this.generatesItemsEachOpen = generatesItemsEachOpen;
		return this;
	}

	public InventoryMessengerBuilder setShared(boolean shared) {
		this.shared = shared;
		return this;
	}

	public InventoryMessengerBuilder setPlaceholderGenerator(PlaceholderGenerator placeholderGenerator) {
		this.placeholderGenerator = placeholderGenerator;
		return this;
	}

	public InventoryMessengerBuilder setOpenAction(Consumer<Player> openAction) {
		this.openAction = openAction;
		return this;
	}

	public InventoryMessengerBuilder setCloseAction(Consumer<Player> closeAction) {
		this.closeAction = closeAction;
		return this;
	}

	public InventoryMessengerBuilder setExceptionHandler(Consumer<Player> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	public InventoryMessenger createInventoryMessenger() {
		return new InventoryMessenger(titleComponent, titleTranslationKey, inventoryType, chestRows, clickables, background, generatesItemsEachOpen, shared, placeholderGenerator, openAction, closeAction, exceptionHandler);
	}

}
