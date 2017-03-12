package android.net;

import android.os.Debug;
import android.os.Process;
import android.util.Log;
import com.google.android.collect.Lists;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

public class KnoxVpnProxySelector extends ProxySelector {
    private static final boolean DBG;
    private static final String PROXY = "PROXY ";
    private static final String SOCKS = "SOCKS ";
    private static final String TAG = "KnoxVpnProxySelector";
    private static IConnectivityManager mConnectivityManager = null;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.net.IConnectivityManager getConnectivityServiceInstance() {
        /*
        r6 = this;
        r2 = android.os.Binder.clearCallingIdentity();
        r4 = "connectivity";
        r0 = android.os.ServiceManager.getService(r4);	 Catch:{ Throwable -> 0x0018 }
        if (r0 == 0) goto L_0x0012;
    L_0x000c:
        r4 = android.net.IConnectivityManager.Stub.asInterface(r0);	 Catch:{ Throwable -> 0x0018 }
        mConnectivityManager = r4;	 Catch:{ Throwable -> 0x0018 }
    L_0x0012:
        android.os.Binder.restoreCallingIdentity(r2);
    L_0x0015:
        r4 = mConnectivityManager;
        return r4;
    L_0x0018:
        r1 = move-exception;
        r4 = "KnoxVpnProxySelector";
        r5 = "Exception occured while trying to get the connectivityservice instance in knox vpn proxy selector";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0024 }
        android.os.Binder.restoreCallingIdentity(r2);
        goto L_0x0015;
    L_0x0024:
        r4 = move-exception;
        android.os.Binder.restoreCallingIdentity(r2);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.KnoxVpnProxySelector.getConnectivityServiceInstance():android.net.IConnectivityManager");
    }

    public List<Proxy> select(URI uri) {
        Log.d(TAG, "selection of proxy is being reached for the caller " + Process.myUid());
        List<Proxy> ret = Lists.newArrayList();
        try {
            String urlString = uri.toURL().toString();
            if (DBG) {
                Log.d(TAG, "pac url being recieved is " + urlString + "for the caller " + Process.myUid());
            }
            String[] proxyInfo = getConnectivityServiceInstance().getProxyInfoForUid(Process.myUid());
            String host = proxyInfo[0];
            if (DBG) {
                Log.d(TAG, "host value is " + host + "for caller " + Process.myUid());
            }
            String port = proxyInfo[1];
            if (DBG) {
                Log.d(TAG, "port value is " + port + "for caller " + Process.myUid());
            }
            if (!(host == null || port == null)) {
                Proxy proxy = new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(host, Integer.parseInt(port)));
                if (proxy != null) {
                    Log.d(TAG, "valid knox vpn proxy is added for caller" + Process.myUid());
                    ret.add(proxy);
                }
            }
            if (ret.size() == 0) {
                Log.d(TAG, "in-valid knox vpn proxy is added for caller" + Process.myUid());
                ret.add(Proxy.NO_PROXY);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occured for the caller " + Process.myUid());
            if (ret.size() == 0) {
                Log.d(TAG, "in-valid knox vpn proxy is added for caller during exception " + Process.myUid());
                ret.add(Proxy.NO_PROXY);
            }
        }
        return ret;
    }

    private static List<Proxy> parseResponse(String response) {
        String[] split = response.split(";");
        List<Proxy> ret = Lists.newArrayList();
        for (String s : split) {
            Log.d(TAG, "s value is " + s);
            String trimmed = s.trim();
            if (trimmed.equals("DIRECT")) {
                ret.add(Proxy.NO_PROXY);
            } else if (trimmed.startsWith(PROXY)) {
                proxy = proxyFromHostPort(Type.HTTP, trimmed.substring(PROXY.length()));
                if (proxy != null) {
                    ret.add(proxy);
                }
            } else if (trimmed.startsWith(SOCKS)) {
                proxy = proxyFromHostPort(Type.SOCKS, trimmed.substring(SOCKS.length()));
                if (proxy != null) {
                    ret.add(proxy);
                }
            }
        }
        if (ret.size() == 0) {
            Log.d(TAG, "ret value is 0");
            ret.add(Proxy.NO_PROXY);
        }
        return ret;
    }

    private static Proxy proxyFromHostPort(Type type, String hostPortString) {
        RuntimeException e;
        try {
            String[] hostPort = hostPortString.split(":");
            String host = hostPort[0];
            Log.d(TAG, "host value is " + host);
            int port = Integer.parseInt(hostPort[1]);
            Log.d(TAG, "port value is " + port);
            return new Proxy(type, InetSocketAddress.createUnresolved(host, port));
        } catch (NumberFormatException e2) {
            e = e2;
        } catch (ArrayIndexOutOfBoundsException e3) {
            e = e3;
        }
        Log.d(TAG, "Unable to parse proxy " + hostPortString + " " + e);
        return null;
    }

    public void connectFailed(URI uri, SocketAddress address, IOException failure) {
    }
}
