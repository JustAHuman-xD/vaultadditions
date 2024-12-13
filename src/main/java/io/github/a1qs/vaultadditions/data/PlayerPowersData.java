package io.github.a1qs.vaultadditions.data;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import io.github.a1qs.vaultadditions.util.MiscUtil;
import io.github.a1qs.vaultadditions.vault.PlayerAdditionalVaultStats;
import io.github.a1qs.vaultadditions.vault.menu.PowerTree;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.snapshot.AttributeSnapshotHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerPowersData extends SavedData {
    protected static final String DATA_NAME = "vaultadditions_PlayerPowers";
    private Map<UUID, PowerTree> playerMap = new HashMap<>();
    private final Set<UUID> scheduledMerge = new HashSet<>();
    private PowerTree previous;

    public PlayerPowersData() {
    }

    public PowerTree getPowers(Player player) {
        return this.getPowers(player.getUUID());
    }

    public PowerTree getPowers(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, (uuid1) -> {
            return CustomVaultConfigRegistry.POWERS.getAll().copy();
        });
    }

    public void resetAllPlayerPowerTrees(ServerLevel level) {
        this.playerMap.clear();
        this.setDirty();

        for (ServerPlayer player : level.players()) {
            this.getPowers(player).sync(SkillContext.of(player));
        }

    }

    public void resetPowers(ServerPlayer player) {
        this.getPowers(player).iterate(LearnableSkill.class, (skill) -> {
            skill.onRemove(SkillContext.of(player));
        });
        this.playerMap.remove(player.getUUID());
        this.setDirty();
        this.getPowers(player).sync(SkillContext.of(player));
    }

    public boolean isDirty() {
        return true;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isServer()) {
                PlayerPowersData data = get((ServerLevel)event.world);
                if (data.previous != CustomVaultConfigRegistry.POWERS.getAll()) {
                    data.previous = CustomVaultConfigRegistry.POWERS.getAll();
                    data.scheduledMerge.addAll(data.playerMap.keySet());
                }
            }

        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isServer()) {
                Player var2 = event.player;
                if (var2 instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer)var2;
                    PlayerPowersData data = get(player.getLevel());
                    if (data.scheduledMerge.remove(player.getUUID())) {
                        SkillContext context = SkillContext.of(player);
                        data.playerMap.put(player.getUUID(), (PowerTree) (data.playerMap.get(player.getUUID())).mergeFrom(CustomVaultConfigRegistry.POWERS.getAll().copy(), context));
                        SkillContext ctx = MiscUtil.ofPowers(player);
                        PlayerAdditionalVaultStats stats2 = PlayerAdditionalVaultStatData.get((ServerLevel)player.level).getVaultStats(player);
                        stats2.setPowerPoints(ctx.getLearnPoints());

                        AttributeSnapshotHelper.getInstance().refreshSnapshotDelayed(player);
                    }
                    data.getPowers(player).onTick(SkillContext.of(player));
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void onTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.START) {
//            if (event.side.isServer()) {
//                Player var2 = event.player;
//                if (var2 instanceof ServerPlayer) {
//                    ServerPlayer player = (ServerPlayer)var2;
//                    PlayerPowersData data = get(player.getLevel());
//                    if (data.scheduledMerge.remove(player.getUUID())) {
//                        SkillContext context = MiscUtil.ofPowers(player);
//                        data.playerMap.put(player.getUUID(), (PowerTree) (data.playerMap.get(player.getUUID())).mergeFrom(MiscUtil.POWERS.getAll().copy(), context));
////                        PlayerVaultStats stats = PlayerVaultStatsData.get((ServerLevel)player.level).getVaultStats(player);
////                        stats.setSkillPoints(context.getLearnPoints());
////                        stats.setRegretPoints(context.getRegretPoints());
////                        SkillContext ctx = MiscUtil.ofPowers(player);
//                        PlayerAdditionalVaultStats stats2 = PlayerAdditionalVaultStatData.get((ServerLevel)player.level).getVaultStats(player);
//                        stats2.setPowerPoints(context.getLearnPoints());
//
//                        AttributeSnapshotHelper.getInstance().refreshSnapshotDelayed(player);
//                    }
//
//                    data.getPowers(player).onTick(MiscUtil.ofPowers(player));
//                }
//            }
//
//        }
//    }

    private static PlayerPowersData create(CompoundTag tag) {
        PlayerPowersData data = new PlayerPowersData();
        data.load(tag);
        return data;
    }

    public void load(CompoundTag nbt) {
        this.playerMap.clear();
        this.scheduledMerge.clear();
        ListTag playerList = nbt.getList("Players", 8);
        ListTag talentList = nbt.getList("Powers", 10);
        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        } else {
            for(int i = 0; i < playerList.size(); ++i) {
                UUID playerUUID = UUID.fromString(playerList.getString(i));
                Adapters.SKILL.readNbt(talentList.getCompound(i)).ifPresent((tree) -> {
                    this.playerMap.put(playerUUID, (PowerTree)tree);
                    this.scheduledMerge.add(playerUUID);
                });
            }

            this.setDirty();
        }
    }

    public CompoundTag save(CompoundTag nbt) {
        ListTag playerList = new ListTag();
        ListTag talentList = new ListTag();
        this.playerMap.forEach((uuid, researchTree) -> {
            Adapters.SKILL.writeNbt(researchTree).ifPresent((tag) -> {
                playerList.add(StringTag.valueOf(uuid.toString()));
                talentList.add(tag);
            });
        });
        nbt.put("Players", playerList);
        nbt.put("Powers", talentList);
        return nbt;
    }

    public static PlayerPowersData getServer() {
        return get(ServerLifecycleHooks.getCurrentServer());
    }

    public static PlayerPowersData get(ServerLevel world) {
        return get(world.getServer());
    }

    public static PlayerPowersData get(MinecraftServer srv) {
        return srv.overworld().getDataStorage().computeIfAbsent(PlayerPowersData::create, PlayerPowersData::new, DATA_NAME);
    }
}
