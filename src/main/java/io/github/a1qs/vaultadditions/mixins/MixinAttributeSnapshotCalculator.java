package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.ArmorEffectRegistry;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.effect.ArmorSetEffect;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.effect.VanillaAttributeArmorEffect;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.talent.GearAttributeSkill;
import iskallia.vault.snapshot.AttributeSnapshot;
import iskallia.vault.snapshot.AttributeSnapshotCalculator;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = AttributeSnapshotCalculator.class, remap = false)
public class MixinAttributeSnapshotCalculator {
    @Unique
    private static final Set<UUID> trackedPlayers = new HashSet<>();

    @Inject(method = "computeSnapshot", at = @At(value = "INVOKE", target = "Liskallia/vault/snapshot/AttributeSnapshotCalculator;addExpertiseInformationToSnapshot(Lnet/minecraft/server/level/ServerPlayer;Liskallia/vault/snapshot/AttributeSnapshot;)V"))
    private static void injectPowerCompute(ServerPlayer player, AttributeSnapshot snapshot, CallbackInfo ci) {
        vaultadditions$addPowerInformationToSnapshot(player, snapshot);

    }

    @Inject(method = "computeSnapshot", at = @At("HEAD"))
    private static void injectArmorSetCompute(ServerPlayer player, AttributeSnapshot snapshot, CallbackInfo ci) {
        vaultadditions$addArmorSetEffects(player, snapshot);
    }

    @Unique
    private static void vaultadditions$addPowerInformationToSnapshot(ServerPlayer player, AttributeSnapshot snapshot) {
        PowerTree expertise = PlayerPowersData.get(player.getLevel()).getPowers(player);
        expertise.iterate(GearAttributeSkill.class, (attributeSkill) -> {
            if (attributeSkill instanceof Skill skill) {
                if (skill.isUnlocked()) {
                    attributeSkill.getGearAttributes(MiscUtil.ofPowers(player)).forEach((attributeValue) -> {
                        Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
                        VaultGearAttribute<?> attribute = attributeValue.getAttribute();
                        AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
                                attribute,
                                (attr) -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
                        );

                        InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
                        snapshotInvoker.invokeAddCachedValue(attributeValue.getValue());
                    });
                }
            }

        });
    }

    @Unique
    private static void vaultadditions$addArmorSetEffects(ServerPlayer player, AttributeSnapshot snapshot) {
        boolean hasEffect = false;

        for(Map.Entry<ArmorModel, List<ArmorSetEffect>> obj : ArmorEffectRegistry.getArmorEffects().entrySet()) {
            if(!ModelUtil.isWearingArmorSet(obj.getKey(), player)) continue;

            if(ArmorEffectRegistry.getEffectsForArmor(obj.getKey()) != null) {
                List<ArmorSetEffect> effectList = ArmorEffectRegistry.getEffectsForArmor(obj.getKey());



                for(ArmorSetEffect effect : effectList) {
                    if (effect instanceof VanillaAttributeArmorEffect vanillaEffect) {
                        vanillaEffect.apply(player); // Apply effect to player
                    }
                    Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
                    VaultGearAttribute<?> attribute = effect.getVaultGearAttributeInstance().getAttribute();

                    AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
                            attribute,
                            (attr) -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
                    );
                    InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
                    snapshotInvoker.invokeAddCachedValue(effect.getVaultGearAttributeInstance().getValue());
                    hasEffect = true;
                }
            }
        }

        // Remove effects if player is no longer wearing a full armor set
        // this shit is black magic and i dont want to look at it
        // dont look at it either, it MIGHT work and im willing to take those odds
        if (!hasEffect && trackedPlayers.contains(player.getUUID())) {
            for (List<ArmorSetEffect> effects : ArmorEffectRegistry.getArmorEffects().values()) {
                for (ArmorSetEffect effect : effects) {
                    if (effect instanceof VanillaAttributeArmorEffect vanillaEffect) {
                        vanillaEffect.remove(player);
                    }
                }
            }
            trackedPlayers.remove(player.getUUID());
        } else if (hasEffect) {
            trackedPlayers.add(player.getUUID());
        }

    }

    @Mixin(value = AttributeSnapshot.class, remap = false)
    public interface AccessorAttributeSnapshot {
        @Accessor
        Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> getGearAttributeValues();

    }

    @Mixin(targets = "iskallia.vault.snapshot.AttributeSnapshot$AttributeValue")
    public interface InvokeAttributeSnapshotAttributeValue {
        @Invoker("<init>")
        static AttributeSnapshot.AttributeValue invokeConstructor() {
            throw new AssertionError();
        }

        @Invoker("addCachedValue")
        void invokeAddCachedValue(Object object);

        @Accessor("cachedValues")
        List<?> getCachedValues();
    }
}
