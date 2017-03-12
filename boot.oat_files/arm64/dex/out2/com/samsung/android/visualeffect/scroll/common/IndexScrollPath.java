package com.samsung.android.visualeffect.scroll.common;

import android.graphics.Matrix;
import android.graphics.Path;

public class IndexScrollPath {
    private float lastX;
    private float lastX1;
    private float lastY;
    private float lastY1;
    private final float offsetX = -475.9f;
    private final float offsetY = -423.8f;
    private Path[] path = new Path[23];
    private final int pathTotal = 23;
    private float ratio = 1.41f;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand = new int[SVGCommand.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.V.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.v.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.H.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.h.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.M.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.m.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.Z.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.z.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.L.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.l.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.C.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.c.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.S.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[SVGCommand.s.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    public enum SVGCommand {
        M,
        m,
        Z,
        z,
        L,
        l,
        H,
        h,
        V,
        v,
        C,
        c,
        S,
        s
    }

    public IndexScrollPath(float scale) {
        this.ratio *= scale;
        setPath();
    }

    private void setPath() {
        Matrix mtrx = new Matrix();
        mtrx.postTranslate(-475.9f, -423.8f);
        mtrx.postScale(this.ratio, this.ratio);
        int count = -1 + 1;
        Path seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 558.6f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -101.3f, -4.9f, -87.9f, -4.9f, -134.7f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -42.1f, 4.9f, -23.1f, 4.9f, -134.7f);
        vectorParsing(seqPath, SVGCommand.V, 558.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 583.8f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -120.3f, -11.9f, -107.2f, -11.9f, -160.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -54.6f, 11.9f, -33.0f, 11.9f, -160.0f);
        vectorParsing(seqPath, SVGCommand.V, 583.8f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 588.8f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -117.6f, -19.4f, -104.6f, -19.4f, -165.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -62.3f, 19.4f, -40.7f, 19.4f, -165.0f);
        vectorParsing(seqPath, SVGCommand.V, 588.8f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 596.3f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -113.9f, -28.4f, -102.9f, -28.4f, -172.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -71.3f, 28.4f, -55.5f, 28.4f, -172.5f);
        vectorParsing(seqPath, SVGCommand.V, 596.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 611.3f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -134.0f, -38.3f, -111.8f, -38.3f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.5f, 38.3f, -53.3f, 38.3f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 611.3f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -134.0f, -48.9f, -111.8f, -48.9f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.5f, 48.9f, -53.3f, 48.9f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 611.3f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -125.3f, -59.4f, -111.8f, -59.4f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.5f, 59.4f, -63.2f, 59.4f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 633.9f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -143.5f, -69.9f, -125.4f, -69.9f, -210.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -85.9f, 69.9f, -63.2f, 69.9f, -210.0f);
        vectorParsing(seqPath, SVGCommand.V, 633.9f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 521.7f);
        vectorParsing(seqPath, SVGCommand.v, 147.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -174.2f, -83.4f, -159.4f, -83.4f, -245.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -87.3f, 83.4f, -63.8f, 83.4f, -244.9f);
        vectorParsing(seqPath, SVGCommand.v, 147.0f);
        vectorParsing(seqPath, SVGCommand.V, 521.7f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 521.3f);
        vectorParsing(seqPath, SVGCommand.v, 147.6f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -104.5f, -40.6f, -137.9f, -69.3f, -175.2f);
        vectorParsing(seqPath, SVGCommand.c, -9.0f, -10.2f, -18.1f, -22.6f, -22.5f, -36.4f);
        vectorParsing(seqPath, SVGCommand.c, -3.2f, -9.7f, -4.1f, -20.6f, -4.1f, -33.4f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -15.1f, 1.5f, -27.4f, 5.7f, -38.1f);
        vectorParsing(seqPath, SVGCommand.c, 4.9f, -13.2f, 12.2f, -24.9f, 21.3f, -34.4f);
        vectorParsing(seqPath, SVGCommand.c, 2.6f, -2.7f, 5.2f, -5.4f, 7.7f, -8.3f);
        vectorParsing(seqPath, SVGCommand.c, 27.6f, -31.4f, 61.3f, -66.3f, 61.3f, -164.2f);
        vectorParsing(seqPath, SVGCommand.v, 147.5f);
        vectorParsing(seqPath, SVGCommand.V, 521.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 521.3f);
        vectorParsing(seqPath, SVGCommand.v, 147.6f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -104.5f, -48.7f, -137.9f, -81.6f, -175.2f);
        vectorParsing(seqPath, SVGCommand.c, -10.3f, -10.2f, -18.4f, -22.6f, -23.5f, -36.4f);
        vectorParsing(seqPath, SVGCommand.c, -3.7f, -9.7f, -5.9f, -20.6f, -5.9f, -33.4f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -15.1f, 2.9f, -27.4f, 7.7f, -38.1f);
        vectorParsing(seqPath, SVGCommand.c, 5.6f, -13.2f, 13.9f, -24.9f, 24.4f, -34.4f);
        vectorParsing(seqPath, SVGCommand.c, 3.0f, -2.7f, 5.9f, -5.4f, 8.8f, -8.3f);
        vectorParsing(seqPath, SVGCommand.c, 31.6f, -31.4f, 70.1f, -66.3f, 70.1f, -164.2f);
        vectorParsing(seqPath, SVGCommand.v, 147.5f);
        vectorParsing(seqPath, SVGCommand.V, 521.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 516.6f);
        vectorParsing(seqPath, SVGCommand.v, 152.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.6f, -5.7f, -64.4f, -14.5f, -86.8f);
        vectorParsing(seqPath, SVGCommand.c, -19.2f, -43.9f, -54.6f, -67.6f, -62.6f, -72.6f);
        vectorParsing(seqPath, SVGCommand.c, -28.7f, -16.8f, -48.2f, -48.9f, -48.2f, -85.7f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.1f, 18.7f, -67.6f, 46.5f, -84.7f);
        vectorParsing(seqPath, SVGCommand.l, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, -0.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.8f, -0.5f, 1.5f, -0.9f, 2.3f, -1.3f);
        vectorParsing(seqPath, SVGCommand.c, 9.4f, -5.8f, 44.4f, -29.5f, 63.1f, -72.2f);
        vectorParsing(seqPath, SVGCommand.c, 8.1f, -22.1f, 13.2f, -50.0f, 13.2f, -86.7f);
        vectorParsing(seqPath, SVGCommand.V, 331.0f);
        vectorParsing(seqPath, SVGCommand.V, 516.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 516.6f);
        vectorParsing(seqPath, SVGCommand.v, 152.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.6f, -6.7f, -64.4f, -17.0f, -86.8f);
        vectorParsing(seqPath, SVGCommand.c, -22.6f, -43.9f, -64.1f, -67.6f, -73.5f, -72.6f);
        vectorParsing(seqPath, SVGCommand.c, -33.7f, -16.8f, -56.5f, -48.9f, -56.5f, -85.7f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.1f, 21.9f, -67.6f, 54.6f, -84.7f);
        vectorParsing(seqPath, SVGCommand.l, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f, -0.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.9f, -0.5f, 1.8f, -0.9f, 2.7f, -1.3f);
        vectorParsing(seqPath, SVGCommand.c, 11.1f, -5.8f, 52.1f, -29.5f, 74.0f, -72.2f);
        vectorParsing(seqPath, SVGCommand.c, 9.4f, -22.1f, 15.5f, -50.0f, 15.5f, -86.7f);
        vectorParsing(seqPath, SVGCommand.V, 331.0f);
        vectorParsing(seqPath, SVGCommand.V, 516.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.9f, 490.4f);
        vectorParsing(seqPath, SVGCommand.v, 178.4f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -29.9f, -4.0f, -54.0f, -10.4f, -74.0f);
        vectorParsing(seqPath, SVGCommand.c, -17.3f, -49.4f, -81.5f, -73.8f, -93.1f, -77.9f);
        vectorParsing(seqPath, SVGCommand.c, -0.2f, -0.1f, -0.4f, -0.2f, -0.7f, -0.2f);
        vectorParsing(seqPath, SVGCommand.c, -0.7f, -0.3f, -1.2f, -0.4f, -1.2f, -0.4f);
        vectorParsing(seqPath, SVGCommand.v, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, -37.9f, -13.6f, -65.0f, -49.9f, -65.0f, -92.5f);
        vectorParsing(seqPath, SVGCommand.s, 27.1f, -78.9f, 65.1f, -92.5f);
        vectorParsing(seqPath, SVGCommand.v, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.1f, 0.0f, 0.2f, -0.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.9f, -0.3f, 1.7f, -0.6f, 2.6f, -0.9f);
        vectorParsing(seqPath, SVGCommand.c, 13.9f, -4.9f, 76.3f, -29.4f, 93.2f, -77.7f);
        vectorParsing(seqPath, SVGCommand.c, 5.9f, -19.9f, 9.5f, -43.9f, 9.5f, -73.9f);
        vectorParsing(seqPath, SVGCommand.v, 178.4f);
        vectorParsing(seqPath, SVGCommand.V, 490.4f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 376.5f, 325.6f);
        vectorParsing(seqPath, SVGCommand.c, -52.2f, 0.0f, -94.5f, 44.0f, -94.5f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 42.3f, 98.2f, 94.5f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 37.3f, 0.0f, 99.5f, 36.5f, 99.5f, 146.7f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 285.5f, 425.0f, 325.6f, 376.5f, 325.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 361.6f, 325.6f);
        vectorParsing(seqPath, SVGCommand.c, -60.0f, 0.0f, -108.6f, 44.0f, -108.6f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 48.6f, 98.2f, 108.6f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 55.1f, 0.0f, 82.9f, 33.6f, 105.4f, 77.8f);
        vectorParsing(seqPath, SVGCommand.c, 9.0f, 17.7f, 8.9f, 41.4f, 8.9f, 68.9f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 300.5f, 412.0f, 325.6f, 361.6f, 325.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 467.9f, 247.8f);
        vectorParsing(seqPath, SVGCommand.C, 437.0f, 342.0f, 362.8f, 325.6f, 323.8f, 325.6f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 36.2f, 0.0f, 89.5f, -25.0f, 144.1f, 77.8f);
        vectorParsing(seqPath, SVGCommand.c, 9.2f, 17.3f, 8.1f, 41.4f, 8.1f, 68.9f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.v, -245.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 206.4f, 472.9f, 228.9f, 467.9f, 247.8f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 470.5f, 236.7f);
        vectorParsing(seqPath, SVGCommand.C, 433.1f, 366.0f, 344.6f, 341.8f, 323.3f, 333.9f);
        vectorParsing(seqPath, SVGCommand.c, -1.5f, -0.7f, -3.1f, -1.3f, -4.6f, -1.9f);
        vectorParsing(seqPath, SVGCommand.c, -10.9f, -4.1f, -22.6f, -6.4f, -34.9f, -6.4f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 14.3f, 0.0f, 27.9f, -3.1f, 40.2f, -8.6f);
        vectorParsing(seqPath, SVGCommand.c, 21.9f, -8.1f, 106.9f, -30.1f, 146.0f, 97.5f);
        vectorParsing(seqPath, SVGCommand.c, 3.8f, 16.6f, 6.0f, 35.6f, 6.0f, 57.9f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 201.2f, 474.0f, 220.2f, 470.5f, 236.7f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 386.1f, 364.9f);
        vectorParsing(seqPath, SVGCommand.c, -6.9f, 7.6f, -15.7f, 11.9f, -22.4f, 11.9f);
        vectorParsing(seqPath, SVGCommand.c, -8.1f, 0.0f, -12.8f, -3.2f, -25.3f, -16.9f);
        vectorParsing(seqPath, SVGCommand.v, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, -18.0f, -21.0f, -44.8f, -34.3f, -74.6f, -34.3f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 30.8f, 0.0f, 58.2f, -14.1f, 76.2f, -36.3f);
        vectorParsing(seqPath, SVGCommand.c, 11.3f, -12.2f, 15.9f, -15.1f, 23.7f, -15.1f);
        vectorParsing(seqPath, SVGCommand.c, 7.2f, 0.0f, 16.0f, 4.7f, 23.0f, 13.3f);
        vectorParsing(seqPath, SVGCommand.c, 0.5f, 0.6f, 1.0f, 1.3f, 1.5f, 1.9f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.1f, 0.1f, 0.1f);
        vectorParsing(seqPath, SVGCommand.l, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 32.3f, 40.8f, 87.8f, 82.1f, 87.8f, 183.0f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 292.7f, 418.1f, 324.4f, 386.1f, 364.9f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 392.5f, 366.8f);
        vectorParsing(seqPath, SVGCommand.c, -7.0f, 10.7f, -16.0f, 28.5f, -26.8f, 28.5f);
        vectorParsing(seqPath, SVGCommand.c, -8.1f, 0.0f, -27.8f, -24.6f, -40.3f, -38.3f);
        vectorParsing(seqPath, SVGCommand.v, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, -18.0f, -21.0f, -40.8f, -31.3f, -70.6f, -31.3f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 30.8f, 0.0f, 54.2f, -11.1f, 72.2f, -33.3f);
        vectorParsing(seqPath, SVGCommand.c, 11.3f, -12.2f, 30.9f, -37.8f, 38.7f, -37.8f);
        vectorParsing(seqPath, SVGCommand.c, 10.6f, 0.0f, 10.9f, 15.6f, 27.4f, 34.8f);
        vectorParsing(seqPath, SVGCommand.c, 12.9f, 14.9f, 82.8f, 66.2f, 82.8f, 183.1f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 299.2f, 423.4f, 324.5f, 392.5f, 366.8f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 340.4f, 398.8f);
        vectorParsing(seqPath, SVGCommand.L, 340.4f, 398.8f);
        vectorParsing(seqPath, SVGCommand.c, -11.1f, -42.1f, -49.4f, -73.2f, -95.0f, -73.2f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 45.4f, 0.0f, 83.6f, -30.8f, 94.9f, -72.7f);
        vectorParsing(seqPath, SVGCommand.l, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 2.9f, -9.1f, 15.0f, -25.6f, 21.3f, -25.6f);
        vectorParsing(seqPath, SVGCommand.C, 355.4f, 423.8f, 343.4f, 408.0f, 340.4f, 398.8f);
        vectorParsing(seqPath, SVGCommand.z);
        vectorParsing(seqPath, SVGCommand.M, 387.4f, 392.3f);
        vectorParsing(seqPath, SVGCommand.L, 387.4f, 392.3f);
        vectorParsing(seqPath, SVGCommand.C, 387.4f, 392.3f, 387.4f, 392.4f, 387.4f, 392.3f);
        vectorParsing(seqPath, SVGCommand.c, -0.4f, 1.2f, -0.7f, 2.4f, -1.0f, 3.6f);
        vectorParsing(seqPath, SVGCommand.c, -2.4f, 8.2f, -11.7f, 27.9f, -18.5f, 27.9f);
        vectorParsing(seqPath, SVGCommand.c, 7.2f, 0.0f, 16.7f, 20.9f, 18.7f, 28.1f);
        vectorParsing(seqPath, SVGCommand.c, 0.2f, 0.8f, 0.4f, 1.6f, 0.6f, 2.4f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vectorParsing(seqPath, SVGCommand.v, 0.0f);
        vectorParsing(seqPath, SVGCommand.c, 17.8f, 62.6f, 88.7f, 76.6f, 88.7f, 214.5f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 323.7f, 405.6f, 330.8f, 387.4f, 392.3f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 379.3f, 423.8f);
        vectorParsing(seqPath, SVGCommand.c, 2.9f, 3.0f, 4.0f, 4.5f, 4.5f, 9.8f);
        vectorParsing(seqPath, SVGCommand.c, 7.9f, 77.9f, 92.2f, 82.4f, 92.2f, 235.3f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 159.0f, -84.0f, 158.1f, -91.9f, 234.1f);
        vectorParsing(seqPath, SVGCommand.C, 383.2f, 421.2f, 382.2f, 421.3f, 379.3f, 423.8f);
        vectorParsing(seqPath, SVGCommand.z);
        vectorParsing(seqPath, SVGCommand.M, 235.7f, 325.6f);
        vectorParsing(seqPath, SVGCommand.c, -54.3f, 0.0f, -98.2f, 44.0f, -98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.3f, 44.0f, 98.2f, 98.2f, 98.2f);
        vectorParsing(seqPath, SVGCommand.c, 50.2f, 0.0f, 91.6f, -37.6f, 97.5f, -86.2f);
        vectorParsing(seqPath, SVGCommand.c, 0.5f, -3.9f, 3.1f, -9.4f, 7.6f, -12.0f);
        vectorParsing(seqPath, SVGCommand.c, -4.6f, -3.8f, -7.2f, -9.5f, -7.8f, -14.2f);
        vectorParsing(seqPath, SVGCommand.C, 326.0f, 362.1f, 285.1f, 325.6f, 235.7f, 325.6f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 385.5f, 423.8f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 85.7f, 90.4f, 87.3f, 90.4f, 245.1f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.9f, 345.1f, 385.5f, 336.5f, 385.5f, 423.8f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
    }

    public Path getPath(int index) {
        return this.path[index];
    }

    public float getCircleCenterX() {
        return -245.209f * this.ratio;
    }

    public float getCircleCenterY() {
        return 0.019012451f * this.ratio;
    }

    public float getCircleRadius() {
        return 98.231f * this.ratio;
    }

    public int getPathTotal() {
        return 23;
    }

    private void vectorParsing(Path p, SVGCommand cmd) {
        vectorParsing(p, cmd, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float value) {
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[cmd.ordinal()]) {
            case 1:
            case 2:
                vectorParsing(p, cmd, 0.0f, value, 0.0f, 0.0f, 0.0f, 0.0f);
                return;
            case 3:
            case 4:
                vectorParsing(p, cmd, value, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                return;
            default:
                return;
        }
    }

    private void vectorParsing(Path p, SVGCommand cmd, float x1, float y1) {
        vectorParsing(p, cmd, x1, y1, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float x1, float y1, float x2, float y2) {
        vectorParsing(p, cmd, x1, y1, x2, y2, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float x1, float y1, float x2, float y2, float x3, float y3) {
        boolean wasCurve = false;
        float y;
        float x;
        float tx2;
        float ty2;
        float tx;
        float ty;
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath$SVGCommand[cmd.ordinal()]) {
            case 1:
            case 2:
                y = y1;
                if (cmd != SVGCommand.v) {
                    p.lineTo(this.lastX, y);
                    this.lastY = y;
                    break;
                }
                p.rLineTo(0.0f, y);
                this.lastY += y;
                break;
            case 3:
            case 4:
                x = x1;
                if (cmd != SVGCommand.h) {
                    p.lineTo(x, this.lastY);
                    this.lastX = x;
                    break;
                }
                p.rLineTo(x, 0.0f);
                this.lastX += x;
                break;
            case 5:
            case 6:
                x = x1;
                y = y1;
                if (cmd != SVGCommand.m) {
                    p.moveTo(x, y);
                    this.lastX = x;
                    this.lastY = y;
                    break;
                }
                p.rMoveTo(x, y);
                this.lastX += x;
                this.lastY += y;
                break;
            case 7:
            case 8:
                p.close();
                this.lastX1 = 0.0f;
                this.lastY1 = 0.0f;
                this.lastX = 0.0f;
                this.lastY = 0.0f;
                break;
            case 9:
            case 10:
                x = x1;
                y = y1;
                if (cmd != SVGCommand.l) {
                    p.lineTo(x, y);
                    this.lastX = x;
                    this.lastY = y;
                    break;
                }
                p.rLineTo(x, y);
                this.lastX += x;
                this.lastY += y;
                break;
            case 11:
            case 12:
                wasCurve = true;
                float tx1 = x1;
                float ty1 = y1;
                tx2 = x2;
                ty2 = y2;
                tx = x3;
                ty = y3;
                if (cmd == SVGCommand.c) {
                    tx1 += this.lastX;
                    tx2 += this.lastX;
                    tx += this.lastX;
                    ty1 += this.lastY;
                    ty2 += this.lastY;
                    ty += this.lastY;
                }
                p.cubicTo(tx1, ty1, tx2, ty2, tx, ty);
                this.lastX1 = tx2;
                this.lastY1 = ty2;
                this.lastX = tx;
                this.lastY = ty;
                break;
            case 13:
            case 14:
                wasCurve = true;
                tx2 = x1;
                ty2 = y1;
                tx = x2;
                ty = y2;
                if (cmd == SVGCommand.s) {
                    tx2 += this.lastX;
                    tx += this.lastX;
                    ty2 += this.lastY;
                    ty += this.lastY;
                }
                p.cubicTo((2.0f * this.lastX) - this.lastX1, (2.0f * this.lastY) - this.lastY1, tx2, ty2, tx, ty);
                this.lastX1 = tx2;
                this.lastY1 = ty2;
                this.lastX = tx;
                this.lastY = ty;
                break;
        }
        if (!wasCurve) {
            this.lastX1 = this.lastX;
            this.lastY1 = this.lastY;
        }
    }
}
