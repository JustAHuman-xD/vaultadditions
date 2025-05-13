package io.github.a1qs.vaultadditions.vault.gear.effect.set;

import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import net.minecraft.network.chat.MutableComponent;

public abstract class ArmorSetEffect {
    public abstract VaultGearAttributeInstance<?> getVaultGearAttributeInstance();
    public abstract MutableComponent getTooltipComponent();
}
