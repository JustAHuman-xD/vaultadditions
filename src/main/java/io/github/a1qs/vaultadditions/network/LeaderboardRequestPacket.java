package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PowerCrystalData;
import io.github.a1qs.vaultadditions.init.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LeaderboardRequestPacket {
    public LeaderboardRequestPacket() {}

    public static void encode(LeaderboardRequestPacket msg, FriendlyByteBuf buffer) {}

    public static LeaderboardRequestPacket decode(FriendlyByteBuf buffer) {
        return new LeaderboardRequestPacket();
    }

    public static void handle(LeaderboardRequestPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player != null) {
            PowerCrystalData powerData = PowerCrystalData.getServer();
            EventData eventData = EventData.getServer();
            ModNetwork.sendToClient(new LeaderboardDataPacket(powerData.getPlayerContributionsMap(), eventData.getNextScheduledEvent()), player);
        }
        contextSupplier.get().setPacketHandled(true);
    }
}
