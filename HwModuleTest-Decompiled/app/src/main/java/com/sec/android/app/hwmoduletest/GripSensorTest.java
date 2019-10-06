package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.Phone;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.xmldata.support.Support.HwTestMenu;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class GripSensorTest extends BaseActivity {
    /* access modifiers changed from: private */
    public static int IS_GRIP_COUNT = 0;
    protected static final int KEY_TIMEOUT = 2;
    protected static final int KEY_TIMER_EXPIRED = 1;
    private static final String MASK_SENSING_GRIP_OFF = "000000000000";
    private static final String MASK_SENSING_GRIP_SIDE_ON = "000001000000";
    private static final String MASK_SENSING_GRIP_TOP_ON = "010000000000";
    private static final String MASK_SENSING_GRIP_TOP_SIDE_ON = "010001000000";
    protected static final int MILLIS_IN_SEC = 1000;
    private static final int MSG_DEFAULT_FROM_CP = 1000;
    private static final int MSG_GET_GRIPSTATE_FROM_CP = 1001;
    private static final int MSG_UI_SENSOR1_PRESS = 1011;
    private static final int MSG_UI_SENSOR1_RELEASE = 1010;
    private static final int VIB_STRONG = 1;
    private static final int VIB_WEAK = 0;
    private final String CLASS_NAME = "GripSensor Test";
    /* access modifiers changed from: private */
    public String GripState = "0";
    private Handler IPCHandler = new Handler() {
        String[] getDataArr;

        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                GripSensorTest.this.mResultData = msg.getData().getByteArray("response");
                if (GripSensorTest.this.mResultData != null) {
                    this.getDataArr = GripSensorTest.this.byte2Int(GripSensorTest.this.mResultData);
                    if (this.getDataArr != null && this.getDataArr.length >= 1) {
                        GripSensorTest.this.GripState = this.getDataArr[0];
                        StringBuilder sb = new StringBuilder();
                        sb.append("Grip Sensor Grip State : ");
                        sb.append(GripSensorTest.this.GripState);
                        LtUtil.log_i("GripSensor Test", "MSG_GET_GRIPSTATE_FROM_CP", sb.toString());
                        if ("0".equalsIgnoreCase(GripSensorTest.this.GripState)) {
                            GripSensorTest.this.UIHandler.sendEmptyMessage(1010);
                        } else {
                            GripSensorTest.this.UIHandler.sendEmptyMessage(1011);
                        }
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler UIHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1010:
                    GripSensorTest.this.stopVibration();
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                    GripSensorTest.this.info1.setText(C0268R.string.release);
                    LtUtil.log_i("GripSensor Test", "UIHandler", "release grip sensor1");
                    return;
                case 1011:
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.working);
                    GripSensorTest.this.info1.setText(C0268R.string.working);
                    GripSensorTest.this.ActiveVibrate(0);
                    LtUtil.log_i("GripSensor Test", "UIHandler", "working grip sensor1");
                    return;
                default:
                    return;
            }
        }
    };
    private final int VIBRATE_TIME = 65535;
    /* access modifiers changed from: private */
    public TextView info1;
    /* access modifiers changed from: private */
    public TextView info2;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout1;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout2;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("Action :");
            sb.append(action);
            LtUtil.log_i("GripSensor Test", "onReceive", sb.toString());
            if ("com.sec.android.app.factorytest".equals(action)) {
                String cmdData = intent.getStringExtra("COMMAND");
                String sensorData = "null";
                if (cmdData != null) {
                    sensorData = cmdData.substring(6);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("cmdData==");
                sb2.append(cmdData);
                LtUtil.log_i("GripSensor Test", "onReceive", sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("sensorData==");
                sb3.append(sensorData);
                LtUtil.log_i("GripSensor Test", "onReceive", sb3.toString());
                if (GripSensorTest.IS_GRIP_COUNT == 1) {
                    LtUtil.log_i("GripSensor Test", "onReceive", "IS_GRIP_COUNT == 1");
                    if ("0100".equals(sensorData)) {
                        GripSensorTest.this.stopVibration();
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                        GripSensorTest.this.info1.setText(C0268R.string.release);
                        LtUtil.log_i("GripSensor Test", "onReceive", "________ 0100 ________");
                    } else if ("0000".equals(sensorData)) {
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.working);
                        GripSensorTest.this.info1.setText(C0268R.string.working);
                        GripSensorTest.this.ActiveVibrate(0);
                        LtUtil.log_i("GripSensor Test", "onReceive", "________ 0000 ________");
                    } else if (GripSensorTest.MASK_SENSING_GRIP_TOP_SIDE_ON.equals(sensorData) || GripSensorTest.MASK_SENSING_GRIP_TOP_ON.equals(sensorData) || GripSensorTest.MASK_SENSING_GRIP_SIDE_ON.equals(sensorData)) {
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.working);
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.working);
                        GripSensorTest.this.info1.setText(C0268R.string.working);
                        GripSensorTest.this.info2.setText(C0268R.string.working);
                        GripSensorTest.this.ActiveVibrate(1);
                    } else {
                        GripSensorTest.this.stopVibration();
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                        GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.black);
                        GripSensorTest.this.info1.setText(C0268R.string.release);
                        GripSensorTest.this.info2.setText(C0268R.string.release);
                        LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_OFF");
                    }
                } else if (GripSensorTest.MASK_SENSING_GRIP_TOP_SIDE_ON.equals(sensorData)) {
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.working);
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.working);
                    GripSensorTest.this.info1.setText(C0268R.string.working);
                    GripSensorTest.this.info2.setText(C0268R.string.working);
                    GripSensorTest.this.ActiveVibrate(1);
                    LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_SIDE_BACK_ON");
                } else if (GripSensorTest.MASK_SENSING_GRIP_TOP_ON.equals(sensorData)) {
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.working);
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.black);
                    GripSensorTest.this.info1.setText(C0268R.string.working);
                    GripSensorTest.this.info2.setText(C0268R.string.release);
                    GripSensorTest.this.ActiveVibrate(0);
                    LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_SIDE_ON");
                } else if (GripSensorTest.MASK_SENSING_GRIP_SIDE_ON.equals(sensorData)) {
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.working);
                    GripSensorTest.this.info1.setText(C0268R.string.release);
                    GripSensorTest.this.info2.setText(C0268R.string.working);
                    GripSensorTest.this.ActiveVibrate(0);
                    LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_BACK_ON");
                } else if (GripSensorTest.MASK_SENSING_GRIP_OFF.equals(sensorData)) {
                    GripSensorTest.this.stopVibration();
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.black);
                    GripSensorTest.this.info1.setText(C0268R.string.release);
                    GripSensorTest.this.info2.setText(C0268R.string.release);
                    LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_OFF");
                } else {
                    GripSensorTest.this.stopVibration();
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout1, C0268R.color.black);
                    GripSensorTest.this.setBackgroundColor(GripSensorTest.this.mBackgroudLayout2, C0268R.color.black);
                    GripSensorTest.this.info1.setText(C0268R.string.release);
                    GripSensorTest.this.info2.setText(C0268R.string.release);
                    LtUtil.log_i("GripSensor Test", "onReceive", "MASK_SENSING_GRIP_OFF else");
                }
            } else if ("com.android.samsungtest.GripTestStop".equals(action)) {
                GripSensorTest.this.sendToRilGripSensorStop();
                LtUtil.log_i("GripSensor Test", "onReceive", "GRIPGRIP *******   GripSensorTest - onReceive - get Stop GripTest ******* GRIPGRIP");
                GripSensorTest.this.finish();
            }
        }
    };
    private long mCurrentTime = 0;
    /* access modifiers changed from: private */
    public FactoryTestPhone mFactoryPhone = null;
    private boolean mIsPressedBackkey = false;
    /* access modifiers changed from: private */
    public boolean mIsThreading = true;
    /* access modifiers changed from: private */
    public byte[] mResultData;
    /* access modifiers changed from: private */
    public Thread mThread = null;
    private Vibrator mVibrator;
    WakeLock mWl;
    private boolean pass1 = false;
    private boolean pass2 = false;
    private Phone phone = null;
    private TextView txtgripsensor1;
    private TextView txtgripsensor2;
    private boolean working = false;

    enum GripData {
        RESET(3),
        GRIP_STATE(6),
        INTERRUPT_ON(8),
        INTERRUPT_OFF(9);
        
        private int value;

        private GripData(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }

    public GripSensorTest() {
        super("GripSensorTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gripsensor);
        LtUtil.log_d("GripSensor Test", "onCreate", "onCreate");
    }

    public void onResume() {
        super.onResume();
        LtUtil.log_d("GripSensor Test", "onResume", "onResume");
        ConnectSecPhoneService();
        StringBuilder sb = new StringBuilder();
        sb.append("send Grip Sensor SET : 113 RESET : ");
        sb.append((byte) GripData.RESET.getValue());
        LtUtil.log_i("GripSensor Test", "onResume", sb.toString());
        this.mFactoryPhone.sendGripControlToRil(FactoryTestPhone.OEM_MISC_SET_GRIP_SENSOR_INFO, (byte) GripData.RESET.getValue(), 1000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("send Grip Sensor SET : 113 INTERRUPT_ON : ");
        sb2.append((byte) GripData.INTERRUPT_ON.getValue());
        LtUtil.log_i("GripSensor Test", "onResume", sb2.toString());
        this.mFactoryPhone.sendGripControlToRil(FactoryTestPhone.OEM_MISC_SET_GRIP_SENSOR_INFO, (byte) GripData.INTERRUPT_ON.getValue(), 1000);
        init();
        RunThread();
        this.mWl.acquire();
    }

    public void onPause() {
        this.mWl.release();
        this.IPCHandler.removeMessages(1001);
        this.UIHandler.removeMessages(1011);
        if (this.mThread != null) {
            do {
                this.mThread.interrupt();
                Sleep.sleep(100);
            } while (this.mIsThreading);
        }
        if (this.mFactoryPhone != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("send Grip Sensor SET : 113 INTERRUPT_OFF : ");
            sb.append((byte) GripData.INTERRUPT_OFF.getValue());
            LtUtil.log_i("GripSensor Test", "onPause", sb.toString());
            this.mFactoryPhone.sendGripControlToRil(FactoryTestPhone.OEM_MISC_SET_GRIP_SENSOR_INFO, (byte) GripData.INTERRUPT_OFF.getValue(), 1000);
            this.mFactoryPhone.unbindSecPhoneService();
        }
        this.mVibrator.cancel();
        LtUtil.log_d("GripSensor Test", "onPause", "onPause");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mVibrator.cancel();
        super.onDestroy();
    }

    private void sendToRilGripSensorStart() {
        LtUtil.log_i("GripSensor Test", "sendToRilGripSensorStart", "sendToRilGripSensorStart=");
        this.mFactoryPhone.requestGripSensorOn(true);
    }

    /* access modifiers changed from: private */
    public void sendToRilGripSensorStop() {
        LtUtil.log_i("GripSensor Test", "sendToRilGripSensorStop", "sendToRilGripSensorStop=");
        this.mFactoryPhone.requestGripSensorOn(false);
    }

    private void init() {
        this.mWl = ((PowerManager) getSystemService("power")).newWakeLock(26, "My Lock Tag");
        this.txtgripsensor1 = (TextView) findViewById(C0268R.C0269id.grip_str_sen1);
        this.txtgripsensor2 = (TextView) findViewById(C0268R.C0269id.grip_str_sen2);
        this.info1 = (TextView) findViewById(C0268R.C0269id.info1);
        this.info2 = (TextView) findViewById(C0268R.C0269id.info2);
        this.mBackgroudLayout1 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_side);
        this.mBackgroudLayout2 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_back);
        String gripCount = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_GRIP);
        if (gripCount.length() > 0) {
            IS_GRIP_COUNT = Integer.valueOf(gripCount).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IS_GRIP_COUNT : ");
        sb.append(IS_GRIP_COUNT);
        LtUtil.log_i("GripSensor Test", "init", sb.toString());
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
        LtUtil.log_d("GripSensor Test", "onFinish", "GripSensor Test finish");
        this.mVibrator.cancel();
        finish();
    }

    public void onExit() {
        LtUtil.log_d("GripSensor Test", "onExit", "finish");
        this.mVibrator.cancel();
        setResult(0);
        finish();
    }

    private void startVibration(int intensity) {
        LtUtil.log_i("GripSensor Test", "startVibration", "Vibration start");
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

    /* access modifiers changed from: private */
    public void stopVibration() {
        this.mVibrator.cancel();
        LtUtil.log_i("GripSensor Test", "stopVibration", "Vibration stop");
    }

    /* access modifiers changed from: private */
    public void ActiveVibrate(int intensity) {
        startVibration(intensity);
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 500);
    }

    private void ConnectSecPhoneService() {
        if (this.mFactoryPhone == null) {
            this.mFactoryPhone = new FactoryTestPhone(this, this.IPCHandler);
        }
        if (this.mFactoryPhone != null) {
            this.mFactoryPhone.bindSecPhoneService();
        }
    }

    private void RunThread() {
        this.mThread = new Thread(new Runnable() {
            public void run() {
                while (!GripSensorTest.this.mThread.isInterrupted()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("send Grip Sensor GET : 112 GRIP_STATE : ");
                    sb.append((byte) GripData.GRIP_STATE.getValue());
                    LtUtil.log_i("GripSensor Test", "RunThread", sb.toString());
                    GripSensorTest.this.mFactoryPhone.sendGripControlToRil(FactoryTestPhone.OEM_MISC_GET_GRIP_SENSOR_INFO, (byte) GripData.GRIP_STATE.getValue(), 1001);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        LtUtil.log_e(ie);
                    }
                }
                GripSensorTest.this.mIsThreading = false;
            }
        });
        this.mThread.start();
    }

    /* access modifiers changed from: private */
    public String[] byte2Int(byte[] src) {
        String[] strArr = {"", "", "", ""};
        if (src == null) {
            return strArr;
        }
        IntBuffer intBuf = ByteBuffer.wrap(src).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        for (int i = 0; i < array.length; i++) {
            strArr[i] = String.valueOf(array[i]);
        }
        return strArr;
    }
}
