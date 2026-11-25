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
    private static boolean f3chart$resetProfilerOnNextVisibleFrame;

    @Inject(method = "showProfilerChart", at = @At("RETURN"), cancellable = true)
    private void f3chart$keepProfilerRunning(CallbackInfoReturnable<Boolean> cir) {
        OverlayConfig config = OverlayConfig.get();
        if (!config.showPieChart && !config.showList) {
            return;
        }

        // Reopening the F3 debug overlay starts a fresh vanilla profiler session.
        // Reproduce that reset for our always-visible overlay with one inactive frame.
        boolean debugOverlayVisible = Minecraft.getInstance().debugEntries.isOverlayVisible();
        if (debugOverlayVisible && !f3chart$wasDebugOverlayVisible) {
            f3chart$resetProfilerOnNextVisibleFrame = true;
        }
        f3chart$wasDebugOverlayVisible = debugOverlayVisible;

        if (f3chart$resetProfilerOnNextVisibleFrame) {
            f3chart$resetProfilerOnNextVisibleFrame = false;
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
