package bet.astral.guiman.clickable;

import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.translation.TranslationKey;
import de.cubbossa.translations.ComponentSplit;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
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
	public static final Clickable EMPTY = new ClickableBuilder(Material.AIR).priority(0).displayIfNoPermissions().build();
	static final Component PERMISSION_MESSAGE = Component.text("Sorry, but you do not have permissions to use this", NamedTextColor.RED);

	private final int priority;
	@NotNull
	@Getter(AccessLevel.NONE)
	private final ItemStack itemStack;
	@NotNull
	private final Permission permission;
	private final boolean displayIfNoPermissions;
	@NotNull
	private final Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions;
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
	@Nullable
	private final Function<Player, PlaceholderList> placeholderGenerator;

	public Clickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, @NotNull Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions, @Nullable Map<String, Object> data, boolean isAsync, @Nullable TranslationKey permissionMessage, @Nullable TranslationKey itemName, @Nullable TranslationKey itemLore, @Nullable Function<Player, PlaceholderList> placeholderGenerator) {
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
		this.placeholderGenerator = placeholderGenerator;
		generateIds();
	}

	public Clickable(int priority, @NotNull ItemStack itemStack, boolean displayIfNoPermissions, @NotNull Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions, @NotNull Map<String, Object> data, boolean isAsync, @Nullable TranslationKey permissionMessage, @Nullable TranslationKey itemName, @Nullable TranslationKey itemLore, @Nullable Function<Player, PlaceholderList> placeholderGenerator) {
		this.priority = priority;
		this.actions = actions;
		this.itemStack = itemStack;
		this.displayIfNoPermissions = displayIfNoPermissions;
		this.data = data;
		this.async = isAsync;
		this.permissionMessage = permissionMessage;
		this.itemName = itemName;
		this.itemLore = itemLore;
		this.placeholderGenerator = placeholderGenerator;
		this.permission = Permission.NONE;
		generateIds();
	}

	public static Clickable empty(ItemStack itemstack) {
		return new ClickableBuilder(itemstack).build();
	}

	public static Clickable empty(@NotNull Material border) {
		return new ClickableBuilder(border, meta->meta.setHideTooltip(true)).build();
	}

	public static Clickable general(ItemStack itemStack, TriConsumer<Clickable, ItemStack, Player> action){
		return new ClickableBuilder(itemStack).actionGeneral(action).build();
	}

	@NotNull
	public static ClickableBuilder builder(){
		return new ClickableBuilder();
	}
	@NotNull
	public static ClickableBuilder builder(@NotNull ItemStack itemStack){
		return new ClickableBuilder(itemStack);
	}
	@NotNull
	public static ClickableBuilder builder(@NotNull Material material){
		return new ClickableBuilder(material);
	}
	@NotNull
	public static ClickableBuilder builder(@NotNull Material material, Consumer<ItemMeta> meta){
		return new ClickableBuilder(material, meta);
	}
	@NotNull
 	public static <Meta extends ItemMeta> ClickableBuilder builder(@NotNull Material material, @NotNull Consumer<Meta> meta, @NotNull Class<Meta> type){
		return new ClickableBuilder(material, meta, type);
	}

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

	public void setData(String data, Object value){
		this.data.put(data, value);
	}
	public void setData(Map<String, Object> data) {
		this.data.clear();
		if (data == null){
			return;
		}
		this.data.putAll(data);
	}
	public void clearData(){
		this.data.clear();
	}
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
			System.out.println("RETURN Messenger");
			return itemStack;
		}
		Receiver receiver = messenger.convertReceiver(player);
		if (receiver == null){
			System.out.println("RETURN Item");
			return itemStack;
		}

		ItemMeta meta = itemStack.getItemMeta();
		PlaceholderList placeholders = getPlaceholderGenerator() != null ? getPlaceholderGenerator().apply(player) : null;
		if (placeholders==null){
			placeholders = new PlaceholderList();
		}

		if (itemName != null){
			meta.displayName(messenger
					.disablePrefixForNextParse()
					.parseComponent(new MessageInfoBuilder(itemName).addPlaceholders(placeholders).create(), ComponentType.CHAT, receiver));
		} else if (itemLore != null){
			Component component = messenger
					.disablePrefixForNextParse()
					.parseComponent(new MessageInfoBuilder(itemLore)
							.addPlaceholders(placeholders).create(), ComponentType.CHAT, receiver);
			System.out.println("LOREEEEEE");
			if (component != null){
				System.out.println("NOT NULLL");
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
		if (messenger == null || permissionMessage == null){
			player.sendMessage(PERMISSION_MESSAGE);
		} else {
			messenger.message(player, permissionMessage);
		}
	}
}