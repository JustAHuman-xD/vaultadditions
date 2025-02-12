package io.github.a1qs.vaultadditions.item;

import io.github.a1qs.vaultadditions.block.GlobeExpanderBlock;
import io.github.a1qs.vaultadditions.client.ClientEventData;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.init.ModBlocks;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class PowerCrystal extends Item {

    public PowerCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int increase = ServerConfigs.POWER_CRYSTAL_INCREASE.get();
        String append = Math.abs(ServerConfigs.POWER_CRYSTAL_INCREASE.get()) == 1 ? "" : "s";
        if(ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) { // The time for expanding the border is limited to events & the specified timespan
            if(ClientEventData.isGlobeExpanderRequired()) {
                pTooltipComponents.add(new TextComponent("Temporarily Increases the World Border by ").append(increase + append).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
                if(VaultBarOverlay.vaultLevel >= 100) {
                    pTooltipComponents.add(
                            new TextComponent("Grants a").withStyle(ChatFormatting.YELLOW)
                                    .append(new TextComponent(" Power Point").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414))))
                                    .append(new TextComponent(" upon use with the Globe Expander").withStyle(ChatFormatting.YELLOW))
                    );
                }
            } else if(!TimeUtil.pastDate()) {
                pTooltipComponents.add(new TextComponent( "Increases the World Border by ").append(increase + append).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
                if(VaultBarOverlay.vaultLevel >= 100) {
                    pTooltipComponents.add(
                            new TextComponent("Grants a").withStyle(ChatFormatting.YELLOW)
                                    .append(new TextComponent(" Power Point").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414))))
                                    .append(new TextComponent(" upon use with the Globe Expander").withStyle(ChatFormatting.YELLOW))
                    );
                }
            } else if (VaultBarOverlay.vaultLevel >= 100) {
                pTooltipComponents.add(
                        new TextComponent("Grants a").withStyle(ChatFormatting.YELLOW)
                                .append(new TextComponent(" Power Point").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414))))
                                .append(new TextComponent(" upon use").withStyle(ChatFormatting.YELLOW))
                );
            }
        } else {
            pTooltipComponents.add(new TextComponent("Increases the World Border by ").append(increase + append).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
            if(VaultBarOverlay.vaultLevel >= 100) {
                pTooltipComponents.add(
                        new TextComponent("Grants a").withStyle(ChatFormatting.YELLOW)
                                .append(new TextComponent(" Power Point").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414))))
                                .append(new TextComponent(" upon use with the Globe Expander").withStyle(ChatFormatting.YELLOW))
                );
            }
        }

        if(pTooltipComponents.size() <= 1) {
            pTooltipComponents.add(new TextComponent("I seem to be too weak to make use of this...").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED));
        }

    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResultHolder.fail(player.getItemInHand(hand));
        if (isUsingInvalidBlock(level, player)) return InteractionResultHolder.fail(player.getItemInHand(hand));
        if (GlobeExpanderBlock.isCurrentlyInUse())  return InteractionResultHolder.fail(player.getItemInHand(hand));

        ItemStack heldItemStack = player.getItemInHand(hand);
        ServerLevel serverLevel = (ServerLevel) level;
        PlayerVaultStatsData statsData = PlayerVaultStatsData.get(serverLevel);

        if (statsData.getVaultStats(player).getVaultLevel() < 100) {
            return InteractionResultHolder.fail(heldItemStack);
        }

        PlayerAdditionalVaultStatData additionalStatsData = PlayerAdditionalVaultStatData.get((ServerLevel) level);
        int consumeAmount = player.isCrouching() ? heldItemStack.getCount() : 1;

        if (!player.getAbilities().instabuild) {
            heldItemStack.shrink(consumeAmount);
        }

        additionalStatsData.addPowerPoints((ServerPlayer) player, consumeAmount);
        player.awardStat(Stats.ITEM_USED.get(this));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));

        return InteractionResultHolder.success(heldItemStack);
    }

    private boolean isUsingInvalidBlock(Level world, Player player) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
        return blockState.is(ModBlocks.GLOBE_EXPANDER.get());
    }
}
