package io.github.a1qs.vaultadditions.vault.gear.seteffect.effect;

import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;

public class AbilityAttributeArmorEffect extends ArmorSetEffect {
    private final String abilityName;
    private final int abilityLevel;

    public AbilityAttributeArmorEffect(String abilityName, int abilityLevel) {
        this.abilityName = abilityName;
        this.abilityLevel = abilityLevel;
    }
    @Override
    public VaultGearAttributeInstance<?> getVaultGearAttributeInstance() {
        return VaultGearAttributeHelper.abilityLevelAttributeInstance(this.abilityName, this.abilityLevel);
    }

    // yummy unchecked assignment !!!!!
    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay((VaultGearAttributeInstance) this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }
}
