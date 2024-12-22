package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.HoyArmorLayers;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.registry.ArmorPieceModelRegistry;

import iskallia.vault.init.ModDynamicModels;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModDynamicModels.Armor.class, remap = false)
public class MixinModDynamicModels$Armor {
    @Shadow @Final public static ArmorPieceModelRegistry PIECE_REGISTRY;

    private static final ArmorModel HOY_82 =
            PIECE_REGISTRY.registerAll(
            new ArmorModel(VaultMod.id("gear/armor/hoy"), "HoY_82")
                    .properties(new DynamicModelProperties()
                            .allowTransmogrification()
                            .discoverOnRoll()
                    )
                    .usingLayers(new HoyArmorLayers())
                    .addSlot(EquipmentSlot.HEAD)
                    .addSlot(EquipmentSlot.CHEST)
                    .addSlot(EquipmentSlot.LEGS)
                    .addSlot(EquipmentSlot.FEET)
    );

}
