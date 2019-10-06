package com.sec.android.app.hwmoduletest.modules;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import egis.client.api.EgisFingerprint;

public class SensorReadManager {
    public static final int MAGNETIC_DATA_NUMBER_ADC = 5;
    public static final int MAGNETIC_DATA_NUMBER_ALL = 8;
    public static final int MAGNETIC_DATA_NUMBER_DAC = 4;
    public static final int MAGNETIC_DATA_NUMBER_OFFSET_H = 7;
    public static final int MAGNETIC_DATA_NUMBER_POWER_OFF = 2;
    public static final int MAGNETIC_DATA_NUMBER_POWER_ON = 1;
    public static final int MAGNETIC_DATA_NUMBER_SELF = 6;
    public static final int MAGNETIC_DATA_NUMBER_STATUS = 3;
    public static final int MAGNETIC_DATA_NUMBER_TEMP = 2;
    private final String CLASS_NAME = "SensorReadManager";
    private final boolean DEBUG = true;
    private int DUMMY;
    private final int RETURN_DATA_ARRAY_SIZE_MAX;
    private final int SENSOR_ON_ARRAY_INDEX_ACCELEROMETER;
    private final int SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_N_ANGLE;
    private final int SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_SELF;
    private final int SENSOR_ON_ARRAY_INDEX_BAROMETER;
    private final int SENSOR_ON_ARRAY_INDEX_GYRO;
    private final int SENSOR_ON_ARRAY_INDEX_GYRO_EXPANSION;
    private final int SENSOR_ON_ARRAY_INDEX_GYRO_POWER;
    private final int SENSOR_ON_ARRAY_INDEX_GYRO_SELF;
    private final int SENSOR_ON_ARRAY_INDEX_GYRO_TEMPERATURE;
    private final int SENSOR_ON_ARRAY_INDEX_LIGHT;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_ADC;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_DAC;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_OFFSETH;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_OFF;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_ON;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_SELF;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_STATUS;
    private final int SENSOR_ON_ARRAY_INDEX_MAGNETIC_TEMPERATURE;
    private final int SENSOR_ON_ARRAY_INDEX_PROXIMITY;
    private final int SENSOR_ON_ARRAY_LENGTH;
    private Sensor mAccelerometerSensor;
    private Sensor mBarometerSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mLightSensor;
    private Sensor mMagneticSensor;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Accelerometer;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Barometer;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Gyro;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Light;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Magnetic;
    /* access modifiers changed from: private */
    public float[] mOriginalData_Proximity;
    private Sensor mProximitySensor;
    private String[] mReturnData;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager;
    private boolean[] mSensorOn;

    public static class GyroExpansionData {
        public short[] mData = new short[3];
        public float[] mNoiseBias = new float[3];
        public float[] mRMSValue = new float[3];
        public int mReturnValue;
    }

    public static class MagneticExpansionData {
        public String[] mADC = null;
        public String[] mADC2 = null;
        public String[] mDAC = null;
        public String[] mOffset_H = null;
        public String[] mPowerOff = null;
        public String[] mPowerOn = null;
        public int mReturnValue;
        public String[] mSelf = null;
        public String[] mStatus = null;
        public String[] mTemperature = null;
    }

