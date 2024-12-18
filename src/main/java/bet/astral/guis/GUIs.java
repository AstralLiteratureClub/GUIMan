package bet.astral.guis;

import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.signman.SignGUIBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.event.inventory.InventoryType;

public class GUIs {
    public static InventoryGUIBuilder inventoryGUI(ChestRows rows){
        return InventoryGUI.builder(rows);
    }
    public static InventoryGUIBuilder inventoryGUI(InventoryType type){
        return InventoryGUI.builder(type);
    }

    public static SignGUIBuilder signGUI(){
        return new SignGUIBuilder();
    }
    public static SignGUIBuilder signGUI(ComponentSerializer<? extends Component, ? extends Component, String> serializer){
        return new SignGUIBuilder(serializer);
    }
}
