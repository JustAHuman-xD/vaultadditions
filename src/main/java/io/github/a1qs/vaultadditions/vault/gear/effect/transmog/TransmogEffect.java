package io.github.a1qs.vaultadditions.vault.gear.effect.transmog;

import com.google.gson.JsonElement;

import java.util.HashSet;
import java.util.Set;

public abstract class TransmogEffect {
    private static final Set<TransmogEffect> TYPES = new HashSet<>();
    protected static void registerType(TransmogEffect type) {
        TYPES.add(type);
    }

    public abstract JsonElement serialize();
    public abstract TransmogEffect deserialize(JsonElement json);

    public static TransmogEffect deserializeEffect(JsonElement json) {
        for (TransmogEffect type : TYPES) {
            TransmogEffect effect = type.deserialize(json);
            if (effect != null) {
                return effect;
            }
        }
        return null;
    }
}
