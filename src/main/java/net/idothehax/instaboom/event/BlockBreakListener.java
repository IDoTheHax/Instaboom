package net.idothehax.instaboom.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.idothehax.instaboom.Instaboom;
import net.idothehax.instaboom.utils.ExplosionUtils;
import net.minecraft.server.world.ServerWorld;

public class BlockBreakListener {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient() && Instaboom.isExplosionsEnabledForEvent("blockBreak")) {
                ExplosionUtils.kaboom((ServerWorld) world, pos, player);
            }
        });
    }
}