package bet.astral.guiman.core.sign;

import bet.astral.guiman.core.GUIManInitializer;
import bet.astral.guiman.api.sign.api.material.SignMaterial;
import bet.astral.guiman.api.sign.api.material.SmallSignMaterial;
import org.bukkit.Material;

public class SignMaterialReg {
	public static void init(){
		SignMaterial.OAK = new SignMaterial(Material.OAK_SIGN);

		if (GUIManInitializer.isOrNever("1.14")) {
			SignMaterial.BIRCH = new SignMaterial(Material.BIRCH_SIGN);
			SignMaterial.SPRUCE = new SignMaterial(Material.SPRUCE_SIGN);
			SignMaterial.DARK_OAK = new SignMaterial(Material.DARK_OAK_SIGN);
			SignMaterial.JUNGLE = new SignMaterial(Material.JUNGLE_SIGN);
		} else {
			SignMaterial.BIRCH = SignMaterial.OAK;
			SignMaterial.SPRUCE = SignMaterial.OAK;
			SignMaterial.DARK_OAK = SignMaterial.OAK;
			SignMaterial.JUNGLE = SignMaterial.OAK;
		}
		if (GUIManInitializer.isOrNever("1.16")) {
			SignMaterial.CRIMSON = new SignMaterial(Material.CRIMSON_SIGN);
			SignMaterial.WARPED = new SignMaterial(Material.WARPED_SIGN);
		} else {
			SignMaterial.CRIMSON = SignMaterial.OAK;
			SignMaterial.WARPED = SignMaterial.OAK;
		}
		if (GUIManInitializer.isOrNever("1.19")) {
			SignMaterial.MANGROVE = new SignMaterial(Material.MANGROVE_SIGN);
		} else {
			SignMaterial.MANGROVE = SignMaterial.OAK;
		}
		if (GUIManInitializer.isOrNever("1.20")) {
			SignMaterial.CHERRY = new SignMaterial(Material.CHERRY_SIGN);
			SignMaterial.BAMBOO = new SignMaterial(Material.BAMBOO_SIGN);
		} else {
			SignMaterial.CHERRY = SignMaterial.OAK;
			SignMaterial.BAMBOO = SignMaterial.OAK;
		}
		if (GUIManInitializer.isOrNever("1.21.4")) {
			SignMaterial.PALE_OAK = new SignMaterial(Material.PALE_OAK_SIGN);
		}

		if (GUIManInitializer.isOrNever("1.20")){
			SmallSignMaterial.OAK = new SmallSignMaterial(Material.OAK_HANGING_SIGN);
			SmallSignMaterial.BIRCH = new SmallSignMaterial(Material.BIRCH_HANGING_SIGN);
			SmallSignMaterial.SPRUCE = new SmallSignMaterial(Material.SPRUCE_HANGING_SIGN);
			SmallSignMaterial.DARK_OAK = new SmallSignMaterial(Material.DARK_OAK_HANGING_SIGN);
			SmallSignMaterial.JUNGLE = new SmallSignMaterial(Material.JUNGLE_HANGING_SIGN);
			SmallSignMaterial.MANGROVE = new SmallSignMaterial(Material.MANGROVE_HANGING_SIGN);
			SmallSignMaterial.CHERRY = new SmallSignMaterial(Material.CHERRY_HANGING_SIGN);
			SmallSignMaterial.PALE_OAK = new SmallSignMaterial(Material.PALE_OAK_HANGING_SIGN);
			SmallSignMaterial.BAMBOO = new SmallSignMaterial(Material.BAMBOO_HANGING_SIGN);
			SmallSignMaterial.CRIMSON = new SmallSignMaterial(Material.CRIMSON_HANGING_SIGN);
			SmallSignMaterial.WARPED = new SmallSignMaterial(Material.WARPED_HANGING_SIGN);
		}
		else {
			SmallSignMaterial.OAK = new SmallSignMaterial(Material.OAK_SIGN);
			SmallSignMaterial.BIRCH = new SmallSignMaterial(Material.BIRCH_SIGN);
			SmallSignMaterial.SPRUCE = new SmallSignMaterial(Material.SPRUCE_SIGN);
			SmallSignMaterial.DARK_OAK = new SmallSignMaterial(Material.DARK_OAK_SIGN);
			SmallSignMaterial.JUNGLE = new SmallSignMaterial(Material.JUNGLE_SIGN);
			SmallSignMaterial.MANGROVE = new SmallSignMaterial(Material.MANGROVE_SIGN);
			SmallSignMaterial.CHERRY = new SmallSignMaterial(Material.CHERRY_SIGN);
			SmallSignMaterial.BAMBOO = new SmallSignMaterial(Material.BAMBOO_SIGN);
			SmallSignMaterial.CRIMSON = new SmallSignMaterial(Material.CRIMSON_SIGN);
			SmallSignMaterial.WARPED = new SmallSignMaterial(Material.WARPED_SIGN);
		}

		if (GUIManInitializer.isOrNever("1.21.4")) {
			SmallSignMaterial.PALE_OAK =  SmallSignMaterial.OAK;
		} else {
			SmallSignMaterial.PALE_OAK =  new SmallSignMaterial(Material.OAK_HANGING_SIGN);
		}
	}
}
