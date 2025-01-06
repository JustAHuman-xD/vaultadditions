package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.HokageRobesArmorLayers;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.HokageRobesMasklessArmorLayers;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.HoyArmorLayers;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.HoyGroguArmorLayers;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import net.minecraft.world.entity.EquipmentSlot;

public class ModModels {
    public static DynamicModelProperties STANDARD_PROPERTIES = new DynamicModelProperties().allowTransmogrification().discoverOnRoll();

    public static class Armor {
        public static final ArmorModel HOY_82 = new ArmorModel(VaultMod.id("gear/armor/hoy"), "Beskar")
                .properties(STANDARD_PROPERTIES)
                .usingLayers(new HoyArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel HOY_82_GROGU = new ArmorModel(VaultMod.id("gear/armor/hoy_with_grogu"), "Beskar & Grogu")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new HoyGroguArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel HOKAGE_ROBES = new ArmorModel(VaultMod.id("gear/armor/hokage_robes"), "Hokage Robes")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new HokageRobesArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel HOKAGE_ROBES_MASKLESS = new ArmorModel(VaultMod.id("gear/armor/hokage_robes_maskless"), "Hokage Robes Maskless")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new HokageRobesMasklessArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);
    }

    public static class WoldsBattleStaffs {
        public static final HandHeldModel DARKSABER =
                new HandHeldModel(VaultMod.id("gear/battlestaff/darksaber"), "The Darksaber").properties(STANDARD_PROPERTIES);
    }
}
