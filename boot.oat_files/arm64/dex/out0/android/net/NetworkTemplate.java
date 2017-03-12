package android.net;

import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.TelephonyManager;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;
import java.util.Objects;

public class NetworkTemplate implements Parcelable {
    public static final Creator<NetworkTemplate> CREATOR = new Creator<NetworkTemplate>() {
        public NetworkTemplate createFromParcel(Parcel in) {
            return new NetworkTemplate(in);
        }

        public NetworkTemplate[] newArray(int size) {
            return new NetworkTemplate[size];
        }
    };
    private static final int[] DATA_USAGE_NETWORK_TYPES = Resources.getSystem().getIntArray(17235983);
    public static final int MATCH_BLUETOOTH = 8;
    public static final int MATCH_ETHERNET = 5;
    @Deprecated
    public static final int MATCH_MOBILE_3G_LOWER = 2;
    @Deprecated
    public static final int MATCH_MOBILE_4G = 3;
    public static final int MATCH_MOBILE_ALL = 1;
    public static final int MATCH_MOBILE_ENT1 = 9;
    public static final int MATCH_MOBILE_WILDCARD = 6;
    public static final int MATCH_WIFI = 4;
    public static final int MATCH_WIFI_WILDCARD = 7;
    private static boolean sForceAllNetworkTypes = false;
    private final int mMatchRule;
    private final String[] mMatchSubscriberIds;
    private final String mNetworkId;
    private final String mSubscriberId;

    public static void forceAllNetworkTypes() {
        sForceAllNetworkTypes = true;
    }

    public static NetworkTemplate buildTemplateMobileAll(String subscriberId) {
        return new NetworkTemplate(1, subscriberId, null);
    }

    @Deprecated
    public static NetworkTemplate buildTemplateMobile3gLower(String subscriberId) {
        return new NetworkTemplate(2, subscriberId, null);
    }

    @Deprecated
    public static NetworkTemplate buildTemplateMobile4g(String subscriberId) {
        return new NetworkTemplate(3, subscriberId, null);
    }

    public static NetworkTemplate buildTemplateMobileEnt1(String subscriberId) {
        return new NetworkTemplate(9, subscriberId, null);
    }

    public static NetworkTemplate buildTemplateMobileWildcard() {
        return new NetworkTemplate(6, null, null);
    }

    public static NetworkTemplate buildTemplateWifiWildcard() {
        return new NetworkTemplate(7, null, null);
    }

    @Deprecated
    public static NetworkTemplate buildTemplateWifi() {
        return buildTemplateWifiWildcard();
    }

    public static NetworkTemplate buildTemplateWifi(String networkId) {
        return new NetworkTemplate(4, null, networkId);
    }

    public static NetworkTemplate buildTemplateEthernet() {
        return new NetworkTemplate(5, null, null);
    }

    public static NetworkTemplate buildTemplateBluetooth() {
        return new NetworkTemplate(8, null, null);
    }

    public NetworkTemplate(int matchRule, String subscriberId, String networkId) {
        this(matchRule, subscriberId, new String[]{subscriberId}, networkId);
    }

    public NetworkTemplate(int matchRule, String subscriberId, String[] matchSubscriberIds, String networkId) {
        this.mMatchRule = matchRule;
        this.mSubscriberId = subscriberId;
        this.mMatchSubscriberIds = matchSubscriberIds;
        this.mNetworkId = networkId;
    }

