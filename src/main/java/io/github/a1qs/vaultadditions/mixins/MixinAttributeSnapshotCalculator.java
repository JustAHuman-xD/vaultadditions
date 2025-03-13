package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.ArmorEffectRegistry;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.ArmorSetEffect;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
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

import java.util.List;
import java.util.Map;

@Mixin(value = AttributeSnapshotCalculator.class, remap = false)
public class MixinAttributeSnapshotCalculator {
    @Inject(method = "computeSnapshot", at = @At(value = "INVOKE", target = "Liskallia/vault/snapshot/AttributeSnapshotCalculator;addExpertiseInformationToSnapshot(Lnet/minecraft/server/level/ServerPlayer;Liskallia/vault/snapshot/AttributeSnapshot;)V"))
    private static void injectPowerCompute(ServerPlayer player, AttributeSnapshot snapshot, CallbackInfo ci) {
        vaultadditions$addPowerInformationToSnapshot(player, snapshot);
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
//        ArmorPieceModel model = ModelUtil.getWornSet(player);
//        if(model != null) {
//            List<ArmorSetEffect> effectList = ArmorEffectRegistry.getEffectsForArmor(model.getArmorModel());
//            for(ArmorSetEffect effect : effectList) {
//                Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
//                VaultGearAttribute<?> attribute = effect.getVaultGearAttributeInstance().getAttribute();
//
//                AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
//                        attribute,
//                        (attr) -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
//                );
//                InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
//                snapshotInvoker.invokeAddCachedValue(effect.getVaultGearAttributeInstance().getValue());
//            }
//        }

        for(Map.Entry<ArmorModel, List<ArmorSetEffect>> obj : ArmorEffectRegistry.getArmorEffects().entrySet()) {
            if(!ModelUtil.isWearingArmorSet(obj.getKey(), player)) return;

            if(ArmorEffectRegistry.getEffectsForArmor(obj.getKey()) != null) {
                List<ArmorSetEffect> effectList = ArmorEffectRegistry.getEffectsForArmor(obj.getKey());

                for(ArmorSetEffect effect : effectList) {
                    Map<VaultGearAttribute<?>, AttributeSnapshot.AttributeValue<?, ?>> gearAttributeValues = ((AccessorAttributeSnapshot) snapshot).getGearAttributeValues();
                    VaultGearAttribute<?> attribute = effect.getVaultGearAttributeInstance().getAttribute();

                    AttributeSnapshot.AttributeValue<?, ?> attributeSnapshotValue = gearAttributeValues.computeIfAbsent(
                            attribute,
                            (attr) -> InvokeAttributeSnapshotAttributeValue.invokeConstructor()
                    );
                    InvokeAttributeSnapshotAttributeValue snapshotInvoker = (InvokeAttributeSnapshotAttributeValue) attributeSnapshotValue;
                    snapshotInvoker.invokeAddCachedValue(effect.getVaultGearAttributeInstance().getValue());
                }
            }
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
