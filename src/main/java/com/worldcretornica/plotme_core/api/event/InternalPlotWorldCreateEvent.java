package com.worldcretornica.plotme_core.api.event;

import java.util.Map;

public class InternalPlotWorldCreateEvent implements ICancellable, Event {

    private final String worldName;
    private final Map<String, String> parameters;
    private boolean canceled;

    public InternalPlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        this.worldName = worldName;
        this.parameters = parameters;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Get the name of the world created
     * @return world name
     */
    public String getWorldName() {
        return worldName;
    }


    public Map<String, String> getParameters() {
        return parameters;
    }

}
