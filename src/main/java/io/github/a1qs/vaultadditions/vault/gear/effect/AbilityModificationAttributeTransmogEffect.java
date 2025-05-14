package io.github.a1qs.vaultadditions.vault.gear.effect;

import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.attribute.ability.AbilityFloatValueAttribute;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.BiFunction;

public class AbilityModificationAttributeTransmogEffect extends AttributeTransmogEffect<? extends AbilityFloatValueAttribute> {
    private final String abilityName;
    private final float modificationValue;
    private final AbilityModification abilityModification;

    public AbilityModificationAttributeTransmogEffect(String abilityName, float modificationValue, AbilityModification abilityModification) {
        this.abilityName = abilityName;
        this.modificationValue = modificationValue;
        this.abilityModification = abilityModification;
    }

    @Override
    public VaultGearAttributeInstance<? extends AbilityFloatValueAttribute> getVaultGearAttributeInstance() {
        return null;
    }

    public enum AbilityModification {
        COOLDOWN_REDUCTION_FLAT(VaultGearAttributeHelper::abilityCooldownFlatAttributeInstance),
        COOLDOWN_REDUCTION_PERCENTAGE(VaultGearAttributeHelper::abilityCooldownPercentageAttributeInstance),
        MANA_COST_REDUCTION_FLAT(VaultGearAttributeHelper::abilityManaCostFlatAttributeInstance),
        MANA_COST_REDUCTION_PERCENTAGE(VaultGearAttributeHelper::abilityManaCostPercentageAttributeInstance);

        private final BiFunction<String, Float, VaultGearAttributeInstance<? extends AbilityFloatValueAttribute>> attributeFunction;

        AbilityModification(BiFunction<String, Float, VaultGearAttributeInstance<? extends AbilityFloatValueAttribute>> attributeFunction) {
            this.attributeFunction = attributeFunction;
        }

        public VaultGearAttributeInstance<? extends AbilityFloatValueAttribute> getVaultGearAttribute(String abilityName, float modificationValue) {
            return attributeFunction.apply(abilityName, modificationValue);
        }
    }
}
