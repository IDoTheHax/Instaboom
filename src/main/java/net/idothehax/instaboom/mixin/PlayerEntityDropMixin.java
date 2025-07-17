package net.idothehax.instaboom.mixin;

import net.idothehax.instaboom.Instaboom;
import net.idothehax.instaboom.event.ExplosionUtils;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(PlayerEntity.class)
public class PlayerEntityDropMixin {
    private static final Random RANDOM = new Random();

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
    private void instaboom_onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        ItemEntity dropped = cir.getReturnValue();
        if (dropped != null && !dropped.getWorld().isClient() && Instaboom.isExplosionsEnabledForEvent("explosiveDrop")) {
            if (RANDOM.nextFloat() < Instaboom.config.explosiveDropChance) {
                BlockPos pos = dropped.getBlockPos();
                ExplosionUtils.kaboom((ServerWorld) dropped.getWorld(), pos, Instaboom.config.explosionStrength);
            }
        }
    }
}