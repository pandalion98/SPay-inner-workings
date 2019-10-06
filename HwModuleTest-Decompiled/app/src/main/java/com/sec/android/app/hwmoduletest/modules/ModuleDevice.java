package com.sec.android.app.hwmoduletest.modules;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.util.Locale;

public class ModuleDevice extends ModuleObject {
    public static final String ACTION_EXTERNAL_STORAGE_MOUNTED = "com.sec.factory.action.ACTION_EXTERNAL_STORAGE_MOUNTED";
    public static final String ACTION_EXTERNAL_STORAGE_UNMOUNTED = "com.sec.factory.action.ACTION_EXTERNAL_STORAGE_UNMOUNTED";
    public static final String ACTION_OTG_RESPONSE = "com.sec.factory.intent.ACTION_OTG_RESPONSE";
    public static final String ACTION_USB_STORAGE_MOUNTED = "com.sec.factory.action.ACTION_USB_STORAGE_MOUNTED";
    public static final String ACTION_USB_STORAGE_UNMOUNTED = "com.sec.factory.action.ACTION_USB_STORAGE_UNMOUNTED";
    public static final int EXTERNAL_MEMORY = 1;
    public static final int FOLDER_STATE_CLOSE = 1;
    public static final int FOLDER_STATE_ERROR = 2;
    public static final int FOLDER_STATE_OPEN = 0;
    public static final int INTERNAL_MEMORY = 0;
    public static final String MOOD_LED_RGB_COLOR_1 = Spec.getString(Spec.MOOD_LED_RGB_COLOR_1);
    public static final String MOOD_LED_RGB_COLOR_2 = Spec.getString(Spec.MOOD_LED_RGB_COLOR_2);
    public static final String MOOD_LED_RGB_COLOR_3 = Spec.getString(Spec.MOOD_LED_RGB_COLOR_3);
    public static final int MOOD_LED_STATE_OFF = 0;
    public static final int MOOD_LED_STATE_RGB_BLUE = 8;
    public static final int MOOD_LED_STATE_RGB_GREEN = 7;
    public static final int MOOD_LED_STATE_RGB_RED = 6;
    public static final int MOOD_LED_STATE_UX5_COLOR_1 = 1;
    public static final int MOOD_LED_STATE_UX5_COLOR_2 = 2;
    public static final int MOOD_LED_STATE_UX5_COLOR_3 = 3;
    public static final int MOOD_LED_STATE_UX5_COLOR_4 = 4;
    public static final int MOOD_LED_STATE_UX5_COLOR_5 = 5;
    public static final String MOOD_LED_UX5_COLOR_1 = Spec.getString(Spec.MOOD_LED_UX5_COLOR_1);
    public static final String MOOD_LED_UX5_COLOR_2 = Spec.getString(Spec.MOOD_LED_UX5_COLOR_2);
    public static final String MOOD_LED_UX5_COLOR_3 = Spec.getString(Spec.MOOD_LED_UX5_COLOR_3);
    public static final String MOOD_LED_UX5_COLOR_4 = Spec.getString(Spec.MOOD_LED_UX5_COLOR_4);
    public static final String MOOD_LED_UX5_COLOR_5 = Spec.getString(Spec.MOOD_LED_UX5_COLOR_5);
    private static final byte MSG_OTG_TEST_FINISH = 0;
    private static final byte[] OTG_MUIC_OFF = {49, 0};
    private static final byte[] OTG_MUIC_ON = {48, 0};
    private static final byte[] OTG_TEST_OFF = {79, NVAccessor.NV_VALUE_FAIL, NVAccessor.NV_VALUE_FAIL, 0};
    private static final byte[] OTG_TEST_ON = {79, NVAccessor.NV_VALUE_NOTEST, 0};
    private static final int OTG_TEST_WAIT_DELAY = 2000;
    public static final String PATH_EXTERNAL_STORAGE = "extSdCard";
    public static final String PATH_INTERNAL_STORAGE = "sdcard";
    public static final String PATH_OTG_STORAGE = "UsbStotageX";
    private static final String[] STORAGE_PATH = {"/mnt/sdcard", "/mnt/extSdCard", "/system"};
    public static final int SYSTEM_STORAGE = 2;
    public static final String TSP_CMD_CHIP_NAME_READ = "get_chip_name";
    public static final String TSP_CMD_CHIP_VENDOR_READ = "get_chip_vendor";
    public static final String TSP_CMD_DISABLE_DEAD_ZONE = "dead_zone_enable,0";
    public static final String TSP_CMD_ENABLE_DEAD_ZONE = "dead_zone_enable,1";
    public static final String TSP_CMD_MODULE_OFF_MASTER = "module_off_master";
    public static final String TSP_CMD_MODULE_OFF_SLAVE = "module_off_slave";
    public static final String TSP_CMD_MODULE_ON_MASTER = "module_on_master";
    public static final String TSP_CMD_MODULE_ON_SLAVE = "module_on_slave";
    public static final String TSP_CMD_MODULE_TSP_CONNECTION = "get_tsp_connection";
    public static final String TSP_CMD_READ1_CM_ABS = "get_cm_abs";
    public static final String TSP_CMD_READ1_GET_REF = "get_reference";
    public static final String TSP_CMD_READ1_RAWCAP = "get_rawcap";
    public static final String TSP_CMD_READ1_RUN_CM_ABS = "run_cm_abs_read";
    public static final String TSP_CMD_READ1_RUN_DELTA = "run_delta_read";
    public static final String TSP_CMD_READ1_RUN_RAWCAP = "run_rawcap_read";
    public static final String TSP_CMD_READ1_RUN_REF = "run_reference_read";
    public static final String TSP_CMD_READ2_CM_DELFA = "get_cm_delta";
    public static final String TSP_CMD_READ2_GET_DELTA = "get_delta";
    public static final String TSP_CMD_READ2_GET_RAW = "get_raw";
    public static final String TSP_CMD_READ2_RUN_CM_DELTA = "run_cm_delta_read";
    public static final String TSP_CMD_READ2_RUN_RxToRx = "run_rx_to_rx_read";
    public static final String TSP_CMD_READ2_RxToRx = "get_rx_to_rx";
    public static final String TSP_CMD_READ3_GET_DELTA = "get_delta";
    public static final String TSP_CMD_READ3_INTENSITY = "get_intensity";
    public static final String TSP_CMD_READ3_RUN_DELTA = "run_delta_read";
    public static final String TSP_CMD_READ3_RUN_INTENSITY = "run_intensity_read";
    public static final String TSP_CMD_READ3_RUN_TxToTx = "run_tx_to_tx_read";
    public static final String TSP_CMD_READ3_TxToTx = "get_tx_to_tx";
    public static final String TSP_CMD_READ4_RUN_TxToGND = "run_tx_to_gnd_read";
    public static final String TSP_CMD_READ4_TxToGND = "get_tx_to_gnd";
    public static final String TSP_CMD_READ5_RUN_RAW = "run_raw_read";
    public static final String TSP_CMD_READ_THRESHOLD = "get_threshold";
    public static final String TSP_CMD_SELFTEST = "run_selftest";
    public static final String TSP_CMD_X_COUNT = "get_x_num";
    public static final String TSP_CMD_Y_COUNT = "get_y_num";
    public static final String TSP_FIRMWARE_VERSION_PANEL = "get_fw_ver_ic";
    public static final String TSP_FIRMWARE_VERSION_PHONE = "get_fw_ver_bin";
    public static final String TSP_RETURN_VALUE_NA = "NA";
    public static final String TSP_RETURN_VALUE_NG = "NG";
    public static final String TSP_RETURN_VALUE_OK = "OK";
    public static final int TYPE_EXTERNAL_STORAGE = 1;
    public static final int TYPE_INTERNAL_STORAGE = 0;
    public static final int TYPE_USB_STORAGE = 2;
    public static final int UNIT_BYTE = 0;
    public static final int UNIT_GB = 1073741824;
    public static final int UNIT_KB = 1024;
    public static final int UNIT_MB = 1048576;
    public static final int USER_STORAGE_1 = 0;
    public static final int USER_STORAGE_2 = 1;
    private static StorageVolume mExternalSDStorageVolume = null;
    private static ModuleDevice mInstance = null;
    private static StorageVolume mInternalSDStorageVolume = null;
    private static StorageVolume mUSBStorageVolume = null;
    public static final String mood_led_node = "MOOD_LED_NODE";
    private boolean mIsMountedExternalStorage = false;
    private boolean mIsMountedUsbStorage = false;
    private StorageManager mStorageManager = null;
    private Vibrator mVibrator;

