package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineIn33 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.001f, 0.32f}, new float[]{0.32f, 0.59f, 1.0f}};

    public SineIn33(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
