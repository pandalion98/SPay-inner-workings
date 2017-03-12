package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.INSTRUCTION;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.LIB_TYPE;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.sensorhub.SensorHubEvent;
import com.samsung.android.sensorhub.SensorHubEventListener;
import com.samsung.android.sensorhub.SensorHubManager;

public class SensorHubParserProvider extends SensorHubParserProtocol implements IApPowerObserver {
    private static final int PACKET_MAX_SIZE = 16384;
    private static volatile SensorHubParserProvider instance;
    private int mApStatus;
    private TypeParser mExtLibParser;
    private TypeParser mLibParser;
    private TypeParser mRequestLibParser;
    private final SensorHubEventListener mSensorHubListener = new SensorHubEventListener() {
        public synchronized void onGetSensorHubData(SensorHubEvent event) {
            String apStatus = "AP_NONE";
            if (SensorHubParserProvider.this.mApStatus == -46) {
                apStatus = "AP_SLEEP";
                CaTimeManager.getInstance().sendCurTimeToSensorHub();
            } else if (SensorHubParserProvider.this.mApStatus == -47) {
                apStatus = "AP_WAKEUP";
            }
            CaLogger.debug("onGetSensorHubData Event [event buffer len :" + Integer.toString(event.length) + "], " + apStatus);
            if (SensorHubParserProvider.this.mLibParser == null) {
                CaLogger.warning(SensorHubErrors.ERROR_LIBRARY_PARSER_NULL_EXEPTION.getMessage());
            } else if (!SensorHubParserProvider.this.mLibParser.checkParserMap()) {
                CaLogger.warning(SensorHubErrors.ERROR_EMPTY_PARSER_MAP.getMessage());
            } else if (event.length <= 0) {
                CaLogger.warning(SensorHubErrors.ERROR_GET_SENSOR_HUB_EVENT.getMessage());
            } else {
                int result = SensorHubParserProvider.this.parse(event.buffer);
                if (result != SensorHubErrors.SUCCESS.getCode()) {
                    CaLogger.error(SensorHubErrors.getMessage(result));
                }
            }
        }
    };
    private SensorHubManager mSensorHubManager;

    public static SensorHubParserProvider getInstance() {
        if (instance == null) {
            synchronized (SensorHubParserProvider.class) {
                if (instance == null) {
                    instance = new SensorHubParserProvider();
                }
            }
        }
        return instance;
    }

