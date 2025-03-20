package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.blockentity.render.ColoredVelvetBedRenderer;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.*;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.model.item.shield.ShieldModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ColoredVelvetBedRenderer.HEAD_LAYER_LOCATION, ColoredVelvetBedRenderer::createHeadLayer);
        event.registerLayerDefinition(ColoredVelvetBedRenderer.FOOT_LAYER_LOCATION, ColoredVelvetBedRenderer::createFootLayer);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void stitchTextures(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }

        // Loop through all 16 Minecraft colors and register each texture
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation texture = VaultAdditions.id("entity/bed/velvet_bed_" + color.getName());
            event.addSprite(texture);
        }

    }




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

        public static final ArmorModel DINDJARIN = new ArmorModel(VaultMod.id("gear/armor/dindjarin"), "DinDjarin")
                .properties(new DynamicModelProperties().allowTransmogrification().discoverOnRoll())
                .usingLayers(new DindjarinArmorLayers())
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
