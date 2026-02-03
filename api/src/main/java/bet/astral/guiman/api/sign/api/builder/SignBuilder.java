package bet.astral.guiman.api.sign.api.builder;

import bet.astral.guiman.api.inventory.annotations.MinecraftVersion;
import bet.astral.guiman.api.inventory.annotations.UseMessenger;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.guiman.api.sign.api.material.ISignMaterial;
import bet.astral.guiman.api.sign.api.Sign;
import bet.astral.guiman.api.sign.api.SignHandler;
import bet.astral.guiman.api.sign.api.SignSize;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Sing builder to build a new instance of {@link Sign}
 */
public interface SignBuilder {
    /**
     * Sets the material of the sign
     * @param material material
     * @return this
     */
    SignBuilder setMaterial(ISignMaterial material);
    /**
     * Sets the sign material to a random sign material out of all materials. Option to choose the sign size
     * @param size size of sign
     * @return this
     */
    @MinecraftVersion("1.20")
    SignBuilder setRandomMaterial(SignSize size);

    /**
     * Sets the sign material to a random sign material out of all materials. Uses the original sign size
     * @return this
     */
    SignBuilder setRandomMaterial();

    /**
     * Sets the messenger used, when generating this sign and translating lines
     * @param messenger messenger
     * @return translator
     */
    @UseMessenger
    SignBuilder setMessenger(Messenger messenger);
    /**
     * Sets the placeholder generator used, when trying to generate the sign with messenger
     * @param placeholderGenerator placeholder generator
     * @return this
     */
    @UseMessenger
    SignBuilder setPlaceholderGenerator(Function<Player, PlaceholderCollection> placeholderGenerator);

    SignBuilder setLinesPlain(@NotNull List<String> lines);

    SignBuilder setLinesPlain(String @NotNull ... lines);

    /**
     * Sets the given lines to the given translations which are translated by the messenger
     * @param lines lines
     * @return this
     */
    @UseMessenger
    SignBuilder setLines(TranslationKey @NotNull ... lines);

    /**
     * Sets the `line` to the given translation which is translated using the provided messenger
     * @param line line
     * @param translationKey translation key
     * @return this
     */
    @UseMessenger
    SignBuilder setLine(@Range(from = 1, to = 4) int line, TranslationKey translationKey);

    /**
     *
     * @param line
     * @param string
     * @return
     */
    SignBuilder setLinePlain(@Range(from = 1, to = 4) int line, String string);

    /**
     * Sets the lines of the sign to the given lines
     * @param lines lines
     * @return this
     */
    SignBuilder setLines(@NotNull List<Component> lines);

    /**
     * Sets the lines of the sign to the given lines
     * @param lines lines
     * @return this
     */
    SignBuilder setLines(Component @NotNull ... lines);

    /**
     * Sets a sign line to the given text
     * @param line line number
     * @param text text
     * @return this
     */
    SignBuilder setLine(@Range(from = 1, to = 4) int line, Component text);

    /**
     * Sets the text color to the given dye color. This is visible in the sign GUI itself
     * @param color color
     * @return this
     */
    @MinecraftVersion("1.14")
    SignBuilder setColor(DyeColor color);

    /**
     * Sets the sign player sign handler. This is used when the sign is signed by the player
     * @param handler handler
     * @return this
     */
    SignBuilder setHandler(SignHandler handler);

    /**
     * Sets the consumer used when the player opens the sign
     * @param openConsumer consumer
     * @return this
     */
    SignBuilder setOpenConsumer(Consumer<Player> openConsumer);

    /**
     * Builds the completed sign
     * @return sign
     * @throws IllegalStateException if the sign does not have a material.
     */
    Sign build();
}
