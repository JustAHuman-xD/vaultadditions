package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import iskallia.vault.VaultMod;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.SingleItemEntry;
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

public class VaultStatueLootConfig extends Config {
    @Expose
    private WeightedList<SingleItemEntry> LOOT = new WeightedList<>();
    @Expose
    private int INTERVAL;
    @Expose
    private int MIN_ITEM_GENERATED;
    @Expose
    private int MAX_ITEM_GENERATED;



    public String getName() {
        return "vaultadditions_statue_vault";
    }

    protected void reset() {
        this.LOOT = new WeightedList<>();
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.NETHER_BRICK.getDefaultInstance()), 1));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.ICE.getDefaultInstance()), 1));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.STONE.getDefaultInstance()), 1));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.GRANITE.getDefaultInstance()), 2));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.DIORITE.getDefaultInstance()), 2));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.ANDESITE.getDefaultInstance()), 2));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.COBBLESTONE.getDefaultInstance()), 3));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.COBBLED_DEEPSLATE.getDefaultInstance()), 1));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.COBWEB.getDefaultInstance()), 1));
        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(Items.NETHERRACK.getDefaultInstance()), 10));

        ItemStack stack = new ItemStack(ModItems.UNKNOWN_ITEM);
        stack.setHoverName(new TextComponent("Fancy Example NBT item").withStyle(ChatFormatting.RED));
        stack.enchant(Enchantments.DEPTH_STRIDER, 10);

        this.LOOT.add(new WeightedList.Entry<>(new SingleItemEntry(stack), 10));

        this.INTERVAL = 500;
        this.MIN_ITEM_GENERATED = 100;
        this.MAX_ITEM_GENERATED = 200;
    }

    public ItemStack randomLoot() {
        return this.getItem(this.LOOT.getRandom(new Random()));
    }

    public int getInterval() {
        return this.INTERVAL;
    }

    public int getRandomItemCount() {
        Random random = new Random();
        sanityCheck();
        return random.nextInt(MAX_ITEM_GENERATED - MIN_ITEM_GENERATED + 1) + MIN_ITEM_GENERATED;
    }


    private ItemStack getItem(SingleItemEntry entry) {
        ItemStack stack = ItemStack.EMPTY;

        try {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.ITEM));
            stack = new ItemStack(item);
            if (entry.NBT != null) {
                if(!entry.NBT.equals("{}")) {
                    CompoundTag nbt = TagParser.parseTag(entry.NBT);
                    stack.setTag(nbt);
                }
            }
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

        return stack;
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
        WeightedList<SingleItemEntry> entries = this.LOOT;
        if (entries.size() < 5) {
            VaultMod.LOGGER.error("Invalid config: statue weighted list should have more than 5 entries");
            return List.of(Items.DIORITE.getDefaultInstance());
        } else {
            while(true) {
                label27:
                while(options.size() < 5) {
                    SingleItemEntry entry = entries.getRandom(new Random());
                    ItemStack item = this.getItem(entry);
                    if (item.isEmpty()) {
                        entries.remove(entry);
                    } else {
                        Iterator var5 = options.iterator();

                        while(var5.hasNext()) {
                            ItemStack i = (ItemStack)var5.next();
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
}
