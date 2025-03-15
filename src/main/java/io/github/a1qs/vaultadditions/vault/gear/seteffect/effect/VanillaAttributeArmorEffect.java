package io.github.a1qs.vaultadditions.vault.gear.seteffect.effect;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class VanillaAttributeArmorEffect extends ArmorSetEffect {
    private final Attribute attribute;
    private final double value;
    private final AttributeModifier.Operation operation;
    private final AttributeModifier modifier;

    public VanillaAttributeArmorEffect(Attribute attribute, double value, AttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
        this.modifier = new AttributeModifier("armor_set_bonus", value, operation);
    }
    @Override
    public VaultGearAttributeInstance<?> getVaultGearAttributeInstance() {
        VaultGearAttribute<?> attribute = ModGearAttributes.getGearAttribute(this.attribute, this.operation);
        return VaultGearAttributeInstance.cast(attribute, this.value);
    }

    // yummy unchecked assignment !!!!!
    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay((VaultGearAttributeInstance) this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }

    public void apply(Player player) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null && !instance.hasModifier(modifier)) {
            instance.addTransientModifier(modifier);
        }
    }

    public void remove(Player player) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(modifier);
        }
    }
}