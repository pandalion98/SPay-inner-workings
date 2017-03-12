package android.view;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.FastPrintWriter;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public final class WindowManagerGlobal {
    public static final int ADD_APP_EXITING = -4;
    public static final int ADD_BAD_APP_TOKEN = -1;
    public static final int ADD_BAD_SUBWINDOW_TOKEN = -2;
    public static final int ADD_BLOCK_BY_MIRROR_LINK = -100;
    public static final int ADD_DUPLICATE_ADD = -5;
    public static final int ADD_FLAG_APP_VISIBLE = 2;
    public static final int ADD_FLAG_IN_TOUCH_MODE = 1;
    public static final int ADD_INVALID_DISPLAY = -9;
    public static final int ADD_INVALID_TYPE = -10;
    public static final int ADD_MULTIPLE_SINGLETON = -7;
    public static final int ADD_NOT_APP_TOKEN = -3;
    public static final int ADD_OKAY = 0;
    public static final int ADD_PERMISSION_DENIED = -8;
    public static final int ADD_STARTING_NOT_NEEDED = -6;
    public static final int RELAYOUT_DEFER_SURFACE_DESTROY = 2;
    public static final int RELAYOUT_INSETS_PENDING = 1;
    public static final int RELAYOUT_RES_FIRST_TIME = 2;
    public static final int RELAYOUT_RES_IN_TOUCH_MODE = 1;
    public static final int RELAYOUT_RES_SURFACE_CHANGED = 4;
    private static final boolean SAFE_DEBUG;
    private static final String TAG = "WindowManager";
    private static WindowManagerGlobal sDefaultWindowManager;
    private static IWindowManager sWindowManagerService;
    private static IWindowSession sWindowSession;
    private final ArraySet<View> mDyingViews = new ArraySet();
    private final Object mLock = new Object();
    private final ArrayList<LayoutParams> mParams = new ArrayList();
    private final ArrayList<ViewRootImpl> mRoots = new ArrayList();
    private Runnable mSystemPropertyUpdater;
    private final ArrayList<View> mViews = new ArrayList();

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        SAFE_DEBUG = z;
    }

    private WindowManagerGlobal() {
    }

    public static void initialize() {
        getWindowManagerService();
    }

    public static WindowManagerGlobal getInstance() {
        WindowManagerGlobal windowManagerGlobal;
        synchronized (WindowManagerGlobal.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerGlobal();
            }
            windowManagerGlobal = sDefaultWindowManager;
        }
        return windowManagerGlobal;
    }

    public static IWindowManager getWindowManagerService() {
        IWindowManager iWindowManager;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowManagerService == null) {
                sWindowManagerService = Stub.asInterface(ServiceManager.getService("window"));
                try {
                    sWindowManagerService = getWindowManagerService();
                    ValueAnimator.setDurationScale(sWindowManagerService.getCurrentAnimatorScale());
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to get WindowManagerService, cannot set animator scale", e);
                }
            }
            iWindowManager = sWindowManagerService;
        }
        return iWindowManager;
    }

    public static IWindowSession getWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowSession == null) {
                try {
                    InputMethodManager imm = InputMethodManager.getInstance();
                    sWindowSession = getWindowManagerService().openSession(new IWindowSessionCallback.Stub() {
                        public void onAnimatorScaleChanged(float scale) {
                            ValueAnimator.setDurationScale(scale);
                        }
                    }, imm.getClient(), imm.getInputContext());
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to open window session", e);
                }
            }
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    public static IWindowSession peekWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    public String[] getViewRootNames() {
        String[] mViewRoots;
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            mViewRoots = new String[numRoots];
            for (int i = 0; i < numRoots; i++) {
                mViewRoots[i] = getWindowName((ViewRootImpl) this.mRoots.get(i));
            }
        }
        return mViewRoots;
    }

    public ArrayList<ViewRootImpl> getRootViews(IBinder token) {
        ArrayList<ViewRootImpl> views = new ArrayList();
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            for (int i = 0; i < numRoots; i++) {
                LayoutParams params = (LayoutParams) this.mParams.get(i);
                if (params.token != null) {
                    if (params.token != token) {
                        boolean isChild = false;
                        if (params.type >= 1000 && params.type <= LayoutParams.LAST_SUB_WINDOW) {
                            for (int j = 0; j < numRoots; j++) {
                                LayoutParams paramsj = (LayoutParams) this.mParams.get(j);
                                if (params.token == ((View) this.mViews.get(j)).getWindowToken() && paramsj.token == token) {
                                    isChild = true;
                                    break;
                                }
                            }
                        }
                        if (!isChild) {
                        }
                    }
                    views.add(this.mRoots.get(i));
                }
            }
        }
        return views;
    }

    public View getRootView(String name) {
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                if (name.equals(getWindowName(root))) {
                    View view = root.getView();
                    return view;
                }
            }
            return null;
        }
    }

    public void addView(View view, ViewGroup.LayoutParams params, Display display, Window parentWindow) {
        int index;
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        } else if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        } else if (params instanceof LayoutParams) {
            ViewRootImpl root;
            LayoutParams wparams = (LayoutParams) params;
            if (parentWindow != null) {
                parentWindow.adjustLayoutParamsForSubWindow(wparams);
            } else {
                Context context = view.getContext();
                if (!(context == null || (context.getApplicationInfo().flags & 536870912) == 0)) {
                    wparams.flags |= 16777216;
                }
            }
            View panelParentView = null;
            synchronized (this.mLock) {
                if (this.mSystemPropertyUpdater == null) {
                    this.mSystemPropertyUpdater = new Runnable() {
                        public void run() {
                            synchronized (WindowManagerGlobal.this.mLock) {
                                for (int i = WindowManagerGlobal.this.mRoots.size() - 1; i >= 0; i--) {
                                    ((ViewRootImpl) WindowManagerGlobal.this.mRoots.get(i)).loadSystemProperties();
                                }
                            }
                        }
                    };
                    SystemProperties.addChangeCallback(this.mSystemPropertyUpdater);
                }
                index = findViewLocked(view, false);
                if (index >= 0) {
                    if (this.mDyingViews.contains(view)) {
                        ((ViewRootImpl) this.mRoots.get(index)).doDie();
                    } else {
                        throw new IllegalStateException("View " + view + " has already been added to the window manager.");
                    }
                }
                if (wparams.type >= 1000 && wparams.type <= LayoutParams.LAST_SUB_WINDOW) {
                    int miniDialogParentIndex = -1;
                    int count = this.mViews.size();
                    int i = 0;
                    while (i < count) {
                        if (((ViewRootImpl) this.mRoots.get(i)).mWindow.asBinder() == wparams.token) {
                            panelParentView = (View) this.mViews.get(i);
                        } else if (((LayoutParams) this.mParams.get(i)).type == LayoutParams.TYPE_MINI_APP_DIALOG || ((LayoutParams) this.mParams.get(i)).type == LayoutParams.TYPE_MINI_APP_ON_KEYGUARD_DIALOG) {
                            miniDialogParentIndex = i;
                        }
                        i++;
                    }
                    if (panelParentView == null && miniDialogParentIndex != -1) {
                        wparams.token = ((ViewRootImpl) this.mRoots.get(miniDialogParentIndex)).mWindow.asBinder();
                        panelParentView = (View) this.mViews.get(miniDialogParentIndex);
                    }
                }
                root = new ViewRootImpl(view.getContext(), display);
                view.setLayoutParams(wparams);
                this.mViews.add(view);
                this.mRoots.add(root);
                this.mParams.add(wparams);
            }
            try {
                root.setView(view, wparams, panelParentView);
            } catch (RuntimeException e) {
                synchronized (this.mLock) {
                    index = findViewLocked(view, false);
                    if (index >= 0) {
                        removeViewLocked(index, true);
                    }
                    throw e;
                }
            }
        } else {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        } else if (params instanceof LayoutParams) {
            LayoutParams wparams = (LayoutParams) params;
            view.setLayoutParams(wparams);
            synchronized (this.mLock) {
                int index = findViewLocked(view, true);
                ViewRootImpl root = (ViewRootImpl) this.mRoots.get(index);
                this.mParams.remove(index);
                this.mParams.add(index, wparams);
                root.setLayoutParams(wparams, false);
            }
        } else {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    public void removeView(View view, boolean immediate) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        synchronized (this.mLock) {
            int index = findViewLocked(view, true);
            View curView = ((ViewRootImpl) this.mRoots.get(index)).getView();
            removeViewLocked(index, immediate);
            if (curView == view) {
            } else {
                throw new IllegalStateException("Calling with view " + view + " but the ViewAncestor is attached to " + curView);
            }
        }
    }

    public void closeAll(IBinder token, String who, String what) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            int i = 0;
            while (i < count) {
                if (token == null || ((LayoutParams) this.mParams.get(i)).token == token) {
                    ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                    if (who != null) {
                        WindowLeaked leak = new WindowLeaked(what + " " + who + " has leaked window " + root.getView() + " that was originally added here");
                        leak.setStackTrace(root.getLocation().getStackTrace());
                        Log.e(TAG, "", leak);
                    }
                    removeViewLocked(i, false);
                }
                i++;
            }
        }
    }

    private void removeViewLocked(int index, boolean immediate) {
        ViewRootImpl root = (ViewRootImpl) this.mRoots.get(index);
        View view = root.getView();
        if (view != null) {
            InputMethodManager imm = InputMethodManager.getInstance();
            if (imm != null) {
                imm.windowDismissed(((View) this.mViews.get(index)).getWindowToken());
            }
        }
        boolean deferred = root.die(immediate);
        if (view != null) {
            view.assignParent(null);
            if (deferred) {
                this.mDyingViews.add(view);
            }
        }
    }

    void doRemoveView(ViewRootImpl root) {
        synchronized (this.mLock) {
            int index = this.mRoots.indexOf(root);
            if (index >= 0) {
                this.mRoots.remove(index);
                this.mParams.remove(index);
                this.mDyingViews.remove((View) this.mViews.remove(index));
            }
        }
        if (HardwareRenderer.sTrimForeground && HardwareRenderer.isAvailable()) {
            doTrimForeground();
        }
    }

    private int findViewLocked(View view, boolean required) {
        int index = this.mViews.indexOf(view);
        if (!required || index >= 0) {
            return index;
        }
        throw new IllegalArgumentException("View=" + view + " not attached to window manager");
    }

    public static boolean shouldDestroyEglContext(int trimLevel) {
        if (trimLevel >= 80) {
            return true;
        }
        if (trimLevel < 60 || ActivityManager.isHighEndGfx()) {
            return false;
        }
        return true;
    }

    public void trimMemory(int level) {
        if (HardwareRenderer.isAvailable()) {
            if (shouldDestroyEglContext(level)) {
                synchronized (this.mLock) {
                    for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                        ((ViewRootImpl) this.mRoots.get(i)).destroyHardwareResources();
                    }
                }
                level = 80;
            }
            HardwareRenderer.trimMemory(level);
            if (HardwareRenderer.sTrimForeground) {
                doTrimForeground();
            }
        }
    }

    public static void trimForeground() {
        if (HardwareRenderer.sTrimForeground && HardwareRenderer.isAvailable()) {
            getInstance().doTrimForeground();
        }
    }

    private void doTrimForeground() {
        boolean hasVisibleWindows = false;
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                if (root.mView == null || root.getHostVisibility() != 0 || root.mAttachInfo.mHardwareRenderer == null) {
                    root.destroyHardwareResources();
                } else {
                    hasVisibleWindows = true;
                }
            }
        }
        if (!hasVisibleWindows) {
            HardwareRenderer.trimMemory(80);
        }
    }

    public void dumpGfxInfo(FileDescriptor fd, String[] args) {
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
        try {
            synchronized (this.mLock) {
                int i;
                int count = this.mViews.size();
                pw.println("Profile data in ms:");
                for (i = 0; i < count; i++) {
                    pw.printf("\n\t%s (visibility=%d)", new Object[]{getWindowName(root), Integer.valueOf(((ViewRootImpl) this.mRoots.get(i)).getHostVisibility())});
                    HardwareRenderer renderer = root.getView().mAttachInfo.mHardwareRenderer;
                    if (renderer != null) {
                        renderer.dumpGfxInfo(pw, fd, args);
                    }
                }
                pw.println("\nView hierarchy:\n");
                int viewsCount = 0;
                int displayListsSize = 0;
                int[] info = new int[2];
                for (i = 0; i < count; i++) {
                    ((ViewRootImpl) this.mRoots.get(i)).dumpGfxInfo(info);
                    pw.printf("  %s\n  %d views, %.2f kB of display lists", new Object[]{getWindowName(root), Integer.valueOf(info[0]), Float.valueOf(((float) info[1]) / 1024.0f)});
                    pw.printf("\n\n", new Object[0]);
                    viewsCount += info[0];
                    displayListsSize += info[1];
                }
                pw.printf("\nTotal ViewRootImpl: %d\n", new Object[]{Integer.valueOf(count)});
                pw.printf("Total Views:        %d\n", new Object[]{Integer.valueOf(viewsCount)});
                pw.printf("Total DisplayList:  %.2f kB\n\n", new Object[]{Float.valueOf(((float) displayListsSize) / 1024.0f)});
            }
            pw.flush();
        } catch (Throwable th) {
            pw.flush();
        }
    }

    private static String getWindowName(ViewRootImpl root) {
        return root.mWindowAttributes.getTitle() + "/" + root.getClass().getName() + '@' + Integer.toHexString(root.hashCode());
    }

    public void setStoppedState(IBinder token, boolean stopped) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            int i = 0;
            while (i < count) {
                if (token == null || ((LayoutParams) this.mParams.get(i)).token == token) {
                    ((ViewRootImpl) this.mRoots.get(i)).setWindowStopped(stopped);
                }
                i++;
            }
        }
    }

    public void reportNewConfiguration(Configuration config) {
        Throwable th;
        synchronized (this.mLock) {
            try {
                int count = this.mViews.size();
                Configuration config2 = new Configuration(config);
                int i = 0;
                while (i < count) {
                    try {
                        ((ViewRootImpl) this.mRoots.get(i)).requestUpdateConfiguration(config2);
                        i++;
                    } catch (Throwable th2) {
                        th = th2;
                        config = config2;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void changeCanvasOpacity(IBinder token, boolean opaque) {
        if (token != null) {
            synchronized (this.mLock) {
                for (int i = this.mParams.size() - 1; i >= 0; i--) {
                    if (((LayoutParams) this.mParams.get(i)).token == token) {
                        ((ViewRootImpl) this.mRoots.get(i)).changeCanvasOpacity(opaque);
                        return;
                    }
                }
            }
        }
    }

    public void setMultiWindowScale(float hScale, float vScale) {
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ((ViewRootImpl) this.mRoots.get(i)).setMultiWindowScale(hScale, vScale);
            }
        }
    }

    public void setResources(IBinder token, Resources newResources) {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            synchronized (this.mLock) {
                if (token == null || newResources == null) {
                    return;
                }
                Iterator i$ = getRootViews(token).iterator();
                while (i$.hasNext()) {
                    ViewRootImpl rootView = (ViewRootImpl) i$.next();
                    if (!(rootView.getView() == null || rootView.getView().getContext() == null)) {
                        rootView.getView().getContext().setResources(newResources);
                        if (SAFE_DEBUG) {
                            Log.i(TAG, "setResources, v=" + rootView + ", r=" + newResources + ", param=" + rootView.getView().getLayoutParams());
                        }
                    }
                }
            }
        }
    }
}
