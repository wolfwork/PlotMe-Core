package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotAddAllowedEvent extends PlotPlayerAddEvent implements ICancellable, Event {

    public InternalPlotAddAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        super(world, plot, player, allowed);
    }

    /**
     * Get the UUID as a string of the player that was allowed to the plot.
     * @return the UUID as a string of the player added
     */
    public String getAllowedPlayer() {
        return super.getAddedPlayer();
    }
}
