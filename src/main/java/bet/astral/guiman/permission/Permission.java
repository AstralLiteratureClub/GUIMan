package bet.astral.guiman.permission;

import bet.astral.guiman.InventoryGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Permission {
	Permission NONE = new NonePermission();
	@Contract(value = "_ -> new", pure = true)
	static @NotNull Permission of(String permission){
		return new BukkitPermission(permission, false, false);
	}
	@Contract(value = "_ -> new", pure = true)
	static @NotNull Permission of(Predicate<Player> predicate){
		return new PredicatePermission(predicate, false, false);
	}

	@Contract(value = "_ -> new", pure = true)
	static @NotNull Permission of(BiPredicate<InventoryGUI, Player> predicate) {
		return new BiPreicatePermission(predicate, false, false);
	}

	@Contract(value = "_, _, _ -> new", pure = true)
	static @NotNull Permission of(String permission, boolean display, boolean runAction){
		return new BukkitPermission(permission, display, runAction);
	}
	@Contract(value = "_, _, _ -> new", pure = true)
	static @NotNull Permission of(Predicate<Player> predicate, boolean display, boolean runAction){
		return new PredicatePermission(predicate, display, runAction);
	}
	@Contract(value = "_, _, _ -> new", pure = true)
	static @NotNull Permission of(BiPredicate<InventoryGUI, Player> predicate, boolean display, boolean runAction) {
		return new BiPreicatePermission(predicate, display, runAction);
	}

	boolean hasPermission(Player player, InventoryGUI inventoryGUI);
	boolean displayIfHasNoPermission();
	boolean runActionIfHasNoPermission();
	class NonePermission implements Permission {
		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return true;
		}

		@Override
		public boolean displayIfHasNoPermission() {
			return false;
		}

		@Override
		public boolean runActionIfHasNoPermission() {
			return false;
		}
	}
	class PredicatePermission implements Permission {
		private final Predicate<Player> predicate;
		private final boolean displayIfHasNoPermission;
		private final boolean runActionIfHasNoPermission;

		public PredicatePermission(Predicate<Player> predicate, boolean displayIfHasNoPermission, boolean runActionIfHasNoPermission) {
			this.predicate = predicate;
			this.displayIfHasNoPermission = displayIfHasNoPermission;
			this.runActionIfHasNoPermission = runActionIfHasNoPermission;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return predicate.test(player);
		}

		@Override
		public boolean displayIfHasNoPermission() {
			return displayIfHasNoPermission;
		}

		@Override
		public boolean runActionIfHasNoPermission() {
			return runActionIfHasNoPermission;
		}
	}
	class BiPreicatePermission implements Permission {
		private final BiPredicate<InventoryGUI, Player> predicate;
		private final boolean displayIfHasNoPermission;
		private final boolean runActionIfHasNoPermission;

		public BiPreicatePermission(BiPredicate<InventoryGUI, Player> predicate, boolean displayIfHasNoPermission, boolean runActionIfHasNoPermission) {
			this.predicate = predicate;
			this.displayIfHasNoPermission = displayIfHasNoPermission;
			this.runActionIfHasNoPermission = runActionIfHasNoPermission;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return predicate.test(gui, player);
		}

		@Override
		public boolean displayIfHasNoPermission() {
			return displayIfHasNoPermission;
		}

		@Override
		public boolean runActionIfHasNoPermission() {
			return runActionIfHasNoPermission;
		}
	}
	class BukkitPermission implements Permission {
		private final String permission;
		private final boolean displayIfHasNoPermission;
		private final boolean runActionIfHasNoPermission;

		public BukkitPermission(String permission, boolean displayIfHasNoPermission, boolean runActionIfHasNoPermission) {
			this.permission = permission;
			this.displayIfHasNoPermission = displayIfHasNoPermission;
			this.runActionIfHasNoPermission = runActionIfHasNoPermission;
		}

		@Override
		public boolean hasPermission(Player player, InventoryGUI gui) {
			return player.hasPermission(permission);
		}

		@Override
		public boolean displayIfHasNoPermission() {
			return displayIfHasNoPermission;
		}

		@Override
		public boolean runActionIfHasNoPermission() {
			return runActionIfHasNoPermission;
		}
	}
}
