package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker.OnTimeChangedListener;
import com.android.internal.R;
import com.samsung.android.writingbuddy.WritingBuddyImpl;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnTextUpdateListener;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnTextWritingListener;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.LocaleData;

class TimePickerSpinnerDelegate extends AbstractTimePickerDelegate {
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final int HOURS_IN_HALF_DAY = 12;
    private final int PICKER_HOUR = 0;
    private final int PICKER_MINUTE = 1;
    private boolean TW_DEBUG = false;
    private final Button mAmPmButton;
    private ImageButton mAmPmDecrementButton;
    private ImageButton mAmPmIncrementButton;
    private final NumberPicker mAmPmSpinner;
    private final EditText mAmPmSpinnerInput;
    private final String[] mAmPmStrings;
    private final TextView mDivider;
    private OnEditorActionListener mEditorActionListener = new OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                TimePickerSpinnerDelegate.this.updateInputState();
            }
            return false;
        }
    };
    private ImageButton mHourDecrementButton;
    private char mHourFormat;
    private ImageButton mHourIncrementButton;
    private final NumberPicker mHourSpinner;
    private final EditText mHourSpinnerInput;
    private boolean mHourWithTwoDigit;
    private boolean mIs24HourView;
    private boolean mIsAm;
    private boolean mIsEnabled = true;
    private ImageButton mMinuteDecrementButton;
    private ImageButton mMinuteIncrementButton;
    private final NumberPicker mMinuteSpinner;
    private final EditText mMinuteSpinnerInput;
    private EditText[] mPickerTexts = new EditText[3];
    private Calendar mTempCalendar;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private final int mHour;
        private final int mMinute;

        private SavedState(Parcelable superState, int hour, int minute) {
            super(superState);
            this.mHour = hour;
            this.mMinute = minute;
        }

        private SavedState(Parcel in) {
            super(in);
            this.mHour = in.readInt();
            this.mMinute = in.readInt();
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mHour);
            dest.writeInt(this.mMinute);
        }
    }

    private class TwKeyListener implements OnKeyListener {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (TimePickerSpinnerDelegate.this.TW_DEBUG) {
                Log.d("Picker", event.toString());
            }
            if (event.getAction() != 1) {
                return false;
            }
            switch (keyCode) {
                case 23:
                    if (TimePickerSpinnerDelegate.this.mDelegator.getResources().getConfiguration().keyboard == 3) {
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
        private int mId;
        private int mMaxLen;
        private int mNext;
        private String prevText;

        public TwTextWatcher(int maxLen, int id) {
            this.mMaxLen = maxLen;
            this.mId = id;
            this.mNext = this.mId + 1 >= 2 ? -1 : this.mId + 1;
        }

        public void afterTextChanged(Editable view) {
            if (TimePickerSpinnerDelegate.this.TW_DEBUG) {
                Log.d("Picker", "aftertextchanged: " + view.toString());
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (TimePickerSpinnerDelegate.this.TW_DEBUG) {
                Log.d("Picker", "beforeTextChanged: " + s + ", " + start + ", " + count + ", " + after);
            }
            this.prevText = s.toString();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TimePickerSpinnerDelegate.this.TW_DEBUG) {
                Log.d("Picker", "onTextChanged: " + this.prevText);
            }
            if (TimePickerSpinnerDelegate.this.TW_DEBUG) {
                Log.d("Picker", "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);
            }
            String tag = (String) TimePickerSpinnerDelegate.this.mPickerTexts[this.mId].getTag();
            if (tag != null && (tag.equals("onClick") || tag.equals("onLongClick"))) {
                TimePickerSpinnerDelegate.this.mPickerTexts[this.mId].setTag("");
            } else if (this.prevText.length() < s.length() && s.length() == this.mMaxLen && TimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                changeFocus();
            }
        }

        private void changeFocus() {
            if (!AccessibilityManager.getInstance(TimePickerSpinnerDelegate.this.mContext).isTouchExplorationEnabled()) {
                int next = (this.mId + 1) % 2;
                if (this.mNext >= 0) {
                    TimePickerSpinnerDelegate.this.mPickerTexts[this.mNext].requestFocus();
                    if (TimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                        TimePickerSpinnerDelegate.this.mPickerTexts[this.mId].clearFocus();
                    }
                }
            }
        }
    }

    public TimePickerSpinnerDelegate(TimePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        int layoutResourceId = a.getResourceId(12, R.layout.time_picker_legacy);
        a.recycle();
        LayoutInflater.from(this.mContext).inflate(layoutResourceId, this.mDelegator, true);
        this.mHourSpinner = (NumberPicker) delegator.findViewById(R.id.hour);
        this.mHourSpinner.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                if (!TimePickerSpinnerDelegate.this.is24HourView()) {
                    int oldValueNeedAmPmChange = 11;
                    int newValueNeedAmPmChange = 12;
                    if (TimePickerSpinnerDelegate.this.mHourFormat == 'K') {
                        oldValueNeedAmPmChange = 11;
                        newValueNeedAmPmChange = 0;
                    }
                    if ((oldVal == oldValueNeedAmPmChange && newVal == newValueNeedAmPmChange) || (oldVal == newValueNeedAmPmChange && newVal == oldValueNeedAmPmChange)) {
                        TimePickerSpinnerDelegate.this.mIsAm = !TimePickerSpinnerDelegate.this.mIsAm;
                        TimePickerSpinnerDelegate.this.updateAmPmControl();
                    }
                }
                TimePickerSpinnerDelegate.this.onTimeChanged();
            }
        });
        this.mHourSpinnerInput = (EditText) this.mHourSpinner.findViewById(R.id.numberpicker_input);
        this.mHourSpinner.twSetYearDateTimeInputMode();
        this.mHourSpinnerInput.setImeOptions(33554437);
        this.mHourSpinner.twSetMaxInputLength(2);
        this.mHourSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mDivider = (TextView) this.mDelegator.findViewById(R.id.divider);
        if (this.mDivider != null) {
            setDividerText();
        }
        this.mMinuteSpinner = (NumberPicker) this.mDelegator.findViewById(R.id.minute);
        this.mMinuteSpinner.twSetYearDateTimeInputMode();
        this.mMinuteSpinner.setMinValue(0);
        this.mMinuteSpinner.setMaxValue(59);
        this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
        this.mMinuteSpinner.setFormatter(NumberPicker.getTwoDigitFormatter());
        this.mMinuteSpinner.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                TimePickerSpinnerDelegate.this.onTimeChanged();
            }
        });
        this.mMinuteSpinnerInput = (EditText) this.mMinuteSpinner.findViewById(R.id.numberpicker_input);
        this.mMinuteSpinnerInput.setImeOptions(33554438);
        this.mMinuteSpinner.twSetMaxInputLength(2);
        this.mMinuteSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mAmPmStrings = getAmPmStrings(context);
        View amPmView = this.mDelegator.findViewById(R.id.amPm);
        if (amPmView instanceof Button) {
            this.mAmPmSpinner = null;
            this.mAmPmSpinnerInput = null;
            this.mAmPmButton = (Button) amPmView;
            this.mAmPmButton.setOnClickListener(new OnClickListener() {
                public void onClick(View button) {
                    button.requestFocus();
                    TimePickerSpinnerDelegate.this.mIsAm = !TimePickerSpinnerDelegate.this.mIsAm;
                    TimePickerSpinnerDelegate.this.updateAmPmControl();
                    TimePickerSpinnerDelegate.this.onTimeChanged();
                }
            });
        } else {
            this.mAmPmButton = null;
            this.mAmPmSpinner = (NumberPicker) amPmView;
            this.mAmPmSpinner.setMinValue(0);
            this.mAmPmSpinner.setMaxValue(1);
            this.mAmPmSpinner.setDisplayedValues(this.mAmPmStrings);
            this.mAmPmSpinner.setOnValueChangedListener(new OnValueChangeListener() {
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    TimePickerSpinnerDelegate.this.mIsAm = !TimePickerSpinnerDelegate.this.mIsAm;
                    TimePickerSpinnerDelegate.this.updateAmPmControl();
                    TimePickerSpinnerDelegate.this.onTimeChanged();
                }
            });
            this.mAmPmSpinnerInput = (EditText) this.mAmPmSpinner.findViewById(R.id.numberpicker_input);
            this.mAmPmSpinnerInput.setInputType(0);
            this.mAmPmSpinnerInput.setCursorVisible(false);
            this.mAmPmSpinnerInput.setFocusable(false);
            this.mAmPmSpinnerInput.setFocusableInTouchMode(false);
        }
        OnTouchListener TouchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 1:
                        TimePickerSpinnerDelegate.this.updateInputState();
                        break;
                }
                return false;
            }
        };
        this.mHourIncrementButton = this.mHourSpinner.getIncrementButton();
        this.mMinuteIncrementButton = this.mMinuteSpinner.getIncrementButton();
        this.mHourDecrementButton = this.mHourSpinner.getDecrementButton();
        this.mMinuteDecrementButton = this.mMinuteSpinner.getDecrementButton();
        if (this.mHourIncrementButton != null) {
            this.mHourIncrementButton.setOnTouchListener(TouchListener);
        }
        if (this.mMinuteIncrementButton != null) {
            this.mMinuteIncrementButton.setOnTouchListener(TouchListener);
        }
        if (this.mHourDecrementButton != null) {
            this.mHourDecrementButton.setOnTouchListener(TouchListener);
        }
        if (this.mMinuteDecrementButton != null) {
            this.mMinuteDecrementButton.setOnTouchListener(TouchListener);
        }
        if (!(amPmView instanceof Button)) {
            this.mAmPmIncrementButton = this.mAmPmSpinner.getIncrementButton();
            this.mAmPmDecrementButton = this.mAmPmSpinner.getDecrementButton();
            if (!(this.mAmPmIncrementButton == null || this.mAmPmDecrementButton == null)) {
                this.mAmPmIncrementButton.setNextFocusDownId(this.mAmPmDecrementButton.getId());
                this.mAmPmIncrementButton.setOnTouchListener(TouchListener);
                this.mAmPmDecrementButton.setNextFocusUpId(this.mAmPmIncrementButton.getId());
                this.mAmPmDecrementButton.setOnTouchListener(TouchListener);
            }
        }
        if (isAmPmAtStart()) {
            ViewGroup amPmParent = (ViewGroup) delegator.findViewById(R.id.timePickerLayout);
            amPmParent.removeView(amPmView);
            amPmParent.addView(amPmView, 0);
            MarginLayoutParams lp = (MarginLayoutParams) amPmView.getLayoutParams();
            int startMargin = lp.getMarginStart();
            int endMargin = lp.getMarginEnd();
            if (startMargin != endMargin) {
                lp.setMarginStart(endMargin);
                lp.setMarginEnd(startMargin);
            }
        }
        getHourFormatData();
        updateHourControl();
        updateMinuteControl();
        updateAmPmControl();
        setCurrentHour(this.mTempCalendar.get(11));
        setCurrentMinute(this.mTempCalendar.get(12));
        if (!isEnabled()) {
            setEnabled(false);
        }
        setContentDescriptions();
        if (this.mDelegator.getImportantForAccessibility() == 0) {
            this.mDelegator.setImportantForAccessibility(1);
        }
        setTextWatcher();
        if (isWritingBuddyFeatureEnabled()) {
            WritingBuddyImpl wb = new WritingBuddyImpl(delegator);
            if (is24HourView()) {
                wb.setBoardTemplate(15);
            } else if (this.mIsAm) {
                wb.setBoardTemplate(16);
            } else {
                wb.setBoardTemplate(17);
            }
            wb.setOnTextUpdateListener(new OnTextUpdateListener() {
                public CharSequence onTextUpdated(CharSequence text) {
                    TextView tv = (TextView) TimePickerSpinnerDelegate.this.mHourSpinner.findViewById(R.id.numberpicker_input);
                    String hour = tv != null ? tv.getText().toString() : "00";
                    tv = (TextView) TimePickerSpinnerDelegate.this.mMinuteSpinner.findViewById(R.id.numberpicker_input);
                    String curTime = "" + hour + "//" + (tv != null ? tv.getText().toString() : "00");
                    if (!TimePickerSpinnerDelegate.this.is24HourView()) {
                        String ampm = "AM";
                        if (TimePickerSpinnerDelegate.this.mAmPmButton != null) {
                            ampm = TimePickerSpinnerDelegate.this.mAmPmButton.getText().toString();
                        } else if (TimePickerSpinnerDelegate.this.mAmPmSpinner != null) {
                            tv = (TextView) TimePickerSpinnerDelegate.this.mAmPmSpinner.findViewById(R.id.numberpicker_input);
                            if (tv != null) {
                                ampm = tv.getText().toString();
                            }
                        }
                        curTime = curTime + "//" + ampm;
                        if (TimePickerSpinnerDelegate.this.mDelegator.getWritingBuddy(false) != null) {
                            if (TimePickerSpinnerDelegate.this.mIsAm) {
                                TimePickerSpinnerDelegate.this.mDelegator.getWritingBuddy(false).setBoardTemplate(16);
                            } else {
                                TimePickerSpinnerDelegate.this.mDelegator.getWritingBuddy(false).setBoardTemplate(17);
                            }
                        }
                    }
                    return curTime.trim();
                }
            });
            wb.setOnTextWritingListener(new OnTextWritingListener() {
                public void onTextReceived(CharSequence text) {
                    String[] numString = text.toString().split("//");
                    if (numString.length >= 2) {
                        int hour = -1;
                        int minute = -1;
                        boolean existAmPm = false;
                        String tmpStr = "";
                        for (int i = 0; i < numString.length; i++) {
                            tmpStr = numString[i] != null ? numString[i].trim() : "";
                            if (i == 0) {
                                try {
                                    hour = !TextUtils.isEmpty(tmpStr) ? Integer.parseInt(tmpStr) : -1;
                                } catch (NumberFormatException e) {
                                    hour = -1;
                                }
                            } else if (i == 1) {
                                try {
                                    minute = !TextUtils.isEmpty(tmpStr) ? Integer.parseInt(tmpStr) : -1;
                                } catch (NumberFormatException e2) {
                                    minute = -1;
                                }
                            } else if (i == 2) {
                                tmpStr = numString[i] != null ? numString[i].trim() : "";
                                if (tmpStr != "") {
                                    existAmPm = true;
                                }
                                try {
                                    String initAM = TimePickerSpinnerDelegate.this.mAmPmStrings[0];
                                    String initPM = TimePickerSpinnerDelegate.this.mAmPmStrings[1];
                                    if (existAmPm && (initAM.toLowerCase().equals(tmpStr.toLowerCase()) || "AM".toLowerCase().equals(tmpStr.toLowerCase()))) {
                                        TimePickerSpinnerDelegate.this.mIsAm = true;
                                    } else if (existAmPm) {
                                        if (initPM.toLowerCase().equals(tmpStr.toLowerCase()) || "PM".toLowerCase().equals(tmpStr.toLowerCase())) {
                                            TimePickerSpinnerDelegate.this.mIsAm = false;
                                        }
                                    }
                                } catch (Exception e3) {
                                    existAmPm = false;
                                }
                            }
                        }
                        if (hour >= 0) {
                            TimePickerSpinnerDelegate.this.mHourSpinner.setValue(hour);
                        }
                        if (minute >= 0) {
                            TimePickerSpinnerDelegate.this.mMinuteSpinner.setValue(minute);
                        }
                        if (!TimePickerSpinnerDelegate.this.is24HourView() && existAmPm) {
                            int index = TimePickerSpinnerDelegate.this.mIsAm ? 0 : 1;
                            if (TimePickerSpinnerDelegate.this.mAmPmSpinner != null) {
                                TimePickerSpinnerDelegate.this.mAmPmSpinner.setValue(index);
                            } else if (TimePickerSpinnerDelegate.this.mAmPmButton != null) {
                                TimePickerSpinnerDelegate.this.mAmPmButton.setText(TimePickerSpinnerDelegate.this.mAmPmStrings[index]);
                            }
                        }
                        TimePickerSpinnerDelegate.this.onTimeChanged();
                    }
                }
            });
            this.mDelegator.setWritingBuddyEnabled(this.mIsEnabled);
        }
    }

    private void getHourFormatData() {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int lengthPattern = bestDateTimePattern.length();
        this.mHourWithTwoDigit = false;
        int i = 0;
        while (i < lengthPattern) {
            char c = bestDateTimePattern.charAt(i);
            if (c == 'H' || c == DateFormat.HOUR || c == 'K' || c == DateFormat.HOUR_OF_DAY) {
                this.mHourFormat = c;
                if (i + 1 < lengthPattern && c == bestDateTimePattern.charAt(i + 1)) {
                    this.mHourWithTwoDigit = true;
                    return;
                }
                return;
            }
            i++;
        }
    }

    private boolean isAmPmAtStart() {
        return DateFormat.getBestDateTimePattern(this.mCurrentLocale, "hm").startsWith("a");
    }

    private void setDividerText() {
        CharSequence separatorText;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int hourIndex = bestDateTimePattern.lastIndexOf(72);
        if (hourIndex == -1) {
            hourIndex = bestDateTimePattern.lastIndexOf(104);
        }
        if (hourIndex == -1) {
            separatorText = ":";
        } else {
            int minuteIndex = bestDateTimePattern.indexOf(109, hourIndex + 1);
            if (minuteIndex == -1) {
                separatorText = Character.toString(bestDateTimePattern.charAt(hourIndex + 1));
            } else {
                separatorText = bestDateTimePattern.substring(hourIndex + 1, minuteIndex);
            }
        }
        this.mDivider.setText(separatorText);
    }

    public void setCurrentHour(int currentHour) {
        setCurrentHour(currentHour, true);
    }

    private void setCurrentHour(int currentHour, boolean notifyTimeChanged) {
        if (currentHour != getCurrentHour()) {
            if (!is24HourView()) {
                if (currentHour >= 12) {
                    this.mIsAm = false;
                    if (currentHour > 12) {
                        currentHour -= 12;
                    }
                } else {
                    this.mIsAm = true;
                    if (currentHour == 0) {
                        currentHour = 12;
                    }
                }
                updateAmPmControl();
            }
            this.mHourSpinner.setValue(currentHour);
            if (notifyTimeChanged) {
                onTimeChanged();
            }
        }
    }

    public int getCurrentHour() {
        int currentHour = this.mHourSpinner.getValue();
        if (is24HourView()) {
            return currentHour;
        }
        if (this.mIsAm) {
            return currentHour % 12;
        }
        return (currentHour % 12) + 12;
    }

    public void setCurrentMinute(int currentMinute) {
        if (currentMinute != getCurrentMinute()) {
            this.mMinuteSpinner.setValue(currentMinute);
            onTimeChanged();
        }
    }

    public int getCurrentMinute() {
        return this.mMinuteSpinner.getValue();
    }

    public void setIs24HourView(boolean is24HourView) {
        if (this.mIs24HourView != is24HourView) {
            int currentHour = getCurrentHour();
            this.mIs24HourView = is24HourView;
            getHourFormatData();
            updateHourControl();
            setCurrentHour(currentHour, false);
            updateMinuteControl();
            updateAmPmControl();
            if (isWritingBuddyFeatureEnabled()) {
                WritingBuddyImpl wb = this.mDelegator.getWritingBuddy(false);
                if (wb == null) {
                    return;
                }
                if (this.mIs24HourView) {
                    wb.setBoardTemplate(15);
                } else if (this.mIsAm) {
                    wb.setBoardTemplate(16);
                } else {
                    wb.setBoardTemplate(17);
                }
            }
        }
    }

    public boolean is24HourView() {
        return this.mIs24HourView;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mOnTimeChangedListener = onTimeChangedListener;
    }

    public void setEnabled(boolean enabled) {
        this.mMinuteSpinner.setEnabled(enabled);
        if (this.mDivider != null) {
            this.mDivider.setEnabled(enabled);
        }
        this.mHourSpinner.setEnabled(enabled);
        if (this.mAmPmSpinner != null) {
            this.mAmPmSpinner.setEnabled(enabled);
        } else {
            this.mAmPmButton.setEnabled(enabled);
        }
        this.mIsEnabled = enabled;
        if (isWritingBuddyFeatureEnabled()) {
            this.mDelegator.setWritingBuddyEnabled(enabled);
        }
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public int getBaseline() {
        return this.mHourSpinner.getBaseline();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        switch (layoutDirection) {
            case 1:
                this.mHourSpinnerInput.setImeOptions(33554438);
                this.mMinuteSpinnerInput.setImeOptions(33554437);
                return;
            default:
                this.mHourSpinnerInput.setImeOptions(33554437);
                this.mMinuteSpinnerInput.setImeOptions(33554438);
                return;
        }
    }

    public Parcelable onSaveInstanceState(Parcelable superState) {
        updateInputState();
        if (this.mHourSpinnerInput != null) {
            this.mHourSpinnerInput.clearFocus();
        }
        if (this.mMinuteSpinnerInput != null) {
            this.mMinuteSpinnerInput.clearFocus();
        }
        if (this.mAmPmSpinnerInput != null) {
            this.mAmPmSpinnerInput.clearFocus();
        }
        return new SavedState(superState, getCurrentHour(), getCurrentMinute());
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setCurrentHour(ss.getHour());
        setCurrentMinute(ss.getMinute());
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        int flags;
        if (this.mIs24HourView) {
            flags = 1 | 128;
        } else {
            flags = 1 | 64;
        }
        this.mTempCalendar.set(11, getCurrentHour());
        this.mTempCalendar.set(12, getCurrentMinute());
        event.getText().add(DateUtils.formatDateTime(this.mContext, this.mTempCalendar.getTimeInMillis(), flags));
    }

    private void updateInputState() {
        InputMethodManager inputMethodManager = InputMethodManager.peekInstance();
        if (inputMethodManager == null) {
            return;
        }
        if (inputMethodManager.isActive(this.mHourSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mHourSpinnerInput != null) {
                this.mHourSpinnerInput.clearFocus();
            }
        } else if (inputMethodManager.isActive(this.mMinuteSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mMinuteSpinnerInput != null) {
                this.mMinuteSpinnerInput.clearFocus();
            }
        } else if (inputMethodManager.isActive(this.mAmPmSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mAmPmSpinnerInput != null) {
                this.mAmPmSpinnerInput.clearFocus();
            }
        }
    }

    private void updateAmPmControl() {
        if (!is24HourView()) {
            int index = this.mIsAm ? 0 : 1;
            if (this.mAmPmSpinner != null) {
                this.mAmPmSpinner.setValue(index);
                this.mAmPmSpinner.setVisibility(0);
            } else {
                this.mAmPmButton.setText(this.mAmPmStrings[index]);
                this.mAmPmButton.setVisibility(0);
            }
        } else if (this.mAmPmSpinner != null) {
            this.mAmPmSpinner.setVisibility(8);
        } else {
            this.mAmPmButton.setVisibility(8);
        }
        this.mDelegator.sendAccessibilityEvent(4);
    }

    public void setCurrentLocale(Locale locale) {
        super.setCurrentLocale(locale);
        this.mTempCalendar = Calendar.getInstance(locale);
    }

    private void onTimeChanged() {
        this.mDelegator.sendAccessibilityEvent(4);
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getCurrentHour(), getCurrentMinute());
        }
    }

    private void updateHourControl() {
        if (is24HourView()) {
            if (this.mHourFormat == DateFormat.HOUR_OF_DAY) {
                this.mHourSpinner.setMinValue(1);
                this.mHourSpinner.setMaxValue(24);
            } else {
                this.mHourSpinner.setMinValue(0);
                this.mHourSpinner.setMaxValue(23);
            }
        } else if (this.mHourFormat == 'K') {
            this.mHourSpinner.setMinValue(0);
            this.mHourSpinner.setMaxValue(11);
        } else {
            this.mHourSpinner.setMinValue(1);
            this.mHourSpinner.setMaxValue(12);
        }
        this.mHourSpinner.setFormatter(this.mHourWithTwoDigit ? NumberPicker.getTwoDigitFormatter() : null);
    }

    private void updateMinuteControl() {
        if (is24HourView()) {
            this.mMinuteSpinnerInput.setImeOptions(6);
        } else if (this.mAmPmSpinner != null) {
            this.mMinuteSpinnerInput.setImeOptions(5);
        } else {
            this.mMinuteSpinnerInput.setImeOptions(6);
        }
        this.mMinuteSpinnerInput.setImeOptions(33554438);
    }

    private void setContentDescriptions() {
        trySetContentDescription(this.mMinuteSpinner, R.id.increment, R.string.time_picker_increment_minute_button);
        trySetContentDescription(this.mMinuteSpinner, R.id.decrement, R.string.time_picker_decrement_minute_button);
        trySetContentDescription(this.mHourSpinner, R.id.increment, R.string.time_picker_increment_hour_button);
        trySetContentDescription(this.mHourSpinner, R.id.decrement, R.string.time_picker_decrement_hour_button);
        if (this.mAmPmSpinner != null) {
            trySetContentDescription(this.mAmPmSpinner, R.id.increment, R.string.time_picker_increment_set_pm_button);
            trySetContentDescription(this.mAmPmSpinner, R.id.decrement, R.string.time_picker_decrement_set_am_button);
        }
    }

    private void trySetContentDescription(View root, int viewId, int contDescResId) {
        View target = root.findViewById(viewId);
        if (target != null) {
            target.setContentDescription(this.mContext.getString(contDescResId));
        }
    }

    public static String[] getAmPmStrings(Context context) {
        String[] result = new String[2];
        LocaleData d = LocaleData.get(context.getResources().getConfiguration().locale);
        if (isMeaLanguage()) {
            result[0] = d.amPm[0];
            result[1] = d.amPm[1];
        } else {
            result[0] = d.amPm[0].length() > 4 ? d.narrowAm : d.amPm[0];
            result[1] = d.amPm[1].length() > 4 ? d.narrowPm : d.amPm[1];
        }
        return result;
    }

    private static boolean isMeaLanguage() {
        String language = Locale.getDefault().getLanguage();
        if ("lo".equals(language) || "ar".equals(language) || "fa".equals(language) || "ur".equals(language)) {
            return true;
        }
        return false;
    }

    public EditText[] getTimePickerText() {
        return this.mPickerTexts;
    }

    private void setTextWatcher() {
        this.mPickerTexts[0] = this.mHourSpinner.getMText();
        this.mPickerTexts[1] = this.mMinuteSpinner.getMText();
        this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(2, 0));
        this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1));
        this.mPickerTexts[0].setOnKeyListener(new TwKeyListener());
        this.mPickerTexts[1].setOnKeyListener(new TwKeyListener());
    }

    private boolean isWritingBuddyFeatureEnabled() {
        return false;
    }
}
