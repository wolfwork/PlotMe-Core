package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class IServerBridge {

    private boolean usinglwc;

    public abstract IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname);

    public abstract IPlotMe_ChunkGenerator getPlotMeGenerator(String worldname);

    public abstract IOfflinePlayer getOfflinePlayer(UUID uuid);

    public abstract IOfflinePlayer getOfflinePlayer(String player);

    /**
     * Gets the player from the given UUID.
     *
     * @param uuid UUID of the player to retrieve
     * @return a player if one was found, null otherwise
     */
    public abstract IPlayer getPlayer(UUID uuid);

    public abstract IPlayer getPlayerExact(String name);

    public abstract List<IPlayer> getOnlinePlayers();

    public abstract Logger getLogger();

    public abstract int scheduleSyncRepeatingTask(Runnable func, long l, long l2);

    public abstract void cancelTask(int taskid);

    public abstract int scheduleSyncDelayedTask(Runnable task, int i);

    public abstract void setupHooks();

    public abstract Economy getEconomy();

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @return Amount currently held in players account
     */
    public abstract double getBalance(IPlayer player);

    public abstract EconomyResponse withdrawPlayer(IPlayer player, double price);

    public abstract EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid);

    public abstract PlotWorldEdit getPlotWorldEdit();

    public boolean getUsinglwc() {
        return usinglwc;
    }

    public void setUsinglwc(boolean usinglwc) {
        this.usinglwc = usinglwc;
    }

    /**
     * Gets the world with the given name.
     *
     * @param worldName the name of the world
     * @return a world with the given name, or null if none exists
     */
    public abstract IWorld getWorld(String worldName);

    public abstract void setupCommands();

    public abstract void unHook();

    public abstract void setupListeners();

    public abstract void runTaskAsynchronously(Runnable runnable);

    public abstract IBiome getBiome(String name);

    public abstract IEventFactory getEventFactory();

    public abstract InputStream getResource(String path);

    public abstract String getDataFolder();

    public abstract void reloadConfig();

    public abstract IConfigSection getConfig();

    public abstract IConfigSection getConfig(String file);

    public abstract void saveResource(String fileName, boolean replace);

    public abstract boolean addMultiverseWorld(String worldname, String environment, String seed, String generator);

    public abstract List<String> getBiomes();

    /**
     * Get all Existing Plotworlds.
     * @return all plotworlds on the server
     */
    public abstract List<IWorld> getWorlds();

    public abstract boolean createPlotWorld(String worldname, String generator, Map<String, String> args);

    public abstract IMaterial getMaterial(String string);

    public abstract IConfigSection loadDefaultConfig(String string);

}