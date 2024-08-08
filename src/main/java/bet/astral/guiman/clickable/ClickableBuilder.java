package bet.astral.guiman.clickable;

import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class instance builder for {@link Clickable}.
 */
public class ClickableBuilder implements Cloneable, ClickableLike {
	private int priority = 0;
	private @NotNull ItemStack itemStack;
	private Permission permission = Permission.NONE;
	private boolean displayIfNoPermissions = false;
	private Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions = new HashMap<>();
	private Map<String, Object> data;
	private boolean async = true;
	private TranslationKey permissionMessage = null;
	private TranslationKey itemName = null;
	private TranslationKey itemLore = null;
	private Function<Player, PlaceholderList> placeholderGenerator = (p)->new PlaceholderList();

	/**
	 * Creates a new instance of clickable builder. Uses {@link Material#AIR} as the item stack display
	 */
	public ClickableBuilder() {
		this.itemStack = ItemStack.of(Material.AIR);
	}
	/**
	 * Creates a new instance of clickable builder.
	 * @param itemStack item stack
	 */
	public ClickableBuilder(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	/**
	 * Creates a new instance of clickable builder using given material.
	 * @param material item
	 */
	public ClickableBuilder(@NotNull Material material) {
		this.itemStack = new ItemStack(material);
	}
	/**
	 * Creates a new instance of clickable builder using given material.
	 * Provides item meta-consumer so item meta does not need to be set using {@link ItemStack}
	 * @param material item
	 */
	public ClickableBuilder(@NotNull Material material, @NotNull Consumer<ItemMeta> itemMeta){
		itemStack = new ItemStack(material);
		itemStack.editMeta(itemMeta);
	}
	/**
	 * Creates a new instance of clickable builder using given material.
	 * Provides item meta-consumer so item meta does not need to be set using {@link ItemStack}.
	 * Provides {@link Meta Meta} item meta-consumer
	 * @param material item
	 */
	public <Meta extends ItemMeta> ClickableBuilder(@NotNull Material material, @NotNull Consumer<Meta> meta, Class<Meta> metaClass){
		itemStack = new ItemStack(material);
		itemStack.editMeta(metaClass, meta);
	}

	/**
	 * Sets the priority of clickable, the higher priority, the higher priority for it to be used
	 * @param priority priority
	 * @return this
	 */
	@NotNull
	public ClickableBuilder priority(int priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * Sets data value which clickables can be used by built clickable
	 * @param key key
	 * @param value value
	 * @return this
	 */
	@NotNull
	public ClickableBuilder data(String key, Object value){
		if (this.data == null){
			this.data = new HashMap<>();
		}
		this.data.put(key, value);
		return this;
	}

	@Contract(value = "_ -> this", mutates = "this")
	@NotNull
	private ClickableBuilder data(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	/**
	 * Returns data value using key, if data is null, returns null
	 * @param key key
	 * @return nullable data
	 */
	@Nullable
	public Object getData(String key){
		if (this.data != null){
			return this.data.get(key);
		}
		return null;
	}

	/**
	 * Makes the displayed item to the given item stack
	 * @param itemStack itemStack
	 * @return this
	 */
	@NotNull
	public ClickableBuilder item(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	/**
	 * Makes the displayed item to the given item stack
	 * @param material material
	 * @return this
	 */
	@NotNull
	public ClickableBuilder item(@NotNull Material material) {
		this.itemStack = ItemStack.of(material);
		return this;
	}

	/**
	 * Makes the displayed item to the given item stack
	 * @param material material
	 * @param meta item meta
	 * @return this
	 */
	@NotNull
	public ClickableBuilder item(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
		this.itemStack = ItemStack.of(material);
		itemStack.editMeta(meta);
		return this;
	}

	/**
	 * Makes the displayed item to the given item stack, gives consumer of item meta of given class type
	 * @param material material
	 * @param meta meta
	 * @param metaClass meta class type
	 * @return this
	 * @param <Meta> Item meta-type
	 */
	@NotNull
	public <Meta extends ItemMeta> ClickableBuilder item(@NotNull Material material, @NotNull Consumer<Meta> meta, Class<Meta> metaClass){
		itemStack = new ItemStack(material);
		itemStack.editMeta(metaClass, meta);
		return this;
	}

	/**
	 * Makes the displayed item to the given item stack, gives consumer meta of given item
	 * @param material material
	 * @param meta meta
	 * @return this
	 */
	@NotNull
	public ClickableBuilder item(@NotNull Material material, @NotNull ItemMeta meta) {
		this.itemStack = ItemStack.of(material);
		itemStack.setItemMeta(meta);
		return this;
	}

	/**
	 * Sets the permission to display this clickable
	 * @param permission permission
	 * @return this
	 */
	@NotNull
	public ClickableBuilder permission(@NotNull String permission){
		this.permission = Permission.of(permission);
		return this;
	}

	/**
	 * Sets the permission to display this clickable
	 * @param permission permission
	 * @return this
	 */
	@NotNull
	public ClickableBuilder permission(@NotNull Permission permission){
		this.permission = permission;
		return this;
	}

	/**
	 * Sets the permission to display this clickable
	 * @param permission permission
	 * @return this
	 */
	@NotNull
	public ClickableBuilder permission(@NotNull Predicate<Player> permission){
		this.permission = Permission.of(permission);
		return this;
	}

	/**
	 * Sets if this is displayed, even if the player has no permission to use clickable
	 * @return this
	 */
	@NotNull
	public ClickableBuilder displayIfNoPermissions(){
		this.displayIfNoPermissions = true;
		return this;
	}

	/**
	 * Overrides the actions for this clickable
	 * @param actions actions
	 * @return this
	 */
	@NotNull
	public ClickableBuilder action(Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions){
		this.actions = new HashMap<>(actions);
		return this;
	}

	/**
	 * Sets the click action for given click type using given action consumer
	 * @param type type
	 * @param action action
	 * @return this
	 */
	@NotNull
	public ClickableBuilder action(@NotNull ClickType type, @NotNull TriConsumer<Clickable, ItemStack, Player> action){
		this.actions.put(type, action);
		return this;
	}

	/**
	 * Sets the click action for given click type using given action consumer
	 * @param types click types
	 * @param action action
	 * @return this
	 */
	@NotNull
	public ClickableBuilder action(@NotNull ClickType[] types, @NotNull TriConsumer<Clickable, ItemStack, Player> action){
		for (ClickType clickType : types){
			this.actions.put(clickType, action);
		}
		return this;
	}

	/**
	 * Sets the click action for given click type using given action consumer
	 * @param types click types
	 * @param action action
	 * @return this
	 */
	@NotNull
	public ClickableBuilder action(@NotNull Collection<ClickType> types, @NotNull TriConsumer<Clickable, ItemStack, Player> action){
		for (ClickType clickType : types){
			this.actions.put(clickType, action);
		}
		return this;
	}

	/**
	 * Sets the action for all click types to given action
	 * @param action action
	 * @return this
	 */
	@NotNull
	public ClickableBuilder actionAll(@NotNull TriConsumer<Clickable, ItemStack, Player> action){
		return action(ClickType.values(), action);
	}

	/**
	 * Sets the general click types to have given action
	 * @param action action
	 * @return this
	 */
	public ClickableBuilder actionGeneral(@NotNull TriConsumer<Clickable, ItemStack, Player> action){
		action(ClickType.SHIFT_LEFT, action);
		action(ClickType.SHIFT_RIGHT, action);
		action(ClickType.LEFT, action);
		action(ClickType.RIGHT, action);
		return this;
	}

	/**
	 * Makes all actions executed using this clickable in ASynchronous thread.
	 * @return this
	 */
	public ClickableBuilder async(){
		this.async = true;
		return this;
	}
	/**
	 * Makes all actions executed using this clickable in bukkit main synchronous thread or folia entity scheduler.
	 * @return this
	 */
	public ClickableBuilder sync(){
		this.async = false;
		return this;
	}

	/**
	 * Sets the placeholder generator
	 *
	 * @param placeholderGenerator placeholder generator
	 * @return this
	 */
	public ClickableBuilder placeholderGenerator(@NotNull Function<Player, PlaceholderList> placeholderGenerator) {
		this.placeholderGenerator = placeholderGenerator;
		return this;
	}

	/**
	 * Sets the lore override used if translation item lore
	 *
	 * @param itemLore item lore override
	 * @return this
	 */
	public ClickableBuilder description(@Nullable TranslationKey itemLore) {
		this.itemLore = itemLore;
		return this;
	}

	/**
	 * Sets the name override used if translation item name
	 *
	 * @param itemName item name override
	 * @return this
	 */
	public ClickableBuilder title(@Nullable TranslationKey itemName) {
		this.itemName = itemName;
		return this;
	}

	/**
	 * Sets the permission message translation key
	 *
	 * @param permissionMessage permission message
	 * @return this
	 */
	public ClickableBuilder permissionMessage(@Nullable TranslationKey permissionMessage) {
		this.permissionMessage = permissionMessage;
		return this;
	}

	/**
	 * Builds the clickable builder using all given values
	 * @return this
	 */
	public Clickable build(){
		return new Clickable(priority, itemStack, permission, displayIfNoPermissions, actions, data, async, permissionMessage, itemName, itemLore, placeholderGenerator);
	}

	/**
	 * Clones the clickable builder to a new instance
	 * @return new instance
	 */
	@Override
	public ClickableBuilder clone(){
		ClickableBuilder builder = new ClickableBuilder(itemStack);
		builder.priority = priority;
		builder.itemStack = itemStack;
		builder.permission = permission;
		builder.displayIfNoPermissions = displayIfNoPermissions;
		builder.actions = actions;
		builder.data = data;
		builder.async = async;
		return builder;
	}

	@Override
	public Clickable asClickable() {
		return build();
	}

	@Override
	public ItemStack generate(Messenger messenger, Player player) {
		return build().generate(messenger, player);
	}
}