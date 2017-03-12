package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class CubicEaseOut extends BaseInterpolator {
    public CubicEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        t -= 1.0f;
        return ((t * t) * t) + 1.0f;
    }
}
