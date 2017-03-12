package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SensorHubModeHandler extends SensorHubParserBean implements ISensorHubParser {
    protected abstract CopyOnWriteArrayList<String> getModeList();

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if (!super.checkParserMap() || (packet.length - tmpNext) - 1 < 0) {
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int mode = packet[tmpNext];
        if (isDuplicatedMode(mode)) {
            tmpNext = tmpNext2;
            return parseDuplicatedMode(mode, packet, tmpNext2);
        }
        String key = null;
        for (MODE i : MODE.values()) {
            if ((i.value & mode) == i.value) {
                key = getParserKey(mode);
                break;
            }
        }
        if (key == null) {
            CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
            tmpNext = tmpNext2;
            return -1;
        }
        tmpNext = tmpNext2;
        return parse(packet, tmpNext2, key);
    }

    private boolean isDuplicatedMode(int mode) {
        int duplicated = 0;
        for (MODE i : MODE.values()) {
            if ((MODE.BATCH_MODE.value & mode) != MODE.BATCH_MODE.value) {
                duplicated += (i.value & mode) == i.value ? 1 : 0;
            }
        }
        if (duplicated > 1) {
            return true;
        }
        return false;
    }

    private int parseDuplicatedMode(int mode, byte[] packet, int next) {
        int tmpNext = next;
        int repeateNext = next;
        for (MODE i : MODE.values()) {
            if ((MODE.BATCH_MODE.value & mode) != MODE.BATCH_MODE.value && (i.value & mode) == i.value) {
                String key = getParserKey(i.value);
                if (key == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                    return -1;
                }
                tmpNext = parse(packet, repeateNext, key);
            }
        }
        if (tmpNext != repeateNext) {
            return tmpNext;
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
        return -1;
    }

    private int parse(byte[] packet, int next, String key) {
        int tmpNext = next;
        if (!super.checkParserMap(key)) {
            return -1;
        }
        ISensorHubParser parser = super.getParser(key);
        if (parser != null) {
            return parser.parse(packet, next);
        }
        return -1;
    }

    protected final String getParserKey(int type) {
        for (MODE j : MODE.values()) {
            if (type == j.value && super.checkParserMap(j.toString())) {
                return j.toString();
            }
        }
        return null;
    }

    protected final boolean isRunning() {
        Iterator<String> i = getModeList().iterator();
        while (i.hasNext()) {
            if (ListenerListManager.getInstance().getUsedTotalCount((String) i.next()) > 0) {
                return true;
            }
        }
        return false;
    }
}
