package android.telephony;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.IPhoneStateListener.Stub;
import java.util.List;

public class PhoneStateListener {
    private static final boolean DBG = false;
    public static final int LISTEN_CALL_FORWARDING_INDICATOR = 8;
    public static final int LISTEN_CALL_STATE = 32;
    public static final int LISTEN_CARRIER_NETWORK_CHANGE = 65536;
    public static final int LISTEN_CELL_INFO = 1024;
    public static final int LISTEN_CELL_LOCATION = 16;
    public static final int LISTEN_DATA_ACTIVITY = 128;
    public static final int LISTEN_DATA_CONNECTION_REAL_TIME_INFO = 8192;
    public static final int LISTEN_DATA_CONNECTION_STATE = 64;
    public static final int LISTEN_DUN_DATA_ACTIVITY = 262144;
    public static final int LISTEN_DUN_DATA_CONNECTION_STATE = 524288;
    public static final int LISTEN_FDN_UPDATED = 131072;
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 4;
    public static final int LISTEN_NONE = 0;
    public static final int LISTEN_OEM_HOOK_RAW_EVENT = 32768;
    public static final int LISTEN_OTASP_CHANGED = 512;
    public static final int LISTEN_PRECISE_CALL_STATE = 2048;
    public static final int LISTEN_PRECISE_DATA_CONNECTION_STATE = 4096;
    public static final int LISTEN_SERVICE_STATE = 1;
    @Deprecated
    public static final int LISTEN_SIGNAL_STRENGTH = 2;
    public static final int LISTEN_SIGNAL_STRENGTHS = 256;
    public static final int LISTEN_VOICE_RADIO_BEARER_HO_STATE = 1048576;
    public static final int LISTEN_VOLTE_STATE = 16384;
    private static final String LOG_TAG = "PhoneStateListener";
    IPhoneStateListener callback;
    private final Handler mHandler;
    protected int mSubId;

    public PhoneStateListener() {
        this(Integer.MAX_VALUE, Looper.myLooper());
    }

    public PhoneStateListener(Looper looper) {
        this(Integer.MAX_VALUE, looper);
    }

    public PhoneStateListener(int subId) {
        this(subId, Looper.myLooper());
    }

    public PhoneStateListener(int subId, Looper looper) {
        this.mSubId = -1;
        this.callback = new Stub() {
            public void onServiceStateChanged(ServiceState serviceState) {
                Message.obtain(PhoneStateListener.this.mHandler, 1, 0, 0, serviceState).sendToTarget();
            }

            public void onSignalStrengthChanged(int asu) {
                Message.obtain(PhoneStateListener.this.mHandler, 2, asu, 0, null).sendToTarget();
            }

            public void onMessageWaitingIndicatorChanged(boolean mwi) {
                int i;
                Handler access$000 = PhoneStateListener.this.mHandler;
                if (mwi) {
                    i = 1;
                } else {
                    i = 0;
                }
                Message.obtain(access$000, 4, i, 0, null).sendToTarget();
            }

            public void onCallForwardingIndicatorChanged(boolean cfi) {
                int i;
                Handler access$000 = PhoneStateListener.this.mHandler;
                if (cfi) {
                    i = 1;
                } else {
                    i = 0;
                }
                Message.obtain(access$000, 8, i, 0, null).sendToTarget();
            }

            public void onCellLocationChanged(Bundle bundle) {
                Message.obtain(PhoneStateListener.this.mHandler, 16, 0, 0, CellLocation.newFromBundle(bundle)).sendToTarget();
            }

            public void onCallStateChanged(int state, String incomingNumber) {
                Message.obtain(PhoneStateListener.this.mHandler, 32, state, 0, incomingNumber).sendToTarget();
            }

            public void onDataConnectionStateChanged(int state, int networkType) {
                Message.obtain(PhoneStateListener.this.mHandler, 64, state, networkType).sendToTarget();
            }

            public void onDataActivity(int direction) {
                Message.obtain(PhoneStateListener.this.mHandler, 128, direction, 0, null).sendToTarget();
            }

            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                Message.obtain(PhoneStateListener.this.mHandler, 256, 0, 0, signalStrength).sendToTarget();
            }

            public void onOtaspChanged(int otaspMode) {
                Message.obtain(PhoneStateListener.this.mHandler, 512, otaspMode, 0).sendToTarget();
            }

            public void onCellInfoChanged(List<CellInfo> cellInfo) {
                Message.obtain(PhoneStateListener.this.mHandler, 1024, 0, 0, cellInfo).sendToTarget();
            }

            public void onPreciseCallStateChanged(PreciseCallState callState) {
                Message.obtain(PhoneStateListener.this.mHandler, 2048, 0, 0, callState).sendToTarget();
            }

            public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) {
                Message.obtain(PhoneStateListener.this.mHandler, 4096, 0, 0, dataConnectionState).sendToTarget();
            }

