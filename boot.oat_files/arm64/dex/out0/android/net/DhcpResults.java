package android.net;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.net.Inet4Address;
import java.util.Objects;

public class DhcpResults extends StaticIpConfiguration {
    public static final Creator<DhcpResults> CREATOR = new Creator<DhcpResults>() {
        public DhcpResults createFromParcel(Parcel in) {
            DhcpResults dhcpResults = new DhcpResults();
            DhcpResults.readFromParcel(dhcpResults, in);
            return dhcpResults;
        }

        public DhcpResults[] newArray(int size) {
            return new DhcpResults[size];
        }
    };
    public static final int RESPONSE_DHCP_REQUEST = 2;
    public static final int RESTART_DHCP_DISCOVER = 4;
    private static final String TAG = "DhcpResults";
    public int leaseDuration;
    public int resultAfterRoaming;
    public Inet4Address serverAddress;
    public String vendorInfo;

    public DhcpResults(StaticIpConfiguration source) {
        super(source);
    }

    public DhcpResults(DhcpResults source) {
        super(source);
        if (source != null) {
            this.serverAddress = source.serverAddress;
            this.vendorInfo = source.vendorInfo;
            this.leaseDuration = source.leaseDuration;
            this.resultAfterRoaming = source.resultAfterRoaming;
        }
    }

    public void updateFromDhcpRequest(DhcpResults orig) {
        if (orig != null) {
            if (this.gateway == null) {
                this.gateway = orig.gateway;
            }
            if (this.dnsServers.size() == 0) {
                this.dnsServers.addAll(orig.dnsServers);
            }
        }
    }

    public boolean hasMeteredHint() {
        if (this.vendorInfo != null) {
            return this.vendorInfo.contains("ANDROID_METERED");
        }
        return false;
    }

    public void clear() {
        super.clear();
        this.vendorInfo = null;
        this.leaseDuration = 0;
        this.resultAfterRoaming = 0;
    }

    public String toString() {
        StringBuffer str = new StringBuffer(super.toString());
        str.append(" DHCP server ").append(this.serverAddress);
        str.append(" Vendor info ").append(this.vendorInfo);
        str.append(" lease ").append(this.leaseDuration).append(" seconds");
        if (this.resultAfterRoaming == 2) {
            str.append(" - Server responded to REQUEST at last DHCP process");
        }
        return str.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DhcpResults)) {
            return false;
        }
        DhcpResults target = (DhcpResults) obj;
        if (super.equals((StaticIpConfiguration) obj) && Objects.equals(this.serverAddress, target.serverAddress) && Objects.equals(this.vendorInfo, target.vendorInfo) && this.leaseDuration == target.leaseDuration && this.resultAfterRoaming == target.resultAfterRoaming) {
            return true;
        }
        return false;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.leaseDuration);
        NetworkUtils.parcelInetAddress(dest, this.serverAddress, flags);
        dest.writeString(this.vendorInfo);
        dest.writeInt(this.resultAfterRoaming);
    }

    private static void readFromParcel(DhcpResults dhcpResults, Parcel in) {
        StaticIpConfiguration.readFromParcel(dhcpResults, in);
        dhcpResults.leaseDuration = in.readInt();
        dhcpResults.serverAddress = (Inet4Address) NetworkUtils.unparcelInetAddress(in);
        dhcpResults.vendorInfo = in.readString();
    }

    public boolean setIpAddress(String addrString, int prefixLength) {
        try {
            this.ipAddress = new LinkAddress((Inet4Address) NetworkUtils.numericToInetAddress(addrString), prefixLength);
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "setIpAddress failed with addrString " + addrString + "/" + prefixLength);
            return true;
        } catch (ClassCastException e2) {
            Log.e(TAG, "setIpAddress failed with addrString " + addrString + "/" + prefixLength);
            return true;
        }
    }

    public boolean setGateway(String addrString) {
        try {
            this.gateway = NetworkUtils.numericToInetAddress(addrString);
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "setGateway failed with addrString " + addrString);
            return true;
        }
    }

    public boolean addDns(String addrString) {
        if (!TextUtils.isEmpty(addrString)) {
            try {
                this.dnsServers.add(NetworkUtils.numericToInetAddress(addrString));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "addDns failed with addrString " + addrString);
                return true;
            }
        }
        return false;
    }

    public boolean setServerAddress(String addrString) {
        if (addrString != null) {
            try {
                if (!addrString.isEmpty()) {
                    this.serverAddress = (Inet4Address) NetworkUtils.numericToInetAddress(addrString);
                    return false;
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "setServerAddress failed with addrString " + addrString);
                return true;
            } catch (ClassCastException e2) {
                Log.e(TAG, "setServerAddress failed with addrString " + addrString);
                return true;
            }
        }
        this.serverAddress = (Inet4Address) Inet4Address.LOOPBACK;
        return false;
    }

    public void setLeaseDuration(int duration) {
        this.leaseDuration = duration;
    }

    public void setRoamingResult(int RoamingResult) {
        this.resultAfterRoaming = RoamingResult;
    }

    public void setVendorInfo(String info) {
        this.vendorInfo = info;
    }

    public void setDomains(String newDomains) {
        this.domains = newDomains;
    }
}
