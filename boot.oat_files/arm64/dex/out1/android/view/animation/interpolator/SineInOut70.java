package android.view.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import com.samsung.android.multiwindow.MultiWindowFacade;

public class SineInOut70 extends BaseInterpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.01f, 0.45f}, new float[]{0.45f, MultiWindowFacade.SPLIT_MAX_WEIGHT, 0.908f}, new float[]{0.908f, 0.9999f, 1.0f}};

    public SineInOut70(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        return SineBase.getInterpolation(input, segments);
    }
}
