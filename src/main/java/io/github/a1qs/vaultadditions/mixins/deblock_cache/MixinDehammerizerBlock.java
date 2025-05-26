package io.github.a1qs.vaultadditions.mixins.deblock_cache;

import io.github.a1qs.vaultadditions.util.DeblockCacheHolder;
import iskallia.vault.block.DehammerizerBlock;
import iskallia.vault.block.DemagnetizerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(DehammerizerBlock.class)
public class MixinDehammerizerBlock implements EntityBlock, DeblockCacheHolder {
    @Unique private static final Map<ResourceKey<Level>, Set<BlockPos>> vaultadditions$CACHE = new ConcurrentHashMap<>();

    @Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0))
    public void onNeighborCycle(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving, CallbackInfo ci) {
        handleStateCycle(pLevel, pPos);
    }

    @Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public boolean onNeighborSet(Level instance, BlockPos pPos, BlockState pNewState, int pFlags) {
        return handleNeighborSet(instance, pNewState, pPos, pFlags);
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
