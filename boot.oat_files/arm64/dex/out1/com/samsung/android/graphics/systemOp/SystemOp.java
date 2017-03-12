package com.samsung.android.graphics.systemOp;

import android.os.Build;
import android.os.ISecExternalDisplayService;
import android.os.ISecExternalDisplayService.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SystemOp {
    public static final int CoreType_ERROR = 0;
    public static final int CoreType_kCoreTypeBig = 2;
    public static final int CoreType_kCoreTypeLittle = 1;

    public static class CoreStatus {
        public boolean active;
        public int coreType;
    }

    public static class CpuInfo {
        public String cpuArchitecture;
        public int cpuImplementer;
        public int cpuPart;
        public int cpuRevision;
        public int cpuVariant;
        public String features;
        public String hardware;
        public String processor;
    }

    public static boolean isSamsungDevice() {
        if (Build.BRAND.trim().toLowerCase().contains("samsung")) {
            return true;
        }
        return false;
    }

    public static int getCPUCoreCount() {
        int coreCount = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/exynos_info/core_status"));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    } else if (line.contains("cpu ")) {
                        String[] strs = line.split("cpu ", 5);
                        if ((strs[0].trim().compareToIgnoreCase("big") == 0 || strs[0].trim().compareToIgnoreCase("little") == 0) && !line.contains("noncpu")) {
                            coreCount++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SystemOp", "JAVA getCPUCoreCount " + e.getMessage());
                    br.close();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("SystemOp", "------ getCPUCoreCount " + e2.getMessage());
        }
        return coreCount;
    }

    public static CoreStatus getCPUCoreStatus(int coreNu) {
        CoreStatus coreStatus = new CoreStatus();
        coreStatus.coreType = 0;
        coreStatus.active = false;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/exynos_info/core_status"));
            do {
                try {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SystemOp", "JAVA getCPUCoreStatus " + e.getMessage());
                    br.close();
                }
            } while (!line.contains("cpu " + coreNu));
            if (line.toLowerCase().contains("little")) {
                coreStatus.coreType = 1;
            } else if (line.toLowerCase().contains("big")) {
                coreStatus.coreType = 2;
            } else {
                coreStatus.coreType = 0;
            }
            String[] strs = line.split(": ", 5);
            if (strs[1].compareToIgnoreCase(SmartFaceManager.PAGE_MIDDLE) == 0) {
                coreStatus.active = false;
            } else if (strs[1].compareToIgnoreCase(SmartFaceManager.PAGE_BOTTOM) == 0) {
                coreStatus.active = true;
            } else {
                coreStatus.active = false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("SystemOp", "------ getCPUCoreStatus " + e2.getMessage());
        }
        return coreStatus;
    }

    public static CpuInfo getCpuInfo() {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.processor = "";
        cpuInfo.features = "";
        cpuInfo.cpuImplementer = 0;
        cpuInfo.cpuArchitecture = "";
        cpuInfo.cpuVariant = 0;
        cpuInfo.cpuPart = 0;
        cpuInfo.cpuRevision = 0;
        cpuInfo.hardware = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] strs = line.split(": ", 5);
                if (strs.length == 2) {
                    if (strs[0].trim().compareTo("Processor") == 0) {
                        cpuInfo.processor = strs[1];
                    } else {
                        try {
                            if (strs[0].trim().compareTo("Features") == 0) {
                                cpuInfo.features = strs[1];
                            } else if (strs[0].trim().compareTo("CPU implementer") == 0) {
                                hexLowerCase = strs[1].toLowerCase();
                                if (hexLowerCase.contains("x") && hexLowerCase.split("x", 5).length == 2) {
                                    cpuInfo.cpuImplementer = Integer.parseInt(hexLowerCase.split("x", 5)[1], 16);
                                }
                            } else if (strs[0].trim().compareTo("CPU architecture") == 0) {
                                cpuInfo.cpuArchitecture = strs[1];
                            } else if (strs[0].trim().compareTo("CPU variant") == 0) {
                                hexLowerCase = strs[1].toLowerCase();
                                if (hexLowerCase.contains("x") && hexLowerCase.split("x", 5).length == 2) {
                                    cpuInfo.cpuVariant = Integer.parseInt(hexLowerCase.split("x", 5)[1], 16);
                                }
                            } else if (strs[0].trim().compareTo("CPU part") == 0) {
                                hexLowerCase = strs[1].toLowerCase();
                                if (hexLowerCase.contains("x") && hexLowerCase.split("x", 5).length == 2) {
                                    cpuInfo.cpuPart = Integer.parseInt(hexLowerCase.split("x", 5)[1], 16);
                                }
                            } else if (strs[0].trim().compareTo("CPU revision") == 0) {
                                cpuInfo.cpuRevision = Integer.parseInt(strs[1], 10);
                            } else if (strs[0].trim().compareTo("Hardware") == 0) {
                                cpuInfo.hardware = strs[1];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("SystemOp", "JAVA getCpuInfo " + e.getMessage());
                            br.close();
                        }
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("SystemOp", "------ getCpuInfo " + e2.getMessage());
        }
        return cpuInfo;
    }

    public static boolean setDFSPowerLevel(int powerLevel) {
        boolean z = false;
        ISecExternalDisplayService edsService = Stub.asInterface(ServiceManager.getService("SecExternalDisplayService"));
        if (edsService != null) {
            try {
                z = edsService.SecExternalDisplaySetFPS(powerLevel);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
            }
        }
        return z;
    }

    public static int getDFSPowerLevel() {
        int i = 0;
        ISecExternalDisplayService edsService = Stub.asInterface(ServiceManager.getService("SecExternalDisplayService"));
        if (edsService != null) {
            try {
                i = edsService.SecExternalDisplayGetFPS();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
            }
        }
        return i;
    }

    public static boolean isDFSAvailable() {
        boolean z = false;
        ISecExternalDisplayService edsService = Stub.asInterface(ServiceManager.getService("SecExternalDisplayService"));
        if (edsService != null) {
            try {
                z = edsService.SecExternalDisplaySetFPS(-1);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
            }
        }
        return z;
    }

    public static float getDisplayRefreshRate() {
        int dfs = getDFSPowerLevel();
        if (dfs > 0 && dfs <= 100) {
            return 15.0f + (44.0f * (((float) dfs) / 100.0f));
        }
        Log.e("SystemOp", "getDisplayRefreshRate getDFSPowerLevel failed: dfs = " + dfs + " %");
        return 0.0f;
    }

    public static boolean setDisplayRefreshRate(float fps) {
        if (fps < 15.0f || fps > 60.0f) {
            Log.e("SystemOp", "setDisplayRefreshRate input fps is outof range [15-60] ");
            return false;
        } else if (setDFSPowerLevel((int) (((fps - 15.0f) * 100.0f) / 44.0f))) {
            return true;
        } else {
            Log.e("SystemOp", "setDisplayRefreshRate java failed ");
            return false;
        }
    }
}
