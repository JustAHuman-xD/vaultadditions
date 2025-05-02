package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.util.SoundChoice;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.AdditionalArmorModel;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.SmiteArchonAbility;
import iskallia.vault.skill.ability.effect.spi.AbstractSmiteAbility;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = SmiteArchonAbility.class, remap = false)
public class MixinSmiteArchonAbility extends AbstractSmiteAbility {
    @Unique private static final SoundChoice ACTIVATE = new SoundChoice(ModSounds.SMITE, 0.5F, 1.0F);

    @Override
    protected void doToggleSound(SkillContext context) {
        if (!this.isActive()) {
            return;
        }

        context.getSource().as(ServerPlayer.class).ifPresent((player) -> {
            SoundChoice sound = ACTIVATE;
            if (ModelUtil.getWornSet(player) instanceof AdditionalArmorModel model) {
                sound = model.getCustomSound(SmiteArchonAbility.class, sound);
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), sound.event(), SoundSource.PLAYERS, sound.volume(), sound.pitch());
            player.playNotifySound(sound.event(), SoundSource.PLAYERS, sound.volume(), sound.pitch());
        });
    }
}
