package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserBean;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SUB_DATA_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class EnvironmentSensorHandler extends SensorHubParserBean {
    private static final int DEFAULT_INTERVAL = 60;
    private static volatile EnvironmentSensorHandler instance;
    private int mInterval = 60;

    public static EnvironmentSensorHandler getInstance() {
        if (instance == null) {
            synchronized (EnvironmentSensorHandler.class) {
                if (instance == null) {
                    instance = new EnvironmentSensorHandler();
                }
            }
        }
        return instance;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if (!super.checkParserMap()) {
            return -1;
        }
        if ((packet.length - tmpNext) - 1 < 0) {
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int loggingStatus = packet[tmpNext];
        if (loggingStatus != 2 && loggingStatus != 1) {
            CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_LOGGING_STATE.getMessage());
            tmpNext = tmpNext2;
            return -1;
        } else if ((packet.length - tmpNext2) - 1 < 0) {
            tmpNext = tmpNext2;
            return -1;
        } else {
            tmpNext = tmpNext2 + 1;
            int packageCount = packet[tmpNext2];
            if (packageCount <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_PACKAGE_SIZE.getMessage());
                return -1;
            }
            int i = 0;
            tmpNext2 = tmpNext;
            while (i < packageCount) {
                if ((packet.length - tmpNext2) - 1 < 0) {
                    tmpNext = tmpNext2;
                    return -1;
                }
                tmpNext = tmpNext2 + 1;
                String key = getParserKey(packet[tmpNext2]);
                if (key != null && super.checkParserMap(key)) {
                    ISensorHubParser parser = super.getParser(key);
                    if (parser != null) {
                        ((EnvironmentSensorProvider) parser).setLoggingStatus(loggingStatus);
                        tmpNext = parser.parse(packet, tmpNext);
                    }
                }
                i++;
                tmpNext2 = tmpNext;
            }
            return tmpNext2;
        }
    }

    private String getParserKey(int type) {
        for (SUB_DATA_TYPE j : SUB_DATA_TYPE.values()) {
            if (type == j.value && super.checkParserMap(j.toString())) {
                return j.toString();
            }
        }
        return null;
    }

    protected final void setInterval(int interval) {
        this.mInterval = interval;
    }

    protected final int getInterval() {
        return this.mInterval;
    }
}
