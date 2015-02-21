package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private Long[] currentClear;

    private PlotToClear plottoclear;
    private int taskId;

    public PlotMeSpool(PlotMe_Core instance, PlotToClear plotToClear) {
        plugin = instance;
        plottoclear = plotToClear;
    }

    @Override
    public void run() {
        if (getPlotToClear() != null) {
            IWorld world = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());
            PlotMeCoreManager plotMeCoreManager = PlotMeCoreManager.getInstance();
            IPlotMe_GeneratorManager genmanager = plotMeCoreManager.getGenManager(world);

            if (world != null) {
                if (currentClear == null) {
                    currentClear = genmanager
                            .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), null);
                } else {
                    currentClear = genmanager
                            .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"),
                                    currentClear);
                }

                if (currentClear == null) {
                    if (getPlotToClear().getReason() == ClearReason.Clear) {
                        genmanager.adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false, false);
                    } else {
                        genmanager.adjustPlotFor(world, getPlotToClear().getPlotId(), false, false, false, false);
                    }
                    if (plugin.getServerBridge().getUsingLwc()) {
                        plotMeCoreManager.removeLWC(world, getPlotToClear().getPlotId());
                    }
                    genmanager.refreshPlotChunks(world, getPlotToClear().getPlotId());

                    plottoclear.getRequester().sendMessage(
                            plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(getPlotToClear(), taskId);
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(getPlotToClear(), taskId);
                plottoclear = null;
                currentClear = null;
            }
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public PlotToClear getPlotToClear() {
        return plottoclear;
    }

}
