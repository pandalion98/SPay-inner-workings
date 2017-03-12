package android.view.animation.interpolator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import com.android.internal.R;

public class BackEaseInOut extends BaseInterpolator {
    private float overshot;

    public BackEaseInOut(float overshot) {
        this.overshot = overshot;
    }

    public BackEaseInOut(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public BackEaseInOut(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.BackEaseInOut, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.BackEaseInOut);
        }
        this.overshot = a.getFloat(0, 0.0f);
        a.recycle();
    }

    public float getInterpolation(float t) {
        return inout(t, this.overshot);
    }

    private float inout(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        t *= 2.0f;
        if (t < 1.0f) {
            o = (float) (((double) o) * 1.525d);
            return ((t * t) * (((1.0f + o) * t) - o)) * 0.5f;
        }
        t -= 2.0f;
        o = (float) (((double) o) * 1.525d);
        return (((t * t) * (((1.0f + o) * t) + o)) + 2.0f) * 0.5f;
    }
}
