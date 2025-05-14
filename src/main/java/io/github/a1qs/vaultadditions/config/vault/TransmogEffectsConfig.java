package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.effect.TransmogEffect;
import iskallia.vault.config.Config;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransmogEffectsConfig extends Config {
    @Expose
    private final JsonObject transmogEffects = new JsonObject();
    public final Map<DynamicModel<?>, List<TransmogEffect>> effects = new HashMap<>();

    public boolean hasEffect(ItemStack itemStack, TransmogEffect effect) {
        return hasEffect(ModelUtil.getDynamicModel(itemStack, false), effect);
    }

    public boolean hasEffect(DynamicModel<?> model, TransmogEffect effect) {
        if (model == null) {
            return false;
        }
        List<TransmogEffect> effectList = effects.get(model);
        return effectList != null && effectList.contains(effect);
    }

    public <E extends TransmogEffect> List<E> getEffects(ItemStack itemStack, Class<E> type) {
        return getEffects(ModelUtil.getDynamicModel(itemStack, false), type);
    }

    public <E extends TransmogEffect> List<E> getEffects(DynamicModel<?> model, Class<E> type) {
        if (model == null) {
            return List.of();
        }
        List<TransmogEffect> effectList = effects.get(model);
        if (effectList == null) {
            return List.of();
        }

        List<E> effects = new ArrayList<>();
        for (TransmogEffect effect : effectList) {
            if (type.isInstance(effect)) {
                effects.add(type.cast(effect));
            }
        }
        return effects;
    }

    public List<TransmogEffect> getEffects(ItemStack itemStack) {
        return getEffects(ModelUtil.getDynamicModel(itemStack, false));
    }

    public List<TransmogEffect> getEffects(DynamicModel<?> model) {
        return model == null ? List.of() : effects.getOrDefault(model, List.of());
    }

    @Override
    protected void onLoad(@Nullable Config oldConfigInstance) {
        for (String key : transmogEffects.keySet()) {
            ResourceLocation id = ResourceLocation.tryParse(key);
            DynamicModel<?> model = ModDynamicModels.REGISTRIES.getModelByResourceLocation(id).orElse(null);
            if (id == null || model == null) {
                VaultAdditions.LOGGER.warn("Invalid transmog identifier: {}", key);
                continue;
            }

            List<TransmogEffect> effects = new ArrayList<>();
            JsonArray serializedEffects = transmogEffects.get(key) instanceof JsonArray array ? array : null;
            if (serializedEffects == null) {
                serializedEffects = new JsonArray();
                serializedEffects.add(transmogEffects.get(key));
            } else if (serializedEffects.isEmpty()) {
                VaultAdditions.LOGGER.info("No transmog effects for {}, skipping", key);
                continue;
            }

            for (JsonElement serializedEffect : serializedEffects) {
                TransmogEffect effect = TransmogEffect.deserializeEffect(serializedEffect);
                if (effect == null) {
                    VaultAdditions.LOGGER.warn("Invalid transmog effect {}/{}, skipping", key, serializedEffect);
                    continue;
                }
                effects.add(effect);
            }

            if (!effects.isEmpty()) {
                this.effects.put(model, Collections.unmodifiableList(effects));
            }
        }
    }

    @Override
    protected void reset() {

    }

    @Override
    public String getName() {
        return "vaultadditions_transmog_effects";
    }
}
