package android.widget;

import android.app.im.InjectionManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.widget.ScrollingTabContainerView;

public abstract class AdapterView<T extends Adapter> extends ViewGroup {
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = Long.MIN_VALUE;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    public static final int ITEM_VIEW_TYPE_IGNORE = -1;
    static final int SYNC_FIRST_POSITION = 1;
    static final int SYNC_MAX_DURATION_MILLIS = 100;
    static final int SYNC_SELECTED_POSITION = 0;
    private final boolean isElasticEnabled;
    boolean mBlockLayoutRequests;
    boolean mDataChanged;
    private boolean mDesiredFocusableInTouchModeState;
    private boolean mDesiredFocusableState;
    private View mEmptyView;
    @ExportedProperty(category = "scrolling")
    int mFirstPosition;
    boolean mInLayout;
    @ExportedProperty(category = "list")
    int mItemCount;
    private int mLayoutHeight;
    boolean mNeedSync;
    @ExportedProperty(category = "list")
    int mNextSelectedPosition;
    long mNextSelectedRowId;
    int mOldItemCount;
    int mOldSelectedPosition;
    long mOldSelectedRowId;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    OnTwMultiSelectedListener mOnTwMultiSelectedListener;
    OnTwNotifyKeyPressListener mOnTwNotifyKeyPressListener;
    private boolean mPenPressState;
    private SelectionNotifier mPendingSelectionNotifier;
    @ExportedProperty(category = "list")
    int mSelectedPosition;
    long mSelectedRowId;
    private SelectionNotifier mSelectionNotifier;
    int mSpecificTop;
    long mSyncHeight;
    int mSyncMode;
    int mSyncPosition;
    long mSyncRowId;

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    class AdapterDataSetObserver extends DataSetObserver {
        private Parcelable mInstanceState = null;

        AdapterDataSetObserver() {
        }

        public void onChanged() {
            AdapterView.this.mDataChanged = true;
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = AdapterView.this.getAdapter().getCount();
            if (!AdapterView.this.getAdapter().hasStableIds() || this.mInstanceState == null || AdapterView.this.mOldItemCount != 0 || AdapterView.this.mItemCount <= 0) {
                AdapterView.this.rememberSyncState();
            } else {
                AdapterView.this.onRestoreInstanceState(this.mInstanceState);
                this.mInstanceState = null;
            }
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void onInvalidated() {
            AdapterView.this.mDataChanged = true;
            if (AdapterView.this.getAdapter().hasStableIds()) {
                this.mInstanceState = AdapterView.this.onSaveInstanceState();
            }
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = 0;
            AdapterView.this.mSelectedPosition = -1;
            AdapterView.this.mSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNextSelectedPosition = -1;
            AdapterView.this.mNextSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNeedSync = false;
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void clearSavedState() {
            this.mInstanceState = null;
        }
    }

    public static class AdapterContextMenuInfo implements ContextMenuInfo {
        public long id;
        public int position;
        public View targetView;

        public AdapterContextMenuInfo(View targetView, int position, long id) {
            this.targetView = targetView;
            this.position = position;
            this.id = id;
        }
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long j);

        void onNothingSelected(AdapterView<?> adapterView);
    }

    public interface OnTwMultiSelectedListener {
        void OnTwMultiSelectStart(int i, int i2);

        void OnTwMultiSelectStop(int i, int i2);

        void onTwMultiSelected(AdapterView<?> adapterView, View view, int i, long j, boolean z, boolean z2, boolean z3);
    }

    public interface OnTwNotifyKeyPressListener {
        void onTwNotifyKeyPress(AdapterView<?> adapterView, View view, int i, long j, boolean z);
    }

    private class SelectionNotifier implements Runnable {
        private SelectionNotifier() {
        }

        public void run() {
            AdapterView.this.mPendingSelectionNotifier = null;
            if (!AdapterView.this.mDataChanged || AdapterView.this.getViewRootImpl() == null || !AdapterView.this.getViewRootImpl().isLayoutRequested()) {
                AdapterView.this.dispatchOnItemSelected();
            } else if (AdapterView.this.getAdapter() != null) {
                AdapterView.this.mPendingSelectionNotifier = this;
            }
        }
    }

    public abstract T getAdapter();

    public abstract View getSelectedView();

    public abstract void setAdapter(T t);

    public abstract void setSelection(int i);

    public AdapterView(Context context) {
        this(context, null);
    }

    public AdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mFirstPosition = 0;
        this.mSyncRowId = Long.MIN_VALUE;
        this.mNeedSync = false;
        this.mInLayout = false;
        this.mNextSelectedPosition = -1;
        this.mNextSelectedRowId = Long.MIN_VALUE;
        this.mSelectedPosition = -1;
        this.mSelectedRowId = Long.MIN_VALUE;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.mBlockLayoutRequests = false;
        this.isElasticEnabled = true;
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public boolean performItemClick(View view, int position, long id) {
        boolean result;
        if (this.mOnItemClickListener != null) {
            if (!((this.mOnItemClickListener instanceof ScrollingTabContainerView) || this.mPenPressState)) {
                playSoundEffect(0);
            }
            if (InjectionManager.getInstance() != null && view != null && getTag() != null && InjectionManager.getInstance().dispatchAdapterEvent(view.getContext(), 1, this, view, position, id)) {
                return true;
            }
            this.mOnItemClickListener.onItemClick(this, view, position, id);
            result = true;
        } else {
            result = false;
        }
        if (view != null) {
            view.sendAccessibilityEvent(1);
        }
        return result;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.mOnItemLongClickListener = listener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    public final OnItemSelectedListener getOnItemSelectedListener() {
        return this.mOnItemSelectedListener;
    }

    public void setTwMultiSelectedListener(OnTwMultiSelectedListener listener) {
        this.mOnTwMultiSelectedListener = listener;
    }

    public final OnTwMultiSelectedListener getOnTwMultiSelectedListener() {
        return this.mOnTwMultiSelectedListener;
    }

    public boolean twNotifyMultiSelectedState(View view, int position, long id, boolean shiftPressState, boolean ctrlPressState, boolean penPressState) {
        this.mPenPressState = penPressState;
        if (this.mOnTwMultiSelectedListener == null) {
            return false;
        }
        this.mOnTwMultiSelectedListener.onTwMultiSelected(this, view, position, id, shiftPressState, ctrlPressState, penPressState);
        return true;
    }

    public void twNotifyMultiSelectedStart() {
        if (this.mOnTwMultiSelectedListener == null) {
        }
    }

    public void twNotifyMultiSelectedStop() {
        this.mPenPressState = false;
        if (this.mOnTwMultiSelectedListener == null) {
        }
    }

    public void twNotifyMultiSelectedStart(int startX, int startY) {
        if (this.mOnTwMultiSelectedListener != null) {
            this.mOnTwMultiSelectedListener.OnTwMultiSelectStart(startX, startY);
        }
    }

    public void twNotifyMultiSelectedStop(int endX, int endY) {
        this.mPenPressState = false;
        if (this.mOnTwMultiSelectedListener != null) {
            this.mOnTwMultiSelectedListener.OnTwMultiSelectStop(endX, endY);
        }
    }

    public void setTwNotifyOnKeyPressListener(OnTwNotifyKeyPressListener listener) {
        this.mOnTwNotifyKeyPressListener = listener;
    }

    public final OnTwNotifyKeyPressListener getOnTwNotifyKeyPressListener() {
        return this.mOnTwNotifyKeyPressListener;
    }

    public boolean twNotifyKeyPress(View view, int position, long id, boolean shiftPressState) {
        if (this.mOnTwNotifyKeyPressListener == null) {
            return false;
        }
        this.mOnTwNotifyKeyPressListener.onTwNotifyKeyPress(this, view, position, id, shiftPressState);
        return true;
    }

    public void addView(View child) {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    public void addView(View child, int index) {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    public void addView(View child, LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    public void addView(View child, int index, LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    public void removeView(View child) {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    public void removeViewAt(int index) {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    public void removeAllViews() {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mLayoutHeight = getHeight();
    }

    @CapturedViewProperty
    public int getSelectedItemPosition() {
        return this.mNextSelectedPosition;
    }

    @CapturedViewProperty
    public long getSelectedItemId() {
        return this.mNextSelectedRowId;
    }

    public Object getSelectedItem() {
        T adapter = getAdapter();
        int selection = getSelectedItemPosition();
        if (adapter == null || adapter.getCount() <= 0 || selection < 0) {
            return null;
        }
        return adapter.getItem(selection);
    }

    @CapturedViewProperty
    public int getCount() {
        return this.mItemCount;
    }

    public int getPositionForView(View view) {
        int childCount;
        View listItem = view;
        while (true) {
            int i;
            try {
                View v = (View) listItem.getParent();
                if (v != null && !v.equals(this)) {
                    listItem = v;
                } else if (listItem != null) {
                    return -1;
                } else {
                    childCount = getChildCount();
                    for (i = 0; i < childCount; i++) {
                        if (getChildAt(i).equals(listItem)) {
                            return this.mFirstPosition + i;
                        }
                    }
                    return -1;
                }
            } catch (ClassCastException e) {
                return -1;
            }
        }
        if (listItem != null) {
            return -1;
        }
        childCount = getChildCount();
        for (i = 0; i < childCount; i++) {
            if (getChildAt(i).equals(listItem)) {
                return this.mFirstPosition + i;
            }
        }
        return -1;
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public int getLastVisiblePosition() {
        return (this.mFirstPosition + getChildCount()) - 1;
    }

    @RemotableViewMethod
    public void setEmptyView(View emptyView) {
        boolean empty = true;
        this.mEmptyView = emptyView;
        if (emptyView != null && emptyView.getImportantForAccessibility() == 0) {
            emptyView.setImportantForAccessibility(1);
        }
        T adapter = getAdapter();
        if (!(adapter == null || adapter.isEmpty())) {
            empty = false;
        }
        updateEmptyStatus(empty);
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    boolean isInFilterMode() {
        return false;
    }

    public void setFocusable(boolean focusable) {
        boolean z = true;
        T adapter = getAdapter();
        boolean empty;
        if (adapter == null || adapter.getCount() == 0) {
            empty = true;
        } else {
            empty = false;
        }
        this.mDesiredFocusableState = focusable;
        if (!focusable) {
            this.mDesiredFocusableInTouchModeState = false;
        }
        if (!focusable || (empty && !isInFilterMode())) {
            z = false;
        }
        super.setFocusable(z);
    }

    public void setFocusableInTouchMode(boolean focusable) {
        boolean z = true;
        T adapter = getAdapter();
        boolean empty;
        if (adapter == null || adapter.getCount() == 0) {
            empty = true;
        } else {
            empty = false;
        }
        this.mDesiredFocusableInTouchModeState = focusable;
        if (focusable) {
            this.mDesiredFocusableState = true;
        }
        if (!focusable || (empty && !isInFilterMode())) {
            z = false;
        }
        super.setFocusableInTouchMode(z);
    }

    void checkFocus() {
        boolean empty;
        boolean focusable;
        boolean z;
        boolean z2 = false;
        T adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            empty = true;
        } else {
            empty = false;
        }
        if (!empty || isInFilterMode()) {
            focusable = true;
        } else {
            focusable = false;
        }
        if (focusable && this.mDesiredFocusableInTouchModeState) {
            z = true;
        } else {
            z = false;
        }
        super.setFocusableInTouchMode(z);
        if (focusable && this.mDesiredFocusableState) {
            z = true;
        } else {
            z = false;
        }
        super.setFocusable(z);
        if (this.mEmptyView != null) {
            if (adapter == null || adapter.isEmpty()) {
                z2 = true;
            }
            updateEmptyStatus(z2);
        }
    }

    private void updateEmptyStatus(boolean empty) {
        if (isInFilterMode()) {
            empty = false;
        }
        if (empty) {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(0);
                setVisibility(8);
            } else {
                setVisibility(0);
            }
            if (this.mDataChanged) {
                onLayout(false, this.mLeft, this.mTop, this.mRight, this.mBottom);
                return;
            }
            return;
        }
        if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(8);
        }
        setVisibility(0);
    }

    public Object getItemAtPosition(int position) {
        T adapter = getAdapter();
        return (adapter == null || position < 0) ? null : adapter.getItem(position);
    }

    public long getItemIdAtPosition(int position) {
        T adapter = getAdapter();
        return (adapter == null || position < 0) ? Long.MIN_VALUE : adapter.getItemId(position);
    }

    public void setOnClickListener(OnClickListener l) {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mSelectionNotifier);
    }

    void selectionChanged() {
        this.mPendingSelectionNotifier = null;
        if (this.mOnItemSelectedListener == null && !AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            return;
        }
        if (this.mInLayout || this.mBlockLayoutRequests) {
            if (this.mSelectionNotifier == null) {
                this.mSelectionNotifier = new SelectionNotifier();
            } else {
                removeCallbacks(this.mSelectionNotifier);
            }
            post(this.mSelectionNotifier);
            return;
        }
        dispatchOnItemSelected();
    }

    void selectionChangedForAccessibility() {
        performAccessibilityActionsOnSelected();
    }

    private void dispatchOnItemSelected() {
        fireOnSelected();
        performAccessibilityActionsOnSelected();
    }

    private void fireOnSelected() {
        if (this.mOnItemSelectedListener != null) {
            int selection = getSelectedItemPosition();
            if (selection >= 0) {
                View v = getSelectedView();
                this.mOnItemSelectedListener.onItemSelected(this, v, selection, getAdapter().getItemId(selection));
                return;
            }
            this.mOnItemSelectedListener.onNothingSelected(this);
        }
    }

    private void performAccessibilityActionsOnSelected() {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && getSelectedItemPosition() >= 0) {
            sendAccessibilityEvent(4);
        }
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        View selectedView = getSelectedView();
        if (selectedView != null && selectedView.getVisibility() == 0 && selectedView.dispatchPopulateAccessibilityEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean onRequestSendAccessibilityEventInternal(View child, AccessibilityEvent event) {
        if (!super.onRequestSendAccessibilityEventInternal(child, event)) {
            return false;
        }
        AccessibilityEvent record = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(record);
        child.dispatchPopulateAccessibilityEvent(record);
        event.appendRecord(record);
        return true;
    }

    public CharSequence getAccessibilityClassName() {
        return AdapterView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setScrollable(isScrollableForAccessibility());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            info.setEnabled(selectedView.isEnabled());
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setScrollable(isScrollableForAccessibility());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            event.setEnabled(selectedView.isEnabled());
        }
        event.setCurrentItemIndex(getSelectedItemPosition());
        event.setFromIndex(getFirstVisiblePosition());
        event.setToIndex(getLastVisiblePosition());
        event.setItemCount(getCount());
    }

    private boolean isScrollableForAccessibility() {
        T adapter = getAdapter();
        if (adapter == null) {
            return false;
        }
        int itemCount = adapter.getCount();
        if (itemCount <= 0) {
            return false;
        }
        if (getFirstVisiblePosition() > 0 || getLastVisiblePosition() < itemCount - 1) {
            return true;
        }
        return false;
    }

    protected boolean canAnimate() {
        return super.canAnimate() && this.mItemCount > 0;
    }

    void handleDataChanged() {
        int count = this.mItemCount;
        boolean found = false;
        if (count > 0) {
            int newPos;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                newPos = findSyncPosition();
                if (newPos >= 0 && lookForSelectablePosition(newPos, true) == newPos) {
                    setNextSelectedPositionInt(newPos);
                    found = true;
                }
            }
            if (!found) {
                newPos = getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                int selectablePos = lookForSelectablePosition(newPos, true);
                if (selectablePos < 0) {
                    selectablePos = lookForSelectablePosition(newPos, false);
                }
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    checkSelectionChanged();
                    found = true;
                }
            }
        }
        if (!found) {
            this.mSelectedPosition = -1;
            this.mSelectedRowId = Long.MIN_VALUE;
            this.mNextSelectedPosition = -1;
            this.mNextSelectedRowId = Long.MIN_VALUE;
            this.mNeedSync = false;
            checkSelectionChanged();
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    void checkSelectionChanged() {
        if (!(this.mSelectedPosition == this.mOldSelectedPosition && this.mSelectedRowId == this.mOldSelectedRowId)) {
            selectionChanged();
            this.mOldSelectedPosition = this.mSelectedPosition;
            this.mOldSelectedRowId = this.mSelectedRowId;
        }
        if (this.mPendingSelectionNotifier != null) {
            this.mPendingSelectionNotifier.run();
        }
    }

    int findSyncPosition() {
        int count = this.mItemCount;
        if (count == 0) {
            return -1;
        }
        long idToMatch = this.mSyncRowId;
        int seed = this.mSyncPosition;
        if (idToMatch == Long.MIN_VALUE) {
            return -1;
        }
        seed = Math.min(count - 1, Math.max(0, seed));
        long endTime = SystemClock.uptimeMillis() + 100;
        int first = seed;
        int last = seed;
        boolean next = false;
        T adapter = getAdapter();
        if (adapter == null) {
            return -1;
        }
        while (SystemClock.uptimeMillis() <= endTime) {
            if (adapter.getItemId(seed) != idToMatch) {
                boolean hitLast = last == count + -1;
                boolean hitFirst = first == 0;
                if (hitLast && hitFirst) {
                    break;
                } else if (hitFirst || (next && !hitLast)) {
                    last++;
                    seed = last;
                    next = false;
                } else if (hitLast || !(next || hitFirst)) {
                    first--;
                    seed = first;
                    next = true;
                }
            } else {
                return seed;
            }
        }
        return -1;
    }

    int lookForSelectablePosition(int position, boolean lookDown) {
        return position;
    }

    void setSelectedPositionInt(int position) {
        this.mSelectedPosition = position;
        this.mSelectedRowId = getItemIdAtPosition(position);
    }

    void setNextSelectedPositionInt(int position) {
        this.mNextSelectedPosition = position;
        this.mNextSelectedRowId = getItemIdAtPosition(position);
        if (this.mNeedSync && this.mSyncMode == 0 && position >= 0) {
            this.mSyncPosition = position;
            this.mSyncRowId = this.mNextSelectedRowId;
        }
    }

    void rememberSyncState() {
        if (getChildCount() > 0) {
            this.mNeedSync = true;
            this.mSyncHeight = (long) this.mLayoutHeight;
            View v;
            if (this.mSelectedPosition >= 0) {
                v = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (v != null) {
                    this.mSpecificTop = v.getTop();
                }
                this.mSyncMode = 0;
                return;
            }
            v = getChildAt(0);
            T adapter = getAdapter();
            if (this.mFirstPosition < 0 || this.mFirstPosition >= adapter.getCount()) {
                this.mSyncRowId = -1;
            } else {
                this.mSyncRowId = adapter.getItemId(this.mFirstPosition);
            }
            this.mSyncPosition = this.mFirstPosition;
            if (v != null) {
                this.mSpecificTop = v.getTop();
            }
            this.mSyncMode = 1;
        }
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("scrolling:firstPosition", this.mFirstPosition);
        encoder.addProperty("list:nextSelectedPosition", this.mNextSelectedPosition);
        encoder.addProperty("list:nextSelectedRowId", (float) this.mNextSelectedRowId);
        encoder.addProperty("list:selectedPosition", this.mSelectedPosition);
        encoder.addProperty("list:itemCount", this.mItemCount);
    }
}
