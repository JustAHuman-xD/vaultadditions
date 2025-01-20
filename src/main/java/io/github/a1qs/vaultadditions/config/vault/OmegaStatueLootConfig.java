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
        this.DROPS.add(new ProductEntry(Items.DIAMOND, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.NETHER_STAR, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.EMERALD, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.NETHERITE_INGOT, 1, null), 3);
        this.DROPS.add(new ProductEntry(Items.DRAGON_EGG, 1, null), 3);


        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;

        this.MAX_ACCELERATION_CHIPS = 4;
        this.INTERVAL_DECREASE_PER_CHIP.put(1, 50);
        this.INTERVAL_DECREASE_PER_CHIP.put(2, 100);
        this.INTERVAL_DECREASE_PER_CHIP.put(3, 200);
        this.INTERVAL_DECREASE_PER_CHIP.put(4, 500);
    }

    public int getMaxAccelerationChips() {
        return this.MAX_ACCELERATION_CHIPS;
    }

    public int getIntervalDecrease(int chipCount) {
        return this.INTERVAL_DECREASE_PER_CHIP.get(chipCount);
    }
}
