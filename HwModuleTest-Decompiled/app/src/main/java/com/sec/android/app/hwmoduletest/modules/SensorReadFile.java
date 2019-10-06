package com.sec.android.app.hwmoduletest.modules;

import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class SensorReadFile {
    private final String CLASS_NAME = "SensorReadFile";
    private final boolean DEBUG = true;
    private final int INFO_ARRAY_INDEX_ACCELEROMETER;
    private final int INFO_ARRAY_INDEX_ACCELEROMETER_CAL;
    private final int INFO_ARRAY_INDEX_ACCELEROMETER_INTPIN;
    private final int INFO_ARRAY_INDEX_ACCELEROMETER_N_ANGLE;
    private final int INFO_ARRAY_INDEX_ACCELEROMETER_SELF;
    private final int INFO_ARRAY_INDEX_BAROMETER_EEPROM;
    private final int INFO_ARRAY_INDEX_GYRO_SELFTEST;
    private final int INFO_ARRAY_INDEX_GYRO_TEMPERATURE;
    private final int INFO_ARRAY_INDEX_LIGHT;
    private final int INFO_ARRAY_INDEX_LIGHT_ADC;
    private final int INFO_ARRAY_INDEX_MAGNETIC_ADC;
    private final int INFO_ARRAY_INDEX_MAGNETIC_DAC;
    private final int INFO_ARRAY_INDEX_MAGNETIC_POWER_OFF;
    private final int INFO_ARRAY_INDEX_MAGNETIC_POWER_ON;
    private final int INFO_ARRAY_INDEX_MAGNETIC_SELF;
    private final int INFO_ARRAY_INDEX_MAGNETIC_STATUS;
    private final int INFO_ARRAY_INDEX_MAGNETIC_TEMP;
    private final int INFO_ARRAY_INDEX_PROXIMITY_ADC;
    private final int INFO_ARRAY_INDEX_PROXIMITY_AVG;
    private final int INFO_ARRAY_INDEX_PROXIMITY_OFFSET;
    private final int INFO_ARRAY_LENGTH;
    /* access modifiers changed from: private */
    public int LOOP_DELAY;
    private int dummy = 0;
    private String mFeature_Magnetic;
    /* access modifiers changed from: private */
    public Info[] mInfo;
    /* access modifiers changed from: private */
    public boolean mInterrupted;
    private boolean mIsLoop;
    /* access modifiers changed from: private */
    public int[] mModuleSensorIDArray;
    private SensorReadThread mSensorReadThread;
    private String[] mTemp_String;

    private static class Info {
        public String[] mData = null;
        public String mFileID;
        public boolean mIsExistFile;
        public String mName;

        public Info(String name, String fileid) {
            this.mName = name;
            this.mFileID = fileid;
            this.mIsExistFile = Kernel.isExistFileID(fileid);
        }
    }

    class SensorReadThread extends Thread {
        SensorReadThread() {
        }

        public void run() {
            while (!SensorReadFile.this.mInterrupted) {
                int i = 0;
                while (i < SensorReadFile.this.mModuleSensorIDArray.length) {
                    try {
                        int infoArrayIndex = SensorReadFile.this.ConverterID(SensorReadFile.this.mModuleSensorIDArray[i]);
                        if (-1 < infoArrayIndex && SensorReadFile.this.mInfo != null && SensorReadFile.this.mInfo[infoArrayIndex] != null && SensorReadFile.this.mInfo[infoArrayIndex].mIsExistFile) {
                            LtUtil.log_d("SensorReadFile", "read", SensorReadFile.this.mInfo[infoArrayIndex].mName);
                            String mTemp = Kernel.read(SensorReadFile.this.mInfo[infoArrayIndex].mFileID);
                            if (mTemp != null) {
                                try {
                                    SensorReadFile.this.mInfo[infoArrayIndex].mData = mTemp.split(",");
                                } catch (Exception e) {
                                    String str = "SensorReadFile";
                                    String str2 = "SensorReadThread-run";
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("execption : ");
                                    sb.append(e);
                                    LtUtil.log_d(str, str2, sb.toString());
                                }
                            }
                        }
                        i++;
                    } catch (InterruptedException e2) {
                        LtUtil.log_e(e2);
                    } finally {
                        try {
                            sleep((long) SensorReadFile.this.LOOP_DELAY);
                        } catch (InterruptedException e1) {
                            LtUtil.log_e(e1);
                        }
                    }
                }
                try {
                    sleep((long) SensorReadFile.this.LOOP_DELAY);
                } catch (InterruptedException e12) {
                    LtUtil.log_e(e12);
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LoopStop-mInterrupted : ");
            sb2.append(SensorReadFile.this.mInterrupted);
            LtUtil.log_d("SensorReadFile", "SensorReadThread-run", sb2.toString());
        }
    }

    public SensorReadFile(int[] moduleSensorID) {
        int i = this.dummy;
        this.dummy = i + 1;
        this.INFO_ARRAY_INDEX_ACCELEROMETER = i;
        int i2 = this.dummy;
        this.dummy = i2 + 1;
        this.INFO_ARRAY_INDEX_ACCELEROMETER_N_ANGLE = i2;
        int i3 = this.dummy;
        this.dummy = i3 + 1;
        this.INFO_ARRAY_INDEX_ACCELEROMETER_SELF = i3;
        int i4 = this.dummy;
        this.dummy = i4 + 1;
        this.INFO_ARRAY_INDEX_ACCELEROMETER_CAL = i4;
        int i5 = this.dummy;
        this.dummy = i5 + 1;
        this.INFO_ARRAY_INDEX_ACCELEROMETER_INTPIN = i5;
        int i6 = this.dummy;
        this.dummy = i6 + 1;
        this.INFO_ARRAY_INDEX_BAROMETER_EEPROM = i6;
        int i7 = this.dummy;
        this.dummy = i7 + 1;
        this.INFO_ARRAY_INDEX_GYRO_TEMPERATURE = i7;
        int i8 = this.dummy;
        this.dummy = i8 + 1;
        this.INFO_ARRAY_INDEX_GYRO_SELFTEST = i8;
        int i9 = this.dummy;
        this.dummy = i9 + 1;
        this.INFO_ARRAY_INDEX_LIGHT = i9;
        int i10 = this.dummy;
        this.dummy = i10 + 1;
        this.INFO_ARRAY_INDEX_LIGHT_ADC = i10;
        int i11 = this.dummy;
        this.dummy = i11 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_POWER_ON = i11;
        int i12 = this.dummy;
        this.dummy = i12 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_POWER_OFF = i12;
        int i13 = this.dummy;
        this.dummy = i13 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_STATUS = i13;
        int i14 = this.dummy;
        this.dummy = i14 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_TEMP = i14;
        int i15 = this.dummy;
        this.dummy = i15 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_DAC = i15;
        int i16 = this.dummy;
        this.dummy = i16 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_ADC = i16;
        int i17 = this.dummy;
        this.dummy = i17 + 1;
        this.INFO_ARRAY_INDEX_MAGNETIC_SELF = i17;
        int i18 = this.dummy;
        this.dummy = i18 + 1;
        this.INFO_ARRAY_INDEX_PROXIMITY_ADC = i18;
        int i19 = this.dummy;
        this.dummy = i19 + 1;
        this.INFO_ARRAY_INDEX_PROXIMITY_AVG = i19;
        int i20 = this.dummy;
        this.dummy = i20 + 1;
        this.INFO_ARRAY_INDEX_PROXIMITY_OFFSET = i20;
        this.INFO_ARRAY_LENGTH = this.dummy;
        this.mInfo = new Info[this.INFO_ARRAY_LENGTH];
        this.mSensorReadThread = null;
        this.mInterrupted = false;
        this.mIsLoop = false;
        this.LOOP_DELAY = 1000;
        this.mFeature_Magnetic = Feature.getString(Feature.SENSOR_NAME_MAGNETIC);
        this.mTemp_String = null;
        LtUtil.log_i("SensorReadFile", "SensorReadFile", "Sensor On");
        this.mModuleSensorIDArray = moduleSensorID;
        init();
    }

    public void sensorOff() {
        LtUtil.log_i("SensorReadFile", "sensorOff", "Sensor Off");
        if (this.mSensorReadThread != null && this.mSensorReadThread.isAlive()) {
            this.mInterrupted = true;
            LtUtil.log_i("SensorReadFile", "sensorOff", "Loop Stop");
            StringBuilder sb = new StringBuilder();
            sb.append("mInterrupted : ");
            sb.append(this.mInterrupted);
            LtUtil.log_i("SensorReadFile", "sensorOff", sb.toString());
            this.mSensorReadThread = null;
        }
        this.mInfo = null;
    }

    private void init() {
        StringBuilder sb = new StringBuilder();
        sb.append("INFO_ARRAY_LENGTH : ");
        sb.append(this.INFO_ARRAY_LENGTH);
        LtUtil.log_d("SensorReadFile", "init", sb.toString());
        int infoArrayIndex = -1;
        if (this.mModuleSensorIDArray != null) {
            for (int i = 0; i < this.mModuleSensorIDArray.length; i++) {
                if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____ACCELEROMETER) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____ACCELEROMETER);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____ACCELEROMETER", Kernel.ACCEL_SENSOR_RAW);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____ACCELEROMETER_N_ANGLE) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____ACCELEROMETER_N_ANGLE);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____ACCELEROMETER_N_ANGLE", Kernel.ACCEL_SENSOR_RAW);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____ACCELEROMETER_SELF) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____ACCELEROMETER_SELF);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____ACCELEROMETER_SELF", Kernel.ACCEL_SENSOR_RAW);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____ACCELEROMETER_CAL) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____ACCELEROMETER_CAL);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____ACCELEROMETER_CAL", Kernel.ACCEL_SENSOR_CAL);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____ACCELEROMETER_INTPIN) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____ACCELEROMETER_INTPIN);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____ACCELEROMETER_INTPIN", Kernel.ACCEL_SENSOR_INTPIN);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____BAROMETER_EEPROM) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____BAROMETER_EEPROM);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____BAROMETER_EEPROM", Kernel.BAROME_EEPROM);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____GYRO_TEMPERATURE) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____GYRO_TEMPERATURE);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____GYRO_TEMPERATURE", Kernel.GYRO_SENSOR_TEMP);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____GYRO_SELFTEST) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____GYRO_SELFTEST);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____GYRO_SELF", Kernel.GYRO_SENSOR_SELFTEST);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____LIGHT) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____LIGHT);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____LIGHT", Kernel.LIGHT_SENSOR_LUX);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____LIGHT_ADC) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____LIGHT_ADC);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____LIGHT_ADC", Kernel.LIGHT_SENSOR_ADC);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_POWER_ON) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_POWER_ON);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_POWER_ON", Kernel.GEOMAGNETIC_SENSOR_POWER);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_STATUS) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_STATUS);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_STATUS", Kernel.GEOMAGNETIC_SENSOR_STATUS);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_TEMPERATURE) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_TEMPERATURE);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_TEMPERATURE", Kernel.GEOMAGNETIC_SENSOR_TEMP);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_DAC) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_DAC);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_DAC", Kernel.GEOMAGNETIC_SENSOR_DAC);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_ADC) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_ADC);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_ADC", Kernel.GEOMAGNETIC_SENSOR_ADC);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____MAGNETIC_SELF) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____MAGNETIC_SELF);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____MAGNETIC_SELF", Kernel.GEOMAGNETIC_SENSOR_SELFTEST);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____PROXIMITY_ADC) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____PROXIMITY_ADC);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____PROXIMITY_ADC", Kernel.PROXI_SENSOR_ADC);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____PROXIMITY_AVG) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____PROXIMITY_AVG);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____PROXIMITY_AVG", Kernel.PROXI_SENSOR_ADC_AVG);
                    }
                } else if (this.mModuleSensorIDArray[i] == ModuleSensor.ID_FILE____PROXIMITY_OFFSET) {
                    infoArrayIndex = ConverterID(ModuleSensor.ID_FILE____PROXIMITY_OFFSET);
                    if (-1 < infoArrayIndex) {
                        this.mInfo[infoArrayIndex] = new Info("ID_FILE____PROXIMITY_OFFSET", Kernel.PROXI_SENSOR_OFFSET);
                    }
                }
                if (-1 >= infoArrayIndex || infoArrayIndex >= this.INFO_ARRAY_LENGTH) {
                    LtUtil.log_e("SensorReadFile", "init", "ID : Unknown");
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mInfo - mName : ");
                    sb2.append(this.mInfo[infoArrayIndex].mName);
                    LtUtil.log_i("SensorReadFile", "init", sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("mInfo - mFileID : ");
                    sb3.append(this.mInfo[infoArrayIndex].mFileID);
                    LtUtil.log_i("SensorReadFile", "init", sb3.toString());
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("mInfo - mIsExistFile : ");
                    sb4.append(this.mInfo[infoArrayIndex].mIsExistFile);
                    LtUtil.log_i("SensorReadFile", "init", sb4.toString());
                }
            }
            return;
        }
        LtUtil.log_e("SensorReadFile", "init", "mModuleSensorIDArray null");
    }

    public boolean isSensorOn(int moduleSensorID) {
        int infoArrayIndex = ConverterID(moduleSensorID);
        if (this.mInfo == null || this.mInfo[infoArrayIndex] == null) {
            return false;
        }
        return this.mInfo[infoArrayIndex].mIsExistFile;
    }

    public void startLoop(int loopDelay_millisecond) {
        this.mIsLoop = true;
        this.mSensorReadThread = new SensorReadThread();
        this.LOOP_DELAY = loopDelay_millisecond;
        this.mSensorReadThread.setDaemon(true);
        this.mSensorReadThread.start();
    }

    public String[] returnData(int moduleSensorID) {
        int infoArrayIndex = ConverterID(moduleSensorID);
        if (!this.mIsLoop) {
            String str = "";
            if (-1 < infoArrayIndex && this.mInfo[infoArrayIndex].mIsExistFile) {
                LtUtil.log_d("SensorReadFile", "returnData", this.mInfo[infoArrayIndex].mName);
                String mTemp = Kernel.read(this.mInfo[infoArrayIndex].mFileID);
                if (mTemp != null) {
                    this.mInfo[infoArrayIndex].mData = mTemp.split(",");
                    if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_DAC || moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_ADC || moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_SELF) {
                        checkSpec(moduleSensorID, infoArrayIndex);
                    }
                }
            } else if (-1 < infoArrayIndex && !this.mInfo[infoArrayIndex].mIsExistFile) {
                LtUtil.log_d("SensorReadFile", "read no file", this.mInfo[infoArrayIndex].mName);
                this.mInfo[infoArrayIndex].mData = "0".split(",");
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mInfo[infoArrayIndex].mName);
        sb.append(" => ");
        sb.append(dataCheck(this.mInfo[infoArrayIndex].mData));
        LtUtil.log_d("SensorReadFile", "returnData", sb.toString());
        return this.mInfo[infoArrayIndex].mData;
    }

    private void checkSpec(int moduleSensorID, int infoArrayIndex) {
        String str = "";
        dataCheck(this.mInfo[infoArrayIndex].mData);
        if (this.mInfo[infoArrayIndex].mData != null) {
            if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_DAC) {
                if (this.mInfo[infoArrayIndex].mData.length == 4) {
                    String specResult = this.mInfo[infoArrayIndex].mData[0];
                    int x = Integer.valueOf(this.mInfo[infoArrayIndex].mData[1].replace(" ", "")).intValue();
                    int y = Integer.valueOf(this.mInfo[infoArrayIndex].mData[2].replace(" ", "")).intValue();
                    int z = Integer.valueOf(this.mInfo[infoArrayIndex].mData[3].replace(" ", "")).intValue();
                    if (specResult.equals(EgisFingerprint.MAJOR_VERSION)) {
                        this.mInfo[infoArrayIndex].mData[0] = "OK";
                    }
                    if (specResult.equals("0")) {
                        this.mInfo[infoArrayIndex].mData[0] = "NG";
                    }
                    String specResult2 = SensorCalculator.checkSpecMagneticDAC(x, y, z);
                    if (specResult2.equals("OK") || specResult2.equals("NG")) {
                        this.mInfo[infoArrayIndex].mData[0] = specResult2;
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> specResult");
                    } else {
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> default");
                    }
                }
            } else if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_ADC) {
                if (this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE) || this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION)) {
                    this.mTemp_String = this.mInfo[infoArrayIndex].mData;
                    this.mInfo[infoArrayIndex].mData = new String[4];
                    int x2 = Integer.valueOf(this.mTemp_String[0].replace(" ", "")).intValue();
                    int y2 = Integer.valueOf(this.mTemp_String[1].replace(" ", "")).intValue();
                    int z2 = Integer.valueOf(this.mTemp_String[2].replace(" ", "")).intValue();
                    this.mInfo[infoArrayIndex].mData[0] = SensorCalculator.checkSpecMagneticADC(x2, y2, z2);
                    String[] strArr = this.mInfo[infoArrayIndex].mData;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(x2);
                    strArr[1] = sb.toString();
                    String[] strArr2 = this.mInfo[infoArrayIndex].mData;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(y2);
                    strArr2[2] = sb2.toString();
                    String[] strArr3 = this.mInfo[infoArrayIndex].mData;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("");
                    sb3.append(z2);
                    strArr3[3] = sb3.toString();
                } else if (this.mInfo[infoArrayIndex].mData.length == 4) {
                    String specResult3 = this.mInfo[infoArrayIndex].mData[0];
                    int x3 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[1].replace(" ", "")).intValue();
                    int y3 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[2].replace(" ", "")).intValue();
                    int z3 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[3].replace(" ", "")).intValue();
                    if (specResult3.equals(EgisFingerprint.MAJOR_VERSION)) {
                        this.mInfo[infoArrayIndex].mData[0] = "OK";
                    }
                    if (specResult3.equals("0")) {
                        this.mInfo[infoArrayIndex].mData[0] = "NG";
                    }
                    String specResult4 = SensorCalculator.checkSpecMagneticADC(x3, y3, z3);
                    if (specResult4.equals("OK") || specResult4.equals("NG")) {
                        this.mInfo[infoArrayIndex].mData[0] = specResult4;
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> specResult");
                    } else {
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> default");
                    }
                }
            } else if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_SELF) {
                if (this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150) || this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE) || this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION)) {
                    this.mTemp_String = this.mInfo[infoArrayIndex].mData;
                    this.mInfo[infoArrayIndex].mData = new String[2];
                    int z4 = Integer.valueOf(this.mTemp_String[0].replace(" ", "")).intValue();
                    this.mInfo[infoArrayIndex].mData[0] = SensorCalculator.checkSpecMagneticSelf(0, 0, z4);
                    String[] strArr4 = this.mInfo[infoArrayIndex].mData;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("");
                    sb4.append(z4);
                    strArr4[1] = sb4.toString();
                } else if (!this.mFeature_Magnetic.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_NEWEST) && this.mInfo[infoArrayIndex].mData.length == 4) {
                    String specResult5 = this.mInfo[infoArrayIndex].mData[0];
                    int x4 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[1].replace(" ", "")).intValue();
                    int y4 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[2].replace(" ", "")).intValue();
                    int z5 = Integer.valueOf(this.mInfo[infoArrayIndex].mData[3].replace(" ", "")).intValue();
                    if (specResult5.equals(EgisFingerprint.MAJOR_VERSION)) {
                        this.mInfo[infoArrayIndex].mData[0] = "OK";
                    }
                    if (specResult5.equals("0")) {
                        this.mInfo[infoArrayIndex].mData[0] = "NG";
                    }
                    String specResult6 = SensorCalculator.checkSpecMagneticSelf(x4, y4, z5);
                    if (specResult6.equals("OK") || specResult6.equals("NG")) {
                        this.mInfo[infoArrayIndex].mData[0] = specResult6;
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> specResult");
                    } else {
                        LtUtil.log_i("SensorReadFile", "checkSpec", "<Write> default");
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public int ConverterID(int moduleSensorID) {
        if (moduleSensorID == ModuleSensor.ID_FILE____ACCELEROMETER) {
            return this.INFO_ARRAY_INDEX_ACCELEROMETER;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____ACCELEROMETER_N_ANGLE) {
            return this.INFO_ARRAY_INDEX_ACCELEROMETER_N_ANGLE;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____ACCELEROMETER_SELF) {
            return this.INFO_ARRAY_INDEX_ACCELEROMETER_SELF;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____ACCELEROMETER_CAL) {
            return this.INFO_ARRAY_INDEX_ACCELEROMETER_CAL;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____ACCELEROMETER_INTPIN) {
            return this.INFO_ARRAY_INDEX_ACCELEROMETER_INTPIN;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____BAROMETER_EEPROM) {
            return this.INFO_ARRAY_INDEX_BAROMETER_EEPROM;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____GYRO_TEMPERATURE) {
            return this.INFO_ARRAY_INDEX_GYRO_TEMPERATURE;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____GYRO_SELFTEST) {
            return this.INFO_ARRAY_INDEX_GYRO_SELFTEST;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____LIGHT) {
            return this.INFO_ARRAY_INDEX_LIGHT;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____LIGHT_ADC) {
            return this.INFO_ARRAY_INDEX_LIGHT_ADC;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_POWER_ON) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_POWER_ON;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_POWER_OFF) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_POWER_OFF;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_STATUS) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_STATUS;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_TEMPERATURE) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_TEMP;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_DAC) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_DAC;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_ADC) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_ADC;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____MAGNETIC_SELF) {
            return this.INFO_ARRAY_INDEX_MAGNETIC_SELF;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____PROXIMITY_ADC) {
            return this.INFO_ARRAY_INDEX_PROXIMITY_ADC;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____PROXIMITY_AVG) {
            return this.INFO_ARRAY_INDEX_PROXIMITY_AVG;
        }
        if (moduleSensorID == ModuleSensor.ID_FILE____PROXIMITY_OFFSET) {
            return this.INFO_ARRAY_INDEX_PROXIMITY_OFFSET;
        }
        return -1;
    }

    private String dataCheck(String[] data) {
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
