package com.samsung.android.contextaware.dataprovider.sensorhubprovider.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IntLibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCoverManager;
import com.samsung.android.contextaware.utilbundle.ICoverStatusChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.cover.CoverState;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneStateMonitorRunner extends IntLibTypeProvider implements ICoverStatusChangeObserver {
    private static final int COVER_CLOSE = 0;
    private static final int COVER_OPEN = 1;
    private static final int COVER_TYPE_FLIP = 1;
    private static final int COVER_TYPE_NONE = 0;
    private static final int COVER_TYPE_VIEW = 2;
    private static final String GET_PHONE_STATE_ACTION = "com.samsung.android.contextaware.GET_PHONE_STATE";
    private static final String LOG_FILE = "/data/log/CAE/phone_state.txt";
    private static final String LOG_FILE_DIR = "/data/log/CAE";
    private static final int MSG_COVER_STATE = 65261;
    private static final int MSG_TIMER_EXPIRED = 65263;
    private static final int TURN_OVER_LIGHTING_DISABLED = 0;
    private static final int TURN_OVER_LIGHTING_ENABLED = 1;
    private static final long WAIT_RESPONSE_TIME = 200;
    private Context mContext = null;
    private Handler mHandler;
    private final Looper mLooper;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public final void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaLogger.error(" context is null");
            } else if (intent == null) {
                CaLogger.error(" intent is null");
            } else if (intent.getAction().equals(PhoneStateMonitorRunner.GET_PHONE_STATE_ACTION)) {
                CaLogger.info(intent.toString());
                PhoneStateMonitorRunner.this.getState();
            }
        }
    };

    private enum ContextName {
        Movement(0),
        LcdDirect(1),
        Embower(2),
        FinalLcdOff(3),
        LcdOffInference(4),
        LcdOffRecommend(5),
        TimeStamp(6);
        
        private int val;

        private ContextName(int v) {
            this.val = v;
        }
    }

    public PhoneStateMonitorRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        this.mContext = context;
        this.mLooper = looper;
        createHandler();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_PHONE_STATE_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_PHONE_STATE_MONITOR_SERVICE;
    }

    public final String[] getContextValueNames() {
        return new String[]{"movement", "lcddirect", "embower", "finalLcdOff", "lcdOffInference", "lcdOffRecommend", "timestamp"};
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        CaLogger.info("parse start:" + tmpNext);
        String[] names = getContextValueNames();
        if ((packet.length - tmpNext) - 6 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        tmpNext = ((tmpNext + 1) + 1) + 1;
        int tmpNext2 = tmpNext + 1;
        boolean lcdOff = packet[tmpNext] != (byte) 0;
        tmpNext = tmpNext2 + 1;
        int lcdDirectEvent = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        int embowerEvent = packet[tmpNext];
        super.getContextBean().putContext(names[ContextName.LcdDirect.val], lcdDirectEvent);
        super.getContextBean().putContext(names[ContextName.Embower.val], embowerEvent);
        super.getContextBean().putContext(names[ContextName.LcdOffRecommend.val], lcdOff);
        super.getContextBean().putContext(names[ContextName.TimeStamp.val], System.currentTimeMillis());
        super.notifyObserver();
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    private void recordPhoneDrop() {
        IOException e;
        Throwable th;
        File folder = new File(LOG_FILE_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        FileOutputStream fOutStream = null;
        try {
            FileOutputStream fOutStream2 = new FileOutputStream(new File(LOG_FILE), true);
            if (fOutStream2 != null) {
                try {
                    fOutStream2.write((getDate(System.currentTimeMillis()) + " - PHONE DROP DETECTED\n").getBytes());
                } catch (IOException e2) {
                    e = e2;
                    fOutStream = fOutStream2;
                    try {
                        CaLogger.error(e.toString());
                        if (fOutStream != null) {
                            try {
                                fOutStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fOutStream != null) {
                            try {
                                fOutStream.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fOutStream = fOutStream2;
                    if (fOutStream != null) {
                        fOutStream.close();
                    }
                    throw th;
                }
            }
            if (fOutStream2 != null) {
                try {
                    fOutStream2.close();
                    fOutStream = fOutStream2;
                    return;
                } catch (IOException e322) {
                    e322.printStackTrace();
                    fOutStream = fOutStream2;
                    return;
                }
            }
        } catch (IOException e4) {
            e322 = e4;
            CaLogger.error(e322.toString());
            if (fOutStream != null) {
                fOutStream.close();
            }
        }
    }

    private String getDate(long time) {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(time));
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        int i = 1;
        byte[] packet = new byte[5];
        packet[0] = (byte) 0;
        packet[1] = (byte) 1;
        packet[2] = (byte) isTurnOverLighting();
        packet[3] = (byte) CaCoverManager.getInstance(this.mLooper).getCoverType();
        if (!CaCoverManager.getInstance(this.mLooper).getCoverState()) {
            i = 0;
        }
        packet[4] = (byte) i;
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 2};
    }

    public final <E> boolean setPropertyValue(int property, E e) {
        if (property == 1) {
            getState();
        }
        return true;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        IntentFilter phoneState = new IntentFilter(GET_PHONE_STATE_ACTION);
        CaCoverManager.getInstance(this.mLooper).registerObserver(this);
        super.enable();
    }

    private void getState() {
        int i = 1;
        if (!super.isDisable()) {
            byte[] packet = new byte[4];
            packet[0] = (byte) 0;
            packet[1] = (byte) 3;
            if (!CaCoverManager.getInstance(this.mLooper).getCoverState()) {
                i = 0;
            }
            packet[2] = (byte) i;
            packet[3] = (byte) CaCoverManager.getInstance(this.mLooper).getCoverType();
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), packet);
            this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
            this.mHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, WAIT_RESPONSE_TIME);
        }
    }

    public final void disable() {
        CaLogger.trace();
        CaCoverManager.getInstance(this.mLooper).unregisterObserver(this);
        super.disable();
    }

    protected final void notifyInitContext() {
        if (ListenerListManager.getInstance().getUsedTotalCount(getContextType()) == 1) {
            super.notifyInitContext();
        }
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    public void onCoverStatusChanged(CoverState state) {
        int status = state.getSwitchState() ? 1 : 0;
        CaLogger.info("Cover status:" + status);
        if (super.isDisable()) {
            CaLogger.warning("runner disabled");
        } else {
            sendPropertyValueToSensorHub(ISensorHubCmdProtocol.TYPE_PHONE_STATE_MONITOR_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(status, 1));
        }
    }

    private int isTurnOverLighting() {
        if (false) {
            return 1;
        }
        return 0;
    }

    private void createHandler() {
        this.mHandler = new Handler(this.mLooper) {
            public void handleMessage(Message msg) {
                if (msg.what == PhoneStateMonitorRunner.MSG_TIMER_EXPIRED) {
                    String[] names = PhoneStateMonitorRunner.this.getContextValueNames();
                    if (super.isDisable()) {
                        CaLogger.debug("runner disabled");
                        return;
                    }
                    PhoneStateMonitorRunner.this.getContextBean().putContext(names[ContextName.Movement.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(names[ContextName.LcdDirect.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(names[ContextName.Embower.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(names[ContextName.FinalLcdOff.val], false);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(names[ContextName.LcdOffInference.val], false);
                    PhoneStateMonitorRunner.this.notifyObserver();
                }
            }
        };
    }
}
