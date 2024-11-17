package io.github.a1qs.vaultadditions.events;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Event {
    private static final Random rand = new Random();
    private final ResourceLocation eventId;
    private final String eventMessage;
    private final int eventWeight;
    private final long eventDuration;
    private final boolean crystalSubmission;
    private int requiredCrystals;
    private int crystalsSubmitted;

    public Event(ResourceLocation eventId, String eventMessage, int eventWeight, long eventDuration, boolean crystalSubmission) {
        this.eventId = eventId;
        this.eventMessage = eventMessage;
        this.eventWeight = eventWeight;
        this.eventDuration = eventDuration;
        this.crystalSubmission = crystalSubmission;
        this.requiredCrystals = 0;
        if(crystalSubmission) requiredCrystals = rand.nextInt(ServerConfigs.CRYSTAL_SUBMIT_MIN.get(), ServerConfigs.CRYSTAL_SUBMIT_MAX.get()+1);
    }

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

    public void setCrystalsSubmitted(int crystalsSubmitted) {
        this.crystalsSubmitted = crystalsSubmitted;
    }

    public static CompoundTag serialize(Event event) {
        CompoundTag tag = new CompoundTag();
        tag.putString("EventId", event.getId().toString());
        tag.putString("EventMessage", event.getEventMessage());
        tag.putInt("EventWeight", event.getEventWeight());
        tag.putLong("EventDuration", event.getEventDuration());
        tag.putBoolean("IsCrystalSubmission", event.isCrystalSubmission());
        return tag;
    }

    public static Event deserialize(CompoundTag tag) {
        return new Event(new ResourceLocation(tag.getString("EventId")),
                tag.getString("EventMessage"),
                tag.getInt("EventWeight"),
                tag.getLong("EventDuration"),
                tag.getBoolean("IsCrystalSubmission")
        );
    }

    public static final Map<String, ResourceLocation> EVENT_IDS = new HashMap<>();

    static {
        EVENT_IDS.put("BORDER_EXPANSION_ENABLED", new ResourceLocation(VaultAdditions.MOD_ID, "expansion_enabled"));
        EVENT_IDS.put("PORTAL_MODIFIER_1", new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_1"));
        EVENT_IDS.put("PORTAL_MODIFIER_2", new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_2"));
        EVENT_IDS.put("PORTAL_MODIFIER_3", new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_3"));
        EVENT_IDS.put("PORTAL_MODIFIER_4", new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_4"));
        EVENT_IDS.put("PORTAL_MODIFIER_5", new ResourceLocation(VaultAdditions.MOD_ID, "event_modifier_5"));
    }
}
