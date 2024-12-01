package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.CustomVaultConfigRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class Event {
    public static final ResourceLocation BORDER_EXPANSION_ENABLED = new ResourceLocation(VaultAdditions.MOD_ID, "event_re_enable_border_expansion");
    public static final ResourceLocation ADD_PORTAL_MODIFIERS = new ResourceLocation(VaultAdditions.MOD_ID, "event_add_portal_modifiers");
    public static final ResourceLocation ADD_VAULT_COMPLETION_ITEM = new ResourceLocation(VaultAdditions.MOD_ID, "event_vault_completion_item");

    private final int configIndex;
    private int requiredCrystals;
    private int crystalsSubmitted;


    public Event(int configIndex, int requiredCrystals, int crystalsSubmitted) {
        this.configIndex = configIndex;
        this.requiredCrystals = requiredCrystals;
        this.crystalsSubmitted = crystalsSubmitted;
    }

    public static CompoundTag serialize(Event event) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ConfigIndex", event.getConfigIndex());
        tag.putInt("RequiredCrystals", event.getRequiredCrystals());
        tag.putInt("SubmittedCrystals", event.getCrystalsSubmitted());
        return tag;
    }

    public static Event deserialize(CompoundTag tag) {
        return new Event(
                tag.getInt("ConfigIndex"),
                tag.getInt("RequiredCrystals"),
                tag.getInt("SubmittedCrystals")
        );
    }

    public int getRequiredCrystals() {
        return requiredCrystals;
    }

    public int getCrystalsSubmitted() {
        return crystalsSubmitted;
    }

    public void setRequiredCrystals(int requiredCrystals) {
        this.requiredCrystals = requiredCrystals;
    }

    public void addCrystalsSubmitted(int crystalsSubmitted) {
        this.crystalsSubmitted = Math.min(this.crystalsSubmitted + crystalsSubmitted, getRequiredCrystals());
    }

    public boolean isModifierActive() {
        return this.crystalsSubmitted >= requiredCrystals;
    }

    public int getConfigIndex() {
        return configIndex;
    }

    // Event data getters

    public ResourceLocation getEventId() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.getEventId();
    }

    public Component getEventStartMessage() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex())
                .value.getParsedMessage(
                        CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(
                                this.getConfigIndex()
                        ).value.getEventStartMessage());
    }

    public Component getEventEndMessage() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex())
                .value.getParsedMessage(
                        CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(
                                this.getConfigIndex()
                        ).value.getEventEndMessage());
    }

    public Component getEventLoginMessage() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex())
                .value.getParsedMessage(
                        CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(
                                this.getConfigIndex()
                        ).value.getEventLoginMessage());
    }

    public Component getEventDisplayMessage() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex())
                .value.getParsedMessage(
                        CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(
                                this.getConfigIndex()
                        ).value.getEventDisplayMessage());
    }

    public Component getEventEnabledMessage() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex())
                .value.getParsedMessage(
                        CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(
                                this.getConfigIndex()
                        ).value.getEventEnabledMessage());
    }

    public boolean isCrystalSubmissionEvent() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.isCrystalSubmission();
    }

    public Map<ResourceLocation, Integer> getAdditionalModifiers() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.getAdditionalModifiers();
    }

    public long getEventDuration() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.getEventDuration();
    }

    public ItemStack getItemStack() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.getItemToAdd();
    }

    public float getChance() {
        return CustomVaultConfigRegistry.EVENT_CONFIG.getWeightedList().get(this.getConfigIndex()).value.getChance();
    }



}
