package android.net.wifi.hs20;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WifiHs20Manager {
    public static final String ACTION_HS20_PROVISIONING_STATE_CHANGE = "adroid.net.wifi.hs20.provisioning_state";
    public static final String ACTION_SEC_CLOSE_BROWSER = "android.net.wifi.HS20_SEC_CLOSE_BROWSER";
    public static final String ACTION_SEC_LAUNCH_OPERATOR_URL = "android.net.wifi.HS20_SEC_LAUNCH_OPERTOR_URL";
    public static final String ACTION_SEC_LAUNCH_SP_URL = "android.net.wifi.HS20_SEC_LAUNCH_SP_URL";
    public static final int B64 = 6;
    private static final int BASE = 163840;
    public static final int BINARY = 4;
    public static final int BOOL = 2;
    public static final int CHAR = 1;
    public static final int DM_ADD_ACTION = 3;
    public static final int DM_DELETE_ACTION = 6;
    public static final int DM_END_ACTION = 2;
    public static final int DM_EXEC_ACTION = 7;
    public static final int DM_GET_ACTION = 5;
    public static final int DM_REPLACE_ACTION = 4;
    public static final int DM_START_ACTION = 1;
    public static final String EAP_TYPE_FOUND = "android.net.wifi.hs20.EAP_TYPE_FOUND";
    public static final String EXTRA_HS20_STATE = "hs20_state";
    public static final int HS20_ANQP_ALL = -1;
    public static final int HS20_ANQP_ALL_EX_OSU = 1;
    public static final int HS20_ANQP_ANONYMOUS_NAI = 256;
    public static final int HS20_ANQP_CAPABILITY = 2;
    public static final int HS20_ANQP_CONN_CAP = 16;
    public static final int HS20_ANQP_FRIENLDLY_NAME = 4;
    public static final int HS20_ANQP_ICON_REQUEST = 512;
    public static final int HS20_ANQP_NAI_HOME = 32;
    public static final int HS20_ANQP_OPERATING_CLASS = 64;
    public static final int HS20_ANQP_OSU_LIST = 128;
    public static final int HS20_ANQP_WAN_MATRIX = 8;
    public static final String HS20_DISABLED_COMPLETE_BY_CREDERROR_ACTION = "android.net.wifi.HS20_DISABLED_COMPLETE_BY_CREDERROR_ACTION";
    public static final String HS20_ICON_STORE_PATH = "/data/hs20/icons/";
    public static final String HS20_OSU_SELECTED = "android.net.wifi.HS20_OSU_SELECTED";
    public static final int HS20_OSU_STATE_UNKNOWN = 9999;
    public static final String HS20_PROVISIONIG_FRIENDLY_NAME = "friendly_name";
    public static final String HS20_PROVISIONIG_ICON_PATH = "icon_path";
    public static final String HS20_PROVISIONIG_STATE = "hs20_state";
    public static final String HS20_REMEDIATION_REQUEST_ACTION = "android.net.wifi.HS20_REMEDIATION_REQUEST";
    public static final String HS20_STATE_CHANGE_ACTION = "android.net.wifi.HS20_STATE_CHANGE";
    public static final int HS20_STATE_DISABLED = 0;
    public static final int HS20_STATE_ENABLED = 1;
    public static final int HS20_STATE_POLICY_UPDATE_FAILED = 9009;
    public static final int HS20_STATE_POLICY_UPDATE_STARTED = 9007;
    public static final int HS20_STATE_POLICY_UPDATE_SUCCESSFUL = 9008;
    public static final int HS20_STATE_REMEDIATION_FAILED = 9006;
    public static final int HS20_STATE_REMEDIATION_STARTED = 9004;
    public static final int HS20_STATE_REMEDIATION_SUCCESSFUL = 9005;
    public static final int HS20_STATE_SUBSCRIPTION_FAILED = 9003;
    public static final int HS20_STATE_SUBSCRIPTION_STARTED = 9001;
    public static final int HS20_STATE_SUBSCRIPTION_SUCCESSFUL = 9002;
    public static final int HS20_STATE_UNKNOWN = 2;
    public static final int HTTP_GET_CONTENT_TYPE_ERROR = 3;
    public static final int HTTP_GET_RESPONSE_ERROR = 5;
    public static final String INSTALL_CRED = "android.net.wifi.hs20.INSTALL_CRED";
    public static final String INSTALL_CRED_BROWSER = "android.net.wifi.hs20.INSTALL_CRED_BROWSER";
    public static final int INTEGER = 0;
    public static final int INVALID_CRED_ID = -1;
    public static final int NODE = 5;
    public static final int REQUEST_ANQP_INFO = 163841;
    public static final int REQUEST_ANQP_INFO_FAILED = 163842;
    public static final int REQUEST_ANQP_INFO_SUCCEEDED = 163843;
    public static final int REQUEST_OSU_ICON = 163844;
    public static final int REQUEST_OSU_ICON_FAILED = 163845;
    public static final int REQUEST_OSU_ICON_SUCCEEDED = 163846;
    public static final int SEC_COMMAND_CLEAR_HS20 = 1001;
    public static final int SEC_COMMAND_ID_HS20_FETCH_OSU = 1000;
    public static final int SEC_COMMAND_ID_RELOAD_SIM_STATE = 195;
    public static final int START_OSU = 163847;
    public static final int START_OSU_BROWSER = 163848;
    public static final int START_OSU_FAILED = 163849;
    public static final int START_OSU_SUCCEEDED = 163850;
    private static final String TAG = "Hs20Manager";
    public static final String TEST_CLEAR_DB = "android.net.wifi.hs20.TEST_CLEAR_DB";
    public static final String TEST_CONNECT_WITH_STATIC_IP = "android.net.wifi.hs20.TEST_CONNECT_WITH_STATIC_IP";
    public static final String TEST_GEN_CRED_CONF_FILE = "android.net.wifi.hs20.TEST_GEN_CRED_CONF_FILE";
    public static final String TEST_START_OSU_PROCESS = "android.net.wifi.hs20.TEST_START_OSU_PROCESS";
    public static final String TEST_START_OSU_PROCESS_NOSIGMA = "android.net.wifi.hs20.TEST_START_OSU_PROCESS_NOSIGMA";
    public static final String TEST_TRIGGER_INSTALL_FILE = "android.net.wifi.hs20.TEST_TRIGGER_INSTALL_FILE";
    public static final String TEST_TRIGGER_POLICY_UPDATE = "android.net.wifi.hs20.TEST_TRIGGER_POLICY_UPDATE";
    public static final int UNKNOWN_TYPE = -1;
    private Context mContext;
    IWifiHs20Manager mService;

    public enum AccessProtocol {
        OMADM,
        SOAP,
        OMADM_SOAP,
        SOAP_OMADM,
        NONE
    }

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
                        Log.d(WifiHs20Manager.TAG, "Ignored " + message);
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
            Log.d(WifiHs20Manager.TAG, "peekListener() key=" + key);
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
            Log.d(WifiHs20Manager.TAG, "getListener() key=" + key + " force=" + forceRemove);
            if (key == 0) {
                return null;
            }
            synchronized (this.mListenerMapLock) {
                if (!forceRemove) {
                    int count = ((Integer) this.mListenerMapCount.get(Integer.valueOf(key))).intValue();
                    Log.d(WifiHs20Manager.TAG, "count=" + count);
                    count--;
                    this.mListenerMapCount.put(Integer.valueOf(key), Integer.valueOf(count));
                    if (count > 0) {
                        return null;
                    }
                }
                Log.d(WifiHs20Manager.TAG, "remove key");
                this.mListenerMapCount.remove(Integer.valueOf(key));
                Object remove = this.mListenerMap.remove(Integer.valueOf(key));
                return remove;
            }
        }

        private void anqpRequestStart(ScanResult sr) {
            Log.d(WifiHs20Manager.TAG, "anqpRequestStart sr.bssid=" + sr.BSSID);
            synchronized (this.mAnqpRequestLock) {
                this.mAnqpRequest.add(sr);
            }
        }

        private void anqpRequestFinish(ScanResult sr) {
            Log.d(WifiHs20Manager.TAG, "anqpRequestFinish sr.bssid=" + sr.BSSID);
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

    public enum CredentialType {
        TTLS,
        TLS,
        SIM,
        AKA
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

    public WifiHs20Manager(Context context, IWifiHs20Manager service) {
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
            e.printStackTrace();
            return null;
        }
    }

    public void startOSUProcess(WifiHs20OsuProvider selectedOsuProvider) {
        try {
            this.mService.startOSUProcess(selectedOsuProvider);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isSubscrptionInProgress() {
        try {
            return this.mService.isSubscrptionInProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cancelSubscription() {
        try {
            this.mService.cancelSubscription();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startPolicyUpdate(String fqdn) {
        try {
            this.mService.startPolicyUpdate(fqdn);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<WifiHs20SubscribedSp> getSubscribedSpList() {
        try {
            return this.mService.getSubscribedSpList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean updateCredPriorities(Bundle args) {
        try {
            return this.mService.updateCredPriorities(args);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean enableSubscription(int hsId, boolean enable) {
        try {
            return this.mService.enableSubscription(hsId, enable);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean deleteSubscription(int hsId) {
        try {
            return this.mService.deleteSubscription(hsId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean startSession() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 1);
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getDMData(WifiHs20DMData data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 5);
            bundle.putString("Uri", data.uri);
            Bundle dmData = this.mService.getDMData(bundle);
            if (dmData == null) {
                return false;
            }
            int format = dmData.getInt("Format");
            Log.d(TAG, "getDMData: format" + format + "Length of the data" + dmData.getInt("dataLen"));
            if (format == 0) {
                Log.d(TAG, "getDMData: intData" + dmData.getLong("data"));
                data.intData = dmData.getLong("data");
                data.fmt = format;
                Log.d(TAG, "getDMData: intData11: " + data.intData);
            } else if (format == 2) {
                data.booleanData = dmData.getBoolean("data");
                Log.d(TAG, "getDMData: booleanData" + data.booleanData);
                data.fmt = format;
            } else if (format == 1) {
                Log.d(TAG, "getDMData: charData" + dmData.getString("data"));
                data.charData = dmData.getString("data");
                Log.d(TAG, "getDMData: charData11: " + data.charData);
                data.charDataLen = dmData.getInt("dataLen");
                data.fmt = format;
            } else if (format == 4) {
                Log.d(TAG, "getDMData: byteData" + dmData.getByteArray("data"));
                data.byteData = dmData.getByteArray("data");
                Log.d(TAG, "getDMData: byteData11: " + data.byteData);
                data.byteDataLen = dmData.getInt("dataLen");
                data.fmt = format;
            } else if (format == 5) {
                data.charData = dmData.getString("data");
                data.fmt = format;
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean addDMData(WifiHs20DMData data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 3);
            bundle.putString("Uri", data.uri);
            bundle.putInt("Format", data.fmt);
            int format = data.fmt;
            if (format == 0) {
                bundle.putLong("data", data.intData);
            } else if (format == 2) {
                bundle.putBoolean("data", data.booleanData);
            } else if (format == 1) {
                bundle.putString("data", data.charData);
                bundle.putInt("dataLen", data.charDataLen);
            } else if (format == 4) {
                bundle.putByteArray("data", data.byteData);
                bundle.putInt("dataLen", data.byteDataLen);
            }
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean replaceDMData(WifiHs20DMData data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 4);
            bundle.putString("Uri", data.uri);
            bundle.putInt("Format", data.fmt);
            int format = data.fmt;
            if (format == 0) {
                bundle.putLong("data", data.intData);
            } else if (format == 2) {
                bundle.putBoolean("data", data.booleanData);
            } else if (format == 1) {
                bundle.putString("data", data.charData);
                bundle.putInt("dataLen", data.charDataLen);
            } else if (format == 4) {
                bundle.putByteArray("data", data.byteData);
                bundle.putInt("dataLen", data.byteDataLen);
            }
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean deleteDMData(WifiHs20DMData data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 6);
            bundle.putString("Uri", data.uri);
            bundle.putInt("Format", data.fmt);
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean exec(WifiHs20DMData param) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 7);
            bundle.putString("Command", param.command);
            bundle.putString("execData", param.execData);
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean endSession(int status) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Action", 2);
            bundle.putInt("status", status);
            return this.mService.handleDMRequest(bundle);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getFormatType(String uri) {
        try {
            return this.mService.getFormatType(uri);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public WifiHs20DMData getCredentials(int credentialType, int credentialID, String subscriptionType) {
        WifiHs20DMData dmData = new WifiHs20DMData();
        try {
            Bundle credentialDetails = this.mService.getCredentials(credentialType, credentialID, subscriptionType);
            if (credentialDetails == null) {
                return null;
            }
            dmData.username = credentialDetails.getString("Username");
            dmData.password = credentialDetails.getByteArray("Password");
            return dmData;
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getCertID(int id) {
        try {
            return this.mService.getCertID(id);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public byte[] getKeyPass() {
        try {
            return this.mService.getKeyPass();
        } catch (RemoteException e) {
            return null;
        }
    }

    public int modifyPasspointCred(String credInfo) {
        try {
            Log.d("modifyPasspointCred", "Load the credentials");
            return this.mService.modifyPasspointCred(credInfo);
        } catch (RemoteException e) {
            return -1;
        }
    }
}
