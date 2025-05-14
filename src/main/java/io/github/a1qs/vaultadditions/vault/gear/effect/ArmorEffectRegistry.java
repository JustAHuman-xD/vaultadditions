package io.github.a1qs.vaultadditions.vault.gear.effect;

import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ArmorEffectRegistry {
    public static void registerArmorSetEffects() {
        for (ArmorModel model : ModModels.HOKAGE_ARMOR) {
            ArmorEffectRegistry.registerArmorSet(
                    model,
                    new AbilityModificationAttributeTransmogEffect("Empower", -0.25F, AbilityModificationAttributeTransmogEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                    new VanillaAttributeArmorTransmogEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
            );
        }

        for (ArmorModel model : ModModels.HOY_ARMOR) {
            ArmorEffectRegistry.registerArmorSet(
                    model,
                    new AbilityModificationAttributeTransmogEffect("Mana_Shield_Legacy", -0.25F, AbilityModificationAttributeTransmogEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                    new AbilityModificationAttributeTransmogEffect("Smite_Archon", -0.25F, AbilityModificationAttributeTransmogEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE)
            );
        }

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.VIKING.getModel(),
                new EffectAttributeTransmogEffect(MobEffects.DAMAGE_BOOST, 10),
                new AbilityAttributeTransmogEffect("Rampage", 2)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.CELESTIAL.getModel(),
                new AbilityModificationAttributeTransmogEffect("Empower", -0.25F, AbilityModificationAttributeTransmogEffect.AbilityModification.MANA_COST_REDUCTION_PERCENTAGE),
                new AbilityModificationAttributeTransmogEffect("Ghost_Walk", -0.5F, AbilityModificationAttributeTransmogEffect.AbilityModification.COOLDOWN_REDUCTION_PERCENTAGE),
                new VanillaAttributeArmorTransmogEffect(Attributes.MOVEMENT_SPEED, 0.1F, AttributeModifier.Operation.MULTIPLY_BASE)
        );

        ArmorEffectRegistry.registerArmorSet(
                ModModels.Armor.SPACE_MARINE.getModel(),
                new AbilityAttributeTransmogEffect("Dash", 1),
                new EffectAttributeTransmogEffect(MobEffects.DAMAGE_BOOST, 10),
                new VaultAttributeTransmogEffect<>(ModGearAttributes.RESISTANCE, 0.1F),
                new VanillaAttributeArmorTransmogEffect(ModAttributes.SIZE_SCALE, 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
    }
}
