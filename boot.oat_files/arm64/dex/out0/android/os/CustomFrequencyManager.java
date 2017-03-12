package android.os;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomFrequencyManager {
    public static final int CPU_CORE_NUM_MAX_LIMIT = 5;
    public static final int CPU_CORE_NUM_MIN_LIMIT = 4;
    public static final int CPU_DISABLE_CSTATE = 12;
    public static final int CPU_HOTPLUG_DISABLE = 14;
    public static final int CPU_LEGACY_SCHEDULER = 13;
    private static final boolean DEBUG = "eng".equals(Build.TYPE);
    public static final int DVFS_MAX_LIMIT = 2;
    public static final int DVFS_MIN_LIMIT = 1;
    public static final int FREQUENCY_BUS_TYPE_BOOST_MAX_LIMIT = 11;
    public static final int FREQUENCY_BUS_TYPE_BOOST_MIN_LIMIT = 10;
    public static final int FREQUENCY_CPU_TYPE_BOOST_MAX_LIMIT = 7;
    public static final int FREQUENCY_CPU_TYPE_BOOST_MIN_LIMIT = 6;
    public static final int FREQUENCY_LCD_FRAME_RATE = 3;
    public static final int FREQUENCY_MMC_TYPE_BURST_MODE = 8;
    public static final int FREQUENCY_REQ_TYPE_GPU = 1;
    public static final int FREQUENCY_REQ_TYPE_GPU_MAX = 9;
    public static final int PCIE_PSM_DISABLE = 15;
    private static final String TAG = "CustomFrequencyManager";
    private static final Object lock = new Object();
    private static Context mContext = null;
    private static IBinder mServerAppToken;
    private boolean infinitCPUBoostServing = false;
    private boolean infinitCPUCoreServing = false;
    private boolean infinitGPUServing = false;
    private boolean infinitLCDFrameReqServing = false;
    private boolean infinitSysBusReqServing = false;
    Handler mHandler;
    boolean mHeld = false;
    ICustomFrequencyManager mService;

    public abstract class FrequencyRequest {
        public int mFrequency;
        boolean mInvalidParam = false;
        public String mPkgName;
        long mTimeoutMs;
        IBinder mToken;
        public int mType;

        public abstract void cancelFrequencyRequestImpl();

        public abstract void doFrequencyRequestImpl();

        FrequencyRequest(int type, int frequency, long timeout, String pkgName) {
            try {
                CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "Boost Request from package = " + pkgName + " frequency : " + frequency + "type = " + type);
                if (type == 1 || type == 9) {
                    if (!CustomFrequencyManager.this.mService.checkGPUFrequencyRange(frequency)) {
                        CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "GPUFrequencyRequest : invalid frequency range");
                        this.mInvalidParam = true;
                        return;
                    }
                    this.mType = type;
                    this.mFrequency = frequency;
                    this.mTimeoutMs = timeout;
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "!! pkgName = " + pkgName);
                    if (CustomFrequencyManager.mContext != null && "android".equals(CustomFrequencyManager.mContext.getPackageName())) {
                        if (CustomFrequencyManager.mServerAppToken == null && CustomFrequencyManager.mServerAppToken.isBinderAlive()) {
                            this.mToken = CustomFrequencyManager.mServerAppToken;
                        } else {
                            CustomFrequencyManager.createServerAppToken();
                            this.mToken = CustomFrequencyManager.mServerAppToken;
                        }
                    }
                    if (this.mToken == null) {
                        this.mToken = new Binder();
                    }
                    this.mPkgName = pkgName;
                } else if (type == 10 || type == 11) {
                    if (!CustomFrequencyManager.this.mService.checkSysBusFrequencyRange(frequency)) {
                        CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "SysBusFrequencyRequest : invalid frequency range");
                        this.mInvalidParam = true;
                        return;
                    }
                    this.mType = type;
                    this.mFrequency = frequency;
                    this.mTimeoutMs = timeout;
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "!! pkgName = " + pkgName);
                    if (CustomFrequencyManager.mServerAppToken == null) {
                    }
                    CustomFrequencyManager.createServerAppToken();
                    this.mToken = CustomFrequencyManager.mServerAppToken;
                    if (this.mToken == null) {
                        this.mToken = new Binder();
                    }
                    this.mPkgName = pkgName;
                } else {
                    if (type == 7 || type == 6) {
                        if (!CustomFrequencyManager.this.mService.checkCPUBoostRange(frequency)) {
                            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPUDVFSControlRequest : invalid frequency range");
                            this.mInvalidParam = true;
                            return;
                        }
                    }
                    this.mType = type;
                    this.mFrequency = frequency;
                    this.mTimeoutMs = timeout;
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "!! pkgName = " + pkgName);
                    if (CustomFrequencyManager.mServerAppToken == null) {
                    }
                    CustomFrequencyManager.createServerAppToken();
                    this.mToken = CustomFrequencyManager.mServerAppToken;
                    if (this.mToken == null) {
                        this.mToken = new Binder();
                    }
                    this.mPkgName = pkgName;
                }
            } catch (Exception e) {
                CustomFrequencyManager.printExceptionTrace(e);
            }
        }

        public int getLockType() {
            return this.mType;
        }

        public void doFrequencyRequest() {
            if (!this.mInvalidParam) {
                doFrequencyRequestImpl();
            }
        }

        public void cancelFrequencyRequest() {
            if (!this.mInvalidParam) {
                cancelFrequencyRequestImpl();
            }
        }
    }

    private class CPUCoreControlRequest extends FrequencyRequest {
        Runnable mCPUCoreReleaser = new Runnable() {
            public void run() {
                CPUCoreControlRequest.this.cancelFrequencyRequest();
            }
        };

        CPUCoreControlRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPUCoreControlRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.requestCPUCore(this.mType, this.mFrequency, this.mToken, this.mPkgName);
                    if (this.mTimeoutMs == -1) {
                        CustomFrequencyManager.this.infinitCPUCoreServing = true;
                    } else {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mCPUCoreReleaser, this.mTimeoutMs);
                        CustomFrequencyManager.this.infinitCPUCoreServing = false;
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPUCoreReleaser);
                    CustomFrequencyManager.this.mService.releaseCPUCore(this.mType, this.mToken, this.mPkgName);
                    CustomFrequencyManager.this.infinitCPUCoreServing = false;
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private class CPUDVFSControlRequest extends FrequencyRequest {
        Runnable mCPUDVFSReleaser = new Runnable() {
            public void run() {
                CPUDVFSControlRequest.this.cancelFrequencyRequest();
            }
        };
        int mCallingID = -1;

        CPUDVFSControlRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
            switch (type) {
                case 6:
                case 7:
                    int[] supportedFrequency = CustomFrequencyManager.this.getSupportedCPUFrequency();
                    if (supportedFrequency == null) {
                        CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CustomFrequencyManager : CPUDVFSControlRequest : getSupportedFrequency : null");
                        this.mInvalidParam = true;
                        return;
                    }
                    boolean bFound = false;
                    for (int i : supportedFrequency) {
                        if (frequency == i) {
                            bFound = true;
                        }
                    }
                    if (!bFound) {
                        CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CustomFrequencyManager : CPUDVFSControlRequest : invalid frequency");
                        this.mInvalidParam = true;
                        return;
                    }
                    return;
                default:
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CustomFrequencyManager : CPUDVFSControlRequest : invalid type");
                    this.mInvalidParam = true;
                    return;
            }
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPUDVFSControlRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    if (this.mCallingID == -1) {
                        CustomFrequencyManager.this.mService.acquireDVFSLock(this.mType, this.mFrequency, this.mToken, this.mPkgName);
                        if (this.mTimeoutMs == -1) {
                            CustomFrequencyManager.this.infinitCPUBoostServing = true;
                        } else {
                            CustomFrequencyManager.this.mHandler.postDelayed(this.mCPUDVFSReleaser, this.mTimeoutMs);
                        }
                    } else {
                        doFrequencyRequest(this.mCallingID);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public synchronized void doFrequencyRequest(int callingID) {
            try {
                CustomFrequencyManager.this.mService.acquirePersistentDVFSLock(this.mType, this.mFrequency, callingID, this.mPkgName);
            } catch (Exception e) {
                CustomFrequencyManager.printExceptionTrace(e);
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPUDVFSReleaser);
                    CustomFrequencyManager.this.mService.releaseDVFSLock(this.mToken, this.mPkgName);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public synchronized void cancelFrequencyRequest(int callingID) {
            try {
                CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPUDVFSReleaser);
                CustomFrequencyManager.this.mService.releasePersistentDVFSLock(callingID, this.mPkgName);
            } catch (Exception e) {
                CustomFrequencyManager.printExceptionTrace(e);
            }
        }
    }

    public class CPUDisCStateRequest extends FrequencyRequest {
        Runnable mCPUDisCStateReleaser = new Runnable() {
            public void run() {
                CPUDisCStateRequest.this.cancelFrequencyRequest();
            }
        };

        CPUDisCStateRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPUDisCStateRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.disableCPUCState(this.mToken, this.mPkgName);
                    if (this.mTimeoutMs != -1) {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mCPUDisCStateReleaser, this.mTimeoutMs);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPUDisCStateReleaser);
                    CustomFrequencyManager.this.mService.enableCPUCState(this.mToken, this.mPkgName);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    public class CPUHotplugDisableRequest extends FrequencyRequest {
        Runnable mCPUHotplugDisableReleaser = new Runnable() {
            public void run() {
                CPUHotplugDisableRequest.this.cancelFrequencyRequest();
            }
        };

        CPUHotplugDisableRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPUHotplugDisableRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.enableHotplugDisable(this.mToken, this.mPkgName);
                    if (this.mTimeoutMs != -1) {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mCPUHotplugDisableReleaser, this.mTimeoutMs);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPUHotplugDisableReleaser);
                    CustomFrequencyManager.this.mService.disableHotplugDisable(this.mToken, this.mPkgName);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    public class CPULegacySchedulerRequest extends FrequencyRequest {
        Runnable mCPULegacySchedulerReleaser = new Runnable() {
            public void run() {
                CPULegacySchedulerRequest.this.cancelFrequencyRequest();
            }
        };

        CPULegacySchedulerRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "CPULegacySchedulerRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.enableLegacyScheduler(this.mToken, this.mPkgName);
                    if (this.mTimeoutMs != -1) {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mCPULegacySchedulerReleaser, this.mTimeoutMs);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mCPULegacySchedulerReleaser);
                    CustomFrequencyManager.this.mService.disableLegacyScheduler(this.mToken, this.mPkgName);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private class GPUFrequencyRequest extends FrequencyRequest {
        Runnable mGPUReleaser = new Runnable() {
            public void run() {
                GPUFrequencyRequest.this.cancelFrequencyRequest();
            }
        };

        GPUFrequencyRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "GPU : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "GPU : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
                try {
                    CustomFrequencyManager.this.mService.requestGPU(this.mType, this.mFrequency, this.mToken, this.mPkgName);
                    if (this.mTimeoutMs == -1) {
                        CustomFrequencyManager.this.infinitGPUServing = true;
                    } else {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mGPUReleaser, this.mTimeoutMs);
                        CustomFrequencyManager.this.infinitGPUServing = false;
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mGPUReleaser);
                    CustomFrequencyManager.this.mService.releaseGPU(this.mType, this.mToken, this.mPkgName);
                    CustomFrequencyManager.this.infinitGPUServing = false;
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private class LCDFrameRateRequest extends FrequencyRequest {
        Runnable mFrameRateReleaser = new Runnable() {
            public void run() {
                CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "LCDFrameRateRequest:: mFrameRateReleaser -> cancelFrequencyRequest.");
                LCDFrameRateRequest.this.cancelFrequencyRequest();
            }
        };

        LCDFrameRateRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "LCDFrameRate : doFrequencyRequest:: requestLCDFrameRate - mFrequency = " + this.mFrequency + ", mTimeoutMs = " + this.mTimeoutMs + ", mPkgName = " + this.mPkgName);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "LCDFrameRate : doFrequencyRequest:: requestLCDFrameRate - mFrequency = " + this.mFrequency + ", mTimeoutMs = " + this.mTimeoutMs + ", mPkgName = " + this.mPkgName);
                    CustomFrequencyManager.this.mService.requestLCDFrameRate(this.mFrequency, this.mToken, this.mPkgName);
                    if (this.mTimeoutMs == -1) {
                        CustomFrequencyManager.this.infinitLCDFrameReqServing = true;
                    } else {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mFrameRateReleaser, this.mTimeoutMs);
                        CustomFrequencyManager.this.infinitLCDFrameReqServing = false;
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mFrameRateReleaser);
                    CustomFrequencyManager.this.mService.restoreLCDFrameRate(this.mToken, this.mPkgName);
                    CustomFrequencyManager.this.infinitLCDFrameReqServing = false;
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private class MMCBurstControlRequest extends FrequencyRequest {
        Runnable mMCBurstModeReleaser = new Runnable() {
            public void run() {
                CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "MMCBurstControlRequest:: mMCBurstModeReleaser -> cancelFrequencyRequest.");
                MMCBurstControlRequest.this.cancelFrequencyRequest();
            }
        };

        MMCBurstControlRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "MMCBurstControlRequest : doFrequencyRequest:: mFrequency = " + this.mFrequency + ", mTimeoutMs = " + this.mTimeoutMs + ", mPkgName = " + this.mPkgName);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.requestMMCBurstRate(this.mFrequency, this.mToken, this.mPkgName);
                    if (this.mTimeoutMs != -1) {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mMCBurstModeReleaser, this.mTimeoutMs);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mMCBurstModeReleaser);
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "MMCBurstControlRequest.cancelFrequencyRequest");
                    CustomFrequencyManager.this.mService.restoreMMCBurstRate(this.mToken);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    public class PCIePSMDisableRequest extends FrequencyRequest {
        Runnable mPCIePSMDisableReleaser = new Runnable() {
            public void run() {
                PCIePSMDisableRequest.this.cancelFrequencyRequest();
            }
        };

        PCIePSMDisableRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "PCIePSMDisableRequest : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mService.enablePCIePSMDisable(this.mToken, this.mPkgName);
                    if (this.mTimeoutMs != -1) {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mPCIePSMDisableReleaser, this.mTimeoutMs);
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mPCIePSMDisableReleaser);
                    CustomFrequencyManager.this.mService.disablePCIePSMDisable(this.mToken, this.mPkgName);
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private class SysBusFrequencyRequest extends FrequencyRequest {
        Runnable mSysBusReleaser = new Runnable() {
            public void run() {
                SysBusFrequencyRequest.this.cancelFrequencyRequest();
            }
        };

        SysBusFrequencyRequest(int type, int frequency, long timeout, String pkgName) {
            super(type, frequency, timeout, pkgName);
        }

        public void doFrequencyRequestImpl() {
            CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "SysBus : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.logOnEng(CustomFrequencyManager.TAG, "SysBus : doFrequencyRequest::  = " + this.mFrequency + " Timeout : " + this.mTimeoutMs);
                    CustomFrequencyManager.this.mService.requestSysBus(this.mType, this.mFrequency, this.mToken, this.mPkgName);
                    if (this.mTimeoutMs == -1) {
                        CustomFrequencyManager.this.infinitSysBusReqServing = true;
                    } else {
                        CustomFrequencyManager.this.mHandler.postDelayed(this.mSysBusReleaser, this.mTimeoutMs);
                        CustomFrequencyManager.this.infinitSysBusReqServing = false;
                    }
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }

        public void cancelFrequencyRequestImpl() {
            synchronized (this.mToken) {
                try {
                    CustomFrequencyManager.this.mHandler.removeCallbacks(this.mSysBusReleaser);
                    CustomFrequencyManager.this.mService.releaseSysBus(this.mType, this.mToken, this.mPkgName);
                    CustomFrequencyManager.this.infinitSysBusReqServing = false;
                } catch (Exception e) {
                    CustomFrequencyManager.printExceptionTrace(e);
                }
            }
        }
    }

    private CustomFrequencyManager() {
    }

    private static void printExceptionTrace(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    public CustomFrequencyManager(ICustomFrequencyManager service, Handler handler) {
        this.mService = service;
        this.mHandler = handler;
    }

    public int getStandbyTimeInUltraPowerSavingMode() {
        int i = -1;
        if (this.mService != null) {
            try {
                i = this.mService.getStandbyTimeInUltraPowerSavingMode();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return i;
    }

    public int getStandbyTimeInPowerSavingMode() {
        int i = -1;
        if (this.mService != null) {
            try {
                i = this.mService.getStandbyTimeInPowerSavingMode();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return i;
    }

    public int[] getSupportedCPUCoreNum() {
        int[] iArr = null;
        if (this.mService != null) {
            try {
                iArr = this.mService.getSupportedCPUCoreNum();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return iArr;
    }

    public int[] getSupportedGPUFrequency() {
        int[] iArr = null;
        if (this.mService != null) {
            try {
                iArr = this.mService.getSupportedGPUFrequency();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return iArr;
    }

    public int[] getSupportedSysBusFrequency() {
        int[] iArr = null;
        if (this.mService != null) {
            try {
                iArr = this.mService.getSupportedSysBusFrequency();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return iArr;
    }

    public int[] getSupportedCPUFrequency() {
        int[] iArr = null;
        if (this.mService != null) {
            try {
                iArr = this.mService.getSupportedCPUFrequency();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return iArr;
    }

    public int[] getSupportedLCDFrequency() {
        int[] iArr = null;
        if (this.mService != null) {
            try {
                iArr = this.mService.getSupportedLCDFrequency();
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
        return iArr;
    }

    private static synchronized void createServerAppToken() {
        synchronized (CustomFrequencyManager.class) {
            if (mServerAppToken == null) {
                mServerAppToken = new Binder();
            }
        }
    }

    public FrequencyRequest newFrequencyRequest(int type, int frequency, long timeout, String pkgName, Context context) {
        FrequencyRequest req;
        synchronized (lock) {
            mContext = context;
            req = newFrequencyRequest(type, frequency, timeout, pkgName);
            mContext = null;
        }
        return req;
    }

    private FrequencyRequest newFrequencyRequest(int type, int frequency, long timeout, String pkgName) {
        logOnEng(TAG, "newFrequencyRequest  - mFrequency = " + frequency + ", mTimeoutMs = " + timeout + ", mPkgName = " + pkgName);
        if (type == 1 || type == 9) {
            return new GPUFrequencyRequest(type, frequency, timeout, pkgName);
        }
        if (type == 10 || type == 11) {
            return new SysBusFrequencyRequest(type, frequency, timeout, pkgName);
        }
        if (type == 3) {
            return new LCDFrameRateRequest(type, frequency, timeout, pkgName);
        }
        if (type == 4 || type == 5) {
            return new CPUCoreControlRequest(type, frequency, timeout, pkgName);
        }
        if (type == 7 || type == 6) {
            return new CPUDVFSControlRequest(type, frequency, timeout, pkgName);
        }
        if (type == 8) {
            return new MMCBurstControlRequest(type, frequency, timeout, pkgName);
        }
        if (type == 12) {
            return new CPUDisCStateRequest(type, frequency, timeout, pkgName);
        }
        if (type == 13) {
            return new CPULegacySchedulerRequest(type, frequency, timeout, pkgName);
        }
        if (type == 14) {
            return new CPUHotplugDisableRequest(type, frequency, timeout, pkgName);
        }
        if (type == 15) {
            return new PCIePSMDisableRequest(type, frequency, timeout, pkgName);
        }
        return null;
    }

    public void onTopAppChanged(boolean isTopFullscreen) {
        if (this.mService != null) {
            if (isTopFullscreen) {
                try {
                    this.mService.requestMpParameterUpdate("--Nw 2.4 --Tw 150 --Ns 1.4 --Ts 100 --util_h 100 --util_l 0");
                    return;
                } catch (Exception e) {
                    printExceptionTrace(e);
                    return;
                }
            }
            this.mService.requestMpParameterUpdate("--Nw 1.99 --Tw 140 --Ns 1.1 --Ts 190 --util_h 70 --util_l 60");
        }
    }

    public void requestCPUUpdate(int cpu, int enable) {
        if (this.mService != null) {
            try {
                logOnEng(TAG, "in manager requestCPUUpdate" + cpu + " " + enable);
                this.mService.requestCPUUpdate(cpu, enable);
            } catch (RemoteException e) {
                printExceptionTrace(e);
            }
        }
    }

    public void mpdUpdate(int mpEnable) {
        if (this.mService != null) {
            try {
                logOnEng(TAG, "in manager mpUpdate" + mpEnable);
                this.mService.mpdUpdate(mpEnable);
            } catch (RemoteException e) {
                printExceptionTrace(e);
            }
        }
    }

    private static void logOnEng(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public void notifyWmAniationState(long currentTime, boolean isStart) {
        if (this.mService != null) {
            try {
                this.mService.notifyWmAniationState(currentTime, isStart);
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
    }

    public void sendCommandToSSRM(String type, String value) {
        if (this.mService != null) {
            try {
                this.mService.sendCommandToSSRM(type, value);
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
    }

    public void reviewPackage(String packagePath, String Packagename) {
        if (this.mService != null) {
            try {
                this.mService.reviewPackage(packagePath, Packagename);
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
    }

    public void setHglPolicy(String xml) {
        if (this.mService != null) {
            try {
                this.mService.setHglPolicy(xml);
            } catch (Exception e) {
                printExceptionTrace(e);
            }
        }
    }

    public int getBatteryRemainingUsageTime(int mode) {
        int i = -1;
        if (this.mService != null) {
            try {
                i = this.mService.getBatteryRemainingUsageTime(mode);
            } catch (RemoteException e) {
                Log.i(TAG, "problem returning RemainingUsageTime : e = " + e.getMessage());
            }
        }
        return i;
    }

    public byte[] getBatteryStatistics(int what) {
        byte[] bArr = null;
        if (this.mService != null) {
            try {
                bArr = this.mService.getBatteryStatistics(what);
            } catch (RemoteException e) {
                Log.i(TAG, "problem returning batteryStats : e = " + e.getMessage());
            }
        }
        return bArr;
    }

    public boolean deleteBatteryStatsDataBase() {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.deleteBatteryStatsDatabase();
            } catch (RemoteException e) {
                Log.i(TAG, "problem returning batteryStats : e = " + e.getMessage());
            }
        }
        return z;
    }

    public int getBatteryDeltaSum(int what) {
        int i = 0;
        if (this.mService != null) {
            try {
                i = this.mService.getBatteryDeltaSum(what);
            } catch (RemoteException e) {
                Log.i(TAG, "problem returning batteryStats : e = " + e.getMessage());
            }
        }
        return i;
    }

    public int getBatteryTotalCapacity() {
        int i = -1;
        if (this.mService != null) {
            try {
                i = this.mService.getBatteryTotalCapacity();
            } catch (RemoteException e) {
                Log.i(TAG, "Problem getting battery total capacity : e = " + e.getMessage());
            }
        }
        return i;
    }

    public long getSavedTimeAfterKillingApp(String packageName) {
        long j = -1;
        if (this.mService != null) {
            Log.i(TAG, "getSavedTimeAfterKilling called: " + packageName);
            try {
                j = this.mService.getSavedTimeAfterKillingApp(packageName);
            } catch (RemoteException e) {
                Log.i(TAG, "problem returning saved time : e = " + e.getMessage());
            }
        }
        return j;
    }

    public String[] getAbusiveAppList() {
        if (this.mService == null) {
            return new String[0];
        }
        try {
            return this.mService.getAbusiveAppList();
        } catch (RemoteException e) {
            printExceptionTrace(e);
            return new String[0];
        }
    }

    public Intent getDvfsPolicyByHint(String hint) {
        Intent policyIntent = null;
        try {
            policyIntent = this.mService.getDvfsPolicyByHint(hint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policyIntent;
    }

    public void setGamePowerSaving(boolean enabled) {
        try {
            if (this.mService != null) {
                this.mService.setGamePowerSaving(enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public void setGameFps(int level) {
        try {
            if (this.mService != null) {
                this.mService.setGameFps(level);
            }
        } catch (RemoteException e) {
        }
    }

    public int getGameThrottlingLevel() {
        if (this.mService == null) {
            return 0;
        }
        int level = 0;
        try {
            return this.mService.getGameThrottlingLevel();
        } catch (RemoteException e) {
            return level;
        }
    }

    public void setGameTurboMode(boolean enabled) {
        try {
            if (this.mService != null) {
                this.mService.setGameTurboMode(enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public String[] getFrequentlyUsedAppListByLocation(double latitude, double longitude, int numOfItems) {
        String[] result = null;
        try {
            if (this.mService != null) {
                result = this.mService.getFrequentlyUsedAppListByLocation(latitude, longitude, numOfItems);
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public void acquireGovernorParameter(Intent param) {
        try {
            if (this.mService != null) {
                this.mService.acquireGovernorParameter(param);
            }
        } catch (RemoteException e) {
        }
    }

    public void releaseGovernorParameter() {
        try {
            if (this.mService != null) {
                this.mService.releaseGovernorParameter();
            }
        } catch (RemoteException e) {
        }
    }
}
