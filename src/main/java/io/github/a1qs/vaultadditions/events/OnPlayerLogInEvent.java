package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.util.DateCheck;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLogInEvent {
//    @SubscribeEvent
//    public static void restoreBorderValue(PlayerEvent.PlayerLoggedInEvent event) {
//        Level level = event.getPlayer().getLevel();
//        if (level instanceof ServerLevel serverLevel) {
//            WorldBorderData data = WorldBorderData.get(serverLevel);
//            MinecraftServer srv = ServerLifecycleHooks.getCurrentServer();
//            WorldBorder border = srv.overworld().getWorldBorder();
//            if (data.getWorldBorderSize() != border.getSize() && data.getWorldBorderSize() != 0) {
//                border.setSize(data.getWorldBorderSize());
//                VaultAdditions.LOGGER.info("Reset Border to its last saved value!");
//            }
//        }
//    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!DateCheck.pastDate() && ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) {
            event.getPlayer().sendMessage(DateCheck.untilDateMessage().withStyle(ChatFormatting.LIGHT_PURPLE), Util.NIL_UUID);
        }
    }
}
