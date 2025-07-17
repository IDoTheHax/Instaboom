package net.idothehax.instaboom.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class LandmineManager extends PersistentState {
    private static final String SAVE_KEY = "instaboom_landmines";
    private final Set<BlockPos> landmines = new HashSet<>();
    private static final Random RANDOM = new Random();
    private static final double NATURAL_MINE_CHANCE = 0.05; // 5% chance
    private static ServerWorld serverWorld;

    public LandmineManager() {
        super();
    }

    public static void setServerWorld(ServerWorld world) {
        serverWorld = world;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList landmineList = new NbtList();

        for (BlockPos pos : landmines) {
            NbtCompound posNbt = new NbtCompound();
            posNbt.putInt("X", pos.getX());
            posNbt.putInt("Y", pos.getY());
            posNbt.putInt("Z", pos.getZ());
            landmineList.add(posNbt);
        }

        nbt.put("Landmines", landmineList);
        return nbt;
    }

    public static LandmineManager getManager(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new IllegalStateException("Cannot get LandmineManager from client world");
        }

        ServerWorld serverWorld = (ServerWorld) world;
        setServerWorld(serverWorld);

        return serverWorld.getPersistentStateManager()
                .getOrCreate(
                        TYPE,
                        SAVE_KEY
                );
    }

    public static LandmineManager createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        LandmineManager manager = new LandmineManager();
        NbtList landmineList = nbt.getList("Landmines", 10); // 10 is the NBT type for compounds :devious:

        for (int i = 0; i < landmineList.size(); i++) {
            NbtCompound pos = landmineList.getCompound(i);
            manager.landmines.add(new BlockPos(
                    pos.getInt("X"),
                    pos.getInt("Y"),
                    pos.getInt("Z")
            ));
        }

        return manager;
    }

    public void addLandmine(BlockPos pos) {
        landmines.add(pos.toImmutable());
        markDirty();
    }

    public boolean isLandmine(BlockPos pos) {
        return landmines.contains(pos);
    }

    public void removeLandmine(BlockPos pos) {
        landmines.remove(pos);
        markDirty();
    }

    public void tryConvertToLandmine(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.getBlock() == Blocks.GRASS_BLOCK && RANDOM.nextDouble() < NATURAL_MINE_CHANCE) {
            addLandmine(pos);
            markDirty();
        }
    }

    // Helper method to get singleton instance
    public static LandmineManager getInstance() {
        if (serverWorld == null) {
            throw new IllegalStateException("ServerWorld not initialized");
        }
        return getManager(serverWorld);
    }

    // Type definition for persistent state system
    public static final PersistentState.Type<LandmineManager> TYPE = new PersistentState.Type<>(
            LandmineManager::new,
            LandmineManager::createFromNbt,
            null
    );
}