package android.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.DisplayInfo;
import android.view.HapticFeedbackConstants;
import android.view.IWindowManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import com.android.internal.R;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.samsung.android.cover.ICoverManager.Stub;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.smartface.SmartFaceManager;

public class HoverPopupWindow {
    private static final String AIRCOMMAND_MORPH_USP = SystemProperties.get("ro.aircommand.morph.usp");
    static final boolean DEBUG = false;
    private static final String DEVICE_TYPE = SystemProperties.get("ro.build.characteristics");
    private static final int HOVER_DETECT_TIME_MS = 300;
    private static final int MSG_DISMISS_POPUP = 2;
    private static final int MSG_SHOW_POPUP = 1;
    private static final int MSG_TIMEOUT = 1;
    private static final int POPUP_TIMEOUT_MS = 10000;
    static final String TAG = "HoverPopupWindow";
    private static final int TIMEOUT_DELAY = 500;
    private static final int TIMEOUT_DELAY_LONG = 2000;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_TOOLTIP = 1;
    public static final int TYPE_USER_CUSTOM = 3;
    public static final int TYPE_WIDGET_DEFAULT = 2;
    private static final int UI_THREAD_BUSY_TIME_MS = 1000;
    private final int ANCHORVIEW_COORDINATES_TYPE_NONE;
    private final int ANCHORVIEW_COORDINATES_TYPE_SCREEN;
    private final int ANCHORVIEW_COORDINATES_TYPE_WINDOW;
    private float H;
    private final int ID_TOOLTIP_VIEW;
    private final int MARGIN_FOR_HOVER_RING;
    private int MOVE_CENTER;
    private int MOVE_LEFT;
    private int MOVE_LEFT_TO_CENTER;
    private int MOVE_RIGHT;
    private int MOVE_RIGHT_TO_CENTER;
    private final int SHOW_ANIMATION_DURATION;
    private float TW;
    private float W;
    private Rect mAnchorRect;
    private View mAnchorView;
    protected int mAnimationStyle;
    private PointF mCenterPoint;
    private int mContainerLeftOnWindow;
    private HoverPopupContainer mContentContainer;
    private int mContentHeight;
    private LayoutParams mContentLP;
    private int mContentResId;
    protected CharSequence mContentText;
    protected View mContentView;
    private int mContentWidth;
    private final Context mContext;
    private int mCoordinatesOfAnchorView;
    private ICoverManager mCoverManager;
    private int mDirection;
    protected Handler mDismissHandler;
    private Runnable mDismissPopupRunnable;
    private boolean mDismissTouchableHPWOnActionUp;
    private Rect mDisplayFrame;
    private int mDisplayFrameLeft;
    private int mDisplayFrameRight;
    private int mDisplayWidthToComputeAniWidth;
    private boolean mEnabled;
    private float mFontScale;
    private int mFullTextPopupRightLimit;
    private int mGuideLineColor;
    protected int mGuideLineFadeOffset;
    private int mGuideRingDrawableId;
    private Handler mHandler;
    private int mHashCodeForViewState;
    protected int mHoverDetectTimeMS;
    private int mHoverPaddingBottom;
    private int mHoverPaddingLeft;
    private int mHoverPaddingRight;
    private int mHoverPaddingTop;
    private int mHoveringPointX;
    private int mHoveringPointY;
    private boolean mIsFHAnimationEnabled;
    private boolean mIsFHAnimationEnabledByApp;
    private boolean mIsFHGuideLineEnabled;
    private boolean mIsFHGuideLineEnabledByApp;
    private boolean mIsFHSoundAndHapticEnabled;
    protected boolean mIsGuideLineEnabled;
    private boolean mIsHoverPaddingEnabled;
    private boolean mIsInfoPickerMoveEabled;
    private boolean mIsInfoPickerMoveEabledByApp;
    private boolean mIsPopupTouchable;
    private boolean mIsProgressBar;
    private boolean mIsSPenPointChanged;
    private boolean mIsSetInfoPickerColorToAndMoreBottomImg;
    private boolean mIsShowMessageSent;
    private boolean mIsSkipPenPointEffect;
    private boolean mIsTryingShowPopup;
    private PointF mLeftPoint;
    private HoverPopupListener mListener;
    private boolean mNeedToMeasureContentView;
    private boolean mOverTopBoundary;
    protected final View mParentView;
    private Point mPenWindowStartPos;
    private float mPickerPadding;
    private int mPickerXoffset;
    private PopupWindow mPopup;
    protected int mPopupGravity;
    private int mPopupOffsetX;
    private int mPopupOffsetY;
    protected int mPopupPosX;
    protected int mPopupPosY;
    protected int mPopupType;
    private HoverPopupPreShowListener mPreShowListener;
    private Rect mReferncedAnchorRect;
    private PointF mRightPoint;
    protected boolean mShowPopupAlways;
    private Runnable mShowPopupRunnable;
    private int mToolType;
    private TouchablePopupContainer mTouchableContainer;
    private int mUspLevel;
    private int mWindowGapX;
    private int mWindowGapY;
    private boolean misDialer;
    private boolean misGravityBottomUnder;
    private float objAnimationValue;
    private ValueAnimator objAnimator;

    public static final class Gravity {
        public static final int BOTTOM = 80;
        public static final int BOTTOM_UNDER = 20560;
        public static final int CENTER = 17;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_HORIZONTAL_ON_POINT = 513;
        public static final int CENTER_HORIZONTAL_ON_WINDOW = 257;
        public static final int CENTER_VERTICAL = 16;
        public static final int HORIZONTAL_GRAVITY_MASK = 3855;
        public static final int LEFT = 3;
        public static final int LEFT_CENTER_AXIS = 259;
        public static final int LEFT_OUTSIDE = 771;
        public static final int NO_GRAVITY = 0;
        public static final int RIGHT = 5;
        public static final int RIGHT_CENTER_AXIS = 261;
        public static final int RIGHT_OUTSIDE = 1285;
        public static final int TOP = 48;
        public static final int TOP_ABOVE = 12336;
        public static final int VERTICAL_GRAVITY_MASK = 61680;
    }

    private class HoverPopupContainer extends FrameLayout {
        static final boolean DEBUG = false;
        static final String TAG = "HoverPopupContainer";
        private final float DEFAULT_BG_OUTLINE_THICKNESS = 1.5f;
        private final float DEFAULT_BG_PADDING = 10.0f;
        private int POPUPSTATE_CENTER = 2;
        private int POPUPSTATE_LEFT = 1;
        private int POPUPSTATE_RIGHT = 0;
        private Animation ani = null;
        private boolean isFHmoveAnimation = false;
        private int mAnimationAreaOffset = 100;
        private float mBGPaddingBottomPX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        private float mBGPaddingTopPX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        private Context mFHPopCContext = null;
        private boolean mIsFHEnabled = false;
        private boolean mIsRingEnabled = false;
        private int mLeftLimit = -1;
        private int mLineEndX;
        private int mLineEndY;
        private int mLineOverlappedHeight = 0;
        private Paint mLinePaint;
        private int mLineStartX;
        private int mLineStartY;
        private int mLineThickness = 0;
        private int mOldLineEndX = -1;
        private int mOldLineEndY = -1;
        protected boolean mOverTopBoundaryEnabled = false;
        private float mPickerHeightPX = 0.0f;
        private int mPickerLineColor = -1;
        private int mPickerLineColorOnBottom = -1;
        private int mPickerOutlineThicknessPX = 0;
        private int mPickerSpaceColor = -1;
        private float mPickerWidthPX = 0.0f;
        private int mPopupState = -1;
        private int mRightLimit = -1;
        private Drawable mRingDrawable;
        private int mRingHeight;
        private int mRingWidth;
        private int mTopPickerOffset = 0;
        private float mTotalLeftLimit = 0.0f;
        private float mTotalRightLimit = 0.0f;
        private boolean misMovetoRight = false;

        public HoverPopupContainer(Context context) {
            super(context);
            this.mFHPopCContext = context;
            this.mPopupState = this.POPUPSTATE_CENTER;
            TypedArray a = this.mContext.obtainStyledAttributes(R.styleable.Theme);
            this.mPickerLineColor = a.getColor(354, -12095358);
            this.mPickerLineColorOnBottom = a.getColor(R.styleable.Theme_hoverPopupPickerLineColorOnBottom, -10846063);
            this.mPickerSpaceColor = a.getColor(R.styleable.Theme_hoverPopupPickerSpaceColor, -13674908);
            a.recycle();
            this.mBGPaddingBottomPX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            this.mBGPaddingTopPX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            this.mPickerOutlineThicknessPX = this.mContext.getResources().getDimensionPixelSize(R.dimen.airview_default_bg_outline_thickness);
            this.mPickerWidthPX = (float) HoverPopupWindow.this.convertDPtoPX(HoverPopupWindow.this.TW, null);
            this.mPickerHeightPX = (float) HoverPopupWindow.this.convertDPtoPX(HoverPopupWindow.this.H, null);
        }

        public void setFHGuideLineForCotainer(boolean enabled) {
            this.mIsFHEnabled = enabled;
        }

        public void setFHmoveAnimation(boolean enable) {
            this.isFHmoveAnimation = enable;
        }

        public void setPickerLimit(int leftlimit, int rightlimit) {
            this.mLeftLimit = leftlimit;
            this.mRightLimit = rightlimit;
        }

        public void setOverTopForCotainer(boolean enabled) {
            Log.d(TAG, "HoverPopupContainer.setOverTopForCotainer: enabled = " + enabled);
            this.mOverTopBoundaryEnabled = enabled;
            Log.d(TAG, "HoverPopupContainer.setOverTopForCotainer: mOverTopBoundaryEnabled = " + this.mOverTopBoundaryEnabled);
        }

        public void setOverTopPickerOffset(int offset) {
            this.mTopPickerOffset = offset;
        }

        public void setGuideLine(int drawableId, int lineColor) {
            this.mLineOverlappedHeight = HoverPopupWindow.this.convertDPtoPX(1.0f, null);
            this.mLineThickness = HoverPopupWindow.this.convertDPtoPX(1.5f, null);
            this.mRingDrawable = getResources().getDrawable(drawableId);
            if (this.mRingDrawable != null) {
                this.mRingWidth = this.mRingDrawable.getIntrinsicWidth();
                this.mRingHeight = this.mRingDrawable.getIntrinsicHeight();
                this.mRingDrawable.setBounds(0, 0, this.mRingWidth, this.mRingHeight);
            }
            this.mLinePaint = new Paint();
            this.mLinePaint.setStrokeWidth((float) this.mLineThickness);
            this.mLinePaint.setStrokeCap(Cap.ROUND);
            this.mLinePaint.setColor(lineColor);
            this.mLinePaint.setAntiAlias(true);
        }

        public void updateDecoration() {
            invalidate();
        }

        public int getLineEndX() {
            return this.mLineEndX;
        }

        public int getLineStartY() {
            return this.mLineStartY;
        }

        public int getLineOverlappedHeight() {
            return this.mLineOverlappedHeight;
        }

        public void setGuideLine(int startX, int startY, int endX, int endY, boolean ringEnabled, boolean fHEnabled) {
            this.mLineStartX = startX;
            this.mLineStartY = startY;
            this.mLineEndX = endX;
            this.mLineEndY = endY;
            this.mIsRingEnabled = ringEnabled;
            this.mIsFHEnabled = fHEnabled;
        }

        public void setGuideLineEndPoint(int pointX, int pointY) {
            this.mLineEndX = pointX;
            this.mLineEndY = pointY;
        }

        public void setPopupState(int state) {
            this.mPopupState = state;
        }

