package bet.astral.guiman;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.clickable.ClickableProvider;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
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
public class InventoryGUI {
	protected static JavaPlugin PLUGIN;
	public static void init(@NotNull JavaPlugin plugin){
		InventoryGUI.PLUGIN = plugin;
		plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
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

	@Nullable
	private final Messenger messenger;
	@Nullable
	private final TranslationKey nameTranslation;
	@Nullable
	private final Function<Player, PlaceholderList> placeholderGenerator;
	private final boolean useMessenger;

	public InventoryGUI(@Nullable Component name, InventoryType type, int slots, Background background, Map<Integer, Collection<ClickableLike>> clickable, Consumer<Player> closeConsumer, Consumer<Player> openConsumer, boolean regenerateItems, Messenger messenger) {
		this.name = name;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
		this.type = type;
		this.slots = slots;
		this.background = background;
		this.clickables = clickable;
		this.messenger = messenger;
		this.nameTranslation = null;
		this.placeholderGenerator = null;
		this.useMessenger = false;
		ids.put(Clickable.EMPTY.getId(), Clickable.EMPTY);
	}

	public InventoryGUI(@NotNull TranslationKey nameTranslation, @Nullable Function<Player, PlaceholderList> placeholderGenerator, @NotNull Messenger messenger, InventoryType type, int slots, Background background, Map<Integer, Collection<ClickableLike>> clickable, Consumer<Player> closeConsumer, Consumer<Player> openConsumer, boolean regenerateItems) {
		this.name = Component.translatable(nameTranslation);
		this.nameTranslation = nameTranslation;
		this.placeholderGenerator = placeholderGenerator;
		this.messenger = messenger;
		this.type = type;
		this.slots = slots;
		this.background = background;
		this.clickables = clickable;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
		this.useMessenger = true;
		ids.put(Clickable.EMPTY.getId(), Clickable.EMPTY);
	}

	protected Clickable registerClickable(@NotNull ClickableLike clickableLike, @NotNull Player player) {
		Clickable clickable = clickableLike instanceof ClickableProvider clickableProvider ? clickableProvider.provide(player) : clickableLike.asClickable();
		ids.put(clickable.getId(), clickable);
		return clickable;
	}

	public void open(Player player) {
		CompletableFuture.runAsync(()->{
			try {
				this.getPlayers().putIfAbsent(player, new InteractableGUI(this, player));
				InteractableGUI gui = players.get(player);
				if (gui == null) {
					gui = new InteractableGUI(this, player);
					gui.generate(player, messenger);
				} else if (regenerateItems) {
					gui.generate(player, messenger);
				}

				final InteractableGUI interactableGUI = gui;
				player.getScheduler().run(PLUGIN, t -> player.openInventory(interactableGUI.getInventory()), null);
			} catch (Exception e){
				PLUGIN.getSLF4JLogger().error("Error while trying to open GUI to "+ player.getName(), e);
			}
		});
	}

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
