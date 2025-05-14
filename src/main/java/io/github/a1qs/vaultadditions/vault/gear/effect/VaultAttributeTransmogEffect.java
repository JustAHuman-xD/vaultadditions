package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.VaultGearAttributeRegistry;
import iskallia.vault.gear.attribute.VaultGearAttributeSerializer;
import iskallia.vault.gear.attribute.ability.AbilityFloatValueAttribute;
import iskallia.vault.gear.attribute.type.VaultGearAttributeType;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.resources.ResourceLocation;

public class VaultAttributeTransmogEffect<T> extends AttributeTransmogEffect<T> {
    private final VaultGearAttribute<T> attribute;
    private final T value;

    public VaultAttributeTransmogEffect(VaultGearAttribute<T> attribute, T attributeValue) {
        this.attribute = attribute;
        this.value = attributeValue;
    }

    @Override
    public VaultGearAttributeInstance<T> getVaultGearAttributeInstance() {
        return VaultGearAttributeInstance.cast(this.attribute, this.value);
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = withType();
        json.addProperty("attribute", attribute.getRegistryName().toString());
        json.add("value", attribute.getType().serialize(value));
        return null;
    }

    public static <T> VaultAttributeTransmogEffect<T> fromJson(JsonObject json, VaultGearAttribute<T> attribute) {
        T value = VaultGearAttributeType.GSON.fromJson(json.get("value"), attribute.getType());
        AbilityFloatValueAttribute
        return null;
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        return fromJson(object, VaultGearAttributeRegistry.getRegistry().getValue(ResourceLocation.tryParse(object.get("attribute").getAsString())));
    }
}
