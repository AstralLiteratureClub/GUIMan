package bet.astral.guiman;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryGUIBuilder {
	private @Nullable Component name;
	private @NotNull InventoryType type;
	private @Nullable Clickable background;
	private @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable = new HashMap<>();
	private @Nullable Consumer<@NotNull Player> closeConsumer;
	private @Nullable Consumer<@NotNull Player> openConsumer;
	private boolean regenerateItems = false;
	private int rows;

	public InventoryGUIBuilder(int rows){
		this.type = InventoryType.CHEST;
		this.rows = rows;
	}
	public InventoryGUIBuilder(InventoryType type){
		this.type = type;
		this.rows = type.getDefaultSize();
	}

	public InventoryGUIBuilder name(@Nullable Component name) {
		this.name = name;
		return this;
	}

	public InventoryGUIBuilder type(@NotNull InventoryType type) {
		this.type = type;
		this.rows = 0;
		return this;
	}
	public InventoryGUIBuilder chestType(int rows) {
		this.type = InventoryType.CHEST;
		this.rows = rows;
		return this;
	}

	public InventoryGUIBuilder setBackground(@Nullable Clickable background) {
		this.background = background;
		return this;
	}
	public InventoryGUIBuilder setBackground(@Nullable ClickableBuilder background) {
		if (background == null){
			this.background = null;
			return this;
		}
		this.background = background.build();
		return this;
	}
	public InventoryGUIBuilder setSlotClickable(int slot, @NotNull List<@NotNull Clickable> clickable){
		this.clickable.put(slot, clickable);
		return this;
	}
	public InventoryGUIBuilder setSlotClickableBuilders(int slot, @NotNull List<@NotNull ClickableBuilder> clickable){
		List<Clickable> clickables = new ArrayList<>();
		for (ClickableBuilder b : clickable){
			clickables.add(b.createClickable());
		}
		this.clickable.put(slot, clickables);
		return this;
	}
	public InventoryGUIBuilder setSlotClickable(int slot, Clickable clickable){
		this.clickable.put(slot, List.of(clickable));
		return this;
	}
	public InventoryGUIBuilder setSlotClickable(int slot, ClickableBuilder clickable){
		this.clickable.put(slot, List.of(clickable.build()));
		return this;
	}
	public InventoryGUIBuilder addSlotClickable(int slot, Clickable clickable){
		List<Clickable> clickables = this.clickable.get(slot) != null ? new ArrayList<>(this.clickable.get(slot)) : new ArrayList<>();
		clickables.add(clickable);
		this.clickable.put(slot, clickables);
		return this;
	}
	public InventoryGUIBuilder addSlotClickable(int slot, ClickableBuilder clickable){
		List<Clickable> clickables = this.clickable.get(slot) != null ? new ArrayList<>(this.clickable.get(slot)) : new ArrayList<>();
		clickables.add(clickable.build());
		this.clickable.put(slot, clickables);
		return this;
	}


	public InventoryGUIBuilder addSlotEmpty(int slot, Material material){
		List<Clickable> clickables = this.clickable.get(slot) != null ? new ArrayList<>(this.clickable.get(slot)) : new ArrayList<>();
		clickables.add(Clickable.empty(new ItemStack(material)));
		this.clickable.put(slot, clickables);
		return this;
	}
	public InventoryGUIBuilder addSlotEmpty(int slot, ItemStack itemstack){
		List<Clickable> clickables = this.clickable.get(slot) != null ? new ArrayList<>(this.clickable.get(slot)) : new ArrayList<>();
		clickables.add(Clickable.empty(itemstack));
		this.clickable.put(slot, clickables);
		return this;
	}

	public InventoryGUIBuilder setClickable(@NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable) {
		this.clickable = clickable;
		return this;
	}

	public InventoryGUIBuilder setCloseConsumer(@NotNull Consumer<@NotNull Player> closeConsumer) {
		this.closeConsumer = closeConsumer;
		return this;
	}

	public InventoryGUIBuilder setOpenConsumer(@NotNull Consumer<@NotNull Player> openConsumer) {
		this.openConsumer = openConsumer;
		return this;
	}

	public InventoryGUIBuilder replaceItemsEachOpen(boolean regenerate) {
		this.regenerateItems = regenerate;
		return this;
	}


	@Deprecated(forRemoval = true)
	public GUI createGUI() {
		if (this.type == InventoryType.CHEST){
			return new GUI(name, rows, background, clickable, closeConsumer, openConsumer, regenerateItems);
		} else {
			return new GUI(name, type, background, clickable, closeConsumer, openConsumer, regenerateItems);
		}
	}

	public InventoryGUI build(){
		return createGUI();
	}
}