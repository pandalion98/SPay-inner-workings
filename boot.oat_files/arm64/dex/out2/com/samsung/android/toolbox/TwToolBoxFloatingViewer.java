package com.samsung.android.toolbox;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IKnoxModeChangeObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.DVFSHelper;
import android.os.Handler;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.service.dreams.IDreamManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.interpolator.SineInOut80;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import com.samsung.android.cover.CoverManager;
import com.samsung.android.cover.CoverManager.StateListener;
import com.samsung.android.cover.CoverState;
import com.samsung.android.quickconnect.IQuickConnectManager;
import com.samsung.android.toolbox.ITwToolBoxServiceCallback.Stub;
import dalvik.system.VMRuntime;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TwToolBoxFloatingViewer extends FrameLayout {
    private static final String TAG = "toolbox";
    private static final String TOOLBOX_EDIT_INTENT = "com.samsung.TOOLBOX_LIST";
    private final String[] BLOCK_LIST = new String[]{"com.sec.android.app.camera", "com.sec.android.app.camera.shootingmode.sequence", "com.sec.android.app.ma.recorder", "com.sec.android.app.simcardmanagement", "com.sec.android.app.kidshome", "com.sec.kidsplat.parentalcontrol", "com.sec.android.app.magnifier", "com.sec.android.app.bcocr", "com.sec.android.app.ocr3", "com.sec.allsharecastplayer"};
    private final int FIXED_MAIN_ITEM_GAP;
    private final int FIXED_MAIN_ITEM_HEIGHT;
    private final int FIXED_MAIN_ITEM_WIDTH;
    private final int FIXED_ROUND_TAIL_SIZE;
    private final int FIXED_SHORTCUT_ITEM_GAP;
    private final int FIXED_SHORTCUT_ITEM_SIZE;
    private final int FIXED_SHORTCUT_SHADOW_HEIGHT_PX;
    private final int FIXED_START_POSITION_RIGHT_PADDING;
    private final int FIXED_START_POSITION_TOP_PADDING;
    private final int LONG_PRESS_PANEL_BUTTON_BG_HEIGHT;
    private final int LONG_PRESS_PANEL_BUTTON_BG_WIDTH;
    private final int LONG_PRESS_PANEL_BUTTON_SIZE;
    private final int LONG_PRESS_PANEL_HEIGHT_PX;
    private final int LONG_PRESS_PANEL_TOP_EMPTY_SPACE_PX;
    private final float MAIN_DECREASE_RATIO = 0.24f;
    private final float MAIN_DIM_RATIO = 0.4f;
    private final int SCALED_APPROACH_SLOP;
    private final int SCALED_TOUCH_SLOP;
    private final int SLEEP_TIME_OUT = 3000;
    private final int TOOLBOX_MAX_ITEM_COUNT = 5;
    private final int TOTAL_HEIGHT;
    private int mAbsX;
    private int mAbsY;
    private ActivityManager mActivityManager;
    private Object[] mAllPackages;
    private final LayoutParams mAndromedaWindowAttributes;
    private boolean mBoxAnimating = false;
    private ITwToolBoxServiceCallback mCallbcak = new Stub() {
        public boolean isContain(int x, int y) throws RemoteException {
            if (TwToolBoxFloatingViewer.this.mHiddenToolBox || TwToolBoxFloatingViewer.this.mDisableFloatingViewer) {
                return false;
            }
            boolean conflict = TwToolBoxFloatingViewer.this.mGlobalRect.contains(x, y);
            Log.i(TwToolBoxFloatingViewer.TAG, "Callback isContain() " + conflict);
            return conflict;
        }

        public void receiveMessage(String pkg, int message, int option) throws RemoteException {
            int i = 2;
            if (message == 2) {
                TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mCheckEnvironment);
                if ((option & 8) != 0) {
                    String lastPackage = TwToolBoxFloatingViewer.this.mCurrentPackage;
                    TwToolBoxFloatingViewer.this.mCurrentPackage = pkg;
                    if (!lastPackage.equals(TwToolBoxFloatingViewer.this.mCurrentPackage)) {
                        boolean needBlock = false;
                        for (String blockPkg : TwToolBoxFloatingViewer.this.BLOCK_LIST) {
                            if (blockPkg.equals(TwToolBoxFloatingViewer.this.mCurrentPackage)) {
                                needBlock = true;
                                break;
                            }
                        }
                        if (needBlock) {
                            i = 4;
                        }
                        option |= i;
                    }
                    TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                    TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateVisibility, 100);
                }
                if ((option & 16) != 0) {
                    TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                    TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateVisibility, 100);
                }
                if ((option & 32) != 0) {
                    option = 4;
                }
                if (!((option & 2) == 0 || TwToolBoxFloatingViewer.this.mSipVisible)) {
                    TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mPlayAnimationFadeInRunnable);
                }
                if ((option & 4) != 0) {
                    TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mPlayAnimationFadeOutRunnable);
                }
            } else if (message == 3) {
                TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mTouchUpOrCancelRunnable, 100);
            }
        }
    };
    private boolean mCarMode = false;
    private ContentObserver mCarModeObserver;
    private int mCenterX;
    private int mCenterY;
    private final Runnable mCheckEnvironment = new Runnable() {
        public void run() {
            boolean needUpdate = false;
            boolean coverOpen = TwToolBoxFloatingViewer.this.getSCoverState();
            if (coverOpen != TwToolBoxFloatingViewer.this.mCoverOpen) {
                TwToolBoxFloatingViewer.this.mCoverOpen = coverOpen;
                needUpdate = true;
            }
            boolean isDreaming = false;
            try {
                if (TwToolBoxFloatingViewer.this.mDreamManager.isDreaming()) {
                    isDreaming = true;
                    Log.d(TwToolBoxFloatingViewer.TAG, "Daydream isDreaming");
                }
            } catch (RemoteException e) {
            }
            boolean keyguardShowing = TwToolBoxFloatingViewer.this.mDelegateKeyguardShowing.isKeyguardShowing() || isDreaming || (TwToolBoxFloatingViewer.this.mDelegateKeyguardShowing.isKeyguardSecure() && TwToolBoxFloatingViewer.this.mDelegateKeyguardShowing.inKeyguardRestrictedKeyInputMode());
            if (keyguardShowing != TwToolBoxFloatingViewer.this.mKeyguardShowing) {
                TwToolBoxFloatingViewer.this.mKeyguardShowing = keyguardShowing;
                needUpdate = true;
            }
            boolean OTAShowing = TwToolBoxFloatingViewer.this.isEnableOTA();
            if (OTAShowing != TwToolBoxFloatingViewer.this.mOTAShowing) {
                TwToolBoxFloatingViewer.this.mOTAShowing = OTAShowing;
                needUpdate = true;
            }
            if (needUpdate) {
                TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateVisibility, 300);
            }
        }
    };
    private Choreographer mChoreographer;
    private ContentResolver mContentResolver;
    private Context mContext;
    private CoverManager mCoverManager = null;
    private boolean mCoverOpen = true;
    private StateListener mCoverStateListener = new StateListener() {
        public void onCoverStateChanged(CoverState state) {
            TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mUpdateVisibility);
            TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateVisibility, 100);
        }
    };
    private String mCurrentPackage = "";
    private int mDVFSCookie = 0;
    private DVFSHelper mDVFSHelper = null;
    private DVFSHelper mDVFSHelperBUS = null;
    private DVFSHelper mDVFSHelperCore = null;
    private DVFSHelper mDVFSHelperGPU = null;
    private boolean mDVFSLockAcquired = false;
    private boolean mDeivceProvisioned = false;
    public DelegateKeyguardShowing mDelegateKeyguardShowing = new DelegateKeyguardShowing() {
        public boolean isKeyguardShowing() {
            return false;
        }

        public boolean isKeyguardLocked() {
            return false;
        }

        public boolean isKeyguardSecure() {
            return false;
        }

        public boolean inKeyguardRestrictedKeyInputMode() {
            return false;
        }
    };
    private int mDeltaX;
    private int mDeltaY;
    private final float mDensity;
    private ContentObserver mDeviceProvisionedObserver;
    private boolean mDisableFloatingViewer = false;
    private DisplayMetrics mDisplayMetrics;
    private final Rect mDownSlopArea = new Rect();
    private final Rect mDrawGlobalRect = new Rect();
    private IDreamManager mDreamManager;
    private int mEditAfterScreenCapture = -1;
    private boolean mEnableFloatingViewer = true;
    private Drawable mEndFloatingBg;
    private final Rect mEndFloatingBgArea = new Rect();
    private Drawable mEndFloatingEditBg;
    private final Rect mEndFloatingEditBgArea = new Rect();
    private Drawable mEndFloatingEditBtn;
    private final Rect mEndFloatingEditBtnArea = new Rect();
    private Drawable mEndFloatingEditBtnFocused;
    private final Rect mEndFloatingEditConflictArea = new Rect();
    private String mEndFloatingMsgEdit = "";
    private String mEndFloatingMsgRemove = "";
    private Drawable mEndFloatingRemoveBg;
    private final Rect mEndFloatingRemoveBgArea = new Rect();
    private Drawable mEndFloatingRemoveBtn;
    private final Rect mEndFloatingRemoveBtnArea = new Rect();
    private final Rect mEndFloatingRemoveBtnCoverArea = new Rect();
    private Drawable mEndFloatingRemoveBtnFocused;
    private Drawable mEndFloatingRemoveBtnFocusedCover;
    private final Rect mEndFloatingRemoveConflictArea = new Rect();
    private ValueAnimator mEnterAnimator;
    private float mEnterProgressValue = 0.0f;
    private ValueAnimator mExpandAnimator;
    private float mExpandProgressValue = 0.0f;
    private boolean mExpanded = false;
    private boolean mExploreByTouchMode = false;
    private ContentObserver mExploreByTouchModeObserver;
    private Drawable mFloatingBoxBg;
    private final Typeface mFont = Typeface.create("sec-roboto-light", 0);
    private final Rect mGlobalRect = new Rect();
    private final Runnable mGoToPositionAndromeda = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.goToPositionAndromeda();
        }
    };
    private final Runnable mGoToPositionCallback = new Runnable() {
        public void run() {
            int i = -1;
            boolean keepGoing = true;
            int deltaX = TwToolBoxFloatingViewer.this.mTargetX - TwToolBoxFloatingViewer.this.mCenterX;
            int deltaY = TwToolBoxFloatingViewer.this.mTargetY - TwToolBoxFloatingViewer.this.mCenterY;
            if (deltaX == 0 && deltaY == 0 && !TwToolBoxFloatingViewer.this.mTracking) {
                TwToolBoxFloatingViewer.this.setEnableVSync(false);
                keepGoing = false;
                if (TwToolBoxFloatingViewer.this.mNeedSaveCenterPosition) {
                    TwToolBoxFloatingViewer.this.saveCenterPosition();
                    TwToolBoxFloatingViewer.this.mNeedSaveCenterPosition = false;
                }
            }
            if (deltaX != 0 && Math.abs(deltaX) < TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP) {
                deltaX += (deltaX > 0 ? 1 : -1) * TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP;
            }
            if (deltaY != 0 && Math.abs(deltaY) < TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP) {
                int access$5800 = TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP;
                if (deltaY > 0) {
                    i = 1;
                }
                deltaY += access$5800 * i;
            }
            TwToolBoxFloatingViewer.access$5212(TwToolBoxFloatingViewer.this, deltaX / TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP);
            TwToolBoxFloatingViewer.access$5412(TwToolBoxFloatingViewer.this, deltaY / TwToolBoxFloatingViewer.this.SCALED_APPROACH_SLOP);
            TwToolBoxFloatingViewer.this.invalidate();
            if (TwToolBoxFloatingViewer.this.mChoreographer != null && keepGoing) {
                TwToolBoxFloatingViewer.this.mChoreographer.postCallback(1, TwToolBoxFloatingViewer.this.mGoToPositionCallback, null);
            }
        }
    };
    private boolean mHiddenToolBox = false;
    private boolean mInVSync = false;
    private final boolean mIsTablet;
    private boolean mKeyguardShowing = false;
    private boolean mKidsMode = false;
    private ContentObserver mKidsModeObserver;
    private boolean mKnoxRunning = false;
    private int mLastOrientation = -1;
    private ValueAnimator mLongPressPanelAnimator;
    private int mLongPressPanelPosition = 0;
    private boolean mLongPressPanelRemoveAnimating = false;
    private float mLongPressPanelRemoveBtnAngle = 0.0f;
    private final AnimatorSet mLongPressPanelRemoveBtnAnimator = new AnimatorSet();
    private int mLongPressPanelRemoveBtnPosition = 0;
    private boolean mLongPressPanelShowing = false;
    private boolean mLongPressed = false;
    private ToolBoxCharacter mMainCharacter;
    private int mMainCharacterDecreaseAmount = 0;
    private int mMainCharacterDegree;
    private TwToolBoxManager mManager;
    private final Canvas mMaskCanvas = new Canvas();
    private final Paint mMaskPaint = new Paint();
    private boolean mMovingByAnimation = false;
    private boolean mNeedSaveCenterPosition = false;
    private boolean mOTAShowing = false;
    private final ArrayList<ToolBoxMenu> mObjects = new ArrayList();
    private ContentObserver mPackageListObserver;
    private PackageManager mPackageManager;
    private int mPersonaId = 0;
    private PersonaManager mPersonaManager;
    private BroadcastReceiver mPinnedWindowReceiver;
    private final Runnable mPlayAnimationCollapseRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationCollapse();
        }
    };
    private final Runnable mPlayAnimationEnterRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationEnter();
        }
    };
    private final Runnable mPlayAnimationExpandRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationExpand();
        }
    };
    private final Runnable mPlayAnimationFadeInRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationFadeIn();
        }
    };
    private final Runnable mPlayAnimationFadeOutRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationFadeOut();
        }
    };
    private final Runnable mPlayAnimationSleepRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationSleep();
        }
    };
    private final Runnable mPlayAnimationWakeupRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.playAnimationWakeup();
        }
    };
    private ValueAnimator mPositionAnimatorX;
    private ValueAnimator mPositionAnimatorY;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                Log.i(TwToolBoxFloatingViewer.TAG, "android.intent.action.USER_SWITCHED");
                TwToolBoxFloatingViewer.this.mValidUser = TwToolBoxFloatingViewer.this.isValidUser();
                TwToolBoxFloatingViewer.this.loadCenterPosition();
                TwToolBoxFloatingViewer.this.reset();
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mGoToPositionAndromeda);
                TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mUpdateVisibility);
            }
        }
    };
    private Runnable mReset = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.clearToolBox();
            TwToolBoxFloatingViewer.this.prepareToolBoxLinear();
            TwToolBoxFloatingViewer.this.mReseting = false;
            TwToolBoxFloatingViewer.this.updateVisibility();
            TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mPlayAnimationWakeupRunnable);
            TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mPlayAnimationWakeupRunnable, 50);
        }
    };
    private boolean mReseting = false;
    private Resources mResources;
    private BroadcastReceiver mScreenUnlockReceiver;
    private final Runnable mScreenWriteRecovery = new Runnable() {
        public void run() {
            System.putIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, "edit_after_screen_capture", TwToolBoxFloatingViewer.this.mEditAfterScreenCapture, -2);
            TwToolBoxFloatingViewer.this.mEditAfterScreenCapture = -1;
        }
    };
    private final Rect mShadowRect = new Rect();
    private final ArrayList<Animator> mShakeAnimators = new ArrayList(3);
    private BroadcastReceiver mSipReceiver;
    private boolean mSipVisible = false;
    private ValueAnimator mSleepAnimator;
    private StatusBarManager mStatusBarManager;
    private int mTargetX;
    private int mTargetY;
    private final Rect mTextBounds = new Rect();
    private final Paint mTextPaint = new Paint();
    private final Runnable mTouchClickRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.onToolClick();
        }
    };
    private final Runnable mTouchLongClickRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.onToolLongClick();
        }
    };
    TouchMode mTouchMode = TouchMode.NORMAL;
    private ToolBoxMenu mTouchTarget;
    private final Runnable mTouchUpOrCancelRunnable = new Runnable() {
        public void run() {
            TwToolBoxFloatingViewer.this.onTouchUpOrCancel(3, TwToolBoxFloatingViewer.this.mAbsX, TwToolBoxFloatingViewer.this.mAbsY);
        }
    };
    private boolean mTracking = false;
    private ValueAnimator mTransparentAnimator;
    private ValueAnimator mTurnOffAnimator;
    private float mTurnOffEffectRatio = 0.0f;
    private TwToolBoxLongPressPanel mTwToolBoxLongPressPanel;
    private final Runnable mUpdateLongPressPanelRect = new Runnable() {
        public void run() {
            if (TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel != null) {
                TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.getWindowVisibleContentFrame(TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.mLongPressPanelRect);
            }
        }
    };
    private final Runnable mUpdateVisibility = new Runnable() {
        public void run() {
            try {
                TwToolBoxFloatingViewer.this.updateVisibility();
            } catch (Exception e) {
            }
        }
    };
    private UserManager mUserManager;
    private boolean mValidUser = false;
    private ViewConfiguration mViewConfiguration;
    private boolean mVisible = false;
    private boolean mVisibleFloatingStyle = false;
    public final LayoutParams mWindowAttributes;
    private WindowManager mWindowManager;
    private final PorterDuffXfermode mXfermode = new PorterDuffXfermode(Mode.DST_IN);
    private Method methodPauseGc = null;
    private Method methodResumeGc = null;

    public interface DelegateKeyguardShowing {
        boolean inKeyguardRestrictedKeyInputMode();

        boolean isKeyguardLocked();

        boolean isKeyguardSecure();

        boolean isKeyguardShowing();
    }

    public interface ToolBoxAction {
        void onAction();

        void onUpdateEnableStatus();
    }

    public interface ToolBoxToggleAction extends ToolBoxAction {
        void onUpdateToggleStatus();
    }

    public enum OperationType {
        APP_GENERIC,
        APP_MINI_MODE,
        TOGGLE
    }

    class ToolBoxMenu implements Comparable<ToolBoxMenu> {
        ToolBoxAction action;
        float angleDegree = 0.0f;
        ValueAnimator aniExpand;
        Drawable background;
        Drawable backgroundInverse;
        Rect bounds = new Rect();
        boolean isAnimating = false;
        boolean isEnabled = true;
        boolean isFunctionShortcut = false;
        boolean isPressed = false;
        boolean isRectangleShape = true;
        OperationType operationType = OperationType.APP_GENERIC;
        String packageName;
        int priority;
        BitmapDrawable shadow;
        Bitmap shadowBitmap;
        boolean toggleStatus;
        float x = 0.0f;
        float y = 0.0f;

        public ToolBoxMenu(final String packageName) {
            this.packageName = packageName;
            this.action = new ToolBoxAction(TwToolBoxFloatingViewer.this) {
                public void onAction() {
                    String[] data = packageName.split("/");
                    if (data != null) {
                        String pkgName = data.length > 0 ? data[0] : "00";
                        String activityName = data.length > 1 ? data[1] : "00";
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.addCategory("android.intent.category.LAUNCHER");
                        intent.setFlags(270532608);
                        if (intent != null) {
                            if ("00".equals(activityName)) {
                                intent.setPackage(pkgName);
                            } else {
                                try {
                                    intent.setComponent(new ComponentName(pkgName, activityName));
                                } catch (Exception e) {
                                }
                            }
                            TwToolBoxFloatingViewer.this.startActivityOrTask(intent);
                        }
                    }
                }

                public void onUpdateEnableStatus() {
                }
            };
        }

        public int compareTo(ToolBoxMenu o) {
            return Integer.valueOf(this.priority).compareTo(Integer.valueOf(o.priority));
        }
    }

    class ToolBoxCharacter extends ToolBoxMenu {
        Drawable backgroundOpen;
        int characterType = 0;

        public ToolBoxCharacter(String packageName) {
            super(packageName);
            this.backgroundOpen = TwToolBoxFloatingViewer.this.mContext.getDrawable(17304112);
        }
    }

    private enum TouchMode {
        NORMAL,
        DRAG,
        POSITIONING
    }

    private class TwToolBoxLongPressPanel extends FrameLayout {
        private Runnable mHideTask = new Runnable() {
            public void run() {
                if (TwToolBoxFloatingViewer.this.mLongPressPanelPosition != (-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX)) {
                    if (TwToolBoxFloatingViewer.this.mLongPressPanelAnimator.isRunning()) {
                        TwToolBoxFloatingViewer.this.mLongPressPanelAnimator.cancel();
                    }
                    TwToolBoxFloatingViewer.this.mLongPressPanelAnimator.setIntValues(new int[]{-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX, TwToolBoxFloatingViewer.this.mLongPressPanelPosition});
                    TwToolBoxFloatingViewer.this.mLongPressPanelAnimator.setDuration((long) ((int) (300.0f * (1.0f - Math.abs((float) (TwToolBoxFloatingViewer.this.mLongPressPanelPosition / TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX))))));
                    TwToolBoxFloatingViewer.this.mLongPressPanelAnimator.reverse();
                }
            }
        };
        final LayoutParams mLongPressPanelAttributes;
        final Rect mLongPressPanelRect = new Rect();

        public TwToolBoxLongPressPanel(Context context) {
            super(context);
            this.mLongPressPanelAttributes = new LayoutParams(-1, TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX, 0, 0, TwToolBoxFloatingViewer.this.mWindowAttributes.type, TwToolBoxFloatingViewer.this.mWindowAttributes.flags, TwToolBoxFloatingViewer.this.mWindowAttributes.format);
            this.mLongPressPanelAttributes.privateFlags = TwToolBoxFloatingViewer.this.mWindowAttributes.privateFlags;
            this.mLongPressPanelAttributes.gravity = TwToolBoxFloatingViewer.this.mWindowAttributes.gravity;
            this.mLongPressPanelAttributes.inputFeatures = TwToolBoxFloatingViewer.this.mWindowAttributes.inputFeatures;
            this.mLongPressPanelAttributes.setTitle("TwToolBoxLongPressPanel");
            setVisibility(8);
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int screenWidth = TwToolBoxFloatingViewer.this.mDisplayMetrics.widthPixels;
            TwToolBoxFloatingViewer.this.mLongPressPanelPosition = -TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX;
            TwToolBoxFloatingViewer.this.mEndFloatingBgArea.set(0, 0, screenWidth, TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX);
            int halfWidth = screenWidth / 2;
            int editBtnWidth = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_SIZE;
            int editBtnHeight = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_SIZE;
            int editBgWidth = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_BG_WIDTH;
            int editBgHeight = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_BG_HEIGHT;
            int removeBtnWidth = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_SIZE;
            int removeBtnHeight = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_SIZE;
            int removeBgWidth = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_BG_WIDTH;
            int removeBgHeight = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_BUTTON_BG_HEIGHT;
            if (TwToolBoxFloatingViewer.this.mIsTablet) {
                int leftCenterX = halfWidth - (editBgWidth / 2);
                int rightCenterX = halfWidth + (removeBgWidth / 2);
                int topEmptySpace = ((TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX - TwToolBoxFloatingViewer.this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX) - editBtnHeight) / 2;
                TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.set((leftCenterX - editBtnWidth) - 1, topEmptySpace, leftCenterX - 1, topEmptySpace + editBtnHeight);
                TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.set((rightCenterX - removeBtnWidth) - 1, topEmptySpace, rightCenterX - 1, topEmptySpace + removeBtnHeight);
                TwToolBoxFloatingViewer.this.mEndFloatingEditBgArea.set(leftCenterX - (editBgWidth / 2), (TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX - TwToolBoxFloatingViewer.this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX) - editBgHeight, (editBgWidth / 2) + leftCenterX, TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX - TwToolBoxFloatingViewer.this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX);
                TwToolBoxFloatingViewer.this.mEndFloatingRemoveBgArea.set(rightCenterX - (removeBgWidth / 2), (TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX - TwToolBoxFloatingViewer.this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX) - removeBgHeight, (removeBgWidth / 2) + rightCenterX, TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX - TwToolBoxFloatingViewer.this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX);
                TwToolBoxFloatingViewer.this.mEndFloatingEditConflictArea.set(TwToolBoxFloatingViewer.this.mEndFloatingEditBgArea);
                TwToolBoxFloatingViewer.this.mEndFloatingRemoveConflictArea.set(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBgArea);
                return;
            }
            leftCenterX = halfWidth - (halfWidth / 2);
            rightCenterX = halfWidth + (halfWidth / 2);
            topEmptySpace = TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_TOP_EMPTY_SPACE_PX;
            TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.set(leftCenterX - (editBtnWidth / 2), topEmptySpace, (editBtnWidth / 2) + leftCenterX, topEmptySpace + editBtnHeight);
            TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.set(rightCenterX - (removeBtnWidth / 2), topEmptySpace, (removeBtnWidth / 2) + rightCenterX, topEmptySpace + removeBtnHeight);
            TwToolBoxFloatingViewer.this.mEndFloatingEditBgArea.set(TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.centerX() - (editBgWidth / 2), 0, TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.centerX() + (editBgWidth / 2), editBgHeight);
            TwToolBoxFloatingViewer.this.mEndFloatingRemoveBgArea.set(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.centerX() - (removeBgWidth / 2), 0, TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.centerX() + (removeBgWidth / 2), removeBgHeight);
            TwToolBoxFloatingViewer.this.mEndFloatingEditConflictArea.set(TwToolBoxFloatingViewer.this.mEndFloatingEditBgArea);
            TwToolBoxFloatingViewer.this.mEndFloatingRemoveConflictArea.set(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBgArea);
        }

        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            int longPressPanelPosition = TwToolBoxFloatingViewer.this.mLongPressPanelPosition;
            if (TwToolBoxFloatingViewer.this.mTouchMode == TouchMode.POSITIONING || longPressPanelPosition != (-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX)) {
                if (longPressPanelPosition < 0) {
                    canvas.translate(0.0f, (float) longPressPanelPosition);
                }
                Rect r = TwToolBoxFloatingViewer.this.mMainCharacter.bounds;
                int absoluteMainLeft = r.left + TwToolBoxFloatingViewer.this.mWindowAttributes.x;
                int absoluteMainTop = (r.top + TwToolBoxFloatingViewer.this.mWindowAttributes.y) - this.mLongPressPanelRect.top;
                int absoluteMainRight = r.right + TwToolBoxFloatingViewer.this.mWindowAttributes.x;
                int absoluteMainBottom = (r.bottom + TwToolBoxFloatingViewer.this.mWindowAttributes.y) - this.mLongPressPanelRect.top;
                TwToolBoxFloatingViewer.this.mEndFloatingBg.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingBgArea);
                TwToolBoxFloatingViewer.this.mEndFloatingBg.draw(canvas);
                Drawable editButton = TwToolBoxFloatingViewer.this.mEndFloatingEditBtn;
                Drawable removeButton = TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtn;
                boolean removeNeedAnimating = false;
                if (TwToolBoxFloatingViewer.this.mEndFloatingEditConflictArea.intersects(absoluteMainLeft, absoluteMainTop, absoluteMainRight, absoluteMainBottom)) {
                    TwToolBoxFloatingViewer.this.mEndFloatingEditBg.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingEditBgArea);
                    TwToolBoxFloatingViewer.this.mEndFloatingEditBg.draw(canvas);
                    editButton = TwToolBoxFloatingViewer.this.mEndFloatingEditBtnFocused;
                } else if (TwToolBoxFloatingViewer.this.mEndFloatingRemoveConflictArea.intersects(absoluteMainLeft, absoluteMainTop, absoluteMainRight, absoluteMainBottom)) {
                    TwToolBoxFloatingViewer.this.mEndFloatingRemoveBg.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBgArea);
                    TwToolBoxFloatingViewer.this.mEndFloatingRemoveBg.draw(canvas);
                    removeButton = TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnFocused;
                    removeNeedAnimating = true;
                }
                editButton.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea);
                editButton.draw(canvas);
                removeButton.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea);
                removeButton.draw(canvas);
                if (TwToolBoxFloatingViewer.this.mIsTablet) {
                    int lenEditMsg = (int) TwToolBoxFloatingViewer.this.mTextPaint.measureText(TwToolBoxFloatingViewer.this.mEndFloatingMsgEdit);
                    int lenRemoveMsg = (int) TwToolBoxFloatingViewer.this.mTextPaint.measureText(TwToolBoxFloatingViewer.this.mEndFloatingMsgRemove);
                    canvas.drawText(TwToolBoxFloatingViewer.this.mEndFloatingMsgEdit, (float) ((TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.right + 1) + (lenEditMsg / 2)), (float) (TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.bottom - (TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.height() / 3)), TwToolBoxFloatingViewer.this.mTextPaint);
                    canvas.drawText(TwToolBoxFloatingViewer.this.mEndFloatingMsgRemove, (float) ((TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.right + 1) + (lenRemoveMsg / 2)), (float) (TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.bottom - (TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.height() / 3)), TwToolBoxFloatingViewer.this.mTextPaint);
                } else {
                    TwToolBoxFloatingViewer.this.mTextPaint.getTextBounds(TwToolBoxFloatingViewer.this.mEndFloatingMsgEdit, 0, TwToolBoxFloatingViewer.this.mEndFloatingMsgEdit.length() - 1, TwToolBoxFloatingViewer.this.mTextBounds);
                    canvas.drawText(TwToolBoxFloatingViewer.this.mEndFloatingMsgEdit, (float) TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.centerX(), (float) (TwToolBoxFloatingViewer.this.mEndFloatingEditBtnArea.bottom + TwToolBoxFloatingViewer.this.mTextBounds.height()), TwToolBoxFloatingViewer.this.mTextPaint);
                    TwToolBoxFloatingViewer.this.mTextPaint.getTextBounds(TwToolBoxFloatingViewer.this.mEndFloatingMsgRemove, 0, TwToolBoxFloatingViewer.this.mEndFloatingMsgRemove.length() - 1, TwToolBoxFloatingViewer.this.mTextBounds);
                    canvas.drawText(TwToolBoxFloatingViewer.this.mEndFloatingMsgRemove, (float) TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.centerX(), (float) (TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea.bottom + TwToolBoxFloatingViewer.this.mTextBounds.height()), TwToolBoxFloatingViewer.this.mTextPaint);
                }
                if (removeNeedAnimating) {
                    if (!TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating) {
                        TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnPosition = 0;
                        TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnAngle = 0.0f;
                        TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating = true;
                        TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnAnimator.start();
                    }
                    int removeButtonPosition = TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnPosition;
                    float removeButtonAngle = TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnAngle;
                    TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnCoverArea.set(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnArea);
                    TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnFocusedCover.setBounds(TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnCoverArea);
                    float rotatePivotX = (float) TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnCoverArea.centerX();
                    float rotatePivotY = (float) TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnCoverArea.centerY();
                    if (removeButtonPosition > 0) {
                        canvas.translate(0.0f, (float) (-removeButtonPosition));
                        canvas.rotate(removeButtonAngle, rotatePivotX, rotatePivotY);
                    }
                    TwToolBoxFloatingViewer.this.mEndFloatingRemoveBtnFocusedCover.draw(canvas);
                    if (removeButtonPosition > 0) {
                        canvas.rotate(-removeButtonAngle, rotatePivotX, rotatePivotY);
                        canvas.translate(0.0f, (float) removeButtonPosition);
                    }
                } else if (TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating) {
                    TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnAnimator.cancel();
                }
                if (longPressPanelPosition < 0) {
                    canvas.translate(0.0f, (float) (-longPressPanelPosition));
                }
            }
        }

        void hide() {
            post(this.mHideTask);
        }
    }

    static /* synthetic */ int access$5212(TwToolBoxFloatingViewer x0, int x1) {
        int i = x0.mCenterX + x1;
        x0.mCenterX = i;
        return i;
    }

    static /* synthetic */ int access$5412(TwToolBoxFloatingViewer x0, int x1) {
        int i = x0.mCenterY + x1;
        x0.mCenterY = i;
        return i;
    }

    public TwToolBoxFloatingViewer(Context context) {
        boolean z;
        boolean z2 = true;
        super(context);
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mResources = context.getResources();
        this.mDisplayMetrics = this.mResources.getDisplayMetrics();
        this.mPackageManager = context.getPackageManager();
        this.mViewConfiguration = ViewConfiguration.get(context);
        this.mCoverManager = new CoverManager(context);
        this.mStatusBarManager = (StatusBarManager) context.getSystemService("statusbar");
        this.mActivityManager = (ActivityManager) context.getSystemService("activity");
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mPersonaManager = (PersonaManager) context.getSystemService("persona");
        this.mManager = new TwToolBoxManager(context);
        this.mGlobalRect.set(100, 100, 101, 101);
        this.mDensity = this.mDisplayMetrics.density;
        if (this.mResources.getConfiguration().smallestScreenWidthDp >= 600) {
            z = true;
        } else {
            z = false;
        }
        this.mIsTablet = z;
        this.FIXED_MAIN_ITEM_WIDTH = this.mResources.getDimensionPixelSize(17105389);
        this.FIXED_MAIN_ITEM_HEIGHT = this.mResources.getDimensionPixelSize(17105389);
        this.FIXED_MAIN_ITEM_GAP = this.mResources.getDimensionPixelSize(17105392);
        this.FIXED_SHORTCUT_ITEM_SIZE = this.mResources.getDimensionPixelSize(17105390);
        this.FIXED_SHORTCUT_ITEM_GAP = this.mResources.getDimensionPixelSize(17105391);
        this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX = this.mResources.getDimensionPixelSize(17105386);
        this.FIXED_ROUND_TAIL_SIZE = this.mResources.getDimensionPixelSize(17105393);
        this.FIXED_START_POSITION_TOP_PADDING = this.mResources.getDimensionPixelSize(17105394);
        this.FIXED_START_POSITION_RIGHT_PADDING = this.mResources.getDimensionPixelSize(17105395);
        this.LONG_PRESS_PANEL_HEIGHT_PX = this.mResources.getDimensionPixelSize(17105387);
        this.LONG_PRESS_PANEL_TOP_EMPTY_SPACE_PX = this.mResources.getDimensionPixelSize(17105388);
        this.LONG_PRESS_PANEL_BUTTON_SIZE = this.mResources.getDimensionPixelSize(17105398);
        this.LONG_PRESS_PANEL_BUTTON_BG_WIDTH = this.mResources.getDimensionPixelSize(17105399);
        this.LONG_PRESS_PANEL_BUTTON_BG_HEIGHT = this.mResources.getDimensionPixelSize(17105400);
        this.SCALED_TOUCH_SLOP = this.mViewConfiguration.getScaledTouchSlop();
        this.SCALED_APPROACH_SLOP = this.mResources.getDimensionPixelSize(17105401);
        this.TOTAL_HEIGHT = (((this.FIXED_MAIN_ITEM_HEIGHT + this.FIXED_MAIN_ITEM_GAP) + this.FIXED_SHORTCUT_ITEM_SIZE) + ((this.FIXED_SHORTCUT_ITEM_GAP + this.FIXED_SHORTCUT_ITEM_SIZE) * 4)) + this.FIXED_ROUND_TAIL_SIZE;
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize((float) this.mResources.getDimensionPixelSize(17105397));
        this.mMaskPaint.setFilterBitmap(false);
        this.mTextPaint.setARGB(255, 255, 255, 255);
        if (this.mIsTablet) {
            int textShadowSize = this.mResources.getDimensionPixelSize(17105396);
            this.mTextPaint.setTypeface(this.mFont);
            this.mTextPaint.setShadowLayer((float) textShadowSize, (float) textShadowSize, (float) textShadowSize, -1073741824);
        }
        this.mFloatingBoxBg = context.getDrawable(17304110);
        this.mEndFloatingBg = context.getDrawable(17304121);
        this.mEndFloatingEditBg = context.getDrawable(17304107);
        this.mEndFloatingEditBtn = context.getDrawable(17304108);
        this.mEndFloatingEditBtnFocused = context.getDrawable(17304109);
        this.mEndFloatingRemoveBg = context.getDrawable(17304122);
        this.mEndFloatingRemoveBtn = context.getDrawable(17304123);
        this.mEndFloatingRemoveBtnFocused = context.getDrawable(17304124);
        this.mEndFloatingRemoveBtnFocusedCover = context.getDrawable(17304125);
        this.mEndFloatingMsgEdit = context.getString(17041193);
        this.mEndFloatingMsgRemove = context.getString(17040636);
        this.mDeivceProvisioned = Global.getInt(this.mContentResolver, "device_provisioned", 0) != 0;
        if (Secure.getIntForUser(this.mContentResolver, "touch_exploration_enabled", 0, -2) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mExploreByTouchMode = z;
        if (System.getIntForUser(this.mContentResolver, TwToolBoxService.SETTINGS_KIDS_MODE, 0, -2) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mKidsMode = z;
        if (System.getIntForUser(this.mContentResolver, TwToolBoxService.SETTINGS_CAR_MODE, 0, -2) == 0) {
            z2 = false;
        }
        this.mCarMode = z2;
        this.mValidUser = isValidUser();
        this.mKnoxRunning = isKnoxRunning();
        this.mWindowAttributes = new LayoutParams(this.FIXED_MAIN_ITEM_WIDTH, this.TOTAL_HEIGHT, 0, 0, 2015, 792, -3);
        LayoutParams layoutParams = this.mWindowAttributes;
        layoutParams.privateFlags |= 16;
        if (ActivityManager.isHighEndGfx()) {
            layoutParams = this.mWindowAttributes;
            layoutParams.flags |= 16777216;
            layoutParams = this.mWindowAttributes;
            layoutParams.privateFlags |= 2;
        }
        this.mWindowAttributes.gravity = 51;
        layoutParams = this.mWindowAttributes;
        layoutParams.inputFeatures |= 2;
        this.mWindowAttributes.setTitle("TwToolBoxFloatingViewer");
        this.mAndromedaWindowAttributes = new LayoutParams(0, 0);
        this.mAndromedaWindowAttributes.copyFrom(this.mWindowAttributes);
        layoutParams = this.mAndromedaWindowAttributes;
        this.mAndromedaWindowAttributes.y = -10000;
        layoutParams.x = -10000;
        this.mTwToolBoxLongPressPanel = new TwToolBoxLongPressPanel(context);
        updateVisibility();
        if (this.mContext != null) {
            this.mDVFSHelper = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER", 12, 0);
            this.mDVFSHelper.addExtraOptionsByDefaultPolicy("ListView_scroll");
            this.mDVFSHelperGPU = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER_GPU", 16, 0);
            this.mDVFSHelperGPU.addExtraOptionsByDefaultPolicy("ListView_scroll");
            this.mDVFSHelperBUS = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER_BUS", 19, 0);
            this.mDVFSHelperBUS.addExtraOptionsByDefaultPolicy("ListView_scroll");
            if (DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM != 0) {
                this.mDVFSHelperCore = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER_CORE", 14, 0);
                this.mDVFSHelperCore.addExtraOption("CORE_NUM", (long) DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM);
            }
        }
        Class clazz = VMRuntime.class;
        try {
            this.methodPauseGc = clazz.getDeclaredMethod("pauseGc", new Class[]{String.class});
            this.methodResumeGc = clazz.getDeclaredMethod("resumeGc", new Class[]{Integer.TYPE, Boolean.TYPE});
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e2) {
        }
        this.mWindowManager.addView(this.mTwToolBoxLongPressPanel, this.mTwToolBoxLongPressPanel.mLongPressPanelAttributes);
    }

    public void registerCallback() {
        boolean haveHardKeyboard;
        boolean z = true;
        this.mManager.registerCallback(this.mCallbcak);
        setInitVSync();
        if (!(this.mCoverManager == null || this.mCoverStateListener == null)) {
            this.mCoverManager.registerListener(this.mCoverStateListener);
        }
        if (this.mPersonaManager != null) {
            this.mPersonaManager.registerKnoxModeChangeObserver(new IKnoxModeChangeObserver.Stub() {
                public void onKnoxModeChange(int personaId) {
                    Log.i(TwToolBoxFloatingViewer.TAG, "onKnoxModeChange personaId:" + personaId);
                    TwToolBoxFloatingViewer.this.mPersonaId = personaId;
                    TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                    TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                }
            });
        }
        Configuration config = this.mResources.getConfiguration();
        if (config.keyboard != 1) {
            haveHardKeyboard = true;
        } else {
            haveHardKeyboard = false;
        }
        boolean hardKeyShown;
        if (!haveHardKeyboard || config.hardKeyboardHidden == 2) {
            hardKeyShown = false;
        } else {
            hardKeyShown = true;
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            if (!imm.isInputMethodShown() || hardKeyShown) {
                z = false;
            }
            this.mSipVisible = z;
        }
        IntentFilter sipIntentFilter = new IntentFilter();
        sipIntentFilter.addAction("ResponseAxT9Info");
        if (this.mSipReceiver == null) {
            this.mSipReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    TwToolBoxFloatingViewer.this.mSipVisible = intent.getBooleanExtra("AxT9IME.isVisibleWindow", true);
                    if (TwToolBoxFloatingViewer.this.mLongPressPanelPosition != (-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX) || TwToolBoxFloatingViewer.this.mLongPressPanelShowing) {
                        TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.hide();
                    }
                    String[] arr$ = TwToolBoxFloatingViewer.this.BLOCK_LIST;
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        if (!arr$[i$].equals(TwToolBoxFloatingViewer.this.mCurrentPackage)) {
                            i$++;
                        } else {
                            return;
                        }
                    }
                    if (TwToolBoxFloatingViewer.this.mSipVisible) {
                        TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mPlayAnimationFadeOutRunnable);
                        if (TwToolBoxFloatingViewer.this.mTracking) {
                            TwToolBoxFloatingViewer.this.onTouchUpOrCancel(3, TwToolBoxFloatingViewer.this.mAbsX, TwToolBoxFloatingViewer.this.mAbsY);
                            return;
                        }
                        return;
                    }
                    TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mPlayAnimationFadeInRunnable);
                }
            };
        }
        this.mContext.registerReceiver(this.mSipReceiver, sipIntentFilter);
        if (this.mScreenUnlockReceiver == null) {
            this.mScreenUnlockReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mCheckEnvironment, 300);
                }
            };
        }
        this.mContext.registerReceiver(this.mScreenUnlockReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));
        if (this.mPinnedWindowReceiver == null) {
            this.mPinnedWindowReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (TwToolBoxService.ACTION_LOCK_TASK_MODE.equals(intent.getAction())) {
                        TwToolBoxFloatingViewer.this.post(TwToolBoxFloatingViewer.this.mUpdateVisibility);
                    }
                }
            };
        }
        this.mContext.registerReceiver(this.mPinnedWindowReceiver, new IntentFilter(TwToolBoxService.ACTION_LOCK_TASK_MODE));
        prepareToolBoxLinear();
    }

    public void unregisterCallback() {
        this.mManager.unregisterCallback(this.mCallbcak);
        if (!(this.mCoverManager == null || this.mCoverStateListener == null)) {
            this.mCoverManager.unregisterListener(this.mCoverStateListener);
        }
        if (this.mSipReceiver != null) {
            this.mContext.unregisterReceiver(this.mSipReceiver);
        }
        this.mSipReceiver = null;
        if (this.mScreenUnlockReceiver != null) {
            this.mContext.unregisterReceiver(this.mScreenUnlockReceiver);
        }
        this.mScreenUnlockReceiver = null;
        if (this.mPinnedWindowReceiver != null) {
            this.mContext.unregisterReceiver(this.mPinnedWindowReceiver);
        }
        this.mPinnedWindowReceiver = null;
        clearToolBox();
    }

    public void reset() {
        this.mReseting = true;
        if (this.mExpanded) {
            removeCallbacks(this.mPlayAnimationCollapseRunnable);
            post(this.mPlayAnimationCollapseRunnable);
        }
        postDelayed(this.mReset, this.mExpanded ? 500 : 10);
    }

    private void updatePosition() {
        if (isAttachedToWindow()) {
            this.mWindowAttributes.x = this.mGlobalRect.left - this.mMainCharacterDecreaseAmount;
            this.mWindowAttributes.y = this.mGlobalRect.top - this.mMainCharacterDecreaseAmount;
            this.mWindowManager.updateViewLayout(this, this.mWindowAttributes);
        }
    }

    private boolean updateVisibility() {
        boolean keyguardShowing;
        boolean z;
        boolean z2 = true;
        boolean isDreaming = false;
        try {
            if (this.mDreamManager.isDreaming()) {
                isDreaming = true;
                Log.d(TAG, "Daydream isDreaming");
            }
        } catch (RemoteException e) {
        }
        if (this.mDelegateKeyguardShowing.isKeyguardShowing() || isDreaming || (this.mDelegateKeyguardShowing.isKeyguardSecure() && this.mDelegateKeyguardShowing.inKeyguardRestrictedKeyInputMode())) {
            keyguardShowing = true;
        } else {
            keyguardShowing = false;
        }
        if (keyguardShowing != this.mKeyguardShowing) {
            this.mKeyguardShowing = keyguardShowing;
        }
        boolean OTAShowing = isEnableOTA();
        if (OTAShowing != this.mOTAShowing) {
            this.mOTAShowing = OTAShowing;
        }
        if (TwToolBoxService.TOOLBOX_SUPPORT && System.getIntForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX, 0, -2) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mVisible = z;
        this.mValidUser = isValidUser();
        this.mKnoxRunning = isKnoxRunning();
        this.mCoverOpen = getSCoverState();
        this.mEnableFloatingViewer = isEnableFloatingViewer();
        this.mDisableFloatingViewer = isDisableFloatingViewer();
        if (!(this.mVisible && this.mEnableFloatingViewer && !isWindowPinned())) {
            z2 = false;
        }
        this.mVisibleFloatingStyle = z2;
        try {
            int visibility = this.mVisibleFloatingStyle ? 0 : 8;
            if (getVisibility() != visibility) {
                setVisibility(visibility);
            } else {
                invalidate();
            }
        } catch (Exception e2) {
        }
        return this.mVisibleFloatingStyle;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ContentResolver cr = this.mContentResolver;
        Handler handler = getHandler();
        this.mCurrentPackage = ((RunningAppProcessInfo) ((ActivityManager) this.mContext.getSystemService("activity")).getRunningAppProcesses().get(0)).processName;
        this.mPackageListObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                TwToolBoxFloatingViewer.this.reset();
            }
        };
        this.mDeviceProvisionedObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                boolean z = false;
                TwToolBoxFloatingViewer twToolBoxFloatingViewer = TwToolBoxFloatingViewer.this;
                if (Global.getInt(TwToolBoxFloatingViewer.this.mContentResolver, "device_provisioned", 0) != 0) {
                    z = true;
                }
                twToolBoxFloatingViewer.mDeivceProvisioned = z;
                TwToolBoxFloatingViewer.this.updateVisibility();
            }
        };
        this.mExploreByTouchModeObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                boolean z = false;
                TwToolBoxFloatingViewer twToolBoxFloatingViewer = TwToolBoxFloatingViewer.this;
                if (Secure.getIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, "touch_exploration_enabled", 0, -2) != 0) {
                    z = true;
                }
                twToolBoxFloatingViewer.mExploreByTouchMode = z;
                TwToolBoxFloatingViewer.this.updateVisibility();
            }
        };
        this.mKidsModeObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                boolean z = false;
                TwToolBoxFloatingViewer twToolBoxFloatingViewer = TwToolBoxFloatingViewer.this;
                if (System.getIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, TwToolBoxService.SETTINGS_KIDS_MODE, 0, -2) != 0) {
                    z = true;
                }
                twToolBoxFloatingViewer.mKidsMode = z;
                Log.d(TwToolBoxFloatingViewer.TAG, "KidsMode onChanged() " + TwToolBoxFloatingViewer.this.mKidsMode);
                TwToolBoxFloatingViewer.this.updateVisibility();
            }
        };
        this.mCarModeObserver = new ContentObserver(handler) {
            public void onChange(boolean selfChange) {
                boolean z = false;
                TwToolBoxFloatingViewer twToolBoxFloatingViewer = TwToolBoxFloatingViewer.this;
                if (System.getIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, TwToolBoxService.SETTINGS_CAR_MODE, 0, -2) != 0) {
                    z = true;
                }
                twToolBoxFloatingViewer.mCarMode = z;
                Log.d(TwToolBoxFloatingViewer.TAG, "CarMode onChanged() " + TwToolBoxFloatingViewer.this.mCarMode);
                TwToolBoxFloatingViewer.this.updateVisibility();
            }
        };
        cr.registerContentObserver(System.getUriFor(TwToolBoxService.SETTINGS_TOOLBOX_PACKAGE_LIST), false, this.mPackageListObserver, -1);
        cr.registerContentObserver(Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver);
        cr.registerContentObserver(Secure.getUriFor("touch_exploration_enabled"), false, this.mExploreByTouchModeObserver, -1);
        cr.registerContentObserver(System.getUriFor(TwToolBoxService.SETTINGS_KIDS_MODE), false, this.mKidsModeObserver, -1);
        cr.registerContentObserver(System.getUriFor(TwToolBoxService.SETTINGS_CAR_MODE), false, this.mCarModeObserver, -1);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.mContext.registerReceiver(this.mReceiver, filter);
        this.mCheckEnvironment.run();
        removeCallbacks(this.mUpdateVisibility);
        updateVisibility();
        if (this.mSipVisible) {
            post(this.mPlayAnimationFadeOutRunnable);
            return;
        }
        post(this.mPlayAnimationEnterRunnable);
        postDelayed(this.mPlayAnimationExpandRunnable, this.mEnterAnimator.getDuration());
        postDelayed(this.mPlayAnimationCollapseRunnable, this.mEnterAnimator.getDuration() + 700);
    }

    protected void onDetachedFromWindow() {
        this.mWindowManager.removeView(this.mTwToolBoxLongPressPanel);
        super.onDetachedFromWindow();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int orientation = this.mResources.getConfiguration().orientation;
        if (this.mLastOrientation == -1) {
            if (w > 0 && h > 0) {
                this.mLastOrientation = orientation;
                if (System.getFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO, 0.0f, -2) == 0.0f) {
                    saveDefaultCenterPosition();
                }
                loadCenterPosition();
            }
        } else if (this.mLastOrientation != orientation) {
            this.mLastOrientation = orientation;
            loadCenterPosition();
            playAnimationCollapse();
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mEndFloatingMsgEdit = this.mResources.getString(17041193);
        this.mEndFloatingMsgRemove = this.mResources.getString(17040636);
        if (this.mLastOrientation != newConfig.orientation) {
            this.mLastOrientation = newConfig.orientation;
            loadCenterPosition();
            playAnimationCollapse();
            postInvalidate();
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawToolBox(canvas, this.mCenterX, this.mCenterY, false);
    }

    private void drawToolBox(Canvas canvas, int centerX, int centerY, boolean isReCalculation) {
        int halfHeight = 0;
        boolean boxAnimating = false;
        this.mMainCharacterDecreaseAmount = 0;
        if (this.mExpandAnimator != null) {
            boxAnimating = false | this.mExpandAnimator.isRunning();
        }
        if (this.mTransparentAnimator != null) {
            boxAnimating |= this.mTransparentAnimator.isRunning();
        }
        Iterator i$ = this.mObjects.iterator();
        while (i$.hasNext()) {
            ToolBoxMenu m = (ToolBoxMenu) i$.next();
            Drawable background = (m.operationType != OperationType.TOGGLE || m.toggleStatus) ? m.background : m.backgroundInverse;
            int alpha = (int) (((float) (m.isEnabled ? 255 : 128)) * this.mExpandProgressValue);
            boxAnimating |= m.isAnimating;
            int halfWidth;
            if (m instanceof ToolBoxCharacter) {
                halfWidth = this.FIXED_MAIN_ITEM_WIDTH / 2;
                halfHeight = this.FIXED_MAIN_ITEM_HEIGHT / 2;
                m.bounds.set(centerX - halfWidth, centerY - halfHeight, centerX + halfWidth, centerY + halfHeight);
                if (this.mEnterProgressValue > 0.0f) {
                    this.mMainCharacterDecreaseAmount = (int) (((float) this.FIXED_MAIN_ITEM_WIDTH) * Math.abs(0.24f * (this.mEnterProgressValue - 0.5f)));
                } else if (this.mTurnOffEffectRatio > 0.0f) {
                    this.mMainCharacterDecreaseAmount = (int) (((float) this.FIXED_MAIN_ITEM_WIDTH) * this.mTurnOffEffectRatio);
                } else {
                    this.mMainCharacterDecreaseAmount = (int) (((((float) this.FIXED_MAIN_ITEM_WIDTH) * 0.24f) / 2.0f) * (1.0f - this.mExpandProgressValue));
                }
                m.bounds.inset(this.mMainCharacterDecreaseAmount, this.mMainCharacterDecreaseAmount);
                background.setBounds(m.bounds);
                this.mGlobalRect.set(m.bounds);
            } else if (this.mExpandProgressValue != 0.0f) {
                background.setAlpha(alpha);
                halfWidth = this.FIXED_SHORTCUT_ITEM_SIZE / 2;
                halfHeight = this.FIXED_SHORTCUT_ITEM_SIZE / 2;
                m.bounds.set(centerX - halfWidth, (int) ((((float) centerY) + m.y) - ((float) halfHeight)), centerX + halfWidth, (int) ((((float) centerY) + m.y) + ((float) halfHeight)));
                m.bounds.inset(this.mMainCharacterDecreaseAmount, this.mMainCharacterDecreaseAmount);
                background.setBounds(m.bounds);
                this.mGlobalRect.union(m.bounds);
            }
        }
        Rect rect = this.mGlobalRect;
        rect.bottom += (int) (((float) this.FIXED_ROUND_TAIL_SIZE) * this.mExpandProgressValue);
        rect = this.mGlobalRect;
        rect.bottom += (int) (((float) this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX) * (1.0f - this.mExpandProgressValue));
        this.mBoxAnimating = boxAnimating;
        if (!isReCalculation) {
            int w = this.mDisplayMetrics.widthPixels;
            int h = this.mDisplayMetrics.heightPixels;
            int offsetX = 0;
            int offsetY = 0;
            if (this.mGlobalRect.left < 0) {
                offsetX = -this.mGlobalRect.left;
            } else if (this.mGlobalRect.right > w) {
                offsetX = w - this.mGlobalRect.right;
            }
            if (this.mGlobalRect.top < 0) {
                offsetY = -this.mGlobalRect.top;
            } else if (this.mGlobalRect.bottom > h) {
                offsetY = h - this.mGlobalRect.bottom;
            }
            if (!(offsetX == 0 && offsetY == 0)) {
                drawToolBox(canvas, centerX + offsetX, centerY + offsetY, true);
                return;
            }
        }
        updatePosition();
        this.mDrawGlobalRect.set(this.mGlobalRect);
        int zeroOffsetX = (-this.mDrawGlobalRect.left) + this.mMainCharacterDecreaseAmount;
        int zeroOffsetY = (-this.mDrawGlobalRect.top) + this.mMainCharacterDecreaseAmount;
        this.mDrawGlobalRect.offsetTo(this.mMainCharacterDecreaseAmount, this.mMainCharacterDecreaseAmount);
        if (this.mMainCharacterDegree > 0 || this.mBoxAnimating) {
            this.mFloatingBoxBg.setBounds(this.mDrawGlobalRect);
            this.mFloatingBoxBg.setAlpha((int) (255.0f * this.mExpandProgressValue));
            this.mFloatingBoxBg.draw(canvas);
        }
        for (int idx = this.mObjects.size() - 1; idx >= 0; idx--) {
            m = (ToolBoxMenu) this.mObjects.get(idx);
            background = (m.operationType != OperationType.TOGGLE || m.toggleStatus) ? m.background : m.backgroundInverse;
            alpha = (int) (((float) (m.isEnabled ? 255 : 128)) * this.mExpandProgressValue);
            m.bounds.offset(zeroOffsetX, zeroOffsetY);
            if (m instanceof ToolBoxCharacter) {
                ToolBoxCharacter main = (ToolBoxCharacter) m;
                if (main.shadow != null) {
                    this.mShadowRect.set(main.bounds);
                    this.mShadowRect.offset(0, this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX);
                    main.shadow.setBounds(this.mShadowRect);
                    main.shadow.setAlpha((int) (((double) (255.0f * (1.0f - this.mExpandProgressValue))) * 0.2d));
                    main.shadow.draw(canvas);
                }
                Drawable mainNormalBg = main.background;
                int mainCenterX = main.bounds.centerX();
                int mainCenterY = main.bounds.centerY();
                mainNormalBg.setBounds(main.bounds);
                if (this.mMainCharacterDegree > 0) {
                    canvas.rotate((float) this.mMainCharacterDegree, (float) mainCenterX, (float) mainCenterY);
                }
                if (this.mMainCharacterDegree < 90) {
                    mainNormalBg.draw(canvas);
                }
                if (this.mMainCharacterDegree > 0) {
                    canvas.rotate((float) (-this.mMainCharacterDegree), (float) mainCenterX, (float) mainCenterY);
                }
                Drawable mainOpenBg = main.backgroundOpen;
                mainOpenBg.setBounds(main.bounds);
                if (this.mMainCharacterDegree > 0) {
                    canvas.rotate((float) (this.mMainCharacterDegree - 90), (float) mainCenterX, (float) mainCenterY);
                }
                mainOpenBg.setAlpha((int) ((((float) this.mMainCharacterDegree) / 90.0f) * 255.0f));
                mainOpenBg.draw(canvas);
                if (this.mMainCharacterDegree > 0) {
                    canvas.rotate((float) (-(this.mMainCharacterDegree - 90)), (float) mainCenterX, (float) mainCenterY);
                }
            } else if (alpha != 0 && m.y >= ((float) halfHeight)) {
                if (m.shadow != null) {
                    this.mShadowRect.set(m.bounds);
                    this.mShadowRect.offset(0, this.FIXED_SHORTCUT_SHADOW_HEIGHT_PX);
                    m.shadow.setBounds(this.mShadowRect);
                    m.shadow.setAlpha((int) (((double) alpha) * 0.2d));
                    m.shadow.draw(canvas);
                }
                background.setBounds(m.bounds);
                background.draw(canvas);
            }
        }
    }

    public void invalidate() {
        super.invalidate();
        if (this.mTouchMode == TouchMode.POSITIONING) {
            this.mTwToolBoxLongPressPanel.invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case 7:
            case 9:
            case 10:
                return false;
            default:
                int x = (int) event.getRawX();
                this.mAbsX = x;
                int y = (int) event.getRawY();
                this.mAbsY = y;
                if (action == 0) {
                    onTouchDown(x, y);
                }
                if (this.mTracking && !this.mReseting && !this.mMovingByAnimation && this.mVisibleFloatingStyle) {
                    if (action == 2) {
                        onTouchMove(x, y);
                    } else if (action == 1 || action == 3) {
                        onTouchUpOrCancel(action, x, y);
                    }
                    return true;
                } else if (action != 1 && action != 3) {
                    return false;
                } else {
                    removeCallbacks(this.mTouchLongClickRunnable);
                    if (this.mLongPressPanelPosition == (-this.LONG_PRESS_PANEL_HEIGHT_PX) && !this.mLongPressPanelShowing) {
                        return false;
                    }
                    onTouchUpOrCancel(3, this.mAbsX, this.mAbsY);
                    Log.e(TAG, "Recovered LongPressPanel");
                    return false;
                }
        }
    }

    private void onTouchDown(int x, int y) {
        removeCallbacks(this.mTouchLongClickRunnable);
        if (!this.mReseting && !this.mMovingByAnimation) {
            int relX = x - this.mWindowAttributes.x;
            int relY = y - this.mWindowAttributes.y;
            boolean conflict = !this.mHiddenToolBox && this.mEnableFloatingViewer && this.mGlobalRect.contains(x, y);
            boolean tracking = false;
            if (conflict) {
                if (this.mSipVisible && this.mVisibleFloatingStyle) {
                    post(this.mUpdateVisibility);
                    this.mTracking = false;
                    return;
                }
                Iterator i$ = this.mObjects.iterator();
                while (i$.hasNext()) {
                    ToolBoxMenu m = (ToolBoxMenu) i$.next();
                    Rect r = m.bounds;
                    if (!m.isRectangleShape) {
                        if (!isPointInCircle((double) r.centerX(), (double) r.centerY(), (double) (r.width() / 2), relX, relY)) {
                            continue;
                        }
                    } else if (!r.contains(relX, relY)) {
                        continue;
                    }
                    if (!m.isAnimating) {
                        tracking = true;
                        this.mTouchTarget = m;
                        break;
                    }
                }
            }
            this.mTracking = tracking;
            this.mNeedSaveCenterPosition = false;
            if (conflict) {
                removeCallbacks(this.mPlayAnimationSleepRunnable);
                postDelayed(this.mPlayAnimationSleepRunnable, 3000);
                removeCallbacks(this.mPlayAnimationWakeupRunnable);
                postDelayed(this.mPlayAnimationWakeupRunnable, 50);
            } else if (this.mExpanded) {
                removeCallbacks(this.mPlayAnimationCollapseRunnable);
                post(this.mPlayAnimationCollapseRunnable);
            }
            if (this.mTracking) {
                this.mLongPressed = false;
                this.mDownSlopArea.set(x - this.SCALED_TOUCH_SLOP, y - this.SCALED_TOUCH_SLOP, this.SCALED_TOUCH_SLOP + x, this.SCALED_TOUCH_SLOP + y);
                Runnable runnable = this.mTouchLongClickRunnable;
                ViewConfiguration viewConfiguration = this.mViewConfiguration;
                postDelayed(runnable, (long) ViewConfiguration.getLongPressTimeout());
                this.mDeltaX = relX - this.mMainCharacter.bounds.centerX();
                this.mDeltaY = relY - this.mMainCharacter.bounds.centerY();
            }
        }
    }

    private void onTouchMove(int x, int y) {
        if (this.mTouchMode == TouchMode.DRAG || this.mTouchMode == TouchMode.POSITIONING) {
            this.mTargetX = x - this.mDeltaX;
            this.mTargetY = y - this.mDeltaY;
        } else if (!this.mDownSlopArea.contains(x, y)) {
            removeCallbacks(this.mTouchLongClickRunnable);
            if (this.mTouchTarget == this.mMainCharacter) {
                this.mTouchMode = TouchMode.DRAG;
                goToPosition(x - this.mDeltaX, y - this.mDeltaY);
                if (this.mExpanded) {
                    removeCallbacks(this.mPlayAnimationCollapseRunnable);
                    post(this.mPlayAnimationCollapseRunnable);
                }
            }
        }
    }

    private void onTouchUpOrCancel(int action, int x, int y) {
        TouchMode lastTouchMode = this.mTouchMode;
        removeCallbacks(this.mTouchLongClickRunnable);
        removeCallbacks(this.mTouchUpOrCancelRunnable);
        if (action == 1 && !this.mLongPressed && this.mTouchMode != TouchMode.DRAG && this.mDownSlopArea.contains(x, y)) {
            post(this.mTouchClickRunnable);
        }
        Rect r = this.mMainCharacter.bounds;
        if (this.mTouchMode == TouchMode.POSITIONING) {
            int absoluteMainLeft = r.left + this.mWindowAttributes.x;
            int absoluteMainTop = (r.top + this.mWindowAttributes.y) - this.mTwToolBoxLongPressPanel.mLongPressPanelRect.top;
            int absoluteMainRight = r.right + this.mWindowAttributes.x;
            int absoluteMainBottom = (r.bottom + this.mWindowAttributes.y) - this.mTwToolBoxLongPressPanel.mLongPressPanelRect.top;
            if (action != 3) {
                if (this.mEndFloatingEditConflictArea.intersects(absoluteMainLeft, absoluteMainTop, absoluteMainRight, absoluteMainBottom)) {
                    Intent intent = new Intent(TOOLBOX_EDIT_INTENT);
                    intent.addFlags(268468224);
                    this.mContext.startActivityAsUser(intent, new UserHandle(-2));
                    this.mStatusBarManager.collapsePanels();
                    goToPositionOneShot((int) (System.getFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO, 0.0f, -2) * ((float) this.mDisplayMetrics.widthPixels)), (int) (System.getFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_Y_RATIO, 0.0f, -2) * ((float) this.mDisplayMetrics.heightPixels)));
                } else if (this.mEndFloatingRemoveConflictArea.intersects(absoluteMainLeft, absoluteMainTop, absoluteMainRight, absoluteMainBottom)) {
                    this.mTurnOffAnimator.start();
                } else {
                    saveCenterPosition();
                    if (this.mSipVisible && !this.mHiddenToolBox) {
                        boolean needBlock = false;
                        for (String blockPkg : this.BLOCK_LIST) {
                            if (blockPkg.equals(this.mCurrentPackage)) {
                                needBlock = true;
                                break;
                            }
                        }
                        if (needBlock) {
                            post(this.mPlayAnimationFadeOutRunnable);
                        }
                    }
                }
            }
        } else {
            if (this.mTouchMode == TouchMode.DRAG) {
                this.mNeedSaveCenterPosition = true;
            }
        }
        if (this.mLongPressPanelPosition != (-this.LONG_PRESS_PANEL_HEIGHT_PX) || this.mLongPressPanelShowing) {
            this.mTwToolBoxLongPressPanel.hide();
        }
        this.mTouchMode = TouchMode.NORMAL;
        this.mLongPressed = false;
        if (lastTouchMode != this.mTouchMode) {
            postInvalidate();
        }
        if (getAlpha() == 1.0f) {
            removeCallbacks(this.mPlayAnimationSleepRunnable);
            postDelayed(this.mPlayAnimationSleepRunnable, 3000);
        }
        this.mTracking = false;
    }

    private void onToolLongClick() {
        if (this.mTouchTarget != null) {
            this.mLongPressed = true;
            Log.d(TAG, "LongClick() " + this.mTouchTarget.priority);
            if (this.mTouchTarget == this.mMainCharacter) {
                performHapticFeedback(0);
                playSoundEffect(0);
                this.mTouchMode = TouchMode.POSITIONING;
                goToPosition(this.mAbsX - this.mDeltaX, this.mAbsY - this.mDeltaY);
                this.mLongPressPanelAnimator.setIntValues(new int[]{-this.LONG_PRESS_PANEL_HEIGHT_PX, 0});
                this.mLongPressPanelAnimator.setDuration(300);
                this.mLongPressPanelAnimator.start();
                if (this.mExpanded) {
                    removeCallbacks(this.mPlayAnimationCollapseRunnable);
                    post(this.mPlayAnimationCollapseRunnable);
                    return;
                }
                playAnimationEnter();
            }
        }
    }

    private void onToolClick() {
        if (this.mTouchTarget != null && !this.mBoxAnimating) {
            if (this.mTouchTarget == this.mMainCharacter) {
                performHapticFeedback(50009);
                playSoundEffect(0);
                if (this.mExpanded) {
                    removeCallbacks(this.mPlayAnimationCollapseRunnable);
                    post(this.mPlayAnimationCollapseRunnable);
                } else {
                    int allPkgCount;
                    if (this.mAllPackages == null) {
                        allPkgCount = 0;
                    } else {
                        allPkgCount = this.mAllPackages.length;
                    }
                    if (allPkgCount == this.mObjects.size()) {
                        Iterator i$ = this.mObjects.iterator();
                        while (i$.hasNext()) {
                            ToolBoxMenu m = (ToolBoxMenu) i$.next();
                            if (m != this.mMainCharacter && !m.isFunctionShortcut && !isEnabledPkg(m.packageName.split("/")[0])) {
                                this.mReset.run();
                                break;
                            }
                        }
                    } else {
                        this.mReset.run();
                    }
                    removeCallbacks(this.mPlayAnimationExpandRunnable);
                    post(this.mPlayAnimationExpandRunnable);
                }
            } else if (this.mExpanded && this.mTouchTarget.isEnabled) {
                this.mTouchTarget.action.onAction();
                performHapticFeedback(50009);
                playSoundEffect(0);
                removeCallbacks(this.mPlayAnimationCollapseRunnable);
                post(this.mPlayAnimationCollapseRunnable);
                this.mStatusBarManager.collapsePanels();
            }
            Log.d(TAG, "Click() " + this.mTouchTarget.priority);
        }
    }

    private void goToPosition(int x, int y) {
        this.mTargetX = x;
        this.mTargetY = y;
        setEnableVSync(true);
    }

    private void goToPositionOneShot(int x, int y) {
        int oldCenterX = this.mCenterX;
        int oldCenterY = this.mCenterY;
        setEnableVSync(false);
        if (x != oldCenterX || y != oldCenterY) {
            if (this.mPositionAnimatorX == null) {
                this.mPositionAnimatorX = ValueAnimator.ofInt(new int[]{oldCenterX, x});
                this.mPositionAnimatorX.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            TwToolBoxFloatingViewer.this.mCenterX = ((Integer) animation.getAnimatedValue()).intValue();
                            TwToolBoxFloatingViewer.this.invalidate();
                        } catch (Exception e) {
                        }
                    }
                });
                this.mPositionAnimatorY = ValueAnimator.ofInt(new int[]{oldCenterY, y});
                this.mPositionAnimatorY.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            TwToolBoxFloatingViewer.this.mCenterY = ((Integer) animation.getAnimatedValue()).intValue();
                            TwToolBoxFloatingViewer.this.invalidate();
                        } catch (Exception e) {
                        }
                    }
                });
                this.mPositionAnimatorY.addListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                        TwToolBoxFloatingViewer.this.mMovingByAnimation = true;
                    }

                    public void onAnimationEnd(Animator animation) {
                        TwToolBoxFloatingViewer.this.mMovingByAnimation = false;
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }

                    public void onAnimationCancel(Animator animation) {
                    }
                });
            } else {
                this.mPositionAnimatorX.setIntValues(new int[]{oldCenterX, x});
                this.mPositionAnimatorY.setIntValues(new int[]{oldCenterY, y});
            }
            this.mPositionAnimatorX.start();
            this.mPositionAnimatorY.start();
        }
    }

    private void goToPositionAndromeda() {
        if (isAttachedToWindow()) {
            this.mWindowManager.updateViewLayout(this, this.mAndromedaWindowAttributes);
        }
    }

    private void acquireDVFS() {
        releaseDVFS();
        if (!this.mDVFSLockAcquired) {
            if (this.mDVFSHelper != null) {
                this.mDVFSHelper.acquire();
            }
            if (this.mDVFSHelperGPU != null) {
                this.mDVFSHelperGPU.acquire();
            }
            if (this.mDVFSHelperBUS != null) {
                this.mDVFSHelperBUS.acquire();
            }
            if (this.mDVFSHelperCore != null) {
                this.mDVFSHelperCore.acquire();
            }
            this.mDVFSLockAcquired = true;
        }
    }

    private void releaseDVFS() {
        if (this.mDVFSLockAcquired) {
            if (!(this.mDVFSCookie == 0 || this.methodResumeGc == null)) {
                try {
                    this.methodResumeGc.invoke(VMRuntime.getRuntime(), new Object[]{Integer.valueOf(this.mDVFSCookie), Boolean.valueOf(false)});
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e2) {
                }
                this.mDVFSCookie = 0;
            }
            if (this.mDVFSHelper != null) {
                this.mDVFSHelper.release();
            }
            if (this.mDVFSHelperCore != null) {
                this.mDVFSHelperCore.release();
            }
            if (this.mDVFSHelperGPU != null) {
                this.mDVFSHelperGPU.release();
            }
            if (this.mDVFSHelperBUS != null) {
                this.mDVFSHelperBUS.release();
            }
            this.mDVFSLockAcquired = false;
        }
    }

    private void pauseGC() {
        resumeGC();
        if (this.methodPauseGc != null) {
            try {
                this.mDVFSCookie = ((Integer) this.methodPauseGc.invoke(VMRuntime.getRuntime(), new Object[]{"AbsListScroll"})).intValue();
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
        }
    }

    private void resumeGC() {
        if (this.mDVFSCookie != 0 && this.methodResumeGc != null) {
            try {
                this.methodResumeGc.invoke(VMRuntime.getRuntime(), new Object[]{Integer.valueOf(this.mDVFSCookie), Boolean.valueOf(false)});
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
            this.mDVFSCookie = 0;
        }
    }

    private void setInitVSync() {
        this.mInVSync = false;
        initVSync();
    }

    private void setEnableVSync(boolean status) {
        if (status) {
            pauseGC();
            acquireDVFS();
            updateVSync();
            return;
        }
        stopVSync();
        releaseDVFS();
        resumeGC();
    }

    private void initVSync() {
        if (this.mChoreographer == null) {
            this.mChoreographer = Choreographer.getInstance();
        }
    }

    private void updateVSync() {
        if (!this.mInVSync) {
            this.mInVSync = true;
            initVSync();
            this.mChoreographer.postCallback(1, this.mGoToPositionCallback, null);
        }
    }

    private void stopVSync() {
        this.mInVSync = false;
        if (this.mChoreographer != null) {
            this.mChoreographer.removeCallbacks(1, this.mGoToPositionCallback, null);
            this.mChoreographer = null;
        }
    }

    private boolean isPointInCircle(double centerX, double centerY, double radius, int x, int y) {
        if (((double) x) < centerX - radius || ((double) x) > centerX + radius || ((double) y) < centerY - radius || ((double) y) > centerY + radius) {
            return false;
        }
        double dx = centerX - ((double) x);
        double dy = centerY - ((double) y);
        return (dx * dx) + (dy * dy) <= radius * radius;
    }

    private void prepareToolBoxLinear() {
        Log.d(TAG, "prepareToolBoxLinear()");
        SineInOut80 interpolator = new SineInOut80();
        this.mObjects.clear();
        String[] tools = this.mManager.getToolList();
        ArrayList<String> toolArrayList = new ArrayList();
        toolArrayList.add("0");
        for (String t : tools) {
            toolArrayList.add(t);
        }
        this.mAllPackages = toolArrayList.toArray();
        int priority = 0;
        for (String pkg : this.mAllPackages) {
            String[] data = pkg.split("/");
            String pkgName = data[0];
            String activityName = data.length < 2 ? "00" : data[1];
            boolean isMainCharactor = "0".equals(pkgName);
            ToolBoxMenu toolBoxCharacter;
            if (isMainCharactor) {
                toolBoxCharacter = new ToolBoxCharacter(pkg);
                this.mMainCharacter = toolBoxCharacter;
            } else {
                toolBoxCharacter = new ToolBoxMenu(pkg);
            }
            m.priority = priority;
            final ToolBoxMenu toolBoxMenu = m;
            AnimatorListener animatorListener = new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    toolBoxMenu.isAnimating = true;
                }

                public void onAnimationEnd(Animator animation) {
                    toolBoxMenu.isAnimating = false;
                    TwToolBoxFloatingViewer.this.invalidate();
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationCancel(Animator animation) {
                }
            };
            if (isMainCharactor) {
                m.background = this.mContext.getDrawable(17304111);
                m.aniExpand = ValueAnimator.ofInt(new int[]{0, 90});
                m.aniExpand.setInterpolator(interpolator);
                m.aniExpand.addListener(animatorListener);
                m.aniExpand.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            TwToolBoxFloatingViewer.this.mMainCharacterDegree = ((Integer) animation.getAnimatedValue()).intValue();
                            TwToolBoxFloatingViewer.this.invalidate();
                        } catch (Exception e) {
                        }
                    }
                });
            } else {
                try {
                    PackageManager pm = this.mPackageManager;
                    if ("00".equals(activityName)) {
                        m.background = pm.getApplicationIcon(pkgName);
                    } else {
                        m.background = pm.getActivityIcon(new ComponentName(pkgName, activityName));
                    }
                    if (!isEnabledPkg(pkgName)) {
                    }
                } catch (NameNotFoundException e) {
                    Log.e(TAG, "There is no package : " + e);
                    if (activityName.toLowerCase().contains("index")) {
                        try {
                            if (!initializeFunctionShortcut(m, Integer.parseInt(activityName.replace("index", "")))) {
                            }
                        } catch (NumberFormatException e2) {
                        }
                    }
                } catch (Exception e3) {
                }
                toolBoxMenu = m;
                AnimatorUpdateListener animatorUpdateListener = new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            Integer value = (Integer) animation.getAnimatedValue();
                            toolBoxMenu.y = (float) value.intValue();
                            TwToolBoxFloatingViewer.this.invalidate();
                        } catch (Exception e) {
                        }
                    }
                };
                int endY = (((this.FIXED_MAIN_ITEM_HEIGHT / 2) + this.FIXED_MAIN_ITEM_GAP) + (this.FIXED_SHORTCUT_ITEM_SIZE / 2)) + ((this.FIXED_SHORTCUT_ITEM_GAP + this.FIXED_SHORTCUT_ITEM_SIZE) * (priority - 1));
                m.aniExpand = ValueAnimator.ofInt(new int[]{0, endY});
                m.aniExpand.setInterpolator(interpolator);
                m.aniExpand.addListener(animatorListener);
                m.aniExpand.addUpdateListener(animatorUpdateListener);
                m.aniExpand.setStartDelay((long) ((this.mAllPackages.length - priority) * 33));
            }
            if (m.background instanceof BitmapDrawable) {
                Bitmap bitmapMask;
                Bitmap bitmap = ((BitmapDrawable) m.background).getBitmap();
                if (isMainCharactor) {
                    bitmapMask = Bitmap.createScaledBitmap(bitmap, this.FIXED_MAIN_ITEM_WIDTH, this.FIXED_MAIN_ITEM_WIDTH, true);
                } else {
                    bitmapMask = Bitmap.createScaledBitmap(bitmap, this.FIXED_SHORTCUT_ITEM_SIZE, this.FIXED_SHORTCUT_ITEM_SIZE, true);
                }
                m.shadowBitmap = Bitmap.createBitmap(bitmapMask.getWidth(), bitmapMask.getHeight(), Config.ARGB_8888);
                this.mMaskCanvas.setBitmap(m.shadowBitmap);
                this.mMaskPaint.setXfermode(null);
                this.mMaskCanvas.drawRect(0.0f, 0.0f, (float) bitmapMask.getWidth(), (float) bitmapMask.getHeight(), this.mMaskPaint);
                this.mMaskPaint.setXfermode(this.mXfermode);
                this.mMaskCanvas.drawBitmap(bitmapMask, 0.0f, 0.0f, this.mMaskPaint);
                this.mMaskPaint.setXfermode(null);
                m.shadow = new BitmapDrawable(this.mResources, m.shadowBitmap);
            }
            this.mObjects.add(m);
            priority++;
        }
        this.mExpandAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mExpandAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mExpandProgressValue = ((Float) animation.getAnimatedValue()).floatValue();
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mExpandAnimator.setDuration(400);
        this.mTransparentAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mTransparentAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                try {
                    TwToolBoxFloatingViewer.this.updateVisibility();
                } catch (Exception e) {
                }
            }

            public void onAnimationEnd(Animator animation) {
                try {
                    if (TwToolBoxFloatingViewer.this.mSipVisible) {
                        TwToolBoxFloatingViewer.this.setVisibility(8);
                    } else {
                        TwToolBoxFloatingViewer.this.updateVisibility();
                    }
                } catch (Exception e) {
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mTransparentAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mSleepAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.4f});
        this.mSleepAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mSleepAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mEnterAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mEnterAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                TwToolBoxFloatingViewer.this.mEnterProgressValue = 0.0f;
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mEnterAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mEnterProgressValue = ((Float) animation.getAnimatedValue()).floatValue();
                    TwToolBoxFloatingViewer.this.setAlpha(TwToolBoxFloatingViewer.this.mEnterProgressValue);
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mEnterAnimator.setDuration(300);
        this.mLongPressPanelAnimator = ValueAnimator.ofInt(new int[]{-this.LONG_PRESS_PANEL_HEIGHT_PX, 0});
        this.mLongPressPanelAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (TwToolBoxFloatingViewer.this.mLongPressPanelPosition == (-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX)) {
                    TwToolBoxFloatingViewer.this.mLongPressPanelShowing = true;
                }
                try {
                    TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.setVisibility(0);
                } catch (Exception e) {
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (TwToolBoxFloatingViewer.this.mLongPressPanelPosition == (-TwToolBoxFloatingViewer.this.LONG_PRESS_PANEL_HEIGHT_PX)) {
                    TwToolBoxFloatingViewer.this.mLongPressPanelShowing = false;
                }
                try {
                    TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.getWindowVisibleContentFrame(TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.mLongPressPanelRect);
                    TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateLongPressPanelRect, 3000);
                    if (!TwToolBoxFloatingViewer.this.mLongPressPanelShowing) {
                        TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.setVisibility(8);
                    }
                } catch (Exception e) {
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mLongPressPanelAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mLongPressPanelPosition = ((Integer) animation.getAnimatedValue()).intValue();
                    TwToolBoxFloatingViewer.this.mTwToolBoxLongPressPanel.invalidate();
                } catch (Exception e) {
                }
            }
        });
        int removeButtonMoveAmount = (int) (2.0f * this.mDensity);
        ValueAnimator upAnimator = ValueAnimator.ofInt(new int[]{0, removeButtonMoveAmount});
        upAnimator.setDuration(150);
        upAnimator.setStartDelay(60);
        upAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
                TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating = false;
            }
        });
        upAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnPosition = ((Integer) animation.getAnimatedValue()).intValue();
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        ValueAnimator downAnimator = ValueAnimator.ofInt(new int[]{removeButtonMoveAmount, 0});
        downAnimator.setDuration(150);
        downAnimator.setStartDelay(60);
        downAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating = false;
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
                TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating = false;
            }
        });
        downAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnPosition = ((Integer) animation.getAnimatedValue()).intValue();
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        ValueAnimator shakeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 12.0f, -12.0f, 12.0f, -12.0f, 0.0f});
        shakeAnimator.setDuration(400);
        shakeAnimator.setStartDelay(60);
        shakeAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
                TwToolBoxFloatingViewer.this.mLongPressPanelRemoveAnimating = false;
            }
        });
        shakeAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mLongPressPanelRemoveBtnAngle = ((Float) animation.getAnimatedValue()).floatValue();
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mShakeAnimators.clear();
        this.mShakeAnimators.add(upAnimator);
        this.mShakeAnimators.add(shakeAnimator);
        this.mShakeAnimators.add(downAnimator);
        this.mLongPressPanelRemoveBtnAnimator.playSequentially(this.mShakeAnimators);
        this.mTurnOffAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mTurnOffAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                System.putIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX, 0, -2);
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        this.mTurnOffAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    TwToolBoxFloatingViewer.this.mTurnOffEffectRatio = ((Float) animation.getAnimatedValue()).floatValue();
                    TwToolBoxFloatingViewer.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        postInvalidate();
        Log.d(TAG, "Count=" + this.mObjects.size());
    }

    private void clearToolBox() {
        removeCallbacks(this.mUpdateVisibility);
        removeCallbacks(this.mUpdateLongPressPanelRect);
        removeCallbacks(this.mCheckEnvironment);
        removeCallbacks(this.mTouchClickRunnable);
        removeCallbacks(this.mTouchLongClickRunnable);
        removeCallbacks(this.mTouchUpOrCancelRunnable);
        removeCallbacks(this.mPlayAnimationSleepRunnable);
        removeCallbacks(this.mPlayAnimationWakeupRunnable);
        removeCallbacks(this.mPlayAnimationFadeInRunnable);
        removeCallbacks(this.mPlayAnimationFadeOutRunnable);
        removeCallbacks(this.mPlayAnimationExpandRunnable);
        removeCallbacks(this.mPlayAnimationCollapseRunnable);
        removeCallbacks(this.mPlayAnimationEnterRunnable);
        setEnableVSync(false);
        if (!this.mObjects.isEmpty()) {
            Iterator i$ = this.mObjects.iterator();
            while (i$.hasNext()) {
                ToolBoxMenu m = (ToolBoxMenu) i$.next();
                m.aniExpand.removeAllListeners();
                m.aniExpand.removeAllUpdateListeners();
                m.aniExpand = null;
                m.action = null;
                if (m.shadowBitmap != null) {
                    m.shadowBitmap.recycle();
                    m.shadowBitmap = null;
                }
            }
            this.mObjects.clear();
            this.mExpandAnimator.removeAllListeners();
            this.mExpandAnimator.removeAllUpdateListeners();
            this.mTransparentAnimator.removeAllListeners();
            this.mTransparentAnimator.removeAllUpdateListeners();
            this.mSleepAnimator.removeAllListeners();
            this.mSleepAnimator.removeAllUpdateListeners();
            this.mEnterAnimator.removeAllListeners();
            this.mEnterAnimator.removeAllUpdateListeners();
            this.mLongPressPanelAnimator.removeAllListeners();
            this.mLongPressPanelAnimator.removeAllUpdateListeners();
            this.mTurnOffAnimator.removeAllListeners();
            this.mTurnOffAnimator.removeAllUpdateListeners();
            i$ = this.mShakeAnimators.iterator();
            while (i$.hasNext()) {
                ValueAnimator va = (ValueAnimator) ((Animator) i$.next());
                va.removeAllListeners();
                va.removeAllUpdateListeners();
            }
            this.mShakeAnimators.clear();
            Log.d(TAG, "ToolBox disposed...");
        }
        if (this.mPositionAnimatorX != null) {
            this.mPositionAnimatorX.removeAllUpdateListeners();
            this.mPositionAnimatorX = null;
            this.mPositionAnimatorY.removeAllListeners();
            this.mPositionAnimatorY.removeAllUpdateListeners();
            this.mPositionAnimatorY = null;
        }
        this.mExpanded = false;
        this.mEnterProgressValue = 0.0f;
        this.mExpandProgressValue = 0.0f;
        this.mMainCharacterDegree = 0;
        this.mLongPressPanelPosition = -this.LONG_PRESS_PANEL_HEIGHT_PX;
    }

    private boolean initializeFunctionShortcut(final ToolBoxMenu m, int index) {
        final ComponentName cm;
        switch (index) {
            case 0:
                try {
                    cm = new ComponentName("com.samsung.android.app.galaxyfinder", "com.samsung.android.app.galaxyfinder.GalaxyFinderActivity");
                    m.packageName = cm.flattenToString();
                    m.background = this.mContext.getDrawable(17304116);
                    m.isFunctionShortcut = true;
                    m.action = new ToolBoxAction() {
                        public void onAction() {
                            try {
                                Intent intent = new Intent();
                                intent.setComponent(cm);
                                TwToolBoxFloatingViewer.this.startActivityOrTask(intent, true);
                            } catch (Exception ex) {
                                Log.e(TwToolBoxFloatingViewer.TAG, "Sfinder Runtime error : " + ex);
                            }
                        }

                        public void onUpdateEnableStatus() {
                        }
                    };
                    break;
                } catch (Exception e) {
                    Log.e(TAG, "Sfinder Create error : " + e);
                    return false;
                }
            case 1:
                try {
                    m.background = this.mContext.getDrawable(17304115);
                    m.isFunctionShortcut = true;
                    m.action = new ToolBoxAction() {
                        public void onAction() {
                            try {
                                IQuickConnectManager.Stub.asInterface(ServiceManager.getService("quickconnect")).selectedItemCallback();
                            } catch (Exception ex) {
                                Log.e(TwToolBoxFloatingViewer.TAG, "QuickConnect Runtime error : " + ex);
                            }
                        }

                        public void onUpdateEnableStatus() {
                        }
                    };
                    break;
                } catch (Exception e2) {
                    Log.e(TAG, "QuickConnect Create error : " + e2);
                    return false;
                }
            case 2:
                try {
                    m.operationType = OperationType.TOGGLE;
                    m.background = this.mContext.getDrawable(17304120);
                    m.backgroundInverse = this.mResources.getDrawable(17304119);
                    m.toggleStatus = getTorchStatus();
                    m.isFunctionShortcut = true;
                    m.action = new ToolBoxToggleAction() {
                        public void onAction() {
                            try {
                                m.toggleStatus = !TwToolBoxFloatingViewer.this.getTorchStatus();
                                TwToolBoxFloatingViewer.this.toggleTorch();
                            } catch (Exception ex) {
                                Log.e(TwToolBoxFloatingViewer.TAG, "TorchLight Runtime error : " + ex);
                            }
                        }

                        public void onUpdateToggleStatus() {
                            m.toggleStatus = TwToolBoxFloatingViewer.this.getTorchStatus();
                        }

                        public void onUpdateEnableStatus() {
                            boolean z = false;
                            ToolBoxMenu toolBoxMenu = m;
                            if (System.getIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, "activity_zone_is_using_torch_light", 0, -2) == 0) {
                                z = true;
                            }
                            toolBoxMenu.isEnabled = z;
                        }
                    };
                    break;
                } catch (Exception e22) {
                    Log.e(TAG, "TorchLight Create error : " + e22);
                    return false;
                }
            case 3:
                try {
                    m.background = this.mContext.getDrawable(17304118);
                    m.isFunctionShortcut = true;
                    m.action = new ToolBoxAction() {
                        public void onAction() {
                            try {
                                if (TwToolBoxFloatingViewer.this.mEditAfterScreenCapture == -1) {
                                    TwToolBoxFloatingViewer.this.mEditAfterScreenCapture = System.getIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, "edit_after_screen_capture", 0, -2);
                                }
                                System.putIntForUser(TwToolBoxFloatingViewer.this.mContentResolver, "edit_after_screen_capture", 1, -2);
                                TwToolBoxFloatingViewer.this.setVisibility(8);
                                TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mUpdateVisibility, 2000);
                                TwToolBoxFloatingViewer.this.screenCapture();
                                TwToolBoxFloatingViewer.this.removeCallbacks(TwToolBoxFloatingViewer.this.mScreenWriteRecovery);
                                TwToolBoxFloatingViewer.this.postDelayed(TwToolBoxFloatingViewer.this.mScreenWriteRecovery, 10000);
                            } catch (Exception ex) {
                                Log.e(TwToolBoxFloatingViewer.TAG, "ScreenWrite Runtime error : " + ex);
                            }
                        }

                        public void onUpdateEnableStatus() {
                        }
                    };
                    break;
                } catch (Exception e222) {
                    Log.e(TAG, "ScreenWrite Create error : " + e222);
                    return false;
                }
            case 4:
                try {
                    cm = new ComponentName("com.sec.android.app.magnifier", "com.sec.android.app.magnifier.Magnifier");
                    m.packageName = cm.flattenToString();
                    m.background = this.mContext.getDrawable(17304114);
                    m.isFunctionShortcut = true;
                    m.action = new ToolBoxAction() {
                        public void onAction() {
                            try {
                                Intent intent = new Intent();
                                intent.setComponent(cm);
                                intent.addFlags(268435456);
                                TwToolBoxFloatingViewer.this.startActivityOrTask(intent, true);
                            } catch (Exception ex) {
                                Log.e(TwToolBoxFloatingViewer.TAG, "Magnifier Runtime error : " + ex);
                            }
                        }

                        public void onUpdateEnableStatus() {
                        }
                    };
                    break;
                } catch (Exception e2222) {
                    Log.e(TAG, "Magnifier Create error : " + e2222);
                    return false;
                }
            default:
                return false;
        }
        return true;
    }

    private void playAnimationEnter() {
        if (!this.mHiddenToolBox) {
            this.mEnterAnimator.start();
        }
    }

    private void playAnimationSleep() {
        if (!this.mHiddenToolBox && !this.mLongPressed && !this.mExpanded && this.mTouchMode == TouchMode.NORMAL && !this.mTransparentAnimator.isRunning()) {
            if (this.mSleepAnimator.isRunning()) {
                this.mSleepAnimator.cancel();
            }
            this.mSleepAnimator.setDuration(667);
            this.mSleepAnimator.setFloatValues(new float[]{getAlpha(), 0.4f});
            this.mSleepAnimator.start();
        }
    }

    private void playAnimationWakeup() {
        if (!this.mHiddenToolBox && !this.mTransparentAnimator.isRunning()) {
            if (this.mSleepAnimator.isRunning()) {
                this.mSleepAnimator.cancel();
            }
            if (getAlpha() != 1.0f) {
                this.mSleepAnimator.setDuration(333);
                this.mSleepAnimator.setFloatValues(new float[]{1.0f, alpha});
                this.mSleepAnimator.reverse();
            }
        }
    }

    private void playAnimationExpand() {
        if (!this.mExpanded) {
            removeCallbacks(this.mPlayAnimationSleepRunnable);
            this.mExpanded = true;
            this.mExpandAnimator.start();
            Iterator i$ = this.mObjects.iterator();
            while (i$.hasNext()) {
                ToolBoxMenu m = (ToolBoxMenu) i$.next();
                if (m.action instanceof ToolBoxToggleAction) {
                    ((ToolBoxToggleAction) m.action).onUpdateToggleStatus();
                }
                m.action.onUpdateEnableStatus();
                m.aniExpand.start();
            }
        }
    }

    private void playAnimationCollapse() {
        if (this.mExpanded) {
            this.mExpanded = false;
            this.mExpandAnimator.reverse();
            Iterator i$ = this.mObjects.iterator();
            while (i$.hasNext()) {
                ((ToolBoxMenu) i$.next()).aniExpand.reverse();
            }
            removeCallbacks(this.mPlayAnimationSleepRunnable);
            postDelayed(this.mPlayAnimationSleepRunnable, 3000);
        }
    }

    private void playAnimationFadeIn() {
        if (this.mHiddenToolBox) {
            this.mEnableFloatingViewer = isEnableFloatingViewer();
            this.mDisableFloatingViewer = !this.mEnableFloatingViewer;
            if (!this.mDisableFloatingViewer) {
                if (this.mSleepAnimator.isRunning()) {
                    this.mSleepAnimator.cancel();
                }
                Log.v(TAG, "playAnimationFadeIn()");
                this.mHiddenToolBox = false;
                this.mTransparentAnimator.setFloatValues(new float[]{1.0f, getAlpha()});
                this.mTransparentAnimator.reverse();
                removeCallbacks(this.mPlayAnimationSleepRunnable);
                postDelayed(this.mPlayAnimationSleepRunnable, 3000);
            }
        }
    }

    private void playAnimationFadeOut() {
        if (!this.mHiddenToolBox) {
            if (this.mSleepAnimator.isRunning()) {
                this.mSleepAnimator.cancel();
            }
            removeCallbacks(this.mPlayAnimationSleepRunnable);
            Log.v(TAG, "playAnimationFadeOut()");
            this.mHiddenToolBox = true;
            this.mTransparentAnimator.setFloatValues(new float[]{getAlpha(), 0.0f});
            this.mTransparentAnimator.start();
        }
    }

    private void saveCenterPosition() {
        System.putFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO, ((float) this.mCenterX) / ((float) this.mDisplayMetrics.widthPixels), -2);
        System.putFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_Y_RATIO, ((float) this.mCenterY) / ((float) this.mDisplayMetrics.heightPixels), -2);
    }

    private void saveDefaultCenterPosition() {
        int defaultHalfWidth = this.mMainCharacter == null ? 0 : (this.FIXED_MAIN_ITEM_WIDTH / 2) - this.mMainCharacterDecreaseAmount;
        float screenWidth = (float) this.mDisplayMetrics.widthPixels;
        float screenHeight = (float) this.mDisplayMetrics.heightPixels;
        int topPadding = this.FIXED_START_POSITION_TOP_PADDING;
        System.putFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO, ((screenWidth - ((float) defaultHalfWidth)) - ((float) this.FIXED_START_POSITION_RIGHT_PADDING)) / screenWidth, -2);
        System.putFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_Y_RATIO, ((float) (defaultHalfWidth + topPadding)) / screenHeight, -2);
    }

    private void loadCenterPosition() {
        this.mCenterX = (int) (System.getFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO, 0.0f, -2) * ((float) this.mDisplayMetrics.widthPixels));
        this.mCenterY = (int) (System.getFloatForUser(this.mContentResolver, TwToolBoxService.SETTINGS_SHOW_TOOLBOX_FLOATING_Y_RATIO, 0.0f, -2) * ((float) this.mDisplayMetrics.heightPixels));
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            removeCallbacks(this.mPlayAnimationSleepRunnable);
            postDelayed(this.mPlayAnimationSleepRunnable, 3000);
            removeCallbacks(this.mPlayAnimationWakeupRunnable);
            postDelayed(this.mPlayAnimationWakeupRunnable, 50);
            return;
        }
        removeCallbacks(this.mPlayAnimationSleepRunnable);
        removeCallbacks(this.mPlayAnimationWakeupRunnable);
        removeCallbacks(this.mTouchLongClickRunnable);
        goToPositionAndromeda();
    }

    private boolean getSCoverState() {
        if (this.mCoverManager != null) {
            CoverState state = this.mCoverManager.getCoverState();
            if (state != null) {
                return state.getSwitchState();
            }
        }
        return true;
    }

    private void startActivityOrTask(Intent intent) {
        startActivityOrTask(intent, false);
    }

    private void startActivityOrTask(Intent intent, boolean isFunctionShortcut) {
        if (intent != null) {
            int bestMatchTaskId = -1;
            ComponentName component = intent.getComponent();
            String targetPackageName = component.getClassName();
            List<RunningTaskInfo> taskInfoList = this.mActivityManager.getRunningTasks(30);
            if (taskInfoList != null) {
                for (RunningTaskInfo taskInfo : taskInfoList) {
                    String runningPackageName;
                    if (taskInfo.topActivity == null) {
                        runningPackageName = "null";
                    } else {
                        try {
                            runningPackageName = taskInfo.topActivity.getClassName();
                        } catch (Exception e) {
                            Log.e(TAG, "Exception startActivityOrTask() : " + e);
                        }
                    }
                    String sourcePackageName;
                    if (taskInfo.sourceActivity == null) {
                        sourcePackageName = "null";
                    } else {
                        sourcePackageName = taskInfo.sourceActivity.getClassName();
                    }
                    if (!taskInfo.isHomeType && taskInfo.userId == ActivityManager.getCurrentUser()) {
                        if (runningPackageName.equals(targetPackageName) || sourcePackageName.equals(targetPackageName)) {
                            try {
                                if ((this.mPackageManager.getActivityInfo(component, 0).flags & 4) != 0 && taskInfo.multiWindowStyle.getType() == 0) {
                                    bestMatchTaskId = -1;
                                    break;
                                } else if (runningPackageName.equals(targetPackageName)) {
                                    bestMatchTaskId = taskInfo.id;
                                    break;
                                } else if (!isFunctionShortcut && sourcePackageName.equals(targetPackageName)) {
                                    bestMatchTaskId = taskInfo.id;
                                }
                            } catch (Exception e2) {
                                bestMatchTaskId = -1;
                            }
                        }
                    }
                }
                if (bestMatchTaskId >= 0) {
                    this.mActivityManager.moveTaskToFront(bestMatchTaskId, 1);
                    Log.d(TAG, "moveTaskToFront()");
                    return;
                }
            }
            this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
            Log.d(TAG, "startActivityAsUser()");
        }
    }

    private boolean isValidUser() {
        boolean isKnoxUser;
        if (this.mPersonaId >= 100 || this.mPersonaManager.isKioskContainerExistOnDevice()) {
            isKnoxUser = true;
        } else {
            isKnoxUser = false;
        }
        return (isKnoxUser || this.mUserManager.getUserInfo(UserHandle.myUserId()).isRestricted()) ? false : true;
    }

    private boolean isKnoxRunning() {
        return "true".equals(SystemProperties.get("dev.knoxapp.running", "false"));
    }

    private boolean isDisableFloatingViewer() {
        boolean needBlock = false;
        for (String blockPkg : this.BLOCK_LIST) {
            if (blockPkg.equals(this.mCurrentPackage)) {
                needBlock = true;
                break;
            }
        }
        if (needBlock || this.mKeyguardShowing || !this.mDeivceProvisioned || this.mKidsMode || !this.mCoverOpen || this.mExploreByTouchMode || !this.mValidUser || this.mOTAShowing || this.mCarMode || this.mKnoxRunning || isWindowPinned()) {
            return true;
        }
        return false;
    }

    private boolean isEnableFloatingViewer() {
        boolean needBlock = false;
        for (String blockPkg : this.BLOCK_LIST) {
            if (blockPkg.equals(this.mCurrentPackage)) {
                needBlock = true;
                break;
            }
        }
        if (needBlock || this.mKeyguardShowing || !this.mDeivceProvisioned || this.mKidsMode || !this.mCoverOpen || this.mExploreByTouchMode || !this.mValidUser || this.mOTAShowing || this.mCarMode || this.mKnoxRunning || isWindowPinned()) {
            return false;
        }
        return true;
    }

    private boolean isEnabledPkg(String packageName) {
        try {
            int enable = this.mPackageManager.getApplicationEnabledSetting(packageName);
            if (2 == enable || 3 == enable) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    private boolean isEnableOTA() {
        return false;
    }

    private boolean isWindowPinned() {
        return this.mActivityManager.isInLockTaskMode();
    }

    private void toggleTorch() {
        Intent intent = new Intent(getTorchStatus() ? TwToolBoxService.ACTION_ASSISTIVE_LIGHT_OFF : TwToolBoxService.ACTION_ASSISTIVE_LIGHT_ON);
        intent.addFlags(268435456);
        this.mContext.sendBroadcast(intent);
    }

    private boolean getTorchStatus() {
        return System.getIntForUser(this.mContentResolver, "torch_light", 0, -2) != 0;
    }

    private void screenCapture() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyEvent captureEvent = new KeyEvent(0, 120);
                    Instrumentation captureIns = new Instrumentation();
                    captureIns.sendKeySync(captureEvent);
                    Thread.sleep(500);
                    captureIns.sendKeySync(new KeyEvent(1, 120));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
