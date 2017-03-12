package android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.Binder;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.text.TextUtils;
import com.android.internal.R;

public class NtpTrustedTime implements TrustedTime {
    private static final boolean LOGD = true;
    private static final String TAG = "NtpTrustedTime";
    private static Context sContext;
    private static NtpTrustedTime sSingleton;
    private ConnectivityManager mCM;
    private long mCachedNtpCertainty;
    private long mCachedNtpElapsedRealtime;
    private long mCachedNtpTime;
    private boolean mHasCache;
    private int mRetryStep = 0;
    private String mServer;
    private String mServer2 = "";
    private String mServer3 = "";
    private String mServer4 = "";
    private long mTimeout;

    private NtpTrustedTime(String server, long timeout) {
        Log.d(TAG, "creating NtpTrustedTime using " + server);
        this.mServer = server;
        this.mTimeout = timeout;
    }

    private NtpTrustedTime(String server, String server2, long timeout) {
        Log.d(TAG, "creating NtpTrustedTime using server1:" + server + " server2:" + server2);
        this.mServer = server;
        this.mServer2 = server2;
        this.mTimeout = timeout;
    }

    private NtpTrustedTime(String server, String server2, String server3, long timeout) {
        Log.d(TAG, "creating NtpTrustedTime using server1:" + server + " server2:" + server2 + " server3:" + server3);
        this.mServer = server;
        this.mServer2 = server2;
        this.mServer3 = server3;
        this.mTimeout = timeout;
    }

    private NtpTrustedTime(String server, String server2, String server3, String server4, long timeout) {
        Log.d(TAG, "creating NtpTrustedTime using server1:" + server + " server2:" + server2 + " server3:" + server3 + " server4:" + server4);
        this.mServer = server;
        this.mServer2 = server2;
        this.mServer3 = server3;
        this.mServer4 = server4;
        this.mTimeout = timeout;
    }

    private static boolean isCHNOrHKTW() {
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        return "CHC".equals(salesCode) || "CHU".equals(salesCode) || "CHM".equals(salesCode) || "CTC".equals(salesCode) || "BRI".equals(salesCode) || "TGY".equals(salesCode) || "CWT".equals(salesCode) || "FET".equals(salesCode) || "TWM".equals(salesCode) || "CHZ".equals(salesCode) || "CHN".equals(salesCode);
    }

