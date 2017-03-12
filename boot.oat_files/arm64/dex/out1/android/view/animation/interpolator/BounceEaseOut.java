package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;

public class BounceEaseOut extends BaseInterpolator {
    public BounceEaseOut(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        return out(t);
    }

    private float out(float t) {
        if (((double) t) < 0.36363636363636365d) {
            return (7.5625f * t) * t;
        }
        if (((double) t) < 0.7272727272727273d) {
            t = (float) (((double) t) - 0.5454545454545454d);
            return ((7.5625f * t) * t) + 0.75f;
        } else if (((double) t) < 0.9090909090909091d) {
            t = (float) (((double) t) - 0.8181818181818182d);
            return ((7.5625f * t) * t) + 0.9375f;
        } else {
            t = (float) (((double) t) - 0.9545454545454546d);
            return ((7.5625f * t) * t) + 0.984375f;
        }
    }
}
