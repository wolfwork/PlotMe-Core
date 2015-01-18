package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDisposeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotDisposeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotDisposeEvent event;

    public PlotDisposeEvent(PlotMe_Core instance, World world, Plot plot, Player disposer) {
        super(instance, plot, world);
        event = new InternalPlotDisposeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(disposer));
    }

    public PlotDisposeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer disposer) {
        super(instance, plot, world);
        event = new InternalPlotDisposeEvent(instance, world, plot, disposer);
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

    public InternalPlotDisposeEvent getInternal() {
        return event;
    }
}
