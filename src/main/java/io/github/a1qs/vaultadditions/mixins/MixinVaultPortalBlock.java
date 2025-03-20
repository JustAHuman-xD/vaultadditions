package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.events.VaultAdditionsEvent;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.core.vault.modifier.VaultModifierStack;
import iskallia.vault.core.vault.modifier.registry.VaultModifierRegistry;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;


@Mixin(value = VaultPortalBlock.class)
public class MixinVaultPortalBlock {
    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Liskallia/vault/block/entity/VaultPortalTileEntity;getData()Ljava/util/Optional;", shift = At.Shift.AFTER))
    public void addEventModifier(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            EventData data = EventData.get(serverLevel);
            if (data.conditionsCompleted() && data.getActiveEvent().getEventId().equals(VaultAdditionsEvent.ADD_PORTAL_MODIFIERS)) {
                BlockEntity te = level.getBlockEntity(pos);
                VaultPortalTileEntity portal = te instanceof VaultPortalTileEntity ? (VaultPortalTileEntity)te : null;
                if(portal != null && portal.getData().isPresent()) {
                    CrystalData crystalData = portal.getData().get();

                    for(Map.Entry<ResourceLocation, Integer> rl : data.getActiveEvent().getAdditionalModifiers().entrySet()) {
                        VaultModifier<?> modifier = VaultModifierRegistry.get(rl.getKey());
                        if(modifier != null) {
                            crystalData.getModifiers().add(VaultModifierStack.of(modifier, rl.getValue()));
                        } else {
                            VaultAdditions.LOGGER.error("Non-existant modifier {} found, could not apply Event Modifier", rl);
                        }
                    }
                }
            }
        }
    }
}
