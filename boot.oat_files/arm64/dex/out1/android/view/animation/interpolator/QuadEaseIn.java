package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class QuadEaseIn extends BaseInterpolator {
    public QuadEaseIn(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return in(t);
    }

    private float in(float t) {
        return t * t;
    }
}
