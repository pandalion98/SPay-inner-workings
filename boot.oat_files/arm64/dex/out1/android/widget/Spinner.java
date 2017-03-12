package android.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings$System;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListPopupWindow.ForwardingListener;
import android.widget.PopupWindow.OnDismissListener;
import com.android.internal.R;

public class Spinner extends AbsSpinner implements OnClickListener {
    private static final int MAX_ITEMS_MEASURED = 15;
    public static final int MODE_DIALOG = 0;
    public static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "Spinner";
    private Drawable mArchivedBackground;
    private boolean mDisableChildrenWhenDisabled;
    int mDropDownWidth;
    private int mDropdownHorizontalOffset;
    private ForwardingListener mForwardingListener;
    private int mGravity;
    private boolean mIsThemeDeviceDefaultFamily;
    private int mMode;
    private SpinnerPopup mPopup;
    private Context mPopupContext;
    private SpinnerAdapter mTempAdapter;
    private Rect mTempRect;

    private interface SpinnerPopup {
        void dismiss();

        Drawable getBackground();

        CharSequence getHintText();

        int getHorizontalOffset();

        int getVerticalOffset();

        boolean isShowing();

        void setAdapter(ListAdapter listAdapter);

        void setBackgroundDrawable(Drawable drawable);

        void setHeight(int i);

        void setHorizontalOffset(int i);

        void setPromptText(CharSequence charSequence);

        void setVerticalOffset(int i);

        void show(int i, int i2);
    }

    private class DialogPopup implements SpinnerPopup, OnClickListener {
        private ListAdapter mListAdapter;
        private AlertDialog mPopup;
        private CharSequence mPrompt;

        private DialogPopup() {
        }

        public void dismiss() {
            if (this.mPopup != null) {
                this.mPopup.dismiss();
                this.mPopup = null;
            }
        }

        public boolean isShowing() {
            return this.mPopup != null ? this.mPopup.isShowing() : false;
        }

        public void setAdapter(ListAdapter adapter) {
            this.mListAdapter = adapter;
        }

        public void setPromptText(CharSequence hintText) {
            this.mPrompt = hintText;
        }

        public CharSequence getHintText() {
            return this.mPrompt;
        }

        public void show(int textDirection, int textAlignment) {
            if (this.mListAdapter != null) {
                Builder builder = new Builder(Spinner.this.getPopupContext());
                if (this.mPrompt != null) {
                    builder.setTitle(this.mPrompt);
                }
                this.mPopup = builder.setSingleChoiceItems(this.mListAdapter, Spinner.this.getSelectedItemPosition(), this).create();
                ListView listView = this.mPopup.getListView();
                listView.setTextDirection(textDirection);
                listView.setTextAlignment(textAlignment);
                this.mPopup.show();
            }
        }

        public void onClick(DialogInterface dialog, int which) {
            Spinner.this.setSelection(which);
            if (Spinner.this.mOnItemClickListener != null) {
                Spinner.this.performItemClick(null, which, this.mListAdapter.getItemId(which));
            }
            dismiss();
        }

