package android.location;

public interface ExerciseLocationListener {
    void onLocationChanged(long[] jArr, double[] dArr, double[] dArr2, float[] fArr, float[] fArr2, float[] fArr3, double[] dArr3, double[] dArr4, long[] jArr2, int i);

    void onStatusChanged(int i);
}
