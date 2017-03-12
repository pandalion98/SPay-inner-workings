package com.samsung.media.fmradio;

import android.os.Handler;
import android.os.Message;
import com.samsung.media.fmradio.internal.IFMEventListener;
import com.samsung.media.fmradio.internal.IFMEventListener.Stub;

public class FMEventListener {
    private static final int EVENT_AF_RECEIVED = 14;
    private static final int EVENT_AF_STARTED = 13;
    private static final int EVENT_CHANNEL_FOUND = 1;
    private static final int EVENT_EAR_PHONE_CONNECT = 8;
    private static final int EVENT_EAR_PHONE_DISCONNECT = 9;
    private static final int EVENT_OFF = 6;
    private static final int EVENT_ON = 5;
    private static final int EVENT_PIECC_EVENT = 18;
    private static final int EVENT_RDS_DISABLED = 12;
    private static final int EVENT_RDS_ENABLED = 11;
    private static final int EVENT_RDS_EVENT = 10;
    private static final int EVENT_REC_FINISH = 17;
    private static final int EVENT_RTPLUS_EVENT = 16;
    private static final int EVENT_SCAN_FINISHED = 3;
    private static final int EVENT_SCAN_STARTED = 2;
    private static final int EVENT_SCAN_STOPPED = 4;
    private static final int EVENT_TUNE = 7;
    private static final int EVENT_VOLUME_LOCK = 15;
    IFMEventListener callback = new Stub() {
        public void onChannelFound(long freq) {
            Message.obtain(FMEventListener.this.mHandler, 1, 0, 0, Long.valueOf(freq)).sendToTarget();
        }

        public void onScanStarted() {
            Message.obtain(FMEventListener.this.mHandler, 2, 0, 0, null).sendToTarget();
        }

        public void onScanStopped(long[] freqArry) {
            Message.obtain(FMEventListener.this.mHandler, 4, 0, 0, freqArry).sendToTarget();
        }

        public void onScanFinished(long[] freqArry) {
            Message.obtain(FMEventListener.this.mHandler, 3, 0, 0, freqArry).sendToTarget();
        }

        public void onOn() {
            Message.obtain(FMEventListener.this.mHandler, 5, 0, 0, null).sendToTarget();
        }

        public void onOff(int reasonCode) {
            Message.obtain(FMEventListener.this.mHandler, 6, 0, 0, Integer.valueOf(reasonCode)).sendToTarget();
        }

        public void onTune(long freq) {
            Message.obtain(FMEventListener.this.mHandler, 7, 0, 0, Long.valueOf(freq)).sendToTarget();
        }

        public void earPhoneConnected() {
            Message.obtain(FMEventListener.this.mHandler, 8, 0, 0, null).sendToTarget();
        }

        public void earPhoneDisconnected() {
            Message.obtain(FMEventListener.this.mHandler, 9, 0, 0, null).sendToTarget();
        }

        public void onRDSReceived(long freq, String channelName, String radioText) {
            Message.obtain(FMEventListener.this.mHandler, 10, 0, 0, new Object[]{Long.valueOf(freq), channelName, radioText}).sendToTarget();
        }

        public void onRTPlusReceived(int contentType1, int startPos1, int additionalLen1, int contentType2, int startPos2, int additionalLen2) {
            Message.obtain(FMEventListener.this.mHandler, 16, 0, 0, new Object[]{Integer.valueOf(contentType1), Integer.valueOf(startPos1), Integer.valueOf(additionalLen1), Integer.valueOf(contentType2), Integer.valueOf(startPos2), Integer.valueOf(additionalLen2)}).sendToTarget();
        }

        public void onPIECCReceived(int pi, int ecc) {
            Message.obtain(FMEventListener.this.mHandler, 18, 0, 0, new Object[]{Integer.valueOf(pi), Integer.valueOf(ecc)}).sendToTarget();
        }

        public void onRDSEnabled() {
            Message.obtain(FMEventListener.this.mHandler, 11, 0, 0, null).sendToTarget();
        }

        public void onRDSDisabled() {
            Message.obtain(FMEventListener.this.mHandler, 12, 0, 0, null).sendToTarget();
            FMEventListener.this.mHandler.removeMessages(10);
            FMEventListener.this.mHandler.removeMessages(16);
        }

        public void onAFStarted() {
            Message.obtain(FMEventListener.this.mHandler, 13, 0, 0, null).sendToTarget();
        }

        public void onAFReceived(long freq) {
            Message.obtain(FMEventListener.this.mHandler, 14, 0, 0, Long.valueOf(freq)).sendToTarget();
        }

        public void volumeLock() {
            Message.obtain(FMEventListener.this.mHandler, 15, 0, 0, null).sendToTarget();
        }

        public void recFinish() {
            Message.obtain(FMEventListener.this.mHandler, 17, 0, 0, null).sendToTarget();
        }
    };
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FMEventListener.this.onChannelFound(msg.obj.longValue());
                    return;
                case 2:
                    FMEventListener.this.onScanStarted();
                    return;
                case 3:
                    FMEventListener.this.onScanFinished((long[]) msg.obj);
                    return;
                case 4:
                    FMEventListener.this.onScanStopped((long[]) msg.obj);
                    return;
                case 5:
                    FMEventListener.this.onOn();
                    return;
                case 6:
                    FMEventListener.this.onOff(((Integer) msg.obj).intValue());
                    return;
                case 7:
                    FMEventListener.this.onTune(((Long) msg.obj).longValue());
                    return;
                case 8:
                    FMEventListener.this.earPhoneConnected();
                    return;
                case 9:
                    FMEventListener.this.earPhoneDisconnected();
                    return;
                case 10:
                    Object[] obArry = (Object[]) msg.obj;
                    FMEventListener.this.onRDSReceived(((Long) obArry[0]).longValue(), (String) obArry[1], (String) obArry[2]);
                    return;
                case 11:
                    FMEventListener.this.onRDSEnabled();
                    return;
                case 12:
                    FMEventListener.this.onRDSDisabled();
                    return;
                case 13:
                    FMEventListener.this.onAFStarted();
                    return;
                case 14:
                    Long freq = (Long) msg.obj;
                    FMEventListener.this.onAFReceived(freq.longValue());
                    FMEventListener.this.onTune(freq.longValue());
                    return;
                case 15:
                    FMEventListener.this.volumeLock();
                    return;
                case 16:
                    Object[] rtpArry = (Object[]) msg.obj;
                    FMEventListener.this.onRTPlusReceived(((Integer) rtpArry[0]).intValue(), ((Integer) rtpArry[1]).intValue(), ((Integer) rtpArry[2]).intValue(), ((Integer) rtpArry[3]).intValue(), ((Integer) rtpArry[4]).intValue(), ((Integer) rtpArry[5]).intValue());
                    return;
                case 17:
                    FMEventListener.this.recFinish();
                    return;
                case 18:
                    Object[] pieccArry = (Object[]) msg.obj;
                    FMEventListener.this.onPIECCReceived(((Integer) pieccArry[0]).intValue(), ((Integer) pieccArry[1]).intValue());
                    return;
                default:
                    return;
            }
        }
    };

    public void onChannelFound(long frequency) {
    }

    public void onScanStarted() {
    }

    public void onScanStopped(long[] frequency) {
    }

    public void onScanFinished(long[] frequency) {
    }

    public void onOn() {
    }

    public void onOff(int reasonCode) {
    }

    public void onTune(long frequency) {
    }

    public void earPhoneConnected() {
    }

    public void earPhoneDisconnected() {
    }

    public void onRDSReceived(long freq, String channelName, String radioText) {
    }

    public void onRTPlusReceived(int contentType1, int startPos1, int additionalLen1, int contentType2, int startPos2, int additionalLen2) {
    }

    public void onPIECCReceived(int pi, int ecc) {
    }

    public void onRDSEnabled() {
    }

    public void onRDSDisabled() {
    }

    public void onAFStarted() {
    }

    public void onAFReceived(long freq) {
    }

    public void volumeLock() {
    }

    public void recFinish() {
    }
}
