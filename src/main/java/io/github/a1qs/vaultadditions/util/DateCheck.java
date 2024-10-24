package io.github.a1qs.vaultadditions.util;

import io.github.a1qs.vaultadditions.VaultAdditions;
import io.github.a1qs.vaultadditions.config.CommonConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;

public class DateCheck {
    private static String date = CommonConfigs.STOP_ACCEPTING_GEMSTONES_DATE.get();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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
        try {
            LocalDate configDate = LocalDate.parse(date, formatter);
            LocalDate current = LocalDate.now();

            if (current.isEqual(configDate) || current.isAfter(configDate)) {
                return true;
            }
        } catch (DateTimeParseException e) {
            VaultAdditions.LOGGER.error(e.toString());
        }
        return false;
    }

    public static MutableComponent untilDateMessage() {
        try {
            LocalDateTime targetDateTime = LocalDateTime.parse(date, formatter);
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


}
