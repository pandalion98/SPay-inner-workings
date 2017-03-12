package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.GeneralUtil;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView.OnEditorActionListener;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.writingbuddy.WritingBuddyImpl;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnTextUpdateListener;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnTextWritingListener;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.ICU;

public class DatePicker extends FrameLayout {
    private static final String LOG_TAG = DatePicker.class.getSimpleName();
    private static final int MODE_CALENDAR = 2;
    private static final int MODE_SPINNER = 1;
    private final DatePickerDelegate mDelegate;

    interface DatePickerDelegate {
        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        CalendarView getCalendarView();

        boolean getCalendarViewShown();

        int getDayOfMonth();

        int getFirstDayOfWeek();

        Calendar getMaxDate();

        Calendar getMinDate();

        int getMonth();

        boolean getSpinnersShown();

        int getYear();

        void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener);

        boolean isEnabled();

        void onConfigurationChanged(Configuration configuration);

        void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onRestoreInstanceState(Parcelable parcelable);

        Parcelable onSaveInstanceState(Parcelable parcelable);

        void setCalendarViewShown(boolean z);

        void setEnabled(boolean z);

        void setFirstDayOfWeek(int i);

        void setMaxDate(long j);

        void setMinDate(long j);

        void setSpinnersShown(boolean z);

        void setValidationCallback(ValidationCallback validationCallback);

