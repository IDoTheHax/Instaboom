package net.idothehax.instaboom.utils;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import java.util.Random;

public class ExplosionEffects {
    private static final Random RANDOM = new Random();

    public static void applyRandomEffect(ServerWorld world, BlockPos pos) {
        // Get all players within 10 blocks
        Box box = new Box(pos).expand(10.0);
        world.getPlayers().forEach(player -> {
            if (player.getBoundingBox().intersects(box)) {
                applyRandomStatusEffect(player);
            }
        });
    }

    private static void applyRandomStatusEffect(PlayerEntity player) {
        switch (RANDOM.nextInt(10)) {
            case 0 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1));
            case 1 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 2));
            case 2 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 2));
            case 3 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
            case 4 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
            case 5 -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 0));
            }
            case 6 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 200, 0));
            case 7 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
            case 8 -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0));
            case 9 -> {
                // Multi-effect combo
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0));
            }
        }
    }
}