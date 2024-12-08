package io.github.a1qs.vaultadditions.item;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.events.VaultAdditionsEvent;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.world.data.PlayerVaultStatsData;
import jdk.jfr.Event;
import mezz.jei.forge.config.ServerConfig;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class PowerCrystal extends Item {

    public PowerCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) { // The time for expanding the border is limited to events & the specified timespan

            // The Specified timespan hasnt passed
            if(!TimeUtil.pastDate()) {
                pTooltipComponents.add(new TextComponent("Increases the World Border by ").append(ServerConfigs.POWER_CRYSTAL_INCREASE.get().toString()).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
            } else { // The specified timespan has passed
                EventData data = EventData.getServer();
                if(data.isEventActive()) {
                    if(data.getActiveEvent().isCrystalSubmissionEvent() || data.getActiveEvent().getEventId().equals(VaultAdditionsEvent.BORDER_EXPANSION_ENABLED)) {
                        pTooltipComponents.add(new TextComponent("Temporarily increases the World Border by ").append(ServerConfigs.POWER_CRYSTAL_INCREASE.get().toString()).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
                    }
                }
            }
        } else {
            pTooltipComponents.add(new TextComponent("Increases the World Border by ").append(ServerConfigs.POWER_CRYSTAL_INCREASE.get().toString()).withStyle(ChatFormatting.YELLOW).append(" Blocks!"));
        }

        if(VaultBarOverlay.vaultLevel >= 100) {
            pTooltipComponents.add(
                    new TextComponent("Grants a").withStyle(ChatFormatting.YELLOW)
                            .append(new TextComponent(" Power Point").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16724414))))
                            .append(new TextComponent(" upon use.").withStyle(ChatFormatting.YELLOW))
            );
        }

        if(pTooltipComponents.size() <= 1) {
            pTooltipComponents.add(new TextComponent("I seem to be too weak to make use of this...").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED));
        }

    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldItemStack = player.getItemInHand(hand);
        if(!TimeUtil.pastDate()) return InteractionResultHolder.fail(heldItemStack);


        if (!world.isClientSide) {
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerLevel) world);
            if(statsData.getVaultStats(player).getVaultLevel() < 100) return InteractionResultHolder.fail(heldItemStack);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

            PlayerAdditionalVaultStatData additionalStatsData = PlayerAdditionalVaultStatData.get((ServerLevel) world);
            if(player.isCrouching()) {
                int itemStackCount = heldItemStack.getCount();
                if(!player.getAbilities().instabuild) heldItemStack.shrink(itemStackCount);

                additionalStatsData.addPowerPoints((ServerPlayer) player, itemStackCount);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.success(heldItemStack);
            }

            if(!player.getAbilities().instabuild) heldItemStack.shrink(1);
            additionalStatsData.addPowerPoints((ServerPlayer) player, 1);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.success(heldItemStack);
        }
        return InteractionResultHolder.fail(heldItemStack);
    }


}
