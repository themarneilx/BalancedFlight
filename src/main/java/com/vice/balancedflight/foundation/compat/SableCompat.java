package com.vice.balancedflight.foundation.compat;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.ServiceConfigurationError;

public final class SableCompat
{
    private static volatile CompanionApi api = resolveApi();

    private SableCompat() {}

    public static boolean isLoaded() {
        return api != null;
    }

    public static boolean allowsAnchorFlightInLevel(Player player) {
        Level level = player.level();
        if (level.dimension() == Level.OVERWORLD)
            return true;

        CompanionApi current = api;
        if (current == null)
            return false;

        BlockPos pos = player.blockPosition();
        try {
            return (boolean) current.isInPlotGrid().invoke(current.instance(), level,
                    SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        } catch (ReflectiveOperationException | LinkageError | ClassCastException exception) {
            disable(current, exception);
            return false;
        }
    }

    public static double horizontalDistanceSqr(Level level, Vec3 from, BlockPos to) {
        Vec3 target = Vec3.atLowerCornerOf(to);
        CompanionApi current = api;
        if (current != null) {
            try {
                // Both positions must use world space, including players standing in a plot.
                from = (Vec3) current.projectOutOfSubLevel().invoke(current.instance(), level, from);
                target = (Vec3) current.projectOutOfSubLevel().invoke(current.instance(), level, target);
            } catch (ReflectiveOperationException | LinkageError | ClassCastException exception) {
                disable(current, exception);
                return Double.POSITIVE_INFINITY;
            }
        }

        double dx = from.x - target.x;
        double dz = from.z - target.z;
        return dx * dx + dz * dz;
    }

    private static CompanionApi resolveApi() {
        if (!ExternalMods.SABLE.isLoaded())
            return null;

        try {
            Class<?> companion = Class.forName("dev.ryanhcode.sable.companion.SableCompanion");
            return new CompanionApi(companion.getField("INSTANCE").get(null),
                    companion.getMethod("projectOutOfSubLevel", Level.class, Position.class),
                    companion.getMethod("isInPlotGrid", Level.class, int.class, int.class));
        } catch (ReflectiveOperationException | LinkageError | ServiceConfigurationError exception) {
            BalancedFlight.LOGGER.warn("Sable compatibility is unavailable; using normal anchor range checks", exception);
            return null;
        }
    }

    private static synchronized void disable(CompanionApi failedApi, Throwable exception) {
        if (api == failedApi) {
            api = null;
            BalancedFlight.LOGGER.warn("Disabling Sable compatibility after a Companion API failure", exception);
        }
    }

    private record CompanionApi(Object instance, Method projectOutOfSubLevel, Method isInPlotGrid) {}
}
