package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public abstract class ActivityTrackerProvider extends LibTypeProvider {
    protected abstract byte getModeType();

    public ActivityTrackerProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        ActivityTrackerHandler.getInstance().initialize(context, looper);
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ACTIVITY_TRACKER_SERVICE;
    }

    public String[] getContextValueNames() {
        return new String[]{"OperationMode", "TimeStamp", "ActivityType", "Accuracy"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] packet = new byte[11];
        packet[0] = getModeType();
        if (getActivityType() < 0) {
            return null;
        }
        byte[] activityType = CaConvertUtil.intToByteArr(getActivityType(), 4);
        packet[1] = activityType[0];
        packet[2] = activityType[1];
        packet[3] = activityType[2];
        packet[4] = activityType[3];
        packet[5] = getAccuracyType();
        byte[] arBatchingPeriod = CaConvertUtil.intToByteArr(getBatchingPeriod(), 2);
        packet[6] = arBatchingPeriod[0];
        packet[7] = arBatchingPeriod[1];
        Calendar temp = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int hour = temp.get(11);
        int minute = temp.get(12);
        int second = temp.get(13);
        packet[8] = (byte) hour;
        packet[9] = (byte) minute;
        packet[10] = (byte) second;
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        byte[] packet = new byte[8];
        packet[0] = getModeType();
        if (getActivityType() < 0) {
            return null;
        }
        byte[] activityType = CaConvertUtil.intToByteArr(getActivityType(), 4);
        packet[1] = activityType[0];
        packet[2] = activityType[1];
        packet[3] = activityType[2];
        packet[4] = activityType[3];
        packet[5] = getAccuracyType();
        byte[] arBatchingPeriod = CaConvertUtil.intToByteArr(getBatchingPeriod(), 2);
        packet[6] = arBatchingPeriod[0];
        packet[7] = arBatchingPeriod[1];
        return packet;
    }

    public void enable() {
        ActivityTrackerHandler.getInstance().enable();
        super.enable();
    }

    public void disable() {
        ActivityTrackerHandler.getInstance().disable();
        super.disable();
    }

    protected int getActivityType() {
        return 0;
    }

    protected byte getAccuracyType() {
        return (byte) 0;
    }

    protected int getBatchingPeriod() {
        return 0;
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] names = getContextValueNames();
        getContextBean().putContext(names[0], (short) getModeType());
        timeTemp = new byte[4];
        int tmpNext2 = tmpNext + 1;
        timeTemp[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        timeTemp[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        timeTemp[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        timeTemp[3] = packet[tmpNext2];
        getContextBean().putContext(names[1], CaTimeManager.getInstance().getTimeStampForUTC(byteArrayToLong(timeTemp)));
        tmpNext = parseData(packet, tmpNext);
        if (tmpNext > 0) {
            notifyObserver();
        }
        return tmpNext;
    }

    protected int parseData(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] names = getContextValueNames();
        int tmpNext2 = tmpNext + 1;
        getContextBean().putContext(names[2], packet[tmpNext]);
        tmpNext = tmpNext2 + 1;
        getContextBean().putContext(names[3], packet[tmpNext2]);
        return tmpNext;
    }

    protected final long byteArrayToLong(byte[] bytes) {
        int i;
        ByteBuffer byte_buf = ByteBuffer.allocate(8);
        byte[] change = new byte[8];
        for (i = 0; i < 8; i++) {
            change[i] = (byte) 0;
        }
        for (i = 0; i < bytes.length; i++) {
            change[7 - i] = bytes[(bytes.length - 1) - i];
        }
        byte_buf = ByteBuffer.wrap(change);
        byte_buf.order(ByteOrder.BIG_ENDIAN);
        return byte_buf.getLong();
    }
}
