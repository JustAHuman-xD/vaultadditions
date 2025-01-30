package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class OmegaStatueLootConfig extends AbstractStatueLootConfig {
    @Expose
    private int MAX_ACCELERATION_CHIPS;
    @Expose
    private HashMap<Integer, Integer> INTERVAL_DECREASE_PER_CHIP = new HashMap<>();

    public String getName() {
        return "vaultadditions_statue_omega";
    }

    protected void reset() {
        this.DROPS.add(new ProductEntry(Items.STONE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.GRANITE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.DIORITE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.DIRT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.GRASS_BLOCK, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.GRAVEL, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.SAND, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.COBBLESTONE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.OAK_LOG, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.OAK_LEAVES, 1, null), 5);
        this.DROPS.add(new ProductEntry(Items.SPRUCE_LEAVES, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.BIRCH_LEAVES, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.NETHERRACK, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.BASALT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.MOSSY_COBBLESTONE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.CLAY, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.TERRACOTTA, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.PUMPKIN, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.MELON, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.DEEPSLATE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.TUFF, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.CRIMSON_NYLIUM, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.WARPED_NYLIUM, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.COAL_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.LAPIS_ORE, 1, null), 1);
        this.DROPS.add(new ProductEntry(Items.JUNGLE_LOG, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.ACACIA_LOG, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.SPRUCE_LOG, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.JUNGLE_LEAVES, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.FLOWERING_AZALEA_LEAVES, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.SEA_PICKLE, 1, null), 2);
        this.DROPS.add(new ProductEntry(Items.DEAD_BUSH, 1, null), 2);
        this.DROPS.add(new ProductEntry(Items.MYCELIUM, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.MOSSY_STONE_BRICKS, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.SNOW_BLOCK, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.SOUL_SAND, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.WHITE_CONCRETE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.CHORUS_FLOWER, 1, null), 2);
        this.DROPS.add(new ProductEntry(Items.SEA_LANTERN, 1, null), 2);
        this.DROPS.add(new ProductEntry(Items.END_STONE, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.MAGMA_BLOCK, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.WHITE_WOOL, 1, null), 2);
        this.DROPS.add(new ProductEntry(Items.CRIMSON_STEM, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.WARPED_STEM, 1, null), 3);


        this.INTERVAL = 1200;
        this.MIN_ITEM_GENERATED = 0;
        this.MAX_ITEM_GENERATED = 0;

        this.MAX_ACCELERATION_CHIPS = 3;
        this.INTERVAL_DECREASE_PER_CHIP.put(1, 266);
        this.INTERVAL_DECREASE_PER_CHIP.put(2, 533);
        this.INTERVAL_DECREASE_PER_CHIP.put(3, 800);
    }

    public int getMaxAccelerationChips() {
        return this.MAX_ACCELERATION_CHIPS;
    }

    public int getIntervalDecrease(int chipCount) {
        return this.INTERVAL_DECREASE_PER_CHIP.get(chipCount);
    }
}
