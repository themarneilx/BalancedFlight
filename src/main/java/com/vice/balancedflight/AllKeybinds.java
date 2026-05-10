package com.vice.balancedflight;

import com.mojang.blaze3d.platform.InputConstants;
import com.vice.balancedflight.foundation.events.InputEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.KeyboardInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AllKeybinds
{
    public static final KeyMapping TAKE_OFF_KEY = new KeyMapping("key.take_off",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category." + BalancedFlight.MODID);

    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        event.register(TAKE_OFF_KEY);
    }
}