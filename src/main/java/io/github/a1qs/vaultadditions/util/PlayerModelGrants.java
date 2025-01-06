package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.dynamodel.DynamicModel;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import xyz.iwolfking.woldsvaults.init.ModItems;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PlayerModelGrants {
    private final String playerName;
    private final List<ArmorGrantData> grants;


    public PlayerModelGrants(String playerName, List<ArmorGrantData> grants) {
        this.playerName = playerName.toLowerCase(); // Store as lowercase for case-insensitive comparison
        this.grants = grants;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<ArmorGrantData> getGrants() {
        return grants;
    }

    public static class GrantConfigurations {
        public static final List<PlayerModelGrants> PLAYER_GRANTS = List.of(
                // HoY_82
                new PlayerModelGrants("hoy_82", Stream.of(
                        new ArmorGrantData(ModModels.Armor.HOY_82, "You have been granted the Beskar armor set!"),
                        new ArmorGrantData(ModModels.Armor.HOY_82_GROGU, "You have been granted the Beskar & Grogu armor set!"),
                        ModList.get().isLoaded("woldsvaults")
                                ? new ArmorGrantData(ModModels.WoldsBattleStaffs.DARKSABER, "You have been granted the Darksaber Battlestaff Transmog!", ModItems.BATTLESTAFF.asItem())
                                : null
                ).filter(Objects::nonNull).toList()), // Filter out null entries

                // TigerShrimp88
                new PlayerModelGrants("tigershrimp88", List.of(
                        new ArmorGrantData(ModModels.Armor.HOKAGE_ROBES, "You have been granted the Hokage Robes armor set!"),
                        new ArmorGrantData(ModModels.Armor.HOKAGE_ROBES_MASKLESS, "You have been granted the Hokage Robes Maskless Armor Set!")
                ))
        );
    }

    public static class ArmorGrantData {
        final DynamicModel<?> model;
        final String message;
        final Item item;

        ArmorGrantData(DynamicModel<?> model, String message) {
            this(model, message, null);
        }

        ArmorGrantData(DynamicModel<?> model, String message, Item item) {
            this.model = model;
            this.message = message;
            this.item = item;
        }

        public DynamicModel<?> getModel() {
            return model;
        }

        public String getMessage() {
            return message;
        }

        public Item getItem() {
            return item;
        }

    }
}
