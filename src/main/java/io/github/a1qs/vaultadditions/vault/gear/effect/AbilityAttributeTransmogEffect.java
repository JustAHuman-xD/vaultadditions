package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.a1qs.vaultadditions.util.VaultGearAttributeHelper;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.attribute.ability.AbilityLevelAttribute;

public class AbilityAttributeTransmogEffect extends AttributeTransmogEffect<AbilityLevelAttribute> {
    private final String abilityName;
    private final int abilityLevel;

    public AbilityAttributeTransmogEffect(String abilityName, int abilityLevel) {
        this.abilityName = abilityName;
        this.abilityLevel = abilityLevel;
    }

    @Override
    public VaultGearAttributeInstance<AbilityLevelAttribute> getVaultGearAttributeInstance() {
        return VaultGearAttributeHelper.abilityLevelAttributeInstance(this.abilityName, this.abilityLevel);
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = withType();
        json.addProperty("abilityName", this.abilityName);
        json.addProperty("abilityLevel", this.abilityLevel);
        return json;
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        return new AbilityAttributeTransmogEffect(object.get("abilityName").getAsString(), object.get("abilityLevel").getAsInt());
    }
}
