package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class EADRunner extends LibTypeProvider {
    private float mMode = 0.0f;

    private enum ContextName {
        R((byte) 0),
        G((byte) 1),
        B((byte) 2),
        Lux((byte) 3),
        CCT((byte) 4);
        
        private byte val;

        private ContextName(byte v) {
            this.val = v;
        }
    }

    public EADRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_EAD.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_EAD_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return CaConvertUtil.intToByteArr((int) (this.mMode * 10000.0f), 4);
    }

    public final String[] getContextValueNames() {
        return new String[]{"R", "G", "B", "Lux", "CCT"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 66) {
            float mode = ((Float) ((ContextAwarePropertyBundle) value).getValue()).floatValue();
            CaLogger.info("Mode = " + Float.toString(mode));
            this.mMode = mode;
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EAD_SERVICE, (byte) 1, CaConvertUtil.intToByteArr((int) (10000.0f * mode), 4));
            return true;
        } else if (property != 67) {
            return false;
        } else {
            int duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Duration = " + duration);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EAD_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(duration, 1));
            return true;
        }
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> outerList = new ArrayList();
        ArrayList<SensorHubSyntax> list = new ArrayList();
        String[] names = getContextValueNames();
        list.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, names[ContextName.R.val]));
        list.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, names[ContextName.G.val]));
        list.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, names[ContextName.B.val]));
        list.add(new SensorHubSyntax(DATATYPE.LONG, 1.0d, names[ContextName.Lux.val]));
        list.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.CCT.val]));
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
