package com.sec.android.app.hwmoduletest.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.HwTestMenu;

public class SensorReadIntent {
    private final String ACCELERMETER_INTENT_ACTION_READ;
    private final String ACCELERMETER_INTENT_ACTION_STOP;
    private final int ACCEL_COUNT_MAX;
    private final String CLASS_NAME = "SensorReadIntent";
    private final String CP_INTENT_ACTION_READ;
    private final boolean DEBUG = true;
    private int DUMMY;
    private final int GRIP_COUNT_MAX;
    private final String GRIP_INTENT_ACTION_READ;
    private final String GRIP_INTENT_ACTION_READ_OTHER;
    private final String GRIP_INTENT_ACTION_STOP;
    private final String GRIP_VALUE__INTENT_EXTRA_NAME;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N2;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N2N3;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N3;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_2;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_2N3;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_3;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_HC_0000;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_HC_0100;
    private final String GRIP_VALUE__INTENT_EXTRA_VALUE__RELEASE_ALL;
    private final int SENSOR_ON_ARRAY_INDEX_GRIP;
    private final int SENSOR_ON_ARRAY_LENGTH;
    private int count_Grip;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private int[] mModuleSensorIDArray;
    private int[] mOriginalData_CPsAccelerometer;
    private int[] mOriginalData_Grip;
    private final boolean[] mSensorOn;

