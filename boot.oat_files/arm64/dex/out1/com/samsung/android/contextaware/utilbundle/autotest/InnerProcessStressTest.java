package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Random;

abstract class InnerProcessStressTest extends CaAutoTest {
    protected abstract byte[] getPacket(int i);

    protected InnerProcessStressTest(int delayTime) {
        super(delayTime);
    }

    public final void run() {
        while (true) {
            try {
                Thread.sleep((long) getDelayTime());
                byte[] packet = getPacket(new Random().nextInt(3));
                if (packet != null && packet.length > 0) {
                    for (int i : packet) {
                        CaLogger.info("Packet = " + Integer.toString(i));
                    }
                    SensorHubParserProvider.getInstance().parseForScenarioTesting(packet);
                    if (super.isStopTest()) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                CaLogger.exception(e);
                return;
            }
        }
    }
}
