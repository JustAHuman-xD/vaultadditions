package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.config.Configs;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.effect.AttributeTransmogEffect;
import io.github.a1qs.vaultadditions.vault.gear.effect.VanillaAttributeArmorTransmogEffect;
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
    @Unique private static final Map<UUID, List<AttributeTransmogEffect>> vaultadditions$setEffects = new HashMap<>();

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
        expertise.iterate(GearAttributeSkill.class,attributeSkill -> {
            if (attributeSkill instanceof Skill skill && skill.isUnlocked()) {
                attributeSkill.getGearAttributes(MiscUtil.ofPowers(player)).forEach(attributeValue -> {
                    Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
                    VaultGearAttribute<?> attribute = attributeValue.getAttribute();
                    AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
                            attribute,
                            attr -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
                    );

                    InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
                    snapshotInvoker.invokeAddCachedValue(attributeValue.getValue());
                });
            }
        });
    }

    @Unique
    private static void vaultadditions$addArmorSetEffects(ServerPlayer player, AttributeSnapshot snapshot) {
        ArmorModel wornSet = ModelUtil.getWornSet(player);
        List<AttributeTransmogEffect> effects = Configs.TRANSMOG_EFFECTS_CONFIG.getEffects(wornSet, AttributeTransmogEffect.class);
        for (AttributeTransmogEffect effect : effects) {
            if (effect instanceof VanillaAttributeArmorTransmogEffect vanillaEffect) {
                vanillaEffect.apply(player);
            }

            Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
            VaultGearAttribute<?> attribute = effect.getVaultGearAttributeInstance().getAttribute();
            AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
                    attribute,
                    attr -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
            );
            InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
            snapshotInvoker.invokeAddCachedValue(effect.getVaultGearAttributeInstance().getValue());
        }

        List<AttributeTransmogEffect> oldEffects = vaultadditions$setEffects.remove(player.getUUID());
        oldEffects.removeAll(effects);
        for (AttributeTransmogEffect effect : oldEffects) {
            if (effect instanceof VanillaAttributeArmorTransmogEffect vanillaEffect) {
                vanillaEffect.remove(player);
            }
        }
        vaultadditions$setEffects.put(player.getUUID(), effects);
    }

    @Mixin(value = AttributeSnapshot.class, remap = false)
    public interface AccessorAttributeSnapshot {
        @Accessor
        Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> getGearAttributeValues();
    }

    @Mixin(AttributeSnapshot.AttributeValue.class)
    public interface InvokeAttributeSnapshotAttributeValue {
        @Invoker("<init>")
        static AttributeSnapshot.AttributeValue invokeConstructor() {
            throw new AssertionError();
        }

        @Invoker(value = "addCachedValue", remap = false)
        void invokeAddCachedValue(Object object);
    }
}
