package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class ExpoEaseInOut extends BaseInterpolator {
    public ExpoEaseInOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return inout(t);
    }

    private float inout(float t) {
        if (t == 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        t *= 2.0f;
        if (t < 1.0f) {
            return (float) (Math.pow(2.0d, (double) (10.0f * (t - 1.0f))) * 0.5d);
        }
        return (float) (((-Math.pow(2.0d, (double) (-10.0f * (t - 1.0f)))) + 2.0d) * 0.5d);
    }
}
