package com.vice.balancedflight.foundation.network;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;

public record CustomNetworkMessage(UUID player, String message) implements CustomPacketPayload
{
    public static final Type<CustomNetworkMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(BalancedFlight.MODID, "main_network")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomNetworkMessage> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            CustomNetworkMessage::player,
            ByteBufCodecs.STRING_UTF8,
            CustomNetworkMessage::message,
            CustomNetworkMessage::new
    );

    public static void handle(CustomNetworkMessage payload, IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if ("FIRE_ROCKET".equals(payload.message()))
            {
                FireRocket(payload.player());
            }
        });
    }

    public static void Send(Level world, Player player, String message) {
        if (world.isClientSide) {
            PacketDistributor.sendToServer(new CustomNetworkMessage(player.getGameProfile().getId(), message));
        }
        if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new CustomNetworkMessage(player.getGameProfile().getId(), message));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void FireRocket(UUID uuid)
    {
        Player player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);

        if (player == null)
            return;

        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 64);
        player.level().addFreshEntity(new FireworkRocketEntity(player.level(), itemstack, player));
    }
}
