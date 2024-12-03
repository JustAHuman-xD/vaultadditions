package io.github.a1qs.vaultadditions.config;

import io.github.a1qs.vaultadditions.config.vault.*;

public class CustomVaultConfigRegistry {
    public static PowerGUIConfig POWERS_GUI;
    public static PowerConfig POWERS;
    public static VaultStatueLootConfig STATUE_LOOT_VAULT;
    public static GiftStatueLootConfig STATUE_LOOT_GIFT;
    public static MegaGiftStatueLootConfig STATUE_LOOT_MEGA_GIFT;
    public static ArenaGiftStatueLootConfig STATUE_LOOT_ARENA;
    public static EventConfig EVENT_CONFIG;
    public static NameProviderConfig NAME_PROVIDER_CONFIG;

    public static void registerCustomConfigs() {
        POWERS_GUI = new PowerGUIConfig().readConfig();
        POWERS = new PowerConfig().readConfig();
        STATUE_LOOT_VAULT = new VaultStatueLootConfig().readConfig();
        STATUE_LOOT_GIFT = new GiftStatueLootConfig().readConfig();
        STATUE_LOOT_MEGA_GIFT = new MegaGiftStatueLootConfig().readConfig();
        STATUE_LOOT_ARENA = new ArenaGiftStatueLootConfig().readConfig();
        NAME_PROVIDER_CONFIG = new NameProviderConfig().readConfig();
        EVENT_CONFIG = new EventConfig().readConfig();
    }
}
