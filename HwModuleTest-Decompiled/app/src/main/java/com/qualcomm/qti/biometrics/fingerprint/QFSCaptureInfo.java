package com.qualcomm.qti.biometrics.fingerprint;

import android.graphics.Bitmap;

public class QFSCaptureInfo {
    private Bitmap bitmap;
    private int height;
    private int quality;
    private Bitmap rawBitmap;
    private int width;

    public QFSCaptureInfo(Bitmap bitmap2, int height2, int quality2, Bitmap rawBitmap2, int width2) {
        this.bitmap = bitmap2;
        this.height = height2;
        this.quality = quality2;
        this.rawBitmap = rawBitmap2;
        this.width = width2;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public int getQuality() {
        return this.quality;
    }

    public void setQuality(int quality2) {
        this.quality = quality2;
    }

    public Bitmap getRawBitmap() {
        return this.rawBitmap;
    }

    public void setRawBitmap(Bitmap rawBitmap2) {
        this.rawBitmap = rawBitmap2;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }
}
