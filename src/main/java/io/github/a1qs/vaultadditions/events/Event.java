package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.VaultAdditions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private final ResourceLocation eventId;
    private final String eventMessage;
    private final int eventWeight;
    private final long eventDuration;
    private final boolean crystalSubmission;
    private int requiredCrystals;
    private int crystalsSubmitted;


    public Event(ResourceLocation eventId, String eventMessage, int eventWeight, long eventDuration, boolean crystalSubmission, int requiredCrystals, int crystalsSubmitted) {
        this.eventId = eventId;
        this.eventMessage = eventMessage;
        this.eventWeight = eventWeight;
        this.eventDuration = eventDuration;
        this.crystalSubmission = crystalSubmission;
        this.requiredCrystals = requiredCrystals;
        this.crystalsSubmitted = crystalsSubmitted;
    }

    public ResourceLocation getId() {
        return eventId;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public int getEventWeight() {
        return eventWeight;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public boolean isCrystalSubmission() {
        return crystalSubmission;
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

    public void setCrystalsSubmitted(int crystalsSubmitted) {
        this.crystalsSubmitted = crystalsSubmitted;
    }

    public void addCrystalsSubmitted(int crystalsSubmitted) {
        this.crystalsSubmitted = Math.min(this.crystalsSubmitted + crystalsSubmitted, getRequiredCrystals());
    }

    public boolean isModifierActive() {
        return this.crystalsSubmitted >= requiredCrystals;
    }

    public static CompoundTag serialize(Event event) {
        CompoundTag tag = new CompoundTag();
        tag.putString("EventId", event.getId().toString());
        tag.putString("EventMessage", event.getEventMessage());
        tag.putInt("EventWeight", event.getEventWeight());
        tag.putLong("EventDuration", event.getEventDuration());
        tag.putBoolean("IsCrystalSubmission", event.isCrystalSubmission());
        tag.putInt("RequiredCrystals", event.getRequiredCrystals());
        tag.putInt("SubmittedCrystals", event.getCrystalsSubmitted());
        return tag;
    }

    public static Event deserialize(CompoundTag tag) {
        return new Event(
                new ResourceLocation(tag.getString("EventId")),
                tag.getString("EventMessage"),
                tag.getInt("EventWeight"),
                tag.getLong("EventDuration"),
                tag.getBoolean("IsCrystalSubmission"),
                tag.getInt("RequiredCrystals"),
                tag.getInt("SubmittedCrystals")
        );
    }

    public static final Map<String, ResourceLocation> EVENT_IDS = new HashMap<>();
    public static final ResourceLocation BORDER_EXPANSION_ENABLED = new ResourceLocation(VaultAdditions.MOD_ID, "expansion_enabled");
    public static final ResourceLocation PORTAL_MODIFIER_1 = new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_1");
    public static final ResourceLocation PORTAL_MODIFIER_2 = new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_2");
    public static final ResourceLocation PORTAL_MODIFIER_3 = new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_3");
    public static final ResourceLocation PORTAL_MODIFIER_4 = new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_4");
    public static final ResourceLocation PORTAL_MODIFIER_5 = new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_5");
    public static final Event FALLBACK = new Event(
            new ResourceLocation(VaultAdditions.MOD_ID, "fallback"),
            "Fallback Event, Report to author",
            1,
            300,
            false,
            0,
            0
    );

    static {
        EVENT_IDS.put("BORDER_EXPANSION_ENABLED", BORDER_EXPANSION_ENABLED);
        EVENT_IDS.put("PORTAL_MODIFIER_1", PORTAL_MODIFIER_1);
        EVENT_IDS.put("PORTAL_MODIFIER_2", PORTAL_MODIFIER_2);
        EVENT_IDS.put("PORTAL_MODIFIER_3", PORTAL_MODIFIER_3);
        EVENT_IDS.put("PORTAL_MODIFIER_4", PORTAL_MODIFIER_4);
        EVENT_IDS.put("PORTAL_MODIFIER_5", PORTAL_MODIFIER_5);
    }
}
