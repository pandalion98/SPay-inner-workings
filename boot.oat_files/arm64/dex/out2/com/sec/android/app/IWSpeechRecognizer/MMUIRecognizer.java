package com.sec.android.app.IWSpeechRecognizer;

import android.util.Log;

public class MMUIRecognizer {
    private static final String TAG = MMUIRecognizer.class.getSimpleName();

    public native int RECThread(short[] sArr);

    public native int ResetFx();

    public native int SASRClose();

    public native int SASRDoRecognition(float[] fArr, String[] strArr, String str, short[] sArr, String[] strArr2);

    public native int SASRInit(String str);

    public native int SASRLoadModel(String str);

    public native int SASRReset();

    public native int SaveChnUpdate(String str);

    public native int SetSRLanguage(int i);

    public static int init() {
        try {
            Log.i(TAG, "Trying to load libsasr-jni.so");
            System.loadLibrary("sasr-jni");
            Log.i(TAG, "Loading libsasr-jni.so done");
            return 0;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "WARNING: Could not load libsasr-jni.so");
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, "WARNING: Could not load libsasr-jni.so");
            return -1;
        }
    }
}
