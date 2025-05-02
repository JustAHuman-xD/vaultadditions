package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.dynamodel.registry.ArmorPieceModelRegistry;
import iskallia.vault.init.ModDynamicModels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModDynamicModels.Armor.class, remap = false)
public class MixinModDynamicModels$Armor {
    @Shadow @Final public static ArmorPieceModelRegistry PIECE_REGISTRY;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void injectArmorModels(CallbackInfo ci) {
        for (ModModels.Armor set : ModModels.Armor.values()) {
            PIECE_REGISTRY.registerAll(set.getModel());
        }
        for (ModModels.GeckoArmor set : ModModels.GeckoArmor.values()) {
            PIECE_REGISTRY.registerAll(set.getModel());
        }
    }
}
