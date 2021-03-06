package com.boydti.fawe.object.extent;

import com.sk89q.worldedit.MutableBlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.BlockRegistry;

public class TransformExtent extends BlockTransformExtent {

    private final MutableBlockVector mutable = new MutableBlockVector();
    private Vector min;
    private int maxy;

    public TransformExtent(Extent parent, BlockRegistry registry) {
        super(parent, registry);
        this.maxy = parent.getMaximumPoint().getBlockY();
    }

    @Override
    public ResettableExtent setExtent(Extent extent) {
        min = null;
        maxy = extent.getMaximumPoint().getBlockY();
        return super.setExtent(extent);
    }

    public void setOrigin(Vector pos) {
        this.min = pos;
    }

    public Vector getPos(Vector pos) {
        if (min == null) {
            min = new Vector(pos);
        }
        mutable.mutX(((pos.getX() - min.getX())));
        mutable.mutY(((pos.getY() - min.getY())));
        mutable.mutZ(((pos.getZ() - min.getZ())));
        Vector tmp = getTransform().apply(mutable);
        tmp.mutX((tmp.getX() + min.getX()));
        tmp.mutY((tmp.getY() + min.getY()));
        tmp.mutZ((tmp.getZ() + min.getZ()));
        return tmp;
    }

    public Vector getPos(int x, int y, int z) {
        if (min == null) {
            min = new Vector(x, y, z);
        }
        mutable.mutX(((x - min.getX())));
        mutable.mutY(((y - min.getY())));
        mutable.mutZ(((z - min.getZ())));
        Vector tmp = getTransform().apply(mutable);
        tmp.mutX((tmp.getX() + min.getX()));
        tmp.mutY((tmp.getY() + min.getY()));
        tmp.mutZ((tmp.getZ() + min.getZ()));
        return tmp;
    }

    @Override
    public BaseBlock getLazyBlock(int x, int y, int z) {
        return transformFast(super.getLazyBlock(getPos(x, y, z)));
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        return transformFast(super.getLazyBlock(getPos(position)));
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        return transformFast(super.getBlock(getPos(position)));
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        mutable.mutX(position.getBlockX());
        mutable.mutZ(position.getBlockZ());
        mutable.mutY(0);
        return super.getBiome(getPos(mutable).toVector2D());
    }

    @Override
    public boolean setBlock(int x, int y, int z, BaseBlock block) throws WorldEditException {
        return super.setBlock(getPos(x, y, z), transformFastInverse(block));
    }


    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        return super.setBlock(getPos(location), transformFastInverse(block));
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        mutable.mutX(position.getBlockX());
        mutable.mutZ(position.getBlockZ());
        mutable.mutY(0);
        return super.setBiome(getPos(mutable).toVector2D(), biome);
    }
}
