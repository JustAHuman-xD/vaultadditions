package io.github.a1qs.vaultadditions.vault.skill.ability;

import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.entity.ThornCloudEntity;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.ability.effect.spi.core.InstantManaAbility;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.util.calc.EffectDurationHelper;
import iskallia.vault.util.calc.ThornsHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;


public class ThornedFrenzyAbility extends InstantManaAbility {
    private int durationSeconds;
    private float radius;
    private int damageDelay;
    private float thornsMultiplier;

    public ThornedFrenzyAbility() {
    }

    public ThornedFrenzyAbility(int unlockLevel, int learnPointCost, int regretPointCost, int cooldownTicks, float manaCost, int durationSeconds, float radius, int damageDelay, float thornsMultiplier) {
        super(unlockLevel, learnPointCost, regretPointCost, cooldownTicks, manaCost);
        this.durationSeconds = durationSeconds;
        this.radius = radius;
        this.damageDelay = damageDelay;
        this.thornsMultiplier = thornsMultiplier;
    }



    private float getThornsDamage(ServerPlayer player) {
        float reflectedDamage = 0.0F;
        float dmg = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float thornsMultiplier = ThornsHelper.getThornsDamageMultiplier(player);
        if (thornsMultiplier > 0.0F) {
            reflectedDamage += dmg * thornsMultiplier;
        }

        float additionalThornsDamage = ThornsHelper.getAdditionalThornsFlatDamage(player);
        reflectedDamage += additionalThornsDamage;
        reflectedDamage *= this.thornsMultiplier;
        return reflectedDamage;
    }

    public int getDurationTicks(LivingEntity entity) {
        int durationTicks = this.durationSeconds * 20;
        return EffectDurationHelper.adjustEffectDurationFloor(entity, (float)durationTicks);
    }

    @Override
    protected Ability.ActionResult doAction(SkillContext context) {
        return context.getSource().as(ServerPlayer.class).map((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());

            ThornCloudEntity thornCloud = new ThornCloudEntity(player.level, pos.x, pos.y, pos.z);
            thornCloud.setOwner(player);
            thornCloud.setRadius(radius / 2);
            thornCloud.setDuration(getDurationTicks(player));
            thornCloud.setDamageDelay(damageDelay);
            thornCloud.setDamage(this.getThornsDamage(player));
            thornCloud.setWaitTime(20);


            // Spawn the entity in the world
            player.level.addFreshEntity(thornCloud);

            return ActionResult.successCooldownImmediate();
        }).orElse(ActionResult.fail());
    }

    @Override
    protected void doSound(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            Vec3 pos = context.getSource().getPos().orElse(player.position());
            player.level.playSound(player, pos.x, pos.y, pos.z, ModSounds.TOTEM, SoundSource.PLAYERS, 0.1F, 1.0F);
            player.playNotifySound(ModSounds.TOTEM, SoundSource.PLAYERS,  0.3F, 1.0F);
        });
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        super.writeBits(buffer);
        Adapters.INT.writeBits(this.durationSeconds, buffer);
        Adapters.FLOAT.writeBits(this.radius, buffer);
        Adapters.INT.writeBits(this.damageDelay, buffer);
        Adapters.FLOAT.writeBits(this.thornsMultiplier, buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        super.readBits(buffer);
        this.durationSeconds = Adapters.INT.readBits(buffer).orElseThrow();
        this.radius = Adapters.FLOAT.readBits(buffer).orElseThrow();
        this.damageDelay = Adapters.INT.readBits(buffer).orElseThrow();
        this.thornsMultiplier = Adapters.FLOAT.readBits(buffer).orElseThrow();
    }

    @Override
    public Optional<CompoundTag> writeNbt() {
        return super.writeNbt().map(nbt -> {
            Adapters.INT.writeNbt(this.durationSeconds).ifPresent(tag -> nbt.put("durationSeconds", tag));
            Adapters.FLOAT.writeNbt(this.radius).ifPresent(tag -> nbt.put("radius", tag));
            Adapters.INT.writeNbt(this.damageDelay).ifPresent(tag -> nbt.put("damageDelay", tag));
            Adapters.FLOAT.writeNbt(this.thornsMultiplier).ifPresent(tag -> nbt.put("thornsMultiplier", tag));
            return nbt;
        });
    }

    @Override
    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.durationSeconds = Adapters.INT.readNbt(nbt.get("durationSeconds")).orElse(0);
        this.radius = Adapters.FLOAT.readNbt(nbt.get("radius")).orElse(0.0F);
        this.damageDelay = Adapters.INT.readNbt(nbt.get("damageDelay")).orElse(0);
        this.thornsMultiplier = Adapters.FLOAT.readNbt(nbt.get("thornsMultiplier")).orElse(0.0F);
    }

    @Override
    public Optional<JsonObject> writeJson() {
        return super.writeJson().map(json -> {
            Adapters.INT.writeJson(this.durationSeconds).ifPresent(element -> json.add("durationSeconds", element));
            Adapters.FLOAT.writeJson(this.radius).ifPresent(element -> json.add("radius", element));
            Adapters.INT.writeJson(this.damageDelay).ifPresent(element -> json.add("damageDelay", element));
            Adapters.FLOAT.writeJson(this.thornsMultiplier).ifPresent(element -> json.add("thornsMultiplier", element));
            return json;
        });
    }

    @Override
    public void readJson(JsonObject json) {
        super.readJson(json);
        this.durationSeconds = Adapters.INT.readJson(json.get("durationSeconds")).orElse(0);
        this.radius = Adapters.FLOAT.readJson(json.get("radius")).orElse(0.0F);
        this.damageDelay = Adapters.INT.readJson(json.get("damageDelay")).orElse(0);
        this.thornsMultiplier = Adapters.FLOAT.readJson(json.get("thornsMultiplier")).orElse(0.0F);
    }
}
