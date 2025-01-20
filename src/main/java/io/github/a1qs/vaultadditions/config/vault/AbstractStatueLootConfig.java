package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.VaultMod;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractStatueLootConfig extends Config {
    @Expose
    protected WeightedList<ProductEntry> DROPS = new WeightedList<>();
    @Expose
    protected int INTERVAL;
    @Expose
    protected int MIN_ITEM_GENERATED;
    @Expose
    protected int MAX_ITEM_GENERATED;

    @Override
    protected void reset() {

    }

    public int getInterval() {
        return this.INTERVAL;
    }

    public int getRandomItemCount() {
        Random random = new Random();
        sanityCheck();
        return random.nextInt(MAX_ITEM_GENERATED - MIN_ITEM_GENERATED + 1) + MIN_ITEM_GENERATED;
    }

    private void sanityCheck() {
        if (MIN_ITEM_GENERATED > MAX_ITEM_GENERATED) {
            VaultAdditions.LOGGER.warn("Min value is greater than Max value. Swapping values.");
            int temp = this.MIN_ITEM_GENERATED;
            this.MIN_ITEM_GENERATED = MAX_ITEM_GENERATED;
            this.MAX_ITEM_GENERATED = temp;
        }
    }

    public List<ItemStack> getOptions() {
        List<ItemStack> options = new ArrayList<>();
        WeightedList<ProductEntry> entries = this.DROPS;
        if (entries.size() < 5) {
            VaultMod.LOGGER.error("Invalid config: statue weighted list should have more than 5 entries");
            return List.of(Items.DIORITE.getDefaultInstance());
        } else {
            while(true) {
                label27:
                while(options.size() < 5) {
                    ProductEntry entry = entries.getRandom(Config.rand);
                    ItemStack item = entry.generateItemStack();
                    if (item.isEmpty()) {
                        entries.remove(entry);
                    } else {
                        for (ItemStack i : options) {
                            if (i.getItem() == item.getItem()) {
                                continue label27;
                            }
                        }
                        options.add(item);
                    }
                }

                return options;
            }
        }
    }

    public ItemStack randomLoot() {
        return this.DROPS.getRandom(new Random()).generateItemStack();
    }
}
