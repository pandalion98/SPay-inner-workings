package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbnormalPressureMonitorRunner extends LibTypeProvider {
    private static final String LOG_FILE = "/data/log/CAE/phone_state.txt";
    private static final String LOG_FILE_DIR = "/data/log/CAE";

    public AbnormalPressureMonitorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ABNORMAL_PRESSURE_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ABNORMAL_PRESSURE_MONITOR;
    }

    public final String[] getContextValueNames() {
        return new String[]{"xaxis", "yaxis", "zaxis", "barometer"};
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        CaLogger.trace();
        String[] names = getContextValueNames();
        if ((packet.length - tmpNext) - 16 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        ContextBean contextBean = super.getContextBean();
        String str = names[0];
        r5 = new byte[4];
        int tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = names[1];
        r5 = new byte[4];
        tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = names[2];
        r5 = new byte[4];
        tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = names[3];
        r5 = new byte[4];
        tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        super.notifyObserver();
        return tmpNext;
    }

    private void recordAbnormalPressure() {
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
                    fOutStream2.write((getDate(System.currentTimeMillis()) + " - ABNORMAL PRESSURE DETECTED\n").getBytes());
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
