# F3 Chart Overlay

A client-side Fabric mod for Minecraft Java 26.2. It keeps the vanilla debug profiler pie-chart data visible as a HUD overlay even when the F3 debug screen is closed.

## Features

- Always-visible profiler pie chart; F3 does not need to be held open.
- Pie chart and profiler entry list can be enabled independently.
- Configurable refresh rate from 1 to 30 updates per second.
- In-game settings screen, opened with **O** by default.
- The settings key can be rebound under **Options → Controls → F3 Chart Overlay**.
- Settings are saved to config/f3chartoverlay.json.

## Settings

Press **O** in-game to open the settings screen. The pie chart and profiler list have separate toggles. Enter a refresh rate from 1 to 30 and press Save. Values outside that range are clamped.

## Build

Requires JDK 25 and Gradle 9.7.1.

```sh
gradle build
```

The built mod JAR is written to build/libs/.

## GitHub Actions

The workflow at .github/workflows/build.yml builds on pushes to main, pull requests, and manual dispatches. It uploads the built JAR as the F3ChartOverlay artifact.

## Compatibility

- Minecraft Java 26.2
- Fabric Loader
- Fabric API
- Client-side only

## License

MIT
