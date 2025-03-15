package io.github.a1qs.vaultadditions.util;

import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.ability.*;
import iskallia.vault.gear.attribute.custom.effect.EffectGearAttribute;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffect;

public class VaultGearAttributeHelper {
    public static VaultGearAttributeInstance<?> abilityLevelAttributeInstance(String name, int levelChange) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_LEVEL, new AbilityLevelAttribute(name, levelChange));
    }

    public static VaultGearAttributeInstance<?> abilityCooldownFlatAttributeInstance(String name, float amount) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_COOLDOWN_FLAT, new AbilityCooldownFlatAttribute(name, amount));
    }

    public static VaultGearAttributeInstance<?> abilityCooldownPercentageAttributeInstance(String abilityName, float amount) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_COOLDOWN_PERCENT, new AbilityCooldownPercentAttribute(abilityName, amount));
    }

    public static VaultGearAttributeInstance<?> abilityManaCostFlatAttributeInstance(String name, float amount) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_MANACOST_FLAT, new AbilityManaCostPercentAttribute(name, amount));
    }

    public static VaultGearAttributeInstance<?> abilityManaCostPercentageAttributeInstance(String abilityName, float amount) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_MANACOST_PERCENT, new AbilityManaCostPercentAttribute(abilityName, amount));
    }

    public static VaultGearAttributeInstance<?> potionEffectVaultAttributeInstance(MobEffect effect, int amplifier) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.EFFECT, new EffectGearAttribute(effect, amplifier));
    }


}
