package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuintEaseOut extends BaseInterpolator {
    public QuintEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        t -= 1.0f;
        return ((((t * t) * t) * t) * t) + 1.0f;
    }
}
