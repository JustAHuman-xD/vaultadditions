package io.github.a1qs.vaultadditions.data;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.events.Event;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventData extends SavedData {

    private static final String DATA_NAME = "vaultadditions_EventData";
    private final Map<ResourceLocation, Event> eventMap = new HashMap<>();
    private final List<String> scheduledEvents = new ArrayList<>();
    private Event activeEvent;
    private long eventDuration;
    private boolean isActive;

    public void startEvent(Event event) {
        VaultAdditions.LOGGER.info("Started Event: {}", event.getId());
        this.eventDuration = event.getEventDuration();
        this.isActive = true;
        this.activeEvent = event;
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(
                new TextComponent(event.getEventMessage()).withStyle(ChatFormatting.YELLOW),
                ChatType.SYSTEM,
                Util.NIL_UUID
        );
        setDirty();
    }


    public void stopEvent() {
        VaultAdditions.LOGGER.info("Event Ended: {}", this.getActiveEvent());
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new TextComponent("Stopped Event"), ChatType.SYSTEM, Util.NIL_UUID);
        this.activeEvent = null;
        this.isActive = false;
        setDirty();
    }


    @SubscribeEvent
    public static void onEventDataTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        EventData eventData = EventData.get(ServerLifecycleHooks.getCurrentServer());

        if(eventData.isActive) {
            eventData.eventDuration--;
            if (eventData.eventDuration <= 0) {

                eventData.stopEvent();
            }
            eventData.setDirty();
            return;
        }

        List<String> eventsToRemove = new ArrayList<>();

        for (String scheduledEvent : eventData.scheduledEvents) {
            long timestamp = TimeUtil.convertToEpochMillis(scheduledEvent);
            if (timestamp == -1) {
                VaultAdditions.LOGGER.error("Invalid Timestamp: {}", scheduledEvent);
                eventsToRemove.add(scheduledEvent);
                continue;
            }

            if (eventData.isTimePassed(timestamp)) {
                eventData.startEvent(eventData.selectRandomEvent());
                eventsToRemove.add(scheduledEvent);
            }
        }

        eventData.scheduledEvents.removeAll(eventsToRemove);
        eventData.setDirty();
    }

    private Event selectRandomEvent() {
        int totalWeight = eventMap.values().stream().mapToInt(Event::getEventWeight).sum();
        if (totalWeight == 0) {
            return new Event(
                    new ResourceLocation(VaultAdditions.MOD_ID, "fallback"),
                    "Fallback Event, Report to author",
                    1,
                    300,
                    false
            );
        }

        int randomWeight = (int) (Math.random() * totalWeight);
        for (Event event : eventMap.values()) {
            randomWeight -= event.getEventWeight();
            if (randomWeight < 0) {
                return event;
            }
        }

        return new Event(
                new ResourceLocation(VaultAdditions.MOD_ID, "fallback"),
                "Fallback Event, Report to author",
                1,
                300,
                false
        );
    }

    public boolean removeEventByResourceLocation(ResourceLocation resourceLocation) {
        for (ResourceLocation entry : eventMap.keySet()) {
            if (entry.equals(resourceLocation)) {
                eventMap.remove(resourceLocation);
                setDirty();
                return true;
            }
        }
        return false;
    }

    public void addEvent(Event event) {
        eventMap.put(event.getId(), event);
        setDirty();
    }

    public void addEventDate(String date) {
        scheduledEvents.add(date);
        setDirty();
    }

    private boolean isTimePassed(long timestamp) {
        return System.currentTimeMillis() >= timestamp;
    }

    public boolean isEventActive() {
        return isActive;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public Event getActiveEvent() {
        return activeEvent;
    }

    public List<String> getScheduledEvents() {
        return scheduledEvents;
    }

    public String getNextScheduledEvent() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        return getScheduledEvents().stream()
                .filter(event -> {
                    try {
                        // Parse the event string into a LocalDateTime
                        LocalDateTime eventDateTime = LocalDateTime.parse(event, TimeUtil.TIME_FORMAT);
                        return eventDateTime.isAfter(now.toLocalDateTime());
                    } catch (Exception e) {
                        // If parsing fails, treat the event as invalid
                        return false;
                    }
                })
                .min(Comparator.comparing(event -> {
                    LocalDateTime eventDateTime = LocalDateTime.parse(event, TimeUtil.TIME_FORMAT);
                    return eventDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }))
                .orElse("No upcoming events!");
    }

    public List<ResourceLocation> getAllEventIds() {
        return new ArrayList<>(eventMap.keySet());
    }

    // Load-Save methods
    public static EventData load(CompoundTag nbt) {
        EventData data = new EventData();


        ListTag eventList = nbt.getList("EventData", Tag.TAG_COMPOUND);
        for (int i = 0; i < eventList.size(); i++) {
            CompoundTag eventTag = eventList.getCompound(i);

            ResourceLocation eventId = new ResourceLocation(eventTag.getString("EventId"));
            String eventMessage = eventTag.getString("EventMessage");
            int eventWeight = eventTag.getInt("EventWeight");
            long eventDuration = eventTag.getLong("EventDuration");
            boolean crystalSubmission = eventTag.getBoolean("CrystalSubmission");
            int requiredCrystals = eventTag.getInt("RequiredCrystals");
            int crystalsSubmitted = eventTag.getInt("SubmittedCrystals");


            Event event = new Event(eventId, eventMessage, eventWeight, eventDuration, crystalSubmission, requiredCrystals,crystalsSubmitted);
            data.eventMap.put(eventId, event);
        }

        ListTag scheduledEventTag = nbt.getList("ScheduledEvents", Tag.TAG_STRING); // Use STRING here
        for (int i = 0; i < scheduledEventTag.size(); i++) {
            String time = scheduledEventTag.getString(i);

            if (!time.isEmpty()) {
                data.scheduledEvents.add(time);
            }
        }

        data.isActive = nbt.getBoolean("IsActive");
        data.eventDuration = nbt.getLong("EventDuration");
        if (nbt.contains("ActiveEvent")) {
            data.activeEvent = Event.deserialize((CompoundTag) nbt.get("ActiveEvent"));
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {

        ListTag eventList = new ListTag();
        for (Event event : eventMap.values()) {
            CompoundTag eventTag = new CompoundTag();
            eventTag.putString("EventId", event.getId().toString());
            eventTag.putString("EventMessage", event.getEventMessage());
            eventTag.putInt("EventWeight", event.getEventWeight());
            eventTag.putLong("EventDuration", event.getEventDuration());
            eventTag.putBoolean("CrystalSubmission", event.isCrystalSubmission());
            eventTag.putInt("RequiredCrystals", event.getRequiredCrystals());
            eventTag.putInt("SubmittedCrystals", event.getCrystalsSubmitted());
            eventList.add(eventTag);
        }
        nbt.put("EventData", eventList);

        ListTag scheduledEventTag = new ListTag();
        for (String time : scheduledEvents) {
            scheduledEventTag.add(StringTag.valueOf(time));
        }
        nbt.put("ScheduledEvents", scheduledEventTag);

        nbt.putBoolean("IsActive", isActive);
        nbt.putLong("EventDuration", eventDuration);

        if(isActive) {
            nbt.put("ActiveEvent", Event.serialize(activeEvent));
        }

        return nbt;
    }

    // Data-getters
    public static EventData getServer() {
        return get(ServerLifecycleHooks.getCurrentServer());
    }

    public static EventData get(ServerLevel level) {
        return get(level.getServer());
    }

    public static EventData get(MinecraftServer srv) {
        return srv.overworld().getDataStorage().computeIfAbsent(EventData::load, EventData::new, DATA_NAME);
    }
}

