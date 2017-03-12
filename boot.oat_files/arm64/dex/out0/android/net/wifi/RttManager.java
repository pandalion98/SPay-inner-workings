package android.net.wifi;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.AsyncChannel;
import java.util.concurrent.CountDownLatch;

public class RttManager {
    public static final int BASE = 160256;
    public static final int CMD_OP_ABORTED = 160260;
    public static final int CMD_OP_FAILED = 160258;
    public static final int CMD_OP_START_RANGING = 160256;
    public static final int CMD_OP_STOP_RANGING = 160257;
    public static final int CMD_OP_SUCCEEDED = 160259;
    private static final boolean DBG = true;
    public static final String DESCRIPTION_KEY = "android.net.wifi.RttManager.Description";
    private static final int INVALID_KEY = 0;
    public static final int PREAMBLE_HT = 2;
    public static final int PREAMBLE_LEGACY = 1;
    public static final int PREAMBLE_VHT = 4;
    public static final int REASON_INVALID_LISTENER = -3;
    public static final int REASON_INVALID_REQUEST = -4;
    public static final int REASON_NOT_AVAILABLE = -2;
    public static final int REASON_PERMISSION_DENIED = -5;
    public static final int REASON_UNSPECIFIED = -1;
    public static final int RTT_BW_10_SUPPORT = 2;
    public static final int RTT_BW_160_SUPPORT = 32;
    public static final int RTT_BW_20_SUPPORT = 4;
    public static final int RTT_BW_40_SUPPORT = 8;
    public static final int RTT_BW_5_SUPPORT = 1;
    public static final int RTT_BW_80_SUPPORT = 16;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_10 = 6;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_160 = 3;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_20 = 0;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_40 = 1;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_5 = 5;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_80 = 2;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_80P80 = 4;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_UNSPECIFIED = -1;
    public static final int RTT_PEER_NAN = 5;
    public static final int RTT_PEER_P2P_CLIENT = 4;
    public static final int RTT_PEER_P2P_GO = 3;
    public static final int RTT_PEER_TYPE_AP = 1;
    public static final int RTT_PEER_TYPE_STA = 2;
    @Deprecated
    public static final int RTT_PEER_TYPE_UNSPECIFIED = 0;
    public static final int RTT_STATUS_ABORTED = 8;
    public static final int RTT_STATUS_FAILURE = 1;
    public static final int RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL = 6;
    public static final int RTT_STATUS_FAIL_BUSY_TRY_LATER = 12;
    public static final int RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE = 15;
    public static final int RTT_STATUS_FAIL_INVALID_TS = 9;
    public static final int RTT_STATUS_FAIL_NOT_SCHEDULED_YET = 4;
    public static final int RTT_STATUS_FAIL_NO_CAPABILITY = 7;
    public static final int RTT_STATUS_FAIL_NO_RSP = 2;
    public static final int RTT_STATUS_FAIL_PROTOCOL = 10;
    public static final int RTT_STATUS_FAIL_REJECTED = 3;
    public static final int RTT_STATUS_FAIL_SCHEDULE = 11;
    public static final int RTT_STATUS_FAIL_TM_TIMEOUT = 5;
    public static final int RTT_STATUS_INVALID_REQ = 13;
    public static final int RTT_STATUS_NO_WIFI = 14;
    public static final int RTT_STATUS_SUCCESS = 0;
    @Deprecated
    public static final int RTT_TYPE_11_MC = 4;
    @Deprecated
    public static final int RTT_TYPE_11_V = 2;
    public static final int RTT_TYPE_ONE_SIDED = 1;
    public static final int RTT_TYPE_TWO_SIDED = 2;
    @Deprecated
    public static final int RTT_TYPE_UNSPECIFIED = 0;
    private static final String TAG = "RttManager";
    private static AsyncChannel sAsyncChannel;
    private static final Object sCapabilitiesLock = new Object();
    private static CountDownLatch sConnected;
    private static HandlerThread sHandlerThread;
    private static int sListenerKey = 1;
    private static final SparseArray sListenerMap = new SparseArray();
    private static final Object sListenerMapLock = new Object();
    private static int sThreadRefCount;
    private static final Object sThreadRefLock = new Object();
    private Context mContext;
    private RttCapabilities mRttCapabilities;
    private IRttManager mService;

