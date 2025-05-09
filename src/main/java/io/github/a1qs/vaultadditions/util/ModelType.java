package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.vault.gear.gecko.item.GeckoHandHeldModel;
import io.github.a1qs.vaultadditions.vault.gear.gecko.item.GeckoPlainModel;
import io.github.a1qs.vaultadditions.vault.gear.gecko.item.GeckoShieldModel;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.model.item.PlainItemModel;
import iskallia.vault.dynamodel.model.item.shield.ShieldModel;
import iskallia.vault.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public enum ModelType {
    SWORD(ModItems.SWORD, HandHeldModel::new, GeckoHandHeldModel::new),
    AXE(ModItems.AXE, HandHeldModel::new, GeckoHandHeldModel::new),
    BATTLESTAFF(xyz.iwolfking.woldsvaults.init.ModItems.BATTLESTAFF, HandHeldModel::new, GeckoHandHeldModel::new),
    TRIDENT(xyz.iwolfking.woldsvaults.init.ModItems.TRIDENT, HandHeldModel::new, GeckoHandHeldModel::new),
    SHIELD(ModItems.SHIELD, ShieldModel::new, GeckoShieldModel::new),
    FOCUS(ModItems.FOCUS, PlainItemModel::new, GeckoPlainModel::new),
    WAND(ModItems.WAND, PlainItemModel::new, GeckoPlainModel::new),
    MAGNETS(ModItems.MAGNET, PlainItemModel::new, GeckoPlainModel::new),
    WENDARR_IDOL("idol", ModItems.IDOL_TIMEKEEPER, PlainItemModel::new, GeckoPlainModel::new),
    IDONA_IDOL("idol", ModItems.IDOL_MALEVOLENCE, PlainItemModel::new, GeckoPlainModel::new),
    VELARA_IDOL("idol", ModItems.IDOL_BENEVOLENT, PlainItemModel::new, GeckoPlainModel::new),
    TENOS_IDOL("idol", ModItems.IDOL_OMNISCIENT, PlainItemModel::new, GeckoPlainModel::new);

    private final String type;
    private final Item item;
    private final ModelFactory modelFactory;
    private final GeckoModelFactory geckoModelFactory;

    ModelType(Item item, ModelFactory modelFactory, GeckoModelFactory geckoModelFactory) {
        this.type = name().toLowerCase();
        this.item = item;
        this.modelFactory = modelFactory;
        this.geckoModelFactory = geckoModelFactory;
    }

    ModelType(String type, Item item, ModelFactory modelFactory, GeckoModelFactory geckoModelFactory) {
        this.type = type;
        this.item = item;
        this.modelFactory = modelFactory;
        this.geckoModelFactory = geckoModelFactory;
    }

    public Item getItem() {
        return item;
    }

    public DynamicModel<?> createModel(String id, String displayName) {
        return modelFactory.create(VaultMod.id("gear/" + type + "/" + id), displayName);
    }

    public DynamicModel<?> createModel(ResourceLocation id, String displayName) {
        return modelFactory.create(id, displayName);
    }

    public DynamicModel<?> createGeckoModel(String id, String displayName, String animationName, float transitionTicks) {
        return geckoModelFactory.create(id, type, displayName, animationName, transitionTicks);
    }

    @FunctionalInterface
    private interface ModelFactory {
        DynamicModel<?> create(ResourceLocation id, String displayName);
    }

    @FunctionalInterface
    private interface GeckoModelFactory {
        DynamicModel<?> create(String id, String type, String displayName, String animationName, float transitionTicks);
    }
}
