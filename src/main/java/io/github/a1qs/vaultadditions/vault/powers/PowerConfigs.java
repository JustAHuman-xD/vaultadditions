package io.github.a1qs.vaultadditions.vault.powers;



import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.source.SkillSource;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PowerConfigs {
    public static SpecialExpertisesGUIConfig SPECIAL_EXPERTISES_GUI;
    public static SpecialExpertisesConfig SPECIAL_EXPERTISES;

    public static SkillContext ofSpecialExpertise(ServerPlayer player) {
        PlayerVaultStats stats = PlayerVaultStatsData.get((ServerLevel)player.level).getVaultStats(player);
        return new SkillContext(stats.getVaultLevel(), stats.getUnspentArchetypePoints(), 0, SkillSource.of(player));
    }
}
