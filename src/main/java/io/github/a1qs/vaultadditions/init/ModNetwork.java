package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.network.KnownPowerMessage;
import io.github.a1qs.vaultadditions.network.PowerLevelMessage;
import io.github.a1qs.vaultadditions.network.PowerPointMessage;
import io.github.a1qs.vaultadditions.network.ServerboundOpenPowersMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(VaultAdditions.MOD_ID, "network"), () -> VERSION, VERSION::equals, VERSION::equals);;
    private static int ID = 0;

    public static void initialize() {
        CHANNEL.registerMessage(nextId(), ServerboundOpenPowersMessage.class, ServerboundOpenPowersMessage::encode, ServerboundOpenPowersMessage::decode, ServerboundOpenPowersMessage::handle);
        CHANNEL.registerMessage(nextId(), KnownPowerMessage.class, KnownPowerMessage::encode, KnownPowerMessage::decode, KnownPowerMessage::handle);
        CHANNEL.registerMessage(nextId(), PowerLevelMessage.class, PowerLevelMessage::encode, PowerLevelMessage::decode, PowerLevelMessage::handle);
        CHANNEL.registerMessage(nextId(), PowerPointMessage.class, PowerPointMessage::encode, PowerPointMessage::decode, PowerPointMessage::handle);
    }

    public static int nextId() {
        return ID++;
    }

}