    @Deprecated
    public class Capabilities {
        public int supportedPeerType;
        public int supportedType;
    }

    public static class ParcelableRttParams implements Parcelable {
        public static final Creator<ParcelableRttParams> CREATOR = new Creator<ParcelableRttParams>() {
            public ParcelableRttParams createFromParcel(Parcel in) {
                int num = in.readInt();
                if (num == 0) {
                    return new ParcelableRttParams(null);
                }
                RttParams[] params = new RttParams[num];
                for (int i = 0; i < num; i++) {
                    boolean z;
                    params[i] = new RttParams();
                    params[i].deviceType = in.readInt();
                    params[i].requestType = in.readInt();
                    params[i].bssid = in.readString();
                    params[i].channelWidth = in.readInt();
                    params[i].frequency = in.readInt();
                    params[i].centerFreq0 = in.readInt();
                    params[i].centerFreq1 = in.readInt();
                    params[i].numberBurst = in.readInt();
                    params[i].interval = in.readInt();
                    params[i].numSamplesPerBurst = in.readInt();
                    params[i].numRetriesPerMeasurementFrame = in.readInt();
                    params[i].numRetriesPerFTMR = in.readInt();
                    params[i].LCIRequest = in.readInt() == 1;
                    RttParams rttParams = params[i];
                    if (in.readInt() == 1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    rttParams.LCRRequest = z;
                    params[i].burstTimeout = in.readInt();
                    params[i].preamble = in.readInt();
                    params[i].bandwidth = in.readInt();
                }
                return new ParcelableRttParams(params);
            }

            public ParcelableRttParams[] newArray(int size) {
                return new ParcelableRttParams[size];
            }
        };
        public RttParams[] mParams;

        ParcelableRttParams(RttParams[] params) {
            this.mParams = params;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            if (this.mParams != null) {
                dest.writeInt(this.mParams.length);
                for (RttParams params : this.mParams) {
                    int i;
                    dest.writeInt(params.deviceType);
                    dest.writeInt(params.requestType);
                    dest.writeString(params.bssid);
                    dest.writeInt(params.channelWidth);
                    dest.writeInt(params.frequency);
                    dest.writeInt(params.centerFreq0);
                    dest.writeInt(params.centerFreq1);
                    dest.writeInt(params.numberBurst);
                    dest.writeInt(params.interval);
                    dest.writeInt(params.numSamplesPerBurst);
                    dest.writeInt(params.numRetriesPerMeasurementFrame);
                    dest.writeInt(params.numRetriesPerFTMR);
                    dest.writeInt(params.LCIRequest ? 1 : 0);
                    if (params.LCRRequest) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    dest.writeInt(i);
                    dest.writeInt(params.burstTimeout);
                    dest.writeInt(params.preamble);
                    dest.writeInt(params.bandwidth);
                }
                return;
            }
            dest.writeInt(0);
        }
    }

    public static class ParcelableRttResults implements Parcelable {
        public static final Creator<ParcelableRttResults> CREATOR = new Creator<ParcelableRttResults>() {
            public ParcelableRttResults createFromParcel(Parcel in) {
                int num = in.readInt();
                if (num == 0) {
                    return new ParcelableRttResults(null);
                }
                RttResult[] results = new RttResult[num];
                for (int i = 0; i < num; i++) {
                    results[i] = new RttResult();
                    results[i].bssid = in.readString();
                    results[i].burstNumber = in.readInt();
                    results[i].measurementFrameNumber = in.readInt();
                    results[i].successMeasurementFrameNumber = in.readInt();
                    results[i].frameNumberPerBurstPeer = in.readInt();
                    results[i].status = in.readInt();
                    results[i].measurementType = in.readInt();
                    results[i].retryAfterDuration = in.readInt();
                    results[i].ts = in.readLong();
                    results[i].rssi = in.readInt();
                    results[i].rssiSpread = in.readInt();
                    results[i].txRate = in.readInt();
                    results[i].rtt = in.readLong();
                    results[i].rttStandardDeviation = in.readLong();
                    results[i].rttSpread = in.readLong();
                    results[i].distance = in.readInt();
                    results[i].distanceStandardDeviation = in.readInt();
                    results[i].distanceSpread = in.readInt();
                    results[i].burstDuration = in.readInt();
                    results[i].negotiatedBurstNum = in.readInt();
                    results[i].LCI = new WifiInformationElement();
                    results[i].LCI.id = in.readByte();
                    if (results[i].LCI.id != (byte) -1) {
                        results[i].LCI.data = new byte[in.readByte()];
                        in.readByteArray(results[i].LCI.data);
                    }
                    results[i].LCR = new WifiInformationElement();
                    results[i].LCR.id = in.readByte();
                    if (results[i].LCR.id != (byte) -1) {
                        results[i].LCR.data = new byte[in.readByte()];
                        in.readByteArray(results[i].LCR.data);
                    }
                }
                return new ParcelableRttResults(results);
            }

            public ParcelableRttResults[] newArray(int size) {
                return new ParcelableRttResults[size];
            }
        };
        public RttResult[] mResults;

