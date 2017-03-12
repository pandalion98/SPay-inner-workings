package android.text.method;

import android.text.format.DateFormat;

public class TimeKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.AM_PM, DateFormat.MINUTE, 'p', ':'};
    private static TimeKeyListener sInstance;

    public int getInputType() {
        return 36;
    }

    protected char[] getAcceptedChars() {
        return CHARACTERS;
    }

    public static TimeKeyListener getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new TimeKeyListener();
        return sInstance;
    }
}
