package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.core.vault.objective.InfiniteRaidObjective;
import iskallia.vault.core.random.RandomSource;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.item.crystal.objective.RaidCrystalObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RaidCrystalObjective.class)
public class MixinRaidCrystalObjective {
    @Inject(method = "configure", at = @At("TAIL"))
    public void configure(Vault vault, RandomSource random, CallbackInfo ci) {
        vault.ifPresent(Vault.OBJECTIVES, objectives -> objectives.add(new InfiniteRaidObjective()));
    }
}