        public void setBackgroundDrawable(Drawable bg) {
            Log.e(Spinner.TAG, "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        public void setVerticalOffset(int px) {
            Log.e(Spinner.TAG, "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        public void setHorizontalOffset(int px) {
            Log.e(Spinner.TAG, "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        public Drawable getBackground() {
            return null;
        }

        public int getVerticalOffset() {
            return 0;
        }

        public int getHorizontalOffset() {
            return 0;
        }

        public void setHeight(int height) {
            Log.e(Spinner.TAG, "Cannot set DropDown Height for MODE_DIALOG, ignoring");
        }
    }

    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(SpinnerAdapter adapter, Theme dropDownTheme) {
            this.mAdapter = adapter;
            if (adapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) adapter;
            }
            if (dropDownTheme != null && (adapter instanceof ThemedSpinnerAdapter)) {
                ThemedSpinnerAdapter themedAdapter = (ThemedSpinnerAdapter) adapter;
                if (themedAdapter.getDropDownViewTheme() == null) {
                    themedAdapter.setDropDownViewTheme(dropDownTheme);
                }
            }
        }

        public int getCount() {
            return this.mAdapter == null ? 0 : this.mAdapter.getCount();
        }

        public Object getItem(int position) {
            return this.mAdapter == null ? null : this.mAdapter.getItem(position);
        }

        public long getItemId(int position) {
            return this.mAdapter == null ? -1 : this.mAdapter.getItemId(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return this.mAdapter == null ? null : this.mAdapter.getDropDownView(position, convertView, parent);
        }

        public boolean hasStableIds() {
            return this.mAdapter != null && this.mAdapter.hasStableIds();
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(observer);
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter adapter = this.mListAdapter;
            if (adapter != null) {
                return adapter.areAllItemsEnabled();
            }
            return true;
        }

        public boolean isEnabled(int position) {
            ListAdapter adapter = this.mListAdapter;
            if (adapter != null) {
                return adapter.isEnabled(position);
            }
            return true;
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    private class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
        private ListAdapter mAdapter;
        private CharSequence mHintText;

        public DropdownPopup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setAnchorView(Spinner.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new OnItemClickListener(Spinner.this) {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Spinner.this.setSelection(position);
                    if (Spinner.this.mOnItemClickListener != null) {
                        Spinner.this.performItemClick(v, position, DropdownPopup.this.mAdapter.getItemId(position));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        public void setAdapter(ListAdapter adapter) {
            super.setAdapter(adapter);
            this.mAdapter = adapter;
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        public void setPromptText(CharSequence hintText) {
            this.mHintText = hintText;
        }

        void computeContentWidth() {
            Drawable background = getBackground();
            int hOffset = 0;
            Rect contentFrame = new Rect();
            Spinner.this.getWindowVisibleContentFrame(contentFrame);
            if (background != null) {
                background.getPadding(Spinner.this.mTempRect);
                hOffset = Spinner.this.isLayoutRtl() ? Spinner.this.mTempRect.right : -Spinner.this.mTempRect.left;
            } else {
                Rect access$400 = Spinner.this.mTempRect;
                Spinner.this.mTempRect.right = 0;
                access$400.left = 0;
            }
            int spinnerPaddingLeft = Spinner.this.getPaddingLeft();
            int spinnerPaddingRight = Spinner.this.getPaddingRight();
            int spinnerWidth = Spinner.this.getWidth();
            if (Spinner.this.mDropDownWidth == -2) {
                int contentWidth = Spinner.this.measureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                int contentWidthLimit = ((contentFrame.right - contentFrame.left) - Spinner.this.mTempRect.left) - Spinner.this.mTempRect.right;
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit;
                }
                if (Spinner.this.mIsThemeDeviceDefaultFamily) {
                    setContentWidth(Math.max(contentWidth, spinnerWidth));
                } else {
                    setContentWidth(Math.max(contentWidth, (spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight));
                }
            } else if (Spinner.this.mDropDownWidth == -1) {
                setContentWidth((spinnerWidth - spinnerPaddingLeft) - spinnerPaddingRight);
            } else {
                setContentWidth(Spinner.this.mDropDownWidth);
            }
            if (Spinner.this.isLayoutRtl()) {
                hOffset += (spinnerWidth - spinnerPaddingRight) - getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            if (Spinner.this.mIsThemeDeviceDefaultFamily) {
                setHorizontalOffset(Spinner.this.mDropdownHorizontalOffset + hOffset);
            } else {
                setHorizontalOffset(hOffset);
            }
        }

        public void show(int textDirection, int textAlignment) {
            boolean wasShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            ListView listView = getListView();
            listView.setChoiceMode(1);
            listView.setTextDirection(textDirection);
            listView.setTextAlignment(textAlignment);
            setSelection(Spinner.this.getSelectedItemPosition());
            if (!wasShowing) {
                ViewTreeObserver vto = Spinner.this.getViewTreeObserver();
                if (vto != null) {
                    final OnGlobalLayoutListener layoutListener = new OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            if (Spinner.this.isVisibleToUser()) {
                                DropdownPopup.this.computeContentWidth();
                                super.show();
                                return;
                            }
                            DropdownPopup.this.dismiss();
                        }
                    };
                    vto.addOnGlobalLayoutListener(layoutListener);
                    setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ViewTreeObserver vto = Spinner.this.getViewTreeObserver();
                            if (vto != null) {
                                vto.removeOnGlobalLayoutListener(layoutListener);
                            }
                        }
                    });
                }
            }
        }
    }

    static class SavedState extends SavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean showDropdown;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.showDropdown = in.readByte() != (byte) 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (this.showDropdown ? 1 : 0));
        }
    }

