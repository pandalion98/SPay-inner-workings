package android.widget;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.UndoOperation;
import android.content.UndoOwner;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.provider.UserDictionary.Words;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.ParcelableSpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.WordIterator;
import android.text.style.EasyEditSpan;
import android.text.style.ReplacementSpan;
import android.text.style.SuggestionRangeSpan;
import android.text.style.SuggestionSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ActionMode.Callback2;
import android.view.DisplayListCanvas;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.RenderNode;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.CursorAnchorInfo.Builder;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import com.android.internal.R;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.AutoScrollHelper;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.writingbuddy.WritingBuddyImpl;
import com.sec.android.app.CscFeature;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Editor {
    static final int BLINK = 500;
    private static final boolean DEBUG_UNDO = false;
    private static int DRAG_SHADOW_MAX_TEXT_LENGTH = 20;
    private static final int ERROR_MESSAGE_LAYOUT = 17367312;
    static final int EXTRACT_NOTHING = -2;
    static final int EXTRACT_UNKNOWN = -1;
    private static final float LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS = 0.5f;
    private static final int MENU_ITEM_ORDER_COPY = 2;
    private static final int MENU_ITEM_ORDER_CUT = 1;
    private static final int MENU_ITEM_ORDER_PASTE = 3;
    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 10;
    private static final int MENU_ITEM_ORDER_REPLACE = 6;
    private static final int MENU_ITEM_ORDER_SELECT_ALL = 5;
    private static final int MENU_ITEM_ORDER_SHARE = 4;
    private static final String SWITCH_CONTROL_ENABLED = "universal_switch_enabled";
    private static final String TAG = "Editor";
    private static final float[] TEMP_POSITION = new float[2];
    private static final int TW_ERROR_MESSAGE_LAYOUT = 17367384;
    private static final int TW_MENU_ITEM_ORDER_CLIPBOARD = 6;
    private static final int TW_MENU_ITEM_ORDER_CLOSE = 0;
    private static final int TW_MENU_ITEM_ORDER_COPY = 3;
    private static final int TW_MENU_ITEM_ORDER_CUT = 2;
    private static final int TW_MENU_ITEM_ORDER_DICTIONARY = 7;
    private static final int TW_MENU_ITEM_ORDER_PASTE = 4;
    private static final int TW_MENU_ITEM_ORDER_SELECT_ALL = 1;
    private static final int TW_MENU_ITEM_ORDER_SHARE = 5;
    private static final int TW_MENU_ITEM_ORDER_WEBSEARCH = 8;
    private static final String UNDO_OWNER_TAG = "Editor";
    private static final int UNSET_LINE = -1;
    private static final int UNSET_X_VALUE = -1;
    private static final boolean mDisableDoubleTapTextSelection = CscFeature.getInstance().getEnableStatus("CscFeature_Framework_DisableDoubleTapTextSelection", false);
    private boolean isSecClipboardEnabled = false;
    boolean mAllowUndo = true;
    Blink mBlink;
    CorrectionHighlighter mCorrectionHighlighter;
    boolean mCreatedWithASelection;
    final CursorAnchorInfoNotifier mCursorAnchorInfoNotifier = new CursorAnchorInfoNotifier();
    int mCursorCount;
    final Drawable[] mCursorDrawable = new Drawable[2];
    private int mCursorShiftOffset = 0;
    boolean mCursorVisible = true;
    Callback mCustomInsertionActionModeCallback;
    Callback mCustomSelectionActionModeCallback;
    boolean mDiscardNextActionUp;
    boolean mDoubleTap = false;
    CharSequence mError;
    ErrorPopup mErrorPopup;
    boolean mErrorWasChanged;
    boolean mFrozenWithFocus;
    boolean mIgnoreActionUpEvent;
    boolean mInBatchEditControllers;
    InputContentType mInputContentType;
    InputMethodState mInputMethodState;
    int mInputType = 0;
    private int mInsertActionHandleTopMargin;
    private Runnable mInsertionActionModeRunnable;
    boolean mInsertionControllerEnabled;
    InsertionPointCursorController mInsertionPointCursorController;
    boolean mIsInsertionActionModeStartPending = false;
    private boolean mIsSuggestionsPopup = false;
    KeyListener mKeyListener;
    float mLastDownPositionX;
    float mLastDownPositionY;
    private int mPopupBelowMarginDifSelectAndInsert;
    private PositionListener mPositionListener;
    boolean mPreserveDetachedSelection;
    final ProcessTextIntentActionsHandler mProcessTextIntentActionsHandler;
    boolean mSelectAllOnFocus;
    private Drawable mSelectHandleCenter;
    private Drawable mSelectHandleLeft;
    private Drawable mSelectHandleRight;
    boolean mSelectionActionMode = false;
    boolean mSelectionControllerEnabled;
    SelectionModifierCursorController mSelectionModifierCursorController;
    boolean mSelectionMoved;
    long mShowCursor;
    boolean mShowErrorAfterAttach;
    private final Runnable mShowFloatingToolbar = new Runnable() {
        public void run() {
            if (Editor.this.mTextActionMode != null) {
                Editor.this.mTextActionMode.hide(0);
            }
        }
    };
    boolean mShowSoftInputOnFocus = true;
    Runnable mShowSuggestionRunnable;
    private SpanController mSpanController;
    SpellChecker mSpellChecker;
    SuggestionRangeSpan mSuggestionRangeSpan;
    SuggestionsPopupWindow mSuggestionsPopupWindow;
    private Rect mTempRect;
    boolean mTemporaryDetach;
    ActionMode mTextActionMode;
    boolean mTextIsSelectable;
    TextRenderNode[] mTextRenderNodes;
    private TextView mTextView;
    private boolean mThemeIsDeviceDefault = false;
    boolean mTouchFocusSelected;
    final UndoInputFilter mUndoInputFilter = new UndoInputFilter(this);
    private final UndoManager mUndoManager = new UndoManager();
    private UndoOwner mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    private boolean mUpdateWordIteratorText;
    private TextViewPositionListener mWBPositionListener = null;
    WordIterator mWordIterator;
    private WordIterator mWordIteratorWithText;

    private interface TextViewPositionListener {
        void updatePosition(int i, int i2, boolean z, boolean z2);
    }

    private class Blink extends Handler implements Runnable {
        private boolean mCancelled;

        private Blink() {
        }

        public void run() {
            if (!this.mCancelled) {
                removeCallbacks(this);
                if (Editor.this.shouldBlink()) {
                    if (Editor.this.mTextView.getLayout() != null) {
                        Editor.this.mTextView.invalidateCursorPath();
                    }
                    postAtTime(this, SystemClock.uptimeMillis() + 500);
                }
            }
        }

        void cancel() {
            if (!this.mCancelled) {
                removeCallbacks(this);
                this.mCancelled = true;
            }
        }

        void uncancel() {
            this.mCancelled = false;
        }
    }

    private class CorrectionHighlighter {
        private static final int FADE_OUT_DURATION = 400;
        private int mEnd;
        private long mFadingStartTime;
        private final Paint mPaint = new Paint(1);
        private final Path mPath = new Path();
        private int mStart;
        private RectF mTempRectF;

        public CorrectionHighlighter() {
            this.mPaint.setCompatibilityScaling(Editor.this.mTextView.getResources().getCompatibilityInfo().applicationScale);
            this.mPaint.setStyle(Style.FILL);
        }

        public void highlight(CorrectionInfo info) {
            this.mStart = info.getOffset();
            this.mEnd = this.mStart + info.getNewText().length();
            this.mFadingStartTime = SystemClock.uptimeMillis();
            if (this.mStart < 0 || this.mEnd < 0) {
                stopAnimation();
            }
        }

        public void draw(Canvas canvas, int cursorOffsetVertical) {
            if (updatePath() && updatePaint()) {
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, (float) cursorOffsetVertical);
                }
                canvas.drawPath(this.mPath, this.mPaint);
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, (float) (-cursorOffsetVertical));
                }
                invalidate(true);
                return;
            }
            stopAnimation();
            invalidate(false);
        }

        private boolean updatePaint() {
            long duration = SystemClock.uptimeMillis() - this.mFadingStartTime;
            if (duration > 400) {
                return false;
            }
            this.mPaint.setColor((Editor.this.mTextView.mHighlightColor & 16777215) + (((int) (((float) Color.alpha(Editor.this.mTextView.mHighlightColor)) * (1.0f - (((float) duration) / 400.0f)))) << 24));
            return true;
        }

        private boolean updatePath() {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                return false;
            }
            int length = Editor.this.mTextView.getText().length();
            int start = Math.min(length, this.mStart);
            int end = Math.min(length, this.mEnd);
            this.mPath.reset();
            layout.getSelectionPath(start, end, this.mPath);
            return true;
        }

        private void invalidate(boolean delayed) {
            if (Editor.this.mTextView.getLayout() != null) {
                if (this.mTempRectF == null) {
                    this.mTempRectF = new RectF();
                }
                this.mPath.computeBounds(this.mTempRectF, false);
                int left = Editor.this.mTextView.getCompoundPaddingLeft();
                int top = Editor.this.mTextView.getExtendedPaddingTop() + Editor.this.mTextView.getVerticalOffset(true);
                if (delayed) {
                    Editor.this.mTextView.postInvalidateOnAnimation(((int) this.mTempRectF.left) + left, ((int) this.mTempRectF.top) + top, ((int) this.mTempRectF.right) + left, ((int) this.mTempRectF.bottom) + top);
                } else {
                    Editor.this.mTextView.postInvalidate((int) this.mTempRectF.left, (int) this.mTempRectF.top, (int) this.mTempRectF.right, (int) this.mTempRectF.bottom);
                }
            }
        }

        private void stopAnimation() {
            Editor.this.mCorrectionHighlighter = null;
        }
    }

    private final class CursorAnchorInfoNotifier implements TextViewPositionListener {
        final Builder mSelectionInfoBuilder;
        final int[] mTmpIntOffset;
        final Matrix mViewToScreenMatrix;

        private CursorAnchorInfoNotifier() {
            this.mSelectionInfoBuilder = new Builder();
            this.mTmpIntOffset = new int[2];
            this.mViewToScreenMatrix = new Matrix();
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            InputMethodState ims = Editor.this.mInputMethodState;
            if (ims != null && ims.mBatchEditNesting <= 0) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                if (imm != null) {
                    if (imm.isActive(Editor.this.mTextView) && imm.isCursorAnchorInfoEnabled()) {
                        Layout layout = Editor.this.mTextView.getLayout();
                        if (layout != null) {
                            int line;
                            int offset;
                            Builder builder = this.mSelectionInfoBuilder;
                            builder.reset();
                            int selectionStart = Editor.this.mTextView.getSelectionStart();
                            builder.setSelectionRange(selectionStart, Editor.this.mTextView.getSelectionEnd());
                            this.mViewToScreenMatrix.set(Editor.this.mTextView.getMatrix());
                            Editor.this.mTextView.getLocationOnScreen(this.mTmpIntOffset);
                            this.mViewToScreenMatrix.postTranslate((float) this.mTmpIntOffset[0], (float) this.mTmpIntOffset[1]);
                            builder.setMatrix(this.mViewToScreenMatrix);
                            float viewportToContentHorizontalOffset = (float) Editor.this.mTextView.viewportToContentHorizontalOffset();
                            float viewportToContentVerticalOffset = (float) Editor.this.mTextView.viewportToContentVerticalOffset();
                            CharSequence text = Editor.this.mTextView.getText();
                            if (text instanceof Spannable) {
                                Spannable sp = (Spannable) text;
                                int composingTextStart = BaseInputConnection.getComposingSpanStart(sp);
                                int composingTextEnd = BaseInputConnection.getComposingSpanEnd(sp);
                                if (composingTextEnd < composingTextStart) {
                                    int temp = composingTextEnd;
                                    composingTextEnd = composingTextStart;
                                    composingTextStart = temp;
                                }
                                boolean hasComposingText = composingTextStart >= 0 && composingTextStart < composingTextEnd;
                                if (hasComposingText) {
                                    builder.setComposingText(composingTextStart, text.subSequence(composingTextStart, composingTextEnd));
                                    int minLine = layout.getLineForOffset(composingTextStart);
                                    int maxLine = layout.getLineForOffset(composingTextEnd - 1);
                                    for (line = minLine; line <= maxLine; line++) {
                                        int lineStart = layout.getLineStart(line);
                                        int lineEnd = layout.getLineEnd(line);
                                        int offsetStart = Math.max(lineStart, composingTextStart);
                                        int offsetEnd = Math.min(lineEnd, composingTextEnd);
                                        boolean ltrLine = layout.getParagraphDirection(line) == 1;
                                        float[] widths = new float[(offsetEnd - offsetStart)];
                                        layout.getPaint().getTextWidths(text, offsetStart, offsetEnd, widths);
                                        float top = (float) layout.getLineTop(line);
                                        float bottom = (float) layout.getLineBottom(line);
                                        for (offset = offsetStart; offset < offsetEnd; offset++) {
                                            float left;
                                            float right;
                                            float charWidth = widths[offset - offsetStart];
                                            boolean isRtl = layout.isRtlCharAt(offset);
                                            float primary = layout.getPrimaryHorizontal(offset);
                                            float secondary = layout.getSecondaryHorizontal(offset);
                                            if (ltrLine) {
                                                if (isRtl) {
                                                    left = secondary - charWidth;
                                                    right = secondary;
                                                } else {
                                                    left = primary;
                                                    right = primary + charWidth;
                                                }
                                            } else if (isRtl) {
                                                left = primary - charWidth;
                                                right = primary;
                                            } else {
                                                left = secondary;
                                                right = secondary + charWidth;
                                            }
                                            float localLeft = left + viewportToContentHorizontalOffset;
                                            float localRight = right + viewportToContentHorizontalOffset;
                                            float localTop = top + viewportToContentVerticalOffset;
                                            float localBottom = bottom + viewportToContentVerticalOffset;
                                            boolean isTopLeftVisible = Editor.this.isPositionVisible(localLeft, localTop);
                                            boolean isBottomRightVisible = Editor.this.isPositionVisible(localRight, localBottom);
                                            int characterBoundsFlags = 0;
                                            if (isTopLeftVisible || isBottomRightVisible) {
                                                characterBoundsFlags = 0 | 1;
                                            }
                                            if (!(isTopLeftVisible && isBottomRightVisible)) {
                                                characterBoundsFlags |= 2;
                                            }
                                            if (isRtl) {
                                                characterBoundsFlags |= 4;
                                            }
                                            builder.addCharacterBounds(offset, localLeft, localTop, localRight, localBottom, characterBoundsFlags);
                                        }
                                    }
                                }
                            }
                            if (selectionStart >= 0) {
                                offset = selectionStart;
                                line = layout.getLineForOffset(offset);
                                float insertionMarkerX = layout.getPrimaryHorizontal(offset) + viewportToContentHorizontalOffset;
                                float insertionMarkerTop = ((float) layout.getLineTop(line)) + viewportToContentVerticalOffset;
                                float insertionMarkerBaseline = ((float) layout.getLineBaseline(line)) + viewportToContentVerticalOffset;
                                float insertionMarkerBottom = ((float) layout.getLineBottom(line)) + viewportToContentVerticalOffset;
                                boolean isTopVisible = Editor.this.isPositionVisible(insertionMarkerX, insertionMarkerTop);
                                boolean isBottomVisible = Editor.this.isPositionVisible(insertionMarkerX, insertionMarkerBottom);
                                int insertionMarkerFlags = 0;
                                if (isTopVisible || isBottomVisible) {
                                    insertionMarkerFlags = 0 | 1;
                                }
                                if (!(isTopVisible && isBottomVisible)) {
                                    insertionMarkerFlags |= 2;
                                }
                                if (layout.isRtlCharAt(offset)) {
                                    insertionMarkerFlags |= 4;
                                }
                                builder.setInsertionMarkerLocation(insertionMarkerX, insertionMarkerTop, insertionMarkerBaseline, insertionMarkerBottom, insertionMarkerFlags);
                            }
                            imm.updateCursorAnchorInfo(Editor.this.mTextView, builder.build());
                        }
                    }
                }
            }
        }
    }

    private interface CursorController extends OnTouchModeChangeListener {
        void hide();

        void onDetached();

        void show();
    }

    private static class DragLocalState {
        public int end;
        public TextView sourceTextView;
        public int start;

        public DragLocalState(TextView sourceTextView, int start, int end) {
            this.sourceTextView = sourceTextView;
            this.start = start;
            this.end = end;
        }
    }

    private interface EasyEditDeleteListener {
        void onDeleteClick(EasyEditSpan easyEditSpan);
    }

    private abstract class PinnedPopupWindow implements TextViewPositionListener {
        protected ViewGroup mContentView;
        protected PopupWindow mPopupWindow;
        int mPositionX;
        int mPositionY;

        protected abstract int clipVertically(int i);

        protected abstract void createPopupWindow();

        protected abstract int getTextOffset();

        protected abstract int getVerticalLocalPosition(int i);

        protected abstract void initContentView();

        public PinnedPopupWindow() {
            createPopupWindow();
            this.mPopupWindow.setWindowLayoutType(1005);
            this.mPopupWindow.setWidth(-2);
            this.mPopupWindow.setHeight(-2);
            initContentView();
            this.mContentView.setLayoutParams(new LayoutParams(-2, -2));
            this.mPopupWindow.setContentView(this.mContentView);
        }

        public void show() {
            Editor.this.getPositionListener().addSubscriber(this, false);
            computeLocalPosition();
            PositionListener positionListener = Editor.this.getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
        }

        protected void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            this.mContentView.measure(MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        private void computeLocalPosition() {
            measureContent();
            int width = this.mContentView.getMeasuredWidth();
            int offset = getTextOffset();
            this.mPositionX = (int) (Editor.this.mTextView.getLayout().getPrimaryHorizontal(offset) - (((float) width) / 2.0f));
            this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
            int line = Editor.this.mTextView.getLayout().getLineForOffset(offset);
            int suggestionsPopupYoffset = (int) Editor.this.mTextView.getResources().getDimension(R.dimen.tw_editor_suggestions_popupwindow_vertical_padding);
            if (Editor.this.mIsSuggestionsPopup) {
                this.mPositionY = getVerticalLocalPosition(line) + suggestionsPopupYoffset;
                Editor.this.mIsSuggestionsPopup = false;
            } else {
                this.mPositionY = getVerticalLocalPosition(line);
            }
            this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
        }

        private void updatePosition(int parentPositionX, int parentPositionY) {
            int positionX = parentPositionX + this.mPositionX;
            int positionY = clipVertically(parentPositionY + this.mPositionY);
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            positionX = Math.max(0, Math.min(displayMetrics.widthPixels - this.mContentView.getMeasuredWidth(), positionX));
            if (isShowing()) {
                this.mPopupWindow.update(positionX, positionY, -1, -1);
            } else {
                this.mPopupWindow.showAtLocation(Editor.this.mTextView, 0, positionX, positionY);
            }
        }

        public void hide() {
            this.mPopupWindow.dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (isShowing() && Editor.this.isOffsetVisible(getTextOffset())) {
                if (parentScrolled) {
                    computeLocalPosition();
                }
                updatePosition(parentPositionX, parentPositionY);
                return;
            }
            hide();
        }

        public boolean isShowing() {
            return this.mPopupWindow.isShowing();
        }
    }

    private class EasyEditPopupWindow extends PinnedPopupWindow implements OnClickListener {
        private static final int POPUP_TEXT_LAYOUT = 17367305;
        private TextView mDeleteTextView;
        private EasyEditSpan mEasyEditSpan;
        private EasyEditDeleteListener mOnDeleteListener;

        private EasyEditPopupWindow() {
            super();
        }

        protected void createPopupWindow() {
            this.mPopupWindow = new PopupWindow(Editor.this.mTextView.getContext(), null, (int) R.attr.textSelectHandleWindowStyle);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setClippingEnabled(true);
        }

        protected void initContentView() {
            LinearLayout linearLayout = new LinearLayout(Editor.this.mTextView.getContext());
            linearLayout.setOrientation(0);
            this.mContentView = linearLayout;
            this.mContentView.setBackgroundResource(R.drawable.text_edit_side_paste_window);
            LayoutInflater inflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService("layout_inflater");
            LayoutParams wrapContent = new LayoutParams(-2, -2);
            this.mDeleteTextView = (TextView) inflater.inflate(17367305, null);
            this.mDeleteTextView.setLayoutParams(wrapContent);
            this.mDeleteTextView.setText((int) R.string.delete);
            this.mDeleteTextView.setOnClickListener(this);
            this.mContentView.addView(this.mDeleteTextView);
        }

        public void setEasyEditSpan(EasyEditSpan easyEditSpan) {
            this.mEasyEditSpan = easyEditSpan;
        }

        private void setOnDeleteListener(EasyEditDeleteListener listener) {
            this.mOnDeleteListener = listener;
        }

        public void onClick(View view) {
            if (view == this.mDeleteTextView && this.mEasyEditSpan != null && this.mEasyEditSpan.isDeleteEnabled() && this.mOnDeleteListener != null) {
                this.mOnDeleteListener.onDeleteClick(this.mEasyEditSpan);
            }
        }

        public void hide() {
            if (this.mEasyEditSpan != null) {
                this.mEasyEditSpan.setDeleteEnabled(false);
            }
            this.mOnDeleteListener = null;
            super.hide();
        }

        protected int getTextOffset() {
            return ((Editable) Editor.this.mTextView.getText()).getSpanEnd(this.mEasyEditSpan);
        }

        protected int getVerticalLocalPosition(int line) {
            return Editor.this.mTextView.getLayout().getLineBottom(line);
        }

        protected int clipVertically(int positionY) {
            return positionY;
        }
    }

    public static class EditOperation extends UndoOperation<Editor> {
        public static final ClassLoaderCreator<EditOperation> CREATOR = new ClassLoaderCreator<EditOperation>() {
            public EditOperation createFromParcel(Parcel in) {
                return new EditOperation(in, null);
            }

            public EditOperation createFromParcel(Parcel in, ClassLoader loader) {
                return new EditOperation(in, loader);
            }

            public EditOperation[] newArray(int size) {
                return new EditOperation[size];
            }
        };
        private static final int TYPE_DELETE = 1;
        private static final int TYPE_INSERT = 0;
        private static final int TYPE_REPLACE = 2;
        private int mNewCursorPos;
        private String mNewText;
        private int mNewTextStart;
        private int mOldCursorPos;
        private String mOldText;
        private int mOldTextStart;
        private int mType;

        public EditOperation(Editor editor, String oldText, int dstart, String newText) {
            super(editor.mUndoOwner);
            this.mOldText = oldText;
            this.mNewText = newText;
            if (this.mNewText.length() > 0 && this.mOldText.length() == 0) {
                this.mType = 0;
                this.mNewTextStart = dstart;
            } else if (this.mNewText.length() != 0 || this.mOldText.length() <= 0) {
                this.mType = 2;
                this.mNewTextStart = dstart;
                this.mOldTextStart = dstart;
            } else {
                this.mType = 1;
                this.mOldTextStart = dstart;
            }
            this.mOldCursorPos = editor.mTextView.getSelectionStart();
            this.mNewCursorPos = this.mNewText.length() + dstart;
        }

        public EditOperation(Parcel src, ClassLoader loader) {
            super(src, loader);
            this.mType = src.readInt();
            this.mOldText = src.readString();
            this.mOldTextStart = src.readInt();
            this.mNewText = src.readString();
            this.mNewTextStart = src.readInt();
            this.mOldCursorPos = src.readInt();
            this.mNewCursorPos = src.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mType);
            dest.writeString(this.mOldText);
            dest.writeInt(this.mOldTextStart);
            dest.writeString(this.mNewText);
            dest.writeInt(this.mNewTextStart);
            dest.writeInt(this.mOldCursorPos);
            dest.writeInt(this.mNewCursorPos);
        }

        private int getNewTextEnd() {
            return this.mNewTextStart + this.mNewText.length();
        }

        private int getOldTextEnd() {
            return this.mOldTextStart + this.mOldText.length();
        }

        public void commit() {
        }

        public void undo() {
            modifyText((Editable) ((Editor) getOwnerData()).mTextView.getText(), this.mNewTextStart, getNewTextEnd(), this.mOldText, this.mOldTextStart, this.mOldCursorPos);
        }

        public void redo() {
            modifyText((Editable) ((Editor) getOwnerData()).mTextView.getText(), this.mOldTextStart, getOldTextEnd(), this.mNewText, this.mNewTextStart, this.mNewCursorPos);
        }

        private boolean mergeWith(EditOperation edit) {
            switch (this.mType) {
                case 0:
                    return mergeInsertWith(edit);
                case 1:
                    return mergeDeleteWith(edit);
                case 2:
                    return mergeReplaceWith(edit);
                default:
                    return false;
            }
        }

        private boolean mergeInsertWith(EditOperation edit) {
            if (edit.mType != 0 || getNewTextEnd() != edit.mNewTextStart) {
                return false;
            }
            this.mNewText += edit.mNewText;
            this.mNewCursorPos = edit.mNewCursorPos;
            return true;
        }

        private boolean mergeDeleteWith(EditOperation edit) {
            if (edit.mType != 1 || this.mOldTextStart != edit.getOldTextEnd()) {
                return false;
            }
            this.mOldTextStart = edit.mOldTextStart;
            this.mOldText = edit.mOldText + this.mOldText;
            this.mNewCursorPos = edit.mNewCursorPos;
            return true;
        }

        private boolean mergeReplaceWith(EditOperation edit) {
            if (edit.mType != 0 || getNewTextEnd() != edit.mNewTextStart) {
                return false;
            }
            this.mOldText += edit.mOldText;
            this.mNewText += edit.mNewText;
            this.mNewCursorPos = edit.mNewCursorPos;
            return true;
        }

        public void forceMergeWith(EditOperation edit) {
            Editable editable = (Editable) ((Editor) getOwnerData()).mTextView.getText();
            Editable originalText = new SpannableStringBuilder(editable.toString());
            modifyText(originalText, this.mNewTextStart, getNewTextEnd(), this.mOldText, this.mOldTextStart, this.mOldCursorPos);
            Editable finalText = new SpannableStringBuilder(editable.toString());
            modifyText(finalText, edit.mOldTextStart, edit.getOldTextEnd(), edit.mNewText, edit.mNewTextStart, edit.mNewCursorPos);
            this.mType = 2;
            this.mNewText = finalText.toString();
            this.mNewTextStart = 0;
            this.mOldText = originalText.toString();
            this.mOldTextStart = 0;
            this.mNewCursorPos = edit.mNewCursorPos;
        }

        private static void modifyText(Editable text, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
            if (Editor.isValidRange(text, deleteFrom, deleteTo) && newTextInsertAt <= text.length() - (deleteTo - deleteFrom)) {
                if (deleteFrom != deleteTo) {
                    text.delete(deleteFrom, deleteTo);
                }
                if (newText.length() != 0) {
                    text.insert(newTextInsertAt, newText);
                }
            }
            if (newCursorPos >= 0 && newCursorPos <= text.length()) {
                Selection.setSelection(text, newCursorPos);
            }
        }

        private String getTypeString() {
            switch (this.mType) {
                case 0:
                    return "insert";
                case 1:
                    return "delete";
                case 2:
                    return "replace";
                default:
                    return "";
            }
        }

        public String toString() {
            return "[mType=" + getTypeString() + ", " + "mOldText=" + this.mOldText + ", " + "mOldTextStart=" + this.mOldTextStart + ", " + "mNewText=" + this.mNewText + ", " + "mNewTextStart=" + this.mNewTextStart + ", " + "mOldCursorPos=" + this.mOldCursorPos + ", " + "mNewCursorPos=" + this.mNewCursorPos + "]";
        }
    }

    private static class ErrorPopup extends PopupWindow {
        private boolean mAbove = false;
        private int mPopupInlineErrorAboveBackgroundId = 0;
        private int mPopupInlineErrorBackgroundId = 0;
        private final TextView mView;

        ErrorPopup(TextView v, int width, int height) {
            super((View) v, width, height);
            this.mView = v;
            this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 275);
            this.mView.setBackgroundResource(this.mPopupInlineErrorBackgroundId);
        }

        void fixDirection(boolean above) {
            this.mAbove = above;
            if (above) {
                this.mPopupInlineErrorAboveBackgroundId = getResourceId(this.mPopupInlineErrorAboveBackgroundId, R.styleable.Theme_errorMessageAboveBackground);
            } else {
                this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 275);
            }
            this.mView.setBackgroundResource(above ? this.mPopupInlineErrorAboveBackgroundId : this.mPopupInlineErrorBackgroundId);
        }

        private int getResourceId(int currentId, int index) {
            if (currentId != 0) {
                return currentId;
            }
            TypedArray styledAttributes = this.mView.getContext().obtainStyledAttributes(android.R.styleable.Theme);
            currentId = styledAttributes.getResourceId(index, 0);
            styledAttributes.recycle();
            return currentId;
        }

        public void update(int x, int y, int w, int h, boolean force) {
            super.update(x, y, w, h, force);
            boolean above = isAboveAnchor();
            if (above != this.mAbove) {
                fixDirection(above);
            }
        }
    }

    private abstract class HandleView extends View implements TextViewPositionListener {
        static final int HANDLE_TYPE_END = 2;
        static final int HANDLE_TYPE_INSERT = 3;
        static final int HANDLE_TYPE_NONE = 0;
        static final int HANDLE_TYPE_START = 1;
        private static final int HISTORY_SIZE = 5;
        private static final int TOUCH_UP_FILTER_DELAY_AFTER = 150;
        private static final int TOUCH_UP_FILTER_DELAY_BEFORE = 350;
        private final PopupWindow mContainer;
        protected Drawable mDrawable;
        protected Drawable mDrawableLtr;
        protected Drawable mDrawableRtl;
        public int mHandleType = 0;
        private boolean mHandlerHasMoved = false;
        private int[] mHighlightRect = new int[2];
        protected int mHorizontalGravity;
        protected int mHotspotX;
        private float mIdealVerticalOffset;
        private boolean mIsDragging;
        private int mLastParentX;
        private int mLastParentY;
        private int mMinSize;
        private int mNumberPreviousOffsets = 0;
        private boolean mPositionHasChanged = true;
        protected int mPositionX;
        protected int mPositionY;
        protected int mPrevLine = -1;
        private int[] mPreviousHighlightRect = new int[2];
        protected int mPreviousLineTouched = -1;
        protected int mPreviousOffset = -1;
        private int mPreviousOffsetIndex = 0;
        private final int[] mPreviousOffsets = new int[5];
        private final long[] mPreviousOffsetsTimes = new long[5];
        private float mTouchOffsetY;
        private float mTouchToWindowOffsetX;
        private float mTouchToWindowOffsetY;
        protected boolean mbSwitchCursor;

        public abstract int getCurrentCursorOffset();

        protected abstract int getHorizontalGravity(boolean z);

        protected abstract int getHotspotX(Drawable drawable, boolean z);

        public abstract void updatePosition(float f, float f2);

        protected abstract void updateSelection(int i);

        public HandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(Editor.this.mTextView.getContext());
            this.mContainer = new PopupWindow(Editor.this.mTextView.getContext(), null, (int) R.attr.textSelectHandleWindowStyle);
            this.mContainer.setSplitTouchEnabled(true);
            this.mContainer.setClippingEnabled(false);
            this.mContainer.setWindowLayoutType(1002);
            this.mContainer.setWidth(-2);
            this.mContainer.setHeight(-2);
            this.mContainer.setContentView(this);
            this.mDrawableLtr = drawableLtr;
            this.mDrawableRtl = drawableRtl;
            if (Editor.this.mThemeIsDeviceDefault) {
                this.mMinSize = Editor.this.mTextView.getContext().getResources().getDimensionPixelSize(R.dimen.tw_edittext_field_handler_min_width);
            } else {
                this.mMinSize = Editor.this.mTextView.getContext().getResources().getDimensionPixelSize(R.dimen.text_handle_min_size);
            }
            updateDrawable();
            int handleHeight = getPreferredHeight();
            this.mTouchOffsetY = -0.3f * ((float) handleHeight);
            this.mIdealVerticalOffset = 0.7f * ((float) handleHeight);
        }

        public float getIdealVerticalOffset() {
            return this.mIdealVerticalOffset;
        }

        protected boolean calculateForSwitchingCursor() {
            return true;
        }

        public boolean refreshForSwitchingCursor() {
            return true;
        }

        protected void updateDrawable() {
            if (!this.mIsDragging) {
                Layout layout = Editor.this.mTextView.getLayout();
                if (layout != null) {
                    int offset = getCurrentCursorOffset();
                    boolean isRtlCharAtOffset = layout.isRtlCharAt(offset);
                    Drawable oldDrawable = this.mDrawable;
                    if (this.mbSwitchCursor) {
                        isRtlCharAtOffset = !isRtlCharAtOffset;
                    }
                    this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
                    this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
                    this.mHorizontalGravity = getHorizontalGravity(isRtlCharAtOffset);
                    if (oldDrawable != this.mDrawable && isShowing()) {
                        this.mPositionX = (int) ((((layout.getPrimaryHorizontal(offset) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset())) + ((float) getCursorOffset()));
                        this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                        this.mPositionHasChanged = true;
                        updatePosition(this.mLastParentX, this.mLastParentY, false, false);
                        postInvalidate();
                    }
                }
            }
        }

        private void startTouchUpFilter(int offset) {
            this.mNumberPreviousOffsets = 0;
            addPositionToTouchUpFilter(offset);
        }

        private void addPositionToTouchUpFilter(int offset) {
            this.mPreviousOffsetIndex = (this.mPreviousOffsetIndex + 1) % 5;
            this.mPreviousOffsets[this.mPreviousOffsetIndex] = offset;
            this.mPreviousOffsetsTimes[this.mPreviousOffsetIndex] = SystemClock.uptimeMillis();
            this.mNumberPreviousOffsets++;
        }

        private void filterOnTouchUp() {
            long now = SystemClock.uptimeMillis();
            int i = 0;
            int index = this.mPreviousOffsetIndex;
            int iMax = Math.min(this.mNumberPreviousOffsets, 5);
            while (i < iMax && now - this.mPreviousOffsetsTimes[index] < 150) {
                i++;
                index = ((this.mPreviousOffsetIndex - i) + 5) % 5;
            }
            if (i > 0 && i < iMax && now - this.mPreviousOffsetsTimes[index] > 350) {
                positionAtCursorOffset(this.mPreviousOffsets[index], false);
            }
        }

        public boolean offsetHasBeenChanged() {
            return this.mNumberPreviousOffsets > 1;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getPreferredWidth(), getPreferredHeight());
        }

        protected int getPreferredWidth() {
            return Math.max(this.mDrawable.getIntrinsicWidth(), this.mMinSize);
        }

        private int getPreferredHeight() {
            return Math.max(this.mDrawable.getIntrinsicHeight(), this.mMinSize);
        }

        public void show() {
            if (!isShowing()) {
                Editor.this.getPositionListener().addSubscriber(this, true);
                this.mPreviousOffset = -1;
                positionAtCursorOffset(getCurrentCursorOffset(), false);
            }
        }

        protected void dismiss() {
            this.mIsDragging = false;
            this.mHandlerHasMoved = false;
            this.mContainer.dismiss();
            onDetached();
        }

        public void hide() {
            dismiss();
            Editor.this.removeMessage();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        public boolean isShowing() {
            return this.mContainer.isShowing();
        }

        private boolean isVisible() {
            if (this.mIsDragging) {
                return true;
            }
            if (Editor.this.mTextView.isInBatchEditMode()) {
                return false;
            }
            int mCalculatePositionY = this.mPositionY;
            if (this.mHandleType == 3) {
                mCalculatePositionY -= Editor.this.mInsertActionHandleTopMargin;
            }
            return Editor.this.isPositionVisible((float) ((this.mPositionX + this.mHotspotX) + getHorizontalOffset()), (float) mCalculatePositionY);
        }

        protected void positionAtCursorOffset(int offset, boolean parentScrolled) {
            if (Editor.this.mTextView.getLayout() == null) {
                Editor.this.prepareCursorControllers();
                return;
            }
            boolean offsetChanged;
            Layout layout = Editor.this.getActiveLayout();
            int line = layout.getLineForOffset(offset);
            if (offset != this.mPreviousOffset) {
                offsetChanged = true;
            } else {
                offsetChanged = false;
            }
            if (!offsetChanged) {
                this.mHighlightRect[0] = (int) layout.getPrimaryHorizontal(offset);
                this.mHighlightRect[1] = layout.getLineBottom(line);
                if (!(this.mHighlightRect[0] == this.mPreviousHighlightRect[0] && this.mHighlightRect[1] == this.mPreviousHighlightRect[1])) {
                    this.mPositionX = (int) ((((((float) this.mHighlightRect[0]) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset())) + ((float) getCursorOffset()));
                    this.mPositionY = this.mHighlightRect[1];
                    this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                    this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                }
                this.mPreviousHighlightRect[0] = this.mHighlightRect[0];
                this.mPreviousHighlightRect[1] = this.mHighlightRect[1];
                this.mPositionHasChanged = true;
            }
            if (offsetChanged || parentScrolled) {
                if (offsetChanged) {
                    updateSelection(offset);
                    addPositionToTouchUpFilter(offset);
                }
                this.mPrevLine = line;
                this.mPositionX = (int) ((((layout.getPrimaryHorizontal(offset) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset())) + ((float) getCursorOffset()));
                this.mPositionY = layout.getLineBottom(line);
                this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                if (this.mHandleType == 3) {
                    this.mPositionY += Editor.this.mInsertActionHandleTopMargin;
                }
                this.mPreviousOffset = offset;
                this.mPositionHasChanged = true;
            }
        }

        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            positionAtCursorOffset(getCurrentCursorOffset(), parentScrolled);
            if (parentPositionChanged || this.mPositionHasChanged) {
                if (this.mIsDragging) {
                    if (!(parentPositionX == this.mLastParentX && parentPositionY == this.mLastParentY)) {
                        this.mTouchToWindowOffsetX += (float) (parentPositionX - this.mLastParentX);
                        this.mTouchToWindowOffsetY += (float) (parentPositionY - this.mLastParentY);
                        this.mLastParentX = parentPositionX;
                        this.mLastParentY = parentPositionY;
                    }
                    onHandleMoved();
                    this.mHandlerHasMoved = true;
                } else if (!(parentPositionX == this.mLastParentX && parentPositionY == this.mLastParentY)) {
                    this.mLastParentX = parentPositionX;
                    this.mLastParentY = parentPositionY;
                }
                if (isVisible()) {
                    int positionX = parentPositionX + this.mPositionX;
                    int positionY = parentPositionY + this.mPositionY;
                    if (isShowing()) {
                        this.mContainer.update(positionX, positionY, -1, -1);
                    } else {
                        this.mContainer.showAtLocation(Editor.this.mTextView, 0, positionX, positionY);
                    }
                } else if (isShowing()) {
                    dismiss();
                }
                this.mPositionHasChanged = false;
            }
        }

        public void showAtLocation(int offset) {
            int[] tmpCords = new int[2];
            Editor.this.mTextView.getLocationInWindow(tmpCords);
            Layout layout = Editor.this.mTextView.getLayout();
            int posX = tmpCords[0];
            int posY = tmpCords[1];
            int startX = (int) ((((layout.getPrimaryHorizontal(offset) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset())) + ((float) getCursorOffset()));
            this.mContainer.showAtLocation(Editor.this.mTextView, 0, (startX + Editor.this.mTextView.viewportToContentHorizontalOffset()) + posX, (layout.getLineBottom(layout.getLineForOffset(offset)) + Editor.this.mTextView.viewportToContentVerticalOffset()) + posY);
        }

        protected void onDraw(Canvas c) {
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            int left = getHorizontalOffset();
            this.mDrawable.setBounds(left, 0, left + drawWidth, this.mDrawable.getIntrinsicHeight());
            this.mDrawable.draw(c);
        }

        protected int getHorizontalOffset() {
            int width = getPreferredWidth();
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            switch (this.mHorizontalGravity) {
                case 3:
                    return 0;
                case 5:
                    return width - drawWidth;
                default:
                    return (width - drawWidth) / 2;
            }
        }

        protected int getCursorOffset() {
            return 0;
        }

        public boolean onTouchEvent(MotionEvent ev) {
            Editor.this.updateFloatingToolbarVisibility(ev);
            switch (ev.getActionMasked()) {
                case 0:
                    startTouchUpFilter(getCurrentCursorOffset());
                    this.mTouchToWindowOffsetX = ev.getRawX() - ((float) this.mPositionX);
                    this.mTouchToWindowOffsetY = ev.getRawY() - ((float) this.mPositionY);
                    PositionListener positionListener = Editor.this.getPositionListener();
                    this.mLastParentX = positionListener.getPositionX();
                    this.mLastParentY = positionListener.getPositionY();
                    this.mIsDragging = true;
                    this.mPreviousLineTouched = -1;
                    break;
                case 1:
                    filterOnTouchUp();
                    this.mIsDragging = false;
                    if (Editor.this.mThemeIsDeviceDefault) {
                        refreshForSwitchingCursor();
                        if (Editor.this.mTextView.getSelectionStart() > Editor.this.mTextView.getSelectionEnd()) {
                            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd(), Editor.this.mTextView.getSelectionStart());
                        }
                    }
                    updateDrawable();
                    this.mHandlerHasMoved = false;
                    if (Editor.this.mSelectionModifierCursorController != null) {
                        Editor.this.mSelectionModifierCursorController.setDragAcceleratorActive(false);
                        break;
                    }
                    break;
                case 2:
                    float newVerticalOffset;
                    float rawX = ev.getRawX();
                    float rawY = ev.getRawY();
                    float previousVerticalOffset = this.mTouchToWindowOffsetY - ((float) this.mLastParentY);
                    float currentVerticalOffset = (rawY - ((float) this.mPositionY)) - ((float) this.mLastParentY);
                    if (previousVerticalOffset < this.mIdealVerticalOffset) {
                        newVerticalOffset = Math.max(Math.min(currentVerticalOffset, this.mIdealVerticalOffset), previousVerticalOffset);
                    } else {
                        newVerticalOffset = Math.min(Math.max(currentVerticalOffset, this.mIdealVerticalOffset), previousVerticalOffset);
                    }
                    this.mTouchToWindowOffsetY = ((float) this.mLastParentY) + newVerticalOffset;
                    updatePosition(((rawX - this.mTouchToWindowOffsetX) + ((float) this.mHotspotX)) + ((float) getHorizontalOffset()), (rawY - this.mTouchToWindowOffsetY) + this.mTouchOffsetY);
                    break;
                case 3:
                    this.mIsDragging = false;
                    updateDrawable();
                    this.mHandlerHasMoved = false;
                    break;
            }
            return true;
        }

        public boolean isDragging() {
            return this.mIsDragging;
        }

        public boolean isMoved() {
            return this.mHandlerHasMoved;
        }

        void onHandleMoved() {
            Editor.this.removeMessage();
        }

        public void onDetached() {
        }
    }

    static class InputContentType {
        boolean enterDown;
        Bundle extras;
        int imeActionId;
        CharSequence imeActionLabel;
        int imeOptions = 0;
        OnEditorActionListener onEditorActionListener;
        String privateImeOptions;

        InputContentType() {
        }
    }

    static class InputMethodState {
        int mBatchEditNesting;
        int mChangedDelta;
        int mChangedEnd;
        int mChangedStart;
        boolean mContentChanged;
        boolean mCursorChanged;
        final ExtractedText mExtractedText = new ExtractedText();
        ExtractedTextRequest mExtractedTextRequest;
        boolean mSelectionModeChanged;

        InputMethodState() {
        }
    }

    private class InsertionHandleView extends HandleView {
        private static final int DELAY_BEFORE_HANDLE_FADES_OUT = 4000;
        private static final int RECENT_CUT_COPY_DURATION = 15000;
        private float mDownPositionX;
        private float mDownPositionY;
        private Runnable mHider;

        public InsertionHandleView(Drawable drawable) {
            super(drawable, drawable);
            this.mHandleType = 3;
        }

        public void show() {
            super.show();
            long durationSinceCutOrCopy = SystemClock.uptimeMillis() - TextView.sLastCutCopyOrTextChangedTime;
            if (Editor.this.mInsertionActionModeRunnable != null && (Editor.this.mDoubleTap || Editor.this.isCursorInsideEasyCorrectionSpan())) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            if (!Editor.this.mDoubleTap && !Editor.this.isCursorInsideEasyCorrectionSpan() && durationSinceCutOrCopy < 15000 && Editor.this.mTextActionMode == null) {
                if (Editor.this.mInsertionActionModeRunnable == null) {
                    Editor.this.mInsertionActionModeRunnable = new Runnable() {
                        public void run() {
                            Editor.this.startInsertionActionMode();
                        }
                    };
                }
                Editor.this.mTextView.postDelayed(Editor.this.mInsertionActionModeRunnable, (long) (ViewConfiguration.getDoubleTapTimeout() + 1));
            }
            if (!Editor.this.mThemeIsDeviceDefault || Editor.this.mTextActionMode == null) {
                hideAfterDelay();
            } else {
                removeHiderCallback();
            }
        }

        private void hideAfterDelay() {
            if (this.mHider == null) {
                this.mHider = new Runnable() {
                    public void run() {
                        InsertionHandleView.this.hide();
                    }
                };
            } else {
                removeHiderCallback();
            }
            Editor.this.mTextView.postDelayed(this.mHider, 4000);
        }

        private void removeHiderCallback() {
            if (this.mHider != null) {
                Editor.this.mTextView.removeCallbacks(this.mHider);
            }
        }

        public void addHiderCallback() {
            if (this.mHider == null) {
                this.mHider = new Runnable() {
                    public void run() {
                        InsertionHandleView.this.hide();
                    }
                };
            } else {
                removeHiderCallback();
            }
            Editor.this.mTextView.postDelayed(this.mHider, 4000);
        }

        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            return drawable.getIntrinsicWidth() / 2;
        }

        protected int getHorizontalGravity(boolean isRtlRun) {
            return 1;
        }

        protected int getCursorOffset() {
            int offset = super.getCursorOffset();
            Drawable cursor = Editor.this.mCursorCount > 0 ? Editor.this.mCursorDrawable[0] : null;
            if (cursor == null) {
                return offset;
            }
            cursor.getPadding(Editor.this.mTempRect);
            return offset + (((cursor.getIntrinsicWidth() - Editor.this.mTempRect.left) - Editor.this.mTempRect.right) / 2);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            boolean result = super.onTouchEvent(ev);
            switch (ev.getActionMasked()) {
                case 0:
                    this.mDownPositionX = ev.getRawX();
                    this.mDownPositionY = ev.getRawY();
                    break;
                case 1:
                    if (!offsetHasBeenChanged()) {
                        float deltaX = this.mDownPositionX - ev.getRawX();
                        float deltaY = this.mDownPositionY - ev.getRawY();
                        float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        int touchSlop = ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledTouchSlop();
                        if (distanceSquared < ((float) (touchSlop * touchSlop))) {
                            if (Editor.this.mTextActionMode != null) {
                                Editor.this.mTextActionMode.finish();
                            } else {
                                Editor.this.startInsertionActionMode();
                            }
                        }
                    } else if (Editor.this.mTextActionMode != null) {
                        Editor.this.mTextActionMode.invalidateContentRect();
                    }
                    if (Editor.this.mThemeIsDeviceDefault && Editor.this.mTextActionMode != null) {
                        removeHiderCallback();
                        break;
                    }
                    hideAfterDelay();
                    break;
                    break;
                case 3:
                    hideAfterDelay();
                    break;
            }
            return result;
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        public void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset);
        }

        public void updatePosition(float x, float y) {
            int offset;
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                if (this.mPreviousLineTouched == -1) {
                    this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
                }
                int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
                offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                this.mPreviousLineTouched = currLine;
            } else {
                offset = Editor.this.mTextView.getOffsetForPosition(x, y);
            }
            positionAtCursorOffset(offset, false);
            if (Editor.this.mTextActionMode != null) {
                Editor.this.mTextActionMode.invalidate();
            }
        }

        void onHandleMoved() {
            super.onHandleMoved();
            removeHiderCallback();
        }

        public void onDetached() {
            super.onDetached();
            removeHiderCallback();
        }
    }

    private class InsertionPointCursorController implements CursorController {
        private InsertionHandleView mHandle;

        private InsertionPointCursorController() {
        }

        public void show() {
            if (!Editor.this.isUniversalSwitchEnabled()) {
                getHandle().show();
                if (Editor.this.mSelectionModifierCursorController != null) {
                    Editor.this.mSelectionModifierCursorController.hide();
                }
            }
        }

        public void hide() {
            if (this.mHandle != null) {
                this.mHandle.hide();
            }
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        private InsertionHandleView getHandle() {
            if (Editor.this.mSelectHandleCenter == null) {
                Editor.this.mSelectHandleCenter = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleRes);
            }
            if (this.mHandle == null) {
                this.mHandle = new InsertionHandleView(Editor.this.mSelectHandleCenter);
            }
            return this.mHandle;
        }

        public void onDetached() {
            Editor.this.mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
            if (this.mHandle != null) {
                this.mHandle.onDetached();
            }
        }
    }

    private class PositionListener implements OnPreDrawListener {
        private final int MAXIMUM_NUMBER_OF_LISTENERS;
        private boolean[] mCanMove;
        protected Handler mDelayHandler;
        private int mDelayTime;
        private int mNumberOfListeners;
        private boolean mPositionHasChanged;
        private TextViewPositionListener[] mPositionListeners;
        private int mPositionX;
        private int mPositionY;
        private boolean mScrollHasChanged;
        private int[] mTempContentsViewCoords;
        final int[] mTempCoords;

        private PositionListener() {
            this.MAXIMUM_NUMBER_OF_LISTENERS = 7;
            this.mPositionListeners = new TextViewPositionListener[7];
            this.mCanMove = new boolean[7];
            this.mPositionHasChanged = true;
            this.mTempCoords = new int[2];
            this.mTempContentsViewCoords = null;
            this.mDelayTime = 300;
            this.mDelayHandler = null;
        }

        public void addSubscriber(TextViewPositionListener positionListener, boolean canMove) {
            if (this.mNumberOfListeners == 0) {
                updatePosition();
                Editor.this.mTextView.getViewTreeObserver().addOnPreDrawListener(this);
            }
            int emptySlotIndex = -1;
            int i = 0;
            while (i < 7) {
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
            for (int i = 0; i < 7; i++) {
                if (this.mPositionListeners[i] == positionListener) {
                    this.mPositionListeners[i] = null;
                    this.mNumberOfListeners--;
                    break;
                }
            }
            if (this.mNumberOfListeners == 0) {
                Editor.this.mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
            }
        }

        public int getPositionX() {
            return this.mPositionX;
        }

        public int getPositionY() {
            return this.mPositionY;
        }

        private void prepareHander() {
            this.mDelayHandler = new Handler() {
                public void handleMessage(Message msg) {
                    PositionListener.this.updatePositionWithDelay(msg.obj, PositionListener.this.mPositionX, PositionListener.this.mPositionY, true, true);
                }
            };
        }

        public boolean onPreDraw() {
            updatePosition();
            checkContentsSizeChanged();
            int i = 0;
            while (i < 7) {
                if (this.mPositionHasChanged || this.mScrollHasChanged || this.mCanMove[i]) {
                    TextViewPositionListener positionListener = this.mPositionListeners[i];
                    if (positionListener != null) {
                        boolean isNeedToDelay = false;
                        if (Editor.this.mThemeIsDeviceDefault && this.mPositionHasChanged && (positionListener instanceof HandleView)) {
                            HandleView currentHandle = (HandleView) positionListener;
                            if (!currentHandle.isDragging()) {
                                currentHandle.dismiss();
                                if (!Editor.this.mSelectionActionMode && (currentHandle instanceof InsertionHandleView) && Editor.this.mTextActionMode == null) {
                                    ((InsertionHandleView) currentHandle).addHiderCallback();
                                }
                                isNeedToDelay = true;
                            }
                        }
                        if (isNeedToDelay) {
                            Message msg = Message.obtain();
                            msg.obj = positionListener;
                            msg.what = 0;
                            if (positionListener instanceof HandleView) {
                                msg.what = ((HandleView) positionListener).mHandleType;
                            }
                            if (this.mDelayHandler == null) {
                                prepareHander();
                            }
                            this.mDelayHandler.removeMessages(msg.what);
                            this.mDelayHandler.sendMessageDelayed(msg, (long) this.mDelayTime);
                        } else {
                            try {
                                positionListener.updatePosition(this.mPositionX, this.mPositionY, this.mPositionHasChanged, this.mScrollHasChanged);
                            } catch (BadTokenException e) {
                                Log.e("Editor", "Occurred BadTokenException by position update timing.");
                                e.printStackTrace();
                            }
                        }
                    }
                }
                i++;
            }
            this.mScrollHasChanged = false;
            return true;
        }

        private void updatePositionWithDelay(TextViewPositionListener positionListener, int positionX, int positionY, boolean positonChanged, boolean scrollChanged) {
            positionListener.updatePosition(positionX, positionY, positonChanged, scrollChanged);
        }

        private void updatePosition() {
            boolean z;
            Editor.this.mTextView.getLocationInWindow(this.mTempCoords);
            if (this.mTempCoords[0] == this.mPositionX && this.mTempCoords[1] == this.mPositionY) {
                z = false;
            } else {
                z = true;
            }
            this.mPositionHasChanged = z;
            this.mPositionX = this.mTempCoords[0];
            this.mPositionY = this.mTempCoords[1];
        }

        private void checkContentsSizeChanged() {
            if (Editor.this.mTextView.getRootView() != null) {
                if (this.mTempContentsViewCoords == null) {
                    this.mTempContentsViewCoords = new int[2];
                } else if (!(this.mPositionHasChanged || (this.mTempContentsViewCoords[0] == 0 && this.mTempContentsViewCoords[1] == 0))) {
                    this.mPositionHasChanged = true;
                }
                this.mTempContentsViewCoords[0] = 0;
                this.mTempContentsViewCoords[1] = 0;
            }
        }

        public void onScrollChanged() {
            this.mScrollHasChanged = true;
        }
    }

    static final class ProcessTextIntentActionsHandler {
        private final SparseArray<AccessibilityAction> mAccessibilityActions;
        private final SparseArray<Intent> mAccessibilityIntents;
        private final Editor mEditor;
        private int mMenuItemImgSize;
        private final PackageManager mPackageManager;
        private final TextView mTextView;

        private ProcessTextIntentActionsHandler(Editor editor) {
            this.mAccessibilityIntents = new SparseArray();
            this.mAccessibilityActions = new SparseArray();
            this.mEditor = (Editor) Preconditions.checkNotNull(editor);
            this.mTextView = (TextView) Preconditions.checkNotNull(this.mEditor.mTextView);
            this.mPackageManager = (PackageManager) Preconditions.checkNotNull(this.mTextView.getContext().getPackageManager());
            this.mMenuItemImgSize = this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_selectall_mtrl).getIntrinsicWidth();
        }

        public void onInitializeMenu(Menu menu) {
            onInitializeMenu(menu, false);
        }

        public void onInitializeMenu(Menu menu, boolean isDeviceDefault) {
            int i = 0;
            for (ResolveInfo resolveInfo : getSupportedActivities()) {
                int i2;
                String resolveInfoString = resolveInfo.toString();
                if (resolveInfoString != null && isDeviceDefault) {
                    Drawable MenuIcon = resolveInfo.loadIcon(this.mTextView.getContext().getPackageManager());
                    if (resolveInfoString.contains("com.sec.android.app.translator") || resolveInfoString.contains("com.google.android.apps.translate")) {
                        MenuIcon = this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_translate_mtrl);
                    } else if (MenuIcon != null) {
                        ColorMatrix cm = new ColorMatrix();
                        cm.setSaturation(0.0f);
                        MenuIcon.setColorFilter(new ColorMatrixColorFilter(cm));
                        MenuIcon.setBounds(0, 0, this.mMenuItemImgSize, this.mMenuItemImgSize);
                    }
                    if (MenuIcon != null) {
                        i2 = i + 1;
                        menu.add(0, 0, i + 10, getLabel(resolveInfo)).setIcon(MenuIcon).setIntent(createProcessTextIntentForResolveInfo(resolveInfo)).setShowAsAction(1);
                        i = i2;
                    }
                }
                i2 = i + 1;
                menu.add(0, 0, i + 10, getLabel(resolveInfo)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo)).setShowAsAction(1);
                i = i2;
            }
        }

        public boolean performMenuItemAction(MenuItem item) {
            return fireIntent(item.getIntent());
        }

        public void initializeAccessibilityActions() {
            this.mAccessibilityIntents.clear();
            this.mAccessibilityActions.clear();
            int i = 0;
            for (ResolveInfo resolveInfo : getSupportedActivities()) {
                int i2 = i + 1;
                int actionId = 268435712 + i;
                this.mAccessibilityActions.put(actionId, new AccessibilityAction(actionId, getLabel(resolveInfo)));
                this.mAccessibilityIntents.put(actionId, createProcessTextIntentForResolveInfo(resolveInfo));
                i = i2;
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
            for (int i = 0; i < this.mAccessibilityActions.size(); i++) {
                nodeInfo.addAction((AccessibilityAction) this.mAccessibilityActions.valueAt(i));
            }
        }

        public boolean performAccessibilityAction(int actionId) {
            return fireIntent((Intent) this.mAccessibilityIntents.get(actionId));
        }

        private boolean fireIntent(Intent intent) {
            if (intent == null || !"android.intent.action.PROCESS_TEXT".equals(intent.getAction())) {
                return false;
            }
            intent.putExtra("android.intent.extra.PROCESS_TEXT", this.mTextView.getSelectedText());
            this.mEditor.mPreserveDetachedSelection = true;
            this.mTextView.startActivityForResult(intent, 100);
            return true;
        }

        private List<ResolveInfo> getSupportedActivities() {
            return this.mTextView.getContext().getPackageManager().queryIntentActivities(createProcessTextIntent(), 0);
        }

        private Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
            return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", !this.mTextView.isTextEditable()).setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }

        private Intent createProcessTextIntent() {
            return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
        }

        private CharSequence getLabel(ResolveInfo resolveInfo) {
            return resolveInfo.loadLabel(this.mPackageManager);
        }
    }

    private class SelectionEndHandleView extends HandleView {
        private boolean mInWord;
        private boolean mLanguageDirectionChanged;
        private float mPrevX;
        private final float mTextViewEdgeSlop;
        private final int[] mTextViewLocation;
        private float mTouchWordDelta;

        public SelectionEndHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
            this.mInWord = false;
            this.mLanguageDirectionChanged = false;
            this.mTextViewLocation = new int[2];
            this.mHandleType = 2;
            this.mTextViewEdgeSlop = (float) (ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledTouchSlop() * 4);
        }

        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return (drawable.getIntrinsicWidth() * 3) / 4;
            }
            return drawable.getIntrinsicWidth() / 4;
        }

        protected int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun ? 5 : 3;
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionEnd();
        }

        public void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionStart(), offset);
            if (Editor.this.mTextActionMode != null) {
                Editor.this.mTextActionMode.invalidate();
            }
            updateDrawable();
        }

        public void updatePosition(float x, float y) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                positionAndAdjustForCrossingHandles(Editor.this.mTextView.getOffsetForPosition(x, y));
                return;
            }
            if (this.mPreviousLineTouched == -1) {
                this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
            }
            boolean positionCursor = false;
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
            int initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            if (initialOffset <= selectionStart) {
                if (Editor.this.mThemeIsDeviceDefault) {
                    currLine = layout.getLineForOffset(initialOffset);
                    initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                } else {
                    currLine = layout.getLineForOffset(selectionStart);
                    initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                }
            }
            int offset = initialOffset;
            int end = Editor.this.getWordEnd(offset);
            int start = Editor.this.getWordStart(offset);
            if (this.mPrevX == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
                this.mPrevX = x;
            }
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            boolean selectionEndRtl = layout.isRtlCharAt(selectionEnd);
            boolean atRtl = layout.isRtlCharAt(offset);
            boolean isLvlBoundary = layout.isLevelBoundary(offset);
            if (isLvlBoundary || ((selectionEndRtl && !atRtl) || (!selectionEndRtl && atRtl))) {
                this.mLanguageDirectionChanged = true;
                this.mTouchWordDelta = 0.0f;
                positionAndAdjustForCrossingHandles(offset);
            } else if (!this.mLanguageDirectionChanged || isLvlBoundary) {
                float xDiff = x - this.mPrevX;
                boolean isExpanding = atRtl ? xDiff < 0.0f || currLine < this.mPreviousLineTouched : xDiff > 0.0f || currLine > this.mPreviousLineTouched;
                if (Editor.this.mTextView.getHorizontallyScrolling() && positionNearEdgeOfScrollingView(x, atRtl)) {
                    if (Editor.this.mTextView.canScrollHorizontally(atRtl ? -1 : 1) && ((isExpanding && offset > selectionEnd) || !isExpanding)) {
                        this.mTouchWordDelta = 0.0f;
                        positionAndAdjustForCrossingHandles(atRtl ? layout.getOffsetToLeftOf(this.mPreviousOffset) : layout.getOffsetToRightOf(this.mPreviousOffset));
                        return;
                    }
                }
                if (isExpanding) {
                    if (!this.mInWord || currLine > this.mPrevLine) {
                        int wordEndOnCurrLine = end;
                        if (!(layout == null || layout.getLineForOffset(end) == currLine)) {
                            wordEndOnCurrLine = layout.getLineEnd(currLine);
                        }
                        if (offset >= start + ((wordEndOnCurrLine - start) / 2) || currLine > this.mPrevLine) {
                            offset = end;
                        } else {
                            offset = this.mPreviousOffset;
                        }
                    }
                    if (offset > initialOffset) {
                        this.mTouchWordDelta = layout.getPrimaryHorizontal(offset) - Editor.this.mTextView.convertToLocalHorizontalCoordinate(x);
                    } else {
                        this.mTouchWordDelta = 0.0f;
                    }
                    positionCursor = true;
                } else {
                    int adjustedOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, this.mTouchWordDelta + x);
                    if (adjustedOffset < this.mPreviousOffset || currLine < this.mPrevLine) {
                        if (currLine < this.mPrevLine) {
                            offset = end;
                            if (offset > initialOffset) {
                                this.mTouchWordDelta = layout.getPrimaryHorizontal(offset) - Editor.this.mTextView.convertToLocalHorizontalCoordinate(x);
                            } else {
                                this.mTouchWordDelta = 0.0f;
                            }
                        } else {
                            offset = adjustedOffset;
                        }
                        positionCursor = true;
                    } else if (adjustedOffset > this.mPreviousOffset) {
                        this.mTouchWordDelta = layout.getPrimaryHorizontal(this.mPreviousOffset) - Editor.this.mTextView.convertToLocalHorizontalCoordinate(x);
                    }
                }
                if (positionCursor) {
                    this.mPreviousLineTouched = currLine;
                    positionAndAdjustForCrossingHandles(offset);
                }
                this.mPrevX = x;
            } else {
                positionAndAdjustForCrossingHandles(offset);
                this.mTouchWordDelta = 0.0f;
                this.mLanguageDirectionChanged = false;
            }
        }

        private void positionAndAdjustForCrossingHandles(int offset) {
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            if (offset <= selectionStart) {
                if (!Editor.this.mThemeIsDeviceDefault) {
                    offset = Editor.this.getNextCursorOffset(selectionStart, true);
                } else if (offset == selectionStart) {
                    return;
                }
                this.mTouchWordDelta = 0.0f;
            }
            positionAtCursorOffset(offset, false);
        }

        protected void positionAtCursorOffset(int offset, boolean parentScrolled) {
            super.positionAtCursorOffset(offset, parentScrolled);
            boolean z = (offset == -1 || Editor.this.getWordIteratorWithText().isBoundary(offset)) ? false : true;
            this.mInWord = z;
        }

        public boolean refreshForSwitchingCursor() {
            if (!calculateForSwitchingCursor()) {
                return false;
            }
            Editor.this.mTextView.invalidate();
            return true;
        }

        protected boolean calculateForSwitchingCursor() {
            boolean bSwitchCursor = this.mbSwitchCursor;
            if (isHandleViewScreenOut()) {
                this.mbSwitchCursor = true;
            } else {
                this.mbSwitchCursor = false;
            }
            if (bSwitchCursor == this.mbSwitchCursor) {
                return false;
            }
            updateDrawable();
            this.mPositionX = (int) (((Editor.this.mTextView.getLayout().getPrimaryHorizontal(getCurrentCursorOffset()) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset()));
            this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean superResult = super.onTouchEvent(event);
            if (event.getActionMasked() == 0) {
                this.mTouchWordDelta = 0.0f;
                this.mPrevX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            return superResult;
        }

        private boolean positionNearEdgeOfScrollingView(float x, boolean atRtl) {
            boolean z = true;
            Editor.this.mTextView.getLocationOnScreen(this.mTextViewLocation);
            if (!atRtl) {
                if (x <= ((float) ((this.mTextViewLocation[0] + Editor.this.mTextView.getWidth()) - Editor.this.mTextView.getPaddingRight())) - this.mTextViewEdgeSlop) {
                    z = false;
                }
                return z;
            } else if (x < ((float) (this.mTextViewLocation[0] + Editor.this.mTextView.getPaddingLeft())) + this.mTextViewEdgeSlop) {
                return true;
            } else {
                return false;
            }
        }

        private boolean isHandleViewScreenOut() {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            PositionListener positionListener = Editor.this.getPositionListener();
            int iconSize = this.mDrawableLtr.getIntrinsicWidth() / 2;
            int horizontalOffset = 0;
            switch (getHorizontalGravity(this.mbSwitchCursor)) {
                case 3:
                    horizontalOffset = 0;
                    break;
                case 5:
                    horizontalOffset = getPreferredWidth() - (iconSize * 2);
                    break;
            }
            if ((((this.mPositionX + positionListener.getPositionX()) + this.mHotspotX) + iconSize) + horizontalOffset > displayMetrics.widthPixels) {
                return true;
            }
            return false;
        }
    }

    class SelectionModifierCursorController implements CursorController {
        private float mDownPositionX;
        private float mDownPositionY;
        private boolean mDragAcceleratorActive;
        private SelectionEndHandleView mEndHandle;
        private boolean mGestureStayedInTapRegion;
        private boolean mHaventMovedEnoughToStartDrag;
        private int mLineSelectionIsOn = -1;
        private int mMaxTouchOffset;
        private int mMinTouchOffset;
        private SelectionStartHandleView mStartHandle;
        private int mStartOffset = -1;
        private boolean mSwitchedLines = false;

        SelectionModifierCursorController() {
            resetTouchOffsets();
        }

        public void show() {
            if (!Editor.this.isUniversalSwitchEnabled() && !Editor.this.mTextView.isInBatchEditMode()) {
                initDrawables();
                initHandles();
                Editor.this.hideInsertionPointCursorController();
            }
        }

        private void initDrawables() {
            if (Editor.this.mSelectHandleLeft == null) {
                Editor.this.mSelectHandleLeft = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleLeftRes);
            }
            if (Editor.this.mSelectHandleRight == null) {
                Editor.this.mSelectHandleRight = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleRightRes);
            }
        }

        private void initHandles() {
            if (this.mStartHandle == null) {
                this.mStartHandle = new SelectionStartHandleView(Editor.this.mSelectHandleLeft, Editor.this.mSelectHandleRight);
            }
            if (this.mEndHandle == null) {
                this.mEndHandle = new SelectionEndHandleView(Editor.this.mSelectHandleRight, Editor.this.mSelectHandleLeft);
            }
            this.mStartHandle.show();
            this.mEndHandle.show();
            Editor.this.hideInsertionPointCursorController();
        }

        public void hide() {
            if (this.mStartHandle != null) {
                this.mStartHandle.hide();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.hide();
            }
        }

        public void enterDrag() {
            this.mDragAcceleratorActive = true;
            this.mStartOffset = Editor.this.mTextView.getOffsetForPosition(Editor.this.mLastDownPositionX, Editor.this.mLastDownPositionY);
            this.mLineSelectionIsOn = Editor.this.mTextView.getLineAtCoordinate(Editor.this.mLastDownPositionY);
            Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(true);
        }

        public void onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            float deltaX;
            float deltaY;
            float distanceSquared;
            int startOffset;
            switch (event.getActionMasked()) {
                case 0:
                    if (Editor.this.extractedTextModeWillBeStarted()) {
                        hide();
                        return;
                    }
                    int offsetForPosition = Editor.this.mTextView.getOffsetForPosition(eventX, eventY);
                    this.mMaxTouchOffset = offsetForPosition;
                    this.mMinTouchOffset = offsetForPosition;
                    if (this.mGestureStayedInTapRegion && !Editor.mDisableDoubleTapTextSelection && Editor.this.mDoubleTap) {
                        deltaX = eventX - this.mDownPositionX;
                        deltaY = eventY - this.mDownPositionY;
                        distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        int doubleTapSlop = ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledDoubleTapSlop();
                        if ((distanceSquared < ((float) (doubleTapSlop * doubleTapSlop))) && Editor.this.isPositionOnText(eventX, eventY)) {
                            if (Editor.this.selectCurrentWordAndStartDrag()) {
                                Editor.this.startSelectionActionMode();
                            }
                            Editor.this.mDiscardNextActionUp = true;
                        }
                    }
                    this.mDownPositionX = eventX;
                    this.mDownPositionY = eventY;
                    this.mGestureStayedInTapRegion = true;
                    this.mHaventMovedEnoughToStartDrag = true;
                    return;
                case 1:
                    if (this.mDragAcceleratorActive) {
                        Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(false);
                        show();
                        startOffset = Editor.this.mTextView.getSelectionStart();
                        int endOffset = Editor.this.mTextView.getSelectionEnd();
                        if (endOffset < startOffset) {
                            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), endOffset, startOffset);
                        }
                        if (Editor.this.mTextActionMode == null && Editor.this.getInsertionController() != null) {
                            Editor.this.getInsertionController().show();
                        }
                        if (this.mStartHandle != null) {
                            this.mStartHandle.refreshForSwitchingCursor();
                        }
                        if (this.mEndHandle != null) {
                            this.mEndHandle.refreshForSwitchingCursor();
                        }
                        this.mDragAcceleratorActive = false;
                        this.mStartOffset = -1;
                        this.mSwitchedLines = false;
                        return;
                    }
                    return;
                case 2:
                    ViewConfiguration viewConfig = ViewConfiguration.get(Editor.this.mTextView.getContext());
                    int touchSlop = viewConfig.getScaledTouchSlop();
                    if (this.mGestureStayedInTapRegion || this.mHaventMovedEnoughToStartDrag) {
                        deltaX = eventX - this.mDownPositionX;
                        deltaY = eventY - this.mDownPositionY;
                        distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        if (this.mGestureStayedInTapRegion) {
                            int doubleTapTouchSlop = viewConfig.getScaledDoubleTapTouchSlop();
                            this.mGestureStayedInTapRegion = distanceSquared <= ((float) (doubleTapTouchSlop * doubleTapTouchSlop));
                        }
                        if (this.mHaventMovedEnoughToStartDrag) {
                            this.mHaventMovedEnoughToStartDrag = distanceSquared <= ((float) (touchSlop * touchSlop));
                        }
                    }
                    if ((this.mStartHandle == null || !this.mStartHandle.isShowing()) && this.mStartOffset != -1 && Editor.this.mTextView.getLayout() != null && !this.mHaventMovedEnoughToStartDrag) {
                        float y = eventY;
                        if (this.mSwitchedLines) {
                            float fingerOffset;
                            if (this.mStartHandle != null) {
                                fingerOffset = this.mStartHandle.getIdealVerticalOffset();
                            } else {
                                fingerOffset = (float) touchSlop;
                            }
                            y = eventY - fingerOffset;
                        }
                        int currLine = Editor.this.getCurrentLineAdjustedForSlop(Editor.this.mTextView.getLayout(), this.mLineSelectionIsOn, y);
                        if (this.mSwitchedLines || currLine == this.mLineSelectionIsOn) {
                            int offset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, eventX);
                            if (this.mStartOffset < offset) {
                                offset = Editor.this.getWordEnd(offset);
                                startOffset = Editor.this.getWordStart(this.mStartOffset);
                            } else {
                                offset = Editor.this.getWordStart(offset);
                                startOffset = Editor.this.getWordEnd(this.mStartOffset);
                            }
                            this.mLineSelectionIsOn = currLine;
                            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), startOffset, offset);
                            return;
                        }
                        this.mSwitchedLines = true;
                        return;
                    }
                    return;
                case 5:
                case 6:
                    if (Editor.this.mTextView.getContext().getPackageManager().hasSystemFeature("android.hardware.touchscreen.multitouch.distinct")) {
                        updateMinAndMaxOffsets(event);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void updateMinAndMaxOffsets(MotionEvent event) {
            int pointerCount = event.getPointerCount();
            for (int index = 0; index < pointerCount; index++) {
                int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(index), event.getY(index));
                if (offset < this.mMinTouchOffset) {
                    this.mMinTouchOffset = offset;
                }
                if (offset > this.mMaxTouchOffset) {
                    this.mMaxTouchOffset = offset;
                }
            }
        }

        public int getMinTouchOffset() {
            return this.mMinTouchOffset;
        }

        public int getMaxTouchOffset() {
            return this.mMaxTouchOffset;
        }

        public void resetTouchOffsets() {
            this.mMaxTouchOffset = -1;
            this.mMinTouchOffset = -1;
            this.mStartOffset = -1;
            this.mDragAcceleratorActive = false;
            this.mSwitchedLines = false;
        }

        public boolean isSelectionStartDragged() {
            return this.mStartHandle != null && this.mStartHandle.isDragging();
        }

        public boolean isDragAcceleratorActive() {
            return this.mDragAcceleratorActive;
        }

        private void setDragAcceleratorActive(boolean isDragAcceleratorActive) {
            this.mDragAcceleratorActive = isDragAcceleratorActive;
        }

        public boolean isSelectHandlerNotMoved() {
            return (this.mStartHandle == null || this.mStartHandle.isMoved() || this.mEndHandle == null || this.mEndHandle.isMoved()) ? false : true;
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        public void onDetached() {
            Editor.this.mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
            if (this.mStartHandle != null) {
                this.mStartHandle.onDetached();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.onDetached();
            }
        }
    }

    private class SelectionStartHandleView extends HandleView {
        private boolean mInWord;
        private boolean mLanguageDirectionChanged;
        private float mPrevX;
        private final float mTextViewEdgeSlop;
        private final int[] mTextViewLocation;
        private float mTouchWordDelta;

        public SelectionStartHandleView(Drawable drawableLtr, Drawable drawableRtl) {
            super(drawableLtr, drawableRtl);
            this.mInWord = false;
            this.mLanguageDirectionChanged = false;
            this.mTextViewLocation = new int[2];
            this.mHandleType = 1;
            this.mTextViewEdgeSlop = (float) (ViewConfiguration.get(Editor.this.mTextView.getContext()).getScaledTouchSlop() * 4);
        }

        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun) {
                return drawable.getIntrinsicWidth() / 4;
            }
            return (drawable.getIntrinsicWidth() * 3) / 4;
        }

        protected int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun ? 3 : 5;
        }

        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        public void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset, Editor.this.mTextView.getSelectionEnd());
            updateDrawable();
            if (Editor.this.mTextActionMode != null) {
                Editor.this.mTextActionMode.invalidate();
            }
        }

        public void updatePosition(float x, float y) {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                positionAndAdjustForCrossingHandles(Editor.this.mTextView.getOffsetForPosition(x, y));
                return;
            }
            if (this.mPreviousLineTouched == -1) {
                this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
            }
            boolean positionCursor = false;
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
            int initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
            if (initialOffset >= selectionEnd) {
                if (Editor.this.mThemeIsDeviceDefault) {
                    currLine = layout.getLineForOffset(initialOffset);
                    initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                } else {
                    currLine = layout.getLineForOffset(selectionEnd);
                    initialOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x);
                }
            }
            int offset = initialOffset;
            int end = Editor.this.getWordEnd(offset);
            int start = Editor.this.getWordStart(offset);
            if (this.mPrevX == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
                this.mPrevX = x;
            }
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            boolean selectionStartRtl = layout.isRtlCharAt(selectionStart);
            boolean atRtl = layout.isRtlCharAt(offset);
            boolean isLvlBoundary = layout.isLevelBoundary(offset);
            if (isLvlBoundary || ((selectionStartRtl && !atRtl) || (!selectionStartRtl && atRtl))) {
                this.mLanguageDirectionChanged = true;
                this.mTouchWordDelta = 0.0f;
                positionAndAdjustForCrossingHandles(offset);
            } else if (!this.mLanguageDirectionChanged || isLvlBoundary) {
                float xDiff = x - this.mPrevX;
                boolean isExpanding = atRtl ? xDiff > 0.0f || currLine > this.mPreviousLineTouched : xDiff < 0.0f || currLine < this.mPreviousLineTouched;
                if (!Editor.this.mTextView.getHorizontallyScrolling() || !positionNearEdgeOfScrollingView(x, atRtl) || Editor.this.mTextView.getScrollX() == 0 || ((!isExpanding || offset >= selectionStart) && isExpanding)) {
                    if (isExpanding) {
                        if (!this.mInWord || currLine < this.mPrevLine) {
                            int wordStartOnCurrLine = start;
                            if (!(layout == null || layout.getLineForOffset(start) == currLine)) {
                                wordStartOnCurrLine = layout.getLineStart(currLine);
                            }
                            if (offset <= end - ((end - wordStartOnCurrLine) / 2) || currLine < this.mPrevLine) {
                                offset = start;
                            } else {
                                offset = this.mPreviousOffset;
                            }
                        }
                        if (layout == null || offset >= initialOffset) {
                            this.mTouchWordDelta = 0.0f;
                        } else {
                            this.mTouchWordDelta = Editor.this.mTextView.convertToLocalHorizontalCoordinate(x) - layout.getPrimaryHorizontal(offset);
                        }
                        positionCursor = true;
                    } else {
                        int adjustedOffset = Editor.this.mTextView.getOffsetAtCoordinate(currLine, x - this.mTouchWordDelta);
                        if (adjustedOffset > this.mPreviousOffset || currLine > this.mPrevLine) {
                            if (currLine > this.mPrevLine) {
                                offset = start;
                                if (layout == null || offset >= initialOffset) {
                                    this.mTouchWordDelta = 0.0f;
                                } else {
                                    this.mTouchWordDelta = Editor.this.mTextView.convertToLocalHorizontalCoordinate(x) - layout.getPrimaryHorizontal(offset);
                                }
                            } else {
                                offset = adjustedOffset;
                            }
                            positionCursor = true;
                        } else if (adjustedOffset < this.mPreviousOffset) {
                            this.mTouchWordDelta = Editor.this.mTextView.convertToLocalHorizontalCoordinate(x) - layout.getPrimaryHorizontal(this.mPreviousOffset);
                        }
                    }
                    if (positionCursor) {
                        this.mPreviousLineTouched = currLine;
                        positionAndAdjustForCrossingHandles(offset);
                    }
                    this.mPrevX = x;
                    return;
                }
                this.mTouchWordDelta = 0.0f;
                positionAndAdjustForCrossingHandles(atRtl ? layout.getOffsetToRightOf(this.mPreviousOffset) : layout.getOffsetToLeftOf(this.mPreviousOffset));
            } else {
                positionAndAdjustForCrossingHandles(offset);
                this.mTouchWordDelta = 0.0f;
                this.mLanguageDirectionChanged = false;
            }
        }

        private void positionAndAdjustForCrossingHandles(int offset) {
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            if (offset >= selectionEnd) {
                if (!Editor.this.mThemeIsDeviceDefault) {
                    offset = Editor.this.getNextCursorOffset(selectionEnd, false);
                } else if (offset == selectionEnd) {
                    return;
                }
                this.mTouchWordDelta = 0.0f;
            }
            positionAtCursorOffset(offset, false);
        }

        protected void positionAtCursorOffset(int offset, boolean parentScrolled) {
            super.positionAtCursorOffset(offset, parentScrolled);
            boolean z = (offset == -1 || Editor.this.getWordIteratorWithText().isBoundary(offset)) ? false : true;
            this.mInWord = z;
        }

        public boolean refreshForSwitchingCursor() {
            if (!calculateForSwitchingCursor()) {
                return false;
            }
            Editor.this.mTextView.invalidate();
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
            Layout layout = Editor.this.mTextView.getLayout();
            this.mPositionX = (int) (((layout.getPrimaryHorizontal(getCurrentCursorOffset()) - (layout.getParagraphDirection(layout.getLineForOffset(getCurrentCursorOffset())) == -1 ? Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS : -0.5f)) - ((float) this.mHotspotX)) - ((float) getHorizontalOffset()));
            this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean superResult = super.onTouchEvent(event);
            if (event.getActionMasked() == 0) {
                this.mTouchWordDelta = 0.0f;
                this.mPrevX = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            return superResult;
        }

        private boolean positionNearEdgeOfScrollingView(float x, boolean atRtl) {
            boolean z = true;
            Editor.this.mTextView.getLocationOnScreen(this.mTextViewLocation);
            if (!atRtl) {
                if (x >= ((float) (this.mTextViewLocation[0] + Editor.this.mTextView.getPaddingLeft())) + this.mTextViewEdgeSlop) {
                    z = false;
                }
                return z;
            } else if (x > ((float) ((this.mTextViewLocation[0] + Editor.this.mTextView.getWidth()) - Editor.this.mTextView.getPaddingRight())) - this.mTextViewEdgeSlop) {
                return true;
            } else {
                return false;
            }
        }

        private boolean isHandleViewScreenOut() {
            return (((this.mPositionX + Editor.this.getPositionListener().getPositionX()) + this.mHotspotX) - (this.mDrawableLtr.getIntrinsicWidth() / 2)) + getHorizontalOffset() < 0;
        }
    }

    class SpanController implements SpanWatcher {
        private static final int DISPLAY_TIMEOUT_MS = 3000;
        private Runnable mHidePopup;
        private EasyEditPopupWindow mPopupWindow;

        SpanController() {
        }

        private boolean isNonIntermediateSelectionSpan(Spannable text, Object span) {
            return (Selection.SELECTION_START == span || Selection.SELECTION_END == span) && (text.getSpanFlags(span) & 512) == 0;
        }

        public void onSpanAdded(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (span instanceof EasyEditSpan) {
                if (this.mPopupWindow == null) {
                    this.mPopupWindow = new EasyEditPopupWindow();
                    this.mHidePopup = new Runnable() {
                        public void run() {
                            SpanController.this.hide();
                        }
                    };
                }
                if (this.mPopupWindow.mEasyEditSpan != null) {
                    this.mPopupWindow.mEasyEditSpan.setDeleteEnabled(false);
                }
                this.mPopupWindow.setEasyEditSpan((EasyEditSpan) span);
                this.mPopupWindow.setOnDeleteListener(new EasyEditDeleteListener() {
                    public void onDeleteClick(EasyEditSpan span) {
                        Editable editable = (Editable) Editor.this.mTextView.getText();
                        int start = editable.getSpanStart(span);
                        int end = editable.getSpanEnd(span);
                        if (start >= 0 && end >= 0) {
                            SpanController.this.sendEasySpanNotification(1, span);
                            Editor.this.mTextView.deleteText_internal(start, end);
                        }
                        editable.removeSpan(span);
                    }
                });
                if (Editor.this.mTextView.getWindowVisibility() == 0 && Editor.this.mTextView.getLayout() != null && !Editor.this.extractedTextModeWillBeStarted()) {
                    this.mPopupWindow.show();
                    Editor.this.mTextView.removeCallbacks(this.mHidePopup);
                    Editor.this.mTextView.postDelayed(this.mHidePopup, 3000);
                }
            }
        }

        public void onSpanRemoved(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (this.mPopupWindow != null && span == this.mPopupWindow.mEasyEditSpan) {
                hide();
            }
        }

        public void onSpanChanged(Spannable text, Object span, int previousStart, int previousEnd, int newStart, int newEnd) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (this.mPopupWindow != null && (span instanceof EasyEditSpan)) {
                EasyEditSpan easyEditSpan = (EasyEditSpan) span;
                sendEasySpanNotification(2, easyEditSpan);
                text.removeSpan(easyEditSpan);
            }
        }

        public void hide() {
            if (this.mPopupWindow != null) {
                this.mPopupWindow.hide();
                Editor.this.mTextView.removeCallbacks(this.mHidePopup);
            }
        }

        private void sendEasySpanNotification(int textChangedType, EasyEditSpan span) {
            try {
                PendingIntent pendingIntent = span.getPendingIntent();
                if (pendingIntent != null) {
                    Intent intent = new Intent();
                    intent.putExtra(EasyEditSpan.EXTRA_TEXT_CHANGED_TYPE, textChangedType);
                    pendingIntent.send(Editor.this.mTextView.getContext(), 0, intent);
                }
            } catch (CanceledException e) {
                Log.w("Editor", "PendingIntent for notification cannot be sent", e);
            }
        }
    }

    private class SuggestionsPopupWindow extends PinnedPopupWindow implements OnItemClickListener {
        private static final int ADD_TO_DICTIONARY = -1;
        private static final int DELETE_TEXT = -2;
        private static final int MAX_NUMBER_SUGGESTIONS = 5;
        private boolean mCursorWasVisibleBeforeSuggestions;
        private boolean mIsShowingUp = false;
        private int mNumberOfSuggestions;
        private final HashMap<SuggestionSpan, Integer> mSpansLengths;
        private SuggestionInfo[] mSuggestionInfos;
        private final Comparator<SuggestionSpan> mSuggestionSpanComparator;
        private SuggestionAdapter mSuggestionsAdapter;

        private class CustomPopupWindow extends PopupWindow {
            public CustomPopupWindow(Context context, int defStyleAttr) {
                super(context, null, defStyleAttr);
            }

            public void dismiss() {
                super.dismiss();
                Editor.this.getPositionListener().removeSubscriber(SuggestionsPopupWindow.this);
                ((Spannable) Editor.this.mTextView.getText()).removeSpan(Editor.this.mSuggestionRangeSpan);
                Editor.this.mTextView.setCursorVisible(SuggestionsPopupWindow.this.mCursorWasVisibleBeforeSuggestions);
                if (Editor.this.hasInsertionController()) {
                    Editor.this.getInsertionController().show();
                }
            }
        }

        private class SuggestionAdapter extends BaseAdapter {
            private LayoutInflater mInflater;

            private SuggestionAdapter() {
                this.mInflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService("layout_inflater");
            }

            public int getCount() {
                return SuggestionsPopupWindow.this.mNumberOfSuggestions;
            }

            public Object getItem(int position) {
                return SuggestionsPopupWindow.this.mSuggestionInfos[position];
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) convertView;
                if (textView == null) {
                    textView = (TextView) this.mInflater.inflate(Editor.this.mTextView.mTextEditSuggestionItemLayout, parent, false);
                }
                SuggestionInfo suggestionInfo = SuggestionsPopupWindow.this.mSuggestionInfos[position];
                textView.setText(suggestionInfo.text);
                if (suggestionInfo.suggestionIndex == -1 || suggestionInfo.suggestionIndex == -2) {
                    textView.setBackgroundColor(0);
                } else if (Editor.this.mThemeIsDeviceDefault) {
                    ShapeDrawable bg = new ShapeDrawable(new RectShape());
                    bg.getPaint().setColor(0);
                    final RippleDrawable ripple = new RippleDrawable(ColorStateList.valueOf(687865856), bg, null);
                    textView.setOnTouchListener(new OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            ripple.setHotspot(event.getX(), event.getY());
                            return false;
                        }
                    });
                    textView.setBackground(ripple);
                } else {
                    textView.setBackgroundColor(-1);
                }
                return textView;
            }
        }

        private class SuggestionInfo {
            TextAppearanceSpan highlightSpan;
            int suggestionEnd;
            int suggestionIndex;
            SuggestionSpan suggestionSpan;
            int suggestionStart;
            SpannableStringBuilder text;

            private SuggestionInfo() {
                this.text = new SpannableStringBuilder();
                this.highlightSpan = new TextAppearanceSpan(Editor.this.mTextView.getContext(), R.style.TextAppearance_SuggestionHighlight);
            }
        }

        private class SuggestionSpanComparator implements Comparator<SuggestionSpan> {
            private SuggestionSpanComparator() {
            }

            public int compare(SuggestionSpan span1, SuggestionSpan span2) {
                boolean misspelled2 = false;
                int flag1 = span1.getFlags();
                int flag2 = span2.getFlags();
                if (flag1 != flag2) {
                    boolean easy2;
                    boolean misspelled1;
                    boolean easy1 = (flag1 & 1) != 0;
                    if ((flag2 & 1) != 0) {
                        easy2 = true;
                    } else {
                        easy2 = false;
                    }
                    if ((flag1 & 2) != 0) {
                        misspelled1 = true;
                    } else {
                        misspelled1 = false;
                    }
                    if ((flag2 & 2) != 0) {
                        misspelled2 = true;
                    }
                    if (easy1 && !misspelled1) {
                        return -1;
                    }
                    if (easy2 && !misspelled2) {
                        return 1;
                    }
                    if (misspelled1) {
                        return -1;
                    }
                    if (misspelled2) {
                        return 1;
                    }
                }
                return ((Integer) SuggestionsPopupWindow.this.mSpansLengths.get(span1)).intValue() - ((Integer) SuggestionsPopupWindow.this.mSpansLengths.get(span2)).intValue();
            }
        }

        public SuggestionsPopupWindow() {
            super();
            this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
            this.mSuggestionSpanComparator = new SuggestionSpanComparator();
            this.mSpansLengths = new HashMap();
        }

        protected void createPopupWindow() {
            this.mPopupWindow = new CustomPopupWindow(Editor.this.mTextView.getContext(), R.attr.textSuggestionsWindowStyle);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setClippingEnabled(false);
        }

        protected void initContentView() {
            ListView listView = new ListView(Editor.this.mTextView.getContext());
            this.mSuggestionsAdapter = new SuggestionAdapter();
            listView.setAdapter(this.mSuggestionsAdapter);
            listView.setOnItemClickListener(this);
            this.mContentView = listView;
            this.mSuggestionInfos = new SuggestionInfo[7];
            for (int i = 0; i < this.mSuggestionInfos.length; i++) {
                this.mSuggestionInfos[i] = new SuggestionInfo();
            }
        }

        public boolean isShowingUp() {
            return this.mIsShowingUp;
        }

        public void onParentLostFocus() {
            this.mIsShowingUp = false;
        }

        private SuggestionSpan[] getSuggestionSpans() {
            int pos = Editor.this.mTextView.getSelectionStart();
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(pos, pos, SuggestionSpan.class);
            this.mSpansLengths.clear();
            for (SuggestionSpan suggestionSpan : suggestionSpans) {
                this.mSpansLengths.put(suggestionSpan, Integer.valueOf(spannable.getSpanEnd(suggestionSpan) - spannable.getSpanStart(suggestionSpan)));
            }
            Arrays.sort(suggestionSpans, this.mSuggestionSpanComparator);
            return suggestionSpans;
        }

        public void show() {
            if ((Editor.this.mTextView.getText() instanceof Editable) && updateSuggestions()) {
                this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
                Editor.this.mTextView.setCursorVisible(false);
                this.mIsShowingUp = true;
                Editor.this.mIsSuggestionsPopup = true;
                super.show();
            }
        }

        protected void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            int horizontalMeasure = MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE);
            int verticalMeasure = MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE);
            int width = 0;
            View view = null;
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                view = this.mSuggestionsAdapter.getView(i, view, this.mContentView);
                view.getLayoutParams().width = -2;
                view.measure(horizontalMeasure, verticalMeasure);
                width = Math.max(width, view.getMeasuredWidth());
            }
            this.mContentView.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), verticalMeasure);
            Drawable popupBackground = this.mPopupWindow.getBackground();
            if (popupBackground != null) {
                if (Editor.this.mTempRect == null) {
                    Editor.this.mTempRect = new Rect();
                }
                popupBackground.getPadding(Editor.this.mTempRect);
                width += Editor.this.mTempRect.left + Editor.this.mTempRect.right;
            }
            this.mPopupWindow.setWidth(width);
        }

        protected int getTextOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        protected int getVerticalLocalPosition(int line) {
            return Editor.this.mTextView.getLayout().getLineBottom(line);
        }

        protected int clipVertically(int positionY) {
            return Math.min(positionY, Editor.this.mTextView.getResources().getDisplayMetrics().heightPixels - this.mContentView.getMeasuredHeight());
        }

        public void hide() {
            super.hide();
        }

        private boolean updateSuggestions() {
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans = getSuggestionSpans();
            int nbSpans = suggestionSpans.length;
            if (nbSpans == 0) {
                return false;
            }
            SuggestionInfo suggestionInfo;
            this.mNumberOfSuggestions = 0;
            int spanUnionStart = Editor.this.mTextView.getText().length();
            int spanUnionEnd = 0;
            SuggestionSpan misspelledSpan = null;
            int underlineColor = 0;
            int spanIndex = 0;
            while (spanIndex < nbSpans) {
                SuggestionSpan suggestionSpan = suggestionSpans[spanIndex];
                int spanStart = spannable.getSpanStart(suggestionSpan);
                int spanEnd = spannable.getSpanEnd(suggestionSpan);
                spanUnionStart = Math.min(spanStart, spanUnionStart);
                spanUnionEnd = Math.max(spanEnd, spanUnionEnd);
                if ((suggestionSpan.getFlags() & 2) != 0) {
                    misspelledSpan = suggestionSpan;
                }
                if (spanIndex == 0) {
                    underlineColor = suggestionSpan.getUnderlineColor();
                }
                String[] suggestions = suggestionSpan.getSuggestions();
                int nbSuggestions = suggestions.length;
                for (int suggestionIndex = 0; suggestionIndex < nbSuggestions; suggestionIndex++) {
                    int i;
                    String suggestion = suggestions[suggestionIndex];
                    boolean suggestionIsDuplicate = false;
                    for (i = 0; i < this.mNumberOfSuggestions; i++) {
                        if (this.mSuggestionInfos[i].text.toString().equals(suggestion)) {
                            SuggestionSpan otherSuggestionSpan = this.mSuggestionInfos[i].suggestionSpan;
                            int otherSpanStart = spannable.getSpanStart(otherSuggestionSpan);
                            int otherSpanEnd = spannable.getSpanEnd(otherSuggestionSpan);
                            if (spanStart == otherSpanStart && spanEnd == otherSpanEnd) {
                                suggestionIsDuplicate = true;
                                break;
                            }
                        }
                    }
                    if (!suggestionIsDuplicate) {
                        suggestionInfo = this.mSuggestionInfos[this.mNumberOfSuggestions];
                        suggestionInfo.suggestionSpan = suggestionSpan;
                        suggestionInfo.suggestionIndex = suggestionIndex;
                        suggestionInfo.text.replace(0, suggestionInfo.text.length(), (CharSequence) suggestion);
                        this.mNumberOfSuggestions++;
                        if (this.mNumberOfSuggestions == 5) {
                            spanIndex = nbSpans;
                            break;
                        }
                    }
                }
                spanIndex++;
            }
            for (i = 0; i < this.mNumberOfSuggestions; i++) {
                highlightTextDifferences(this.mSuggestionInfos[i], spanUnionStart, spanUnionEnd);
            }
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (!(imm == null || imm.isCurrentInputMethodAsSamsungKeyboard() || misspelledSpan == null)) {
                int misspelledStart = spannable.getSpanStart(misspelledSpan);
                int misspelledEnd = spannable.getSpanEnd(misspelledSpan);
                if (misspelledStart >= 0 && misspelledEnd > misspelledStart) {
                    suggestionInfo = this.mSuggestionInfos[this.mNumberOfSuggestions];
                    suggestionInfo.suggestionSpan = misspelledSpan;
                    suggestionInfo.suggestionIndex = -1;
                    suggestionInfo.text.replace(0, suggestionInfo.text.length(), Editor.this.mTextView.getContext().getString(R.string.addToDictionary));
                    suggestionInfo.text.setSpan(suggestionInfo.highlightSpan, 0, 0, 33);
                    this.mNumberOfSuggestions++;
                }
            }
            suggestionInfo = this.mSuggestionInfos[this.mNumberOfSuggestions];
            suggestionInfo.suggestionSpan = null;
            suggestionInfo.suggestionIndex = -2;
            suggestionInfo.text.replace(0, suggestionInfo.text.length(), Editor.this.mTextView.getContext().getString(R.string.deleteText));
            suggestionInfo.text.setSpan(suggestionInfo.highlightSpan, 0, 0, 33);
            this.mNumberOfSuggestions++;
            if (Editor.this.mSuggestionRangeSpan == null) {
                Editor.this.mSuggestionRangeSpan = new SuggestionRangeSpan();
            }
            if (underlineColor == 0) {
                Editor.this.mSuggestionRangeSpan.setBackgroundColor(Editor.this.mTextView.mHighlightColor);
            } else {
                Editor.this.mSuggestionRangeSpan.setBackgroundColor((16777215 & underlineColor) + (((int) (((float) Color.alpha(underlineColor)) * 0.4f)) << 24));
            }
            spannable.setSpan(Editor.this.mSuggestionRangeSpan, spanUnionStart, spanUnionEnd, 33);
            this.mSuggestionsAdapter.notifyDataSetChanged();
            return true;
        }

        private void highlightTextDifferences(SuggestionInfo suggestionInfo, int unionStart, int unionEnd) {
            Spannable text = (Spannable) Editor.this.mTextView.getText();
            int spanStart = text.getSpanStart(suggestionInfo.suggestionSpan);
            int spanEnd = text.getSpanEnd(suggestionInfo.suggestionSpan);
            suggestionInfo.suggestionStart = spanStart - unionStart;
            suggestionInfo.suggestionEnd = suggestionInfo.suggestionStart + suggestionInfo.text.length();
            suggestionInfo.text.setSpan(suggestionInfo.highlightSpan, 0, suggestionInfo.text.length(), 33);
            String textAsString = text.toString();
            suggestionInfo.text.insert(0, textAsString.substring(unionStart, spanStart));
            suggestionInfo.text.append(textAsString.substring(spanEnd, unionEnd));
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Editable editable = (Editable) Editor.this.mTextView.getText();
            SuggestionInfo suggestionInfo = this.mSuggestionInfos[position];
            if (suggestionInfo.suggestionIndex == -2) {
                int spanUnionStart = editable.getSpanStart(Editor.this.mSuggestionRangeSpan);
                int spanUnionEnd = editable.getSpanEnd(Editor.this.mSuggestionRangeSpan);
                if (spanUnionStart >= 0 && spanUnionEnd > spanUnionStart) {
                    if (spanUnionEnd < editable.length() && Character.isSpaceChar(editable.charAt(spanUnionEnd)) && (spanUnionStart == 0 || Character.isSpaceChar(editable.charAt(spanUnionStart - 1)))) {
                        spanUnionEnd++;
                    }
                    Editor.this.mTextView.deleteText_internal(spanUnionStart, spanUnionEnd);
                }
                hide();
                return;
            }
            int spanStart = editable.getSpanStart(suggestionInfo.suggestionSpan);
            int spanEnd = editable.getSpanEnd(suggestionInfo.suggestionSpan);
            if (spanStart < 0 || spanEnd <= spanStart) {
                hide();
                return;
            }
            String originalText = editable.toString().substring(spanStart, spanEnd);
            if (suggestionInfo.suggestionIndex == -1) {
                Intent intent = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
                intent.putExtra(Words.WORD, originalText);
                intent.putExtra(Words.LOCALE, Editor.this.mTextView.getTextServicesLocale().toString());
                intent.setFlags(intent.getFlags() | 268435456);
                Editor.this.mTextView.getContext().startActivity(intent);
                editable.removeSpan(suggestionInfo.suggestionSpan);
                Selection.setSelection(editable, spanEnd);
                Editor.this.updateSpellCheckSpans(spanStart, spanEnd, false);
            } else {
                int i;
                SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) editable.getSpans(spanStart, spanEnd, SuggestionSpan.class);
                int length = suggestionSpans.length;
                int[] suggestionSpansStarts = new int[length];
                int[] suggestionSpansEnds = new int[length];
                int[] suggestionSpansFlags = new int[length];
                for (i = 0; i < length; i++) {
                    SuggestionSpan suggestionSpan = suggestionSpans[i];
                    suggestionSpansStarts[i] = editable.getSpanStart(suggestionSpan);
                    suggestionSpansEnds[i] = editable.getSpanEnd(suggestionSpan);
                    suggestionSpansFlags[i] = editable.getSpanFlags(suggestionSpan);
                    int suggestionSpanFlags = suggestionSpan.getFlags();
                    if ((suggestionSpanFlags & 2) > 0) {
                        suggestionSpan.setFlags((suggestionSpanFlags & -3) & -2);
                    }
                }
                String suggestion = suggestionInfo.text.subSequence(suggestionInfo.suggestionStart, suggestionInfo.suggestionEnd).toString();
                Editor.this.mTextView.replaceText_internal(spanStart, spanEnd, suggestion);
                suggestionInfo.suggestionSpan.notifySelection(Editor.this.mTextView.getContext(), originalText, suggestionInfo.suggestionIndex);
                suggestionInfo.suggestionSpan.getSuggestions()[suggestionInfo.suggestionIndex] = originalText;
                int lengthDifference = suggestion.length() - (spanEnd - spanStart);
                i = 0;
                while (i < length) {
                    if (suggestionSpansStarts[i] <= spanStart && suggestionSpansEnds[i] >= spanEnd) {
                        Editor.this.mTextView.setSpan_internal(suggestionSpans[i], suggestionSpansStarts[i], suggestionSpansEnds[i] + lengthDifference, suggestionSpansFlags[i]);
                    }
                    i++;
                }
                int newCursorPosition = spanEnd + lengthDifference;
                Editor.this.mTextView.setCursorPosition_internal(newCursorPosition, newCursorPosition);
            }
            hide();
        }
    }

    private class TextActionModeCallback extends Callback2 {
        private int mHandleHeight;
        private final boolean mHasSelection;
        private final RectF mSelectionBounds = new RectF();
        private final Path mSelectionPath = new Path();
        private boolean mThemeIsDeviceDefault = false;
        private boolean mUseNewSamsungToolbar = false;

        public TextActionModeCallback(boolean hasSelection) {
            this.mHasSelection = hasSelection;
            if (this.mHasSelection) {
                SelectionModifierCursorController selectionController = Editor.this.getSelectionController();
                if (selectionController.mStartHandle == null) {
                    selectionController.initDrawables();
                    selectionController.initHandles();
                    selectionController.hide();
                }
                this.mHandleHeight = Math.max(Editor.this.mSelectHandleLeft.getMinimumHeight(), Editor.this.mSelectHandleRight.getMinimumHeight());
                return;
            }
            InsertionPointCursorController insertionController = Editor.this.getInsertionController();
            if (insertionController != null) {
                insertionController.getHandle();
                this.mHandleHeight = Editor.this.mSelectHandleCenter.getMinimumHeight();
            }
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(null);
            mode.setSubtitle(null);
            mode.setTitleOptionalHint(true);
            TypedArray a = Editor.this.mTextView.getContext().obtainStyledAttributes(new int[]{R.attr.parentIsDeviceDefault});
            this.mThemeIsDeviceDefault = a.getBoolean(0, true);
            a.recycle();
            this.mUseNewSamsungToolbar = Editor.this.mTextView.getContext().getResources().getBoolean(R.bool.tw_edit_text_new_concept);
            if (this.mThemeIsDeviceDefault && this.mUseNewSamsungToolbar) {
                menu.add(0, (int) R.id.floatingToolbarClose, 0, (CharSequence) "close").setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_close_mtrl));
            }
            populateMenuWithItems(menu);
            Callback customCallback = getCustomCallback();
            if (customCallback == null || customCallback.onCreateActionMode(mode, menu)) {
                if (Editor.this.mTextView.canProcessText()) {
                    Editor.this.mProcessTextIntentActionsHandler.onInitializeMenu(menu, this.mThemeIsDeviceDefault);
                }
                if (!menu.hasVisibleItems() && mode.getCustomView() == null) {
                    return false;
                }
                if (this.mThemeIsDeviceDefault && this.mUseNewSamsungToolbar && menu.size() <= 1) {
                    return false;
                }
                Editor.this.mTextView.setHasTransientState(true);
                return true;
            }
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd());
            return false;
        }

        private Callback getCustomCallback() {
            return this.mHasSelection ? Editor.this.mCustomSelectionActionModeCallback : Editor.this.mCustomInsertionActionModeCallback;
        }

        private void populateMenuWithItems(Menu menu) {
            if (Editor.this.mTextView.canCut()) {
                if (this.mThemeIsDeviceDefault) {
                    menu.add(0, (int) R.id.cut, 2, (int) R.string.cut).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_cut_mtrl));
                } else {
                    menu.add(0, (int) R.id.cut, 1, (int) R.string.cut);
                }
                menu.findItem(R.id.cut).setAlphabeticShortcut(StateProperty.TARGET_X).setShowAsAction(2);
            }
            if (Editor.this.mTextView.canCopy()) {
                if (this.mThemeIsDeviceDefault) {
                    menu.add(0, (int) R.id.copy, 3, (int) R.string.copy).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_copy_mtrl));
                } else {
                    menu.add(0, (int) R.id.copy, 2, (int) R.string.copy);
                }
                menu.findItem(R.id.copy).setAlphabeticShortcut('c').setShowAsAction(2);
            }
            if (Editor.this.mTextView.canPaste()) {
                if (this.mThemeIsDeviceDefault) {
                    menu.add(0, (int) R.id.paste, 4, (int) R.string.paste).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_paste_mtrl));
                } else {
                    menu.add(0, (int) R.id.paste, 3, (int) R.string.paste);
                }
                menu.findItem(R.id.paste).setAlphabeticShortcut('v').setShowAsAction(2);
            }
            if (Editor.this.mTextView.canShare()) {
                if (this.mThemeIsDeviceDefault) {
                    menu.add(0, (int) R.id.shareText, 5, (int) R.string.share).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_share_mtrl));
                } else {
                    menu.add(0, (int) R.id.shareText, 4, (int) R.string.share);
                }
                menu.findItem(R.id.shareText).setShowAsAction(1);
            }
            updateSelectAllItem(menu);
            if (this.mThemeIsDeviceDefault) {
                updateClipboardItem(menu);
                updateDictionaryItem(menu);
                updateWebSearchItem(menu);
                return;
            }
            updateReplaceItem(menu);
            updateClipboardItem(menu);
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateSelectAllItem(menu);
            if (!this.mThemeIsDeviceDefault) {
                updateReplaceItem(menu);
            }
            Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                return customCallback.onPrepareActionMode(mode, menu);
            }
            return true;
        }

        private void updateClipboardItem(Menu menu) {
            if (Editor.this.mTextView.canClipboard()) {
                menu.add(0, (int) R.id.clipboard, 6, (int) R.string.zzzzz_tw_cursor_handle_clipboard).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_clipboard_mtrl)).setShowAsAction(1);
            }
        }

        private void updateDictionaryItem(Menu menu) {
            if (Editor.this.isDictionaryEnabled()) {
                menu.add(0, (int) R.id.dictionary, 7, (int) R.string.dictionary).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_dictionary_mtrl)).setShowAsAction(1);
            }
        }

        private void updateWebSearchItem(Menu menu) {
            if (Editor.this.mTextView.canWebSearch()) {
                menu.add(0, (int) R.id.websearch, 8, (int) R.string.websearch).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_websearch_mtrl)).setShowAsAction(1);
            }
        }

        private void updateSelectAllItem(Menu menu) {
            boolean canSelectAll = Editor.this.mTextView.canSelectAllText();
            boolean selectAllItemExists;
            if (menu.findItem(R.id.selectAll) != null) {
                selectAllItemExists = true;
            } else {
                selectAllItemExists = false;
            }
            if (canSelectAll && !selectAllItemExists) {
                if (this.mThemeIsDeviceDefault) {
                    menu.add(0, (int) R.id.selectAll, 1, (int) R.string.selectAll).setIcon(Editor.this.mTextView.getContext().getResources().getDrawable(R.drawable.tw_text_edit_action_popup_selectall_mtrl));
                } else {
                    menu.add(0, (int) R.id.selectAll, 5, (int) R.string.selectAll);
                }
                menu.findItem(R.id.selectAll).setShowAsAction(1);
            } else if (!canSelectAll && selectAllItemExists) {
                menu.removeItem(R.id.selectAll);
            }
        }

        private void updateReplaceItem(Menu menu) {
            boolean canReplace;
            if (Editor.this.mTextView.isSuggestionsEnabled() && Editor.this.shouldOfferToShowSuggestions() && (!Editor.this.mTextView.isInExtractedMode() || !Editor.this.mTextView.hasSelection())) {
                canReplace = true;
            } else {
                canReplace = false;
            }
            boolean replaceItemExists;
            if (menu.findItem(R.id.replaceText) != null) {
                replaceItemExists = true;
            } else {
                replaceItemExists = false;
            }
            if (canReplace && !replaceItemExists) {
                menu.add(0, (int) R.id.replaceText, 6, (int) R.string.replace).setShowAsAction(1);
            } else if (!canReplace && replaceItemExists) {
                menu.removeItem(R.id.replaceText);
            }
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (Editor.this.mProcessTextIntentActionsHandler.performMenuItemAction(item)) {
                return true;
            }
            Callback customCallback = getCustomCallback();
            if (customCallback == null || !customCallback.onActionItemClicked(mode, item)) {
                return Editor.this.mTextView.onTextContextMenuItem(item.getItemId());
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                customCallback.onDestroyActionMode(mode);
            }
            if (!Editor.this.mPreserveDetachedSelection) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd());
                Editor.this.mTextView.setHasTransientState(false);
            }
            if (Editor.this.mSelectionModifierCursorController != null) {
                Editor.this.mSelectionModifierCursorController.hide();
            }
            Editor.this.mTextActionMode = null;
        }

        public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            if (!view.equals(Editor.this.mTextView) || Editor.this.mTextView.getLayout() == null) {
                super.onGetContentRect(mode, view, outRect);
                return;
            }
            if (Editor.this.mTextView.getSelectionStart() != Editor.this.mTextView.getSelectionEnd()) {
                this.mSelectionPath.reset();
                Editor.this.mTextView.getLayout().getSelectionPath(Editor.this.mTextView.getSelectionStart(), Editor.this.mTextView.getSelectionEnd(), this.mSelectionPath);
                this.mSelectionPath.computeBounds(this.mSelectionBounds, true);
                RectF rectF = this.mSelectionBounds;
                rectF.bottom += (float) this.mHandleHeight;
            } else if (Editor.this.mCursorCount == 2) {
                Rect firstCursorBounds = Editor.this.mCursorDrawable[0].getBounds();
                Rect secondCursorBounds = Editor.this.mCursorDrawable[1].getBounds();
                this.mSelectionBounds.set((float) Math.min(firstCursorBounds.left, secondCursorBounds.left), (float) Math.min(firstCursorBounds.top, secondCursorBounds.top), (float) Math.max(firstCursorBounds.right, secondCursorBounds.right), (float) (Math.max(firstCursorBounds.bottom, secondCursorBounds.bottom) + this.mHandleHeight));
            } else {
                Layout layout = Editor.this.getActiveLayout();
                int line = layout.getLineForOffset(Editor.this.mTextView.getSelectionStart());
                float primaryHorizontal = layout.getPrimaryHorizontal(Editor.this.mTextView.getSelectionStart());
                this.mSelectionBounds.set(primaryHorizontal, (float) (layout.getLineTop(line) + Editor.this.mPopupBelowMarginDifSelectAndInsert), ((float) Editor.this.mTextView.mCursorWidth) + primaryHorizontal, (float) ((layout.getLineTop(line + 1) + this.mHandleHeight) + Editor.this.mInsertActionHandleTopMargin));
            }
            int textHorizontalOffset = Editor.this.mTextView.viewportToContentHorizontalOffset();
            int textVerticalOffset = Editor.this.mTextView.viewportToContentVerticalOffset();
            outRect.set((int) Math.floor((double) (this.mSelectionBounds.left + ((float) textHorizontalOffset))), (int) Math.floor((double) (this.mSelectionBounds.top + ((float) textVerticalOffset))), (int) Math.ceil((double) (this.mSelectionBounds.right + ((float) textHorizontalOffset))), (int) Math.ceil((double) (this.mSelectionBounds.bottom + ((float) textVerticalOffset))));
        }
    }

    private static class TextRenderNode {
        boolean isDirty = true;
        RenderNode renderNode;

        public TextRenderNode(String name) {
            this.renderNode = RenderNode.create(name, null);
        }

        boolean needsRecord() {
            return this.isDirty || !this.renderNode.isValid();
        }
    }

    public static class UndoInputFilter implements InputFilter {
        private final Editor mEditor;
        private boolean mHasComposition;
        private boolean mIsUserEdit;

        public UndoInputFilter(Editor editor) {
            this.mEditor = editor;
        }

        public void saveInstanceState(Parcel parcel) {
            int i;
            int i2 = 1;
            if (this.mIsUserEdit) {
                i = 1;
            } else {
                i = 0;
            }
            parcel.writeInt(i);
            if (!this.mHasComposition) {
                i2 = 0;
            }
            parcel.writeInt(i2);
        }

        public void restoreInstanceState(Parcel parcel) {
            boolean z;
            boolean z2 = true;
            if (parcel.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            this.mIsUserEdit = z;
            if (parcel.readInt() == 0) {
                z2 = false;
            }
            this.mHasComposition = z2;
        }

        public void beginBatchEdit() {
            this.mIsUserEdit = true;
        }

        public void endBatchEdit() {
            this.mIsUserEdit = false;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (canUndoEdit(source, start, end, dest, dstart, dend) && !handleCompositionEdit(source, start, end, dstart)) {
                handleKeyboardEdit(source, start, end, dest, dstart, dend);
            }
            return null;
        }

        private boolean handleCompositionEdit(CharSequence source, int start, int end, int dstart) {
            if (isComposition(source)) {
                this.mHasComposition = true;
                return true;
            }
            boolean hadComposition = this.mHasComposition;
            this.mHasComposition = false;
            if (!hadComposition) {
                return false;
            }
            if (start == end) {
                return true;
            }
            recordEdit(new EditOperation(this.mEditor, "", dstart, TextUtils.substring(source, start, end)), false);
            return true;
        }

        private void handleKeyboardEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            boolean forceMerge = isInTextWatcher();
            String newText = TextUtils.substring(source, start, end);
            recordEdit(new EditOperation(this.mEditor, TextUtils.substring(dest, dstart, dend), dstart, newText), forceMerge);
        }

        private void recordEdit(EditOperation edit, boolean forceMerge) {
            UndoManager um = this.mEditor.mUndoManager;
            um.beginUpdate("Edit text");
            EditOperation lastEdit = (EditOperation) um.getLastOperation(EditOperation.class, this.mEditor.mUndoOwner, 1);
            if (lastEdit == null) {
                um.addOperation(edit, 0);
            } else if (forceMerge) {
                lastEdit.forceMergeWith(edit);
            } else if (!this.mIsUserEdit) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            } else if (!lastEdit.mergeWith(edit)) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            }
            um.endUpdate();
        }

        private boolean canUndoEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!this.mEditor.mAllowUndo || this.mEditor.mUndoManager.isInUndo() || !Editor.isValidRange(source, start, end) || !Editor.isValidRange(dest, dstart, dend)) {
                return false;
            }
            if (start == end && dstart == dend) {
                return false;
            }
            return true;
        }

        private boolean isComposition(CharSequence source) {
            if (!(source instanceof Spannable)) {
                return false;
            }
            Spannable text = (Spannable) source;
            if (BaseInputConnection.getComposingSpanStart(text) < BaseInputConnection.getComposingSpanEnd(text)) {
                return true;
            }
            return false;
        }

        private boolean isInTextWatcher() {
            CharSequence text = this.mEditor.mTextView.getText();
            return (text instanceof SpannableStringBuilder) && ((SpannableStringBuilder) text).getTextWatcherDepth() > 0;
        }
    }

    Editor(TextView textView) {
        this.mTextView = textView;
        this.mTextView.setFilters(this.mTextView.getFilters());
        this.mProcessTextIntentActionsHandler = new ProcessTextIntentActionsHandler();
        this.isSecClipboardEnabled = this.mTextView.getClipboardExManager().isEnabled();
        TypedArray a = this.mTextView.getContext().obtainStyledAttributes(new int[]{R.attr.parentIsDeviceDefault});
        if (a.getBoolean(0, true)) {
            this.mThemeIsDeviceDefault = true;
            this.mPopupBelowMarginDifSelectAndInsert = this.mTextView.getResources().getDimensionPixelSize(R.dimen.tw_text_edit_action_popup_bottom_margin) - this.mTextView.getResources().getDimensionPixelSize(R.dimen.tw_text_insert_action_popup_bottom_margin);
            this.mInsertActionHandleTopMargin = this.mTextView.getResources().getDimensionPixelSize(R.dimen.tw_text_insert_action_handle_top_margin);
        } else {
            this.mThemeIsDeviceDefault = false;
        }
        a.recycle();
    }

    ParcelableParcel saveInstanceState() {
        ParcelableParcel state = new ParcelableParcel(getClass().getClassLoader());
        Parcel parcel = state.getParcel();
        this.mUndoManager.saveInstanceState(parcel);
        this.mUndoInputFilter.saveInstanceState(parcel);
        return state;
    }

    void restoreInstanceState(ParcelableParcel state) {
        Parcel parcel = state.getParcel();
        this.mUndoManager.restoreInstanceState(parcel, state.getClassLoader());
        this.mUndoInputFilter.restoreInstanceState(parcel);
        this.mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    }

    void forgetUndoRedo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        this.mUndoManager.forgetUndos(owners, -1);
        this.mUndoManager.forgetRedos(owners, -1);
    }

    boolean canUndo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        if (!this.mAllowUndo || this.mUndoManager.countUndos(owners) <= 0) {
            return false;
        }
        return true;
    }

    boolean canRedo() {
        UndoOwner[] owners = new UndoOwner[]{this.mUndoOwner};
        if (!this.mAllowUndo || this.mUndoManager.countRedos(owners) <= 0) {
            return false;
        }
        return true;
    }

    void undo() {
        if (this.mAllowUndo) {
            this.mUndoManager.undo(new UndoOwner[]{this.mUndoOwner}, 1);
        }
    }

    void redo() {
        if (this.mAllowUndo) {
            this.mUndoManager.redo(new UndoOwner[]{this.mUndoOwner}, 1);
        }
    }

    void replace() {
        int middle = (this.mTextView.getSelectionStart() + this.mTextView.getSelectionEnd()) / 2;
        stopTextActionMode();
        Selection.setSelection((Spannable) this.mTextView.getText(), middle);
        showSuggestions();
    }

    void onAttachedToWindow() {
        if (this.mShowErrorAfterAttach) {
            showError();
            this.mShowErrorAfterAttach = false;
        }
        this.mTemporaryDetach = false;
        ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
        if (this.mInsertionPointCursorController != null) {
            observer.addOnTouchModeChangeListener(this.mInsertionPointCursorController);
        }
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.resetTouchOffsets();
            observer.addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
        }
        updateSpellCheckSpans(0, this.mTextView.getText().length(), true);
        if (this.mTextView.hasTransientState() && this.mTextView.getSelectionStart() != this.mTextView.getSelectionEnd()) {
            this.mTextView.setHasTransientState(false);
            startSelectionActionMode();
        }
        getPositionListener().addSubscriber(this.mCursorAnchorInfoNotifier, true);
        resumeBlink();
    }

    void onDetachedFromWindow() {
        getPositionListener().removeSubscriber(this.mCursorAnchorInfoNotifier);
        if (this.mError != null) {
            hideError();
        }
        suspendBlink();
        if (this.mInsertionPointCursorController != null) {
            this.mInsertionPointCursorController.onDetached();
        }
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.onDetached();
        }
        if (this.mShowSuggestionRunnable != null) {
            this.mTextView.removeCallbacks(this.mShowSuggestionRunnable);
        }
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
        destroyDisplayListsData();
        if (this.mSpellChecker != null) {
            this.mSpellChecker.closeSession();
            this.mSpellChecker = null;
        }
        this.mPreserveDetachedSelection = true;
        hideCursorAndSpanControllers();
        stopTextActionMode();
        this.mPreserveDetachedSelection = false;
        this.mTemporaryDetach = false;
    }

    private void destroyDisplayListsData() {
        if (this.mTextRenderNodes != null) {
            for (int i = 0; i < this.mTextRenderNodes.length; i++) {
                RenderNode displayList = this.mTextRenderNodes[i] != null ? this.mTextRenderNodes[i].renderNode : null;
                if (displayList != null && displayList.isValid()) {
                    displayList.destroyDisplayListData();
                }
            }
        }
    }

    private void showError() {
        if (this.mTextView.getWindowToken() == null) {
            this.mShowErrorAfterAttach = true;
            return;
        }
        if (this.mErrorPopup == null) {
            TextView err;
            LayoutInflater inflater = LayoutInflater.from(this.mTextView.getContext());
            if (this.mThemeIsDeviceDefault) {
                err = (TextView) inflater.inflate(17367384, null);
            } else {
                err = (TextView) inflater.inflate(17367312, null);
            }
            float scale = this.mTextView.getResources().getDisplayMetrics().density;
            this.mErrorPopup = new ErrorPopup(err, (int) ((200.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS), (int) ((50.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
            this.mErrorPopup.setFocusable(false);
            this.mErrorPopup.setOutsideTouchable(true);
            this.mErrorPopup.setInputMethodMode(1);
        }
        TextView tv = (TextView) this.mErrorPopup.getContentView();
        chooseSize(this.mErrorPopup, this.mError, tv);
        tv.setText(this.mError);
        this.mErrorPopup.showAsDropDown(this.mTextView, getErrorX(), getErrorY());
        this.mErrorPopup.fixDirection(this.mErrorPopup.isAboveAnchor());
    }

    public void setError(CharSequence error, Drawable icon) {
        this.mError = TextUtils.stringOrSpannedString(error);
        this.mErrorWasChanged = true;
        if (this.mError == null) {
            setErrorIcon(null);
            if (this.mErrorPopup != null) {
                if (this.mErrorPopup.isShowing()) {
                    this.mErrorPopup.dismiss();
                }
                this.mErrorPopup = null;
            }
            this.mShowErrorAfterAttach = false;
            return;
        }
        setErrorIcon(icon);
        if (this.mTextView.isFocused()) {
            showError();
        }
    }

    private void setErrorIcon(Drawable icon) {
        Drawables dr = this.mTextView.mDrawables;
        if (dr == null) {
            TextView textView = this.mTextView;
            dr = new Drawables(this.mTextView.getContext());
            textView.mDrawables = dr;
        }
        dr.setErrorDrawable(icon, this.mTextView);
        this.mTextView.resetResolvedDrawables();
        this.mTextView.invalidate();
        this.mTextView.requestLayout();
    }

    private void hideError() {
        if (this.mErrorPopup != null && this.mErrorPopup.isShowing()) {
            this.mErrorPopup.dismiss();
        }
        this.mShowErrorAfterAttach = false;
    }

    private int getErrorX() {
        int i = 0;
        float scale = this.mTextView.getResources().getDisplayMetrics().density;
        Drawables dr = this.mTextView.mDrawables;
        switch (this.mTextView.getLayoutDirection()) {
            case 1:
                if (dr != null) {
                    i = dr.mDrawableSizeLeft;
                }
                return ((((i / 2) - ((int) ((23.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS))) - this.mTextView.getWidth()) + this.mErrorPopup.getWidth()) + this.mTextView.getPaddingEnd();
            default:
                if (dr != null) {
                    i = dr.mDrawableSizeRight;
                }
                return ((this.mTextView.getWidth() - this.mErrorPopup.getWidth()) - this.mTextView.getPaddingEnd()) + (((-i) / 2) + ((int) ((23.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS)));
        }
    }

    private int getErrorY() {
        int height = 0;
        int compoundPaddingTop = this.mTextView.getCompoundPaddingTop();
        int vspace = ((this.mTextView.getBottom() - this.mTextView.getTop()) - this.mTextView.getCompoundPaddingBottom()) - compoundPaddingTop;
        Drawables dr = this.mTextView.mDrawables;
        switch (this.mTextView.getLayoutDirection()) {
            case 1:
                if (dr != null) {
                    height = dr.mDrawableHeightLeft;
                }
                break;
            default:
                if (dr != null) {
                    height = dr.mDrawableHeightRight;
                    break;
                }
                break;
        }
        return (((compoundPaddingTop + ((vspace - height) / 2)) + height) - this.mTextView.getHeight()) - ((int) ((2.0f * this.mTextView.getResources().getDisplayMetrics().density) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
    }

    void createInputContentTypeIfNeeded() {
        if (this.mInputContentType == null) {
            this.mInputContentType = new InputContentType();
        }
    }

    void createInputMethodStateIfNeeded() {
        if (this.mInputMethodState == null) {
            this.mInputMethodState = new InputMethodState();
        }
    }

    boolean isCursorVisible() {
        return this.mCursorVisible && this.mTextView.isTextEditable();
    }

    void prepareCursorControllers() {
        boolean enabled;
        boolean z;
        boolean z2 = true;
        boolean windowSupportsHandles = false;
        LayoutParams params = this.mTextView.getRootView().getLayoutParams();
        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams windowParams = (WindowManager.LayoutParams) params;
            if (windowParams.type < 1000 || windowParams.type > WindowManager.LayoutParams.LAST_SUB_WINDOW) {
                windowSupportsHandles = true;
            } else {
                windowSupportsHandles = false;
            }
        }
        if (!windowSupportsHandles || this.mTextView.getLayout() == null) {
            enabled = false;
        } else {
            enabled = true;
        }
        if (enabled && isCursorVisible()) {
            z = true;
        } else {
            z = false;
        }
        this.mInsertionControllerEnabled = z;
        if (!(enabled && this.mTextView.textCanBeSelected())) {
            z2 = false;
        }
        this.mSelectionControllerEnabled = z2;
        if (!this.mInsertionControllerEnabled) {
            hideInsertionPointCursorController();
            if (this.mInsertionPointCursorController != null) {
                this.mInsertionPointCursorController.onDetached();
                this.mInsertionPointCursorController = null;
            }
        }
        if (!this.mSelectionControllerEnabled) {
            stopTextActionMode();
            if (this.mSelectionModifierCursorController != null) {
                this.mSelectionModifierCursorController.onDetached();
                this.mSelectionModifierCursorController = null;
            }
        }
    }

    void hideInsertionPointCursorController() {
        if (this.mInsertionPointCursorController != null) {
            this.mInsertionPointCursorController.hide();
        }
    }

    void hideCursorAndSpanControllers() {
        hideCursorControllers();
        hideSpanControllers();
    }

    private void hideSpanControllers() {
        if (this.mSpanController != null) {
            this.mSpanController.hide();
        }
    }

    private void hideCursorControllers() {
        if (this.mSuggestionsPopupWindow != null && (this.mTextView.isInExtractedMode() || !this.mSuggestionsPopupWindow.isShowingUp())) {
            this.mSuggestionsPopupWindow.hide();
        }
        hideInsertionPointCursorController();
        stopTextActionMode();
    }

    private void updateSpellCheckSpans(int start, int end, boolean createSpellChecker) {
        this.mTextView.removeAdjacentSuggestionSpans(start);
        this.mTextView.removeAdjacentSuggestionSpans(end);
        if (this.mTextView.isTextEditable() && this.mTextView.isSuggestionsEnabled() && !this.mTextView.isInExtractedMode()) {
            if (this.mSpellChecker == null && createSpellChecker) {
                this.mSpellChecker = new SpellChecker(this.mTextView);
            }
            if (this.mSpellChecker != null) {
                this.mSpellChecker.spellCheck(start, end);
            }
        }
    }

    void onScreenStateChanged(int screenState) {
        switch (screenState) {
            case 0:
                suspendBlink();
                return;
            case 1:
                resumeBlink();
                return;
            default:
                return;
        }
    }

    private void suspendBlink() {
        if (this.mBlink != null) {
            this.mBlink.cancel();
        }
    }

    private void resumeBlink() {
        if (this.mBlink != null) {
            this.mBlink.uncancel();
            makeBlink();
        }
    }

    void adjustInputType(boolean password, boolean passwordInputType, boolean webPasswordInputType, boolean numberPasswordInputType) {
        if ((this.mInputType & 15) == 1) {
            if (password || passwordInputType) {
                this.mInputType = (this.mInputType & -4081) | 128;
            }
            if (webPasswordInputType) {
                this.mInputType = (this.mInputType & -4081) | 224;
            }
        } else if ((this.mInputType & 15) == 2 && numberPasswordInputType) {
            this.mInputType = (this.mInputType & -4081) | 16;
        }
    }

    private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
        int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        int ht = tv.getPaddingTop() + tv.getPaddingBottom();
        CharSequence charSequence = text;
        Layout l = new StaticLayout(charSequence, tv.getPaint(), this.mTextView.getResources().getDimensionPixelSize(R.dimen.textview_error_popup_default_width), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        float max = 0.0f;
        for (int i = 0; i < l.getLineCount(); i++) {
            max = Math.max(max, l.getLineWidth(i));
        }
        pop.setWidth(((int) Math.ceil((double) max)) + wid);
        pop.setHeight(l.getHeight() + ht);
    }

    void setFrame() {
        if (this.mErrorPopup != null) {
            chooseSize(this.mErrorPopup, this.mError, (TextView) this.mErrorPopup.getContentView());
            this.mErrorPopup.update(this.mTextView, getErrorX(), getErrorY(), this.mErrorPopup.getWidth(), this.mErrorPopup.getHeight());
        }
    }

    private int getWordStart(int offset) {
        int retOffset;
        if (getWordIteratorWithText().isOnPunctuation(getWordIteratorWithText().prevBoundary(offset))) {
            retOffset = getWordIteratorWithText().getPunctuationBeginning(offset);
        } else {
            retOffset = getWordIteratorWithText().getPrevWordBeginningOnTwoWordsBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    private int getWordEnd(int offset) {
        int retOffset;
        if (getWordIteratorWithText().isAfterPunctuation(getWordIteratorWithText().nextBoundary(offset))) {
            retOffset = getWordIteratorWithText().getPunctuationEnd(offset);
        } else {
            retOffset = getWordIteratorWithText().getNextWordEndOnTwoWordBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    private boolean selectCurrentWord() {
        if (!this.mTextView.canSelectText()) {
            return false;
        }
        if (this.mTextView.hasPasswordTransformationMethod()) {
            return this.mTextView.selectAllText();
        }
        this.mTextView.clearAllMultiSelection();
        int inputType = this.mTextView.getInputType();
        int klass = inputType & 15;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        if (klass == 2 || klass == 3 || klass == 4 || variation == 16 || variation == 32 || variation == 208 || variation == 176) {
            return this.mTextView.selectAllText();
        }
        ReplacementSpan[] replacementSpans;
        long lastTouchOffsets = getLastTouchOffsets();
        int minOffset = TextUtils.unpackRangeStartFromLong(lastTouchOffsets);
        int maxOffset = TextUtils.unpackRangeEndFromLong(lastTouchOffsets);
        if (minOffset == maxOffset && maxOffset == this.mTextView.getText().length()) {
            replacementSpans = (ReplacementSpan[]) ((Spanned) this.mTextView.getText()).getSpans(minOffset, maxOffset, ReplacementSpan.class);
            if (replacementSpans.length >= 1) {
                int selStart = ((Spanned) this.mTextView.getText()).getSpanStart(replacementSpans[0]);
                int selEnd = ((Spanned) this.mTextView.getText()).getSpanEnd(replacementSpans[0]);
                Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
                return selEnd > selStart;
            }
        }
        if (minOffset < 0 || minOffset >= this.mTextView.getText().length()) {
            return false;
        }
        if (maxOffset < 0 || maxOffset >= this.mTextView.getText().length()) {
            return false;
        }
        int selectionStart;
        int selectionEnd;
        URLSpan[] urlSpans = (URLSpan[]) ((Spanned) this.mTextView.getText()).getSpans(minOffset, maxOffset, URLSpan.class);
        replacementSpans = (ReplacementSpan[]) ((Spanned) this.mTextView.getText()).getSpans(minOffset, maxOffset, ReplacementSpan.class);
        if (urlSpans.length >= 1) {
            URLSpan urlSpan = urlSpans[0];
            selectionStart = ((Spanned) this.mTextView.getText()).getSpanStart(urlSpan);
            selectionEnd = ((Spanned) this.mTextView.getText()).getSpanEnd(urlSpan);
        } else if (replacementSpans.length >= 1) {
            ReplacementSpan replacementSpan = replacementSpans[0];
            selectionStart = ((Spanned) this.mTextView.getText()).getSpanStart(replacementSpan);
            selectionEnd = ((Spanned) this.mTextView.getText()).getSpanEnd(replacementSpan);
        } else {
            WordIterator wordIterator = getWordIterator();
            wordIterator.setCharSequence(this.mTextView.getText(), minOffset, maxOffset);
            selectionStart = wordIterator.getBeginning(minOffset);
            selectionEnd = wordIterator.getEnd(maxOffset);
            if (selectionStart == -1 || selectionEnd == -1 || selectionStart == selectionEnd) {
                long range = getCharClusterRange(minOffset);
                selectionStart = TextUtils.unpackRangeStartFromLong(range);
                selectionEnd = TextUtils.unpackRangeEndFromLong(range);
            }
        }
        Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
        if (selectionEnd > selectionStart) {
            return true;
        }
        return false;
    }

    void onLocaleChanged() {
        this.mWordIterator = null;
        this.mWordIteratorWithText = null;
    }

    public WordIterator getWordIterator() {
        if (this.mWordIterator == null) {
            this.mWordIterator = new WordIterator(this.mTextView.getTextServicesLocale());
        }
        return this.mWordIterator;
    }

    private WordIterator getWordIteratorWithText() {
        if (this.mWordIteratorWithText == null) {
            this.mWordIteratorWithText = new WordIterator(this.mTextView.getTextServicesLocale());
            this.mUpdateWordIteratorText = true;
        }
        if (this.mUpdateWordIteratorText) {
            CharSequence text = this.mTextView.getText();
            this.mWordIteratorWithText.setCharSequence(text, 0, text.length());
            this.mUpdateWordIteratorText = false;
        }
        return this.mWordIteratorWithText;
    }

    private int getNextCursorOffset(int offset, boolean findAfterGivenOffset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return offset;
        }
        int i;
        CharSequence text = this.mTextView.getText();
        TextPaint paint = layout.getPaint();
        int length = text.length();
        if (layout.isRtlCharAt(offset)) {
            i = 1;
        } else {
            i = 0;
        }
        int nextOffset = paint.getTextRunCursor(text, 0, length, i, offset, findAfterGivenOffset ? 0 : 2);
        return nextOffset != -1 ? nextOffset : offset;
    }

    private long getCharClusterRange(int offset) {
        if (offset < this.mTextView.getText().length()) {
            return TextUtils.packRangeInLong(offset, getNextCursorOffset(offset, true));
        }
        if (offset - 1 >= 0) {
            return TextUtils.packRangeInLong(getNextCursorOffset(offset, false), offset);
        }
        return TextUtils.packRangeInLong(offset, offset);
    }

    private boolean touchPositionIsInSelection() {
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        if (selectionStart == selectionEnd) {
            return false;
        }
        if (selectionStart > selectionEnd) {
            int tmp = selectionStart;
            selectionStart = selectionEnd;
            selectionEnd = tmp;
            Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
        }
        SelectionModifierCursorController selectionController = getSelectionController();
        boolean z = selectionController.getMinTouchOffset() >= selectionStart && selectionController.getMaxTouchOffset() < selectionEnd;
        return z;
    }

    private PositionListener getPositionListener() {
        if (this.mPositionListener == null) {
            this.mPositionListener = new PositionListener();
        }
        return this.mPositionListener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isPositionVisible(float r12, float r13) {
        /*
        r11 = this;
        r10 = 0;
        r5 = 1;
        r4 = 0;
        r6 = TEMP_POSITION;
        monitor-enter(r6);
        r2 = TEMP_POSITION;	 Catch:{ all -> 0x008d }
        r7 = 0;
        r2[r7] = r12;	 Catch:{ all -> 0x008d }
        r7 = 1;
        r2[r7] = r13;	 Catch:{ all -> 0x008d }
        r3 = r11.mTextView;	 Catch:{ all -> 0x008d }
    L_0x0010:
        if (r3 == 0) goto L_0x008a;
    L_0x0012:
        r7 = r11.mTextView;	 Catch:{ all -> 0x008d }
        if (r3 == r7) goto L_0x002c;
    L_0x0016:
        r7 = 0;
        r8 = r2[r7];	 Catch:{ all -> 0x008d }
        r9 = r3.getScrollX();	 Catch:{ all -> 0x008d }
        r9 = (float) r9;	 Catch:{ all -> 0x008d }
        r8 = r8 - r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008d }
        r7 = 1;
        r8 = r2[r7];	 Catch:{ all -> 0x008d }
        r9 = r3.getScrollY();	 Catch:{ all -> 0x008d }
        r9 = (float) r9;	 Catch:{ all -> 0x008d }
        r8 = r8 - r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008d }
    L_0x002c:
        r7 = 0;
        r7 = r2[r7];	 Catch:{ all -> 0x008d }
        r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1));
        if (r7 < 0) goto L_0x0052;
    L_0x0033:
        r7 = 1;
        r7 = r2[r7];	 Catch:{ all -> 0x008d }
        r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1));
        if (r7 < 0) goto L_0x0052;
    L_0x003a:
        r7 = 0;
        r7 = r2[r7];	 Catch:{ all -> 0x008d }
        r8 = r3.getWidth();	 Catch:{ all -> 0x008d }
        r8 = (float) r8;	 Catch:{ all -> 0x008d }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 > 0) goto L_0x0052;
    L_0x0046:
        r7 = 1;
        r7 = r2[r7];	 Catch:{ all -> 0x008d }
        r8 = r3.getHeight();	 Catch:{ all -> 0x008d }
        r8 = (float) r8;	 Catch:{ all -> 0x008d }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 <= 0) goto L_0x0054;
    L_0x0052:
        monitor-exit(r6);	 Catch:{ all -> 0x008d }
    L_0x0053:
        return r4;
    L_0x0054:
        r7 = r3.getMatrix();	 Catch:{ all -> 0x008d }
        r7 = r7.isIdentity();	 Catch:{ all -> 0x008d }
        if (r7 != 0) goto L_0x0065;
    L_0x005e:
        r7 = r3.getMatrix();	 Catch:{ all -> 0x008d }
        r7.mapPoints(r2);	 Catch:{ all -> 0x008d }
    L_0x0065:
        r7 = 0;
        r8 = r2[r7];	 Catch:{ all -> 0x008d }
        r9 = r3.getLeft();	 Catch:{ all -> 0x008d }
        r9 = (float) r9;	 Catch:{ all -> 0x008d }
        r8 = r8 + r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008d }
        r7 = 1;
        r8 = r2[r7];	 Catch:{ all -> 0x008d }
        r9 = r3.getTop();	 Catch:{ all -> 0x008d }
        r9 = (float) r9;	 Catch:{ all -> 0x008d }
        r8 = r8 + r9;
        r2[r7] = r8;	 Catch:{ all -> 0x008d }
        r1 = r3.getParent();	 Catch:{ all -> 0x008d }
        r7 = r1 instanceof android.view.View;	 Catch:{ all -> 0x008d }
        if (r7 == 0) goto L_0x0088;
    L_0x0083:
        r0 = r1;
        r0 = (android.view.View) r0;	 Catch:{ all -> 0x008d }
        r3 = r0;
        goto L_0x0010;
    L_0x0088:
        r3 = 0;
        goto L_0x0010;
    L_0x008a:
        monitor-exit(r6);	 Catch:{ all -> 0x008d }
        r4 = r5;
        goto L_0x0053;
    L_0x008d:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x008d }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.isPositionVisible(float, float):boolean");
    }

    private boolean isOffsetVisible(int offset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        return isPositionVisible((float) (this.mTextView.viewportToContentHorizontalOffset() + ((int) layout.getPrimaryHorizontal(offset))), (float) (this.mTextView.viewportToContentVerticalOffset() + layout.getLineBottom(layout.getLineForOffset(offset))));
    }

    private boolean isPositionOnText(float x, float y) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = this.mTextView.getLineAtCoordinate(y);
        x = this.mTextView.convertToLocalHorizontalCoordinate(x);
        if (x < layout.getLineLeft(line) || x > layout.getLineRight(line)) {
            return false;
        }
        return true;
    }

    public boolean performLongClick(boolean handled) {
        if (!(handled || isPositionOnText(this.mLastDownPositionX, this.mLastDownPositionY) || !this.mInsertionControllerEnabled)) {
            int offset = this.mTextView.getOffsetForPosition(this.mLastDownPositionX, this.mLastDownPositionY);
            stopTextActionMode();
            if (!this.mTextView.isFocused()) {
                this.mTextView.requestFocus();
            }
            Selection.setSelection((Spannable) this.mTextView.getText(), offset);
            getInsertionController().show();
            startInsertionActionMode();
            handled = true;
        }
        if (!(handled || this.mTextActionMode == null)) {
            if (touchPositionIsInSelection()) {
                int start = this.mTextView.getSelectionStart();
                int end = this.mTextView.getSelectionEnd();
                CharSequence selectedText = this.mTextView.getTransformedText(start, end);
                this.mTextView.startDrag(ClipData.newPlainText(null, selectedText), getTextThumbnailBuilder(selectedText), new DragLocalState(this.mTextView, start, end), 1);
                stopTextActionMode();
            } else {
                stopTextActionMode();
                selectCurrentWordAndStartDrag();
                startSelectionActionMode();
                getSelectionController().hide();
            }
            handled = true;
        }
        if (!handled) {
            handled = selectCurrentWordAndStartDrag();
            startSelectionActionMode();
            if (getSelectionController() != null) {
                getSelectionController().hide();
            }
        }
        return handled;
    }

    private long getLastTouchOffsets() {
        SelectionModifierCursorController selectionController = getSelectionController();
        return TextUtils.packRangeInLong(selectionController.getMinTouchOffset(), selectionController.getMaxTouchOffset());
    }

    void onFocusChanged(boolean focused, int direction) {
        this.mShowCursor = SystemClock.uptimeMillis();
        ensureEndedBatchEdit();
        int selStart;
        int selEnd;
        if (focused) {
            boolean z;
            selStart = this.mTextView.getSelectionStart();
            selEnd = this.mTextView.getSelectionEnd();
            boolean isFocusHighlighted;
            if (this.mSelectAllOnFocus && selStart == 0 && selEnd == this.mTextView.getText().length()) {
                isFocusHighlighted = true;
            } else {
                isFocusHighlighted = false;
            }
            if (this.mFrozenWithFocus && this.mTextView.hasSelection() && !isFocusHighlighted) {
                z = true;
            } else {
                z = false;
            }
            this.mCreatedWithASelection = z;
            if (!this.mFrozenWithFocus || selStart < 0 || selEnd < 0) {
                int lastTapPosition = getLastTapPosition();
                if (lastTapPosition >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), lastTapPosition);
                }
                MovementMethod mMovement = this.mTextView.getMovementMethod();
                if (mMovement != null) {
                    mMovement.onTakeFocus(this.mTextView, (Spannable) this.mTextView.getText(), direction);
                }
                if ((this.mTextView.isInExtractedMode() || this.mSelectionMoved) && selStart >= 0 && selEnd >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
                }
                if (this.mSelectAllOnFocus) {
                    this.mTextView.selectAllText();
                }
                this.mTouchFocusSelected = true;
            }
            this.mFrozenWithFocus = false;
            this.mSelectionMoved = false;
            if (this.mError != null) {
                showError();
            }
            makeBlink();
            return;
        }
        if (this.mError != null) {
            hideError();
        }
        this.mTextView.onEndBatchEdit();
        if (this.mTextView.isInExtractedMode()) {
            selStart = this.mTextView.getSelectionStart();
            selEnd = this.mTextView.getSelectionEnd();
            hideCursorAndSpanControllers();
            stopTextActionMode();
            Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
        } else {
            if (this.mTemporaryDetach) {
                this.mPreserveDetachedSelection = true;
            }
            hideCursorAndSpanControllers();
            stopTextActionMode();
            if (this.mTemporaryDetach) {
                this.mPreserveDetachedSelection = false;
            }
            downgradeEasyCorrectionSpans();
        }
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.resetTouchOffsets();
        }
    }

    private void downgradeEasyCorrectionSpans() {
        CharSequence text = this.mTextView.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
            for (int i = 0; i < suggestionSpans.length; i++) {
                int flags = suggestionSpans[i].getFlags();
                if ((flags & 1) != 0 && (flags & 2) == 0) {
                    suggestionSpans[i].setFlags(flags & -2);
                }
            }
        }
    }

    void sendOnTextChanged(int start, int after) {
        updateSpellCheckSpans(start, start + after, false);
        this.mUpdateWordIteratorText = true;
        removeMessage();
        hideCursorControllers();
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.resetTouchOffsets();
        }
        stopTextActionMode();
    }

    private int getLastTapPosition() {
        if (this.mSelectionModifierCursorController != null) {
            int lastTapPosition = this.mSelectionModifierCursorController.getMinTouchOffset();
            if (lastTapPosition >= 0) {
                if (lastTapPosition > this.mTextView.getText().length()) {
                    return this.mTextView.getText().length();
                }
                return lastTapPosition;
            }
        }
        return -1;
    }

    void onWindowFocusChanged(boolean hasWindowFocus) {
        boolean immFullScreen = false;
        if (hasWindowFocus) {
            if (this.mBlink != null) {
                this.mBlink.uncancel();
                makeBlink();
            }
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null && imm.isFullscreenMode()) {
                immFullScreen = true;
            }
            if (this.mSelectionModifierCursorController != null && this.mTextView.hasSelection() && !immFullScreen && this.mTextActionMode != null) {
                this.mSelectionModifierCursorController.show();
                return;
            }
            return;
        }
        if (this.mBlink != null) {
            this.mBlink.cancel();
        }
        if (this.mInputContentType != null) {
            this.mInputContentType.enterDown = false;
        }
        hideCursorAndSpanControllers();
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.hide();
        }
        if (this.mSuggestionsPopupWindow != null) {
            this.mSuggestionsPopupWindow.onParentLostFocus();
        }
        ensureEndedBatchEdit();
    }

    void onTouchEvent(MotionEvent event) {
        updateFloatingToolbarVisibility(event);
        if (hasSelectionController()) {
            getSelectionController().onTouchEvent(event);
        }
        if (this.mShowSuggestionRunnable != null) {
            this.mTextView.removeCallbacks(this.mShowSuggestionRunnable);
            this.mShowSuggestionRunnable = null;
        }
        switch (event.getActionMasked()) {
            case 0:
                this.mLastDownPositionX = event.getX();
                this.mLastDownPositionY = event.getY();
                this.mTouchFocusSelected = false;
                this.mIgnoreActionUpEvent = false;
                return;
            case 1:
                if (this.mShowSoftInputOnFocus && !this.mTextView.getHideSoftInput() && this.mTextView.isTextEditable()) {
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    if (imm != null) {
                        imm.showSoftInput(this.mTextView, 0, null);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void updateFloatingToolbarVisibility(MotionEvent event) {
        if (this.mTextActionMode != null) {
            switch (event.getActionMasked()) {
                case 1:
                case 3:
                    showFloatingToolbar();
                    return;
                case 2:
                    if (!hasSelectionController() || !getSelectionController().isSelectHandlerNotMoved() || getSelectionController().isDragAcceleratorActive()) {
                        ViewConfiguration viewConfig = ViewConfiguration.get(this.mTextView.getContext());
                        float deltaX = event.getX() - this.mLastDownPositionX;
                        float deltaY = event.getY() - this.mLastDownPositionY;
                        float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        int touchSlop = viewConfig.getScaledTouchSlop();
                        if (distanceSquared > ((float) (touchSlop * touchSlop))) {
                            hideFloatingToolbar();
                            selectCurrentWordAndStartDrag();
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

    private void hideFloatingToolbar() {
        if (this.mTextActionMode != null) {
            this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
            this.mTextActionMode.hide(-1);
        }
    }

    private void showFloatingToolbar() {
        if (this.mTextActionMode != null) {
            int delay = ViewConfiguration.getDoubleTapTimeout();
            this.mTextView.postDelayed(this.mShowFloatingToolbar, 100);
        }
    }

    public void beginBatchEdit() {
        this.mInBatchEditControllers = true;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting + 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 1) {
                ims.mCursorChanged = false;
                ims.mChangedDelta = 0;
                if (ims.mContentChanged) {
                    ims.mChangedStart = 0;
                    ims.mChangedEnd = this.mTextView.getText().length();
                } else {
                    ims.mChangedStart = -1;
                    ims.mChangedEnd = -1;
                    ims.mContentChanged = false;
                }
                this.mUndoInputFilter.beginBatchEdit();
                this.mTextView.onBeginBatchEdit();
            }
        }
    }

    public void endBatchEdit() {
        this.mInBatchEditControllers = false;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting - 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 0) {
                finishBatchEdit(ims);
            }
        }
    }

    void ensureEndedBatchEdit() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting != 0) {
            ims.mBatchEditNesting = 0;
            finishBatchEdit(ims);
        }
    }

    void finishBatchEdit(InputMethodState ims) {
        this.mTextView.onEndBatchEdit();
        this.mUndoInputFilter.endBatchEdit();
        if (ims.mContentChanged || ims.mSelectionModeChanged) {
            this.mTextView.updateAfterEdit();
            reportExtractedText();
        } else if (ims.mCursorChanged) {
            this.mTextView.invalidateCursor();
        }
        sendUpdateSelection();
    }

    boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        return extractTextInternal(request, -1, -1, -1, outText);
    }

    private boolean extractTextInternal(ExtractedTextRequest request, int partialStartOffset, int partialEndOffset, int delta, ExtractedText outText) {
        if (request == null || outText == null) {
            return false;
        }
        CharSequence content = this.mTextView.getText();
        if (content == null) {
            return false;
        }
        if (partialStartOffset != -2) {
            int N = content.length();
            if (partialStartOffset < 0) {
                outText.partialEndOffset = -1;
                outText.partialStartOffset = -1;
                partialStartOffset = 0;
                partialEndOffset = N;
            } else {
                partialEndOffset += delta;
                if (content instanceof Spanned) {
                    Spanned spanned = (Spanned) content;
                    Object[] spans = spanned.getSpans(partialStartOffset, partialEndOffset, ParcelableSpan.class);
                    int i = spans.length;
                    while (i > 0) {
                        i--;
                        int j = spanned.getSpanStart(spans[i]);
                        if (j < partialStartOffset) {
                            partialStartOffset = j;
                        }
                        j = spanned.getSpanEnd(spans[i]);
                        if (j > partialEndOffset) {
                            partialEndOffset = j;
                        }
                    }
                }
                outText.partialStartOffset = partialStartOffset;
                outText.partialEndOffset = partialEndOffset - delta;
                if (partialStartOffset > N) {
                    partialStartOffset = N;
                } else if (partialStartOffset < 0) {
                    partialStartOffset = 0;
                }
                if (partialEndOffset > N) {
                    partialEndOffset = N;
                } else if (partialEndOffset < 0) {
                    partialEndOffset = 0;
                }
            }
            if ((request.flags & 1) != 0) {
                outText.text = content.subSequence(partialStartOffset, partialEndOffset);
            } else {
                outText.text = TextUtils.substring(content, partialStartOffset, partialEndOffset);
            }
        } else {
            outText.partialStartOffset = 0;
            outText.partialEndOffset = 0;
            outText.text = "";
        }
        outText.flags = 0;
        if (MetaKeyKeyListener.getMetaState(content, 2048) != 0) {
            outText.flags |= 2;
        }
        if (this.mTextView.isSingleLine()) {
            outText.flags |= 1;
        }
        outText.startOffset = 0;
        outText.selectionStart = this.mTextView.getSelectionStart();
        outText.selectionEnd = this.mTextView.getSelectionEnd();
        return true;
    }

    boolean reportExtractedText() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            boolean contentChanged = ims.mContentChanged;
            if (contentChanged || ims.mSelectionModeChanged) {
                ims.mContentChanged = false;
                ims.mSelectionModeChanged = false;
                ExtractedTextRequest req = ims.mExtractedTextRequest;
                if (req != null) {
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    if (imm != null) {
                        if (ims.mChangedStart < 0 && !contentChanged) {
                            ims.mChangedStart = -2;
                        }
                        if (extractTextInternal(req, ims.mChangedStart, ims.mChangedEnd, ims.mChangedDelta, ims.mExtractedText)) {
                            imm.updateExtractedText(this.mTextView, req.token, ims.mExtractedText);
                            ims.mChangedStart = -1;
                            ims.mChangedEnd = -1;
                            ims.mChangedDelta = 0;
                            ims.mContentChanged = false;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void sendUpdateSelection() {
        if (this.mInputMethodState != null && this.mInputMethodState.mBatchEditNesting <= 0) {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                int selectionStart = this.mTextView.getSelectionStart();
                int selectionEnd = this.mTextView.getSelectionEnd();
                int candStart = -1;
                int candEnd = -1;
                if (this.mTextView.getText() instanceof Spannable) {
                    Spannable sp = (Spannable) this.mTextView.getText();
                    candStart = BaseInputConnection.getComposingSpanStart(sp);
                    candEnd = BaseInputConnection.getComposingSpanEnd(sp);
                }
                imm.updateSelection(this.mTextView, selectionStart, selectionEnd, candStart, candEnd);
            }
        }
    }

    void onDraw(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting == 0) {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null && imm.isActive(this.mTextView) && (ims.mContentChanged || ims.mSelectionModeChanged)) {
                reportExtractedText();
            }
        }
        if (this.mCorrectionHighlighter != null) {
            this.mCorrectionHighlighter.draw(canvas, cursorOffsetVertical);
        }
        if (highlight != null && selectionStart == selectionEnd && this.mCursorCount > 0) {
            drawCursor(canvas, cursorOffsetVertical);
            if (this.mSelectionActionMode) {
                if (this.mSelectionModifierCursorController != null) {
                    this.mSelectionModifierCursorController.hide();
                }
                stopTextActionMode();
            }
            highlight = null;
        }
        if (this.mTextView.canHaveDisplayList() && canvas.isHardwareAccelerated()) {
            drawHardwareAccelerated(canvas, layout, highlight, highlightPaint, cursorOffsetVertical);
        } else {
            layout.draw(canvas, highlight, highlightPaint, cursorOffsetVertical);
        }
    }

    private void drawHardwareAccelerated(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        long lineRange = layout.getLineRangeForDraw(canvas);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine >= 0) {
            layout.drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
            if (layout instanceof DynamicLayout) {
                if (this.mTextRenderNodes == null) {
                    this.mTextRenderNodes = (TextRenderNode[]) ArrayUtils.emptyArray(TextRenderNode.class);
                }
                DynamicLayout dynamicLayout = (DynamicLayout) layout;
                int[] blockEndLines = dynamicLayout.getBlockEndLines();
                int[] blockIndices = dynamicLayout.getBlockIndices();
                int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
                int indexFirstChangedBlock = dynamicLayout.getIndexFirstChangedBlock();
                int endOfPreviousBlock = -1;
                int searchStartIndex = 0;
                for (int i = 0; i < numberOfBlocks; i++) {
                    int blockEndLine = blockEndLines[i];
                    int blockIndex = blockIndices[i];
                    if (blockIndex == -1) {
                        blockIndex = getAvailableDisplayListIndex(blockIndices, numberOfBlocks, searchStartIndex);
                        blockIndices[i] = blockIndex;
                        if (this.mTextRenderNodes[blockIndex] != null) {
                            this.mTextRenderNodes[blockIndex].isDirty = true;
                        }
                        searchStartIndex = blockIndex + 1;
                    }
                    if (this.mTextRenderNodes[blockIndex] == null) {
                        this.mTextRenderNodes[blockIndex] = new TextRenderNode("Text " + blockIndex);
                    }
                    boolean blockDisplayListIsInvalid = this.mTextRenderNodes[blockIndex].needsRecord();
                    RenderNode blockDisplayList = this.mTextRenderNodes[blockIndex].renderNode;
                    if (i >= indexFirstChangedBlock || blockDisplayListIsInvalid) {
                        int blockBeginLine = endOfPreviousBlock + 1;
                        int top = layout.getLineTop(blockBeginLine);
                        int bottom = layout.getLineBottom(blockEndLine);
                        int left = 0;
                        int right = this.mTextView.getWidth();
                        if (this.mTextView.getHorizontallyScrolling()) {
                            float min = AutoScrollHelper.NO_MAX;
                            float max = Float.MIN_VALUE;
                            for (int line = blockBeginLine; line <= blockEndLine; line++) {
                                min = Math.min(min, layout.getLineLeft(line));
                                max = Math.max(max, layout.getLineRight(line));
                            }
                            left = (int) min;
                            right = (int) (LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS + max);
                        }
                        if (blockDisplayListIsInvalid) {
                            Canvas displayListCanvas = blockDisplayList.start(right - left, bottom - top);
                            displayListCanvas.setHighContrastText(((DisplayListCanvas) canvas).getHighContrastText());
                            layout.setHighContastTextMode(((DisplayListCanvas) canvas).getHighContrastText());
                            try {
                                displayListCanvas.translate((float) (-left), (float) (-top));
                                layout.drawText(displayListCanvas, blockBeginLine, blockEndLine);
                                this.mTextRenderNodes[blockIndex].isDirty = false;
                            } finally {
                                blockDisplayList.end(displayListCanvas);
                                blockDisplayList.setClipToBounds(false);
                            }
                        }
                        blockDisplayList.setLeftTopRightBottom(left, top, right, bottom);
                    }
                    ((DisplayListCanvas) canvas).drawRenderNode(blockDisplayList);
                    endOfPreviousBlock = blockEndLine;
                }
                dynamicLayout.setIndexFirstChangedBlock(numberOfBlocks);
                return;
            }
            layout.drawText(canvas, firstLine, lastLine);
        }
    }

    private int getAvailableDisplayListIndex(int[] blockIndices, int numberOfBlocks, int searchStartIndex) {
        int length = this.mTextRenderNodes.length;
        for (int i = searchStartIndex; i < length; i++) {
            boolean blockIndexFound = false;
            for (int j = 0; j < numberOfBlocks; j++) {
                if (blockIndices[j] == i) {
                    blockIndexFound = true;
                    break;
                }
            }
            if (!blockIndexFound) {
                return i;
            }
        }
        this.mTextRenderNodes = (TextRenderNode[]) GrowingArrayUtils.append(this.mTextRenderNodes, length, null);
        return length;
    }

    private void drawCursor(Canvas canvas, int cursorOffsetVertical) {
        boolean translate = cursorOffsetVertical != 0;
        if (translate) {
            canvas.translate(0.0f, (float) cursorOffsetVertical);
        }
        for (int i = 0; i < this.mCursorCount; i++) {
            this.mCursorDrawable[i].draw(canvas);
        }
        if (translate) {
            canvas.translate(0.0f, (float) (-cursorOffsetVertical));
        }
    }

    void invalidateTextDisplayList(Layout layout, int start, int end) {
        if (this.mTextRenderNodes != null && (layout instanceof DynamicLayout)) {
            int firstLine = layout.getLineForOffset(start);
            int lastLine = layout.getLineForOffset(end);
            DynamicLayout dynamicLayout = (DynamicLayout) layout;
            int[] blockEndLines = dynamicLayout.getBlockEndLines();
            int[] blockIndices = dynamicLayout.getBlockIndices();
            int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            int i = 0;
            while (i < numberOfBlocks && blockEndLines[i] < firstLine) {
                i++;
            }
            while (i < numberOfBlocks) {
                int blockIndex = blockIndices[i];
                if (blockIndex != -1) {
                    this.mTextRenderNodes[blockIndex].isDirty = true;
                }
                if (blockEndLines[i] < lastLine) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    void invalidateTextDisplayList() {
        if (this.mTextRenderNodes != null) {
            for (int i = 0; i < this.mTextRenderNodes.length; i++) {
                if (this.mTextRenderNodes[i] != null) {
                    this.mTextRenderNodes[i].isDirty = true;
                }
            }
        }
    }

    void updateCursorsPositions() {
        if (this.mTextView.mCursorDrawableRes == 0) {
            this.mCursorCount = 0;
            return;
        }
        int i;
        Layout layout = getActiveLayout();
        int offset = this.mTextView.getSelectionStart();
        int line = layout.getLineForOffset(offset);
        int top = layout.getLineTop(line);
        int bottom = layout.getLineTop(line + 1);
        if (layout.isLevelBoundary(offset)) {
            i = 2;
        } else {
            i = 1;
        }
        this.mCursorCount = i;
        int middle = bottom;
        if (this.mCursorCount == 2) {
            middle = (top + bottom) >> 1;
        }
        boolean clamped = layout.shouldClampCursor(line);
        updateCursorPosition(0, top, middle, layout.getPrimaryHorizontal(offset, clamped));
        if (this.mCursorCount == 2) {
            updateCursorPosition(1, middle, bottom, layout.getSecondaryHorizontal(offset, clamped));
        }
    }

    void startInsertionActionMode() {
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        if (!extractedTextModeWillBeStarted()) {
            stopTextActionMode();
            Callback actionModeCallback = new TextActionModeCallback(false);
            this.mSelectionActionMode = false;
            if (this.mThemeIsDeviceDefault) {
                this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 99);
            } else {
                this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 1);
            }
            if (this.mTextActionMode != null && getInsertionController() != null) {
                getInsertionController().show();
            }
        }
    }

    boolean startSelectionActionMode() {
        boolean selectionStarted = startSelectionActionModeInternal();
        if (selectionStarted) {
            getSelectionController().show();
        } else if (getInsertionController() != null) {
            getInsertionController().show();
        }
        return selectionStarted;
    }

    private boolean selectCurrentWordAndStartDrag() {
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        if (extractedTextModeWillBeStarted() || !checkFieldAndSelectCurrentWord()) {
            return false;
        }
        getSelectionController().enterDrag();
        return true;
    }

    boolean checkFieldAndSelectCurrentWord() {
        if (!this.mTextView.canSelectText() || !this.mTextView.requestFocus()) {
            Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
            return false;
        } else if (this.mTextView.hasSelection()) {
            return true;
        } else {
            return selectCurrentWord();
        }
    }

    private boolean isUniversalSwitchEnabled() {
        return Secure.getInt(this.mTextView.getContext().getContentResolver(), SWITCH_CONTROL_ENABLED, 0) == 1;
    }

    private boolean startSelectionActionModeInternal() {
        boolean z = false;
        if (this.mTextActionMode != null) {
            this.mTextActionMode.invalidate();
        } else if (checkFieldAndSelectCurrentWord()) {
            boolean willExtract = extractedTextModeWillBeStarted();
            if (!(willExtract || isUniversalSwitchEnabled())) {
                Callback actionModeCallback = new TextActionModeCallback(true);
                this.mSelectionActionMode = true;
                if (this.mThemeIsDeviceDefault) {
                    this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 99);
                } else {
                    this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 1);
                }
            }
            if (this.mTextActionMode != null || willExtract) {
                z = true;
            }
        }
        return z;
    }

    void updateSelectionHandler() {
    }

    boolean extractedTextModeWillBeStarted() {
        if (this.mTextView.isInExtractedMode()) {
            return false;
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm == null || !imm.isFullscreenMode()) {
            return false;
        }
        return true;
    }

    private boolean shouldOfferToShowSuggestions() {
        CharSequence text = this.mTextView.getText();
        if (!(text instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) text;
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(selectionStart, selectionEnd, SuggestionSpan.class);
        if (suggestionSpans.length == 0) {
            return false;
        }
        int i;
        if (selectionStart == selectionEnd) {
            for (SuggestionSpan suggestions : suggestionSpans) {
                if (suggestions.getSuggestions().length > 0) {
                    return true;
                }
            }
            return false;
        }
        int minSpanStart = this.mTextView.getText().length();
        int maxSpanEnd = 0;
        int unionOfSpansCoveringSelectionStartStart = this.mTextView.getText().length();
        int unionOfSpansCoveringSelectionStartEnd = 0;
        boolean hasValidSuggestions = false;
        for (i = 0; i < suggestionSpans.length; i++) {
            int spanStart = spannable.getSpanStart(suggestionSpans[i]);
            int spanEnd = spannable.getSpanEnd(suggestionSpans[i]);
            minSpanStart = Math.min(minSpanStart, spanStart);
            maxSpanEnd = Math.max(maxSpanEnd, spanEnd);
            if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                hasValidSuggestions = hasValidSuggestions || suggestionSpans[i].getSuggestions().length > 0;
                unionOfSpansCoveringSelectionStartStart = Math.min(unionOfSpansCoveringSelectionStartStart, spanStart);
                unionOfSpansCoveringSelectionStartEnd = Math.max(unionOfSpansCoveringSelectionStartEnd, spanEnd);
            }
        }
        if (!hasValidSuggestions) {
            return false;
        }
        if (unionOfSpansCoveringSelectionStartStart >= unionOfSpansCoveringSelectionStartEnd) {
            return false;
        }
        if (minSpanStart < unionOfSpansCoveringSelectionStartStart || maxSpanEnd > unionOfSpansCoveringSelectionStartEnd) {
            return false;
        }
        return true;
    }

    private boolean isCursorInsideEasyCorrectionSpan() {
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) ((Spannable) this.mTextView.getText()).getSpans(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), SuggestionSpan.class);
        for (SuggestionSpan flags : suggestionSpans) {
            if ((flags.getFlags() & 1) != 0) {
                return true;
            }
        }
        return false;
    }

    void onTouchUpEvent(MotionEvent event) {
        boolean selectAllGotFocus = this.mSelectAllOnFocus && this.mTextView.didTouchFocusSelect();
        hideCursorAndSpanControllers();
        stopTextActionMode();
        CharSequence text = this.mTextView.getText();
        if (!selectAllGotFocus && text.length() > 0) {
            Selection.setSelection((Spannable) text, this.mTextView.getOffsetForPosition(event.getX(), event.getY()));
            if (this.mSpellChecker != null) {
                this.mSpellChecker.onSelectionChanged();
            }
            if (!extractedTextModeWillBeStarted()) {
                if (isCursorInsideEasyCorrectionSpan()) {
                    if (this.mInsertionActionModeRunnable != null) {
                        this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
                    }
                    this.mShowSuggestionRunnable = new Runnable() {
                        public void run() {
                            Editor.this.showSuggestions();
                        }
                    };
                    this.mTextView.postDelayed(this.mShowSuggestionRunnable, (long) ViewConfiguration.getDoubleTapTimeout());
                } else if (hasInsertionController()) {
                    getInsertionController().show();
                }
            }
        }
    }

    protected void stopTextActionMode() {
        if (this.mTextActionMode != null) {
            this.mSelectionActionMode = false;
            if (this.mSelectionModifierCursorController != null) {
                this.mSelectionModifierCursorController.resetTouchOffsets();
            }
            this.mTextActionMode.finish();
        }
    }

    boolean hasInsertionController() {
        return this.mInsertionControllerEnabled;
    }

    boolean hasSelectionController() {
        return this.mSelectionControllerEnabled;
    }

    InsertionPointCursorController getInsertionController() {
        if (!this.mInsertionControllerEnabled) {
            return null;
        }
        if (this.mInsertionPointCursorController == null) {
            this.mInsertionPointCursorController = new InsertionPointCursorController();
            this.mTextView.getViewTreeObserver().addOnTouchModeChangeListener(this.mInsertionPointCursorController);
        }
        return this.mInsertionPointCursorController;
    }

    SelectionModifierCursorController getSelectionController() {
        if (!this.mSelectionControllerEnabled) {
            return null;
        }
        if (this.mSelectionModifierCursorController == null) {
            this.mSelectionModifierCursorController = new SelectionModifierCursorController();
            this.mTextView.getViewTreeObserver().addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
        }
        return this.mSelectionModifierCursorController;
    }

    private void updateCursorPosition(int cursorIndex, int top, int bottom, float horizontal) {
        if (this.mCursorDrawable[cursorIndex] == null) {
            this.mCursorDrawable[cursorIndex] = this.mTextView.getContext().getDrawable(this.mTextView.mCursorDrawableRes);
        }
        if (this.mTempRect == null) {
            this.mTempRect = new Rect();
        }
        this.mCursorDrawable[cursorIndex].getPadding(this.mTempRect);
        int width = this.mCursorDrawable[cursorIndex].getIntrinsicWidth();
        int left = ((int) Math.max(LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS, horizontal - LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS)) - this.mTempRect.left;
        int cursorWidth = this.mTextView.mCursorWidth;
        int visibleCursorWidth = (width - this.mTempRect.left) - this.mTempRect.right;
        if (visibleCursorWidth < cursorWidth) {
            width += cursorWidth - visibleCursorWidth;
        }
        this.mCursorDrawable[cursorIndex].setBounds(left, top - this.mTempRect.top, left + width, this.mTempRect.bottom + bottom);
        int tvWidth = (this.mTextView.getWidth() - this.mTextView.getTotalPaddingLeft()) - this.mTextView.getTotalPaddingRight();
        this.mCursorShiftOffset = 0;
        int clippedCursorWidth = (((left - this.mTextView.getScrollX()) - tvWidth) + this.mTempRect.left) + cursorWidth;
        if (clippedCursorWidth > 0 && clippedCursorWidth < cursorWidth) {
            this.mCursorShiftOffset = clippedCursorWidth;
            this.mCursorDrawable[cursorIndex].setBounds(left - this.mCursorShiftOffset, top - this.mTempRect.top, (left + width) - this.mCursorShiftOffset, this.mTempRect.bottom + bottom);
        }
    }

    public void onCommitCorrection(CorrectionInfo info) {
        if (this.mCorrectionHighlighter == null) {
            this.mCorrectionHighlighter = new CorrectionHighlighter();
        } else {
            this.mCorrectionHighlighter.invalidate(false);
        }
        this.mCorrectionHighlighter.highlight(info);
    }

    void showSuggestions() {
        if (this.mSuggestionsPopupWindow == null) {
            this.mSuggestionsPopupWindow = new SuggestionsPopupWindow();
        }
        hideCursorAndSpanControllers();
        stopTextActionMode();
        this.mSuggestionsPopupWindow.show();
    }

    void onScrollChanged() {
        if (this.mPositionListener != null) {
            this.mPositionListener.onScrollChanged();
        }
        if (this.mTextActionMode != null) {
            this.mTextActionMode.invalidateContentRect();
        }
    }

    private boolean shouldBlink() {
        if (!isCursorVisible() || !this.mTextView.isFocused()) {
            return false;
        }
        int start = this.mTextView.getSelectionStart();
        if (start < 0) {
            return false;
        }
        int end = this.mTextView.getSelectionEnd();
        if (end < 0 || start != end) {
            return false;
        }
        return true;
    }

    void makeBlink() {
        if (shouldBlink()) {
            this.mShowCursor = SystemClock.uptimeMillis();
            if (this.mBlink == null) {
                this.mBlink = new Blink();
            }
            this.mBlink.removeCallbacks(this.mBlink);
            this.mBlink.postAtTime(this.mBlink, this.mShowCursor + 500);
        } else if (this.mBlink != null) {
            this.mBlink.removeCallbacks(this.mBlink);
        }
    }

    private DragShadowBuilder getTextThumbnailBuilder(CharSequence text) {
        TextView shadowView = (TextView) View.inflate(this.mTextView.getContext(), R.layout.text_drag_thumbnail, null);
        if (shadowView == null) {
            throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
        }
        if (text.length() > DRAG_SHADOW_MAX_TEXT_LENGTH) {
            int end = DRAG_SHADOW_MAX_TEXT_LENGTH;
            if (Character.isLowSurrogate(text.charAt(end))) {
                end--;
            }
            text = text.subSequence(0, end);
        }
        shadowView.setText(text);
        shadowView.setTextColor(this.mTextView.getTextColors());
        shadowView.setTextAppearance(16);
        shadowView.setGravity(17);
        shadowView.setLayoutParams(new LayoutParams(-2, -2));
        int size = MeasureSpec.makeMeasureSpec(0, 0);
        shadowView.measure(size, size);
        shadowView.layout(0, 0, shadowView.getMeasuredWidth(), shadowView.getMeasuredHeight());
        shadowView.invalidate();
        return new DragShadowBuilder(shadowView);
    }

    void onDrop(DragEvent event) {
        StringBuilder content = new StringBuilder("");
        ClipData clipData = event.getClipData();
        if (clipData == null) {
            this.mTextView.setOnDragResult(false);
            return;
        }
        int itemCount = clipData.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            CharSequence str = clipData.getItemAt(i).coerceToStyledTextForEditor(this.mTextView.getContext());
            if (str != null) {
                content.append(str);
            }
        }
        if (content.length() == 0) {
            this.mTextView.setOnDragResult(false);
            return;
        }
        int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
        DragLocalState localState = event.getLocalState();
        DragLocalState dragLocalState = null;
        if (localState instanceof DragLocalState) {
            dragLocalState = localState;
        }
        boolean dragDropIntoItself = dragLocalState != null && dragLocalState.sourceTextView == this.mTextView;
        if (!dragDropIntoItself || offset < dragLocalState.start || offset >= dragLocalState.end) {
            int originalLength = this.mTextView.getText().length();
            int min = offset;
            int max = offset;
            Selection.setSelection((Spannable) this.mTextView.getText(), max);
            this.mTextView.replaceText_internal(min, max, content);
            if (dragDropIntoItself) {
                int dragSourceStart = dragLocalState.start;
                int dragSourceEnd = dragLocalState.end;
                if (max <= dragSourceStart) {
                    int shift = this.mTextView.getText().length() - originalLength;
                    dragSourceStart += shift;
                    dragSourceEnd += shift;
                }
                this.mTextView.deleteText_internal(dragSourceStart, dragSourceEnd);
                int prevCharIdx = Math.max(0, dragSourceStart - 1);
                int nextCharIdx = Math.min(this.mTextView.getText().length(), dragSourceStart + 1);
                if (nextCharIdx > prevCharIdx + 1) {
                    CharSequence t = this.mTextView.getTransformedText(prevCharIdx, nextCharIdx);
                    if (Character.isSpaceChar(t.charAt(0)) && Character.isSpaceChar(t.charAt(1))) {
                        this.mTextView.deleteText_internal(prevCharIdx, prevCharIdx + 1);
                    }
                }
            }
            this.mTextView.setOnDragResult(true);
            return;
        }
        this.mTextView.setOnDragResult(false);
    }

    public void addSpanWatchers(Spannable text) {
        int textLength = text.length();
        if (this.mKeyListener != null) {
            text.setSpan(this.mKeyListener, 0, textLength, 18);
        }
        if (this.mSpanController == null) {
            this.mSpanController = new SpanController();
        }
        text.setSpan(this.mSpanController, 0, textLength, 18);
    }

    public void removeMessage() {
        if (getPositionListener().mDelayHandler != null) {
            if (getPositionListener().mDelayHandler.hasMessages(0)) {
                getPositionListener().mDelayHandler.removeMessages(0);
            }
            if (getPositionListener().mDelayHandler.hasMessages(1)) {
                getPositionListener().mDelayHandler.removeMessages(1);
            }
            if (getPositionListener().mDelayHandler.hasMessages(2)) {
                getPositionListener().mDelayHandler.removeMessages(2);
            }
            if (getPositionListener().mDelayHandler.hasMessages(3)) {
                getPositionListener().mDelayHandler.removeMessages(3);
            }
        }
    }

    private Layout getActiveLayout() {
        Layout layout = this.mTextView.getLayout();
        Layout hintLayout = this.mTextView.getHintLayout();
        if (!TextUtils.isEmpty(layout.getText()) || hintLayout == null || TextUtils.isEmpty(hintLayout.getText())) {
            return layout;
        }
        return hintLayout;
    }

    private int getCurrentLineAdjustedForSlop(Layout layout, int prevLine, float y) {
        int trueLine = this.mTextView.getLineAtCoordinate(y);
        if (layout == null || prevLine > layout.getLineCount() || layout.getLineCount() <= 0 || prevLine < 0 || Math.abs(trueLine - prevLine) >= 2) {
            return trueLine;
        }
        int currLine;
        float verticalOffset = (float) this.mTextView.viewportToContentVerticalOffset();
        int lineCount = layout.getLineCount();
        float slop = ((float) this.mTextView.getLineHeight()) * LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS;
        float yTopBound = Math.max((((float) layout.getLineTop(prevLine)) + verticalOffset) - slop, (((float) layout.getLineTop(0)) + verticalOffset) + slop);
        float yBottomBound = Math.min((((float) layout.getLineBottom(prevLine)) + verticalOffset) + slop, (((float) layout.getLineBottom(lineCount - 1)) + verticalOffset) - slop);
        if (y <= yTopBound) {
            currLine = Math.max(prevLine - 1, 0);
        } else if (y >= yBottomBound) {
            currLine = Math.min(prevLine + 1, lineCount - 1);
        } else {
            currLine = prevLine;
        }
        return currLine;
    }

    private static boolean isValidRange(CharSequence text, int start, int end) {
        return start >= 0 && start <= end && end <= text.length();
    }

    private boolean isDictionaryEnabled() {
        if (this.mTextView == null || !this.mTextView.canDictionary() || this.mTextView.getContext().getPackageManager().queryIntentActivities(new Intent("com.sec.android.app.dictionary.SEARCH"), 0).size() == 0 || this.mTextView.getSelectionStart() < 0 || this.mTextView.getSelectionEnd() < 0 || this.mTextView.getSelectionStart() == this.mTextView.getSelectionEnd() || isInSpannableObj()) {
            return false;
        }
        return true;
    }

    private boolean isInSpannableObj() {
        int max = this.mTextView.getText().length();
        int selStart = this.mTextView.getSelectionStart();
        int selEnd = this.mTextView.getSelectionEnd();
        CharSequence cs = this.mTextView.getText().subSequence(Math.max(0, Math.min(selStart, selEnd)), Math.max(0, Math.max(selStart, selEnd)));
        if (cs == null) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (cs.charAt(i) == '￼') {
                return true;
            }
        }
        return false;
    }

    public void sendToDictionary(String word, int start, int end) {
        PackageManager pm = this.mTextView.getContext().getPackageManager();
        Intent intent = new Intent("com.sec.android.app.dictionary.SEARCH");
        if (this.mTextView.isMultiWindow()) {
            intent.setFlags(268435456);
            intent.putExtra("keyword", word);
            try {
                this.mTextView.getContext().startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
                Log.e("Editor", "Fail to launch dictionary.");
                return;
            }
        }
        intent.addFlags(32);
        intent.putExtra("keyword", word);
        intent.putExtra("force", SmartFaceManager.TRUE);
        if (pm.queryBroadcastReceivers(intent, 32).size() > 0) {
            this.mTextView.getContext().sendBroadcast(intent);
        }
    }

    void stopCursorBlink(boolean stop) {
        if (stop) {
            suspendBlink();
        } else {
            resumeBlink();
        }
    }

    void setWBPositionListenerEnalbed(boolean enable) {
        if (enable) {
            if (this.mWBPositionListener == null) {
                this.mWBPositionListener = new TextViewPositionListener() {
                    public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
                        WritingBuddyImpl wb = Editor.this.mTextView.getWritingBuddy(false);
                        if (wb == null) {
                            return;
                        }
                        if (parentPositionChanged || parentScrolled) {
                            wb.notifyPositionChanged(parentPositionChanged ? 1 : 2);
                        }
                    }
                };
            }
            getPositionListener().addSubscriber(this.mWBPositionListener, true);
        } else if (this.mWBPositionListener != null) {
            getPositionListener().removeSubscriber(this.mWBPositionListener);
            this.mWBPositionListener = null;
        }
    }
}
