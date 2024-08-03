package bet.astral.guiman;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Deprecated(forRemoval = true)
public final class GUI extends InventoryGUI {
	public GUI(@Nullable Component name, @NotNull InventoryType type, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, @Nullable Consumer<@NotNull Player> closeConsumer, @Nullable Consumer<@NotNull Player> openConsumer, boolean regenerateItems) {
		super(name, type, background, clickable, closeConsumer, openConsumer, regenerateItems);
	}

	public GUI(@Nullable Component name, int rows, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, @Nullable Consumer<@NotNull Player> closeConsumer, @Nullable Consumer<@NotNull Player> openConsumer, boolean regenerateItems) {
		super(name, rows, background, clickable, closeConsumer, openConsumer, regenerateItems);
	}

	public GUI(@Nullable Component name, @NotNull InventoryType type, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, boolean regenerateItems) {
		super(name, type, background, clickable, regenerateItems);
	}

	public GUI(@Nullable Component name, int rows, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, boolean regenerateItems) {
		super(name, rows, background, clickable, regenerateItems);
	}
}
