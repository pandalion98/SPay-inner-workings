package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.R;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaTelephonyManager implements IUtilManager {
    private static final byte CALL_STATE_ACTIVE = (byte) -39;
    private static final byte CALL_STATE_IDLE = (byte) -40;
    private static int CALL_STATE_IDLE_1 = 1;
    private static int CALL_STATE_INCOMING_ANSWERED = 3;
    private static int CALL_STATE_INCOMING_MISSED = 4;
    private static int CALL_STATE_INCOMING_RINGING = 2;
    private static volatile CaTelephonyManager instance;
    private CellLocation mCellLocation;
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    CaLogger.info("CALL_STATE_IDLE");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-40);
                    return;
                case 1:
                    CaLogger.info("CALL_STATE_RINGING");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-39);
                    return;
                case 2:
                    CaLogger.info("CALL_STATE_OFFHOOK");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-39);
                    return;
                default:
                    CaLogger.info("state is unknown (state : " + Integer.toString(state) + ")");
                    return;
            }
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            CellLocation cellLocation = CaTelephonyManager.this.mTelephonyManager.getCellLocation();
            if (cellLocation != null && CaTelephonyManager.this.isCellLocationChanged(cellLocation)) {
                CaTelephonyManager.this.mCellLocation = cellLocation;
                CaTelephonyManager.this.sendCellInfoToSensorHub();
            }
        }
    };
    private TelephonyManager mTelephonyManager;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg = new int[Msg.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.INCOMING_RINGING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.OFF_HOOK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.IDLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static class CallStateMachine extends StateMachine {
        private final IdleState mIdleState = new IdleState();
        private final IncomingAnsweredState mIncomingAnsweredState = new IncomingAnsweredState();
        private final IncomingState mIncomingState = new IncomingState();

        class IdleState extends State {
            IdleState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (AnonymousClass2.$SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.values()[message.what].ordinal()]) {
                    case 1:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIncomingState);
                        break;
                    case 2:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class IncomingAnsweredState extends State {
            IncomingAnsweredState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (AnonymousClass2.$SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.values()[message.what].ordinal()]) {
                    case 2:
                        CaLogger.error("Unexpected call state");
                        break;
                    case 3:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIdleState);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class IncomingState extends State {
            IncomingState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (AnonymousClass2.$SwitchMap$com$samsung$android$contextaware$utilbundle$CaTelephonyManager$Msg[Msg.values()[message.what].ordinal()]) {
                    case 2:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIncomingAnsweredState);
                        break;
                    case 3:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIdleState);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        CallStateMachine(String name) {
            super(name);
            addState(this.mIdleState);
            addState(this.mIncomingState);
            addState(this.mIncomingAnsweredState);
            setInitialState(this.mIdleState);
        }

        public void exit() {
            quit();
        }
    }

    private enum Msg {
        IDLE("MSG_IDLE"),
        INCOMING_RINGING("MSG_INCOMING_RINGING"),
        OFF_HOOK("MSG_OFF_HOOK");
        
        private final String val;

        private Msg(String v) {
            this.val = v;
        }
    }

    public static CaTelephonyManager getInstance() {
        if (instance == null) {
            synchronized (CaTelephonyManager.class) {
                if (instance == null) {
                    instance = new CaTelephonyManager();
                }
            }
        }
        return instance;
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY);
        if (this.mTelephonyManager == null) {
            CaLogger.error("mTelephonyManager is null");
        } else {
            this.mTelephonyManager.listen(this.mPhoneStateListener, R.styleable.Theme_alertDialogCenterButtons);
        }
    }

    public final void terminateManager() {
        if (this.mTelephonyManager != null) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
        }
    }

    private boolean isCellLocationChanged(CellLocation cl) {
        int curCid;
        int curLac;
        int curBid;
        int curNid;
        int curSid;
        if (this.mCellLocation != null) {
            if (this.mCellLocation instanceof GsmCellLocation) {
                if (!(cl instanceof GsmCellLocation)) {
                    return true;
                }
                int prevCid = ((GsmCellLocation) this.mCellLocation).getCid();
                int prevLac = ((GsmCellLocation) this.mCellLocation).getLac();
                curCid = ((GsmCellLocation) cl).getCid();
                curLac = ((GsmCellLocation) cl).getLac();
                if (prevCid == curCid || prevLac == curLac || curCid == 0 || curLac == 0) {
                    return false;
                }
                return true;
            } else if (!(this.mCellLocation instanceof CdmaCellLocation)) {
                return false;
            } else {
                if (!(cl instanceof CdmaCellLocation)) {
                    return true;
                }
                int prevBid = ((CdmaCellLocation) this.mCellLocation).getBaseStationId();
                int prevNid = ((CdmaCellLocation) this.mCellLocation).getNetworkId();
                int prevSid = ((CdmaCellLocation) this.mCellLocation).getSystemId();
                curBid = ((CdmaCellLocation) cl).getBaseStationId();
                curNid = ((CdmaCellLocation) cl).getNetworkId();
                curSid = ((CdmaCellLocation) cl).getSystemId();
                if ((prevBid == curBid && prevNid == curNid && prevSid == curSid) || curBid == 0 || curNid == 0 || curSid == 0) {
                    return false;
                }
                return true;
            }
        } else if (cl instanceof GsmCellLocation) {
            curCid = ((GsmCellLocation) cl).getCid();
            curLac = ((GsmCellLocation) cl).getLac();
            if (curCid == 0 || curLac == 0) {
                return false;
            }
            return true;
        } else if (!(cl instanceof CdmaCellLocation)) {
            return false;
        } else {
            curBid = ((CdmaCellLocation) cl).getBaseStationId();
            curNid = ((CdmaCellLocation) cl).getNetworkId();
            curSid = ((CdmaCellLocation) cl).getSystemId();
            if (curBid == 0 || curNid == 0 || curSid == 0) {
                return false;
            }
            return true;
        }
    }

    private void sendCallStatusToSensorHub(int state) {
        SensorHubCommManager.getInstance().sendCmdToSensorHub(new byte[]{(byte) state, (byte) 0}, ISensorHubCmdProtocol.INST_LIB_NOTI, (byte) 17);
    }

    private void sendCellInfoToSensorHub() {
        byte[] dataPacket = new byte[14];
        boolean toSendData = false;
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        dataPacket[0] = (byte) utcTime[0];
        dataPacket[1] = (byte) utcTime[1];
        dataPacket[2] = (byte) utcTime[2];
        int size;
        if (this.mCellLocation instanceof GsmCellLocation) {
            dataPacket[3] = (byte) 0;
            int cid = ((GsmCellLocation) this.mCellLocation).getCid();
            int lac = ((GsmCellLocation) this.mCellLocation).getLac();
            System.arraycopy(CaConvertUtil.intToByteArr(cid, 4), 0, dataPacket, 4, 4);
            size = 4 + 4;
            System.arraycopy(CaConvertUtil.intToByteArr(lac, 2), 0, dataPacket, size, 2);
            size += 2;
            System.arraycopy(CaConvertUtil.intToByteArr(0, 2), 0, dataPacket, size, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(0, 2), 0, dataPacket, size + 2, 2);
            toSendData = true;
        } else if (this.mCellLocation instanceof CdmaCellLocation) {
            dataPacket[3] = (byte) 1;
            int baseStationId = ((CdmaCellLocation) this.mCellLocation).getBaseStationId();
            int networkId = ((CdmaCellLocation) this.mCellLocation).getNetworkId();
            int systemId = ((CdmaCellLocation) this.mCellLocation).getSystemId();
            System.arraycopy(CaConvertUtil.intToByteArr(baseStationId, 2), 0, dataPacket, 4, 2);
            size = 4 + 2;
            System.arraycopy(CaConvertUtil.intToByteArr(networkId, 2), 0, dataPacket, size, 2);
            size += 2;
            System.arraycopy(CaConvertUtil.intToByteArr(systemId, 2), 0, dataPacket, size, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(0, 4), 0, dataPacket, size + 2, 4);
            toSendData = true;
        }
        if (toSendData) {
            SensorHubCommManager.getInstance().sendCmdToSensorHub(dataPacket, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, (byte) 17);
        }
    }
}
