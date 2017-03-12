package com.android.internal.logging;

import android.content.Context;
import android.os.Build;

public class MetricsLogger implements MetricsConstants {
    public static final int ACCESSIBILITY_DIRECT_ACCESS = 306;
    public static final int ACCESSIBILITY_HEARING = 300;
    public static final int ACCESSIBILITY_MOBILITY = 301;
    public static final int ACCESSIBILITY_NOTIFICATION_REMINDER = 307;
    public static final int ACCESSIBILITY_SHARED_ACCESSIBILITY = 308;
    public static final int ACCESSIBILITY_SOUND_DETECTOR = 311;
    public static final int ACCESSIBILITY_TAB_AND_HOLD = 309;
    public static final int ACCESSIBILITY_TOGGLE_ACCESS_CONTROL = 303;
    public static final int ACCESSIBILITY_TOGGLE_AIR_WAKEUP = 304;
    public static final int ACCESSIBILITY_TOGGLE_MAGNIFIER = 305;
    public static final int ACCESSIBILITY_UNIVERSAL_LOCK = 310;
    public static final int ACCESSIBILITY_VISION = 302;
    public static final int ACTION_ASSIST_LONG_PRESS = 239;
    public static final int ACTION_DOUBLE_TAP_POWER_CAMERA_GESTURE = 255;
    public static final int ACTION_FINGERPRINT_AUTH = 252;
    public static final int ACTION_FINGERPRINT_DELETE = 253;
    public static final int ACTION_FINGERPRINT_ENROLL = 251;
    public static final int ACTION_FINGERPRINT_RENAME = 254;
    public static final int ACTION_WIGGLE_CAMERA_GESTURE = 256;
    public static final int FINGERPRINT_ENROLLING = 240;
    public static final int FINGERPRINT_ENROLLING_SETUP = 246;
    public static final int FINGERPRINT_ENROLL_FINISH = 242;
    public static final int FINGERPRINT_ENROLL_FINISH_SETUP = 248;
    public static final int FINGERPRINT_ENROLL_INTRO = 243;
    public static final int FINGERPRINT_ENROLL_INTRO_SETUP = 249;
    public static final int FINGERPRINT_ENROLL_ONBOARD = 244;
    public static final int FINGERPRINT_ENROLL_ONBOARD_SETUP = 250;
    public static final int FINGERPRINT_ENROLL_SIDECAR = 245;
    public static final int FINGERPRINT_FIND_SENSOR = 241;
    public static final int FINGERPRINT_FIND_SENSOR_SETUP = 247;
    public static final int QS_APN = 406;
    public static final int QS_DEFAULTDATA = 407;
    public static final int QS_EBOOK = 408;
    public static final int QS_MOBILEDATA = 409;
    public static final int QS_NFC = 410;
    public static final int QS_POWERSAVING = 412;
    public static final int QS_PRIVATEMODE = 411;
    public static final int QS_QUICKCONNECT = 400;
    public static final int QS_SCREENMIRRORING = 405;
    public static final int QS_SFINDER = 401;
    public static final int QS_SILENTMODE = 402;
    public static final int QS_SMARTSTAY = 403;
    public static final int QS_SYNC = 404;
    public static final int QS_TOUCHSENSITIVITY = 413;
    public static final int QS_ULTRAPOWERSAVING = 414;
    public static final int QS_VOLTE = 415;
    public static final int QS_VOWIFI = 417;
    public static final int QS_WIFICALLING = 416;
    public static final int SETTINGS_CPA = 500;
    public static final int SETTINGS_DATA_SLOT = 501;
    public static final int SETTINGS_GLOBAL_ROAMING = 502;
    public static final int SETTINGS_LGT_CUSTOMER = 503;
    public static final int SETTINGS_LGT_ENV = 504;
    public static final int SETTINGS_LTE_MODE = 505;
    public static final int SETTINGS_LTE_ROAMING = 506;
    public static final int SETTINGS_ROAMING = 507;

    public static void visible(Context context, int category) throws IllegalArgumentException {
        if (Build.IS_DEBUGGABLE && category == 0) {
            throw new IllegalArgumentException("Must define metric category");
        }
        EventLogTags.writeSysuiViewVisibility(category, 100);
    }

    public static void hidden(Context context, int category) throws IllegalArgumentException {
        if (Build.IS_DEBUGGABLE && category == 0) {
            throw new IllegalArgumentException("Must define metric category");
        }
        EventLogTags.writeSysuiViewVisibility(category, 0);
    }

    public static void visibility(Context context, int category, boolean visibile) throws IllegalArgumentException {
        if (visibile) {
            visible(context, category);
        } else {
            hidden(context, category);
        }
    }

    public static void visibility(Context context, int category, int vis) throws IllegalArgumentException {
        visibility(context, category, vis == 0);
    }

    public static void action(Context context, int category) {
        action(context, category, "");
    }

    public static void action(Context context, int category, int value) {
        action(context, category, Integer.toString(value));
    }

    public static void action(Context context, int category, boolean value) {
        action(context, category, Boolean.toString(value));
    }

    public static void action(Context context, int category, String pkg) {
        if (Build.IS_DEBUGGABLE && category == 0) {
            throw new IllegalArgumentException("Must define metric category");
        }
        EventLogTags.writeSysuiAction(category, pkg);
    }

    public static void count(Context context, String name, int value) {
        EventLogTags.writeSysuiCount(name, value);
    }

    public static void histogram(Context context, String name, int bucket) {
        EventLogTags.writeSysuiHistogram(name, bucket);
    }
}
