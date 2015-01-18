package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class BukkitEntity implements IEntity {

    private final Entity entity;

    public BukkitEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ILocation getLocation() {
        return new BukkitLocation(entity.getLocation());
    }

    @Override
    public void setLocation(ILocation location) {
        BukkitLocation loc = null;
        if (location instanceof BukkitLocation) {
            loc = (BukkitLocation) location;
        }
        if (loc != null) {
            entity.teleport(loc.getLocation());
        }
    }

    /**
     * Get the world the entity is currently in.
     *
     * @return the world the entity is in
     */
    @Override
    public IWorld getWorld() {
        return new BukkitWorld(entity.getWorld());
    }

    @Override
    public void remove() {
        entity.remove();
    }

    /**
     * Gets the name of the actor
     *
     * @return name of the actor
     */
    @Override
    public String getName() {
        return entity.getName();
    }

    /**
     * Gets the UUID of the actor
     *
     * @return UUID of the actor
     */
    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

}
