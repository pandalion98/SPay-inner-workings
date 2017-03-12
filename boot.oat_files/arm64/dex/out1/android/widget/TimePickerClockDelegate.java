package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan.VerbatimBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.widget.RadialTimePickerView.OnValueSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TimePicker.OnTimeChangedListener;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

class TimePickerClockDelegate extends AbstractTimePickerDelegate implements OnValueSelectedListener {
    static final int AM = 0;
    private static final int AMPM_INDEX = 2;
    private static final int[] ATTRS_DISABLED_ALPHA = new int[]{R.attr.disabledAlpha};
    private static final int[] ATTRS_TEXT_COLOR = new int[]{R.attr.textColor};
    private static final int ENABLE_PICKER_INDEX = 3;
    private static final int HOURS_IN_HALF_DAY = 12;
    private static final int HOUR_INDEX = 0;
    private static final int MINUTE_INDEX = 1;
    static final int PM = 1;
    private static final String TAG = "TimePickerClockDelegate";
    private boolean mAllowAutoAdvance;
    private int mAmKeyCode;
    private final CheckedTextView mAmLabel;
    private final View mAmPmLayout;
    private final String mAmText;
    private final OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hours /*16909486*/:
                    TimePickerClockDelegate.this.setCurrentItemShowing(0, true, true);
                    break;
                case R.id.minutes /*16909488*/:
                    TimePickerClockDelegate.this.setCurrentItemShowing(1, true, true);
                    break;
                case R.id.am_label /*16909490*/:
                    TimePickerClockDelegate.this.setAmOrPm(0);
                    break;
                case R.id.pm_label /*16909491*/:
                    TimePickerClockDelegate.this.setAmOrPm(1);
                    break;
                default:
                    return;
            }
            TimePickerClockDelegate.this.tryVibrate();
        }
    };
    private String mDeletedKeyFormat;
    private String mDoublePlaceholderText;
    private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus && TimePickerClockDelegate.this.mInKbMode && TimePickerClockDelegate.this.isTypedTimeFullyLegal()) {
                TimePickerClockDelegate.this.finishKbMode();
                if (TimePickerClockDelegate.this.mOnTimeChangedListener != null) {
                    TimePickerClockDelegate.this.mOnTimeChangedListener.onTimeChanged(TimePickerClockDelegate.this.mDelegator, TimePickerClockDelegate.this.mRadialTimePickerView.getCurrentHour(), TimePickerClockDelegate.this.mRadialTimePickerView.getCurrentMinute());
                }
            }
        }
    };
    private final View mHeaderView;
    private final TextView mHourView;
    private boolean mInKbMode;
    private int mInitialHourOfDay;
    private int mInitialMinute;
    private boolean mIs24HourView;
    private boolean mIsAmPmAtStart;
    private boolean mIsEnabled = true;
    private final OnKeyListener mKeyListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == 1) {
                return TimePickerClockDelegate.this.processKeyUp(keyCode);
            }
            return false;
        }
    };
    private boolean mLastAnnouncedIsHour;
    private CharSequence mLastAnnouncedText;
    private Node mLegalTimesTree;
    private final TextView mMinuteView;
    private char mPlaceholderText;
    private int mPmKeyCode;
    private final CheckedTextView mPmLabel;
    private final String mPmText;
    private final RadialTimePickerView mRadialTimePickerView;
    private String mSelectHours;
    private String mSelectMinutes;
    private final TextView mSeparatorView;
    private Calendar mTempCalendar;
    private ArrayList<Integer> mTypedTimes = new ArrayList();

    private static class ClickActionDelegate extends AccessibilityDelegate {
        private final AccessibilityAction mClickAction;

        public ClickActionDelegate(Context context, int resId) {
            this.mClickAction = new AccessibilityAction(16, context.getString(resId));
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(this.mClickAction);
        }
    }

    private class Node {
        private ArrayList<Node> mChildren = new ArrayList();
        private int[] mLegalKeys;

        public Node(int... legalKeys) {
            this.mLegalKeys = legalKeys;
        }

        public void addChild(Node child) {
            this.mChildren.add(child);
        }

        public boolean containsKey(int key) {
            for (int i : this.mLegalKeys) {
                if (i == key) {
                    return true;
                }
            }
            return false;
        }

        public Node canReach(int key) {
            if (this.mChildren == null) {
                return null;
            }
            Iterator i$ = this.mChildren.iterator();
            while (i$.hasNext()) {
                Node child = (Node) i$.next();
                if (child.containsKey(key)) {
                    return child;
                }
            }
            return null;
        }
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
        private final int mCurrentItemShowing;
        private final int mHour;
        private final boolean mInKbMode;
        private final boolean mIs24HourMode;
        private final int mMinute;
        private final ArrayList<Integer> mTypedTimes;

        private SavedState(Parcelable superState, int hour, int minute, boolean is24HourMode, boolean isKbMode, ArrayList<Integer> typedTimes, int currentItemShowing) {
            super(superState);
            this.mHour = hour;
            this.mMinute = minute;
            this.mIs24HourMode = is24HourMode;
            this.mInKbMode = isKbMode;
            this.mTypedTimes = typedTimes;
            this.mCurrentItemShowing = currentItemShowing;
        }

        private SavedState(Parcel in) {
            boolean z;
            boolean z2 = true;
            super(in);
            this.mHour = in.readInt();
            this.mMinute = in.readInt();
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            this.mIs24HourMode = z;
            if (in.readInt() != 1) {
                z2 = false;
            }
            this.mInKbMode = z2;
            this.mTypedTimes = in.readArrayList(getClass().getClassLoader());
            this.mCurrentItemShowing = in.readInt();
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        public boolean is24HourMode() {
            return this.mIs24HourMode;
        }

        public boolean inKbMode() {
            return this.mInKbMode;
        }

        public ArrayList<Integer> getTypesTimes() {
            return this.mTypedTimes;
        }

        public int getCurrentItemShowing() {
            return this.mCurrentItemShowing;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            int i2 = 1;
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mHour);
            dest.writeInt(this.mMinute);
            if (this.mIs24HourMode) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            if (!this.mInKbMode) {
                i2 = 0;
            }
            dest.writeInt(i2);
            dest.writeList(this.mTypedTimes);
            dest.writeInt(this.mCurrentItemShowing);
        }
    }

    public TimePickerClockDelegate(TimePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        Resources res = this.mContext.getResources();
        this.mSelectHours = res.getString(R.string.select_hours);
        this.mSelectMinutes = res.getString(R.string.select_minutes);
        String[] amPmStrings = TimePickerSpinnerDelegate.getAmPmStrings(context);
        this.mAmText = amPmStrings[0];
        this.mPmText = amPmStrings[1];
        View mainView = inflater.inflate(a.getResourceId(10, R.layout.time_picker_material), (ViewGroup) delegator);
        this.mHeaderView = mainView.findViewById(R.id.time_header);
        this.mHourView = (TextView) mainView.findViewById(R.id.hours);
        this.mHourView.setOnClickListener(this.mClickListener);
        this.mHourView.setAccessibilityDelegate(new ClickActionDelegate(context, R.string.select_hours));
        this.mSeparatorView = (TextView) mainView.findViewById(R.id.separator);
        this.mMinuteView = (TextView) mainView.findViewById(R.id.minutes);
        this.mMinuteView.setOnClickListener(this.mClickListener);
        this.mMinuteView.setAccessibilityDelegate(new ClickActionDelegate(context, R.string.select_minutes));
        this.mHourView.setMinWidth(computeStableWidth(this.mHourView, 24));
        this.mMinuteView.setMinWidth(computeStableWidth(this.mMinuteView, 60));
        SpannableStringBuilder amLabel = new SpannableStringBuilder().append(amPmStrings[0], new VerbatimBuilder(amPmStrings[0]).build(), 0);
        this.mAmPmLayout = mainView.findViewById(R.id.ampm_layout);
        this.mAmLabel = (CheckedTextView) this.mAmPmLayout.findViewById(R.id.am_label);
        this.mAmLabel.setText(obtainVerbatim(amPmStrings[0]));
        this.mAmLabel.setOnClickListener(this.mClickListener);
        this.mPmLabel = (CheckedTextView) this.mAmPmLayout.findViewById(R.id.pm_label);
        this.mPmLabel.setText(obtainVerbatim(amPmStrings[1]));
        this.mPmLabel.setOnClickListener(this.mClickListener);
        ColorStateList headerTextColor = null;
        int timeHeaderTextAppearance = a.getResourceId(1, 0);
        if (timeHeaderTextAppearance != 0) {
            TypedArray textAppearance = this.mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, timeHeaderTextAppearance);
            headerTextColor = applyLegacyColorFixes(textAppearance.getColorStateList(0));
            textAppearance.recycle();
        }
        if (headerTextColor == null) {
            headerTextColor = a.getColorStateList(11);
        }
        if (headerTextColor != null) {
            this.mHourView.setTextColor(headerTextColor);
            this.mSeparatorView.setTextColor(headerTextColor);
            this.mMinuteView.setTextColor(headerTextColor);
            this.mAmLabel.setTextColor(headerTextColor);
            this.mPmLabel.setTextColor(headerTextColor);
        }
        if (a.hasValueOrEmpty(0)) {
            this.mHeaderView.setBackground(a.getDrawable(0));
        }
        a.recycle();
        this.mRadialTimePickerView = (RadialTimePickerView) mainView.findViewById(R.id.radial_picker);
        setupListeners();
        this.mAllowAutoAdvance = true;
        this.mDoublePlaceholderText = res.getString(R.string.time_placeholder);
        this.mDeletedKeyFormat = res.getString(R.string.deleted_key);
        this.mPlaceholderText = this.mDoublePlaceholderText.charAt(0);
        this.mPmKeyCode = -1;
        this.mAmKeyCode = -1;
        generateLegalTimesTree();
        Calendar calendar = Calendar.getInstance(this.mCurrentLocale);
        initialize(calendar.get(11), calendar.get(12), false, 0);
    }

    private static final CharSequence obtainVerbatim(String text) {
        return new SpannableStringBuilder().append((CharSequence) text, new VerbatimBuilder(text).build(), 0);
    }

    private ColorStateList applyLegacyColorFixes(ColorStateList color) {
        if (color == null || color.hasState(R.attr.state_activated)) {
            return color;
        }
        int activatedColor;
        int defaultColor;
        if (color.hasState(R.attr.state_selected)) {
            activatedColor = color.getColorForState(StateSet.get(10), 0);
            defaultColor = color.getColorForState(StateSet.get(8), 0);
        } else {
            activatedColor = color.getDefaultColor();
            defaultColor = multiplyAlphaComponent(activatedColor, this.mContext.obtainStyledAttributes(ATTRS_DISABLED_ALPHA).getFloat(0, 0.3f));
        }
        if (activatedColor == 0 || defaultColor == 0) {
            return null;
        }
        stateSet = new int[2][];
        stateSet[0] = new int[]{R.attr.state_activated};
        stateSet[1] = new int[0];
        return new ColorStateList(stateSet, new int[]{activatedColor, defaultColor});
    }

    private int multiplyAlphaComponent(int color, float alphaMod) {
        return (((int) ((((float) ((color >> 24) & 255)) * alphaMod) + 0.5f)) << 24) | (color & 16777215);
    }

    private int computeStableWidth(TextView v, int maxNumber) {
        int maxWidth = 0;
        for (int i = 0; i < maxNumber; i++) {
            v.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}));
            v.measure(0, 0);
            int width = v.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private void initialize(int hourOfDay, int minute, boolean is24HourView, int index) {
        this.mInitialHourOfDay = hourOfDay;
        this.mInitialMinute = minute;
        this.mIs24HourView = is24HourView;
        this.mInKbMode = false;
        updateUI(index);
    }

    private void setupListeners() {
        this.mHeaderView.setOnKeyListener(this.mKeyListener);
        this.mHeaderView.setOnFocusChangeListener(this.mFocusListener);
        this.mHeaderView.setFocusable(true);
        this.mRadialTimePickerView.setOnValueSelectedListener(this);
    }

    private void updateUI(int index) {
        updateRadialPicker(index);
        updateHeaderAmPm();
        updateHeaderHour(this.mInitialHourOfDay, false);
        updateHeaderSeparator();
        updateHeaderMinute(this.mInitialMinute, false);
        this.mDelegator.invalidate();
    }

    private void updateRadialPicker(int index) {
        this.mRadialTimePickerView.initialize(this.mInitialHourOfDay, this.mInitialMinute, this.mIs24HourView);
        setCurrentItemShowing(index, false, true);
    }

    private void updateHeaderAmPm() {
        if (this.mIs24HourView) {
            this.mAmPmLayout.setVisibility(8);
            return;
        }
        setAmPmAtStart(DateFormat.getBestDateTimePattern(this.mCurrentLocale, "hm").startsWith("a"));
        updateAmPmLabelStates(this.mInitialHourOfDay < 12 ? 0 : 1);
    }

    private void setAmPmAtStart(boolean isAmPmAtStart) {
        if (this.mIsAmPmAtStart != isAmPmAtStart) {
            this.mIsAmPmAtStart = isAmPmAtStart;
            LayoutParams params = (LayoutParams) this.mAmPmLayout.getLayoutParams();
            if (!(params.getRule(1) == 0 && params.getRule(0) == 0)) {
                if (isAmPmAtStart) {
                    params.removeRule(1);
                    params.addRule(0, this.mHourView.getId());
                } else {
                    params.removeRule(0);
                    params.addRule(1, this.mMinuteView.getId());
                }
            }
            this.mAmPmLayout.setLayoutParams(params);
        }
    }

    public void setCurrentHour(int currentHour) {
        int i = 1;
        if (this.mInitialHourOfDay != currentHour) {
            this.mInitialHourOfDay = currentHour;
            updateHeaderHour(currentHour, true);
            updateHeaderAmPm();
            this.mRadialTimePickerView.setCurrentHour(currentHour);
            RadialTimePickerView radialTimePickerView = this.mRadialTimePickerView;
            if (this.mInitialHourOfDay < 12) {
                i = 0;
            }
            radialTimePickerView.setAmOrPm(i);
            this.mDelegator.invalidate();
            onTimeChanged();
        }
    }

    public int getCurrentHour() {
        int currentHour = this.mRadialTimePickerView.getCurrentHour();
        if (this.mIs24HourView) {
            return currentHour;
        }
        switch (this.mRadialTimePickerView.getAmOrPm()) {
            case 1:
                return (currentHour % 12) + 12;
            default:
                return currentHour % 12;
        }
    }

    public void setCurrentMinute(int currentMinute) {
        if (this.mInitialMinute != currentMinute) {
            this.mInitialMinute = currentMinute;
            updateHeaderMinute(currentMinute, true);
            this.mRadialTimePickerView.setCurrentMinute(currentMinute);
            this.mDelegator.invalidate();
            onTimeChanged();
        }
    }

    public int getCurrentMinute() {
        return this.mRadialTimePickerView.getCurrentMinute();
    }

    public void setIs24HourView(boolean is24HourView) {
        if (is24HourView != this.mIs24HourView) {
            this.mIs24HourView = is24HourView;
            generateLegalTimesTree();
            int hour = this.mRadialTimePickerView.getCurrentHour();
            this.mInitialHourOfDay = hour;
            updateHeaderHour(hour, false);
            updateHeaderAmPm();
            updateRadialPicker(this.mRadialTimePickerView.getCurrentItemShowing());
            this.mDelegator.invalidate();
        }
    }

    public boolean is24HourView() {
        return this.mIs24HourView;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener callback) {
        this.mOnTimeChangedListener = callback;
    }

    public void setEnabled(boolean enabled) {
        this.mHourView.setEnabled(enabled);
        this.mMinuteView.setEnabled(enabled);
        this.mAmLabel.setEnabled(enabled);
        this.mPmLabel.setEnabled(enabled);
        this.mRadialTimePickerView.setEnabled(enabled);
        this.mIsEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public int getBaseline() {
        return -1;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        updateUI(this.mRadialTimePickerView.getCurrentItemShowing());
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
    }

    public Parcelable onSaveInstanceState(Parcelable superState) {
        return new SavedState(superState, getCurrentHour(), getCurrentMinute(), is24HourView(), inKbMode(), getTypedTimes(), getCurrentItemShowing());
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setInKbMode(ss.inKbMode());
        setTypedTimes(ss.getTypesTimes());
        initialize(ss.getHour(), ss.getMinute(), ss.is24HourMode(), ss.getCurrentItemShowing());
        this.mRadialTimePickerView.invalidate();
        if (this.mInKbMode) {
            tryStartingKbMode(-1);
            this.mHourView.invalidate();
        }
    }

    public void setCurrentLocale(Locale locale) {
        super.setCurrentLocale(locale);
        this.mTempCalendar = Calendar.getInstance(locale);
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

    private void setInKbMode(boolean inKbMode) {
        this.mInKbMode = inKbMode;
    }

    private boolean inKbMode() {
        return this.mInKbMode;
    }

    private void setTypedTimes(ArrayList<Integer> typeTimes) {
        this.mTypedTimes = typeTimes;
    }

    private ArrayList<Integer> getTypedTimes() {
        return this.mTypedTimes;
    }

    private int getCurrentItemShowing() {
        return this.mRadialTimePickerView.getCurrentItemShowing();
    }

    private void onTimeChanged() {
        this.mDelegator.sendAccessibilityEvent(4);
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getCurrentHour(), getCurrentMinute());
        }
    }

    private void tryVibrate() {
        this.mDelegator.performHapticFeedback(4);
    }

    private void updateAmPmLabelStates(int amOrPm) {
        boolean isAm;
        boolean isPm;
        if (amOrPm == 0) {
            isAm = true;
        } else {
            isAm = false;
        }
        this.mAmLabel.setActivated(isAm);
        this.mAmLabel.setChecked(isAm);
        if (amOrPm == 1) {
            isPm = true;
        } else {
            isPm = false;
        }
        this.mPmLabel.setActivated(isPm);
        this.mPmLabel.setChecked(isPm);
    }

    public void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance) {
        switch (pickerIndex) {
            case 0:
                if (!this.mAllowAutoAdvance || !autoAdvance) {
                    updateHeaderHour(newValue, true);
                    break;
                }
                updateHeaderHour(newValue, false);
                setCurrentItemShowing(1, true, false);
                this.mDelegator.announceForAccessibility(newValue + ". " + this.mSelectMinutes);
                break;
                break;
            case 1:
                updateHeaderMinute(newValue, true);
                break;
            case 2:
                updateAmPmLabelStates(newValue);
                break;
            case 3:
                if (!isTypedTimeFullyLegal()) {
                    this.mTypedTimes.clear();
                }
                finishKbMode();
                break;
        }
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getCurrentHour(), getCurrentMinute());
        }
    }

    private void updateHeaderHour(int value, boolean announce) {
        String format;
        CharSequence text;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int lengthPattern = bestDateTimePattern.length();
        boolean hourWithTwoDigit = false;
        char hourFormat = '\u0000';
        int i = 0;
        while (i < lengthPattern) {
            char c = bestDateTimePattern.charAt(i);
            if (c == 'H' || c == DateFormat.HOUR || c == 'K' || c == DateFormat.HOUR_OF_DAY) {
                hourFormat = c;
                if (i + 1 < lengthPattern && c == bestDateTimePattern.charAt(i + 1)) {
                    hourWithTwoDigit = true;
                }
                if (hourWithTwoDigit) {
                    format = "%d";
                } else {
                    format = "%02d";
                }
                if (this.mIs24HourView) {
                    value = modulo12(value, hourFormat != 'K');
                } else if (hourFormat == DateFormat.HOUR_OF_DAY && value == 0) {
                    value = 24;
                }
                text = String.format(format, new Object[]{Integer.valueOf(value)});
                this.mHourView.setText(text);
                if (announce) {
                    tryAnnounceForAccessibility(text, true);
                }
            }
            i++;
        }
        if (hourWithTwoDigit) {
            format = "%d";
        } else {
            format = "%02d";
        }
        if (this.mIs24HourView) {
            if (hourFormat != 'K') {
            }
            value = modulo12(value, hourFormat != 'K');
        } else {
            value = 24;
        }
        text = String.format(format, new Object[]{Integer.valueOf(value)});
        this.mHourView.setText(text);
        if (announce) {
            tryAnnounceForAccessibility(text, true);
        }
    }

    private void tryAnnounceForAccessibility(CharSequence text, boolean isHour) {
        if (this.mLastAnnouncedIsHour != isHour || !text.equals(this.mLastAnnouncedText)) {
            this.mDelegator.announceForAccessibility(text);
            this.mLastAnnouncedText = text;
            this.mLastAnnouncedIsHour = isHour;
        }
    }

    private static int modulo12(int n, boolean startWithZero) {
        int value = n % 12;
        if (value != 0 || startWithZero) {
            return value;
        }
        return 12;
    }

    private void updateHeaderSeparator() {
        CharSequence separatorText;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int hIndex = lastIndexOfAny(bestDateTimePattern, new char[]{'H', DateFormat.HOUR, 'K', DateFormat.HOUR_OF_DAY});
        if (hIndex == -1) {
            separatorText = ":";
        } else {
            separatorText = Character.toString(bestDateTimePattern.charAt(hIndex + 1));
        }
        this.mSeparatorView.setText(separatorText);
    }

    private static int lastIndexOfAny(String str, char[] any) {
        if (lengthAny > 0) {
            for (int i = str.length() - 1; i >= 0; i--) {
                char c = str.charAt(i);
                for (char c2 : any) {
                    if (c == c2) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void updateHeaderMinute(int value, boolean announceForAccessibility) {
        if (value == 60) {
            value = 0;
        }
        CharSequence text = String.format(this.mCurrentLocale, "%02d", new Object[]{Integer.valueOf(value)});
        this.mMinuteView.setText(text);
        if (announceForAccessibility) {
            tryAnnounceForAccessibility(text, false);
        }
    }

    private void setCurrentItemShowing(int index, boolean animateCircle, boolean announce) {
        boolean z;
        boolean z2 = true;
        this.mRadialTimePickerView.setCurrentItemShowing(index, animateCircle);
        if (index == 0) {
            if (announce) {
                this.mDelegator.announceForAccessibility(this.mSelectHours);
            }
        } else if (announce) {
            this.mDelegator.announceForAccessibility(this.mSelectMinutes);
        }
        TextView textView = this.mHourView;
        if (index == 0) {
            z = true;
        } else {
            z = false;
        }
        textView.setActivated(z);
        TextView textView2 = this.mMinuteView;
        if (index != 1) {
            z2 = false;
        }
        textView2.setActivated(z2);
    }

    private void setAmOrPm(int amOrPm) {
        updateAmPmLabelStates(amOrPm);
        this.mRadialTimePickerView.setAmOrPm(amOrPm);
    }

    private boolean processKeyUp(int keyCode) {
        if (keyCode == 67) {
            if (this.mInKbMode && !this.mTypedTimes.isEmpty()) {
                String deletedKeyStr;
                int deleted = deleteLastTypedKey();
                if (deleted == getAmOrPmKeyCode(0)) {
                    deletedKeyStr = this.mAmText;
                } else if (deleted == getAmOrPmKeyCode(1)) {
                    deletedKeyStr = this.mPmText;
                } else {
                    deletedKeyStr = String.format("%d", new Object[]{Integer.valueOf(getValFromKeyCode(deleted))});
                }
                this.mDelegator.announceForAccessibility(String.format(this.mDeletedKeyFormat, new Object[]{deletedKeyStr}));
                updateDisplay(true);
            }
        } else if (keyCode == 7 || keyCode == 8 || keyCode == 9 || keyCode == 10 || keyCode == 11 || keyCode == 12 || keyCode == 13 || keyCode == 14 || keyCode == 15 || keyCode == 16 || (!this.mIs24HourView && (keyCode == getAmOrPmKeyCode(0) || keyCode == getAmOrPmKeyCode(1)))) {
            if (this.mInKbMode) {
                if (!addKeyIfLegal(keyCode)) {
                    return true;
                }
                updateDisplay(false);
                return true;
            } else if (this.mRadialTimePickerView == null) {
                Log.e(TAG, "Unable to initiate keyboard mode, TimePicker was null.");
                return true;
            } else {
                this.mTypedTimes.clear();
                tryStartingKbMode(keyCode);
                return true;
            }
        }
        return false;
    }

    private void tryStartingKbMode(int keyCode) {
        if (keyCode == -1 || addKeyIfLegal(keyCode)) {
            this.mInKbMode = true;
            onValidationChanged(false);
            updateDisplay(false);
            this.mRadialTimePickerView.setInputEnabled(false);
        }
    }

    private boolean addKeyIfLegal(int keyCode) {
        if (this.mIs24HourView && this.mTypedTimes.size() == 4) {
            return false;
        }
        if (!this.mIs24HourView && isTypedTimeFullyLegal()) {
            return false;
        }
        this.mTypedTimes.add(Integer.valueOf(keyCode));
        if (isTypedTimeLegalSoFar()) {
            int val = getValFromKeyCode(keyCode);
            this.mDelegator.announceForAccessibility(String.format("%d", new Object[]{Integer.valueOf(val)}));
            if (isTypedTimeFullyLegal()) {
                if (!this.mIs24HourView && this.mTypedTimes.size() <= 3) {
                    this.mTypedTimes.add(this.mTypedTimes.size() - 1, Integer.valueOf(7));
                    this.mTypedTimes.add(this.mTypedTimes.size() - 1, Integer.valueOf(7));
                }
                onValidationChanged(true);
            }
            return true;
        }
        deleteLastTypedKey();
        return false;
    }

    private boolean isTypedTimeLegalSoFar() {
        Node node = this.mLegalTimesTree;
        Iterator i$ = this.mTypedTimes.iterator();
        while (i$.hasNext()) {
            node = node.canReach(((Integer) i$.next()).intValue());
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isTypedTimeFullyLegal() {
        boolean z = false;
        if (this.mIs24HourView) {
            int[] values = getEnteredTime(null);
            if (values[0] < 0 || values[1] < 0 || values[1] >= 60) {
                return false;
            }
            return true;
        }
        if (this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(0))) || this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(1)))) {
            z = true;
        }
        return z;
    }

    private int deleteLastTypedKey() {
        int deleted = ((Integer) this.mTypedTimes.remove(this.mTypedTimes.size() - 1)).intValue();
        if (!isTypedTimeFullyLegal()) {
            onValidationChanged(false);
        }
        return deleted;
    }

    private void finishKbMode() {
        this.mInKbMode = false;
        if (!this.mTypedTimes.isEmpty()) {
            int[] values = getEnteredTime(null);
            this.mRadialTimePickerView.setCurrentHour(values[0]);
            this.mRadialTimePickerView.setCurrentMinute(values[1]);
            if (!this.mIs24HourView) {
                this.mRadialTimePickerView.setAmOrPm(values[2]);
            }
            this.mTypedTimes.clear();
        }
        updateDisplay(false);
        this.mRadialTimePickerView.setInputEnabled(true);
    }

    private void updateDisplay(boolean allowEmptyDisplay) {
        if (allowEmptyDisplay || !this.mTypedTimes.isEmpty()) {
            CharSequence minuteStr;
            boolean[] enteredZeros = new boolean[]{false, false};
            int[] values = getEnteredTime(enteredZeros);
            String hourFormat = enteredZeros[0] ? "%02d" : "%2d";
            String minuteFormat = enteredZeros[1] ? "%02d" : "%2d";
            CharSequence hourStr = values[0] == -1 ? this.mDoublePlaceholderText : String.format(hourFormat, new Object[]{Integer.valueOf(values[0])}).replace(' ', this.mPlaceholderText);
            if (values[1] == -1) {
                minuteStr = this.mDoublePlaceholderText;
            } else {
                minuteStr = String.format(minuteFormat, new Object[]{Integer.valueOf(values[1])}).replace(' ', this.mPlaceholderText);
            }
            this.mHourView.setText(hourStr);
            this.mHourView.setActivated(false);
            this.mMinuteView.setText(minuteStr);
            this.mMinuteView.setActivated(false);
            if (!this.mIs24HourView) {
                updateAmPmLabelStates(values[2]);
                return;
            }
            return;
        }
        int hour = this.mRadialTimePickerView.getCurrentHour();
        int minute = this.mRadialTimePickerView.getCurrentMinute();
        updateHeaderHour(hour, false);
        updateHeaderMinute(minute, false);
        if (!this.mIs24HourView) {
            updateAmPmLabelStates(hour < 12 ? 0 : 1);
        }
        setCurrentItemShowing(this.mRadialTimePickerView.getCurrentItemShowing(), true, true);
        onValidationChanged(true);
    }

    private int getValFromKeyCode(int keyCode) {
        switch (keyCode) {
            case 7:
                return 0;
            case 8:
                return 1;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                return 4;
            case 12:
                return 5;
            case 13:
                return 6;
            case 14:
                return 7;
            case 15:
                return 8;
            case 16:
                return 9;
            default:
                return -1;
        }
    }

    private int[] getEnteredTime(boolean[] enteredZeros) {
        int amOrPm = -1;
        int startIndex = 1;
        if (!this.mIs24HourView && isTypedTimeFullyLegal()) {
            int keyCode = ((Integer) this.mTypedTimes.get(this.mTypedTimes.size() - 1)).intValue();
            if (keyCode == getAmOrPmKeyCode(0)) {
                amOrPm = 0;
            } else if (keyCode == getAmOrPmKeyCode(1)) {
                amOrPm = 1;
            }
            startIndex = 2;
        }
        int minute = -1;
        int hour = -1;
        for (int i = startIndex; i <= this.mTypedTimes.size(); i++) {
            int val = getValFromKeyCode(((Integer) this.mTypedTimes.get(this.mTypedTimes.size() - i)).intValue());
            if (i == startIndex) {
                minute = val;
            } else if (i == startIndex + 1) {
                minute += val * 10;
                if (enteredZeros != null && val == 0) {
                    enteredZeros[1] = true;
                }
            } else if (i == startIndex + 2) {
                hour = val;
            } else if (i == startIndex + 3) {
                hour += val * 10;
                if (enteredZeros != null && val == 0) {
                    enteredZeros[0] = true;
                }
            }
        }
        return new int[]{hour, minute, amOrPm};
    }

    private int getAmOrPmKeyCode(int amOrPm) {
        if (this.mAmKeyCode == -1 || this.mPmKeyCode == -1) {
            KeyCharacterMap kcm = KeyCharacterMap.load(-1);
            CharSequence amText = this.mAmText.toLowerCase(this.mCurrentLocale);
            CharSequence pmText = this.mPmText.toLowerCase(this.mCurrentLocale);
            int N = Math.min(amText.length(), pmText.length());
            int i = 0;
            while (i < N) {
                if (amText.charAt(i) != pmText.charAt(i)) {
                    KeyEvent[] events = kcm.getEvents(new char[]{amText.charAt(i), pmText.charAt(i)});
                    if (events == null || events.length != 4) {
                        Log.e(TAG, "Unable to find keycodes for AM and PM.");
                    } else {
                        this.mAmKeyCode = events[0].getKeyCode();
                        this.mPmKeyCode = events[2].getKeyCode();
                    }
                } else {
                    i++;
                }
            }
        }
        if (amOrPm == 0) {
            return this.mAmKeyCode;
        }
        if (amOrPm == 1) {
            return this.mPmKeyCode;
        }
        return -1;
    }

    private void generateLegalTimesTree() {
        this.mLegalTimesTree = new Node(new int[0]);
        if (this.mIs24HourView) {
            int[] iArr = new int[6];
            Node node = new Node(7, 8, 9, 10, 11, 12);
            iArr = new int[10];
            node = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
            node.addChild(node);
            iArr = new int[2];
            Node firstDigit = new Node(7, 8);
            this.mLegalTimesTree.addChild(firstDigit);
            iArr = new int[6];
            node = new Node(7, 8, 9, 10, 11, 12);
            firstDigit.addChild(node);
            node.addChild(node);
            iArr = new int[4];
            node.addChild(new Node(13, 14, 15, 16));
            iArr = new int[4];
            node = new Node(13, 14, 15, 16);
            firstDigit.addChild(node);
            node.addChild(node);
            firstDigit = new Node(9);
            this.mLegalTimesTree.addChild(firstDigit);
            iArr = new int[4];
            node = new Node(7, 8, 9, 10);
            firstDigit.addChild(node);
            node.addChild(node);
            iArr = new int[2];
            node = new Node(11, 12);
            firstDigit.addChild(node);
            node.addChild(node);
            iArr = new int[7];
            firstDigit = new Node(10, 11, 12, 13, 14, 15, 16);
            this.mLegalTimesTree.addChild(firstDigit);
            firstDigit.addChild(node);
            return;
        }
        Node ampm = new Node(getAmOrPmKeyCode(0), getAmOrPmKeyCode(1));
        firstDigit = new Node(8);
        this.mLegalTimesTree.addChild(firstDigit);
        firstDigit.addChild(ampm);
        iArr = new int[3];
        node = new Node(7, 8, 9);
        firstDigit.addChild(node);
        node.addChild(ampm);
        iArr = new int[6];
        node = new Node(7, 8, 9, 10, 11, 12);
        node.addChild(node);
        node.addChild(ampm);
        iArr = new int[10];
        Node fourthDigit = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        node.addChild(fourthDigit);
        fourthDigit.addChild(ampm);
        iArr = new int[4];
        node = new Node(13, 14, 15, 16);
        node.addChild(node);
        node.addChild(ampm);
        iArr = new int[3];
        node = new Node(10, 11, 12);
        firstDigit.addChild(node);
        iArr = new int[10];
        node = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        node.addChild(node);
        node.addChild(ampm);
        iArr = new int[8];
        firstDigit = new Node(9, 10, 11, 12, 13, 14, 15, 16);
        this.mLegalTimesTree.addChild(firstDigit);
        firstDigit.addChild(ampm);
        iArr = new int[6];
        node = new Node(7, 8, 9, 10, 11, 12);
        firstDigit.addChild(node);
        iArr = new int[10];
        node = new Node(7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        node.addChild(node);
        node.addChild(ampm);
    }
}
