package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Window.Callback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.policy.PhoneWindow;

public class SallyService extends Service implements Callback, KeyEvent.Callback {
    private static final String TAG = "Sally Service";
    protected Context mContext;
    private View mDecor;
    private Window mWindow;
    private LayoutParams mWindowAttributes;
    protected WindowManager mWindowManager;

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() : " + this);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.mContext = this;
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager != null) {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(getPackageName(), 0);
                if (appInfo != null) {
                    this.mContext = new ContextThemeWrapper(this, appInfo.theme);
                    Log.i(TAG, "loaded theme = " + appInfo.theme);
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.mWindowAttributes = createLayoutParams();
        this.mWindow = new PhoneWindow(this.mContext);
        if (this.mWindow != null) {
            this.mWindow.setWindowManager(this.mWindowManager, null, null);
            this.mWindowManager = this.mWindow.getWindowManager();
            this.mWindow.requestFeature(1);
            this.mWindow.setCallback(this);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand()");
        if (this.mWindow != null) {
            this.mWindow.setAttributes(this.mWindowAttributes);
        }
        this.mDecor = this.mWindow.getDecorView();
        if (this.mDecor != null) {
            this.mDecor.setVisibility(0);
            LayoutParams l = this.mWindow.getAttributes();
            if ((l.softInputMode & 256) == 0) {
                LayoutParams nl = new LayoutParams();
                nl.copyFrom(l);
                nl.softInputMode |= 256;
                l = nl;
            }
            try {
                this.mWindowManager.addView(this.mDecor, l);
            } catch (Exception e) {
                e.printStackTrace();
                stopSelf();
            }
        }
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mDecor != null && this.mDecor.isAttachedToWindow()) {
            this.mWindowManager.removeView(this.mDecor);
        }
        Log.i(TAG, "onDestroy() : " + this);
        this.mContext = null;
        this.mDecor = null;
    }

    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind() : " + this);
        return null;
    }

    public void stopService() {
        if (this.mDecor != null) {
            this.mWindowManager.removeView(this.mDecor);
            this.mDecor = null;
        }
        stopForeground(true);
        stopSelf();
    }

    public final Context getContext() {
        return this.mContext;
    }

    public void setContentView(int layoutResID) {
        Log.i(TAG, "setContentView() layoutResID");
        this.mWindow.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        Log.i(TAG, "setContentView() view");
        this.mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "setContentView() view, params");
        this.mWindow.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "addContentView() view, params");
        this.mWindow.addContentView(view, params);
    }

    public View findViewById(int id) {
        Log.i(TAG, "findViewById()");
        return this.mWindow.findViewById(id);
    }

    public LayoutParams getAttributes() {
        Log.i(TAG, "getAttributes()");
        return this.mWindowAttributes;
    }

    public void setAttributes(LayoutParams a) {
        Log.i(TAG, "setAttributes()");
        this.mWindow.setAttributes(a);
    }

    public LayoutParams createLayoutParams() {
        Log.i(TAG, "createLayoutParams");
        LayoutParams lp = new LayoutParams(2002, 17040192, -3);
        lp.privateFlags |= 16;
        lp.softInputMode = 32;
        lp.setTitle(getClass().getName());
        return lp;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public Window addWindow(View view, int width, int height, int windowType) {
        return addWindow(view, width, height, windowType, 0);
    }

    public Window addWindow(View view, int width, int height, int windowType, int windowFlags) {
        Log.i(TAG, "addWindow view");
        boolean toGo = false;
        IllegalArgumentException exception = null;
        LayoutParams newWindowAttributes = createLayoutParams();
        if (this.mContext == null) {
            return null;
        }
        Window newWindow = new PhoneWindow(this.mContext);
        newWindow.requestFeature(1);
        newWindow.setWindowManager(this.mWindowManager, null, null);
        newWindowAttributes.width = width;
        newWindowAttributes.height = height;
        newWindowAttributes.type = windowType;
        newWindowAttributes.flags |= windowFlags;
        newWindow.setAttributes(newWindowAttributes);
        newWindow.setContentView(view);
        View newDecor = newWindow.getDecorView();
        if (newDecor != null) {
            newDecor.setVisibility(0);
            do {
                try {
                    this.mWindowManager.addView(newDecor, newWindowAttributes);
                    continue;
                } catch (IllegalArgumentException e) {
                    if (toGo) {
                        exception = e;
                        break;
                    }
                    Log.e(TAG, "View Problem: " + view.toString() + "w: " + width + "h: " + height + "t: " + windowType);
                    toGo = true;
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException e2) {
                        continue;
                    }
                }
            } while (toGo);
        }
        if (exception == null) {
            return newWindow;
        }
        throw exception;
    }

    public boolean removeWindow(Window window) {
        Log.i(TAG, "removeWindow window");
        if (window == null) {
            return false;
        }
        this.mWindowManager.removeView(window.getDecorView());
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        stopService();
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mWindow.superDispatchKeyEvent(event)) {
            return true;
        }
        return event.dispatch(this, this.mDecor != null ? this.mDecor.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (this.mWindow.superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this.mWindow.superDispatchTouchEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (this.mWindow.superDispatchTrackballEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (this.mWindow.superDispatchGenericMotionEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        return false;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public void onWindowAttributesChanged(LayoutParams attrs) {
    }

    public void onContentChanged() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public void onPanelClosed(int featureId, Menu menu) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        return false;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return null;
    }

    public void onActionModeStarted(ActionMode mode) {
    }

    public void onActionModeFinished(ActionMode mode) {
    }
}
