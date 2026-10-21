package net.minecraft.world.phys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;

public class Vec3 implements Position {
    public final double x, y, z;
    public Vec3(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }
    public static Vec3 atLowerCornerOf(BlockPos pos) { return new Vec3(pos.getX(), pos.getY(), pos.getZ()); }
}
