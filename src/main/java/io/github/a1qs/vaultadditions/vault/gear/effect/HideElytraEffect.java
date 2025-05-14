package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

public class HideElytraEffect extends TransmogEffect {
    public static final HideElytraEffect INSTANCE = new HideElytraEffect();

    @Override
    public MutableComponent getTooltip() {
        return new TextComponent("Hides Elytra");
    }

    @Override
    public JsonElement serialize() {
        return new JsonPrimitive(type());
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        return this;
    }
}
