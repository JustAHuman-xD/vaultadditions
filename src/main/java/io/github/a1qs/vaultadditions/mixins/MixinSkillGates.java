package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.util.MiscUtil;
import iskallia.vault.skill.SkillGates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = SkillGates.class, remap = false)
public class MixinSkillGates {

    @ModifyVariable(
            method = "getAllSkillIds",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private List<String> addPowerSkills(List<String> allSkillIds) {
        MiscUtil.POWERS.getAll().skills.forEach((s) -> {
            allSkillIds.add(s.getId());
        });

        return allSkillIds;
    }
}
