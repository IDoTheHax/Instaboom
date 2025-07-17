package net.idothehax.instaboom.mixin;

import net.idothehax.instaboom.Instaboom;
import net.idothehax.instaboom.utils.ExplosionUtils;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityPickupMixin {
    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void instaboom_onPickup(PlayerEntity player, CallbackInfo ci) {
        // Add debug logging to track the grace period and stage
        if (!player.getWorld().isClient()) {
            boolean shouldExplode = Instaboom.isExplosionsEnabledForEvent("itemPickup");
            System.out.println("Item pickup - Grace Period Over: " + Instaboom.gracePeriodOver +
                    ", Current Stage: " + Instaboom.currentStage +
                    ", Should Explode: " + shouldExplode);

            if (shouldExplode) {
                BlockPos pos = player.getBlockPos();
                ExplosionUtils.kaboom((ServerWorld) player.getWorld(), pos, player);
            }
        }
    }
}