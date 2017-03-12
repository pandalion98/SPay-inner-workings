package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;
import java.util.List;

abstract class OperationDebugging extends CaAutoTest {
    private final List<byte[]> mPacketList = new ArrayList();

    protected abstract void doDebugging(byte[] bArr);

    protected OperationDebugging(int delayTime) {
        super(delayTime);
    }

    public final void run() {
        int i = 0;
        while (i < this.mPacketList.size()) {
            try {
                Thread.sleep((long) super.getDelayTime());
                if (!super.isStopTest()) {
                    CaLogger.debug("Scenario [" + Integer.toString(i) + "]");
                    for (int j : (byte[]) this.mPacketList.get(i)) {
                        CaLogger.info("Packet = " + Integer.toString(j));
                    }
                    doDebugging((byte[]) this.mPacketList.get(i));
                    i++;
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                CaLogger.exception(e);
                return;
            }
        }
    }

    protected final void addPacket(byte[] packet) {
        this.mPacketList.add(packet);
    }

    protected final void removePacket(byte[] packet) {
        this.mPacketList.remove(packet);
    }

    protected final void clearPacket() {
        this.mPacketList.clear();
    }
}
