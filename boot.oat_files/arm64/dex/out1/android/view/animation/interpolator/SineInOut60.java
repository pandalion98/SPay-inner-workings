package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineInOut60 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.01f, 0.37f}, new float[]{0.37f, 0.72f, 0.888f}, new float[]{0.888f, 0.9999f, 1.0f}};

    public SineInOut60(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
