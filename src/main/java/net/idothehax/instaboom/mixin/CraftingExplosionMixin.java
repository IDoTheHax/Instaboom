package net.idothehax.instaboom.mixin;

import net.idothehax.instaboom.Instaboom;
import net.idothehax.instaboom.utils.CraftingExplosionHandler;
import net.idothehax.instaboom.utils.ExplosionUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingExplosionMixin extends ScreenHandler {

    // Constructor required for extending ScreenHandler
    protected CraftingExplosionMixin() {
        super(null, -1);
    }

    @Inject(method = "updateResult", at = @At("TAIL"))
    private static void onCraftingComplete(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci) {
        // Use the player parameter directly - it's already provided by the method
        PlayerEntity craftingPlayer = player;

        if (craftingPlayer != null && !craftingPlayer.getWorld().isClient && Instaboom.isExplosionsEnabledForEvent("crafting")) {
            ItemStack result = resultInventory.getStack(0); // Get result from result inventory, not crafting inventory
            if (!result.isEmpty()) {
                float explosionChance = CraftingExplosionHandler.getExplosionChance(result);

                if (craftingPlayer.getRandom().nextFloat() < explosionChance) {
                    float strength = CraftingExplosionHandler.getExplosionStrength(result);

                    // Warn player about dangerous crafting
                    craftingPlayer.sendMessage(Text.literal("§c⚠ Unstable crafting detected! (" +
                            String.format("%.0f", explosionChance * 100) + "% risk)"), true);

                    // Create explosion with calculated strength
                    ExplosionUtils.kaboom(
                            (ServerWorld) craftingPlayer.getWorld(),
                            craftingPlayer.getBlockPos(),
                            craftingPlayer
                    );
                }
            }
        }
    }
}