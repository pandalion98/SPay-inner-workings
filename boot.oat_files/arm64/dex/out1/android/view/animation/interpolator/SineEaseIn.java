package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineEaseIn extends BaseInterpolator {
    public SineEaseIn(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return in(t);
    }

    private float in(float t) {
        return (float) ((-Math.cos(((double) t) * 1.5707963267948966d)) + 1.0d);
    }
}
