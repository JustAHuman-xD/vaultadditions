package io.github.a1qs.vaultadditions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class EnchantedFireBlock extends BaseFireBlock {
    public EnchantedFireBlock(Properties pProperties) {
        super(pProperties, 0);
    }

    @Override @ParametersAreNonnullByDefault
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        // Do nothing
    }

    @Override
    protected boolean canBurn(@NotNull BlockState blockState) {
        return false;
    }
}
