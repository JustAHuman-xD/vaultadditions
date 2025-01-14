package io.github.a1qs.vaultadditions.events;

import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.time.TickClock;
import iskallia.vault.world.data.PlayerTitlesData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static iskallia.vault.world.data.PlayerTitlesData.Type.TAB_LIST;

@Mod.EventBusSubscriber
public class TabListNameFormatEvent {
    private static final Map<UUID, Long> IN_VAULT = new HashMap<>();



    @SubscribeEvent
    public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            int vaultLevel = PlayerVaultStatsData.get(player.getLevel()).getVaultStats(player).getVaultLevel();

            MutableComponent display = new TextComponent("");

            MutableComponent level = new TextComponent(String.valueOf(vaultLevel)).withStyle(ChatFormatting.YELLOW);
            MutableComponent space = new TextComponent(" ");
            Component playerName = PlayerTitlesData.getCustomName(player.getUUID(),
                    new TextComponent(player.getName().getString()), TAB_LIST, false).orElse(new TextComponent("").append(player.getName()));

            display.append(level).append(space).append(playerName);

            if (IN_VAULT.containsKey(player.getUUID())) {
                String time = formatTime(IN_VAULT.get(player.getUUID()));

                display.append(new TextComponent(" (Vault | " + time + ")").withStyle(ChatFormatting.DARK_GRAY));
            }

            event.setDisplayName(display);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer) {
            if (event.phase != TickEvent.Phase.END || serverPlayer.server.overworld().getGameTime() % 100 != 0) return;

            boolean isInVault = ServerVaults.get(serverPlayer.level).isPresent();

            if (isInVault) {
                Vault v = ServerVaults.get(serverPlayer.level).get();
                long time = Math.abs(v.get(Vault.CLOCK).get(TickClock.DISPLAY_TIME));

                IN_VAULT.put(serverPlayer.getUUID(), time);
            } else {
                var val = IN_VAULT.remove(serverPlayer.getUUID());
                if(val != null) {
                    serverPlayer.refreshTabListName();
                }
            }

            if (IN_VAULT.containsKey(serverPlayer.getUUID())) {
                serverPlayer.refreshTabListName();
            }
        }
    }

    private static String formatTime(long time) {
        long seconds = time / 20L % 60L;
        long minutes = time / 20L / 60L % 60L;
        long hours = time / 20L / 60L / 60L;
        return hours > 0L ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);

    }

}
