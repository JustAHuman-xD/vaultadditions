package io.github.a1qs.vaultadditions.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.a1qs.vaultadditions.block.RotateableFoolsGold;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModBlocks.class)
public class MixinModBlocks {
    @WrapOperation(method = "<clinit>", at = @At(value = "FIELD", target = "Liskallia/vault/init/ModBlocks;FOOLS_GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.PUTSTATIC))
    private static void rotateableFoolsGold(Block value, Operation<Void> original) {
        original.call(new RotateableFoolsGold(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).noOcclusion()));
    }
}