        public ParcelableRttResults(RttResult[] results) {
            this.mResults = results;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            if (this.mResults != null) {
                dest.writeInt(this.mResults.length);
                for (RttResult result : this.mResults) {
                    dest.writeString(result.bssid);
                    dest.writeInt(result.burstNumber);
                    dest.writeInt(result.measurementFrameNumber);
                    dest.writeInt(result.successMeasurementFrameNumber);
                    dest.writeInt(result.frameNumberPerBurstPeer);
                    dest.writeInt(result.status);
                    dest.writeInt(result.measurementType);
                    dest.writeInt(result.retryAfterDuration);
                    dest.writeLong(result.ts);
                    dest.writeInt(result.rssi);
                    dest.writeInt(result.rssiSpread);
                    dest.writeInt(result.txRate);
                    dest.writeLong(result.rtt);
                    dest.writeLong(result.rttStandardDeviation);
                    dest.writeLong(result.rttSpread);
                    dest.writeInt(result.distance);
                    dest.writeInt(result.distanceStandardDeviation);
                    dest.writeInt(result.distanceSpread);
                    dest.writeInt(result.burstDuration);
                    dest.writeInt(result.negotiatedBurstNum);
                    dest.writeByte(result.LCI.id);
                    if (result.LCI.id != (byte) -1) {
                        dest.writeByte((byte) result.LCI.data.length);
                        dest.writeByteArray(result.LCI.data);
                    }
                    dest.writeByte(result.LCR.id);
                    if (result.LCR.id != (byte) -1) {
                        dest.writeInt((byte) result.LCR.data.length);
                        dest.writeByte(result.LCR.id);
                    }
                }
                return;
            }
            dest.writeInt(0);
        }
    }

