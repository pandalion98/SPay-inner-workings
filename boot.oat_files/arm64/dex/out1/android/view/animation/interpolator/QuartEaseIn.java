package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuartEaseIn extends BaseInterpolator {
    public QuartEaseIn(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return in(t);
    }

    private float in(float t) {
        return ((t * t) * t) * t;
    }
}
