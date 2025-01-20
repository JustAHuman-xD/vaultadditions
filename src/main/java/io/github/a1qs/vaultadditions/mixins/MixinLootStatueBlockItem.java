package io.github.a1qs.vaultadditions.mixins;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import io.github.a1qs.vaultadditions.item.LootStatueBlockItem;
import iskallia.vault.init.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LootStatueBlockItem.class)
public class MixinLootStatueBlockItem {


    /**
     * @author a1qs
     * @reason move to vaultaddition configs
     */
    @Overwrite(remap = false)
    public static ItemStack getStatueBlockItem(String nickname) {
        ItemStack itemStack = new ItemStack(ModBlocks.LOOT_STATUE_ITEM);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("PlayerNickname", nickname);
        ItemStack loot = CustomVaultConfigRegistry.STATUE_LOOT_OMEGA.randomLoot();
        nbt.put("LootItem", loot.serializeNBT());

        CompoundTag stackNBT = new CompoundTag();
        stackNBT.put("BlockEntityTag", nbt);
        itemStack.setTag(stackNBT);
        return itemStack;
    }
}
