package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class ExpoEaseOut extends BaseInterpolator {
    public ExpoEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        double d = 1.0d;
        if (t < 1.0f) {
            d = 1.0d + (-Math.pow(2.0d, (double) (-10.0f * t)));
        }
        return (float) d;
    }
}
