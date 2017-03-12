package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.MathUtils;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.android.internal.R;
import com.android.internal.widget.ExploreByTouchHelper;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class SimpleMonthView extends View {
    private static final int DAYS_IN_WEEK = 7;
    private static final String DAY_OF_WEEK_FORMAT = "EEEEE";
    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final String DEFAULT_TITLE_FORMAT = "MMMMy";
    private static final int DEFAULT_WEEK_START = 1;
    private static final int MAX_WEEKS_IN_MONTH = 6;
    private int mActivatedDay;
    private final Calendar mCalendar;
    private int mCellWidth;
    private final NumberFormat mDayFormatter;
    private int mDayHeight;
    private final Paint mDayHighlightPaint;
    private final SimpleDateFormat mDayOfWeekFormatter;
    private int mDayOfWeekHeight;
    private final Calendar mDayOfWeekLabelCalendar;
    private final TextPaint mDayOfWeekPaint;
    private int mDayOfWeekStart;
    private final TextPaint mDayPaint;
    private final Paint mDaySelectorPaint;
    private int mDaySelectorRadius;
    private ColorStateList mDayTextColor;
    private int mDaysInMonth;
    private final int mDesiredCellWidth;
    private final int mDesiredDayHeight;
    private final int mDesiredDayOfWeekHeight;
    private final int mDesiredDaySelectorRadius;
    private final int mDesiredMonthHeight;
    private int mEnabledDayEnd;
    private int mEnabledDayStart;
    private int mMonth;
    private int mMonthHeight;
    private final TextPaint mMonthPaint;
    private OnDayClickListener mOnDayClickListener;
    private int mPaddedHeight;
    private int mPaddedWidth;
    private CharSequence mTitle;
    private final SimpleDateFormat mTitleFormatter;
    private int mToday;
    private final MonthViewTouchHelper mTouchHelper;
    private int mTouchedItem;
    private int mWeekStart;
    private int mYear;

    public interface OnDayClickListener {
        void onDayClick(SimpleMonthView simpleMonthView, Calendar calendar);
    }

    private class MonthViewTouchHelper extends ExploreByTouchHelper {
        private static final String DATE_FORMAT = "dd MMMM yyyy";
        private final Calendar mTempCalendar = Calendar.getInstance();
        private final Rect mTempRect = new Rect();

        public MonthViewTouchHelper(View host) {
            super(host);
        }

        protected int getVirtualViewAt(float x, float y) {
            int day = SimpleMonthView.this.getDayAtLocation((int) (x + 0.5f), (int) (0.5f + y));
            return day != -1 ? day : Integer.MIN_VALUE;
        }

        protected void getVisibleVirtualViews(IntArray virtualViewIds) {
            for (int day = 1; day <= SimpleMonthView.this.mDaysInMonth; day++) {
                virtualViewIds.add(day);
            }
        }

        protected void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            event.setContentDescription(getDayDescription(virtualViewId));
        }

        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
            if (SimpleMonthView.this.getBoundsForDay(virtualViewId, this.mTempRect)) {
                node.setText(getDayText(virtualViewId));
                node.setContentDescription(getDayDescription(virtualViewId));
                node.setBoundsInParent(this.mTempRect);
                boolean isDayEnabled = SimpleMonthView.this.isDayEnabled(virtualViewId);
                if (isDayEnabled) {
                    node.addAction(AccessibilityAction.ACTION_CLICK);
                }
                node.setEnabled(isDayEnabled);
                if (virtualViewId == SimpleMonthView.this.mActivatedDay) {
                    node.setChecked(true);
                    return;
                }
                return;
            }
            this.mTempRect.setEmpty();
            node.setContentDescription("");
            node.setBoundsInParent(this.mTempRect);
            node.setVisibleToUser(false);
        }

        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            switch (action) {
                case 16:
                    return SimpleMonthView.this.onDayClicked(virtualViewId);
                default:
                    return false;
            }
        }

        private CharSequence getDayDescription(int id) {
            if (!SimpleMonthView.this.isValidDayOfMonth(id)) {
                return "";
            }
            this.mTempCalendar.set(SimpleMonthView.this.mYear, SimpleMonthView.this.mMonth, id);
            return DateFormat.format(DATE_FORMAT, this.mTempCalendar.getTimeInMillis());
        }

        private CharSequence getDayText(int id) {
            if (SimpleMonthView.this.isValidDayOfMonth(id)) {
                return SimpleMonthView.this.mDayFormatter.format((long) id);
            }
            return null;
        }
    }

    public SimpleMonthView(Context context) {
        this(context, null);
    }

    public SimpleMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMonthPaint = new TextPaint();
        this.mDayOfWeekPaint = new TextPaint();
        this.mDayPaint = new TextPaint();
        this.mDaySelectorPaint = new Paint();
        this.mDayHighlightPaint = new Paint();
        this.mCalendar = Calendar.getInstance();
        this.mDayOfWeekLabelCalendar = Calendar.getInstance();
        this.mActivatedDay = -1;
        this.mToday = -1;
        this.mWeekStart = 1;
        this.mEnabledDayStart = 1;
        this.mEnabledDayEnd = 31;
        this.mTouchedItem = -1;
        Resources res = context.getResources();
        this.mDesiredMonthHeight = res.getDimensionPixelSize(R.dimen.date_picker_month_height);
        this.mDesiredDayOfWeekHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_height);
        this.mDesiredDayHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_height);
        this.mDesiredCellWidth = res.getDimensionPixelSize(R.dimen.date_picker_day_width);
        this.mDesiredDaySelectorRadius = res.getDimensionPixelSize(R.dimen.date_picker_day_selector_radius);
        this.mTouchHelper = new MonthViewTouchHelper(this);
        setAccessibilityDelegate(this.mTouchHelper);
        setImportantForAccessibility(1);
        Locale locale = res.getConfiguration().locale;
        this.mTitleFormatter = new SimpleDateFormat(DateFormat.getBestDateTimePattern(locale, DEFAULT_TITLE_FORMAT), locale);
        this.mDayOfWeekFormatter = new SimpleDateFormat(DAY_OF_WEEK_FORMAT, locale);
        this.mDayFormatter = NumberFormat.getIntegerInstance(locale);
        initPaints(res);
    }

    private ColorStateList applyTextAppearance(Paint p, int resId) {
        TypedArray ta = this.mContext.obtainStyledAttributes(null, R.styleable.TextAppearance, 0, resId);
        String fontFamily = ta.getString(12);
        if (fontFamily != null) {
            p.setTypeface(Typeface.create(fontFamily, 0));
        }
        p.setTextSize((float) ta.getDimensionPixelSize(0, (int) p.getTextSize()));
        ColorStateList textColor = ta.getColorStateList(3);
        if (textColor != null) {
            p.setColor(textColor.getColorForState(ENABLED_STATE_SET, 0));
        }
        ta.recycle();
        return textColor;
    }

    public int getMonthHeight() {
        return this.mMonthHeight;
    }

    public int getCellWidth() {
        return this.mCellWidth;
    }

    public void setMonthTextAppearance(int resId) {
        applyTextAppearance(this.mMonthPaint, resId);
        invalidate();
    }

    public void setDayOfWeekTextAppearance(int resId) {
        applyTextAppearance(this.mDayOfWeekPaint, resId);
        invalidate();
    }

    public void setDayTextAppearance(int resId) {
        ColorStateList textColor = applyTextAppearance(this.mDayPaint, resId);
        if (textColor != null) {
            this.mDayTextColor = textColor;
        }
        invalidate();
    }

    public CharSequence getTitle() {
        if (this.mTitle == null) {
            this.mTitle = this.mTitleFormatter.format(this.mCalendar.getTime());
        }
        return this.mTitle;
    }

    private void initPaints(Resources res) {
        String monthTypeface = res.getString(R.string.date_picker_month_typeface);
        String dayOfWeekTypeface = res.getString(R.string.date_picker_day_of_week_typeface);
        String dayTypeface = res.getString(R.string.date_picker_day_typeface);
        int monthTextSize = res.getDimensionPixelSize(R.dimen.date_picker_month_text_size);
        int dayOfWeekTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_text_size);
        int dayTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_text_size);
        this.mMonthPaint.setAntiAlias(true);
        this.mMonthPaint.setTextSize((float) monthTextSize);
        this.mMonthPaint.setTypeface(Typeface.create(monthTypeface, 0));
        this.mMonthPaint.setTextAlign(Align.CENTER);
        this.mMonthPaint.setStyle(Style.FILL);
        this.mDayOfWeekPaint.setAntiAlias(true);
        this.mDayOfWeekPaint.setTextSize((float) dayOfWeekTextSize);
        this.mDayOfWeekPaint.setTypeface(Typeface.create(dayOfWeekTypeface, 0));
        this.mDayOfWeekPaint.setTextAlign(Align.CENTER);
        this.mDayOfWeekPaint.setStyle(Style.FILL);
        this.mDaySelectorPaint.setAntiAlias(true);
        this.mDaySelectorPaint.setStyle(Style.FILL);
        this.mDayHighlightPaint.setAntiAlias(true);
        this.mDayHighlightPaint.setStyle(Style.FILL);
        this.mDayPaint.setAntiAlias(true);
        this.mDayPaint.setTextSize((float) dayTextSize);
        this.mDayPaint.setTypeface(Typeface.create(dayTypeface, 0));
        this.mDayPaint.setTextAlign(Align.CENTER);
        this.mDayPaint.setStyle(Style.FILL);
    }

    void setMonthTextColor(ColorStateList monthTextColor) {
        this.mMonthPaint.setColor(monthTextColor.getColorForState(ENABLED_STATE_SET, 0));
        invalidate();
    }

    void setDayOfWeekTextColor(ColorStateList dayOfWeekTextColor) {
        this.mDayOfWeekPaint.setColor(dayOfWeekTextColor.getColorForState(ENABLED_STATE_SET, 0));
        invalidate();
    }

    void setDayTextColor(ColorStateList dayTextColor) {
        this.mDayTextColor = dayTextColor;
        invalidate();
    }

    void setDaySelectorColor(ColorStateList dayBackgroundColor) {
        this.mDaySelectorPaint.setColor(dayBackgroundColor.getColorForState(StateSet.get(40), 0));
        invalidate();
    }

    void setDayHighlightColor(ColorStateList dayHighlightColor) {
        this.mDayHighlightPaint.setColor(dayHighlightColor.getColorForState(StateSet.get(24), 0));
        invalidate();
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.mOnDayClickListener = listener;
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        return this.mTouchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() + 0.5f);
        int y = (int) (event.getY() + 0.5f);
        int action = event.getAction();
        switch (action) {
            case 0:
            case 2:
                int touchedItem = getDayAtLocation(x, y);
                if (this.mTouchedItem != touchedItem) {
                    this.mTouchedItem = touchedItem;
                    invalidate();
                }
                if (action == 0 && touchedItem < 0) {
                    return false;
                }
            case 1:
                onDayClicked(getDayAtLocation(x, y));
                break;
            case 3:
                break;
        }
        this.mTouchedItem = -1;
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        canvas.translate((float) paddingLeft, (float) paddingTop);
        drawMonth(canvas);
        drawDaysOfWeek(canvas);
        drawDays(canvas);
        canvas.translate((float) (-paddingLeft), (float) (-paddingTop));
    }

    private void drawMonth(Canvas canvas) {
        canvas.drawText(getTitle().toString(), ((float) this.mPaddedWidth) / 2.0f, (((float) this.mMonthHeight) - (this.mMonthPaint.ascent() + this.mMonthPaint.descent())) / 2.0f, this.mMonthPaint);
    }

    private void drawDaysOfWeek(Canvas canvas) {
        TextPaint p = this.mDayOfWeekPaint;
        int headerHeight = this.mMonthHeight;
        int rowHeight = this.mDayOfWeekHeight;
        int colWidth = this.mCellWidth;
        float halfLineHeight = (p.ascent() + p.descent()) / 2.0f;
        int rowCenter = headerHeight + (rowHeight / 2);
        for (int col = 0; col < 7; col++) {
            int colCenterRtl;
            int colCenter = (colWidth * col) + (colWidth / 2);
            if (isLayoutRtl()) {
                colCenterRtl = this.mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            canvas.drawText(getDayOfWeekLabel((this.mWeekStart + col) % 7), (float) colCenterRtl, ((float) rowCenter) - halfLineHeight, p);
        }
    }

    private String getDayOfWeekLabel(int dayOfWeek) {
        this.mDayOfWeekLabelCalendar.set(7, dayOfWeek);
        return this.mDayOfWeekFormatter.format(this.mDayOfWeekLabelCalendar.getTime());
    }

    private void drawDays(Canvas canvas) {
        Paint p = this.mDayPaint;
        int headerHeight = this.mMonthHeight + this.mDayOfWeekHeight;
        int rowHeight = this.mDayHeight;
        int colWidth = this.mCellWidth;
        float halfLineHeight = (p.ascent() + p.descent()) / 2.0f;
        int rowCenter = headerHeight + (rowHeight / 2);
        int day = 1;
        int col = findDayOffset();
        while (day <= this.mDaysInMonth) {
            int colCenterRtl;
            int dayTextColor;
            int colCenter = (colWidth * col) + (colWidth / 2);
            if (isLayoutRtl()) {
                colCenterRtl = this.mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            int stateMask = 0;
            boolean isDayEnabled = isDayEnabled(day);
            if (isDayEnabled) {
                stateMask = 0 | 8;
            }
            boolean isDayActivated = this.mActivatedDay == day;
            if (isDayActivated) {
                stateMask |= 32;
                canvas.drawCircle((float) colCenterRtl, (float) rowCenter, (float) this.mDaySelectorRadius, this.mDaySelectorPaint);
            } else if (this.mTouchedItem == day) {
                stateMask |= 16;
                if (isDayEnabled) {
                    canvas.drawCircle((float) colCenterRtl, (float) rowCenter, (float) this.mDaySelectorRadius, this.mDayHighlightPaint);
                }
            }
            if (!(this.mToday == day) || isDayActivated) {
                dayTextColor = this.mDayTextColor.getColorForState(StateSet.get(stateMask), 0);
            } else {
                dayTextColor = this.mDaySelectorPaint.getColor();
            }
            p.setColor(dayTextColor);
            canvas.drawText(this.mDayFormatter.format((long) day), (float) colCenterRtl, ((float) rowCenter) - halfLineHeight, p);
            col++;
            if (col == 7) {
                col = 0;
                rowCenter += rowHeight;
            }
            day++;
        }
    }

    private boolean isDayEnabled(int day) {
        return day >= this.mEnabledDayStart && day <= this.mEnabledDayEnd;
    }

    private boolean isValidDayOfMonth(int day) {
        return day >= 1 && day <= this.mDaysInMonth;
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= 1 && day <= 7;
    }

    private static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
    }

    public void setSelectedDay(int dayOfMonth) {
        this.mActivatedDay = dayOfMonth;
        this.mTouchHelper.invalidateRoot();
        invalidate();
    }

    public void setFirstDayOfWeek(int weekStart) {
        if (isValidDayOfWeek(weekStart)) {
            this.mWeekStart = weekStart;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        this.mTouchHelper.invalidateRoot();
        invalidate();
    }

    void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd) {
        this.mActivatedDay = selectedDay;
        if (isValidMonth(month)) {
            this.mMonth = month;
        }
        this.mYear = year;
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        this.mDayOfWeekStart = this.mCalendar.get(7);
        if (isValidDayOfWeek(weekStart)) {
            this.mWeekStart = weekStart;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        Calendar today = Calendar.getInstance();
        this.mToday = -1;
        this.mDaysInMonth = getDaysInMonth(this.mMonth, this.mYear);
        for (int i = 0; i < this.mDaysInMonth; i++) {
            int day = i + 1;
            if (sameDay(day, today)) {
                this.mToday = day;
            }
        }
        this.mEnabledDayStart = MathUtils.constrain(enabledDayStart, 1, this.mDaysInMonth);
        this.mEnabledDayEnd = MathUtils.constrain(enabledDayEnd, this.mEnabledDayStart, this.mDaysInMonth);
        this.mTitle = null;
        this.mTouchHelper.invalidateRoot();
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private boolean sameDay(int day, Calendar today) {
        return this.mYear == today.get(1) && this.mMonth == today.get(2) && day == today.get(5);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(View.resolveSize(((this.mDesiredCellWidth * 7) + getPaddingStart()) + getPaddingEnd(), widthMeasureSpec), View.resolveSize(((((this.mDesiredDayHeight * 6) + this.mDesiredDayOfWeekHeight) + this.mDesiredMonthHeight) + getPaddingTop()) + getPaddingBottom(), heightMeasureSpec));
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        requestLayout();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int w = right - left;
            int h = bottom - top;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            int paddedWidth = (w - paddingRight) - paddingLeft;
            int paddedHeight = (h - paddingBottom) - paddingTop;
            if (paddedWidth != this.mPaddedWidth && paddedHeight != this.mPaddedHeight) {
                this.mPaddedWidth = paddedWidth;
                this.mPaddedHeight = paddedHeight;
                float scaleH = ((float) paddedHeight) / ((float) ((getMeasuredHeight() - paddingTop) - paddingBottom));
                int cellWidth = this.mPaddedWidth / 7;
                this.mMonthHeight = (int) (((float) this.mDesiredMonthHeight) * scaleH);
                this.mDayOfWeekHeight = (int) (((float) this.mDesiredDayOfWeekHeight) * scaleH);
                this.mDayHeight = (int) (((float) this.mDesiredDayHeight) * scaleH);
                this.mCellWidth = cellWidth;
                this.mDaySelectorRadius = Math.min(this.mDesiredDaySelectorRadius, Math.min((cellWidth / 2) + Math.min(paddingLeft, paddingRight), (this.mDayHeight / 2) + paddingBottom));
                this.mTouchHelper.invalidateRoot();
            }
        }
    }

    private int findDayOffset() {
        int offset = this.mDayOfWeekStart - this.mWeekStart;
        if (this.mDayOfWeekStart < this.mWeekStart) {
            return offset + 7;
        }
        return offset;
    }

    private int getDayAtLocation(int x, int y) {
        int paddedX = x - getPaddingLeft();
        if (paddedX < 0 || paddedX >= this.mPaddedWidth) {
            return -1;
        }
        int headerHeight = this.mMonthHeight + this.mDayOfWeekHeight;
        int paddedY = y - getPaddingTop();
        if (paddedY < headerHeight || paddedY >= this.mPaddedHeight) {
            return -1;
        }
        int paddedXRtl;
        if (isLayoutRtl()) {
            paddedXRtl = this.mPaddedWidth - paddedX;
        } else {
            paddedXRtl = paddedX;
        }
        int day = ((((paddedXRtl * 7) / this.mPaddedWidth) + (((paddedY - headerHeight) / this.mDayHeight) * 7)) + 1) - findDayOffset();
        if (isValidDayOfMonth(day)) {
            return day;
        }
        return -1;
    }

    private boolean getBoundsForDay(int id, Rect outBounds) {
        if (!isValidDayOfMonth(id)) {
            return false;
        }
        int left;
        int index = (id - 1) + findDayOffset();
        int col = index % 7;
        int colWidth = this.mCellWidth;
        if (isLayoutRtl()) {
            left = (getWidth() - getPaddingRight()) - ((col + 1) * colWidth);
        } else {
            left = getPaddingLeft() + (col * colWidth);
        }
        int row = index / 7;
        int rowHeight = this.mDayHeight;
        int top = (getPaddingTop() + (this.mMonthHeight + this.mDayOfWeekHeight)) + (row * rowHeight);
        outBounds.set(left, top, left + colWidth, top + rowHeight);
        return true;
    }

    private boolean onDayClicked(int day) {
        if (!isValidDayOfMonth(day) || !isDayEnabled(day)) {
            return false;
        }
        if (this.mOnDayClickListener != null) {
            Calendar date = Calendar.getInstance();
            date.set(this.mYear, this.mMonth, day);
            this.mOnDayClickListener.onDayClick(this, date);
        }
        this.mTouchHelper.sendEventForVirtualView(day, 1);
        return true;
    }
}
