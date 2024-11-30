package io.github.a1qs.vaultadditions.config.vault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
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

        // Create JSON text components
        JsonElement complexMessage = createComplexTextComponent();

        // Add entries to the event list
        eventList.add(new EventEntry(
                new ResourceLocation("vaultadditions:test1"),
                complexMessage,
                200L,
                false), 2);

        eventList.add(new EventEntry(
                new ResourceLocation("vaultadditions:test2"),
                complexMessage,
                200L,
                false), 54);

        eventList.add(new EventEntry(
                new ResourceLocation("vaultadditions:test3"),
                complexMessage,
                200L,
                true), 54534);

        eventList.add(new EventEntry(
                        new ResourceLocation("vaultadditions:test4"),
                        complexMessage,
                        200L,
                        true,
                        List.of(new ResourceLocation("the_vault:dev1"), new ResourceLocation("the_vault:dev2"))),
                3289);
    }

    public WeightedList<EventEntry> getWeightedList() {
        return eventList;
    }




    private JsonElement createComplexTextComponent() {
        JsonArray jsonArray = new JsonArray();

        // Adding styled text components
        JsonObject part1 = new JsonObject();
        part1.addProperty("text", "This is a ");
        part1.addProperty("color", "yellow");

        JsonObject part2 = new JsonObject();
        part2.addProperty("text", "test message");
        part2.addProperty("color", "green");
        part2.addProperty("bold", true);

        JsonObject part3 = new JsonObject();
        part3.addProperty("text", " with dynamic content.");
        part3.addProperty("color", "blue");

        JsonObject part4 = new JsonObject();
        part4.addProperty("text", "\nThis is a test for the placeholders: EVENTDURATION: {eventDuration}");
        part4.addProperty("color", "red");

        // Add parts to the array
        jsonArray.add(part1);
        jsonArray.add(part2);
        jsonArray.add(part3);
        jsonArray.add(part4);

        return jsonArray;
    }
}
