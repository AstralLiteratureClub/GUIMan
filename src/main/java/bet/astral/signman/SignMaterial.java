package bet.astral.signman;

import org.bukkit.Material;

public enum SignMaterial {
	OAK(Material.OAK_SIGN, Material.OAK_HANGING_SIGN),
	BIRCH(Material.BIRCH_SIGN, Material.BIRCH_HANGING_SIGN),
	SPRUCE(Material.SPRUCE_SIGN, Material.SPRUCE_HANGING_SIGN),
	DARK_OAK(Material.DARK_OAK_SIGN, Material.DARK_OAK_HANGING_SIGN),
	JUNGLE(Material.JUNGLE_SIGN, Material.JUNGLE_HANGING_SIGN),
	MANGROVE(Material.MANGROVE_SIGN, Material.MANGROVE_HANGING_SIGN),
	CHERRY(Material.CHERRY_SIGN, Material.CHERRY_HANGING_SIGN),
	PALE_OAK(Material.PALE_OAK_SIGN, Material.PALE_OAK_HANGING_SIGN),
	BAMBOO(Material.BAMBOO_SIGN, Material.BAMBOO_HANGING_SIGN),
	CRIMSON(Material.CRIMSON_SIGN, Material.CRIMSON_HANGING_SIGN),
	WARPED(Material.WARPED_SIGN, Material.WARPED_HANGING_SIGN)
	;

	private final Material normalMaterial;
	private final Material hangingMaterial;

	SignMaterial(Material normalMaterial, Material hangingMaterial) {
		this.normalMaterial = normalMaterial;
		this.hangingMaterial = hangingMaterial;
	}

	public Material getMaterial(SignSize signSize){
		return signSize==SignSize.SMALL ? hangingMaterial : normalMaterial;
	}
}
