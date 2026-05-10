package com.vice.balancedflight.foundation.network;

import com.vice.balancedflight.BalancedFlight;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class BalancedFlightNetwork
{
    public static final String VERSION = "1.0";

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(BalancedFlight.MODID).versioned(VERSION);
        registrar.playBidirectional(CustomNetworkMessage.TYPE, CustomNetworkMessage.STREAM_CODEC, CustomNetworkMessage::handle);
    }
}
