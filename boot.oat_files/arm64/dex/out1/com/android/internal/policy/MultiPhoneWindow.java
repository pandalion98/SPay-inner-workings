package com.android.internal.policy;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.hardware.display.DisplayManagerGlobal;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersonaManager;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.interpolator.SineInOut33;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.os.SMProviderContract;
import com.android.internal.policy.multiwindow.ApplicationThumbnail;
import com.android.internal.policy.multiwindow.Border;
import com.android.internal.policy.multiwindow.Docking;
import com.android.internal.policy.multiwindow.Docking.OnDockingListener;
import com.android.internal.policy.multiwindow.EdgeInspector;
import com.android.internal.policy.multiwindow.MinimizeAnimator;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowLoggingHelper;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.multiwindow.ui.GuideView;
import com.samsung.android.sdk.multiwindow.SMultiWindowListener.ExitListener;
import com.samsung.android.sdk.multiwindow.SMultiWindowListener.StateChangeListener;
import com.samsung.android.sdk.multiwindow.SMultiWindowListener.StateChangeListener2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MultiPhoneWindow extends PhoneWindow {
    static final boolean DEBUG;
    static final boolean DEBUG_FLOATING_BGCOLOR = DEBUG;
    static final boolean DEBUG_FLOATING_CYCLE = DEBUG;
    static final boolean DEBUG_FLOATING_SIZE = DEBUG;
    static final boolean DEBUG_GUIDEVIEW = false;
    public static final boolean DEBUG_MINIMIZE_ANIM = DEBUG;
    static final boolean DEBUG_ORIENTATION = DEBUG;
    static final boolean DEBUG_RESIZE_VISUAL_CUE = DEBUG;
    static final boolean DEBUG_TAB = DEBUG;
    private static final int LOGGING_REASON_DOCKING = 1;
    private static final int STATE_FLOATING = 2;
    private static final int STATE_MINIMIZED_FLOATING = 4;
    private static final int STATE_NONE = -1;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_SCALED_FLOATING = 3;
    static final String TAG = "MultiPhoneWindow";
    private static final boolean bDSSEnabled = true;
    private final int NOT_SET = -1;
    protected final int TEMP_TOAST_HEIGHT = 40;
    private Drawable badgeIcon = null;
    private Activity mActivity = null;
    private AudioManager mAudioManager;
    private ColorDrawable mBlackColor = new ColorDrawable(-16777216);
    private Border mBorder;
    private boolean mContentLayoutGenerated = false;
    private ViewGroup mContentRootContainer;
    private Context mContext;
    private ApplicationThumbnail mCustomMinimizedThumbnail = null;
    private View mCustomMinimizedView = null;
    private boolean mDismissGuideByDockingCanceled;
    private Docking mDocking;
    private boolean mDragMode = false;
    BroadcastReceiver mDragModeReceiver = null;
    private float mDssScale = 1.0f;
    private int mFloatingFlag;
    private int mFloatingMenuRightMargin;
    private int mFocusedViewId = -1;
    private GuideView mGuideView;
    private float mHScale = 1.0f;
    Handler mHandler = new Handler() {
    };
    private boolean mHasStackFocus = false;
    private int mHeaderButtonSoundId = -1;
    private int mInitialFlag;
    private boolean mIsAttachedToWindow = false;
    private boolean mIsBorderShown;
    private boolean mIsFinishing = false;
    private boolean mIsFullScreen = false;
    private boolean mIsMinimizeDisabled = false;
    private boolean mIsPenButtonPressed = false;
    private boolean mIsSecure = false;
    private final boolean mIsSupportDiagonalResizable;
    private final boolean mIsSupportMinimizeAnimation;
    private final boolean mIsSupportSimplificationUI;
    private int mLastHoverIcon = -1;
    private int mLastOrientation = 0;
    private boolean mLastRotated = false;
    private Rect mLastStackBound = new Rect();
    private Rect mMaxLandStackBoundForSelectiveOrientation = new Rect();
    private Rect mMaxPortStackBoundForSelectiveOrientation = new Rect();
    private float mMaxSizeRatio = 1.0f;
    private Rect mMinStackBoundForLand = new Rect();
    private Rect mMinStackBoundForPort = new Rect();
    private MinimizeAnimator mMinimizeAnimator;
    private ExitListener mMultiWindowExitListener = null;
    private final MultiWindowFacade mMultiWindowFacade;
    private StateChangeListener mMultiWindowListener = null;
    private StateChangeListener2 mMultiWindowListener2 = null;
    private MultiWindowStyle mMultiWindowStyle;
    PenWindowController mPenWindowController;
    private final PowerManager mPowerManager;
    private Rect mResizablePadding = new Rect();
    private int mRotation = 0;
    private SoundPool mSoundPool;
    private Rect mStackBound = new Rect();
    private int mStatusBarHeight;
    private Window mSubWindow;
    private int mTargetSdkVersion;
    private int mThickness;
    private ApplicationThumbnail mThumbnail;
    private int mTitleBarHeight;
    private Rect mTmpBound = new Rect();
    private IBinder mToken = null;
    private ColorDrawable mTrasnparentColor = new ColorDrawable(0);
    private float mVScale = 1.0f;
    private AlertDialog mVideoCapabilityDialog = null;
    private VideoCapabilityReceiver mVideoCapabilityReceiver = new VideoCapabilityReceiver();
    private int userId = 0;

    public class DragModeBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.sec.android.action.NOTIFY_STOP_DRAG_MODE")) {
                MultiPhoneWindow.this.mDragMode = false;
                if (MultiPhoneWindow.DEBUG) {
                    Log.i(MultiPhoneWindow.TAG, "SmartClipService Stoped");
                }
                if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                    MultiPhoneWindow.this.mPenWindowController.performStopDragMode();
                }
                if (MultiPhoneWindow.this.mMultiWindowFacade.getDragAndDropModeOfStack(MultiPhoneWindow.this.mToken)) {
                    MultiPhoneWindow.this.mMultiWindowFacade.setDragAndDropModeOfStack(MultiPhoneWindow.this.mToken, false);
                }
            }
            if (intent.getAction().equals("com.sec.android.action.NOTIFY_START_DRAG_MODE")) {
                MultiPhoneWindow.this.mDragMode = true;
            }
            if (MultiPhoneWindow.this.mMinimizeAnimator != null) {
                MultiPhoneWindow.this.mMinimizeAnimator.setDragAndDropMode(MultiPhoneWindow.this.mDragMode);
            }
        }
    }

    protected abstract class PenWindowController implements OnClickListener, OnLongClickListener, OnTouchListener, OnHoverListener {
        protected View mBtnDragAndDrop;
        protected View mBtnExit;
        protected View mBtnMaximize;
        protected View mBtnMinimize;
        protected int mControllerHeight = 0;
        protected Dialog mDnDHelpPopupDialog;
        protected AlertDialog mDnDHelpPopupDialogLegacy;
        protected GestureDetector mGestureDetector = null;
        private Drawable mHeaderWindowControllerHoverImage;
        private boolean mIsInputMethodForceHiding;
        private boolean mIsLongPressed;
        private boolean mIsMoving;
        protected boolean mIsShowing = false;
        private int mLastHandledX;
        private int mLastHandledY;
        protected View mMenuContainer;
        private int mMoveInterval = 0;
        protected boolean mNeedInvalidate = false;
        protected View mPenWindowHeader;

        protected abstract void performInflateController();

        protected abstract void performUpdateBackground();

        protected abstract void performUpdateVisibility(boolean z);

        protected abstract void removeResizeVisualCue();

        protected abstract void updateAvailableButtons();

        protected abstract void updatePosition();

        protected void performStartDragMode() {
        }

        protected void performStopDragMode() {
        }

        protected void performUpdateMenuVisibility(boolean visible) {
        }

        protected void setHeaderGestureDetector(GestureDetector g) {
            this.mGestureDetector = g;
        }

        public IBinder getWindowToken() {
            return MultiPhoneWindow.this.getDecorView().getWindowToken();
        }

        public void invalidate() {
            this.mNeedInvalidate = true;
        }

        public PenWindowController() {
            this.mMoveInterval = MultiPhoneWindow.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_move_interporation);
        }

        protected void generateLayout() {
            performInflateController();
            this.mPenWindowHeader.setOnTouchListener(this);
            this.mPenWindowHeader.setOnLongClickListener(this);
            this.mPenWindowHeader.setOnHoverListener(this);
            this.mBtnDragAndDrop = this.mMenuContainer.findViewById(R.id.multi_window_btn_drag_and_drop);
            this.mBtnDragAndDrop.setOnClickListener(this);
            this.mBtnDragAndDrop.setOnLongClickListener(this);
            this.mBtnMinimize = this.mMenuContainer.findViewById(R.id.multi_window_btn_minimize);
            this.mBtnMinimize.setOnClickListener(this);
            this.mBtnMinimize.setOnLongClickListener(this);
            this.mBtnMaximize = this.mMenuContainer.findViewById(R.id.multi_window_btn_maximize);
            this.mBtnMaximize.setOnClickListener(this);
            this.mBtnMaximize.setOnLongClickListener(this);
            this.mBtnExit = this.mMenuContainer.findViewById(R.id.multi_window_btn_exit);
            this.mBtnExit.setOnClickListener(this);
            this.mBtnExit.setOnLongClickListener(this);
            checkFunctionState();
            updateAvailableButtons();
        }

        private void makeDnDHelpPopupLayout() {
            if (this.mDnDHelpPopupDialog == null || !this.mDnDHelpPopupDialog.isShowing()) {
                try {
                    View dnDHelpPopupView = ((LayoutInflater) MultiPhoneWindow.this.mContext.getSystemService("layout_inflater")).inflate((int) R.layout.help_popup_drag_and_drop, null);
                    if (dnDHelpPopupView == null) {
                        return;
                    }
                    if (!MultiPhoneWindow.this.mContentLayoutGenerated || MultiPhoneWindow.this.mPenWindowController.mDnDHelpPopupDialog == null || !MultiPhoneWindow.this.mPenWindowController.mDnDHelpPopupDialog.isShowing()) {
                        ((Button) dnDHelpPopupView.findViewById(R.id.help_button_ok)).setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                MultiPhoneWindow.this.mMultiWindowFacade.updatePreferenceThroughSystemProcess("do_not_show_help_popup_drag_and_drop", 1);
                                PenWindowController.this.mDnDHelpPopupDialog.dismiss();
                            }
                        });
                        this.mDnDHelpPopupDialog = new Dialog(MultiPhoneWindow.this.mContext, R.style.MultiWindowHelpOverlay);
                        this.mDnDHelpPopupDialog.setContentView(dnDHelpPopupView);
                        this.mDnDHelpPopupDialog.setCanceledOnTouchOutside(false);
                        this.mDnDHelpPopupDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                PenWindowController.this.startDragMode(MultiPhoneWindow.this.mContext);
                            }
                        });
                        LayoutParams p = this.mDnDHelpPopupDialog.getWindow().getAttributes();
                        p.setTitle("DnDHelpPopupDialog");
                        p.flags |= 514;
                        p.flags &= -131073;
                        p.dimAmount = 0.5f;
                        p.type = 2;
                        this.mDnDHelpPopupDialog.show();
                    }
                } catch (NotFoundException e) {
                }
            }
        }

        private void makeDnDHelpPopupLegacyLayout() {
            try {
                final Context context = new ContextThemeWrapper(MultiPhoneWindow.this.mContext, (int) R.style.Theme_DeviceDefault_Light_Dialog);
                View contentView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate((int) R.layout.help_popup_drag_and_drop_legacy, null);
                if (contentView == null) {
                    return;
                }
                if (!MultiPhoneWindow.this.mContentLayoutGenerated || this.mDnDHelpPopupDialogLegacy == null || !this.mDnDHelpPopupDialogLegacy.isShowing()) {
                    final CheckBox checkBox = (CheckBox) contentView.findViewById(R.id.help_popup_checkBox);
                    this.mDnDHelpPopupDialogLegacy = new Builder(context, 5).setTitle(context.getResources().getString(R.string.SS_DRAG_AND_DROP)).setView(contentView).setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (checkBox.isChecked()) {
                                MultiPhoneWindow.this.mMultiWindowFacade.updatePreferenceThroughSystemProcess("do_not_show_help_popup_drag_and_drop", 1);
                            }
                            PenWindowController.this.mDnDHelpPopupDialogLegacy.dismiss();
                        }
                    }).create();
                    this.mDnDHelpPopupDialogLegacy.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            PenWindowController.this.startDragMode(context);
                        }
                    });
                    Window w = this.mDnDHelpPopupDialogLegacy.getWindow();
                    w.addFlags(512);
                    LayoutParams p = w.getAttributes();
                    p.setTitle("DnDHelpPopupDialog");
                    p.type = 2;
                    this.mDnDHelpPopupDialogLegacy.show();
                }
            } catch (NotFoundException e) {
            }
        }

        private void removeDnDHelpPopup() {
            if (MultiPhoneWindow.this.mIsSupportSimplificationUI) {
                if (this.mDnDHelpPopupDialog != null && this.mDnDHelpPopupDialog.isShowing()) {
                    this.mDnDHelpPopupDialog.hide();
                    this.mDnDHelpPopupDialog = null;
                }
            } else if (this.mDnDHelpPopupDialogLegacy != null && this.mDnDHelpPopupDialogLegacy.isShowing()) {
                this.mDnDHelpPopupDialogLegacy.hide();
                this.mDnDHelpPopupDialogLegacy = null;
            }
        }

        private void startDragMode(Context context) {
            if (MultiPhoneWindow.this.getMultiWindowStyle() == null || MultiPhoneWindow.this.getMultiWindowStyle().getType() == 2) {
                Intent intent = new Intent();
                if (MultiWindowFeatures.isSupportSimplificationUI(context)) {
                    intent.setClassName("com.android.systemui", "com.android.systemui.multiwindow.centerbarwindow.SmartClipDragDrop");
                } else {
                    intent.setClassName("com.sec.android.app.FlashBarService", "com.sec.android.app.FlashBarService.SmartClipDragDrop");
                }
                context.startService(intent);
                performStartDragMode();
                MultiPhoneWindow.this.mMultiWindowFacade.setDragAndDropModeOfStack(MultiPhoneWindow.this.mToken, true);
                MultiWindowLoggingHelper.insertLog(MultiPhoneWindow.this.mContext, MultiWindowLoggingHelper.POPUPWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.DRAGCONTENT_TYPE);
            }
        }

        protected boolean isShowing() {
            return this.mIsShowing;
        }

        protected int getControllerHeight() {
            return this.mControllerHeight;
        }

        private void checkFunctionState() {
            PackageManager pm = MultiPhoneWindow.this.mActivity.getPackageManager();
            if (pm != null) {
                ActivityInfo activityInfo = null;
                try {
                    activityInfo = pm.getActivityInfo(MultiPhoneWindow.this.mActivity.getComponentName(), 128);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (activityInfo != null) {
                    ArrayList<String> disableFunctions = new ArrayList();
                    if (activityInfo.metaData != null) {
                        String activityFunction = activityInfo.metaData.getString("com.samsung.android.sdk.multiwindow.disablefunction");
                        if (activityFunction != null) {
                            disableFunctions.addAll(Arrays.asList(activityFunction.split("\\|")));
                        }
                    }
                    if (!(activityInfo.applicationInfo == null || activityInfo.applicationInfo.metaData == null)) {
                        String applicationFunction = activityInfo.applicationInfo.metaData.getString("com.samsung.android.sdk.multiwindow.disablefunction");
                        if (applicationFunction != null) {
                            disableFunctions.addAll(Arrays.asList(applicationFunction.split("\\|")));
                        }
                    }
                    this.mBtnDragAndDrop.setVisibility(0);
                    Iterator i$ = disableFunctions.iterator();
                    while (i$.hasNext()) {
                        String disableFunction = (String) i$.next();
                        if (disableFunction.equals("minimize")) {
                            MultiPhoneWindow.this.mIsMinimizeDisabled = true;
                            this.mBtnMinimize.setVisibility(8);
                        } else if (disableFunction.equals("maximize")) {
                            this.mBtnMaximize.setVisibility(8);
                        } else if (disableFunction.equals("exit")) {
                            this.mBtnExit.setVisibility(8);
                        } else if (disableFunctions.contains("drag_and_drop")) {
                            this.mBtnDragAndDrop.setVisibility(8);
                        }
                    }
                }
            }
        }

        public void onClick(View view) {
            boolean checked = true;
            if (view.equals(this.mPenWindowHeader)) {
                performUpdateMenuVisibility(true);
            } else if (view.equals(this.mBtnDragAndDrop)) {
                if (MultiPhoneWindow.this.mMultiWindowFacade.getPreferenceThroughSystemProcess("do_not_show_help_popup_drag_and_drop") == 0) {
                    checked = false;
                }
                if (checked) {
                    startDragMode(MultiPhoneWindow.this.mContext);
                    return;
                }
                performUpdateMenuVisibility(false);
                if (MultiPhoneWindow.this.mIsSupportSimplificationUI) {
                    makeDnDHelpPopupLayout();
                } else {
                    makeDnDHelpPopupLegacyLayout();
                }
            } else if (view.equals(this.mBtnMinimize)) {
                ((FloatingMenuView) MultiPhoneWindow.this.mPenWindowController.mMenuContainer).removeSelf();
                MultiPhoneWindow.this.minimizeWindow(0, false);
                MultiWindowLoggingHelper.insertLog(MultiPhoneWindow.this.mContext, MultiWindowLoggingHelper.POPUPWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.MIN_TYPE);
            } else if (view.equals(this.mBtnMaximize)) {
                if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                    performUpdateVisibility(false);
                }
                MultiPhoneWindow.this.requestState(1);
                MultiWindowLoggingHelper.insertLog(MultiPhoneWindow.this.mContext, MultiWindowLoggingHelper.POPUPWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.MAX_TYPE);
            } else if (view.equals(this.mBtnExit) && MultiPhoneWindow.this.mActivity != null) {
                if (MultiPhoneWindow.this.mMultiWindowExitListener == null || MultiPhoneWindow.this.mMultiWindowExitListener.onWindowExit()) {
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    if (imm != null && imm.isInputMethodShown()) {
                        MultiPhoneWindow.this.forceHideInputMethod();
                    }
                    if (this.mBtnExit.getHoverPopupWindow() != null) {
                        this.mBtnExit.getHoverPopupWindow().dismiss();
                    }
                    try {
                        if (MultiPhoneWindow.this.mMultiWindowStyle.isCascade()) {
                            MultiPhoneWindow.this.mMultiWindowFacade.finishAllTasks(MultiPhoneWindow.this.mActivity.getActivityToken(), 0);
                        } else {
                            MultiPhoneWindow.this.mActivity.finishAffinity();
                        }
                    } catch (IllegalStateException e) {
                        MultiPhoneWindow.this.mActivity.finish();
                    }
                    MultiPhoneWindow.this.mIsFinishing = true;
                    MultiWindowLoggingHelper.insertLog(MultiPhoneWindow.this.mContext, MultiWindowLoggingHelper.POPUPWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.CLOSE_TYPE);
                    return;
                }
                Log.w(MultiPhoneWindow.TAG, "onWindowExit return false");
                MultiWindowLoggingHelper.insertLog(MultiPhoneWindow.this.mContext, MultiWindowLoggingHelper.POPUPWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.CLOSE_TYPE);
            }
        }

        public boolean onLongClick(View v) {
            if (this.mIsMoving) {
                if (MultiPhoneWindow.DEBUG_MINIMIZE_ANIM) {
                    Log.d(MultiPhoneWindow.TAG, "onLongClick : moving so return false");
                }
                return false;
            }
            this.mIsLongPressed = true;
            int[] screenPos = new int[2];
            Rect displayFrame = new Rect();
            v.getLocationOnScreen(screenPos);
            v.getWindowVisibleDisplayFrame(displayFrame);
            int height = v.getHeight();
            if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                FloatingMenuView menuContainer = MultiPhoneWindow.this.mPenWindowController.mMenuContainer;
                if (!(menuContainer == null || menuContainer.mFloatingMenuContainer == null)) {
                    height = menuContainer.mFloatingMenuContainer.getHeight();
                }
            }
            Context context = MultiPhoneWindow.this.getContext();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            MultiWindowStyle mwStyle = MultiPhoneWindow.this.mContext.getAppMultiWindowStyle();
            if (mwStyle != null && mwStyle.getType() == 2 && ((MultiPhoneWindow.this.mActivity.getRequestedOrientation() == 1 && screenWidth > screenHeight) || (MultiPhoneWindow.this.mActivity.getRequestedOrientation() == 0 && screenWidth < screenHeight))) {
                int tempWidth = screenWidth;
                screenWidth = screenHeight;
                screenHeight = tempWidth;
            }
            Toast cheatSheet = Toast.makeText(context, v.getContentDescription(), 0);
            cheatSheet.setIgnoreMultiWindowLayout();
            View toastView = ((LayoutInflater) MultiPhoneWindow.this.mContext.getSystemService("layout_inflater")).inflate((int) R.layout.mw_transient_notification, null);
            cheatSheet.setView(toastView);
            ((TextView) toastView.findViewById(R.id.mw_transient_notification_msg)).setText(v.getContentDescription());
            cheatSheet.setGravity(53, ((screenWidth - screenPos[0]) + MultiPhoneWindow.this.getStackBound().left) - (v.getWidth() / 2), height / 2);
            cheatSheet.show();
            return true;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouch(android.view.View r13, android.view.MotionEvent r14) {
            /*
            r12 = this;
            r11 = 0;
            r10 = 1;
            r6 = r14.getAction();
            r0 = r6 & 255;
            switch(r0) {
                case 0: goto L_0x0015;
                case 1: goto L_0x0135;
                case 2: goto L_0x008b;
                case 3: goto L_0x0135;
                default: goto L_0x000b;
            };
        L_0x000b:
            r6 = r12.mGestureDetector;
            if (r6 == 0) goto L_0x0014;
        L_0x000f:
            r6 = r12.mGestureDetector;
            r6.onTouchEvent(r14);
        L_0x0014:
            return r10;
        L_0x0015:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mHasStackFocus;
            if (r6 != 0) goto L_0x0036;
        L_0x001d:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mMultiWindowFacade;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.mToken;
            r5 = r6.getStackId(r7);
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mMultiWindowFacade;
            r6.setFocusedStack(r5, r10);
        L_0x0036:
            r12.mIsInputMethodForceHiding = r11;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.forceHideInputMethod();
            if (r6 == 0) goto L_0x0043;
        L_0x0040:
            r12.mIsInputMethodForceHiding = r10;
            goto L_0x0014;
        L_0x0043:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6.init();
            r12.mIsMoving = r11;
            r12.mIsLongPressed = r11;
            r6 = r14.getRawXForScaledWindow();
            r6 = (int) r6;
            r12.mLastHandledX = r6;
            r6 = r14.getRawYForScaledWindow();
            r6 = (int) r6;
            r12.mLastHandledY = r6;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6.mDismissGuideByDockingCanceled = r11;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mContext;
            if (r6 == 0) goto L_0x007b;
        L_0x006b:
            r4 = new android.content.Intent;
            r6 = "com.sec.android.OUTSIDE_TOUCH";
            r4.<init>(r6);
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mContext;
            r6.sendBroadcast(r4);
        L_0x007b:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mTmpBound;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.getStackBound();
            r6.set(r7);
            goto L_0x000b;
        L_0x008b:
            r6 = r12.mIsInputMethodForceHiding;
            if (r6 != 0) goto L_0x0014;
        L_0x008f:
            r6 = r12.mIsMoving;
            r7 = r12.mIsLongPressed;
            r6 = r6 | r7;
            if (r6 != 0) goto L_0x00cc;
        L_0x0096:
            r6 = r14.getRawXForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledX;
            r6 = r6 - r7;
            r6 = java.lang.Math.abs(r6);
            r6 = (float) r6;
            r7 = r12.mMoveInterval;
            r7 = (float) r7;
            r8 = com.android.internal.policy.MultiPhoneWindow.this;
            r8 = r8.mDssScale;
            r7 = r7 / r8;
            r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
            if (r6 >= 0) goto L_0x00cc;
        L_0x00b1:
            r6 = r14.getRawYForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledY;
            r6 = r6 - r7;
            r6 = java.lang.Math.abs(r6);
            r6 = (float) r6;
            r7 = r12.mMoveInterval;
            r7 = (float) r7;
            r8 = com.android.internal.policy.MultiPhoneWindow.this;
            r8 = r8.mDssScale;
            r7 = r7 / r8;
            r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
            if (r6 < 0) goto L_0x000b;
        L_0x00cc:
            r12.mIsMoving = r10;
            r6 = r14.getRawXForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledX;
            r2 = r6 - r7;
            r6 = r14.getRawYForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledY;
            r3 = r6 - r7;
            r6 = r14.getRawXForScaledWindow();
            r6 = (int) r6;
            r12.mLastHandledX = r6;
            r6 = r14.getRawYForScaledWindow();
            r6 = (int) r6;
            r12.mLastHandledY = r6;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r7 = r12.mLastHandledX;
            r8 = r12.mLastHandledY;
            r6 = r6.updateZone(r7, r8);
            if (r6 == 0) goto L_0x00fe;
        L_0x00fe:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r1 = r6.getBounds();
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mTmpBound;
            r6.offset(r2, r3);
            if (r1 == 0) goto L_0x0127;
        L_0x0113:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6 = r6.isDockingCanceled();
            if (r6 != 0) goto L_0x0127;
        L_0x011f:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = 2;
            r6.showGuide(r1, r7);
            goto L_0x000b;
        L_0x0127:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.mTmpBound;
            r8 = 3;
            r6.showGuide(r7, r8);
            goto L_0x000b;
        L_0x0135:
            r6 = r12.mIsInputMethodForceHiding;
            if (r6 != 0) goto L_0x0014;
        L_0x0139:
            r6 = r12.mIsMoving;
            if (r6 == 0) goto L_0x01f3;
        L_0x013d:
            r6 = r14.getRawXForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledX;
            r2 = r6 - r7;
            r6 = r14.getRawYForScaledWindow();
            r6 = (int) r6;
            r7 = r12.mLastHandledY;
            r3 = r6 - r7;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6 = r6.isDocking();
            if (r6 == 0) goto L_0x01de;
        L_0x015b:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6 = r6.isDockingCanceled();
            if (r6 != 0) goto L_0x01de;
        L_0x0167:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6.checkCenterBarPoint();
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mIsSupportSimplificationUI;
            if (r6 == 0) goto L_0x01ad;
        L_0x0178:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mMultiWindowFacade;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.mToken;
            r8 = com.android.internal.policy.MultiPhoneWindow.this;
            r8 = r8.mDocking;
            r8 = r8.getZone();
            r9 = com.android.internal.policy.MultiPhoneWindow.this;
            r9 = r9.getMultiWindowStyle();
            r8 = com.android.internal.policy.multiwindow.Docking.getChanagedMultiWindowStyle(r8, r9);
            r6.setMultiWindowStyleWithLogging(r7, r8, r10);
        L_0x019b:
            r12.mIsMoving = r11;
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mDocking;
            r6.clear();
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6.dismissGuide();
            goto L_0x000b;
        L_0x01ad:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mMultiWindowFacade;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.mToken;
            r8 = com.android.internal.policy.MultiPhoneWindow.this;
            r8 = r8.mDocking;
            r8 = r8.getZone();
            r9 = com.android.internal.policy.MultiPhoneWindow.this;
            r9 = r9.getMultiWindowStyle();
            r8 = com.android.internal.policy.multiwindow.Docking.getChanagedMultiWindowStyle(r8, r9);
            r6.setMultiWindowStyle(r7, r8);
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mContext;
            r7 = "POPW";
            r8 = "CHANGE-SPLIT";
            com.samsung.android.multiwindow.MultiWindowLoggingHelper.insertLog(r6, r7, r8);
            goto L_0x019b;
        L_0x01de:
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r6 = r6.mTmpBound;
            r6.offset(r2, r3);
            r6 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = com.android.internal.policy.MultiPhoneWindow.this;
            r7 = r7.mTmpBound;
            r6.setStackBound(r7);
            goto L_0x019b;
        L_0x01f3:
            r12.performUpdateMenuVisibility(r10);
            goto L_0x019b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.MultiPhoneWindow.PenWindowController.onTouch(android.view.View, android.view.MotionEvent):boolean");
        }

        public boolean onHover(View v, MotionEvent event) {
            try {
                if (event.getAction() == 9 || event.getAction() == 7) {
                    if (v.getId() == R.id.windowTitleBar) {
                        PointerIcon.setHoveringSpenIcon(5, -1);
                    } else if (this.mHeaderWindowControllerHoverImage != null) {
                        PointerIcon.setHoveringSpenIcon(0, this.mHeaderWindowControllerHoverImage);
                    } else {
                        setHeaderWindowControllerHoverImage();
                    }
                    return false;
                }
                if (event.getAction() == 10) {
                    PointerIcon.setHoveringSpenIcon(1, -1);
                }
                return false;
            } catch (RemoteException e) {
                Log.e(MultiPhoneWindow.TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
            }
        }

        private void setHeaderWindowControllerHoverImage() {
            this.mHeaderWindowControllerHoverImage = MultiPhoneWindow.this.mContext.getResources().getDrawable(R.drawable.multi_window_centerbar_hover_icon, null);
        }
    }

    protected class HeaderWindowController extends PenWindowController {
        private ImageView mDragAndDropView;
        protected boolean mIsAttached = false;
        private FloatingMenuView mLocalMenuContainer;
        private ResizeVisualCue mResizeVisualCue;
        private LayoutParams mWindowAttributes;
        private WindowManager mWindowManager;

        private class FloatingMenuView extends FrameLayout implements OnTouchListener {
            private ArrayList<View> mAvailableButtons = new ArrayList();
            private View mFloatingMenu = LayoutInflater.from(getContext()).inflate((int) R.layout.multiwindow_floating_menu, null);
            private View mFloatingMenuBg = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg);
            private boolean mFloatingMenuCloseAnimating = false;
            private LinearLayout mFloatingMenuContainer;
            private boolean mFloatingMenuOpenAnimating = false;
            private boolean mIsAttached;
            private WindowManager mWindowManager;

            public FloatingMenuView(Context context) {
                super(context);
                this.mFloatingMenuBg.setLayoutDirection(0);
                this.mFloatingMenuContainer = (LinearLayout) this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_buttons);
                RippleDrawable ripple = (RippleDrawable) this.mFloatingMenuContainer.getBackground();
                if (ripple != null) {
                    ripple.setColor(ColorStateList.valueOf((16777215 & this.mContext.getResources().getColor(R.color.multiwindow_centerbar_menu_press_tint_color)) | 1711276032));
                }
                if (this.mFloatingMenuContainer.getFitsSystemWindows()) {
                    this.mFloatingMenuContainer.setFitsSystemWindows(false);
                }
                addView(this.mFloatingMenu);
                this.mWindowManager = (WindowManager) getContext().getSystemService("window");
            }

            public void checkAvailableButtonsForAnim() {
                this.mAvailableButtons.clear();
                if (HeaderWindowController.this.mBtnDragAndDrop.getVisibility() != 8) {
                    this.mAvailableButtons.add(HeaderWindowController.this.mBtnDragAndDrop);
                }
                if (HeaderWindowController.this.mBtnMinimize.getVisibility() != 8) {
                    this.mAvailableButtons.add(HeaderWindowController.this.mBtnMinimize);
                }
                if (HeaderWindowController.this.mBtnMaximize.getVisibility() != 8) {
                    this.mAvailableButtons.add(HeaderWindowController.this.mBtnMaximize);
                }
                if (HeaderWindowController.this.mBtnExit.getVisibility() != 8) {
                    this.mAvailableButtons.add(HeaderWindowController.this.mBtnExit);
                }
                View leftImageView = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg_left);
                View rightImageView = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg_right);
                View centerImageView = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg_center);
                this.mFloatingMenu.measure(0, 0);
                MultiPhoneWindow.this.mFloatingMenuRightMargin = View.resolveSize(((MarginLayoutParams) HeaderWindowController.this.mLocalMenuContainer.mFloatingMenuContainer.getLayoutParams()).rightMargin, 0);
                RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) this.mFloatingMenuContainer.getLayoutParams();
                centerImageView.getLayoutParams().width = (((this.mFloatingMenuContainer.getMeasuredWidth() + containerParams.leftMargin) + containerParams.rightMargin) - leftImageView.getMeasuredWidth()) - rightImageView.getMeasuredWidth();
                Iterator i$ = this.mAvailableButtons.iterator();
                while (i$.hasNext()) {
                    View button = (View) i$.next();
                    if (button.getFitsSystemWindows()) {
                        button.setFitsSystemWindows(false);
                    }
                    button.setOnTouchListener(this);
                }
            }

            public void show(int x, int y, boolean autoclose) {
                if (!this.mIsAttached) {
                    Log.d(MultiPhoneWindow.TAG, "show menu at (" + x + ", " + y + ")");
                    LayoutParams lp = generateLayoutParam();
                    lp.x = x;
                    lp.y = y;
                    HeaderWindowController.this.mBtnDragAndDrop.setHoverPopupType(0);
                    HeaderWindowController.this.mBtnMinimize.setHoverPopupType(0);
                    HeaderWindowController.this.mBtnMaximize.setHoverPopupType(0);
                    HeaderWindowController.this.mBtnExit.setHoverPopupType(0);
                    this.mWindowManager.addView(this, lp);
                    animateFloatingMenuOpen(autoclose);
                    this.mIsAttached = true;
                }
            }

            public void dismiss(boolean anim) {
                if (this.mIsAttached) {
                    Log.d(MultiPhoneWindow.TAG, "dismiss menu with anim=" + anim);
                    if (anim) {
                        animateFloatingMenuClose();
                    } else {
                        removeSelf();
                    }
                }
            }

            private void removeSelf() {
                if (this.mFloatingMenuOpenAnimating || this.mFloatingMenuCloseAnimating) {
                    this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_left).clearAnimation();
                    this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_right).clearAnimation();
                    this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_center).clearAnimation();
                    Iterator i$ = this.mAvailableButtons.iterator();
                    while (i$.hasNext()) {
                        ((View) i$.next()).clearAnimation();
                    }
                    this.mFloatingMenuCloseAnimating = false;
                    this.mFloatingMenuOpenAnimating = false;
                }
                this.mWindowManager.removeView(this);
                this.mIsAttached = false;
            }

            private void setFloatingButtonHoverListener() {
                int hoverPopupTopMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_hoverwindow_margin_top);
                Rect stackBound = new Rect(MultiPhoneWindow.this.getStackBound());
                HeaderWindowController.this.mBtnDragAndDrop.setHoverPopupType(1);
                if (HeaderWindowController.this.mBtnDragAndDrop.getHoverPopupWindow() != null) {
                    HeaderWindowController.this.mBtnDragAndDrop.getHoverPopupWindow().setPopupPosOffset((-stackBound.left) - (HeaderWindowController.this.mBtnDragAndDrop.getWidth() / 2), hoverPopupTopMargin - stackBound.top);
                    HeaderWindowController.this.mBtnDragAndDrop.getHoverPopupWindow().setPopupGravity(12341);
                }
                HeaderWindowController.this.mBtnMinimize.setHoverPopupType(1);
                if (HeaderWindowController.this.mBtnMinimize.getHoverPopupWindow() != null) {
                    HeaderWindowController.this.mBtnMinimize.getHoverPopupWindow().setPopupPosOffset((-stackBound.left) - (HeaderWindowController.this.mBtnMinimize.getWidth() / 2), hoverPopupTopMargin - stackBound.top);
                    HeaderWindowController.this.mBtnMinimize.getHoverPopupWindow().setPopupGravity(12341);
                }
                HeaderWindowController.this.mBtnMaximize.setHoverPopupType(1);
                if (HeaderWindowController.this.mBtnMaximize.getHoverPopupWindow() != null) {
                    HeaderWindowController.this.mBtnMaximize.getHoverPopupWindow().setPopupPosOffset((-stackBound.left) - (HeaderWindowController.this.mBtnMaximize.getWidth() / 2), hoverPopupTopMargin - stackBound.top);
                    HeaderWindowController.this.mBtnMaximize.getHoverPopupWindow().setPopupGravity(12341);
                }
                HeaderWindowController.this.mBtnExit.setHoverPopupType(1);
                if (HeaderWindowController.this.mBtnExit.getHoverPopupWindow() != null) {
                    HeaderWindowController.this.mBtnExit.getHoverPopupWindow().setPopupPosOffset((-stackBound.left) - (HeaderWindowController.this.mBtnExit.getWidth() / 2), hoverPopupTopMargin - stackBound.top);
                    HeaderWindowController.this.mBtnExit.getHoverPopupWindow().setPopupGravity(12341);
                }
            }

            private void animateFloatingMenuOpen(final boolean autoclose) {
                if (!this.mFloatingMenuOpenAnimating && this.mAvailableButtons.size() > 0) {
                    int offset;
                    this.mFloatingMenuCloseAnimating = false;
                    this.mFloatingMenuOpenAnimating = true;
                    View leftBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_left);
                    View rightBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_right);
                    View centerBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_center);
                    Animation centerAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.multiwindow_floating_menu_open_center);
                    centerAnim.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                            if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                                MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(4);
                            }
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            FloatingMenuView.this.mFloatingMenuOpenAnimating = false;
                            FloatingMenuView.this.setFloatingButtonHoverListener();
                            if (autoclose) {
                                MultiPhoneWindow.this.mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        FloatingMenuView.this.animateFloatingMenuClose();
                                        if (MultiPhoneWindow.this.mIsSupportSimplificationUI && HeaderWindowController.this.mResizeVisualCue != null) {
                                            HeaderWindowController.this.mResizeVisualCue.playAnimation(true);
                                        }
                                    }
                                }, 500);
                                if (MultiPhoneWindow.this.mIsSupportSimplificationUI) {
                                    MultiPhoneWindow.this.mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            if (HeaderWindowController.this.mResizeVisualCue != null) {
                                                HeaderWindowController.this.mResizeVisualCue.playAnimation(false);
                                            }
                                        }
                                    }, 2500);
                                }
                            }
                            MultiPhoneDecorView decorView = (MultiPhoneDecorView) MultiPhoneWindow.this.getDecorView();
                            if (decorView.mIsResize && MultiPhoneWindow.this.mContentLayoutGenerated) {
                                MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(4);
                            }
                            Log.d(MultiPhoneWindow.TAG, "animateFloatingMenuOpen anim is end. autoClose=" + autoclose + " isResizing now=" + decorView.mIsResize);
                        }
                    });
                    centerBackground.startAnimation(centerAnim);
                    leftBackground.startAnimation(getButtonAnimation(true, true));
                    rightBackground.startAnimation(getButtonAnimation(true, false));
                    int left = 0;
                    int right = this.mAvailableButtons.size() - 1;
                    int closer = ((this.mAvailableButtons.size() / 2) + (this.mAvailableButtons.size() % 2)) - 1;
                    if (this.mAvailableButtons.size() % 2 == 0) {
                        offset = closer + 1;
                    } else {
                        offset = closer;
                    }
                    while (left <= right) {
                        Animation a = AnimationUtils.loadAnimation(this.mContext, R.anim.multiwindow_floating_menu_buttons_fadein);
                        a.setStartOffset((long) (offset * 100));
                        if (left != right) {
                            ((View) this.mAvailableButtons.get(right)).startAnimation(a);
                        }
                        ((View) this.mAvailableButtons.get(left)).startAnimation(a);
                        left++;
                        right--;
                        offset--;
                    }
                }
            }

            private void animateFloatingMenuClose() {
                if (!this.mFloatingMenuCloseAnimating && this.mAvailableButtons.size() > 0) {
                    this.mFloatingMenuCloseAnimating = true;
                    this.mFloatingMenuOpenAnimating = false;
                    View leftBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_left);
                    View rightBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_right);
                    View centerBackground = this.mFloatingMenuBg.findViewById(R.id.multiwindow_floating_menu_bg_center);
                    Animation centerAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.multiwindow_floating_menu_close_center);
                    centerAnim.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                            if (MultiPhoneWindow.DEBUG) {
                                Log.i(MultiPhoneWindow.TAG, "animateFloatingMenuClose anim is started, mActivity=" + MultiPhoneWindow.this.mActivity);
                            }
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            MultiPhoneDecorView decorView = (MultiPhoneDecorView) MultiPhoneWindow.this.getDecorView();
                            if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                                if (decorView.mIsResize) {
                                    MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(4);
                                } else {
                                    MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(0);
                                }
                            }
                            FloatingMenuView.this.mFloatingMenuCloseAnimating = false;
                            FloatingMenuView.this.removeSelf();
                            if (MultiPhoneWindow.DEBUG) {
                                Log.d(MultiPhoneWindow.TAG, "animateFloatingMenuClose anim is end, mActivity=" + MultiPhoneWindow.this.mActivity);
                            }
                        }
                    });
                    centerBackground.startAnimation(centerAnim);
                    leftBackground.startAnimation(getButtonAnimation(false, true));
                    rightBackground.startAnimation(getButtonAnimation(false, false));
                    int left = 0;
                    int right = this.mAvailableButtons.size() - 1;
                    int offset = 0;
                    while (left <= right) {
                        Animation a = AnimationUtils.loadAnimation(this.mContext, R.anim.multiwindow_floating_menu_buttons_fadeout);
                        a.setStartOffset((long) (offset * 100));
                        if (left != right) {
                            ((View) this.mAvailableButtons.get(right)).startAnimation(a);
                        }
                        ((View) this.mAvailableButtons.get(left)).startAnimation(a);
                        left++;
                        right--;
                        offset++;
                    }
                }
            }

            private TranslateAnimation getButtonAnimation(boolean open, boolean left) {
                int measureWidth;
                View leftBackground = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg_left);
                View rightBackground = this.mFloatingMenu.findViewById(R.id.multiwindow_floating_menu_bg_right);
                TranslateAnimation t = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                if (MultiPhoneWindow.this.mTargetSdkVersion < 19) {
                    measureWidth = HeaderWindowController.this.mLocalMenuContainer.mFloatingMenu.getMeasuredWidth() + MultiPhoneWindow.this.mFloatingMenuRightMargin;
                } else {
                    measureWidth = HeaderWindowController.this.mLocalMenuContainer.mFloatingMenu.getMeasuredWidth();
                }
                int showposition = (measureWidth / 2) - ((leftBackground.getMeasuredWidth() + rightBackground.getMeasuredWidth()) / 2);
                if (!open) {
                    if (left) {
                        t = new TranslateAnimation(0, 0.0f, 0, (float) showposition, 0, 0.0f, 0, 0.0f);
                    } else {
                        t = new TranslateAnimation(0, 0.0f, 0, (float) (-showposition), 0, 0.0f, 0, 0.0f);
                    }
                    t.setStartOffset(200);
                } else if (left) {
                    t = new TranslateAnimation(0, (float) showposition, 0, 0.0f, 0, 0.0f, 0, 0.0f);
                } else {
                    t = new TranslateAnimation(0, (float) (-showposition), 0, 0.0f, 0, 0.0f, 0, 0.0f);
                }
                t.setFillAfter(true);
                t.setInterpolator(this.mContext, R.anim.accelerate_interpolator);
                t.setDuration((long) this.mContext.getResources().getInteger(R.integer.multiwindow_floating_menu_anim_duration));
                return t;
            }

            public boolean dispatchTouchEvent(MotionEvent ev) {
                if ((ev.getAction() & 255) == 4) {
                    dismiss(true);
                }
                return super.dispatchTouchEvent(ev);
            }

            private LayoutParams generateLayoutParam() {
                LayoutParams lp = new LayoutParams();
                lp.gravity = 8388659;
                lp.width = -2;
                lp.height = -2;
                lp.format = -2;
                lp.type = 1007;
                lp.setTitle("PenWindow Titlebar");
                lp.token = MultiPhoneWindow.this.getDecorView().getWindowToken();
                lp.flags = 262664;
                lp.multiWindowFlags |= 8;
                return lp;
            }

            public boolean onTouch(View v, MotionEvent event) {
                boolean result = v.onTouchEvent(event);
                if (event.getAction() == 0) {
                    int layoutLeft;
                    int layoutRight;
                    RippleDrawable ripple = (RippleDrawable) this.mFloatingMenuContainer.getBackground();
                    int rippleSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_menu_button_ripple_size);
                    if (rippleSize > v.getWidth()) {
                        layoutLeft = v.getLeft() - ((rippleSize - v.getWidth()) / 2);
                        layoutRight = v.getRight() + ((rippleSize - v.getWidth()) / 2);
                    } else {
                        layoutLeft = v.getLeft() + ((v.getWidth() - rippleSize) / 2);
                        layoutRight = v.getRight() - ((v.getWidth() - rippleSize) / 2);
                    }
                    ripple.setHotspotBounds(layoutLeft, v.getTop(), layoutRight, v.getBottom());
                    this.mFloatingMenuContainer.drawableHotspotChanged(v.getX() + event.getX(), v.getY() + event.getY());
                    this.mFloatingMenuContainer.setPressed(true);
                } else if (event.getAction() != 2) {
                    this.mFloatingMenuContainer.setPressed(false);
                }
                return result;
            }
        }

        private class HeaderGestureDetectorListener implements OnGestureListener {
            private HeaderGestureDetectorListener() {
            }

            public boolean onDown(MotionEvent e) {
                return false;
            }

            public void onShowPress(MotionEvent e) {
            }

            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            public void onLongPress(MotionEvent e) {
                MultiPhoneWindow.this.mTmpBound.set(MultiPhoneWindow.this.getStackBound());
                MultiPhoneWindow.this.showGuide(MultiPhoneWindow.this.mTmpBound, 3);
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        }

        class ResizeVisualCue extends FrameLayout {
            private static final int CUE_COUNT = 4;
            private ImageView[] mCueViews;
            Animation mEndAnimation;
            boolean mIsAttached;
            Animation mStartAnimation;
            int mThicknessBorer;
            WindowManager mWindowManager;

            private ResizeVisualCue(Context context) {
                super(context);
                this.mCueViews = new ImageView[4];
                this.mWindowManager = (WindowManager) getContext().getSystemService("window");
                this.mThicknessBorer = context.getResources().getDimensionPixelSize(R.dimen.multiwindow_borderline_thickness);
                for (int i = 0; i < 4; i++) {
                    this.mCueViews[i] = new ImageView(context);
                    this.mCueViews[i].setImageResource(R.drawable.multiwindow_popupwindow_resizing_visual_cue);
                    this.mCueViews[i].setVisibility(4);
                }
                this.mCueViews[0].setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 53));
                this.mCueViews[1].setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 85));
                this.mCueViews[2].setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 83));
                this.mCueViews[3].setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 51));
            }

            private LayoutParams generateLayoutParam() {
                LayoutParams lp = new LayoutParams();
                lp.gravity = 8388659;
                lp.width = (MultiPhoneWindow.this.mStackBound.width() + this.mCueViews[0].getDrawable().getIntrinsicWidth()) - this.mThicknessBorer;
                lp.height = (MultiPhoneWindow.this.mStackBound.height() + this.mCueViews[0].getDrawable().getIntrinsicHeight()) - this.mThicknessBorer;
                lp.x -= (this.mCueViews[0].getDrawable().getIntrinsicWidth() / 2) - (this.mThicknessBorer / 2);
                lp.y -= (this.mCueViews[0].getDrawable().getIntrinsicHeight() / 2) - (this.mThicknessBorer / 2);
                lp.format = -2;
                lp.type = 1007;
                lp.setTitle("PenWindow ResizeVisualCue");
                lp.token = MultiPhoneWindow.this.getDecorView().getWindowToken();
                lp.flags = 536;
                lp.multiWindowFlags |= 8;
                return lp;
            }

            void addWindow() {
                if (MultiPhoneWindow.this.mActivity != null && !this.mIsAttached) {
                    if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                        Log.i(MultiPhoneWindow.TAG, "addWindow ResizeVisualCue");
                    }
                    this.mWindowManager.addView(this, generateLayoutParam());
                    for (int i = 0; i < 4; i++) {
                        addView(this.mCueViews[i]);
                    }
                    this.mIsAttached = true;
                }
            }

            void removeWindow() {
                if (this.mIsAttached) {
                    MultiPhoneWindow.this.mHandler.post(new Runnable() {
                        public void run() {
                            ResizeVisualCue.this.cancelAnimation();
                        }
                    });
                    for (int i = 0; i < 4; i++) {
                        removeView(this.mCueViews[i]);
                    }
                    this.mWindowManager.removeView(this);
                    this.mIsAttached = false;
                }
            }

            void show() {
                if (this.mIsAttached) {
                    for (int i = 0; i < 4; i++) {
                        this.mCueViews[i].setVisibility(0);
                    }
                }
            }

            void hide() {
                if (this.mIsAttached) {
                    for (int i = 0; i < 4; i++) {
                        this.mCueViews[i].setVisibility(4);
                    }
                }
            }

            void setAnimation() {
                this.mStartAnimation = new AlphaAnimation(0.0f, 1.0f);
                this.mStartAnimation.setDuration(400);
                this.mStartAnimation.setInterpolator(new SineInOut33());
                this.mStartAnimation.setFillAfter(true);
                this.mStartAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                            Log.i(MultiPhoneWindow.TAG, "mStartAnimation : onAnimationStart() for ResizeVisualCue");
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                            Log.i(MultiPhoneWindow.TAG, "mStartAnimation : onAnimationEnd() for ResizeVisualCue");
                        }
                    }
                });
                this.mEndAnimation = new AlphaAnimation(1.0f, 0.0f);
                this.mEndAnimation.setDuration(200);
                this.mEndAnimation.setInterpolator(new SineInOut33());
                this.mEndAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                            Log.i(MultiPhoneWindow.TAG, "mEndAnimation : onAnimationStart() for ResizeVisualCue");
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                            Log.i(MultiPhoneWindow.TAG, "mEndAnimation : onAnimationEnd() for ResizeVisualCue");
                        }
                        HeaderWindowController.this.removeResizeVisualCue();
                    }
                });
                if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                    Log.i(MultiPhoneWindow.TAG, "setAnimation for ResizeVisualCue");
                }
            }

            void cancelAnimation() {
                if (this.mStartAnimation != null) {
                    this.mStartAnimation.cancel();
                    this.mStartAnimation = null;
                    if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                        Log.i(MultiPhoneWindow.TAG, "Cancel mStartAnimation of ResizeVisualCue");
                    }
                }
                if (this.mEndAnimation != null) {
                    this.mEndAnimation.cancel();
                    this.mEndAnimation = null;
                    if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                        Log.i(MultiPhoneWindow.TAG, "Cancel mEndAnimation of ResizeVisualCue");
                    }
                }
            }

            void playAnimation(boolean startAnimation) {
                int i;
                if (startAnimation) {
                    if (this.mStartAnimation != null) {
                        show();
                        for (i = 0; i < 4; i++) {
                            this.mCueViews[i].startAnimation(this.mStartAnimation);
                        }
                        if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                            Log.i(MultiPhoneWindow.TAG, "Play mStartAnimation of ResizeVisualCue");
                        }
                    }
                } else if (this.mEndAnimation != null) {
                    for (i = 0; i < 4; i++) {
                        this.mCueViews[i].startAnimation(this.mEndAnimation);
                    }
                    if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                        Log.i(MultiPhoneWindow.TAG, "Play mEndAnimation of ResizeVisualCue");
                    }
                }
            }
        }

        public HeaderWindowController() {
            super();
            this.mWindowManager = (WindowManager) MultiPhoneWindow.this.mContext.getSystemService("window");
            setHeaderGestureDetector(new GestureDetector(MultiPhoneWindow.this.mContext, new HeaderGestureDetectorListener()));
        }

        protected void performInflateController() {
            this.mPenWindowHeader = new FrameLayout(MultiPhoneWindow.this.mContext);
            this.mPenWindowHeader.setClickable(true);
            this.mPenWindowHeader.setBackgroundTintList(null);
            ((FrameLayout) this.mPenWindowHeader).setForegroundTintList(null);
            this.mDragAndDropView = new ImageView(MultiPhoneWindow.this.mContext);
            this.mDragAndDropView.setImageTintList(null);
            this.mDragAndDropView.setImageResource(R.drawable.multiwindow_ic_drag_drop);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
            lp.gravity = 17;
            ((FrameLayout) this.mPenWindowHeader).addView(this.mDragAndDropView, (ViewGroup.LayoutParams) lp);
            this.mDragAndDropView.setVisibility(4);
            performUpdateBackground();
            View floatingMenuView = new FloatingMenuView(new ContextThemeWrapper(MultiPhoneWindow.this.mContext, (int) R.style.Theme_DeviceDefault_Light));
            this.mLocalMenuContainer = floatingMenuView;
            this.mMenuContainer = floatingMenuView;
            Drawable drw = MultiPhoneWindow.this.mContext.getResources().getDrawable(R.drawable.multi_window_drag_drop_handler, null);
            if (drw != null) {
                this.mControllerHeight = drw.getIntrinsicHeight();
            }
        }

        protected void updateAvailableButtons() {
            if (this.mLocalMenuContainer != null) {
                this.mLocalMenuContainer.checkAvailableButtonsForAnim();
            }
        }

        protected void removeResizeVisualCue() {
            if (this.mResizeVisualCue != null) {
                if (MultiPhoneWindow.DEBUG_RESIZE_VISUAL_CUE) {
                    Log.i(MultiPhoneWindow.TAG, "removeWindow ResizeVisualCue");
                }
                this.mResizeVisualCue.removeWindow();
                this.mResizeVisualCue.hide();
                this.mResizeVisualCue = null;
            }
        }

        protected void performUpdateVisibility(boolean visible) {
            MultiPhoneWindow.this.updateIsFullScreen();
            if (!MultiPhoneWindow.this.mIsFullScreen) {
                Log.d(MultiPhoneWindow.TAG, "performUpdateVisibility, not full screen ignore visible [" + visible + "] request");
                if (this.mIsAttached) {
                    this.mWindowManager.removeViewImmediate(this.mPenWindowHeader);
                    this.mIsAttached = false;
                }
            } else if (this.mIsShowing == visible && !this.mNeedInvalidate) {
                Log.d(MultiPhoneWindow.TAG, "performUpdateVisibility, same visibility " + visible);
            } else if (MultiPhoneWindow.this.mContentLayoutGenerated && MultiPhoneWindow.this.mPenWindowController.getWindowToken() == null) {
                if (MultiPhoneWindow.DEBUG_TAB) {
                    Log.d("TAG", "token is null");
                }
            } else if (!visible || MultiPhoneWindow.this.getDecorView().getVisibility() == 0) {
                this.mIsShowing = visible;
                this.mNeedInvalidate = false;
                if (visible) {
                    this.mWindowAttributes = generateLayoutParam();
                    this.mWindowAttributes.x = (((int) (((float) MultiPhoneWindow.this.getStackBound().width()) * MultiPhoneWindow.this.mDssScale)) - this.mControllerHeight) / 2;
                    LayoutParams layoutParams = this.mWindowAttributes;
                    layoutParams.y -= this.mControllerHeight / 2;
                    if (MultiPhoneWindow.this.needTitleBar(MultiPhoneWindow.this.getMultiWindowStyle())) {
                        this.mPenWindowHeader.setVisibility(0);
                        if (this.mIsAttached) {
                            this.mWindowManager.updateViewLayout(this.mPenWindowHeader, this.mWindowAttributes);
                        } else {
                            try {
                                this.mWindowManager.addView(this.mPenWindowHeader, this.mWindowAttributes);
                                this.mIsAttached = true;
                                if (this.mDragAndDropView.getVisibility() != 0 && MultiPhoneWindow.this.mMultiWindowFacade.getDragAndDropModeOfStack(MultiPhoneWindow.this.mToken)) {
                                    Log.i(MultiPhoneWindow.TAG, "mDragAndDropView visible");
                                    this.mDragAndDropView.setVisibility(0);
                                }
                            } catch (Exception e) {
                                this.mPenWindowHeader.setVisibility(4);
                                return;
                            }
                        }
                    }
                    this.mPenWindowHeader.setVisibility(8);
                    if (MultiPhoneWindow.this.mMultiWindowFacade.needToExposureTitleBarMenu()) {
                        if (MultiPhoneWindow.this.mIsSupportSimplificationUI) {
                            MultiPhoneWindow.this.mPenWindowController.removeResizeVisualCue();
                            this.mResizeVisualCue = new ResizeVisualCue(MultiPhoneWindow.this.mContext);
                            this.mResizeVisualCue.addWindow();
                            this.mResizeVisualCue.setAnimation();
                        }
                        MultiPhoneWindow.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (MultiPhoneWindow.this.mActivity == null || !MultiPhoneWindow.this.mActivity.isResumed()) {
                                    Log.w(MultiPhoneWindow.TAG, "skip showMneu(true) in Runnable, mActivity is abnormal state");
                                } else {
                                    HeaderWindowController.this.showMenu(true);
                                }
                            }
                        }, 500);
                        return;
                    }
                    return;
                }
                this.mLocalMenuContainer.dismiss(false);
                if (this.mIsAttached) {
                    this.mWindowManager.removeViewImmediate(this.mPenWindowHeader);
                    this.mIsAttached = false;
                }
            } else {
                Log.d(MultiPhoneWindow.TAG, "performUpdateVisibility(), Skip add mPenWindowHeader, DecorView is not visible");
            }
        }

        protected void updatePosition() {
            if (this.mPenWindowHeader != null) {
                this.mWindowAttributes = generateLayoutParam();
                this.mWindowAttributes.x = (((int) (((float) MultiPhoneWindow.this.getStackBound().width()) * MultiPhoneWindow.this.mDssScale)) - this.mControllerHeight) / 2;
                LayoutParams layoutParams = this.mWindowAttributes;
                layoutParams.y -= this.mControllerHeight / 2;
                try {
                    this.mWindowManager.updateViewLayout(this.mPenWindowHeader, this.mWindowAttributes);
                } catch (Exception e) {
                    Log.w(MultiPhoneWindow.TAG, "Fail MultiPhoneWindow::updatePosition(), mActivity=" + MultiPhoneWindow.this.mActivity);
                }
            }
        }

        protected void performUpdateBackground() {
            this.mPenWindowHeader.setBackgroundResource(R.drawable.multi_window_drag_drop_handler);
        }

        protected void performStartDragMode() {
            super.performStartDragMode();
            this.mLocalMenuContainer.dismiss(false);
            if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(0);
            }
            this.mDragAndDropView.setVisibility(0);
        }

        protected void performStopDragMode() {
            super.performStopDragMode();
            this.mDragAndDropView.setVisibility(4);
        }

        protected void performUpdateMenuVisibility(boolean visible) {
            super.performUpdateMenuVisibility(visible);
            if (visible) {
                showMenu();
            } else if (MultiPhoneWindow.this.mIsFinishing || !MultiPhoneWindow.this.mIsAttachedToWindow) {
                if (MultiPhoneWindow.DEBUG) {
                    Log.i(MultiPhoneWindow.TAG, "performUpdateMenuVisibility, mActivity=" + MultiPhoneWindow.this.mActivity + ", visible=" + visible + ", mIsFinishing=" + MultiPhoneWindow.this.mIsFinishing + ", mIsAttachedToWindow=" + MultiPhoneWindow.this.mIsAttachedToWindow);
                }
                this.mLocalMenuContainer.dismiss(false);
            } else {
                this.mLocalMenuContainer.dismiss(true);
            }
        }

        private LayoutParams generateLayoutParam() {
            LayoutParams lp = new LayoutParams();
            lp.setTitle("MultiWindow Controller");
            lp.gravity = 8388659;
            lp.width = -2;
            lp.height = -2;
            lp.format = -2;
            lp.type = 1006;
            lp.token = MultiPhoneWindow.this.getDecorView().getWindowToken();
            lp.flags = 520;
            lp.multiWindowFlags |= 8;
            return lp;
        }

        private void showMenu() {
            showMenu(false);
        }

        private void showMenu(boolean autoclose) {
            if (this.mIsAttached) {
                int x = (((int) (((float) MultiPhoneWindow.this.getStackBound().width()) * MultiPhoneWindow.this.mDssScale)) - this.mLocalMenuContainer.mFloatingMenu.getMeasuredWidth()) / 2;
                this.mLocalMenuContainer.show(x, this.mWindowAttributes.y, autoclose);
                this.mLocalMenuContainer.show(x, this.mWindowAttributes.y, autoclose);
                if (MultiWindowFeatures.isSupportCenterbarClickSound(MultiPhoneWindow.this.mContext)) {
                    if (MultiPhoneWindow.this.mSoundPool != null && MultiPhoneWindow.this.mHeaderButtonSoundId != -1) {
                        MultiPhoneWindow.this.mSoundPool.play(MultiPhoneWindow.this.mHeaderButtonSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
                    }
                } else if (MultiPhoneWindow.this.mAudioManager != null) {
                    MultiPhoneWindow.this.mAudioManager.playSoundEffect(100);
                }
            }
        }
    }

    private final class MultiPhoneDecorView extends DecorView {
        private boolean mConsumeHoverEvent = false;
        private float mFixedRatio = 0.0f;
        private Rect mGuideViewBound = new Rect();
        private boolean mIsResize = false;
        private boolean mLastCorner;
        private boolean mLastHoverEdge;
        private int mLastMoveX;
        private int mLastMoveY;
        private int mMaxHeight = 0;
        private int mMaxWidth = 0;
        private int mMinHeight = 0;
        private int mMinWidth = 0;
        private int mScreenHeight = 0;
        private int mScreenWidth = 0;
        private EdgeInspector mTouchEdgeInspector = new EdgeInspector(null, null, MultiPhoneWindow.this.mIsSupportDiagonalResizable);

        public MultiPhoneDecorView(Context context, int featureId) {
            super(context, featureId);
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            boolean fixedRatio = false;
            MultiWindowStyle style = getMultiWindowStyle();
            if (!style.isCascade() || style.isEnabled(4)) {
                MultiPhoneWindow.this.dismissGuide();
                return super.dispatchTouchEvent(ev);
            } else if (ev.getY() / MultiPhoneWindow.this.mDssScale >= ((float) MultiPhoneWindow.this.mTitleBarHeight) || ev.getY() / MultiPhoneWindow.this.mDssScale < 0.0f || this.mIsResize || (MultiPhoneWindow.this.mIsSupportDiagonalResizable && ((float) (MultiPhoneWindow.this.getStackBound().left + (MultiPhoneWindow.this.mResizablePadding.left * 2))) >= ev.getRawXForScaledWindow())) {
                switch (ev.getAction()) {
                    case 0:
                        if ((ev.getButtonState() & 2) != 0) {
                            MultiPhoneWindow.this.mIsPenButtonPressed = true;
                        }
                        MultiPhoneWindow.this.initStackBound();
                        this.mTouchEdgeInspector.clear();
                        Rect rect = new Rect();
                        rect.set(MultiPhoneWindow.this.getStackBound());
                        this.mTouchEdgeInspector.set(rect, MultiPhoneWindow.this.mResizablePadding);
                        this.mLastMoveX = (int) ev.getRawXForScaledWindow();
                        this.mLastMoveY = (int) ev.getRawYForScaledWindow();
                        this.mTouchEdgeInspector.check(this.mLastMoveX, this.mLastMoveY);
                        this.mContext.sendBroadcast(new Intent("com.sec.android.OUTSIDE_TOUCH"));
                        if (this.mTouchEdgeInspector.isEdge() && !MultiPhoneWindow.this.mIsPenButtonPressed && (!MultiPhoneWindow.this.mIsSupportDiagonalResizable || this.mTouchEdgeInspector.isCorner())) {
                            MultiPhoneWindow.this.forceHideInputMethod();
                            MultiPhoneWindow.this.getDecorView().performHapticFeedback(0);
                            this.mIsResize = true;
                            MultiPhoneWindow.this.mTmpBound.set(MultiPhoneWindow.this.getStackBound());
                            this.mGuideViewBound.set(MultiPhoneWindow.this.mTmpBound);
                            initResizePenWindow();
                            MultiPhoneWindow.this.showGuide(MultiPhoneWindow.this.mTmpBound, 0);
                            if (!MultiPhoneWindow.this.mContentLayoutGenerated) {
                                return true;
                            }
                            MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setBackgroundColor(0);
                            if (!MultiPhoneWindow.this.mIsSupportSimplificationUI) {
                                return true;
                            }
                            MultiPhoneWindow.this.mPenWindowController.removeResizeVisualCue();
                            return true;
                        }
                    case 1:
                    case 3:
                        MultiPhoneWindow.this.mIsPenButtonPressed = false;
                        this.mIsResize = false;
                        this.mFixedRatio = 0.0f;
                        if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                            MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(0);
                        }
                        MultiPhoneWindow.this.dismissGuide();
                        if (this.mTouchEdgeInspector != null) {
                            if (!this.mTouchEdgeInspector.isEdge()) {
                                this.mTouchEdgeInspector.clear();
                                break;
                            } else if (!MultiPhoneWindow.this.mIsSupportDiagonalResizable || this.mTouchEdgeInspector.isCorner()) {
                                if ((this.mGuideViewBound.width() <= this.mMaxWidth || this.mMaxWidth <= 0) && ((this.mGuideViewBound.height() <= this.mMaxHeight || this.mMaxHeight <= 0) && !isExceededLimitTop(this.mGuideViewBound))) {
                                    if (MultiPhoneWindow.this.validateStackBound(this.mGuideViewBound)) {
                                        int stackOrientation = this.mGuideViewBound.height() > this.mGuideViewBound.width() ? 1 : 2;
                                        MultiPhoneWindow.this.mTmpBound.set(this.mGuideViewBound);
                                        MultiPhoneWindow.this.checkMaxStackSize(MultiPhoneWindow.this.mTmpBound, stackOrientation);
                                        if (!MultiPhoneWindow.this.mTmpBound.equals(this.mGuideViewBound)) {
                                            if (MultiPhoneWindow.DEBUG) {
                                                Log.w(MultiPhoneWindow.TAG, "change guideview bounds before set, old=" + this.mGuideViewBound + ", new=" + MultiPhoneWindow.this.mTmpBound + ", " + MultiPhoneWindow.this.mActivity);
                                            }
                                            this.mGuideViewBound.set(MultiPhoneWindow.this.mTmpBound);
                                        }
                                        MultiPhoneWindow.this.setStackBound(this.mGuideViewBound);
                                        if (MultiPhoneWindow.this.mPenWindowController.mIsShowing && MultiPhoneWindow.this.mContentLayoutGenerated) {
                                            MultiPhoneWindow.this.mPenWindowController.performUpdateBackground();
                                            MultiPhoneWindow.this.mPenWindowController.updatePosition();
                                            MultiPhoneWindow.this.mPenWindowController.performUpdateVisibility(true);
                                        }
                                    }
                                    MultiPhoneWindow.this.mTmpBound.setEmpty();
                                    this.mGuideViewBound.setEmpty();
                                    MultiPhoneWindow.this.adjustScaleFactor();
                                    return true;
                                }
                                MultiPhoneWindow.this.requestState(1);
                                return true;
                            }
                        }
                        break;
                    case 2:
                        if ((MultiPhoneWindow.this.mIsSupportDiagonalResizable || !this.mTouchEdgeInspector.isEdge()) && (!MultiPhoneWindow.this.mIsSupportDiagonalResizable || !this.mTouchEdgeInspector.isCorner())) {
                            if (this.mTouchEdgeInspector.isCandidate()) {
                                this.mLastMoveX = (int) ev.getRawXForScaledWindow();
                                this.mLastMoveY = (int) ev.getRawYForScaledWindow();
                                this.mTouchEdgeInspector.check(this.mLastMoveX, this.mLastMoveY);
                                if ((!MultiPhoneWindow.this.mIsSupportDiagonalResizable && this.mTouchEdgeInspector.isEdge()) || this.mTouchEdgeInspector.isCorner()) {
                                    if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                                        MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setBackgroundColor(0);
                                    }
                                    MultiPhoneWindow.this.forceHideInputMethod();
                                    MultiPhoneWindow.this.getDecorView().performHapticFeedback(0);
                                    MultiPhoneWindow.this.mTmpBound.set(MultiPhoneWindow.this.getStackBound());
                                    this.mGuideViewBound.set(MultiPhoneWindow.this.mTmpBound);
                                    MultiPhoneWindow.this.showGuide(MultiPhoneWindow.this.mTmpBound, 0);
                                    this.mIsResize = true;
                                    ev.setAction(3);
                                    break;
                                }
                            }
                        }
                        if (MultiPhoneWindow.this.mIsSupportDiagonalResizable || style.isEnabled(32768) || this.mTouchEdgeInspector.isCorner()) {
                            fixedRatio = true;
                        }
                        if (MultiPhoneWindow.this.mIsPenButtonPressed) {
                            return true;
                        }
                        resizePenWindow(fixedRatio, ev, style);
                        return true;
                        break;
                }
                return super.dispatchTouchEvent(ev);
            } else {
                this.mTouchEdgeInspector.clear();
                return super.dispatchTouchEvent(ev);
            }
        }

        private void initResizePenWindow() {
            Point size = new Point();
            MultiPhoneWindow.this.getDisplaySize(size, true);
            this.mMinWidth = 0;
            this.mMinHeight = 0;
            this.mScreenWidth = size.x;
            this.mScreenHeight = size.y;
            this.mMaxWidth = (int) (((float) size.x) * MultiPhoneWindow.this.mMaxSizeRatio);
            this.mMaxHeight = (int) (((float) size.y) * MultiPhoneWindow.this.mMaxSizeRatio);
            if (MultiPhoneWindow.this.mActivity != null) {
                int orientation = MultiPhoneWindow.this.mActivity.getResources().getConfiguration().orientation;
                Rect frame = new Rect();
                if (orientation == 1) {
                    frame.set(MultiPhoneWindow.this.mMinStackBoundForPort);
                } else {
                    frame.set(MultiPhoneWindow.this.mMinStackBoundForLand);
                }
                boolean changed = MultiPhoneWindow.this.checkMaxStackSize(frame, orientation);
                this.mMinWidth = frame.width();
                this.mMinHeight = frame.height();
                if (changed) {
                    this.mMaxWidth = this.mMinWidth;
                    this.mMaxHeight = this.mMinHeight;
                }
            }
        }

        private boolean isExceededLimitTop(Rect guideViewBounds) {
            if (guideViewBounds != null) {
                int limitTop = -1;
                if (MultiPhoneWindow.this.mPenWindowController != null && MultiPhoneWindow.this.mPenWindowController.getControllerHeight() > 0) {
                    limitTop = MultiPhoneWindow.this.mStatusBarHeight + (MultiPhoneWindow.this.mPenWindowController.getControllerHeight() / 2);
                }
                if (limitTop > 0 && limitTop > this.mLastMoveY && guideViewBounds.top == limitTop) {
                    return true;
                }
            }
            return false;
        }

        private void resizePenWindow(boolean isFixedRatio, MotionEvent ev, MultiWindowStyle style) {
            if (style.isEnabled(65536)) {
                MultiPhoneWindow.this.showGuide(this.mGuideViewBound, 1);
                return;
            }
            Rect access$200;
            if (!style.isEnabled(2048)) {
                this.mMinWidth = 1;
                this.mMinHeight = 1;
            }
            boolean resizable = false;
            if (!isFixedRatio) {
                resizable = true;
            } else if (this.mGuideViewBound.width() >= this.mMinWidth && this.mGuideViewBound.height() >= this.mMinHeight) {
                resizable = true;
            }
            int dx;
            if (this.mTouchEdgeInspector.isEdge(4)) {
                dx = ((int) ev.getRawXForScaledWindow()) - this.mGuideViewBound.left;
                if (MultiPhoneWindow.this.mTmpBound.right > MultiPhoneWindow.this.mTmpBound.left + dx && (resizable || (isFixedRatio && dx < 0))) {
                    access$200 = MultiPhoneWindow.this.mTmpBound;
                    access$200.left += dx;
                    resizable = true;
                }
            } else if (this.mTouchEdgeInspector.isEdge(8)) {
                dx = ((int) ev.getRawXForScaledWindow()) - this.mGuideViewBound.right;
                if (MultiPhoneWindow.this.mTmpBound.right > MultiPhoneWindow.this.mTmpBound.left + dx && (resizable || (isFixedRatio && dx > 0))) {
                    access$200 = MultiPhoneWindow.this.mTmpBound;
                    access$200.right += dx;
                    if (MultiPhoneWindow.this.mTmpBound.width() <= this.mMinWidth) {
                        MultiPhoneWindow.this.mTmpBound.right = MultiPhoneWindow.this.mTmpBound.left + this.mMinWidth;
                    }
                    resizable = true;
                }
            }
            if (this.mTouchEdgeInspector.isEdge(2)) {
                int dy = (int) (ev.getRawYForScaledWindow() - ((float) this.mGuideViewBound.bottom));
                if (MultiPhoneWindow.this.mTmpBound.bottom > MultiPhoneWindow.this.mTmpBound.top + dy && (resizable || (isFixedRatio && dy > 0))) {
                    access$200 = MultiPhoneWindow.this.mTmpBound;
                    access$200.bottom += dy;
                    if (MultiPhoneWindow.this.mTmpBound.height() <= this.mMinHeight) {
                        MultiPhoneWindow.this.mTmpBound.bottom = MultiPhoneWindow.this.mTmpBound.top + this.mMinHeight;
                    }
                    resizable = true;
                }
            }
            adjustPenWindowSize(MultiPhoneWindow.this.mTmpBound, 0.0f);
            if (isFixedRatio && resizable) {
                if (this.mFixedRatio == 0.0f) {
                    this.mFixedRatio = ((float) this.mGuideViewBound.width()) / ((float) this.mGuideViewBound.height());
                }
                int width = MultiPhoneWindow.this.mTmpBound.width();
                int height = MultiPhoneWindow.this.mTmpBound.height();
                if (this.mTouchEdgeInspector.isEdge(4) || this.mTouchEdgeInspector.isEdge(8)) {
                    height = Math.round(((float) MultiPhoneWindow.this.mTmpBound.width()) / this.mFixedRatio);
                    if (height < this.mMinHeight) {
                        height = this.mMinHeight;
                    }
                    if (this.mTouchEdgeInspector.isEdge(1)) {
                        MultiPhoneWindow.this.mTmpBound.top = MultiPhoneWindow.this.mTmpBound.bottom - height;
                    } else {
                        MultiPhoneWindow.this.mTmpBound.bottom = MultiPhoneWindow.this.mTmpBound.top + height;
                    }
                } else if (this.mTouchEdgeInspector.isEdge(1)) {
                    MultiPhoneWindow.this.mTmpBound.left = MultiPhoneWindow.this.mTmpBound.right - ((int) (((float) height) * this.mFixedRatio));
                } else {
                    MultiPhoneWindow.this.mTmpBound.right = MultiPhoneWindow.this.mTmpBound.left + ((int) (((float) height) * this.mFixedRatio));
                    if (MultiPhoneWindow.this.mTmpBound.width() <= this.mMinWidth) {
                        MultiPhoneWindow.this.mTmpBound.right = MultiPhoneWindow.this.mTmpBound.left + this.mMinWidth;
                    }
                }
                adjustPenWindowSize(MultiPhoneWindow.this.mTmpBound, this.mFixedRatio);
            }
            this.mLastMoveX = (int) ev.getRawXForScaledWindow();
            this.mLastMoveY = (int) ev.getRawYForScaledWindow();
            this.mTouchEdgeInspector.check(this.mLastMoveX, this.mLastMoveY);
            this.mGuideViewBound.set(MultiPhoneWindow.this.mTmpBound);
            if (isFixedRatio && (this.mGuideViewBound.width() == MultiPhoneWindow.this.getStackBound().width() || this.mGuideViewBound.height() == MultiPhoneWindow.this.getStackBound().height())) {
                if (this.mGuideViewBound.width() == MultiPhoneWindow.this.getStackBound().width()) {
                    this.mGuideViewBound.bottom = MultiPhoneWindow.this.getStackBound().bottom;
                } else {
                    this.mGuideViewBound.right = MultiPhoneWindow.this.getStackBound().right;
                }
            }
            if (isExceededLimitTop(this.mGuideViewBound)) {
                MultiPhoneWindow.this.showGuide(new Rect(0, 0, this.mScreenWidth, this.mScreenHeight), 2);
            } else if (this.mGuideViewBound.width() <= this.mMinWidth && this.mGuideViewBound.height() <= this.mMinHeight) {
                MultiPhoneWindow.this.showGuide(this.mGuideViewBound, 1);
            } else if (isFixedRatio && (this.mGuideViewBound.width() == this.mMinWidth || this.mGuideViewBound.height() == this.mMinHeight)) {
                MultiPhoneWindow.this.showGuide(this.mGuideViewBound, 1);
            } else if (this.mGuideViewBound.width() > this.mMaxWidth || this.mGuideViewBound.height() > this.mMaxHeight) {
                MultiPhoneWindow.this.showGuide(new Rect(0, 0, this.mScreenWidth, this.mScreenHeight), 2);
            } else {
                MultiPhoneWindow.this.showGuide(this.mGuideViewBound, 0);
            }
            this.mIsResize = true;
        }

        private void adjustPenWindowSize(Rect curBound, float ratio) {
            int what = -1;
            boolean lastStackBoundIsMax = true;
            if (!MultiPhoneWindow.this.mIsSupportSimplificationUI && (MultiPhoneWindow.this.getStackBound().width() == this.mMaxWidth || MultiPhoneWindow.this.getStackBound().height() == this.mMaxHeight)) {
                lastStackBoundIsMax = false;
            }
            if (curBound.width() < this.mMinWidth) {
                if (this.mTouchEdgeInspector.isEdge(4)) {
                    curBound.left = curBound.right - this.mMinWidth;
                } else if (this.mTouchEdgeInspector.isEdge(8)) {
                    curBound.right = curBound.left + this.mMinWidth;
                }
                what = 0;
            } else if (!(curBound.width() <= this.mMaxWidth || MultiPhoneWindow.this.mMaxSizeRatio == 1.0f || lastStackBoundIsMax)) {
                if (this.mTouchEdgeInspector.isEdge(4)) {
                    curBound.left = curBound.right - this.mMaxWidth;
                } else if (this.mTouchEdgeInspector.isEdge(8)) {
                    curBound.right = curBound.left + this.mMaxWidth;
                }
                what = 1;
            }
            if (curBound.height() < this.mMinHeight) {
                if (this.mTouchEdgeInspector.isEdge(1)) {
                    curBound.top = curBound.bottom - this.mMinHeight;
                } else if (this.mTouchEdgeInspector.isEdge(2)) {
                    curBound.bottom = curBound.top + this.mMinHeight;
                }
                what = 2;
            } else if (!(curBound.height() <= this.mMaxHeight || MultiPhoneWindow.this.mMaxSizeRatio == 1.0f || lastStackBoundIsMax)) {
                if (this.mTouchEdgeInspector.isEdge(1)) {
                    curBound.top = curBound.bottom - this.mMaxHeight;
                } else if (this.mTouchEdgeInspector.isEdge(2)) {
                    curBound.bottom = curBound.top + this.mMaxHeight;
                }
                what = 3;
            }
            if (ratio != 0.0f) {
                switch (what) {
                    case 0:
                    case 1:
                        if (!this.mTouchEdgeInspector.isEdge(1)) {
                            curBound.bottom = curBound.top + Math.round(((float) curBound.width()) / ratio);
                            break;
                        } else {
                            curBound.top = curBound.bottom - Math.round(((float) curBound.width()) / ratio);
                            break;
                        }
                    case 2:
                    case 3:
                        if (!this.mTouchEdgeInspector.isEdge(4)) {
                            curBound.right = curBound.left + ((int) (((float) curBound.height()) * ratio));
                            break;
                        } else {
                            curBound.left = curBound.right - ((int) (((float) curBound.height()) * ratio));
                            break;
                        }
                }
                if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                    int margin = MultiPhoneWindow.this.mPenWindowController.getControllerHeight() / 2;
                    if (curBound.top - margin < MultiPhoneWindow.this.mStatusBarHeight) {
                        curBound.top = MultiPhoneWindow.this.mStatusBarHeight + margin;
                        if (this.mTouchEdgeInspector.isEdge(4)) {
                            curBound.left = curBound.right - ((int) (((float) curBound.height()) * ratio));
                        } else {
                            curBound.right = curBound.left + ((int) (((float) curBound.height()) * ratio));
                        }
                    }
                }
            }
        }

        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if (MultiPhoneWindow.DEBUG && hasWindowFocus) {
                Log.d(MultiPhoneWindow.TAG, "onWindowFocusChanged: " + getMultiWindowStyle().getType() + " type (" + MultiWindowStyle.zoneToString(getMultiWindowStyle().getZone()) + ")");
            }
            if (MultiPhoneWindow.this.mContentLayoutGenerated && !hasWindowFocus) {
                MultiPhoneWindow.this.mPenWindowController.performUpdateMenuVisibility(false);
            }
        }

        protected void onWindowVisibilityChanged(int visibility) {
            super.onWindowVisibilityChanged(visibility);
            if (visibility == 8 && MultiPhoneWindow.this.mVideoCapabilityDialog != null && MultiPhoneWindow.this.mVideoCapabilityDialog.isShowing()) {
                MultiPhoneWindow.this.mVideoCapabilityDialog.dismiss();
                MultiPhoneWindow.this.mVideoCapabilityDialog = null;
            }
        }

        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            resizeKnoxBadge(canvas);
            drawBorder(canvas);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (!MultiPhoneWindow.this.mContentLayoutGenerated) {
                return;
            }
            if (MultiPhoneWindow.this.getState() == 4 || MultiPhoneWindow.this.getState() == 3 || MultiPhoneWindow.this.getState() == 2) {
                MultiPhoneWindow.this.mPenWindowController.performUpdateVisibility(true);
            }
        }

        private void drawBorder(Canvas canvas) {
            LayoutParams attrs;
            if (MultiPhoneWindow.this.mIsBorderShown) {
                attrs = MultiPhoneWindow.this.getAttributes();
                if (!getMultiWindowStyle().isEnabled(4) && attrs.width == -1 && attrs.height == -1) {
                    MultiPhoneWindow.this.mBorder.drawFocusBorder(canvas, getMeasuredWidth(), getMeasuredHeight());
                } else {
                    MultiPhoneWindow.this.mIsBorderShown = false;
                }
            } else if (!MultiPhoneWindow.this.mHasStackFocus && getMultiWindowStyle().isSplit()) {
                attrs = MultiPhoneWindow.this.getAttributes();
                if (attrs.width != -2 || attrs.height != -2) {
                    int right = getMeasuredWidth();
                    int bottom = getMeasuredHeight();
                    if (!(MultiPhoneWindow.this.getStackBound().width() == right && MultiPhoneWindow.this.getStackBound().height() == bottom)) {
                        Rect stackBound = MultiPhoneWindow.this.mMultiWindowFacade.getStackBound(MultiPhoneWindow.this.mToken);
                        if (stackBound == null || stackBound.width() < right || stackBound.height() < bottom || stackBound.width() - right > MultiPhoneWindow.this.mThickness / 2 || stackBound.height() - bottom > MultiPhoneWindow.this.mThickness / 2) {
                            return;
                        }
                    }
                    MultiPhoneWindow.this.mBorder.drawUnfocusBorder(canvas, right, bottom, getMultiWindowStyle().getZone());
                }
            }
        }

        private void resizeKnoxBadge(Canvas canvas) {
            if (getContext().getUserId() < 100 || !PersonaManager.isKnoxMultiwindowsSupported()) {
                return;
            }
            if (getMultiWindowStyle().getType() == 2) {
                if (MultiPhoneWindow.this.badgeIcon == null) {
                    MultiPhoneWindow.this.badgeIcon = super.getUserBadgeIcon(getContext().getUserId());
                }
                if (getMultiWindowStyle() == null || getMultiWindowStyle().getBounds() == null) {
                    super.drawKnoxBadge(canvas);
                    return;
                }
                Rect bounds = getMultiWindowStyle().getBounds();
                float resizedWidth = ((float) MultiPhoneWindow.this.badgeIcon.getIntrinsicWidth()) * (((float) getMeasuredWidth()) / ((float) (bounds.right - bounds.left)));
                MultiPhoneWindow.this.badgeIcon.setBounds(getMeasuredWidth() - ((int) (((double) resizedWidth) + 0.5d)), getMeasuredHeight() - ((int) (((double) (((float) MultiPhoneWindow.this.badgeIcon.getIntrinsicHeight()) * (((float) getMeasuredHeight()) / ((float) (bounds.bottom - bounds.top))))) + 0.5d)), getMeasuredWidth(), getMeasuredHeight());
                MultiPhoneWindow.this.badgeIcon.draw(canvas);
            } else if (getMultiWindowStyle().getType() == 1) {
                if (MultiPhoneWindow.this.badgeIcon == null) {
                    MultiPhoneWindow.this.badgeIcon = super.getUserBadgeIcon(getContext().getUserId());
                }
                super.drawKnoxBadge(canvas);
            }
        }

        protected boolean dispatchHoverEvent(MotionEvent event) {
            MultiWindowStyle style = getMultiWindowStyle();
            boolean isMouse = event.getToolType(0) == 3;
            if (!style.isCascade() || style.isEnabled(4)) {
                if (isMouse && MultiPhoneWindow.this.mLastHoverIcon != 101) {
                    try {
                        MultiPhoneWindow.this.mLastHoverIcon = 101;
                        PointerIcon.setMouseIcon(MultiPhoneWindow.this.mLastHoverIcon, -1);
                    } catch (RemoteException e) {
                        Log.e(MultiPhoneWindow.TAG, "Failed to change Pen Point to MOUSEICON_DEFAULT");
                    }
                }
                return super.dispatchHoverEvent(event);
            } else if (event.getY() > 0.0f && event.getY() < ((float) MultiPhoneWindow.this.mTitleBarHeight)) {
                return super.dispatchHoverEvent(event);
            } else {
                int hoverIcon = -1;
                boolean isFixedSize = style.isEnabled(65536);
                if (event.getAction() == 7 || event.getAction() == 9) {
                    boolean isEdge;
                    boolean isCorner;
                    this.mTouchEdgeInspector.clear();
                    this.mTouchEdgeInspector.set(MultiPhoneWindow.this.getStackBound(), MultiPhoneWindow.this.mResizablePadding);
                    if (event.getAction() == 9) {
                        this.mTouchEdgeInspector.check((int) event.getRawXForScaledWindow(), (int) event.getRawYForScaledWindow());
                        isEdge = this.mTouchEdgeInspector.isEdge();
                        isCorner = this.mTouchEdgeInspector.isCorner();
                    } else {
                        this.mTouchEdgeInspector.check((int) event.getRawXForScaledWindow(), (int) event.getRawYForScaledWindow());
                        isEdge = this.mTouchEdgeInspector.isEdge();
                        isCorner = this.mTouchEdgeInspector.isCorner();
                    }
                    if (!isFixedSize && this.mLastHoverEdge != isEdge && !isCorner) {
                        this.mConsumeHoverEvent = true;
                        if (MultiPhoneWindow.this.mIsSupportDiagonalResizable) {
                            if (isMouse || (MultiPhoneWindow.this.mLastHoverIcon >= 6 && MultiPhoneWindow.this.mLastHoverIcon <= 9)) {
                                hoverIcon = isMouse ? 101 : 1;
                            }
                            this.mConsumeHoverEvent = false;
                        } else if (this.mTouchEdgeInspector.isEdge(2)) {
                            hoverIcon = isMouse ? 107 : 7;
                        } else if (this.mTouchEdgeInspector.isEdge(12)) {
                            hoverIcon = isMouse ? 106 : 6;
                        } else {
                            hoverIcon = isMouse ? 101 : 1;
                            this.mConsumeHoverEvent = false;
                        }
                        this.mLastHoverEdge = isEdge;
                        this.mLastCorner = false;
                    } else if (!(isFixedSize || this.mLastCorner == isCorner)) {
                        this.mConsumeHoverEvent = true;
                        if (!this.mTouchEdgeInspector.isEdge(2) || MultiPhoneWindow.this.isInputMethodShown()) {
                            if (!MultiPhoneWindow.this.mIsSupportDiagonalResizable || !this.mTouchEdgeInspector.isEdge(1) || MultiPhoneWindow.this.isInputMethodShown()) {
                                hoverIcon = isMouse ? 101 : 1;
                                this.mConsumeHoverEvent = false;
                            } else if (this.mTouchEdgeInspector.isEdge(4)) {
                                hoverIcon = isMouse ? 108 : 8;
                            } else if (this.mTouchEdgeInspector.isEdge(8)) {
                                hoverIcon = isMouse ? 109 : 9;
                            }
                        } else if (this.mTouchEdgeInspector.isEdge(4)) {
                            hoverIcon = isMouse ? 109 : 9;
                        } else if (this.mTouchEdgeInspector.isEdge(8)) {
                            hoverIcon = isMouse ? 108 : 8;
                        }
                        this.mLastCorner = isCorner;
                        this.mLastHoverEdge = false;
                    }
                } else if (event.getAction() == 10) {
                    hoverIcon = isMouse ? 101 : 1;
                    this.mConsumeHoverEvent = false;
                    this.mLastHoverEdge = false;
                    this.mLastCorner = false;
                }
                if (hoverIcon > 0) {
                    try {
                        MultiPhoneWindow.this.mLastHoverIcon = hoverIcon;
                        if (isMouse) {
                            PointerIcon.setMouseIcon(hoverIcon, -1);
                        } else {
                            PointerIcon.setHoveringSpenIcon(hoverIcon, -1);
                        }
                    } catch (RemoteException e2) {
                        Log.e(MultiPhoneWindow.TAG, "Failed to change Pen Point to HOVERING / " + isMouse);
                    }
                }
                if (this.mConsumeHoverEvent) {
                    return true;
                }
                return super.dispatchHoverEvent(event);
            }
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (MultiPhoneWindow.DEBUG) {
                Log.d(MultiPhoneWindow.TAG, "onAttachedToWindow");
            }
            hackTurnOffWindowResizeAnim(true);
            MultiPhoneWindow.this.mVideoCapabilityReceiver.register();
            IntentFilter dragModeReceiver = new IntentFilter();
            dragModeReceiver.addAction("com.sec.android.action.NOTIFY_STOP_DRAG_MODE");
            dragModeReceiver.addAction("com.sec.android.action.NOTIFY_START_DRAG_MODE");
            MultiPhoneWindow multiPhoneWindow = MultiPhoneWindow.this;
            boolean z = getLayoutParams().width == -1 && getLayoutParams().height == -1;
            multiPhoneWindow.mIsFullScreen = z;
            MultiPhoneWindow.this.refreshUI(-1);
            if (getMultiWindowStyle().isCascade()) {
                updateMultiPhoneWindowLayout();
            }
            if (MultiPhoneWindow.this.mDragModeReceiver == null) {
                MultiPhoneWindow.this.mDragModeReceiver = new DragModeBroadcastReceiver();
                this.mContext.registerReceiver(MultiPhoneWindow.this.mDragModeReceiver, dragModeReceiver);
            }
            MultiPhoneWindow.this.mIsAttachedToWindow = true;
        }

        public void setBackgroundColor(int color) {
            if (getBackground() != MultiPhoneWindow.this.mTrasnparentColor) {
                super.setBackgroundColor(color);
            }
        }

        public void onDetachedFromWindow() {
            MultiPhoneWindow.this.mIsAttachedToWindow = false;
            if (MultiPhoneWindow.DEBUG) {
                Log.d(MultiPhoneWindow.TAG, "onDetachedFromWindow, mActivity=" + MultiPhoneWindow.this.mActivity);
            }
            MultiPhoneWindow.this.mVideoCapabilityReceiver.unregister();
            MultiPhoneWindow.this.dismissGuide();
            if (MultiPhoneWindow.this.mContentLayoutGenerated) {
                MultiPhoneWindow.this.mPenWindowController.performUpdateMenuVisibility(false);
                MultiPhoneWindow.this.mPenWindowController.performUpdateVisibility(false);
                MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setBackground(null);
            }
            if (MultiPhoneWindow.this.mSoundPool != null) {
                MultiPhoneWindow.this.mSoundPool.release();
                MultiPhoneWindow.this.mSoundPool = null;
            }
            if (MultiPhoneWindow.this.mDragModeReceiver != null) {
                this.mContext.unregisterReceiver(MultiPhoneWindow.this.mDragModeReceiver);
                MultiPhoneWindow.this.mDragModeReceiver = null;
            }
            if (MultiPhoneWindow.this.mMinimizeAnimator != null) {
                MultiPhoneWindow.this.mMinimizeAnimator.removeWindow(true);
            }
        }

        protected void updateColorViewInt(ColorViewState state, int sysUiVis, int color, int size, boolean verticalBar, int rightMargin, boolean animate) {
            if (getMultiWindowStyle().isCascade()) {
                updateMultiPhoneWindowLayout();
                return;
            }
            if (getMultiWindowStyle().isSplit()) {
                ViewRootImpl viewRootImpl = getViewRootImpl();
                if (!(state.view == null || viewRootImpl == null || !viewRootImpl.getAppVisibility())) {
                    state.view.setVisibility(4);
                    state.targetVisibility = 4;
                }
            }
            if (getMultiWindowStyle().isNormal() && state.view != null && state.view.getVisibility() == 8) {
                state.view.setVisibility(state.targetVisibility);
            }
            super.updateColorViewInt(state, sysUiVis, color, size, false, rightMargin, animate);
        }

        protected void updateMultiPhoneWindowLayout() {
            if (!(this.mStatusColorViewState == null || this.mStatusColorViewState.view == null || this.mStatusColorViewState.view.getVisibility() == 8)) {
                this.mStatusColorViewState.view.setVisibility(8);
            }
            if (!(this.mNavigationColorViewState == null || this.mNavigationColorViewState.view == null)) {
                removeView(this.mNavigationColorViewState.view);
                this.mNavigationColorViewState.view = null;
            }
            if (this.mStatusGuard != null) {
                removeView(this.mStatusGuard);
                this.mStatusGuard = null;
            }
            if (this.mNavigationGuard != null) {
                removeView(this.mNavigationGuard);
                this.mNavigationGuard = null;
            }
        }

        protected void dispatchMultiWindowStateChanged(int state) {
            super.dispatchMultiWindowStateChanged(state);
            if (MultiPhoneWindow.this.mMultiWindowListener2 != null) {
                if (MultiPhoneWindow.this.getAttributes() != null && MultiPhoneWindow.DEBUG) {
                    Log.i(MultiPhoneWindow.TAG, "dispatchMultiWindowStateChanged state=" + state + ", title= " + MultiPhoneWindow.this.getAttributes().getTitle());
                }
                MultiPhoneWindow.this.mMultiWindowListener2.onStateChanged(state);
            }
        }
    }

    class VideoCapabilityReceiver extends BroadcastReceiver {
        VideoCapabilityReceiver() {
        }

        public void register() {
            MultiPhoneWindow.this.mContext.registerReceiver(this, new IntentFilter("android.intent.action.VIDEOCAPABILITY"));
        }

        public void unregister() {
            MultiPhoneWindow.this.mContext.unregisterReceiver(this);
        }

        public void onReceive(Context context, Intent intent) {
            DecorView decorView = (DecorView) MultiPhoneWindow.this.getDecorView();
            if (decorView != null && decorView.getVisibility() == 0 && MultiPhoneWindow.this.mVideoCapabilityDialog == null) {
                int requestedPid = -1;
                try {
                    requestedPid = Integer.valueOf(intent.getType()).intValue();
                } catch (NumberFormatException e) {
                    Log.w(MultiPhoneWindow.TAG, "attached item in getType() is not an int type");
                }
                if (requestedPid > -1) {
                    RunningAppProcessInfo pinfo = new RunningAppProcessInfo();
                    ActivityManager.getMyMemoryState(pinfo);
                    if (pinfo.pid == requestedPid) {
                        Builder videoCapabilityAlert = new Builder(MultiPhoneWindow.this.mContext);
                        videoCapabilityAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MultiPhoneWindow.this.mVideoCapabilityDialog = null;
                            }
                        });
                        videoCapabilityAlert.setMessage(R.string.SS_STOP_THE_OTHER_VIDEO_AND_TRY_AGAIN);
                        MultiPhoneWindow.this.mVideoCapabilityDialog = videoCapabilityAlert.create();
                        MultiPhoneWindow.this.mVideoCapabilityDialog.show();
                    }
                }
            }
        }
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DEBUG = z;
    }

    public MultiPhoneWindow(Context context) {
        super(context);
        this.mContext = context;
        this.userId = context.getUserId();
        this.mMultiWindowFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
        this.mPowerManager = (PowerManager) this.mContext.getSystemService(SMProviderContract.KEY_POWER);
        this.mIsSupportDiagonalResizable = MultiWindowFeatures.isSupportStyleTransition(this.mContext);
        this.mIsSupportMinimizeAnimation = MultiWindowFeatures.isSupportMinimizeAnimation(this.mContext);
        this.mIsSupportSimplificationUI = MultiWindowFeatures.isSupportSimplificationUI(this.mContext);
        this.mTargetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion;
        if (this.mContext instanceof Activity) {
            this.mActivity = (Activity) this.mContext;
            this.mToken = this.mActivity.getActivityToken();
            updateMultiWindowStyle(this.mActivity.getMultiWindowStyle());
            this.mDssScale = this.mContext.getApplicationInfo().dssScale;
            this.mDocking = new Docking(this.mActivity, this.mMultiWindowFacade, this.mDssScale);
            this.mDocking.setOnDockingListener(new OnDockingListener() {
                public void onCancel() {
                    if (MultiPhoneWindow.this.getState() == 4 || MultiPhoneWindow.this.getState() == 1) {
                        MultiPhoneWindow.this.dismissGuide();
                        return;
                    }
                    if (MultiPhoneWindow.DEBUG) {
                        Log.d(MultiPhoneWindow.TAG, "mDocking.onCancel() : mDismissGuideByDockingCanceled=" + MultiPhoneWindow.this.mDismissGuideByDockingCanceled);
                    }
                    if (MultiPhoneWindow.this.mDismissGuideByDockingCanceled) {
                        MultiPhoneWindow.this.dismissGuide();
                    } else {
                        MultiPhoneWindow.this.showGuide(MultiPhoneWindow.this.mTmpBound, 3);
                    }
                }
            });
        }
        updateRestricedStackBounds();
        initResource();
    }

    private void updateRestricedStackBounds() {
        boolean toggle;
        Point displaySize = new Point();
        getDisplaySize(displaySize);
        int configOrientation = this.mContext.getResources().getConfiguration().orientation;
        if ((configOrientation != 1 || displaySize.x <= displaySize.y) && (configOrientation != 2 || displaySize.y <= displaySize.x)) {
            toggle = false;
        } else {
            toggle = true;
        }
        if (toggle) {
            int temp = displaySize.x;
            displaySize.x = displaySize.y;
            displaySize.y = temp;
        }
        int floatingMinimumSizeRatioPercentage = this.mContext.getResources().getInteger(R.integer.multiwindow_floating_minimum_size_ratio);
        int floatingMinimumSizeSelectiveRatioPercentage = this.mContext.getResources().getInteger(R.integer.multiwindow_selectiveScaleFactor);
        Rect minSize = new Rect(0, 0, (int) (((float) (displaySize.x * floatingMinimumSizeRatioPercentage)) / 1000.0f), (int) (((float) (displaySize.y * floatingMinimumSizeRatioPercentage)) / 1000.0f));
        Rect maxSize = new Rect(0, 0, (int) (((float) (displaySize.x * floatingMinimumSizeSelectiveRatioPercentage)) / 1000.0f), (int) (((float) (displaySize.y * floatingMinimumSizeSelectiveRatioPercentage)) / 1000.0f));
        if (configOrientation == 1) {
            this.mMinStackBoundForPort.set(minSize);
            this.mMinStackBoundForLand.set(minSize.left, minSize.top, minSize.left + minSize.height(), minSize.top + minSize.width());
            this.mMaxPortStackBoundForSelectiveOrientation.set(maxSize);
            this.mMaxLandStackBoundForSelectiveOrientation.set(maxSize.left, maxSize.top, maxSize.left + maxSize.height(), maxSize.top + maxSize.width());
        } else {
            this.mMinStackBoundForPort.set(minSize.left, minSize.top, minSize.left + minSize.height(), minSize.top + minSize.width());
            this.mMinStackBoundForLand.set(minSize);
            this.mMaxPortStackBoundForSelectiveOrientation.set(maxSize.left, maxSize.top, maxSize.left + maxSize.height(), maxSize.top + maxSize.width());
            this.mMaxLandStackBoundForSelectiveOrientation.set(maxSize);
        }
        if (DEBUG) {
            Log.d(TAG, "updateRestricedStackBounds, r=" + this.mActivity + ", mMinPort=" + this.mMinStackBoundForPort + ", mMinLand=" + this.mMinStackBoundForLand + ", mMaxPortSelective=" + this.mMaxPortStackBoundForSelectiveOrientation + ", mMaxLandSelective=" + this.mMaxLandStackBoundForSelectiveOrientation);
        }
    }

    protected DecorView generateDecor() {
        return new MultiPhoneDecorView(this.mContext, -1);
    }

    protected ViewGroup generateLayout(DecorView decor) {
        ViewGroup ret = super.generateLayout(decor);
        if (getStackBound().isEmpty()) {
            Rect bounds = this.mMultiWindowFacade.getStackOriginalBound(this.mToken);
            if (bounds != null) {
                setStackBoundsInternel(bounds);
                if (DEBUG) {
                    Log.i(TAG, "generateLayout(), local setBounds=" + bounds + ", mActivity=" + this.mActivity);
                }
            } else if (DEBUG) {
                Log.w(TAG, "generateLayout(), bounds is null, mActivity=" + this.mActivity);
            }
        }
        this.mInitialFlag = getAttributes().flags;
        this.mFloatingFlag = -1;
        this.mLastRotated = isRotated(getWindowManager().getDefaultDisplay().getRotation());
        this.mLastOrientation = this.mActivity.getResources().getConfiguration().orientation;
        if (this.mBorder == null) {
            this.mBorder = new Border(this.mContext, getWindowManager());
        }
        generatePenWindowLayout();
        if (this.mGuideView == null) {
            this.mGuideView = new GuideView(getDecorView(), LayoutParams.TYPE_MULTI_WINDOW_GUIDE_VIEW);
        }
        return ret;
    }

    private void initResource() {
        this.mTitleBarHeight = (int) this.mContext.getResources().getDimension(R.dimen.multiwindow_titlebar_height);
        this.mResizablePadding.left = ((int) this.mContext.getResources().getDimension(R.dimen.multiwindow_floating_resize_area)) / 3;
        if (this.mIsSupportDiagonalResizable) {
            this.mResizablePadding.left = (int) this.mContext.getResources().getDimension(R.dimen.multiwindow_floating_resize_area);
            this.mResizablePadding.top = this.mResizablePadding.left;
            this.mMaxSizeRatio = ((float) this.mContext.getResources().getInteger(R.integer.multiwindow_floating_maximum_size_ratio)) / 1000.0f;
        }
        this.mResizablePadding.right = this.mResizablePadding.left;
        this.mResizablePadding.bottom = this.mResizablePadding.left;
        this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        this.mThickness = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_focusline_thickness);
        if (MultiWindowFeatures.isSupportCenterbarClickSound(this.mContext)) {
            this.mSoundPool = new SoundPool(1, 1, 0);
            this.mHeaderButtonSoundId = this.mSoundPool.load(this.mContext, R.raw.multi_window_floating_buttons, 1);
            return;
        }
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
    }

    private void generatePenWindowLayout() {
        if (this.mIsSupportMinimizeAnimation) {
            this.mMinimizeAnimator = new MinimizeAnimator(this.mContext, this, this.mDssScale);
        }
        if (MultiWindowFeatures.isSupportFreeStyle(this.mContext)) {
            this.mContentRootContainer = (ViewGroup) LayoutInflater.from(this.mContext).inflate((int) R.layout.mw_window_content_frame, null);
            this.mPenWindowController = new HeaderWindowController();
            this.mPenWindowController.generateLayout();
            DecorView decor = (DecorView) getDecorView();
            ((MultiPhoneDecorView) decor).updateMultiPhoneWindowLayout();
            for (int i = 0; i < decor.getChildCount(); i++) {
                View content = decor.getChildAt(i);
                decor.removeView(content);
                this.mContentRootContainer.addView(content);
            }
            decor.addView(this.mContentRootContainer);
            this.mContentLayoutGenerated = true;
        }
    }

    public View getContentRootContainer() {
        return this.mContentRootContainer;
    }

    public void handlePause() {
        if (this.mPenWindowController != null && this.mPenWindowController.mMenuContainer != null) {
            boolean isScreenOn = true;
            if (getDecorView() != null) {
                Display display = getDecorView().getDisplay();
                if (display != null) {
                    isScreenOn = display.getState() == 2;
                }
            }
            if (this.mMultiWindowStyle.getType() == 2 && isScreenOn) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (((FloatingMenuView) MultiPhoneWindow.this.mPenWindowController.mMenuContainer).mIsAttached) {
                            MultiPhoneWindow.this.mPenWindowController.mPenWindowHeader.setVisibility(0);
                            ((FloatingMenuView) MultiPhoneWindow.this.mPenWindowController.mMenuContainer).dismiss(false);
                        }
                    }
                }, 400);
            }
        }
    }

    private void restoreFocusedView() {
        if (this.mFocusedViewId != -1) {
            View needsFocus = getDecorView().findViewById(this.mFocusedViewId);
            if (needsFocus != null) {
                needsFocus.requestFocus();
            }
            this.mFocusedViewId = -1;
        }
    }

    private void saveFocusedView() {
        View focusedView = getDecorView().findFocus();
        if (focusedView != null) {
            this.mFocusedViewId = focusedView.getId();
        }
    }

    private void initFocusedView() {
        this.mFocusedViewId = -1;
    }

    private boolean forceHideInputMethod() {
        boolean hardKeyShown = true;
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm == null) {
            return false;
        }
        boolean haveHardKeyboard;
        boolean res = imm.forceHideSoftInput();
        Configuration conf = this.mContext.getResources().getConfiguration();
        if (conf.keyboard != 1) {
            haveHardKeyboard = true;
        } else {
            haveHardKeyboard = false;
        }
        if (!haveHardKeyboard || conf.hardKeyboardHidden == 2) {
            hardKeyShown = false;
        }
        if (hardKeyShown) {
            return false;
        }
        return res;
    }

    private boolean isInputMethodShown() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            return imm.isInputMethodShown();
        }
        return false;
    }

    protected boolean isFloatingWindow() {
        if (getAttributes().height == -2 && getAttributes().width == -2) {
            return true;
        }
        return false;
    }

    private boolean isRotated(int rotation) {
        switch (rotation) {
            case 0:
            case 2:
                return false;
            default:
                return true;
        }
    }

    public void onVisibilityChanged(boolean show) {
        if (this.mPenWindowController != null && this.mPenWindowController.getWindowToken() != null) {
            switch (getState()) {
                case 2:
                case 3:
                    this.mPenWindowController.performUpdateVisibility(show);
                    if (!show) {
                        dismissGuide();
                        return;
                    }
                    return;
                case 4:
                    if (show && getDecorView().getVisibility() == 0) {
                        getDecorView().setVisibility(4);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    protected void dispatchWindowAttributesChanged(LayoutParams attrs) {
        if (this.mContentLayoutGenerated && this.mActivity != null && this.mActivity.getMultiWindowStyle().isCascade() && (attrs.flags & 512) == 0) {
            attrs.flags |= 512;
        }
        super.dispatchWindowAttributesChanged(attrs);
    }

    private boolean getDisplaySize(Point size) {
        return getDisplaySize(size, false);
    }

    private boolean getDisplaySize(Point outbound, boolean isReal) {
        Rect rect = new Rect();
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        if (display == null) {
            return false;
        }
        display.getSize(outbound);
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && isReal && this.mMultiWindowFacade != null) {
            DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
            if (displayInfo != null) {
                this.mMultiWindowFacade.getRealSize(rect, displayInfo);
                outbound.x = rect.width();
                outbound.y = rect.height();
            }
        }
        outbound.x = (int) (((float) outbound.x) / this.mDssScale);
        outbound.y = (int) (((float) outbound.y) / this.mDssScale);
        return true;
    }

    private boolean checkMaxStackSize(Rect rect, int orientation) {
        int mw;
        int mh;
        Point d = new Point();
        getDisplaySize(d, true);
        int w = rect.width();
        int h = rect.height();
        int dw = (int) (((float) d.x) * this.mMaxSizeRatio);
        int dh = (int) (((float) d.y) * this.mMaxSizeRatio);
        if (orientation == 1) {
            mw = this.mMinStackBoundForPort.width();
            mh = this.mMinStackBoundForPort.height();
        } else {
            mw = this.mMinStackBoundForLand.width();
            mh = this.mMinStackBoundForLand.height();
        }
        boolean isSelective = false;
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && this.mActivity != null) {
            isSelective = this.mActivity.isSelectiveOrientationState();
            if (isSelective) {
                if (orientation == 1) {
                    mw = this.mMaxPortStackBoundForSelectiveOrientation.width();
                    mh = this.mMaxPortStackBoundForSelectiveOrientation.height();
                } else {
                    mw = this.mMaxLandStackBoundForSelectiveOrientation.width();
                    mh = this.mMaxLandStackBoundForSelectiveOrientation.height();
                }
                DecorView decor = (DecorView) getDecorView();
                ((MultiPhoneDecorView) decor).mMaxWidth = mw;
                ((MultiPhoneDecorView) decor).mMaxHeight = mh;
            }
        }
        if (w < mw || h < mh || (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && isSelective)) {
            int gapWidth = (mw - w) / 2;
            rect.set(rect.left - gapWidth, rect.top, (rect.left + mw) - gapWidth, rect.top + mh);
        }
        w = rect.width();
        h = rect.height();
        if (w > dw) {
            rect.set(rect.left, rect.top, rect.left + dw, rect.top + ((int) (((float) h) * (((float) dw) / ((float) w)))));
            return true;
        } else if (h <= dh) {
            return false;
        } else {
            rect.set(rect.left, rect.top, rect.left + ((int) (((float) w) * (((float) dh) / ((float) h)))), rect.top + dh);
            return true;
        }
    }

    public Object getMultiPhoneWindowEvent() {
        return this;
    }

    public MultiWindowStyle getMultiWindowStyle() {
        if (this.mMultiWindowStyle == null && this.mActivity != null) {
            this.mMultiWindowStyle = new MultiWindowStyle(this.mActivity.getMultiWindowStyle());
        }
        return this.mMultiWindowStyle;
    }

    public boolean hasStackFocus() {
        return this.mHasStackFocus;
    }

    public int getOptionsPanelGravity() {
        if (!(this.mContext == null || this.mActivity == null)) {
            DisplayInfo di = new DisplayInfo();
            getWindowManager().getDefaultDisplay().getDisplayInfo(di);
            MultiWindowStyle style = getMultiWindowStyle();
            int requestOrientation = this.mActivity.getRequestedOrientation();
            if (style != null && style.getType() == 2 && requestOrientation == 1 && di != null && (di.rotation == 1 || di.rotation == 3)) {
                return 81;
            }
        }
        return super.getOptionsPanelGravity();
    }

    public boolean isTouchBlocked() {
        if (this.mMultiWindowStyle != null && (this.mMultiWindowStyle.getType() != 1 || this.mMultiWindowStyle.isEnabled(4096))) {
            return false;
        }
        DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        if (this.mContext.getResources().getConfiguration().orientation == 1) {
            if (((float) getStackBound().height()) / ((float) dm.heightPixels) <= MultiWindowFacade.SPLIT_MIN_WEIGHT) {
                return true;
            }
            return false;
        } else if (((float) getStackBound().width()) / ((float) dm.widthPixels) <= MultiWindowFacade.SPLIT_MIN_WEIGHT) {
            return true;
        } else {
            return false;
        }
    }

    private void initStackBound() {
        int i = 1;
        Rect bound = this.mMultiWindowFacade.getStackOriginalBound(this.mToken);
        if (bound != null) {
            if (!(bound.height() > bound.width())) {
                i = 2;
            }
            checkMaxStackSize(bound, i);
            setStackBound(bound);
        }
    }

    public Rect getStackBound() {
        return this.mStackBound;
    }

    protected void setStackBound(Rect requestBound) {
        if (requestBound != null && !requestBound.isEmpty()) {
            if (getState() == 3 || getState() == 2) {
                adjustStackBound(requestBound);
            }
            if (this.mMultiWindowFacade != null && !requestBound.equals(getStackBound())) {
                boolean sizeChanged = false;
                if (!(getStackBound().width() == requestBound.width() && getStackBound().height() == requestBound.height())) {
                    sizeChanged = true;
                }
                this.mTmpBound.set(requestBound);
                if (DEBUG) {
                    Log.i(TAG, "setStackBounds, requestBound=" + requestBound + ", mActivity=" + this.mActivity);
                }
                this.mMultiWindowFacade.setStackBound(this.mToken, requestBound);
                this.mLastStackBound.set(getStackBound());
                getStackBound().set(requestBound);
                this.mRotation = getWindowManager().getDefaultDisplay().getRotation();
                if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && this.mMultiWindowFacade != null && this.mActivity != null && this.mActivity.isSelectiveOrientationState()) {
                    DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
                    if (displayInfo != null) {
                        this.mRotation = displayInfo.rotation;
                    }
                }
                if (!getMultiWindowStyle().isNormal() && sizeChanged && this.mMultiWindowListener != null) {
                    this.mMultiWindowListener.onSizeChanged(requestBound);
                }
            }
        }
    }

    private void setStackBoundByStackId(int stackId, Rect bound) {
        if (bound != null) {
            if (getState() != 4) {
                adjustStackBound(bound);
            }
            if (this.mMultiWindowFacade != null && !bound.isEmpty()) {
                this.mMultiWindowFacade.setStackBoundByStackId(stackId, bound);
            }
        }
    }

    private void setStackBoundInScreen(int leftBoundary, int topBoundary, boolean isMinimizing) {
        boolean adjustBound = false;
        Point screenSize = new Point();
        getDisplaySize(screenSize, true);
        Rect currStackBound = new Rect(getStackBound());
        if (currStackBound.left < leftBoundary) {
            currStackBound.offset(leftBoundary - currStackBound.left, 0);
            adjustBound = true;
        }
        if (currStackBound.top < topBoundary) {
            currStackBound.offset(0, topBoundary - currStackBound.top);
            adjustBound = true;
        }
        if (!isMinimizing) {
            if (currStackBound.right > screenSize.x) {
                currStackBound.offset(screenSize.x - currStackBound.right, 0);
                adjustBound = true;
            }
            if (currStackBound.bottom > screenSize.y) {
                currStackBound.offset(0, screenSize.y - currStackBound.bottom);
                adjustBound = true;
            }
            Rect tempStackBound = new Rect(currStackBound);
            adjustStackBound(tempStackBound);
            if (!currStackBound.equals(tempStackBound)) {
                currStackBound.set(tempStackBound);
                adjustBound = true;
            }
        }
        if (adjustBound) {
            setStackBound(currStackBound);
        }
    }

    private void setStackBoundsInternel(Rect newBounds) {
        if (newBounds != null && !newBounds.equals(getStackBound())) {
            boolean sizeChanged = false;
            if (!(getStackBound().width() == newBounds.width() && getStackBound().height() == newBounds.height())) {
                sizeChanged = true;
            }
            this.mTmpBound.set(newBounds);
            this.mLastStackBound.set(newBounds);
            getStackBound().set(newBounds);
            this.mRotation = getWindowManager().getDefaultDisplay().getRotation();
            if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && this.mMultiWindowFacade != null && this.mActivity != null && this.mActivity.isSelectiveOrientationState()) {
                DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
                if (displayInfo != null) {
                    this.mRotation = displayInfo.rotation;
                }
            }
            if (this.mContentLayoutGenerated && !getMultiWindowStyle().isNormal() && sizeChanged && this.mMultiWindowListener != null) {
                this.mMultiWindowListener.onSizeChanged(newBounds);
            }
        }
    }

    private void adjustScaleFactor() {
        this.mHScale = 1.0f;
        this.mVScale = 1.0f;
        if (getState() == 3) {
            Point size = new Point();
            getDisplaySize(size);
            if (this.mActivity != null && ((this.mActivity.getRequestedOrientation() == 1 && size.x > size.y) || ((this.mActivity.getRequestedOrientation() == 0 && size.x < size.y) || !validateStackBound(this.mStackBound)))) {
                int temp = size.x;
                size.x = size.y;
                size.y = temp;
            }
            this.mHScale = ((float) getStackBound().width()) / ((float) size.x);
            this.mVScale = ((float) getStackBound().height()) / ((float) (size.y + this.mTitleBarHeight));
            if (DEBUG_FLOATING_CYCLE) {
                Log.d(TAG, "adjustScaleFactor mStackBound=" + getStackBound() + ",size=" + size);
            }
        }
        if (DEBUG_FLOATING_CYCLE) {
            Log.d(TAG, "adjustScaleFactor result hScale=" + this.mHScale + ",vScale=" + this.mVScale);
        }
        WindowManagerGlobal.getInstance().setMultiWindowScale(this.mHScale, this.mVScale);
        View decorView = getDecorView();
        if (decorView != null && decorView.getViewRootImpl() != null) {
            decorView.getViewRootImpl().setMultiWindowScale(this.mHScale, this.mVScale);
        }
    }

    private boolean adjustMinimizedStackBound(Rect stackBound) {
        boolean outOfBound = false;
        int originPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_minimized_height);
        Point screenSize = new Point();
        getDisplaySize(screenSize, true);
        int right = stackBound.left + originPixelSize;
        int bottom = stackBound.top + originPixelSize;
        if (stackBound.left < 0) {
            stackBound.offset(-stackBound.left, 0);
            outOfBound = true;
        } else if (right > screenSize.x) {
            stackBound.offset(screenSize.x - right, 0);
            outOfBound = true;
        }
        if (bottom > screenSize.y) {
            stackBound.offset(0, screenSize.y - bottom);
            return true;
        } else if (stackBound.top >= this.mStatusBarHeight) {
            return outOfBound;
        } else {
            stackBound.offset(0, this.mStatusBarHeight - stackBound.top);
            return true;
        }
    }

    private void adjustStackBound(Rect stackBound) {
        int boundaryX = stackBound.width() / 3;
        int boundaryY = stackBound.height() / 3;
        int headerWindowMargin = 0;
        if (this.mContentLayoutGenerated) {
            boundaryX = (stackBound.width() / 2) + this.mPenWindowController.getControllerHeight();
            boundaryY = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_pen_window_show_boundaryY);
            headerWindowMargin = this.mPenWindowController.getControllerHeight() / 2;
        }
        Point maxSize = new Point();
        getDisplaySize(maxSize, true);
        if (stackBound.left > maxSize.x - boundaryX) {
            stackBound.offsetTo(maxSize.x - boundaryX, stackBound.top);
        } else if (stackBound.right < boundaryX) {
            stackBound.offsetTo(boundaryX - stackBound.width(), stackBound.top);
        }
        if (stackBound.top > maxSize.y - boundaryY) {
            stackBound.offsetTo(stackBound.left, maxSize.y - boundaryY);
        } else if (stackBound.top < this.mStatusBarHeight + headerWindowMargin) {
            stackBound.offsetTo(stackBound.left, this.mStatusBarHeight + headerWindowMargin);
        }
    }

    public boolean moveStackBound(int dx, int dy, boolean moving) {
        Rect stackBound = new Rect(getStackBound());
        stackBound.offset(dx, dy);
        adjustStackBound(stackBound);
        setStackBound(stackBound);
        return false;
    }

    private void updateMultiWindowStyle(MultiWindowStyle newStyle) {
        if (DEBUG_FLOATING_CYCLE) {
            Log.d(TAG, "updateMultiWindowStyle style:" + getMultiWindowStyle() + ",newStyle=" + newStyle);
        }
        if (!getMultiWindowStyle().equals(newStyle)) {
            getMultiWindowStyle().setTo(newStyle, true);
            getMultiWindowStyle().setAppRequestOrientation(newStyle.getAppRequestOrientation(), true);
            dismissWritingBuddy();
        }
    }

    public void setAppRequestOrientation(int requestedOrientation) {
        int prevOrientation = getMultiWindowStyle().getAppRequestOrientation();
        if (prevOrientation != requestedOrientation) {
            getMultiWindowStyle().setAppRequestOrientation(requestedOrientation, true);
            if (DEBUG) {
                Log.i(TAG, "setAppRequestOrientation(), prev=" + prevOrientation + ", requested=" + requestedOrientation + ", a=" + this.mActivity);
            }
        }
    }

    public void arrangeScaleStackBound() {
        if (this.mActivity.isResumed() && getMultiWindowStyle().isCascade() && checkRotationNeeded(this.mActivity.getRequestedOrientation())) {
            boolean bChangedStack = false;
            Rect stackBound = this.mMultiWindowFacade.getStackOriginalBound(this.mToken);
            if (stackBound != null) {
                switch (this.mActivity.getRequestedOrientation()) {
                    case 0:
                    case 6:
                    case 8:
                    case 11:
                        if (stackBound.width() < stackBound.height()) {
                            bChangedStack = true;
                            break;
                        }
                        break;
                    case 1:
                    case 7:
                    case 9:
                    case 12:
                        if (stackBound.width() > stackBound.height()) {
                            bChangedStack = true;
                            break;
                        }
                        break;
                }
                if (bChangedStack) {
                    Rect oldRect = getStackBound();
                    Rect newRect = new Rect(oldRect.top, oldRect.left, oldRect.bottom, oldRect.right);
                    if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
                        newRect.set(oldRect.left, oldRect.top, oldRect.right, oldRect.bottom);
                        Point displaySize = new Point();
                        getDisplaySize(displaySize, true);
                        int displayOrientation = displaySize.y > displaySize.x ? 1 : 2;
                        int preferredOrientation = this.mActivity.getPreferredOrientation();
                        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && this.mActivity.isSelectiveOrientationState()) {
                            displayOrientation = displayOrientation == 1 ? 2 : 1;
                        }
                        checkMaxStackSize(newRect, displayOrientation);
                    }
                    setStackBound(newRect);
                    initStackBound();
                    adjustScaleFactor();
                    if (this.mContentLayoutGenerated && this.mPenWindowController != null) {
                        this.mPenWindowController.updatePosition();
                    }
                }
            }
        }
    }

    private boolean checkRotationNeeded(int requestedOrientation) {
        int expectedOrientation = this.mMultiWindowFacade.getExpectedOrientation();
        int expectedExplicitOrientation = getExplicitOrientation(expectedOrientation);
        boolean rotated = isRotated(getWindowManager().getDefaultDisplay().getRotation());
        boolean rotationNeeded = false;
        if (expectedExplicitOrientation != -1 && ((expectedOrientation == 1 && rotated) || (expectedOrientation == 0 && !rotated))) {
            rotationNeeded = true;
        }
        int requestedExplicitOrientation = getExplicitOrientation(requestedOrientation);
        if (rotationNeeded || requestedExplicitOrientation == -1 || requestedExplicitOrientation == expectedExplicitOrientation) {
            return rotationNeeded;
        }
        return false;
    }

    private int getExplicitOrientation(int requestedOrientation) {
        switch (requestedOrientation) {
            case 0:
            case 6:
            case 8:
            case 11:
                return 0;
            case 1:
            case 7:
            case 9:
            case 12:
                return 1;
            default:
                return -1;
        }
    }

    private boolean validateStackBound(Rect bound) {
        if (bound == null) {
            return false;
        }
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        if ((screenWidth < screenHeight && bound.width() < bound.height()) || (screenWidth > screenHeight && bound.width() > bound.height())) {
            return true;
        }
        if (DEBUG_ORIENTATION) {
            Log.d(TAG, "validateStackBound " + (getAttributes() != null ? getAttributes().getTitle() : "") + ", bound=Point(" + bound.width() + ", " + bound.height() + ")" + ", screenSize=(" + screenWidth + FingerprintManager.FINGER_PERMISSION_DELIMITER + screenHeight + ")");
        }
        return false;
    }

    public void onMultiWindowStyleChanged(MultiWindowStyle style, int notifyReason) {
        if (DEBUG_FLOATING_CYCLE) {
            Log.d(TAG, "onMultiWindowStyleChanged, reason=0x" + Integer.toHexString(notifyReason) + ", newStyle" + style + ", oldStyle=" + getMultiWindowStyle() + ", mActivity=" + this.mActivity);
        }
        if ((notifyReason & 32) != 0) {
            requestState(4);
            refreshUI(4);
        } else if ((notifyReason & 4) == 0) {
            if ((notifyReason & 1) != 0) {
                if (!(this.mMinimizeAnimator == null || !getMultiWindowStyle().isEnabled(4) || style.isEnabled(4))) {
                    this.mMinimizeAnimator.updateMultiWindowStyleChanged(null);
                }
                if (this.mContentLayoutGenerated) {
                    this.mPenWindowController.removeDnDHelpPopup();
                }
            }
            if (this.mMultiWindowListener != null) {
                if (getMultiWindowStyle().getType() != style.getType()) {
                    boolean z;
                    StateChangeListener stateChangeListener = this.mMultiWindowListener;
                    if (style.getType() != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    stateChangeListener.onModeChanged(z);
                } else if (getMultiWindowStyle().getZone() != style.getZone()) {
                    this.mMultiWindowListener.onZoneChanged(style.getZone());
                }
            }
            if (style.isEnabled(4)) {
                state = getState();
                updateMultiWindowStyle(style);
                setStackBoundsInternel(this.mMultiWindowFacade.getStackOriginalBound(this.mToken));
                if (!(this.mMinimizeAnimator == null || (notifyReason & 256) == 0)) {
                    this.mMinimizeAnimator.updateMultiWindowStyleChanged(getStackBound());
                }
                if (state == 1) {
                    refreshUI(-1);
                }
                dismissGuide();
                return;
            }
            updateMultiWindowStyle(style);
            dismissGuide();
            setStackBoundsInternel(this.mMultiWindowFacade.getStackOriginalBound(this.mToken));
            if ((notifyReason & 256) != 0 && this.mContentLayoutGenerated) {
                this.mPenWindowController.performUpdateMenuVisibility(false);
            }
            if (this.mContentLayoutGenerated) {
                this.mPenWindowController.invalidate();
                refreshUI(-1);
            }
        } else if (style.isEnabled(131072)) {
            setStackBoundsInternel(style.getBounds());
            refreshUI(4);
        } else if (style.getType() == 2) {
            ViewRootImpl viewRootImpl = getDecorView().getViewRootImpl();
            if ((viewRootImpl != null && !viewRootImpl.getStopped()) || !this.mPowerManager.isInteractive()) {
                state = getState();
                if (state == 4) {
                    return;
                }
                if (state == 2 || state == 3) {
                    refreshUI(4);
                }
            }
        }
    }

    public void onMultiWindowConfigurationChanged(int configDiff) {
        if ((configDiff & 256) != 0) {
            updateRestricedStackBounds();
        }
        if ((configDiff & 128) != 0 && this.mActivity != null) {
            int mwState = getState();
            if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && mwState == 3) {
                if (getMultiWindowStyle().isFakeTarget(this.mActivity.getPreferredOrientation())) {
                    if (this.mContentLayoutGenerated) {
                        this.mPenWindowController.performUpdateMenuVisibility(false);
                        return;
                    }
                    return;
                }
            }
            Rect oldRect = getStackBound();
            Rect newRect = new Rect();
            this.mLastOrientation = this.mActivity.getResources().getConfiguration().orientation;
            if (DEBUG_ORIENTATION) {
                Log.d(TAG, "onMultiWindowConfigurationChanged " + (getAttributes() != null ? getAttributes().getTitle() : "") + "oldRect=" + oldRect);
            }
            boolean rotated = isRotated(getWindowManager().getDefaultDisplay().getRotation());
            if (this.mLastRotated != rotated || !validateStackBound(oldRect) || mwState == 4) {
                this.mLastRotated = rotated;
                if (mwState == 3 || mwState == 4) {
                    if (this.mContentLayoutGenerated) {
                        this.mPenWindowController.performUpdateMenuVisibility(false);
                        if (this.mMinimizeAnimator != null) {
                            this.mMinimizeAnimator.updateMultiWindowConfigurationChanged();
                        }
                    }
                    this.mDismissGuideByDockingCanceled = true;
                    return;
                }
                switch (mwState) {
                    case 1:
                        setStackBound(this.mMultiWindowFacade.getStackOriginalBound(this.mToken));
                        return;
                    case 2:
                        initStackBound();
                        oldRect = getStackBound();
                        Point size = new Point();
                        getDisplaySize(size);
                        int dx = 0;
                        int dy = 0;
                        newRect.set(oldRect);
                        if (oldRect.left < 0) {
                            dx = -oldRect.left;
                        } else {
                            if (oldRect.right > size.x) {
                                dx = size.x - oldRect.right;
                            }
                        }
                        if (oldRect.top < this.mStatusBarHeight) {
                            dy = this.mStatusBarHeight - oldRect.top;
                        } else {
                            if (oldRect.bottom > size.y) {
                                dy = size.y - oldRect.bottom;
                            }
                        }
                        if (!(dx == 0 && dy == 0)) {
                            newRect.offset(dx, dy);
                        }
                        setStackBound(newRect);
                        return;
                    case 3:
                    case 4:
                        Point maxSize = new Point();
                        getDisplaySize(maxSize);
                        this.mDismissGuideByDockingCanceled = true;
                        int requestOrientation = this.mActivity.getRequestedOrientation();
                        if ((requestOrientation == 0 || requestOrientation == 1) && this.mActivity.getMultiWindowStyle().isEnabled(4194304)) {
                            ViewRootImpl viewRootImpl = getDecorView().getViewRootImpl();
                            if ((viewRootImpl != null && !viewRootImpl.getStopped()) || this.mActivity.getMultiWindowStyle() == null || !this.mActivity.getMultiWindowStyle().isEnabled(4)) {
                                float relativeL = ((float) oldRect.left) / ((float) maxSize.y);
                                float relativeR = ((float) (maxSize.y - oldRect.right)) / ((float) maxSize.y);
                                if (relativeR > 0.0f && maxSize.x - oldRect.width() > 0) {
                                    relativeL = ((((float) (maxSize.x - oldRect.width())) / ((float) maxSize.x)) * relativeL) / (relativeL + relativeR);
                                }
                                newRect.offsetTo((int) (((float) maxSize.x) * (5.0E-5f + relativeL)), (int) (((float) maxSize.y) * (5.0E-5f + (((float) oldRect.top) / ((float) maxSize.x)))));
                                newRect.right = newRect.left + oldRect.width();
                                newRect.bottom = newRect.top + oldRect.height();
                            } else {
                                return;
                            }
                        }
                        newRect = new Rect(oldRect.top, oldRect.left, oldRect.bottom, oldRect.right);
                        if (mwState == 3 && validateStackBound(oldRect) && this.mContentLayoutGenerated) {
                            this.mPenWindowController.performUpdateVisibility(true);
                        }
                        if (validateStackBound(newRect)) {
                            checkMaxStackSize(newRect, this.mLastOrientation);
                            setStackBound(newRect);
                            adjustScaleFactor();
                            if (mwState == 3 && this.mContentLayoutGenerated) {
                                this.mPenWindowController.performUpdateVisibility(true);
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void onMultiWindowFocusChanged(int notifyReason, boolean focus, boolean keepInputMethod) {
        if (DEBUG && focus) {
            Log.d(TAG, "onMultiWindowFocusChanged: " + getMultiWindowStyle().getType() + " type (" + MultiWindowStyle.zoneToString(getMultiWindowStyle().getZone()) + ")");
        }
        if (!((notifyReason & 1) == 0 && (notifyReason & 2) == 0 && (notifyReason & 4) == 0)) {
            setStackFocus(focus);
        }
        if (getMultiWindowStyle().isSplit() && !focus) {
            closeAllPanels();
            if (!keepInputMethod) {
                forceHideInputMethod();
            }
        }
    }

    private void setStackFocus(boolean focus) {
        if (this.mHasStackFocus != focus) {
            this.mHasStackFocus = focus;
            if (this.mSubWindow != null) {
                this.mSubWindow.changeStackFocus(this.mHasStackFocus);
            }
            refreshBorder();
        }
    }

    private int getState() {
        MultiWindowStyle style = getMultiWindowStyle();
        switch (style.getType()) {
            case 2:
                if (style.isEnabled(4)) {
                    return 4;
                }
                if (style.isEnabled(2048)) {
                    return 3;
                }
                return 2;
            default:
                return 1;
        }
    }

    private void requestState(int state) {
        MultiWindowStyle requestStyle = new MultiWindowStyle(getMultiWindowStyle());
        requestStyle.setBounds(getStackBound());
        if (DEBUG_FLOATING_CYCLE) {
            Log.d(TAG, "requestState state:" + state + ",currentState=" + getState() + " caller=" + Debug.getCallers(2));
        }
        switch (state) {
            case 1:
                requestStyle.setType(0);
                this.mMultiWindowFacade.setMultiWindowStyle(this.mToken, requestStyle);
                return;
            case 2:
                requestStyle.setType(2, false);
                requestStyle.setZone(0);
                requestStyle.setOption(16, true);
                requestStyle.setOption(2048, false);
                requestStyle.setOption(4, false);
                this.mMultiWindowFacade.setMultiWindowStyle(this.mToken, requestStyle);
                return;
            case 3:
                requestStyle.setType(2, false);
                requestStyle.setZone(0);
                requestStyle.setOption(16, false);
                requestStyle.setOption(2048, true);
                requestStyle.setOption(4, false);
                this.mMultiWindowFacade.setMultiWindowStyle(this.mToken, requestStyle);
                return;
            case 4:
                if (this.mIsMinimizeDisabled) {
                    Log.w(TAG, "minimize function is disabled. do not minimize");
                    return;
                } else if (!requestStyle.isEnabled(4)) {
                    if (requestStyle.getType() != 2) {
                        requestStyle.setType(2, false);
                        requestStyle.setOption(2048, true);
                    }
                    requestStyle.setOption(4, true);
                    this.mMultiWindowFacade.setMultiWindowStyle(this.mToken, requestStyle);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private void refreshUI(int forceState) {
        if (!this.mContentLayoutGenerated) {
            return;
        }
        if (!this.mContentLayoutGenerated || this.mPenWindowController.getWindowToken() != null) {
            int state;
            this.mIsBorderShown = false;
            if (forceState != -1) {
                state = forceState;
            } else {
                state = getState();
            }
            if (DEBUG_FLOATING_CYCLE) {
                Log.d(TAG, "refreshUI state=" + state);
            }
            LayoutParams attrs = getAttributes();
            if (state != 4) {
                if (this.mIsSecure) {
                    attrs.flags |= 8192;
                }
            } else if ((attrs.flags & 8192) != 0) {
                attrs.flags &= -8193;
                this.mIsSecure = true;
            }
            int flags = attrs.flags;
            switch (state) {
                case 1:
                    if (this.mMinimizeAnimator != null) {
                        this.mMinimizeAnimator.removeWindow();
                    }
                    if (this.mContentLayoutGenerated) {
                        this.mPenWindowController.performUpdateVisibility(false);
                        if (this.mIsSupportSimplificationUI) {
                            this.mPenWindowController.removeResizeVisualCue();
                        }
                    }
                    getDecorView().setVisibility(0);
                    initFocusedView();
                    if ((this.mInitialFlag & 512) == 0) {
                        attrs.flags &= -513;
                    }
                    getDecorView().requestLayout();
                    getDecorView().invalidate();
                    break;
                case 2:
                    if (this.mMinimizeAnimator != null) {
                        this.mMinimizeAnimator.removeWindow();
                    }
                    adjustScaleFactor();
                    if (this.mContentLayoutGenerated) {
                        this.mPenWindowController.performUpdateVisibility(true);
                    }
                    getDecorView().setVisibility(0);
                    initFocusedView();
                    attrs.flags |= 512;
                    break;
                case 3:
                    if (this.mMinimizeAnimator != null) {
                        this.mMinimizeAnimator.removeWindow();
                    }
                    this.mIsBorderShown = true;
                    adjustScaleFactor();
                    if (this.mContentLayoutGenerated) {
                        this.mPenWindowController.performUpdateVisibility(true);
                    }
                    getDecorView().setVisibility(0);
                    restoreFocusedView();
                    attrs.flags |= 512;
                    refreshBorder();
                    break;
                case 4:
                    if (this.mIsMinimizeDisabled) {
                        Log.w(TAG, "minimize function is disabled. do not minimize.");
                        return;
                    } else if (this.mMinimizeAnimator == null || !this.mMinimizeAnimator.isMinimizeIconVisible()) {
                        if (this.mFloatingFlag == -1) {
                            this.mFloatingFlag = flags;
                        }
                        requestState(4);
                        if (this.mMinimizeAnimator != null) {
                            showMinimizedIconWindow();
                        }
                        attrs.flags |= 512;
                        saveFocusedView();
                        getDecorView().setVisibility(4);
                        adjustScaleFactor();
                        break;
                    } else {
                        Log.w(TAG, "minimize icon is already visible.");
                        return;
                    }
                    break;
            }
            if (flags != attrs.flags) {
                setAttributes(attrs);
            }
        } else if (DEBUG) {
            Log.d(TAG, "token is null");
        }
    }

    private void showMinimizedIconWindow() {
        if (this.mContentLayoutGenerated) {
            if (DEBUG) {
                Log.i(TAG, "showMinimizedIconWindow");
            }
            if (this.mContentLayoutGenerated && this.mIsSupportSimplificationUI) {
                this.mPenWindowController.removeResizeVisualCue();
            }
            if (this.mCustomMinimizedView != null) {
                this.mMinimizeAnimator.makeMinimizeIconWindow(this.mCustomMinimizedView);
            } else if (this.mCustomMinimizedThumbnail != null) {
                this.mMinimizeAnimator.makeMinimizeIconWindow(this.mCustomMinimizedThumbnail);
            } else {
                this.mMinimizeAnimator.makeMinimizeIconWindow();
            }
        }
    }

    public void requestMaximize() {
        MultiWindowStyle multiWindowStyle = getMultiWindowStyle();
        if (multiWindowStyle.isEnabled(4)) {
            if (DEBUG_FLOATING_CYCLE) {
                Log.d(TAG, "Minimized->Floating");
            }
            multiWindowStyle.setOption(4, false);
            if (multiWindowStyle.isEnabled(131072)) {
                multiWindowStyle.setOption(2048, false);
                requestState(1);
                return;
            }
            setStackBoundInScreen(0, 0, false);
            requestState(getState());
            getDecorView().setVisibility(0);
        }
    }

    public int getRotationOfStackBound() {
        return this.mRotation;
    }

    public Docking getDockingInstance() {
        return this.mDocking;
    }

    public void dismissGuide() {
        if (this.mGuideView != null) {
            this.mGuideView.hide();
            this.mGuideView.dismiss();
        }
    }

    public void showGuide(Rect rect, int type) {
        if ((type != 3 || this.mHasStackFocus) && this.mIsAttachedToWindow && getMultiWindowStyle().isCascade()) {
            this.mGuideView.setGuideState(type);
            showGuide(rect);
        }
    }

    private void showGuide(Rect rect) {
        if (getMultiWindowStyle().isCascade()) {
            this.mGuideView.init();
            this.mGuideView.setMultiWindowFlags(24);
            DisplayInfo displayInfo = DisplayManagerGlobal.getInstance().getDisplayInfo(0, this.mToken);
            Rect scaledRect = new Rect(rect);
            scaledRect.scale(this.mDssScale);
            this.mGuideView.show(scaledRect.left, scaledRect.top, scaledRect.width(), scaledRect.height());
        }
    }

    private void updateIsFullScreen() {
        if (getDecorView().getLayoutParams() != null) {
            boolean isFullScreen = getDecorView().getLayoutParams().width == -1 && getDecorView().getLayoutParams().height == -1;
            if (this.mIsFullScreen != isFullScreen) {
                if (DEBUG) {
                    Log.i(TAG, "Refresh mIsFullScreen=" + this.mIsFullScreen + "->" + isFullScreen);
                }
                this.mIsFullScreen = isFullScreen;
            }
        }
    }

    private void refreshBorder() {
        if (getState() == 3) {
            MultiPhoneDecorView decorView = (MultiPhoneDecorView) getDecorView();
            if (this.mContentLayoutGenerated && !decorView.mIsResize) {
                this.mPenWindowController.performUpdateBackground();
            }
            if (this.mBorder != null) {
                this.mBorder.setFocus(this.mHasStackFocus);
                getDecorView().requestLayout();
                getDecorView().invalidate();
            }
        } else if (getMultiWindowStyle().isSplit()) {
            getDecorView().invalidate();
        }
    }

    protected boolean needTitleBar(MultiWindowStyle style) {
        if (style.isEnabled(16) || isFloatingWindow()) {
            return false;
        }
        return true;
    }

    private void dismissWritingBuddy() {
        if (this.mContentLayoutGenerated && !getMultiWindowStyle().isNormal()) {
            View decorView = getDecorView();
            if (decorView != null && decorView.getViewRootImpl() != null) {
                View v = decorView.getViewRootImpl().getCurrentWritingBuddyView();
                if (v != null && v.getWritingBuddy(false) != null) {
                    v.getWritingBuddy(false).finish(true);
                }
            }
        }
    }

    public void exitByCloseBtn() {
        if (this.mActivity == null) {
            return;
        }
        if (this.mMultiWindowExitListener == null || this.mMultiWindowExitListener.onWindowExit()) {
            try {
                this.mActivity.finishAffinity();
            } catch (IllegalStateException e) {
                this.mActivity.finish();
            }
            MultiWindowLoggingHelper.insertLog(this.mContext, MultiWindowLoggingHelper.SPLITWINDOW_LOGGING_FEATURE, MultiWindowLoggingHelper.CLOSE_TYPE);
            return;
        }
        Log.w(TAG, "onWindowExit return false");
    }

    public boolean setStateChangeListener(StateChangeListener listener) {
        this.mMultiWindowListener = listener;
        return true;
    }

    public boolean setStateChangeListener2(StateChangeListener2 listener) {
        this.mMultiWindowListener2 = listener;
        return true;
    }

    public boolean setExitListener(ExitListener listener) {
        this.mMultiWindowExitListener = listener;
        return true;
    }

    public void moveWindow(View view) {
        view.setOnTouchListener(this.mPenWindowController);
    }

    public void multiWindow(int windowMode, boolean pinup) {
        requestState(2);
    }

    public void minimizeWindow(int windowMode, boolean hide) {
        if (getState() != 4 && this.mMultiWindowFacade != null) {
            this.mMultiWindowFacade.minimizeWindow(this.mToken);
        }
    }

    public void normalWindow(int windowMode) {
        requestState(1);
    }

    public PointF getScaleInfo() {
        View decorView = getDecorView();
        if (decorView == null || decorView.getViewRootImpl() == null) {
            return new PointF(1.0f, 1.0f);
        }
        return decorView.getViewRootImpl().getMultiWindowScale();
    }

    public void setIsolatedCenterPoint(Point point) {
        MultiWindowStyle style = getMultiWindowStyle();
        if (this.mMultiWindowFacade != null) {
            style.setIsolatedCenterPoint(point);
            updateMultiWindowStyle(style);
            this.mMultiWindowFacade.updateIsolatedCenterPoint(point);
        }
    }

    public void disableMultiWindowTrayBar(boolean disable) {
        LayoutParams attrs = getAttributes();
        if (disable) {
            attrs.multiWindowFlags |= 2;
        } else {
            attrs.multiWindowFlags &= -3;
        }
        setAttributes(attrs);
    }

    public void setMinimizeIcon(Drawable icon) {
        if (icon == null) {
            this.mCustomMinimizedThumbnail = null;
            return;
        }
        if (this.mCustomMinimizedView != null) {
            this.mCustomMinimizedView = null;
        }
        if (this.mCustomMinimizedThumbnail == null) {
            this.mCustomMinimizedThumbnail = ApplicationThumbnail.create(this.mActivity);
        }
        this.mCustomMinimizedThumbnail.setCustomMinimizeIcon(icon);
    }

    public void setMinimizeView(View view) {
        if (!MultiWindowFeatures.isSupportFreeStyle(this.mContext)) {
            return;
        }
        if (view == null) {
            this.mCustomMinimizedView = null;
        } else if (view.getParent() != null) {
            Log.w(TAG, "setMinimizeView : request view is already added >> " + view);
        } else if (view.getLayoutParams().width == -2 && view.getLayoutParams().height == -2) {
            if (this.mCustomMinimizedThumbnail != null) {
                this.mCustomMinimizedThumbnail = null;
            }
            this.mCustomMinimizedView = view;
        } else {
            Log.w(TAG, "setMinimizeView : view width and height must be WRAP_CONTENT.");
        }
    }
}
