package android.view.animation.interpolator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import com.android.internal.R;

public class BackEaseIn extends BaseInterpolator {
    private float overshot;

    public BackEaseIn(float overshot) {
        this.overshot = overshot;
    }

    public BackEaseIn(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public BackEaseIn(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.BackEaseIn, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.BackEaseIn);
        }
        this.overshot = a.getFloat(0, 0.0f);
        a.recycle();
    }

    public float getInterpolation(float t) {
        return in(t, this.overshot);
    }

    private float in(float t, float o) {
        if (o == 0.0f) {
            o = 1.70158f;
        }
        return (t * t) * (((1.0f + o) * t) - o);
    }
}
