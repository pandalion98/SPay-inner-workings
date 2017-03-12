package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class SineEaseOut extends BaseInterpolator {
    public SineEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        return (float) Math.sin(((double) t) * 1.5707963267948966d);
    }
}
