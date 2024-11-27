package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.EventBlock;
import io.github.a1qs.vaultadditions.block.GlobeExpanderBlock;
import io.github.a1qs.vaultadditions.block.LootStatueBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VaultAdditions.MOD_ID);


    public static final RegistryObject<Block> GLOBE_EXPANDER = registerBlock("globe_expander",
            () -> new GlobeExpanderBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(-1).noOcclusion()), CreativeModeTab.TAB_TOOLS,
            true);

    public static final RegistryObject<Block> EVENT_BLOCK = registerBlock("event_block",
            () -> new EventBlock(BlockBehaviour.Properties.of(Material.BARRIER)
                    .strength(-1, 3.6E8F).noOcclusion().noCollission().noDrops()), CreativeModeTab.TAB_MISC,
            true);

    public static final RegistryObject<Block> LOOT_STATUE = registerBlock("loot_statue_block",
            () -> new LootStatueBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(20, 3.6E8F).noOcclusion()),
            CreativeModeTab.TAB_MISC,
            false);


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab, boolean createBlockItem) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        if(createBlockItem) registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }
}