        public void setFHmoveAnimationOffset(int offset) {
            Log.d(TAG, "HoverPopupContainer(): setFHmoveAnimationOffset: offset = " + offset);
            this.mAnimationAreaOffset = offset;
            Log.d(TAG, "HoverPopupContainer(): setFHmoveAnimationOffset: mAnimationAreaOffset = " + this.mAnimationAreaOffset);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (getChildCount() != 0 && getChildAt(0) != null) {
                if (this.mRingDrawable == null) {
                    setGuideLine(R.drawable.hover_ic_point, -8810071);
                }
                if (this.mIsRingEnabled) {
                    canvas.save();
                    canvas.translate((float) (this.mLineEndX - (this.mRingWidth / 2)), (float) (this.mLineEndY - (this.mRingHeight / 2)));
                    if (!this.mIsFHEnabled) {
                        this.mRingDrawable.draw(canvas);
                    }
                    canvas.restore();
                    if (!this.mIsFHEnabled) {
                        if (this.mLineStartY < this.mLineEndY) {
                            canvas.drawLine((float) this.mLineStartX, (float) (this.mLineStartY - this.mLineOverlappedHeight), (float) this.mLineEndX, (float) ((this.mLineEndY - (this.mRingHeight / 2)) + this.mLineOverlappedHeight), this.mLinePaint);
                        } else if (this.mLineStartY > this.mLineEndY) {
                            canvas.drawLine((float) this.mLineStartX, (float) (this.mLineStartY + this.mLineOverlappedHeight), (float) this.mLineEndX, (float) ((this.mLineEndY + (this.mRingHeight / 2)) - this.mLineOverlappedHeight), this.mLinePaint);
                        }
                    }
                } else if (!this.mIsFHEnabled) {
                    canvas.drawLine((float) this.mLineStartX, (float) this.mLineStartY, (float) this.mLineEndX, (float) this.mLineEndY, this.mLinePaint);
                }
                if (!HoverPopupWindow.this.mIsFHGuideLineEnabled) {
                    return;
                }
                if (HoverPopupWindow.this.mContentView == null) {
                    Log.d(TAG, "HoverPopupContainer.draw(): mContentView is null, return");
                    return;
                }
                float previousRightX;
                float previousLeftX;
                float previousCenterX;
                float adjustedLPointX;
                float adjustedRPointX;
                float adjustedLPointY;
                float adjustedRPointY;
                if (HoverPopupWindow.this.mContentContainer != null) {
                    HoverPopupWindow.this.W = (float) HoverPopupWindow.this.mContentContainer.getWidth();
                }
                if (HoverPopupWindow.this.mCenterPoint == null) {
                    HoverPopupWindow.this.mCenterPoint = new PointF(HoverPopupWindow.this.W / 2.0f, this.mPickerHeightPX);
                    HoverPopupWindow.this.mLeftPoint = new PointF((HoverPopupWindow.this.W / 2.0f) - (this.mPickerWidthPX / 2.0f), 0.0f);
                    HoverPopupWindow.this.mRightPoint = new PointF((HoverPopupWindow.this.W / 2.0f) + (this.mPickerWidthPX / 2.0f), 0.0f);
                }
                if (this.mBGPaddingTopPX < 0.0f && this.mBGPaddingBottomPX < 0.0f) {
                    this.mBGPaddingTopPX = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.airview_default_bg_padding_top);
                    this.mBGPaddingBottomPX = (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.airview_default_bg_padding_bottom);
                    if (getChildCount() > 0) {
                        Drawable d = null;
                        View child = getChildAt(0);
                        if (child != null) {
                            d = child.getBackground();
                        }
                        if (d != null) {
                            Rect r = new Rect();
                            d.getPadding(r);
                            if (r.top < r.bottom) {
                                this.mBGPaddingTopPX -= (float) (r.bottom - r.top);
                            }
                        }
                    }
                }
                HoverPopupWindow.this.mCenterPoint.x = (float) getLineEndX();
                if (this.mOverTopBoundaryEnabled) {
                    HoverPopupWindow.this.mCenterPoint.y = ((float) getLineStartY()) - (this.mPickerHeightPX - this.mBGPaddingTopPX);
                } else {
                    HoverPopupWindow.this.mCenterPoint.y = ((float) getLineStartY()) + (this.mPickerHeightPX - this.mBGPaddingBottomPX);
                }
                if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                    HoverPopupWindow.this.mLeftPoint.x = HoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / 2.0f);
                } else {
                    HoverPopupWindow.this.mLeftPoint.x = HoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / 2.0f);
                }
                if (this.mOverTopBoundaryEnabled) {
                    HoverPopupWindow.this.mLeftPoint.y = (((float) getLineStartY()) + this.mBGPaddingTopPX) + ((float) this.mPickerOutlineThicknessPX);
                } else {
                    HoverPopupWindow.this.mLeftPoint.y = (((float) getLineStartY()) - this.mBGPaddingBottomPX) - ((float) this.mPickerOutlineThicknessPX);
                }
                HoverPopupWindow.this.mRightPoint.x = HoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                HoverPopupWindow.this.mRightPoint.y = HoverPopupWindow.this.mLeftPoint.y;
                if (this.mOverTopBoundaryEnabled && HoverPopupWindow.this.mIsFHAnimationEnabled) {
                    int contentViewHalfWidth = HoverPopupWindow.this.mContentView.getWidth() / 2;
                    this.mTotalLeftLimit = (float) (this.mLeftLimit + contentViewHalfWidth);
                    this.mTotalRightLimit = (float) ((this.mRightLimit - contentViewHalfWidth) + 10);
                } else {
                    this.mTotalLeftLimit = (((float) this.mLeftLimit) + HoverPopupWindow.this.mPickerPadding) + ((float) this.mAnimationAreaOffset);
                    this.mTotalRightLimit = (((float) this.mRightLimit) - HoverPopupWindow.this.mPickerPadding) - ((float) this.mAnimationAreaOffset);
                }
                int movelength = (HoverPopupWindow.this.mAnchorView.getWidth() - HoverPopupWindow.this.mContentView.getWidth()) / 2;
                if (HoverPopupWindow.this.mLeftPoint.x < this.mTotalLeftLimit && this.mLeftLimit != -1 && this.mPopupState == this.POPUPSTATE_CENTER) {
                    previousRightX = HoverPopupWindow.this.mRightPoint.x;
                    previousLeftX = HoverPopupWindow.this.mLeftPoint.x;
                    previousCenterX = HoverPopupWindow.this.mCenterPoint.x;
                    HoverPopupWindow.this.mLeftPoint.x = ((float) this.mLeftLimit) + HoverPopupWindow.this.mPickerPadding;
                    HoverPopupWindow.this.mRightPoint.x = HoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                    if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                        HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / 2.0f);
                    } else {
                        HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / 2.0f);
                    }
                    if (((float) (HoverPopupWindow.this.mAnchorView.getLeft() - HoverPopupWindow.this.mContainerLeftOnWindow)) < HoverPopupWindow.this.mLeftPoint.x || movelength > 0 || !HoverPopupWindow.this.mIsFHAnimationEnabled) {
                        this.mPopupState = this.POPUPSTATE_RIGHT;
                        HoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_RIGHT);
                    } else {
                        HoverPopupWindow.this.mRightPoint.x = previousRightX;
                        HoverPopupWindow.this.mLeftPoint.x = previousLeftX;
                        HoverPopupWindow.this.mCenterPoint.x = previousCenterX;
                    }
                }
                if (HoverPopupWindow.this.mRightPoint.x > this.mTotalRightLimit && this.mRightLimit != -1 && this.mPopupState == this.POPUPSTATE_CENTER) {
                    previousRightX = HoverPopupWindow.this.mRightPoint.x;
                    previousLeftX = HoverPopupWindow.this.mLeftPoint.x;
                    previousCenterX = HoverPopupWindow.this.mCenterPoint.x;
                    HoverPopupWindow.this.mRightPoint.x = ((float) this.mRightLimit) - HoverPopupWindow.this.mPickerPadding;
                    HoverPopupWindow.this.mLeftPoint.x = HoverPopupWindow.this.mRightPoint.x - this.mPickerWidthPX;
                    if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                        HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / 2.0f);
                    } else {
                        HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / 2.0f);
                    }
                    if (HoverPopupWindow.this.mAnchorView.getRight() - HoverPopupWindow.this.mContainerLeftOnWindow <= 0 || ((float) (HoverPopupWindow.this.mAnchorView.getRight() - HoverPopupWindow.this.mContainerLeftOnWindow)) > HoverPopupWindow.this.mRightPoint.x || movelength > 0 || !HoverPopupWindow.this.mIsFHAnimationEnabled) {
                        this.mPopupState = this.POPUPSTATE_LEFT;
                        HoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_LEFT);
                    } else {
                        HoverPopupWindow.this.mRightPoint.x = previousRightX;
                        HoverPopupWindow.this.mLeftPoint.x = previousLeftX;
                        HoverPopupWindow.this.mCenterPoint.x = previousCenterX;
                    }
                }
                if (this.mPopupState == this.POPUPSTATE_RIGHT) {
                    if (HoverPopupWindow.this.mLeftPoint.x <= this.mTotalLeftLimit || this.mLeftLimit == -1) {
                        HoverPopupWindow.this.mLeftPoint.x = ((float) this.mLeftLimit) + HoverPopupWindow.this.mPickerPadding;
                        HoverPopupWindow.this.mRightPoint.x = HoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                        if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                            HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / 2.0f);
                        } else {
                            HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / 2.0f);
                        }
                    } else {
                        this.mPopupState = this.POPUPSTATE_CENTER;
                        HoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_CENTER);
                    }
                }
                if (this.mPopupState == this.POPUPSTATE_LEFT) {
                    if (HoverPopupWindow.this.mRightPoint.x >= this.mTotalRightLimit || this.mRightLimit == -1) {
                        HoverPopupWindow.this.mRightPoint.x = ((float) this.mRightLimit) - HoverPopupWindow.this.mPickerPadding;
                        HoverPopupWindow.this.mLeftPoint.x = HoverPopupWindow.this.mRightPoint.x - this.mPickerWidthPX;
                        if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                            HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / 2.0f);
                        } else {
                            HoverPopupWindow.this.mCenterPoint.x = HoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / 2.0f);
                        }
                    } else {
                        this.mPopupState = this.POPUPSTATE_CENTER;
                        HoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_CENTER);
                    }
                }
                if (!HoverPopupWindow.this.mIsInfoPickerMoveEabled) {
                    int anchorViewCenter = 0;
                    if (HoverPopupWindow.this.mReferncedAnchorRect != null) {
                        anchorViewCenter = (HoverPopupWindow.this.mReferncedAnchorRect.left + (HoverPopupWindow.this.mAnchorView.getWidth() / 2)) - HoverPopupWindow.this.mContainerLeftOnWindow;
                    }
                    if (!(anchorViewCenter == 0 || HoverPopupWindow.this.mFullTextPopupRightLimit == -1 || HoverPopupWindow.this.mPickerXoffset + anchorViewCenter >= HoverPopupWindow.this.mFullTextPopupRightLimit)) {
                        HoverPopupWindow.this.mCenterPoint.x = (float) (HoverPopupWindow.this.mPickerXoffset + anchorViewCenter);
                        HoverPopupWindow.this.mLeftPoint.x = HoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / 2.0f);
                        HoverPopupWindow.this.mRightPoint.x = HoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                    }
                }
                int adjustPointer = (this.mPickerOutlineThicknessPX % 2 != 0 ? this.mPickerOutlineThicknessPX + 1 : this.mPickerOutlineThicknessPX) / 2;
                if (this.mPickerOutlineThicknessPX != 4) {
                    adjustedLPointX = HoverPopupWindow.this.mLeftPoint.x - ((float) adjustPointer);
                    adjustedRPointX = HoverPopupWindow.this.mRightPoint.x + ((float) adjustPointer);
                    if (this.mOverTopBoundaryEnabled) {
                        adjustedLPointY = HoverPopupWindow.this.mLeftPoint.y + ((float) adjustPointer);
                        adjustedRPointY = HoverPopupWindow.this.mRightPoint.y + ((float) adjustPointer);
                    } else {
                        adjustedLPointY = HoverPopupWindow.this.mLeftPoint.y - ((float) adjustPointer);
                        adjustedRPointY = HoverPopupWindow.this.mRightPoint.y - ((float) adjustPointer);
                    }
                } else {
                    adjustedLPointX = HoverPopupWindow.this.mLeftPoint.x;
                    adjustedRPointX = HoverPopupWindow.this.mRightPoint.x;
                    adjustedLPointY = HoverPopupWindow.this.mLeftPoint.y;
                    adjustedRPointY = HoverPopupWindow.this.mRightPoint.y;
                }
                if (!this.mOverTopBoundaryEnabled || this.mPopupState != this.POPUPSTATE_CENTER || HoverPopupWindow.this.mPopupType != 3 || HoverPopupWindow.this.mIsInfoPickerMoveEabled || !HoverPopupWindow.this.mIsFHAnimationEnabled) {
                    TypedArray a = this.mContext.obtainStyledAttributes(R.styleable.Theme);
                    if (!HoverPopupWindow.this.mIsSetInfoPickerColorToAndMoreBottomImg || this.mOverTopBoundaryEnabled) {
                        this.mPickerSpaceColor = a.getColor(R.styleable.Theme_hoverPopupPickerSpaceColor, -10654339);
                    } else {
                        this.mPickerSpaceColor = a.getColor(R.styleable.Theme_hoverPopupBottomBgColor, -10654339);
                    }
                    a.recycle();
                    Paint Pnt = new Paint(1);
                    Pnt.setStrokeWidth((float) this.mPickerOutlineThicknessPX);
                    Pnt.setColor(this.mPickerSpaceColor);
                    Pnt.setAntiAlias(true);
                    Path path1 = new Path();
                    path1.setFillType(FillType.EVEN_ODD);
                    path1.moveTo(adjustedLPointX, adjustedLPointY);
                    path1.lineTo(HoverPopupWindow.this.mCenterPoint.x, HoverPopupWindow.this.mCenterPoint.y);
                    path1.lineTo(adjustedRPointX, adjustedRPointY);
                    path1.close();
                    Pnt.setStyle(Style.FILL);
                    canvas.drawPath(path1, Pnt);
                    Path path2 = new Path();
                    if (this.mOverTopBoundaryEnabled) {
                        Pnt.setColor(this.mPickerLineColorOnBottom);
                    } else {
                        Pnt.setColor(this.mPickerLineColor);
                    }
                    Pnt.setStrokeWidth((float) this.mPickerOutlineThicknessPX);
                    Pnt.setStyle(Style.STROKE);
                    Pnt.setStrokeJoin(Join.ROUND);
                    path2.moveTo(adjustedLPointX, adjustedLPointY);
                    path2.lineTo(HoverPopupWindow.this.mCenterPoint.x, HoverPopupWindow.this.mCenterPoint.y);
                    path2.lineTo(adjustedRPointX, adjustedRPointY);
                    path2.close();
                    canvas.drawPath(path2, Pnt);
                    Path path3 = new Path();
                    int adjustLineOffset = this.mPickerOutlineThicknessPX % 2;
                    if (this.mPickerOutlineThicknessPX == 4) {
                        adjustedLPointX = HoverPopupWindow.this.mLeftPoint.x - ((float) adjustPointer);
                        adjustedRPointX = HoverPopupWindow.this.mRightPoint.x + ((float) adjustPointer);
                    }
                    path3.moveTo(adjustedLPointX, adjustedLPointY);
                    path3.lineTo(adjustedRPointX, adjustedRPointY);
                    Pnt.setStrokeWidth((float) (this.mPickerOutlineThicknessPX + adjustLineOffset));
                    Pnt.setAntiAlias(false);
                    Pnt.setColor(this.mPickerSpaceColor);
                    Pnt.setStyle(Style.STROKE);
                    path3.close();
                    canvas.drawPath(path3, Pnt);
                }
            }
        }

        protected boolean pointInValidPaddingArea(int localX, int localY) {
            if (getPaddingTop() > getPaddingBottom()) {
                if (localX >= getWidth() || localY > getPaddingTop()) {
                    return false;
                }
                return true;
            } else if (getPaddingTop() >= getPaddingBottom()) {
                return false;
            } else {
                if (localX >= getWidth() || localY < getHeight() - getPaddingBottom()) {
                    return false;
                }
                return true;
            }
        }
    }

    public interface HoverPopupListener {
        boolean onSetContentView(View view, HoverPopupWindow hoverPopupWindow);
    }

    public interface HoverPopupPreShowListener {
        boolean onHoverPopupPreShow();
    }

    public static class QuintEaseOut implements Interpolator {
        public float getInterpolation(float input) {
            input = (input / 1.0f) - 1.0f;
            return ((((input * input) * input) * input) * input) + 1.0f;
        }
    }

    protected class TouchablePopupContainer extends FrameLayout {
        private static final int MSG_TIMEOUT = 1;
        private static final int TIMEOUT_DELAY = 500;
        private static final int TIMEOUT_DELAY_LONG = 2000;
        protected Handler mContainerDismissHandler = null;
        private Runnable mDismissPopupRunnable = null;
        private boolean mIsHoverExitCalled = false;

        public TouchablePopupContainer(Context context) {
            super(context);
            this.mContainerDismissHandler = new Handler(HoverPopupWindow.this) {
                public void handleMessage(Message msg) {
                    Log.d(HoverPopupWindow.TAG, "TouchablePopupContainer: ***** mContainerDismissHandler handleMessage *****");
                    if (HoverPopupWindow.this.mPopup != null && HoverPopupWindow.this.mPopup.isShowing() && msg.what == 1) {
                        Log.d(HoverPopupWindow.TAG, "TouchablePopupContainer: mContainerDismissHandler handleMessage: Call dismiss");
                        HoverPopupWindow.this.dismiss();
                    }
                }
            };
        }

        public boolean dispatchTouchEvent(MotionEvent event) {
            if (this.mIsHoverExitCalled && this.mDismissPopupRunnable != null) {
                removeCallbacks(this.mDismissPopupRunnable);
                this.mDismissPopupRunnable = null;
                this.mIsHoverExitCalled = false;
            }
            boolean superRet = super.dispatchTouchEvent(event);
            if (event.getAction() == 1 && HoverPopupWindow.this.mDismissTouchableHPWOnActionUp) {
                postDelayed(new Runnable() {
                    public void run() {
                        HoverPopupWindow.this.dismiss();
                    }
                }, 100);
            }
            return superRet;
        }

        protected boolean dispatchHoverEvent(MotionEvent event) {
            int action = event.getAction();
            if (action == 10) {
                if (pointInView(event.getX(), event.getY(), -2.0f)) {
                    this.mIsHoverExitCalled = true;
                    this.mDismissPopupRunnable = new Runnable() {
                        public void run() {
                            HoverPopupWindow.this.dismiss();
                        }
                    };
                    postDelayed(this.mDismissPopupRunnable, 100);
                } else {
                    boolean superRet = super.dispatchHoverEvent(event);
                    HoverPopupWindow.this.dismiss();
                    return superRet;
                }
            } else if (action == 7 && HoverPopupWindow.this.mToolType != 3) {
                resetTimeout();
            }
            return super.dispatchHoverEvent(event);
        }

        public void resetTimeout() {
            if (this.mContainerDismissHandler != null) {
                if (this.mContainerDismissHandler.hasMessages(1)) {
                    this.mContainerDismissHandler.removeMessages(1);
                }
                if (Build.PRODUCT == null || !(Build.PRODUCT.startsWith("gt5note") || Build.PRODUCT.startsWith("noble"))) {
                    this.mContainerDismissHandler.sendMessageDelayed(this.mContainerDismissHandler.obtainMessage(1), 500);
                } else {
                    this.mContainerDismissHandler.sendMessageDelayed(this.mContainerDismissHandler.obtainMessage(1), 2000);
                }
            }
        }
    }

    @Deprecated
    public HoverPopupWindow(View parentView) {
        this(parentView, 0);
    }

    public HoverPopupWindow(View parentView, int type) {
        this.ID_TOOLTIP_VIEW = 117506049;
        this.MARGIN_FOR_HOVER_RING = 8;
        this.mPopupType = 0;
        this.mToolType = 0;
        this.ANCHORVIEW_COORDINATES_TYPE_NONE = 0;
        this.ANCHORVIEW_COORDINATES_TYPE_WINDOW = 1;
        this.ANCHORVIEW_COORDINATES_TYPE_SCREEN = 2;
        this.mAnchorRect = null;
        this.mDisplayFrame = null;
        this.mContentWidth = 0;
        this.mContentHeight = 0;
        this.mNeedToMeasureContentView = false;
        this.mIsShowMessageSent = false;
        this.mShowPopupRunnable = null;
        this.mDismissPopupRunnable = null;
        this.mDismissTouchableHPWOnActionUp = true;
        this.mIsHoverPaddingEnabled = false;
        this.W = 0.0f;
        this.H = 10.0f;
        this.TW = 15.0f;
        this.mLeftPoint = null;
        this.mRightPoint = null;
        this.mCenterPoint = null;
        this.mPickerPadding = 54.0f;
        this.mHandler = null;
        this.SHOW_ANIMATION_DURATION = 500;
        this.MOVE_RIGHT = 0;
        this.MOVE_LEFT = 1;
        this.MOVE_CENTER = 2;
        this.MOVE_LEFT_TO_CENTER = 3;
        this.MOVE_RIGHT_TO_CENTER = 4;
        this.mDirection = this.MOVE_CENTER;
        this.mDisplayWidthToComputeAniWidth = 0;
        this.mDisplayFrameLeft = 0;
        this.mDisplayFrameRight = 0;
        this.mContainerLeftOnWindow = 0;
        this.mPickerXoffset = 0;
        this.mReferncedAnchorRect = null;
        this.mDismissHandler = null;
        this.mCoverManager = null;
        this.mFullTextPopupRightLimit = -1;
        this.mPenWindowStartPos = null;
        this.mFontScale = 0.0f;
        this.mUspLevel = 0;
        this.mParentView = parentView;
        this.mContext = parentView.getContext();
        this.mPopupType = type;
        initInstance();
        setInstanceByType(type);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (!HoverPopupWindow.this.mIsFHAnimationEnabled) {
                    return;
                }
                if ((HoverPopupWindow.this.mOverTopBoundary || HoverPopupWindow.this.misGravityBottomUnder) && HoverPopupWindow.this.mPopup != null && HoverPopupWindow.this.mPopup.isShowing() && HoverPopupWindow.this.mAnchorView != null && HoverPopupWindow.this.mContentView != null) {
                    int movelength = (HoverPopupWindow.this.mAnchorView.getWidth() - HoverPopupWindow.this.mContentView.getWidth()) / 2;
                    if (movelength < 0) {
                        if (msg.what == 0) {
                            int tempMoveLength = HoverPopupWindow.this.mPopupPosX + ((HoverPopupWindow.this.mAnchorView.getWidth() * 2) / 3);
                            movelength = HoverPopupWindow.this.mContentView.getWidth() + tempMoveLength > HoverPopupWindow.this.mDisplayWidthToComputeAniWidth ? tempMoveLength - ((HoverPopupWindow.this.mContentView.getWidth() + tempMoveLength) - HoverPopupWindow.this.mDisplayWidthToComputeAniWidth) : tempMoveLength;
                        } else if (msg.what == 1) {
                            movelength = HoverPopupWindow.this.mPopupPosX;
                        }
                    }
                    if (msg.what == 0) {
                        HoverPopupWindow.this.mDirection = HoverPopupWindow.this.MOVE_RIGHT;
                        HoverPopupWindow.this.setAnimator(movelength, HoverPopupWindow.this.mDirection);
                        HoverPopupWindow.this.objAnimator.start();
                    } else if (msg.what == 1) {
                        HoverPopupWindow.this.mDirection = HoverPopupWindow.this.MOVE_LEFT;
                        HoverPopupWindow.this.setAnimator(movelength, HoverPopupWindow.this.mDirection);
                        HoverPopupWindow.this.objAnimator.start();
                    } else if (msg.what == 2) {
                        if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_LEFT) {
                            HoverPopupWindow.this.mDirection = HoverPopupWindow.this.MOVE_LEFT_TO_CENTER;
                        } else if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_RIGHT) {
                            HoverPopupWindow.this.mDirection = HoverPopupWindow.this.MOVE_RIGHT_TO_CENTER;
                        }
                        HoverPopupWindow.this.setAnimator(movelength, HoverPopupWindow.this.mDirection);
                        HoverPopupWindow.this.objAnimator.start();
                    }
                }
            }
        };
        this.mDismissHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (HoverPopupWindow.this.mPopup != null && HoverPopupWindow.this.mPopup.isShowing() && msg.what == 1) {
                    Log.d(HoverPopupWindow.TAG, "mDismissHandler handleMessage: Call dismiss");
                    HoverPopupWindow.this.dismiss();
                }
            }
        };
    }

    protected void initInstance() {
        this.mPopup = null;
        this.mEnabled = true;
        this.mHoverDetectTimeMS = 300;
        this.mPopupGravity = 12849;
        this.mPopupPosX = 0;
        this.mPopupPosY = 0;
        this.mHoveringPointX = 0;
        this.mHoveringPointY = 0;
        this.mPopupOffsetX = 0;
        this.mPopupOffsetY = 0;
        this.mWindowGapX = 0;
        this.mWindowGapY = 0;
        this.mHoverPaddingLeft = 0;
        this.mHoverPaddingRight = 0;
        this.mHoverPaddingTop = 0;
        this.mHoverPaddingBottom = 0;
        this.mListener = null;
        this.mContentText = null;
        this.mAnimationStyle = R.style.Animation_HoverPopup;
        this.mIsGuideLineEnabled = false;
        this.mIsFHGuideLineEnabled = false;
        this.misDialer = false;
        this.mIsProgressBar = false;
        this.mIsFHAnimationEnabled = true;
        this.mIsInfoPickerMoveEabled = true;
        this.mIsFHGuideLineEnabledByApp = false;
        this.mIsFHAnimationEnabledByApp = false;
        this.mIsInfoPickerMoveEabledByApp = false;
        this.mIsSetInfoPickerColorToAndMoreBottomImg = false;
        this.mIsFHSoundAndHapticEnabled = true;
        this.mCoordinatesOfAnchorView = 0;
        this.mOverTopBoundary = false;
        this.misGravityBottomUnder = false;
        this.mGuideLineFadeOffset = 0;
        this.mContentView = null;
        this.mContentContainer = null;
        this.mTouchableContainer = null;
        this.mAnchorView = null;
        this.mIsSPenPointChanged = false;
        this.mIsPopupTouchable = false;
        this.mIsTryingShowPopup = false;
        this.mShowPopupAlways = false;
        this.mIsSkipPenPointEffect = false;
        TypedArray a = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        this.mGuideRingDrawableId = a.getResourceId(353, R.drawable.hover_ic_point);
        this.mGuideLineColor = a.getColor(352, -8810071);
        a.recycle();
        this.mFullTextPopupRightLimit = -1;
        this.mPenWindowStartPos = new Point();
        this.mPenWindowStartPos.x = 0;
        this.mPenWindowStartPos.y = 0;
        this.mFontScale = 0.0f;
        this.mUspLevel = this.mContext.getPackageManager().getSystemFeatureLevel("com.sec.feature.spen_usp");
        initCoverManager();
    }

    private void initCoverManager() {
        if (this.mCoverManager == null) {
            this.mCoverManager = Stub.asInterface(ServiceManager.getService("cover"));
            if (this.mCoverManager == null) {
                Log.e(TAG, "warning: no COVER_MANAGER_SERVICE");
            }
        }
    }

    protected void setInstanceByType(int type) {
        if (type == 1) {
            this.mHoverDetectTimeMS = 300;
            this.mPopupGravity = 20819;
            this.mAnimationStyle = R.style.Animation_HoverPopup;
        }
    }

    private void setAnimator(int movelength, int direction) {
        if (direction == this.MOVE_LEFT || direction == this.MOVE_RIGHT) {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{0.0f, (float) movelength});
        } else if (direction == this.MOVE_LEFT_TO_CENTER || direction == this.MOVE_RIGHT_TO_CENTER) {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{(float) movelength, 0.0f});
        } else {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 0.0f});
        }
        this.objAnimator.setInterpolator(new QuintEaseOut());
        this.objAnimator.setDuration(500);
        this.objAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                HoverPopupWindow.this.objAnimationValue = ((Float) animation.getAnimatedValue()).floatValue();
                if (HoverPopupWindow.this.mPopup == null) {
                    return;
                }
                if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_LEFT && ((float) HoverPopupWindow.this.mDisplayFrameLeft) < (((float) HoverPopupWindow.this.mPopupPosX) - HoverPopupWindow.this.objAnimationValue) + ((float) HoverPopupWindow.this.mContentView.getWidth())) {
                    HoverPopupWindow.this.mPopup.update((int) (((float) HoverPopupWindow.this.mPopupPosX) - HoverPopupWindow.this.objAnimationValue), HoverPopupWindow.this.mPopupPosY, -1, -1);
                } else if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_RIGHT && ((float) HoverPopupWindow.this.mDisplayFrameRight) > (((float) HoverPopupWindow.this.mPopupPosX) + HoverPopupWindow.this.objAnimationValue) + ((float) HoverPopupWindow.this.mContentView.getWidth())) {
                    HoverPopupWindow.this.mPopup.update((int) (((float) HoverPopupWindow.this.mPopupPosX) + HoverPopupWindow.this.objAnimationValue), HoverPopupWindow.this.mPopupPosY, -1, -1);
                } else if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_LEFT_TO_CENTER) {
                    HoverPopupWindow.this.mPopup.update((int) (((float) HoverPopupWindow.this.mPopupPosX) - HoverPopupWindow.this.objAnimationValue), HoverPopupWindow.this.mPopupPosY, -1, -1);
                } else if (HoverPopupWindow.this.mDirection == HoverPopupWindow.this.MOVE_RIGHT_TO_CENTER) {
                    HoverPopupWindow.this.mPopup.update((int) (((float) HoverPopupWindow.this.mPopupPosX) + HoverPopupWindow.this.objAnimationValue), HoverPopupWindow.this.mPopupPosY, -1, -1);
                }
            }
        });
    }

    public boolean isHoverPopupPossible() {
        if (this.mPopupType == 0) {
            return false;
        }
        if (this.mPopupType == 1) {
            if (this.mParentView == null || TextUtils.isEmpty(getTooltipText())) {
                return false;
            }
            return true;
        } else if (this.mPopupType == 2) {
            return false;
        } else {
            if (this.mPopupType == 3) {
                return true;
            }
            return true;
        }
    }

    public boolean isDialer() {
        return this.misDialer;
    }

    public void setInstanceOfDialer(boolean enabled) {
        this.misDialer = enabled;
    }

    public boolean isProgressBar() {
        return this.mIsProgressBar;
    }

    public void setInstanceOfProgressBar(boolean enabled) {
        this.mIsProgressBar = enabled;
    }

    protected int getUspLevel() {
        return this.mUspLevel;
    }

    protected boolean isHoveringSettingEnabled(int type) {
        if (this.mToolType == 2) {
            return isSPenHoveringSettingsEnabled(type);
        }
        if (this.mToolType == 1) {
            return isFingerHoveringSettingsEnabled(type);
        }
        return false;
    }

    public boolean isUseOldAirviewSettingsMenu() {
        if ((Build.PRODUCT == null || (!Build.PRODUCT.startsWith("hlte") && !Build.PRODUCT.startsWith("h3g") && !Build.PRODUCT.startsWith("ha3g"))) && !SmartFaceManager.TRUE.equals(AIRCOMMAND_MORPH_USP)) {
            return false;
        }
        return true;
    }

    protected boolean isSPenHoveringSettingsEnabled(int type) {
        boolean isSPenHoveringOn;
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING, 0, -3) == 1) {
            isSPenHoveringOn = true;
        } else {
            isSPenHoveringOn = false;
        }
        if (isSPenHoveringOn) {
            if (type == 1) {
                if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_ICON_LABEL, 0, -3) == 1) {
                    return true;
                }
            } else if (type == 3 || type == 2) {
                if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_INFORMATION_PREVIEW, 0, -3) == 1) {
                    if (!isUseOldAirviewSettingsMenu()) {
                        return true;
                    }
                    if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_SPEED_DIAL_PREVIEW, 0, -3) == 0 && isDialer()) {
                        return false;
                    }
                    if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_PROGRESS_PREVIEW, 0, -3) == 0 && isProgressBar()) {
                        return false;
                    }
                    return true;
                } else if (!isUseOldAirviewSettingsMenu()) {
                    return false;
                } else {
                    if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_SPEED_DIAL_PREVIEW, 0, -3) == 1 && isDialer()) {
                        return true;
                    }
                    if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_PROGRESS_PREVIEW, 0, -3) == 1 && isProgressBar()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isFingerHoveringSettingsEnabled(int type) {
        boolean isFingerHoveringOn;
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW, 0, -3) == 1) {
            isFingerHoveringOn = true;
        } else {
            isFingerHoveringOn = false;
        }
        if (!isFingerHoveringOn || type == 1) {
            return false;
        }
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_INFORMATION_PREVIEW, 0, -3) == 1) {
            if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_PROGRESS_BAR_PREVIEW, 0, -3) == 0 && isProgressBar()) {
                return false;
            }
            if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_SPEED_DIAL_TIP, 0, -3) == 1) {
                return true;
            }
            if (isDialer()) {
                return false;
            }
            return true;
        } else if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_SPEED_DIAL_TIP, 0, -3) == 1 && isDialer()) {
            return true;
        } else {
            if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_PROGRESS_BAR_PREVIEW, 0, -3) == 1 && isProgressBar()) {
                return true;
            }
            return false;
        }
    }

    protected boolean isMouseHoveringSettingsEnabled(int type) {
        return false;
    }

    public void setHoverPopupToolType(int type) {
        this.mToolType = type;
    }

    public boolean isLockScreenMode() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        return ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    private boolean isViewCoverClose() {
        boolean isCoverOpen = true;
        try {
            if (this.mCoverManager != null) {
                CoverState coverState = this.mCoverManager.getCoverState();
                if (coverState != null) {
                    isCoverOpen = coverState.getSwitchState();
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getCoverState: ", e);
        }
        return !isCoverOpen;
    }

    public void setDismissTouchableHPWOnActionUp(boolean bDismissTouchableHPWOnActionUp) {
        this.mDismissTouchableHPWOnActionUp = bDismissTouchableHPWOnActionUp;
    }

    public boolean getIsDismissTouchableHPWOnActionUp() {
        return this.mDismissTouchableHPWOnActionUp;
    }

    @Deprecated
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    @Deprecated
    public boolean getEnabled() {
        return this.mEnabled;
    }

    public View getParentView() {
        return this.mParentView;
    }

    public void setHoverPopupListener(HoverPopupListener l) {
        this.mListener = l;
    }

    public void setHoverPopupPreShowListener(HoverPopupPreShowListener l) {
        this.mPreShowListener = l;
    }

    @Deprecated
    public void setContent(int resId) {
        this.mContentResId = resId;
        this.mNeedToMeasureContentView = true;
    }

    public void setContent(View view) {
        setContent(view, view != null ? view.getLayoutParams() : null);
    }

    public void setContent(View view, LayoutParams lp) {
        this.mContentView = view;
        this.mContentLP = lp;
        this.mNeedToMeasureContentView = true;
    }

    public void setContent(CharSequence text) {
        this.mContentText = text;
        this.mNeedToMeasureContentView = true;
    }

    public View getContent() {
        return this.mContentView;
    }

    public boolean isShowing() {
        if (this.mPopup != null) {
            return this.mPopup.isShowing();
        }
        return false;
    }

    public void setHoverDetectTime(int ms) {
        this.mHoverDetectTimeMS = ms;
    }

    public void setHoverPaddingArea(int left, int top, int right, int bottom) {
        this.mHoverPaddingLeft = left;
        this.mHoverPaddingRight = right;
        this.mHoverPaddingTop = top;
        this.mHoverPaddingBottom = bottom;
        if (this.mHoverPaddingLeft != 0 || this.mHoverPaddingRight != 0 || this.mHoverPaddingTop != 0 || this.mHoverPaddingBottom != 0) {
            this.mIsHoverPaddingEnabled = true;
        }
    }

    public void setShowPopupAlways(boolean always) {
        this.mShowPopupAlways = always;
    }

    @Deprecated
    public void setAnchorView(View anchor) {
        this.mAnchorView = anchor;
    }

    public void setPopupGravity(int gravity) {
        this.mPopupGravity = gravity;
    }

    public void setPopupPosOffset(int x, int y) {
        this.mPopupOffsetX = x;
        this.mPopupOffsetY = y;
    }

    public void setPickerXOffset(int xOffset) {
        this.mPickerXoffset = xOffset;
    }

    public void setOverTopPickerOffset(int offset) {
        if (this.mContentContainer != null) {
            this.mContentContainer.setOverTopPickerOffset(offset);
        }
    }

    public void setHoveringPoint(int x, int y) {
        this.mHoveringPointX = x;
        this.mHoveringPointY = y;
    }

    @Deprecated
    protected CharSequence getPriorityContentText() {
        if (!TextUtils.isEmpty(this.mContentText)) {
            return this.mContentText;
        }
        if (TextUtils.isEmpty(this.mParentView.getContentDescription())) {
            return null;
        }
        return this.mParentView.getContentDescription();
    }

    private CharSequence getTooltipText() {
        if (!TextUtils.isEmpty(this.mContentText)) {
            return this.mContentText;
        }
        if (TextUtils.isEmpty(this.mParentView.getContentDescription())) {
            return null;
        }
        return this.mParentView.getContentDescription();
    }

    private void playSoundAndHapticFeedback() {
        ((AudioManager) this.mContext.getSystemService("audio")).playSoundEffect(103);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.VIBRATE") == 0) {
            this.mParentView.performHapticFeedback(HapticFeedbackConstants.VIBE_TOUCH);
        }
    }

    @Deprecated
    public void show() {
        show(this.mPopupType);
    }

    public void show(int type) {
        if (type != this.mPopupType) {
            this.mPopupType = type;
            setInstanceByType(type);
        }
        if ((this.mPreShowListener != null && !this.mPreShowListener.onHoverPopupPreShow()) || !this.mEnabled || type == 0 || this.mIsShowMessageSent) {
            return;
        }
        if ((!this.mIsHoverPaddingEnabled || this.mIsTryingShowPopup) && isHoverPopupPossible() && isHoveringSettingEnabled(type) && !isShowing() && this.mParentView.getHandler() != null && !isViewCoverClose() && !isLockScreenMode()) {
            LayoutParams vlp = this.mParentView.getRootView().getLayoutParams();
            if (vlp instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams wlp = (WindowManager.LayoutParams) vlp;
                if (wlp.type == WindowManager.LayoutParams.TYPE_COCKTAIL_BAR || wlp.type == 98) {
                    setFHGuideLineEnabled(false);
                }
            }
            this.mHashCodeForViewState = getStateHashCode();
            if (!this.mIsSkipPenPointEffect) {
                showPenPointEffect(true);
            }
            if (this.mIsFHSoundAndHapticEnabled && this.mToolType == 1 && Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_SOUND_AND_HAPTIC_FEEDBACK, 0) == 1) {
                playSoundAndHapticFeedback();
            }
            if (this.mPopupType == 1) {
                this.mDismissPopupRunnable = new Runnable() {
                    public void run() {
                        HoverPopupWindow.this.dismissPopup();
                    }
                };
            }
            this.mShowPopupRunnable = new Runnable() {
                public void run() {
                    HoverPopupWindow.this.showPopup();
                    if (HoverPopupWindow.this.mPopupType == 1 && HoverPopupWindow.this.isShowing()) {
                        HoverPopupWindow.this.mParentView.postDelayed(HoverPopupWindow.this.mDismissPopupRunnable, 10000);
                    }
                }
            };
            this.mParentView.postDelayed(this.mShowPopupRunnable, (long) this.mHoverDetectTimeMS);
            this.mIsShowMessageSent = true;
        }
    }

    private void showPopup() {
        if (this.mHashCodeForViewState != getStateHashCode()) {
            if (this.mUspLevel > 3 && this.mParentView.getWindowVisibility() == 0 && this.mParentView.getVisibility() == 0) {
                dismiss();
                show();
                return;
            }
            dismiss();
        } else if (this.mParentView.getIsDetachedFromWindow()) {
            dismiss();
        } else {
            if (!this.mIsSkipPenPointEffect) {
                showPenPointEffect(true);
            }
            this.mIsSkipPenPointEffect = false;
            if (!(DEVICE_TYPE == null || !DEVICE_TYPE.contains("tablet") || "mocha".equals(SystemProperties.get("ro.build.scafe")))) {
                setFHGuideLineEnabled(false);
            }
            if (Build.PRODUCT != null && Build.PRODUCT.startsWith("noble")) {
                setFHGuideLineEnabled(false);
            }
            if (this.mPopup != null) {
                this.mPopup.dismiss();
            }
            createPopupWindow();
            setPopupContent();
            updateHoverPopup();
        }
    }

    protected PopupWindow createPopupWindow() {
        if (this.mPopup == null) {
            this.mPopup = new PopupWindow(this.mParentView.getContext());
            this.mPopup.setWidth(-2);
            this.mPopup.setHeight(-2);
            this.mPopup.setTouchable(this.mIsPopupTouchable);
            this.mPopup.setClippingEnabled(false);
            this.mPopup.setBackgroundDrawable(null);
            this.mPopup.setWindowLayoutType(1005);
            View anchorView = this.mAnchorView != null ? this.mAnchorView : this.mParentView;
            LayoutParams vlp = anchorView.getRootView().getLayoutParams();
            if ((vlp instanceof WindowManager.LayoutParams) && ((WindowManager.LayoutParams) vlp).type == 1007) {
                this.mPopup.setIgnoreMultiWindowLayout(true);
            }
            if (!(anchorView.getApplicationWindowToken() == anchorView.getWindowToken() || this.mParentView.isScaleWindow())) {
                this.mPopup.setLayoutInScreenEnabled(true);
            }
            this.mPopup.setAnimationStyle(this.mAnimationStyle);
        }
        return this.mPopup;
    }

    private void setPopupContent() {
        switch (this.mPopupType) {
            case 0:
                this.mContentView = null;
                break;
            case 1:
                makeToolTipContentView();
                break;
            case 2:
                makeDefaultContentView();
                break;
            case 3:
                if (this.mContentView == null && this.mContentResId != 0) {
                    LayoutInflater inflater;
                    if ((Build.PRODUCT == null || !Build.PRODUCT.startsWith("gt5note")) && this.mUspLevel <= 3) {
                        inflater = LayoutInflater.from(this.mContext);
                    } else {
                        inflater = LayoutInflater.from(new ContextThemeWrapper(this.mContext, (int) R.style.Theme_DeviceDefault_Light));
                    }
                    try {
                        this.mContentView = inflater.inflate(this.mContentResId, null);
                        break;
                    } catch (InflateException e) {
                        this.mContentView = null;
                        break;
                    }
                }
                break;
            default:
                this.mContentView = null;
                break;
        }
        if (this.mListener != null) {
            this.mListener.onSetContentView(this.mParentView, this);
        }
    }

    protected void makeDefaultContentView() {
        makeToolTipContentView();
    }

    private void makeToolTipContentView() {
        CharSequence text = getTooltipText();
        if (TextUtils.isEmpty(text)) {
            this.mContentView = null;
            return;
        }
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;
        if (!(this.mContentView != null && this.mContentView.getId() == 117506049 && this.mFontScale == fontScale)) {
            LayoutInflater inflater;
            if (this.mFontScale != fontScale) {
                this.mFontScale = fontScale;
            }
            if ((Build.PRODUCT == null || !Build.PRODUCT.startsWith("gt5note")) && this.mUspLevel <= 3) {
                inflater = LayoutInflater.from(this.mContext);
            } else {
                inflater = LayoutInflater.from(new ContextThemeWrapper(this.mContext, (int) R.style.Theme_DeviceDefault_Light));
            }
            this.mContentView = inflater.inflate((int) R.layout.hover_tooltip_popup, null);
            this.mContentView.setHoverPopupType(0);
            this.mContentView.setId(117506049);
        }
        ((TextView) this.mContentView).setText(text);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void computePopupPosition(android.view.View r55, int r56, int r57, int r58) {
        /*
        r54 = this;
        r0 = r54;
        r4 = r0.mContentView;
        if (r4 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r55;
        r1 = r54;
        r1.mAnchorView = r0;
        r0 = r56;
        r1 = r54;
        r1.mPopupGravity = r0;
        r0 = r57;
        r1 = r54;
        r1.mPopupOffsetX = r0;
        r0 = r58;
        r1 = r54;
        r1.mPopupOffsetY = r0;
        if (r55 == 0) goto L_0x04c4;
    L_0x0021:
        r14 = r55;
    L_0x0023:
        r0 = r54;
        r4 = r0.mContext;
        r4 = r4.getResources();
        r27 = r4.getDisplayMetrics();
        r13 = 0;
        r4 = 2;
        r12 = new int[r4];
        r4 = 2;
        r11 = new int[r4];
        r14.getLocationOnScreen(r12);
        r14.getLocationInWindow(r11);
        r4 = r14.updateDisplayListIfDirty();
        r4 = r4.hasIdentityMatrix();
        if (r4 != 0) goto L_0x00ab;
    L_0x0046:
        r4 = r14.getRotation();
        r5 = 0;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0061;
    L_0x004f:
        r4 = r14.getRotationX();
        r5 = 0;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0061;
    L_0x0058:
        r4 = r14.getRotationY();
        r5 = 0;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x00ab;
    L_0x0061:
        r4 = 2;
        r0 = new float[r4];
        r34 = r0;
        r4 = 0;
        r5 = 1;
        r6 = 0;
        r34[r5] = r6;
        r34[r4] = r6;
        r4 = r14.getMatrix();
        r0 = r34;
        r4.mapPoints(r0);
        r4 = r14.getRotation();
        r4 = (int) r4;
        r5 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        if (r4 != r5) goto L_0x04ca;
    L_0x007f:
        r4 = 0;
        r5 = 0;
        r5 = r11[r5];
        r6 = r14.getWidth();
        r5 = r5 - r6;
        r11[r4] = r5;
        r4 = 1;
        r5 = 1;
        r5 = r11[r5];
        r6 = r14.getHeight();
        r5 = r5 - r6;
        r11[r4] = r5;
        r4 = 0;
        r5 = 0;
        r5 = r12[r5];
        r6 = r14.getWidth();
        r5 = r5 - r6;
        r12[r4] = r5;
        r4 = 1;
        r5 = 1;
        r5 = r12[r5];
        r6 = r14.getHeight();
        r5 = r5 - r6;
        r12[r4] = r5;
    L_0x00ab:
        r26 = new android.graphics.Rect;
        r26.<init>();
        r0 = r26;
        r14.getWindowVisibleContentFrame(r0);
        r0 = r54;
        r4 = r0.mParentView;
        r4 = r4.isScaleWindow();
        if (r4 == 0) goto L_0x0107;
    L_0x00bf:
        r42 = r14.getRootView();
        r51 = r42.getLayoutParams();
        r0 = r51;
        r4 = r0 instanceof android.view.WindowManager.LayoutParams;
        if (r4 == 0) goto L_0x0107;
    L_0x00cd:
        r53 = r51;
        r53 = (android.view.WindowManager.LayoutParams) r53;
        r0 = r54;
        r4 = r0.mContext;
        r15 = r4.getBaseActivityToken();
        r0 = r54;
        r4 = r0.mContext;
        r5 = "multiwindow_facade";
        r39 = r4.getSystemService(r5);
        r39 = (com.samsung.android.multiwindow.MultiWindowFacade) r39;
        r0 = r39;
        r4 = r0.getStackPosition(r15);
        r0 = r54;
        r0.mPenWindowStartPos = r4;
        r0 = r54;
        r4 = r0.mPenWindowStartPos;
        if (r4 == 0) goto L_0x04f8;
    L_0x00f6:
        r0 = r54;
        r4 = r0.mPenWindowStartPos;
        r4 = r4.x;
        r0 = r54;
        r5 = r0.mPenWindowStartPos;
        r5 = r5.y;
        r0 = r26;
        r0.offset(r4, r5);
    L_0x0107:
        r0 = r54;
        r4 = r0.mAnchorView;
        r43 = r4.getRootView();
        r36 = 0;
        r45 = r43.getWidth();
        r44 = r43.getHeight();
        r0 = r27;
        r4 = r0.widthPixels;
        r0 = r45;
        if (r0 != r4) goto L_0x012b;
    L_0x0121:
        r0 = r27;
        r4 = r0.heightPixels;
        r0 = r44;
        if (r0 != r4) goto L_0x012b;
    L_0x0129:
        r36 = 1;
    L_0x012b:
        r4 = r14.getApplicationWindowToken();
        r5 = r14.getWindowToken();
        if (r4 != r5) goto L_0x0501;
    L_0x0135:
        r4 = 0;
        r4 = r12[r4];
        r5 = 0;
        r5 = r11[r5];
        r4 = r4 - r5;
        r0 = r54;
        r0.mWindowGapX = r4;
        r4 = 1;
        r4 = r12[r4];
        r5 = 1;
        r5 = r11[r5];
        r4 = r4 - r5;
        r0 = r54;
        r0.mWindowGapY = r4;
        r4 = 1;
        r0 = r54;
        r0.mCoordinatesOfAnchorView = r4;
        r13 = new android.graphics.Rect;
        r4 = 0;
        r4 = r11[r4];
        r5 = 1;
        r5 = r11[r5];
        r6 = 0;
        r6 = r11[r6];
        r7 = r14.getWidth();
        r6 = r6 + r7;
        r7 = 1;
        r7 = r11[r7];
        r8 = r14.getHeight();
        r7 = r7 + r8;
        r13.<init>(r4, r5, r6, r7);
    L_0x016b:
        r0 = r26;
        r4 = r0.left;
        if (r4 >= 0) goto L_0x01d3;
    L_0x0171:
        r0 = r26;
        r4 = r0.top;
        if (r4 >= 0) goto L_0x01d3;
    L_0x0177:
        r0 = r54;
        r4 = r0.mParentView;
        r42 = r4.getRootView();
        r51 = r42.getLayoutParams();
        r0 = r51;
        r4 = r0 instanceof android.view.WindowManager.LayoutParams;
        if (r4 == 0) goto L_0x01d3;
    L_0x0189:
        r53 = r51;
        r53 = (android.view.WindowManager.LayoutParams) r53;
        r0 = r53;
        r4 = r0.systemUiVisibility;
        r0 = r53;
        r5 = r0.subtreeSystemUiVisibility;
        r4 = r4 | r5;
        r4 = r4 & 1028;
        if (r4 != 0) goto L_0x0553;
    L_0x019a:
        r37 = 1;
    L_0x019c:
        r49 = 0;
        r0 = r53;
        r4 = r0.flags;
        r4 = r4 & 512;
        r5 = 1;
        if (r4 != r5) goto L_0x01b8;
    L_0x01a7:
        if (r37 == 0) goto L_0x01b8;
    L_0x01a9:
        r0 = r54;
        r4 = r0.mContext;
        r4 = r4.getResources();
        r5 = 17104919; // 0x1050017 float:2.4428306E-38 double:8.450953E-317;
        r49 = r4.getDimensionPixelSize(r5);
    L_0x01b8:
        r4 = 0;
        r0 = r26;
        r0.left = r4;
        r0 = r49;
        r1 = r26;
        r1.top = r0;
        r0 = r27;
        r4 = r0.widthPixels;
        r0 = r26;
        r0.right = r4;
        r0 = r27;
        r4 = r0.heightPixels;
        r0 = r26;
        r0.bottom = r4;
    L_0x01d3:
        r4 = 0;
        r0 = r26;
        r5 = r0.left;
        r4 = java.lang.Math.max(r4, r5);
        r0 = r54;
        r0.mDisplayFrameLeft = r4;
        r4 = 0;
        r0 = r26;
        r5 = r0.top;
        r4 = java.lang.Math.max(r4, r5);
        r0 = r54;
        r0.mDisplayFrameRight = r4;
        r0 = r54;
        r4 = r0.mDisplayFrameRight;
        r0 = r54;
        r5 = r0.mDisplayFrameLeft;
        r4 = r4 - r5;
        r0 = r54;
        r0.mDisplayWidthToComputeAniWidth = r4;
        r0 = r54;
        r4 = r0.mContentLP;
        if (r4 != 0) goto L_0x0557;
    L_0x0200:
        r0 = r27;
        r4 = r0.widthPixels;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r52 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
        r0 = r27;
        r4 = r0.heightPixels;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r31 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
    L_0x0214:
        r0 = r54;
        r4 = r0.mContentView;
        r0 = r52;
        r1 = r31;
        r4.measure(r0, r1);
        r4 = 0;
        r0 = r54;
        r0.mNeedToMeasureContentView = r4;
        r0 = r54;
        r4 = r0.mContentView;
        r25 = r4.getMeasuredWidth();
        r0 = r54;
        r4 = r0.mContentView;
        r23 = r4.getMeasuredHeight();
        r0 = r54;
        r4 = r0.mPopup;
        r0 = r25;
        r4.setWidth(r0);
        r0 = r54;
        r4 = r0.mPopup;
        r0 = r23;
        r4.setHeight(r0);
        r0 = r54;
        r1 = r26;
        r2 = r25;
        r3 = r23;
        r0.computePopupPositionInternal(r13, r1, r2, r3);
        r4 = new android.graphics.Rect;
        r5 = r13.left;
        r6 = r13.top;
        r7 = r13.right;
        r8 = r13.bottom;
        r4.<init>(r5, r6, r7, r8);
        r0 = r54;
        r0.mReferncedAnchorRect = r4;
        r0 = r54;
        r0 = r0.mPopupPosX;
        r40 = r0;
        r0 = r54;
        r0 = r0.mPopupPosY;
        r41 = r0;
        r16 = 0;
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 2;
        if (r4 != r5) goto L_0x0598;
    L_0x0277:
        r4 = r41 + r23;
        r5 = r13.top;
        if (r4 <= r5) goto L_0x028d;
    L_0x027d:
        r4 = r13.bottom;
        r0 = r41;
        if (r0 >= r4) goto L_0x028d;
    L_0x0283:
        r0 = r26;
        r4 = r0.top;
        r4 = r4 + r41;
        r5 = r13.bottom;
        if (r4 < r5) goto L_0x028f;
    L_0x028d:
        r16 = 1;
    L_0x028f:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        if (r4 != 0) goto L_0x029b;
    L_0x0295:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        if (r4 == 0) goto L_0x0861;
    L_0x029b:
        if (r16 == 0) goto L_0x0861;
    L_0x029d:
        r35 = 1;
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r0 = r54;
        r1 = r27;
        r38 = r0.convertDPtoPX(r4, r1);
        r17 = 0;
        r22 = 0;
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 2;
        if (r4 != r5) goto L_0x05af;
    L_0x02b4:
        r4 = r13.left;
        r0 = r40;
        r4 = java.lang.Math.min(r0, r4);
        r5 = 0;
        r17 = java.lang.Math.max(r4, r5);
        r4 = r40 + r25;
        r5 = r13.right;
        r4 = java.lang.Math.max(r4, r5);
        r0 = r27;
        r5 = r0.widthPixels;
        r22 = java.lang.Math.min(r4, r5);
    L_0x02d1:
        r0 = r17;
        r1 = r54;
        r1.mContainerLeftOnWindow = r0;
        r4 = r13.centerY();
        r0 = r41;
        if (r0 <= r4) goto L_0x05f3;
    L_0x02df:
        r35 = 0;
    L_0x02e1:
        r0 = r54;
        r4 = r0.mContentContainer;
        if (r4 != 0) goto L_0x0314;
    L_0x02e7:
        r4 = new android.widget.HoverPopupWindow$HoverPopupContainer;
        r0 = r54;
        r5 = r0.mContext;
        r0 = r54;
        r4.<init>(r5);
        r0 = r54;
        r0.mContentContainer = r4;
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r4.setBackgroundColor(r5);
        r4 = "HoverPopupWindow";
        r5 = "FingerHoverPopupWindow: kdhpoint2";
        android.util.Log.d(r4, r5);
        r0 = r54;
        r4 = r0.mContentContainer;
        r0 = r54;
        r5 = r0.mGuideRingDrawableId;
        r0 = r54;
        r6 = r0.mGuideLineColor;
        r4.setGuideLine(r5, r6);
    L_0x0314:
        r0 = r54;
        r4 = r0.mContentContainer;
        if (r4 == 0) goto L_0x0335;
    L_0x031a:
        r0 = r54;
        r4 = r0.mOverTopBoundary;
        if (r4 != 0) goto L_0x0326;
    L_0x0320:
        r0 = r54;
        r4 = r0.misGravityBottomUnder;
        if (r4 == 0) goto L_0x05f7;
    L_0x0326:
        r4 = "HoverPopupWindow";
        r5 = "FingerHoverPopupWindow: Call setOverTopForCotainer(true)";
        android.util.Log.d(r4, r5);
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 1;
        r4.setOverTopForCotainer(r5);
    L_0x0335:
        r0 = r54;
        r4 = r0.mContentView;
        r24 = r4.getLayoutParams();
        if (r24 != 0) goto L_0x0608;
    L_0x033f:
        r0 = r54;
        r4 = r0.mContentView;
        r5 = new android.widget.FrameLayout$LayoutParams;
        r0 = r25;
        r1 = r23;
        r5.<init>(r0, r1);
        r4.setLayoutParams(r5);
    L_0x034f:
        r0 = r54;
        r4 = r0.mContentContainer;
        r4 = r4.getChildCount();
        if (r4 == 0) goto L_0x0373;
    L_0x0359:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r4 = r4.getChildAt(r5);
        r0 = r54;
        r5 = r0.mContentView;
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x0373;
    L_0x036c:
        r0 = r54;
        r4 = r0.mContentContainer;
        r4.removeAllViews();
    L_0x0373:
        r0 = r54;
        r4 = r0.mContentContainer;
        r4 = r4.getChildCount();
        if (r4 != 0) goto L_0x0388;
    L_0x037d:
        r0 = r54;
        r4 = r0.mContentContainer;
        r0 = r54;
        r5 = r0.mContentView;
        r4.addView(r5);
    L_0x0388:
        r0 = r54;
        r4 = r0.mPopup;
        r5 = -2;
        r4.setWidth(r5);
        r0 = r54;
        r4 = r0.mPopup;
        r5 = -2;
        r4.setHeight(r5);
        r4 = r17 - r40;
        r19 = java.lang.Math.abs(r4);
        r4 = r40 + r25;
        r4 = r22 - r4;
        r20 = java.lang.Math.abs(r4);
        r21 = 0;
        r18 = 0;
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        if (r4 == 0) goto L_0x0628;
    L_0x03b0:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        if (r4 == 0) goto L_0x0628;
    L_0x03b6:
        if (r35 == 0) goto L_0x0616;
    L_0x03b8:
        r18 = r38;
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r0 = r19;
        r1 = r20;
        r2 = r18;
        r4.setPadding(r0, r5, r1, r2);
    L_0x03c8:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 2;
        if (r4 != r5) goto L_0x067a;
    L_0x03cf:
        if (r35 == 0) goto L_0x03d3;
    L_0x03d1:
        r40 = r17;
    L_0x03d3:
        r0 = r54;
        r4 = r0.mHoveringPointX;
        r4 = r4 - r40;
        r0 = r54;
        r5 = r0.mWindowGapX;
        r32 = r4 - r5;
        r0 = r54;
        r4 = r0.mHoveringPointY;
        r4 = r4 - r41;
        r0 = r54;
        r5 = r0.mWindowGapY;
        r33 = r4 - r5;
        if (r35 == 0) goto L_0x072f;
    L_0x03ed:
        r50 = r14.getViewRootImpl();
        if (r50 == 0) goto L_0x047c;
    L_0x03f3:
        r46 = r50.getMultiWindowScale();
        r30 = new android.graphics.PointF;
        r4 = 0;
        r5 = 0;
        r0 = r30;
        r0.<init>(r4, r5);
        r0 = r46;
        r4 = r0.x;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0414;
    L_0x040a:
        r0 = r46;
        r4 = r0.y;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x06ef;
    L_0x0414:
        r4 = r14.getApplicationWindowToken();
        r5 = r14.getWindowToken();
        if (r4 != r5) goto L_0x068d;
    L_0x041e:
        r0 = r54;
        r4 = r0.mHoveringPointX;
        r4 = (float) r4;
        r0 = r40;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.x;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r26;
        r5 = r0.left;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.x;
        r4 = r4 / r5;
        r0 = r30;
        r0.x = r4;
        r0 = r54;
        r4 = r0.mHoveringPointY;
        r4 = (float) r4;
        r0 = r41;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.y;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r26;
        r5 = r0.top;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.y;
        r4 = r4 / r5;
        r0 = r30;
        r0.y = r4;
    L_0x0458:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x06c9;
    L_0x045f:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r23 - r6;
        r0 = r30;
        r7 = r0.x;
        r7 = (int) r7;
        r0 = r30;
        r8 = r0.y;
        r8 = (int) r8;
        r9 = 0;
        r10 = 1;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
    L_0x047c:
        r4 = r25 / 2;
        r47 = r19 + r4;
        r0 = r54;
        r4 = r0.mGuideLineFadeOffset;
        r48 = r23 - r4;
        r28 = r32;
        r29 = r33;
        r4 = r19 + 10;
        r0 = r28;
        if (r0 >= r4) goto L_0x0490;
    L_0x0490:
        r0 = r40;
        r1 = r54;
        r1.mPopupPosX = r0;
        r0 = r41;
        r1 = r54;
        r1.mPopupPosY = r0;
        r0 = r54;
        r4 = r0.mPopup;
        r0 = r54;
        r5 = r0.mAnimationStyle;
        r4.setAnimationStyle(r5);
        r0 = r54;
        r4 = r0.mIsFHAnimationEnabled;
        if (r4 != 0) goto L_0x0006;
    L_0x04ad:
        r0 = r54;
        r4 = r0.mContentContainer;
        if (r4 == 0) goto L_0x0006;
    L_0x04b3:
        r4 = "HoverPopupWindow";
        r5 = "HoverPopupWindow.computePopupPosition() : Call setFHmoveAnimationOffset(0)";
        android.util.Log.d(r4, r5);
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r4.setFHmoveAnimationOffset(r5);
        goto L_0x0006;
    L_0x04c4:
        r0 = r54;
        r14 = r0.mParentView;
        goto L_0x0023;
    L_0x04ca:
        r4 = 0;
        r5 = 0;
        r5 = r11[r5];
        r6 = 0;
        r6 = r34[r6];
        r6 = (int) r6;
        r5 = r5 - r6;
        r11[r4] = r5;
        r4 = 1;
        r5 = 1;
        r5 = r11[r5];
        r6 = 1;
        r6 = r34[r6];
        r6 = (int) r6;
        r5 = r5 - r6;
        r11[r4] = r5;
        r4 = 0;
        r5 = 0;
        r5 = r12[r5];
        r6 = 0;
        r6 = r34[r6];
        r6 = (int) r6;
        r5 = r5 - r6;
        r12[r4] = r5;
        r4 = 1;
        r5 = 1;
        r5 = r12[r5];
        r6 = 1;
        r6 = r34[r6];
        r6 = (int) r6;
        r5 = r5 - r6;
        r12[r4] = r5;
        goto L_0x00ab;
    L_0x04f8:
        r4 = "HoverPopupWindow";
        r5 = "HoverPopupWindow: computePopupPosition : mPenWindowStartPos == null";
        android.util.Log.d(r4, r5);
        goto L_0x0107;
    L_0x0501:
        r4 = 2;
        r0 = r54;
        r0.mCoordinatesOfAnchorView = r4;
        r4 = 0;
        r0 = r54;
        r0.mWindowGapX = r4;
        r4 = 0;
        r0 = r54;
        r0.mWindowGapY = r4;
        r13 = new android.graphics.Rect;
        r4 = 0;
        r4 = r12[r4];
        r5 = 1;
        r5 = r12[r5];
        r6 = 0;
        r6 = r12[r6];
        r7 = r14.getWidth();
        r6 = r6 + r7;
        r7 = 1;
        r7 = r12[r7];
        r8 = r14.getHeight();
        r7 = r7 + r8;
        r13.<init>(r4, r5, r6, r7);
        r0 = r26;
        r4 = r0.left;
        if (r4 >= 0) goto L_0x016b;
    L_0x0531:
        r0 = r26;
        r4 = r0.top;
        if (r4 >= 0) goto L_0x016b;
    L_0x0537:
        r4 = 0;
        r0 = r26;
        r0.left = r4;
        r0 = r27;
        r4 = r0.widthPixels;
        r0 = r26;
        r0.right = r4;
        r4 = 0;
        r0 = r26;
        r0.top = r4;
        r0 = r27;
        r4 = r0.heightPixels;
        r0 = r26;
        r0.bottom = r4;
        goto L_0x016b;
    L_0x0553:
        r37 = 0;
        goto L_0x019c;
    L_0x0557:
        r0 = r54;
        r4 = r0.mContentLP;
        r4 = r4.width;
        if (r4 < 0) goto L_0x0581;
    L_0x055f:
        r0 = r54;
        r4 = r0.mContentLP;
        r4 = r4.width;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r52 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
    L_0x056b:
        r0 = r54;
        r4 = r0.mContentLP;
        r4 = r4.height;
        if (r4 < 0) goto L_0x058c;
    L_0x0573:
        r0 = r54;
        r4 = r0.mContentLP;
        r4 = r4.height;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r31 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
        goto L_0x0214;
    L_0x0581:
        r0 = r27;
        r4 = r0.widthPixels;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r52 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
        goto L_0x056b;
    L_0x058c:
        r0 = r27;
        r4 = r0.heightPixels;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r31 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5);
        goto L_0x0214;
    L_0x0598:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 1;
        if (r4 != r5) goto L_0x028f;
    L_0x059f:
        r4 = r41 + r23;
        r5 = r13.top;
        if (r4 <= r5) goto L_0x05ab;
    L_0x05a5:
        r4 = r13.bottom;
        r0 = r41;
        if (r0 < r4) goto L_0x028f;
    L_0x05ab:
        r16 = 1;
        goto L_0x028f;
    L_0x05af:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 1;
        if (r4 != r5) goto L_0x02d1;
    L_0x05b6:
        r4 = r13.left;
        r0 = r40;
        r4 = java.lang.Math.min(r0, r4);
        r0 = r26;
        r5 = r0.left;
        r5 = -r5;
        r17 = java.lang.Math.max(r4, r5);
        r4 = r40 + r25;
        r5 = r13.right;
        r4 = java.lang.Math.max(r4, r5);
        r0 = r26;
        r5 = r0.right;
        r0 = r26;
        r6 = r0.left;
        r5 = r5 - r6;
        r22 = java.lang.Math.min(r4, r5);
        r0 = r54;
        r4 = r0.mFullTextPopupRightLimit;
        r5 = -1;
        if (r4 == r5) goto L_0x02d1;
    L_0x05e3:
        r0 = r54;
        r4 = r0.mFullTextPopupRightLimit;
        r0 = r22;
        if (r0 <= r4) goto L_0x02d1;
    L_0x05eb:
        r0 = r54;
        r0 = r0.mFullTextPopupRightLimit;
        r22 = r0;
        goto L_0x02d1;
    L_0x05f3:
        r35 = 1;
        goto L_0x02e1;
    L_0x05f7:
        r4 = "HoverPopupWindow";
        r5 = "FingerHoverPopupWindow: Call setOverTopForCotainer(false)";
        android.util.Log.d(r4, r5);
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r4.setOverTopForCotainer(r5);
        goto L_0x0335;
    L_0x0608:
        r0 = r25;
        r1 = r24;
        r1.width = r0;
        r0 = r23;
        r1 = r24;
        r1.height = r0;
        goto L_0x034f;
    L_0x0616:
        r21 = r38;
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r0 = r19;
        r1 = r21;
        r2 = r20;
        r4.setPadding(r0, r1, r2, r5);
        goto L_0x03c8;
    L_0x0628:
        if (r35 == 0) goto L_0x0653;
    L_0x062a:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 2;
        if (r4 != r5) goto L_0x0643;
    L_0x0631:
        r18 = r38;
    L_0x0633:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r0 = r19;
        r1 = r20;
        r2 = r18;
        r4.setPadding(r0, r5, r1, r2);
        goto L_0x03c8;
    L_0x0643:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 1;
        if (r4 != r5) goto L_0x0633;
    L_0x064a:
        r4 = r13.bottom;
        r4 = r4 + r38;
        r5 = r41 + r23;
        r18 = r4 - r5;
        goto L_0x0633;
    L_0x0653:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 2;
        if (r4 != r5) goto L_0x066c;
    L_0x065a:
        r21 = r38;
    L_0x065c:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = 0;
        r0 = r19;
        r1 = r21;
        r2 = r20;
        r4.setPadding(r0, r1, r2, r5);
        goto L_0x03c8;
    L_0x066c:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 1;
        if (r4 != r5) goto L_0x065c;
    L_0x0673:
        r4 = r13.top;
        r4 = r4 - r38;
        r21 = r41 - r4;
        goto L_0x065c;
    L_0x067a:
        r0 = r54;
        r4 = r0.mCoordinatesOfAnchorView;
        r5 = 1;
        if (r4 != r5) goto L_0x03d3;
    L_0x0681:
        if (r35 == 0) goto L_0x0687;
    L_0x0683:
        r40 = r17;
        goto L_0x03d3;
    L_0x0687:
        r40 = r17;
        r41 = r41 - r21;
        goto L_0x03d3;
    L_0x068d:
        r0 = r54;
        r4 = r0.mHoveringPointX;
        r4 = (float) r4;
        r0 = r40;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.x;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r54;
        r5 = r0.mWindowGapX;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.x;
        r4 = r4 / r5;
        r0 = r30;
        r0.x = r4;
        r0 = r54;
        r4 = r0.mHoveringPointY;
        r4 = (float) r4;
        r0 = r41;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.y;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r54;
        r5 = r0.mWindowGapY;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.y;
        r4 = r4 / r5;
        r0 = r30;
        r0.y = r4;
        goto L_0x0458;
    L_0x06c9:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x047c;
    L_0x06d0:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r23 - r6;
        r0 = r30;
        r7 = r0.x;
        r7 = (int) r7;
        r0 = r30;
        r8 = r0.y;
        r8 = (int) r8;
        r9 = 1;
        r10 = 0;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x047c;
    L_0x06ef:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x070f;
    L_0x06f6:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r23 - r6;
        r9 = 0;
        r10 = 1;
        r7 = r32;
        r8 = r33;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x047c;
    L_0x070f:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x047c;
    L_0x0716:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r23 - r6;
        r9 = 1;
        r10 = 0;
        r7 = r32;
        r8 = r33;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x047c;
    L_0x072f:
        r50 = r14.getViewRootImpl();
        if (r50 == 0) goto L_0x0490;
    L_0x0735:
        r46 = r50.getMultiWindowScale();
        r30 = new android.graphics.PointF;
        r4 = 0;
        r5 = 0;
        r0 = r30;
        r0.<init>(r4, r5);
        r0 = r46;
        r4 = r0.x;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0756;
    L_0x074c:
        r0 = r46;
        r4 = r0.y;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x0821;
    L_0x0756:
        r4 = r14.getApplicationWindowToken();
        r5 = r14.getWindowToken();
        if (r4 != r5) goto L_0x07c0;
    L_0x0760:
        r0 = r54;
        r4 = r0.mHoveringPointX;
        r4 = (float) r4;
        r0 = r40;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.x;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r26;
        r5 = r0.left;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.x;
        r4 = r4 / r5;
        r0 = r30;
        r0.x = r4;
        r0 = r54;
        r4 = r0.mHoveringPointY;
        r4 = (float) r4;
        r0 = r41;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.y;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r26;
        r5 = r0.top;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.y;
        r4 = r4 / r5;
        r0 = r30;
        r0.y = r4;
    L_0x079a:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x07fb;
    L_0x07a1:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r6 + r21;
        r0 = r30;
        r7 = r0.x;
        r7 = (int) r7;
        r0 = r30;
        r8 = r0.y;
        r8 = (int) r8;
        r9 = 0;
        r10 = 1;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x0490;
    L_0x07c0:
        r0 = r54;
        r4 = r0.mHoveringPointX;
        r4 = (float) r4;
        r0 = r40;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.x;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r54;
        r5 = r0.mWindowGapX;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.x;
        r4 = r4 / r5;
        r0 = r30;
        r0.x = r4;
        r0 = r54;
        r4 = r0.mHoveringPointY;
        r4 = (float) r4;
        r0 = r41;
        r5 = (float) r0;
        r0 = r46;
        r6 = r0.y;
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r54;
        r5 = r0.mWindowGapY;
        r5 = (float) r5;
        r4 = r4 - r5;
        r0 = r46;
        r5 = r0.y;
        r4 = r4 / r5;
        r0 = r30;
        r0.y = r4;
        goto L_0x079a;
    L_0x07fb:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x0490;
    L_0x0802:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r6 + r21;
        r0 = r30;
        r7 = r0.x;
        r7 = (int) r7;
        r0 = r30;
        r8 = r0.y;
        r8 = (int) r8;
        r9 = 1;
        r10 = 0;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x0490;
    L_0x0821:
        r0 = r54;
        r4 = r0.mIsFHGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x0841;
    L_0x0828:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r6 + r21;
        r9 = 0;
        r10 = 1;
        r7 = r32;
        r8 = r33;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x0490;
    L_0x0841:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        r5 = 1;
        if (r4 != r5) goto L_0x0490;
    L_0x0848:
        r0 = r54;
        r4 = r0.mContentContainer;
        r5 = r25 / 2;
        r5 = r5 + r19;
        r0 = r54;
        r6 = r0.mGuideLineFadeOffset;
        r6 = r6 + r21;
        r9 = 1;
        r10 = 0;
        r7 = r32;
        r8 = r33;
        r4.setGuideLine(r5, r6, r7, r8, r9, r10);
        goto L_0x0490;
    L_0x0861:
        r0 = r54;
        r4 = r0.mIsPopupTouchable;
        if (r4 == 0) goto L_0x08da;
    L_0x0867:
        r0 = r54;
        r4 = r0.mIsGuideLineEnabled;
        if (r4 != 0) goto L_0x08da;
    L_0x086d:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        if (r4 != 0) goto L_0x0882;
    L_0x0873:
        r4 = new android.widget.HoverPopupWindow$TouchablePopupContainer;
        r0 = r54;
        r5 = r0.mContext;
        r0 = r54;
        r4.<init>(r5);
        r0 = r54;
        r0.mTouchableContainer = r4;
    L_0x0882:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r4 = r4.getChildCount();
        if (r4 != 0) goto L_0x08b4;
    L_0x088c:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r0 = r54;
        r5 = r0.mContentView;
        r4.addView(r5);
    L_0x0897:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        if (r4 == 0) goto L_0x0490;
    L_0x089d:
        r0 = r54;
        r4 = r0.mToolType;
        r5 = 3;
        if (r4 == r5) goto L_0x0490;
    L_0x08a4:
        r4 = "HoverPopupWindow";
        r5 = "computePopupPosition: Call resetTimeout()";
        android.util.Log.d(r4, r5);
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r4.resetTimeout();
        goto L_0x0490;
    L_0x08b4:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r5 = 0;
        r4 = r4.getChildAt(r5);
        r0 = r54;
        r5 = r0.mContentView;
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x0897;
    L_0x08c7:
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r4.removeAllViews();
        r0 = r54;
        r4 = r0.mTouchableContainer;
        r0 = r54;
        r5 = r0.mContentView;
        r4.addView(r5);
        goto L_0x0897;
    L_0x08da:
        r0 = r54;
        r4 = r0.mContentContainer;
        if (r4 == 0) goto L_0x0490;
    L_0x08e0:
        r0 = r54;
        r4 = r0.mContentContainer;
        r4.removeAllViews();
        r4 = 0;
        r0 = r54;
        r0.mContentContainer = r4;
        goto L_0x0490;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.HoverPopupWindow.computePopupPosition(android.view.View, int, int, int):void");
    }

    private void computePopupPositionInternal(Rect anchorRect, Rect displayFrame, int contentWidth, int contentHeight) {
        DisplayMetrics displayMetrics;
        LayoutParams vlp;
        boolean isSystemUiVisible;
        this.mAnchorRect = anchorRect;
        this.mDisplayFrame = displayFrame;
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        int posX = this.mPopupOffsetX;
        int posY = this.mPopupOffsetY;
        int hGravity = this.mPopupGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int vGravity = this.mPopupGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int tooltipShiftX = this.mContext.getResources().getDimensionPixelSize(R.dimen.hover_tooltip_popup_shift);
        int tooltipTopMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.hover_tooltip_popup_top_margin);
        if (this.mPopupGravity != 0) {
            switch (hGravity) {
                case 1:
                    posX = anchorRect.centerX() - (contentWidth / 2);
                    break;
                case 3:
                    posX = anchorRect.left;
                    break;
                case 5:
                    posX = anchorRect.right - contentWidth;
                    break;
                case 257:
                    posX = displayFrame.centerX() - (contentWidth / 2);
                    break;
                case 259:
                    if (this.mPopupType != 1) {
                        posX = anchorRect.centerX() - contentWidth;
                        break;
                    } else {
                        posX = (anchorRect.centerX() - contentWidth) + tooltipShiftX;
                        break;
                    }
                case 261:
                    posX = anchorRect.centerX();
                    break;
                case 513:
                    posX = (this.mHoveringPointX - (contentWidth / 2)) - this.mWindowGapX;
                    break;
                case Gravity.LEFT_OUTSIDE /*771*/:
                    posX = anchorRect.left - contentWidth;
                    break;
                case Gravity.RIGHT_OUTSIDE /*1285*/:
                    posX = anchorRect.right;
                    break;
                default:
                    posX = this.mPopupOffsetX;
                    break;
            }
            posX += this.mPopupOffsetX;
            switch (vGravity) {
                case 16:
                    posY = anchorRect.centerY() - (contentHeight / 2);
                    break;
                case 48:
                    posY = anchorRect.top;
                    break;
                case 80:
                    posY = anchorRect.bottom - contentHeight;
                    break;
                case Gravity.TOP_ABOVE /*12336*/:
                    posY = anchorRect.top - contentHeight;
                    break;
                case Gravity.BOTTOM_UNDER /*20560*/:
                    if (this.mPopupType == 1) {
                        posY = anchorRect.bottom + tooltipTopMargin;
                    } else {
                        posY = anchorRect.bottom;
                    }
                    this.misGravityBottomUnder = true;
                    break;
                default:
                    posY = this.mPopupOffsetY;
                    break;
            }
            posY += this.mPopupOffsetY;
        } else if (this.mCoordinatesOfAnchorView == 2) {
            posX = this.mPopupOffsetX + displayFrame.left;
            posY = this.mPopupOffsetY + displayFrame.top;
        } else if (this.mCoordinatesOfAnchorView == 1) {
            posX = this.mPopupOffsetX;
            posY = this.mPopupOffsetY;
        }
        if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            vlp = this.mParentView.getRootView().getLayoutParams();
            isSystemUiVisible = false;
            if (vlp instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams wlp = (WindowManager.LayoutParams) vlp;
                isSystemUiVisible = ((wlp.systemUiVisibility | wlp.subtreeSystemUiVisibility) & 1028) == 0;
            }
            if (isSystemUiVisible) {
                int statusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
            }
            if (posY + contentHeight > displayMetrics.heightPixels && vGravity == 20560 && this.mPopupType == 1) {
                posX = (anchorRect.centerX() - (contentWidth / 2)) + this.mPopupOffsetX;
            }
        } else if (this.mCoordinatesOfAnchorView == 1 && posY + contentHeight > displayFrame.bottom - displayFrame.top && vGravity == 20560 && this.mPopupType == 1) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            if (anchorRect.top >= contentHeight) {
                if (displayFrame.top != this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height) || posY + contentHeight > displayFrame.bottom) {
                    posX = (anchorRect.centerX() - (contentWidth / 2)) + this.mPopupOffsetX;
                }
            } else {
                if ((displayFrame.top + posY) + contentHeight > displayMetrics.heightPixels) {
                    posX = (anchorRect.centerX() - (contentWidth / 2)) + this.mPopupOffsetX;
                }
            }
        }
        Log.d(TAG, "computePopupPositionInternal: check window boundary ");
        if (this.mParentView.isScaleWindow()) {
            displayMetrics = new DisplayMetrics();
            displayMetrics.setTo(this.mContext.getResources().getDisplayMetrics());
            if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
                MultiWindowFacade mwFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
                if (mwFacade != null) {
                    DisplayInfo realDisplayInfo = mwFacade.getSystemDisplayInfo();
                    if (realDisplayInfo != null) {
                        realDisplayInfo.getAppMetrics(displayMetrics);
                        displayMetrics.scaledDensity = displayMetrics.density * this.mContext.getResources().getConfiguration().fontScale;
                    }
                }
            }
            ViewRootImpl viewRoot;
            PointF scaleFactor;
            if (this.mCoordinatesOfAnchorView == 2) {
                viewRoot = (this.mAnchorView != null ? this.mAnchorView : this.mParentView).getViewRootImpl();
                if (viewRoot != null) {
                    scaleFactor = viewRoot.getMultiWindowScale();
                    if (posX < 0) {
                        posX = Math.max(0, posX);
                    } else {
                        if (displayFrame.left + ((int) (((float) (posX + contentWidth)) * scaleFactor.x)) > displayMetrics.widthPixels) {
                            posX = Math.min(posX, displayMetrics.widthPixels - ((int) (((float) contentWidth) * scaleFactor.x)));
                        }
                    }
                    if (this.mPenWindowStartPos != null) {
                        if ((((float) posX) * scaleFactor.x) + ((float) this.mPenWindowStartPos.x) < 0.0f) {
                            posX = Math.max(-((int) (((float) this.mPenWindowStartPos.x) / scaleFactor.x)), posX);
                        } else {
                            if (this.mPenWindowStartPos.x + ((int) (((float) (posX + contentWidth)) * scaleFactor.x)) > displayMetrics.widthPixels) {
                                posX = Math.min(posX, ((int) (((float) (displayMetrics.widthPixels - this.mPenWindowStartPos.x)) / scaleFactor.x)) - contentWidth);
                            }
                        }
                    }
                }
            } else if (this.mCoordinatesOfAnchorView == 1) {
                viewRoot = (this.mAnchorView != null ? this.mAnchorView : this.mParentView).getViewRootImpl();
                if (viewRoot != null) {
                    scaleFactor = viewRoot.getMultiWindowScale();
                    if (displayFrame.left + ((int) (((float) posX) * scaleFactor.x)) < 0) {
                        posX = 0;
                    } else {
                        if (displayFrame.left + ((int) (((float) (posX + contentWidth)) * scaleFactor.x)) >= displayMetrics.widthPixels) {
                            posX = Math.min(posX, ((int) (((float) (displayMetrics.widthPixels - displayFrame.left)) / scaleFactor.x)) - contentWidth);
                        } else {
                            if (posX + contentWidth > displayMetrics.widthPixels) {
                                posX = displayMetrics.widthPixels - contentWidth;
                            }
                        }
                    }
                }
            }
        } else if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            fulltextAirviewShiftX = this.mContext.getResources().getDimensionPixelSize(R.dimen.hover_fulltext_popup_left_right_shift);
            if (this.mPopupType == 1) {
                if (posX < 0) {
                    posX = Math.max(0, posX);
                } else {
                    if (posX + contentWidth > displayMetrics.widthPixels) {
                        posX = Math.min(posX, displayMetrics.widthPixels - contentWidth);
                    }
                }
            } else if (posX < 0) {
                posX = Math.max(fulltextAirviewShiftX, posX);
            } else {
                if (posX + contentWidth > displayMetrics.widthPixels) {
                    posX = Math.min(posX, (displayMetrics.widthPixels - contentWidth) - fulltextAirviewShiftX);
                }
            }
        } else if (this.mCoordinatesOfAnchorView == 1) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            Context context;
            Rect cocktailRect;
            if (displayFrame.left + posX <= 0) {
                fulltextAirviewShiftX = this.mContext.getResources().getDimensionPixelSize(R.dimen.hover_fulltext_popup_left_right_shift);
                posX = Math.min(posX, (displayFrame.right - displayFrame.left) - contentWidth);
                if (this.mPopupType == 1) {
                    posX = Math.max(-displayFrame.left, posX);
                } else {
                    posX = Math.max((-displayFrame.left) + fulltextAirviewShiftX, posX);
                }
                context = this.mContext;
                try {
                    cocktailRect = IWindowManager.Stub.asInterface(ServiceManager.getService("window")).getCocktailBarFrame();
                } catch (RemoteException e) {
                    cocktailRect = null;
                    Log.e(TAG, "windowManager.getCocktailBarFrame() :: error occurred");
                }
                if (cocktailRect != null && cocktailRect.left > 0) {
                    vlp = this.mParentView.getRootView().getLayoutParams();
                    if (vlp instanceof WindowManager.LayoutParams) {
                        wlp = (WindowManager.LayoutParams) vlp;
                        if (wlp.type == 2220 || wlp.type == 98) {
                            posX = Math.max(cocktailRect.left, posX);
                        } else {
                            posX = Math.max(0, posX);
                        }
                    }
                }
            } else {
                if ((displayFrame.left + posX) + contentWidth >= displayMetrics.widthPixels) {
                    fulltextAirviewShiftX = this.mContext.getResources().getDimensionPixelSize(R.dimen.hover_fulltext_popup_left_right_shift);
                    try {
                        context = this.mContext;
                        cocktailRect = IWindowManager.Stub.asInterface(ServiceManager.getService("window")).getCocktailBarFrame();
                        if (cocktailRect == null || cocktailRect.right <= 0) {
                            if (this.mPopupType == 1) {
                                posX = Math.min(posX, (displayMetrics.widthPixels - displayFrame.left) - contentWidth);
                            } else {
                                posX = Math.min(posX, ((displayMetrics.widthPixels - displayFrame.left) - contentWidth) - fulltextAirviewShiftX);
                            }
                            this.mFullTextPopupRightLimit = posX + contentWidth;
                        } else {
                            WindowManager wm = (WindowManager) this.mContext.getSystemService("window");
                            DisplayMetrics metrics = new DisplayMetrics();
                            wm.getDefaultDisplay().getRealMetrics(metrics);
                            if (this.mPopupType == 1) {
                                posX = Math.min(posX, ((metrics.widthPixels - cocktailRect.right) - displayFrame.left) - contentWidth);
                            } else {
                                posX = Math.min(posX, (((metrics.widthPixels - cocktailRect.right) - displayFrame.left) - contentWidth) - fulltextAirviewShiftX);
                            }
                            this.mFullTextPopupRightLimit = posX + contentWidth;
                        }
                    } catch (Exception e2) {
                        Log.d(TAG, "HoverPopupWindow:computePopupPositionInternal : WINDOW_SERVICE remote exception occurred. ");
                    }
                } else if (displayFrame.left > 0) {
                    context = this.mContext;
                    try {
                        cocktailRect = IWindowManager.Stub.asInterface(ServiceManager.getService("window")).getCocktailBarFrame();
                    } catch (RemoteException e3) {
                        cocktailRect = null;
                        Log.e(TAG, "windowManager.getCocktailBarFrame() :: error occurred");
                    }
                    if (cocktailRect != null && cocktailRect.left > 0) {
                        vlp = this.mParentView.getRootView().getLayoutParams();
                        if (vlp instanceof WindowManager.LayoutParams) {
                            wlp = (WindowManager.LayoutParams) vlp;
                            if (wlp.type == 2220 || wlp.type == 98) {
                                posX = Math.max(cocktailRect.left, posX);
                            } else {
                                posX = Math.max(0, posX);
                            }
                        }
                    }
                    posX = Math.min(posX, (displayFrame.right - displayFrame.left) - contentWidth);
                }
            }
        }
        if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            vlp = this.mParentView.getRootView().getLayoutParams();
            isSystemUiVisible = false;
            if (vlp instanceof WindowManager.LayoutParams) {
                wlp = (WindowManager.LayoutParams) vlp;
                isSystemUiVisible = ((wlp.systemUiVisibility | wlp.subtreeSystemUiVisibility) & 1028) == 0;
            }
            statusBarHeight = 0;
            if (isSystemUiVisible) {
                statusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
            }
            if (posY >= statusBarHeight) {
                if (posY + contentHeight > displayMetrics.heightPixels) {
                    if (vGravity != 20560) {
                        Log.d(TAG, "computePopupPositionInternal: #5 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        posY = anchorRect.top - contentHeight;
                    } else if (anchorRect.top >= contentHeight) {
                        posY = (anchorRect.top - contentHeight) - this.mPopupOffsetY;
                        Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3: misGravityBottomUnder = " + this.misGravityBottomUnder);
                        if (this.misGravityBottomUnder) {
                            this.misGravityBottomUnder = false;
                            Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        }
                    }
                } else if (vGravity == 12336) {
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #6 set mOverTopBoundary = " + this.mOverTopBoundary);
                }
            } else if (vGravity != 12336) {
                Log.d(TAG, "computePopupPositionInternal #2-1: mOverTopBoundary = " + this.mOverTopBoundary);
                posY = Math.max(displayFrame.top, posY);
            } else if (displayMetrics.heightPixels - anchorRect.bottom >= contentHeight) {
                posY = anchorRect.bottom + this.mPopupOffsetY;
                Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                this.mOverTopBoundary = true;
            } else {
                if (displayMetrics.heightPixels - anchorRect.bottom > anchorRect.top - statusBarHeight) {
                    posY = anchorRect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                    this.mOverTopBoundary = true;
                } else {
                    posY = statusBarHeight;
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #2: mOverTopBoundary = " + this.mOverTopBoundary);
                }
            }
        } else if (this.mCoordinatesOfAnchorView == 1) {
            vlp = this.mParentView.getRootView().getLayoutParams();
            isSystemUiVisible = false;
            if (vlp instanceof WindowManager.LayoutParams) {
                wlp = (WindowManager.LayoutParams) vlp;
                isSystemUiVisible = ((wlp.systemUiVisibility | wlp.subtreeSystemUiVisibility) & 1028) == 0;
            }
            int realStatusBarHeight = 0;
            statusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
            if (isSystemUiVisible) {
                realStatusBarHeight = statusBarHeight;
            }
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            if (displayFrame.top + posY >= statusBarHeight) {
                if (posY + contentHeight > displayFrame.bottom - displayFrame.top) {
                    if (vGravity != 20560) {
                        Log.d(TAG, "computePopupPositionInternal: #5 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        posY = displayFrame.top != realStatusBarHeight ? Math.min((displayFrame.bottom - displayFrame.top) - contentHeight, posY) : Math.min(displayFrame.bottom - contentHeight, posY);
                    } else if (anchorRect.top < contentHeight) {
                        if ((displayFrame.top + posY) + contentHeight > displayMetrics.heightPixels) {
                            posY = (anchorRect.top - contentHeight) - this.mPopupOffsetY;
                            Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-2: misGravityBottomUnder = " + this.misGravityBottomUnder);
                            if (this.misGravityBottomUnder) {
                                this.misGravityBottomUnder = false;
                                Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                            }
                        }
                    } else if (displayFrame.top != statusBarHeight || posY + contentHeight > displayFrame.bottom) {
                        posY = (anchorRect.top - contentHeight) - this.mPopupOffsetY;
                        Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-2: misGravityBottomUnder = " + this.misGravityBottomUnder);
                        if (this.misGravityBottomUnder) {
                            this.misGravityBottomUnder = false;
                            Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        }
                    } else {
                        Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-1: misGravityBottomUnder = " + this.misGravityBottomUnder);
                    }
                } else if (vGravity == 12336) {
                    this.mOverTopBoundary = false;
                    this.misGravityBottomUnder = false;
                    if (posY < statusBarHeight && (posY + contentHeight) + statusBarHeight > anchorRect.top) {
                        posY = anchorRect.bottom;
                        this.misGravityBottomUnder = true;
                    }
                    Log.d(TAG, "computePopupPositionInternal: #6 set mOverTopBoundary = " + this.mOverTopBoundary);
                } else if (posY < statusBarHeight && displayFrame.top == statusBarHeight) {
                    posY = statusBarHeight;
                }
            } else if (vGravity != 12336) {
                Log.d(TAG, "computePopupPositionInternal #2-1: mOverTopBoundary = " + this.mOverTopBoundary);
                posY = Math.max(statusBarHeight, posY);
            } else if (((displayFrame.bottom - displayFrame.top) - anchorRect.bottom) - statusBarHeight >= contentHeight) {
                posY = anchorRect.bottom;
                if ((((displayFrame.bottom - displayFrame.top) - anchorRect.bottom) - statusBarHeight) - this.mPopupOffsetY >= contentHeight) {
                    posY += this.mPopupOffsetY;
                }
                Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                this.mOverTopBoundary = true;
            } else {
                if (((displayFrame.bottom - displayFrame.top) - anchorRect.bottom) - statusBarHeight > anchorRect.top) {
                    posY = anchorRect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                    this.mOverTopBoundary = true;
                } else if (((displayFrame.top + anchorRect.top) - contentHeight) - realStatusBarHeight > 0) {
                    this.mOverTopBoundary = false;
                } else if ((displayMetrics.heightPixels - (displayFrame.top + anchorRect.bottom)) - contentHeight > 0) {
                    posY = anchorRect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1-2");
                    this.mOverTopBoundary = true;
                } else {
                    posY = statusBarHeight;
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #2: mOverTopBoundary = " + this.mOverTopBoundary);
                }
            }
        }
        this.mPopupPosX = posX;
        this.mPopupPosY = posY;
        if (this.mParentView.isScaleWindow()) {
            wm = (WindowManager) this.mContext.getSystemService("window");
            if (wm != null) {
                displayMetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(displayMetrics);
                int yOffset = this.mParentView.getRootView().getHeight() - displayMetrics.heightPixels;
                if (yOffset > 0) {
                    this.mPopupPosY -= yOffset;
                }
            }
        }
    }

    public void updateHoverPopup() {
        if (this.mPopup == null || !this.mPopup.isShowing() || this.mNeedToMeasureContentView) {
            updateHoverPopup(this.mAnchorView != null ? this.mAnchorView : this.mParentView, this.mPopupGravity, this.mPopupOffsetX, this.mPopupOffsetY);
            return;
        }
        computePopupPositionInternal(this.mAnchorRect, this.mDisplayFrame, this.mContentWidth, this.mContentHeight);
        this.mPopup.update(this.mPopupPosX, this.mPopupPosY, -1, -1);
    }

    private void updateHoverPopup(View anchor, int gravity, int offsetX, int offsetY) {
        if (this.mPopup != null) {
            computePopupPosition(anchor, gravity, offsetX, offsetY);
            if (this.mContentWidth != 0 || this.mContentHeight != 0) {
                if (this.mIsPopupTouchable && this.mTouchableContainer != null) {
                    this.mPopup.setContentView(this.mTouchableContainer);
                } else if (!this.mIsGuideLineEnabled || this.mContentContainer == null) {
                    this.mPopup.setContentView(this.mContentView);
                } else {
                    this.mPopup.setContentView(this.mContentContainer);
                }
                if (this.mPopup.getContentView() == null) {
                    return;
                }
                if (this.mPopup.isShowing()) {
                    this.mPopup.update(this.mPopupPosX, this.mPopupPosY, this.mContentWidth, this.mContentHeight);
                } else if (anchor.getApplicationWindowToken() == null || anchor.getApplicationWindowToken() == anchor.getWindowToken()) {
                    this.mPopup.showAtLocation(anchor, 0, this.mPopupPosX, this.mPopupPosY);
                } else {
                    this.mPopup.showAtLocation(anchor.getApplicationWindowToken(), 0, this.mPopupPosX, this.mPopupPosY);
                }
            }
        }
    }

    public void setAnimationStyle(int aniStyle) {
        this.mAnimationStyle = aniStyle;
        if (this.mPopup != null) {
            this.mPopup.setAnimationStyle(this.mAnimationStyle);
        }
    }

    public void setTouchablePopup(boolean isTouchable) {
        this.mIsPopupTouchable = isTouchable;
        if (this.mPopup != null) {
            this.mPopup.setTouchable(this.mIsPopupTouchable);
        }
    }

    public void setGuideLineEnabled(boolean enabled) {
        this.mIsGuideLineEnabled = enabled;
    }

    public void setFHGuideLineEnabled(boolean enabled) {
        this.mIsFHGuideLineEnabledByApp = true;
        setFHGuideLineEnabledByApp(enabled, true);
    }

    public void setFHGuideLineEnabledByApp(boolean enabled, boolean calledByApp) {
        if (calledByApp) {
            this.mIsFHGuideLineEnabled = enabled;
            if (this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = true;
            } else if (!this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = false;
            }
        } else if (!this.mIsFHGuideLineEnabledByApp) {
            this.mIsFHGuideLineEnabled = enabled;
            if (this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = true;
            } else if (!this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = false;
            }
        }
    }

    public void setFHAnimationEnabled(boolean enabled) {
        this.mIsFHAnimationEnabledByApp = true;
        setFHAnimationEnabledByApp(enabled, true);
    }

    public void setFHAnimationEnabledByApp(boolean enabled, boolean calledByApp) {
        if (calledByApp) {
            this.mIsFHAnimationEnabled = enabled;
        } else if (!this.mIsFHAnimationEnabledByApp) {
            this.mIsFHAnimationEnabled = enabled;
        }
    }

    public void setInfoPickerMoveEabled(boolean enabled) {
        this.mIsInfoPickerMoveEabledByApp = true;
        setInfoPickerMoveEabledByApp(enabled, true);
    }

    public void setInfoPickerMoveEabledByApp(boolean enabled, boolean calledByApp) {
        if (calledByApp) {
            this.mIsInfoPickerMoveEabled = enabled;
        } else if (!this.mIsInfoPickerMoveEabledByApp) {
            this.mIsInfoPickerMoveEabled = enabled;
        }
    }

    public void setInfoPickerColorToAndMoreBottomImg(boolean enabled) {
        this.mIsSetInfoPickerColorToAndMoreBottomImg = enabled;
    }

    public void setFHSoundAndHapticEnabled(boolean enabled) {
        this.mIsFHSoundAndHapticEnabled = enabled;
    }

    public void setGuideLineFadeOffset(int offset) {
        this.mGuideLineFadeOffset = convertDPtoPX((float) offset, null);
    }

    public void setGuideLineStyle(int ringDrawable, int lineColor) {
        this.mGuideRingDrawableId = ringDrawable;
        this.mGuideLineColor = lineColor;
    }

    public boolean getFHGuideLineEnabled() {
        return this.mIsFHGuideLineEnabled;
    }

    public boolean getFHAnimationEnabled() {
        return this.mIsFHAnimationEnabled;
    }

    public boolean getInfoPickerMoveEabled() {
        return this.mIsInfoPickerMoveEabled;
    }

    public boolean onHoverEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        if (action == 9) {
            if (SystemClock.uptimeMillis() - event.getEventTime() > 1000) {
                return true;
            }
            if (this.mIsHoverPaddingEnabled) {
                if (pointInValidHoverArea(x, y)) {
                    this.mIsTryingShowPopup = true;
                } else {
                    this.mIsTryingShowPopup = false;
                }
            }
        } else if (action == 7) {
            int rawX = (int) event.getRawX();
            int rawY = (int) event.getRawY();
            if (this.mUspLevel <= 3) {
                rawX = (int) event.getRawXForScaledWindow();
                rawY = (int) event.getRawYForScaledWindow();
            }
            setHoveringPoint(rawX, rawY);
            if (this.mIsHoverPaddingEnabled) {
                boolean isPointInValidHoverArea = pointInValidHoverArea(x, y);
                if (!isPointInValidHoverArea || this.mIsTryingShowPopup) {
                    if (!(isPointInValidHoverArea || !this.mIsTryingShowPopup || this.mIsPopupTouchable)) {
                        this.mIsTryingShowPopup = false;
                        dismiss();
                        return true;
                    }
                } else if (SystemClock.uptimeMillis() - event.getEventTime() > 1000) {
                    this.mIsTryingShowPopup = false;
                    return true;
                } else {
                    this.mIsTryingShowPopup = true;
                    show();
                    return true;
                }
            }
            if ((this.mIsGuideLineEnabled || this.mIsFHGuideLineEnabled) && isShowing()) {
                View popupView = this.mPopup.getContentView();
                if (popupView instanceof HoverPopupContainer) {
                    View anchorView;
                    HoverPopupContainer containerView = (HoverPopupContainer) popupView;
                    if (!(this.mContentContainer == null || this.mContentView == null)) {
                        int infopickerLeftLimit = this.mContentContainer.getPaddingLeft();
                        this.mContentContainer.setPickerLimit(infopickerLeftLimit, this.mContentView.getWidth() + infopickerLeftLimit);
                    }
                    if (this.mAnchorView != null) {
                        anchorView = this.mAnchorView;
                    } else {
                        anchorView = this.mParentView;
                    }
                    ViewRootImpl viewRoot = anchorView.getViewRootImpl();
                    if (viewRoot != null) {
                        PointF scaleFactor = viewRoot.getMultiWindowScale();
                        PointF fPos = new PointF(0.0f, 0.0f);
                        if (scaleFactor.x == 1.0f && scaleFactor.y == 1.0f) {
                            containerView.setGuideLineEndPoint((rawX - this.mPopupPosX) - this.mWindowGapX, (rawY - this.mPopupPosY) - this.mWindowGapY);
                        } else {
                            Rect displayFrame = new Rect();
                            anchorView.getWindowVisibleContentFrame(displayFrame);
                            if (this.mPenWindowStartPos == null) {
                                this.mPenWindowStartPos = ((MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade")).getStackPosition(this.mContext.getBaseActivityToken());
                            }
                            if (!(this.mPenWindowStartPos == null || (this.mPenWindowStartPos.x == 0 && this.mPenWindowStartPos.y == 0))) {
                                displayFrame.offset(this.mPenWindowStartPos.x, this.mPenWindowStartPos.y);
                            }
                            fPos.x = ((((float) rawX) - (((float) this.mPopupPosX) * scaleFactor.x)) - ((float) displayFrame.left)) / scaleFactor.x;
                            fPos.y = ((((float) rawY) - (((float) this.mPopupPosY) * scaleFactor.y)) - ((float) displayFrame.top)) / scaleFactor.y;
                            containerView.setGuideLineEndPoint((int) fPos.x, (int) fPos.y);
                        }
                    }
                    if (!this.mPopup.isShowing()) {
                        containerView.updateDecoration();
                    } else if (this.mIsFHAnimationEnabled || this.mIsFHGuideLineEnabled) {
                        if (this.mIsFHGuideLineEnabled) {
                            containerView.setFHGuideLineForCotainer(true);
                        }
                        containerView.updateDecoration();
                    }
                }
            }
            if (this.mToolType != 3) {
                resetTimeout();
            }
            return true;
        } else if (action == 10) {
            if (this.mContentContainer != null) {
                this.mContentContainer.setPopupState(2);
            }
            if (this.mIsPopupTouchable) {
                if (this.mDismissHandler != null && this.mDismissHandler.hasMessages(1)) {
                    this.mDismissHandler.removeMessages(1);
                }
                if (isShowing()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void postDismiss(int ms) {
        this.mParentView.postDelayed(new Runnable() {
            public void run() {
                HoverPopupWindow.this.dismiss();
            }
        }, (long) ms);
    }

    public void dismiss() {
        if (!this.mIsSkipPenPointEffect) {
            showPenPointEffect(false);
        }
        dismissPopup();
        this.mLeftPoint = null;
        this.mRightPoint = null;
        this.mCenterPoint = null;
        this.mPenWindowStartPos = null;
    }

    private void dismissPopup() {
        if (!(!this.mIsShowMessageSent && this.mShowPopupRunnable == null && this.mDismissPopupRunnable == null)) {
            this.mParentView.removeCallbacks(this.mShowPopupRunnable);
            this.mParentView.removeCallbacks(this.mDismissPopupRunnable);
            this.mShowPopupRunnable = null;
            this.mDismissPopupRunnable = null;
            this.mIsShowMessageSent = false;
        }
        if (this.mPopup != null) {
            this.mPopup.dismiss();
            this.mPopup = null;
        }
    }

    protected void showPenPointEffect(boolean show) {
        if (this.mToolType != 2) {
            return;
        }
        if (show) {
            try {
                PointerIcon.setHoveringSpenIcon(10, -1);
            } catch (RemoteException e) {
            }
            this.mIsSPenPointChanged = true;
        } else if (!show && this.mIsSPenPointChanged) {
            try {
                PointerIcon.setHoveringSpenIcon(20, -1);
            } catch (RemoteException e2) {
            }
            this.mIsSPenPointChanged = false;
        }
    }

    private boolean pointInValidHoverArea(float localX, float localY) {
        return localX >= ((float) this.mHoverPaddingLeft) && localX < ((float) ((this.mParentView.getRight() - this.mParentView.getLeft()) - this.mHoverPaddingRight)) && localY >= ((float) this.mHoverPaddingTop) && localY < ((float) ((this.mParentView.getBottom() - this.mParentView.getTop()) - this.mHoverPaddingBottom));
    }

    protected int convertDPtoPX(float dp, DisplayMetrics displayMetrics) {
        if (displayMetrics == null) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, dp, displayMetrics) + 0.5f);
    }

    private int getStateHashCode() {
        int hashCode = this.mPopupType;
        if (this.mParentView == null) {
            return hashCode;
        }
        hashCode |= (((((this.mParentView.getWindowVisibility() << 1) | (this.mParentView.getVisibility() << 2)) | (this.mParentView.getLeft() << 4)) | (this.mParentView.getRight() << 8)) | (this.mParentView.getTop() << 12)) | (this.mParentView.getBottom() << 16);
        int[] location = new int[2];
        this.mParentView.getLocationOnScreen(location);
        return hashCode | ((location[0] << 20) | (location[1] << 24));
    }

    private void resetTimeout() {
        if (this.mDismissHandler != null) {
            if (this.mDismissHandler.hasMessages(1)) {
                this.mDismissHandler.removeMessages(1);
            }
            if (Build.PRODUCT == null || !(Build.PRODUCT.startsWith("gt5note") || Build.PRODUCT.startsWith("noble"))) {
                this.mDismissHandler.sendMessageDelayed(this.mDismissHandler.obtainMessage(1), 500);
            } else {
                this.mDismissHandler.sendMessageDelayed(this.mDismissHandler.obtainMessage(1), 2000);
            }
        }
    }
}
