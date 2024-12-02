package io.github.a1qs.vaultadditions.util;



import io.github.a1qs.vaultadditions.data.PlayerAdditionalVaultStatData;
import io.github.a1qs.vaultadditions.vault.PlayerAdditionalVaultStats;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.source.SkillSource;
import iskallia.vault.util.function.ObservableSupplier;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class MiscUtil {
    public static UUID sizeScaleModifierUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static int unspentPowerPoints;
    public static final ObservableSupplier<Integer> POWER_POINT_SUPPLIER = ObservableSupplier.of(() -> unspentPowerPoints, Integer::equals);
    public static Component unspentPowerPointComponent;
    public static int unspentPowerPointComponentWidth;

    public static SkillContext ofPowers(ServerPlayer player) {
        PlayerVaultStats stats = PlayerVaultStatsData.get((ServerLevel)player.level).getVaultStats(player);
        PlayerAdditionalVaultStats additionalStats = PlayerAdditionalVaultStatData.get((ServerLevel)player.level).getVaultStats(player);
        return new SkillContext(stats.getVaultLevel(), additionalStats.getUnspentPowerPoints(), 0, SkillSource.of(player));
    }

}
