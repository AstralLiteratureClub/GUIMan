package bet.astral.guiman.core.inventory.gui;

import bet.astral.guiman.api.GUIMan;
import bet.astral.guiman.api.MessengerConfig;
import bet.astral.guiman.api.inventory.gui.InventoryGUI;
import bet.astral.guiman.api.inventory.gui.MInventoryGUI;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MPlayerInventory extends PlayerInventory {
	/**
	 * Creates a new interactable gui for given player using the given gui. Does not support shared GUIs
	 *
	 * @param gui    inventory base
	 * @param player player to open to
	 */
	public MPlayerInventory(@NotNull InventoryGUI gui, @NotNull Player player) {
		super(gui, player);
	}

	public static void setup() {
		PlayerInventory.createInventory = (holder, t, name) -> {
			if (t instanceof Integer size) {
				if (name == null)
					return Bukkit.createInventory(holder, size);
				return Bukkit.createInventory(holder, size, name);
			} else {
				if (name == null)
					return Bukkit.createInventory(holder, (InventoryType) t);
				return Bukkit.createInventory(holder, (InventoryType) t, name);
			}
		};
	}

	@Override
	protected Component getName(InventoryGUI gui, Player player) {
		if (gui instanceof MInventoryGUI mGUI && mGUI.hasComponentTitle()) {
			final MessengerConfig config = GUIMan.getGUIMan().getMessengerConfig();
			final Messenger messenger = config.getMessenger();
			final TranslationKey title = mGUI.getTitleTranslation();
			final PlaceholderGenerator globalGenerator = config.getPlaceholderGenerator();
			final PlaceholderGenerator localGenerator = mGUI.getPlaceholderGenerator();
			Locale locale = messenger.getLocaleFromReceiver(player);
			if (locale == null) {
				locale = Locale.US;
			}

			PlaceholderList list = new PlaceholderList();
			if (globalGenerator != null) {
				list.addAll(globalGenerator.apply(player));
			}
			if (localGenerator != null) {
				list.addAll(localGenerator.apply(player));
			}

			Component component = messenger
				.disablePrefixForNextParse()
				.parseComponent(
					messenger.createMessage(title).withReceiver(player)
						.withPlaceholders(list).withLocale(locale).build(), ComponentType.CHAT, messenger.convertReceiver(player));

			return component;
		}
		return super.getName(gui, player);
	}
}
