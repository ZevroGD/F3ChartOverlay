# Changelog

## 1.0.0

First stable release.

### Added
- Configurable overlay scale from 50% to 200%.
- Configurable horizontal and vertical placement using offsets from the bottom-right corner.
- Settings for position and scale in the in-game configuration screen.
- Mod Menu configuration entry.
- Independent visibility controls for the profiler pie chart and profiler list.
- Configurable profiler refresh rate from 1 to 30 updates per second.

### Improved
- Reopening the F3 debug screen resets profiler collection so the overlay can display fresh profiling data.
- Configuration values are validated and clamped to supported ranges.
- Build workflow uses Java 25 and Gradle 9.8.0 for Minecraft 26.2.

### Compatibility
- Minecraft Java 26.2
- Fabric Loader and Fabric API
- Client-side only
