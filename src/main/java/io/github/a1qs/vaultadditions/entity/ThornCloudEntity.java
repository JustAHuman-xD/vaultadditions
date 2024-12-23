package io.github.a1qs.vaultadditions.entity;

import io.github.a1qs.vaultadditions.init.ModEntities;
import iskallia.vault.entity.entity.EternalEntity;
import iskallia.vault.init.ModParticles;
import iskallia.vault.world.data.VaultPartyData;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class ThornCloudEntity extends Entity {
    private static final EntityDataAccessor<Float> RADIUS  = SynchedEntityData.defineId(ThornCloudEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IGNORE_RADIUS = SynchedEntityData.defineId(ThornCloudEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(ThornCloudEntity.class, EntityDataSerializers.FLOAT);

    private int duration; // Duration of the Cloud
    private int waitTime; // Time to wait until being effective
    private int damageDelay; // Damage Delay of the Cloud
    private LivingEntity owner; // Owner of the Cloud
    private UUID ownerUniqueId; // UUID of the Owner

    public ThornCloudEntity(EntityType<? extends ThornCloudEntity> cloud, Level world) {
        super(cloud, world);
        this.duration = 600;
        this.waitTime = 20;
        this.damageDelay = 20;
        this.noPhysics = true;
        this.setRadius(3.0F);
    }

    public ThornCloudEntity(Level world, double x, double y, double z) {
        this(ModEntities.THORN_CLOUD, world);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(RADIUS, 0.5F);
        this.getEntityData().define(IGNORE_RADIUS, false);
        this.getEntityData().define(DAMAGE, 1.0F); // Initialize the DAMAGE field with a default value
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radiusIn) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(RADIUS, radiusIn);
        }
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    protected void setIgnoreRadius(boolean ignoreRadius) {
        this.getEntityData().set(IGNORE_RADIUS, ignoreRadius);
    }

    public boolean shouldIgnoreRadius() {
        return this.getEntityData().get(IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    public void setDamageDelay(int damageDelay) {
        this.damageDelay = damageDelay;
    }

    public void setDamage(float damage) {
        this.getEntityData().set(DAMAGE, damage);
    }

    public float getDamage() {
        return this.getEntityData().get(DAMAGE);
    }


    public void setRemainingFireTicks(int seconds) {
        super.setRemainingFireTicks(0);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.tickParticles();
        } else {
            boolean ignoreRadius = this.shouldIgnoreRadius();
            float radius = this.getRadius();

            if (this.tickCount >= this.waitTime + this.duration) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }

            boolean flag1 = this.tickCount < this.waitTime;
            if (ignoreRadius != flag1) {
                this.setIgnoreRadius(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.tickCount % this.damageDelay == 0) {
                List<LivingEntity> entitiesInRadius = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));

                for (LivingEntity entity : entitiesInRadius) {
                    if (canApplyEffects(entity)) {
                        entity.hurt(DamageSource.thorns(this), this.getDamage());
                        entity.invulnerableTime = 0;
                        entity.setDeltaMovement(0, 0, 0);

                        level.playSound(null, entity.blockPosition(), SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 0.4F, 1.0F);
                    }
                }
            }
        }
    }

    private int summonTimer = 0; // Timer to track particle summon phase

    private void tickParticles() {
        // Get the current bounding box of the entity
        var boundingBox = this.getBoundingBox();

        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;
        double groundY = boundingBox.minY; // Ground level
        double staticY = (boundingBox.minY + boundingBox.maxY) / 2.0; // Middle of the entity height

        // Use the radius of the bounding box to determine the circle size
        double radius = Math.max(boundingBox.maxX - boundingBox.minX, boundingBox.maxZ - boundingBox.minZ) / 2.0;

        // Number of particles around the circle
        int particleCount = 10;

        ParticleEngine pm = Minecraft.getInstance().particleEngine;

        // Phase 1: Rising Phase
        if (summonTimer < 10) { // Rising phase
            double progress = summonTimer / 10.0; // Progress from 0.0 to 1.0
            double currentY = groundY + progress * (staticY - groundY); // Interpolate Y position

            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount; // Angle in radians
                double x = centerX + radius * Math.cos(angle);
                double z = centerZ + radius * Math.sin(angle);

                Particle particle = pm.createParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, currentY, z, 0.0D, 0.05D, 0.0D);
                if (particle != null) {
                    float mix = (float) progress; // Progress determines the color transition
                    particle.setColor(
                            Mth.lerp(mix, 1.0F, 0.8F), // Red: high intensity for warm colors
                            Mth.lerp(mix, 0.8F, 0.7F), // Green: medium to high intensity
                            Mth.lerp(mix, 0.3F, 0.1F)  // Blue: low intensity for orange/yellow, near 0 for warm
                    );
                    particle.setLifetime(10);
                    particle.scale(2.0F);
                }
            }

            summonTimer++;
        } else { // Rotating and Shaking phase
            double currentY = staticY; // Particles stay at the static height

            for (int i = 0; i < particleCount; i++) {
                // Create a base angle for rotation
                double angle = (2 * Math.PI * i / particleCount) + (summonTimer * 0.1); // Gradually increase the angle to make particles rotate
                double x = centerX + radius * Math.cos(angle);
                double z = centerZ + radius * Math.sin(angle);

                // Add some randomness to simulate shaking
                double shakeX = (Math.random() - 0.5) * 0.1; // Small random offset
                double shakeZ = (Math.random() - 0.5) * 0.1;

                Particle particle = pm.createParticle(ParticleTypes.CRIT, x + shakeX, currentY + 0.75, z + shakeZ, 0.0D, 0.0D, 0.0D);
                if (particle != null) {
                    float mix = 0.5F + (float) Math.sin(summonTimer * 0.1) * 0.5F; // Oscillating mix value for dynamic color
                    particle.setColor(
                            Mth.lerp(mix, 1.0F, 0.8F), // Red
                            Mth.lerp(mix, 0.8F, 0.7F), // Green
                            Mth.lerp(mix, 0.3F, 0.1F)  // Blue
                    );
                    particle.scale(2.0F);
                }
            }

            summonTimer++;
        }
    }

    protected boolean canApplyEffects(LivingEntity target) {
        if (this.ownerUniqueId == null) {
            return true;
        }

        UUID targetUUID = target.getUUID();
        if (targetUUID.equals(this.ownerUniqueId)) {
            return false;
        }

        UUID ownerUUID = this.ownerUniqueId;
        Level world = this.getCommandSenderWorld();
        if (!(world instanceof ServerLevel serverLevel)) {
            return true;
        }

        if (target instanceof EternalEntity) {
            UUID eternalTargetOwnerUUID = ((EternalEntity)target).getOwner().map(Function.identity(), Entity::getUUID);
            if (eternalTargetOwnerUUID.equals(ownerUUID)) {
                return false;
            }

            VaultPartyData.Party party = VaultPartyData.get(serverLevel).getParty(eternalTargetOwnerUUID).orElse(null);
            if (party != null && party.hasMember(ownerUUID)) {
                return false;
            }
        }

        if (target instanceof TamableAnimal tamable) {
            if (ownerUUID.equals(tamable.getOwnerUUID())) {
                return false;
            }
        }

        VaultPartyData.Party party = VaultPartyData.get(serverLevel).getParty(ownerUUID).orElse(null);
        return party == null || !party.hasMember(targetUUID);
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.tickCount = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.damageDelay = compound.getInt("DamageDelay");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("DamageDelay", this.damageDelay);
        compound.putFloat("Radius", this.getRadius());

        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }

    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (RADIUS.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return false;
    }
}

