package android.util.secutil;

import android.content.Context;
import android.text.format.Time;
import android.util.Patterns;
import com.android.internal.content.NativeLibraryHelper;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartParser {
    static final boolean DEBUG = true;
    private static final String DELIMITER = "＃";
    static final String TAG = "SmartParser";
    private int dayOfToday;
    Context mContext;
    private int mDegree;
    private SmartParsingData mParsingData;
    private String mWorkStr;
    private String mWorkStrForMillis;
    private int monthOfToday;
    private Calendar today;
    private int yearOfToday;

    public SmartParser(String str, Context context) {
        this.mContext = context;
        this.mDegree = 0;
        init(str);
        doParsing();
    }

    public SmartParser(String str, Context context, int degree) {
        this.mContext = context;
        this.mDegree = degree;
        init(str);
        doParsing();
    }

    public void init(String str) {
        this.mParsingData = new SmartParsingData();
        this.mParsingData.setOriginalData(str);
        this.mWorkStr = " " + str + " ";
        this.mWorkStrForMillis = " " + str + " ";
        this.today = Calendar.getInstance();
        this.yearOfToday = this.today.get(1);
        this.monthOfToday = this.today.get(2);
        this.dayOfToday = this.today.get(5);
    }

    public void doParsing() {
        parsingEmailInfo();
        parsingDateInfo();
        parsingTimeInfo();
        parsingPhoneNumInfo();
        parsingURLInfo();
        parsingDateMillisInfo();
        parsingTimeMillisInfo();
        arrangeRemainData();
        this.mParsingData.setRemainData(this.mWorkStr);
    }

    private void parsingDateMillisInfo() {
        Pattern p1 = Pattern.compile("((((19|20)(([02468][048])|([13579][26]))[\\-|\\/|\\.]0?2[\\-|\\/|\\.]29)|((((20[0-9][0-9])|(19[0-9][0-9]))[\\-|\\/|\\.])?(((0?[13578]|10|12)[\\-|\\/|\\.]31)|((0?[1,3-9]|1[0-2])[\\-|\\/|\\.](29|30))|((0?[1-9]|1[0-2])[\\-|\\/|\\.](1[0-9]|2[0-8]|0?[1-9])))[[:space:]])))");
        Matcher m1 = p1.matcher(this.mWorkStrForMillis);
        this.mWorkStrForMillis = p1.matcher(this.mWorkStrForMillis).replaceAll(DELIMITER);
        while (m1.find()) {
            String matchString = removeUnnecessary(m1.group(0));
            this.mParsingData.setInfo(convertDateToMillis(matchString, 1), 6);
            Log.secD(TAG, "add date for millis(type1): " + matchString);
        }
        Pattern p2 = Pattern.compile("((((Jan|January|Mar|March|May|Jul|July|Aug|August|Oct|October|Dec|December)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Apr|April|Jun|June|Sep|September|Nov|November)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])|((Feb|February)(\\.[[:space:]]?|[[:space:]])((([1-2][0-9]|3[01])(th)?)|0?1(st)?|0?2(nd)?|0?3(rd)?|0?[4-9](th)?)((\\,[[:space:]]?|\\.[[:space:]]?|[[:space:]]?)((20[0-9][0-9])|(19[0-9][0-9]))?)?[[:space:]])))");
        Matcher m2 = p2.matcher(this.mWorkStrForMillis);
        this.mWorkStrForMillis = p2.matcher(this.mWorkStrForMillis).replaceAll(DELIMITER);
        while (m2.find()) {
            matchString = removeUnnecessary(m2.group(0));
            this.mParsingData.setInfo(convertDateToMillis(matchString, 2), 6);
            Log.secD(TAG, "add date for millis(type2): " + matchString);
        }
        String countryDateString = SmartPatterns.getCountryDateString(this.mContext);
        if (countryDateString.length() > 0 && countryDateString.charAt(0) == '|') {
            StringBuilder sb = new StringBuilder(countryDateString);
            sb.deleteCharAt(0);
            Pattern p3 = Pattern.compile("(" + sb.toString() + ")");
            Matcher m3 = p3.matcher(this.mWorkStrForMillis);
            this.mWorkStrForMillis = p3.matcher(this.mWorkStrForMillis).replaceAll(DELIMITER);
            while (m3.find()) {
                matchString = removeUnnecessary(m3.group(0));
                this.mParsingData.setInfo(convertDateToMillis(matchString, 1), 6);
                Log.secD(TAG, "add date for millis(type3, country): " + matchString);
            }
        }
    }

    private void parsingTimeMillisInfo() {
        Pattern p = Pattern.compile("(((((0[1-9]|1[1-2])[[:space:]]?\\:[[:space:]]?[0-5][0-9][[:space:]]?(am|pm|AM|PM))|(([0-1][0-9]|2[0-3])[[:space:]]?\\:[[:space:]]?[0-5][0-9]))" + SmartPatterns.getCountryTimeString(this.mContext) + "))");
        Matcher m = p.matcher(this.mWorkStrForMillis);
        this.mWorkStrForMillis = p.matcher(this.mWorkStrForMillis).replaceAll(DELIMITER);
        while (m.find()) {
            String matchString = removeUnnecessary(m.group(0));
            this.mParsingData.setInfo(convertTimeToMillis(matchString), 7);
            Log.secD(TAG, "add time for millis : " + matchString);
        }
    }

    private void parsingDateInfo() {
        Pattern p1 = Pattern.compile(SmartPatterns.DEFAULT_DATE_STRING_TYPE1);
        Matcher m1 = p1.matcher(this.mWorkStr);
        this.mWorkStr = p1.matcher(this.mWorkStr).replaceAll(DELIMITER);
        while (m1.find()) {
            String matchString = removeUnnecessary(m1.group(0));
            this.mParsingData.setInfo(matchString, 1);
            Log.secD(TAG, "add date(pattern type1): " + matchString);
        }
        Pattern p2 = Pattern.compile(SmartPatterns.DEFAULT_DATE_STRING_TYPE2);
        Matcher m2 = p2.matcher(this.mWorkStr);
        this.mWorkStr = p2.matcher(this.mWorkStr).replaceAll(DELIMITER);
        while (m2.find()) {
            matchString = removeUnnecessary(m2.group(0));
            this.mParsingData.setInfo(matchString, 1);
            Log.secD(TAG, "add date(pattern type2): " + matchString);
        }
        StringBuilder sb = new StringBuilder(SmartPatterns.getCountryDateString(this.mContext));
        if (sb.length() > 0 && sb.charAt(0) == '|') {
            sb.deleteCharAt(0);
            Pattern p3 = Pattern.compile(sb.toString());
            Matcher m3 = p3.matcher(this.mWorkStr);
            this.mWorkStr = p3.matcher(this.mWorkStr).replaceAll(DELIMITER);
            while (m3.find()) {
                matchString = removeUnnecessary(m3.group(0));
                this.mParsingData.setInfo(matchString, 1);
                Log.secD(TAG, "add date(pattern type3, country): " + matchString);
            }
        }
    }

    private void parsingTimeInfo() {
        Pattern p = Pattern.compile(SmartPatterns.DEFAULT_TIME_STRING + SmartPatterns.getCountryTimeString(this.mContext));
        Matcher m = p.matcher(this.mWorkStr);
        this.mWorkStr = p.matcher(this.mWorkStr).replaceAll(DELIMITER);
        while (m.find()) {
            String matchString = removeUnnecessary(m.group(0));
            this.mParsingData.setInfo(matchString, 2);
            Log.secD(TAG, "add time : " + matchString);
        }
    }

    private void parsingPhoneNumInfo() {
        Pattern p;
        if (this.mDegree >= 0) {
            p = SmartPatterns.PHONE_NUMBER;
        } else {
            p = SmartPatterns.PHONE_NUMBER_WEAK;
        }
        Matcher m = p.matcher(this.mWorkStr);
        this.mWorkStr = p.matcher(this.mWorkStr).replaceAll(DELIMITER);
        Pattern hyphen = SmartPatterns.HYPHEN;
        while (m.find()) {
            String matchString = "";
            if (this.mDegree >= -1) {
                matchString = removeUnnecessary(m.group(0), false);
            } else {
                matchString = removeUnnecessary(m.group(0));
            }
            matchString = hyphen.matcher(matchString).replaceAll(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            if (matchString.length() >= 7) {
                this.mParsingData.setInfo(matchString, 3);
                Log.secD(TAG, "add tel number : " + matchString);
            }
        }
        refactoringPhoneNumber();
    }

    private void parsingEmailInfo() {
        Pattern p;
        if (this.mDegree >= 0) {
            p = Patterns.EMAIL_ADDRESS;
        } else {
            p = SmartPatterns.EMAIL_ADDRESS_WEAK;
        }
        Matcher m = p.matcher(this.mWorkStr);
        if (this.mDegree >= -1) {
            this.mWorkStr = p.matcher(this.mWorkStr).replaceAll(DELIMITER);
        }
        Pattern hyphen = SmartPatterns.HYPHEN;
        while (m.find()) {
            String matchString = "";
            if (this.mDegree >= -1) {
                matchString = removeUnnecessary(m.group(0), false);
            } else {
                matchString = removeUnnecessary(m.group(0));
            }
            matchString = hyphen.matcher(matchString).replaceAll(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            this.mParsingData.setInfo(matchString, 4);
            Log.secD(TAG, "add email address : " + matchString);
        }
    }

    private void parsingURLInfo() {
        Pattern p = Patterns.WEB_URL_EX;
        Matcher m = p.matcher(this.mWorkStr);
        this.mWorkStr = p.matcher(this.mWorkStr).replaceAll(DELIMITER);
        while (m.find()) {
            String matchString = removeUnnecessary(m.group(0));
            this.mParsingData.setInfo(matchString, 5);
            Log.secD(TAG, "add URL : " + matchString);
        }
    }

    private void arrangeRemainData() {
        this.mWorkStr = Pattern.compile("(＃|[[:space:]])+").matcher(this.mWorkStr).replaceAll(" ");
    }

    public SmartParsingData getTotalData() {
        return this.mParsingData;
    }

    private String removeUnnecessary(String str) {
        return removeUnnecessary(str, true);
    }

    private String removeUnnecessary(String str, boolean onlyStartEndCheck) {
        StringBuilder builder = new StringBuilder(str);
        String result = "";
        if (str.startsWith("\n") || str.startsWith(" ")) {
            builder.deleteCharAt(0);
        }
        if (str.endsWith("\n") || str.endsWith(" ")) {
            builder.deleteCharAt(builder.length() - 1);
        }
        result = builder.toString();
        if (onlyStartEndCheck) {
            return result;
        }
        return Pattern.compile("[:space:]").matcher(result).replaceAll("");
    }

    private void refactoringPhoneNumber() {
        if (this.mParsingData.getCount(3) == 1) {
            String str = (String) this.mParsingData.getInfo(3).get(0);
            int spaceCount = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == ' ') {
                    spaceCount++;
                }
            }
            if (spaceCount > 0 && (str.length() / spaceCount) + 1 > 8) {
                Matcher m = SmartPatterns.REFACTORING_PHONE_NUMBER.matcher(str);
                this.mParsingData.deleteInfo(0, 3);
                while (m.find()) {
                    this.mParsingData.setInfo(m.group(0), 3);
                    Log.secD(TAG, "add refactoring phone number : " + m.group(0));
                }
            }
        }
    }

    private String convertDateToMillis(String dateStr, int patternType) {
        Time t = new Time(Time.TIMEZONE_UTC);
        String[] separated;
        if (patternType == 1) {
            try {
                separated = dateStr.split(SmartPatterns.SPILT_PATTERN_DATE_TYPE1);
                if (separated.length == 3) {
                    t.year = Integer.parseInt(separated[0]);
                    t.month = Integer.parseInt(separated[1]) - 1;
                    t.monthDay = Integer.parseInt(separated[2]);
                } else if (separated.length == 2) {
                    t.year = this.yearOfToday;
                    t.month = Integer.parseInt(separated[0]) - 1;
                    t.monthDay = Integer.parseInt(separated[1]);
                } else {
                    Log.secD(TAG, "fail convertDateToMillis() by invalid length. (type:1)");
                    return "";
                }
            } catch (Exception e) {
                Log.secD(TAG, "fail convertDateToMillis() by exception : " + e.getMessage());
                return "";
            }
        } else if (patternType == 2) {
            separated = dateStr.split(SmartPatterns.SPILT_PATTERN_DATE_TYPE2);
            if (separated.length == 3) {
                t.year = Integer.parseInt(separated[2]);
                t.month = ((Integer) SmartPatterns.globalDateMap.get(separated[0])).intValue() - 1;
                t.monthDay = Integer.parseInt(convertDayToInteger(separated[1]));
            } else if (separated.length == 2) {
                t.year = this.yearOfToday;
                t.month = ((Integer) SmartPatterns.globalDateMap.get(separated[0])).intValue() - 1;
                t.monthDay = Integer.parseInt(convertDayToInteger(separated[1]));
            } else {
                Log.secD(TAG, "fail convertDateToMillis() by invalid length. (type:2)");
                return "";
            }
        } else {
            Log.secD(TAG, "fail convertDateToMillis() by invalid patternType : ");
            return "";
        }
        t.hour = 0;
        t.minute = 0;
        t.second = 0;
        Log.secD(TAG, "convertDateToMillis() completed successfully");
        Log.secD(TAG, "year:" + t.year + ", month:" + t.month + ", day:" + t.monthDay + ", hour:" + t.hour + ", minute:" + t.minute + ", second:" + t.second);
        return Long.toString(t.toMillis(true));
    }

    public String convertDayToInteger(String dayStr) {
        if (dayStr.length() < 3) {
            return dayStr;
        }
        StringBuilder builder = new StringBuilder(dayStr);
        if (dayStr.endsWith("st") || dayStr.endsWith("nd") || dayStr.endsWith("rd") || dayStr.endsWith("th")) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private String convertTimeToMillis(String timeStr) {
        Time t = new Time(Time.TIMEZONE_UTC);
        try {
            Pattern prefixPattern = Pattern.compile(SmartPatterns.PREFIX_FOR_TIME_MILLIS);
            Matcher prefixMatcher = prefixPattern.matcher(timeStr);
            timeStr = prefixPattern.matcher(timeStr).replaceAll("");
            String[] separated = new String[2];
            String amOfEachCountry = "오전";
            String pmOfEachCountry = "오후";
            Matcher m = Pattern.compile("[0-9]+").matcher(timeStr);
            int i = 0;
            while (m.find()) {
                separated[i] = m.group(0);
                i++;
            }
            t.year = this.yearOfToday;
            t.month = this.monthOfToday;
            t.monthDay = this.dayOfToday;
            t.hour = Integer.parseInt(separated[0]);
            if (!timeStr.contains("pm")) {
                if (!(timeStr.contains("PM") || timeStr.contains(pmOfEachCountry))) {
                    if (!timeStr.contains("am")) {
                        if (!(timeStr.contains("AM") || timeStr.contains(amOfEachCountry))) {
                            t.hour = Integer.parseInt(separated[0]);
                            t.minute = Integer.parseInt(separated[1]);
                            t.second = 0;
                            Log.secD(TAG, "convertTimeToMillis() completed successfully");
                            Log.secD(TAG, "year:" + t.year + ", month:" + t.month + ", day:" + t.monthDay + ", hour:" + t.hour + ", minute:" + t.minute + ", second:" + t.second);
                            return Long.toString(t.toMillis(true));
                        }
                    }
                    if (t.hour == 12) {
                        t.hour = 0;
                    }
                    t.minute = Integer.parseInt(separated[1]);
                    t.second = 0;
                    Log.secD(TAG, "convertTimeToMillis() completed successfully");
                    Log.secD(TAG, "year:" + t.year + ", month:" + t.month + ", day:" + t.monthDay + ", hour:" + t.hour + ", minute:" + t.minute + ", second:" + t.second);
                    return Long.toString(t.toMillis(true));
                }
            }
            if (t.hour != 12) {
                t.hour += 12;
            }
            t.minute = Integer.parseInt(separated[1]);
            t.second = 0;
            Log.secD(TAG, "convertTimeToMillis() completed successfully");
            Log.secD(TAG, "year:" + t.year + ", month:" + t.month + ", day:" + t.monthDay + ", hour:" + t.hour + ", minute:" + t.minute + ", second:" + t.second);
            return Long.toString(t.toMillis(true));
        } catch (Exception e) {
            Log.secD(TAG, "fail convertTimeToMillis() by exception : " + e.getMessage());
            return "";
        }
    }

    public void clear() {
        this.mParsingData.clear();
        this.mParsingData = null;
    }
}
