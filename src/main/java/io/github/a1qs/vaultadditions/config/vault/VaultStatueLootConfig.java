package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.VaultMod;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class VaultStatueLootConfig extends AbstractStatueLootConfig {




    public String getName() {
        return "vaultadditions_statue_vault";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.APPLE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.GLOW_ITEM_FRAME, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ITEM_FRAME, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ICE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.DIORITE, 1, null), 3);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;
    }

    public ItemStack randomLoot() {
        return this.DROPS.getRandom(new Random()).generateItemStack();
    }
}
