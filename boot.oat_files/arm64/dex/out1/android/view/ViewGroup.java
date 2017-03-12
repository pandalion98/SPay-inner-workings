package android.view;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.app.im.InjectionManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pools.SynchronizedPool;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.FlagToString;
import android.view.ViewDebug.IntToString;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LayoutAnimationController.AnimationParameters;
import android.view.animation.Transformation;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.util.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ViewGroup extends View implements ViewParent, ViewManager {
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static final int ARRAY_INITIAL_CAPACITY = 12;
    private static final int CHILD_LEFT_INDEX = 0;
    private static final int CHILD_TOP_INDEX = 1;
    protected static final int CLIP_TO_PADDING_MASK = 34;
    private static final boolean DBG = false;
    public static boolean DEBUG_DRAW = false;
    private static final int[] DESCENDANT_FOCUSABILITY_FLAGS = new int[]{131072, 262144, 393216};
    private static final int FLAG_ADD_STATES_FROM_CHILDREN = 8192;
    private static final int FLAG_ALWAYS_DRAWN_WITH_CACHE = 16384;
    private static final int FLAG_ANIMATION_CACHE = 64;
    static final int FLAG_ANIMATION_DONE = 16;
    private static final int FLAG_CHILDREN_DRAWN_WITH_CACHE = 32768;
    static final int FLAG_CLEAR_TRANSFORMATION = 256;
    static final int FLAG_CLIP_CHILDREN = 1;
    private static final int FLAG_CLIP_TO_PADDING = 2;
    protected static final int FLAG_DISALLOW_INTERCEPT = 524288;
    static final int FLAG_INVALIDATE_REQUIRED = 4;
    static final int FLAG_IS_TRANSITION_GROUP = 16777216;
    static final int FLAG_IS_TRANSITION_GROUP_SET = 33554432;
    private static final int FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET = 8388608;
    private static final int FLAG_MASK_FOCUSABILITY = 393216;
    private static final int FLAG_NOTIFY_ANIMATION_LISTENER = 512;
    private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 65536;
    static final int FLAG_OPTIMIZE_INVALIDATE = 128;
    private static final int FLAG_PADDING_NOT_NULL = 32;
    private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 4194304;
    private static final int FLAG_RUN_ANIMATION = 8;
    private static final int FLAG_SPLIT_MOTION_EVENTS = 2097152;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_NOT_TYPED = 268435456;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_TYPED = 134217728;
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
    static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 67108864;
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
    public static final int FOCUS_AFTER_DESCENDANTS = 262144;
    public static final int FOCUS_BEFORE_DESCENDANTS = 131072;
    public static final int FOCUS_BLOCK_DESCENDANTS = 393216;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static int LAYOUT_MODE_DEFAULT = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    private static final int LAYOUT_MODE_UNDEFINED = -1;
    public static final int PERSISTENT_ALL_CACHES = 3;
    public static final int PERSISTENT_ANIMATION_CACHE = 1;
    public static final int PERSISTENT_NO_CACHE = 0;
    public static final int PERSISTENT_SCROLLING_CACHE = 2;
    private static final ActionMode SENTINEL_ACTION_MODE = new ActionMode() {
        public void setTitle(CharSequence title) {
        }

        public void setTitle(int resId) {
        }

        public void setSubtitle(CharSequence subtitle) {
        }

        public void setSubtitle(int resId) {
        }

        public void setCustomView(View view) {
        }

        public void invalidate() {
        }

        public void finish() {
        }

        public Menu getMenu() {
            return null;
        }

        public CharSequence getTitle() {
            return null;
        }

        public CharSequence getSubtitle() {
            return null;
        }

        public View getCustomView() {
            return null;
        }

        public MenuInflater getMenuInflater() {
            return null;
        }
    };
    private static final String TAG = "ViewGroup";
    private static float[] sDebugLines;
    private static Paint sDebugPaint;
    private final boolean isElasticEnabled;
    private AnimationListener mAnimationListener;
    Paint mCachePaint;
    private boolean mChildAcceptsDrag;
    @ExportedProperty(category = "layout")
    private int mChildCountWithTransientState;
    private Transformation mChildTransformation;
    private View[] mChildren;
    private int mChildrenCount;
    private DragEvent mCurrentDrag;
    private View mCurrentDragView;
    protected ArrayList<View> mDisappearingChildren;
    private HashSet<View> mDragNotifiedChildren;
    private HoverTarget mFirstHoverTarget;
    private TouchTarget mFirstTouchTarget;
    private View mFocused;
    @ExportedProperty(flagMapping = {@FlagToString(equals = 1, mask = 1, name = "CLIP_CHILDREN"), @FlagToString(equals = 2, mask = 2, name = "CLIP_TO_PADDING"), @FlagToString(equals = 32, mask = 32, name = "PADDING_NOT_NULL")}, formatToHexString = true)
    protected int mGroupFlags;
    private boolean mHoveredSelf;
    RectF mInvalidateRegion;
    Transformation mInvalidationTransformation;
    @ExportedProperty(category = "events")
    private int mLastTouchDownIndex;
    @ExportedProperty(category = "events")
    private long mLastTouchDownTime;
    @ExportedProperty(category = "events")
    private float mLastTouchDownX;
    @ExportedProperty(category = "events")
    private float mLastTouchDownY;
    private LayoutAnimationController mLayoutAnimationController;
    private boolean mLayoutCalledWhileSuppressed;
    private int mLayoutMode;
    private TransitionListener mLayoutTransitionListener;
    private PointF mLocalPoint;
    private int mNestedScrollAxes;
    protected OnHierarchyChangeListener mOnHierarchyChangeListener;
    protected int mPersistentDrawingCache;
    private ArrayList<View> mPreSortedChildren;
    boolean mSuppressLayout;
    private float[] mTempPoint;
    private List<Integer> mTransientIndices;
    private List<View> mTransientViews;
    private LayoutTransition mTransition;
    private ArrayList<View> mTransitioningViews;
    private int mTwHorizontalScrollbarRectRelativePosX;
    private int mTwVerticalScrollbarRectRelativePosY;
    private float mTwX;
    private float mTwY;
    private ArrayList<View> mVisibilityChangingChildren;

    static class ChildListForAccessibility {
        private static final int MAX_POOL_SIZE = 32;
        private static final SynchronizedPool<ChildListForAccessibility> sPool = new SynchronizedPool(32);
        private final ArrayList<View> mChildren = new ArrayList();
        private final ArrayList<ViewLocationHolder> mHolders = new ArrayList();

        ChildListForAccessibility() {
        }

        public static ChildListForAccessibility obtain(ViewGroup parent, boolean sort) {
            ChildListForAccessibility list = (ChildListForAccessibility) sPool.acquire();
            if (list == null) {
                list = new ChildListForAccessibility();
            }
            list.init(parent, sort);
            return list;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        public int getChildCount() {
            return this.mChildren.size();
        }

        public View getChildAt(int index) {
            return (View) this.mChildren.get(index);
        }

        public int getChildIndex(View child) {
            return this.mChildren.indexOf(child);
        }

        private void init(ViewGroup parent, boolean sort) {
            int i;
            ArrayList<View> children = this.mChildren;
            int childCount = parent.getChildCount();
            for (i = 0; i < childCount; i++) {
                children.add(parent.getChildAt(i));
            }
            if (sort) {
                ArrayList<ViewLocationHolder> holders = this.mHolders;
                for (i = 0; i < childCount; i++) {
                    holders.add(ViewLocationHolder.obtain(parent, (View) children.get(i)));
                }
                sort(holders);
                for (i = 0; i < childCount; i++) {
                    ViewLocationHolder holder = (ViewLocationHolder) holders.get(i);
                    children.set(i, holder.mView);
                    holder.recycle();
                }
                holders.clear();
            }
        }

        private void sort(ArrayList<ViewLocationHolder> holders) {
            try {
                ViewLocationHolder.setComparisonStrategy(1);
                Collections.sort(holders);
            } catch (IllegalArgumentException e) {
                ViewLocationHolder.setComparisonStrategy(2);
                Collections.sort(holders);
            }
        }

        private void clear() {
            this.mChildren.clear();
        }
    }

    private static final class HoverTarget {
        private static final int MAX_RECYCLED = 32;
        private static HoverTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        public View child;
        public HoverTarget next;

        private HoverTarget() {
        }

        public static HoverTarget obtain(View child) {
            HoverTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new HoverTarget();
                } else {
                    target = sRecycleBin;
                    sRecycleBin = target.next;
                    sRecycledCount--;
                    target.next = null;
                }
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    this.next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount++;
                } else {
                    this.next = null;
                }
                this.child = null;
            }
        }
    }

    public static class LayoutParams {
        @Deprecated
        public static final int FILL_PARENT = -1;
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;
        @ExportedProperty(category = "layout", mapping = {@IntToString(from = -1, to = "MATCH_PARENT"), @IntToString(from = -2, to = "WRAP_CONTENT")})
        public int height;
        public AnimationParameters layoutAnimationParameters;
        @ExportedProperty(category = "layout", mapping = {@IntToString(from = -1, to = "MATCH_PARENT"), @IntToString(from = -2, to = "WRAP_CONTENT")})
        public int width;

        public LayoutParams(Context c, AttributeSet attrs) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_Layout);
            setBaseAttributes(a, 0, 1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public LayoutParams(LayoutParams source) {
            this.width = source.width;
            this.height = source.height;
        }

        LayoutParams() {
        }

        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            this.width = a.getLayoutDimension(widthAttr, "layout_width");
            this.height = a.getLayoutDimension(heightAttr, "layout_height");
        }

        public void resolveLayoutDirection(int layoutDirection) {
        }

        public String debug(String output) {
            return output + "ViewGroup.LayoutParams={ width=" + sizeToString(this.width) + ", height=" + sizeToString(this.height) + " }";
        }

        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
        }

        protected static String sizeToString(int size) {
            if (size == -2) {
                return "wrap-content";
            }
            if (size == -1) {
                return "match-parent";
            }
            return String.valueOf(size);
        }

        void encode(ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            encodeProperties(encoder);
            encoder.endObject();
        }

        protected void encodeProperties(ViewHierarchyEncoder encoder) {
            encoder.addProperty("width", this.width);
            encoder.addProperty("height", this.height);
        }
    }

    public static class MarginLayoutParams extends LayoutParams {
        public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;
        private static final int DEFAULT_MARGIN_RESOLVED = 0;
        private static final int LAYOUT_DIRECTION_MASK = 3;
        private static final int LEFT_MARGIN_UNDEFINED_MASK = 4;
        private static final int NEED_RESOLUTION_MASK = 32;
        private static final int RIGHT_MARGIN_UNDEFINED_MASK = 8;
        private static final int RTL_COMPATIBILITY_MODE_MASK = 16;
        private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;
        @ExportedProperty(category = "layout")
        public int bottomMargin;
        @ExportedProperty(category = "layout")
        private int endMargin;
        @ExportedProperty(category = "layout")
        public int leftMargin;
        @ExportedProperty(category = "layout", flagMapping = {@FlagToString(equals = 3, mask = 3, name = "LAYOUT_DIRECTION"), @FlagToString(equals = 4, mask = 4, name = "LEFT_MARGIN_UNDEFINED_MASK"), @FlagToString(equals = 8, mask = 8, name = "RIGHT_MARGIN_UNDEFINED_MASK"), @FlagToString(equals = 16, mask = 16, name = "RTL_COMPATIBILITY_MODE_MASK"), @FlagToString(equals = 32, mask = 32, name = "NEED_RESOLUTION_MASK")}, formatToHexString = true)
        byte mMarginFlags;
        @ExportedProperty(category = "layout")
        public int rightMargin;
        @ExportedProperty(category = "layout")
        private int startMargin;
        @ExportedProperty(category = "layout")
        public int topMargin;

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            boolean isSystemApp = true;
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_MarginLayout);
            setBaseAttributes(a, 0, 1);
            int margin = a.getDimensionPixelSize(2, -1);
            if (margin >= 0) {
                this.leftMargin = margin;
                this.topMargin = margin;
                this.rightMargin = margin;
                this.bottomMargin = margin;
            } else {
                this.leftMargin = a.getDimensionPixelSize(3, Integer.MIN_VALUE);
                if (this.leftMargin == Integer.MIN_VALUE) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 4);
                    this.leftMargin = 0;
                }
                this.rightMargin = a.getDimensionPixelSize(5, Integer.MIN_VALUE);
                if (this.rightMargin == Integer.MIN_VALUE) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 8);
                    this.rightMargin = 0;
                }
                this.topMargin = a.getDimensionPixelSize(4, 0);
                this.bottomMargin = a.getDimensionPixelSize(6, 0);
                this.startMargin = a.getDimensionPixelSize(7, Integer.MIN_VALUE);
                this.endMargin = a.getDimensionPixelSize(8, Integer.MIN_VALUE);
                if (isMarginRelative()) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                }
            }
            boolean hasRtlSupport = c.getApplicationInfo().hasRtlSupport();
            if ((c.getApplicationInfo().flags & 1) == 0) {
                isSystemApp = false;
            }
            if ((c.getApplicationInfo().targetSdkVersion < 17 && !isSystemApp) || !hasRtlSupport) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 16);
            }
            this.mMarginFlags = (byte) (this.mMarginFlags | 0);
            a.recycle();
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            this.mMarginFlags = (byte) (this.mMarginFlags & -17);
        }

        public MarginLayoutParams(MarginLayoutParams source) {
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.width = source.width;
            this.height = source.height;
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public MarginLayoutParams(LayoutParams source) {
            super(source);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            this.mMarginFlags = (byte) (this.mMarginFlags & -17);
        }

        public final void copyMarginsFrom(MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public void setMargins(int left, int top, int right, int bottom) {
            this.leftMargin = left;
            this.topMargin = top;
            this.rightMargin = right;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags & -5);
            this.mMarginFlags = (byte) (this.mMarginFlags & -9);
            if (isMarginRelative()) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 32);
            } else {
                this.mMarginFlags = (byte) (this.mMarginFlags & -33);
            }
        }

        public void setMarginsRelative(int start, int top, int end, int bottom) {
            this.startMargin = start;
            this.topMargin = top;
            this.endMargin = end;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public void setMarginStart(int start) {
            this.startMargin = start;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginStart() {
            if (this.startMargin != Integer.MIN_VALUE) {
                return this.startMargin;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            switch (this.mMarginFlags & 3) {
                case 1:
                    return this.rightMargin;
                default:
                    return this.leftMargin;
            }
        }

        public void setMarginEnd(int end) {
            this.endMargin = end;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginEnd() {
            if (this.endMargin != Integer.MIN_VALUE) {
                return this.endMargin;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            switch (this.mMarginFlags & 3) {
                case 1:
                    return this.leftMargin;
                default:
                    return this.rightMargin;
            }
        }

        public boolean isMarginRelative() {
            return (this.startMargin == Integer.MIN_VALUE && this.endMargin == Integer.MIN_VALUE) ? false : true;
        }

        public void setLayoutDirection(int layoutDirection) {
            if ((layoutDirection == 0 || layoutDirection == 1) && layoutDirection != (this.mMarginFlags & 3)) {
                this.mMarginFlags = (byte) (this.mMarginFlags & -4);
                this.mMarginFlags = (byte) (this.mMarginFlags | (layoutDirection & 3));
                if (isMarginRelative()) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                } else {
                    this.mMarginFlags = (byte) (this.mMarginFlags & -33);
                }
            }
        }

        public int getLayoutDirection() {
            return this.mMarginFlags & 3;
        }

        public void resolveLayoutDirection(int layoutDirection) {
            setLayoutDirection(layoutDirection);
            if (isMarginRelative() && (this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
        }

        private void doResolveMargins() {
            int i = 0;
            if ((this.mMarginFlags & 16) != 16) {
                int i2;
                switch (this.mMarginFlags & 3) {
                    case 1:
                        if (this.endMargin > Integer.MIN_VALUE) {
                            i2 = this.endMargin;
                        } else {
                            i2 = 0;
                        }
                        this.leftMargin = i2;
                        if (this.startMargin > Integer.MIN_VALUE) {
                            i = this.startMargin;
                        }
                        this.rightMargin = i;
                        break;
                    default:
                        if (this.startMargin > Integer.MIN_VALUE) {
                            i2 = this.startMargin;
                        } else {
                            i2 = 0;
                        }
                        this.leftMargin = i2;
                        if (this.endMargin > Integer.MIN_VALUE) {
                            i = this.endMargin;
                        }
                        this.rightMargin = i;
                        break;
                }
            }
            if ((this.mMarginFlags & 4) == 4 && this.startMargin > Integer.MIN_VALUE) {
                this.leftMargin = this.startMargin;
            }
            if ((this.mMarginFlags & 8) == 8 && this.endMargin > Integer.MIN_VALUE) {
                this.rightMargin = this.endMargin;
            }
            this.mMarginFlags = (byte) (this.mMarginFlags & -33);
        }

        public boolean isLayoutRtl() {
            return (this.mMarginFlags & 3) == 1;
        }

        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
            Insets oi = View.isLayoutModeOptical(view.mParent) ? view.getOpticalInsets() : Insets.NONE;
            ViewGroup.fillDifference(canvas, oi.left + view.getLeft(), oi.top + view.getTop(), view.getRight() - oi.right, view.getBottom() - oi.bottom, this.leftMargin, this.topMargin, this.rightMargin, this.bottomMargin, paint);
        }

        protected void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("leftMargin", this.leftMargin);
            encoder.addProperty("topMargin", this.topMargin);
            encoder.addProperty("rightMargin", this.rightMargin);
            encoder.addProperty("bottomMargin", this.bottomMargin);
            encoder.addProperty("startMargin", this.startMargin);
            encoder.addProperty("endMargin", this.endMargin);
        }
    }

    public interface OnHierarchyChangeListener {
        void onChildViewAdded(View view, View view2);

        void onChildViewRemoved(View view, View view2);
    }

    private static final class TouchTarget {
        public static final int ALL_POINTER_IDS = -1;
        private static final int MAX_RECYCLED = 32;
        private static TouchTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        public View child;
        public TouchTarget next;
        public int pointerIdBits;

        private TouchTarget() {
        }

        public static TouchTarget obtain(View child, int pointerIdBits) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                    sRecycleBin = target.next;
                    sRecycledCount--;
                    target.next = null;
                }
            }
            target.child = child;
            target.pointerIdBits = pointerIdBits;
            return target;
        }

        public void recycle() {
            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    this.next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount++;
                } else {
                    this.next = null;
                }
                this.child = null;
            }
        }
    }

    static class ViewLocationHolder implements Comparable<ViewLocationHolder> {
        public static final int COMPARISON_STRATEGY_LOCATION = 2;
        public static final int COMPARISON_STRATEGY_STRIPE = 1;
        private static final int MAX_POOL_SIZE = 32;
        private static int sComparisonStrategy = 1;
        private static final SynchronizedPool<ViewLocationHolder> sPool = new SynchronizedPool(32);
        private int mLayoutDirection;
        private final Rect mLocation = new Rect();
        public View mView;

        ViewLocationHolder() {
        }

        public static ViewLocationHolder obtain(ViewGroup root, View view) {
            ViewLocationHolder holder = (ViewLocationHolder) sPool.acquire();
            if (holder == null) {
                holder = new ViewLocationHolder();
            }
            holder.init(root, view);
            return holder;
        }

        public static void setComparisonStrategy(int strategy) {
            sComparisonStrategy = strategy;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        public int compareTo(ViewLocationHolder another) {
            if (another == null) {
                return 1;
            }
            if (sComparisonStrategy == 1) {
                if (this.mLocation.bottom - another.mLocation.top <= 0) {
                    return -1;
                }
                if (this.mLocation.top - another.mLocation.bottom >= 0) {
                    return 1;
                }
            }
            if (this.mLayoutDirection == 0) {
                int leftDifference = this.mLocation.left - another.mLocation.left;
                if (leftDifference != 0) {
                    return leftDifference;
                }
            }
            int rightDifference = this.mLocation.right - another.mLocation.right;
            if (rightDifference != 0) {
                return -rightDifference;
            }
            int topDifference = this.mLocation.top - another.mLocation.top;
            if (topDifference != 0) {
                return topDifference;
            }
            int heightDiference = this.mLocation.height() - another.mLocation.height();
            if (heightDiference != 0) {
                return -heightDiference;
            }
            int widthDiference = this.mLocation.width() - another.mLocation.width();
            if (widthDiference != 0) {
                return -widthDiference;
            }
            return this.mView.getAccessibilityViewId() - another.mView.getAccessibilityViewId();
        }

        private void init(ViewGroup root, View view) {
            Rect viewLocation = this.mLocation;
            view.getDrawingRect(viewLocation);
            root.offsetDescendantRectToMyCoords(view, viewLocation);
            this.mView = view;
            this.mLayoutDirection = root.getLayoutDirection();
        }

        private void clear() {
            this.mView = null;
            this.mLocation.set(0, 0, 0, 0);
        }
    }

    protected abstract void onLayout(boolean z, int i, int i2, int i3, int i4);

    public ViewGroup(Context context) {
        this(context, null);
    }

    public ViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mLastTouchDownIndex = -1;
        this.isElasticEnabled = true;
        this.mLayoutMode = -1;
        this.mSuppressLayout = false;
        this.mLayoutCalledWhileSuppressed = false;
        this.mChildCountWithTransientState = 0;
        this.mTransientIndices = null;
        this.mTransientViews = null;
        this.mTwHorizontalScrollbarRectRelativePosX = 0;
        this.mTwVerticalScrollbarRectRelativePosY = 0;
        this.mLayoutTransitionListener = new TransitionListener() {
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (transitionType == 3) {
                    ViewGroup.this.startViewTransition(view);
                }
            }

            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (ViewGroup.this.mLayoutCalledWhileSuppressed && !transition.isChangingLayout()) {
                    ViewGroup.this.requestLayout();
                    ViewGroup.this.mLayoutCalledWhileSuppressed = false;
                }
                if (transitionType == 3 && ViewGroup.this.mTransitioningViews != null) {
                    ViewGroup.this.endViewTransition(view);
                }
            }
        };
        initViewGroup();
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean debugDraw() {
        return DEBUG_DRAW || (this.mAttachInfo != null && this.mAttachInfo.mDebugLayout);
    }

    private void initViewGroup() {
        if (!debugDraw()) {
            setFlags(128, 128);
        }
        this.mGroupFlags |= 1;
        this.mGroupFlags |= 2;
        this.mGroupFlags |= 16;
        this.mGroupFlags |= 64;
        this.mGroupFlags |= 16384;
        if (this.mContext.getApplicationInfo().targetSdkVersion >= 11) {
            this.mGroupFlags |= 2097152;
        }
        setDescendantFocusability(131072);
        this.mChildren = new View[12];
        this.mChildrenCount = 0;
        this.mPersistentDrawingCache = 2;
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewGroup, defStyleAttr, defStyleRes);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    setClipChildren(a.getBoolean(attr, true));
                    break;
                case 1:
                    setClipToPadding(a.getBoolean(attr, true));
                    break;
                case 2:
                    int id = a.getResourceId(attr, -1);
                    if (id <= 0) {
                        break;
                    }
                    setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this.mContext, id));
                    break;
                case 3:
                    setAnimationCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 4:
                    setPersistentDrawingCache(a.getInt(attr, 2));
                    break;
                case 5:
                    setAlwaysDrawnWithCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 6:
                    setAddStatesFromChildren(a.getBoolean(attr, false));
                    break;
                case 7:
                    setDescendantFocusability(DESCENDANT_FOCUSABILITY_FLAGS[a.getInt(attr, 0)]);
                    break;
                case 8:
                    setMotionEventSplittingEnabled(a.getBoolean(attr, false));
                    break;
                case 9:
                    if (!a.getBoolean(attr, false)) {
                        break;
                    }
                    setLayoutTransition(new LayoutTransition());
                    break;
                case 10:
                    setLayoutMode(a.getInt(attr, -1));
                    break;
                case 11:
                    setTransitionGroup(a.getBoolean(attr, false));
                    break;
                case 12:
                    setTouchscreenBlocksFocus(a.getBoolean(attr, false));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @ExportedProperty(category = "focus", mapping = {@IntToString(from = 131072, to = "FOCUS_BEFORE_DESCENDANTS"), @IntToString(from = 262144, to = "FOCUS_AFTER_DESCENDANTS"), @IntToString(from = 393216, to = "FOCUS_BLOCK_DESCENDANTS")})
    public int getDescendantFocusability() {
        return this.mGroupFlags & 393216;
    }

    public void setDescendantFocusability(int focusability) {
        switch (focusability) {
            case 131072:
            case 262144:
            case 393216:
                this.mGroupFlags &= -393217;
                this.mGroupFlags |= 393216 & focusability;
                return;
            default:
                throw new IllegalArgumentException("must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
        }
    }

    void handleFocusGainInternal(int direction, Rect previouslyFocusedRect) {
        if (this.mFocused != null) {
            this.mFocused.unFocus(this);
            this.mFocused = null;
        }
        super.handleFocusGainInternal(direction, previouslyFocusedRect);
    }

    public void requestChildFocus(View child, View focused) {
        if (getDescendantFocusability() != 393216) {
            super.unFocus(focused);
            if (this.mFocused != child) {
                if (this.mFocused != null) {
                    this.mFocused.unFocus(focused);
                }
                this.mFocused = child;
            }
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, focused);
            }
        }
    }

    public void focusableViewAvailable(View v) {
        if (this.mParent != null && getDescendantFocusability() != 393216) {
            if (!isFocusableInTouchMode() && shouldBlockFocusForTouchscreen()) {
                return;
            }
            if (!isFocused() || getDescendantFocusability() == 262144) {
                this.mParent.focusableViewAvailable(v);
            }
        }
    }

    public boolean showContextMenuForChild(View originalView) {
        return this.mParent != null && this.mParent.showContextMenuForChild(originalView);
    }

    public ActionMode startActionModeForChild(View originalView, Callback callback) {
        if ((this.mGroupFlags & 134217728) != 0) {
            return SENTINEL_ACTION_MODE;
        }
        try {
            this.mGroupFlags |= 268435456;
            ActionMode startActionModeForChild = startActionModeForChild(originalView, callback, 0);
            return startActionModeForChild;
        } finally {
            this.mGroupFlags &= -268435457;
        }
    }

    public ActionMode startActionModeForChild(View originalView, Callback callback, int type) {
        if ((this.mGroupFlags & 268435456) == 0 && type == 0) {
            try {
                this.mGroupFlags |= 134217728;
                ActionMode mode = startActionModeForChild(originalView, callback);
                if (mode != SENTINEL_ACTION_MODE) {
                    return mode;
                }
            } finally {
                this.mGroupFlags &= -134217729;
            }
        }
        if (this.mParent == null) {
            return null;
        }
        try {
            return this.mParent.startActionModeForChild(originalView, callback, type);
        } catch (AbstractMethodError e) {
            return this.mParent.startActionModeForChild(originalView, callback);
        }
    }

    public boolean dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        if (super.dispatchActivityResult(who, requestCode, resultCode, data)) {
            return true;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i).dispatchActivityResult(who, requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }

    public View focusSearch(View focused, int direction) {
        if (isRootNamespace()) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        }
        if (this.mParent != null) {
            return this.mParent.focusSearch(focused, direction);
        }
        return null;
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return false;
    }

    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        ViewParent parent = this.mParent;
        if (parent != null && onRequestSendAccessibilityEvent(child, event)) {
            return parent.requestSendAccessibilityEvent(this, event);
        }
        return false;
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.onRequestSendAccessibilityEvent(this, child, event);
        }
        return onRequestSendAccessibilityEventInternal(child, event);
    }

    public boolean onRequestSendAccessibilityEventInternal(View child, AccessibilityEvent event) {
        return true;
    }

    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        boolean oldHasTransientState = hasTransientState();
        if (childHasTransientState) {
            this.mChildCountWithTransientState++;
        } else {
            this.mChildCountWithTransientState--;
        }
        boolean newHasTransientState = hasTransientState();
        if (this.mParent != null && oldHasTransientState != newHasTransientState) {
            try {
                this.mParent.childHasTransientStateChanged(this, newHasTransientState);
            } catch (AbstractMethodError e) {
                Log.e(TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
            }
        }
    }

    public boolean hasTransientState() {
        return this.mChildCountWithTransientState > 0 || super.hasTransientState();
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        return this.mFocused != null && this.mFocused.dispatchUnhandledMove(focused, direction);
    }

    public void clearChildFocus(View child) {
        this.mFocused = null;
        if (this.mParent != null) {
            this.mParent.clearChildFocus(this);
        }
    }

    public void clearFocus() {
        if (this.mFocused == null) {
            super.clearFocus();
            return;
        }
        View focused = this.mFocused;
        this.mFocused = null;
        focused.clearFocus();
    }

    void unFocus(View focused) {
        if (this.mFocused == null) {
            super.unFocus(focused);
            return;
        }
        this.mFocused.unFocus(focused);
        this.mFocused = null;
    }

    public View getFocusedChild() {
        return this.mFocused;
    }

    View getDeepestFocusedChild() {
        for (View v = this; v != null; v = v instanceof ViewGroup ? ((ViewGroup) v).getFocusedChild() : null) {
            if (v.isFocused()) {
                return v;
            }
        }
        return null;
    }

    public boolean hasFocus() {
        return ((this.mPrivateFlags & 2) == 0 && this.mFocused == null) ? false : true;
    }

    public View findFocus() {
        if (isFocused()) {
            return this;
        }
        if (this.mFocused != null) {
            return this.mFocused.findFocus();
        }
        return null;
    }

    public boolean hasFocusable() {
        if ((this.mViewFlags & 12) != 0) {
            return false;
        }
        if (isFocusable()) {
            return true;
        }
        if (getDescendantFocusability() == 393216) {
            return false;
        }
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i].hasFocusable()) {
                return true;
            }
        }
        return false;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            if (shouldBlockFocusForTouchscreen()) {
                focusableMode |= 1;
            }
            int count = this.mChildrenCount;
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                if ((child.mViewFlags & 12) == 0) {
                    child.addFocusables(views, direction, focusableMode);
                }
            }
        }
        if (descendantFocusability == 262144 && focusableCount != views.size()) {
            return;
        }
        if (isFocusableInTouchMode() || !shouldBlockFocusForTouchscreen()) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
            if (hasFocus() && !getDeepestFocusedChild().isFocusableInTouchMode()) {
                View newFocus = focusSearch(2);
                if (newFocus != null) {
                    newFocus.requestFocus();
                    return;
                }
                return;
            }
            return;
        }
        this.mGroupFlags &= -67108865;
    }

    public boolean getTouchscreenBlocksFocus() {
        return (this.mGroupFlags & 67108864) != 0;
    }

    boolean shouldBlockFocusForTouchscreen() {
        return getTouchscreenBlocksFocus() && this.mContext.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
        super.findViewsWithText(outViews, text, flags);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && (child.mPrivateFlags & 8) == 0) {
                child.findViewsWithText(outViews, text, flags);
            }
        }
    }

    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        View foundView = super.findViewByAccessibilityIdTraversal(accessibilityId);
        if (foundView != null) {
            return foundView;
        }
        if (getAccessibilityNodeProvider() != null) {
            return null;
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            foundView = children[i].findViewByAccessibilityIdTraversal(accessibilityId);
            if (foundView != null) {
                return foundView;
            }
        }
        return null;
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowFocusChanged(hasFocus);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        super.addTouchables(views);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0) {
                child.addTouchables(views);
            }
        }
    }

    public void makeOptionalFitsSystemWindows() {
        super.makeOptionalFitsSystemWindows();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].makeOptionalFitsSystemWindows();
        }
    }

    public void dispatchDisplayHint(int hint) {
        super.dispatchDisplayHint(hint);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchDisplayHint(hint);
        }
    }

    protected void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        if (this.mTransition != null) {
            if (newVisibility == 0) {
                this.mTransition.showChild(this, child, oldVisibility);
            } else {
                this.mTransition.hideChild(this, child, newVisibility);
                if (this.mTransitioningViews != null && this.mTransitioningViews.contains(child)) {
                    if (this.mVisibilityChangingChildren == null) {
                        this.mVisibilityChangingChildren = new ArrayList();
                    }
                    this.mVisibilityChangingChildren.add(child);
                    addDisappearingView(child);
                }
            }
        }
        if (this.mCurrentDrag != null && newVisibility == 0) {
            notifyChildOfDrag(child);
        }
    }

    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] != null) {
                children[i].dispatchVisibilityChanged(changedView, visibility);
            }
        }
    }

    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] != null) {
                children[i].dispatchWindowVisibilityChanged(visibility);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        super.dispatchConfigurationChanged(newConfig);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] != null) {
                children[i].dispatchConfigurationChanged(newConfig);
            }
        }
    }

    public void recomputeViewAttributes(View child) {
        if (this.mAttachInfo != null && !this.mAttachInfo.mRecomputeGlobalAttributes) {
            ViewParent parent = this.mParent;
            if (parent != null) {
                parent.recomputeViewAttributes(this);
            }
        }
    }

    void dispatchCollectViewAttributes(AttachInfo attachInfo, int visibility) {
        if ((visibility & 12) == 0) {
            super.dispatchCollectViewAttributes(attachInfo, visibility);
            int count = this.mChildrenCount;
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                child.dispatchCollectViewAttributes(attachInfo, (child.mViewFlags & 12) | visibility);
            }
        }
    }

    public void bringChildToFront(View child) {
        int index = indexOfChild(child);
        if (index >= 0) {
            removeFromArray(index);
            addInArray(child, this.mChildrenCount);
            child.mParent = this;
            requestLayout();
            invalidate();
        }
    }

    private PointF getLocalPoint() {
        if (this.mLocalPoint == null) {
            this.mLocalPoint = new PointF();
        }
        return this.mLocalPoint;
    }

    public boolean dispatchDragEvent(DragEvent event) {
        boolean retval = false;
        float tx = event.mX;
        float ty = event.mY;
        ViewRootImpl root = getViewRootImpl();
        PointF localPoint = getLocalPoint();
        View child;
        View target;
        View view;
        switch (event.mAction) {
            case 1:
                this.mCurrentDragView = null;
                this.mCurrentDrag = DragEvent.obtain(event);
                if (this.mDragNotifiedChildren == null) {
                    this.mDragNotifiedChildren = new HashSet();
                } else {
                    this.mDragNotifiedChildren.clear();
                }
                this.mChildAcceptsDrag = false;
                int count = this.mChildrenCount;
                View[] children = this.mChildren;
                int i = 0;
                while (i < count) {
                    child = children[i];
                    child.mPrivateFlags2 &= -4;
                    if (child.getVisibility() == 0 && notifyChildOfDrag(children[i])) {
                        this.mChildAcceptsDrag = true;
                    }
                    i++;
                }
                if (this.mChildAcceptsDrag) {
                    retval = true;
                    break;
                }
                break;
            case 2:
                target = findFrontmostDroppableChildAt(event.mX, event.mY, localPoint);
                if (this.mCurrentDragView != target) {
                    root.setDragFocus(target);
                    int action = event.mAction;
                    if (this.mCurrentDragView != null) {
                        view = this.mCurrentDragView;
                        event.mAction = 6;
                        view.dispatchDragEvent(event);
                        view.mPrivateFlags2 &= -3;
                        view.refreshDrawableState();
                    }
                    this.mCurrentDragView = target;
                    if (target != null) {
                        event.mAction = 5;
                        target.dispatchDragEvent(event);
                        target.mPrivateFlags2 |= 2;
                        target.refreshDrawableState();
                    }
                    event.mAction = action;
                }
                if (target != null) {
                    event.mX = localPoint.x;
                    event.mY = localPoint.y;
                    retval = target.dispatchDragEvent(event);
                    event.mX = tx;
                    event.mY = ty;
                    break;
                }
                break;
            case 3:
                target = findFrontmostDroppableChildAt(event.mX, event.mY, localPoint);
                if (target != null) {
                    event.mX = localPoint.x;
                    event.mY = localPoint.y;
                    retval = target.dispatchDragEvent(event);
                    event.mX = tx;
                    event.mY = ty;
                    break;
                }
                break;
            case 4:
                if (this.mDragNotifiedChildren != null) {
                    Iterator i$ = this.mDragNotifiedChildren.iterator();
                    while (i$.hasNext()) {
                        child = (View) i$.next();
                        child.dispatchDragEvent(event);
                        child.mPrivateFlags2 &= -4;
                        child.refreshDrawableState();
                    }
                    this.mDragNotifiedChildren.clear();
                    if (this.mCurrentDrag != null) {
                        this.mCurrentDrag.recycle();
                        this.mCurrentDrag = null;
                    }
                }
                if (this.mChildAcceptsDrag) {
                    retval = true;
                    break;
                }
                break;
            case 6:
                if (this.mCurrentDragView != null) {
                    view = this.mCurrentDragView;
                    view.dispatchDragEvent(event);
                    view.mPrivateFlags2 &= -3;
                    view.refreshDrawableState();
                    this.mCurrentDragView = null;
                    break;
                }
                break;
        }
        if (retval) {
            return retval;
        }
        return super.dispatchDragEvent(event);
    }

    View findFrontmostDroppableChildAt(float x, float y, PointF outLocalPoint) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = count - 1; i >= 0; i--) {
            View child = children[i];
            if (child.canAcceptDrag() && isTransformedTouchPointInView(x, y, child, outLocalPoint)) {
                return child;
            }
        }
        return null;
    }

    boolean notifyChildOfDrag(View child) {
        boolean canAccept = false;
        if (!this.mDragNotifiedChildren.contains(child)) {
            this.mDragNotifiedChildren.add(child);
            canAccept = child.dispatchDragEvent(this.mCurrentDrag);
            if (canAccept && !child.canAcceptDrag()) {
                child.mPrivateFlags2 |= 1;
                child.refreshDrawableState();
            }
        }
        return canAccept;
    }

    public boolean resetDragableChildren(DragEvent event) {
        boolean ret = false;
        if (this.mCurrentDragView != null) {
            View view = this.mCurrentDragView;
            DragEvent ev = DragEvent.obtain(event);
            ev.mAction = 6;
            view.dispatchDragEvent(ev);
            ev.recycle();
            view.mPrivateFlags2 &= -3;
            view.refreshDrawableState();
            this.mCurrentDragView = null;
        }
        if (this.mDragNotifiedChildren == null) {
            this.mDragNotifiedChildren = new HashSet();
        } else {
            Iterator i$ = this.mDragNotifiedChildren.iterator();
            while (i$.hasNext()) {
                View v = (View) i$.next();
                if (v != null) {
                    v.mPrivateFlags2 &= -3;
                }
            }
            this.mDragNotifiedChildren.clear();
        }
        this.mChildAcceptsDrag = false;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        int i = 0;
        while (i < count) {
            View child = children[i];
            child.mPrivateFlags2 &= -4;
            if (child.getVisibility() == 0 && notifyChildOfDrag(children[i])) {
                this.mChildAcceptsDrag = true;
                ret = true;
            }
            i++;
        }
        return ret;
    }

    public boolean hasCurrentDrag() {
        return this.mCurrentDrag != null;
    }

    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        super.dispatchWindowSystemUiVisiblityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowSystemUiVisiblityChanged(visible);
        }
    }

    public void dispatchSystemUiVisibilityChanged(int visible) {
        super.dispatchSystemUiVisibilityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchSystemUiVisibilityChanged(visible);
        }
    }

    boolean updateLocalSystemUiVisibility(int localValue, int localChanges) {
        boolean changed = super.updateLocalSystemUiVisibility(localValue, localChanges);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            changed |= children[i].updateLocalSystemUiVisibility(localValue, localChanges);
        }
        return changed;
    }

    public boolean dispatchKeyEventTextMultiSelection(KeyEvent event) {
        if (sSpenUspLevel <= 0) {
            return false;
        }
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i].dispatchKeyEventTextMultiSelection(event)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyEventPreIme(event);
        }
        if (this.mFocused == null || (this.mFocused.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchKeyEventPreIme(event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onKeyEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) == 18) {
            if (super.dispatchKeyEvent(event)) {
                return true;
            }
        } else if (this.mFocused != null && (this.mFocused.mPrivateFlags & 16) == 16 && this.mFocused.dispatchKeyEvent(event)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
        }
        return false;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyShortcutEvent(event);
        }
        if (this.mFocused == null || (this.mFocused.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTrackballEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) == 18) {
            if (super.dispatchTrackballEvent(event)) {
                return true;
            }
        } else if (this.mFocused != null && (this.mFocused.mPrivateFlags & 16) == 16 && this.mFocused.dispatchTrackballEvent(event)) {
            return true;
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
        }
        return false;
    }

    protected boolean dispatchHoverEvent(MotionEvent event) {
        View child;
        int action = event.getAction();
        boolean interceptHover = onInterceptHoverEvent(event);
        event.setAction(action);
        MotionEvent eventNoHistory = event;
        boolean handled = false;
        HoverTarget firstOldHoverTarget = this.mFirstHoverTarget;
        this.mFirstHoverTarget = null;
        if (!(interceptHover || action == 10)) {
            float x = event.getX();
            float y = event.getY();
            int childrenCount = this.mChildrenCount;
            if (childrenCount != 0) {
                ArrayList<View> preorderedList = buildOrderedChildList();
                boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
                View[] children = this.mChildren;
                HoverTarget lastHoverTarget = null;
                for (int i = childrenCount - 1; i >= 0; i--) {
                    int childIndex;
                    if (customOrder) {
                        childIndex = getChildDrawingOrder(childrenCount, i);
                    } else {
                        childIndex = i;
                    }
                    child = preorderedList == null ? children[childIndex] : (View) preorderedList.get(childIndex);
                    if (canViewReceivePointerEvents(child) && isTransformedTouchPointInView(x, y, child, null)) {
                        boolean wasHovered;
                        HoverTarget hoverTarget = firstOldHoverTarget;
                        HoverTarget predecessor = null;
                        while (hoverTarget != null) {
                            if (hoverTarget.child == child) {
                                if (predecessor != null) {
                                    predecessor.next = hoverTarget.next;
                                } else {
                                    firstOldHoverTarget = hoverTarget.next;
                                }
                                hoverTarget.next = null;
                                wasHovered = true;
                                if (lastHoverTarget == null) {
                                    lastHoverTarget.next = hoverTarget;
                                } else {
                                    this.mFirstHoverTarget = hoverTarget;
                                }
                                lastHoverTarget = hoverTarget;
                                if (action != 9) {
                                    if (!wasHovered) {
                                        handled |= dispatchTransformedGenericPointerEvent(event, child);
                                    }
                                } else if (action == 7) {
                                    if (wasHovered) {
                                        eventNoHistory = obtainMotionEventNoHistoryOrSelf(eventNoHistory);
                                        eventNoHistory.setAction(9);
                                        handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child);
                                        eventNoHistory.setAction(action);
                                        handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child);
                                    } else {
                                        handled |= dispatchTransformedGenericPointerEvent(event, child);
                                    }
                                }
                                if (handled) {
                                    break;
                                }
                            } else {
                                predecessor = hoverTarget;
                                hoverTarget = hoverTarget.next;
                            }
                        }
                        hoverTarget = HoverTarget.obtain(child);
                        wasHovered = false;
                        if (lastHoverTarget == null) {
                            this.mFirstHoverTarget = hoverTarget;
                        } else {
                            lastHoverTarget.next = hoverTarget;
                        }
                        lastHoverTarget = hoverTarget;
                        if (action != 9) {
                            if (action == 7) {
                                if (wasHovered) {
                                    handled |= dispatchTransformedGenericPointerEvent(event, child);
                                } else {
                                    eventNoHistory = obtainMotionEventNoHistoryOrSelf(eventNoHistory);
                                    eventNoHistory.setAction(9);
                                    handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child);
                                    eventNoHistory.setAction(action);
                                    handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child);
                                }
                            }
                        } else if (wasHovered) {
                            handled |= dispatchTransformedGenericPointerEvent(event, child);
                        }
                        if (handled) {
                            break;
                        }
                    }
                }
                if (preorderedList != null) {
                    preorderedList.clear();
                }
            }
        }
        while (firstOldHoverTarget != null) {
            child = firstOldHoverTarget.child;
            if (action == 10) {
                handled |= dispatchTransformedGenericPointerEvent(event, child);
            } else {
                if (action == 7) {
                    dispatchTransformedGenericPointerEvent(event, child);
                }
                eventNoHistory = obtainMotionEventNoHistoryOrSelf(eventNoHistory);
                eventNoHistory.setAction(10);
                dispatchTransformedGenericPointerEvent(eventNoHistory, child);
                eventNoHistory.setAction(action);
            }
            HoverTarget nextOldHoverTarget = firstOldHoverTarget.next;
            firstOldHoverTarget.recycle();
            firstOldHoverTarget = nextOldHoverTarget;
        }
        boolean newHoveredSelf = !handled;
        if (newHoveredSelf != this.mHoveredSelf) {
            if (this.mHoveredSelf) {
                if (action == 10) {
                    handled |= super.dispatchHoverEvent(event);
                } else {
                    if (action == 7) {
                        super.dispatchHoverEvent(event);
                    }
                    eventNoHistory = obtainMotionEventNoHistoryOrSelf(eventNoHistory);
                    eventNoHistory.setAction(10);
                    super.dispatchHoverEvent(eventNoHistory);
                    eventNoHistory.setAction(action);
                }
                this.mHoveredSelf = false;
            }
            if (newHoveredSelf) {
                if (action == 9) {
                    handled |= super.dispatchHoverEvent(event);
                    this.mHoveredSelf = true;
                } else if (action == 7) {
                    eventNoHistory = obtainMotionEventNoHistoryOrSelf(eventNoHistory);
                    eventNoHistory.setAction(9);
                    handled |= super.dispatchHoverEvent(eventNoHistory);
                    eventNoHistory.setAction(action);
                    handled |= super.dispatchHoverEvent(eventNoHistory);
                    this.mHoveredSelf = true;
                }
            }
        } else if (newHoveredSelf) {
            handled |= super.dispatchHoverEvent(event);
        }
        if (!(eventNoHistory == event || eventNoHistory.mRecycled)) {
            eventNoHistory.recycle();
        }
        return handled;
    }

    private void exitHoverTargets() {
        if (this.mHoveredSelf || this.mFirstHoverTarget != null) {
            long now = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
            event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
            dispatchHoverEvent(event);
            event.recycle();
        }
    }

    private void cancelHoverTarget(View view) {
        HoverTarget predecessor = null;
        HoverTarget target = this.mFirstHoverTarget;
        while (target != null) {
            HoverTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstHoverTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
                event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
                view.dispatchHoverEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    protected boolean hasHoveredChild() {
        return this.mFirstHoverTarget != null;
    }

    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        if (getAccessibilityNodeProvider() == null) {
            ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
            try {
                int childrenCount = children.getChildCount();
                for (int i = 0; i < childrenCount; i++) {
                    View child = children.getChildAt(i);
                    if ((child.mViewFlags & 12) == 0) {
                        if (child.includeForAccessibility()) {
                            outChildren.add(child);
                        } else {
                            child.addChildrenForAccessibility(outChildren);
                        }
                    }
                }
            } finally {
                children.recycle();
            }
        }
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    private static MotionEvent obtainMotionEventNoHistoryOrSelf(MotionEvent event) {
        return event.getHistorySize() == 0 ? event : MotionEvent.obtainNoHistory(event);
    }

    protected boolean dispatchGenericPointerEvent(MotionEvent event) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            int i = childrenCount - 1;
            while (i >= 0) {
                int childIndex;
                if (customOrder) {
                    childIndex = getChildDrawingOrder(childrenCount, i);
                } else {
                    childIndex = i;
                }
                View child = preorderedList == null ? children[childIndex] : (View) preorderedList.get(childIndex);
                if (!canViewReceivePointerEvents(child) || !isTransformedTouchPointInView(x, y, child, null) || !dispatchTransformedGenericPointerEvent(event, child)) {
                    i--;
                } else if (preorderedList == null) {
                    return true;
                } else {
                    preorderedList.clear();
                    return true;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.dispatchGenericPointerEvent(event);
    }

    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchGenericFocusedEvent(event);
        }
        if (this.mFocused == null || (this.mFocused.mPrivateFlags & 16) != 16) {
            return false;
        }
        return this.mFocused.dispatchGenericMotionEvent(event);
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent event, View child) {
        float offsetX = (float) (this.mScrollX - child.mLeft);
        float offsetY = (float) (this.mScrollY - child.mTop);
        if (child.hasIdentityMatrix()) {
            event.offsetLocation(offsetX, offsetY);
            boolean handled = child.dispatchGenericMotionEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return handled;
        }
        MotionEvent transformedEvent = MotionEvent.obtain(event);
        transformedEvent.offsetLocation(offsetX, offsetY);
        transformedEvent.transform(child.getInverseMatrix());
        handled = child.dispatchGenericMotionEvent(transformedEvent);
        transformedEvent.recycle();
        return handled;
    }

    public void twSmoothScrollBy(int distance) {
    }

    public void twSetSelection(int position) {
    }

    protected int twGetItemCount() {
        return 0;
    }

    protected boolean isTwUsingAdapterView() {
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTouchEvent(ev, 1);
        }
        if (ev.isTargetAccessibilityFocus() && isAccessibilityFocusedViewOrHost()) {
            ev.setTargetAccessibilityFocus(false);
        }
        boolean handled = false;
        if (onFilterTouchEventForSecurity(ev)) {
            boolean intercepted;
            int action = ev.getAction();
            int actionMasked = action & 255;
            if (actionMasked == 0 || actionMasked == 211) {
                cancelAndClearTouchTargets(ev);
                resetTouchState();
            }
            if (actionMasked == 0 || actionMasked == 211 || this.mFirstTouchTarget != null) {
                if ((this.mGroupFlags & 524288) != 0) {
                    intercepted = false;
                } else {
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action);
                }
            } else {
                intercepted = true;
            }
            if (intercepted || this.mFirstTouchTarget != null) {
                ev.setTargetAccessibilityFocus(false);
            }
            boolean canceled = resetCancelNextUpFlag(this) || actionMasked == 3;
            boolean split = (this.mGroupFlags & 2097152) != 0;
            TouchTarget newTouchTarget = null;
            boolean alreadyDispatchedToNewTouchTarget = false;
            if (!(canceled || intercepted)) {
                View findChildWithAccessibilityFocus = ev.isTargetAccessibilityFocus() ? findChildWithAccessibilityFocus() : null;
                if (actionMasked == 0 || actionMasked == 211 || ((split && actionMasked == 5) || actionMasked == 7)) {
                    int actionIndex = ev.getActionIndex();
                    int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex) : -1;
                    removePointersFromTouchTargets(idBitsToAssign);
                    int childrenCount = this.mChildrenCount;
                    if (null == null && childrenCount != 0) {
                        float x = ev.getX(actionIndex);
                        float y = ev.getY(actionIndex);
                        ArrayList<View> preorderedList = buildOrderedChildList();
                        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
                        View[] children = this.mChildren;
                        int i = childrenCount - 1;
                        while (i >= 0) {
                            int childIndex;
                            View child;
                            if (customOrder) {
                                childIndex = getChildDrawingOrder(childrenCount, i);
                            } else {
                                childIndex = i;
                            }
                            if (preorderedList == null) {
                                child = children[childIndex];
                            } else {
                                child = (View) preorderedList.get(childIndex);
                            }
                            if (findChildWithAccessibilityFocus != null) {
                                if (findChildWithAccessibilityFocus != child) {
                                    continue;
                                    i--;
                                } else {
                                    findChildWithAccessibilityFocus = null;
                                    i = childrenCount - 1;
                                }
                            }
                            if (canViewReceivePointerEvents(child) && isTransformedTouchPointInView(x, y, child, null)) {
                                newTouchTarget = getTouchTarget(child);
                                if (newTouchTarget != null) {
                                    newTouchTarget.pointerIdBits |= idBitsToAssign;
                                    break;
                                }
                                resetCancelNextUpFlag(child);
                                if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                    this.mLastTouchDownTime = ev.getDownTime();
                                    if (preorderedList != null) {
                                        for (int j = 0; j < childrenCount; j++) {
                                            if (children[childIndex] == this.mChildren[j]) {
                                                this.mLastTouchDownIndex = j;
                                                break;
                                            }
                                        }
                                    } else {
                                        this.mLastTouchDownIndex = childIndex;
                                    }
                                    this.mLastTouchDownX = ev.getX();
                                    this.mLastTouchDownY = ev.getY();
                                    newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                    alreadyDispatchedToNewTouchTarget = true;
                                } else {
                                    ev.setTargetAccessibilityFocus(false);
                                    i--;
                                }
                            } else {
                                ev.setTargetAccessibilityFocus(false);
                                i--;
                            }
                        }
                        if (preorderedList != null) {
                            preorderedList.clear();
                        }
                    }
                    if (newTouchTarget == null && this.mFirstTouchTarget != null) {
                        newTouchTarget = this.mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }
            if (this.mFirstTouchTarget == null) {
                handled = dispatchTransformedTouchEvent(ev, canceled, null, -1);
            } else {
                TouchTarget predecessor = null;
                TouchTarget target = this.mFirstTouchTarget;
                while (target != null) {
                    TouchTarget next = target.next;
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else if (target.child != null) {
                        boolean cancelChild = resetCancelNextUpFlag(target.child) || intercepted;
                        if (dispatchTransformedTouchEvent(ev, cancelChild, target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        if (cancelChild) {
                            if (predecessor == null) {
                                this.mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                        }
                    } else {
                        Log.e(TAG, "Skip dispatching event because target.child is null.");
                    }
                    predecessor = target;
                    target = next;
                }
            }
            if (canceled || actionMasked == 1 || actionMasked == 212 || actionMasked == 7) {
                resetTouchState();
            } else if (split && actionMasked == 6) {
                removePointersFromTouchTargets(1 << ev.getPointerId(ev.getActionIndex()));
            }
        }
        if (!(handled || this.mInputEventConsistencyVerifier == null)) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
        }
        return handled;
    }

    private View findChildWithAccessibilityFocus() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null) {
            return null;
        }
        View current = viewRoot.getAccessibilityFocusedHost();
        if (current == null) {
            return null;
        }
        View parent = current.getParent();
        while (parent instanceof View) {
            if (parent == this) {
                return current;
            }
            current = parent;
            parent = current.getParent();
        }
        return null;
    }

    private void resetTouchState() {
        clearTouchTargets();
        resetCancelNextUpFlag(this);
        this.mGroupFlags &= -524289;
        this.mNestedScrollAxes = 0;
    }

    private static boolean resetCancelNextUpFlag(View view) {
        if (view == null || (view.mPrivateFlags & 67108864) == 0) {
            return false;
        }
        view.mPrivateFlags &= -67108865;
        return true;
    }

    private void clearTouchTargets() {
        TouchTarget target = this.mFirstTouchTarget;
        if (target != null) {
            do {
                TouchTarget next = target.next;
                target.recycle();
                target = next;
            } while (target != null);
            this.mFirstTouchTarget = null;
        }
    }

    private void cancelAndClearTouchTargets(MotionEvent event) {
        if (this.mFirstTouchTarget != null) {
            boolean syntheticEvent = false;
            if (event == null) {
                long now = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
                syntheticEvent = true;
            }
            for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
                resetCancelNextUpFlag(target.child);
                dispatchTransformedTouchEvent(event, true, target.child, target.pointerIdBits);
            }
            clearTouchTargets();
            if (syntheticEvent && !event.mRecycled) {
                event.recycle();
            }
        }
    }

    private TouchTarget getTouchTarget(View child) {
        for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
            if (target.child == child) {
                return target;
            }
        }
        return null;
    }

    private TouchTarget addTouchTarget(View child, int pointerIdBits) {
        TouchTarget target = TouchTarget.obtain(child, pointerIdBits);
        target.next = this.mFirstTouchTarget;
        this.mFirstTouchTarget = target;
        return target;
    }

    private void removePointersFromTouchTargets(int pointerIdBits) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if ((target.pointerIdBits & pointerIdBits) != 0) {
                target.pointerIdBits &= pointerIdBits ^ -1;
                if (target.pointerIdBits == 0) {
                    if (predecessor == null) {
                        this.mFirstTouchTarget = next;
                    } else {
                        predecessor.next = next;
                    }
                    target.recycle();
                    target = next;
                }
            }
            predecessor = target;
            target = next;
        }
    }

    private void cancelTouchTarget(View view) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstTouchTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
                view.dispatchTouchEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    private static boolean canViewReceivePointerEvents(View child) {
        if (child == null) {
            Log.e(TAG, "There is not child on canViewReceivePointerEvents");
            return false;
        } else if ((child.mViewFlags & 12) == 0 || child.getAnimation() != null) {
            return true;
        } else {
            return false;
        }
    }

    private float[] getTempPoint() {
        if (this.mTempPoint == null) {
            this.mTempPoint = new float[2];
        }
        return this.mTempPoint;
    }

    protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        float[] point = getTempPoint();
        point[0] = x;
        point[1] = y;
        transformPointToViewLocal(point, child);
        boolean isInView = child.pointInView(point[0], point[1]);
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0], point[1]);
        }
        return isInView;
    }

    public void transformPointToViewLocal(float[] point, View child) {
        point[0] = point[0] + ((float) (this.mScrollX - child.mLeft));
        point[1] = point[1] + ((float) (this.mScrollY - child.mTop));
        if (!child.hasIdentityMatrix()) {
            child.getInverseMatrix().mapPoints(point);
        }
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel, View child, int desiredPointerIdBits) {
        int oldAction = event.getAction();
        boolean handled;
        if (cancel || oldAction == 3) {
            event.setAction(3);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
                if (InjectionManager.getInstance() != null) {
                    InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
                }
            } else {
                handled = child.dispatchTouchEvent(event);
                if (InjectionManager.getInstance() != null) {
                    InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
                }
            }
            event.setAction(oldAction);
            return handled;
        }
        int oldPointerIdBits = event.getPointerIdBits();
        int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;
        if (newPointerIdBits == 0) {
            return false;
        }
        MotionEvent transformedEvent;
        if (newPointerIdBits != oldPointerIdBits) {
            transformedEvent = event.split(newPointerIdBits);
        } else if (child != null && !child.hasIdentityMatrix()) {
            transformedEvent = MotionEvent.obtain(event);
        } else if (child == null) {
            handled = super.dispatchTouchEvent(event);
            if (InjectionManager.getInstance() == null) {
                return handled;
            }
            InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
            return handled;
        } else {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            handled = child.dispatchTouchEvent(event);
            if (InjectionManager.getInstance() != null) {
                InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
            }
            event.offsetLocation(-offsetX, -offsetY);
            return handled;
        }
        if (child == null) {
            handled = super.dispatchTouchEvent(transformedEvent);
            if (InjectionManager.getInstance() != null) {
                InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
            }
        } else {
            transformedEvent.offsetLocation((float) (this.mScrollX - child.mLeft), (float) (this.mScrollY - child.mTop));
            if (!child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }
            handled = child.dispatchTouchEvent(transformedEvent);
            if (InjectionManager.getInstance() != null) {
                InjectionManager.getInstance().dispatchScaleEvent(this.mContext, 1, null, event);
            }
        }
        transformedEvent.recycle();
        return handled;
    }

    public void setMotionEventSplittingEnabled(boolean split) {
        if (split) {
            this.mGroupFlags |= 2097152;
        } else {
            this.mGroupFlags &= -2097153;
        }
    }

    public boolean isMotionEventSplittingEnabled() {
        return (this.mGroupFlags & 2097152) == 2097152;
    }

    public boolean isTransitionGroup() {
        boolean z = false;
        if ((this.mGroupFlags & 33554432) == 0) {
            ViewOutlineProvider outlineProvider = getOutlineProvider();
            if (!(getBackground() == null && getTransitionName() == null && (outlineProvider == null || outlineProvider == ViewOutlineProvider.BACKGROUND))) {
                z = true;
            }
            return z;
        } else if ((this.mGroupFlags & 16777216) != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setTransitionGroup(boolean isTransitionGroup) {
        this.mGroupFlags |= 33554432;
        if (isTransitionGroup) {
            this.mGroupFlags |= 16777216;
        } else {
            this.mGroupFlags &= -16777217;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        boolean z;
        if ((this.mGroupFlags & 524288) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (disallowIntercept != z) {
            if (disallowIntercept) {
                this.mGroupFlags |= 524288;
            } else {
                this.mGroupFlags &= -524289;
            }
            if (this.mParent != null) {
                this.mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        int descendantFocusability = getDescendantFocusability();
        boolean took;
        switch (descendantFocusability) {
            case 131072:
                took = super.requestFocus(direction, previouslyFocusedRect);
                if (took) {
                    return took;
                }
                return onRequestFocusInDescendants(direction, previouslyFocusedRect);
            case 262144:
                took = onRequestFocusInDescendants(direction, previouslyFocusedRect);
                if (took) {
                    return took;
                }
                return super.requestFocus(direction, previouslyFocusedRect);
            case 393216:
                return super.requestFocus(direction, previouslyFocusedRect);
            default:
                throw new IllegalStateException("descendant focusability must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS but is " + descendantFocusability);
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = this.mChildrenCount;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        View[] children = this.mChildren;
        for (int i = index; i != end; i += increment) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchStartTemporaryDetach();
        }
    }

    public void dispatchFinishTemporaryDetach() {
        super.dispatchFinishTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchFinishTemporaryDetach();
        }
    }

    void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        int i;
        this.mGroupFlags |= 4194304;
        super.dispatchAttachedToWindow(info, visibility);
        this.mGroupFlags &= -4194305;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            View child = children[i];
            if (child != null) {
                child.dispatchAttachedToWindow(info, combineVisibility(visibility, child.getVisibility()));
            }
        }
        int transientCount = this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
        for (i = 0; i < transientCount; i++) {
            View view = (View) this.mTransientViews.get(i);
            view.dispatchAttachedToWindow(info, combineVisibility(visibility, view.getVisibility()));
        }
    }

    void dispatchScreenStateChanged(int screenState) {
        super.dispatchScreenStateChanged(screenState);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchScreenStateChanged(screenState);
        }
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        boolean handled = false;
        if (includeForAccessibility()) {
            handled = super.dispatchPopulateAccessibilityEventInternal(event);
            if (handled) {
                return handled;
            }
        }
        ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
        try {
            int childCount = children.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = children.getChildAt(i);
                if ((child.mViewFlags & 12) == 0) {
                    handled = child.dispatchPopulateAccessibilityEvent(event);
                    if (handled) {
                        return handled;
                    }
                }
            }
            children.recycle();
            return false;
        } finally {
            children.recycle();
        }
    }

    public void dispatchProvideStructure(ViewStructure structure) {
        super.dispatchProvideStructure(structure);
        if (!isAssistBlocked() && structure.getChildCount() == 0) {
            int childrenCount = getChildCount();
            if (childrenCount > 0) {
                structure.setChildCount(childrenCount);
                ArrayList<View> preorderedList = buildOrderedChildList();
                boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
                View[] children = this.mChildren;
                for (int i = 0; i < childrenCount; i++) {
                    int childIndex;
                    if (customOrder) {
                        try {
                            childIndex = getChildDrawingOrder(childrenCount, i);
                        } catch (IndexOutOfBoundsException e) {
                            childIndex = i;
                            if (this.mContext.getApplicationInfo().targetSdkVersion < 23) {
                                Log.w(TAG, "Bad getChildDrawingOrder while collecting assist @ " + i + " of " + childrenCount, e);
                                customOrder = false;
                                if (i > 0) {
                                    int j;
                                    int[] permutation = new int[childrenCount];
                                    SparseBooleanArray usedIndices = new SparseBooleanArray();
                                    for (j = 0; j < i; j++) {
                                        permutation[j] = getChildDrawingOrder(childrenCount, j);
                                        usedIndices.put(permutation[j], true);
                                    }
                                    int nextIndex = 0;
                                    for (j = i; j < childrenCount; j++) {
                                        while (usedIndices.get(nextIndex, false)) {
                                            nextIndex++;
                                        }
                                        permutation[j] = nextIndex;
                                        nextIndex++;
                                    }
                                    preorderedList = new ArrayList(childrenCount);
                                    for (j = 0; j < childrenCount; j++) {
                                        preorderedList.add(children[permutation[j]]);
                                    }
                                }
                            } else {
                                throw e;
                            }
                        }
                    }
                    childIndex = i;
                    (preorderedList == null ? children[childIndex] : (View) preorderedList.get(childIndex)).dispatchProvideStructure(structure.newChild(i));
                }
            }
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (getAccessibilityNodeProvider() == null && this.mAttachInfo != null) {
            ArrayList<View> childrenForAccessibility = this.mAttachInfo.mTempArrayList;
            childrenForAccessibility.clear();
            addChildrenForAccessibility(childrenForAccessibility);
            int childrenForAccessibilityCount = childrenForAccessibility.size();
            int i = 0;
            while (i < childrenForAccessibilityCount) {
                try {
                    info.addChildUnchecked((View) childrenForAccessibility.get(i));
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            childrenForAccessibility.clear();
        }
    }

    public CharSequence getAccessibilityClassName() {
        return ViewGroup.class.getName();
    }

    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        if (getAccessibilityLiveRegion() != 0) {
            notifyViewAccessibilityStateChangedIfNeeded(changeType);
        } else if (this.mParent != null) {
            try {
                this.mParent.notifySubtreeAccessibilityStateChanged(this, source, changeType);
            } catch (AbstractMethodError e) {
                Log.e("View", this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
            }
        }
    }

    void resetSubtreeAccessibilityStateChanged() {
        super.resetSubtreeAccessibilityStateChanged();
        View[] children = this.mChildren;
        int childCount = this.mChildrenCount;
        for (int i = 0; i < childCount; i++) {
            if (children[i] != null) {
                children[i].resetSubtreeAccessibilityStateChanged();
            }
        }
    }

    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    void dispatchDetachedFromWindow() {
        int i;
        int transientCount = 0;
        cancelAndClearTouchTargets(null);
        exitHoverTargets();
        this.mLayoutCalledWhileSuppressed = false;
        this.mDragNotifiedChildren = null;
        if (this.mCurrentDrag != null) {
            this.mCurrentDrag.recycle();
            this.mCurrentDrag = null;
        }
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            children[i].dispatchDetachedFromWindow();
        }
        clearDisappearingChildren();
        if (this.mTransientViews != null) {
            transientCount = this.mTransientIndices.size();
        }
        for (i = 0; i < transientCount; i++) {
            ((View) this.mTransientViews.get(i)).dispatchDetachedFromWindow();
        }
        super.dispatchDetachedFromWindow();
    }

    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if ((((this.mPaddingLeft | this.mPaddingTop) | this.mPaddingRight) | this.mPaddingBottom) != 0) {
            this.mGroupFlags |= 32;
        } else {
            this.mGroupFlags &= -33;
        }
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchSaveInstanceState(container);
            }
        }
    }

    protected void dispatchFreezeSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchRestoreInstanceState(container);
            }
        }
    }

    protected void dispatchThawSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    protected void setChildrenDrawingCacheEnabled(boolean enabled) {
        if (enabled || (this.mPersistentDrawingCache & 3) != 3) {
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i = 0; i < count; i++) {
                children[i].setDrawingCacheEnabled(enabled);
            }
        }
    }

    Bitmap createSnapshot(Config quality, int backgroundColor, boolean skipChildren) {
        int i;
        int count = this.mChildrenCount;
        int[] visibilities = null;
        if (skipChildren) {
            visibilities = new int[count];
            for (i = 0; i < count; i++) {
                View child = getChildAt(i);
                visibilities[i] = child.getVisibility();
                if (visibilities[i] == 0) {
                    child.setVisibility(4);
                }
            }
        }
        Bitmap b = super.createSnapshot(quality, backgroundColor, skipChildren);
        if (skipChildren) {
            for (i = 0; i < count; i++) {
                getChildAt(i).setVisibility(visibilities[i]);
            }
        }
        return b;
    }

    boolean isLayoutModeOptical() {
        return this.mLayoutMode == 1;
    }

    Insets computeOpticalInsets() {
        if (!isLayoutModeOptical()) {
            return Insets.NONE;
        }
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                Insets insets = child.getOpticalInsets();
                left = Math.max(left, insets.left);
                top = Math.max(top, insets.top);
                right = Math.max(right, insets.right);
                bottom = Math.max(bottom, insets.bottom);
            }
        }
        return Insets.of(left, top, right, bottom);
    }

    private static void fillRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (x1 != x2 && y1 != y2) {
            int tmp;
            if (x1 > x2) {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y1 > y2) {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            canvas.drawRect((float) x1, (float) y1, (float) x2, (float) y2, paint);
        }
    }

    private static int sign(int x) {
        return x >= 0 ? 1 : -1;
    }

    private static void drawCorner(Canvas c, Paint paint, int x1, int y1, int dx, int dy, int lw) {
        fillRect(c, paint, x1, y1, x1 + dx, y1 + (sign(dy) * lw));
        fillRect(c, paint, x1, y1, x1 + (sign(dx) * lw), y1 + dy);
    }

    private int dipsToPixels(int dips) {
        return (int) ((((float) dips) * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    private static void drawRectCorners(Canvas canvas, int x1, int y1, int x2, int y2, Paint paint, int lineLength, int lineWidth) {
        drawCorner(canvas, paint, x1, y1, lineLength, lineLength, lineWidth);
        drawCorner(canvas, paint, x1, y2, lineLength, -lineLength, lineWidth);
        drawCorner(canvas, paint, x2, y1, -lineLength, lineLength, lineWidth);
        drawCorner(canvas, paint, x2, y2, -lineLength, -lineLength, lineWidth);
    }

    private static void fillDifference(Canvas canvas, int x2, int y2, int x3, int y3, int dx1, int dy1, int dx2, int dy2, Paint paint) {
        int x1 = x2 - dx1;
        int x4 = x3 + dx2;
        int y4 = y3 + dy2;
        fillRect(canvas, paint, x1, y2 - dy1, x4, y2);
        fillRect(canvas, paint, x1, y2, x2, y3);
        fillRect(canvas, paint, x3, y2, x4, y3);
        fillRect(canvas, paint, x1, y3, x4, y4);
    }

    protected void onDebugDrawMargins(Canvas canvas, Paint paint) {
        for (int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            c.getLayoutParams().onDebugDraw(c, canvas, paint);
        }
    }

    protected void onDebugDraw(Canvas canvas) {
        int i;
        Paint paint = getDebugPaint();
        paint.setColor(Menu.CATEGORY_MASK);
        paint.setStyle(Style.STROKE);
        for (i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            if (c.getVisibility() != 8) {
                Insets insets = c.getOpticalInsets();
                drawRect(canvas, paint, insets.left + c.getLeft(), insets.top + c.getTop(), (c.getRight() - insets.right) - 1, (c.getBottom() - insets.bottom) - 1);
            }
        }
        paint.setColor(Color.argb(63, 255, 0, 255));
        paint.setStyle(Style.FILL);
        onDebugDrawMargins(canvas, paint);
        paint.setColor(Color.rgb(63, 127, 255));
        paint.setStyle(Style.FILL);
        int lineLength = dipsToPixels(8);
        int lineWidth = dipsToPixels(1);
        for (i = 0; i < getChildCount(); i++) {
            c = getChildAt(i);
            if (c.getVisibility() != 8) {
                drawRectCorners(canvas, c.getLeft(), c.getTop(), c.getRight(), c.getBottom(), paint, lineLength, lineWidth);
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        int i;
        View child;
        boolean usingRenderNodeProperties = canvas.isRecordingFor(this.mRenderNode);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        int flags = this.mGroupFlags;
        if ((flags & 8) != 0 && canAnimate()) {
            if (isHardwareAccelerated()) {
            }
            for (i = 0; i < childrenCount; i++) {
                child = children[i];
                if ((child.mViewFlags & 12) == 0) {
                    attachLayoutAnimationParameters(child, child.getLayoutParams(), i, childrenCount);
                    bindLayoutAnimation(child);
                }
            }
            LayoutAnimationController controller = this.mLayoutAnimationController;
            if (controller.willOverlap()) {
                this.mGroupFlags |= 128;
            }
            controller.start();
            this.mGroupFlags &= -9;
            this.mGroupFlags &= -17;
            if (this.mAnimationListener != null) {
                this.mAnimationListener.onAnimationStart(controller.getAnimation());
            }
        }
        int clipSaveCount = 0;
        boolean clipToPadding = (flags & 34) == 34;
        if (clipToPadding) {
            clipSaveCount = canvas.save();
            canvas.clipRect(this.mScrollX + this.mPaddingLeft, this.mScrollY + this.mPaddingTop, ((this.mScrollX + this.mRight) - this.mLeft) - this.mPaddingRight, ((this.mScrollY + this.mBottom) - this.mTop) - this.mPaddingBottom);
        }
        this.mPrivateFlags &= -65;
        this.mGroupFlags &= -5;
        boolean more = false;
        long drawingTime = getDrawingTime();
        if (usingRenderNodeProperties) {
            canvas.insertReorderBarrier();
        }
        int transientCount = this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
        int transientIndex = transientCount != 0 ? 0 : -1;
        ArrayList<View> preorderedList = usingRenderNodeProperties ? null : buildOrderedChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        i = 0;
        while (i < childrenCount) {
            int childIndex;
            while (transientIndex >= 0 && ((Integer) this.mTransientIndices.get(transientIndex)).intValue() == i) {
                View transientChild = (View) this.mTransientViews.get(transientIndex);
                if ((transientChild.mViewFlags & 12) == 0 || transientChild.getAnimation() != null) {
                    more |= drawChild(canvas, transientChild, drawingTime);
                }
                transientIndex++;
                if (transientIndex >= transientCount) {
                    transientIndex = -1;
                }
            }
            if (customOrder) {
                childIndex = getChildDrawingOrder(childrenCount, i);
            } else {
                childIndex = i;
            }
            child = preorderedList == null ? children[childIndex] : (View) preorderedList.get(childIndex);
            if (child != null && ((child.mViewFlags & 12) == 0 || child.getAnimation() != null)) {
                more |= drawChild(canvas, child, drawingTime);
            }
            i++;
        }
        while (transientIndex >= 0) {
            transientChild = (View) this.mTransientViews.get(transientIndex);
            if ((transientChild.mViewFlags & 12) == 0 || transientChild.getAnimation() != null) {
                more |= drawChild(canvas, transientChild, drawingTime);
            }
            transientIndex++;
            if (transientIndex >= transientCount) {
                break;
            }
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            for (i = disappearingChildren.size() - 1; i >= 0; i--) {
                more |= drawChild(canvas, (View) disappearingChildren.get(i), drawingTime);
            }
        }
        if (usingRenderNodeProperties) {
            canvas.insertInorderBarrier();
        }
        if (debugDraw()) {
            onDebugDraw(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(clipSaveCount);
        }
        flags = this.mGroupFlags;
        if ((flags & 4) == 4) {
            invalidate(true);
        }
        if ((flags & 16) == 0 && (flags & 512) == 0 && this.mLayoutAnimationController.isDone() && !more) {
            this.mGroupFlags |= 512;
            post(new Runnable() {
                public void run() {
                    ViewGroup.this.notifyAnimationListener();
                }
            });
        }
    }

    public ViewGroupOverlay getOverlay() {
        if (this.mOverlay == null) {
            this.mOverlay = new ViewGroupOverlay(this.mContext, this);
        }
        return (ViewGroupOverlay) this.mOverlay;
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        return i;
    }

    private boolean hasChildWithZ() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            if (this.mChildren[i].getZ() != 0.0f) {
                return true;
            }
        }
        return false;
    }

    ArrayList<View> buildOrderedChildList() {
        int count = this.mChildrenCount;
        if (count <= 1 || !hasChildWithZ()) {
            return null;
        }
        if (this.mPreSortedChildren == null) {
            this.mPreSortedChildren = new ArrayList(count);
        } else {
            this.mPreSortedChildren.ensureCapacity(count);
        }
        boolean useCustomOrder = isChildrenDrawingOrderEnabled();
        for (int i = 0; i < this.mChildrenCount; i++) {
            int childIndex;
            if (useCustomOrder) {
                childIndex = getChildDrawingOrder(this.mChildrenCount, i);
            } else {
                childIndex = i;
            }
            View nextChild = this.mChildren[childIndex];
            float currentZ = nextChild.getZ();
            int insertIndex = i;
            while (insertIndex > 0 && ((View) this.mPreSortedChildren.get(insertIndex - 1)).getZ() > currentZ) {
                insertIndex--;
            }
            this.mPreSortedChildren.add(insertIndex, nextChild);
        }
        return this.mPreSortedChildren;
    }

    private void notifyAnimationListener() {
        this.mGroupFlags &= -513;
        this.mGroupFlags |= 16;
        if (this.mAnimationListener != null) {
            post(new Runnable() {
                public void run() {
                    ViewGroup.this.mAnimationListener.onAnimationEnd(ViewGroup.this.mLayoutAnimationController.getAnimation());
                }
            });
        }
        invalidate(true);
    }

    protected void dispatchGetDisplayList() {
        int i;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (i = 0; i < count; i++) {
            View child = children[i];
            if (child != null && ((child.mViewFlags & 12) == 0 || child.getAnimation() != null)) {
                recreateChildDisplayList(child);
            }
        }
        if (this.mOverlay != null) {
            recreateChildDisplayList(this.mOverlay.getOverlayView());
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            int disappearingCount = disappearingChildren.size();
            for (i = 0; i < disappearingCount; i++) {
                recreateChildDisplayList((View) disappearingChildren.get(i));
            }
        }
    }

    private void recreateChildDisplayList(View child) {
        boolean z;
        if ((child.mPrivateFlags & Integer.MIN_VALUE) != 0) {
            z = true;
        } else {
            z = false;
        }
        child.mRecreateDisplayList = z;
        child.mPrivateFlags &= Integer.MAX_VALUE;
        child.updateDisplayListIfDirty();
        child.mRecreateDisplayList = false;
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return child.draw(canvas, this, drawingTime);
    }

    void getScrollIndicatorBounds(Rect out) {
        super.getScrollIndicatorBounds(out);
        if ((this.mGroupFlags & 34) == 34) {
            out.left += this.mPaddingLeft;
            out.right -= this.mPaddingRight;
            out.top += this.mPaddingTop;
            out.bottom -= this.mPaddingBottom;
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean getClipChildren() {
        return (this.mGroupFlags & 1) != 0;
    }

    public void setClipChildren(boolean clipChildren) {
        if (clipChildren != ((this.mGroupFlags & 1) == 1)) {
            setBooleanFlag(1, clipChildren);
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = getChildAt(i);
                if (child.mRenderNode != null) {
                    child.mRenderNode.setClipToBounds(clipChildren);
                }
            }
            invalidate(true);
        }
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (hasBooleanFlag(2) != clipToPadding) {
            setBooleanFlag(2, clipToPadding);
            invalidate(true);
        }
    }

    @ExportedProperty(category = "drawing")
    public boolean getClipToPadding() {
        return hasBooleanFlag(2);
    }

    public void dispatchSetSelected(boolean selected) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setSelected(selected);
        }
    }

    public void dispatchSetActivated(boolean activated) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setActivated(activated);
        }
    }

    protected void dispatchSetPressed(boolean pressed) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (!(pressed && (child.isClickable() || child.isLongClickable()))) {
                child.setPressed(pressed);
            }
        }
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
        int count = this.mChildrenCount;
        if (count != 0) {
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                boolean nonActionable;
                View child = children[i];
                if (child.isClickable() || child.isLongClickable()) {
                    nonActionable = false;
                } else {
                    nonActionable = true;
                }
                boolean duplicatesState;
                if ((child.mViewFlags & 4194304) != 0) {
                    duplicatesState = true;
                } else {
                    duplicatesState = false;
                }
                if (nonActionable || duplicatesState) {
                    float[] point = getTempPoint();
                    point[0] = x;
                    point[1] = y;
                    transformPointToViewLocal(point, child);
                    child.drawableHotspotChanged(point[0], point[1]);
                }
            }
        }
    }

    void dispatchCancelPendingInputEvents() {
        super.dispatchCancelPendingInputEvents();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            if (children[i] != null) {
                children[i].dispatchCancelPendingInputEvents();
            }
        }
    }

    protected void setStaticTransformationsEnabled(boolean enabled) {
        setBooleanFlag(2048, enabled);
    }

    protected boolean getChildStaticTransformation(View child, Transformation t) {
        return false;
    }

    Transformation getChildTransformation() {
        if (this.mChildTransformation == null) {
            this.mChildTransformation = new Transformation();
        }
        return this.mChildTransformation;
    }

    protected View findViewTraversal(int id) {
        if (id == this.mID) {
            return this;
        }
        int i;
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (i = 0; i < len; i++) {
            View v = where[i];
            if (v != null && (v.mPrivateFlags & 8) == 0) {
                v = v.findViewById(id);
                if (v != null) {
                    return v;
                }
            }
        }
        int overlayID = -1;
        if (!((-16777216 & id) == 16777216 || this.mContext == null || this.mContext.getOverlayDirs() == null)) {
            overlayID = this.mContext.getAssets().getOverlayID(id);
            if (overlayID != -1) {
                Log.i(TAG, "Overriding ID from overlay package = " + Integer.toHexString(id));
            }
        }
        if (overlayID != -1) {
            if (overlayID == this.mID) {
                return this;
            }
            for (i = 0; i < len; i++) {
                v = where[i];
                if ((v.mPrivateFlags & 8) == 0) {
                    v = v.findViewById(overlayID);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    protected View findViewWithTagTraversal(Object tag) {
        if (tag != null && tag.equals(this.mTag)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if ((v.mPrivateFlags & 8) == 0) {
                v = v.findViewWithTag(tag);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    protected View findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        if (predicate.apply(this)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if (v != childToSkip && (v.mPrivateFlags & 8) == 0) {
                v = v.findViewByPredicate(predicate);
                if (v != null) {
                    return v;
                }
            }
        }
        return null;
    }

    public void addTransientView(View view, int index) {
        if (index >= 0) {
            if (this.mTransientIndices == null) {
                this.mTransientIndices = new ArrayList();
                this.mTransientViews = new ArrayList();
            }
            int oldSize = this.mTransientIndices.size();
            if (oldSize > 0) {
                int insertionIndex = 0;
                while (insertionIndex < oldSize && index >= ((Integer) this.mTransientIndices.get(insertionIndex)).intValue()) {
                    insertionIndex++;
                }
                this.mTransientIndices.add(insertionIndex, Integer.valueOf(index));
                this.mTransientViews.add(insertionIndex, view);
            } else {
                this.mTransientIndices.add(Integer.valueOf(index));
                this.mTransientViews.add(view);
            }
            view.mParent = this;
            view.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
            invalidate(true);
        }
    }

    public void removeTransientView(View view) {
        if (this.mTransientViews != null) {
            int size = this.mTransientViews.size();
            for (int i = 0; i < size; i++) {
                if (view == this.mTransientViews.get(i)) {
                    this.mTransientViews.remove(i);
                    this.mTransientIndices.remove(i);
                    view.mParent = null;
                    view.dispatchDetachedFromWindow();
                    invalidate(true);
                    return;
                }
            }
        }
    }

    public int getTransientViewCount() {
        return this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
    }

    public int getTransientViewIndex(int position) {
        if (position < 0 || this.mTransientIndices == null || position >= this.mTransientIndices.size()) {
            return -1;
        }
        return ((Integer) this.mTransientIndices.get(position)).intValue();
    }

    public View getTransientView(int position) {
        if (this.mTransientViews == null || position >= this.mTransientViews.size()) {
            return null;
        }
        return (View) this.mTransientViews.get(position);
    }

    public void addView(View child) {
        addView(child, -1);
    }

    public void addView(View child, int index) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
            }
        }
        addView(child, index, params);
    }

    public void addView(View child, int width, int height) {
        LayoutParams params = generateDefaultLayoutParams();
        params.width = width;
        params.height = height;
        addView(child, -1, params);
    }

    public void addView(View child, LayoutParams params) {
        addView(child, -1, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        requestLayout();
        invalidate(true);
        addViewInner(child, index, params, false);
    }

    public void updateViewLayout(View view, LayoutParams params) {
        if (!checkLayoutParams(params)) {
            throw new IllegalArgumentException("Invalid LayoutParams supplied to " + this);
        } else if (view.mParent != this) {
            throw new IllegalArgumentException("Given view not a child of " + this);
        } else {
            view.setLayoutParams(params);
        }
    }

    protected boolean checkLayoutParams(LayoutParams p) {
        return p != null;
    }

    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.mOnHierarchyChangeListener = listener;
    }

    void dispatchViewAdded(View child) {
        onViewAdded(child);
        if (this.mOnHierarchyChangeListener != null) {
            this.mOnHierarchyChangeListener.onChildViewAdded(this, child);
        }
    }

    public void onViewAdded(View child) {
    }

    void dispatchViewRemoved(View child) {
        onViewRemoved(child);
        if (this.mOnHierarchyChangeListener != null) {
            this.mOnHierarchyChangeListener.onChildViewRemoved(this, child);
        }
    }

    public void onViewRemoved(View child) {
    }

    private void clearCachedLayoutMode() {
        if (!hasBooleanFlag(8388608)) {
            this.mLayoutMode = -1;
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        clearCachedLayoutMode();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearCachedLayoutMode();
    }

    protected boolean addViewInLayout(View child, int index, LayoutParams params) {
        return addViewInLayout(child, index, params, false);
    }

    protected boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        child.mParent = null;
        addViewInner(child, index, params, preventRequestLayout);
        child.mPrivateFlags = (child.mPrivateFlags & -6291457) | 32;
        return true;
    }

    protected void cleanupLayoutState(View child) {
        child.mPrivateFlags &= -4097;
    }

    private void addViewInner(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        if (this.mTransition != null) {
            this.mTransition.cancel(3);
        }
        if (child.getParent() != null) {
            throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        if (this.mTransition != null) {
            this.mTransition.addChild(this, child);
        }
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        if (preventRequestLayout) {
            child.mLayoutParams = params;
        } else {
            child.setLayoutParams(params);
        }
        if (index < 0) {
            index = this.mChildrenCount;
        }
        addInArray(child, index);
        if (preventRequestLayout) {
            child.assignParent(this);
        } else {
            child.mParent = this;
        }
        if (child.hasFocus()) {
            requestChildFocus(child, child.findFocus());
        }
        AttachInfo ai = this.mAttachInfo;
        if (ai != null && (this.mGroupFlags & 4194304) == 0) {
            boolean lastKeepOn = ai.mKeepScreenOn;
            ai.mKeepScreenOn = false;
            child.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
            if (ai.mKeepScreenOn) {
                needGlobalAttributesUpdate(true);
            }
            ai.mKeepScreenOn = lastKeepOn;
        }
        if (child.isLayoutDirectionInherited()) {
            child.resetRtlProperties();
        }
        dispatchViewAdded(child);
        if ((child.mViewFlags & 4194304) == 4194304) {
            this.mGroupFlags |= 65536;
        }
        if (child.hasTransientState()) {
            childHasTransientStateChanged(child, true);
        }
        if (child.getVisibility() != 8) {
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        if (this.mTransientIndices != null) {
            int transientCount = this.mTransientIndices.size();
            for (int i = 0; i < transientCount; i++) {
                int oldIndex = ((Integer) this.mTransientIndices.get(i)).intValue();
                if (index <= oldIndex) {
                    this.mTransientIndices.set(i, Integer.valueOf(oldIndex + 1));
                }
            }
        }
    }

    private void addInArray(View child, int index) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        int size = children.length;
        if (index == count) {
            if (size == count) {
                this.mChildren = new View[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, size);
                children = this.mChildren;
            }
            int i = this.mChildrenCount;
            this.mChildrenCount = i + 1;
            children[i] = child;
        } else if (index < count) {
            if (size == count) {
                this.mChildren = new View[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, index);
                System.arraycopy(children, index, this.mChildren, index + 1, count - index);
                children = this.mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            this.mChildrenCount++;
            if (this.mLastTouchDownIndex >= index) {
                this.mLastTouchDownIndex++;
            }
        } else {
            throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
        }
    }

    private void removeFromArray(int index) {
        View[] children = this.mChildren;
        if ((this.mTransitioningViews == null || !this.mTransitioningViews.contains(children[index])) && children[index] != null) {
            children[index].mParent = null;
        }
        int count = this.mChildrenCount;
        int i;
        if (index == count - 1) {
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        } else if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        } else {
            System.arraycopy(children, index + 1, children, index, (count - index) - 1);
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        }
        if (this.mLastTouchDownIndex == index) {
            this.mLastTouchDownTime = 0;
            this.mLastTouchDownIndex = -1;
        } else if (this.mLastTouchDownIndex > index) {
            this.mLastTouchDownIndex--;
        }
    }

    private void removeFromArray(int start, int count) {
        View[] children = this.mChildren;
        int childrenCount = this.mChildrenCount;
        start = Math.max(0, start);
        int end = Math.min(childrenCount, start + count);
        if (start != end) {
            int i;
            if (end == childrenCount) {
                for (i = start; i < end; i++) {
                    children[i].mParent = null;
                    children[i] = null;
                }
            } else {
                for (i = start; i < end; i++) {
                    children[i].mParent = null;
                }
                System.arraycopy(children, end, children, start, childrenCount - end);
                for (i = childrenCount - (end - start); i < childrenCount; i++) {
                    children[i] = null;
                }
            }
            this.mChildrenCount -= end - start;
        }
    }

    private void bindLayoutAnimation(View child) {
        child.setAnimation(this.mLayoutAnimationController.getAnimationForView(child));
    }

    protected void attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count) {
        AnimationParameters animationParams = params.layoutAnimationParameters;
        if (animationParams == null) {
            animationParams = new AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }
        animationParams.count = count;
        animationParams.index = index;
    }

    public void removeView(View view) {
        if (removeViewInternal(view)) {
            requestLayout();
            invalidate(true);
        }
    }

    public void removeViewInLayout(View view) {
        removeViewInternal(view);
    }

    public void removeViewsInLayout(int start, int count) {
        removeViewsInternal(start, count);
    }

    public void removeViewAt(int index) {
        removeViewInternal(index, getChildAt(index));
        requestLayout();
        invalidate(true);
    }

    public void removeViews(int start, int count) {
        removeViewsInternal(start, count);
        requestLayout();
        invalidate(true);
    }

    private boolean removeViewInternal(View view) {
        int index = indexOfChild(view);
        if (index < 0) {
            return false;
        }
        removeViewInternal(index, view);
        return true;
    }

    private void removeViewInternal(int index, View view) {
        int transientCount = 0;
        if (this.mTransition != null) {
            this.mTransition.removeChild(this, view);
        }
        boolean clearChildFocus = false;
        if (view == this.mFocused) {
            view.unFocus(null);
            clearChildFocus = true;
        }
        view.clearAccessibilityFocus();
        cancelTouchTarget(view);
        cancelHoverTarget(view);
        if (view.getAnimation() != null || (this.mTransitioningViews != null && this.mTransitioningViews.contains(view))) {
            addDisappearingView(view);
        } else if (view.mAttachInfo != null) {
            view.dispatchDetachedFromWindow();
        }
        if (view.hasTransientState()) {
            childHasTransientStateChanged(view, false);
        }
        needGlobalAttributesUpdate(false);
        removeFromArray(index);
        if (clearChildFocus) {
            clearChildFocus(view);
            if (!rootViewRequestFocus()) {
                notifyGlobalFocusCleared(this);
            }
        }
        dispatchViewRemoved(view);
        if (view.getVisibility() != 8) {
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        if (this.mTransientIndices != null) {
            transientCount = this.mTransientIndices.size();
        }
        for (int i = 0; i < transientCount; i++) {
            int oldIndex = ((Integer) this.mTransientIndices.get(i)).intValue();
            if (index < oldIndex) {
                this.mTransientIndices.set(i, Integer.valueOf(oldIndex - 1));
            }
        }
    }

    public void setLayoutTransition(LayoutTransition transition) {
        if (this.mTransition != null) {
            LayoutTransition previousTransition = this.mTransition;
            previousTransition.cancel();
            previousTransition.removeTransitionListener(this.mLayoutTransitionListener);
        }
        this.mTransition = transition;
        if (this.mTransition != null) {
            this.mTransition.addTransitionListener(this.mLayoutTransitionListener);
        }
    }

    public LayoutTransition getLayoutTransition() {
        return this.mTransition;
    }

    private void removeViewsInternal(int start, int count) {
        boolean detach;
        View focused = this.mFocused;
        if (this.mAttachInfo != null) {
            detach = true;
        } else {
            detach = false;
        }
        boolean clearChildFocus = false;
        View[] children = this.mChildren;
        int end = start + count;
        for (int i = start; i < end; i++) {
            View view = children[i];
            if (this.mTransition != null) {
                this.mTransition.removeChild(this, view);
            }
            if (view == focused) {
                view.unFocus(null);
                clearChildFocus = true;
            }
            view.clearAccessibilityFocus();
            cancelTouchTarget(view);
            cancelHoverTarget(view);
            if (view.getAnimation() != null || (this.mTransitioningViews != null && this.mTransitioningViews.contains(view))) {
                addDisappearingView(view);
            } else if (detach) {
                view.dispatchDetachedFromWindow();
            }
            if (view.hasTransientState()) {
                childHasTransientStateChanged(view, false);
            }
            needGlobalAttributesUpdate(false);
            dispatchViewRemoved(view);
        }
        removeFromArray(start, count);
        if (clearChildFocus) {
            clearChildFocus(focused);
            if (!rootViewRequestFocus()) {
                notifyGlobalFocusCleared(focused);
            }
        }
    }

    public void removeAllViews() {
        removeAllViewsInLayout();
        requestLayout();
        invalidate(true);
    }

    public void removeAllViewsInLayout() {
        int count = this.mChildrenCount;
        if (count > 0) {
            boolean detach;
            View[] children = this.mChildren;
            this.mChildrenCount = 0;
            View focused = this.mFocused;
            if (this.mAttachInfo != null) {
                detach = true;
            } else {
                detach = false;
            }
            boolean clearChildFocus = false;
            needGlobalAttributesUpdate(false);
            for (int i = count - 1; i >= 0; i--) {
                View view = children[i];
                if (this.mTransition != null) {
                    this.mTransition.removeChild(this, view);
                }
                if (view == focused) {
                    view.unFocus(null);
                    clearChildFocus = true;
                }
                view.clearAccessibilityFocus();
                cancelTouchTarget(view);
                cancelHoverTarget(view);
                if (view.getAnimation() != null || (this.mTransitioningViews != null && this.mTransitioningViews.contains(view))) {
                    addDisappearingView(view);
                } else if (detach) {
                    view.dispatchDetachedFromWindow();
                }
                if (view.hasTransientState()) {
                    childHasTransientStateChanged(view, false);
                }
                dispatchViewRemoved(view);
                view.mParent = null;
                children[i] = null;
            }
            if (clearChildFocus) {
                clearChildFocus(focused);
                if (!rootViewRequestFocus()) {
                    notifyGlobalFocusCleared(focused);
                }
            }
        }
    }

    protected void removeDetachedView(View child, boolean animate) {
        if (this.mTransition != null) {
            this.mTransition.removeChild(this, child);
        }
        if (child == this.mFocused) {
            child.clearFocus();
        }
        child.clearAccessibilityFocus();
        cancelTouchTarget(child);
        cancelHoverTarget(child);
        if ((animate && child.getAnimation() != null) || (this.mTransitioningViews != null && this.mTransitioningViews.contains(child))) {
            addDisappearingView(child);
        } else if (child.mAttachInfo != null) {
            child.dispatchDetachedFromWindow();
        }
        if (child.hasTransientState()) {
            childHasTransientStateChanged(child, false);
        }
        dispatchViewRemoved(child);
    }

    protected void attachViewToParent(View child, int index, LayoutParams params) {
        child.mLayoutParams = params;
        if (index < 0) {
            index = this.mChildrenCount;
        }
        addInArray(child, index);
        child.mParent = this;
        child.mPrivateFlags = (((child.mPrivateFlags & -6291457) & -32769) | 32) | Integer.MIN_VALUE;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (child.hasFocus()) {
            requestChildFocus(child, child.findFocus());
        }
    }

    protected void detachViewFromParent(View child) {
        removeFromArray(indexOfChild(child));
    }

    protected void detachViewFromParent(int index) {
        removeFromArray(index);
    }

    protected void detachViewsFromParent(int start, int count) {
        removeFromArray(start, count);
    }

    protected void detachAllViewsFromParent() {
        int count = this.mChildrenCount;
        if (count > 0) {
            View[] children = this.mChildren;
            this.mChildrenCount = 0;
            for (int i = count - 1; i >= 0; i--) {
                children[i].mParent = null;
                children[i] = null;
            }
        }
    }

    public final void invalidateChild(View child, Rect dirty) {
        ViewParent parent = this;
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            RectF boundingRect;
            boolean drawAnimation = (child.mPrivateFlags & 64) == 64;
            Matrix childMatrix = child.getMatrix();
            boolean isOpaque = child.isOpaque() && !drawAnimation && child.getAnimation() == null && childMatrix.isIdentity();
            int opaqueFlag = isOpaque ? 4194304 : 2097152;
            if (child.mLayerType != 0) {
                this.mPrivateFlags |= Integer.MIN_VALUE;
                this.mPrivateFlags &= -32769;
            }
            int[] location = attachInfo.mInvalidateChildLocation;
            location[0] = child.mLeft;
            location[1] = child.mTop;
            if (!(childMatrix.isIdentity() && (this.mGroupFlags & 2048) == 0)) {
                Matrix transformMatrix;
                boundingRect = attachInfo.mTmpTransformRect;
                boundingRect.set(dirty);
                if ((this.mGroupFlags & 2048) != 0) {
                    Transformation t = attachInfo.mTmpTransformation;
                    if (getChildStaticTransformation(child, t)) {
                        transformMatrix = attachInfo.mTmpMatrix;
                        transformMatrix.set(t.getMatrix());
                        if (!childMatrix.isIdentity()) {
                            transformMatrix.preConcat(childMatrix);
                        }
                    } else {
                        transformMatrix = childMatrix;
                    }
                } else {
                    transformMatrix = childMatrix;
                }
                transformMatrix.mapRect(boundingRect);
                dirty.set((int) (boundingRect.left - 0.5f), (int) (boundingRect.top - 0.5f), (int) (boundingRect.right + 0.5f), (int) (boundingRect.bottom + 0.5f));
            }
            do {
                View view = null;
                if (parent instanceof View) {
                    view = (View) parent;
                }
                if (drawAnimation) {
                    if (view != null) {
                        view.mPrivateFlags |= 64;
                    } else if (parent instanceof ViewRootImpl) {
                        ((ViewRootImpl) parent).mIsAnimating = true;
                    }
                }
                if (view != null) {
                    if ((view.mViewFlags & 12288) != 0 && view.getSolidColor() == 0) {
                        opaqueFlag = 2097152;
                    }
                    if ((view.mPrivateFlags & 6291456) != 2097152) {
                        view.mPrivateFlags = (view.mPrivateFlags & -6291457) | opaqueFlag;
                    }
                }
                parent = parent.invalidateChildInParent(location, dirty);
                if (view != null) {
                    Matrix m = view.getMatrix();
                    if (m.isIdentity()) {
                        continue;
                    } else {
                        boundingRect = attachInfo.mTmpTransformRect;
                        boundingRect.set(dirty);
                        m.mapRect(boundingRect);
                        dirty.set((int) (boundingRect.left - 0.5f), (int) (boundingRect.top - 0.5f), (int) (boundingRect.right + 0.5f), (int) (boundingRect.bottom + 0.5f));
                        continue;
                    }
                }
            } while (parent != null);
        }
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if ((this.mPrivateFlags & 32) != 32 && (this.mPrivateFlags & 32768) != 32768) {
            return null;
        }
        if ((this.mGroupFlags & 144) != 128) {
            dirty.offset(location[0] - this.mScrollX, location[1] - this.mScrollY);
            if ((this.mGroupFlags & 1) == 0) {
                dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            }
            int left = this.mLeft;
            int top = this.mTop;
            if ((this.mGroupFlags & 1) == 1 && !dirty.intersect(0, 0, this.mRight - left, this.mBottom - top)) {
                dirty.setEmpty();
            }
            this.mPrivateFlags &= -32769;
            location[0] = left;
            location[1] = top;
            if (this.mLayerType != 0) {
                this.mPrivateFlags |= Integer.MIN_VALUE;
            }
            return this.mParent;
        }
        this.mPrivateFlags &= -32801;
        location[0] = this.mLeft;
        location[1] = this.mTop;
        if ((this.mGroupFlags & 1) == 1) {
            dirty.set(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
        } else {
            dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
        }
        if (this.mLayerType != 0) {
            this.mPrivateFlags |= Integer.MIN_VALUE;
        }
        return this.mParent;
    }

    public boolean damageChildDeferred(View child) {
        ViewParent parent = getParent();
        while (parent != null) {
            if (parent instanceof ViewGroup) {
                parent = parent.getParent();
            } else if (parent instanceof ViewRootImpl) {
                ((ViewRootImpl) parent).invalidate();
                return true;
            } else {
                parent = null;
            }
        }
        return false;
    }

    public void damageChild(View child, Rect dirty) {
        if (!damageChildDeferred(child)) {
            ViewParent parent = this;
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                int left = child.mLeft;
                int top = child.mTop;
                if (!child.getMatrix().isIdentity()) {
                    child.transformRect(dirty);
                }
                do {
                    if (parent instanceof ViewGroup) {
                        ViewGroup parentVG = (ViewGroup) parent;
                        if (parentVG.mLayerType != 0) {
                            parentVG.invalidate();
                            parent = null;
                            continue;
                        } else {
                            parent = parentVG.damageChildInParent(left, top, dirty);
                            left = parentVG.mLeft;
                            top = parentVG.mTop;
                            continue;
                        }
                    } else {
                        int[] location = attachInfo.mInvalidateChildLocation;
                        location[0] = left;
                        location[1] = top;
                        parent = parent.invalidateChildInParent(location, dirty);
                        continue;
                    }
                } while (parent != null);
            }
        }
    }

    protected ViewParent damageChildInParent(int left, int top, Rect dirty) {
        if (!((this.mPrivateFlags & 32) == 0 && (this.mPrivateFlags & 32768) == 0)) {
            dirty.offset(left - this.mScrollX, top - this.mScrollY);
            if ((this.mGroupFlags & 1) == 0) {
                dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            }
            if ((this.mGroupFlags & 1) == 0 || dirty.intersect(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop)) {
                if (!getMatrix().isIdentity()) {
                    transformRect(dirty);
                }
                return this.mParent;
            }
        }
        return null;
    }

    public final void offsetDescendantRectToMyCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, true, false);
    }

    public final void offsetRectIntoDescendantCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, false, false);
    }

    void offsetRectBetweenParentAndChild(View descendant, Rect rect, boolean offsetFromChildToParent, boolean clipToBounds) {
        if (descendant != this) {
            View theParent = descendant.mParent;
            while (theParent != null && (theParent instanceof View) && theParent != this) {
                View p;
                if (offsetFromChildToParent) {
                    rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
                    if (clipToBounds) {
                        p = theParent;
                        if (!rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop)) {
                            rect.setEmpty();
                        }
                    }
                } else {
                    if (clipToBounds) {
                        p = theParent;
                        if (!rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop)) {
                            rect.setEmpty();
                        }
                    }
                    rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
                }
                descendant = theParent;
                theParent = descendant.mParent;
            }
            if (theParent != this) {
                throw new IllegalArgumentException("parameter must be a descendant of this view");
            } else if (offsetFromChildToParent) {
                rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
            } else {
                rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
            }
        }
    }

    public void offsetChildrenTopAndBottom(int offset) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        boolean invalidate = false;
        for (int i = 0; i < count; i++) {
            View v = children[i];
            v.mTop += offset;
            v.mBottom += offset;
            if (v.mRenderNode != null) {
                invalidate = true;
                v.mRenderNode.offsetTopAndBottom(offset);
            }
        }
        if (invalidate) {
            invalidateViewProperty(false, false);
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    public void twOffsetChildrenLeftAndRight(int offset) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        boolean invalidate = false;
        for (int i = 0; i < count; i++) {
            View v = children[i];
            v.mLeft += offset;
            v.mRight += offset;
            if (v.mRenderNode != null) {
                invalidate = true;
                v.mRenderNode.offsetLeftAndRight(offset);
            }
        }
        if (invalidate) {
            invalidateViewProperty(false, false);
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        RectF rect;
        if (this.mAttachInfo != null) {
            rect = this.mAttachInfo.mTmpTransformRect;
        } else {
            rect = new RectF();
        }
        rect.set(r);
        if (!child.hasIdentityMatrix()) {
            child.getMatrix().mapRect(rect);
        }
        int dx = child.mLeft - this.mScrollX;
        int dy = child.mTop - this.mScrollY;
        rect.offset((float) dx, (float) dy);
        if (offset != null) {
            if (!child.hasIdentityMatrix()) {
                float[] position = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformLocation : new float[2];
                position[0] = (float) offset.x;
                position[1] = (float) offset.y;
                child.getMatrix().mapPoints(position);
                offset.x = (int) (position[0] + 0.5f);
                offset.y = (int) (position[1] + 0.5f);
            }
            offset.x += dx;
            offset.y += dy;
        }
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        boolean rectIsVisible = true;
        if (this.mParent == null || ((this.mParent instanceof ViewGroup) && ((ViewGroup) this.mParent).getClipChildren())) {
            rectIsVisible = rect.intersect(0.0f, 0.0f, (float) width, (float) height);
        }
        if (rectIsVisible && (this.mGroupFlags & 34) == 34) {
            rectIsVisible = rect.intersect((float) this.mPaddingLeft, (float) this.mPaddingTop, (float) (width - this.mPaddingRight), (float) (height - this.mPaddingBottom));
        }
        if (rectIsVisible && this.mClipBounds != null) {
            rectIsVisible = rect.intersect((float) this.mClipBounds.left, (float) this.mClipBounds.top, (float) this.mClipBounds.right, (float) this.mClipBounds.bottom);
        }
        r.set((int) (rect.left + 0.5f), (int) (rect.top + 0.5f), (int) (rect.right + 0.5f), (int) (rect.bottom + 0.5f));
        if (!rectIsVisible || this.mParent == null) {
            return rectIsVisible;
        }
        return this.mParent.getChildVisibleRect(this, r, offset);
    }

    public final void layout(int l, int t, int r, int b) {
        if (this.mSuppressLayout || (this.mTransition != null && this.mTransition.isChangingLayout())) {
            this.mLayoutCalledWhileSuppressed = true;
            return;
        }
        if (this.mTransition != null) {
            this.mTransition.layoutChange(this);
        }
        super.layout(l, t, r, b);
    }

    protected boolean canAnimate() {
        return this.mLayoutAnimationController != null;
    }

    public void startLayoutAnimation() {
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
            requestLayout();
        }
    }

    public void scheduleLayoutAnimation() {
        this.mGroupFlags |= 8;
    }

    public void setLayoutAnimation(LayoutAnimationController controller) {
        this.mLayoutAnimationController = controller;
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
        }
    }

    public LayoutAnimationController getLayoutAnimation() {
        return this.mLayoutAnimationController;
    }

    public boolean isAnimationCacheEnabled() {
        return (this.mGroupFlags & 64) == 64;
    }

    public void setAnimationCacheEnabled(boolean enabled) {
        setBooleanFlag(64, enabled);
    }

    public boolean isAlwaysDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 16384) == 16384;
    }

    public void setAlwaysDrawnWithCacheEnabled(boolean always) {
        setBooleanFlag(16384, always);
    }

    protected boolean isChildrenDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 32768) == 32768;
    }

    protected void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        setBooleanFlag(32768, enabled);
    }

    @ExportedProperty(category = "drawing")
    protected boolean isChildrenDrawingOrderEnabled() {
        return (this.mGroupFlags & 1024) == 1024;
    }

    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        setBooleanFlag(1024, enabled);
    }

    private boolean hasBooleanFlag(int flag) {
        return (this.mGroupFlags & flag) == flag;
    }

    private void setBooleanFlag(int flag, boolean value) {
        if (value) {
            this.mGroupFlags |= flag;
        } else {
            this.mGroupFlags &= flag ^ -1;
        }
    }

    @ExportedProperty(category = "drawing", mapping = {@IntToString(from = 0, to = "NONE"), @IntToString(from = 1, to = "ANIMATION"), @IntToString(from = 2, to = "SCROLLING"), @IntToString(from = 3, to = "ALL")})
    public int getPersistentDrawingCache() {
        return this.mPersistentDrawingCache;
    }

    public void setPersistentDrawingCache(int drawingCacheToKeep) {
        this.mPersistentDrawingCache = drawingCacheToKeep & 3;
    }

    private void setLayoutMode(int layoutMode, boolean explicitly) {
        this.mLayoutMode = layoutMode;
        setBooleanFlag(8388608, explicitly);
    }

    void invalidateInheritedLayoutMode(int layoutModeOfRoot) {
        if (this.mLayoutMode != -1 && this.mLayoutMode != layoutModeOfRoot && !hasBooleanFlag(8388608)) {
            setLayoutMode(-1, false);
            int N = getChildCount();
            for (int i = 0; i < N; i++) {
                getChildAt(i).invalidateInheritedLayoutMode(layoutModeOfRoot);
            }
        }
    }

    public int getLayoutMode() {
        if (this.mLayoutMode == -1) {
            setLayoutMode(this.mParent instanceof ViewGroup ? ((ViewGroup) this.mParent).getLayoutMode() : LAYOUT_MODE_DEFAULT, false);
        }
        return this.mLayoutMode;
    }

    public void setLayoutMode(int layoutMode) {
        if (this.mLayoutMode != layoutMode) {
            invalidateInheritedLayoutMode(layoutMode);
            setLayoutMode(layoutMode, layoutMode != -1);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    protected void debug(int depth) {
        super.debug(depth);
        if (this.mFocused != null) {
            Log.d("View", View.debugIndent(depth) + "mFocused");
        }
        if (this.mChildrenCount != 0) {
            Log.d("View", View.debugIndent(depth) + "{");
        }
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            this.mChildren[i].debug(depth + 1);
        }
        if (this.mChildrenCount != 0) {
            Log.d("View", View.debugIndent(depth) + "}");
        }
    }

    public int indexOfChild(View child) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public int getChildCount() {
        return this.mChildrenCount;
    }

    public View getChildAt(int index) {
        if (index < 0 || index >= this.mChildrenCount) {
            return null;
        }
        return this.mChildren[index];
    }

    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < size; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, lp.height);
        if (this.mSkipRtlCheck) {
            child.mSkipRtlCheck = this.mSkipRtlCheck;
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, (((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height);
        if (this.mSkipRtlCheck) {
            child.mSkipRtlCheck = this.mSkipRtlCheck;
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int size = Math.max(0, MeasureSpec.getSize(spec) - padding);
        int resultSize = 0;
        int resultMode = 0;
        switch (specMode) {
            case Integer.MIN_VALUE:
                if (childDimension < 0) {
                    if (childDimension != -1) {
                        if (childDimension == -2) {
                            resultSize = size;
                            resultMode = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    resultSize = size;
                    resultMode = Integer.MIN_VALUE;
                    break;
                }
                resultSize = childDimension;
                resultMode = 1073741824;
                break;
                break;
            case 0:
                if (childDimension < 0) {
                    if (childDimension != -1) {
                        if (childDimension == -2) {
                            if (View.sUseZeroUnspecifiedMeasureSpec) {
                                resultSize = 0;
                            } else {
                                resultSize = size;
                            }
                            resultMode = 0;
                            break;
                        }
                    }
                    if (View.sUseZeroUnspecifiedMeasureSpec) {
                        resultSize = 0;
                    } else {
                        resultSize = size;
                    }
                    resultMode = 0;
                    break;
                }
                resultSize = childDimension;
                resultMode = 1073741824;
                break;
                break;
            case 1073741824:
                if (childDimension < 0) {
                    if (childDimension != -1) {
                        if (childDimension == -2) {
                            resultSize = size;
                            resultMode = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    resultSize = size;
                    resultMode = 1073741824;
                    break;
                }
                resultSize = childDimension;
                resultMode = 1073741824;
                break;
                break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    public void clearDisappearingChildren() {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null) {
            int count = disappearingChildren.size();
            for (int i = 0; i < count; i++) {
                View view = (View) disappearingChildren.get(i);
                if (view.mAttachInfo != null) {
                    view.dispatchDetachedFromWindow();
                }
                view.clearAnimation();
            }
            disappearingChildren.clear();
            invalidate();
        }
    }

    private void addDisappearingView(View v) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren == null) {
            disappearingChildren = new ArrayList();
            this.mDisappearingChildren = disappearingChildren;
        }
        disappearingChildren.add(v);
    }

    void finishAnimatingView(View view, Animation animation) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null && disappearingChildren.contains(view)) {
            disappearingChildren.remove(view);
            if (view.mAttachInfo != null) {
                view.dispatchDetachedFromWindow();
            }
            view.clearAnimation();
            this.mGroupFlags |= 4;
        }
        if (!(animation == null || animation.getFillAfter())) {
            view.clearAnimation();
        }
        if ((view.mPrivateFlags & 65536) == 65536) {
            view.onAnimationEnd();
            view.mPrivateFlags &= -65537;
            this.mGroupFlags |= 4;
        }
    }

    boolean isViewTransitioning(View view) {
        return this.mTransitioningViews != null && this.mTransitioningViews.contains(view);
    }

    public void startViewTransition(View view) {
        if (view.mParent == this) {
            if (this.mTransitioningViews == null) {
                this.mTransitioningViews = new ArrayList();
            }
            this.mTransitioningViews.add(view);
        }
    }

    public void endViewTransition(View view) {
        if (this.mTransitioningViews != null) {
            this.mTransitioningViews.remove(view);
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            if (disappearingChildren != null && disappearingChildren.contains(view)) {
                disappearingChildren.remove(view);
                if (this.mVisibilityChangingChildren == null || !this.mVisibilityChangingChildren.contains(view)) {
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    if (view.mParent != null) {
                        view.mParent = null;
                    }
                } else {
                    this.mVisibilityChangingChildren.remove(view);
                }
                invalidate();
            }
        }
    }

    public void suppressLayout(boolean suppress) {
        this.mSuppressLayout = suppress;
        if (!suppress && this.mLayoutCalledWhileSuppressed) {
            requestLayout();
            this.mLayoutCalledWhileSuppressed = false;
        }
    }

    public boolean isLayoutSuppressed() {
        return this.mSuppressLayout;
    }

    public boolean gatherTransparentRegion(Region region) {
        boolean meOpaque;
        boolean z = false;
        if ((this.mPrivateFlags & 512) == 0) {
            meOpaque = true;
        } else {
            meOpaque = false;
        }
        if (meOpaque && region == null) {
            return true;
        }
        super.gatherTransparentRegion(region);
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        boolean noneOfTheChildrenAreTransparent = true;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (((child.mViewFlags & 12) == 0 || child.getAnimation() != null) && !child.gatherTransparentRegion(region)) {
                noneOfTheChildrenAreTransparent = false;
            }
        }
        if (meOpaque || noneOfTheChildrenAreTransparent) {
            z = true;
        }
        return z;
    }

    public void requestTransparentRegion(View child) {
        if (child != null) {
            child.mPrivateFlags |= 512;
            if (this.mParent != null) {
                this.mParent.requestTransparentRegion(this);
            }
        }
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        insets = super.dispatchApplyWindowInsets(insets);
        if (!insets.isConsumed()) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                insets = getChildAt(i).dispatchApplyWindowInsets(insets);
                if (insets.isConsumed()) {
                    break;
                }
            }
        }
        return insets;
    }

    public AnimationListener getLayoutAnimationListener() {
        return this.mAnimationListener;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((this.mGroupFlags & 65536) == 0) {
            return;
        }
        if ((this.mGroupFlags & 8192) != 0) {
            throw new IllegalStateException("addStateFromChildren cannot be enabled if a child has duplicateParentState set to true");
        }
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 4194304) != 0) {
                child.refreshDrawableState();
            }
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].jumpDrawablesToCurrentState();
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        if ((this.mGroupFlags & 8192) == 0) {
            return super.onCreateDrawableState(extraSpace);
        }
        int i;
        int need = 0;
        int n = getChildCount();
        for (i = 0; i < n; i++) {
            int[] childState = getChildAt(i).getDrawableState();
            if (childState != null) {
                need += childState.length;
            }
        }
        int[] state = super.onCreateDrawableState(extraSpace + need);
        for (i = 0; i < n; i++) {
            childState = getChildAt(i).getDrawableState();
            if (childState != null) {
                state = View.mergeDrawableStates(state, childState);
            }
        }
        return state;
    }

    public void setAddStatesFromChildren(boolean addsStates) {
        if (addsStates) {
            this.mGroupFlags |= 8192;
        } else {
            this.mGroupFlags &= -8193;
        }
        refreshDrawableState();
    }

    public boolean addStatesFromChildren() {
        return (this.mGroupFlags & 8192) != 0;
    }

    public void childDrawableStateChanged(View child) {
        if ((this.mGroupFlags & 8192) != 0) {
            refreshDrawableState();
        }
    }

    public void setLayoutAnimationListener(AnimationListener animationListener) {
        this.mAnimationListener = animationListener;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        ViewRootImpl viewAncestor = getViewRootImpl();
        if (viewAncestor != null) {
            viewAncestor.requestTransitionStart(transition);
        }
    }

    public boolean resolveRtlPropertiesIfNeeded() {
        boolean result = super.resolveRtlPropertiesIfNeeded();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveRtlPropertiesIfNeeded();
                }
            }
        }
        return result;
    }

    public boolean resolveLayoutDirection() {
        boolean result = super.resolveLayoutDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveLayoutDirection();
                }
            }
        }
        return result;
    }

    public boolean resolveTextDirection() {
        boolean result = super.resolveTextDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextDirectionInherited()) {
                    child.resolveTextDirection();
                }
            }
        }
        return result;
    }

    public boolean resolveTextAlignment() {
        boolean result = super.resolveTextAlignment();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextAlignmentInherited()) {
                    child.resolveTextAlignment();
                }
            }
        }
        return result;
    }

    public void resolvePadding() {
        super.resolvePadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.isPaddingResolved()) {
                child.resolvePadding();
            }
        }
    }

    protected void resolveDrawables() {
        super.resolveDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.areDrawablesResolved()) {
                child.resolveDrawables();
            }
        }
    }

    public void resolveLayoutParams() {
        super.resolveLayoutParams();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).resolveLayoutParams();
        }
    }

    public void resetResolvedLayoutDirection() {
        super.resetResolvedLayoutDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedLayoutDirection();
            }
        }
    }

    public void resetResolvedTextDirection() {
        super.resetResolvedTextDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextDirectionInherited()) {
                child.resetResolvedTextDirection();
            }
        }
    }

    public void resetResolvedTextAlignment() {
        super.resetResolvedTextAlignment();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextAlignmentInherited()) {
                child.resetResolvedTextAlignment();
            }
        }
    }

    public void resetResolvedPadding() {
        super.resetResolvedPadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedPadding();
            }
        }
    }

    protected void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedDrawables();
            }
        }
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mNestedScrollAxes = axes;
    }

    public void onStopNestedScroll(View child) {
        stopNestedScroll();
        this.mNestedScrollAxes = 0;
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }

    protected void onSetLayoutParams(View child, LayoutParams layoutParams) {
    }

    public void captureTransitioningViews(List<View> transitioningViews) {
        if (getVisibility() == 0) {
            if (isTransitionGroup()) {
                transitioningViews.add(this);
                return;
            }
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).captureTransitioningViews(transitioningViews);
            }
        }
    }

    public void findNamedViews(Map<String, View> namedElements) {
        if (getVisibility() == 0 || this.mGhostView != null) {
            super.findNamedViews(namedElements);
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).findNamedViews(namedElements);
            }
        }
    }

    private static Paint getDebugPaint() {
        if (sDebugPaint == null) {
            sDebugPaint = new Paint();
            sDebugPaint.setAntiAlias(false);
        }
        return sDebugPaint;
    }

    private static void drawRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (sDebugLines == null) {
            sDebugLines = new float[16];
        }
        sDebugLines[0] = (float) x1;
        sDebugLines[1] = (float) y1;
        sDebugLines[2] = (float) x2;
        sDebugLines[3] = (float) y1;
        sDebugLines[4] = (float) x2;
        sDebugLines[5] = (float) y1;
        sDebugLines[6] = (float) x2;
        sDebugLines[7] = (float) y2;
        sDebugLines[8] = (float) x2;
        sDebugLines[9] = (float) y2;
        sDebugLines[10] = (float) x1;
        sDebugLines[11] = (float) y2;
        sDebugLines[12] = (float) x1;
        sDebugLines[13] = (float) y2;
        sDebugLines[14] = (float) x1;
        sDebugLines[15] = (float) y1;
        canvas.drawLines(sDebugLines, paint);
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("focus:descendantFocusability", getDescendantFocusability());
        encoder.addProperty("drawing:clipChildren", getClipChildren());
        encoder.addProperty("drawing:clipToPadding", getClipToPadding());
        encoder.addProperty("drawing:childrenDrawingOrderEnabled", isChildrenDrawingOrderEnabled());
        encoder.addProperty("drawing:persistentDrawingCache", getPersistentDrawingCache());
        int n = getChildCount();
        encoder.addProperty("meta:__childCount__", (short) n);
        for (int i = 0; i < n; i++) {
            encoder.addPropertyKey("meta:__child__" + i);
            getChildAt(i).encode(encoder);
        }
    }

    public void requestOnStylusButtonEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestOnStylusButtonEvent(event);
        }
    }

    @RemotableViewMethod
    public void setFingerHoveredInAppWidget(boolean fingerHovered) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            setFingerHoveredInAppWidgetWithChild(getChildAt(i), fingerHovered);
        }
    }

    protected void setFingerHoveredInAppWidgetWithChild(View view, boolean fingerHovered) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setFingerHoveredInAppWidgetWithChild(viewGroup.getChildAt(i), fingerHovered);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setFingerHoveredInAppWidget(fingerHovered);
        }
    }

    protected boolean dispatchSipResizeAnimationState(boolean isStart) {
        int count = getChildCount();
        boolean isConsumed = false;
        for (int i = 0; i < count; i++) {
            if (getChildAt(i) != null) {
                isConsumed = getChildAt(i).dispatchSipResizeAnimationState(isStart);
            }
            if (isConsumed) {
                break;
            }
        }
        return false;
    }
}
