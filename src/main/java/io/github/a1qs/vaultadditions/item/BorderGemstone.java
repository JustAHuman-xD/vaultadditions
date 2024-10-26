package io.github.a1qs.vaultadditions.item;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.util.DateUtil;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class BorderGemstone extends Item {

    public BorderGemstone(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(!DateUtil.pastDate()) {
            pTooltipComponents.add(new TextComponent("Increases the World Border by ").append(ServerConfigs.POWER_CRYSTAL_INCREASE.get().toString()).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
        } else {
            pTooltipComponents.add(
                    new TextComponent("Adds a ")
                            .append(new TextComponent("Power point").withStyle(ChatFormatting.YELLOW))
                            .append(" upon right clicking\nRequires Vault level ")
                            .append(new TextComponent("100").withStyle(ChatFormatting.YELLOW))
            );
        }
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldItemStack = player.getItemInHand(hand);
        if(!DateUtil.pastDate()) return InteractionResultHolder.fail(heldItemStack);

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerLevel) world);
            if(statsData.getVaultStats(player).getVaultLevel() < 100) return InteractionResultHolder.fail(heldItemStack);

            if(player.isCrouching()) {
                int itemStackCount = heldItemStack.getCount();
                if(!player.getAbilities().instabuild) heldItemStack.shrink(itemStackCount);

                statsData.addArchetypePoints((ServerPlayer) player, itemStackCount);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.success(heldItemStack);
            }

            if(!player.getAbilities().instabuild) heldItemStack.shrink(1);
            statsData.addArchetypePoints((ServerPlayer) player, 1);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.success(heldItemStack);
        }

        return InteractionResultHolder.sidedSuccess(heldItemStack, world.isClientSide());
    }


}
