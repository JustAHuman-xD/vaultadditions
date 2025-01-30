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
        this.DROPS.add(new ProductEntry(Items.DEEPSLATE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.TUFF, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.CRIMSON_NYLIUM, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.WARPED_NYLIUM, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.COAL_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.IRON_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.COPPER_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.LAPIS_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.JUNGLE_LOG, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.ACACIA_LOG, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.DARK_OAK_LOG, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SPRUCE_LOG, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.JUNGLE_LEAVES, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.FLOWERING_AZALEA_LEAVES, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SEA_PICKLE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.DEAD_BUSH, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.MYCELIUM, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.MOSSY_STONE_BRICKS, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SNOW_BLOCK, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SOUL_SAND, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.WHITE_CONCRETE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.END_STONE, 1, null), 1);


        this.INTERVAL = 400;
        this.MIN_ITEM_GENERATED = 64;
        this.MAX_ITEM_GENERATED = 192;
    }


}
