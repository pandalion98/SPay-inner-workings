package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.ACTIVITY_TRACKER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.PEDOMETER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.POSITIONING_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SLEEP_MONITOR_EXT_LIB_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class ExtLibTypeParser extends TypeParser {
    ExtLibTypeParser() {
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        for (DATA_TYPE i : DATA_TYPE.values()) {
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (packet[tmpNext] == i.value) {
                if (i.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_FOR_POSITIONING.name())) {
                    tmpNext = parseForPositioning(packet, tmpNext + 1);
                    break;
                } else if (i.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_PEDOMETER.name())) {
                    tmpNext = parseForPedometer(packet, tmpNext + 1);
                    break;
                } else if (i.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.name())) {
                    tmpNext = parseForActivityTracker(packet, tmpNext + 1);
                    break;
                } else if (i.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_SLEEP_MONITOR.name())) {
                    tmpNext = parseForSleepMonitorTracker(packet, tmpNext + 1);
                    break;
                }
            }
        }
        return tmpNext;
    }

    private int parseForPositioning(byte[] packet, int next) {
        int tmpNext = next;
        for (POSITIONING_EXT_LIB_TYPE i : POSITIONING_EXT_LIB_TYPE.values()) {
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (packet[tmpNext] == i.value) {
                ISensorHubParser parser = super.getParser(i.toString());
                if (parser != null) {
                    tmpNext = parser.parse(packet, tmpNext + 1);
                    break;
                }
            }
        }
        return tmpNext;
    }

    private int parseForPedometer(byte[] packet, int next) {
        int tmpNext = next;
        for (PEDOMETER_EXT_LIB_TYPE i : PEDOMETER_EXT_LIB_TYPE.values()) {
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (packet[tmpNext] == i.value) {
                ISensorHubParser parser = super.getParser(i.toString());
                if (parser != null) {
                    tmpNext = parser.parse(packet, tmpNext + 1);
                    break;
                }
            }
        }
        return tmpNext;
    }

    private int parseForActivityTracker(byte[] packet, int next) {
        int tmpNext = next;
        for (ACTIVITY_TRACKER_EXT_LIB_TYPE i : ACTIVITY_TRACKER_EXT_LIB_TYPE.values()) {
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (packet[tmpNext] == i.value) {
                ISensorHubParser parser = super.getParser(i.toString());
                if (parser != null) {
                    if ((packet.length - tmpNext) - 1 < 0) {
                        CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                        return -1;
                    }
                    int tmpNext2 = tmpNext + 1;
                    if ((MODE.NORMAL_MODE.value & packet[tmpNext]) != MODE.NORMAL_MODE.value) {
                        CaLogger.error(SensorHubErrors.ERROR_MODE_FAULT.getMessage());
                        tmpNext = tmpNext2;
                        return -1;
                    }
                    tmpNext = parser.parse(packet, tmpNext2 + 1);
                    return tmpNext;
                }
            }
        }
        return tmpNext;
    }

    private int parseForSleepMonitorTracker(byte[] packet, int next) {
        int tmpNext = next;
        for (SLEEP_MONITOR_EXT_LIB_TYPE i : SLEEP_MONITOR_EXT_LIB_TYPE.values()) {
            if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (packet[tmpNext] == i.value) {
                ISensorHubParser parser = super.getParser(i.toString());
                if (parser != null) {
                    if ((packet.length - tmpNext) - 1 < 0) {
                        CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                        return -1;
                    }
                    int tmpNext2 = tmpNext + 1;
                    if ((MODE.NORMAL_MODE.value & packet[tmpNext]) != MODE.NORMAL_MODE.value) {
                        CaLogger.error(SensorHubErrors.ERROR_MODE_FAULT.getMessage());
                        tmpNext = tmpNext2;
                        return -1;
                    }
                    tmpNext = parser.parse(packet, tmpNext2 + 1);
                    return tmpNext;
                }
            }
        }
        return tmpNext;
    }
}
