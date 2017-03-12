package android.app.im.feature;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public interface IScaleView extends IInjection {
    public static final int ON_SCALE = 2;
    public static final int ON_SCALE_BEGIN = 4;
    public static final int ON_SCALE_END = 3;
    public static final int ON_TOUCH = 1;

    void onScale(ScaleGestureDetector scaleGestureDetector);

    void onScaleBegin(ScaleGestureDetector scaleGestureDetector);

    void onScaleEnd(ScaleGestureDetector scaleGestureDetector);

    void onTouch(MotionEvent motionEvent);
}
