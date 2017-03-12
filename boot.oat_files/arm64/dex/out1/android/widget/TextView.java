package android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Paint.MyanmarEncoding;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.ExtractEditText;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ParcelableParcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.provider.Settings.Secure;
import android.sec.clipboard.ClipboardExManager;
import android.sec.clipboard.ClipboardExManager.ClipboardEventListener;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.ClipboardDefine;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.data.list.ClipboardDataIntent;
import android.sec.clipboard.data.list.ClipboardDataMultipleUri;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.clipboard.data.list.ClipboardDataUri;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.RestrictionPolicy;
import android.service.notification.ZenModeConfig;
import android.text.BoringLayout;
import android.text.BoringLayout.Metrics;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.GetChars;
import android.text.GraphicsOperations;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.MultiSelection;
import android.text.ParcelableSpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.DateKeyListener;
import android.text.method.DateTimeKeyListener;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.LinkMovementMethod;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.text.method.TimeKeyListener;
import android.text.method.TransformationMethod;
import android.text.method.TransformationMethod2;
import android.text.method.WordIterator;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.EasyEditSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.text.style.URLSpan;
import android.text.style.UpdateAppearance;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.GateConfig;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.AccessibilityIterators.TextSegmentIterator;
import android.view.ActionMode.Callback;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.MotionEventMonitor;
import android.view.ViewRootImpl.MotionEventMonitor.OnTouchListener;
import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnStylusButtonEventListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SpellCheckerSubtype;
import android.view.textservice.TextServicesManager;
import android.widget.RemoteViews.RemoteView;
import android.widget.SearchView.SearchAutoComplete;
import com.android.internal.R;
import com.android.internal.util.FastMath;
import com.android.internal.widget.EditableInputConnection;
import com.samsung.android.cover.CoverManager;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.samsung.android.cover.ICoverManager.Stub;
import com.samsung.android.hermes.HermesServiceManager;
import com.samsung.android.penselect.PenSelectionController;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.smartclip.SmartClipDataElementImpl;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParserException;

@RemoteView
public class TextView extends View implements OnPreDrawListener {
    static final int ACCESSIBILITY_ACTION_PROCESS_TEXT_START_ID = 268435712;
    private static final int ACCESSIBILITY_ACTION_SHARE = 268435456;
    private static final int ANIMATED_SCROLL_GAP = 250;
    private static final int CHANGE_WATCHER_PRIORITY = 100;
    public static final int CLIPBOARD_ID = 5;
    static final boolean DEBUG_EXTRACT = false;
    private static final int DECIMAL = 4;
    public static final int DICTIONARY_ID = 9;
    private static final Spanned EMPTY_SPANNED = new SpannedString("");
    private static final int EMS = 1;
    static final float HCT_LETTER_SPACING = 0.06f;
    static final float HCT_LETTER_SPACING_NOT_BACKUPPED = -300.0f;
    static final int ID_CLIPBOARD = 16908375;
    static final int ID_CLOSE = 16908377;
    static final int ID_COPY = 16908321;
    static final int ID_CUT = 16908320;
    static final int ID_DELETE = 16908376;
    static final int ID_DICTIONARY = 16908373;
    static final int ID_MULTI_SELECT_ALL = 16908381;
    static final int ID_MULTI_SELECT_COPY = 16908382;
    static final int ID_MULTI_SELECT_DICTIONARY = 16908383;
    static final int ID_MULTI_SELECT_SHARE = 16908385;
    static final int ID_MULTI_SELECT_TRANSLATE = 16908384;
    static final int ID_PASTE = 16908322;
    static final int ID_PASTE_AS_PLAIN_TEXT = 16908337;
    static final int ID_REDO = 16908339;
    static final int ID_REPLACE = 16908340;
    static final int ID_SELECT_ALL = 16908319;
    static final int ID_SHARE = 16908341;
    static final int ID_UNDO = 16908338;
    static final int ID_WEBSEARCH = 16909562;
    private static final int LINES = 1;
    static final String LOG_TAG = "TextView";
    private static final int MARQUEE_FADE_NORMAL = 0;
    private static final int MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS = 1;
    private static final int MARQUEE_FADE_SWITCH_SHOW_FADE = 2;
    private static final int MONOSPACE = 3;
    private static final int[] MULTILINE_STATE_SET = new int[]{R.attr.state_multiline};
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private static final int PIXELS = 2;
    static final int PROCESS_TEXT_REQUEST_CODE = 100;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    public static final int SHARE_ID = 7;
    private static final int SIGNED = 2;
    private static final int STRIKE_ANIMATION_DURATION = 400;
    private static final RectF TEMP_RECTF = new RectF();
    public static final int TRANSLATE_ID = 6;
    private static final Metrics UNKNOWN_BORING = new Metrics();
    private static final int VERY_WIDE = 1048576;
    public static final int WEBSEARCH_ID = 8;
    private static final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
    private static long mCurTime = 0;
    private static boolean mIsFindTargetView = false;
    private static long mLastHoveredTime = 0;
    private static TextView mLastHoveredView = null;
    private static long mLastPenDownTime = 0;
    private static OnTouchListener mMotionEventMonitorListener = null;
    private static Runnable mShowPenSelectionRunnable = null;
    private static TextView mTargetView = null;
    static long sLastCutCopyOrTextChangedTime;
    private static final Pattern urlPattern = Patterns.WEB_URL;
    private final int HOVER_INTERVAL;
    private int SEC_CLIPBOARD_DISABLED;
    private int SEC_CLIPBOARD_ENABLED;
    private int SEC_CLIPBOARD_UNKNOWN;
    private float TOUCH_DELTA;
    private boolean doShowingHermes;
    private boolean fromResLock;
    private final boolean isElasticEnabled;
    private boolean mAllowTransformationLengthChange;
    private boolean mAttachedWindow;
    private int mAutoLinkMask;
    private float mBackuppedLetterSpacing;
    private Metrics mBoring;
    private int mBreakStrategy;
    private BufferType mBufferType;
    private boolean mCanSelectText;
    private boolean mCanTextMultiSelection;
    private ChangeWatcher mChangeWatcher;
    private boolean mChangedSelectionBySIP;
    private CharWrapper mCharWrapper;
    private int mClipboardDataFormat;
    private ClipboardExManager mClipboardExManager;
    private CoverManager mCoverManager;
    private int mCurHintTextColor;
    @ExportedProperty(category = "text")
    private int mCurTextColor;
    private volatile Locale mCurrentSpellCheckerLocaleCache;
    int mCursorDrawableRes;
    int mCursorWidth;
    private int mDeferScroll;
    private int mDesiredHeightAtMeasure;
    private boolean mDispatchTemporaryDetach;
    private CharSequence mDisplayText;
    private float mDrawStrikeAnimationValue;
    private ValueAnimator mDrawTextStrikeAnimator;
    Drawables mDrawables;
    private Factory mEditableFactory;
    private Editor mEditor;
    private int mEllipsisKeywordCount;
    private int mEllipsisKeywordStart;
    private TruncateAt mEllipsize;
    private boolean mEnableClipboard;
    private boolean mEnableDictionary;
    private boolean mEnableLinkPreview;
    private boolean mEnableMultiSelection;
    private boolean mEnableShare;
    private boolean mEnableWebSearch;
    private InputFilter[] mFilters;
    private boolean mFirstTouch;
    private boolean mFreezesText;
    @ExportedProperty(category = "text")
    private int mGravity;
    private boolean mHideSoftInput;
    int mHighlightColor;
    private final Paint mHighlightPaint;
    private Path mHighlightPath;
    private boolean mHighlightPathBogus;
    private CharSequence mHint;
    private Metrics mHintBoring;
    private Layout mHintLayout;
    private ColorStateList mHintTextColor;
    private boolean mHorizontallyScrolling;
    private long mHoverEnterTime;
    private long mHoverExitTime;
    private Object mHoveredSpan;
    private int mHyphenationFrequency;
    private boolean mIncludePad;
    private int mIsSecClipboardEnabled;
    boolean mIsTouchDown;
    private int mLastLayoutDirection;
    private long mLastScroll;
    private long mLastTouchUpTime;
    private Layout mLayout;
    private boolean mLineIsDrawed;
    private ColorStateList mLinkTextColor;
    private boolean mLinksClickable;
    private ArrayList<TextWatcher> mListeners;
    private boolean mLocaleChanged;
    private Marquee mMarquee;
    private int mMarqueeFadeMode;
    private int mMarqueeRepeatLimit;
    private int mMaxMode;
    private int mMaxWidth;
    private int mMaxWidthMode;
    private int mMaximum;
    private int mMinMode;
    private int mMinWidth;
    private int mMinWidthMode;
    private int mMinimum;
    private MovementMethod mMovement;
    private int mMultiHighlightColor;
    private Paint mMultiHighlightPaint;
    private MultiSelectPopupWindow mMultiSelectPopupWindow;
    private int mOldMaxMode;
    private int mOldMaximum;
    protected boolean mOnDragResult;
    protected TextViewClipboardEventListener mPasteEventListener;
    private PenSelectionController mPenSelectionController;
    private boolean mPreDrawListenerDetached;
    private boolean mPreDrawRegistered;
    private boolean mPreventDefaultMovement;
    private boolean mRestartMarquee;
    private RestrictionPolicy mRestrictionPolicy;
    private BoringLayout mSavedHintLayout;
    private BoringLayout mSavedLayout;
    private Layout mSavedMarqueeModeLayout;
    private Scroller mScroller;
    private int mShadowColor;
    private float mShadowDx;
    private float mShadowDy;
    private float mShadowRadius;
    private boolean mSingleLine;
    private boolean mSkipDrawTextStrike;
    private boolean mSkipUpdateDisplayText;
    private float mSpacingAdd;
    private float mSpacingMult;
    private Spannable.Factory mSpannableFactory;
    private Paint mStrikeThroughPaint;
    @ExportedProperty
    private CharSequence mStringName;
    private StylusEventListener mStylusEventListener;
    private Rect mTempRect;
    boolean mTemporaryDetach;
    @ExportedProperty(category = "text")
    private CharSequence mText;
    private ColorStateList mTextColor;
    private TextDirectionHeuristic mTextDir;
    int mTextEditSuggestionItemLayout;
    private final TextPaint mTextPaint;
    int mTextSelectHandleLeftRes;
    int mTextSelectHandleRes;
    int mTextSelectHandleRightRes;
    private boolean mTextStrikeThroughEnabled;
    protected int mToolType;
    private TransformationMethod mTransformation;
    private CharSequence mTransformed;
    private boolean mUseDisplayText;
    private boolean mUserSetTextScaleX;
    private int mWBMaxLength;
    private CharSequence mWBTextBuffer;
    private WordIterator mWordIteratorForMultiSelection;
    private boolean mhasMultiSelection;