    public static synchronized NtpTrustedTime getInstance(Context context) {
        NtpTrustedTime ntpTrustedTime;
        synchronized (NtpTrustedTime.class) {
            if (sSingleton == null) {
                String server;
                Resources res = context.getResources();
                ContentResolver resolver = context.getContentResolver();
                String defaultServer = res.getString(R.string.config_ntpServer);
                long defaultTimeout = (long) res.getInteger(R.integer.config_ntpTimeout);
                String secureServer = Global.getString(resolver, "ntp_server");
                long timeout = Global.getLong(resolver, "ntp_timeout", defaultTimeout);
                if (secureServer != null) {
                    server = secureServer;
                } else {
                    server = defaultServer;
                }
                String server2 = res.getString(R.string.config_ntpServer2);
                String server3 = res.getString(R.string.config_ntpServer3);
                String server4 = res.getString(R.string.config_ntpServer4);
                if (!isCHNOrHKTW() || TextUtils.isEmpty(server2)) {
                    sSingleton = new NtpTrustedTime(server, timeout);
                } else {
                    Log.d(TAG, "Reduce timeout value because of china weak network. Timeout = 5000");
                    if (TextUtils.isEmpty(server3)) {
                        sSingleton = new NtpTrustedTime(server, server2, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                    } else if (TextUtils.isEmpty(server4)) {
                        sSingleton = new NtpTrustedTime(server, server2, server3, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                    } else {
                        sSingleton = new NtpTrustedTime(server, server2, server3, server4, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                    }
                }
                sContext = context;
            }
            ntpTrustedTime = sSingleton;
        }
        return ntpTrustedTime;
    }

    public boolean forceRefresh() {
        if (TextUtils.isEmpty(this.mServer)) {
            return false;
        }
        synchronized (this) {
            if (this.mCM == null) {
                this.mCM = (ConnectivityManager) sContext.getSystemService("connectivity");
            }
        }
        NetworkInfo ni = this.mCM == null ? null : this.mCM.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.d(TAG, "forceRefresh: no connectivity");
            return false;
        }
        int pco = SystemProperties.getInt("ril.pco.default", 0);
        if (pco == 2) {
            Log.d(TAG, "forceRefresh: ril.pco.default (" + pco + ")");
            return false;
        }
        Log.d(TAG, "forceRefresh() from cache miss");
        String labtest = SystemProperties.get("ril.ntptrustedtime");
        if ("off".equals(labtest)) {
            Log.d(TAG, "forceRefresh: ril.ntptrustedtime (" + labtest + ")");
            return false;
        }
        SntpClient client = new SntpClient();
        switch (this.mRetryStep) {
            case 0:
                if (!client.requestTime(this.mServer, (int) this.mTimeout)) {
                    if (!isCHNOrHKTW()) {
                        this.mRetryStep = 4;
                        break;
                    }
                    this.mRetryStep = 1;
                    break;
                }
                this.mHasCache = true;
                this.mCachedNtpTime = client.getNtpTime();
                this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
                this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
                Log.d(TAG, "requestTime Success from server:" + this.mServer + " mCachedNtpTime : " + this.mCachedNtpTime + " mCachedNtpElapsedRealtime : " + this.mCachedNtpElapsedRealtime + " mCachedNtpCertainty : " + this.mCachedNtpCertainty);
                return true;
            case 1:
                if (TextUtils.isEmpty(this.mServer2) || !client.requestTime(this.mServer2, (int) this.mTimeout)) {
                    this.mRetryStep = 2;
                    break;
                }
                this.mHasCache = true;
                this.mCachedNtpTime = client.getNtpTime();
                this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
                this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
                Log.d(TAG, "requestTime Success from server2:" + this.mServer2 + " mCachedNtpTime : " + this.mCachedNtpTime + " mCachedNtpElapsedRealtime : " + this.mCachedNtpElapsedRealtime + " mCachedNtpCertainty : " + this.mCachedNtpCertainty);
                return true;
            case 2:
                if (TextUtils.isEmpty(this.mServer3) || !client.requestTime(this.mServer3, (int) this.mTimeout)) {
                    this.mRetryStep = 3;
                    break;
                }
                this.mHasCache = true;
                this.mCachedNtpTime = client.getNtpTime();
                this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
                this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
                Log.d(TAG, "requestTime Success from server3:" + this.mServer3 + " mCachedNtpTime : " + this.mCachedNtpTime + " mCachedNtpElapsedRealtime : " + this.mCachedNtpElapsedRealtime + " mCachedNtpCertainty : " + this.mCachedNtpCertainty);
                return true;
                break;
            case 3:
                if (TextUtils.isEmpty(this.mServer4) || !client.requestTime(this.mServer4, (int) this.mTimeout)) {
                    this.mRetryStep = 0;
                    break;
                }
                this.mHasCache = true;
                this.mCachedNtpTime = client.getNtpTime();
                this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
                this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
                Log.d(TAG, "requestTime Success from server4:" + this.mServer4 + " mCachedNtpTime : " + this.mCachedNtpTime + " mCachedNtpElapsedRealtime : " + this.mCachedNtpElapsedRealtime + " mCachedNtpCertainty : " + this.mCachedNtpCertainty);
                return true;
                break;
            case 4:
                if (!client.requestTime("north-america.pool.ntp.org", (int) this.mTimeout)) {
                    this.mRetryStep = 0;
                    break;
                }
                this.mHasCache = true;
                this.mCachedNtpTime = client.getNtpTime();
                this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
                this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
                Log.d(TAG, "requestTime Success from server:north-america.pool.ntp.org mCachedNtpTime : " + this.mCachedNtpTime + " mCachedNtpElapsedRealtime : " + this.mCachedNtpElapsedRealtime + " mCachedNtpCertainty : " + this.mCachedNtpCertainty);
                return true;
            default:
                this.mRetryStep = 0;
                break;
        }
        Log.d(TAG, "forceRefresh Fail.");
        return false;
    }

    public boolean hasCache() {
        return this.mHasCache;
    }

    public long getCacheAge() {
        if (this.mHasCache) {
            return SystemClock.elapsedRealtime() - this.mCachedNtpElapsedRealtime;
        }
        return Long.MAX_VALUE;
    }

    public long getCacheCertainty() {
        if (this.mHasCache) {
            return this.mCachedNtpCertainty;
        }
        return Long.MAX_VALUE;
    }

    public long currentTimeMillis() {
        if (this.mHasCache) {
            Log.d(TAG, "currentTimeMillis() cache hit");
            return this.mCachedNtpTime + getCacheAge();
        }
        throw new IllegalStateException("Missing authoritative time source");
    }

    public long getCachedNtpTime() {
        Log.d(TAG, "getCachedNtpTime() cache hit");
        return this.mCachedNtpTime;
    }

    public long getCachedNtpTimeReference() {
        return this.mCachedNtpElapsedRealtime;
    }

    public boolean setNtpInfoInternal(Context context, String server, long timeout) {
        if (Binder.getCallingPid() != Process.myPid()) {
            return false;
        }
        Log.v(TAG, "setNtpInfoInternal server " + server + " timeout " + timeout);
        Resources res = context.getResources();
        ContentResolver resolver = context.getContentResolver();
        if (server == null) {
            String defaultServer = res.getString(R.string.config_ntpServer);
            String secureServer = Global.getString(resolver, "ntp_server");
            if (secureServer == null) {
                secureServer = defaultServer;
            }
            this.mServer = secureServer;
            this.mTimeout = Global.getLong(resolver, "ntp_timeout", (long) res.getInteger(R.integer.config_ntpTimeout));
        } else if (timeout == 0) {
            this.mTimeout = Global.getLong(resolver, "ntp_timeout", (long) res.getInteger(R.integer.config_ntpTimeout));
            this.mServer = server;
        } else {
            this.mServer = server;
            this.mTimeout = timeout;
        }
        return true;
    }
}
