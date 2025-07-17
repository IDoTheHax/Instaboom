package net.idothehax.instaboom.utils;

import net.idothehax.instaboom.Instaboom;
import net.idothehax.instaboom.registry.BlockExplosionRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class ExplosionUtils {
    private static final Random RANDOM = new Random();
    private static final int MAX_CHAIN_LENGTH = 3;

    public static void kaboom(ServerWorld world, BlockPos pos, PlayerEntity player) {
        kaboomWithChain(world, pos, player, 0);
    }

    private static void kaboomWithChain(ServerWorld world, BlockPos pos, PlayerEntity player, int chainDepth) {
        BlockState blockState = world.getBlockState(pos);
        BlockExplosionRegistry.BlockExplosionData explosionData =
                BlockExplosionRegistry.getExplosionData(blockState.getBlock());

        // Create explosion using the correct method signature
        world.createExplosion(
                null, // Entity causing explosion
                pos.getX() + 0.5, // X coordinate
                pos.getY() + 0.5, // Y coordinate
                pos.getZ() + 0.5, // Z coordinate
                explosionData.strength,
                World.ExplosionSourceType.BLOCK // Use appropriate explosion source type
        );

        // Chain reaction based on block value
        if (chainDepth < MAX_CHAIN_LENGTH && RANDOM.nextFloat() < explosionData.chainChance) {
            // Calculate new position for chain explosion
            BlockPos chainPos = pos.add(
                    RANDOM.nextInt(7) - 3,
                    RANDOM.nextInt(5) - 2,
                    RANDOM.nextInt(7) - 3
            );

            // Schedule next chain explosion using Fabric's ServerTickEvents
            int delay = RANDOM.nextInt(5) + 1; // 1-5 tick delay
            scheduleChainExplosion(world, chainPos, player, chainDepth + 1, delay);
        }
    }


    private static void scheduleChainExplosion(ServerWorld world, BlockPos pos, PlayerEntity player, int depth, int delay) {
        int[] ticksLeft = {delay};
        boolean[] completed = {false};

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (completed[0]) return; // Skip if already completed

            if (ticksLeft[0] <= 0) {
                kaboomWithChain(world, pos, player, depth);
                completed[0] = true; // Mark as completed
                return;
            }
            ticksLeft[0]--;
        });
    }
}