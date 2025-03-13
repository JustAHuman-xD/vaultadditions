package io.github.a1qs.vaultadditions.vault.gear.armorseteffects;

import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.effect.AbilityAttributeArmorEffect;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.effect.EffectAttributeArmorEffect;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.effect.VaultAttributeArmorEffect;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.*;

public class ArmorEffectRegistry {
    private static final Map<ArmorModel, List<ArmorSetEffect>> ARMOR_EFFECTS = new HashMap<>();

    public static void registerArmorSet(ArmorModel model, ArmorSetEffect... effects) {
        ARMOR_EFFECTS.put(model, Arrays.asList(effects));
    }

    public static List<ArmorSetEffect> getEffectsForArmor(ArmorModel model) {
        return ARMOR_EFFECTS.getOrDefault(model, Collections.emptyList());
    }

    public static void registerArmorSetEffects() {
        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.SPACE_MARINE,
                new AbilityAttributeArmorEffect("Dash", 1)
                //new EffectAttributeArmorEffect(MobEffects.DAMAGE_BOOST, 9),
                //new VaultAttributeArmorEffect<>(ModGearAttributes.KNOCKBACK_RESISTANCE, 0.1F),
                //new VaultAttributeArmorEffect<>(ModGearAttributes.RESISTANCE, 0.1F)
        );
    }

    public static Map<ArmorModel, List<ArmorSetEffect>> getArmorEffects() {
        return ARMOR_EFFECTS;
    }
}
