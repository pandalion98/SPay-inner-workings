package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;

public abstract class VoiceLibProvider extends SensorHubProvider {
    protected VoiceLibProvider(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    protected final byte getInstructionForEnable() {
        return ISensorHubCmdProtocol.INST_VOICE_ADD;
    }

    protected final byte getInstructionForDisable() {
        return ISensorHubCmdProtocol.INST_VOICE_REMOVE;
    }
}
