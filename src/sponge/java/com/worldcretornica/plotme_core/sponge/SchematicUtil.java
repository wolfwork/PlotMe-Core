package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.AbstractSchematicUtil;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.schematic.Schematic;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class SchematicUtil extends AbstractSchematicUtil {

    private final PlotMe_Sponge plugin;

    public SchematicUtil(PlotMe_Sponge instance) {
        plugin = instance;
    }

    @Override
    public void pasteSchematic(ILocation loc, Schematic schem) {

    }

    @Nullable
    @Override
    public Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {
        return null;
    }

    @Nullable
    @Override
    public Schematic createCompiledSchematic(ILocation loc1, ILocation loc2) {
        return null;
    }

    @Override
    public void saveCompiledSchematic(Schematic schem, String name) {

    }

    @Nullable
    @Override
    public Schematic loadCompiledSchematic(String name) {
        return null;
    }
}