    public enum HallIC {
        FOLDER_STATE_OPEN,
        FOLDER_STATE_CLOSE,
        FOLDER_STATE_ERROR;
        
        public static final String CLASS_NAME = "ModuleDevice.HallIC";

        public static HallIC getHallICState() {
            LtUtil.log_i(CLASS_NAME, "getHallICState", null);
            try {
                String state1 = Kernel.read(Kernel.PATH_HALLIC_STATE);
                StringBuilder sb = new StringBuilder();
                sb.append("state - hall IC 1 ( ");
                sb.append(state1);
                sb.append(" )");
                LtUtil.log_i(CLASS_NAME, "getHallICState", sb.toString());
                if (!"OPEN".equals(state1)) {
                    if (!EgisFingerprint.MAJOR_VERSION.equals(state1)) {
                        if (!"CLOSE".equals(state1)) {
                            if (!"0".equals(state1)) {
                                return FOLDER_STATE_ERROR;
                            }
                        }
                        return FOLDER_STATE_CLOSE;
                    }
                }
                return FOLDER_STATE_OPEN;
            } catch (Exception e) {
                LtUtil.log_i(CLASS_NAME, "getHallICState", "Exception");
            }
        }

        public static HallIC getHallICState2() {
            LtUtil.log_i(CLASS_NAME, "getHallICState2", null);
            try {
                String state2 = Kernel.read(Kernel.PATH_HALLIC_STATE2);
                StringBuilder sb = new StringBuilder();
                sb.append("state - hall IC 2 ( ");
                sb.append(state2);
                sb.append(" )");
                LtUtil.log_i(CLASS_NAME, "getHallICState2", sb.toString());
                if (!"OPEN".equals(state2)) {
                    if (!EgisFingerprint.MAJOR_VERSION.equals(state2)) {
                        if (!"CLOSE".equals(state2)) {
                            if (!"0".equals(state2)) {
                                return FOLDER_STATE_ERROR;
                            }
                        }
                        return FOLDER_STATE_CLOSE;
                    }
                }
                return FOLDER_STATE_OPEN;
            } catch (Exception e) {
                LtUtil.log_i(CLASS_NAME, "getHallICState2", "Exception");
            }
        }
    }

