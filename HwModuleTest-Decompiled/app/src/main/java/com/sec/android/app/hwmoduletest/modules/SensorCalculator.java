package com.sec.android.app.hwmoduletest.modules;

import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;

public class SensorCalculator {
    private static final String CLASS_NAME = "SensorCalculator";
    private static final boolean DEBUG = true;
    private static int RANGE_MAX_INTEGER = Integer.MAX_VALUE;
    private static int RANGE_MIN_INTEGER = Integer.MIN_VALUE;
    private static String RESULT_VALUE_NG = "NG";
    private static String RESULT_VALUE_NOTSUPPORTED = ModuleSensor.RETURN_DATA_ARRAY_INDEX_1_NONE;
    private static String RESULT_VALUE_OK = "OK";
    private static Accelerometer mAccelerometer = null;
    private static String mFeature_Accelerometer;
    private static String mFeature_Magnetic;
    private static Range mRangeMagneticADC = null;
    private static Range mRangeMagneticADC2 = null;
    private static Range mRangeMagneticDAC = null;
    private static Range mRangeMagneticSelf = null;

    private static class Accelerometer {
        private int mBitCount;
        private int mFlatness_Max;
        private int mFlatness_Min;
        private boolean mIsSupport_INT_Pin;
        /* access modifiers changed from: private */
        public Range mRange = null;

        public Accelerometer(int bitCount, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, int flatnessMin, int flatnessMax, boolean isSupport_INT_Pin) {
            this.mBitCount = bitCount;
            Range range = new Range(xMin, xMax, yMin, yMax, zMin, zMax);
            this.mRange = range;
            this.mFlatness_Min = flatnessMin;
            this.mFlatness_Max = flatnessMax;
            this.mIsSupport_INT_Pin = isSupport_INT_Pin;
        }
    }

    private static class Range {
        /* access modifiers changed from: private */
        public boolean mIsSupportRange1_X;
        /* access modifiers changed from: private */
        public boolean mIsSupportRange1_Y;
        /* access modifiers changed from: private */
        public boolean mIsSupportRange1_Z;
        /* access modifiers changed from: private */
        public boolean mIsSupportRange2_X;
        /* access modifiers changed from: private */
        public boolean mIsSupportRange2_Y;
        /* access modifiers changed from: private */
        public boolean mIsSupportRange2_Z;
        /* access modifiers changed from: private */
        public int mRange1_X_Max;
        /* access modifiers changed from: private */
        public int mRange1_X_Min;
        /* access modifiers changed from: private */
        public int mRange1_Y_Max;
        /* access modifiers changed from: private */
        public int mRange1_Y_Min;
        /* access modifiers changed from: private */
        public int mRange1_Z_Max;
        /* access modifiers changed from: private */
        public int mRange1_Z_Min;
        /* access modifiers changed from: private */
        public int mRange2_X_Max;
        /* access modifiers changed from: private */
        public int mRange2_X_Min;
        /* access modifiers changed from: private */
        public int mRange2_Y_Max;
        /* access modifiers changed from: private */
        public int mRange2_Y_Min;
        /* access modifiers changed from: private */
        public int mRange2_Z_Max;
        /* access modifiers changed from: private */
        public int mRange2_Z_Min;
        /* access modifiers changed from: private */
        public int mRangeCount = 1;

        public Range(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
            this.mRange1_X_Min = xMin;
            this.mRange1_X_Max = xMax;
            this.mRange1_Y_Min = yMin;
            this.mRange1_Y_Max = yMax;
            this.mRange1_Z_Min = zMin;
            this.mRange1_Z_Max = zMax;
            if (this.mRange1_X_Min != this.mRange1_X_Max) {
                this.mIsSupportRange1_X = SensorCalculator.DEBUG;
            }
            if (this.mRange1_Y_Min != this.mRange1_Y_Max) {
                this.mIsSupportRange1_Y = SensorCalculator.DEBUG;
            }
            if (this.mRange1_Z_Min != this.mRange1_Z_Max) {
                this.mIsSupportRange1_Z = SensorCalculator.DEBUG;
            }
        }

