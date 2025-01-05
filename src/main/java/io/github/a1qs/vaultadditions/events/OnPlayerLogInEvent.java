package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.init.ModModels;
import io.github.a1qs.vaultadditions.init.ModNetwork;
import io.github.a1qs.vaultadditions.network.EventSyncMessage;
import io.github.a1qs.vaultadditions.util.PlayerModelGrants;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import iskallia.vault.VaultMod;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.world.data.DiscoveredModelsData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.iwolfking.woldsvaults.init.ModItems;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnPlayerLogInEvent {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ServerLevel level = player.getLevel();
        EventData data = EventData.get(level);

        PlayerPowersData.get(level).getPowers(player).sync(SkillContext.of(player));
        PlayerAdditionalVaultStatData.get(level).getVaultStats(player).sync(level.getServer());


        if(!TimeUtil.pastDate() && ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) {
            player.sendMessage(TimeUtil.untilDateMessage().withStyle(ChatFormatting.LIGHT_PURPLE), Util.NIL_UUID);
        }

        if(data.isEventActive()) {
            player.sendMessage(data.getActiveEvent().getEventLoginMessage(), Util.NIL_UUID);
        }

        // Send a network message to inform clients about event stuff
        ModNetwork.sendToClient(new EventSyncMessage(data.globeExpanderRequired(), data.isEventActive()), player);
    }

    @SubscribeEvent
    public static void grantSpecialArmorSets(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        String playerName = event.getPlayer().getDisplayName().getString();


        for (PlayerModelGrants config : PlayerModelGrants.GrantConfigurations.PLAYER_GRANTS) {
            if (playerName.contains(config.getPlayerName()) || !FMLEnvironment.production) {
                DiscoveredModelsData discoveredModelsData = DiscoveredModelsData.get((ServerLevel) player.level);

                for (PlayerModelGrants.ArmorGrantData grant : config.getGrants()) {
                    ResourceLocation modelId = grant.getModel().getId();

                    boolean hasModel = discoveredModelsData.getDiscoveredModels(player.getUUID())
                            .stream()
                            .anyMatch(id -> id.toString().startsWith(modelId.toString()));

                    if (!hasModel) {
                        MutableComponent info = new TextComponent(grant.getMessage()).withStyle(ChatFormatting.GOLD);
                        player.sendMessage(info, Util.NIL_UUID);

                        if (grant.hasItem()) {
                            discoveredModelsData.discoverModelAndBroadcast(grant.getItem(), modelId, player);
                        } else if(grant.getModel() instanceof ArmorModel armorModel) {
                            discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, armorModel);
                        }
                    }
                }
            }
        }
    }

//        if(name.toLowerCase().contains("HoY_82".toLowerCase()) || !FMLEnvironment.production) {
//
//            ResourceLocation modelId1 = ModModels.Armor.HOY_82.getId();
//            ResourceLocation modelId2 = ModModels.Armor.HOY_82_GROGU.getId();
//            ResourceLocation modelId3 = ModModels.WoldsBattleStaffs.DARKSABER.getId();
//
//            boolean hasModel1 = discoveredModelsData.getDiscoveredModels(player.getUUID())
//                    .stream()
//                    .anyMatch(id -> id.toString().startsWith(modelId1.toString()));
//
//            boolean hasModel2 = discoveredModelsData.getDiscoveredModels(player.getUUID())
//                    .stream()
//                    .anyMatch(id -> id.toString().startsWith(modelId2.toString()));
//
//            boolean hasModel3 = discoveredModelsData.getDiscoveredModels(player.getUUID())
//                    .stream()
//                    .anyMatch(id -> id.toString().startsWith(modelId3.toString()));
//
//            if (!hasModel1) {
//                MutableComponent info = new TextComponent("You have been granted the Beskar armor set!").withStyle(ChatFormatting.GOLD);
//                player.sendMessage(info, Util.NIL_UUID);
//                discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, ModModels.Armor.HOY_82);
//            }
//
//            if (!hasModel2) {
//                MutableComponent info = new TextComponent("You have been granted the Beskar & Grogu armor set!").withStyle(ChatFormatting.GOLD);
//                player.sendMessage(info, Util.NIL_UUID);
//                discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, ModModels.Armor.HOY_82_GROGU);
//            }
//
//            if (!hasModel3 && ModList.get().isLoaded("woldsvaults")) {
//                MutableComponent info = new TextComponent("You have been granted the Darksaber set!").withStyle(ChatFormatting.GOLD);
//                player.sendMessage(info, Util.NIL_UUID);
//                discoveredModelsData.discoverModelAndBroadcast(ModItems.BATTLESTAFF.asItem(), modelId3, player);
//            }
//        }
//
//        if(name.toLowerCase().contains("TigerShrimp88".toLowerCase()) || !FMLEnvironment.production) {
//            DiscoveredModelsData discoveredModelsData = DiscoveredModelsData.get((ServerLevel)player.level);
//            ResourceLocation modelId = ModModels.Armor.HOKAGE_ROBES.getId();
//
//            boolean hasModel = discoveredModelsData.getDiscoveredModels(player.getUUID())
//                    .stream()
//                    .anyMatch(id -> id.toString().startsWith(modelId.toString()));
//
//            if (!hasModel) {
//                MutableComponent info = new TextComponent("You have been granted the Hokage Robes armor set!").withStyle(ChatFormatting.GOLD);
//                player.sendMessage(info, Util.NIL_UUID);
//                discoveredModelsData.discoverAllArmorPieceAndBroadcast(player, ModModels.Armor.HOKAGE_ROBES);
//            }
//        }


}
