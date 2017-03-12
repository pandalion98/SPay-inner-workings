package com.android.internal.policy.multiwindow;

import android.app.Activity;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.android.internal.R;

public class ApplicationSquareThumbnail extends ApplicationThumbnail {
    private static final float SQUARE_RATIO = 0.9f;
    private boolean mFakeMinimize = false;
    private int mIconSize;

    public static ApplicationThumbnail create(Activity a) {
        return new ApplicationSquareThumbnail(a);
    }

    public static ApplicationThumbnail create(Activity a, ComponentName c) {
        return new ApplicationSquareThumbnail(a, c);
    }

    private ApplicationSquareThumbnail(Activity a) {
        super(a);
        this.mMinimizeBg = a.getResources().getDrawable(R.drawable.mw_window_iconic_square_bg);
        this.mIconSize = a.getResources().getDimensionPixelSize(R.dimen.mw_window_iconic_square_size);
    }

    private ApplicationSquareThumbnail(Activity a, ComponentName c) {
        super(a, c);
        this.mMinimizeBg = a.getResources().getDrawable(R.drawable.mw_window_iconic_square_bg);
        this.mIconSize = a.getResources().getDimensionPixelSize(R.dimen.mw_window_iconic_square_size);
    }

    protected Bitmap getMinimizedBitmap(boolean bFocused) {
        Drawable bgDrawable;
        if (bFocused) {
            bgDrawable = this.mMinimizeBgFocus;
        } else {
            bgDrawable = this.mMinimizeBg;
        }
        Bitmap output = Bitmap.createBitmap(this.mIconSize, this.mIconSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        bgDrawable.setBounds(new Rect(0, 0, this.mIconSize, this.mIconSize));
        if (!this.mFakeMinimize) {
            bgDrawable.draw(canvas);
        }
        int center = this.mIconSize / 2;
        int halfSize = ((int) ((((double) this.mIconSize) / Math.sqrt(2.0d)) * 0.8999999761581421d)) / 2;
        this.mMinimizeIcon.setBounds(new Rect(center - halfSize, center - halfSize, center + halfSize, center + halfSize));
        this.mMinimizeIcon.draw(canvas);
        return output;
    }
}
