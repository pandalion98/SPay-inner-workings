package android.media.audiofx;

import android.util.Log;
import java.util.UUID;

public class MySound extends AudioEffect {
    public static final UUID EFFECT_TYPE_MYSOUND = UUID.fromString("d2bc05e0-50b0-11e2-bcfd-0800200c9a66");
    private static final String TAG = "MySound";

    public MySound(int priority, int audioSession) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        super(EFFECT_TYPE_MYSOUND, EFFECT_TYPE_NULL, priority, audioSession);
        Log.w(TAG, "init MySound module");
        if (audioSession == 0) {
            Log.w(TAG, "WARNING: attaching an SoundAlive to global output mix is deprecated!");
        }
    }

    public void setGain(int[] leftGain, int[] rightGain) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException, RuntimeException {
        int i;
        byte[] param = new byte[12];
        byte[] value = new byte[1];
        for (i = 0; i < 6; i++) {
            param[i] = (byte) leftGain[i];
        }
        for (i = 0; i < 6; i++) {
            param[i + 6] = (byte) rightGain[i];
        }
        value[0] = (byte) 0;
        checkStatus(setParameter(param, value));
    }
}
