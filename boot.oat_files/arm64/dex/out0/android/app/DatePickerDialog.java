package android.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.DatePicker.ValidationCallback;
import java.util.Calendar;

public class DatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private final Calendar mCalendar;
    private final DatePicker mDatePicker;
    private final OnDateSetListener mDateSetListener;
    private InputMethodManager mImm;
    private boolean mTitleNeedsUpdate;
    private final ValidationCallback mValidationCallback;

    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker, int i, int i2, int i3);
    }

    public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    static int resolveDialogTheme(Context context, int resid) {
        if (resid != 0) {
            return resid;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.datePickerDialogTheme, outValue, true);
        return outValue.resourceId;
    }

    public DatePickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, resolveDialogTheme(context, theme));
        this.mTitleNeedsUpdate = true;
        this.mValidationCallback = new ValidationCallback() {
            public void onValidationChanged(boolean valid) {
                Button positive = DatePickerDialog.this.getButton(-1);
                if (positive != null) {
                    positive.setEnabled(valid);
                }
            }
        };
        this.mDateSetListener = listener;
        this.mCalendar = Calendar.getInstance();
        Context themeContext = getContext();
        View view = LayoutInflater.from(themeContext).inflate(17367110, null);
        setView(view);
        setButton(-1, themeContext.getString(R.string.ok), (OnClickListener) this);
        setButton(-2, themeContext.getString(R.string.cancel), (OnClickListener) this);
        setButtonPanelLayoutHint(1);
        this.mDatePicker = (DatePicker) view.findViewById(16909194);
        this.mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        this.mDatePicker.setValidationCallback(this.mValidationCallback);
        this.mImm = (InputMethodManager) themeContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) {
        this.mDatePicker.init(year, month, day, this);
        updateTitle(year, month, day);
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
                if (this.mDateSetListener != null) {
                    this.mDatePicker.clearFocus();
                    this.mDateSetListener.onDateSet(this.mDatePicker, this.mDatePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public DatePicker getDatePicker() {
        return this.mDatePicker;
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        this.mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void updateTitle(int year, int month, int day) {
        if (!this.mDatePicker.getCalendarViewShown()) {
            this.mCalendar.set(1, year);
            this.mCalendar.set(2, month);
            this.mCalendar.set(5, day);
            setTitle(DateUtils.formatDateTime(this.mContext, this.mCalendar.getTimeInMillis(), 98326));
            this.mTitleNeedsUpdate = true;
        } else if (this.mTitleNeedsUpdate) {
            this.mTitleNeedsUpdate = false;
            setTitle(17040341);
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("year", this.mDatePicker.getYear());
        state.putInt("month", this.mDatePicker.getMonth());
        state.putInt("day", this.mDatePicker.getDayOfMonth());
        return state;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mDatePicker.init(savedInstanceState.getInt("year"), savedInstanceState.getInt("month"), savedInstanceState.getInt("day"), this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCancelable && isShowing() && this.mWindow.shouldCloseOnTouch(this.mContext, event)) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(this.mDatePicker.getWindowToken(), 0);
            }
            this.mDatePicker.clearFocus();
        }
        return super.onTouchEvent(event);
    }
}
