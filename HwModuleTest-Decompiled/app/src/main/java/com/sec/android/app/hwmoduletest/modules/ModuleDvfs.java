package com.sec.android.app.hwmoduletest.modules;

import android.content.Context;
import com.samsung.android.os.SemDvfsManager;

public class ModuleDvfs {
    private SemDvfsManager mBusHelper = null;
    private Context mContext;
    private SemDvfsManager mCpuCoreNumHelper = null;
    private SemDvfsManager mCpuHelper = null;
    private SemDvfsManager mCstateDisableHelper = null;

    public ModuleDvfs(Context context) {
        this.mContext = context;
    }

    public void initCpuBoost() {
        this.mCpuHelper = SemDvfsManager.createInstance(this.mContext, 12);
        int[] supportedCpuFreqTable = this.mCpuHelper.getSupportedFrequency();
        if (supportedCpuFreqTable != null) {
            this.mCpuHelper.setDvfsValue(supportedCpuFreqTable[0]);
        }
        this.mCpuCoreNumHelper = SemDvfsManager.createInstance(this.mContext, 14);
        int[] supportedCpuCoreNum = this.mCpuCoreNumHelper.getSupportedFrequency();
        if (supportedCpuCoreNum != null) {
            this.mCpuCoreNumHelper.setDvfsValue(supportedCpuCoreNum[0]);
        }
        this.mBusHelper = SemDvfsManager.createInstance(this.mContext, 19);
        int[] supportedBusFreqTable = this.mBusHelper.getSupportedFrequency();
        if (supportedBusFreqTable != null) {
            this.mBusHelper.setDvfsValue(supportedBusFreqTable[0]);
        }
        this.mCstateDisableHelper = SemDvfsManager.createInstance(this.mContext, 23);
    }

    public void setCpuBoost() {
        if (!(this.mCpuHelper == null || this.mCpuCoreNumHelper == null)) {
            if (this.mBusHelper != null) {
                this.mBusHelper.acquire();
            }
            this.mCpuHelper.acquire();
            this.mCpuCoreNumHelper.acquire();
        }
        if (this.mCstateDisableHelper != null) {
            this.mCstateDisableHelper.acquire();
        }
    }

    public void disableCpuBoost() {
        if (this.mCpuHelper != null) {
            this.mCpuHelper.release();
        }
        if (this.mCpuCoreNumHelper != null) {
            this.mCpuCoreNumHelper.release();
        }
        if (this.mBusHelper != null) {
            this.mBusHelper.release();
        }
        if (this.mCstateDisableHelper != null) {
            this.mCstateDisableHelper.release();
        }
    }
}
