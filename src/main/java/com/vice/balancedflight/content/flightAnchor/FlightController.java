package com.vice.balancedflight.content.flightAnchor;

import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.foundation.compat.AscendedRingCurio;
import com.vice.balancedflight.foundation.compat.SableCompat;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.WeakHashMap;

public class FlightController
{
    private static final String FLIGHT_GRANTED = "balancedflight:flight_granted";
    private static final Map<Player, MobEffectInstance> slowFallingEffects = new WeakHashMap<>();

    public static void tick(Player player)
    {
        // Client-only effects can linger at 0:00 because removal is synchronized by the server.
        if (player.level().isClientSide)
            return;

        if (player.isCreative() || player.isSpectator()) {
            player.getPersistentData().remove(FLIGHT_GRANTED);
            removeOwnSlowFalling(player);
            return;
        }

        if (!BalancedFlightConfig.enableSlowFallingOnFlightLoss.get())
            removeOwnSlowFalling(player);

        FlightMode allowed = AllowedFlightModes(player, false);

        switch (allowed)
        {
            case None, Elytra -> {
                if (player.getPersistentData().getBoolean(FLIGHT_GRANTED))
                {
                    player.getPersistentData().remove(FLIGHT_GRANTED);
                    boolean wasFlying = player.getAbilities().flying;
                    stopFlying(player);
                    if (wasFlying && BalancedFlightConfig.enableSlowFallingOnFlightLoss.get()
                            && !player.hasEffect(MobEffects.SLOW_FALLING)) {
                        MobEffectInstance effect = new MobEffectInstance(MobEffects.SLOW_FALLING, 200);
                        if (player.addEffect(effect))
                            slowFallingEffects.put(player, effect);
                    }
                }
            }
            case Creative, Both -> {
                removeOwnSlowFalling(player);
                if (!player.getAbilities().mayfly) {
                    startFlying(player);
                    player.getPersistentData().putBoolean(FLIGHT_GRANTED, true);
                }
            }
        }
    }

    private static void removeOwnSlowFalling(Player player) {
        MobEffectInstance effect = slowFallingEffects.remove(player);
        if (effect != null && player.getEffect(MobEffects.SLOW_FALLING) == effect
                && effect.getAmplifier() == 0 && effect.endsWithin(200))
            player.removeEffect(MobEffects.SLOW_FALLING);
    }

    public static void startFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    public static void stopFlying(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }

    public static FlightMode AllowedFlightModes(Player player, boolean onlyCareAboutElytra)
    {
        boolean hasAscended = AscendedRingCurio.HasAscendedRing(player);
        boolean CanElytraFly;
        boolean CanCreativeFly;

        if (hasAscended)
        {
            // fetch from config and look up allowed modes from truth table
            CanElytraFly = BalancedFlightConfig.ElytraAscended.get();
            CanCreativeFly = BalancedFlightConfig.CreativeAscended.get();
            FlightMode allowedModes = FlightMode.fromBools(CanElytraFly, CanCreativeFly);

            // if it's just creative, both, or neither, just return
            if (allowedModes != FlightMode.Elytra)
                return allowedModes;

            // if Elytra doesn't give unlimited creative flight,
            // check if Basic tier is allowed to fly.
            if (!CanCreativeFly && BalancedFlightConfig.CreativeAnchor.get())
            {
                if (IsWithinFlightRange(player))
                    return FlightMode.fromBools(CanElytraFly, true);
            }

            return allowedModes;
        }

        // only has basic ring at this point
        CanElytraFly = BalancedFlightConfig.ElytraAnchor.get();
        CanCreativeFly = BalancedFlightConfig.CreativeAnchor.get();

        if (onlyCareAboutElytra && !CanElytraFly)
            return FlightMode.None;

        if (IsWithinFlightRange(player))
            return FlightMode.fromBools(CanElytraFly, CanCreativeFly);
        else
            return FlightMode.None;
    }

    private static boolean IsWithinFlightRange(Player player)
    {
        if (!SableCompat.allowsAnchorFlightInLevel(player))
            return false;

        double anchorDistanceMultiplier = BalancedFlightConfig.anchorDistanceMultiplier.get();

        Level level = player.level();
        Vec3 playerPos = player.position();
        return FlightAnchorEntity.ActiveAnchors.stream()
                .filter(anchor -> anchor.getLevel() == level && !anchor.isRemoved())
                .anyMatch(anchor -> {
                    double radius = anchorDistanceMultiplier * anchor.getSpeed();
                    return SableCompat.horizontalDistanceSqr(level, playerPos, anchor.getBlockPos()) < radius * radius;
                });
    }

    public enum FlightMode {
        None,
        Elytra,
        Creative,
        Both;

        public static FlightMode fromBools(boolean ElytraAllowed, boolean CreativeAllowed) {
            if (ElytraAllowed && CreativeAllowed)
                return Both;

            if (ElytraAllowed)
                return Elytra;

            if (CreativeAllowed)
                return Creative;

            return None;
        }

        public boolean canElytraFly() {
            return this == Elytra || this == Both;
        }

        public boolean canCreativeFly() {
            return this == Creative || this == Both;
        }
    }
}
