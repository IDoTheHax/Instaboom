package net.idothehax.instaboom.mixin;

import net.idothehax.instaboom.utils.LandmineManager;
import net.idothehax.instaboom.utils.ExplosionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class LandmineMixin {
    @Inject(method = "move", at = @At("HEAD"))
    private void onMove(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof PlayerEntity && !entity.getWorld().isClient) {
            BlockPos pos = entity.getBlockPos().down(); // Block being stood on
            LandmineManager manager = LandmineManager.getInstance();

            if (manager.isLandmine(pos)) {
                // BOOM!
                manager.removeLandmine(pos);
                ExplosionUtils.kaboom(
                        (ServerWorld) entity.getWorld(),
                        pos,
                        (PlayerEntity) entity
                );
            }
        }
    }
}