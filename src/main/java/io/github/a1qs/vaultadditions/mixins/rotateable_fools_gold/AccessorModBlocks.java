package io.github.a1qs.vaultadditions.mixins.rotateable_fools_gold;

import iskallia.vault.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ModBlocks.class, remap = false)
public interface AccessorModBlocks {
    @Mutable @Accessor
    static void setFOOLS_GOLD_BLOCK(Block value) {
        throw new AssertionError("Mixin failed to apply");
    }
}
