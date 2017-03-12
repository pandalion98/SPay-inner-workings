package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
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
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class DevicePhysicalContextMonitorRunner extends LibTypeProvider {

    private enum ContextName {
        AODStatus((byte) 0),
        VersionYear((byte) 1),
        VersionMonth((byte) 2),
        VersionDay((byte) 3),
        VersionRevision((byte) 4),
        AODReason((byte) 5);
        
        private byte val;

        private ContextName(byte v) {
            this.val = v;
        }
    }

    public DevicePhysicalContextMonitorRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[4];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        packet[1] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        packet[2] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        packet[3] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 0};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 7) {
            CaLogger.info("Get status");
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 1, (byte) 1});
            return true;
        } else if (property == 8) {
            CaLogger.info("Get version");
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 2, (byte) 1});
            return true;
        } else if (property == 80) {
            int aod;
            try {
                aod = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e) {
                CaLogger.error("DPCM setProperty Exception: " + e.getMessage().toString() + ", sensorProx = 1");
                aod = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 1, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(aod, 1)[0]});
            return true;
        } else if (property == 81) {
            int sensorProx;
            try {
                sensorProx = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e2) {
                CaLogger.error("DPCM setProperty Exception: " + e2.getMessage().toString() + ", sensorProx = 1");
                sensorProx = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 2, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(sensorProx, 1)[0]});
            return true;
        } else if (property == 82) {
            int sensorBright;
            try {
                sensorBright = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e22) {
                CaLogger.error("DPCM setProperty Exception: " + e22.getMessage().toString() + ", sensorBright = 1");
                sensorBright = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 2, new byte[]{(byte) 2, CaConvertUtil.intToByteArr(sensorBright, 1)[0]});
            return true;
        } else if (property == 83) {
            int aodDuration;
            try {
                aodDuration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e222) {
                CaLogger.error("DPCM setProperty Exception: " + e222.getMessage().toString() + ", aodDuration = 600");
                aodDuration = 600;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(aodDuration, 2)[0], CaConvertUtil.intToByteArr(aodDuration, 2)[1]});
            return true;
        } else if (property == 84) {
            try {
                duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e2222) {
                CaLogger.error("DPCM setProperty Exception: " + e2222.getMessage().toString() + ", duration = 3*1000");
                duration = 3000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 2, CaConvertUtil.intToByteArr(duration, 2)[0], CaConvertUtil.intToByteArr(duration, 2)[1]});
            return true;
        } else if (property == 85) {
            try {
                duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e22222) {
                CaLogger.error("DPCM setProperty Exception: " + e22222.getMessage().toString() + ", duration = 60*1000");
                duration = 60000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 3, CaConvertUtil.intToByteArr(duration, 2)[0], CaConvertUtil.intToByteArr(duration, 2)[1]});
            return true;
        } else if (property == 86) {
            try {
                duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e222222) {
                CaLogger.error("DPCM setProperty Exception: " + e222222.getMessage().toString() + ", duration = 3*1000");
                duration = 3000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 4, CaConvertUtil.intToByteArr(duration, 2)[0], CaConvertUtil.intToByteArr(duration, 2)[1]});
            return true;
        } else if (property == 87) {
            try {
                scenario = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e2222222) {
                CaLogger.error("DPCM setProperty Exception: " + e2222222.getMessage().toString() + ", scenario = 15");
                scenario = 15;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 4, new byte[]{CaConvertUtil.intToByteArr(scenario, 4)[0], CaConvertUtil.intToByteArr(scenario, 4)[1], CaConvertUtil.intToByteArr(scenario, 4)[2], CaConvertUtil.intToByteArr(scenario, 4)[3], (byte) 1});
            return true;
        } else if (property != 88) {
            return false;
        } else {
            try {
                scenario = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            } catch (Exception e22222222) {
                CaLogger.error("DPCM setProperty Exception: " + e22222222.getMessage().toString() + ", scenario = 15");
                scenario = 15;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 4, new byte[]{CaConvertUtil.intToByteArr(scenario, 4)[0], CaConvertUtil.intToByteArr(scenario, 4)[1], CaConvertUtil.intToByteArr(scenario, 4)[2], CaConvertUtil.intToByteArr(scenario, 4)[3], (byte) 2});
            return true;
        }
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> outerList = new ArrayList();
        ArrayList<SensorHubSyntax> aodStatusList = new ArrayList();
        ArrayList<SensorHubSyntax> versionList = new ArrayList();
        String[] names = getContextValueNames();
        aodStatusList.add(new SensorHubSyntax((byte) 1));
        aodStatusList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.AODStatus.val]));
        aodStatusList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.AODReason.val]));
        versionList.add(new SensorHubSyntax((byte) 3));
        versionList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.VersionYear.val]));
        versionList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.VersionMonth.val]));
        versionList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.VersionDay.val]));
        versionList.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, names[ContextName.VersionRevision.val]));
        outerList.add(aodStatusList);
        outerList.add(versionList);
        return outerList;
    }

    public final String[] getContextValueNames() {
        return new String[]{"AODStatus", "VersionYear", "VersionMonth", "VersionDay", "VersionRevision", "AODReason"};
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
