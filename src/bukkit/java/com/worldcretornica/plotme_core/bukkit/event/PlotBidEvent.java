package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBidEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBidEvent extends PlotEvent implements Cancellable {

    private final InternalPlotBidEvent event;

    public PlotBidEvent(PlotMe_Core instance, World world, Plot plot, Player bidder, double bid) {
        super(instance, plot, world);
        event = new InternalPlotBidEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(bidder), bid);
    }

    public PlotBidEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer bidder, double bid) {
        super(instance, plot, world);
        event = new InternalPlotBidEvent(instance, world, plot, bidder, bid);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public double getBid() {
        return event.getBid();
    }

    public double getPreviousBid() {
        return event.getPlot().getCurrentBid();
    }

    public String getPreviousBidder() {
        return event.getPlot().getCurrentBidder();
    }

    public InternalPlotBidEvent getInternal() {
        return event;
    }
}
