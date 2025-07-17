package net.idothehax.instaboom.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.idothehax.instaboom.Instaboom;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class MobHitListener {
    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient() && Instaboom.isExplosionsEnabledForEvent("mobHit")) {
                BlockPos pos = entity.getBlockPos();
                ExplosionUtils.kaboom((ServerWorld) world, pos, Instaboom.config.explosionStrength);
            }
            return ActionResult.PASS;
        });
    }
}