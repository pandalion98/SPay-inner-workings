package android.sec.clipboard.util;

import java.util.Calendar;
import java.util.Random;

public class StringHelper {
    private static StringHelper instance = new StringHelper();

    public static StringHelper getInstance() {
        return instance;
    }

    public static StringBuffer getUniqueString() {
        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        Calendar oCalendar = Calendar.getInstance();
        sb.append(oCalendar.get(12));
        sb.append(oCalendar.get(13));
        sb.append("_");
        sb.append(oCalendar.get(14));
        sb.append("_");
        sb.append(rand.nextInt(oCalendar.get(14) + 1));
        return sb;
    }
}
