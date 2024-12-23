package io.github.a1qs.vaultadditions.mixins;


import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import io.github.a1qs.vaultadditions.events.VaultCommonEvents;
import iskallia.vault.core.data.key.FieldKey;
import iskallia.vault.core.event.common.ChestGenerationEvent;
import iskallia.vault.core.vault.ClassicLootLogic;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.world.storage.VirtualWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = ClassicLootLogic.class, remap = false)
public class MixinClassicLootLogic {
    @Shadow @Final
    public static FieldKey<Void> ADD_CATALYST_FRAGMENTS;

    @Inject(method = "onChestPostGenerate", at = @At("TAIL"))
    protected void onChestPostGenerate(VirtualWorld world, Vault vault, ChestGenerationEvent.Data data, CallbackInfo ci) {

        if (((ClassicLootLogic)(Object)this).has(ADD_CATALYST_FRAGMENTS)) {
            double probability = CustomVaultConfigRegistry.EXTRA_VAULT_CHEST_META.getpowerCrystalChance(data.getState().getBlock(), data.getRarity());
            probability = VaultCommonEvents.CHEST_POWER_CRYSTAL_GENERATION_EVENT.invoke(data.getPlayer(), probability).getProbability();
            if ((double)data.getRandom().nextFloat() < probability) {
                data.getLoot().add(CustomVaultConfigRegistry.EXTRA_VAULT_CHEST_META.randomItem());
            }
        }
    }

}
