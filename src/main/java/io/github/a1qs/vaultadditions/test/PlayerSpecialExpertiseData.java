package io.github.a1qs.vaultadditions.test;

import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import io.github.a1qs.vaultadditions.vault.powers.PowerConfigs;
import iskallia.vault.core.data.adapter.Adapters;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.snapshot.AttributeSnapshotHelper;
import iskallia.vault.world.data.PlayerVaultStatsData;
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
public class PlayerSpecialExpertiseData extends SavedData {
    protected static final String DATA_NAME = "vaultadditions_PlayerSpecialExpertises";
    private Map<UUID, SpecialExpertiseTree> playerMap = new HashMap<>();
    private final Set<UUID> scheduledMerge = new HashSet<>();
    private SpecialExpertiseTree previous;

    public PlayerSpecialExpertiseData() {
    }

    public SpecialExpertiseTree getSpecialExpertises(Player player) {
        return this.getSpecialExpertises(player.getUUID());
    }

    public SpecialExpertiseTree getSpecialExpertises(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, (uuid1) -> {
            return PowerConfigs.SPECIAL_EXPERTISES.getAll().copy();
        });
    }

    public void resetAllPlayerSpecialExpertiseTrees(ServerLevel level) {
        this.playerMap.clear();
        this.setDirty();
        Iterator var2 = level.players().iterator();

        while(var2.hasNext()) {
            ServerPlayer player = (ServerPlayer)var2.next();
            this.getSpecialExpertises(player).sync(SkillContext.of(player));
        }

    }

    public void resetSpecialExpertiseTree(ServerPlayer player) {
        this.getSpecialExpertises(player).iterate(LearnableSkill.class, (skill) -> {
            skill.onRemove(SkillContext.of(player));
        });
        this.playerMap.remove(player.getUUID());
        this.setDirty();
        this.getSpecialExpertises(player).sync(SkillContext.of(player));
    }

    public boolean isDirty() {
        return true;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isServer()) {
                PlayerSpecialExpertiseData data = get((ServerLevel)event.world);
                if (data.previous != PowerConfigs.SPECIAL_EXPERTISES.getAll()) {
                    data.previous = PowerConfigs.SPECIAL_EXPERTISES.getAll();
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
                    PlayerSpecialExpertiseData data = get(player.getLevel());
                    if (data.scheduledMerge.remove(player.getUUID())) {
                        SkillContext context = SkillContext.of(player);
                        data.playerMap.put(player.getUUID(), (SpecialExpertiseTree) (data.playerMap.get(player.getUUID())).mergeFrom(PowerConfigs.SPECIAL_EXPERTISES.getAll().copy(), context));
                        PlayerVaultStats stats = PlayerVaultStatsData.get((ServerLevel)player.level).getVaultStats(player);
                        stats.setSkillPoints(context.getLearnPoints());
                        stats.setRegretPoints(context.getRegretPoints());
                        AttributeSnapshotHelper.getInstance().refreshSnapshotDelayed(player);
                    }

                    data.getSpecialExpertises(player).onTick(SkillContext.of(player));
                }
            }

        }
    }

    private static PlayerSpecialExpertiseData create(CompoundTag tag) {
        PlayerSpecialExpertiseData data = new PlayerSpecialExpertiseData();
        data.load(tag);
        return data;
    }

    public void load(CompoundTag nbt) {
        this.playerMap.clear();
        this.scheduledMerge.clear();
        ListTag playerList = nbt.getList("Players", 8);
        ListTag talentList = nbt.getList("SpecialExpertises", 10);
        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        } else {
            for(int i = 0; i < playerList.size(); ++i) {
                UUID playerUUID = UUID.fromString(playerList.getString(i));
                Adapters.SKILL.readNbt(talentList.getCompound(i)).ifPresent((tree) -> {
                    this.playerMap.put(playerUUID, (SpecialExpertiseTree)tree);
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
        nbt.put("SpecialExpertises", talentList);
        return nbt;
    }

    public static PlayerSpecialExpertiseData getServer() {
        return get(ServerLifecycleHooks.getCurrentServer());
    }

    public static PlayerSpecialExpertiseData get(ServerLevel world) {
        return get(world.getServer());
    }

    public static PlayerSpecialExpertiseData get(MinecraftServer srv) {
        return (PlayerSpecialExpertiseData)srv.overworld().getDataStorage().computeIfAbsent(PlayerSpecialExpertiseData::create, PlayerSpecialExpertiseData::new, DATA_NAME);
    }
}
