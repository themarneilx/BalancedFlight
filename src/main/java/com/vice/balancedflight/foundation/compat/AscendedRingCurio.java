package com.vice.balancedflight.foundation.compat;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class AscendedRingCurio
{
    public static boolean HasAscendedRing(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(BalancedFlight.ASCENDED_FLIGHT_RING.get()))
                .orElse(false);
    }
}
