package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.blockentity.render.ColoredVelvetBedRenderer;
import io.github.a1qs.vaultadditions.vault.gear.gecko.GeckoArmorModel;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.AdditionalArmorModel;
import io.github.a1qs.vaultadditions.vault.gear.model.armor.layers.*;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModelProperties;
import iskallia.vault.dynamodel.model.armor.ArmorLayers;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.model.item.shield.ShieldModel;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.skill.ability.effect.ManaShieldAbility;
import iskallia.vault.skill.ability.effect.SmiteArchonAbility;
import iskallia.vault.skill.ability.effect.spi.AbstractSmiteAbility;
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

import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    public static final Set<AdditionalArmorModel> HOY_ARMOR = Set.of(Armor.HOY_82.model, Armor.HOY_82_GROGU.model, Armor.DINDJARIN.model, Armor.BOKATAN.model, GeckoArmor.HOYTEST.model);
    public static final Set<AdditionalArmorModel> HOKAGE_ARMOR = Set.of(Armor.HOKAGE_ROBES.model, Armor.HOKAGE_ROBES_MASKLESS.model);

    static {
        for (AdditionalArmorModel model : HOY_ARMOR) {
            model.abilitySound(SmiteArchonAbility.class, ModSounds.HOY_ACTIVATE_ARCHON.get());
            model.abilitySound(AbstractSmiteAbility.class, ModSounds.HOY_ARCHON_BOLT.get());
            model.abilitySound(DashAbility.class, ModSounds.HOY_DASH.get());
            model.abilitySound(ManaShieldAbility.class, ModSounds.HOY_ACTIVATE_MANASHIELD.get());
            model.abilitySound(ManaShieldAbility.class, ModSounds.HOY_MANASHIELD_HIT.get());
            model.elytraSound(ModSounds.HOY_ELYTRA_GLIDE.get(), 0.2F);
        }

        for (AdditionalArmorModel model : HOKAGE_ARMOR) {
            model.abilitySound(SmiteArchonAbility.class, ModSounds.TIGER_ACTIVATE_ARCHON.get(), 0.2F, 1F);
            model.abilitySound(AbstractSmiteAbility.class, ModSounds.TIGER_ARCHON_BOLT.get());
            model.abilitySound(DashAbility.class, ModSounds.TIGER_DASH.get());
            model.abilitySound(ManaShieldAbility.class, ModSounds.TIGER_ACTIVATE_MANASHIELD.get());
        }
    }

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

    public enum Armor {
        HOY_82("hoy", "Beskar", new HoyArmorLayers()),
        HOY_82_GROGU("hoy_with_grogu", "Beskar & Grogu", new HoyGroguArmorLayers()),
        DINDJARIN("dindjarin", "DinDjarin", new DindjarinArmorLayers()),
        HOKAGE_ROBES("hokage_robes", "Hokage Robes", new HokageRobesArmorLayers()),
        HOKAGE_ROBES_MASKLESS("hokage_robes_maskless", "Hokage Robes Maskless", new HokageRobesMasklessArmorLayers()),
        CELESTIAL("celestial", "Celestial", new CelestialArmorLayers()),
        VIKING("viking", "Viking", new VikingArmorLayers(), true, false),
        BOKATAN("bokatan", "Bokatan", new BokatanArmorLayers()),
        SPACE_MARINE("spacemarine", "Space Marine", new SpaceMarineArmorLayers());

        private final AdditionalArmorModel model;

        Armor(String id, String displayName, ArmorLayers layers) {
            this(id, displayName, layers, true, true);
        }

        Armor(String id, String displayName, ArmorLayers layers, boolean allowTransmog, boolean hideElytra) {
            this(id, displayName, layers, allowTransmog, hideElytra, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        }

        Armor(String id, String displayName, ArmorLayers layers, boolean allowTransmog, boolean hideElytra, EquipmentSlot... slots) {
            this.model = new AdditionalArmorModel(VaultMod.id("gear/armor/" + id), displayName, hideElytra);
            this.model.properties(new DynamicModelProperties());
            this.model.usingLayers(layers);
            if (allowTransmog) {
                this.model.getModelProperties().allowTransmogrification().discoverOnRoll();
            }
            for (EquipmentSlot slot : slots) {
                this.model.addSlot(slot);
            }
        }

        public AdditionalArmorModel getModel() {
            return this.model;
        }
    }

    public enum GeckoArmor {
        HOYTEST("hoytest", "Hoytest", "ballanimation", 20);

        private final GeckoArmorModel model;

        GeckoArmor(String id, String displayName, String animationName, int transitionTicks) {
            this(id, displayName, animationName, transitionTicks, true, true);
        }

        GeckoArmor(String id, String displayName, String animationName, int transitionTicks, boolean allowTransmog, boolean hideElytra) {
            this(id, displayName, animationName, transitionTicks, allowTransmog, hideElytra, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        }

        GeckoArmor(String id, String displayName, String animationName, int transitionTicks, boolean allowTransmog, boolean hideElytra, EquipmentSlot... slots) {
            this.model = new GeckoArmorModel(id, displayName, animationName, transitionTicks, hideElytra);
            this.model.properties(new DynamicModelProperties());
            if (allowTransmog) {
                this.model.getModelProperties().allowTransmogrification().discoverOnRoll();
            }
            for (EquipmentSlot slot : slots) {
                this.model.addSlot(slot);
            }
        }

        public GeckoArmorModel getModel() {
            return this.model;
        }
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
