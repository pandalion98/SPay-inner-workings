package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.SensorTestMenu;
import com.sec.xmldata.support.Support.Spec;
import com.sec.xmldata.support.Support.TestCase;
import java.text.DecimalFormat;
import java.util.Timer;

public class LightSensorReadTest extends BaseActivity {
    private final int LEVEL_1 = 0;
    private final int LEVEL_1_MAX = -1;
    private final int LEVEL_2 = 1;
    private final int LEVEL_2_MAX = -1;
    private final int LEVEL_3 = 2;
    private final int LEVEL_3_MAX = -1;
    private final int LEVEL_4 = 3;
    private final int LEVEL_4_MAX = -1;
    private final int LEVEL_5 = 4;
    private final int LEVEL_6 = 5;
    private final int LEVEL_7 = 6;
    private final byte MSG_UPDATE_LEVEL = 1;
    /* access modifiers changed from: private */
    public TextView mAdcText;
    private TextView mAdcTitle;
    private Button mBackButton;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                LightSensorReadTest.this.mLuxText.setText(LightSensorReadTest.this.mLightSensorThread.getLuxString());
                if (LightSensorReadTest.this.mIsReadFromManager) {
                    LightSensorReadTest.this.mAdcText.setText(LightSensorReadTest.this.mLightSensorThread.getAdcString());
                }
                LightSensorReadTest.this.mLevelText.setText(LightSensorReadTest.this.mLightSensorThread.getLevelString());
                LightSensorReadTest.this.mLevelText.setTextColor(LightSensorReadTest.this.mLevelColor);
                if (!LightSensorReadTest.this.mIsRGBSensor) {
                    return;
                }
                if ("CM36686".equalsIgnoreCase(Kernel.read(Kernel.LIGHT_SENSOR_NAME)) || "STK3328".equalsIgnoreCase(Kernel.read(Kernel.LIGHT_SENSOR_NAME))) {
                    LightSensorReadTest.this.mRgbwText.setText(LightSensorReadTest.this.mLightSensorThread.getAlsWString());
                } else {
                    LightSensorReadTest.this.mRgbwText.setText(LightSensorReadTest.this.mLightSensorThread.getRgbwString());
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsRGBSensor = false;
    /* access modifiers changed from: private */
    public boolean mIsReadFromManager = false;
    /* access modifiers changed from: private */
    public int mLevelColor;
    /* access modifiers changed from: private */
    public final long[] mLevelDownTable = new long[7];
    /* access modifiers changed from: private */
    public TextView mLevelText;
    /* access modifiers changed from: private */
    public final long[] mLevelUpTable = new long[7];
    Sensor mLightSensor;
    LightSensorThread mLightSensorThread;
    Sensor mLightSensor_CCT;
    /* access modifiers changed from: private */
    public TextView mLuxText;
    /* access modifiers changed from: private */
    public String mReadTargetFileID = null;
    /* access modifiers changed from: private */
    public TextView mRgbwText;
    SensorManager mSensorManager;
    Timer mTimer;

    private class LightSensorThread extends Thread implements SensorEventListener {
        private static final int SLEEP_DURATION = 50;
        private float mAdc;
        private int mBlueValue;
        private float mCctValue;
        private int mGreenValue;
        private boolean mIsRunningTask;
        private int mLevel;
        private float[] mLighSensortValues;
        private float[] mLighSensortValues_CCT;
        private String mLightSensorValues_File;
        private float mLux;
        private float mPreviousLux;
        private int mRedValue;
        private final boolean mSupportRawLux;
        String mTestcase;
        private int mWhiteValue;

        private LightSensorThread() {
            boolean z = false;
            this.mIsRunningTask = false;
            this.mLighSensortValues = new float[3];
            this.mLighSensortValues_CCT = new float[3];
            this.mLevel = 0;
            this.mWhiteValue = 0;
            this.mGreenValue = 0;
            this.mRedValue = 0;
            this.mBlueValue = 0;
            this.mCctValue = 0.0f;
            this.mPreviousLux = 0.0f;
            this.mTestcase = SensorTestMenu.getTestCase(SensorTestMenu.NAME_LIGHT_LUX);
            if (this.mTestcase != null) {
                z = this.mTestcase.contains("RAW_LUX");
            }
            this.mSupportRawLux = z;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == 5) {
                this.mLighSensortValues = (float[]) event.values.clone();
            } else if (type == 65587) {
                this.mLighSensortValues_CCT = (float[]) event.values.clone();
            }
        }

        public synchronized void run() {
            while (true) {
                if (this.mIsRunningTask) {
                    readToLightSensor();
                    LightSensorReadTest.this.mHandler.sendEmptyMessage(1);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    LtUtil.log_d(LightSensorReadTest.this.CLASS_NAME, "run", "Thread interrupted");
                    return;
                }
            }
        }

        public void setEnable(boolean e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Set = ");
            sb.append(e);
            LtUtil.log_d(LightSensorReadTest.this.CLASS_NAME, "setEnable", sb.toString());
            this.mIsRunningTask = e;
        }

        private void readToLightSensor() {
            if (LightSensorReadTest.this.mReadTargetFileID != null) {
                this.mLightSensorValues_File = Kernel.read(LightSensorReadTest.this.mReadTargetFileID);
                if (this.mLightSensorValues_File != null) {
                    int i = 0;
                    if (TestCase.getEnabled(TestCase.IS_DISPLAY_LIGHT_SENSOR_ADC_ONLY)) {
                        this.mLux = Float.valueOf(this.mLightSensorValues_File).floatValue();
                    } else {
                        this.mLux = this.mLighSensortValues[0];
                    }
                    int i2 = 1;
                    if (LightSensorReadTest.this.mIsRGBSensor) {
                        this.mCctValue = this.mLighSensortValues_CCT[1];
                        getRGBWFromSysfs(this.mLightSensorValues_File);
                    } else if (LightSensorReadTest.this.mIsReadFromManager) {
                        if (this.mLightSensorValues_File == null || !this.mLightSensorValues_File.contains(",")) {
                            this.mAdc = Float.valueOf(this.mLightSensorValues_File).floatValue();
                        } else {
                            this.mAdc = Float.valueOf(this.mLightSensorValues_File.split(",")[0]).floatValue();
                        }
                    }
                    if (this.mLevel > 1) {
                        i = this.mLevel - 1;
                    }
                    int tempLevel = i;
                    if (this.mPreviousLux >= this.mLux) {
                        if (this.mLux < ((float) LightSensorReadTest.this.mLevelDownTable[tempLevel])) {
                            if (this.mLevel > 1) {
                                i2 = this.mLevel - 1;
                            }
                            this.mLevel = i2;
                        }
                    } else if (this.mLux > ((float) LightSensorReadTest.this.mLevelUpTable[tempLevel])) {
                        this.mLevel++;
                    }
                    switch (this.mLevel) {
                        case 1:
                            LightSensorReadTest.this.mLevelColor = -65536;
                            break;
                        case 2:
                            LightSensorReadTest.this.mLevelColor = -256;
                            break;
                        case 3:
                            LightSensorReadTest.this.mLevelColor = -16711936;
                            break;
                        case 4:
                            LightSensorReadTest.this.mLevelColor = -16711681;
                            break;
                        case 5:
                            LightSensorReadTest.this.mLevelColor = -16776961;
                            break;
                        default:
                            LightSensorReadTest.this.mLevelColor = -16776961;
                            break;
                    }
                    this.mPreviousLux = this.mLux;
                    return;
                }
                LtUtil.log_e(LightSensorReadTest.this.CLASS_NAME, "readToLightSensor", "File Not Found => Value : null");
            }
        }

        private void setBrightness(int level) {
            switch (level) {
                case 0:
                    BaseActivity.mModulePower.setBrightness(30);
                    return;
                case 1:
                    BaseActivity.mModulePower.setBrightness(60);
                    return;
                case 2:
                    BaseActivity.mModulePower.setBrightness(90);
                    return;
                case 3:
                    BaseActivity.mModulePower.setBrightness(120);
                    return;
                case 4:
                    BaseActivity.mModulePower.setBrightness(150);
                    return;
                case 5:
                    BaseActivity.mModulePower.setBrightness(180);
                    return;
                case 6:
                    BaseActivity.mModulePower.setBrightness(255);
                    return;
                default:
                    BaseActivity.mModulePower.setBrightness(255);
                    return;
            }
        }

        private int computeForLux(String input) {
            int luxValue;
            String[] rgbwString = input.split(",");
            this.mGreenValue = Integer.valueOf(rgbwString[1]).intValue();
            this.mWhiteValue = Integer.valueOf(rgbwString[3]).intValue();
            float I_cf = ((float) this.mGreenValue) / ((float) this.mWhiteValue);
            if (this.mGreenValue < 5) {
                return 0;
            }
            if (((double) I_cf) <= 0.5d) {
                luxValue = (int) (0.03d * Math.pow((double) this.mGreenValue, 1.34d));
            } else if (0.5d >= ((double) I_cf) || I_cf >= 1.0f) {
                luxValue = (int) (((((double) this.mGreenValue) * 0.18d) * 6.4d) / ((double) I_cf));
            } else {
                luxValue = (int) (((double) this.mGreenValue) * ((1.5d * ((double) I_cf)) - 0.45d));
            }
            return luxValue;
        }

        private boolean getRGBWFromSysfs(String input) {
            if (input == null) {
                return false;
            }
            String[] rgbwString = input.split(",");
            try {
                this.mRedValue = Integer.valueOf(rgbwString[0]).intValue();
                this.mGreenValue = Integer.valueOf(rgbwString[1]).intValue();
                this.mBlueValue = Integer.valueOf(rgbwString[2]).intValue();
                if (Spec.getBoolean(Spec.RGBSENSOR_SUPPORT_WHITE)) {
                    this.mWhiteValue = Integer.valueOf(rgbwString[3]).intValue();
                }
                return true;
            } catch (Exception e) {
                LtUtil.log_e(e);
                return false;
            }
        }

        private int computeForCctValue(String input) {
            float I_cf;
            double d;
            String str = input;
            if (str == null) {
                return -1;
            }
            String[] rgbwString = str.split(",");
            try {
                this.mRedValue = Integer.valueOf(rgbwString[0]).intValue();
                this.mGreenValue = Integer.valueOf(rgbwString[1]).intValue();
                this.mBlueValue = Integer.valueOf(rgbwString[2]).intValue();
                this.mWhiteValue = Integer.valueOf(rgbwString[3]).intValue();
                int cctValue = -1;
                if (this.mWhiteValue != 0) {
                    I_cf = ((float) this.mGreenValue) / ((float) this.mWhiteValue);
                } else {
                    I_cf = (float) this.mGreenValue;
                }
                if (this.mWhiteValue >= 62000) {
                    cctValue = 2700;
                } else if (((double) I_cf) <= 0.5d) {
                    double ccti = (this.mGreenValue != 0 ? ((double) (this.mRedValue - this.mBlueValue)) / ((double) this.mGreenValue) : (double) (this.mRedValue - this.mBlueValue)) + 0.84d;
                    cctValue = ccti <= 0.155d ? 7096 : ccti >= 1.4d ? 2467 : (int) (2900.0d * Math.pow(ccti, -0.48d));
                } else {
                    double ccti2 = (this.mGreenValue != 0 ? ((double) (this.mRedValue - this.mBlueValue)) / ((double) this.mGreenValue) : (double) (this.mRedValue - this.mBlueValue)) + 0.29d;
                    if (ccti2 <= 0.155d) {
                        cctValue = 7096;
                    } else if (ccti2 >= 1.4d) {
                        cctValue = 2467;
                    } else if (((double) I_cf) <= 0.5d || ((double) I_cf) >= 0.9d) {
                        if (((double) I_cf) >= 0.9d && ((double) I_cf) < 1.65d) {
                            cctValue = (int) (2900.0d * Math.pow(ccti2, -0.48d));
                        } else if (((double) I_cf) < 1.65d || ((double) I_cf) > 1.9d) {
                            cctValue = (int) (2900.0d * Math.pow(0.1232d, -0.48d));
                        } else {
                            double cctixlamp = 0.07d * ((double) I_cf);
                            if (this.mRedValue != this.mBlueValue) {
                                d = ((double) ((((float) this.mWhiteValue) / ((float) Math.abs(this.mRedValue - this.mBlueValue))) * I_cf)) * Math.abs(ccti2);
                            } else {
                                d = ((double) (((float) this.mWhiteValue) * I_cf)) * Math.abs(ccti2);
                            }
                            cctValue = (int) (((double) (2900.0f * ((float) d))) * Math.pow(cctixlamp, -0.48d));
                        }
                    } else if (ccti2 > 0.4d && ccti2 < 1.1d) {
                        cctValue = (int) (2700.0d * Math.pow(ccti2, -1.126d));
                    } else if (ccti2 > 1.1d && ccti2 < 1.4d) {
                        cctValue = (int) (2900.0d * Math.pow(ccti2, -0.48d));
                    }
                }
                return cctValue;
            } catch (Exception e) {
                LtUtil.log_e(e);
                return -1;
            }
        }

        /* access modifiers changed from: private */
        public String getLuxString() {
            DecimalFormat df = new DecimalFormat("0.00");
            StringBuilder sb = new StringBuilder();
            sb.append(df.format((double) this.mLux));
            sb.append(" lux(from manager)");
            return sb.toString();
        }

        private String getRawLuxString() {
            if (this.mLighSensortValues == null || this.mLighSensortValues.length <= 2) {
                return null;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            StringBuilder sb = new StringBuilder();
            sb.append("Raw lux : ");
            sb.append(df.format((double) this.mLighSensortValues[2]));
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getAdcString() {
            DecimalFormat df = new DecimalFormat("0.00");
            StringBuilder sb = new StringBuilder();
            sb.append(df.format((double) this.mAdc));
            sb.append(" Adc(from sysfs)");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getRgbwString() {
            StringBuilder sb = new StringBuilder();
            sb.append("R: ");
            sb.append(this.mRedValue);
            sb.append("\nG: ");
            sb.append(this.mGreenValue);
            sb.append("\nB: ");
            sb.append(this.mBlueValue);
            String result = sb.toString();
            if (Spec.getBoolean(Spec.RGBSENSOR_SUPPORT_WHITE)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(result);
                sb2.append("\nW: ");
                sb2.append(this.mWhiteValue);
                result = sb2.toString();
            }
            if (this.mSupportRawLux && getRawLuxString() != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(result);
                sb3.append("\n");
                sb3.append(getRawLuxString());
                result = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(result);
            sb4.append("\nCCT: ");
            sb4.append(this.mCctValue);
            return sb4.toString();
        }

        /* access modifiers changed from: private */
        public String getAlsWString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ALS: ");
            sb.append(this.mRedValue);
            sb.append("\nW: ");
            sb.append(this.mGreenValue);
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getLevelString() {
            StringBuilder sb = new StringBuilder();
            sb.append("LEVEL ");
            sb.append(this.mLevel);
            return sb.toString();
        }
    }

    public LightSensorReadTest() {
        super("LightSensorReadTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.light_sensor_read_test);
        this.mLuxText = (TextView) findViewById(C0268R.C0269id.tv_lux);
        this.mAdcTitle = (TextView) findViewById(C0268R.C0269id.tv_adc_title);
        this.mAdcText = (TextView) findViewById(C0268R.C0269id.tv_adc);
        this.mLevelText = (TextView) findViewById(C0268R.C0269id.tv_level);
        this.mRgbwText = (TextView) findViewById(C0268R.C0269id.values_rgbw);
        this.mBackButton = (Button) findViewById(C0268R.C0269id.btn_back);
        this.mBackButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LightSensorReadTest.this.finish();
            }
        });
        this.mTimer = new Timer();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mLightSensor = this.mSensorManager.getDefaultSensor(5);
        this.mLightSensor_CCT = this.mSensorManager.getDefaultSensor(65587);
        this.mLightSensorThread = new LightSensorThread();
        this.mLightSensorThread.start();
        checkFile();
        this.mIsReadFromManager = isReadFromManager();
        this.mIsRGBSensor = isRgbSensorSupported();
        StringBuilder sb = new StringBuilder();
        sb.append("mIsReadFromManager=");
        sb.append(this.mIsReadFromManager);
        sb.append(", mIsRGBSensor=");
        sb.append(this.mIsRGBSensor);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        if (!this.mIsReadFromManager) {
            this.mAdcTitle.setVisibility(8);
            this.mAdcText.setVisibility(8);
        }
        if (TestCase.getEnabled(TestCase.IS_DISPLAY_LIGHT_SENSOR_ADC_ONLY) && !this.mIsRGBSensor) {
            this.mRgbwText.setVisibility(8);
        }
        setLevelRange();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        int mDelay;
        super.onResume();
        if (TestCase.getEnabled(TestCase.IS_DISPLAY_LIGHT_SENSOR_SLOW)) {
            mDelay = 3;
        } else {
            mDelay = 2;
        }
        this.mSensorManager.registerListener(this.mLightSensorThread, this.mLightSensor, mDelay);
        this.mSensorManager.registerListener(this.mLightSensorThread, this.mLightSensor_CCT, mDelay);
        this.mLightSensorThread.setEnable(true);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mLightSensorThread.setEnable(false);
        this.mSensorManager.unregisterListener(this.mLightSensorThread);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mLightSensorThread.interrupt();
    }

    public void setBrightness() {
        if (VERSION.SDK_INT >= 28) {
            LtUtil.log_d(this.CLASS_NAME, "setBrightness", "Temporary Brightness Off");
            mModulePower.setBrightness(-1);
            return;
        }
        LtUtil.log_d(this.CLASS_NAME, "setBrightness", "Do nothing");
    }

    public void setBrightnessMode() {
        LtUtil.log_d(this.CLASS_NAME, "setBrightnessMode", "Set AUTO_BRIGHTNESS_MODE_ON");
        mModulePower.setScreenBrightnessMode(1);
    }

    private void checkFile() {
        LtUtil.log_d(this.CLASS_NAME, "checkFile", null);
        if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_RAW)) {
            LtUtil.log_d(this.CLASS_NAME, "checkFile", "read target : LIGHT_SENSOR_RAW");
            this.mReadTargetFileID = Kernel.LIGHT_SENSOR_RAW;
        } else if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_LUX)) {
            LtUtil.log_d(this.CLASS_NAME, "checkFile", "read target : LIGHT_SENSOR_LUX");
            this.mReadTargetFileID = Kernel.LIGHT_SENSOR_LUX;
        } else if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_ADC)) {
            LtUtil.log_d(this.CLASS_NAME, "checkFile", "read target : LIGHT_SENSOR_ADC");
            this.mReadTargetFileID = Kernel.LIGHT_SENSOR_ADC;
        } else {
            LtUtil.log_e(this.CLASS_NAME, "checkFile", "no read target");
            this.mReadTargetFileID = null;
        }
    }

    private boolean isReadFromManager() {
        StringBuilder sb = new StringBuilder();
        sb.append("IS_DISPLAY_LUX_ADC : ");
        sb.append(TestCase.getEnabled(TestCase.IS_DISPLAY_LUX_ADC));
        LtUtil.log_i(this.CLASS_NAME, "isReadFromManager", sb.toString());
        if (TestCase.getEnabled(TestCase.IS_DISPLAY_LUX_ADC)) {
            return true;
        }
        return false;
    }

    private boolean isRgbSensorSupported() {
        boolean isrgbsensor = true;
        String hwrevision = Properties.get(Properties.HW_REVISION);
        if (hwrevision != null) {
            String[] hwrevString = Spec.getString(Spec.NO_RGBSENSOR_SUPPORT_REV).split(",");
            if ("always".equalsIgnoreCase(hwrevString[0])) {
                return true;
            }
            int count = 0;
            while (true) {
                if (count >= hwrevString.length) {
                    break;
                } else if ("-1".equals(hwrevString[0]) || hwrevision.equals(hwrevString[count])) {
                    isrgbsensor = false;
                } else {
                    count++;
                }
            }
            isrgbsensor = false;
        }
        return isrgbsensor;
    }

    private void setLevelRange() {
        int i = 0;
        this.mLevelUpTable[0] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_1);
        this.mLevelUpTable[1] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_2);
        this.mLevelUpTable[2] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_3);
        this.mLevelUpTable[3] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_4);
        this.mLevelUpTable[4] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_5);
        this.mLevelUpTable[5] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_6);
        this.mLevelUpTable[6] = Spec.getLong(Spec.LIGHT_SENSOR_UP_LEVEL_7);
        this.mLevelDownTable[0] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_1);
        this.mLevelDownTable[1] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_2);
        this.mLevelDownTable[2] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_3);
        this.mLevelDownTable[3] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_4);
        this.mLevelDownTable[4] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_5);
        this.mLevelDownTable[5] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_6);
        this.mLevelDownTable[6] = Spec.getLong(Spec.LIGHT_SENSOR_DOWN_LEVEL_7);
        while (true) {
            int i2 = i;
            if (i2 < 6) {
                StringBuilder sb = new StringBuilder();
                sb.append("UP[");
                sb.append(i2);
                sb.append("] = ");
                sb.append(this.mLevelUpTable[i2]);
                LtUtil.log_d(this.CLASS_NAME, "setLevelRange", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("DOWN[");
                sb2.append(i2);
                sb2.append("] = ");
                sb2.append(this.mLevelDownTable[i2]);
                LtUtil.log_d(this.CLASS_NAME, "setLevelRange", sb2.toString());
                i = i2 + 1;
            } else {
                return;
            }
        }
    }
}
