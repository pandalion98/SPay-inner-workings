package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SleepMonitorProviderForExtLib;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.TypeParser;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SleepMonitorCurrentInfoRunner extends SleepMonitorProviderForExtLib {
    public SleepMonitorCurrentInfoRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    public final String getContextType() {
        return ContextType.REQUEST_SENSORHUB_SLEEP_MONITOR_CURRENT_INFO.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 1, (byte) 0};
    }

    protected final int parse(int next, byte[] packet) {
        TypeParser libParser = SensorHubParserProvider.getInstance().getLibParser();
        if (libParser == null) {
            CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
            return -1;
        }
        ISensorHubParser parser = libParser.getParser(DATA_TYPE.LIBRARY_DATATYPE_SLEEP_MONITOR.toString());
        if (parser != null) {
            return parser.parse(packet, next);
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
        return -1;
    }

    public final String[] getContextValueNames() {
        return null;
    }

    protected void doTimeOutChecking(Listener listener, Bundle bundle) {
        if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getCode()));
        }
        listener.setContextCollectionResultNotifyCompletion(true);
        notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), bundle);
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    protected final ITimeOutCheckObserver getTimeOutCheckObserver() {
        return this;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
