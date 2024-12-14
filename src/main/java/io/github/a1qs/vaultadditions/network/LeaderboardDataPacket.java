package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.client.menu.LeaderboardMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class LeaderboardDataPacket {
    private final Map<UUID, Integer> leaderboard;

    public LeaderboardDataPacket(Map<UUID, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public static void encode(LeaderboardDataPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.leaderboard.size());
        msg.leaderboard.forEach((uuid, count) -> {
            buffer.writeUUID(uuid);
            buffer.writeInt(count);
        });
    }

    public static LeaderboardDataPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<UUID, Integer> leaderboard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            UUID uuid = buffer.readUUID();
            int score = buffer.readInt();
            leaderboard.put(uuid, score);
        }
        return new LeaderboardDataPacket(leaderboard);
    }

    public static void handle(LeaderboardDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new LeaderboardMenu(new TranslatableComponent("screen." + VaultAdditions.MOD_ID + ".leaderboard"), msg.leaderboard));
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
