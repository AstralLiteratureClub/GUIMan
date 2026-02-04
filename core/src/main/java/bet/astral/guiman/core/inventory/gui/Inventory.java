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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class Inventory implements MInventoryGUI {
	private final Component titleComponent;

	private final InventoryType inventoryType;
	private final ChestRows chestRows;

	private final Map<Integer, Collection<ClickableLike>> clickables;
	private final Background background;
	private final boolean generatesItemsEachOpen;
	private final boolean shared;

	private final Consumer<Player> openAction;
	private final Consumer<Player> closeAction;
	private final Consumer<Player> exceptionHandler;

	@Override
	public @Nullable Component getTitle() {
		return titleComponent;
	}

	@Override
	public @Nullable TranslationKey getTitleTranslation() {
		return titleTranslationKey;
	}

	@Override
	public boolean hasTranslationKeyTitle() {
		return titleTranslationKey != null;
	}

	@Override
	public boolean hasComponentTitle() {
		return titleComponent != null;
	}

	@Override
	public InventoryType getInventoryType() {
		return inventoryType;
	}

	@Override
	public boolean isChest() {
		return inventoryType == InventoryType.CHEST;
	}

	@Override
	public ChestRows getChestRows() {
		return isChest() ? chestRows : ChestRows.THREE;
	}

	@Override
	public int getRows() {
		return isChest() ? chestRows.getRows() : 3;
	}

	@Override
	public int getSlots() {
		return inventoryType.getDefaultSize();
	}

	@Override
	public Map<Integer, Collection<ClickableLike>> getClickables() {
		return clickables;
	}

	@Override
	public Collection<ClickableLike> getClickable(int slot) {
		return clickables.getOrDefault(slot, new ArrayList<>());
	}

	@Override
	public Background getBackground() {
		return background;
	}

	@Override
	public boolean regeneratesItemsEachOpen() {
		return generatesItemsEachOpen;
	}

	@Override
	public boolean isShared() {
		return shared;
	}

	@Override
	public PlaceholderGenerator getPlaceholderGenerator() {
		return placeholderGenerator;
	}

	@Override
	public boolean hasPlaceholderGenerator() {
		return placeholderGenerator != null;
	}

	@Override
	public Consumer<Player> getOpenAction() {
		return openAction;
	}

	@Override
	public Consumer<Player> getCloseAction() {
		return closeAction;
	}

	@Override
	public Consumer<Player> getExceptionHandler() {
		return exceptionHandler;
	}

	@Override
	public void open(Player player) {

	}
}
