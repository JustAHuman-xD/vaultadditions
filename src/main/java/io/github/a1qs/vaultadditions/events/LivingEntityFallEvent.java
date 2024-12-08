package io.github.a1qs.vaultadditions.events;

import iskallia.vault.gear.trinket.TrinketHelper;
import iskallia.vault.gear.trinket.effects.MultiJumpTrinket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEntityFallEvent {

    @SubscribeEvent
    public static void multiJumpTrinketFallReductionEvent(LivingFallEvent event) {
        if(event.getEntityLiving() instanceof ServerPlayer player) {
            // -2
            if (!TrinketHelper.getTrinkets(player, MultiJumpTrinket.class).stream().noneMatch((trinket) -> trinket.isUsable(player))) {
                if(event.getDistance() < 5.0F) {
                    event.setCanceled(true);
                } else {
                    event.setDistance(event.getDistance() - 2.0F);
                }
            }
        }
    }
}
