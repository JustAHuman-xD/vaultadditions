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
    private final String nextScheduledEvent;

    public LeaderboardDataPacket(Map<UUID, Integer> leaderboard, String nextScheduledEvent) {
        this.leaderboard = leaderboard;
        this.nextScheduledEvent = nextScheduledEvent;
    }

    public static void encode(LeaderboardDataPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.leaderboard.size());
        msg.leaderboard.forEach((uuid, count) -> {
            buffer.writeUUID(uuid);
            buffer.writeInt(count);
        });

        buffer.writeUtf(msg.nextScheduledEvent);
    }

    public static LeaderboardDataPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<UUID, Integer> leaderboard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            UUID uuid = buffer.readUUID();
            int score = buffer.readInt();
            leaderboard.put(uuid, score);
        }

        String nextEvent = buffer.readUtf();
        return new LeaderboardDataPacket(leaderboard, nextEvent);
    }

    public static void handle(LeaderboardDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(
                    new LeaderboardMenu(
                            new TranslatableComponent("screen." + VaultAdditions.MOD_ID + ".leaderboard"),
                            msg.leaderboard,
                            msg.nextScheduledEvent
                    )
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
