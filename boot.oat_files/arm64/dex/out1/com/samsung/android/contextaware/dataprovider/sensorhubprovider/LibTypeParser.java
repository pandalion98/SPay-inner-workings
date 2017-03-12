package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorHandler;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class LibTypeParser extends TypeParser {
    LibTypeParser() {
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        DATA_TYPE[] arr$ = DATA_TYPE.values();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            DATA_TYPE i = arr$[i$];
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else if (packet[tmpNext] != i.value) {
                i$++;
            } else {
                ISensorHubParser parser = super.getParser(i.toString());
                if (parser == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                    return -1;
                }
                if (SensorHubMultiModeParser.getInstance().containsParser(DATA_TYPE.getCode(i.value))) {
                    ISensorHubParser multiModeParser = SensorHubMultiModeParser.getInstance().getParser(DATA_TYPE.getCode(i.value));
                    if (multiModeParser == null) {
                        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
                    } else {
                        tmpNext = multiModeParser.parse(packet, tmpNext + 1);
                    }
                } else {
                    tmpNext = i.value == DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.value ? EnvironmentSensorHandler.getInstance().parse(packet, tmpNext + 1) : parser.parse(packet, tmpNext + 1);
                }
                if (tmpNext == next) {
                    return tmpNext;
                }
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                return -1;
            }
        }
        if (tmpNext == next) {
            return tmpNext;
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
        return -1;
    }
}
