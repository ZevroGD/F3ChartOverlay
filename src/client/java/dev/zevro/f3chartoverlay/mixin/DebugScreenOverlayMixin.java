package dev.zevro.f3chartoverlay.mixin;

import dev.zevro.f3chartoverlay.OverlayConfig;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Inject(method = "showProfilerChart", at = @At("RETURN"), cancellable = true)
    private void f3chart$keepProfilerRunning(CallbackInfoReturnable<Boolean> cir) {
        OverlayConfig config = OverlayConfig.get();
        if (config.showPieChart || config.showList) {
            cir.setReturnValue(true);
        }
    }
}
