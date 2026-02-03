package bet.astral.signman.v1_20_5.network;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.guiman.api.sign.api.Sign;
import bet.astral.guiman.api.sign.api.SignResult;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NettyInjector_v1_20_5 {
	private final JavaPlugin plugin;
	private final boolean async;
	private final NamespacedKey key;

	public NettyInjector_v1_20_5(JavaPlugin plugin, boolean async) {
		this.plugin = plugin;
		this.async = async;
		key = new NamespacedKey(plugin, "sign_packet_handler");
	}

	public void addInjector(Player player) {
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
		Channel channel = serverPlayer.connection.connection.channel;

		if (channel.pipeline().get(key.toString()) != null)
			return;
		channel.pipeline().addAfter("decoder", key.toString(), new NettyHandler_v1_20_5(plugin, async, player));
	}

	public void removeInjector(Player player) {
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
		Channel channel = serverPlayer.connection.connection.channel;

		if (channel.pipeline().get(key.toString()) == null) return;
		channel.pipeline().remove(key.toString());
	}

	public void sendSign(Player player, Sign gui) {
		addInjector(player);
		handle(player, gui);
	}

    private List<Component> handleMessengerTranslations(Player player, @NotNull Sign gui) {
        if (gui.getMessenger() == null) {
            return gui.getLines();
        }

        Messenger messenger = gui.getMessenger();
        PlaceholderCollection placeholders =
                gui.getPlaceholderGenerator() != null
                        ? gui.getPlaceholderGenerator().apply(player)
                        : new PlaceholderList();

        List<Component> result = new ArrayList<>(4);
        List<Component> baseLines = gui.getLines();
        List<TranslationKey> translationLines = gui.getTranslationKeyLines();

        for (int i = 0; i < 4; i++) {
            Component line = i < baseLines.size() && baseLines.get(i) != null
                    ? baseLines.get(i)
                    : Component.empty();

            if (i < translationLines.size()) {
                TranslationKey key = translationLines.get(i);
                if (key != null) {
                    Component translated = messenger.parseComponent(
                            new MessageInfoBuilder(key)
                                    .withReceiver(player)
                                    .withPlaceholders(placeholders)
                                    .build(),
                            ComponentType.CHAT
                    );

                    if (translated != null) {
                        line = translated;
                    }
                }
            }

            result.add(line);
        }

        return result;
    }


	private void handle(Player player, Sign gui){
		Location location = player.getLocation().add(0, -2, 0);
		if (location.getY()<location.getWorld().getMinHeight()){
			location.add(0, 4, 0);
		}
		if (gui.getOpenConsumer() != null) {
			gui.getOpenConsumer().accept(player);
		}

		BlockPos pos = CraftLocation.toBlockPosition(location);
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

		Material blockType = gui.getMaterial().getMaterial();
		BlockData blockData = blockType.createBlockData();
		org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blockData.createBlockState();
		SignSide side = sign.getSide(Side.FRONT);
		side.setColor(gui.getTextColor());
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

		NettyHandler_v1_20_5.PACKET_HANDLERS.put(player.getUniqueId(), packet-> {
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
				gui.sign(player, new SignResult(components));
			}
			return true;
		});
	}
}
