package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;

class CaOperationDebugging extends OperationDebugging {
    protected CaOperationDebugging(int delayTime) {
        super(delayTime);
    }

    protected final void doDebugging(byte[] packet) {
        SensorHubParserProvider.getInstance().parseForScenarioTesting(packet);
    }
}
