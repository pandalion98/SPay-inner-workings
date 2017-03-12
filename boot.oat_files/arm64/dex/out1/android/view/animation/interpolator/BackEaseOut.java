package android.view.animation.interpolator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import com.android.internal.R;

public class BackEaseOut extends BaseInterpolator {
    private float overshot;

    public BackEaseOut(float overshot) {
        this.overshot = overshot;
    }

    public BackEaseOut(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public BackEaseOut(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.BackEaseOut, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.BackEaseOut);
        }
        this.overshot = a.getFloat(0, 0.0f);
        a.recycle();
    }

    public float getInterpolation(float t) {
        return out(t, this.overshot);
    }

    private float out(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        t -= 1.0f;
        return ((t * t) * (((o + 1.0f) * t) + o)) + 1.0f;
    }
}
