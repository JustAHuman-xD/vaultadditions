package io.github.a1qs.vaultadditions.mixins.rotateable_fools_gold;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = MappedRegistry.class, remap = false)
public interface AccessorMappedRegistry<T> {
    @Accessor Map<T, Holder.Reference<T>> getIntrusiveHolderCache();
}
