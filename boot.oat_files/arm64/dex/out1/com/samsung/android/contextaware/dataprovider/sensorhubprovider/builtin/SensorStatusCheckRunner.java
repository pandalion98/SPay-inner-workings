package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class SensorStatusCheckRunner extends LibTypeProvider {
    private float mMode = 0.0f;

    private enum ContextName {
        Status((byte) 0),
        XAxis((byte) 1),
        YAxis((byte) 2),
        ZAxis((byte) 3);
        
        private byte val;

        private ContextName(byte v) {
            this.val = v;
        }
    }

    public SensorStatusCheckRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SENSOR_STATUS_CHECK.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_SENSOR_STATUS_CHECK_SERVICE;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Status", "XAxis", "YAxis", "ZAxis"};
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> outerList = new ArrayList();
        ArrayList<SensorHubSyntax> list = new ArrayList();
        String[] names = getContextValueNames();
        list.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.Status.val]));
        list.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.XAxis.val]));
        list.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.YAxis.val]));
        list.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.ZAxis.val]));
        outerList.add(list);
        return outerList;
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

    public final void disable() {
        CaLogger.trace();
        super.disable();
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
