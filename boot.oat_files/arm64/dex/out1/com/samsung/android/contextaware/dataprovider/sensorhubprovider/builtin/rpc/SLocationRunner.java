package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.rpc;

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
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class SLocationRunner extends LibTypeProvider {

    private enum ContextName {
        GeoFenceId((byte) 0),
        GeoFenceStatus((byte) 1),
        Latitude((byte) 2),
        Longitude((byte) 3),
        TotalGpsCount((byte) 4),
        SuccessGpsCount((byte) 5),
        Distance((byte) 6),
        Timestamp((byte) 7),
        Accuracy((byte) 8),
        FunctionType((byte) 9),
        ErrorCode((byte) 10),
        EventTypeArray(ISensorHubCmdProtocol.TYPE_STOP_ALERT_SERVICE),
        EventStatusArray(ISensorHubCmdProtocol.TYPE_ENVIRONMENT_SENSOR_SERVICE),
        DataArray((byte) 13),
        TimestampArray((byte) 14),
        DataCount((byte) 15);
        
        private byte val;

        private ContextName(byte v) {
            this.val = v;
        }
    }

    public SLocationRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SLOCATION.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        int[] data;
        ByteBuffer byteBuffer;
        if (property == 68) {
            CaLogger.info("Add");
            data = (int[]) ((ContextAwarePropertyBundle) value).getValue();
            if (data.length != 6) {
                CaLogger.error("missing data");
                return false;
            }
            byteBuffer = ByteBuffer.allocate(data.length * 4);
            byteBuffer.asIntBuffer().put(data);
            byte[] byteArray = byteBuffer.array();
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 1, Arrays.copyOfRange(byteArray, 2, byteArray.length));
            return true;
        } else if (property == 69) {
            CaLogger.info("Remove");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue(), 2));
            return true;
        } else if (property == 70) {
            CaLogger.info("PauseResume");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 3, CaConvertUtil.intToByteArr(((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue(), 2));
            return true;
        } else if (property == 71) {
            CaLogger.info("Update");
            data = (int[]) ((ContextAwarePropertyBundle) value).getValue();
            if (data.length != 3) {
                CaLogger.error("missing data");
                return false;
            }
            outStream = new ByteArrayOutputStream();
            try {
                outStream.write(CaConvertUtil.intToByteArr(data[0], 2));
                outStream.write(CaConvertUtil.intToByteArr(data[1], 4));
                outStream.write(CaConvertUtil.intToByteArr(data[2], 1));
            } catch (IOException e) {
                CaLogger.error("error converting");
                e.printStackTrace();
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 4, outStream.toByteArray());
            return true;
        } else if (property == 72) {
            CaLogger.info("Set Loc");
            data = (int[]) ((ContextAwarePropertyBundle) value).getValue();
            if (data.length != 5) {
                CaLogger.error("missing data");
                return false;
            }
            byteBuffer = ByteBuffer.allocate(data.length * 4);
            byteBuffer.asIntBuffer().put(data);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 5, byteBuffer.array());
            return true;
        } else if (property == 73) {
            CaLogger.info("Start AR");
            data = (int[]) ((ContextAwarePropertyBundle) value).getValue();
            if (data.length != 2) {
                CaLogger.error("missing data");
                return false;
            }
            outStream = new ByteArrayOutputStream();
            try {
                outStream.write(CaConvertUtil.intToByteArr(data[0], 4));
                outStream.write(CaConvertUtil.intToByteArr(data[1], 1));
            } catch (IOException e2) {
                CaLogger.error("error converting");
                e2.printStackTrace();
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 6, outStream.toByteArray());
            return true;
        } else if (property == 74) {
            CaLogger.info("Stop AR");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 7, new byte[]{(byte) 0});
            return true;
        } else if (property != 75) {
            return false;
        } else {
            CaLogger.info("CurLoc func");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE, (byte) 8, CaConvertUtil.intToByteArr(((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue(), 1));
            return true;
        }
    }

    protected void display() {
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> outerList = new ArrayList();
        ArrayList<SensorHubSyntax> coreDetectList = new ArrayList();
        ArrayList<SensorHubSyntax> gpsCountList = new ArrayList();
        ArrayList<SensorHubSyntax> ARList = new ArrayList();
        ArrayList<SensorHubSyntax> coreDistList = new ArrayList();
        ArrayList<SensorHubSyntax> coreErrorList = new ArrayList();
        ArrayList<SensorHubSyntax> dumpStateList = new ArrayList();
        String[] names = getContextValueNames();
        coreDetectList.add(new SensorHubSyntax((byte) 1));
        coreDetectList.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, names[ContextName.GeoFenceId.val]));
        coreDetectList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.GeoFenceStatus.val]));
        coreDetectList.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, names[ContextName.Latitude.val]));
        coreDetectList.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, names[ContextName.Longitude.val]));
        coreDetectList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.Accuracy.val]));
        gpsCountList.add(new SensorHubSyntax((byte) 2));
        gpsCountList.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, names[ContextName.GeoFenceId.val]));
        gpsCountList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.TotalGpsCount.val]));
        gpsCountList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.SuccessGpsCount.val]));
        ARList.add(new SensorHubSyntax((byte) 3));
        coreDistList.add(new SensorHubSyntax((byte) 4));
        coreDistList.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10.0d, names[ContextName.Distance.val]));
        coreDistList.add(new SensorHubSyntax(DATATYPE.LONG, 1.0d, names[ContextName.Timestamp.val]));
        coreDistList.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, names[ContextName.Latitude.val]));
        coreDistList.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, names[ContextName.Longitude.val]));
        coreDistList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.Accuracy.val]));
        coreErrorList.add(new SensorHubSyntax((byte) 5));
        coreErrorList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.FunctionType.val]));
        coreErrorList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.ErrorCode.val]));
        coreErrorList.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, names[ContextName.GeoFenceId.val]));
        dumpStateList.add(new SensorHubSyntax((byte) 6));
        dumpStateList.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, names[ContextName.DataCount.val]));
        ArrayList repeatList = new ArrayList();
        repeatList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.EventTypeArray.val]));
        repeatList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.EventStatusArray.val]));
        repeatList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.DataArray.val]));
        repeatList.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, names[ContextName.TimestampArray.val]));
        dumpStateList.add(new SensorHubSyntax(repeatList));
        outerList.add(coreDetectList);
        outerList.add(gpsCountList);
        outerList.add(ARList);
        outerList.add(coreDistList);
        outerList.add(coreErrorList);
        outerList.add(dumpStateList);
        return outerList;
    }

    public final String[] getContextValueNames() {
        return new String[]{"GeoFenceId", "GeoFenceStatus", "Latitude", "Longitude", "TotalGpsCount", "SuccessGpsCount", "Distance", "Timestamp", "Accuracy", "FunctionType", "ErrorCode", "EventTypeArray", "EventStatusArray", "DataArray", "TimeStampArray", "DataCount"};
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

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
