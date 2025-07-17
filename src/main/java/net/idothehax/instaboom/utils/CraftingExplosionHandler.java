package net.idothehax.instaboom.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.HashMap;
import java.util.Map;

public class CraftingExplosionHandler {
    private static final Map<Item, ExplosionTier> ITEM_TIERS = new HashMap<>();

    public enum ExplosionTier {
        BASIC(0.1f, 1.0f),      // Common items
        UNCOMMON(0.2f, 2.0f),   // Iron, Gold
        RARE(0.3f, 3.0f),       // Diamond
        EPIC(0.4f, 4.0f);       // Netherite

        public final float chance;
        public final float strength;

        ExplosionTier(float chance, float strength) {
            this.chance = chance;
            this.strength = strength;
        }
    }

    static {
        // Initialize explosion tiers for different items

        // Basic items
        ITEM_TIERS.put(Items.WOODEN_SWORD, ExplosionTier.BASIC);
        ITEM_TIERS.put(Items.WOODEN_PICKAXE, ExplosionTier.BASIC);
        ITEM_TIERS.put(Items.STONE_SWORD, ExplosionTier.BASIC);
        ITEM_TIERS.put(Items.STONE_PICKAXE, ExplosionTier.BASIC);

        // Uncommon items
        ITEM_TIERS.put(Items.IRON_INGOT, ExplosionTier.UNCOMMON);
        ITEM_TIERS.put(Items.IRON_SWORD, ExplosionTier.UNCOMMON);
        ITEM_TIERS.put(Items.IRON_PICKAXE, ExplosionTier.UNCOMMON);
        ITEM_TIERS.put(Items.GOLD_INGOT, ExplosionTier.UNCOMMON);
        ITEM_TIERS.put(Items.GOLDEN_SWORD, ExplosionTier.UNCOMMON);

        // Rare items
        ITEM_TIERS.put(Items.DIAMOND, ExplosionTier.RARE);
        ITEM_TIERS.put(Items.DIAMOND_SWORD, ExplosionTier.RARE);
        ITEM_TIERS.put(Items.DIAMOND_PICKAXE, ExplosionTier.RARE);
        ITEM_TIERS.put(Items.ENCHANTING_TABLE, ExplosionTier.RARE);

        // Epic items
        ITEM_TIERS.put(Items.NETHERITE_INGOT, ExplosionTier.EPIC);
        ITEM_TIERS.put(Items.NETHERITE_SWORD, ExplosionTier.EPIC);
        ITEM_TIERS.put(Items.NETHERITE_PICKAXE, ExplosionTier.EPIC);
        ITEM_TIERS.put(Items.BEACON, ExplosionTier.EPIC);
    }

    public static ExplosionTier getItemTier(ItemStack stack) {
        return ITEM_TIERS.getOrDefault(stack.getItem(), ExplosionTier.BASIC);
    }

    public static float getExplosionChance(ItemStack stack) {
        ExplosionTier tier = getItemTier(stack);
        float baseChance = tier.chance;

        // Increase chance based on stack size
        if (stack.getCount() > 1) {
            baseChance += (stack.getCount() - 1) * 0.05f; // +5% per additional item
        }

        // Increase chance if item is enchanted
        if (stack.hasEnchantments()) {
            baseChance += 0.1f; // +10% for enchanted items
        }

        return Math.min(baseChance, 0.95f); // Cap at 95% chance
    }

    public static float getExplosionStrength(ItemStack stack) {
        ExplosionTier tier = getItemTier(stack);
        float baseStrength = tier.strength;

        // Increase strength based on stack size
        if (stack.getCount() > 1) {
            baseStrength += (stack.getCount() - 1) * 0.2f; // +0.2 per additional item
        }

        // Increase strength if item is enchanted
        if (stack.hasEnchantments()) {
            baseStrength += 1.0f; // +1.0 for enchanted items
        }

        return baseStrength;
    }
}