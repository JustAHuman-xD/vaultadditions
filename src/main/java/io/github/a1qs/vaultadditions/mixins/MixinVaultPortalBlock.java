package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.events.Event;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.core.vault.modifier.VaultModifierStack;
import iskallia.vault.core.vault.modifier.registry.VaultModifierRegistry;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = VaultPortalBlock.class, remap = false)
public class MixinVaultPortalBlock {
    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Liskallia/vault/block/entity/VaultPortalTileEntity;getData()Ljava/util/Optional;", shift = At.Shift.AFTER))
    public void addEventModifier(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            if (EventData.get(serverLevel).conditionsCompleted()) {
                BlockEntity te = level.getBlockEntity(pos);
                VaultPortalTileEntity portal = te instanceof VaultPortalTileEntity ? (VaultPortalTileEntity)te : null;
                if(portal != null && portal.getData().isPresent()) {
                    CrystalData crystalData = portal.getData().get();
                    VaultModifier modifier = VaultModifierRegistry.get(Event.EVENT_MODIFIER_MAP.get(EventData.get(serverLevel).getActiveEvent().getId()));
                    if(modifier != null) {
                        crystalData.getModifiers().add(VaultModifierStack.of(modifier));
                    } else {
                        VaultAdditions.LOGGER.error("Non-existant modifier {} found, could not apply Event Modifier", Event.EVENT_MODIFIER_MAP.get(EventData.get(serverLevel).getActiveEvent().getId()));
                    }
                }
            }
        }
    }

}
