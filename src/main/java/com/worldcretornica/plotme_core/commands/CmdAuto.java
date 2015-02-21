package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAuto extends PlotCommand {

    public CmdAuto(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.USER_AUTO)) {
            if (manager.isPlotWorld(player) || serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                IWorld world;
                if (!manager.isPlotWorld(player) && serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    if (args.length == 2) {
                        world = serverBridge.getWorld(args[1]);
                    } else {
                        world = manager.getFirstWorld();
                    }

                    if (!manager.isPlotWorld(world)) {
                        player.sendMessage("§c" + world + " " + C("MsgWorldNotPlot"));
                        return true;
                    }
                } else {
                    world = player.getWorld();
                }

                PlotMapInfo pmi = manager.getMap(world);
                int playerLimit = getPlotLimit(player);

                int plotsOwned = manager.getNbOwnedPlot(player.getUniqueId(), world.getName().toLowerCase());

                if (playerLimit != -1 && plotsOwned >= playerLimit && !player.hasPermission("PlotMe.admin")) {
                    player.sendMessage("§c" + C("MsgAlreadyReachedMaxPlots") + " (" + plotsOwned + "/" + playerLimit + "). " + C("WordUse")
                            + " §c/plotme home§r " + C("MsgToGetToIt"));
                } else {
                    int limit = pmi.getPlotAutoLimit();

                    int x = 0;
                    int z = 0;
                    int dx = 0;
                    int dz = -1;
                    int t = limit;
                    int maxPlots = t * t;

                    for (int i = 0; i < maxPlots; i++) {
                        if ((-limit / 2 <= x) && (x <= limit / 2) && (-limit / 2 <= z) && (z <= limit / 2)) {
                            PlotId id = new PlotId(x, z);
                            if (manager.isPlotAvailable(id, pmi)) {
                                double price = 0.0;

                                InternalPlotCreateEvent event;

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getClaimPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughAuto") + " " + C("WordMissing") + " §r" + Util()
                                                .moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);
                                }
                                if (!event.isCancelled()) {
                                    manager.createPlot(world, id, player.getName(), player.getUniqueId(), pmi);

                                    player.setLocation(manager.getPlotHome(world, id));

                                    player.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));

                                    if (isAdvancedLogging()) {
                                        if (price == 0) {
                                            serverBridge.getLogger().info(player.getName() + " " + C("MsgClaimedPlot") + " " + id);
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgClaimedPlot") + " " + id + (" " + C("WordFor") + " "
                                                            + price));
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                        if ((x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1 - z))) {
                            t = dx;
                            dx = -dz;
                            dz = t;
                        }
                        x += dx;
                        z += dz;
                    }
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
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