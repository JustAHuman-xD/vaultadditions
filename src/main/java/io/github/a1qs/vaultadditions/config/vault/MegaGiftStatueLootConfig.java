package io.github.a1qs.vaultadditions.config.vault;

import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.world.item.Items;

public class MegaGiftStatueLootConfig extends AbstractStatueLootConfig {

    public String getName() {
        return "vaultadditions_statue_mega_gift";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.IRON_INGOT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.IRON_HORSE_ARMOR, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.IRON_DOOR, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.IRON_BLOCK, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.IRON_AXE, 1, null), 3);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;
    }
}
