package io.github.a1qs.vaultadditions.client.sound;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomElytraSoundInstance extends ElytraOnPlayerSoundInstance {
    private final ResourceLocation soundLocation;
    private final float volumeModifier;

    public CustomElytraSoundInstance(LocalPlayer pPlayer, SoundEvent sound, float volumeModifier) {
        super(pPlayer);
        this.soundLocation = sound.getLocation();
        this.volumeModifier = volumeModifier;
    }

    public void tick() {
        super.tick();
        this.volume *= volumeModifier;
    }

    @Override
    public ResourceLocation getLocation() {
        return this.soundLocation;
    }

    @Override
    public WeighedSoundEvents resolve(SoundManager pHandler) {
        WeighedSoundEvents event = pHandler.getSoundEvent(this.location);
        this.sound = event == null
                ? SoundManager.EMPTY_SOUND
                : event.getSound();
        return event;
    }
}
