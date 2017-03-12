package android.app;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.OnWindowDismissedCallback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.PhoneWindow;
import java.lang.ref.WeakReference;

public class Dialog implements DialogInterface, Callback, KeyEvent.Callback, OnCreateContextMenuListener, OnWindowDismissedCallback {
    private static final int CANCEL = 68;
    private static final boolean DEBUG = false;
    private static final String DIALOG_HIERARCHY_TAG = "android:dialogHierarchy";
    private static final String DIALOG_SHOWING_TAG = "android:dialogShowing";
    private static final int DISMISS = 67;
    private static final int SHOW = 69;
    private static final String TAG = "Dialog";
    private ActionBar mActionBar;
    private ActionMode mActionMode;
    private int mActionModeTypeStarting;
    private String mCancelAndDismissTaken;
    private Message mCancelMessage;
    protected boolean mCancelable;
    private boolean mCanceled;
    final Context mContext;
    private boolean mCreated;
    View mDecor;
    private final Runnable mDismissAction;
    private Message mDismissMessage;
    private final Handler mHandler;
    private boolean mHasFocus;
    private Handler mListenersHandler;
    private OnKeyListener mOnKeyListener;
    private Activity mOwnerActivity;
    private SearchEvent mSearchEvent;
    private Message mShowMessage;
    private boolean mShowing;
    Window mWindow;
    final WindowManager mWindowManager;

    private static final class ListenersHandler extends Handler {
        private WeakReference<DialogInterface> mDialog;