        void updateDate(int i, int i2, int i3);
    }

    static abstract class AbstractDatePickerDelegate implements DatePickerDelegate {
        protected Context mContext;
        protected Locale mCurrentLocale;
        protected DatePicker mDelegator;
        protected OnDateChangedListener mOnDateChangedListener;
        protected ValidationCallback mValidationCallback;

        public AbstractDatePickerDelegate(DatePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        protected void setCurrentLocale(Locale locale) {
            if (!locale.equals(this.mCurrentLocale)) {
                this.mCurrentLocale = locale;
                onLocaleChanged(locale);
            }
        }

        public void setValidationCallback(ValidationCallback callback) {
            this.mValidationCallback = callback;
        }

        protected void onValidationChanged(boolean valid) {
            if (this.mValidationCallback != null) {
                this.mValidationCallback.onValidationChanged(valid);
            }
        }

        protected void onLocaleChanged(Locale locale) {
        }
    }

    private static class DatePickerSpinnerDelegate extends AbstractDatePickerDelegate {
        private static final String DATE_FORMAT = "MM/dd/yyyy";
        private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;
        private static final boolean DEFAULT_ENABLED_STATE = true;
        private static final int DEFAULT_END_YEAR = 2100;
        private static final boolean DEFAULT_SPINNERS_SHOWN = true;
        private static final int DEFAULT_START_YEAR = 1900;
        private static final boolean TW_DEBUG = false;
        static final String productName = SystemProperties.get("ro.build.product");
        private final int FORMAT_DDMMYYYY = 1;
        private final int FORMAT_MMDDYYYY = 0;
        private final int FORMAT_YYYYMMDD = 2;
        private final int PICKER_DAY = 0;
        private final int PICKER_MONTH = 1;
        private final int PICKER_YEAR = 2;
        private boolean isMonthJan = false;
        private final CalendarView mCalendarView;
        private Calendar mCurrentDate;
        private final DateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        private ImageButton mDayDecrementButton;
        private ImageButton mDayIncrementButton;
        private final NumberPicker mDaySpinner;
        private final EditText mDaySpinnerInput;
        private OnEditorActionListener mEditorActionListener = new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 9999) {
                    DatePickerSpinnerDelegate.this.twLog("EditorAction 9999 arrived");
                    DatePickerSpinnerDelegate.this.isMonthJan = true;
                }
                return false;
            }
        };
        private boolean mIsEnabled = true;
        private boolean mIsParentThemeDeviceDefault = false;
        private Calendar mMaxDate;
        private Calendar mMinDate;
        private ImageButton mMonthDecrementButton;
        EditText mMonthEdit;
        private ImageButton mMonthIncrementButton;
        private final NumberPicker mMonthSpinner;
        private final EditText mMonthSpinnerInput;
        private int mNumberOfMonths;
        private EditText[] mPickerTexts = new EditText[3];
        private String[] mShortMonths;
        private final LinearLayout mSpinners;
        private Calendar mTempDate;
        private int mWBOriginSpninnerHeight = 0;
        private ImageButton mYearDecrementButton;
        private OnEditorActionListener mYearEditorActionListener = new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    AccessibilityManager manager = AccessibilityManager.getInstance(DatePickerSpinnerDelegate.this.mContext);
                    if (DatePickerSpinnerDelegate.this.mCalendarView != null && DatePickerSpinnerDelegate.this.getCalendarViewShown()) {
                        DatePickerSpinnerDelegate.this.updateCalendarView();
                        if (manager.isTouchExplorationEnabled() && manager.isEnabled()) {
                            DatePickerSpinnerDelegate.this.updateInputState();
                        }
                    }
                    if (!(manager.isTouchExplorationEnabled() && manager.isEnabled())) {
                        DatePickerSpinnerDelegate.this.updateInputState();
                    }
                }
                return false;
            }
        };
        private ImageButton mYearIncrementButton;
        private final NumberPicker mYearSpinner;
        private final EditText mYearSpinnerInput;

        private class TwKeyListener implements OnKeyListener {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                DatePickerSpinnerDelegate.this.twLog(event.toString());
                if (event.getAction() != 1) {
                    return false;
                }
                switch (keyCode) {
                    case 23:
                        if (DatePickerSpinnerDelegate.this.mDelegator.getResources().getConfiguration().keyboard == 3) {
                            return false;
                        }
                        break;
                    case 61:
                    case 66:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        public class TwTextWatcher implements TextWatcher {
            private int changedLen = 0;
            private boolean isMonth;
            private int mCheck;
            private int mId;
            private int mMaxLen;
            private int mNext;
            private String prevText;

            public TwTextWatcher(int maxLen, int id, boolean month) {
                this.mMaxLen = maxLen;
                this.mId = id;
                this.isMonth = month;
                this.mCheck = this.mId - 1;
                if (this.mCheck < 0) {
                    this.mCheck = 2;
                }
                this.mNext = this.mId + 1 >= 3 ? -1 : this.mId + 1;
            }

            public void afterTextChanged(Editable view) {
                DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "aftertextchanged: " + view.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "beforeTextChanged: " + s + ", " + start + ", " + count + ", " + after);
                this.prevText = s.toString();
                this.changedLen = after;
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "onTextChanged: " + this.prevText + " -> " + s);
                String tag = (String) DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].getTag();
                if (tag != null && (tag.equals("onClick") || tag.equals("onLongClick"))) {
                    DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "TAG exists: " + tag);
                } else if (this.isMonth) {
                    if (DatePickerSpinnerDelegate.this.usingNumericMonths()) {
                        DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "Samsung Keypad Num Month");
                        if (this.changedLen != 1) {
                            return;
                        }
                        if (s.length() == this.mMaxLen) {
                            if (DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                            }
                        } else if (s.length() > 0 && !s.toString().equals(SmartFaceManager.PAGE_BOTTOM) && !s.toString().equals(SmartFaceManager.PAGE_MIDDLE) && DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                            changeFocus();
                        }
                    } else if (DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused() && !this.prevText.equals(SmartFaceManager.PAGE_MIDDLE) && !this.prevText.equals(SmartFaceManager.PAGE_BOTTOM) && !this.prevText.equals("2") && !this.prevText.equals("3") && !this.prevText.equals("4") && !this.prevText.equals("5") && !this.prevText.equals("6") && !this.prevText.equals("7") && !this.prevText.equals("8") && !this.prevText.equals("9") && !this.prevText.equals("10") && !this.prevText.equals("11")) {
                        if (s.length() >= this.mMaxLen) {
                            if (!isMeaLanguage()) {
                                changeFocus();
                            } else if (isMonthStr(this.prevText)) {
                                changeFocus();
                            }
                        } else if ((isSwaLanguage() || isFarsiLanguage()) && s.length() > 0 && !isNumericStr(s)) {
                            changeFocus();
                        }
                    }
                } else if (this.prevText.length() < s.length() && s.length() == this.mMaxLen && this.changedLen == 1 && DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                    changeFocus();
                }
            }

            private boolean isSwaLanguage() {
                String language = DatePickerSpinnerDelegate.this.mCurrentLocale.getLanguage();
                if ("hi".equals(language) || "ta".equals(language) || "ml".equals(language) || "te".equals(language) || "or".equals(language) || "ne".equals(language) || "as".equals(language) || "bn".equals(language) || "gu".equals(language) || "si".equals(language) || "pa".equals(language) || "kn".equals(language) || "mr".equals(language)) {
                    return true;
                }
                return false;
            }

            private boolean isMeaLanguage() {
                String language = DatePickerSpinnerDelegate.this.mCurrentLocale.getLanguage();
                if ("ar".equals(language) || "fa".equals(language) || "ur".equals(language)) {
                    return true;
                }
                return false;
            }

            private boolean isFarsiLanguage() {
                if ("fa".equals(DatePickerSpinnerDelegate.this.mCurrentLocale.getLanguage())) {
                    return true;
                }
                return false;
            }

            private boolean isNumericStr(CharSequence s) {
                return Character.isDigit(s.charAt(0));
            }

            private boolean isMonthStr(String s) {
                boolean result = false;
                for (int i = 0; i < DatePickerSpinnerDelegate.this.mNumberOfMonths; i++) {
                    if (s.equals(DatePickerSpinnerDelegate.this.mShortMonths[i])) {
                        result = true;
                    }
                }
                return result;
            }

            private void changeFocus() {
                if (!AccessibilityManager.getInstance(DatePickerSpinnerDelegate.this.mContext).isTouchExplorationEnabled()) {
                    DatePickerSpinnerDelegate.this.twLog("[" + this.mId + "] " + "changeFocus() mNext : " + this.mNext + ", mCheck : " + this.mCheck);
                    if (this.mNext >= 0) {
                        if (!DatePickerSpinnerDelegate.this.mPickerTexts[this.mCheck].isFocused()) {
                            DatePickerSpinnerDelegate.this.mPickerTexts[this.mNext].requestFocus();
                        }
                        if (DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                            DatePickerSpinnerDelegate.this.mPickerTexts[this.mId].clearFocus();
                        }
                    }
                }
            }
        }

        DatePickerSpinnerDelegate(DatePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(delegator, context);
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
            TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
            boolean spinnersShown = attributesArray.getBoolean(6, true);
            boolean calendarViewShown = attributesArray.getBoolean(7, true);
            int startYear = attributesArray.getInt(1, DEFAULT_START_YEAR);
            int endYear = attributesArray.getInt(2, 2100);
            String minDate = attributesArray.getString(4);
            String maxDate = attributesArray.getString(5);
            int layoutResourceId = attributesArray.getResourceId(20, R.layout.date_picker_legacy);
            attributesArray.recycle();
            TypedValue outValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
            if (outValue.data != 0) {
                this.mIsParentThemeDeviceDefault = true;
            }
            ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(layoutResourceId, this.mDelegator, true);
            OnValueChangeListener anonymousClass1 = new OnValueChangeListener() {
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    DatePickerSpinnerDelegate.this.mTempDate.setTimeInMillis(DatePickerSpinnerDelegate.this.mCurrentDate.getTimeInMillis());
                    if (picker == DatePickerSpinnerDelegate.this.mDaySpinner) {
                        int maxDayOfMonth = DatePickerSpinnerDelegate.this.mTempDate.getActualMaximum(5);
                        if (oldVal == maxDayOfMonth && newVal == 1) {
                            DatePickerSpinnerDelegate.this.mTempDate.set(5, newVal);
                        } else if (oldVal == 1 && newVal == maxDayOfMonth) {
                            DatePickerSpinnerDelegate.this.mTempDate.set(5, newVal);
                        } else {
                            DatePickerSpinnerDelegate.this.mTempDate.add(5, newVal - oldVal);
                        }
                    } else if (picker == DatePickerSpinnerDelegate.this.mMonthSpinner) {
                        if (oldVal == 11 && newVal == 0) {
                            DatePickerSpinnerDelegate.this.mTempDate.set(2, newVal);
                        } else if (oldVal == 0 && newVal == 11) {
                            DatePickerSpinnerDelegate.this.mTempDate.set(2, newVal);
                        } else {
                            DatePickerSpinnerDelegate.this.mTempDate.add(2, newVal - oldVal);
                        }
                    } else if (picker == DatePickerSpinnerDelegate.this.mYearSpinner) {
                        DatePickerSpinnerDelegate.this.mTempDate.add(1, newVal - oldVal);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    DatePickerSpinnerDelegate.this.setDate(DatePickerSpinnerDelegate.this.mTempDate.get(1), DatePickerSpinnerDelegate.this.mTempDate.get(2), DatePickerSpinnerDelegate.this.mTempDate.get(5));
                    DatePickerSpinnerDelegate.this.updateSpinners();
                    DatePickerSpinnerDelegate.this.updateCalendarView();
                    DatePickerSpinnerDelegate.this.notifyDateChanged();
                }
            };
            this.mSpinners = (LinearLayout) this.mDelegator.findViewById(R.id.pickers);
            this.mCalendarView = (CalendarView) this.mDelegator.findViewById(R.id.calendar_view);
            this.mCalendarView.setOnDateChangeListener(new OnDateChangeListener() {
                public void onSelectedDayChange(CalendarView view, int year, int month, int monthDay) {
                    DatePickerSpinnerDelegate.this.setDate(year, month, monthDay);
                    DatePickerSpinnerDelegate.this.updateSpinners();
                    DatePickerSpinnerDelegate.this.notifyDateChanged();
                }
            });
            this.mDaySpinner = (NumberPicker) this.mDelegator.findViewById(R.id.day);
            this.mDaySpinner.setFormatter(NumberPicker.getTwoDigitFormatter());
            this.mDaySpinner.setOnLongPressUpdateInterval(100);
            this.mDaySpinner.setOnValueChangedListener(anonymousClass1);
            this.mDaySpinnerInput = (EditText) this.mDaySpinner.findViewById(R.id.numberpicker_input);
            this.mDaySpinner.twSetMaxInputLength(2);
            this.mDaySpinner.twSetYearDateTimeInputMode();
            this.mMonthSpinner = (NumberPicker) this.mDelegator.findViewById(R.id.month);
            this.mMonthSpinnerInput = (EditText) this.mMonthSpinner.findViewById(R.id.numberpicker_input);
            if (usingNumericMonths()) {
                this.mMonthSpinner.setMinValue(1);
                this.mMonthSpinner.setMaxValue(12);
                this.mMonthSpinner.setFormatter(NumberPicker.getTwoDigitFormatter());
                this.mMonthSpinner.twSetYearDateTimeInputMode();
                this.mMonthSpinner.twSetMaxInputLength(2);
            } else {
                this.mMonthSpinner.setMinValue(0);
                this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
                this.mMonthSpinner.setDisplayedValues(this.mShortMonths);
                this.mMonthSpinnerInput.setInputType(1);
                this.mMonthSpinner.twSetMonthInputMode();
            }
            this.mMonthSpinner.setOnLongPressUpdateInterval(200);
            this.mMonthSpinner.setOnValueChangedListener(anonymousClass1);
            this.mYearSpinner = (NumberPicker) this.mDelegator.findViewById(R.id.year);
            this.mYearSpinner.setOnLongPressUpdateInterval(100);
            this.mYearSpinner.setOnValueChangedListener(anonymousClass1);
            this.mYearSpinnerInput = (EditText) this.mYearSpinner.findViewById(R.id.numberpicker_input);
            String language = this.mCurrentLocale.getLanguage();
            if (("ar".equals(language) || "fa".equals(language) || "ur".equals(language)) && this.mIsParentThemeDeviceDefault && isPhone()) {
                this.mMonthSpinnerInput.setTextSize(1, 30.0f);
                this.mYearSpinnerInput.setTextSize(1, 30.0f);
                this.mDaySpinnerInput.setTextSize(1, 30.0f);
            }
            this.mYearSpinner.twSetMaxInputLength(4);
            this.mYearSpinner.twSetYearDateTimeInputMode();
            OnTouchListener TouchListener = new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case 1:
                            DatePickerSpinnerDelegate.this.updateInputState();
                            break;
                    }
                    return false;
                }
            };
            this.mDayIncrementButton = this.mDaySpinner.getIncrementButton();
            this.mMonthIncrementButton = this.mMonthSpinner.getIncrementButton();
            this.mYearIncrementButton = this.mYearSpinner.getIncrementButton();
            this.mDayDecrementButton = this.mDaySpinner.getDecrementButton();
            this.mMonthDecrementButton = this.mMonthSpinner.getDecrementButton();
            this.mYearDecrementButton = this.mYearSpinner.getDecrementButton();
            if (this.mDayIncrementButton != null) {
                this.mDayIncrementButton.setOnTouchListener(TouchListener);
            }
            if (this.mMonthIncrementButton != null) {
                this.mMonthIncrementButton.setOnTouchListener(TouchListener);
            }
            if (this.mYearIncrementButton != null) {
                this.mYearIncrementButton.setOnTouchListener(TouchListener);
            }
            if (this.mDayDecrementButton != null) {
                this.mDayDecrementButton.setOnTouchListener(TouchListener);
            }
            if (this.mMonthDecrementButton != null) {
                this.mMonthDecrementButton.setOnTouchListener(TouchListener);
            }
            if (this.mYearDecrementButton != null) {
                this.mYearDecrementButton.setOnTouchListener(TouchListener);
            }
            if (spinnersShown || calendarViewShown) {
                setSpinnersShown(spinnersShown);
                setCalendarViewShown(calendarViewShown);
            } else {
                setSpinnersShown(true);
            }
            this.mTempDate.clear();
            if (TextUtils.isEmpty(minDate)) {
                this.mTempDate.set(startYear, 0, 1);
            } else {
                if (!parseDate(minDate, this.mTempDate)) {
                    this.mTempDate.set(startYear, 0, 1);
                }
            }
            setMinDate(this.mTempDate.getTimeInMillis());
            this.mTempDate.clear();
            if (TextUtils.isEmpty(maxDate)) {
                this.mTempDate.set(endYear, 11, 31);
            } else {
                if (!parseDate(maxDate, this.mTempDate)) {
                    this.mTempDate.set(endYear, 11, 31);
                }
            }
            setMaxDate(this.mTempDate.getTimeInMillis());
            this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
            init(this.mCurrentDate.get(1), this.mCurrentDate.get(2), this.mCurrentDate.get(5), null);
            reorderSpinners();
            setContentDescriptions();
            if (this.mDelegator.getImportantForAccessibility() == 0) {
                this.mDelegator.setImportantForAccessibility(1);
            }
            if (this.mDelegator.isWritingBuddyFeatureEnabled()) {
                WritingBuddyImpl wb = new WritingBuddyImpl(this.mSpinners);
                wb.setBoardTemplate(18);
                wb.setOnTextUpdateListener(new OnTextUpdateListener() {
                    public CharSequence onTextUpdated(CharSequence text) {
                        String curDate = "";
                        String day = DatePickerSpinnerDelegate.this.mDaySpinnerInput != null ? DatePickerSpinnerDelegate.this.mDaySpinnerInput.getText().toString() : "01";
                        String month = DatePickerSpinnerDelegate.this.mMonthSpinnerInput != null ? DatePickerSpinnerDelegate.this.mMonthSpinnerInput.getText().toString() : "01";
                        String year = DatePickerSpinnerDelegate.this.mYearSpinnerInput != null ? DatePickerSpinnerDelegate.this.mYearSpinnerInput.getText().toString() : "2013";
                        int cnt = DatePickerSpinnerDelegate.this.mSpinners.getChildCount();
                        for (int i = 0; i < cnt; i++) {
                            View child = DatePickerSpinnerDelegate.this.mSpinners.getChildAt(i);
                            if (child != null) {
                                if (child.equals(DatePickerSpinnerDelegate.this.mDaySpinner)) {
                                    curDate = curDate + day;
                                } else if (child.equals(DatePickerSpinnerDelegate.this.mMonthSpinner)) {
                                    curDate = curDate + month;
                                } else if (child.equals(DatePickerSpinnerDelegate.this.mYearSpinner)) {
                                    curDate = curDate + year;
                                }
                                if (i != cnt - 1) {
                                    curDate = curDate + "//";
                                }
                            }
                        }
                        return curDate.trim();
                    }
                });
                wb.setOnTextWritingListener(new OnTextWritingListener() {
                    public void onTextReceived(CharSequence text) {
                        String[] numString = text.toString().split("//");
                        if (numString.length >= 3) {
                            int i;
                            int year = -1;
                            int month = -1;
                            int day = -1;
                            String tmpStr = "";
                            char[] order = ICU.getDateFormatOrder(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd"));
                            int spinnerCount = order.length;
                            for (int i2 = 0; i2 < spinnerCount; i2++) {
                                switch (order[i2]) {
                                    case 'M':
                                        month = DatePickerSpinnerDelegate.this.getValueFromWBResult(android.text.format.DateFormat.MONTH, numString[i2] != null ? numString[i2].trim() : "");
                                        break;
                                    case 'd':
                                        day = DatePickerSpinnerDelegate.this.getValueFromWBResult(android.text.format.DateFormat.DATE, numString[i2] != null ? numString[i2].trim() : "");
                                        break;
                                    case 'y':
                                        year = DatePickerSpinnerDelegate.this.getValueFromWBResult('y', numString[i2] != null ? numString[i2].trim() : "");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            DatePickerSpinnerDelegate datePickerSpinnerDelegate = DatePickerSpinnerDelegate.this;
                            if (year <= 0) {
                                year = DatePickerSpinnerDelegate.this.mCurrentDate.get(1);
                            }
                            if (month > 0) {
                                i = month - 1;
                            } else {
                                i = DatePickerSpinnerDelegate.this.mCurrentDate.get(2);
                            }
                            if (day <= 0) {
                                day = DatePickerSpinnerDelegate.this.mCurrentDate.get(5);
                            }
                            datePickerSpinnerDelegate.setDate(year, i, day);
                            DatePickerSpinnerDelegate.this.updateSpinners();
                            DatePickerSpinnerDelegate.this.updateCalendarView();
                            DatePickerSpinnerDelegate.this.notifyDateChanged();
                        }
                    }
                });
                this.mSpinners.setWritingBuddyEnabled(this.mIsEnabled);
                if (this.mIsParentThemeDeviceDefault) {
                    LayoutParams lp = this.mSpinners.getLayoutParams();
                    if (lp != null && this.mWBOriginSpninnerHeight == 0) {
                        this.mWBOriginSpninnerHeight = lp.height;
                    }
                }
            }
        }

        private boolean isPhone() {
            String characteristics = SystemProperties.get("ro.build.characteristics");
            if (characteristics == null || !characteristics.contains("tablet")) {
                return true;
            }
            return false;
        }

        public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
            setDate(year, monthOfYear, dayOfMonth);
            updateSpinners();
            updateCalendarView();
            this.mOnDateChangedListener = onDateChangedListener;
        }

        public void updateDate(int year, int month, int dayOfMonth) {
            if (isNewDate(year, month, dayOfMonth)) {
                setDate(year, month, dayOfMonth);
                updateSpinners();
                updateCalendarView();
                notifyDateChanged();
            }
        }

        public int getYear() {
            return this.mCurrentDate.get(1);
        }

        public int getMonth() {
            return this.mCurrentDate.get(2);
        }

        public int getDayOfMonth() {
            return this.mCurrentDate.get(5);
        }

        public void setFirstDayOfWeek(int firstDayOfWeek) {
            this.mCalendarView.setFirstDayOfWeek(firstDayOfWeek);
        }

        public int getFirstDayOfWeek() {
            return this.mCalendarView.getFirstDayOfWeek();
        }

        public void setMinDate(long minDate) {
            this.mTempDate.setTimeInMillis(minDate);
            if (this.mTempDate.get(1) != this.mMinDate.get(1) || this.mTempDate.get(6) == this.mMinDate.get(6)) {
                this.mMinDate.setTimeInMillis(minDate);
                this.mCalendarView.setMinDate(minDate);
                if (this.mCurrentDate.before(this.mMinDate)) {
                    this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
                    updateCalendarView();
                }
                updateSpinners();
            }
        }

        public Calendar getMinDate() {
            Calendar minDate = Calendar.getInstance();
            minDate.setTimeInMillis(this.mCalendarView.getMinDate());
            return minDate;
        }

        public void setMaxDate(long maxDate) {
            this.mTempDate.setTimeInMillis(maxDate);
            if (this.mTempDate.get(1) != this.mMaxDate.get(1) || this.mTempDate.get(6) == this.mMaxDate.get(6)) {
                this.mMaxDate.setTimeInMillis(maxDate);
                this.mCalendarView.setMaxDate(maxDate);
                if (this.mCurrentDate.after(this.mMaxDate)) {
                    this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
                    updateCalendarView();
                }
                updateSpinners();
            }
        }

        public Calendar getMaxDate() {
            Calendar maxDate = Calendar.getInstance();
            maxDate.setTimeInMillis(this.mCalendarView.getMaxDate());
            return maxDate;
        }

        public void setEnabled(boolean enabled) {
            this.mDaySpinner.setEnabled(enabled);
            this.mMonthSpinner.setEnabled(enabled);
            this.mYearSpinner.setEnabled(enabled);
            this.mCalendarView.setEnabled(enabled);
            this.mIsEnabled = enabled;
            if (this.mDelegator.isWritingBuddyFeatureEnabled()) {
                this.mSpinners.setWritingBuddyEnabled(enabled);
            }
        }

        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        public CalendarView getCalendarView() {
            return this.mCalendarView;
        }

        public void setCalendarViewShown(boolean shown) {
            this.mCalendarView.setVisibility(shown ? 0 : 8);
            if (this.mDelegator.isWritingBuddyFeatureEnabled() && this.mIsParentThemeDeviceDefault && GeneralUtil.isTablet()) {
                LayoutParams spinnnerLP = this.mSpinners.getLayoutParams();
                LayoutParams calendarLP = this.mCalendarView.getLayoutParams();
                if (this.mCalendarView.getVisibility() == 0) {
                    if (spinnnerLP != null && calendarLP != null && calendarLP.height > 0) {
                        this.mWBOriginSpninnerHeight = spinnnerLP.height;
                        spinnnerLP.height = calendarLP.height;
                    }
                } else if (this.mWBOriginSpninnerHeight != 0 && spinnnerLP != null && spinnnerLP.height != this.mWBOriginSpninnerHeight) {
                    spinnnerLP.height = this.mWBOriginSpninnerHeight;
                }
            }
        }

        public boolean getCalendarViewShown() {
            return this.mCalendarView.getVisibility() == 0;
        }

        public void setSpinnersShown(boolean shown) {
            this.mSpinners.setVisibility(shown ? 0 : 8);
        }

        public boolean getSpinnersShown() {
            return this.mSpinners.isShown();
        }

        public void onConfigurationChanged(Configuration newConfig) {
            setCurrentLocale(newConfig.locale);
        }

        public Parcelable onSaveInstanceState(Parcelable superState) {
            updateInputState();
            return new SavedState(superState, getYear(), getMonth(), getDayOfMonth());
        }

        public void onRestoreInstanceState(Parcelable state) {
            SavedState ss = (SavedState) state;
            setDate(ss.mYear, ss.mMonth, ss.mDay);
            updateSpinners();
            updateCalendarView();
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            onPopulateAccessibilityEvent(event);
            return true;
        }

        public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
            event.getText().add(DateUtils.formatDateTime(this.mContext, this.mCurrentDate.getTimeInMillis(), 20));
        }

        protected void setCurrentLocale(Locale locale) {
            super.setCurrentLocale(locale);
            this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
            this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
            this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
            this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
            this.mNumberOfMonths = this.mTempDate.getActualMaximum(2) + 1;
            this.mShortMonths = new DateFormatSymbols().getShortMonths();
            if (usingNumericMonths()) {
                this.mShortMonths = new String[this.mNumberOfMonths];
                for (int i = 0; i < this.mNumberOfMonths; i++) {
                    this.mShortMonths[i] = String.format("%d", new Object[]{Integer.valueOf(i + 1)});
                }
            }
        }

        private boolean usingNumericMonths() {
            if (productName == null || !productName.contains("ks02")) {
                return Character.isDigit(this.mShortMonths[0].charAt(0));
            }
            return true;
        }

        private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
            if (oldCalendar == null) {
                return Calendar.getInstance(locale);
            }
            long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }

        private void reorderSpinners() {
            this.mSpinners.removeAllViews();
            char[] order = ICU.getDateFormatOrder(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd"));
            int spinnerCount = order.length;
            for (int i = 0; i < spinnerCount; i++) {
                switch (order[i]) {
                    case 'M':
                        this.mSpinners.addView(this.mMonthSpinner);
                        setImeOptions(this.mMonthSpinner, spinnerCount, i);
                        break;
                    case 'd':
                        this.mSpinners.addView(this.mDaySpinner);
                        setImeOptions(this.mDaySpinner, spinnerCount, i);
                        break;
                    case 'y':
                        this.mSpinners.addView(this.mYearSpinner);
                        setImeOptions(this.mYearSpinner, spinnerCount, i);
                        break;
                    default:
                        throw new IllegalArgumentException(Arrays.toString(order));
                }
            }
            char c = order[0];
            if (c == android.text.format.DateFormat.MONTH) {
                setTextWatcher(0);
            } else if (c == android.text.format.DateFormat.DATE) {
                setTextWatcher(1);
            } else if (c == 'y') {
                setTextWatcher(2);
            }
        }

        private boolean parseDate(String date, Calendar outDate) {
            try {
                outDate.setTime(this.mDateFormat.parse(date));
                return true;
            } catch (ParseException e) {
                Log.w(DatePicker.LOG_TAG, "Date: " + date + " not in format: " + DATE_FORMAT);
                return false;
            }
        }

        private boolean isNewDate(int year, int month, int dayOfMonth) {
            return (this.mCurrentDate.get(1) == year && this.mCurrentDate.get(2) == dayOfMonth && this.mCurrentDate.get(5) == month) ? false : true;
        }

        private void setDate(int year, int month, int dayOfMonth) {
            this.mCurrentDate.set(year, month, dayOfMonth);
            if (this.mCurrentDate.before(this.mMinDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
            } else if (this.mCurrentDate.after(this.mMaxDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
            }
        }

        private void updateSpinners() {
            if (this.mCurrentDate.equals(this.mMinDate) || (this.mCurrentDate.get(1) == this.mMinDate.get(1) && this.mCurrentDate.get(2) == this.mMinDate.get(2) && this.mCurrentDate.get(5) == this.mMinDate.get(5))) {
                this.mDaySpinner.setMinValue(this.mCurrentDate.get(5));
                this.mDaySpinner.setMaxValue(this.mCurrentDate.getActualMaximum(5));
                this.mDaySpinner.setWrapSelectorWheel(false);
                this.mMonthSpinner.setDisplayedValues(null);
                if (usingNumericMonths()) {
                    this.mMonthSpinner.setMinValue(this.mCurrentDate.get(2) + 1);
                    this.mMonthSpinner.setMaxValue(this.mCurrentDate.getActualMaximum(2) + 1);
                } else {
                    this.mMonthSpinner.setMinValue(this.mCurrentDate.get(2));
                    this.mMonthSpinner.setMaxValue(this.mCurrentDate.getActualMaximum(2));
                }
                this.mMonthSpinner.setWrapSelectorWheel(false);
            } else if (this.mCurrentDate.equals(this.mMaxDate) || (this.mCurrentDate.get(1) == this.mMaxDate.get(1) && this.mCurrentDate.get(2) == this.mMaxDate.get(2) && this.mCurrentDate.get(5) == this.mMaxDate.get(5))) {
                this.mDaySpinner.setMinValue(this.mCurrentDate.getActualMinimum(5));
                this.mDaySpinner.setMaxValue(this.mCurrentDate.get(5));
                this.mDaySpinner.setWrapSelectorWheel(false);
                this.mMonthSpinner.setDisplayedValues(null);
                if (usingNumericMonths()) {
                    this.mMonthSpinner.setMinValue(this.mCurrentDate.getActualMinimum(2) + 1);
                    this.mMonthSpinner.setMaxValue(this.mCurrentDate.get(2) + 1);
                } else {
                    this.mMonthSpinner.setMinValue(this.mCurrentDate.getActualMinimum(2));
                    this.mMonthSpinner.setMaxValue(this.mCurrentDate.get(2));
                }
                this.mMonthSpinner.setWrapSelectorWheel(false);
            } else {
                this.mDaySpinner.setMinValue(1);
                this.mDaySpinner.setMaxValue(this.mCurrentDate.getActualMaximum(5));
                this.mDaySpinner.setWrapSelectorWheel(true);
                this.mMonthSpinner.setDisplayedValues(null);
                if (usingNumericMonths()) {
                    this.mMonthSpinner.setMinValue(1);
                    this.mMonthSpinner.setMaxValue(12);
                } else {
                    this.mMonthSpinner.setMinValue(0);
                    this.mMonthSpinner.setMaxValue(11);
                }
                this.mMonthSpinner.setWrapSelectorWheel(true);
            }
            String[] displayedValues = (String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1);
            if (usingNumericMonths()) {
                this.mMonthSpinner.setDisplayedValues(null);
            } else {
                this.mMonthSpinner.setDisplayedValues(displayedValues);
            }
            this.mYearSpinner.setMinValue(this.mMinDate.get(1));
            this.mYearSpinner.setMaxValue(this.mMaxDate.get(1));
            this.mYearSpinner.setWrapSelectorWheel(false);
            this.mYearSpinner.setValue(this.mCurrentDate.get(1));
            if (this.mYearSpinnerInput.hasFocus()) {
                Selection.setSelection(this.mYearSpinnerInput.getText(), this.mYearSpinnerInput.getText().length());
            }
            if (usingNumericMonths()) {
                this.mMonthSpinner.setValue(this.mCurrentDate.get(2) + 1);
            } else {
                this.mMonthSpinner.setValue(this.mCurrentDate.get(2));
            }
            this.mDaySpinner.setValue(this.mCurrentDate.get(5));
            if (usingNumericMonths()) {
                this.mMonthSpinnerInput.setRawInputType(2);
            }
        }

        private void updateCalendarView() {
            this.mCalendarView.setDate(this.mCurrentDate.getTimeInMillis(), false, true);
        }

        private void notifyDateChanged() {
            this.mDelegator.sendAccessibilityEvent(4);
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(this.mDelegator, getYear(), getMonth(), getDayOfMonth());
            }
        }

        private void setImeOptions(NumberPicker spinner, int spinnerCount, int spinnerIndex) {
            int imeOptions;
            if (spinnerIndex < spinnerCount - 1) {
                imeOptions = 5 | 33554432;
            } else {
                imeOptions = 6 | 33554432;
            }
            ((TextView) spinner.findViewById(R.id.numberpicker_input)).setImeOptions(imeOptions);
        }

        private void setContentDescriptions() {
            trySetContentDescription(this.mDaySpinner, R.id.increment, R.string.date_picker_increment_day_button);
            trySetContentDescription(this.mDaySpinner, R.id.decrement, R.string.date_picker_decrement_day_button);
            trySetContentDescription(this.mMonthSpinner, R.id.increment, R.string.date_picker_increment_month_button);
            trySetContentDescription(this.mMonthSpinner, R.id.decrement, R.string.date_picker_decrement_month_button);
            trySetContentDescription(this.mYearSpinner, R.id.increment, R.string.date_picker_increment_year_button);
            trySetContentDescription(this.mYearSpinner, R.id.decrement, R.string.date_picker_decrement_year_button);
        }

        private void trySetContentDescription(View root, int viewId, int contDescResId) {
            View target = root.findViewById(viewId);
            if (target != null) {
                target.setContentDescription(this.mContext.getString(contDescResId));
            }
        }

        private void updateInputState() {
            InputMethodManager inputMethodManager = InputMethodManager.peekInstance();
            if (inputMethodManager == null) {
                return;
            }
            if (inputMethodManager.isActive(this.mYearSpinnerInput)) {
                inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
                this.mYearSpinnerInput.clearFocus();
            } else if (inputMethodManager.isActive(this.mMonthSpinnerInput)) {
                inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
                this.mMonthSpinnerInput.clearFocus();
            } else if (inputMethodManager.isActive(this.mDaySpinnerInput)) {
                inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
                this.mDaySpinnerInput.clearFocus();
            }
        }

        private int getValueFromWBResult(char type, String value) {
            int result = -1;
            if (value == null || TextUtils.isEmpty(value.trim())) {
                return -1;
            }
            String valueString = value.trim();
            if (type == android.text.format.DateFormat.DATE) {
                try {
                    result = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    result = -1;
                }
            } else if (type == android.text.format.DateFormat.MONTH) {
                if (usingNumericMonths() || this.mShortMonths == null) {
                    try {
                        result = Integer.parseInt(valueString);
                    } catch (NumberFormatException e2) {
                        result = -1;
                    }
                } else {
                    int cnt = this.mShortMonths.length;
                    for (int i = 0; i < cnt; i++) {
                        if (valueString.toLowerCase().contains(this.mShortMonths[i].toLowerCase())) {
                            result = i + 1;
                        }
                    }
                }
            } else if (type == 'y') {
                try {
                    result = Integer.parseInt(valueString);
                } catch (NumberFormatException e3) {
                    result = -1;
                }
            }
            return result;
        }

        private void setTextWatcher(int format) {
            twLog("setTextWatcher() usingNumericMonths  : " + usingNumericMonths() + "format  : " + format);
            switch (format) {
                case 0:
                    this.mPickerTexts[0] = this.mMonthSpinner.getMText();
                    this.mPickerTexts[1] = this.mDaySpinner.getMText();
                    this.mPickerTexts[2] = this.mYearSpinner.getMText();
                    if (usingNumericMonths()) {
                        this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(2, 0, true));
                        this.mPickerTexts[0].setOnEditorActionListener(this.mEditorActionListener);
                    } else {
                        this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(3, 0, true));
                    }
                    this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, false));
                    this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(4, 2, false));
                    this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                    break;
                case 1:
                    this.mPickerTexts[0] = this.mDaySpinner.getMText();
                    this.mPickerTexts[1] = this.mMonthSpinner.getMText();
                    this.mPickerTexts[2] = this.mYearSpinner.getMText();
                    this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(2, 0, false));
                    if (usingNumericMonths()) {
                        this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, true));
                        this.mPickerTexts[1].setOnEditorActionListener(this.mEditorActionListener);
                    } else {
                        this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(3, 1, true));
                    }
                    this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(4, 2, false));
                    this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                    break;
                case 2:
                    this.mPickerTexts[0] = this.mYearSpinner.getMText();
                    this.mPickerTexts[1] = this.mMonthSpinner.getMText();
                    this.mPickerTexts[2] = this.mDaySpinner.getMText();
                    this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(4, 0, false));
                    if (usingNumericMonths()) {
                        this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, true));
                        this.mPickerTexts[1].setOnEditorActionListener(this.mEditorActionListener);
                    } else {
                        this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(3, 1, true));
                    }
                    this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(2, 2, false));
                    this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                    break;
            }
            this.mPickerTexts[0].setOnKeyListener(new TwKeyListener());
            this.mPickerTexts[1].setOnKeyListener(new TwKeyListener());
            this.mPickerTexts[2].setOnKeyListener(new TwKeyListener());
        }

        private void twLog(String msg) {
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(DatePicker datePicker, int i, int i2, int i3);
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private final int mDay;
        private final int mMonth;
        private final int mYear;

        private SavedState(Parcelable superState, int year, int month, int day) {
            super(superState);
            this.mYear = year;
            this.mMonth = month;
            this.mDay = day;
        }

        private SavedState(Parcel in) {
            super(in);
            this.mYear = in.readInt();
            this.mMonth = in.readInt();
            this.mDay = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mYear);
            dest.writeInt(this.mMonth);
            dest.writeInt(this.mDay);
        }
    }

    public interface ValidationCallback {
        void onValidationChanged(boolean z);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        int mode = a.getInt(16, 1);
        int firstDayOfWeek = a.getInt(3, 0);
        a.recycle();
        switch (mode) {
            case 2:
                this.mDelegate = createCalendarUIDelegate(context, attrs, defStyleAttr, defStyleRes);
                break;
            default:
                this.mDelegate = createSpinnerUIDelegate(context, attrs, defStyleAttr, defStyleRes);
                break;
        }
        if (firstDayOfWeek != 0) {
            setFirstDayOfWeek(firstDayOfWeek);
        }
    }

    private DatePickerDelegate createSpinnerUIDelegate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new DatePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    private DatePickerDelegate createCalendarUIDelegate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new DatePickerCalendarDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        this.mDelegate.init(year, monthOfYear, dayOfMonth, onDateChangedListener);
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        this.mDelegate.updateDate(year, month, dayOfMonth);
    }

    public int getYear() {
        return this.mDelegate.getYear();
    }

    public int getMonth() {
        return this.mDelegate.getMonth();
    }

    public int getDayOfMonth() {
        return this.mDelegate.getDayOfMonth();
    }

    public long getMinDate() {
        return this.mDelegate.getMinDate().getTimeInMillis();
    }

    public void setMinDate(long minDate) {
        this.mDelegate.setMinDate(minDate);
    }

    public long getMaxDate() {
        return this.mDelegate.getMaxDate().getTimeInMillis();
    }

    public void setMaxDate(long maxDate) {
        this.mDelegate.setMaxDate(maxDate);
    }

    public void setValidationCallback(ValidationCallback callback) {
        this.mDelegate.setValidationCallback(callback);
    }

    public void setEnabled(boolean enabled) {
        if (this.mDelegate.isEnabled() != enabled) {
            super.setEnabled(enabled);
            this.mDelegate.setEnabled(enabled);
        }
    }

    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        this.mDelegate.onPopulateAccessibilityEvent(event);
    }

    public CharSequence getAccessibilityClassName() {
        return DatePicker.class.getName();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < 1 || firstDayOfWeek > 7) {
            throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        this.mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    public int getFirstDayOfWeek() {
        return this.mDelegate.getFirstDayOfWeek();
    }

    public boolean getCalendarViewShown() {
        return this.mDelegate.getCalendarViewShown();
    }

    public CalendarView getCalendarView() {
        return this.mDelegate.getCalendarView();
    }

    public void setCalendarViewShown(boolean shown) {
        this.mDelegate.setCalendarViewShown(shown);
    }

    public boolean getSpinnersShown() {
        return this.mDelegate.getSpinnersShown();
    }

    public void setSpinnersShown(boolean shown) {
        this.mDelegate.setSpinnersShown(shown);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    protected Parcelable onSaveInstanceState() {
        return this.mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    protected void onRestoreInstanceState(Parcelable state) {
        BaseSavedState ss = (BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDelegate.onRestoreInstanceState(ss);
    }
}
