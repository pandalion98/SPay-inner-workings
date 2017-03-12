package com.samsung.android.penselect;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class PenSelectionController {
    private static final String TAG = "PenSelectController";
    private static PenSelectionController sInstance;

    static class PenSelectionContents {
        public String mContentStr;

        PenSelectionContents() {
        }
    }

    public static PenSelectionController getInstance() {
        if (sInstance == null) {
            sInstance = new PenSelectionController();
        }
        return sInstance;
    }

    private PenSelectionController() {
    }

    private boolean isVisibleView(View view) {
        return view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0;
    }

    private boolean getPenSelectionContents(Context context, View view, PenSelectionContents contents) {
        boolean haveContents = false;
        if (!isVisibleView(view)) {
            return false;
        }
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (!textView.hasMultiSelection()) {
                return false;
            }
            CharSequence selectedText = textView.getMultiSelectionText();
            if (TextUtils.isEmpty(selectedText)) {
                return false;
            }
            if (TextUtils.isEmpty(contents.mContentStr)) {
                contents.mContentStr = selectedText.toString();
            } else {
                contents.mContentStr += "\n" + selectedText.toString();
            }
            return true;
        } else if (!(view instanceof ViewGroup)) {
            return false;
        } else {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getPenSelectionContents(context, vg.getChildAt(i), contents)) {
                    haveContents = true;
                }
            }
            return haveContents;
        }
    }

    public String getPenSelectionContents(Context context, View topMostView) {
        PenSelectionContents contents = new PenSelectionContents();
        getPenSelectionContents(context, topMostView, contents);
        return contents.mContentStr;
    }

    public boolean clearAllPenSelection(Context context, View topMostView) {
        View view = topMostView;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.hasMultiSelection()) {
                textView.clearMultiSelection();
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                clearAllPenSelection(context, vg.getChildAt(i));
            }
        }
        return true;
    }

    public boolean isPenSelectionArea(Context context, View topMostView, int x, int y) {
        View view = topMostView;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.hasMultiSelection() && textView.isMultiSelectionLinkArea(x, y)) {
                return true;
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (isPenSelectionArea(context, vg.getChildAt(i), x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public View findTargetTextView(Context context, View topMostView, Rect rect) {
        View view = topMostView;
        if (!checkRectInView(view, rect)) {
            return null;
        }
        if (!(view instanceof ViewGroup)) {
            return view;
        }
        ViewGroup vg = (ViewGroup) view;
        View view2 = null;
        int childCount = vg.getChildCount();
        if (childCount == 0) {
            if (view instanceof WebView) {
                return view;
            }
            Drawable background = vg.getBackground();
            if (background != null && background.isVisible() && background.getOpacity() > -2) {
                return view;
            }
        }
        for (int i = childCount - 1; i >= 0; i--) {
            view2 = findTargetTextView(context, vg.getChildAt(i), rect);
            if (view2 != null) {
                break;
            }
        }
        return view2;
    }

    public boolean checkRectInView(View view, Rect rectSrc) {
        if (view.getVisibility() != 0) {
            return false;
        }
        int[] screenOffsetOfView = new int[2];
        view.getLocationOnScreen(screenOffsetOfView);
        return new Rect(screenOffsetOfView[0], screenOffsetOfView[1], screenOffsetOfView[0] + view.getWidth(), screenOffsetOfView[1] + view.getHeight()).contains(rectSrc);
    }
}