    private NetworkTemplate(Parcel in) {
        this.mMatchRule = in.readInt();
        this.mSubscriberId = in.readString();
        this.mMatchSubscriberIds = in.createStringArray();
        this.mNetworkId = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMatchRule);
        dest.writeString(this.mSubscriberId);
        dest.writeStringArray(this.mMatchSubscriberIds);
        dest.writeString(this.mNetworkId);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("NetworkTemplate: ");
        builder.append("matchRule=").append(getMatchRuleName(this.mMatchRule));
        if (this.mSubscriberId != null) {
            builder.append(", subscriberId=").append(NetworkIdentity.scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mMatchSubscriberIds != null) {
            builder.append(", matchSubscriberIds=").append(Arrays.toString(NetworkIdentity.scrubSubscriberId(this.mMatchSubscriberIds)));
        }
        if (this.mNetworkId != null) {
            builder.append(", networkId=").append(this.mNetworkId);
        }
        return builder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mMatchRule), this.mSubscriberId, this.mNetworkId});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkTemplate)) {
            return false;
        }
        NetworkTemplate other = (NetworkTemplate) obj;
        if (this.mMatchRule == other.mMatchRule && Objects.equals(this.mSubscriberId, other.mSubscriberId) && Objects.equals(this.mNetworkId, other.mNetworkId)) {
            return true;
        }
        return false;
    }

    public boolean isMatchRuleMobile() {
        switch (this.mMatchRule) {
            case 1:
            case 2:
            case 3:
            case 6:
            case 9:
                return true;
            default:
                return false;
        }
    }

    public int getMatchRule() {
        return this.mMatchRule;
    }

    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public String getNetworkId() {
        return this.mNetworkId;
    }

    public boolean matches(NetworkIdentity ident) {
        switch (this.mMatchRule) {
            case 1:
                return matchesMobile(ident);
            case 2:
                return matchesMobile3gLower(ident);
            case 3:
                return matchesMobile4g(ident);
            case 4:
                return matchesWifi(ident);
            case 5:
                return matchesEthernet(ident);
            case 6:
                return matchesMobileWildcard(ident);
            case 7:
                return matchesWifiWildcard(ident);
            case 8:
                return matchesBluetooth(ident);
            case 9:
                return matchesMobileEnt1(ident);
            default:
                throw new IllegalArgumentException("unknown network template");
        }
    }

    private boolean matchesMobile(NetworkIdentity ident) {
        if (ident.mType == 6) {
            return true;
        }
        boolean matchesType;
        if (sForceAllNetworkTypes || ArrayUtils.contains(DATA_USAGE_NETWORK_TYPES, ident.mType)) {
            matchesType = true;
        } else {
            matchesType = false;
        }
        if (matchesType && ArrayUtils.contains(this.mMatchSubscriberIds, ident.mSubscriberId)) {
            return true;
        }
        return false;
    }

    @Deprecated
    private boolean matchesMobile3gLower(NetworkIdentity ident) {
        ensureSubtypeAvailable();
        if (ident.mType == 6 || !matchesMobile(ident)) {
            return false;
        }
        switch (TelephonyManager.getNetworkClass(ident.mSubType)) {
            case 0:
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    private boolean matchesMobile4g(NetworkIdentity ident) {
        ensureSubtypeAvailable();
        if (ident.mType == 6) {
            return true;
        }
        if (matchesMobile(ident)) {
            switch (TelephonyManager.getNetworkClass(ident.mSubType)) {
                case 3:
                    return true;
            }
        }
        return false;
    }

    private boolean matchesMobileEnt1(NetworkIdentity ident) {
        switch (ident.mType) {
            case 28:
                return Objects.equals(this.mSubscriberId, ident.mSubscriberId);
            default:
                return false;
        }
    }

    private boolean matchesWifi(NetworkIdentity ident) {
        switch (ident.mType) {
            case 1:
                return Objects.equals(WifiInfo.removeDoubleQuotes(this.mNetworkId), WifiInfo.removeDoubleQuotes(ident.mNetworkId));
            default:
                return false;
        }
    }

    private boolean matchesEthernet(NetworkIdentity ident) {
        if (ident.mType == 9) {
            return true;
        }
        return false;
    }

    private boolean matchesMobileWildcard(NetworkIdentity ident) {
        if (ident.mType == 6 || sForceAllNetworkTypes || ArrayUtils.contains(DATA_USAGE_NETWORK_TYPES, ident.mType)) {
            return true;
        }
        return false;
    }

    private boolean matchesWifiWildcard(NetworkIdentity ident) {
        switch (ident.mType) {
            case 1:
            case 13:
                return true;
            default:
                return false;
        }
    }

    private boolean matchesBluetooth(NetworkIdentity ident) {
        if (ident.mType == 7) {
            return true;
        }
        return false;
    }

    public boolean countVideoCall() {
        switch (this.mMatchRule) {
            case 4:
            case 5:
            case 7:
                return false;
            default:
                return true;
        }
    }

    private static String getMatchRuleName(int matchRule) {
        switch (matchRule) {
            case 1:
                return "MOBILE_ALL";
            case 2:
                return "MOBILE_3G_LOWER";
            case 3:
                return "MOBILE_4G";
            case 4:
                return "WIFI";
            case 5:
                return "ETHERNET";
            case 6:
                return "MOBILE_WILDCARD";
            case 7:
                return "WIFI_WILDCARD";
            case 8:
                return "BLUETOOTH";
            case 9:
                return "MATCH_MOBILE_ENT1";
            default:
                return "UNKNOWN";
        }
    }

    private static void ensureSubtypeAvailable() {
        throw new IllegalArgumentException("Unable to enforce 3G_LOWER template on combined data.");
    }

    public static NetworkTemplate normalize(NetworkTemplate template, String[] merged) {
        if (template.isMatchRuleMobile() && ArrayUtils.contains(merged, template.mSubscriberId)) {
            return new NetworkTemplate(template.mMatchRule, merged[0], merged, template.mNetworkId);
        }
        return template;
    }
}
