package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLogInEvent {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ServerLevel level = player.getLevel();

        PlayerPowersData.get(level).getPowers(player).sync(SkillContext.of(player));
        PlayerAdditionalVaultStatData.get(level).getVaultStats(player).sync(level.getServer());


        if(!TimeUtil.pastDate() && ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) {
            player.sendMessage(TimeUtil.untilDateMessage().withStyle(ChatFormatting.LIGHT_PURPLE), Util.NIL_UUID);
        }

        if(EventData.getServer().isEventActive()) {
            long remainingTime = EventData.getServer().getEventDuration();

            long seconds = (remainingTime / 20) % 60;
            long minutes = (remainingTime / (20 * 60)) % 60;
            long hours = (remainingTime / (20 * 60 * 60)) % 24;
            long days = remainingTime / (20 * 60 * 60 * 24);

            player.sendMessage(new TextComponent("The Event \"").withStyle(ChatFormatting.YELLOW)
                    .append(new TextComponent(EventData.getServer().getActiveEvent().getEventMessage()).withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent("\" is active for another " + days + "d " + hours + "h " + minutes + "m " + seconds + "s!").withStyle(ChatFormatting.YELLOW)), Util.NIL_UUID);
        }
    }
}
