package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class ModelUtil {

    public static boolean isWearingHoySet(Player player) {
        return isWearingArmorSet(ModModels.Armor.HOY_82, player)
                || isWearingArmorSet(ModModels.Armor.HOY_82_GROGU, player)
                || isWearingArmorSet(ModModels.Armor.BOKATAN, player);
    }

    public static boolean isWearingHokageRobesSet(Player player) {
        return isWearingArmorSet(ModModels.Armor.HOKAGE_ROBES, player) || isWearingArmorSet(ModModels.Armor.HOKAGE_ROBES_MASKLESS, player);
    }

    public static boolean isWearingJetpackPiece(Player player) {
        return isWearingArmorPiece(ModModels.Armor.HOY_82, EquipmentSlot.CHEST, player) ||
               isWearingArmorPiece(ModModels.Armor.HOY_82_GROGU, EquipmentSlot.CHEST, player) ||
               isWearingArmorPiece(ModModels.Armor.DINDJARIN, EquipmentSlot.CHEST, player);
    }


    public static boolean isWearingArmorPiece(ArmorModel model, EquipmentSlot slot, Player player) {
        if(model.getPiece(slot).isPresent()) {
            ArmorPieceModel pieceModel = model.getPiece(slot).get();

            ItemStack equipmentStack = player.getInventory().armor.get(equipmentSlotToInventoryIndex(slot));

            if (!(equipmentStack.getItem() instanceof VaultArmorItem)) return false;
            VaultGearData gearData = VaultGearData.read(equipmentStack);
            ResourceLocation modelId = gearData.getFirstValue(ModGearAttributes.GEAR_MODEL).orElse(null);

            if (modelId == null) return false;

            return pieceModel.getId().equals(modelId);
        }
        return false;
    }

    public static boolean isWearingArmorSet(ArmorModel armorSet, Player player) {
         return armorSet.getPieces().entrySet().stream().allMatch(entry -> {
            EquipmentSlot equipmentSlot = entry.getKey();
            ArmorPieceModel pieceModel = entry.getValue();
            ItemStack equipmentStack = player.getInventory().armor.get(equipmentSlotToInventoryIndex(equipmentSlot));

            if (!(equipmentStack.getItem() instanceof VaultArmorItem)) return false;

            VaultGearData gearData = VaultGearData.read(equipmentStack);
            ResourceLocation modelId = gearData.getFirstValue(ModGearAttributes.GEAR_MODEL).orElse(null);
            if (modelId == null) return false;

            return pieceModel.getId().equals(modelId);
        });
    }

    // Fuck. You.
//    @Nullable
//    public static ArmorPieceModel getWornSet(Player player) {
//        ArmorPieceModel firstModel = null;
//
//        for(EquipmentSlot slot : EquipmentSlot.values()) {
//            if(slot.equals(EquipmentSlot.MAINHAND) || slot.equals(EquipmentSlot.OFFHAND)) continue;
//
//            ItemStack equipmentStack = player.getInventory().armor.get(equipmentSlotToInventoryIndex(slot));
//            if (!(equipmentStack.getItem() instanceof VaultArmorItem)) return null;
//
//            VaultGearData gearData = VaultGearData.read(equipmentStack);
//            ResourceLocation modelId = gearData.getFirstValue(ModGearAttributes.GEAR_MODEL).orElse(null);
//            if (modelId == null) return null;
//
//            ArmorPieceModel currentModel = ModDynamicModels.Armor.PIECE_REGISTRY.get(modelId).orElse(null);
//            if (currentModel == null) return null;
//
//            if (firstModel == null) {
//                firstModel = currentModel;
//            } else if (!firstModel.equals(currentModel)) {
//                return null; // If any piece doesn't match, return null
//            }
//        }
//
//
//        return firstModel;
//    }

    private static int equipmentSlotToInventoryIndex(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HEAD -> 3;
            case CHEST -> 2;
            case LEGS -> 1;
            case FEET -> 0;
            default -> -1;
        };
    }
}
