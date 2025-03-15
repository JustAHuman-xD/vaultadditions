package io.github.a1qs.vaultadditions.vault.gear.seteffect.effect;

import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearModifier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;

public class EffectAttributeArmorEffect extends ArmorSetEffect {
    private final MobEffect effect;
    private final int amplifier;

    public EffectAttributeArmorEffect(MobEffect effect, int amplifier) {
        this.effect = effect;
        this.amplifier = amplifier;
    }
    @Override
    public VaultGearAttributeInstance<?> getVaultGearAttributeInstance() {
        return VaultGearAttributeHelper.potionEffectVaultAttributeInstance(this.effect, this.amplifier);
    }

    // yummy unchecked assignment !!!!!
    @Override
    public MutableComponent getTooltipComponent() {
        return this.getVaultGearAttributeInstance().getAttribute().getReader().getDisplay((VaultGearAttributeInstance) this.getVaultGearAttributeInstance(), VaultGearModifier.AffixType.PREFIX);
    }
}