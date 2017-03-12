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
import android.view.RemotableViewMethod;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;

@RemoteView
public class ViewFlipper extends ViewAnimator {
    private static final int DEFAULT_INTERVAL = 3000;
    private static final boolean LOGD = false;
    private static final String TAG = "ViewFlipper";
    private final int FLIP_MSG = 1;
    private boolean mAutoStart = false;
    private int mFlipInterval = DEFAULT_INTERVAL;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1 && ViewFlipper.this.mRunning) {
                ViewFlipper.this.showNext();
                sendMessageDelayed(obtainMessage(1), (long) ViewFlipper.this.mFlipInterval);
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                ViewFlipper.this.mUserPresent = false;
                ViewFlipper.this.updateRunning();
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                ViewFlipper.this.mUserPresent = true;
                ViewFlipper.this.updateRunning(false);
            }
        }
    };
    private boolean mRunning = false;
    private boolean mStarted = false;
    private boolean mUserPresent = true;
    private boolean mVisible = false;

    public ViewFlipper(Context context) {
        super(context);
    }

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewFlipper);
        this.mFlipInterval = a.getInt(0, DEFAULT_INTERVAL);
        this.mAutoStart = a.getBoolean(1, false);
        a.recycle();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        getContext().registerReceiverAsUser(this.mReceiver, Process.myUserHandle(), filter, null, this.mHandler);
        if (this.mAutoStart) {
            startFlipping();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        getContext().unregisterReceiver(this.mReceiver);
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

    @RemotableViewMethod
    public void setFlipInterval(int milliseconds) {
        this.mFlipInterval = milliseconds;
    }

    public void startFlipping() {
        this.mStarted = true;
        updateRunning();
    }

    public void stopFlipping() {
        this.mStarted = false;
        updateRunning();
    }

    public CharSequence getAccessibilityClassName() {
        return ViewFlipper.class.getName();
    }

    private void updateRunning() {
        updateRunning(true);
    }

    private void updateRunning(boolean flipNow) {
        boolean running = this.mVisible && this.mStarted && this.mUserPresent;
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
}
