package dev.zevro.f3chartoverlay.mixin;

import dev.zevro.f3chartoverlay.OverlayConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    private static boolean f3chart$wasDebugOverlayVisible;
    private static long f3chart$lastProfilerResetNanos;

    @Inject(method = "showProfilerChart", at = @At("RETURN"), cancellable = true)
    private void f3chart$keepProfilerRunning(CallbackInfoReturnable<Boolean> cir) {
        OverlayConfig config = OverlayConfig.get();
        if (!config.showPieChart && !config.showList) {
            return;
        }

        long now = System.nanoTime();
        boolean debugOverlayVisible = Minecraft.getInstance().debugEntries.isOverlayVisible();
        boolean reopenedDebugOverlay = debugOverlayVisible && !f3chart$wasDebugOverlayVisible;
        f3chart$wasDebugOverlayVisible = debugOverlayVisible;

        long refreshInterval = Math.max(1, Math.min(30, config.refreshRate)) * 1_000_000_000L;
        boolean refreshDue = f3chart$lastProfilerResetNanos == 0L
            || now - f3chart$lastProfilerResetNanos >= refreshInterval;

        if (reopenedDebugOverlay || refreshDue) {
            // One inactive profiler tick makes Minecraft create a fresh ActiveProfiler.
            // ProfilerOverlay keeps its last valid results during this reset tick.
            f3chart$lastProfilerResetNanos = now;
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(true);
    }

    @Redirect(
        method = "extractRenderState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/debugchart/ProfilerPieChart;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
        )
    )
    private void f3chart$suppressVanillaProfilerChart(ProfilerPieChart chart, GuiGraphicsExtractor graphics) {
        // The mod draws its own chart/list. Never render the vanilla chart.
    }
}
