package io.github.a1qs.vaultadditions.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.data.EventData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEntry {
    @Expose
    public final ResourceLocation eventId;

    @Expose
    public final JsonElement eventStartMessage;

    @Expose
    public final JsonElement eventEndMessage;

    @Expose
    public final JsonElement eventLoginMessage;

    @Expose
    public final JsonElement eventDisplayMessage;

    @Expose
    public final long eventDuration;

    @Expose
    public final boolean crystalSubmission;

    @Expose
    public List<ResourceLocation> additionalModifiers;

    @Expose
    public int minCrystalsSubmitted;

    @Expose
    public int maxCrystalsSubmitted;

    public EventEntry(ResourceLocation eventId, JsonElement eventStartMessage, JsonElement eventEndMessage, JsonElement eventLoginMessage, JsonElement eventDisplayMessage, long eventDuration, boolean crystalSubmission) {
        this.eventId = eventId;
        this.eventStartMessage = eventStartMessage;
        this.eventEndMessage = eventEndMessage;
        this.eventLoginMessage = eventLoginMessage;
        this.eventDisplayMessage = eventDisplayMessage;
        this.eventDuration = eventDuration;
        this.crystalSubmission = crystalSubmission;
        if(crystalSubmission) minCrystalsSubmitted = 50;
        if(crystalSubmission) maxCrystalsSubmitted = 250;
    }

    public EventEntry(ResourceLocation eventId, JsonElement eventStartMessage, JsonElement eventEndMessage, JsonElement eventLoginMessage, JsonElement eventDisplayMessage, long eventDuration, boolean crystalSubmission, List<ResourceLocation> resourceLocationList) {
        this.eventId = eventId;
        this.eventStartMessage = eventStartMessage;
        this.eventEndMessage = eventEndMessage;
        this.eventLoginMessage = eventLoginMessage;
        this.eventDisplayMessage = eventDisplayMessage;
        this.eventDuration = eventDuration;
        this.crystalSubmission = crystalSubmission;
        if(crystalSubmission) minCrystalsSubmitted = 50;
        if(crystalSubmission) maxCrystalsSubmitted = 250;
        this.additionalModifiers = resourceLocationList;
    }

    /**
     * Parses the `eventMessage` into a `Component`, replacing placeholders with the given values.
     *
     * @return A parsed `Component` ready for rendering.
     */
    public Component getParsedMessage(JsonElement message) {
        EventData d = EventData.getServer(); // bandaid fix LMAO
        boolean eventActive = d.isEventActive();
        Map<String, String> placeholders = new HashMap<>();

        // static event data
        placeholders.put("totalEventDurationTicks", String.valueOf(this.eventDuration));
        placeholders.put("totalEventDurationSeconds", String.valueOf((this.eventDuration / 20) % 60));
        placeholders.put("totalEventDurationMinutes", String.valueOf((this.eventDuration / (20 * 60)) % 60));
        placeholders.put("totalEventDurationHours", String.valueOf((this.eventDuration / (20 * 60 * 60)) % 24));
        placeholders.put("totalEventDurationDays", String.valueOf(this.eventDuration / (20 * 60 * 60 * 24)));
        placeholders.put("eventId", this.eventId.toString());
        placeholders.put("crystalSubmission", String.valueOf(this.crystalSubmission));
        placeholders.put("minCrystalsSubmitted", String.valueOf(this.minCrystalsSubmitted));
        placeholders.put("maxCrystalsSubmitted", String.valueOf(this.maxCrystalsSubmitted));

        // active event based data
        placeholders.put("eventDurationTicks", String.valueOf(eventActive ? d.getEventDuration() : null));
        placeholders.put("eventDurationSeconds", String.valueOf(eventActive ? (d.getEventDuration() / 20) % 60 : null));
        placeholders.put("eventDurationMinutes", String.valueOf(eventActive ? (d.getEventDuration() / 20 * 60) % 60 : null));
        placeholders.put("eventDurationHours", String.valueOf(eventActive ? (d.getEventDuration() / (20 * 60 * 60)) % 24 : null));
        placeholders.put("eventDurationDays", String.valueOf(eventActive ? d.getEventDuration() / (20 * 60 * 60 * 24) : null));
        placeholders.put("requiredCrystals", String.valueOf(eventActive ? d.getActiveEvent().getRequiredCrystals() : null));
        placeholders.put("submittedCrystals", String.valueOf(eventActive ? d.getActiveEvent().getCrystalsSubmitted() : null));

        if (message.isJsonArray()) {
            JsonArray messageArray = message.getAsJsonArray();
            JsonArray substitutedArray = new JsonArray();

            for (JsonElement element : messageArray) {
                if (element.isJsonObject()) {
                    JsonObject substitutedElement = substitutePlaceholders(element.getAsJsonObject(), placeholders);
                    substitutedArray.add(substitutedElement);
                } else {
                    substitutedArray.add(element);
                }
            }

            return Component.Serializer.fromJson(substitutedArray.toString());
        } else {
            throw new IllegalStateException("eventMessage must be a JSON array!");
        }
    }

    /**
     * Replaces placeholders in a JSON object with actual values.
     *
     * @param json The JSON object containing the text component data.
     * @param placeholders A map of placeholder keys to their replacement values.
     * @return A JSON object with placeholders substituted.
     */
    private static JsonObject substitutePlaceholders(JsonObject json, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            if (json.has("text")) {
                String text = json.get("text").getAsString();
                json.addProperty("text", text.replace(placeholder, entry.getValue()));
            }
        }
        return json;
    }



    /**
     *  Getter/Setters
     */

    public ResourceLocation getId() {
        return eventId;
    }

    public JsonElement getEventStartMessage() {
        return eventStartMessage;
    }

    public JsonElement getEventEndMessage() {
        return eventEndMessage;
    }

    public JsonElement getEventLoginMessage() {
        return eventLoginMessage;
    }

    public JsonElement getEventDisplayMessage() {
        return eventDisplayMessage;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public boolean isCrystalSubmission() {
        return crystalSubmission;
    }

    public List<ResourceLocation> getAdditionalModifiers() {
        return additionalModifiers;
    }

    public int getMinCrystalsSubmitted() {
        return minCrystalsSubmitted;
    }

    public int getMaxCrystalsSubmitted() {
        return maxCrystalsSubmitted;
    }
}
