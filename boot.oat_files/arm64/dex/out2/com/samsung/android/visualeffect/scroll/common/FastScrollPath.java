package com.samsung.android.visualeffect.scroll.common;

import android.graphics.Matrix;
import android.graphics.Path;

public class FastScrollPath {
    private float lastX;
    private float lastX1;
    private float lastY;
    private float lastY1;
    private final float offsetX = -475.949f;
    private final float offsetY = -423.819f;
    private Path[] path = new Path[9];
    private final int pathTotal = 9;
    private float ratio = 1.41f;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand = new int[SVGCommand.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.V.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.v.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.H.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.h.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.M.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.m.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.Z.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.z.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.L.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.l.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.C.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.c.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.S.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[SVGCommand.s.ordinal()] = 14;
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

    public FastScrollPath(float scale) {
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
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -101.26f, -4.949f, -87.906f, -4.949f, -134.736f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -42.143f, 4.949f, -23.086f, 4.949f, -134.738f);
        vectorParsing(seqPath, SVGCommand.V, 558.556f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 583.819f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -120.259f, -11.949f, -107.176f, -11.949f, -160.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -54.641f, 11.949f, -33.027f, 11.949f, -160.0f);
        vectorParsing(seqPath, SVGCommand.V, 583.819f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 588.819f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -117.609f, -19.449f, -104.641f, -19.449f, -165.0f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -62.291f, 19.449f, -40.678f, 19.449f, -165.0f);
        vectorParsing(seqPath, SVGCommand.V, 588.819f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 596.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -113.916f, -28.449f, -102.9f, -28.449f, -172.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -71.336f, 28.449f, -55.52f, 28.449f, -172.5f);
        vectorParsing(seqPath, SVGCommand.V, 596.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 611.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -133.986f, -38.282f, -111.85f, -38.282f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.539f, 38.282f, -53.268f, 38.282f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 611.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -133.986f, -48.949f, -111.85f, -48.949f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.539f, 48.949f, -53.268f, 48.949f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 611.319f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -125.32f, -59.449f, -111.85f, -59.449f, -187.5f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -77.539f, 59.449f, -63.153f, 59.449f, -187.5f);
        vectorParsing(seqPath, SVGCommand.V, 611.319f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 633.913f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -143.494f, -69.949f, -125.365f, -69.949f, -210.096f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -85.893f, 69.949f, -63.151f, 69.949f, -210.0f);
        vectorParsing(seqPath, SVGCommand.V, 633.913f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
        count++;
        seqPath = new Path();
        vectorParsing(seqPath, SVGCommand.M, 475.949f, 521.741f);
        vectorParsing(seqPath, SVGCommand.v, 147.125f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -174.199f, -83.433f, -159.357f, -83.449f, -245.033f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.004f, 0.0f, -0.01f, 0.0f, -0.014f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -0.002f, 0.0f, -0.002f, 0.0f, -0.004f);
        vectorParsing(seqPath, SVGCommand.s, 0.0f, -0.002f, 0.0f, -0.004f);
        vectorParsing(seqPath, SVGCommand.c, 0.0f, -87.303f, 83.449f, -63.812f, 83.449f, -244.946f);
        vectorParsing(seqPath, SVGCommand.v, 147.029f);
        vectorParsing(seqPath, SVGCommand.V, 521.741f);
        vectorParsing(seqPath, SVGCommand.z);
        seqPath.transform(mtrx);
        this.path[count] = seqPath;
    }

    public Path getPath(int index) {
        return this.path[index];
    }

    public int getPathTotal() {
        return 9;
    }

    private void vectorParsing(Path p, SVGCommand cmd) {
        vectorParsing(p, cmd, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void vectorParsing(Path p, SVGCommand cmd, float value) {
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[cmd.ordinal()]) {
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
        switch (AnonymousClass1.$SwitchMap$com$samsung$android$visualeffect$scroll$common$FastScrollPath$SVGCommand[cmd.ordinal()]) {
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
