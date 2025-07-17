package net.idothehax.instaboom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.idothehax.instaboom.event.BlockBreakListener;
import net.idothehax.instaboom.event.MobHitListener;
import net.idothehax.instaboom.registry.BlockExplosionRegistry;
import net.idothehax.instaboom.utils.LandmineManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class Instaboom implements ModInitializer {
    public static InstaboomConfig config;
    public static boolean gracePeriodOver = false;
    public static int currentStage = 1;
    private static long tickCount = 0;
    private static final int GRACE_TICKS = 20 * 60;
    private static final int STAGE_INTERVAL_TICKS = 20 * 120;

    private static MinecraftServer serverInstance;

    @Override
    public void onInitialize() {
        config = InstaboomConfig.loadConfig();
        BlockBreakListener.register();
        MobHitListener.register();
        BlockExplosionRegistry.initialize();
        System.out.println("Instaboom mod initialized. 60 seconds grace period is active.");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCount++;
            if (tickCount == 1) {
                serverInstance = server;
            }
            if (!gracePeriodOver && tickCount % 20 == 0) {
                int secondsLeft = (int)((GRACE_TICKS - tickCount) / 20);
                broadcastTitle("Stability ending in: " + secondsLeft + " seconds");
                if (tickCount >= GRACE_TICKS) {
                    gracePeriodOver = true;
                    broadcastTitle("Stability over! Explosions begin!");
                    tickCount = 0;
                }
            } else if (gracePeriodOver && tickCount % STAGE_INTERVAL_TICKS == 0) {
                currentStage++;
                broadcastTitle("Escalation Mode: Stage " + currentStage + " activated! Brace yourself.");
            }
        });


        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                // Initialize the landmine manager for each world
                LandmineManager.setServerWorld(world);
                LandmineManager.getManager(world);
            }
        });
    }

    /**
     * Check if explosions are enabled for this kind of event based on current stage and grace period.
     *
     * @param eventType Should be one of: "blockBreak", "mobHit", "itemPickup", "explosiveDrop", "crafting"
     * @return true if this event should produce explosion based on the current stage
     */
    public static boolean isExplosionsEnabledForEvent(String eventType) {
        // During grace period, no explosions at all
        if (!gracePeriodOver) return false;

        return switch (currentStage) {
            // Stage 1: only block breaks are explosive
            case 1 -> eventType.equals("blockBreak");

            // Stage 2: block breaks, mob hits, item pickups and drops are explosive
            case 2 -> eventType.equals("blockBreak") ||
                    eventType.equals("mobHit") ||
                    eventType.equals("itemPickup") ||
                    eventType.equals("explosiveDrop");

            // Stage 3 and beyond: everything can explode
            default -> true;
        };
    }

    private void broadcastTitle(String titleText) {
        if (serverInstance != null) {
            for (ServerPlayerEntity player : serverInstance.getPlayerManager().getPlayerList()) {
                // Send title packet using the correct constructor
                TitleS2CPacket titlePacket = new TitleS2CPacket(Text.literal(titleText));
                player.networkHandler.sendPacket(titlePacket);
            }
        }
    }


}