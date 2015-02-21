package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IConfigSection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlotMapInfo {

    private final PlotMe_Core plugin;

    private final ConcurrentHashMap<PlotId, Plot> plots;
    private final String world;
    private final IConfigSection config;

    public PlotMapInfo(PlotMe_Core instance, String world) {
        plugin = instance;
        this.world = world.toLowerCase();
        config = plugin.getServerBridge().loadDefaultConfig("worlds." + this.world);
        plots = new ConcurrentHashMap<>(1000, 0.75f, 5);
        if (plugin.getServerBridge().getConfig().getBoolean("LoadAllPlotsOnStart", false)) {
            plugin.getSqlManager().loadPlotsAsynchronously(world);
        }
    }

    public int getNbPlots() {
        return plots.size();
    }

    public Plot getPlot(PlotId id) {
        if (id == null) {
            return null;
        }
        if (!plots.containsKey(id)) {
            Plot plot = plugin.getSqlManager().getPlot(world, id);
            if (plot == null) {
                return null;
            }

            plots.put(id, plot);
        }

        return plots.get(id);
    }

    public ConcurrentHashMap<PlotId, Plot> getLoadedPlots() {
        return plots;
    }

    public void addPlot(PlotId id, Plot plot) {
        plots.putIfAbsent(id, plot);
    }

    public void removePlot(PlotId id) {
        plots.remove(id);
    }

    private List<Integer> getProtectedBlocks() {
        return config.getIntegerList("ProtectedBlocks");
    }

    public boolean isProtectedBlock(int blockId) {
        return getProtectedBlocks().contains(blockId);
    }

    private List<String> getPreventedItems() {
        return config.getStringList("PreventedItems");
    }

    public boolean isPreventedItem(String itemId) {
        return getPreventedItems().contains(itemId);
    }

    public int getPlotAutoLimit() {
        return config.getInt("PlotAutoLimit");
    }

    public void setPlotAutoLimit(int plotAutoLimit) {
        config.set("PlotAutoLimit", plotAutoLimit);
        config.saveConfig();
    }

    public int getDaysToExpiration() {
        return config.getInt("DaysToExpiration");
    }

    public void setDaysToExpiration(int daysToExpiration) {
        config.set("DaysToExpiration", daysToExpiration);
        config.saveConfig();
    }

    private IConfigSection getEconomySection() {
        return config.getConfigurationSection("economy");
    }

    public boolean isUseEconomy() {
        return getEconomySection().getBoolean("UseEconomy");
    }

    public void setUseEconomy(boolean useEconomy) {
        getEconomySection().set("UseEconomy", useEconomy);
        config.saveConfig();
    }

    public boolean canUseProjectiles() {
        return config.getBoolean("Projectiles");
    }

    public void setUseProjectiles(boolean allowed) {
        config.set("Projectiles", allowed);
        config.saveConfig();
    }

    public boolean isCanPutOnSale() {
        return getEconomySection().getBoolean("CanPutOnSale");
    }

    public void setCanPutOnSale(boolean canPutOnSale) {
        getEconomySection().set("CanPutOnSale", canPutOnSale);
        config.saveConfig();
    }

    public boolean isRefundClaimPriceOnReset() {
        return getEconomySection().getBoolean("RefundClaimPriceOnReset");
    }

    public void setRefundClaimPriceOnReset(boolean refundClaimPriceOnReset) {
        getEconomySection().set("RefundClaimPriceOnReset", refundClaimPriceOnReset);
        config.saveConfig();
    }

    public boolean isRefundClaimPriceOnSetOwner() {
        return getEconomySection().getBoolean("RefundClaimPriceOnSetOwner");
    }

    public void setRefundClaimPriceOnSetOwner(boolean refundClaimPriceOnSetOwner) {
        getEconomySection().set("RefundClaimPriceOnSetOwner", refundClaimPriceOnSetOwner);
        config.saveConfig();
    }

    public double getClaimPrice() {
        return getEconomySection().getDouble("ClaimPrice");
    }

    public void setClaimPrice(double claimPrice) {
        getEconomySection().set("ClaimPrice", claimPrice);
        config.saveConfig();
    }

    public double getClearPrice() {
        return getEconomySection().getDouble("ClearPrice");
    }

    public void setClearPrice(double clearPrice) {
        getEconomySection().set("ClearPrice", clearPrice);
        config.saveConfig();
    }

    public double getAddPlayerPrice() {
        return getEconomySection().getDouble("AddPlayerPrice");
    }

    public void setAddPlayerPrice(double addPlayerPrice) {
        getEconomySection().set("AddPlayerPrice", addPlayerPrice);
        config.saveConfig();
    }

    public double getDenyPlayerPrice() {
        return getEconomySection().getDouble("DenyPlayerPrice");
    }

    public void setDenyPlayerPrice(double denyPlayerPrice) {
        getEconomySection().set("DenyPlayerPrice", denyPlayerPrice);
        config.saveConfig();
    }

    public double getRemovePlayerPrice() {
        return getEconomySection().getDouble("RemovePlayerPrice");
    }

    public void setRemovePlayerPrice(double removePlayerPrice) {
        getEconomySection().set("RemovePlayerPrice", removePlayerPrice);
        config.saveConfig();
    }

    public double getUndenyPlayerPrice() {
        return getEconomySection().getDouble("UndenyPlayerPrice");
    }

    public void setUndenyPlayerPrice(double undenyPlayerPrice) {
        getEconomySection().set("UndenyPlayerPrice", undenyPlayerPrice);
        config.saveConfig();
    }

    public double getPlotHomePrice() {
        return getEconomySection().getDouble("PlotHomePrice");
    }

    public void setPlotHomePrice(double plotHomePrice) {
        getEconomySection().set("PlotHomePrice", plotHomePrice);
        config.saveConfig();
    }

    public double getSellToPlayerPrice() {
        return getEconomySection().getDouble("SellToPlayerPrice");
    }

    public void setSellToPlayerPrice(double sellToPlayerPrice) {
        getEconomySection().set("SellToPlayerPrice", sellToPlayerPrice);
        config.saveConfig();
    }

    public double getBiomeChangePrice() {
        return getEconomySection().getDouble("BiomeChangePrice");
    }

    public void setBiomeChangePrice(double biomeChangePrice) {
        getEconomySection().set("BiomeChangePrice", biomeChangePrice);
        config.saveConfig();
    }

    public double getProtectPrice() {
        return getEconomySection().getDouble("ProtectPrice");
    }

    public void setProtectPrice(double protectPrice) {
        getEconomySection().set("ProtectPrice", protectPrice);
        config.saveConfig();
    }

    public double getDisposePrice() {
        return getEconomySection().getDouble("DisposePrice");
    }

    public void setDisposePrice(double disposePrice) {
        getEconomySection().set("DisposePrice", disposePrice);
        config.saveConfig();
    }

    public boolean isAutoLinkPlots() {
        return config.getBoolean("AutoLinkPlots");
    }

    public void setAutoLinkPlots(boolean autoLinkPlots) {
        config.set("AutoLinkPlots", autoLinkPlots);
        config.saveConfig();
    }

    public boolean isDisableExplosion() {
        return config.getBoolean("DisableExplosion");
    }

    public void setDisableExplosion(boolean disableExplosion) {
        config.set("DisableExplosion", disableExplosion);
        config.saveConfig();
    }

    public boolean isDisableIgnition() {
        return config.getBoolean("DisableIgnition");
    }

    public void setDisableIgnition(boolean disableIgnition) {
        config.set("DisableIgnition", disableIgnition);
        config.saveConfig();
    }

    public boolean isUseProgressiveClear() {
        return config.getBoolean("UseProgressiveClear");
    }

    public void setUseProgressiveClear(boolean useProgressiveClear) {
        config.set("UseProgressiveClear", useProgressiveClear);
        config.saveConfig();
    }
}
