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

public class LeaderboardDataMessage {
    private final Map<UUID, Integer> leaderboard;
    private final String nextScheduledEvent;
    private final VaultAdditionsEvent optionalEvent;
    private final long optionalEventDuration;

    public LeaderboardDataMessage(Map<UUID, Integer> leaderboard, String nextScheduledEvent, VaultAdditionsEvent optionalEvent, long optionalEventDuration) {
        this.leaderboard = leaderboard;
        this.nextScheduledEvent = nextScheduledEvent;
        this.optionalEvent = optionalEvent;
        this.optionalEventDuration = optionalEventDuration;
    }

    public static void encode(LeaderboardDataMessage msg, FriendlyByteBuf buffer) {
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
            buffer.writeLong(msg.optionalEventDuration);
        } else {
            buffer.writeBoolean(false); // No event present
        }
    }

    public static LeaderboardDataMessage decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Map<UUID, Integer> leaderboard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            UUID uuid = buffer.readUUID();
            int score = buffer.readInt();
            leaderboard.put(uuid, score);
        }

        String nextEvent = buffer.readUtf();
        long eventDuration = -1L;
        VaultAdditionsEvent optionalEvent = null;
        if (buffer.readBoolean()) { // Check if event is present
            int configIndex = buffer.readInt();
            int requiredCrystals = buffer.readInt();
            int crystalsSubmitted = buffer.readInt();
            optionalEvent = new VaultAdditionsEvent(configIndex, requiredCrystals, crystalsSubmitted);
            eventDuration = buffer.readLong();
        }

        return new LeaderboardDataMessage(leaderboard, nextEvent, optionalEvent, eventDuration);
    }

    public static void handle(LeaderboardDataMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(
                    new LeaderboardMenu(
                            new TranslatableComponent("screen." + VaultAdditions.MOD_ID + ".leaderboard"),
                            msg.leaderboard,
                            msg.nextScheduledEvent,
                            msg.optionalEvent,
                            msg.optionalEventDuration
                    )
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
