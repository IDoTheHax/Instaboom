package net.idothehax.instaboom.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.HashMap;
import java.util.Map;

public class BlockExplosionRegistry {
    private static final Map<Block, BlockExplosionData> EXPLOSION_DATA = new HashMap<>();

    public static class BlockExplosionData {
        public final float strength;
        public final float chainChance;

        public BlockExplosionData(float strength, float chainChance) {
            this.strength = strength;
            this.chainChance = chainChance;
        }
    }

    public static void initialize() {
        // Wood tier - low strength, very low chain chance
        registerBlock(Blocks.OAK_LOG, 1.5f, 0.05f);
        registerBlock(Blocks.BIRCH_LOG, 1.5f, 0.05f);
        registerBlock(Blocks.SPRUCE_LOG, 1.5f, 0.05f);
        registerBlock(Blocks.JUNGLE_LOG, 1.5f, 0.05f);
        registerBlock(Blocks.ACACIA_LOG, 1.5f, 0.05f);
        registerBlock(Blocks.DARK_OAK_LOG, 1.5f, 0.05f);

        // Stone tier - medium strength, low chain chance
        registerBlock(Blocks.STONE, 2.5f, 0.1f);
        registerBlock(Blocks.COBBLESTONE, 2.5f, 0.1f);
        registerBlock(Blocks.GRANITE, 2.5f, 0.1f);
        registerBlock(Blocks.DIORITE, 2.5f, 0.1f);
        registerBlock(Blocks.ANDESITE, 2.5f, 0.1f);

        // Ore tier - high strength, medium chain chance
        registerBlock(Blocks.IRON_ORE, 3.0f, 0.15f);
        registerBlock(Blocks.GOLD_ORE, 3.0f, 0.2f);
        registerBlock(Blocks.DIAMOND_ORE, 3.5f, 0.25f);
        registerBlock(Blocks.EMERALD_ORE, 3.5f, 0.25f);
        registerBlock(Blocks.REDSTONE_ORE, 3.0f, 0.15f);
        registerBlock(Blocks.LAPIS_ORE, 3.0f, 0.15f);

        // Special tier - very high strength, high chain chance
        registerBlock(Blocks.OBSIDIAN, 5.0f, 0.3f);
        registerBlock(Blocks.ANCIENT_DEBRIS, 5.0f, 0.35f);
        registerBlock(Blocks.NETHERITE_BLOCK, 6.0f, 0.4f);

        // Default values for unregistered blocks
        registerBlock(Blocks.AIR, 1.0f, 0.0f);
    }

    public static void registerBlock(Block block, float strength, float chainChance) {
        EXPLOSION_DATA.put(block, new BlockExplosionData(strength, chainChance));
    }

    public static BlockExplosionData getExplosionData(Block block) {
        return EXPLOSION_DATA.getOrDefault(block, new BlockExplosionData(1.0f, 0.0f));
    }
}