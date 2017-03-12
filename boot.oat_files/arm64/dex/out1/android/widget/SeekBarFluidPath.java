package android.widget;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;

public class SeekBarFluidPath {
    private final String TAG = "SeekBarFluidPath";
    private final int circleTotal = 6;
    private final float cx = 412.978f;
    private final float cy = 234.667f;
    private float lastX;
    private float lastX1;
    private float lastY;
    private float lastY1;
    private final float offsetX = -412.978f;
    private final float offsetY = -234.667f;
    private Path[] path = new Path[15];
    private final int pathTotal = 15;
    private final float radius = 205.038f;
    private float scale;
    private final int shapeTotal = 9;
    private final float tailWidthFromCircleCenter = 329.1f;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand = new int[SVGCommand.values().length];

        static {
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.V.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.v.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.H.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.h.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.M.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.m.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.Z.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.z.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.L.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.l.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.C.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.c.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.S.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[SVGCommand.s.ordinal()] = 14;
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

    public SeekBarFluidPath(float thumbWidth) {
        this.scale = thumbWidth / 205.038f;
        setPath();
    }

    private void setPath() {
        Path seqPath;
        int count = -1;
        Matrix mtrx = new Matrix();
        mtrx.postTranslate(-412.978f, -234.667f);
        mtrx.postScale(this.scale, this.scale);
        for (int i = 0; i < 6; i++) {
            count++;
            float radius = 72.978f + (((102.519f - 72.978f) * ((float) i)) / 5.0f);
            seqPath = new Path();
            seqPath.addCircle(412.978f, 234.667f, radius, Direction.CW);
            seqPath.transform(mtrx);
            this.path[count] = seqPath;
        }
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 306.627f, 234.667f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -19.023f, 13.841f, -44.234f, 13.841f, -44.234f);
        vectorParsing(seqPath, SVGCommand.l, -0.003f, 0.011f);
        vectorParsing(seqPath, SVGCommand.c, 16.512f, -34.481f, 51.73f, -58.296f, 92.513f, -58.296f);
        vectorParsing(seqPath, SVGCommand.c, 56.62f, 0.0f, 102.519f, 45.9f, 102.519f, 102.519f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 56.619f, -45.898f, 102.518f, -102.519f, 102.518f);
        vectorParsing(seqPath, SVGCommand.c, -40.782f, 0.0f, -76.001f, -23.814f, -92.513f, -58.295f);
        vectorParsing(seqPath, SVGCommand.l, 0.003f, 0.011f);
        vectorParsing(seqPath, SVGCommand.C, 320.468f, 278.901f, 306.627f, 253.69f, 306.627f, 234.667f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.c, -43.809f, 0.0f, -81.192f, 27.481f, -95.869f, 66.143f);
        vectorParsing(seqPath, SVGCommand.l, -0.009f, 0.002f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, -0.024f, 0.075f, -0.07f, 0.204f);
        vectorParsing(seqPath, SVGCommand.c, -0.084f, 0.222f, -0.171f, 0.443f, -0.253f, 0.666f);
        vectorParsing(seqPath, SVGCommand.c, -1.099f, 2.738f, -5.223f, 11.339f, -15.287f, 16.459f);
        vectorParsing(seqPath, SVGCommand.l, 0.003f, 0.024f);
        vectorParsing(seqPath, SVGCommand.c, -7.134f, 3.37f, -23.743f, 10.609f, -23.743f, 19.021f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 8.394f, 16.588f, 15.619f, 23.697f, 18.998f);
        vectorParsing(seqPath, SVGCommand.l, 0.043f, 0.072f);
        vectorParsing(seqPath, SVGCommand.c, 12.088f, 6.148f, 15.61f, 17.328f, 15.61f, 17.328f);
        vectorParsing(seqPath, SVGCommand.l, 0.014f, -0.007f);
        vectorParsing(seqPath, SVGCommand.c, 14.682f, 38.653f, 52.063f, 66.127f, 95.864f, 66.127f);
        vectorParsing(seqPath, SVGCommand.c, 56.619f, 0.0f, 102.518f, -45.899f, 102.518f, -102.518f);
        vectorParsing(seqPath, SVGCommand.C, 515.495f, 178.048f, 469.597f, 132.148f, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.c, -40.863f, 0.0f, -76.138f, 23.909f, -92.608f, 58.5f);
        vectorParsing(seqPath, SVGCommand.v, -0.011f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, -1.324f, 2.724f, -4.662f, 6.328f);
        vectorParsing(seqPath, SVGCommand.C, 299.0f, 213.0f, 274.0f, 217.0f, 254.613f, 228.965f);
        vectorParsing(seqPath, SVGCommand.c, -3.455f, 2.04f, -6.363f, 4.193f, -6.363f, 5.693f);
        vectorParsing(seqPath, SVGCommand.s, 2.908f, 3.654f, 6.363f, 5.694f);
        vectorParsing(seqPath, SVGCommand.C, 274.0f, 252.316f, 299.0f, 256.316f, 315.707f, 272.35f);
        vectorParsing(seqPath, SVGCommand.c, 3.338f, 3.605f, 4.662f, 6.329f, 4.662f, 6.329f);
        vectorParsing(seqPath, SVGCommand.v, -0.011f);
        vectorParsing(seqPath, SVGCommand.c, 16.471f, 34.59f, 51.745f, 58.5f, 92.608f, 58.5f);
        vectorParsing(seqPath, SVGCommand.c, 56.619f, 0.0f, 102.518f, -45.89f, 102.518f, -102.51f);
        vectorParsing(seqPath, SVGCommand.C, 515.495f, 178.039f, 469.597f, 132.148f, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.c, -37.985f, 0.0f, -71.143f, 20.659f, -88.856f, 51.354f);
        vectorParsing(seqPath, SVGCommand.l, -0.001f, -0.001f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, -10.801f, 21.656f, -39.621f, 30.999f);
        vectorParsing(seqPath, SVGCommand.c, -15.166f, 4.917f, -32.625f, 10.208f, -37.875f, 11.875f);
        vectorParsing(seqPath, SVGCommand.c, -3.93f, 1.248f, -14.875f, 6.042f, -14.875f, 8.292f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 2.062f, 10.461f, 6.061f, 14.25f, 7.458f);
        vectorParsing(seqPath, SVGCommand.c, 9.533f, 3.514f, 21.052f, 5.265f, 38.25f, 11.25f);
        vectorParsing(seqPath, SVGCommand.c, 28.375f, 9.875f, 39.871f, 32.458f, 39.871f, 32.458f);
        vectorParsing(seqPath, SVGCommand.l, 0.001f, -0.002f);
        vectorParsing(seqPath, SVGCommand.c, 17.711f, 30.694f, 50.871f, 51.354f, 88.856f, 51.354f);
        vectorParsing(seqPath, SVGCommand.c, 56.619f, 0.0f, 102.518f, -45.899f, 102.518f, -102.518f);
        vectorParsing(seqPath, SVGCommand.C, 515.495f, 178.048f, 469.597f, 132.148f, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 515.495f, 234.667f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -56.62f, -45.898f, -102.519f, -102.518f, -102.519f);
        vectorParsing(seqPath, SVGCommand.c, -27.64f, 0.0f, -52.724f, 10.939f, -71.16f, 28.722f);
        vectorParsing(seqPath, SVGCommand.c, -2.305f, 2.223f, -4.505f, 4.552f, -6.593f, 6.981f);
        vectorParsing(seqPath, SVGCommand.C, 328.0f, 176.0f, 324.0f, 186.0f, 315.646f, 193.611f);
        vectorParsing(seqPath, SVGCommand.c, -2.525f, 2.465f, -5.457f, 5.026f, -8.847f, 7.576f);
        vectorParsing(seqPath, SVGCommand.c, -10.165f, 7.652f, -24.429f, 15.212f, -44.049f, 19.813f);
        vectorParsing(seqPath, SVGCommand.c, -16.081f, 3.772f, -38.25f, 9.167f, -38.25f, 9.167f);
        vectorParsing(seqPath, SVGCommand.l, -21.312f, 4.461f);
        vectorParsing(seqPath, SVGCommand.h, -0.153f);
        vectorParsing(seqPath, SVGCommand.l, -0.223f, 0.041f);
        vectorParsing(seqPath, SVGCommand.l, 21.5f, 4.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 22.169f, 5.395f, 38.25f, 9.167f);
        vectorParsing(seqPath, SVGCommand.c, 19.621f, 4.602f, 33.884f, 12.161f, 44.05f, 19.813f);
        vectorParsing(seqPath, SVGCommand.c, 3.388f, 2.55f, 6.321f, 5.111f, 8.845f, 7.576f);
        vectorParsing(seqPath, SVGCommand.c, 8.355f, 7.611f, 12.355f, 17.611f, 19.579f, 25.76f);
        vectorParsing(seqPath, SVGCommand.c, 2.089f, 2.429f, 4.289f, 4.759f, 6.594f, 6.981f);
        vectorParsing(seqPath, SVGCommand.c, 18.438f, 17.783f, 43.521f, 28.722f, 71.16f, 28.722f);
        vectorParsing(seqPath, SVGCommand.c, 56.606f, 0.0f, 102.496f, -45.878f, 102.517f, -102.479f);
        vectorParsing(seqPath, SVGCommand.h, 0.188f);
        vectorParsing(seqPath, SVGCommand.C, 515.494f, 234.695f, 515.495f, 234.681f, 515.495f, 234.667f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 193.125f, 236.492f);
        vectorParsing(seqPath, SVGCommand.v, 0.002f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, 26.575f, 2.122f, 63.125f, 10.316f);
        vectorParsing(seqPath, SVGCommand.c, 10.756f, 2.412f, 20.388f, 6.067f, 28.915f, 10.372f);
        vectorParsing(seqPath, SVGCommand.c, 8.526f, 4.305f, 15.948f, 9.259f, 22.285f, 14.269f);
        vectorParsing(seqPath, SVGCommand.C, 325.0f, 283.31f, 334.0f, 304.31f, 351.349f, 316.561f);
        vectorParsing(seqPath, SVGCommand.c, 5.709f, 4.298f, 11.883f, 8.015f, 18.431f, 11.062f);
        vectorParsing(seqPath, SVGCommand.c, 13.096f, 6.095f, 27.694f, 9.51f, 43.091f, 9.539f);
        vectorParsing(seqPath, SVGCommand.c, 55.742f, 0.104f, 101.713f, -45.091f, 102.61f, -100.826f);
        vectorParsing(seqPath, SVGCommand.c, 0.01f, -0.537f, 0.007f, -1.071f, 0.007f, -1.606f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.025f, 0.0f, -0.05f, 0.0f, -0.074f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.025f, 0.0f, -0.05f, 0.0f, -0.075f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.535f, 0.003f, -1.069f, -0.007f, -1.606f);
        vectorParsing(seqPath, SVGCommand.c, -0.897f, -55.734f, -46.868f, -100.93f, -102.61f, -100.826f);
        vectorParsing(seqPath, SVGCommand.c, -15.396f, 0.029f, -29.995f, 3.444f, -43.091f, 9.539f);
        vectorParsing(seqPath, SVGCommand.c, -6.548f, 3.047f, -12.722f, 6.764f, -18.431f, 11.063f);
        vectorParsing(seqPath, SVGCommand.C, 334.0f, 165.0f, 325.0f, 186.0f, 307.45f, 197.859f);
        vectorParsing(seqPath, SVGCommand.c, -6.337f, 5.01f, -13.759f, 9.964f, -22.285f, 14.269f);
        vectorParsing(seqPath, SVGCommand.c, -8.527f, 4.305f, -18.159f, 7.96f, -28.915f, 10.372f);
        vectorParsing(seqPath, SVGCommand.c, -36.55f, 8.195f, -63.125f, 10.316f, -63.125f, 10.316f);
        vectorParsing(seqPath, SVGCommand.l, -18.002f, 1.764f);
        vectorParsing(seqPath, SVGCommand.l, -0.912f, 0.063f);
        vectorParsing(seqPath, SVGCommand.l, 0.126f, 0.012f);
        vectorParsing(seqPath, SVGCommand.l, -0.126f, 0.012f);
        vectorParsing(seqPath, SVGCommand.l, 0.912f, 0.062f);
        vectorParsing(seqPath, SVGCommand.L, 193.125f, 236.492f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 429.795f, 133.539f);
        vectorParsing(seqPath, SVGCommand.c, -5.325f, -0.89f, -10.787f, -1.364f, -16.347f, -1.389f);
        vectorParsing(seqPath, SVGCommand.c, -3.908f, 0.142f, -7.835f, 0.426f, -11.773f, 0.879f);
        vectorParsing(seqPath, SVGCommand.c, -14.586f, 0.304f, -27.641f, 4.321f, -39.675f, 10.647f);
        vectorParsing(seqPath, SVGCommand.c, -2.405f, 1.266f, -4.771f, 2.623f, -7.099f, 4.063f);
        vectorParsing(seqPath, SVGCommand.c, -10.602f, 6.841f, -19.023f, 15.788f, -27.729f, 24.577f);
        vectorParsing(seqPath, SVGCommand.c, -2.177f, 2.197f, -4.372f, 4.383f, -6.63f, 6.514f);
        vectorParsing(seqPath, SVGCommand.C, 297.0f, 200.001f, 270.0f, 220.001f, 238.239f, 225.764f);
        vectorParsing(seqPath, SVGCommand.c, -2.809f, 0.559f, -5.635f, 1.054f, -8.469f, 1.5f);
        vectorParsing(seqPath, SVGCommand.c, -4.724f, 0.743f, -9.474f, 1.349f, -14.218f, 1.881f);
        vectorParsing(seqPath, SVGCommand.c, -2.373f, 0.266f, -4.744f, 0.514f, -7.11f, 0.751f);
        vectorParsing(seqPath, SVGCommand.c, -21.442f, 2.105f, -41.109f, 3.721f, -62.526f, 4.721f);
        vectorParsing(seqPath, SVGCommand.c, 21.417f, 1.0f, 41.084f, 2.616f, 62.526f, 4.72f);
        vectorParsing(seqPath, SVGCommand.c, 2.366f, 0.238f, 4.737f, 0.486f, 7.11f, 0.752f);
        vectorParsing(seqPath, SVGCommand.c, 4.744f, 0.532f, 9.494f, 1.138f, 14.218f, 1.881f);
        vectorParsing(seqPath, SVGCommand.c, 2.834f, 0.446f, 5.66f, 0.941f, 8.469f, 1.5f);
        vectorParsing(seqPath, SVGCommand.C, 270.0f, 249.233f, 297.0f, 269.233f, 320.542f, 290.404f);
        vectorParsing(seqPath, SVGCommand.c, 2.258f, 2.131f, 4.453f, 4.317f, 6.63f, 6.514f);
        vectorParsing(seqPath, SVGCommand.c, 8.706f, 8.789f, 17.128f, 17.736f, 27.729f, 24.577f);
        vectorParsing(seqPath, SVGCommand.c, 2.327f, 1.44f, 4.693f, 2.797f, 7.099f, 4.062f);
        vectorParsing(seqPath, SVGCommand.c, 12.034f, 6.327f, 25.089f, 10.344f, 39.675f, 10.648f);
        vectorParsing(seqPath, SVGCommand.c, 3.938f, 0.452f, 7.865f, 0.737f, 11.773f, 0.879f);
        vectorParsing(seqPath, SVGCommand.c, 5.56f, -0.025f, 11.021f, -0.5f, 16.347f, -1.389f);
        vectorParsing(seqPath, SVGCommand.c, 47.937f, -8.006f, 84.991f, -51.043f, 85.69f, -101.078f);
        vectorParsing(seqPath, SVGCommand.C, 514.786f, 184.582f, 477.731f, 141.545f, 429.795f, 133.539f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.c, -5.069f, 0.0f, -10.051f, 0.368f, -14.934f, 1.057f);
        vectorParsing(seqPath, SVGCommand.c, -12.762f, 2.199f, -24.831f, 6.035f, -36.479f, 10.861f);
        vectorParsing(seqPath, SVGCommand.c, -3.882f, 1.609f, -7.718f, 3.328f, -11.516f, 5.132f);
        vectorParsing(seqPath, SVGCommand.c, -3.07f, 1.72f, -6.131f, 3.481f, -9.186f, 5.273f);
        vectorParsing(seqPath, SVGCommand.c, -8.466f, 5.632f, -16.878f, 11.419f, -25.351f, 17.106f);
        vectorParsing(seqPath, SVGCommand.c, -4.237f, 2.844f, -8.489f, 5.663f, -12.766f, 8.442f);
        vectorParsing(seqPath, SVGCommand.c, -22.922f, 15.387f, -54.544f, 28.516f, -80.175f, 36.165f);
        vectorParsing(seqPath, SVGCommand.c, -1.831f, 0.547f, -3.631f, 1.065f, -5.395f, 1.554f);
        vectorParsing(seqPath, SVGCommand.C, 209.0f, 220.0f, 202.0f, 221.0f, 194.077f, 222.746f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, 0.0f, -1.99f, 0.334f, -5.306f, 0.891f);
        vectorParsing(seqPath, SVGCommand.c, -16.583f, 2.784f, -66.333f, 11.074f, -66.333f, 11.074f);
        vectorParsing(seqPath, SVGCommand.s, 49.75f, 8.29f, 66.333f, 11.074f);
        vectorParsing(seqPath, SVGCommand.c, 3.316f, 0.557f, 5.306f, 0.891f, 5.306f, 0.891f);
        vectorParsing(seqPath, SVGCommand.c, 7.924f, 1.745f, 14.924f, 2.745f, 23.101f, 5.007f);
        vectorParsing(seqPath, SVGCommand.c, 1.764f, 0.489f, 3.564f, 1.008f, 5.395f, 1.554f);
        vectorParsing(seqPath, SVGCommand.c, 25.63f, 7.649f, 57.252f, 20.779f, 80.175f, 36.165f);
        vectorParsing(seqPath, SVGCommand.c, 4.276f, 2.779f, 8.528f, 5.598f, 12.766f, 8.442f);
        vectorParsing(seqPath, SVGCommand.c, 8.473f, 5.687f, 16.885f, 11.474f, 25.351f, 17.106f);
        vectorParsing(seqPath, SVGCommand.c, 3.055f, 1.792f, 6.115f, 3.553f, 9.186f, 5.273f);
        vectorParsing(seqPath, SVGCommand.c, 3.798f, 1.805f, 7.634f, 3.523f, 11.516f, 5.132f);
        vectorParsing(seqPath, SVGCommand.c, 11.648f, 4.826f, 23.718f, 8.663f, 36.479f, 10.862f);
        vectorParsing(seqPath, SVGCommand.c, 4.883f, 0.688f, 9.864f, 1.056f, 14.934f, 1.056f);
        vectorParsing(seqPath, SVGCommand.c, 56.619f, 0.0f, 102.518f, -45.942f, 102.518f, -102.562f);
        vectorParsing(seqPath, SVGCommand.C, 515.495f, 178.092f, 469.597f, 132.148f, 412.978f, 132.148f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 515.49f, 234.463f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.042f, 0.003f, -0.084f, 0.003f, -0.126f);
        vectorParsing(seqPath, SVGCommand.h, -0.006f);
        vectorParsing(seqPath, SVGCommand.c, -0.221f, -39.386f, -22.642f, -73.513f, -55.395f, -90.49f);
        vectorParsing(seqPath, SVGCommand.c, -4.703f, -2.438f, -9.62f, -4.521f, -14.721f, -6.219f);
        vectorParsing(seqPath, SVGCommand.c, -24.321f, -8.072f, -47.016f, -5.489f, -68.779f, 2.028f);
        vectorParsing(seqPath, SVGCommand.c, -4.353f, 1.503f, -8.669f, 3.203f, -12.957f, 5.048f);
        vectorParsing(seqPath, SVGCommand.c, -41.634f, 19.301f, -81.634f, 41.301f, -124.231f, 55.891f);
        vectorParsing(seqPath, SVGCommand.c, -4.412f, 1.456f, -8.845f, 2.839f, -13.298f, 4.155f);
        vectorParsing(seqPath, SVGCommand.c, -22.265f, 6.572f, -45.031f, 11.476f, -68.242f, 15.177f);
        vectorParsing(seqPath, SVGCommand.c, -4.588f, 0.686f, -9.192f, 1.326f, -13.81f, 1.933f);
        vectorParsing(seqPath, SVGCommand.c, -4.306f, 0.598f, -8.599f, 1.174f, -12.873f, 1.74f);
        vectorParsing(seqPath, SVGCommand.l, -83.077f, 10.737f);
        vectorParsing(seqPath, SVGCommand.h, -1.944f);
        vectorParsing(seqPath, SVGCommand.l, 0.972f, 0.126f);
        vectorParsing(seqPath, SVGCommand.l, -0.972f, 0.126f);
        vectorParsing(seqPath, SVGCommand.h, 1.944f);
        vectorParsing(seqPath, SVGCommand.l, 83.077f, 10.737f);
        vectorParsing(seqPath, SVGCommand.c, 4.274f, 0.566f, 8.567f, 1.142f, 12.873f, 1.74f);
        vectorParsing(seqPath, SVGCommand.c, 4.618f, 0.607f, 9.222f, 1.247f, 13.81f, 1.933f);
        vectorParsing(seqPath, SVGCommand.c, 23.211f, 3.701f, 45.977f, 8.605f, 68.242f, 15.177f);
        vectorParsing(seqPath, SVGCommand.c, 4.453f, 1.316f, 8.886f, 2.699f, 13.298f, 4.156f);
        vectorParsing(seqPath, SVGCommand.c, 42.597f, 14.589f, 82.597f, 36.589f, 124.231f, 55.89f);
        vectorParsing(seqPath, SVGCommand.c, 4.288f, 1.845f, 8.604f, 3.545f, 12.957f, 5.048f);
        vectorParsing(seqPath, SVGCommand.c, 21.764f, 7.517f, 44.458f, 10.1f, 68.779f, 2.028f);
        vectorParsing(seqPath, SVGCommand.c, 5.101f, -1.697f, 10.018f, -3.781f, 14.721f, -6.219f);
        vectorParsing(seqPath, SVGCommand.c, 32.753f, -16.977f, 55.174f, -51.104f, 55.395f, -90.49f);
        vectorParsing(seqPath, SVGCommand.h, 0.006f);
        vectorParsing(seqPath, SVGCommand.C, 515.493f, 234.547f, 515.49f, 234.505f, 515.49f, 234.463f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
    }

    public Path getPath(int index) {
        return this.path[index];
    }

    public int getPathTotal() {
        return 15;
    }

    public int getCircleTotal() {
        return 6;
    }

    public float getTailWidthFromCircleCenter() {
        return 329.1f * this.scale;
    }

    private void vectorParsing(Path p, SVGCommand cmd) {
        vectorParsing(p, cmd, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float value) {
        switch (AnonymousClass1.$SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[cmd.ordinal()]) {
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
        switch (AnonymousClass1.$SwitchMap$android$widget$SeekBarFluidPath$SVGCommand[cmd.ordinal()]) {
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
