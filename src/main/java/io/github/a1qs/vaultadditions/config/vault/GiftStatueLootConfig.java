package io.github.a1qs.vaultadditions.config.vault;

import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.world.item.Items;

public class GiftStatueLootConfig extends AbstractStatueLootConfig {
    public GiftStatueLootConfig() {
    }

    public String getName() {
        return "vaultadditions_statue_gift";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.ACACIA_BOAT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ACACIA_BUTTON, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ACACIA_DOOR, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ACACIA_FENCE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ACACIA_FENCE_GATE, 1, null), 3);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;
    }


}
