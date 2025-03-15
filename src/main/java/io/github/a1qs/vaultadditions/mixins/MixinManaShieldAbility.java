package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.effect.ManaShieldAbility;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import net.minecraft.client.model.Model;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ManaShieldAbility.class)
public class MixinManaShieldAbility {

    @Inject(method = "lambda$doAction$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    public void injectSoundEvents(ServerPlayer player, CallbackInfoReturnable<Ability.ActionResult> cir) {
        if(player == null) {
            VaultAdditions.LOGGER.error("Player is null when trying to play sound for 'Mana Shield'!");
            return;
        }
        if(ModelUtil.isWearingHoySet(player)) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), io.github.a1qs.vaultadditions.init.ModSounds.HOY_ACTIVATE_MANASHIELD.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
            return;
        }

        if(ModelUtil.isWearingHokageRobesSet(player)) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), io.github.a1qs.vaultadditions.init.ModSounds.TIGER_ACTIVATE_MANASHIELD.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
            return;
        }

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MANA_SHIELD, SoundSource.PLAYERS, 0.2F, 0.2F);
    }


    @Inject(method = "on", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;removeModifier(Ljava/util/UUID;)V"))
    private static void modifyHitSound(LivingDamageEvent event, CallbackInfo ci) {
        Entity manaShieldAttribute = event.getEntity();
        if (manaShieldAttribute instanceof ServerPlayer player) {
            float pitch = 0.7F + player.getLevel().getRandom().nextFloat(0.6F);
            if(ModelUtil.isWearingHoySet(player)) {
                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), io.github.a1qs.vaultadditions.init.ModSounds.HOY_MANASHIELD_HIT.get(), SoundSource.PLAYERS, 0.1F, pitch);
                return;
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MANA_SHIELD_HIT, SoundSource.PLAYERS, 0.1F, pitch);

        }

    }

    @Redirect(method = "lambda$doAction$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void redirectSoundEvents(Level instance, Player player, double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float v4, float v5) {
    }

    @Redirect(method = "on", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void redirectSoundEvent(Level instance, Player player, double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float v4, float v5) {
    }

}
