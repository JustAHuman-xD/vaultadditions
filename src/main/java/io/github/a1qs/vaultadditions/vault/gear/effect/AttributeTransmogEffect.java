package io.github.a1qs.vaultadditions.vault.gear.effect;

import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;

public abstract class AttributeTransmogEffect<T> extends TransmogEffect {
    public abstract VaultGearAttributeInstance<T> getVaultGearAttributeInstance();

    @Override
    public MutableComponent getTooltip() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay(this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }
}
