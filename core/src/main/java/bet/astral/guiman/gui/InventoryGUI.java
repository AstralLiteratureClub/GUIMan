package bet.astral.guiman.gui;

import bet.astral.guiman.GUIMan;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.gui.builders.InventoryGUIPatternBuilder;
import bet.astral.guiman.internals.InteractableGUI;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * GUIMans GUIs are InventoryGUIs.
 * Provides a GUI which can be edited using {@link InventoryGUIBuilder}.
 * Allows messenger-based translation GUIs to require only one GUI per server.
 */
@Getter
public class InventoryGUI  {
	@ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
	@Deprecated(forRemoval = true)
	public static void init(@NotNull JavaPlugin plugin){
		GUIMan.init(plugin);
	}
	@Getter(AccessLevel.NONE)
	public static final Consumer<Player> EMPTY_CONSUMER = player -> {};
	private final Map<Player, InteractableGUI> players = new HashMap<>();
	private final Component name;
	private final InventoryType type;
	private final int slots;
	private final Background background;
	private final Map<Integer, Collection<ClickableLike>> clickables;
	@Getter(AccessLevel.PUBLIC)
	private final Map<Integer, Clickable> ids = new HashMap<>();
	@Getter(AccessLevel.NONE)
	private final Consumer<Player> closeConsumer;
	private final Consumer<Player> openConsumer;
	private final boolean regenerateItems;
	@NotNull
	private final Map<Integer, Map<ClickType, GlobalGUIClickAction>> guiClickActions;
	@NotNull
	private final Map<Integer, Map<ClickType, GlobalGUIClickAction>> playerClickActions;

	@Nullable
	private final Messenger messenger;
	@Nullable
	private final TranslationKey nameTranslation;
	@NotNull
	private final Function<Player, PlaceholderCollection> placeholderGenerator;
	private final boolean useMessenger;
	private final Consumer<Player> generationExceptionPlayerHandler;

	/**
	 * Creates a new inventory gui builder instance, with inventory type being {@link InventoryType#CHEST}
	 * @param rows rows to display
	 * @return new builder
	 */
	@NotNull
	public static InventoryGUIBuilder builder(@NotNull ChestRows rows) {
		return new InventoryGUIBuilder(rows, InventoryType.CHEST);
	}
	/**
	 * Creates a new inventory gui builder instance, with inventory type being {@link InventoryType#CHEST}
	 * @param rows rows to display
	 * @return new builder
	 */
	@NotNull
	public static InventoryGUIBuilder builder(int rows) {
		return new InventoryGUIBuilder(ChestRows.rows(rows), InventoryType.CHEST);
	}
	@NotNull
	public static InventoryGUIBuilder builder(@NotNull InventoryType inventoryType){
		return new InventoryGUIBuilder(inventoryType == InventoryType.CHEST ? ChestRows.THREE : ChestRows.ONE, inventoryType);
	}
	/**
	 * Creates a new inventory gui {@link InventoryGUIPatternBuilder} builder instance, with inventory type being {@link InventoryType#CHEST}.
	 * @param rows rows to display
	 * @return new builder
	 */
	@NotNull
	public static InventoryGUIPatternBuilder patternBuilder(int rows) {
		return new InventoryGUIBuilder(ChestRows.rows(rows), InventoryType.CHEST).patternBuilder();
	}

	/**
	 * Creates a new inventory gui {@link InventoryGUIPatternBuilder} builder instance, with the inventory type being given inventory type.
	 * @param inventoryType inventory type
	 * @return new builder
	 */
	@NotNull
	public static InventoryGUIPatternBuilder patternBuilder(@NotNull InventoryType inventoryType){
		return new InventoryGUIBuilder(inventoryType == InventoryType.CHEST ? ChestRows.THREE : ChestRows.ONE, inventoryType).patternBuilder();
	}

