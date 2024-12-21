package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.skill.ability.BladeFrenzyAbility;
import io.github.a1qs.vaultadditions.vault.skill.ability.LegacyManaShieldAbility;
import iskallia.vault.init.ModAbilityLabelBindings;
import iskallia.vault.skill.ability.component.AbilityLabelFormatters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = ModAbilityLabelBindings.class, remap = false)
public class MixinModAbilityLabelBindings {
    @Inject(method = "register()V", at = @At("TAIL"))
    private static void addAbilityLabelBindings(CallbackInfo ci) {
        ModAbilityLabelBindings.register(
                LegacyManaShieldAbility.class,
                Map.of(
                        "absorb",
                        ability -> AbilityLabelFormatters.percentRounded(ability.getPercentageDamageAbsorbed()),
                        "manaPerDamage",
                        ability -> AbilityLabelFormatters.decimal(ability.getManaPerDamageScalar())
                )
        );
        ModAbilityLabelBindings.register(
                BladeFrenzyAbility.class,
                Map.of(
                        "percentAttackDealt",
                        ability -> AbilityLabelFormatters.percentRounded(ability.getPercentAttackDealt()),
                        "radius",
                        ability -> AbilityLabelFormatters.decimal(ability.getUnmodifiedRadius()),
                        "knockbackStrengthMultiplier",
                        ability -> AbilityLabelFormatters.decimal(ability.getKnockbackStrengthMultiplier())
                )
        );
    }
}
