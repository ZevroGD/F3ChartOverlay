package dev.zevro.f3chartoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public final class F3ChartOverlayClient implements ClientModInitializer {
    public static final String MOD_ID = "f3chartoverlay";

    @Override
    public void onInitializeClient() {
        OverlayConfig.load();
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(MOD_ID, "profiler_overlay"),
            ProfilerOverlay::render
        );
    }
}