    public static class RttCapabilities implements Parcelable {
        public static final Creator<RttCapabilities> CREATOR = new Creator<RttCapabilities>() {
            public RttCapabilities createFromParcel(Parcel in) {
                boolean z;
                boolean z2 = true;
                RttCapabilities capabilities = new RttCapabilities();
                capabilities.oneSidedRttSupported = in.readInt() == 1;
                if (in.readInt() == 1) {
                    z = true;
                } else {
                    z = false;
                }
                capabilities.twoSided11McRttSupported = z;
                if (in.readInt() == 1) {
                    z = true;
                } else {
                    z = false;
                }
                capabilities.lciSupported = z;
                if (in.readInt() != 1) {
                    z2 = false;
                }
                capabilities.lcrSupported = z2;
                capabilities.preambleSupported = in.readInt();
                capabilities.bwSupported = in.readInt();
                return capabilities;
            }

            public RttCapabilities[] newArray(int size) {
                return new RttCapabilities[size];
            }
        };
        public int bwSupported;
        public boolean lciSupported;
        public boolean lcrSupported;
        public boolean oneSidedRttSupported;
        public int preambleSupported;
        @Deprecated
        public boolean supportedPeerType;
        @Deprecated
        public boolean supportedType;
        public boolean twoSided11McRttSupported;

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("oneSidedRtt ").append(this.oneSidedRttSupported ? "is Supported. " : "is not supported. ").append("twoSided11McRtt ").append(this.twoSided11McRttSupported ? "is Supported. " : "is not supported. ").append("lci ").append(this.lciSupported ? "is Supported. " : "is not supported. ").append("lcr ").append(this.lcrSupported ? "is Supported. " : "is not supported. ");
            if ((this.preambleSupported & 1) != 0) {
                sb.append("Legacy ");
            }
            if ((this.preambleSupported & 2) != 0) {
                sb.append("HT ");
            }
            if ((this.preambleSupported & 4) != 0) {
                sb.append("VHT ");
            }
            sb.append("is supported. \n");
            if ((this.bwSupported & 1) != 0) {
                sb.append("5 MHz ");
            }
            if ((this.bwSupported & 2) != 0) {
                sb.append("10 MHz ");
            }
            if ((this.bwSupported & 4) != 0) {
                sb.append("20 MHz ");
            }
            if ((this.bwSupported & 8) != 0) {
                sb.append("40 MHz ");
            }
            if ((this.bwSupported & 16) != 0) {
                sb.append("80 MHz ");
            }
            if ((this.bwSupported & 32) != 0) {
                sb.append("160 MHz ");
            }
            sb.append("is supported.");
            return sb.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            int i2 = 1;
            dest.writeInt(this.oneSidedRttSupported ? 1 : 0);
            if (this.twoSided11McRttSupported) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            if (this.lciSupported) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            if (!this.lcrSupported) {
                i2 = 0;
            }
            dest.writeInt(i2);
            dest.writeInt(this.preambleSupported);
            dest.writeInt(this.bwSupported);
        }
    }

    public interface RttListener {
        void onAborted();

        void onFailure(int i, String str);

        void onSuccess(RttResult[] rttResultArr);
    }

    public static class RttParams {
        public boolean LCIRequest;
        public boolean LCRRequest;
        public int bandwidth = 4;
        public String bssid;
        public int burstTimeout = 15;
        public int centerFreq0;
        public int centerFreq1;
        public int channelWidth;
        public int deviceType = 1;
        public int frequency;
        public int interval;
        public int numRetriesPerFTMR = 0;
        public int numRetriesPerMeasurementFrame = 0;
        public int numSamplesPerBurst = 8;
        @Deprecated
        public int num_retries;
        @Deprecated
        public int num_samples;
        public int numberBurst = 0;
        public int preamble = 2;
        public int requestType = 1;
    }

    public static class RttResult {
        public WifiInformationElement LCI;
        public WifiInformationElement LCR;
        public String bssid;
        public int burstDuration;
        public int burstNumber;
        public int distance;
        public int distanceSpread;
        public int distanceStandardDeviation;
        @Deprecated
        public int distance_cm;
        @Deprecated
        public int distance_sd_cm;
        @Deprecated
        public int distance_spread_cm;
        public int frameNumberPerBurstPeer;
        public int measurementFrameNumber;
        public int measurementType;
        public int negotiatedBurstNum;
        @Deprecated
        public int requestType;
        public int retryAfterDuration;
        public int rssi;
        public int rssiSpread;
        @Deprecated
        public int rssi_spread;
        public long rtt;
        public long rttSpread;
        public long rttStandardDeviation;
        @Deprecated
        public long rtt_ns;
        @Deprecated
        public long rtt_sd_ns;
        @Deprecated
        public long rtt_spread_ns;
        public int rxRate;
        public int status;
        public int successMeasurementFrameNumber;
        public long ts;
        public int txRate;
        @Deprecated
        public int tx_rate;
    }

