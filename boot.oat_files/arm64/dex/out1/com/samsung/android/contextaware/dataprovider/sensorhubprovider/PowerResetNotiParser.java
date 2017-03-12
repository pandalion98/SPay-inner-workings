package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.UserHandle;
import com.samsung.android.contextaware.ContextAwareManager;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaBootStatus;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class PowerResetNotiParser implements ISensorHubParser, ISensorHubResetObservable {
    private static final int SLPI_CMD_RESET_STATE = 0;
    private static final int SLPI_CMD_RESTORE_STATE = 1;
    private static final int SLPI_CMD_UNKNOWN_STATE = -1;
    private static final String SLPI_RESET_STATUS = "restrict";
    private static final String SLPI_RESET_STATUS_PATH = "/sys/class/sensors/ssc_core/operation_mode";
    private static final String SLPI_RESTORE_STATUS = "normal";
    private static final String SLPI_UNKNOWN_STATUS = "unknown";
    Context mContext = null;
    private String mLastStatus = "unknown";
    private final CopyOnWriteArrayList<ISensorHubResetObserver> mListeners = new CopyOnWriteArrayList();
    private SLPIResetObserver mResetObserver;

    class SLPIResetObserver extends FileObserver {
        private Context mContext;

        SLPIResetObserver(String path, Context context) {
            super(path);
            this.mContext = context;
        }

        public void onEvent(int event, String path) {
            if (PowerResetNotiParser.this.isSLPISupported() && (event & 2) == 2) {
                String data = PowerResetNotiParser.this.getFileData(PowerResetNotiParser.SLPI_RESET_STATUS_PATH);
                CaLogger.info("CTS status : " + data);
                if (PowerResetNotiParser.this.mLastStatus.compareTo(data) != 0) {
                    int result;
                    if ("normal".compareTo(data) == 0) {
                        CaLogger.info("SLPI status : SLPI_RESTORE_STATUS");
                        result = SensorHubCommManager.getInstance().sendCmdToSensorHub(CaConvertUtil.intToByteArr(1, 1), ISensorHubCmdProtocol.INST_LIB_PUTVALUE, ISensorHubCmdProtocol.TYPE_SLPI_RESET_STATE);
                        if (result != SensorHubErrors.SUCCESS.getCode()) {
                            CaLogger.error(SensorHubErrors.getMessage(result));
                        }
                        PowerResetNotiParser.this.notifySensorHubResetObserver(-43);
                    } else if (PowerResetNotiParser.SLPI_RESET_STATUS.compareTo(data) == 0) {
                        CaLogger.info("SLPI status : SLPI_RESET_STATUS");
                        result = SensorHubCommManager.getInstance().sendCmdToSensorHub(CaConvertUtil.intToByteArr(0, 1), ISensorHubCmdProtocol.INST_LIB_PUTVALUE, ISensorHubCmdProtocol.TYPE_SLPI_RESET_STATE);
                        if (result != SensorHubErrors.SUCCESS.getCode()) {
                            CaLogger.error(SensorHubErrors.getMessage(result));
                        }
                    } else {
                        CaLogger.info("Status of SLPI is invalid");
                        return;
                    }
                    PowerResetNotiParser.this.mLastStatus = data;
                    return;
                }
                CaLogger.info("Status of SLPI is same so skip event!!");
            }
        }
    }

    public PowerResetNotiParser(Context context) {
        this.mContext = context;
        if (isSLPISupported()) {
            this.mResetObserver = new SLPIResetObserver(SLPI_RESET_STATUS_PATH, context);
            if (this.mResetObserver != null) {
                this.mResetObserver.startWatching();
                CaLogger.info("SLPIResetObserver : start");
                return;
            }
            CaLogger.info("SLPIResetObserver : observer is null");
        }
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        Bundle dispContextData = new Bundle();
        int tmpNext2 = tmpNext + 1;
        int sensorHubStatus = packet[tmpNext];
        dispContextData.putString("Noti", Integer.toString(sensorHubStatus));
        if (sensorHubStatus == -43) {
            CaLogger.debug("================= Noti (Power) =================");
            CaLogger.info("Noti Type : SensorHub Reset");
            notifySensorHubResetObserver(sensorHubStatus);
            if (CaBootStatus.getInstance().isBootComplete() && this.mContext != null) {
                this.mContext.sendBroadcastAsUser(new Intent(ContextAwareManager.SENSORHUB_RESET_ACTION), UserHandle.ALL);
            }
        }
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    public final void registerSensorHubResetObserver(ISensorHubResetObserver observer) {
        if (!this.mListeners.contains(observer)) {
            this.mListeners.add(observer);
        }
    }

    public final void unregisterSensorHubResetObserver(ISensorHubResetObserver observer) {
        if (this.mListeners.contains(observer)) {
            this.mListeners.remove(observer);
        }
    }

    public final void notifySensorHubResetObserver(int status) {
        Iterator<ISensorHubResetObserver> i = this.mListeners.iterator();
        while (i.hasNext()) {
            ISensorHubResetObserver observer = (ISensorHubResetObserver) i.next();
            if (observer != null) {
                observer.updateSensorHubResetStatus(status);
            }
        }
    }

    private boolean isSLPISupported() {
        if (new File(SLPI_RESET_STATUS_PATH).exists()) {
            return true;
        }
        return false;
    }

    private String getFileData(String file_path) {
        Throwable th;
        String s = "";
        StringBuffer sb = new StringBuffer("");
        FileReader file_reader = null;
        if (file_path == null) {
            CaLogger.error("File Path is null!!");
            return s;
        }
        try {
            FileReader file_reader2 = new FileReader(file_path);
            try {
                if (file_reader2.ready()) {
                    while (true) {
                        int data = file_reader2.read();
                        if (data == -1) {
                            break;
                        }
                        sb.append((char) data);
                    }
                    s = sb.toString().replace("\n", "");
                }
                try {
                    file_reader2.close();
                    file_reader = file_reader2;
                } catch (IOException e) {
                    e.printStackTrace();
                    file_reader = file_reader2;
                }
            } catch (FileNotFoundException e2) {
                file_reader = file_reader2;
                try {
                    CaLogger.error("File is not found");
                    try {
                        file_reader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return s;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        file_reader.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                file_reader = file_reader2;
                CaLogger.error("File is not found");
                try {
                    file_reader.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
                return s;
            } catch (Throwable th3) {
                th = th3;
                file_reader = file_reader2;
                file_reader.close();
                throw th;
            }
        } catch (FileNotFoundException e5) {
            CaLogger.error("File is not found");
            file_reader.close();
            return s;
        } catch (IOException e6) {
            CaLogger.error("File is not found");
            file_reader.close();
            return s;
        }
        return s;
    }
}
