package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import com.worldcretornica.plotme_core.storage.Database;

import java.util.List;

public class PlotRunnableDeleteExpire implements Runnable {

    private final PlotMe_Core plugin;
    private final ICommandSender sender;

    public PlotRunnableDeleteExpire(PlotMe_Core instance, ICommandSender sender) {
        plugin = instance;
        this.sender = sender;
    }

    @Override
    public void run() {
        Database sqlmanager = plugin.getSqlManager();
        PlotMeCoreManager plotMeCoreManager = PlotMeCoreManager.getInstance();

        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            IWorld world = plugin.getWorldCurrentlyProcessingExpired();
            List<Plot> expiredPlots = sqlmanager.getExpiredPlots(world);

            if (expiredPlots.isEmpty()) {
                plugin.setCounterExpired(0);
            } else {
                String ids = "";

                for (Plot expiredPlot : expiredPlots) {
                    InternalPlotResetEvent event = new InternalPlotResetEvent(world, expiredPlot, sender);
                    plugin.getServerBridge().getEventBus().post(event);
                    if (!event.isCancelled()) {
                        plotMeCoreManager.clear(world, expiredPlot, sender, ClearReason.Expired);

                        PlotId id = expiredPlot.getId();
                        ids += id + ", ";

                        plotMeCoreManager.removePlot(world, id);
                        plotMeCoreManager.removeOwnerSign(world, id);
                        plotMeCoreManager.removeSellSign(world, id);

                        sqlmanager.deletePlot(expiredPlot.getInternalID());

                        plugin.setCounterExpired(plugin.getCounterExpired() - 1);
                    }
                }

                if (", ".equals(ids.substring(ids.length() - 2))) {
                    ids = ids.substring(0, ids.length() - 2);
                }

                plugin.getLogger().info(plugin.C("MsgDeletedExpiredPlots") + " " + ids);
            }

            if (plugin.getCounterExpired() == 0) {
                plugin.getLogger().info(plugin.C("MsgDeleteSessionFinished"));
                plugin.setWorldCurrentlyProcessingExpired(null);
            }
        }
    }
}