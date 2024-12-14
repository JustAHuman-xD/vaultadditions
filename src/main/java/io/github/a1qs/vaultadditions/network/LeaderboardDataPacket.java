package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.client.menu.LeaderboardMenu;
import io.github.a1qs.vaultadditions.events.VaultAdditionsEvent;
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
    private final VaultAdditionsEvent optionalEvent;

    public LeaderboardDataPacket(Map<UUID, Integer> leaderboard, String nextScheduledEvent, VaultAdditionsEvent optionalEvent) {
        this.leaderboard = leaderboard;
        this.nextScheduledEvent = nextScheduledEvent;
        this.optionalEvent = optionalEvent;
    }

    public static void encode(LeaderboardDataPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.leaderboard.size());
        msg.leaderboard.forEach((uuid, count) -> {
            buffer.writeUUID(uuid);
            buffer.writeInt(count);
        });

        buffer.writeUtf(msg.nextScheduledEvent);

        if (msg.optionalEvent != null) {
            buffer.writeBoolean(true); // Flag to indicate presence
            buffer.writeInt(msg.optionalEvent.getConfigIndex());
            buffer.writeInt(msg.optionalEvent.getRequiredCrystals());
            buffer.writeInt(msg.optionalEvent.getCrystalsSubmitted());
        } else {
            buffer.writeBoolean(false); // No event present
        }
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

        VaultAdditionsEvent optionalEvent = null;
        if (buffer.readBoolean()) { // Check if event is present
            int configIndex = buffer.readInt();
            int requiredCrystals = buffer.readInt();
            int crystalsSubmitted = buffer.readInt();
            optionalEvent = new VaultAdditionsEvent(configIndex, requiredCrystals, crystalsSubmitted);
        }

        return new LeaderboardDataPacket(leaderboard, nextEvent, optionalEvent);
    }

    public static void handle(LeaderboardDataPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(
                    new LeaderboardMenu(
                            new TranslatableComponent("screen." + VaultAdditions.MOD_ID + ".leaderboard"),
                            msg.leaderboard,
                            msg.nextScheduledEvent,
                            msg.optionalEvent
                    )
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
