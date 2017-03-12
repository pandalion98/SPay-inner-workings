package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.animation.BaseInterpolator;

public class CircEaseOut extends BaseInterpolator {
    public CircEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        t -= 1.0f;
        return FloatMath.sqrt(1.0f - (t * t));
    }
}
