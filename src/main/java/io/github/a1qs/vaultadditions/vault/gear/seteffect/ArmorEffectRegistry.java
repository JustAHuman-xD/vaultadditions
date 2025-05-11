package io.github.a1qs.vaultadditions.vault.gear.seteffect;

import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.effect.*;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorEffectRegistry {
    private static final Map<ArmorModel, List<ArmorSetEffect>> ARMOR_EFFECTS = new HashMap<>();

    public static void registerArmorSet(ArmorModel model, ArmorSetEffect... effects) {
        ARMOR_EFFECTS.put(model, Arrays.asList(effects));
    }

    public static List<ArmorSetEffect> getEffectsForArmor(ArmorModel model) {
        return model == null ? Collections.emptyList() : ARMOR_EFFECTS.getOrDefault(model, Collections.emptyList());
    }

    public static void registerArmorSetEffects() {
        for (ArmorModel model : ModModels.HOKAGE_ARMOR) {
            ArmorEffectRegistry.registerArmorSet(
                    model,
                    new AbilityModificationAttributeArmorEffect("Empower", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                    new VanillaAttributeArmorEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
            );
        }

        for (ArmorModel model : ModModels.HOY_ARMOR) {
            ArmorEffectRegistry.registerArmorSet(
                    model,
                    new AbilityModificationAttributeArmorEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                    new AbilityModificationAttributeArmorEffect("Smite_Archon", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
            );
        }

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.VIKING.getModel(),
                new EffectAttributeArmorEffect(MobEffects.DAMAGE_BOOST, 10),
                new AbilityAttributeArmorEffect("Rampage", 2)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.CELESTIAL.getModel(),
                new AbilityModificationAttributeArmorEffect("Empower", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeArmorEffect("Ghost_Walk", -0.5F, AbilityModificationAttributeArmorEffect.AbilityModification.COOLDOWN_REDUCTION_PERCENTAGE),
                new VanillaAttributeArmorEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.SPACE_MARINE.getModel(),
                new AbilityAttributeArmorEffect("Dash", 1),
                new EffectAttributeArmorEffect(MobEffects.DAMAGE_BOOST, 10),
                new VaultAttributeArmorEffect<>(ModGearAttributes.RESISTANCE, 0.1F),
                new VanillaAttributeArmorEffect(ModAttributes.SIZE_SCALE, 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
    }

    public static Map<ArmorModel, List<ArmorSetEffect>> getArmorEffects() {
        return ARMOR_EFFECTS;
    }
}