        public Range(int range1_X_Min, int range1_X_Max, int range1_Y_Min, int range1_Y_Max, int range1_Z_Min, int range1_Z_Max, int range2_X_Min, int range2_X_Max, int range2_Y_Min, int range2_Y_Max, int range2_Z_Min, int range2_Z_Max) {
            this(range1_X_Min, range1_X_Max, range1_Y_Min, range1_Y_Max, range1_Z_Min, range1_Z_Max);
            this.mRange2_X_Min = range2_X_Min;
            this.mRange2_X_Max = range2_X_Max;
            this.mRange2_Y_Min = range2_Y_Min;
            this.mRange2_Y_Max = range2_Y_Max;
            this.mRange2_Z_Min = range2_Z_Min;
            this.mRange2_Z_Max = range2_Z_Max;
            if (this.mRange2_X_Min != this.mRange2_X_Max) {
                this.mIsSupportRange2_X = SensorCalculator.DEBUG;
            }
            if (this.mRange2_Y_Min != this.mRange2_Y_Max) {
                this.mIsSupportRange2_Y = SensorCalculator.DEBUG;
            }
            if (this.mRange2_Z_Min != this.mRange2_Z_Max) {
                this.mIsSupportRange2_Z = SensorCalculator.DEBUG;
            }
        }
    }

