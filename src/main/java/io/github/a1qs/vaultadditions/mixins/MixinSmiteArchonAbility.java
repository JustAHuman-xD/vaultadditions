package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.util.ModelUtil;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.SmiteArchonAbility;
import iskallia.vault.skill.ability.effect.spi.AbstractSmiteAbility;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = SmiteArchonAbility.class, remap = false)
public class MixinSmiteArchonAbility extends AbstractSmiteAbility {

    @Override
    protected void doToggleSound(SkillContext context) {
        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            if (this.isActive()) {
                if(ModelUtil.isWearingHoySet(player)) {
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SMITE, SoundSource.PLAYERS, 0.5F, 1.0F);
                    player.playNotifySound(io.github.a1qs.vaultadditions.init.ModSounds.HOY_ACTIVATE_SMITE_ARCHON.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
                    return;
                }

                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SMITE, SoundSource.PLAYERS, 0.5F, 1.0F);
                player.playNotifySound(ModSounds.SMITE, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

        });


    }
}
