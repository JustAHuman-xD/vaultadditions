package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.a1qs.vaultadditions.VaultAdditions;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashSet;
import java.util.Set;

public abstract class TransmogEffect {
    private static final Set<TransmogEffect> TYPES = new HashSet<>();
    protected static void register(TransmogEffect type) {
        TYPES.add(type);
    }

    public abstract MutableComponent getTooltip();

    public abstract JsonElement serialize();
    public abstract TransmogEffect deserialize(JsonElement json);

    public String type() {
        return getClass().getSimpleName();
    }
    protected boolean isType(JsonElement json) {
        JsonPrimitive type = json.isJsonPrimitive() ? json.getAsJsonPrimitive() : null;
        if (type == null && json instanceof JsonObject object && object.get("type") instanceof JsonPrimitive primitive) {
            type = primitive;
        }
        return type != null && type.isString() && type.getAsString().equals(type());
    }
    protected JsonObject withType() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type());
        return json;
    }

    public static TransmogEffect deserializeEffect(JsonElement json) {
        for (TransmogEffect type : TYPES) {
            if (type.isType(json)) {
                try {
                    TransmogEffect effect = type.deserialize(json);
                    if (effect != null) {
                        return effect;
                    }
                } catch (Exception e) {
                    VaultAdditions.LOGGER.error("Failed to deserialize transmog effect " + json, e);
                }
            }
        }
        return null;
    }

    public static void registerTypes() {
        TYPES.clear();
        register(new AbilitySoundTransmogEffect(null, null));
        register(new AttributeTransmogEffect<>(null));
        register(new ElytraSoundTransmogEffect(null, 0f));
        register(HideElytraTransmogEffect.INSTANCE);
        register(new VanillaAttributeArmorTransmogEffect<>(null, null, null, 0));
    }
}
