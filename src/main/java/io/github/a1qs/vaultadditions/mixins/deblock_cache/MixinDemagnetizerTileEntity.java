package io.github.a1qs.vaultadditions.mixins.deblock_cache;

import io.github.a1qs.vaultadditions.util.DeblockCacheHolder;
import iskallia.vault.block.entity.DemagnetizerTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DemagnetizerTileEntity.class, remap = false)
public class MixinDemagnetizerTileEntity {
    /**
     * @author JustAHuman
     * @reason Use a cache instead of constantly checking the world for nearby demagnetizers.
     */
    @Overwrite
    public static boolean hasDemagnetizerAround(Entity entity) {
        return ((DeblockCacheHolder) ModBlocks.DEMAGNETIZER_BLOCK).vaultadditions$isInRange(entity.level, entity.position());
    }
}
