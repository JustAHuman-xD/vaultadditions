package io.github.a1qs.vaultadditions.util;



import io.github.a1qs.vaultadditions.vault.powers.PowerConfig;
import io.github.a1qs.vaultadditions.vault.powers.PowerGUIConfig;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.source.SkillSource;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class MiscUtil {
    public static PowerGUIConfig POWERS_GUI;
    public static PowerConfig POWERS;
    public static UUID sizeScaleModifierUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public static SkillContext ofPowers(ServerPlayer player) {
        PlayerVaultStats stats = PlayerVaultStatsData.get((ServerLevel)player.level).getVaultStats(player);
        return new SkillContext(stats.getVaultLevel(), stats.getUnspentArchetypePoints(), 0, SkillSource.of(player));
    }
}
