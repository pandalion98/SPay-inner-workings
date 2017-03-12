package android.app;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.app.IVRManagerService;
import com.android.internal.app.IVRManagerService.Stub;

public class VRManager implements IVRManager {
    public static final String INTENT_ACTION_HMT_CONNECTED = "com.samsung.intent.action.HMT_CONNECTED";
    public static final String INTENT_ACTION_HMT_DISCONNECTED = "com.samsung.intent.action.HMT_DISCONNECTED";
    private Context mContext;
    private IVRManagerService mService;

    VRManager(Context context) {
        this.mContext = context;
    }

    private synchronized IVRManagerService getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("vr"));
            if (this.mService == null) {
                Slog.w("VRManager", "warning: no VR_MANAGER_SERVICE");
            }
        }
        return this.mService;
    }

    public boolean isConnected() {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.isConnected();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean setThreadSchedFifo(String pkg, int pid, int tid, int prio) {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.setThreadSchedFifo(pkg, pid, tid, prio);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int setAffinity(int pid, int[] cpus) {
        int result = -1;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.setAffinity(pid, cpus);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String vrManagerVersion() {
        return "0.3.0-2014-04-21";
    }

    public String vrOVRVersion() {
        return "0.3.0-2014-04-21";
    }

    public void setOption(String optionName, String value) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setOption(optionName, value);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getOption(String optionName) {
        String result = null;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.getOption(optionName);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int[] setCPUClockMhz(String pkg, int[] mhz, int corenum) {
        int[] result = new int[]{0};
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.setCPUClockMhz(pkg, mhz, corenum);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void releaseCPUMhz(String pkg) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.releaseCPUMhz(pkg);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int setGPUClockMhz(String pkg, int mhz) {
        int result = 0;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.setGPUClockMhz(pkg, mhz);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void releaseGPUMhz(String pkg) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.releaseGPUMhz(pkg);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean relFreq(String pkg) {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.relFreq(pkg);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int[] return2EnableFreqLev() {
        int[] result = new int[]{0};
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.return2EnableFreqLev();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int[] SetVrClocks(String pkg, int cpu, int gpu) {
        int[] result = new int[]{0};
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.SetVrClocks(pkg, cpu, gpu);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int GetPowerLevelState() {
        int result = -1;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.GetPowerLevelState();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean setVideoMode(String pkg, float dutyCycle, boolean monoMode) {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.setVideoMode(pkg, dutyCycle, monoMode);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setSystemOption(String option, String value) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setSystemOption(option, value);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getSystemOption(String option) {
        String result = null;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.getSystemOption(option);
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRMode(boolean enable) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRMode(enable);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isVRMode() {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.isVRMode();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRBright(int bright) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRBright(bright);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getVRBright() {
        int result = 0;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.getVRBright();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRColorTemperature(int value) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRColorTemperature(value);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getVRColorTemperature() {
        int result = 0;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.getVRColorTemperature();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRDarkAdaptation(boolean enable) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRDarkAdaptation(enable);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isVRDarkAdaptationEnabled() {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.isVRDarkAdaptationEnabled();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRComfortableView(boolean enable) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRComfortableView(enable);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isVRComfortableViewEnabled() {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.isVRComfortableViewEnabled();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setVRLowPersistence(boolean enable) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.setVRLowPersistence(enable);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isVRLowPersistenceEnabled() {
        boolean result = false;
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                result = svc.isVRLowPersistenceEnabled();
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void enforceCallingSelfPermission(String method) throws SecurityException {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.enforceCallingSelfPermission(method);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}
