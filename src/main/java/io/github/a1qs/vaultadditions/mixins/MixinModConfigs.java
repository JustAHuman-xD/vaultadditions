package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.vault.powers.PowerConfigs;
import io.github.a1qs.vaultadditions.vault.powers.SpecialExpertisesConfig;
import io.github.a1qs.vaultadditions.vault.powers.SpecialExpertisesGUIConfig;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModConfigs.class, remap = false)
public class MixinModConfigs {

    @Inject(method = "register", at = @At("TAIL"))
    private static void injectRegistries(CallbackInfo ci) {
        PowerConfigs.SPECIAL_EXPERTISES_GUI = new SpecialExpertisesGUIConfig().readConfig();
        PowerConfigs.SPECIAL_EXPERTISES = new SpecialExpertisesConfig().readConfig();
        VaultAdditions.LOGGER.info("Successfully loaded custom Vault Configs");
    }
}
