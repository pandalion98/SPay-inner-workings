package android.media.audiofx;

import android.util.Log;

public class AutomaticGainControl extends AudioEffect {
    private static final String TAG = "AutomaticGainControl";

    public static boolean isAvailable() {
        return false;
    }

    public static AutomaticGainControl create(int audioSession) {
        Log.w(TAG, "not implemented on this device " + null);
        return null;
    }

    private AutomaticGainControl(int audioSession) throws IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        super(EFFECT_TYPE_AGC, EFFECT_TYPE_NULL, 0, audioSession);
    }
}
