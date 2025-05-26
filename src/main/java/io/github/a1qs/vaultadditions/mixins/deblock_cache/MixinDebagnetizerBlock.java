package io.github.a1qs.vaultadditions.mixins.deblock_cache;

import io.github.a1qs.vaultadditions.util.DeblockCacheHolder;
import iskallia.vault.block.DebagnetizerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(DebagnetizerBlock.class)
public class MixinDebagnetizerBlock implements EntityBlock, DeblockCacheHolder {
    @Unique private static final Map<ResourceKey<Level>, Set<BlockPos>> vaultadditions$CACHE = new ConcurrentHashMap<>();

    /**
     * @author JustAHuman
     * @reason Use a cache instead of constantly checking the world for nearby debagnetizers.
     */
    @Overwrite(remap = false)
    public boolean isInRange(Level world, Vec3 origin) {
        return vaultadditions$isInRange(world, origin);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public void endTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {
        handleStateCycle(world, pos);
    }

    @Override @ParametersAreNonnullByDefault
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    public Map<ResourceKey<Level>, Set<BlockPos>> vaultadditions$getCache() {
        return vaultadditions$CACHE;
    }
}
