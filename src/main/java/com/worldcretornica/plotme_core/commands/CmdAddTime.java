package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdAddTime extends PlotCommand {

    public CmdAddTime(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_ADDTIME)) {
            if (manager.getMap(player).getDaysToExpiration() != 0) {
                if (manager.isPlotWorld(player)) {
                    String id = manager.getPlotId(player);

                    if (id.isEmpty()) {
                        player.sendMessage("§c" + C("MsgNoPlotFound"));
                    } else if (!manager.isPlotAvailable(id, player)) {
                        Plot plot = manager.getPlotById(id, player);
                        if (plot != null) {
                            String name = player.getName();

                            plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                            player.sendMessage(C("MsgPlotExpirationReset"));

                            if (isAdvancedLogging()) {
                                serverBridge.getLogger().info(name + " reset expiration on plot " + id);
                            }
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        return true;
                    }
                } else {
                    player.sendMessage("§c" + C("MsgNotPlotWorld"));
                    return true;
                }
            } else {
                player.sendMessage("Plots don't expire in this world");
                return true;
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
