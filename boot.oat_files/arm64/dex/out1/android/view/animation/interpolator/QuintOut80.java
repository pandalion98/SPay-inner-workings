package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuintOut80 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.718f, 0.845f}, new float[]{0.845f, 0.998f, 1.0f}};

    public QuintOut80(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
