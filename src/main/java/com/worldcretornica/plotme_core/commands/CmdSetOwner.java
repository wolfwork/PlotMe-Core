package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdSetOwner extends PlotCommand {

    public CmdSetOwner(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        IWorld world = player.getWorld();
        PlotMapInfo pmi = manager.getMap(world);
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER)) {
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else {
                    String newOwner = args[1];
                    if (newOwner.startsWith("group:")) {
                        player.sendMessage("You cannot make a group an owner. Try adding them to the plot instead.");
                        return true;
                    }
                    String oldowner = "<" + C("WordNotApplicable") + ">";
                    String playerName = player.getName();

                    if (!manager.isPlotAvailable(id, pmi)) {
                        Plot plot = manager.getPlotById(id, pmi);

                        oldowner = plot.getOwner();

                        InternalPlotOwnerChangeEvent event;

                        if (manager.isEconomyEnabled(world)) {
                            if (pmi.isRefundClaimPriceOnSetOwner() && !newOwner.equals(oldowner)) {
                                event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newOwner);

                                if (event.isCancelled()) {
                                    return true;
                                }
                                if (plot.getOwnerId() != null) {
                                    IOfflinePlayer playeroldowner = serverBridge.getOfflinePlayer(plot.getOwnerId());
                                    EconomyResponse er = serverBridge.depositPlayer(playeroldowner, pmi.getClaimPrice());

                                    if (er.transactionSuccess()) {
                                        IPlayer oldOwner = serverBridge.getPlayer(plot.getOwnerId());
                                        if (oldOwner != null) {
                                            oldOwner.sendMessage(
                                                    C("MsgYourPlot") + " " + id + " " + C("MsgNowOwnedBy") + " " + newOwner + ". " + Util()
                                                            .moneyFormat(pmi.getClaimPrice(), true));
                                        }
                                    } else {
                                        player.sendMessage("§c" + er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newOwner);
                            }

                            if (plot.getCurrentBidderId() != null) {
                                IOfflinePlayer playercurrentbidder = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());
                                EconomyResponse er = serverBridge.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                if (er.transactionSuccess()) {
                                    IPlayer currentBidder = serverBridge.getPlayer(playercurrentbidder.getUniqueId());
                                    if (currentBidder != null) {
                                        currentBidder.sendMessage(
                                                C("WordPlot") + " " + id + " " + C("MsgChangedOwnerFrom") + " " + oldowner + " " + C("WordTo") + " "
                                                        + newOwner + ". " + Util().moneyFormat(plot.getCurrentBid(), true));
                                    }
                                } else {
                                    player.sendMessage(er.errorMessage);
                                    serverBridge.getLogger().warning(er.errorMessage);
                                }
                            }
                        } else {
                            event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newOwner);
                        }

                        if (!event.isCancelled()) {
                            plot.setCurrentBidder(null);
                            plot.setCurrentBidderId(null);
                            plot.setCurrentBid(0.0);
                            plot.setAuctioned(false);
                            plot.setForSale(false);

                            manager.removeAuctionSign(world, id);
                            manager.removeSellSign(world, id);

                            plot.updateField("currentbidder", null);
                            plot.updateField("currentbid", 0);
                            plot.updateField("auctionned", false);
                            plot.updateField("forsale", false);
                            plot.updateField("currentbidderid", null);

                            plot.setOwner(newOwner);

                            manager.setOwnerSign(world, plot);

                            plot.updateField("owner", newOwner);
                        }
                    } else {
                        manager.createPlot(world, id, newOwner, null, pmi);
                    }

                    player.sendMessage(C("MsgOwnerChangedTo") + " §c" + newOwner);

                    if (isAdvancedLogging()) {
                        serverBridge.getLogger()
                                .info(playerName + " " + C("MsgChangedOwnerOf") + " " + id + " " + C("WordFrom") + " " + oldowner + " " + C("WordTo")
                                        + " " + newOwner);
                    }
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
