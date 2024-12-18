package bet.astral.guiman.clickable;

import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.translation.TranslationKey;
import de.cubbossa.translations.ComponentSplit;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Clickable is a button in GUIMan inventories.
 * Handles all click actions and permission handling using all info provided to clickable
 * @see ClickableBuilder
 */
@Getter
public final class Clickable implements Comparable<Clickable>, ClickableLike{
	public static final NamespacedKey ITEM_KEY = new NamespacedKey("guiman", "guiman_inventory_item");
	static final Random RANDOM = new Random(System.nanoTime());
	public static final Clickable EMPTY = Clickable.builder(Material.AIR).priority(0).displayIfNoPermissions().build();
	@ApiStatus.Obsolete
	public static final Component PERMISSION_MESSAGE_DEFAULT = Component.text("Sorry, but you do not have permissions to use this", NamedTextColor.RED);
	public static final TranslationKey PERMISSION_MESSAGE_TRANSLATION = TranslationKey.of("guiman.clickable.no-permissions");

	private final int priority;
	@NotNull
	@Getter(AccessLevel.NONE)
	private final ItemStack itemStack;
	@NotNull
	private final Permission permission;
	private final boolean displayIfNoPermissions;
	@NotNull
	private final Map<ClickType, ClickAction> actions;
	@NotNull
	private final Map<String, Object> data;
	private final int id = RANDOM.nextInt();
	private final boolean async;
	@Nullable
	private final TranslationKey permissionMessage;
	@Nullable
	private final TranslationKey itemName;
	@Nullable
	private final TranslationKey itemLore;
	@NotNull
	private final Function<Player, PlaceholderCollection> placeholderGenerator;

