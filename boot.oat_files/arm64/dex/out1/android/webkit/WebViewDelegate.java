package android.webkit;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.SparseArray;
import android.view.DisplayListCanvas;
import android.view.View;
import android.view.ViewRootImpl;

public final class WebViewDelegate {

    public interface OnTraceEnabledChangeListener {
        void onTraceEnabledChange(boolean z);
    }

    WebViewDelegate() {
    }

    public void setOnTraceEnabledChangeListener(final OnTraceEnabledChangeListener listener) {
        SystemProperties.addChangeCallback(new Runnable() {
            public void run() {
                listener.onTraceEnabledChange(WebViewDelegate.this.isTraceTagEnabled());
            }
        });
    }

    public boolean isTraceTagEnabled() {
        return Trace.isTagEnabled(16);
    }

    public boolean canInvokeDrawGlFunctor(View containerView) {
        return containerView.getViewRootImpl() != null;
    }

    public void invokeDrawGlFunctor(View containerView, long nativeDrawGLFunctor, boolean waitForCompletion) {
        containerView.getViewRootImpl().invokeFunctor(nativeDrawGLFunctor, waitForCompletion);
    }

    public void callDrawGlFunction(Canvas canvas, long nativeDrawGLFunctor) {
        if (canvas instanceof DisplayListCanvas) {
            ((DisplayListCanvas) canvas).callDrawGLFunction2(nativeDrawGLFunctor);
            return;
        }
        throw new IllegalArgumentException(canvas.getClass().getName() + " is not a DisplayList canvas");
    }

    public void detachDrawGlFunctor(View containerView, long nativeDrawGLFunctor) {
        ViewRootImpl viewRootImpl = containerView.getViewRootImpl();
        if (nativeDrawGLFunctor != 0 && viewRootImpl != null) {
            viewRootImpl.detachFunctor(nativeDrawGLFunctor);
        }
    }

    public int getPackageId(Resources resources, String packageName) {
        SparseArray<String> packageIdentifiers = resources.getAssets().getAssignedPackageIdentifiers();
        for (int i = 0; i < packageIdentifiers.size(); i++) {
            if (packageName.equals((String) packageIdentifiers.valueAt(i))) {
                return packageIdentifiers.keyAt(i);
            }
        }
        throw new RuntimeException("Package not found: " + packageName);
    }

    public Application getApplication() {
        return ActivityThread.currentApplication();
    }

    public String getErrorString(Context context, int errorCode) {
        return LegacyErrorStrings.getString(errorCode, context);
    }

    public void addWebViewAssetPath(Context context) {
        context.getAssets().addAssetPath(WebViewFactory.getLoadedPackageInfo().applicationInfo.sourceDir);
    }
}
