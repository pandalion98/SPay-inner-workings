package com.android.internal.widget;

public interface DirectionLockTouchListener {
    public static final String DIAGONAL = "DIAGONAL";
    public static final String DOWN = "DOWN";
    public static final String FINGER_LIFT = "FINGER_LIFT";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String UP = "UP";

    void onDirectionDetected(String str, boolean z);

    void onDirectionStarted();
}
