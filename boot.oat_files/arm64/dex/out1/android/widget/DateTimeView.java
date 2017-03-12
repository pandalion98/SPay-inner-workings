package android.widget;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.RemoteViews.RemoteView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

@RemoteView
public class DateTimeView extends TextView {
    private static final int SHOW_MONTH_DAY_YEAR = 1;
    private static final int SHOW_TIME = 0;
    private static final String TAG = "DateTimeView";
    private static final long TWELVE_HOURS_IN_MINUTES = 720;
    private static final long TWENTY_FOUR_HOURS_IN_MILLIS = 86400000;
    private static final ThreadLocal<ReceiverInfo> sReceiverInfo = new ThreadLocal();
    int mLastDisplay = -1;
    DateFormat mLastFormat;
    Date mTime;
    long mTimeMillis;
    private long mUpdateTimeMillis;

    private static class ReceiverInfo {
        private final ArrayList<DateTimeView> mAttachedViews;
        private final ContentObserver mObserver;
        private final BroadcastReceiver mReceiver;

        private ReceiverInfo() {
            this.mAttachedViews = new ArrayList();
            this.mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (!"android.intent.action.TIME_TICK".equals(intent.getAction()) || System.currentTimeMillis() >= ReceiverInfo.this.getSoonestUpdateTime()) {
                        ReceiverInfo.this.updateAll();
                    }
                }
            };
            this.mObserver = new ContentObserver(new Handler()) {
                public void onChange(boolean selfChange) {
                    ReceiverInfo.this.updateAll();
                }
            };
        }

        public void addView(DateTimeView v) {
            boolean register = this.mAttachedViews.isEmpty();
            this.mAttachedViews.add(v);
            if (register) {
                register(getApplicationContextIfAvailable(v.getContext()));
            }
        }

        public void removeView(DateTimeView v) {
            this.mAttachedViews.remove(v);
            if (this.mAttachedViews.isEmpty()) {
                unregister(getApplicationContextIfAvailable(v.getContext()));
            }
        }

        void updateAll() {
            int count = this.mAttachedViews.size();
            for (int i = 0; i < count; i++) {
                ((DateTimeView) this.mAttachedViews.get(i)).clearFormatAndUpdate();
            }
        }

        long getSoonestUpdateTime() {
            long result = Long.MAX_VALUE;
            int count = this.mAttachedViews.size();
            for (int i = 0; i < count; i++) {
                long time = ((DateTimeView) this.mAttachedViews.get(i)).mUpdateTimeMillis;
                if (time < result) {
                    result = time;
                }
            }
            return result;
        }

        static final Context getApplicationContextIfAvailable(Context context) {
            Context ac = context.getApplicationContext();
            return ac != null ? ac : ActivityThread.currentApplication().getApplicationContext();
        }

        void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.TIME_TICK");
            filter.addAction("android.intent.action.TIME_SET");
            filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            filter.addAction("android.intent.action.TIMEZONE_CHANGED");
            context.registerReceiver(this.mReceiver, filter);
        }

        void unregister(Context context) {
            context.unregisterReceiver(this.mReceiver);
        }
    }

    public DateTimeView(Context context) {
        super(context);
    }

    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ReceiverInfo ri = (ReceiverInfo) sReceiverInfo.get();
        if (ri == null) {
            ri = new ReceiverInfo();
            sReceiverInfo.set(ri);
        }
        ri.addView(this);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ReceiverInfo ri = (ReceiverInfo) sReceiverInfo.get();
        if (ri != null) {
            ri.removeView(this);
        }
    }

    @RemotableViewMethod
    public void setTime(long time) {
        Time t = new Time();
        t.set(time);
        t.second = 0;
        this.mTimeMillis = t.toMillis(false);
        this.mTime = new Date(t.year - 1900, t.month, t.monthDay, t.hour, t.minute, 0);
        update();
    }

    void update() {
        if (this.mTime != null) {
            int display;
            DateFormat format;
            long start = System.nanoTime();
            Date time = this.mTime;
            Time t = new Time();
            t.set(this.mTimeMillis);
            t.second = 0;
            t.hour -= 12;
            long twelveHoursBefore = t.toMillis(false);
            t.hour += 12;
            long twelveHoursAfter = t.toMillis(false);
            t.hour = 0;
            t.minute = 0;
            long midnightBefore = t.toMillis(false);
            t.monthDay++;
            long midnightAfter = t.toMillis(false);
            t.set(System.currentTimeMillis());
            t.second = 0;
            long nowMillis = t.normalize(false);
            if ((nowMillis < midnightBefore || nowMillis >= midnightAfter) && (nowMillis < twelveHoursBefore || nowMillis >= twelveHoursAfter)) {
                display = 1;
            } else {
                display = 0;
            }
            if (display != this.mLastDisplay || this.mLastFormat == null) {
                switch (display) {
                    case 0:
                        format = getTimeFormat();
                        break;
                    case 1:
                        format = DateFormat.getDateInstance(3);
                        break;
                    default:
                        throw new RuntimeException("unknown display value: " + display);
                }
                this.mLastFormat = format;
            } else {
                format = this.mLastFormat;
            }
            setText((CharSequence) format.format(this.mTime));
            if (display == 0) {
                if (twelveHoursAfter <= midnightAfter) {
                    twelveHoursAfter = midnightAfter;
                }
                this.mUpdateTimeMillis = twelveHoursAfter;
            } else if (this.mTimeMillis < nowMillis) {
                this.mUpdateTimeMillis = 0;
            } else {
                if (twelveHoursBefore >= midnightBefore) {
                    twelveHoursBefore = midnightBefore;
                }
                this.mUpdateTimeMillis = twelveHoursBefore;
            }
            long finish = System.nanoTime();
        }
    }

    private DateFormat getTimeFormat() {
        return android.text.format.DateFormat.getTimeFormat(getContext());
    }

    void clearFormatAndUpdate() {
        this.mLastFormat = null;
        update();
    }
}
