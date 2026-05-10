package com.vice.balancedflight.foundation.events;

import com.vice.balancedflight.foundation.network.BalancedFlightNetwork;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModEvents
{
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        BalancedFlightNetwork.registerMessages(event);
    }
}
