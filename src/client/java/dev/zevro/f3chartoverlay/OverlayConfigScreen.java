package dev.zevro.f3chartoverlay;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class OverlayConfigScreen extends Screen {
    private final Screen parent;
    private EditBox refreshRateField;
    private EditBox rightOffsetField;
    private EditBox bottomOffsetField;
    private EditBox scaleField;

    public OverlayConfigScreen() {
        this(null);
    }

    public OverlayConfigScreen(Screen parent) {
        super(Component.literal("F3 Chart Overlay"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        OverlayConfig config = OverlayConfig.get();
        int center = this.width / 2;
        int top = this.height / 2 - 108;

        this.addRenderableWidget(Button.builder(chartLabel(config.showPieChart), button -> {
            config.showPieChart = !config.showPieChart;
            button.setMessage(chartLabel(config.showPieChart));
            OverlayConfig.save();
        }).bounds(center - 100, top, 200, 20).build());

        this.addRenderableWidget(Button.builder(listLabel(config.showList), button -> {
            config.showList = !config.showList;
            button.setMessage(listLabel(config.showList));
            OverlayConfig.save();
        }).bounds(center - 100, top + 26, 200, 20).build());

        refreshRateField = createField(center - 30, top + 66, "Refresh interval (seconds)", config.refreshRate, 2);
        rightOffsetField = createField(center - 100, top + 112, "Right offset", config.x, 4);
        bottomOffsetField = createField(center + 15, top + 112, "Bottom offset", config.y, 4);
        scaleField = createField(center - 30, top + 158, "Scale", config.scalePercent, 3);

        this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> saveAndClose())
            .bounds(center - 100, top + 190, 95, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> this.onClose())
            .bounds(center + 5, top + 190, 95, 20).build());
    }

    private EditBox createField(int x, int y, String label, int value, int maxLength) {
        EditBox field = new EditBox(this.font, x, y, 85, 20, Component.literal(label));
        field.setMaxLength(maxLength);
        field.setValue(Integer.toString(value));
        this.addRenderableWidget(field);
        return field;
    }

    private static Component chartLabel(boolean enabled) {
        return Component.literal("Pie chart: " + (enabled ? "ON" : "OFF"));
    }

    private static Component listLabel(boolean enabled) {
        return Component.literal("Profiler list: " + (enabled ? "ON" : "OFF"));
    }

    private static int readInteger(EditBox field, int fallback) {
        try {
            return Integer.parseInt(field.getValue().trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private void saveAndClose() {
        OverlayConfig config = OverlayConfig.get();
        config.setRefreshRate(readInteger(refreshRateField, config.refreshRate));
        config.setPosition(
            readInteger(rightOffsetField, config.x),
            readInteger(bottomOffsetField, config.y)
        );
        config.setScalePercent(readInteger(scaleField, config.scalePercent));
        OverlayConfig.save();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        int center = this.width / 2;
        int top = this.height / 2 - 108;
        graphics.centeredText(this.font, this.title, center, top - 24, 0xFFFFFFFF);
        graphics.text(this.font, "Refresh interval (1-30 seconds)", center - 100, top + 51, 0xFFFFFFFF);
        graphics.text(this.font, "Right offset", center - 100, top + 98, 0xFFFFFFFF);
        graphics.text(this.font, "Bottom offset", center + 15, top + 98, 0xFFFFFFFF);
        graphics.text(this.font, "Scale (50-200%)", center - 100, top + 143, 0xFFFFFFFF);
        graphics.text(this.font, "Offsets are measured from the bottom-right corner.", center - 100,
            top + 222, 0xFFAAAAAA);
        graphics.text(this.font, "Default key: O (rebind in Options > Controls)", center - 100,
            top + 234, 0xFFAAAAAA);
    }
}
