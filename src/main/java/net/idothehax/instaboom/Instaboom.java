package net.idothehax.instaboom;

import net.fabricmc.api.ModInitializer;

public class Instaboom implements ModInitializer {
    public static InstaboomConfig config;

    @Override
    public void onInitialize() {
        config = InstaboomConfig.loadConfig();
        // Pass config to listeners
    }
}
