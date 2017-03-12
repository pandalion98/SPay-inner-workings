package com.samsung.android.smartclip;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;

public class SmartClipCroppedAreaImpl implements SlookSmartClipCroppedArea {
    private Rect mRect = null;

    public SmartClipCroppedAreaImpl(Rect rect) {
        this.mRect = rect;
    }

    public Rect getRect() {
        return new Rect(this.mRect);
    }

    public boolean intersects(View view) {
        if (view == null || this.mRect == null) {
            return false;
        }
        return intersects(getScreenRectOfView(view));
    }

    public boolean intersects(Rect rect) {
        if (rect == null || this.mRect == null) {
            return false;
        }
        return Rect.intersects(getRect(), rect);
    }

    private Rect getScreenRectOfView(View view) {
        Rect screenRectOfView = new Rect();
        Point screenPointOfView = getScreenPointOfView(view);
        screenRectOfView.left = screenPointOfView.x;
        screenRectOfView.top = screenPointOfView.y;
        screenRectOfView.right = screenRectOfView.left + view.getWidth();
        screenRectOfView.bottom = screenRectOfView.top + view.getHeight();
        return screenRectOfView;
    }

    private Point getScreenPointOfView(View view) {
        Point screenPointOfView = new Point();
        int[] screenOffsetOfView = new int[2];
        view.getLocationOnScreen(screenOffsetOfView);
        screenPointOfView.x = screenOffsetOfView[0];
        screenPointOfView.y = screenOffsetOfView[1];
        return screenPointOfView;
    }
}
