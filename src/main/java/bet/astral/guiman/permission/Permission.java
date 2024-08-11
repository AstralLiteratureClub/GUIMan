package bet.astral.guiman.permission;

import bet.astral.guiman.InventoryGUI;
import org.bukkit.entity.Player;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Permission {
	Permission NONE = new NonePermission();
	static Permission of(String permission){
		return new BukkitPermission(permission);
	}
	static Permission of(Predicate<Player> predicate){
		return new PredicatePermission(predicate);
	}
	static Permission of(BiPredicate<InventoryGUI, Player> predicate) {
		return new BiPreicatePermission(predicate);
	}
	boolean hasPermission(Player player, InventoryGUI inventoryGUI);
	class NonePermission implements Permission {
		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return true;
		}
	}
	class PredicatePermission implements Permission {
		private final Predicate<Player> predicate;

		public PredicatePermission(Predicate<Player> predicate) {
			this.predicate = predicate;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return predicate.test(player);
		}
	}
	class BiPreicatePermission implements Permission {
		private final BiPredicate<InventoryGUI, Player> predicate;

		public BiPreicatePermission(BiPredicate<InventoryGUI, Player> predicate) {
			this.predicate = predicate;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return predicate.test(gui, player);
		}
	}
	class BukkitPermission implements Permission {
		private final String permission;

		public BukkitPermission(String permission) {
			this.permission = permission;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return player.hasPermission(permission);
		}
	}
}
