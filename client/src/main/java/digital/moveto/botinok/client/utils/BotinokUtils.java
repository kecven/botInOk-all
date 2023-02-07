package digital.moveto.botinok.client.utils;

import digital.moveto.botinok.client.config.ClientConst;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Character.MAX_RADIX;

public class BotinokUtils {
    public static String getRandomSearchKeyword() {
        int id = (int) (Math.random() * ClientConst.SEARCH_KEYWORDS.size());
        return ClientConst.SEARCH_KEYWORDS.get(id);
    }

    public static String generateRandomSid() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            result.append(Integer.toString((int) (Math.random() * MAX_RADIX), MAX_RADIX));
        }
        return result.toString();
    }

    public static boolean checkShabatDay() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        DayOfWeek dayOfWeek = localDateTimeNow.getDayOfWeek();

        return dayOfWeek == DayOfWeek.FRIDAY && localDateTimeNow.getHour() >= 16
                || dayOfWeek == DayOfWeek.SATURDAY && localDateTimeNow.getHour() <= 21;
    }


    public static boolean equalsDateAndDateTime(LocalDate date, LocalDateTime dateTime){
        return date.getYear() == dateTime.getYear()
                &&     date.getMonthValue() == dateTime.getMonthValue()
                &&     date.getDayOfMonth() == dateTime.getDayOfMonth();
    }
}
