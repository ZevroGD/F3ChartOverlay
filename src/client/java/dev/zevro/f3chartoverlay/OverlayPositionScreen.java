package dev.zevro.f3chartoverlay;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public final class OverlayPositionScreen extends Screen {
    private final Screen parent;
    private boolean dragging;
    private double dragStartX;
    private double dragStartY;
    private int overlayStartX;
    private int overlayStartY;

    public OverlayPositionScreen(Screen parent) {
        super(Component.literal("Edit F3 Chart Overlay"));
        this.parent = parent;
    }

    private int overlayX() {
        float scale = OverlayConfig.get().scalePercent / 100.0F;
        return this.width - Math.round(ProfilerOverlay.getOverlayWidth() * scale) - OverlayConfig.get().x;
    }

    private int overlayY() {
        float scale = OverlayConfig.get().scalePercent / 100.0F;
        return this.height - Math.round(ProfilerOverlay.getOverlayHeight() * scale) - OverlayConfig.get().y;
    }

    private boolean isOverOverlay(double mouseX, double mouseY) {
        float scale = OverlayConfig.get().scalePercent / 100.0F;
        int left = overlayX() - Math.round(5 * scale);
        int top = overlayY() - Math.round(5 * scale);
        int right = overlayX() + Math.round((ProfilerOverlay.getOverlayWidth() - 5) * scale);
        int bottom = overlayY() + Math.round((ProfilerOverlay.getOverlayHeight() + 5) * scale);
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && isOverOverlay(event.x(), event.y())) {
            dragging = true;
            dragStartX = event.x();
            dragStartY = event.y();
            overlayStartX = overlayX();
            overlayStartY = overlayY();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (!dragging || event.button() != 0) return false;
        int newX = overlayStartX + (int) Math.round(event.x() - dragStartX);
        int newY = overlayStartY + (int) Math.round(event.y() - dragStartY);
        float scale = OverlayConfig.get().scalePercent / 100.0F;
        OverlayConfig config = OverlayConfig.get();
        config.x = Math.max(0, Math.min(4096,
            this.width - Math.round(ProfilerOverlay.getOverlayWidth() * scale) - newX));
        config.y = Math.max(0, Math.min(4096,
            this.height - Math.round(ProfilerOverlay.getOverlayHeight() * scale) - newY));
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (dragging && event.button() == 0) {
            dragging = false;
            OverlayConfig.save();
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isOverOverlay(mouseX, mouseY) && scrollY != 0) {
            OverlayConfig config = OverlayConfig.get();
            int oldX = overlayX();
            int oldY = overlayY();
            config.setScalePercent(config.scalePercent + (scrollY > 0 ? 5 : -5));
            float scale = config.scalePercent / 100.0F;
            config.x = Math.max(0, Math.min(4096,
                this.width - Math.round(ProfilerOverlay.getOverlayWidth() * scale) - oldX));
            config.y = Math.max(0, Math.min(4096,
                this.height - Math.round(ProfilerOverlay.getOverlayHeight() * scale) - oldY));
            OverlayConfig.save();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void onClose() {
        OverlayConfig.save();
        this.minecraft.gui.setScreen(parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, "Drag the overlay to move it", this.width / 2, 18, 0xFFFFFFFF);
        graphics.centeredText(this.font, "Scroll over it to resize (50-200%)", this.width / 2, 32, 0xFFFFFFFF);
        graphics.centeredText(this.font, "Press ESC when finished", this.width / 2, 46, 0xFFAAAAAA);
    }
}
