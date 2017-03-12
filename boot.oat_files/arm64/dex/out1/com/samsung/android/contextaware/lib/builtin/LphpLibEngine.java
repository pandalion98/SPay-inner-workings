package com.samsung.android.contextaware.lib.builtin;

public class LphpLibEngine {
    public final native void native_LPHPEngine_hybridGpsDeliverGpsData(double[] dArr, float[] fArr, int[] iArr, long j, int i, int i2);

    public final native void native_LPHPEngine_hybridGpsDeliverRotationData(int i);

    public final native void native_LPHPEngine_hybridGpsDeliverSensorHubData(int[] iArr, double d, double d2, double d3, double d4, double d5, int i);

    public final native int native_LPHPEngine_hybridGpsDeliverSensorsData(float[][] fArr, float[][] fArr2, float[][] fArr3, long[] jArr, long[] jArr2);

    public final native void native_LPHPEngine_hybridGpsDeliverSvStatus(int i, int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr2);

    public final native void native_LPHPEngine_hybridGpsDeliverWpsData(double[] dArr, int[] iArr, long j, int i, int i2);

    public final native void native_LPHPEngine_hybridGpsFinalize();

    public final native int native_LPHPEngine_hybridGpsGetPedestrianStatus();

    public final native void native_LPHPEngine_hybridGpsInitialize(int i);

    public final native void native_LPHPEngine_hybridGpsRequestGpsData(double[] dArr, float[] fArr, int[] iArr);

    public final native void native_LPHPEngine_hybridGpsSetAccuracy(int i);

    public final native int native_LPHPEngine_hybridGpshybridGpsIsFilterInitialized();
}
