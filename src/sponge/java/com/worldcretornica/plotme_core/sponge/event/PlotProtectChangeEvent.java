package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.bukkit.event.Cancellable;
import org.spongepowered.api.entity.player.Player;

public class PlotProtectChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotProtectChangeEvent event;

    public PlotProtectChangeEvent(IWorld world, Plot plot, IPlayer player, boolean protect) {
        super(plot, world);
        event = new InternalPlotProtectChangeEvent(world, plot, player, protect);
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
        return ((SpongePlayer) event.getPlayer()).getPlayer();
    }

    public boolean isProtected() {
        return event.isProtected();
    }

    public InternalPlotProtectChangeEvent getInternal() {
        return event;
    }
}
