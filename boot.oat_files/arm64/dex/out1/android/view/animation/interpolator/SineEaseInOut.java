package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineEaseInOut extends BaseInterpolator {
    public SineEaseInOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return inout(t);
    }

    private float inout(float t) {
        return (float) (-0.5d * (Math.cos(3.141592653589793d * ((double) t)) - 1.0d));
    }
}
