package com.vice.balancedflight.foundation.events;

import com.vice.balancedflight.content.flightAnchor.FlightController;
import com.vice.balancedflight.foundation.compat.AscendedRingCurio;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

@EventBusSubscriber
public class CommonEvents
{
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        Player player = event.getEntity();
        FlightController.tick(player);
    }


    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (Objects.equals(event.getSource().getMsgId(), "flyIntoWall") && BalancedFlightConfig.disableElytraDamage.get()) {
            if (event.getEntity() instanceof Player player) {
                if (FlightController.AllowedFlightModes(player, true) != FlightController.FlightMode.None)
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        if (Objects.equals(event.getSource().getMsgId(), "fall")) {
            if (event.getEntity() instanceof Player player) {
                if (AscendedRingCurio.HasAscendedRing(player) && BalancedFlightConfig.disableFallDamageWhenWearingRing.get())
                    event.setCanceled(true);

                if (BalancedFlightConfig.disableFallDamageNearAnchor.get() && FlightController.AllowedFlightModes(player, false) != FlightController.FlightMode.None)
                    event.setCanceled(true);
            }
        }
    }
}
