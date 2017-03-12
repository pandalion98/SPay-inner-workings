package android.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.TimePicker.ValidationCallback;
import java.text.DateFormat;
import java.util.Calendar;

public class TimePickerDialog extends AlertDialog implements OnClickListener, OnTimeChangedListener {
    private static final String HOUR = "hour";
    private static final String IS_24_HOUR = "is24hour";
    private static final String MINUTE = "minute";
    private final Calendar mCalendar;
    private final DateFormat mDateFormat;
    private InputMethodManager mImm;
    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private final TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;
    private final ValidationCallback mValidationCallback;

    public interface OnTimeSetListener {
        void onTimeSet(TimePicker timePicker, int i, int i2);
    }

    public TimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        this(context, 0, listener, hourOfDay, minute, is24HourView);
    }

    static int resolveDialogTheme(Context context, int resId) {
        if (resId != 0) {
            return resId;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.timePickerDialogTheme, outValue, true);
        return outValue.resourceId;
    }

    public TimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, resolveDialogTheme(context, themeResId));
        this.mValidationCallback = new ValidationCallback() {
            public void onValidationChanged(boolean valid) {
                Button positive = TimePickerDialog.this.getButton(-1);
                if (positive != null) {
                    positive.setEnabled(valid);
                }
            }
        };
        this.mTimeSetListener = listener;
        this.mInitialHourOfDay = hourOfDay;
        this.mInitialMinute = minute;
        this.mIs24HourView = is24HourView;
        Context themeContext = getContext();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.timePickerDialogTheme, outValue, true);
        int layoutResId = outValue.resourceId;
        View view = LayoutInflater.from(themeContext).inflate(17367313, null);
        setView(view);
        setButton(-1, themeContext.getString(R.string.ok), (OnClickListener) this);
        setButton(-2, themeContext.getString(R.string.cancel), (OnClickListener) this);
        setButtonPanelLayoutHint(1);
        this.mTimePicker = (TimePicker) view.findViewById(16909484);
        this.mTimePicker.setIs24HourView(Boolean.valueOf(this.mIs24HourView));
        this.mTimePicker.setCurrentHour(Integer.valueOf(this.mInitialHourOfDay));
        this.mTimePicker.setCurrentMinute(Integer.valueOf(this.mInitialMinute));
        this.mTimePicker.setOnTimeChangedListener(this);
        this.mTimePicker.setValidationCallback(this.mValidationCallback);
        this.mDateFormat = android.text.format.DateFormat.getTimeFormat(context);
        this.mCalendar = Calendar.getInstance();
        updateTitle(this.mInitialHourOfDay, this.mInitialMinute);
        this.mImm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void updateTitle(int hour, int minute) {
        this.mCalendar.set(11, hour);
        this.mCalendar.set(12, minute);
        setTitle(this.mDateFormat.format(this.mCalendar.getTime()));
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        updateTitle(hourOfDay, minute);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (this.mImm != null) {
            this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        switch (which) {
            case -2:
                cancel();
                return;
            case -1:
                if (this.mTimeSetListener != null) {
                    this.mTimePicker.clearFocus();
                    this.mTimeSetListener.onTimeSet(this.mTimePicker, this.mTimePicker.getCurrentHour().intValue(), this.mTimePicker.getCurrentMinute().intValue());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void updateTime(int hourOfDay, int minuteOfHour) {
        this.mTimePicker.setCurrentHour(Integer.valueOf(hourOfDay));
        this.mTimePicker.setCurrentMinute(Integer.valueOf(minuteOfHour));
    }

    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("hour", this.mTimePicker.getCurrentHour().intValue());
        state.putInt("minute", this.mTimePicker.getCurrentMinute().intValue());
        state.putBoolean(IS_24_HOUR, this.mTimePicker.is24HourView());
        return state;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt("hour");
        int minute = savedInstanceState.getInt("minute");
        this.mTimePicker.setIs24HourView(Boolean.valueOf(savedInstanceState.getBoolean(IS_24_HOUR)));
        this.mTimePicker.setCurrentHour(Integer.valueOf(hour));
        this.mTimePicker.setCurrentMinute(Integer.valueOf(minute));
        updateTitle(hour, minute);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCancelable && isShowing() && this.mWindow.shouldCloseOnTouch(this.mContext, event)) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(this.mTimePicker.getWindowToken(), 0);
            }
            this.mTimePicker.clearFocus();
        }
        return super.onTouchEvent(event);
    }
}
