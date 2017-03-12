package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class ExpoEaseIn extends BaseInterpolator {
    public ExpoEaseIn(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return in(t);
    }

    private float in(float t) {
        return (float) (t == 0.0f ? 0.0d : Math.pow(2.0d, (double) (10.0f * (t - 1.0f))));
    }
}
