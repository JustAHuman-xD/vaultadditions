package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.mixins.MixinModDynamicModels$Armor;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import iskallia.vault.VaultMod;
import iskallia.vault.event.EntityEvents;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.world.data.DiscoveredModelsData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.iwolfking.woldsvaults.init.ModItems;

import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
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
            player.sendMessage(EventData.getServer().getActiveEvent().getEventLoginMessage(), Util.NIL_UUID);
        }
    }

    @SubscribeEvent
    public static void grantHoyModelsOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        String name = event.getPlayer().getDisplayName().getString();

        if(name.toLowerCase().contains("HoY_82".toLowerCase())) {
            DiscoveredModelsData discoveredModelsData = DiscoveredModelsData.get((ServerLevel)player.level);
            ResourceLocation modelId = ModModels.Armor.HOY_82.getId();
            ResourceLocation modelId2 = ModModels.Armor.HOY_82_GROGU.getId();
            ResourceLocation modelId3 = ModModels.WoldsBattleStaffs.DARKSABER.getId();

            if (!discoveredModelsData.getDiscoveredModels(player.getUUID()).contains(modelId)) {
                MutableComponent info = (new TextComponent("You have been granted the Beskar armor set!")).withStyle(ChatFormatting.GOLD);
                player.sendMessage(info, Util.NIL_UUID);
                discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, ModModels.Armor.HOY_82);
            }

            if (!discoveredModelsData.getDiscoveredModels(player.getUUID()).contains(modelId2)) {
                MutableComponent info = (new TextComponent("You have been granted the Beskar & Grogu armor set!")).withStyle(ChatFormatting.GOLD);
                player.sendMessage(info, Util.NIL_UUID);
                discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, ModModels.Armor.HOY_82_GROGU);
            }

            if (!discoveredModelsData.getDiscoveredModels(player.getUUID()).contains(modelId3) && ModList.get().isLoaded("woldsvaults")) {
                MutableComponent info = (new TextComponent("You have been granted the Darksaber set!")).withStyle(ChatFormatting.GOLD);
                player.sendMessage(info, Util.NIL_UUID);
                discoveredModelsData.discoverModelAndBroadcast(ModItems.BATTLESTAFF.asItem(), modelId3, player);
            }
        }
    }
}
