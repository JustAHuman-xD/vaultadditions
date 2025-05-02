package io.github.a1qs.vaultadditions;

import com.mojang.logging.LogUtils;
import io.github.a1qs.vaultadditions.block.blockentity.render.*;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.init.*;
import io.github.a1qs.vaultadditions.vault.gear.seteffect.ArmorEffectRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(VaultAdditions.MOD_ID)
public class VaultAdditions {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "vaultadditions";
    public static final CreativeModeTab VAULT_ADDITIONS_TAB = new ModCreativeTab(MOD_ID);

    public VaultAdditions() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModParticles.PARTICLE_TYPES.register(eventBus);
        ModSounds.SOUNDS.register(eventBus);

        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfigs.SPEC, "vaultadditions-server.toml");

        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER && !ModList.get().isLoaded("worldborderfixer")) {
            LOGGER.error("Worldborderfixer is not installed. Please install 'Multi World Borders Unofficial'!");
        }
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.initialize();
        ArmorEffectRegistry.registerArmorSetEffects();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModScreens.register();
        ModKeybinds.register();
        BlockEntityRenderers.register(ModBlockEntities.EVENT_BLOCK_ENTITY.get(), EventBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GLOBE_EXPANDER_ENTITY.get(), GlobeExpanderEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LOOT_STATUE_BLOCK_ENTITY.get(), LootStatueBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.STATUE_CAULDRON_BLOCK_ENTITY.get(), StatueCauldronRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.PLAYER_TRADER_BLOCK_ENTITY.get(), PlayerTraderBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.COLORED_VELVET_BED_BLOCK_ENTITY.get(), ColoredVelvetBedRenderer::new);
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
