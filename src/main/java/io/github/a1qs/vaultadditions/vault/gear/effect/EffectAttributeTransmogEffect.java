package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.custom.effect.EffectGearAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectAttributeTransmogEffect extends AttributeTransmogEffect<EffectGearAttribute> {
    private final MobEffect effect;
    private final int amplifier;

    public EffectAttributeTransmogEffect(MobEffect effect, int amplifier) {
        this.effect = effect;
        this.amplifier = amplifier;
    }

    @Override
    public VaultGearAttributeInstance<EffectGearAttribute> getVaultGearAttributeInstance() {
        return VaultGearAttributeHelper.potionEffectVaultAttributeInstance(this.effect, this.amplifier);
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = withType();
        json.addProperty("effect", this.effect.getRegistryName().toString());
        json.addProperty("amplifier", this.amplifier);
        return null;
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        return new EffectAttributeTransmogEffect(
                ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(object.get("effect").getAsString())),
                object.get("amplifier").getAsInt()
        );
    }
}