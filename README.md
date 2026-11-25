# F3 Chart Overlay

A client-side Fabric mod for Minecraft Java 26.2.

## Planned features

- An always-visible profiler pie-chart overlay that does not require the F3 debug screen.
- A separately toggleable profiler entry list.
- Independent visibility controls for the chart and list.
- Configurable profiler sampling refresh rate from 1 to 30 updates per second.
- A settings screen and movable overlay.
- Persistent configuration in `.minecraft/config/f3chartoverlay.json`.

## Development status

The initial project scaffold and HUD registration are in place. The current HUD is a visual placeholder: the next implementation step is connecting it to the same profiler data used by Minecraft's vanilla debug pie chart. It deliberately does not invent or display fake profiler values.

## Build

Requires JDK 25.

```bash
./gradlew build
```
