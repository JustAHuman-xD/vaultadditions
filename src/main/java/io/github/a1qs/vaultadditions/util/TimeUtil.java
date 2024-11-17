package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.ServerConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;

public class TimeUtil {

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final Random random = new Random();
    private static final String[] messages = {
            "%d %s, %d %s, and %d %s remain until the World can no longer grow.",
            "%d %s, %d %s, and %d %s left until the World cant grow anymore!",
            "Time is ticking! Only %d %s, %d %s, and %d %s to go until the World will no longer be able to expand.",
            "Just %d %s, %d %s, and %d %s left for the World to stop expanding.",
            "The clock is ticking: %d %s, %d %s, and %d %s remain until the World can no longer be expanded.",
            "Only %d %s, %d, %s, and %d %s until the World border is frozen.",
            "Only %d %s, %d, %s, and %d %s until the World Expansion will halt."
    };


    public static boolean pastDate() {
        if(!ServerConfigs.LIMIT_TIME_FOR_EXPANSION.get()) return true;

        String date = ServerConfigs.STOP_ACCEPTING_GEMSTONES_DATE.get();
        try {
            LocalDate configDate = LocalDate.parse(date, TIME_FORMAT);
            LocalDate current = LocalDate.now(); //Gets the current time based off of SystemTime

            if (current.isEqual(configDate) || current.isAfter(configDate)) {
                return true;
            }
        } catch (DateTimeParseException e) {
            VaultAdditions.LOGGER.error(e.toString());
        }
        return false;
    }

    public static MutableComponent untilDateMessage() {
        String date = ServerConfigs.STOP_ACCEPTING_GEMSTONES_DATE.get();
        try {
            LocalDateTime targetDateTime = LocalDateTime.parse(date, TIME_FORMAT);
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (currentDateTime.isBefore(targetDateTime)) {
                Duration duration = Duration.between(currentDateTime, targetDateTime);

                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                long minutes = duration.toMinutes() % 60;

                String messageTemplate = messages[random.nextInt(messages.length)];

                String dayText = days == 1 ? "Day" : "Days";
                String hourText = hours == 1 ? "Hour" : "Hours";
                String minuteText = minutes == 1 ? "Minute" : "Minutes";

                String formattedMessage = String.format(messageTemplate, days, dayText, hours, hourText, minutes, minuteText);

                return new TextComponent(formattedMessage);
            }
        } catch (DateTimeParseException e) {
            VaultAdditions.LOGGER.error(e.toString());
        }
        return new TextComponent("What did you do. (please report this)").withStyle(ChatFormatting.DARK_RED);
    }

    public static long convertToEpochMillis(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, TIME_FORMAT).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            return -1;
        }
    }
}
