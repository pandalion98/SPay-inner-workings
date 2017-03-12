package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import java.util.Locale;

public class ProxyInfo implements Parcelable {
    public static final Creator<ProxyInfo> CREATOR = new Creator<ProxyInfo>() {
        public ProxyInfo createFromParcel(Parcel in) {
            String host = null;
            int port = 0;
            String username = null;
            String password = null;
            int knoxVpnProfile = 0;
            if (in.readByte() != (byte) 0) {
                Uri url = (Uri) Uri.CREATOR.createFromParcel(in);
                int localPort = in.readInt();
                knoxVpnProfile = in.readInt();
                ProxyInfo proxyInfo = new ProxyInfo(url, localPort);
                proxyInfo.setKnoxVpnProfile(knoxVpnProfile);
                return proxyInfo;
            }
            ProxyInfo proxyProperties;
            if (in.readByte() != (byte) 0) {
                host = in.readString();
                port = in.readInt();
                knoxVpnProfile = in.readInt();
                if (in.readByte() != (byte) 0) {
                    username = in.readString();
                    password = in.readString();
                    knoxVpnProfile = in.readInt();
                }
            }
            String exclList = in.readString();
            String[] parsedExclList = in.readStringArray();
            if (username == null || username.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                ProxyInfo proxyInfo2 = new ProxyInfo(host, port, exclList, parsedExclList);
                proxyInfo2.setKnoxVpnProfile(knoxVpnProfile);
            } else {
                proxyProperties = new ProxyInfo(host, port, username, password, exclList, parsedExclList);
                proxyProperties.setKnoxVpnProfile(knoxVpnProfile);
            }
            return proxyProperties;
        }

        public ProxyInfo[] newArray(int size) {
            return new ProxyInfo[size];
        }
    };
    public static final String LOCAL_EXCL_LIST = "";
    public static final String LOCAL_HOST = "localhost";
    public static final int LOCAL_PORT = -1;
    private String mExclusionList;
    private String mHost;
    private int mKnoxVpnProfile;
    private Uri mPacFileUrl;
    private String[] mParsedExclusionList;
    private String mPassword;
    private int mPort;
    private String mUsername;

    public static ProxyInfo buildDirectProxy(String host, int port) {
        return new ProxyInfo(host, port, null);
    }

    public static ProxyInfo buildDirectProxy(String host, int port, List<String> exclList) {
        String[] array = (String[]) exclList.toArray(new String[exclList.size()]);
        return new ProxyInfo(host, port, TextUtils.join(",", array), array);
    }

    public static ProxyInfo buildPacProxy(Uri pacUri) {
        return new ProxyInfo(pacUri);
    }

