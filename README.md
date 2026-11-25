# F3 Chart Overlay

A client-side Fabric mod for Minecraft Java 26.2. It keeps the vanilla debug profiler pie-chart data visible as a HUD overlay even when the F3 debug screen is closed.

## Features

- Always-visible profiler pie chart; F3 does not need to be held open.
- Pie chart and profiler entry list can be enabled independently.
- Configurable refresh rate from 1 to 30 updates per second.
- Configurable overlay scale from 50% to 200%.
- Configurable position using right and bottom offsets.
- Reopening the F3 debug screen starts a fresh profiler collection session.
- In-game settings screen, opened with **O** by default or through Mod Menu.
- The settings key can be rebound under **Options → Controls → F3 Chart Overlay**.
- Settings are saved to config/f3chartoverlay.json.

## Settings

Open the settings with **O** or through the Mod Menu configuration button. The pie chart and profiler list have separate toggles.

- **Refresh rate:** 1 to 30 updates per second.
- **Right offset:** distance from the right edge of the screen.
- **Bottom offset:** distance from the bottom edge of the screen.
- **Scale:** 50% to 200%.

The default position is 8 GUI-scaled pixels from the right and bottom edges, at 100% scale. Values are clamped to their supported ranges.

## Build

Requires JDK 25 and Gradle 9.8.0.

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
