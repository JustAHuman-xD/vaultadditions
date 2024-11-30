package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.github.a1qs.vaultadditions.events.Event;
import io.github.a1qs.vaultadditions.events.EventEntry;
import iskallia.vault.config.Config;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.resources.ResourceLocation;

import java.util.List;


public class EventConfig extends Config {
    @Expose
    private final WeightedList<EventEntry> eventList = new WeightedList<>();

    @Override
    public String getName() {
        return "vaultadditions_events";
    }

    @Override
    protected void reset() {
        this.eventList.clear();

        // Add entries to the event list
        eventList.add(new EventEntry(
                Event.BORDER_EXPANSION_ENABLED,
                createComplexStartMessage(),
                createComplexEndMessage(),
                createComplexLoginMessage(),
                createComplexDisplayMessage(),
                200L,
                false), 2);

        eventList.add(new EventEntry(
                Event.BORDER_EXPANSION_ENABLED,
                createComplexStartMessage(),
                createComplexEndMessage(),
                createComplexLoginMessage(),
                createComplexDisplayMessage(),
                200L,
                true), 2);

        eventList.add(new EventEntry(
                        Event.ADD_PORTAL_MODIFIERS,
                        createComplexStartMessage(),
                        createComplexEndMessage(),
                        createComplexLoginMessage(),
                        createComplexDisplayMessage(),
                        200L,
                        true,
                        List.of(new ResourceLocation("the_vault:energizing"), new ResourceLocation("the_vault:energizing"), new ResourceLocation("the_vault:energizing"), new ResourceLocation("the_vault:energizing"))),
                3);

        eventList.add(new EventEntry(
                        Event.ADD_PORTAL_MODIFIERS,
                        createComplexStartMessage(),
                        createComplexEndMessage(),
                        createComplexLoginMessage(),
                        createComplexDisplayMessage(),
                        200L,
                        true,
                        List.of(new ResourceLocation("the_vault:energizing"), new ResourceLocation("the_vault:item_quantity"))),
                3);
    }

    public WeightedList<EventEntry> getWeightedList() {
        return eventList;
    }




    private JsonElement createComplexStartMessage() {
        JsonArray jsonArray = new JsonArray();

        // Adding styled text components
        JsonObject part1 = new JsonObject();
        part1.addProperty("text", "This is a ");
        part1.addProperty("color", "yellow");

        JsonObject part2 = new JsonObject();
        part2.addProperty("text", "start message");
        part2.addProperty("color", "green");
        part2.addProperty("bold", true);

        JsonObject part3 = new JsonObject();
        part3.addProperty("text", " with dynamic content. (Total Duration: {totalEventDurationTicks} ticks)");
        part3.addProperty("color", "blue");


        // Add parts to the array
        jsonArray.add(part1);
        jsonArray.add(part2);
        jsonArray.add(part3);

        return jsonArray;
    }

    private JsonElement createComplexEndMessage() {
        JsonArray jsonArray = new JsonArray();

        // Adding styled text components
        JsonObject part1 = new JsonObject();
        part1.addProperty("text", "This is an ");
        part1.addProperty("color", "yellow");
        part1.addProperty("bold", true);

        JsonObject part2 = new JsonObject();
        part2.addProperty("text", "end message");
        part2.addProperty("color", "dark_green");

        JsonObject part3 = new JsonObject();
        part3.addProperty("text", " with dynamic content. (isCrystalSubmission? {crystalSubmission})");
        part3.addProperty("color", "gold");


        // Add parts to the array
        jsonArray.add(part1);
        jsonArray.add(part2);
        jsonArray.add(part3);

        return jsonArray;
    }

    private JsonElement createComplexLoginMessage() {
        JsonArray jsonArray = new JsonArray();

        // Adding styled text components
        JsonObject part1 = new JsonObject();
        part1.addProperty("text", "This is a ");
        part1.addProperty("color", "green");

        JsonObject part2 = new JsonObject();
        part2.addProperty("text", "login message");
        part2.addProperty("color", "blue");
        part2.addProperty("bold", true);

        JsonObject part3 = new JsonObject();
        part3.addProperty("text", " with dynamic content. (Time Remaining: {eventDurationDays}d, {eventDurationHours}h, {eventDurationMinutes}m, {eventDurationSeconds}s )");
        part3.addProperty("color", "red");


        // Add parts to the array
        jsonArray.add(part1);
        jsonArray.add(part2);
        jsonArray.add(part3);

        return jsonArray;
    }

    private JsonElement createComplexDisplayMessage() {
        JsonArray jsonArray = new JsonArray();

        // Adding styled text components
        JsonObject part1 = new JsonObject();
        part1.addProperty("text", "Message");
        part1.addProperty("color", "light_purple");


        JsonObject part2 = new JsonObject();
        part2.addProperty("text", " with dynamic content. (MinCrystalsSubmitted: {minCrystalsSubmitted})");
        part2.addProperty("color", "red");


        // Add parts to the array
        jsonArray.add(part1);
        jsonArray.add(part2);

        return jsonArray;
    }
}
