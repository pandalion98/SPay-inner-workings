package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.VibrationEffect.SemMagnitudeType;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class VibrationTest extends BaseActivity {
    private static final String ACTION_OQCSBFTT_READ_DATA = "com.sec.factory.OQCSBFTT.READ_DATA";
    private static final int MAGNITUDE_MAX = 10000;
    public static final long TIMELESS_VIBRATOR = 2147483647L;
    /* access modifiers changed from: private */
    public Thread mAccSensorThread = null;
    private Sensor mAcceSensor;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("Action :");
            sb.append(action);
            LtUtil.log_d(VibrationTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb.toString());
            if (action.equals(VibrationTest.ACTION_OQCSBFTT_READ_DATA)) {
                int item = intent.getIntExtra("item", 0);
                int time = intent.getIntExtra("time", 0);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ITEM :");
                sb2.append(item);
                LtUtil.log_d(VibrationTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("time : ");
                sb3.append(time);
                LtUtil.log_d(VibrationTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb3.toString());
                if (item == 405) {
                    int[] access$400 = VibrationTest.this.mRawMax;
                    int[] access$4002 = VibrationTest.this.mRawMax;
                    VibrationTest.this.mRawMax[2] = Integer.MIN_VALUE;
                    access$4002[1] = Integer.MIN_VALUE;
                    access$400[0] = Integer.MIN_VALUE;
                    int[] access$500 = VibrationTest.this.mRawMin;
                    int[] access$5002 = VibrationTest.this.mRawMin;
                    VibrationTest.this.mRawMin[2] = Integer.MAX_VALUE;
                    access$5002[1] = Integer.MAX_VALUE;
                    access$500[0] = Integer.MAX_VALUE;
                    VibrationTest.this.mRawAvg[0] = (double) VibrationTest.this.mRawData[0];
                    VibrationTest.this.mRawAvg[1] = (double) VibrationTest.this.mRawData[1];
                    VibrationTest.this.mRawAvg[2] = (double) VibrationTest.this.mRawData[2];
                    VibrationTest.this.mRawCnt = 1;
                }
                if (VibrationTest.this.mOqcTimer == null) {
                    VibrationTest.this.mOqcTimer = new Timer("OqcDataReadTimer");
                }
                VibrationTest.this.mOqcTimer.schedule(new TimerTask() {
                    public void run() {
                        FileOutputStream out;
                        VibrationTest.this.mDataGet = false;
                        StringBuilder sb = new StringBuilder();
                        sb.append(VibrationTest.this.mRawMin[0]);
                        sb.append(",");
                        sb.append(VibrationTest.this.mRawMax[0]);
                        sb.append(",");
                        sb.append((int) VibrationTest.this.mRawAvg[0]);
                        sb.append(",");
                        sb.append(VibrationTest.this.mRawMin[1]);
                        sb.append(",");
                        sb.append(VibrationTest.this.mRawMax[1]);
                        sb.append(",");
                        sb.append((int) VibrationTest.this.mRawAvg[1]);
                        sb.append(",");
                        sb.append(VibrationTest.this.mRawMin[2]);
                        sb.append(",");
                        sb.append(VibrationTest.this.mRawMax[2]);
                        sb.append(",");
                        sb.append((int) VibrationTest.this.mRawAvg[2]);
                        String result = sb.toString();
                        String secefsOqcsbfttDirectory = Kernel.getFilePath(Kernel.SECEFS_OQCSBFTT_DIRECTORY);
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(secefsOqcsbfttDirectory);
                            sb2.append("/");
                            sb2.append(405);
                            out = new FileOutputStream(sb2.toString());
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Data : ");
                            sb3.append(result);
                            LtUtil.log_d(VibrationTest.this.CLASS_NAME, "OQC Timer task expired", sb3.toString());
                            out.write(result.getBytes());
                            out.close();
                            out.close();
                            return;
                        } catch (IOException e) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("IOException : ");
                            sb4.append(e.getMessage());
                            LtUtil.log_d(VibrationTest.this.CLASS_NAME, "mOqcTask", sb4.toString());
                            return;
                        } catch (Throwable th) {
                            r3.addSuppressed(th);
                        }
                        throw th;
                    }
                }, (long) time);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mDataGet = false;
    private boolean mFeatureTouchFinish = true;
    /* access modifiers changed from: private */
    public Timer mOqcTimer;
    /* access modifiers changed from: private */
    public double[] mRawAvg = new double[3];
    /* access modifiers changed from: private */
    public int mRawCnt = 0;
    /* access modifiers changed from: private */
    public int[] mRawData = new int[3];
    /* access modifiers changed from: private */
    public int[] mRawMax = new int[3];
    /* access modifiers changed from: private */
    public int[] mRawMin = new int[3];
    private AccelSensorListener mSensorListener = new AccelSensorListener();
    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    private Object vibratorInstance;

    private class AccelSensorListener implements SensorEventListener {
        private AccelSensorListener() {
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            VibrationTest.this.mAccSensorThread = new Thread(new Runnable() {
                public void run() {
                    String rawDataStr = Kernel.read(Kernel.ACCEL_SENSOR_RAW, 0);
                    if (rawDataStr != null) {
                        String[] rawDatas = rawDataStr.trim().split(",");
                        VibrationTest.this.mRawData[0] = Integer.parseInt(rawDatas[0].trim());
                        VibrationTest.this.mRawData[1] = Integer.parseInt(rawDatas[1].trim());
                        VibrationTest.this.mRawData[2] = Integer.parseInt(rawDatas[2].trim());
                    } else {
                        LtUtil.log_d(VibrationTest.this.CLASS_NAME, "onSensorChanged", "rawDataStr is null");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("mRawData - ");
                    sb.append(VibrationTest.this.mRawData[0]);
                    sb.append(", ");
                    sb.append(VibrationTest.this.mRawData[1]);
                    sb.append(", ");
                    sb.append(VibrationTest.this.mRawData[2]);
                    LtUtil.log_d(VibrationTest.this.CLASS_NAME, "onSensorChanged", sb.toString());
                    VibrationTest.this.mRawAvg[0] = (VibrationTest.this.mRawAvg[0] * (((double) VibrationTest.this.mRawCnt) / (((double) VibrationTest.this.mRawCnt) + 1.0d))) + (((double) VibrationTest.this.mRawData[0]) / (((double) VibrationTest.this.mRawCnt) + 1.0d));
                    VibrationTest.this.mRawAvg[1] = (VibrationTest.this.mRawAvg[1] * (((double) VibrationTest.this.mRawCnt) / (((double) VibrationTest.this.mRawCnt) + 1.0d))) + (((double) VibrationTest.this.mRawData[1]) / (((double) VibrationTest.this.mRawCnt) + 1.0d));
                    VibrationTest.this.mRawAvg[2] = (VibrationTest.this.mRawAvg[2] * (((double) VibrationTest.this.mRawCnt) / (((double) VibrationTest.this.mRawCnt) + 1.0d))) + (((double) VibrationTest.this.mRawData[2]) / (((double) VibrationTest.this.mRawCnt) + 1.0d));
                    VibrationTest.this.mRawCnt = VibrationTest.this.mRawCnt + 1;
                    if (VibrationTest.this.mRawMax[0] < VibrationTest.this.mRawData[0]) {
                        VibrationTest.this.mRawMax[0] = VibrationTest.this.mRawData[0];
                    }
                    if (VibrationTest.this.mRawMin[0] > VibrationTest.this.mRawData[0]) {
                        VibrationTest.this.mRawMin[0] = VibrationTest.this.mRawData[0];
                    }
                    if (VibrationTest.this.mRawMax[1] < VibrationTest.this.mRawData[1]) {
                        VibrationTest.this.mRawMax[1] = VibrationTest.this.mRawData[1];
                    }
                    if (VibrationTest.this.mRawMin[1] > VibrationTest.this.mRawData[1]) {
                        VibrationTest.this.mRawMin[1] = VibrationTest.this.mRawData[1];
                    }
                    if (VibrationTest.this.mRawMax[2] < VibrationTest.this.mRawData[2]) {
                        VibrationTest.this.mRawMax[2] = VibrationTest.this.mRawData[2];
                    }
                    if (VibrationTest.this.mRawMin[2] > VibrationTest.this.mRawData[2]) {
                        VibrationTest.this.mRawMin[2] = VibrationTest.this.mRawData[2];
                    }
                }
            });
            VibrationTest.this.mAccSensorThread.start();
        }
    }

    public VibrationTest() {
        super("VibrationTest");
    }

    private void startVibrate(long milliseconds) {
        if (!TestCase.getEnabled(TestCase.IS_VIBETONZ_UNSUPPORTED_HW) || milliseconds != TIMELESS_VIBRATOR) {
            VibrationEffect vibe = VibrationEffect.createWaveform(new long[]{0, milliseconds}, 0);
            vibe.semSetMagnitudeType(SemMagnitudeType.TYPE_MAX);
            this.mVibrator.vibrate(vibe);
            return;
        }
        String[] patternValues = TestCase.getString(TestCase.IS_VIBETONZ_UNSUPPORTED_HW).split(",");
        VibrationEffect vibe2 = VibrationEffect.createWaveform(new long[]{(long) Integer.parseInt(patternValues[0].trim()), (long) Integer.parseInt(patternValues[1].trim())}, 0);
        vibe2.semSetMagnitudeType(SemMagnitudeType.TYPE_MAX);
        this.mVibrator.vibrate(vibe2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFeatureTouchFinish = Feature.getBoolean(Feature.IS_VIBRATION_FINISH_BY_TOUCH);
        View view = new View(this);
        view.setBackgroundColor(-16777216);
        setContentView(view);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        LtUtil.setRemoveSystemUI(getWindow(), true);
        Intent intent = getIntent();
        try {
            this.vibratorInstance = Class.forName("android.os.SystemVibrator").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NullPointerException e4) {
            e4.printStackTrace();
        } catch (InstantiationException e5) {
            e5.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!this.mFeatureTouchFinish) {
            getWindow().getDecorView().post(new Runnable() {
                public void run() {
                    try {
                        Class.forName("android.view.View").getMethod("twSetEnabledThreeFingerGesture", new Class[]{Boolean.TYPE}).invoke(VibrationTest.this.getWindow().getDecorView(), new Object[]{Boolean.valueOf(false)});
                    } catch (NoSuchMethodException e) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception1", 0).show();
                    } catch (ClassNotFoundException e2) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception2", 0).show();
                    } catch (IllegalAccessException e3) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception3", 0).show();
                    } catch (IllegalArgumentException e4) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception4", 0).show();
                    } catch (InvocationTargetException e5) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception5", 0).show();
                    }
                }
            });
        }
        if (isOqcsbftt) {
            registerOqcsbfttReceiver();
            sensorRegister();
            NVAccessor.setNV(405, NVAccessor.NV_VALUE_ENTER);
        }
        startVibrate(TIMELESS_VIBRATOR);
        LtUtil.log_d(this.CLASS_NAME, "onResume", "Vibrate start");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        try {
            Class.forName("android.os.SystemVibrator").getMethod("resetMagnitude", new Class[0]).invoke(this.vibratorInstance, new Object[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NullPointerException e4) {
            e4.printStackTrace();
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
        }
        if (isOqcsbftt) {
            unregisterReceiver(this.mBroadcastReceiver);
            sensorUnregister();
            if (this.mOqcTimer != null) {
                this.mOqcTimer.purge();
                this.mOqcTimer.cancel();
                this.mOqcTimer = null;
            }
            if (this.mAccSensorThread != null) {
                this.mAccSensorThread.interrupt();
            }
        }
        this.mVibrator.cancel();
        LtUtil.log_d(this.CLASS_NAME, "onPause", "Vibrate stop");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mVibrator = null;
        super.onDestroy();
        if (!this.mFeatureTouchFinish) {
            getWindow().getDecorView().post(new Runnable() {
                public void run() {
                    try {
                        Class.forName("android.view.View").getMethod("twSetEnabledThreeFingerGesture", new Class[]{Boolean.TYPE}).invoke(VibrationTest.this.getWindow().getDecorView(), new Object[]{Boolean.valueOf(true)});
                    } catch (NoSuchMethodException e) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception1", 0).show();
                    } catch (ClassNotFoundException e2) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception2", 0).show();
                    } catch (IllegalAccessException e3) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception3", 0).show();
                    } catch (IllegalArgumentException e4) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception4", 0).show();
                    } catch (InvocationTargetException e5) {
                        Toast.makeText(VibrationTest.this.getBaseContext(), "Exception5", 0).show();
                    }
                }
            });
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mFeatureTouchFinish) {
            if (event.getAction() == 0) {
                Toast.makeText(this, "To finish this test, press the volume down key.", 0).show();
            }
            return false;
        }
        finish();
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mFeatureTouchFinish) {
            return super.onKeyDown(keyCode, event);
        }
        if (event.getAction() == 0) {
            if (keyCode != 4) {
                if (keyCode == 25) {
                    finish();
                } else if (keyCode != 187) {
                    Toast.makeText(this, "To finish this test, press the volume down key.", 0).show();
                }
            }
            if (this.mFeatureTouchFinish) {
                try {
                    Class.forName("android.os.SystemVibrator").getMethod("resetMagnitude", new Class[0]).invoke(this.vibratorInstance, new Object[0]);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (IllegalArgumentException e3) {
                    e3.printStackTrace();
                } catch (NullPointerException e4) {
                    e4.printStackTrace();
                } catch (NoSuchMethodException e5) {
                    e5.printStackTrace();
                } catch (InvocationTargetException e6) {
                    e6.printStackTrace();
                }
                startVibrate(TIMELESS_VIBRATOR);
            }
        }
        return true;
    }

    private void sensorRegister() {
        LtUtil.log_d(this.CLASS_NAME, "sensorRegister", "Sensor Registering now...");
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mAcceSensor = this.mSensorManager.getDefaultSensor(1);
        this.mSensorManager.registerListener(this.mSensorListener, this.mAcceSensor, 1);
        turnLPFOnOff(false);
    }

    private void sensorUnregister() {
        LtUtil.log_d(this.CLASS_NAME, "sensorUnregister", "Sensor Unregistering now...");
        if (this.mSensorManager != null) {
            turnLPFOnOff(true);
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
        this.mSensorManager = null;
        this.mSensorListener = null;
        this.mAcceSensor = null;
    }

    private void turnLPFOnOff(boolean isOn) {
        if (isOn) {
            Kernel.write(Kernel.MOTOR_LPF, EgisFingerprint.MAJOR_VERSION);
        }
        if (!isOn) {
            Kernel.write(Kernel.MOTOR_LPF, "0");
        }
        String str = this.CLASS_NAME;
        String str2 = "turnLPFOnOff";
        StringBuilder sb = new StringBuilder();
        sb.append("Turn motor LPF to ");
        sb.append(isOn ? "On" : "Off");
        LtUtil.log_d(str, str2, sb.toString());
    }

    private void registerOqcsbfttReceiver() {
        IntentFilter OqcsbfttReadData = new IntentFilter();
        OqcsbfttReadData.addAction(ACTION_OQCSBFTT_READ_DATA);
        registerReceiver(this.mBroadcastReceiver, OqcsbfttReadData);
    }
}
