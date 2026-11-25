package dev.zevro.f3chartoverlay;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class OverlayConfigScreen extends Screen {
    private EditBox refreshRateField;
    private Button chartButton;
    private Button listButton;

    public OverlayConfigScreen() {
        super(Component.literal("F3 Chart Overlay"));
    }

    @Override
    protected void init() {
        OverlayConfig config = OverlayConfig.get();
        int center = this.width / 2;
        int top = this.height / 2 - 72;

        chartButton = Button.builder(chartLabel(config.showPieChart), button -> {
            config.showPieChart = !config.showPieChart;
            button.setMessage(chartLabel(config.showPieChart));
            OverlayConfig.save();
        }).bounds(center - 100, top, 200, 20).build();
        this.addRenderableWidget(chartButton);

        listButton = Button.builder(listLabel(config.showList), button -> {
            config.showList = !config.showList;
            button.setMessage(listLabel(config.showList));
            OverlayConfig.save();
        }).bounds(center - 100, top + 26, 200, 20).build();
        this.addRenderableWidget(listButton);

        refreshRateField = new EditBox(
            this.font, center - 30, top + 62, 60, 20,
            Component.literal("Refresh rate")
        );
        refreshRateField.setMaxLength(2);
        refreshRateField.setValue(Integer.toString(config.refreshRate));
        this.addRenderableWidget(refreshRateField);

        this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> saveAndClose())
            .bounds(center - 100, top + 100, 95, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> this.onClose())
            .bounds(center + 5, top + 100, 95, 20).build());
    }

    private static Component chartLabel(boolean enabled) {
        return Component.literal("Pie chart: " + (enabled ? "ON" : "OFF"));
    }

    private static Component listLabel(boolean enabled) {
        return Component.literal("Profiler list: " + (enabled ? "ON" : "OFF"));
    }

    private void saveAndClose() {
        String value = refreshRateField.getValue().trim();
        if (!value.isEmpty()) {
            try {
                OverlayConfig.get().setRefreshRate(Integer.parseInt(value));
            } catch (NumberFormatException ignored) {
                // Keep the previous value if the field is not a valid integer.
            }
        }
        OverlayConfig.save();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 105, 0xFFFFFFFF);
        graphics.text(this.font, "Refresh rate (1-30 updates per second)", this.width / 2 - 100,
            this.height / 2 - 10, 0xFFFFFFFF);
        graphics.text(this.font, "Default key: O (change it in Options > Controls)", this.width / 2 - 100,
            this.height / 2 + 48, 0xFFAAAAAA);
    }
}
