package dev.ryanhcode.sable.companion;

import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface SableCompanion {
    SableCompanion INSTANCE = create();
    Vec3 projectOutOfSubLevel(Level level, Position position);
    boolean isInPlotGrid(Level level, int chunkX, int chunkZ);

    private static SableCompanion create() {
        if (Boolean.getBoolean("test.sable.serviceFailure"))
            throw new java.util.ServiceConfigurationError("Missing Sable service provider");
        if (Boolean.getBoolean("test.sable.missing"))
            throw new NoClassDefFoundError("Missing Companion implementation");
        return new SableCompanion() {
            public Vec3 projectOutOfSubLevel(Level level, Position pos) {
                if (Boolean.getBoolean("test.sable.failure"))
                    throw new IllegalStateException("Projection failed");
                return new Vec3(pos.x() >= 1600 ? pos.x() - 1600 : pos.x(), pos.y(), pos.z());
            }
            public boolean isInPlotGrid(Level level, int chunkX, int chunkZ) {
                return chunkX == 100 && chunkZ == -1;
            }
        };
    }
}
