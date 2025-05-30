package io.github.a1qs.vaultadditions.mixins.crucible_export;

import iskallia.vault.core.world.data.tile.PartialBlock;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PartialBlock.class)
public interface AccessorPartialBlock {
    @Accessor ResourceLocation getId();
}
