package io.github.a1qs.vaultadditions.patreon;

import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.world.data.DiscoveredModelsData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrantPatreonTransmogs {


    @SubscribeEvent
    public static void on(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getPlayer().level.isClientSide()) return;

        String url = "https://raw.githubusercontent.com/a1qs/dev_01/refs/heads/main/patreon_transmogs.json"; //TODO:
        PatreonConfig cfg = PatreonTransmogReader.fetchPatreonTransmogs(url);


        if(cfg != null) {
            ServerPlayer player = (ServerPlayer) event.getPlayer();
            for (PatreonConfig.PatreonTransmog transmog : cfg.getPatreonTransmogs()) {
                if(event.getPlayer().getUUID().equals(UUID.fromString(transmog.getUuid()))) {
                    DiscoveredModelsData discoveredModelsData = DiscoveredModelsData.get((ServerLevel) player.level);

                    for (String modelId : transmog.getTransmogs().stream().filter(Objects::nonNull).toList()) {

                        boolean hasModel = discoveredModelsData.getDiscoveredModels(player.getUUID())
                                .stream()
                                .anyMatch(id -> id.toString().startsWith(modelId.toString()));

                        if(!hasModel) {
                            ResourceLocation model = new ResourceLocation(modelId);
                            ModDynamicModels.REGISTRIES.getModelAndAssociatedItem(model).ifPresent(pair -> {
                                DynamicModel<?> gearModel = pair.getFirst();
                                Item associatedItem = pair.getSecond();
                                discoveredModelsData.discoverModelAndBroadcast(associatedItem, gearModel.getId(), player);
                            });
                        }

                    }
                }
            }
        }
    }
}
