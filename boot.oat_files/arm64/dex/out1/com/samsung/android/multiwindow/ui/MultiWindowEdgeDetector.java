package com.samsung.android.multiwindow.ui;

import android.content.Context;
import android.content.Intent;
import android.os.FactoryTest;
import android.provider.Settings$System;
import android.util.secutil.Slog;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.android.internal.R;
import com.samsung.android.cocktailbar.CocktailBarFeatures;
import com.samsung.android.multiwindow.MultiWindowFeatures;

public class MultiWindowEdgeDetector {
    private static final boolean DEBUG = true;
    public static final int EDGE_LEFT_TOP = 5;
    public static final int EDGE_RIGHT_TOP = 9;
    private static final String TAG = "MultiWindowEdgeDetector";
    private static float mHeight;
    private static float mHeightForPen;
    private static boolean mIsSupportCocktailBar;
    private static float mWidth;
    private Context mContext;
    private int mEdge = 0;
    private int mRotation;
    private int mScreenWidth;

    public MultiWindowEdgeDetector(Context context) {
        this.mContext = context;
        mWidth = this.mContext.getResources().getDimension(R.dimen.multiwindow_gesture_width);
        mHeight = this.mContext.getResources().getDimension(R.dimen.multiwindow_gesture_height);
        mHeightForPen = this.mContext.getResources().getDimension(R.dimen.multiwindow_gesture_height_pen);
        mIsSupportCocktailBar = CocktailBarFeatures.isSupportCocktailBar(this.mContext);
        if (CocktailBarFeatures.isSupportCocktailPanel(this.mContext)) {
            mWidth -= this.mContext.getResources().getDimension(R.dimen.multiwindow_gesture_curve_supplement_width);
        }
    }

    public void init() {
        this.mScreenWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;
        if (mIsSupportCocktailBar) {
            this.mRotation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean lastResizing = isEdge();
        if (ev.getActionMasked() == 0) {
            init();
            this.mEdge = getEdgeFlag(this.mScreenWidth, ev);
            if (mIsSupportCocktailBar) {
                this.mEdge = checkCocktailBar(this.mEdge);
            }
            if (this.mEdge == 5 || this.mEdge == 9) {
                lastResizing = true;
            } else {
                lastResizing = false;
            }
            if (!notMultiWindowEdgeSupport(ev)) {
                return lastResizing;
            }
            this.mEdge = 0;
            return false;
        } else if (ev.getActionMasked() != 1 && ev.getActionMasked() != 3) {
            return lastResizing;
        } else {
            this.mEdge = 0;
            return lastResizing;
        }
    }

    public boolean isEdge() {
        return this.mEdge == 5 || this.mEdge == 9;
    }

    public void reset() {
        Slog.d(TAG, "reset last edge detect inform by force");
        this.mEdge = 0;
    }

    private boolean notMultiWindowEdgeSupport(MotionEvent ev) {
        return ((ev == null || (ev.getButtonState() & 2) == 0) && Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.DB_MULTI_WINDOW_MODE, 0, -2) == 1 && Settings$System.getIntForUser(this.mContext.getContentResolver(), "db_popup_view_shortcut", 1, -2) == 1 && Settings$System.getIntForUser(this.mContext.getContentResolver(), Settings$System.EASY_MODE_SWITCH, 1, -2) == 1 && !FactoryTest.isRunningFactoryApp() && Settings$System.getIntForUser(this.mContext.getContentResolver(), "car_mode_on", 0, -2) == 0) ? false : true;
    }

    public static int getEdgeFlag(int screenWidth, MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        if (ev.getToolType(ev.getActionIndex()) == 2) {
            mHeight = mHeightForPen;
        }
        if (y > mHeight) {
            return 0;
        }
        int flag = 1;
        if (x < mWidth) {
            flag = 1 | 4;
        } else if (x > ((float) screenWidth) - mWidth) {
            flag = 1 | 8;
        }
        Slog.d(TAG, "getEdgeFlag:" + Integer.toHexString(flag));
        return flag;
    }

    private int checkCocktailBar(int flag) {
        if (flag == 5 && (this.mRotation == 1 || this.mRotation == 2)) {
            return 0;
        }
        if (flag != 9) {
            return flag;
        }
        if (this.mRotation == 0 || this.mRotation == 1) {
            return 0;
        }
        return flag;
    }

    public void showMultiWindowGestureHelp() {
        if (!notMultiWindowEdgeSupport(null)) {
            try {
                Intent multiWindowUIIntent = new Intent("android.intent.action.MAIN");
                if (MultiWindowFeatures.isSupportSimplificationUI(this.mContext)) {
                    multiWindowUIIntent.setClassName("com.android.systemui", "com.android.systemui.multiwindow.centerbarwindow.CenterBarWindowService");
                } else {
                    multiWindowUIIntent.setClassName("com.sec.android.app.FlashBarService", "com.sec.android.app.FlashBarService.MultiWindowTrayService");
                }
                multiWindowUIIntent.setAction("com.sec.android.multiwindow.gesture.overlayHelp");
                this.mContext.startService(multiWindowUIIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
