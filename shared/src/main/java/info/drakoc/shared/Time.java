package info.drakoc.shared;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class Time {

    public static String convertToLocalTime(String time) {
        ZonedDateTime isoDateTime = ZonedDateTime.parse(time);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime localZonedDateTime = isoDateTime.withZoneSameInstant(zoneId);
        return localZonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}
