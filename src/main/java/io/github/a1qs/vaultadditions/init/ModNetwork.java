package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.network.KnownSpecialExpertiseMessage;
import io.github.a1qs.vaultadditions.network.ServerboundOpenSpecialExpertisesMessage;
import io.github.a1qs.vaultadditions.network.SpecialExpertiseLevelMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(VaultAdditions.MOD_ID, "network"), () -> VERSION, VERSION::equals, VERSION::equals);;
    private static int ID = 0;

    public static void initialize() {
        CHANNEL.registerMessage(nextId(), ServerboundOpenSpecialExpertisesMessage.class, ServerboundOpenSpecialExpertisesMessage::encode, ServerboundOpenSpecialExpertisesMessage::decode, ServerboundOpenSpecialExpertisesMessage::handle);
        CHANNEL.registerMessage(nextId(), KnownSpecialExpertiseMessage.class, KnownSpecialExpertiseMessage::encode, KnownSpecialExpertiseMessage::decode, KnownSpecialExpertiseMessage::handle);
        CHANNEL.registerMessage(nextId(), SpecialExpertiseLevelMessage.class, SpecialExpertiseLevelMessage::encode, SpecialExpertiseLevelMessage::decode, SpecialExpertiseLevelMessage::handle);
    }

    public static int nextId() {
        return ID++;
    }

}
