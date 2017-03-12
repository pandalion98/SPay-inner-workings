package android.net.wifi.hs20;

import android.net.wifi.hs20.WifiHs20Manager.AccessProtocol;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WifiHs20OsuProvider implements Parcelable {
    public static final Creator<WifiHs20OsuProvider> CREATOR = new Creator<WifiHs20OsuProvider>() {
        public WifiHs20OsuProvider createFromParcel(Parcel in) {
            Bundle b = in.readBundle();
            HashMap<String, String> osuIcon = (HashMap) b.getSerializable("osu_icon");
            HashMap<String, String> serviceDescription = (HashMap) b.getSerializable("service_description");
            return new WifiHs20OsuProvider(in.readString(), in.readString(), in.readString(), in.readString(), (HashMap) b.getSerializable("freindly_name"), URI.create(in.readString()), AccessProtocol.valueOf(in.readString()), osuIcon, in.readString(), serviceDescription, Long.valueOf(in.readLong()));
        }

        public WifiHs20OsuProvider[] newArray(int size) {
            return new WifiHs20OsuProvider[size];
        }
    };
    private static final boolean DBG = true;
    private static final String TAG = "Hs20OsuProvider";
    private String NAI;
    private String bssid;
    private HashMap<String, String> friendlyName;
    private String hessid;
    private AccessProtocol methodType;
    private HashMap<String, String> osuIcon;
    private String osuSsid;
    private long seen;
    private URI serverUri;
    private HashMap<String, String> serviceDescription;
    private String ssid;

    public WifiHs20OsuProvider() {
        this.friendlyName = new HashMap();
        this.osuIcon = new HashMap();
        this.serviceDescription = new HashMap();
    }

    public WifiHs20OsuProvider(String _ssid, String _bssid, String _hessid, String _osuSsid, HashMap<String, String> _friendlyName, URI _serverUri, AccessProtocol _methodType, HashMap<String, String> _osuIcon, String _NAI, HashMap<String, String> _serviceDescription, Long _seen) {
        this.ssid = _ssid;
        this.hessid = _hessid;
        this.bssid = _bssid;
        this.osuSsid = _osuSsid;
        this.friendlyName = _friendlyName;
        this.serverUri = _serverUri;
        this.methodType = _methodType;
        this.osuIcon = _osuIcon;
        this.NAI = _NAI;
        this.serviceDescription = _serviceDescription;
        this.seen = _seen.longValue();
    }

    public WifiHs20OsuProvider(WifiHs20OsuProvider source) {
        this.friendlyName = new HashMap();
        this.osuIcon = new HashMap();
        this.serviceDescription = new HashMap();
        this.friendlyName.putAll(source.friendlyName);
        this.osuIcon.putAll(source.osuIcon);
        this.serviceDescription.putAll(source.serviceDescription);
        this.serverUri = source.serverUri;
        this.methodType = source.methodType;
        this.NAI = source.NAI;
        this.hessid = source.hessid;
        this.ssid = source.ssid;
        this.bssid = source.bssid;
        this.osuSsid = source.osuSsid;
        this.seen = source.seen;
    }

    private WifiHs20OsuIcon getBestIcon(ArrayList<WifiHs20OsuIcon> icons) {
        if (icons.isEmpty()) {
            return null;
        }
        return (WifiHs20OsuIcon) icons.get(0);
    }

    public HashMap<String, String> getFriendlyNames() {
        return this.friendlyName;
    }

    public String getFriendlyName() {
        return (String) this.friendlyName.get(Locale.getDefault().getISO3Language());
    }

    public String getFriendlyName(String languageCode) {
        return (String) this.friendlyName.get(languageCode);
    }

    public void setFriendlyName(HashMap<String, String> friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void addFriendlyName(String lang, String name) {
        this.friendlyName.put(lang, name);
    }

    public URI getServerUri() {
        return this.serverUri;
    }

    public void setServerUri(URI serverUri) {
        this.serverUri = serverUri;
    }

    public void setMethodType(AccessProtocol methodType) {
        this.methodType = methodType;
    }

    public AccessProtocol getMethodType() {
        return this.methodType;
    }

    public boolean isHavingIcon() {
        if (this.osuIcon.size() > 0) {
            return true;
        }
        return false;
    }

    public HashMap<String, String> getOsuIcons() {
        return this.osuIcon;
    }

    public String getOsuIcon() {
        String iconFileName = (String) this.osuIcon.get(Locale.getDefault().getISO3Language());
        if (iconFileName == null) {
            return (String) this.osuIcon.get("zxx");
        }
        return iconFileName;
    }

    public String getOsuIcon(String languageCode) {
        return (String) this.osuIcon.get(languageCode);
    }

    public void setOsuIcon(HashMap<String, String> osuIcons) {
        this.osuIcon = osuIcons;
    }

    public void addOsuIcon(String lang, String filename) {
        this.osuIcon.put(lang, filename);
    }

    public String getNAI() {
        return this.NAI;
    }

    public String getOsuNAI() {
        return this.NAI;
    }

    public void setNAI(String nAI) {
        this.NAI = nAI;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String nSsid) {
        this.ssid = nSsid;
    }

    public String getBssid() {
        return this.bssid;
    }

    public void setBssid(String nBssid) {
        this.bssid = nBssid;
    }

    public String getHessid() {
        return this.hessid;
    }

    public void setHessid(String nHessid) {
        this.hessid = nHessid;
    }

    public String getOsuSsid() {
        return this.osuSsid;
    }

    public void setOsuSsid(String nOsuSsid) {
        this.osuSsid = nOsuSsid;
    }

    public void setSeen(long nSeen) {
        this.seen = nSeen;
    }

    public void setSeen(String nSeen) {
        try {
            this.seen = Long.parseLong(nSeen, 10);
        } catch (NumberFormatException e) {
            this.seen = 0;
        }
    }

    public long getSeen() {
        return this.seen;
    }

    public HashMap<String, String> getServiceDescription() {
        return this.serviceDescription;
    }

    public String getServiceDescription(String languageCode) {
        return (String) this.serviceDescription.get(languageCode);
    }

    public void setServiceDescription(HashMap<String, String> serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public void addServiceDescription(String lang, String value) {
        this.serviceDescription.put(lang, value);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        StringBuffer append = sb.append("FriendlyName: ").append(this.friendlyName == null ? none : this.friendlyName).append(", ServerUri: ").append(this.serverUri == null ? none : this.serverUri).append(", MethodType: ").append(this.methodType == null ? none : this.methodType.name()).append(", Icon: ").append(this.osuIcon == null ? none : this.osuIcon).append(", ssid: ").append(this.ssid == null ? none : this.ssid).append(", hessid: ").append(this.hessid == null ? none : this.hessid).append(", bssid: ").append(this.bssid == null ? none : this.bssid).append(", osuSsid: ").append(this.osuSsid == null ? none : this.osuSsid).append(", NAI: ").append(this.NAI == null ? none : this.NAI).append(", ServiceDescription: ");
        if (this.serviceDescription != null) {
            none = this.serviceDescription;
        }
        append.append(none);
        return sb.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putSerializable("freindly_name", this.friendlyName);
        b.putSerializable("osu_icon", this.osuIcon);
        b.putSerializable("service_description", this.serviceDescription);
        dest.writeBundle(b);
        dest.writeString(this.serverUri.toString());
        dest.writeString(this.methodType.name());
        dest.writeString(this.NAI);
        dest.writeString(this.ssid);
        dest.writeString(this.bssid);
        dest.writeString(this.hessid);
        dest.writeString(this.osuSsid);
        dest.writeLong(this.seen);
    }
}
