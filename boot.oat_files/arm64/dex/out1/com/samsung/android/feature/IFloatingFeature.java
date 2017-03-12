package com.samsung.android.feature;

public interface IFloatingFeature {
    boolean getEnableStatus(String str);

    boolean getEnableStatus(String str, boolean z);

    int getInteger(String str);

    int getInteger(String str, int i);

    String getString(String str);

    String getString(String str, String str2);
}
