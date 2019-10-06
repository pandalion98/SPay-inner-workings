package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;
import java.util.Timer;
import java.util.TimerTask;

public class GripSensorTestTouchIc extends BaseActivity {
    /* access modifiers changed from: private */
    public static int IS_GRIP_COUNT = 0;
    private static final int RELEASEALL = 1;
    private static final int VIB_STRONG = 1;
    private static final int VIB_WEAK = 0;
    private static final int WORKING1 = 0;
    private static final int WORKING2 = 2;
    private static final int WORKINGALL = 3;
    private final String CLASS_NAME = "GripSensorTestTouchIc";
    /* access modifiers changed from: private */
    public boolean RESET_FLUG = true;
    private final int VIBRATE_TIME = 65535;
    byte WHAT_EXIT = 1;
    byte WHAT_NOTI_SENSOR_UPDATAE = 0;
    /* access modifiers changed from: private */
    public TextView info1;
    /* access modifiers changed from: private */
    public TextView info2;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout1;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout2;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (GripSensorTestTouchIc.IS_GRIP_COUNT == 2) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.working);
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout2, C0268R.color.black);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.working);
                        GripSensorTestTouchIc.this.info2.setText(C0268R.string.release);
                        GripSensorTestTouchIc.this.ActiveVibrate(1);
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 2 WORKING1");
                        return;
                    }
                    return;
                case 1:
                    if (GripSensorTestTouchIc.IS_GRIP_COUNT == 1) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.black);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.release);
                        GripSensorTestTouchIc.this.stopVibration();
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 1 RELEASE");
                        return;
                    } else if (GripSensorTestTouchIc.IS_GRIP_COUNT == 2) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.black);
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout2, C0268R.color.black);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.release);
                        GripSensorTestTouchIc.this.info2.setText(C0268R.string.release);
                        GripSensorTestTouchIc.this.stopVibration();
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 2 RELEASE");
                        return;
                    } else {
                        return;
                    }
                case 2:
                    if (GripSensorTestTouchIc.IS_GRIP_COUNT == 2) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.black);
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout2, C0268R.color.working);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.release);
                        GripSensorTestTouchIc.this.info2.setText(C0268R.string.working);
                        GripSensorTestTouchIc.this.ActiveVibrate(1);
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 2 WORKING2");
                        return;
                    }
                    return;
                case 3:
                    if (GripSensorTestTouchIc.IS_GRIP_COUNT == 1) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.working);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.working);
                        GripSensorTestTouchIc.this.ActiveVibrate(1);
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 1 WORKINGALL");
                        return;
                    } else if (GripSensorTestTouchIc.IS_GRIP_COUNT == 2) {
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout1, C0268R.color.working);
                        GripSensorTestTouchIc.this.setBackgroundColor(GripSensorTestTouchIc.this.mBackgroudLayout2, C0268R.color.working);
                        GripSensorTestTouchIc.this.info1.setText(C0268R.string.working);
                        GripSensorTestTouchIc.this.info2.setText(C0268R.string.working);
                        GripSensorTestTouchIc.this.ActiveVibrate(1);
                        LtUtil.log_i("GripSensorTestTouchIc", "Handler", "GRIP COUNT 2 WORKINGALL");
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Thread mThreadGettest = null;
    private Vibrator mVibrator;
    WakeLock mWl;
    private boolean pass1 = false;
    private boolean pass2 = false;
    float[] temp;
    private TextView txtgripsensor1;
    private TextView txtgripsensor2;
    private boolean working = false;

    public GripSensorTestTouchIc() {
        super("GripSensorTestTouchIc");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gripsensor_touchic);
        init();
        LtUtil.log_d("GripSensorTestTouchIc", "onCreate", "onCreate");
    }

    public void onResume() {
        super.onResume();
        gripOn();
        sensorOn();
        LtUtil.log_d("GripSensorTestTouchIc", "onResume", "onResume");
        this.mWl.acquire();
    }

    public void onPause() {
        this.mWl.release();
        this.mVibrator.cancel();
        sensorOff();
        gripOff();
        super.onPause();
        LtUtil.log_d("GripSensorTestTouchIc", "onPause", "onPause");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        String gripCount = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_GRIP);
        this.mWl = ((PowerManager) getSystemService("power")).newWakeLock(26, "My Lock Tag");
        this.txtgripsensor1 = (TextView) findViewById(C0268R.C0269id.grip_str_sen1);
        this.txtgripsensor2 = (TextView) findViewById(C0268R.C0269id.grip_str_sen2);
        this.info1 = (TextView) findViewById(C0268R.C0269id.info1);
        this.info2 = (TextView) findViewById(C0268R.C0269id.info2);
        this.info1.setText(C0268R.string.release);
        this.info2.setText(C0268R.string.release);
        this.mBackgroudLayout1 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_side);
        this.mBackgroudLayout2 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_back);
        if (gripCount.length() > 0) {
            IS_GRIP_COUNT = Integer.valueOf(gripCount).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IS_GRIP_COUNT : ");
        sb.append(IS_GRIP_COUNT);
        LtUtil.log_i("GripSensorTestTouchIc", "init", sb.toString());
        if (IS_GRIP_COUNT == 1) {
            this.mBackgroudLayout2.setVisibility(8);
            this.txtgripsensor1.setVisibility(8);
            this.txtgripsensor2.setVisibility(8);
            this.info2.setVisibility(8);
        }
        this.mVibrator = (Vibrator) getSystemService("vibrator");
    }

    public void setBackgroundColor(LinearLayout layout, int id) {
        layout.setBackgroundColor(getResources().getColor(id));
    }

    public void onFinish() {
        LtUtil.log_d("GripSensorTestTouchIc", "onFinish", "GripSensor Test finish");
        this.mVibrator.cancel();
        finish();
    }

    public void onExit() {
        LtUtil.log_d("GripSensorTestTouchIc", "onExit", "finish");
        this.mVibrator.cancel();
        setResult(0);
        finish();
    }

    private void gripOn() {
        LtUtil.log_d("GripSensorTestTouchIc", "gripOn", "gripOn : 1");
        Kernel.write(Kernel.GRIP_TOUCH_SENSOR_ENABLE, EgisFingerprint.MAJOR_VERSION);
    }

    private void gripOff() {
        LtUtil.log_d("GripSensorTestTouchIc", "gripOn", "gripOff : 0");
        Kernel.write(Kernel.GRIP_TOUCH_SENSOR_ENABLE, "0");
    }

    /* access modifiers changed from: private */
    public void gripReset() {
        LtUtil.log_d("GripSensorTestTouchIc", "gripReset", "gripReset");
        this.RESET_FLUG = false;
        Kernel.write(Kernel.GRIP_TOUCH_SENSOR_RESET, EgisFingerprint.MAJOR_VERSION);
    }

    private void startVibration(int intensity) {
        LtUtil.log_i("GripSensorTestTouchIc", "startVibration", "Vibration start");
        long[] pattern = {30, 100};
        if (intensity == 0) {
            pattern[0] = 30;
            pattern[1] = 100;
        } else if (intensity == 1) {
            pattern[0] = 30;
            pattern[1] = 400;
        }
        this.mVibrator.vibrate(pattern, 0);
    }

    private void sensorOn() {
        this.mThreadGettest = new Thread(new Runnable() {
            public void run() {
                while (!GripSensorTestTouchIc.this.mThreadGettest.isInterrupted()) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                        LtUtil.log_e(ie);
                    }
                    if (GripSensorTestTouchIc.this.RESET_FLUG) {
                        GripSensorTestTouchIc.this.gripReset();
                        Kernel.read(Kernel.GRIP_TOUCH_SENSOR_CHECK);
                    } else {
                        GripSensorTestTouchIc.this.UpdateCSPercent();
                    }
                }
            }
        });
        this.mThreadGettest.start();
    }

    private void sensorOff() {
        while (this.mThreadGettest != null && this.mThreadGettest.isAlive()) {
            this.mThreadGettest.interrupt();
        }
        this.mThreadGettest = null;
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(2);
    }

    /* access modifiers changed from: private */
    public void UpdateCSPercent() {
        String status1 = Kernel.read(Kernel.GRIP_TOUCH_SENSOR_CHECK);
        if (IS_GRIP_COUNT == 2) {
            String status2 = Kernel.read(Kernel.GRIP_TOUCH_SENSOR_CHECK);
            if ("0".equals(status1) && "0".equals(status2)) {
                this.mHandler.sendEmptyMessage(1);
            } else if (EgisFingerprint.MAJOR_VERSION.equals(status1) && "0".equals(status2)) {
                this.mHandler.sendEmptyMessage(0);
            } else if (EgisFingerprint.MAJOR_VERSION.equals(status1) && EgisFingerprint.MAJOR_VERSION.equals(status2)) {
                this.mHandler.sendEmptyMessage(3);
            } else if ("0".equals(status1) && EgisFingerprint.MAJOR_VERSION.equals(status2)) {
                this.mHandler.sendEmptyMessage(2);
            }
        } else if (IS_GRIP_COUNT != 1) {
        } else {
            if ("0".equals(status1)) {
                this.mHandler.sendEmptyMessage(1);
            } else if (EgisFingerprint.MAJOR_VERSION.equals(status1)) {
                this.mHandler.sendEmptyMessage(3);
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopVibration() {
        this.mVibrator.cancel();
        LtUtil.log_i("GripSensorTestTouchIc", "stopVibration", "Vibration stop");
    }

    /* access modifiers changed from: private */
    public void ActiveVibrate(int intensity) {
        startVibration(intensity);
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 500);
    }
}
