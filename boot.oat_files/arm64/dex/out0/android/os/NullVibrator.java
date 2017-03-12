package android.os;

import android.media.AudioAttributes;

public class NullVibrator extends Vibrator {
    private static final NullVibrator sInstance = new NullVibrator();

    private NullVibrator() {
    }

    public static NullVibrator getInstance() {
        return sInstance;
    }

    public boolean hasVibrator() {
        return false;
    }

    public void vibrate(int uid, String opPkg, long milliseconds, AudioAttributes attributes) {
    }

    public void vibrate(int uid, String opPkg, long[] pattern, int repeat, AudioAttributes attributes) {
        if (repeat >= pattern.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void cancel() {
    }
}
