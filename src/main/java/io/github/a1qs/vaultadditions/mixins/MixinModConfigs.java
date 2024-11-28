package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.vault.StatueLootConfig;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import io.github.a1qs.vaultadditions.config.vault.PowerConfig;
import io.github.a1qs.vaultadditions.config.vault.PowerGUIConfig;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModConfigs.class, remap = false)
public class MixinModConfigs {

    @Inject(method = "register", at = @At("TAIL"))
    private static void injectRegistries(CallbackInfo ci) {
        MiscUtil.POWERS_GUI = new PowerGUIConfig().readConfig();
        MiscUtil.POWERS = new PowerConfig().readConfig();
        MiscUtil.STATUE_LOOT = new StatueLootConfig().readConfig();
        VaultAdditions.LOGGER.info("Successfully loaded custom Vault Configs");
    }
}
