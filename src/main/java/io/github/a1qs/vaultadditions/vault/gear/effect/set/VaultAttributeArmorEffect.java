package io.github.a1qs.vaultadditions.vault.gear.effect.set;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;

public class VaultAttributeArmorEffect<T extends Number> extends ArmorSetEffect {
    private final VaultGearAttribute<?> attribute;
    private final T value;

    public VaultAttributeArmorEffect(VaultGearAttribute<T> attribute, T attributeValue) {
        this.attribute = attribute;
        this.value = attributeValue;
    }
    @Override
    public VaultGearAttributeInstance<?> getVaultGearAttributeInstance() {
        return VaultGearAttributeInstance.cast(this.attribute, this.value);
    }

    // yummy unchecked assignment !!!!!
    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay((VaultGearAttributeInstance) this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }
}
