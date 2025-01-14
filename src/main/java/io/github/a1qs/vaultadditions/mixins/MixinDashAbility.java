package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.init.ModSounds;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DashAbility.class, remap = false)
public class MixinDashAbility {

    @Inject(method = "doSound", at = @At("HEAD"), cancellable = true)
    private void injectSoundEvents(SkillContext context, CallbackInfo ci) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            if(ModelUtil.isWearingHoySet(player)) {
                player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.HOY_DASH.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
                player.playNotifySound(ModSounds.HOY_DASH.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
                ci.cancel();
            }
        });
    }
}
