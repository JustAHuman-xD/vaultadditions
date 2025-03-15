package io.github.a1qs.vaultadditions.vault.gear.seteffect.effect;

import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.BiFunction;

public class AbilityModificationAttributeArmorEffect extends ArmorSetEffect {
    private final String abilityName;
    private final float modificationValue;
    private final AbilityModification abilityModification;

    public AbilityModificationAttributeArmorEffect(String abilityName, float modificationValue, AbilityModification abilityModification) {
        this.abilityName = abilityName;
        this.modificationValue = modificationValue;
        this.abilityModification = abilityModification;
    }

    @Override
    public VaultGearAttributeInstance<?> getVaultGearAttributeInstance() {
        return abilityModification.getVaultGearAttribute(this.abilityName, this.modificationValue);
    }

    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay((VaultGearAttributeInstance) this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }

    public enum AbilityModification {
        COOLDOWN_REDUCTION_FLAT(VaultGearAttributeHelper::abilityCooldownFlatAttributeInstance),
        COOLDOWN_REDUCTION_PERCENTAGE(VaultGearAttributeHelper::abilityCooldownPercentageAttributeInstance),
        MANA_COST_REDUCTION_FLAT(VaultGearAttributeHelper::abilityManaCostFlatAttributeInstance),
        MANA_COST_REDUCTION_PERCENTAGE(VaultGearAttributeHelper::abilityManaCostPercentageAttributeInstance);

        private final BiFunction<String, Float, VaultGearAttributeInstance<?>> attributeFunction;

        AbilityModification(BiFunction<String, Float, VaultGearAttributeInstance<?>> attributeFunction) {
            this.attributeFunction = attributeFunction;
        }

        public VaultGearAttributeInstance<?> getVaultGearAttribute(String abilityName, float modificationValue) {
            return attributeFunction.apply(abilityName, modificationValue);
        }
    }
}
