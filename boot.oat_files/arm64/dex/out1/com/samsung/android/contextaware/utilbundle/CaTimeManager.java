package com.samsung.android.contextaware.utilbundle;

import android.text.format.DateUtils;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class CaTimeManager {
    private static volatile CaTimeManager instance;

    public static CaTimeManager getInstance() {
        if (instance == null) {
            synchronized (CaTimeManager.class) {
                if (instance == null) {
                    instance = new CaTimeManager();
                }
            }
        }
        return instance;
    }

    public final long getTimeStampForUTC(long timeStamp) {
        Calendar calender = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int hr = calender.get(11);
        int min = calender.get(12);
        return getTimeStampForUTC24((long) (((((hr * 3600) + (min * 60)) + calender.get(13)) * 1000) + calender.get(14)), calender.getTimeInMillis(), timeStamp);
    }

    public final long getTimeStampForUTC24(long curUtcTime, long curTimeMilliSec, long timeStamp) {
        long diff = curUtcTime - timeStamp;
        if (diff < 0) {
            if (diff < -84500000) {
                diff += DateUtils.DAY_IN_MILLIS;
            } else {
                diff = 0;
            }
        }
        return curTimeMilliSec - diff;
    }

    public final long getTimeStampForUTC(long curUtcTime, long curTimeMilliSec, long timeStamp) {
        long diff = curUtcTime - timeStamp;
        if (diff < 0) {
            diff += DateUtils.DAY_IN_MILLIS;
        }
        return curTimeMilliSec - diff;
    }

    public final void sendCurTimeToSensorHub() {
        dataPacket = new byte[3];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        dataPacket[0] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        dataPacket[1] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        dataPacket[2] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        int result = SensorHubCommManager.getInstance().sendCmdToSensorHub(dataPacket, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, (byte) 14);
        if (result != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(result));
        }
    }
}
