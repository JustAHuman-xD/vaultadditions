package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.init.ModModels;
import iskallia.vault.config.Config;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransmogUnlocksConfig extends Config {
    @Expose
    public Map<String, List<String>> unlocks = new HashMap<>();

    public Map<UUID, List<DynamicModel<?>>> transmogUnlocks = new HashMap<>();

    public List<DynamicModel<?>> getUnlocks(Player player) {
        return transmogUnlocks.get(player.getUUID());
    }

    @Override
    public <T extends Config> T readConfig() {
        super.readConfig();
        for (Map.Entry<String, List<String>> entry : unlocks.entrySet()) {
            UUID uuid;
            try {
                uuid = UUID.fromString(entry.getKey());
            } catch (Exception e) {
                VaultAdditions.LOGGER.error("[Transmog Unlocks Config] Invalid UUID: {}, skipping", entry.getKey());
                continue;
            }

            List<DynamicModel<?>> transmogs = new ArrayList<>();
            for (String modelId : unlocks.get(entry.getKey())) {
                DynamicModel<?> model = ModDynamicModels.REGISTRIES.getModelByResourceLocation(ResourceLocation.tryParse(modelId)).orElse(null);
                if (model == null) {
                    VaultAdditions.LOGGER.warn("[Transmog Unlocks Config] Invalid transmog model: {}, skipping", modelId);
                    continue;
                }
                transmogs.add(model);
            }

            transmogUnlocks.put(uuid, transmogs);
        }
        return (T) this;
    }

    @Override
    protected void reset() {
        // Hoy Unlocks
        unlocks.put("5b61cfde-84b8-4b12-b375-26fe25be0443", List.of(
                ModModels.Armor.HOY_82.getModel().getId().toString(),
                ModModels.Armor.HOY_82_GROGU.getModel().getId().toString(),
                ModModels.Armor.DINDJARIN.getModel().getId().toString(),
                ModModels.GeckoArmor.GROGU.getModel().getId().toString(),
                ModModels.Item.DARKSABER.getModel().getId().toString(),
                ModModels.GeckoItem.DARKSABER2.getModel().getId().toString()
        ));
        // Tiger Unlocks
        unlocks.put("8561dac7-e879-4d98-a92f-c379614eaa4e", List.of(
                ModModels.Armor.HOKAGE_ROBES.getModel().getId().toString(),
                ModModels.Armor.HOKAGE_ROBES_MASKLESS.getModel().getId().toString()
        ));
    }

    @Override
    public String getName() {
        return "vaultadditions_transmog_unlocks";
    }
}
