package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View.BaseSavedState;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R;
import java.util.Locale;

public class TimePicker extends FrameLayout {
    private static final int MODE_CLOCK = 2;
    private static final int MODE_SPINNER = 1;
    private final TimePickerDelegate mDelegate;

    interface TimePickerDelegate {
        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        int getBaseline();

        int getCurrentHour();

        int getCurrentMinute();

        boolean is24HourView();

        boolean isEnabled();

        void onConfigurationChanged(Configuration configuration);

        void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onRestoreInstanceState(Parcelable parcelable);

        void onRtlPropertiesChanged(int i);

        Parcelable onSaveInstanceState(Parcelable parcelable);

        void setCurrentHour(int i);

        void setCurrentMinute(int i);

        void setEnabled(boolean z);

        void setIs24HourView(boolean z);

        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);

        void setValidationCallback(ValidationCallback validationCallback);
    }

    static abstract class AbstractTimePickerDelegate implements TimePickerDelegate {
        protected Context mContext;
        protected Locale mCurrentLocale;
        protected TimePicker mDelegator;
        protected OnTimeChangedListener mOnTimeChangedListener;
        protected ValidationCallback mValidationCallback;

        public AbstractTimePickerDelegate(TimePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        public void setCurrentLocale(Locale locale) {
            if (!locale.equals(this.mCurrentLocale)) {
                this.mCurrentLocale = locale;
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
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(TimePicker timePicker, int i, int i2);
    }

    public interface ValidationCallback {
        void onValidationChanged(boolean z);
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.timePickerStyle);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        int mode = a.getInt(8, 1);
        a.recycle();
        switch (mode) {
            case 2:
                this.mDelegate = new TimePickerClockDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                return;
            default:
                this.mDelegate = new TimePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                return;
        }
    }

    public void setHour(int hour) {
        this.mDelegate.setCurrentHour(hour);
    }

    public int getHour() {
        return this.mDelegate.getCurrentHour();
    }

    public void setMinute(int minute) {
        this.mDelegate.setCurrentMinute(minute);
    }

    public int getMinute() {
        return this.mDelegate.getCurrentMinute();
    }

    @Deprecated
    public void setCurrentHour(Integer currentHour) {
        setHour(currentHour.intValue());
    }

    @Deprecated
    public Integer getCurrentHour() {
        return Integer.valueOf(this.mDelegate.getCurrentHour());
    }

    @Deprecated
    public void setCurrentMinute(Integer currentMinute) {
        this.mDelegate.setCurrentMinute(currentMinute.intValue());
    }

    @Deprecated
    public Integer getCurrentMinute() {
        return Integer.valueOf(this.mDelegate.getCurrentMinute());
    }

    public void setIs24HourView(Boolean is24HourView) {
        if (is24HourView != null) {
            this.mDelegate.setIs24HourView(is24HourView.booleanValue());
        }
    }

    public boolean is24HourView() {
        return this.mDelegate.is24HourView();
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    public void setValidationCallback(ValidationCallback callback) {
        this.mDelegate.setValidationCallback(callback);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    public int getBaseline() {
        return this.mDelegate.getBaseline();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        this.mDelegate.onRtlPropertiesChanged(layoutDirection);
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

    public CharSequence getAccessibilityClassName() {
        return TimePicker.class.getName();
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }
}
