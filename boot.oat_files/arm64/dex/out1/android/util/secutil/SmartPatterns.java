package android.util.secutil;

import android.content.Context;
import com.android.internal.R;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SmartPatterns {
    public static String COUNTRY_DATE_STRING = "";
    public static String COUNTRY_TIME_STRING = "";
    public static final String DEFAULT_DATE_STRING_TYPE1 = "(((19|20)(([02468][048])|([13579][26]))[\\-|\\/|\\.]0?2[\\-|\\/|\\.]29)|((((20[0-9][0-9])|(19[0-9][0-9]))[\\-|\\/|\\.])?(((0?[13578]|10|12)[\\-|\\/|\\.]31)|((0?[1,3-9]|1[0-2])[\\-|\\/|\\.](29|30))|((0?[1-9]|1[0-2])[\\-|\\/|\\.](1[0-9]|2[0-8]|0?[1-9])))[[:space:]]))";
    public static final String DEFAULT_DATE_STRING_TYPE2 = "(((Jan|January|Mar|March|May|Jul|July|Aug|August|Oct|October|Dec|December)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Apr|April|Jun|June|Sep|September|Nov|November)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Feb|February)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]]))";
    public static final String DEFAULT_TIME_STRING = "(((0[1-9]|1[1-2])[[:space:]]?\\:[[:space:]]?[0-5][0-9][[:space:]]?(am|pm|AM|PM))|(([0-1][0-9]|2[0-3])[[:space:]]?\\:[[:space:]]?[0-5][0-9]))";
    public static final Pattern EMAIL_ADDRESS_WEAK = Pattern.compile("([a-zA-Z0-9\\+\\.\\_\\%\\-\\+­]{1,256}[[:space:]]?\\@[[:space:]]?[a-zA-Z0-9][a-zA-Z0-9\\-­]{0,64}([[:space:]]?\\.[[:space:]]?[a-zA-Z0-9][a-zA-Z0-9\\-­]{0,25})+)");
    public static final Pattern HYPHEN = Pattern.compile("(­)");
    public static final Pattern PHONE_NUMBER = Pattern.compile("([[:space:]](\\+[0-9]+[\\- \\.­]*)?(\\([0-9]+\\)[\\- \\.­]*)?([0-9][0-9\\- \\.­][0-9\\- \\.­]+[0-9])[[:space:]])");
    public static final Pattern PHONE_NUMBER_WEAK = Pattern.compile("((\\+[0-9]+[[[:space:]]?\\-\\.­]*)?(\\([0-9]+\\)[[[:space:]]?\\-\\.­]*)?([0-9][0-9[[:space:]]?\\-\\.­][0-9[[:space:]]?\\-\\.­]+[0-9]))");
    public static final String PREFIX_FOR_DATE_MILLIS = "((Date|date|날짜)[[[:space:]]\\:\\;\\-]+)";
    public static final String PREFIX_FOR_TIME_MILLIS = "((Time|time|시간)[[[:space:]]\\:\\;\\-]+)";
    public static final Pattern REFACTORING_PHONE_NUMBER = Pattern.compile("((\\+[0-9]+[[[:space:]]?\\-\\.­]*)?(\\([0-9]+\\)[[[:space:]]?\\-\\.­]*)?([0-9][0-9[[:space:]]?\\-\\.­][0-9[[:space:]]?\\-\\.­]+[0-9]))");
    public static final String SPILT_PATTERN_DATE_TYPE1 = "[[[:space:]]\\-\\/\\.년월일]+";
    public static final String SPILT_PATTERN_DATE_TYPE2 = "[[[:space:]]\\,\\.]+";
    public static Map<String, Integer> globalDateMap = new LinkedHashMap();

    static {
        globalDateMap.put("Jan", Integer.valueOf(1));
        globalDateMap.put("January", Integer.valueOf(1));
        globalDateMap.put("Feb", Integer.valueOf(2));
        globalDateMap.put("February", Integer.valueOf(2));
        globalDateMap.put("Mar", Integer.valueOf(3));
        globalDateMap.put("March", Integer.valueOf(3));
        globalDateMap.put("Apr", Integer.valueOf(4));
        globalDateMap.put("April", Integer.valueOf(4));
        globalDateMap.put("May", Integer.valueOf(5));
        globalDateMap.put("Jun", Integer.valueOf(6));
        globalDateMap.put("June", Integer.valueOf(6));
        globalDateMap.put("Jul", Integer.valueOf(7));
        globalDateMap.put("July", Integer.valueOf(7));
        globalDateMap.put("Aug", Integer.valueOf(8));
        globalDateMap.put("August", Integer.valueOf(8));
        globalDateMap.put("Sep", Integer.valueOf(9));
        globalDateMap.put("September", Integer.valueOf(9));
        globalDateMap.put("Octo", Integer.valueOf(10));
        globalDateMap.put("October", Integer.valueOf(10));
        globalDateMap.put("Nov", Integer.valueOf(11));
        globalDateMap.put("November", Integer.valueOf(11));
        globalDateMap.put("Dec", Integer.valueOf(12));
        globalDateMap.put("December", Integer.valueOf(12));
    }

    private SmartPatterns() {
    }

    public static String getCountryDateString(Context context) {
        return context.getString(R.string.smart_patterns_date_format);
    }

    public static String getCountryTimeString(Context context) {
        return context.getString(R.string.smart_patterns_time_format);
    }

    public static Pattern getSmartPatternsForDate(Context context) {
        COUNTRY_DATE_STRING = context.getString(R.string.smart_patterns_date_format);
        return Pattern.compile("(((19|20)(([02468][048])|([13579][26]))[\\-|\\/|\\.]0?2[\\-|\\/|\\.]29)|((((20[0-9][0-9])|(19[0-9][0-9]))[\\-|\\/|\\.])?(((0?[13578]|10|12)[\\-|\\/|\\.]31)|((0?[1,3-9]|1[0-2])[\\-|\\/|\\.](29|30))|((0?[1-9]|1[0-2])[\\-|\\/|\\.](1[0-9]|2[0-8]|0?[1-9])))[[:space:]]))|(((Jan|January|Mar|March|May|Jul|July|Aug|August|Oct|October|Dec|December)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Apr|April|Jun|June|Sep|September|Nov|November)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Feb|February)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]]))" + COUNTRY_DATE_STRING);
    }

    public static Pattern getSmartPatternsForTime(Context context) {
        COUNTRY_TIME_STRING = context.getString(R.string.smart_patterns_time_format);
        return Pattern.compile("((((0[1-9]|1[1-2])[[:space:]]?\\:[[:space:]]?[0-5][0-9][[:space:]]?(am|pm|AM|PM))|(([0-1][0-9]|2[0-3])[[:space:]]?\\:[[:space:]]?[0-5][0-9]))" + COUNTRY_TIME_STRING + ")");
    }
}
