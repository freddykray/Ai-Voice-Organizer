package by.freddykray.AI.Voice.Organizer.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateTextFormatter {

    private static final Locale RU_LOCALE = new Locale("ru");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM", RU_LOCALE);

    public String formatDeadline(Instant dateTime, boolean hasExactTime) {
        if (dateTime == null) {
            return "без дедлайна";
        }

        ZonedDateTime zonedDateTime = dateTime.atZone(ZONE_ID);
        String datePart = DATE_FORMATTER.format(zonedDateTime);

        if (!hasExactTime) {
            return "до " + datePart;
        }

        return "до " + datePart + " в " + buildTimeText(zonedDateTime);
    }

    public String formatReminder(Instant dateTime, boolean hasExactTime) {
        if (dateTime == null) {
            return "без напоминания";
        }

        ZonedDateTime zonedDateTime = dateTime.atZone(ZONE_ID);
        String datePart = DATE_FORMATTER.format(zonedDateTime);

        if (!hasExactTime) {
            return datePart;
        }

        return datePart + " в " + buildTimeText(zonedDateTime);
    }

    private String buildTimeText(ZonedDateTime zonedDateTime) {
        int hour24 = zonedDateTime.getHour();
        int hour12 = convertTo12HourFormat(hour24);

        return hour12 + " " + getHourWord(hour12) + " " + getPeriodOfDay(hour24);
    }

    private int convertTo12HourFormat(int hour24) {
        if (hour24 == 0) {
            return 12;
        }
        if (hour24 > 12) {
            return hour24 - 12;
        }
        return hour24;
    }

    private String getHourWord(int hour) {
        int lastTwoDigits = hour % 100;
        int lastDigit = hour % 10;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            return "часов";
        }

        return switch (lastDigit) {
            case 1 -> "час";
            case 2, 3, 4 -> "часа";
            default -> "часов";
        };
    }

    private String getPeriodOfDay(int hour24) {
        if (hour24 >= 0 && hour24 < 6) {
            return "ночи";
        }
        if (hour24 >= 6 && hour24 < 12) {
            return "утра";
        }
        if (hour24 >= 12 && hour24 < 18) {
            return "дня";
        }
        return "вечера";
    }
}