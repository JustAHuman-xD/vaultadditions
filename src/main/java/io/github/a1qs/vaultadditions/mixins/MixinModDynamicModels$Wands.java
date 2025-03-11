package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;
import iskallia.vault.init.ModDynamicModels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModDynamicModels.Wands.class, remap = false)
public class MixinModDynamicModels$Wands {

    @Shadow
    @Final
    public static DynamicModelRegistry<PlainItemModel> REGISTRY;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void injectWandModels(CallbackInfo ci) {
        REGISTRY.register(ModModels.Wands.SIDEARM)
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll());
    }
}
