package bet.astral.guiman;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.builders.BackgroundBuilder;
import bet.astral.guiman.background.Backgrounds;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.more4j.function.consumer.TriConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryGUIBuilder {
	public static boolean throwExceptionIfMessengerNull = true;
	private Component name;
	private final InventoryType type;
	private Background background;
	private final Map<@NotNull Integer, @NotNull Collection<@NotNull ClickableLike>> clickables = new HashMap<>();
	private Consumer<@NotNull Player> closeConsumer;
	private Consumer<@NotNull Player> openConsumer;
	private boolean regenerateItems = false;
	private final ChestRows rows;
	private TranslationKey titleTranslation;
	private Function<Player, PlaceholderList> placeholderGenerator = (p)->new PlaceholderList();
	private Messenger messenger;
	private Consumer<Void> builderExceptionPlayerHandler;
	private Consumer<Player> generationExceptionPlayerHandler;

	/**
	 * Creates chest inventory gui builder. Chest inventories is the most supported inventory type of GUIMan
	 * @param rows how many rows
	 */
	public InventoryGUIBuilder(@Range(from = 1, to = 6) int rows){
		this.type = InventoryType.CHEST;
		this.rows = ChestRows.rows(rows);
	}
	/**
	 * Creates chest inventory gui builder. Chest inventories is the most supported inventory type of GUIMan
	 * @param rows how many rows
	 */
	public InventoryGUIBuilder(ChestRows rows){
		this.type = InventoryType.CHEST;
		this.rows = rows;
	}

	/**
	 * Creates a custom inventory gui specified using {@link InventoryType type}
	 * @param type inventoryType
	 */
	public InventoryGUIBuilder(InventoryType type) throws IllegalArgumentException{
		switch (type){
			case PLAYER, JUKEBOX, CHISELED_BOOKSHELF, CREATIVE, MERCHANT, DECORATED_POT, COMPOSTER ->{
				throw new IllegalArgumentException("Cannot use inventory type "+ type.name()+" as inventory type!");
			}
		}
		this.type = type;
		this.rows = null;
	}

	/**
	 * Sets the title of the inventory to given component
	 * @param name title of inventory
	 * @return this
	 */
	@ApiStatus.Obsolete
	public InventoryGUIBuilder title(@Nullable Component name) {
		this.name = name;
		return this;
	}
	/**
	 * Sets the title of the inventory to the given component
	 * Modern way to parse the title before opening inventory to the player.
	 * @param name title of inventory
	 * @return this
	 */
	public InventoryGUIBuilder title(@NotNull TranslationKey name) {
		this.name = Component.translatable(name);
		this.titleTranslation = name;
		return this;
	}

	/**
	 * Sets the messenger used to generate components when generating inventory of players
	 * @param messenger messenger
	 * @return this
	 */
	public InventoryGUIBuilder messenger(@NotNull Messenger messenger){
		this.messenger = messenger;
		return this;
	}

	public InventoryGUIBuilder placeholderGenerator(@NotNull Function<Player, PlaceholderList> generator){
		this.placeholderGenerator = generator;
		return this;
	}

	/**
	 * Sets the background displayed, for slots which don't have a clickable
	 * @param backgroundBuilder background
	 * @return this
	 */
	@Contract("_ -> this")
	@NotNull
	public InventoryGUIBuilder background(@Nullable BackgroundBuilder backgroundBuilder) {
		if (backgroundBuilder == null){
			this.background = Backgrounds.EMPTY;
			return this;
		}
		Background background = backgroundBuilder.build();
		if (background == null){
			this.background = Backgrounds.EMPTY;
			return this;
		}
		this.background = background;
		return this;
	}

	/**
	 * Sets the background displayed, for slots which don't have a clickable
	 * @param background background
	 * @return this
	 */
	@Contract("_ -> this")
	@NotNull
	public InventoryGUIBuilder background(@Nullable Background background) {
		if (background == null){
			this.background = Backgrounds.EMPTY;
			return this;
		}
		this.background = background;
		return this;
	}

	public InventoryGUIBuilder clickable(int slot, @NotNull ClickableLike clickable){
		return clickable(slot, List.of(clickable));
	}
	public InventoryGUIBuilder clickable(int slot, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		this.clickables.put(slot, new LinkedList<>(clickables));
		return this;
	}
	public InventoryGUIBuilder clickable(int[] slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		for (int slot : slots){
			clickable(slot, clickables);
		}
		return this;
	}
	public InventoryGUIBuilder clickable(int[] slots, @NotNull ClickableLike clickable){
		for (int slot : slots){
			clickable(slot, clickable);
		}
		return this;
	}
	public InventoryGUIBuilder clickable(Collection<Integer> slots, @NotNull ClickableLike clickable){
		for (int slot : slots){
			clickable(slot, clickable);
		}
		return this;
	}
	public InventoryGUIBuilder clickable(Collection<Integer> slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		for (int slot : slots){
			clickable(slot, clickables);
		}
		return this;
	}

	public InventoryGUIBuilder clickableGeneral(int slot, ItemStack itemStack, TriConsumer<Clickable, ItemStack, Player> action){
		return clickable(slot, Clickable.general(itemStack, action));
	}
	public <Meta extends ItemMeta> InventoryGUIBuilder clickableGeneral(int slot, Material material, Consumer<Meta> meta, Class<Meta> metaType, TriConsumer<Clickable, ItemStack, Player> action){
		return clickable(slot, Clickable.builder(material, meta, metaType).actionGeneral(action));
	}
	public InventoryGUIBuilder clickableGeneral(int slot, Material material, Consumer<ItemMeta> meta, TriConsumer<Clickable, ItemStack, Player> action){
		return clickable(slot, Clickable.builder(material, meta).actionGeneral(action));
	}
	public InventoryGUIBuilder clickableGeneral(int slot, Material material, TriConsumer<Clickable, ItemStack, Player> action){
		return clickable(slot, Clickable.builder(material).actionGeneral(action));
	}

	public InventoryGUIBuilder addClickable(int slot, @NotNull ClickableLike clickable){
		return addClickable(slot, List.of(clickable));
	}
	public InventoryGUIBuilder addClickable(int slot, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		if (this.clickables.get(slot) == null){
			this.clickables.put(slot, new LinkedList<>(clickables));
			return this;
		}
		add(this.clickables.get(slot), clickables);
		return this;
	}
	public InventoryGUIBuilder addClickable(int[] slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		for (int slot : slots){
			addClickable(slot, clickables);
		}
		return this;
	}
	public InventoryGUIBuilder addClickable(int[] slots, @NotNull ClickableLike clickable){
		for (int slot : slots){
			addClickable(slot, clickable);
		}
		return this;
	}
	public InventoryGUIBuilder addClickable(Collection<Integer> slots, @NotNull ClickableLike clickable){
		for (int slot : slots){
			addClickable(slot, clickable);
		}
		return this;
	}
	public InventoryGUIBuilder addClickable(Collection<Integer> slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables){
		for (int slot : slots){
			addClickable(slot, clickables);
		}
		return this;
	}
	private void add(Collection<ClickableLike> base, Collection<? extends ClickableLike> adding){
		base.addAll(adding);
	}

	public InventoryGUIBuilder closeConsumer(@Nullable Consumer<@NotNull Player> closeConsumer) {
		this.closeConsumer = closeConsumer;
		return this;
	}

	public InventoryGUIBuilder openConsumer(@Nullable Consumer<@NotNull Player> openConsumer) {
		this.openConsumer = openConsumer;
		return this;
	}

	public InventoryGUIBuilder replaceItemsEachOpen() {
		this.regenerateItems = true;
		return this;
	}

	/**
	 * Accepted when an internal exception has accorded
	 * while trying to create package {@code THIS} builder to a {@link InventoryGUI}
	 *
	 * @param builderExceptionPlayerHandler action executed when an error accords
	 * @return this
	 */
	public InventoryGUIBuilder builderExceptionPlayerHandler(Consumer<Void> builderExceptionPlayerHandler) {
		this.builderExceptionPlayerHandler = builderExceptionPlayerHandler;
		return this;
	}

	/**
	 * Accepted when an internal exception has accorded
	 * while trying to generate inventory values in inventory {@link InteractableGUI},
	 * {@link InteractableGUI#generate(Player, Messenger)}
	 *
	 * @param generationExceptionPlayerHandler action executed when an error accords
	 * @return this
	 */
	public InventoryGUIBuilder generationExceptionPlayerHandler(Consumer<Player> generationExceptionPlayerHandler) {
		this.generationExceptionPlayerHandler = generationExceptionPlayerHandler;
		return this;
	}

	public InventoryGUI build(){
		try {
			if (messenger == null && throwExceptionIfMessengerNull) {
				throw new RuntimeException("Messenger was null while trying to package builder to InventoryGUI");
			}
			if (this.type == InventoryType.CHEST) {
				if (titleTranslation != null) {
					return new InventoryGUI(titleTranslation, placeholderGenerator, messenger, InventoryType.CHEST, rows.getSlots(), background, clickables, closeConsumer, openConsumer, regenerateItems, generationExceptionPlayerHandler);
				} else {
					return new InventoryGUI(name, InventoryType.CHEST, rows.getSlots(), background, clickables, closeConsumer, openConsumer, regenerateItems, messenger, generationExceptionPlayerHandler);
				}
			} else {
				if (titleTranslation != null) {
					return new InventoryGUI(titleTranslation, placeholderGenerator, messenger, type, type.getDefaultSize(), background, clickables, closeConsumer, openConsumer, regenerateItems, generationExceptionPlayerHandler);
				} else {
					return new InventoryGUI(name, type, type.getDefaultSize(), background, clickables, closeConsumer, openConsumer, regenerateItems, messenger, generationExceptionPlayerHandler);
				}
			}
		} catch (Exception e){
			if (builderExceptionPlayerHandler != null) {
				builderExceptionPlayerHandler.accept(null);
			}
			throw new RuntimeException(e);
		}
	}
}