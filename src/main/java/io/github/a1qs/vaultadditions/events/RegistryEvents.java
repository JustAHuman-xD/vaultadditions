package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.init.ModEffects;
import io.github.a1qs.vaultadditions.init.ModEntities;
import io.github.a1qs.vaultadditions.init.ModModels;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.register(event);
    }
    @SubscribeEvent
    public static void onEffectRegister(RegistryEvent.Register<MobEffect> event) {
        ModEffects.registerEffects(event);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        ModModels.registerSounds();
    }
}
