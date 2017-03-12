package android.inputmethodservice;

import android.R;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ResourcesManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PermissionInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodImpl;
import android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodSessionImpl;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Process;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.Slog;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class InputMethodService extends AbstractInputMethodService {
    public static final int BACK_DISPOSITION_DEFAULT = 0;
    public static final int BACK_DISPOSITION_WILL_DISMISS = 2;
    public static final int BACK_DISPOSITION_WILL_NOT_DISMISS = 1;
    static final boolean DEBUG = false;
    public static final int IME_ACTIVE = 1;
    public static final int IME_VISIBLE = 2;
    private static final String IS_3X4_KEYPAD = "AxT9IME.is3X4Keypad";
    private static final String IS_MOVABLE_KEYPAD = "AxT9IME.isMovableKeypad";
    private static final String IS_VISIBLE_WINDOW = "AxT9IME.isVisibleWindow";
    static final int MOVEMENT_DOWN = -1;
    static final int MOVEMENT_UP = -2;
    private static final String REQUEST_AXT9INFO = "RequestAxT9Info";
    private static final String REQUEST_AXT9INFO_CLOSE = "com.samsung.axt9info.close";
    private static final String RESPONSE_AXT9INFO = "ResponseAxT9Info";
    private static final String RESPONSE_AXT9INFO_TYPE_CHANGED = "ResponseAxT9InfoTypeChanged";
    private static final boolean SAFE_DEBUG;
    static final String TAG = "InputMethodService";
    protected static boolean mFloatingForMultiWindow;
    private static boolean mIsJanpaneseModel;
    private static boolean mIsTabletDevice;
    final OnClickListener mActionClickListener = new OnClickListener() {
        public void onClick(View v) {
            EditorInfo ei = InputMethodService.this.getCurrentInputEditorInfo();
            InputConnection ic = InputMethodService.this.getCurrentInputConnection();
            if (ei != null && ic != null) {
                if (ei.actionId != 0) {
                    ic.performEditorAction(ei.actionId);
                } else if ((ei.imeOptions & 255) != 1) {
                    ic.performEditorAction(ei.imeOptions & 255);
                }
            }
        }
    };
    private BroadcastReceiver mBR = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(InputMethodService.TAG, "mBR.onReceive()");
            if (intent.getAction().equals(InputMethodService.REQUEST_AXT9INFO)) {
                Intent respInt = new Intent();
                respInt.setAction(InputMethodService.RESPONSE_AXT9INFO);
                respInt.putExtra(InputMethodService.IS_VISIBLE_WINDOW, InputMethodService.this.getIsVisibleWindow());
                respInt.putExtra(InputMethodService.IS_MOVABLE_KEYPAD, InputMethodService.this.isMovable());
                respInt.putExtra(InputMethodService.IS_3X4_KEYPAD, InputMethodService.this.is34Keypad());
                InputMethodService.this.sendBroadcast(respInt);
            } else if (intent.getAction().equals(InputMethodService.REQUEST_AXT9INFO_CLOSE) && InputMethodService.this.isInputViewShown()) {
                InputMethodService.this.requestHideSelf(0);
            }
        }
    };
    int mBackDisposition;
    private int mCachedAccessoryKeyboardState = 0;
    FrameLayout mCandidatesFrame;
    boolean mCandidatesViewStarted;
    int mCandidatesVisibility;
    CompletionInfo[] mCurCompletions;
    ViewGroup mExtractAccessories;
    Button mExtractAction;
    ExtractEditText mExtractEditText;
    FrameLayout mExtractFrame;
    View mExtractView;
    boolean mExtractViewHidden;
    ExtractedText mExtractedText;
    int mExtractedToken;
    boolean mFullscreenApplied;
    ViewGroup mFullscreenArea;
    boolean mHardwareAccelerated;
    InputMethodManager mImm;
    boolean mInShowWindow;
    LayoutInflater mInflater;
    boolean mInitialized;
    InputBinding mInputBinding;
    InputConnection mInputConnection;
    EditorInfo mInputEditorInfo;
    FrameLayout mInputFrame;
    boolean mInputStarted;
    View mInputView;
    boolean mInputViewStarted;
    final OnComputeInternalInsetsListener mInsetsComputer = new OnComputeInternalInsetsListener() {
        public void onComputeInternalInsets(InternalInsetsInfo info) {
            if (InputMethodService.this.isExtractViewShown()) {
                View decor = InputMethodService.this.getWindow().getWindow().getDecorView();
                Rect rect = info.contentInsets;
                Rect rect2 = info.visibleInsets;
                int height = decor.getHeight();
                rect2.top = height;
                rect.top = height;
                info.touchableRegion.setEmpty();
                info.setTouchableInsets(0);
            } else {
                InputMethodService.this.onComputeInsets(InputMethodService.this.mTmpInsets);
                info.contentInsets.top = InputMethodService.this.mTmpInsets.contentTopInsets;
                info.visibleInsets.top = InputMethodService.this.mTmpInsets.visibleTopInsets;
                info.touchableRegion.set(InputMethodService.this.mTmpInsets.touchableRegion);
                info.setTouchableInsets(InputMethodService.this.mTmpInsets.touchableInsets);
            }
            InputMethodService.this.visibleTop = info.visibleInsets.top;
        }
    };
    protected boolean mIs34Keypad = false;
    boolean mIsFullscreen;
    boolean mIsInputViewShown;
    protected boolean mIsMovable = false;
    boolean mIsVisibleWindow;
    boolean mLastShowInputRequested;
    View mRootView;
    boolean mShouldClearInsetOfPreviousIme;
    int mShowInputFlags;
    boolean mShowInputForced;
    boolean mShowInputRequested;
    boolean mSideSyncShowInputRequested;
    InputConnection mStartedInputConnection;
    int mStatusIcon;
    int mTheme = 0;
    TypedArray mThemeAttrs;
    final Insets mTmpInsets = new Insets();
    final int[] mTmpLocation = new int[2];
    IBinder mToken;
    protected boolean mWACOMPen = false;
    SoftInputWindow mWindow;
    boolean mWindowAdded;
    boolean mWindowCreated;
    boolean mWindowVisible;
    boolean mWindowWasVisible;
    boolean minimized = false;
    boolean needSetlayout = false;
    int visibleTop = 0;

    public class InputMethodImpl extends AbstractInputMethodImpl {
        public InputMethodImpl() {
            super();
        }

        public void updateWacomState(int value) {
            if (value == 1) {
                InputMethodService.this.mWACOMPen = true;
            } else {
                InputMethodService.this.mWACOMPen = false;
            }
        }

        public void updateFloatingState(int value) {
            if (value == 1) {
                InputMethodService.mFloatingForMultiWindow = true;
            } else {
                InputMethodService.mFloatingForMultiWindow = false;
            }
        }

        public void attachToken(IBinder token) {
            if (InputMethodService.this.mToken == null) {
                InputMethodService.this.mToken = token;
                InputMethodService.this.mWindow.setToken(token);
            }
        }

        public void bindInput(InputBinding binding) {
            InputMethodService.this.mInputBinding = binding;
            InputMethodService.this.mInputConnection = binding.getConnection();
            InputConnection ic = InputMethodService.this.getCurrentInputConnection();
            if (ic != null) {
                ic.reportFullscreenMode(InputMethodService.this.mIsFullscreen);
            }
            InputMethodService.this.initialize();
            InputMethodService.this.onBindInput();
        }

        public void unbindInput() {
            InputMethodService.this.onUnbindInput();
            InputMethodService.this.mInputBinding = null;
            InputMethodService.this.mInputConnection = null;
        }

        public void startInput(InputConnection ic, EditorInfo attribute) {
            InputMethodService.this.doStartInput(ic, attribute, false);
        }

        public void restartInput(InputConnection ic, EditorInfo attribute) {
            InputMethodService.this.doStartInput(ic, attribute, true);
        }

        public void hideSoftInput(int flags, ResultReceiver resultReceiver) {
            boolean wasVis = InputMethodService.this.isInputViewShown();
            InputMethodService.this.mShowInputFlags = 0;
            InputMethodService.this.mShowInputRequested = false;
            InputMethodService.this.mShowInputForced = false;
            InputMethodService.this.doHideWindow();
            InputMethodService.this.clearInsetOfPreviousIme();
            if (resultReceiver != null) {
                int i = wasVis != InputMethodService.this.isInputViewShown() ? 3 : wasVis ? 0 : 1;
                resultReceiver.send(i, null);
            }
            InputMethodService.this.setPressBtnSIPOnOff(false);
        }

        public void minimizeSoftInput(int height) {
            if (!Secure.getString(InputMethodService.this.getContentResolver(), Secure.DEFAULT_INPUT_METHOD).equals("com.sec.android.inputmethod/.SamsungKeypad") && InputMethodService.this.mImm != null && InputMethodService.this.mToken != null) {
                InputMethodService.this.mImm.hideSoftInputFromInputMethod(InputMethodService.this.mToken, 0);
            } else if (InputMethodService.this.mInputView == null || InputMethodService.this.mCandidatesFrame == null) {
                Log.e(InputMethodService.TAG, "mInputView or mCandidatesFrame is null in minimizeSoftInput");
            } else if (!InputMethodService.this.mInputView.isShown()) {
                Log.e(InputMethodService.TAG, "Keyboard is not showing so minimizeSoftInput not working.");
            } else if (InputMethodService.this.mInputView.getHeight() == 0) {
                Log.v(InputMethodService.TAG, "height is 0");
            } else {
                if (height <= 0 || height > 253) {
                    height = 22;
                }
                height = (int) TypedValue.applyDimension(1, (float) height, InputMethodService.this.getResources().getDisplayMetrics());
                InputMethodService.this.minimized = true;
                InputMethodService.this.mWindow.setMinimizeFlag(InputMethodService.this.minimized);
                InputMethodService.this.doMinimizeSoftInput(height);
            }
        }

        public void unMinimizeSoftInput() {
            InputMethodService.this.undoMinimizeSoftInput();
        }

        public void showSoftInput(int flags, ResultReceiver resultReceiver) {
            int i;
            int i2 = 2;
            boolean wasVis = InputMethodService.this.isInputViewShown();
            InputMethodService.this.mShowInputFlags = 0;
            if (InputMethodService.this.needSetlayout) {
                InputMethodService.this.mWindow.getWindow().setLayout(-1, -2);
                InputMethodService.this.undoMinimizeSoftInput();
                InputMethodService.this.needSetlayout = false;
            }
            if (InputMethodService.this.onShowInputRequested(flags, false)) {
                try {
                    InputMethodService.this.showWindow(true);
                } catch (BadTokenException e) {
                    InputMethodService.this.mWindowVisible = false;
                    InputMethodService.this.mWindowAdded = false;
                }
            }
            InputMethodService.this.clearInsetOfPreviousIme();
            boolean showing = InputMethodService.this.isInputViewShown();
            InputMethodManager inputMethodManager = InputMethodService.this.mImm;
            IBinder iBinder = InputMethodService.this.mToken;
            if (showing) {
                i = 2;
            } else {
                i = 0;
            }
            inputMethodManager.setImeWindowStatus(iBinder, i | 1, InputMethodService.this.mBackDisposition);
            if (resultReceiver != null) {
                if (wasVis == InputMethodService.this.isInputViewShown()) {
                    i2 = wasVis ? 0 : 1;
                }
                resultReceiver.send(i2, null);
            }
            InputMethodService.this.setPressBtnSIPOnOff(false);
        }

        public void changeInputMethodSubtype(InputMethodSubtype subtype) {
            InputMethodService.this.onCurrentInputMethodSubtypeChanged(subtype);
        }
    }

    public class InputMethodSessionImpl extends AbstractInputMethodSessionImpl {
        public InputMethodSessionImpl() {
            super();
        }

        public void finishInput() {
            if (isEnabled()) {
                InputMethodService.this.doFinishInput();
            }
        }

        public void displayCompletions(CompletionInfo[] completions) {
            if (isEnabled()) {
                InputMethodService.this.mCurCompletions = completions;
                InputMethodService.this.onDisplayCompletions(completions);
            }
        }

        public void updateExtractedText(int token, ExtractedText text) {
            if (isEnabled()) {
                InputMethodService.this.onUpdateExtractedText(token, text);
            }
        }

        public void updateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
            if (isEnabled()) {
                InputMethodService.this.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
            }
        }

        public void viewClicked(boolean focusChanged) {
            if (isEnabled()) {
                if (InputMethodService.this.minimized) {
                    InputMethodService.this.undoMinimizeSoftInput();
                    InputMethodService.this.mWindow.getWindow().setLayout(-1, -2);
                    InputMethodService.this.minimized = false;
                    InputMethodService.this.mWindow.setMinimizeFlag(InputMethodService.this.minimized);
                }
                InputMethodService.this.onViewClicked(focusChanged);
            }
        }

        public void updateCursor(Rect newCursor) {
            if (isEnabled()) {
                InputMethodService.this.onUpdateCursor(newCursor);
            }
        }

        public void appPrivateCommand(String action, Bundle data) {
            if (isEnabled()) {
                InputMethodService.this.onAppPrivateCommand(action, data);
            }
        }

        public void toggleSoftInput(int showFlags, int hideFlags) {
            InputMethodService.this.onToggleSoftInput(showFlags, hideFlags);
        }

        public void updateCursorAnchorInfo(CursorAnchorInfo info) {
            if (isEnabled()) {
                InputMethodService.this.onUpdateCursorAnchorInfo(info);
            }
        }

        public void showSideSyncSoftInput(int showFlags) {
            InputMethodService.this.onShowSideSyncSoftInput(showFlags);
        }
    }

    public static final class Insets {
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        public int contentTopInsets;
        public int touchableInsets;
        public final Region touchableRegion = new Region();
        public int visibleTopInsets;
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        SAFE_DEBUG = z;
    }

    private void setFlagIsVisibleWindow(boolean isVisibleWindow) {
        this.mIsVisibleWindow = isVisibleWindow;
    }

    private boolean getIsVisibleWindow() {
        return this.mIsVisibleWindow;
    }

    private void sendInputViewShownState() {
        Intent respInt = new Intent();
        respInt.setAction(RESPONSE_AXT9INFO);
        respInt.putExtra(IS_VISIBLE_WINDOW, getIsVisibleWindow());
        respInt.putExtra(IS_MOVABLE_KEYPAD, isMovable());
        respInt.putExtra(IS_3X4_KEYPAD, is34Keypad());
        respInt.putExtra("PID", Process.myPid());
        sendBroadcast(respInt);
    }

    public void setTheme(int theme) {
        if (this.mWindow != null) {
            throw new IllegalStateException("Must be called before onCreate()");
        }
        this.mTheme = theme;
    }

    public boolean enableHardwareAcceleration() {
        if (this.mWindow != null) {
            throw new IllegalStateException("Must be called before onCreate()");
        } else if (!ActivityManager.isHighEndGfx()) {
            return false;
        } else {
            this.mHardwareAccelerated = true;
            return true;
        }
    }

    private void getSecDeviceInfo() {
        String deviceType = SystemProperties.get("ro.build.characteristics");
        if (deviceType != null && deviceType.contains("tablet")) {
            mIsTabletDevice = true;
        }
        if (SystemProperties.get("ro.csc.country_code").equals("JP")) {
            mIsJanpaneseModel = true;
        }
    }

    public void onCreate() {
        boolean z;
        this.mTheme = Resources.selectSystemTheme(this.mTheme, getApplicationInfo().targetSdkVersion, R.style.Theme_InputMethod, R.style.Theme_Holo_InputMethod, R.style.Theme_DeviceDefault_InputMethod, R.style.Theme_DeviceDefault_InputMethod);
        super.setTheme(this.mTheme);
        super.onCreate();
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.mImm.getInputMethodWindowVisibleHeight() > 0) {
            z = true;
        } else {
            z = false;
        }
        this.mShouldClearInsetOfPreviousIme = z;
        this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWindow = new SoftInputWindow(this, "InputMethod", this.mTheme, null, null, this.mDispatcherState, 2011, 80, false);
        if (this.mHardwareAccelerated) {
            this.mWindow.getWindow().addFlags(16777216);
        }
        initViews();
        this.mWindow.getWindow().setLayout(-1, -2);
        undoMinimizeSoftInput();
        getSecDeviceInfo();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REQUEST_AXT9INFO);
        filter.addAction(REQUEST_AXT9INFO_CLOSE);
        registerReceiver(this.mBR, filter);
        Log.d(TAG, "mBR.registerReceiver()");
    }

    public void onInitializeInterface() {
    }

    void initialize() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mIsFullscreen = false;
            onInitializeInterface();
        }
    }

    void initViews() {
        this.mInitialized = false;
        this.mWindowCreated = false;
        this.mShowInputRequested = false;
        this.mShowInputForced = false;
        this.mThemeAttrs = obtainStyledAttributes(R.styleable.InputMethodService);
        this.mRootView = this.mInflater.inflate(17367164, null);
        this.mRootView.setSystemUiVisibility(768);
        this.mWindow.setContentView(this.mRootView);
        this.mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsComputer);
        this.mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsComputer);
        if (Global.getInt(getContentResolver(), Global.FANCY_IME_ANIMATIONS, 0) != 0) {
            this.mWindow.getWindow().setWindowAnimations(16974567);
        }
        this.mFullscreenArea = (ViewGroup) this.mRootView.findViewById(16909273);
        this.mExtractViewHidden = false;
        this.mExtractFrame = (FrameLayout) this.mRootView.findViewById(R.id.extractArea);
        this.mExtractView = null;
        this.mExtractEditText = null;
        this.mExtractAccessories = null;
        this.mExtractAction = null;
        this.mFullscreenApplied = false;
        this.mCandidatesFrame = (FrameLayout) this.mRootView.findViewById(R.id.candidatesArea);
        this.mInputFrame = (FrameLayout) this.mRootView.findViewById(R.id.inputArea);
        this.mInputView = null;
        this.mIsInputViewShown = false;
        this.mExtractFrame.setVisibility(8);
        this.mCandidatesVisibility = getCandidatesHiddenVisibility();
        this.mCandidatesFrame.setVisibility(this.mCandidatesVisibility);
        this.mInputFrame.setVisibility(8);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsComputer);
        doFinishInput();
        if (this.mWindowAdded && this.mWindow != null) {
            this.mWindow.getWindow().setWindowAnimations(0);
            this.mWindow.dismiss();
        }
        unregisterReceiver(this.mBR);
        Log.d(TAG, "mBR.unregisterReceiver()");
    }

    public void onConfiguratinChangedForAllDisplays(Configuration[] newConfigs) {
        if (SAFE_DEBUG) {
            for (int i = 0; i < newConfigs.length; i++) {
                Slog.d(TAG, "onConfiguration - newConfigs[" + i + "]=" + newConfigs[i]);
            }
        }
        int index = this.mImm.getCurrentFocusDisplayID();
        if (SAFE_DEBUG) {
            Slog.d(TAG, "onConfiguration - getCurrentFocusDisplayID()==" + index);
        }
        if (index != -1 && newConfigs[index] != null) {
            if (SAFE_DEBUG) {
                Slog.d(TAG, "onConfiguration - newConfigs[" + index + "] will be applied.(" + newConfigs[index] + ")");
            }
            ResourcesManager.getInstance().applyConfigurationToResourcesLocked(newConfigs[index], null, true);
            onConfigurationChanged(newConfigs[index]);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean visible = this.mWindowVisible;
        int showFlags = this.mShowInputFlags;
        boolean showingInput = this.mShowInputRequested;
        CompletionInfo[] completions = this.mCurCompletions;
        if (isExtractViewShown() && (this.mExtractView instanceof ExtractEditLayout)) {
            ExtractEditLayout extractEditLayout = (ExtractEditLayout) this.mExtractView;
        }
        initViews();
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
        if (this.mInputStarted) {
            doStartInput(getCurrentInputConnection(), getCurrentInputEditorInfo(), true);
        }
        if (visible) {
            if (showingInput) {
                if (onShowInputRequested(showFlags, true)) {
                    showWindow(true);
                    if (completions != null) {
                        this.mCurCompletions = completions;
                        onDisplayCompletions(completions);
                    }
                } else {
                    doHideWindow();
                }
            } else if (this.mCandidatesVisibility == 0) {
                showWindow(false);
            } else {
                doHideWindow();
            }
            boolean showing = onEvaluateInputViewShown();
            if (this.mWindowVisible) {
                int i;
                InputMethodManager inputMethodManager = this.mImm;
                IBinder iBinder = this.mToken;
                if (showing) {
                    i = 2;
                } else {
                    i = 0;
                }
                inputMethodManager.setImeWindowStatus(iBinder, i | 1, this.mBackDisposition);
                return;
            }
            return;
        }
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.reportFullscreenMode(this.mIsFullscreen);
        }
    }

    private void checkandshowInputMehtodPicker() {
        this.mImm.dismissAndShowAgainInputMethodPicker();
    }

    public AbstractInputMethodImpl onCreateInputMethodInterface() {
        return new InputMethodImpl();
    }

    public AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface() {
        return new InputMethodSessionImpl();
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public Dialog getWindow() {
        return this.mWindow;
    }

    public void setBackDisposition(int disposition) {
        this.mBackDisposition = disposition;
    }

    public int getBackDisposition() {
        return this.mBackDisposition;
    }

    public int getMaxWidth() {
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public InputBinding getCurrentInputBinding() {
        return this.mInputBinding;
    }

    public InputConnection getCurrentInputConnection() {
        InputConnection ic = this.mStartedInputConnection;
        return ic != null ? ic : this.mInputConnection;
    }

    public boolean getCurrentInputStarted() {
        return this.mInputStarted;
    }

    public EditorInfo getCurrentInputEditorInfo() {
        return this.mInputEditorInfo;
    }

    public void updateFullscreenMode() {
        boolean isFullscreen;
        boolean changed;
        boolean z = true;
        if (this.mShowInputRequested && onEvaluateFullscreenMode()) {
            isFullscreen = true;
        } else {
            isFullscreen = false;
        }
        if (this.mLastShowInputRequested != this.mShowInputRequested) {
            changed = true;
        } else {
            changed = false;
        }
        if (!(this.mIsFullscreen == isFullscreen && this.mFullscreenApplied)) {
            changed = true;
            this.mIsFullscreen = isFullscreen;
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.reportFullscreenMode(isFullscreen);
            }
            this.mFullscreenApplied = true;
            initialize();
            LayoutParams lp = (LayoutParams) this.mFullscreenArea.getLayoutParams();
            if (isFullscreen) {
                this.mFullscreenArea.setBackgroundDrawable(this.mThemeAttrs.getDrawable(0));
                lp.height = 0;
                lp.weight = 1.0f;
            } else {
                this.mFullscreenArea.setBackgroundDrawable(null);
                lp.height = -2;
                lp.weight = 0.0f;
            }
            undoMinimizeSoftInput();
            ((ViewGroup) this.mFullscreenArea.getParent()).updateViewLayout(this.mFullscreenArea, lp);
            if (isFullscreen) {
                if (this.mExtractView == null) {
                    View v = onCreateExtractTextView();
                    if (v != null) {
                        setExtractView(v);
                    }
                }
                startExtractingText(false);
            }
            updateExtractFrameVisibility();
        }
        if (changed) {
            Window window = this.mWindow.getWindow();
            if (this.mShowInputRequested) {
                z = false;
            }
            onConfigureWindow(window, isFullscreen, z);
            this.mLastShowInputRequested = this.mShowInputRequested;
        }
    }

    public void onConfigureWindow(Window win, boolean isFullscreen, boolean isCandidatesOnly) {
        int currentHeight = this.mWindow.getWindow().getAttributes().height;
        int newHeight = isFullscreen ? -1 : -2;
        if (this.mIsInputViewShown && currentHeight != newHeight) {
            Log.w(TAG, "Window size has been changed. This may cause jankiness of resizing window: " + currentHeight + " -> " + newHeight);
        }
        if (!this.needSetlayout) {
            undoMinimizeSoftInput();
            this.mWindow.getWindow().setLayout(-1, newHeight);
        }
    }

    public boolean isFullscreenMode() {
        return this.mIsFullscreen;
    }

    public boolean onEvaluateFullscreenMode() {
        if (getResources().getConfiguration().orientation != 2) {
            return false;
        }
        if (this.mInputEditorInfo != null && ((this.mInputEditorInfo.packageName.equals("com.android.mms") || this.mInputEditorInfo.packageName.equals("com.android.email")) && (this.mInputEditorInfo.inputType & 0) == 0 && (this.mInputEditorInfo.imeOptions & 0) == 0 && this.mInputEditorInfo.initialSelStart < 0 && this.mInputEditorInfo.initialSelEnd < 0)) {
            return false;
        }
        if (this.mInputEditorInfo != null && this.mInputEditorInfo.packageName.equals("com.facebook.katana") && (this.mInputEditorInfo.inputType & 0) == 0 && (this.mInputEditorInfo.inputType & 262144) != 0 && (this.mInputEditorInfo.imeOptions & 0) == 0) {
            return false;
        }
        if (this.mInputEditorInfo != null && this.mInputEditorInfo.packageName.equals("com.facebook.orca") && (this.mInputEditorInfo.inputType & 131072) != 0) {
            return false;
        }
        if (this.mInputEditorInfo != null && (this.mInputEditorInfo.imeOptions & 268435456) != 0) {
            return false;
        }
        if (this.mInputEditorInfo == null || (this.mInputEditorInfo.imeOptions & 33554432) == 0) {
            return true;
        }
        return false;
    }

    public void setExtractViewShown(boolean shown) {
        if (this.mExtractViewHidden == shown) {
            this.mExtractViewHidden = !shown;
            updateExtractFrameVisibility();
        }
    }

    public boolean isExtractViewShown() {
        return this.mIsFullscreen && !this.mExtractViewHidden;
    }

    void updateExtractFrameVisibility() {
        int vis;
        boolean z;
        int i = 1;
        if (isFullscreenMode()) {
            if (this.mExtractViewHidden) {
                vis = 4;
            } else {
                vis = 0;
            }
            this.mExtractFrame.setVisibility(vis);
        } else {
            vis = 0;
            this.mExtractFrame.setVisibility(8);
        }
        if (this.mCandidatesVisibility == 0) {
            z = true;
        } else {
            z = false;
        }
        updateCandidatesVisibility(z);
        if (this.mWindowWasVisible && this.mFullscreenArea.getVisibility() != vis) {
            TypedArray typedArray = this.mThemeAttrs;
            if (vis != 0) {
                i = 2;
            }
            int animRes = typedArray.getResourceId(i, 0);
            if (animRes != 0) {
                this.mFullscreenArea.startAnimation(AnimationUtils.loadAnimation(this, animRes));
            }
        }
        this.mFullscreenArea.setVisibility(vis);
    }

    public void onComputeInsets(Insets outInsets) {
        int[] loc = this.mTmpLocation;
        if (this.mInputFrame.getVisibility() == 0) {
            this.mInputFrame.getLocationInWindow(loc);
        } else {
            loc[1] = getWindow().getWindow().getDecorView().getHeight();
        }
        if (isFullscreenMode()) {
            outInsets.contentTopInsets = getWindow().getWindow().getDecorView().getHeight();
        } else {
            outInsets.contentTopInsets = loc[1];
        }
        if (this.mCandidatesFrame.getVisibility() == 0) {
            this.mCandidatesFrame.getLocationInWindow(loc);
        }
        outInsets.visibleTopInsets = loc[1];
        outInsets.touchableInsets = 2;
        outInsets.touchableRegion.setEmpty();
    }

    public void updateInputViewShown() {
        boolean isShown;
        int i = 0;
        if (this.mShowInputRequested && onEvaluateInputViewShown()) {
            isShown = true;
        } else {
            isShown = false;
        }
        if (this.mIsInputViewShown != isShown && this.mWindowVisible) {
            this.mIsInputViewShown = isShown;
            FrameLayout frameLayout = this.mInputFrame;
            if (!isShown) {
                i = 8;
            }
            frameLayout.setVisibility(i);
            if (this.mInputView == null) {
                initialize();
                View v = onCreateInputView();
                if (v != null) {
                    setInputView(v);
                }
            }
        }
    }

    public boolean isShowInputRequested() {
        return this.mShowInputRequested;
    }

    public boolean isInputViewShown() {
        return this.mIsInputViewShown && this.mWindowVisible;
    }

    private int getCachedAccessoryKeyboardState() {
        return this.mCachedAccessoryKeyboardState;
    }

    public boolean onEvaluateInputViewShown() {
        this.mCachedAccessoryKeyboardState = this.mImm.isAccessoryKeyboardState();
        if (getPressBtnSIPOnOff() && this.mCachedAccessoryKeyboardState != 0) {
            return true;
        }
        Configuration config = getResources().getConfiguration();
        if (this.mCachedAccessoryKeyboardState != 0 && !getOnScreenKeyboardSettingValue()) {
            return false;
        }
        if (config.keyboard == 1 || config.hardKeyboardHidden == 2 || config.keyboard == 3) {
            return true;
        }
        if (this.mCachedAccessoryKeyboardState == 0 || !getOnScreenKeyboardSettingValue()) {
            return false;
        }
        return true;
    }

    private boolean getOnScreenKeyboardSettingValue() {
        int defaultValue = 1;
        if (mIsTabletDevice) {
            defaultValue = 0;
        }
        if (Secure.getIntForUser(getContentResolver(), Secure.SHOW_IME_WITH_HARD_KEYBOARD, defaultValue, UserHandle.myUserId()) == 1) {
            return true;
        }
        return false;
    }

    public void setCandidatesViewShown(boolean shown) {
        updateCandidatesVisibility(shown);
        if (!this.mShowInputRequested && this.mWindowVisible != shown) {
            if (shown) {
                showWindow(false);
            } else {
                doHideWindow();
            }
        }
    }

    void updateCandidatesVisibility(boolean shown) {
        int vis = shown ? 0 : getCandidatesHiddenVisibility();
        if (this.mCandidatesVisibility != vis) {
            this.mCandidatesFrame.setVisibility(vis);
            this.mCandidatesVisibility = vis;
        }
    }

    public int getCandidatesHiddenVisibility() {
        return isExtractViewShown() ? 8 : 4;
    }

    public void showStatusIcon(int iconResId) {
        this.mStatusIcon = iconResId;
        this.mImm.showStatusIcon(this.mToken, getPackageName(), iconResId);
    }

    public void hideStatusIcon() {
        this.mStatusIcon = 0;
        this.mImm.hideStatusIcon(this.mToken);
    }

    public void switchInputMethod(String id) {
        this.mImm.setInputMethod(this.mToken, id);
    }

    public void setExtractView(View view) {
        this.mExtractFrame.removeAllViews();
        this.mExtractFrame.addView(view, new FrameLayout.LayoutParams(-1, -1));
        this.mExtractView = view;
        if (view != null) {
            this.mExtractEditText = (ExtractEditText) view.findViewById(R.id.inputExtractEditText);
            this.mExtractEditText.setIME(this);
            this.mExtractAction = (Button) view.findViewById(16909275);
            if (this.mExtractAction != null) {
                this.mExtractAccessories = (ViewGroup) view.findViewById(16909274);
                this.mExtractAccessories.setBackgroundColor(-1);
            }
            startExtractingText(false);
            this.mExtractView.setIME(this);
            this.mExtractEditText.setWritingBuddyEnabled(false);
            return;
        }
        this.mExtractEditText = null;
        this.mExtractAccessories = null;
        this.mExtractAction = null;
    }

    public void setCandidatesView(View view) {
        this.mCandidatesFrame.removeAllViews();
        this.mCandidatesFrame.addView(view, new FrameLayout.LayoutParams(-1, -2));
    }

    public void setInputView(View view) {
        this.mInputFrame.removeAllViews();
        this.mInputFrame.addView(view, new FrameLayout.LayoutParams(-1, -2));
        this.mInputView = view;
    }

    public View onCreateExtractTextView() {
        return this.mInflater.inflate(17367330, null);
    }

    public View onCreateCandidatesView() {
        return null;
    }

    public View onCreateInputView() {
        return null;
    }

    public void onStartInputView(EditorInfo info, boolean restarting) {
    }

    public void onFinishInputView(boolean finishingInput) {
        if (this.minimized) {
            Log.e(TAG, "hideWindow set minimized false");
            this.minimized = false;
            this.needSetlayout = true;
            this.mWindow.setMinimizeFlag(this.minimized);
        }
        if (!finishingInput) {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    public void onStartCandidatesView(EditorInfo info, boolean restarting) {
    }

    public void onFinishCandidatesView(boolean finishingInput) {
        if (!finishingInput) {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    public boolean onShowInputRequested(int flags, boolean configChange) {
        if (!onEvaluateInputViewShown()) {
            return false;
        }
        if ((flags & 1) == 0) {
            if (!configChange && onEvaluateFullscreenMode()) {
                return false;
            }
            Configuration config = getResources().getConfiguration();
            if (config.keyboard == 3) {
                if (this.mInputEditorInfo != null && this.mInputEditorInfo.inputType == 0) {
                    return false;
                }
            } else if (config.keyboard != 1) {
                return false;
            }
            if (getCachedAccessoryKeyboardState() != 0) {
                return false;
            }
        }
        if ((flags & 2) != 0) {
            this.mShowInputForced = true;
        }
        return true;
    }

    public void setIsMovable(boolean isMovable) {
        this.mIsMovable = isMovable;
    }

    public boolean isMovable() {
        return this.mIsMovable;
    }

    public void setIs34Keypad(boolean is34key) {
        this.mIs34Keypad = is34key;
    }

    private boolean is34Keypad() {
        return this.mIs34Keypad;
    }

    public void showWindow(boolean showInput) {
        if (this.mInShowWindow) {
            Log.w(TAG, "Re-entrance in to showWindow");
            return;
        }
        try {
            this.mWindowWasVisible = this.mWindowVisible;
            this.mInShowWindow = true;
            showWindowInner(showInput);
            if (this.mWindowVisible) {
                setFlagIsVisibleWindow(showInput);
                sendInputViewShownState();
            }
            if (this.mWindowVisible) {
                this.mWindowWasVisible = true;
            } else {
                this.mWindowWasVisible = false;
            }
            this.mInShowWindow = false;
        } catch (Throwable th) {
            if (this.mWindowVisible) {
                this.mWindowWasVisible = true;
            } else {
                this.mWindowWasVisible = false;
            }
            this.mInShowWindow = false;
        }
    }

    boolean isEmailInputType(EditorInfo attribute) {
        int variation = attribute.inputType & PermissionInfo.PROTECTION_MASK_FLAGS;
        if (variation == 160 || variation == 224 || variation == 208) {
            return true;
        }
        return false;
    }

    void showWindowInner(boolean showInput) {
        int i;
        int i2;
        int i3 = 2;
        boolean doShowInput = false;
        if (this.mWindowVisible) {
            i = 1;
        } else {
            i = 0;
        }
        if (isInputViewShown()) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        int previousImeWindowStatus = i | i2;
        this.mWindowVisible = true;
        if (!this.mShowInputRequested) {
            if (this.mInputStarted && showInput) {
                doShowInput = true;
                this.mShowInputRequested = true;
            }
        }
        initialize();
        try {
            updateFullscreenMode();
            updateInputViewShown();
            if (!(this.mWindowAdded && this.mWindowCreated)) {
                this.mWindowAdded = true;
                this.mWindowCreated = true;
                initialize();
                View v = onCreateCandidatesView();
                if (v != null) {
                    setCandidatesView(v);
                }
            }
            if (this.mSideSyncShowInputRequested || (getResources().getConfiguration().keyboard == 3 && "KOREA".equals(SystemProperties.get("ro.csc.country_code")) && isEmailInputType(this.mInputEditorInfo))) {
                Log.v(TAG, "SideSyncShowInputRequested true. CALL: onStartInput");
                onStartInput(this.mInputEditorInfo, false);
            }
            if (this.mShowInputRequested) {
                if (!this.mInputViewStarted || this.mSideSyncShowInputRequested || (getResources().getConfiguration().keyboard == 3 && "KOREA".equals(SystemProperties.get("ro.csc.country_code")) && isEmailInputType(this.mInputEditorInfo))) {
                    if (this.mSideSyncShowInputRequested) {
                        Log.v(TAG, "SideSyncShowInputRequested true. WILLCALL: onStartInputView");
                    }
                    this.mInputViewStarted = true;
                    onStartInputView(this.mInputEditorInfo, false);
                }
            } else if (!this.mCandidatesViewStarted || this.mSideSyncShowInputRequested || (getResources().getConfiguration().keyboard == 3 && "KOREA".equals(SystemProperties.get("ro.csc.country_code")) && isEmailInputType(this.mInputEditorInfo))) {
                this.mCandidatesViewStarted = true;
                onStartCandidatesView(this.mInputEditorInfo, false);
            }
            if (doShowInput) {
                startExtractingText(false);
            }
            if (System.getInt(getContentResolver(), "sidesync_tablet_connect", 0) == 1) {
                Log.e(TAG, "showWindow: sidesync_tablet_connect is connected!");
                this.mSideSyncShowInputRequested = false;
                return;
            }
            if (!isInputViewShown()) {
                i3 = 0;
            }
            int nextImeWindowStatus = i3 | 1;
            if (previousImeWindowStatus != nextImeWindowStatus) {
                this.mImm.setImeWindowStatus(this.mToken, nextImeWindowStatus, this.mBackDisposition);
            }
            if (((previousImeWindowStatus & 1) == 0 || this.mSideSyncShowInputRequested || false) && this.mToken != null) {
                onWindowShown();
                try {
                    this.mWindow.setToken(this.mToken);
                    this.mWindow.show();
                } catch (NumberFormatException e) {
                    Log.e(TAG, "showWindow: NumberFormatException occured!");
                    this.mWindowVisible = false;
                    this.mWindowAdded = false;
                } catch (IllegalArgumentException e2) {
                    Log.e(TAG, "showWindow: IllegalArgumentException occured!");
                    this.mWindowVisible = false;
                    this.mWindowAdded = false;
                } catch (BadTokenException e3) {
                    Log.e(TAG, "showWindow: BadTokenException occured!");
                    this.mWindowVisible = false;
                    this.mWindowAdded = false;
                }
                this.mShouldClearInsetOfPreviousIme = false;
            }
            this.mSideSyncShowInputRequested = false;
        } catch (IllegalArgumentException e4) {
            this.mWindowVisible = false;
            this.mWindowAdded = false;
            this.mWindowCreated = false;
            Log.e(TAG, "showWindowInner: IllegalArgumentException occured.");
            e4.printStackTrace();
        }
    }

    private void finishViews() {
        if (this.mInputViewStarted) {
            onFinishInputView(false);
        } else if (this.mCandidatesViewStarted) {
            onFinishCandidatesView(false);
        }
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
    }

    private void doHideWindow() {
        boolean isSideSyncSourceConnected = false;
        if (System.getInt(getContentResolver(), "sidesync_tablet_connect", 0) == 1) {
            sendBroadcast(new Intent("com.sec.android.sidesync.source.HIDE_SIP"));
            isSideSyncSourceConnected = true;
        }
        if (!isSideSyncSourceConnected || this.mWindowWasVisible) {
            this.mImm.setImeWindowStatus(this.mToken, 0, this.mBackDisposition);
        }
        hideWindow();
    }

    public void hideWindow() {
        boolean isSideSyncSourceConnected = false;
        if (System.getInt(getContentResolver(), "sidesync_tablet_connect", 0) == 1) {
            isSideSyncSourceConnected = true;
        }
        finishViews();
        if (this.mWindowVisible) {
            if (!isSideSyncSourceConnected || this.mWindowWasVisible) {
                this.mWindow.hide();
            }
            this.mWindowVisible = false;
            onWindowHidden();
            this.mWindowWasVisible = false;
        }
        setFlagIsVisibleWindow(false);
        sendInputViewShownState();
    }

    public void onWindowShown() {
    }

    public void onWindowHidden() {
    }

    private void clearInsetOfPreviousIme() {
        if (this.mShouldClearInsetOfPreviousIme && this.mWindow != null) {
            try {
                this.mWindow.show();
                this.mWindow.hide();
            } catch (BadTokenException e) {
                this.mWindowVisible = false;
                this.mWindowAdded = false;
            }
            this.mShouldClearInsetOfPreviousIme = false;
        }
    }

    public void onBindInput() {
    }

    public void onUnbindInput() {
    }

    public void onStartInput(EditorInfo attribute, boolean restarting) {
    }

    void doFinishInput() {
        if (this.mInputViewStarted) {
            onFinishInputView(true);
        } else if (this.mCandidatesViewStarted) {
            onFinishCandidatesView(true);
        }
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
        if (this.mInputStarted) {
            onFinishInput();
        }
        this.mInputStarted = false;
        this.mStartedInputConnection = null;
        this.mCurCompletions = null;
    }

    void doStartInput(InputConnection ic, EditorInfo attribute, boolean restarting) {
        if (!restarting) {
            doFinishInput();
        }
        this.mInputStarted = true;
        this.mStartedInputConnection = ic;
        this.mInputEditorInfo = attribute;
        initialize();
        onStartInput(attribute, restarting);
        if (!this.mWindowVisible) {
            return;
        }
        if (this.mShowInputRequested) {
            this.mInputViewStarted = true;
            onStartInputView(this.mInputEditorInfo, restarting);
            startExtractingText(true);
        } else if (this.mCandidatesVisibility == 0) {
            this.mCandidatesViewStarted = true;
            onStartCandidatesView(this.mInputEditorInfo, restarting);
        }
    }

    public void onFinishInput() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.finishComposingText();
        }
    }

    public void onDisplayCompletions(CompletionInfo[] completions) {
    }

    public void onUpdateExtractedText(int token, ExtractedText text) {
        if (this.mExtractedToken == token && text != null && this.mExtractEditText != null) {
            this.mExtractedText = text;
            this.mExtractEditText.setExtractedText(text);
        }
    }

    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        ExtractEditText eet = this.mExtractEditText;
        if (eet != null && isFullscreenMode() && this.mExtractedText != null) {
            int off = this.mExtractedText.startOffset;
            eet.startInternalChanges();
            newSelStart -= off;
            newSelEnd -= off;
            int len = eet.getText().length();
            if (newSelStart < 0) {
                newSelStart = 0;
            } else if (newSelStart > len) {
                newSelStart = len;
            }
            if (newSelEnd < 0) {
                newSelEnd = 0;
            } else if (newSelEnd > len) {
                newSelEnd = len;
            }
            eet.setSelection(newSelStart, newSelEnd);
            eet.finishInternalChanges();
        }
    }

    public void onViewClicked(boolean focusChanged) {
    }

    @Deprecated
    public void onUpdateCursor(Rect newCursor) {
    }

    public void onUpdateCursorAnchorInfo(CursorAnchorInfo cursorAnchorInfo) {
    }

    public void requestHideSelf(int flags) {
        this.mImm.hideSoftInputFromInputMethod(this.mToken, flags);
    }

    private void requestShowSelf(int flags) {
        this.mImm.showSoftInputFromInputMethod(this.mToken, flags);
    }

    private boolean handleBack(boolean doIt) {
        if (this.mShowInputRequested) {
            if (!doIt) {
                return true;
            }
            requestHideSelf(0);
            return true;
        } else if (!this.mWindowVisible) {
            return false;
        } else {
            if (this.mCandidatesVisibility == 0) {
                if (!doIt) {
                    return true;
                }
                setCandidatesViewShown(false);
                return true;
            } else if (!doIt) {
                return true;
            } else {
                doHideWindow();
                return true;
            }
        }
    }

    private ExtractEditText getExtractEditTextIfVisible() {
        if (isExtractViewShown() && isInputViewShown()) {
            return this.mExtractEditText;
        }
        return null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return doMovementKey(keyCode, event, -1);
        }
        ExtractEditText eet = getExtractEditTextIfVisible();
        if (eet != null && eet.handleBackInTextActionModeIfNeeded(event)) {
            return true;
        }
        if (!handleBack(false)) {
            return false;
        }
        event.startTracking();
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return doMovementKey(keyCode, event, count);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4) {
            ExtractEditText eet = getExtractEditTextIfVisible();
            if (eet != null && eet.handleBackInTextActionModeIfNeeded(event)) {
                return true;
            }
            if (event.isTracking() && !event.isCanceled()) {
                return handleBack(true);
            }
        }
        return doMovementKey(keyCode, event, -2);
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onAppPrivateCommand(String action, Bundle data) {
    }

    private void onToggleSoftInput(int showFlags, int hideFlags) {
        if (isInputViewShown()) {
            requestHideSelf(hideFlags);
        } else {
            requestShowSelf(showFlags);
        }
    }

    private void onShowSideSyncSoftInput(int showFlags) {
        this.mSideSyncShowInputRequested = true;
        requestShowSelf(showFlags);
    }

    void reportExtractedMovement(int keyCode, int count) {
        int dx = 0;
        int dy = 0;
        switch (keyCode) {
            case 19:
                dy = -count;
                break;
            case 20:
                dy = count;
                break;
            case 21:
                dx = -count;
                break;
            case 22:
                dx = count;
                break;
        }
        onExtractedCursorMovement(dx, dy);
    }

    boolean doMovementKey(int keyCode, KeyEvent event, int count) {
        ExtractEditText eet = getExtractEditTextIfVisible();
        if (eet != null) {
            MovementMethod movement = eet.getMovementMethod();
            Layout layout = eet.getLayout();
            if (!(movement == null || layout == null)) {
                if (count == -1) {
                    if (movement.onKeyDown(eet, eet.getText(), keyCode, event)) {
                        reportExtractedMovement(keyCode, 1);
                        return true;
                    }
                } else if (count == -2) {
                    if (movement.onKeyUp(eet, eet.getText(), keyCode, event)) {
                        return true;
                    }
                } else if (movement.onKeyOther(eet, eet.getText(), event)) {
                    reportExtractedMovement(keyCode, count);
                } else {
                    KeyEvent down = KeyEvent.changeAction(event, 0);
                    if (movement.onKeyDown(eet, eet.getText(), keyCode, down)) {
                        KeyEvent up = KeyEvent.changeAction(event, 1);
                        movement.onKeyUp(eet, eet.getText(), keyCode, up);
                        while (true) {
                            count--;
                            if (count <= 0) {
                                break;
                            }
                            movement.onKeyDown(eet, eet.getText(), keyCode, down);
                            movement.onKeyUp(eet, eet.getText(), keyCode, up);
                        }
                        reportExtractedMovement(keyCode, count);
                    }
                }
            }
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                    return true;
            }
        }
        return false;
    }

    public void sendDownUpKeyEvents(int keyEventCode) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            long eventTime = SystemClock.uptimeMillis();
            ic.sendKeyEvent(new KeyEvent(eventTime, eventTime, 0, keyEventCode, 0, 0, -1, 0, 6));
            ic.sendKeyEvent(new KeyEvent(eventTime, SystemClock.uptimeMillis(), 1, keyEventCode, 0, 0, -1, 0, 6));
        }
    }

    public boolean sendDefaultEditorAction(boolean fromEnterKey) {
        EditorInfo ei = getCurrentInputEditorInfo();
        if (ei == null || ((fromEnterKey && (ei.imeOptions & 1073741824) != 0) || (ei.imeOptions & 255) == 1)) {
            return false;
        }
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            return true;
        }
        ic.performEditorAction(ei.imeOptions & 255);
        return true;
    }

    public void sendKeyChar(char charCode) {
        switch (charCode) {
            case '\n':
                if (!sendDefaultEditorAction(true)) {
                    sendDownUpKeyEvents(66);
                    return;
                }
                return;
            default:
                if (charCode < '0' || charCode > '9') {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        ic.commitText(String.valueOf(charCode), 1);
                        return;
                    }
                    return;
                }
                sendDownUpKeyEvents((charCode - 48) + 7);
                return;
        }
    }

    public void onExtractedSelectionChanged(int start, int end) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setSelection(start, end);
        }
    }

    public void onReplaceDeleteText(CharSequence oldText, CharSequence newText) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setSelection(0, oldText.length());
            boolean b = conn.deleteSurroundingText(0, oldText.length());
            conn.commitText(newText, 0);
        }
    }

    public void onExtractedDeleteText(int start, int end) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setSelection(start, start);
            conn.deleteSurroundingText(0, end - start);
        }
    }

    public void onExtractedReplaceText(int start, int end, CharSequence text) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setComposingRegion(start, end);
            conn.commitText(text, 1);
        }
    }

    public void onExtractedSetSpan(Object span, int start, int end, int flags) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null && conn.setSelection(start, end)) {
            CharSequence text = conn.getSelectedText(1);
            if (text instanceof Spannable) {
                ((Spannable) text).setSpan(span, 0, text.length(), flags);
                conn.setComposingRegion(start, end);
                conn.commitText(text, 1);
            }
        }
    }

    public void onExtractedTextClicked() {
        if (this.mExtractEditText != null && this.mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    public void onExtractedCursorMovement(int dx, int dy) {
        if (this.mExtractEditText != null && dy != 0 && this.mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    public boolean onExtractTextContextMenuItem(int id) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.performContextMenuAction(id);
        }
        return true;
    }

    public CharSequence getTextForImeAction(int imeOptions) {
        switch (imeOptions & 255) {
            case 1:
                return null;
            case 2:
                return getText(17040428);
            case 3:
                return getText(17040429);
            case 4:
                return getText(17040430);
            case 5:
                return getText(17040431);
            case 6:
                return getText(17040432);
            case 7:
                return getText(17040433);
            default:
                return getText(17040434);
        }
    }

    public void onUpdateExtractingVisibility(EditorInfo ei) {
        if (ei.inputType == 0 || (ei.imeOptions & 268435456) != 0) {
            setExtractViewShown(false);
        } else {
            setExtractViewShown(true);
        }
    }

    public void onUpdateExtractingViews(EditorInfo ei) {
        boolean hasAction = true;
        if (isExtractViewShown() && this.mExtractAccessories != null) {
            if (ei.actionLabel == null && ((ei.imeOptions & 255) == 1 || (ei.imeOptions & 536870912) != 0 || ei.inputType == 0)) {
                hasAction = false;
            }
            if (hasAction) {
                this.mExtractAccessories.setVisibility(0);
                this.mExtractAccessories.setBackgroundColor(-1);
                if (this.mExtractAction != null) {
                    if (ei.actionLabel != null) {
                        this.mExtractAction.setText(ei.actionLabel);
                    } else {
                        this.mExtractAction.setText(getTextForImeAction(ei.imeOptions));
                    }
                    this.mExtractAction.setOnClickListener(this.mActionClickListener);
                    return;
                }
                return;
            }
            this.mExtractAccessories.setVisibility(8);
            if (this.mExtractAction != null) {
                this.mExtractAction.setOnClickListener(null);
            }
        }
    }

    public void onExtractingInputChanged(EditorInfo ei) {
        if (ei.inputType == 0) {
            requestHideSelf(2);
        }
    }

    void startExtractingText(boolean inputChanged) {
        ExtractEditText eet = this.mExtractEditText;
        if (eet != null && getCurrentInputStarted() && isFullscreenMode()) {
            this.mExtractedToken++;
            ExtractedTextRequest req = new ExtractedTextRequest();
            req.token = this.mExtractedToken;
            req.flags = 1;
            req.hintMaxLines = 10;
            req.hintMaxChars = 10000;
            InputConnection ic = getCurrentInputConnection();
            this.mExtractedText = ic == null ? null : ic.getExtractedText(req, 1);
            if (this.mExtractedText == null || ic == null) {
                Log.e(TAG, "Unexpected null in startExtractingText : mExtractedText = " + this.mExtractedText + ", input connection = " + ic);
            }
            EditorInfo ei = getCurrentInputEditorInfo();
            try {
                eet.startInternalChanges();
                onUpdateExtractingVisibility(ei);
                onUpdateExtractingViews(ei);
                int inputType = ei.inputType;
                if ((inputType & 15) == 1 && (262144 & inputType) != 0) {
                    inputType |= 131072;
                }
                eet.setInputType(inputType);
                eet.setHint(ei.hintText);
                eet.setTextColor(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
                eet.setBackgroundColor(-1);
                if (ei.privateImeOptions != null) {
                    String[] tmpImeOptions = ei.privateImeOptions.split("#");
                    if (tmpImeOptions.length == 2 && (tmpImeOptions[0].equals("AppName=Memo") || tmpImeOptions[0].equals("AppName=Diary"))) {
                        String[] tmpColor = tmpImeOptions[1].split("=");
                        if (tmpColor.length == 2 && tmpColor[0].equals("Color")) {
                            tmpColor[1] = tmpColor[1].toLowerCase().replaceAll("0x", ProxyInfo.LOCAL_EXCL_LIST);
                            try {
                                eet.setBackgroundColor((int) Long.parseLong(tmpColor[1], 16));
                            } catch (NumberFormatException e) {
                            }
                        }
                    }
                }
                if (this.mExtractedText != null) {
                    eet.setEnabled(true);
                    eet.setExtractedText(this.mExtractedText);
                } else {
                    eet.setEnabled(false);
                    eet.setText(ProxyInfo.LOCAL_EXCL_LIST);
                }
                eet.finishInternalChanges();
                if (inputChanged) {
                    onExtractingInputChanged(ei);
                }
            } catch (Throwable th) {
                eet.finishInternalChanges();
            }
        }
    }

    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
    }

    public int getInputMethodWindowRecommendedHeight() {
        return this.mImm.getInputMethodWindowVisibleHeight();
    }

    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
        Printer p = new PrintWriterPrinter(fout);
        p.println("Input method service state for " + this + ":");
        p.println("  mWindowCreated=" + this.mWindowCreated + " mWindowAdded=" + this.mWindowAdded);
        p.println("  mWindowVisible=" + this.mWindowVisible + " mWindowWasVisible=" + this.mWindowWasVisible + " mInShowWindow=" + this.mInShowWindow);
        p.println("  Configuration=" + getResources().getConfiguration());
        p.println("  mToken=" + this.mToken);
        p.println("  mInputBinding=" + this.mInputBinding);
        p.println("  mInputConnection=" + this.mInputConnection);
        p.println("  mStartedInputConnection=" + this.mStartedInputConnection);
        p.println("  mInputStarted=" + this.mInputStarted + " mInputViewStarted=" + this.mInputViewStarted + " mCandidatesViewStarted=" + this.mCandidatesViewStarted);
        if (this.mInputEditorInfo != null) {
            p.println("  mInputEditorInfo:");
            this.mInputEditorInfo.dump(p, "    ");
        } else {
            p.println("  mInputEditorInfo: null");
        }
        p.println("  mShowInputRequested=" + this.mShowInputRequested + " mLastShowInputRequested=" + this.mLastShowInputRequested + " mShowInputForced=" + this.mShowInputForced + " mShowInputFlags=0x" + Integer.toHexString(this.mShowInputFlags));
        p.println("  mCandidatesVisibility=" + this.mCandidatesVisibility + " mFullscreenApplied=" + this.mFullscreenApplied + " mIsFullscreen=" + this.mIsFullscreen + " mExtractViewHidden=" + this.mExtractViewHidden);
        if (this.mExtractedText != null) {
            p.println("  mExtractedText:");
            p.println("    text=" + this.mExtractedText.text.length() + " chars" + " startOffset=" + this.mExtractedText.startOffset);
            p.println("    selectionStart=" + this.mExtractedText.selectionStart + " selectionEnd=" + this.mExtractedText.selectionEnd + " flags=0x" + Integer.toHexString(this.mExtractedText.flags));
        } else {
            p.println("  mExtractedText: null");
        }
        p.println("  mExtractedToken=" + this.mExtractedToken);
        p.println("  mIsInputViewShown=" + this.mIsInputViewShown + " mStatusIcon=" + this.mStatusIcon);
        p.println("Last computed insets:");
        p.println("  contentTopInsets=" + this.mTmpInsets.contentTopInsets + " visibleTopInsets=" + this.mTmpInsets.visibleTopInsets + " touchableInsets=" + this.mTmpInsets.touchableInsets + " touchableRegion=" + this.mTmpInsets.touchableRegion);
        p.println(" mShouldClearInsetOfPreviousIme=" + this.mShouldClearInsetOfPreviousIme);
    }

    public void setExtractSelectionToEnd() {
        ExtractEditText eet = this.mExtractEditText;
        int selectionEnd = Selection.getSelectionEnd(eet.getText());
        eet.setSelection(selectionEnd);
        eet.onSelectionChanged(selectionEnd, selectionEnd);
    }

    public void hideExtractCursorController() {
        this.mExtractEditText.hideCursorControllers();
    }

    public void forceExtractEditTextClose() {
        if (this.mShowInputRequested) {
            if (isExtractViewShown() && (this.mExtractView instanceof ExtractEditLayout)) {
                ExtractEditLayout extractEditLayout = (ExtractEditLayout) this.mExtractView;
            }
            requestHideSelf(0);
        } else if (!this.mWindowVisible) {
        } else {
            if (this.mCandidatesVisibility == 0) {
                setCandidatesViewShown(false);
            } else {
                doHideWindow();
            }
        }
    }

    long prepareSpacesAroundPaste(int min, int max, CharSequence paste) {
        if (paste.length() > 0) {
            CharSequence charBefore;
            CharSequence charAfter;
            InputConnection conn = getCurrentInputConnection();
            if (min > 0) {
                conn.setSelection(min, min);
                charBefore = conn.getTextBeforeCursor(1, 0);
                charAfter = conn.getTextAfterCursor(1, 0);
                if (!(charBefore == null || charAfter == null)) {
                    if (Character.isSpaceChar(charBefore.charAt(0)) && Character.isSpaceChar(charAfter.charAt(0))) {
                        onExtractedDeleteText(min - 1, min);
                    } else if (!(Character.isSpaceChar(charBefore.charAt(0)) || charBefore.charAt(0) == '\n' || Character.isSpaceChar(charAfter.charAt(0)) || charAfter.charAt(0) == '\n')) {
                        onExtractedReplaceText(min, min, " ");
                        min++;
                        max++;
                    }
                }
            }
            conn.setSelection(max, max);
            charBefore = conn.getTextBeforeCursor(1, 0);
            charAfter = conn.getTextAfterCursor(1, 0);
            if (!(charBefore == null || charAfter == null || charAfter.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                if (Character.isSpaceChar(charBefore.charAt(0)) && Character.isSpaceChar(charAfter.charAt(0))) {
                    onExtractedDeleteText(max, max + 1);
                } else if (!(Character.isSpaceChar(charBefore.charAt(0)) || charBefore.charAt(0) == '\n' || Character.isSpaceChar(charAfter.charAt(0)) || charAfter.charAt(0) == '\n')) {
                    onExtractedReplaceText(max, max, " ");
                    max++;
                }
            }
        }
        return (long) max;
    }

    public void changeFullInputMethod(boolean fullMode) {
        this.mFullscreenApplied = false;
        this.mImm.changeFullInputMethod(fullMode);
    }

    public void doMinimizeSoftInput() {
    }

    public void doMinimizeSoftInput(int height) {
    }

    public void undoMinimizeSoftInput() {
    }
}
