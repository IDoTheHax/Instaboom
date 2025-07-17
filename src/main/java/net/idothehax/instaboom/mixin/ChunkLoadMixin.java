package net.idothehax.instaboom.mixin;

import net.idothehax.instaboom.utils.LandmineManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public class ChunkLoadMixin {
    @Inject(method = "setLoadedToWorld", at = @At("TAIL"))
    private void onChunkLoad(CallbackInfo ci) {
        WorldChunk chunk = (WorldChunk) (Object) this;
        if (!(chunk.getWorld() instanceof ServerWorld)) return;

        ServerWorld world = (ServerWorld) chunk.getWorld();
        LandmineManager manager = LandmineManager.getManager(world);

        ChunkSection[] sections = chunk.getSectionArray();
        for (int sectionY = 0; sectionY < sections.length; sectionY++) {
            ChunkSection section = sections[sectionY];
            if (section != null && !section.isEmpty()) {
                int yStart = sectionY * 16;
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockPos pos = chunk.getPos().getStartPos().add(
                                    x,
                                    yStart + y,
                                    z
                            );
                            manager.tryConvertToLandmine(world, pos, section.getBlockState(x, y, z));
                        }
                    }
                }
            }
        }
    }
}