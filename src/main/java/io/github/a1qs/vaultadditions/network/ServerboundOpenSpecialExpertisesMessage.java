package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.init.ModContainers;
import io.github.a1qs.vaultadditions.test.PlayerSpecialExpertiseData;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import iskallia.vault.container.NBTElementContainer;
import iskallia.vault.core.net.ArrayBitBuffer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class ServerboundOpenSpecialExpertisesMessage {
    public static final ServerboundOpenSpecialExpertisesMessage INSTANCE = new ServerboundOpenSpecialExpertisesMessage();

    public ServerboundOpenSpecialExpertisesMessage() {
    }

    public static void encode(ServerboundOpenSpecialExpertisesMessage message, FriendlyByteBuf buffer) {
    }

    public static ServerboundOpenSpecialExpertisesMessage decode(FriendlyByteBuf buffer) {
        return INSTANCE;
    }

    public static void handle(ServerboundOpenSpecialExpertisesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                PlayerSpecialExpertiseData playerExpertisesData = PlayerSpecialExpertiseData.get((ServerLevel)sender.level);
                final SpecialExpertiseTree expertiseTree = playerExpertisesData.getSpecialExpertises(sender);
                NetworkHooks.openGui(sender, new MenuProvider() {
                    @Nonnull
                    public Component getDisplayName() {
                        return new TranslatableComponent("container.vault.specialexpertises");
                    }

                    @ParametersAreNonnullByDefault
                    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                        return new NBTElementContainer(() -> {
                            return ModContainers.SPECIAL_EXPERTISE_TAB_CONTAINER;
                        }, i, playerInventory.player, expertiseTree);
                    }
                }, (buffer) -> {
                    ArrayBitBuffer buffer1 = ArrayBitBuffer.empty();
                    expertiseTree.writeBits(buffer1);
                    buffer.writeLongArray(buffer1.toLongArray());
                });
            }
        });
        context.setPacketHandled(true);
    }
}
