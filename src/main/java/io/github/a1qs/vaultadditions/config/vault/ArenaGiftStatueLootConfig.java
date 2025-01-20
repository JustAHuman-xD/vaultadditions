package io.github.a1qs.vaultadditions.config.vault;

import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.world.item.Items;

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
