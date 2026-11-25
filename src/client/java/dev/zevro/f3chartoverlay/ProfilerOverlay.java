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
    private static final int OVERLAY_WIDTH = 270;
    private static ProfileResults cachedResults;

    private ProfilerOverlay() {}

    public static int getOverlayWidth() {
        return OVERLAY_WIDTH;
    }

    public static int getOverlayHeight() {
        OverlayConfig config = OverlayConfig.get();
        int listHeight = 0;
        if (config.showList) {
            int visibleEntries = 14;
            if (cachedResults != null) {
                ProfilerPieChart chart = Minecraft.getInstance().getDebugOverlay().getProfilerPieChart();
                List<ResultField> entries = cachedResults.getTimes(
                    ((ProfilerPieChartAccessor) chart).f3chart$getTreePath()
                );
                visibleEntries = Math.min(Math.max(0, entries.size() - 1), 14);
            }
            listHeight = visibleEntries * 10 + 18;
        }
        return (config.showPieChart ? 129 : 0) + listHeight;
    }

    public static void render(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        OverlayConfig config = OverlayConfig.get();
        if (!config.showPieChart && !config.showList) return;

        ProfilerPieChart vanillaChart = client.getDebugOverlay().getProfilerPieChart();
        ProfileResults latest = ((ProfilerPieChartAccessor) vanillaChart).f3chart$getResults();

        // A scheduled profiler reset produces one null-results tick. Keep displaying
        // the previous snapshot until the new profiler has collected usable data.
        if (latest != null) {
            List<ResultField> freshEntries = latest.getTimes(
                ((ProfilerPieChartAccessor) vanillaChart).f3chart$getTreePath()
            );
            if (freshEntries.size() > 1) {
                cachedResults = latest;
            }
        }

        if (cachedResults == null) return;

        List<ResultField> entries = new ArrayList<>(cachedResults.getTimes(
            ((ProfilerPieChartAccessor) vanillaChart).f3chart$getTreePath()
        ));
        if (entries.isEmpty()) return;

        ResultField root = entries.removeFirst();
        Font font = client.font;
        int right = OVERLAY_WIDTH;
        int visibleEntries = Math.min(entries.size(), 14);
        int listHeight = config.showList ? visibleEntries * 10 + 18 : 0;
        int overlayHeight = (config.showPieChart ? 129 : 0) + listHeight;
        int bottom = overlayHeight;
        float scale = Math.max(0.5F, Math.min(2.0F, config.scalePercent / 100.0F));
        int overlayX = graphics.guiWidth() - Math.round(right * scale) - config.x;
        int overlayY = graphics.guiHeight() - Math.round(overlayHeight * scale) - config.y;

        graphics.pose().pushMatrix();
        graphics.pose().translate(overlayX, overlayY);
        graphics.pose().scale(scale, scale);

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
            graphics.fill(-5, panelTop - 5, right + 5, bottom + 5, 0xA9000000);
            graphics.text(font, ProfileResults.demanglePath(root.name) + "  " + PERCENT.format(root.globalPercentage) + "%",
                0, panelTop, 0xFFFFFFFF);
            for (int i = 0; i < visibleEntries; i++) {
                ResultField entry = entries.get(i);
                String name = (i + 1) + ". " + ProfileResults.demanglePath(entry.name);
                String values = PERCENT.format(entry.percentage) + "%  " + PERCENT.format(entry.globalPercentage) + "%";
                int rowY = panelTop + 12 + i * 10;
                graphics.text(font, name, 0, rowY, entry.getColor());
                graphics.text(font, values, right - font.width(values), rowY, entry.getColor());
            }
        }

        graphics.pose().popMatrix();
    }
}
