package android.net.wifi.p2p;

import android.content.Context;
import android.media.tv.TvContract;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceResponse;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiP2pManager {
    public static final int ADD_LOCAL_SERVICE = 139292;
    public static final int ADD_LOCAL_SERVICE_FAILED = 139293;
    public static final int ADD_LOCAL_SERVICE_SUCCEEDED = 139294;
    public static final int ADD_SERVICE_REQUEST = 139301;
    public static final int ADD_SERVICE_REQUEST_FAILED = 139302;
    public static final int ADD_SERVICE_REQUEST_SUCCEEDED = 139303;
    public static final String APP_PKG_BUNDLE_KEY = "appPkgName";
    private static final int BASE = 139264;
    public static final int BUSY = 2;
    public static final int CANCEL_CONNECT = 139274;
    public static final int CANCEL_CONNECT_FAILED = 139275;
    public static final int CANCEL_CONNECT_SUCCEEDED = 139276;
    public static final int CLEAR_LOCAL_SERVICES = 139298;
    public static final int CLEAR_LOCAL_SERVICES_FAILED = 139299;
    public static final int CLEAR_LOCAL_SERVICES_SUCCEEDED = 139300;
    public static final int CLEAR_SERVICE_REQUESTS = 139307;
    public static final int CLEAR_SERVICE_REQUESTS_FAILED = 139308;
    public static final int CLEAR_SERVICE_REQUESTS_SUCCEEDED = 139309;
    public static final int CONNECT = 139271;
    public static final int CONNECTION_REQUESTED = 139383;
    public static final int CONNECT_FAILED = 139272;
    public static final int CONNECT_INVITE_LIST = 139374;
    public static final int CONNECT_PERSISTENT = 139377;
    public static final int CONNECT_SUCCEEDED = 139273;
    public static final int CREATE_GROUP = 139277;
    public static final int CREATE_GROUP_FAILED = 139278;
    public static final int CREATE_GROUP_SUCCEEDED = 139279;
    private static final boolean DBG;
    public static final int DELETE_PERSISTENT_GROUP = 139318;
    public static final int DELETE_PERSISTENT_GROUP_FAILED = 139319;
    public static final int DELETE_PERSISTENT_GROUP_SUCCEEDED = 139320;
    public static final int DIALOG_LISTENER_ATTACHED = 139382;
    public static final int DIALOG_LISTENER_DETACHED = 139381;
    public static final int DISABLE_P2P = 139368;
    public static final int DISABLE_P2P_SUCCEEDED = 139369;
    public static final int DISCOVER_PEERS = 139265;
    public static final int DISCOVER_PEERS_FAILED = 139266;
    public static final int DISCOVER_PEERS_SUCCEEDED = 139267;
    public static final int DISCOVER_PEERS_WITH_FLUSH = 139371;
    public static final int DISCOVER_SERVICES = 139310;
    public static final int DISCOVER_SERVICES_FAILED = 139311;
    public static final int DISCOVER_SERVICES_SUCCEEDED = 139312;
    public static final int ENABLE_P2P = 139365;
    public static final int ENABLE_P2P_SUCCEEDED = 139367;
    public static final int ERROR = 0;
    public static final String EXTRA_CONNECTED_DEV_ADDR = "connectedDevAddress";
    public static final String EXTRA_CONNECTED_DEV_INTF_ADDR = "connectedDevIntfAddress";
    public static final String EXTRA_DISCOVERY_STATE = "discoveryState";
    public static final String EXTRA_HANDOVER_MESSAGE = "android.net.wifi.p2p.EXTRA_HANDOVER_MESSAGE";
    public static final String EXTRA_LINK_CAPABILITIES = "linkCapabilities";
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NFC_TRIGGERED = "wifiP2pNfcTriggered";
    public static final String EXTRA_P2P_DEVICE_LIST = "wifiP2pDeviceList";
    public static final String EXTRA_REQUEST_STATE = "requestState";
    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";
    public static final String EXTRA_WIFI_P2P_GROUP = "p2pGroupInfo";
    public static final String EXTRA_WIFI_P2P_GROUP_INFO = "p2pGroupInfo";
    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";
    public static final int GET_HANDOVER_REQUEST = 139339;
    public static final int GET_HANDOVER_SELECT = 139340;
    public static final int INITIATOR_REPORT_NFC_HANDOVER = 139342;
    public static final int MAX_CLIENT_SUPPORT = Integer.parseInt("4");
    public static final int MIRACAST_DISABLED = 0;
    public static final int MIRACAST_SINK = 2;
    public static final int MIRACAST_SOURCE = 1;
    public static final int NFC_TAG_INITIATED = 139404;
    public static final int NOT_IN_FOREGROUND = 4;
    public static final int NO_SERVICE_REQUESTS = 3;
    public static final String P2P_CONFIG_BUNDLE_KEY = "wifiP2pConfig";
    public static final String P2P_DEV_BUNDLE_KEY = "wifiP2pDevice";
    public static final int P2P_LISTEN = 139372;
    public static final int P2P_UNSUPPORTED = 1;
    public static final int PING = 139313;
    public static final int REMOVE_GROUP = 139280;
    public static final int REMOVE_GROUP_FAILED = 139281;
    public static final int REMOVE_GROUP_SUCCEEDED = 139282;
    public static final int REMOVE_LOCAL_SERVICE = 139295;
    public static final int REMOVE_LOCAL_SERVICE_FAILED = 139296;
    public static final int REMOVE_LOCAL_SERVICE_SUCCEEDED = 139297;
    public static final int REMOVE_P2P_CLIENT = 139408;
    public static final int REMOVE_P2P_CLIENT_FAILED = 139409;
    public static final int REMOVE_P2P_CLIENT_SUCCEEDED = 139410;
    public static final int REMOVE_SERVICE_REQUEST = 139304;
    public static final int REMOVE_SERVICE_REQUEST_FAILED = 139305;
    public static final int REMOVE_SERVICE_REQUEST_SUCCEEDED = 139306;
    public static final int REPORT_NFC_HANDOVER_FAILED = 139345;
    public static final int REPORT_NFC_HANDOVER_SUCCEEDED = 139344;
    public static final int REQUEST_CONFIG_LIST = 139378;
    public static final int REQUEST_CONNECTION_INFO = 139285;
    public static final int REQUEST_GROUP_INFO = 139287;
    public static final int REQUEST_NFC_CONNECT = 139376;
    public static final int REQUEST_PEERS = 139283;
    public static final int REQUEST_PERSISTENT_GROUP_INFO = 139321;
    public static final String RESET_DIALOG_LISTENER_BUNDLE_KEY = "dialogResetFlag";
    public static final int RESPONDER_REPORT_NFC_HANDOVER = 139343;
    public static final int RESPONSE_CONFIG_LIST = 139379;
    public static final int RESPONSE_CONNECTION_INFO = 139286;
    public static final int RESPONSE_GET_HANDOVER_MESSAGE = 139341;
    public static final int RESPONSE_GROUP_INFO = 139288;
    public static final int RESPONSE_PEERS = 139284;
    public static final int RESPONSE_PERSISTENT_GROUP_INFO = 139322;
    public static final int RESPONSE_SERVICE = 139314;
    public static final int SEC_COMMAND_ID_ADD_HISTORICAL_DUMPLOG = 139412;
    public static final int SEC_COMMAND_ID_P2P_STOP_DISCOVERY_NO_FLUSH = 139407;
    public static final int SEC_COMMAND_ID_REJECT_CONNECT = 139394;
    public static final int SEC_COMMAND_ID_SECONNECT_PREQ = 139405;
    public static final int SEC_COMMAND_ID_SECONNECT_PRESP = 139406;
    public static final int SEC_COMMAND_ID_SET_MIRACAST = 139411;
    public static final int SET_CHANNEL = 139335;
    public static final int SET_CHANNEL_FAILED = 139336;
    public static final int SET_CHANNEL_SUCCEEDED = 139337;
    public static final int SET_DEVICE_NAME = 139315;
    public static final int SET_DEVICE_NAME_FAILED = 139316;
    public static final int SET_DEVICE_NAME_SUCCEEDED = 139317;
    public static final int SET_DIALOG_LISTENER = 139380;
    public static final int SET_LISTEN_OFFLOADING = 139415;
    public static final int SET_LISTEN_OFFLOADING_TIMER = 139414;
    public static final int SET_P2P_TIMER = 139373;
    public static final int SET_STOPFIND_TIMER = 139375;
    public static final int SET_WFD_INFO = 139323;
    public static final int SET_WFD_INFO_FAILED = 139324;
    public static final int SET_WFD_INFO_SUCCEEDED = 139325;
    public static final int SHOW_PIN_REQUESTED = 139384;
    public static final int START_LISTEN = 139329;
    public static final int START_LISTEN_FAILED = 139330;
    public static final int START_LISTEN_SUCCEEDED = 139331;
    public static final int START_WPS = 139326;
    public static final int START_WPS_FAILED = 139327;
    public static final int START_WPS_SUCCEEDED = 139328;
    public static final int STOP_DISCOVERY = 139268;
    public static final int STOP_DISCOVERY_FAILED = 139269;
    public static final int STOP_DISCOVERY_SUCCEEDED = 139270;
    public static final int STOP_LISTEN = 139332;
    public static final int STOP_LISTEN_FAILED = 139333;
    public static final int STOP_LISTEN_SUCCEEDED = 139334;
    private static final String TAG = "WifiP2pManager";
    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION = "android.net.wifi.p2p.CONNECTION_STATE_CHANGE";
    public static final String WIFI_P2P_DISCOVERY_CHANGED_ACTION = "android.net.wifi.p2p.DISCOVERY_STATE_CHANGE";
    public static final int WIFI_P2P_DISCOVERY_STARTED = 2;
    public static final int WIFI_P2P_DISCOVERY_STOPPED = 1;
    public static final String WIFI_P2P_PEERS_CHANGED_ACTION = "android.net.wifi.p2p.PEERS_CHANGED";
    public static final String WIFI_P2P_PERSISTENT_GROUPS_CHANGED_ACTION = "android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED";
    public static final String WIFI_P2P_REQUEST_CHANGED_ACTION = "android.net.wifi.p2p.REQUEST_STATE_CHANGE";
    public static final String WIFI_P2P_STATE_CHANGED_ACTION = "android.net.wifi.p2p.STATE_CHANGED";
    public static final int WIFI_P2P_STATE_CONNECTED = 3;
    public static final int WIFI_P2P_STATE_DISABLED = 1;
    public static final int WIFI_P2P_STATE_ENABLED = 2;
    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION = "android.net.wifi.p2p.THIS_DEVICE_CHANGED";
    public static final String WPS_PIN_BUNDLE_KEY = "wpsPin";
    IWifiP2pManager mService;

    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    public static class Channel {
        private static final int INVALID_LISTENER_KEY = 0;
        private AsyncChannel mAsyncChannel = new AsyncChannel();
        private ChannelListener mChannelListener;
        Context mContext;
        private DialogListener mDialogListener;
        private DnsSdServiceResponseListener mDnsSdServRspListener;
        private DnsSdTxtRecordListener mDnsSdTxtListener;
        private P2pHandler mHandler;
        private int mListenerKey = 0;
        private HashMap<Integer, Object> mListenerMap = new HashMap();
        private Object mListenerMapLock = new Object();
        private ServiceResponseListener mServRspListener;
        private UpnpServiceResponseListener mUpnpServRspListener;

        class P2pHandler extends Handler {
            P2pHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message message) {
                String handoverMessage = null;
                Object listener = Channel.this.getListener(message.arg2);
                switch (message.what) {
                    case 69636:
                        if (Channel.this.mChannelListener != null) {
                            Channel.this.mChannelListener.onChannelDisconnected();
                            Channel.this.mChannelListener = null;
                            return;
                        }
                        return;
                    case WifiP2pManager.DISCOVER_PEERS_FAILED /*139266*/:
                    case WifiP2pManager.STOP_DISCOVERY_FAILED /*139269*/:
                    case WifiP2pManager.CONNECT_FAILED /*139272*/:
                    case WifiP2pManager.CANCEL_CONNECT_FAILED /*139275*/:
                    case WifiP2pManager.CREATE_GROUP_FAILED /*139278*/:
                    case WifiP2pManager.REMOVE_GROUP_FAILED /*139281*/:
                    case WifiP2pManager.ADD_LOCAL_SERVICE_FAILED /*139293*/:
                    case WifiP2pManager.REMOVE_LOCAL_SERVICE_FAILED /*139296*/:
                    case WifiP2pManager.CLEAR_LOCAL_SERVICES_FAILED /*139299*/:
                    case WifiP2pManager.ADD_SERVICE_REQUEST_FAILED /*139302*/:
                    case WifiP2pManager.REMOVE_SERVICE_REQUEST_FAILED /*139305*/:
                    case WifiP2pManager.CLEAR_SERVICE_REQUESTS_FAILED /*139308*/:
                    case WifiP2pManager.DISCOVER_SERVICES_FAILED /*139311*/:
                    case WifiP2pManager.SET_DEVICE_NAME_FAILED /*139316*/:
                    case WifiP2pManager.DELETE_PERSISTENT_GROUP_FAILED /*139319*/:
                    case WifiP2pManager.SET_WFD_INFO_FAILED /*139324*/:
                    case WifiP2pManager.START_WPS_FAILED /*139327*/:
                    case WifiP2pManager.START_LISTEN_FAILED /*139330*/:
                    case WifiP2pManager.STOP_LISTEN_FAILED /*139333*/:
                    case WifiP2pManager.SET_CHANNEL_FAILED /*139336*/:
                    case WifiP2pManager.REPORT_NFC_HANDOVER_FAILED /*139345*/:
                    case WifiP2pManager.REMOVE_P2P_CLIENT_FAILED /*139409*/:
                        if (listener != null) {
                            ((ActionListener) listener).onFailure(message.arg1);
                            return;
                        }
                        return;
                    case WifiP2pManager.DISCOVER_PEERS_SUCCEEDED /*139267*/:
                    case WifiP2pManager.STOP_DISCOVERY_SUCCEEDED /*139270*/:
                    case WifiP2pManager.CONNECT_SUCCEEDED /*139273*/:
                    case WifiP2pManager.CANCEL_CONNECT_SUCCEEDED /*139276*/:
                    case WifiP2pManager.CREATE_GROUP_SUCCEEDED /*139279*/:
                    case WifiP2pManager.REMOVE_GROUP_SUCCEEDED /*139282*/:
                    case WifiP2pManager.ADD_LOCAL_SERVICE_SUCCEEDED /*139294*/:
                    case WifiP2pManager.REMOVE_LOCAL_SERVICE_SUCCEEDED /*139297*/:
                    case WifiP2pManager.CLEAR_LOCAL_SERVICES_SUCCEEDED /*139300*/:
                    case WifiP2pManager.ADD_SERVICE_REQUEST_SUCCEEDED /*139303*/:
                    case WifiP2pManager.REMOVE_SERVICE_REQUEST_SUCCEEDED /*139306*/:
                    case WifiP2pManager.CLEAR_SERVICE_REQUESTS_SUCCEEDED /*139309*/:
                    case WifiP2pManager.DISCOVER_SERVICES_SUCCEEDED /*139312*/:
                    case WifiP2pManager.SET_DEVICE_NAME_SUCCEEDED /*139317*/:
                    case WifiP2pManager.DELETE_PERSISTENT_GROUP_SUCCEEDED /*139320*/:
                    case WifiP2pManager.SET_WFD_INFO_SUCCEEDED /*139325*/:
                    case WifiP2pManager.START_WPS_SUCCEEDED /*139328*/:
                    case WifiP2pManager.START_LISTEN_SUCCEEDED /*139331*/:
                    case WifiP2pManager.STOP_LISTEN_SUCCEEDED /*139334*/:
                    case WifiP2pManager.SET_CHANNEL_SUCCEEDED /*139337*/:
                    case WifiP2pManager.REPORT_NFC_HANDOVER_SUCCEEDED /*139344*/:
                    case WifiP2pManager.REMOVE_P2P_CLIENT_SUCCEEDED /*139410*/:
                        if (listener != null) {
                            ((ActionListener) listener).onSuccess();
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_PEERS /*139284*/:
                        WifiP2pDeviceList peers = message.obj;
                        if (listener != null) {
                            ((PeerListListener) listener).onPeersAvailable(peers);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_CONNECTION_INFO /*139286*/:
                        WifiP2pInfo wifiP2pInfo = message.obj;
                        if (listener != null) {
                            ((ConnectionInfoListener) listener).onConnectionInfoAvailable(wifiP2pInfo);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_GROUP_INFO /*139288*/:
                        WifiP2pGroup group = message.obj;
                        if (listener != null) {
                            ((GroupInfoListener) listener).onGroupInfoAvailable(group);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_SERVICE /*139314*/:
                        Channel.this.handleServiceResponse(message.obj);
                        return;
                    case WifiP2pManager.RESPONSE_PERSISTENT_GROUP_INFO /*139322*/:
                        WifiP2pGroupList groups = message.obj;
                        if (listener != null) {
                            ((PersistentGroupInfoListener) listener).onPersistentGroupInfoAvailable(groups);
                            return;
                        }
                        return;
                    case WifiP2pManager.RESPONSE_GET_HANDOVER_MESSAGE /*139341*/:
                        Bundle handoverBundle = message.obj;
                        if (listener != null) {
                            if (handoverBundle != null) {
                                handoverMessage = handoverBundle.getString(WifiP2pManager.EXTRA_HANDOVER_MESSAGE);
                            }
                            ((HandoverMessageListener) listener).onHandoverMessageAvailable(handoverMessage);
                            return;
                        }
                        return;
                    case 139379:
                        if (message.obj != null) {
                            WifiP2pConfigList configList = message.obj;
                            if (listener != null) {
                                ((ConfigListListener) listener).onConfigListAvailable(configList);
                                return;
                            }
                            return;
                        } else if (listener != null) {
                            ((ConfigListListener) listener).onConfigListUnavailable();
                            return;
                        } else {
                            return;
                        }
                    case 139381:
                        if (Channel.this.mDialogListener != null) {
                            Channel.this.mDialogListener.onDetached(message.arg1);
                            Channel.this.mDialogListener = null;
                            return;
                        }
                        return;
                    case WifiP2pManager.DIALOG_LISTENER_ATTACHED /*139382*/:
                        if (Channel.this.mDialogListener != null) {
                            Channel.this.mDialogListener.onAttached();
                            return;
                        }
                        return;
                    case WifiP2pManager.CONNECTION_REQUESTED /*139383*/:
                        if (Channel.this.mDialogListener != null) {
                            Bundle bundle = message.getData();
                            Channel.this.mDialogListener.onConnectionRequested((WifiP2pDevice) bundle.getParcelable("wifiP2pDevice"), (WifiP2pConfig) bundle.getParcelable(WifiP2pManager.P2P_CONFIG_BUNDLE_KEY));
                            return;
                        }
                        return;
                    case 139384:
                        if (Channel.this.mDialogListener != null) {
                            Channel.this.mDialogListener.onShowPinRequested(message.getData().getString(WifiP2pManager.WPS_PIN_BUNDLE_KEY));
                            return;
                        }
                        return;
                    default:
                        Log.d(WifiP2pManager.TAG, "Ignored " + message);
                        return;
                }
            }
        }

        Channel(Context context, Looper looper, ChannelListener l) {
            this.mHandler = new P2pHandler(looper);
            this.mChannelListener = l;
            this.mContext = context;
        }

        private void handleServiceResponse(WifiP2pServiceResponse resp) {
            if (resp instanceof WifiP2pDnsSdServiceResponse) {
                handleDnsSdServiceResponse((WifiP2pDnsSdServiceResponse) resp);
            } else if (resp instanceof WifiP2pUpnpServiceResponse) {
                if (this.mUpnpServRspListener != null) {
                    handleUpnpServiceResponse((WifiP2pUpnpServiceResponse) resp);
                }
            } else if (this.mServRspListener != null) {
                this.mServRspListener.onServiceAvailable(resp.getServiceType(), resp.getRawData(), resp.getSrcDevice());
            }
        }

        private void handleUpnpServiceResponse(WifiP2pUpnpServiceResponse resp) {
            this.mUpnpServRspListener.onUpnpServiceAvailable(resp.getUniqueServiceNames(), resp.getSrcDevice());
        }

        private void handleDnsSdServiceResponse(WifiP2pDnsSdServiceResponse resp) {
            if (resp.getDnsType() == 12) {
                if (this.mDnsSdServRspListener != null) {
                    this.mDnsSdServRspListener.onDnsSdServiceAvailable(resp.getInstanceName(), resp.getDnsQueryName(), resp.getSrcDevice());
                }
            } else if (resp.getDnsType() != 16) {
                Log.e(WifiP2pManager.TAG, "Unhandled resp " + resp);
            } else if (this.mDnsSdTxtListener != null) {
                this.mDnsSdTxtListener.onDnsSdTxtRecordAvailable(resp.getDnsQueryName(), resp.getTxtRecord(), resp.getSrcDevice());
            }
        }

        private int putListener(Object listener) {
            if (listener == null) {
                return 0;
            }
            int key;
            synchronized (this.mListenerMapLock) {
                do {
                    key = this.mListenerKey;
                    this.mListenerKey = key + 1;
                } while (key == 0);
                this.mListenerMap.put(Integer.valueOf(key), listener);
            }
            return key;
        }

        private Object getListener(int key) {
            if (key == 0) {
                return null;
            }
            Object remove;
            synchronized (this.mListenerMapLock) {
                remove = this.mListenerMap.remove(Integer.valueOf(key));
            }
            return remove;
        }

        private void setDialogListener(DialogListener listener) {
            this.mDialogListener = listener;
        }
    }

    public interface ChannelListener {
        void onChannelDisconnected();
    }

    public interface ConfigListListener {
        void onConfigListAvailable(WifiP2pConfigList wifiP2pConfigList);

        void onConfigListUnavailable();
    }

    public interface ConnectionInfoListener {
        void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo);
    }

    public interface DialogListener {
        void onAttached();

        void onConnectionRequested(WifiP2pDevice wifiP2pDevice, WifiP2pConfig wifiP2pConfig);

        void onDetached(int i);

        void onShowPinRequested(String str);
    }

    public interface DnsSdServiceResponseListener {
        void onDnsSdServiceAvailable(String str, String str2, WifiP2pDevice wifiP2pDevice);
    }

    public interface DnsSdTxtRecordListener {
        void onDnsSdTxtRecordAvailable(String str, Map<String, String> map, WifiP2pDevice wifiP2pDevice);
    }

    public interface GroupInfoListener {
        void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup);
    }

    public interface HandoverMessageListener {
        void onHandoverMessageAvailable(String str);
    }

    public interface PeerListListener {
        void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList);
    }

    public interface PersistentGroupInfoListener {
        void onPersistentGroupInfoAvailable(WifiP2pGroupList wifiP2pGroupList);
    }

    public interface ServiceResponseListener {
        void onServiceAvailable(int i, byte[] bArr, WifiP2pDevice wifiP2pDevice);
    }

    public interface UpnpServiceResponseListener {
        void onUpnpServiceAvailable(List<String> list, WifiP2pDevice wifiP2pDevice);
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public WifiP2pManager(IWifiP2pManager service) {
        this.mService = service;
    }

    private static void checkChannel(Channel c) {
        if (c == null) {
            throw new IllegalArgumentException("Channel needs to be initialized");
        }
    }

    private static void checkServiceInfo(WifiP2pServiceInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("service info is null");
        }
    }

    private static void checkServiceRequest(WifiP2pServiceRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("service request is null");
        }
    }

    private static void checkP2pConfig(WifiP2pConfig c) {
        if (c == null) {
            throw new IllegalArgumentException("config cannot be null");
        } else if (TextUtils.isEmpty(c.deviceAddress)) {
            throw new IllegalArgumentException("deviceAddress cannot be empty");
        }
    }

    private static void sendHistoricalDumplog(Channel c, String f) {
        if (c == null) {
            throw new IllegalArgumentException("Channel needs to be initialized");
        }
        String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        Message msg = new Message();
        msg.what = SEC_COMMAND_ID_ADD_HISTORICAL_DUMPLOG;
        Bundle args = new Bundle();
        args.putString("extra_log", new String("WifiP2pManager." + f + " called by ---> " + c.mContext.getPackageName() + " @ " + currentTimeStamp + "\n"));
        msg.obj = args;
        c.mAsyncChannel.sendMessage(msg);
    }

    public Channel initialize(Context srcContext, Looper srcLooper, ChannelListener listener) {
        return initalizeChannel(srcContext, srcLooper, listener, getMessenger());
    }

    public Channel initializeInternal(Context srcContext, Looper srcLooper, ChannelListener listener) {
        return initalizeChannel(srcContext, srcLooper, listener, getP2pStateMachineMessenger());
    }

    private Channel initalizeChannel(Context srcContext, Looper srcLooper, ChannelListener listener, Messenger messenger) {
        if (messenger == null) {
            return null;
        }
        Channel c = new Channel(srcContext, srcLooper, listener);
        if (c.mAsyncChannel.connectSync(srcContext, c.mHandler, messenger) != 0) {
            return null;
        }
        return c;
    }

    public void discoverPeers(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISCOVER_PEERS, 0, c.putListener(listener));
        sendHistoricalDumplog(c, "discoverPeers()");
    }

    public void discoverPeers(Channel c, int channelNum, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISCOVER_PEERS, channelNum, c.putListener(listener));
        sendHistoricalDumplog(c, "discoverPeers(" + channelNum + ")");
    }

    public void stopPeerDiscovery(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(STOP_DISCOVERY, 0, c.putListener(listener));
    }

    public void connect(Channel c, WifiP2pConfig config, ActionListener listener) {
        checkChannel(c);
        checkP2pConfig(config);
        c.mAsyncChannel.sendMessage(CONNECT, 0, c.putListener(listener), config);
    }

    public void cancelConnect(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CANCEL_CONNECT, 0, c.putListener(listener));
    }

    public void createGroup(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CREATE_GROUP, -2, c.putListener(listener));
    }

    public void removeGroup(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(REMOVE_GROUP, 0, c.putListener(listener));
        if (DBG) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
    }

    public void removeClient(Channel c, WifiP2pConfig config, ActionListener listener) {
        checkChannel(c);
        checkP2pConfig(config);
        c.mAsyncChannel.sendMessage(REMOVE_P2P_CLIENT, 0, c.putListener(listener), config);
        if (DBG) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
    }

    public void listen(Channel c, boolean enable, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(enable ? START_LISTEN : STOP_LISTEN, 0, c.putListener(listener));
    }

    public void setWifiP2pChannels(Channel c, int lc, int oc, ActionListener listener) {
        checkChannel(c);
        Bundle p2pChannels = new Bundle();
        p2pChannels.putInt("lc", lc);
        p2pChannels.putInt("oc", oc);
        c.mAsyncChannel.sendMessage(SET_CHANNEL, 0, c.putListener(listener), p2pChannels);
    }

    public void startWps(Channel c, WpsInfo wps, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(START_WPS, 0, c.putListener(listener), wps);
    }

    public void addLocalService(Channel c, WifiP2pServiceInfo servInfo, ActionListener listener) {
        checkChannel(c);
        checkServiceInfo(servInfo);
        c.mAsyncChannel.sendMessage(ADD_LOCAL_SERVICE, 0, c.putListener(listener), servInfo);
    }

    public void removeLocalService(Channel c, WifiP2pServiceInfo servInfo, ActionListener listener) {
        checkChannel(c);
        checkServiceInfo(servInfo);
        c.mAsyncChannel.sendMessage(REMOVE_LOCAL_SERVICE, 0, c.putListener(listener), servInfo);
    }

    public void clearLocalServices(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CLEAR_LOCAL_SERVICES, 0, c.putListener(listener));
    }

    public void setServiceResponseListener(Channel c, ServiceResponseListener listener) {
        checkChannel(c);
        c.mServRspListener = listener;
    }

    public void setDnsSdResponseListeners(Channel c, DnsSdServiceResponseListener servListener, DnsSdTxtRecordListener txtListener) {
        checkChannel(c);
        c.mDnsSdServRspListener = servListener;
        c.mDnsSdTxtListener = txtListener;
    }

    public void setUpnpServiceResponseListener(Channel c, UpnpServiceResponseListener listener) {
        checkChannel(c);
        c.mUpnpServRspListener = listener;
    }

    public void discoverServices(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISCOVER_SERVICES, 0, c.putListener(listener));
        sendHistoricalDumplog(c, "discoverServices()");
    }

    public void discoverServices(Channel c, int channelNum, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISCOVER_SERVICES, channelNum, c.putListener(listener));
        sendHistoricalDumplog(c, "discoverServices(" + channelNum + ")");
    }

    public void addServiceRequest(Channel c, WifiP2pServiceRequest req, ActionListener listener) {
        checkChannel(c);
        checkServiceRequest(req);
        c.mAsyncChannel.sendMessage(ADD_SERVICE_REQUEST, 0, c.putListener(listener), req);
    }

    public void removeServiceRequest(Channel c, WifiP2pServiceRequest req, ActionListener listener) {
        checkChannel(c);
        checkServiceRequest(req);
        c.mAsyncChannel.sendMessage(REMOVE_SERVICE_REQUEST, 0, c.putListener(listener), req);
    }

    public void clearServiceRequests(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CLEAR_SERVICE_REQUESTS, 0, c.putListener(listener));
    }

    public void requestPeers(Channel c, PeerListListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(REQUEST_PEERS, 0, c.putListener(listener));
    }

    public void requestConnectionInfo(Channel c, ConnectionInfoListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(REQUEST_CONNECTION_INFO, 0, c.putListener(listener));
    }

    public void requestGroupInfo(Channel c, GroupInfoListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(REQUEST_GROUP_INFO, 0, c.putListener(listener));
    }

    public void setDeviceName(Channel c, String devName, ActionListener listener) {
        checkChannel(c);
        WifiP2pDevice d = new WifiP2pDevice();
        d.deviceName = devName;
        c.mAsyncChannel.sendMessage(SET_DEVICE_NAME, 0, c.putListener(listener), d);
    }

    public void setWFDInfo(Channel c, WifiP2pWfdInfo wfdInfo, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(SET_WFD_INFO, 0, c.putListener(listener), wfdInfo);
    }

    public void setDialogListener(Channel c, DialogListener listener) {
        checkChannel(c);
        c.setDialogListener(listener);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(APP_PKG_BUNDLE_KEY, c.mContext.getPackageName());
        bundle.putBoolean(RESET_DIALOG_LISTENER_BUNDLE_KEY, listener == null);
        msg.what = 139380;
        msg.setData(bundle);
        c.mAsyncChannel.sendMessage(msg);
    }

    public void deletePersistentGroup(Channel c, int netId, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DELETE_PERSISTENT_GROUP, netId, c.putListener(listener));
    }

    public void requestPersistentGroupInfo(Channel c, PersistentGroupInfoListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(REQUEST_PERSISTENT_GROUP_INFO, 0, c.putListener(listener));
    }

    public void setMiracastMode(int mode) {
        try {
            this.mService.setMiracastMode(mode);
        } catch (RemoteException e) {
        }
    }

    public void enableP2p(Channel c) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(ENABLE_P2P);
        if (DBG) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
    }

    public void disableP2p(Channel c) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISABLE_P2P);
        if (DBG) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
    }

    public void discoverPeersWithFlush(Channel c, int timeout, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(DISCOVER_PEERS_WITH_FLUSH, timeout, c.putListener(listener));
        sendHistoricalDumplog(c, "discoverPeersWithFlush()");
    }

    public void setListenOffloadingTimer(Channel c, boolean start) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(SET_LISTEN_OFFLOADING_TIMER, start ? 1 : 0);
    }

    public void setListenOffloading(Channel c, int channel, int period, int interval, int count, ActionListener listener) {
        checkChannel(c);
        Bundle p2pLOParams = new Bundle();
        p2pLOParams.putInt(TvContract.PARAM_CHANNEL, channel);
        p2pLOParams.putInt("period", period);
        p2pLOParams.putInt("interval", interval);
        p2pLOParams.putInt("count", count);
        c.mAsyncChannel.sendMessage(SET_LISTEN_OFFLOADING, 0, c.putListener(listener), p2pLOParams);
    }

    public void connect(Channel c, WifiP2pConfigList configs, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CONNECT_INVITE_LIST, 0, c.putListener(listener), configs);
    }

    public void requestP2pListen(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(P2P_LISTEN, 0, c.putListener(listener));
    }

    public void setStopfindTimer(Channel c, boolean start) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(SET_STOPFIND_TIMER, start ? 1 : 2);
    }

    public boolean isInactiveState() {
        try {
            return this.mService.isInactiveState();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isWifiP2pEnabled() {
        try {
            if (this.mService.getWifiP2pState() == 1) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isWifiP2pConnected() {
        try {
            if (this.mService.getWifiP2pState() == 3) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void requestNfcConnect(Channel c, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(139376, 1, c.putListener(listener));
    }

    public void requestNfcConnect(Channel c, boolean enable, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(139376, enable ? 1 : 0, c.putListener(listener));
    }

    public Messenger getMessenger() {
        try {
            return this.mService.getMessenger();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void connect(Channel c, int freq, WifiP2pConfig config, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CONNECT_PERSISTENT, freq, c.putListener(listener), config);
    }

    public void requestConfigList(Channel c, ConfigListListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(139378, 0, c.putListener(listener));
    }

    public int callSECApi(Channel c, Message msg) {
        checkChannel(c);
        if (msg == null) {
            return -1;
        }
        switch (msg.what) {
            case SEC_COMMAND_ID_REJECT_CONNECT /*139394*/:
                c.mAsyncChannel.sendMessage(SEC_COMMAND_ID_REJECT_CONNECT);
                return 0;
            case 139405:
                Message req_msg = Message.obtain();
                Bundle req_bundle = new Bundle();
                req_bundle.putString("REQ_DATA", (String) msg.obj);
                req_msg.what = 139405;
                req_msg.setData(req_bundle);
                c.mAsyncChannel.sendMessage(req_msg);
                return 0;
            case 139406:
                Message resp_msg = Message.obtain();
                Bundle resp_bundle = new Bundle();
                resp_bundle.putString("RESP_DATA", (String) msg.obj);
                resp_msg.what = 139406;
                resp_msg.setData(resp_bundle);
                c.mAsyncChannel.sendMessage(resp_msg);
                return 0;
            case SEC_COMMAND_ID_P2P_STOP_DISCOVERY_NO_FLUSH /*139407*/:
                c.mAsyncChannel.sendMessage(SEC_COMMAND_ID_P2P_STOP_DISCOVERY_NO_FLUSH);
                return -1;
            case SEC_COMMAND_ID_SET_MIRACAST /*139411*/:
                int mode = -1;
                if (msg.arg1 == 0) {
                    mode = 0;
                } else if (msg.arg1 == 1) {
                    mode = 1;
                } else if (msg.arg1 == 2) {
                    mode = 2;
                }
                if (mode == -1) {
                    return -1;
                }
                try {
                    this.mService.setMiracastMode(mode);
                    return 0;
                } catch (RemoteException e) {
                    return -1;
                }
            default:
                Log.e(TAG, "Unhandled message: " + msg.what);
                return -1;
        }
    }

    public Messenger getP2pStateMachineMessenger() {
        try {
            return this.mService.getP2pStateMachineMessenger();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void getNfcHandoverRequest(Channel c, HandoverMessageListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(GET_HANDOVER_REQUEST, 0, c.putListener(listener));
    }

    public void getNfcHandoverSelect(Channel c, HandoverMessageListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(GET_HANDOVER_SELECT, 0, c.putListener(listener));
    }

    public void initiatorReportNfcHandover(Channel c, String handoverSelect, ActionListener listener) {
        checkChannel(c);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HANDOVER_MESSAGE, handoverSelect);
        c.mAsyncChannel.sendMessage(INITIATOR_REPORT_NFC_HANDOVER, 0, c.putListener(listener), bundle);
    }

    public void responderReportNfcHandover(Channel c, String handoverRequest, ActionListener listener) {
        checkChannel(c);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HANDOVER_MESSAGE, handoverRequest);
        c.mAsyncChannel.sendMessage(RESPONDER_REPORT_NFC_HANDOVER, 0, c.putListener(listener), bundle);
    }
}
