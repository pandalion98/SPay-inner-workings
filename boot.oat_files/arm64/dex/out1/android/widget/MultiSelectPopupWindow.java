package android.widget;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.Layout;
import android.text.MultiSelection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManagerGlobal;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.sec.android.emergencymode.EmergencyManager;
import java.util.ArrayList;

public class MultiSelectPopupWindow {
    private static final String TAG = "MultiSelectPopupWindow";
    private static final float[] TEMP_POSITION = new float[2];
    private static MultiSelectQuickPopupWindow mMultiSelectQuickPopupWindow;
    private static TextView mTextView;
    private static MultiSelectPopupWindow sInstance;
    private MultiSelectController mMultiSelectController;
    private PositionListener mPositionListener;
    private Drawable mSelectHandleLeft;
    private Drawable mSelectHandleRight;

    private interface CursorController extends OnTouchModeChangeListener {
        void hide();

        void onDetached();

        void show();
    }

    private interface TextViewPositionListener {
        void updatePosition(int i, int i2, boolean z, boolean z2);
    }

    private abstract class HandleView extends View implements TextViewPositionListener {
        static final int HANDLE_TYPE_END = 2;
        static final int HANDLE_TYPE_NONE = 0;
        static final int HANDLE_TYPE_START = 1;
        protected int mBaselineY;
        private final PopupWindow mContainer = new PopupWindow(MultiSelectPopupWindow.mTextView.getContext(), null, (int) R.attr.textSelectHandleWindowStyle);
        protected boolean mCurCursorPosTop = false;
        protected Drawable mDrawable;
        protected Drawable mDrawableLtr;
        protected Drawable mDrawableRtl;
        protected int mEndRange;
        public int mHandleType = 0;
        protected int mHotspotX;
        private float mIdealVerticalOffset;
        private int mInitPositionX;
        private int mInitPositionY;
        private float mInitRawY;
        protected boolean mIsDragging;
        private int mLastParentX;
        private int mLastParentY;
        protected boolean mPositionHasChanged = true;
        protected int mPositionX;
        protected int mPositionY;
        private int mPreviousOffset = -1;
        protected int mStartRange;
        private int mStatusbarHeight = ((int) (25.0f * MultiSelectPopupWindow.mTextView.getResources().getDisplayMetrics().density));
        private float mTouchOffsetY;
        private float mTouchToWindowOffsetX;
        private float mTouchToWindowOffsetY;
        protected boolean mbFlipCursor = false;
        protected boolean mbSwitchCursor;

        public abstract int getCurrentCursorOffset();

        protected abstract int getHotspotX(Drawable drawable, boolean z);

        public abstract void updatePosition(float f, float f2);

        protected abstract void updateSelection(int i);

        public HandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(MultiSelectPopupWindow.mTextView.getContext());
            this.mContainer.setSplitTouchEnabled(true);
            this.mContainer.setClippingEnabled(false);
            this.mContainer.setWindowLayoutType(1002);
            this.mContainer.setContentView(this);
            this.mDrawableLtr = drawableLtr;
            this.mDrawableRtl = drawableRtl;
            updateDrawable();
            recalHandleView();
        }

