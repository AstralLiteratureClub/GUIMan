package bet.astral.guiman.api.sign.api.material;

import bet.astral.guiman.api.annotations.MinecraftVersion;
import org.bukkit.Material;

/**
 * Represents default or legacy sign size material
 */
public class SignMaterial implements ISignMaterial {
    /**
     * Represents oak sign
     */
    public static SignMaterial OAK;
    /**
     * Represents birch sign
     */
    @MinecraftVersion("1.14")
    public static SignMaterial BIRCH;
    /**
     * Represents spruce sign
     */
    @MinecraftVersion("1.14")
    public static SignMaterial SPRUCE;
    /**
     * Represents dark oak sign
     */
    @MinecraftVersion("1.14")
    public static SignMaterial DARK_OAK;
    /**
     * Represents jungle sign
     */
    @MinecraftVersion("1.14")
    public static SignMaterial JUNGLE;
    /**
     * Represents mangrove sign
     */
    @MinecraftVersion("1.19")
    public static SignMaterial MANGROVE;
    /**
     * Represents cherry sign
     */
    @MinecraftVersion("1.20")
    public static SignMaterial CHERRY;
    /**
     * Represents pale oak sign
     */
    @MinecraftVersion("1.21.4")
    public static SignMaterial PALE_OAK;
    /**
     * Represents bamboo sign
     */
    @MinecraftVersion("1.20")
    public static SignMaterial BAMBOO;
    /**
     * Represents crimson sign
     */
    @MinecraftVersion("1.16")
    public static SignMaterial CRIMSON;
    /**
     * Represents warped sign
     */
    @MinecraftVersion("1.16")
    public static SignMaterial WARPED;
    private final Material material;

    /**
     * Creates a new sign material instance
     * @param material material
     */
    public SignMaterial(Material material) {
        this.material = material;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Material getMaterial() {
        return material;
    }
}
