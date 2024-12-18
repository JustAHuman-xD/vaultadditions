package io.github.a1qs.vaultadditions.client.render;

import com.github.alexthe666.citadel.Citadel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.a1qs.vaultadditions.client.particle.ShieldWallParticle;
import io.github.a1qs.vaultadditions.init.ModEffects;
import io.github.a1qs.vaultadditions.init.ModParticles;
import iskallia.vault.client.particles.EntityLockedParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
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

                float range = 1.2F;

                //int color = 15186688;
                int color = 7131872;
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


    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Pre<Player, PlayerModel<Player>> event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.hasEffect(ModEffects.SHIELD_WALL)) {
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource bufferSource = event.getMultiBufferSource();

            // Calculate positions around the player
            int shieldCount = 4; // Number of shields
            float range = 1.2F; // Distance from the player

            for (int i = 0; i < shieldCount; i++) {
                float angle = player.tickCount + event.getPartialTick() % 360 + (i * (360F / shieldCount));
                float xOffset = range * (float) Math.cos(Math.toRadians(angle));
                float zOffset = range * (float) Math.sin(Math.toRadians(angle));

                poseStack.pushPose();

                // Position the shield
                poseStack.translate(player.getX() + xOffset - event.getEntity().getX(),
                        player.getY() + 0.75F - event.getEntity().getY(),
                        player.getZ() + zOffset - event.getEntity().getZ());

                // Rotate to face outward
                poseStack.mulPose(Vector3f.YN.rotationDegrees(angle - 100F));
                float scale = 3.0F;
                poseStack.scale(scale, scale, scale);

                // Render the shield model
                renderShieldModel(poseStack, bufferSource);

                poseStack.popPose();
            }
        }
    }

    private static void renderShieldModel(PoseStack poseStack, MultiBufferSource bufferSource) {
        ItemStack shieldStack = new ItemStack(Items.SHIELD);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                shieldStack,
                ItemTransforms.TransformType.GROUND,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                0
        );
    }

}
