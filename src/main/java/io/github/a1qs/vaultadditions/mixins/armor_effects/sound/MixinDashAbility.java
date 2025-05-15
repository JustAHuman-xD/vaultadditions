package io.github.a1qs.vaultadditions.mixins.armor_effects.sound;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.Configs;
import io.github.a1qs.vaultadditions.util.SoundChoice;
import io.github.a1qs.vaultadditions.vault.gear.effect.AbilitySoundTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.TransmogEffect;
import iskallia.vault.init.ModAbilities;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DashAbility.class, remap = false)
public class MixinDashAbility extends Ability {

    @Inject(method = "doSound", at = @At("HEAD"), cancellable = true)
    private void injectSoundEvents(SkillContext context, CallbackInfo ci) {
        context.getSource().as(ServerPlayer.class).ifPresent(player -> {
            VaultAdditions.LOGGER.info("All Player Active Effects:");
            for (TransmogEffect effect : Configs.TRANSMOG_EFFECTS_CONFIG.getEffects(player)) {
                VaultAdditions.LOGGER.info("Effect: {}", effect.serialize().toString());
            }

            SoundChoice def = new SoundChoice(null, 0.2F, 1.0F);
            VaultAdditions.LOGGER.info("Dash Sound Default: {}", def);
            SoundChoice sound = AbilitySoundTransmogEffect.getSound(player, ModAbilities.DASH, def);
            VaultAdditions.LOGGER.info("Dash Sound Post Effect: {}", sound);
            if (sound.event() != null) {
                VaultAdditions.LOGGER.info("Playing Sound: {}", sound);
                player.level.playSound(player, player.getX(), player.getY(), player.getZ(), sound.event(), SoundSource.PLAYERS, sound.volume(), sound.pitch());
                player.playNotifySound(sound.event(), SoundSource.PLAYERS, sound.volume(), sound.pitch());
                ci.cancel();
            }
        });
    }
}
