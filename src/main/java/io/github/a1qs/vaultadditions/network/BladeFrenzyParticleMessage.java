package io.github.a1qs.vaultadditions.network;

import iskallia.vault.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class BladeFrenzyParticleMessage {
    private final Vec3 playerPos;
    private final float radius;

    public BladeFrenzyParticleMessage(Vec3 playerPos, float radius) {
        this.playerPos = playerPos;
        this.radius = radius;
    }

    public static void encode(BladeFrenzyParticleMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.playerPos.x);
        buffer.writeDouble(message.playerPos.y);
        buffer.writeDouble(message.playerPos.z);
        buffer.writeFloat(message.radius);
    }

    public static BladeFrenzyParticleMessage decode(FriendlyByteBuf buffer) {
        return new BladeFrenzyParticleMessage(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), buffer.readFloat());
    }

    public static void handle(BladeFrenzyParticleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                spawnParticles(message.playerPos, message.radius);
            }

        });
        context.setPacketHandled(true);
    }

//    @OnlyIn(Dist.CLIENT)
//    public static void spawnParticles(Vec3 pos, float radius) {
//        Level level = Minecraft.getInstance().level;
//        if (level != null) {
//            Random random = new Random();
//            ParticleEngine pm = Minecraft.getInstance().particleEngine;
//            int color = 15417396;
//            float red = (float)(color >>> 16 & 255) / 255.0F;
//            float green = (float)(color >>> 8 & 255) / 255.0F;
//            float blue = (float)(color & 255) / 255.0F;
//
//            int color1 = 16539420;
//            float red1 = (float)(color1 >>> 16 & 255) / 255.0F;
//            float green1 = (float)(color1 >>> 8 & 255) / 255.0F;
//            float blue1 = (float)(color1 & 255) / 255.0F;
//
//
//            for(int i = 0; i < 140; ++i) {
//                float rotation = random.nextFloat() * 360.0F;
//
//                Vec3 offset = new Vec3((double)(radius / 3.0F) * Math.cos(rotation), 0.0F, (double)(radius / 3.0F) * Math.sin(rotation));
//                Particle p = pm.createParticle(ModParticles.NOVA_CLOUD.get(), pos.x() + offset.x, pos.y() + random.nextDouble() * (double)0.15F, pos.z() + offset.z, offset.x / (double)6.0F, random.nextDouble() * (double)0.125F, offset.z / (double)6.0F);
//                p.setColor(Mth.clamp(red, 0.0F, 1.0F), Mth.clamp(green, 0.0F, 1.0F), Mth.clamp(blue, 0.0F, 1.0F));
//
//                offset = new Vec3((double)(radius / 6.0F) * Math.cos(rotation), 0.0F, (double)(radius / 6.0F) * Math.sin(rotation));
//                Particle p1 = pm.createParticle(ModParticles.NOVA_CLOUD.get(), pos.x() + offset.x, pos.y() + random.nextDouble() * (double)0.15F, pos.z() + offset.z, offset.x / (double)8.0F, random.nextDouble() * (double)0.125F, offset.z / (double)8.0F);
//                p1.setColor(Mth.clamp(red1, 0.0F, 1.0F), Mth.clamp(green1, 0.0F, 1.0F), Mth.clamp(blue1, 0.0F, 1.0F));
//
//            }
//        }
//    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnParticles(Vec3 pos, float radius) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Random random = new Random();
            ParticleEngine pm = Minecraft.getInstance().particleEngine;

            int count = 150;
            for (int i = 0; i < count; i++) {
                float rotation = random.nextFloat() * 360.0F;
                float speed = random.nextFloat() * 0.1F + 0.05F;
                float mix = random.nextFloat();
                Vec3 offset = new Vec3((radius / 2.0F) * Math.cos(Math.toRadians(rotation)), random.nextDouble() * 0.2F - 0.1F, (radius / 2.0F) * Math.sin(Math.toRadians(rotation)));

                Particle nova = pm.createParticle(
                        ModParticles.NOVA_CLOUD.get(),
                        pos.x() + offset.x,
                        pos.y() + random.nextDouble() * 0.2F + offset.y,
                        pos.z() + offset.z,
                        offset.x * speed,
                        random.nextDouble() * 0.1F,
                        offset.z * speed
                );
                nova.setColor(Mth.lerp(mix, 1.0F, 0.6F), Mth.lerp(mix, 0.2F, 0.8F), Mth.lerp(mix, 0.3F, 1.0F));
                nova.scale(2.0F + random.nextFloat() * 0.5F);

                Vec3 pylonOffset = offset.scale(1.5);
                Particle pylon = pm.createParticle(
                        ModParticles.UBER_PYLON.get(),
                        pos.x() + pylonOffset.x,
                        pos.y() + random.nextDouble() * 0.2F + pylonOffset.y,
                        pos.z() + pylonOffset.z,
                        pylonOffset.x * speed,
                        random.nextDouble() * 0.1F *2,
                        pylonOffset.z * speed
                );
                pylon.setColor(Mth.lerp(mix, 1.0F, 0.6F), Mth.lerp(mix, 0.2F, 0.8F), Mth.lerp(mix, 0.3F, 1.0F));
                pylon.scale(2.0F + random.nextFloat() * 0.5F);

                pm.createParticle(
                        ModParticles.LUCKY_HIT_LEECH.get(),
                        pos.x() + offset.x,
                        pos.y() + random.nextDouble() * 0.2F + offset.y,
                        pos.z() + offset.z,
                        offset.x * speed,
                        random.nextDouble() * 0.1F,
                        offset.z * speed
                ).scale(1F + random.nextFloat() * 0.5F);

                pm.createParticle(
                        ParticleTypes.CRIMSON_SPORE,
                        pos.x() + offset.x,
                        pos.y() + random.nextDouble() * 0.2F + offset.y,
                        pos.z() + offset.z,
                        offset.x * speed,
                        random.nextDouble() * 0.1F,
                        offset.z * speed
                ).scale(1F + random.nextFloat() * 0.5F);
            }
        }
    }
}
