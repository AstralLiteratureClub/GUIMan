package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.api.inventory.ChestRows;
import bet.astral.guiman.api.inventory.background.Background;
import bet.astral.guiman.api.inventory.clickable.ClickableLike;
import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import bet.astral.guiman.core.GUIManInitializer;
import bet.astral.guiman.core.inventory.clickable.Clickable;
import bet.astral.guiman.core.inventory.internals.InteractableGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Inventory implements InventoryGUI {
	@ApiStatus.Internal
	private final Map<Integer, Clickable> ids = new HashMap<>();
	@ApiStatus.Internal
	private final Map<Player, InteractableGUI> players = new HashMap<>();

	protected final Component titleComponent;
	protected final Map<String, Object> data;

	protected final InventoryType inventoryType;
	protected final ChestRows chestRows;

	protected final Map<Integer, Collection<ClickableLike>> clickables;
	protected final Background background;
	protected final boolean generatesItemsEachOpen;
	protected final boolean shared;

	protected final Consumer<Player> openAction;
	protected final Consumer<Player> closeAction;
	protected final BiConsumer<Player, Exception> exceptionHandler;

	public Inventory(Component titleComponent, Map<String, Object> data, InventoryType inventoryType, ChestRows chestRows, Map<Integer, Collection<ClickableLike>> clickables, Background background, boolean generatesItemsEachOpen, boolean shared, Consumer<Player> openAction, Consumer<Player> closeAction, BiConsumer<Player, Exception> exceptionHandler) {
		this.titleComponent = titleComponent;
		this.data = data != null ? data : new HashMap<>();
		this.inventoryType = inventoryType;
		this.chestRows = chestRows;
		this.clickables = clickables;
		this.background = background;
		this.generatesItemsEachOpen = generatesItemsEachOpen;
		this.shared = shared;
		this.openAction = openAction;
		this.closeAction = closeAction;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public @Nullable Component getTitle() {
		return titleComponent;
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
	public Consumer<Player> getOpenAction() {
		return openAction;
	}

	@Override
	public Consumer<Player> getCloseAction() {
		return closeAction;
	}

	@Override
	public BiConsumer<Player, Exception> getExceptionHandler() {
		return exceptionHandler;
	}

	@Override
	public void setData(@NotNull String key, Object value) {
		data.put(key, value);
	}

	@Override
	public @Nullable Object getData(String key) {
		return data.get(key);
	}

	@Override
	public @NotNull Map<String, Object> getData() {
		return data;
	}

	@Override
	public void clearData() {
		data.clear();
	}

	/**
	 * Opens the GUI to a player and generates the GUI if none is found.
	 * Uses asynchronous ways to generate inventories and open inventory in the main bukkit thread after generation
	 * @param player player to open to
	 */
	@NonBlocking
	public void open(Player player) {
		CompletableFuture.runAsync(()->{
			try {
				this.players.putIfAbsent(player, new InteractableGUI(this, player));
				InteractableGUI gui = players.get(player);
				if (gui == null) {
					gui = new InteractableGUI(this, player);
					gui.generate(player, messenger);
				} else if (regenerateItems) {
					gui.generate(player, messenger);
				}

				final InteractableGUI interactableGUI = gui;
				player.getScheduler().run(GUIManInitializer.GUIMAN.getPlugin(), t ->{
					player.openInventory(interactableGUI.getInventory());
				} , null);
			} catch (Exception e){
				GUIManInitializer.GUIMAN.getPlugin().getSLF4JLogger().error("Error while trying to open GUI to {}", player.getName(), e);
			}
		}).exceptionally(throwable->{
			GUIManInitializer.GUIMAN.getPlugin().getSLF4JLogger().error("Caught exception while trying to open GUI inventory!", throwable);
			return null;
		});
	}

	/**
	 * Returns the id associated with given item stack
	 * @param itemStack item stack
	 * @return id, else {@link Clickable#EMPTY}'s id
	 */
	public int getId(@Nullable ItemStack itemStack){
		if (itemStack == null){
			return Clickable.EMPTY.getId();
		}
		ItemMeta meta = itemStack.getItemMeta();
		PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
		Integer id = persistentDataContainer.get(Clickable.ITEM_KEY, PersistentDataType.INTEGER);
		if (id == null){
			return Clickable.EMPTY.getId();
		}
		return id;
	}

	public void registerClickable(bet.astral.guiman.api.inventory.clickable.Clickable clickable, @NotNull Player player) {

	}
}