        protected void updateDrawable() {
            boolean isRtlCharAtOffset = MultiSelectPopupWindow.mTextView.getLayout().isRtlCharAt(getCurrentCursorOffset());
            this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
            this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(this.mDrawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
        }

        public void show() {
            if (!isShowing()) {
                MultiSelectPopupWindow.this.getPositionListener().addSubscriber(this, true);
                this.mPreviousOffset = -1;
                positionAtCursorOffset(getCurrentCursorOffset(), false, false);
                int[] window = new int[2];
                int[] screen = new int[2];
                MultiSelectPopupWindow.mTextView.getLocationInWindow(window);
                MultiSelectPopupWindow.mTextView.getLocationOnScreen(screen);
                int positionX = this.mPositionX + window[0];
                int positionY = this.mPositionY + window[1];
                int gapX = screen[0] - window[0];
                int gapY = screen[1] - window[1];
                if (isShowing()) {
                    if (!(MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken())) {
                        if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                            this.mContainer.setLayoutInScreenEnabled(false);
                        }
                        positionX += gapX;
                        positionY += gapY;
                    }
                    this.mContainer.update(positionX, positionY, -1, -1);
                } else if (MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken()) {
                    this.mContainer.setLayoutInScreenEnabled(false);
                    try {
                        this.mContainer.showAtLocation(MultiSelectPopupWindow.mTextView, 0, positionX, positionY);
                    } catch (BadTokenException e) {
                        MultiSelectPopupWindow.mTextView.clearAllMultiSelection();
                        Log.e(MultiSelectPopupWindow.TAG, "showAtLocation occur BadTokenException");
                    }
                } else {
                    this.mContainer.setLayoutInScreenEnabled(true);
                    if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                        this.mContainer.setLayoutInScreenEnabled(false);
                    }
                    this.mContainer.showAtLocation(MultiSelectPopupWindow.mTextView.getApplicationWindowToken(), 0, positionX + gapX, positionY + gapY);
                }
            }
        }

        protected void dismiss() {
            this.mIsDragging = false;
            this.mContainer.dismiss();
            onDetached();
            this.mbSwitchCursor = false;
        }

        public void hide() {
            dismiss();
            MultiSelectPopupWindow.this.getPositionListener().removeSubscriber(this);
        }

        void hideMultiSelectQuickPopupWindow() {
            if (MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow() != null) {
                MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().hide();
            }
        }

        public boolean isShowing() {
            return this.mContainer.isShowing();
        }

        private boolean isVisible() {
            if (this.mIsDragging) {
                return true;
            }
            float compensation = 0.0f;
            Layout layout = MultiSelectPopupWindow.mTextView.getLayout();
            if (layout != null) {
                compensation = layout.getParagraphDirection(layout.getLineForOffset(getCurrentCursorOffset())) == -1 ? 0.5f : -0.5f;
            }
            return MultiSelectPopupWindow.this.isPositionVisible((int) (((float) (this.mPositionX + this.mHotspotX)) - compensation), this.mBaselineY);
        }

        protected void positionAtCursorOffset(int offset, boolean parentPositionChanged, boolean parentScrolled) {
            Layout layout = MultiSelectPopupWindow.mTextView.getLayout();
            if (layout != null) {
                boolean offsetChanged = offset != this.mPreviousOffset;
                if (offsetChanged || parentPositionChanged || parentScrolled) {
                    if (offsetChanged) {
                        updateSelection(offset);
                    }
                    int line = layout.getLineForOffset(offset);
                    this.mPositionX = (int) ((layout.getPrimaryHorizontal(offset) + (layout.getParagraphDirection(line) == -1 ? 0.5f : -0.5f)) - ((float) this.mHotspotX));
                    this.mPositionY = layout.getLineBottom(line);
                    this.mBaselineY = layout.getLineBaseline(line);
                    this.mPositionX += MultiSelectPopupWindow.mTextView.viewportToContentHorizontalOffset();
                    this.mPositionY += MultiSelectPopupWindow.mTextView.viewportToContentVerticalOffset();
                    this.mBaselineY += MultiSelectPopupWindow.mTextView.viewportToContentVerticalOffset();
                    this.mPreviousOffset = offset;
                    this.mPositionHasChanged = true;
                }
            }
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            positionAtCursorOffset(getCurrentCursorOffset(), parentPositionChanged, parentScrolled);
            if (parentPositionChanged || this.mPositionHasChanged) {
                if (this.mIsDragging) {
                    if (!(parentPositionX == this.mLastParentX && parentPositionY == this.mLastParentY)) {
                        this.mTouchToWindowOffsetX += (float) (parentPositionX - this.mLastParentX);
                        this.mTouchToWindowOffsetY += (float) (parentPositionY - this.mLastParentY);
                        this.mLastParentX = parentPositionX;
                        this.mLastParentY = parentPositionY;
                    }
                    onHandleMoved();
                }
                if (isVisible() && !parentPositionChanged) {
                    int[] window = new int[2];
                    int[] screen = new int[2];
                    MultiSelectPopupWindow.mTextView.getLocationInWindow(window);
                    MultiSelectPopupWindow.mTextView.getLocationOnScreen(screen);
                    int gapX = screen[0] - window[0];
                    int gapY = screen[1] - window[1];
                    int positionX = parentPositionX + this.mPositionX;
                    int positionY = parentPositionY + this.mPositionY;
                    if (isShowing()) {
                        if (!(MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken())) {
                            if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                                this.mContainer.setLayoutInScreenEnabled(false);
                            }
                            positionX += gapX;
                            positionY += gapY;
                        }
                        this.mContainer.update(positionX, positionY, -1, -1);
                    } else if (MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken()) {
                        this.mContainer.setLayoutInScreenEnabled(false);
                        try {
                            this.mContainer.showAtLocation(MultiSelectPopupWindow.mTextView, 0, positionX, positionY);
                        } catch (BadTokenException e) {
                            MultiSelectPopupWindow.mTextView.clearAllMultiSelection();
                            Log.e(MultiSelectPopupWindow.TAG, "showAtLocation occur BadTokenException");
                        }
                    } else {
                        this.mContainer.setLayoutInScreenEnabled(true);
                        if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                            this.mContainer.setLayoutInScreenEnabled(false);
                        }
                        this.mContainer.showAtLocation(MultiSelectPopupWindow.mTextView.getApplicationWindowToken(), 0, positionX + gapX, positionY + gapY);
                    }
                } else if (isShowing()) {
                    dismiss();
                }
                this.mPositionHasChanged = false;
                MultiSelectPopupWindow.this.relocateMultiSelectQuickPopupWindow();
            }
        }

        protected void onDraw(Canvas c) {
            this.mDrawable.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
            this.mDrawable.draw(c);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
                return true;
            }
            switch (ev.getActionMasked()) {
                case 0:
                    this.mTouchToWindowOffsetX = ev.getRawXForScaledWindow() - ((float) this.mPositionX);
                    this.mTouchToWindowOffsetY = ev.getRawYForScaledWindow() - ((float) this.mPositionY);
                    this.mInitPositionX = this.mPositionX;
                    this.mInitPositionY = this.mPositionY;
                    this.mInitRawY = ev.getRawYForScaledWindow();
                    int[] range = new int[2];
                    if (MultiSelectPopupWindow.mTextView.getVisibleTextRange(range)) {
                        this.mStartRange = range[0];
                        this.mEndRange = range[1];
                    } else {
                        this.mStartRange = 0;
                        this.mEndRange = text.length();
                    }
                    PositionListener positionListener = MultiSelectPopupWindow.this.getPositionListener();
                    this.mLastParentX = positionListener.getPositionX();
                    this.mLastParentY = positionListener.getPositionY();
                    this.mIsDragging = true;
                    MultiSelectPopupWindow.mTextView.mIsTouchDown = true;
                    break;
                case 1:
                    this.mIsDragging = false;
                    MultiSelectPopupWindow.mTextView.mIsTouchDown = false;
                    refreshForSwitchingCursor();
                    int selStart = MultiSelection.getSelectionStart(text);
                    int selEnd = MultiSelection.getSelectionEnd(text);
                    if (selStart > selEnd) {
                        MultiSelection.setSelection((Spannable) text, selEnd, selStart);
                    }
                    if (!(MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow() == null || MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().isShowing())) {
                        MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().show();
                        break;
                    }
                case 2:
                    float newVerticalOffset;
                    float rawX = ev.getRawXForScaledWindow();
                    float rawY = ev.getRawYForScaledWindow();
                    float previousVerticalOffset = this.mTouchToWindowOffsetY - ((float) this.mLastParentY);
                    float currentVerticalOffset = (rawY - ((float) this.mPositionY)) - ((float) this.mLastParentY);
                    if (previousVerticalOffset < this.mIdealVerticalOffset) {
                        newVerticalOffset = Math.max(Math.min(currentVerticalOffset, this.mIdealVerticalOffset), previousVerticalOffset);
                    } else if (currentVerticalOffset < previousVerticalOffset) {
                        newVerticalOffset = Math.max(Math.max(currentVerticalOffset, this.mIdealVerticalOffset), previousVerticalOffset);
                    } else {
                        newVerticalOffset = Math.min(Math.max(currentVerticalOffset, this.mIdealVerticalOffset), previousVerticalOffset);
                    }
                    this.mTouchToWindowOffsetY = ((float) this.mLastParentY) + newVerticalOffset;
                    float newPosX = (rawX - this.mTouchToWindowOffsetX) + ((float) this.mHotspotX);
                    float newPosY = (rawY - this.mTouchToWindowOffsetY) + this.mTouchOffsetY;
                    if (getViewRootImpl() != null) {
                        PointF scaleRatio = getViewRootImpl().getMultiWindowScale();
                        if (scaleRatio.x != 1.0f) {
                            newPosX = (((rawX - (this.mTouchToWindowOffsetX + ((float) this.mInitPositionX))) / scaleRatio.x) + ((float) this.mInitPositionX)) + ((float) this.mHotspotX);
                        }
                        if (scaleRatio.y != 1.0f) {
                            newPosY = (((rawY - this.mInitRawY) / scaleRatio.y) + ((float) this.mInitPositionY)) + this.mTouchOffsetY;
                        }
                    }
                    updatePosition(newPosX, newPosY);
                    break;
                case 3:
                    this.mIsDragging = false;
                    break;
            }
            return true;
        }

        public boolean isDragging() {
            return this.mIsDragging;
        }

        void onHandleMoved() {
            hideMultiSelectQuickPopupWindow();
        }

        public void onDetached() {
            hideMultiSelectQuickPopupWindow();
        }

        protected boolean calculateForSwitchingCursor() {
            return true;
        }

        public boolean refreshForSwitchingCursor() {
            return true;
        }

        public void initPreviousOffset() {
            this.mPreviousOffset = -1;
        }

        public void recalHandleView() {
            int handleHeight = this.mDrawable.getIntrinsicHeight();
            this.mTouchOffsetY = -0.3f * ((float) handleHeight);
            this.mIdealVerticalOffset = 0.7f * ((float) handleHeight);
        }
    }

    private class MultiSelectController implements CursorController {
        private float mDownPositionX;
        private float mDownPositionY;
        private SelectionEndHandleView mEndHandle;
        private SelectionStartHandleView mStartHandle;

        private MultiSelectController() {
        }

        public void show() {
            initDrawables();
            initHandles();
        }

        private void initDrawables() {
            if (MultiSelectPopupWindow.this.mSelectHandleLeft == null) {
                MultiSelectPopupWindow.this.mSelectHandleLeft = MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(MultiSelectPopupWindow.mTextView.mTextSelectHandleLeftRes);
            }
            if (MultiSelectPopupWindow.this.mSelectHandleRight == null) {
                MultiSelectPopupWindow.this.mSelectHandleRight = MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(MultiSelectPopupWindow.mTextView.mTextSelectHandleRightRes);
            }
        }

        private void initHandles() {
            if (this.mStartHandle == null) {
                this.mStartHandle = new SelectionStartHandleView(MultiSelectPopupWindow.this.mSelectHandleLeft, MultiSelectPopupWindow.this.mSelectHandleRight);
            }
            if (this.mEndHandle == null) {
                this.mEndHandle = new SelectionEndHandleView(MultiSelectPopupWindow.this.mSelectHandleRight, MultiSelectPopupWindow.this.mSelectHandleLeft);
            }
            if (MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow() != null) {
                MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().show();
            }
            this.mStartHandle.show();
            this.mEndHandle.show();
        }

        public void hide() {
            if (this.mStartHandle != null) {
                this.mStartHandle.hide();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.hide();
            }
            if (MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow() != null) {
                MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().hide();
            }
        }

        public boolean isSelectionStartDragged() {
            return this.mStartHandle != null && this.mStartHandle.isDragging();
        }

        public boolean isSelectionEndDragged() {
            return this.mEndHandle != null && this.mEndHandle.isDragging();
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        public void onDetached() {
            MultiSelectPopupWindow.mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
            if (this.mStartHandle != null) {
                this.mStartHandle.onDetached();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.onDetached();
            }
        }

        public void initPreviousOffset() {
            if (this.mStartHandle != null) {
                this.mStartHandle.initPreviousOffset();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.initPreviousOffset();
            }
        }

        public void relocateMultiSelectQuickPopupWindow() {
            if (!isSelectionStartDragged() && !isSelectionEndDragged() && this.mStartHandle != null && this.mEndHandle != null) {
                CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
                if (text == null) {
                    Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
                }
                int selStart = MultiSelection.getSelectionStart((Spannable) text);
                int selEnd = MultiSelection.getSelectionEnd((Spannable) text);
                if (this.mStartHandle.isShowing()) {
                    MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().relocateMultiSelectQuickPopupWindow(selStart);
                } else if (this.mEndHandle.isShowing()) {
                    MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().relocateMultiSelectQuickPopupWindow(selEnd);
                } else if (this.mStartHandle.isShowing() && this.mEndHandle.isShowing()) {
                    MultiSelectPopupWindow.this.getMultiSelectQuickPopupWindow().relocateMultiSelectQuickPopupWindow(selStart);
                } else if (!this.mStartHandle.isShowing() && !this.mEndHandle.isShowing()) {
                    this.mStartHandle.hideMultiSelectQuickPopupWindow();
                } else if (!this.mEndHandle.isShowing()) {
                    this.mStartHandle.hideMultiSelectQuickPopupWindow();
                }
            }
        }
    }

    private class MultiSelectQuickPopupWindow implements OnClickListener, TextViewPositionListener {
        private static final int COPY = 1;
        private static final int DICTIONARY = 3;
        private static final int SELECTALL = 0;
        private static final int SHARE = 2;
        private static final int TRANSLATE = 4;
        private static final int TW_POPUP_TEXT_LAYOUT = 17367383;
        private int MAX_ITEMS_LANDSCAPE_PHONE;
        private int MAX_ITEMS_LANDSCAPE_TABLET;
        private int MAX_ITEMS_PORTRAIT_PHONE;
        private int MAX_ITEMS_PORTRAIT_TABLET;
        private final int TOTAL_ITEMS = 5;
        private int mActionBarHeight;
        private ViewGroup mContentView;
        private Drawable mDivider;
        private int mDividerWidth;
        private int mExtraItemWidth;
        private int mHandlerHeight;
        private HorizontalScrollView mHorizontalScrollView;
        ArrayList<Drawable> mIconDrawables = new ArrayList();
        private boolean mIsTablet = false;
        private int mItemWidth;
        private int mMaxShowingMenuCount;
        boolean[] mMenuEnables = new boolean[]{true, true, false, false, false};
        private int mMenuItemCount;
        ArrayList<TextView> mMenuTextViews = new ArrayList();
        private int mPopupBgPaddingHeight;
        private int mPopupBgPaddingWidth;
        private int mPopupHeight;
        private int mPopupMaxWidth;
        private int mPopupWidth;
        private PopupWindow mPopupWindow;
        int mPositionX;
        int mPositionY;
        private int mStatusbarHeight;

        public MultiSelectQuickPopupWindow() {
            this.mIsTablet = MultiSelectPopupWindow.this.isTablet();
            createPopupWindow();
            this.mPopupWindow.setWindowLayoutType(1002);
            this.mPopupWindow.setHeight(-2);
            initContentView();
            setHeight();
            this.mPopupWindow.setContentView(this.mHorizontalScrollView);
        }

        public void refreshResource() {
            ((TextView) this.mMenuTextViews.get(0)).setText((int) R.string.selectAll);
            ((TextView) this.mMenuTextViews.get(1)).setText((int) R.string.copy);
            ((TextView) this.mMenuTextViews.get(2)).setText((int) R.string.share);
            ((TextView) this.mMenuTextViews.get(3)).setText((int) R.string.dictionary);
            ((TextView) this.mMenuTextViews.get(4)).setText((int) R.string.translate);
            for (int i = 0; i < 5; i++) {
                ((TextView) this.mMenuTextViews.get(i)).enableMultiSelection(false);
            }
            setHeight();
        }

        private void createPopupWindow() {
            Drawable bgRes = MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_frame_mtrl);
            Rect bgResPadding = new Rect();
            bgRes.getPadding(bgResPadding);
            this.mPopupBgPaddingWidth = bgResPadding.left + bgResPadding.right;
            this.mPopupBgPaddingHeight = bgResPadding.top + bgResPadding.bottom;
            this.mPopupWindow = new PopupWindow(MultiSelectPopupWindow.mTextView.getContext(), null);
            this.mPopupWindow.setClippingEnabled(false);
            this.mPopupWindow.setBackgroundDrawable(bgRes);
        }

        private void initContentView() {
            this.MAX_ITEMS_PORTRAIT_PHONE = MultiSelectPopupWindow.mTextView.getContext().getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_portrait_phone);
            this.MAX_ITEMS_LANDSCAPE_PHONE = MultiSelectPopupWindow.mTextView.getContext().getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_landscape_phone);
            this.MAX_ITEMS_PORTRAIT_TABLET = MultiSelectPopupWindow.mTextView.getContext().getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_portrait_tablet);
            this.MAX_ITEMS_LANDSCAPE_TABLET = MultiSelectPopupWindow.mTextView.getContext().getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_landscape_tablet);
            this.mItemWidth = (int) MultiSelectPopupWindow.mTextView.getContext().getResources().getDimension(R.dimen.tw_text_edit_action_popup_item_width);
            this.mExtraItemWidth = (int) MultiSelectPopupWindow.mTextView.getContext().getResources().getDimension(R.dimen.tw_text_edit_action_popup_extra_item_width);
            this.mDivider = MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_divider_mtrl);
            this.mDividerWidth = this.mDivider.getIntrinsicWidth();
            LayoutInflater inflater = (LayoutInflater) MultiSelectPopupWindow.mTextView.getContext().getSystemService("layout_inflater");
            LinearLayout linearLayout = new LinearLayout(MultiSelectPopupWindow.mTextView.getContext());
            linearLayout.setShowDividers(2);
            linearLayout.setDividerDrawable(this.mDivider);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(17);
            this.mContentView = linearLayout;
            this.mContentView.setLayoutParams(new LayoutParams(-2, -2));
            this.mHorizontalScrollView = new HorizontalScrollView(MultiSelectPopupWindow.mTextView.getContext());
            this.mHorizontalScrollView.setLayoutParams(new LayoutParams(-2, -2));
            this.mHorizontalScrollView.setHorizontalScrollBarEnabled(false);
            this.mIconDrawables.add(MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_selectall_mtrl));
            this.mIconDrawables.add(MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_copy_mtrl));
            this.mIconDrawables.add(MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_share_mtrl));
            this.mIconDrawables.add(MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_dictionary_mtrl));
            this.mIconDrawables.add(MultiSelectPopupWindow.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_translate_mtrl));
            for (int i = 0; i < 5; i++) {
                TextView t = (TextView) inflater.inflate(17367383, this.mContentView, false);
                t.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable) this.mIconDrawables.get(i), null, null);
                t.setOnClickListener(this);
                this.mContentView.addView(t);
                this.mMenuTextViews.add(i, t);
            }
            this.mHorizontalScrollView.addView(this.mContentView);
            this.mContentView.setLayoutDirection(0);
        }

        public void show() {
            refreshResource();
            this.mMenuItemCount = 0;
            this.mMenuEnables[2] = MultiSelectPopupWindow.this.isShareViaEnable();
            this.mMenuEnables[3] = MultiSelectPopupWindow.this.isDictionaryEnable();
            this.mMenuEnables[4] = MultiSelectPopupWindow.this.isTranslatorEnable();
            for (int i = 0; i < 5; i++) {
                if (this.mMenuEnables[i]) {
                    this.mMenuItemCount++;
                    ((TextView) this.mMenuTextViews.get(i)).setVisibility(0);
                } else {
                    ((TextView) this.mMenuTextViews.get(i)).setVisibility(8);
                }
            }
            calculatePopupWindowWidth();
            computeLocalPosition();
            this.mPopupWindow.setWidth(this.mPopupWidth);
            this.mHorizontalScrollView.setScrollX(0);
            MultiSelectPopupWindow.this.getPositionListener().addSubscriber(this, true);
            updateMultiSelectQuickPopupPosition();
        }

        private void calculatePopupWindowWidth() {
            int currentMenuCount = 0;
            this.mPopupWidth = 0;
            for (int i = 0; i < 5; i++) {
                if (this.mMenuEnables[i]) {
                    this.mPopupWidth += this.mItemWidth;
                    currentMenuCount++;
                    if (this.mPopupWidth <= getPopupWidthLimit()) {
                        this.mPopupWidth += this.mDividerWidth;
                        if (currentMenuCount >= getMenuLimit()) {
                            break;
                        }
                    } else {
                        this.mPopupWidth -= this.mItemWidth;
                        this.mPopupWidth -= this.mDividerWidth;
                        currentMenuCount--;
                        break;
                    }
                }
            }
            if (currentMenuCount < this.mMenuItemCount) {
                this.mPopupWidth += this.mExtraItemWidth;
            } else {
                this.mPopupWidth -= this.mDividerWidth;
            }
            this.mPopupWidth += this.mPopupBgPaddingWidth;
        }

        private int getMenuLimit() {
            boolean isPortrait = true;
            if (MultiSelectPopupWindow.mTextView.getContext().getResources().getConfiguration().orientation != 1) {
                isPortrait = false;
            }
            return this.mIsTablet ? isPortrait ? this.MAX_ITEMS_PORTRAIT_TABLET : this.MAX_ITEMS_LANDSCAPE_TABLET : isPortrait ? this.MAX_ITEMS_PORTRAIT_PHONE : this.MAX_ITEMS_LANDSCAPE_PHONE;
        }

        private int getPopupWidthLimit() {
            int items = getMenuLimit();
            this.mPopupMaxWidth = (this.mItemWidth * items) + ((items - 1) * this.mDividerWidth);
            return this.mPopupMaxWidth;
        }

        private void computeLocalPosition() {
            measureContent();
            int[] position = new int[2];
            getMultiSelectQuickPopupPosition(position);
            this.mPositionX = position[0];
            this.mPositionY = position[1];
        }

        protected void measureContent() {
            DisplayMetrics displayMetrics = MultiSelectPopupWindow.mTextView.getResources().getDisplayMetrics();
            this.mHorizontalScrollView.measure(MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        private void updateMultiSelectQuickPopupPosition() {
            int[] window = new int[2];
            int[] screen = new int[2];
            MultiSelectPopupWindow.mTextView.getLocationInWindow(window);
            MultiSelectPopupWindow.mTextView.getLocationOnScreen(screen);
            int gapX = screen[0] - window[0];
            int gapY = screen[1] - window[1];
            if (isShowing()) {
                if (!(MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken())) {
                    if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                        this.mPopupWindow.setLayoutInScreenEnabled(false);
                    }
                    this.mPositionX += gapX;
                    this.mPositionY += gapY;
                }
                this.mPopupWindow.update(this.mPositionX, this.mPositionY, -1, -1);
            } else if (MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == null || MultiSelectPopupWindow.mTextView.getApplicationWindowToken() == MultiSelectPopupWindow.mTextView.getWindowToken()) {
                this.mPopupWindow.setLayoutInScreenEnabled(false);
                try {
                    this.mPopupWindow.showAtLocation(MultiSelectPopupWindow.mTextView, 0, this.mPositionX, this.mPositionY);
                } catch (BadTokenException e) {
                    MultiSelectPopupWindow.mTextView.clearAllMultiSelection();
                    Log.e(MultiSelectPopupWindow.TAG, "showAtLocation occur BadTokenException");
                }
            } else {
                this.mPopupWindow.setLayoutInScreenEnabled(true);
                if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                    this.mPopupWindow.setLayoutInScreenEnabled(false);
                }
                this.mPositionX += gapX;
                this.mPositionY += gapY;
                this.mPopupWindow.showAtLocation(MultiSelectPopupWindow.mTextView.getApplicationWindowToken(), 0, this.mPositionX, this.mPositionY);
            }
        }

        public void relocateMultiSelectQuickPopupWindow(int offset) {
            if (offset >= 0) {
                computeLocalPosition();
                calculatePopupWindowWidth();
                this.mPopupWindow.setWidth(this.mPopupWidth);
                updateMultiSelectQuickPopupPosition();
            }
        }

        public void onClick(View view) {
            int id = 0;
            if (view == this.mMenuTextViews.get(0)) {
                id = R.id.multiSelectAll;
            } else if (view == this.mMenuTextViews.get(1)) {
                id = R.id.multiSelectCopy;
            } else if (view == this.mMenuTextViews.get(2)) {
                id = R.id.multiSelectShare;
            } else if (view == this.mMenuTextViews.get(3)) {
                id = R.id.multiSelectDictionary;
            } else if (view == this.mMenuTextViews.get(4)) {
                id = R.id.multiSelectTranslate;
            }
            MultiSelectPopupWindow.mTextView.onMultiSelectMenuItem(id);
        }

        public void hide() {
            this.mPopupWindow.dismiss();
            MultiSelectPopupWindow.this.getPositionListener().removeSubscriber(this);
        }

        public boolean isShowing() {
            return this.mPopupWindow.isShowing();
        }

        private void setHeight() {
            TypedArray a = null;
            try {
                a = MultiSelectPopupWindow.mTextView.getContext().obtainStyledAttributes(null, android.R.styleable.ActionBar, R.attr.actionBarStyle, 0);
                this.mActionBarHeight = a.getLayoutDimension(4, 0);
                this.mHandlerHeight = MultiSelectPopupWindow.mTextView.getResources().getDrawable(MultiSelectPopupWindow.mTextView.mTextSelectHandleRes).getIntrinsicHeight();
                this.mStatusbarHeight = (int) (25.0f * MultiSelectPopupWindow.mTextView.getResources().getDisplayMetrics().density);
                a.recycle();
            } catch (NotFoundException e) {
                a.recycle();
            }
        }

        private int getTextOffset() {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
            }
            return (MultiSelection.getSelectionStart(text) + MultiSelection.getSelectionEnd(text)) / 2;
        }

        private void getMultiSelectQuickPopupPosition(int[] position) {
            if (position == null) {
                position = new int[2];
            }
            int menuItemCount = this.mMenuItemCount;
            if (MultiSelectPopupWindow.mTextView.getContext().getResources().getConfiguration().orientation == 1) {
            }
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
            }
            int[] screen = new int[2];
            int[] window = new int[2];
            MultiSelectPopupWindow.mTextView.getLocationInWindow(window);
            MultiSelectPopupWindow.mTextView.getLocationOnScreen(screen);
            int parentViewPositionX = screen[0] - window[0];
            int parentViewPositionY = screen[1] - window[1];
            int viewportToContentVerticalOffset = MultiSelectPopupWindow.mTextView.viewportToContentVerticalOffset();
            int viewportToContentHorizontalOffset = MultiSelectPopupWindow.mTextView.viewportToContentHorizontalOffset();
            int screenWidth = MultiSelectPopupWindow.mTextView.getContext().getResources().getDisplayMetrics().widthPixels;
            int screenHeight = MultiSelectPopupWindow.mTextView.getContext().getResources().getDisplayMetrics().heightPixels;
            int fullScreenHeight = screenHeight;
            int screenTop = 0;
            int screenBottom = screenHeight;
            int selStart = MultiSelection.getSelectionStart(text);
            int selEnd = MultiSelection.getSelectionEnd(text);
            int startLine = MultiSelectPopupWindow.mTextView.getLayout().getLineForOffset(selStart);
            int endLine = MultiSelectPopupWindow.mTextView.getLayout().getLineForOffset(selEnd);
            int startLineTop = (MultiSelectPopupWindow.mTextView.getLayout().getLineTop(startLine) + window[1]) + viewportToContentVerticalOffset;
            int startLineBottom = (MultiSelectPopupWindow.mTextView.getLayout().getLineBottom(startLine) + window[1]) + viewportToContentVerticalOffset;
            int endLineTop = (MultiSelectPopupWindow.mTextView.getLayout().getLineTop(endLine) + window[1]) + viewportToContentVerticalOffset;
            int endLineBottom = (MultiSelectPopupWindow.mTextView.getLayout().getLineBottom(endLine) + window[1]) + viewportToContentVerticalOffset;
            int startX = (((int) MultiSelectPopupWindow.mTextView.getLayout().getPrimaryHorizontal(selStart)) + window[0]) + viewportToContentHorizontalOffset;
            int endX = (((int) MultiSelectPopupWindow.mTextView.getLayout().getPrimaryHorizontal(selEnd)) + window[0]) + viewportToContentHorizontalOffset;
            int maxWidth = MultiSelectPopupWindow.mTextView.getLayout().getWidth();
            int actionBarHeight = this.mActionBarHeight;
            int handlerHeight = this.mHandlerHeight;
            boolean isStatusBarVisible = true;
            this.mPopupHeight = this.mContentView.getMeasuredHeight() + this.mPopupBgPaddingHeight;
            try {
                isStatusBarVisible = WindowManagerGlobal.getWindowManagerService().isStatusBarVisible();
            } catch (RemoteException e) {
                Log.w(MultiSelectPopupWindow.TAG, "Remote exception while to check isStatusBarVisible");
            }
            if (!isStatusBarVisible) {
                this.mStatusbarHeight = 0;
            }
            if (MultiSelectPopupWindow.mTextView.isMultiWindow()) {
                Rect stackBoxBound = ((MultiWindowFacade) MultiSelectPopupWindow.mTextView.getContext().getSystemService("multiwindow_facade")).getStackBound(MultiSelectPopupWindow.mTextView.getContext().getBaseActivityToken());
                if (stackBoxBound != null) {
                    screenTop = stackBoxBound.top;
                    screenBottom = stackBoxBound.bottom;
                    screenHeight = stackBoxBound.height();
                }
                if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                    screenTop = 0;
                }
                if ((screen[1] < screenTop || screen[1] > screenBottom) && screen[1] > 0 && screen[1] < fullScreenHeight) {
                    screenTop = 0;
                    screenBottom = fullScreenHeight;
                }
            }
            if (startLine == endLine) {
                position[0] = (((endX - startX) / 2) + startX) - (this.mPopupWidth / 2);
            } else {
                position[0] = (window[0] + (maxWidth / 2)) - (this.mPopupWidth / 2);
            }
            if (position[0] < 0) {
                if (parentViewPositionX < Math.abs(position[0])) {
                    position[0] = position[0] + (Math.abs(position[0]) - parentViewPositionX);
                } else if ((position[0] + parentViewPositionX) + this.mPopupWidth > screenWidth) {
                    position[0] = position[0] - (((position[0] + parentViewPositionX) + this.mPopupWidth) - screenWidth);
                }
                if (position[0] < 0) {
                    position[0] = 0;
                    if ((position[0] + parentViewPositionX) + this.mPopupWidth > screenWidth) {
                        position[0] = position[0] - (((position[0] + parentViewPositionX) + this.mPopupWidth) - screenWidth);
                    }
                }
            } else if ((position[0] + this.mPopupWidth) + parentViewPositionX > screenWidth) {
                position[0] = position[0] - (((position[0] + parentViewPositionX) + this.mPopupWidth) - screenWidth);
            }
            position[1] = startLineTop - this.mPopupHeight;
            if (position[1] + parentViewPositionY <= (this.mStatusbarHeight + actionBarHeight) + screenTop) {
                position[1] = endLineBottom + handlerHeight;
            }
            if ((position[1] + parentViewPositionY) + this.mPopupHeight > screenBottom) {
                int middlePosition;
                if (endLineBottom > screenBottom) {
                    middlePosition = screenBottom;
                } else {
                    middlePosition = endLineBottom;
                }
                if (startLineBottom < 0) {
                    startLineBottom = 0;
                }
                middlePosition = (middlePosition - (startLineBottom + handlerHeight)) / 2;
                if (endLineBottom - (startLineBottom + handlerHeight) > this.mPopupHeight) {
                    position[1] = (startLineBottom + handlerHeight) + (middlePosition - (this.mPopupHeight / 2));
                } else {
                    position[1] = screenBottom - this.mPopupHeight;
                }
            }
            if ((position[1] + parentViewPositionY) + this.mPopupHeight > screenBottom || position[1] + parentViewPositionY < screenTop) {
                position[1] = screenBottom - this.mPopupHeight;
                position[1] = position[1] - screenTop;
            }
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (isShowing() && MultiSelectPopupWindow.this.isOffsetVisible(getTextOffset())) {
                if (parentScrolled) {
                    computeLocalPosition();
                }
                updateMultiSelectQuickPopupPosition();
            }
        }
    }

    private class PositionListener implements OnPreDrawListener {
        private final int MAXIMUM_NUMBER_OF_LISTENERS;
        private boolean[] mCanMove;
        private int[] mNewRect;
        private int mNumberOfListeners;
        private boolean mPositionHasChanged;
        private TextViewPositionListener[] mPositionListeners;
        private int mPositionX;
        private int mPositionY;
        private int[] mRect;
        private boolean mScrollHasChanged;
        private int[] mTempContentsViewCoords;
        final int[] mTempCoords;

        private PositionListener() {
            this.MAXIMUM_NUMBER_OF_LISTENERS = 3;
            this.mPositionListeners = new TextViewPositionListener[3];
            this.mCanMove = new boolean[3];
            this.mPositionHasChanged = true;
            this.mRect = new int[2];
            this.mNewRect = new int[2];
            this.mTempCoords = new int[2];
            this.mTempContentsViewCoords = null;
        }

        public void addSubscriber(TextViewPositionListener positionListener, boolean canMove) {
            if (this.mNumberOfListeners == 0) {
                updatePosition();
                MultiSelectPopupWindow.mTextView.getViewTreeObserver().addOnPreDrawListener(this);
            }
            int emptySlotIndex = -1;
            int i = 0;
            while (i < 3) {
                TextViewPositionListener listener = this.mPositionListeners[i];
                if (listener != positionListener) {
                    if (emptySlotIndex < 0 && listener == null) {
                        emptySlotIndex = i;
                    }
                    i++;
                } else {
                    return;
                }
            }
            this.mPositionListeners[emptySlotIndex] = positionListener;
            this.mCanMove[emptySlotIndex] = canMove;
            this.mNumberOfListeners++;
        }

        public void removeSubscriber(TextViewPositionListener positionListener) {
            for (int i = 0; i < 3; i++) {
                if (this.mPositionListeners[i] == positionListener) {
                    this.mPositionListeners[i] = null;
                    this.mNumberOfListeners--;
                    break;
                }
            }
            if (this.mNumberOfListeners == 0) {
                MultiSelectPopupWindow.mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
            }
        }

        public int getPositionX() {
            return this.mPositionX;
        }

        public int getPositionY() {
            return this.mPositionY;
        }

        public boolean onPreDraw() {
            if (MultiSelectPopupWindow.mTextView == null) {
                if (0 < 3) {
                    this.mPositionListeners[0] = null;
                }
                this.mNumberOfListeners = 0;
            } else {
                updatePosition();
                if (MultiSelectPopupWindow.mTextView.isScaleWindow()) {
                    this.mScrollHasChanged = true;
                    MultiSelectPopupWindow.this.initMultiSelectControllerPosition();
                }
                if (MultiSelectPopupWindow.mTextView.checkValidMultiSelectionForPreDraw()) {
                    int i = 0;
                    while (i < 3) {
                        if (this.mPositionHasChanged || this.mScrollHasChanged || this.mCanMove[i]) {
                            TextViewPositionListener positionListener = this.mPositionListeners[i];
                            if (positionListener != null) {
                                positionListener.updatePosition(this.mPositionX, this.mPositionY, this.mPositionHasChanged, this.mScrollHasChanged);
                            }
                        }
                        i++;
                    }
                    this.mScrollHasChanged = false;
                } else {
                    MultiSelectPopupWindow.mTextView.clearMultiSelection();
                }
            }
            return true;
        }

        private void updatePosition() {
            MultiSelectPopupWindow.mTextView.getLocationInWindow(this.mTempCoords);
            this.mNewRect[0] = MultiSelectPopupWindow.mTextView.getWidth();
            this.mNewRect[1] = MultiSelectPopupWindow.mTextView.getHeight();
            boolean z = (this.mTempCoords[0] == this.mPositionX && this.mTempCoords[1] == this.mPositionY && this.mRect[0] == this.mNewRect[0] && this.mRect[1] == this.mNewRect[1]) ? false : true;
            this.mPositionHasChanged = z;
            this.mPositionX = this.mTempCoords[0];
            this.mPositionY = this.mTempCoords[1];
            this.mRect[0] = this.mNewRect[0];
            this.mRect[1] = this.mNewRect[1];
        }

        public void onScrollChanged() {
            this.mScrollHasChanged = true;
        }
    }

    private class SelectionEndHandleView extends HandleView {
        public SelectionEndHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
            this.mHandleType = 2;
        }

        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return (drawable.getIntrinsicWidth() * 3) / 4;
            }
            return drawable.getIntrinsicWidth() / 4;
        }

        public int getCurrentCursorOffset() {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
            }
            return MultiSelection.getSelectionEnd(text);
        }

        public void updateSelection(int offset) {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
                return;
            }
            MultiSelection.setSelection((Spannable) text, MultiSelection.getSelectionStart(text), offset);
            updateDrawable();
        }

        public void updatePosition(float x, float y) {
            int offset = MultiSelectPopupWindow.mTextView.getOffsetForPosition(x, y);
            if (offset != MultiSelection.getSelectionStart(MultiSelectPopupWindow.mTextView.getTextForMultiSelection())) {
                if (offset > this.mEndRange) {
                    offset = this.mEndRange;
                }
                positionAtCursorOffset(offset, false, false);
            }
        }

        protected void positionAtCursorOffset(int offset, boolean parentPositionChanged, boolean parentScrolled) {
            super.positionAtCursorOffset(offset, parentPositionChanged, parentScrolled);
            if (!this.mIsDragging) {
                calculateForSwitchingCursor();
                this.mPositionHasChanged = true;
                invalidate();
            }
        }

        public boolean refreshForSwitchingCursor() {
            if (!this.mbSwitchCursor && (!isHandleViewScreenOut() || this.mbSwitchCursor)) {
                return false;
            }
            MultiSelectPopupWindow.mTextView.invalidate();
            return true;
        }

        protected boolean calculateForSwitchingCursor() {
            boolean bSwitchCursor = this.mbSwitchCursor;
            this.mbSwitchCursor = false;
            if (isHandleViewScreenOut()) {
                this.mbSwitchCursor = true;
            }
            if (bSwitchCursor == this.mbSwitchCursor) {
                return false;
            }
            updateDrawable();
            this.mPositionX = (int) ((MultiSelectPopupWindow.mTextView.getLayout().getPrimaryHorizontal(getCurrentCursorOffset()) - 0.5f) - ((float) this.mHotspotX));
            this.mPositionX += MultiSelectPopupWindow.mTextView.viewportToContentHorizontalOffset();
            return true;
        }

        protected void updateDrawable() {
            int offset = getCurrentCursorOffset();
            Drawable oldDrawable = this.mDrawable;
            boolean isRtlCharAtOffset = MultiSelectPopupWindow.mTextView.getLayout().isRtlCharAt(offset);
            if (this.mbSwitchCursor) {
                isRtlCharAtOffset = !isRtlCharAtOffset;
            }
            this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
            this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
            if (oldDrawable != this.mDrawable) {
                recalHandleView();
                invalidate();
            }
        }

        private boolean isHandleViewScreenOut() {
            return ((this.mPositionX + MultiSelectPopupWindow.this.getPositionListener().getPositionX()) + this.mHotspotX) + (this.mDrawableRtl.getIntrinsicWidth() / 2) > this.mContext.getResources().getDisplayMetrics().widthPixels;
        }
    }

    private class SelectionStartHandleView extends HandleView {
        public SelectionStartHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
            this.mHandleType = 1;
        }

        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return drawable.getIntrinsicWidth() / 4;
            }
            return (drawable.getIntrinsicWidth() * 3) / 4;
        }

        public int getCurrentCursorOffset() {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
            }
            return MultiSelection.getSelectionStart(text);
        }

        public void updateSelection(int offset) {
            CharSequence text = MultiSelectPopupWindow.mTextView.getTextForMultiSelection();
            if (text == null) {
                Log.e(MultiSelectPopupWindow.TAG, "getTextFormultiSelection() text is null");
                return;
            }
            MultiSelection.setSelection((Spannable) text, offset, MultiSelection.getSelectionEnd(text));
            updateDrawable();
        }

        public void updatePosition(float x, float y) {
            int offset = MultiSelectPopupWindow.mTextView.getOffsetForPosition(x, y);
            if (offset != MultiSelection.getSelectionEnd(MultiSelectPopupWindow.mTextView.getTextForMultiSelection())) {
                if (offset < this.mStartRange) {
                    offset = this.mStartRange;
                }
                positionAtCursorOffset(offset, false, false);
            }
        }

        protected void positionAtCursorOffset(int offset, boolean parentPositionChanged, boolean parentScrolled) {
            super.positionAtCursorOffset(offset, parentPositionChanged, parentScrolled);
            calculateForSwitchingCursor();
            this.mPositionHasChanged = true;
            invalidate();
        }

        public boolean refreshForSwitchingCursor() {
            if (!isHandleViewScreenOut() || this.mbSwitchCursor) {
                return false;
            }
            MultiSelectPopupWindow.mTextView.invalidate();
            return true;
        }

        protected boolean calculateForSwitchingCursor() {
            boolean bSwitchCursor = this.mbSwitchCursor;
            this.mbSwitchCursor = false;
            if (isHandleViewScreenOut()) {
                this.mbSwitchCursor = true;
            }
            if (bSwitchCursor == this.mbSwitchCursor) {
                return false;
            }
            updateDrawable();
            this.mPositionX = (int) ((MultiSelectPopupWindow.mTextView.getLayout().getPrimaryHorizontal(getCurrentCursorOffset()) - 0.5f) - ((float) this.mHotspotX));
            this.mPositionX += MultiSelectPopupWindow.mTextView.viewportToContentHorizontalOffset();
            return true;
        }

        protected void updateDrawable() {
            int offset = getCurrentCursorOffset();
            Drawable oldDrawable = this.mDrawable;
            boolean isRtlCharAtOffset = MultiSelectPopupWindow.mTextView.getLayout().isRtlCharAt(offset);
            if (this.mbSwitchCursor) {
                isRtlCharAtOffset = !isRtlCharAtOffset;
            }
            this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
            this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
            if (oldDrawable != this.mDrawable) {
                recalHandleView();
                invalidate();
            }
        }

        private boolean isHandleViewScreenOut() {
            return ((this.mPositionX + MultiSelectPopupWindow.this.getPositionListener().getPositionX()) + this.mHotspotX) - (this.mDrawableRtl.getIntrinsicWidth() / 2) < 0;
        }
    }

    public static MultiSelectPopupWindow getInstance() {
        if (sInstance == null) {
            sInstance = new MultiSelectPopupWindow();
        }
        return sInstance;
    }

    private MultiSelectPopupWindow() {
        mTextView = null;
        mMultiSelectQuickPopupWindow = null;
    }

    public void showMultiSelectPopupWindow() {
        if (getMultiSelectController() != null) {
            getMultiSelectController().hide();
            getMultiSelectController().show();
        }
    }

    public void hideMultiSelectPopupWindow() {
        if (getMultiSelectController() != null) {
            getMultiSelectController().hide();
        }
    }

    MultiSelectQuickPopupWindow getMultiSelectQuickPopupWindow() {
        if (mMultiSelectQuickPopupWindow != null) {
            return mMultiSelectQuickPopupWindow;
        }
        mMultiSelectQuickPopupWindow = new MultiSelectQuickPopupWindow();
        return mMultiSelectQuickPopupWindow;
    }

    public void changeCurrentSelectedView(TextView textView) {
        if (mTextView != textView) {
            mTextView = textView;
            if (mMultiSelectQuickPopupWindow == null) {
                mMultiSelectQuickPopupWindow = new MultiSelectQuickPopupWindow();
            }
        }
    }

    void onScrollChanged() {
        if (this.mPositionListener != null) {
            this.mPositionListener.onScrollChanged();
        }
    }

    private boolean isOffsetVisible(int offset) {
        Layout layout = mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        return isPositionVisible(mTextView.viewportToContentHorizontalOffset() + ((int) layout.getPrimaryHorizontal(offset)), mTextView.viewportToContentVerticalOffset() + layout.getLineBaseline(layout.getLineForOffset(offset)));
    }

    private boolean isShareViaEnable() {
        if (isEmergencyMode()) {
            return false;
        }
        return true;
    }

    private boolean isDictionaryEnable() {
        if (mTextView.getContext().getPackageManager().queryIntentActivities(new Intent("com.sec.android.app.dictionary.SEARCH"), 0).size() == 0 || isEmergencyMode()) {
            return false;
        }
        return true;
    }

    private boolean isTranslatorEnable() {
        if (mTextView.getContext().getPackageManager().queryIntentActivities(new Intent("com.sec.android.app.translator.TRANSLATE_FOR_NON_ACTIVITY"), 0).size() == 0 || !isEmergencyMode()) {
            return false;
        }
        return true;
    }

    private boolean isEmergencyMode() {
        EmergencyManager em = EmergencyManager.getInstance(mTextView.getContext());
        if (em != null) {
            return em.isEmergencyMode();
        }
        return false;
    }

    private PositionListener getPositionListener() {
        if (this.mPositionListener == null) {
            this.mPositionListener = new PositionListener();
        }
        return this.mPositionListener;
    }

    public void initMultiSelectControllerPosition() {
        if (getMultiSelectController() != null) {
            getMultiSelectController().initPreviousOffset();
        }
    }

    MultiSelectController getMultiSelectController() {
        if (mTextView == null) {
            return null;
        }
        if (this.mMultiSelectController == null) {
            this.mMultiSelectController = new MultiSelectController();
            mTextView.getViewTreeObserver().addOnTouchModeChangeListener(this.mMultiSelectController);
        }
        return this.mMultiSelectController;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isPositionVisible(int r12, int r13) {
        /*
        r11 = this;
        r10 = 0;
        r5 = 1;
        r4 = 0;
        r6 = TEMP_POSITION;
        monitor-enter(r6);
        r2 = TEMP_POSITION;	 Catch:{ all -> 0x008f }
        r7 = 0;
        r8 = (float) r12;	 Catch:{ all -> 0x008f }
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
        r7 = 1;
        r8 = (float) r13;	 Catch:{ all -> 0x008f }
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
        r3 = mTextView;	 Catch:{ all -> 0x008f }
    L_0x0012:
        if (r3 == 0) goto L_0x008c;
    L_0x0014:
        r7 = mTextView;	 Catch:{ all -> 0x008f }
        if (r3 == r7) goto L_0x002e;
    L_0x0018:
        r7 = 0;
        r8 = r2[r7];	 Catch:{ all -> 0x008f }
        r9 = r3.getScrollX();	 Catch:{ all -> 0x008f }
        r9 = (float) r9;	 Catch:{ all -> 0x008f }
        r8 = r8 - r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
        r7 = 1;
        r8 = r2[r7];	 Catch:{ all -> 0x008f }
        r9 = r3.getScrollY();	 Catch:{ all -> 0x008f }
        r9 = (float) r9;	 Catch:{ all -> 0x008f }
        r8 = r8 - r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
    L_0x002e:
        r7 = 0;
        r7 = r2[r7];	 Catch:{ all -> 0x008f }
        r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1));
        if (r7 < 0) goto L_0x0054;
    L_0x0035:
        r7 = 1;
        r7 = r2[r7];	 Catch:{ all -> 0x008f }
        r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1));
        if (r7 < 0) goto L_0x0054;
    L_0x003c:
        r7 = 0;
        r7 = r2[r7];	 Catch:{ all -> 0x008f }
        r8 = r3.getWidth();	 Catch:{ all -> 0x008f }
        r8 = (float) r8;	 Catch:{ all -> 0x008f }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 > 0) goto L_0x0054;
    L_0x0048:
        r7 = 1;
        r7 = r2[r7];	 Catch:{ all -> 0x008f }
        r8 = r3.getHeight();	 Catch:{ all -> 0x008f }
        r8 = (float) r8;	 Catch:{ all -> 0x008f }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 <= 0) goto L_0x0056;
    L_0x0054:
        monitor-exit(r6);	 Catch:{ all -> 0x008f }
    L_0x0055:
        return r4;
    L_0x0056:
        r7 = r3.getMatrix();	 Catch:{ all -> 0x008f }
        r7 = r7.isIdentity();	 Catch:{ all -> 0x008f }
        if (r7 != 0) goto L_0x0067;
    L_0x0060:
        r7 = r3.getMatrix();	 Catch:{ all -> 0x008f }
        r7.mapPoints(r2);	 Catch:{ all -> 0x008f }
    L_0x0067:
        r7 = 0;
        r8 = r2[r7];	 Catch:{ all -> 0x008f }
        r9 = r3.getLeft();	 Catch:{ all -> 0x008f }
        r9 = (float) r9;	 Catch:{ all -> 0x008f }
        r8 = r8 + r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
        r7 = 1;
        r8 = r2[r7];	 Catch:{ all -> 0x008f }
        r9 = r3.getTop();	 Catch:{ all -> 0x008f }
        r9 = (float) r9;	 Catch:{ all -> 0x008f }
        r8 = r8 + r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008f }
        r1 = r3.getParent();	 Catch:{ all -> 0x008f }
        r7 = r1 instanceof android.view.View;	 Catch:{ all -> 0x008f }
        if (r7 == 0) goto L_0x008a;
    L_0x0085:
        r0 = r1;
        r0 = (android.view.View) r0;	 Catch:{ all -> 0x008f }
        r3 = r0;
        goto L_0x0012;
    L_0x008a:
        r3 = 0;
        goto L_0x0012;
    L_0x008c:
        monitor-exit(r6);	 Catch:{ all -> 0x008f }
        r4 = r5;
        goto L_0x0055;
    L_0x008f:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x008f }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.MultiSelectPopupWindow.isPositionVisible(int, int):boolean");
    }

    public void relocateMultiSelectQuickPopupWindow() {
        if (getMultiSelectController() != null) {
            getMultiSelectController().relocateMultiSelectQuickPopupWindow();
        }
    }

    private boolean isTablet() {
        if ("short".equals(SystemProperties.get("ro.build.scafe.size"))) {
            return false;
        }
        return true;
    }
}
