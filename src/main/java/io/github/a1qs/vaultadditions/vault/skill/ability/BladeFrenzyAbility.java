package io.github.a1qs.vaultadditions.vault.skill.ability;

import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.init.ModNetwork;
import io.github.a1qs.vaultadditions.init.ModSounds;
import io.github.a1qs.vaultadditions.network.BladeFrenzyParticleMessage;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.ability.effect.spi.core.InstantManaAbility;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.util.AABBHelper;
import iskallia.vault.util.calc.AreaOfEffectHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = VaultAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BladeFrenzyAbility extends InstantManaAbility {
    private float radius;
    private float percentAttackDealt;
    private float knockbackStrengthMultiplier;

    public BladeFrenzyAbility() {
    }

    public BladeFrenzyAbility(int unlockLevel, int learnPointCost, int regretPointCost, int cooldownTicks, float manaCost, float radius, float percentAttackDealt, float knockbackStrengthMultiplier) {
        super(unlockLevel, learnPointCost, regretPointCost, cooldownTicks, manaCost);
        this.radius = radius;
        this.percentAttackDealt = percentAttackDealt;
        this.knockbackStrengthMultiplier = knockbackStrengthMultiplier;
    }

    protected Ability.ActionResult doAction(SkillContext context) {
        return context.getSource().as(ServerPlayer.class).map((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());
            List<LivingEntity> targetEntities = this.getTargetEntities(player.level, player, pos);
            float attackDamage = this.getDamage(player);
            DamageSource damageSource = DamageSource.playerAttack(player);

            for(LivingEntity entity : targetEntities) {
                if (!entity.isInvulnerableTo(damageSource)) {
                    float damageModifier = this.getDamageModifier(this.getRadius(player), player.distanceTo(entity));
                    ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> {
                        if (entity.hurt(damageSource, attackDamage * damageModifier) && !Mth.equal(this.getKnockbackStrengthMultiplier(), 0.0F)) {
                            double dx = pos.x - entity.getX();
                            double dz = pos.z - entity.getZ();
                            if (dx * dx + dz * dz < 1.0E-4) {
                                dx = (Math.random() - Math.random()) * 0.01;
                                dz = (Math.random() - Math.random()) * 0.01;
                            }
                            entity.knockback((0.4F * this.getKnockbackStrengthMultiplier()), dx, dz);
                        }

                    });
                }
            }

            return ActionResult.successCooldownImmediate();
        }).orElse(ActionResult.fail());
    }


    public float getUnmodifiedRadius() {
        return this.radius;
    }

    public float getPercentAttackDealt() {
        return percentAttackDealt;
    }

    public float getKnockbackStrengthMultiplier() {
        return this.knockbackStrengthMultiplier;
    }


    private float getRadius(Entity attacker) {
        float realRadius = this.getUnmodifiedRadius();
        if (attacker instanceof LivingEntity livingEntity) {
            realRadius = AreaOfEffectHelper.adjustAreaOfEffect(livingEntity, this, realRadius);
        }

        return realRadius;
    }

    private float getDamage(ServerPlayer player) {
        return (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * this.getPercentAttackDealt();
    }

    private @NotNull List<LivingEntity> getTargetEntities(Level world, LivingEntity attacker, Vec3 pos) {
        float radius = this.getRadius(attacker);
        return world.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat().range(radius).selector((entity) -> !(entity instanceof Player)), attacker, AABBHelper.create(pos, radius));
    }

    protected float getDamageModifier(float radius, float dist) {
        if (dist >= 0.0F && dist < radius / 5.0F) {
            return 1.0F;
        } else if (dist >= radius / 5.0F && dist < radius / 5.0F * 2.0F) {
            return 0.8F;
        } else if (dist >= radius / 5.0F * 2.0F && dist < radius / 5.0F * 3.0F) {
            return 0.6F;
        } else if (dist >= radius / 5.0F * 3.0F && dist < radius / 5.0F * 4.0F) {
            return 0.4F;
        } else {
            return 0.2F;
        }
    }

    protected void doParticles(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());
            float radius = this.getRadius(player);
            ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new BladeFrenzyParticleMessage(new Vec3(pos.x, pos.y + (double)0.15F, pos.z), radius));
        });
    }

    @Override
    protected void doSound(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());
            player.level.playSound(player, pos.x, pos.y, pos.z, ModSounds.BLADE_FRENZY.get(), SoundSource.PLAYERS, 0.1F, 1.0F);
            player.playNotifySound(ModSounds.BLADE_FRENZY.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
        });
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.FLOAT.writeBits(this.radius, buffer);
        Adapters.FLOAT.writeBits(this.percentAttackDealt, buffer);
        Adapters.FLOAT.writeBits(this.knockbackStrengthMultiplier, buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        this.radius = Adapters.FLOAT.readBits(buffer).orElseThrow();
        this.percentAttackDealt = Adapters.FLOAT.readBits(buffer).orElseThrow();
        this.knockbackStrengthMultiplier = Adapters.FLOAT.readBits(buffer).orElseThrow();
    }

    @Override
    public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map(nbt -> {
            Adapters.FLOAT.writeNbt(this.radius).ifPresent(tag -> nbt.put("radius", tag));
            Adapters.FLOAT.writeNbt(this.percentAttackDealt).ifPresent(tag -> nbt.put("percentAttackDealt", tag));
            Adapters.FLOAT.writeNbt(this.knockbackStrengthMultiplier).ifPresent(tag -> nbt.put("knockbackStrengthMultiplier", tag));
            return nbt;
        });
    }

    @Override
    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.radius = Adapters.FLOAT.readNbt(nbt.get("radius")).orElse(0.0F);
        this.percentAttackDealt = Adapters.FLOAT.readNbt(nbt.get("percentAttackDealt")).orElse(0.0F);
        this.knockbackStrengthMultiplier = Adapters.FLOAT.readNbt(nbt.get("knockbackStrengthMultiplier")).orElse(0.0F);

    }

    @Override
    public Optional<JsonObject> writeJson() {
        return super.writeJson().map(json -> {
            Adapters.FLOAT.writeJson(this.radius).ifPresent(element -> json.add("radius", element));
            Adapters.FLOAT.writeJson(this.percentAttackDealt).ifPresent(element -> json.add("percentAttackDealt", element));
            Adapters.FLOAT.writeJson(this.knockbackStrengthMultiplier).ifPresent(element -> json.add("knockbackStrengthMultiplier", element));
            return json;
        });
    }

    @Override
    public void readJson(JsonObject json) {
        super.readJson(json);
        this.radius = Adapters.FLOAT.readJson(json.get("radius")).orElse(0.0F);
        this.percentAttackDealt = Adapters.FLOAT.readJson(json.get("percentAttackDealt")).orElse(0.0F);
        this.knockbackStrengthMultiplier = Adapters.FLOAT.readJson(json.get("knockbackStrengthMultiplier")).orElse(0.0F);
    }
}
