package bet.astral.signman;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public interface SignAction {
	boolean keepOpen();
	void run(Player player, List<Component> lines);
}
