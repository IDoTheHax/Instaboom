package net.idothehax.instaboom.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

import java.util.HashMap;
import java.util.Map;

public class BlockExplosionRegistry {
    private static final Map<Block, Float> EXPLOSION_STRENGTHS = new HashMap<>();

    // Default strengths for different block categories
    private static final float WOOD_STRENGTH = 1.5f;
    private static final float STONE_STRENGTH = 2.5f;
    private static final float ORE_STRENGTH = 3.0f;
    private static final float OBSIDIAN_STRENGTH = 5.0f;
    private static final float DEFAULT_STRENGTH = 1.0f;

    public static void initialize() {
        // Wood-based blocks (smaller explosions)
        registerBlock(Blocks.OAK_LOG, WOOD_STRENGTH);
        registerBlock(Blocks.BIRCH_LOG, WOOD_STRENGTH);
        registerBlock(Blocks.SPRUCE_LOG, WOOD_STRENGTH);
        registerBlock(Blocks.JUNGLE_LOG, WOOD_STRENGTH);
        registerBlock(Blocks.ACACIA_LOG, WOOD_STRENGTH);
        registerBlock(Blocks.DARK_OAK_LOG, WOOD_STRENGTH);

        // Stone-based blocks (medium explosions)
        registerBlock(Blocks.STONE, STONE_STRENGTH);
        registerBlock(Blocks.COBBLESTONE, STONE_STRENGTH);
        registerBlock(Blocks.GRANITE, STONE_STRENGTH);
        registerBlock(Blocks.DIORITE, STONE_STRENGTH);
        registerBlock(Blocks.ANDESITE, STONE_STRENGTH);

        // Ores (larger explosions)
        registerBlock(Blocks.IRON_ORE, ORE_STRENGTH);
        registerBlock(Blocks.GOLD_ORE, ORE_STRENGTH);
        registerBlock(Blocks.DIAMOND_ORE, ORE_STRENGTH);
        registerBlock(Blocks.EMERALD_ORE, ORE_STRENGTH);
        registerBlock(Blocks.REDSTONE_ORE, ORE_STRENGTH);
        registerBlock(Blocks.LAPIS_ORE, ORE_STRENGTH);

        // Special blocks (very large explosions)
        registerBlock(Blocks.OBSIDIAN, OBSIDIAN_STRENGTH);
        registerBlock(Blocks.ANCIENT_DEBRIS, OBSIDIAN_STRENGTH);
    }

    public static void registerBlock(Block block, float strength) {
        EXPLOSION_STRENGTHS.put(block, strength);
    }

    public static float getExplosionStrength(Block block) {
        return EXPLOSION_STRENGTHS.getOrDefault(block, DEFAULT_STRENGTH);
    }
}
