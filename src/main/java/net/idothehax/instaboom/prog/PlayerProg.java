package net.idothehax.instaboom.prog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProg {
    private static final Map<UUID, PlayerStats> PLAYER_STATS = new HashMap<>();

    public static class PlayerStats {
        private int explosionsTriggered = 0;
        private int explosionResistanceLevel = 0;
        private float damageReduction = 0.0f;

        public void incrementExplosions() {
            explosionsTriggered++;
            updateProgression();
        }

        private void updateProgression() {
            // Every 50 explosions, gain a level of resistance
            explosionResistanceLevel = explosionsTriggered / 50;
            // Each level reduces damage by 10% (max 90%)
            damageReduction = Math.min(0.9f, explosionResistanceLevel * 0.1f);
        }

        public float getDamageReduction() {
            return damageReduction;
        }

        public int getLevel() {
            return explosionResistanceLevel;
        }

        // Save/load progression
        public NbtCompound saveToNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("explosions", explosionsTriggered);
            nbt.putInt("level", explosionResistanceLevel);
            nbt.putFloat("reduction", damageReduction);
            return nbt;
        }

        public void loadFromNbt(NbtCompound nbt) {
            explosionsTriggered = nbt.getInt("explosions");
            explosionResistanceLevel = nbt.getInt("level");
            damageReduction = nbt.getFloat("reduction");
        }
    }

    public static PlayerStats getStats(PlayerEntity player) {
        return PLAYER_STATS.computeIfAbsent(player.getUuid(), k -> new PlayerStats());
    }

    public static void onExplosionTriggered(PlayerEntity player) {
        getStats(player).incrementExplosions();
        if (player instanceof ServerPlayerEntity) {
            updatePlayerAbilities((ServerPlayerEntity) player);
        }
    }

    private static void updatePlayerAbilities(ServerPlayerEntity player) {
        PlayerStats stats = getStats(player);
        if (stats.getLevel() > 0) {
            player.sendMessage(Text.literal("§6Explosion Resistance Level " + stats.getLevel() +
                    " (Damage reduced by " + (int)(stats.getDamageReduction() * 100) + "%)"), true);
        }
    }
}
