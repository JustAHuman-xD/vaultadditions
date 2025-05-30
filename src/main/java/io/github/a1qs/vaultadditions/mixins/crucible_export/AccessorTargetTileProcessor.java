package io.github.a1qs.vaultadditions.mixins.crucible_export;

import iskallia.vault.core.world.data.tile.TilePredicate;
import iskallia.vault.core.world.processor.tile.TargetTileProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TargetTileProcessor.class, remap = false)
public interface AccessorTargetTileProcessor {
    @Accessor TilePredicate getPredicate();
}
