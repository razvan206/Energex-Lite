package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class TimeConversionUtils {
    public static int intervalToIndex(String interval) {
        String[] hourAndMinutes = interval.split("\\s*[:\\-]\\s*");
        final int hour = Integer.parseInt(hourAndMinutes[0]);
        final int minutes = Integer.parseInt(hourAndMinutes[1]);
        return hour * 4 + (minutes / 15) + 1;
    }
    public static String indexToInterval(int index){
        int bruteMinutes = index * 15;
        int secondHour = bruteMinutes / 60;
        int firstHour;
        int firstMinutes;
        int secondMinutes = (int)((bruteMinutes / 60.0 - secondHour) * 60);
        if (secondMinutes == 0){
            firstHour = secondHour - 1;
            firstMinutes = 45;
        }
        else{
            firstHour = secondHour;
            firstMinutes = secondMinutes - 15;
        }
        String res = String.format("%02d:%02d - %02d:%02d",firstHour,firstMinutes,secondHour,secondMinutes);
        return res;
    }
    public static int timestampToInterval(String timestamp){
        int index = timestamp.indexOf("T") + 1;
        String first = timestamp.substring(index);
        String[] hourAndMinutes = first.split(":");
        int firstHour = Integer.parseInt(hourAndMinutes[0]);
        int firstMinutes = Integer.parseInt(hourAndMinutes[1]);
        int secondHour;
        int secondMinutes;
        if(firstMinutes != 45){
            secondHour = firstHour;
            secondMinutes = firstMinutes + 15;
        }
        else{
            secondHour = firstHour + 1;
            secondMinutes = 0;
        }
        String second = String.format("%02d:%02d",secondHour,secondMinutes);
        String res = first + " - " + second;
        return intervalToIndex(res);
    }
    public static boolean isDateValid(String text){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm").withResolverStyle(ResolverStyle.STRICT);
            LocalDateTime.parse(text, formatter);
            return true;
        }
        catch (DateTimeParseException e){
            return false;
        }
    }
}
