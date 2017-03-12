package com.samsung.media.fmradio;

import android.content.Context;
import android.media.AudioManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.media.fmradio.internal.IFMPlayer;
import com.samsung.media.fmradio.internal.IFMPlayer.Stub;

public class FMPlayer {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "FMPlayer";
    public static final int OFF_AIRPLANE_MODE_SET = 3;
    public static final int OFF_BATTERY_LOW = 7;
    public static final int OFF_CALL_ACTIVE = 1;
    public static final int OFF_DEVICE_SHUTDOWN = 6;
    public static final int OFF_EAR_PHONE_DISCONNECT = 2;
    public static final int OFF_NORMAL = 0;
    public static final int OFF_PAUSE_COMMAND = 5;
    public static final int OFF_STOP_COMMAND = 4;
    static Context mContext;
    private AudioManager mAudioManager;
    private IFMPlayer mPlayer = Stub.asInterface(ServiceManager.getService(LOG_TAG));

    public void log(String str) {
        Log.i(LOG_TAG, str);
    }

    public FMPlayer(Context context) {
        mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        log("Player created :" + this.mPlayer);
    }

    public boolean on() throws FMPlayerException {
        if (isTvOutPlugged()) {
            throw new FMPlayerException(7, "TV out is on", new Throwable("TV out is on."));
        } else if (!isHeadsetPlugged()) {
            throw new FMPlayerException(4, "Headset is not presents.", new Throwable("Headset is not presents."));
        } else if (isAirPlaneMode()) {
            throw new FMPlayerException(5, "AirPlane mode is on.", new Throwable("AirPlane mode is on."));
        } else {
            boolean val = false;
            try {
                val = this.mPlayer.on();
            } catch (RemoteException e) {
                remoteError(e);
            }
            if (!isBatteryLow()) {
                return val;
            }
            throw new FMPlayerException(6, "Battery is low.", new Throwable("Batterys is low."));
        }
    }

    public boolean on(boolean testMode) throws FMPlayerException {
        if (!testMode) {
            return on();
        }
        if (isAirPlaneMode()) {
            throw new FMPlayerException(5, "AirPlane mode is on.", new Throwable("AirPlane mode is on."));
        }
        boolean val = false;
        try {
            return this.mPlayer.on_in_testmode();
        } catch (RemoteException e) {
            remoteError(e);
            return val;
        }
    }