        public ListenersHandler(Dialog dialog) {
            this.mDialog = new WeakReference(dialog);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 67:
                    ((OnDismissListener) msg.obj).onDismiss((DialogInterface) this.mDialog.get());
                    return;
                case 68:
                    ((OnCancelListener) msg.obj).onCancel((DialogInterface) this.mDialog.get());
                    return;
                case 69:
                    ((OnShowListener) msg.obj).onShow((DialogInterface) this.mDialog.get());
                    return;
                default:
                    return;
            }
        }
    }

    public Dialog(Context context) {
        this(context, 0, true);
    }

    public Dialog(Context context, int themeResId) {
        this(context, themeResId, true);
    }

    Dialog(Context context, int themeResId, boolean createContextThemeWrapper) {
        this.mCancelable = true;
        this.mCreated = false;
        this.mShowing = false;
        this.mCanceled = false;
        this.mHandler = new Handler();
        this.mActionModeTypeStarting = 0;
        this.mHasFocus = false;
        this.mDismissAction = new Runnable() {
            public void run() {
                Dialog.this.dismissDialog();
            }
        };
        if (createContextThemeWrapper) {
            if (themeResId == 0) {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.dialogTheme, outValue, true);
                themeResId = outValue.resourceId;
            }
            this.mContext = new ContextThemeWrapper(context, themeResId);
        } else {
            this.mContext = context;
        }
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window w = new PhoneWindow(this.mContext);
        this.mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setWindowManager(this.mWindowManager, null, null);
        w.setGravity(17);
        this.mListenersHandler = new ListenersHandler(this);
    }

    @Deprecated
    protected Dialog(Context context, boolean cancelable, Message cancelCallback) {
        this(context);
        this.mCancelable = cancelable;
        this.mCancelMessage = cancelCallback;
    }

    protected Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context);
        this.mCancelable = cancelable;
        setOnCancelListener(cancelListener);
    }

    public final Context getContext() {
        return this.mContext;
    }

    public ActionBar getActionBar() {
        return this.mActionBar;
    }

    public final void setOwnerActivity(Activity activity) {
        this.mOwnerActivity = activity;
        getWindow().setVolumeControlStream(this.mOwnerActivity.getVolumeControlStream());
    }

    public final Activity getOwnerActivity() {
        return this.mOwnerActivity;
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void create() {
        if (!this.mCreated) {
            dispatchOnCreate(null);
        }
    }

    public void show() {
        if (!checkShowingCondition()) {
            return;
        }
        if (!this.mShowing) {
            this.mCanceled = false;
            if (!this.mCreated) {
                dispatchOnCreate(null);
            }
            onStart();
            if ((this instanceof ProgressDialog) && ((ProgressDialog) this).getCurrentProgressStyle() == 1000) {
                LayoutParams lp = this.mWindow.getAttributes();
                int dialogBaseSize = this.mContext.getResources().getDimensionPixelSize(17105252);
                lp.height = dialogBaseSize;
                lp.width = dialogBaseSize;
                this.mWindow.setAttributes(lp);
            }
            this.mDecor = this.mWindow.getDecorView();
            if (this.mActionBar == null && this.mWindow.hasFeature(8)) {
                ApplicationInfo info = this.mContext.getApplicationInfo();
                this.mWindow.setDefaultIcon(info.icon);
                this.mWindow.setDefaultLogo(info.logo);
                this.mActionBar = new WindowDecorActionBar(this);
            }
            LayoutParams l = this.mWindow.getAttributes();
            if ((l.softInputMode & 256) == 0) {
                LayoutParams nl = new LayoutParams();
                nl.copyFrom(l);
                nl.softInputMode |= 256;
                l = nl;
            }
            this.mWindowManager.addView(this.mDecor, l);
            this.mShowing = true;
            sendShowMessage();
        } else if (this.mDecor != null) {
            if (this.mWindow.hasFeature(8)) {
                this.mWindow.invalidatePanelMenu(8);
            }
            this.mDecor.setVisibility(0);
        }
    }

    public void ExpandedShow(boolean isMainScreen) {
        Log.e(TAG, "call ExpandedShow");
        Log.e(TAG, "isMainScreen : " + isMainScreen);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);
        if (!checkShowingCondition()) {
            return;
        }
        if (this.mShowing) {
            if (this.mDecor != null) {
                if (this.mWindow.hasFeature(8)) {
                    this.mWindow.invalidatePanelMenu(8);
                }
                this.mDecor.setVisibility(0);
            }
            LayoutParams l = this.mWindow.getAttributes();
            Log.e(TAG, "mShowing is true");
            Log.e(TAG, "l.x  is " + l.x);
            l.x = isMainScreen ? pt.x / 2 : (-pt.x) / 2;
            this.mWindow.setAttributes(l);
            Log.e(TAG, "setAttributes " + l.x);
            return;
        }
        this.mCanceled = false;
        if (!this.mCreated) {
            dispatchOnCreate(null);
        }
        onStart();
        this.mDecor = this.mWindow.getDecorView();
        if (this.mActionBar == null && this.mWindow.hasFeature(8)) {
            ApplicationInfo info = this.mContext.getApplicationInfo();
            this.mWindow.setDefaultIcon(info.icon);
            this.mWindow.setDefaultLogo(info.logo);
            this.mActionBar = new WindowDecorActionBar(this);
        }
        l = this.mWindow.getAttributes();
        if ((l.softInputMode & 256) == 0) {
            LayoutParams nl = new LayoutParams();
            nl.copyFrom(l);
            nl.softInputMode |= 256;
            l = nl;
        }
        l.x = isMainScreen ? pt.x / 2 : (-pt.x) / 2;
        Log.e(TAG, "l.x is " + l.x);
        this.mWindowManager.addView(this.mDecor, l);
        this.mWindow.setAttributes(l);
        this.mShowing = true;
        sendShowMessage();
    }

    private boolean checkShowingCondition() {
        if (!checkMirrorLinkEnabled() || checkIME()) {
            return true;
        }
        return checkWhiteList();
    }

    private boolean checkMirrorLinkEnabled() {
        if (SystemProperties.get("net.mirrorlink.on").equals(WifiEnterpriseConfig.ENGINE_ENABLE)) {
            return true;
        }
        return false;
    }

    private boolean checkIME() {
        Window window = getWindow();
        if (window == null) {
            return false;
        }
        switch (window.getAttributes().type) {
            case 2011:
            case 2012:
                return true;
            default:
                return false;
        }
    }

    private boolean checkWhiteList() {
        String BASE_PATH_PKGNAME = "pkgname";
        Uri CONTENT_URI_PKGNAMES = Uri.parse("content://" + "com.samsung.mirrorlink.acms.pkgnames" + "/" + BASE_PATH_PKGNAME);
        int res = this.mContext.checkCallingOrSelfPermission("com.mirrorlink.android.service.ACCESS_PERMISSION");
        Log.d(TAG, "Check Access Permission  : res = " + res);
        if (res != 0) {
            return false;
        }
        String currentPackageName = this.mContext.getPackageName();
        Cursor cursor = this.mContext.getContentResolver().query(CONTENT_URI_PKGNAMES, new String[]{BASE_PATH_PKGNAME}, BASE_PATH_PKGNAME + "=?", new String[]{currentPackageName}, null);
        if (cursor == null) {
            Log.d(TAG, "Cursor is null");
            return false;
        }
        boolean z;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.d(TAG, "currentPackageName =  " + currentPackageName);
            Log.d(TAG, "db column packagename =" + cursor.getString(cursor.getColumnIndex(BASE_PATH_PKGNAME)));
            z = true;
        } else {
            z = false;
        }
        cursor.close();
        return z;
    }

    public void hide() {
        if (this.mDecor != null) {
            this.mDecor.setVisibility(8);
        }
    }

    public void dismiss() {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            dismissDialog();
        } else {
            this.mHandler.post(this.mDismissAction);
        }
    }

    void dismissDialog() {
        if (this.mDecor != null && this.mShowing) {
            if (this.mWindow.isDestroyed()) {
                Log.e(TAG, "Tried to dismissDialog() but the Dialog's window was already destroyed!");
                return;
            }
            try {
                this.mWindowManager.removeViewImmediate(this.mDecor);
            } finally {
                if (this.mActionMode != null) {
                    this.mActionMode.finish();
                }
                this.mDecor = null;
                this.mWindow.closeAllPanels();
                onStop();
                this.mShowing = false;
                sendDismissMessage();
            }
        }
    }

    private void sendDismissMessage() {
        if (this.mDismissMessage != null) {
            Message.obtain(this.mDismissMessage).sendToTarget();
        }
    }

    private void sendShowMessage() {
        if (this.mShowMessage != null) {
            Message.obtain(this.mShowMessage).sendToTarget();
        }
    }

    void dispatchOnCreate(Bundle savedInstanceState) {
        if (!this.mCreated) {
            onCreate(savedInstanceState);
            this.mCreated = true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
    }

    protected void onStart() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(true);
        }
    }

    protected void onStop() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(false);
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DIALOG_SHOWING_TAG, this.mShowing);
        if (this.mCreated) {
            bundle.putBundle(DIALOG_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Bundle dialogHierarchyState = savedInstanceState.getBundle(DIALOG_HIERARCHY_TAG);
        if (dialogHierarchyState != null) {
            dispatchOnCreate(savedInstanceState);
            this.mWindow.restoreHierarchyState(dialogHierarchyState);
            if (savedInstanceState.getBoolean(DIALOG_SHOWING_TAG)) {
                show();
            }
        }
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public View getCurrentFocus() {
        return this.mWindow != null ? this.mWindow.getCurrentFocus() : null;
    }

    public View findViewById(int id) {
        return this.mWindow.findViewById(id);
    }

    public void setContentView(int layoutResID) {
        this.mWindow.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        this.mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.addContentView(view, params);
    }

    public void setTitle(CharSequence title) {
        this.mWindow.setTitle(title);
        this.mWindow.getAttributes().setTitle(title);
    }

    public void setTitle(int titleId) {
        setTitle(this.mContext.getText(titleId));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        event.startTracking();
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !event.isTracking() || event.isCanceled()) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        if (this.mCancelable) {
            cancel();
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mCancelable || !this.mShowing || !this.mWindow.shouldCloseOnTouch(this.mContext, event)) {
            return false;
        }
        cancel();
        return true;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onWindowAttributesChanged(LayoutParams params) {
        if (this.mDecor != null) {
            this.mWindowManager.updateViewLayout(this.mDecor, params);
        }
    }

    public void onContentChanged() {
    }

    public boolean getDialogFocus() {
        return this.mHasFocus;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        this.mHasFocus = hasFocus;
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public void onWindowDismissed() {
        dismiss();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((this.mOnKeyListener != null && this.mOnKeyListener.onKey(this, event.getKeyCode(), event)) || this.mWindow.superDispatchKeyEvent(event)) {
            return true;
        }
        return event.dispatch(this, this.mDecor != null ? this.mDecor.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (this.mWindow.superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTouchEvent(ev)) {
            return true;
        }
        if (getContext().getResources().getConfiguration().mobileKeyboardCovered == 1 && ev.getAction() == 0) {
            if (ev.getRawY() > ((float) getContext().getResources().getDisplayMetrics().heightPixels)) {
                return true;
            }
        }
        return onTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        boolean isFullScreen;
        event.setClassName(getClass().getName());
        event.setPackageName(this.mContext.getPackageName());
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        if (params.width == -1 && params.height == -1) {
            isFullScreen = true;
        } else {
            isFullScreen = false;
        }
        event.setFullScreen(isFullScreen);
        return false;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == 0) {
            return onCreateOptionsMenu(menu);
        }
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0 || menu == null) {
            return true;
        }
        if (onPrepareOptionsMenu(menu) && menu.hasVisibleItems()) {
            return true;
        }
        return false;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(true);
        }
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void openOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.openPanel(0, null);
        }
    }

    public void closeOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.closePanel(0);
        }
    }

    public void invalidateOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.invalidatePanelMenu(0);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public void openContextMenu(View view) {
        view.showContextMenu();
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        return onSearchRequested();
    }

    public boolean onSearchRequested() {
        SearchManager searchManager = (SearchManager) this.mContext.getSystemService("search");
        ComponentName appName = getAssociatedActivity();
        if (appName == null || searchManager.getSearchableInfo(appName) == null) {
            return false;
        }
        searchManager.startSearch(null, false, appName, null, false);
        dismiss();
        return true;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (this.mActionBar == null || this.mActionModeTypeStarting != 0) {
            return null;
        }
        return this.mActionBar.startActionMode(callback);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        try {
            this.mActionModeTypeStarting = type;
            ActionMode onWindowStartingActionMode = onWindowStartingActionMode(callback);
            return onWindowStartingActionMode;
        } finally {
            this.mActionModeTypeStarting = 0;
        }
    }

    public void onActionModeStarted(ActionMode mode) {
        this.mActionMode = mode;
    }

    public void onActionModeFinished(ActionMode mode) {
        if (mode == this.mActionMode) {
            this.mActionMode = null;
        }
    }

    private ComponentName getAssociatedActivity() {
        Activity activity = this.mOwnerActivity;
        Context context = getContext();
        while (activity == null && context != null) {
            if (context instanceof Activity) {
                activity = (Activity) context;
            } else {
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
        }
        if (activity == null) {
            return null;
        }
        return activity.getComponentName();
    }

    public void takeKeyEvents(boolean get) {
        this.mWindow.takeKeyEvents(get);
    }

    public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
    }

    public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
    }

    public void setCancelable(boolean flag) {
        this.mCancelable = flag;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !this.mCancelable) {
            this.mCancelable = true;
        }
        this.mWindow.setCloseOnTouchOutside(cancel);
    }

    public void cancel() {
        if (!(this.mCanceled || this.mCancelMessage == null)) {
            this.mCanceled = true;
            Message.obtain(this.mCancelMessage).sendToTarget();
        }
        dismiss();
    }

    public void setOnCancelListener(OnCancelListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            throw new IllegalStateException("OnCancelListener is already taken by " + this.mCancelAndDismissTaken + " and can not be replaced.");
        } else if (listener != null) {
            this.mCancelMessage = this.mListenersHandler.obtainMessage(68, listener);
        } else {
            this.mCancelMessage = null;
        }
    }

    public void setCancelMessage(Message msg) {
        this.mCancelMessage = msg;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            throw new IllegalStateException("OnDismissListener is already taken by " + this.mCancelAndDismissTaken + " and can not be replaced.");
        } else if (listener != null) {
            this.mDismissMessage = this.mListenersHandler.obtainMessage(67, listener);
        } else {
            this.mDismissMessage = null;
        }
    }

    public void setOnShowListener(OnShowListener listener) {
        if (listener != null) {
            this.mShowMessage = this.mListenersHandler.obtainMessage(69, listener);
        } else {
            this.mShowMessage = null;
        }
    }

    public void setDismissMessage(Message msg) {
        this.mDismissMessage = msg;
    }

    public boolean takeCancelAndDismissListeners(String msg, OnCancelListener cancel, OnDismissListener dismiss) {
        if (this.mCancelAndDismissTaken != null) {
            this.mCancelAndDismissTaken = null;
        } else if (!(this.mCancelMessage == null && this.mDismissMessage == null)) {
            return false;
        }
        setOnCancelListener(cancel);
        setOnDismissListener(dismiss);
        this.mCancelAndDismissTaken = msg;
        return true;
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
    }
}
