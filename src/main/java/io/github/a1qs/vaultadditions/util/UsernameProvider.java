package io.github.a1qs.vaultadditions.util;

import com.mojang.authlib.GameProfile;
import io.github.a1qs.vaultadditions.client.ClientInfoHandler;
import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraftforge.common.UsernameCache;

import java.util.Random;
import java.util.UUID;

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

    public static String getUsernameFromUUID(UUID uuid) {
        String cachedName = ClientInfoHandler.getCachedUsername(uuid);
        if (!cachedName.equals("Unknown")) {
            return cachedName;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null) {
            for (PlayerInfo playerInfo : minecraft.getConnection().getOnlinePlayers()) {
                if (playerInfo.getProfile().getId().equals(uuid)) {
                    String name = playerInfo.getProfile().getName();
                    ClientInfoHandler.updateCachedUsername(uuid, name);
                    return name;
                }
            }
        }

        // Fallback: Attempt to resolve the profile using the session service
        GameProfile profile = minecraft.getMinecraftSessionService().fillProfileProperties(new GameProfile(uuid, null), false);
        if (profile != null && profile.getName() != null) {
            return profile.getName();
        }

        return "Unknown"; // Fallback if no name is found
    }


}
