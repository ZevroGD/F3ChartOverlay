# F3 Chart Overlay

A client-side Fabric mod for Minecraft Java 26.2.

## Features in progress

- Always-on-screen profiler pie chart, independent of the F3 debug overlay.
- Independently toggleable profiler entry list.
- Configurable refresh rate from 1 to 30 updates per second.
- Persistent configuration in `.minecraft/config/f3chartoverlay.json`.
- Future settings screen, keybinds and draggable positioning.

The overlay reads Minecraft's own profiler results. It does not fabricate profiler entries. When either overlay component is enabled, a small mixin keeps vanilla's frame profiler collecting data even while the debug screen is closed.

## Build

Requires JDK 25.

```bash
gradle build
```
