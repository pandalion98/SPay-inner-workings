package android.net.wifi.passpoint;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WifiPasspointManager {
    private static final int BASE = 163840;
    private static final boolean DBG = true;
    public static final String PASSPOINT_CRED_CHANGED_ACTION = "android.net.wifi.passpoint.CRED_CHANGE";
    public static final String PASSPOINT_OSU_AVAILABLE_ACTION = "android.net.wifi.passpoint.OSU_AVAILABLE";
    public static final int PASSPOINT_STATE_ACCESS = 3;
    public static final String PASSPOINT_STATE_CHANGED_ACTION = "android.net.wifi.passpoint.STATE_CHANGE";
    public static final int PASSPOINT_STATE_DISABLED = 1;
    public static final int PASSPOINT_STATE_DISCOVERY = 2;
    public static final int PASSPOINT_STATE_PROVISION = 4;
    public static final int PASSPOINT_STATE_UNKNOWN = 0;
    public static final String PASSPOINT_USER_REM_REQ_ACTION = "android.net.wifi.passpoint.USER_REM_REQ";
    public static final String PROTOCOL_DM = "OMA-DM-ClientInitiated";
    public static final String PROTOCOL_SOAP = "SPP-ClientInitiated";
    public static final int REASON_BUSY = 2;
    public static final int REASON_ERROR = 0;
    public static final int REASON_INVALID_PARAMETER = 3;
    public static final int REASON_NOT_TRUSTED = 4;
    public static final int REASON_WIFI_DISABLED = 1;
    public static final int REQUEST_ANQP_INFO = 163841;
    public static final int REQUEST_ANQP_INFO_FAILED = 163842;
    public static final int REQUEST_ANQP_INFO_SUCCEEDED = 163843;
    public static final int REQUEST_OSU_ICON = 163844;
    public static final int REQUEST_OSU_ICON_FAILED = 163845;
    public static final int REQUEST_OSU_ICON_SUCCEEDED = 163846;
    public static final int START_OSU = 163847;
    public static final int START_OSU_BROWSER = 163848;
    public static final int START_OSU_FAILED = 163849;
    public static final int START_OSU_SUCCEEDED = 163850;
    private static final String TAG = "PasspointManager";
    private Context mContext;
    IWifiPasspointManager mService;

    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    public static class Channel {
        private static final int INVALID_LISTENER_KEY = 0;
        private List<ScanResult> mAnqpRequest = new LinkedList();
        private Object mAnqpRequestLock = new Object();
        private AsyncChannel mAsyncChannel = new AsyncChannel();
        private ChannelListener mChannelListener;
        Context mContext;
        private PasspointHandler mHandler;
        private int mListenerKey = 0;
        private HashMap<Integer, Object> mListenerMap = new HashMap();
        private HashMap<Integer, Integer> mListenerMapCount = new HashMap();
        private Object mListenerMapLock = new Object();

        class PasspointHandler extends Handler {
            PasspointHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message message) {
                Object listener;
                switch (message.what) {
                    case 69636:
                        if (Channel.this.mChannelListener != null) {
                            Channel.this.mChannelListener.onChannelDisconnected();
                            Channel.this.mChannelListener = null;
                            return;
                        }
                        return;
                    case 163842:
                        Channel.this.anqpRequestFinish((ScanResult) message.obj);
                        listener = Channel.this.getListener(message.arg2, false);
                        if (listener == null) {
                            Channel.this.getListener(message.arg2, true);
                        }
                        if (listener != null) {
                            ((ActionListener) listener).onFailure(message.arg1);
                            return;
                        }
                        return;
                    case 163843:
                        Channel.this.anqpRequestFinish(message.obj);
                        listener = Channel.this.getListener(message.arg2, false);
                        if (listener != null) {
                            ((ActionListener) listener).onSuccess();
                            return;
                        }
                        return;
                    case 163848:
                        listener = Channel.this.peekListener(message.arg2);
                        if (listener != null) {
                            ParcelableString str = message.obj;
                            if (str == null || str.string == null) {
                                ((OsuRemListener) listener).onBrowserDismiss();
                                return;
                            } else {
                                ((OsuRemListener) listener).onBrowserLaunch(str.string);
                                return;
                            }
                        }
                        return;
                    case 163849:
                        listener = Channel.this.getListener(message.arg2, true);
                        if (listener != null) {
                            ((OsuRemListener) listener).onFailure(message.arg1);
                            return;
                        }
                        return;
                    case 163850:
                        listener = Channel.this.getListener(message.arg2, true);
                        if (listener != null) {
                            ((OsuRemListener) listener).onSuccess();
                            return;
                        }
                        return;
                    default:
                        Log.d(WifiPasspointManager.TAG, "Ignored " + message);
                        return;
                }
            }
        }

        Channel(Context context, Looper looper, ChannelListener l) {
            this.mHandler = new PasspointHandler(looper);
            this.mChannelListener = l;
            this.mContext = context;
        }

        private int putListener(Object listener) {
            return putListener(listener, 1);
        }

        private int putListener(Object listener, int count) {
            if (listener == null || count <= 0) {
                return 0;
            }
            int key;
            synchronized (this.mListenerMapLock) {
                do {
                    key = this.mListenerKey;
                    this.mListenerKey = key + 1;
                } while (key == 0);
                this.mListenerMap.put(Integer.valueOf(key), listener);
                this.mListenerMapCount.put(Integer.valueOf(key), Integer.valueOf(count));
            }
            return key;
        }

        private Object peekListener(int key) {
            Log.d(WifiPasspointManager.TAG, "peekListener() key=" + key);
            if (key == 0) {
                return null;
            }
            Object obj;
            synchronized (this.mListenerMapLock) {
                obj = this.mListenerMap.get(Integer.valueOf(key));
            }
            return obj;
        }

        private Object getListener(int key, boolean forceRemove) {
            Log.d(WifiPasspointManager.TAG, "getListener() key=" + key + " force=" + forceRemove);
            if (key == 0) {
                return null;
            }
            synchronized (this.mListenerMapLock) {
                if (!forceRemove) {
                    int count = ((Integer) this.mListenerMapCount.get(Integer.valueOf(key))).intValue();
                    Log.d(WifiPasspointManager.TAG, "count=" + count);
                    count--;
                    this.mListenerMapCount.put(Integer.valueOf(key), Integer.valueOf(count));
                    if (count > 0) {
                        return null;
                    }
                }
                Log.d(WifiPasspointManager.TAG, "remove key");
                this.mListenerMapCount.remove(Integer.valueOf(key));
                Object remove = this.mListenerMap.remove(Integer.valueOf(key));
                return remove;
            }
        }

        private void anqpRequestStart(ScanResult sr) {
            Log.d(WifiPasspointManager.TAG, "anqpRequestStart sr.bssid=" + sr.BSSID);
            synchronized (this.mAnqpRequestLock) {
                this.mAnqpRequest.add(sr);
            }
        }

        private void anqpRequestFinish(WifiPasspointInfo result) {
            Log.d(WifiPasspointManager.TAG, "anqpRequestFinish pi.bssid=" + result.bssid);
            synchronized (this.mAnqpRequestLock) {
                for (ScanResult sr : this.mAnqpRequest) {
                    if (sr.BSSID.equals(result.bssid)) {
                        Log.d(WifiPasspointManager.TAG, "find hit " + result.bssid);
                        this.mAnqpRequest.remove(sr);
                        Log.d(WifiPasspointManager.TAG, "mAnqpRequest.len=" + this.mAnqpRequest.size());
                        break;
                    }
                }
            }
        }

        private void anqpRequestFinish(ScanResult sr) {
            Log.d(WifiPasspointManager.TAG, "anqpRequestFinish sr.bssid=" + sr.BSSID);
            synchronized (this.mAnqpRequestLock) {
                for (ScanResult sr1 : this.mAnqpRequest) {
                    if (sr1.BSSID.equals(sr.BSSID)) {
                        this.mAnqpRequest.remove(sr1);
                        break;
                    }
                }
            }
        }
    }

    public interface ChannelListener {
        void onChannelDisconnected();
    }

    public interface OsuRemListener {
        void onBrowserDismiss();

        void onBrowserLaunch(String str);

        void onFailure(int i);

        void onSuccess();
    }

    public static class ParcelableString implements Parcelable {
        public static final Creator<ParcelableString> CREATOR = new Creator<ParcelableString>() {
            public ParcelableString createFromParcel(Parcel in) {
                ParcelableString ret = new ParcelableString();
                ret.string = in.readString();
                return ret;
            }

            public ParcelableString[] newArray(int size) {
                return new ParcelableString[size];
            }
        };
        public String string;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.string);
        }
    }

    public WifiPasspointManager(Context context, IWifiPasspointManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public Channel initialize(Context srcContext, Looper srcLooper, ChannelListener listener) {
        Messenger messenger = getMessenger();
        if (messenger == null) {
            return null;
        }
        Channel c = new Channel(srcContext, srcLooper, listener);
        if (c.mAsyncChannel.connectSync(srcContext, c.mHandler, messenger) != 0) {
            return null;
        }
        return c;
    }

    public Messenger getMessenger() {
        try {
            return this.mService.getMessenger();
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getPasspointState() {
        try {
            return this.mService.getPasspointState();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void requestAnqpInfo(Channel c, List<ScanResult> requested, int mask, ActionListener listener) {
        Log.d(TAG, "requestAnqpInfo start");
        Log.d(TAG, "requested.size=" + requested.size());
        checkChannel(c);
        List<ScanResult> list = new ArrayList();
        for (ScanResult sr : requested) {
            if (sr.capabilities.contains("[HS20]")) {
                list.add(sr);
                c.anqpRequestStart(sr);
                Log.d(TAG, "adding " + sr.BSSID);
            }
        }
        int count = list.size();
        Log.d(TAG, "after filter, count=" + count);
        if (count == 0) {
            Log.d(TAG, "ANQP info request contains no HS20 APs, skipped");
            listener.onSuccess();
            return;
        }
        int key = c.putListener(listener, count);
        for (ScanResult sr2 : list) {
            c.mAsyncChannel.sendMessage(163841, mask, key, sr2);
        }
        Log.d(TAG, "requestAnqpInfo end");
    }

    public void requestOsuIcons(Channel c, List<WifiPasspointOsuProvider> list, int resolution, ActionListener listener) {
    }

    public List<WifiPasspointPolicy> requestCredentialMatch(List<ScanResult> requested) {
        try {
            return this.mService.requestCredentialMatch(requested);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<WifiPasspointCredential> getCredentials() {
        try {
            return this.mService.getCredentials();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean addCredential(WifiPasspointCredential cred) {
        try {
            return this.mService.addCredential(cred);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean updateCredential(WifiPasspointCredential cred) {
        try {
            return this.mService.updateCredential(cred);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean removeCredential(WifiPasspointCredential cred) {
        try {
            return this.mService.removeCredential(cred);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void startOsu(Channel c, WifiPasspointOsuProvider osu, OsuRemListener listener) {
        Log.d(TAG, "startOsu start");
        checkChannel(c);
        c.mAsyncChannel.sendMessage(163847, 0, c.putListener(listener), osu);
        Log.d(TAG, "startOsu end");
    }

    public void startRemediation(Channel c, OsuRemListener listener) {
    }

    public void connect(WifiPasspointPolicy policy) {
    }

    private static void checkChannel(Channel c) {
        if (c == null) {
            throw new IllegalArgumentException("Channel needs to be initialized");
        }
    }
}
