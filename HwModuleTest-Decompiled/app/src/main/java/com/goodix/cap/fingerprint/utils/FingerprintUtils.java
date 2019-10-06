package com.goodix.cap.fingerprint.utils;

import android.content.Context;
import android.os.Vibrator;

public class FingerprintUtils {
    private static final long[] FP_ERROR_VIBRATE_PATTERN = {0, 30, 100, 30};
    private static final long[] FP_SUCCESS_VIBRATE_PATTERN = {0, 30};

    public static void vibrateFingerprintError(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(FP_ERROR_VIBRATE_PATTERN, -1);
        }
    }

    public static void vibrateFingerprintSuccess(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(FP_SUCCESS_VIBRATE_PATTERN, -1);
        }
    }
}
