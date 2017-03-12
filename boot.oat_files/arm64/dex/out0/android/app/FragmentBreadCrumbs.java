package android.app;

import android.R;
import android.animation.LayoutTransition;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

@Deprecated
public class FragmentBreadCrumbs extends ViewGroup implements OnBackStackChangedListener {
    private static final int DEFAULT_GRAVITY = 8388627;
    Activity mActivity;
    private int mColorPrimary;
    LinearLayout mContainer;
    private int mGravity;
    LayoutInflater mInflater;
    private boolean mIsDeviceDefault;
    private boolean mIsDeviceDefaultDark;
    private int mLayoutResId;
    int mMaxVisible;
    private OnBreadCrumbClickListener mOnBreadCrumbClickListener;
    private OnClickListener mOnClickListener;
    private OnClickListener mParentClickListener;
    BackStackRecord mParentEntry;
    private int mTextColor;
    BackStackRecord mTopEntry;

    public interface OnBreadCrumbClickListener {
        boolean onBreadCrumbClick(BackStackEntry backStackEntry, int i);
    }

    public FragmentBreadCrumbs(Context context) {
        this(context, null);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs) {
        this(context, attrs, 18219037);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMaxVisible = -1;
        this.mIsDeviceDefault = false;
        this.mIsDeviceDefaultDark = true;
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (v.getTag() instanceof BackStackEntry) {
                    BackStackEntry bse = (BackStackEntry) v.getTag();
                    if (bse != FragmentBreadCrumbs.this.mParentEntry) {
                        if (FragmentBreadCrumbs.this.mOnBreadCrumbClickListener != null) {
                            if (FragmentBreadCrumbs.this.mOnBreadCrumbClickListener.onBreadCrumbClick(bse == FragmentBreadCrumbs.this.mTopEntry ? null : bse, 0)) {
                                return;
                            }
                        }
                        if (bse == FragmentBreadCrumbs.this.mTopEntry) {
                            FragmentBreadCrumbs.this.mActivity.getFragmentManager().popBackStack();
                        } else {
                            FragmentBreadCrumbs.this.mActivity.getFragmentManager().popBackStack(bse.getId(), 0);
                        }
                    } else if (FragmentBreadCrumbs.this.mParentClickListener != null) {
                        FragmentBreadCrumbs.this.mParentClickListener.onClick(v);
                    }
                }
            }
        };
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(18219197, outValue, true);
        if (outValue.data != 0) {
            this.mIsDeviceDefault = true;
        }
        TypedValue outValueDarkTheme = new TypedValue();
        getContext().getTheme().resolveAttribute(18219199, outValueDarkTheme, true);
        if (outValueDarkTheme.data == 0) {
            this.mIsDeviceDefaultDark = false;
        }
        context.getTheme().resolveAttribute(R.attr.colorPrimary, outValue, true);
        if (outValue.resourceId > 0) {
            this.mColorPrimary = context.getResources().getColor(outValue.resourceId);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.FragmentBreadCrumbs, defStyleAttr, defStyleRes);
        this.mGravity = a.getInt(0, DEFAULT_GRAVITY);
        this.mLayoutResId = a.getResourceId(1, 17367139);
        if (!this.mIsDeviceDefault) {
            this.mTextColor = a.getColor(2, 0);
        }
        a.recycle();
    }

    public void setActivity(Activity a) {
        this.mActivity = a;
        this.mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContainer = (LinearLayout) this.mInflater.inflate(17367141, this, false);
        addView(this.mContainer);
        a.getFragmentManager().addOnBackStackChangedListener(this);
        updateCrumbs();
        setLayoutTransition(new LayoutTransition());
    }

    public void setMaxVisible(int visibleCrumbs) {
        if (visibleCrumbs < 1) {
            throw new IllegalArgumentException("visibleCrumbs must be greater than zero");
        }
        this.mMaxVisible = visibleCrumbs;
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, OnClickListener listener) {
        this.mParentEntry = createBackStackEntry(title, shortTitle);
        this.mParentClickListener = listener;
        updateCrumbs();
    }

    public void setOnBreadCrumbClickListener(OnBreadCrumbClickListener listener) {
        this.mOnBreadCrumbClickListener = listener;
    }

    private BackStackRecord createBackStackEntry(CharSequence title, CharSequence shortTitle) {
        if (title == null) {
            return null;
        }
        BackStackRecord entry = new BackStackRecord((FragmentManagerImpl) this.mActivity.getFragmentManager());
        entry.setBreadCrumbTitle(title);
        entry.setBreadCrumbShortTitle(shortTitle);
        return entry;
    }

    public void setTitle(CharSequence title, CharSequence shortTitle) {
        this.mTopEntry = createBackStackEntry(title, shortTitle);
        updateCrumbs();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() != 0) {
            int childLeft;
            int childRight;
            View child = getChildAt(0);
            int childTop = this.mPaddingTop;
            int childBottom = (this.mPaddingTop + child.getMeasuredHeight()) - this.mPaddingBottom;
            switch (Gravity.getAbsoluteGravity(this.mGravity & 8388615, getLayoutDirection())) {
                case 1:
                    childLeft = this.mPaddingLeft + (((this.mRight - this.mLeft) - child.getMeasuredWidth()) / 2);
                    childRight = childLeft + child.getMeasuredWidth();
                    break;
                case 5:
                    childRight = (this.mRight - this.mLeft) - this.mPaddingRight;
                    childLeft = childRight - child.getMeasuredWidth();
                    break;
                default:
                    childLeft = this.mPaddingLeft;
                    childRight = childLeft + child.getMeasuredWidth();
                    break;
            }
            if (childLeft < this.mPaddingLeft) {
                childLeft = this.mPaddingLeft;
            }
            if (childRight > (this.mRight - this.mLeft) - this.mPaddingRight) {
                childRight = (this.mRight - this.mLeft) - this.mPaddingRight;
            }
            child.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int measuredChildState = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                measuredChildState = combineMeasuredStates(measuredChildState, child.getMeasuredState());
            }
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(maxWidth + (this.mPaddingLeft + this.mPaddingRight), getSuggestedMinimumWidth()), widthMeasureSpec, measuredChildState), resolveSizeAndState(Math.max(maxHeight + (this.mPaddingTop + this.mPaddingBottom), getSuggestedMinimumHeight()), heightMeasureSpec, measuredChildState << 16));
    }

    public void onBackStackChanged() {
        updateCrumbs();
    }

    private int getPreEntryCount() {
        int i = 1;
        int i2 = this.mTopEntry != null ? 1 : 0;
        if (this.mParentEntry == null) {
            i = 0;
        }
        return i2 + i;
    }

    private BackStackEntry getPreEntry(int index) {
        if (this.mParentEntry != null) {
            return index == 0 ? this.mParentEntry : this.mTopEntry;
        } else {
            return this.mTopEntry;
        }
    }

    void updateCrumbs() {
        FragmentManager fm = this.mActivity.getFragmentManager();
        int numEntries = fm.getBackStackEntryCount();
        int numPreEntries = getPreEntryCount();
        int numViews = this.mContainer.getChildCount();
        int i = 0;
        while (i < numEntries + numPreEntries) {
            BackStackEntry bse = i < numPreEntries ? getPreEntry(i) : fm.getBackStackEntryAt(i - numPreEntries);
            if (i < numViews && this.mContainer.getChildAt(i).getTag() != bse) {
                for (int j = i; j < numViews; j++) {
                    this.mContainer.removeViewAt(i);
                }
                numViews = i;
            }
            if (i >= numViews) {
                View item = this.mInflater.inflate(this.mLayoutResId, this, false);
                TextView text = (TextView) item.findViewById(R.id.title);
                text.setText(bse.getBreadCrumbTitle());
                text.setTag(bse);
                if (!this.mIsDeviceDefault) {
                    text.setTextColor(this.mTextColor);
                }
                if (i == 0) {
                    item.findViewById(16908350).setVisibility(8);
                }
                if (this.mIsDeviceDefault && !this.mIsDeviceDefaultDark) {
                    Drawable mStateListDrawable = (StateListDrawable) text.getBackground();
                    text.setBackground(new RippleDrawable(ColorStateList.valueOf(Color.argb(41, 0, 0, 0)), mStateListDrawable, mStateListDrawable));
                    Drawable d = mStateListDrawable.getStateDrawable(0);
                    if (d instanceof ColorDrawable) {
                        ((ColorDrawable) d).setColor(Color.argb(76, Color.red(this.mColorPrimary), Color.green(this.mColorPrimary), Color.blue(this.mColorPrimary)));
                    }
                    if ((numEntries < numPreEntries && numPreEntries - i == 1) || (numEntries >= numPreEntries && i == numEntries)) {
                        text.setTypeface(Typeface.create("sec-roboto-light", 1));
                    }
                }
                this.mContainer.addView(item);
                text.setOnClickListener(this.mOnClickListener);
            }
            i++;
        }
        int viewI = numEntries + numPreEntries;
        numViews = this.mContainer.getChildCount();
        while (numViews > viewI) {
            this.mContainer.removeViewAt(numViews - 1);
            numViews--;
        }
        i = 0;
        while (i < numViews) {
            View child = this.mContainer.getChildAt(i);
            child.findViewById(R.id.title).setEnabled(i < numViews + -1);
            if (this.mMaxVisible > 0) {
                int i2;
                child.setVisibility(i < numViews - this.mMaxVisible ? 8 : 0);
                View leftIcon = child.findViewById(16908350);
                if (i <= numViews - this.mMaxVisible || i == 0) {
                    i2 = 8;
                } else {
                    i2 = 0;
                }
                leftIcon.setVisibility(i2);
            }
            i++;
        }
    }
}
