package io.github.a1qs.vaultadditions.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PowerCrystalData extends SavedData {
    protected static final String DATA_NAME = "vaultadditions_PowerCrystalData";
    private final Map<UUID, Integer> playerContributedCrystals = new HashMap<>();

    public static PowerCrystalData load(CompoundTag nbt) {
        PowerCrystalData data = new PowerCrystalData();
        ListTag playersList = nbt.getList("Players", 10); // 10 = CompoundTag type

        for (int i = 0; i < playersList.size(); i++) {
            CompoundTag playerTag = playersList.getCompound(i);
            UUID playerId = UUID.fromString(playerTag.getString("UUID"));
            int powerValue = playerTag.getInt("ContributedCrystals");
            data.playerContributedCrystals.put(playerId, powerValue);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag playersList = new ListTag();

        for (Map.Entry<UUID, Integer> entry : playerContributedCrystals.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putString("UUID", entry.getKey().toString());
            playerTag.putInt("ContributedCrystals", entry.getValue());
            playersList.add(playerTag);
        }

        nbt.put("Players", playersList);
        return nbt;
    }


    public void setPlayerContributedCrystals(UUID playerId, int contributedGemstones) {
        playerContributedCrystals.put(playerId, contributedGemstones);
        this.setDirty();
    }

    public void addCrystalContribution(UUID playerId, int contributed) {
        playerContributedCrystals.put(playerId, playerContributedCrystals.getOrDefault(playerId, 0) + contributed);
        this.setDirty();
    }

    public int getPlayerContributedCrystals(UUID playerId) {
        return playerContributedCrystals.getOrDefault(playerId, 0);
    }

    public Map<UUID, Integer> getPlayerContributionsMap() {
        return playerContributedCrystals;
    }

    public void resetContributionsForPlayer(UUID playerId) {
        playerContributedCrystals.remove(playerId);
        this.setDirty();
    }

    public int getTotalContributedCrystals() {
        return playerContributedCrystals
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }


    //Gets the data via the current serverlifecycle
    public static PowerCrystalData getServer() {
        return get(ServerLifecycleHooks.getCurrentServer());
    }

    //uses the ServerLevel to get the server and execute get
    public static PowerCrystalData get(ServerLevel level) {
        return get(level.getServer());
    }

    //uses the server to get the overworlddatastorage
    public static PowerCrystalData get(MinecraftServer srv) {
        return srv.overworld().getDataStorage().computeIfAbsent(PowerCrystalData::load, PowerCrystalData::new, DATA_NAME);
    }
}

