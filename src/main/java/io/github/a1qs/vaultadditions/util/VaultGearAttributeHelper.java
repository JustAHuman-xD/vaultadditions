package io.github.a1qs.vaultadditions.util;

import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.ability.AbilityLevelAttribute;
import iskallia.vault.gear.attribute.custom.effect.EffectGearAttribute;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffect;

public class VaultGearAttributeHelper {
    public static VaultGearAttributeInstance<?> abilityLevelAttributeInstance(String name, int levelChange) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.ABILITY_LEVEL, new AbilityLevelAttribute(name, levelChange));
    }

    public static VaultGearAttributeInstance<?> potionEffectVaultAttributeInstance(MobEffect effect, int amplifier) {
        return VaultGearAttributeInstance.cast(ModGearAttributes.EFFECT, new EffectGearAttribute(effect, amplifier));
    }


}
