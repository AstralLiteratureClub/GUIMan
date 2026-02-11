package bet.astral.guiman.core.inventory.gui.builders;

import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import bet.astral.guiman.api.inventory.gui.InventoryGUIBuilder;
import bet.astral.guiman.core.inventory.gui.Inventory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InventoryBuilder implements InventoryGUIBuilder, Cloneable {
	private final Map<String, Object> data = new HashMap<>();
	private InventoryType type = InventoryType.CHEST;
	private ChestRows rows = ChestRows.THREE;
	private Component title;
	private Background background;
	private boolean regenerateItems = false;
	private boolean shared = false;
	private final Map<@NotNull Integer, @NotNull Collection<@NotNull ClickableLike>> clickables = new HashMap<>();
	private Consumer<@NotNull Player> closeConsumer;
	private Consumer<@NotNull Player> openConsumer;
	private Consumer<Void> builderExceptionPlayerHandler;
	private BiConsumer<Player, Exception> generationExceptionPlayerHandler;


	@ApiStatus.Internal
	protected InventoryBuilder(InventoryBuilder builder){
		this.title = builder.title;
		this.type = builder.type;
		this.background = builder.background;
		this.clickables.putAll(builder.clickables);
		this.closeConsumer = builder.closeConsumer;
		this.openConsumer = builder.openConsumer;
		this.regenerateItems = builder.regenerateItems;
		this.rows = builder.rows;
		this.builderExceptionPlayerHandler = builder.builderExceptionPlayerHandler;
		this.generationExceptionPlayerHandler = builder.generationExceptionPlayerHandler;
	}

	public InventoryBuilder() {
	}

	@Override
	public @NotNull InventoryGUIBuilder setTitle(Component titleComponent) {
		this.title = titleComponent;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setInventoryType(InventoryType inventoryType) {
		this.type = inventoryType;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setChestRows(ChestRows chestRows) {
		this.rows = chestRows;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setClickables(Map<Integer, Collection<bet.astral.guiman.api.inventory.clickable.ClickableLike>> clickables) {
		this.clickables.clear();
		this.clickables.putAll(clickables);
		return this;
	}

	@Override
	public InventoryGUIBuilder setClickable(int slot, bet.astral.guiman.api.inventory.clickable.ClickableLike clickableLike) {
		this.clickables.put(slot, new HashSet<>());
		this.clickables.get(slot).add(clickableLike);
		return this;
	}

	@Override
	public InventoryGUIBuilder setClickables(int slot, bet.astral.guiman.api.inventory.clickable.ClickableLike... clickableLikes) {
		this.clickables.put(slot, new HashSet<>());
		this.clickables.get(slot).addAll(Arrays.asList(clickableLikes));
		return this;
	}

	@Override
	public InventoryGUIBuilder addClickable(int slot, bet.astral.guiman.api.inventory.clickable.ClickableLike clickableLike) {
		this.clickables.putIfAbsent(slot, new HashSet<>());
		this.clickables.get(slot).add(clickableLike);
		return this;
	}

	@Override
	public InventoryGUIBuilder addClickables(int slot, bet.astral.guiman.api.inventory.clickable.ClickableLike... clickableLike) {
		this.clickables.putIfAbsent(slot, new HashSet<>());
		this.clickables.get(slot).addAll(Arrays.asList(clickableLike));
		return this;
	}

	@Override
	public InventoryGUIBuilder generateClickables(int slot, SlotClickableGenerator generator) {
		Collection<ClickableLike> clickableLikes = generator.generate();
		this.clickables.putIfAbsent(slot, new HashSet<>());
		this.clickables.get(slot).addAll(clickableLikes);
		return this;
	}

	@Override
	public InventoryGUIBuilder generateClickables(FullClickableGenerator generator) {
		Map<Integer, Collection<ClickableLike>> clickables = generator.generate();
		this.clickables.clear();
		this.clickables.putAll(clickables);
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setBackground(bet.astral.guiman.api.inventory.background.Background background) {
		this.background = background;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setGeneratesItemsEachOpen(boolean generatesItemsEachOpen) {
		this.regenerateItems = generatesItemsEachOpen;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setShared(boolean shared) {
		this.shared = shared;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setOpenAction(Consumer<Player> openAction) {
		this.openConsumer = openAction;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setCloseAction(Consumer<Player> closeAction) {
		this.closeConsumer = closeAction;
		return this;
	}

	@Override
	public @NotNull InventoryGUIBuilder setExceptionHandler(BiConsumer<Player, Exception> exceptionHandler) {
		this.generationExceptionPlayerHandler = exceptionHandler;
		return this;
	}

	@Override
	public InventoryGUIBuilder setData(@NotNull String key, Object value) {
		data.put(key, value);
		return this;
	}

	public @NotNull InventoryGUI build(){
		return new Inventory(
				title,
				data,
				type,
				rows,
				clickables,
				background,
				regenerateItems,
				shared,
				openConsumer,
				closeConsumer,
				generationExceptionPlayerHandler
			);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIBuilder clone() {
		return new InventoryBuilder(this);
	}
}
