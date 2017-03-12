package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;

class CaOperationStressTest extends InnerProcessStressTest {
    protected CaOperationStressTest(int delayTime) {
        super(delayTime);
    }

    protected final byte[] getPacket(int service) {
        switch (service) {
            case 0:
                return getServicePacket((byte) 5);
            case 1:
                return getServicePacket((byte) 13);
            case 2:
                return getServicePacket((byte) 7);
            default:
                return new byte[0];
        }
    }

    private byte[] getPedometer() {
        return new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 23, ISensorHubCmdProtocol.TYPE_CALL_MOTION_SERVICE, (byte) 75, (byte) 43, (byte) 0, (byte) 3};
    }

    private byte[] getEnvironmentSensorPacket() {
        return new byte[]{(byte) 1, (byte) 1, ISensorHubCmdProtocol.TYPE_ENVIRONMENT_SENSOR_SERVICE, (byte) 2, (byte) 1, (byte) 6, (byte) 0, (byte) 3, (byte) 0, ISensorHubCmdProtocol.TYPE_ACTIVITY_TRACKER_SERVICE, (byte) 0, (byte) 21, (byte) 0, ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE, (byte) 0, (byte) 36, (byte) 0, (byte) 31, (byte) 0, (byte) 36, (byte) 0, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE, (byte) 0, ISensorHubCmdProtocol.TYPE_CALL_MOTION_SERVICE, (byte) 0, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE};
    }

    private byte[] getEnvironmentSensorPacket1() {
        return new byte[]{(byte) 1, (byte) 1, ISensorHubCmdProtocol.TYPE_ENVIRONMENT_SENSOR_SERVICE, (byte) 1, (byte) 1, (byte) 6, (byte) 0, (byte) 1, (byte) 0, (byte) 5, (byte) 0, (byte) 6, (byte) 0, (byte) 7};
    }

    private byte[] getServicePacket(byte libType) {
        return new byte[]{(byte) 1, (byte) 1, libType, (byte) 1};
    }
}
