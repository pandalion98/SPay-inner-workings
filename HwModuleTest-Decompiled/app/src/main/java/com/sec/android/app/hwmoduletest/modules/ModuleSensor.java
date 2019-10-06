package com.sec.android.app.hwmoduletest.modules;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import com.sec.android.app.hwmoduletest.modules.SensorReadManager.GyroExpansionData;
import com.sec.android.app.hwmoduletest.modules.SensorReadManager.MagneticExpansionData;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.TestCase;
import egis.optical.client.api.EgisFingerprint;
import java.text.DecimalFormat;

public class ModuleSensor extends ModuleObject {
    private static int DUMMY = 0;
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMA022 = "BMA022";
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMA023 = "BMA023";
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMA220 = "BMA220";
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMA222 = "BMA222";
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMA250 = "BMA250";
    public static final String FEATURE_ACCELEROMETER_BOSCH_BMI168 = "BM168";
    public static final String FEATURE_ACCELEROMETER_BOSCH_SMB380 = "SMB380";
    public static final String FEATURE_ACCELEROMETER_INVENSENSE_MPU6050 = "MPU6050";
    public static final String FEATURE_ACCELEROMETER_INVENSENSE_MPU6051 = "MPU6051";
    public static final String FEATURE_ACCELEROMETER_INVENSENSE_MPU6515M = "MPU6515M";
    public static final String FEATURE_ACCELEROMETER_KIONIX_KXTF9 = "KXTF9";
    public static final String FEATURE_ACCELEROMETER_KIONIX_KXUD9 = "KXUD9";
    public static final String FEATURE_ACCELEROMETER_STMICRO_K3DH = "K3DH";
    public static final String FEATURE_ACCELEROMETER_STMICRO_K6DS3 = "K6DS3";
    public static final String FEATURE_ACCELEROMETER_STMICRO_KR3DH = "KR3DH";
    public static final String FEATURE_ACCELEROMETER_STMICRO_KR3DM = "KR3DM";
    public static final String FEATURE_ACCELEROMETER_STMICRO_LSM330DLC = "LSM330DLC";
    public static final String FEATURE_GESTURE_MAX88922 = "MAX88922";
    public static final String FEATURE_GESTURE_TMG3992 = "TMG3992";
    public static final String FEATURE_GYROSCOP_BOSCH = "BOSCH";
    public static final String FEATURE_GYROSCOP_INVENSENSE = "INVENSENSE";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6050 = "INVENSENSE_MPU6050";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6051 = "INVENSENSE_MPU6051";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6051M = "INVENSENSE_MPU6051M";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6500 = "INVENSENSE_MPU6500";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6515 = "INVENSENSE_MPU6515";
    public static final String FEATURE_GYROSCOP_INVENSENSE_MPU6515M = "INVENSENSE_MPU6515M";
    public static final String FEATURE_GYROSCOP_MAXIM = "MAXIM";
    public static final String FEATURE_GYROSCOP_STMICRO_SMARTPHONE = "STMICRO_SMARTPHONE";
    public static final String FEATURE_GYROSCOP_STMICRO_TABLET = "STMICRO_TABLET";
    public static final String FEATURE_MAGENTIC_AK09911 = "AK09911";
    public static final String FEATURE_MAGENTIC_AK09911C = "AK09911C";
    public static final String FEATURE_MAGENTIC_AK09916C = "AK09916C";
    public static final String FEATURE_MAGENTIC_AK09918C = "AK09918C";
    public static final String FEATURE_MAGENTIC_AK8963 = "AK8963";
    public static final String FEATURE_MAGENTIC_AK8963C = "AK8963C";
    public static final String FEATURE_MAGENTIC_AK8963C_MANAGER = "AK8963C_MANAGER";
    public static final String FEATURE_MAGENTIC_AK8973 = "AK8973";
    public static final String FEATURE_MAGENTIC_AK8975 = "AK8975";
    public static final String FEATURE_MAGENTIC_BMC150 = "BOSCH_BMC150";
    public static final String FEATURE_MAGENTIC_BMC150_COMBINATION = "BMC150_COMBINATION";
    public static final String FEATURE_MAGENTIC_BMC150_NEWEST = "BMC150_NEWEST";
    public static final String FEATURE_MAGENTIC_BMC150_POWER_NOISE = "BOSCH_BMC150_POWER_NOISE";
    public static final String FEATURE_MAGENTIC_HSCDTD004 = "HSCDTD004";
    public static final String FEATURE_MAGENTIC_HSCDTD004A = "HSCDTD004A";
    public static final String FEATURE_MAGENTIC_HSCDTD006A = "HSCDTD006A";
    public static final String FEATURE_MAGENTIC_HSCDTD008A = "HSCDTD008A";
    public static final String FEATURE_MAGENTIC_STMICRO = "STMICRO_K303C";
    public static final String FEATURE_MAGENTIC_STMICRO_LSM303AH = "STMICRO_LSM303AH";
    public static final String FEATURE_MAGENTIC_YAS529 = "YAS529";
    public static final String FEATURE_MAGENTIC_YAS530 = "YAS530";
    public static final String FEATURE_MAGENTIC_YAS530A = "YAS530A";
    public static final String FEATURE_MAGENTIC_YAS530C = "YAS530C";
    public static final String FEATURE_MAGENTIC_YAS532 = "YAS532";
    public static final String FEATURE_MAGENTIC_YAS532B = "YAS532B";
    public static final String FEATURE_MAGENTIC_YAS537 = "YAS537";
    public static final String FEATURE_MAGENTIC_YAS539 = "YAS539";
    public static final int GRIP_STATUS_DETECT = 1;
    public static final int GRIP_STATUS_RELEASE = 0;
    public static final int ID_FILE____ACCELEROMETER;
    public static final int ID_FILE____ACCELEROMETER_CAL;
    public static final int ID_FILE____ACCELEROMETER_INTPIN;
    public static final int ID_FILE____ACCELEROMETER_N_ANGLE;
    public static final int ID_FILE____ACCELEROMETER_SELF;
    public static final int ID_FILE____BAROMETER_EEPROM;
    public static final int ID_FILE____COUNT = (DUMMY - ID_MANAGER_COUNT);
    public static final int ID_FILE____GYRO_SELFTEST;
    public static final int ID_FILE____GYRO_TEMPERATURE;
    public static final int ID_FILE____LIGHT;
    public static final int ID_FILE____LIGHT_ADC;
    public static final int ID_FILE____LIGHT_CCT;
    public static final int ID_FILE____MAGNETIC_ADC;
    public static final int ID_FILE____MAGNETIC_DAC;
    public static final int ID_FILE____MAGNETIC_POWER_OFF;
    public static final int ID_FILE____MAGNETIC_POWER_ON;
    public static final int ID_FILE____MAGNETIC_SELF;
    public static final int ID_FILE____MAGNETIC_STATUS;
    public static final int ID_FILE____MAGNETIC_TEMPERATURE;
    public static final int ID_FILE____PROXIMITY_ADC;
    public static final int ID_FILE____PROXIMITY_AVG;
    public static final int ID_FILE____PROXIMITY_OFFSET;
    public static final int ID_INTENT__COUNT = (DUMMY - (ID_MANAGER_COUNT + ID_FILE____COUNT));
    public static final int ID_INTENT__CP_ACCELEROMETER;
    public static final int ID_INTENT__GRIP;
    public static final int ID_MANAGER_ACCELEROMETER;
    public static final int ID_MANAGER_ACCELEROMETER_N_ANGLE;
    public static final int ID_MANAGER_ACCELEROMETER_SELF;
    public static final int ID_MANAGER_BAROMETER;
    public static final int ID_MANAGER_COUNT = DUMMY;
    public static final int ID_MANAGER_GYRO;
    public static final int ID_MANAGER_GYRO_EXPANSION;
    public static final int ID_MANAGER_GYRO_POWER;
    public static final int ID_MANAGER_GYRO_SELF;
    public static final int ID_MANAGER_GYRO_TEMPERATURE;
    public static final int ID_MANAGER_LIGHT;
    public static final int ID_MANAGER_MAGNETIC;
    public static final int ID_MANAGER_MAGNETIC_ADC;
    public static final int ID_MANAGER_MAGNETIC_DAC;
    public static final int ID_MANAGER_MAGNETIC_OFFSETH;
    public static final int ID_MANAGER_MAGNETIC_POWER_OFF;
    public static final int ID_MANAGER_MAGNETIC_POWER_ON;
    public static final int ID_MANAGER_MAGNETIC_SELF;
    public static final int ID_MANAGER_MAGNETIC_STATUS;
    public static final int ID_MANAGER_MAGNETIC_TEMPERATURE;
    public static final int ID_MANAGER_PROXIMITY;
    public static final int ID_SCOPE_MAX = (DUMMY - 1);
    public static final int ID_SCOPE_MIN = DUMMY;
    public static final String RETURN_DATA_ARRAY_INDEX_1_NG = "NG";
    public static final String RETURN_DATA_ARRAY_INDEX_1_NONE = "None";
    public static final String RETURN_DATA_ARRAY_INDEX_1_OK = "OK";
    public static final int TARGET_FILE = 2;
    public static final int TARGET_INTENT = 3;
    public static final int TARGET_MANAGER = 1;
    public static final int WHAT_NOTI_SENSOR_UPDATAE = 0;
    private static ModuleSensor mInstance = null;
    private final boolean DEBUG = true;
    private final int RETURN_DATA_ARRAY_SIZE_MAX = 16;
    private final float STANDARD_GRAVITY = 9.80665f;
    public String mFeature_Gesture = Feature.getString(Feature.SENSOR_NAME_GESTURE);
    public String mFeature_Gyroscope = Kernel.getGyroSensorName();
    public String mFeature_Magnetic = Kernel.getGeoMagneticSensorName();
    /* access modifiers changed from: private */
    public boolean mInterrupted = false;
    private NotiThread mNoti = null;
    private String[] mReturnData = new String[16];
    private SensorManager mSensorManager = null;
    private SensorReadFile mSensorReadFile = null;
    private SensorReadIntent mSensorReadIntent = null;
    private SensorReadManager mSensorReadManager = null;
    private float[] mTemp_Float = null;
    private int[] mTemp_Int = new int[3];
    private MagneticExpansionData mTemp_Magnetic = null;
    private String[] mTemp_String = null;
    private float magnitude = 0.0f;

