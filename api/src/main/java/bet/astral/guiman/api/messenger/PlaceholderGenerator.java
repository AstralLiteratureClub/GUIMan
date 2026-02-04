package bet.astral.guiman.api.messenger;

import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import org.bukkit.entity.Player;

import java.util.function.Function;

public interface PlaceholderGenerator extends Function<Player, PlaceholderCollection> {
}
