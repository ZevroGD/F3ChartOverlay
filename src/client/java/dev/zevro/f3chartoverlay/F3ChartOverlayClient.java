package dev.zevro.f3chartoverlay;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class F3ChartOverlayClient implements ClientModInitializer {
    public static final String MOD_ID = "f3chartoverlay";
    private static KeyMapping openSettingsKey;

    @Override
    public void onInitializeClient() {
        OverlayConfig.load();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "general")
        );
        openSettingsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.f3chartoverlay.open_settings",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_O,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new OverlayConfigScreen());
                }
            }
        });

        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(MOD_ID, "profiler_overlay"),
            ProfilerOverlay::render
        );
    }

    public static void openSettings(Minecraft client) {
        client.setScreen(new OverlayConfigScreen());
    }
}
