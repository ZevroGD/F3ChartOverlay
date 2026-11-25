package dev.zevro.f3chartoverlay;

import dev.zevro.f3chartoverlay.mixin.ProfilerPieChartAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProfilerOverlay {
    private static final DecimalFormat PERCENT = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
    private static ProfileResults cachedResults;
    private static long lastRefreshNanos;

    private ProfilerOverlay() {}

    public static void render(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        OverlayConfig config = OverlayConfig.get();
        if (!config.showPieChart && !config.showList) return;

        ProfilerPieChart vanillaChart = client.getDebugOverlay().getProfilerPieChart();
        ProfileResults latest = ((ProfilerPieChartAccessor) vanillaChart).f3chart$getResults();
        long now = System.nanoTime();
        long refreshInterval = 1_000_000_000L / Math.max(1, Math.min(30, config.refreshRate));

        if (latest == null) {
            cachedResults = null;
            lastRefreshNanos = 0L;
            return;
        }
        if (cachedResults == null || now - lastRefreshNanos >= refreshInterval) {
            cachedResults = latest;
            lastRefreshNanos = now;
        }

        List<ResultField> entries = new ArrayList<>(cachedResults.getTimes(
            ((ProfilerPieChartAccessor) vanillaChart).f3chart$getTreePath()
        ));
        if (entries.isEmpty()) return;

        ResultField root = entries.removeFirst();
        Font font = client.font;
        int right = graphics.guiWidth() - 8;
        int left = right - 260;
        int bottom = graphics.guiHeight() - 8;
        int visibleEntries = Math.min(entries.size(), 14);
        int listHeight = config.showList ? visibleEntries * 10 + 18 : 0;

        if (config.showPieChart) {
            int chartBottom = config.showList ? bottom - listHeight : bottom;
            int chartCenterX = right - 130;
            int chartCenterY = chartBottom - 5 - 62;
            int chartLeft = chartCenterX - 130;
            int chartRight = chartCenterX + 130;
            int chartTop = chartCenterY - 52;
            graphics.fill(chartLeft - 5, chartTop - 5, chartRight + 5, chartBottom + 5, 0xA9000000);
            graphics.profilerChart(entries, chartLeft, chartTop, chartRight, chartCenterY + 62);
            graphics.text(font, ProfileResults.demanglePath(root.name) + "  " + PERCENT.format(root.globalPercentage) + "%",
                chartLeft, chartTop - 12, 0xFFFFFFFF);
        }

        if (config.showList) {
            int panelTop = bottom - listHeight;
            graphics.fill(left - 5, panelTop - 5, right + 5, bottom + 5, 0xA9000000);
            graphics.text(font, ProfileResults.demanglePath(root.name) + "  " + PERCENT.format(root.globalPercentage) + "%",
                left, panelTop, 0xFFFFFFFF);
            for (int i = 0; i < visibleEntries; i++) {
                ResultField entry = entries.get(i);
                String name = (i + 1) + ". " + ProfileResults.demanglePath(entry.name);
                String values = PERCENT.format(entry.percentage) + "%  " + PERCENT.format(entry.globalPercentage) + "%";
                int rowY = panelTop + 12 + i * 10;
                graphics.text(font, name, left, rowY, entry.getColor());
                graphics.text(font, values, right - font.width(values), rowY, entry.getColor());
            }
        }
    }
}
