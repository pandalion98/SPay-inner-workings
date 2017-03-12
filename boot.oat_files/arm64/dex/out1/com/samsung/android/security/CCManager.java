package com.samsung.android.security;

public class CCManager {
    public static native boolean isMdfDisabled();

    public static native boolean isMdfEnabled();

    public static native boolean isMdfEnforced();

    public static native boolean isMdfReady();

    public static native boolean isMdfSupported();

    public static native int updateMdfStatus();

    public static native String updateMdfVersion();
}