    public interface OnEditorActionListener {
        boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent);
    }

    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = new int[Alignment.values().length];

        static {
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_OPPOSITE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_CENTER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum BufferType {
        NORMAL,
        SPANNABLE,
        EDITABLE
    }

    private class ChangeWatcher implements TextWatcher, SpanWatcher {
        private CharSequence mBeforeText;

        private ChangeWatcher() {
        }

        public void beforeTextChanged(CharSequence buffer, int start, int before, int after) {
            if (AccessibilityManager.getInstance(TextView.this.mContext).isEnabled() && (!(TextView.isPasswordInputType(TextView.this.getInputType()) || TextView.this.hasPasswordTransformationMethod()) || TextView.this.shouldSpeakPasswordsForAccessibility())) {
                this.mBeforeText = buffer.toString();
            }
            TextView.this.sendBeforeTextChanged(buffer, start, before, after);
        }

        public void onTextChanged(CharSequence buffer, int start, int before, int after) {
            TextView.this.handleTextChanged(buffer, start, before, after);
            if (!AccessibilityManager.getInstance(TextView.this.mContext).isEnabled()) {
                return;
            }
            if (TextView.this.isFocused() || (TextView.this.isSelected() && TextView.this.isShown())) {
                TextView.this.sendAccessibilityEventTypeViewTextChanged(this.mBeforeText, start, before, after);
                this.mBeforeText = null;
            }
        }

        public void afterTextChanged(Editable buffer) {
            TextView.this.sendAfterTextChanged(buffer);
            if (MetaKeyKeyListener.getMetaState((CharSequence) buffer, 2048) != 0) {
                MetaKeyKeyListener.stopSelecting(TextView.this, buffer);
            }
        }

        public void onSpanChanged(Spannable buf, Object what, int s, int e, int st, int en) {
            TextView.this.spanChange(buf, what, s, st, e, en);
        }

        public void onSpanAdded(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, -1, s, -1, e);
        }

        public void onSpanRemoved(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, s, -1, e, -1);
        }
    }

    private static class CharWrapper implements CharSequence, GetChars, GraphicsOperations {
        private char[] mChars;
        private int mLength;
        private int mStart;

        public CharWrapper(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        void set(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        public int length() {
            return this.mLength;
        }

        public char charAt(int off) {
            return this.mChars[this.mStart + off];
        }

        public String toString() {
            return new String(this.mChars, this.mStart, this.mLength);
        }

        public CharSequence subSequence(int start, int end) {
            if (start >= 0 && end >= 0 && start <= this.mLength && end <= this.mLength) {
                return new String(this.mChars, this.mStart + start, end - start);
            }
            throw new IndexOutOfBoundsException(start + ", " + end);
        }

        public void getChars(int start, int end, char[] buf, int off) {
            if (start < 0 || end < 0 || start > this.mLength || end > this.mLength) {
                throw new IndexOutOfBoundsException(start + ", " + end);
            }
            System.arraycopy(this.mChars, this.mStart + start, buf, off, end - start);
        }

        public void drawText(Canvas c, int start, int end, float x, float y, Paint p) {
            c.drawText(this.mChars, start + this.mStart, end - start, x, y, p);
        }

        public void drawTextRun(Canvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawTextRun(this.mChars, start + this.mStart, count, contextStart + this.mStart, contextCount, x, y, isRtl, p);
        }

        public float measureText(int start, int end, Paint p) {
            return p.measureText(this.mChars, this.mStart + start, end - start);
        }

        public int getTextWidths(int start, int end, float[] widths, Paint p) {
            return p.getTextWidths(this.mChars, this.mStart + start, end - start, widths);
        }

        public float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            return p.getTextRunAdvances(this.mChars, start + this.mStart, count, contextStart + this.mStart, contextCount, isRtl, advances, advancesIndex);
        }

        public int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, Paint p) {
            int contextCount = contextEnd - contextStart;
            return p.getTextRunCursor(this.mChars, contextStart + this.mStart, contextCount, dir, offset + this.mStart, cursorOpt);
        }
    }

    static class Drawables {
        static final int BOTTOM = 3;
        static final int DRAWABLE_LEFT = 1;
        static final int DRAWABLE_NONE = -1;
        static final int DRAWABLE_RIGHT = 0;
        static final int LEFT = 0;
        static final int RIGHT = 2;
        static final int TOP = 1;
        final Rect mCompoundRect = new Rect();
        Drawable mDrawableEnd;
        Drawable mDrawableError;
        int mDrawableHeightEnd;
        int mDrawableHeightError;
        int mDrawableHeightLeft;
        int mDrawableHeightRight;
        int mDrawableHeightStart;
        int mDrawableHeightTemp;
        Drawable mDrawableLeftInitial;
        int mDrawablePadding;
        Drawable mDrawableRightInitial;
        int mDrawableSaved = -1;
        int mDrawableSizeBottom;
        int mDrawableSizeEnd;
        int mDrawableSizeError;
        int mDrawableSizeLeft;
        int mDrawableSizeRight;
        int mDrawableSizeStart;
        int mDrawableSizeTemp;
        int mDrawableSizeTop;
        Drawable mDrawableStart;
        Drawable mDrawableTemp;
        int mDrawableWidthBottom;
        int mDrawableWidthTop;
        boolean mHasTint;
        boolean mHasTintMode;
        boolean mIsRtlCompatibilityMode;
        boolean mOverride;
        final Drawable[] mShowing = new Drawable[4];
        ColorStateList mTintList;
        Mode mTintMode;

        public Drawables(Context context) {
            boolean z;
            if (context.getApplicationInfo().targetSdkVersion < 17 || !context.getApplicationInfo().hasRtlSupport()) {
                z = true;
            } else {
                z = false;
            }
            this.mIsRtlCompatibilityMode = z;
            this.mOverride = false;
        }

        public void resolveWithLayoutDirection(int layoutDirection) {
            this.mShowing[0] = this.mDrawableLeftInitial;
            this.mShowing[2] = this.mDrawableRightInitial;
            if (!this.mIsRtlCompatibilityMode) {
                switch (layoutDirection) {
                    case 1:
                        if (this.mOverride) {
                            this.mShowing[2] = this.mDrawableStart;
                            this.mDrawableSizeRight = this.mDrawableSizeStart;
                            this.mDrawableHeightRight = this.mDrawableHeightStart;
                            this.mShowing[0] = this.mDrawableEnd;
                            this.mDrawableSizeLeft = this.mDrawableSizeEnd;
                            this.mDrawableHeightLeft = this.mDrawableHeightEnd;
                            break;
                        }
                        break;
                    default:
                        if (this.mOverride) {
                            this.mShowing[0] = this.mDrawableStart;
                            this.mDrawableSizeLeft = this.mDrawableSizeStart;
                            this.mDrawableHeightLeft = this.mDrawableHeightStart;
                            this.mShowing[2] = this.mDrawableEnd;
                            this.mDrawableSizeRight = this.mDrawableSizeEnd;
                            this.mDrawableHeightRight = this.mDrawableHeightEnd;
                            break;
                        }
                        break;
                }
            }
            if (this.mDrawableStart != null && this.mShowing[0] == null) {
                this.mShowing[0] = this.mDrawableStart;
                this.mDrawableSizeLeft = this.mDrawableSizeStart;
                this.mDrawableHeightLeft = this.mDrawableHeightStart;
            }
            if (this.mDrawableEnd != null && this.mShowing[2] == null) {
                this.mShowing[2] = this.mDrawableEnd;
                this.mDrawableSizeRight = this.mDrawableSizeEnd;
                this.mDrawableHeightRight = this.mDrawableHeightEnd;
            }
            applyErrorDrawableIfNeeded(layoutDirection);
            updateDrawablesLayoutDirection(layoutDirection);
        }

        private void updateDrawablesLayoutDirection(int layoutDirection) {
            for (Drawable dr : this.mShowing) {
                if (dr != null) {
                    dr.setLayoutDirection(layoutDirection);
                }
            }
        }

        public void setErrorDrawable(Drawable dr, TextView tv) {
            if (!(this.mDrawableError == dr || this.mDrawableError == null)) {
                this.mDrawableError.setCallback(null);
            }
            this.mDrawableError = dr;
            if (this.mDrawableError != null) {
                Rect compoundRect = this.mCompoundRect;
                this.mDrawableError.setState(tv.getDrawableState());
                this.mDrawableError.copyBounds(compoundRect);
                this.mDrawableError.setCallback(tv);
                this.mDrawableSizeError = compoundRect.width();
                this.mDrawableHeightError = compoundRect.height();
                return;
            }
            this.mDrawableHeightError = 0;
            this.mDrawableSizeError = 0;
        }

        private void applyErrorDrawableIfNeeded(int layoutDirection) {
            switch (this.mDrawableSaved) {
                case 0:
                    this.mShowing[2] = this.mDrawableTemp;
                    this.mDrawableSizeRight = this.mDrawableSizeTemp;
                    this.mDrawableHeightRight = this.mDrawableHeightTemp;
                    break;
                case 1:
                    this.mShowing[0] = this.mDrawableTemp;
                    this.mDrawableSizeLeft = this.mDrawableSizeTemp;
                    this.mDrawableHeightLeft = this.mDrawableHeightTemp;
                    break;
            }
            if (this.mDrawableError != null) {
                switch (layoutDirection) {
                    case 1:
                        this.mDrawableSaved = 1;
                        this.mDrawableTemp = this.mShowing[0];
                        this.mDrawableSizeTemp = this.mDrawableSizeLeft;
                        this.mDrawableHeightTemp = this.mDrawableHeightLeft;
                        this.mShowing[0] = this.mDrawableError;
                        this.mDrawableSizeLeft = this.mDrawableSizeError;
                        this.mDrawableHeightLeft = this.mDrawableHeightError;
                        return;
                    default:
                        this.mDrawableSaved = 0;
                        this.mDrawableTemp = this.mShowing[2];
                        this.mDrawableSizeTemp = this.mDrawableSizeRight;
                        this.mDrawableHeightTemp = this.mDrawableHeightRight;
                        this.mShowing[2] = this.mDrawableError;
                        this.mDrawableSizeRight = this.mDrawableSizeError;
                        this.mDrawableHeightRight = this.mDrawableHeightError;
                        return;
                }
            }
        }
    }

    private static final class Marquee {
        private static final int MARQUEE_DELAY = 1200;
        private static final float MARQUEE_DELTA_MAX = 0.07f;
        private static final int MARQUEE_DP_PER_SECOND = 30;
        private static final byte MARQUEE_RUNNING = (byte) 2;
        private static final byte MARQUEE_STARTING = (byte) 1;
        private static final byte MARQUEE_STOPPED = (byte) 0;
        private final Choreographer mChoreographer;
        private float mFadeStop;
        private float mGhostOffset;
        private float mGhostStart;
        private long mLastAnimationMs;
        private float mMaxFadeScroll;
        private float mMaxScroll;
        private final float mPixelsPerSecond;
        private int mRepeatLimit;
        private FrameCallback mRestartCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                if (Marquee.this.mStatus == (byte) 2) {
                    if (Marquee.this.mRepeatLimit >= 0) {
                        Marquee.this.mRepeatLimit = Marquee.this.mRepeatLimit - 1;
                    }
                    Marquee.this.start(Marquee.this.mRepeatLimit);
                }
            }
        };
        private float mScroll;
        private FrameCallback mStartCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                Marquee.this.mStatus = (byte) 2;
                Marquee.this.mLastAnimationMs = Marquee.this.mChoreographer.getFrameTime();
                Marquee.this.tick();
            }
        };
        private byte mStatus = (byte) 0;
        private FrameCallback mTickCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                Marquee.this.tick();
            }
        };
        private final WeakReference<TextView> mView;

        Marquee(TextView v) {
            this.mPixelsPerSecond = 30.0f * v.getContext().getResources().getDisplayMetrics().density;
            this.mView = new WeakReference(v);
            this.mChoreographer = Choreographer.getInstance();
        }

        void tick() {
            if (this.mStatus == (byte) 2) {
                this.mChoreographer.removeFrameCallback(this.mTickCallback);
                TextView textView = (TextView) this.mView.get();
                if (textView == null) {
                    return;
                }
                if (textView.isFocused() || textView.isSelected()) {
                    long currentMs = this.mChoreographer.getFrameTime();
                    long deltaMs = currentMs - this.mLastAnimationMs;
                    this.mLastAnimationMs = currentMs;
                    this.mScroll += (((float) deltaMs) / 1000.0f) * this.mPixelsPerSecond;
                    if (this.mScroll > this.mMaxScroll) {
                        this.mScroll = this.mMaxScroll;
                        this.mChoreographer.postFrameCallbackDelayed(this.mRestartCallback, 1200);
                    } else {
                        this.mChoreographer.postFrameCallback(this.mTickCallback);
                    }
                    textView.invalidate();
                }
            }
        }

        void stop() {
            this.mStatus = (byte) 0;
            this.mChoreographer.removeFrameCallback(this.mStartCallback);
            this.mChoreographer.removeFrameCallback(this.mRestartCallback);
            this.mChoreographer.removeFrameCallback(this.mTickCallback);
            resetScroll();
        }

        private void resetScroll() {
            this.mScroll = 0.0f;
            TextView textView = (TextView) this.mView.get();
            if (textView != null) {
                textView.invalidate();
            }
        }

        void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            this.mRepeatLimit = repeatLimit;
            TextView textView = (TextView) this.mView.get();
            if (textView != null && textView.mLayout != null) {
                this.mStatus = (byte) 1;
                this.mScroll = 0.0f;
                int textWidth = (textView.getWidth() - textView.getCompoundPaddingLeft()) - textView.getCompoundPaddingRight();
                float lineWidth = textView.mLayout.getLineWidth(0);
                float gap = ((float) textWidth) / 3.0f;
                this.mGhostStart = (lineWidth - ((float) textWidth)) + gap;
                this.mMaxScroll = this.mGhostStart + ((float) textWidth);
                this.mGhostOffset = lineWidth + gap;
                this.mFadeStop = (((float) textWidth) / 6.0f) + lineWidth;
                this.mMaxFadeScroll = (this.mGhostStart + lineWidth) + lineWidth;
                textView.invalidate();
                this.mChoreographer.postFrameCallback(this.mStartCallback);
            }
        }

        float getGhostOffset() {
            return this.mGhostOffset;
        }

        float getScroll() {
            return this.mScroll;
        }

        float getMaxFadeScroll() {
            return this.mMaxFadeScroll;
        }

        boolean shouldDrawLeftFade() {
            return this.mScroll <= this.mFadeStop;
        }

        boolean shouldDrawGhost() {
            return this.mStatus == (byte) 2 && this.mScroll > this.mGhostStart;
        }

        boolean isRunning() {
            return this.mStatus == (byte) 2;
        }

        boolean isStopped() {
            return this.mStatus == (byte) 0;
        }
    }

    private class MoreInfoHPW extends HoverPopupWindow {
        private static final boolean DEBUG = true;
        private static final int ID_INFOVIEW = 117510676;
        private static final String TAG = "MoreInfoHPW";
        private int mInitialMaxLine = 7;
        private int mLastOrientation = 0;
        TextView mParentTextView = null;

        public MoreInfoHPW(View parentView, int type) {
            super(parentView, type);
            if (this.mParentView instanceof TextView) {
                this.mParentTextView = (TextView) this.mParentView;
                return;
            }
            Log.e(TAG, "Parent view is not a TextView");
            this.mParentTextView = new TextView(TextView.this.mContext);
        }

        protected void setInstanceByType(int type) {
            super.setInstanceByType(type);
            if (type == 2) {
                this.mPopupGravity = 12849;
                this.mAnimationStyle = R.style.Animation_HoverPopup;
                this.mHoverDetectTimeMS = 300;
            }
        }

        public boolean isHoverPopupPossible() {
            boolean ret;
            if (this.mPopupType != 2) {
                ret = super.isHoverPopupPossible();
            } else if (this.mShowPopupAlways) {
                return true;
            } else {
                ret = false;
                if (!TextUtils.isEmpty(this.mContentText)) {
                    ret = true;
                } else if (this.mParentTextView.getLineCount() == 1 && this.mParentTextView.canMarquee()) {
                    ret = true;
                } else if (this.mParentTextView.mLayout != null && this.mParentTextView.mLayout.getEllipsisCount(0) > 0) {
                    ret = true;
                }
            }
            return ret;
        }

        protected void makeDefaultContentView() {
            TextView v;
            int orientation = TextView.this.mContext.getResources().getConfiguration().orientation;
            if (this.mContentView != null && this.mContentView.getId() == ID_INFOVIEW && orientation == this.mLastOrientation) {
                v = this.mContentView;
            } else {
                LayoutInflater inflater;
                if ((Build.PRODUCT == null || !Build.PRODUCT.startsWith("gt5note")) && getUspLevel() <= 3) {
                    inflater = LayoutInflater.from(TextView.this.mContext);
                } else {
                    inflater = LayoutInflater.from(new ContextThemeWrapper(TextView.this.mContext, (int) R.style.Theme_DeviceDefault_Light));
                }
                v = (TextView) inflater.inflate((int) R.layout.hover_text_popup, null);
                v.setHoverPopupType(0);
                v.setId(ID_INFOVIEW);
                this.mInitialMaxLine = v.getMaxLines();
                this.mLastOrientation = orientation;
            }
            CharSequence text = !TextUtils.isEmpty(this.mContentText) ? this.mContentText : this.mParentTextView.getText();
            if (TextUtils.isEmpty(text)) {
                v = null;
            } else {
                v.setText(text.toString());
                v.setEllipsize(TruncateAt.END);
            }
            this.mContentView = v;
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        ParcelableParcel editorState;
        CharSequence error;
        boolean frozenWithFocus;
        int selEnd;
        int selStart;
        CharSequence text;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.selStart);
            out.writeInt(this.selEnd);
            out.writeInt(this.frozenWithFocus ? 1 : 0);
            TextUtils.writeToParcel(this.text, out, flags);
            if (this.error == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                TextUtils.writeToParcel(this.error, out, flags);
            }
            if (this.editorState == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            this.editorState.writeToParcel(out, flags);
        }

        public String toString() {
            String str = "TextView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " start=" + this.selStart + " end=" + this.selEnd;
            if (this.text != null) {
                str = str + " text=" + this.text;
            }
            return str + "}";
        }

        private SavedState(Parcel in) {
            super(in);
            this.selStart = in.readInt();
            this.selEnd = in.readInt();
            this.frozenWithFocus = in.readInt() != 0;
            this.text = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() != 0) {
                this.error = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if (in.readInt() != 0) {
                this.editorState = (ParcelableParcel) ParcelableParcel.CREATOR.createFromParcel(in);
            }
        }
    }

    private class StylusEventListener implements OnStylusButtonEventListener {
        private boolean mDirLTR = true;
        private int mEndCurPosition = 0;
        private boolean mIsSelectableTextView = false;
        private float mMaxX = 0.0f;
        private boolean mPressed = false;
        private int mStartCurPosition = 0;
        private float mStartX = 0.0f;
        private float mStartY = 0.0f;
        private TextView mThisView = null;
        private int oldEndPos = -1;
        private int[] selectRange = new int[2];

        public StylusEventListener(TextView textView) {
            this.mThisView = textView;
        }

        public void onStylusButtonEvent(MotionEvent event, int clipboardId) {
            int action = event.getActionMasked();
            if (event.getToolType(0) == 2 && TextView.this.mEnableMultiSelection) {
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                if ((action != 0 || (event.getButtonState() & 32) == 0) && action != 211) {
                    TextView.mIsFindTargetView = false;
                    if (!this.mPressed) {
                        return;
                    }
                    if (TextView.mTargetView == null) {
                        return;
                    }
                }
                if (!TextView.mIsFindTargetView) {
                    if (TextView.mTargetView != null) {
                        TextView.mTargetView.clearMultiSelection();
                        TextView.mTargetView = null;
                    }
                    if (TextView.this.canTextMultiSelection()) {
                        TextView.mCurTime = SystemClock.uptimeMillis();
                        if (TextView.mLastHoveredView != null && TextView.mCurTime - TextView.mLastHoveredTime < 100) {
                            TextView.mTargetView = TextView.mLastHoveredView;
                        }
                    }
                    TextView.mLastHoveredView = null;
                    TextView.mIsFindTargetView = true;
                }
                if (!TextView.mIsFindTargetView || (TextView.mTargetView != null && this.mThisView != null)) {
                    if (TextView.mTargetView != this.mThisView) {
                        this.mThisView.hideCursorControllers();
                        return;
                    } else if (TextView.this.mMarquee != null && !TextView.this.mMarquee.isStopped()) {
                        return;
                    } else {
                        if (TextView.mTargetView instanceof EditText) {
                            TextView.mTargetView.hideCursorControllers();
                            if (!(TextView.this.checkPosInView((int) rawX, (int) rawY, 0) && TextView.this.canSelectText())) {
                                return;
                            }
                        } else if (!TextView.this.checkPosOnText((int) rawX, (int) rawY, (int) TextView.this.TOUCH_DELTA)) {
                            return;
                        }
                    }
                }
                return;
                CharSequence text = TextView.this.getTextForMultiSelection();
                if (!TextUtils.isEmpty(text)) {
                    Point startPos;
                    float endX;
                    float endY;
                    switch (action) {
                        case 0:
                        case 211:
                            if (!this.mPressed) {
                                TextView.this.hideMultiSelectPopupWindow();
                                boolean z = TextView.this.isTextSelectable() || (this.mThisView instanceof EditText);
                                this.mIsSelectableTextView = z;
                                startPos = TextView.this.getScreenPointOfView(this.mThisView);
                                this.mStartX = rawX - ((float) startPos.x);
                                this.mStartY = rawY - ((float) startPos.y);
                                this.mMaxX = 0.0f;
                                this.mStartCurPosition = TextView.this.getOffsetForPosition(this.mStartX, this.mStartY);
                                if (this.mStartCurPosition >= 0) {
                                    if (TextView.this.isMultiPenSelectionEnabled() && !this.mIsSelectableTextView) {
                                        if (TextView.mCurTime - TextView.mLastPenDownTime < ((long) ViewConfiguration.getDoubleTapTimeout())) {
                                            if (TextView.mShowPenSelectionRunnable != null) {
                                                this.mThisView.removeCallbacks(TextView.mShowPenSelectionRunnable);
                                                TextView.mShowPenSelectionRunnable = null;
                                            }
                                            MultiSelection.setIsMultiSelectingText(false);
                                            MultiSelection.setNeedToScroll(false);
                                            TextView.mLastPenDownTime = TextView.mCurTime;
                                            return;
                                        }
                                        MultiSelection.setIsMultiSelectingText(true);
                                        if (!TextView.this.getVisibleTextRange(this.selectRange)) {
                                            this.selectRange[0] = 0;
                                            this.selectRange[1] = text.length();
                                        }
                                        if (this.mStartCurPosition < this.selectRange[0]) {
                                            this.mStartCurPosition = this.selectRange[0];
                                        } else if (this.mStartCurPosition > this.selectRange[1]) {
                                            this.mStartCurPosition = this.selectRange[1];
                                        }
                                        TextView.mLastPenDownTime = TextView.mCurTime;
                                        Log.d(TextView.LOG_TAG, "Pen down with side button! : start text selection");
                                    }
                                    this.oldEndPos = this.mStartCurPosition;
                                    this.mPressed = true;
                                    TextView.this.mIsTouchDown = true;
                                }
                                MultiSelection.setNeedToScroll(false);
                                return;
                            }
                            return;
                        case 1:
                        case 212:
                            break;
                        case 2:
                        case 213:
                            if ((event.getButtonState() & 32) != 0) {
                                if (TextView.this.isMultiPenSelectionEnabled()) {
                                    startPos = TextView.this.getScreenPointOfView(this.mThisView);
                                    endX = rawX - ((float) startPos.x);
                                    endY = rawY - ((float) startPos.y);
                                    MultiSelection.setNeedToScroll(TextView.this.checkPosInView((int) rawX, (int) rawY, 0));
                                    if (this.mMaxX < Math.abs(this.mStartX - endX)) {
                                        this.mMaxX = Math.abs(this.mStartX - endX);
                                    }
                                    this.mEndCurPosition = TextView.this.getOffsetForPosition(endX, endY);
                                    if (!this.mIsSelectableTextView) {
                                        if (this.mEndCurPosition < this.selectRange[0]) {
                                            this.mEndCurPosition = this.selectRange[0];
                                        } else if (this.mEndCurPosition > this.selectRange[1]) {
                                            this.mEndCurPosition = this.selectRange[1];
                                        }
                                    }
                                    if (this.mStartCurPosition == this.mEndCurPosition) {
                                        if (!this.mIsSelectableTextView) {
                                            MultiSelection.removeCurSelection((Spannable) text);
                                            return;
                                        }
                                        return;
                                    } else if (this.oldEndPos != this.mEndCurPosition && this.mEndCurPosition >= 0) {
                                        this.oldEndPos = this.mEndCurPosition;
                                        TextView.this.mHighlightPathBogus = true;
                                        if (this.mIsSelectableTextView) {
                                            if (!this.mThisView.isFocused()) {
                                                this.mThisView.requestFocus();
                                            }
                                            Selection.setSelection((Spannable) text, this.mStartCurPosition, this.mEndCurPosition);
                                            return;
                                        }
                                        if (this.mStartCurPosition > text.length()) {
                                            this.mStartCurPosition = text.length();
                                        }
                                        if (this.mEndCurPosition > text.length()) {
                                            this.mEndCurPosition = text.length();
                                        }
                                        MultiSelection.setSelection((Spannable) text, this.mStartCurPosition, this.mEndCurPosition);
                                        TextView.this.mhasMultiSelection = true;
                                        return;
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                            break;
                        case 3:
                        case 214:
                            TextView.this.mIsTouchDown = false;
                            this.mPressed = false;
                            if (this.mIsSelectableTextView) {
                                TextView.this.mhasMultiSelection = false;
                                if (this.mStartCurPosition >= 0) {
                                    Selection.setSelection((Spannable) text, this.mStartCurPosition);
                                    return;
                                }
                                return;
                            }
                            MultiSelection.setIsMultiSelectingText(false);
                            MultiSelection.removeCurSelection((Spannable) text);
                            TextView.this.mhasMultiSelection = MultiSelection.getMultiSelectionCount((Spannable) text) > 0;
                            return;
                        default:
                            return;
                    }
                    startPos = TextView.this.getScreenPointOfView(this.mThisView);
                    endX = rawX - ((float) startPos.x);
                    endY = rawY - ((float) startPos.y);
                    if (this.mMaxX < Math.abs(this.mStartX - endX)) {
                        this.mMaxX = Math.abs(this.mStartX - endX);
                    }
                    this.mEndCurPosition = TextView.this.getOffsetForPosition(endX, endY);
                    if (!this.mIsSelectableTextView) {
                        if (this.mEndCurPosition < this.selectRange[0]) {
                            this.mEndCurPosition = this.selectRange[0];
                        } else if (this.mEndCurPosition > this.selectRange[1]) {
                            this.mEndCurPosition = this.selectRange[1];
                        }
                    }
                    boolean isSameLine = false;
                    if (TextView.this.mLayout != null) {
                        isSameLine = TextView.this.getLineAtCoordinate(this.mStartY) == TextView.this.getLineAtCoordinate(endY);
                    }
                    if (this.mIsSelectableTextView || !isSameLine || this.mMaxX >= TextView.this.TOUCH_DELTA) {
                        MultiSelection.setNeedToScroll(false);
                        if (this.mStartCurPosition == this.mEndCurPosition) {
                            this.mPressed = false;
                            if (this.mIsSelectableTextView) {
                                TextView.this.mhasMultiSelection = false;
                                return;
                            }
                            MultiSelection.setIsMultiSelectingText(false);
                            MultiSelection.removeCurSelection((Spannable) text);
                            TextView.this.mhasMultiSelection = MultiSelection.getMultiSelectionCount((Spannable) text) > 0;
                            return;
                        }
                        if (this.mStartCurPosition > this.mEndCurPosition) {
                            int temp = this.mStartCurPosition;
                            this.mStartCurPosition = this.mEndCurPosition;
                            this.mEndCurPosition = temp;
                        }
                        if (!this.mIsSelectableTextView) {
                            MultiSelection.setSelection((Spannable) text, this.mStartCurPosition, this.mEndCurPosition);
                            TextView.this.showMultiSelectPopupWindow();
                            try {
                                new HermesServiceManager(TextView.this.getContext()).training(text.subSequence(this.mStartCurPosition, this.mEndCurPosition).toString());
                            } catch (IllegalStateException e) {
                                Log.e(TextView.LOG_TAG, "** skip HERMES Service by IllegalStateException **");
                            }
                            Log.d(TextView.LOG_TAG, "Pen up with side button! : end text selection");
                            TextView.this.registerForTouchMonitorListener();
                            TextView.this.mhasMultiSelection = true;
                            MultiSelection.setIsMultiSelectingText(false);
                        } else if (this.mStartCurPosition >= 0 && this.mEndCurPosition >= 0 && (TextView.this.isTextSelectionEnabled() || isSameLine)) {
                            Selection.setSelection((Spannable) text, this.mStartCurPosition, this.mEndCurPosition);
                            if (TextView.this.mEditor != null && TextView.this.mEditor.mCreatedWithASelection) {
                                TextView.this.mEditor.stopTextActionMode();
                            }
                            if (!(TextView.this.mEditor == null || (this.mThisView instanceof ExtractEditText))) {
                                if (TextView.this.mEditor.startSelectionActionMode()) {
                                    TextView.this.mEditor.updateSelectionHandler();
                                }
                                TextView.this.mEditor.mCreatedWithASelection = false;
                            }
                        }
                        TextView.this.mIsTouchDown = false;
                        this.mPressed = false;
                        return;
                    }
                    if (TextView.mShowPenSelectionRunnable != null) {
                        this.mThisView.removeCallbacks(TextView.mShowPenSelectionRunnable);
                        TextView.mShowPenSelectionRunnable = null;
                    }
                    TextView.mShowPenSelectionRunnable = new Runnable() {
                        public void run() {
                            CharSequence text = TextView.this.getTextForMultiSelection();
                            if (TextView.this.selectCurrentWordForMultiSelection(StylusEventListener.this.mStartCurPosition, StylusEventListener.this.mEndCurPosition)) {
                                StylusEventListener.this.mStartCurPosition = MultiSelection.getSelectionStart((Spannable) text);
                                StylusEventListener.this.mEndCurPosition = MultiSelection.getSelectionEnd((Spannable) text);
                                TextView.this.showMultiSelectPopupWindow();
                                try {
                                    new HermesServiceManager(TextView.this.getContext()).training(text.subSequence(StylusEventListener.this.mStartCurPosition, StylusEventListener.this.mEndCurPosition).toString());
                                } catch (IllegalStateException e) {
                                    Log.e(TextView.LOG_TAG, "** skip HERMES Service by IllegalStateException **");
                                }
                                Log.d(TextView.LOG_TAG, "Pen up with side button! : end text selection");
                                TextView.this.registerForTouchMonitorListener();
                                TextView.this.mhasMultiSelection = true;
                                MultiSelection.setIsMultiSelectingText(false);
                                MultiSelection.setNeedToScroll(false);
                            }
                        }
                    };
                    this.mThisView.postDelayed(TextView.mShowPenSelectionRunnable, (long) ViewConfiguration.getDoubleTapTimeout());
                    TextView.this.mIsTouchDown = false;
                    this.mPressed = false;
                }
            }
        }
    }

    protected class TextViewClipboardEventListener implements ClipboardEventListener {
        protected TextViewClipboardEventListener() {
        }

        public void onPaste(ClipboardData data) {
            final ClipboardData dataInner = data;
            TextView.this.post(new Runnable() {
                public void run() {
                    TextView.this.pasteClipBoardData(dataInner);
                }
            });
        }
    }

    private class TouchMonitorListener implements OnTouchListener {
        private final int globalTimeForTouch;
        private long mPressTime;
        private float mStartX;
        private float mStartY;

        private TouchMonitorListener() {
            this.mStartX = 0.0f;
            this.mStartY = 0.0f;
            this.globalTimeForTouch = 1000;
            this.mPressTime = 0;
        }

        public void onTouch(MotionEvent event) {
            int action = event.getAction();
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            switch (action) {
                case 0:
                    this.mPressTime = SystemClock.uptimeMillis();
                    this.mStartX = rawX;
                    this.mStartY = rawY;
                    return;
                case 1:
                    long elipseTime = SystemClock.uptimeMillis() - this.mPressTime;
                    float moveX = Math.abs(rawX - this.mStartX);
                    float moveY = Math.abs(rawY - this.mStartY);
                    boolean flag = (moveX * moveX) + (moveY * moveY) <= TextView.this.TOUCH_DELTA * TextView.this.TOUCH_DELTA;
                    if (MultiSelection.getIsMultiSelectingText() || elipseTime >= 1000 || !flag) {
                        if (!TextView.this.isValidMultiSelection()) {
                            TextView.this.clearMultiSelection();
                            return;
                        }
                        return;
                    } else if (!TextView.this.mPenSelectionController.isPenSelectionArea(TextView.this.getContext(), TextView.this.getRootView(), (int) rawX, (int) rawY)) {
                        TextView.this.clearAllMultiSelection();
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    static {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.measureText("H");
    }

    private void pasteClipBoardData(ClipboardData data) {
        int min = 0;
        int max = this.mText.length();
        CharSequence prevString = this.mText.toString();
        if ((this.mText instanceof Editable) && hasFocus() && data != null) {
            if (isFocused()) {
                int selStart = getSelectionStart();
                int selEnd = getSelectionEnd();
                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            CharSequence paste = null;
            switch (data.GetFomat()) {
                case 2:
                    paste = ((ClipboardDataText) data).GetText();
                    break;
                case 4:
                    paste = ((ClipboardDataHtml) data).getPlainText();
                    break;
                case 5:
                    paste = ((ClipboardDataUri) data).GetUri().toString();
                    break;
                case 6:
                    ClipboardDataIntent intent = (ClipboardDataIntent) data;
                    if (intent.GetIntent() != null) {
                        paste = intent.GetIntent().getDataString();
                        break;
                    }
                    break;
                case 7:
                    ClipboardDataMultipleUri multipleUri = (ClipboardDataMultipleUri) data;
                    if (multipleUri.GetMultipleUri() != null) {
                        paste = multipleUri.GetMultipleUri().toString();
                        break;
                    }
                    break;
            }
            if (paste != null && paste.length() > 0) {
                paste = removeEasyEditSpan(paste);
                Selection.setSelection((Spannable) this.mText, max);
                if (this.mText instanceof Spanned) {
                    paste = new SpannableStringBuilder(paste);
                }
                ((Editable) this.mText).replace(min, max, paste);
                stopTextActionMode();
            }
        }
    }

    protected ClipboardExManager getClipboardExManager() {
        if (this.mClipboardExManager == null) {
            this.mClipboardExManager = (ClipboardExManager) getContext().getSystemService("clipboardEx");
        }
        return this.mClipboardExManager;
    }

    protected void destroyHardwareResources() {
        super.destroyHardwareResources();
        if (this.mEditor != null) {
            this.mEditor.invalidateTextDisplayList();
        }
    }

    public TextView(Context context) {
        this(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.textViewStyle);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int n;
        int i;
        int attr;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCursorWidth = 1;
        this.mChangedSelectionBySIP = false;
        this.fromResLock = false;
        this.mEditableFactory = Factory.getInstance();
        this.mSpannableFactory = Spannable.Factory.getInstance();
        this.mEllipsisKeywordStart = -1;
        this.mEllipsisKeywordCount = -1;
        this.mToolType = 0;
        this.isElasticEnabled = true;
        this.mPasteEventListener = null;
        this.mClipboardExManager = null;
        this.SEC_CLIPBOARD_UNKNOWN = -1;
        this.SEC_CLIPBOARD_DISABLED = 0;
        this.SEC_CLIPBOARD_ENABLED = 1;
        this.mIsSecClipboardEnabled = this.SEC_CLIPBOARD_UNKNOWN;
        this.mWBMaxLength = -1;
        this.mDrawTextStrikeAnimator = null;
        this.mDrawStrikeAnimationValue = 0.0f;
        this.mTextStrikeThroughEnabled = false;
        this.mLineIsDrawed = false;
        this.mSkipDrawTextStrike = false;
        this.mMarqueeRepeatLimit = 3;
        this.mLastLayoutDirection = -1;
        this.mMarqueeFadeMode = 0;
        this.mBufferType = BufferType.NORMAL;
        this.mDisplayText = null;
        this.mUseDisplayText = false;
        this.mSkipUpdateDisplayText = false;
        this.mhasMultiSelection = false;
        this.mEnableMultiSelection = true;
        this.mEnableLinkPreview = false;
        this.mCanTextMultiSelection = false;
        this.mMultiHighlightColor = -1728022343;
        this.doShowingHermes = false;
        this.HOVER_INTERVAL = 300;
        this.mHoveredSpan = null;
        this.mHoverEnterTime = -1;
        this.mHoverExitTime = -1;
        this.TOUCH_DELTA = 12.0f;
        this.mCoverManager = null;
        this.mWordIteratorForMultiSelection = null;
        this.mPenSelectionController = PenSelectionController.getInstance();
        this.mLocaleChanged = false;
        this.mBackuppedLetterSpacing = HCT_LETTER_SPACING_NOT_BACKUPPED;
        this.mGravity = 8388659;
        this.mLinksClickable = true;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mMaximum = Integer.MAX_VALUE;
        this.mMaxMode = 1;
        this.mMinimum = 0;
        this.mMinMode = 1;
        this.mOldMaximum = this.mMaximum;
        this.mOldMaxMode = this.mMaxMode;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxWidthMode = 2;
        this.mMinWidth = 0;
        this.mMinWidthMode = 2;
        this.mDesiredHeightAtMeasure = -1;
        this.mIncludePad = true;
        this.mDeferScroll = -1;
        this.mFilters = NO_FILTERS;
        this.mHighlightColor = 1714664933;
        this.mHighlightPathBogus = true;
        this.mFirstTouch = false;
        this.mLastTouchUpTime = 0;
        this.mClipboardDataFormat = 2;
        this.mOnDragResult = true;
        this.mHideSoftInput = false;
        this.mIsTouchDown = false;
        this.mCanSelectText = true;
        this.mEnableShare = true;
        this.mEnableClipboard = true;
        this.mEnableDictionary = true;
        this.mEnableWebSearch = false;
        this.mWBTextBuffer = null;
        this.mMultiSelectPopupWindow = MultiSelectPopupWindow.getInstance();
        this.mText = "";
        Resources res = getResources();
        CompatibilityInfo compat = res.getCompatibilityInfo();
        this.mRestrictionPolicy = EnterpriseDeviceManager.getInstance().getRestrictionPolicy();
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.density = res.getDisplayMetrics().density;
        this.mTextPaint.setCompatibilityScaling(compat.applicationScale);
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setCompatibilityScaling(compat.applicationScale);
        this.mMultiHighlightPaint = new Paint(1);
        this.mMultiHighlightPaint.setCompatibilityScaling(compat.applicationScale);
        this.TOUCH_DELTA = this.mTextPaint.density * 12.0f;
        this.mMovement = getDefaultMovementMethod();
        this.mTransformation = null;
        int textColorHighlight = 0;
        ColorStateList textColor = null;
        ColorStateList textColorHint = null;
        ColorStateList textColorLink = null;
        int textSize = 15;
        String fontFamily = null;
        boolean fontFamilyExplicit = false;
        int typefaceIndex = -1;
        int styleIndex = -1;
        boolean allCaps = false;
        int shadowcolor = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float r = 0.0f;
        boolean elegant = false;
        float letterSpacing = 0.0f;
        String fontFeatureSettings = null;
        this.mBreakStrategy = 0;
        this.mHyphenationFrequency = 0;
        Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.TextViewAppearance, defStyleAttr, defStyleRes);
        TypedArray appearance = null;
        int ap = a.getResourceId(0, -1);
        a.recycle();
        if (ap != -1) {
            appearance = theme.obtainStyledAttributes(ap, R.styleable.TextAppearance);
        }
        if (appearance != null) {
            n = appearance.getIndexCount();
            for (i = 0; i < n; i++) {
                attr = appearance.getIndex(i);
                switch (attr) {
                    case 0:
                        textSize = appearance.getDimensionPixelSize(attr, textSize);
                        break;
                    case 1:
                        typefaceIndex = appearance.getInt(attr, -1);
                        break;
                    case 2:
                        styleIndex = appearance.getInt(attr, -1);
                        break;
                    case 3:
                        textColor = appearance.getColorStateList(attr);
                        break;
                    case 4:
                        textColorHighlight = appearance.getColor(attr, textColorHighlight);
                        break;
                    case 5:
                        textColorHint = appearance.getColorStateList(attr);
                        break;
                    case 6:
                        textColorLink = appearance.getColorStateList(attr);
                        break;
                    case 7:
                        shadowcolor = appearance.getInt(attr, 0);
                        break;
                    case 8:
                        dx = appearance.getFloat(attr, 0.0f);
                        break;
                    case 9:
                        dy = appearance.getFloat(attr, 0.0f);
                        break;
                    case 10:
                        r = appearance.getFloat(attr, 0.0f);
                        break;
                    case 11:
                        allCaps = appearance.getBoolean(attr, false);
                        break;
                    case 12:
                        fontFamily = appearance.getString(attr);
                        break;
                    case 13:
                        elegant = appearance.getBoolean(attr, false);
                        break;
                    case 14:
                        letterSpacing = appearance.getFloat(attr, 0.0f);
                        break;
                    case 15:
                        fontFeatureSettings = appearance.getString(attr);
                        break;
                    default:
                        break;
                }
            }
            appearance.recycle();
        }
        boolean editable = getDefaultEditable();
        CharSequence inputMethod = null;
        int numeric = 0;
        CharSequence digits = null;
        boolean phone = false;
        boolean autotext = false;
        int autocap = -1;
        int buffertype = 0;
        boolean selectallonfocus = false;
        Drawable drawableLeft = null;
        Drawable drawableTop = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        Drawable drawableStart = null;
        Drawable drawableEnd = null;
        ColorStateList drawableTint = null;
        Mode drawableTintMode = null;
        int drawablePadding = 0;
        int ellipsize = -1;
        boolean singleLine = false;
        int maxlength = -1;
        CharSequence text = "";
        CharSequence hint = null;
        boolean password = false;
        int inputType = 0;
        int stringID = -1;
        this.mCursorWidth = (int) getContext().getResources().getDimension(R.dimen.tw_edittext_field_cursor_width);
        a = theme.obtainStyledAttributes(attrs, R.styleable.TextView, defStyleAttr, defStyleRes);
        n = a.getIndexCount();
        for (i = 0; i < n; i++) {
            attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    setEnabled(a.getBoolean(attr, isEnabled()));
                    break;
                case 2:
                    textSize = a.getDimensionPixelSize(attr, textSize);
                    break;
                case 3:
                    typefaceIndex = a.getInt(attr, typefaceIndex);
                    break;
                case 4:
                    styleIndex = a.getInt(attr, styleIndex);
                    break;
                case 5:
                    textColor = a.getColorStateList(attr);
                    break;
                case 6:
                    textColorHighlight = a.getColor(attr, textColorHighlight);
                    break;
                case 7:
                    textColorHint = a.getColorStateList(attr);
                    break;
                case 8:
                    textColorLink = a.getColorStateList(attr);
                    break;
                case 9:
                    ellipsize = a.getInt(attr, ellipsize);
                    break;
                case 10:
                    setGravity(a.getInt(attr, -1));
                    break;
                case 11:
                    this.mAutoLinkMask = a.getInt(attr, 0);
                    break;
                case 12:
                    this.mLinksClickable = a.getBoolean(attr, true);
                    break;
                case 13:
                    setMaxWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case 14:
                    setMaxHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case 15:
                    setMinWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case 16:
                    setMinHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case 17:
                    buffertype = a.getInt(attr, buffertype);
                    break;
                case 18:
                    text = a.getText(attr);
                    if (!Build.IS_SYSTEM_SECURE) {
                        break;
                    }
                    stringID = a.getResourceId(attr, -1);
                    break;
                case 19:
                    hint = a.getText(attr);
                    if (!Build.IS_SYSTEM_SECURE) {
                        break;
                    }
                    stringID = a.getResourceId(attr, -1);
                    break;
                case 20:
                    setTextScaleX(a.getFloat(attr, 1.0f));
                    break;
                case 21:
                    if (!a.getBoolean(attr, true)) {
                        setCursorVisible(false);
                        break;
                    }
                    break;
                case 22:
                    setMaxLines(a.getInt(attr, -1));
                    break;
                case 23:
                    setLines(a.getInt(attr, -1));
                    break;
                case 24:
                    setHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case 25:
                    setMinLines(a.getInt(attr, -1));
                    break;
                case 26:
                    setMaxEms(a.getInt(attr, -1));
                    break;
                case 27:
                    setEms(a.getInt(attr, -1));
                    break;
                case 28:
                    setWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case 29:
                    setMinEms(a.getInt(attr, -1));
                    break;
                case 30:
                    if (!a.getBoolean(attr, false)) {
                        break;
                    }
                    setHorizontallyScrolling(true);
                    break;
                case 31:
                    password = a.getBoolean(attr, password);
                    break;
                case 32:
                    singleLine = a.getBoolean(attr, singleLine);
                    break;
                case 33:
                    selectallonfocus = a.getBoolean(attr, selectallonfocus);
                    break;
                case 34:
                    if (!a.getBoolean(attr, true)) {
                        setIncludeFontPadding(false);
                        break;
                    }
                    break;
                case 35:
                    maxlength = a.getInt(attr, -1);
                    break;
                case 36:
                    shadowcolor = a.getInt(attr, 0);
                    break;
                case 37:
                    dx = a.getFloat(attr, 0.0f);
                    break;
                case 38:
                    dy = a.getFloat(attr, 0.0f);
                    break;
                case 39:
                    r = a.getFloat(attr, 0.0f);
                    break;
                case 40:
                    numeric = a.getInt(attr, numeric);
                    break;
                case 41:
                    digits = a.getText(attr);
                    break;
                case 42:
                    phone = a.getBoolean(attr, phone);
                    break;
                case 43:
                    inputMethod = a.getText(attr);
                    break;
                case 44:
                    autocap = a.getInt(attr, autocap);
                    break;
                case 45:
                    autotext = a.getBoolean(attr, autotext);
                    break;
                case 46:
                    editable = a.getBoolean(attr, editable);
                    break;
                case 47:
                    this.mFreezesText = a.getBoolean(attr, false);
                    break;
                case 48:
                    drawableTop = a.getDrawable(attr);
                    break;
                case 49:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case 50:
                    drawableLeft = a.getDrawable(attr);
                    break;
                case 51:
                    drawableRight = a.getDrawable(attr);
                    break;
                case 52:
                    drawablePadding = a.getDimensionPixelSize(attr, drawablePadding);
                    break;
                case 53:
                    this.mSpacingAdd = (float) a.getDimensionPixelSize(attr, (int) this.mSpacingAdd);
                    break;
                case 54:
                    this.mSpacingMult = a.getFloat(attr, this.mSpacingMult);
                    break;
                case 55:
                    setMarqueeRepeatLimit(a.getInt(attr, this.mMarqueeRepeatLimit));
                    break;
                case 56:
                    inputType = a.getInt(attr, 0);
                    break;
                case 57:
                    setPrivateImeOptions(a.getString(attr));
                    break;
                case 58:
                    try {
                        setInputExtras(a.getResourceId(attr, 0));
                        break;
                    } catch (Throwable e) {
                        Log.w(LOG_TAG, "Failure reading input extras", e);
                        break;
                    } catch (Throwable e2) {
                        Log.w(LOG_TAG, "Failure reading input extras", e2);
                        break;
                    }
                case 59:
                    createEditorIfNeeded();
                    this.mEditor.createInputContentTypeIfNeeded();
                    this.mEditor.mInputContentType.imeOptions = a.getInt(attr, this.mEditor.mInputContentType.imeOptions);
                    break;
                case 60:
                    createEditorIfNeeded();
                    this.mEditor.createInputContentTypeIfNeeded();
                    this.mEditor.mInputContentType.imeActionLabel = a.getText(attr);
                    break;
                case 61:
                    createEditorIfNeeded();
                    this.mEditor.createInputContentTypeIfNeeded();
                    this.mEditor.mInputContentType.imeActionId = a.getInt(attr, this.mEditor.mInputContentType.imeActionId);
                    break;
                case 62:
                    this.mTextSelectHandleLeftRes = a.getResourceId(attr, 0);
                    break;
                case 63:
                    this.mTextSelectHandleRightRes = a.getResourceId(attr, 0);
                    break;
                case 64:
                    this.mTextSelectHandleRes = a.getResourceId(attr, 0);
                    break;
                case 67:
                    setTextIsSelectable(a.getBoolean(attr, false));
                    break;
                case 70:
                    this.mCursorDrawableRes = a.getResourceId(attr, 0);
                    break;
                case 71:
                    this.mTextEditSuggestionItemLayout = a.getResourceId(attr, 0);
                    break;
                case 72:
                    allCaps = a.getBoolean(attr, false);
                    break;
                case 73:
                    drawableStart = a.getDrawable(attr);
                    break;
                case 74:
                    drawableEnd = a.getDrawable(attr);
                    break;
                case 75:
                    fontFamily = a.getString(attr);
                    fontFamilyExplicit = true;
                    break;
                case 76:
                    elegant = a.getBoolean(attr, false);
                    break;
                case 77:
                    letterSpacing = a.getFloat(attr, 0.0f);
                    break;
                case 78:
                    fontFeatureSettings = a.getString(attr);
                    break;
                case 79:
                    drawableTint = a.getColorStateList(attr);
                    break;
                case 80:
                    drawableTintMode = Drawable.parseTintMode(a.getInt(attr, -1), drawableTintMode);
                    break;
                case 81:
                    this.mBreakStrategy = a.getInt(attr, 0);
                    break;
                case 82:
                    this.mHyphenationFrequency = a.getInt(attr, 0);
                    break;
                case 83:
                    createEditorIfNeeded();
                    this.mEditor.mAllowUndo = a.getBoolean(attr, true);
                    break;
                case 86:
                    this.mClipboardDataFormat = a.getInt(attr, 2);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        BufferType bufferType = BufferType.EDITABLE;
        int variation = inputType & 4095;
        boolean passwordInputType = variation == 129;
        boolean webPasswordInputType = variation == 225;
        boolean numberPasswordInputType = variation == 18;
        if (inputMethod == null) {
            if (digits == null) {
                if (inputType == 0) {
                    if (!phone) {
                        if (numeric == 0) {
                            if (!autotext && autocap == -1) {
                                if (!isTextSelectable()) {
                                    if (!editable) {
                                        if (this.mEditor != null) {
                                            this.mEditor.mKeyListener = null;
                                        }
                                        switch (buffertype) {
                                            case 0:
                                                bufferType = BufferType.NORMAL;
                                                break;
                                            case 1:
                                                bufferType = BufferType.SPANNABLE;
                                                break;
                                            case 2:
                                                bufferType = BufferType.EDITABLE;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    createEditorIfNeeded();
                                    this.mEditor.mKeyListener = TextKeyListener.getInstance();
                                    this.mEditor.mInputType = 1;
                                } else {
                                    if (this.mEditor != null) {
                                        this.mEditor.mKeyListener = null;
                                        this.mEditor.mInputType = 0;
                                    }
                                    bufferType = BufferType.SPANNABLE;
                                    setMovementMethod(ArrowKeyMovementMethod.getInstance());
                                }
                            } else {
                                Capitalize cap;
                                inputType = 1;
                                switch (autocap) {
                                    case 1:
                                        cap = Capitalize.SENTENCES;
                                        inputType = 1 | 16384;
                                        break;
                                    case 2:
                                        cap = Capitalize.WORDS;
                                        inputType = 1 | 8192;
                                        break;
                                    case 3:
                                        cap = Capitalize.CHARACTERS;
                                        inputType = 1 | 4096;
                                        break;
                                    default:
                                        cap = Capitalize.NONE;
                                        break;
                                }
                                createEditorIfNeeded();
                                this.mEditor.mKeyListener = TextKeyListener.getInstance(autotext, cap);
                                this.mEditor.mInputType = inputType;
                            }
                        } else {
                            createEditorIfNeeded();
                            this.mEditor.mKeyListener = DigitsKeyListener.getInstance((numeric & 2) != 0, (numeric & 4) != 0);
                            inputType = 2;
                            if ((numeric & 2) != 0) {
                                inputType = 2 | 4096;
                            }
                            if ((numeric & 4) != 0) {
                                inputType |= 8192;
                            }
                            this.mEditor.mInputType = inputType;
                        }
                    } else {
                        createEditorIfNeeded();
                        this.mEditor.mKeyListener = DialerKeyListener.getInstance();
                        this.mEditor.mInputType = 3;
                    }
                } else {
                    setInputType(inputType, true);
                    singleLine = !isMultilineInputType(inputType);
                }
            } else {
                createEditorIfNeeded();
                this.mEditor.mKeyListener = DigitsKeyListener.getInstance(digits.toString());
                this.mEditor.mInputType = inputType != 0 ? inputType : 1;
            }
        } else {
            try {
                Class<?> c = Class.forName(inputMethod.toString());
                try {
                    createEditorIfNeeded();
                    this.mEditor.mKeyListener = (KeyListener) c.newInstance();
                    try {
                        int i2;
                        Editor editor = this.mEditor;
                        if (inputType != 0) {
                            i2 = inputType;
                        } else {
                            i2 = this.mEditor.mKeyListener.getInputType();
                        }
                        editor.mInputType = i2;
                    } catch (IncompatibleClassChangeError e3) {
                        this.mEditor.mInputType = 1;
                    }
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                } catch (Throwable ex2) {
                    throw new RuntimeException(ex2);
                }
            } catch (Throwable ex22) {
                throw new RuntimeException(ex22);
            }
        }
        if (this.mEditor != null) {
            this.mEditor.adjustInputType(password, passwordInputType, webPasswordInputType, numberPasswordInputType);
        }
        if (selectallonfocus) {
            createEditorIfNeeded();
            this.mEditor.mSelectAllOnFocus = true;
            if (bufferType == BufferType.NORMAL) {
                bufferType = BufferType.SPANNABLE;
            }
        }
        if (!(drawableTint == null && drawableTintMode == null)) {
            if (this.mDrawables == null) {
                this.mDrawables = new Drawables(context);
            }
            if (drawableTint != null) {
                this.mDrawables.mTintList = drawableTint;
                this.mDrawables.mHasTint = true;
            }
            if (drawableTintMode != null) {
                this.mDrawables.mTintMode = drawableTintMode;
                this.mDrawables.mHasTintMode = true;
            }
        }
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        setRelativeDrawablesIfNeeded(drawableStart, drawableEnd);
        setCompoundDrawablePadding(drawablePadding);
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, singleLine, singleLine);
        if (singleLine && getKeyListener() == null && ellipsize < 0) {
            ellipsize = 3;
        }
        switch (ellipsize) {
            case 1:
                setEllipsize(TruncateAt.START);
                break;
            case 2:
                setEllipsize(TruncateAt.MIDDLE);
                break;
            case 3:
                setEllipsize(TruncateAt.END);
                break;
            case 4:
                if (ViewConfiguration.get(context).isFadingMarqueeEnabled()) {
                    setHorizontalFadingEdgeEnabled(true);
                    this.mMarqueeFadeMode = 0;
                } else {
                    setHorizontalFadingEdgeEnabled(false);
                    this.mMarqueeFadeMode = 1;
                }
                setEllipsize(TruncateAt.MARQUEE);
                break;
        }
        if (textColor == null) {
            textColor = ColorStateList.valueOf(-16777216);
        }
        setTextColor(textColor);
        setHintTextColor(textColorHint);
        setLinkTextColor(textColorLink);
        if (textColorHighlight != 0) {
            setHighlightColor(textColorHighlight);
        }
        setRawTextSize((float) textSize);
        setElegantTextHeight(elegant);
        setLetterSpacing(letterSpacing);
        if (onCheckIsTextEditor() && isHighContrastTextEnabled() && letterSpacing < HCT_LETTER_SPACING) {
            this.mBackuppedLetterSpacing = this.mTextPaint.getLetterSpacing();
            this.mTextPaint.setLetterSpacing(HCT_LETTER_SPACING);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
        setFontFeatureSettings(fontFeatureSettings);
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        }
        if (password || passwordInputType || webPasswordInputType || numberPasswordInputType) {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else if (this.mEditor != null && (this.mEditor.mInputType & 4095) == 129) {
        }
        if (!(typefaceIndex == -1 || fontFamilyExplicit)) {
            fontFamily = null;
        }
        setTypefaceFromAttrs(fontFamily, typefaceIndex, styleIndex);
        if (shadowcolor != 0) {
            setShadowLayer(r, dx, dy, shadowcolor);
        }
        if (maxlength >= 0) {
            setFilters(new InputFilter[]{new LengthFilter(maxlength)});
        } else {
            setFilters(NO_FILTERS);
        }
        if (stringID != -1) {
            setStringName(stringID);
            this.fromResLock = true;
        }
        setText(text, bufferType);
        if (hint != null) {
            setHint(hint);
        }
        a = context.obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, defStyleRes);
        boolean focusable = (this.mMovement == null && getKeyListener() == null) ? false : true;
        boolean clickable = focusable || isClickable();
        boolean longClickable = focusable || isLongClickable();
        n = a.getIndexCount();
        for (i = 0; i < n; i++) {
            attr = a.getIndex(i);
            switch (attr) {
                case 19:
                    focusable = a.getBoolean(attr, focusable);
                    break;
                case 30:
                    clickable = a.getBoolean(attr, clickable);
                    break;
                case 31:
                    longClickable = a.getBoolean(attr, longClickable);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        setFocusable(focusable);
        setClickable(clickable);
        setLongClickable(longClickable);
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        if (getHoverUIFeatureLevel() >= 2) {
            setHoverPopupType(2);
        }
        if (Build.IS_SYSTEM_SECURE) {
            this.fromResLock = false;
        }
        this.mPasteEventListener = new TextViewClipboardEventListener();
        if (isMultiPenSelectionEnabled()) {
            this.mCoverManager = new CoverManager(context);
        }
        this.mWBMaxLength = maxlength;
        initTextStrikeThroughAnim(context);
    }

    private int[] parseDimensionArray(TypedArray dimens) {
        if (dimens == null) {
            return null;
        }
        int[] result = new int[dimens.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = dimens.getDimensionPixelSize(i, 0);
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == -1 && data != null) {
                CharSequence result = data.getCharSequenceExtra("android.intent.extra.PROCESS_TEXT");
                if (result != null) {
                    if (isTextEditable()) {
                        replaceSelectionWithText(result);
                    } else if (result.length() > 0) {
                        Toast.makeText(getContext(), String.valueOf(result), 1).show();
                    }
                }
            } else if (this.mText instanceof Spannable) {
                stopTextActionMode();
                Selection.setSelection((Spannable) this.mText, getSelectionStart(), getSelectionEnd());
            }
            if (this.mEditor.hasSelectionController()) {
                this.mEditor.startSelectionActionMode();
            }
        }
    }

    private void setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (familyName != null) {
            tf = Typeface.create(familyName, styleIndex);
            if (tf != null) {
                setTypeface(tf);
                return;
            }
        }
        switch (typefaceIndex) {
            case 1:
                tf = Typeface.SANS_SERIF;
                break;
            case 2:
                tf = Typeface.SERIF;
                break;
            case 3:
                tf = Typeface.MONOSPACE;
                break;
        }
        setTypeface(tf, styleIndex);
    }

    private void setRelativeDrawablesIfNeeded(Drawable start, Drawable end) {
        boolean hasRelativeDrawables;
        if (start == null && end == null) {
            hasRelativeDrawables = false;
        } else {
            hasRelativeDrawables = true;
        }
        if (hasRelativeDrawables) {
            Drawables dr = this.mDrawables;
            if (dr == null) {
                dr = new Drawables(getContext());
                this.mDrawables = dr;
            }
            this.mDrawables.mOverride = true;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableStart = start;
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableEnd = end;
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            resetResolvedDrawables();
            resolveDrawables();
            applyCompoundDrawableTint();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            InputMethodManager imm;
            if (!enabled) {
                imm = InputMethodManager.peekInstance();
                if (imm != null && imm.isActive(this)) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                }
            }
            super.setEnabled(enabled);
            if (enabled) {
                imm = InputMethodManager.peekInstance();
                if (imm != null) {
                    imm.restartInput(this);
                }
            }
            if (this.mEditor != null) {
                this.mEditor.invalidateTextDisplayList();
                this.mEditor.prepareCursorControllers();
                this.mEditor.makeBlink();
            }
        }
    }

    public void setTypeface(Typeface tf, int style) {
        boolean z = false;
        if (style > 0) {
            int typefaceStyle;
            float f;
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setTypeface(tf);
            if (tf != null) {
                typefaceStyle = tf.getStyle();
            } else {
                typefaceStyle = 0;
            }
            int need = style & (typefaceStyle ^ -1);
            TextPaint textPaint = this.mTextPaint;
            if ((need & 1) != 0) {
                z = true;
            }
            textPaint.setFakeBoldText(z);
            textPaint = this.mTextPaint;
            if ((need & 2) != 0) {
                f = -0.25f;
            } else {
                f = 0.0f;
            }
            textPaint.setTextSkewX(f);
            return;
        }
        if (tf != null) {
            Log.d(LOG_TAG, "setTypeface with style : " + style);
            tf = Typeface.create(tf, style);
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(tf);
    }

    protected boolean getDefaultEditable() {
        return false;
    }

    protected MovementMethod getDefaultMovementMethod() {
        return null;
    }

    @CapturedViewProperty
    public CharSequence getText() {
        return this.mText;
    }

    @CapturedViewProperty
    public CharSequence getStringName() {
        return this.mStringName;
    }

    public int length() {
        return this.mText.length();
    }

    public Editable getEditableText() {
        return this.mText instanceof Editable ? (Editable) this.mText : null;
    }

    public int getLineHeight() {
        return FastMath.round((((float) this.mTextPaint.getFontMetricsInt(null)) * this.mSpacingMult) + this.mSpacingAdd);
    }

    public final Layout getLayout() {
        return this.mLayout;
    }

    final Layout getHintLayout() {
        return this.mHintLayout;
    }

    public final UndoManager getUndoManager() {
        throw new UnsupportedOperationException("not implemented");
    }

    public final void setUndoManager(UndoManager undoManager, String tag) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final KeyListener getKeyListener() {
        return this.mEditor == null ? null : this.mEditor.mKeyListener;
    }

    public void setKeyListener(KeyListener input) {
        setKeyListenerOnly(input);
        fixFocusableAndClickableSettings();
        if (input != null) {
            createEditorIfNeeded();
            try {
                this.mEditor.mInputType = this.mEditor.mKeyListener.getInputType();
            } catch (IncompatibleClassChangeError e) {
                this.mEditor.mInputType = 1;
            }
            setInputTypeSingleLine(this.mSingleLine);
        } else if (this.mEditor != null) {
            this.mEditor.mInputType = 0;
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    private void setKeyListenerOnly(KeyListener input) {
        if (this.mEditor != null || input != null) {
            createEditorIfNeeded();
            if (this.mEditor.mKeyListener != input) {
                this.mEditor.mKeyListener = input;
                if (!(input == null || (this.mText instanceof Editable))) {
                    setText(this.mText);
                }
                setFilters((Editable) this.mText, this.mFilters);
            }
        }
    }

    public final MovementMethod getMovementMethod() {
        return this.mMovement;
    }

    public final void setMovementMethod(MovementMethod movement) {
        if (this.mMovement != movement) {
            this.mMovement = movement;
            if (!(movement == null || (this.mText instanceof Spannable))) {
                setText(this.mText);
            }
            fixFocusableAndClickableSettings();
            if (this.mEditor != null) {
                this.mEditor.prepareCursorControllers();
            }
        }
    }

    private void fixFocusableAndClickableSettings() {
        if (this.mMovement == null && (this.mEditor == null || this.mEditor.mKeyListener == null)) {
            setFocusable(false);
            setClickable(false);
            setLongClickable(false);
            return;
        }
        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
    }

    public final TransformationMethod getTransformationMethod() {
        return this.mTransformation;
    }

    public final void setTransformationMethod(TransformationMethod method) {
        if (method != this.mTransformation) {
            if (this.mTransformation != null && (this.mText instanceof Spannable)) {
                ((Spannable) this.mText).removeSpan(this.mTransformation);
            }
            this.mTransformation = method;
            if (method instanceof TransformationMethod2) {
                boolean z;
                TransformationMethod2 method2 = (TransformationMethod2) method;
                if (isTextSelectable() || (this.mText instanceof Editable)) {
                    z = false;
                } else {
                    z = true;
                }
                this.mAllowTransformationLengthChange = z;
                method2.setLengthChangesAllowed(this.mAllowTransformationLengthChange);
            } else {
                this.mAllowTransformationLengthChange = false;
            }
            setText(this.mText);
            if (hasPasswordTransformationMethod()) {
                notifyViewAccessibilityStateChangedIfNeeded(0);
            }
        }
    }

    public int getCompoundPaddingTop() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[1] == null) {
            return this.mPaddingTop;
        }
        return (this.mPaddingTop + dr.mDrawablePadding) + dr.mDrawableSizeTop;
    }

    public int getCompoundPaddingBottom() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[3] == null) {
            return this.mPaddingBottom;
        }
        return (this.mPaddingBottom + dr.mDrawablePadding) + dr.mDrawableSizeBottom;
    }

    public int getCompoundPaddingLeft() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[0] == null) {
            return this.mPaddingLeft;
        }
        return (this.mPaddingLeft + dr.mDrawablePadding) + dr.mDrawableSizeLeft;
    }

    public int getCompoundPaddingRight() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[2] == null) {
            return this.mPaddingRight;
        }
        return (this.mPaddingRight + dr.mDrawablePadding) + dr.mDrawableSizeRight;
    }

    public int getCompoundPaddingStart() {
        resolveDrawables();
        switch (getLayoutDirection()) {
            case 1:
                return getCompoundPaddingRight();
            default:
                return getCompoundPaddingLeft();
        }
    }

    public int getCompoundPaddingEnd() {
        resolveDrawables();
        switch (getLayoutDirection()) {
            case 1:
                return getCompoundPaddingLeft();
            default:
                return getCompoundPaddingRight();
        }
    }

    public int getExtendedPaddingTop() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingTop();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingTop();
        }
        int top = getCompoundPaddingTop();
        int viewht = (getHeight() - top) - getCompoundPaddingBottom();
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return top;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return top;
        }
        if (gravity == 80) {
            return (top + viewht) - layoutht;
        }
        return top + ((viewht - layoutht) / 2);
    }

    public int getExtendedPaddingBottom() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingBottom();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingBottom();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return bottom;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return (bottom + viewht) - layoutht;
        }
        return gravity != 80 ? bottom + ((viewht - layoutht) / 2) : bottom;
    }

    public int getTotalPaddingLeft() {
        return getCompoundPaddingLeft();
    }

    public int getTotalPaddingRight() {
        return getCompoundPaddingRight();
    }

    public int getTotalPaddingStart() {
        return getCompoundPaddingStart();
    }

    public int getTotalPaddingEnd() {
        return getCompoundPaddingEnd();
    }

    public int getTotalPaddingTop() {
        return getExtendedPaddingTop() + getVerticalOffset(true);
    }

    public int getTotalPaddingBottom() {
        return getExtendedPaddingBottom() + getBottomVerticalOffset(true);
    }

    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        boolean drawables;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = null;
            if (dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = null;
            dr.mDrawableHeightStart = 0;
            dr.mDrawableSizeStart = 0;
            dr.mDrawableHeightEnd = 0;
            dr.mDrawableSizeEnd = 0;
        }
        if (left == null && top == null && right == null && bottom == null) {
            drawables = false;
        } else {
            drawables = true;
        }
        if (drawables) {
            if (dr == null) {
                dr = new Drawables(getContext());
                this.mDrawables = dr;
            }
            this.mDrawables.mOverride = false;
            if (!(dr.mShowing[0] == left || dr.mShowing[0] == null)) {
                dr.mShowing[0].setCallback(null);
            }
            dr.mShowing[0] = left;
            if (!(dr.mShowing[1] == top || dr.mShowing[1] == null)) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (!(dr.mShowing[2] == right || dr.mShowing[2] == null)) {
                dr.mShowing[2].setCallback(null);
            }
            dr.mShowing[2] = right;
            if (!(dr.mShowing[3] == bottom || dr.mShowing[3] == null)) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                left.setCallback(this);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableHeightLeft = 0;
                dr.mDrawableSizeLeft = 0;
            }
            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                right.setCallback(this);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableHeightRight = 0;
                dr.mDrawableSizeRight = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        } else if (dr != null) {
            if (dr.mDrawablePadding == 0) {
                this.mDrawables = null;
            } else {
                for (int i = dr.mShowing.length - 1; i >= 0; i--) {
                    if (dr.mShowing[i] != null) {
                        dr.mShowing[i].setCallback(null);
                    }
                    dr.mShowing[i] = null;
                }
                dr.mDrawableHeightLeft = 0;
                dr.mDrawableSizeLeft = 0;
                dr.mDrawableHeightRight = 0;
                dr.mDrawableSizeRight = 0;
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        }
        if (dr != null) {
            dr.mDrawableLeftInitial = left;
            dr.mDrawableRightInitial = right;
        }
        resetResolvedDrawables();
        resolveDrawables();
        applyCompoundDrawableTint();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        Drawable drawable;
        Drawable drawable2 = null;
        Context context = getContext();
        if (left != 0) {
            drawable = context.getDrawable(left);
        } else {
            drawable = null;
        }
        Drawable drawable3 = top != 0 ? context.getDrawable(top) : null;
        Drawable drawable4 = right != 0 ? context.getDrawable(right) : null;
        if (bottom != 0) {
            drawable2 = context.getDrawable(bottom);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable, drawable3, drawable4, drawable2);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        boolean drawables;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            Drawable[] drawableArr = dr.mShowing;
            dr.mDrawableLeftInitial = null;
            drawableArr[0] = null;
            if (dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            drawableArr = dr.mShowing;
            dr.mDrawableRightInitial = null;
            drawableArr[2] = null;
            dr.mDrawableHeightLeft = 0;
            dr.mDrawableSizeLeft = 0;
            dr.mDrawableHeightRight = 0;
            dr.mDrawableSizeRight = 0;
        }
        if (start == null && top == null && end == null && bottom == null) {
            drawables = false;
        } else {
            drawables = true;
        }
        if (drawables) {
            if (dr == null) {
                dr = new Drawables(getContext());
                this.mDrawables = dr;
            }
            this.mDrawables.mOverride = true;
            if (!(dr.mDrawableStart == start || dr.mDrawableStart == null)) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = start;
            if (!(dr.mShowing[1] == top || dr.mShowing[1] == null)) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (!(dr.mDrawableEnd == end || dr.mDrawableEnd == null)) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = end;
            if (!(dr.mShowing[3] == bottom || dr.mShowing[3] == null)) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        } else if (dr != null) {
            if (dr.mDrawablePadding == 0) {
                this.mDrawables = null;
            } else {
                if (dr.mDrawableStart != null) {
                    dr.mDrawableStart.setCallback(null);
                }
                dr.mDrawableStart = null;
                if (dr.mShowing[1] != null) {
                    dr.mShowing[1].setCallback(null);
                }
                dr.mShowing[1] = null;
                if (dr.mDrawableEnd != null) {
                    dr.mDrawableEnd.setCallback(null);
                }
                dr.mDrawableEnd = null;
                if (dr.mShowing[3] != null) {
                    dr.mShowing[3].setCallback(null);
                }
                dr.mShowing[3] = null;
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        }
        resetResolvedDrawables();
        resolveDrawables();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        Drawable drawable = null;
        Context context = getContext();
        Drawable drawable2 = start != 0 ? context.getDrawable(start) : null;
        Drawable drawable3 = top != 0 ? context.getDrawable(top) : null;
        Drawable drawable4 = end != 0 ? context.getDrawable(end) : null;
        if (bottom != 0) {
            drawable = context.getDrawable(bottom);
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (start != null) {
            start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
        }
        if (end != null) {
            end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawablesRelative(start, top, end, bottom);
    }

    public Drawable[] getCompoundDrawables() {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            return (Drawable[]) dr.mShowing.clone();
        }
        return new Drawable[]{null, null, null, null};
    }

    public Drawable[] getCompoundDrawablesRelative() {
        if (this.mDrawables != null) {
            return new Drawable[]{this.mDrawables.mDrawableStart, this.mDrawables.mShowing[1], this.mDrawables.mDrawableEnd, this.mDrawables.mShowing[3]};
        }
        return new Drawable[]{null, null, null, null};
    }

    @RemotableViewMethod
    public void setCompoundDrawablePadding(int pad) {
        Drawables dr = this.mDrawables;
        if (pad != 0) {
            if (dr == null) {
                dr = new Drawables(getContext());
                this.mDrawables = dr;
            }
            dr.mDrawablePadding = pad;
        } else if (dr != null) {
            dr.mDrawablePadding = pad;
        }
        invalidate();
        requestLayout();
    }

    public int getCompoundDrawablePadding() {
        Drawables dr = this.mDrawables;
        return dr != null ? dr.mDrawablePadding : 0;
    }

    public void setCompoundDrawableTintList(ColorStateList tint) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        this.mDrawables.mTintList = tint;
        this.mDrawables.mHasTint = true;
        applyCompoundDrawableTint();
    }

    public ColorStateList getCompoundDrawableTintList() {
        return this.mDrawables != null ? this.mDrawables.mTintList : null;
    }

    public void setCompoundDrawableTintMode(Mode tintMode) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        this.mDrawables.mTintMode = tintMode;
        this.mDrawables.mHasTintMode = true;
        applyCompoundDrawableTint();
    }

    public Mode getCompoundDrawableTintMode() {
        return this.mDrawables != null ? this.mDrawables.mTintMode : null;
    }

    private void applyCompoundDrawableTint() {
        if (this.mDrawables != null) {
            if (this.mDrawables.mHasTint || this.mDrawables.mHasTintMode) {
                ColorStateList tintList = this.mDrawables.mTintList;
                Mode tintMode = this.mDrawables.mTintMode;
                boolean hasTint = this.mDrawables.mHasTint;
                boolean hasTintMode = this.mDrawables.mHasTintMode;
                int[] state = getDrawableState();
                for (Drawable dr : this.mDrawables.mShowing) {
                    if (!(dr == null || dr == this.mDrawables.mDrawableError)) {
                        dr.mutate();
                        if (hasTint) {
                            dr.setTintList(tintList);
                        }
                        if (hasTintMode) {
                            dr.setTintMode(tintMode);
                        }
                        if (dr.isStateful()) {
                            dr.setState(state);
                        }
                    }
                }
            }
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (!(left == this.mPaddingLeft && right == this.mPaddingRight && top == this.mPaddingTop && bottom == this.mPaddingBottom)) {
            nullLayouts();
        }
        super.setPadding(left, top, right, bottom);
        invalidate();
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        if (!(start == getPaddingStart() && end == getPaddingEnd() && top == this.mPaddingTop && bottom == this.mPaddingBottom)) {
            nullLayouts();
        }
        super.setPaddingRelative(start, top, end, bottom);
        invalidate();
    }

    public final int getAutoLinkMask() {
        return this.mAutoLinkMask;
    }

    public void setTextAppearance(int resId) {
        setTextAppearance(this.mContext, resId);
    }

    @Deprecated
    public void setTextAppearance(Context context, int resId) {
        TypedArray ta = context.obtainStyledAttributes(resId, android.R.styleable.TextAppearance);
        int textColorHighlight = ta.getColor(4, 0);
        if (textColorHighlight != 0) {
            setHighlightColor(textColorHighlight);
        }
        ColorStateList textColor = ta.getColorStateList(3);
        if (textColor != null) {
            setTextColor(textColor);
        }
        int textSize = ta.getDimensionPixelSize(0, 0);
        if (textSize != 0) {
            setRawTextSize((float) textSize);
        }
        ColorStateList textColorHint = ta.getColorStateList(5);
        if (textColorHint != null) {
            setHintTextColor(textColorHint);
        }
        ColorStateList textColorLink = ta.getColorStateList(6);
        if (textColorLink != null) {
            setLinkTextColor(textColorLink);
        }
        setTypefaceFromAttrs(ta.getString(12), ta.getInt(1, -1), ta.getInt(2, -1));
        int shadowColor = ta.getInt(7, 0);
        if (shadowColor != 0) {
            float dx = ta.getFloat(8, 0.0f);
            float dy = ta.getFloat(9, 0.0f);
            setShadowLayer(ta.getFloat(10, 0.0f), dx, dy, shadowColor);
        }
        if (ta.getBoolean(11, false)) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        }
        if (ta.hasValue(13)) {
            setElegantTextHeight(ta.getBoolean(13, false));
        }
        if (ta.hasValue(14)) {
            setLetterSpacing(ta.getFloat(14, 0.0f));
        }
        if (ta.hasValue(15)) {
            setFontFeatureSettings(ta.getString(15));
        }
        ta.recycle();
    }

    public Locale getTextLocale() {
        return this.mTextPaint.getTextLocale();
    }

    public void setTextLocale(Locale locale) {
        this.mLocaleChanged = true;
        this.mTextPaint.setTextLocale(locale);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        boolean isMobileKeyboard;
        super.onConfigurationChanged(newConfig);
        if (!this.mLocaleChanged) {
            this.mTextPaint.setTextLocale(Locale.getDefault());
        }
        if (!((this instanceof ExtractEditText) || this.mEditor == null)) {
            this.mEditor.onScrollChanged();
        }
        if (this.mhasMultiSelection) {
            clearAllMultiSelection();
        }
        if (this.mContext.getResources().getConfiguration().mobileKeyboardCovered == 1) {
            isMobileKeyboard = true;
        } else {
            isMobileKeyboard = false;
        }
        if (isMobileKeyboard && getWritingBuddy(false) != null) {
            getWritingBuddy(false).finish(true);
        }
    }

    @ExportedProperty(category = "text")
    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    @ExportedProperty(category = "text")
    public float getScaledTextSize() {
        return this.mTextPaint.getTextSize() / this.mTextPaint.density;
    }

    @ExportedProperty(category = "text", mapping = {@IntToString(from = 0, to = "NORMAL"), @IntToString(from = 1, to = "BOLD"), @IntToString(from = 2, to = "ITALIC"), @IntToString(from = 3, to = "BOLD_ITALIC")})
    public int getTypefaceStyle() {
        Typeface typeface = this.mTextPaint.getTypeface();
        return typeface != null ? typeface.getStyle() : 0;
    }

    @RemotableViewMethod
    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        Resources r;
        Context c = getContext();
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(size);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getTextScaleX() {
        return this.mTextPaint.getTextScaleX();
    }

    @RemotableViewMethod
    public void setTextScaleX(float size) {
        if (size != this.mTextPaint.getTextScaleX()) {
            this.mUserSetTextScaleX = true;
            this.mTextPaint.setTextScaleX(size);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public void setElegantTextHeight(boolean elegant) {
        this.mTextPaint.setElegantTextHeight(elegant);
    }

    public float getLetterSpacing() {
        return this.mTextPaint.getLetterSpacing();
    }

    @RemotableViewMethod
    public void setLetterSpacing(float letterSpacing) {
        if (letterSpacing != this.mTextPaint.getLetterSpacing()) {
            if (onCheckIsTextEditor() && isHighContrastTextEnabled() && letterSpacing < HCT_LETTER_SPACING) {
                this.mBackuppedLetterSpacing = this.mTextPaint.getLetterSpacing();
                this.mTextPaint.setLetterSpacing(HCT_LETTER_SPACING);
            } else {
                this.mTextPaint.setLetterSpacing(letterSpacing);
            }
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public String getFontFeatureSettings() {
        return this.mTextPaint.getFontFeatureSettings();
    }

    public void setBreakStrategy(int breakStrategy) {
        this.mBreakStrategy = breakStrategy;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getBreakStrategy() {
        return this.mBreakStrategy;
    }

    public void setHyphenationFrequency(int hyphenationFrequency) {
        this.mHyphenationFrequency = hyphenationFrequency;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getHyphenationFrequency() {
        return this.mHyphenationFrequency;
    }

    @RemotableViewMethod
    public void setFontFeatureSettings(String fontFeatureSettings) {
        if (fontFeatureSettings != this.mTextPaint.getFontFeatureSettings()) {
            this.mTextPaint.setFontFeatureSettings(fontFeatureSettings);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public MyanmarEncoding getMyanmarEncoding() {
        return this.mTextPaint.getMyanmarEncoding();
    }

    public void setMyanmarEncoding(MyanmarEncoding myanmarEncoding) {
        if (myanmarEncoding != this.mTextPaint.getMyanmarEncoding()) {
            this.mTextPaint.setMyanmarEncoding(myanmarEncoding);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    @RemotableViewMethod
    public void setMyanmarEncoding(int myanmarEncoding) {
        switch (myanmarEncoding) {
            case 0:
                this.mTextPaint.setMyanmarEncoding(MyanmarEncoding.ME_UNICODE);
                return;
            case 1:
                this.mTextPaint.setMyanmarEncoding(MyanmarEncoding.ME_ZAWGYI);
                return;
            default:
                this.mTextPaint.setMyanmarEncoding(MyanmarEncoding.ME_AUTO);
                return;
        }
    }

    @RemotableViewMethod
    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        this.mTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getTextColors() {
        return this.mTextColor;
    }

    public final int getCurrentTextColor() {
        return this.mCurTextColor;
    }

    @RemotableViewMethod
    public void setHighlightColor(int color) {
        if (this.mHighlightColor != color) {
            this.mHighlightColor = color;
            invalidate();
        }
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    @RemotableViewMethod
    public final void setShowSoftInputOnFocus(boolean show) {
        createEditorIfNeeded();
        this.mEditor.mShowSoftInputOnFocus = show;
    }

    public final boolean getShowSoftInputOnFocus() {
        return this.mEditor == null || this.mEditor.mShowSoftInputOnFocus;
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        this.mTextPaint.setShadowLayer(radius, dx, dy, color);
        this.mShadowRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        this.mShadowColor = color;
        if (this.mEditor != null) {
            this.mEditor.invalidateTextDisplayList();
        }
        invalidate();
    }

    public int addOuterShadowTextEffect(float angle, float offset, float softness, int color, float blendingOpacity) {
        return this.mTextPaint.addOuterShadowTextEffect(angle, offset, softness, color, blendingOpacity);
    }

    public int addInnerShadowTextEffect(float angle, float offset, float softness, int color, float blendingOpacity) {
        return this.mTextPaint.addInnerShadowTextEffect(angle, offset, softness, color, blendingOpacity);
    }

    public int addStrokeTextEffect(float size, int color, float blendingOpacity) {
        return this.mTextPaint.addStrokeTextEffect(size, color, blendingOpacity);
    }

    public int addOuterGlowTextEffect(float size, int color, float blendingOpacity) {
        return this.mTextPaint.addOuterGlowTextEffect(size, color, blendingOpacity);
    }

    public int addLinearGradientTextEffect(float angle, float scale, int[] colors, float[] alphas, float[] positions, float blendingOpacity) {
        return this.mTextPaint.addLinearGradientTextEffect(angle, scale, colors, alphas, positions, blendingOpacity);
    }

    public void removeTextEffect(int id) {
        this.mTextPaint.removeTextEffect(id);
    }

    public void setTextEffectOpacity(float blendingOpacity) {
        this.mTextPaint.setTextEffectOpacity(blendingOpacity);
    }

    public void clearTextEffectOpacity() {
        this.mTextPaint.clearTextEffectOpacity();
    }

    public void clearAllTextEffect() {
        this.mTextPaint.clearAllTextEffect();
    }

    public float getShadowRadius() {
        return this.mShadowRadius;
    }

    public float getShadowDx() {
        return this.mShadowDx;
    }

    public float getShadowDy() {
        return this.mShadowDy;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    @RemotableViewMethod
    public final void setAutoLinkMask(int mask) {
        this.mAutoLinkMask = mask;
    }

    @RemotableViewMethod
    public final void setLinksClickable(boolean whether) {
        this.mLinksClickable = whether;
    }

    public final boolean getLinksClickable() {
        return this.mLinksClickable;
    }

    public URLSpan[] getUrls() {
        if (this.mText instanceof Spanned) {
            return (URLSpan[]) ((Spanned) this.mText).getSpans(0, this.mText.length(), URLSpan.class);
        }
        return new URLSpan[0];
    }

    @RemotableViewMethod
    public final void setHintTextColor(int color) {
        this.mHintTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setHintTextColor(ColorStateList colors) {
        this.mHintTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getHintTextColors() {
        return this.mHintTextColor;
    }

    public final int getCurrentHintTextColor() {
        return this.mHintTextColor != null ? this.mCurHintTextColor : this.mCurTextColor;
    }

    @RemotableViewMethod
    public final void setLinkTextColor(int color) {
        this.mLinkTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setLinkTextColor(ColorStateList colors) {
        this.mLinkTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getLinkTextColors() {
        return this.mLinkTextColor;
    }

    public void setGravity(int gravity) {
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            gravity |= Gravity.START;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        boolean newLayout = false;
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != (this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK)) {
            newLayout = true;
        }
        if (gravity != this.mGravity) {
            invalidate();
        }
        this.mGravity = gravity;
        if (this.mLayout != null && newLayout) {
            makeNewLayout(this.mLayout.getWidth(), this.mHintLayout == null ? 0 : this.mHintLayout.getWidth(), UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), true);
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public int getPaintFlags() {
        return this.mTextPaint.getFlags();
    }

    @RemotableViewMethod
    public void setPaintFlags(int flags) {
        if (this.mTextPaint.getFlags() != flags) {
            this.mTextPaint.setFlags(flags);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setHorizontallyScrolling(boolean whether) {
        if (this.mHorizontallyScrolling != whether) {
            this.mHorizontallyScrolling = whether;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean getHorizontallyScrolling() {
        return this.mHorizontallyScrolling;
    }

    @RemotableViewMethod
    public void setMinLines(int minlines) {
        this.mMinimum = minlines;
        this.mMinMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinLines() {
        return this.mMinMode == 1 ? this.mMinimum : -1;
    }

    @RemotableViewMethod
    public void setMinHeight(int minHeight) {
        this.mMinimum = minHeight;
        this.mMinMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinHeight() {
        return this.mMinMode == 2 ? this.mMinimum : -1;
    }

    @RemotableViewMethod
    public void setMaxLines(int maxlines) {
        this.mMaximum = maxlines;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxLines() {
        return this.mMaxMode == 1 ? this.mMaximum : -1;
    }

    @RemotableViewMethod
    public void setMaxHeight(int maxHeight) {
        this.mMaximum = maxHeight;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxHeight() {
        return this.mMaxMode == 2 ? this.mMaximum : -1;
    }

    @RemotableViewMethod
    public void setLines(int lines) {
        this.mMinimum = lines;
        this.mMaximum = lines;
        this.mMinMode = 1;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setHeight(int pixels) {
        this.mMinimum = pixels;
        this.mMaximum = pixels;
        this.mMinMode = 2;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setMinEms(int minems) {
        this.mMinWidth = minems;
        this.mMinWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinEms() {
        return this.mMinWidthMode == 1 ? this.mMinWidth : -1;
    }

    @RemotableViewMethod
    public void setMinWidth(int minpixels) {
        this.mMinWidth = minpixels;
        this.mMinWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinWidth() {
        return this.mMinWidthMode == 2 ? this.mMinWidth : -1;
    }

    @RemotableViewMethod
    public void setMaxEms(int maxems) {
        this.mMaxWidth = maxems;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxEms() {
        return this.mMaxWidthMode == 1 ? this.mMaxWidth : -1;
    }

    @RemotableViewMethod
    public void setMaxWidth(int maxpixels) {
        this.mMaxWidth = maxpixels;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxWidth() {
        return this.mMaxWidthMode == 2 ? this.mMaxWidth : -1;
    }

    @RemotableViewMethod
    public void setEms(int ems) {
        this.mMinWidth = ems;
        this.mMaxWidth = ems;
        this.mMinWidthMode = 1;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setWidth(int pixels) {
        this.mMinWidth = pixels;
        this.mMaxWidth = pixels;
        this.mMinWidthMode = 2;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public void setLineSpacing(float add, float mult) {
        if (this.mSpacingAdd != add || this.mSpacingMult != mult) {
            this.mSpacingAdd = add;
            this.mSpacingMult = mult;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getLineSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public float getLineSpacingExtra() {
        return this.mSpacingAdd;
    }

    public final void append(CharSequence text) {
        append(text, 0, text.length());
    }

    public void append(CharSequence text, int start, int end) {
        if (!(this.mText instanceof Editable)) {
            setText(this.mText, BufferType.EDITABLE);
        }
        ((Editable) this.mText).append(text, start, end);
    }

    private void updateTextColors() {
        boolean inval = false;
        int color = this.mTextColor.getColorForState(getDrawableState(), 0);
        if (color != this.mCurTextColor) {
            this.mCurTextColor = color;
            inval = true;
        }
        if (this.mLinkTextColor != null) {
            color = this.mLinkTextColor.getColorForState(getDrawableState(), 0);
            if (color != this.mTextPaint.linkColor) {
                this.mTextPaint.linkColor = color;
                inval = true;
            }
        }
        if (this.mHintTextColor != null) {
            if (this.mContext.getResources().getBoolean(R.bool.tw_edit_text_hint_theme) && !(this instanceof SearchAutoComplete)) {
                this.mHintTextColor = this.mHintTextColor.withAlpha(179);
            }
            color = this.mHintTextColor.getColorForState(getDrawableState(), 0);
            if (color != this.mCurHintTextColor) {
                this.mCurHintTextColor = color;
                if (this.mText.length() == 0) {
                    inval = true;
                }
            }
        }
        if (inval) {
            if (this.mEditor != null) {
                this.mEditor.invalidateTextDisplayList();
            }
            invalidate();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((this.mTextColor != null && this.mTextColor.isStateful()) || ((this.mHintTextColor != null && this.mHintTextColor.isStateful()) || (this.mLinkTextColor != null && this.mLinkTextColor.isStateful()))) {
            updateTextColors();
        }
        if (this.mDrawables != null) {
            int[] state = getDrawableState();
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null && dr.isStateful()) {
                    dr.setState(state);
                }
            }
            Drawables dr2 = this.mDrawables;
            if (dr2.mDrawableStart != null && dr2.mDrawableStart.isStateful()) {
                dr2.mDrawableStart.setState(state);
            }
            if (dr2.mDrawableEnd != null && dr2.mDrawableEnd.isStateful()) {
                dr2.mDrawableEnd.setState(state);
            }
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mDrawables != null) {
            int[] state = getDrawableState();
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null && dr.isStateful()) {
                    dr.setHotspot(x, y);
                }
            }
            Drawables dr2 = this.mDrawables;
            if (dr2.mDrawableStart != null) {
                dr2.mDrawableStart.setHotspot(x, y);
            }
            if (dr2.mDrawableEnd != null) {
                dr2.mDrawableEnd.setHotspot(x, y);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        boolean save = this.mFreezesText;
        int start = 0;
        int end = 0;
        if (this.mText != null) {
            start = getSelectionStart();
            end = getSelectionEnd();
            if (start >= 0 || end >= 0) {
                save = true;
            }
        }
        if (!save) {
            return superState;
        }
        SavedState ss = new SavedState(superState);
        ss.selStart = start;
        ss.selEnd = end;
        if (this.mText instanceof Spanned) {
            Spannable sp = new SpannableStringBuilder(this.mText);
            if (this.mEditor != null) {
                removeMisspelledSpans(sp);
                sp.removeSpan(this.mEditor.mSuggestionRangeSpan);
            }
            ss.text = sp;
        } else {
            ss.text = this.mText.toString();
        }
        if (isFocused() && start >= 0 && end >= 0) {
            ss.frozenWithFocus = true;
        }
        ss.error = getError();
        if (this.mEditor == null) {
            return ss;
        }
        ss.editorState = this.mEditor.saveInstanceState();
        return ss;
    }

    void removeMisspelledSpans(Spannable spannable) {
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
        for (int i = 0; i < suggestionSpans.length; i++) {
            int flags = suggestionSpans[i].getFlags();
            if (!((flags & 1) == 0 || (flags & 2) == 0)) {
                spannable.removeSpan(suggestionSpans[i]);
            }
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (ss.text != null) {
                setText(ss.text);
            }
            if (ss.selStart >= 0 && ss.selEnd >= 0 && (this.mText instanceof Spannable)) {
                int len = this.mText.length();
                if (ss.selStart > len || ss.selEnd > len) {
                    String restored = "";
                    if (ss.text != null) {
                        restored = "(restored) ";
                    }
                    Log.e(LOG_TAG, "Saved cursor position " + ss.selStart + "/" + ss.selEnd + " out of range for " + restored + "text " + this.mText);
                } else {
                    Selection.setSelection((Spannable) this.mText, ss.selStart, ss.selEnd);
                    if (ss.frozenWithFocus) {
                        createEditorIfNeeded();
                        this.mEditor.mFrozenWithFocus = true;
                    }
                }
            }
            if (ss.error != null) {
                final CharSequence error = ss.error;
                post(new Runnable() {
                    public void run() {
                        if (TextView.this.mEditor == null || !TextView.this.mEditor.mErrorWasChanged) {
                            TextView.this.setError(error);
                        }
                    }
                });
            }
            if (ss.editorState != null) {
                createEditorIfNeeded();
                this.mEditor.restoreInstanceState(ss.editorState);
                return;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @RemotableViewMethod
    public void setFreezesText(boolean freezesText) {
        this.mFreezesText = freezesText;
    }

    public boolean getFreezesText() {
        return this.mFreezesText;
    }

    public final void setEditableFactory(Factory factory) {
        this.mEditableFactory = factory;
        setText(this.mText);
    }

    public final void setSpannableFactory(Spannable.Factory factory) {
        this.mSpannableFactory = factory;
        setText(this.mText);
    }

    @RemotableViewMethod
    public final void setText(CharSequence text) {
        setText(text, this.mBufferType);
    }

    @RemotableViewMethod
    public final void setTextKeepState(CharSequence text) {
        setTextKeepState(text, this.mBufferType);
    }

    public void setText(CharSequence text, BufferType type) {
        setText(text, type, true, 0);
        if (this.mCharWrapper != null) {
            this.mCharWrapper.mChars = null;
        }
    }

    private void setText(CharSequence text, BufferType type, boolean notifyBefore, int oldlen) {
        if (text == null) {
            text = "";
        }
        if (!isSuggestionsEnabled()) {
            text = removeSuggestionSpans(text);
        }
        if (!this.mUserSetTextScaleX) {
            this.mTextPaint.setTextScaleX(1.0f);
        }
        if ((text instanceof Spanned) && ((Spanned) text).getSpanStart(TruncateAt.MARQUEE) >= 0) {
            if (ViewConfiguration.get(this.mContext).isFadingMarqueeEnabled()) {
                setHorizontalFadingEdgeEnabled(true);
                this.mMarqueeFadeMode = 0;
            } else {
                setHorizontalFadingEdgeEnabled(false);
                this.mMarqueeFadeMode = 1;
            }
            setEllipsize(TruncateAt.MARQUEE);
        }
        for (InputFilter filter : this.mFilters) {
            CharSequence out = filter.filter(text, 0, text.length(), EMPTY_SPANNED, 0, 0);
            if (out != null) {
                text = out;
            }
        }
        if (notifyBefore) {
            if (this.mText != null) {
                oldlen = this.mText.length();
                sendBeforeTextChanged(this.mText, 0, oldlen, text.length());
            } else {
                sendBeforeTextChanged("", 0, 0, text.length());
            }
        }
        boolean needEditableForNotification = false;
        if (!(this.mListeners == null || this.mListeners.size() == 0)) {
            needEditableForNotification = true;
        }
        if (type == BufferType.EDITABLE || getKeyListener() != null || needEditableForNotification) {
            createEditorIfNeeded();
            this.mEditor.forgetUndoRedo();
            Editable t = this.mEditableFactory.newEditable(text);
            text = t;
            setFilters(t, this.mFilters);
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                imm.restartInput(this);
            }
        } else if (type == BufferType.SPANNABLE || this.mMovement != null) {
            text = this.mSpannableFactory.newSpannable(text);
        } else if (!(text instanceof CharWrapper)) {
            text = TextUtils.stringOrSpannedString(text);
        }
        this.mDisplayText = null;
        this.mUseDisplayText = false;
        if (this.mAutoLinkMask != 0) {
            Spannable s2;
            if (type == BufferType.EDITABLE || (text instanceof Spannable)) {
                s2 = (Spannable) text;
            } else {
                s2 = this.mSpannableFactory.newSpannable(text);
            }
            if (Linkify.addLinks(s2, this.mAutoLinkMask)) {
                text = s2;
                type = type == BufferType.EDITABLE ? BufferType.EDITABLE : BufferType.SPANNABLE;
                this.mText = text;
                if (this.mLinksClickable && !textCanBeSelected()) {
                    setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }
        this.mTextPaint.setMyanmarEncoding(Locale.getDefault());
        this.mBufferType = type;
        this.mText = text;
        if (this.mTransformation == null) {
            this.mTransformed = text;
        } else {
            this.mTransformed = this.mTransformation.getTransformation(text, this);
        }
        int textLength = text.length();
        Spannable sp;
        TextView textView;
        if ((text instanceof Spannable) && !this.mAllowTransformationLengthChange) {
            sp = (Spannable) text;
            for (Object removeSpan : (ChangeWatcher[]) sp.getSpans(0, sp.length(), ChangeWatcher.class)) {
                sp.removeSpan(removeSpan);
            }
            if (this.mChangeWatcher == null) {
                textView = this;
                this.mChangeWatcher = new ChangeWatcher();
            }
            sp.setSpan(this.mChangeWatcher, 0, textLength, 6553618);
            if (this.mEditor != null) {
                this.mEditor.addSpanWatchers(sp);
            }
            if (this.mTransformation != null) {
                sp.setSpan(this.mTransformation, 0, textLength, 18);
            }
            if (this.mMovement != null) {
                this.mMovement.initialize(this, (Spannable) text);
                if (this.mEditor != null) {
                    this.mEditor.mSelectionMoved = false;
                }
            }
        } else if (isMultiPenSelectionEnabled() && !TextUtils.isEmpty(this.mTransformed)) {
            if (this.mTransformed.length() > 5000) {
                enableMultiSelection(false);
            } else {
                this.mDisplayText = new SpannableStringBuilder(this.mTransformed);
                sp = (Spannable) this.mDisplayText;
                if (this.mChangeWatcher == null) {
                    textView = this;
                    this.mChangeWatcher = new ChangeWatcher();
                }
                sp.setSpan(this.mChangeWatcher, 0, this.mDisplayText.length(), 6553618);
                this.mUseDisplayText = true;
                this.mSkipUpdateDisplayText = true;
            }
        }
        if (this.mLayout != null) {
            checkForRelayout();
        }
        sendOnTextChanged(text, 0, oldlen, textLength);
        onTextChanged(text, 0, oldlen, textLength);
        notifyViewAccessibilityStateChangedIfNeeded(2);
        if (needEditableForNotification) {
            sendAfterTextChanged((Editable) text);
        }
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
        if (Build.IS_SYSTEM_SECURE) {
            setStringName(this.mText);
        }
    }

    public final void setText(char[] text, int start, int len) {
        int oldlen = 0;
        if (start < 0 || len < 0 || start + len > text.length) {
            throw new IndexOutOfBoundsException(start + ", " + len);
        }
        if (this.mText != null) {
            oldlen = this.mText.length();
            sendBeforeTextChanged(this.mText, 0, oldlen, len);
        } else {
            sendBeforeTextChanged("", 0, 0, len);
        }
        if (this.mCharWrapper == null) {
            this.mCharWrapper = new CharWrapper(text, start, len);
        } else {
            this.mCharWrapper.set(text, start, len);
        }
        setText(this.mCharWrapper, this.mBufferType, false, oldlen);
    }

    public final void setTextKeepState(CharSequence text, BufferType type) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int len = text.length();
        setText(text, type);
        if ((start >= 0 || end >= 0) && (this.mText instanceof Spannable)) {
            Selection.setSelection((Spannable) this.mText, Math.max(0, Math.min(start, len)), Math.max(0, Math.min(end, len)));
        }
    }

    @RemotableViewMethod
    public final void setText(int resid) {
        if (Build.IS_SYSTEM_SECURE) {
            setStringName(resid);
        }
        setText(getContext().getResources().getText(resid));
    }

    public final void setText(int resid, BufferType type) {
        if (Build.IS_SYSTEM_SECURE) {
            setStringName(resid);
        }
        setText(getContext().getResources().getText(resid), type);
    }

    public final void setStringName(CharSequence text) {
        if (Build.IS_SYSTEM_SECURE && !this.fromResLock) {
            this.mStringName = getContext().getResources().getStringNames(text);
        }
    }

    public final void setStringName(int resid) {
        if (Build.IS_SYSTEM_SECURE) {
            this.mStringName = getContext().getResources().getResourceName(resid);
        }
    }

    @RemotableViewMethod
    public final void setHint(CharSequence hint) {
        this.mHint = TextUtils.stringOrSpannedString(hint);
        if (Build.IS_SYSTEM_SECURE) {
            setStringName(hint);
        }
        if (this.mLayout != null) {
            checkForRelayout();
        }
        if (this.mText.length() == 0) {
            invalidate();
        }
        if (this.mEditor != null && this.mText.length() == 0 && this.mHint != null) {
            this.mEditor.invalidateTextDisplayList();
        }
    }

    @RemotableViewMethod
    public final void setHint(int resid) {
        if (Build.IS_SYSTEM_SECURE) {
            setStringName(resid);
        }
        setHint(getContext().getResources().getText(resid));
    }

    @CapturedViewProperty
    public CharSequence getHint() {
        return this.mHint;
    }

    boolean isSingleLine() {
        return this.mSingleLine;
    }

    private static boolean isMultilineInputType(int type) {
        return (131087 & type) == 131073;
    }

    CharSequence removeSuggestionSpans(CharSequence text) {
        if (text instanceof Spanned) {
            Spannable spannable;
            if (text instanceof Spannable) {
                spannable = (Spannable) text;
            } else {
                spannable = new SpannableString(text);
                Object text2 = spannable;
            }
            SuggestionSpan[] spans = (SuggestionSpan[]) spannable.getSpans(0, text.length(), SuggestionSpan.class);
            for (Object removeSpan : spans) {
                spannable.removeSpan(removeSpan);
            }
        }
        return text;
    }

    CharSequence removeEasyEditSpan(CharSequence text) {
        if (text instanceof Spanned) {
            Spannable spannable;
            if (text instanceof Spannable) {
                spannable = (Spannable) text;
            } else {
                spannable = new SpannableString(text);
                Object text2 = spannable;
            }
            EasyEditSpan[] spans = (EasyEditSpan[]) spannable.getSpans(0, text.length(), EasyEditSpan.class);
            for (Object removeSpan : spans) {
                spannable.removeSpan(removeSpan);
            }
        }
        return text;
    }

    public void setInputType(int type) {
        boolean singleLine;
        boolean z = false;
        boolean wasPassword = isPasswordInputType(getInputType());
        boolean wasVisiblePassword = isVisiblePasswordInputType(getInputType());
        setInputType(type, false);
        boolean isPassword = isPasswordInputType(type);
        boolean isVisiblePassword = isVisiblePasswordInputType(type);
        boolean forceUpdate = false;
        if (isPassword) {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else if (isVisiblePassword) {
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
        } else if (wasPassword || wasVisiblePassword) {
            setTypefaceFromAttrs(null, -1, -1);
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
        }
        if (isMultilineInputType(type)) {
            singleLine = false;
        } else {
            singleLine = true;
        }
        if (this.mSingleLine != singleLine || forceUpdate) {
            if (!isPassword) {
                z = true;
            }
            applySingleLine(singleLine, z, true);
        }
        if (!isSuggestionsEnabled()) {
            this.mText = removeSuggestionSpans(this.mText);
        }
        if (GateConfig.isGateEnabled()) {
            logLetterMode(type);
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    boolean hasPasswordTransformationMethod() {
        return this.mTransformation instanceof PasswordTransformationMethod;
    }

    private static boolean isPasswordInputType(int inputType) {
        int variation = inputType & 4095;
        return variation == 129 || variation == 225 || variation == 18;
    }

    private static boolean isVisiblePasswordInputType(int inputType) {
        return (inputType & 4095) == 145;
    }

    public void setRawInputType(int type) {
        if (type != 0 || this.mEditor != null) {
            createEditorIfNeeded();
            this.mEditor.mInputType = type;
            if (GateConfig.isGateEnabled()) {
                logLetterMode(type);
            }
        }
    }

    private void setInputType(int type, boolean direct) {
        KeyListener input;
        boolean z = true;
        int cls = type & 15;
        if (cls == 1) {
            boolean autotext;
            Capitalize cap;
            if ((32768 & type) != 0) {
                autotext = true;
            } else {
                autotext = false;
            }
            if ((type & 4096) != 0) {
                cap = Capitalize.CHARACTERS;
            } else if ((type & 8192) != 0) {
                cap = Capitalize.WORDS;
            } else if ((type & 16384) != 0) {
                cap = Capitalize.SENTENCES;
            } else {
                cap = Capitalize.NONE;
            }
            input = TextKeyListener.getInstance(autotext, cap);
        } else if (cls == 2) {
            boolean z2 = (type & 4096) != 0;
            if ((type & 8192) == 0) {
                z = false;
            }
            input = DigitsKeyListener.getInstance(z2, z);
        } else if (cls == 4) {
            switch (type & InputType.TYPE_MASK_VARIATION) {
                case 16:
                    input = DateKeyListener.getInstance();
                    break;
                case 32:
                    input = TimeKeyListener.getInstance();
                    break;
                default:
                    input = DateTimeKeyListener.getInstance();
                    break;
            }
        } else if (cls == 3) {
            input = DialerKeyListener.getInstance();
        } else {
            input = TextKeyListener.getInstance();
        }
        setRawInputType(type);
        if (direct) {
            createEditorIfNeeded();
            this.mEditor.mKeyListener = input;
        } else {
            setKeyListenerOnly(input);
        }
        if (GateConfig.isGateEnabled()) {
            logLetterMode(type);
        }
    }

    private void logLetterMode(int type) {
        if (GateConfig.isGateEnabled() && (type & 15) == 1) {
            int shift;
            if ((type & 4096) != 0) {
                shift = 2;
            } else if ((type & 8192) != 0) {
                shift = 3;
            } else if ((type & 16384) != 0) {
                shift = 1;
            } else {
                shift = 0;
            }
            Log.i("GATE", "<GATE-M>KEYSELECT: SHIFT=" + shift + "</GATE-M>");
        }
    }

    public int getInputType() {
        return this.mEditor == null ? 0 : this.mEditor.mInputType;
    }

    public void setImeOptions(int imeOptions) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeOptions = imeOptions;
    }

    public int getImeOptions() {
        return (this.mEditor == null || this.mEditor.mInputContentType == null) ? 0 : this.mEditor.mInputContentType.imeOptions;
    }

    public void setImeActionLabel(CharSequence label, int actionId) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeActionLabel = label;
        this.mEditor.mInputContentType.imeActionId = actionId;
    }

    public CharSequence getImeActionLabel() {
        return (this.mEditor == null || this.mEditor.mInputContentType == null) ? null : this.mEditor.mInputContentType.imeActionLabel;
    }

    public int getImeActionId() {
        return (this.mEditor == null || this.mEditor.mInputContentType == null) ? 0 : this.mEditor.mInputContentType.imeActionId;
    }

    public void setOnEditorActionListener(OnEditorActionListener l) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.onEditorActionListener = l;
    }

    public void onEditorAction(int actionCode) {
        InputContentType ict;
        if (this.mEditor == null) {
            ict = null;
        } else {
            ict = this.mEditor.mInputContentType;
        }
        if (ict != null) {
            if (ict.onEditorActionListener != null && ict.onEditorActionListener.onEditorAction(this, actionCode, null)) {
                return;
            }
            View v;
            if (actionCode == 5) {
                v = focusSearch(2);
                if (v != null && !v.requestFocus(2)) {
                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                }
                return;
            } else if (actionCode == 7) {
                v = focusSearch(1);
                if (v != null && !v.requestFocus(1)) {
                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                }
                return;
            } else if (actionCode == 6) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                if (imm != null && imm.isActive(this)) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    return;
                }
                return;
            }
        }
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            long eventTime = SystemClock.uptimeMillis();
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(eventTime, eventTime, 0, 66, 0, 0, -1, 0, 22));
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(SystemClock.uptimeMillis(), eventTime, 1, 66, 0, 0, -1, 0, 22));
        }
    }

    public void setPrivateImeOptions(String type) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.privateImeOptions = type;
    }

    public String getPrivateImeOptions() {
        return (this.mEditor == null || this.mEditor.mInputContentType == null) ? null : this.mEditor.mInputContentType.privateImeOptions;
    }

    public void setInputExtras(int xmlResId) throws XmlPullParserException, IOException {
        createEditorIfNeeded();
        XmlResourceParser parser = getResources().getXml(xmlResId);
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.extras = new Bundle();
        getResources().parseBundleExtras(parser, this.mEditor.mInputContentType.extras);
    }

    public Bundle getInputExtras(boolean create) {
        if (this.mEditor == null && !create) {
            return null;
        }
        createEditorIfNeeded();
        if (this.mEditor.mInputContentType == null) {
            if (!create) {
                return null;
            }
            this.mEditor.createInputContentTypeIfNeeded();
        }
        if (this.mEditor.mInputContentType.extras == null) {
            if (!create) {
                return null;
            }
            this.mEditor.mInputContentType.extras = new Bundle();
        }
        return this.mEditor.mInputContentType.extras;
    }

    public CharSequence getError() {
        return this.mEditor == null ? null : this.mEditor.mError;
    }

    @RemotableViewMethod
    public void setError(CharSequence error) {
        if (error == null) {
            setError(null, null);
            return;
        }
        Drawable dr = getContext().getDrawable(R.drawable.indicator_input_error);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        setError(error, dr);
    }

    public void setError(CharSequence error, Drawable icon) {
        createEditorIfNeeded();
        this.mEditor.setError(error, icon);
        notifyViewAccessibilityStateChangedIfNeeded(0);
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        if (this.mEditor != null) {
            this.mEditor.setFrame();
        }
        restartMarqueeIfNeeded();
        return result;
    }

    private void restartMarqueeIfNeeded() {
        if (this.mRestartMarquee && this.mEllipsize == TruncateAt.MARQUEE) {
            this.mRestartMarquee = false;
            startMarquee();
        }
    }

    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            throw new IllegalArgumentException();
        }
        this.mFilters = filters;
        if (this.mText instanceof Editable) {
            setFilters((Editable) this.mText, filters);
        }
    }

    private void setFilters(Editable e, InputFilter[] filters) {
        if (this.mEditor != null) {
            boolean undoFilter = this.mEditor.mUndoInputFilter != null;
            boolean keyFilter = this.mEditor.mKeyListener instanceof InputFilter;
            int num = 0;
            if (undoFilter) {
                num = 0 + 1;
            }
            if (keyFilter) {
                num++;
            }
            if (num > 0) {
                InputFilter[] nf = new InputFilter[(filters.length + num)];
                System.arraycopy(filters, 0, nf, 0, filters.length);
                num = 0;
                if (undoFilter) {
                    nf[filters.length] = this.mEditor.mUndoInputFilter;
                    num = 0 + 1;
                }
                if (keyFilter) {
                    nf[filters.length + num] = (InputFilter) this.mEditor.mKeyListener;
                }
                e.setFilters(nf);
                return;
            }
        }
        e.setFilters(filters);
    }

    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    private int getBoxHeight(Layout l) {
        Insets opticalInsets = View.isLayoutModeOptical(this.mParent) ? getOpticalInsets() : Insets.NONE;
        return ((getMeasuredHeight() - (l == this.mHintLayout ? getCompoundPaddingTop() + getCompoundPaddingBottom() : getExtendedPaddingTop() + getExtendedPaddingBottom())) + opticalInsets.top) + opticalInsets.bottom;
    }

    int getVerticalOffset(boolean forceNormal) {
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!(forceNormal || this.mText.length() != 0 || this.mHintLayout == null)) {
            l = this.mHintLayout;
        }
        if (gravity == 48) {
            return 0;
        }
        int boxht = getBoxHeight(l);
        int textht = l.getHeight();
        if (textht >= boxht) {
            return 0;
        }
        if (gravity == 80) {
            return boxht - textht;
        }
        return (boxht - textht) >> 1;
    }

    private int getBottomVerticalOffset(boolean forceNormal) {
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!(forceNormal || this.mText.length() != 0 || this.mHintLayout == null)) {
            l = this.mHintLayout;
        }
        if (gravity == 80) {
            return 0;
        }
        int boxht = getBoxHeight(l);
        int textht = l.getHeight();
        if (textht >= boxht) {
            return 0;
        }
        if (gravity == 48) {
            return boxht - textht;
        }
        return (boxht - textht) >> 1;
    }

    void invalidateCursorPath() {
        if (this.mHighlightPathBogus) {
            invalidateCursor();
            return;
        }
        int horizontalPadding = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (this.mEditor.mCursorCount == 0) {
            synchronized (TEMP_RECTF) {
                float thick = (float) Math.ceil((double) this.mTextPaint.getStrokeWidth());
                if (thick < 1.0f) {
                    thick = 1.0f;
                }
                thick /= 2.0f;
                this.mHighlightPath.computeBounds(TEMP_RECTF, false);
                invalidate((int) Math.floor((double) ((((float) horizontalPadding) + TEMP_RECTF.left) - thick)), (int) Math.floor((double) ((((float) verticalPadding) + TEMP_RECTF.top) - thick)), (int) Math.ceil((double) ((((float) horizontalPadding) + TEMP_RECTF.right) + thick)), (int) Math.ceil((double) ((((float) verticalPadding) + TEMP_RECTF.bottom) + thick)));
            }
            return;
        }
        for (int i = 0; i < this.mEditor.mCursorCount; i++) {
            Rect bounds = this.mEditor.mCursorDrawable[i].getBounds();
            invalidate(bounds.left + horizontalPadding, bounds.top + verticalPadding, bounds.right + horizontalPadding, bounds.bottom + verticalPadding);
        }
    }

    void invalidateCursor() {
        int where = getSelectionEnd();
        invalidateCursor(where, where, where);
    }

    private void invalidateCursor(int a, int b, int c) {
        if (a >= 0 || b >= 0 || c >= 0) {
            invalidateRegion(Math.min(Math.min(a, b), c), Math.max(Math.max(a, b), c), true);
        }
    }

    void invalidateRegion(int start, int end, boolean invalidateCursor) {
        if (this.mLayout == null) {
            invalidate();
            return;
        }
        int lineEnd;
        int left;
        int right;
        int lineStart = this.mLayout.getLineForOffset(start);
        int top = this.mLayout.getLineTop(lineStart);
        if (lineStart > 0) {
            top -= this.mLayout.getLineDescent(lineStart - 1);
        }
        if (start == end) {
            lineEnd = lineStart;
        } else {
            lineEnd = this.mLayout.getLineForOffset(end);
        }
        int bottom = this.mLayout.getLineBottom(lineEnd);
        if (invalidateCursor && this.mEditor != null) {
            for (int i = 0; i < this.mEditor.mCursorCount; i++) {
                Rect bounds = this.mEditor.mCursorDrawable[i].getBounds();
                top = Math.min(top, bounds.top);
                bottom = Math.max(bottom, bounds.bottom);
            }
        }
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (lineStart != lineEnd || invalidateCursor) {
            left = compoundPaddingLeft;
            right = getWidth() - getCompoundPaddingRight();
        } else {
            left = ((int) this.mLayout.getPrimaryHorizontal(start)) + compoundPaddingLeft;
            right = ((int) (((double) this.mLayout.getPrimaryHorizontal(end)) + 1.0d)) + compoundPaddingLeft;
        }
        invalidate(this.mScrollX + left, verticalPadding + top, this.mScrollX + right, verticalPadding + bottom);
    }

    private void registerForPreDraw() {
        if (!this.mPreDrawRegistered) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawRegistered = true;
        }
    }

    private void unregisterForPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        this.mPreDrawRegistered = false;
        this.mPreDrawListenerDetached = false;
    }

    public boolean onPreDraw() {
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mMovement != null) {
            int curs = getSelectionEnd();
            if (!(this.mEditor == null || this.mEditor.mSelectionModifierCursorController == null || !this.mEditor.mSelectionModifierCursorController.isSelectionStartDragged())) {
                curs = getSelectionStart();
            }
            if (curs < 0 && (this.mGravity & 112) == 80) {
                curs = this.mText.length();
            }
            if (curs >= 0) {
                bringPointIntoView(curs);
            }
        } else {
            bringTextIntoView();
        }
        if (this.mEditor != null && this.mEditor.mCreatedWithASelection) {
            if (this.mEditor.extractedTextModeWillBeStarted()) {
                this.mEditor.checkFieldAndSelectCurrentWord();
            } else if (this.mEditor.mSelectionActionMode) {
                this.mEditor.startSelectionActionMode();
            }
            this.mEditor.mCreatedWithASelection = false;
        }
        unregisterForPreDraw();
        return true;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTemporaryDetach = false;
        if (isMultiPenSelectionEnabled()) {
            this.mAttachedWindow = true;
            registerForStylusPenEvent();
        } else if (isTextSelectionEnabled() && sSpenUspLevel > 0) {
            this.mAttachedWindow = true;
            if (textCanBeSelected()) {
                registerForStylusPenEvent();
            }
        }
        if (this.mEditor != null) {
            this.mEditor.onAttachedToWindow();
        }
        if (this.mPreDrawListenerDetached) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawListenerDetached = false;
        }
    }

    protected void onDetachedFromWindowInternal() {
        if (this.mPreDrawRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this);
            this.mPreDrawListenerDetached = true;
        }
        if (isMultiPenSelectionEnabled()) {
            removeForStylusPenEvent();
            clearMultiSelection();
        }
        if (isTextSelectionEnabled() && sSpenUspLevel > 0) {
            removeForStylusPenEvent();
        }
        resetResolvedDrawables();
        if (this.mEditor != null) {
            this.mEditor.onDetachedFromWindow();
        }
        super.onDetachedFromWindowInternal();
    }

    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (this.mEditor != null) {
            this.mEditor.onScreenStateChanged(screenState);
        }
    }

    protected boolean isPaddingOffsetRequired() {
        return this.mShadowRadius != 0.0f || this.mDrawables != null || this.mEllipsize == TruncateAt.MARQUEE || this.mTextPaint.hasTextEffect();
    }

    protected int getLeftPaddingOffset() {
        if (this.mTextPaint.hasTextEffect()) {
            return (getCompoundPaddingLeft() - this.mPaddingLeft) - this.mTextPaint.getTextEffectOffsetLeft();
        }
        return (getCompoundPaddingLeft() - this.mPaddingLeft) + ((int) Math.min(0.0f, this.mShadowDx - this.mShadowRadius));
    }

    protected int getTopPaddingOffset() {
        if (this.mTextPaint.hasTextEffect()) {
            return -this.mTextPaint.getTextEffectOffsetTop();
        }
        return (int) Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
    }

    protected int getBottomPaddingOffset() {
        if (this.mTextPaint.hasTextEffect()) {
            return this.mTextPaint.getTextEffectOffsetBottom();
        }
        return (int) Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
    }

    private int getFudgedPaddingRight() {
        return Math.max(0, getCompoundPaddingRight() - ((((int) this.mTextPaint.density) + 2) - 1));
    }

    protected int getRightPaddingOffset() {
        if (this.mTextPaint.hasTextEffect()) {
            return (-(getCompoundPaddingRight() - this.mPaddingRight)) + this.mTextPaint.getTextEffectOffsetRight();
        }
        return (-(getFudgedPaddingRight() - this.mPaddingRight)) + ((int) Math.max(0.0f, this.mShadowDx + this.mShadowRadius));
    }

    protected boolean verifyDrawable(Drawable who) {
        boolean verified = super.verifyDrawable(who);
        if (verified || this.mDrawables == null) {
            return verified;
        }
        for (Drawable dr : this.mDrawables.mShowing) {
            if (who == dr) {
                return true;
            }
        }
        return verified;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
            }
        }
    }

    public void invalidateDrawable(Drawable drawable) {
        boolean handled = false;
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getBounds();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            Drawables drawables = this.mDrawables;
            if (drawables != null) {
                int compoundPaddingTop;
                if (drawable == drawables.mShowing[0]) {
                    compoundPaddingTop = getCompoundPaddingTop();
                    scrollX += this.mPaddingLeft;
                    scrollY += (((((this.mBottom - this.mTop) - getCompoundPaddingBottom()) - compoundPaddingTop) - drawables.mDrawableHeightLeft) / 2) + compoundPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[2]) {
                    compoundPaddingTop = getCompoundPaddingTop();
                    scrollX += ((this.mRight - this.mLeft) - this.mPaddingRight) - drawables.mDrawableSizeRight;
                    scrollY += (((((this.mBottom - this.mTop) - getCompoundPaddingBottom()) - compoundPaddingTop) - drawables.mDrawableHeightRight) / 2) + compoundPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[1]) {
                    compoundPaddingLeft = getCompoundPaddingLeft();
                    scrollX += (((((this.mRight - this.mLeft) - getCompoundPaddingRight()) - compoundPaddingLeft) - drawables.mDrawableWidthTop) / 2) + compoundPaddingLeft;
                    scrollY += this.mPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[3]) {
                    compoundPaddingLeft = getCompoundPaddingLeft();
                    scrollX += (((((this.mRight - this.mLeft) - getCompoundPaddingRight()) - compoundPaddingLeft) - drawables.mDrawableWidthBottom) / 2) + compoundPaddingLeft;
                    scrollY += ((this.mBottom - this.mTop) - this.mPaddingBottom) - drawables.mDrawableSizeBottom;
                    handled = true;
                }
            }
            if (handled) {
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            }
        }
        if (!handled) {
            super.invalidateDrawable(drawable);
        }
    }

    public boolean hasOverlappingRendering() {
        return !(getBackground() == null || getBackground().getCurrent() == null) || (this.mText instanceof Spannable) || hasSelection() || isHorizontalFadingEdgeEnabled();
    }

    public boolean isTextSelectable() {
        return this.mEditor == null ? false : this.mEditor.mTextIsSelectable;
    }

    public void setTextIsSelectable(boolean selectable) {
        if (selectable || this.mEditor != null) {
            createEditorIfNeeded();
            if (this.mEditor.mTextIsSelectable != selectable) {
                if (isMultiPenSelectionEnabled()) {
                    registerForStylusPenEvent();
                } else if (isTextSelectionEnabled() && sSpenUspLevel > 0 && selectable && this.mAttachedWindow) {
                    registerForStylusPenEvent();
                }
                this.mEditor.mTextIsSelectable = selectable;
                setFocusableInTouchMode(selectable);
                setFocusable(selectable);
                setClickable(selectable);
                setLongClickable(selectable);
                setMovementMethod(selectable ? ArrowKeyMovementMethod.getInstance() : null);
                setText(this.mText, selectable ? BufferType.SPANNABLE : BufferType.NORMAL);
                this.mEditor.prepareCursorControllers();
            }
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState;
        if (this.mSingleLine) {
            drawableState = super.onCreateDrawableState(extraSpace);
        } else {
            drawableState = super.onCreateDrawableState(extraSpace + 1);
            View.mergeDrawableStates(drawableState, MULTILINE_STATE_SET);
        }
        if (isTextSelectable()) {
            int length = drawableState.length;
            for (int i = 0; i < length; i++) {
                if (drawableState[i] == 16842919) {
                    int[] nonPressedState = new int[(length - 1)];
                    System.arraycopy(drawableState, 0, nonPressedState, 0, i);
                    System.arraycopy(drawableState, i + 1, nonPressedState, i, (length - i) - 1);
                    return nonPressedState;
                }
            }
        }
        return drawableState;
    }

    private Path getUpdatedHighlightPath() {
        Path highlight = null;
        Paint highlightPaint = this.mHighlightPaint;
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (this.mhasMultiSelection) {
            CharSequence text = getTextForMultiSelection();
            if (text == null) {
                this.mhasMultiSelection = false;
                return null;
            }
            if (this.mHighlightPathBogus) {
                if (this.mHighlightPath == null) {
                    this.mHighlightPath = new Path();
                }
                this.mHighlightPath.reset();
                int[] multiSelStart = MultiSelection.getMultiSelectionStart((Spannable) text);
                int[] multiSelEnd = MultiSelection.getMultiSelectionEnd((Spannable) text);
                int multiSelCount = MultiSelection.getMultiSelectionCount((Spannable) text);
                for (int i = 0; i < multiSelCount; i++) {
                    this.mLayout.addSelectionPath(multiSelStart[i], multiSelEnd[i], this.mHighlightPath);
                }
                this.mHighlightPathBogus = false;
            }
            this.mMultiHighlightPaint.setColor(this.mMultiHighlightColor);
            this.mMultiHighlightPaint.setStyle(Style.FILL);
            return this.mHighlightPath;
        }
        if (this.mMovement != null && ((isFocused() || isPressed()) && selStart >= 0)) {
            if (selStart != selEnd) {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                highlightPaint.setColor(this.mHighlightColor);
                highlightPaint.setStyle(Style.FILL);
                highlight = this.mHighlightPath;
            } else if (this.mEditor != null && this.mEditor.isCursorVisible() && (SystemClock.uptimeMillis() - this.mEditor.mShowCursor) % 1000 < 500) {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getCursorPath(selStart, this.mHighlightPath, this.mText);
                    this.mEditor.updateCursorsPositions();
                    this.mHighlightPathBogus = false;
                }
                highlightPaint.setColor(this.mCurTextColor);
                highlightPaint.setStyle(Style.STROKE);
                highlightPaint.setStrokeWidth((float) this.mCursorWidth);
                highlight = this.mHighlightPath;
            }
        }
        return highlight;
    }

    public int getHorizontalOffsetForDrawables() {
        return 0;
    }

    protected void onDraw(Canvas canvas) {
        int leftOffset;
        float clipTop;
        restartMarqueeIfNeeded();
        super.onDraw(canvas);
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int compoundPaddingTop = getCompoundPaddingTop();
        int compoundPaddingRight = getCompoundPaddingRight();
        int compoundPaddingBottom = getCompoundPaddingBottom();
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        int right = this.mRight;
        int left = this.mLeft;
        int bottom = this.mBottom;
        int top = this.mTop;
        boolean isLayoutRtl = isLayoutRtl();
        int offset = getHorizontalOffsetForDrawables();
        if (isLayoutRtl) {
            leftOffset = 0;
        } else {
            leftOffset = offset;
        }
        int rightOffset = isLayoutRtl ? offset : 0;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            int vspace = ((bottom - top) - compoundPaddingBottom) - compoundPaddingTop;
            int hspace = ((right - left) - compoundPaddingRight) - compoundPaddingLeft;
            if (dr.mShowing[0] != null) {
                canvas.save();
                canvas.translate((float) ((this.mPaddingLeft + scrollX) + leftOffset), (float) ((scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightLeft) / 2)));
                dr.mShowing[0].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[2] != null) {
                canvas.save();
                canvas.translate((float) (((((scrollX + right) - left) - this.mPaddingRight) - dr.mDrawableSizeRight) - rightOffset), (float) ((scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightRight) / 2)));
                dr.mShowing[2].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[1] != null) {
                canvas.save();
                canvas.translate((float) ((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthTop) / 2)), (float) (this.mPaddingTop + scrollY));
                dr.mShowing[1].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[3] != null) {
                canvas.save();
                canvas.translate((float) ((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthBottom) / 2)), (float) ((((scrollY + bottom) - top) - this.mPaddingBottom) - dr.mDrawableSizeBottom));
                dr.mShowing[3].draw(canvas);
                canvas.restore();
            }
        }
        int color = this.mCurTextColor;
        if (this.mLayout == null) {
            assumeLayout();
        }
        Layout layout = this.mLayout;
        if (this.mHint != null && this.mText.length() == 0) {
            if (this.mHintTextColor != null) {
                color = this.mCurHintTextColor;
            }
            layout = this.mHintLayout;
        }
        this.mTextPaint.setColor(color);
        this.mTextPaint.drawableState = getDrawableState();
        canvas.save();
        int extendedPaddingTop = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();
        int maxScrollY = this.mLayout.getHeight() - (((this.mBottom - this.mTop) - compoundPaddingBottom) - compoundPaddingTop);
        float clipLeft = (float) (compoundPaddingLeft + scrollX);
        if (scrollY == 0) {
            clipTop = 0.0f;
        } else {
            clipTop = (float) (extendedPaddingTop + scrollY);
        }
        float clipRight = (float) (((right - left) - getFudgedPaddingRight()) + scrollX);
        int i = (bottom - top) + scrollY;
        if (scrollY == maxScrollY) {
            extendedPaddingBottom = 0;
        }
        float clipBottom = (float) (i - extendedPaddingBottom);
        if (this.mTextPaint.hasTextEffect()) {
            clipLeft -= (float) this.mTextPaint.getTextEffectOffsetLeft();
            clipRight += (float) this.mTextPaint.getTextEffectOffsetRight();
            clipTop -= (float) this.mTextPaint.getTextEffectOffsetTop();
            clipBottom += (float) this.mTextPaint.getTextEffectOffsetBottom();
        } else if (this.mShadowRadius != 0.0f) {
            clipLeft += Math.min(0.0f, this.mShadowDx - this.mShadowRadius);
            clipRight += Math.max(0.0f, this.mShadowDx + this.mShadowRadius);
            clipTop += Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
            clipBottom += Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
        }
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        int voffsetText = 0;
        int voffsetCursor = 0;
        if ((this.mGravity & 112) != 48) {
            voffsetText = getVerticalOffset(false);
            voffsetCursor = getVerticalOffset(true);
        }
        int hctHor = 0;
        int hctVert = 0;
        int layoutDirection = getLayoutDirection();
        int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, layoutDirection);
        if (isHighContrastTextEnabled()) {
            if ((absoluteGravity & 7) != 1) {
                hctHor = (int) Math.floor((double) (this.mTextPaint.getHCTStrokeWidth() / 2.0f));
                if (layoutDirection == 1) {
                    hctHor *= -1;
                }
            }
            if ((absoluteGravity & 112) != 16) {
                hctVert = (int) Math.floor((double) (this.mTextPaint.getHCTStrokeWidth() / 2.0f));
            }
        }
        canvas.translate((float) (compoundPaddingLeft + hctHor), (float) ((extendedPaddingTop + voffsetText) + hctVert));
        absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, getLayoutDirection());
        if (this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode != 1) {
            if (!this.mSingleLine && getLineCount() == 1 && canMarquee() && (absoluteGravity & 7) != 3) {
                canvas.translate(((float) layout.getParagraphDirection(0)) * (this.mLayout.getLineRight(0) - ((float) ((this.mRight - this.mLeft) - (getCompoundPaddingLeft() + getCompoundPaddingRight())))), 0.0f);
            }
            if (this.mMarquee != null && this.mMarquee.isRunning()) {
                canvas.translate(((float) layout.getParagraphDirection(0)) * (-this.mMarquee.getScroll()), 0.0f);
            }
        }
        int cursorOffsetVertical = voffsetCursor - voffsetText;
        Path highlight = getUpdatedHighlightPath();
        Paint highlightPaint = this.mHighlightPaint;
        if (this.mhasMultiSelection) {
            highlightPaint = this.mMultiHighlightPaint;
        }
        if (layout != null) {
            layout.setHighContastTextMode(isHighContrastTextEnabled());
        }
        if (this.mEditor != null) {
            this.mEditor.onDraw(canvas, layout, highlight, highlightPaint, cursorOffsetVertical);
        } else {
            layout.draw(canvas, highlight, highlightPaint, cursorOffsetVertical);
        }
        if (this.mMarquee != null && this.mMarquee.shouldDrawGhost()) {
            canvas.translate(((float) layout.getParagraphDirection(0)) * this.mMarquee.getGhostOffset(), 0.0f);
            layout.draw(canvas, highlight, highlightPaint, cursorOffsetVertical);
        }
        if (GateConfig.isGateEnabled() && GateConfig.isGateLcdtextEnabled()) {
            Log.i("GATE", "<GATE-M>LCDSTR:" + this.mText + "/LCDSTR</GATE-M>");
        }
        canvas.restore();
        drawTextStrikethrough(canvas, color);
    }

    private void initTextStrikeThroughAnim(Context context) {
        this.mStrikeThroughPaint = new Paint();
        this.mStrikeThroughPaint.setColor(this.mCurTextColor);
        this.mStrikeThroughPaint.setStrokeWidth(3.0f);
        if (this.mDrawTextStrikeAnimator == null) {
            this.mDrawTextStrikeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mDrawTextStrikeAnimator.setDuration(400);
            this.mDrawTextStrikeAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    TextView.this.mLineIsDrawed = true;
                }
            });
            this.mDrawTextStrikeAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator anim) {
                    TextView.this.mDrawStrikeAnimationValue = anim.getAnimatedFraction();
                    TextView.this.invalidate();
                }
            });
        }
    }

    private void drawTextStrikethrough(Canvas canvas, int textColor) {
        int leftPadding = getTotalPaddingLeft();
        int rightPadding = getTotalPaddingBottom();
        int topPadding = getTotalPaddingTop();
        int bottomPadding = getTotalPaddingBottom();
        int lineCount = getLineCount();
        Layout textLayout = getLayout();
        if (this.mStrikeThroughPaint != null) {
            this.mStrikeThroughPaint.setColor(textColor);
        }
        if (!this.mSkipDrawTextStrike && this.mTextStrikeThroughEnabled && this.mDrawStrikeAnimationValue > 0.0f && this.mStrikeThroughPaint != null && lineCount > 0) {
            int i;
            float[] accumLineWidths = new float[lineCount];
            for (i = 0; i < lineCount; i++) {
                accumLineWidths[i] = textLayout.getLineWidth(i);
                if (i > 0) {
                    accumLineWidths[i] = accumLineWidths[i] + accumLineWidths[i - 1];
                }
            }
            float strikethroughCurrLength = accumLineWidths[accumLineWidths.length - 1] * this.mDrawStrikeAnimationValue;
            i = 0;
            while (i < lineCount) {
                float lowerBound = i == 0 ? 0.0f : accumLineWidths[i - 1];
                float upperBound = accumLineWidths[i];
                float currentLineLength = 0.0f;
                if (strikethroughCurrLength <= lowerBound) {
                    currentLineLength = 0.0f;
                } else if (strikethroughCurrLength > lowerBound && strikethroughCurrLength <= upperBound) {
                    currentLineLength = strikethroughCurrLength - lowerBound;
                } else if (strikethroughCurrLength > upperBound) {
                    currentLineLength = upperBound - lowerBound;
                }
                float lineTop = (float) (textLayout.getLineTop(i) + topPadding);
                float baseLine = (float) (textLayout.getLineBaseline(i) + topPadding);
                if (i == 0) {
                    lineTop -= (float) textLayout.getTopPadding();
                }
                float lineY = (lineTop + ((baseLine - lineTop) * 0.65f)) - (this.mStrikeThroughPaint.getStrokeWidth() / 2.0f);
                canvas.drawLine((float) leftPadding, lineY, ((float) leftPadding) + currentLineLength, lineY, this.mStrikeThroughPaint);
                i++;
            }
        }
    }

    public void setStrikeAnimationEnabled(boolean enabled) {
        this.mTextStrikeThroughEnabled = enabled;
    }

    public void startDrawStrikeAnimation() {
        if (this.mDrawTextStrikeAnimator != null && this.mTextStrikeThroughEnabled) {
            this.mSkipDrawTextStrike = false;
            this.mDrawTextStrikeAnimator.start();
        }
    }

    public void removeTextStrike() {
        this.mSkipDrawTextStrike = true;
        invalidate();
    }

    public void getFocusedRect(Rect r) {
        if (this.mLayout == null) {
            super.getFocusedRect(r);
            return;
        }
        int selEnd = getSelectionEnd();
        if (selEnd < 0) {
            super.getFocusedRect(r);
            return;
        }
        int selStart = getSelectionStart();
        if (selStart < 0 || selStart >= selEnd) {
            int line = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(line);
            r.bottom = this.mLayout.getLineBottom(line);
            r.left = ((int) this.mLayout.getPrimaryHorizontal(selEnd)) - 2;
            r.right = r.left + 4;
        } else {
            int lineStart = this.mLayout.getLineForOffset(selStart);
            int lineEnd = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(lineStart);
            r.bottom = this.mLayout.getLineBottom(lineEnd);
            if (lineStart == lineEnd) {
                r.left = (int) this.mLayout.getPrimaryHorizontal(selStart);
                r.right = (int) this.mLayout.getPrimaryHorizontal(selEnd);
            } else {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                synchronized (TEMP_RECTF) {
                    this.mHighlightPath.computeBounds(TEMP_RECTF, true);
                    r.left = ((int) TEMP_RECTF.left) - 1;
                    r.right = ((int) TEMP_RECTF.right) + 1;
                }
            }
        }
        int paddingLeft = getCompoundPaddingLeft();
        int paddingTop = getExtendedPaddingTop();
        if ((this.mGravity & 112) != 48) {
            paddingTop += getVerticalOffset(false);
        }
        r.offset(paddingLeft, paddingTop);
        r.bottom += getExtendedPaddingBottom();
    }

    public int getLineCount() {
        return this.mLayout != null ? this.mLayout.getLineCount() : 0;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (this.mLayout != null) {
            int baseline = this.mLayout.getLineBounds(line, bounds);
            int voffset = getExtendedPaddingTop();
            if ((this.mGravity & 112) != 48) {
                voffset += getVerticalOffset(true);
            }
            if (bounds != null) {
                bounds.offset(getCompoundPaddingLeft(), voffset);
            }
            return baseline + voffset;
        } else if (bounds == null) {
            return 0;
        } else {
            bounds.set(0, 0, 0, 0);
            return 0;
        }
    }

    public int getBaseline() {
        if (this.mLayout == null) {
            return super.getBaseline();
        }
        return getBaselineOffset() + this.mLayout.getLineBaseline(0);
    }

    int getBaselineOffset() {
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (View.isLayoutModeOptical(this.mParent)) {
            voffset -= getOpticalInsets().top;
        }
        return getExtendedPaddingTop() + voffset;
    }

    protected int getFadeTop(boolean offsetRequired) {
        if (this.mLayout == null) {
            return 0;
        }
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (offsetRequired) {
            voffset += getTopPaddingOffset();
        }
        return getExtendedPaddingTop() + voffset;
    }

    protected int getFadeHeight(boolean offsetRequired) {
        return this.mLayout != null ? this.mLayout.getHeight() : 0;
    }

    public void changeClipboardDataFormat(int newFormat) {
        if (this.mClipboardDataFormat != newFormat) {
            this.mClipboardDataFormat = newFormat;
        }
    }

    public boolean onKeyTextMultiSelection(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mhasMultiSelection) {
            return super.onKeyTextMultiSelection(keyCode, event);
        }
        clearMultiSelection();
        return true;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && handleBackInTextActionModeIfNeeded(event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public boolean handleBackInTextActionModeIfNeeded(KeyEvent event) {
        if (this.mEditor == null || this.mEditor.mTextActionMode == null) {
            return false;
        }
        DispatcherState state;
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            state = getKeyDispatcherState();
            if (state == null) {
                return true;
            }
            state.startTracking(event, this);
            return true;
        }
        if (event.getAction() == 1) {
            state = getKeyDispatcherState();
            if (state != null) {
                state.handleUpEvent(event);
            }
            if (event.isTracking() && !event.isCanceled()) {
                stopTextActionMode();
                return true;
            }
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getSecClipboardEnabled() && keyCode == 1009 && !getClipboardExManager().showDialog(this.mClipboardDataFormat, this.mPasteEventListener)) {
            Log.e(LOG_TAG, "Samsung clipboard is not shown");
        }
        if (keyCode == 19 || keyCode == 20 || keyCode == 21 || keyCode == 22) {
            stopTextActionMode();
            if (event.isShiftPressed()) {
                this.mChangedSelectionBySIP = true;
            }
        }
        if (doKeyDown(keyCode, event, null) == 0) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        KeyEvent down = KeyEvent.changeAction(event, 0);
        int which = doKeyDown(keyCode, down, event);
        if (which == 0) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        if (which == -1) {
            return true;
        }
        repeatCount--;
        KeyEvent up = KeyEvent.changeAction(event, 1);
        if (which == 1) {
            this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            while (true) {
                repeatCount--;
                if (repeatCount <= 0) {
                    break;
                }
                this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, down);
                this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            }
            hideErrorIfUnchanged();
        } else if (which == 2) {
            this.mMovement.onKeyUp(this, (Spannable) this.mText, keyCode, up);
            while (true) {
                repeatCount--;
                if (repeatCount <= 0) {
                    break;
                }
                this.mMovement.onKeyDown(this, (Spannable) this.mText, keyCode, down);
                this.mMovement.onKeyUp(this, (Spannable) this.mText, keyCode, up);
            }
        }
        return true;
    }

    private boolean shouldAdvanceFocusOnEnter() {
        if (getKeyListener() == null) {
            return false;
        }
        if (this.mSingleLine) {
            return true;
        }
        if (this.mEditor == null || (this.mEditor.mInputType & 15) != 1) {
            return false;
        }
        int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
        if (variation == 32 || variation == 48) {
            return true;
        }
        return false;
    }

    private boolean shouldAdvanceFocusOnTab() {
        if (getKeyListener() == null || this.mSingleLine || this.mEditor == null || (this.mEditor.mInputType & 15) != 1) {
            return true;
        }
        int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
        if (variation == 262144 || variation == 131072) {
            return false;
        }
        return true;
    }

    private int doKeyDown(int keyCode, KeyEvent event, KeyEvent otherEvent) {
        if (!isEnabled()) {
            return 0;
        }
        boolean doDown;
        int i;
        if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
            this.mPreventDefaultMovement = false;
        }
        switch (keyCode) {
            case 4:
                if (getSecClipboardEnabled() && getClipboardExManager().isShowing()) {
                    getClipboardExManager().dismissDialog();
                    return -1;
                } else if (!(this.mEditor == null || this.mEditor.mTextActionMode == null)) {
                    stopTextActionMode();
                    return -1;
                }
                break;
            case 23:
                if (event.hasNoModifiers() && shouldAdvanceFocusOnEnter()) {
                    return 0;
                }
            case 61:
                if ((event.hasNoModifiers() || event.hasModifiers(1)) && shouldAdvanceFocusOnTab()) {
                    return 0;
                }
            case 66:
                if (event.hasNoModifiers()) {
                    if (this.mEditor != null && this.mEditor.mInputContentType != null && this.mEditor.mInputContentType.onEditorActionListener != null && this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                        this.mEditor.mInputContentType.enterDown = true;
                        return -1;
                    } else if ((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) {
                        if (hasOnClickListeners()) {
                            return 0;
                        }
                        return -1;
                    }
                }
                break;
        }
        if (!(this.mEditor == null || this.mEditor.mKeyListener == null)) {
            boolean handled;
            doDown = true;
            if (otherEvent != null) {
                try {
                    beginBatchEdit();
                    handled = this.mEditor.mKeyListener.onKeyOther(this, (Editable) this.mText, otherEvent);
                    hideErrorIfUnchanged();
                    doDown = false;
                    if (handled) {
                        return -1;
                    }
                    endBatchEdit();
                } catch (AbstractMethodError e) {
                } finally {
                    endBatchEdit();
                }
            }
            if (doDown) {
                beginBatchEdit();
                handled = this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, event);
                endBatchEdit();
                hideErrorIfUnchanged();
                if (handled) {
                    return 1;
                }
            }
        }
        if (!(this.mMovement == null || this.mLayout == null)) {
            doDown = true;
            if (otherEvent != null) {
                try {
                    doDown = false;
                    if (this.mMovement.onKeyOther(this, (Spannable) this.mText, otherEvent)) {
                        return -1;
                    }
                } catch (AbstractMethodError e2) {
                }
            }
            if (doDown && this.mMovement.onKeyDown(this, (Spannable) this.mText, keyCode, event)) {
                if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                    this.mPreventDefaultMovement = true;
                }
                return 2;
            }
        }
        if (!this.mPreventDefaultMovement || KeyEvent.isModifierKey(keyCode)) {
            boolean z = false;
        } else {
            i = -1;
        }
        return i;
    }

    public void resetErrorChangedFlag() {
        if (this.mEditor != null) {
            this.mEditor.mErrorWasChanged = false;
        }
    }

    public void hideErrorIfUnchanged() {
        if (this.mEditor != null && this.mEditor.mError != null && !this.mEditor.mErrorWasChanged) {
            setError(null, null);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isEnabled()) {
            return super.onKeyUp(keyCode, event);
        }
        InputMethodManager imm;
        if (!KeyEvent.isModifierKey(keyCode)) {
            this.mPreventDefaultMovement = false;
        }
        switch (keyCode) {
            case 23:
                if (event.hasNoModifiers() && !hasOnClickListeners() && this.mMovement != null && (this.mText instanceof Editable) && this.mLayout != null && onCheckIsTextEditor()) {
                    imm = InputMethodManager.peekInstance();
                    viewClicked(imm);
                    if (imm != null && getShowSoftInputOnFocus()) {
                        imm.showSoftInput(this, 0);
                    }
                }
                return super.onKeyUp(keyCode, event);
            case 66:
                if (event.hasNoModifiers()) {
                    if (!(this.mEditor == null || this.mEditor.mInputContentType == null || this.mEditor.mInputContentType.onEditorActionListener == null || !this.mEditor.mInputContentType.enterDown)) {
                        this.mEditor.mInputContentType.enterDown = false;
                        if (this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                            return true;
                        }
                    }
                    if (((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) && !hasOnClickListeners()) {
                        View v = focusSearch(130);
                        if (v != null) {
                            if (v.requestFocus(130)) {
                                super.onKeyUp(keyCode, event);
                                return true;
                            }
                            throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                        } else if ((event.getFlags() & 16) != 0) {
                            imm = InputMethodManager.peekInstance();
                            if (imm != null && imm.isActive(this)) {
                                imm.hideSoftInputFromWindow(getWindowToken(), 0);
                            }
                        }
                    }
                    return super.onKeyUp(keyCode, event);
                }
                break;
        }
        if ((keyCode == 59 || keyCode == 60) && this.mChangedSelectionBySIP) {
            imm = InputMethodManager.peekInstance();
            if (this.mEditor != null && hasSelection() && isFocused() && isShown() && imm != null && (imm.isAccessoryKeyboardState() & 7) == 0) {
                int start = getSelectionStart();
                int end = getSelectionEnd();
                if (start > end) {
                    Selection.setSelection((Spannable) this.mText, end, start);
                }
                this.mEditor.startSelectionActionMode();
            }
        }
        this.mChangedSelectionBySIP = false;
        if (this.mEditor != null && this.mEditor.mKeyListener != null && this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, event)) {
            return true;
        }
        if (this.mMovement == null || this.mLayout == null || !this.mMovement.onKeyUp(this, (Spannable) this.mText, keyCode, event)) {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    public boolean onCheckIsTextEditor() {
        return (this.mEditor == null || this.mEditor.mInputType == 0) ? false : true;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (onCheckIsTextEditor() && isEnabled()) {
            this.mEditor.createInputMethodStateIfNeeded();
            outAttrs.inputType = getInputType();
            if (this.mEditor.mInputContentType != null) {
                outAttrs.imeOptions = this.mEditor.mInputContentType.imeOptions;
                outAttrs.privateImeOptions = this.mEditor.mInputContentType.privateImeOptions;
                outAttrs.actionLabel = this.mEditor.mInputContentType.imeActionLabel;
                outAttrs.actionId = this.mEditor.mInputContentType.imeActionId;
                outAttrs.extras = this.mEditor.mInputContentType.extras;
            } else {
                outAttrs.imeOptions = 0;
            }
            if (focusSearch(130) != null) {
                outAttrs.imeOptions |= 134217728;
            }
            if (focusSearch(33) != null) {
                outAttrs.imeOptions |= 67108864;
            }
            if ((outAttrs.imeOptions & 255) == 0) {
                if ((outAttrs.imeOptions & 134217728) != 0) {
                    outAttrs.imeOptions |= 5;
                } else {
                    outAttrs.imeOptions |= 6;
                }
                if (!shouldAdvanceFocusOnEnter()) {
                    outAttrs.imeOptions |= 1073741824;
                }
            }
            if (isMultilineInputType(outAttrs.inputType)) {
                outAttrs.imeOptions |= 1073741824;
            }
            outAttrs.hintText = this.mHint;
            if (this.mText instanceof Editable) {
                InputConnection ic = new EditableInputConnection(this);
                outAttrs.initialSelStart = getSelectionStart();
                outAttrs.initialSelEnd = getSelectionEnd();
                outAttrs.initialCapsMode = ic.getCursorCapsMode(getInputType());
                return ic;
            }
        }
        return null;
    }

    public boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        createEditorIfNeeded();
        return this.mEditor.extractText(request, outText);
    }

    static void removeParcelableSpans(Spannable spannable, int start, int end) {
        Object[] spans = spannable.getSpans(start, end, ParcelableSpan.class);
        int i = spans.length;
        while (i > 0) {
            i--;
            spannable.removeSpan(spans[i]);
        }
    }

    public void setExtractedText(ExtractedText text) {
        int start;
        int end;
        int N;
        Editable content = getEditableText();
        if (text.text != null) {
            if (content == null) {
                setText(text.text, BufferType.EDITABLE);
            } else {
                start = 0;
                end = content.length();
                if (text.partialStartOffset >= 0) {
                    N = content.length();
                    start = text.partialStartOffset;
                    if (start > N) {
                        start = N;
                    }
                    end = text.partialEndOffset;
                    if (end > N) {
                        end = N;
                    }
                }
                removeParcelableSpans(content, start, end);
                if (!TextUtils.equals(content.subSequence(start, end), text.text)) {
                    content.replace(start, end, text.text);
                } else if (text.text instanceof Spanned) {
                    TextUtils.copySpansFrom((Spanned) text.text, start, end, Object.class, content, start);
                }
            }
        }
        Spannable sp = (Spannable) getText();
        N = sp.length();
        start = text.selectionStart;
        if (start < 0) {
            start = 0;
        } else if (start > N) {
            start = N;
        }
        end = text.selectionEnd;
        if (end < 0) {
            end = 0;
        } else if (end > N) {
            end = N;
        }
        Selection.setSelection(sp, start, end);
        if ((text.flags & 2) != 0) {
            MetaKeyKeyListener.startSelecting(this, sp);
        } else {
            MetaKeyKeyListener.stopSelecting(this, sp);
        }
    }

    public void setExtracting(ExtractedTextRequest req) {
        if (this.mEditor.mInputMethodState != null) {
            this.mEditor.mInputMethodState.mExtractedTextRequest = req;
        }
        this.mEditor.hideCursorAndSpanControllers();
        stopTextActionMode();
    }

    public void onCommitCompletion(CompletionInfo text) {
    }

    public void onCommitCorrection(CorrectionInfo info) {
        if (this.mEditor != null) {
            this.mEditor.onCommitCorrection(info);
        }
    }

    public void beginBatchEdit() {
        if (this.mEditor != null) {
            this.mEditor.beginBatchEdit();
        }
    }

    public void endBatchEdit() {
        if (this.mEditor != null) {
            this.mEditor.endBatchEdit();
        }
    }

    public void onBeginBatchEdit() {
    }

    public void onEndBatchEdit() {
    }

    public boolean onPrivateIMECommand(String action, Bundle data) {
        return false;
    }

    private void nullLayouts() {
        if ((this.mLayout instanceof BoringLayout) && this.mSavedLayout == null) {
            this.mSavedLayout = (BoringLayout) this.mLayout;
        }
        if ((this.mHintLayout instanceof BoringLayout) && this.mSavedHintLayout == null) {
            this.mSavedHintLayout = (BoringLayout) this.mHintLayout;
        }
        this.mHintLayout = null;
        this.mLayout = null;
        this.mSavedMarqueeModeLayout = null;
        this.mHintBoring = null;
        this.mBoring = null;
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
    }

    private void assumeLayout() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width < 1) {
            width = 0;
        }
        int physicalWidth = width;
        if (this.mHorizontallyScrolling) {
            width = 1048576;
        }
        makeNewLayout(width, physicalWidth, UNKNOWN_BORING, UNKNOWN_BORING, physicalWidth, false);
    }

    private Alignment getLayoutAlignment() {
        switch (getTextAlignment()) {
            case 1:
                switch (this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case 1:
                        return Alignment.ALIGN_CENTER;
                    case 3:
                        return Alignment.ALIGN_LEFT;
                    case 5:
                        return Alignment.ALIGN_RIGHT;
                    case Gravity.START /*8388611*/:
                        return Alignment.ALIGN_NORMAL;
                    case Gravity.END /*8388613*/:
                        return Alignment.ALIGN_OPPOSITE;
                    default:
                        return Alignment.ALIGN_NORMAL;
                }
            case 2:
                return Alignment.ALIGN_NORMAL;
            case 3:
                return Alignment.ALIGN_OPPOSITE;
            case 4:
                return Alignment.ALIGN_CENTER;
            case 5:
                return getLayoutDirection() == 1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
            case 6:
                return getLayoutDirection() == 1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT;
            default:
                return Alignment.ALIGN_NORMAL;
        }
    }

    protected void makeNewLayout(int wantWidth, int hintWidth, Metrics boring, Metrics hintBoring, int ellipsisWidth, boolean bringIntoView) {
        stopMarquee();
        this.mOldMaximum = this.mMaximum;
        this.mOldMaxMode = this.mMaxMode;
        this.mHighlightPathBogus = true;
        if (wantWidth < 0) {
            wantWidth = 0;
        }
        if (hintWidth < 0) {
            hintWidth = 0;
        }
        Alignment alignment = getLayoutAlignment();
        boolean testDirChange = this.mSingleLine && this.mLayout != null && (alignment == Alignment.ALIGN_NORMAL || alignment == Alignment.ALIGN_OPPOSITE);
        int oldDir = 0;
        if (testDirChange) {
            oldDir = this.mLayout.getParagraphDirection(0);
        }
        boolean shouldEllipsize = this.mEllipsize != null && getKeyListener() == null;
        boolean switchEllipsize = this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode != 0;
        TruncateAt effectiveEllipsize = this.mEllipsize;
        if (this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode == 1) {
            effectiveEllipsize = TruncateAt.END_SMALL;
        }
        if (this.mTextDir == null) {
            this.mTextDir = getTextDirectionHeuristic();
        }
        TextDirectionHeuristic hintTextDir = getTextDirectionHeuristic(true);
        this.mLayout = makeSingleLayout(wantWidth, boring, ellipsisWidth, alignment, shouldEllipsize, effectiveEllipsize, effectiveEllipsize == this.mEllipsize);
        if (switchEllipsize) {
            this.mSavedMarqueeModeLayout = makeSingleLayout(wantWidth, boring, ellipsisWidth, alignment, shouldEllipsize, effectiveEllipsize == TruncateAt.MARQUEE ? TruncateAt.END : TruncateAt.MARQUEE, effectiveEllipsize != this.mEllipsize);
        }
        shouldEllipsize = this.mEllipsize != null;
        this.mHintLayout = null;
        if (this.mHint != null) {
            if (shouldEllipsize) {
                hintWidth = wantWidth;
            }
            if (hintBoring == UNKNOWN_BORING) {
                hintBoring = BoringLayout.isBoring(this.mHint, this.mTextPaint, hintTextDir, this.mHintBoring);
                if (hintBoring != null) {
                    this.mHintBoring = hintBoring;
                }
            }
            if (hintBoring != null) {
                if (hintBoring.width <= hintWidth && (!shouldEllipsize || hintBoring.width <= ellipsisWidth)) {
                    if (this.mSavedHintLayout != null) {
                        this.mHintLayout = this.mSavedHintLayout.replaceOrMake(this.mHint, this.mTextPaint, hintWidth, alignment, this.mSpacingMult, this.mSpacingAdd, hintBoring, this.mIncludePad);
                    } else {
                        this.mHintLayout = BoringLayout.make(this.mHint, this.mTextPaint, hintWidth, alignment, this.mSpacingMult, this.mSpacingAdd, hintBoring, this.mIncludePad);
                    }
                    this.mSavedHintLayout = (BoringLayout) this.mHintLayout;
                } else if (shouldEllipsize && hintBoring.width <= hintWidth) {
                    if (this.mSavedHintLayout != null) {
                        this.mHintLayout = this.mSavedHintLayout.replaceOrMake(this.mHint, this.mTextPaint, hintWidth, alignment, this.mSpacingMult, this.mSpacingAdd, hintBoring, this.mIncludePad, this.mEllipsize, ellipsisWidth);
                    } else {
                        this.mHintLayout = BoringLayout.make(this.mHint, this.mTextPaint, hintWidth, alignment, this.mSpacingMult, this.mSpacingAdd, hintBoring, this.mIncludePad, this.mEllipsize, ellipsisWidth);
                    }
                }
            }
            if (this.mHintLayout == null) {
                Builder builder = Builder.obtain(this.mHint, 0, this.mHint.length(), this.mTextPaint, hintWidth).setAlignment(alignment).setTextDirection(hintTextDir).setLineSpacing(this.mSpacingAdd, this.mSpacingMult).setIncludePad(this.mIncludePad).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency);
                if (shouldEllipsize) {
                    builder.setEllipsize(this.mEllipsize).setEllipsizedWidth(ellipsisWidth).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE);
                }
                this.mHintLayout = builder.build();
            }
        }
        if (bringIntoView || (testDirChange && oldDir != this.mLayout.getParagraphDirection(0))) {
            registerForPreDraw();
        }
        if (this.mEllipsize == TruncateAt.MARQUEE) {
            if (!compressText((float) ellipsisWidth)) {
                int height = this.mLayoutParams.height;
                if (height == -2 || height == -1) {
                    this.mRestartMarquee = true;
                } else {
                    startMarquee();
                }
            }
        }
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
    }

    private Layout makeSingleLayout(int wantWidth, Metrics boring, int ellipsisWidth, Alignment alignment, boolean shouldEllipsize, TruncateAt effectiveEllipsize, boolean useSaved) {
        Layout result;
        CharSequence transformed = this.mTransformed;
        if (this.mUseDisplayText && isMultiPenSelectionEnabled()) {
            transformed = this.mDisplayText;
        }
        if (effectiveEllipsize == TruncateAt.KEYWORD && this.mEllipsisKeywordStart >= 0 && this.mEllipsisKeywordStart < this.mTransformed.length()) {
            int len = this.mTransformed.length();
            char[] chars = new char[len];
            float[] widths = new float[len];
            char[] ELLIPSIS_NORMAL = new char[]{'…'};
            TextUtils.getChars(transformed, 0, len, chars, 0);
            float tmpwid = this.mTextPaint.measureText(new String(chars, this.mEllipsisKeywordStart, len - this.mEllipsisKeywordStart));
            float ellipsWid = this.mTextPaint.measureText(ELLIPSIS_NORMAL, 0, 1);
            if (this.mEllipsisKeywordStart == 0) {
                effectiveEllipsize = TruncateAt.END;
            } else if (tmpwid + ellipsWid <= ((float) ellipsisWidth)) {
                effectiveEllipsize = TruncateAt.START;
            } else {
                int sum = 0;
                float ret = this.mTextPaint.getTextRunAdvances(chars, 0, len, 0, len, false, widths, 0);
                int i = 0;
                while (i < this.mEllipsisKeywordStart) {
                    sum = (int) (((float) sum) + widths[i]);
                    if (ellipsWid <= ((float) sum)) {
                        break;
                    }
                    i++;
                }
                if (i < this.mEllipsisKeywordStart) {
                    chars[0] = ELLIPSIS_NORMAL[0];
                    for (i = 1; i < this.mEllipsisKeywordStart; i++) {
                        chars[i] = '﻿';
                    }
                    String str = new String(chars, 0, len);
                    if (this.mTransformed instanceof Spanned) {
                        SpannableString ss = new SpannableString(str);
                        TextUtils.copySpansFrom((Spanned) this.mTransformed, 0, len, Object.class, ss, 0);
                        transformed = ss;
                    } else {
                        Object transformed2 = str;
                    }
                    effectiveEllipsize = TruncateAt.END;
                } else {
                    effectiveEllipsize = TruncateAt.END;
                }
            }
        }
        if (this.mText instanceof Spannable) {
            TruncateAt truncateAt;
            CharSequence charSequence = this.mText;
            TextPaint textPaint = this.mTextPaint;
            TextDirectionHeuristic textDirectionHeuristic = this.mTextDir;
            float f = this.mSpacingMult;
            float f2 = this.mSpacingAdd;
            boolean z = this.mIncludePad;
            int i2 = this.mBreakStrategy;
            int i3 = this.mHyphenationFrequency;
            if (getKeyListener() == null) {
                truncateAt = effectiveEllipsize;
            } else {
                truncateAt = null;
            }
            result = new DynamicLayout(charSequence, transformed, textPaint, wantWidth, alignment, textDirectionHeuristic, f, f2, z, i2, i3, truncateAt, ellipsisWidth);
        } else {
            if (boring == UNKNOWN_BORING) {
                boring = BoringLayout.isBoring(transformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring != null) {
                    this.mBoring = boring;
                }
            }
            if (boring != null) {
                if (boring.width <= wantWidth && (effectiveEllipsize == null || boring.width <= ellipsisWidth)) {
                    if (!useSaved || this.mSavedLayout == null) {
                        result = BoringLayout.make(transformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring, this.mIncludePad);
                    } else {
                        result = this.mSavedLayout.replaceOrMake(transformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring, this.mIncludePad);
                    }
                    if (useSaved) {
                        this.mSavedLayout = (BoringLayout) result;
                    }
                } else if (shouldEllipsize && boring.width <= wantWidth) {
                    if (!useSaved || this.mSavedLayout == null) {
                        result = BoringLayout.make(transformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                    } else {
                        result = this.mSavedLayout.replaceOrMake(transformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                    }
                }
            }
            result = null;
        }
        if (result != null) {
            return result;
        }
        Builder builder = Builder.obtain(transformed, 0, transformed.length(), this.mTextPaint, wantWidth).setAlignment(alignment).setTextDirection(this.mTextDir).setLineSpacing(this.mSpacingAdd, this.mSpacingMult).setIncludePad(this.mIncludePad).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency);
        if (shouldEllipsize) {
            builder.setEllipsize(effectiveEllipsize).setEllipsizedWidth(ellipsisWidth).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE);
        }
        return builder.build();
    }

    private boolean compressText(float width) {
        if (isHardwareAccelerated() || width <= 0.0f || this.mLayout == null || getLineCount() != 1 || this.mUserSetTextScaleX || this.mTextPaint.getTextScaleX() != 1.0f) {
            return false;
        }
        float overflow = ((this.mLayout.getLineWidth(0) + 1.0f) - width) / width;
        if (overflow <= 0.0f || overflow > 0.07f) {
            return false;
        }
        this.mTextPaint.setTextScaleX((1.0f - overflow) - 0.005f);
        post(new Runnable() {
            public void run() {
                TextView.this.requestLayout();
            }
        });
        return true;
    }

    private static int desired(Layout layout) {
        int i;
        int n = layout.getLineCount();
        CharSequence text = layout.getText();
        float max = 0.0f;
        for (i = 0; i < n - 1; i++) {
            if (text.charAt(layout.getLineEnd(i) - 1) != '\n') {
                return -1;
            }
        }
        for (i = 0; i < n; i++) {
            max = Math.max(max, layout.getLineWidth(i));
        }
        return (int) Math.ceil((double) max);
    }

    public void setIncludeFontPadding(boolean includepad) {
        if (this.mIncludePad != includepad) {
            this.mIncludePad = includepad;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean getIncludeFontPadding() {
        return this.mIncludePad;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int hintWidth;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Metrics boring = UNKNOWN_BORING;
        Metrics hintBoring = UNKNOWN_BORING;
        if (this.mTextDir == null) {
            this.mTextDir = getTextDirectionHeuristic();
        }
        int des = -1;
        boolean fromexisting = false;
        if (widthMode == 1073741824) {
            width = widthSize;
        } else {
            if (this.mLayout != null && this.mEllipsize == null) {
                des = desired(this.mLayout);
            }
            if (des < 0) {
                boring = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring != null) {
                    this.mBoring = boring;
                }
            } else {
                fromexisting = true;
            }
            if (boring == null || boring == UNKNOWN_BORING) {
                if (des < 0) {
                    des = (int) Math.ceil((double) Layout.getDesiredWidth(this.mTransformed, this.mTextPaint));
                }
                width = des;
            } else {
                width = boring.width;
            }
            if (isHighContrastTextEnabled()) {
                width += (int) Math.ceil((double) this.mTextPaint.getHCTStrokeWidth());
            }
            Drawables dr = this.mDrawables;
            if (dr != null) {
                width = Math.max(Math.max(width, dr.mDrawableWidthTop), dr.mDrawableWidthBottom);
            }
            if (this.mHint != null) {
                int hintDes = -1;
                if (this.mHintLayout != null && this.mEllipsize == null) {
                    hintDes = desired(this.mHintLayout);
                }
                if (hintDes < 0) {
                    hintBoring = BoringLayout.isBoring(this.mHint, this.mTextPaint, this.mTextDir, this.mHintBoring);
                    if (hintBoring != null) {
                        this.mHintBoring = hintBoring;
                    }
                }
                if (hintBoring == null || hintBoring == UNKNOWN_BORING) {
                    if (hintDes < 0) {
                        hintDes = (int) Math.ceil((double) Layout.getDesiredWidth(this.mHint, this.mTextPaint));
                    }
                    hintWidth = hintDes;
                } else {
                    hintWidth = hintBoring.width;
                }
                if (hintWidth > width) {
                    width = hintWidth;
                }
            }
            width += getCompoundPaddingLeft() + getCompoundPaddingRight();
            if (this.mMaxWidthMode == 1) {
                width = Math.min(width, this.mMaxWidth * getLineHeight());
            } else {
                width = Math.min(width, this.mMaxWidth);
            }
            if (this.mMinWidthMode == 1) {
                width = Math.max(width, this.mMinWidth * getLineHeight());
            } else {
                width = Math.max(width, this.mMinWidth);
            }
            width = Math.max(width, getSuggestedMinimumWidth());
            if (widthMode == Integer.MIN_VALUE) {
                width = Math.min(widthSize, width);
            }
        }
        int want = (width - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int unpaddedWidth = want;
        if (this.mHorizontallyScrolling) {
            want = 1048576;
        }
        int hintWant = want;
        hintWidth = this.mHintLayout == null ? hintWant : this.mHintLayout.getWidth();
        if (this.mLayout == null) {
            makeNewLayout(want, hintWant, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
        } else {
            boolean layoutChanged = (this.mLayout.getWidth() == want && hintWidth == hintWant && this.mLayout.getEllipsizedWidth() == (width - getCompoundPaddingLeft()) - getCompoundPaddingRight()) ? false : true;
            boolean widthChanged = this.mHint == null && this.mEllipsize == null && want > this.mLayout.getWidth() && ((this.mLayout instanceof BoringLayout) || (fromexisting && des >= 0 && des <= want));
            boolean maximumChanged = (this.mMaxMode == this.mOldMaxMode && this.mMaximum == this.mOldMaximum) ? false : true;
            if (layoutChanged || maximumChanged) {
                if (maximumChanged || !widthChanged) {
                    makeNewLayout(want, hintWant, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                } else {
                    this.mLayout.increaseWidthTo(want);
                }
            }
        }
        if (heightMode == 1073741824) {
            height = heightSize;
            this.mDesiredHeightAtMeasure = -1;
        } else {
            int desired = getDesiredHeight();
            height = desired;
            this.mDesiredHeightAtMeasure = desired;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(desired, heightSize);
            }
            if (isHighContrastTextEnabled()) {
                height += (int) Math.ceil((double) this.mTextPaint.getHCTStrokeWidth());
            }
        }
        int unpaddedHeight = (height - getCompoundPaddingTop()) - getCompoundPaddingBottom();
        if (this.mMaxMode == 1 && this.mLayout.getLineCount() > this.mMaximum) {
            unpaddedHeight = Math.min(unpaddedHeight, this.mLayout.getLineTop(this.mMaximum));
        }
        if (this.mMovement != null || this.mLayout.getWidth() > unpaddedWidth || this.mLayout.getHeight() > unpaddedHeight) {
            registerForPreDraw();
        } else {
            scrollTo(0, 0);
        }
        if (this.mhasMultiSelection) {
            CharSequence text = getTextForMultiSelection();
            int[] selectRange = new int[2];
            boolean flag = getVisibleTextRange(selectRange);
            if (text != null && flag) {
                int[] multiSelStart = MultiSelection.getMultiSelectionStart((Spannable) text);
                int[] multiSelEnd = MultiSelection.getMultiSelectionEnd((Spannable) text);
                int multiSelCount = MultiSelection.getMultiSelectionCount((Spannable) text);
                int i = 0;
                while (i < multiSelCount) {
                    int newStart = multiSelStart[i];
                    int newEnd = multiSelEnd[i];
                    if (newStart < selectRange[0]) {
                        newStart = selectRange[0];
                    } else if (newStart > selectRange[1]) {
                        newStart = selectRange[1];
                    }
                    if (newEnd < selectRange[0]) {
                        newEnd = selectRange[0];
                    } else if (newEnd > selectRange[1]) {
                        newEnd = selectRange[1];
                    }
                    if (multiSelStart[i] != newStart || multiSelEnd[i] != newEnd) {
                        clearMultiSelection();
                        break;
                    }
                    i++;
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    private int getDesiredHeight() {
        boolean z = true;
        int desiredHeight = getDesiredHeight(this.mLayout, true);
        Layout layout = this.mHintLayout;
        if (this.mEllipsize == null) {
            z = false;
        }
        return Math.max(desiredHeight, getDesiredHeight(layout, z));
    }

    private int getDesiredHeight(Layout layout, boolean cap) {
        if (layout == null) {
            return 0;
        }
        int linecount = layout.getLineCount();
        int pad = getCompoundPaddingTop() + getCompoundPaddingBottom();
        int desired = layout.getLineTop(linecount);
        Drawables dr = this.mDrawables;
        if (dr != null) {
            desired = Math.max(Math.max(desired, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
        }
        desired += pad;
        if (this.mMaxMode != 1) {
            desired = Math.min(desired, this.mMaximum);
        } else if (cap && linecount > this.mMaximum) {
            desired = layout.getLineTop(this.mMaximum);
            if (dr != null) {
                desired = Math.max(Math.max(desired, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
            }
            desired += pad;
            linecount = this.mMaximum;
        }
        if (this.mMinMode != 1) {
            desired = Math.max(desired, this.mMinimum);
        } else if (linecount < this.mMinimum) {
            desired += getLineHeight() * (this.mMinimum - linecount);
        }
        return Math.max(desired, getSuggestedMinimumHeight());
    }

    private void checkForResize() {
        boolean sizeChanged = false;
        if (this.mLayout != null) {
            if (this.mLayoutParams.width == -2) {
                sizeChanged = true;
                invalidate();
            }
            if (this.mLayoutParams.height == -2) {
                if (getDesiredHeight() != getHeight()) {
                    sizeChanged = true;
                }
            } else if (this.mLayoutParams.height == -1 && this.mDesiredHeightAtMeasure >= 0 && getDesiredHeight() != this.mDesiredHeightAtMeasure) {
                sizeChanged = true;
            }
        }
        if (sizeChanged) {
            requestLayout();
        }
    }

    private void checkForRelayout() {
        if ((this.mLayoutParams.width != -2 || (this.mMaxWidthMode == this.mMinWidthMode && this.mMaxWidth == this.mMinWidth)) && ((this.mHint == null || this.mHintLayout != null) && ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight() > 0)) {
            int hintWant;
            int oldht = this.mLayout.getHeight();
            int want = this.mLayout.getWidth();
            if (this.mHintLayout == null) {
                hintWant = 0;
            } else {
                hintWant = this.mHintLayout.getWidth();
            }
            makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
            if (this.mEllipsize != TruncateAt.MARQUEE) {
                if (this.mLayoutParams.height != -2 && this.mLayoutParams.height != -1) {
                    invalidate();
                    return;
                } else if (this.mLayout.getHeight() == oldht && (this.mHintLayout == null || this.mHintLayout.getHeight() == oldht)) {
                    invalidate();
                    return;
                }
            }
            requestLayout();
            invalidate();
            return;
        }
        nullLayouts();
        requestLayout();
        invalidate();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mDeferScroll >= 0) {
            int curs = this.mDeferScroll;
            this.mDeferScroll = -1;
            bringPointIntoView(Math.min(curs, this.mText.length()));
        }
    }

    private boolean isShowingHint() {
        return TextUtils.isEmpty(this.mText) && !TextUtils.isEmpty(this.mHint);
    }

    private boolean bringTextIntoView() {
        int scrollx;
        int scrolly;
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        int line = 0;
        if ((this.mGravity & 112) == 80) {
            line = layout.getLineCount() - 1;
        }
        Alignment a = layout.getParagraphAlignment(line);
        int dir = layout.getParagraphDirection(line);
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int ht = layout.getHeight();
        if (a == Alignment.ALIGN_NORMAL) {
            a = dir == 1 ? Alignment.ALIGN_LEFT : Alignment.ALIGN_RIGHT;
        } else if (a == Alignment.ALIGN_OPPOSITE) {
            a = dir == 1 ? Alignment.ALIGN_RIGHT : Alignment.ALIGN_LEFT;
        }
        if (a == Alignment.ALIGN_CENTER) {
            int left = (int) Math.floor((double) layout.getLineLeft(line));
            int right = (int) Math.ceil((double) layout.getLineRight(line));
            if (right - left < hspace) {
                scrollx = ((right + left) / 2) - (hspace / 2);
            } else if (dir < 0) {
                scrollx = right - hspace;
            } else {
                scrollx = left;
            }
        } else if (a == Alignment.ALIGN_RIGHT) {
            scrollx = ((int) Math.ceil((double) layout.getLineRight(line))) - hspace;
        } else {
            scrollx = (int) Math.floor((double) layout.getLineLeft(line));
        }
        if (ht < vspace) {
            scrolly = 0;
        } else if ((this.mGravity & 112) == 80) {
            scrolly = ht - vspace;
        } else {
            scrolly = 0;
        }
        if (scrollx == this.mScrollX && scrolly == this.mScrollY) {
            return false;
        }
        scrollTo(scrollx, scrolly);
        return true;
    }

    public boolean bringPointIntoView(int offset) {
        if (isLayoutRequested()) {
            this.mDeferScroll = offset;
            return false;
        }
        boolean changed = false;
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        if (layout == null) {
            return false;
        }
        int grav;
        int line = layout.getLineForOffset(offset);
        switch (AnonymousClass8.$SwitchMap$android$text$Layout$Alignment[layout.getParagraphAlignment(line).ordinal()]) {
            case 1:
                grav = 1;
                break;
            case 2:
                grav = -1;
                break;
            case 3:
                grav = layout.getParagraphDirection(line);
                break;
            case 4:
                grav = -layout.getParagraphDirection(line);
                break;
            default:
                grav = 0;
                break;
        }
        int x = (int) layout.getPrimaryHorizontal(offset, grav > 0);
        int top = layout.getLineTop(line);
        int bottom = layout.getLineTop(line + 1);
        int left = (int) Math.floor((double) layout.getLineLeft(line));
        int right = (int) Math.ceil((double) layout.getLineRight(line));
        int ht = layout.getHeight();
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        if (!this.mHorizontallyScrolling && right - left > hspace && right > x) {
            right = Math.max(x, left + hspace);
        }
        int hslack = (bottom - top) / 2;
        int vslack = hslack;
        if (vslack > vspace / 4) {
            vslack = vspace / 4;
        }
        if (hslack > hspace / 4) {
            hslack = hspace / 4;
        }
        int hs = this.mScrollX;
        int vs = this.mScrollY;
        if (top - vs < vslack) {
            vs = top - vslack;
        }
        if (bottom - vs > vspace - vslack) {
            vs = bottom - (vspace - vslack);
        }
        if (ht - vs < vspace) {
            vs = ht - vspace;
        }
        if (0 - vs > 0) {
            vs = 0;
        }
        if (grav != 0) {
            if (x - hs < hslack) {
                hs = x - hslack;
            }
            if (x - hs > hspace - hslack) {
                hs = x - (hspace - hslack);
            }
        }
        if (grav < 0) {
            if (left - hs > 0) {
                hs = left;
            }
            if (right - hs < hspace) {
                hs = right - hspace;
            }
        } else if (grav > 0) {
            if (right - hs < hspace) {
                hs = right - hspace;
            }
            if (left - hs > 0) {
                hs = left;
            }
        } else if (right - left <= hspace) {
            hs = left - ((hspace - (right - left)) / 2);
        } else if (x > right - hslack) {
            hs = right - hspace;
        } else if (x < left + hslack) {
            hs = left;
        } else if (left > hs) {
            hs = left;
        } else if (right < hs + hspace) {
            hs = right - hspace;
        } else {
            if (x - hs < hslack) {
                hs = x - hslack;
            }
            if (x - hs > hspace - hslack) {
                hs = x - (hspace - hslack);
            }
        }
        if (!(hs == this.mScrollX && vs == this.mScrollY)) {
            if (this.mScroller == null) {
                scrollTo(hs, vs);
            } else {
                int dx = hs - this.mScrollX;
                int dy = vs - this.mScrollY;
                if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                    this.mScroller.startScroll(this.mScrollX, this.mScrollY, dx, dy);
                    awakenScrollBars(this.mScroller.getDuration());
                    invalidate();
                } else {
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                    }
                    scrollBy(dx, dy);
                }
                this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
            }
            changed = true;
        }
        if (!isFocused()) {
            return changed;
        }
        if (this.mTempRect == null) {
            this.mTempRect = new Rect();
        }
        this.mTempRect.set(x - 2, top, x + 2, bottom);
        getInterestingRect(this.mTempRect, line);
        this.mTempRect.offset(this.mScrollX, this.mScrollY);
        if (requestRectangleOnScreen(this.mTempRect)) {
            return true;
        }
        return changed;
    }

    public boolean moveCursorToVisibleOffset() {
        if (!(this.mText instanceof Spannable)) {
            return false;
        }
        int start = getSelectionStart();
        if (start != getSelectionEnd()) {
            return false;
        }
        int lowChar;
        int highChar;
        int line = this.mLayout.getLineForOffset(start);
        int top = this.mLayout.getLineTop(line);
        int bottom = this.mLayout.getLineTop(line + 1);
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int vslack = (bottom - top) / 2;
        if (vslack > vspace / 4) {
            vslack = vspace / 4;
        }
        int vs = this.mScrollY;
        if (top < vs + vslack) {
            line = this.mLayout.getLineForVertical((vs + vslack) + (bottom - top));
        } else if (bottom > (vspace + vs) - vslack) {
            line = this.mLayout.getLineForVertical(((vspace + vs) - vslack) - (bottom - top));
        }
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int hs = this.mScrollX;
        int leftChar = this.mLayout.getOffsetForHorizontal(line, (float) hs);
        int rightChar = this.mLayout.getOffsetForHorizontal(line, (float) (hspace + hs));
        if (leftChar < rightChar) {
            lowChar = leftChar;
        } else {
            lowChar = rightChar;
        }
        if (leftChar > rightChar) {
            highChar = leftChar;
        } else {
            highChar = rightChar;
        }
        int newStart = start;
        if (newStart < lowChar) {
            newStart = lowChar;
        } else if (newStart > highChar) {
            newStart = highChar;
        }
        if (newStart == start) {
            return false;
        }
        Selection.setSelection((Spannable) this.mText, newStart);
        return true;
    }

    public void computeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            this.mScrollX = this.mScroller.getCurrX();
            this.mScrollY = this.mScroller.getCurrY();
            invalidateParentCaches();
            postInvalidate();
        }
    }

    private void getInterestingRect(Rect r, int line) {
        convertFromViewportToContentCoordinates(r);
        if (line == 0) {
            r.top -= getExtendedPaddingTop();
        }
        if (line == this.mLayout.getLineCount() - 1) {
            r.bottom += getExtendedPaddingBottom();
        }
    }

    private void convertFromViewportToContentCoordinates(Rect r) {
        int horizontalOffset = viewportToContentHorizontalOffset();
        r.left += horizontalOffset;
        r.right += horizontalOffset;
        int verticalOffset = viewportToContentVerticalOffset();
        r.top += verticalOffset;
        r.bottom += verticalOffset;
    }

    int viewportToContentHorizontalOffset() {
        return getCompoundPaddingLeft() - this.mScrollX;
    }

    int viewportToContentVerticalOffset() {
        int offset = getExtendedPaddingTop() - this.mScrollY;
        if ((this.mGravity & 112) != 48) {
            return offset + getVerticalOffset(false);
        }
        return offset;
    }

    public void debug(int depth) {
        super.debug(depth);
        String output = View.debugIndent(depth) + "frame={" + this.mLeft + ", " + this.mTop + ", " + this.mRight + ", " + this.mBottom + "} scroll={" + this.mScrollX + ", " + this.mScrollY + "} ";
        if (this.mText != null) {
            output = output + "mText=\"" + this.mText + "\" ";
            if (this.mLayout != null) {
                output = output + "mLayout width=" + this.mLayout.getWidth() + " height=" + this.mLayout.getHeight();
            }
            if (Build.IS_SYSTEM_SECURE && this.mStringName != null) {
                output = output + "mStringName = \"" + this.mStringName + "\" ";
            }
        } else {
            output = output + "mText=NULL";
        }
        Log.d("View", output);
    }

    @ExportedProperty(category = "text")
    public int getSelectionStart() {
        return Selection.getSelectionStart(getText());
    }

    @ExportedProperty(category = "text")
    public int getSelectionEnd() {
        return Selection.getSelectionEnd(getText());
    }

    public boolean hasSelection() {
        int selectionStart = getSelectionStart();
        return selectionStart >= 0 && selectionStart != getSelectionEnd();
    }

    String getSelectedText() {
        if (!hasSelection()) {
            return null;
        }
        int start = getSelectionStart();
        int end = getSelectionEnd();
        return String.valueOf(start > end ? this.mText.subSequence(end, start) : this.mText.subSequence(start, end));
    }

    public void setSingleLine() {
        setSingleLine(true);
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    @RemotableViewMethod
    public void setSingleLine(boolean singleLine) {
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, true, true);
    }

    private void setInputTypeSingleLine(boolean singleLine) {
        if (this.mEditor != null && (this.mEditor.mInputType & 15) == 1) {
            Editor editor;
            if (singleLine) {
                editor = this.mEditor;
                editor.mInputType &= -131073;
                return;
            }
            editor = this.mEditor;
            editor.mInputType |= 131072;
        }
    }

    private void applySingleLine(boolean singleLine, boolean applyTransformation, boolean changeMaxLines) {
        this.mSingleLine = singleLine;
        if (singleLine) {
            setLines(1);
            setHorizontallyScrolling(true);
            if (applyTransformation) {
                setTransformationMethod(SingleLineTransformationMethod.getInstance());
                return;
            }
            return;
        }
        if (changeMaxLines) {
            setMaxLines(Integer.MAX_VALUE);
        }
        setHorizontallyScrolling(false);
        if (applyTransformation) {
            setTransformationMethod(null);
        }
    }

    public void setEllipsize(TruncateAt where) {
        if (this.mEllipsize == where) {
            return;
        }
        if (where == TruncateAt.KEYWORD) {
            this.mEllipsisKeywordStart = -1;
            this.mEllipsisKeywordCount = -1;
            return;
        }
        this.mEllipsize = where;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public void setEllipsize(TruncateAt where, int keywordStart, int keywordCount) {
        if (this.mEllipsize != where && keywordStart >= 0) {
            this.mEllipsize = where;
            this.mEllipsisKeywordStart = keywordStart;
            this.mEllipsisKeywordCount = keywordCount;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setMarqueeRepeatLimit(int marqueeLimit) {
        this.mMarqueeRepeatLimit = marqueeLimit;
    }

    public int getMarqueeRepeatLimit() {
        return this.mMarqueeRepeatLimit;
    }

    @ExportedProperty
    public TruncateAt getEllipsize() {
        return this.mEllipsize;
    }

    @RemotableViewMethod
    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        createEditorIfNeeded();
        this.mEditor.mSelectAllOnFocus = selectAllOnFocus;
        if (selectAllOnFocus && !(this.mText instanceof Spannable)) {
            setText(this.mText, BufferType.SPANNABLE);
        }
    }

    @RemotableViewMethod
    public void setCursorVisible(boolean visible) {
        if (!visible || this.mEditor != null) {
            createEditorIfNeeded();
            if (this.mEditor.mCursorVisible != visible) {
                this.mEditor.mCursorVisible = visible;
                invalidate();
                this.mEditor.makeBlink();
                this.mEditor.prepareCursorControllers();
                this.mEditor.stopTextActionMode();
            }
        }
    }

    public boolean isCursorVisible() {
        return this.mEditor == null ? true : this.mEditor.mCursorVisible;
    }

    private boolean canMarquee() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width <= 0) {
            return false;
        }
        if (this.mLayout.getLineWidth(0) > ((float) width) || (this.mMarqueeFadeMode != 0 && this.mSavedMarqueeModeLayout != null && this.mSavedMarqueeModeLayout.getLineWidth(0) > ((float) width))) {
            return true;
        }
        return false;
    }

    private void startMarquee() {
        if (getKeyListener() != null || compressText((float) ((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight()))) {
            return;
        }
        if (this.mMarquee != null && !this.mMarquee.isStopped()) {
            return;
        }
        if ((isFocused() || isSelected()) && getLineCount() == 1 && canMarquee()) {
            if (this.mMarqueeFadeMode == 1) {
                this.mMarqueeFadeMode = 2;
                Layout tmp = this.mLayout;
                this.mLayout = this.mSavedMarqueeModeLayout;
                this.mSavedMarqueeModeLayout = tmp;
                setHorizontalFadingEdgeEnabled(true);
                requestLayout();
                invalidate();
            }
            if (this.mMarquee == null) {
                this.mMarquee = new Marquee(this);
            }
            this.mMarquee.start(this.mMarqueeRepeatLimit);
        }
    }

    private void stopMarquee() {
        if (!(this.mMarquee == null || this.mMarquee.isStopped())) {
            this.mMarquee.stop();
        }
        if (this.mMarqueeFadeMode == 2) {
            this.mMarqueeFadeMode = 1;
            Layout tmp = this.mSavedMarqueeModeLayout;
            this.mSavedMarqueeModeLayout = this.mLayout;
            this.mLayout = tmp;
            setHorizontalFadingEdgeEnabled(false);
            requestLayout();
            invalidate();
        }
    }

    private void startStopMarquee(boolean start) {
        if (this.mEllipsize != TruncateAt.MARQUEE) {
            return;
        }
        if (start) {
            startMarquee();
        } else {
            stopMarquee();
        }
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    protected void onSelectionChanged(int selStart, int selEnd) {
        sendAccessibilityEvent(8192);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        if (this.mListeners != null) {
            int i = this.mListeners.indexOf(watcher);
            if (i >= 0) {
                this.mListeners.remove(i);
            }
        }
    }

    private void sendBeforeTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ((TextWatcher) list.get(i)).beforeTextChanged(text, start, before, after);
            }
        }
        removeIntersectingNonAdjacentSpans(start, start + before, SpellCheckSpan.class);
        removeIntersectingNonAdjacentSpans(start, start + before, SuggestionSpan.class);
    }

    private <T> void removeIntersectingNonAdjacentSpans(int start, int end, Class<T> type) {
        if (this.mText instanceof Editable) {
            Editable text = this.mText;
            T[] spans = text.getSpans(start, end, type);
            int length = spans.length;
            int i = 0;
            while (i < length) {
                int spanStart = text.getSpanStart(spans[i]);
                if (text.getSpanEnd(spans[i]) != start && spanStart != end) {
                    text.removeSpan(spans[i]);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    void removeAdjacentSuggestionSpans(int pos) {
        if (this.mText instanceof Editable) {
            Editable text = this.mText;
            SuggestionSpan[] spans = (SuggestionSpan[]) text.getSpans(pos, pos, SuggestionSpan.class);
            int length = spans.length;
            for (int i = 0; i < length; i++) {
                int spanStart = text.getSpanStart(spans[i]);
                int spanEnd = text.getSpanEnd(spans[i]);
                if ((spanEnd == pos || spanStart == pos) && SpellChecker.haveWordBoundariesChanged(text, pos, pos, spanStart, spanEnd)) {
                    text.removeSpan(spans[i]);
                }
            }
        }
    }

    void sendOnTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                if (i < list.size()) {
                    ((TextWatcher) list.get(i)).onTextChanged(text, start, before, after);
                }
            }
        }
        clearMultiSelection();
        if (this.mUseDisplayText && !this.mSkipUpdateDisplayText) {
            this.mDisplayText = new SpannableStringBuilder(this.mTransformed);
            Spannable sp = this.mDisplayText;
            if (this.mChangeWatcher == null) {
                this.mChangeWatcher = new ChangeWatcher();
            }
            sp.setSpan(this.mChangeWatcher, 0, this.mDisplayText.length(), 6553618);
        }
        this.mSkipUpdateDisplayText = false;
        if (this.mEditor != null) {
            this.mEditor.sendOnTextChanged(start, after);
        }
    }

    void sendAfterTextChanged(Editable text) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ((TextWatcher) list.get(i)).afterTextChanged(text);
            }
        }
        hideErrorIfUnchanged();
    }

    void updateAfterEdit() {
        invalidate();
        int curs = getSelectionStart();
        if (curs >= 0 || (this.mGravity & 112) == 80) {
            registerForPreDraw();
        }
        checkForResize();
        if (curs >= 0) {
            this.mHighlightPathBogus = true;
            if (this.mEditor != null) {
                this.mEditor.makeBlink();
            }
            bringPointIntoView(curs);
        }
    }

    void handleTextChanged(CharSequence buffer, int start, int before, int after) {
        sLastCutCopyOrTextChangedTime = 0;
        InputMethodState ims = this.mEditor == null ? null : this.mEditor.mInputMethodState;
        if (ims == null || ims.mBatchEditNesting == 0) {
            updateAfterEdit();
        }
        if (ims != null) {
            ims.mContentChanged = true;
            if (ims.mChangedStart < 0) {
                ims.mChangedStart = start;
                ims.mChangedEnd = start + before;
            } else {
                ims.mChangedStart = Math.min(ims.mChangedStart, start);
                ims.mChangedEnd = Math.max(ims.mChangedEnd, (start + before) - ims.mChangedDelta);
            }
            ims.mChangedDelta += after - before;
        }
        resetErrorChangedFlag();
        sendOnTextChanged(buffer, start, before, after);
        onTextChanged(buffer, start, before, after);
    }

    void spanChange(Spanned buf, Object what, int oldStart, int newStart, int oldEnd, int newEnd) {
        InputMethodState ims;
        boolean selChanged = false;
        int newSelStart = -1;
        int newSelEnd = -1;
        if (this.mEditor == null) {
            ims = null;
        } else {
            ims = this.mEditor.mInputMethodState;
        }
        boolean multiSelChanged = false;
        if (what == MultiSelection.CURRENT_SELECTION_END) {
            multiSelChanged = true;
            newSelEnd = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(MultiSelection.getSelectionStart(buf), oldStart, newStart);
                checkForResize();
                registerForPreDraw();
                if (this.mEditor != null) {
                    this.mEditor.makeBlink();
                }
            }
        }
        if (what == MultiSelection.CURRENT_SELECTION_START) {
            multiSelChanged = true;
            newSelStart = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(MultiSelection.getSelectionEnd(buf), oldStart, newStart);
            }
        }
        if (multiSelChanged) {
            this.mHighlightPathBogus = true;
            if (!(this.mEditor == null || isFocused())) {
                this.mEditor.mSelectionMoved = true;
            }
        }
        if (what == Selection.SELECTION_END) {
            selChanged = true;
            newSelEnd = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(Selection.getSelectionStart(buf), oldStart, newStart);
                checkForResize();
                registerForPreDraw();
                if (this.mEditor != null) {
                    this.mEditor.makeBlink();
                }
            }
        }
        if (what == Selection.SELECTION_START) {
            selChanged = true;
            newSelStart = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(Selection.getSelectionEnd(buf), oldStart, newStart);
            }
        }
        if (selChanged) {
            this.mHighlightPathBogus = true;
            if (!(this.mEditor == null || isFocused())) {
                this.mEditor.mSelectionMoved = true;
            }
            if ((buf.getSpanFlags(what) & 512) == 0) {
                if (newSelStart < 0) {
                    newSelStart = Selection.getSelectionStart(buf);
                }
                if (newSelEnd < 0) {
                    newSelEnd = Selection.getSelectionEnd(buf);
                }
                onSelectionChanged(newSelStart, newSelEnd);
            }
        }
        if ((what instanceof UpdateAppearance) || (what instanceof ParagraphStyle) || (what instanceof CharacterStyle)) {
            if (ims == null || ims.mBatchEditNesting == 0) {
                invalidate();
                this.mHighlightPathBogus = true;
                checkForResize();
            } else {
                ims.mContentChanged = true;
            }
            if (this.mEditor != null) {
                if (oldStart >= 0) {
                    this.mEditor.invalidateTextDisplayList(this.mLayout, oldStart, oldEnd);
                }
                if (newStart >= 0) {
                    this.mEditor.invalidateTextDisplayList(this.mLayout, newStart, newEnd);
                }
            }
        }
        if (MetaKeyKeyListener.isMetaTracker(buf, what)) {
            this.mHighlightPathBogus = true;
            if (ims != null && MetaKeyKeyListener.isSelectingMetaTracker(buf, what)) {
                ims.mSelectionModeChanged = true;
            }
            if (Selection.getSelectionStart(buf) >= 0) {
                if (ims == null || ims.mBatchEditNesting == 0) {
                    invalidateCursor();
                } else {
                    ims.mCursorChanged = true;
                }
            }
        }
        if (!(!(what instanceof ParcelableSpan) || ims == null || ims.mExtractedTextRequest == null)) {
            if (ims.mBatchEditNesting != 0) {
                if (oldStart >= 0) {
                    if (ims.mChangedStart > oldStart) {
                        ims.mChangedStart = oldStart;
                    }
                    if (ims.mChangedStart > oldEnd) {
                        ims.mChangedStart = oldEnd;
                    }
                }
                if (newStart >= 0) {
                    if (ims.mChangedStart > newStart) {
                        ims.mChangedStart = newStart;
                    }
                    if (ims.mChangedStart > newEnd) {
                        ims.mChangedStart = newEnd;
                    }
                }
            } else {
                ims.mContentChanged = true;
            }
        }
        if (this.mEditor != null && this.mEditor.mSpellChecker != null && newStart < 0 && (what instanceof SpellCheckSpan)) {
            this.mEditor.mSpellChecker.onSpellCheckSpanRemoved((SpellCheckSpan) what);
        }
    }

    public void dispatchFinishTemporaryDetach() {
        this.mDispatchTemporaryDetach = true;
        super.dispatchFinishTemporaryDetach();
        this.mDispatchTemporaryDetach = false;
    }

    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (!this.mDispatchTemporaryDetach) {
            this.mTemporaryDetach = true;
        }
        if (this.mEditor != null) {
            this.mEditor.mTemporaryDetach = true;
        }
    }

    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (!this.mDispatchTemporaryDetach) {
            this.mTemporaryDetach = false;
        }
        if (this.mEditor != null) {
            this.mEditor.mTemporaryDetach = false;
        }
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (this.mTemporaryDetach) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            return;
        }
        if (this.mEditor != null) {
            this.mEditor.onFocusChanged(focused, direction);
        }
        if (focused && (this.mText instanceof Spannable)) {
            MetaKeyKeyListener.resetMetaState(this.mText);
        }
        startStopMarquee(focused);
        if (this.mTransformation != null) {
            this.mTransformation.onFocusChanged(this, this.mText, focused, direction, previouslyFocusedRect);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.mEditor != null) {
            this.mEditor.onWindowFocusChanged(hasWindowFocus);
        }
        startStopMarquee(hasWindowFocus);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (!(this.mEditor == null || visibility == 0)) {
            this.mEditor.hideCursorAndSpanControllers();
            stopTextActionMode();
        }
        if (visibility != 0) {
            clearMultiSelection();
        }
    }

    public void clearComposingText() {
        if (this.mText instanceof Spannable) {
            BaseInputConnection.removeComposingSpans((Spannable) this.mText);
        }
    }

    @RemotableViewMethod
    public void setSelected(int value) {
        boolean z = true;
        if (1 != value) {
            z = false;
        }
        setSelected(z);
    }

    public void setSelected(boolean selected) {
        boolean wasSelected = isSelected();
        super.setSelected(selected);
        if (selected != wasSelected && this.mEllipsize == TruncateAt.MARQUEE) {
            if (selected) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    public boolean onHoverEvent(MotionEvent event) {
        CharSequence text = getTextForMultiSelection();
        mLastHoveredView = this;
        mLastHoveredTime = SystemClock.uptimeMillis();
        if (text != null && sSpenUspLevel >= 3) {
            boolean isBtnPressed = (event.getButtonState() & 32) != 0;
            int action = event.getActionMasked();
            this.mEnableLinkPreview = isLinkPreviewEnabled(event.getToolType(0));
            if (action == 9) {
                this.mCanTextMultiSelection = canTextMultiSelection();
            }
            if (action == 10) {
                if (this.mHoveredSpan != null) {
                    try {
                        new HermesServiceManager(getContext()).dismissHermes();
                    } catch (IllegalStateException e) {
                        Log.d(LOG_TAG, "** skip HERMES Service by IllegalStateException, onHoverExit **");
                    }
                }
                this.mHoveredSpan = null;
                this.mHoverEnterTime = -1;
                this.mHoverExitTime = -1;
                if (MultiSelection.isTextViewHovered()) {
                    MultiSelection.setTextViewHovered(false);
                    try {
                        PointerIcon.setHoveringSpenIcon(1, -1);
                    } catch (RemoteException e2) {
                        Log.d(LOG_TAG, "Failed to change Pen Point to HOVERING_SPENICON_HOVERPOPUP_DEFAULT");
                    }
                }
                return super.onHoverEvent(event);
            } else if (!this.mEnableLinkPreview && !isBtnPressed) {
                return super.onHoverEvent(event);
            } else {
                if (action == 7) {
                    Layout layout = getLayout();
                    if (layout == null) {
                        return super.onHoverEvent(event);
                    }
                    int rawX = (int) event.getRawX();
                    int rawY = (int) event.getRawY();
                    if (isBtnPressed && this.mCanTextMultiSelection) {
                        if (checkPosOnText(rawX, rawY, (int) this.TOUCH_DELTA)) {
                            if (MultiSelection.getHoveredIcon() != 2) {
                                post(new Runnable() {
                                    public void run() {
                                        try {
                                            MultiSelection.setTextViewHovered(true, 2);
                                            PointerIcon.setHoveringSpenIcon(2, -1);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                try {
                                    if (this.mHoveredSpan != null) {
                                        new HermesServiceManager(getContext()).dismissHermes();
                                    }
                                    this.mHoveredSpan = null;
                                    this.mHoverEnterTime = -1;
                                    this.mHoverExitTime = -1;
                                } catch (IllegalStateException e3) {
                                    Log.d(LOG_TAG, "** hovering dismiss **");
                                }
                            }
                            return super.onHoverEvent(event);
                        }
                    }
                    if (MultiSelection.getHoveredIcon() == 2) {
                        MultiSelection.setTextViewHovered(false);
                        try {
                            PointerIcon.setHoveringSpenIcon(1, -1);
                        } catch (RemoteException e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (((URLSpan[]) ((Spannable) text).getSpans(0, text.length(), URLSpan.class)).length != 0) {
                        int x = (((int) event.getX()) - getTotalPaddingLeft()) + getScrollX();
                        int curLine = layout.getLineForVertical((((int) event.getY()) - getTotalPaddingTop()) + getScrollY());
                        int off = layout.getOffsetForHorizontal(curLine, (float) x);
                        URLSpan[] link = (URLSpan[]) ((Spannable) text).getSpans(off, off, URLSpan.class);
                        if (link.length != 0) {
                            this.doShowingHermes = true;
                            int start = ((Spannable) text).getSpanStart(link[0]);
                            int end = ((Spannable) text).getSpanEnd(link[0]);
                            String url = text.subSequence(start, end).toString();
                            if (emailPattern.matcher(url).find()) {
                                return super.onHoverEvent(event);
                            }
                            if (!urlPattern.matcher(url).find()) {
                                return super.onHoverEvent(event);
                            }
                            int spanStartX;
                            int sLine = layout.getLineForOffset(start);
                            int eLine = layout.getLineForOffset(end);
                            if (curLine == sLine) {
                                spanStartX = (int) layout.getPrimaryHorizontal(start);
                            } else {
                                spanStartX = (int) layout.getPrimaryHorizontal(0);
                            }
                            int spanEndX;
                            if (curLine == eLine) {
                                spanEndX = (int) layout.getPrimaryHorizontal(end);
                            } else {
                                spanEndX = (int) layout.getLineWidth(curLine);
                            }
                            if (spanStartX > x || x > spanEndX) {
                                this.mHoverExitTime = -1;
                            } else {
                                if (this.mHoveredSpan != link[0]) {
                                    if (this.mHoverEnterTime <= 0) {
                                        this.mHoverEnterTime = System.currentTimeMillis();
                                        post(new Runnable() {
                                            public void run() {
                                                MultiSelection.setTextViewHovered(true, 10);
                                                try {
                                                    PointerIcon.setHoveringSpenIcon(10, -1);
                                                } catch (RemoteException e) {
                                                    Log.d(TextView.LOG_TAG, "Failed to change Pen Point to HOVERING_SPENICON_MORE");
                                                }
                                            }
                                        });
                                    } else if (System.currentTimeMillis() - this.mHoverEnterTime > 300) {
                                        HermesServiceManager hermesManager = null;
                                        try {
                                            hermesManager = new HermesServiceManager(getContext());
                                        } catch (IllegalStateException e5) {
                                            Log.d(LOG_TAG, "** skip HERMES Service by IllegalStateException, onHover : URLSpan1 **");
                                        }
                                        if (!(this.mHoveredSpan == null || hermesManager == null)) {
                                            hermesManager.dismissHermes();
                                        }
                                        this.mHoveredSpan = link[0];
                                        this.mHoverEnterTime = -1;
                                        if (hermesManager != null) {
                                            PointF scaleRatio;
                                            int topPadding = getTotalPaddingTop();
                                            int top = layout.getLineTop(sLine) + topPadding;
                                            int bottom = layout.getLineBottom(eLine) + topPadding;
                                            PointF pointF = new PointF(1.0f, 1.0f);
                                            ViewRootImpl viewRootImpl = null;
                                            if (isScaleWindow() && viewRootImpl != null) {
                                                scaleRatio = viewRootImpl.getMultiWindowScale();
                                            }
                                            rawX = (int) event.getRawXForScaledWindow();
                                            rawY = (int) event.getRawYForScaledWindow();
                                            top = (int) (((float) rawY) - ((event.getY() - ((float) top)) * scaleRatio.y));
                                            bottom = (int) (((float) rawY) + ((((float) bottom) - event.getY()) * scaleRatio.y));
                                            Rect rect = new Rect();
                                            rect.set(rawX, top, rawX + 1, bottom);
                                            if (!TextUtils.isEmpty(url)) {
                                                hermesManager.showHermes(url, rect, 1);
                                                this.doShowingHermes = false;
                                            }
                                        }
                                    }
                                }
                            }
                            return super.onHoverEvent(event);
                        } else if (this.doShowingHermes) {
                            MultiSelection.setTextViewHovered(false);
                            try {
                                PointerIcon.setHoveringSpenIcon(1, -1);
                            } catch (RemoteException e6) {
                                Log.d(LOG_TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
                            }
                            this.doShowingHermes = false;
                        }
                    }
                    if (this.mHoveredSpan != null) {
                        if (this.mHoverExitTime <= 0) {
                            this.mHoverExitTime = System.currentTimeMillis();
                            MultiSelection.setTextViewHovered(false);
                            try {
                                PointerIcon.setHoveringSpenIcon(1, -1);
                            } catch (RemoteException e7) {
                                Log.d(LOG_TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
                            }
                        } else if (System.currentTimeMillis() - this.mHoverExitTime > 300) {
                            try {
                                new HermesServiceManager(getContext()).dismissHermes();
                            } catch (IllegalStateException e8) {
                                Log.d(LOG_TAG, "** skip HERMES Service by IllegalStateException, onHover : hover exit **");
                            }
                            this.mHoveredSpan = null;
                            this.mHoverEnterTime = -1;
                            this.mHoverExitTime = -1;
                        }
                        return super.onHoverEvent(event);
                    }
                }
            }
        }
        return super.onHoverEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int i = 0;
        int action = event.getActionMasked();
        if (this.mEditor != null && action == 0) {
            if (!this.mFirstTouch || SystemClock.uptimeMillis() - this.mLastTouchUpTime > ((long) ViewConfiguration.getDoubleTapTimeout())) {
                this.mEditor.mDiscardNextActionUp = false;
                this.mEditor.mDoubleTap = false;
                this.mFirstTouch = true;
            } else {
                this.mEditor.mDoubleTap = true;
                this.mFirstTouch = false;
            }
        }
        if (action == 1) {
            this.mLastTouchUpTime = SystemClock.uptimeMillis();
        }
        if (this.mEditor != null) {
            this.mEditor.onTouchEvent(event);
            if (this.mEditor.mSelectionModifierCursorController != null && this.mEditor.mSelectionModifierCursorController.isDragAcceleratorActive()) {
                return true;
            }
        }
        boolean superResult = super.onTouchEvent(event);
        if (this.mEditor != null && this.mEditor.mDiscardNextActionUp && action == 1) {
            this.mEditor.mDiscardNextActionUp = false;
            if (this.mEditor.mIsInsertionActionModeStartPending) {
                this.mEditor.startInsertionActionMode();
                this.mEditor.mIsInsertionActionModeStartPending = false;
            }
            return superResult;
        }
        boolean touchIsFinished;
        if (action != 1 || ((this.mEditor != null && this.mEditor.mIgnoreActionUpEvent) || !isFocused())) {
            touchIsFinished = false;
        } else {
            touchIsFinished = true;
        }
        if ((this.mMovement != null || onCheckIsTextEditor()) && isEnabled() && (this.mText instanceof Spannable) && this.mLayout != null) {
            boolean handled = false;
            if (this.mMovement != null) {
                handled = false | this.mMovement.onTouchEvent(this, (Spannable) this.mText, event);
            }
            boolean textIsSelectable = isTextSelectable();
            if (touchIsFinished && this.mLinksClickable && this.mAutoLinkMask != 0 && textIsSelectable) {
                ClickableSpan[] links = (ClickableSpan[]) ((Spannable) this.mText).getSpans(getSelectionStart(), getSelectionEnd(), ClickableSpan.class);
                if (links.length > 0) {
                    links[0].onClick(this);
                    handled = true;
                }
            }
            if (touchIsFinished && (isTextEditable() || textIsSelectable)) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                viewClicked(imm);
                if (!textIsSelectable && this.mEditor.mShowSoftInputOnFocus) {
                    if (imm != null && imm.showSoftInput(this, 0)) {
                        i = 1;
                    }
                    handled |= i;
                }
                this.mEditor.onTouchUpEvent(event);
                handled = true;
            }
            if (handled) {
                return true;
            }
        }
        return superResult;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (!(this.mMovement == null || !(this.mText instanceof Spannable) || this.mLayout == null)) {
            try {
                if (this.mMovement.onGenericMotionEvent(this, (Spannable) this.mText, event)) {
                    return true;
                }
            } catch (AbstractMethodError e) {
            }
        }
        return super.onGenericMotionEvent(event);
    }

    boolean isTextEditable() {
        return (this.mText instanceof Editable) && onCheckIsTextEditor() && isEnabled();
    }

    public boolean didTouchFocusSelect() {
        return this.mEditor != null && this.mEditor.mTouchFocusSelected;
    }

    public void cancelLongPress() {
        super.cancelLongPress();
        if (this.mEditor != null) {
            this.mEditor.mIgnoreActionUpEvent = true;
        }
    }

    public boolean onTrackballEvent(MotionEvent event) {
        if (this.mMovement == null || !(this.mText instanceof Spannable) || this.mLayout == null || !this.mMovement.onTrackballEvent(this, (Spannable) this.mText, event)) {
            return super.onTrackballEvent(event);
        }
        return true;
    }

    public void setScroller(Scroller s) {
        this.mScroller = s;
    }

    protected float getLeftFadingEdgeStrength() {
        if (this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode != 1) {
            if (this.mMarquee != null && !this.mMarquee.isStopped()) {
                Marquee marquee = this.mMarquee;
                if (marquee.shouldDrawLeftFade()) {
                    return marquee.getScroll() / ((float) getHorizontalFadingEdgeLength());
                }
                return 0.0f;
            } else if (getLineCount() == 1) {
                switch (Gravity.getAbsoluteGravity(this.mGravity, getLayoutDirection()) & 7) {
                    case 1:
                    case 7:
                        if (this.mLayout.getParagraphDirection(0) != 1) {
                            return ((((this.mLayout.getLineRight(0) - ((float) (this.mRight - this.mLeft))) - ((float) getCompoundPaddingLeft())) - ((float) getCompoundPaddingRight())) - this.mLayout.getLineLeft(0)) / ((float) getHorizontalFadingEdgeLength());
                        }
                        return 0.0f;
                    case 3:
                        return 0.0f;
                    case 5:
                        return ((((this.mLayout.getLineRight(0) - ((float) (this.mRight - this.mLeft))) - ((float) getCompoundPaddingLeft())) - ((float) getCompoundPaddingRight())) - this.mLayout.getLineLeft(0)) / ((float) getHorizontalFadingEdgeLength());
                }
            }
        }
        return super.getLeftFadingEdgeStrength();
    }

    protected float getRightFadingEdgeStrength() {
        if (this.mEllipsize == TruncateAt.MARQUEE && this.mMarqueeFadeMode != 1) {
            if (this.mMarquee != null && !this.mMarquee.isStopped()) {
                Marquee marquee = this.mMarquee;
                return (marquee.getMaxFadeScroll() - marquee.getScroll()) / ((float) getHorizontalFadingEdgeLength());
            } else if (getLineCount() == 1) {
                switch (Gravity.getAbsoluteGravity(this.mGravity, getLayoutDirection()) & 7) {
                    case 1:
                    case 7:
                        if (this.mLayout.getParagraphDirection(0) != -1) {
                            return (this.mLayout.getLineWidth(0) - ((float) (((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight()))) / ((float) getHorizontalFadingEdgeLength());
                        }
                        return 0.0f;
                    case 3:
                        return (this.mLayout.getLineWidth(0) - ((float) (((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight()))) / ((float) getHorizontalFadingEdgeLength());
                    case 5:
                        return 0.0f;
                }
            }
        }
        return super.getRightFadingEdgeStrength();
    }

    protected int computeHorizontalScrollRange() {
        if (this.mLayout != null) {
            return (this.mSingleLine && (this.mGravity & 7) == 3) ? (int) this.mLayout.getLineWidth(0) : this.mLayout.getWidth();
        } else {
            return super.computeHorizontalScrollRange();
        }
    }

    protected int computeVerticalScrollRange() {
        if (this.mLayout != null) {
            return this.mLayout.getHeight();
        }
        return super.computeVerticalScrollRange();
    }

    protected int computeVerticalScrollExtent() {
        return (getHeight() - getCompoundPaddingTop()) - getCompoundPaddingBottom();
    }

    public void findViewsWithText(ArrayList<View> outViews, CharSequence searched, int flags) {
        super.findViewsWithText(outViews, searched, flags);
        if (!outViews.contains(this) && (flags & 1) != 0 && !TextUtils.isEmpty(searched) && !TextUtils.isEmpty(this.mText)) {
            if (this.mText.toString().toLowerCase().contains(searched.toString().toLowerCase())) {
                outViews.add(this);
            }
        }
    }

    public static ColorStateList getTextColors(Context context, TypedArray attrs) {
        if (attrs == null) {
            throw new NullPointerException();
        }
        TypedArray a = context.obtainStyledAttributes(android.R.styleable.TextView);
        ColorStateList colors = a.getColorStateList(5);
        if (colors == null) {
            int ap = a.getResourceId(1, 0);
            if (ap != 0) {
                TypedArray appearance = context.obtainStyledAttributes(ap, android.R.styleable.TextAppearance);
                colors = appearance.getColorStateList(3);
                appearance.recycle();
            }
        }
        a.recycle();
        return colors;
    }

    public static int getTextColor(Context context, TypedArray attrs, int def) {
        ColorStateList colors = getTextColors(context, attrs);
        return colors == null ? def : colors.getDefaultColor();
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (!event.hasModifiers(4096)) {
            if (event.hasModifiers(4097)) {
                switch (keyCode) {
                    case 50:
                        if (canPaste()) {
                            return onTextContextMenuItem(16908337);
                        }
                        break;
                    case 54:
                        if (canRedo()) {
                            return onTextContextMenuItem(16908339);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        switch (keyCode) {
            case 29:
                if (canSelectText()) {
                    return onTextContextMenuItem(16908319);
                }
                break;
            case 31:
                if (canCopy()) {
                    return onTextContextMenuItem(16908321);
                }
                break;
            case 32:
                if (canDelete()) {
                    return onTextContextMenuItem(16908376);
                }
                break;
            case 50:
                if (canPaste()) {
                    return onTextContextMenuItem(16908322);
                }
                break;
            case 52:
                if (canCut()) {
                    return onTextContextMenuItem(16908320);
                }
                break;
            case 53:
                if (canRedo()) {
                    return onTextContextMenuItem(16908339);
                }
                break;
            case 54:
                if (canUndo()) {
                    return onTextContextMenuItem(16908338);
                }
                break;
        }
        return super.onKeyShortcut(keyCode, event);
    }

    boolean canSelectText() {
        return (this.mText.length() == 0 || this.mEditor == null || !this.mEditor.hasSelectionController() || isCarMode()) ? false : true;
    }

    boolean textCanBeSelected() {
        if (this.mMovement == null || !this.mMovement.canSelectArbitrarily()) {
            return false;
        }
        if (isTextEditable() || (isTextSelectable() && (this.mText instanceof Spannable) && isEnabled())) {
            return true;
        }
        return false;
    }

    private Locale getTextServicesLocale(boolean allowNullLocale) {
        updateTextServicesLocaleAsync();
        return (this.mCurrentSpellCheckerLocaleCache != null || allowNullLocale) ? this.mCurrentSpellCheckerLocaleCache : Locale.getDefault();
    }

    public Locale getTextServicesLocale() {
        return getTextServicesLocale(false);
    }

    public boolean isInExtractedMode() {
        return false;
    }

    public Locale getSpellCheckerLocale() {
        return getTextServicesLocale(true);
    }

    private void updateTextServicesLocaleAsync() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                TextView.this.updateTextServicesLocaleLocked();
            }
        });
    }

    private void updateTextServicesLocaleLocked() {
        Locale locale;
        SpellCheckerSubtype subtype = ((TextServicesManager) this.mContext.getSystemService("textservices")).getCurrentSpellCheckerSubtype(true);
        if (subtype != null) {
            locale = SpellCheckerSubtype.constructLocaleFromString(subtype.getLocale());
        } else {
            locale = null;
        }
        this.mCurrentSpellCheckerLocaleCache = locale;
    }

    void onLocaleChanged() {
        this.mEditor.mWordIterator = null;
    }

    public WordIterator getWordIterator() {
        if (this.mEditor != null) {
            return this.mEditor.getWordIterator();
        }
        return null;
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        if (!hasPasswordTransformationMethod() || shouldSpeakPasswordsForAccessibility()) {
            CharSequence text = getTextForAccessibility();
            if (!TextUtils.isEmpty(text)) {
                event.getText().add(text);
            }
        }
    }

    private boolean shouldSpeakPasswordsForAccessibility() {
        return Secure.getIntForUser(this.mContext.getContentResolver(), "speak_password", 0, -3) == 1;
    }

    public CharSequence getAccessibilityClassName() {
        return TextView.class.getName();
    }

    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
        boolean isPassword = hasPasswordTransformationMethod() || isPasswordInputType(getInputType());
        if (!isPassword) {
            if (this.mLayout == null) {
                assumeLayout();
            }
            Layout layout = this.mLayout;
            int lineCount = layout.getLineCount();
            if (lineCount <= 1) {
                structure.setText(getText(), getSelectionStart(), getSelectionEnd());
            } else {
                int topLine;
                int bottomLine;
                int[] tmpCords = new int[2];
                getLocationInWindow(tmpCords);
                int topWindowLocation = tmpCords[1];
                View root = this;
                ViewParent viewParent = getParent();
                while (viewParent instanceof View) {
                    root = (View) viewParent;
                    viewParent = root.getParent();
                }
                int windowHeight = root.getHeight();
                if (topWindowLocation >= 0) {
                    topLine = getLineAtCoordinateUnclamped(0.0f);
                    bottomLine = getLineAtCoordinateUnclamped((float) (windowHeight - 1));
                } else {
                    topLine = getLineAtCoordinateUnclamped((float) (-topWindowLocation));
                    bottomLine = getLineAtCoordinateUnclamped((float) ((windowHeight - 1) - topWindowLocation));
                }
                int expandedTopLine = topLine - ((bottomLine - topLine) / 2);
                if (expandedTopLine < 0) {
                    expandedTopLine = 0;
                }
                int expandedBottomLine = bottomLine + ((bottomLine - topLine) / 2);
                if (expandedBottomLine >= lineCount) {
                    expandedBottomLine = lineCount - 1;
                }
                int expandedTopChar = layout.getLineStart(expandedTopLine);
                int expandedBottomChar = layout.getLineEnd(expandedBottomLine);
                int selStart = getSelectionStart();
                int selEnd = getSelectionEnd();
                if (selStart < selEnd) {
                    if (selStart < expandedTopChar) {
                        expandedTopChar = selStart;
                    }
                    if (selEnd > expandedBottomChar) {
                        expandedBottomChar = selEnd;
                    }
                }
                CharSequence text = getText();
                if (expandedTopChar > 0 || expandedBottomChar < text.length()) {
                    text = text.subSequence(expandedTopChar, expandedBottomChar);
                }
                structure.setText(text, selStart - expandedTopChar, selEnd - expandedTopChar);
                int[] lineOffsets = new int[((bottomLine - topLine) + 1)];
                int[] lineBaselines = new int[((bottomLine - topLine) + 1)];
                int baselineOffset = getBaselineOffset();
                for (int i = topLine; i <= bottomLine; i++) {
                    lineOffsets[i - topLine] = layout.getLineStart(i);
                    lineBaselines[i - topLine] = layout.getLineBaseline(i) + baselineOffset;
                }
                structure.setTextLines(lineOffsets, lineBaselines);
            }
            int style = 0;
            int typefaceStyle = getTypefaceStyle();
            if ((typefaceStyle & 1) != 0) {
                style = 0 | 1;
            }
            if ((typefaceStyle & 2) != 0) {
                style |= 2;
            }
            int paintFlags = this.mTextPaint.getFlags();
            if ((paintFlags & 32) != 0) {
                style |= 1;
            }
            if ((paintFlags & 8) != 0) {
                style |= 4;
            }
            if ((paintFlags & 16) != 0) {
                style |= 8;
            }
            structure.setTextStyle(getTextSize(), getCurrentTextColor(), 1, style);
        }
        structure.setHint(getHint());
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setPassword(hasPasswordTransformationMethod());
        if (event.getEventType() == 8192) {
            event.setFromIndex(Selection.getSelectionStart(this.mText));
            event.setToIndex(Selection.getSelectionEnd(this.mText));
            event.setItemCount(this.mText.length());
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        boolean isPassword = hasPasswordTransformationMethod();
        info.setPassword(isPassword);
        if (!isPassword || shouldSpeakPasswordsForAccessibility()) {
            info.setText(getTextForAccessibility());
        }
        if (this.mBufferType == BufferType.EDITABLE) {
            info.setEditable(true);
        }
        if (this.mEditor != null) {
            info.setInputType(this.mEditor.mInputType);
            if (this.mEditor.mError != null) {
                info.setContentInvalid(true);
                info.setError(this.mEditor.mError);
            }
        }
        if (!TextUtils.isEmpty(this.mText)) {
            info.addAction(256);
            info.addAction(512);
            info.setMovementGranularities(31);
            info.addAction(131072);
        }
        if (isFocused()) {
            if (canCopy()) {
                info.addAction(16384);
            }
            if (canPaste()) {
                info.addAction(32768);
            }
            if (canCut()) {
                info.addAction(65536);
            }
            if (canShare()) {
                info.addAction(new AccessibilityAction(268435456, getResources().getString(R.string.share)));
            }
            if (canProcessText()) {
                this.mEditor.mProcessTextIntentActionsHandler.onInitializeAccessibilityNodeInfo(info);
            }
        }
        for (InputFilter filter : this.mFilters) {
            if (filter instanceof LengthFilter) {
                info.setMaxTextLength(((LengthFilter) filter).getMax());
            }
        }
        if (!isSingleLine()) {
            info.setMultiLine(true);
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (this.mEditor != null && this.mEditor.mProcessTextIntentActionsHandler.performAccessibilityAction(action)) {
            return true;
        }
        switch (action) {
            case 16:
                boolean handled = false;
                if (isClickable() || isLongClickable()) {
                    if (isFocusable() && !isFocused()) {
                        requestFocus();
                    }
                    performClick();
                    handled = true;
                }
                if ((this.mMovement == null && !onCheckIsTextEditor()) || !isEnabled() || !(this.mText instanceof Spannable) || this.mLayout == null) {
                    return handled;
                }
                if ((!isTextEditable() && !isTextSelectable()) || !isFocused()) {
                    return handled;
                }
                InputMethodManager imm = InputMethodManager.peekInstance();
                viewClicked(imm);
                if (isTextSelectable() || this.mEditor == null || !this.mEditor.mShowSoftInputOnFocus || imm == null) {
                    return handled;
                }
                return handled | imm.showSoftInput(this, 0);
            case 256:
            case 512:
                ensureIterableTextForAccessibilitySelectable();
                return super.performAccessibilityActionInternal(action, arguments);
            case 16384:
                if (isFocused() && canCopy() && onTextContextMenuItem(16908321)) {
                    return true;
                }
                return false;
            case 32768:
                if (isFocused() && canPaste() && onTextContextMenuItem(16908322)) {
                    return true;
                }
                return false;
            case 65536:
                if (isFocused() && canCut() && onTextContextMenuItem(16908320)) {
                    return true;
                }
                return false;
            case 131072:
                ensureIterableTextForAccessibilitySelectable();
                CharSequence text = getIterableTextForAccessibility();
                if (text == null) {
                    return false;
                }
                int start;
                int end;
                if (arguments != null) {
                    start = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1);
                } else {
                    start = -1;
                }
                if (arguments != null) {
                    end = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1);
                } else {
                    end = -1;
                }
                if (!(getSelectionStart() == start && getSelectionEnd() == end)) {
                    if (start == end && end == -1) {
                        Selection.removeSelection((Spannable) text);
                        return true;
                    } else if (start >= 0 && start <= end && end <= text.length()) {
                        Selection.setSelection((Spannable) text, start, end);
                        if (this.mEditor == null) {
                            return true;
                        }
                        this.mEditor.startSelectionActionMode();
                        return true;
                    }
                }
                return false;
            case 268435456:
                if (isFocused() && canShare() && onTextContextMenuItem(16908341)) {
                    return true;
                }
                return false;
            default:
                return super.performAccessibilityActionInternal(action, arguments);
        }
    }

    public void sendAccessibilityEventInternal(int eventType) {
        if (eventType == 32768 && this.mEditor != null) {
            this.mEditor.mProcessTextIntentActionsHandler.initializeAccessibilityActions();
        }
        if (eventType != 4096) {
            super.sendAccessibilityEventInternal(eventType);
        }
    }

    public CharSequence getTextForAccessibility() {
        CharSequence text = getText();
        if (TextUtils.isEmpty(text)) {
            return getHint();
        }
        return text;
    }

    void sendAccessibilityEventTypeViewTextChanged(CharSequence beforeText, int fromIndex, int removedCount, int addedCount) {
        AccessibilityEvent event = AccessibilityEvent.obtain(16);
        event.setFromIndex(fromIndex);
        event.setRemovedCount(removedCount);
        event.setAddedCount(addedCount);
        event.setBeforeText(beforeText);
        sendAccessibilityEventUnchecked(event);
    }

    public boolean isInputMethodTarget() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        return imm != null && imm.isActive(this);
    }

    public boolean onTextContextMenuItem(int id) {
        boolean shouldRestartActionMode = false;
        int min = 0;
        int max = this.mText.length();
        if (isFocused()) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();
            min = Math.max(0, Math.min(selStart, selEnd));
            max = Math.max(0, Math.max(selStart, selEnd));
        }
        ClipboardDataText clipdata;
        String source;
        switch (id) {
            case 16908319:
                if (!(this.mEditor == null || this.mEditor.mTextActionMode == null)) {
                    shouldRestartActionMode = true;
                }
                stopTextActionMode();
                selectAllText();
                if (!shouldRestartActionMode) {
                    return true;
                }
                this.mEditor.startSelectionActionMode();
                return true;
            case 16908320:
                if (!this.mRestrictionPolicy.isClipboardAllowed(true)) {
                    return false;
                }
                if (getSecClipboardEnabled()) {
                    clipdata = new ClipboardDataText();
                    clipdata.SetText(this.mTransformed.subSequence(min, max));
                    setPrimaryClipEx(clipdata);
                } else {
                    setPrimaryClip(ClipData.newPlainText(null, getTransformedText(min, max)));
                }
                deleteText_internal(min, max);
                stopTextActionMode();
                return true;
            case 16908321:
                if (!this.mRestrictionPolicy.isClipboardAllowed(true)) {
                    return false;
                }
                if (getSecClipboardEnabled()) {
                    clipdata = new ClipboardDataText();
                    clipdata.SetText(this.mTransformed.subSequence(min, max));
                    setPrimaryClipEx(clipdata);
                } else {
                    setPrimaryClip(ClipData.newPlainText(null, getTransformedText(min, max)));
                }
                stopTextActionMode();
                return true;
            case 16908322:
                paste(min, max, true);
                stopTextActionMode();
                return true;
            case 16908337:
                paste(min, max, false);
                return true;
            case 16908338:
                if (this.mEditor == null) {
                    return true;
                }
                this.mEditor.undo();
                return true;
            case 16908339:
                if (this.mEditor == null) {
                    return true;
                }
                this.mEditor.redo();
                return true;
            case 16908340:
                if (this.mEditor == null) {
                    return true;
                }
                this.mEditor.replace();
                return true;
            case 16908341:
                shareSelectedText();
                return true;
            case 16908373:
                if (this.mEditor == null) {
                    return true;
                }
                source = this.mTransformed.subSequence(min, max).toString();
                this.mEditor.stopTextActionMode();
                this.mEditor.sendToDictionary(source, min, max);
                return true;
            case 16908375:
                if (getClipboardExManager().showDialog(this.mClipboardDataFormat, this.mPasteEventListener)) {
                    return true;
                }
                Log.e(LOG_TAG, "clip board is not shown");
                return true;
            case 16908376:
                deleteText_internal(min, max);
                return true;
            case 16908377:
                if (this.mEditor == null) {
                    return true;
                }
                this.mEditor.stopTextActionMode();
                this.mEditor.mSelectionModifierCursorController.hide();
                return true;
            case 16909562:
                Intent send = new Intent("android.intent.action.WEB_SEARCH");
                source = this.mTransformed.subSequence(min, max).toString();
                send.putExtra("new_search", true);
                send.putExtra("query", source);
                send.putExtra("com.android.browser.application_id", getContext().getPackageName());
                try {
                    send.setFlags(268435456);
                    getContext().startActivity(send);
                    return true;
                } catch (ActivityNotFoundException ex) {
                    Log.e(LOG_TAG, "WebSearch failed");
                    Log.e(LOG_TAG, "ActivityNotFoundException", ex);
                    return true;
                }
            default:
                return false;
        }
    }

    CharSequence getTransformedText(int start, int end) {
        return removeSuggestionSpans(this.mTransformed.subSequence(start, end));
    }

    public boolean performClick() {
        boolean result = super.performClick();
        if (result && this.mhasMultiSelection) {
            clearMultiSelection();
        }
        return result;
    }

    public boolean performLongClick() {
        boolean handled = false;
        AccessibilityManager am = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        if (super.performLongClick()) {
            handled = true;
        }
        if (!(this.mEditor == null || am.isUniversalSwitchEnabled())) {
            handled |= this.mEditor.performLongClick(handled);
        }
        if (handled && this.mEditor != null) {
            this.mEditor.mDiscardNextActionUp = true;
        }
        return handled;
    }

    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (this.mEditor != null) {
            this.mEditor.onScrollChanged();
        }
    }

    public boolean isSuggestionsEnabled() {
        if (this.mEditor == null || (this.mEditor.mInputType & 15) != 1 || (this.mEditor.mInputType & 524288) > 0) {
            return false;
        }
        int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
        if (variation == 0 || variation == 48 || variation == 80 || variation == 64 || variation == 160) {
            return true;
        }
        return false;
    }

    public void setCustomSelectionActionModeCallback(Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomSelectionActionModeCallback = actionModeCallback;
    }

    public Callback getCustomSelectionActionModeCallback() {
        return this.mEditor == null ? null : this.mEditor.mCustomSelectionActionModeCallback;
    }

    public void setCustomInsertionActionModeCallback(Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomInsertionActionModeCallback = actionModeCallback;
    }

    public Callback getCustomInsertionActionModeCallback() {
        return this.mEditor == null ? null : this.mEditor.mCustomInsertionActionModeCallback;
    }

    protected void stopTextActionMode() {
        if (this.mEditor != null) {
            this.mEditor.stopTextActionMode();
        }
    }

    boolean canUndo() {
        return this.mEditor != null && this.mEditor.canUndo();
    }

    boolean canRedo() {
        return this.mEditor != null && this.mEditor.canRedo();
    }

    boolean canCut() {
        if (!hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && (this.mText instanceof Editable) && this.mEditor != null && this.mEditor.mKeyListener != null) {
            return true;
        }
        return false;
    }

    boolean canCopy() {
        if (!hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && this.mEditor != null) {
            return true;
        }
        return false;
    }

    boolean canDelete() {
        if (!hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection()) {
            return true;
        }
        return false;
    }

    boolean canDictionary() {
        return canCopy() && isFinishSetupWizard() && this.mEnableDictionary;
    }

    boolean canWebSearch() {
        return canCopy() && isFinishSetupWizard() && this.mEnableWebSearch;
    }

    boolean canClipboard() {
        return getSecClipboardEnabled() && (this.mText instanceof Editable) && getKeyListener() != null && getClipboardExManager().getDataListSize() > 0 && this.mEnableClipboard;
    }

    boolean canShare() {
        return canCopy() && isFinishSetupWizard() && this.mEnableShare;
    }

    boolean canPaste() {
        if ((this.mText instanceof Editable) && this.mEditor != null && this.mEditor.mKeyListener != null && getSelectionStart() >= 0 && getSelectionEnd() >= 0) {
            if (!getSecClipboardEnabled()) {
                return ((ClipboardManager) getContext().getSystemService("clipboard")).hasPrimaryClip();
            }
            if (getClipboardExManager().hasData(2)) {
                return true;
            }
            Log.e(ClipboardDefine.CLIPBOARD_TAG, "clipEx is " + getClipboardExManager());
            Log.e(ClipboardDefine.CLIPBOARD_TAG, "clipEx has text data : false");
        }
        return false;
    }

    boolean canProcessText() {
        if (!getContext().canStartActivityForResult() || getId() == -1 || hasPasswordTransformationMethod() || this.mText.length() <= 0 || !hasSelection() || this.mEditor == null) {
            return false;
        }
        return true;
    }

    boolean canSelectAllText() {
        return (!canSelectText() || hasPasswordTransformationMethod() || (getSelectionStart() == 0 && getSelectionEnd() == this.mText.length())) ? false : true;
    }

    boolean selectAllText() {
        if (this.mEditor != null) {
            this.mEditor.hideInsertionPointCursorController();
        }
        int length = this.mText.length();
        Selection.setSelection((Spannable) this.mText, 0, length);
        return length > 0;
    }

    void replaceSelectionWithText(CharSequence text) {
        ((Editable) this.mText).replace(getSelectionStart(), getSelectionEnd(), text);
    }

    private void paste(int min, int max, boolean withFormatting) {
        ClipData clip;
        boolean didFirst;
        int i;
        CharSequence paste;
        if (!this.mRestrictionPolicy.isClipboardShareAllowed()) {
            clip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
            if (clip != null) {
                didFirst = false;
                for (i = 0; i < clip.getItemCount(); i++) {
                    paste = clip.getItemAt(i).coerceToStyledText(getContext());
                    if (paste != null) {
                        if (didFirst) {
                            ((Editable) this.mText).insert(getSelectionEnd(), "\n");
                            ((Editable) this.mText).insert(getSelectionEnd(), paste);
                        } else {
                            Selection.setSelection((Spannable) this.mText, max);
                            ((Editable) this.mText).replace(min, max, paste);
                            didFirst = true;
                        }
                    }
                }
                stopTextActionMode();
                sLastCutCopyOrTextChangedTime = 0;
                return;
            }
        }
        CharSequence text;
        if (!getSecClipboardEnabled()) {
            clip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
            if (clip != null) {
                didFirst = false;
                for (i = 0; i < clip.getItemCount(); i++) {
                    if (withFormatting) {
                        paste = clip.getItemAt(i).coerceToStyledText(getContext());
                    } else {
                        text = clip.getItemAt(i).coerceToText(getContext());
                        paste = text instanceof Spanned ? text.toString() : text;
                    }
                    if (paste != null) {
                        if (didFirst) {
                            ((Editable) this.mText).insert(getSelectionEnd(), "\n");
                            ((Editable) this.mText).insert(getSelectionEnd(), paste);
                        } else {
                            Selection.setSelection((Spannable) this.mText, max);
                            ((Editable) this.mText).replace(min, max, paste);
                            didFirst = true;
                        }
                    }
                }
                stopTextActionMode();
                sLastCutCopyOrTextChangedTime = 0;
            }
        } else if (getClipboardExManager().isFiltered()) {
            getClipboardExManager().requestPaste(getClipboardExManager().getData(1));
            sLastCutCopyOrTextChangedTime = 0;
        } else {
            try {
                ClipboardDataText clipdata = (ClipboardDataText) getClipboardExManager().getData(2);
                if (clipdata != null) {
                    paste = null;
                    text = clipdata.GetText();
                    if (text != null) {
                        paste = (withFormatting && (text instanceof Spanned)) ? text : text.toString();
                    }
                    if (paste != null && paste.length() > 0) {
                        paste = removeEasyEditSpan(paste);
                        Selection.setSelection((Spannable) this.mText, max);
                        ((Editable) this.mText).replace(min, max, paste);
                        stopTextActionMode();
                        sLastCutCopyOrTextChangedTime = 0;
                    }
                }
            } catch (ClassCastException e) {
                Log.e(LOG_TAG, "skip to paste clipdata - ClassCastException");
            }
        }
    }

    private void shareSelectedText() {
        String selectedText = getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("text/plain");
            sharingIntent.removeExtra("android.intent.extra.TEXT");
            sharingIntent.putExtra("android.intent.extra.TEXT", selectedText);
            getContext().startActivity(Intent.createChooser(sharingIntent, null));
            stopTextActionMode();
        }
    }

    private void setPrimaryClip(ClipData clip) {
        ((ClipboardManager) getContext().getSystemService("clipboard")).setPrimaryClip(clip);
        sLastCutCopyOrTextChangedTime = SystemClock.uptimeMillis();
    }

    private void setPrimaryClipEx(ClipboardDataText clip) {
        getClipboardExManager().setData(getContext(), clip);
        sLastCutCopyOrTextChangedTime = SystemClock.uptimeMillis();
    }

    public int getOffsetForPosition(float x, float y) {
        if (getLayout() == null) {
            return -1;
        }
        return getOffsetAtCoordinate(getLineAtCoordinate(y), x);
    }

    float convertToLocalHorizontalCoordinate(float x) {
        return Math.min((float) ((getWidth() - getTotalPaddingRight()) - 1), Math.max(0.0f, x - ((float) getTotalPaddingLeft()))) + ((float) getScrollX());
    }

    int getLineAtCoordinate(float y) {
        return getLayout().getLineForVertical((int) (Math.min((float) ((getHeight() - getTotalPaddingBottom()) - 1), Math.max(0.0f, y - ((float) getTotalPaddingTop()))) + ((float) getScrollY())));
    }

    int getLineAtCoordinateUnclamped(float y) {
        return getLayout().getLineForVertical((int) ((y - ((float) getTotalPaddingTop())) + ((float) getScrollY())));
    }

    int getOffsetAtCoordinate(int line, float x) {
        return getLayout().getOffsetForHorizontal(line, convertToLocalHorizontalCoordinate(x));
    }

    public boolean onDragEvent(DragEvent event) {
        switch (event.getAction()) {
            case 1:
                boolean z = this.mEditor != null && this.mEditor.hasInsertionController();
                return z;
            case 2:
                Selection.setSelection((Spannable) this.mText, getOffsetForPosition(event.getX(), event.getY()));
                return true;
            case 3:
                this.mOnDragResult = true;
                if (this.mEditor != null && (this.mText instanceof Spannable)) {
                    this.mEditor.onDrop(event);
                }
                return this.mOnDragResult;
            case 5:
                requestFocus();
                return true;
            default:
                return true;
        }
    }

    public void setOnDragResult(boolean value) {
        this.mOnDragResult = value;
    }

    boolean isInBatchEditMode() {
        if (this.mEditor == null) {
            return false;
        }
        InputMethodState ims = this.mEditor.mInputMethodState;
        if (ims == null) {
            return this.mEditor.mInBatchEditControllers;
        }
        if (ims.mBatchEditNesting > 0) {
            return true;
        }
        return false;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        TextDirectionHeuristic newTextDir = getTextDirectionHeuristic();
        if (this.mTextDir != newTextDir) {
            this.mTextDir = newTextDir;
            if (this.mLayout != null) {
                checkForRelayout();
            }
        }
    }

    TextDirectionHeuristic getTextDirectionHeuristic() {
        boolean defaultIsRtl = true;
        if (hasPasswordTransformationMethod()) {
            return TextDirectionHeuristics.LTR;
        }
        if (getLayoutDirection() != 1) {
            defaultIsRtl = false;
        }
        switch (getTextDirection()) {
            case 2:
                return TextDirectionHeuristics.ANYRTL_LTR;
            case 3:
                return TextDirectionHeuristics.LTR;
            case 4:
                return TextDirectionHeuristics.RTL;
            case 5:
                return TextDirectionHeuristics.LOCALE;
            case 6:
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case 7:
                return TextDirectionHeuristics.FIRSTSTRONG_RTL;
            default:
                if (defaultIsRtl) {
                    return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                }
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
        }
    }

    TextDirectionHeuristic getTextDirectionHeuristic(boolean isHint) {
        boolean defaultIsRtl = true;
        if (!isHint) {
            return getTextDirectionHeuristic();
        }
        if (getLayoutDirection() != 1) {
            defaultIsRtl = false;
        }
        switch (getTextDirection()) {
            case 2:
                return TextDirectionHeuristics.ANYRTL_LTR;
            case 3:
                return TextDirectionHeuristics.LTR;
            case 4:
                return TextDirectionHeuristics.RTL;
            case 5:
                return TextDirectionHeuristics.LOCALE;
            case 6:
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case 7:
                return TextDirectionHeuristics.FIRSTSTRONG_RTL;
            default:
                if (defaultIsRtl) {
                    return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                }
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        if (this.mLastLayoutDirection != layoutDirection) {
            this.mLastLayoutDirection = layoutDirection;
            if (this.mDrawables != null) {
                this.mDrawables.resolveWithLayoutDirection(layoutDirection);
            }
        }
    }

    protected void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        this.mLastLayoutDirection = -1;
    }

    protected void viewClicked(InputMethodManager imm) {
        if (imm != null) {
            imm.viewClicked(this);
        }
    }

    protected void deleteText_internal(int start, int end) {
        ((Editable) this.mText).delete(start, end);
    }

    protected void replaceText_internal(int start, int end, CharSequence text) {
        ((Editable) this.mText).replace(start, end, text);
    }

    protected void setSpan_internal(Object span, int start, int end, int flags) {
        ((Editable) this.mText).setSpan(span, start, end, flags);
    }

    protected void setCursorPosition_internal(int start, int end) {
        Selection.setSelection((Editable) this.mText, start, end);
    }

    private void createEditorIfNeeded() {
        if (this.mEditor == null) {
            this.mEditor = new Editor(this);
        }
    }

    public CharSequence getIterableTextForAccessibility() {
        return this.mText;
    }

    private void ensureIterableTextForAccessibilitySelectable() {
        if (!(this.mText instanceof Spannable)) {
            setText(this.mText, BufferType.SPANNABLE);
        }
    }

    public TextSegmentIterator getIteratorForGranularity(int granularity) {
        TextSegmentIterator iterator;
        switch (granularity) {
            case 4:
                Spannable text = (Spannable) getIterableTextForAccessibility();
                if (!(TextUtils.isEmpty(text) || getLayout() == null)) {
                    iterator = LineTextSegmentIterator.getInstance();
                    iterator.initialize(text, getLayout());
                    return iterator;
                }
            case 16:
                if (!(TextUtils.isEmpty((Spannable) getIterableTextForAccessibility()) || getLayout() == null)) {
                    iterator = PageTextSegmentIterator.getInstance();
                    iterator.initialize(this);
                    return iterator;
                }
        }
        return super.getIteratorForGranularity(granularity);
    }

    public int getAccessibilitySelectionStart() {
        return getSelectionStart();
    }

    public boolean isAccessibilitySelectionExtendable() {
        return true;
    }

    public int getAccessibilitySelectionEnd() {
        return getSelectionEnd();
    }

    public void setAccessibilitySelection(int start, int end) {
        if (getAccessibilitySelectionStart() != start || getAccessibilitySelectionEnd() != end) {
            if (this.mEditor != null) {
                this.mEditor.hideCursorAndSpanControllers();
                this.mEditor.stopTextActionMode();
            }
            CharSequence text = getIterableTextForAccessibility();
            if (Math.min(start, end) < 0 || Math.max(start, end) > text.length()) {
                Selection.removeSelection((Spannable) text);
            } else {
                Selection.setSelection((Spannable) text, start, end);
            }
        }
    }

    protected void encodeProperties(ViewHierarchyEncoder stream) {
        String str = null;
        super.encodeProperties(stream);
        TruncateAt ellipsize = getEllipsize();
        stream.addProperty("text:ellipsize", ellipsize == null ? null : ellipsize.name());
        stream.addProperty("text:textSize", getTextSize());
        stream.addProperty("text:scaledTextSize", getScaledTextSize());
        stream.addProperty("text:typefaceStyle", getTypefaceStyle());
        stream.addProperty("text:selectionStart", getSelectionStart());
        stream.addProperty("text:selectionEnd", getSelectionEnd());
        stream.addProperty("text:curTextColor", this.mCurTextColor);
        String str2 = "text:text";
        if (this.mText != null) {
            str = this.mText.toString();
        }
        stream.addProperty(str2, str);
        stream.addProperty("text:gravity", this.mGravity);
    }

    public void forceHideSoftInput(boolean hide) {
        this.mHideSoftInput = hide;
    }

    public boolean getHideSoftInput() {
        return this.mHideSoftInput;
    }

    public void setCursorColor(int color) {
    }

    public void hideCursorControllers() {
        if (this.mEditor != null) {
            this.mEditor.hideCursorAndSpanControllers();
            this.mEditor.stopTextActionMode();
        }
    }

    public boolean isCursorControllersShowing() {
        return true;
    }

    public boolean isEllipsis() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (getVisibility() != 0 || width <= 0 || getLineCount() != 1 || getLayout() == null || (getLayout().getLineWidth(0) <= ((float) width) && getLayout().getEllipsisCount(0) <= 0)) {
            return false;
        }
        return true;
    }

    public Rect getSpannedTextRect(Rect targetRect) {
        CharSequence text = getText();
        if (!(text instanceof Spanned) || this.mLayout == null) {
            return null;
        }
        Spanned spannedText = (Spanned) text;
        if (((ReplacementSpan[]) spannedText.getSpans(0, text.length(), ReplacementSpan.class)).length <= 0) {
            return null;
        }
        Point startPos = getScreenPointOfView(this);
        int tx = targetRect.left - startPos.x;
        int line = getLineAtCoordinate((float) (targetRect.top - startPos.y));
        int maxLine = getMaxLines();
        if (maxLine > 0 && maxLine <= line) {
            line = maxLine - 1;
        }
        int offset = getOffsetAtCoordinate(line, (float) tx);
        int textXPos = (int) this.mLayout.getPrimaryHorizontal(offset);
        if (tx < textXPos) {
            if (offset <= 0) {
                return null;
            }
            offset--;
            textXPos = (int) this.mLayout.getPrimaryHorizontal(offset);
        }
        ReplacementSpan[] spans = (ReplacementSpan[]) spannedText.getSpans(offset, offset, ReplacementSpan.class);
        if (spans.length <= 0) {
            return null;
        }
        int start = spannedText.getSpanStart(spans[0]);
        int end = spannedText.getSpanEnd(spans[0]);
        int top = this.mLayout.getLineTop(line);
        int bottom = this.mLayout.getLineBottom(line);
        Rect rect = new Rect(0, 0, 0, 0);
        rect.right = spans[0].getSize(this.mTextPaint, spannedText, start, end, null);
        rect.bottom = bottom - top;
        rect.offset(startPos.x + textXPos, startPos.y + top);
        return rect;
    }

    private Point getScreenPointOfView(View view) {
        Point screenPointOfView = new Point();
        int[] screenOffsetOfView = new int[2];
        view.getLocationOnScreen(screenOffsetOfView);
        screenPointOfView.x = screenOffsetOfView[0];
        screenPointOfView.y = screenOffsetOfView[1];
        return screenPointOfView;
    }

    public HoverPopupWindow getHoverPopupWindow() {
        if (!isHoveringUIEnabled()) {
            return null;
        }
        if (this.mHoverPopup == null) {
            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
        }
        setHoverPopupWindowSettings(2);
        return this.mHoverPopup;
    }

    public HoverPopupWindow getHoverPopupWindow(int tooltype) {
        if (!isHoveringUIEnabled()) {
            return null;
        }
        if (this.mHoverPopup == null) {
            this.mToolType = tooltype;
            if (tooltype != 2) {
                if (tooltype == 1) {
                    switch (this.mHoverPopupType) {
                        case 1:
                            break;
                        case 2:
                            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
                            break;
                        case 3:
                            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
                            break;
                        default:
                            break;
                    }
                }
            }
            this.mHoverPopup = new MoreInfoHPW(this, this.mHoverPopupType);
        }
        setHoverPopupWindowSettings(tooltype);
        this.mHoverPopupToolTypeByApp = tooltype;
        if (tooltype == 1 && this.mHoverPopupType == 1 && this.mHoverPopup != null) {
            this.mHoverPopup.dismiss();
            this.mHoverPopup = null;
        }
        return this.mHoverPopup;
    }

    private boolean isLinkPreviewEnabled(int toolType) {
        switch (toolType) {
            case 1:
            case 3:
                return false;
            case 2:
                return isLinkPreviewSettingsEnabled();
            default:
                return false;
        }
    }

    private boolean isLinkPreviewSettingsEnabled() {
        boolean isSPenHoveringOn;
        if (Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING, 0, -3) == 1) {
            isSPenHoveringOn = true;
        } else {
            isSPenHoveringOn = false;
        }
        return isSPenHoveringOn && Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_LINK_PREVIEW, 0, -3) == 1;
    }

    private boolean canTextMultiSelection() {
        return (!this.mEnableMultiSelection || !isCoverOpened() || !isFinishSetupWizard() || isCarMode() || isKeyguardLocked() || isDisabledStylusPenEvent() || isSubWindow()) ? false : true;
    }

    private boolean isCoverOpened() {
        if (this.mCoverManager != null) {
            try {
                ICoverManager svc = Stub.asInterface(ServiceManager.getService("cover"));
                if (svc != null) {
                    CoverState coverState = svc.getCoverState();
                    if (coverState != null) {
                        return coverState.getSwitchState();
                    }
                }
            } catch (RemoteException e) {
                Log.w(LOG_TAG, "isCoverOpened() : RemoteException!!!!");
            }
        } else {
            Log.w(LOG_TAG, "isCoverOpened() : mCoverManager is null!!!!");
        }
        return true;
    }

    private boolean isCarMode() {
        if (Secure.getIntForUser(this.mContext.getContentResolver(), "car_mode_on", 0, -3) != 1) {
            return false;
        }
        Log.w(LOG_TAG, "TextView does not support text selection on Carmode.");
        return true;
    }

    private boolean isKeyguardLocked() {
        boolean z = false;
        if (this.mContext == null) {
            Log.d(LOG_TAG, "isKeyguardLocked. context is null");
        } else {
            KeyguardManager keyGuard = (KeyguardManager) this.mContext.getSystemService("keyguard");
            if (keyGuard == null) {
                Log.d(LOG_TAG, "keyGuard Service is null");
            } else {
                z = keyGuard.isKeyguardLocked();
                if (z) {
                    Log.d(LOG_TAG, "Keyguard is Locked!");
                }
            }
        }
        return z;
    }

    private boolean isFinishSetupWizard() {
        if ("FINISH".equals(SystemProperties.get("persist.sys.setupwizard"))) {
            return true;
        }
        Log.w(LOG_TAG, "Setup Wizard is not finished.");
        return false;
    }

    private boolean isSubWindow() {
        LayoutParams params = getRootView().getLayoutParams();
        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams windowParams = (WindowManager.LayoutParams) params;
            if (windowParams.type >= 1000 && windowParams.type <= WindowManager.LayoutParams.LAST_SUB_WINDOW) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPosInView(int x, int y, int overplus) {
        if (!isVisibleToUser()) {
            return false;
        }
        Rect rect = new Rect();
        getGlobalVisibleRect(rect, null);
        Point startPos = getScreenPointOfView(getRootView());
        rect.offset(startPos.x, startPos.y);
        rect.inset(-overplus, -overplus);
        if (x < rect.left || x > rect.right || y < rect.top || y > rect.bottom) {
            return false;
        }
        return true;
    }

    private boolean checkPosOnText(int x, int y, int overplus) {
        Layout layout = getLayout();
        if (layout == null) {
            return false;
        }
        int[] screenOffsetOfView = new int[2];
        getLocationOnScreen(screenOffsetOfView);
        int posX = x - screenOffsetOfView[0];
        int posY = y - screenOffsetOfView[1];
        int line = getLineAtCoordinate((float) posY);
        int lineTop = layout.getLineTop(line) + getTotalPaddingTop();
        int lineBtm = layout.getLineBottom(line) + getTotalPaddingTop();
        int lineWidth = (int) layout.getLineWidth(line);
        int textStartX = (((int) layout.getPrimaryHorizontal(layout.getLineStart(line))) - getScrollX()) + getTotalPaddingLeft();
        int textEndX;
        if (layout.getParagraphDirection(line) == -1) {
            textEndX = textStartX;
            textStartX = textEndX - lineWidth;
        } else {
            textEndX = textStartX + lineWidth;
        }
        if (posX < textStartX - overplus || textEndX + overplus < posX || posY < lineTop || posY > lineBtm) {
            return false;
        }
        return true;
    }

    public boolean checkValidMultiSelectionForPreDraw() {
        if (!this.mhasMultiSelection || !isVisibleToUser() || getLayout() == null) {
            return false;
        }
        View parent = this;
        View rootView = getRootView();
        do {
            parent = (View) parent.getParent();
            if (parent == null) {
                return false;
            }
            if (parent == rootView) {
                return true;
            }
        } while (parent instanceof View);
        return false;
    }

    public boolean isValidMultiSelection() {
        if (!this.mhasMultiSelection || !isVisibleToUser()) {
            return false;
        }
        CharSequence text = getTextForMultiSelection();
        if (text == null) {
            return false;
        }
        Layout layout = getLayout();
        if (layout == null) {
            return false;
        }
        Rect tvRect = new Rect();
        getGlobalVisibleRect(tvRect, null);
        Point startPos = getScreenPointOfView(getRootView());
        tvRect.offset(startPos.x, startPos.y);
        int selStart = MultiSelection.getSelectionStart((Spannable) text);
        int selEnd = MultiSelection.getSelectionEnd((Spannable) text);
        if (selStart >= selEnd) {
            int tmp = selStart;
            selStart = selEnd;
            selEnd = tmp;
        }
        int sLine = layout.getLineForOffset(selStart);
        int eLine = layout.getLineForOffset(selEnd);
        int topPadding = getTotalPaddingTop();
        int leftPadding = getTotalPaddingLeft();
        int sx = (((int) layout.getPrimaryHorizontal(selStart)) + leftPadding) - getScrollX();
        int ex = (((int) layout.getPrimaryHorizontal(selEnd)) + leftPadding) - getScrollX();
        if (sx > ex) {
            tmp = sx;
            sx = ex;
            ex = tmp;
        }
        int lineTop = (layout.getLineTop(sLine) + topPadding) - getScrollY();
        int baseLine = (layout.getLineBaseline(eLine) + topPadding) - getScrollY();
        startPos = getScreenPointOfView(this);
        Rect srcRect = new Rect(sx, lineTop, ex, baseLine);
        srcRect.offset(startPos.x, startPos.y);
        if (!srcRect.intersect(tvRect)) {
            return false;
        }
        if (this.mPenSelectionController.findTargetTextView(getContext(), getRootView(), srcRect) != this) {
            return false;
        }
        return true;
    }

    public int extractSmartClipData(SlookSmartClipDataElement resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (((SmartClipDataElementImpl) resultElement).getExtractionMode() == 0) {
            TransformationMethod transformationMethod = getTransformationMethod();
            if (transformationMethod == null || !(transformationMethod instanceof PasswordTransformationMethod)) {
                CharSequence charSequence = getTextForRectSelection(croppedArea.getRect());
                if (charSequence == null) {
                    charSequence = "";
                }
                resultElement.addTag(new SlookSmartClipMetaTag("plain_text", charSequence.toString()));
            }
        } else {
            super.extractSmartClipData(resultElement, croppedArea);
        }
        return 1;
    }

    private CharSequence getTextForRectSelection(Rect selectedRect) {
        CharSequence text = getTextForMultiSelection();
        if (text == null) {
            text = getText();
        }
        if (TextUtils.isEmpty(text) || this.mLayout == null) {
            return null;
        }
        Rect tvRect = new Rect();
        getGlobalVisibleRect(tvRect, null);
        Point startPos = getScreenPointOfView(getRootView());
        tvRect.offset(startPos.x, startPos.y);
        startPos = getScreenPointOfView(this);
        int topPadding = getTotalPaddingTop();
        int leftPadding = getTotalPaddingLeft();
        int textBtm = ((this.mLayout.getLineBottom(this.mLayout.getLineCount() - 1) + startPos.y) + topPadding) - getScrollY();
        tvRect.top = Math.max(((this.mLayout.getLineTop(0) + startPos.y) + topPadding) - getScrollY(), tvRect.top);
        tvRect.bottom = Math.min(textBtm, tvRect.bottom);
        if (!Rect.intersects(tvRect, selectedRect)) {
            return null;
        }
        int startX = selectedRect.left - startPos.x;
        int endX = selectedRect.right - startPos.x;
        int endY = selectedRect.bottom - startPos.y;
        int startOffset = getOffsetAtCoordinate(getLineAtCoordinate((float) (selectedRect.top - startPos.y)), (float) startX);
        int endOffset = getOffsetAtCoordinate(getLineAtCoordinate((float) endY), (float) endX);
        if (startOffset < 0 || endOffset < 0 || startOffset == endOffset) {
            return null;
        }
        if (startOffset > endOffset) {
            int tmp = startOffset;
            startOffset = endOffset;
            endOffset = tmp;
        }
        return text.subSequence(startOffset, endOffset);
    }

    public CharSequence getTextForMultiSelection() {
        if (this.mUseDisplayText) {
            return this.mDisplayText;
        }
        if (this.mTransformed instanceof Spannable) {
            return this.mTransformed;
        }
        if (this instanceof EditText) {
            return this.mText;
        }
        return null;
    }

    public boolean getVisibleTextRange(int[] range) {
        Layout layout = getLayout();
        if (layout == null || range == null || range.length < 2) {
            return false;
        }
        CharSequence text = layout.getText();
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        int start;
        int count;
        if (this.mEllipsize == TruncateAt.START) {
            start = layout.getEllipsisStart(0);
            count = layout.getEllipsisCount(0);
            if (count > 0) {
                range[0] = (start + count) - 1;
                range[1] = text.length();
            } else {
                range[0] = 0;
                range[1] = text.length();
            }
        } else if (this.mEllipsize == TruncateAt.MIDDLE) {
            range[0] = 0;
            range[1] = text.length();
        } else if (this.mEllipsize == TruncateAt.KEYWORD) {
            start = layout.getEllipsisStart(0);
            count = layout.getEllipsisCount(0);
            range[0] = 0;
            range[1] = text.length();
            if (start == 0) {
                if (count > 0) {
                    range[0] = (start + count) - 1;
                    range[1] = text.length();
                }
            } else if (count > 0) {
                range[0] = 0;
                range[1] = start + 1;
            }
        } else {
            int line = layout.getLineCount() - 1;
            if (line < 0) {
                return false;
            }
            start = layout.getEllipsisStart(line);
            if (layout.getEllipsisCount(line) > 0) {
                range[0] = 0;
                range[1] = (layout.getLineStart(line) + start) + 1;
                CharSequence disText = getTextForMultiSelection();
                if (disText != null && Character.isLowSurrogate(disText.charAt(range[1]))) {
                    range[1] = range[1] + 1;
                }
            } else {
                range[0] = 0;
                range[1] = text.length();
            }
        }
        return true;
    }

    public boolean hasMultiSelection() {
        return this.mhasMultiSelection;
    }

    public boolean clearMultiSelection() {
        this.mWordIteratorForMultiSelection = null;
        if (this.mhasMultiSelection) {
            CharSequence text = getTextForMultiSelection();
            if (text != null) {
                MultiSelection.clearMultiSelection((Spannable) text);
            }
            hideMultiSelectPopupWindow();
            removeForTouchMonitorListener();
            mTargetView = null;
            this.mhasMultiSelection = false;
            invalidate();
        }
        return true;
    }

    public CharSequence getMultiSelectionText() {
        CharSequence selectedText = null;
        CharSequence text = getTextForMultiSelection();
        if (text == null || !this.mhasMultiSelection) {
            return null;
        }
        int[] range = new int[2];
        boolean flag = getVisibleTextRange(range);
        int[] multiSelStart = MultiSelection.getMultiSelectionStart((Spannable) text);
        int[] multiSelEnd = MultiSelection.getMultiSelectionEnd((Spannable) text);
        int multiSelCount = MultiSelection.getMultiSelectionCount((Spannable) text);
        for (int i = 0; i < multiSelCount; i++) {
            if (multiSelStart[i] <= multiSelEnd[i]) {
                if (flag) {
                    if (multiSelStart[i] == range[0]) {
                        multiSelStart[i] = 0;
                    }
                    if (multiSelEnd[i] == range[1]) {
                        multiSelEnd[i] = text.length();
                    }
                }
                if (selectedText == null) {
                    selectedText = new SpannableStringBuilder(text.subSequence(multiSelStart[i], multiSelEnd[i]));
                } else {
                    ((Editable) selectedText).append(text.subSequence(multiSelStart[i], multiSelEnd[i]));
                }
            }
        }
        return selectedText;
    }

    public boolean isMultiSelectionLinkArea(int x, int y) {
        if (!this.mhasMultiSelection || this.mLayout == null || !checkPosInView(x, y, 0)) {
            return false;
        }
        CharSequence text = getTextForMultiSelection();
        if (text == null) {
            return false;
        }
        Point startPos = getScreenPointOfView(this);
        int offset = getOffsetAtCoordinate(getLineAtCoordinate((float) (y - startPos.y)), (float) (x - startPos.x));
        int[] multiSelStart = MultiSelection.getMultiSelectionStart((Spannable) text);
        int[] multiSelEnd = MultiSelection.getMultiSelectionEnd((Spannable) text);
        int multiSelCount = MultiSelection.getMultiSelectionCount((Spannable) text);
        int i = 0;
        while (i < multiSelCount) {
            if (multiSelStart[i] <= offset && offset <= multiSelEnd[i]) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean clearAllMultiSelection() {
        removeForTouchMonitorListener();
        return this.mPenSelectionController.clearAllPenSelection(getContext(), getRootView());
    }

    public void registerForTouchMonitorListener() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null && isMultiPenSelectionEnabled()) {
            if (mMotionEventMonitorListener != null) {
                removeForTouchMonitorListener();
            }
            MotionEventMonitor monitor = viewRootImpl.getMotionEventMonitor();
            mMotionEventMonitorListener = new TouchMonitorListener();
            monitor.registerMotionEventMonitor(mMotionEventMonitorListener);
        }
    }

    public void removeForTouchMonitorListener() {
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null && mMotionEventMonitorListener != null) {
            viewRootImpl.getMotionEventMonitor().unregisterMotionEventMonitor(mMotionEventMonitorListener);
            mMotionEventMonitorListener = null;
        }
    }

    public void enableMultiSelection(boolean flag) {
        this.mEnableMultiSelection = flag;
        if (flag) {
            registerForStylusPenEvent();
        } else {
            removeForStylusPenEvent();
        }
    }

    public boolean isEnableMultiSelection() {
        return this.mEnableMultiSelection;
    }

    private boolean selectCurrentWordForMultiSelection(int minOffset, int maxOffset) {
        CharSequence text = getTextForMultiSelection();
        if (text == null) {
            return false;
        }
        if (hasPasswordTransformationMethod()) {
            MultiSelection.selectAll((Spannable) text);
            return true;
        }
        int inputType = getInputType();
        int klass = inputType & 15;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        if (klass == 2 || klass == 3 || klass == 4 || variation == 16 || variation == 32 || variation == 208 || variation == 176) {
            MultiSelection.selectAll((Spannable) text);
            return true;
        }
        int selectionStart;
        int selectionEnd;
        URLSpan[] urlSpans = (URLSpan[]) ((Spanned) text).getSpans(minOffset, maxOffset, URLSpan.class);
        if (urlSpans.length >= 1) {
            URLSpan urlSpan = urlSpans[0];
            selectionStart = ((Spanned) text).getSpanStart(urlSpan);
            selectionEnd = ((Spanned) text).getSpanEnd(urlSpan);
        } else {
            if (this.mWordIteratorForMultiSelection == null) {
                this.mWordIteratorForMultiSelection = new WordIterator(getTextServicesLocale());
            }
            this.mWordIteratorForMultiSelection.setCharSequence(text, minOffset, maxOffset);
            selectionStart = this.mWordIteratorForMultiSelection.getBeginning(minOffset);
            selectionEnd = this.mWordIteratorForMultiSelection.getEnd(maxOffset);
            if (selectionStart == -1 || selectionEnd == -1 || selectionStart == selectionEnd) {
                return false;
            }
        }
        if (selectionStart < 0 || selectionEnd < 0) {
            return false;
        }
        if (selectionStart >= selectionEnd) {
            return false;
        }
        this.mIsTouchDown = false;
        MultiSelection.setSelection((Spannable) text, selectionStart, selectionEnd);
        return true;
    }

    private void registerForStylusPenEvent() {
        if (this.mStylusEventListener == null) {
            if (!this.mEnableMultiSelection) {
                removeForStylusPenEvent();
            } else if (!isDisabledStylusPenEvent()) {
                ViewTreeObserver observer = getViewTreeObserver();
                this.mStylusEventListener = new StylusEventListener(this);
                observer.addOnStylusButtonEventListener(getContext(), this.mStylusEventListener);
            }
        }
    }

    private void removeForStylusPenEvent() {
        if (this.mStylusEventListener != null) {
            getViewTreeObserver().removeOnStylusButtonEventListener(this.mStylusEventListener);
            this.mStylusEventListener = null;
        }
    }

    private boolean isDisabledStylusPenEvent() {
        String packName = this.mContext.getBasePackageName();
        if (packName == null || (!packName.equals("flipboard.boxer.app") && !packName.equals("com.android.systemui") && !packName.equals("com.android.keyguard") && !packName.equals(ZenModeConfig.SYSTEM_AUTHORITY))) {
            return false;
        }
        return true;
    }

    public void setNewActionPopupMenu(int menuId, boolean value) {
        switch (menuId) {
            case 5:
                this.mEnableClipboard = value;
                return;
            case 7:
                this.mEnableShare = value;
                return;
            case 8:
                this.mEnableWebSearch = value;
                return;
            case 9:
                this.mEnableDictionary = value;
                return;
            default:
                Log.e(LOG_TAG, "UnSupported menuID = " + menuId + ", value = " + value);
                return;
        }
    }

    public void enableNewActionPopupWindow(boolean flag) {
        this.mCanSelectText = flag;
    }

    public boolean isEnableNewActionPopupWindow() {
        if (this.mEditor != null) {
            return this.mCanSelectText;
        }
        return false;
    }

    public boolean getSecClipboardEnabled() {
        if (this.mIsSecClipboardEnabled == this.SEC_CLIPBOARD_UNKNOWN) {
            if (getClipboardExManager().isEnabled()) {
                this.mIsSecClipboardEnabled = this.SEC_CLIPBOARD_ENABLED;
            } else {
                this.mIsSecClipboardEnabled = this.SEC_CLIPBOARD_DISABLED;
            }
            if (this.mIsSecClipboardEnabled != this.SEC_CLIPBOARD_ENABLED) {
                return false;
            }
            return true;
        } else if (this.mIsSecClipboardEnabled == this.SEC_CLIPBOARD_ENABLED) {
            return true;
        } else {
            return false;
        }
    }

    public CharSequence getWBTextBuffer(boolean refresh) {
        if (refresh || this.mWBTextBuffer == null) {
            if (this.mText instanceof Editable) {
                this.mWBTextBuffer = this.mEditableFactory.newEditable(this.mText);
            } else if (this.mText instanceof Spannable) {
                this.mWBTextBuffer = this.mSpannableFactory.newSpannable(this.mText);
            } else {
                this.mWBTextBuffer = TextUtils.stringOrSpannedString(this.mText);
            }
        }
        return this.mWBTextBuffer;
    }

    public void setWBTextBuffer(CharSequence text) {
        this.mWBTextBuffer = text;
    }

    public boolean applyWBTextBuffer(boolean initBuffer) {
        if (this.mWBTextBuffer == null) {
            return false;
        }
        if (this.mText instanceof Editable) {
            ((Editable) this.mText).replace(0, this.mText.length(), this.mWBTextBuffer);
        } else {
            setText(this.mWBTextBuffer, BufferType.EDITABLE);
        }
        int selectionStart = Selection.getSelectionStart(this.mWBTextBuffer);
        int selectionEnd = Selection.getSelectionEnd(this.mWBTextBuffer);
        Log.d(LOG_TAG, "WB index " + selectionStart + " " + selectionEnd);
        if (selectionStart >= 0 && getEditableText() != null) {
            int length = getEditableText().length();
            Spannable editableText = getEditableText();
            if (selectionStart > length) {
                selectionStart = length;
            }
            if (selectionEnd <= length) {
                length = selectionEnd;
            }
            Selection.setSelection(editableText, selectionStart, length);
        }
        if (initBuffer) {
            this.mWBTextBuffer = null;
        }
        return true;
    }

    public void stopCursorBlink(boolean stop) {
        if (this.mEditor != null) {
            this.mEditor.stopCursorBlink(stop);
        }
    }

    public void setWBPositionListenerEnalbed(boolean enable) {
        if (this.mEditor != null) {
            this.mEditor.setWBPositionListenerEnalbed(enable);
        }
    }

    public void performWBEditorAction(int actionCode) {
        InputContentType ict;
        if (this.mEditor == null) {
            ict = null;
        } else {
            ict = this.mEditor.mInputContentType;
        }
        if (ict != null) {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (ict.onEditorActionListener != null && ict.onEditorActionListener.onEditorAction(this, actionCode, null)) {
                if (actionCode != 5 && actionCode != 7) {
                    return;
                }
                if (imm != null) {
                    imm.forceHideSoftInput();
                }
            }
            View v;
            if (actionCode == 5) {
                v = focusSearch(2);
                if (v == null) {
                    return;
                }
                if ((!(v instanceof TextView) || !((TextView) v).requestWritingBuddy()) && !v.requestFocus(2)) {
                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                }
                return;
            } else if (actionCode == 7) {
                v = focusSearch(1);
                if (v == null) {
                    return;
                }
                if ((!(v instanceof TextView) || !((TextView) v).requestWritingBuddy()) && !v.requestFocus(1)) {
                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                }
                return;
            } else if (actionCode == 6) {
                if (imm != null && imm.isActive(this)) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    return;
                }
                return;
            } else if (actionCode == 3 && getText() != null && getText().toString() != null && TextUtils.isEmpty(getText().toString())) {
                return;
            }
        }
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            long eventTime = SystemClock.uptimeMillis();
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(eventTime, eventTime, 0, 66, 0, 0, -1, 0, 22));
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(SystemClock.uptimeMillis(), eventTime, 1, 66, 0, 0, -1, 0, 22));
        }
    }

    private boolean requestWritingBuddy() {
        Log.d(LOG_TAG, "Request WritingBuddy");
        if (isWritingBuddyEnabled() && getWritingBuddy(true).show()) {
            return true;
        }
        return false;
    }

    public void extractEditorInfo(EditorInfo outAttrs) {
        if (outAttrs != null) {
            onCreateInputConnection(outAttrs);
            if (outAttrs.imeOptions == 0 || outAttrs.inputType == 0) {
                if (onCheckIsTextEditor() && isEnabled()) {
                    outAttrs.inputType = getInputType();
                    if (this.mEditor.mInputContentType != null) {
                        outAttrs.imeOptions = this.mEditor.mInputContentType.imeOptions;
                        outAttrs.privateImeOptions = this.mEditor.mInputContentType.privateImeOptions;
                        outAttrs.actionLabel = this.mEditor.mInputContentType.imeActionLabel;
                        outAttrs.actionId = this.mEditor.mInputContentType.imeActionId;
                        outAttrs.extras = this.mEditor.mInputContentType.extras;
                    } else {
                        outAttrs.imeOptions = 0;
                    }
                    View v = focusSearch(130);
                    if (v != null && (v instanceof EditText)) {
                        outAttrs.imeOptions |= 134217728;
                    }
                    if (focusSearch(33) != null) {
                        outAttrs.imeOptions |= 67108864;
                    }
                    if ((outAttrs.imeOptions & 255) == 0) {
                        if ((outAttrs.imeOptions & 134217728) != 0) {
                            outAttrs.imeOptions |= 5;
                        } else {
                            outAttrs.imeOptions |= 6;
                        }
                        if (!shouldAdvanceFocusOnEnter()) {
                            outAttrs.imeOptions |= 1073741824;
                        }
                    }
                    if (isMultilineInputType(outAttrs.inputType)) {
                        outAttrs.imeOptions |= 1073741824;
                    }
                    outAttrs.hintText = this.mHint;
                    if (this.mText instanceof Editable) {
                        outAttrs.initialSelStart = getSelectionStart();
                        outAttrs.initialSelEnd = getSelectionEnd();
                    }
                } else {
                    return;
                }
            }
            if (outAttrs.extras == null) {
                outAttrs.extras = new Bundle();
            }
            if (outAttrs.extras.getInt("maxLength", -1) < 0) {
                outAttrs.extras.putInt("maxLength", this.mWBMaxLength);
            }
        }
    }

    private void showMultiSelectPopupWindow() {
        if (this.mLayout != null && this.mMultiSelectPopupWindow != null) {
            this.mMultiSelectPopupWindow.changeCurrentSelectedView(this);
            this.mMultiSelectPopupWindow.showMultiSelectPopupWindow();
        }
    }

    private void hideMultiSelectPopupWindow() {
        this.mMultiSelectPopupWindow.hideMultiSelectPopupWindow();
    }

    public boolean onMultiSelectMenuItem(int id) {
        String source = this.mPenSelectionController.getPenSelectionContents(getContext(), getRootView());
        CharSequence text = getTextForMultiSelection();
        if (text == null) {
            Log.e(LOG_TAG, "getTextFormultiSelection() text is null");
            return false;
        }
        if (id != 16908381) {
            clearAllMultiSelection();
        }
        if (source == null) {
            Log.e(LOG_TAG, "Multi Selected Text String is null");
            return false;
        }
        switch (id) {
            case 16908381:
                int[] range = new int[2];
                if (!getVisibleTextRange(range)) {
                    range[0] = 0;
                    range[1] = text.length();
                }
                MultiSelection.setSelection((Spannable) text, range[0], range[1]);
                showMultiSelectPopupWindow();
                return true;
            case 16908382:
                if (!this.mRestrictionPolicy.isClipboardAllowed(true)) {
                    return false;
                }
                if (getSecClipboardEnabled()) {
                    ClipboardDataText clipdata = new ClipboardDataText();
                    clipdata.SetText(source);
                    setPrimaryClipEx(clipdata);
                } else {
                    setPrimaryClip(ClipData.newPlainText(null, new SpannableStringBuilder(source)));
                }
                return true;
            case 16908383:
                sendToDictionary(source, 0, source.length());
                return true;
            case 16908384:
                sendToTranslate(source);
                return true;
            case 16908385:
                Intent send = new Intent("android.intent.action.SEND");
                send.setType("text/plain");
                send.putExtra("android.intent.extra.TEXT", source);
                try {
                    Intent i = Intent.createChooser(send, getContext().getString(R.string.share));
                    i.addFlags(268435456);
                    getContext().startActivity(i);
                } catch (ActivityNotFoundException ex) {
                    Log.e(LOG_TAG, "Share failed");
                    Log.e(LOG_TAG, "ActivityNotFoundException", ex);
                }
                return true;
            default:
                return false;
        }
    }

    public void sendToDictionary(String word, int start, int end) {
        PackageManager pm = getContext().getPackageManager();
        Intent intent = new Intent("com.sec.android.app.dictionary.SEARCH");
        intent.addFlags(32);
        intent.putExtra("keyword", word);
        intent.putExtra("force", SmartFaceManager.TRUE);
        if (pm.queryBroadcastReceivers(intent, 32).size() > 0) {
            getContext().sendBroadcast(intent);
        }
    }

    public void sendToTranslate(String source) {
        String ACTION_SEC_TRANSLATE = "com.sec.android.app.translator.TRANSLATE_FOR_NON_ACTIVITY";
        String EXTRA_NAME_MODE = "mode";
        String EXTRA_NAME_SOURCE_TEXT = "source_text";
        String EXTRA_NAME_AUTO_START = "auto_start_translation";
        String EXTRA_NAME_CLIENT_ID = "client_id";
        String EXTRA_VALUE_VIEWER_MODE = "viewer";
        Intent intent = new Intent();
        intent.setAction("com.sec.android.app.translator.TRANSLATE_FOR_NON_ACTIVITY");
        intent.putExtra("mode", "viewer");
        intent.putExtra("source_text", source);
        intent.putExtra("auto_start_translation", true);
        intent.putExtra("client_id", "XGPPDdj5SG");
        intent.setFlags(268435456);
        try {
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(LOG_TAG, "Translate failed");
            Log.e(LOG_TAG, "ActivityNotFoundException", e);
        }
    }

    public void setHCTLetterSpacing() {
        if (onCheckIsTextEditor() && backupLetterSpacing() && this.mBackuppedLetterSpacing < HCT_LETTER_SPACING) {
            this.mTextPaint.setLetterSpacing(HCT_LETTER_SPACING);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void resetHCTLetterSpacing() {
        if (this.mBackuppedLetterSpacing - HCT_LETTER_SPACING_NOT_BACKUPPED >= 1.0E-4f) {
            this.mTextPaint.setLetterSpacing(this.mBackuppedLetterSpacing);
            this.mBackuppedLetterSpacing = HCT_LETTER_SPACING_NOT_BACKUPPED;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    private boolean backupLetterSpacing() {
        if (this.mBackuppedLetterSpacing - HCT_LETTER_SPACING_NOT_BACKUPPED >= 1.0E-4f) {
            return false;
        }
        this.mBackuppedLetterSpacing = this.mTextPaint.getLetterSpacing();
        return true;
    }
}
