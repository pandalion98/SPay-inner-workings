package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuintOut50 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.502f, 0.742f}, new float[]{0.742f, 1.082f, 1.0f}};

    public QuintOut50(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
