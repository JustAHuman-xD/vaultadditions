package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import net.minecraftforge.common.UsernameCache;

import java.util.Random;

public class UsernameProvider {
    public static String getRandomKnownUsername() {
        Random generator = new Random();
        Object[] values = UsernameCache.getMap().values().toArray();
        return (String) values[generator.nextInt(values.length)];
    }

    public static String getRandomUsername() {
        if(CustomVaultConfigRegistry.NAME_PROVIDER_CONFIG.IS_USED) {
            return CustomVaultConfigRegistry.NAME_PROVIDER_CONFIG.getRandomName();
        }
        return getRandomKnownUsername();
    }


}
