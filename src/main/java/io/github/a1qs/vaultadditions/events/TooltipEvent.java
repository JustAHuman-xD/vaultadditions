package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.Configs;
import io.github.a1qs.vaultadditions.util.ModelUtil;
import io.github.a1qs.vaultadditions.vault.gear.effect.TransmogEffect;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.gear.item.VaultGearItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class TooltipEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addArmorSetBuffs(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> toolTip = event.getToolTip();
        if (!(itemStack.getItem() instanceof VaultGearItem)) {
            return;
        }

        DynamicModel<?> model = ModelUtil.getDynamicModel(itemStack, false);
        if (!(model instanceof ArmorPieceModel piece)) {
            return;
        }

        List<TransmogEffect> pieceEffects = Configs.TRANSMOG_EFFECTS_CONFIG.getEffects(piece);
        if (!pieceEffects.isEmpty()) {
            List<Component> pieceTooltip = new ArrayList<>();
            for (TransmogEffect effect : pieceEffects) {
                MutableComponent effectText = effect.getTooltip();
                if (effectText != null) {
                    pieceTooltip.add(new TextComponent("✦ ").withStyle(ChatFormatting.AQUA).append(effectText));
                }
            }

            if (!pieceTooltip.isEmpty()) {
                toolTip.add(new TextComponent(""));
                toolTip.add(new TextComponent("Model Bonus:").withStyle(ChatFormatting.GREEN));
                toolTip.addAll(pieceTooltip);
            }
        }
        List<TransmogEffect> setEffects = Configs.TRANSMOG_EFFECTS_CONFIG.getEffects(piece.getArmorModel());
        if (!setEffects.isEmpty()) {
            List<Component> setTooltip = new ArrayList<>();
            for (TransmogEffect effect : setEffects) {
                MutableComponent effectText = effect.getTooltip();
                if (effectText != null) {
                    setTooltip.add(new TextComponent("✦ ").withStyle(ChatFormatting.AQUA).append(effectText));
                }
            }

            if (!setTooltip.isEmpty()) {
                toolTip.add(new TextComponent(""));
                toolTip.add(new TextComponent("Full Set Bonus:").withStyle(ChatFormatting.GREEN));
                toolTip.addAll(setTooltip);
            }
        }
    }

}
