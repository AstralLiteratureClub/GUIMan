package bet.astral.guiman.api.sign.api.material;

import bet.astral.guiman.api.annotations.MinecraftVersion;
import org.bukkit.Material;

/**
 * Represents a hanging sign (small size) material
 */
@MinecraftVersion("1.20")
public class SmallSignMaterial implements ISignMaterial {
    /**
     * Represents oak hanging sign
     */
    public static SmallSignMaterial OAK;
    /**
     * Represents birch hanging sign
     */
    public static SmallSignMaterial BIRCH;
    /**
     * Represents spruce hanging sign
     */
    public static SmallSignMaterial SPRUCE;
    /**
     * Represents dark oak hanging sign
     */
    public static SmallSignMaterial DARK_OAK;
    /**
     * Represents jungle hanging sign
     */
    public static SmallSignMaterial JUNGLE;
    /**
     * Represents mangrove hanging sign
     */
    public static SmallSignMaterial MANGROVE;
    /**
     * Represents cherry hanging sign
     */
    public static SmallSignMaterial CHERRY;
    /**
     * Represents pale oak hanging sign
     */
    @MinecraftVersion("1.21.4")
    public static SmallSignMaterial PALE_OAK;
    /**
     * Represents bamboo hanging sign
     */
    public static SmallSignMaterial BAMBOO;
    /**
     * Represents crimson hanging sign
     */
    public static SmallSignMaterial CRIMSON;
    /**
     * Represents warped hanging sign
     */
    public static SmallSignMaterial WARPED;
    /**
     * Represents oak hanging sign
     */
    private final Material material;

    public SmallSignMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
