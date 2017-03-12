package android.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class GlobalActionsFrameLayout extends LinearLayout {
    private static final int MESSAGE_SHOW_CONFIRM_LANDSCAPE = 7;
    private static final int MESSAGE_SHOW_CONFIRM_PORTRAIT = 6;
    private static final String TAG_BG = "globalactions_bg";
    LinearLayout bg = null;
    LinearLayout bg_land = null;
    int childcnt = 0;
    int[] childindex = null;
    HorizontalScrollView hsv = null;
    boolean mConfirmLandscape = false;
    boolean mConfirmPortrait = false;
    Context mContext;
    Handler mHandler;
    ScrollView sv = null;

    public GlobalActionsFrameLayout(Context context) {
        super(context);
    }

    public GlobalActionsFrameLayout(Context context, int childcount) {
        super(context);
        this.mContext = context;
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(17367142, null);
        this.childcnt = childcount;
        this.childindex = new int[this.childcnt];
        this.sv = (ScrollView) view.findViewById(16909216);
        this.hsv = (HorizontalScrollView) view.findViewById(16909218);
        this.bg = (LinearLayout) view.findViewById(16909217);
        this.bg_land = (LinearLayout) view.findViewById(16909219);
        addView(view);
    }

    public GlobalActionsFrameLayout(Context context, int childcount, Handler handler) {
        super(context);
        this.mContext = context;
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(17367142, null);
        this.childcnt = childcount;
        this.childindex = new int[this.childcnt];
        this.sv = (ScrollView) view.findViewById(16909216);
        this.hsv = (HorizontalScrollView) view.findViewById(16909218);
        this.bg = (LinearLayout) view.findViewById(16909217);
        this.bg_land = (LinearLayout) view.findViewById(16909219);
        this.mHandler = handler;
        addView(view);
    }

    public GlobalActionsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlobalActionsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GlobalActionsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void hideAllView(View selectedView) {
        for (int i = 0; i < this.bg.getChildCount(); i++) {
            View view = this.bg.getChildAt(i);
            if (!selectedView.equals(view)) {
                final View tempView = view;
                FrameLayout imageFrameLayout = (FrameLayout) tempView.findViewById(16909232);
                imageFrameLayout.setClickable(false);
                imageFrameLayout.setFocusable(false);
                view.animate().alpha(0.0f).setDuration(200).withEndAction(new Runnable() {
                    public void run() {
                        tempView.setVisibility(4);
                    }
                }).start();
            }
        }
    }

    public void showAllView(View selectedView) {
        for (int i = 0; i < this.bg.getChildCount(); i++) {
            View view = this.bg.getChildAt(i);
            if (!selectedView.equals(view)) {
                final View tempView = view;
                view.animate().alpha(1.0f).setDuration(200).withEndAction(new Runnable() {
                    public void run() {
                        tempView.setVisibility(0);
                    }
                }).start();
            }
        }
    }

    public void hideAllViewForLand(View selectedView) {
        for (int i = 0; i < this.bg_land.getChildCount(); i++) {
            View view = this.bg_land.getChildAt(i);
            if (!selectedView.equals(view)) {
                final View tempView = view;
                FrameLayout imageFrameLayout = (FrameLayout) tempView.findViewById(16909232);
                imageFrameLayout.setClickable(false);
                imageFrameLayout.setFocusable(false);
                view.animate().alpha(0.0f).setDuration(200).withEndAction(new Runnable() {
                    public void run() {
                        tempView.setVisibility(4);
                    }
                }).start();
            }
        }
    }

    public void showAllViewForLand(View selectedView) {
        for (int i = 0; i < this.bg_land.getChildCount(); i++) {
            View view = this.bg_land.getChildAt(i);
            if (!selectedView.equals(view)) {
                final View tempView = view;
                view.animate().alpha(1.0f).setDuration(200).withEndAction(new Runnable() {
                    public void run() {
                        tempView.setVisibility(0);
                    }
                }).start();
            }
        }
    }

    public void checkOrientation(String orientation) {
        if (orientation.equals("ConfirmPortrait")) {
            this.mConfirmPortrait = true;
        } else if (orientation.equals("ConfirmLandscape")) {
            this.mConfirmLandscape = true;
        } else if (orientation.equals("HideConfirmPortrait")) {
            this.mConfirmPortrait = false;
        } else if (orientation.equals("HideConfirmLandscape")) {
            this.mConfirmLandscape = false;
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (int i = 0; i < this.bg.getChildCount(); i++) {
            getChildAt(i);
        }
        if (newConfig.orientation == 1) {
            this.sv.setVisibility(0);
            this.hsv.setVisibility(4);
            if (this.mConfirmLandscape && !this.mConfirmPortrait) {
                this.mHandler.sendEmptyMessage(6);
            }
        } else if (newConfig.orientation == 2) {
            this.sv.setVisibility(4);
            this.hsv.setVisibility(0);
            if (this.mConfirmPortrait && !this.mConfirmLandscape) {
                this.mHandler.sendEmptyMessage(7);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
