package com.samsung.android.visualeffect.scroll.common;

import android.graphics.Matrix;
import android.graphics.Path;

public class IndexScrollPath15Frames {
    private float lastX;
    private float lastX1;
    private float lastY;
    private float lastY1;
    private final float offsetX = -475.949f;
    private final float offsetY = -423.819f;
    private Path[] path = new Path[15];
    private final int pathTotal = 15;
    private float ratio = 1.41f;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand = new int[SVGCommand.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.V.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.v.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.H.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.h.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.M.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.m.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.Z.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.z.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.L.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.l.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.C.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.c.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.S.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[SVGCommand.s.ordinal()] = 14;
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

    public IndexScrollPath15Frames(float scale) {
        this.ratio *= scale;
        setPath();
    }

    private void setPath() {
        Matrix mtrx = new Matrix();
        mtrx.postTranslate(-475.949f, -423.819f);
        mtrx.postScale(this.ratio, this.ratio);
        int count = -1 + 1;
        Path seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 558.556f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -101.25f, -11.431f, -87.928f, -11.431f, -134.736f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -42.143f, 11.431f, -23.086f, 11.431f, -134.738f);
        vectorParsing(seqPath, SVGCommand.V, 558.556f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 583.819f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -120.234f, -21.431f, -107.232f, -21.431f, -160.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -54.641f, 21.431f, -33.027f, 21.431f, -160.0f);
        vectorParsing(seqPath, SVGCommand.V, 583.819f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 588.819f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -117.609f, -31.431f, -104.641f, -31.431f, -165.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -62.291f, 31.431f, -40.678f, 31.431f, -165.0f);
        vectorParsing(seqPath, SVGCommand.V, 588.819f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 596.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -113.916f, -41.431f, -102.9f, -41.431f, -172.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -71.336f, 41.431f, -55.52f, 41.431f, -172.5f);
        vectorParsing(seqPath, SVGCommand.V, 596.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 611.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -123.82f, -61.431f, -111.85f, -61.431f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.539f, 61.431f, -60.348f, 61.431f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 633.913f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -135.238f, -81.431f, -125.365f, -81.431f, -210.096f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -85.893f, 81.431f, -67.492f, 81.431f, -210.0f);
        vectorParsing(seqPath, SVGCommand.V, 633.913f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 521.741f);
        vectorParsing(seqPath, SVGCommand.v, 147.125f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -157.727f, -90.414f, -159.357f, -90.431f, -245.033f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.004f, 0.0f, -0.01f, 0.0f, -0.014f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.002f, 0.0f, -0.002f, 0.0f, -0.004f);
        vectorParsing(seqPath, SVGCommand.s, 0.0f, -0.002f, 0.0f, -0.004f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -87.303f, 90.431f, -78.727f, 90.431f, -244.946f);
        vectorParsing(seqPath, SVGCommand.v, 147.029f);
        vectorParsing(seqPath, SVGCommand.V, 521.741f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 521.296f);
        vectorParsing(seqPath, SVGCommand.v, 147.57f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -104.541f, -48.503f, -137.873f, -81.22f, -175.158f);
        vectorParsing(seqPath, SVGCommand.c, -10.288f, -10.162f, -18.345f, -22.576f, -23.366f, -36.445f);
        vectorParsing(seqPath, SVGCommand.c, -3.692f, -9.742f, -5.84f, -20.648f, -5.844f, -33.422f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.008f, -0.001f, -0.014f, -0.001f, -0.021f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.002f, 0.0f, -0.004f, 0.0f, -0.004f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.002f, 0.0f, -0.004f, 0.0f, -0.006f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -15.129f, 2.866f, -27.424f, 7.669f, -38.104f);
        vectorParsing(seqPath, SVGCommand.c, 5.546f, -13.16f, 13.874f, -24.859f, 24.25f, -34.359f);
        vectorParsing(seqPath, SVGCommand.c, 2.959f, -2.709f, 5.902f, -5.436f, 8.743f, -8.268f);
        vectorParsing(seqPath, SVGCommand.c, 31.485f, -31.383f, 69.769f, -66.343f, 69.769f, -164.213f);
        vectorParsing(seqPath, SVGCommand.V, 326.34f);
        vectorParsing(seqPath, SVGCommand.V, 521.296f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 516.644f);
        vectorParsing(seqPath, SVGCommand.v, 152.223f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.578f, -5.94f, -64.434f, -15.063f, -86.795f);
        vectorParsing(seqPath, SVGCommand.c, -20.052f, -43.871f, -56.916f, -67.645f, -65.194f, -72.568f);
        vectorParsing(seqPath, SVGCommand.c, -29.941f, -16.83f, -50.174f, -48.891f, -50.174f, -85.684f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -36.086f, 19.462f, -67.621f, 48.457f, -84.697f);
        vectorParsing(seqPath, SVGCommand.l, 0.005f, -0.01f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.045f, -0.023f, 0.129f, -0.07f);
        vectorParsing(seqPath, SVGCommand.c, 0.784f, -0.459f, 1.575f, -0.912f, 2.375f, -1.35f);
        vectorParsing(seqPath, SVGCommand.c, 9.827f, -5.807f, 46.201f, -29.478f, 65.695f, -72.157f);
        vectorParsing(seqPath, SVGCommand.c, 8.387f, -22.134f, 13.77f, -49.979f, 13.77f, -86.67f);
        vectorParsing(seqPath, SVGCommand.v, 152.127f);
        vectorParsing(seqPath, SVGCommand.V, 516.644f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 490.419f);
        vectorParsing(seqPath, SVGCommand.v, 178.447f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -29.924f, -3.977f, -54.008f, -10.416f, -74.023f);
        vectorParsing(seqPath, SVGCommand.c, -17.263f, -49.383f, -81.459f, -73.822f, -93.135f, -77.904f);
        vectorParsing(seqPath, SVGCommand.c, -0.226f, -0.076f, -0.448f, -0.154f, -0.674f, -0.232f);
        vectorParsing(seqPath, SVGCommand.c, -0.748f, -0.256f, -1.158f, -0.389f, -1.158f, -0.389f);
        vectorParsing(seqPath, SVGCommand.v, -0.02f);
        vectorParsing(seqPath, SVGCommand.c, -37.922f, -13.611f, -65.047f, -49.875f, -65.047f, -92.48f);
        vectorParsing(seqPath, SVGCommand.s, 27.129f, -78.873f, 65.053f, -92.48f);
        vectorParsing(seqPath, SVGCommand.v, -0.004f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 0.064f, -0.021f, 0.169f, -0.055f);
        vectorParsing(seqPath, SVGCommand.c, 0.854f, -0.305f, 1.714f, -0.598f, 2.579f, -0.879f);
        vectorParsing(seqPath, SVGCommand.c, 13.869f, -4.901f, 76.337f, -29.361f, 93.167f, -77.667f);
        vectorParsing(seqPath, SVGCommand.c, 5.877f, -19.865f, 9.462f, -43.921f, 9.462f, -73.866f);
        vectorParsing(seqPath, SVGCommand.v, 178.352f);
        vectorParsing(seqPath, SVGCommand.V, 490.419f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 467.882f, 247.768f);
        vectorParsing(seqPath, SVGCommand.c, -20.18f, 71.22f, -89.707f, 79.547f, -124.169f, 79.14f);
        vectorParsing(seqPath, SVGCommand.c, -0.396f, -0.005f, -0.787f, 0.002f, -1.173f, -0.017f);
        vectorParsing(seqPath, SVGCommand.c, -3.1f, -0.156f, -4.348f, -0.425f, -10.92f, -0.99f);
        vectorParsing(seqPath, SVGCommand.c, -0.114f, -0.009f, -0.229f, -0.021f, -0.344f, -0.028f);
        vectorParsing(seqPath, SVGCommand.c, -0.001f, 0.0f, -0.003f, -0.002f, -0.005f, -0.002f);
        vectorParsing(seqPath, SVGCommand.c, -2.481f, -0.188f, -4.99f, -0.283f, -7.521f, -0.283f);
        vectorParsing(seqPath, SVGCommand.c, -54.252f, 0.0f, -98.232f, 43.98f, -98.232f, 98.232f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.25f, 43.98f, 98.23f, 98.232f, 98.23f);
        vectorParsing(seqPath, SVGCommand.c, 2.53f, 0.0f, 5.039f, -0.096f, 7.521f, -0.283f);
        vectorParsing(seqPath, SVGCommand.c, 0.002f, 0.0f, 0.004f, 0.0f, 0.005f, -0.002f);
        vectorParsing(seqPath, SVGCommand.c, 0.115f, -0.008f, 0.229f, -0.02f, 0.344f, -0.029f);
        vectorParsing(seqPath, SVGCommand.c, 6.572f, -0.563f, 7.82f, -0.834f, 10.92f, -0.99f);
        vectorParsing(seqPath, SVGCommand.c, 0.386f, -0.018f, 0.776f, -0.012f, 1.173f, -0.016f);
        vectorParsing(seqPath, SVGCommand.c, 34.462f, -0.408f, 103.989f, 7.92f, 124.169f, 79.139f);
        vectorParsing(seqPath, SVGCommand.c, 5.044f, 18.883f, 8.067f, 41.408f, 8.067f, 68.904f);
        vectorParsing(seqPath, SVGCommand.V, 423.819f);
        vectorParsing(seqPath, SVGCommand.V, 178.865f);
        vectorParsing(seqPath, SVGCommand.C, 475.949f, 206.361f, 472.926f, 228.887f, 467.882f, 247.768f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 470.513f, 236.688f);
        vectorParsing(seqPath, SVGCommand.c, -37.409f, 129.311f, -125.933f, 105.129f, -147.181f, 97.208f);
        vectorParsing(seqPath, SVGCommand.c, -1.529f, -0.674f, -3.078f, -1.311f, -4.646f, -1.906f);
        vectorParsing(seqPath, SVGCommand.c, -10.855f, -4.133f, -22.629f, -6.402f, -34.936f, -6.402f);
        vectorParsing(seqPath, SVGCommand.c, -54.252f, 0.0f, -98.23f, 43.98f, -98.23f, 98.232f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.25f, 43.979f, 98.23f, 98.23f, 98.23f);
        vectorParsing(seqPath, SVGCommand.c, 14.314f, 0.0f, 27.912f, -3.064f, 40.178f, -8.568f);
        vectorParsing(seqPath, SVGCommand.c, 21.921f, -8.102f, 106.916f, -30.068f, 146.018f, 97.455f);
        vectorParsing(seqPath, SVGCommand.c, 3.793f, 16.555f, 6.003f, 35.619f, 6.003f, 57.93f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.948f, 201.176f, 473.957f, 220.212f, 470.513f, 236.688f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 386.113f, 364.853f);
        vectorParsing(seqPath, SVGCommand.c, -6.917f, 7.637f, -15.663f, 11.949f, -22.433f, 11.949f);
        vectorParsing(seqPath, SVGCommand.c, -8.125f, 0.0f, -12.818f, -3.17f, -25.315f, -16.871f);
        vectorParsing(seqPath, SVGCommand.v, 0.004f);
        vectorParsing(seqPath, SVGCommand.c, -18.016f, -21.023f, -44.758f, -34.348f, -74.614f, -34.348f);
        vectorParsing(seqPath, SVGCommand.c, -54.253f, 0.0f, -98.232f, 43.981f, -98.232f, 98.233f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.25f, 43.979f, 98.23f, 98.232f, 98.23f);
        vectorParsing(seqPath, SVGCommand.c, 30.76f, 0.0f, 58.214f, -14.143f, 76.225f, -36.275f);
        vectorParsing(seqPath, SVGCommand.c, 11.304f, -12.217f, 15.94f, -15.119f, 23.705f, -15.119f);
        vectorParsing(seqPath, SVGCommand.c, 7.211f, 0.0f, 15.965f, 4.684f, 22.964f, 13.25f);
        vectorParsing(seqPath, SVGCommand.c, 0.487f, 0.635f, 0.979f, 1.268f, 1.478f, 1.898f);
        vectorParsing(seqPath, SVGCommand.c, 0.02f, 0.027f, 0.042f, 0.053f, 0.062f, 0.08f);
        vectorParsing(seqPath, SVGCommand.l, 0.001f, -0.002f);
        vectorParsing(seqPath, SVGCommand.c, 32.315f, 40.836f, 87.765f, 71.205f, 87.765f, 182.984f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.949f, 299.208f, 418.065f, 324.435f, 386.113f, 364.853f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 349.401f, 398.815f);
        vectorParsing(seqPath, SVGCommand.l, -0.003f, 0.008f);
        vectorParsing(seqPath, SVGCommand.c, -11.057f, -42.145f, -49.404f, -73.236f, -95.018f, -73.236f);
        vectorParsing(seqPath, SVGCommand.c, -54.252f, 0.0f, -98.231f, 43.978f, -98.231f, 98.232f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 54.252f, 43.979f, 98.23f, 98.231f, 98.23f);
        vectorParsing(seqPath, SVGCommand.c, 45.419f, 0.0f, 83.632f, -30.828f, 94.873f, -72.699f);
        vectorParsing(seqPath, SVGCommand.l, 0.006f, 0.004f);
        vectorParsing(seqPath, SVGCommand.c, 2.93f, -9.145f, 9.025f, -25.553f, 15.26f, -25.553f);
        vectorParsing(seqPath, SVGCommand.C, 358.384f, 423.802f, 352.383f, 408.034f, 349.401f, 398.815f);
        vectorParsing(seqPath, SVGCommand.z);
        vectorParsing(seqPath, SVGCommand.M, 387.429f, 392.333f);
        vectorParsing(seqPath, SVGCommand.l, -0.003f, -0.004f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, -0.008f, 0.031f, -0.022f, 0.092f);
        vectorParsing(seqPath, SVGCommand.c, -0.336f, 1.146f, -0.654f, 2.311f, -0.954f, 3.496f);
        vectorParsing(seqPath, SVGCommand.c, -2.36f, 8.246f, -8.698f, 27.885f, -15.545f, 27.885f);
        vectorParsing(seqPath, SVGCommand.c, 7.172f, 0.0f, 13.658f, 20.918f, 15.684f, 28.135f);
        vectorParsing(seqPath, SVGCommand.c, 0.209f, 0.809f, 0.424f, 1.611f, 0.649f, 2.404f);
        vectorParsing(seqPath, SVGCommand.c, 0.001f, 0.002f, 0.006f, 0.021f, 0.006f, 0.021f);
        vectorParsing(seqPath, SVGCommand.v, -0.002f);
        vectorParsing(seqPath, SVGCommand.c, 17.828f, 62.609f, 88.706f, 76.602f, 88.706f, 214.504f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.949f, 323.687f, 405.554f, 330.763f, 387.429f, 392.333f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 385.52f, 423.812f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 85.697f, 90.43f, 87.313f, 90.43f, 245.055f);
        vectorParsing(seqPath, SVGCommand.v, -490.0f);
        vectorParsing(seqPath, SVGCommand.C, 475.949f, 345.085f, 385.52f, 336.509f, 385.52f, 423.812f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
    }

    public Path getPath(int index) {
        return this.path[index];
    }

    public float getCircleCenterX() {
        return -245.25801f * this.ratio;
    }

    public float getCircleCenterY() {
        return 0.0f * this.ratio;
    }

    public float getCircleRadius() {
        return 98.231f * this.ratio;
    }

    public int getPathTotal() {
        return 15;
    }

    private void vectorParsing(Path p, SVGCommand cmd) {
        vectorParsing(p, cmd, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float value) {
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[cmd.ordinal()]) {
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
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$IndexScrollPath15Frames$SVGCommand[cmd.ordinal()]) {
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