    public final void initialize(Context context) {
        if (this.mSensorHubManager == null) {
            this.mSensorHubManager = (SensorHubManager) context.getSystemService("sensorhub");
            if (this.mSensorHubManager == null) {
                CaLogger.error("mSensorHubManager is null.");
                return;
            }
            HandlerThread handlerThread = new HandlerThread("CAESHubEvtHler");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            if (looper == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_LOOPER_NULL_EXCEPTION.getMessage());
                return;
            }
            this.mSensorHubManager.registerListener(this.mSensorHubListener, this.mSensorHubManager.getDefaultSensorHub(1), 0, new Handler(looper));
        }
        this.mLibParser = new LibTypeParser();
        this.mExtLibParser = new ExtLibTypeParser();
        this.mRequestLibParser = new RequestLibTypeParser();
        this.mApStatus = 0;
        CaPowerManager.getInstance().registerApPowerObserver(this);
    }

    public final void terminate() {
        if (this.mSensorHubManager != null) {
            this.mApStatus = 0;
            CaPowerManager.getInstance().unregisterApPowerObserver(this);
            this.mSensorHubManager.unregisterListener(this.mSensorHubListener);
            this.mSensorHubManager = null;
            this.mLibParser = null;
            this.mExtLibParser = null;
            this.mRequestLibParser = null;
        }
    }

    private int parse(byte[] dataPacket) {
        byte[] packet = (byte[]) dataPacket.clone();
        CaLogger.info("buffer size = " + Integer.toString(packet.length));
        int checkResult = checkPacketSize(packet);
        if (checkResult != SensorHubErrors.SUCCESS.getCode()) {
            return checkResult;
        }
        String str = CaConvertUtil.byteArrToString(packet);
        if (str == null || str.isEmpty()) {
            CaLogger.warning("Packet is null");
        } else {
            CaLogger.info(str);
        }
        int i = 0;
        while (i < packet.length) {
            if ((packet.length - i) - 2 < 0) {
                return SensorHubErrors.ERROR_PACKET_HEADER_LENGTH.getCode();
            }
            if (!checkInstruction(packet[i])) {
                return SensorHubErrors.ERROR_INSTRUCTION_FIELD_PARSING.getCode();
            }
            byte inst = packet[i];
            i++;
            if (!checkLibType(packet[i])) {
                return SensorHubErrors.ERROR_TYPE_FIELD_PARSING.getCode();
            }
            i = parseData(inst, packet[i], packet, i + 1);
            if (i < 0) {
                return SensorHubErrors.ERROR_DATA_FIELD_PARSING.getCode();
            }
        }
        return SensorHubErrors.SUCCESS.getCode();
    }

    public final void parseForScenarioTesting(byte[] packet) {
        int result = parse(packet);
        if (result != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(result));
        }
    }

    private int checkPacketSize(byte[] packet) {
        if (packet.length <= 0) {
            return SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode();
        }
        if (packet.length > 16384) {
            return SensorHubErrors.ERROR_PACKET_LENGTH_OVERFLOW.getCode();
        }
        return SensorHubErrors.SUCCESS.getCode();
    }

    private int parseData(byte inst, byte type, byte[] packet, int next) {
        int tmpNext = next;
        if (this.mLibParser == null || !this.mLibParser.checkParserMap()) {
            CaLogger.error(SensorHubErrors.ERROR_LIBRARY_PARSER_OBJECT.getMessage());
            return -1;
        }
        int parsingResult = parseNotiPowerData(inst, type, packet, tmpNext);
        if (parsingResult > 0) {
            return parsingResult;
        }
        parsingResult = parseDebugMsg(inst, type, packet, tmpNext);
        if (parsingResult > 0) {
            return parsingResult;
        }
        if (inst != INSTRUCTION.INST_LIBRARY.value) {
            CaLogger.error(SensorHubErrors.ERROR_INSTRUCTION_VALUE.getMessage());
            return -1;
        }
        if (type == LIB_TYPE.TYPE_LIBRARY.value) {
            tmpNext = this.mLibParser.parse(packet, tmpNext);
        } else if (type == LIB_TYPE.TYPE_LIBRARY_EXT.value) {
            tmpNext = this.mExtLibParser.parse(packet, tmpNext);
        } else if (type == LIB_TYPE.TYPE_LIBRARY_REQUEST.value) {
            tmpNext = this.mRequestLibParser.parse(packet, tmpNext);
        } else {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
            tmpNext = -1;
        }
        return tmpNext;
    }

    private int parseNotiPowerData(byte inst, byte type, byte[] packet, int next) {
        int tmpNext = next;
        ISensorHubParser notiParser = this.mLibParser.getParser(LIB_TYPE.TYPE_NOTI_POWER.toString());
        if (inst == INSTRUCTION.INST_NOTI.value && type == LIB_TYPE.TYPE_NOTI_POWER.value && notiParser != null) {
            return notiParser.parse(packet, tmpNext);
        }
        return -1;
    }

    private int parseDebugMsg(byte inst, byte type, byte[] packet, int next) {
        int tmpNext = next;
        ISensorHubParser debugMsgParser = this.mLibParser.getParser(LIB_TYPE.TYPE_SENSORHUB_DEBUG_MSG.toString());
        if (inst == INSTRUCTION.INST_LIBRARY.value && type == LIB_TYPE.TYPE_SENSORHUB_DEBUG_MSG.value && debugMsgParser != null) {
            return debugMsgParser.parse(packet, tmpNext);
        }
        return -1;
    }

    private boolean checkInstruction(byte inst) {
        for (INSTRUCTION i : INSTRUCTION.values()) {
            if (i.value == inst) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLibType(byte type) {
        for (LIB_TYPE i : LIB_TYPE.values()) {
            if (i.value == type) {
                return true;
            }
        }
        return false;
    }

    public final TypeParser getLibParser() {
        return this.mLibParser;
    }

    public final TypeParser getExtLibParser() {
        return this.mExtLibParser;
    }

    public final TypeParser getRequestLibParser() {
        return this.mRequestLibParser;
    }

    public final void updateApPowerStatus(int status, long timeStamp) {
        this.mApStatus = status;
        String apStatus = "AP_NONE";
        if (this.mApStatus == -46) {
            apStatus = "AP_SLEEP";
        } else if (this.mApStatus == -47) {
            apStatus = "AP_WAKEUP";
        }
        CaLogger.info(apStatus);
    }

    public final void initializePreparedSubCollection() {
    }
}