            public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) {
                Message.obtain(PhoneStateListener.this.mHandler, 8192, 0, 0, dcRtInfo).sendToTarget();
            }

            public void onVoLteServiceStateChanged(VoLteServiceState lteState) {
                Message.obtain(PhoneStateListener.this.mHandler, 16384, 0, 0, lteState).sendToTarget();
            }

            public void onOemHookRawEvent(byte[] rawData) {
                Message.obtain(PhoneStateListener.this.mHandler, 32768, 0, 0, rawData).sendToTarget();
            }

            public void onCarrierNetworkChange(boolean active) {
                Message.obtain(PhoneStateListener.this.mHandler, 65536, 0, 0, Boolean.valueOf(active)).sendToTarget();
            }

            public void onFdnUpdated() {
                Message.obtain(PhoneStateListener.this.mHandler, 131072, 0, 0, null).sendToTarget();
            }

            public void onDunDataActivity(int direction) {
                Message.obtain(PhoneStateListener.this.mHandler, 262144, direction, 0, null).sendToTarget();
            }

            public void onDunDataConnectionStateChanged(int state, int networkType) {
                Message.obtain(PhoneStateListener.this.mHandler, 524288, state, networkType, null).sendToTarget();
            }

            public void onVoiceRadioBearerHoStateChanged(int state) {
                Message.obtain(PhoneStateListener.this.mHandler, 1048576, state, 0, null).sendToTarget();
            }
        };
        this.mSubId = subId;
        this.mHandler = new Handler(looper) {
            public void handleMessage(Message msg) {
                boolean z = true;
                PhoneStateListener phoneStateListener;
                switch (msg.what) {
                    case 1:
                        PhoneStateListener.this.onServiceStateChanged((ServiceState) msg.obj);
                        return;
                    case 2:
                        PhoneStateListener.this.onSignalStrengthChanged(msg.arg1);
                        return;
                    case 4:
                        phoneStateListener = PhoneStateListener.this;
                        if (msg.arg1 == 0) {
                            z = false;
                        }
                        phoneStateListener.onMessageWaitingIndicatorChanged(z);
                        return;
                    case 8:
                        phoneStateListener = PhoneStateListener.this;
                        if (msg.arg1 == 0) {
                            z = false;
                        }
                        phoneStateListener.onCallForwardingIndicatorChanged(z);
                        return;
                    case 16:
                        PhoneStateListener.this.onCellLocationChanged((CellLocation) msg.obj);
                        return;
                    case 32:
                        PhoneStateListener.this.onCallStateChanged(msg.arg1, (String) msg.obj);
                        return;
                    case 64:
                        PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1, msg.arg2);
                        PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1);
                        return;
                    case 128:
                        PhoneStateListener.this.onDataActivity(msg.arg1);
                        return;
                    case 256:
                        PhoneStateListener.this.onSignalStrengthsChanged((SignalStrength) msg.obj);
                        return;
                    case 512:
                        PhoneStateListener.this.onOtaspChanged(msg.arg1);
                        return;
                    case 1024:
                        PhoneStateListener.this.onCellInfoChanged((List) msg.obj);
                        return;
                    case 2048:
                        PhoneStateListener.this.onPreciseCallStateChanged((PreciseCallState) msg.obj);
                        return;
                    case 4096:
                        PhoneStateListener.this.onPreciseDataConnectionStateChanged((PreciseDataConnectionState) msg.obj);
                        return;
                    case 8192:
                        PhoneStateListener.this.onDataConnectionRealTimeInfoChanged((DataConnectionRealTimeInfo) msg.obj);
                        return;
                    case 16384:
                        PhoneStateListener.this.onVoLteServiceStateChanged((VoLteServiceState) msg.obj);
                        return;
                    case 32768:
                        PhoneStateListener.this.onOemHookRawEvent((byte[]) msg.obj);
                        return;
                    case 65536:
                        PhoneStateListener.this.onCarrierNetworkChange(((Boolean) msg.obj).booleanValue());
                        return;
                    case 131072:
                        PhoneStateListener.this.onFdnUpdated();
                        return;
                    case 262144:
                        PhoneStateListener.this.onDunDataActivity(msg.arg1);
                        return;
                    case 524288:
                        PhoneStateListener.this.onDunDataConnectionStateChanged(msg.arg1, msg.arg2);
                        return;
                    case 1048576:
                        PhoneStateListener.this.onVoiceRadioBearerHoStateChanged(msg.arg1);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void onServiceStateChanged(ServiceState serviceState) {
    }

    @Deprecated
    public void onSignalStrengthChanged(int asu) {
    }

    public void onMessageWaitingIndicatorChanged(boolean mwi) {
    }

    public void onCallForwardingIndicatorChanged(boolean cfi) {
    }

    public void onCellLocationChanged(CellLocation location) {
    }

    public void onCallStateChanged(int state, String incomingNumber) {
    }

    public void onDataConnectionStateChanged(int state) {
    }

    public void onDataConnectionStateChanged(int state, int networkType) {
    }

    public void onDataActivity(int direction) {
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
    }

    public void onOtaspChanged(int otaspMode) {
    }

    public void onCellInfoChanged(List<CellInfo> list) {
    }

    public void onPreciseCallStateChanged(PreciseCallState callState) {
    }

    public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) {
    }

    public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) {
    }

    public void onVoLteServiceStateChanged(VoLteServiceState stateInfo) {
    }

    public void onOemHookRawEvent(byte[] rawData) {
    }

    public void onCarrierNetworkChange(boolean active) {
    }

    public void onFdnUpdated() {
    }

    public void onDunDataActivity(int direction) {
    }

    public void onDunDataConnectionStateChanged(int state, int networkType) {
    }

    public void onVoiceRadioBearerHoStateChanged(int state) {
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, s);
    }
}
