package io.github.a1qs.vaultadditions.mixins.rotateable_fools_gold;

import io.github.a1qs.vaultadditions.block.RotateableFoolsGold;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModBlocks.class, remap = false)
public class MixinModBlocks {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void rotateableFoolsGold(CallbackInfo ci) {
        AccessorModBlocks.setFOOLS_GOLD_BLOCK(new RotateableFoolsGold(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    }
}
