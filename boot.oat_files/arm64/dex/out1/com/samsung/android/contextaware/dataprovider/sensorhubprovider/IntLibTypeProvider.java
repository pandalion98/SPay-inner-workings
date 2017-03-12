package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class IntLibTypeProvider extends SensorHubProvider {
    protected IntLibTypeProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final byte getInstructionForEnable() {
        return ISensorHubCmdProtocol.INST_LIB_ADD;
    }

    protected final byte getInstructionForDisable() {
        return ISensorHubCmdProtocol.INST_LIB_REMOVE;
    }

    protected final void getContextInfo(Listener listener) {
        CaLogger.error(ContextAwareServiceErrors.ERROR_NOT_SUPPORT_CMD.getMessage());
    }
}
