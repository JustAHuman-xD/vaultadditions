package io.github.a1qs.vaultadditions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.a1qs.vaultadditions.data.EventData;
import io.github.a1qs.vaultadditions.events.Event;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

public class EventCommands {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_EVENTS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(Event.EVENT_IDS.values(), builder);

    public EventCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultadditions")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("event")
                        .then(Commands.literal("stopEvent")
                                .executes(this::stopEvent)
                        )
                        .then(Commands.literal("getCurrentEvent")
                                .executes(this::getCurrentEvent)
                        )
                        .then(Commands.literal("scheduleEventDate")
                                .then(Commands.argument("dd/MM/yyyy HH:mm:ss", StringArgumentType.string())
                                        .executes(this::scheduleEventDate)
                                )
                        )
                        .then(Commands.literal("clearScheduledEvents")
                                .executes(this::clearScheduledEvents)
                        )
                        .then(Commands.literal("addEventEntry")
                                .then(Commands.argument("eventId", ResourceLocationArgument.id())
                                        .suggests(SUGGEST_EVENTS)
                                        .then(Commands.argument("eventMessage", StringArgumentType.string())
                                                .then(Commands.argument("weight", IntegerArgumentType.integer(1))
                                                        .then(Commands.argument("durationInMin", LongArgumentType.longArg(1))
                                                                .then(Commands.argument("isCrystalSubmission", BoolArgumentType.bool())
                                                                        .executes(this::addEventEntry)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("removeEventEntry")
                                .then(Commands.argument("eventEntry", ResourceLocationArgument.id())
                                        .suggests(suggestEvents())
                                        .executes(this::removeEventEntry)

                                )
                        )
                )
        );
    }



    private int stopEvent(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        data.stopEvent();
        return 0;
    }

    private int getCurrentEvent(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        Event activeEvent = data.getActiveEvent();
        if(activeEvent != null) {
            context.getSource().sendSuccess(new TextComponent("=== ")
                    .append(new TextComponent("Current active Event").withStyle(ChatFormatting.AQUA))
                    .append(new TextComponent(" ===\n").withStyle(ChatFormatting.WHITE))
                    .append(new TextComponent("    Event ID: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.getId() + "\n").withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent("    Event Message: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.getEventMessage() + "\n").withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent("    Event Weight: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.getEventWeight() + "\n").withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent("    Event Duration: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.getEventDuration() + "\n").withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent("    Is Crystal Submission: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.isCrystalSubmission() + "\n").withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent("    Required Crystal Submission: ").withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(new TextComponent(activeEvent.getCrystalsSubmitted() + " / " + activeEvent.getRequiredCrystals())),
                    false);
            return 0;
        } else {
            context.getSource().sendFailure(new TextComponent("No active event found!"));
            return 1;
        }
    }

    private int clearScheduledEvents(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        if(data.getScheduledEvents().isEmpty()) {
            context.getSource().sendFailure(new TextComponent("No scheduled events!"));
            return 1;
        }
        data.getScheduledEvents().clear();
        context.getSource().sendSuccess(new TextComponent("Cleared all scheduled Events!").withStyle(ChatFormatting.YELLOW), true);
        return 0;
    }

    private int scheduleEventDate(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        data.addEventDate(StringArgumentType.getString(context, "dd/MM/yyyy HH:mm:ss"));
        return 0;
    }

    private int addEventEntry(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        Event event = new Event(
                ResourceLocationArgument.getId(context, "eventId"),
                StringArgumentType.getString(context, "eventMessage"),
                IntegerArgumentType.getInteger(context, "weight"),
                LongArgumentType.getLong(context, "durationInMin") * 20 * 60,
                BoolArgumentType.getBool(context, "isCrystalSubmission"),
                0,
                0
                );

        data.addEvent(event);
        context.getSource().sendSuccess(new TextComponent("Added a new Event of type: ")
                        .append(new TextComponent(event.getId().toString()).withStyle(ChatFormatting.YELLOW)),
                true
        );
        return 0;
    }

    private int removeEventEntry(CommandContext<CommandSourceStack> context) {
        EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
        ResourceLocation toRemove = ResourceLocationArgument.getId(context, "eventEntry");
        boolean removed = data.removeEventByResourceLocation(toRemove);
        if(removed) {
            context.getSource().sendSuccess(new TextComponent("Removed Event: ")
                    .append(new TextComponent(toRemove.toString()).withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent(" successfully!")),
                    true
            );
            return 0;
        } else {
            context.getSource().sendFailure(new TextComponent("Event not found: " + toRemove));
            return 1;
        }
    }


    private int getRequiredPermissionLevel() {
        return 2;
    }

    private static SuggestionProvider<CommandSourceStack> suggestEvents() {
        return (commandContext, suggestionsBuilder) -> {
            EventData data = EventData.get(ServerLifecycleHooks.getCurrentServer());
            List<ResourceLocation> eventList = data.getAllEventIds();

            for(ResourceLocation event : eventList) {
                suggestionsBuilder.suggest(event.toString());
            }
            return suggestionsBuilder.buildFuture();
        };
    }
}
