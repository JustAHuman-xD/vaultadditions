package io.github.a1qs.vaultadditions.mixins.crucible_export;

import iskallia.vault.core.world.data.tile.PartialBlockState;
import iskallia.vault.core.world.template.StructureTemplate;
import net.minecraft.core.IdMapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = StructureTemplate.IdPalette.class, remap = false)
public interface AccessorIdPalette {
    @Accessor IdMapper<PartialBlockState> getIds();
}
