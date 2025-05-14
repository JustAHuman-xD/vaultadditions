package io.github.a1qs.vaultadditions.vault.gear.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class VanillaAttributeArmorTransmogEffect<T> extends AttributeTransmogEffect<T> {
    private final Supplier<VaultGearAttributeInstance<T>> factory;
    private final Attribute attribute;
    private final AttributeModifier.Operation operation;
    private final double value;
    private final AttributeModifier modifier;

    public VanillaAttributeArmorTransmogEffect(Supplier<VaultGearAttributeInstance<T>> factory, Attribute attribute, AttributeModifier.Operation operation, double value) {
        this.factory = factory;
        this.attribute = attribute;
        this.operation = operation;
        this.value = value;
        this.modifier = new AttributeModifier("Armor Set Effect", value, operation);
    }

    @Override
    public VaultGearAttributeInstance<T> getVaultGearAttributeInstance() {
        return factory.get();
    }

    public void apply(Player player) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null && !instance.hasModifier(modifier)) {
            instance.addTransientModifier(modifier);
        }
    }

    public void remove(Player player) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(modifier);
        }
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = withType();
        json.addProperty("attribute", attribute.getRegistryName().toString());
        json.addProperty("operation", operation.name());
        json.addProperty("value", value);
        return json;
    }

    @Override
    public TransmogEffect deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(object.get("attribute").getAsString()));
        AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(object.get("operation").getAsString());
        double value = object.get("value").getAsDouble();
        return new VanillaAttributeArmorTransmogEffect<>(() -> {
            return VaultGearAttributeInstance.cast(ModGearAttributes.getGearAttribute(attribute, operation), value);
        }, attribute, operation, value);
    }
}