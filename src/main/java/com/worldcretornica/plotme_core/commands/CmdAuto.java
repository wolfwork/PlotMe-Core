package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAuto extends PlotCommand {

    public CmdAuto(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "auto";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_AUTO)) {
            if (manager.isPlotWorld(player) || plugin.getConfig().getBoolean("allowWorldTeleport")) {
                IWorld world;
                if (!manager.isPlotWorld(player) && plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    if (args.length == 2) {
                        world = serverBridge.getWorld(args[1]);
                    } else {
                        world = manager.getFirstWorld();
                    }
                    if (world == null) {
                        player.sendMessage(C("MsgNotPlotWorld"));
                        return true;
                    }
                    if (!manager.isPlotWorld(world)) {
                        player.sendMessage(world + " " + C("MsgWorldNotPlot"));
                        return true;
                    }
                } else {
                    world = player.getWorld();
                }

                int playerLimit = getPlotLimit(player);

                int plotsOwned = manager.getOwnedPlotCount(player.getUniqueId(), world.getName().toLowerCase());

                if (playerLimit != -1 && plotsOwned >= playerLimit && !player.hasPermission("PlotMe.admin")) {
                    player.sendMessage(C("MsgAlreadyReachedMaxPlots") + " (" + plotsOwned + "/" + playerLimit + "). " + C("WordUse")
                            + " /plotme home " + C("MsgToGetToIt"));
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(world);
                int limit = pmi.getPlotAutoLimit();

                int x = 0;
                int z = 0;
                int dx = 0;
                int dz = -1;
                int t = limit;
                int maxPlots = t * t;

                for (int i = 0; i < maxPlots; i++) {
                    if (-limit / 2 <= x && x <= limit / 2 && -limit / 2 <= z && z <= limit / 2) {
                        PlotId id = new PlotId(x, z, world);
                        if (manager.isPlotAvailable(id, pmi)) {
                            double price = 0.0;

                            InternalPlotCreateEvent event = new InternalPlotCreateEvent(world, id, player);
                            serverBridge.getEventBus().post(event);
                            if (manager.isEconomyEnabled(pmi)) {
                                price = pmi.getClaimPrice();
                                double balance = serverBridge.getBalance(player);

                                if (balance < price) {
                                    player.sendMessage(C("MsgNotEnoughAuto") + " " + C("WordMissing") + " " + plugin
                                            .moneyFormat(price - balance, false));
                                    return true;
                                } else {

                                    if (event.isCancelled()) {
                                        return true;
                                    }
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!event.isCancelled()) {
                                manager.createPlot(world, id, player.getName(), player.getUniqueId(), pmi);

                                player.setLocation(manager.getPlotHome(world, id));

                                player.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " /plotme home " + C("MsgToGetToIt"));

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
                    if (x == z || x < 0 && x == -z || x > 0 && x == 1 - z) {
                        t = dx;
                        dx = -dz;
                        dz = t;
                    }
                    x += dx;
                    z += dz;
                }
                player.sendMessage(C("MsgNoPlotFound"));
                return true;
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdAutoUsage");
    }

}