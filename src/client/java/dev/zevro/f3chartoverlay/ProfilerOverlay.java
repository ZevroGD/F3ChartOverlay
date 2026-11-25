package dev.zevro.f3chartoverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.DeltaTracker;

public final class ProfilerOverlay {
    private ProfilerOverlay() {}

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        OverlayConfig config = OverlayConfig.get();
        if (!config.showPieChart && !config.showList) return;

        Font font = client.font;
        int x = config.x;
        int y = config.y;
        int width = 190;
        int height = (config.showPieChart ? 94 : 0) + (config.showList ? 42 : 0) + 8;

        graphics.fill(x, y, x + width, y + height, 0xA9000000);
        graphics.fill(x, y, x + width, y + 1, 0xFF55C7FF);

        graphics.text(font, "F3 Chart Overlay", x + 6, y + 5, 0xFFFFFFFF);

        int contentY = y + 19;
        if (config.showPieChart) {
            graphics.text(font, "Pie chart", x + 6, contentY, 0xFF55C7FF);
            graphics.text(font, "Waiting for profiler data", x + 6, contentY + 13, 0xFFCCCCCC);
            contentY += 42;
        }

        if (config.showList) {
            graphics.text(font, "Profiler list", x + 6, contentY, 0xFF55C7FF);
            graphics.text(font, "Waiting for profiler data", x + 6, contentY + 13, 0xFFCCCCCC);
        }
    }
}
