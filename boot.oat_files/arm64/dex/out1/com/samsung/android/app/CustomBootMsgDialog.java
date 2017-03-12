package com.samsung.android.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;

public class CustomBootMsgDialog extends Dialog {
    private static final int RESET_BIG_GEAR_DEGREE = 900;
    final String TAG = "CustomBootMsgDialog";
    private Runnable mAnimationRunnable = new Runnable() {
        public void run() {
            if (CustomBootMsgDialog.this.mBigGear != null && CustomBootMsgDialog.this.mSmallGear != null) {
                long currentTime = AnimationUtils.currentAnimationTimeMillis();
                long timeGap = currentTime - CustomBootMsgDialog.this.mPreviousTime;
                CustomBootMsgDialog.this.mPreviousTime = currentTime;
                if (timeGap != currentTime) {
                    float bigGearRotation = CustomBootMsgDialog.this.mBigGear.getRotation();
                    float smallGearRotation = CustomBootMsgDialog.this.mSmallGear.getRotation();
                    if (bigGearRotation >= 900.0f) {
                        CustomBootMsgDialog.this.mBigGear.setRotation(0.0f);
                        CustomBootMsgDialog.this.mSmallGear.setRotation(0.0f);
                    } else {
                        CustomBootMsgDialog.this.mBigGear.setRotation(((((float) timeGap) * 30.0f) / 1000.0f) + bigGearRotation);
                        CustomBootMsgDialog.this.mSmallGear.setRotation(((((float) timeGap) * -45.0f) / 1000.0f) + smallGearRotation);
                    }
                }
                if (CustomBootMsgDialog.this.mAnimationRunning) {
                    CustomBootMsgDialog.this.mBigGear.postOnAnimationDelayed(this, 32);
                }
            }
        }
    };
    private boolean mAnimationRunning = false;
    private View mBigGear;
    int mCurrent = 0;
    private Handler mHandler;
    int mMax = 0;
    private long mPreviousTime;
    ProgressBar mProgressBar = null;
    private View mSmallGear;
    TextView mUpgradeProgressMsg = null;

    public CustomBootMsgDialog(Context context, int title_id) {
        super(context, R.style.Theme_Light_NoTitleBar_Fullscreen);
        LayoutParams lp = getWindow().getAttributes();
        lp.type = LayoutParams.TYPE_BOOT_PROGRESS;
        lp.flags |= 1280;
        lp.screenOrientation = 5;
        lp.height = -1;
        lp.width = -1;
        getWindow().setAttributes(lp);
        View v = LayoutInflater.from(context).inflate((int) R.layout.tw_upgrade_dialog_layout, null);
        TextView title = (TextView) v.findViewById(R.id.title_msg);
        this.mUpgradeProgressMsg = (TextView) v.findViewById(R.id.upgrade_progress_msg);
        this.mProgressBar = (ProgressBar) v.findViewById(R.id.upgrade_progressbar);
        this.mBigGear = v.findViewById(R.id.fota_big_gear);
        this.mSmallGear = v.findViewById(R.id.fota_small_gear);
        this.mHandler = new Handler();
        this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                CustomBootMsgDialog.this.mBigGear.getViewTreeObserver().removeOnPreDrawListener(this);
                CustomBootMsgDialog.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        CustomBootMsgDialog.this.mPreviousTime = 0;
                        CustomBootMsgDialog.this.mAnimationRunning = true;
                        CustomBootMsgDialog.this.mBigGear.postOnAnimation(CustomBootMsgDialog.this.mAnimationRunnable);
                    }
                }, 1000);
                return false;
            }
        });
        title.setText(context.getResources().getString(title_id));
        setContentView(v);
    }

    public void dismiss() {
        Log.e("CustomBootMsgDialog", "dismiss CustomBootMsg ");
        this.mAnimationRunning = false;
        if (this.mBigGear != null) {
            this.mBigGear.clearAnimation();
        }
        if (this.mSmallGear != null) {
            this.mSmallGear.clearAnimation();
        }
        super.dismiss();
    }

    public void setProgress(String msg) {
        Log.e("CustomBootMsgDialog", "Booting " + msg);
        if (msg == null) {
            this.mProgressBar.setVisibility(8);
            return;
        }
        parseDigit(msg);
        if (this.mMax != 0) {
            this.mProgressBar.setVisibility(0);
            this.mProgressBar.setMax(this.mMax);
            this.mProgressBar.setProgress(this.mCurrent);
        } else {
            this.mProgressBar.setVisibility(8);
        }
        this.mUpgradeProgressMsg.setText((CharSequence) msg);
    }

    private void parseDigit(String msg) {
        int index;
        int current_max_index = 0;
        int temp_digit = 0;
        ArrayList<Integer> min_max = new ArrayList();
        for (index = 0; index < msg.length(); index++) {
            char digit = msg.charAt(index);
            if ('0' > digit || digit > '9') {
                if (temp_digit != 0) {
                    min_max.add(Integer.valueOf(temp_digit));
                    current_max_index++;
                }
                temp_digit = 0;
            } else {
                temp_digit = ((temp_digit * 10) + digit) - 48;
            }
        }
        temp_digit = 0;
        for (index = 0; index < min_max.size(); index++) {
            if (temp_digit == 0) {
                temp_digit = ((Integer) min_max.get(index)).intValue();
            } else {
                this.mMax = temp_digit > ((Integer) min_max.get(index)).intValue() ? temp_digit : ((Integer) min_max.get(index)).intValue();
                this.mCurrent = temp_digit <= ((Integer) min_max.get(index)).intValue() ? temp_digit : ((Integer) min_max.get(index)).intValue();
            }
        }
    }
}
