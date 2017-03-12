package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.animation.BaseInterpolator;

public class CircEaseInOut extends BaseInterpolator {
    public CircEaseInOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return inout(t);
    }

    private float inout(float t) {
        t *= 2.0f;
        if (t < 1.0f) {
            return -0.5f * (FloatMath.sqrt(1.0f - (t * t)) - 1.0f);
        }
        t -= 2.0f;
        return 0.5f * (FloatMath.sqrt(1.0f - (t * t)) + 1.0f);
    }
}