    private class NotiThread extends Thread {
        private int NOTI_LOOP_DELAY;
        private Handler mNotiHandler;

        private NotiThread() {
            this.NOTI_LOOP_DELAY = 100;
            this.mNotiHandler = null;
        }

        public void run() {
            while (!ModuleSensor.this.mInterrupted) {
                this.mNotiHandler.sendEmptyMessage(0);
                try {
                    sleep((long) this.NOTI_LOOP_DELAY);
                } catch (InterruptedException e) {
                    LtUtil.log_e(e);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("LoopStop-mInterrupted : ");
            sb.append(ModuleSensor.this.mInterrupted);
            LtUtil.log_d(ModuleSensor.this.CLASS_NAME, "NotiThread-run", sb.toString());
        }

        public void setLoopDelay(int loopDelay_millisecond) {
            this.NOTI_LOOP_DELAY = loopDelay_millisecond;
        }

        public void setHandler(Handler notiHandler) {
            this.mNotiHandler = notiHandler;
        }
    }

    static {
        DUMMY = 0;
        int i = DUMMY;
        DUMMY = i + 1;
        ID_MANAGER_ACCELEROMETER = i;
        int i2 = DUMMY;
        DUMMY = i2 + 1;
        ID_MANAGER_ACCELEROMETER_N_ANGLE = i2;
        int i3 = DUMMY;
        DUMMY = i3 + 1;
        ID_MANAGER_ACCELEROMETER_SELF = i3;
        int i4 = DUMMY;
        DUMMY = i4 + 1;
        ID_MANAGER_BAROMETER = i4;
        int i5 = DUMMY;
        DUMMY = i5 + 1;
        ID_MANAGER_GYRO = i5;
        int i6 = DUMMY;
        DUMMY = i6 + 1;
        ID_MANAGER_GYRO_POWER = i6;
        int i7 = DUMMY;
        DUMMY = i7 + 1;
        ID_MANAGER_GYRO_EXPANSION = i7;
        int i8 = DUMMY;
        DUMMY = i8 + 1;
        ID_MANAGER_GYRO_SELF = i8;
        int i9 = DUMMY;
        DUMMY = i9 + 1;
        ID_MANAGER_GYRO_TEMPERATURE = i9;
        int i10 = DUMMY;
        DUMMY = i10 + 1;
        ID_MANAGER_LIGHT = i10;
        int i11 = DUMMY;
        DUMMY = i11 + 1;
        ID_MANAGER_MAGNETIC = i11;
        int i12 = DUMMY;
        DUMMY = i12 + 1;
        ID_MANAGER_MAGNETIC_POWER_ON = i12;
        int i13 = DUMMY;
        DUMMY = i13 + 1;
        ID_MANAGER_MAGNETIC_STATUS = i13;
        int i14 = DUMMY;
        DUMMY = i14 + 1;
        ID_MANAGER_MAGNETIC_TEMPERATURE = i14;
        int i15 = DUMMY;
        DUMMY = i15 + 1;
        ID_MANAGER_MAGNETIC_DAC = i15;
        int i16 = DUMMY;
        DUMMY = i16 + 1;
        ID_MANAGER_MAGNETIC_ADC = i16;
        int i17 = DUMMY;
        DUMMY = i17 + 1;
        ID_MANAGER_MAGNETIC_SELF = i17;
        int i18 = DUMMY;
        DUMMY = i18 + 1;
        ID_MANAGER_MAGNETIC_OFFSETH = i18;
        int i19 = DUMMY;
        DUMMY = i19 + 1;
        ID_MANAGER_MAGNETIC_POWER_OFF = i19;
        int i20 = DUMMY;
        DUMMY = i20 + 1;
        ID_MANAGER_PROXIMITY = i20;
        int i21 = DUMMY;
        DUMMY = i21 + 1;
        ID_FILE____ACCELEROMETER = i21;
        int i22 = DUMMY;
        DUMMY = i22 + 1;
        ID_FILE____ACCELEROMETER_N_ANGLE = i22;
        int i23 = DUMMY;
        DUMMY = i23 + 1;
        ID_FILE____ACCELEROMETER_SELF = i23;
        int i24 = DUMMY;
        DUMMY = i24 + 1;
        ID_FILE____ACCELEROMETER_CAL = i24;
        int i25 = DUMMY;
        DUMMY = i25 + 1;
        ID_FILE____ACCELEROMETER_INTPIN = i25;
        int i26 = DUMMY;
        DUMMY = i26 + 1;
        ID_FILE____BAROMETER_EEPROM = i26;
        int i27 = DUMMY;
        DUMMY = i27 + 1;
        ID_FILE____GYRO_TEMPERATURE = i27;
        int i28 = DUMMY;
        DUMMY = i28 + 1;
        ID_FILE____GYRO_SELFTEST = i28;
        int i29 = DUMMY;
        DUMMY = i29 + 1;
        ID_FILE____LIGHT = i29;
        int i30 = DUMMY;
        DUMMY = i30 + 1;
        ID_FILE____LIGHT_CCT = i30;
        int i31 = DUMMY;
        DUMMY = i31 + 1;
        ID_FILE____LIGHT_ADC = i31;
        int i32 = DUMMY;
        DUMMY = i32 + 1;
        ID_FILE____MAGNETIC_POWER_ON = i32;
        int i33 = DUMMY;
        DUMMY = i33 + 1;
        ID_FILE____MAGNETIC_POWER_OFF = i33;
        int i34 = DUMMY;
        DUMMY = i34 + 1;
        ID_FILE____MAGNETIC_STATUS = i34;
        int i35 = DUMMY;
        DUMMY = i35 + 1;
        ID_FILE____MAGNETIC_TEMPERATURE = i35;
        int i36 = DUMMY;
        DUMMY = i36 + 1;
        ID_FILE____MAGNETIC_DAC = i36;
        int i37 = DUMMY;
        DUMMY = i37 + 1;
        ID_FILE____MAGNETIC_ADC = i37;
        int i38 = DUMMY;
        DUMMY = i38 + 1;
        ID_FILE____MAGNETIC_SELF = i38;
        int i39 = DUMMY;
        DUMMY = i39 + 1;
        ID_FILE____PROXIMITY_ADC = i39;
        int i40 = DUMMY;
        DUMMY = i40 + 1;
        ID_FILE____PROXIMITY_AVG = i40;
        int i41 = DUMMY;
        DUMMY = i41 + 1;
        ID_FILE____PROXIMITY_OFFSET = i41;
        int i42 = DUMMY;
        DUMMY = i42 + 1;
        ID_INTENT__CP_ACCELEROMETER = i42;
        int i43 = DUMMY;
        DUMMY = i43 + 1;
        ID_INTENT__GRIP = i43;
    }

    private ModuleSensor(Context context) {
        super(context, "ModuleSensor");
        LtUtil.log_i(this.CLASS_NAME, "ModuleSensor", null);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        SensorCalculator.initialize();
    }

    public static ModuleSensor instance(Context context) {
        if (mInstance == null) {
            mInstance = new ModuleSensor(context);
        }
        return mInstance;
    }

    public void SensorOn(int[] sensorID) {
        int[] id_Manager = new int[ID_MANAGER_COUNT];
        int[] id_File = new int[ID_FILE____COUNT];
        int[] id_Intent = new int[ID_INTENT__COUNT];
        if (sensorID != null) {
            int index_Intent = 0;
            int index_File = 0;
            int index_Manager = 0;
            for (int i = 0; i < sensorID.length; i++) {
                if (ID_SCOPE_MIN <= sensorID[i] && sensorID[i] < ID_MANAGER_COUNT) {
                    int index_Manager2 = index_Manager + 1;
                    id_Manager[index_Manager] = sensorID[i];
                    index_Manager = index_Manager2;
                } else if (ID_MANAGER_COUNT <= sensorID[i] && sensorID[i] < ID_MANAGER_COUNT + ID_FILE____COUNT) {
                    int index_File2 = index_File + 1;
                    id_File[index_File] = sensorID[i];
                    index_File = index_File2;
                } else if (ID_MANAGER_COUNT + ID_FILE____COUNT > sensorID[i] || sensorID[i] > ID_SCOPE_MAX) {
                    LtUtil.log_e(this.CLASS_NAME, "SensorOn", "ID : Unknown");
                } else {
                    int index_Intent2 = index_Intent + 1;
                    id_Intent[index_Intent] = sensorID[i];
                    index_Intent = index_Intent2;
                }
            }
            if (index_Manager > 0) {
                LtUtil.log_d(this.CLASS_NAME, "SensorOn", "Manager");
                int[] id = new int[index_Manager];
                for (int i2 = 0; i2 < index_Manager; i2++) {
                    id[i2] = id_Manager[i2];
                }
                this.mSensorReadManager = new SensorReadManager(id, this.mSensorManager);
            }
            if (index_File > 0) {
                LtUtil.log_d(this.CLASS_NAME, "SensorOn", "File");
                int[] id2 = new int[index_File];
                for (int i3 = 0; i3 < index_File; i3++) {
                    id2[i3] = id_File[i3];
                }
                this.mSensorReadFile = new SensorReadFile(id2);
            }
            if (index_Intent > 0) {
                LtUtil.log_d(this.CLASS_NAME, "SensorOn", "Intent");
                int[] id3 = new int[index_Intent];
                for (int i4 = 0; i4 < index_Intent; i4++) {
                    id3[i4] = id_Intent[i4];
                }
                this.mSensorReadIntent = new SensorReadIntent(mContext, id3);
            }
            int i5 = index_Manager;
            int index_Manager3 = index_File;
            int index_File3 = index_Intent;
        }
    }

    public void SensorOn(int[] sensorID, Handler notiHandler, int loopDelay_millisecond) {
        SensorOn(sensorID);
        if (this.mSensorReadFile != null) {
            this.mSensorReadFile.startLoop(loopDelay_millisecond);
        }
        if (sensorID != null) {
            this.mNoti = new NotiThread();
            this.mNoti.setLoopDelay(loopDelay_millisecond);
            this.mNoti.setHandler(notiHandler);
            this.mNoti.setDaemon(true);
            this.mInterrupted = false;
            this.mNoti.start();
        }
    }

    public void SensorOff() {
        if (this.mSensorReadManager != null) {
            this.mSensorReadManager.sensorOff();
            this.mSensorReadManager = null;
            LtUtil.log_d(this.CLASS_NAME, "SensorOff", "Manager");
        }
        if (this.mSensorReadFile != null) {
            this.mSensorReadFile.sensorOff();
            this.mSensorReadFile = null;
            LtUtil.log_d(this.CLASS_NAME, "SensorOff", "File");
        }
        if (this.mNoti != null && this.mNoti.isAlive()) {
            this.mInterrupted = true;
            StringBuilder sb = new StringBuilder();
            sb.append("mInterrupted : ");
            sb.append(this.mInterrupted);
            LtUtil.log_e(this.CLASS_NAME, "notiStop", sb.toString());
            this.mNoti = null;
        }
    }

    public void SensorOff_Intent(int sensorID) {
        if (sensorID <= ID_INTENT__COUNT && this.mSensorReadIntent != null) {
            if (sensorID == ID_INTENT__GRIP) {
                this.mSensorReadIntent.sensorOff();
            } else if (sensorID == ID_INTENT__CP_ACCELEROMETER) {
                this.mSensorReadIntent.disableReceiver_CPsAccelerometer();
            }
            this.mSensorReadIntent = null;
            LtUtil.log_d(this.CLASS_NAME, "SensorOff", "Intent");
        }
        if (this.mNoti != null && this.mNoti.isAlive()) {
            this.mInterrupted = true;
            StringBuilder sb = new StringBuilder();
            sb.append("mInterrupted : ");
            sb.append(this.mInterrupted);
            LtUtil.log_e(this.CLASS_NAME, "notiStop", sb.toString());
            this.mNoti = null;
        }
    }

    public boolean isSensorOn(int sensorID) {
        if (this.mSensorReadManager != null && ID_SCOPE_MIN <= sensorID && sensorID < ID_MANAGER_COUNT) {
            return this.mSensorReadManager.isSensorOn(sensorID);
        }
        if (this.mSensorReadFile != null && ID_MANAGER_COUNT <= sensorID && sensorID < ID_MANAGER_COUNT + ID_FILE____COUNT) {
            return this.mSensorReadFile.isSensorOn(sensorID);
        }
        if (this.mSensorReadIntent != null && ID_MANAGER_COUNT + ID_FILE____COUNT <= sensorID && sensorID <= ID_SCOPE_MAX) {
            return this.mSensorReadIntent.isSensorOn(sensorID);
        }
        LtUtil.log_e(this.CLASS_NAME, "SensorOn", "null / ID unknown");
        return false;
    }

    public String[] getData(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("id : ");
        sb.append(getString_ID(id));
        LtUtil.log_d(this.CLASS_NAME, "getData", sb.toString());
        if (id == ID_MANAGER_ACCELEROMETER) {
            return getAccelermeterXYZ(1);
        }
        if (id == ID_FILE____ACCELEROMETER) {
            return getAccelermeterXYZ(2);
        }
        if (id == ID_MANAGER_ACCELEROMETER_N_ANGLE) {
            return getAccelermeterXYZnAngle(1);
        }
        if (id == ID_FILE____ACCELEROMETER_N_ANGLE) {
            return getAccelermeterXYZnAngle(2);
        }
        if (id == ID_MANAGER_ACCELEROMETER_SELF) {
            return getAccelermeterSelf(1);
        }
        if (id == ID_FILE____ACCELEROMETER_SELF) {
            return getAccelermeterSelf(2);
        }
        if (id == ID_FILE____ACCELEROMETER_CAL) {
            return getAccelermeterCal(2);
        }
        if (id == ID_FILE____ACCELEROMETER_INTPIN) {
            return getAccelermeterIntpin(2);
        }
        if (id == ID_INTENT__CP_ACCELEROMETER) {
            return getAccelermeterXYZnAngle(3);
        }
        if (id == ID_MANAGER_BAROMETER) {
            return getBarometer(1);
        }
        if (id == ID_FILE____BAROMETER_EEPROM) {
            return getBarometerEEPROM(2);
        }
        if (id == ID_INTENT__GRIP) {
            return getGrip(3);
        }
        if (id == ID_MANAGER_GYRO) {
            return getGyro(1);
        }
        if (id == ID_MANAGER_GYRO_EXPANSION) {
            return getGyroExpansion(1);
        }
        if (id == ID_MANAGER_GYRO_TEMPERATURE) {
            return getGyroTemperature(1);
        }
        if (id == ID_FILE____GYRO_TEMPERATURE) {
            return getGyroTemperature(2);
        }
        if (id == ID_MANAGER_GYRO_SELF) {
            return getGyroSelf(1);
        }
        if (id == ID_FILE____GYRO_SELFTEST) {
            return getGyroSelf(2);
        }
        if (id == ID_MANAGER_LIGHT) {
            return getLight(1);
        }
        if (id == ID_FILE____LIGHT) {
            return getLight(2);
        }
        if (id == ID_FILE____LIGHT_CCT) {
            return getLightCCT(2);
        }
        if (id == ID_FILE____LIGHT_ADC) {
            return getLightADC(2);
        }
        if (id == ID_MANAGER_MAGNETIC) {
            return getMagnetic(1);
        }
        if (id == ID_MANAGER_MAGNETIC_POWER_ON) {
            return getMagneticPowerOn(1);
        }
        if (id == ID_FILE____MAGNETIC_POWER_ON) {
            return getMagneticPowerOn(2);
        }
        if (id == ID_MANAGER_MAGNETIC_POWER_OFF) {
            return getMagneticPowerOff(1);
        }
        if (id == ID_FILE____MAGNETIC_POWER_OFF) {
            return getMagneticPowerOff(2);
        }
        if (id == ID_MANAGER_MAGNETIC_STATUS) {
            return getMagneticStatus(1);
        }
        if (id == ID_FILE____MAGNETIC_STATUS) {
            return getMagneticStatus(2);
        }
        if (id == ID_MANAGER_MAGNETIC_TEMPERATURE) {
            return getMagneticTemperature(1);
        }
        if (id == ID_FILE____MAGNETIC_TEMPERATURE) {
            return getMagneticTemperature(2);
        }
        if (id == ID_MANAGER_MAGNETIC_DAC) {
            return getMagneticDAC(1);
        }
        if (id == ID_FILE____MAGNETIC_DAC) {
            return getMagneticDAC(2);
        }
        if (id == ID_MANAGER_MAGNETIC_ADC) {
            return getMagneticADC(1);
        }
        if (id == ID_FILE____MAGNETIC_ADC) {
            return getMagneticADC(2);
        }
        if (id == ID_MANAGER_MAGNETIC_SELF) {
            return getMagneticSelf(1);
        }
        if (id == ID_FILE____MAGNETIC_SELF) {
            return getMagneticSelf(2);
        }
        if (id == ID_MANAGER_MAGNETIC_OFFSETH) {
            return getMagneticOffsetH(1);
        }
        if (id == ID_MANAGER_PROXIMITY) {
            return getProximity(1);
        }
        if (id == ID_FILE____PROXIMITY_ADC) {
            return getProximityADC(2);
        }
        if (id == ID_FILE____PROXIMITY_AVG) {
            return getProximityAVG(2);
        }
        if (id == ID_FILE____PROXIMITY_OFFSET) {
            return getProximityOffset(2);
        }
        LtUtil.log_e(this.CLASS_NAME, "getData", "id : Unknown");
        return null;
    }

    private String[] getAccelermeterXYZ(int target) {
        double changeBit;
        if (target == 1 && this.mSensorReadManager != null) {
            this.mTemp_Float = this.mSensorReadManager.returnAccelermeter();
            if (this.mTemp_Float != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(" Count : ");
                sb.append(this.mTemp_Float.length);
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZ", sb.toString());
                this.mReturnData[0] = "4";
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                if ("12".equals(TestCase.getString(TestCase.SENSOR_TEST_ACC_BIT))) {
                    changeBit = 104.48979591836734d;
                } else {
                    changeBit = 24.0d;
                }
                if (TestCase.getEnabled(TestCase.IS_SENSOR_TEST_ACC_REVERSE)) {
                    String[] strArr = this.mReturnData;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(((int) (((double) this.mTemp_Float[1]) * changeBit)) * -1);
                    strArr[2] = sb2.toString();
                    String[] strArr2 = this.mReturnData;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("");
                    sb3.append(((int) (((double) this.mTemp_Float[0]) * changeBit)) * -1);
                    strArr2[3] = sb3.toString();
                } else {
                    String[] strArr3 = this.mReturnData;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("");
                    sb4.append((int) (((double) this.mTemp_Float[0]) * changeBit));
                    strArr3[2] = sb4.toString();
                    String[] strArr4 = this.mReturnData;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("");
                    sb5.append((int) (((double) this.mTemp_Float[1]) * changeBit));
                    strArr4[3] = sb5.toString();
                }
                String[] strArr5 = this.mReturnData;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("");
                sb6.append((int) (((double) this.mTemp_Float[2]) * changeBit));
                strArr5[4] = sb6.toString();
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZ", "null");
                return null;
            }
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____ACCELEROMETER);
            if (this.mTemp_String != null) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Count : ");
                sb7.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZ", sb7.toString());
                this.mReturnData[0] = "4";
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                this.mReturnData[2] = this.mTemp_String[0];
                this.mReturnData[3] = this.mTemp_String[1];
                this.mReturnData[4] = this.mTemp_String[2];
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZ", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZ", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getAccelermeterXYZnAngle(int target) {
        int i = target;
        this.mTemp_String = getAccelermeterXYZ(target);
        if (this.mTemp_String != null) {
            this.mTemp_Int[0] = Integer.parseInt(this.mTemp_String[2].trim());
            this.mTemp_Int[1] = Integer.parseInt(this.mTemp_String[3].trim());
            this.mTemp_Int[2] = Integer.parseInt(this.mTemp_String[4].trim());
            this.mTemp_String = SensorCalculator.accelerometerAngle(this.mTemp_Int);
        }
        if (i == 1 && this.mSensorReadManager != null) {
            this.mTemp_Float = this.mSensorReadManager.returnAccelermeter();
            if (!(this.mTemp_Float == null || this.mTemp_String == null)) {
                this.mReturnData[0] = "10";
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                String[] strArr = this.mReturnData;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.mTemp_Int[0]);
                strArr[2] = sb.toString();
                String[] strArr2 = this.mReturnData;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(this.mTemp_Int[1]);
                strArr2[3] = sb2.toString();
                String[] strArr3 = this.mReturnData;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("");
                sb3.append(this.mTemp_Int[2]);
                strArr3[4] = sb3.toString();
                this.mReturnData[5] = this.mTemp_String[0];
                this.mReturnData[6] = this.mTemp_String[1];
                this.mReturnData[7] = this.mTemp_String[2];
                this.magnitude = (float) Math.sqrt((double) ((this.mTemp_Float[0] * this.mTemp_Float[0]) + (this.mTemp_Float[1] * this.mTemp_Float[1]) + (this.mTemp_Float[2] * this.mTemp_Float[2])));
                String[] strArr4 = this.mReturnData;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(this.magnitude);
                strArr4[8] = sb4.toString();
                String[] strArr5 = this.mReturnData;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("");
                sb5.append(Math.abs(this.magnitude - 9.80665f));
                strArr5[9] = sb5.toString();
                String[] strArr6 = this.mReturnData;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("");
                sb6.append((int) (((float) (-Math.atan2((double) this.mTemp_Float[0], (double) this.mTemp_Float[1]))) * 57.29578f));
                strArr6[10] = sb6.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append("coordinates_x :");
                sb7.append(this.mReturnData[2]);
                sb7.append(", coordinates_y :");
                sb7.append(this.mReturnData[3]);
                sb7.append(", coordinates_z :");
                sb7.append(this.mReturnData[4]);
                LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb7.toString());
                StringBuilder sb8 = new StringBuilder();
                sb8.append("X_Angle :");
                sb8.append(this.mReturnData[5]);
                sb8.append(", Y_Angle :");
                sb8.append(this.mReturnData[6]);
                sb8.append(", z_Angle :");
                sb8.append(this.mReturnData[7]);
                LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb8.toString());
                StringBuilder sb9 = new StringBuilder();
                sb9.append("magnitude :");
                sb9.append(this.mReturnData[8]);
                sb9.append(", deviation :");
                sb9.append(this.mReturnData[9]);
                LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb9.toString());
                StringBuilder sb10 = new StringBuilder();
                sb10.append("XY_value :");
                sb10.append(this.mReturnData[10]);
                LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb10.toString());
            }
        } else if (i != 2 || this.mSensorReadFile == null) {
            if (i != 3 || this.mSensorReadIntent == null) {
                return null;
            }
            this.mTemp_Int = this.mSensorReadIntent.returnCPsAccelerometerData();
            if (this.mTemp_Int != null) {
                this.mTemp_String = SensorCalculator.accelerometerAngle(this.mTemp_Int);
            }
            if (this.mTemp_Int == null || this.mTemp_String == null) {
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZnAngle", "null");
                return null;
            }
            StringBuilder sb11 = new StringBuilder();
            sb11.append("mTemp_Float.length : ");
            sb11.append(this.mTemp_Float.length);
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZnAngle", sb11.toString());
            this.mReturnData[0] = "10";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr7 = this.mReturnData;
            StringBuilder sb12 = new StringBuilder();
            sb12.append("");
            sb12.append(this.mTemp_Int[0]);
            strArr7[2] = sb12.toString();
            String[] strArr8 = this.mReturnData;
            StringBuilder sb13 = new StringBuilder();
            sb13.append("");
            sb13.append(this.mTemp_Int[1]);
            strArr8[3] = sb13.toString();
            String[] strArr9 = this.mReturnData;
            StringBuilder sb14 = new StringBuilder();
            sb14.append("");
            sb14.append(this.mTemp_Int[2]);
            strArr9[4] = sb14.toString();
            this.mReturnData[5] = this.mTemp_String[0];
            this.mReturnData[6] = this.mTemp_String[1];
            this.mReturnData[7] = this.mTemp_String[2];
            this.magnitude = (float) Math.sqrt((double) ((this.mTemp_Int[0] * this.mTemp_Int[0]) + (this.mTemp_Int[1] * this.mTemp_Int[1]) + (this.mTemp_Int[2] * this.mTemp_Int[2])));
            String[] strArr10 = this.mReturnData;
            StringBuilder sb15 = new StringBuilder();
            sb15.append("");
            sb15.append(this.magnitude);
            strArr10[8] = sb15.toString();
            String[] strArr11 = this.mReturnData;
            StringBuilder sb16 = new StringBuilder();
            sb16.append("");
            sb16.append(Math.abs(this.magnitude - 9.80665f));
            strArr11[9] = sb16.toString();
            String[] strArr12 = this.mReturnData;
            StringBuilder sb17 = new StringBuilder();
            sb17.append("");
            sb17.append((int) (((float) (-Math.atan2((double) this.mTemp_Int[0], (double) this.mTemp_Int[1]))) * 57.29578f));
            strArr12[10] = sb17.toString();
            StringBuilder sb18 = new StringBuilder();
            sb18.append("CP: coordinates_x :");
            sb18.append(this.mReturnData[2]);
            sb18.append(", coordinates_y :");
            sb18.append(this.mReturnData[3]);
            sb18.append(", coordinates_z :");
            sb18.append(this.mReturnData[4]);
            LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb18.toString());
            StringBuilder sb19 = new StringBuilder();
            sb19.append("CP: X_Angle :");
            sb19.append(this.mReturnData[5]);
            sb19.append(", Y_Angle :");
            sb19.append(this.mReturnData[6]);
            sb19.append(", z_Angle :");
            sb19.append(this.mReturnData[7]);
            LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb19.toString());
            StringBuilder sb20 = new StringBuilder();
            sb20.append("CP: magnitude :");
            sb20.append(this.mReturnData[8]);
            sb20.append(", deviation :");
            sb20.append(this.mReturnData[9]);
            LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb20.toString());
            StringBuilder sb21 = new StringBuilder();
            sb21.append("CP: XY_value :");
            sb21.append(this.mReturnData[10]);
            LtUtil.log_i(this.CLASS_NAME, "getAccelermeterRawXYZ", sb21.toString());
        } else if (this.mTemp_String != null) {
            this.mReturnData[0] = "7";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr13 = this.mReturnData;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("");
            sb22.append(this.mTemp_Int[0]);
            strArr13[2] = sb22.toString();
            String[] strArr14 = this.mReturnData;
            StringBuilder sb23 = new StringBuilder();
            sb23.append("");
            sb23.append(this.mTemp_Int[1]);
            strArr14[3] = sb23.toString();
            String[] strArr15 = this.mReturnData;
            StringBuilder sb24 = new StringBuilder();
            sb24.append("");
            sb24.append(this.mTemp_Int[2]);
            strArr15[4] = sb24.toString();
            this.mReturnData[5] = this.mTemp_String[0];
            this.mReturnData[6] = this.mTemp_String[1];
            this.mReturnData[7] = this.mTemp_String[2];
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZNAngle", dataCheck(this.mReturnData));
        } else {
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterXYZnAngle", "null");
            return null;
        }
        return this.mReturnData;
    }

    private String[] getAccelermeterSelf(int target) {
        this.mTemp_String = getAccelermeterXYZ(target);
        if (this.mTemp_String != null) {
            this.mTemp_Int[0] = Integer.parseInt(this.mTemp_String[2].trim());
            this.mTemp_Int[1] = Integer.parseInt(this.mTemp_String[3].trim());
            this.mTemp_Int[2] = Integer.parseInt(this.mTemp_String[4].trim());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = SensorCalculator.getResultAccelerometerSelf(this.mTemp_Int[0], this.mTemp_Int[1], this.mTemp_Int[2]);
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterSelf", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getAccelermeterSelf", "null");
        return null;
    }

    private String[] getAccelermeterCal(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____ACCELEROMETER_CAL);
        if (this.mTemp_String != null) {
            if (this.mTemp_String.length <= 1) {
                this.mTemp_String = this.mTemp_String[0].split(" ");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterCal", sb.toString());
            this.mReturnData[0] = "4";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            this.mReturnData[3] = this.mTemp_String[1];
            this.mReturnData[4] = this.mTemp_String[2];
            LtUtil.log_d(this.CLASS_NAME, "getAccelermeterCal", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getAccelermeterCal", "null");
        return null;
    }

    private String[] getAccelermeterIntpin(int target) {
        String INTPIN_ENABLE = EgisFingerprint.MAJOR_VERSION;
        String INTPIN_DISABLE = "0";
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        if (Kernel.write(Kernel.ACCEL_SENSOR_INTPIN, INTPIN_ENABLE)) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____ACCELEROMETER_INTPIN);
            if (this.mTemp_String != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Count : ");
                sb.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterIntpin", sb.toString());
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                this.mReturnData[2] = this.mTemp_String[0];
                Kernel.write(Kernel.ACCEL_SENSOR_INTPIN, INTPIN_DISABLE);
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getAccelermeterIntpin", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getAccelermeterIntpin", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getBarometer(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target == 2 && this.mSensorReadFile == null) ? null : null;
        }
        this.mTemp_Float = this.mSensorReadManager.returnBarometer();
        if (this.mTemp_Float != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Float.length);
            LtUtil.log_d(this.CLASS_NAME, "getBarometer", sb.toString());
            DecimalFormat format = new DecimalFormat("#.##");
            this.mReturnData[0] = "4";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = String.valueOf(format.format((double) this.mTemp_Float[0]));
            String[] strArr = this.mReturnData;
            SensorManager sensorManager = this.mSensorManager;
            strArr[3] = String.valueOf(format.format((double) SensorManager.getAltitude(1013.25f, this.mTemp_Float[0])));
            this.mReturnData[4] = Kernel.read(Kernel.BAROMETE_TEMPERATURE);
            LtUtil.log_d(this.CLASS_NAME, "getBarometer", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getBarometer", "null");
        return null;
    }

    private String[] getBarometerEEPROM(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____BAROMETER_EEPROM);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getBarometerEEPROM", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            LtUtil.log_d(this.CLASS_NAME, "getBarometerEEPROM", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getBarometerEEPROM", "null");
        return null;
    }

    private String[] getGrip(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            return null;
        }
        if ((target == 2 && this.mSensorReadFile != null) || target != 3 || this.mSensorReadIntent == null) {
            return null;
        }
        this.mTemp_Int = this.mSensorReadIntent.returnGrip();
        if (this.mTemp_Int != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Int.length);
            LtUtil.log_d(this.CLASS_NAME, "getGrip", sb.toString());
            this.mReturnData[0] = "3";
            String[] strArr = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mTemp_Int[0]);
            strArr[1] = sb2.toString();
            String[] strArr2 = this.mReturnData;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.mTemp_Int[1]);
            strArr2[2] = sb3.toString();
            String[] strArr3 = this.mReturnData;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.mTemp_Int[2]);
            strArr3[3] = sb4.toString();
            LtUtil.log_d(this.CLASS_NAME, "getGyro", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getGrip", "null");
        return null;
    }

    private String[] getGyro(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target == 2 && this.mSensorReadFile == null) ? null : null;
        }
        this.mTemp_Float = this.mSensorReadManager.returnGyro();
        if (this.mTemp_Float != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Float.length);
            LtUtil.log_d(this.CLASS_NAME, "getGyro", sb.toString());
            this.mReturnData[0] = "4";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mTemp_Float[0] * 57.295776f);
            strArr[2] = sb2.toString();
            String[] strArr2 = this.mReturnData;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.mTemp_Float[1] * 57.295776f);
            strArr2[3] = sb3.toString();
            String[] strArr3 = this.mReturnData;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.mTemp_Float[2] * 57.295776f);
            strArr3[4] = sb4.toString();
            LtUtil.log_d(this.CLASS_NAME, "getGyro", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getGyro", "null");
        return null;
    }

    private String[] getGyroExpansion(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target == 2 && this.mSensorReadFile == null) ? null : null;
        }
        GyroExpansionData returnValue = new GyroExpansionData();
        if (returnValue.mNoiseBias == null || returnValue.mData == null || returnValue.mRMSValue == null) {
            if (returnValue.mNoiseBias == null) {
                LtUtil.log_d(this.CLASS_NAME, "getGyroExpansion", "Noise Bias null");
            }
            if (returnValue.mData == null) {
                LtUtil.log_d(this.CLASS_NAME, "getGyroExpansion", "Data null");
            }
            if (returnValue.mRMSValue == null) {
                LtUtil.log_d(this.CLASS_NAME, "getGyroExpansion", "RMS null");
            }
            return null;
        }
        this.mReturnData[0] = "10";
        String[] strArr = this.mReturnData;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(returnValue.mReturnValue);
        strArr[1] = sb.toString();
        String[] strArr2 = this.mReturnData;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(returnValue.mNoiseBias[0]);
        strArr2[2] = sb2.toString();
        String[] strArr3 = this.mReturnData;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(returnValue.mNoiseBias[1]);
        strArr3[3] = sb3.toString();
        String[] strArr4 = this.mReturnData;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("");
        sb4.append(returnValue.mNoiseBias[2]);
        strArr4[4] = sb4.toString();
        String[] strArr5 = this.mReturnData;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("");
        sb5.append(returnValue.mData[0]);
        strArr5[5] = sb5.toString();
        String[] strArr6 = this.mReturnData;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("");
        sb6.append(returnValue.mData[1]);
        strArr6[6] = sb6.toString();
        String[] strArr7 = this.mReturnData;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("");
        sb7.append(returnValue.mData[2]);
        strArr7[7] = sb7.toString();
        String[] strArr8 = this.mReturnData;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("");
        sb8.append(returnValue.mRMSValue[0]);
        strArr8[8] = sb8.toString();
        String[] strArr9 = this.mReturnData;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("");
        sb9.append(returnValue.mRMSValue[1]);
        strArr9[9] = sb9.toString();
        String[] strArr10 = this.mReturnData;
        StringBuilder sb10 = new StringBuilder();
        sb10.append("");
        sb10.append(returnValue.mRMSValue[2]);
        strArr10[10] = sb10.toString();
        LtUtil.log_d(this.CLASS_NAME, "getGyroExpansion", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getGyroTemperature(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____GYRO_TEMPERATURE);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getGyroTemperature", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            LtUtil.log_d(this.CLASS_NAME, "getGyroTemperature", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getGyroTemperature", "null");
        return null;
    }

    private String[] getGyroSelf(int target) {
        String resultValue;
        if (target == 1 && this.mSensorReadManager != null) {
            GyroExpansionData returnValue = new GyroExpansionData();
            if (returnValue.mNoiseBias == null || returnValue.mRMSValue == null) {
                if (returnValue.mNoiseBias == null) {
                    LtUtil.log_d(this.CLASS_NAME, "getGyroSelf", "Noise Bias null");
                }
                if (returnValue.mRMSValue == null) {
                    LtUtil.log_d(this.CLASS_NAME, "getGyroSelf", "RMS null");
                }
                return null;
            }
            this.mReturnData[0] = "7";
            String[] strArr = this.mReturnData;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(returnValue.mReturnValue);
            strArr[1] = sb.toString();
            String[] strArr2 = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(returnValue.mNoiseBias[0]);
            strArr2[2] = sb2.toString();
            String[] strArr3 = this.mReturnData;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(returnValue.mNoiseBias[1]);
            strArr3[3] = sb3.toString();
            String[] strArr4 = this.mReturnData;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(returnValue.mNoiseBias[2]);
            strArr4[4] = sb4.toString();
            String[] strArr5 = this.mReturnData;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(returnValue.mRMSValue[0]);
            strArr5[5] = sb5.toString();
            String[] strArr6 = this.mReturnData;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("");
            sb6.append(returnValue.mRMSValue[1]);
            strArr6[6] = sb6.toString();
            String[] strArr7 = this.mReturnData;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("");
            sb7.append(returnValue.mRMSValue[2]);
            strArr7[7] = sb7.toString();
            if (this.mFeature_Gyroscope == FEATURE_GYROSCOP_STMICRO_SMARTPHONE || this.mFeature_Gyroscope == FEATURE_GYROSCOP_STMICRO_TABLET) {
                this.mReturnData[0] = EgisFingerprint.JAR_VERSION;
                this.mReturnData[8] = "";
            }
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            String str = "";
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____GYRO_SELFTEST);
            if (this.mTemp_String != null) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Count : ");
                sb8.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getGyroTemperature", sb8.toString());
                if (!this.mTemp_String[6].equals(egis.client.api.EgisFingerprint.MAJOR_VERSION) || !this.mTemp_String[7].equals(egis.client.api.EgisFingerprint.MAJOR_VERSION)) {
                    resultValue = "-1";
                } else {
                    resultValue = "0";
                }
                this.mReturnData[0] = "7";
                String[] strArr8 = this.mReturnData;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("");
                sb9.append(resultValue);
                strArr8[1] = sb9.toString();
                String[] strArr9 = this.mReturnData;
                StringBuilder sb10 = new StringBuilder();
                sb10.append("");
                sb10.append(this.mTemp_String[0]);
                strArr9[2] = sb10.toString();
                String[] strArr10 = this.mReturnData;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("");
                sb11.append(this.mTemp_String[1]);
                strArr10[3] = sb11.toString();
                String[] strArr11 = this.mReturnData;
                StringBuilder sb12 = new StringBuilder();
                sb12.append("");
                sb12.append(this.mTemp_String[2]);
                strArr11[4] = sb12.toString();
                String[] strArr12 = this.mReturnData;
                StringBuilder sb13 = new StringBuilder();
                sb13.append("");
                sb13.append(this.mTemp_String[3]);
                strArr12[5] = sb13.toString();
                String[] strArr13 = this.mReturnData;
                StringBuilder sb14 = new StringBuilder();
                sb14.append("");
                sb14.append(this.mTemp_String[4]);
                strArr13[6] = sb14.toString();
                String[] strArr14 = this.mReturnData;
                StringBuilder sb15 = new StringBuilder();
                sb15.append("");
                sb15.append(this.mTemp_String[5]);
                strArr14[7] = sb15.toString();
                if (this.mFeature_Gyroscope == FEATURE_GYROSCOP_STMICRO_SMARTPHONE || this.mFeature_Gyroscope == FEATURE_GYROSCOP_STMICRO_TABLET) {
                    this.mReturnData[0] = EgisFingerprint.JAR_VERSION;
                    String[] strArr15 = this.mReturnData;
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append("");
                    sb16.append(this.mTemp_String[6]);
                    strArr15[8] = sb16.toString();
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getGyroSelfTest", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getGyroSelf", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getLight(int target) {
        String[] mReturnData2 = new String[16];
        if (target == 1 && this.mSensorReadManager != null) {
            this.mTemp_Float = this.mSensorReadManager.returnLight();
            if (this.mTemp_Float != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Count : ");
                sb.append(this.mTemp_Float.length);
                LtUtil.log_d(this.CLASS_NAME, "getLight", sb.toString());
                mReturnData2[0] = EgisFingerprint.MAJOR_VERSION;
                mReturnData2[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(this.mTemp_Float[0]);
                mReturnData2[2] = sb2.toString();
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getLight", "null");
                return null;
            }
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____LIGHT);
            if (this.mTemp_String != null) {
                int length = this.mTemp_String.length;
                String str = "";
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Count : ");
                sb3.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getLight", sb3.toString());
                String resultstring = length == 1 ? this.mTemp_String[0] : computeForLux(this.mTemp_String);
                mReturnData2[0] = EgisFingerprint.MAJOR_VERSION;
                mReturnData2[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(resultstring);
                mReturnData2[2] = sb4.toString();
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getLight", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getLight", dataCheck(mReturnData2));
        return mReturnData2;
    }

    private String[] getLightCCT(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            if (target != 2 || this.mSensorReadFile == null) {
                return null;
            }
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____LIGHT);
            if (this.mTemp_String != null) {
                int length = this.mTemp_String.length;
                String str = "";
                StringBuilder sb = new StringBuilder();
                sb.append("Count : ");
                sb.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getLight", sb.toString());
                String resultstring = length == 1 ? this.mTemp_String[0] : computeForCCT(this.mTemp_String);
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                String[] strArr = this.mReturnData;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(resultstring);
                strArr[2] = sb2.toString();
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getLight", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getLight", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getLightADC(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____LIGHT_ADC);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getLightADC", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mTemp_String[0]);
            strArr[2] = sb2.toString();
            LtUtil.log_d(this.CLASS_NAME, "getLightADC", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getLightADC", "null");
        return null;
    }

    private String[] getMagnetic(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target == 2 && this.mSensorReadFile == null) ? null : null;
        }
        this.mTemp_Float = this.mSensorReadManager.returnMagnetic();
        if (this.mTemp_Float != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Float.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagnetic", sb.toString());
            this.mReturnData[0] = "4";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mTemp_Float[0]);
            strArr[2] = sb2.toString();
            String[] strArr2 = this.mReturnData;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.mTemp_Float[1]);
            strArr2[3] = sb3.toString();
            String[] strArr3 = this.mReturnData;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.mTemp_Float[2]);
            strArr3[4] = sb4.toString();
            LtUtil.log_d(this.CLASS_NAME, "getMagnetic", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagnetic", "null");
        return null;
    }

    private String[] getMagneticPowerOn(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD008A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(1, this.mFeature_Magnetic);
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_STMicro(1, this.mFeature_Magnetic);
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mPowerOn == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOn", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mPowerOn.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOn", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = this.mTemp_Magnetic.mPowerOn[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mPowerOn[1];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_POWER_ON);
            if (this.mTemp_String != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Count : ");
                sb2.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOn", sb2.toString());
                if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_COMBINATION)) {
                    Kernel.write(Kernel.GEOMAGNETIC_SENSOR_POWER, "0");
                    this.mTemp_String[0] = Kernel.read(Kernel.GEOMAGNETIC_SENSOR_POWER);
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    if ("0".equalsIgnoreCase(this.mTemp_String[0])) {
                        this.mReturnData[1] = "OK";
                        this.mReturnData[2] = egis.client.api.EgisFingerprint.MAJOR_VERSION;
                    } else {
                        this.mReturnData[1] = "NG";
                        this.mReturnData[2] = "-1";
                    }
                } else {
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                    this.mReturnData[2] = this.mTemp_String[0];
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOn", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOn", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticPowerOff(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(2, this.mFeature_Magnetic);
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mPowerOff == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOff", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mPowerOff.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOff", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = this.mTemp_Magnetic.mPowerOff[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mPowerOff[1];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_POWER_OFF);
            if (this.mTemp_String != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Count : ");
                sb2.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOff", sb2.toString());
                if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_COMBINATION)) {
                    Kernel.write(Kernel.GEOMAGNETIC_SENSOR_POWER, EgisFingerprint.MAJOR_VERSION);
                    this.mTemp_String[0] = Kernel.read(Kernel.GEOMAGNETIC_SENSOR_POWER);
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    if (EgisFingerprint.MAJOR_VERSION.equalsIgnoreCase(this.mTemp_String[0])) {
                        this.mReturnData[1] = "OK";
                        this.mReturnData[2] = egis.client.api.EgisFingerprint.MAJOR_VERSION;
                    } else {
                        this.mReturnData[1] = "NG";
                        this.mReturnData[2] = "-1";
                    }
                } else {
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                    this.mReturnData[2] = this.mTemp_String[0];
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOff", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticPowerOff", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticStatus(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(3, this.mFeature_Magnetic);
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mStatus == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticStatus", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mStatus.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticStatus", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = this.mTemp_Magnetic.mStatus[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mStatus[1];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_STATUS);
            if (this.mTemp_String != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Count : ");
                sb2.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getMagneticStatus", sb2.toString());
                if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD008A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    if (this.mTemp_String[0].equals(egis.client.api.EgisFingerprint.MAJOR_VERSION)) {
                        this.mReturnData[1] = "OK";
                    } else {
                        this.mReturnData[1] = "NG";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                } else {
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                    if (this.mTemp_String[0].equals("OK")) {
                        this.mReturnData[2] = egis.client.api.EgisFingerprint.MAJOR_VERSION;
                    } else {
                        this.mReturnData[2] = "0";
                    }
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticStatus", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticStatus", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticTemperature(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(2, this.mFeature_Magnetic);
            } else {
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = "OK";
                this.mReturnData[2] = "0";
                LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", dataCheck(this.mReturnData));
                return this.mReturnData;
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mTemperature == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mTemperature.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = this.mTemp_Magnetic.mTemperature[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mTemperature[1];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529)) {
                this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_TEMPERATURE);
                if (this.mTemp_String != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Count : ");
                    sb2.append(this.mTemp_String.length);
                    LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", sb2.toString());
                    this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                    this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                    String[] strArr = this.mReturnData;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("");
                    sb3.append(this.mTemp_String[0]);
                    strArr[2] = sb3.toString();
                } else {
                    LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", "null");
                    return null;
                }
            } else {
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
                this.mReturnData[2] = "0";
                LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", dataCheck(this.mReturnData));
                return this.mReturnData;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticTemperature", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticDAC(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(4, this.mFeature_Magnetic);
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mDAC == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticDAC", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mDAC.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticDAC", sb.toString());
            this.mReturnData[0] = "4";
            this.mReturnData[1] = this.mTemp_Magnetic.mDAC[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mDAC[1];
            this.mReturnData[3] = this.mTemp_Magnetic.mDAC[2];
            this.mReturnData[4] = this.mTemp_Magnetic.mDAC[3];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_DAC);
            if (this.mTemp_String != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Count : ");
                sb2.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getMagneticDAC", sb2.toString());
                if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                    this.mReturnData[0] = "4";
                    this.mReturnData[1] = this.mTemp_String[0];
                    if (this.mTemp_String[0].equals("OK")) {
                        this.mReturnData[2] = egis.client.api.EgisFingerprint.MAJOR_VERSION;
                    } else {
                        this.mReturnData[2] = "0";
                    }
                    this.mReturnData[3] = "0";
                    this.mReturnData[4] = "0";
                } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                    int x = Integer.parseInt(this.mTemp_String[0]);
                    int y = Integer.parseInt(this.mTemp_String[1]);
                    int z = Integer.parseInt(this.mTemp_String[2]);
                    this.mReturnData[0] = "4";
                    if (-2000 > x || x > 2000 || -2000 > y || y > 2000 || -2000 > z || z > 2000) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                    this.mReturnData[3] = this.mTemp_String[1];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD008A)) {
                    int x2 = Integer.parseInt(this.mTemp_String[0]);
                    int y2 = Integer.parseInt(this.mTemp_String[1]);
                    int z2 = Integer.parseInt(this.mTemp_String[2]);
                    this.mReturnData[0] = "4";
                    if (-8192 > x2 || x2 > 8192 || -8192 > y2 || y2 > 8192 || -8192 > z2 || z2 > 8192) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                    this.mReturnData[3] = this.mTemp_String[1];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else {
                    this.mReturnData[0] = "4";
                    this.mReturnData[1] = this.mTemp_String[0];
                    this.mReturnData[2] = this.mTemp_String[1];
                    this.mReturnData[3] = this.mTemp_String[2];
                    this.mReturnData[4] = this.mTemp_String[3];
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticDAC", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticDAC", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticADC(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(5, this.mFeature_Magnetic);
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mADC == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticADC", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mADC.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticADC", sb.toString());
            this.mReturnData[0] = "4";
            this.mReturnData[1] = this.mTemp_Magnetic.mADC[0];
            this.mReturnData[2] = this.mTemp_Magnetic.mADC[1];
            this.mReturnData[3] = this.mTemp_Magnetic.mADC[2];
            this.mReturnData[4] = this.mTemp_Magnetic.mADC[3];
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_ADC);
            if (this.mTemp_String != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Count : ");
                sb2.append(this.mTemp_String.length);
                LtUtil.log_d(this.CLASS_NAME, "getMagneticADC", sb2.toString());
                if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                    int x = Integer.parseInt(this.mTemp_String[0]);
                    int y = Integer.parseInt(this.mTemp_String[1]);
                    int z = Integer.parseInt(this.mTemp_String[2]);
                    this.mReturnData[0] = "4";
                    if (-2000 > x || x > 2000 || -2000 > y || y > 2000 || -2000 > z || z > 2000) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                    this.mReturnData[3] = this.mTemp_String[1];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD008A)) {
                    int x2 = Integer.parseInt(this.mTemp_String[0]);
                    int y2 = Integer.parseInt(this.mTemp_String[1]);
                    int z2 = Integer.parseInt(this.mTemp_String[2]);
                    this.mReturnData[0] = "4";
                    if (-8192 > x2 || x2 > 8192 || -8192 > y2 || y2 > 8192 || -8192 > z2 || z2 > 8192) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                    this.mReturnData[3] = this.mTemp_String[1];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO)) {
                    int x3 = Integer.parseInt(this.mTemp_String[0]);
                    int y3 = Integer.parseInt(this.mTemp_String[1]);
                    int z3 = Integer.parseInt(this.mTemp_String[2]);
                    this.mReturnData[0] = "4";
                    if (-16384 > x3 || x3 > 16384 || -16384 > y3 || y3 > 16384 || -16384 > z3 || z3 > 16384) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[0];
                    this.mReturnData[3] = this.mTemp_String[1];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
                    int x4 = Integer.parseInt(this.mTemp_String[1]);
                    int y4 = Integer.parseInt(this.mTemp_String[2]);
                    int z4 = Integer.parseInt(this.mTemp_String[3]);
                    this.mReturnData[0] = "4";
                    if (-16384 > x4 || x4 > 16384 || -16384 > y4 || y4 > 16384 || -16384 > z4 || z4 > 16384) {
                        this.mReturnData[1] = "NG";
                    } else {
                        this.mReturnData[1] = "OK";
                    }
                    this.mReturnData[2] = this.mTemp_String[1];
                    this.mReturnData[3] = this.mTemp_String[2];
                    this.mReturnData[4] = this.mTemp_String[2];
                } else {
                    this.mReturnData[0] = "4";
                    this.mReturnData[1] = this.mTemp_String[0];
                    this.mReturnData[2] = this.mTemp_String[1];
                    this.mReturnData[3] = this.mTemp_String[2];
                    this.mReturnData[4] = this.mTemp_String[3];
                }
            } else {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticADC", "null");
                return null;
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticADC", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticSelf(int target) {
        if (target == 1 && this.mSensorReadManager != null) {
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8973) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8975) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK8963C_MANAGER) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09911C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09916C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_AK09918C)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS529) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD004A) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_HSCDTD006A)) {
                this.mTemp_Magnetic = this.mSensorReadManager.returnMagneticExpansion_Alps(6, this.mFeature_Magnetic);
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
                this.mTemp_Magnetic = new MagneticExpansionData();
            }
            if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mSelf == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticSelf", "null");
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Magnetic.mSelf.length);
            LtUtil.log_d(this.CLASS_NAME, "getMagneticSelf", sb.toString());
            if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = this.mTemp_Magnetic.mSelf[0];
                this.mReturnData[2] = this.mTemp_Magnetic.mSelf[1];
            } else {
                this.mReturnData[0] = "4";
                this.mReturnData[1] = this.mTemp_Magnetic.mSelf[0];
                this.mReturnData[2] = this.mTemp_Magnetic.mSelf[1];
                this.mReturnData[3] = this.mTemp_Magnetic.mSelf[2];
                this.mReturnData[4] = this.mTemp_Magnetic.mSelf[3];
            }
        } else if (target != 2 || this.mSensorReadFile == null) {
            return null;
        } else {
            this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_SELF);
            if (this.mTemp_String == null) {
                LtUtil.log_d(this.CLASS_NAME, "getMagneticSelf", "null");
                return null;
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = this.mTemp_String[0];
                this.mReturnData[2] = this.mTemp_String[1];
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_COMBINATION)) {
                Kernel.write(Kernel.GEOMAGNETIC_SENSOR_SELFTEST, EgisFingerprint.MAJOR_VERSION);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____MAGNETIC_SELF);
                this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
                this.mReturnData[1] = this.mTemp_String[0];
                this.mReturnData[2] = this.mTemp_String[1];
            } else if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_BMC150_NEWEST)) {
                return this.mTemp_String;
            } else {
                this.mReturnData[0] = "4";
                this.mReturnData[1] = this.mTemp_String[0];
                this.mReturnData[2] = this.mTemp_String[1];
                this.mReturnData[3] = this.mTemp_String[2];
                this.mReturnData[4] = this.mTemp_String[3];
            }
        }
        LtUtil.log_d(this.CLASS_NAME, "getMagneticSelf", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getMagneticOffsetH(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target != 2 || this.mSensorReadFile == null) ? null : null;
        }
        if (this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS530C) || this.mFeature_Magnetic.equals(FEATURE_MAGENTIC_YAS532)) {
            this.mTemp_Magnetic = new MagneticExpansionData();
        }
        if (this.mTemp_Magnetic == null || this.mTemp_Magnetic.mOffset_H == null) {
            LtUtil.log_d(this.CLASS_NAME, "getMagneticOffsetH", "null");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Count : ");
        sb.append(this.mTemp_Magnetic.mOffset_H.length);
        LtUtil.log_d(this.CLASS_NAME, "getMagneticOffsetH", sb.toString());
        this.mReturnData[0] = "4";
        this.mReturnData[1] = this.mTemp_Magnetic.mOffset_H[0];
        this.mReturnData[2] = this.mTemp_Magnetic.mOffset_H[1];
        this.mReturnData[3] = this.mTemp_Magnetic.mOffset_H[2];
        this.mReturnData[4] = this.mTemp_Magnetic.mOffset_H[3];
        LtUtil.log_d(this.CLASS_NAME, "getMagneticOffsetH", dataCheck(this.mReturnData));
        return this.mReturnData;
    }

    private String[] getProximity(int target) {
        if (target != 1 || this.mSensorReadManager == null) {
            return (target == 2 && this.mSensorReadFile == null) ? null : null;
        }
        this.mTemp_Float = this.mSensorReadManager.returnProximity();
        if (this.mTemp_Float != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_Float.length);
            LtUtil.log_d(this.CLASS_NAME, "getProximity", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            String[] strArr = this.mReturnData;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mTemp_Float[0]);
            strArr[2] = sb2.toString();
            LtUtil.log_d(this.CLASS_NAME, "getProximity", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getProximity", "null");
        return null;
    }

    private String[] getProximityADC(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____PROXIMITY_ADC);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getProximityADC", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            LtUtil.log_d(this.CLASS_NAME, "getProximityADC", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getProximityADC", "null");
        return null;
    }

    private String[] getProximityAVG(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____PROXIMITY_AVG);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getProximityAVG", sb.toString());
            this.mReturnData[0] = EgisFingerprint.MAJOR_VERSION;
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            LtUtil.log_d(this.CLASS_NAME, "getProximityAVG", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getProximityAVG", "null");
        return null;
    }

    private String[] getProximityOffset(int target) {
        if ((target == 1 && this.mSensorReadManager != null) || target != 2 || this.mSensorReadFile == null) {
            return null;
        }
        this.mTemp_String = this.mSensorReadFile.returnData(ID_FILE____PROXIMITY_OFFSET);
        if (this.mTemp_String != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Count : ");
            sb.append(this.mTemp_String.length);
            LtUtil.log_d(this.CLASS_NAME, "getProximityOffset", sb.toString());
            this.mReturnData[0] = "3";
            this.mReturnData[1] = RETURN_DATA_ARRAY_INDEX_1_NONE;
            this.mReturnData[2] = this.mTemp_String[0];
            this.mReturnData[3] = this.mTemp_String[1];
            LtUtil.log_d(this.CLASS_NAME, "getProximityOffset", dataCheck(this.mReturnData));
            return this.mReturnData;
        }
        LtUtil.log_d(this.CLASS_NAME, "getProximityOffset", "null");
        return null;
    }

    private String dataCheck(String[] data) {
        String result = "";
        int length = 0;
        if (data != null) {
            length = Integer.parseInt(data[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("length : ");
        sb.append(length);
        LtUtil.log_d(this.CLASS_NAME, "dataCheck", sb.toString());
        if (data != null) {
            for (int i = 0; i < length + 1; i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(result);
                sb2.append(data[i]);
                result = sb2.toString();
                if (i < length) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(result);
                    sb3.append(" , ");
                    result = sb3.toString();
                }
            }
        }
        return result;
    }

    private String getString_ID(int id) {
        if (id == ID_MANAGER_ACCELEROMETER) {
            return "ID_MANAGER_ACCELEROMETER";
        }
        if (id == ID_MANAGER_ACCELEROMETER_N_ANGLE) {
            return "ID_MANAGER_ACCELEROMETER_N_ANGLE";
        }
        if (id == ID_MANAGER_ACCELEROMETER_SELF) {
            return "ID_MANAGER_ACCELEROMETER_SELF";
        }
        if (id == ID_MANAGER_BAROMETER) {
            return "ID_MANAGER_BAROMETER";
        }
        if (id == ID_MANAGER_GYRO) {
            return "ID_MANAGER_GYRO";
        }
        if (id == ID_MANAGER_GYRO_POWER) {
            return "ID_MANAGER_GYRO_POWER";
        }
        if (id == ID_MANAGER_GYRO_EXPANSION) {
            return "ID_MANAGER_GYRO_EXPANSION";
        }
        if (id == ID_MANAGER_GYRO_SELF) {
            return "ID_MANAGER_GYRO_SELF";
        }
        if (id == ID_MANAGER_GYRO_TEMPERATURE) {
            return "ID_MANAGER_GYRO_TEMPERATURE";
        }
        if (id == ID_MANAGER_LIGHT) {
            return "ID_MANAGER_LIGHT";
        }
        if (id == ID_MANAGER_MAGNETIC) {
            return "ID_MANAGER_MAGNETIC";
        }
        if (id == ID_MANAGER_MAGNETIC_POWER_ON) {
            return "ID_MANAGER_MAGNETIC_POWER_ON";
        }
        if (id == ID_MANAGER_MAGNETIC_STATUS) {
            return "ID_MANAGER_MAGNETIC_STATUS";
        }
        if (id == ID_MANAGER_MAGNETIC_TEMPERATURE) {
            return "ID_MANAGER_MAGNETIC_TEMPERATURE";
        }
        if (id == ID_MANAGER_MAGNETIC_DAC) {
            return "ID_MANAGER_MAGNETIC_DAC";
        }
        if (id == ID_MANAGER_MAGNETIC_ADC) {
            return "ID_MANAGER_MAGNETIC_ADC";
        }
        if (id == ID_MANAGER_MAGNETIC_SELF) {
            return "ID_MANAGER_MAGNETIC_SELF";
        }
        if (id == ID_MANAGER_MAGNETIC_OFFSETH) {
            return "ID_MANAGER_MAGNETIC_OFFSETH";
        }
        if (id == ID_MANAGER_PROXIMITY) {
            return "ID_MANAGER_PROXIMITY";
        }
        if (id == ID_FILE____ACCELEROMETER) {
            return "ID_FILE____ACCELEROMETER";
        }
        if (id == ID_FILE____ACCELEROMETER_N_ANGLE) {
            return "ID_FILE____ACCELEROMETER_N_ANGLE";
        }
        if (id == ID_FILE____ACCELEROMETER_SELF) {
            return "ID_FILE____ACCELEROMETER_SELF";
        }
        if (id == ID_FILE____ACCELEROMETER_CAL) {
            return "ID_FILE____ACCELEROMETER_CAL";
        }
        if (id == ID_FILE____ACCELEROMETER_INTPIN) {
            return "ID_FILE____ACCELEROMETER_INTPIN";
        }
        if (id == ID_FILE____BAROMETER_EEPROM) {
            return "ID_FILE____BAROMETER_EEPROM";
        }
        if (id == ID_FILE____GYRO_TEMPERATURE) {
            return "ID_FILE____GYRO_TEMPERATURE";
        }
        if (id == ID_FILE____GYRO_SELFTEST) {
            return "ID_FILE____GYRO_SELFTEST";
        }
        if (id == ID_FILE____LIGHT) {
            return "ID_FILE____LIGHT";
        }
        if (id == ID_FILE____LIGHT_ADC) {
            return "ID_FILE____LIGHT_ADC";
        }
        if (id == ID_FILE____MAGNETIC_POWER_ON) {
            return "ID_FILE____MAGNETIC_POWER_ON";
        }
        if (id == ID_FILE____MAGNETIC_STATUS) {
            return "ID_FILE____MAGNETIC_STATUS";
        }
        if (id == ID_FILE____MAGNETIC_TEMPERATURE) {
            return "ID_FILE____MAGNETIC_TEMPERATURE";
        }
        if (id == ID_FILE____MAGNETIC_DAC) {
            return "ID_FILE____MAGNETIC_DAC";
        }
        if (id == ID_FILE____MAGNETIC_ADC) {
            return "ID_FILE____MAGNETIC_ADC";
        }
        if (id == ID_FILE____MAGNETIC_SELF) {
            return "ID_FILE____MAGNETIC_SELF";
        }
        if (id == ID_FILE____PROXIMITY_ADC) {
            return "ID_FILE____PROXIMITY_ADC";
        }
        if (id == ID_FILE____PROXIMITY_AVG) {
            return "ID_FILE____PROXIMITY_AVG";
        }
        if (id == ID_FILE____PROXIMITY_OFFSET) {
            return "ID_FILE____PROXIMITY_OFFSET";
        }
        if (id == ID_INTENT__GRIP) {
            return "ID_INTENT__GRIP";
        }
        if (id == ID_INTENT__CP_ACCELEROMETER) {
            return "ID_INTENT__CP_ACCELEROMETER";
        }
        return Properties.PROPERTIES_DEFAULT_STRING;
    }

    private String computeForLux(String[] input) {
        int luxValue;
        int green = Integer.valueOf(input[1]).intValue();
        int white = Integer.valueOf(input[3]).intValue();
        float I_cf = white != 0 ? ((float) green) / ((float) white) : (float) green;
        if (green < 5) {
            return "0";
        }
        if (((double) I_cf) <= 0.5d) {
            luxValue = (int) (0.0376d * Math.pow(((double) green) * 0.9d, 1.328d));
        } else if (0.5d >= ((double) I_cf) || ((double) I_cf) >= 0.9d) {
            luxValue = (int) (((((double) green) * 0.18d) * 9.25d) / ((double) I_cf));
        } else {
            luxValue = (int) (((double) green) * ((1.5d * ((double) I_cf)) - 0.46d));
        }
        return String.valueOf(luxValue);
    }

    private String computeForCCT(String[] input) {
        float I_cf;
        double d;
        int mRedValue = Integer.valueOf(input[0]).intValue();
        int mGreenValue = Integer.valueOf(input[1]).intValue();
        int mBlueValue = Integer.valueOf(input[2]).intValue();
        int mWhiteValue = Integer.valueOf(input[3]).intValue();
        int cctValue = -1;
        if (mWhiteValue != 0) {
            I_cf = ((float) mGreenValue) / ((float) mWhiteValue);
        } else {
            I_cf = (float) mGreenValue;
        }
        if (mWhiteValue >= 62000) {
            cctValue = 2700;
        } else if (((double) I_cf) <= 0.5d) {
            double ccti = (mGreenValue != 0 ? ((double) (mRedValue - mBlueValue)) / ((double) mGreenValue) : (double) (mRedValue - mBlueValue)) + 0.84d;
            cctValue = ccti <= 0.155d ? 7096 : ccti >= 1.4d ? 2467 : (int) (Math.pow(ccti, -0.48d) * 2900.0d);
        } else {
            double ccti2 = (mGreenValue != 0 ? ((double) (mRedValue - mBlueValue)) / ((double) mGreenValue) : (double) (mRedValue - mBlueValue)) + 0.29d;
            if (ccti2 <= 0.155d) {
                cctValue = 7096;
            } else if (ccti2 >= 1.4d) {
                cctValue = 2467;
            } else if (((double) I_cf) <= 0.5d || ((double) I_cf) >= 0.9d) {
                if (((double) I_cf) >= 0.9d && ((double) I_cf) < 1.65d) {
                    cctValue = (int) (Math.pow(ccti2, -0.48d) * 2900.0d);
                } else if (((double) I_cf) < 1.65d || ((double) I_cf) > 1.9d) {
                    cctValue = (int) (Math.pow(0.1232d, -0.48d) * 2900.0d);
                } else {
                    double cctixlamp = 0.07d * ((double) I_cf);
                    if (mRedValue != mBlueValue) {
                        d = ((double) ((((float) mWhiteValue) / ((float) Math.abs(mRedValue - mBlueValue))) * I_cf)) * Math.abs(ccti2);
                    } else {
                        d = ((double) (((float) mWhiteValue) * I_cf)) * Math.abs(ccti2);
                    }
                    cctValue = (int) (((double) (2900.0f * ((float) d))) * Math.pow(cctixlamp, -0.48d));
                }
            } else if (ccti2 > 0.4d && ccti2 < 1.1d) {
                cctValue = (int) (2700.0d * Math.pow(ccti2, -1.126d));
            } else if (ccti2 > 1.1d && ccti2 < 1.4d) {
                cctValue = (int) (Math.pow(ccti2, -0.48d) * 2900.0d);
            }
        }
        return String.valueOf(cctValue);
    }
}
