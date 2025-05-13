package io.github.a1qs.vaultadditions.vault.gear.effect.set;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearAttributeRegistry;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class VaultAttributeArmorEffect$UNUSED<T extends Number> extends ArmorSetEffect {
    private final ResourceLocation id;
    private final T value;

    public VaultAttributeArmorEffect$UNUSED(ResourceLocation id, T attributeValue) {
        this.id = id;
        this.value = attributeValue;
    }
    @Override
    public VaultGearAttributeInstance<T> getVaultGearAttributeInstance() {
        return VaultGearAttributeInstance.cast(this.getAttribute(), this.value);
    }

    // Fuck you, fuck you again, and then fuck yourself once more. thanks
    public VaultGearAttribute<T> getAttribute() {
        return (VaultGearAttribute<T>) VaultGearAttributeRegistry.getAttribute(id);
    }

    // yummy unchecked assignment !!!!!
    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay(this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }
}