	/**
	 * Creates a new clickable. Not recommended to be used. Use {@link ClickableBuilder}
	 * @param priority the priority of the clickable. The higher priority, the higher priority is for it to be displayed
	 * @param itemStack item stack
	 * @param permission permission to use clickable
	 * @param displayIfNoPermissions should the item be displayed if no permission
	 * @param actions actions when clicking
	 * @param data cached data stored in clickable
	 * @param isAsync is actions asynchronous?
	 * @param permissionMessage permission message translation
	 * @param itemName item name translations
	 * @param itemLore item lore translation
	 * @param placeholderGenerator placeholder generator
	 */
	public Clickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, @NotNull Map<ClickType, ClickAction> actions, @Nullable Map<String, Object> data, boolean isAsync, @Nullable TranslationKey permissionMessage, @Nullable TranslationKey itemName, @Nullable TranslationKey itemLore, @Nullable Function<Player, PlaceholderCollection> placeholderGenerator) {
		this.priority = priority;
		this.actions = actions;
		this.itemStack = itemStack;
		this.permission = permission;
		this.displayIfNoPermissions = displayIfNoPermissions;
		this.data = data != null ? new HashMap<>(data) : new HashMap<>();
		this.async = isAsync;
		this.permissionMessage = permissionMessage;
		this.itemName = itemName;
		this.itemLore = itemLore;
		this.placeholderGenerator = placeholderGenerator != null ? placeholderGenerator : p->new PlaceholderList();
		generateIds();
	}

	/**
	 * Creates clickable without any actions
	 * @param itemStack item stack
	 * @return clickable
	 */
	public static ClickableBuilder noTooltip(ItemStack itemStack) {
		return new ClickableBuilder(itemStack).hideTooltip();
	}

	/**
	 * Creates clickable without tooltips and click actions
	 * @param material material
	 * @return clickable
	 */
	public static ClickableBuilder noTooltip(@NotNull Material material) {
		return Clickable.builder(material).hideTooltip();
	}

	/**
	 * Creates clickable with given item stack and makes the general click actions execute given action
	 * @param itemStack item stack
	 * @param action action
	 * @return clickable
	 */
	public static ClickableBuilder general(ItemStack itemStack, ClickAction action){
		return new ClickableBuilder(itemStack).actionGeneral(action);
	}

	/**
	 * Creates a clickable builder
	 * @return builder
	 * @deprecated Use {@link #builder(Material)}
	 */
	@NotNull
	@Deprecated(forRemoval = true)
	@ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
	public static ClickableBuilder builder(){
		return builder(Material.AIR);
	}

	/**
	 * Creates a clickable builder with given item stack as icon
	 * @param itemStack item stack
	 * @return builder
	 */
	@NotNull
	public static ClickableBuilder builder(@NotNull ItemStack itemStack){
		return new ClickableBuilder(itemStack);
	}

	/**
	 * Creates a clickable builder with given material as icon
	 * @param material material
	 * @return builder
	 */
	@NotNull
	public static ClickableBuilder builder(@NotNull Material material){
		return new ClickableBuilder(new ItemStack(material));
	}

	/**
	 * Creates a clickable builder with given item stack as a material
	 * @param material material
	 * @param meta meta-consumer, to edit meta when item stack is created
	 * @return builder
	 */
	@NotNull
	public static ClickableBuilder builder(@NotNull Material material, Consumer<ItemMeta> meta){
		ItemStack itemStack1 = new ItemStack(material);
		itemStack1.editMeta(meta);
		return new ClickableBuilder(itemStack1);
	}

	/**
	 * Creates a clickable builder with given item stack as material.
	 * @param material material
	 * @param meta meta
	 * @param type the type of item meta
	 * @return clickable
	 * @param <Meta> the type of item meta
	 */
	@NotNull
 	public static <Meta extends ItemMeta> ClickableBuilder builder(@NotNull Material material, @NotNull Consumer<Meta> meta, @NotNull Class<Meta> type){
		ItemStack itemStack1 = new ItemStack(material);
		itemStack1.editMeta(type, meta);
		return new ClickableBuilder(itemStack1);
	}

	/**
	 * Generates an id for this clickable.
	 */
	private void generateIds(){
		if (itemStack.getType()==Material.AIR){
			return;
		}
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null){
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(ITEM_KEY, PersistentDataType.INTEGER, id);
		itemStack.setItemMeta(meta);
	}

	/**
	 * Sets cached data to the clickable
	 * @param data data
	 * @param value value
	 */
	public void setData(String data, Object value){
		this.data.put(data, value);
	}

	/**
	 * Overrides all of the cached data and stores given data to cached data
	 * @param data data
	 */
	public void setData(Map<String, Object> data) {
		this.data.clear();
		if (data == null){
			return;
		}
		this.data.putAll(data);
	}

	/**
	 * Clears all the cached data
	 */
	public void clearData(){
		this.data.clear();
	}

	/**
	 * Removes given key's data from cached data
	 * @param key key
	 */
	public void clearData(String key){
		this.data.remove(key);
	}

	public Object getData(String data){
		return this.data.get(data);
	}

	@Override
	public int compareTo(@NotNull Clickable o) {
		return Integer.compare(getPriority(), o.getPriority());
	}

	@Override
	public ItemStack generate(@Nullable Messenger messenger, @NotNull Player player) {
		if (messenger == null){
			return itemStack;
		}
		Receiver receiver = messenger.convertReceiver(player);
		if (receiver == null){
			return itemStack;
		}

		ItemMeta meta = itemStack.getItemMeta();
		PlaceholderCollection placeholders = getPlaceholderGenerator().apply(player);
		if (placeholders==null){
			placeholders = new PlaceholderList();
		}

		if (itemName != null){
			meta.displayName(messenger
					.disablePrefixForNextParse()
					.parseComponent(new MessageInfoBuilder(itemName).withPlaceholders(placeholders).create(), ComponentType.CHAT, receiver));
		} else if (itemLore != null){
			Component component = messenger
					.disablePrefixForNextParse()
					.parseComponent(new MessageInfoBuilder(itemLore)
							.withPlaceholders(placeholders).create(), ComponentType.CHAT, receiver);
			if (component != null){
				meta.lore(ComponentSplit.split(component, "\n"));
			}
		}
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Override
	public Clickable asClickable() {
		return this;
	}

	public void sendPermissionMessage(Player player, @Nullable Messenger messenger) {
		if (permissionMessage == null){
			if (messenger.getLocaleFromReceiver(player) != null
					&& messenger.getBaseComponent(PERMISSION_MESSAGE_TRANSLATION, Objects.requireNonNull(messenger.getLocaleFromReceiver(player)))!=null){
				messenger.message(player, PERMISSION_MESSAGE_TRANSLATION);
			}
		} else {
			if (messenger != null) {
				messenger.message(player, permissionMessage);
			}
		}
		player.sendMessage(PERMISSION_MESSAGE_DEFAULT);
	}
}