package com.android.server;

import android.os.SystemProperties;

public class FMPlayerNative {
    private static final boolean DEBUGGABLE;
    private static FMRadioService mService;

    static class PIECCData {
        public int mECC;
        public int mPI;

        public PIECCData(int PI, int ECC) {
            this.mPI = PI;
            this.mECC = ECC;
        }
    }

    static class RDSData {
        public String mChannelName;
        public long mFreq;
        public String mRadioText;

        public RDSData(long freq, byte[] channelName, byte[] radioText) {
            this.mFreq = freq;
            try {
                this.mChannelName = new String(channelName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.mRadioText = new String(radioText);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public String toString() {
            return "\n== RDSData :--> \nFreq :" + this.mFreq + " \nChannel Name:" + this.mChannelName + "<--" + " \nRadio Text :" + this.mRadioText + "<--: =====";
        }
    }

    static class RTPlusData {
        public int mAdditionalLen1;
        public int mAdditionalLen2;
        public int mContentType1;
        public int mContentType2;
        public int mStartPos1;
        public int mStartPos2;

        public RTPlusData(int contentType1, int startPos1, int additionalLen1, int contentType2, int startPos2, int additionalLen2) {
            this.mContentType1 = contentType1;
            this.mStartPos1 = startPos1;
            this.mAdditionalLen1 = additionalLen1;
            this.mContentType2 = contentType2;
            this.mStartPos2 = startPos2;
            this.mAdditionalLen2 = additionalLen2;
        }
    }

    public native void cancelAFSwitching();

    public native void cancelSeek();

    public native void disableAF();

    public native void disableDNS();

    public native void disableRDS();

    public native void enableAF();

    public native void enableDNS();

    public native void enableRDS();

    public native int getAFRMSSISamples();

    public native int getAFRMSSIThreshold();

    public native int getAFValid_th();

    public native int getAF_th();

    public native int getCFOTh12();

    public native int getCnt_th();

    public native int getCnt_th_2();

    public native long getCurrentChannel();

    public native long getCurrentRSSI();

    public native long getCurrentSNR();

    public native int getGoodChannelRMSSIThreshold();

    public native String getHybridSearch();

    public native long getMaxVolume();

    public native int getOffChannelThreshold();

    public native int getOnChannelThreshold();

    public native int getRMSSIFirstStage();

    public native int getRSSI_th();

    public native int getRSSI_th_2();

    public native int getSINRFirstStage();

    public native int getSINRSamples();

    public native int getSINRThreshold();

    public native int getSNR_th();

    public native int getSNR_th_2();

    public native int getSearchAlgoType();

    public native int getSeekDC();

    public native int getSeekQA();

    public native boolean getSoftMuteMode();

    public native long getVolume();

    public native void muteOff();

    public native void muteOn();

    public native void off();

    public native long on();

    public native long searchAll();

    public native long searchDown();

    public native long searchUp();

    public native long seekDown();

    public native long seekUp();

    public native void setAFRMSSISamples(int i);

    public native void setAFRMSSIThreshold(int i);

    public native void setAFValid_th(int i);

    public native void setAF_th(int i);

    public native void setBand(int i);

    public native void setCFOTh12(int i);

    public native void setChannelSpacing(int i);

    public native void setCnt_th(int i);

    public native void setCnt_th_2(int i);

    public native void setDEConstant(long j);

    public native void setFMIntenna(boolean z);

    public native void setGoodChannelRMSSIThreshold(int i);

    public native void setHybridSearch(String str);

    public native void setMono();

    public native void setOffChannelThreshold(int i);

    public native void setOnChannelThreshold(int i);

    public native void setRMSSIFirstStage(int i);

    public native void setRSSI_th(int i);

    public native void setRSSI_th_2(int i);

    public native void setRecordMode(int i);

    public native void setSINRFirstStage(int i);

    public native void setSINRSamples(int i);

    public native void setSINRThreshold(int i);

    public native void setSNR_th(int i);

    public native void setSNR_th_2(int i);

    public native void setScanning(boolean z);

    public native void setSearchAlgoType(int i);

    public native void setSeekDC(int i);

    public native void setSeekQA(int i);

    public native void setSeekRSSI(long j);

    public native void setSeekSNR(long j);

    public native void setSoftMuteControl(int i, int i2, int i3);

    public native void setSoftmute(boolean z);

    public native void setSpeakerOn(boolean z);

    public native void setStereo();

    public native void setVolume(long j);

    public native void tune(long j);

    static {
        boolean z = true;
        System.loadLibrary("fmradio_jni");
        System.out.println("FMRadio lib loaded");
        if (SystemProperties.getInt("ro.debuggable", 0) != 1) {
            z = false;
        }
        DEBUGGABLE = z;
    }

    public FMPlayerNative(FMRadioService service) {
        mService = service;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        mService = null;
    }

    public static void notifyRDSEvent(RDSData ob) {
        if (mService.isRDSEnable()) {
            if (DEBUGGABLE) {
                FMRadioService.log("Got Events :" + ob);
            }
            mService.notifyEvent(10, ob);
        }
    }

    public static void notifyRTPlusEvent(RTPlusData ob) {
        if (mService.isRDSEnable()) {
            if (DEBUGGABLE) {
                FMRadioService.log("Got notifyRTPlusEvents :" + ob);
            }
            mService.notifyEvent(16, ob);
        }
    }

    public static void notifyPIECCEvent(PIECCData ob) {
        if (mService.isDNSEnable()) {
            if (DEBUGGABLE) {
                FMRadioService.log("Got notifyPIECCEvents :" + ob);
            }
            mService.notifyEvent(18, ob);
        }
    }

    public static void notifyAFStarted() {
        if (mService.isAFEnable()) {
            FMRadioService.log("NotifyAFStarted :");
            mService.notifyEvent(13, null);
        }
    }

    public static void notifyAFDataReceived(long af) {
        if (mService.isAFEnable()) {
            if (DEBUGGABLE) {
                FMRadioService.log("notifyAFDataReceived :" + af);
            }
            mService.notifyEvent(14, Long.valueOf(af));
        }
    }

    public void offFMService() {
    }
}
