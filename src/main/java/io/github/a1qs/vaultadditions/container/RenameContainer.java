package io.github.a1qs.vaultadditions.container;

import io.github.a1qs.vaultadditions.init.ModContainers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RenameContainer extends AbstractContainerMenu {
    private final CompoundTag nbt;

    public RenameContainer(int windowId, CompoundTag nbt) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        this.nbt = nbt.getCompound("Data");
    }

    public boolean stillValid(Player playerIn) {
        return true;
    }

    public CompoundTag getNbt() {
        return this.nbt;
    }

}