	/**
	 * Creates a GUI with static name which is not modified using messenger
	 * @param name inventory title
	 * @param type the type of inventory (Can be null)
	 * @param slots how many slots (or 0 if no chest inventory)
	 * @param background background
	 * @param clickable the clickables
	 * @param closeConsumer consumer ran when closing inventory
	 * @param openConsumer consumer ran when opening inventory
 	 * @param regenerateItems should inventory always regenerate?
	 * @param messenger messenger
	 * @param generationExceptionPlayerHandler exception handler when trying to generate inventory for a player
	 */
	@ApiStatus.Internal
	public InventoryGUI(@Nullable Component name, InventoryType type, int slots, Background background, Map<Integer, Collection<ClickableLike>> clickable, Consumer<Player> closeConsumer, Consumer<Player> openConsumer, boolean regenerateItems, @NotNull Map<Integer, Map<ClickType, GlobalGUIClickAction>> guiClickActions, @NotNull Map<Integer, Map<ClickType, GlobalGUIClickAction>> playerClickActions, @Nullable Messenger messenger, Consumer<Player> generationExceptionPlayerHandler) {
		this.name = name;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
		this.type = type;
		this.slots = slots;
		this.background = background;
		this.clickables = clickable;
        this.guiClickActions = guiClickActions;
        this.playerClickActions = playerClickActions;
        this.messenger = messenger;
		this.generationExceptionPlayerHandler = generationExceptionPlayerHandler;
		this.nameTranslation = null;
		this.placeholderGenerator = p->new PlaceholderList();
		this.useMessenger = false;
		ids.put(Clickable.EMPTY.getId(), Clickable.EMPTY);
	}

	/**
	 * Creates a GUI with static name which is not modified using messenger
	 * @param nameTranslation the translation code for inventory title
	 * @param placeholderGenerator the placeholder generator ran when creating title
	 * @param type the type of inventory (Can be null)
	 * @param slots how many slots (or 0 if no chest inventory)
	 * @param background background
	 * @param clickable the clickables
	 * @param closeConsumer consumer ran when closing inventory
	 * @param openConsumer consumer ran when opening inventory
	 * @param regenerateItems should inventory always regenerate?
	 * @param messenger messenger
	 * @param generationExceptionPlayerHandler exception handler when trying to generate inventory for a player
	 */
	@ApiStatus.Internal
	public InventoryGUI(@NotNull TranslationKey nameTranslation, @Nullable Function<Player, PlaceholderCollection> placeholderGenerator, @NotNull Messenger messenger, InventoryType type, int slots, Background background, Map<Integer, Collection<ClickableLike>> clickable, Consumer<Player> closeConsumer, Consumer<Player> openConsumer, boolean regenerateItems, @NotNull Map<Integer, Map<ClickType, GlobalGUIClickAction>> guiClickActions, @NotNull Map<Integer, Map<ClickType, GlobalGUIClickAction>> playerClickActions, Consumer<Player> generationExceptionPlayerHandler) {
		this.name = Component.translatable(nameTranslation);
		this.nameTranslation = nameTranslation;
		this.placeholderGenerator = placeholderGenerator != null ? placeholderGenerator : p -> new PlaceholderList();
		this.messenger = messenger;
		this.type = type;
		this.slots = slots;
		this.background = background;
		this.clickables = clickable;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
        this.guiClickActions = guiClickActions;
        this.playerClickActions = playerClickActions;
        this.generationExceptionPlayerHandler = generationExceptionPlayerHandler;
		this.useMessenger = true;
		ids.put(Clickable.EMPTY.getId(), Clickable.EMPTY);
	}

	/**
	 * Registers clickable to generated when making player inventories
	 * @param clickable clickable to register
	 * @param player player to provide for
	 * @return given clickable
	 */
	@ApiStatus.Internal
	public Clickable registerClickable(@NotNull Clickable clickable, @NotNull Player player) {
		ids.put(clickable.getId(), clickable);
		return clickable;
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
				GUIMan guiMan = GUIMan.GUIMAN;
				this.getPlayers().putIfAbsent(player, new InteractableGUI(this, player));
				InteractableGUI gui = players.get(player);
				if (gui == null) {
					gui = new InteractableGUI(this, player);
					gui.generate(player, messenger);
				} else if (regenerateItems) {
					gui.generate(player, messenger);
				}

				final InteractableGUI interactableGUI = gui;
				player.getScheduler().run(GUIMan.GUIMAN.getPlugin(), t ->{
					player.openInventory(interactableGUI.getInventory());
				} , null);
			} catch (Exception e){
				GUIMan.GUIMAN.getPlugin().getSLF4JLogger().error("Error while trying to open GUI to {}", player.getName(), e);
			}
		}).exceptionally(throwable->{
			GUIMan.GUIMAN.getPlugin().getSLF4JLogger().error("Caught exception while trying to open GUI inventory!", throwable);
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
}