    public ProxyInfo(String host, int port, String exclList) {
        this.mKnoxVpnProfile = 0;
        this.mHost = host;
        this.mPort = port;
        setExclusionList(exclList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(String host, int port, String username, String password, String exclList) {
        this.mKnoxVpnProfile = 0;
        this.mHost = host;
        this.mPort = port;
        this.mUsername = username;
        this.mPassword = password;
        setExclusionList(exclList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(Uri pacFileUrl) {
        this.mKnoxVpnProfile = 0;
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        setExclusionList(LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    public ProxyInfo(String pacFileUrl) {
        this.mKnoxVpnProfile = 0;
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        setExclusionList(LOCAL_EXCL_LIST);
        this.mPacFileUrl = Uri.parse(pacFileUrl);
    }

    public ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        this.mKnoxVpnProfile = 0;
        this.mHost = LOCAL_HOST;
        this.mPort = localProxyPort;
        setExclusionList(LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    private ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        this.mKnoxVpnProfile = 0;
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    private ProxyInfo(String host, int port, String username, String password, String exclList, String[] parsedExclList) {
        this.mKnoxVpnProfile = 0;
        this.mHost = host;
        this.mPort = port;
        this.mUsername = username;
        this.mPassword = password;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(ProxyInfo source) {
        this.mKnoxVpnProfile = 0;
        if (source != null) {
            this.mHost = source.getHost();
            this.mPort = source.getPort();
            this.mUsername = source.getUsername();
            this.mPassword = source.getPassword();
            this.mPacFileUrl = source.mPacFileUrl;
            this.mExclusionList = source.getExclusionListAsString();
            this.mParsedExclusionList = source.mParsedExclusionList;
            return;
        }
        this.mPacFileUrl = Uri.EMPTY;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            return new InetSocketAddress(this.mHost, this.mPort);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Uri getPacFileUrl() {
        return this.mPacFileUrl;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getPort() {
        return this.mPort;
    }

    public String getUsername() {
        return this.mUsername;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public void setKnoxVpnProfile(int knoxVpnProfile) {
        this.mKnoxVpnProfile = knoxVpnProfile;
    }

    public int getKnoxVpnProfile() {
        return this.mKnoxVpnProfile;
    }

    public String[] getExclusionList() {
        return this.mParsedExclusionList;
    }

    public String getExclusionListAsString() {
        return this.mExclusionList;
    }

    private void setExclusionList(String exclusionList) {
        this.mExclusionList = exclusionList;
        if (this.mExclusionList == null) {
            this.mParsedExclusionList = new String[0];
        } else {
            this.mParsedExclusionList = exclusionList.toLowerCase(Locale.ROOT).split(",");
        }
    }

    public boolean isValid() {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            return true;
        }
        return Proxy.validate(this.mHost == null ? LOCAL_EXCL_LIST : this.mHost, this.mPort == 0 ? LOCAL_EXCL_LIST : Integer.toString(this.mPort), this.mExclusionList == null ? LOCAL_EXCL_LIST : this.mExclusionList) == 0;
    }

    public Proxy makeProxy() {
        Proxy proxy = Proxy.NO_PROXY;
        if (this.mHost == null) {
            return proxy;
        }
        try {
            return new Proxy(Type.HTTP, new InetSocketAddress(this.mHost, this.mPort));
        } catch (IllegalArgumentException e) {
            return proxy;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(this.mPacFileUrl);
        }
        if (this.mHost != null) {
            sb.append("[");
            sb.append(this.mHost);
            sb.append("] ");
            sb.append(Integer.toString(this.mPort));
            sb.append("[username : ");
            sb.append(this.mUsername);
            sb.append("] ");
            sb.append("[pw : ");
            sb.append(this.mPassword);
            sb.append("]");
            if (this.mExclusionList != null) {
                sb.append(" xl=").append(this.mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (!(o instanceof ProxyInfo)) {
            return false;
        }
        ProxyInfo p = (ProxyInfo) o;
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            if (!(this.mPacFileUrl.equals(p.getPacFileUrl()) && this.mPort == p.mPort)) {
                z = false;
            }
            return z;
        } else if (!Uri.EMPTY.equals(p.mPacFileUrl)) {
            return false;
        } else {
            if (this.mExclusionList != null && !this.mExclusionList.equals(p.getExclusionListAsString())) {
                return false;
            }
            if (this.mHost != null && p.getHost() != null && !this.mHost.equals(p.getHost())) {
                return false;
            }
            if (this.mHost != null && p.mHost == null) {
                return false;
            }
            if ((this.mHost == null && p.mHost != null) || this.mPort != p.mPort) {
                return false;
            }
            if (this.mUsername != null && p.getUsername() != null && !this.mUsername.equals(p.getUsername())) {
                return false;
            }
            if (this.mUsername != null && p.mUsername == null) {
                return false;
            }
            if (this.mUsername == null && p.mUsername != null) {
                return false;
            }
            if (this.mPassword != null && p.getPassword() != null && !this.mPassword.equals(p.getPassword())) {
                return false;
            }
            if (this.mPassword != null && p.mPassword == null) {
                return false;
            }
            if (this.mPassword != null || p.mPassword == null) {
                return true;
            }
            return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.mHost == null ? 0 : this.mHost.hashCode();
        if (this.mExclusionList != null) {
            i = this.mExclusionList.hashCode();
        }
        return (hashCode + i) + this.mPort;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (Uri.EMPTY.equals(this.mPacFileUrl)) {
            dest.writeByte((byte) 0);
            if (this.mHost != null) {
                dest.writeByte((byte) 1);
                dest.writeString(this.mHost);
                dest.writeInt(this.mPort);
                dest.writeInt(this.mKnoxVpnProfile);
                if (this.mUsername != null) {
                    dest.writeByte((byte) 1);
                    dest.writeString(this.mUsername);
                    dest.writeString(this.mPassword);
                    dest.writeInt(this.mKnoxVpnProfile);
                } else {
                    dest.writeByte((byte) 0);
                }
            } else {
                dest.writeByte((byte) 0);
            }
            dest.writeString(this.mExclusionList);
            dest.writeStringArray(this.mParsedExclusionList);
            return;
        }
        dest.writeByte((byte) 1);
        this.mPacFileUrl.writeToParcel(dest, 0);
        dest.writeInt(this.mPort);
        dest.writeInt(this.mKnoxVpnProfile);
    }
}
