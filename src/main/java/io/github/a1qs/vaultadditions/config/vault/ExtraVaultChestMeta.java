package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.init.ModItems;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.ItemEntry;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ExtraVaultChestMeta extends Config {
    @Expose
    private final Map<Block, Map<VaultRarity, Double>> powerCrystalChances = new LinkedHashMap();

    @Expose
    private final WeightedList<ItemEntry> itemsToConsider = new WeightedList<>();

    @Override
    public String getName() {
        return "vaultadditions_extra_vault_chest_meta";
    }

    @Override
    protected void reset() {
        this.powerCrystalChances.clear();
        this.set(ModBlocks.WOODEN_CHEST, 0.0F, this.powerCrystalChances);
        this.set(ModBlocks.GILDED_CHEST, 0.2F, this.powerCrystalChances);
        this.set(ModBlocks.LIVING_CHEST, 0.0F, this.powerCrystalChances);
        this.set(ModBlocks.ORNATE_CHEST, 0.5F, this.powerCrystalChances);
        this.set(ModBlocks.ALTAR_CHEST, 0.7F, this.powerCrystalChances);
        this.set(ModBlocks.TREASURE_CHEST, 0.5F, this.powerCrystalChances);
        this.set(ModBlocks.ORNATE_STRONGBOX, 0.5F, this.powerCrystalChances);
        this.set(ModBlocks.GILDED_STRONGBOX, 0.2F, this.powerCrystalChances);
        this.set(ModBlocks.LIVING_STRONGBOX, 0.0F, this.powerCrystalChances);
        this.set(ModBlocks.ORNATE_BARREL, 0.5F, this.powerCrystalChances);
        this.set(ModBlocks.GILDED_BARREL, 0.2F, this.powerCrystalChances);
        this.set(ModBlocks.LIVING_BARREL, 0.0F, this.powerCrystalChances);
        this.set(ModBlocks.WOODEN_BARREL, 0.0F, this.powerCrystalChances);

        this.itemsToConsider.add(new WeightedList.Entry<>(new ItemEntry(ModItems.POWER_CRYSTAL.get() ,3), 5));
        this.itemsToConsider.add(new WeightedList.Entry<>(new ItemEntry(ModItems.POWER_ORB.get(), 1), 2));
    }

    public double getpowerCrystalChance(Block block, VaultRarity rarity) {
        return this.powerCrystalChances.getOrDefault(block, Collections.emptyMap()).getOrDefault(rarity, (double) 0.0F);
    }

    private void set(Block block, double chance, Map<Block, Map<VaultRarity, Double>> mapOut) {
        for(VaultRarity value : VaultRarity.values()) {
            mapOut.computeIfAbsent(block, (block1) -> new LinkedHashMap<>()).put(value, chance);
        }
    }

    public ItemStack randomItem() {
        return this.getItem(this.itemsToConsider.getRandom(new Random()));
    }

    private ItemStack getItem(ItemEntry entry) {
        ItemStack stack = ItemStack.EMPTY;

        try {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.ITEM));
            stack = new ItemStack(item);
            if (entry.NBT != null) {
                CompoundTag nbt = TagParser.parseTag(entry.NBT);
                stack.setTag(nbt);
            }
            stack.setCount(entry.AMOUNT);

        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

        return stack;
    }
}
