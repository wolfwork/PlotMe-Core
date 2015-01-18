package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotProtectChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final boolean _protected;
    private boolean canceled;

    public InternalPlotProtectChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, boolean protect) {
        super(instance, plot, world);
        this.player = player;
        _protected = protect;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public boolean getProtected() {
        return _protected;
    }
}
