package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.INSTRUCTION;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.LIB_TYPE;
import java.util.Random;

class ApPowerStressTest extends InnerProcessStressTest {
    protected ApPowerStressTest(int delayTime) {
        super(delayTime);
    }

    protected final byte[] getPacket(int service) {
        switch (new Random().nextInt(2)) {
            case 0:
                return getPacket((byte) -47);
            case 1:
                return getPacket((byte) -46);
            default:
                return new byte[0];
        }
    }

    private byte[] getPacket(byte status) {
        return new byte[]{INSTRUCTION.INST_NOTI.value, LIB_TYPE.TYPE_NOTI_POWER.value, status};
    }
}
