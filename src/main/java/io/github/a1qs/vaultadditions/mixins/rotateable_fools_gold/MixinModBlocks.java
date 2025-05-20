package io.github.a1qs.vaultadditions.mixins.rotateable_fools_gold;

import io.github.a1qs.vaultadditions.block.RotateableFoolsGold;
import iskallia.vault.VaultMod;
import iskallia.vault.init.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.RegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModBlocks.class, remap = false)
public abstract class MixinModBlocks {
    @Inject(method = "registerBlocks", at = @At("TAIL"))
    private static void rotateableFoolsGold(RegistryEvent.Register<Block> event, CallbackInfo ci) {
        Block foolsGold = new RotateableFoolsGold(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK));
        AccessorModBlocks.setFOOLS_GOLD_BLOCK(foolsGold);
        registerBlock(event, foolsGold, VaultMod.id("fools_gold"));
    }

    @Shadow
    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
        throw new AssertionError("Mixin failed to apply");
    }
}
