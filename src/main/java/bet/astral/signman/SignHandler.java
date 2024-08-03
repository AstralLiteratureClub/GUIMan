package bet.astral.signman;

import org.bukkit.entity.Player;

import java.util.List;

@FunctionalInterface
public interface SignHandler {
	List<SignAction> handle(Player player, SignResult info);
}
