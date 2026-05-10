package com.vice.balancedflight;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class BalancedFlightClient
{
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(BalancedFlightClient::clientInit);
        modEventBus.addListener(BalancedFlightClient::registerRenderers);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new BalancedFlightPonderPlugin());
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BalancedFlight.FLIGHT_ANCHOR_BLOCK_ENTITY.get(), AllGeckoRenderers.FlightAnchorGeckoRenderer.TileRenderer::apply);
    }
}