    private ModuleDevice(Context context) {
        super(context, "ModuleDevice");
        LtUtil.log_i(this.CLASS_NAME, "ModuleDevice", "Create ModuleDevice");
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mStorageManager = (StorageManager) getSystemService("storage");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        LtUtil.log_i(this.CLASS_NAME, "ModuleDevice", "registerReceiver OK");
    }

    public static ModuleDevice instance(Context context) {
        if (mInstance == null) {
            mInstance = new ModuleDevice(context);
        }
        return mInstance;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        LtUtil.log_i(this.CLASS_NAME, "ModuleDevice", "finalize ModuleDevice");
        super.finalize();
    }

    public long getSize(int storage, int unit) {
        StatFs stat = new StatFs(STORAGE_PATH[storage]);
        return (((long) stat.getBlockCount()) * ((long) stat.getBlockSize())) / ((long) unit);
    }

    public long getAvailableSize(int storage, int unit) {
        StatFs stat = new StatFs(STORAGE_PATH[storage]);
        return (((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize())) / ((long) unit);
    }

    public long getUseSize(int storage, int unit) {
        return getSize(storage, unit) - getAvailableSize(storage, unit);
    }

    public boolean isInnerMemoryExist() {
        if (new StatFs(Environment.getExternalStorageDirectory().toString()).getBlockCount() > 0) {
            return true;
        }
        return false;
    }

    public long getInnerMemorySize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
        return (((long) stat.getBlockCount()) * ((long) stat.getBlockSize())) / 1048576;
    }

    public String getStateExternalMemory() {
        return Kernel.read(Kernel.EXTERNAL_MEMORY_INSERTED);
    }

    public boolean isExternalMemoryExist() {
        String state = Environment.getExternalStorageState();
        return "mounted".equalsIgnoreCase(state) || "unmounted".equalsIgnoreCase(state) || "mounted_ro".equalsIgnoreCase(state);
    }

