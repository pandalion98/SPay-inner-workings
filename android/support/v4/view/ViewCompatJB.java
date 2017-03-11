package android.support.v4.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

class ViewCompatJB {
    ViewCompatJB() {
    }

    public static boolean hasTransientState(View view) {
        return view.hasTransientState();
    }

    public static void setHasTransientState(View view, boolean z) {
        view.setHasTransientState(z);
    }

    public static void postInvalidateOnAnimation(View view) {
        view.postInvalidateOnAnimation();
    }

    public static void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4) {
        view.postInvalidate(i, i2, i3, i4);
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }

    public static void postOnAnimationDelayed(View view, Runnable runnable, long j) {
        view.postOnAnimationDelayed(runnable, j);
    }

    public static int getImportantForAccessibility(View view) {
        return view.getImportantForAccessibility();
    }

    public static void setImportantForAccessibility(View view, int i) {
        view.setImportantForAccessibility(i);
    }

    public static boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        return view.performAccessibilityAction(i, bundle);
    }

    public static Object getAccessibilityNodeProvider(View view) {
        return view.getAccessibilityNodeProvider();
    }

    public static ViewParent getParentForAccessibility(View view) {
        return view.getParentForAccessibility();
    }
}
