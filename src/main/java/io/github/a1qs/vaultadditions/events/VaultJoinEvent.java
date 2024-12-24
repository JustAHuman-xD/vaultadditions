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


        UUID ownerID = vault.get(Vault.OWNER);
        PowerCrystalData powerCrystalData = PowerCrystalData.get(srv);

        int extraTicks = CustomVaultConfigRegistry.EXTRA_VAULT_TIME_CONTRIBUTIONS.getPlayerCappedTicks(powerCrystalData.getPlayerContributedCrystals(ownerID));
        int extraTotalTicks = CustomVaultConfigRegistry.EXTRA_VAULT_TIME_CONTRIBUTIONS.getServerCappedTicks(powerCrystalData.getTotalContributedCrystals());

        int totalTicks = extraTicks + extraTotalTicks;
        vault.get(Vault.CLOCK).addModifier(new PowerCrystalExtension(vault.get(Vault.ID), totalTicks));
    }


}
