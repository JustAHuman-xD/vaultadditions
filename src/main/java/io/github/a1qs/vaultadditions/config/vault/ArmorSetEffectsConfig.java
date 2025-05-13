package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.vault.gear.effect.set.ArmorSetEffect;
import iskallia.vault.config.Config;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorSetEffectsConfig extends Config {
    @Expose
    private JsonObject setEffects;

    public final Map<ResourceLocation, List<ArmorSetEffect>> effects = new HashMap<>();

    @Override
    public <T extends Config> T readConfig() {
        super.readConfig();

        for (String key : setEffects.keySet()) {

        }
    }

    @Override
    protected void reset() {

    }

    @Override
    public String getName() {
        return "vaultadditions_armor_set_effects";
    }
}
