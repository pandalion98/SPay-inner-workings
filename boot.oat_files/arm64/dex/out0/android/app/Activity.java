package android.app;

import android.R;
import android.app.ActivityManager.TaskDescription;
import android.app.Instrumentation.ActivityResult;
import android.app.assist.AssistContent;
import android.app.im.InjectionConstants.DispatchParentCall;
import android.app.im.InjectionManager;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.NetworkPolicyManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.DVFSHelper;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.preference.PreferenceActivity;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.BoostFramework;
import android.util.EventLog;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.OnWindowDismissedCallback;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toolbar;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.ToolbarActionBar;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.MultiPhoneWindow;
import com.android.internal.policy.PhoneWindow;
import com.samsung.android.dualscreen.DualScreen;
import com.samsung.android.dualscreen.DualScreenManager;
import com.samsung.android.multiwindow.MultiWindowApplicationInfos;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.sdk.dualscreen.SDualScreenListener.ExpandRequestListener;
import com.samsung.android.sdk.dualscreen.SDualScreenListener.ScreenChangeListener;
import com.samsung.android.sdk.dualscreen.SDualScreenListener.ShrinkRequestListener;
import com.sec.android.app.CscFeature;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Activity extends ContextThemeWrapper implements Factory2, Callback, KeyEvent.Callback, OnCreateContextMenuListener, ComponentCallbacks2, OnWindowDismissedCallback {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_DUALSCREEN = DualScreenManager.DEBUG;
    private static final boolean DEBUG_LIFECYCLE = false;
    public static final int DEFAULT_KEYS_DIALER = 1;
    public static final int DEFAULT_KEYS_DISABLE = 0;
    public static final int DEFAULT_KEYS_SEARCH_GLOBAL = 4;
    public static final int DEFAULT_KEYS_SEARCH_LOCAL = 3;
    public static final int DEFAULT_KEYS_SHORTCUT = 2;
    protected static final int[] FOCUSED_STATE_SET = new int[]{R.attr.state_focused};
    static final String FRAGMENTS_TAG = "android:fragments";
    private static final String HAS_CURENT_PERMISSIONS_REQUEST_KEY = "android:hasCurrentPermissionsRequest";
    private static final String REQUEST_PERMISSIONS_WHO_PREFIX = "@android:requestPermissions:";
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_FIRST_USER = 1;
    public static final int RESULT_OK = -1;
    private static final String SAVED_DIALOGS_TAG = "android:savedDialogs";
    private static final String SAVED_DIALOG_ARGS_KEY_PREFIX = "android:dialog_args_";
    private static final String SAVED_DIALOG_IDS_KEY = "android:savedDialogIds";
    private static final String SAVED_DIALOG_KEY_PREFIX = "android:dialog_";
    private static final String TAG = "Activity";
    private static final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
    private static int[] mAsParamVal;
    private static int mDragBoostPossible = -1;
    private static final boolean mIsAmericano = SystemProperties.get("ro.build.scafe").equals("americano");
    private static boolean mIsFirstACtivityStart = true;
    private static BoostFramework mPerf = null;
    private static int mPerfLockDuration = -1;
    private final boolean DEBUG_ELASTIC = true;
    private final boolean isElasticEnabled = true;
    ActionBar mActionBar = null;
    private int mActionModeTypeStarting = 0;
    ActivityInfo mActivityInfo;
    ActivityTransitionState mActivityTransitionState = new ActivityTransitionState();
    private Application mApplication;
    boolean mCalled;
    private boolean mChangeCanvasToTranslucent;
    boolean mChangingConfigurations = false;
    private ComponentName mComponent;
    int mConfigChangeFlags;
    Configuration mCurrentConfig;
    View mDecor = null;
    private int mDefaultKeyMode = 0;
    private SpannableStringBuilder mDefaultKeySsb = null;
    private boolean mDestroyed;
    private boolean mDoReportFullyDrawn = true;
    String mEmbeddedID;
    private boolean mEnableDefaultActionBarUp;
    SharedElementCallback mEnterTransitionListener = SharedElementCallback.NULL_CALLBACK;
    SharedElementCallback mExitTransitionListener = SharedElementCallback.NULL_CALLBACK;
    private ExpandRequestListener mExpandRequestListener = null;
    private OnCreateContextMenuListener mFeatureContextMenuListener = new FeatureContextMenuListener();
    boolean mFinished;
    private int mFlipfont = 0;
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    final Handler mHandler = new Handler();
    private boolean mHasCurrentPermissionsRequest;
    private int mIdent;
    private InjectionManager mInjectionManager;
    private final Object mInstanceTracker = StrictMode.trackActivity(this);
    private Instrumentation mInstrumentation;
    Intent mIntent;
    NonConfigurationInstances mLastNonConfigurationInstances;
    private DVFSHelper mLauncherBooster = null;
    ActivityThread mMainThread;
    private final ArrayList<ManagedCursor> mManagedCursors = new ArrayList();
    private SparseArray<ManagedDialog> mManagedDialogs;
    private MenuInflater mMenuInflater;
    MultiWindowStyle mMultiWindowStyle = new MultiWindowStyle();
    Activity mParent;
    private NetworkPolicyManager mPolicyManager;
    public int mPreferredOrientation = -2;
    private boolean mPreventEmbeddedTabs = false;
    String mReferrer;
    int mResultCode = 0;
    Intent mResultData = null;
    boolean mResumed;
    private ScreenChangeListener mScreenChangeListener = null;
    private SearchEvent mSearchEvent;
    private SearchManager mSearchManager;
    private ShrinkRequestListener mShrinkRequestListener = null;
    private int mStackedHeight = -1;
    boolean mStartedActivity;
    private boolean mStopped;
    View mSubDecor;
    Window mSubWindow;
    boolean mSubWindowAdded;
    private Window mSubWindowDummpy;
    boolean mTemporaryPause = false;
    private CharSequence mTitle;
    private int mTitleColor = 0;
    private boolean mTitleReady = false;
    private IBinder mToken;
    private TranslucentConversionListener mTranslucentCallback;
    private Thread mUiThread;
    boolean mVisibleBehind;
    boolean mVisibleFromClient = true;
    boolean mVisibleFromServer = false;
    private VoiceInteractor mVoiceInteractor;
    private Window mWindow;
    boolean mWindowAdded = false;
    private WindowManager mWindowManager;
    private String myName = getClass().getName();

    public interface TranslucentConversionListener {
        void onTranslucentConversionComplete(boolean z);
    }

    private class FeatureContextMenuListener implements OnCreateContextMenuListener {
        private FeatureContextMenuListener() {
        }

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            Log.d(Activity.TAG, "onCreateContextMenu dispatchCreateContextMenu");
            Activity.this.onCreateContextMenu(menu, v, menuInfo);
            if (Activity.this.mInjectionManager != null) {
                Activity.this.mInjectionManager.dispatchCreateContextMenu(Activity.this, menu, v, menuInfo, false);
            }
        }
    }

    class HostCallbacks extends FragmentHostCallback<Activity> {
        public HostCallbacks() {
            super(Activity.this);
        }

        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            Activity.this.dump(prefix, fd, writer, args);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return !Activity.this.isFinishing();
        }

        public LayoutInflater onGetLayoutInflater() {
            LayoutInflater result = Activity.this.getLayoutInflater();
            if (onUseFragmentManagerInflaterFactory()) {
                return result.cloneInContext(Activity.this);
            }
            return result;
        }

        public boolean onUseFragmentManagerInflaterFactory() {
            return Activity.this.getApplicationInfo().targetSdkVersion >= 21;
        }

        public Activity onGetHost() {
            return Activity.this;
        }

        public void onInvalidateOptionsMenu() {
            Activity.this.invalidateOptionsMenu();
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
            Activity.this.startActivityFromFragment(fragment, intent, requestCode, options);
        }

        public void onRequestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
            Activity.this.startActivityForResult(Activity.REQUEST_PERMISSIONS_WHO_PREFIX + fragment.mWho, Activity.this.getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, null);
        }

        public boolean onHasWindowAnimations() {
            return Activity.this.getWindow() != null;
        }

        public int onGetWindowAnimations() {
            Window w = Activity.this.getWindow();
            return w == null ? 0 : w.getAttributes().windowAnimations;
        }

        public void onAttachFragment(Fragment fragment) {
            Activity.this.onAttachFragment(fragment);
        }

        public View onFindViewById(int id) {
            return Activity.this.findViewById(id);
        }

        public boolean onHasView() {
            Window w = Activity.this.getWindow();
            return (w == null || w.peekDecorView() == null) ? false : true;
        }
    }

    private static final class ManagedCursor {
        private final Cursor mCursor;
        private boolean mReleased = false;
        private boolean mUpdated = false;

        ManagedCursor(Cursor cursor) {
            this.mCursor = cursor;
        }
    }

    private static class ManagedDialog {
        Bundle mArgs;
        Dialog mDialog;

        private ManagedDialog() {
        }
    }

    static final class NonConfigurationInstances {
        Object activity;
        HashMap<String, Object> children;
        List<Fragment> fragments;
        ArrayMap<String, LoaderManager> loaders;
        VoiceInteractor voiceInteractor;

        NonConfigurationInstances() {
        }
    }

    public interface TranslucentConversionStateListener {
        void onTranslucentConversionStateComplete(boolean z);
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public void setIntent(Intent newIntent) {
        this.mIntent = newIntent;
    }

    public final Application getApplication() {
        return this.mApplication;
    }

    public final boolean isChild() {
        return this.mParent != null;
    }

    public final Activity getParent() {
        return this.mParent;
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public LoaderManager getLoaderManager() {
        return this.mFragments.getLoaderManager();
    }

    public View getCurrentFocus() {
        return this.mWindow != null ? this.mWindow.getCurrentFocus() : null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        if (context != null) {
            if (isSupportSmartManagerCHNEnhancedFeature("trafficmanager")) {
                this.mPolicyManager = NetworkPolicyManager.from(this);
                checkFireWallPermission(context.getPackageName());
            }
            Typeface.SetAppTypeFace(context, context.getPackageName());
        } else {
            Typeface.SetAppTypeFace(context, "android");
        }
        if (this.mLastNonConfigurationInstances != null) {
            this.mFragments.restoreLoaderNonConfig(this.mLastNonConfigurationInstances.loaders);
        }
        if (this.mActivityInfo.parentActivityName != null) {
            if (this.mActionBar == null) {
                this.mEnableDefaultActionBarUp = true;
            } else {
                this.mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
            }
        }
        if (savedInstanceState != null) {
            this.mFragments.restoreAllState(savedInstanceState.getParcelable(FRAGMENTS_TAG), this.mLastNonConfigurationInstances != null ? this.mLastNonConfigurationInstances.fragments : null);
        }
        this.mFragments.dispatchCreate();
        getApplication().dispatchActivityCreated(this, savedInstanceState);
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.attachActivity(this);
        }
        this.mCalled = true;
    }

    private void checkFireWallPermission(String packageName) {
        this.mPolicyManager.checkFireWallPermission(mIsFirstACtivityStart, getApplicationContext().getPackageName());
        mIsFirstACtivityStart = false;
    }

    private boolean isSupportSmartManagerCHNEnhancedFeature(String featureName) {
        String features = CscFeature.getInstance().getString("CscFeature_SmartManager_ConfigSubFeatures");
        return !TextUtils.isEmpty(features) && features.contains(featureName);
    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onCreate(savedInstanceState);
    }

    final void performRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        restoreManagedDialogs(savedInstanceState);
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONRESTOREINSTANCESTATE, this, savedInstanceState, false);
        }
    }

    final void performRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        onRestoreInstanceState(savedInstanceState, persistentState);
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONRESTOREINSTANCESTATE, this, savedInstanceState, false);
        }
        if (savedInstanceState != null) {
            restoreManagedDialogs(savedInstanceState);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (this.mWindow != null) {
            Bundle windowState = savedInstanceState.getBundle(WINDOW_HIERARCHY_TAG);
            if (windowState != null) {
                this.mWindow.restoreHierarchyState(windowState);
            }
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void restoreManagedDialogs(Bundle savedInstanceState) {
        Bundle b = savedInstanceState.getBundle(SAVED_DIALOGS_TAG);
        if (b != null) {
            this.mManagedDialogs = new SparseArray(numDialogs);
            for (int valueOf : b.getIntArray(SAVED_DIALOG_IDS_KEY)) {
                Integer dialogId = Integer.valueOf(valueOf);
                Bundle dialogState = b.getBundle(savedDialogKeyFor(dialogId.intValue()));
                if (dialogState != null) {
                    ManagedDialog md = new ManagedDialog();
                    md.mArgs = b.getBundle(savedDialogArgsKeyFor(dialogId.intValue()));
                    md.mDialog = createDialog(dialogId, dialogState, md.mArgs);
                    if (md.mDialog != null) {
                        this.mManagedDialogs.put(dialogId.intValue(), md);
                        onPrepareDialog(dialogId.intValue(), md.mDialog, md.mArgs);
                        md.mDialog.onRestoreInstanceState(dialogState);
                    }
                }
            }
        }
    }

    private Dialog createDialog(Integer dialogId, Bundle state, Bundle args) {
        Dialog dialog = onCreateDialog(dialogId.intValue(), args);
        if (dialog == null) {
            return null;
        }
        dialog.dispatchOnCreate(state);
        return dialog;
    }

    private static String savedDialogKeyFor(int key) {
        return SAVED_DIALOG_KEY_PREFIX + key;
    }

    private static String savedDialogArgsKeyFor(int key) {
        return SAVED_DIALOG_ARGS_KEY_PREFIX + key;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        if (!isChild()) {
            this.mTitleReady = true;
            onTitleChanged(getTitle(), getTitleColor());
        }
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchViewCreated(this, false);
        }
        this.mCalled = true;
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onPostCreate(savedInstanceState);
    }

    protected void onStart() {
        this.mCalled = true;
        this.mFragments.doLoaderStart();
        getApplication().dispatchActivityStarted(this);
    }

    protected void onRestart() {
        this.mCalled = true;
    }

    public void onStateNotSaved() {
    }

    protected void onResume() {
        getApplication().dispatchActivityResumed(this);
        this.mActivityTransitionState.onResume();
        this.mCalled = true;
        getWindow().arrangeScaleStackBound();
    }

    protected void onPostResume() {
        Window win = getWindow();
        if (win != null) {
            win.makeActive();
        }
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(true);
        }
        this.mCalled = true;
    }

    public boolean isVoiceInteraction() {
        return this.mVoiceInteractor != null;
    }

    public boolean isVoiceInteractionRoot() {
        try {
            return this.mVoiceInteractor != null && ActivityManagerNative.getDefault().isRootVoiceInteraction(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    public VoiceInteractor getVoiceInteractor() {
        return this.mVoiceInteractor;
    }

    protected void onNewIntent(Intent intent) {
    }

    final void performSaveInstanceState(Bundle outState) {
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONSAVEINSTANCESTATE, this, outState, false);
        }
        onSaveInstanceState(outState);
        saveManagedDialogs(outState);
        this.mActivityTransitionState.saveState(outState);
        storeHasCurrentPermissionRequest(outState);
    }

    final void performSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONSAVEINSTANCESTATE, this, outState, false);
        }
        onSaveInstanceState(outState, outPersistentState);
        saveManagedDialogs(outState);
        storeHasCurrentPermissionRequest(outState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle(WINDOW_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        Parcelable p = this.mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
        getApplication().dispatchActivitySaveInstanceState(this, outState);
    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        onSaveInstanceState(outState);
    }

    private void saveManagedDialogs(Bundle outState) {
        if (this.mManagedDialogs != null) {
            int numDialogs = this.mManagedDialogs.size();
            if (numDialogs != 0) {
                Bundle dialogState = new Bundle();
                int[] ids = new int[this.mManagedDialogs.size()];
                for (int i = 0; i < numDialogs; i++) {
                    int key = this.mManagedDialogs.keyAt(i);
                    ids[i] = key;
                    ManagedDialog md = (ManagedDialog) this.mManagedDialogs.valueAt(i);
                    dialogState.putBundle(savedDialogKeyFor(key), md.mDialog.onSaveInstanceState());
                    if (md.mArgs != null) {
                        dialogState.putBundle(savedDialogArgsKeyFor(key), md.mArgs);
                    }
                }
                dialogState.putIntArray(SAVED_DIALOG_IDS_KEY, ids);
                outState.putBundle(SAVED_DIALOGS_TAG, dialogState);
            }
        }
    }

    protected void onPause() {
        getApplication().dispatchActivityPaused(this);
        this.mCalled = true;
        getWindow().handlePause();
    }

    protected void onUserLeaveHint() {
    }

    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return false;
    }

    public CharSequence onCreateDescription() {
        return null;
    }

    public void onProvideAssistData(Bundle data) {
    }

    public void onProvideAssistContent(AssistContent outContent) {
    }

    public boolean showAssist(Bundle args) {
        try {
            return ActivityManagerNative.getDefault().showAssistFromActivity(this.mToken, args);
        } catch (RemoteException e) {
            return false;
        }
    }

    protected void onStop() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(false);
        }
        this.mActivityTransitionState.onStop();
        getApplication().dispatchActivityStopped(this);
        this.mTranslucentCallback = null;
        this.mCalled = true;
    }

    protected void onDestroy() {
        int i;
        this.mCalled = true;
        if (this.mManagedDialogs != null) {
            int numDialogs = this.mManagedDialogs.size();
            for (i = 0; i < numDialogs; i++) {
                ManagedDialog md = (ManagedDialog) this.mManagedDialogs.valueAt(i);
                if (md.mDialog.isShowing()) {
                    md.mDialog.dismiss();
                }
            }
            this.mManagedDialogs = null;
        }
        synchronized (this.mManagedCursors) {
            int numCursors = this.mManagedCursors.size();
            for (i = 0; i < numCursors; i++) {
                ManagedCursor c = (ManagedCursor) this.mManagedCursors.get(i);
                if (c != null) {
                    c.mCursor.close();
                }
            }
            this.mManagedCursors.clear();
        }
        if (this.mSearchManager != null) {
            this.mSearchManager.stopSearch();
        }
        getApplication().dispatchActivityDestroyed(this);
    }

    public void reportFullyDrawn() {
        if (this.mDoReportFullyDrawn) {
            this.mDoReportFullyDrawn = false;
            try {
                ActivityManagerNative.getDefault().reportActivityFullyDrawn(this.mToken);
            } catch (RemoteException e) {
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mCalled = true;
        if (!(newConfig == null || newConfig.FlipFont <= 0 || this.mFlipfont == newConfig.FlipFont)) {
            Context context = getApplicationContext();
            if (context != null) {
                Typeface.SetAppTypeFace(context, context.getPackageName());
            } else {
                Typeface.SetAppTypeFace(context, "android");
            }
            this.mFlipfont = newConfig.FlipFont;
        }
        this.mFragments.dispatchConfigurationChanged(newConfig);
        if (this.mWindow != null) {
            this.mWindow.onConfigurationChanged(newConfig);
        }
        if (this.mActionBar != null) {
            this.mActionBar.onConfigurationChanged(newConfig);
        }
    }

    public int getChangingConfigurations() {
        return this.mConfigChangeFlags;
    }

    @Deprecated
    public Object getLastNonConfigurationInstance() {
        return this.mLastNonConfigurationInstances != null ? this.mLastNonConfigurationInstances.activity : null;
    }

    public Object onRetainNonConfigurationInstance() {
        return null;
    }

    HashMap<String, Object> getLastNonConfigurationChildInstances() {
        return this.mLastNonConfigurationInstances != null ? this.mLastNonConfigurationInstances.children : null;
    }

    HashMap<String, Object> onRetainNonConfigurationChildInstances() {
        return null;
    }

    NonConfigurationInstances retainNonConfigurationInstances() {
        Object activity = onRetainNonConfigurationInstance();
        HashMap<String, Object> children = onRetainNonConfigurationChildInstances();
        List<Fragment> fragments = this.mFragments.retainNonConfig();
        ArrayMap<String, LoaderManager> loaders = this.mFragments.retainLoaderNonConfig();
        if (activity == null && children == null && fragments == null && loaders == null && this.mVoiceInteractor == null) {
            return null;
        }
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.activity = activity;
        nci.children = children;
        nci.fragments = fragments;
        nci.loaders = loaders;
        if (this.mVoiceInteractor == null) {
            return nci;
        }
        this.mVoiceInteractor.retainInstance();
        nci.voiceInteractor = this.mVoiceInteractor;
        return nci;
    }

    public void onLowMemory() {
        this.mCalled = true;
        this.mFragments.dispatchLowMemory();
    }

    public void onTrimMemory(int level) {
        this.mCalled = true;
        this.mFragments.dispatchTrimMemory(level);
    }

    public FragmentManager getFragmentManager() {
        return this.mFragments.getFragmentManager();
    }

    public void onAttachFragment(Fragment fragment) {
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, null, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public void startManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            this.mManagedCursors.add(new ManagedCursor(c));
        }
    }

    @Deprecated
    public void stopManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            int N = this.mManagedCursors.size();
            for (int i = 0; i < N; i++) {
                if (((ManagedCursor) this.mManagedCursors.get(i)).mCursor == c) {
                    this.mManagedCursors.remove(i);
                    break;
                }
            }
        }
    }

    @Deprecated
    public void setPersistent(boolean isPersistent) {
    }

    public View findViewById(int id) {
        return getWindow().findViewById(id);
    }

    public ActionBar getActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    public void setActionBar(Toolbar toolbar) {
        if (getActionBar() instanceof WindowDecorActionBar) {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set android:windowActionBar to false in your theme to use a Toolbar instead.");
        }
        this.mMenuInflater = null;
        ToolbarActionBar tbab = new ToolbarActionBar(toolbar, getTitle(), this);
        this.mActionBar = tbab;
        this.mWindow.setCallback(tbab.getWrappedWindowCallback());
        this.mActionBar.invalidateOptionsMenu();
    }

    private void initWindowDecorActionBar() {
        Window window = getWindow();
        window.getDecorView();
        if (!isChild() && window.hasFeature(8) && this.mActionBar == null) {
            this.mActionBar = new WindowDecorActionBar(this);
            this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            this.mWindow.setDefaultIcon(this.mActivityInfo.getIconResource());
            this.mWindow.setDefaultLogo(this.mActivityInfo.getLogoResource());
        }
    }

    public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }

    public void setContentView(View view) {
        getWindow().setContentView(view);
        initWindowDecorActionBar();
    }

    public void setContentView(View view, LayoutParams params) {
        getWindow().setContentView(view, params);
        initWindowDecorActionBar();
    }

    public void demoteRenderer() {
        getWindow().getDecorView().demoteRenderer();
    }

    public void setBufferCount(int count) {
        getWindow().getDecorView().setBufferCount(count);
    }

    public void addContentView(View view, LayoutParams params) {
        getWindow().addContentView(view, params);
        initWindowDecorActionBar();
    }

    public TransitionManager getContentTransitionManager() {
        return getWindow().getTransitionManager();
    }

    public void setContentTransitionManager(TransitionManager tm) {
        getWindow().setTransitionManager(tm);
    }

    public Scene getContentScene() {
        return getWindow().getContentScene();
    }

    public void setFinishOnTouchOutside(boolean finish) {
        this.mWindow.setCloseOnTouchOutside(finish);
    }

    public final void setDefaultKeyMode(int mode) {
        this.mDefaultKeyMode = mode;
        switch (mode) {
            case 0:
            case 2:
                this.mDefaultKeySsb = null;
                return;
            case 1:
            case 3:
            case 4:
                this.mDefaultKeySsb = new SpannableStringBuilder();
                Selection.setSelection(this.mDefaultKeySsb, 0);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (getApplicationInfo().targetSdkVersion >= 5) {
                event.startTracking();
            } else {
                onBackPressed();
            }
            return true;
        } else if (this.mDefaultKeyMode == 0) {
            return false;
        } else {
            if (this.mDefaultKeyMode == 2) {
                Window w = getWindow();
                if (w.hasFeature(0) && w.performPanelShortcut(0, keyCode, event, 2)) {
                    return true;
                }
                return false;
            }
            boolean handled;
            boolean clearSpannable = false;
            if (event.getRepeatCount() == 0 && !event.isSystem()) {
                handled = TextKeyListener.getInstance().onKeyDown(null, this.mDefaultKeySsb, keyCode, event);
                if (handled && this.mDefaultKeySsb.length() > 0) {
                    String str = this.mDefaultKeySsb.toString();
                    clearSpannable = true;
                    switch (this.mDefaultKeyMode) {
                        case 1:
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + str));
                            intent.addFlags(268435456);
                            startActivity(intent);
                            break;
                        case 3:
                            startSearch(str, false, null, false);
                            break;
                        case 4:
                            startSearch(str, false, null, true);
                            break;
                        default:
                            break;
                    }
                }
            }
            clearSpannable = true;
            handled = false;
            if (!clearSpannable) {
                return handled;
            }
            this.mDefaultKeySsb.clear();
            this.mDefaultKeySsb.clearSpans();
            Selection.setSelection(this.mDefaultKeySsb, 0);
            return handled;
        }
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getApplicationInfo().targetSdkVersion < 5 || keyCode != 4 || !event.isTracking() || event.isCanceled()) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        if (this.mActionBar == null || !this.mActionBar.collapseActionView()) {
            try {
                if (!this.mFragments.getFragmentManager().popBackStackImmediate()) {
                    finishAfterTransition();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActionBar actionBar = getActionBar();
        return actionBar != null && actionBar.onKeyShortcut(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mWindow.shouldCloseOnTouch(this, event)) {
            return false;
        }
        finish();
        return true;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onUserInteraction() {
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (this.mParent == null) {
            View decor = this.mDecor;
            if (decor != null && decor.getParent() != null) {
                getWindowManager().updateViewLayout(decor, params);
            }
        }
    }

    public void onContentChanged() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public boolean hasWindowFocus() {
        Window w = getWindow();
        if (w != null) {
            View d = w.getDecorView();
            if (d != null) {
                return d.hasWindowFocus();
            }
        }
        return false;
    }

    public boolean hasWindowFocusSafe() {
        Window w = getWindow();
        if (w != null) {
            View d = w.peekDecorView();
            if (d != null) {
                return d.hasWindowFocus();
            }
        }
        return false;
    }

    public void onWindowDismissed() {
        finish();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        onUserInteraction();
        if (event.getKeyCode() == 82 && this.mActionBar != null && this.mActionBar.onMenuKeyEvent(event)) {
            return true;
        }
        Window win = getWindow();
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = this.mDecor;
        if (decor == null) {
            decor = win.getDecorView();
        }
        return event.dispatch(this, decor != null ? decor.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        onUserInteraction();
        if (getWindow().superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(getPackageName());
        LayoutParams params = getWindow().getAttributes();
        boolean isFullScreen = params.width == -1 && params.height == -1;
        event.setFullScreen(isFullScreen);
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            event.getText().add(title);
        }
        return true;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId != 0) {
            return false;
        }
        boolean show = onCreateOptionsMenu(menu);
        if (this.mInjectionManager != null) {
            show |= this.mInjectionManager.dispatchCreateOptionsMenu(this, menu, getMenuInflater(), false);
        }
        return show | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0 || menu == null) {
            return true;
        }
        boolean goforit = onPrepareOptionsMenu(menu);
        if (this.mInjectionManager != null) {
            goforit |= this.mInjectionManager.dispatchPrepareOptionsMenu(this, menu);
        }
        return goforit | this.mFragments.dispatchPrepareOptionsMenu(menu);
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            initWindowDecorActionBar();
            if (this.mActionBar != null) {
                this.mActionBar.dispatchMenuVisibilityChanged(true);
            } else {
                Log.e(TAG, "Tried to open action bar menu with no action bar");
            }
        }
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        CharSequence titleCondensed = item.getTitleCondensed();
        switch (featureId) {
            case 0:
                if (titleCondensed != null) {
                    EventLog.writeEvent(50000, new Object[]{Integer.valueOf(0), titleCondensed.toString()});
                }
                if ((this.mInjectionManager != null && this.mInjectionManager.dispatchOptionsItemSelected(this, item)) || onOptionsItemSelected(item) || this.mFragments.dispatchOptionsItemSelected(item)) {
                    return true;
                }
                if (item.getItemId() != R.id.home || this.mActionBar == null || (this.mActionBar.getDisplayOptions() & 4) == 0) {
                    return false;
                }
                if (this.mParent == null) {
                    return onNavigateUp();
                }
                return this.mParent.onNavigateUpFromChild(this);
            case 6:
                if (titleCondensed != null) {
                    EventLog.writeEvent(50000, new Object[]{Integer.valueOf(1), titleCondensed.toString()});
                }
                if (this.mInjectionManager != null && this.mInjectionManager.dispatchContextItemSelected(this, item)) {
                    Log.d(TAG, "onContextItemSelected handled by feature app");
                    return true;
                } else if (onContextItemSelected(item)) {
                    return true;
                } else {
                    return this.mFragments.dispatchContextItemSelected(item);
                }
            default:
                return false;
        }
    }

    public void onPanelClosed(int featureId, Menu menu) {
        switch (featureId) {
            case 0:
                this.mFragments.dispatchOptionsMenuClosed(menu);
                onOptionsMenuClosed(menu);
                return;
            case 6:
                onContextMenuClosed(menu);
                return;
            case 8:
                initWindowDecorActionBar();
                this.mActionBar.dispatchMenuVisibilityChanged(false);
                return;
            default:
                return;
        }
    }

    public void invalidateOptionsMenu() {
        if (!this.mWindow.hasFeature(0)) {
            return;
        }
        if (this.mActionBar == null || !this.mActionBar.invalidateOptionsMenu()) {
            this.mWindow.invalidatePanelMenu(0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mParent != null) {
            return this.mParent.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.mParent != null) {
            return this.mParent.onPrepareOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mParent != null) {
            return this.mParent.onOptionsItemSelected(item);
        }
        return false;
    }

    public boolean onNavigateUp() {
        Intent upIntent = getParentActivityIntent();
        if (upIntent == null) {
            return false;
        }
        if (this.mActivityInfo.taskAffinity == null) {
            finish();
        } else if (shouldUpRecreateTask(upIntent)) {
            TaskStackBuilder b = TaskStackBuilder.create(this);
            onCreateNavigateUpTaskStack(b);
            onPrepareNavigateUpTaskStack(b);
            b.startActivities();
            if (this.mResultCode == 0 && this.mResultData == null) {
                finishAffinity();
            } else {
                Log.i(TAG, "onNavigateUp only finishing topmost activity to return a result");
                finish();
            }
        } else {
            navigateUpTo(upIntent);
        }
        return true;
    }

    public boolean onNavigateUpFromChild(Activity child) {
        return onNavigateUp();
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        builder.addParentStack(this);
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onOptionsMenuClosed(Menu menu) {
        if (this.mParent != null) {
            this.mParent.onOptionsMenuClosed(menu);
        }
    }

    public void openOptionsMenu() {
        if (!this.mWindow.hasFeature(0)) {
            return;
        }
        if (this.mActionBar == null || !this.mActionBar.openOptionsMenu()) {
            this.mWindow.openPanel(0, null);
        }
    }

    public void closeOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.closePanel(0);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
        Log.d(TAG, "registerForContextMenu with mFeatureContextMenuListener");
        view.setOnCreateContextMenuListener(this.mFeatureContextMenuListener);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public void openContextMenu(View view) {
        view.showContextMenu();
    }

    public void closeContextMenu() {
        if (this.mWindow.hasFeature(6)) {
            this.mWindow.closePanel(6);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (this.mParent != null) {
            return this.mParent.onContextItemSelected(item);
        }
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
        if (this.mParent != null) {
            this.mParent.onContextMenuClosed(menu);
        }
    }

    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return null;
    }

    @Deprecated
    protected Dialog onCreateDialog(int id, Bundle args) {
        return onCreateDialog(id);
    }

    @Deprecated
    protected void onPrepareDialog(int id, Dialog dialog) {
        dialog.setOwnerActivity(this);
    }

    @Deprecated
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        onPrepareDialog(id, dialog);
    }

    @Deprecated
    public final void showDialog(int id) {
        showDialog(id, null);
    }

    @Deprecated
    public final boolean showDialog(int id, Bundle args) {
        if (this.mManagedDialogs == null) {
            this.mManagedDialogs = new SparseArray();
        }
        ManagedDialog md = (ManagedDialog) this.mManagedDialogs.get(id);
        if (md == null) {
            md = new ManagedDialog();
            md.mDialog = createDialog(Integer.valueOf(id), null, args);
            if (md.mDialog == null) {
                return false;
            }
            this.mManagedDialogs.put(id, md);
        }
        md.mArgs = args;
        onPrepareDialog(id, md.mDialog, args);
        md.mDialog.show();
        return true;
    }

    @Deprecated
    public final void dismissDialog(int id) {
        if (this.mManagedDialogs == null) {
            throw missingDialog(id);
        }
        ManagedDialog md = (ManagedDialog) this.mManagedDialogs.get(id);
        if (md == null) {
            throw missingDialog(id);
        }
        md.mDialog.dismiss();
    }

    private IllegalArgumentException missingDialog(int id) {
        return new IllegalArgumentException("no dialog with id " + id + " was ever " + "shown via Activity#showDialog");
    }

    @Deprecated
    public final void removeDialog(int id) {
        if (this.mManagedDialogs != null) {
            ManagedDialog md = (ManagedDialog) this.mManagedDialogs.get(id);
            if (md != null) {
                md.mDialog.dismiss();
                this.mManagedDialogs.remove(id);
            }
        }
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        boolean result = onSearchRequested();
        this.mSearchEvent = null;
        return result;
    }

    public boolean onSearchRequested() {
        if (InjectionManager.getInstance() != null) {
            InjectionManager.getInstance().dispatchParentCallToFeature(DispatchParentCall.ONSEARCHREQUESTED, this);
        }
        if ((getResources().getConfiguration().uiMode & 15) == 4) {
            return false;
        }
        startSearch(null, false, null, false);
        return true;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        ensureSearchManager();
        this.mSearchManager.startSearch(initialQuery, selectInitialQuery, getComponentName(), appSearchData, globalSearch);
    }

    public void triggerSearch(String query, Bundle appSearchData) {
        ensureSearchManager();
        this.mSearchManager.triggerSearch(query, getComponentName(), appSearchData);
    }

    public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
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

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            if (this.mActionBar != null) {
                this.mMenuInflater = new MenuInflater(this.mActionBar.getThemedContext(), this);
            } else {
                this.mMenuInflater = new MenuInflater(this);
            }
        }
        return this.mMenuInflater;
    }

    protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
        if (this.mParent == null) {
            super.onApplyThemeResource(theme, resid, first);
        } else {
            try {
                theme.setTo(this.mParent.getTheme());
            } catch (Exception e) {
            }
            theme.applyStyle(resid, false);
        }
        if (theme != null) {
            TypedArray a = theme.obtainStyledAttributes(com.android.internal.R.styleable.Theme);
            int colorPrimary = a.getColor(227, 0);
            int textColorPrimary = a.getColor(6, 0);
            a.recycle();
            if (colorPrimary != 0) {
                TaskDescription v = new TaskDescription(null, null, colorPrimary);
                v.setTextPrimaryColor(textColorPrimary);
                setTaskDescription(v);
            }
        }
    }

    public final void requestPermissions(String[] permissions, int requestCode) {
        if (this.mHasCurrentPermissionsRequest) {
            Log.w(TAG, "Can reqeust only one set of permissions at a time");
            onRequestPermissionsResult(requestCode, new String[0], new int[0]);
            return;
        }
        startActivityForResult(REQUEST_PERMISSIONS_WHO_PREFIX, getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, null);
        this.mHasCurrentPermissionsRequest = true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return getPackageManager().shouldShowRequestPermissionRationale(permission);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        String callerPackageName = getPackageName();
        if ("com.sec.android.app.launcher".equals(callerPackageName) || "com.sec.android.app.easylauncher".equals(callerPackageName)) {
            if (this.mLauncherBooster == null) {
                this.mLauncherBooster = new DVFSHelper(this, "LAUNCHER_BOOST", 12, 0);
            }
            if (intent.getComponent() != null) {
                this.mLauncherBooster.onAppLaunchEvent(this, intent.getComponent().getClassName());
            }
        }
        if (this.mParent == null) {
            ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, requestCode, options);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
            }
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            cancelInputsAndStartExitTransition(options);
        } else if (options != null) {
            this.mParent.startActivityFromChild(this, intent, requestCode, options);
        } else {
            this.mParent.startActivityFromChild(this, intent, requestCode);
        }
    }

    private void cancelInputsAndStartExitTransition(Bundle options) {
        View decor = this.mWindow != null ? this.mWindow.peekDecorView() : null;
        if (decor != null) {
            decor.cancelPendingInputEvents();
        }
        if (options != null && !isTopOfTask()) {
            this.mActivityTransitionState.startExitOutTransition(this, options);
        }
    }

    public void startActivityForResultAsUser(Intent intent, int requestCode, UserHandle user) {
        startActivityForResultAsUser(intent, requestCode, null, user);
    }

    public void startActivityForResultAsUser(Intent intent, int requestCode, Bundle options, UserHandle user) {
        if (this.mParent != null) {
            throw new RuntimeException("Can't be called from a child");
        }
        ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, requestCode, options, user);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
        }
        if (requestCode >= 0) {
            this.mStartedActivity = true;
        }
        cancelInputsAndStartExitTransition(options);
    }

    public void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        if (this.mParent != null) {
            throw new RuntimeException("Can't be called from a child");
        }
        ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, -1, options, user);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    public void startActivityAsCaller(Intent intent, Bundle options, boolean ignoreTargetSecurity, int userId) {
        if (this.mParent != null) {
            throw new RuntimeException("Can't be called from a child");
        }
        ActivityResult ar = this.mInstrumentation.execStartActivityAsCaller(this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, -1, options, ignoreTargetSecurity, userId);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        if (this.mParent == null) {
            startIntentSenderForResultInner(intent, requestCode, fillInIntent, flagsMask, flagsValues, this, options);
        } else if (options != null) {
            this.mParent.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            this.mParent.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    private void startIntentSenderForResultInner(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, Activity activity, Bundle options) throws SendIntentException {
        String resolvedType = null;
        if (fillInIntent != null) {
            try {
                fillInIntent.migrateExtraStreamToClipData();
                fillInIntent.prepareToLeaveProcess();
                resolvedType = fillInIntent.resolveTypeIfNeeded(getContentResolver());
            } catch (RemoteException e) {
            }
        }
        int result = ActivityManagerNative.getDefault().startActivityIntentSender(this.mMainThread.getApplicationThread(), intent, fillInIntent, resolvedType, this.mToken, activity.mEmbeddedID, requestCode, flagsMask, flagsValues, options);
        if (result == -6) {
            throw new SendIntentException();
        }
        Instrumentation.checkStartActivityResult(result, null);
        if (requestCode >= 0) {
            this.mStartedActivity = true;
        }
    }

    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    public void startActivity(Intent intent, Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            startActivityForResult(intent, -1);
        }
    }

    public void startActivities(Intent[] intents) {
        startActivities(intents, null);
    }

    public void startActivities(Intent[] intents, Bundle options) {
        this.mInstrumentation.execStartActivities(this, this.mMainThread.getApplicationThread(), this.mToken, this, intents, options);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        if (options != null) {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        return startActivityIfNeeded(intent, requestCode, null);
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode, Bundle options) {
        if (this.mParent == null) {
            int result = 1;
            try {
                Parcelable referrer = onProvideReferrer();
                if (referrer != null) {
                    intent.putExtra(Intent.EXTRA_REFERRER, referrer);
                }
                intent.migrateExtraStreamToClipData();
                intent.prepareToLeaveProcess();
                result = ActivityManagerNative.getDefault().startActivity(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent.resolveTypeIfNeeded(getContentResolver()), this.mToken, this.mEmbeddedID, requestCode, 1, null, options);
            } catch (RemoteException e) {
            }
            Instrumentation.checkStartActivityResult(result, intent);
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            return result != 1;
        } else {
            throw new UnsupportedOperationException("startActivityIfNeeded can only be called from a top-level activity");
        }
    }

    public boolean startNextMatchingActivity(Intent intent) {
        return startNextMatchingActivity(intent, null);
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle options) {
        if (this.mParent == null) {
            try {
                intent.migrateExtraStreamToClipData();
                intent.prepareToLeaveProcess();
                return ActivityManagerNative.getDefault().startNextMatchingActivity(this.mToken, intent, options);
            } catch (RemoteException e) {
                return false;
            }
        }
        throw new UnsupportedOperationException("startNextMatchingActivity can only be called from a top-level activity");
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        startActivityFromChild(child, intent, requestCode, null);
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode, Bundle options) {
        ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, child, intent, requestCode, options);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, child.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, null);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        startActivityForResult(fragment.mWho, intent, requestCode, options);
    }

    public void startActivityForResult(String who, Intent intent, int requestCode, Bundle options) {
        Parcelable referrer = onProvideReferrer();
        if (referrer != null) {
            intent.putExtra(Intent.EXTRA_REFERRER, referrer);
        }
        ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, who, intent, requestCode, options);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, who, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    public boolean canStartActivityForResult() {
        return true;
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        startIntentSenderForResultInner(intent, requestCode, fillInIntent, flagsMask, flagsValues, child, options);
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        try {
            ActivityManagerNative.getDefault().overridePendingTransition(this.mToken, getPackageName(), enterAnim, exitAnim);
        } catch (RemoteException e) {
        }
    }

    public final void setResult(int resultCode) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = null;
        }
    }

    public final void setResult(int resultCode, Intent data) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = data;
        }
    }

    public Uri getReferrer() {
        Intent intent = getIntent();
        Uri referrer = (Uri) intent.getParcelableExtra(Intent.EXTRA_REFERRER);
        if (referrer != null) {
            return referrer;
        }
        String referrerName = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
        if (referrerName != null) {
            return Uri.parse(referrerName);
        }
        if (this.mReferrer != null) {
            return new Builder().scheme("android-app").authority(this.mReferrer).build();
        }
        return null;
    }

    public Uri onProvideReferrer() {
        return null;
    }

    public String getCallingPackage() {
        try {
            return ActivityManagerNative.getDefault().getCallingPackage(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public ComponentName getCallingActivity() {
        try {
            return ActivityManagerNative.getDefault().getCallingActivity(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setVisible(boolean visible) {
        if (this.mVisibleFromClient != visible) {
            this.mVisibleFromClient = visible;
            if (!this.mVisibleFromServer) {
                return;
            }
            if (visible) {
                makeVisible();
            } else {
                this.mDecor.setVisibility(4);
            }
        }
    }

    void makeVisible() {
        if (!this.mWindowAdded) {
            getWindowManager().addView(this.mDecor, getWindow().getAttributes());
            this.mWindowAdded = true;
        }
        this.mDecor.setVisibility(0);
    }

    public boolean isFinishing() {
        return this.mFinished;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public boolean isChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public void recreate() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can only be called on top-level activity");
        } else if (Looper.myLooper() != this.mMainThread.getLooper()) {
            throw new IllegalStateException("Must be called from main thread");
        } else {
            this.mMainThread.requestRelaunchActivity(this.mToken, null, null, 0, false, null, null, false, getMultiWindowStyle(), getBaseContext().getDisplayId());
        }
    }

    private void finish(boolean finishTask) {
        if (this.mParent == null) {
            int resultCode;
            Intent resultData;
            synchronized (this) {
                resultCode = this.mResultCode;
                resultData = this.mResultData;
            }
            if (resultData != null) {
                try {
                    resultData.prepareToLeaveProcess();
                } catch (RemoteException e) {
                    return;
                }
            }
            if (ActivityManagerNative.getDefault().finishActivity(this.mToken, resultCode, resultData, finishTask)) {
                this.mFinished = true;
                return;
            }
            return;
        }
        this.mParent.finishFromChild(this);
    }

    public void finish() {
        finish(false);
    }

    public void finishAffinity() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can not be called from an embedded activity");
        } else if (this.mResultCode == 0 && this.mResultData == null) {
            try {
                if (ActivityManagerNative.getDefault().finishActivityAffinity(this.mToken)) {
                    this.mFinished = true;
                }
            } catch (RemoteException e) {
            }
        } else {
            throw new IllegalStateException("Can not be called to deliver a result");
        }
    }

    public void finishFromChild(Activity child) {
        finish();
    }

    public void finishAfterTransition() {
        if (!this.mActivityTransitionState.startExitBackTransition(this)) {
            finish();
        }
    }

    public void finishActivity(int requestCode) {
        if (this.mParent == null) {
            try {
                ActivityManagerNative.getDefault().finishSubActivity(this.mToken, this.mEmbeddedID, requestCode);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        this.mParent.finishActivityFromChild(this, requestCode);
    }

    public void finishActivityFromChild(Activity child, int requestCode) {
        try {
            ActivityManagerNative.getDefault().finishSubActivity(this.mToken, child.mEmbeddedID, requestCode);
        } catch (RemoteException e) {
        }
    }

    public void finishAndRemoveTask() {
        finish(true);
    }

    public boolean releaseInstance() {
        try {
            return ActivityManagerNative.getDefault().releaseActivityInstance(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onActivityReenter(int resultCode, Intent data) {
    }

    public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        String packageName = getPackageName();
        try {
            data.prepareToLeaveProcess();
            IIntentSender target = ActivityManagerNative.getDefault().getIntentSender(3, packageName, this.mParent == null ? this.mToken : this.mParent.mToken, this.mEmbeddedID, requestCode, new Intent[]{data}, null, flags, null, UserHandle.myUserId());
            return target != null ? new PendingIntent(target) : null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setRequestedOrientation(int requestedOrientation) {
        if (this.mParent == null) {
            try {
                ActivityManagerNative.getDefault().setRequestedOrientation(this.mToken, requestedOrientation);
                this.mPreferredOrientation = requestedOrientation;
                this.mMultiWindowStyle.setAppRequestOrientation(requestedOrientation, true);
                getWindow().setAppRequestOrientation(requestedOrientation);
            } catch (RemoteException e) {
            }
            if (requestedOrientation != -1) {
                getWindow().arrangeScaleStackBound();
                return;
            }
            return;
        }
        this.mParent.setRequestedOrientation(requestedOrientation);
    }

    public int getRequestedOrientation() {
        if (this.mParent != null) {
            return this.mParent.getRequestedOrientation();
        }
        try {
            return ActivityManagerNative.getDefault().getRequestedOrientation(this.mToken);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void setMultiWindowStyle(MultiWindowStyle style) {
        if (this.mParent == null) {
            MultiWindowFacade service = (MultiWindowFacade) getSystemService(Context.MULTIWINDOW_FACADE_SERVICE);
            if (service != null) {
                service.setMultiWindowStyle(this.mToken, style);
                return;
            }
            return;
        }
        this.mParent.setMultiWindowStyle(style);
    }

    public void updateMultiWindowSetting(StringBuilder key, boolean enable) {
        if (this.mParent == null) {
            MultiWindowFacade service = (MultiWindowFacade) getSystemService(Context.MULTIWINDOW_FACADE_SERVICE);
            ComponentName cn = getComponentName();
            String reason = "MW/" + cn.getPackageName() + "/" + cn.getClassName() + "/";
            if (service != null) {
                service.updateMultiWindowSetting(key.toString(), reason, enable);
                return;
            }
            return;
        }
        this.mParent.updateMultiWindowSetting(key, enable);
    }

    public MultiWindowStyle getMultiWindowStyle() {
        if (this.mParent == null) {
            return this.mMultiWindowStyle;
        }
        return this.mParent.getMultiWindowStyle();
    }

    public int getWindowMode() {
        if (this.mParent == null) {
            return this.mMultiWindowStyle.convertToWindowMode();
        }
        MultiWindowStyle style = this.mParent.getMultiWindowStyle();
        if (style != null) {
            return style.convertToWindowMode();
        }
        return 0;
    }

    public void setWindowMode(int mode, boolean allInTask) {
        MultiWindowStyle style = new MultiWindowStyle().convertToMultiWindowStyle(mode);
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(new ComponentName((Context) this, getClass()), 128);
        } catch (NameNotFoundException e) {
        }
        if (activityInfo != null && MultiWindowApplicationInfos.getInstance().isSupportMultiWindow(activityInfo)) {
            style.setOption(2, true);
            style.parseStyleOptions(this, activityInfo);
        }
        if (this.mParent == null) {
            MultiWindowFacade service = (MultiWindowFacade) getSystemService(Context.MULTIWINDOW_FACADE_SERVICE);
            if (service != null) {
                service.setMultiWindowStyle(this.mToken, style);
                return;
            }
            return;
        }
        this.mParent.setMultiWindowStyle(style);
    }

    public void onWindowStatusChanged(MultiWindowStyle windowMode, int windowInfoChanged) {
    }

    public int getTaskId() {
        try {
            return ActivityManagerNative.getDefault().getTaskForActivity(this.mToken, false);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isTaskRoot() {
        try {
            if (ActivityManagerNative.getDefault().getTaskForActivity(this.mToken, true) >= 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean moveTaskToBack(boolean nonRoot) {
        try {
            return ActivityManagerNative.getDefault().moveActivityTaskToBack(this.mToken, nonRoot);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getLocalClassName() {
        String pkg = getPackageName();
        String cls = this.mComponent.getClassName();
        int packageLen = pkg.length();
        return (cls.startsWith(pkg) && cls.length() > packageLen && cls.charAt(packageLen) == '.') ? cls.substring(packageLen + 1) : cls;
    }

    public ComponentName getComponentName() {
        return this.mComponent;
    }

    public SharedPreferences getPreferences(int mode) {
        return getSharedPreferences(getLocalClassName(), mode);
    }

    private void ensureSearchManager() {
        if (this.mSearchManager == null) {
            this.mSearchManager = new SearchManager(this, null);
        }
    }

    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        } else if (Context.WINDOW_SERVICE.equals(name)) {
            return this.mWindowManager;
        } else {
            if (!"search".equals(name)) {
                return super.getSystemService(name);
            }
            ensureSearchManager();
            return this.mSearchManager;
        }
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        onTitleChanged(title, this.mTitleColor);
        if (this.mParent != null) {
            this.mParent.onChildTitleChanged(this, title);
        }
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    @Deprecated
    public void setTitleColor(int textColor) {
        this.mTitleColor = textColor;
        onTitleChanged(this.mTitle, textColor);
    }

    public final CharSequence getTitle() {
        return this.mTitle;
    }

    public final int getTitleColor() {
        return this.mTitleColor;
    }

    protected void onTitleChanged(CharSequence title, int color) {
        if (this.mTitleReady) {
            Window win = getWindow();
            if (win != null) {
                win.setTitle(title);
                if (color != 0) {
                    win.setTitleColor(color);
                }
            }
            if (this.mActionBar != null) {
                this.mActionBar.setWindowTitle(title);
            }
        }
    }

    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
    }

    public void setTaskDescription(TaskDescription taskDescription) {
        TaskDescription td;
        if (taskDescription.getIconFilename() != null || taskDescription.getIcon() == null) {
            td = taskDescription;
        } else {
            int size = ActivityManager.getLauncherLargeIconSizeInner(this);
            td = new TaskDescription(taskDescription.getLabel(), Bitmap.createScaledBitmap(taskDescription.getIcon(), size, size, true), taskDescription.getPrimaryColor());
            td.setTextPrimaryColor(taskDescription.getTextPrimaryColor());
        }
        if ("com.sec.android.app.camera".equals(getPackageName())) {
            IBinder token = this.mToken;
            final TaskDescription taskDesc = td;
            new Thread() {
                public void run() {
                    try {
                        ActivityManagerNative.getDefault().setTaskDescription(Activity.this.mToken, taskDesc);
                    } catch (RemoteException e) {
                    }
                }
            }.start();
            return;
        }
        try {
            ActivityManagerNative.getDefault().setTaskDescription(this.mToken, td);
        } catch (RemoteException e) {
        }
    }

    public final void setProgressBarVisibility(boolean visible) {
        getWindow().setFeatureInt(2, visible ? -1 : -2);
    }

    public final void setProgressBarIndeterminateVisibility(boolean visible) {
        getWindow().setFeatureInt(5, visible ? -1 : -2);
    }

    public final void setProgressBarIndeterminate(boolean indeterminate) {
        getWindow().setFeatureInt(2, indeterminate ? -3 : -4);
    }

    public final void setProgress(int progress) {
        getWindow().setFeatureInt(2, progress + 0);
    }

    public final void setSecondaryProgress(int secondaryProgress) {
        getWindow().setFeatureInt(2, secondaryProgress + 20000);
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public final void setMediaController(MediaController controller) {
        getWindow().setMediaController(controller);
    }

    public final MediaController getMediaController() {
        return getWindow().getMediaController();
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != this.mUiThread) {
            this.mHandler.post(action);
        } else {
            action.run();
        }
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if ("fragment".equals(name)) {
            return this.mFragments.onCreateView(parent, name, context, attrs);
        }
        return onCreateView(name, context, attrs);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        dumpInner(prefix, fd, writer, args);
    }

    void dumpInner(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix);
        writer.print("Local Activity ");
        writer.print(Integer.toHexString(System.identityHashCode(this)));
        writer.println(" State:");
        String innerPrefix = prefix + "  ";
        writer.print(innerPrefix);
        writer.print("mResumed=");
        writer.print(this.mResumed);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mFinished=");
        writer.println(this.mFinished);
        writer.print(innerPrefix);
        writer.print("mChangingConfigurations=");
        writer.println(this.mChangingConfigurations);
        writer.print(innerPrefix);
        writer.print("mCurrentConfig=");
        writer.println(this.mCurrentConfig);
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            Configuration overrideConfig = getOverrideConfiguration();
            if (!(overrideConfig == null || overrideConfig == Configuration.EMPTY)) {
                writer.print(innerPrefix);
                writer.print("overrideConfig=");
                writer.println(overrideConfig);
            }
            writer.print(innerPrefix);
            writer.print("DisplayMetrics=");
            writer.println(getResources().getDisplayMetrics());
        }
        writer.print(innerPrefix);
        writer.print("mMultiWindowStyle=");
        writer.println(this.mMultiWindowStyle);
        if (this.mWindow != null && (this.mWindow instanceof MultiPhoneWindow)) {
            writer.print(innerPrefix);
            writer.print("mStackBounds=");
            writer.println(((MultiPhoneWindow) this.mWindow).getStackBound());
        }
        this.mFragments.dumpLoaders(innerPrefix, fd, writer, args);
        this.mFragments.getFragmentManager().dump(innerPrefix, fd, writer, args);
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.dump(innerPrefix, fd, writer, args);
        }
        if (!(getWindow() == null || getWindow().peekDecorView() == null || getWindow().peekDecorView().getViewRootImpl() == null)) {
            getWindow().peekDecorView().getViewRootImpl().dump(prefix, fd, writer, args);
        }
        this.mHandler.getLooper().dump(new PrintWriterPrinter(writer), prefix);
    }

    public boolean isImmersive() {
        try {
            return ActivityManagerNative.getDefault().isImmersive(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean isTopOfTask() {
        try {
            return ActivityManagerNative.getDefault().isTopOfTask(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void convertFromTranslucent() {
        try {
            this.mTranslucentCallback = null;
            if (ActivityManagerNative.getDefault().convertFromTranslucent(this.mToken)) {
                WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, true);
            }
        } catch (RemoteException e) {
        }
    }

    public void convertFromTranslucent(boolean skipSetWindowOpaque) {
        try {
            this.mTranslucentCallback = null;
            if (ActivityManagerNative.getDefault().convertFromTranslucent(this.mToken, skipSetWindowOpaque)) {
                WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, true);
            }
        } catch (RemoteException e) {
        }
    }

    public boolean convertToTranslucent(final TranslucentConversionStateListener callback) {
        return convertToTranslucent(new TranslucentConversionListener() {
            public void onTranslucentConversionComplete(boolean drawComplete) {
                callback.onTranslucentConversionStateComplete(drawComplete);
            }
        }, null);
    }

    public boolean convertToTranslucent(TranslucentConversionListener callback, ActivityOptions options) {
        boolean drawComplete;
        try {
            this.mTranslucentCallback = callback;
            this.mChangeCanvasToTranslucent = ActivityManagerNative.getDefault().convertToTranslucent(this.mToken, options);
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
            drawComplete = true;
        } catch (RemoteException e) {
            this.mChangeCanvasToTranslucent = false;
            drawComplete = false;
        }
        if (!(this.mChangeCanvasToTranslucent || this.mTranslucentCallback == null)) {
            this.mTranslucentCallback.onTranslucentConversionComplete(drawComplete);
        }
        return this.mChangeCanvasToTranslucent;
    }

    void onTranslucentConversionComplete(boolean drawComplete) {
        if (this.mTranslucentCallback != null) {
            this.mTranslucentCallback.onTranslucentConversionComplete(drawComplete);
            this.mTranslucentCallback = null;
        }
        if (this.mChangeCanvasToTranslucent) {
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
        }
    }

    public void onNewActivityOptions(ActivityOptions options) {
        this.mActivityTransitionState.setEnterActivityOptions(this, options);
        if (!this.mStopped) {
            this.mActivityTransitionState.enterReady(this);
        }
    }

    ActivityOptions getActivityOptions() {
        try {
            return ActivityManagerNative.getDefault().getActivityOptions(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean requestVisibleBehind(boolean visible) {
        if (!this.mResumed) {
            visible = false;
        }
        try {
            boolean z = ActivityManagerNative.getDefault().requestVisibleBehind(this.mToken, visible) && visible;
            this.mVisibleBehind = z;
        } catch (RemoteException e) {
            this.mVisibleBehind = false;
        }
        return this.mVisibleBehind;
    }

    public void onVisibleBehindCanceled() {
        this.mCalled = true;
    }

    public boolean isBackgroundVisibleBehind() {
        try {
            return ActivityManagerNative.getDefault().isBackgroundVisibleBehind(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void onBackgroundVisibleBehindChanged(boolean visible) {
    }

    public void onEnterAnimationComplete() {
    }

    public void dispatchEnterAnimationComplete() {
        onEnterAnimationComplete();
        if (getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().getViewTreeObserver().dispatchOnEnterAnimationComplete();
        }
    }

    public void setImmersive(boolean i) {
        try {
            ActivityManagerNative.getDefault().setImmersive(this.mToken, i);
        } catch (RemoteException e) {
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mWindow.getDecorView().startActionMode(callback);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return this.mWindow.getDecorView().startActionMode(callback, type);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (this.mActionModeTypeStarting == 0) {
            initWindowDecorActionBar();
            if (this.mActionBar != null) {
                return this.mActionBar.startActionMode(callback);
            }
        }
        return null;
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
    }

    public void onActionModeFinished(ActionMode mode) {
    }

    public boolean shouldUpRecreateTask(Intent targetIntent) {
        boolean z = false;
        try {
            PackageManager pm = getPackageManager();
            ComponentName cn = targetIntent.getComponent();
            if (cn == null) {
                cn = targetIntent.resolveActivity(pm);
            }
            ActivityInfo info = pm.getActivityInfo(cn, 0);
            if (info.taskAffinity != null) {
                z = ActivityManagerNative.getDefault().shouldUpRecreateTask(this.mToken, info.taskAffinity);
            }
        } catch (RemoteException e) {
        } catch (NameNotFoundException e2) {
        }
        return z;
    }

    public boolean navigateUpTo(Intent upIntent) {
        boolean z = false;
        if (this.mParent != null) {
            return this.mParent.navigateUpToFromChild(this, upIntent);
        }
        if (upIntent.getComponent() == null) {
            ComponentName destInfo = upIntent.resolveActivity(getPackageManager());
            if (destInfo == null) {
                return z;
            }
            Intent upIntent2 = new Intent(upIntent);
            upIntent2.setComponent(destInfo);
            upIntent = upIntent2;
        }
        synchronized (this) {
            int resultCode = this.mResultCode;
            Intent resultData = this.mResultData;
        }
        if (resultData != null) {
            resultData.prepareToLeaveProcess();
        }
        try {
            upIntent.prepareToLeaveProcess();
            return ActivityManagerNative.getDefault().navigateUpTo(this.mToken, upIntent, resultCode, resultData);
        } catch (RemoteException e) {
            return z;
        }
    }

    public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
        return navigateUpTo(upIntent);
    }

    public Intent getParentActivityIntent() {
        Intent intent = null;
        String parentName = this.mActivityInfo.parentActivityName;
        if (!TextUtils.isEmpty(parentName)) {
            ComponentName target = new ComponentName((Context) this, parentName);
            try {
                intent = getPackageManager().getActivityInfo(target, 0).parentActivityName == null ? Intent.makeMainActivity(target) : new Intent().setComponent(target);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "getParentActivityIntent: bad parentActivityName '" + parentName + "' in manifest");
            }
        }
        return intent;
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mEnterTransitionListener = callback;
    }

    public void setExitSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mExitTransitionListener = callback;
    }

    public void postponeEnterTransition() {
        this.mActivityTransitionState.postponeEnterTransition();
    }

    public void startPostponedEnterTransition() {
        this.mActivityTransitionState.startPostponedEnterTransition();
    }

    final void setParent(Activity parent) {
        this.mParent = parent;
    }

    final void attach(Context context, ActivityThread aThread, Instrumentation instr, IBinder token, int ident, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, NonConfigurationInstances lastNonConfigurationInstances, Configuration config, String referrer, IVoiceInteractor voiceInteractor) {
        attachBaseContext(context);
        this.mFragments.attachHost(null);
        this.mToken = token;
        this.mParent = parent;
        MultiWindowStyle mwStyle = getMultiWindowStyle();
        if (parent == null && mwStyle != null && mwStyle.isMultiPhoneWindowNeeded(info, context)) {
            this.mWindow = makeNewWindow(this, true);
        } else {
            this.mWindow = new PhoneWindow(this);
        }
        this.mWindow.setCallback(this);
        this.mWindow.setOnWindowDismissedCallback(this);
        this.mWindow.getLayoutInflater().setPrivateFactory(this);
        if (info.softInputMode != 0) {
            this.mWindow.setSoftInputMode(info.softInputMode);
        }
        if (info.uiOptions != 0) {
            this.mWindow.setUiOptions(info.uiOptions);
        }
        this.mUiThread = Thread.currentThread();
        this.mMainThread = aThread;
        this.mInstrumentation = instr;
        this.mIdent = ident;
        this.mApplication = application;
        this.mIntent = intent;
        this.mReferrer = referrer;
        this.mComponent = intent.getComponent();
        this.mActivityInfo = info;
        this.mTitle = title;
        this.mEmbeddedID = id;
        this.mLastNonConfigurationInstances = lastNonConfigurationInstances;
        if (voiceInteractor != null) {
            if (lastNonConfigurationInstances != null) {
                this.mVoiceInteractor = lastNonConfigurationInstances.voiceInteractor;
            } else {
                this.mVoiceInteractor = new VoiceInteractor(voiceInteractor, this, this, Looper.myLooper());
            }
        }
        this.mWindow.setWindowManager((WindowManager) context.getSystemService(Context.WINDOW_SERVICE), this.mToken, this.mComponent.flattenToString(), (info.flags & 512) != 0);
        if (this.mParent != null) {
            this.mWindow.setContainer(this.mParent.getWindow());
        }
        this.mWindowManager = this.mWindow.getWindowManager();
        this.mCurrentConfig = config;
    }

    public final IBinder getActivityToken() {
        return this.mParent != null ? this.mParent.getActivityToken() : this.mToken;
    }

    final void performCreateCommon() {
        boolean z = false;
        if (!this.mWindow.getWindowStyle().getBoolean(10, false)) {
            z = true;
        }
        this.mVisibleFromClient = z;
        this.mFragments.dispatchActivityCreated();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
    }

    final void performCreate(Bundle icicle) {
        this.mMainThread.mLastIntendedActivityToken = getActivityToken();
        restoreHasCurrentPermissionRequest(icicle);
        onCreate(icicle);
        Log.d(TAG, "performCreate Call Injection manager");
        this.mInjectionManager = InjectionManager.getInstance();
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONCREATE, this, icicle, false);
        }
        if ((this instanceof PreferenceActivity) && this.mInjectionManager != null) {
            this.mInjectionManager.dispatchPreferences((PreferenceActivity) this);
        }
        this.mActivityTransitionState.readState(icicle);
        performCreateCommon();
    }

    final void performCreate(Bundle icicle, PersistableBundle persistentState) {
        restoreHasCurrentPermissionRequest(icicle);
        onCreate(icicle, persistentState);
        this.mActivityTransitionState.readState(icicle);
        performCreateCommon();
    }

    final void performStart() {
        this.mMainThread.mLastIntendedActivityToken = getActivityToken();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
        this.mFragments.noteStateNotSaved();
        this.mCalled = false;
        this.mFragments.execPendingActions();
        this.mInstrumentation.callActivityOnStart(this);
        if (this.mCalled) {
            if (this.mInjectionManager != null) {
                this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONSTART, this.myName, (Object) this);
            }
            this.mFragments.dispatchStart();
            this.mFragments.reportLoaderStart();
            this.mActivityTransitionState.enterReady(this);
            return;
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onStart()");
    }

    final void performRestart() {
        this.mMainThread.mLastIntendedActivityToken = getActivityToken();
        this.mFragments.noteStateNotSaved();
        if (this.mStopped) {
            this.mStopped = false;
            if (this.mToken != null && this.mParent == null) {
                WindowManagerGlobal.getInstance().setStoppedState(this.mToken, false);
            }
            synchronized (this.mManagedCursors) {
                int N = this.mManagedCursors.size();
                for (int i = 0; i < N; i++) {
                    ManagedCursor mc = (ManagedCursor) this.mManagedCursors.get(i);
                    if (mc.mReleased || mc.mUpdated) {
                        if (mc.mCursor.requery() || getApplicationInfo().targetSdkVersion < 14) {
                            mc.mReleased = false;
                            mc.mUpdated = false;
                        } else {
                            throw new IllegalStateException("trying to requery an already closed cursor  " + mc.mCursor);
                        }
                    }
                }
            }
            this.mCalled = false;
            this.mInstrumentation.callActivityOnRestart(this);
            if (this.mCalled) {
                if (this.mInjectionManager != null) {
                    this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONRESTART, this.myName, (Object) this);
                }
                performStart();
                return;
            }
            throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onRestart()");
        }
    }

    final void performResume() {
        performRestart();
        this.mFragments.execPendingActions();
        this.mLastNonConfigurationInstances = null;
        this.mCalled = false;
        this.mInstrumentation.callActivityOnResume(this);
        if (this.mCalled) {
            if (!(this.mVisibleFromClient || this.mFinished)) {
                Log.w(TAG, "An activity without a UI must call finish() before onResume() completes");
                if (getApplicationInfo().targetSdkVersion > 22) {
                    throw new IllegalStateException("Activity " + this.mComponent.toShortString() + " did not call finish() prior to onResume() completing");
                }
            }
            this.mCalled = false;
            if (this.mInjectionManager != null) {
                this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONRESUME, this.myName, (Object) this);
            }
            this.mFragments.dispatchResume();
            this.mFragments.execPendingActions();
            onPostResume();
            if (!this.mCalled) {
                throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onPostResume()");
            }
            return;
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onResume()");
    }

    final void performPause() {
        this.mDoReportFullyDrawn = false;
        this.mFragments.dispatchPause();
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONPAUSE, this.myName, (Object) this);
        }
        this.mCalled = false;
        onPause();
        this.mResumed = false;
        if (this.mCalled || getApplicationInfo().targetSdkVersion < 9) {
            this.mResumed = false;
            return;
        }
        throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onPause()");
    }

    final void performUserLeaving() {
        onUserInteraction();
        onUserLeaveHint();
    }

    final void performStop() {
        this.mDoReportFullyDrawn = false;
        this.mFragments.doLoaderStop(this.mChangingConfigurations);
        if (!this.mStopped) {
            if (this.mWindow != null) {
                this.mWindow.closeAllPanels();
            }
            if (this.mToken != null && this.mParent == null) {
                WindowManagerGlobal.getInstance().setStoppedState(this.mToken, true);
            }
            this.mFragments.dispatchStop();
            if (this.mInjectionManager != null) {
                this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONSTOP, this.myName, (Object) this);
            }
            this.mCalled = false;
            this.mInstrumentation.callActivityOnStop(this);
            if (this.mCalled) {
                synchronized (this.mManagedCursors) {
                    int N = this.mManagedCursors.size();
                    for (int i = 0; i < N; i++) {
                        ManagedCursor mc = (ManagedCursor) this.mManagedCursors.get(i);
                        if (!mc.mReleased) {
                            mc.mCursor.deactivate();
                            mc.mReleased = true;
                        }
                    }
                }
                this.mStopped = true;
            } else {
                throw new SuperNotCalledException("Activity " + this.mComponent.toShortString() + " did not call through to super.onStop()");
            }
        }
        this.mResumed = false;
    }

    final void performDestroy() {
        this.mDestroyed = true;
        this.mWindow.destroy();
        this.mFragments.dispatchDestroy();
        if (this.mInjectionManager != null) {
            this.mInjectionManager.dispatchParentCallToFeature(DispatchParentCall.ONDESTROY, this.myName, (Object) this);
        }
        onDestroy();
        this.mFragments.doLoaderDestroy();
        if (this.mVoiceInteractor != null) {
            this.mVoiceInteractor.detachActivity();
        }
    }

    public final boolean isResumed() {
        return this.mResumed;
    }

    private void storeHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null && this.mHasCurrentPermissionsRequest) {
            bundle.putBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, true);
        }
    }

    private void restoreHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null) {
            this.mHasCurrentPermissionsRequest = bundle.getBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, false);
        }
    }

    void dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        this.mFragments.noteStateNotSaved();
        if (who == null) {
            onActivityResult(requestCode, resultCode, data);
        } else if (who.startsWith(REQUEST_PERMISSIONS_WHO_PREFIX)) {
            who = who.substring(REQUEST_PERMISSIONS_WHO_PREFIX.length());
            if (TextUtils.isEmpty(who)) {
                dispatchRequestPermissionsResult(requestCode, data);
                return;
            }
            frag = this.mFragments.findFragmentByWho(who);
            if (frag != null) {
                dispatchRequestPermissionsResultToFragment(requestCode, data, frag);
            }
        } else if (who.startsWith("@android:view:")) {
            Iterator i$ = WindowManagerGlobal.getInstance().getRootViews(getActivityToken()).iterator();
            while (i$.hasNext()) {
                ViewRootImpl viewRoot = (ViewRootImpl) i$.next();
                if (viewRoot.getView() != null && viewRoot.getView().dispatchActivityResult(who, requestCode, resultCode, data)) {
                    return;
                }
            }
        } else {
            frag = this.mFragments.findFragmentByWho(who);
            if (frag != null) {
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void startLockTask() {
        try {
            ActivityManagerNative.getDefault().startLockTaskMode(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public void stopLockTask() {
        try {
            ActivityManagerNative.getDefault().stopLockTaskMode();
        } catch (RemoteException e) {
        }
    }

    public void showLockTaskEscapeMessage() {
        try {
            ActivityManagerNative.getDefault().showLockTaskEscapeMessage(this.mToken);
        } catch (RemoteException e) {
        }
    }

    private void dispatchRequestPermissionsResult(int requestCode, Intent data) {
        this.mHasCurrentPermissionsRequest = false;
        onRequestPermissionsResult(requestCode, data != null ? data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES) : new String[0], data != null ? data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS) : new int[0]);
    }

    private void dispatchRequestPermissionsResultToFragment(int requestCode, Intent data, Fragment fragment) {
        fragment.onRequestPermissionsResult(requestCode, data != null ? data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES) : new String[0], data != null ? data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS) : new int[0]);
    }

    public void preventTabsEmbeddedOnActionBar() {
        if (mIsAmericano) {
            this.mPreventEmbeddedTabs = true;
        } else {
            Log.d(TAG, "You can only use this API in maintain release project.");
        }
    }

    public boolean checkNoEmbeddedTabs() {
        return this.mPreventEmbeddedTabs;
    }

    public void setActionBarTabSize(float height) {
        if (mIsAmericano) {
            this.mStackedHeight = (int) height;
        } else {
            Log.d(TAG, "You can only use this API in maintain release project.");
        }
    }

    public int getActionBarTabSize() {
        return this.mStackedHeight;
    }

    public void onMultiWindowStyleChanged(MultiWindowStyle style, int notifyReason) {
        getWindow().onMultiWindowStyleChanged(style, notifyReason);
    }

    public Bundle getWindowInfo() {
        MultiWindowFacade mwFacade = (MultiWindowFacade) getSystemService(Context.MULTIWINDOW_FACADE_SERVICE);
        Bundle bundle = new Bundle();
        if (mwFacade != null) {
            Rect rect = mwFacade.getStackBound(this.mToken);
            bundle.putParcelable(Intent.EXTRA_WINDOW_DEFAULT_SIZE, rect);
            bundle.putParcelable(Intent.EXTRA_WINDOW_LAST_SIZE, rect);
        } else {
            Log.w(TAG, "getWindowInfo(), mwFacade is null");
        }
        return bundle;
    }

    public void onMultiWindowConfigurationChanged(int configDiff) {
        getWindow().onMultiWindowConfigurationChanged(configDiff);
    }

    public void onMultiWindowFocusChanged(int notifyReason, boolean focus, boolean keepInputMethod) {
        getWindow().onMultiWindowFocusChanged(notifyReason, focus, keepInputMethod);
    }

    public void exitByCloseBtn() {
        getWindow().exitByCloseBtn();
    }

    public int getPreferredOrientation() {
        if (this.mMultiWindowStyle.getAppRequestOrientation() != -1) {
            return this.mMultiWindowStyle.getAppRequestOrientation();
        }
        return this.mPreferredOrientation;
    }

    public boolean isWindowAdded() {
        return this.mWindowAdded;
    }

    public Window makeNewWindow(Context context, boolean needMultiPhoneWindow) {
        if (needMultiPhoneWindow) {
            return new MultiPhoneWindow(context);
        }
        Window win = new PhoneWindow(context);
        WindowManager.LayoutParams attributes = win.getAttributes();
        attributes.multiWindowFlags |= 16;
        return win;
    }

    public boolean isSelectiveOrientationState() {
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || this.mMainThread == null) {
            return false;
        }
        return this.mMainThread.isSelectiveOrientationState(this.mToken);
    }

    public boolean isFixedOrientationCascade() {
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || this.mMainThread == null) {
            return false;
        }
        return this.mMainThread.isFixedOrientationCascade(this.mToken);
    }

    public Configuration getOverrideConfiguration() {
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || this.mMainThread == null) {
            return null;
        }
        return this.mMainThread.getOverrideConfiguration(this.mToken);
    }

    public void setSubContentView(int layoutResID) {
    }

    public void setSubContentView(View view) {
    }

    private void addSubWindow() {
    }

    public Window getSubWindow() {
        return null;
    }

    public void setScreenChangeListener(ScreenChangeListener listener) {
        if (DEBUG_DUALSCREEN) {
            Slog.v(TAG, "setScreenChangeListener() : " + this + " class=" + getClass().getSimpleName() + " caller=" + Debug.getCaller());
        }
        this.mScreenChangeListener = listener;
    }

    public void onDisplayChanged(Display display) {
    }

    public void setExpandRequestListener(ExpandRequestListener listener) {
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "setExpandRequestListener() : " + this.mComponent.toShortString() + " listener=" + listener);
        }
        this.mExpandRequestListener = listener;
    }

    public void setShrinkRequestListener(ShrinkRequestListener listener) {
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "setShrinkRequestListener() : " + this.mComponent.toShortString() + " listener=" + listener);
        }
        this.mShrinkRequestListener = listener;
    }

    public void onExpandRequested(int notifyReason) {
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "onExpandRequested() : " + this.mComponent.toShortString() + " notifyReason=" + notifyReason);
        }
        if (this.mExpandRequestListener != null) {
            this.mExpandRequestListener.onExpandRequested(notifyReason);
        }
    }

    public void onShrinkRequested(DualScreen toScreen, int notifyReason) {
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "onShrinkRequested() : " + this.mComponent.toShortString() + " toScreen=" + toScreen + " notifyReason=" + notifyReason);
        }
        if (this.mShrinkRequestListener != null) {
            this.mShrinkRequestListener.onShrinkRequested(toScreen, notifyReason);
        }
    }

    public boolean setCustomImage(FileDescriptor fd, int rotation) {
        boolean z = false;
        try {
            z = ActivityManagerNative.getDefault().setCustomImage(this.mToken, fd != null ? ParcelFileDescriptor.dup(fd) : null, rotation);
        } catch (RemoteException e) {
            Slog.e(TAG, "CustomStartingWindow failed to set custom image", e);
        } catch (IOException e2) {
            Slog.e(TAG, "CustomStartingWindow failed to make ParcelFileDescriptor", e2);
        }
        return z;
    }
}
