package io.github.a1qs.vaultadditions.client.render;

import io.github.a1qs.vaultadditions.client.particle.ShieldWallParticle;
import io.github.a1qs.vaultadditions.init.ModEffects;
import io.github.a1qs.vaultadditions.init.ModParticles;
import iskallia.vault.client.particles.EntityLockedParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ShieldWallRenderer {


    @SubscribeEvent
    public static void onTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof Player player) {
            if (player.level.isClientSide && player.hasEffect(ModEffects.SHIELD_WALL)) {
                if (player != Minecraft.getInstance().player) {
                    return;
                }

                float range = 1F;

                int color = 15186688;
                float red = (float)(color >>> 16 & 255) / 255.0F;
                float green = (float)(color >>> 8 & 255) / 255.0F;
                float blue = (float)(color & 255) / 255.0F;
                ParticleEngine pm = Minecraft.getInstance().particleEngine;

                for(int i = 0; i < 3; ++i) {
                    float rotation = (float)Math.toRadians(((float)(player.tickCount % 90) * 4.0F + (float)(120 * i)));
                    new Vec3((double)range * Math.cos(rotation), 0.0F, (double)range * Math.sin(rotation));
                    Particle particle = pm.createParticle(ModParticles.SHIELD_WALL_PARTICLE.get(), 0.0F, 0.0F, 0.0F, range, (float)Math.toRadians((float)(player.tickCount % 90) * 4.0F + (float)(120 * i)), 0.0F);
                    if (particle instanceof ShieldWallParticle entityLockedParticle) {
                        particle.setColor(Mth.clamp(red, 0.0F, 1.0F), Mth.clamp(green, 0.0F, 1.0F), Mth.clamp(blue, 0.0F, 1.0F));
                        entityLockedParticle.setEntity(player);
                        entityLockedParticle.setPosO();



                    }
                }
            }
        }
    }

}
