package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.DataProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ISensorHubRequestParser;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SensorHubProvider extends DataProvider implements ISensorHubParser, ISensorHubCmdProtocol, ISensorHubRequest {
    public static final int I2C_COMM_ERROR = -5;
    public static final int NOT_RECEIVE_ACK = -11;
    private int mFaultDetectionResult;
    private final CopyOnWriteArrayList<ISensorHubRequestParser> mRequestParserList = new CopyOnWriteArrayList();

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE = new int[DATATYPE.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.BYTE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.SHORT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.INTEGER3.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.INTEGER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.LONG.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.FLOAT2.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.FLOAT3.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.FLOAT4.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.DOUBLE2.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.DOUBLE3.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[DATATYPE.DOUBLE4.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    protected abstract byte getInstLibType();

    protected abstract byte getInstructionForDisable();

    protected abstract byte getInstructionForEnable();

    protected SensorHubProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final void initializeManager() {
    }

    protected final void terminateManager() {
    }

    public void enable() {
        byte[] dataPacket = getDataPacketToRegisterLib();
        if (getInstLibType() < (byte) 0 || dataPacket == null || dataPacket.length <= 0) {
            this.mFaultDetectionResult = SensorHubErrors.ERROR_CMD_PACKET_CREATION_FAULT.getCode();
        } else {
            sendCmdToSensorHub(getInstructionForEnable(), getInstLibType(), dataPacket);
        }
    }

    public void disable() {
        byte[] dataPacket = getDataPacketToUnregisterLib();
        if (getInstLibType() < (byte) 0 || getInstructionForDisable() == (byte) 0 || dataPacket == null || dataPacket.length <= 0) {
            this.mFaultDetectionResult = SensorHubErrors.ERROR_CMD_PACKET_CREATION_FAULT.getCode();
        } else {
            sendCmdToSensorHub(getInstructionForDisable(), getInstLibType(), dataPacket);
        }
    }

    public void clear() {
        this.mFaultDetectionResult = 1;
        super.clear();
    }

    protected final void enableForRestore() {
    }

    protected final void disableForRestore() {
    }

    public final void sendCmdToSensorHub(byte inst, byte type, byte[] data) {
        this.mFaultDetectionResult = SensorHubCommManager.getInstance().sendCmdToSensorHub(data, inst, type);
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        ArrayList<ArrayList<SensorHubSyntax>> syntaxListTable = getParseSyntaxTable();
        int tmpNext2;
        if (syntaxListTable == null || syntaxListTable.size() == 0) {
            String name = getContextValueNames()[0];
            if (name == null || name.isEmpty() || (packet.length - tmpNext) - 1 < 0) {
                return -1;
            }
            tmpNext2 = tmpNext + 1;
            super.getContextBean().putContext(name, packet[tmpNext]);
            tmpNext = tmpNext2;
        } else {
            Iterator i$;
            ArrayList<SensorHubSyntax> syntaxTable = null;
            if (((SensorHubSyntax) ((ArrayList) syntaxListTable.get(0)).get(0)).dataType() != DATATYPE.MESSAGE_TYPE) {
                syntaxTable = (ArrayList) syntaxListTable.get(0);
            } else if ((packet.length - tmpNext) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                tmpNext2 = tmpNext + 1;
                int mType = packet[tmpNext];
                i$ = syntaxListTable.iterator();
                while (i$.hasNext()) {
                    ArrayList<SensorHubSyntax> k = (ArrayList) i$.next();
                    if (((SensorHubSyntax) k.get(0)).messageType() == mType) {
                        syntaxTable = k;
                        super.getContextBean().putContext(((SensorHubSyntax) k.get(0)).name(), mType);
                        break;
                    }
                }
                if (syntaxTable == null) {
                    CaLogger.error(SensorHubErrors.ERROR_EMPTY_REQUEST_LIST.getMessage());
                    tmpNext = tmpNext2;
                    return -1;
                }
                tmpNext = tmpNext2;
            }
            int arraySize = 0;
            int temp = 0;
            i$ = syntaxTable.iterator();
            while (i$.hasNext()) {
                SensorHubSyntax i = (SensorHubSyntax) i$.next();
                if ((packet.length - tmpNext) - i.size() < 0) {
                    CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                    return -1;
                }
                byte[] bArr;
                ContextBean contextBean;
                String name2;
                byte[] bArr2;
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[i.dataType().ordinal()]) {
                    case 1:
                        tmpNext2 = tmpNext + 1;
                        super.getContextBean().putContext(i.name(), packet[tmpNext] != (byte) 0);
                        tmpNext = tmpNext2;
                        break;
                    case 2:
                        tmpNext2 = tmpNext + 1;
                        temp = packet[tmpNext];
                        super.getContextBean().putContext(i.name(), temp / ((int) i.scale()));
                        tmpNext = tmpNext2;
                        break;
                    case 3:
                        bArr = new byte[4];
                        tmpNext2 = tmpNext + 1;
                        bArr[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr[3] = packet[tmpNext2];
                        temp = ByteBuffer.wrap(bArr).getInt();
                        super.getContextBean().putContext(i.name(), temp / ((int) i.scale()));
                        break;
                    case 4:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        bArr2[0] = (byte) 0;
                        tmpNext2 = tmpNext + 1;
                        bArr2[1] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[3] = packet[tmpNext];
                        contextBean.putContext(name2, ByteBuffer.wrap(bArr2).getInt() / ((int) i.scale()));
                        tmpNext = tmpNext2;
                        break;
                    case 5:
                        if (!i.name().equals("TimeStamp")) {
                            contextBean = super.getContextBean();
                            name2 = i.name();
                            bArr2 = new byte[4];
                            tmpNext2 = tmpNext + 1;
                            bArr2[0] = packet[tmpNext];
                            tmpNext = tmpNext2 + 1;
                            bArr2[1] = packet[tmpNext2];
                            tmpNext2 = tmpNext + 1;
                            bArr2[2] = packet[tmpNext];
                            tmpNext = tmpNext2 + 1;
                            bArr2[3] = packet[tmpNext2];
                            contextBean.putContext(name2, ByteBuffer.wrap(bArr2).getInt() / ((int) i.scale()));
                            break;
                        }
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        CaTimeManager instance = CaTimeManager.getInstance();
                        r29 = new byte[8];
                        tmpNext2 = tmpNext + 1;
                        r29[4] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        r29[5] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        r29[6] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        r29[7] = packet[tmpNext2];
                        contextBean.putContext(name2, instance.getTimeStampForUTC(ByteBuffer.wrap(r29).getLong()));
                        break;
                    case 6:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[8];
                        tmpNext2 = tmpNext + 1;
                        bArr2[0] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[1] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[3] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[4] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[5] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[6] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[7] = packet[tmpNext2];
                        contextBean.putContext(name2, ByteBuffer.wrap(bArr2).getLong() / ((long) ((int) i.scale())));
                        break;
                    case 7:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        tmpNext2 = tmpNext + 1;
                        bArr2[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[3] = packet[tmpNext2];
                        contextBean.putContext(name2, ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) i.scale()));
                        break;
                    case 8:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        bArr2[0] = (byte) 0;
                        tmpNext2 = tmpNext + 1;
                        bArr2[1] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[3] = packet[tmpNext];
                        contextBean.putContext(name2, ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) i.scale()));
                        tmpNext = tmpNext2;
                        break;
                    case 9:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        tmpNext2 = tmpNext + 1;
                        bArr2[0] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[1] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[3] = packet[tmpNext2];
                        contextBean.putContext(name2, ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) i.scale()));
                        break;
                    case 10:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        tmpNext2 = tmpNext + 1;
                        bArr2[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[3] = packet[tmpNext2];
                        contextBean.putContext(name2, ((double) ByteBuffer.wrap(bArr2).getInt()) / i.scale());
                        break;
                    case 11:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        bArr2[0] = (byte) 0;
                        tmpNext2 = tmpNext + 1;
                        bArr2[1] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[3] = packet[tmpNext];
                        contextBean.putContext(name2, ((double) ByteBuffer.wrap(bArr2).getInt()) / i.scale());
                        tmpNext = tmpNext2;
                        break;
                    case 12:
                        contextBean = super.getContextBean();
                        name2 = i.name();
                        bArr2 = new byte[4];
                        tmpNext2 = tmpNext + 1;
                        bArr2[0] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[1] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr2[2] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr2[3] = packet[tmpNext2];
                        contextBean.putContext(name2, ((double) ByteBuffer.wrap(bArr2).getInt()) / i.scale());
                        break;
                }
                if (i.name().equals("DataSize") || i.name().equals("DataCount")) {
                    arraySize = temp;
                }
                ArrayList<SensorHubSyntax> repeatSyntax = i.repeatList();
                if (repeatSyntax != null) {
                    SensorHubSyntax r;
                    int totalSize = 0;
                    HashMap<String, Object> objMap = new HashMap();
                    Iterator i$2 = repeatSyntax.iterator();
                    while (i$2.hasNext()) {
                        r = (SensorHubSyntax) i$2.next();
                        switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[r.dataType().ordinal()]) {
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                if (!r.name().equals("TimeStampArray")) {
                                    objMap.put(r.name(), new int[arraySize]);
                                    break;
                                }
                                objMap.put(r.name(), new long[arraySize]);
                                break;
                            case 6:
                                objMap.put(r.name(), new long[arraySize]);
                                break;
                            case 7:
                            case 8:
                            case 9:
                                objMap.put(r.name(), new float[arraySize]);
                                break;
                            case 10:
                            case 11:
                            case 12:
                                objMap.put(r.name(), new double[arraySize]);
                                break;
                            default:
                                break;
                        }
                        totalSize += r.size();
                    }
                    if ((packet.length - tmpNext) - (totalSize * arraySize) >= 0) {
                        for (int j = 0; j < arraySize; j++) {
                            i$2 = repeatSyntax.iterator();
                            while (i$2.hasNext()) {
                                r = (SensorHubSyntax) i$2.next();
                                int[] intArray;
                                long[] longArray;
                                float[] floatArray;
                                double[] doubleArray;
                                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[r.dataType().ordinal()]) {
                                    case 2:
                                        intArray = (int[]) objMap.get(r.name());
                                        tmpNext2 = tmpNext + 1;
                                        intArray[j] = packet[tmpNext] / ((int) r.scale());
                                        objMap.put(r.name(), intArray);
                                        tmpNext = tmpNext2;
                                        break;
                                    case 3:
                                        intArray = (int[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        intArray[j] = ByteBuffer.wrap(bArr).getInt() / ((int) r.scale());
                                        objMap.put(r.name(), intArray);
                                        break;
                                    case 4:
                                        intArray = (int[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        bArr[0] = (byte) 0;
                                        tmpNext2 = tmpNext + 1;
                                        bArr[1] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[2] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[3] = packet[tmpNext];
                                        intArray[j] = ByteBuffer.wrap(bArr).getInt() / ((int) r.scale());
                                        objMap.put(r.name(), intArray);
                                        tmpNext = tmpNext2;
                                        break;
                                    case 5:
                                        if (!r.name().equals("TimeStampArray")) {
                                            intArray = (int[]) objMap.get(r.name());
                                            bArr = new byte[4];
                                            tmpNext2 = tmpNext + 1;
                                            bArr[0] = packet[tmpNext];
                                            tmpNext = tmpNext2 + 1;
                                            bArr[1] = packet[tmpNext2];
                                            tmpNext2 = tmpNext + 1;
                                            bArr[2] = packet[tmpNext];
                                            tmpNext = tmpNext2 + 1;
                                            bArr[3] = packet[tmpNext2];
                                            intArray[j] = ByteBuffer.wrap(bArr).getInt() / ((int) r.scale());
                                            objMap.put(r.name(), intArray);
                                            break;
                                        }
                                        longArray = (long[]) objMap.get(r.name());
                                        CaTimeManager instance2 = CaTimeManager.getInstance();
                                        r27 = new byte[8];
                                        tmpNext2 = tmpNext + 1;
                                        r27[4] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        r27[5] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        r27[6] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        r27[7] = packet[tmpNext2];
                                        longArray[j] = instance2.getTimeStampForUTC(ByteBuffer.wrap(r27).getLong());
                                        break;
                                    case 6:
                                        longArray = (long[]) objMap.get(r.name());
                                        bArr = new byte[8];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[0] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[1] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[4] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[5] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[6] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[7] = packet[tmpNext2];
                                        longArray[j] = ByteBuffer.wrap(bArr).getLong() / ((long) ((int) r.scale()));
                                        objMap.put(r.name(), longArray);
                                        break;
                                    case 7:
                                        floatArray = (float[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        floatArray[j] = ((float) ByteBuffer.wrap(bArr).getInt()) / ((float) r.scale());
                                        objMap.put(r.name(), floatArray);
                                        break;
                                    case 8:
                                        floatArray = (float[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        bArr[0] = (byte) 0;
                                        tmpNext2 = tmpNext + 1;
                                        bArr[1] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[2] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[3] = packet[tmpNext];
                                        floatArray[j] = ((float) ByteBuffer.wrap(bArr).getInt()) / ((float) r.scale());
                                        objMap.put(r.name(), floatArray);
                                        tmpNext = tmpNext2;
                                        break;
                                    case 9:
                                        floatArray = (float[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[0] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[1] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        floatArray[j] = ((float) ByteBuffer.wrap(bArr).getInt()) / ((float) r.scale());
                                        objMap.put(r.name(), floatArray);
                                        break;
                                    case 10:
                                        doubleArray = (double[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        doubleArray[j] = ((double) ByteBuffer.wrap(bArr).getInt()) / r.scale();
                                        objMap.put(r.name(), doubleArray);
                                        break;
                                    case 11:
                                        doubleArray = (double[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        bArr[0] = (byte) 0;
                                        tmpNext2 = tmpNext + 1;
                                        bArr[1] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[2] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[3] = packet[tmpNext];
                                        doubleArray[j] = ((double) ByteBuffer.wrap(bArr).getInt()) / r.scale();
                                        objMap.put(r.name(), doubleArray);
                                        tmpNext = tmpNext2;
                                        break;
                                    case 12:
                                        doubleArray = (double[]) objMap.get(r.name());
                                        bArr = new byte[4];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[0] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[1] = packet[tmpNext2];
                                        tmpNext2 = tmpNext + 1;
                                        bArr[2] = packet[tmpNext];
                                        tmpNext = tmpNext2 + 1;
                                        bArr[3] = packet[tmpNext2];
                                        doubleArray[j] = ((double) ByteBuffer.wrap(bArr).getInt()) / r.scale();
                                        objMap.put(r.name(), doubleArray);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        i$2 = repeatSyntax.iterator();
                        while (i$2.hasNext()) {
                            r = (SensorHubSyntax) i$2.next();
                            switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$dataprovider$sensorhubprovider$SensorHubSyntax$DATATYPE[r.dataType().ordinal()]) {
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                    if (!r.name().equals("TimeStampArray")) {
                                        super.getContextBean().putContext(r.name(), (int[]) objMap.get(r.name()));
                                        break;
                                    }
                                    super.getContextBean().putContext(r.name(), (long[]) objMap.get(r.name()));
                                    break;
                                case 6:
                                    super.getContextBean().putContext(r.name(), (long[]) objMap.get(r.name()));
                                    break;
                                case 7:
                                case 8:
                                case 9:
                                    super.getContextBean().putContext(r.name(), (float[]) objMap.get(r.name()));
                                    break;
                                case 10:
                                case 11:
                                case 12:
                                    super.getContextBean().putContext(r.name(), (double[]) objMap.get(r.name()));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                    return -1;
                }
            }
        }
        super.notifyObserver();
        return tmpNext;
    }

    protected byte[] getDataPacketToRegisterLib() {
        return new byte[2];
    }

    protected byte[] getDataPacketToUnregisterLib() {
        return new byte[2];
    }

    protected final boolean sendCommonValueToSensorHub(byte type, byte[] data) {
        int result = SensorHubCommManager.getInstance().sendCmdToSensorHub(data, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, type);
        if (result == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(result));
        return false;
    }

    protected final boolean sendPropertyValueToSensorHub(byte libType, byte dataType, byte[] data) {
        int result = SensorHubCommManager.getInstance().sendCmdToSensorHub(data, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, libType, dataType);
        if (result == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(result));
        return false;
    }

    protected final boolean sendPropertyValueToSensorHub(byte category, byte libType, byte dataType, byte[] data) {
        int result = SensorHubCommManager.getInstance().sendCmdToSensorHub(data, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, category, libType, dataType);
        if (result == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(result));
        return false;
    }

    public final int parseForRequestType(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int requestType = packet[tmpNext];
        if (this.mRequestParserList == null || this.mRequestParserList.isEmpty()) {
            CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_EMPTY_REQUEST_LIST.getCode()));
            tmpNext = tmpNext2;
            return -1;
        }
        Iterator i$ = this.mRequestParserList.iterator();
        while (i$.hasNext()) {
            ISensorHubRequestParser parser = (ISensorHubRequestParser) i$.next();
            if (requestType == parser.getRequestType()) {
                tmpNext = parser.parse(packet, tmpNext2);
                break;
            }
        }
        tmpNext = tmpNext2;
        return tmpNext;
    }

    public final void pause() {
    }

    public final void resume() {
    }

    protected final void reset() {
        enable();
    }

    public Bundle getFaultDetectionResult() {
        int result;
        if (checkFaultDetectionResult()) {
            result = 0;
        } else {
            result = 1;
        }
        return getFaultDetectionResult(result, SensorHubErrors.getMessage(this.mFaultDetectionResult));
    }

    protected final boolean checkFaultDetectionResult() {
        return this.mFaultDetectionResult == SensorHubErrors.SUCCESS.getCode();
    }

    public String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    protected ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        return null;
    }

    protected final void addRequestParser(ISensorHubRequestParser parser) {
        if (this.mRequestParserList != null && !this.mRequestParserList.contains(parser)) {
            this.mRequestParserList.add(parser);
        }
    }

    protected final void removeRequestParser(ISensorHubRequestParser parser) {
        if (this.mRequestParserList != null && this.mRequestParserList.contains(parser)) {
            this.mRequestParserList.remove(parser);
        }
    }
}
