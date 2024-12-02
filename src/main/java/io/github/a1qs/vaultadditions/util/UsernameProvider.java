package io.github.a1qs.vaultadditions.util;

import net.minecraftforge.common.UsernameCache;

import java.util.Random;

public class UsernameProvider {
    public static String getRandomKnownUsername() {
        Random generator = new Random();
        Object[] values = UsernameCache.getMap().values().toArray();
        return (String) values[generator.nextInt(values.length)];
    }


}
