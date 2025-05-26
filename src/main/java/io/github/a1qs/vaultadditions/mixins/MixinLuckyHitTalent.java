package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.event.ActiveFlagsCheck;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.gear.VaultShieldItem;
import iskallia.vault.network.message.BonkParticleMessage;
import iskallia.vault.network.message.LuckyHitParticleMessage;
import iskallia.vault.skill.ability.effect.BonkLuckyStrikeAbility;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.talent.type.luckyhit.LuckyHitTalent;
import iskallia.vault.skill.talent.type.luckyhit.SweepingLuckyHitTalent;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.skill.tree.TalentTree;
import iskallia.vault.util.calc.LuckyHitHelper;
import iskallia.vault.util.damage.AttackScaleHelper;
import iskallia.vault.util.damage.CritHelper;
import iskallia.vault.util.damage.ThornsReflectDamageSource;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mixin(LuckyHitTalent.class)
@Debug(export = true)
public class MixinLuckyHitTalent {
    @Inject(method = "doLuckyHit", at = @At("HEAD"), cancellable = true)
    private static void doLuckyHit(LivingHurtEvent event, CallbackInfo ci) {
        VaultAdditions.LOGGER.info("Living Hit Event Triggered: {}", event.getSource().getMsgId());
        if (!ActiveFlagsCheck.isAnyFlagActiveLuckyHit()) {
            VaultAdditions.LOGGER.info("No Lucky Hit Flag Active");
            Entity source;
            ServerPlayer attacker;
            if (event.getSource() instanceof ThornsReflectDamageSource) {
                VaultAdditions.LOGGER.info("Thorns Reflect Damage Source Detected");
                source = event.getSource().getEntity();
                if (!(source instanceof ServerPlayer)) {
                    VaultAdditions.LOGGER.info("Source is not a ServerPlayer");
                    ci.cancel();
                    return;
                }

                attacker = (ServerPlayer)source;
                if (attacker.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()) {
                    VaultAdditions.LOGGER.info("Attacker's Offhand is Empty");
                    ci.cancel();
                    return;
                }

                ItemStack stack = attacker.getItemBySlot(EquipmentSlot.OFFHAND);
                if (!(stack.getItem() instanceof VaultShieldItem)) {
                    VaultAdditions.LOGGER.info("Attacker's Offhand Item is not a Vault Shield Item");
                    ci.cancel();
                    return;
                }

                VaultGearData data = VaultGearData.read(stack);
                if (!data.hasAttribute(ModGearAttributes.LUCKY_THORNS)) {
                    VaultAdditions.LOGGER.info("Vault Gear Data does not have Lucky Thorns Attribute");
                    ci.cancel();
                    return;
                }

                if (data.getFirstValue(ModGearAttributes.LUCKY_THORNS).isEmpty()) {
                    VaultAdditions.LOGGER.info("Lucky Thorns Attribute is not present or has no value");
                    ci.cancel();
                    return;
                }

                if (!(Boolean)data.getFirstValue(ModGearAttributes.LUCKY_THORNS).get()) {
                    VaultAdditions.LOGGER.info("Lucky Thorns Attribute is false");
                    ci.cancel();
                    return;
                }
            }

            source = event.getSource().getEntity();
            if (source instanceof ServerPlayer) {
                VaultAdditions.LOGGER.info("Source is a ServerPlayer");
                attacker = (ServerPlayer)source;
                if (!CritHelper.getCrit(attacker)) {
                    VaultAdditions.LOGGER.info("Attacker is not a Critical Hit");
                    if (!(AttackScaleHelper.getLastAttackScale(attacker) < 1.0F)) {
                        VaultAdditions.LOGGER.info("Attack Scale is not less than 1.0F");
                        float probability = LuckyHitHelper.getLuckyHitChance(attacker);
                        MobEffectInstance battleCry = attacker.getEffect(ModEffects.BATTLE_CRY_LUCKY_STRIKE);
                        if (battleCry != null) {
                            AbilityTree abilities = PlayerAbilitiesData.get((ServerLevel)attacker.level).getAbilities(attacker);
                            Iterator var6 = abilities.getAll(BonkLuckyStrikeAbility.class, Skill::isUnlocked).iterator();

                            while(var6.hasNext()) {
                                BonkLuckyStrikeAbility ability = (BonkLuckyStrikeAbility)var6.next();
                                int stacksUsed = ability.getMaxStacksUsedPerHit();
                                MobEffectInstance newBattleCry = null;
                                if (battleCry.getAmplifier() - stacksUsed >= 0) {
                                    newBattleCry = new MobEffectInstance(battleCry.getEffect(), battleCry.getDuration(), battleCry.getAmplifier() - stacksUsed, false, false, true);
                                } else {
                                    stacksUsed = battleCry.getAmplifier() + 1;
                                }

                                probability += ability.getLuckyHitChancePerStack() * (float)stacksUsed;
                                attacker.level.playSound(attacker, attacker.position().x, attacker.position().y, attacker.position().z, ModSounds.BONK, SoundSource.PLAYERS, 1.0F, 0.7F);
                                attacker.playNotifySound(ModSounds.BONK, SoundSource.PLAYERS, 1.0F, 0.7F);
                                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new BonkParticleMessage(new Vec3(attacker.getX(), attacker.getY() + (double)(attacker.getBbHeight() / 3.0F), attacker.getZ()), event.getEntity().getId(), 7206307, 5 * stacksUsed, 5 + (int)((new Random()).nextFloat() * 10.0F)));
                                attacker.removeEffect(ModEffects.BATTLE_CRY_LUCKY_STRIKE);
                                if (newBattleCry != null) {
                                    attacker.addEffect(newBattleCry);
                                }
                            }
                        }

                        VaultAdditions.LOGGER.info("Lucky Hit Probability: {}", probability);
                        float random = attacker.getLevel().getRandom().nextFloat();
                        VaultAdditions.LOGGER.info("Lucky Hit Random: {}", random);
                        if (!(random >= probability)) {
                            VaultAdditions.LOGGER.info("Lucky Hit Triggered");
                            TalentTree tree = PlayerTalentsData.get((ServerLevel)attacker.level).getTalents(attacker);
                            boolean hasLuckyHit = false;
                            List<LuckyHitTalent> luckyHitTalents = tree.getAll(LuckyHitTalent.class, Skill::isUnlocked);
                            Iterator var16 = luckyHitTalents.iterator();

                            SweepingLuckyHitTalent cleave;
                            LuckyHitTalent talent;
                            while(var16.hasNext()) {
                                talent = (LuckyHitTalent)var16.next();
                                if (talent instanceof SweepingLuckyHitTalent) {
                                    cleave = (SweepingLuckyHitTalent)talent;
                                    talent.onLuckyHit(event);
                                    hasLuckyHit = true;
                                }
                            }

                            var16 = luckyHitTalents.iterator();

                            while(var16.hasNext()) {
                                talent = (LuckyHitTalent)var16.next();
                                if (talent instanceof SweepingLuckyHitTalent) {
                                    cleave = (SweepingLuckyHitTalent)talent;
                                } else {
                                    talent.onLuckyHit(event);
                                    hasLuckyHit = true;
                                }
                            }

                            if (!hasLuckyHit) {
                                event.setAmount(event.getAmount() * 1.5F);
                            }

                            event.getEntity().getLevel().playSound((Player)null, event.getEntity(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.75F);
                            ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new LuckyHitParticleMessage(new Vec3(event.getEntity().getX(), event.getEntity().getY() + (double)event.getEntity().getBbHeight(), event.getEntity().getZ())));
                        }
                    }
                }
            }
        }
        ci.cancel();
    }

    @Unique
    private static void vaultadditions$rewrote(LivingHurtEvent event) {
        if (ActiveFlagsCheck.isAnyFlagActiveLuckyHit() || !(event.getSource().getEntity() instanceof ServerPlayer attacker)) {
            VaultAdditions.LOGGER.info("Flag {}, Wrong Attacker {}", ActiveFlagsCheck.isAnyFlagActiveLuckyHit(), !(event.getSource().getEntity() instanceof ServerPlayer));
            return;
        }

        if (event.getSource() instanceof ThornsReflectDamageSource) {
            VaultAdditions.LOGGER.info("Thorns Reflect Damage Source");
            ItemStack stack = attacker.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!(stack.getItem() instanceof VaultShieldItem)) {
                VaultAdditions.LOGGER.info("Not a Vault Shield Item");
                return;
            }

            VaultGearData data = VaultGearData.read(stack);
            Optional<Boolean> luckyThorns = data.getFirstValue(ModGearAttributes.LUCKY_THORNS);
            if (luckyThorns.isEmpty() || !luckyThorns.get()) {
                VaultAdditions.LOGGER.info("present {}, value {}", luckyThorns.isPresent(), luckyThorns.orElse(false));
                return;
            }
        }

        if (CritHelper.getCrit(attacker) || AttackScaleHelper.getLastAttackScale(attacker) < 1.0F) {
            VaultAdditions.LOGGER.info("Crit {}, Attack Scale {}", CritHelper.getCrit(attacker), AttackScaleHelper.getLastAttackScale(attacker));
            return;
        }

        float probability = LuckyHitHelper.getLuckyHitChance(attacker);
        MobEffectInstance battleCry = attacker.getEffect(ModEffects.BATTLE_CRY_LUCKY_STRIKE);
        if (battleCry != null) {
            AbilityTree abilities = PlayerAbilitiesData.get(attacker.getLevel()).getAbilities(attacker);
            for (BonkLuckyStrikeAbility ability : abilities.getAll(BonkLuckyStrikeAbility.class, Skill::isUnlocked)) {
                int stacksUsed = ability.getMaxStacksUsedPerHit();
                MobEffectInstance newBattleCry = null;
                if (battleCry.getAmplifier() - stacksUsed >= 0) {
                    newBattleCry = new MobEffectInstance(battleCry.getEffect(), battleCry.getDuration(), battleCry.getAmplifier() - stacksUsed, false, false, true);
                } else {
                    stacksUsed = battleCry.getAmplifier() + 1;
                }

                probability += ability.getLuckyHitChancePerStack() * (float) stacksUsed;
                attacker.level.playSound(attacker, attacker.position().x, attacker.position().y, attacker.position().z, ModSounds.BONK, SoundSource.PLAYERS, 1.0F, 0.7F);
                attacker.playNotifySound(ModSounds.BONK, SoundSource.PLAYERS, 1.0F, 0.7F);
                ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new BonkParticleMessage(new Vec3(attacker.getX(), attacker.getY() + (double) (attacker.getBbHeight() / 3.0F), attacker.getZ()), event.getEntity().getId(), 7206307, 5 * stacksUsed, 5 + (int) ((new Random()).nextFloat() * 10.0F)));
                attacker.removeEffect(ModEffects.BATTLE_CRY_LUCKY_STRIKE);
                if (newBattleCry != null) {
                    attacker.addEffect(newBattleCry);
                }
            }
        }

        VaultAdditions.LOGGER.info("Lucky Hit Probability: {}", probability);
        float random = attacker.getLevel().getRandom().nextFloat();
        VaultAdditions.LOGGER.info("Lucky Hit Random: {}", random);
        if (random < probability) {
            VaultAdditions.LOGGER.info("Lucky Hit Triggered");
            TalentTree tree = PlayerTalentsData.get(attacker.getLevel()).getTalents(attacker);
            List<LuckyHitTalent> luckyHitTalents = tree.getAll(LuckyHitTalent.class, Skill::isUnlocked);
            for (LuckyHitTalent talent : luckyHitTalents) {
                talent.onLuckyHit(event);
                VaultAdditions.LOGGER.info("Lucky Hit Talent Triggered: {}", talent.getClass().getSimpleName());
            }

            if (luckyHitTalents.isEmpty()) {
                event.setAmount(event.getAmount() * 1.5F);
                VaultAdditions.LOGGER.info("No Lucky Hit Talents, applying 1.5x damage");
            }

            event.getEntity().getLevel().playSound(null, event.getEntity(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.75F);
            ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new LuckyHitParticleMessage(new Vec3(event.getEntity().getX(), event.getEntity().getY() + (double)event.getEntity().getBbHeight(), event.getEntity().getZ())));
        }
    }
}
