package bet.astral.guiman.gui.builders;


import bet.astral.guiman.background.Background;
import bet.astral.guiman.background.builders.BackgroundBuilder;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Inventory GUI builder class which allows patterns to be used as the base.
 * Creates new instance of inventory builder when building inventory gui to allow multi use of builder class
 */
public class InventoryGUIPatternBuilder extends InventoryGUIBuilder {
	private final Map<Character, Set<ClickableLike>> clickablesByChar = new HashMap<>();
	private final Map<Integer, String> rowPatterns = new HashMap<>();
	private ClickableLike empty = null;

	/**
	 * Used to clone the inventory GUI pattern builder
	 * @param builder builder
	 */
	@ApiStatus.Internal
	protected InventoryGUIPatternBuilder(InventoryGUIPatternBuilder builder) {
		super(builder);
		this.clickablesByChar.putAll(builder.clickablesByChar);
		this.rowPatterns.putAll(builder.rowPatterns);
		this.empty = builder.empty;
	}
	/**
	 * Used to clone the inventory GUI pattern builder
	 * @param builder builder
	 */
	@ApiStatus.Internal
	protected InventoryGUIPatternBuilder(InventoryGUIBuilder builder) {
		super(builder);
	}

	public InventoryGUIPatternBuilder clickable(char character, @NotNull Material material){
		return clickable(character, Clickable.builder(material));
	}
	public InventoryGUIPatternBuilder clickable(char character, @NotNull ClickableLike clickableLike){
		this.clickablesByChar.putIfAbsent(character, new HashSet<>());
		clickablesByChar.get(character).add(clickableLike);
		return this;
	}
	public InventoryGUIPatternBuilder clickable(char character, @NotNull ClickableLike... clickableLike){
		this.clickablesByChar.putIfAbsent(character, new HashSet<>());
		clickablesByChar.get(character).addAll(Arrays.stream(clickableLike).toList());
		return this;
	}
	public InventoryGUIPatternBuilder clickable(char character, @NotNull Collection<? extends ClickableLike> clickableLikes){
		this.clickablesByChar.putIfAbsent(character, new HashSet<>());
		clickablesByChar.get(character).addAll(clickableLikes);
		return this;
	}

	public InventoryGUIPatternBuilder pattern(int row, @NotNull String pattern){
		this.rowPatterns.put(row, pattern);
		return this;
	}
	public InventoryGUIPatternBuilder pattern(String @NotNull ... patterns){
		int i = 0;
		for (String pattern : patterns){
			if (pattern == null){
				this.rowPatterns.remove(i);
				i++;
				continue;
			}
			this.rowPatterns.put(i, pattern);
			i++;
		}
		return this;
	}

	/**
	 * Sets the clickable used for slots, which don't have a proper assignment in the pattern mappings
	 *
	 * @param empty new "empty" clickable
	 * @return this
	 */
	@NotNull
	public InventoryGUIPatternBuilder empty(@Nullable ClickableLike empty) {
		this.empty = empty;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUI build() {
		InventoryGUIBuilder builder = super.clone(); // Don't need this builder's state

		for (int i = 0; i < 6; i++){
			String pattern = rowPatterns.get(i);
			String[] split = pattern.split("");
			int slot = 0;
			for (String characterStr : split){
				char character = characterStr.charAt(0);
				Set<ClickableLike> clickableLikes = clickablesByChar.get(character);
				int currentSlot = slot*i;

				if (clickableLikes == null && empty == null){
					slot++;
					continue;
				} else if (clickableLikes == null){
					slot++;
					builder.clickable(currentSlot, empty);
					continue;
				}
				builder.clickable(slot*i, clickableLikes);
				slot++;
			}
		}

		return builder.build();
	}

	/**
	 * Returns this builder instance
	 * @return this
	 */
	@Override
	public InventoryGUIPatternBuilder clone() {
		return new InventoryGUIPatternBuilder(this);
	}

	/**
	 * Returns this builder instance
	 * @return this
	 */
	@Override
	public InventoryGUIPatternBuilder patternBuilder() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder generationExceptionPlayerHandler(Consumer<Player> generationExceptionPlayerHandler) {
		return (InventoryGUIPatternBuilder) super.generationExceptionPlayerHandler(generationExceptionPlayerHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder builderExceptionPlayerHandler(Consumer<Void> builderExceptionPlayerHandler) {
		return (InventoryGUIPatternBuilder) super.builderExceptionPlayerHandler(builderExceptionPlayerHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder replaceItemsEachOpen() {
		return (InventoryGUIPatternBuilder) super.replaceItemsEachOpen();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder openConsumer(@Nullable Consumer<@NotNull Player> openConsumer) {
		return (InventoryGUIPatternBuilder) super.openConsumer(openConsumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder closeConsumer(@Nullable Consumer<@NotNull Player> closeConsumer) {
		return (InventoryGUIPatternBuilder) super.closeConsumer(closeConsumer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(Collection<Integer> slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.addClickable(slots, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(Collection<Integer> slots, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.addClickable(slots, clickable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(int[] slots, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.addClickable(slots, clickable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(int[] slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.addClickable(slots, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(int slot, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.addClickable(slot, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder addClickable(int slot, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.addClickable(slot, clickable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(Collection<Integer> slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.clickable(slots, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(Collection<Integer> slots, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.clickable(slots, clickable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(int[] slots, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.clickable(slots, clickable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(int[] slots, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.clickable(slots, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(int slot, @NotNull Collection<? extends @NotNull ClickableLike> clickables) {
		return (InventoryGUIPatternBuilder) super.clickable(slot, clickables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder clickable(int slot, @NotNull ClickableLike clickable) {
		return (InventoryGUIPatternBuilder) super.clickable(slot, clickable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public @NotNull InventoryGUIPatternBuilder background(@Nullable Background background) {
		return (InventoryGUIPatternBuilder) super.background(background);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @NotNull InventoryGUIPatternBuilder background(@Nullable BackgroundBuilder backgroundBuilder) {
		return (InventoryGUIPatternBuilder) super.background(backgroundBuilder);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder placeholderGenerator(@NotNull Function<Player, PlaceholderCollection> generator) {
		return (InventoryGUIPatternBuilder) super.placeholderGenerator(generator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder messenger(@NotNull Messenger messenger) {
		return (InventoryGUIPatternBuilder) super.messenger(messenger);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InventoryGUIPatternBuilder title(@NotNull TranslationKey name) {
		return (InventoryGUIPatternBuilder) super.title(name);
	}
}
