package io.github.a1qs.vaultadditions.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEntry {
    @Expose
    public final ResourceLocation eventId;

    @Expose
    public final JsonElement eventMessage;

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



    public EventEntry(ResourceLocation eventId, JsonElement eventMessage, long eventDuration, boolean crystalSubmission) {
        this.eventId = eventId;
        this.eventMessage = eventMessage;
        this.eventDuration = eventDuration;
        this.crystalSubmission = crystalSubmission;
        if(crystalSubmission) minCrystalsSubmitted = 50;
        if(crystalSubmission) maxCrystalsSubmitted = 250;
    }

    public EventEntry(ResourceLocation eventId, JsonElement eventMessage, long eventDuration, boolean crystalSubmission, List<ResourceLocation> resourceLocationList) {
        this.eventId = eventId;
        this.eventMessage = eventMessage;
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
    public Component getParsedMessage() {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("eventDuration", String.valueOf(this.eventDuration));
        placeholders.put("eventId", this.eventId.toString());
        placeholders.put("crystalSubmission", String.valueOf(this.crystalSubmission));
        placeholders.put("minCrystalsSubmitted", String.valueOf(this.minCrystalsSubmitted));
        placeholders.put("maxCrystalsSubmitted", String.valueOf(this.maxCrystalsSubmitted));

        if (eventMessage.isJsonArray()) {
            JsonArray messageArray = eventMessage.getAsJsonArray();
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
    private JsonObject substitutePlaceholders(JsonObject json, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            if (json.has("text")) {
                String text = json.get("text").getAsString();
                json.addProperty("text", text.replace(placeholder, entry.getValue()));
            }
        }
        return json;
    }


}
