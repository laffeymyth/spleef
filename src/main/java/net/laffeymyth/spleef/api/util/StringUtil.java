package net.laffeymyth.spleef.api.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@UtilityClass
public class StringUtil {
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public String getDate() {
        return DATE_FORMAT.format(CALENDAR.getTime());
    }

    public String convertSeconds(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
