package io.github.a1qs.vaultadditions.mixins;


import io.github.a1qs.vaultadditions.client.sound.CustomElytraSoundInstance;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {

    @Redirect(method = "onSyncedDataUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private void d(SoundManager instance, SoundInstance pSound) {
        LocalPlayer thisInstance = ((LocalPlayer) (Object) this);

        if(ModelUtil.isWearingHoySet(thisInstance)) {
            Minecraft.getInstance().getSoundManager().play(new CustomElytraSoundInstance(thisInstance));
        } else {
            Minecraft.getInstance().getSoundManager().play(new ElytraOnPlayerSoundInstance(thisInstance));
        }
    }
}