    public SensorReadIntent(Context context, int[] moduleSensorID) {
        this.DUMMY = 0;
        int i = this.DUMMY;
        this.DUMMY = i + 1;
        this.SENSOR_ON_ARRAY_INDEX_GRIP = i;
        this.SENSOR_ON_ARRAY_LENGTH = this.DUMMY;
        this.mSensorOn = new boolean[this.SENSOR_ON_ARRAY_LENGTH];
        this.CP_INTENT_ACTION_READ = "com.sec.android.app.factorytest";
        this.GRIP_INTENT_ACTION_READ = "030005";
        this.GRIP_INTENT_ACTION_READ_OTHER = "07000b";
        this.ACCELERMETER_INTENT_ACTION_READ = "070006";
        this.GRIP_INTENT_ACTION_STOP = "com.android.samsungtest.GripTestStop";
        this.GRIP_VALUE__INTENT_EXTRA_NAME = "COMMAND";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1 = "010000000000";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_2 = "000001000000";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_3 = "";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N2 = "010001000000";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N3 = "";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_2N3 = "";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_1N2N3 = "";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__RELEASE_ALL = "000000000000";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_HC_0100 = "0100";
        this.GRIP_VALUE__INTENT_EXTRA_VALUE__DETECT_SENSOR_HC_0000 = "0000";
        this.ACCELERMETER_INTENT_ACTION_STOP = "com.android.samsungtest.CpAccelermeterOff";
        this.ACCEL_COUNT_MAX = 3;
        this.GRIP_COUNT_MAX = 3;
        this.mOriginalData_Grip = null;
        this.mOriginalData_CPsAccelerometer = null;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                StringBuilder sb = new StringBuilder();
                sb.append("action : ");
                sb.append(action);
                LtUtil.log_i("SensorReadIntent", "mBroadcastReceiver.onReceive()", sb.toString());
                if (action.equals("com.sec.android.app.factorytest")) {
                    String cmdData = intent.getStringExtra("COMMAND");
                    if (cmdData != null) {
                        String sensorData = cmdData.substring(6);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("cmdData : ");
                        sb2.append(cmdData);
                        sb2.append(" , sensorData : ");
                        sb2.append(sensorData);
                        LtUtil.log_i("SensorReadIntent", "mBroadcastReceiver.onReceive()", sb2.toString());
                        if (cmdData.startsWith("030005") || cmdData.startsWith("07000b")) {
                            LtUtil.log_d("SensorReadIntent", "onReceive", "Grip");
                            SensorReadIntent.this.setValueGrip(sensorData);
                        } else if (cmdData.startsWith("070006")) {
                            LtUtil.log_d("SensorReadIntent", "onReceive", "Accelerometer");
                            SensorReadIntent.this.setValueCPsAccelerometerData(sensorData);
                        }
                    }
                }
            }
        };
        LtUtil.log_i("SensorReadIntent", "SensorReadIntent", "Sensor On");
        this.mContext = context;
        this.mModuleSensorIDArray = moduleSensorID;
        this.mOriginalData_Grip = new int[3];
        this.mOriginalData_CPsAccelerometer = new int[3];
        for (int i2 = 0; i2 < this.SENSOR_ON_ARRAY_LENGTH; i2++) {
            this.mSensorOn[i2] = false;
        }
        for (int i3 = 0; i3 < this.mModuleSensorIDArray.length; i3++) {
            if (this.mModuleSensorIDArray[i3] == ModuleSensor.ID_INTENT__GRIP) {
                if (!this.mSensorOn[this.SENSOR_ON_ARRAY_INDEX_GRIP]) {
                    LtUtil.log_d("SensorReadIntent", "SensorReadIntent", "Grip Sensor");
                    LtUtil.log_d("SensorReadIntent", "SensorReadIntent", "CPO");
                    this.mSensorOn[ConverterID(ModuleSensor.ID_INTENT__GRIP)] = true;
                    registerReceiver_Grip();
                } else {
                    LtUtil.log_d("SensorReadIntent", "SensorReadIntent", "Grip Sensor - On");
                }
            } else if (this.mModuleSensorIDArray[i3] == ModuleSensor.ID_INTENT__CP_ACCELEROMETER) {
                LtUtil.log_d("SensorReadIntent", "SensorReadIntent", "CP Accelerometer Sensor - On");
                registerReceiver_CPAccelerometer();
            }
        }
    }

    public void sensorOff() {
        LtUtil.log_i("SensorReadIntent", "sensorOff", "Grip Sensor Off");
        for (int i : this.mModuleSensorIDArray) {
            if (i == ModuleSensor.ID_INTENT__GRIP) {
                LtUtil.log_d("SensorReadIntent", "sensorOff", "Grip Sensor");
                LtUtil.log_d("SensorReadIntent", "sensorOff", "CPO");
                this.mOriginalData_Grip = null;
                unregisterReceiver_Grip();
            }
        }
        for (int i2 = 0; i2 < this.SENSOR_ON_ARRAY_LENGTH; i2++) {
            this.mSensorOn[i2] = false;
        }
        this.mModuleSensorIDArray = null;
    }

    public void disableReceiver_CPsAccelerometer() {
        LtUtil.log_i("SensorReadIntent", "disableBroadcastReceiverCPsAccelerometer", "CP's Acceler... OFF");
        this.mOriginalData_CPsAccelerometer = null;
        unregisterReceiver_CPAccelerometer();
    }

    public boolean isSensorOn(int moduleSensorID) {
        return this.mSensorOn[ConverterID(moduleSensorID)];
    }

    private void registerReceiver_Grip() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.android.app.factorytest");
        filter.addAction("com.android.samsungtest.GripTestStop");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    private void unregisterReceiver_Grip() {
        if (this.mBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    private void registerReceiver_CPAccelerometer() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.android.app.factorytest");
        filter.addAction("com.android.samsungtest.CpAccelermeterOff");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    private void unregisterReceiver_CPAccelerometer() {
        if (this.mBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    /* access modifiers changed from: private */
    public void setValueGrip(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("value : ");
        sb.append(value);
        LtUtil.log_d("SensorReadIntent", "setValueGrip", sb.toString());
        this.count_Grip = Integer.valueOf(HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_GRIP)).intValue();
        if (value.equals("010000000000")) {
            this.mOriginalData_Grip[0] = 1;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 0;
        } else if (value.equals("000001000000")) {
            this.mOriginalData_Grip[0] = 0;
            this.mOriginalData_Grip[1] = 1;
            this.mOriginalData_Grip[2] = 0;
        } else if (value.equals("")) {
            this.mOriginalData_Grip[0] = 0;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 1;
        } else if (value.equals("010001000000")) {
            this.mOriginalData_Grip[0] = 1;
            this.mOriginalData_Grip[1] = 1;
            this.mOriginalData_Grip[2] = 0;
        } else if (value.equals("")) {
            this.mOriginalData_Grip[0] = 1;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 1;
        } else if (value.equals("")) {
            this.mOriginalData_Grip[0] = 0;
            this.mOriginalData_Grip[1] = 1;
            this.mOriginalData_Grip[2] = 1;
        } else if (value.equals("")) {
            this.mOriginalData_Grip[0] = 1;
            this.mOriginalData_Grip[1] = 1;
            this.mOriginalData_Grip[2] = 1;
        } else if (value.equals("000000000000")) {
            this.mOriginalData_Grip[0] = 0;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 0;
        } else if (value.equals("0100")) {
            this.mOriginalData_Grip[0] = 0;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 0;
        } else if (value.equals("0000")) {
            this.mOriginalData_Grip[0] = 1;
            this.mOriginalData_Grip[1] = 0;
            this.mOriginalData_Grip[2] = 0;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unknown => ");
            sb2.append(value);
            LtUtil.log_e("SensorReadIntent", "setValueGrip", sb2.toString());
        }
        if (this.count_Grip == 1) {
            if (value.equals("010000000000") || value.equals("000001000000")) {
                this.mOriginalData_Grip[0] = 1;
            }
            this.mOriginalData_Grip[1] = 1;
            this.mOriginalData_Grip[2] = 0;
        }
    }

    public int[] returnGrip() {
        LtUtil.log_d("SensorReadIntent", "returnGrip", dataCheck(this.mOriginalData_Grip));
        return this.mOriginalData_Grip;
    }

    /* access modifiers changed from: private */
    public void setValueCPsAccelerometerData(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value.substring(2, 4));
        sb.append(value.substring(0, 2));
        String xData = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(value.substring(6, 8));
        sb2.append(value.substring(4, 6));
        String yData = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(value.substring(10, 12));
        sb3.append(value.substring(8, 10));
        String zData = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append("xData=[");
        sb4.append(xData);
        sb4.append("],yData=[");
        sb4.append(yData);
        sb4.append("],zData=[");
        sb4.append(zData);
        sb4.append("]");
        LtUtil.log_d("SensorReadIntent", "setValueCPsAccelerometerData", sb4.toString());
        this.mOriginalData_CPsAccelerometer[0] = (short) Integer.parseInt(xData, 16);
        this.mOriginalData_CPsAccelerometer[1] = (short) Integer.parseInt(yData, 16);
        this.mOriginalData_CPsAccelerometer[2] = (short) Integer.parseInt(zData, 16);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("x=[");
        sb5.append(this.mOriginalData_CPsAccelerometer[0]);
        sb5.append("],y=[");
        sb5.append(this.mOriginalData_CPsAccelerometer[1]);
        sb5.append("],z=[");
        sb5.append(this.mOriginalData_CPsAccelerometer[2]);
        sb5.append("]");
        LtUtil.log_d("SensorReadIntent", "setValueCPsAccelerometerData", sb5.toString());
    }

    public int[] returnCPsAccelerometerData() {
        LtUtil.log_d("SensorReadIntent", "returnCPsAccelerometerData", dataCheck(this.mOriginalData_CPsAccelerometer));
        return this.mOriginalData_CPsAccelerometer;
    }

    private int ConverterID(int moduleSensorID) {
        if (moduleSensorID == ModuleSensor.ID_INTENT__GRIP) {
            return this.SENSOR_ON_ARRAY_INDEX_GRIP;
        }
        return -1;
    }

    private String dataCheck(int[] data) {
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
