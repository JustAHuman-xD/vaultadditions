package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.ArmorEffectRegistry;
import io.github.a1qs.vaultadditions.vault.gear.armorseteffects.ArmorSetEffect;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class TooltipEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addArmorSetBuffs(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> toolTip = event.getToolTip();
        Item item = itemStack.getItem();


        if (item instanceof VaultGearItem) {

            VaultGearData gearData = VaultGearData.read(itemStack);
            ResourceLocation modelId = gearData.getFirstValue(ModGearAttributes.GEAR_MODEL).orElse(null);
            ArmorPieceModel m = ModDynamicModels.Armor.PIECE_REGISTRY.get(modelId).orElse(null);
            if(m != null) {
                List<ArmorSetEffect> c = ArmorEffectRegistry.getEffectsForArmor(m.getArmorModel());
                if(!c.isEmpty()) {
                    toolTip.add(new TextComponent(""));
                    toolTip.add(new TextComponent("Full Set Bonus:").withStyle(ChatFormatting.GREEN));
                }
                for(ArmorSetEffect effect : c) {
                    toolTip.add(new TextComponent("✦ ").withStyle(ChatFormatting.AQUA).append(effect.getTooltipComponent()));
                }
            }
        }
    }

}
