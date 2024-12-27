package io.github.a1qs.vaultadditions.block;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.block.blockentity.PlayerTraderBlockEntity;
import iskallia.vault.container.oversized.OverSizedItemStack;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.CoinBlockItem;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"deprecation", "NullableProblems"})
public class PlayerTraderBlock extends Block implements EntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(Block.box(0, 12, 0, 16, 16, 16),
            Block.box(2, 0, 2, 14, 12, 14));

    private record CoinDefinition(Item coinItem, @Nullable Item previousHigherDenomination, @Nullable Item nextLowerDenomination, int coinValue) {}
    private static Map<Item, PlayerTraderBlock.CoinDefinition> COIN_DEFINITIONS;

    public PlayerTraderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(new TextComponent("NYI").withStyle(ChatFormatting.RED));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return player.getAbilities().instabuild && super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }



    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if(player.isSecondaryUseActive() && player.getAbilities().instabuild) {
            if (worldIn.getBlockEntity(pos) instanceof PlayerTraderBlockEntity tile) {
                tile.clear();
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        }

        if (player.getAbilities().instabuild) {
            // Temporary setting of offers if the player is in creative
            ItemStack o = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack c = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!c.isEmpty() && !o.isEmpty()) {
                if (worldIn.getBlockEntity(pos) instanceof PlayerTraderBlockEntity tile) {
                    tile.setOffer(o.copy(), OverSizedItemStack.of(c.copy()));
                    return InteractionResult.sidedSuccess(worldIn.isClientSide);
                }
            }
        }

        if (worldIn.getBlockEntity(pos) instanceof PlayerTraderBlockEntity tile && handIn == InteractionHand.MAIN_HAND) {
            ItemStack offerStack = tile.getOfferStack();

            if (!offerStack.isEmpty()) {

                return player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).map(itemHandler -> {
                    List<InventoryUtil.ItemAccess> allItems = List.of();
                    if (!player.isCreative()) {
                        allItems = InventoryUtil.findAllItems(player);
                        if (!hasEnoughCurrency(allItems, tile.getCurrencyStack())) {
                            player.displayClientMessage(new TranslatableComponent("message.the_vault.shop_pedestal.fail",
                                    tile.getCurrencyStack().getHoverName()), true);
                            return InteractionResult.sidedSuccess(worldIn.isClientSide);
                        }
                    }
                    if (!player.isCreative()) {
                        if(extractCurrency(player, allItems, tile.getCurrencyStack(), (tile.getCurrencyStack().getItem() instanceof CoinBlockItem))) {
                            player.displayClientMessage(new TranslatableComponent("message.the_vault.shop_pedestal.purchase",
                                    offerStack.getCount(), offerStack.getHoverName(), tile.getCurrencyStack().getCount(), tile.getCurrencyStack().getHoverName()), true);
                            tile.clear(); // Clear the offer, needs to be changed when players can add their offers to support multiple offers.
                        }
                    }
                    ItemHandlerHelper.giveItemToPlayer(player, offerStack.copy());

                    worldIn.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1, 1);
                    return InteractionResult.sidedSuccess(worldIn.isClientSide);
                }).orElse(InteractionResult.PASS);
            }
            return InteractionResult.PASS;
        }


        return InteractionResult.PASS;
    }

    private boolean hasEnoughCurrency(List<InventoryUtil.ItemAccess> allItems, ItemStack currency) {
        if(currency.getItem() instanceof CoinBlockItem) {
            return getCoinDefinitions(currency.getItem()).map(priceCoinDefinition -> {
                int priceValue = priceCoinDefinition.coinValue() * currency.getCount();
                for (InventoryUtil.ItemAccess itemAccess : allItems) {
                    priceValue -= getCoinDefinitions(itemAccess.getStack().getItem())
                            .map(coinDefinition -> coinDefinition.coinValue() * itemAccess.getStack().getCount()).orElse(0);
                    if (priceValue <= 0) {
                        return true;
                    }
                }
                return false;
            }).orElse(false);
        } else {
            for(InventoryUtil.ItemAccess item : allItems) {
                if(currency.getItem() == item.getStack().getItem()) {
                    if(currency.getCount() <= item.getStack().getCount()) {
                        return true;
                    }
                }
            }
        }
        return false;


    }

    public boolean extractCurrency(Player player, List<InventoryUtil.ItemAccess> allItems, ItemStack price, boolean isCoin) {
        if(isCoin) {
            getCoinDefinitions(price.getItem()).ifPresent(priceCoinDefinition -> {
                int priceValue = priceCoinDefinition.coinValue() * price.getCount();

                priceValue = deductCoins(allItems, priceValue, priceCoinDefinition);

                if (priceValue > 0) {
                    priceValue = payUsingLowerDenominations(allItems, priceValue, priceCoinDefinition);
                    priceValue = payUsingHigherDenominations(allItems, priceValue, priceCoinDefinition);
                }

                if (priceValue < 0) {
                    int change = -priceValue;
                    returnChangeToPlayer(player, change);
                }
            });
        } else {
            for(InventoryUtil.ItemAccess item : allItems) {
                if(price.getItem() == item.getStack().getItem()) {
                    if(price.getCount() <= item.getStack().getCount()) {

                        item.setStack(ItemHandlerHelper.copyStackWithSize(item.getStack(), item.getStack().getCount() - price.getCount()));
                        return true;
                    }
                }
            }
        }


        return true;
    }

    private static void returnChangeToPlayer(Player player, int change) {
        while(change > 0) {
            for (PlayerTraderBlock.CoinDefinition definition : COIN_DEFINITIONS.values()) {
                if (definition.coinValue() <= change && change / definition.coinValue() < 9) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(definition.coinItem(), change / definition.coinValue()));
                    change -= definition.coinValue() * (change / definition.coinValue());
                }
            }
        }
    }

    private int payUsingHigherDenominations(List<InventoryUtil.ItemAccess> allItems, int priceValue, PlayerTraderBlock.CoinDefinition coinDefinition) {
        while(priceValue > 0 && coinDefinition.previousHigherDenomination != null) {
            Optional<PlayerTraderBlock.CoinDefinition> higherCoinDefinition = getCoinDefinitions(coinDefinition.previousHigherDenomination);
            if (higherCoinDefinition.isPresent()) {
                coinDefinition = higherCoinDefinition.get();
                priceValue = deductCoins(allItems, priceValue, coinDefinition);
            }
        }
        return priceValue;
    }

    private int payUsingLowerDenominations(List<InventoryUtil.ItemAccess> allItems, int priceValue, PlayerTraderBlock.CoinDefinition coinDefinition) {
        while (priceValue > 0 && coinDefinition.nextLowerDenomination != null) {
            Optional<PlayerTraderBlock.CoinDefinition> lowerCoinDefinition = getCoinDefinitions(coinDefinition.nextLowerDenomination);
            if (lowerCoinDefinition.isPresent()) {
                coinDefinition = lowerCoinDefinition.get();
                priceValue = deductCoins(allItems, priceValue, coinDefinition);
            }
        }
        return priceValue;
    }

    private int deductCoins(List<InventoryUtil.ItemAccess> allItems, int priceValue, PlayerTraderBlock.CoinDefinition coinDefinition) {
        for (InventoryUtil.ItemAccess itemAccess : allItems) {
            ItemStack stack = itemAccess.getStack();
            if (stack.getItem() == coinDefinition.coinItem()) {

                int countToRemove = (int) Math.ceil(Math.min(priceValue, stack.getCount() * coinDefinition.coinValue()) / (double) coinDefinition.coinValue());
                if (countToRemove > 0) {
                    itemAccess.setStack(ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - countToRemove));
                    priceValue -= countToRemove * coinDefinition.coinValue();
                    if (priceValue <= 0) {
                        break;
                    }
                }
            }
        }

        return priceValue;
    }

    private static Optional<PlayerTraderBlock.CoinDefinition> getCoinDefinitions(Item coinBlock) {
        if (COIN_DEFINITIONS == null) {
            COIN_DEFINITIONS = new HashMap<>();
            COIN_DEFINITIONS.put(ModBlocks.VAULT_BRONZE, new PlayerTraderBlock.CoinDefinition(ModBlocks.VAULT_BRONZE, ModBlocks.VAULT_SILVER, null, 1));
            COIN_DEFINITIONS.put(ModBlocks.VAULT_SILVER, new PlayerTraderBlock.CoinDefinition(ModBlocks.VAULT_SILVER, ModBlocks.VAULT_GOLD, ModBlocks.VAULT_BRONZE, 9));
            COIN_DEFINITIONS.put(ModBlocks.VAULT_GOLD, new PlayerTraderBlock.CoinDefinition(ModBlocks.VAULT_GOLD, ModBlocks.VAULT_PLATINUM, ModBlocks.VAULT_SILVER, 81));
            COIN_DEFINITIONS.put(ModBlocks.VAULT_PLATINUM, new PlayerTraderBlock.CoinDefinition(ModBlocks.VAULT_PLATINUM,null, ModBlocks.VAULT_GOLD, 729));
        }

        return Optional.ofNullable(COIN_DEFINITIONS.get(coinBlock));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PlayerTraderBlockEntity(pPos, pState);
    }
}
