package android.view.animation.interpolator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import com.android.internal.R;

public class ElasticEaseOut extends BaseInterpolator {
    private float amplitude;
    private float period;

    public ElasticEaseOut(float amplitude, float period) {
        this.amplitude = amplitude;
        this.period = period;
    }

    public ElasticEaseOut(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public ElasticEaseOut(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.ElasticEaseOut, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.ElasticEaseOut);
        }
        this.amplitude = a.getFloat(0, 0.0f);
        this.period = a.getFloat(1, 0.0f);
        a.recycle();
    }

    public float getInterpolation(float t) {
        return out(t, this.amplitude, this.period);
    }

    private float out(float t, float a, float p) {
        if (t == 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        float s;
        if (p == 0.0f) {
            p = 0.3f;
        }
        if (a == 0.0f || a < 1.0f) {
            a = 1.0f;
            s = p / 4.0f;
        } else {
            s = (float) (Math.asin((double) (1.0f / a)) * (((double) p) / 6.283185307179586d));
        }
        return (float) (((((double) a) * Math.pow(2.0d, (double) (-10.0f * t))) * Math.sin((((double) (t - s)) * 6.283185307179586d) / ((double) p))) + 1.0d);
    }
}
