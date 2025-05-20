package io.github.a1qs.vaultadditions.mixins.rotateable_fools_gold;

import io.github.a1qs.vaultadditions.block.RotateableFoolsGold;
import iskallia.vault.init.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModBlocks.class, remap = false)
public class MixinModBlocks {
    @Redirect(method = "<clinit>", at = @At(value = "FIELD", target = "Liskallia/vault/init/ModBlocks;FOOLS_GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.PUTSTATIC))
    private static void rotateableFoolsGold(Block value) {
        ((AccessorMappedRegistry<Block>) Registry.BLOCK).getIntrusiveHolderCache().remove(value);
        AccessorModBlocks.setFOOLS_GOLD_BLOCK(new RotateableFoolsGold(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    }
}
