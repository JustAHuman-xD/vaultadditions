package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.test.PlayerSpecialExpertiseData;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import io.github.a1qs.vaultadditions.vault.powers.PowerConfigs;
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

import java.util.Map;

@Mixin(value = AttributeSnapshotCalculator.class, remap = false)
public class MixinAttributeSnapshotCalculator {
    @Inject(method = "computeSnapshot", at = @At(value = "INVOKE", target = "Liskallia/vault/snapshot/AttributeSnapshotCalculator;addExpertiseInformationToSnapshot(Lnet/minecraft/server/level/ServerPlayer;Liskallia/vault/snapshot/AttributeSnapshot;)V"))
    private static void injectPowerCompute(ServerPlayer player, AttributeSnapshot snapshot, CallbackInfo ci) {
        vaultadditions$addSpecialExpertiseInformationToSnapshot(player, snapshot);
    }

    @Unique
    private static void vaultadditions$addSpecialExpertiseInformationToSnapshot(ServerPlayer player, AttributeSnapshot snapshot) {
        SpecialExpertiseTree expertise = PlayerSpecialExpertiseData.get(player.getLevel()).getSpecialExpertises(player);
        expertise.iterate(GearAttributeSkill.class, (attributeSkill) -> {
            if (attributeSkill instanceof Skill skill) {
                if (skill.isUnlocked()) {
                    attributeSkill.getGearAttributes(PowerConfigs.ofSpecialExpertise(player)).forEach((attributeValue) -> {
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
    }
}