    public static void initialize() {
        mFeature_Accelerometer = Kernel.read(Kernel.ACCEL_SENSOR_NAME);
        mFeature_Magnetic = Kernel.getGeoMagneticSensorName();
        StringBuilder sb = new StringBuilder();
        sb.append("mFeature_Accelerometer : ");
        sb.append(mFeature_Accelerometer);
        LtUtil.log_d(CLASS_NAME, "initialize", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mFeature_Magnetic : ");
        sb2.append(mFeature_Magnetic);
        LtUtil.log_d(CLASS_NAME, "initialize", sb2.toString());
        setSpecAccel();
        setSpecMagnetic();
    }

    public static Range getmRangeMagneticDAC() {
        return mRangeMagneticDAC;
    }

    public static Range getmRangeMagneticADC() {
        return mRangeMagneticADC;
    }

    public static Range getmRangeMagneticADC2() {
        return mRangeMagneticADC2;
    }

    public static Range getmRangeMagneticSelf() {
        return mRangeMagneticSelf;
    }

    private static void setSpecAccel() {
        if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_SMB380)) {
            Accelerometer accelerometer = new Accelerometer(10, -52, 52, -52, 52, 192, TestResultParser.TEST_TOKEN_FLATNESS_BAD_PIXEL_NUM, 0, 24, DEBUG);
            mAccelerometer = accelerometer;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMA250)) {
            Accelerometer accelerometer2 = new Accelerometer(10, -52, 52, -52, 52, 190, 322, 0, 24, DEBUG);
            mAccelerometer = accelerometer2;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMA022)) {
            Accelerometer accelerometer3 = new Accelerometer(10, -52, 52, -52, 52, 185, 327, 0, 24, DEBUG);
            mAccelerometer = accelerometer3;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMA023)) {
            Accelerometer accelerometer4 = new Accelerometer(10, -56, 56, -56, 56, 181, 331, 0, 24, DEBUG);
            mAccelerometer = accelerometer4;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMA222)) {
            Accelerometer accelerometer5 = new Accelerometer(8, -15, 15, -15, 15, 44, 84, 0, 6, DEBUG);
            mAccelerometer = accelerometer5;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMA220)) {
            Accelerometer accelerometer6 = new Accelerometer(6, -6, 6, -6, 6, 9, 23, 0, 0, false);
            mAccelerometer = accelerometer6;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_BOSCH_BMI168)) {
            Accelerometer accelerometer7 = new Accelerometer(12, -989, 989, -661, 661, 3199, 4993, 0, 100, DEBUG);
            mAccelerometer = accelerometer7;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_STMICRO_KR3DH)) {
            Accelerometer accelerometer8 = new Accelerometer(12, -102, 102, -102, 102, 849, 1199, 0, 100, DEBUG);
            mAccelerometer = accelerometer8;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_STMICRO_K3DH)) {
            Accelerometer accelerometer9 = new Accelerometer(12, -154, 154, -154, 154, 798, 1250, 0, 100, DEBUG);
            mAccelerometer = accelerometer9;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_STMICRO_KR3DM)) {
            Accelerometer accelerometer10 = new Accelerometer(8, -12, 12, -12, 12, 48, 80, 0, 6, DEBUG);
            mAccelerometer = accelerometer10;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_STMICRO_LSM330DLC)) {
            Accelerometer accelerometer11 = new Accelerometer(12, -154, 154, -154, 154, 798, 1250, 0, 100, false);
            mAccelerometer = accelerometer11;
        } else if (mFeature_Accelerometer.contains(ModuleSensor.FEATURE_ACCELEROMETER_STMICRO_K6DS3)) {
            Accelerometer accelerometer12 = new Accelerometer(16, -1476, 1476, -1476, 1476, 6475, 9919, 0, 1600, DEBUG);
            mAccelerometer = accelerometer12;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_KIONIX_KXUD9)) {
            Accelerometer accelerometer13 = new Accelerometer(12, -151, 151, -151, 151, 642, 996, 0, 100, DEBUG);
            mAccelerometer = accelerometer13;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_KIONIX_KXTF9)) {
            Accelerometer accelerometer14 = new Accelerometer(12, -133, 133, -133, 133, 846, TestResultParser.TEST_TOKEN_CHIP_SUPPORT_BIO, 0, 100, DEBUG);
            mAccelerometer = accelerometer14;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_INVENSENSE_MPU6050) || mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_INVENSENSE_MPU6051)) {
            Accelerometer accelerometer15 = new Accelerometer(16, -2054, 2054, -2054, 2054, 13302, 19466, 0, 1600, DEBUG);
            mAccelerometer = accelerometer15;
        } else if (mFeature_Accelerometer.equals(ModuleSensor.FEATURE_ACCELEROMETER_INVENSENSE_MPU6515M)) {
            Accelerometer accelerometer16 = new Accelerometer(16, -2054, 2054, -2054, 2054, 13302, 19466, 0, 1600, DEBUG);
            mAccelerometer = accelerometer16;
        } else {
            mAccelerometer = null;
        }
        specLog(mAccelerometer, "AccelSelf");
    }

    private static void setSpecMagnetic() {
        if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963)) {
            mRangeMagneticDAC = null;
            Range range = new Range(-6500, 6500, -6500, 6500, -6500, 6500);
            mRangeMagneticADC = range;
            mRangeMagneticADC2 = null;
            Range range2 = new Range(-200, 200, -200, 200, -3200, -800);
            mRangeMagneticSelf = range2;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C)) {
            mRangeMagneticDAC = null;
            Range range3 = new Range(-6500, 6500, -6500, 6500, -6500, 6500);
            mRangeMagneticADC = range3;
            mRangeMagneticADC2 = null;
            Range range4 = new Range(-200, 200, -200, 200, -3200, -800);
            mRangeMagneticSelf = range4;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C_MANAGER)) {
            mRangeMagneticDAC = null;
            Range range5 = new Range(-6500, 6500, -6500, 6500, -6500, 6500);
            mRangeMagneticADC = range5;
            mRangeMagneticADC2 = null;
            Range range6 = new Range(-200, 200, -200, 200, -3200, -800);
            mRangeMagneticSelf = range6;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK8973)) {
            Range range7 = new Range(0, 126, 0, 126, 0, 126, 128, 254, 128, 254, 128, 254);
            mRangeMagneticDAC = range7;
            Range range8 = new Range(88, 168, 88, 168, 88, 168);
            mRangeMagneticADC = range8;
            mRangeMagneticADC2 = null;
            mRangeMagneticSelf = null;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK8975)) {
            mRangeMagneticDAC = null;
            Range range9 = new Range(-2000, 2000, -2000, 2000, -2000, 2000);
            mRangeMagneticADC = range9;
            mRangeMagneticADC2 = null;
            Range range10 = new Range(-100, 100, -100, 100, -1000, -300);
            mRangeMagneticSelf = range10;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C)) {
            mRangeMagneticDAC = null;
            Range range11 = new Range(-1600, 1600, -1600, 1600, -1600, 1600);
            mRangeMagneticADC = range11;
            mRangeMagneticADC2 = null;
            Range range12 = new Range(-30, 30, -30, 30, -400, -50);
            mRangeMagneticSelf = range12;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
            mRangeMagneticDAC = null;
            Range range13 = new Range(-6500, 6500, -6500, 6500, -6500, 6500);
            mRangeMagneticADC = range13;
            mRangeMagneticADC2 = null;
            Range range14 = new Range(-30, 30, -30, 30, -400, -50);
            mRangeMagneticSelf = range14;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS529)) {
            Range range15 = new Range(5, 32, 5, 32, 5, 32);
            mRangeMagneticDAC = range15;
            Range range16 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range16;
            mRangeMagneticADC2 = null;
            Range range17 = new Range(80, RANGE_MAX_INTEGER, 107, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range17;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530)) {
            Range range18 = new Range(-30, 30, -30, 30, -30, 30);
            mRangeMagneticDAC = range18;
            Range range19 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range19;
            mRangeMagneticADC2 = null;
            Range range20 = new Range(133, RANGE_MAX_INTEGER, 160, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range20;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530A)) {
            Range range21 = new Range(-30, 30, -30, 30, -30, 30);
            mRangeMagneticDAC = range21;
            Range range22 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range22;
            mRangeMagneticADC2 = null;
            Range range23 = new Range(133, RANGE_MAX_INTEGER, 160, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range23;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530C)) {
            Range range24 = new Range(-30, 30, -30, 30, -30, 30);
            mRangeMagneticDAC = range24;
            Range range25 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range25;
            Range range26 = new Range(-400, TestResultParser.TEST_TOKEN_GET_DR_TIMESTAMP_TIME, -400, TestResultParser.TEST_TOKEN_GET_DR_TIMESTAMP_TIME, -400, TestResultParser.TEST_TOKEN_GET_DR_TIMESTAMP_TIME);
            mRangeMagneticADC2 = range26;
            Range range27 = new Range(133, RANGE_MAX_INTEGER, 160, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range27;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532)) {
            Range range28 = new Range(-30, 30, -30, 30, -30, 30);
            mRangeMagneticDAC = range28;
            Range range29 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range29;
            Range range30 = new Range(-600, TestResultParser.TEST_TOKEN_RESET_FLAG, -600, TestResultParser.TEST_TOKEN_RESET_FLAG, -600, TestResultParser.TEST_TOKEN_RESET_FLAG);
            mRangeMagneticADC2 = range30;
            Range range31 = new Range(17, RANGE_MAX_INTEGER, 22, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range31;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS537)) {
            mRangeMagneticDAC = null;
            mRangeMagneticADC = null;
            Range range32 = new Range(-1000, 1000, -1000, 1000, -1000, 1000);
            mRangeMagneticADC2 = range32;
            Range range33 = new Range(24, RANGE_MAX_INTEGER, 31, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range33;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
            mRangeMagneticDAC = null;
            mRangeMagneticADC = null;
            Range range34 = new Range(-1000, 1000, -1000, 1000, -1000, 1000);
            mRangeMagneticADC2 = range34;
            Range range35 = new Range(24, RANGE_MAX_INTEGER, 31, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range35;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532B)) {
            Range range36 = new Range(-30, 30, -30, 30, -30, 30);
            mRangeMagneticDAC = range36;
            Range range37 = new Range(0, 359, 0, 0, 0, 0);
            mRangeMagneticADC = range37;
            Range range38 = new Range(-600, TestResultParser.TEST_TOKEN_RESET_FLAG, -600, TestResultParser.TEST_TOKEN_RESET_FLAG, -600, TestResultParser.TEST_TOKEN_RESET_FLAG);
            mRangeMagneticADC2 = range38;
            Range range39 = new Range(44, RANGE_MAX_INTEGER, 58, RANGE_MAX_INTEGER, 0, 0);
            mRangeMagneticSelf = range39;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150)) {
            mRangeMagneticDAC = null;
            Range range40 = new Range(-2048, 2048, -2048, 2048, -8192, 8192);
            mRangeMagneticADC = range40;
            mRangeMagneticADC2 = null;
            Range range41 = new Range(0, 0, 0, 0, 2880, 3840);
            mRangeMagneticSelf = range41;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE)) {
            mRangeMagneticDAC = null;
            Range range42 = new Range(-10400, 10400, -10400, 10400, -20000, 20000);
            mRangeMagneticADC = range42;
            mRangeMagneticADC2 = null;
            Range range43 = new Range(0, 0, 0, 0, 2880, 3840);
            mRangeMagneticSelf = range43;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION)) {
            mRangeMagneticDAC = null;
            Range range44 = new Range(-10400, 10400, -10400, 10400, -20000, 20000);
            mRangeMagneticADC = range44;
            mRangeMagneticADC2 = null;
            Range range45 = new Range(0, 0, 0, 0, 2880, 3840);
            mRangeMagneticSelf = range45;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO)) {
            mRangeMagneticDAC = null;
            Range range46 = new Range(-16384, 16384, -16384, 16384, -16384, 16384);
            mRangeMagneticADC = range46;
            mRangeMagneticADC2 = null;
            Range range47 = new Range(-5133, -1711, -5133, -1711, -1711, -171);
            mRangeMagneticSelf = range47;
        } else if (mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
            mRangeMagneticDAC = null;
            Range range48 = new Range(-16384, 16384, -16384, 16384, -16384, 16384);
            mRangeMagneticADC = range48;
            mRangeMagneticADC2 = null;
            Range range49 = new Range(10, 333, 10, 333, 10, 333);
            mRangeMagneticSelf = range49;
        } else {
            mRangeMagneticDAC = null;
            mRangeMagneticADC = null;
            mRangeMagneticADC2 = null;
            mRangeMagneticSelf = null;
        }
        String spec_adc = Spec.getString(Spec.GEOMAGNETIC_SPEC_ADC);
        if (!"-1".equals(spec_adc)) {
            String[] specArray = spec_adc.split(",");
            Range range50 = new Range(Integer.parseInt(specArray[0]), Integer.parseInt(specArray[1]), Integer.parseInt(specArray[2]), Integer.parseInt(specArray[3]), Integer.parseInt(specArray[4]), Integer.parseInt(specArray[5]));
            mRangeMagneticADC = range50;
        }
        specLog(mRangeMagneticDAC, "MagneticDAC");
        specLog(mRangeMagneticADC, "MagneticADC");
        specLog(mRangeMagneticADC2, "MagneticADC2");
        specLog(mRangeMagneticSelf, "MagneticSelf");
    }

    public static String[] accelerometerAngle(int[] data) {
        float realg = (float) Math.sqrt((double) ((data[0] * data[0]) + (data[1] * data[1]) + (data[2] * data[2])));
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((int) (((float) Math.asin((double) (((float) data[0]) / realg))) * 57.29578f));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append((int) (((float) Math.asin((double) (((float) data[1]) / realg))) * 57.29578f));
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append((int) (((((float) Math.acos((double) (((float) data[2]) / realg))) * 57.29578f) - 90.0f) * -1.0f));
        return new String[]{sb.toString(), sb2.toString(), sb3.toString()};
    }

    public static String getResultAccelerometerSelf(int x, int y, int z) {
        String returnValue;
        String returnValue2;
        String returnValue3;
        StringBuilder sb = new StringBuilder();
        sb.append("Feature : ");
        sb.append(mFeature_Accelerometer);
        LtUtil.log_d(CLASS_NAME, "getResultAccelerometerSelf", sb.toString());
        specLog(mAccelerometer, "AccelSelf");
        String returnValue4 = "";
        if (mAccelerometer != null) {
            if (mAccelerometer.mRange.mIsSupportRange1_X) {
                if (x < mAccelerometer.mRange.mRange1_X_Min || mAccelerometer.mRange.mRange1_X_Max < x) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(returnValue4);
                    sb2.append("F");
                    returnValue = sb2.toString();
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(returnValue4);
                    sb3.append("P");
                    returnValue = sb3.toString();
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(mAccelerometer.mRange.mRange1_X_Min);
                sb4.append(" [");
                sb4.append(x);
                sb4.append("] ");
                sb4.append(mAccelerometer.mRange.mRange1_X_Max);
                LtUtil.log_i(CLASS_NAME, "getResultAccelerometerSelf", sb4.toString());
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(returnValue4);
                sb5.append("P");
                returnValue = sb5.toString();
            }
            if (mAccelerometer.mRange.mIsSupportRange1_Y) {
                if (y < mAccelerometer.mRange.mRange1_Y_Min || mAccelerometer.mRange.mRange1_Y_Max < y) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(returnValue);
                    sb6.append("F");
                    returnValue2 = sb6.toString();
                } else {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(returnValue);
                    sb7.append("P");
                    returnValue2 = sb7.toString();
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append("");
                sb8.append(mAccelerometer.mRange.mRange1_Y_Min);
                sb8.append(" [");
                sb8.append(y);
                sb8.append("] ");
                sb8.append(mAccelerometer.mRange.mRange1_Y_Max);
                LtUtil.log_i(CLASS_NAME, "getResultAccelerometerSelf", sb8.toString());
            } else {
                StringBuilder sb9 = new StringBuilder();
                sb9.append(returnValue);
                sb9.append("P");
                returnValue2 = sb9.toString();
            }
            if (mAccelerometer.mRange.mIsSupportRange1_Z) {
                if (z < mAccelerometer.mRange.mRange1_Z_Min || mAccelerometer.mRange.mRange1_Z_Max < z) {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(returnValue2);
                    sb10.append("F");
                    returnValue3 = sb10.toString();
                } else {
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(returnValue2);
                    sb11.append("P");
                    returnValue3 = sb11.toString();
                }
                StringBuilder sb12 = new StringBuilder();
                sb12.append("");
                sb12.append(mAccelerometer.mRange.mRange1_Z_Min);
                sb12.append(" [");
                sb12.append(z);
                sb12.append("] ");
                sb12.append(mAccelerometer.mRange.mRange1_Z_Max);
                LtUtil.log_i(CLASS_NAME, "getResultAccelerometerSelf", sb12.toString());
            } else {
                StringBuilder sb13 = new StringBuilder();
                sb13.append(returnValue2);
                sb13.append("P");
                returnValue3 = sb13.toString();
            }
            LtUtil.log_i(CLASS_NAME, "getResultAccelerometerSelf", returnValue3);
            return returnValue3;
        }
        LtUtil.log_e(CLASS_NAME, "getResultAccelerometerSelf", "FFF - Spec null");
        return "FFF";
    }

    private static boolean isSpecIn(int value, int rangeMin, int rangeMax) {
        if (rangeMin > value || value > rangeMax) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fail => ");
            sb.append(rangeMin);
            sb.append(" [");
            sb.append(value);
            sb.append("] ");
            sb.append(rangeMax);
            LtUtil.log_e(CLASS_NAME, "isSpecIn", sb.toString());
            return false;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Pass => ");
        sb2.append(rangeMin);
        sb2.append(" [");
        sb2.append(value);
        sb2.append("] ");
        sb2.append(rangeMax);
        LtUtil.log_i(CLASS_NAME, "isSpecIn", sb2.toString());
        return DEBUG;
    }

    private static String getResult(int x, int y, int z, Range range) {
        if (range == null) {
            LtUtil.log_e(CLASS_NAME, "getResult", "Fail - Spec null");
            return RESULT_VALUE_NOTSUPPORTED;
        } else if (range.mRangeCount == 1) {
            LtUtil.log_i(CLASS_NAME, "getResult", "mRangeCount : 1");
            if (range.mIsSupportRange1_X && !isSpecIn(x, range.mRange1_X_Min, range.mRange1_X_Max)) {
                LtUtil.log_e(CLASS_NAME, "getResult", "Fail - X");
                return RESULT_VALUE_NG;
            } else if (range.mIsSupportRange1_Y && !isSpecIn(y, range.mRange1_Y_Min, range.mRange1_Y_Max)) {
                LtUtil.log_e(CLASS_NAME, "getResult", "Fail - Y");
                return RESULT_VALUE_NG;
            } else if (!range.mIsSupportRange1_Z || isSpecIn(z, range.mRange1_Z_Min, range.mRange1_Z_Max)) {
                return RESULT_VALUE_OK;
            } else {
                LtUtil.log_e(CLASS_NAME, "getResult", "Fail - Z");
                return RESULT_VALUE_NG;
            }
        } else if (range.mRangeCount == 2) {
            LtUtil.log_i(CLASS_NAME, "getResult", "mRangeCount : 2");
            if (!range.mIsSupportRange1_X || !range.mIsSupportRange2_X) {
                if (!range.mIsSupportRange1_X || range.mIsSupportRange2_X) {
                    if (!range.mIsSupportRange1_X && range.mIsSupportRange2_X && !isSpecIn(x, range.mRange2_X_Min, range.mRange2_X_Max)) {
                        LtUtil.log_e(CLASS_NAME, "getResult", "[Range2] Fail - X");
                        return RESULT_VALUE_NG;
                    }
                } else if (!isSpecIn(x, range.mRange1_X_Min, range.mRange1_X_Max)) {
                    LtUtil.log_e(CLASS_NAME, "getResult", "[Range1] Fail - X");
                    return RESULT_VALUE_NG;
                }
            } else if (!isSpecIn(x, range.mRange1_X_Min, range.mRange1_X_Max) && !isSpecIn(x, range.mRange2_X_Min, range.mRange2_X_Max)) {
                LtUtil.log_e(CLASS_NAME, "getResult", "[All] Fail - X");
                return RESULT_VALUE_NG;
            }
            if (!range.mIsSupportRange1_Y || !range.mIsSupportRange2_Y) {
                if (!range.mIsSupportRange1_Y || range.mIsSupportRange2_Y) {
                    if (!range.mIsSupportRange1_Y && range.mIsSupportRange2_Y && !isSpecIn(y, range.mRange2_Y_Min, range.mRange2_Y_Max)) {
                        LtUtil.log_e(CLASS_NAME, "getResult", "[Range2] Fail - Y");
                        return RESULT_VALUE_NG;
                    }
                } else if (!isSpecIn(y, range.mRange1_Y_Min, range.mRange1_Y_Max)) {
                    LtUtil.log_e(CLASS_NAME, "getResult", "[Range1] Fail - Y");
                    return RESULT_VALUE_NG;
                }
            } else if (!isSpecIn(y, range.mRange1_Y_Min, range.mRange1_Y_Max) && !isSpecIn(y, range.mRange2_Y_Min, range.mRange2_Y_Max)) {
                LtUtil.log_e(CLASS_NAME, "getResult", "[All] Fail - Y");
                return RESULT_VALUE_NG;
            }
            if (!range.mIsSupportRange1_Z || !range.mIsSupportRange2_Z) {
                if (!range.mIsSupportRange1_Z || range.mIsSupportRange2_Z) {
                    if (!range.mIsSupportRange1_Z && range.mIsSupportRange2_Z && !isSpecIn(z, range.mRange2_Z_Min, range.mRange2_Z_Max)) {
                        LtUtil.log_e(CLASS_NAME, "getResult", "[Range2] Fail - Z");
                        return RESULT_VALUE_NG;
                    }
                } else if (!isSpecIn(z, range.mRange1_Z_Min, range.mRange1_Z_Max)) {
                    LtUtil.log_e(CLASS_NAME, "getResult", "[Range1] Fail - Z");
                    return RESULT_VALUE_NG;
                }
            } else if (!isSpecIn(z, range.mRange1_Z_Min, range.mRange1_Z_Max) && !isSpecIn(z, range.mRange2_Z_Min, range.mRange2_Z_Max)) {
                LtUtil.log_e(CLASS_NAME, "getResult", "[All] Fail - Z");
                return RESULT_VALUE_NG;
            }
            return RESULT_VALUE_OK;
        } else {
            LtUtil.log_e(CLASS_NAME, "getResult", "mRangeCount : Unknown");
            return RESULT_VALUE_NOTSUPPORTED;
        }
    }

    public static String checkSpecMagneticDAC(int x, int y, int z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature : ");
        sb.append(mFeature_Magnetic);
        LtUtil.log_d(CLASS_NAME, "checkSpecMagneticDAC", sb.toString());
        specLog(mRangeMagneticDAC, "MagneticDAC");
        return getResult(x, y, z, mRangeMagneticDAC);
    }

    public static String checkSpecMagneticADC(int x, int y, int z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature : ");
        sb.append(mFeature_Magnetic);
        LtUtil.log_d(CLASS_NAME, "checkSpecMagneticADC", sb.toString());
        specLog(mRangeMagneticADC, "MagneticADC");
        return getResult(x, y, z, mRangeMagneticADC);
    }

    public static String checkSpecMagneticADC2(int x, int y, int z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature : ");
        sb.append(mFeature_Magnetic);
        LtUtil.log_d(CLASS_NAME, "checkSpecMagneticADC2", sb.toString());
        specLog(mRangeMagneticADC2, "MagneticADC2");
        return getResult(x, y, z, mRangeMagneticADC2);
    }

    public static String checkSpecMagneticSelf(int x, int y, int z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature : ");
        sb.append(mFeature_Magnetic);
        LtUtil.log_d(CLASS_NAME, "checkSpecMagneticSelf", sb.toString());
        specLog(mRangeMagneticSelf, "MagneticSelf");
        return getResult(x, y, z, mRangeMagneticSelf);
    }

    private static void specLog(Range data, String name) {
        String message;
        String message2;
        String message3;
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(name);
        sb.append("> ");
        String message4 = sb.toString();
        if (data != null) {
            if (data.mIsSupportRange1_X) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(message4);
                sb2.append("X(");
                sb2.append(data.mRange1_X_Min);
                sb2.append(",");
                sb2.append(data.mRange1_X_Max);
                sb2.append(") , ");
                message2 = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(message4);
                sb3.append("X(not supported) , ");
                message2 = sb3.toString();
            }
            if (data.mIsSupportRange1_Y) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(message2);
                sb4.append("Y(");
                sb4.append(data.mRange1_Y_Min);
                sb4.append(",");
                sb4.append(data.mRange1_Y_Max);
                sb4.append(") , ");
                message3 = sb4.toString();
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(message2);
                sb5.append("Y(not supported) , ");
                message3 = sb5.toString();
            }
            if (data.mIsSupportRange1_Z) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(message3);
                sb6.append("Z(");
                sb6.append(data.mRange1_Z_Min);
                sb6.append(",");
                sb6.append(data.mRange1_Z_Max);
                sb6.append(")");
                message = sb6.toString();
            } else {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(message3);
                sb7.append("Z(not supported)");
                message = sb7.toString();
            }
        } else {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("<");
            sb8.append(name);
            sb8.append("> null");
            message = sb8.toString();
        }
        LtUtil.log_d(CLASS_NAME, "specLog", message);
    }

    private static void specLog(Accelerometer data, String name) {
        if (data != null) {
            specLog(data.mRange, name);
        } else {
            specLog((Range) null, name);
        }
    }
}
