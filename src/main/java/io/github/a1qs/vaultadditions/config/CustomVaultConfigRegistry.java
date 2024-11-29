package io.github.a1qs.vaultadditions.config;

import io.github.a1qs.vaultadditions.config.vault.EventConfig;
import io.github.a1qs.vaultadditions.config.vault.PowerConfig;
import io.github.a1qs.vaultadditions.config.vault.PowerGUIConfig;
import io.github.a1qs.vaultadditions.config.vault.StatueLootConfig;

public class CustomVaultConfigRegistry {
    public static PowerGUIConfig POWERS_GUI;
    public static PowerConfig POWERS;
    public static StatueLootConfig STATUE_LOOT;
    public static EventConfig EVENT_CONFIG;

    public static void registerCustomConfigs() {
        POWERS_GUI = new PowerGUIConfig().readConfig();
        POWERS = new PowerConfig().readConfig();
        STATUE_LOOT = new StatueLootConfig().readConfig();
        EVENT_CONFIG = new EventConfig().readConfig();
    }
}
