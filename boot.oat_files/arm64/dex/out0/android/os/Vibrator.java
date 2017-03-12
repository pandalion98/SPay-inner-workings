package android.os;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.wifi.hs20.WifiHs20Manager;
import android.util.Log;

public abstract class Vibrator {
    private static final String TAG = "Vibrator";
    protected final String mPackageName;

    public enum MagnitudeTypes {
        TouchMagnitude,
        NotificationMagnitude,
        CallMagnitude,
        MaxMagnitude,
        MinMagnitude
    }

    public abstract void cancel();

    public abstract boolean hasVibrator();

    public abstract void vibrate(int i, String str, long j, AudioAttributes audioAttributes);

    public abstract void vibrate(int i, String str, long[] jArr, int i2, AudioAttributes audioAttributes);

    public Vibrator() {
        this.mPackageName = ActivityThread.currentPackageName();
    }

    protected Vibrator(Context context) {
        this.mPackageName = context.getOpPackageName();
    }

    public boolean isEnableIntensity() {
        return false;
    }

    public void vibrate(long milliseconds) {
        Log.v(TAG, "Called vibrate(long) API!");
        vibrate(milliseconds, (AudioAttributes) null);
    }

    public void vibrate(long milliseconds, AudioAttributes attributes) {
        Log.v(TAG, "vibrate(long, AudioAttributes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", ms: " + milliseconds + ", AudioAttr: " + attributes);
        vibrate(Process.myUid(), this.mPackageName, milliseconds, attributes);
    }

    public void vibrate(long[] pattern, int repeat) {
        Log.v(TAG, "Called vibrate(long[], int) API!");
        vibrate(pattern, repeat, (AudioAttributes) null);
    }

    public void vibrate(long[] pattern, int repeat, AudioAttributes attributes) {
        Log.v(TAG, "vibrate(long[], int, AudioAttributes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", repeat: " + repeat + ", AudioAttr: " + attributes);
        vibrate(Process.myUid(), this.mPackageName, pattern, repeat, attributes);
    }

    public void vibrate(long milliseconds, MagnitudeTypes magnitudeType) {
    }

    public void vibrate(long[] pattern, int repeat, int magnitude) {
    }

    public void vibrate(long[] pattern, int repeat, MagnitudeTypes magnitudeType) {
    }

    public void vibrate(int type, AudioAttributes attributes, int magnitude) {
    }

    public void vibrate(int type, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
    }

    public void vibrate(int type, int repeat, AudioAttributes attributes, int magnitude) {
    }

    public void vibrate(int type, int repeat, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
    }

    public void vibrate(long milliseconds, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
    }

    public void vibrate(long[] pattern, int repeat, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
    }

    public int getMaxMagnitude() {
        return WifiHs20Manager.HS20_OSU_STATE_UNKNOWN;
    }

    public void setMagnitude(int magnitude) {
    }

    public void resetMagnitude() {
    }

    public void vibrateImmVibe(int type, MagnitudeTypes magnitudeType) {
    }

    public void vibrateImmVibe(int type, int magnitude) {
    }

    public void vibrateImmVibe(byte[] pattern, MagnitudeTypes magnitudeType) {
    }

    public void vibrateImmVibe(byte[] pattern, int magnitude) {
    }
}
