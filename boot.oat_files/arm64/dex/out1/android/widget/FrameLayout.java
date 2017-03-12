package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.util.ArrayList;

@RemoteView
public class FrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    @ExportedProperty(category = "padding")
    private int mForegroundPaddingBottom;
    @ExportedProperty(category = "padding")
    private int mForegroundPaddingLeft;
    @ExportedProperty(category = "padding")
    private int mForegroundPaddingRight;
    @ExportedProperty(category = "padding")
    private int mForegroundPaddingTop;
    private final ArrayList<View> mMatchParentChildren;
    @ExportedProperty(category = "measurement")
    boolean mMeasureAllChildren;
    private final Rect mOverlayBounds;
    private final Rect mSelfBounds;

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
            this.gravity = a.getInt(0, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            this.gravity = source.gravity;
        }
    }

    public FrameLayout(Context context) {
        super(context);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mMatchParentChildren = new ArrayList(1);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mMatchParentChildren = new ArrayList(1);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FrameLayout, defStyleAttr, defStyleRes);
        if (a.getBoolean(0, false)) {
            setMeasureAllChildren(true);
        }
        a.recycle();
    }

    @RemotableViewMethod
    public void setForegroundGravity(int foregroundGravity) {
        if (getForegroundGravity() != foregroundGravity) {
            super.setForegroundGravity(foregroundGravity);
            Drawable foreground = getForeground();
            if (getForegroundGravity() != 119 || foreground == null) {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            } else {
                Rect padding = new Rect();
                if (foreground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            }
            requestLayout();
        }
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    int getPaddingLeftWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingLeft, this.mForegroundPaddingLeft) : this.mPaddingLeft + this.mForegroundPaddingLeft;
    }

    int getPaddingRightWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingRight, this.mForegroundPaddingRight) : this.mPaddingRight + this.mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingTop, this.mForegroundPaddingTop) : this.mPaddingTop + this.mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingBottom, this.mForegroundPaddingBottom) : this.mPaddingBottom + this.mForegroundPaddingBottom;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int count = getChildCount();
        boolean measureMatchParentChildren = (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                android.view.ViewGroup.LayoutParams vlp = child.getLayoutParams();
                if (!(vlp instanceof LayoutParams)) {
                    Log.e(FrameLayout.class.getName(), "LayoutParam is not mine.. this=" + this + "Child=" + child + " LayoutParam=" + vlp);
                    for (int n = 0; n < count; n++) {
                        View v = getChildAt(n);
                        if (v != null) {
                            Log.e(FrameLayout.class.getName(), "Child(" + n + ")=" + v + " LayoutParam=" + v.getLayoutParams());
                        }
                    }
                }
                LayoutParams lp = (LayoutParams) vlp;
                maxWidth = Math.max(maxWidth, (child.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
                maxHeight = Math.max(maxHeight, (child.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
                childState = View.combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1)) {
                    this.mMatchParentChildren.add(child);
                }
            }
        }
        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        maxHeight = Math.max(maxHeight + (getPaddingTopWithForeground() + getPaddingBottomWithForeground()), getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState), View.resolveSizeAndState(maxHeight, heightMeasureSpec, childState << 16));
        count = this.mMatchParentChildren.size();
        if (count > 1) {
            for (i = 0; i < count; i++) {
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                child = (View) this.mMatchParentChildren.get(i);
                MarginLayoutParams lp2 = (MarginLayoutParams) child.getLayoutParams();
                if (lp2.width == -1) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - lp2.leftMargin) - lp2.rightMargin), 1073741824);
                } else {
                    childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, ((getPaddingLeftWithForeground() + getPaddingRightWithForeground()) + lp2.leftMargin) + lp2.rightMargin, lp2.width);
                }
                if (lp2.height == -1) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - lp2.topMargin) - lp2.bottomMargin), 1073741824);
                } else {
                    childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, ((getPaddingTopWithForeground() + getPaddingBottomWithForeground()) + lp2.topMargin) + lp2.bottomMargin, lp2.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void layoutChildren(int r21, int r22, int r23, int r24, boolean r25) {
        /*
        r20 = this;
        r6 = r20.getChildCount();
        r13 = r20.getPaddingLeftWithForeground();
        r18 = r23 - r21;
        r19 = r20.getPaddingRightWithForeground();
        r14 = r18 - r19;
        r15 = r20.getPaddingTopWithForeground();
        r18 = r24 - r22;
        r19 = r20.getPaddingBottomWithForeground();
        r12 = r18 - r19;
        r9 = 0;
    L_0x001d:
        if (r9 >= r6) goto L_0x00bb;
    L_0x001f:
        r0 = r20;
        r3 = r0.getChildAt(r9);
        r18 = r3.getVisibility();
        r19 = 8;
        r0 = r18;
        r1 = r19;
        if (r0 == r1) goto L_0x0073;
    L_0x0031:
        r11 = r3.getLayoutParams();
        r11 = (android.widget.FrameLayout.LayoutParams) r11;
        r17 = r3.getMeasuredWidth();
        r8 = r3.getMeasuredHeight();
        r7 = r11.gravity;
        r18 = -1;
        r0 = r18;
        if (r7 != r0) goto L_0x004a;
    L_0x0047:
        r7 = 8388659; // 0x800033 float:1.1755015E-38 double:4.144548E-317;
    L_0x004a:
        r10 = r20.getLayoutDirection();
        r2 = android.view.Gravity.getAbsoluteGravity(r7, r10);
        r16 = r7 & 112;
        r18 = r2 & 7;
        switch(r18) {
            case 1: goto L_0x0076;
            case 5: goto L_0x008b;
            default: goto L_0x0059;
        };
    L_0x0059:
        r0 = r11.leftMargin;
        r18 = r0;
        r4 = r13 + r18;
    L_0x005f:
        switch(r16) {
            case 16: goto L_0x009d;
            case 48: goto L_0x0096;
            case 80: goto L_0x00b2;
            default: goto L_0x0062;
        };
    L_0x0062:
        r0 = r11.topMargin;
        r18 = r0;
        r5 = r15 + r18;
    L_0x0068:
        r18 = r4 + r17;
        r19 = r5 + r8;
        r0 = r18;
        r1 = r19;
        r3.layout(r4, r5, r0, r1);
    L_0x0073:
        r9 = r9 + 1;
        goto L_0x001d;
    L_0x0076:
        r18 = r14 - r13;
        r18 = r18 - r17;
        r18 = r18 / 2;
        r18 = r18 + r13;
        r0 = r11.leftMargin;
        r19 = r0;
        r18 = r18 + r19;
        r0 = r11.rightMargin;
        r19 = r0;
        r4 = r18 - r19;
        goto L_0x005f;
    L_0x008b:
        if (r25 != 0) goto L_0x0059;
    L_0x008d:
        r18 = r14 - r17;
        r0 = r11.rightMargin;
        r19 = r0;
        r4 = r18 - r19;
        goto L_0x005f;
    L_0x0096:
        r0 = r11.topMargin;
        r18 = r0;
        r5 = r15 + r18;
        goto L_0x0068;
    L_0x009d:
        r18 = r12 - r15;
        r18 = r18 - r8;
        r18 = r18 / 2;
        r18 = r18 + r15;
        r0 = r11.topMargin;
        r19 = r0;
        r18 = r18 + r19;
        r0 = r11.bottomMargin;
        r19 = r0;
        r5 = r18 - r19;
        goto L_0x0068;
    L_0x00b2:
        r18 = r12 - r8;
        r0 = r11.bottomMargin;
        r19 = r0;
        r5 = r18 - r19;
        goto L_0x0068;
    L_0x00bb:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.FrameLayout.layoutChildren(int, int, int, int, boolean):void");
    }

    @RemotableViewMethod
    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    @Deprecated
    public boolean getConsiderGoneChildrenWhenMeasuring() {
        return getMeasureAllChildren();
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public CharSequence getAccessibilityClassName() {
        return FrameLayout.class.getName();
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("measurement:measureAllChildren", this.mMeasureAllChildren);
        encoder.addProperty("padding:foregroundPaddingLeft", this.mForegroundPaddingLeft);
        encoder.addProperty("padding:foregroundPaddingTop", this.mForegroundPaddingTop);
        encoder.addProperty("padding:foregroundPaddingRight", this.mForegroundPaddingRight);
        encoder.addProperty("padding:foregroundPaddingBottom", this.mForegroundPaddingBottom);
    }
}
