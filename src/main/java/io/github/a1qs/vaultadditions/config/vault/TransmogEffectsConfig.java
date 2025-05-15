package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.init.ModSounds;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.util.SoundChoice;
import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import io.github.a1qs.vaultadditions.vault.gear.effect.AbilitySoundTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.AttributeTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.ElytraSoundTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.HideElytraTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.TransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.VanillaAttributeArmorTransmogEffect;
import iskallia.vault.config.Config;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.init.ModAbilities;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransmogEffectsConfig extends Config {
    static {
        TransmogEffect.registerTypes();
    }

    @Expose
    private final JsonObject transmogEffects = new JsonObject();
    public final Map<DynamicModel<?>, List<TransmogEffect>> effects = new HashMap<>();

    public boolean hasEffect(Player player, TransmogEffect effect) {
        if (hasEffect(ModelUtil.getWornSet(player), effect)) {
            return true;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (hasEffect(player.getItemBySlot(slot), effect)) {
                return true;
            }
        }
        return false;
    }

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

    public <T extends TransmogEffect> List<T> getEffects(ServerPlayer player, Class<T> type) {
        ArmorModel wornModel = ModelUtil.getWornSet(player);
        List<T> effects = new ArrayList<>(getEffects(wornModel, type));
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            effects.addAll(getEffects(player.getItemBySlot(slot), type));
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
        for (ArmorModel model : ModModels.HOKAGE_ARMOR) {
            JsonArray effects = new JsonArray();
            effects.add(HideElytraTransmogEffect.INSTANCE.serialize());
            effects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityManaCostPercentage("Empower", -0.25F)).serialize());
            effects.add(new VanillaAttributeArmorTransmogEffect<>(Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, 0.1F).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.SMITE_ARCHON, new SoundChoice(ModSounds.TIGER_ACTIVATE_ARCHON.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect("Smite_Abstract", new SoundChoice(ModSounds.TIGER_ARCHON_BOLT.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.DASH, new SoundChoice(ModSounds.TIGER_DASH.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.MANA_SHIELD, new SoundChoice(ModSounds.TIGER_ACTIVATE_MANASHIELD.get())).serialize());
            transmogEffects.add(model.getId().toString(), effects);
        }

        for (ArmorModel model : ModModels.HOY_ARMOR) {
            JsonArray effects = new JsonArray();
            effects.add(HideElytraTransmogEffect.INSTANCE.serialize());
            effects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityManaCostPercentage("Mana_Shield_Legacy", -0.25F)).serialize());
            effects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityManaCostPercentage("Smite_Archon", -0.25F)).serialize());
            effects.add(new ElytraSoundTransmogEffect(ModSounds.HOY_ELYTRA_GLIDE.get(), 0.2F).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.SMITE_ARCHON, new SoundChoice(ModSounds.HOY_ACTIVATE_ARCHON.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect("Smite_Abstract", new SoundChoice(ModSounds.HOY_ARCHON_BOLT.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.DASH, new SoundChoice(ModSounds.HOY_DASH.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.MANA_SHIELD, new SoundChoice(ModSounds.HOY_ACTIVATE_MANASHIELD.get())).serialize());
            effects.add(new AbilitySoundTransmogEffect(ModAbilities.MANA_SHIELD, new SoundChoice(ModSounds.HOY_MANASHIELD_HIT.get())).serialize());
            transmogEffects.add(model.getId().toString(), effects);
        }

        JsonArray vikingEffects = new JsonArray();
        vikingEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.potionEffect(MobEffects.DAMAGE_BOOST, 10)).serialize());
        vikingEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityLevel("Rampage", 2)).serialize());
        transmogEffects.add(ModModels.Armor.VIKING.getModel().getId().toString(), vikingEffects);

        JsonArray celestialEffects = new JsonArray();
        celestialEffects.add(HideElytraTransmogEffect.INSTANCE.serialize());
        celestialEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityManaCostPercentage("Empower", -0.25F)).serialize());
        celestialEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityCooldownPercentage("Ghost_Walk", -0.5F)).serialize());
        celestialEffects.add(new VanillaAttributeArmorTransmogEffect<>(Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, 0.1F).serialize());
        transmogEffects.add(ModModels.Armor.CELESTIAL.getModel().getId().toString(), celestialEffects);

        JsonArray spaceMarineEffects = new JsonArray();
        spaceMarineEffects.add(new HideElytraTransmogEffect().serialize());
        spaceMarineEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityManaCostPercentage("Dash", -0.25F)).serialize());
        spaceMarineEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.abilityLevel("Dash", 1)).serialize());
        spaceMarineEffects.add(new AttributeTransmogEffect<>(VaultGearAttributeHelper.potionEffect(MobEffects.DAMAGE_BOOST, 10)).serialize());
        spaceMarineEffects.add(new AttributeTransmogEffect<>(new VaultGearAttributeInstance<>(ModGearAttributes.RESISTANCE, 0.1F)).serialize());
        spaceMarineEffects.add(new VanillaAttributeArmorTransmogEffect<>(ModAttributes.SIZE_SCALE, AttributeModifier.Operation.MULTIPLY_TOTAL, 0.25F).serialize());
        transmogEffects.add(ModModels.Armor.SPACE_MARINE.getModel().getId().toString(), spaceMarineEffects);

        JsonArray bokatanEffects = new JsonArray();
        bokatanEffects.add(new HideElytraTransmogEffect().serialize());
        transmogEffects.add(ModModels.Armor.BOKATAN.getModel().getId().toString(), bokatanEffects);
    }

    @Override
    public String getName() {
        return "vaultadditions_transmog_effects";
    }
}