    public Spinner(Context context) {
        this(context, null);
    }

    public Spinner(Context context, int mode) {
        this(context, null, R.attr.spinnerStyle, mode);
    }

    public Spinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spinnerStyle);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0, -1);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, 0, mode);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        this(context, attrs, defStyleAttr, defStyleRes, mode, null);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mMode = -1;
        this.mIsThemeDeviceDefaultFamily = false;
        this.mDropdownHorizontalOffset = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr, defStyleRes);
        if (popupTheme != null) {
            this.mPopupContext = new ContextThemeWrapper(context, popupTheme);
        } else {
            int popupThemeResId = a.getResourceId(7, 0);
            if (popupThemeResId != 0) {
                this.mPopupContext = new ContextThemeWrapper(context, popupThemeResId);
            } else {
                this.mPopupContext = context;
            }
        }
        if (mode == -1) {
            mode = a.getInt(5, 0);
        }
        this.mMode = mode;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        this.mIsThemeDeviceDefaultFamily = outValue.data != 0;
        this.mArchivedBackground = getBackground();
        switch (mode) {
            case 0:
                this.mPopup = new DialogPopup();
                this.mPopup.setPromptText(a.getString(3));
                break;
            case 1:
                final DropdownPopup popup = new DropdownPopup(this.mPopupContext, attrs, defStyleAttr, defStyleRes);
                TypedArray pa = this.mPopupContext.obtainStyledAttributes(attrs, R.styleable.Spinner, defStyleAttr, defStyleRes);
                TypedArray pb = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow, defStyleAttr, defStyleRes);
                this.mDropdownHorizontalOffset = pb.getDimensionPixelOffset(0, 0);
                this.mDropDownWidth = pa.getLayoutDimension(4, -2);
                popup.setBackgroundDrawable(pa.getDrawable(2));
                popup.setPromptText(a.getString(3));
                pa.recycle();
                pb.recycle();
                this.mPopup = popup;
                this.mForwardingListener = new ForwardingListener(this) {
                    public ListPopupWindow getPopup() {
                        return popup;
                    }

                    public boolean onForwardingStarted() {
                        if (!Spinner.this.mPopup.isShowing()) {
                            Spinner.this.mPopup.show(Spinner.this.getTextDirection(), Spinner.this.getTextAlignment());
                        }
                        return true;
                    }
                };
                break;
        }
        this.mGravity = a.getInt(0, 17);
        this.mDisableChildrenWhenDisabled = a.getBoolean(9, false);
        a.recycle();
        if (this.mTempAdapter != null) {
            setAdapter(this.mTempAdapter);
            this.mTempAdapter = null;
        }
    }

    public Context getPopupContext() {
        return this.mPopupContext;
    }

    public void setPopupBackgroundDrawable(Drawable background) {
        if (this.mPopup instanceof DropdownPopup) {
            this.mPopup.setBackgroundDrawable(background);
        } else {
            Log.e(TAG, "setPopupBackgroundDrawable: incompatible spinner mode; ignoring...");
        }
    }

    public void setPopupBackgroundResource(int resId) {
        setPopupBackgroundDrawable(getPopupContext().getDrawable(resId));
    }

    public Drawable getPopupBackground() {
        return this.mPopup.getBackground();
    }

    public void setDropDownVerticalOffset(int pixels) {
        this.mPopup.setVerticalOffset(pixels);
    }

    public int getDropDownVerticalOffset() {
        return this.mPopup.getVerticalOffset();
    }

    public void setDropDownHorizontalOffset(int pixels) {
        this.mDropdownHorizontalOffset = pixels;
        this.mPopup.setHorizontalOffset(pixels);
    }

    public int getDropDownHorizontalOffset() {
        return this.mPopup.getHorizontalOffset();
    }

    public void twSetDropDownHeight(int pixels) {
        if (this.mPopup instanceof DropdownPopup) {
            this.mPopup.setHeight(pixels);
        } else {
            Log.e(TAG, "Cannot set dropdown height for MODE_DIALOG, ignoring");
        }
    }

    public void setDropDownWidth(int pixels) {
        if (this.mPopup instanceof DropdownPopup) {
            this.mDropDownWidth = pixels;
        } else {
            Log.e(TAG, "Cannot set dropdown width for MODE_DIALOG, ignoring");
        }
    }

    public int getDropDownWidth() {
        return this.mDropDownWidth;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.mDisableChildrenWhenDisabled) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setEnabled(enabled);
            }
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((gravity & 7) == 0) {
                gravity |= Gravity.START;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setAdapter(SpinnerAdapter adapter) {
        if (this.mPopup == null) {
            this.mTempAdapter = adapter;
            return;
        }
        super.setAdapter(adapter);
        this.mRecycler.clear();
        if (this.mContext.getApplicationInfo().targetSdkVersion < 21 || adapter == null || adapter.getViewTypeCount() == 1) {
            this.mPopup.setAdapter(new DropDownAdapter(adapter, (this.mPopupContext == null ? this.mContext : this.mPopupContext).getTheme()));
            return;
        }
        throw new IllegalArgumentException("Spinner adapter view type count must be 1");
    }

    public int getBaseline() {
        View child = null;
        if (getChildCount() > 0) {
            child = getChildAt(0);
        } else if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            child = makeView(0, false);
            this.mRecycler.put(0, child);
        }
        if (child == null) {
            return -1;
        }
        int childBaseline = child.getBaseline();
        if (childBaseline >= 0) {
            return child.getTop() + childBaseline;
        }
        return -1;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        throw new RuntimeException("setOnItemClickListener cannot be used with a spinner.");
    }

    public void setOnItemClickListenerInt(OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mForwardingListener == null || !this.mForwardingListener.onTouch(this, event)) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mPopup != null && MeasureSpec.getMode(widthMeasureSpec) == Integer.MIN_VALUE) {
            int contentWidth;
            int measuredWidth = getMeasuredWidth();
            if (!this.mIsThemeDeviceDefaultFamily || getSelectedItemPosition() <= -1 || getSelectedItemPosition() >= this.mAdapter.getCount()) {
                contentWidth = measureContentWidth(getAdapter(), getBackground());
            } else {
                contentWidth = twGetCurrentContentWidth(getAdapter(), getBackground());
            }
            setMeasuredDimension(Math.min(Math.max(measuredWidth, contentWidth), MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
        changeButtonBackground();
    }

    private void changeButtonBackground() {
        boolean show = true;
        if (this.mIsThemeDeviceDefaultFamily) {
            if (Settings$System.getInt(getContext().getContentResolver(), Settings$System.SHOW_BUTTON_BACKGROUND, 0) != 1) {
                show = false;
            }
            if (show) {
                setBackgroundResource(R.drawable.tw_spinner_show_button_background_material);
            } else {
                setBackground(this.mArchivedBackground);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        layout(0, false);
        this.mInLayout = false;
    }

    void layout(int delta, boolean animate) {
        int childrenLeft = this.mSpinnerPadding.left;
        int childrenWidth = ((this.mRight - this.mLeft) - this.mSpinnerPadding.left) - this.mSpinnerPadding.right;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        if (this.mItemCount == 0 || this.mAdapter == null) {
            resetList();
            return;
        }
        if (this.mNextSelectedPosition >= 0) {
            setSelectedPositionInt(this.mNextSelectedPosition);
        }
        recycleAllViews();
        removeAllViewsInLayout();
        this.mFirstPosition = this.mSelectedPosition;
        if (this.mAdapter != null) {
            View sel = makeView(this.mSelectedPosition, true);
            int width = sel.getMeasuredWidth();
            int selectedOffset = childrenLeft;
            switch (Gravity.getAbsoluteGravity(this.mGravity, getLayoutDirection()) & 7) {
                case 1:
                    selectedOffset = ((childrenWidth / 2) + childrenLeft) - (width / 2);
                    break;
                case 5:
                    selectedOffset = (childrenLeft + childrenWidth) - width;
                    break;
            }
            sel.offsetLeftAndRight(selectedOffset);
        }
        this.mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        this.mDataChanged = false;
        this.mNeedSync = false;
        setNextSelectedPositionInt(this.mSelectedPosition);
    }

    private View makeView(int position, boolean addChild) {
        View child;
        if (!this.mDataChanged) {
            child = this.mRecycler.get(position);
            if (child != null) {
                setUpChild(child, addChild);
                return child;
            }
        }
        child = this.mAdapter.getView(position, null, this);
        setUpChild(child, addChild);
        return child;
    }

    private void setUpChild(View child, boolean addChild) {
        LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = generateDefaultLayoutParams();
        }
        addViewInLayout(child, 0, lp);
        child.setSelected(hasFocus());
        if (this.mDisableChildrenWhenDisabled) {
            child.setEnabled(isEnabled());
        }
        child.measure(ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mSpinnerPadding.left + this.mSpinnerPadding.right, lp.width), ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, this.mSpinnerPadding.top + this.mSpinnerPadding.bottom, lp.height));
        int childTop = this.mSpinnerPadding.top + ((((getMeasuredHeight() - this.mSpinnerPadding.bottom) - this.mSpinnerPadding.top) - child.getMeasuredHeight()) / 2);
        child.layout(0, childTop, 0 + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
        if (!addChild) {
            removeViewInLayout(child);
        }
    }

    public boolean performClick() {
        boolean handled = super.performClick();
        if (!handled) {
            handled = true;
            if (!this.mPopup.isShowing()) {
                playSoundEffect(0);
                this.mPopup.show(getTextDirection(), getTextAlignment());
            }
        }
        return handled;
    }

    public void twDismissSpinnerPopup() {
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        setSelection(which);
        dialog.dismiss();
    }

    public CharSequence getAccessibilityClassName() {
        return Spinner.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (this.mAdapter != null) {
            info.setCanOpenPopup(true);
        }
    }

    public void setPrompt(CharSequence prompt) {
        this.mPopup.setPromptText(prompt);
    }

    public void setPromptId(int promptId) {
        setPrompt(getContext().getText(promptId));
    }

    public CharSequence getPrompt() {
        return this.mPopup.getHintText();
    }

    int measureContentWidth(SpinnerAdapter adapter, Drawable background) {
        if (adapter == null) {
            return 0;
        }
        int width = 0;
        View itemView = null;
        int itemType = 0;
        int widthMeasureSpec = MeasureSpec.makeSafeMeasureSpec(getMeasuredWidth(), 0);
        int heightMeasureSpec = MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
        int start = Math.max(0, getSelectedItemPosition());
        int end = Math.min(adapter.getCount(), start + 15);
        for (int i = Math.max(0, start - (15 - (end - start))); i < end; i++) {
            int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            itemView = adapter.getView(i, itemView, this);
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new LayoutParams(-2, -2));
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        if (background == null) {
            return width;
        }
        background.getPadding(this.mTempRect);
        return width + (this.mTempRect.left + this.mTempRect.right);
    }

    private int twGetCurrentContentWidth(SpinnerAdapter adapter, Drawable background) {
        if (adapter == null) {
            return 0;
        }
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        View itemView = adapter.getView(getSelectedItemPosition(), null, this);
        if (itemView.getLayoutParams() == null) {
            itemView.setLayoutParams(new LayoutParams(-2, -2));
        }
        itemView.measure(widthMeasureSpec, heightMeasureSpec);
        int width = itemView.getMeasuredWidth();
        if (background == null) {
            return width;
        }
        background.getPadding(this.mTempRect);
        return width + (this.mTempRect.left + this.mTempRect.right);
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        boolean z = this.mPopup != null && this.mPopup.isShowing();
        ss.showDropdown = z;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.showDropdown) {
            ViewTreeObserver vto = getViewTreeObserver();
            if (vto != null) {
                vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (!Spinner.this.mPopup.isShowing()) {
                            Spinner.this.mPopup.show(Spinner.this.getTextDirection(), Spinner.this.getTextAlignment());
                        }
                        ViewTreeObserver vto = Spinner.this.getViewTreeObserver();
                        if (vto != null) {
                            vto.removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((newConfig.orientation == 2 || newConfig.orientation == 1) && this.mPopup != null && this.mPopup.isShowing() && this.mMode == 1) {
            this.mPopup.dismiss();
        }
    }
}
