package com.goodix.cap.fingerprint.sec;

import android.graphics.Bitmap;

public class GoodixFpEnrollStatus {
    public int badTrial;
    public Bitmap coverageBitmap;
    public int[] currLocation;
    public int[] nextLocation;
    public int[] prevLocations;
    public int progress;
    public int successTrial;
    public int totalTrial;
}