    public String mainMEMOSize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().toString());
        long totalMass = (((long) stat.getBlockCount()) * ((long) stat.getBlockSize())) / 1024;
        long freeMass = (((long) stat.getFreeBlocks()) * ((long) stat.getBlockSize())) / 1024;
        long usedMass = totalMass - freeMass;
        StringBuilder sb = new StringBuilder();
        sb.append("[Main]TotalMass : ");
        sb.append(totalMass);
        sb.append("KB, FreeMass : ");
        sb.append(freeMass);
        sb.append("KB, UsedMass : ");
        sb.append(usedMass);
        sb.append("KB");
        LtUtil.log_i(this.CLASS_NAME, "mainMEMOSize", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Long.toString(totalMass));
        sb2.append(",");
        sb2.append(Long.toString(usedMass));
        String resData = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("resData=");
        sb3.append(resData);
        LtUtil.log_i(this.CLASS_NAME, "mainMEMOSize", sb3.toString());
        return resData;
    }

    public String int_extMEMOSize(int type) {
        StorageVolume[] volumeList = this.mStorageManager.getVolumeList();
        StatFs stat = new StatFs(setStorageVolume(type));
        long totalMass = (((long) stat.getBlockCount()) * ((long) stat.getBlockSize())) / 1024;
        long freeMass = (((long) stat.getFreeBlocks()) * ((long) stat.getBlockSize())) / 1024;
        long usedMass = totalMass - freeMass;
        StringBuilder sb = new StringBuilder();
        sb.append("[Main]TotalMass : ");
        sb.append(totalMass);
        sb.append("Bytes, FreeMass : ");
        sb.append(freeMass);
        sb.append("Bytes, UsedMass : ");
        sb.append(usedMass);
        sb.append("Bytes");
        LtUtil.log_i(this.CLASS_NAME, "externalMEMOSize", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Long.toString(totalMass));
        sb2.append(",");
        sb2.append(Long.toString(usedMass));
        String resData = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("resData=");
        sb3.append(resData);
        LtUtil.log_i(this.CLASS_NAME, "externalMEMOSize", sb3.toString());
        return resData;
    }

    public String int_extMEMOSize(int type, boolean isByte) {
        StorageVolume[] volumeList = this.mStorageManager.getVolumeList();
        StatFs stat = new StatFs(setStorageVolume(type));
        long totalMass = ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
        long freeMass = ((long) stat.getFreeBlocks()) * ((long) stat.getBlockSize());
        long usedMass = totalMass - freeMass;
        StringBuilder sb = new StringBuilder();
        sb.append("[Main]TotalMass : ");
        sb.append(totalMass);
        sb.append("Bytes, FreeMass : ");
        sb.append(freeMass);
        sb.append("Bytes, UsedMass : ");
        sb.append(usedMass);
        sb.append("Bytes");
        LtUtil.log_i(this.CLASS_NAME, "externalMEMOSize", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Long.toString(totalMass));
        sb2.append(",");
        sb2.append(Long.toString(usedMass));
        String resData = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("resData=");
        sb3.append(resData);
        LtUtil.log_i(this.CLASS_NAME, "externalMEMOSize", sb3.toString());
        return resData;
    }

    public String setStorageVolume(int type) {
        StorageVolume[] storageVolumes = this.mStorageManager.getVolumeList();
        int length = storageVolumes.length;
        for (int i = 0; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("getSubSystem() : ");
            sb.append(storageVolumes[i].getSubSystem());
            LtUtil.log_i(this.CLASS_NAME, "setStorageVolume", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getPath() : ");
            sb2.append(storageVolumes[i].getPath());
            LtUtil.log_i(this.CLASS_NAME, "setStorageVolume", sb2.toString());
            if (!storageVolumes[i].getPath().equals("/storage/Private")) {
                if (storageVolumes[i].isRemovable() && storageVolumes[i].getSubSystem().equals("sd")) {
                    mExternalSDStorageVolume = storageVolumes[i];
                } else if (storageVolumes[i].getSubSystem().equals("fuse")) {
                    mInternalSDStorageVolume = storageVolumes[i];
                } else if (storageVolumes[i].isRemovable() && storageVolumes[i].getSubSystem().equals("usb") && this.mStorageManager.getVolumeState(storageVolumes[i].getPath()).equals("mounted")) {
                    mUSBStorageVolume = storageVolumes[i];
                }
            }
        }
        if (type == 0) {
            if (mInternalSDStorageVolume != null && mInternalSDStorageVolume.getPath() != null) {
                return mInternalSDStorageVolume.getPath();
            }
            LtUtil.log_i(this.CLASS_NAME, "setStorageVolume", "mInternalSDStorageVolume is null");
            return null;
        } else if (type == 1) {
            if (mExternalSDStorageVolume != null && mExternalSDStorageVolume.getPath() != null) {
                return mExternalSDStorageVolume.getPath();
            }
            LtUtil.log_i(this.CLASS_NAME, "setStorageVolume", "mExternalSDStorageVolume is null");
            return null;
        } else if (type != 2) {
            return null;
        } else {
            if (mUSBStorageVolume != null && mUSBStorageVolume.getPath() != null) {
                return mExternalSDStorageVolume.getPath();
            }
            LtUtil.log_i(this.CLASS_NAME, "setStorageVolume", "mUSBStorageVolume is null");
            return null;
        }
    }

    public boolean isMountedStorage(int type) {
        String path = setStorageVolume(type);
        StringBuilder sb = new StringBuilder();
        sb.append("path : ");
        sb.append(path);
        LtUtil.log_i(this.CLASS_NAME, "isMountedStorage", sb.toString());
        if (path != null) {
            String state = this.mStorageManager.getVolumeState(path);
            LtUtil.log_i(this.CLASS_NAME, "isMountedStorage", "Environment.MEDIA_MOUNTED : mounted");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("state : ");
            sb2.append(state);
            LtUtil.log_i(this.CLASS_NAME, "isMountedStorage", sb2.toString());
            return state.equals("mounted");
        }
        LtUtil.log_i(this.CLASS_NAME, "isMountedStorage", "another error");
        return false;
    }

    public String getStoragePath(int type) {
        StorageVolume[] volumeList = this.mStorageManager.getVolumeList();
        if (isMountedStorage(type)) {
            String path = setStorageVolume(type);
            StringBuilder sb = new StringBuilder();
            sb.append("Storage path : ");
            sb.append(path);
            LtUtil.log_i(this.CLASS_NAME, "getStorageParh", sb.toString());
            return path;
        }
        LtUtil.log_i(this.CLASS_NAME, "getStorageParh", "Storage path : null");
        return null;
    }

    public String startTSPTest(String cmd) {
        StringBuilder sb = new StringBuilder();
        sb.append("cmd name => ");
        sb.append(cmd);
        LtUtil.log_e(this.CLASS_NAME, "startTSPTest", sb.toString());
        return startTSPTest(cmd.getBytes(), cmd.length());
    }

    private String startTSPTest(byte[] cmd, int commandLength) {
        String str = "";
        String str2 = "";
        if (Kernel.write(Kernel.TSP_COMMAND_CMD, cmd)) {
            String status = Kernel.read(Kernel.TSP_COMMAND_STATUS);
            String result = Kernel.read(Kernel.TSP_COMMAND_RESULT);
            if (status == null) {
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", "Fail - Status Read => status == null");
                return "NG";
            } else if (status.equals("OK")) {
                if (result == null) {
                    LtUtil.log_e(this.CLASS_NAME, "startTSPTest", "Fail - Result Read =>1 result == null");
                    return "NG";
                }
                StringBuilder sb = new StringBuilder();
                sb.append("result : ");
                sb.append(result);
                LtUtil.log_i(this.CLASS_NAME, "startTSPTest", sb.toString());
                return result.substring(commandLength + 1, result.length());
            } else if (status.equals("NOT_APPLICABLE")) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("N/A- Status : ");
                sb2.append(status);
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("N/A - result : ");
                sb3.append(result);
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", sb3.toString());
                if (result != null) {
                    return result.substring(commandLength + 1, result.length());
                }
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", "Fail - Result Read =>2 result == null");
                return "NG";
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Fail - Status : ");
                sb4.append(status);
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Fail - result : ");
                sb5.append(result);
                LtUtil.log_e(this.CLASS_NAME, "startTSPTest", sb5.toString());
                return "NG";
            }
        } else {
            LtUtil.log_e(this.CLASS_NAME, "startTSPTest", "Fail - Command Write");
            return "NG";
        }
    }

    public String startTSPReadTest(String command, int x) {
        String temp_X = startTSPTest("get_x_num");
        if (temp_X.equals("NG")) {
            LtUtil.log_e(this.CLASS_NAME, "startTSPReadTest", "error - TSP_CMD_X_COUNT");
            return "NG";
        }
        int X_AXIS_MAX = Integer.parseInt(temp_X, 10);
        String temp_Y = startTSPTest("get_y_num");
        if (temp_Y.equals("NG")) {
            LtUtil.log_e(this.CLASS_NAME, "startTSPReadTest", "error - TSP_CMD_Y_COUNT");
            return "NG";
        }
        int Y_AXIS_MAX = Integer.parseInt(temp_Y, 10);
        String result = "";
        if (startTSPTest(TSP_CMD_CHIP_VENDOR_READ).toUpperCase(Locale.ENGLISH).equals("SYNAPTICS")) {
            int node_y = 0;
            while (node_y < Y_AXIS_MAX) {
                String result2 = result;
                for (int node_x = 0; node_x < X_AXIS_MAX; node_x++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(command);
                    sb.append(",");
                    sb.append(node_x);
                    sb.append(",");
                    sb.append(node_y);
                    String strCMD = sb.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(result2);
                    sb2.append(startTSPTest(strCMD.getBytes(), strCMD.length()));
                    sb2.append(",");
                    result2 = sb2.toString();
                }
                node_y++;
                result = result2;
            }
            return result.substring(0, result.length() - 1);
        } else if (x >= X_AXIS_MAX) {
            return "NG";
        } else {
            int node_y2 = 0;
            while (node_y2 < Y_AXIS_MAX) {
                String result3 = result;
                for (int node_x2 = 0; node_x2 < X_AXIS_MAX; node_x2++) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(command);
                    sb3.append(",");
                    sb3.append(node_x2);
                    sb3.append(",");
                    sb3.append(node_y2);
                    String strCMD2 = sb3.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(result3);
                    sb4.append(startTSPTest(strCMD2.getBytes(), strCMD2.length()));
                    sb4.append(",");
                    result3 = sb4.toString();
                }
                node_y2++;
                result = result3;
            }
            return result.substring(0, result.length() - 1);
        }
    }

    public void startSensrohubTest() {
        Kernel.write(Kernel.SENSORHUB_MCU, EgisFingerprint.MAJOR_VERSION);
    }

    public String readSensrohubTest() {
        String str = "";
        String result = Kernel.read(Kernel.SENSORHUB_MCU);
        StringBuilder sb = new StringBuilder();
        sb.append("result : ");
        sb.append(result);
        LtUtil.log_i(this.CLASS_NAME, "readSensrohubTest", sb.toString());
        return result;
    }

    private void writeMoodLED(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("A ");
        sb.append(value);
        sb.append(" ");
        sb.append("1000 ");
        sb.append("1000 ");
        sb.append("0");
        Kernel.write("MOOD_LED_NODE", sb.toString());
    }

    public void setMoodLEDlamp(int state) {
        StringBuilder sb = new StringBuilder();
        sb.append("state=");
        sb.append(state);
        LtUtil.log_i(this.CLASS_NAME, "setMoodLEDlamp", sb.toString());
        switch (state) {
            case 0:
                writeMoodLED("0");
                return;
            case 1:
                writeMoodLED(MOOD_LED_UX5_COLOR_1);
                return;
            case 2:
                writeMoodLED(MOOD_LED_UX5_COLOR_2);
                return;
            case 3:
                writeMoodLED(MOOD_LED_UX5_COLOR_3);
                return;
            case 4:
                writeMoodLED(MOOD_LED_UX5_COLOR_4);
                return;
            case 5:
                writeMoodLED(MOOD_LED_UX5_COLOR_5);
                return;
            case 6:
                writeMoodLED(MOOD_LED_RGB_COLOR_1);
                return;
            case 7:
                writeMoodLED(MOOD_LED_RGB_COLOR_2);
                return;
            case 8:
                writeMoodLED(MOOD_LED_RGB_COLOR_3);
                return;
            default:
                return;
        }
    }

    public void hoveringOnOFF(String command) {
        StringBuilder sb = new StringBuilder();
        sb.append("hover_enable");
        sb.append(",");
        sb.append(command);
        String mainCmd = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mainCmd=");
        sb2.append(mainCmd);
        LtUtil.log_i(this.CLASS_NAME, "onOffHovering", sb2.toString());
        Kernel.write(Kernel.TSP_COMMAND_CMD, mainCmd);
    }

    public boolean isDualCamera() {
        String firmwareInfo = Kernel.read(Kernel.CAMERA_SUPPORT_TYPE2);
        if (firmwareInfo != null && firmwareInfo.contains("VALID=Y")) {
            return true;
        }
        LtUtil.log_d(this.CLASS_NAME, "isDualCamera", "maybe this model is not dual camera");
        return false;
    }

    public boolean isFrontDualCamera() {
        String firmwareInfo = Kernel.read(Kernel.CAMERA_FRONT_DUAL_INFO);
        if (firmwareInfo != null && firmwareInfo.contains("VALID=Y")) {
            return true;
        }
        LtUtil.log_d(this.CLASS_NAME, "isDualCamera", "maybe this model is not dual camera");
        return false;
    }
}
