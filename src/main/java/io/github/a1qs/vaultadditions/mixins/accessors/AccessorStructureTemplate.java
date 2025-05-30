package io.github.a1qs.vaultadditions.mixins.accessors;

import iskallia.vault.core.world.template.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureTemplate.class)
public interface AccessorStructureTemplate {
    @Accessor StructureTemplate.IdPalette getPalette();
}
