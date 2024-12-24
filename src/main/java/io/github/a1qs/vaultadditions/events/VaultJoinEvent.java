package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import io.github.a1qs.vaultadditions.config.vault.ExtraVaultTimeContributionsConfig;
import io.github.a1qs.vaultadditions.data.PowerCrystalData;
import io.github.a1qs.vaultadditions.vault.core.time.modifier.PowerCrystalExtension;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.time.TickClock;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultJoinEvent {

    @SubscribeEvent
    public static void onPlayerVaultEnter(iskallia.vault.event.event.VaultJoinEvent event) {
        MinecraftServer srv = ServerLifecycleHooks.getCurrentServer();

        Vault vault = event.getVault();
        boolean alreadyApplied = vault.get(Vault.CLOCK)
                .get(TickClock.MODIFIERS)
                .stream()
                .anyMatch(modifier -> modifier instanceof PowerCrystalExtension);

        if (alreadyApplied) return;


        ExtraVaultTimeContributionsConfig cfg = CustomVaultConfigRegistry.EXTRA_VAULT_TIME_CONTRIBUTIONS;
        UUID ownerID = vault.get(Vault.OWNER);
        PowerCrystalData powerCrystalData = PowerCrystalData.get(srv);

        int extraTicks = calculateCappedTicks(
                powerCrystalData.getPlayerContributedCrystals(ownerID),
                cfg.CONTRIBUTIONS_UNTIL_TIME_INCREASE_PLAYER,
                cfg.TICKS_PER_EFFECTIVE_CONTRIBUTION_PLAYER,
                cfg.MAX_EXTRA_PLAYER_TICKS
        );

        int totalContributions = powerCrystalData.getPlayerContributionsMap()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        int extraTotalTicks = calculateCappedTicks(
                totalContributions,
                cfg.CONTRIBUTIONS_UNTIL_TIME_INCREASE_SERVER,
                cfg.TICKS_PER_EFFECTIVE_CONTRIBUTION_SERVER,
                cfg.MAX_EXTRA_SERVER_TICKS
        );

        int totalTicks = extraTicks + extraTotalTicks;
        vault.get(Vault.CLOCK).addModifier(new PowerCrystalExtension(vault.get(Vault.ID), totalTicks));



    }

    private static int calculateCappedTicks(int contributions, int contributionsUntilIncrease, int ticksPerContribution, int maxTicks) {
        int ticks = (contributions / contributionsUntilIncrease) * ticksPerContribution;
        return Math.min(ticks, maxTicks);
    }
}
