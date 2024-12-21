package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.network.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(VaultAdditions.MOD_ID, "network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static int ID = 0;

    public static void initialize() {
        CHANNEL.registerMessage(nextId(), ServerboundOpenPowersMessage.class, ServerboundOpenPowersMessage::encode, ServerboundOpenPowersMessage::decode, ServerboundOpenPowersMessage::handle);
        CHANNEL.registerMessage(nextId(), KnownPowerMessage.class, KnownPowerMessage::encode, KnownPowerMessage::decode, KnownPowerMessage::handle);
        CHANNEL.registerMessage(nextId(), PowerLevelMessage.class, PowerLevelMessage::encode, PowerLevelMessage::decode, PowerLevelMessage::handle);
        CHANNEL.registerMessage(nextId(), PowerPointMessage.class, PowerPointMessage::encode, PowerPointMessage::decode, PowerPointMessage::handle);
        CHANNEL.registerMessage(nextId(), StatueSelectItemMessage.class, StatueSelectItemMessage::encode, StatueSelectItemMessage::decode, StatueSelectItemMessage::handle);
        CHANNEL.registerMessage(nextId(), RenameStatueMessage.class, RenameStatueMessage::encode, RenameStatueMessage::decode, RenameStatueMessage::handle);
        CHANNEL.registerMessage(nextId(), LeaderboardRequestMessage.class, LeaderboardRequestMessage::encode, LeaderboardRequestMessage::decode, LeaderboardRequestMessage::handle);
        CHANNEL.registerMessage(nextId(), LeaderboardDataMessage.class, LeaderboardDataMessage::encode, LeaderboardDataMessage::decode, LeaderboardDataMessage::handle);
        CHANNEL.registerMessage(nextId(), BladeFrenzyParticleMessage.class, BladeFrenzyParticleMessage::encode, BladeFrenzyParticleMessage::decode, BladeFrenzyParticleMessage::handle);

    }

    public static int nextId() {
        return ID++;
    }

    public static <T> void sendToServer(T message) {
        CHANNEL.sendToServer(message);
    }

    public static <T> void sendToClient(T message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
