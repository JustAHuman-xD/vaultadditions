package io.github.a1qs.vaultadditions.vault.gear.seteffect;

import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.effect.*;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

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
                ModModels.Armor.HOKAGE_ROBES,
                new AbilityModificationAttributeArmorEffect("Empower", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new VanillaAttributeArmorEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.HOKAGE_ROBES_MASKLESS,
                new AbilityModificationAttributeArmorEffect("Empower", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new VanillaAttributeArmorEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.HOY_82,
                new AbilityModificationAttributeArmorEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeArmorEffect("Smite_Archon", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.HOY_82_GROGU,
                new AbilityModificationAttributeArmorEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeArmorEffect("Smite_Archon", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
        );

        //TODO add new hoy set
//        ArmorEffectRegistry.registerArmorSet(
//                ModModels.Armor.HOY_82,
//                new AbilityModificationAttributeArmorEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
//                new AbilityModificationAttributeArmorEffect("Smite_Archon", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
//        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.BOKATAN,
                new AbilityModificationAttributeArmorEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeArmorEffect("Smite_Archon", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.VIKING,
                new EffectAttributeArmorEffect(MobEffects.DAMAGE_BOOST, 10),
                new AbilityAttributeArmorEffect("Rampage", 2)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.CELESTIAL,
                new AbilityModificationAttributeArmorEffect("Empower", -0.25F, AbilityModificationAttributeArmorEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeArmorEffect("Ghost_Walk", -0.5F, AbilityModificationAttributeArmorEffect.AbilityModification.COOLDOWN_REDUCTION_PERCENTAGE),
                new VanillaAttributeArmorEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.SPACE_MARINE,
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
