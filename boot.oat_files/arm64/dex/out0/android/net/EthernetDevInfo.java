package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class EthernetDevInfo implements Parcelable {
    public static final Creator<EthernetDevInfo> CREATOR = new Creator<EthernetDevInfo>() {
        public EthernetDevInfo createFromParcel(Parcel in) {
            EthernetDevInfo info = new EthernetDevInfo();
            info.setIfName(in.readString());
            info.setIpAddress(in.readString());
            info.setNetMask(in.readString());
            info.setRouteAddr(in.readString());
            info.setDnsAddr(in.readString());
            info.setConnectMode(in.readString());
            return info;
        }

        public EthernetDevInfo[] newArray(int size) {
            return new EthernetDevInfo[size];
        }
    };
    public static final String ETH_CONN_MODE_DHCP = "dhcp";
    public static final String ETH_CONN_MODE_MANUAL = "manual";
    public static final String ETH_CONN_MODE_NONE = "none";
    private String dev_name = null;
    private String dns = null;
    private String ipaddr = null;
    private String mode = "dhcp";
    private String netmask = null;
    private String route = null;

    public void setIfName(String ifname) {
        this.dev_name = ifname;
    }

    public String getIfName() {
        return this.dev_name;
    }

    public void setIpAddress(String ip) {
        this.ipaddr = ip;
    }

    public String getIpAddress() {
        return this.ipaddr;
    }

    public void setNetMask(String ip) {
        this.netmask = ip;
    }

    public String getNetMask() {
        return this.netmask;
    }

    public void setRouteAddr(String route) {
        this.route = route;
    }

    public String getRouteAddr() {
        return this.route;
    }

    public void setDnsAddr(String dns) {
        this.dns = dns;
    }

    public String getDnsAddr() {
        return this.dns;
    }

    public boolean setConnectMode(String mode) {
        if (!mode.equals("dhcp") && !mode.equals("manual")) {
            return false;
        }
        this.mode = mode;
        return true;
    }

    public String getConnectMode() {
        return this.mode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dev_name);
        dest.writeString(this.ipaddr);
        dest.writeString(this.netmask);
        dest.writeString(this.route);
        dest.writeString(this.dns);
        dest.writeString(this.mode);
    }
}
