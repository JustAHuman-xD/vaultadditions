package io.github.a1qs.vaultadditions.config.vault;

import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.world.item.Items;

public class VaultStatueLootConfig extends AbstractStatueLootConfig {




    public String getName() {
        return "vaultadditions_statue_vault";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.STONE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.GRANITE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.DIORITE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.DIRT, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.GRASS_BLOCK, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.GRAVEL, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SAND, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.COBBLESTONE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.OAK_LOG, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.OAK_LEAVES, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SPRUCE_LEAVES, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.BIRCH_LEAVES, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.NETHERRACK, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.BASALT, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.MOSSY_COBBLESTONE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.CLAY, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.TERRACOTTA, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.PUMPKIN, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.MELON, 1, null), 1);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 48;
        this.MAX_ITEM_GENERATED = 128;
    }
}
