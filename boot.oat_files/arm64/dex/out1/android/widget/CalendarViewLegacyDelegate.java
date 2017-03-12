package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CalendarView.OnDateChangeListener;
import com.android.internal.R;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.LocaleData;

class CalendarViewLegacyDelegate extends AbstractCalendarViewDelegate {
    private static final int ADJUSTMENT_SCROLL_DURATION = 500;
    private static final int DAYS_PER_WEEK = 7;
    private static final int DEFAULT_DATE_TEXT_SIZE = 14;
    private static final int DEFAULT_SHOWN_WEEK_COUNT = 6;
    private static final boolean DEFAULT_SHOW_WEEK_NUMBER = true;
    private static final int DEFAULT_WEEK_DAY_TEXT_APPEARANCE_RES_ID = -1;
    private static final int GOTO_SCROLL_DURATION = 1000;
    private static final long MILLIS_IN_DAY = 86400000;
    private static final long MILLIS_IN_WEEK = 604800000;
    private static final boolean MOCHA = "mocha".equals(SystemProperties.get("ro.build.scafe"));
    private static final int SCROLL_CHANGE_DELAY = 40;
    private static final int SCROLL_HYST_WEEKS = 2;
    private static final int UNSCALED_BOTTOM_BUFFER = 20;
    private static final int UNSCALED_LIST_SCROLL_TOP_OFFSET = 2;
    private static final int UNSCALED_SELECTED_DATE_VERTICAL_BAR_WIDTH = 6;
    private static final int UNSCALED_WEEK_MIN_VISIBLE_HEIGHT = 12;
    private static final int UNSCALED_WEEK_SEPARATOR_LINE_WIDTH = 1;
    private static final String familyRoboto = "sans-serif";
    private static final String familyRobotoLight = "sans-serif-light";
    private WeeksAdapter mAdapter;
    private int mBottomBuffer = 20;
    private int mCurrentMonthDisplayed = -1;
    private int mCurrentScrollState = 0;
    private int mDateTextAppearanceResId;
    private int mDateTextSize;
    private ViewGroup mDayNamesHeader;
    private String[] mDayNamesLong;
    private String[] mDayNamesShort;
    private int mDaysPerWeek = 7;
    private Calendar mFirstDayOfMonth;
    private int mFirstDayOfWeek;
    private int mFocusedMonthDateColor;
    private int mFocusedSingleDateColor = -1;
    private float mFriction = 0.05f;
    private boolean mIsDeviceDefault = false;
    private boolean mIsScrollingUp = false;
    private int mListScrollTopOffset = 2;
    private ListView mListView;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private TextView mMonthName;
    private OnDateChangeListener mOnDateChangeListener;
    private long mPreviousScrollPosition;
    private int mPreviousScrollState = 0;
    private ScrollStateRunnable mScrollStateChangedRunnable = new ScrollStateRunnable();
    private Drawable mSelectedDateVerticalBar;
    private final int mSelectedDateVerticalBarWidth;
    private int mSelectedWeekBackgroundColor;
    private boolean mShowWeekNumber;
    private int mShownWeekCount;
    private Calendar mTempDate;
    private int mUnfocusedMonthDateColor;
    private float mVelocityScale = 0.333f;
    private int mWeekDayTextAppearanceResId;
    private int mWeekMinVisibleHeight = 12;
    private int mWeekNumberColor;
    private int mWeekSeparatorLineColor;
    private final int mWeekSeperatorLineWidth;

    private class ScrollStateRunnable implements Runnable {
        private int mNewState;
        private AbsListView mView;

        private ScrollStateRunnable() {
        }

        public void doScrollStateChange(AbsListView view, int scrollState) {
            this.mView = view;
            this.mNewState = scrollState;
            CalendarViewLegacyDelegate.this.mDelegator.removeCallbacks(this);
            CalendarViewLegacyDelegate.this.mDelegator.postDelayed(this, 40);
        }

        public void run() {
            CalendarViewLegacyDelegate.this.mCurrentScrollState = this.mNewState;
            if (this.mNewState == 0 && CalendarViewLegacyDelegate.this.mPreviousScrollState != 0) {
                View child = this.mView.getChildAt(0);
                if (child != null) {
                    int dist = child.getBottom() - CalendarViewLegacyDelegate.this.mListScrollTopOffset;
                    if (dist > CalendarViewLegacyDelegate.this.mListScrollTopOffset) {
                        if (CalendarViewLegacyDelegate.this.mIsScrollingUp) {
                            this.mView.smoothScrollBy(dist - child.getHeight(), 500);
                        } else {
                            this.mView.smoothScrollBy(dist, 500);
                        }
                    }
                } else {
                    return;
                }
            }
            CalendarViewLegacyDelegate.this.mPreviousScrollState = this.mNewState;
        }
    }

    private class WeekView extends View {
        private String[] mDayNumbers;
        private final Paint mDrawPaint = new Paint();
        private Calendar mFirstDay;
        private boolean[] mFocusDay;
        private boolean mHasFocusedDay;
        private boolean mHasSelectedDay = false;
        private boolean mHasUnfocusedDay;
        private int mHeight;
        private int mLastWeekDayMonth = -1;
        private final Paint mMonthNumDrawPaint = new Paint();
        private int mMonthOfFirstWeekDay = -1;
        private int mNumCells;
        private int mSelectedDay = -1;
        private int mSelectedLeft = -1;
        private int mSelectedRight = -1;
        private final Rect mTempRect = new Rect();
        private int mWeek = -1;
        private int mWidth;

        public WeekView(Context context) {
            super(context);
            initilaizePaints();
        }

