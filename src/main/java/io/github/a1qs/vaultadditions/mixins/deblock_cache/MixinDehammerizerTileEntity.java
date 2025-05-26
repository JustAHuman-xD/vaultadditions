package io.github.a1qs.vaultadditions.mixins.deblock_cache;

import io.github.a1qs.vaultadditions.util.DeblockCacheHolder;
import iskallia.vault.block.entity.DehammerizerTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DehammerizerTileEntity.class, remap = false)
public class MixinDehammerizerTileEntity {
    /**
     * @author JustAHuman
     * @reason Use a cache instead of constantly checking the world for nearby dehammerizers.
     */
    @Overwrite
    public static boolean hasDemhammerizerAround(Entity entity) {
        return ((DeblockCacheHolder) ModBlocks.DEHAMMERIZER).vaultadditions$isInRange(entity.level, entity.position());
    }
}
