package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.world.World;

public class SpongeBlockLoc implements IBlock {

    private final BlockLoc block;

    public SpongeBlockLoc(BlockLoc block) {
        this.block = block;
    }

    @Override
    public ILocation getLocation() {
        return new SpongeLocation(block.getLocation());
    }

    @Override
    public IWorld getWorld() {
        return new SpongeWorld((World) block.getExtent());
    }

    @Override
    public int getX() {
        return block.getX();
    }

    @Override
    public int getY() {
        return block.getY();
    }

    @Override
    public int getZ() {
        return block.getZ();
    }

    @Override
    public int getTypeId() {
        return 0; //block.getType().getId();
    }

    @Override
    public String getBiome() {
        return block.getExtent().getBiome(block.getPosition().toVector2()).getName();
    }

    @Override
    public boolean setTypeIdAndData(short id, byte data, boolean applyPhysics) {
        return false;
    }

    @Override
    public byte getData() {
        return 0;
    }

    @Override
    public void setTypeId(int id, boolean applyPhysics) {
    }
}