        public void init(int weekNumber, int selectedWeekDay, int focusedMonth) {
            int access$2200;
            this.mSelectedDay = selectedWeekDay;
            this.mHasSelectedDay = this.mSelectedDay != -1;
            if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                access$2200 = CalendarViewLegacyDelegate.this.mDaysPerWeek + 1;
            } else {
                access$2200 = CalendarViewLegacyDelegate.this.mDaysPerWeek;
            }
            this.mNumCells = access$2200;
            this.mWeek = weekNumber;
            CalendarViewLegacyDelegate.this.mTempDate.setTimeInMillis(CalendarViewLegacyDelegate.this.mMinDate.getTimeInMillis());
            CalendarViewLegacyDelegate.this.mTempDate.add(3, this.mWeek);
            CalendarViewLegacyDelegate.this.mTempDate.setFirstDayOfWeek(CalendarViewLegacyDelegate.this.mFirstDayOfWeek);
            this.mDayNumbers = new String[this.mNumCells];
            this.mFocusDay = new boolean[this.mNumCells];
            int i = 0;
            if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                this.mDayNumbers[0] = String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(CalendarViewLegacyDelegate.this.mTempDate.get(3))});
                i = 0 + 1;
            }
            int diff = CalendarViewLegacyDelegate.this.mFirstDayOfWeek - CalendarViewLegacyDelegate.this.mTempDate.get(7);
            if (diff <= 0) {
                CalendarViewLegacyDelegate.this.mTempDate.add(5, diff);
            } else {
                CalendarViewLegacyDelegate.this.mTempDate.add(5, diff - 7);
            }
            if (("Asia/Shanghai".equals(CalendarViewLegacyDelegate.this.mTempDate.getTimeZone().getID()) || "America/Sao_Paulo".equals(CalendarViewLegacyDelegate.this.mTempDate.getTimeZone().getID()) || "America/Santiago".equals(CalendarViewLegacyDelegate.this.mTempDate.getTimeZone().getID())) && !(CalendarViewLegacyDelegate.this.mTempDate.get(10) == 0 && CalendarViewLegacyDelegate.this.mTempDate.get(11) == 0 && CalendarViewLegacyDelegate.this.mTempDate.get(12) == 0 && CalendarViewLegacyDelegate.this.mTempDate.get(13) == 0)) {
                CalendarViewLegacyDelegate.this.mTempDate.add(5, 1);
                CalendarViewLegacyDelegate.this.mTempDate.set(10, 0);
                CalendarViewLegacyDelegate.this.mTempDate.set(11, 0);
                CalendarViewLegacyDelegate.this.mTempDate.set(12, 0);
                CalendarViewLegacyDelegate.this.mTempDate.set(13, 0);
            }
            this.mFirstDay = (Calendar) CalendarViewLegacyDelegate.this.mTempDate.clone();
            this.mMonthOfFirstWeekDay = CalendarViewLegacyDelegate.this.mTempDate.get(2);
            this.mHasUnfocusedDay = true;
            for (i = 
/*
Method generation error in method: android.widget.CalendarViewLegacyDelegate.WeekView.init(int, int, int):void
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r1_2 'i' int) = (r1_0 'i' int), (r1_1 'i' int) binds: {(r1_0 'i' int)=B:7:0x005f, (r1_1 'i' int)=B:8:0x0061} in method: android.widget.CalendarViewLegacyDelegate.WeekView.init(int, int, int):void
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:328)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:265)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:228)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.addInnerClasses(ClassGen.java:241)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:83)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:530)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:514)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 18 more

*/

            private void initilaizePaints() {
                this.mDrawPaint.setFakeBoldText(false);
                this.mDrawPaint.setAntiAlias(true);
                this.mDrawPaint.setStyle(Style.FILL);
                this.mMonthNumDrawPaint.setFakeBoldText(true);
                this.mMonthNumDrawPaint.setAntiAlias(true);
                this.mMonthNumDrawPaint.setStyle(Style.FILL);
                this.mMonthNumDrawPaint.setTextAlign(Align.CENTER);
                this.mMonthNumDrawPaint.setTextSize((float) CalendarViewLegacyDelegate.this.mDateTextSize);
                if (CalendarViewLegacyDelegate.this.mIsDeviceDefault && CalendarViewLegacyDelegate.MOCHA) {
                    this.mDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRobotoLight, 0));
                    this.mMonthNumDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRobotoLight, 0));
                }
                if (CalendarViewLegacyDelegate.this.mIsDeviceDefault) {
                    this.mDrawPaint.setTextSize((float) CalendarViewLegacyDelegate.this.mDateTextSize);
                    this.mMonthNumDrawPaint.setColor(CalendarViewLegacyDelegate.this.mFocusedMonthDateColor);
                }
            }

            public int getMonthOfFirstWeekDay() {
                return this.mMonthOfFirstWeekDay;
            }

            public int getMonthOfLastWeekDay() {
                return this.mLastWeekDayMonth;
            }

            public Calendar getFirstDay() {
                return this.mFirstDay;
            }

            public boolean getDayFromLocation(float x, Calendar outCalendar) {
                int start;
                int end;
                boolean isLayoutRtl = isLayoutRtl();
                if (isLayoutRtl) {
                    start = 0;
                    end = CalendarViewLegacyDelegate.this.mShowWeekNumber ? this.mWidth - (this.mWidth / this.mNumCells) : this.mWidth;
                } else {
                    if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                        start = this.mWidth / this.mNumCells;
                    } else {
                        start = 0;
                    }
                    end = this.mWidth;
                }
                if (x < ((float) start) || x > ((float) end)) {
                    outCalendar.clear();
                    return false;
                }
                int dayPosition = (int) (((x - ((float) start)) * ((float) CalendarViewLegacyDelegate.this.mDaysPerWeek)) / ((float) (end - start)));
                if (isLayoutRtl) {
                    dayPosition = (CalendarViewLegacyDelegate.this.mDaysPerWeek - 1) - dayPosition;
                }
                outCalendar.setTimeInMillis(this.mFirstDay.getTimeInMillis());
                outCalendar.add(5, dayPosition);
                return true;
            }

            protected void onDraw(Canvas canvas) {
                if (CalendarViewLegacyDelegate.this.mIsDeviceDefault && CalendarViewLegacyDelegate.MOCHA) {
                    drawBackgroundForSelected(canvas);
                    drawWeekNumbersAndDates(canvas);
                    drawWeekSeparators(canvas);
                    return;
                }
                drawBackground(canvas);
                drawWeekNumbersAndDates(canvas);
                drawWeekSeparators(canvas);
                drawSelectedDateVerticalBars(canvas);
            }

            private void drawBackground(Canvas canvas) {
                int i = 0;
                if (this.mHasSelectedDay) {
                    this.mDrawPaint.setColor(CalendarViewLegacyDelegate.this.mSelectedWeekBackgroundColor);
                    this.mTempRect.top = CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth;
                    this.mTempRect.bottom = this.mHeight;
                    boolean isLayoutRtl = isLayoutRtl();
                    if (isLayoutRtl) {
                        this.mTempRect.left = 0;
                        this.mTempRect.right = this.mSelectedLeft - 2;
                    } else {
                        Rect rect = this.mTempRect;
                        if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                            i = this.mWidth / this.mNumCells;
                        }
                        rect.left = i;
                        this.mTempRect.right = this.mSelectedLeft - 2;
                    }
                    canvas.drawRect(this.mTempRect, this.mDrawPaint);
                    if (isLayoutRtl) {
                        this.mTempRect.left = this.mSelectedRight + 3;
                        this.mTempRect.right = CalendarViewLegacyDelegate.this.mShowWeekNumber ? this.mWidth - (this.mWidth / this.mNumCells) : this.mWidth;
                    } else {
                        this.mTempRect.left = this.mSelectedRight + 3;
                        this.mTempRect.right = this.mWidth;
                    }
                    canvas.drawRect(this.mTempRect, this.mDrawPaint);
                }
            }

            private void drawBackgroundForSelected(Canvas canvas) {
                if (this.mHasSelectedDay) {
                    this.mDrawPaint.setColor(CalendarViewLegacyDelegate.this.mSelectedWeekBackgroundColor);
                    this.mTempRect.top = CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth - 1;
                    this.mTempRect.bottom = this.mHeight;
                    this.mTempRect.left = this.mSelectedLeft;
                    this.mTempRect.right = this.mSelectedRight - 2;
                    canvas.drawRect(this.mTempRect, this.mDrawPaint);
                }
            }

            private void drawWeekNumbersAndDates(Canvas canvas) {
                int y = ((int) ((((float) this.mHeight) + this.mDrawPaint.getTextSize()) / 2.0f)) - CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth;
                int nDays = this.mNumCells;
                int divisor = nDays * 2;
                this.mDrawPaint.setTextAlign(Align.CENTER);
                this.mDrawPaint.setTextSize((float) CalendarViewLegacyDelegate.this.mDateTextSize);
                int i = 0;
                int fixedDayIndex;
                if (isLayoutRtl()) {
                    while (i < nDays) {
                        this.mMonthNumDrawPaint.setColor(this.mFocusDay[(nDays + -1) - i] ? CalendarViewLegacyDelegate.this.mFocusedMonthDateColor : CalendarViewLegacyDelegate.this.mUnfocusedMonthDateColor);
                        this.mMonthNumDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRoboto, 0));
                        if (CalendarViewLegacyDelegate.this.mIsDeviceDefault && CalendarViewLegacyDelegate.MOCHA && this.mSelectedDay > 0) {
                            if (this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek >= 0) {
                                fixedDayIndex = (this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek) + 1;
                            } else {
                                fixedDayIndex = (this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek) + 8;
                            }
                            if (!CalendarViewLegacyDelegate.this.mShowWeekNumber && fixedDayIndex > 0) {
                                fixedDayIndex = 7 - fixedDayIndex;
                            }
                            if (i == fixedDayIndex) {
                                this.mMonthNumDrawPaint.setColor(CalendarViewLegacyDelegate.this.mFocusedSingleDateColor);
                                this.mMonthNumDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRoboto, 1));
                            }
                        }
                        canvas.drawText(this.mDayNumbers[(nDays - 1) - i], (float) ((((i * 2) + 1) * this.mWidth) / divisor), (float) y, this.mMonthNumDrawPaint);
                        i++;
                    }
                    if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                        this.mDrawPaint.setColor(CalendarViewLegacyDelegate.this.mWeekNumberColor);
                        canvas.drawText(this.mDayNumbers[0], (float) (this.mWidth - (this.mWidth / divisor)), (float) y, this.mDrawPaint);
                        return;
                    }
                    return;
                }
                if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                    this.mDrawPaint.setColor(CalendarViewLegacyDelegate.this.mWeekNumberColor);
                    canvas.drawText(this.mDayNumbers[0], (float) (this.mWidth / divisor), (float) y, this.mDrawPaint);
                    i = 0 + 1;
                }
                while (i < nDays) {
                    this.mMonthNumDrawPaint.setColor(this.mFocusDay[i] ? CalendarViewLegacyDelegate.this.mFocusedMonthDateColor : CalendarViewLegacyDelegate.this.mUnfocusedMonthDateColor);
                    this.mMonthNumDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRoboto, 0));
                    if (CalendarViewLegacyDelegate.this.mIsDeviceDefault && CalendarViewLegacyDelegate.MOCHA && this.mSelectedDay > 0) {
                        fixedDayIndex = this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek >= 0 ? (this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek) + 1 : (this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek) + 8;
                        if (!CalendarViewLegacyDelegate.this.mShowWeekNumber && fixedDayIndex > 0) {
                            fixedDayIndex--;
                        }
                        if (i == fixedDayIndex) {
                            this.mMonthNumDrawPaint.setColor(CalendarViewLegacyDelegate.this.mFocusedSingleDateColor);
                            this.mMonthNumDrawPaint.setTypeface(Typeface.create(CalendarViewLegacyDelegate.familyRoboto, 1));
                        }
                    }
                    canvas.drawText(this.mDayNumbers[i], (float) ((((i * 2) + 1) * this.mWidth) / divisor), (float) y, this.mMonthNumDrawPaint);
                    i++;
                }
            }

            private void drawWeekSeparators(Canvas canvas) {
                int firstFullyVisiblePosition = CalendarViewLegacyDelegate.this.mListView.getFirstVisiblePosition();
                if (CalendarViewLegacyDelegate.this.mListView.getChildAt(0).getTop() < 0) {
                    firstFullyVisiblePosition++;
                }
                if (firstFullyVisiblePosition != this.mWeek || CalendarViewLegacyDelegate.this.mIsDeviceDefault) {
                    float startX;
                    float stopX;
                    this.mDrawPaint.setColor(CalendarViewLegacyDelegate.this.mWeekSeparatorLineColor);
                    this.mDrawPaint.setStrokeWidth((float) CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth);
                    if (isLayoutRtl()) {
                        startX = 0.0f;
                        stopX = CalendarViewLegacyDelegate.this.mShowWeekNumber ? (float) (this.mWidth - (this.mWidth / this.mNumCells)) : (float) this.mWidth;
                    } else {
                        if (CalendarViewLegacyDelegate.this.mShowWeekNumber) {
                            startX = (float) (this.mWidth / this.mNumCells);
                        } else {
                            startX = 0.0f;
                        }
                        stopX = (float) this.mWidth;
                    }
                    canvas.drawLine(startX, 0.0f, stopX, 0.0f, this.mDrawPaint);
                }
            }

            private void drawSelectedDateVerticalBars(Canvas canvas) {
                if (this.mHasSelectedDay) {
                    CalendarViewLegacyDelegate.this.mSelectedDateVerticalBar.setBounds(this.mSelectedLeft - (CalendarViewLegacyDelegate.this.mSelectedDateVerticalBarWidth / 2), CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth, this.mSelectedLeft + (CalendarViewLegacyDelegate.this.mSelectedDateVerticalBarWidth / 2), this.mHeight);
                    CalendarViewLegacyDelegate.this.mSelectedDateVerticalBar.draw(canvas);
                    CalendarViewLegacyDelegate.this.mSelectedDateVerticalBar.setBounds(this.mSelectedRight - (CalendarViewLegacyDelegate.this.mSelectedDateVerticalBarWidth / 2), CalendarViewLegacyDelegate.this.mWeekSeperatorLineWidth, this.mSelectedRight + (CalendarViewLegacyDelegate.this.mSelectedDateVerticalBarWidth / 2), this.mHeight);
                    CalendarViewLegacyDelegate.this.mSelectedDateVerticalBar.draw(canvas);
                }
            }

            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                this.mWidth = w;
                updateSelectionPositions();
            }

            private void updateSelectionPositions() {
                if (this.mHasSelectedDay) {
                    boolean isLayoutRtl = isLayoutRtl();
                    int selectedPosition = this.mSelectedDay - CalendarViewLegacyDelegate.this.mFirstDayOfWeek;
                    if (selectedPosition < 0) {
                        selectedPosition += 7;
                    }
                    if (CalendarViewLegacyDelegate.this.mShowWeekNumber && !isLayoutRtl) {
                        selectedPosition++;
                    }
                    if (isLayoutRtl) {
                        this.mSelectedLeft = (((CalendarViewLegacyDelegate.this.mDaysPerWeek - 1) - selectedPosition) * this.mWidth) / this.mNumCells;
                    } else {
                        this.mSelectedLeft = (this.mWidth * selectedPosition) / this.mNumCells;
                    }
                    this.mSelectedRight = this.mSelectedLeft + (this.mWidth / this.mNumCells);
                    if (this.mSelectedLeft == 0) {
                        this.mSelectedLeft = (int) (getResources().getDisplayMetrics().density * 1.0f);
                    }
                    if (selectedPosition == this.mNumCells - 1) {
                        this.mSelectedRight -= (int) (getResources().getDisplayMetrics().density * 1.0f);
                    }
                }
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                this.mHeight = ((CalendarViewLegacyDelegate.this.mListView.getHeight() - CalendarViewLegacyDelegate.this.mListView.getPaddingTop()) - CalendarViewLegacyDelegate.this.mListView.getPaddingBottom()) / CalendarViewLegacyDelegate.this.mShownWeekCount;
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.mHeight);
            }
        }

        private class WeeksAdapter extends BaseAdapter implements OnTouchListener {
            private int mFocusedMonth;
            private GestureDetector mGestureDetector;
            private final Calendar mSelectedDate = Calendar.getInstance();
            private int mSelectedWeek;
            private int mTotalWeekCount;

            class CalendarGestureListener extends SimpleOnGestureListener {
                CalendarGestureListener() {
                }

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            }

            public WeeksAdapter(Context context) {
                CalendarViewLegacyDelegate.this.mContext = context;
                this.mGestureDetector = new GestureDetector(CalendarViewLegacyDelegate.this.mContext, new CalendarGestureListener());
                init();
            }

            private void init() {
                this.mSelectedWeek = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(this.mSelectedDate);
                this.mTotalWeekCount = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(CalendarViewLegacyDelegate.this.mMaxDate);
                if (!(CalendarViewLegacyDelegate.this.mMinDate.get(7) == CalendarViewLegacyDelegate.this.mFirstDayOfWeek && CalendarViewLegacyDelegate.this.mMaxDate.get(7) == CalendarViewLegacyDelegate.this.mFirstDayOfWeek)) {
                    this.mTotalWeekCount++;
                }
                notifyDataSetChanged();
            }

            public void setSelectedDay(Calendar selectedDay) {
                if (selectedDay.get(6) != this.mSelectedDate.get(6) || selectedDay.get(1) != this.mSelectedDate.get(1)) {
                    this.mSelectedDate.setTimeInMillis(selectedDay.getTimeInMillis());
                    this.mSelectedWeek = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(this.mSelectedDate);
                    this.mFocusedMonth = this.mSelectedDate.get(2);
                    notifyDataSetChanged();
                }
            }

            public Calendar getSelectedDay() {
                return this.mSelectedDate;
            }

            public int getCount() {
                return this.mTotalWeekCount;
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return (long) position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                WeekView weekView;
                if (convertView != null) {
                    weekView = (WeekView) convertView;
                } else {
                    weekView = new WeekView(CalendarViewLegacyDelegate.this.mContext);
                    weekView.setLayoutParams(new LayoutParams(-2, -2));
                    weekView.setClickable(true);
                    weekView.setOnTouchListener(this);
                }
                weekView.init(position, this.mSelectedWeek == position ? this.mSelectedDate.get(7) : -1, this.mFocusedMonth);
                return weekView;
            }

            public void setFocusMonth(int month) {
                if (this.mFocusedMonth != month) {
                    this.mFocusedMonth = month;
                    notifyDataSetChanged();
                }
            }

            public boolean onTouch(View v, MotionEvent event) {
                if (!CalendarViewLegacyDelegate.this.mListView.isEnabled() || !this.mGestureDetector.onTouchEvent(event)) {
                    return false;
                }
                if (!((WeekView) v).getDayFromLocation(event.getX(), CalendarViewLegacyDelegate.this.mTempDate) || CalendarViewLegacyDelegate.this.mTempDate.before(CalendarViewLegacyDelegate.this.mMinDate) || CalendarViewLegacyDelegate.this.mTempDate.after(CalendarViewLegacyDelegate.this.mMaxDate)) {
                    return true;
                }
                onDateTapped(CalendarViewLegacyDelegate.this.mTempDate);
                return true;
            }

            private void onDateTapped(Calendar day) {
                setSelectedDay(day);
                CalendarViewLegacyDelegate.this.setMonthDisplayed(day);
            }
        }

        CalendarViewLegacyDelegate(CalendarView delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(delegator, context);
            TypedArray twAttributesArray = context.obtainStyledAttributes(R.styleable.Theme);
            this.mIsDeviceDefault = twAttributesArray.getBoolean(R.styleable.Theme_parentIsDeviceDefault, false);
            twAttributesArray.recycle();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
            this.mShowWeekNumber = a.getBoolean(1, true);
            this.mFirstDayOfWeek = a.getInt(0, LocaleData.get(Locale.getDefault()).firstDayOfWeek.intValue());
            if (!CalendarView.parseDate(a.getString(2), this.mMinDate)) {
                CalendarView.parseDate("01/01/1900", this.mMinDate);
            }
            if (!CalendarView.parseDate(a.getString(3), this.mMaxDate)) {
                CalendarView.parseDate("01/01/2100", this.mMaxDate);
            }
            if (this.mMaxDate.before(this.mMinDate)) {
                throw new IllegalArgumentException("Max date cannot be before min date.");
            }
            View content;
            this.mShownWeekCount = a.getInt(4, 6);
            this.mSelectedWeekBackgroundColor = a.getColor(5, 0);
            this.mFocusedMonthDateColor = a.getColor(6, 0);
            this.mUnfocusedMonthDateColor = a.getColor(7, 0);
            this.mWeekSeparatorLineColor = a.getColor(9, 0);
            this.mWeekNumberColor = a.getColor(8, 0);
            this.mSelectedDateVerticalBar = a.getDrawable(10);
            this.mDateTextAppearanceResId = a.getResourceId(12, R.style.TextAppearance_Small);
            if (this.mIsDeviceDefault && MOCHA) {
                this.mDateTextSize = a.getDimensionPixelSize(17, 14);
            } else {
                updateDateTextSize();
            }
            this.mWeekDayTextAppearanceResId = a.getResourceId(11, -1);
            a.recycle();
            DisplayMetrics displayMetrics = this.mDelegator.getResources().getDisplayMetrics();
            this.mWeekMinVisibleHeight = (int) TypedValue.applyDimension(1, 12.0f, displayMetrics);
            this.mListScrollTopOffset = (int) TypedValue.applyDimension(1, 2.0f, displayMetrics);
            this.mBottomBuffer = (int) TypedValue.applyDimension(1, 20.0f, displayMetrics);
            this.mSelectedDateVerticalBarWidth = (int) TypedValue.applyDimension(1, 6.0f, displayMetrics);
            this.mWeekSeperatorLineWidth = (int) TypedValue.applyDimension(1, 1.0f, displayMetrics);
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            if (this.mIsDeviceDefault && MOCHA) {
                content = layoutInflater.inflate((int) R.layout.tw_calendar_view, null, false);
            } else {
                content = layoutInflater.inflate((int) R.layout.calendar_view, null, false);
            }
            this.mDelegator.addView(content);
            this.mListView = (ListView) this.mDelegator.findViewById(R.id.list);
            this.mDayNamesHeader = (ViewGroup) content.findViewById(R.id.day_names);
            this.mMonthName = (TextView) content.findViewById(R.id.month_name);
            setUpHeader();
            setUpListView();
            setUpAdapter();
            this.mTempDate.setTimeInMillis(System.currentTimeMillis());
            if (this.mTempDate.before(this.mMinDate)) {
                goTo(this.mMinDate, false, true, true);
            } else if (this.mMaxDate.before(this.mTempDate)) {
                goTo(this.mMaxDate, false, true, true);
            } else {
                goTo(this.mTempDate, false, true, true);
            }
            this.mDelegator.invalidate();
        }

        public void setShownWeekCount(int count) {
            if (this.mShownWeekCount != count) {
                this.mShownWeekCount = count;
                this.mDelegator.invalidate();
            }
        }

        public int getShownWeekCount() {
            return this.mShownWeekCount;
        }

        public void setSelectedWeekBackgroundColor(int color) {
            if (this.mSelectedWeekBackgroundColor != color) {
                this.mSelectedWeekBackgroundColor = color;
                int childCount = this.mListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    WeekView weekView = (WeekView) this.mListView.getChildAt(i);
                    if (weekView.mHasSelectedDay) {
                        weekView.invalidate();
                    }
                }
            }
        }

        public int getSelectedWeekBackgroundColor() {
            return this.mSelectedWeekBackgroundColor;
        }

        public void setFocusedMonthDateColor(int color) {
            if (this.mFocusedMonthDateColor != color) {
                this.mFocusedMonthDateColor = color;
                int childCount = this.mListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    WeekView weekView = (WeekView) this.mListView.getChildAt(i);
                    if (weekView.mHasFocusedDay) {
                        weekView.invalidate();
                    }
                }
            }
        }

        public int getFocusedMonthDateColor() {
            return this.mFocusedMonthDateColor;
        }

        public void setUnfocusedMonthDateColor(int color) {
            if (this.mUnfocusedMonthDateColor != color) {
                this.mUnfocusedMonthDateColor = color;
                int childCount = this.mListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    WeekView weekView = (WeekView) this.mListView.getChildAt(i);
                    if (weekView.mHasUnfocusedDay) {
                        weekView.invalidate();
                    }
                }
            }
        }

        public int getUnfocusedMonthDateColor() {
            return this.mFocusedMonthDateColor;
        }

        public void setWeekNumberColor(int color) {
            if (this.mWeekNumberColor != color) {
                this.mWeekNumberColor = color;
                if (this.mShowWeekNumber) {
                    invalidateAllWeekViews();
                }
            }
        }

        public int getWeekNumberColor() {
            return this.mWeekNumberColor;
        }

        public void setWeekSeparatorLineColor(int color) {
            if (this.mWeekSeparatorLineColor != color) {
                this.mWeekSeparatorLineColor = color;
                invalidateAllWeekViews();
            }
        }

        public int getWeekSeparatorLineColor() {
            return this.mWeekSeparatorLineColor;
        }

        public void setSelectedDateVerticalBar(int resourceId) {
            setSelectedDateVerticalBar(this.mDelegator.getContext().getDrawable(resourceId));
        }

        public void setSelectedDateVerticalBar(Drawable drawable) {
            if (this.mSelectedDateVerticalBar != drawable) {
                this.mSelectedDateVerticalBar = drawable;
                int childCount = this.mListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    WeekView weekView = (WeekView) this.mListView.getChildAt(i);
                    if (weekView.mHasSelectedDay) {
                        weekView.invalidate();
                    }
                }
            }
        }

        public Drawable getSelectedDateVerticalBar() {
            return this.mSelectedDateVerticalBar;
        }

        public void setWeekDayTextAppearance(int resourceId) {
            if (this.mWeekDayTextAppearanceResId != resourceId) {
                this.mWeekDayTextAppearanceResId = resourceId;
                setUpHeader();
            }
        }

        public int getWeekDayTextAppearance() {
            return this.mWeekDayTextAppearanceResId;
        }

        public void setDateTextAppearance(int resourceId) {
            if (this.mDateTextAppearanceResId != resourceId) {
                this.mDateTextAppearanceResId = resourceId;
                updateDateTextSize();
                invalidateAllWeekViews();
            }
        }

        public int getDateTextAppearance() {
            return this.mDateTextAppearanceResId;
        }

        public void setMinDate(long minDate) {
            this.mTempDate.setTimeInMillis(minDate);
            if (!isSameDate(this.mTempDate, this.mMinDate)) {
                this.mMinDate.setTimeInMillis(minDate);
                Calendar date = this.mAdapter.mSelectedDate;
                if (date.before(this.mMinDate)) {
                    this.mAdapter.setSelectedDay(this.mMinDate);
                }
                this.mAdapter.init();
                if (date.before(this.mMinDate)) {
                    setDate(this.mTempDate.getTimeInMillis());
                } else {
                    goTo(date, false, true, true);
                }
            }
        }

        public long getMinDate() {
            return this.mMinDate.getTimeInMillis();
        }

        public void setMaxDate(long maxDate) {
            this.mTempDate.setTimeInMillis(maxDate);
            if (!isSameDate(this.mTempDate, this.mMaxDate)) {
                this.mMaxDate.setTimeInMillis(maxDate);
                this.mAdapter.init();
                Calendar date = this.mAdapter.mSelectedDate;
                if (date.after(this.mMaxDate)) {
                    setDate(this.mMaxDate.getTimeInMillis());
                } else {
                    goTo(date, false, true, true);
                }
            }
        }

        public long getMaxDate() {
            return this.mMaxDate.getTimeInMillis();
        }

        public void setShowWeekNumber(boolean showWeekNumber) {
            if (this.mShowWeekNumber != showWeekNumber) {
                this.mShowWeekNumber = showWeekNumber;
                this.mAdapter.notifyDataSetChanged();
                setUpHeader();
            }
        }

        public boolean getShowWeekNumber() {
            return this.mShowWeekNumber;
        }

        public void setFirstDayOfWeek(int firstDayOfWeek) {
            if (this.mFirstDayOfWeek != firstDayOfWeek) {
                this.mFirstDayOfWeek = firstDayOfWeek;
                this.mAdapter.init();
                this.mAdapter.notifyDataSetChanged();
                setUpHeader();
            }
        }

        public int getFirstDayOfWeek() {
            return this.mFirstDayOfWeek;
        }

        public void setDate(long date) {
            setDate(date, false, false);
        }

        public void setDate(long date, boolean animate, boolean center) {
            this.mTempDate.setTimeInMillis(date);
            if (!isSameDate(this.mTempDate, this.mAdapter.mSelectedDate)) {
                goTo(this.mTempDate, animate, true, center);
            }
        }

        public long getDate() {
            return this.mAdapter.mSelectedDate.getTimeInMillis();
        }

        public void setOnDateChangeListener(OnDateChangeListener listener) {
            this.mOnDateChangeListener = listener;
        }

        public void onConfigurationChanged(Configuration newConfig) {
            setCurrentLocale(newConfig.locale);
        }

        protected void setCurrentLocale(Locale locale) {
            super.setCurrentLocale(locale);
            this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
            this.mFirstDayOfMonth = getCalendarForLocale(this.mFirstDayOfMonth, locale);
            this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
            this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        }

        private void updateDateTextSize() {
            TypedArray dateTextAppearance = this.mDelegator.getContext().obtainStyledAttributes(this.mDateTextAppearanceResId, R.styleable.TextAppearance);
            this.mDateTextSize = dateTextAppearance.getDimensionPixelSize(0, 14);
            dateTextAppearance.recycle();
        }

        private void invalidateAllWeekViews() {
            int childCount = this.mListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mListView.getChildAt(i).invalidate();
            }
        }

        private static Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
            if (oldCalendar == null) {
                return Calendar.getInstance(locale);
            }
            long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }

        private static boolean isSameDate(Calendar firstDate, Calendar secondDate) {
            return firstDate.get(6) == secondDate.get(6) && firstDate.get(1) == secondDate.get(1);
        }

        private void setUpAdapter() {
            if (this.mAdapter == null) {
                this.mAdapter = new WeeksAdapter(this.mContext);
                this.mAdapter.registerDataSetObserver(new DataSetObserver() {
                    public void onChanged() {
                        if (CalendarViewLegacyDelegate.this.mOnDateChangeListener != null) {
                            Calendar selectedDay = CalendarViewLegacyDelegate.this.mAdapter.getSelectedDay();
                            CalendarViewLegacyDelegate.this.mOnDateChangeListener.onSelectedDayChange(CalendarViewLegacyDelegate.this.mDelegator, selectedDay.get(1), selectedDay.get(2), selectedDay.get(5));
                        }
                    }
                });
                this.mListView.setAdapter(this.mAdapter);
            }
            this.mAdapter.notifyDataSetChanged();
        }

        private void setUpHeader() {
            int i;
            this.mDayNamesShort = new String[this.mDaysPerWeek];
            this.mDayNamesLong = new String[this.mDaysPerWeek];
            int count = this.mFirstDayOfWeek + this.mDaysPerWeek;
            for (i = this.mFirstDayOfWeek; i < count; i++) {
                int calendarDay;
                if (i > 7) {
                    calendarDay = i - 7;
                } else {
                    calendarDay = i;
                }
                this.mDayNamesShort[i - this.mFirstDayOfWeek] = DateUtils.getDayOfWeekString(calendarDay, 50);
                this.mDayNamesLong[i - this.mFirstDayOfWeek] = DateUtils.getDayOfWeekString(calendarDay, 10);
            }
            TextView label = (TextView) this.mDayNamesHeader.getChildAt(0);
            if (this.mShowWeekNumber) {
                label.setVisibility(0);
            } else {
                label.setVisibility(8);
            }
            count = this.mDayNamesHeader.getChildCount();
            for (i = 1; i < count; i++) {
                label = (TextView) this.mDayNamesHeader.getChildAt(i);
                if (this.mWeekDayTextAppearanceResId > -1) {
                    label.setTextAppearance(this.mWeekDayTextAppearanceResId);
                }
                if (i < this.mDaysPerWeek + 1) {
                    label.setText(this.mDayNamesShort[i - 1]);
                    label.setContentDescription(this.mDayNamesLong[i - 1]);
                    label.setVisibility(0);
                } else {
                    label.setVisibility(8);
                }
            }
            this.mDayNamesHeader.invalidate();
        }

        private void setUpListView() {
            this.mListView.setDivider(null);
            this.mListView.setItemsCanFocus(true);
            this.mListView.setVerticalScrollBarEnabled(false);
            this.mListView.setOnScrollListener(new OnScrollListener() {
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    CalendarViewLegacyDelegate.this.onScrollStateChanged(view, scrollState);
                }

                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    CalendarViewLegacyDelegate.this.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            });
            this.mListView.setFriction(this.mFriction);
            this.mListView.setVelocityScale(this.mVelocityScale);
        }

        private void goTo(Calendar date, boolean animate, boolean setSelected, boolean forceScroll) {
            if (date.before(this.mMinDate) || date.after(this.mMaxDate)) {
                throw new IllegalArgumentException("Time not between " + this.mMinDate.getTime() + " and " + this.mMaxDate.getTime());
            }
            int firstFullyVisiblePosition = this.mListView.getFirstVisiblePosition();
            View firstChild = this.mListView.getChildAt(0);
            if (firstChild != null && firstChild.getTop() < 0) {
                firstFullyVisiblePosition++;
            }
            int lastFullyVisiblePosition = (this.mShownWeekCount + firstFullyVisiblePosition) - 1;
            if (firstChild != null && firstChild.getTop() > this.mBottomBuffer) {
                lastFullyVisiblePosition--;
            }
            if (setSelected) {
                this.mAdapter.setSelectedDay(date);
            }
            int position = getWeeksSinceMinDate(date);
            if (position < firstFullyVisiblePosition || position > lastFullyVisiblePosition || forceScroll) {
                this.mFirstDayOfMonth.setTimeInMillis(date.getTimeInMillis());
                this.mFirstDayOfMonth.set(5, 1);
                setMonthDisplayed(this.mFirstDayOfMonth);
                if (this.mFirstDayOfMonth.before(this.mMinDate)) {
                    position = 0;
                } else {
                    position = getWeeksSinceMinDate(this.mFirstDayOfMonth);
                }
                this.mPreviousScrollState = 2;
                if (animate) {
                    this.mListView.smoothScrollToPositionFromTop(position, this.mListScrollTopOffset, 1000);
                    return;
                }
                this.mListView.setSelectionFromTop(position, this.mListScrollTopOffset);
                onScrollStateChanged(this.mListView, 0);
            } else if (setSelected) {
                setMonthDisplayed(date);
            }
        }

        private void onScrollStateChanged(AbsListView view, int scrollState) {
            this.mScrollStateChangedRunnable.doScrollStateChange(view, scrollState);
        }

        private void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            WeekView child = (WeekView) view.getChildAt(0);
            if (child != null) {
                long currScroll = (long) ((view.getFirstVisiblePosition() * child.getHeight()) - child.getBottom());
                if (currScroll < this.mPreviousScrollPosition) {
                    this.mIsScrollingUp = true;
                } else if (currScroll > this.mPreviousScrollPosition) {
                    this.mIsScrollingUp = false;
                } else {
                    return;
                }
                int offset = child.getBottom() < this.mWeekMinVisibleHeight ? 1 : 0;
                if (this.mIsScrollingUp) {
                    child = (WeekView) view.getChildAt(offset + 2);
                } else if (offset != 0) {
                    child = (WeekView) view.getChildAt(offset);
                }
                if (child != null) {
                    int month;
                    if (this.mIsScrollingUp) {
                        month = child.getMonthOfFirstWeekDay();
                    } else {
                        month = child.getMonthOfLastWeekDay();
                    }
                    int monthDiff;
                    if (this.mCurrentMonthDisplayed == 11 && month == 0) {
                        monthDiff = 1;
                    } else if (this.mCurrentMonthDisplayed == 0 && month == 11) {
                        monthDiff = -1;
                    } else {
                        monthDiff = month - this.mCurrentMonthDisplayed;
                    }
                    if ((!this.mIsScrollingUp && monthDiff > 0) || (this.mIsScrollingUp && monthDiff < 0)) {
                        Calendar firstDay = child.getFirstDay();
                        if (this.mIsScrollingUp) {
                            firstDay.add(5, -7);
                        } else {
                            firstDay.add(5, 7);
                        }
                        setMonthDisplayed(firstDay);
                    }
                }
                this.mPreviousScrollPosition = currScroll;
                this.mPreviousScrollState = this.mCurrentScrollState;
            }
        }

        private void setMonthDisplayed(Calendar calendar) {
            this.mCurrentMonthDisplayed = calendar.get(2);
            this.mAdapter.setFocusMonth(this.mCurrentMonthDisplayed);
            long millis = calendar.getTimeInMillis();
            this.mMonthName.setText(DateUtils.formatDateRange(this.mContext, millis, millis, 52));
            this.mMonthName.invalidate();
        }

        private int getWeeksSinceMinDate(Calendar date) {
            if (date.before(this.mMinDate)) {
                throw new IllegalArgumentException("fromDate: " + this.mMinDate.getTime() + " does not precede toDate: " + date.getTime());
            }
            long endTimeMillis = date.getTimeInMillis() + ((long) date.getTimeZone().getOffset(date.getTimeInMillis()));
            long startTimeMillis = this.mMinDate.getTimeInMillis() + ((long) this.mMinDate.getTimeZone().getOffset(this.mMinDate.getTimeInMillis()));
            long dayOffsetMillis = ((long) (this.mMinDate.get(7) - this.mFirstDayOfWeek)) * 86400000;
            if (dayOffsetMillis < 0) {
                dayOffsetMillis += 604800000;
            }
            return (int) (((endTimeMillis - startTimeMillis) + dayOffsetMillis) / 604800000);
        }
    }
