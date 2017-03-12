package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.test.FlakyTest;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.sensorhub.SensorHubManager;

public class SensorHubCommManager implements IUtilManager {
    private static volatile SensorHubCommManager instance;
    private SensorHubManager mSensorHubManager;

    public static SensorHubCommManager getInstance() {
        if (instance == null) {
            synchronized (SensorHubCommManager.class) {
                if (instance == null) {
                    instance = new SensorHubCommManager();
                }
            }
        }
        return instance;
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        setSensorHubManager((SensorHubManager) context.getSystemService("sensorhub"));
        if (getSensorHubManager() == null) {
            CaLogger.error("mSensorHubManager is null.");
        }
    }

    public final void terminateManager() {
        setSensorHubManager(null);
    }

    public final int sendPacketToSensorHub(int len, byte[] packet) {
        if (getSensorHubManager() == null) {
            return SensorHubErrors.ERROR_SENSOR_HUB_MANAGER_NULL_EXEPTION.getCode();
        }
        int result = getSensorHubManager().SendSensorHubData(getSensorHubManager().getDefaultSensorHub(1), len, packet);
        if (result > 0) {
            result = SensorHubErrors.SUCCESS.getCode();
        } else if (result == -5) {
            result = SensorHubErrors.ERROR_I2C_COMM.getCode();
        } else if (result == -11) {
            result = SensorHubErrors.ERROR_NOT_RECEIVE_ACK.getCode();
        } else {
            result = SensorHubErrors.ERROR_SENSOR_HUB_MANAGER_FAULT.getCode();
        }
        if (result == SensorHubErrors.SUCCESS.getCode()) {
            return result;
        }
        CaLogger.error(SensorHubErrors.getMessage(result));
        String str = CaConvertUtil.byteArrToString(packet);
        if (str == null || str.isEmpty()) {
            CaLogger.warning("Packet is null");
        } else {
            CaLogger.error("Unable to deliver: " + str);
        }
        return SensorHubErrors.SUCCESS.getCode();
    }

    public final int sendCmdToSensorHub(byte[] data, byte... headerData) {
        int headerLength = headerData.length;
        if (headerLength < 2 || headerLength > 4) {
            return SensorHubErrors.ERROR_CMD_PACKET_HEADER_LENGTH.getCode();
        }
        byte[] packet = generatePacket(data, headerData);
        if (packet == null || packet.length <= 0) {
            return SensorHubErrors.ERROR_CMD_PACKET_GENERATION_FAIL.getCode();
        }
        String str = CaConvertUtil.byteArrToString(packet);
        if (str == null || str.isEmpty()) {
            CaLogger.warning("Packet is null");
        } else {
            CaLogger.info(str);
        }
        int result = sendPacketToSensorHub(packet.length, packet);
        if (result == SensorHubErrors.SUCCESS.getCode()) {
            return result;
        }
        CaLogger.error(SensorHubErrors.getMessage(result));
        return result;
    }

    private byte[] generatePacket(byte[] data, byte... headerData) {
        int headerLength = headerData.length;
        if (headerLength < 2 || headerLength > 4) {
            return null;
        }
        byte[] packet = new byte[(data.length + headerLength)];
        byte[] arr$ = headerData;
        int len$ = arr$.length;
        int i$ = 0;
        int index = 0;
        while (i$ < len$) {
            int index2 = index + 1;
            packet[index] = arr$[i$];
            i$++;
            index = index2;
        }
        arr$ = data;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            index2 = index + 1;
            packet[index] = arr$[i$];
            i$++;
            index = index2;
        }
        return packet;
    }

    private void setSensorHubManager(SensorHubManager manager) {
        this.mSensorHubManager = manager;
    }

    private SensorHubManager getSensorHubManager() {
        return this.mSensorHubManager;
    }

    @FlakyTest
    public final byte[] testGeneratePacket(byte[] data, byte... headerData) {
        return generatePacket(data, headerData);
    }

    @FlakyTest
    public final SensorHubManager testGetSensorHubManager() {
        return this.mSensorHubManager;
    }
}
