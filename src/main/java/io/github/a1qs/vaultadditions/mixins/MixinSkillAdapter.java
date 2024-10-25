package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import io.github.a1qs.vaultadditions.vault.powers.*;
import iskallia.vault.skill.base.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Skill.Adapter.class, remap = false)
public class MixinSkillAdapter {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onAdapterInit(CallbackInfo ci) {
        Skill.Adapter instance = ((Skill.Adapter) (Object)this);
        instance.register("vaultadditions_expertises", SpecialExpertiseTree.class, SpecialExpertiseTree::new);

        instance.register("power_skill_point_increase", SkillPointIncreasePower.class, SkillPointIncreasePower::new);
        instance.register("power_expertise_point_increase", ExpertisePointIncreasePower.class, ExpertisePointIncreasePower::new);

        instance.register("power_vanilla_attribute", VanillaAttributePower.class, VanillaAttributePower::new);
        instance.register("power_vanilla_attribute_2", VanillaAttributePowerAdditional.class, VanillaAttributePowerAdditional::new);
        instance.register("power_gear_attribute", GearAttributePower.class, GearAttributePower::new);
        instance.register("power_gear_attribute_2", GearAttributePowerAdditional.class, GearAttributePowerAdditional::new);
    }
}
