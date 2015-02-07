package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.*;
import com.worldcretornica.plotme_core.utils.Util;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    public static final String CAPTION_FILE = "captions.yml";

    public static final String WORLDS_CONFIG_SECTION = "worlds";
    //Bridge
    private final IServerBridge serverBridge;
    private final AbstractSchematicUtil schematicutil;
    private HashMap<String, IPlotMe_GeneratorManager> managers;
    private IWorld worldcurrentlyprocessingexpired;
    private short counterExpired;
    //Spool stuff
    private ConcurrentLinkedQueue<PlotToClear> plotsToClear;
    //Global variables
    private SqlManager sqlManager;
    private Util util;

    public PlotMe_Core(IServerBridge serverObjectBuilder, AbstractSchematicUtil schematicutil) {
        this.serverBridge = serverObjectBuilder;
        this.schematicutil = schematicutil;
        managers = new HashMap<>();
    }

    public IPlotMe_GeneratorManager getGenManager(String name) {
        return managers.get(name.toLowerCase());
    }
    
    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }

    public void disable() {
        getSqlManager().closeConnection();
        serverBridge.unHook();
        PlotMeCoreManager.getInstance().setPlayersIgnoringWELimit(null);
        setWorldCurrentlyProcessingExpired(null);
        plotsToClear.clear();
        plotsToClear = null;
        managers.clear();
        managers = null;
    }

    public void enable() {
        PlotMeCoreManager.getInstance().setPlugin(this);
        setupMySQL();
        setupConfig();
        setupDefaultCaptions();        
        serverBridge.setupCommands();
        setUtil(new Util(this));
        serverBridge.setupHooks();
        serverBridge.setupListeners();
        setupClearSpools();
        getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        serverBridge.reloadConfig();
        setupConfig();
        reloadCaptionConfig();
        setupDefaultCaptions();
        setupMySQL();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();
        
        for (String worldname : managers.keySet()) {
            setupWorld(worldname.toLowerCase());
        }
    }

    public Logger getLogger() {
        return serverBridge.getLogger();
    }

    /*private void setupWorlds() {
        IConfigSection worldsCS = serverBridge.getConfig().getConfigurationSection(WORLDS_CONFIG_SECTION);
        for (String world : worldsCS.getKeys(false)) {
            String worldName = world.toLowerCase();
            if (getGenManager(worldName) == null) {
                getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", world);
                getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", world);
            } else {
                PlotMapInfo pmi = new PlotMapInfo(this, worldName);
                //Lets just hide a bit of code to clean up the config in here.
                IConfigSection config = getServerBridge().loadDefaultConfig("worlds." + world);
                config.set("BottomBlockId", null);
                config.set("AutoLinkPlots", null);
                plotMeCoreManager.addPlotMap(worldName, pmi);
            }
        }
        if (getPlotMeCoreManager().getPlotMaps().isEmpty()) {
            getLogger().severe("Uh oh. There are no plotworlds setup.");
            getLogger().severe("Is that a mistake? Try making sure you setup PlotMe Correctly PlotMe to stay safe.");
        }
    }*/

    private void setupConfig() {
        // Get the config we will be working with
        IConfigSection config = serverBridge.getConfig();
        config.set("allowToDeny", null);
        // If no world exists add config for a world
        //if (!config.contains("worlds") || config.contains("worlds") && config.getConfigurationSection("worlds").getKeys(false).isEmpty()) {
        if (!(config.contains(WORLDS_CONFIG_SECTION) && !config.getConfigurationSection(WORLDS_CONFIG_SECTION).getKeys(false).isEmpty())) {
            new PlotMapInfo(this, "plotworld");
        }
        // Do any config validation
        if (config.getInt("NbClearSpools") > 100) {
            getLogger().warning("Having more than 100 clear spools seems drastic, changing to 100");
            config.set("NbClearSpools", 100);
        }

        // Copy new values over
        config.copyDefaults(true);
        config.set("Language", null);
        config.set("language", null);
        config.saveConfig();
    }

    private void setupWorld(String worldname) {
        if (getGenManager(worldname) == null) {
            getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", worldname);
            getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", worldname);
        } else {
            PlotMapInfo pmi = new PlotMapInfo(this, worldname);
            //Lets just hide a bit of code to clean up the config in here.
            IConfigSection config = getServerBridge().loadDefaultConfig("worlds." + worldname.toLowerCase());
            config.set("BottomBlockId", null);
            config.set("AutoLinkPlots", null);
            config.saveConfig();
            PlotMeCoreManager.getInstance().addPlotMap(worldname, pmi);
        }

        if (PlotMeCoreManager.getInstance().getPlotMaps().isEmpty()) {
            getLogger().severe("Uh oh. There are no plotworlds setup.");
            getLogger().severe("Is that a mistake? Try making sure you setup PlotMe Correctly PlotMe to stay safe.");
        }
    }

    public IConfigSection getCaptionConfig() {
        return serverBridge.getConfig(CAPTION_FILE);
    }

    public void reloadCaptionConfig() {
        serverBridge.getConfig(CAPTION_FILE).reloadConfig();
    }

    private void setupDefaultCaptions() {
        //Changing Captions File Name
        String pluginsFolder = serverBridge.getDataFolder();
        File coreFolder = new File(pluginsFolder);
        File newCaptionFile = new File(coreFolder, CAPTION_FILE);
        for (String plotMeFiles : coreFolder.list()) {
            if (plotMeFiles.startsWith("caption")) {
                if (CAPTION_FILE.equals(plotMeFiles)) {
                    break;
                } else {
                    File oldCaptionFile = new File(coreFolder, plotMeFiles);
                    if (oldCaptionFile.renameTo(newCaptionFile)) {
                        getLogger().info("Renamed Caption File to captions.yml");
                        if (oldCaptionFile.delete()) {
                            getLogger().info("Deleted old caption file.");
                        } else {
                            getLogger().warning("Failed to delete old caption file. ");
                        }
                    }
                }
            }
        }
        if (!newCaptionFile.exists()) {
            getServerBridge().saveResource(CAPTION_FILE, true);
        }
        serverBridge.getConfig(CAPTION_FILE).set("MsgCannotDenyOwner", "You can''t deny the owner of the plot.");
    }

    /**
     * Setup MySQL Database
     */
    private void setupMySQL() {
        IConfigSection config = serverBridge.getConfig();

        String mySQLconn = config.getString("mySQLconn", "jdbc:mysql://localhost:3306/minecraft");
        String mySQLuname = config.getString("mySQLuname", "root");
        String mySQLpass = config.getString("mySQLpass", "password");

        setSqlManager(new SqlManager(this, mySQLuname, mySQLpass, mySQLconn));
    }

    private void setupClearSpools() {
        plotsToClear = new ConcurrentLinkedQueue<>();
    }

    public void addManager(String world, IPlotMe_GeneratorManager manager) {
        managers.put(world.toLowerCase(), manager);
        setupWorld(world.toLowerCase());
    }

    public IPlotMe_GeneratorManager removeManager(String world) {
        return managers.remove(world);
    }

    public void scheduleTask(Runnable task) {
        getLogger().info(util.C("MsgStartDeleteSession"));

        for (int ctr = 0; ctr < 10; ctr++) {
            serverBridge.scheduleSyncDelayedTask(task, ctr * 100);
        }
    }

    public IWorld getWorldCurrentlyProcessingExpired() {
        return worldcurrentlyprocessingexpired;
    }

    public void setWorldCurrentlyProcessingExpired(IWorld worldcurrentlyprocessingexpired) {
        this.worldcurrentlyprocessingexpired = worldcurrentlyprocessingexpired;
    }

    public short getCounterExpired() {
        return counterExpired;
    }

    public void setCounterExpired(short counterExpired) {
        this.counterExpired = counterExpired;
    }

    public void addPlotToClear(PlotToClear plotToClear) {
        plotsToClear.offer(plotToClear);
        getLogger().info("plot to clear add " + plotToClear.getPlotId());
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        pms.setTaskId(serverBridge.scheduleSyncRepeatingTask(pms, 0L, 60L));
    }

    public void removePlotToClear(PlotToClear plotToClear, int taskId) {
        plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskId);
        getLogger().info("removed taskid " + taskId);
    }

    public PlotToClear getPlotLocked(String world, String id) {
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[plotsToClear.size()])) {
            if (ptc.getWorld().equalsIgnoreCase(world) && ptc.getPlotId().equalsIgnoreCase(id)) {
                return ptc;
            }
        }

        return null;
    }

    public IServerBridge getServerBridge() {
        return serverBridge;
    }

    public SqlManager getSqlManager() {
        return sqlManager;
    }

    private void setSqlManager(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public Util getUtil() {
        return util;
    }

    private void setUtil(Util util) {
        this.util = util;
    }
    
    @Deprecated
    public PlotMeCoreManager getPlotMeCoreManager() {
        return PlotMeCoreManager.getInstance();
    }
}
