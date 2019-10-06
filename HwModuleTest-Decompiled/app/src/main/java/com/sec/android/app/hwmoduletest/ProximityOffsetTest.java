package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import egis.client.api.EgisFingerprint;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ProximityOffsetTest extends BaseActivity implements OnClickListener {
    private static final byte MSG_GET_ADC = 0;
    private static final byte MSG_GET_OFFSET = 1;
    private static final byte MSG_RESET = 2;
    private static final String TAG = "Proximityoffset";
    private final String PATH_PROX_ADC_DATA = "/sys/class/sensors/proximity_sensor/raw_data";
    private final String PATH_PROX_OFFSET = "/sys/class/sensors/proximity_sensor/prox_cal";
    /* access modifiers changed from: private */
    public TextView mAdcView;
    /* access modifiers changed from: private */
    public View mBackview;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TextView access$100 = ProximityOffsetTest.this.mAdcView;
                    StringBuilder sb = new StringBuilder();
                    sb.append("   :  ");
                    sb.append(ProximityOffsetTest.this.mHandlersensor.getADC());
                    access$100.setText(sb.toString());
                    return;
                case 1:
                    TextView access$200 = ProximityOffsetTest.this.mOffsetView;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(":  ");
                    sb2.append(ProximityOffsetTest.this.readOffset());
                    access$200.setText(sb2.toString());
                    return;
                case 2:
                    TextView access$2002 = ProximityOffsetTest.this.mOffsetView;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(":  ");
                    sb3.append(ProximityOffsetTest.this.readOffset());
                    access$2002.setText(sb3.toString());
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public handlersensor mHandlersensor;
    private Button mOffsetBtn;
    private TextView mOffsetResult;
    private int mOffsetValue = 0;
    /* access modifiers changed from: private */
    public TextView mOffsetView;
    private Button mResetBtn;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Timer mTimer;
    private Vibrator mVibrator;
    /* access modifiers changed from: private */
    public TextView mWorkView;

    private class handlersensor extends TimerTask implements SensorEventListener {
        private String data;
        private boolean mIsRunningTask;
        private int mValue;

        private handlersensor() {
            this.mIsRunningTask = false;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            LtUtil.log_i(ProximityOffsetTest.this.CLASS_NAME, "onSensorChanged", "SensorChanged!!!");
            if (((int) event.values[0]) == 0) {
                ProximityOffsetTest.this.mWorkView.setText("Working");
                ProximityOffsetTest.this.mWorkView.setVisibility(0);
                ProximityOffsetTest.this.mBackview.setBackgroundColor(-16711936);
                ProximityOffsetTest.this.startVibrate();
                return;
            }
            ProximityOffsetTest.this.mWorkView.setText("Release");
            ProximityOffsetTest.this.mWorkView.setVisibility(0);
            ProximityOffsetTest.this.mBackview.setBackgroundColor(-1);
            ProximityOffsetTest.this.stopVibrate();
        }

        public void run() {
            if (this.mIsRunningTask) {
                readToProximitySensor();
                ProximityOffsetTest.this.mHandler.sendEmptyMessage(0);
            }
        }

        private void readToProximitySensor() {
            String access$1400;
            String str;
            StringBuilder sb;
            String reader = null;
            try {
                BufferedReader reader2 = new BufferedReader(new FileReader("/sys/class/sensors/proximity_sensor/raw_data"));
                this.data = reader2.readLine();
                reader = this.data;
                if (reader != null) {
                    this.mValue = Integer.parseInt(this.data.trim());
                } else {
                    this.mValue = 0;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ADC: ");
                sb2.append(this.mValue);
                LtUtil.log_i(ProximityOffsetTest.this.CLASS_NAME, "readToProximitySensor", sb2.toString());
                try {
                    reader2.close();
                    return;
                } catch (IOException e) {
                    e = e;
                    access$1400 = ProximityOffsetTest.this.CLASS_NAME;
                    str = "readToProximitySensor";
                    sb = new StringBuilder();
                }
                sb.append("File close exception: ");
                sb.append(e.getMessage());
                LtUtil.log_e(access$1400, str, sb.toString());
            } catch (IOException e2) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" ProximitySensor IOException: ");
                sb3.append(e2.getMessage());
                LtUtil.log_e(ProximityOffsetTest.this.CLASS_NAME, "readToProximitySensor", sb3.toString());
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                        e = e3;
                        access$1400 = ProximityOffsetTest.this.CLASS_NAME;
                        str = "readToProximitySensor";
                        sb = new StringBuilder();
                    }
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                        String access$14002 = ProximityOffsetTest.this.CLASS_NAME;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("File close exception: ");
                        sb4.append(e4.getMessage());
                        LtUtil.log_e(access$14002, "readToProximitySensor", sb4.toString());
                    }
                }
            }
        }

        public int getADC() {
            return this.mValue;
        }

        /* access modifiers changed from: private */
        public void resume() {
            this.mIsRunningTask = true;
        }

        /* access modifiers changed from: private */
        public void pause() {
            this.mIsRunningTask = false;
        }
    }

    public ProximityOffsetTest() {
        super("ProximityOffsetTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.proximity_offset_test);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(65592);
        if (this.mSensor == null) {
            this.mSensor = this.mSensorManager.getDefaultSensor(8);
        }
        this.mBackview = findViewById(C0268R.C0269id.backview);
        this.mWorkView = (TextView) findViewById(C0268R.C0269id.WorkView);
        this.mAdcView = (TextView) findViewById(C0268R.C0269id.ADC);
        this.mOffsetView = (TextView) findViewById(C0268R.C0269id.Offset);
        this.mOffsetResult = (TextView) findViewById(C0268R.C0269id.offset_result);
        this.mOffsetBtn = (Button) findViewById(C0268R.C0269id.Offset_btn);
        this.mOffsetBtn.setOnClickListener(this);
        this.mResetBtn = (Button) findViewById(C0268R.C0269id.Reset_btn);
        this.mResetBtn.setOnClickListener(this);
        this.mTimer = new Timer();
        this.mHandlersensor = new handlersensor();
        this.mTimer.schedule(this.mHandlersensor, 0, 1000);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LtUtil.log_i(this.CLASS_NAME, "onResume", "onResume");
        super.onResume();
        this.mSensorManager.registerListener(this.mHandlersensor, this.mSensor, 2);
        this.mHandlersensor.resume();
        this.mHandler.sendEmptyMessage(0);
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_i(this.CLASS_NAME, "onPause", "onPause");
        super.onPause();
        this.mSensorManager.unregisterListener(this.mHandlersensor);
        stopVibrate();
        this.mHandlersensor.pause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_i(this.CLASS_NAME, "onDestroy", "onDestroy");
        super.onDestroy();
        this.mTimer.cancel();
    }

    /* access modifiers changed from: private */
    public void startVibrate() {
        this.mVibrator.vibrate(new long[]{0, 5000}, 0);
    }

    /* access modifiers changed from: private */
    public void stopVibrate() {
        this.mVibrator.cancel();
    }

    public void onClick(View view) {
        if (view.getId() == C0268R.C0269id.Offset_btn) {
            LtUtil.log_i(this.CLASS_NAME, "onClick", "offset");
            writeFile("/sys/class/sensors/proximity_sensor/prox_cal", "0");
            writeFile("/sys/class/sensors/proximity_sensor/prox_cal", EgisFingerprint.MAJOR_VERSION);
            String[] OffsetValue = readOffset().split(",");
            this.mOffsetValue = Integer.parseInt(OffsetValue[0]);
            int threshold = (int) (((double) Integer.parseInt(OffsetValue[1].trim())) * 1.5d);
            this.mHandler.sendEmptyMessage(1);
            if (this.mOffsetValue > threshold) {
                this.mOffsetResult.setText("Fail");
                this.mOffsetResult.setTextColor(-65536);
                return;
            }
            this.mOffsetResult.setText("Pass");
            this.mOffsetResult.setTextColor(-16776961);
        } else if (view.getId() == C0268R.C0269id.Reset_btn) {
            LtUtil.log_i(this.CLASS_NAME, "onClick", "reset");
            writeFile("/sys/class/sensors/proximity_sensor/prox_cal", "0");
            this.mHandler.sendEmptyMessage(2);
        }
    }

    private boolean writeFile(String filepath, String value) {
        FileWriter fw = null;
        boolean result = false;
        try {
            fw = new FileWriter(filepath);
            fw.write(value);
            result = true;
            try {
                fw.close();
            } catch (IOException e) {
                LtUtil.log_e(this.CLASS_NAME, "writeFile", "IOException");
            }
        } catch (IOException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("IOExceptionfilepath : ");
            sb.append(filepath);
            sb.append(" value : ");
            sb.append(value);
            LtUtil.log_e(this.CLASS_NAME, "writeFile", sb.toString());
            if (fw != null) {
                fw.close();
            }
        } catch (Throwable th) {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e3) {
                    LtUtil.log_e(this.CLASS_NAME, "writeFile", "IOException");
                }
            }
            throw th;
        }
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readOffset() {
        /*
            r7 = this;
            r0 = 0
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0058 }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ IOException -> 0x0058 }
            java.lang.String r4 = "/sys/class/sensors/proximity_sensor/prox_cal"
            r3.<init>(r4)     // Catch:{ IOException -> 0x0058 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0058 }
            r1 = r2
            java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x0058 }
            r0 = r2
            if (r0 == 0) goto L_0x001b
            java.lang.String r2 = r0.trim()     // Catch:{ IOException -> 0x0058 }
            r0 = r2
        L_0x001b:
            java.lang.String r2 = r7.CLASS_NAME     // Catch:{ IOException -> 0x0058 }
            java.lang.String r3 = "readOffset"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0058 }
            r4.<init>()     // Catch:{ IOException -> 0x0058 }
            java.lang.String r5 = "Offset: "
            r4.append(r5)     // Catch:{ IOException -> 0x0058 }
            r4.append(r0)     // Catch:{ IOException -> 0x0058 }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0058 }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r2, r3, r4)     // Catch:{ IOException -> 0x0058 }
            r1.close()     // Catch:{ IOException -> 0x0038 }
        L_0x0037:
            goto L_0x0086
        L_0x0038:
            r2 = move-exception
            java.lang.String r3 = r7.CLASS_NAME
            java.lang.String r4 = "readOffset"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
        L_0x0042:
            java.lang.String r6 = "File close exception: "
            r5.append(r6)
            java.lang.String r6 = r2.getMessage()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r3, r4, r5)
            goto L_0x0037
        L_0x0056:
            r2 = move-exception
            goto L_0x008d
        L_0x0058:
            r2 = move-exception
            java.lang.String r3 = r7.CLASS_NAME     // Catch:{ all -> 0x0056 }
            java.lang.String r4 = "readOffset"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0056 }
            r5.<init>()     // Catch:{ all -> 0x0056 }
            java.lang.String r6 = " ProximitySensor IOException: "
            r5.append(r6)     // Catch:{ all -> 0x0056 }
            java.lang.String r6 = r2.getMessage()     // Catch:{ all -> 0x0056 }
            r5.append(r6)     // Catch:{ all -> 0x0056 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0056 }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r3, r4, r5)     // Catch:{ all -> 0x0056 }
            if (r1 == 0) goto L_0x0086
            r1.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x0037
        L_0x007b:
            r2 = move-exception
            java.lang.String r3 = r7.CLASS_NAME
            java.lang.String r4 = "readOffset"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            goto L_0x0042
        L_0x0086:
            if (r0 != 0) goto L_0x008b
            java.lang.String r2 = "NONE"
            goto L_0x008c
        L_0x008b:
            r2 = r0
        L_0x008c:
            return r2
        L_0x008d:
            if (r1 == 0) goto L_0x00b0
            r1.close()     // Catch:{ IOException -> 0x0093 }
            goto L_0x00b0
        L_0x0093:
            r3 = move-exception
            java.lang.String r4 = r7.CLASS_NAME
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "File close exception: "
            r5.append(r6)
            java.lang.String r6 = r3.getMessage()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            java.lang.String r6 = "readOffset"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r4, r6, r5)
        L_0x00b0:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.ProximityOffsetTest.readOffset():java.lang.String");
    }
}
