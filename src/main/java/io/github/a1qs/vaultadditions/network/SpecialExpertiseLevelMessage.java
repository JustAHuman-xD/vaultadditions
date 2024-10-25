package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.test.PlayerSpecialExpertiseData;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import io.github.a1qs.vaultadditions.vault.powers.PowerConfigs;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpecialExpertiseLevelMessage {
    private final String expertiseName;
    private final boolean isUpgrade;

    public SpecialExpertiseLevelMessage(String expertiseName, boolean isUpgrade) {
        this.expertiseName = expertiseName;
        this.isUpgrade = isUpgrade;
    }

    public static void encode(SpecialExpertiseLevelMessage message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.expertiseName);
        buffer.writeBoolean(message.isUpgrade);
    }

    public static SpecialExpertiseLevelMessage decode(FriendlyByteBuf buffer) {
        return new SpecialExpertiseLevelMessage(buffer.readUtf(), buffer.readBoolean());
    }

    public static void handle(SpecialExpertiseLevelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = (NetworkEvent.Context)contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                if (message.isUpgrade) {
                    upgradeExpertise(message, sender);
                }

            }
        });
        context.setPacketHandled(true);
    }

    private static void upgradeExpertise(SpecialExpertiseLevelMessage message, ServerPlayer player) {
        ServerLevel level = player.getLevel();
        PlayerVaultStatsData statsData = PlayerVaultStatsData.get(level);
        PlayerSpecialExpertiseData expertisesData = PlayerSpecialExpertiseData.get(level);
        SpecialExpertiseTree expertiseTree = expertisesData.getSpecialExpertises(player);
        if (!ModConfigs.SKILL_GATES.getGates().isLocked(message.expertiseName, expertiseTree)) {
            expertiseTree.getForId(message.expertiseName).ifPresent((skill) -> {
                SkillContext context = PowerConfigs.ofSpecialExpertise(player);
                if (skill instanceof LearnableSkill learnable) {
                    if (learnable.canLearn(context)) {
                        learnable.learn(context);
                        PlayerVaultStats stats = statsData.getVaultStats(player);
                        int learnPoints = stats.getUnspentArchetypePoints() - context.getLearnPoints();
                        stats.spendArchetypePoints(player.getServer(), learnPoints);
                        expertiseTree.sync(context);
                    }
                }

            });
        }
    }

}
