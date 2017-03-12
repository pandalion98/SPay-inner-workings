package android.net;

import android.content.Context;
import android.net.IEthernetServiceListener.Stub;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class EthernetManager {
    public static final int DATA_ACTIVITY_IN = 1;
    public static final int DATA_ACTIVITY_INOUT = 3;
    public static final int DATA_ACTIVITY_NONE = 0;
    public static final int DATA_ACTIVITY_NOTIFICATION = 1;
    public static final int DATA_ACTIVITY_OUT = 2;
    public static final int ETH_DEVICE_SCAN_RESULT_READY = 0;
    public static final int ETH_ENABLE_TRAFFIC_STATS_POLL = 4;
    public static final String ETH_STATE_CHANGED_ACTION = "samsung.net.ethernet.ETH_STATE_CHANGED";
    public static final String ETH_STATE_CONNECTOR_ADD_ACTION = "android.net.ethernet.STATE_CONNECTOR_ADD";
    public static final String ETH_STATE_CONNECTOR_REMOVE_ACTION = "android.net.ethernet.STATE_CONNECTOR_REMOVE";
    public static final int ETH_STATE_DISABLED = 1;
    public static final int ETH_STATE_ENABLED = 2;
    public static final int ETH_STATE_ENABLING = 3;
    public static final int ETH_STATE_UNKNOWN = 0;
    public static final int ETH_TRAFFIC_STATS_POLL = 5;
    public static final String EXTRA_ETH_STATE = "eth_state";
    public static final String EXTRA_LAN_CABLE_STATE = "lan_cable_state";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_PREVIOUS_ETH_STATE = "previous_eth_state";
    private static final int MSG_AVAILABILITY_CHANGED = 1000;
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.ethernet.STATE_CHANGE";
    private static final String TAG = "EthernetManager";
    private final Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean isAvailable = true;
            if (msg.what == 1000) {
                if (msg.arg1 != 1) {
                    isAvailable = false;
                }
                Iterator i$ = EthernetManager.this.mListeners.iterator();
                while (i$.hasNext()) {
                    ((Listener) i$.next()).onAvailabilityChanged(isAvailable);
                }
            }
        }
    };
    private final ArrayList<Listener> mListeners = new ArrayList();
    private final IEthernetManager mService;
    private final Stub mServiceListener = new Stub() {
        public void onAvailabilityChanged(boolean isAvailable) {
            int i;
            Handler access$100 = EthernetManager.this.mHandler;
            if (isAvailable) {
                i = 1;
            } else {
                i = 0;
            }
            access$100.obtainMessage(1000, i, 0, null).sendToTarget();
        }
    };

    public interface Listener {
        void onAvailabilityChanged(boolean z);
    }

    public EthernetManager(Context context, IEthernetManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public IpConfiguration getConfiguration() {
        try {
            return this.mService.getConfiguration();
        } catch (NullPointerException e) {
            return new IpConfiguration();
        } catch (RemoteException e2) {
            return new IpConfiguration();
        }
    }

    public void setConfiguration(IpConfiguration config) {
        try {
            this.mService.setConfiguration(config);
        } catch (NullPointerException e) {
        } catch (RemoteException e2) {
        }
    }

    public boolean isAvailable() {
        try {
            return this.mService.isAvailable();
        } catch (NullPointerException e) {
            return false;
        } catch (RemoteException e2) {
            return false;
        }
    }

    public Messenger getEthernetServiceMessenger() {
        Messenger messenger = null;
        try {
            messenger = this.mService.getEthernetServiceMessenger();
        } catch (RemoteException e) {
        } catch (SecurityException e2) {
        }
        return messenger;
    }

    public boolean isEthConfigured() {
        try {
            return this.mService.isEthConfigured();
        } catch (RemoteException e) {
            Log.i(TAG, "Can not check eth config state");
            return false;
        }
    }

    public EthernetDevInfo getSavedEthConfig() {
        try {
            return this.mService.getSavedEthConfig();
        } catch (RemoteException e) {
            Log.i(TAG, "Can not get eth config");
            return null;
        }
    }

    public void UpdateEthDevInfo(EthernetDevInfo info) {
        try {
            this.mService.UpdateEthDevInfo(info);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not update ethernet device info");
        }
    }

    public void setEthEnabled(boolean enable) {
        try {
            this.mService.setEthState(enable ? 2 : 1);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not set new state");
        }
    }

    public void setEthernetState(int state) {
        try {
            this.mService.setEthState(state);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not set new state");
        }
    }

    public int getEthState() {
        try {
            return this.mService.getEthState();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean ethConfigured() {
        try {
            return this.mService.isEthConfigured();
        } catch (RemoteException e) {
            return false;
        }
    }

    public String[] getDeviceNameList() {
        try {
            return this.mService.getDeviceNameList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isEthReset() {
        try {
            return this.mService.isEthReset();
        } catch (RemoteException e) {
            Log.i(TAG, "Can not set new state");
            return false;
        }
    }

    public void setUserDisabled(boolean newState) {
        try {
            this.mService.setUserDisabled(newState);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not set new state");
        }
    }

    public boolean getUserDisabled() {
        try {
            return this.mService.getUserDisabled();
        } catch (RemoteException e) {
            Log.i(TAG, "Can not set new state");
            return false;
        }
    }

    public boolean getConnectorConnected() {
        try {
            return this.mService.getConnectorConnected();
        } catch (RemoteException e) {
            Log.i(TAG, "Can not getConnectorConnected");
            return false;
        }
    }

    public void setCheckConnecting(int value) {
        try {
            this.mService.setCheckConnecting(value);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not setCheckConnecting");
        }
    }

    public int getCheckConnecting() {
        try {
            return this.mService.getCheckConnecting();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setStackConnected(boolean value) {
        try {
            this.mService.setStackConnected(value);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not setStackConnected");
        }
    }

    public boolean getStackConnected() {
        try {
            return this.mService.getStackConnected();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setHWConnected(boolean value) {
        try {
            this.mService.setHWConnected(value);
        } catch (RemoteException e) {
            Log.i(TAG, "Can not setHWConnected");
        }
    }

    public boolean getHWConnected() {
        try {
            return this.mService.getHWConnected();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getTotalInterface() {
        try {
            return this.mService.getTotalInterface();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void ethSetDefaultConf() {
        try {
            this.mService.setEthMode("dhcp");
        } catch (RemoteException e) {
        }
    }

    public void addListener(Listener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        this.mListeners.add(listener);
        if (this.mListeners.size() == 1) {
            try {
                this.mService.addListener(this.mServiceListener);
            } catch (NullPointerException e) {
            } catch (RemoteException e2) {
            }
        }
    }

    public void removeListener(Listener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        this.mListeners.remove(listener);
        if (this.mListeners.isEmpty()) {
            try {
                this.mService.removeListener(this.mServiceListener);
            } catch (NullPointerException e) {
            } catch (RemoteException e2) {
            }
        }
    }
}
