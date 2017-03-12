package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class RequestLibTypeParser extends TypeParser implements ISensorHubParser {
    RequestLibTypeParser() {
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
                tmpNext = ((ISensorHubRequest) parser).parseForRequestType(packet, tmpNext + 1);
                return tmpNext;
            }
        }
        return tmpNext;
    }
}
