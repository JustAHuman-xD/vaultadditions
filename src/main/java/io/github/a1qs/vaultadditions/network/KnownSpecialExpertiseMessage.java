package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.client.ClientSpecialExpertiseData;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.core.net.BitBuffer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KnownSpecialExpertiseMessage {
    private SpecialExpertiseTree tree;

    public KnownSpecialExpertiseMessage(SpecialExpertiseTree tree) {
        this.tree = tree;
    }

    public SpecialExpertiseTree getTree() {
        return this.tree;
    }

    public static void encode(KnownSpecialExpertiseMessage message, FriendlyByteBuf buffer) {
        ArrayBitBuffer bits = ArrayBitBuffer.empty();
        message.tree.writeBits(bits);
        buffer.writeLongArray(bits.toLongArray());
    }

    public static KnownSpecialExpertiseMessage decode(FriendlyByteBuf buffer) {
        BitBuffer bits = ArrayBitBuffer.backing(buffer.readLongArray(), 0);
        SpecialExpertiseTree tree = new SpecialExpertiseTree();
        tree.readBits(bits);
        return new KnownSpecialExpertiseMessage(tree);
    }

    public static void handle(KnownSpecialExpertiseMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ClientSpecialExpertiseData.updateTalents(message);
        });
        context.setPacketHandled(true);
    }
}