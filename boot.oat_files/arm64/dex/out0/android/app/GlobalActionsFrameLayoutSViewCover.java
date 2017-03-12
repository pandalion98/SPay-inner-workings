package android.app;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class GlobalActionsFrameLayoutSViewCover extends LinearLayout {
    LinearLayout bg = null;
    int childcnt = 0;
    int[] childindex = null;
    Context mContext;
    Handler mHandler;
    ScrollView sv = null;

    public GlobalActionsFrameLayoutSViewCover(Context context) {
        super(context);
    }

    public GlobalActionsFrameLayoutSViewCover(Context context, int childcount) {
        super(context);
        this.mContext = context;
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(17367143, null);
        this.childcnt = childcount;
        this.childindex = new int[this.childcnt];
        this.sv = (ScrollView) view.findViewById(16909216);
        this.bg = (LinearLayout) view.findViewById(16909217);
        addView(view);
    }

    public GlobalActionsFrameLayoutSViewCover(Context context, int childcount, Handler handler) {
        super(context);
        this.mContext = context;
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(17367143, null);
        this.childcnt = childcount;
        this.childindex = new int[this.childcnt];
        this.sv = (ScrollView) view.findViewById(16909216);
        this.bg = (LinearLayout) view.findViewById(16909217);
        this.mHandler = handler;
        addView(view);
    }

    public GlobalActionsFrameLayoutSViewCover(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlobalActionsFrameLayoutSViewCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GlobalActionsFrameLayoutSViewCover(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public int getChildIndex() {
        return this.childcnt;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
