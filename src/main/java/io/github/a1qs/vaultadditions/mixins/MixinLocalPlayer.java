package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.client.sound.CustomElytraSoundInstance;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.AdditionalArmorModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {

    @ModifyArg(method = "onSyncedDataUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private SoundInstance elytraSound(SoundInstance pSound) {
        if (ModelUtil.getWornSet(cast()) instanceof AdditionalArmorModel model) {
            return new CustomElytraSoundInstance(cast(), model.getElytraSound(), model.getElytraVolumeModifier());
        }
        return pSound;
    }

    @Unique
    private LocalPlayer cast() {
        return (LocalPlayer) (Object) this;
    }
}
