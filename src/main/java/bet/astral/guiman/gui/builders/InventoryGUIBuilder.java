package bet.astral.guiman.gui.builders;

import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.builders.BackgroundBuilder;
import bet.astral.guiman.background.Backgrounds;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.internals.InteractableGUI;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryGUIBuilder implements Cloneable {
	public static boolean throwExceptionIfMessengerNull = true;
	private final InventoryType type;
	private final ChestRows rows;
	private Messenger messenger;
	private Component name;
	private TranslationKey titleTranslation;
	private Background background;
	private boolean regenerateItems = false;
	private final Map<@NotNull Integer, @NotNull Collection<@NotNull ClickableLike>> clickables = new HashMap<>();
	private Function<Player, PlaceholderCollection> placeholderGenerator = (p)->new PlaceholderList();
	private Consumer<@NotNull Player> closeConsumer;
	private Consumer<@NotNull Player> openConsumer;
	private Consumer<Void> builderExceptionPlayerHandler;
	private Consumer<Player> generationExceptionPlayerHandler;

	@ApiStatus.Internal
	protected InventoryGUIBuilder(InventoryGUIBuilder builder){
		this.name = builder.name;
		this.type = builder.type;
		this.background = builder.background;
		this.clickables.putAll(builder.clickables);
		this.closeConsumer = builder.closeConsumer;
		this.openConsumer = builder.openConsumer;
		this.regenerateItems = builder.regenerateItems;
		this.rows = builder.rows;
		this.titleTranslation = builder.titleTranslation;
		this.placeholderGenerator = builder.placeholderGenerator;
		this.messenger = builder.messenger;
		this.builderExceptionPlayerHandler = builder.builderExceptionPlayerHandler;
		this.generationExceptionPlayerHandler = builder.generationExceptionPlayerHandler;
	}

	/**
	 * Creates chest inventory gui builder.
	 * @param rows how many rows
	 */
	@Deprecated(forRemoval = true)
	public InventoryGUIBuilder(ChestRows rows){
		this(rows, InventoryType.CHEST);
	}
	/**
	 * Creates chest inventory gui builder.
	 * @param rows how many rows
	 */
	@Deprecated(forRemoval = true)
	public InventoryGUIBuilder(@Range(from=1,to=6) int rows){
		this(ChestRows.rows(rows), InventoryType.CHEST);
	}

	/**
	 * Creates chest inventory gui builder. Chest inventories is the most supported inventory type of GUIMan
	 *
	 * @param rows          how many rows
	 * @param inventoryType inventory type
	 */
	public InventoryGUIBuilder(@NotNull ChestRows rows, @NotNull InventoryType inventoryType){
		switch (inventoryType){
			case PLAYER, JUKEBOX, CHISELED_BOOKSHELF, CREATIVE, MERCHANT, DECORATED_POT, COMPOSTER ->{
				throw new IllegalArgumentException("Cannot use inventory type "+ inventoryType.name()+" as inventory type!");
			}
		}
		this.type = InventoryType.CHEST;
		this.rows = rows;
	}

	/**
	 * Creates a custom inventory gui specified using {@link InventoryType type}
	 * @param type inventoryType
	 */
	@Deprecated(forRemoval = true)
	public InventoryGUIBuilder(InventoryType type) throws IllegalArgumentException{
		this(ChestRows.ONE, type);
	}

	/**
	 * Sets the title of the inventory to given component
	 * @param name title of inventory
	 * @return this
	 * @deprecated deprecated in favor of messenger-based methods {@link #title(TranslationKey)}
	 */
	@Deprecated()
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

	public InventoryGUIBuilder placeholderGenerator(@NotNull Function<Player, PlaceholderCollection> generator){
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

	/**
	 * Converts this builder to an inventory pattern builder
	 * @return builder
	 */
	public InventoryGUIPatternBuilder patternBuilder(){
		return new InventoryGUIPatternBuilder(this);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIBuilder clone() {
		return new InventoryGUIBuilder(this);
	}
}