    private static class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            Log.i(RttManager.TAG, "RTT manager get message: " + msg.what);
            switch (msg.what) {
                case DevicePolicyManager.PASSWORD_QUALITY_FINGERPRINT_OLD /*69632*/:
                    if (msg.arg1 == 0) {
                        RttManager.sAsyncChannel.sendMessage(69633);
                    } else {
                        Log.e(RttManager.TAG, "Failed to set up channel connection");
                        RttManager.sAsyncChannel = null;
                    }
                    RttManager.sConnected.countDown();
                    return;
                case 69634:
                    return;
                case 69636:
                    Log.e(RttManager.TAG, "Channel connection lost");
                    RttManager.sAsyncChannel = null;
                    getLooper().quit();
                    return;
                default:
                    Object listener = RttManager.getListener(msg.arg2);
                    if (listener == null) {
                        Log.e(RttManager.TAG, "invalid listener key = " + msg.arg2);
                        return;
                    }
                    Log.i(RttManager.TAG, "listener key = " + msg.arg2);
                    switch (msg.what) {
                        case RttManager.CMD_OP_FAILED /*160258*/:
                            reportFailure(listener, msg);
                            RttManager.removeListener(msg.arg2);
                            return;
                        case RttManager.CMD_OP_SUCCEEDED /*160259*/:
                            reportSuccess(listener, msg);
                            RttManager.removeListener(msg.arg2);
                            return;
                        case RttManager.CMD_OP_ABORTED /*160260*/:
                            ((RttListener) listener).onAborted();
                            RttManager.removeListener(msg.arg2);
                            return;
                        default:
                            Log.d(RttManager.TAG, "Ignoring message " + msg.what);
                            return;
                    }
            }
        }

        void reportSuccess(Object listener, Message msg) {
            RttListener rttListener = (RttListener) listener;
            ((RttListener) listener).onSuccess(msg.obj.mResults);
        }

        void reportFailure(Object listener, Message msg) {
            RttListener rttListener = (RttListener) listener;
            ((RttListener) listener).onFailure(msg.arg1, msg.obj.getString(RttManager.DESCRIPTION_KEY));
        }
    }

    public static class WifiInformationElement {
        public byte[] data;
        public byte id;
    }

    @Deprecated
    public Capabilities getCapabilities() {
        return new Capabilities();
    }

    public RttCapabilities getRttCapabilities() {
        RttCapabilities rttCapabilities;
        synchronized (sCapabilitiesLock) {
            if (this.mRttCapabilities == null) {
                try {
                    this.mRttCapabilities = this.mService.getRttCapabilities();
                } catch (RemoteException e) {
                    Log.e(TAG, "Can not get RTT Capabilities");
                }
            }
            rttCapabilities = this.mRttCapabilities;
        }
        return rttCapabilities;
    }

    private boolean rttParamSanity(RttParams params, int index) {
        if (this.mRttCapabilities == null && getRttCapabilities() == null) {
            Log.e(TAG, "Can not get RTT capabilities");
            throw new IllegalStateException("RTT chip is not working");
        } else if (params.deviceType != 1) {
            return false;
        } else {
            if (params.requestType != 1 && params.requestType != 2) {
                Log.e(TAG, "Request " + index + ": Illegal Request Type: " + params.requestType);
                return false;
            } else if (params.requestType == 1 && !this.mRttCapabilities.oneSidedRttSupported) {
                Log.e(TAG, "Request " + index + ": One side RTT is not supported");
                return false;
            } else if (params.requestType == 2 && !this.mRttCapabilities.twoSided11McRttSupported) {
                Log.e(TAG, "Request " + index + ": two side RTT is not supported");
                return false;
            } else if (params.bssid == null || params.bssid.isEmpty()) {
                Log.e(TAG, "No BSSID in params");
                return false;
            } else if (params.numberBurst != 0) {
                Log.e(TAG, "Request " + index + ": Illegal number of burst: " + params.numberBurst);
                return false;
            } else if (params.numSamplesPerBurst <= 0 || params.numSamplesPerBurst > 31) {
                Log.e(TAG, "Request " + index + ": Illegal sample number per burst: " + params.numSamplesPerBurst);
                return false;
            } else if (params.numRetriesPerMeasurementFrame < 0 || params.numRetriesPerMeasurementFrame > 3) {
                Log.e(TAG, "Request " + index + ": Illegal measurement frame retry number:" + params.numRetriesPerMeasurementFrame);
                return false;
            } else if (params.numRetriesPerFTMR < 0 || params.numRetriesPerFTMR > 3) {
                Log.e(TAG, "Request " + index + ": Illegal FTMR frame retry number:" + params.numRetriesPerFTMR);
                return false;
            } else if (params.LCIRequest && !this.mRttCapabilities.lciSupported) {
                Log.e(TAG, "Request " + index + ": LCI is not supported");
                return false;
            } else if (params.LCRRequest && !this.mRttCapabilities.lcrSupported) {
                Log.e(TAG, "Request " + index + ": LCR is not supported");
                return false;
            } else if (params.burstTimeout < 1 || (params.burstTimeout > 11 && params.burstTimeout != 15)) {
                Log.e(TAG, "Request " + index + ": Illegal burst timeout: " + params.burstTimeout);
                return false;
            } else if ((params.preamble & this.mRttCapabilities.preambleSupported) == 0) {
                Log.e(TAG, "Request " + index + ": Do not support this preamble: " + params.preamble);
                return false;
            } else if ((params.bandwidth & this.mRttCapabilities.bwSupported) != 0) {
                return true;
            } else {
                Log.e(TAG, "Request " + index + ": Do not support this bandwidth: " + params.bandwidth);
                return false;
            }
        }
    }

    public void startRanging(RttParams[] params, RttListener listener) {
        int index = 0;
        RttParams[] arr$ = params;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (rttParamSanity(arr$[i$], index)) {
                index++;
                i$++;
            } else {
                throw new IllegalArgumentException("RTT Request Parameter Illegal");
            }
        }
        validateChannel();
        ParcelableRttParams parcelableParams = new ParcelableRttParams(params);
        Log.i(TAG, "Send RTT request to RTT Service");
        sAsyncChannel.sendMessage(160256, 0, putListener(listener), parcelableParams);
    }

    public void stopRanging(RttListener listener) {
        validateChannel();
        sAsyncChannel.sendMessage(CMD_OP_STOP_RANGING, 0, removeListener((Object) listener));
    }

    public RttManager(Context context, IRttManager service) {
        this.mContext = context;
        this.mService = service;
        init();
    }

    private void init() {
        synchronized (sThreadRefLock) {
            int i = sThreadRefCount + 1;
            sThreadRefCount = i;
            if (i == 1) {
                Messenger messenger = null;
                try {
                    Log.d(TAG, "Get the messenger from " + this.mService);
                    messenger = this.mService.getMessenger();
                } catch (RemoteException e) {
                } catch (SecurityException e2) {
                }
                if (messenger == null) {
                    sAsyncChannel = null;
                    return;
                }
                sHandlerThread = new HandlerThread(TAG);
                sAsyncChannel = new AsyncChannel();
                sConnected = new CountDownLatch(1);
                sHandlerThread.start();
                sAsyncChannel.connect(this.mContext, new ServiceHandler(sHandlerThread.getLooper()), messenger);
                try {
                    sConnected.await();
                } catch (InterruptedException e3) {
                    Log.e(TAG, "interrupted wait at init");
                }
            }
        }
    }

    private void validateChannel() {
        if (sAsyncChannel == null) {
            throw new IllegalStateException("No permission to access and change wifi or a bad initialization");
        }
    }

    private static int putListener(Object listener) {
        if (listener == null) {
            return 0;
        }
        int key;
        synchronized (sListenerMapLock) {
            do {
                key = sListenerKey;
                sListenerKey = key + 1;
            } while (key == 0);
            sListenerMap.put(key, listener);
        }
        return key;
    }

    private static Object getListener(int key) {
        if (key == 0) {
            return null;
        }
        Object listener;
        synchronized (sListenerMapLock) {
            listener = sListenerMap.get(key);
        }
        return listener;
    }

    private static int getListenerKey(Object listener) {
        int i = 0;
        if (listener != null) {
            synchronized (sListenerMapLock) {
                int index = sListenerMap.indexOfValue(listener);
                if (index == -1) {
                } else {
                    i = sListenerMap.keyAt(index);
                }
            }
        }
        return i;
    }

    private static Object removeListener(int key) {
        if (key == 0) {
            return null;
        }
        Object listener;
        synchronized (sListenerMapLock) {
            listener = sListenerMap.get(key);
            sListenerMap.remove(key);
        }
        return listener;
    }

    private static int removeListener(Object listener) {
        int key = getListenerKey(listener);
        if (key != 0) {
            synchronized (sListenerMapLock) {
                sListenerMap.remove(key);
            }
        }
        return key;
    }
}
