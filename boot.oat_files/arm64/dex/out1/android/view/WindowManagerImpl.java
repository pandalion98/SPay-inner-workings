package android.view;

import android.os.IBinder;
import android.view.ViewGroup.LayoutParams;

public final class WindowManagerImpl implements WindowManager {
    private IBinder mDefaultToken;
    private Display mDisplay;
    private final WindowManagerGlobal mGlobal;
    private final Window mParentWindow;

    public WindowManagerImpl(Display display) {
        this(display, null);
    }

    private WindowManagerImpl(Display display, Window parentWindow) {
        this.mGlobal = WindowManagerGlobal.getInstance();
        this.mDisplay = display;
        this.mParentWindow = parentWindow;
    }

    public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
        return new WindowManagerImpl(this.mDisplay, parentWindow);
    }

    public WindowManagerImpl createPresentationWindowManager(Display display) {
        return new WindowManagerImpl(display, this.mParentWindow);
    }

    public void setDefaultToken(IBinder token) {
        this.mDefaultToken = token;
    }

    public void addView(View view, LayoutParams params) {
        applyDefaultToken(params);
        this.mGlobal.addView(view, params, this.mDisplay, this.mParentWindow);
    }

    public void updateViewLayout(View view, LayoutParams params) {
        applyDefaultToken(params);
        this.mGlobal.updateViewLayout(view, params);
    }

    private void applyDefaultToken(LayoutParams params) {
        if (this.mDefaultToken != null && this.mParentWindow == null) {
            if (params instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams wparams = (WindowManager.LayoutParams) params;
                if (wparams.token == null) {
                    wparams.token = this.mDefaultToken;
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    public void removeView(View view) {
        this.mGlobal.removeView(view, false);
    }

    public void removeViewImmediate(View view) {
        this.mGlobal.removeView(view, true);
    }

    public Display getDefaultDisplay() {
        return this.mDisplay;
    }

    public void setDisplay(Display display) {
        if (display != null) {
            this.mDisplay = display;
        }
    }

    public String toString() {
        return super.toString();
    }
}
