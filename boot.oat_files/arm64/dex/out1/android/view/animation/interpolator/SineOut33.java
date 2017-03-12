package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineOut33 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.386f, 0.645f}, new float[]{0.645f, 0.962f, 1.0f}};

    public SineOut33(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
