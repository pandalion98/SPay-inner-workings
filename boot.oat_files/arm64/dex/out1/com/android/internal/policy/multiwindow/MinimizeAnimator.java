package com.android.internal.policy.multiwindow;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.ActivityThread;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.DragEvent;
import android.view.HardwareRenderer;
import android.view.IWindowManager.Stub;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.interpolator.SineInOut33;
import android.view.animation.interpolator.SineInOut70;
import android.view.animation.interpolator.SineInOut80;
import android.view.animation.interpolator.SineInOut90;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.policy.MultiPhoneWindow;
import com.android.internal.policy.PhoneWindow;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowStyle;

public class MinimizeAnimator {
    private static boolean DEBUG = MultiPhoneWindow.DEBUG_MINIMIZE_ANIM;
    private static boolean DEBUG_MINIMIZE_REMOVE_ANIM = false;
    private static final int DRAG_AND_DROP_TIMER_TIME = 700;
    private static final int MESSAGE_REQUEST_MAXIMIZE = 100;
    private static final String TAG = "MinimizeAnimator";
    private static final boolean bDSSEnabled = true;
    private Activity mActivity = null;
    private boolean mAnimating = false;
    private boolean mAnimationCancelByMaximize = false;
    private ViewGroup mContentRootContainer;
    private Context mContext;
    private Docking mDocking;
    private boolean mDragMode = false;
    private float mDssScale = 1.0f;
    private float mFirstDownX = 0.0f;
    private float mFirstDownY = 0.0f;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    MinimizeAnimator.this.mMultiPhoneWindow.requestMaximize();
                    return;
                default:
                    return;
            }
        }
    };
    private int mInitPositionX;
    private int mInitPositionY;
    private final OnComputeInternalInsetsListener mInsetsComputer = new OnComputeInternalInsetsListener() {
        public void onComputeInternalInsets(InternalInsetsInfo info) {
            info.contentInsets.setEmpty();
            info.visibleInsets.setEmpty();
            info.touchableRegion.set(MinimizeAnimator.this.mTouchableRegion);
            info.setTouchableInsets(3);
        }
    };
    private boolean mIsMoving = false;
    private boolean mIsSupportSimplificationUI;
    private boolean mIsTouchDown = false;
    private float mLastPositionX = 0.0f;
    private float mLastPositionY = 0.0f;
    private int mLastRotation = -1;
    private View mMinimizedIcon;
    private final int mMinimizedIconDefaultSize;
    private int mMinimizedIconHeight = 0;
    private int mMinimizedIconWidth = 0;
    private MinimizedWindowListener mMinimizedWindowListener;
    private int mMoveInterval = 0;
    private final MultiPhoneWindow mMultiPhoneWindow;
    private final MultiWindowFacade mMultiWindowFacade;
    private boolean mReadyToShow = false;
    private int mRemoveLayoutHeight = 0;
    private int mStatusBarHeight = 0;
    private ApplicationThumbnail mThumbnail;
    private IBinder mToken = null;
    private final Region mTouchableRegion = new Region();
    private TrashAnimationEffect mTrashAnimationEffect;
    private boolean mUsingSelectiveOrientation = false;
    private Window mWindow = null;
    private WindowManager mWindowManager;

    private class ContentFrameDragListener implements OnDragListener {
        private ContentFrameDragListener() {
        }

        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case 1:
                case 5:
                    return true;
                case 2:
                    if (MinimizeAnimator.this.mDragMode && !MinimizeAnimator.this.mHandler.hasMessages(100)) {
                        MinimizeAnimator.this.mHandler.sendMessageDelayed(Message.obtain(MinimizeAnimator.this.mHandler, 100), 700);
                        break;
                    }
                case 3:
                case 4:
                    if (MinimizeAnimator.this.mHandler.hasMessages(100)) {
                        MinimizeAnimator.this.mHandler.removeMessages(100);
                        break;
                    }
                    break;
                case 6:
                    if (MinimizeAnimator.this.mDragMode && MinimizeAnimator.this.mHandler.hasMessages(100)) {
                        MinimizeAnimator.this.mHandler.removeMessages(100);
                        break;
                    }
            }
            return false;
        }
    }

    private class ContentRootContainer extends FrameLayout {
        private Point mDisplaySize = new Point();
        private int mLastOrientation = -1;

        public ContentRootContainer(Context context) {
            super(context);
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (MinimizeAnimator.DEBUG) {
                MinimizeAnimator.this.mUsingSelectiveOrientation = MinimizeAnimator.this.mActivity.isFixedOrientationCascade();
                MinimizeAnimator.this.getDisplaySize(this.mDisplaySize);
                this.mParent.requestTransparentRegion(this);
            } else {
                MinimizeAnimator.this.mUsingSelectiveOrientation = MinimizeAnimator.this.mActivity.isFixedOrientationCascade();
                MinimizeAnimator.this.getDisplaySize(this.mDisplaySize);
                this.mParent.requestTransparentRegion(this);
            }
        }

        protected void onDetachedFromWindow() {
            if (MinimizeAnimator.DEBUG) {
                super.onDetachedFromWindow();
            } else {
                super.onDetachedFromWindow();
            }
        }

        public boolean gatherTransparentRegion(Region region) {
            boolean opaque = super.gatherTransparentRegion(region);
            if (MinimizeAnimator.this.mIsTouchDown || MinimizeAnimator.this.mAnimating) {
                region.setEmpty();
            } else {
                if (MinimizeAnimator.this.mUsingSelectiveOrientation) {
                    MinimizeAnimator.this.getDisplaySize(this.mDisplaySize);
                }
                Region transparentRegion = new Region(0, 0, this.mDisplaySize.x, this.mDisplaySize.y);
                transparentRegion.op(MinimizeAnimator.this.mTouchableRegion, Op.XOR);
                region.set(transparentRegion);
            }
            if (MinimizeAnimator.DEBUG) {
                Log.d(MinimizeAnimator.TAG, "gatherTransparentRegion: Transparent region=" + region + " Touchable region=" + MinimizeAnimator.this.mTouchableRegion);
            }
            return opaque;
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.mLastOrientation != newConfig.orientation) {
                this.mLastOrientation = newConfig.orientation;
                MinimizeAnimator.this.getDisplaySize(this.mDisplaySize);
            }
        }
    }

    private class MinimizedWindowListener implements OnTouchListener, OnLongClickListener {
        private MinimizedWindowListener() {
        }

        public boolean onLongClick(View v) {
            if (MinimizeAnimator.DEBUG) {
                Log.d(MinimizeAnimator.TAG, "onLongClick mIsMoving=" + MinimizeAnimator.this.mIsMoving);
            }
            if (MinimizeAnimator.this.mIsMoving) {
                return false;
            }
            MinimizeAnimator.this.mTrashAnimationEffect.showTrash();
            if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                if (MinimizeAnimator.this.mTrashAnimationEffect.rangeCheck()) {
                    MinimizeAnimator.this.mTrashAnimationEffect.openTrash((float) ((int) (MinimizeAnimator.this.mMinimizedIcon.getX() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2)))), (float) ((int) (MinimizeAnimator.this.mMinimizedIcon.getY() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2)))));
                }
            } else if (MinimizeAnimator.this.mLastPositionX >= ((float) MinimizeAnimator.this.mStatusBarHeight) && MinimizeAnimator.this.mLastPositionY <= ((float) (MinimizeAnimator.this.mStatusBarHeight + MinimizeAnimator.this.mRemoveLayoutHeight))) {
                MinimizeAnimator.this.mTrashAnimationEffect.openTrash(MinimizeAnimator.this.mLastPositionX, MinimizeAnimator.this.mLastPositionY);
            }
            return true;
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (MinimizeAnimator.this.mMinimizedIcon == null) {
                return false;
            }
            float currentX = event.getRawX();
            float currentY = event.getRawY();
            switch (event.getAction()) {
                case 0:
                    MinimizeAnimator.this.mDocking.init();
                    MinimizeAnimator.this.mIsMoving = false;
                    MinimizeAnimator.this.mIsTouchDown = true;
                    MinimizeAnimator.this.mLastPositionX = MinimizeAnimator.this.mFirstDownX = currentX;
                    MinimizeAnimator.this.mLastPositionY = MinimizeAnimator.this.mFirstDownY = currentY;
                    MinimizeAnimator.this.mTrashAnimationEffect.cancelhideTrashForRemoveAnimation(true);
                    if (MinimizeAnimator.this.mAnimationCancelByMaximize) {
                        MinimizeAnimator.this.mAnimationCancelByMaximize = false;
                    }
                    Point displaySize = new Point();
                    MinimizeAnimator.this.getDisplaySize(displaySize);
                    MinimizeAnimator.this.setTouchableRegion(new Rect(0, 0, displaySize.x, displaySize.y), 0);
                    return false;
                case 1:
                case 3:
                    MinimizeAnimator.this.mIsTouchDown = false;
                    if (MinimizeAnimator.this.mIsMoving) {
                        if (!MinimizeAnimator.this.mDocking.isDocking() || MinimizeAnimator.this.mDocking.isDockingCanceled()) {
                            float diffX;
                            float diffY;
                            if (event.getAction() == 3) {
                                diffX = MinimizeAnimator.this.mLastPositionX - MinimizeAnimator.this.mFirstDownX;
                                diffY = MinimizeAnimator.this.mLastPositionY - MinimizeAnimator.this.mFirstDownY;
                            } else {
                                diffX = currentX - MinimizeAnimator.this.mFirstDownX;
                                diffY = currentY - MinimizeAnimator.this.mFirstDownY;
                            }
                            MinimizeAnimator.this.mMultiPhoneWindow.moveStackBound((int) diffX, (int) diffY, false);
                        } else {
                            MinimizeAnimator.this.mDocking.checkCenterBarPoint();
                            if (MinimizeAnimator.this.mContext instanceof Activity) {
                                ((Activity) MinimizeAnimator.this.mContext).getMultiWindowStyle().setOption(4, false);
                            }
                            if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                                MinimizeAnimator.this.mMultiWindowFacade.setMultiWindowStyleWithLogging(MinimizeAnimator.this.mToken, Docking.getChanagedMultiWindowStyle(MinimizeAnimator.this.mDocking.getZone(), MinimizeAnimator.this.mMultiPhoneWindow.getMultiWindowStyle()), 1);
                            } else {
                                MinimizeAnimator.this.mMultiWindowFacade.setMultiWindowStyle(MinimizeAnimator.this.mToken, Docking.getChanagedMultiWindowStyle(MinimizeAnimator.this.mDocking.getZone(), MinimizeAnimator.this.mMultiPhoneWindow.getMultiWindowStyle()));
                            }
                        }
                    }
                    int movedX = (int) (MinimizeAnimator.this.mMinimizedIcon.getX() + (currentX - MinimizeAnimator.this.mLastPositionX));
                    int movedY = (int) (MinimizeAnimator.this.mMinimizedIcon.getY() + (currentY - MinimizeAnimator.this.mLastPositionY));
                    MinimizeAnimator.this.mMinimizedIcon.setX((float) movedX);
                    MinimizeAnimator.this.mMinimizedIcon.setY((float) movedY);
                    boolean applyThrowAwayAnimation = false;
                    boolean isShowingTrash = MinimizeAnimator.this.mTrashAnimationEffect.isShowingTrash();
                    if (isShowingTrash) {
                        MultiWindowStyle style = MinimizeAnimator.this.mActivity.getMultiWindowStyle();
                        if (style != null && style.getType() == 2 && style.isEnabled(4)) {
                            if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                                if (MinimizeAnimator.this.mTrashAnimationEffect.rangeCheck()) {
                                    applyThrowAwayAnimation = true;
                                    MinimizeAnimator.this.mTrashAnimationEffect.throwAway();
                                }
                            } else if (event.getRawY() >= ((float) MinimizeAnimator.this.mStatusBarHeight) && event.getRawY() <= ((float) (MinimizeAnimator.this.mStatusBarHeight + MinimizeAnimator.this.mRemoveLayoutHeight))) {
                                try {
                                    MinimizeAnimator.this.mMultiWindowFacade.removeAllTasks(MinimizeAnimator.this.mToken, 4);
                                } catch (IllegalStateException e) {
                                    MinimizeAnimator.this.mActivity.finish();
                                }
                            }
                        }
                    }
                    boolean isSetToucbleRegion = true;
                    if (!applyThrowAwayAnimation) {
                        if (isShowingTrash) {
                            if (MinimizeAnimator.this.mAnimationCancelByMaximize) {
                                MinimizeAnimator.this.hide();
                                MinimizeAnimator.this.mTrashAnimationEffect.hideTrash();
                                isSetToucbleRegion = false;
                            } else if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                                MinimizeAnimator.this.mTrashAnimationEffect.hideTrashScaleAlphaAnimation(false, new Rect(movedX, movedY, MinimizeAnimator.this.mMinimizedIconWidth + movedX, MinimizeAnimator.this.mMinimizedIconHeight + movedY));
                                MinimizeAnimator.this.mTrashAnimationEffect.bounceShowRedCircleAnimation(false);
                                MinimizeAnimator.this.mAnimating = true;
                                isSetToucbleRegion = false;
                            } else {
                                MinimizeAnimator.this.mTrashAnimationEffect.hideTrash();
                            }
                        }
                        if (isSetToucbleRegion) {
                            Rect rect = new Rect(movedX, movedY, MinimizeAnimator.this.mMinimizedIconWidth + movedX, MinimizeAnimator.this.mMinimizedIconHeight + movedY);
                            MinimizeAnimator.this.setTouchableRegion(rect, 0);
                            if (!MinimizeAnimator.this.isOutOfDisplay(rect) && MinimizeAnimator.this.mIsMoving) {
                                MinimizeAnimator.this.setTouchableRegion(rect, 150);
                            }
                        }
                    }
                    if (!(MinimizeAnimator.this.mIsMoving || isShowingTrash)) {
                        if (MinimizeAnimator.DEBUG) {
                            Log.d(MinimizeAnimator.TAG, "Minimized -> Floating");
                        }
                        MinimizeAnimator.this.hide();
                        MinimizeAnimator.this.mTrashAnimationEffect.cancelhideTrashForRemoveAnimation(false);
                        MinimizeAnimator.this.mHandler.removeMessages(100);
                        MinimizeAnimator.this.mHandler.sendMessage(MinimizeAnimator.this.mHandler.obtainMessage(100));
                    }
                    MinimizeAnimator.this.mDocking.clear();
                    MinimizeAnimator.this.mMultiPhoneWindow.dismissGuide();
                    return false;
                case 2:
                    if (!MinimizeAnimator.this.mIsMoving) {
                        if (Math.abs(((float) ((int) event.getRawX())) - MinimizeAnimator.this.mFirstDownX) >= ((float) MinimizeAnimator.this.mMoveInterval) || Math.abs(((float) ((int) event.getRawY())) - MinimizeAnimator.this.mFirstDownY) >= ((float) MinimizeAnimator.this.mMoveInterval)) {
                            MinimizeAnimator.this.mIsMoving = true;
                        } else if (!MinimizeAnimator.DEBUG) {
                            return false;
                        } else {
                            Log.d(MinimizeAnimator.TAG, "MinimizedIcon isn't moved");
                            return false;
                        }
                    }
                    if (MinimizeAnimator.this.mLastPositionX == currentX && MinimizeAnimator.this.mLastPositionY == currentY) {
                        return false;
                    }
                    Rect dockingBound;
                    MinimizeAnimator.this.mMinimizedIcon.setX(MinimizeAnimator.this.mMinimizedIcon.getX() + (currentX - MinimizeAnimator.this.mLastPositionX));
                    MinimizeAnimator.this.mMinimizedIcon.setY(MinimizeAnimator.this.mMinimizedIcon.getY() + (currentY - MinimizeAnimator.this.mLastPositionY));
                    MinimizeAnimator.this.mLastPositionX = currentX;
                    MinimizeAnimator.this.mLastPositionY = currentY;
                    if (MinimizeAnimator.this.mTrashAnimationEffect.isShowingTrash()) {
                        if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                            if (MinimizeAnimator.this.mTrashAnimationEffect.rangeCheck()) {
                                MinimizeAnimator.this.mTrashAnimationEffect.openTrash((float) ((int) (MinimizeAnimator.this.mMinimizedIcon.getX() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2)))), (float) ((int) (MinimizeAnimator.this.mMinimizedIcon.getY() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2)))));
                            } else {
                                MinimizeAnimator.this.mTrashAnimationEffect.closeTrash();
                            }
                        } else if (currentY < ((float) MinimizeAnimator.this.mStatusBarHeight) || currentY > ((float) (MinimizeAnimator.this.mStatusBarHeight + MinimizeAnimator.this.mRemoveLayoutHeight))) {
                            MinimizeAnimator.this.mTrashAnimationEffect.closeTrash();
                        } else {
                            MinimizeAnimator.this.mTrashAnimationEffect.openTrash(currentX, currentY);
                        }
                    }
                    int offsetX = 0;
                    int offsetY = 0;
                    if (MinimizeAnimator.this.mDocking.mCurScreenHeight > MinimizeAnimator.this.mDocking.mCurScreenWidth) {
                        if (((float) (MinimizeAnimator.this.mDocking.mCurScreenHeight / 2)) < MinimizeAnimator.this.mMinimizedIcon.getY()) {
                            offsetY = MinimizeAnimator.this.mMinimizedIconDefaultSize;
                        }
                    } else if (((float) (MinimizeAnimator.this.mDocking.mCurScreenWidth / 2)) < MinimizeAnimator.this.mMinimizedIcon.getX()) {
                        offsetX = MinimizeAnimator.this.mMinimizedIconDefaultSize;
                    }
                    if (MinimizeAnimator.this.mTrashAnimationEffect.isShowingTrash() || MinimizeAnimator.this.mDocking.updateZone((int) ((MinimizeAnimator.this.mMinimizedIcon.getX() + ((float) offsetX)) / MinimizeAnimator.this.mDssScale), (int) ((MinimizeAnimator.this.mMinimizedIcon.getY() + ((float) offsetY)) / MinimizeAnimator.this.mDssScale), true)) {
                        dockingBound = MinimizeAnimator.this.mDocking.getBounds();
                    } else {
                        dockingBound = MinimizeAnimator.this.mDocking.getBounds();
                    }
                    if (!(dockingBound == null || MinimizeAnimator.this.mDocking.isDockingCanceled())) {
                        MinimizeAnimator.this.mMultiPhoneWindow.showGuide(dockingBound, 2);
                    }
                    if (!MinimizeAnimator.this.mDocking.isDocking()) {
                        MinimizeAnimator.this.mMultiPhoneWindow.dismissGuide();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    private class TrashAnimationEffect {
        private final int TRASH_DIRECTION_LEFT = 1;
        private final int TRASH_DIRECTION_NONE = 0;
        private final int TRASH_DIRECTION_RIGHT = 2;
        private ImageView coveredTrash;
        private AnimationDrawable mFrameTrashBodyAnim;
        private boolean mIsShowingTrash = false;
        private boolean mIsTrashOpen = false;
        private boolean mNeedAnimation = false;
        private int mRedCircleHeight = 0;
        private int mRemoveLayoutWidth = 0;
        private Rect mRemoveRangeRect;
        private int mRemoveRangeRectMargin;
        private int mShowTrashDirection = 0;
        private int mTopBgFocusEffectMargin = 0;
        private int mTopMargin = 0;
        private int mTrashMargin = 0;
        private int removeBackGroundBlackColor;
        private int removeBackGroundRedColor;
        private View removeLayout;
        private ImageView topBgFocusEffect;
        private LayerDrawable topBgFocusEffectDrawable;
        private GradientDrawable topBgFocusEffectDrawableItem;
        private LinearLayout topBgFocusEffectOuter;
        private View trash;
        private ImageView trashBody;
        private ImageView trashBodyForRemoveAnimation = null;
        private ImageView trashCover;
        private TextView trashText;
        private View trashWithText;

        public TrashAnimationEffect() {
            MinimizeAnimator.this.mMoveInterval = MinimizeAnimator.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_move_interporation);
            this.mTopBgFocusEffectMargin = MinimizeAnimator.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_red_circle_margin_for_gradation_bg);
            this.mTrashMargin = MinimizeAnimator.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_trash_margin_for_gradation_bg);
            this.mRedCircleHeight = MinimizeAnimator.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_remove_red_circle_height);
            this.mRemoveRangeRectMargin = MinimizeAnimator.this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_ratio_margin_to_remove_minimize_icon);
            this.removeBackGroundBlackColor = MinimizeAnimator.this.mContext.getResources().getColor(R.color.multiwindow_minimized_icon_remove_bg_black_color, null);
            this.removeBackGroundRedColor = MinimizeAnimator.this.mContext.getResources().getColor(R.color.multiwindow_minimized_icon_remove_bg_red_color, null);
        }

        boolean isShowingTrash() {
            return this.mIsShowingTrash;
        }

        void showTrash() {
            this.removeLayout = LayoutInflater.from(MinimizeAnimator.this.mContext).inflate((int) R.layout.mw_window_remove_layout, null);
            this.trashWithText = (LinearLayout) this.removeLayout.findViewById(R.id.delete_icon_with_text_layout);
            this.trash = (FrameLayout) this.removeLayout.findViewById(R.id.delete_icon_layout);
            this.coveredTrash = (ImageView) this.removeLayout.findViewById(R.id.delete_icon);
            this.trashCover = (ImageView) this.removeLayout.findViewById(R.id.delete_icon_cover);
            this.trashBody = (ImageView) this.removeLayout.findViewById(R.id.delete_icon_body);
            this.trashText = (TextView) this.removeLayout.findViewById(R.id.delete_text);
            FrameLayout rootView = null;
            if (MinimizeAnimator.this.mWindow != null) {
                rootView = (FrameLayout) MinimizeAnimator.this.mWindow.getDecorView().findViewById(16908290);
            }
            Point fullscreen = new Point();
            MinimizeAnimator.this.getDisplaySize(fullscreen);
            if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                this.trashBodyForRemoveAnimation = (ImageView) this.removeLayout.findViewById(R.id.delete_icon_body_for_remove_animation);
                this.trashBodyForRemoveAnimation.setBackgroundResource(R.anim.multiwindow_full_remove_trash_body);
                this.trashBodyForRemoveAnimation.setVisibility(0);
                this.trashCover.setVisibility(0);
                this.coveredTrash.setVisibility(8);
                this.topBgFocusEffect = (ImageView) this.removeLayout.findViewById(R.id.top_bg_focus_circle);
                this.topBgFocusEffectOuter = (LinearLayout) this.removeLayout.findViewById(R.id.top_bg_focus_circle_outer);
                this.topBgFocusEffectDrawable = (LayerDrawable) MinimizeAnimator.this.mContext.getResources().getDrawable(R.drawable.top_bg_red_circle, null);
                this.topBgFocusEffectDrawableItem = (GradientDrawable) this.topBgFocusEffectDrawable.findDrawableByLayerId(R.id.top_bg_red_circle_item);
                this.mRemoveLayoutWidth = fullscreen.x;
                int left = fullscreen.x / 8;
                this.mRemoveRangeRect = new Rect(left, this.mTrashMargin - this.mRemoveRangeRectMargin, fullscreen.x - left, (this.mTrashMargin + this.mRedCircleHeight) + (this.mRemoveRangeRectMargin * 2));
                if (rootView != null) {
                    rootView.addView(this.removeLayout, 0, new LayoutParams(fullscreen.x, fullscreen.y));
                }
                this.topBgFocusEffect.setVisibility(0);
                showTrashScaleAlphaAnimation();
                changeRedCircleColor(false);
                bounceShowRedCircleAnimation(true);
            } else {
                this.topBgFocusEffect = (ImageView) this.removeLayout.findViewById(R.id.top_bg_focus);
                this.coveredTrash.setVisibility(0);
                this.removeLayout.setBackgroundColor(this.removeBackGroundBlackColor);
                if (rootView != null) {
                    rootView.addView(this.removeLayout, 0, new LayoutParams(fullscreen.x, MinimizeAnimator.this.mRemoveLayoutHeight));
                }
                try {
                    if (Stub.asInterface(ServiceManager.getService("window")).isStatusBarVisible()) {
                        this.mTopMargin = MinimizeAnimator.this.mStatusBarHeight;
                    }
                } catch (RemoteException e) {
                }
                MarginLayoutParams removeLayoutParam = new MarginLayoutParams(this.removeLayout.getLayoutParams());
                removeLayoutParam.topMargin = this.mTopMargin;
                this.removeLayout.setLayoutParams(new FrameLayout.LayoutParams(removeLayoutParam));
                showTrashTranslateAnimation();
            }
            this.mIsShowingTrash = true;
            this.mNeedAnimation = true;
            WindowManager.LayoutParams windowAttributes = MinimizeAnimator.this.createLayoutParams(true);
            if (MinimizeAnimator.this.mWindow != null) {
                View decor = MinimizeAnimator.this.mWindow.getDecorView();
                if (decor != null) {
                    try {
                        MinimizeAnimator.this.mWindowManager.updateViewLayout(decor, windowAttributes);
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }

        void showTrashScaleAlphaAnimation() {
            AlphaAnimation showTrashAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
            showTrashAlphaAnim.setDuration(333);
            showTrashAlphaAnim.setInterpolator(new SineInOut33());
            showTrashAlphaAnim.setFillAfter(true);
            showTrashAlphaAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.v(MinimizeAnimator.TAG, "MinimizeAnimator::showTrashScaleAlphaAnimation() Start of AlphaAnimation");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.v(MinimizeAnimator.TAG, "MinimizeAnimator::showTrashScaleAlphaAnimation() End of AlphaAnimation");
                    }
                }
            });
            ScaleAnimation showTrashScaleUpAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
            showTrashScaleUpAnim.setDuration(333);
            showTrashScaleUpAnim.setInterpolator(new SineInOut90());
            showTrashScaleUpAnim.setFillAfter(true);
            showTrashScaleUpAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::showTrashScaleAlphaAnimation() Start of ScaleUpAnimation");
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::showTrashScaleAlphaAnimation() End of ScaleUpAnimation");
                    }
                }
            });
            AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(showTrashScaleUpAnim);
            animSet.addAnimation(showTrashAlphaAnim);
            animSet.setFillAfter(true);
            this.trashWithText.startAnimation(animSet);
        }

        private void changeRedCircleColor(boolean open) {
            changeRedCircleColor(open, false);
        }

        private void changeRedCircleColor(boolean open, boolean anim) {
            if (open) {
                if (this.mIsTrashOpen) {
                    return;
                }
            } else if (!this.mIsTrashOpen) {
                return;
            }
            if (anim) {
                int[] circleColors = new int[2];
                if (open) {
                    circleColors[0] = this.removeBackGroundBlackColor;
                    circleColors[1] = this.removeBackGroundRedColor;
                } else {
                    circleColors[0] = this.removeBackGroundRedColor;
                    circleColors[1] = this.removeBackGroundBlackColor;
                }
                ValueAnimator transAnim = ValueAnimator.ofArgb(circleColors);
                transAnim.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        TrashAnimationEffect.this.topBgFocusEffectDrawableItem.setColor(((Integer) animation.getAnimatedValue()).intValue());
                        TrashAnimationEffect.this.topBgFocusEffect.setImageDrawable(TrashAnimationEffect.this.topBgFocusEffectDrawable);
                    }
                });
                transAnim.setDuration(200);
                transAnim.start();
                return;
            }
            if (open) {
                this.topBgFocusEffectDrawableItem.setColor(this.removeBackGroundRedColor);
            } else {
                this.topBgFocusEffectDrawableItem.setColor(this.removeBackGroundBlackColor);
            }
            this.topBgFocusEffect.setImageDrawable(this.topBgFocusEffectDrawable);
        }

        private void bounceShowRedCircleAnimation(boolean open) {
            AlphaAnimation topEffectAlphaAnim;
            ScaleAnimation topEffectScaleAnim;
            final boolean openTrash = open;
            if (openTrash) {
                topEffectAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
                topEffectScaleAnim = new ScaleAnimation(0.75f, 1.0f, 0.75f, 1.0f, 1, 0.5f, 1, 0.5f);
            } else {
                topEffectAlphaAnim = new AlphaAnimation(1.0f, 0.0f);
                topEffectScaleAnim = new ScaleAnimation(1.0f, 0.75f, 1.0f, 0.75f, 1, 0.5f, 1, 0.5f);
            }
            topEffectAlphaAnim.setDuration(233);
            topEffectAlphaAnim.setInterpolator(new SineInOut33());
            topEffectAlphaAnim.setFillEnabled(true);
            topEffectAlphaAnim.setFillAfter(true);
            topEffectAlphaAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::bounceShowRedCircleAnimation() Start of AlphaAnimation, open=" + openTrash);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::bounceShowRedCircleAnimation() End of AlphaAnimation, open=" + openTrash);
                    }
                    if (!openTrash) {
                        TrashAnimationEffect.this.topBgFocusEffect.clearAnimation();
                        TrashAnimationEffect.this.topBgFocusEffect.setVisibility(8);
                    }
                }
            });
            topEffectScaleAnim.setDuration(223);
            topEffectScaleAnim.setFillEnabled(true);
            topEffectScaleAnim.setFillAfter(true);
            topEffectScaleAnim.setInterpolator(new SineInOut33());
            topEffectScaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::bounceShowRedCircleAnimation() Start of ScaleAnimation, open=" + openTrash);
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::bounceShowRedCircleAnimation() End of ScaleAnimation, open=" + openTrash);
                    }
                }
            });
            this.topBgFocusEffect.startAnimation(topEffectScaleAnim);
            this.topBgFocusEffectOuter.startAnimation(topEffectAlphaAnim);
        }

        boolean rangeCheck() {
            float right = MinimizeAnimator.this.mMinimizedIcon.getX() + ((float) MinimizeAnimator.this.mMinimizedIconDefaultSize);
            float bottom = MinimizeAnimator.this.mMinimizedIcon.getY() + ((float) MinimizeAnimator.this.mMinimizedIconDefaultSize);
            float compareDistanceHeight = (float) ((MinimizeAnimator.this.mMinimizedIconDefaultSize + this.mRemoveRangeRect.height()) / 2);
            float compareDistanceWidth = (float) ((MinimizeAnimator.this.mMinimizedIconDefaultSize + this.mRemoveRangeRect.width()) / 2);
            float distanceX = (MinimizeAnimator.this.mMinimizedIcon.getX() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2))) - (((float) this.mRemoveRangeRect.left) + ((float) (this.mRemoveRangeRect.width() / 2)));
            float distanceY = (MinimizeAnimator.this.mMinimizedIcon.getY() + ((float) (MinimizeAnimator.this.mMinimizedIconDefaultSize / 2))) - (((float) this.mRemoveRangeRect.top) + ((float) (this.mRemoveRangeRect.height() / 2)));
            if (distanceX < 0.0f) {
                distanceX *= WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            if (distanceY < 0.0f) {
                distanceY *= WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            if (distanceX > compareDistanceWidth || distanceY > compareDistanceHeight) {
                return false;
            }
            return true;
        }

        void showTrashTranslateAnimation() {
            TranslateAnimation anim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE, 1, 0.0f);
            anim.setDuration(300);
            this.removeLayout.setAnimation(anim);
            this.removeLayout.startAnimation(anim);
        }

        void hideTrash() {
            if (this.mIsShowingTrash) {
                this.mIsShowingTrash = false;
                this.removeLayout.setVisibility(8);
                WindowManager.LayoutParams windowAttributes = MinimizeAnimator.this.createLayoutParams(false);
                if (MinimizeAnimator.this.mWindow != null) {
                    View decor = MinimizeAnimator.this.mWindow.getDecorView();
                    if (decor != null) {
                        try {
                            MinimizeAnimator.this.mWindowManager.updateViewLayout(decor, windowAttributes);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        void openTrash(float touchedX, float touchedY) {
            if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                int currentDirection = touchedX < ((float) this.mRemoveLayoutWidth) / 2.0f ? 1 : 2;
                if (this.mShowTrashDirection != currentDirection) {
                    this.mNeedAnimation = true;
                    this.mShowTrashDirection = currentDirection;
                }
            } else {
                this.topBgFocusEffect.setVisibility(0);
                this.coveredTrash.setVisibility(4);
                this.trashCover.setVisibility(0);
                this.trashBody.setVisibility(0);
            }
            if (this.mNeedAnimation) {
                this.mNeedAnimation = false;
                if (!MinimizeAnimator.this.mIsSupportSimplificationUI) {
                    scaleTrashAnimation();
                } else if (this.mShowTrashDirection != 0) {
                    openRedCircleTrashCoverAnimation(this.mShowTrashDirection);
                }
            }
            this.mIsTrashOpen = true;
        }

        void closeTrash() {
            if (this.mIsTrashOpen) {
                if (MinimizeAnimator.this.mIsSupportSimplificationUI) {
                    closeRedCircleTrashCoverAnimation(this.mShowTrashDirection);
                } else {
                    this.coveredTrash.setVisibility(0);
                    this.trashCover.setVisibility(8);
                    this.topBgFocusEffect.setVisibility(8);
                    this.trashBody.setVisibility(8);
                    this.trash.clearAnimation();
                    this.trashCover.clearAnimation();
                }
                this.mNeedAnimation = true;
                this.mIsTrashOpen = false;
            } else if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::closeTrash() trash is already closed.");
            }
        }

        void throwAway() {
            try {
                if (MinimizeAnimator.this.mMinimizedIcon != null) {
                    removeMinimizedIconAnimation();
                    if (MinimizeAnimator.this.mHandler != null) {
                        MinimizeAnimator.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                TrashAnimationEffect.this.removeTrashAnimation();
                                TrashAnimationEffect.this.removeTrashTextAnimation();
                                TrashAnimationEffect.this.removeRedCircleAnimation();
                            }
                        }, 100);
                        MinimizeAnimator.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                TrashAnimationEffect.this.removeTrashCoverCloseAnimation();
                            }
                        }, 133);
                    }
                }
            } catch (IllegalStateException e) {
                MinimizeAnimator.this.mActivity.finish();
            }
        }

        void hideTrashScaleAlphaAnimation(boolean removeTask) {
            hideTrashScaleAlphaAnimation(removeTask, null);
        }

        void hideTrashScaleAlphaAnimation(final boolean removeTask, final Rect touchableRegion) {
            AlphaAnimation hideTrashAlphaAnim = new AlphaAnimation(1.0f, 0.0f);
            hideTrashAlphaAnim.setDuration(333);
            hideTrashAlphaAnim.setInterpolator(new SineInOut33());
            hideTrashAlphaAnim.setFillAfter(true);
            ScaleAnimation hideTrashScaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, 0.5f, 1, 0.5f);
            hideTrashScaleAnim.setDuration(333);
            hideTrashScaleAnim.setInterpolator(new SineInOut90());
            hideTrashScaleAnim.setFillAfter(true);
            hideTrashScaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::hideTrashScaleAlphaAnimation() Start of ScaleUpAnimation");
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::hideTrashScaleAlphaAnimation() End of ScaleUpAnimation");
                    }
                    if (!MinimizeAnimator.this.mIsTouchDown) {
                        TrashAnimationEffect.this.hideTrash();
                        TrashAnimationEffect.this.trash.clearAnimation();
                        TrashAnimationEffect.this.trashCover.clearAnimation();
                        if (removeTask) {
                            TrashAnimationEffect.this.trashText.clearAnimation();
                            TrashAnimationEffect.this.trashBodyForRemoveAnimation.clearAnimation();
                            MinimizeAnimator.this.hide();
                            MinimizeAnimator.this.mMultiWindowFacade.removeAllTasks(MinimizeAnimator.this.mToken, 4, true);
                        } else if (touchableRegion != null) {
                            MinimizeAnimator.this.setTouchableRegion(touchableRegion, 100);
                        }
                    }
                }
            });
            AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(hideTrashScaleAnim);
            animSet.addAnimation(hideTrashAlphaAnim);
            animSet.setFillAfter(true);
            this.trashWithText.startAnimation(animSet);
        }

        void cancelhideTrashForRemoveAnimation(boolean hideTrash) {
            if (MinimizeAnimator.this.mIsSupportSimplificationUI && this.removeLayout != null) {
                if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                    Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::cancelhideTrashForRemoveAnimation() animationCancelByMaximize=" + MinimizeAnimator.this.mAnimationCancelByMaximize);
                }
                if (hideTrash) {
                    hideTrash();
                } else {
                    MinimizeAnimator.this.mAnimationCancelByMaximize = true;
                }
            }
        }

        private void openRedCircleTrashCoverAnimation(int direction) {
            changeRedCircleColor(true, true);
            shakeRedCircleTrashCoverAnimation(direction, true);
            scaleRedCircleTrashCoverAnimation(true);
        }

        private void closeRedCircleTrashCoverAnimation(int direction) {
            changeRedCircleColor(false, true);
            shakeRedCircleTrashCoverAnimation(direction, false);
            scaleRedCircleTrashCoverAnimation(false);
        }

        private void shakeRedCircleTrashCoverAnimation(int direction, boolean open) {
            RotateAnimation rotateAnim;
            TranslateAnimation transAnim;
            int degreeByDirection = 15;
            if (direction == 2) {
                degreeByDirection = 15 * -1;
            }
            final boolean openTrash = open;
            if (open) {
                rotateAnim = new RotateAnimation(null, (float) degreeByDirection, 1, 0.5f, 1, 0.5f);
                transAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -0.05f);
            } else {
                RotateAnimation rotateAnimation = new RotateAnimation((float) degreeByDirection, 0.0f, 1, 0.5f, 1, 0.5f);
                transAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -0.05f, 1, 0.0f);
            }
            rotateAnim.setInterpolator(new SineInOut80());
            rotateAnim.setDuration(266);
            rotateAnim.setFillAfter(true);
            rotateAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::shakeRedCircleTrashCoverAnimation() Start of RotateAnimation, open=" + openTrash);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::shakeRedCircleTrashCoverAnimation() End RotateAnimation, open=" + openTrash);
                    }
                    if (!openTrash) {
                        TrashAnimationEffect.this.trash.clearAnimation();
                        TrashAnimationEffect.this.trashCover.clearAnimation();
                    }
                }
            });
            transAnim.setDuration(266);
            transAnim.setInterpolator(new SineInOut80());
            transAnim.setFillAfter(true);
            transAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::shakeRedCircleTrashCoverAnimation() Start of TranslateAnimation, open=" + openTrash);
                    }
                }

                public void onAnimationEnd(Animation arg0) {
                }

                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::shakeRedCircleTrashCoverAnimation() Start of TranslateAnimation, open=" + openTrash);
                    }
                }
            });
            final AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(transAnim);
            animSet.setFillAfter(true);
            if (MinimizeAnimator.this.mHandler != null) {
                MinimizeAnimator.this.mHandler.post(new Runnable() {
                    public void run() {
                        TrashAnimationEffect.this.trashCover.startAnimation(animSet);
                    }
                });
            }
        }

        private void scaleRedCircleTrashCoverAnimation(boolean open) {
            ScaleAnimation scaleAnim;
            final boolean openTrash = open;
            if (open) {
                scaleAnim = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, 1, 0.5f, 1, 0.5f);
            } else {
                scaleAnim = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f, 1, 0.5f, 1, 0.5f);
            }
            scaleAnim.setInterpolator(new SineInOut80());
            scaleAnim.setDuration(133);
            scaleAnim.setFillEnabled(true);
            scaleAnim.setFillAfter(true);
            scaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::scaleRedCircleTrashCoverAnimation() Start of ScaleAnimation, open=" + openTrash);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::scaleRedCircleTrashCoverAnimation() End ScaleAnimation, open=" + openTrash);
                    }
                }
            });
            if (MinimizeAnimator.this.mHandler != null) {
                MinimizeAnimator.this.mHandler.post(new Runnable() {
                    public void run() {
                        TrashAnimationEffect.this.trash.startAnimation(scaleAnim);
                    }
                });
            }
        }

        private void removeMinimizedIconAnimation() {
            int minimizeIconWidth = MinimizeAnimator.this.mMinimizedIcon.getWidth();
            int trashHeight = this.trashCover.getHeight();
            float originalX = MinimizeAnimator.this.mMinimizedIcon.getX();
            float originalY = MinimizeAnimator.this.mMinimizedIcon.getY();
            MinimizeAnimator.this.mMinimizedIcon.setLeft((int) originalX);
            MinimizeAnimator.this.mMinimizedIcon.setRight(((int) originalX) + minimizeIconWidth);
            MinimizeAnimator.this.mMinimizedIcon.setTop((int) originalY);
            MinimizeAnimator.this.mMinimizedIcon.setBottom(((int) originalY) + minimizeIconWidth);
            MinimizeAnimator.this.mMinimizedIcon.setX(originalX);
            MinimizeAnimator.this.mMinimizedIcon.setY(originalY);
            TranslateAnimation minimizeIconRemoveTransAnim = new TranslateAnimation(1, 0.0f, 1, ((((float) (this.removeLayout.getWidth() - minimizeIconWidth)) * 0.5f) - originalX) / ((float) minimizeIconWidth), 1, 0.0f, 1, ((((float) this.mTrashMargin) - (((float) trashHeight) * 0.35f)) - originalY) / ((float) minimizeIconWidth));
            minimizeIconRemoveTransAnim.setDuration(266);
            minimizeIconRemoveTransAnim.setInterpolator(new SineInOut90());
            minimizeIconRemoveTransAnim.setFillEnabled(true);
            minimizeIconRemoveTransAnim.setFillAfter(true);
            minimizeIconRemoveTransAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                }
            });
            ScaleAnimation minimizeIconRemoveScaleAnim = new ScaleAnimation(1.0f, 0.45f, 1.0f, 0.45f, 1, 0.5f, 1, 0.5f);
            minimizeIconRemoveScaleAnim.setInterpolator(new SineInOut33());
            minimizeIconRemoveScaleAnim.setDuration(266);
            minimizeIconRemoveScaleAnim.setFillEnabled(true);
            minimizeIconRemoveScaleAnim.setFillAfter(true);
            minimizeIconRemoveScaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeMinimizedIconAnimation() Start of ScaleAnimation");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeMinimizedIconAnimation() End of ScaleAnimation");
                    }
                }
            });
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(266);
            alphaAnimation.setInterpolator(new SineInOut33());
            alphaAnimation.setFillEnabled(true);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.v(MinimizeAnimator.TAG, "MinimizeAnimator::removeMinimizedIconAnimation() Start of AlphaAnimation");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.v(MinimizeAnimator.TAG, "MinimizeAnimator::removeMinimizedIconAnimation() End of AlphaAnimation");
                    }
                    MinimizeAnimator.this.mMinimizedIcon.clearAnimation();
                    MinimizeAnimator.this.mMinimizedIcon.setVisibility(4);
                }
            });
            Animation animationSet = new AnimationSet(false);
            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(minimizeIconRemoveScaleAnim);
            animationSet.addAnimation(minimizeIconRemoveTransAnim);
            MinimizeAnimator.this.mMinimizedIcon.startAnimation(animationSet);
        }

        private void removeTrashAnimation() {
            TranslateAnimation removeTrashTransAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 0.25f);
            removeTrashTransAnim.setDuration(266);
            removeTrashTransAnim.setFillEnabled(true);
            removeTrashTransAnim.setFillAfter(true);
            removeTrashTransAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashAnimation() Start of TranslateAnimation");
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashAnimation() End of TranslateAnimation");
                    }
                }
            });
            ScaleAnimation removeTrashScaleMaintainAnim = new ScaleAnimation(1.1f, 1.1f, 1.1f, 1.1f, 1, 0.5f, 1, 0.5f);
            removeTrashScaleMaintainAnim.setDuration(333);
            removeTrashScaleMaintainAnim.setFillEnabled(true);
            removeTrashScaleMaintainAnim.setFillAfter(true);
            removeTrashScaleMaintainAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashAnimation() Start of ScaleMaintainAnimation");
                    }
                    if (MinimizeAnimator.this.mHandler != null) {
                        MinimizeAnimator.this.mHandler.post(new Runnable() {
                            public void run() {
                                TrashAnimationEffect.this.removeTrashBodyFrameAnimation();
                            }
                        });
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashAnimation() End of ScaleMaintainAnimation");
                    }
                    TrashAnimationEffect.this.hideTrashScaleAlphaAnimation(true);
                }
            });
            AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(removeTrashScaleMaintainAnim);
            animSet.addAnimation(removeTrashTransAnim);
            animSet.setFillAfter(true);
            this.trash.startAnimation(animSet);
        }

        private void removeTrashTextAnimation() {
            AlphaAnimation trashTextAnim = new AlphaAnimation(1.0f, 0.0f);
            trashTextAnim.setDuration(233);
            trashTextAnim.setInterpolator(new SineInOut80());
            trashTextAnim.setFillEnabled(true);
            trashTextAnim.setFillAfter(true);
            trashTextAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashTextAnimation() Start of AlphaAnimation");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removetrashTextAnimation() End of AlphaAnimation");
                    }
                }
            });
            this.trashText.startAnimation(trashTextAnim);
        }

        private void removeRedCircleAnimation() {
            AlphaAnimation topEffectRemoveAlphaAnim = new AlphaAnimation(0.4f, 0.0f);
            topEffectRemoveAlphaAnim.setDuration(333);
            topEffectRemoveAlphaAnim.setInterpolator(new SineInOut33());
            topEffectRemoveAlphaAnim.setFillEnabled(true);
            topEffectRemoveAlphaAnim.setFillAfter(true);
            topEffectRemoveAlphaAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeRedCircleAnimation() Start of AlphaAnimation ");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeRedCircleAnimation() End of AlphaAnimation");
                    }
                }
            });
            ScaleAnimation topEffectRemoveScaleAnim = new ScaleAnimation(1.0f, 0.75f, 1.0f, 0.75f, 1, 0.5f, 1, 0.5f);
            topEffectRemoveScaleAnim.setDuration(333);
            topEffectRemoveScaleAnim.setInterpolator(new SineInOut80());
            topEffectRemoveScaleAnim.setFillAfter(true);
            topEffectRemoveScaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeRedCircleAnimation() Start of ScaleUpAnimation");
                    }
                }

                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeRedCircleAnimation() End of ScaleUpAnimation");
                    }
                }
            });
            AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(topEffectRemoveAlphaAnim);
            animSet.addAnimation(topEffectRemoveScaleAnim);
            animSet.setFillAfter(true);
            this.topBgFocusEffect.startAnimation(animSet);
        }

        private void removeTrashBodyFrameAnimation() {
            if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashBodyFrameAnimation() Start of FrameAnimation");
            }
            this.mFrameTrashBodyAnim = null;
            this.mFrameTrashBodyAnim = (AnimationDrawable) this.trashBodyForRemoveAnimation.getBackground();
            this.mFrameTrashBodyAnim.setOneShot(true);
            this.mFrameTrashBodyAnim.start();
        }

        private void removeTrashCoverCloseAnimation() {
            int degreeByDirection = 15;
            if (this.mShowTrashDirection == 2) {
                degreeByDirection = 15 * -1;
            }
            RotateAnimation removeTrashRotateAnim = new RotateAnimation((float) degreeByDirection, 0.0f, 1, 0.5f, 1, 0.5f);
            removeTrashRotateAnim.setInterpolator(new SineInOut80());
            removeTrashRotateAnim.setDuration(100);
            removeTrashRotateAnim.setFillEnabled(true);
            removeTrashRotateAnim.setFillAfter(true);
            removeTrashRotateAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashCoverCloseAnimation() Start of RotateAnimation");
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashCoverCloseAnimation() End RotateAnimation");
                    }
                }
            });
            TranslateAnimation removeTrashTransAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -0.05f, 1, 0.0f);
            removeTrashTransAnim.setDuration(100);
            removeTrashTransAnim.setInterpolator(new SineInOut80());
            removeTrashTransAnim.setFillAfter(true);
            removeTrashTransAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashCoverCloseAnimation() Start of TranslateAnimation");
                    }
                }

                public void onAnimationEnd(Animation arg0) {
                }

                public void onAnimationStart(Animation arg0) {
                    if (MinimizeAnimator.DEBUG_MINIMIZE_REMOVE_ANIM) {
                        Log.d(MinimizeAnimator.TAG, "MinimizeAnimator::removeTrashCoverCloseAnimation() Start of TranslateAnimation");
                    }
                }
            });
            AnimationSet animSet = new AnimationSet(false);
            animSet.addAnimation(removeTrashRotateAnim);
            animSet.addAnimation(removeTrashTransAnim);
            animSet.setFillAfter(true);
            this.trashCover.startAnimation(animSet);
        }

        private void scaleTrashAnimation() {
            ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, 1, 0.5f, 1, 0.5f);
            scaleAnim.setDuration(250);
            scaleAnim.setFillAfter(true);
            scaleAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    TrashAnimationEffect.this.openTrashCoverAnimation();
                }

                public void onAnimationStart(Animation arg0) {
                }
            });
            this.trash.startAnimation(scaleAnim);
        }

        private void openTrashCoverAnimation() {
            TranslateAnimation transAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -0.05f);
            transAnim.setDuration(250);
            transAnim.setFillEnabled(true);
            transAnim.setFillAfter(true);
            transAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    TrashAnimationEffect.this.shakeTrashCoverAnimation();
                }

                public void onAnimationStart(Animation arg0) {
                }
            });
            this.trashCover.startAnimation(transAnim);
        }

        private void shakeTrashCoverAnimation() {
            RotateAnimation rotateAnim = new RotateAnimation(-5.0f, 5.0f, 1, 0.5f, 1, MultiWindowFacade.SPLIT_MAX_WEIGHT);
            rotateAnim.setDuration(60);
            rotateAnim.setRepeatCount(2);
            rotateAnim.setRepeatMode(2);
            rotateAnim.setFillAfter(true);
            rotateAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    TrashAnimationEffect.this.closeTrashCoverAnimation();
                }

                public void onAnimationStart(Animation arg0) {
                }
            });
            this.trashCover.startAnimation(rotateAnim);
        }

        private void closeTrashCoverAnimation() {
            TranslateAnimation transAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -0.05f, 1, 0.0f);
            transAnim.setDuration(500);
            transAnim.setFillAfter(true);
            transAnim.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation arg0) {
                }

                public void onAnimationEnd(Animation arg0) {
                    TrashAnimationEffect.this.shakeTrashCoverAnimation();
                }

                public void onAnimationStart(Animation arg0) {
                }
            });
            this.trashCover.startAnimation(transAnim);
        }
    }

    static {
        if (MultiPhoneWindow.DEBUG_MINIMIZE_ANIM) {
        }
    }

    public MinimizeAnimator(Context context, MultiPhoneWindow multiPhoneWindow, float dssScale) {
        this.mContext = context;
        this.mDssScale = dssScale;
        if (this.mContext instanceof Activity) {
            this.mActivity = (Activity) this.mContext;
        }
        this.mToken = this.mActivity.getActivityToken();
        this.mMultiPhoneWindow = multiPhoneWindow;
        this.mDocking = multiPhoneWindow.getDockingInstance();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mMultiWindowFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
        this.mIsSupportSimplificationUI = MultiWindowFeatures.isSupportSimplificationUI(this.mContext);
        this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        this.mRemoveLayoutHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_remove_layout_height);
        this.mMinimizedIconDefaultSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_minimized_height);
        int i = this.mMinimizedIconDefaultSize;
        this.mMinimizedIconWidth = i;
        this.mMinimizedIconHeight = i;
        this.mRemoveLayoutHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_remove_layout_height);
        this.mTrashAnimationEffect = new TrashAnimationEffect();
    }

    public void makeMinimizeIconWindow(View view) {
        makeMinimizeIconWindow(view, null);
    }

    public void makeMinimizeIconWindow(ApplicationThumbnail thumbnail) {
        makeMinimizeIconWindow(null, thumbnail);
    }

    public void makeMinimizeIconWindow() {
        makeMinimizeIconWindow(null, null);
    }

    private void makeMinimizeIconWindow(View view, ApplicationThumbnail thumbnail) {
        this.mWindow = addWindow();
        if (this.mWindow != null) {
            this.mReadyToShow = true;
            if (view == null) {
                Drawable drawable;
                this.mMinimizedIcon = new ImageView(this.mContext);
                int i = this.mMinimizedIconDefaultSize;
                this.mMinimizedIconWidth = i;
                this.mMinimizedIconHeight = i;
                if (thumbnail == null) {
                    this.mThumbnail = ApplicationThumbnail.create(this.mActivity);
                } else {
                    this.mThumbnail = thumbnail;
                }
                if (this.mThumbnail.isUsedTheme()) {
                    this.mThumbnail.setCustomMinimizeIcon(this.mThumbnail.loadIconForResolveTheme());
                }
                if (PersonaManager.isKnoxId(this.mContext.getUserId())) {
                    drawable = ((PersonaManager) this.mContext.getSystemService("persona")).getCustomBadgedIconifRequired(this.mContext, this.mThumbnail.getIcon(), "multiwindow", new UserHandle(this.mContext.getUserId()), 3);
                } else {
                    drawable = this.mThumbnail.getIcon();
                }
                ((ImageView) this.mMinimizedIcon).setImageDrawable(drawable);
                this.mMinimizedIcon.setContentDescription(this.mThumbnail.getLabel());
            } else {
                view.measure(0, 0);
                this.mMinimizedIconWidth = view.getMeasuredWidth();
                this.mMinimizedIconHeight = view.getMeasuredHeight();
                this.mMinimizedIcon = view;
            }
            if (this.mMinimizedWindowListener == null) {
                this.mMinimizedWindowListener = new MinimizedWindowListener();
            }
            this.mMinimizedIcon.setOnTouchListener(this.mMinimizedWindowListener);
            this.mMinimizedIcon.setOnLongClickListener(this.mMinimizedWindowListener);
            this.mMinimizedIcon.setOnDragListener(new ContentFrameDragListener());
            this.mMinimizedIcon.setHapticFeedbackEnabled(false);
            Rect initPosition = this.mMultiPhoneWindow.getStackBound();
            this.mLastRotation = this.mMultiPhoneWindow.getRotationOfStackBound();
            this.mInitPositionX = ((int) (((float) initPosition.left) * this.mDssScale)) + ((((int) (((float) initPosition.width()) * this.mDssScale)) - this.mMinimizedIconWidth) / 2);
            this.mInitPositionY = ((int) (((float) initPosition.top) * this.mDssScale)) - (this.mMinimizedIconHeight / 2);
            if (this.mInitPositionY < this.mStatusBarHeight) {
                this.mInitPositionY = this.mStatusBarHeight;
            }
            this.mMinimizedIcon.setX((float) this.mInitPositionX);
            this.mMinimizedIcon.setY((float) this.mInitPositionY);
            this.mWindow.addContentView(this.mMinimizedIcon, new LayoutParams(this.mMinimizedIconWidth, this.mMinimizedIconHeight));
            this.mWindow.getDecorView().resetResolvedLayoutDirection();
            this.mWindow.getDecorView().setLayoutDirection(0);
            show();
            float pivotX = ((float) this.mInitPositionX) + (((float) this.mMinimizedIconWidth) / 2.0f);
            float pivotY = ((float) this.mInitPositionY) + (((float) this.mMinimizedIconHeight) / 2.0f);
            Animation a = makeAnimation(pivotX, pivotY);
            a.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (MinimizeAnimator.DEBUG) {
                    }
                    if (MinimizeAnimator.this.mMultiWindowFacade != null) {
                        MinimizeAnimator.this.mMultiWindowFacade.appMinimizingStarted(MinimizeAnimator.this.mToken);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                    if (!MinimizeAnimator.DEBUG) {
                    }
                }

                public void onAnimationEnd(Animation animation) {
                    if (!(MinimizeAnimator.this.mWindow == null || MinimizeAnimator.this.mWindow.getDecorView() == null || !MinimizeAnimator.this.mReadyToShow)) {
                        MinimizeAnimator.this.mReadyToShow = false;
                        MinimizeAnimator.this.setTouchableRegion(new Rect(MinimizeAnimator.this.mInitPositionX, MinimizeAnimator.this.mInitPositionY, MinimizeAnimator.this.mInitPositionX + MinimizeAnimator.this.mMinimizedIconWidth, MinimizeAnimator.this.mInitPositionY + MinimizeAnimator.this.mMinimizedIconHeight), 0);
                    }
                    if (MinimizeAnimator.DEBUG) {
                    }
                    if (MinimizeAnimator.this.mActivity != null && MinimizeAnimator.this.mActivity.getMultiWindowStyle().isEnabled(131072)) {
                        ActivityThread am = ActivityThread.currentActivityThread();
                        if (am != null) {
                            am.forceStopActivity(MinimizeAnimator.this.mToken);
                            am.forceRestartActivity(MinimizeAnimator.this.mToken);
                            if (MinimizeAnimator.DEBUG) {
                                Log.d(MinimizeAnimator.TAG, "onAnimationEnd, force stop/restart, componentName=" + MinimizeAnimator.this.mActivity.getComponentName());
                            }
                        }
                    }
                }
            });
            this.mMinimizedIcon.startAnimation(a);
            if (DEBUG) {
                Log.d(TAG, "makeMinimizeIconWindow componentName=" + this.mActivity.getComponentName());
                Log.d(TAG, "makeMinimizeIconWindow StackBound=" + initPosition + " X=" + this.mInitPositionX + " Y=" + this.mInitPositionY + " pivotX=" + pivotX + " pivotY=" + pivotY);
            }
        }
    }

    private Window addWindow() {
        if (this.mActivity == null || this.mActivity.isWindowAdded()) {
            WindowManager.LayoutParams newWindowAttributes;
            Window newWindow;
            View newDecor;
            if (this.mWindow != null) {
                if (DEBUG) {
                    Log.d(TAG, "MinimizeAnimator::addWindow(), MinimizeAnimator already has mWindow, mWindow should be recreated, token=" + this.mToken);
                }
                removeWindow();
            }
            if (DEBUG) {
                newWindowAttributes = createLayoutParams(false);
                newWindow = new PhoneWindow(this.mContext);
                newWindow.requestFeature(1);
                newWindow.setWindowManager(this.mWindowManager, null, null);
                newWindow.setBackgroundDrawable(new ColorDrawable(0));
                newDecor = newWindow.getDecorView();
                this.mContentRootContainer = new ContentRootContainer(this.mContext);
            } else {
                newWindowAttributes = createLayoutParams(false);
                newWindow = new PhoneWindow(this.mContext);
                newWindow.requestFeature(1);
                newWindow.setWindowManager(this.mWindowManager, null, null);
                newWindow.setBackgroundDrawable(new ColorDrawable(0));
                newDecor = newWindow.getDecorView();
                this.mContentRootContainer = new ContentRootContainer(this.mContext);
            }
            for (int i = 0; i < ((ViewGroup) newDecor).getChildCount(); i++) {
                View content = ((ViewGroup) newDecor).getChildAt(i);
                ((ViewGroup) newDecor).removeView(content);
                this.mContentRootContainer.addView(content);
            }
            ((ViewGroup) newDecor).addView(this.mContentRootContainer, new LayoutParams(-1, -1));
            newDecor.setLayoutDirection(0);
            this.mWindowManager.addView(newDecor, newWindowAttributes);
            return newWindow;
        }
        if (DEBUG) {
            Log.w(TAG, "Skip add MinimizeAnimator Window, DecorWindow is not added, token=" + this.mToken);
        }
        return null;
    }

    public void removeWindow() {
        removeWindow(false);
    }

    public void removeWindow(boolean immediate) {
        if (DEBUG) {
            Log.d(TAG, "removeWindow mWindow=" + this.mWindow + ", immediate=" + immediate + ", caller=" + Debug.getCallers(3));
        }
        if (this.mWindow != null) {
            View decorView = this.mWindow.getDecorView();
            if (decorView.getVisibility() == 0) {
                hide();
            }
            ((FrameLayout) decorView.findViewById(16908290)).removeView(this.mMinimizedIcon);
            if (immediate) {
                this.mWindowManager.removeViewImmediate(decorView);
            } else {
                this.mWindowManager.removeView(decorView);
            }
            this.mWindow = null;
        }
        if (this.mMinimizedIcon != null) {
            this.mMinimizedIcon.setOnTouchListener(null);
            this.mMinimizedIcon.setOnLongClickListener(null);
            this.mMinimizedIcon.setOnDragListener(null);
            this.mMinimizedIcon = null;
        }
        if (this.mThumbnail != null) {
            this.mThumbnail = null;
        }
    }

    public boolean isMinimizeIconVisible() {
        return this.mWindow != null && this.mWindow.getDecorView().getVisibility() == 0;
    }

    private void show() {
        if (DEBUG) {
            Log.d(TAG, HardwareRenderer.OVERDRAW_PROPERTY_SHOW);
        }
        if (this.mWindow != null && this.mWindow.getDecorView() != null) {
            Point displaySize = new Point();
            getDisplaySize(displaySize);
            this.mTouchableRegion.set(0, 0, displaySize.x, displaySize.y);
            ViewTreeObserver viewTreeObserver = this.mWindow.getDecorView().getRootView().getViewTreeObserver();
            viewTreeObserver.removeOnComputeInternalInsetsListener(this.mInsetsComputer);
            viewTreeObserver.addOnComputeInternalInsetsListener(this.mInsetsComputer);
            if (this.mWindow.getDecorView().getVisibility() != 0) {
                this.mWindow.getDecorView().setVisibility(0);
            }
        }
    }

    private void hide() {
        if (DEBUG) {
            Log.d(TAG, "hide");
        }
        if (this.mWindow != null && this.mWindow.getDecorView() != null) {
            this.mWindow.getDecorView().getRootView().getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsComputer);
            this.mWindow.getDecorView().setVisibility(4);
        }
    }

    private WindowManager.LayoutParams createLayoutParams(boolean showTrash) {
        WindowManager.LayoutParams lp;
        if (DEBUG) {
            lp = new WindowManager.LayoutParams(2, 16778536, -3);
            lp.privateFlags |= 16;
            lp.multiWindowFlags |= 1545;
        } else {
            lp = new WindowManager.LayoutParams(2, 16778536, -3);
            lp.privateFlags |= 16;
            lp.multiWindowFlags |= 1545;
        }
        if (this.mIsSupportSimplificationUI && showTrash) {
            lp.multiWindowFlags |= 2048;
        }
        lp.setTitle("MinimizeAnimator " + this.mActivity.getComponentName().getPackageName() + "/" + this.mActivity.getComponentName().getClassName());
        lp.width = -1;
        lp.height = -1;
        lp.softInputMode = 48;
        lp.hasManualSurfaceInsets = true;
        return lp;
    }

    private AnimationSet makeAnimation(float pivotX, float pivotY) {
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 0, pivotX, 0, pivotY);
        scale.setDuration(250);
        scale.setInterpolator(new SineInOut80());
        animationSet.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
        alpha.setDuration(250);
        alpha.setInterpolator(new SineInOut70());
        animationSet.addAnimation(alpha);
        return animationSet;
    }

    private void setTouchableRegion(Rect region, int delayedTime) {
        if (this.mMinimizedIcon != null) {
            if (DEBUG) {
                Log.d(TAG, "setTouchableRegion() touchableRegion=" + region + ", delaytedTime=" + delayedTime + ", caller=" + Debug.getCallers(3));
            }
            final Rect touchableRegion = region;
            if (delayedTime > 0) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (MinimizeAnimator.this.mMinimizedIcon != null) {
                            Rect minimizedBound = new Rect();
                            MinimizeAnimator.this.adjustMinimizedBoundary(touchableRegion, minimizedBound);
                            MinimizeAnimator.this.mMinimizedIcon.setX((float) minimizedBound.left);
                            MinimizeAnimator.this.mMinimizedIcon.setY((float) minimizedBound.top);
                            MinimizeAnimator.this.mAnimating = false;
                            MinimizeAnimator.this.mTouchableRegion.set(minimizedBound);
                            MinimizeAnimator.this.mMinimizedIcon.requestLayout();
                        }
                    }
                }, (long) delayedTime);
                return;
            }
            this.mTouchableRegion.set(touchableRegion);
            this.mMinimizedIcon.requestLayout();
        }
    }

    private boolean getDisplaySize(Point outbound) {
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        if (this.mMultiWindowFacade != null) {
            DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
            if (displayInfo != null) {
                Rect rect = new Rect();
                this.mMultiWindowFacade.getRealSize(rect, displayInfo);
                outbound.x = rect.width();
                outbound.y = rect.height();
                return true;
            }
        }
        if (display == null) {
            return false;
        }
        display.getSize(outbound);
        return true;
    }

    private int getScreenRotation() {
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        if (this.mMultiWindowFacade != null) {
            DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
            if (displayInfo != null) {
                return displayInfo.rotation;
            }
        }
        if (display != null) {
            return display.getRotation();
        }
        return 0;
    }

    private boolean isOutOfDisplay(Rect bound) {
        Point ds = new Point();
        getDisplaySize(ds);
        return new Rect(0, this.mStatusBarHeight, ds.x, ds.y).contains(bound);
    }

    private boolean adjustMinimizedBoundary(Rect stackBound, Rect out) {
        boolean outOfBound = false;
        Point screenSize = new Point();
        getDisplaySize(screenSize);
        int left = stackBound.left + ((stackBound.width() - this.mMinimizedIconWidth) / 2);
        int top = stackBound.top;
        int right = left + this.mMinimizedIconWidth;
        int bottom = top + this.mMinimizedIconHeight;
        out.set(left, top, right, bottom);
        if (left < 0) {
            out.offset(-out.left, 0);
            outOfBound = true;
        } else if (right > screenSize.x) {
            out.offset(screenSize.x - right, 0);
            outOfBound = true;
        }
        if (bottom > screenSize.y) {
            out.offset(0, screenSize.y - bottom);
            outOfBound = true;
        } else if (top < this.mStatusBarHeight) {
            out.offset(0, this.mStatusBarHeight - out.top);
            outOfBound = true;
        }
        if (DEBUG) {
            Log.d(TAG, "adjustMinimizedBoundary StackBound=" + stackBound + " OutBound=" + out);
        }
        return outOfBound;
    }

    private void updateRotationChanged(Rect stackBound) {
        int currentRotation = getScreenRotation();
        int delta = currentRotation - this.mLastRotation;
        if (delta < 0) {
            delta += 4;
        }
        this.mLastRotation = currentRotation;
        Point screenSize = new Point();
        getDisplaySize(screenSize);
        Rect displaySize = new Rect(0, 0, screenSize.x, screenSize.y);
        int left = (int) this.mMinimizedIcon.getX();
        int top = (int) this.mMinimizedIcon.getY();
        Rect lastMinimizedBound = new Rect(left, top, this.mMinimizedIconWidth + left, this.mMinimizedIconHeight + top);
        Rect currentMinimizedBound = new Rect();
        switch (delta) {
            case 0:
                int x = ((int) (((float) stackBound.left) * this.mDssScale)) + ((((int) (((float) stackBound.width()) * this.mDssScale)) - this.mMinimizedIconWidth) / 2);
                int y = ((int) (((float) stackBound.top) * this.mDssScale)) - (this.mMinimizedIconHeight / 2);
                if (y < this.mStatusBarHeight) {
                    y = this.mStatusBarHeight;
                }
                currentMinimizedBound.set(x, y, this.mMinimizedIconWidth + x, this.mMinimizedIconHeight + y);
                break;
            case 1:
                currentMinimizedBound.top = displaySize.bottom - lastMinimizedBound.right;
                currentMinimizedBound.left = lastMinimizedBound.top;
                currentMinimizedBound.right = currentMinimizedBound.left + lastMinimizedBound.height();
                currentMinimizedBound.bottom = currentMinimizedBound.top + lastMinimizedBound.width();
                break;
            case 2:
                currentMinimizedBound.top = displaySize.bottom - lastMinimizedBound.bottom;
                currentMinimizedBound.left = displaySize.right - lastMinimizedBound.right;
                currentMinimizedBound.right = currentMinimizedBound.left + lastMinimizedBound.width();
                currentMinimizedBound.bottom = currentMinimizedBound.top + lastMinimizedBound.height();
                break;
            case 3:
                currentMinimizedBound.top = lastMinimizedBound.left;
                currentMinimizedBound.left = displaySize.right - lastMinimizedBound.bottom;
                currentMinimizedBound.right = currentMinimizedBound.left + lastMinimizedBound.height();
                currentMinimizedBound.bottom = currentMinimizedBound.top + lastMinimizedBound.width();
                break;
        }
        if (this.mReadyToShow) {
            Log.d(TAG, "updateRotationChanged : ready to show");
            this.mReadyToShow = false;
        }
        this.mMinimizedIcon.setX((float) currentMinimizedBound.left);
        this.mMinimizedIcon.setY((float) currentMinimizedBound.top);
        setTouchableRegion(currentMinimizedBound, 0);
        this.mMultiPhoneWindow.moveStackBound((int) (this.mMinimizedIcon.getX() - ((float) (stackBound.left + ((stackBound.width() - this.mMinimizedIconWidth) / 2)))), (int) (this.mMinimizedIcon.getY() - ((float) (stackBound.top - (this.mMinimizedIconHeight / 2)))), false);
        if (DEBUG) {
            Log.d(TAG, "updateRotationChanged rotation=" + delta + " Minimized Bound=" + currentMinimizedBound);
        }
    }

    public void updateMultiWindowConfigurationChanged() {
        if (this.mMinimizedIcon != null) {
        }
    }

    public void updateMultiWindowStyleChanged(Rect stackBound) {
        if (DEBUG) {
            Log.d(TAG, "updateMultiWindowStyleChanged StackBound=" + stackBound);
        }
        if (this.mMinimizedIcon != null) {
            if (stackBound == null) {
                removeWindow(true);
                return;
            }
            this.mTrashAnimationEffect.hideTrash();
            updateRotationChanged(stackBound);
        }
    }

    public void setDragAndDropMode(boolean mode) {
        this.mDragMode = mode;
    }
}
