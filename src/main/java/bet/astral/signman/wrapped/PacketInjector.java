package bet.astral.signman.wrapped;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.signman.SignGUI;
import bet.astral.signman.SignResult;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketInjector {
	private final JavaPlugin plugin;
	private final boolean async;
	private final NamespacedKey key;

	public PacketInjector(JavaPlugin plugin, boolean async) {
		this.plugin = plugin;
		this.async = async;
		key = new NamespacedKey(plugin, "sign_packet_handler");
	}

	public JavaPlugin getPlugin() {
		return null;
	}

	public boolean async() {
		return false;
	}

	public void addInjector(Player player) {
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
		Channel channel = serverPlayer.connection.connection.channel;

		if (channel.pipeline().get(key.toString()) != null)
			return;
		channel.pipeline().addAfter("decoder", key.toString(), new PacketHandler(plugin, async, player));
	}

	public void removeInjector(Player player) {
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
		Channel channel = serverPlayer.connection.connection.channel;

		if (channel.pipeline().get(key.toString()) == null) return;
		channel.pipeline().remove(key.toString());
	}

	public void sendSign(Player player, SignGUI gui) {
		addInjector(player);
		handle(player, gui);
	}

	private List<Component> handleMessengerTranslations(Player player, SignGUI gui) {
		Map<Integer, Component> lines = new HashMap<>();
		if (gui.getMessenger() == null){
			return gui.getLines();
		}
		Messenger messenger = gui.getMessenger();
		PlaceholderCollection collection = gui.getPlaceholderGenerator() != null ? gui.getPlaceholderGenerator().apply(player) : new PlaceholderList();
		for (int i = 0; i < 3; i++){
			lines.put(i, gui.getLines().get(i));
			TranslationKey key = gui.getTranslationKeyLines().get(i);
			if (key == null) continue;
			Component component = messenger.parseComponent(new MessageInfoBuilder(key)
					.withReceiver(player)
					.withPlaceholders(collection).build(), ComponentType.CHAT);

			if (component == null) continue;

			lines.put(i, component);
		}

		return lines.values().stream().toList();
	}

	private void handle(Player player, SignGUI gui){
		Location location = player.getLocation().add(0, -2, 0);
		if (location.getY()<location.getWorld().getMinHeight()){
			location.add(0, 4, 0);
		}
		if (gui.getOpenConsumer() != null) {
			gui.getOpenConsumer().accept(player);
		}

		BlockPos pos = CraftLocation.toBlockPosition(location);
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

		Material blockType = gui.getMaterial().getMaterial(gui.getSignSize());
		BlockData blockData = blockType.createBlockData();
		Sign sign = (Sign) blockData.createBlockState();
		SignSide side = sign.getSide(Side.FRONT);
		side.setColor(gui.getColor());
		int i = 0;
		for (Component component : handleMessengerTranslations(player, gui)){
			if (i>3){
				break;
			}
			side.line(i, component);
			i++;
		}
		player.sendBlockChange(location, blockData);
		player.sendBlockUpdate(location, sign);

		ClientboundOpenSignEditorPacket editorPacket = new ClientboundOpenSignEditorPacket(pos, true);
		serverPlayer.connection.send(editorPacket);

		PacketHandler.PACKET_HANDLERS.put(player.getUniqueId(), packet-> {
			if (!(packet instanceof ServerboundSignUpdatePacket signUpdatePacket)){
				return false;
			}

			if (!async){
				player.sendBlockChange(location, location.getBlock().getBlockData());
			} else {
				Bukkit.getScheduler().runTask(plugin, ()->{
					player.sendBlockChange(location, location.getBlock().getBlockData());
				});

				String[] lines = signUpdatePacket.getLines();
				List<Component> components = new ArrayList<>();
				for (String line : lines){
					components.add(gui.getSerializer().deserialize(line));
				}
				gui.accept(player, new SignResult(components));
			}
			return true;
		});
	}
}
