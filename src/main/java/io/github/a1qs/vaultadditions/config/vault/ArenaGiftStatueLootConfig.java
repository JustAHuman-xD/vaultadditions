package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.VaultMod;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ArenaGiftStatueLootConfig extends AbstractStatueLootConfig {


    public String getName() {
        return "vaultadditions_statue_arena";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.DIORITE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.EGG, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.HANGING_ROOTS, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.RABBIT_FOOT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.LOOM, 1, null), 3);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;
    }
}
