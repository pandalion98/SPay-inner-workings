package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.goodix.cap.fingerprint.BuildConfig;
import com.goodix.cap.fingerprint.sec.p001mt.GFTestManager;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice.HallIC;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.SensorArrow;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Spec;
import com.sec.xmldata.support.Support.TestCase;
import com.synaptics.bpd.fingerprint.Fingerprint;
import egis.client.api.EgisFingerprint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Timer;
import java.util.TimerTask;

public class SensorTest extends BaseActivity implements OnClickListener {
    private static final String ACTION_OQCSBFTT_READ_DATA = "com.sec.factory.OQCSBFTT.READ_DATA";
    private static final int CP_ACCEL_POWER_OFF = 0;
    private static final int CP_ACCEL_POWER_ON = 1;
    private static final int TYPE_AIRMOTION = 2;
    private final String ACTION_WAKE_UP = "com.sec.factory.WakeUp";
    private final String CP_ACCELERMETER_OFF = "com.android.samsungtest.CpAccelermeterOff";
    private final String CP_ACCELERMETER_ON = "com.android.samsungtest.CpAccelermeterOn";
    /* access modifiers changed from: private */
    public final long[] FEED_BACK_PATTERN = {0, 5000};
    private String Finger_testcase;
    private final byte MSG_SET_BAROMETER_SENSOR_PRESSURE = HwModuleTest.ID_LED;
    private final byte MSG_UPDATE_OFFSET_VALUE = HwModuleTest.ID_WACOM;
    private final byte MSG_UPDATE_SENSOR_VALUE = 10;
    private final byte MSG_VIBRATE_FEED_BACK_END = HwModuleTest.ID_SUB_KEY;
    private final byte MSG_VIBRATE_FEED_BACK_START = HwModuleTest.ID_SLEEP;
    private final int TIMER_TASK_PERIOD = 10;
    private final byte WORK_HANDLER_THREAD = 1;
    private final byte WORK_SENSOR_THREAD = 0;
    /* access modifiers changed from: private */
    public boolean bReadUltrasonicData = false;
    /* access modifiers changed from: private */
    public int countValue = 0;
    /* access modifiers changed from: private */
    public boolean isCloseMode = false;
    private Button mAcceGraphButton;
    private Button mAcceImageTestButton;
    private TextView mAcceSensorTitleText;
    /* access modifiers changed from: private */
    public TextView mAcceSensorValueText;
    private View mAcceSeparator;
    private Sensor mAccelerometerSensor;
    /* access modifiers changed from: private */
    public String mAccelerometerSensorString = null;
    private AutoOffsetTask mAutoOffsetTask;
    private Sensor mBIOSensor;
    /* access modifiers changed from: private */
    public LinearLayout mBackground;
    /* access modifiers changed from: private */
    public float mBaromPressureAvg = 0.0f;
    /* access modifiers changed from: private */
    public float mBaromPressureTotal;
    /* access modifiers changed from: private */
    public float[] mBaromPressureTotalArray = new float[5];
    /* access modifiers changed from: private */
    public TextView mBaromSensorAltitudeText;
    /* access modifiers changed from: private */
    public TextView mBaromSensorPressureText;
    private Button mBaromSensorSelftestButton;
    /* access modifiers changed from: private */
    public Button mBaromSensorSettingAltitudeButton;
    private EditText mBaromSensorSettingAltitudeEdit;
    /* access modifiers changed from: private */
    public TextView mBaromSensorSettingAltitudeText;
    private TextView mBaromSensorTitleText;
    private View mBaromSeparator;
    private Sensor mBarometerSensor;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("Action :");
            sb.append(action);
            LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb.toString());
            if (action.equals("com.sec.android.app.factorytest")) {
                String cmdData = intent.getStringExtra("COMMAND");
                if (cmdData != null) {
                    SensorTest.this.catchCPsAccelerometerData(cmdData);
                }
            } else if (action.equals(SensorTest.ACTION_OQCSBFTT_READ_DATA)) {
                FileOutputStream out = null;
                String result = "";
                int item = intent.getIntExtra("item", 0);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ITEM :");
                sb2.append(item);
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb2.toString());
                try {
                    String secefsOqcsbfttDirectory = Kernel.getFilePath(Kernel.SECEFS_OQCSBFTT_DIRECTORY);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(secefsOqcsbfttDirectory);
                    sb3.append("/");
                    sb3.append(item);
                    out = new FileOutputStream(sb3.toString());
                } catch (IOException e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("IOException : ");
                    sb4.append(e.getMessage());
                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb4.toString());
                }
                switch (item) {
                    case 407:
                        if (SensorTest.this.mSensorTask.getProximityStatus() != 0) {
                            result = "NOT FOUND";
                            break;
                        } else {
                            result = "FOUND";
                            break;
                        }
                    case 408:
                        result = Integer.toString(SensorTest.this.mSensorTask.getLightSensorLuxValue());
                        break;
                    case 410:
                        int time = intent.getIntExtra("time", 0);
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("NV_O_ACSENSOR time : ");
                        sb5.append(time);
                        LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb5.toString());
                        int[] access$7200 = SensorTest.this.mRawMax;
                        int[] access$72002 = SensorTest.this.mRawMax;
                        SensorTest.this.mRawMax[2] = Integer.MIN_VALUE;
                        access$72002[1] = Integer.MIN_VALUE;
                        access$7200[0] = Integer.MIN_VALUE;
                        int[] access$7300 = SensorTest.this.mRawMin;
                        int[] access$73002 = SensorTest.this.mRawMin;
                        SensorTest.this.mRawMin[2] = Integer.MAX_VALUE;
                        access$73002[1] = Integer.MAX_VALUE;
                        access$7300[0] = Integer.MAX_VALUE;
                        SensorTest.this.mRawAvg[0] = (double) SensorTest.this.mSensorTask.mRawData[0];
                        SensorTest.this.mRawAvg[1] = (double) SensorTest.this.mSensorTask.mRawData[1];
                        SensorTest.this.mRawAvg[2] = (double) SensorTest.this.mSensorTask.mRawData[2];
                        SensorTest.this.mRawCnt = 1;
                        SensorTest.this.mDataGet = true;
                        if (SensorTest.this.mOqcTimer == null) {
                            SensorTest.this.mOqcTimer = new Timer("OqcDataReadTimer");
                        }
                        SensorTest.this.mOqcTimer.schedule(new TimerTask() {
                            public void run() {
                                FileOutputStream out;
                                SensorTest.this.mDataGet = false;
                                StringBuilder sb = new StringBuilder();
                                sb.append(SensorTest.this.mRawMin[0]);
                                sb.append(",");
                                sb.append(SensorTest.this.mRawMax[0]);
                                sb.append(",");
                                sb.append((int) SensorTest.this.mRawAvg[0]);
                                sb.append(",");
                                sb.append(SensorTest.this.mRawMin[1]);
                                sb.append(",");
                                sb.append(SensorTest.this.mRawMax[1]);
                                sb.append(",");
                                sb.append((int) SensorTest.this.mRawAvg[1]);
                                sb.append(",");
                                sb.append(SensorTest.this.mRawMin[2]);
                                sb.append(",");
                                sb.append(SensorTest.this.mRawMax[2]);
                                sb.append(",");
                                sb.append((int) SensorTest.this.mRawAvg[2]);
                                String result = sb.toString();
                                String secefsOqcsbfttDirectory = Kernel.getFilePath(Kernel.SECEFS_OQCSBFTT_DIRECTORY);
                                try {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(secefsOqcsbfttDirectory);
                                    sb2.append("/");
                                    sb2.append(410);
                                    out = new FileOutputStream(sb2.toString());
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Data : ");
                                    sb3.append(result);
                                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "OQC Timer task expired", sb3.toString());
                                    out.write(result.getBytes());
                                    out.close();
                                    out.close();
                                    return;
                                } catch (IOException e) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("IOException : ");
                                    sb4.append(e.getMessage());
                                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "mOqcTask", sb4.toString());
                                    return;
                                } catch (Throwable th) {
                                    r3.addSuppressed(th);
                                }
                                throw th;
                            }
                        }, (long) time);
                        break;
                    default:
                        LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", "Invalid NV");
                        return;
                }
                try {
                    if (!result.equals("")) {
                        out.write(result.getBytes());
                    }
                    out.close();
                } catch (IOException e2) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("IOException : ");
                    sb6.append(e2.getMessage());
                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb6.toString());
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mCloseOutBoundCount = 0;
    private CommonFingerprint_qbt2000 mCommonFingerprint_qbt2000;
    private TextView mCpAcceSensorTitleText;
    private TextView mCpAcceSensorValueText;
    private View mCpAcceSeparator;
    /* access modifiers changed from: private */
    public boolean mDataGet = false;
    private View mFPSeparator;
    private Button mFPTestButton_IntCheck;
    private Button mFPTestButton_NormalScan;
    private Button mFPTestButton_SNR;
    private Button mFPTestButton_SensorInfo;
    private TextView mFPTitleText;
    private TextView mFPVersion;
    /* access modifiers changed from: private */
    public FactoryTestPhone mFactoryPhone;
    private String mFingerPrint_Name;
    private String mFingerPrint_Vendor;
    private FingerprintManager mFingerprint_combination;
    private Fingerprint mFingerprint_cpid;
    private egistec.csa.client.api.Fingerprint mFingerprint_egis;
    private egistec.optical.csa.client.api.Fingerprint mFingerprint_egisOptical;
    private com.synaptics.fingerprint.namsan.Fingerprint mFingerprint_namsan;
    private com.synaptics.fingerprint.Fingerprint mFingerprint_touch;
    private Sensor mFlickerSensor;
    /* access modifiers changed from: private */
    public TextView mFlickerSensorText;
    /* access modifiers changed from: private */
    public DecimalFormat mFormat;
    private GFTestManager mGFTestManager;
    private Button mGyroSensorDisplayButton;
    private Button mGyroSensorGraphButton;
    private Button mGyroSensorSelftestButton;
    private TextView mGyroSensorTitleText;
    /* access modifiers changed from: private */
    public TextView mGyroSensorValueText;
    private View mGyroSeparator;
    private Sensor mGyroscopeSensor;
    private float mHRMHRAvg = 0.0f;
    private int mHRMHRCount = 1;
    private float mHRMHRMax = 0.0f;
    private float mHRMHRMin = 100.0f;
    private float mHRMHRTotal = 0.0f;
    private Sensor mHRMSensor;
    private TextView mHRMSensorDeivceIdText;
    private TextView mHRMSensorDriverText;
    private Button mHRMSensorEOLTestButton;
    private TextView mHRMSensorLibraryELFText;
    private TextView mHRMSensorLibraryEOLText;
    private TextView mHRMSensorLibraryText;
    private Button mHRMSensorStartButton;
    private TextView mHRMSensorTitleText;
    private View mHRMSeparator;
    private float mHRMSignalQtyAvg = 0.0f;
    private int mHRMSignalQtyCount = 1;
    private float mHRMSignalQtyMax = 0.0f;
    private float mHRMSignalQtyMin = 100.0f;
    private float mHRMSignalQtyTotal = 0.0f;
    private float mHRMSpO2Avg = 0.0f;
    private int mHRMSpO2Count = 1;
    private float mHRMSpO2Max = 0.0f;
    private float mHRMSpO2Min = 100.0f;
    private float mHRMSpO2Total = 0.0f;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (SensorTest.this.work_thread == 1) {
                        SensorTest.this.mAccelerometerSensorString = SensorTest.this.mSensorTask.getAccelerometerSensorString();
                        if (SensorTest.this.mAccelerometerSensorString != null) {
                            SensorTest.this.mAcceSensorValueText.setText(SensorTest.this.mAccelerometerSensorString);
                        }
                        SensorTest.this.mSubAccelerometerSensorString = SensorTest.this.mSensorTask.getSubAccelerometerSensorString();
                        if (SensorTest.this.use_Sub_Accelerometer && SensorTest.this.mSubAccelerometerSensorString != null) {
                            SensorTest.this.mSubAcceSensorValueText.setText(SensorTest.this.mSubAccelerometerSensorString);
                        }
                        SensorTest.this.mProxSensorStatusText.setText(SensorTest.this.mSensorTask.getProximitySensorStatusString());
                        SensorTest.this.mProxSensorAdcAvgText.setText(SensorTest.this.mSensorTask.getProximitySensorAdcAvgString());
                        SensorTest.this.mIrisProxSensorStatusText.setText(SensorTest.this.mSensorTask.getIrisProximitySensorStatusString());
                        if (SensorTest.this.use_Barometer) {
                            try {
                                SensorTest.this.mBaromPressureTotalArray[SensorTest.this.countValue] = SensorTest.this.mSensorTask.getBarometerSensorPressureFloat();
                                SensorTest.access$1916(SensorTest.this, SensorTest.this.mBaromPressureTotalArray[SensorTest.this.countValue]);
                                SensorTest.this.countValue = SensorTest.this.countValue + 1;
                                if (SensorTest.this.countValue == 5) {
                                    SensorTest.this.mBaromPressureAvg = SensorTest.this.mBaromPressureTotal / ((float) SensorTest.this.countValue);
                                    TextView access$2200 = SensorTest.this.mBaromSensorPressureText;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("BAROMETER: ");
                                    sb.append(SensorTest.this.mFormat.format((double) SensorTest.this.mBaromPressureAvg));
                                    sb.append(" hPa");
                                    access$2200.setText(sb.toString());
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mBaromPressureAvg : ");
                                    sb2.append(SensorTest.this.mBaromPressureAvg);
                                    LtUtil.log_d(SensorTest.this.CLASS_NAME, " Handler()", sb2.toString());
                                    SensorTest.this.countValue = 0;
                                    SensorTest.this.mBaromPressureTotal = 0.0f;
                                }
                            } catch (Exception e) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Exception");
                                sb3.append(e);
                                LtUtil.log_d(SensorTest.this.CLASS_NAME, "handleMessage", sb3.toString());
                            }
                            SensorTest.this.mBaromSensorAltitudeText.setText(SensorTest.this.mSensorTask.getBarometerSensorAltitudeString());
                        }
                        if (SensorTest.this.use_Flicker) {
                            SensorTest.this.mFlickerSensorText.setText(SensorTest.this.mSensorTask.getFlickerSensorDataString());
                        }
                        if (SensorTest.this.use_Light_LUX) {
                            SensorTest.this.mLightSensorLuxText.setText(SensorTest.this.mSensorTask.getLightSensorLuxString());
                        }
                        if (SensorTest.this.use_Light_ADC) {
                            SensorTest.this.mLightSensorAdcText.setText(SensorTest.this.mSensorTask.getLightSensorAdcString());
                        }
                        SensorTest.this.mGyroSensorValueText.setText(SensorTest.this.mSensorTask.getGyroscopeSensorValueString());
                        if (SensorTest.this.use_Sub_Gyroscope) {
                            SensorTest.this.mSubGyroSensorValueText.setText(SensorTest.this.mSensorTask.getSubGyroscopeSensorValueString());
                        }
                        SensorTest.this.mMagnSensorValueText.setText(SensorTest.this.mSensorTask.getMagneticSensorValueString());
                        if (SensorTest.this.use_Ultrasonic) {
                            SensorTest.this.mUltraSensorAdcDistanceValueText.setText(SensorTest.this.mSensorTask.getUltrasonicSensorAdcDistanceString());
                        }
                        SensorTest.this.mOrieSensorAzimuthText.setText(SensorTest.this.mSensorTask.getOrientationSensorAzimuthString());
                        SensorTest.this.mOrieSensorPitchText.setText(SensorTest.this.mSensorTask.getOrientationSensorPitchString());
                        SensorTest.this.mOrieSensorRollText.setText(SensorTest.this.mSensorTask.getOrientationSensorRollString());
                        LtUtil.DisableLogs();
                        if ("270".equals(Feature.getString(Feature.MAGNETIC_ROTATE_DEGREE)) || SensorTest.this.mProduct.equals("GT-N8013")) {
                            LtUtil.EnableLogs();
                            LtUtil.log_d(SensorTest.this.CLASS_NAME, " Handler()", "Feature 270");
                            SensorTest.this.mSensorArrow.setDirection((SensorTest.this.mSensorTask.mOrieSensorAzimuth + 270.0f) % 360.0f);
                        } else {
                            SensorTest.this.mSensorArrow.setDirection(SensorTest.this.mSensorTask.mOrieSensorAzimuth);
                        }
                        LtUtil.EnableLogs();
                        SensorTest.this.work_thread = 0;
                        return;
                    }
                    return;
                case 11:
                    if (SensorTest.this.use_Proximity || SensorTest.this.use_TspProximity) {
                        if (TestCase.getEnabled(TestCase.IS_PROXIMITY_TEST_MOTOR_FEEDBACK)) {
                            if ("false".equals(Feature.getString(Feature.SUPPORT_DUAL_LCD_FOLDER))) {
                                SensorTest.this.mVibrator.vibrate(SensorTest.this.FEED_BACK_PATTERN, 0);
                            } else if (HallIC.getHallICState() == HallIC.FOLDER_STATE_OPEN) {
                                SensorTest.this.mVibrator.vibrate(SensorTest.this.FEED_BACK_PATTERN, 0);
                            }
                        }
                        SensorTest.this.mBackground.setBackgroundColor(SensorTest.this.getResources().getColor(C0268R.color.green));
                        return;
                    }
                    return;
                case 12:
                    if (TestCase.getEnabled(TestCase.IS_PROXIMITY_TEST_MOTOR_FEEDBACK)) {
                        SensorTest.this.mVibrator.cancel();
                    }
                    SensorTest.this.mBackground.setBackgroundColor(SensorTest.this.getResources().getColor(C0268R.color.white));
                    return;
                case 13:
                    SensorTest.this.mBaromSensorSettingAltitudeText.setText(msg.obj.toString());
                    return;
                case 14:
                    if (SensorTest.this.mIsProxAutoCal) {
                        TextView access$6400 = SensorTest.this.mProxSensorOffsetText;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Offset: ");
                        sb4.append(SensorTest.this.offsetCheck);
                        access$6400.setText(sb4.toString());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private String mHrm_Name;
    private String mHrm_Vendor;
    private Sensor mHumidSensor;
    /* access modifiers changed from: private */
    public TextView mIrisProxSensorStatusText;
    private TextView mIrisProxSensorTitleText;
    /* access modifiers changed from: private */
    public float mIrisProxSensorValue = -1.0f;
    private View mIrisProxSeparator;
    private SensorEventListener mIrisProximityListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 65582) {
                SensorTest.this.mIrisProxSensorValue = event.values[0];
            }
        }
    };
    private Sensor mIrisProximitySensor;
    private boolean mIsFactoryBin = false;
    /* access modifiers changed from: private */
    public boolean mIsProxAutoCal = false;
    /* access modifiers changed from: private */
    public boolean mIsRGBSensor = false;
    private boolean mIsReadFromManager = false;
    /* access modifiers changed from: private */
    public boolean mIsVibrate = false;
    private Sensor mLightSensor;
    /* access modifiers changed from: private */
    public TextView mLightSensorAdcText;
    /* access modifiers changed from: private */
    public TextView mLightSensorLuxText;
    /* access modifiers changed from: private */
    public String mLightSensorSysfsPath = null;
    private Button mLightSensorTestButton;
    private TextView mLightSensorTitleText;
    private View mLightSeparator;
    private Button mMagnSensorPowerNoiseTestButton;
    private Button mMagnSensorSelfTestButton;
    private TextView mMagnSensorTitleText;
    /* access modifiers changed from: private */
    public TextView mMagnSensorValueText;
    private View mMagnSeparator;
    private Sensor mMagneticSensor;
    private Button mOISGyroSelftestButton;
    private TextView mOISGyroSensorSub;
    /* access modifiers changed from: private */
    public Timer mOqcTimer;
    /* access modifiers changed from: private */
    public TextView mOrieSensorAzimuthText;
    /* access modifiers changed from: private */
    public TextView mOrieSensorPitchText;
    /* access modifiers changed from: private */
    public TextView mOrieSensorRollText;
    private Sensor mOrientationSensor;
    /* access modifiers changed from: private */
    public String mProduct = SystemProperties.get("ro.product.model", Properties.PROPERTIES_DEFAULT_STRING);
    /* access modifiers changed from: private */
    public int mProxAdc;
    private TextView mProxSensor1stThresholdText;
    private TextView mProxSensor2ndThresholdText;
    /* access modifiers changed from: private */
    public TextView mProxSensorAdcAvgText;
    private TextView mProxSensorDefaultTrimText;
    private TextView mProxSensorDeviceID;
    private TextView mProxSensorHighThresholdText;
    private TextView mProxSensorLowThresholdText;
    /* access modifiers changed from: private */
    public TextView mProxSensorOffsetText;
    /* access modifiers changed from: private */
    public TextView mProxSensorStatusText;
    private TextView mProxSensorTitleText;
    private TextView mProxSensorTspColorIdText;
    /* access modifiers changed from: private */
    public float mProxSensorValue = -1.0f;
    private View mProxSeparator;
    private SensorEventListener mProximityListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == 8 || type == 65592) {
                SensorTest.this.mProxSensorValue = event.values[0];
            }
        }
    };
    private Sensor mProximitySensor;
    /* access modifiers changed from: private */
    public double[] mRawAvg = new double[3];
    /* access modifiers changed from: private */
    public int mRawCnt = 0;
    /* access modifiers changed from: private */
    public int[] mRawMax = new int[3];
    /* access modifiers changed from: private */
    public int[] mRawMin = new int[3];
    /* access modifiers changed from: private */
    public SensorArrow mSensorArrow;
    /* access modifiers changed from: private */
    public SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public SensorTask mSensorTask;
    private Timer mSensorTimer;
    private Button mSubAcceGraphButton;
    private TextView mSubAcceSensorTitleText;
    /* access modifiers changed from: private */
    public TextView mSubAcceSensorValueText;
    private View mSubAcceSeparator;
    private Sensor mSubAccelerometerSensor;
    /* access modifiers changed from: private */
    public String mSubAccelerometerSensorString = null;
    private Button mSubGyroSensorDisplayButton;
    private Button mSubGyroSensorGraphButton;
    private Button mSubGyroSensorSelftestButton;
    private TextView mSubGyroSensorTitleText;
    /* access modifiers changed from: private */
    public TextView mSubGyroSensorValueText;
    private View mSubGyroSeparator;
    private Sensor mSubGyroscopeSensor;
    /* access modifiers changed from: private */
    public double[] mSubRawAvg = new double[3];
    /* access modifiers changed from: private */
    public int mSubRawCnt = 0;
    /* access modifiers changed from: private */
    public int[] mSubRawMax = new int[3];
    /* access modifiers changed from: private */
    public int[] mSubRawMin = new int[3];
    private Sensor mTempSensor;
    private final BroadcastReceiver mTimeSetReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("Action :");
            sb.append(action);
            LtUtil.log_d(SensorTest.this.CLASS_NAME, "mTimeSetReceiver onReceive", sb.toString());
            if (intent.getAction().equals("android.intent.action.TIME_SET")) {
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "mTimeSetReceiver", "Received android.intent.action.TIME_SET");
                SensorTest.this.recreate();
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "mTimeSetReceiver", "Timer restarted");
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView mUltraSensorAdcDistanceValueText;
    private TextView mUltraSensorCompanyFWValueText;
    private TextView mUltraSensorTitleText;
    private View mUltraSeparator;
    private Sensor mUltrasonicSensor;
    /* access modifiers changed from: private */
    public Vibrator mVibrator;
    /* access modifiers changed from: private */
    public String offsetCheck = null;
    private Timer offsetTimer;
    private boolean use_Accelerometer = false;
    /* access modifiers changed from: private */
    public boolean use_Barometer = false;
    private boolean use_CpAccelerometer = false;
    private boolean use_Fingerprint = false;
    /* access modifiers changed from: private */
    public boolean use_Flicker = false;
    private boolean use_Gyroscope = false;
    private boolean use_HRM = false;
    private boolean use_IrisProximity = false;
    /* access modifiers changed from: private */
    public boolean use_Light_ADC = false;
    /* access modifiers changed from: private */
    public boolean use_Light_LUX = false;
    private boolean use_Magnetic = false;
    private boolean use_Magnetic_PowerNoise = false;
    private boolean use_OIS_Gyro = false;
    /* access modifiers changed from: private */
    public boolean use_Proximity = false;
    /* access modifiers changed from: private */
    public boolean use_Proximity_ADC = false;
    /* access modifiers changed from: private */
    public boolean use_Sub_Accelerometer = false;
    /* access modifiers changed from: private */
    public boolean use_Sub_Gyroscope = false;
    /* access modifiers changed from: private */
    public boolean use_TspProximity = false;
    /* access modifiers changed from: private */
    public boolean use_Ultrasonic = false;
    /* access modifiers changed from: private */
    public byte work_thread = 0;

    private class AutoOffsetTask extends TimerTask {
        private AutoOffsetTask() {
        }

        public void run() {
            if (SensorTest.this.use_TspProximity) {
                SensorTest.this.mProxAdc = Integer.parseInt(Kernel.read(Kernel.PATH_TSP_PROX_STATE, 0).trim());
            }
            if (SensorTest.this.use_Proximity_ADC) {
                try {
                    SensorTest.this.mProxAdc = Integer.parseInt(Kernel.read(Kernel.PROXI_SENSOR_ADC, 0).trim());
                } catch (Exception e) {
                    SensorTest.this.mProxAdc = 0;
                }
            }
            if (SensorTest.this.mIsProxAutoCal) {
                String autoOffsetValue = Kernel.read(Kernel.PROXI_SENSOR_DEFAULT_TRIM);
                if (autoOffsetValue != null) {
                    SensorTest.this.offsetCheck = autoOffsetValue;
                }
            }
            SensorTest.this.mHandler.sendEmptyMessage(14);
        }
    }

    private class SensorTask extends TimerTask implements SensorEventListener {
        private static final int ULTRASONIC_ESCAPE_CLOSEMODE = 3;
        private final String TAG;
        public int ULTRASONIC_MINIMUM_DETECTABLE_DISTANCE;
        private float awbData;
        private float flickerFrequence;
        private String mAcceSensorString;
        private float[] mAcceSensorValues;
        private int[] mAngle;
        private float mBaromAdjust;
        private float mBaromAltitude;
        private float mBaromPressure;
        private float mBaromSensorValues;
        private int mBlueValue;
        private int mCctValue;
        private float[] mFlickerSensorValues;
        private int mGreenValue;
        private float[] mGyroSensorValues;
        private float mGyroSensor_P;
        private float mGyroSensor_R;
        private float mGyroSensor_Y;
        private int mIrisProxStatus;
        private boolean mIsRunningTask;
        private String mLightSensorAdcValue;
        private float mLightSensorLuxValue;
        private float mLightSensorManagerValue;
        private float[] mMagnSensorValues;
        private float mMagnSensor_X;
        private float mMagnSensor_Y;
        private float mMagnSensor_Z;
        /* access modifiers changed from: private */
        public float mOrieSensorAzimuth;
        private float mOrieSensorPitch;
        private float mOrieSensorRoll;
        private float[] mOrieSensorValues;
        private String mProxAvg;
        private int mProxStatus;
        /* access modifiers changed from: private */
        public int[] mRawData;
        private int mRedValue;
        private String mSubAcceSensorString;
        private float[] mSubAcceSensorValues;
        private int[] mSubAngle;
        private float[] mSubGyroSensorValues;
        private float mSubGyroSensor_P;
        private float mSubGyroSensor_R;
        private float mSubGyroSensor_Y;
        private int[] mSubRawData;
        private int mWhiteValue;
        private String[] ultraAdcDistanceData;

        private SensorTask() {
            this.TAG = "SensorTask";
            this.mIsRunningTask = false;
            this.mAcceSensorValues = new float[3];
            this.mSubAcceSensorValues = new float[3];
            this.mRawData = new int[3];
            this.mSubRawData = new int[3];
            this.mAngle = new int[3];
            this.mSubAngle = new int[3];
            this.mProxStatus = -1;
            this.mIrisProxStatus = -1;
            this.mFlickerSensorValues = new float[7];
            this.awbData = 0.0f;
            this.flickerFrequence = 0.0f;
            this.mBaromAdjust = 0.0f;
            this.mWhiteValue = 0;
            this.mGreenValue = 0;
            this.mRedValue = 0;
            this.mBlueValue = 0;
            this.mCctValue = 0;
            this.mGyroSensorValues = new float[3];
            this.mSubGyroSensorValues = new float[3];
            this.mMagnSensorValues = new float[3];
            this.mOrieSensorValues = new float[3];
            this.ULTRASONIC_MINIMUM_DETECTABLE_DISTANCE = 34;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            if (sensor.getType() == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("TYPE_MAGNETIC_FIELD accuracy : ");
                sb.append(accuracy);
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "onAccuracyChanged", sb.toString());
                SensorTest.this.mSensorArrow.setCurrentCal(accuracy);
            }
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == 65577) {
                this.mFlickerSensorValues = (float[]) event.values.clone();
            } else if (type == 65687) {
                this.mSubAcceSensorValues = (float[]) event.values.clone();
            } else if (type != 65689) {
                switch (type) {
                    case 1:
                        this.mAcceSensorValues = (float[]) event.values.clone();
                        return;
                    case 2:
                        this.mMagnSensorValues = (float[]) event.values.clone();
                        return;
                    case 3:
                        this.mOrieSensorValues = (float[]) event.values.clone();
                        return;
                    case 4:
                        this.mGyroSensorValues = (float[]) event.values.clone();
                        return;
                    case 5:
                        this.mLightSensorManagerValue = event.values[0];
                        return;
                    case 6:
                        this.mBaromSensorValues = event.values[0];
                        return;
                    default:
                        return;
                }
            } else {
                this.mSubGyroSensorValues = (float[]) event.values.clone();
            }
        }

        public void run() {
            if (!this.mIsRunningTask) {
                return;
            }
            if (SensorTest.this.work_thread == 0) {
                readToAccelerometerSensor();
                if (SensorTest.this.use_Sub_Accelerometer) {
                    readToSubAccelerometerSensor();
                }
                readToProximitySensor();
                readToBarometerSensor();
                readToLightSensor();
                readToGyroscopeSensor();
                if (SensorTest.this.use_Sub_Gyroscope) {
                    readToSubGyroscopeSensor();
                }
                readToMagneticSensor();
                readToOrientationSensor();
                if (SensorTest.this.use_Ultrasonic && SensorTest.this.bReadUltrasonicData) {
                    readToUltrasonicSensor();
                }
                if (SensorTest.this.use_Flicker) {
                    readToFlickerSensor();
                }
                SensorTest.this.work_thread = 1;
                return;
            }
            updateUI();
        }

        /* access modifiers changed from: private */
        public void resume() {
            this.mIsRunningTask = true;
            if (SensorTest.this.use_Ultrasonic && SensorTest.this.bReadUltrasonicData) {
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "resume", "Ultrasonic start 1 write");
                try {
                    Kernel.write(Kernel.ULTRASONIC_ADC_DISTANCE, EgisFingerprint.MAJOR_VERSION);
                } catch (Exception e) {
                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "Resume", "Exception accessing ultrasonic file");
                }
            }
        }

        /* access modifiers changed from: private */
        public void pause() {
            this.mIsRunningTask = false;
            if (SensorTest.this.mHandler.hasMessages(10)) {
                SensorTest.this.mHandler.removeMessages(10);
            }
            if (SensorTest.this.mHandler.hasMessages(12)) {
                SensorTest.this.mHandler.removeMessages(12);
            }
            if (SensorTest.this.mHandler.hasMessages(11)) {
                SensorTest.this.mHandler.removeMessages(11);
            }
            if (SensorTest.this.mHandler.hasMessages(13)) {
                SensorTest.this.mHandler.removeMessages(13);
            }
            if (SensorTest.this.mHandler.hasMessages(14)) {
                SensorTest.this.mHandler.removeMessages(14);
            }
            if (SensorTest.this.use_Ultrasonic && SensorTest.this.bReadUltrasonicData) {
                LtUtil.log_d(SensorTest.this.CLASS_NAME, "Pause", "Ultrasonic end 0 write");
                try {
                    Kernel.write(Kernel.ULTRASONIC_ADC_DISTANCE, "0");
                } catch (Exception e) {
                    LtUtil.log_d(SensorTest.this.CLASS_NAME, "onResume", "Exception accessing ultrasonic file");
                }
            }
        }

        private synchronized void updateUI() {
            if (this.mProxStatus == 0 && !SensorTest.this.mIsVibrate) {
                SensorTest.this.mIsVibrate = true;
                SensorTest.this.mHandler.sendEmptyMessage(11);
            } else if (this.mProxStatus != 0 && SensorTest.this.mIsVibrate) {
                SensorTest.this.mIsVibrate = false;
                SensorTest.this.mHandler.sendEmptyMessage(12);
            }
            SensorTest.this.mHandler.sendEmptyMessage(10);
        }

        private void readToAccelerometerSensor() {
            try {
                String[] rawDatas = Kernel.read(Kernel.ACCEL_SENSOR_RAW, 0).trim().split(",");
                this.mRawData[0] = Integer.parseInt(rawDatas[0].trim());
                this.mRawData[1] = Integer.parseInt(rawDatas[1].trim());
                this.mRawData[2] = Integer.parseInt(rawDatas[2].trim());
                float realg = (float) Math.sqrt((double) ((this.mRawData[0] * this.mRawData[0]) + (this.mRawData[1] * this.mRawData[1]) + (this.mRawData[2] * this.mRawData[2])));
                this.mAngle[0] = ((int) (((float) Math.asin((double) (((float) this.mRawData[0]) / realg))) * 57.29578f)) * -1;
                this.mAngle[1] = ((int) (((float) Math.asin((double) (((float) this.mRawData[1]) / realg))) * 57.29578f)) * -1;
                this.mAngle[2] = ((int) (((float) Math.acos((double) (((float) this.mRawData[2]) / realg))) * 57.29578f)) - 90;
                StringBuilder sb = new StringBuilder();
                sb.append("ACC Raw Data - x: ");
                sb.append(this.mRawData[0]);
                sb.append(", y: ");
                sb.append(this.mRawData[1]);
                sb.append(", z: ");
                sb.append(this.mRawData[2]);
                sb.append("\nx-angle: ");
                sb.append(this.mAngle[0] * -1);
                sb.append(", y-angle: ");
                sb.append(this.mAngle[1] * -1);
                sb.append(", z-angle: ");
                sb.append(this.mAngle[2] * -1);
                this.mAcceSensorString = sb.toString();
                if (SensorTest.this.mDataGet) {
                    SensorTest.this.mRawAvg[0] = (SensorTest.this.mRawAvg[0] * (((double) SensorTest.this.mRawCnt) / (((double) SensorTest.this.mRawCnt) + 1.0d))) + (((double) this.mRawData[0]) / (((double) SensorTest.this.mRawCnt) + 1.0d));
                    SensorTest.this.mRawAvg[1] = (SensorTest.this.mRawAvg[1] * (((double) SensorTest.this.mRawCnt) / (((double) SensorTest.this.mRawCnt) + 1.0d))) + (((double) this.mRawData[1]) / (((double) SensorTest.this.mRawCnt) + 1.0d));
                    SensorTest.this.mRawAvg[2] = (SensorTest.this.mRawAvg[2] * (((double) SensorTest.this.mRawCnt) / (((double) SensorTest.this.mRawCnt) + 1.0d))) + (((double) this.mRawData[2]) / (((double) SensorTest.this.mRawCnt) + 1.0d));
                    SensorTest.this.mRawCnt = SensorTest.this.mRawCnt + 1;
                    if (SensorTest.this.mRawMax[0] < this.mRawData[0]) {
                        SensorTest.this.mRawMax[0] = this.mRawData[0];
                    }
                    if (SensorTest.this.mRawMin[0] > this.mRawData[0]) {
                        SensorTest.this.mRawMin[0] = this.mRawData[0];
                    }
                    if (SensorTest.this.mRawMax[1] < this.mRawData[1]) {
                        SensorTest.this.mRawMax[1] = this.mRawData[1];
                    }
                    if (SensorTest.this.mRawMin[1] > this.mRawData[1]) {
                        SensorTest.this.mRawMin[1] = this.mRawData[1];
                    }
                    if (SensorTest.this.mRawMax[2] < this.mRawData[2]) {
                        SensorTest.this.mRawMax[2] = this.mRawData[2];
                    }
                    if (SensorTest.this.mRawMin[2] > this.mRawData[2]) {
                        SensorTest.this.mRawMin[2] = this.mRawData[2];
                    }
                }
            } catch (Exception e) {
                this.mRawData[0] = (int) ((((double) this.mAcceSensorValues[0]) * 1024.0d) / 9.81d);
                this.mRawData[1] = (int) ((((double) this.mAcceSensorValues[1]) * 1024.0d) / 9.81d);
                this.mRawData[2] = (int) ((((double) this.mAcceSensorValues[2]) * 1024.0d) / 9.81d);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ACCELEROMETER: 1, x: ");
                sb2.append(this.mRawData[0]);
                sb2.append(", y: ");
                sb2.append(this.mRawData[1]);
                sb2.append(", z: ");
                sb2.append(this.mRawData[2]);
                this.mAcceSensorString = sb2.toString();
            }
        }

        private void readToSubAccelerometerSensor() {
            try {
                String[] rawDatas = Kernel.read(Kernel.SUB_ACCEL_SENSOR_RAW, 0).trim().split(",");
                this.mSubRawData[0] = Integer.parseInt(rawDatas[0].trim());
                this.mSubRawData[1] = Integer.parseInt(rawDatas[1].trim());
                this.mSubRawData[2] = Integer.parseInt(rawDatas[2].trim());
                float realg = (float) Math.sqrt((double) ((this.mSubRawData[0] * this.mSubRawData[0]) + (this.mSubRawData[1] * this.mSubRawData[1]) + (this.mSubRawData[2] * this.mSubRawData[2])));
                this.mSubAngle[0] = ((int) (((float) Math.asin((double) (((float) this.mSubRawData[0]) / realg))) * 57.29578f)) * -1;
                this.mSubAngle[1] = ((int) (((float) Math.asin((double) (((float) this.mSubRawData[1]) / realg))) * 57.29578f)) * -1;
                this.mSubAngle[2] = ((int) (((float) Math.acos((double) (((float) this.mSubRawData[2]) / realg))) * 57.29578f)) - 90;
                StringBuilder sb = new StringBuilder();
                sb.append("Sub ACC Raw Data - x: ");
                sb.append(this.mSubRawData[0]);
                sb.append(", y: ");
                sb.append(this.mSubRawData[1]);
                sb.append(", z: ");
                sb.append(this.mSubRawData[2]);
                sb.append("\nx-angle: ");
                sb.append(this.mSubAngle[0] * -1);
                sb.append(", y-angle: ");
                sb.append(this.mSubAngle[1] * -1);
                sb.append(", z-angle: ");
                sb.append(this.mSubAngle[2] * -1);
                this.mSubAcceSensorString = sb.toString();
                if (SensorTest.this.mDataGet) {
                    SensorTest.this.mSubRawAvg[0] = (SensorTest.this.mSubRawAvg[0] * (((double) SensorTest.this.mSubRawCnt) / (((double) SensorTest.this.mSubRawCnt) + 1.0d))) + (((double) this.mSubRawData[0]) / (((double) SensorTest.this.mSubRawCnt) + 1.0d));
                    SensorTest.this.mSubRawAvg[1] = (SensorTest.this.mSubRawAvg[1] * (((double) SensorTest.this.mSubRawCnt) / (((double) SensorTest.this.mSubRawCnt) + 1.0d))) + (((double) this.mSubRawData[1]) / (((double) SensorTest.this.mSubRawCnt) + 1.0d));
                    SensorTest.this.mSubRawAvg[2] = (SensorTest.this.mSubRawAvg[2] * (((double) SensorTest.this.mSubRawCnt) / (((double) SensorTest.this.mSubRawCnt) + 1.0d))) + (((double) this.mSubRawData[2]) / (((double) SensorTest.this.mSubRawCnt) + 1.0d));
                    SensorTest.this.mSubRawCnt = SensorTest.this.mSubRawCnt + 1;
                    if (SensorTest.this.mSubRawMax[0] < this.mSubRawData[0]) {
                        SensorTest.this.mSubRawMax[0] = this.mSubRawData[0];
                    }
                    if (SensorTest.this.mSubRawMin[0] > this.mSubRawData[0]) {
                        SensorTest.this.mSubRawMin[0] = this.mSubRawData[0];
                    }
                    if (SensorTest.this.mSubRawMax[1] < this.mSubRawData[1]) {
                        SensorTest.this.mSubRawMax[1] = this.mSubRawData[1];
                    }
                    if (SensorTest.this.mSubRawMin[1] > this.mSubRawData[1]) {
                        SensorTest.this.mSubRawMin[1] = this.mSubRawData[1];
                    }
                    if (SensorTest.this.mSubRawMax[2] < this.mSubRawData[2]) {
                        SensorTest.this.mSubRawMax[2] = this.mSubRawData[2];
                    }
                    if (SensorTest.this.mSubRawMin[2] > this.mSubRawData[2]) {
                        SensorTest.this.mSubRawMin[2] = this.mSubRawData[2];
                    }
                }
            } catch (Exception e) {
                this.mSubRawData[0] = (int) ((((double) this.mSubAcceSensorValues[0]) * 1024.0d) / 9.81d);
                this.mSubRawData[1] = (int) ((((double) this.mSubAcceSensorValues[1]) * 1024.0d) / 9.81d);
                this.mSubRawData[2] = (int) ((((double) this.mSubAcceSensorValues[2]) * 1024.0d) / 9.81d);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("SUB ACCELEROMETER: 65687, x: ");
                sb2.append(this.mSubRawData[0]);
                sb2.append(", y: ");
                sb2.append(this.mSubRawData[1]);
                sb2.append(", z: ");
                sb2.append(this.mSubRawData[2]);
                this.mSubAcceSensorString = sb2.toString();
            }
        }

        /* access modifiers changed from: private */
        public String getAccelerometerSensorString() {
            return this.mAcceSensorString;
        }

        /* access modifiers changed from: private */
        public String getSubAccelerometerSensorString() {
            return this.mSubAcceSensorString;
        }

        /* access modifiers changed from: private */
        public int getProximityStatus() {
            return this.mProxStatus;
        }

        private void readToProximitySensor() {
            if (!SensorTest.this.use_TspProximity) {
                this.mProxStatus = (int) SensorTest.this.mProxSensorValue;
                this.mIrisProxStatus = (int) SensorTest.this.mIrisProxSensorValue;
            }
            if (SensorTest.this.use_Proximity_ADC) {
                try {
                    this.mProxAvg = Kernel.read(Kernel.PROXI_SENSOR_ADC_AVG, 0).trim();
                } catch (Exception e) {
                    this.mProxAvg = "0,0,0";
                }
            }
        }

        /* access modifiers changed from: private */
        public String getProximitySensorStatusString() {
            if (!SensorTest.this.use_TspProximity) {
                StringBuilder sb = new StringBuilder();
                sb.append("PROXIMITY: ");
                sb.append(this.mProxStatus == 0 ? BuildConfig.VERSION_NAME : "0.0");
                return sb.toString();
            } else if (SensorTest.this.mProxAdc > 45) {
                this.mProxStatus = 0;
                return "PROXIMITY: 1.0";
            } else {
                this.mProxStatus = 1;
                return "PROXIMITY: 0.0";
            }
        }

        /* access modifiers changed from: private */
        public String getIrisProximitySensorStatusString() {
            StringBuilder sb = new StringBuilder();
            sb.append("IRISPROXIMITY: ");
            sb.append(this.mIrisProxStatus == 1 ? BuildConfig.VERSION_NAME : "0.0");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getProximitySensorAdcAvgString() {
            if (SensorTest.this.use_TspProximity) {
                StringBuilder sb = new StringBuilder();
                sb.append("ADC: ");
                sb.append(SensorTest.this.mProxAdc);
                return sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ADC: ");
            sb2.append(SensorTest.this.mProxAdc);
            sb2.append("(");
            sb2.append(this.mProxAvg);
            sb2.append(")");
            return sb2.toString();
        }

        private void readToFlickerSensor() {
            if (this.mFlickerSensorValues[0] >= 0.0f) {
                this.awbData = this.mFlickerSensorValues[0];
            } else if (this.mFlickerSensorValues[0] == -1.0f) {
                this.flickerFrequence = this.mFlickerSensorValues[1];
            }
        }

        /* access modifiers changed from: private */
        public String getFlickerSensorDataString() {
            StringBuilder sb = new StringBuilder();
            sb.append("F :");
            sb.append(Float.toString(this.awbData));
            sb.append(",");
            sb.append(Float.toString(this.flickerFrequence));
            return sb.toString();
        }

        private void readToBarometerSensor() {
            this.mBaromPressure = this.mBaromSensorValues;
            if (this.mBaromAdjust <= 0.0f) {
                SensorTest.this.mSensorManager;
                this.mBaromAltitude = SensorManager.getAltitude(1013.25f, this.mBaromSensorValues);
                return;
            }
            SensorTest.this.mSensorManager;
            this.mBaromAltitude = SensorManager.getAltitude(this.mBaromAdjust, this.mBaromSensorValues);
        }

        /* access modifiers changed from: private */
        public void setBarometerSensorAdjustValue(String pressure) {
            this.mBaromAdjust = Float.parseFloat(pressure);
        }

        /* access modifiers changed from: private */
        public void setBarometerSensorPressure(Double pressure) {
            int iPressure = (int) (pressure.doubleValue() * 100.0d);
            StringBuilder sb = new StringBuilder();
            sb.append("setBarometerSensorPressure : ");
            sb.append(iPressure);
            LtUtil.log_d(SensorTest.this.CLASS_NAME, "setBarometerSensorPressure", sb.toString());
            Handler access$9900 = SensorTest.this.mHandler;
            Handler access$99002 = SensorTest.this.mHandler;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(SensorTest.this.getResources().getString(C0268R.string.baro_ref_altitude));
            sb2.append(Double.toString(pressure.doubleValue()));
            access$9900.sendMessage(access$99002.obtainMessage(13, sb2.toString()));
            Kernel.write(Kernel.BROME_SENSOR_SEAR_LEVEL_PRESSURE, Integer.toString(iPressure));
        }

        private String getBarometerSensorPressureString() {
            StringBuilder sb = new StringBuilder();
            sb.append("BAROMETER: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mBaromPressure));
            sb.append(" hPa");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getBarometerSensorAltitudeString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ALTITUDE: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mBaromAltitude));
            sb.append(" m");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public float getBarometerSensorPressureFloat() {
            return this.mBaromPressure;
        }

        private void readToLightSensor() {
            this.mLightSensorLuxValue = this.mLightSensorManagerValue;
            if (SensorTest.this.mLightSensorSysfsPath != null) {
                String lightValue = Kernel.read(SensorTest.this.mLightSensorSysfsPath, 0);
                if (lightValue != null) {
                    this.mLightSensorAdcValue = lightValue;
                }
            }
        }

        /* access modifiers changed from: private */
        public int getLightSensorLuxValue() {
            return (int) this.mLightSensorLuxValue;
        }

        /* access modifiers changed from: private */
        public String getLightSensorLuxString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Light Sensor: ");
            sb.append((int) this.mLightSensorLuxValue);
            sb.append(" lux");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getLightSensorAdcString() {
            if (!SensorTest.this.mIsRGBSensor) {
                LtUtil.DisableLogs();
                if (TestCase.getEnabled(TestCase.IS_DISPLAY_LIGHT_SENSOR_ADC_ONLY)) {
                    LtUtil.EnableLogs();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Light Sensor: ");
                    sb.append(this.mLightSensorAdcValue);
                    sb.append(" Lux");
                    return sb.toString();
                }
                LtUtil.EnableLogs();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Light Sensor: ");
                sb2.append(this.mLightSensorAdcValue);
                sb2.append(" Adc");
                return sb2.toString();
            } else if ("CM36686".equalsIgnoreCase(Kernel.read(Kernel.LIGHT_SENSOR_NAME)) || "STK3328".equalsIgnoreCase(Kernel.read(Kernel.LIGHT_SENSOR_NAME))) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Light Sensor: ");
                sb3.append(this.mLightSensorAdcValue);
                sb3.append(" (ALS,W)");
                return sb3.toString();
            } else if (Spec.getBoolean(Spec.RGBSENSOR_SUPPORT_WHITE, 0)) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Light Sensor: ");
                sb4.append(this.mLightSensorAdcValue);
                sb4.append(" (R,G,B,W)");
                return sb4.toString();
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Light Sensor: ");
                sb5.append(this.mLightSensorAdcValue);
                sb5.append(" (R,G,B)");
                return sb5.toString();
            }
        }

        private void readToGyroscopeSensor() {
            this.mGyroSensor_Y = this.mGyroSensorValues[0] * 57.295776f;
            this.mGyroSensor_P = this.mGyroSensorValues[1] * 57.295776f;
            this.mGyroSensor_R = this.mGyroSensorValues[2] * 57.295776f;
        }

        private void readToSubGyroscopeSensor() {
            this.mSubGyroSensor_Y = this.mSubGyroSensorValues[0] * 57.295776f;
            this.mSubGyroSensor_P = this.mSubGyroSensorValues[1] * 57.295776f;
            this.mSubGyroSensor_R = this.mSubGyroSensorValues[2] * 57.295776f;
        }

        /* access modifiers changed from: private */
        public String getGyroscopeSensorValueString() {
            StringBuilder sb = new StringBuilder();
            sb.append("GYROSCOPE: Y: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mGyroSensor_Y));
            sb.append(", P: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mGyroSensor_P));
            sb.append(", R: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mGyroSensor_R));
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getSubGyroscopeSensorValueString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Sub GYROSCOPE: Y: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mSubGyroSensor_Y));
            sb.append(", P: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mSubGyroSensor_P));
            sb.append(", R: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mSubGyroSensor_R));
            return sb.toString();
        }

        private void readToMagneticSensor() {
            this.mMagnSensor_X = this.mMagnSensorValues[0];
            this.mMagnSensor_Y = this.mMagnSensorValues[1];
            this.mMagnSensor_Z = this.mMagnSensorValues[2];
        }

        /* access modifiers changed from: private */
        public String getMagneticSensorValueString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MAGNETIC: 2, x: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mMagnSensor_X));
            sb.append(", y: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mMagnSensor_Y));
            sb.append(", z: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mMagnSensor_Z));
            return sb.toString();
        }

        private void readToOrientationSensor() {
            this.mOrieSensorAzimuth = this.mOrieSensorValues[0];
            this.mOrieSensorPitch = this.mOrieSensorValues[1];
            this.mOrieSensorRoll = this.mOrieSensorValues[2];
        }

        /* access modifiers changed from: private */
        public String getOrientationSensorAzimuthString() {
            StringBuilder sb = new StringBuilder();
            sb.append("AZIMUTH: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mOrieSensorAzimuth));
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getOrientationSensorPitchString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PITCH: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mOrieSensorPitch));
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getOrientationSensorRollString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ROLL: ");
            sb.append(SensorTest.this.mFormat.format((double) this.mOrieSensorRoll));
            return sb.toString();
        }

        private void readToUltrasonicSensor() {
            String distanceData = Kernel.read(Kernel.ULTRASONIC_ADC_DISTANCE);
            if (distanceData != null) {
                this.ultraAdcDistanceData = distanceData.split(",");
                int UltraDistance = Integer.parseInt(this.ultraAdcDistanceData[1]);
                if (UltraDistance <= this.ULTRASONIC_MINIMUM_DETECTABLE_DISTANCE && UltraDistance != 0) {
                    SensorTest.this.isCloseMode = true;
                    SensorTest.this.mCloseOutBoundCount = 0;
                } else if ((UltraDistance > this.ULTRASONIC_MINIMUM_DETECTABLE_DISTANCE || UltraDistance == 0) && SensorTest.this.isCloseMode) {
                    SensorTest.this.mCloseOutBoundCount = SensorTest.this.mCloseOutBoundCount + 1;
                    if (SensorTest.this.mCloseOutBoundCount >= 3) {
                        SensorTest.this.isCloseMode = false;
                        SensorTest.this.mCloseOutBoundCount = 0;
                    }
                }
                if (SensorTest.this.isCloseMode) {
                    this.ultraAdcDistanceData[1] = Integer.toString(this.ULTRASONIC_MINIMUM_DETECTABLE_DISTANCE);
                    StringBuilder sb = new StringBuilder();
                    sb.append("ultraAdcDistanceData : ");
                    sb.append(this.ultraAdcDistanceData[1]);
                    LtUtil.log_d(SensorTest.this.CLASS_NAME, " readToUltrasonicSensor", sb.toString());
                    return;
                }
                return;
            }
            LtUtil.log_d(SensorTest.this.CLASS_NAME, " readToUltrasonicSensor", "distanceData is null");
        }

        /* access modifiers changed from: private */
        public String getUltrasonicSensorAdcDistanceString() {
            if (!SensorTest.this.bReadUltrasonicData || this.ultraAdcDistanceData[0] == null || this.ultraAdcDistanceData[1] == null) {
                return "ADC : 0 Distance : 0";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("ADC : ");
            sb.append(this.ultraAdcDistanceData[0]);
            sb.append(" Distance : ");
            sb.append(this.ultraAdcDistanceData[1]);
            return sb.toString();
        }
    }

    static /* synthetic */ float access$1916(SensorTest x0, float x1) {
        float f = x0.mBaromPressureTotal + x1;
        x0.mBaromPressureTotal = f;
        return f;
    }

    public SensorTest() {
        super("SensorTest");
    }

    private void registerTimeSetReceiver() {
        LtUtil.log_d(this.CLASS_NAME, "registerTimeSetReceiver", "");
        IntentFilter intentTimer = new IntentFilter();
        intentTimer.addAction("android.intent.action.TIME_SET");
        registerReceiver(this.mTimeSetReceiver, intentTimer);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.sensor_test);
        this.mIsProxAutoCal = Feature.getBoolean(Feature.IS_PROXIMITY_AUTO_CALIBRATION);
        StringBuilder sb = new StringBuilder();
        sb.append("mIsProxAutoCal : ");
        sb.append(this.mIsProxAutoCal);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        this.mIsFactoryBin = FactoryTest.isFactoryBinary();
        initialize();
        DecimalFormatSymbols paramDecimalFormatSymbols = new DecimalFormatSymbols();
        paramDecimalFormatSymbols.setDecimalSeparator('.');
        this.mFormat = new DecimalFormat("#.##", paramDecimalFormatSymbols);
        if (this.use_CpAccelerometer) {
            this.mFactoryPhone = new FactoryTestPhone(this);
            this.mFactoryPhone.bindSecPhoneService();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mSensorTask == null) {
            this.mSensorTask = new SensorTask();
            LtUtil.log_d(this.CLASS_NAME, "onResume", "new sensor task");
        }
        if (this.mSensorTimer == null) {
            this.mSensorTimer = new Timer("SensorTimer");
            LtUtil.log_d(this.CLASS_NAME, "onResume", "new sensor timer");
        }
        this.mSensorTimer.schedule(this.mSensorTask, 0, 10);
        this.mIsVibrate = false;
        this.mProxSensorValue = -1.0f;
        registerTimeSetReceiver();
        if (TestCase.getEnabled(TestCase.IS_PROXIMITY_TEST_MOTOR_FEEDBACK)) {
            this.mVibrator.cancel();
        }
        if (this.use_Flicker) {
            Kernel.write(Kernel.FLICKER_SENSOR_POWER, EgisFingerprint.MAJOR_VERSION);
        }
        this.mBackground.setBackgroundColor(getResources().getColor(C0268R.color.white));
        this.mSensorArrow.setCurrentCal(0);
        this.mSensorManager.registerListener(this.mSensorTask, this.mAccelerometerSensor, 2);
        if (this.use_Sub_Accelerometer) {
            this.mSensorManager.registerListener(this.mSensorTask, this.mSubAccelerometerSensor, 2);
        }
        if (this.use_Proximity || this.use_Proximity_ADC) {
            this.mSensorManager.registerListener(this.mProximityListener, this.mProximitySensor, 2);
        }
        if (this.use_IrisProximity) {
            this.mSensorManager.registerListener(this.mIrisProximityListener, this.mIrisProximitySensor, 2);
        }
        if (this.use_Light_LUX || this.use_Light_ADC) {
            this.mSensorManager.registerListener(this.mSensorTask, this.mLightSensor, 3);
        }
        this.mSensorManager.registerListener(this.mSensorTask, this.mBarometerSensor, 2);
        this.mSensorManager.registerListener(this.mSensorTask, this.mGyroscopeSensor, 2);
        if (this.use_Sub_Gyroscope) {
            this.mSensorManager.registerListener(this.mSensorTask, this.mSubGyroscopeSensor, 2);
        }
        this.mSensorManager.registerListener(this.mSensorTask, this.mMagneticSensor, 2);
        this.mSensorManager.registerListener(this.mSensorTask, this.mOrientationSensor, 0);
        this.mSensorManager.registerListener(this.mSensorTask, this.mTempSensor, 2);
        this.mSensorManager.registerListener(this.mSensorTask, this.mHumidSensor, 2);
        if (this.use_Flicker) {
            this.mSensorManager.registerListener(this.mSensorTask, this.mFlickerSensor, 2);
        }
        this.mSensorTask.resume();
        if (this.use_Proximity) {
            try {
                Kernel.write(Kernel.PROXI_SENSOR_ADC_AVG, EgisFingerprint.MAJOR_VERSION);
            } catch (Exception e) {
                LtUtil.log_d(this.CLASS_NAME, "onResume", "Exception accessing prox avg file");
            }
            String default_trim = null;
            if (!this.mIsProxAutoCal) {
                default_trim = Kernel.read(Kernel.PROXI_SENSOR_DEFAULT_TRIM);
            }
            if (default_trim == null) {
                this.mProxSensorDefaultTrimText.setVisibility(8);
                LtUtil.log_d(this.CLASS_NAME, "onResume", "default_trim is null & DefaultTrim GONE");
            } else {
                TextView textView = this.mProxSensorDefaultTrimText;
                StringBuilder sb = new StringBuilder();
                sb.append("Default Trim : ");
                sb.append(default_trim);
                textView.setText(sb.toString());
            }
        }
        if (this.use_OIS_Gyro) {
            Kernel.write(Kernel.GYRO_OIS_POWER, EgisFingerprint.MAJOR_VERSION);
            readToOisGyroSensor();
        }
        if (this.use_TspProximity) {
            showProxOffset();
        }
        if (this.use_Proximity_ADC) {
            showProxOffset();
            showProxDeviceID();
        }
        if (this.use_Ultrasonic) {
            this.mUltraSensorCompanyFWValueText.setText(getUltrasonicSensorVerValueString());
            this.isCloseMode = false;
            this.mCloseOutBoundCount = 0;
        }
        if (this.use_CpAccelerometer) {
            controlCPsAccelerometer(1);
            registerCpAccelermeterReceiver();
        }
        if (this.use_HRM && this.mHrm_Vendor != null) {
            if (this.mHrm_Vendor.equalsIgnoreCase(ModuleSensor.FEATURE_GYROSCOP_MAXIM)) {
                TextView textView2 = this.mHRMSensorDeivceIdText;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ID : ");
                sb2.append(Kernel.read(Kernel.HRM_DEVICE_ID));
                textView2.setText(sb2.toString());
                TextView textView3 = this.mHRMSensorLibraryText;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Version : ");
                sb3.append(Kernel.read(Kernel.HRM_LIBRARY_VER));
                textView3.setText(sb3.toString());
                TextView textView4 = this.mHRMSensorDriverText;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Driver : ");
                sb4.append(Kernel.read(Kernel.HRM_DRIVER_VER));
                textView4.setText(sb4.toString());
            } else if (this.mHrm_Vendor.equalsIgnoreCase("PARTRON")) {
                TextView textView5 = this.mHRMSensorLibraryText;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Version : ");
                sb5.append(Kernel.read(Kernel.HRM_LIBRARY_VER));
                textView5.setText(sb5.toString());
            } else if (this.mHrm_Vendor.equalsIgnoreCase("ADI")) {
                if ("ADPD143RI".equals(this.mHrm_Name)) {
                    TextView textView6 = this.mHRMSensorDeivceIdText;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("ID : ");
                    sb6.append(Kernel.read(Kernel.HRM_DEVICE_ID));
                    textView6.setText(sb6.toString());
                    TextView textView7 = this.mHRMSensorLibraryText;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Version : ");
                    sb7.append(Kernel.read(Kernel.HRM_LIBRARY_VER));
                    textView7.setText(sb7.toString());
                } else {
                    TextView textView8 = this.mHRMSensorLibraryEOLText;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Version : ");
                    sb8.append(Kernel.read(Kernel.HRM_LIBRARY_EOL_VER));
                    textView8.setText(sb8.toString());
                    TextView textView9 = this.mHRMSensorLibraryELFText;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("ELF : ");
                    sb9.append(Kernel.read(Kernel.HRM_LIBRARY_ELF_VER));
                    textView9.setText(sb9.toString());
                }
                TextView textView10 = this.mHRMSensorDriverText;
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Driver : ");
                sb10.append(Kernel.read(Kernel.HRM_DRIVER_VER));
                textView10.setText(sb10.toString());
            }
        }
        if (isOqcsbftt) {
            registerOqcsbfttReceiver();
            NVAccessor.setNV(407, NVAccessor.NV_VALUE_ENTER);
            NVAccessor.setNV(408, NVAccessor.NV_VALUE_ENTER);
            NVAccessor.setNV(410, NVAccessor.NV_VALUE_ENTER);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.use_Proximity) {
            try {
                Kernel.write(Kernel.PROXI_SENSOR_ADC_AVG, "0");
            } catch (Exception e) {
                LtUtil.log_d(this.CLASS_NAME, "onPause", "Exception accessing prox avg file");
            }
        }
        if (this.use_Flicker) {
            Kernel.write(Kernel.FLICKER_SENSOR_POWER, "0");
        }
        this.mSensorTask.pause();
        this.mSensorTask.cancel();
        this.mSensorManager.unregisterListener(this.mSensorTask);
        this.mSensorTask = null;
        this.mSensorManager.unregisterListener(this.mProximityListener);
        if (this.use_IrisProximity) {
            this.mSensorManager.unregisterListener(this.mIrisProximityListener);
        }
        if (this.use_OIS_Gyro) {
            Kernel.write(Kernel.GYRO_OIS_POWER, "0");
        }
        if (this.use_CpAccelerometer) {
            controlCPsAccelerometer(0);
            unregisterReceiver(this.mBroadcastReceiver);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (SensorTest.this.mFactoryPhone != null) {
                        SensorTest.this.mFactoryPhone.unbindSecPhoneService();
                        SensorTest.this.mFactoryPhone = null;
                        LtUtil.log_i(SensorTest.this.CLASS_NAME, "onPause", "Unbind SecPhoneService");
                    }
                }
            }, 5000);
        }
        this.mVibrator.cancel();
        unregisterReceiver(this.mTimeSetReceiver);
        if (this.use_Proximity_ADC || this.use_TspProximity) {
            this.mAutoOffsetTask.cancel();
            this.offsetTimer.purge();
            this.offsetTimer.cancel();
            this.mAutoOffsetTask = null;
            this.offsetTimer = null;
            LtUtil.log_d(this.CLASS_NAME, "onPause", "offset task, timer - purge cancel");
        }
        this.mSensorTimer.purge();
        this.mSensorTimer.cancel();
        this.mSensorTimer = null;
        LtUtil.log_d(this.CLASS_NAME, "onPause", "sensor task, timer - purge cancel");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        if (isOqcsbftt) {
            unregisterReceiver(this.mBroadcastReceiver);
            if (this.mOqcTimer != null) {
                this.mOqcTimer.purge();
                this.mOqcTimer.cancel();
                this.mOqcTimer = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        Intent intent;
        Intent intent2;
        Intent intent3;
        Intent intent4;
        Intent intent5;
        Intent intent6;
        Intent intent7;
        Intent intent8;
        Intent intent9;
        Intent intent10;
        Intent intent11;
        switch (v.getId()) {
            case C0268R.C0269id.btn_acc_image_test /*2131296546*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "Acc Image Test Start");
                startActivity(new Intent(this, AccImageTest.class));
                return;
            case C0268R.C0269id.btn_acc_graph /*2131296547*/:
                startActivity(new Intent(this, AccGraphActivity.class));
                return;
            case C0268R.C0269id.btn_sub_acc_graph /*2131296552*/:
                Intent intent12 = new Intent(this, AccGraphActivity.class);
                intent12.putExtra("sub_acc", true);
                startActivity(intent12);
                return;
            case C0268R.C0269id.btn_barom_altitude /*2131296574*/:
                String pressureText = this.mBaromSensorSettingAltitudeEdit.getText().toString();
                if (pressureText != null) {
                    this.mSensorTask.setBarometerSensorPressure(Double.valueOf(pressureText));
                    this.mSensorTask.setBarometerSensorAdjustValue(pressureText);
                    return;
                }
                return;
            case C0268R.C0269id.btn_barom_selftest /*2131296577*/:
                startActivity(new Intent(this, BarometerSelfTest.class));
                return;
            case C0268R.C0269id.btn_light_test /*2131296583*/:
                startActivity(new Intent(this, LightSensorReadTest.class));
                return;
            case C0268R.C0269id.btn_gyro_selftest /*2131296587*/:
                startGyroscopeSelfTest();
                return;
            case C0268R.C0269id.btn_gyro_display /*2131296588*/:
                startActivity(new Intent(this, GyroscopeDisplay.class));
                return;
            case C0268R.C0269id.btn_gyro_graph /*2131296589*/:
                startActivity(new Intent(this, GyroscopeGraphActivity.class));
                return;
            case C0268R.C0269id.btn_sub_gyro_selftest /*2131296593*/:
                Intent intent13 = new Intent(this, GyroscopeIcSTMicro.class);
                intent13.putExtra("sub_gyro", true);
                startActivity(intent13);
                return;
            case C0268R.C0269id.btn_sub_gyro_display /*2131296594*/:
                Intent intent14 = new Intent(this, GyroscopeDisplay.class);
                intent14.putExtra("sub_gyro", true);
                startActivity(intent14);
                return;
            case C0268R.C0269id.btn_sub_gyro_graph /*2131296595*/:
                Intent intent15 = new Intent(this, GyroscopeGraphActivity.class);
                intent15.putExtra("sub_gyro", true);
                startActivity(intent15);
                return;
            case C0268R.C0269id.btn_ois_gyro_selftest /*2131296597*/:
                startActivity(new Intent(this, GyroscopeOIS.class));
                return;
            case C0268R.C0269id.btn_magn_self_test /*2131296608*/:
                Intent intent16 = new Intent();
                String feature = ModuleSensor.instance(this).mFeature_Magnetic;
                if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8973) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8975) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C_MANAGER) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                    intent16.setClass(this, MagneticAsahi.class);
                    startActivity(intent16);
                    return;
                } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS529) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530C) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532B) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS537) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
                    intent16.setClass(this, MagneticYamaha.class);
                    startActivity(intent16);
                    return;
                } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD004) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD004A) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD006A) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD008A)) {
                    intent16.setClass(this, MagneticAlps.class);
                    startActivity(intent16);
                    return;
                } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_NEWEST)) {
                    startActivity(new Intent(this, MagneticBosch.class));
                    return;
                } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
                    startActivity(new Intent(this, MagneticSTMicro.class));
                    return;
                } else {
                    return;
                }
            case C0268R.C0269id.btn_magn_power_noise_test /*2131296609*/:
                startActivity(new Intent(this, PowerNoiseGraphActivity.class));
                return;
            case C0268R.C0269id.btn_hrm_start /*2131296613*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_hrm_start");
                startActivity(new Intent(this, HrmTest.class));
                return;
            case C0268R.C0269id.btn_hrm_eoltest /*2131296614*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_hrm_eoltest");
                if (Feature.getBoolean(Feature.SUPPORT_HRM_CONCEPT_ONCE)) {
                    intent = new Intent(this, HrmEolOnce.class);
                } else {
                    intent = new Intent(this, HrmEolTest.class);
                }
                intent.addFlags(536870912);
                startActivity(intent);
                return;
            case C0268R.C0269id.btn_fingerprint_normalscan /*2131296622*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_fingerprint_normalscan");
                if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "NAMSAN".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    if (this.mIsFactoryBin) {
                        intent5 = new Intent(this, FingerPrintTest_namsan.class);
                    } else {
                        intent5 = new Intent(this, FingerPrintTest_namsanTZ.class);
                    }
                    intent5.putExtra("Action", "NORMALDATA");
                    startActivity(intent5);
                    return;
                } else if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "VIPER".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    if (this.mIsFactoryBin) {
                        intent4 = new Intent(this, FingerPrintTest_touch.class);
                    } else {
                        intent4 = new Intent(this, FingerPrintTest_comb.class);
                    }
                    intent4.putExtra("Action", "NORMALDATA");
                    startActivity(intent4);
                    return;
                } else if ("GOODIX".equals(this.mFingerPrint_Vendor)) {
                    if (this.mIsFactoryBin) {
                        intent3 = new Intent(this, FingerPrintTest_cap.class);
                    } else {
                        intent3 = new Intent(this, FingerPrintTest_capTZ.class);
                    }
                    intent3.putExtra("Action", "NORMALDATA");
                    startActivity(intent3);
                    return;
                } else if ("EGISTEC".equals(this.mFingerPrint_Vendor)) {
                    if (this.mIsFactoryBin) {
                        intent2 = new Intent(this, FingerPrintTest_egis.class);
                    } else {
                        intent2 = new Intent(this, FingerPrintTest_egisTZ.class);
                    }
                    intent2.putExtra("Action", "NORMALDATA");
                    startActivity(intent2);
                    return;
                } else if (!"QCOM".equals(this.mFingerPrint_Vendor) || !"qbt2000".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    LtUtil.log_i(this.CLASS_NAME, "onClick", "Can not find the vendor's name or chip's name");
                    return;
                } else {
                    Intent intent17 = new Intent(this, FingerPrintTest_qbt2000.class);
                    intent17.putExtra("tz_enabled", true ^ this.mIsFactoryBin);
                    intent17.putExtra("Action", "NORMALDATA");
                    startActivity(intent17);
                    return;
                }
            case C0268R.C0269id.btn_fingerprint_snr /*2131296623*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_fingerprint_snr");
                if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "NAMSAN".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    Intent intent18 = new Intent(this, FingerPrintTest_namsan.class);
                    intent18.putExtra("Action", "SNR");
                    startActivity(intent18);
                    return;
                } else if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "VIPER".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    Intent intent19 = new Intent(this, FingerPrintTest_touch.class);
                    intent19.putExtra("Action", "SNR");
                    startActivity(intent19);
                    return;
                } else if ("GOODIX".equals(this.mFingerPrint_Vendor)) {
                    Intent intent20 = new Intent(this, FingerPrintTest_cap.class);
                    intent20.putExtra("Action", "SNR");
                    startActivity(intent20);
                    return;
                } else if ("EGISTEC".equals(this.mFingerPrint_Vendor)) {
                    if (this.mFingerPrint_Name == null || !this.mFingerPrint_Name.contains("ET71")) {
                        intent6 = new Intent(this, FingerPrintTest_egis.class);
                        intent6.putExtra("Action", "SNR");
                    } else {
                        intent6 = new Intent(this, FingerprintCalTest_egisOptical.class);
                    }
                    startActivity(intent6);
                    return;
                } else if (!"QCOM".equals(this.mFingerPrint_Vendor) || !"qbt2000".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    LtUtil.log_i(this.CLASS_NAME, "onClick", "Can not find the vendor's name or chip's name");
                    return;
                } else {
                    Intent intent21 = new Intent(this, FingerPrintTest_qbt2000.class);
                    intent21.putExtra("tz_enabled", true ^ this.mIsFactoryBin);
                    intent21.putExtra("Action", "SNR");
                    startActivity(intent21);
                    return;
                }
            case C0268R.C0269id.btn_fingerprint_sensorinfo /*2131296624*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_fingerprint_sensorinfo");
                if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "NAMSAN".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    if (this.mIsFactoryBin) {
                        intent11 = new Intent(this, FingerPrintTest_namsan.class);
                    } else {
                        intent11 = new Intent(this, FingerPrintTest_namsanTZ.class);
                    }
                    intent11.putExtra("Action", "SENSORINFO");
                    startActivity(intent11);
                    return;
                } else if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "VIPER".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    if (this.mIsFactoryBin) {
                        intent10 = new Intent(this, FingerPrintTest_touch.class);
                    } else {
                        intent10 = new Intent(this, FingerPrintTest_comb.class);
                    }
                    intent10.putExtra("Action", "SENSORINFO");
                    startActivity(intent10);
                    return;
                } else if ("SYNAPTICS".equals(this.mFingerPrint_Vendor) && "CPID".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    if (this.mIsFactoryBin) {
                        intent9 = new Intent(this, FingerPrintTest_cpid.class);
                    } else {
                        intent9 = new Intent(this, FingerPrintTest_cpidTZ.class);
                    }
                    intent9.putExtra("Action", "SENSORINFO");
                    startActivity(intent9);
                    return;
                } else if ("GOODIX".equals(this.mFingerPrint_Vendor)) {
                    if (this.mIsFactoryBin) {
                        intent8 = new Intent(this, FingerPrintTest_cap.class);
                    } else {
                        intent8 = new Intent(this, FingerPrintTest_capTZ.class);
                    }
                    intent8.putExtra("Action", "SENSORINFO");
                    startActivity(intent8);
                    return;
                } else if ("EGISTEC".equals(this.mFingerPrint_Vendor)) {
                    if (this.mIsFactoryBin) {
                        intent7 = new Intent(this, FingerPrintTest_egis.class);
                    } else {
                        intent7 = new Intent(this, FingerPrintTest_egisTZ.class);
                    }
                    intent7.putExtra("Action", "SENSORINFO");
                    startActivity(intent7);
                    return;
                } else if (!"QCOM".equals(this.mFingerPrint_Vendor) || !"qbt2000".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    LtUtil.log_i(this.CLASS_NAME, "onClick", "Can not find the vendor's name or chip's name");
                    return;
                } else {
                    Intent intent22 = new Intent(this, FingerPrintSensorInfo_qbt2000.class);
                    intent22.putExtra("tz_enabled", true ^ this.mIsFactoryBin);
                    intent22.putExtra("Action", "SENSORINFO");
                    startActivity(intent22);
                    return;
                }
            case C0268R.C0269id.btn_fingerprint_intcheck /*2131296625*/:
                LtUtil.log_i(this.CLASS_NAME, "onClick", "btn_fingerprint_intcheck");
                if (!"QCOM".equals(this.mFingerPrint_Vendor) || !"qbt2000".equalsIgnoreCase(this.mFingerPrint_Name)) {
                    LtUtil.log_i(this.CLASS_NAME, "onClick", "Can not find the vendor's name or chip's name");
                    return;
                }
                Intent intent23 = new Intent(this, FingerprintIntCheckTest_qbt2000.class);
                intent23.putExtra("tz_enabled", true ^ this.mIsFactoryBin);
                intent23.putExtra("Action", "INTCHECK");
                startActivity(intent23);
                return;
            default:
                return;
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

    /* JADX WARNING: type inference failed for: r11v0, types: [android.content.Context, com.sec.android.app.hwmoduletest.SensorTest, android.view.View$OnClickListener] */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x04f8, code lost:
        r2 = "Fatal error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x0538, code lost:
        r2 = "Fatal error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x05ae, code lost:
        r2 = "Fatal error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x05eb, code lost:
        r2 = "Sensor Init error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x0602, code lost:
        r2 = "Fatal error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:214:0x0630, code lost:
        r2 = "Sensor Init error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x0647, code lost:
        r2 = "Fatal error";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x064c, code lost:
        com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r11.CLASS_NAME, "initialize", "exception!");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:226:0x0657, code lost:
        com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r11.CLASS_NAME, "initialize", "library link exception!");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:225:0x0656 A[ExcHandler: UnsatisfiedLinkError (e java.lang.UnsatisfiedLinkError), Splitter:B:120:0x04be] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initialize() {
        /*
            r11 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            java.lang.String[] r1 = com.sec.xmldata.support.Support.SensorTestMenu.getSensorTest()
            java.util.List r1 = java.util.Arrays.asList(r1)
            r0.<init>(r1)
            r1 = 0
            r2 = r1
        L_0x000f:
            int r3 = r0.size()
            r4 = 1
            if (r2 >= r3) goto L_0x010d
            int r3 = r2 + 3
            int r5 = r0.size()
            if (r3 < r5) goto L_0x0023
            int r3 = r0.size()
            goto L_0x0025
        L_0x0023:
            int r3 = r2 + 3
        L_0x0025:
            java.util.List r3 = r0.subList(r2, r3)
            java.util.Iterator r5 = r3.iterator()
        L_0x002d:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x0109
            java.lang.Object r6 = r5.next()
            java.lang.String r6 = (java.lang.String) r6
            java.lang.String r7 = ","
            java.lang.String[] r7 = r6.split(r7)
            r7 = r7[r1]
            java.lang.String r8 = ","
            java.lang.String[] r8 = r6.split(r8)
            r8 = r8[r4]
            java.lang.String r9 = "Accelerometer"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x0053
            r11.use_Accelerometer = r4
        L_0x0053:
            java.lang.String r9 = "Sub Accelerometer"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x005d
            r11.use_Sub_Accelerometer = r4
        L_0x005d:
            java.lang.String r9 = "CpAccelerometer"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x0067
            r11.use_CpAccelerometer = r4
        L_0x0067:
            java.lang.String r9 = "Proximity_state"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x0071
            r11.use_Proximity = r4
        L_0x0071:
            java.lang.String r9 = "IRIS_Proximity_state"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x007b
            r11.use_IrisProximity = r4
        L_0x007b:
            java.lang.String r9 = "Flicker"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x0085
            r11.use_Flicker = r4
        L_0x0085:
            java.lang.String r9 = "TSP_Proximity_state"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x008f
            r11.use_TspProximity = r4
        L_0x008f:
            java.lang.String r9 = "Proximity_ADC"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x0099
            r11.use_Proximity_ADC = r4
        L_0x0099:
            java.lang.String r9 = "Barometer"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00a3
            r11.use_Barometer = r4
        L_0x00a3:
            java.lang.String r9 = "Light_LUX"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00ad
            r11.use_Light_LUX = r4
        L_0x00ad:
            java.lang.String r9 = "Light_ADC"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00b7
            r11.use_Light_ADC = r4
        L_0x00b7:
            java.lang.String r9 = "Gyroscope"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00c1
            r11.use_Gyroscope = r4
        L_0x00c1:
            java.lang.String r9 = "Sub Gyroscope"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00cb
            r11.use_Sub_Gyroscope = r4
        L_0x00cb:
            java.lang.String r9 = "OIS_Gyro"
            boolean r9 = r9.equals(r8)
            if (r9 == 0) goto L_0x00d5
            r11.use_OIS_Gyro = r4
        L_0x00d5:
            java.lang.String r9 = "Magnetic"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00df
            r11.use_Magnetic = r4
        L_0x00df:
            java.lang.String r9 = "Magnetic_PowerNoise"
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L_0x00e9
            r11.use_Magnetic_PowerNoise = r4
        L_0x00e9:
            java.lang.String r9 = "Ultrasonic"
            boolean r9 = r9.equals(r8)
            if (r9 == 0) goto L_0x00f3
            r11.use_Ultrasonic = r4
        L_0x00f3:
            java.lang.String r9 = "HRM"
            boolean r9 = r9.equals(r8)
            if (r9 == 0) goto L_0x00fd
            r11.use_HRM = r4
        L_0x00fd:
            java.lang.String r9 = "Fingerprint"
            boolean r9 = r9.equals(r8)
            if (r9 == 0) goto L_0x0107
            r11.use_Fingerprint = r4
        L_0x0107:
            goto L_0x002d
        L_0x0109:
            int r2 = r2 + 3
            goto L_0x000f
        L_0x010d:
            r11.checkValidSysfPathLightSensor()
            boolean r2 = r11.isRgbSensorSupported()
            r11.mIsRGBSensor = r2
            r2 = 2131296262(0x7f090006, float:1.8210436E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.LinearLayout r2 = (android.widget.LinearLayout) r2
            r11.mBackground = r2
            r2 = 2131296543(0x7f09011f, float:1.8211006E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mAcceSensorTitleText = r2
            r2 = 2131296544(0x7f090120, float:1.8211008E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mAcceSeparator = r2
            r2 = 2131296545(0x7f090121, float:1.821101E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mAcceSensorValueText = r2
            r2 = 2131296546(0x7f090122, float:1.8211012E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mAcceImageTestButton = r2
            r2 = 2131296547(0x7f090123, float:1.8211014E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mAcceGraphButton = r2
            r2 = 2131296548(0x7f090124, float:1.8211016E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mSubAcceSensorTitleText = r2
            r2 = 2131296549(0x7f090125, float:1.8211018E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mSubAcceSeparator = r2
            r2 = 2131296550(0x7f090126, float:1.821102E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mSubAcceSensorValueText = r2
            r2 = 2131296552(0x7f090128, float:1.8211024E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mSubAcceGraphButton = r2
            r2 = 2131296553(0x7f090129, float:1.8211026E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mCpAcceSensorTitleText = r2
            r2 = 2131296554(0x7f09012a, float:1.8211028E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mCpAcceSeparator = r2
            r2 = 2131296555(0x7f09012b, float:1.821103E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mCpAcceSensorValueText = r2
            boolean r2 = r11.use_Accelerometer
            r3 = 8
            if (r2 != 0) goto L_0x01be
            android.widget.TextView r2 = r11.mAcceSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mAcceSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mAcceSensorValueText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mAcceImageTestButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mAcceGraphButton
            r2.setVisibility(r3)
        L_0x01be:
            boolean r2 = r11.use_Sub_Accelerometer
            if (r2 != 0) goto L_0x01d6
            android.widget.TextView r2 = r11.mSubAcceSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mSubAcceSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mSubAcceSensorValueText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mSubAcceGraphButton
            r2.setVisibility(r3)
        L_0x01d6:
            boolean r2 = r11.use_CpAccelerometer
            if (r2 != 0) goto L_0x01e9
            android.widget.TextView r2 = r11.mCpAcceSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mCpAcceSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mCpAcceSensorValueText
            r2.setVisibility(r3)
        L_0x01e9:
            r2 = 2131296556(0x7f09012c, float:1.8211032E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorTitleText = r2
            r2 = 2131296557(0x7f09012d, float:1.8211034E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mProxSeparator = r2
            r2 = 2131296558(0x7f09012e, float:1.8211036E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorStatusText = r2
            r2 = 2131296559(0x7f09012f, float:1.8211038E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorAdcAvgText = r2
            r2 = 2131296560(0x7f090130, float:1.821104E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorTspColorIdText = r2
            r2 = 2131296561(0x7f090131, float:1.8211042E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorDefaultTrimText = r2
            r2 = 2131296562(0x7f090132, float:1.8211044E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorOffsetText = r2
            r2 = 2131296563(0x7f090133, float:1.8211046E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorHighThresholdText = r2
            r2 = 2131296564(0x7f090134, float:1.8211048E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorLowThresholdText = r2
            r2 = 2131296565(0x7f090135, float:1.821105E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensor1stThresholdText = r2
            r2 = 2131296566(0x7f090136, float:1.8211052E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensor2ndThresholdText = r2
            r2 = 2131296567(0x7f090137, float:1.8211054E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mProxSensorDeviceID = r2
            r2 = 2131296568(0x7f090138, float:1.8211056E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mIrisProxSensorTitleText = r2
            r2 = 2131296569(0x7f090139, float:1.8211058E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mIrisProxSeparator = r2
            r2 = 2131296570(0x7f09013a, float:1.821106E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mIrisProxSensorStatusText = r2
            r2 = 2131296584(0x7f090148, float:1.8211089E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mFlickerSensorText = r2
            boolean r2 = r11.use_Proximity
            if (r2 != 0) goto L_0x02ad
            android.widget.TextView r2 = r11.mProxSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mProxSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorStatusText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorDefaultTrimText
            r2.setVisibility(r3)
        L_0x02ad:
            boolean r2 = r11.use_IrisProximity
            if (r2 != 0) goto L_0x02c0
            android.widget.TextView r2 = r11.mIrisProxSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mIrisProxSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mIrisProxSensorStatusText
            r2.setVisibility(r3)
        L_0x02c0:
            boolean r2 = r11.use_Flicker
            if (r2 != 0) goto L_0x02c9
            android.widget.TextView r2 = r11.mFlickerSensorText
            r2.setVisibility(r3)
        L_0x02c9:
            boolean r2 = r11.use_Proximity_ADC
            if (r2 != 0) goto L_0x02eb
            android.widget.TextView r2 = r11.mProxSensorAdcAvgText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorTspColorIdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorOffsetText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorHighThresholdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorLowThresholdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorDeviceID
            r2.setVisibility(r3)
        L_0x02eb:
            boolean r2 = r11.use_TspProximity
            if (r2 == 0) goto L_0x030d
            android.widget.TextView r2 = r11.mProxSensorTitleText
            r2.setVisibility(r1)
            android.view.View r2 = r11.mProxSeparator
            r2.setVisibility(r1)
            android.widget.TextView r2 = r11.mProxSensorStatusText
            r2.setVisibility(r1)
            android.widget.TextView r2 = r11.mProxSensorAdcAvgText
            r2.setVisibility(r1)
            android.widget.TextView r2 = r11.mProxSensorHighThresholdText
            r2.setVisibility(r1)
            android.widget.TextView r2 = r11.mProxSensorLowThresholdText
            r2.setVisibility(r1)
        L_0x030d:
            boolean r2 = r11.mIsProxAutoCal
            if (r2 == 0) goto L_0x0325
            android.widget.TextView r2 = r11.mProxSensorHighThresholdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensorLowThresholdText
            r2.setVisibility(r3)
            java.lang.String r2 = r11.CLASS_NAME
            java.lang.String r5 = "initialize"
            java.lang.String r6 = "mIsProxAutoCal is true & HighThreshold,LowThreshold GONE"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r5, r6)
            goto L_0x0338
        L_0x0325:
            android.widget.TextView r2 = r11.mProxSensor1stThresholdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mProxSensor2ndThresholdText
            r2.setVisibility(r3)
            java.lang.String r2 = r11.CLASS_NAME
            java.lang.String r5 = "initialize"
            java.lang.String r6 = "mIsProxAutoCal is false & 1stThreshold,2ndThreshold GONE"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r5, r6)
        L_0x0338:
            java.lang.String r2 = "LCD_TYPE_PATH"
            boolean r2 = com.sec.xmldata.support.Support.Kernel.isExistFile(r2)
            r5 = 6
            r6 = 5
            if (r2 == 0) goto L_0x0376
            java.lang.String r2 = "LCD_TYPE_PATH"
            java.lang.String r2 = com.sec.xmldata.support.Support.Kernel.read(r2)
            if (r2 == 0) goto L_0x0356
            boolean r7 = r2.isEmpty()
            if (r7 == 0) goto L_0x0351
            goto L_0x0356
        L_0x0351:
            java.lang.String r2 = r2.substring(r6, r5)
            goto L_0x035f
        L_0x0356:
            java.lang.String r7 = r11.CLASS_NAME
            java.lang.String r8 = "setColorID"
            java.lang.String r9 = "No color id, Please check .../color id node"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r7, r8, r9)
        L_0x035f:
            android.widget.TextView r7 = r11.mProxSensorTspColorIdText
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "TSP color ID : "
            r8.append(r9)
            r8.append(r2)
            java.lang.String r8 = r8.toString()
            r7.setText(r8)
            goto L_0x037d
        L_0x0376:
            android.widget.TextView r2 = r11.mProxSensorTspColorIdText
            java.lang.String r7 = "TSP color ID : NA"
            r2.setText(r7)
        L_0x037d:
            r2 = 2131296611(0x7f090163, float:1.8211144E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorTitleText = r2
            r2 = 2131296612(0x7f090164, float:1.8211146E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mHRMSeparator = r2
            r2 = 2131296613(0x7f090165, float:1.8211148E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mHRMSensorStartButton = r2
            r2 = 2131296614(0x7f090166, float:1.821115E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mHRMSensorEOLTestButton = r2
            r2 = 2131296615(0x7f090167, float:1.8211152E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorLibraryText = r2
            r2 = 2131296616(0x7f090168, float:1.8211154E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorDriverText = r2
            r2 = 2131296617(0x7f090169, float:1.8211156E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorDeivceIdText = r2
            r2 = 2131296618(0x7f09016a, float:1.8211158E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorLibraryEOLText = r2
            r2 = 2131296619(0x7f09016b, float:1.821116E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mHRMSensorLibraryELFText = r2
            boolean r2 = r11.use_HRM
            if (r2 != 0) goto L_0x040f
            android.widget.TextView r2 = r11.mHRMSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mHRMSeparator
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mHRMSensorStartButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mHRMSensorEOLTestButton
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mHRMSensorLibraryText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mHRMSensorDriverText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mHRMSensorDeivceIdText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mHRMSensorLibraryEOLText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mHRMSensorLibraryELFText
            r2.setVisibility(r3)
        L_0x040f:
            r2 = 2131296620(0x7f09016c, float:1.8211162E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mFPTitleText = r2
            r2 = 2131296621(0x7f09016d, float:1.8211164E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mFPSeparator = r2
            r2 = 2131296622(0x7f09016e, float:1.8211166E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mFPTestButton_NormalScan = r2
            r2 = 2131296623(0x7f09016f, float:1.8211168E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mFPTestButton_SNR = r2
            r2 = 2131296624(0x7f090170, float:1.821117E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mFPTestButton_SensorInfo = r2
            r2 = 2131296625(0x7f090171, float:1.8211172E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mFPTestButton_IntCheck = r2
            r2 = 2131296626(0x7f090172, float:1.8211174E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mFPVersion = r2
            boolean r2 = r11.use_Fingerprint
            if (r2 != 0) goto L_0x047e
            android.widget.TextView r2 = r11.mFPTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mFPSeparator
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mFPTestButton_NormalScan
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mFPTestButton_SNR
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mFPTestButton_SensorInfo
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mFPVersion
            r2.setVisibility(r3)
            goto L_0x068f
        L_0x047e:
            java.lang.String r2 = "FINGERPRINT_VENDOR"
            java.lang.String r2 = com.sec.xmldata.support.Support.Kernel.read(r2)
            r11.mFingerPrint_Vendor = r2
            java.lang.String r2 = "FINGERPRINT_CHIP_NAME"
            java.lang.String r2 = com.sec.xmldata.support.Support.Kernel.read(r2)
            r11.mFingerPrint_Name = r2
            java.lang.String r2 = "factory"
            java.lang.String r7 = "BINARY_TYPE"
            java.lang.String r7 = com.sec.xmldata.support.Support.Properties.get(r7)
            boolean r2 = r2.equalsIgnoreCase(r7)
            if (r2 != 0) goto L_0x04ba
            android.widget.Button r2 = r11.mFPTestButton_SNR
            r2.setVisibility(r3)
            java.lang.String r2 = "QCOM"
            java.lang.String r7 = r11.mFingerPrint_Vendor
            boolean r2 = r2.equals(r7)
            if (r2 == 0) goto L_0x04ba
            java.lang.String r2 = "qbt2000"
            java.lang.String r7 = r11.mFingerPrint_Name
            boolean r2 = r2.equalsIgnoreCase(r7)
            if (r2 == 0) goto L_0x04ba
            android.widget.Button r2 = r11.mFPTestButton_IntCheck
            r2.setVisibility(r1)
        L_0x04ba:
            java.lang.String r2 = ""
            java.lang.String r7 = "SYNAPTICS"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x04fc
            java.lang.String r7 = "NAMSAN"
            java.lang.String r8 = r11.mFingerPrint_Name     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equalsIgnoreCase(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x04fc
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x04e4
            com.synaptics.fingerprint.namsan.Fingerprint r7 = new com.synaptics.fingerprint.namsan.Fingerprint     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r7.<init>(r11)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_namsan = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            com.synaptics.fingerprint.namsan.Fingerprint r7 = r11.mFingerprint_namsan     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            java.lang.String r7 = r7.getVersion()     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r2 = r7
            goto L_0x064a
        L_0x04e4:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x04f7, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x04f7, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x04f5:
            goto L_0x064a
        L_0x04f7:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
            goto L_0x04f5
        L_0x04fc:
            java.lang.String r7 = "SYNAPTICS"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x053c
            java.lang.String r7 = "VIPER"
            java.lang.String r8 = r11.mFingerPrint_Name     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equalsIgnoreCase(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x053c
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x0524
            com.synaptics.fingerprint.Fingerprint r7 = new com.synaptics.fingerprint.Fingerprint     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r7.<init>(r11)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_touch = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            com.synaptics.fingerprint.Fingerprint r7 = r11.mFingerprint_touch     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            java.lang.String r7 = r7.getVersion()     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r2 = r7
            goto L_0x064a
        L_0x0524:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x0537, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x0537, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x0535:
            goto L_0x064a
        L_0x0537:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
            goto L_0x0535
        L_0x053c:
            java.lang.String r7 = "SYNAPTICS"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x057c
            java.lang.String r7 = "CPID"
            java.lang.String r8 = r11.mFingerPrint_Name     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equalsIgnoreCase(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x057c
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x0564
            com.synaptics.bpd.fingerprint.Fingerprint r7 = new com.synaptics.bpd.fingerprint.Fingerprint     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r7.<init>(r11)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_cpid = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            com.synaptics.bpd.fingerprint.Fingerprint r7 = r11.mFingerprint_cpid     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            java.lang.String r7 = r7.getVersion()     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r2 = r7
            goto L_0x064a
        L_0x0564:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x0577, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x0577, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x0575:
            goto L_0x064a
        L_0x0577:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
            goto L_0x0575
        L_0x057c:
            java.lang.String r7 = "GOODIX"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x05b2
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x059a
            com.goodix.cap.fingerprint.sec.mt.GFTestManager r7 = new com.goodix.cap.fingerprint.sec.mt.GFTestManager     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r7.<init>(r11)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mGFTestManager = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            com.goodix.cap.fingerprint.sec.mt.GFTestManager r7 = r11.mGFTestManager     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            java.lang.String r7 = r7.getVersion()     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r2 = r7
            goto L_0x064a
        L_0x059a:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x05ad, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x05ad, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x05ab:
            goto L_0x064a
        L_0x05ad:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
            goto L_0x05ab
        L_0x05b2:
            java.lang.String r7 = "EGISTEC"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x0606
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x05ef
            java.lang.String r7 = r11.mFingerPrint_Name     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            if (r7 == 0) goto L_0x05dc
            java.lang.String r7 = r11.mFingerPrint_Name     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r8 = "ET71"
            boolean r7 = r7.contains(r8)     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            if (r7 == 0) goto L_0x05dc
            egistec.optical.csa.client.api.Fingerprint r7 = egistec.optical.csa.client.api.Fingerprint.create(r11)     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            r11.mFingerprint_egisOptical = r7     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            egistec.optical.csa.client.api.Fingerprint r7 = r11.mFingerprint_egisOptical     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.getVersion()     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
            goto L_0x05ee
        L_0x05dc:
            egistec.csa.client.api.Fingerprint r7 = egistec.csa.client.api.Fingerprint.create(r11)     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            r11.mFingerprint_egis = r7     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            egistec.csa.client.api.Fingerprint r7 = r11.mFingerprint_egis     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.getVersion()     // Catch:{ Exception -> 0x05ea, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
            goto L_0x05ee
        L_0x05ea:
            r7 = move-exception
            java.lang.String r8 = "Sensor Init error"
            r2 = r8
        L_0x05ee:
            goto L_0x064a
        L_0x05ef:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x0601, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x0601, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x0600:
            goto L_0x064a
        L_0x0601:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
            goto L_0x0600
        L_0x0606:
            java.lang.String r7 = "QCOM"
            java.lang.String r8 = r11.mFingerPrint_Vendor     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equals(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x064a
            java.lang.String r7 = "qbt2000"
            java.lang.String r8 = r11.mFingerPrint_Name     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            boolean r7 = r7.equalsIgnoreCase(r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x064a
            boolean r7 = r11.mIsFactoryBin     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            if (r7 == 0) goto L_0x0634
            com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000 r7 = new com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            java.lang.String r8 = r11.CLASS_NAME     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r7.<init>(r11, r8)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mCommonFingerprint_qbt2000 = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000 r7 = r11.mCommonFingerprint_qbt2000     // Catch:{ Exception -> 0x062f, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.getFpVersion()     // Catch:{ Exception -> 0x062f, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
        L_0x062e:
            goto L_0x064a
        L_0x062f:
            r7 = move-exception
            java.lang.String r8 = "Sensor Init error"
            r2 = r8
            goto L_0x062e
        L_0x0634:
            java.lang.String r7 = "fingerprint"
            java.lang.Object r7 = r11.getSystemService(r7)     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = (android.hardware.fingerprint.FingerprintManager) r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            r11.mFingerprint_combination = r7     // Catch:{ UnsatisfiedLinkError -> 0x0656, Exception -> 0x064b }
            android.hardware.fingerprint.FingerprintManager r7 = r11.mFingerprint_combination     // Catch:{ Exception -> 0x0646, UnsatisfiedLinkError -> 0x0656 }
            java.lang.String r7 = r7.semGetDaemonVersion()     // Catch:{ Exception -> 0x0646, UnsatisfiedLinkError -> 0x0656 }
            r2 = r7
            goto L_0x064a
        L_0x0646:
            r7 = move-exception
            java.lang.String r8 = "Fatal error"
            r2 = r8
        L_0x064a:
            goto L_0x0661
        L_0x064b:
            r7 = move-exception
            java.lang.String r8 = r11.CLASS_NAME
            java.lang.String r9 = "initialize"
            java.lang.String r10 = "exception!"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r8, r9, r10)
            goto L_0x0661
        L_0x0656:
            r7 = move-exception
            java.lang.String r8 = r11.CLASS_NAME
            java.lang.String r9 = "initialize"
            java.lang.String r10 = "library link exception!"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r8, r9, r10)
            goto L_0x064a
        L_0x0661:
            java.lang.String r7 = r11.CLASS_NAME
            java.lang.String r8 = "initialize"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "sdkVer = "
            r9.append(r10)
            r9.append(r2)
            java.lang.String r9 = r9.toString()
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r7, r8, r9)
            android.widget.TextView r7 = r11.mFPVersion
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "Version : "
            r8.append(r9)
            r8.append(r2)
            java.lang.String r8 = r8.toString()
            r7.setText(r8)
        L_0x068f:
            boolean r2 = r11.use_HRM
            if (r2 != r4) goto L_0x0746
            java.lang.String r2 = "HRM"
            java.lang.String r2 = com.sec.xmldata.support.Support.SensorTestMenu.getTestCase(r2)
            java.lang.String r7 = r11.CLASS_NAME
            java.lang.String r8 = "displayValue"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "testcase = "
            r9.append(r10)
            r9.append(r2)
            java.lang.String r9 = r9.toString()
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r7, r8, r9)
            java.lang.String r7 = "EOL_DISABLE"
            boolean r7 = r7.equalsIgnoreCase(r2)
            if (r7 == 0) goto L_0x06be
            android.widget.Button r7 = r11.mHRMSensorEOLTestButton
            r7.setVisibility(r3)
        L_0x06be:
            java.lang.String r7 = "HRM_VENDOR"
            java.lang.String r7 = com.sec.xmldata.support.Support.Kernel.read(r7)
            r11.mHrm_Vendor = r7
            java.lang.String r7 = "HRM_SENSOR_NAME"
            java.lang.String r7 = com.sec.xmldata.support.Support.Kernel.read(r7)
            r11.mHrm_Name = r7
            java.lang.String r7 = "OSRAM"
            java.lang.String r8 = r11.mHrm_Vendor
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x06e5
            java.lang.String r7 = "MAXIM"
            r11.mHrm_Vendor = r7
            java.lang.String r7 = r11.CLASS_NAME
            java.lang.String r8 = "initialize"
            java.lang.String r9 = "Change the HRM vendor name(OSRAM) to MAXIM temporary"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r7, r8, r9)
        L_0x06e5:
            java.lang.String r7 = r11.mHrm_Vendor
            if (r7 == 0) goto L_0x0746
            java.lang.String r7 = r11.mHrm_Vendor
            java.lang.String r8 = "MAXIM"
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x0703
            android.widget.TextView r7 = r11.mHRMSensorLibraryText
            r7.setVisibility(r1)
            android.widget.TextView r7 = r11.mHRMSensorDriverText
            r7.setVisibility(r1)
            android.widget.TextView r7 = r11.mHRMSensorDeivceIdText
            r7.setVisibility(r1)
            goto L_0x0746
        L_0x0703:
            java.lang.String r7 = r11.mHrm_Vendor
            java.lang.String r8 = "PARTRON"
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x0718
            android.widget.TextView r7 = r11.mHRMSensorLibraryText
            r7.setVisibility(r1)
            android.widget.TextView r7 = r11.mHRMSensorDeivceIdText
            r7.setVisibility(r1)
            goto L_0x0746
        L_0x0718:
            java.lang.String r7 = r11.mHrm_Vendor
            java.lang.String r8 = "ADI"
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x0746
            java.lang.String r7 = "ADPD143RI"
            java.lang.String r8 = r11.mHrm_Name
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x0737
            android.widget.TextView r7 = r11.mHRMSensorLibraryText
            r7.setVisibility(r1)
            android.widget.TextView r7 = r11.mHRMSensorDeivceIdText
            r7.setVisibility(r1)
            goto L_0x0741
        L_0x0737:
            android.widget.TextView r7 = r11.mHRMSensorLibraryEOLText
            r7.setVisibility(r1)
            android.widget.TextView r7 = r11.mHRMSensorLibraryELFText
            r7.setVisibility(r1)
        L_0x0741:
            android.widget.TextView r7 = r11.mHRMSensorDriverText
            r7.setVisibility(r1)
        L_0x0746:
            android.widget.Button r2 = r11.mHRMSensorStartButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mHRMSensorEOLTestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mFPTestButton_NormalScan
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mFPTestButton_SNR
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mFPTestButton_SensorInfo
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mFPTestButton_IntCheck
            r2.setOnClickListener(r11)
            r2 = 2131296571(0x7f09013b, float:1.8211062E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mBaromSensorTitleText = r2
            r2 = 2131296572(0x7f09013c, float:1.8211064E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mBaromSeparator = r2
            r2 = 2131296573(0x7f09013d, float:1.8211066E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mBaromSensorSettingAltitudeText = r2
            r2 = 2131296576(0x7f090140, float:1.8211073E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mBaromSensorPressureText = r2
            r2 = 2131296578(0x7f090142, float:1.8211077E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mBaromSensorAltitudeText = r2
            r2 = 2131296577(0x7f090141, float:1.8211075E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mBaromSensorSelftestButton = r2
            r2 = 2131296574(0x7f09013e, float:1.8211069E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mBaromSensorSettingAltitudeButton = r2
            r2 = 2131296575(0x7f09013f, float:1.821107E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.EditText r2 = (android.widget.EditText) r2
            r11.mBaromSensorSettingAltitudeEdit = r2
            android.widget.EditText r2 = r11.mBaromSensorSettingAltitudeEdit
            com.sec.android.app.hwmoduletest.SensorTest$5 r7 = new com.sec.android.app.hwmoduletest.SensorTest$5
            r7.<init>()
            r2.addTextChangedListener(r7)
            java.lang.String r2 = "factory"
            java.lang.String r7 = "BINARY_TYPE"
            java.lang.String r7 = com.sec.xmldata.support.Support.Properties.get(r7)
            boolean r2 = r2.equalsIgnoreCase(r7)
            if (r2 != 0) goto L_0x07ea
            java.lang.String r2 = r11.CLASS_NAME
            java.lang.String r7 = "initialize"
            java.lang.String r8 = "IS_FACTORY_BINARY = user"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r7, r8)
            android.widget.TextView r2 = r11.mBaromSensorSettingAltitudeText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mBaromSensorSettingAltitudeButton
            r2.setVisibility(r3)
            android.widget.EditText r2 = r11.mBaromSensorSettingAltitudeEdit
            r2.setVisibility(r3)
        L_0x07ea:
            boolean r2 = r11.use_Barometer
            if (r2 != 0) goto L_0x0816
            android.widget.TextView r2 = r11.mBaromSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mBaromSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mBaromSensorSettingAltitudeText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mBaromSensorPressureText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mBaromSensorAltitudeText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mBaromSensorSelftestButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mBaromSensorSettingAltitudeButton
            r2.setVisibility(r3)
            android.widget.EditText r2 = r11.mBaromSensorSettingAltitudeEdit
            r2.setVisibility(r3)
        L_0x0816:
            r2 = 2131296579(0x7f090143, float:1.8211079E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mLightSensorTitleText = r2
            r2 = 2131296580(0x7f090144, float:1.821108E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mLightSeparator = r2
            r2 = 2131296581(0x7f090145, float:1.8211083E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mLightSensorLuxText = r2
            r2 = 2131296582(0x7f090146, float:1.8211085E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mLightSensorAdcText = r2
            r2 = 2131296583(0x7f090147, float:1.8211087E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mLightSensorTestButton = r2
            boolean r2 = r11.use_Light_LUX
            if (r2 != 0) goto L_0x086d
            boolean r2 = r11.use_Light_ADC
            if (r2 != 0) goto L_0x086d
            android.widget.TextView r2 = r11.mLightSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mLightSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mLightSensorLuxText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mLightSensorAdcText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mLightSensorTestButton
            r2.setVisibility(r3)
            goto L_0x0888
        L_0x086d:
            boolean r2 = r11.use_Light_LUX
            if (r2 != 0) goto L_0x087b
            boolean r2 = r11.use_Light_ADC
            if (r2 == 0) goto L_0x087b
            android.widget.TextView r2 = r11.mLightSensorLuxText
            r2.setVisibility(r3)
            goto L_0x0888
        L_0x087b:
            boolean r2 = r11.use_Light_LUX
            if (r2 == 0) goto L_0x0888
            boolean r2 = r11.use_Light_ADC
            if (r2 != 0) goto L_0x0888
            android.widget.TextView r2 = r11.mLightSensorAdcText
            r2.setVisibility(r3)
        L_0x0888:
            boolean r2 = r11.isReadFromManager()
            r11.mIsReadFromManager = r2
            r2 = 2131296380(0x7f09007c, float:1.8210675E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mGyroSensorTitleText = r2
            r2 = 2131296585(0x7f090149, float:1.821109E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mGyroSeparator = r2
            r2 = 2131296586(0x7f09014a, float:1.8211093E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mGyroSensorValueText = r2
            r2 = 2131296587(0x7f09014b, float:1.8211095E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mGyroSensorSelftestButton = r2
            r2 = 2131296588(0x7f09014c, float:1.8211097E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mGyroSensorDisplayButton = r2
            r2 = 2131296589(0x7f09014d, float:1.8211099E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mGyroSensorGraphButton = r2
            r2 = 2131296590(0x7f09014e, float:1.82111E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mSubGyroSensorTitleText = r2
            r2 = 2131296591(0x7f09014f, float:1.8211103E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mSubGyroSeparator = r2
            r2 = 2131296592(0x7f090150, float:1.8211105E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mSubGyroSensorValueText = r2
            r2 = 2131296593(0x7f090151, float:1.8211107E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mSubGyroSensorSelftestButton = r2
            r2 = 2131296594(0x7f090152, float:1.821111E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mSubGyroSensorDisplayButton = r2
            r2 = 2131296595(0x7f090153, float:1.8211111E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mSubGyroSensorGraphButton = r2
            boolean r2 = r11.use_Gyroscope
            if (r2 != 0) goto L_0x0930
            android.widget.TextView r2 = r11.mGyroSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mGyroSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mGyroSensorValueText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mGyroSensorSelftestButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mGyroSensorDisplayButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mGyroSensorGraphButton
            r2.setVisibility(r3)
        L_0x0930:
            boolean r2 = r11.use_Sub_Gyroscope
            if (r2 != 0) goto L_0x0952
            android.widget.TextView r2 = r11.mSubGyroSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mSubGyroSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mSubGyroSensorValueText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mSubGyroSensorSelftestButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mSubGyroSensorDisplayButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mSubGyroSensorGraphButton
            r2.setVisibility(r3)
        L_0x0952:
            r2 = 2131296596(0x7f090154, float:1.8211113E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mOISGyroSensorSub = r2
            r2 = 2131296597(0x7f090155, float:1.8211115E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mOISGyroSelftestButton = r2
            boolean r2 = r11.use_OIS_Gyro
            if (r2 == 0) goto L_0x097a
            boolean r2 = r11.use_Gyroscope
            if (r2 != 0) goto L_0x097a
            android.widget.TextView r2 = r11.mGyroSensorTitleText
            r2.setVisibility(r1)
            android.view.View r2 = r11.mGyroSeparator
            r2.setVisibility(r1)
        L_0x097a:
            boolean r2 = r11.use_OIS_Gyro
            if (r2 != 0) goto L_0x0988
            android.widget.TextView r2 = r11.mOISGyroSensorSub
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mOISGyroSelftestButton
            r2.setVisibility(r3)
        L_0x0988:
            r2 = 2131296602(0x7f09015a, float:1.8211125E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mMagnSensorTitleText = r2
            r2 = 2131296603(0x7f09015b, float:1.8211127E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mMagnSeparator = r2
            r2 = 2131296604(0x7f09015c, float:1.821113E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mMagnSensorValueText = r2
            r2 = 2131296609(0x7f090161, float:1.821114E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mMagnSensorPowerNoiseTestButton = r2
            r2 = 2131296608(0x7f090160, float:1.8211137E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.Button r2 = (android.widget.Button) r2
            r11.mMagnSensorSelfTestButton = r2
            r2 = 2131296605(0x7f09015d, float:1.8211131E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mOrieSensorAzimuthText = r2
            r2 = 2131296606(0x7f09015e, float:1.8211133E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mOrieSensorPitchText = r2
            r2 = 2131296607(0x7f09015f, float:1.8211135E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mOrieSensorRollText = r2
            r2 = 2131296610(0x7f090162, float:1.8211142E38)
            android.view.View r2 = r11.findViewById(r2)
            com.sec.android.app.hwmoduletest.view.SensorArrow r2 = (com.sec.android.app.hwmoduletest.view.SensorArrow) r2
            r11.mSensorArrow = r2
            com.sec.android.app.hwmoduletest.view.SensorArrow r2 = r11.mSensorArrow
            r7 = 1110704128(0x42340000, float:45.0)
            r2.setDirection(r7)
            boolean r2 = r11.use_Magnetic
            if (r2 != 0) goto L_0x0a21
            android.widget.TextView r2 = r11.mMagnSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mMagnSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mMagnSensorValueText
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mMagnSensorPowerNoiseTestButton
            r2.setVisibility(r3)
            android.widget.Button r2 = r11.mMagnSensorSelfTestButton
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mOrieSensorAzimuthText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mOrieSensorPitchText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mOrieSensorRollText
            r2.setVisibility(r3)
            com.sec.android.app.hwmoduletest.view.SensorArrow r2 = r11.mSensorArrow
            r2.setVisibility(r3)
        L_0x0a21:
            r2 = 2131296598(0x7f090156, float:1.8211117E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mUltraSensorTitleText = r2
            r2 = 2131296599(0x7f090157, float:1.821112E38)
            android.view.View r2 = r11.findViewById(r2)
            r11.mUltraSeparator = r2
            r2 = 2131296600(0x7f090158, float:1.8211121E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mUltraSensorCompanyFWValueText = r2
            r2 = 2131296601(0x7f090159, float:1.8211123E38)
            android.view.View r2 = r11.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            r11.mUltraSensorAdcDistanceValueText = r2
            boolean r2 = r11.use_Ultrasonic
            if (r2 != 0) goto L_0x0a63
            android.widget.TextView r2 = r11.mUltraSensorTitleText
            r2.setVisibility(r3)
            android.view.View r2 = r11.mUltraSeparator
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mUltraSensorCompanyFWValueText
            r2.setVisibility(r3)
            android.widget.TextView r2 = r11.mUltraSensorAdcDistanceValueText
            r2.setVisibility(r3)
        L_0x0a63:
            boolean r2 = r11.use_Magnetic_PowerNoise
            if (r2 != 0) goto L_0x0a6c
            android.widget.Button r2 = r11.mMagnSensorPowerNoiseTestButton
            r2.setVisibility(r3)
        L_0x0a6c:
            java.lang.String r2 = "vibrator"
            java.lang.Object r2 = r11.getSystemService(r2)
            android.os.Vibrator r2 = (android.os.Vibrator) r2
            r11.mVibrator = r2
            java.lang.String r2 = "sensor"
            java.lang.Object r2 = r11.getSystemService(r2)
            android.hardware.SensorManager r2 = (android.hardware.SensorManager) r2
            r11.mSensorManager = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            android.hardware.Sensor r2 = r2.getDefaultSensor(r4)
            r11.mAccelerometerSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r4 = 65687(0x10097, float:9.2047E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r4)
            r11.mSubAccelerometerSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r4 = 65592(0x10038, float:9.1914E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r4)
            r11.mProximitySensor = r2
            android.hardware.Sensor r2 = r11.mProximitySensor
            if (r2 != 0) goto L_0x0aaa
            android.hardware.SensorManager r2 = r11.mSensorManager
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mProximitySensor = r2
        L_0x0aaa:
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 65582(0x1002e, float:9.19E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mIrisProximitySensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            android.hardware.Sensor r2 = r2.getDefaultSensor(r5)
            r11.mBarometerSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            android.hardware.Sensor r2 = r2.getDefaultSensor(r6)
            r11.mLightSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 4
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mGyroscopeSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 65689(0x10099, float:9.205E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mSubGyroscopeSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 2
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mMagneticSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 3
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mOrientationSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 65561(0x10019, float:9.187E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mBIOSensor = r2
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 65562(0x1001a, float:9.1872E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mHRMSensor = r2
            boolean r2 = r11.use_Flicker
            if (r2 == 0) goto L_0x0b10
            android.hardware.SensorManager r2 = r11.mSensorManager
            r3 = 65577(0x10029, float:9.1893E-41)
            android.hardware.Sensor r2 = r2.getDefaultSensor(r3)
            r11.mFlickerSensor = r2
        L_0x0b10:
            android.widget.Button r2 = r11.mAcceImageTestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mAcceGraphButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mSubAcceGraphButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mBaromSensorSettingAltitudeButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mBaromSensorSettingAltitudeButton
            r2.setClickable(r1)
            android.widget.Button r2 = r11.mBaromSensorSelftestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mLightSensorTestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mGyroSensorSelftestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mGyroSensorDisplayButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mGyroSensorGraphButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mSubGyroSensorSelftestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mSubGyroSensorDisplayButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mSubGyroSensorGraphButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mOISGyroSelftestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mMagnSensorPowerNoiseTestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mMagnSensorSelfTestButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mHRMSensorStartButton
            r2.setOnClickListener(r11)
            android.widget.Button r2 = r11.mHRMSensorEOLTestButton
            r2.setOnClickListener(r11)
            r2 = 9
            float r2 = com.sec.xmldata.support.Support.HwTestMenu.getUIRate(r2)
            r3 = 0
            int r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r3 == 0) goto L_0x0e6f
            r3 = 1065353216(0x3f800000, float:1.0)
            int r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r3 == 0) goto L_0x0e6f
            android.widget.TextView r3 = r11.mAcceSensorTitleText
            android.widget.TextView r4 = r11.mAcceSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mAcceSensorValueText
            android.widget.TextView r4 = r11.mAcceSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mSubAcceSensorValueText
            android.widget.TextView r4 = r11.mSubAcceSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mAcceImageTestButton
            android.widget.Button r4 = r11.mAcceImageTestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mAcceGraphButton
            android.widget.Button r4 = r11.mAcceGraphButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mSubAcceGraphButton
            android.widget.Button r4 = r11.mSubAcceGraphButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mCpAcceSensorTitleText
            android.widget.TextView r4 = r11.mCpAcceSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mCpAcceSensorValueText
            android.widget.TextView r4 = r11.mCpAcceSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mIrisProxSensorTitleText
            android.widget.TextView r4 = r11.mIrisProxSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mIrisProxSensorStatusText
            android.widget.TextView r4 = r11.mIrisProxSensorStatusText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorTitleText
            android.widget.TextView r4 = r11.mProxSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorStatusText
            android.widget.TextView r4 = r11.mProxSensorStatusText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorAdcAvgText
            android.widget.TextView r4 = r11.mProxSensorAdcAvgText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorTspColorIdText
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorDefaultTrimText
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorOffsetText
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorHighThresholdText
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorLowThresholdText
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mProxSensorDeviceID
            android.widget.TextView r4 = r11.mProxSensorTspColorIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mFlickerSensorText
            android.widget.TextView r4 = r11.mFlickerSensorText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorTitleText
            android.widget.TextView r4 = r11.mHRMSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mHRMSensorStartButton
            android.widget.Button r4 = r11.mHRMSensorStartButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mHRMSensorEOLTestButton
            android.widget.Button r4 = r11.mHRMSensorEOLTestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorLibraryText
            android.widget.TextView r4 = r11.mHRMSensorLibraryText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorDriverText
            android.widget.TextView r4 = r11.mHRMSensorDriverText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorDeivceIdText
            android.widget.TextView r4 = r11.mHRMSensorDeivceIdText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorLibraryEOLText
            android.widget.TextView r4 = r11.mHRMSensorLibraryEOLText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mHRMSensorLibraryELFText
            android.widget.TextView r4 = r11.mHRMSensorLibraryELFText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mFPTitleText
            android.widget.TextView r4 = r11.mFPTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mFPTestButton_NormalScan
            android.widget.Button r4 = r11.mFPTestButton_NormalScan
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mFPTestButton_SNR
            android.widget.Button r4 = r11.mFPTestButton_SNR
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mFPTestButton_SensorInfo
            android.widget.Button r4 = r11.mFPTestButton_SensorInfo
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mFPTestButton_IntCheck
            android.widget.Button r4 = r11.mFPTestButton_IntCheck
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mFPVersion
            android.widget.TextView r4 = r11.mFPVersion
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mBaromSensorTitleText
            android.widget.TextView r4 = r11.mBaromSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mBaromSensorSettingAltitudeText
            android.widget.TextView r4 = r11.mBaromSensorSettingAltitudeText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mBaromSensorPressureText
            android.widget.TextView r4 = r11.mBaromSensorPressureText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mBaromSensorAltitudeText
            android.widget.TextView r4 = r11.mBaromSensorAltitudeText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mBaromSensorSettingAltitudeButton
            android.widget.Button r4 = r11.mBaromSensorSettingAltitudeButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mBaromSensorSelftestButton
            android.widget.Button r4 = r11.mBaromSensorSelftestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mLightSensorTitleText
            android.widget.TextView r4 = r11.mLightSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mLightSensorLuxText
            android.widget.TextView r4 = r11.mLightSensorLuxText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mLightSensorAdcText
            android.widget.TextView r4 = r11.mLightSensorAdcText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mLightSensorTestButton
            android.widget.Button r4 = r11.mLightSensorTestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mGyroSensorTitleText
            android.widget.TextView r4 = r11.mGyroSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mGyroSensorValueText
            android.widget.TextView r4 = r11.mGyroSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mGyroSensorSelftestButton
            android.widget.Button r4 = r11.mGyroSensorSelftestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mGyroSensorDisplayButton
            android.widget.Button r4 = r11.mGyroSensorDisplayButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mGyroSensorGraphButton
            android.widget.Button r4 = r11.mGyroSensorGraphButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mSubGyroSensorTitleText
            android.widget.TextView r4 = r11.mSubGyroSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mSubGyroSensorValueText
            android.widget.TextView r4 = r11.mSubGyroSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mSubGyroSensorSelftestButton
            android.widget.Button r4 = r11.mSubGyroSensorSelftestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mSubGyroSensorDisplayButton
            android.widget.Button r4 = r11.mSubGyroSensorDisplayButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mSubGyroSensorGraphButton
            android.widget.Button r4 = r11.mSubGyroSensorGraphButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mMagnSensorTitleText
            android.widget.TextView r4 = r11.mMagnSensorTitleText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mMagnSensorValueText
            android.widget.TextView r4 = r11.mMagnSensorValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mMagnSensorPowerNoiseTestButton
            android.widget.Button r4 = r11.mMagnSensorPowerNoiseTestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.Button r3 = r11.mMagnSensorSelfTestButton
            android.widget.Button r4 = r11.mMagnSensorSelfTestButton
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mOrieSensorAzimuthText
            android.widget.TextView r4 = r11.mOrieSensorAzimuthText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mOrieSensorPitchText
            android.widget.TextView r4 = r11.mOrieSensorPitchText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mOrieSensorRollText
            android.widget.TextView r4 = r11.mOrieSensorRollText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mUltraSensorCompanyFWValueText
            android.widget.TextView r4 = r11.mUltraSensorCompanyFWValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
            android.widget.TextView r3 = r11.mUltraSensorAdcDistanceValueText
            android.widget.TextView r4 = r11.mUltraSensorAdcDistanceValueText
            float r4 = r4.getTextSize()
            float r4 = r4 * r2
            r3.setTextSize(r1, r4)
        L_0x0e6f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.SensorTest.initialize():void");
    }

    public void showProxOffset() {
        if (this.use_TspProximity) {
            TextView textView = this.mProxSensorHighThresholdText;
            StringBuilder sb = new StringBuilder();
            sb.append("High THD: ");
            sb.append(Integer.toString(Spec.getInt(Spec.TSP_PROX_ADC_WORKING_SPEC)));
            textView.setText(sb.toString());
            TextView textView2 = this.mProxSensorLowThresholdText;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Low THD: ");
            sb2.append(Integer.toString(Spec.getInt(Spec.TSP_PROX_ADC_RELEASE_SPEC)));
            textView2.setText(sb2.toString());
            return;
        }
        if (this.mIsProxAutoCal) {
            String mTHDHighValue = Kernel.read(Kernel.PROXI_SENSOR_SET_HIGH_THRESHOLD);
            String mTHDLowValue = Kernel.read(Kernel.PROXI_SENSOR_SET_LOW_THRESHOLD);
            if (mTHDHighValue == null || mTHDLowValue == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("1st THD high/low : ");
                sb3.append(mTHDHighValue);
                sb3.append(" / ");
                sb3.append(mTHDLowValue);
                LtUtil.log_i(this.CLASS_NAME, "showProxOffset", sb3.toString());
            } else {
                TextView textView3 = this.mProxSensor1stThresholdText;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("1st High/Low THD: ");
                sb4.append(mTHDHighValue);
                sb4.append(" / ");
                sb4.append(mTHDLowValue);
                textView3.setText(sb4.toString());
            }
            String mTHDHighValue2 = Kernel.read(Kernel.PROXI_SENSOR_DETECT_HIGH_THRESHOLD);
            String mTHDLowValue2 = Kernel.read(Kernel.PROXI_SENSOR_DETECT_LOW_THRESHOLD);
            if (mTHDHighValue2 == null || mTHDLowValue2 == null) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("2nd THD high/low : ");
                sb5.append(mTHDHighValue2);
                sb5.append(" / ");
                sb5.append(mTHDLowValue2);
                LtUtil.log_i(this.CLASS_NAME, "showProxOffset", sb5.toString());
            } else {
                TextView textView4 = this.mProxSensor2ndThresholdText;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("2nd High/Low THD: ");
                sb6.append(mTHDHighValue2);
                sb6.append(" / ");
                sb6.append(mTHDLowValue2);
                textView4.setText(sb6.toString());
            }
        } else {
            String[] OffsetValue = readOffset().split(",");
            if (OffsetValue.length > 2) {
                TextView textView5 = this.mProxSensorOffsetText;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Offset: ");
                sb7.append(OffsetValue[0]);
                textView5.setText(sb7.toString());
                TextView textView6 = this.mProxSensorHighThresholdText;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("High THD: ");
                sb8.append(OffsetValue[1]);
                textView6.setText(sb8.toString());
                TextView textView7 = this.mProxSensorLowThresholdText;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Low THD: ");
                sb9.append(OffsetValue[2]);
                textView7.setText(sb9.toString());
            } else {
                StringBuilder sb10 = new StringBuilder();
                sb10.append("readOffset: ");
                sb10.append(readOffset());
                LtUtil.log_i(this.CLASS_NAME, "showProxOffset", sb10.toString());
            }
        }
        if (this.mAutoOffsetTask == null) {
            this.mAutoOffsetTask = new AutoOffsetTask();
            LtUtil.log_i(this.CLASS_NAME, "showProxOffset", "new AutoOffset Task");
        }
        if (this.offsetTimer == null) {
            this.offsetTimer = new Timer("OffsetTimer");
            LtUtil.log_i(this.CLASS_NAME, "showProxOffset", "new AutoOffset Timer");
        }
        this.offsetTimer.schedule(this.mAutoOffsetTask, 0, 1000);
    }

    public void showProxDeviceID() {
        String sensorName = Kernel.read(Kernel.PROXI_SENSOR_NAME);
        if (sensorName == null || sensorName.isEmpty()) {
            LtUtil.log_e(this.CLASS_NAME, "showProxDeviceID", "No sensor name, Please check .../name node");
        }
        TextView textView = this.mProxSensorDeviceID;
        StringBuilder sb = new StringBuilder();
        sb.append("Device ID : ");
        sb.append(sensorName);
        textView.setText(sb.toString());
    }

    public String readOffset() {
        String data = Kernel.read(Kernel.PROXI_SENSOR_OFFSET);
        if (data == null) {
            return "NONE";
        }
        String data2 = data.trim();
        StringBuilder sb = new StringBuilder();
        sb.append("Offset: ");
        sb.append(data2);
        LtUtil.log_i(this.CLASS_NAME, "readOffset", sb.toString());
        return data2;
    }

    private boolean isRgbSensorSupported() {
        String hwrevision = Properties.get(Properties.HW_REVISION);
        if (hwrevision == null) {
            return true;
        }
        String[] hwrevString = Spec.getString(Spec.NO_RGBSENSOR_SUPPORT_REV).split(",");
        for (String equals : hwrevString) {
            if ("-1".equals(hwrevString[0]) || hwrevision.equals(equals)) {
                return false;
            }
        }
        return true;
    }

    private void checkValidSysfPathLightSensor() {
        LtUtil.log_d(this.CLASS_NAME, "checkFile", null);
        if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_RAW)) {
            this.mLightSensorSysfsPath = Kernel.LIGHT_SENSOR_RAW;
        } else if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_LUX)) {
            this.mLightSensorSysfsPath = Kernel.LIGHT_SENSOR_LUX;
        } else if (Kernel.isExistFile(Kernel.LIGHT_SENSOR_ADC)) {
            this.mLightSensorSysfsPath = Kernel.LIGHT_SENSOR_ADC;
        } else {
            LtUtil.log_e(this.CLASS_NAME, "checkFile", "no read target");
            this.mLightSensorSysfsPath = null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("read target : ");
        sb.append(this.mLightSensorSysfsPath);
        LtUtil.log_d(this.CLASS_NAME, "checkFile", sb.toString());
    }

    private void startMagneticSelfTest() {
        String feature = ModuleSensor.instance(this).mFeature_Magnetic;
        StringBuilder sb = new StringBuilder();
        sb.append("Start Magnetic Sensor - feature : ");
        sb.append(feature);
        LtUtil.log_d(this.CLASS_NAME, "startMagnetic", sb.toString());
        if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8973) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8975)) {
            startActivity(new Intent(this, MagneticAsahi.class));
        } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS529) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530C)) {
            startActivity(new Intent(this, MagneticYamaha.class));
        } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD004) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD004A) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD006A) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_HSCDTD008A)) {
            startActivity(new Intent(this, MagneticAlps.class));
        } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
            startActivity(new Intent(this, MagneticSTMicro.class));
        } else if (feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE) || feature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION)) {
            startActivity(new Intent(this, MagneticBosch.class));
        }
    }

    private void startGyroscopeSelfTest() {
        String feature = ModuleSensor.instance(this).mFeature_Gyroscope;
        StringBuilder sb = new StringBuilder();
        sb.append("Start Gyroscope Sensor - feature : ");
        sb.append(feature);
        LtUtil.log_d(this.CLASS_NAME, "startGyroscopeSelfTest", sb.toString());
        if (feature.equals("#DEFAULT")) {
            LtUtil.log_d(this.CLASS_NAME, "startGyroscopeSelfTest", "Gyroscope Standard concept");
            startActivity(new Intent(this, GyroscopeStandard.class));
        }
        if (feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6050) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6051) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6500) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6051M) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6515M) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6515)) {
            startActivity(new Intent(this, GyroscopeInvensense.class));
        } else if (feature.contains(ModuleSensor.FEATURE_GYROSCOP_STMICRO_SMARTPHONE)) {
            startActivity(new Intent(this, GyroscopeIcSTMicro.class));
        } else if (feature.equals(ModuleSensor.FEATURE_GYROSCOP_STMICRO_TABLET)) {
            startActivity(new Intent(this, GyroscopeIcSTMicroTablet.class));
        } else if (feature.contains(ModuleSensor.FEATURE_GYROSCOP_BOSCH)) {
            Intent intent = new Intent(this, GyroscopeBosch.class);
            try {
                intent.putExtra("subtype", feature.split("_")[1]);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("sub type : ");
                sb2.append(feature.split("_")[1]);
                LtUtil.log_d(this.CLASS_NAME, "GyroscopeBOSCH", sb2.toString());
            } catch (IndexOutOfBoundsException e) {
                LtUtil.log_d(this.CLASS_NAME, "GyroscopeBOSCH", "cannot parse sub type");
            } catch (Throwable th) {
                startActivity(intent);
                throw th;
            }
            startActivity(intent);
        } else if (feature.equals(ModuleSensor.FEATURE_GYROSCOP_MAXIM)) {
            startActivity(new Intent(this, GyroscopeMaxim.class));
        }
    }

    private void registerCpAccelermeterReceiver() {
        IntentFilter CpAccelermeterData = new IntentFilter();
        CpAccelermeterData.addAction("com.sec.android.app.factorytest");
        registerReceiver(this.mBroadcastReceiver, CpAccelermeterData);
    }

    private void registerOqcsbfttReceiver() {
        IntentFilter OqcsbfttReadData = new IntentFilter();
        OqcsbfttReadData.addAction(ACTION_OQCSBFTT_READ_DATA);
        registerReceiver(this.mBroadcastReceiver, OqcsbfttReadData);
    }

    private void controlCPsAccelerometer(int control) {
        if (this.mFactoryPhone == null) {
            this.mFactoryPhone = new FactoryTestPhone(this);
            this.mFactoryPhone.bindSecPhoneService();
        }
        switch (control) {
            case 0:
                LtUtil.log_d(this.CLASS_NAME, "controlCPsAccelerometer", "TEST_____ controlCPsAccelerometer : CP_ACCEL_POWER_OFF _____TEST");
                this.mFactoryPhone.sendToRilCpAccelermeter(new byte[]{2, 0, HwModuleTest.ID_SUB_KEY, 0});
                return;
            case 1:
                LtUtil.log_d(this.CLASS_NAME, "controlCPsAccelerometer", "TEST_____ controlCPsAccelerometer : CP_ACCEL_POWER_ON _____TEST");
                this.mFactoryPhone.sendToRilCpAccelermeter(new byte[]{2, 0, HwModuleTest.ID_SUB_KEY, 1});
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void catchCPsAccelerometerData(String cmdData) {
        String str = cmdData;
        LtUtil.log_d(this.CLASS_NAME, "catchCPsAccelerometerData", "TEST-SENSORTEST_____ catchCPsAccelerometerData()_____TEST-SENSORTEST");
        StringBuilder sb = new StringBuilder();
        sb.append("TEST-SENSORTEST_____ cmdData=[");
        sb.append(str);
        sb.append("]_____TEST-SENSORTEST");
        LtUtil.log_d(this.CLASS_NAME, "catchCPsAccelermeteroData", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str.substring(8, 10));
        sb2.append(str.substring(6, 8));
        String xData = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str.substring(12, 14));
        sb3.append(str.substring(10, 12));
        String yData = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str.substring(16, 18));
        sb4.append(str.substring(14, 16));
        String zData = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("TEST-SENSORTEST_____ xData=[");
        sb5.append(xData);
        sb5.append("],yData=[");
        sb5.append(yData);
        sb5.append("],zData=[");
        sb5.append(zData);
        sb5.append("]_____TEST-SENSORTEST");
        LtUtil.log_d(this.CLASS_NAME, "catchCPsAccelerometerData", sb5.toString());
        int x = (short) Integer.parseInt(xData, 16);
        int y = (short) Integer.parseInt(yData, 16);
        int z = (short) Integer.parseInt(zData, 16);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("TEST-SENSORTEST_____ x=[");
        sb6.append(x);
        sb6.append("],y=[");
        sb6.append(y);
        sb6.append("],z=[");
        sb6.append(z);
        sb6.append("]_____TEST-SENSORTEST");
        LtUtil.log_d(this.CLASS_NAME, "catchCPsAccelerometerData", sb6.toString());
        String tempStr = String.format("CP ACCELER: X:%d, Y:%d, Z:%d \n", new Object[]{Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
        int[] angel = new int[3];
        float realg = (float) Math.sqrt((double) ((x * x) + (y * y) + (z * z)));
        angel[0] = (int) (((float) Math.asin((double) (((float) x) / realg))) * 57.29578f);
        int[] angel2 = angel;
        angel2[1] = (int) (((float) Math.asin((double) (((float) y) / realg))) * 57.29578f);
        angel2[2] = ((int) (((float) Math.acos((double) (((float) z) / realg))) * 57.29578f)) - 90;
        angel2[2] = angel2[2] * -1;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(tempStr);
        sb7.append(String.format("CP X-angle: %d, Y-angle: %d, Z-angle: %d ", new Object[]{Integer.valueOf(angel2[0]), Integer.valueOf(angel2[1]), Integer.valueOf(angel2[2])}));
        this.mCpAcceSensorValueText.setText(sb7.toString());
    }

    private String getUltrasonicSensorVerValueString() {
        String[] ultraVerData = null;
        try {
            ultraVerData = Kernel.read(Kernel.ULTRASONIC_VER_CHECK, 1).split(",");
            if (!"0".equals(ultraVerData[0]) || !"0".equals(ultraVerData[1])) {
                this.bReadUltrasonicData = true;
            } else {
                this.bReadUltrasonicData = false;
            }
        } catch (Exception e) {
            LtUtil.log_d(this.CLASS_NAME, "getUltrasonicSensorVerValueString", "Exception accessing ultrasonic file");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ID : ");
        sb.append(ultraVerData[0]);
        sb.append(" F/W  Version : ");
        sb.append(ultraVerData[1]);
        return sb.toString();
    }

    private void readToOisGyroSensor() {
        try {
            String RawdataResult = Kernel.read(Kernel.GYRO_OIS_RAWDATA);
            if (RawdataResult != null) {
                String[] RawData = RawdataResult.split(",");
                TextView textView = this.mOISGyroSensorSub;
                StringBuilder sb = new StringBuilder();
                sb.append("OIS GYRO : X: ");
                sb.append(RawData[0]);
                sb.append(" Y: ");
                sb.append(RawData[1]);
                textView.setText(sb.toString());
                return;
            }
            this.mOISGyroSensorSub.setText("OIS GYRO : X: null Y: null");
        } catch (ArrayIndexOutOfBoundsException e) {
            LtUtil.log_d(this.CLASS_NAME, "readToOisGyroSensor", "ArrayIndexOutOfBoundsException - OIS Gyro Sensor");
        }
    }
}
