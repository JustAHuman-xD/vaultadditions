package io.github.a1qs.vaultadditions.init;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.blockentity.EventBlockEntity;
import io.github.a1qs.vaultadditions.block.blockentity.GlobeExpanderBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, VaultAdditions.MOD_ID);

    public static final RegistryObject<BlockEntityType<GlobeExpanderBlockEntity>> GLOBE_EXPANDER_ENTITY = BLOCK_ENTITIES.register("globe_expander_entity",
            () -> BlockEntityType.Builder.of(GlobeExpanderBlockEntity::new, ModBlocks.GLOBE_EXPANDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<EventBlockEntity>> EVENT_BLOCK_ENTITY = BLOCK_ENTITIES.register("event_block_entity",
            () -> BlockEntityType.Builder.of(EventBlockEntity::new, ModBlocks.EVENT_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
