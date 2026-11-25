package dev.zevro.f3chartoverlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OverlayConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("f3chartoverlay.json");

    public boolean showPieChart = true;
    public boolean showList = true;
    public int refreshRate = 10;
    // Distance from the right and bottom edges of the screen, in GUI-scaled pixels.
    public int x = 8;
    public int y = 8;
    public int scalePercent = 100;

    private static OverlayConfig INSTANCE = new OverlayConfig();

    private OverlayConfig() {}

    public static OverlayConfig get() {
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(FILE)) {
            try {
                OverlayConfig loaded = GSON.fromJson(Files.readString(FILE), OverlayConfig.class);
                if (loaded != null) INSTANCE = loaded;
            } catch (Exception exception) {
                System.err.println("[F3 Chart Overlay] Could not read config: " + exception.getMessage());
            }
        }
        INSTANCE.refreshRate = Math.max(1, Math.min(30, INSTANCE.refreshRate));
        INSTANCE.x = Math.max(0, Math.min(4096, INSTANCE.x));
        INSTANCE.y = Math.max(0, Math.min(4096, INSTANCE.y));
        if (INSTANCE.scalePercent <= 0) INSTANCE.scalePercent = 100;
        INSTANCE.scalePercent = Math.max(50, Math.min(200, INSTANCE.scalePercent));
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException exception) {
            System.err.println("[F3 Chart Overlay] Could not save config: " + exception.getMessage());
        }
    }

    public void setRefreshRate(int value) {
        refreshRate = Math.max(1, Math.min(30, value));
        save();
    }

    public void setPosition(int rightOffset, int bottomOffset) {
        x = Math.max(0, Math.min(4096, rightOffset));
        y = Math.max(0, Math.min(4096, bottomOffset));
        save();
    }

    public void setScalePercent(int value) {
        scalePercent = Math.max(50, Math.min(200, value));
        save();
    }
}
