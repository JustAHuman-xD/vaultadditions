package io.github.a1qs.vaultadditions.block;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EnchantedFireBlock extends BaseFireBlock {
    public EnchantedFireBlock(Properties pProperties) {
        super(pProperties, 0);
    }

    @Override
    protected boolean canBurn(@NotNull BlockState blockState) {
        return false;
    }
}
