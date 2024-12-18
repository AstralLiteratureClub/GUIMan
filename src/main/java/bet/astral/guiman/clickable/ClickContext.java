package bet.astral.guiman.clickable;

import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.utils.MessageSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public final class ClickContext {
	private final InventoryGUI gui;
	private final Inventory inventory;
	private final ItemStack itemStack;
	private final ClickType clickType;
	private final Player who;
	private final Clickable clickable;
	private final MessengerInfo messengerInfo;
//	@Nullable
//	private final InventoryClickEvent inventoryClickEvent;
//	@Nullable
//	private final InventoryDragEvent inventoryDragEvent;

	@Getter
	@RequiredArgsConstructor
	public static final class MessengerInfo implements MessageSender.Packed {
		private final Messenger messenger;
		private final Receiver receiver;
		private final boolean isMessengerSet;
	}
}
