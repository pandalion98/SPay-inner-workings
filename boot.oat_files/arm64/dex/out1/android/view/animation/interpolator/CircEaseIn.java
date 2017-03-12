package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.animation.BaseInterpolator;

public class CircEaseIn extends BaseInterpolator {
    public CircEaseIn(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return in(t);
    }

    private float in(float t) {
        return -(FloatMath.sqrt(1.0f - (t * t)) - 1.0f);
    }
}
