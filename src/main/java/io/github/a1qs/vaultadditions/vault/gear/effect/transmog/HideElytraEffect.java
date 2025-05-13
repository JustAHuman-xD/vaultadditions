package io.github.a1qs.vaultadditions.vault.gear.effect.transmog;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class HideElytraEffect extends TransmogEffect {
    private static final String KEY = "hideElytra";
    public static final HideElytraEffect INSTANCE = new HideElytraEffect();

    @Override
    public JsonElement serialize() {
        return new JsonPrimitive(KEY);
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        return json instanceof JsonPrimitive primitive && primitive.isString()
                && primitive.getAsString().equals(KEY) ? this : null;
    }
}
