package io.github.a1qs.vaultadditions.vault.skill.ability;

import iskallia.vault.core.event.CommonEvents;
import iskallia.vault.init.ModEffects;
import iskallia.vault.mana.Mana;
import iskallia.vault.mana.ManaAction;
import iskallia.vault.skill.ability.effect.ShellPorcupineAbility;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.ability.effect.spi.core.InstantManaAbility;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.util.calc.EffectDurationHelper;
import iskallia.vault.util.calc.PlayerStat;
import iskallia.vault.util.calc.ThornsHelper;
import iskallia.vault.util.damage.DamageOverTimeHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class ThornedFrenzyAbility extends BladeFrenzyAbility {
    private static final Predicate<LivingEntity> MONSTER_PREDICATE = (entity) -> entity.getType().getCategory() == MobCategory.MONSTER;
    private int durationSeconds;

    public ThornedFrenzyAbility() {
    }

    public ThornedFrenzyAbility(int unlockLevel, int learnPointCost, int regretPointCost, int cooldownTicks, float manaCost, float radius, float percentAttackDealt, float knockbackStrengthMultiplier, int durationSeconds) {
        super(unlockLevel, learnPointCost, regretPointCost, cooldownTicks, manaCost, radius, percentAttackDealt, knockbackStrengthMultiplier);
        this.durationSeconds = durationSeconds;
    }

    public int getDurationSecondsUnmodified() {
        return this.durationSeconds;
    }

    public int getDurationTicks(LivingEntity entity) {
        int durationTicks = this.getDurationSecondsUnmodified() * 20;
        return EffectDurationHelper.adjustEffectDurationFloor(entity, (float)durationTicks);
    }

    @Override
    protected Ability.ActionResult doAction(SkillContext context) {
        return context.getSource().as(ServerPlayer.class).map((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());
            List<LivingEntity> targetEntities = this.getTargetEntities(player.level, player, pos);

            float attackDamage = this.getDamage(player); //TODO: thorns damage

            for(LivingEntity targetEntity : targetEntities) {
                DamageOverTimeHelper.invalidateAll(targetEntity);
                DamageOverTimeHelper.applyDamageOverTime(targetEntity, DamageSource.playerAttack(player), attackDamage, this.getDurationTicks(player));
                targetEntity.addEffect(new MobEffectInstance(ModEffects.NOVA_DOT, this.getDurationTicks(player), 0, false, true, false));
            } //TODO: thorns effect

            return ActionResult.successCooldownImmediate();
        }).orElse(ActionResult.fail());
    }





}
