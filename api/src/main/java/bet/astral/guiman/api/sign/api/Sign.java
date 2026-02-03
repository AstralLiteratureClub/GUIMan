package bet.astral.guiman.api.sign.api;

import bet.astral.guiman.api.inventory.annotations.MinecraftVersion;
import bet.astral.guiman.api.inventory.annotations.UseMessenger;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.guiman.api.sign.api.material.ISignMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Sign is a ram cached representation of a sign gui. This is never in the real world of the server. The signs are ran in packet level
 */
public interface Sign {
    /**
     * Returns the material used for this sign
     * @return material
     */
    ISignMaterial getMaterial();

    /**
     * Returns the text color. Dye color is used to recolor the text in the GUI
     * @return text dye color
     */
    @MinecraftVersion("1.14")
    DyeColor getTextColor();

    /**
     * Returns true if the sign is a normal sign.
     * @return if this is a normal sign
     */
    boolean isLargeSign();

    /**
     * Returns true if this is a hanging sign. This method returns FALSE if this is used before 1.20
     * @return if this is a hanging sign
     */
    @MinecraftVersion("1.20")
    boolean isSmallSign();

    /**
     * Returns the lines used in the sign GUI
     * @return lines
     */
    List<Component> getLines();

    /**
     * Returns the translation keys used for the sign lines.
     * @return lines
     */
    @UseMessenger
    List<TranslationKey> getTranslationKeyLines();

    /**
     * Returns the sign handler used when the sign is signed by the play
     * @return
     */
    SignHandler getHandler();

    /**
     * Returns the component serializer used to deserialize components
     * @return serializer
     */
    ComponentSerializer<? extends Component, ? extends Component, String> getSerializer();

    /**
     * Returns the messenger. Messenger can be used to rename the translation keys to translations
     * @return messenger
     */
    @UseMessenger
    Messenger getMessenger();

    /**
     * Returns the placeholder generator used when translating the messenger translations to text
     * @return generator
     */
    @UseMessenger
    Function<Player, PlaceholderCollection> getPlaceholderGenerator();

    /**
     * Returns the consumer ran when the sign is run
     * @return sign open consumer
     */
    Consumer<Player> getOpenConsumer();

    /**
     * Opens the SIGN to the player
     * @param player player
     */
    void open(Player player);

    /**
     * Used internally to sign the sign after receiving it from the packets
     * @param player player
     * @param signResult result
     */
    @ApiStatus.Internal
    void sign(Player player, SignResult signResult);
}
