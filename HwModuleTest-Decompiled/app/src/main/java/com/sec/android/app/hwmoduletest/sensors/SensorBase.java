package com.sec.android.app.hwmoduletest.sensors;

import android.os.Handler;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class SensorBase {
    public static final int ID_FILE____LIGHT = 6;
    public static final int ID_FILE____LIGHT_ADC = 8;
    public static final int ID_FILE____LIGHT_CCT = 7;
    public static final int ID_FILE____PROXIMITY_ADC = 2;
    public static final int ID_FILE____PROXIMITY_AVG = 3;
    public static final int ID_FILE____PROXIMITY_OFFSET = 4;
    public static final int ID_MANAGER_BIO = 12;
    public static final int ID_MANAGER_BIO_HRM = 13;
    public static final int ID_MANAGER_HUMID = 10;
    public static final int ID_MANAGER_LIGHT = 5;
    public static final int ID_MANAGER_PROXIMITY = 1;
    public static final int ID_MANAGER_TEMP = 9;
    public static final int ID_MANAGER_UV = 11;
    public static final int ID_MANAGER_UV_CLOUD = 14;
    public static final int NOTI = 0;
    private final String CLASS_NAME = "SensorBase";

    public SensorBase() {
        LtUtil.log_d("SensorBase", "SensorBase", null);
    }

    public void SensorOn(int[] sensorID) {
    }

    public void SensorOn(int[] sensorID, Handler notiHandler, int messageDelay_SensorUpdate_millisecond) {
    }

    public void SensorOff() {
    }

    public boolean isSensorOn(int sensorID) {
        return true;
    }
}
