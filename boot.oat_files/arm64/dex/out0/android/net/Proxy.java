package android.net;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Proxy {
    private static final boolean DBG;
    private static final Pattern EXCLLIST_PATTERN = Pattern.compile(EXCLLIST_REGEXP);
    private static final String EXCLLIST_REGEXP = "^$|^[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*(,[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*)*$";
    private static final String EXCL_REGEX = "[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*";
    public static final String EXTRA_PROXY_INFO = "android.intent.extra.PROXY_INFO";
    private static final Pattern HOSTNAME_PATTERN = Pattern.compile(HOSTNAME_REGEXP);
    private static final String HOSTNAME_REGEXP = "^$|^[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*$";
    private static final String NAME_IP_REGEX = "[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*";
    public static final String PROXY_CHANGE_ACTION = "android.intent.action.PROXY_CHANGE";
    public static final int PROXY_EXCLLIST_INVALID = 5;
    public static final int PROXY_HOSTNAME_EMPTY = 1;
    public static final int PROXY_HOSTNAME_INVALID = 2;
    public static final int PROXY_PORT_EMPTY = 3;
    public static final int PROXY_PORT_INVALID = 4;
    public static final int PROXY_VALID = 0;
    private static final String TAG = "Proxy";
    private static ConnectivityManager sConnectivityManager = null;
    private static final ProxySelector sDefaultProxySelector = ProxySelector.getDefault();
    private static final String setAuthForPacProfile = "knox.vpn.pac.auth";
    private static int setKnoxProxySelector = 0;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public static final java.net.Proxy getProxy(Context ctx, String url) {
        String host = ProxyInfo.LOCAL_EXCL_LIST;
        if (!(url == null || isLocalHost(host))) {
            List<java.net.Proxy> proxyList = ProxySelector.getDefault().select(URI.create(url));
            if (proxyList.size() > 0) {
                return (java.net.Proxy) proxyList.get(0);
            }
        }
        return java.net.Proxy.NO_PROXY;
    }

    public static final String getHost(Context ctx) {
        java.net.Proxy proxy = getProxy(ctx, null);
        if (proxy == java.net.Proxy.NO_PROXY) {
            return null;
        }
        try {
            return ((InetSocketAddress) proxy.address()).getHostName();
        } catch (Exception e) {
            return null;
        }
    }

    public static final int getPort(Context ctx) {
        java.net.Proxy proxy = getProxy(ctx, null);
        if (proxy == java.net.Proxy.NO_PROXY) {
            return -1;
        }
        try {
            return ((InetSocketAddress) proxy.address()).getPort();
        } catch (Exception e) {
            return -1;
        }
    }

    public static final String getDefaultHost() {
        String host = System.getProperty("http.proxyHost");
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        return host;
    }

    public static final int getDefaultPort() {
        int i = -1;
        if (getDefaultHost() != null) {
            try {
                i = Integer.parseInt(System.getProperty("http.proxyPort"));
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    private static final boolean isLocalHost(String host) {
        if (host == null || host == null) {
            return false;
        }
        try {
            if (host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST)) {
                return true;
            }
            if (NetworkUtils.numericToInetAddress(host).isLoopbackAddress()) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static int validate(String hostname, String port, String exclList) {
        Matcher match = HOSTNAME_PATTERN.matcher(hostname);
        Matcher listMatch = EXCLLIST_PATTERN.matcher(exclList);
        if (!match.matches()) {
            return 2;
        }
        if (!listMatch.matches()) {
            return 5;
        }
        if (hostname.length() > 0 && port.length() == 0) {
            return 3;
        }
        if (port.length() > 0) {
            if (hostname.length() == 0) {
                return 1;
            }
            try {
                int portVal = Integer.parseInt(port);
                if (portVal <= 0 || portVal > 65535) {
                    return 4;
                }
            } catch (NumberFormatException e) {
                return 4;
            }
        }
        return 0;
    }

    public static final void setHttpProxySystemProperty(ProxyInfo p) {
        String host = null;
        String port = null;
        String username = null;
        String password = null;
        String exclList = null;
        Uri pacFileUrl = Uri.EMPTY;
        if (p != null) {
            host = p.getHost();
            port = Integer.toString(p.getPort());
            username = p.getUsername();
            password = p.getPassword();
            exclList = p.getExclusionListAsString();
            pacFileUrl = p.getPacFileUrl();
            setKnoxProxySelector = p.getKnoxVpnProfile();
        }
        if (username == null || username.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            setHttpProxySystemProperty(host, port, exclList, pacFileUrl);
        } else {
            setHttpProxySystemProperty(host, port, username, password, exclList, pacFileUrl);
        }
    }

    public static final void setHttpProxySystemProperty(String host, String port, String exclList, Uri pacFileUrl) {
        if (DBG) {
            Log.d(TAG, "setHttpProxySystemProperty for uid " + Process.myUid() + "The hose value is " + host + "The port value is " + port);
        }
        if (exclList != null) {
            exclList = exclList.replace(",", "|");
        }
        if (host != null) {
            System.setProperty("http.proxyHost", host);
            System.setProperty("https.proxyHost", host);
            System.setProperty("ftp.proxyHost", host);
        } else {
            System.clearProperty("http.proxyHost");
            System.clearProperty("https.proxyHost");
            System.clearProperty("ftp.proxyHost");
        }
        if (port != null) {
            System.setProperty("http.proxyPort", port);
            System.setProperty("https.proxyPort", port);
            System.setProperty("ftp.proxyPort", port);
        } else {
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyPort");
            System.clearProperty("ftp.proxyPort");
        }
        if (exclList != null) {
            System.setProperty("http.nonProxyHosts", exclList);
            System.setProperty("https.nonProxyHosts", exclList);
        } else {
            System.clearProperty("http.nonProxyHosts");
            System.clearProperty("https.nonProxyHosts");
        }
        System.clearProperty("http.proxyUser");
        System.clearProperty("http.proxyPassword");
        if (Uri.EMPTY.equals(pacFileUrl)) {
            ProxySelector.setDefault(sDefaultProxySelector);
            System.clearProperty(setAuthForPacProfile);
        } else if (setKnoxProxySelector == 1) {
            ProxySelector.setDefault(new KnoxVpnProxySelector());
            System.setProperty(setAuthForPacProfile, WifiEnterpriseConfig.ENGINE_ENABLE);
        } else {
            ProxySelector.setDefault(new PacProxySelector());
            System.clearProperty(setAuthForPacProfile);
        }
    }

    public static final void setHttpProxySystemProperty(String host, String port, String username, String password, String exclList, Uri pacFileUrl) {
        if (DBG) {
            Log.d(TAG, "setHttpProxySystemProperty for uid with auth" + Process.myUid() + "The hose value is " + host + "The port value is " + port);
        }
        if (exclList != null) {
            exclList = exclList.replace(",", "|");
        }
        if (host != null) {
            System.setProperty("http.proxyHost", host);
            System.setProperty("https.proxyHost", host);
            System.setProperty("ftp.proxyHost", host);
        } else {
            System.clearProperty("http.proxyHost");
            System.clearProperty("https.proxyHost");
            System.clearProperty("ftp.proxyHost");
        }
        if (port != null) {
            System.setProperty("http.proxyPort", port);
            System.setProperty("https.proxyPort", port);
            System.setProperty("ftp.proxyPort", port);
        } else {
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyPort");
            System.clearProperty("ftp.proxyPort");
        }
        if (username != null) {
            System.setProperty("http.proxyUser", username);
        } else {
            System.clearProperty("http.proxyUser");
        }
        if (password != null) {
            System.setProperty("http.proxyPassword", password);
        } else {
            System.clearProperty("http.proxyPassword");
        }
        if (exclList != null) {
            System.setProperty("http.nonProxyHosts", exclList);
            System.setProperty("https.nonProxyHosts", exclList);
        } else {
            System.clearProperty("http.nonProxyHosts");
            System.clearProperty("https.nonProxyHosts");
        }
        if (Uri.EMPTY.equals(pacFileUrl)) {
            ProxySelector.setDefault(sDefaultProxySelector);
            System.clearProperty(setAuthForPacProfile);
        } else if (setKnoxProxySelector == 1) {
            ProxySelector.setDefault(new KnoxVpnProxySelector());
            System.setProperty(setAuthForPacProfile, WifiEnterpriseConfig.ENGINE_ENABLE);
        } else {
            ProxySelector.setDefault(new PacProxySelector());
            System.clearProperty(setAuthForPacProfile);
        }
    }
}
