package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.*;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.model.item.shield.ShieldModel;
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

        public static final ArmorModel CELESTIAL = new ArmorModel(VaultMod.id("gear/armor/celestial"), "Celestial")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new CelestialArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel VIKING = new ArmorModel(VaultMod.id("gear/armor/viking"), "Viking")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new VikingArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel BOKATAN = new ArmorModel(VaultMod.id("gear/armor/bokatan"), "Bokatan")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new BokatanArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);

        public static final ArmorModel SPACE_MARINE = new ArmorModel(VaultMod.id("gear/armor/spacemarine"), "Space Marine")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new SpaceMarineArmorLayers())
                .addSlot(EquipmentSlot.HEAD)
                .addSlot(EquipmentSlot.CHEST)
                .addSlot(EquipmentSlot.LEGS)
                .addSlot(EquipmentSlot.FEET);
    }

    public static class WoldsBattleStaffs {
        public static final HandHeldModel DARKSABER =
                new HandHeldModel(VaultMod.id("gear/battlestaff/darksaber"), "The Darksaber").properties(STANDARD_PROPERTIES);
    }

    public static class Wands {
        public static final PlainItemModel SIDEARM =
                new PlainItemModel(VaultMod.id("gear/wand/sidearm"), "Sidearm").properties(STANDARD_PROPERTIES);

    }

    public static class Shields {
        public static final ShieldModel RELIC_SHIELD =
                new ShieldModel(VaultMod.id("gear/shield/relicshield"), "Relic Shield").properties(STANDARD_PROPERTIES);

    }

    public static class Swords {
        public static final HandHeldModel CHAIN_SWORD =
                new HandHeldModel(VaultMod.id("gear/sword/chain_sword"), "Chain-Sword").properties(STANDARD_PROPERTIES);
    }
}
