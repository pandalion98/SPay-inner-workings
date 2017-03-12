package android.view;

public class HapticFeedbackConstants {
    public static final int CALENDAR_DATE = 5;
    public static final int CLOCK_TICK = 4;
    public static final int CONTEXT_CLICK = 6;
    public static final int FLAG_IGNORE_GLOBAL_SETTING = 2;
    public static final int FLAG_IGNORE_VIEW_SETTING = 1;
    public static final int KEYBOARD_TAP = 3;
    public static final int LONG_PRESS = 0;
    private static final int NEW_VIBE_MAX_INDEX = 50037;
    private static final int NEW_VIBE_MIN_INDEX = 50009;
    public static final int SAFE_MODE_DISABLED = 10000;
    public static final int SAFE_MODE_ENABLED = 10001;
    private static final int SAMSUNG_VIBE_START = 50000;
    public static final int VIBE_ALERT_ON_CALL = 50015;
    public static final int VIBE_CAUTION = 50011;
    public static final int VIBE_COMMON_A = 50025;
    public static final int VIBE_COMMON_B = 50026;
    public static final int VIBE_COMMON_C = 50027;
    public static final int VIBE_COMMON_D = 50028;
    public static final int VIBE_COMMON_E = 50029;
    public static final int VIBE_COMMON_F = 50030;
    public static final int VIBE_COMMON_G = 50031;
    public static final int VIBE_COMMON_H = 50032;
    public static final int VIBE_COMMON_I = 50033;
    public static final int VIBE_COMMON_J = 50034;
    public static final int VIBE_COMMON_K = 50035;
    public static final int VIBE_COMMON_L = 50036;
    public static final int VIBE_COMMON_M = 50037;
    public static final int VIBE_HW_TOUCH = 50014;
    public static final int VIBE_MAX_INDEX = 22;
    public static final int VIBE_MIN_INDEX = 9;
    public static final int VIBE_NOTIFICATION = 50017;
    public static final int VIBE_NOTIFICATION_ALARM = 50018;
    public static final int VIBE_NOTIFICATION_EMAIL = 50016;
    public static final int VIBE_NOTIFICATION_IM = 50013;
    public static final int VIBE_NOTIFICATION_IM_NEW = 50024;
    public static final int VIBE_NOTIFICATION_MESSAGE = 50017;
    public static final int VIBE_NOTIFICATION_RINGTONE = 50013;
    public static final int VIBE_OPERATION = 50010;
    public static final int VIBE_SILENT = 50012;
    public static final int VIBE_TEXT_SELECTION = 50022;
    public static final int VIBE_TIMEPICKER = 50023;
    public static final int VIBE_TOUCH = 50009;
    public static final int VIBE_VOICE_ERROR = 50019;
    public static final int VIBE_VOICE_READY = 50020;
    public static final int VIBE_VOICE_SUCCESS = 50021;
    public static final int VIRTUAL_KEY = 1;

    private HapticFeedbackConstants() {
    }

    public static int getMinVibeIndex() {
        return 50009;
    }

    public static int getMaxVibeIndex() {
        return 50037;
    }

    public static boolean isValidatedVibeIndex(int index) {
        if (index < 50009 || index > 50037) {
            return false;
        }
        return true;
    }
}
