package dev.zevro.f3chartoverlay.mixin;

import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.util.profiling.ProfileResults;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProfilerPieChart.class)
public interface ProfilerPieChartAccessor {
    @Accessor("profilerPieChartResults")
    ProfileResults f3chart$getResults();

    @Accessor("profilerTreePath")
    String f3chart$getTreePath();
}
