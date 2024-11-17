package io.github.a1qs.vaultadditions.block;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.blockentity.GlobeExpanderBlockEntity;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PowerCrystalData;
import io.github.a1qs.vaultadditions.events.Event;
import io.github.a1qs.vaultadditions.init.ModBlockEntities;
import io.github.a1qs.vaultadditions.init.ModItems;
import io.github.a1qs.vaultadditions.item.PowerCrystal;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import iskallia.vault.init.ModAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GlobeExpanderBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(.1, .1, .1, .9, .9, .9);


    public GlobeExpanderBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @javax.annotation.Nullable BlockGetter pLevel, List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        pTooltip.add(new TextComponent("Upon clicking with a Power Crystal:").withStyle(ChatFormatting.YELLOW));
        pTooltip.add(new TextComponent("Expands the World Border by " + ServerConfigs.POWER_CRYSTAL_INCREASE.get() + " Blocks").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pLevel.isClientSide()) {
            return InteractionResult.PASS;
        }


        if(!(pPlayer.getMainHandItem().getItem() instanceof PowerCrystal)) {
            double worldBorderSize = pPlayer.getLevel().getWorldBorder().getSize();
            pPlayer.displayClientMessage(new TextComponent("Current World Border size Diameter: " + worldBorderSize), true);
            return InteractionResult.SUCCESS;
        }

        if(pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }


        MinecraftServer srv = ServerLifecycleHooks.getCurrentServer();
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        EventData data = EventData.get(srv);
        boolean isActiveBorderEvent = false;
        if(data.isEventActive()) isActiveBorderEvent = data.getActiveEvent().getId().equals(Event.EVENT_IDS.get("BORDER_EXPANSION_ENABLED"));


        if((TimeUtil.pastDate() || !isActiveBorderEvent) && ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) {
            pPlayer.displayClientMessage(new TextComponent("Nothing happened...").withStyle(ChatFormatting.RED), true);
            return InteractionResult.PASS;
        }

        List<ServerLevel> dimensions = Arrays.asList(
                srv.getLevel(Level.OVERWORLD),
                srv.getLevel(Level.NETHER),
                srv.getLevel(Level.END)
        );

        List<ServerLevel> validDimensions = dimensions.stream()
                .filter(Objects::nonNull)
                .toList();


        if(blockEntity instanceof GlobeExpanderBlockEntity expanderEntity) {
            if(expanderEntity.shouldAnimate()) {
                return InteractionResult.PASS;
            }


            if(pPlayer.getMainHandItem().getItem() == ModItems.POWER_CRYSTAL.get()) {
                int powerCrystalIncrease = ServerConfigs.POWER_CRYSTAL_INCREASE.get();
                int handCount = pPlayer.getMainHandItem().getCount();

                for (ServerLevel dimension : validDimensions) {
                    WorldBorder dimensionBorder = dimension.getWorldBorder();

                    double blocksExpanded = calculateDimensionSpecificExpansion(dimension, powerCrystalIncrease, handCount);
                    double newSize = dimensionBorder.getSize() + blocksExpanded;
                    if(newSize >= 5.9999968E7) {
                        VaultAdditions.LOGGER.error("Cannot increase border in {}, size would be {} but the max allowed value is {}", dimension.dimension(), newSize, 5.9999968E7);
                        pPlayer.displayClientMessage(new TextComponent("Please report this to a server admin, see logs for more information.").withStyle(ChatFormatting.RED), true);
                        return InteractionResult.FAIL;
                    }
                    dimensionBorder.lerpSizeBetween(dimensionBorder.getSize(), newSize, 1000);

                    Objects.requireNonNull(expanderEntity.getLevel()).playSound(null, pPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.75F, 0.9F);
                }
                if (!pPlayer.getAbilities().instabuild) {
                    pPlayer.getMainHandItem().setCount(0);
                }

                if(ServerConfigs.GROW_PLAYER_ON_GEMSTONE_SUBMIT.get()) {
                    PowerCrystalData crystalData = PowerCrystalData.get(srv);
                    crystalData.addCrystalContribution(pPlayer.getUUID(), handCount);

                    var sizeScaleAttribute = pPlayer.getAttribute(ModAttributes.SIZE_SCALE);
                    if (sizeScaleAttribute != null) {

                        double increaseAmount = crystalData.getPlayerContributedCrystals(pPlayer.getUUID()) * ServerConfigs.GROW_PLAYER_AMOUNT.get();
                        increaseAmount = Math.min(increaseAmount, ServerConfigs.GROW_PLAYER_CAP.get());
                        // Check if the player already has this modifier
                        AttributeModifier existingModifier = sizeScaleAttribute.getModifier(MiscUtil.sizeScaleModifierUUID);
                        if (existingModifier != null) sizeScaleAttribute.removeModifier(existingModifier); //remove to readd it

                        AttributeModifier sizeScaleModifier = new AttributeModifier(MiscUtil.sizeScaleModifierUUID, "PowerCrystalSizeScale", increaseAmount, AttributeModifier.Operation.ADDITION);
                        sizeScaleAttribute.addPermanentModifier(sizeScaleModifier);
                    }
                }

                expanderEntity.resetSpinTime();
                expanderEntity.setChanged();
                pLevel.sendBlockUpdated(pPos, expanderEntity.getBlockState(), expanderEntity.getBlockState(), 3);

                expanderEntity.setBroadcastMessage(pPlayer.getDisplayName().getString(), powerCrystalIncrease * handCount);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide && pBlockEntityType == ModBlockEntities.GLOBE_EXPANDER_ENTITY.get()) {
            return GlobeExpanderBlockEntity::clientTick;
        }
        return pLevel.isClientSide() ? GlobeExpanderBlockEntity::clientTick : GlobeExpanderBlockEntity::serverTick;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GlobeExpanderBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    private double calculateDimensionSpecificExpansion(ServerLevel dimension, int baseIncrease, int itemCount) {
        if (dimension.dimension() == Level.OVERWORLD) {
            return itemCount * baseIncrease;
        } else if (dimension.dimension() == Level.NETHER) {
            return itemCount * baseIncrease * ServerConfigs.NETHER_BORDER_INCREASE.get();
        } else if (dimension.dimension() == Level.END) {
            return itemCount * baseIncrease * ServerConfigs.END_BORDER_INCREASE.get();
        }
        return itemCount * baseIncrease;
    }

}
