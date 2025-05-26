package io.github.a1qs.vaultadditions.util;

import iskallia.vault.block.DemagnetizerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface DeblockCacheHolder {
    Map<ResourceKey<Level>, Set<BlockPos>> vaultadditions$getCache();

    default void handleStateCycle(Level level, BlockPos pos) {
        Set<BlockPos> positions = vaultadditions$getCache().computeIfAbsent(level.dimension(), k -> Set.of());
        if (!positions.remove(pos)) {
            positions.add(pos);
        }
    }

    default boolean handleNeighborSet(Level level, BlockState state, BlockPos pos, int flags) {
        Set<BlockPos> positions = vaultadditions$getCache().computeIfAbsent(level.dimension(), k -> ConcurrentHashMap.newKeySet());
        if (state.getOptionalValue(DemagnetizerBlock.DEACTIVATED).orElse(false)) {
            positions.remove(pos);
        } else {
            positions.add(pos);
        }
        return level.setBlock(pos, state, flags);
    }

    default boolean vaultadditions$isInRange(Level level, Vec3 origin) {
        Set<BlockPos> positions = vaultadditions$getCache().get(level.dimension());
        if (positions != null) {
            for (BlockPos pos : positions) {
                if (pos.distToLowCornerSqr(origin.x, origin.y, origin.z) < 1024) {
                    return true;
                }
            }
        }
        return false;
    }
}
