package android.net.wifi.passpoint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class WifiPasspointInfo implements Parcelable {
    public static final int ANQP_CAPABILITY = 1;
    public static final int CELLULAR_NETWORK = 64;
    public static final int CONNECTION_CAPABILITY = 2048;
    public static final Creator<WifiPasspointInfo> CREATOR = new Creator<WifiPasspointInfo>() {
        public WifiPasspointInfo createFromParcel(Parcel in) {
            int i;
            WifiPasspointInfo p = new WifiPasspointInfo();
            p.bssid = in.readString();
            p.venueName = in.readString();
            int n = in.readInt();
            if (n > 0) {
                p.networkAuthTypeList = new ArrayList();
                for (i = 0; i < n; i++) {
                    NetworkAuthType auth = new NetworkAuthType();
                    auth.type = in.readInt();
                    auth.redirectUrl = in.readString();
                    p.networkAuthTypeList.add(auth);
                }
            }
            n = in.readInt();
            if (n > 0) {
                p.roamingConsortiumList = new ArrayList();
                for (i = 0; i < n; i++) {
                    p.roamingConsortiumList.add(in.readString());
                }
            }
            n = in.readInt();
            if (n != -1) {
                p.ipAddrTypeAvailability = new IpAddressType();
                p.ipAddrTypeAvailability.availability = n;
            }
            n = in.readInt();
            if (n > 0) {
                p.naiRealmList = new ArrayList();
                for (i = 0; i < n; i++) {
                    NaiRealm realm = new NaiRealm();
                    realm.encoding = in.readInt();
                    realm.realm = in.readString();
                    p.naiRealmList.add(realm);
                }
            }
            n = in.readInt();
            if (n > 0) {
                p.cellularNetworkList = new ArrayList();
                for (i = 0; i < n; i++) {
                    CellularNetwork plmn = new CellularNetwork();
                    plmn.mcc = in.readString();
                    plmn.mnc = in.readString();
                    p.cellularNetworkList.add(plmn);
                }
            }
            n = in.readInt();
            if (n > 0) {
                p.domainNameList = new ArrayList();
                for (i = 0; i < n; i++) {
                    p.domainNameList.add(in.readString());
                }
            }
            p.operatorFriendlyName = in.readString();
            if (in.readInt() > 0) {
                p.wanMetrics = new WanMetrics();
                p.wanMetrics.wanInfo = in.readInt();
                p.wanMetrics.downlinkSpeed = in.readLong();
                p.wanMetrics.uplinkSpeed = in.readLong();
                p.wanMetrics.downlinkLoad = in.readInt();
                p.wanMetrics.uplinkLoad = in.readInt();
                p.wanMetrics.lmd = in.readInt();
            }
            n = in.readInt();
            if (n > 0) {
                p.connectionCapabilityList = new ArrayList();
                for (i = 0; i < n; i++) {
                    IpProtoPort ip = new IpProtoPort();
                    ip.proto = in.readInt();
                    ip.port = in.readInt();
                    ip.status = in.readInt();
                    p.connectionCapabilityList.add(ip);
                }
            }
            n = in.readInt();
            if (n > 0) {
                p.osuProviderList = new ArrayList();
                for (i = 0; i < n; i++) {
                    p.osuProviderList.add((WifiPasspointOsuProvider) WifiPasspointOsuProvider.CREATOR.createFromParcel(in));
                }
            }
            return p;
        }

        public WifiPasspointInfo[] newArray(int size) {
            return new WifiPasspointInfo[size];
        }
    };
    public static final int DOMAIN_NAME = 128;
    public static final int HOTSPOT_CAPABILITY = 256;
    public static final int IP_ADDR_TYPE_AVAILABILITY = 16;
    public static final int NAI_REALM = 32;
    public static final int NETWORK_AUTH_TYPE = 4;
    public static final int OPERATOR_FRIENDLY_NAME = 512;
    public static final int OSU_PROVIDER = 4096;
    public static final int PRESET_ALL = 8191;
    public static final int PRESET_CRED_MATCH = 481;
    public static final int ROAMING_CONSORTIUM = 8;
    public static final int VENUE_NAME = 2;
    public static final int WAN_METRICS = 1024;
    public String bssid;
    public List<CellularNetwork> cellularNetworkList;
    public List<IpProtoPort> connectionCapabilityList;
    public List<String> domainNameList;
    public IpAddressType ipAddrTypeAvailability;
    public List<NaiRealm> naiRealmList;
    public List<NetworkAuthType> networkAuthTypeList;
    public String operatorFriendlyName;
    public List<WifiPasspointOsuProvider> osuProviderList;
    public List<String> roamingConsortiumList;
    public String venueName;
    public WanMetrics wanMetrics;

    public static class CellularNetwork {
        public String mcc;
        public String mnc;

        public String toString() {
            return this.mcc + "," + this.mnc;
        }
    }

    public static class IpAddressType {
        public static final int IPV4_DOUBLE_NAT = 4;
        public static final int IPV4_NOT_AVAILABLE = 0;
        public static final int IPV4_PORT_RESTRICTED = 2;
        public static final int IPV4_PORT_RESTRICTED_DOUBLE_NAT = 6;
        public static final int IPV4_PORT_RESTRICTED_SINGLE_NAT = 5;
        public static final int IPV4_PORT_UNKNOWN = 7;
        public static final int IPV4_PUBLIC = 1;
        public static final int IPV4_SINGLE_NAT = 3;
        public static final int IPV6_AVAILABLE = 1;
        public static final int IPV6_NOT_AVAILABLE = 0;
        public static final int IPV6_UNKNOWN = 2;
        private static final int NULL_VALUE = -1;
        public int availability;

        public int getIpv6Availability() {
            return this.availability & 3;
        }

        public int getIpv4Availability() {
            return (this.availability & 255) >> 2;
        }

        public String toString() {
            return getIpv6Availability() + "," + getIpv4Availability();
        }
    }

    public static class IpProtoPort {
        public static final int STATUS_CLOSED = 0;
        public static final int STATUS_OPEN = 1;
        public static final int STATUS_UNKNOWN = 2;
        public int port;
        public int proto;
        public int status;

        public String toString() {
            return this.proto + "," + this.port + "," + this.status;
        }
    }

    public static class NaiRealm {
        public static final int ENCODING_RFC4282 = 0;
        public static final int ENCODING_UTF8 = 1;
        public int encoding;
        public String realm;

        public String toString() {
            return this.encoding + "," + this.realm;
        }
    }

    public static class NetworkAuthType {
        public static final int TYPE_DNS_REDIRECTION = 3;
        public static final int TYPE_HTTP_REDIRECTION = 2;
        public static final int TYPE_ONLINE_ENROLLMENT = 1;
        public static final int TYPE_TERMS_AND_CONDITION = 0;
        public String redirectUrl;
        public int type;

        public String toString() {
            return this.type + "," + this.redirectUrl;
        }
    }

    public static class WanMetrics {
        public static final int STATUS_DOWN = 2;
        public static final int STATUS_RESERVED = 0;
        public static final int STATUS_TEST = 3;
        public static final int STATUS_UP = 1;
        public int downlinkLoad;
        public long downlinkSpeed;
        public int lmd;
        public int uplinkLoad;
        public long uplinkSpeed;
        public int wanInfo;

        public int getLinkStatus() {
            return this.wanInfo & 3;
        }

        public boolean getSymmetricLink() {
            return (this.wanInfo & 4) != 0;
        }

        public boolean getAtCapacity() {
            return (this.wanInfo & 8) != 0;
        }

        public String toString() {
            return this.wanInfo + "," + this.downlinkSpeed + "," + this.uplinkSpeed + "," + this.downlinkLoad + "," + this.uplinkLoad + "," + this.lmd;
        }
    }

    public static String toAnqpSubtypes(int mask) {
        StringBuilder sb = new StringBuilder();
        if ((mask & 1) != 0) {
            sb.append("257,");
        }
        if ((mask & 2) != 0) {
            sb.append("258,");
        }
        if ((mask & 4) != 0) {
            sb.append("260,");
        }
        if ((mask & 8) != 0) {
            sb.append("261,");
        }
        if ((mask & 16) != 0) {
            sb.append("262,");
        }
        if ((mask & 32) != 0) {
            sb.append("263,");
        }
        if ((mask & 64) != 0) {
            sb.append("264,");
        }
        if ((mask & 128) != 0) {
            sb.append("268,");
        }
        if ((mask & 256) != 0) {
            sb.append("hs20:2,");
        }
        if ((mask & 512) != 0) {
            sb.append("hs20:3,");
        }
        if ((mask & 1024) != 0) {
            sb.append("hs20:4,");
        }
        if ((mask & 2048) != 0) {
            sb.append("hs20:5,");
        }
        if ((mask & 4096) != 0) {
            sb.append("hs20:8,");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("BSSID: ").append("(").append(this.bssid).append(")");
        if (this.venueName != null) {
            sb.append(" venueName: ").append("(").append(this.venueName.replace("\n", "\\n")).append(")");
        }
        if (this.networkAuthTypeList != null) {
            sb.append(" networkAuthType: ");
            for (NetworkAuthType auth : this.networkAuthTypeList) {
                sb.append("(").append(auth.toString()).append(")");
            }
        }
        if (this.roamingConsortiumList != null) {
            sb.append(" roamingConsortium: ");
            for (String oi : this.roamingConsortiumList) {
                sb.append("(").append(oi).append(")");
            }
        }
        if (this.ipAddrTypeAvailability != null) {
            sb.append(" ipAddrTypeAvaibility: ").append("(").append(this.ipAddrTypeAvailability.toString()).append(")");
        }
        if (this.naiRealmList != null) {
            sb.append(" naiRealm: ");
            for (NaiRealm realm : this.naiRealmList) {
                sb.append("(").append(realm.toString()).append(")");
            }
        }
        if (this.cellularNetworkList != null) {
            sb.append(" cellularNetwork: ");
            for (CellularNetwork plmn : this.cellularNetworkList) {
                sb.append("(").append(plmn.toString()).append(")");
            }
        }
        if (this.domainNameList != null) {
            sb.append(" domainName: ");
            for (String fqdn : this.domainNameList) {
                sb.append("(").append(fqdn).append(")");
            }
        }
        if (this.operatorFriendlyName != null) {
            sb.append(" operatorFriendlyName: ").append("(").append(this.operatorFriendlyName).append(")");
        }
        if (this.wanMetrics != null) {
            sb.append(" wanMetrics: ").append("(").append(this.wanMetrics.toString()).append(")");
        }
        if (this.connectionCapabilityList != null) {
            sb.append(" connectionCapability: ");
            for (IpProtoPort ip : this.connectionCapabilityList) {
                sb.append("(").append(ip.toString()).append(")");
            }
        }
        if (this.osuProviderList != null) {
            sb.append(" osuProviderList: ");
            for (WifiPasspointOsuProvider osu : this.osuProviderList) {
                sb.append("(").append(osu.toString()).append(")");
            }
        }
        return sb.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.bssid);
        out.writeString(this.venueName);
        if (this.networkAuthTypeList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.networkAuthTypeList.size());
            for (NetworkAuthType auth : this.networkAuthTypeList) {
                out.writeInt(auth.type);
                out.writeString(auth.redirectUrl);
            }
        }
        if (this.roamingConsortiumList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.roamingConsortiumList.size());
            for (String oi : this.roamingConsortiumList) {
                out.writeString(oi);
            }
        }
        if (this.ipAddrTypeAvailability == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(this.ipAddrTypeAvailability.availability);
        }
        if (this.naiRealmList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.naiRealmList.size());
            for (NaiRealm realm : this.naiRealmList) {
                out.writeInt(realm.encoding);
                out.writeString(realm.realm);
            }
        }
        if (this.cellularNetworkList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.cellularNetworkList.size());
            for (CellularNetwork plmn : this.cellularNetworkList) {
                out.writeString(plmn.mcc);
                out.writeString(plmn.mnc);
            }
        }
        if (this.domainNameList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.domainNameList.size());
            for (String fqdn : this.domainNameList) {
                out.writeString(fqdn);
            }
        }
        out.writeString(this.operatorFriendlyName);
        if (this.wanMetrics == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            out.writeInt(this.wanMetrics.wanInfo);
            out.writeLong(this.wanMetrics.downlinkSpeed);
            out.writeLong(this.wanMetrics.uplinkSpeed);
            out.writeInt(this.wanMetrics.downlinkLoad);
            out.writeInt(this.wanMetrics.uplinkLoad);
            out.writeInt(this.wanMetrics.lmd);
        }
        if (this.connectionCapabilityList == null) {
            out.writeInt(0);
        } else {
            out.writeInt(this.connectionCapabilityList.size());
            for (IpProtoPort ip : this.connectionCapabilityList) {
                out.writeInt(ip.proto);
                out.writeInt(ip.port);
                out.writeInt(ip.status);
            }
        }
        if (this.osuProviderList == null) {
            out.writeInt(0);
            return;
        }
        out.writeInt(this.osuProviderList.size());
        for (WifiPasspointOsuProvider osu : this.osuProviderList) {
            osu.writeToParcel(out, flags);
        }
    }

    public int describeContents() {
        return 0;
    }
}
