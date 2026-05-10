package com.vice.balancedflight.content.angelRing;

import com.vice.balancedflight.foundation.compat.AscendedRingCurio;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class FlightRing extends Item implements ICurioItem {

    public FlightRing(Item.Properties props) { super(props); }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !AscendedRingCurio.HasAscendedRing(slotContext.entity());
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA.value(), 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(Component.literal("An incredibly dense golden ring. Despite its weight, it allows you to fly anywhere (Angel Ring).").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("Allows both creative and enhanced Elytra flight.").withStyle(ChatFormatting.WHITE));
    }
}
