package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * This class acts as a helper method that converts some metrics into another
 * ex : interval to index or vice versa, as well as an validationDate method
 */
public final class TimeConversionUtils {
    private static final int MINUTES_PER_QUARTER = 15;
    private static final int QUARTERS_PER_HOUR = 4;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int LAST_QUARTER_MINUTES = 45;
    private static final int HOURS_PER_DAY = 24;

    public TimeConversionUtils() {
    }

    public static int intervalToIndex(final String interval) {
        String[] hourAndMinutes = interval.split("\\s*[:\\-]\\s*");
        final int hour = Integer.parseInt(hourAndMinutes[0]);
        final int minutes = Integer.parseInt(hourAndMinutes[1]);
        return hour * QUARTERS_PER_HOUR + (minutes / MINUTES_PER_QUARTER) + 1;
    }

    public static String indexToInterval(final int index) {
        int bruteMinutes = index * MINUTES_PER_QUARTER;
        int secondHour = bruteMinutes / MINUTES_PER_HOUR;
        int firstHour;
        int firstMinutes;
        int secondMinutes = (int) (((double) bruteMinutes / MINUTES_PER_HOUR
                - secondHour) * MINUTES_PER_HOUR);
        if (secondMinutes == 0) {
            firstHour = secondHour - 1;
            firstMinutes = LAST_QUARTER_MINUTES;
        } else {
            firstHour = secondHour;
            firstMinutes = secondMinutes - MINUTES_PER_QUARTER;
        }
        if (secondHour == HOURS_PER_DAY) {
            secondHour = 0;
        }
        return String.format("%02d:%02d - %02d:%02d",
                firstHour, firstMinutes, secondHour, secondMinutes);
    }

    public static int timestampToInterval(final String timestamp) {
        int index = timestamp.indexOf("T") + 1;
        String first = timestamp.substring(index);
        String[] hourAndMinutes = first.split(":");
        int firstHour = Integer.parseInt(hourAndMinutes[0]);
        int firstMinutes = Integer.parseInt(hourAndMinutes[1]);
        int secondHour;
        int secondMinutes;
        if (firstMinutes != LAST_QUARTER_MINUTES) {
            secondHour = firstHour;
            secondMinutes = firstMinutes + MINUTES_PER_QUARTER;
        } else {
            secondHour = firstHour + 1;
            secondMinutes = 0;
        }
        String second = String.format("%02d:%02d", secondHour, secondMinutes);
        String res = first + " - " + second;
        return intervalToIndex(res);
    }

    public static boolean isDateValid(final String text) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("uuuu-MM-dd'T'HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDateTime.parse(text, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
