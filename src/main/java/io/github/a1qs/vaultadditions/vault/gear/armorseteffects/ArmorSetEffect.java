package io.github.a1qs.vaultadditions.vault.gear.armorseteffects;

import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public abstract class ArmorSetEffect {
    public abstract VaultGearAttributeInstance<?> getVaultGearAttributeInstance();
    protected abstract MutableComponent getTooltipComponent();
}
