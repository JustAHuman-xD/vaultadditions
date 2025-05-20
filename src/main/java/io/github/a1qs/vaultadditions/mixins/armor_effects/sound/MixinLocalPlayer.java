package io.github.a1qs.vaultadditions.mixins.armor_effects.sound;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.Configs;
import io.github.a1qs.vaultadditions.vault.gear.effect.ElytraSoundTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.TransmogEffect;
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
        VaultAdditions.LOGGER.info("All Player Active Effects:");
        for (TransmogEffect effect : Configs.TRANSMOG_EFFECTS_CONFIG.getEffects(cast())) {
            VaultAdditions.LOGGER.info("Effect: {}", effect);
        }
        ElytraSoundTransmogEffect effect = Configs.TRANSMOG_EFFECTS_CONFIG.getEffect(cast(), ElytraSoundTransmogEffect.class);
        VaultAdditions.LOGGER.info("Elytra Sound Effect: {}, instance {}", effect, effect == null ? "null" : effect.createSoundInstance(cast()));
        return effect != null ? effect.createSoundInstance(cast()) : pSound;
    }

    @Unique
    private LocalPlayer cast() {
        return (LocalPlayer) (Object) this;
    }
}
