package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineInOut33 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.05f, 0.495f}, new float[]{0.495f, 0.94f, 1.0f}};

    public SineInOut33(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
