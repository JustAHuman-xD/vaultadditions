package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.util.DateUtil;
import iskallia.vault.skill.base.SkillContext;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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


        if(!DateUtil.pastDate() && ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) {
            player.sendMessage(DateUtil.untilDateMessage().withStyle(ChatFormatting.LIGHT_PURPLE), Util.NIL_UUID);
        }
    }
}
