package com.sec.android.hardware;

import android.os.SystemProperties;
import com.samsung.android.feature.FloatingFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SecHardwareInterface {
    public static final int BROWSER = 4;
    public static final int CALL = 1;
    public static final int CAMERA = 6;
    private static final String DAEMON_SOCKET = "pps";
    private static String EPEN_SAVINGMODE_PATH = "/sys/class/sec/sec_epen/epen_saving_mode";
    private static String EPEN_TYPE_PATH = "/sys/class/sec/sec_epen/epen_type";
    public static final int HOTSPOT = 5;
    private static final int MAX_CMD_LEN = 100;
    public static final int MDNIE_BROSWER_MODE = 8;
    public static final int MDNIE_CAMERA_MODE = 4;
    public static final int MDNIE_DISPLAY_0 = 0;
    public static final int MDNIE_DISPLAY_1 = 1;
    public static final int MDNIE_EBOOK_MODE = 9;
    public static final int MDNIE_EMAIL_MODE = 10;
    public static final int MDNIE_GALLERY_MODE = 6;
    public static final int MDNIE_UI_MODE = 0;
    public static final int MDNIE_VIDEO_MODE = 1;
    public static final int MDNIE_VT_MODE = 7;
    public static final int MUSIC = 3;
    private static final String READING_MODE_PATH = "sys.dnle.readingmode";
    private static final String TAG = "SecHardwareInterface";
    public static final int TORCH_BRIGHTNESS_MAX = 1;
    public static final int TORCH_BRIGHTNESS_MIN = 14;
    public static final int TORCH_BRIGHTNESS_OFF = 0;
    public static final int TORCH_BRIGHTNESS_STANDARD = 1;
    public static final int VIDEO = 2;
    private static InputStream in = null;
    private static boolean isCabcDaemonConnected = false;
    private static OutputStream out = null;

    public static void setBatteryUse(int module, boolean enable) {
        switch (module) {
            case 1:
                try {
                    setBatteryADC("call", enable);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 2:
                setBatteryADC("video", enable);
                return;
            case 3:
                setBatteryADC("music", enable);
                return;
            case 4:
                setBatteryADC("browser", enable);
                return;
            case 5:
                setBatteryADC("hotspot", enable);
                return;
            case 6:
                setBatteryADC("camera", enable);
                return;
            default:
                return;
        }
        e.printStackTrace();
    }

    private static boolean sysfsWrite(String sysfs, int value) {
        IOException e;
        FileOutputStream out = null;
        try {
            FileOutputStream out2 = new FileOutputStream(new File(sysfs));
            try {
                out2.write(Integer.toString(value).getBytes());
                out2.close();
                out = out2;
                return true;
            } catch (IOException e2) {
                e = e2;
                out = out2;
                e.printStackTrace();
                try {
                    out.close();
                    return false;
                } catch (Exception err) {
                    err.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e3) {
            try {
                e3.printStackTrace();
                return false;
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                out.close();
                return false;
            }
        }
    }

    private static boolean sysfsWrite(String sysfs, String value) throws FileNotFoundException {
        IOException e;
        FileOutputStream out = null;
        try {
            FileOutputStream out2 = new FileOutputStream(new File(sysfs));
            try {
                out2.write(value.getBytes());
                out2.close();
                out = out2;
                return true;
            } catch (IOException e2) {
                e = e2;
                out = out2;
                e.printStackTrace();
                try {
                    out.close();
                    return false;
                } catch (Exception err) {
                    err.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e3) {
            try {
                e3.printStackTrace();
                return false;
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                out.close();
                return false;
            }
        }
    }

    private static boolean IsCabcDaemonConnected() {
        return isCabcDaemonConnected;
    }

    private static void CabcDaemonConnect() {
    }

    private static void CablDaemonControl(String cmd) {
        if (out == null || in == null) {
            isCabcDaemonConnected = false;
            return;
        }
        try {
            out.write(cmd.getBytes());
            out.flush();
            byte[] recv_buf = new byte[MAX_CMD_LEN];
            String str = new String(recv_buf, 0, in.read(recv_buf, 0, recv_buf.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean setAmoledVideoGamma(boolean videomode) {
        return false;
    }

    public static boolean setAmoledACL(boolean enable) {
        return sysfsWrite("/sys/class/lcd/panel/power_reduce", enable ? 1 : 0);
    }

    public static boolean setmDNIeReadingMode(int mode) {
        if (mode == 0) {
            setReadingMode(false);
        } else {
            setReadingMode(true);
        }
        return _setmDNIeUIMode(mode);
    }

    public static boolean setmDNIeUIMode(int mode) {
        if (isReadingMode()) {
            return false;
        }
        return _setmDNIeUIMode(mode);
    }

    public static boolean setmDNIeDualUIMode(int display, int mode) {
        return false;
    }

    private static boolean _setmDNIeUIMode(int mode) {
        return sysfsWrite("/sys/class/mdnie/mdnie/scenario", mode);
    }

    private static boolean _setmDNIeUIModeSub(int mode) {
        return sysfsWrite("/sys/class/mdnie_1/mdnie_1/scenario", mode);
    }

    private static boolean isReadingMode() {
        String ret = SystemProperties.get(READING_MODE_PATH);
        if (ret == null || !ret.equals("1")) {
            return false;
        }
        return true;
    }

    private static void setReadingMode(boolean enable) {
        if (enable) {
            SystemProperties.set(READING_MODE_PATH, "1");
        } else {
            SystemProperties.set(READING_MODE_PATH, "0");
        }
    }

    public static boolean setmDNIeUserMode(int mode) {
        return true & sysfsWrite("/sys/class/mdnie/mdnie/mode", mode);
    }

    public static boolean setmDNIeOutDoor(boolean enable) {
        return false;
    }

    public static boolean setmDNIeNegative(boolean enable) {
        return false;
    }

    public static boolean setTconUIMode(int mode) {
        return sysfsWrite("/sys/class/tcon/tcon/mode", mode);
    }

    public static boolean setmDNIeColorBlind(boolean enable, int[] result) {
        return false;
    }

    public static boolean setPlayVideoSpeed(boolean enable) {
        return false;
    }

    public static boolean setTouchJitterFilter(boolean enable) {
        return false;
    }

    public static void setBatteryADC(String app, boolean enable) {
        IOException e;
        OutputStream out = null;
        try {
            byte[] buffer = new byte[MAX_CMD_LEN];
            try {
                OutputStream out2 = new FileOutputStream(new File("/sys/class/power_supply/battery/" + app));
                if (enable) {
                    try {
                        buffer[0] = (byte) 49;
                    } catch (IOException e2) {
                        e = e2;
                        out = out2;
                        e.printStackTrace();
                        try {
                            out.close();
                        } catch (Exception err) {
                            err.printStackTrace();
                            return;
                        }
                    }
                }
                buffer[0] = (byte) 48;
                out2.write(buffer);
                out2.close();
                out = out2;
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        } catch (IOException e4) {
            e = e4;
            e.printStackTrace();
            out.close();
        }
    }

    public static void setPowerSaveFPS(boolean enable) {
    }

    public static void setMaxCPUFreq(String max_freq) {
        IOException e;
        OutputStream out = null;
        try {
            OutputStream out2 = new FileOutputStream(new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq"));
            try {
                out2.write(max_freq.getBytes());
                out2.close();
                out = out2;
            } catch (IOException e2) {
                e = e2;
                out = out2;
                e.printStackTrace();
                try {
                    out.close();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        } catch (FileNotFoundException e3) {
            try {
                e3.printStackTrace();
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                out.close();
            }
        }
    }

    public static void setTorchLight(boolean enable) {
        if (enable) {
            setTorchLight(14);
        } else {
            setTorchLight(0);
        }
    }

    public static void setTorchLight(int level) {
        String path;
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        FileWriter fw = null;
        String modelname = " ";
        if (modelname.equalsIgnoreCase("GT-N7000") || modelname.equalsIgnoreCase("GT-I9220")) {
            path = "/sys/class/leds/leds-sec/brightness";
        } else if (modelname.equalsIgnoreCase("SHV-E160S") || modelname.equalsIgnoreCase("SHV-E160K") || modelname.equalsIgnoreCase("SHV-E160L")) {
            path = "/sys/class/ledflash/sec_ledflash/torch";
        } else {
            path = "/sys/class/camera/flash/rear_flash";
        }
        try {
            if (!new File(path).exists()) {
                path = "/sys/class/camera/rear/rear_flash";
            }
            FileWriter fw2 = new FileWriter(path);
            if (level <= 0 || level >= 15) {
                fw2.write("0");
            } else {
                try {
                    fw2.write(Integer.toString(level));
                } catch (FileNotFoundException e3) {
                    e = e3;
                    fw = fw2;
                    try {
                        e.printStackTrace();
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    fw = fw2;
                    e222.printStackTrace();
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fw = fw2;
                    if (fw != null) {
                        fw.close();
                    }
                    throw th;
                }
            }
            if (fw2 != null) {
                try {
                    fw2.close();
                    fw = fw2;
                    return;
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                    fw = fw2;
                    return;
                }
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e6) {
            e22222 = e6;
            e22222.printStackTrace();
            if (fw != null) {
                fw.close();
            }
        }
    }

    public static void setEpenHandType(int handtype) {
        IOException e;
        OutputStream out = null;
        try {
            OutputStream out2 = new FileOutputStream(new File("/sys/class/sec/sec_epen/epen_hand"));
            if (handtype == 0) {
                try {
                    out2.write(48);
                } catch (IOException e2) {
                    e = e2;
                    out = out2;
                    e.printStackTrace();
                    try {
                        out.close();
                    } catch (Exception err) {
                        err.printStackTrace();
                        return;
                    }
                }
            } else if (handtype == 1) {
                out2.write(49);
            }
            out2.close();
            out = out2;
        } catch (FileNotFoundException e3) {
            try {
                e3.printStackTrace();
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                out.close();
            }
        }
    }

    public static boolean setEPenType(int type) {
        return sysfsWrite(EPEN_TYPE_PATH, type);
    }

    public static int getEPenType() {
        IOException ee;
        Throwable th;
        BufferedReader br = null;
        int type = -1;
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(EPEN_TYPE_PATH)));
            try {
                type = Integer.parseInt(br2.readLine());
                if (br2 != null) {
                    try {
                        br2.close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                        br = br2;
                    }
                }
                return type;
            } catch (NumberFormatException ne) {
                try {
                    ne.printStackTrace();
                    if (br2 != null) {
                        br2.close();
                        br = null;
                    } else {
                        br = br2;
                    }
                    if (br == null) {
                        return -1;
                    }
                    try {
                        br.close();
                        return -1;
                    } catch (IOException ie2) {
                        ie2.printStackTrace();
                        return -1;
                    }
                } catch (IOException e) {
                    ee = e;
                    br = br2;
                    try {
                        ee.printStackTrace();
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException ie22) {
                                ie22.printStackTrace();
                            }
                        }
                        return type;
                    } catch (Throwable th2) {
                        th = th2;
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException ie222) {
                                ie222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    br = br2;
                    if (br != null) {
                        br.close();
                    }
                    throw th;
                }
            }
        } catch (FileNotFoundException e2) {
            try {
                e2.printStackTrace();
                if (br == null) {
                    return -1;
                }
                try {
                    br.close();
                    return -1;
                } catch (IOException ie2222) {
                    ie2222.printStackTrace();
                    return -1;
                }
            } catch (IOException e3) {
                ee = e3;
                ee.printStackTrace();
                if (br != null) {
                    br.close();
                }
                return type;
            }
        }
    }

    public static boolean setEPenSavingmode(int enable) {
        return sysfsWrite(EPEN_SAVINGMODE_PATH, enable);
    }

    public static boolean enableHoverEvent(boolean enable) {
        if (FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_COMMON_GESTURE_WITH_FINGERHOVER")) {
            try {
                return sysfsWrite("/sys/class/sec/tsp/cmd", enable ? "hover_enable,1" : "hover_enable,0");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean enableHandgripEvent(boolean enable) {
        try {
            return sysfsWrite("/sys/class/sec/tsp/cmd", enable ? "handgrip_enable,1" : "handgrip_enable,0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean enableGloveMode(boolean enable) {
        return true && true;
    }

    public static boolean enableFastGloveMode(boolean enable) {
        boolean ret = true;
        if (FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_COMMON_GESTURE_WITH_FINGERHOVER")) {
            if (enable) {
                try {
                    ret = sysfsWrite("/sys/class/sec/tsp/cmd", "fast_glove_mode,1");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ret = sysfsWrite("/sys/class/sec/tsp/cmd", "fast_glove_mode,0");
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static boolean enableISPFirmwareUpdate(boolean enable) {
        return sysfsWrite("/sys/class/camera/rear/rear_checkApp", enable ? 1 : 0);
    }

    public static boolean enableShowBriefingInformation(boolean enable) {
        try {
            return sysfsWrite("/sys/class/sec/tsp/cmd", enable ? "scrub_enable,1" : "scrub_enable,0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean enableSamsungPay(boolean enable) {
        try {
            return sysfsWrite("/sys/class/sec/tsp/cmd", enable ? "spay_enable,1" : "spay_enable,0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
