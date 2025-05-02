package io.github.a1qs.vaultadditions.vault.gear.model.armor;

import io.github.a1qs.vaultadditions.util.SoundChoice;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdditionalArmorModel extends ArmorModel {
    private final Map<Class<? extends Ability>, List<SoundChoice>> abilitySounds = new HashMap<>();
    private final boolean hidesElytra;

    private SoundEvent elytraSound = SoundEvents.ELYTRA_FLYING;
    private float elytraVolumeModifier = 1;

    public AdditionalArmorModel(ResourceLocation id, String displayName, boolean hidesElytra) {
        super(id, displayName);
        this.hidesElytra = hidesElytra;
    }

    public void abilitySound(Class<? extends Ability> ability, SoundEvent sound) {
        abilitySound(ability, sound, -1, -1);
    }

    public void abilitySound(Class<? extends Ability> ability, SoundEvent sound, float volume, float pitch) {
        abilitySounds.compute(ability, (ab, sounds) -> {
            if (sounds == null) {
                sounds = new ArrayList<>();
            }
            sounds.add(new SoundChoice(sound, volume, pitch));
            return sounds;
        });
    }

    public void elytraSound(SoundEvent sound, float volumeModifier) {
        this.elytraSound = sound;
        this.elytraVolumeModifier = volumeModifier;
    }

    public SoundChoice getCustomSound(Class<? extends Ability> ability, SoundChoice def) {
        List<SoundChoice> sounds = abilitySounds.get(ability);
        return sounds == null ? def : sounds.get(0).extend(def);
    }

    public SoundChoice getCustomSound(Class<? extends Ability> ability, int index, SoundChoice def) {
        List<SoundChoice> sounds = abilitySounds.get(ability);
        return sounds == null || sounds.size() <= index ? def : sounds.get(index).extend(def);
    }

    public SoundEvent getElytraSound() {
        return elytraSound;
    }

    public float getElytraVolumeModifier() {
        return elytraVolumeModifier;
    }

    public boolean hidesElytra() {
        return this.hidesElytra;
    }
}
