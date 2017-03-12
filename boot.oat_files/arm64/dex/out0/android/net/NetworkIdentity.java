package android.net;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Slog;
import java.util.Objects;

public class NetworkIdentity implements Comparable<NetworkIdentity> {
    @Deprecated
    public static final boolean COMBINE_SUBTYPE_ENABLED = true;
    public static final int SUBTYPE_COMBINED = -1;
    private static final String TAG = "NetworkIdentity";
    final String mNetworkId;
    final boolean mRoaming;
    final int mSubType = -1;
    final String mSubscriberId;
    final int mType;

    public NetworkIdentity(int type, int subType, String subscriberId, String networkId, boolean roaming) {
        this.mType = type;
        this.mSubscriberId = subscriberId;
        this.mNetworkId = networkId;
        this.mRoaming = roaming;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mType), Integer.valueOf(this.mSubType), this.mSubscriberId, this.mNetworkId, Boolean.valueOf(this.mRoaming)});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkIdentity)) {
            return false;
        }
        NetworkIdentity ident = (NetworkIdentity) obj;
        if (this.mType == ident.mType && this.mSubType == ident.mSubType && this.mRoaming == ident.mRoaming && Objects.equals(this.mSubscriberId, ident.mSubscriberId) && Objects.equals(this.mNetworkId, ident.mNetworkId)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("type=").append(ConnectivityManager.getNetworkTypeName(this.mType));
        builder.append(", subType=");
        builder.append("COMBINED");
        if (this.mSubscriberId != null) {
            builder.append(", subscriberId=").append(scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mNetworkId != null) {
            builder.append(", networkId=").append(this.mNetworkId);
        }
        if (this.mRoaming) {
            builder.append(", ROAMING");
        }
        return builder.append("}").toString();
    }

    public int getType() {
        return this.mType;
    }

    public int getSubType() {
        return this.mSubType;
    }

    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public String getNetworkId() {
        return this.mNetworkId;
    }

    public boolean getRoaming() {
        return this.mRoaming;
    }

    public static String scrubSubscriberId(String subscriberId) {
        if ("eng".equals(Build.TYPE)) {
            return subscriberId;
        }
        if (subscriberId != null) {
            return subscriberId.substring(0, Math.min(6, subscriberId.length())) + "...";
        }
        return "null";
    }

    public static String[] scrubSubscriberId(String[] subscriberId) {
        if (subscriberId == null) {
            return null;
        }
        String[] res = new String[subscriberId.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = scrubSubscriberId(subscriberId[i]);
        }
        return res;
    }

    public static NetworkIdentity buildNetworkIdentity(Context context, NetworkState state) {
        int type = state.networkInfo.getType();
        int subType = state.networkInfo.getSubtype();
        String subscriberId = null;
        String networkId = null;
        boolean roaming = false;
        if (ConnectivityManager.isNetworkTypeMobile(type)) {
            if (state.subscriberId == null) {
                Slog.w(TAG, "Active mobile network without subscriber!");
            }
            TelephonyManager telephony = (TelephonyManager) context.getSystemService("phone");
            roaming = telephony.isNetworkRoaming();
            if (state.subscriberId != null) {
                subscriberId = state.subscriberId;
            } else {
                subscriberId = telephony.getSubscriberId(SubscriptionManager.getDefaultDataSubId());
            }
            roaming = state.networkInfo.isRoaming();
        } else if (type == 1) {
            if (state.networkId != null) {
                networkId = state.networkId;
            } else {
                WifiInfo info = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                networkId = info != null ? info.getSSID() : null;
            }
        }
        return new NetworkIdentity(type, subType, subscriberId, networkId, roaming);
    }

    public int compareTo(NetworkIdentity another) {
        int res = Integer.compare(this.mType, another.mType);
        if (res == 0) {
            res = Integer.compare(this.mSubType, another.mSubType);
        }
        if (!(res != 0 || this.mSubscriberId == null || another.mSubscriberId == null)) {
            res = this.mSubscriberId.compareTo(another.mSubscriberId);
        }
        if (!(res != 0 || this.mNetworkId == null || another.mNetworkId == null)) {
            res = this.mNetworkId.compareTo(another.mNetworkId);
        }
        if (res == 0) {
            return Boolean.compare(this.mRoaming, another.mRoaming);
        }
        return res;
    }
}
