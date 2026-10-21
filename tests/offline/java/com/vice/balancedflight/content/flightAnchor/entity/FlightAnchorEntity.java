package com.vice.balancedflight.content.flightAnchor.entity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FlightAnchorEntity {
    public static final Set<FlightAnchorEntity> ActiveAnchors = ConcurrentHashMap.newKeySet();
    public final Level level;
    public final BlockPos pos;
    public boolean removed;
    public float speed = 32;
    public FlightAnchorEntity(Level level, BlockPos pos) { this.level = level; this.pos = pos; }
    public Level getLevel() { return level; }
    public BlockPos getBlockPos() { return pos; }
    public float getSpeed() { return speed; }
    public boolean isRemoved() { return removed; }
}
