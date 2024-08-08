package bet.astral.guiman;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableLike;
import bet.astral.guiman.clickable.ClickableProvider;
import bet.astral.guiman.permission.Permission;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;


@ApiStatus.Internal
public class InteractableGUI implements InventoryHolder {
	private final InventoryGUI gui;
	private final Inventory inventory;

	public InteractableGUI(@NotNull InventoryGUI gui, @NotNull Player player) {
		this.gui = gui;
		if (gui.getType() == InventoryType.CHEST) {
			if (gui.getName() == null && gui.getNameTranslation() == null) {
				inventory = Bukkit.createInventory(this, gui.getSlots());
			} else {
				inventory = Bukkit.createInventory(this, gui.getSlots(), gui.getNameTranslation() != null ?
						gui.getMessenger().parseComponent(new MessageInfoBuilder(gui.getNameTranslation())
								.addPlaceholders(gui.getPlaceholderGenerator().apply(player)).create(), ComponentType.CHAT, gui.getMessenger().convertReceiver(player)) : gui.getName());
			}
		} else {
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getType());
			} else {
				inventory = Bukkit.createInventory(this, gui.getType(), gui.getNameTranslation() != null ?
						gui.getMessenger().parseComponent(new MessageInfoBuilder(gui.getNameTranslation())
								.addPlaceholders(gui.getPlaceholderGenerator().apply(player)).create(), ComponentType.CHAT, gui.getMessenger().convertReceiver(player)) : gui.getName());
			}
		}
		generate(player, gui.getMessenger());
	}


	public void generate(@NotNull Player player, @Nullable Messenger messenger) {
		inventory.clear();
		Background background = gui.getBackground();
		for (int i = 0; i < inventory.getSize(); i++) {
			if (i > inventory.getSize()) {
				return;
			}

			boolean isBackground = false;
			Collection<ClickableLike> clickables = gui.getClickables().get(i);
			if (clickables == null || clickables.isEmpty()) {
				if (gui.getBackground() != null) {
					ClickableLike clickableLike = background.getSlotOrEmpty(i);
					clickables = List.of(clickableLike);
					isBackground = true;
				} else {
					return;
				}
			}

			List<Clickable> clickableList =
					clickables.
							stream().
							map(clickableLike ->
									clickableLike instanceof ClickableProvider provider
											? provider.provide(player)
											: clickableLike.asClickable())
							.distinct()
							.sorted()
							.collect(Collectors.toList());
			Collections.reverse(clickableList);

			for (Clickable clickable : clickableList) {
				if (clickable == null){
					clickable = gui.registerClickable(background.getSlotOrEmpty(i), player);
					isBackground = true;
				}
				gui.registerClickable(clickable, player);
				if (!isBackground && (hasPermission(player, clickable.getPermission()) || !hasPermission(player, clickable.getPermission()) && clickable.isDisplayIfNoPermissions())) {
					inventory.setItem(i, clickable.generate(messenger, player));
					break;
				} else {
					inventory.setItem(i, clickable.generate(messenger, player));
				}
			}
		}
	}


	public InventoryGUI getGUI(){
		return gui;
	}

	private boolean hasPermission(@Nullable Player player, Permission permission){
		if (permission == null) {
			return true;
		}
		if (player == null){
			return false;
		}
		return permission.hasPermission(player);
	}


	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}
}