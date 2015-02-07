package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.commands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class BukkitCommand implements CommandExecutor {

    private final PlotMe_Core api;
    private final CmdAdd add;
    private final CmdAddTime addTime;
    private final CmdAuction auction;
    private final CmdAuto auto;
    private final CmdBid bid;
    private final CmdBiome biome;
    private final CmdBiomes biomes;
    private final CmdBuy buy;
    private final CmdClaim claim;
    private final CmdClear clear;
    private final CmdDeny deny;
    private final CmdDispose dispose;
    private final CmdDone done;
    private final CmdDoneList doneList;
    private final CmdExpired expired;
    private final CmdHome home;
    private final CmdInfo info;
    private final CmdMove move;
    private final CmdPlotList plotList;
    private final CmdProtect protect;
    private final CmdReload reload;
    private final CmdRemove remove;
    private final CmdReset reset;
    private final CmdResetExpired resetExpired;
    private final CmdSell sell;
    private final CmdSetOwner setOwner;
    private final CmdShowHelp showHelp;
    private final CmdTP tp;
    private final CmdUndeny undeny;
    private final CmdWEAnywhere weAnywhere;
    private final CmdMiddle middle;
    private final PlotMe_CorePlugin plugin;

    public BukkitCommand(PlotMe_CorePlugin instance) {
        plugin = instance;
        api = instance.getAPI();
        add = new CmdAdd(api);
        addTime = new CmdAddTime(api);
        auction = new CmdAuction(api);
        auto = new CmdAuto(api);
        bid = new CmdBid(api);
        biome = new CmdBiome(api);
        biomes = new CmdBiomes(api);
        buy = new CmdBuy(api);
        claim = new CmdClaim(api);
        clear = new CmdClear(api);
        deny = new CmdDeny(api);
        dispose = new CmdDispose(api);
        done = new CmdDone(api);
        doneList = new CmdDoneList(api);
        expired = new CmdExpired(api);
        home = new CmdHome(api);
        info = new CmdInfo(api);
        move = new CmdMove(api);
        plotList = new CmdPlotList(api);
        protect = new CmdProtect(api);
        reload = new CmdReload(api);
        remove = new CmdRemove(api);
        reset = new CmdReset(api);
        resetExpired = new CmdResetExpired(api);
        sell = new CmdSell(api);
        setOwner = new CmdSetOwner(api);
        showHelp = new CmdShowHelp(api);
        tp = new CmdTP(api);
        undeny = new CmdUndeny(api);
        middle = new CmdMiddle(api);
        weAnywhere = new CmdWEAnywhere(api);
    }

    private String C(String caption) {
        return api.getUtil().C(caption);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage(C("ConsoleHelpMain"));
                sender.sendMessage("- /plotme reload");
                sender.sendMessage(C("ConsoleHelpReload"));
                return true;
            } else {
                if ("reload".equalsIgnoreCase(args[0])) {
                    return reload.exec(new BukkitCommandSender(sender));
                }
                if ("resetexpired".equalsIgnoreCase(args[0])) {
                    return resetExpired.exec(new BukkitCommandSender(sender), args);
                }
            }
        } else {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) sender);

            if (args.length == 0) {
                return showHelp.exec(player, 1);
            } else {
                int page = -1;

                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                }

                if (page == -1) {
                    if ("help".equalsIgnoreCase(args[0])) {
                        if (args.length > 1) {
                            String a1 = args[1];
                            page = -1;

                            try {
                                page = Integer.parseInt(a1);
                            } catch (NumberFormatException ignored) {
                            }
                        }

                        if (page == -1) {
                            return showHelp.exec(player, 1);
                        } else {
                            return showHelp.exec(player, page);
                        }
                    }
                    if ("reload".equalsIgnoreCase(args[0])) {
                        if (player.hasPermission("plotme.admin.reload")) {
                            return reload.exec(player);
                        }
                    }
                    if ("claim".equalsIgnoreCase(args[0])) {
                        return claim.exec(player, args);
                    }
                    if ("auto".equalsIgnoreCase(args[0])) {
                        return auto.exec(player, args);
                    }
                    if ("info".equalsIgnoreCase(args[0]) || "i".equalsIgnoreCase(args[0])) {
                        return info.exec(player);
                    }
                    if ("biome".equalsIgnoreCase(args[0])) {
                        return biome.exec(player, args);
                    }
                    if ("biomes".equalsIgnoreCase(args[0])) {
                        return biomes.exec(player, args);
                    }
                    if ("tp".equalsIgnoreCase(args[0])) {
                        return tp.exec(player, args);
                    }
                    if ("clear".equalsIgnoreCase(args[0])) {
                        return clear.exec(player);
                    }
                    if ("reset".equalsIgnoreCase(args[0])) {
                        return reset.exec(player);
                    }
                    if ("add".equalsIgnoreCase(args[0]) || "+".equalsIgnoreCase(args[0])) {
                        return add.exec(player, args);
                    }
                    if ("deny".equalsIgnoreCase(args[0])) {
                        return deny.exec(player, args);
                    }
                    if ("undeny".equalsIgnoreCase(args[0])) {
                        return undeny.exec(player, args);
                    }
                    if ("remove".equalsIgnoreCase(args[0]) || "-".equalsIgnoreCase(args[0])) {
                        return remove.exec(player, args);
                    }
                    if ("setowner".equalsIgnoreCase(args[0])) {
                        return setOwner.exec(player, args);
                    }
                    if ("move".equalsIgnoreCase(args[0])) {
                        return move.exec(player, args);
                    }
                    if ("weanywhere".equalsIgnoreCase(args[0])) {
                        return weAnywhere.exec(player);
                    }
                    if ("list".equalsIgnoreCase(args[0])) {
                        return plotList.exec(player, args);
                    }
                    if ("expired".equalsIgnoreCase(args[0])) {
                        return expired.exec(player, args);
                    }
                    if ("addtime".equalsIgnoreCase(args[0])) {
                        return addTime.exec(player);
                    }
                    if ("done".equalsIgnoreCase(args[0])) {
                        return done.exec(player);
                    }
                    if ("donelist".equalsIgnoreCase(args[0])) {
                        return doneList.exec(player, args);
                    }
                    if ("protect".equalsIgnoreCase(args[0])) {
                        return protect.exec(player);
                    }
                    if ("sell".equalsIgnoreCase(args[0])) {
                        return sell.exec(player, args);
                    }
                    if ("dispose".equalsIgnoreCase(args[0])) {
                        return dispose.exec(player);
                    }
                    if ("auction".equalsIgnoreCase(args[0])) {
                        return auction.exec(player, args);
                    }
                    if ("buy".equalsIgnoreCase(args[0])) {
                        return buy.exec(player);
                    }
                    if ("bid".equalsIgnoreCase(args[0])) {
                        return bid.exec(player, args);
                    }
                    if ("middle".equalsIgnoreCase(args[0])) {
                        return middle.exec(player);
                    }
                    // arg can be "home" or "home:n"
                    if ((args[0].toLowerCase() + ":").startsWith("home:") || (args[0].toLowerCase() + ":").startsWith("h:")) {
                        return home.exec(player, args);
                    }
                } else {
                    return showHelp.exec(player, page);
                }
            }
        }
        return false;
    }
}
