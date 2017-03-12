package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;

@RemoteView
public class AdapterViewFlipper extends AdapterViewAnimator {
    private static final int DEFAULT_INTERVAL = 10000;
    private static final boolean LOGD = false;
    private static final String TAG = "ViewFlipper";
    private final int FLIP_MSG;
    private boolean mAdvancedByHost;
    private boolean mAttachedReceiver;
    private boolean mAutoStart;
    private int mFlipInterval;
    private final Handler mHandler;
    private final BroadcastReceiver mReceiver;
    private boolean mRunning;
    private boolean mStarted;
    private boolean mUserPresent;
    private boolean mVisible;

    public AdapterViewFlipper(Context context) {
        super(context);
        this.mFlipInterval = 10000;
        this.mAutoStart = false;
        this.mRunning = false;
        this.mStarted = false;
        this.mVisible = false;
        this.mUserPresent = true;
        this.mAdvancedByHost = false;
        this.mAttachedReceiver = false;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String curThName = Thread.currentThread().getName();
                if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    AdapterViewFlipper.this.mUserPresent = false;
                    if ("main".equals(curThName) || "UI".equals(curThName)) {
                        AdapterViewFlipper.this.updateRunning();
                    } else {
                        Log.i(AdapterViewFlipper.TAG, "screen off : current Thread is not UI thread... skip updateRunning");
                    }
                } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                    AdapterViewFlipper.this.mUserPresent = true;
                    if ("main".equals(curThName) || "UI".equals(curThName)) {
                        AdapterViewFlipper.this.updateRunning(false);
                    } else {
                        Log.i(AdapterViewFlipper.TAG, "screen on : current Thread is not UI thread... skip updateRunning");
                    }
                }
            }
        };
        this.FLIP_MSG = 1;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1 && AdapterViewFlipper.this.mRunning) {
                    AdapterViewFlipper.this.showNext();
                }
            }
        };
    }

    public AdapterViewFlipper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdapterViewFlipper(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AdapterViewFlipper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mFlipInterval = 10000;
        this.mAutoStart = false;
        this.mRunning = false;
        this.mStarted = false;
        this.mVisible = false;
        this.mUserPresent = true;
        this.mAdvancedByHost = false;
        this.mAttachedReceiver = false;
        this.mReceiver = /* anonymous class already generated */;
        this.FLIP_MSG = 1;
        this.mHandler = /* anonymous class already generated */;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdapterViewFlipper, defStyleAttr, defStyleRes);
        this.mFlipInterval = a.getInt(0, 10000);
        this.mAutoStart = a.getBoolean(1, false);
        this.mLoopViews = true;
        a.recycle();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        getContext().registerReceiverAsUser(this.mReceiver, Process.myUserHandle(), filter, null, this.mHandler);
        this.mAttachedReceiver = true;
        if (this.mAutoStart) {
            startFlipping();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        if (this.mAttachedReceiver) {
            getContext().unregisterReceiver(this.mReceiver);
            this.mAttachedReceiver = false;
        }
        updateRunning();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        boolean z;
        super.onWindowVisibilityChanged(visibility);
        if (visibility == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mVisible = z;
        updateRunning(false);
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        updateRunning();
    }

    public int getFlipInterval() {
        return this.mFlipInterval;
    }

    public void setFlipInterval(int flipInterval) {
        this.mFlipInterval = flipInterval;
    }

    public void startFlipping() {
        this.mStarted = true;
        updateRunning();
    }

    public void stopFlipping() {
        this.mStarted = false;
        updateRunning();
    }

    @RemotableViewMethod
    public void showNext() {
        if (this.mRunning) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) this.mFlipInterval);
        }
        super.showNext();
    }

    @RemotableViewMethod
    public void showPrevious() {
        if (this.mRunning) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) this.mFlipInterval);
        }
        super.showPrevious();
    }

    private void updateRunning() {
        updateRunning(true);
    }

    private void updateRunning(boolean flipNow) {
        boolean running = !this.mAdvancedByHost && this.mVisible && this.mStarted && this.mUserPresent && this.mAdapter != null;
        if (running != this.mRunning) {
            if (running) {
                showOnly(this.mWhichChild, flipNow);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) this.mFlipInterval);
            } else {
                this.mHandler.removeMessages(1);
            }
            this.mRunning = running;
        }
    }

    public boolean isFlipping() {
        return this.mStarted;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    public boolean isAutoStart() {
        return this.mAutoStart;
    }

    public void fyiWillBeAdvancedByHostKThx() {
        this.mAdvancedByHost = true;
        updateRunning(false);
    }

    public CharSequence getAccessibilityClassName() {
        return AdapterViewFlipper.class.getName();
    }
}
