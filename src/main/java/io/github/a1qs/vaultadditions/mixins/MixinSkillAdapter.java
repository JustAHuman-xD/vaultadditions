package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.expertise.FortunateVaultarExpertise;
import iskallia.vault.skill.base.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Skill.Adapter.class, remap = false)
public class MixinSkillAdapter {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onAdapterInit(CallbackInfo ci) {
        ((Skill.Adapter) (Object)this).register("fortunate_vaultar", FortunateVaultarExpertise.class, FortunateVaultarExpertise::new);
    }
}
