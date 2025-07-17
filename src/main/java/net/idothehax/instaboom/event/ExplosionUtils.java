package net.idothehax.instaboom.event;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class ExplosionUtils {
    public static void kaboom(ServerWorld world, BlockPos pos, float strength) {
        boolean mobGriefing = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
        world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, strength, mobGriefing ? ServerWorld.ExplosionSourceType.MOB : ServerWorld.ExplosionSourceType.NONE);
    }
}
