package net.idothehax.instaboom;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;

public class InstaboomConfig {
    public boolean blockBreakExplosion = true;
    public boolean mobHitExplosion = true;
    public boolean itemPickupExplosion = true;
    public float explosiveDropChance = 0.2f;
    public float explosionStrength = 4.0f;

    public static InstaboomConfig loadConfig() {
        Path configPath = Paths.get("config", "instaboom-config.json");
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                return new Gson().fromJson(reader, InstaboomConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new InstaboomConfig();
    }
}