    private class SensorListener implements SensorEventListener {
        private SensorListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case 1:
                    SensorReadManager.this.mOriginalData_Accelerometer = (float[]) event.values.clone();
                    return;
                case 2:
                    SensorReadManager.this.mOriginalData_Magnetic = (float[]) event.values.clone();
                    return;
                case 4:
                    SensorReadManager.this.mOriginalData_Gyro = (float[]) event.values.clone();
                    return;
                case 5:
                    SensorReadManager.this.mOriginalData_Light = (float[]) event.values.clone();
                    return;
                case 6:
                    SensorReadManager.this.mOriginalData_Barometer = (float[]) event.values.clone();
                    return;
                case 8:
                case 65592:
                    SensorReadManager.this.mOriginalData_Proximity = (float[]) event.values.clone();
                    return;
                default:
                    return;
            }
        }
    }

    public SensorReadManager(int[] sensorID, SensorManager sm) {
        this.DUMMY = 0;
        int i = this.DUMMY;
        this.DUMMY = i + 1;
        this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER = i;
        int i2 = this.DUMMY;
        this.DUMMY = i2 + 1;
        this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_N_ANGLE = i2;
        int i3 = this.DUMMY;
        this.DUMMY = i3 + 1;
        this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_SELF = i3;
        int i4 = this.DUMMY;
        this.DUMMY = i4 + 1;
        this.SENSOR_ON_ARRAY_INDEX_BAROMETER = i4;
        int i5 = this.DUMMY;
        this.DUMMY = i5 + 1;
        this.SENSOR_ON_ARRAY_INDEX_GYRO = i5;
        int i6 = this.DUMMY;
        this.DUMMY = i6 + 1;
        this.SENSOR_ON_ARRAY_INDEX_GYRO_EXPANSION = i6;
        int i7 = this.DUMMY;
        this.DUMMY = i7 + 1;
        this.SENSOR_ON_ARRAY_INDEX_GYRO_POWER = i7;
        int i8 = this.DUMMY;
        this.DUMMY = i8 + 1;
        this.SENSOR_ON_ARRAY_INDEX_GYRO_SELF = i8;
        int i9 = this.DUMMY;
        this.DUMMY = i9 + 1;
        this.SENSOR_ON_ARRAY_INDEX_GYRO_TEMPERATURE = i9;
        int i10 = this.DUMMY;
        this.DUMMY = i10 + 1;
        this.SENSOR_ON_ARRAY_INDEX_LIGHT = i10;
        int i11 = this.DUMMY;
        this.DUMMY = i11 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC = i11;
        int i12 = this.DUMMY;
        this.DUMMY = i12 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_ON = i12;
        int i13 = this.DUMMY;
        this.DUMMY = i13 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_STATUS = i13;
        int i14 = this.DUMMY;
        this.DUMMY = i14 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_TEMPERATURE = i14;
        int i15 = this.DUMMY;
        this.DUMMY = i15 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_DAC = i15;
        int i16 = this.DUMMY;
        this.DUMMY = i16 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_ADC = i16;
        int i17 = this.DUMMY;
        this.DUMMY = i17 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_SELF = i17;
        int i18 = this.DUMMY;
        this.DUMMY = i18 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_OFFSETH = i18;
        int i19 = this.DUMMY;
        this.DUMMY = i19 + 1;
        this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_OFF = i19;
        int i20 = this.DUMMY;
        this.DUMMY = i20 + 1;
        this.SENSOR_ON_ARRAY_INDEX_PROXIMITY = i20;
        this.SENSOR_ON_ARRAY_LENGTH = this.DUMMY;
        this.mSensorOn = new boolean[this.SENSOR_ON_ARRAY_LENGTH];
        this.mSensorManager = null;
        this.mSensorListener = null;
        this.mAccelerometerSensor = null;
        this.mBarometerSensor = null;
        this.mGyroscopeSensor = null;
        this.mLightSensor = null;
        this.mMagneticSensor = null;
        this.mProximitySensor = null;
        this.mOriginalData_Accelerometer = null;
        this.mOriginalData_Barometer = null;
        this.mOriginalData_Gyro = null;
        this.mOriginalData_Light = null;
        this.mOriginalData_Magnetic = null;
        this.mOriginalData_Proximity = null;
        this.RETURN_DATA_ARRAY_SIZE_MAX = 16;
        this.mReturnData = new String[16];
        LtUtil.log_i("SensorReadManager", "SensorReadManager", "Sensor On");
        for (int i21 = 0; i21 < this.SENSOR_ON_ARRAY_LENGTH; i21++) {
            this.mSensorOn[i21] = false;
        }
        this.mSensorManager = sm;
        if (this.mSensorManager != null) {
            for (int i22 = 0; i22 < sensorID.length; i22++) {
                if (this.mSensorListener == null) {
                    this.mSensorListener = new SensorListener();
                }
                if (sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER || sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER_N_ANGLE || sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER_SELF) {
                    if (this.mAccelerometerSensor == null) {
                        this.mAccelerometerSensor = this.mSensorManager.getDefaultSensor(1);
                        this.mSensorManager.registerListener(this.mSensorListener, this.mAccelerometerSensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-AccelerometerSensor");
                    }
                    if (this.mAccelerometerSensor != null) {
                        if (sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER) {
                            this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_ACCELEROMETER)] = true;
                        } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER_N_ANGLE) {
                            this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_ACCELEROMETER_N_ANGLE)] = true;
                        } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_ACCELEROMETER_SELF) {
                            this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_ACCELEROMETER_SELF)] = true;
                        }
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_BAROMETER) {
                    if (this.mBarometerSensor == null) {
                        this.mBarometerSensor = this.mSensorManager.getDefaultSensor(6);
                        this.mSensorManager.registerListener(this.mSensorListener, this.mBarometerSensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-BarometerSensor");
                    }
                    if (this.mBarometerSensor != null) {
                        this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_BAROMETER)] = true;
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_GYRO) {
                    if (this.mGyroscopeSensor == null) {
                        this.mGyroscopeSensor = this.mSensorManager.getDefaultSensor(4);
                        this.mSensorManager.registerListener(this.mSensorListener, this.mGyroscopeSensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-GyroscopeSensor");
                    }
                    if (this.mGyroscopeSensor != null) {
                        this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_GYRO)] = true;
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_LIGHT) {
                    if (this.mLightSensor == null) {
                        this.mLightSensor = this.mSensorManager.getDefaultSensor(5);
                        this.mSensorManager.registerListener(this.mSensorListener, this.mLightSensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-LightSensor");
                    }
                    if (this.mLightSensor != null) {
                        this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_LIGHT)] = true;
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC) {
                    if (this.mMagneticSensor == null) {
                        this.mMagneticSensor = this.mSensorManager.getDefaultSensor(2);
                        this.mSensorManager.registerListener(this.mSensorListener, this.mMagneticSensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-MagneticSensor");
                    }
                    if (this.mMagneticSensor != null) {
                        this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC)] = true;
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_PROXIMITY) {
                    if (this.mProximitySensor == null) {
                        this.mProximitySensor = this.mSensorManager.getDefaultSensor(65592);
                        if (this.mProximitySensor == null) {
                            this.mProximitySensor = this.mSensorManager.getDefaultSensor(8);
                        }
                        this.mSensorManager.registerListener(this.mSensorListener, this.mProximitySensor, 2);
                        LtUtil.log_d("SensorReadManager", "SensorReadManager", "register-ProximitySensor");
                    }
                    if (this.mProximitySensor != null) {
                        this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_PROXIMITY)] = true;
                    }
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_GYRO_EXPANSION) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_GYRO_EXPANSION)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_GYRO_POWER) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_GYRO_POWER)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_GYRO_SELF) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_GYRO_SELF)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_GYRO_TEMPERATURE) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_GYRO_TEMPERATURE)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_POWER_OFF) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_POWER_OFF)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_STATUS) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_STATUS)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_TEMPERATURE) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_TEMPERATURE)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_DAC) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_DAC)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_ADC) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_ADC)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_SELF) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_SELF)] = true;
                } else if (sensorID[i22] == ModuleSensor.ID_MANAGER_MAGNETIC_OFFSETH) {
                    this.mSensorOn[ConverterID(ModuleSensor.ID_MANAGER_MAGNETIC_OFFSETH)] = true;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unregistered-ETC(ModuleSensor ID) : ");
                    sb.append(sensorID[i22]);
                    LtUtil.log_d("SensorReadManager", "SensorReadManager", sb.toString());
                }
            }
            return;
        }
        LtUtil.log_e("SensorReadManager", "SensorReadManager", "SensorManager null !!!");
    }

    public void sensorOff() {
        LtUtil.log_i("SensorReadManager", "sensorOff", "Sensor Off");
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
        this.mSensorManager = null;
        this.mSensorListener = null;
        this.mAccelerometerSensor = null;
        this.mBarometerSensor = null;
        this.mGyroscopeSensor = null;
        this.mLightSensor = null;
        this.mMagneticSensor = null;
        this.mProximitySensor = null;
        this.mOriginalData_Accelerometer = null;
        this.mOriginalData_Barometer = null;
        this.mOriginalData_Gyro = null;
        this.mOriginalData_Light = null;
        this.mOriginalData_Magnetic = null;
        this.mOriginalData_Proximity = null;
        for (int i = 0; i < this.SENSOR_ON_ARRAY_LENGTH; i++) {
            this.mSensorOn[i] = false;
        }
    }

    public boolean isSensorOn(int moduleSensorID) {
        return this.mSensorOn[ConverterID(moduleSensorID)];
    }

    public float[] returnAccelermeter() {
        LtUtil.log_d("SensorReadManager", "returnAccelermeter", dataCheck(this.mOriginalData_Accelerometer));
        return this.mOriginalData_Accelerometer;
    }

    public float[] returnBarometer() {
        LtUtil.log_d("SensorReadManager", "returnBarometer", dataCheck(this.mOriginalData_Barometer));
        return this.mOriginalData_Barometer;
    }

    public float[] returnGyro() {
        LtUtil.log_d("SensorReadManager", "returnGyro", dataCheck(this.mOriginalData_Gyro));
        return this.mOriginalData_Gyro;
    }

    public float[] returnLight() {
        LtUtil.log_d("SensorReadManager", "returnLight", dataCheck(this.mOriginalData_Light));
        return this.mOriginalData_Light;
    }

    public float[] returnMagnetic() {
        LtUtil.log_d("SensorReadManager", "returnMagnetic", dataCheck(this.mOriginalData_Magnetic));
        return this.mOriginalData_Magnetic;
    }

    public float[] returnProximity() {
        LtUtil.log_d("SensorReadManager", "returnProximity", dataCheck(this.mOriginalData_Proximity));
        return this.mOriginalData_Proximity;
    }

    public GyroExpansionData returnGyroExpansion() {
        return null;
    }

    public int returnGyroPower() {
        return 1;
    }

    public MagneticExpansionData returnMagneticExpansion_Asahi(int testNo, String feature) {
        return null;
    }

    public MagneticExpansionData returnMagneticExpansion_Yamaha(int testNo, String feature) {
        return null;
    }

    public MagneticExpansionData returnMagneticExpansion_Alps(int testNo, String feature) {
        int[] parameter1 = new int[3];
        int[] parameter2 = new int[3];
        int[] iArr = new int[3];
        MagneticExpansionData returnData = new MagneticExpansionData();
        if (testNo == 1) {
            parameter1[0] = 1;
            returnData.mPowerOn = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mPowerOn[0] = "OK";
                returnData.mPowerOn[1] = EgisFingerprint.MAJOR_VERSION;
            } else {
                returnData.mPowerOn[0] = "NG";
                returnData.mPowerOn[1] = "0";
            }
        } else if (testNo == 3) {
            parameter1[0] = 1;
            parameter1[0] = 3;
            returnData.mStatus = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mStatus[0] = "OK";
            } else {
                returnData.mStatus[0] = "NG";
            }
            returnData.mStatus[1] = String.valueOf(parameter2[0]);
        } else if (testNo != 2) {
            if (testNo == 4) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                returnData.mDAC = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mDAC[0] = "OK";
                } else {
                    returnData.mDAC[0] = "NG";
                }
                returnData.mDAC[1] = String.valueOf(parameter2[0]);
                returnData.mDAC[2] = String.valueOf(parameter2[1]);
                returnData.mDAC[3] = String.valueOf(parameter2[2]);
            } else if (testNo == 5) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                returnData.mADC = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mADC[0] = "OK";
                } else {
                    returnData.mADC[0] = "NG";
                }
                returnData.mADC[1] = String.valueOf(parameter2[0]);
                returnData.mADC[2] = "0";
                returnData.mADC[3] = "0";
            } else if (testNo == 6) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                parameter1[0] = 6;
                returnData.mSelf = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mSelf[0] = "OK";
                } else {
                    returnData.mSelf[0] = "NG";
                }
                returnData.mSelf[1] = String.valueOf(parameter2[0]);
                returnData.mSelf[2] = String.valueOf(parameter2[1]);
                returnData.mSelf[3] = String.valueOf(parameter2[2]);
            } else if (testNo == 7) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                parameter1[0] = 6;
                parameter1[0] = 7;
                returnData.mOffset_H = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mOffset_H[0] = "OK";
                } else {
                    returnData.mOffset_H[0] = "NG";
                }
                returnData.mOffset_H[1] = String.valueOf(parameter2[0]);
                returnData.mOffset_H[2] = String.valueOf(parameter2[1]);
                returnData.mOffset_H[3] = String.valueOf(parameter2[2]);
            }
        }
        if (testNo == 2) {
            parameter1[0] = 2;
            returnData.mPowerOff = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mPowerOff[0] = "OK";
                returnData.mPowerOff[1] = EgisFingerprint.MAJOR_VERSION;
            } else {
                returnData.mPowerOff[0] = "NG";
                returnData.mPowerOff[1] = "0";
            }
        }
        return returnData;
    }

    public MagneticExpansionData returnMagneticExpansion_Bosch(int testNo, String feature) {
        return null;
    }

    public MagneticExpansionData returnMagneticExpansion_STMicro(int testNo, String feature) {
        int[] parameter1 = new int[3];
        int[] parameter2 = new int[3];
        int[] iArr = new int[3];
        MagneticExpansionData returnData = new MagneticExpansionData();
        if (testNo == 1) {
            parameter1[0] = 1;
            returnData.mPowerOn = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mPowerOn[0] = "OK";
                returnData.mPowerOn[1] = EgisFingerprint.MAJOR_VERSION;
            } else {
                returnData.mPowerOn[0] = "NG";
                returnData.mPowerOn[1] = "0";
            }
        } else if (testNo == 3) {
            parameter1[0] = 1;
            parameter1[0] = 3;
            returnData.mStatus = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mStatus[0] = "OK";
            } else {
                returnData.mStatus[0] = "NG";
            }
            returnData.mStatus[1] = String.valueOf(parameter2[0]);
        } else if (testNo != 2) {
            if (testNo == 4) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                returnData.mDAC = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mDAC[0] = "OK";
                } else {
                    returnData.mDAC[0] = "NG";
                }
                returnData.mDAC[1] = String.valueOf(parameter2[0]);
                returnData.mDAC[2] = String.valueOf(parameter2[1]);
                returnData.mDAC[3] = String.valueOf(parameter2[2]);
            } else if (testNo == 5) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                returnData.mADC = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mADC[0] = "OK";
                } else {
                    returnData.mADC[0] = "NG";
                }
                returnData.mADC[1] = String.valueOf(parameter2[0]);
                returnData.mADC[2] = "0";
                returnData.mADC[3] = "0";
            } else if (testNo == 6) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                parameter1[0] = 6;
                returnData.mSelf = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mSelf[0] = "OK";
                } else {
                    returnData.mSelf[0] = "NG";
                }
                returnData.mSelf[1] = String.valueOf(parameter2[0]);
                returnData.mSelf[2] = String.valueOf(parameter2[1]);
                returnData.mSelf[3] = String.valueOf(parameter2[2]);
            } else if (testNo == 7) {
                parameter1[0] = 1;
                parameter1[0] = 3;
                parameter1[0] = 4;
                parameter1[0] = 5;
                parameter1[0] = 6;
                parameter1[0] = 7;
                returnData.mOffset_H = new String[4];
                if (returnData.mReturnValue >= 0) {
                    returnData.mOffset_H[0] = "OK";
                } else {
                    returnData.mOffset_H[0] = "NG";
                }
                returnData.mOffset_H[1] = String.valueOf(parameter2[0]);
                returnData.mOffset_H[2] = String.valueOf(parameter2[1]);
                returnData.mOffset_H[3] = String.valueOf(parameter2[2]);
            }
        }
        if (testNo == 2) {
            parameter1[0] = 2;
            returnData.mPowerOff = new String[2];
            if (returnData.mReturnValue >= 0) {
                returnData.mPowerOff[0] = "OK";
                returnData.mPowerOff[1] = EgisFingerprint.MAJOR_VERSION;
            } else {
                returnData.mPowerOff[0] = "NG";
                returnData.mPowerOff[1] = "0";
            }
        }
        return returnData;
    }

    private int ConverterID(int moduleSensorID) {
        if (moduleSensorID == ModuleSensor.ID_MANAGER_ACCELEROMETER) {
            return this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_ACCELEROMETER_N_ANGLE) {
            return this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_N_ANGLE;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_ACCELEROMETER_SELF) {
            return this.SENSOR_ON_ARRAY_INDEX_ACCELEROMETER_SELF;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_BAROMETER) {
            return this.SENSOR_ON_ARRAY_INDEX_BAROMETER;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_GYRO) {
            return this.SENSOR_ON_ARRAY_INDEX_GYRO;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_GYRO_POWER) {
            return this.SENSOR_ON_ARRAY_INDEX_GYRO_POWER;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_GYRO_EXPANSION) {
            return this.SENSOR_ON_ARRAY_INDEX_GYRO_EXPANSION;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_GYRO_SELF) {
            return this.SENSOR_ON_ARRAY_INDEX_GYRO_SELF;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_GYRO_TEMPERATURE) {
            return this.SENSOR_ON_ARRAY_INDEX_GYRO_TEMPERATURE;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_LIGHT) {
            return this.SENSOR_ON_ARRAY_INDEX_LIGHT;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_ON;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_POWER_OFF) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_POWER_OFF;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_STATUS) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_STATUS;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_TEMPERATURE) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_TEMPERATURE;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_DAC) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_DAC;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_ADC) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_ADC;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_SELF) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_SELF;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_MAGNETIC_OFFSETH) {
            return this.SENSOR_ON_ARRAY_INDEX_MAGNETIC_OFFSETH;
        }
        if (moduleSensorID == ModuleSensor.ID_MANAGER_PROXIMITY) {
            return this.SENSOR_ON_ARRAY_INDEX_PROXIMITY;
        }
        return -1;
    }

    private String dataCheck(float[] data) {
        String result = "";
        if (data == null) {
            return "Data : null";
        }
        for (int i = 0; i < data.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            sb.append(data[i]);
            result = sb.toString();
            if (i < data.length - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(result);
                sb2.append(" , ");
                result = sb2.toString();
            }
        }
        return result;
    }
}
