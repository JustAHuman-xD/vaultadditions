package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.item.LootStatueBlockItem;
import io.github.a1qs.vaultadditions.item.PowerCrystal;
import io.github.a1qs.vaultadditions.item.PowerOrb;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VaultAdditions.MOD_ID);

    public static final RegistryObject<Item> POWER_CRYSTAL = ITEMS.register("power_crystal",
            () -> new PowerCrystal(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> POWER_ORB = ITEMS.register("power_orb",
            () -> new PowerOrb(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> LOOT_STATUE_ITEM = ModItems.ITEMS.register("loot_statue_block",
            () -> new LootStatueBlockItem(ModBlocks.LOOT_STATUE.get()));
}
