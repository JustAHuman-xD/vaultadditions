package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.block.entity.SkinnableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootStatueTileEntity.class)
public abstract class MixinLootStatueTileEntity extends SkinnableTileEntity {
    @Shadow(remap = false) private int chipCount;

    private MixinLootStatueTileEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    /**
     * @author a1qs
     * @reason move to vaultaddition configs
     */
    @Overwrite(remap = false)
    public boolean addChip() {
        if (chipCount >= CustomVaultConfigRegistry.STATUE_LOOT_OMEGA.getMaxAccelerationChips()) {
            return false;
        }
        chipCount++;
        sendUpdates();
        return true;
    }

    /**
     * @author a1qs
     * @reason move to vaultaddition configs
     */
    @Overwrite(remap = false)
    private int getModifiedInterval() {
        int interval = CustomVaultConfigRegistry.STATUE_LOOT_OMEGA.getInterval();
        if (chipCount == 0) {
            return interval;
        }
        return interval - CustomVaultConfigRegistry.STATUE_LOOT_OMEGA.getIntervalDecrease(chipCount);
    }
}
