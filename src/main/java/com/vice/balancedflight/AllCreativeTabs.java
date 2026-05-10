package com.vice.balancedflight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashSet;
import java.util.Set;

public class AllCreativeTabs
{
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BalancedFlight.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = TAB_REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.balancedflight.base"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(BalancedFlight.FLIGHT_ANCHOR_BLOCK::asStack)
                    .displayItems(((pParameters, pOutput) -> {
                        Set<Item> displayedItems = new LinkedHashSet<>();

                        for (var item : BalancedFlight.CREATE_REGISTRATE.getAll(Registries.ITEM)) {
                            Item registeredItem = item.get();
                            if (displayedItems.add(registeredItem))
                                pOutput.accept(new ItemStack(registeredItem));
                        }

                        for (var block : BalancedFlight.CREATE_REGISTRATE.getAll(Registries.BLOCK)) {
                            Item blockItem = block.get().asItem();
                            if (blockItem != Items.AIR && displayedItems.add(blockItem))
                                pOutput.accept(new ItemStack(blockItem));
                        }
                    }))
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }
}
