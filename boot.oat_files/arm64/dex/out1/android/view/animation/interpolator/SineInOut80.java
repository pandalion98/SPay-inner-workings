package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineInOut80 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.0f, 0.195f}, new float[]{0.195f, 0.48f, 0.645f}, new float[]{0.645f, 0.835f, 0.885f}, new float[]{0.885f, 0.955f, 0.978f}, new float[]{0.978f, 0.9999f, 1.0f}};

    public SineInOut80(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
