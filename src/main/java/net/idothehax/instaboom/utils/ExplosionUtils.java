package net.idothehax.instaboom.utils;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.idothehax.instaboom.prog.PlayerProg;
import net.idothehax.instaboom.registry.BlockExplosionRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExplosionUtils {
    public static void kaboom(ServerWorld world, BlockPos pos, PlayerEntity player) {
        BlockState blockState = world.getBlockState(pos);
        float baseStrength = BlockExplosionRegistry.getExplosionStrength(blockState.getBlock());

        // Record the explosion for player progression
        PlayerProg.onExplosionTriggered(player);

        // Get player's current damage reduction
        float damageReduction = PlayerProg.getStats(player).getDamageReduction();

        // Apply damage reduction to explosion strength
        float finalStrength = baseStrength * (1.0f - damageReduction);

        boolean mobGriefing = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
        world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, finalStrength, mobGriefing ? ServerWorld.ExplosionSourceType.BLOCK : ServerWorld.ExplosionSourceType.NONE);
    }
}
