package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public final class DrawingSpace extends View {
    public DrawingSpace(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DrawingSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DrawingSpace(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingSpace(Context context) {
        this(context, null);
    }

    private static int getDefaultSizeNonGreedy(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                return Math.min(size, specSize);
            case 0:
                return size;
            case 1073741824:
                return specSize;
            default:
                return result;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSizeNonGreedy(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSizeNonGreedy(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
}
