package android.hardware;

import android.util.Log;

public class SecExternalDisplayJNIInterface {
    private static final String TAG = "SecExternalDisplay_Java";

    private native boolean _SecExternalDisplayBlankDisplay(int i);

    private native boolean _SecExternalDisplayCreateSurface(String str, int i, int i2);

    private native boolean _SecExternalDisplayDestroySurface(String str, int i, int i2);

    private native int _SecExternalDisplayGet3DMode();

    private native int _SecExternalDisplayGetResolution();

    private native boolean _SecExternalDisplayGetStatus(int i);

    private native boolean _SecExternalDisplayLEDDestroy();

    private native boolean _SecExternalDisplayLEDInit();

    private native boolean _SecExternalDisplayLEDOff();

    private native boolean _SecExternalDisplayLEDRefresh();

    private native boolean _SecExternalDisplayLEDSetLoop(int i, int i2, int i3, int i4, int i5);

    private native boolean _SecExternalDisplayLEDSetScroll(int i, int i2, int i3, int i4, int i5);

    private native boolean _SecExternalDisplayLEDSetframe(byte[] bArr, int i, int i2);

    private native int _SecExternalDisplayRegisterEVF(boolean z);

    private native int _SecExternalDisplaySet3DMode(int i);

    private native boolean _SecExternalDisplaySetExternalUITransform(int i);

    private native boolean _SecExternalDisplaySetForceMirrorMode(boolean z);

    private native boolean _SecExternalDisplaySetGpuLock(String str, int i);

    private native boolean _SecExternalDisplaySetOutputMode(int i);

    private native boolean _SecExternalDisplaySetPause(boolean z);

    private native boolean _SecExternalDisplaySetResolution(int i);

    private native boolean _SecExternalDisplaySetStatus(int i, boolean z);

    private native boolean _SecExternalDisplayStartStopVFBThread(int i, int i2, boolean z);

    private native boolean _SecExternalDisplayType(boolean z);

    private final native void _native_setup();

    private final native void _release();

    static {
        System.loadLibrary("SecExternalDisplay_jni");
    }

    public SecExternalDisplayJNIInterface() {
        Log.i(TAG, "SecExternalDisplayJNIInterface +");
        _native_setup();
    }

    public boolean SecExternalDisplayLEDInit() {
        return _SecExternalDisplayLEDInit();
    }

    public boolean SecExternalDisplayLEDDestroy() {
        return _SecExternalDisplayLEDDestroy();
    }

    public boolean SecExternalDisplayLEDRefresh() {
        return _SecExternalDisplayLEDRefresh();
    }

    public boolean SecExternalDisplayLEDOff() {
        return _SecExternalDisplayLEDOff();
    }

    public boolean SecExternalDisplayLEDSetLoop(int enable, int time, int count, int start, int end) {
        return _SecExternalDisplayLEDSetLoop(enable, time, count, start, end);
    }

    public boolean SecExternalDisplayLEDSetScroll(int enable, int time, int count, int start, int end) {
        return _SecExternalDisplayLEDSetScroll(enable, time, count, start, end);
    }

    public boolean SecExternalDisplayLEDSetframe(byte[] data, int numOfFrames, int repeatCount) {
        return _SecExternalDisplayLEDSetframe(data, numOfFrames, repeatCount);
    }

    public boolean SecExternalDisplayGetStatus(int param) {
        return _SecExternalDisplayGetStatus(param);
    }

    public boolean SecExternalDisplaySetStatus(int param, boolean status) {
        return _SecExternalDisplaySetStatus(param, status);
    }

    public boolean SecExternalDisplaySetResolution(int nResolution) {
        return _SecExternalDisplaySetResolution(nResolution);
    }

    public int SecExternalDisplayGetResolution() {
        return _SecExternalDisplayGetResolution();
    }

    public boolean SecExternalDisplaySetOutputMode(int nOutputMode) {
        return _SecExternalDisplaySetOutputMode(nOutputMode);
    }

    public int SecExternalDisplayGet3DMode() {
        return _SecExternalDisplayGet3DMode();
    }

    public int SecExternalDisplaySet3DMode(int nS3DMode) {
        return _SecExternalDisplaySet3DMode(nS3DMode);
    }

    public int SecExternalDisplayRegisterEVF(boolean bStatus) {
        return _SecExternalDisplayRegisterEVF(bStatus);
    }

    public boolean SecExternalDisplaySetPause(boolean bStatus) {
        return _SecExternalDisplaySetPause(bStatus);
    }

    public boolean SecExternalDisplaySetForceMirrorMode(boolean bEnable) {
        return _SecExternalDisplaySetForceMirrorMode(bEnable);
    }

    public boolean SecExternalDisplayType(boolean bIsTablet) {
        return _SecExternalDisplayType(bIsTablet);
    }

    public boolean SecExternalDisplaySetExternalUITransform(int transform) {
        return _SecExternalDisplaySetExternalUITransform(transform);
    }

    public boolean SecExternalDisplaySetGpuLock(String filename, int value) {
        return _SecExternalDisplaySetGpuLock(filename, value);
    }

    public boolean SecExternalDisplayCreateSurface(String surface, int width, int height) {
        return _SecExternalDisplayCreateSurface(surface, width, height);
    }

    public boolean SecExternalDisplayDestroySurface(String surface, int width, int height) {
        return _SecExternalDisplayDestroySurface(surface, width, height);
    }

    public boolean SecExternalDisplayBlankDisplay(int mode) {
        return _SecExternalDisplayBlankDisplay(mode);
    }

    public boolean SecExternalDisplayStartStopVFBThread(int width, int height, boolean startStopflag) {
        return _SecExternalDisplayStartStopVFBThread(width, height, startStopflag);
    }
}
