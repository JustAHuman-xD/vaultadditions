package io.github.a1qs.vaultadditions.mixins.crucible_export;

import iskallia.vault.core.world.template.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = StructureTemplate.class, remap = false)
public interface AccessorStructureTemplate {
    @Accessor StructureTemplate.IdPalette getPalette();
}
