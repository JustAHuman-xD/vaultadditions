package io.github.a1qs.vaultadditions.mixins;

import iskallia.vault.config.StatueLootConfig;
import iskallia.vault.config.entry.SingleItemEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.MixedUnits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = StatueLootConfig.class, remap = false)
public class MixinStatueLootConfig {

    /**
     * @author a1qs
     * @reason I DONT CARE, PLEASE FIX THE F***** TAGS APPEARING ON STATUES
     */
    @Overwrite
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
}
