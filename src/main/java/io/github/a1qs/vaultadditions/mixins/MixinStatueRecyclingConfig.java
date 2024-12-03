package io.github.a1qs.vaultadditions.mixins;

import iskallia.vault.VaultMod;
import iskallia.vault.config.StatueRecyclingConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(value = StatueRecyclingConfig.class, remap = false)
public abstract class MixinStatueRecyclingConfig {

    @Shadow
    private HashMap<String, Integer> itemValues;

    /**
     * @author a1qs
     * @reason dont throw an Internalerror when no item with the given id, default to 1
     */
    @Overwrite
    public int getItemValue(String id) {
        if (this.itemValues.containsKey(id)) {
            return this.itemValues.get(id);
        } else {
            VaultMod.LOGGER.error("Could not find statue recycle value of id '{}' defaulted to 1", id);
            return 1;
        }
    }
}
