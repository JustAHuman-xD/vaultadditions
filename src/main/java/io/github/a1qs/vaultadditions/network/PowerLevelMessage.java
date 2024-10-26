package io.github.a1qs.vaultadditions.network;

import io.github.a1qs.vaultadditions.data.PlayerPowersData;
import io.github.a1qs.vaultadditions.vault.powermenu.PowerTree;
import io.github.a1qs.vaultadditions.util.MiscUtil;
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

public class PowerLevelMessage {
    private final String powerName;
    private final boolean isUpgrade;

    public PowerLevelMessage(String powerName, boolean isUpgrade) {
        this.powerName = powerName;
        this.isUpgrade = isUpgrade;
    }

    public static void encode(PowerLevelMessage message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.powerName);
        buffer.writeBoolean(message.isUpgrade);
    }

    public static PowerLevelMessage decode(FriendlyByteBuf buffer) {
        return new PowerLevelMessage(buffer.readUtf(), buffer.readBoolean());
    }

    public static void handle(PowerLevelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                if (message.isUpgrade) {
                    upgradePower(message, sender);
                }

            }
        });
        context.setPacketHandled(true);
    }

    private static void upgradePower(PowerLevelMessage message, ServerPlayer player) {
        ServerLevel level = player.getLevel();
        PlayerVaultStatsData statsData = PlayerVaultStatsData.get(level);
        PlayerPowersData expertisesData = PlayerPowersData.get(level);
        PowerTree powerTree = expertisesData.getPowers(player);
        if (!ModConfigs.SKILL_GATES.getGates().isLocked(message.powerName, powerTree)) {
            powerTree.getForId(message.powerName).ifPresent((skill) -> {
                SkillContext context = MiscUtil.ofPowers(player);
                if (skill instanceof LearnableSkill learnable) {
                    if (learnable.canLearn(context)) {
                        learnable.learn(context);
                        PlayerVaultStats stats = statsData.getVaultStats(player);
                        int learnPoints = stats.getUnspentArchetypePoints() - context.getLearnPoints();
                        stats.spendArchetypePoints(player.getServer(), learnPoints);
                        powerTree.sync(context);
                    }
                }

            });
        }
    }

}