    public boolean isHeadsetPlugged() throws FMPlayerException {
        try {
            return this.mPlayer.isHeadsetPlugged();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isTvOutPlugged() throws FMPlayerException {
        try {
            return this.mPlayer.isTvOutPlugged();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isAirPlaneMode() throws FMPlayerException {
        try {
            return this.mPlayer.isAirPlaneMode();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isBatteryLow() throws FMPlayerException {
        try {
            return this.mPlayer.isBatteryLow();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean setSpeakerOn(boolean bSpeakerOn) throws FMPlayerException {
        log("setting bSpeakerOn = :" + bSpeakerOn);
        try {
            this.mPlayer.setSpeakerOn(bSpeakerOn);
        } catch (RemoteException e) {
            remoteError(e);
        }
        this.mAudioManager.setRadioSpeakerOn(bSpeakerOn);
        if (this.mAudioManager.isRadioSpeakerOn()) {
            return false;
        }
        return true;
    }

    public boolean off() throws FMPlayerException {
        boolean val = false;
        try {
            val = this.mPlayer.off();
        } catch (RemoteException e) {
            remoteError(e);
        }
        return val;
    }

    public boolean isOn() throws FMPlayerException {
        try {
            return this.mPlayer.isOn();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void scan() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            this.mPlayer.scan();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long searchDown() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchDown();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void setStereo() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setStereo();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setMono() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setMono();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long searchUp() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchUp();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long searchAll() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchAll();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void enableRDS() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.enableRDS();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void disableRDS() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.disableRDS();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void enableDNS() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.enableDNS();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void disableDNS() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.disableDNS();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void enableAF() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.enableAF();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void disableAF() throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.disableAF();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void cancelAFSwitching() throws FMPlayerException {
        try {
            this.mPlayer.cancelAFSwitching();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setBand(int band) throws FMPlayerException {
        try {
            this.mPlayer.setBand(band);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setChannelSpacing(int spacing) throws FMPlayerException {
        try {
            this.mPlayer.setChannelSpacing(spacing);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean cancelScan() throws FMPlayerException {
        try {
            return this.mPlayer.cancelScan();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isScanning() throws FMPlayerException {
        try {
            return this.mPlayer.isScanning();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isSeeking() throws FMPlayerException {
        try {
            return this.mPlayer.isSeeking();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    private void remoteError(RemoteException e) throws FMPlayerException {
        e.printStackTrace();
        throw new FMPlayerException(1, "Radio service is not running restart the phone.", e.fillInStackTrace());
    }

    public boolean tune(long frequency) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.tune(frequency);
            return true;
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean mute(boolean value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.mute(value);
            return true;
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void setDEConstant(long value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setDEConstant(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSeekRSSI(long value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSeekRSSI(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSeekSNR(long value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSeekSNR(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long getCurrentRSSI() throws FMPlayerException {
        if (isOn()) {
            try {
                return this.mPlayer.getCurrentRSSI();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return -1;
    }

    public long getCurrentSNR() throws FMPlayerException {
        if (isOn()) {
            try {
                return this.mPlayer.getCurrentSNR();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return -1;
    }

    public long seekUp() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.seekUp();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long seekDown() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.seekDown();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void cancelSeek() throws FMPlayerException {
        try {
            this.mPlayer.cancelSeek();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long getCurrentChannel() throws FMPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.getCurrentChannel();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void setVolume(long val) throws FMPlayerException {
        try {
            this.mPlayer.setVolume(val);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long getVolume() throws FMPlayerException {
        try {
            return this.mPlayer.getVolume();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void setRecordMode(int is_record) throws FMPlayerException {
        try {
            this.mPlayer.setRecordMode(is_record);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public long getMaxVolume() throws FMPlayerException {
        try {
            return this.mPlayer.getMaxVolume();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long[] getLastScanResult() throws FMPlayerException {
        long[] jArr = null;
        if (!isScanning()) {
            try {
                jArr = this.mPlayer.getLastScanResult();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return jArr;
    }

    private void checkOnStatus() throws FMPlayerException {
        if (!isOn()) {
            throw new FMPlayerException(1, "Player is not ON.Call on() method to start player", new Throwable("Player is not ON. use method on() to switch on FM player"));
        }
    }

    private void checkBusy() throws FMPlayerException {
        int code = 0;
        try {
            code = this.mPlayer.isBusy();
        } catch (RemoteException e) {
            remoteError(e);
        }
        if (code == 1) {
            throw new FMPlayerException(3, "Player is scanning channel", new Throwable("Player is busy in scanning. Use cancelScan to stop scanning"));
        }
    }

    public boolean isRDSEnable() throws FMPlayerException {
        try {
            return this.mPlayer.isRDSEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isDNSEnable() throws FMPlayerException {
        try {
            return this.mPlayer.isDNSEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isAFEnable() throws FMPlayerException {
        try {
            return this.mPlayer.isAFEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void setListener(FMEventListener listener) throws FMPlayerException {
        if (listener != null) {
            try {
                this.mPlayer.setListener(listener.callback);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
    }

    public void removeListener(FMEventListener listener) throws FMPlayerException {
        if (listener != null) {
            try {
                this.mPlayer.removeListener(listener.callback);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
    }

    public void setRSSI_th(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setRSSI_th(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSNR_th(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSNR_th(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setCnt_th(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setCnt_th(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setRSSI_th_2(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setRSSI_th_2(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSNR_th_2(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSNR_th_2(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setCnt_th_2(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setCnt_th_2(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void SkipTuning_Value() throws FMPlayerException {
        try {
            this.mPlayer.SkipTuning_Value();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int getRSSI_th() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getRSSI_th();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public int getSNR_th() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSNR_th();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public int getCnt_th() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getCnt_th();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public int getRSSI_th_2() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getRSSI_th_2();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public int getSNR_th_2() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSNR_th_2();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public int getCnt_th_2() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getCnt_th_2();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetAF_th(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setAF_th(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetAF_th() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getAF_th();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetAFValid_th(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setAFValid_th(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetAFValid_th() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getAFValid_th();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void setFMIntenna(boolean setFMIntenna) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setFMIntenna(setFMIntenna);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSoftmute(boolean state) throws FMPlayerException {
        try {
            this.mPlayer.setSoftmute(state);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean getSoftMuteMode() throws FMPlayerException {
        boolean val = false;
        try {
            val = this.mPlayer.getSoftMuteMode();
        } catch (RemoteException e) {
            remoteError(e);
        }
        return val;
    }

    public void setSoftMuteControl(int min_RSSI, int max_RSSI, int max_attenuation) throws FMPlayerException {
        try {
            this.mPlayer.setSoftMuteControl(min_RSSI, max_RSSI, max_attenuation);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void SetSearchAlgoType(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSearchAlgoType(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSearchAlgoType() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSearchAlgoType();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetSINRSamples(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSINRSamples(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSINRSamples() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSINRSamples();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetOnChannelThreshold(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setOnChannelThreshold(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetOnChannelThreshold() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getOnChannelThreshold();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetOffChannelThreshold(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setOffChannelThreshold(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetOffChannelThreshold() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getOffChannelThreshold();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetSINRThreshold(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSINRThreshold(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSINRThreshold() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSINRThreshold();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetCFOTh12(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setCFOTh12(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetCFOTh12() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getCFOTh12();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetRMSSIFirstStage(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setRMSSIFirstStage(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetRMSSIFirstStage() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getRMSSIFirstStage();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetSINRFirstStage(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSINRFirstStage(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSINRFirstStage() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSINRFirstStage();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetAFRMSSIThreshold(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setAFRMSSIThreshold(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetAFRMSSIThreshold() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getAFRMSSIThreshold();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetAFRMSSISamples(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setAFRMSSISamples(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetAFRMSSISamples() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getAFRMSSISamples();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetGoodChannelRMSSIThreshold(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setGoodChannelRMSSIThreshold(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetGoodChannelRMSSIThreshold() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getGoodChannelRMSSIThreshold();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetHybridSearch(String value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setHybridSearch(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public String GetHybridSearch() throws FMPlayerException {
        String val = "";
        if (isOn()) {
            try {
                val = this.mPlayer.getHybridSearch();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetSeekDC(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSeekDC(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSeekDC() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSeekDC();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    public void SetSeekQA(int value) throws FMPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setSeekQA(value);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public int GetSeekQA() throws FMPlayerException {
        int val = -1;
        if (isOn()) {
            try {
                val = this.mPlayer.getSeekQA();
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return val;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.mAudioManager = null;
        this.mPlayer = null;
    }

    public void setInternetStreamingMode(boolean mode) throws FMPlayerException {
        try {
            this.mPlayer.setInternetStreamingMode(mode);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }
}
