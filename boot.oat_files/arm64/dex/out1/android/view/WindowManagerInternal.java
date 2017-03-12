package android.view;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.IBinder;
import android.view.WindowManagerPolicy.WindowState;
import android.view.animation.Animation;
import java.util.List;

public abstract class WindowManagerInternal {

    public static abstract class AppTransitionListener {
        public void onAppTransitionPendingLocked() {
        }

        public void onAppTransitionCancelledLocked() {
        }

        public void onAppTransitionStartingLocked(IBinder openToken, IBinder closeToken, Animation openAnimation, Animation closeAnimation) {
        }

        public void onAppTransitionFinishedLocked(IBinder token) {
        }
    }

    public interface MagnificationCallbacks {
        void onMagnifedBoundsChanged(Region region);

        void onRectangleOnScreenRequested(int i, int i2, int i3, int i4);

        void onRotationChanged(int i);

        void onUserContextChanged();
    }

    public interface WindowsForAccessibilityCallback {
        void onWindowsForAccessibilityChanged(List<WindowInfo> list);
    }

    public abstract void addWindowToken(IBinder iBinder, int i);

    public abstract MagnificationSpec getCompatibleMagnificationSpecForWindow(IBinder iBinder);

    public abstract IBinder getFocusedAppToken();

    public abstract IBinder getFocusedWindowToken();

    public abstract WindowState getInputMethodTarget();

    public abstract int getStackId(IBinder iBinder);

    public abstract void getWindowFrame(IBinder iBinder, Rect rect);

    public abstract boolean isCascade(IBinder iBinder);

    public abstract boolean isGrantPermissionAppToken(IBinder iBinder);

    public abstract boolean isKeyguardLocked();

    public abstract void registerAppTransitionListener(AppTransitionListener appTransitionListener);

    public abstract void removeWindowToken(IBinder iBinder, boolean z);

    public abstract void requestTraversalFromDisplayManager();

    public abstract void setInputFilter(IInputFilter iInputFilter);

    public abstract void setMagnificationCallbacks(MagnificationCallbacks magnificationCallbacks, int i);

    public abstract void setMagnificationSpec(MagnificationSpec magnificationSpec, int i);

    public abstract void setScreenRotationAnimation(int i, int i2);

    public abstract void setTouchExplorationEnabled(boolean z);

    public abstract void setWindowsForAccessibilityCallback(WindowsForAccessibilityCallback windowsForAccessibilityCallback, int i);

    public abstract void showGlobalActions();

    public abstract void showStatusBarByNotification();

    public abstract void waitForAllWindowsDrawn(Runnable runnable, long j, int i);
}
