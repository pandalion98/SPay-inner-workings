package com.sensoryinc.fluentsoftsdk;

import android.util.Log;

public class SensoryBargeInEngine {
    private static final String TAG = SensoryBargeInEngine.class.getSimpleName();

    public native void phrasespotClose(long j);

    public native long phrasespotInit(String str, String str2);

    public native String phrasespotPipe(long j, short[] sArr, long j2, long j3, float[] fArr);

    public void asyncPrint(String s) {
    }

    public static int init() {
        try {
            Log.i(TAG, "Trying to load libSensoryBargeInEngine.so");
            System.loadLibrary("SensoryBargeInEngine");
            Log.i(TAG, "Loading libSensoryBargeInEngine.so");
            return 0;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "WARNING: Could not load libSensoryBargeInEngine.so");
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, "WARNING: Could not load libSensoryBargeInEngine.so");
            return -1;
        }
    }
}
