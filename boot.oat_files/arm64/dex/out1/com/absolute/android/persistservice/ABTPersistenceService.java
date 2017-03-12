package com.absolute.android.persistservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.SyncStateContract.Columns;
import android.util.Log;
import com.absolute.android.crypt.Crypt;
import com.absolute.android.logutil.LogUtil;
import com.absolute.android.persistence.ABTPersistenceManager;
import com.absolute.android.persistence.AppInfoProperties;
import com.absolute.android.persistence.AppProfile;
import com.absolute.android.persistence.IABTDownloadReceiver;
import com.absolute.android.persistence.IABTGetAppInfoReceiver;
import com.absolute.android.persistence.IABTPersistedFile;
import com.absolute.android.persistence.IABTPersistence.Stub;
import com.absolute.android.persistence.IABTPersistenceLog;
import com.absolute.android.persistence.IABTPing;
import com.absolute.android.persistence.IABTResultReceiver;
import com.absolute.android.persistence.MethodReturnValue;
import com.absolute.android.persistence.MethodSpec;
import com.absolute.android.utils.CommandUtil;
import com.absolute.android.utils.DeviceUtil;
import com.absolute.android.utils.ExceptionUtil;
import com.absolute.android.utils.FileUtil;
import com.android.internal.os.SMProviderContract;
import com.android.internal.telephony.SmsConstants;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;

public class ABTPersistenceService extends Stub {
    static final /* synthetic */ boolean b;
    private static String c = "/mnt/pia";
    private static String d = "AbsoluteSoftware";
    private static String e = "package_verifier_enable";
    boolean a = false;
    private x f;
    private Context g;
    private Hashtable h;
    private v i;
    private k j;
    private k k;
    private k l;
    private j m;
    private j n;
    private j o;
    private i p;
    private d q;
    private c r;
    private HashSet s;
    private l t;
    private Hashtable u;
    private Object v;
    private Object w;
    private y x;

    static {
        boolean z;
        if (ABTPersistenceService.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        b = z;
    }

    public int getState() {
        g();
        return this.f.a();
    }

    public void setState(int i) {
        if (i != 1 && i != 2 && i != 3) {
            throw new IllegalArgumentException("The newState is not a valid state");
        } else if (g()) {
            int a = this.f.a();
            this.f.a(i);
            if (a != i) {
                this.i.c("Persistence state was changed from " + ABTPersistenceManager.stateToString(a) + " to " + ABTPersistenceManager.stateToString(i));
                if (i == 1) {
                    a(false);
                }
            }
        }
    }

    public int getVersion() {
        g();
        return 3136;
    }

    public IABTPersistenceLog getLog(String str) {
        if (!g()) {
            return null;
        }
        IABTPersistenceLog adVar;
        synchronized (this) {
            v vVar;
            v vVar2 = (v) this.u.get(str);
            if (vVar2 == null) {
                vVar2 = new v(this.g, str, this.x);
                this.u.put(str, vVar2);
                vVar = vVar2;
            } else {
                vVar = vVar2;
            }
            adVar = new ad(vVar);
        }
        return adVar;
    }

    public synchronized void install(AppProfile appProfile, String str, IABTResultReceiver iABTResultReceiver) {
        if (appProfile == null) {
            throw new NullPointerException("AppProfile argument is null");
        }
        String packageName = appProfile.getPackageName();
        int version = appProfile.getVersion();
        if (version <= 0) {
            String str2 = "Invalid version number: " + version + " specified in AppProfile for package name: " + packageName;
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(str2);
            this.i.a("Install failed: " + str2, null);
            throw illegalArgumentException;
        }
        Object obj;
        if (str == null || str.length() == 0) {
            str2 = appProfile.getUpdateUrl();
            if (str2 == null || str2.length() == 0) {
                str2 = "Installation of application failed because no APK file nor URL was specified.";
                illegalArgumentException = new IllegalArgumentException(str2);
                this.i.a(str2, null);
                throw illegalArgumentException;
            }
            obj = 1;
            str2 = str;
        } else {
            File file = new File(str);
            if (!file.exists()) {
                String str3 = a() + this.g.getPackageManager().getPackagesForUid(Binder.getCallingUid())[0] + File.separatorChar + str;
                file = new File(str3);
                if (file.exists()) {
                    str = str3;
                } else {
                    str2 = "Installation of application APK failed because the file does not exist at " + str + " or " + str3;
                    illegalArgumentException = new IllegalArgumentException(str2);
                    this.i.a(str2, null);
                    throw illegalArgumentException;
                }
            }
            if (file.length() == 0) {
                str2 = "Installation of application APK failed because the file is empty.";
                illegalArgumentException = new IllegalArgumentException(str2);
                this.i.a(str2, null);
                throw illegalArgumentException;
            }
            obj = null;
            str2 = str;
        }
        if (g()) {
            if (!this.j.c(packageName)) {
                this.j.a(appProfile, str2);
            }
            if (obj == null && appProfile.getIsPersisted()) {
                str2 = a(packageName, version, str2, true);
            }
            this.k.a(appProfile, str2);
            this.j.a(packageName, 0);
            if (obj == 1) {
                a(packageName);
            } else {
                b(packageName);
            }
            a(appProfile.getPackageName(), iABTResultReceiver, 0);
        }
    }

    public synchronized void uninstall(String str, boolean z, IABTResultReceiver iABTResultReceiver) {
        if (str == null) {
            throw new NullPointerException("Package name argument is null");
        } else if (g()) {
            if (this.j.b(str) == null) {
                try {
                    this.j.a(new AppProfile(str, this.g.getPackageManager().getPackageInfo(str, 0).versionCode, "", "", "", 0, 0, false, false, "", "", false, null, 0, 0, null), "");
                } catch (NameNotFoundException e) {
                    String str2 = "Can't uninstall. The package: " + str + " was not found.";
                    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(str2);
                    this.i.a(str2, null);
                    throw illegalArgumentException;
                }
            }
            int g = this.j.g(str);
            if (z) {
                g |= 8;
            } else {
                g &= -9;
            }
            this.j.b(str, g);
            a(str, iABTResultReceiver);
        }
    }

    public AppProfile[] getAllApplicationProfiles() {
        if (g()) {
            return this.j.a();
        }
        return null;
    }

    public AppProfile getApplicationProfile(String str) {
        if (g()) {
            return this.j.b(str);
        }
        return null;
    }

    public synchronized void setApplicationProfile(AppProfile appProfile) {
        if (appProfile == null) {
            throw new NullPointerException("App Profile argument is null");
        } else if (g()) {
            String packageName = appProfile.getPackageName();
            Throwable illegalArgumentException;
            if (this.j.c(packageName)) {
                int version = appProfile.getVersion();
                int version2 = this.j.b(packageName).getVersion();
                if (version != version2) {
                    packageName = "Version number: " + version + " specified in the AppProfile for package name: " + packageName + " does not match the existing version: " + version2 + ". Use the install() API if you wish to install a new version";
                    illegalArgumentException = new IllegalArgumentException(packageName);
                    this.i.a("setApplicationProfile() failed: " + packageName, illegalArgumentException);
                    throw illegalArgumentException;
                }
                this.j.a(appProfile);
                this.t.a(appProfile);
            } else {
                illegalArgumentException = new IllegalArgumentException("Package name '" + packageName + "' not found. Cannot update its AppProfile");
                this.i.a("setApplicationProfile() failed: ", illegalArgumentException);
                throw illegalArgumentException;
            }
        }
    }

    public void setPersistence(String str, boolean z) {
        if (str == null) {
            throw new NullPointerException("Package name argument is null");
        } else if (!this.j.c(str)) {
            Throwable illegalArgumentException = new IllegalArgumentException("Package name '" + str + "' not found. Cannot update its 'persist' flag");
            this.i.a("setPersistence() failed: ", illegalArgumentException);
            throw illegalArgumentException;
        } else if (g()) {
            synchronized (this) {
                AppProfile b = this.j.b(str);
                b.setIsPersisted(z);
                this.j.a(b);
            }
        }
    }

    public void setAllPersistence(boolean z) {
        if (g()) {
            this.j.a(z);
        }
    }

    public int getPersistedAppCount() {
        if (g()) {
            return this.j.b();
        }
        return 0;
    }

    public void invokeMethodAsSystem(MethodSpec methodSpec, IABTResultReceiver iABTResultReceiver) {
        if (methodSpec == null) {
            throw new NullPointerException("Method Specification is null");
        } else if (g()) {
            Message.obtain(this.n, 4, new g(this, methodSpec, iABTResultReceiver, this.g.getPackageManager().getPackagesForUid(Binder.getCallingUid())[0])).sendToTarget();
        }
    }

    public IABTPersistedFile getPersistedFile(String str, String str2, boolean z) {
        if (str == null) {
            throw new NullPointerException("Specified packageName argument is null");
        } else if (str2 == null) {
            throw new NullPointerException("Specified fileName argument is null");
        } else if (str2.length() == 0) {
            throw new IllegalArgumentException("Specified fileName argument is empty");
        } else if (!f(str)) {
            throw new IllegalArgumentException("The specified packageName does not match that of a calling package");
        } else if (g()) {
            return new w(str, str2, z, this.i);
        } else {
            return null;
        }
    }

    public IABTPersistedFile getSystemFile(String str, boolean z) {
        if (str == null) {
            throw new NullPointerException("Specified path argument is null");
        } else if (g()) {
            return new ah(str, z, this.i);
        } else {
            return null;
        }
    }

    public void registerPing(String str, IABTPing iABTPing, int i) {
        if (str == null) {
            throw new NullPointerException("Specified packageName argument is null");
        } else if (iABTPing == null) {
            throw new NullPointerException("IABTPing callback argument is null");
        } else if (i == 0) {
            throw new IllegalArgumentException("Specified ping interval argument is 0");
        } else if (!f(str)) {
            throw new IllegalArgumentException("The specified packageName does not match that of a calling package");
        } else if (!g()) {
        } else {
            if (this.j.b(str).getIsMonitored()) {
                this.t.a(str, iABTPing, i);
                return;
            }
            throw new IllegalArgumentException("The AppProfile for the specified package: " + str + " has 'monitor' set to false");
        }
    }

    public void unregisterPing(String str) {
        if (str == null) {
            throw new NullPointerException("Specified packageName argument is null");
        } else if (!f(str)) {
            throw new IllegalArgumentException("The specified packageName does not match that of a calling package");
        } else if (g()) {
            this.t.b(str);
        }
    }

    public String getDeviceId() {
        g();
        return getDeviceIdImpl();
    }

    public void refreshDeviceId() {
        if (g()) {
            this.f.b("");
        }
    }

    public void testFirmwareUpdate() {
        if (g()) {
            this.i.c("Testing firmware update ...");
            b(false);
        }
    }

    public String getDiagnostics() {
        if (!g()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            synchronized (this) {
                stringBuilder.append("Persistence version = 3136, state = " + ABTPersistenceManager.stateToString(this.f.a()) + "\n\n");
                AppProfile[] a = this.j.a();
                if (a == null) {
                    stringBuilder.append("There are no managed applications\n");
                } else {
                    PackageManager packageManager = this.g.getPackageManager();
                    stringBuilder.append("Managed Applications ---\n");
                    for (AppProfile appProfile : a) {
                        int i;
                        String packageName = appProfile.getPackageName();
                        stringBuilder.append(appProfile.toString() + "\n");
                        try {
                            i = packageManager.getPackageInfo(packageName, 0).versionCode;
                        } catch (NameNotFoundException e) {
                            i = 0;
                        }
                        if (i == 0) {
                            stringBuilder.append("Install status: " + packageName + " is currently not installed\n\n");
                        } else {
                            stringBuilder.append("Install status: version " + i + " of " + packageName + " is currently installed\n");
                        }
                        PersistedAppInfo h = this.j.h(packageName);
                        if (h != null) {
                            stringBuilder.append("APK path = " + h.b() + "\n");
                            stringBuilder.append("APK digest = " + h.e() + "\n");
                            int d = h.d();
                            stringBuilder.append("flags = " + d);
                            if ((d & 1) != 0) {
                                stringBuilder.append(": INSTALL_PENDING ");
                            }
                            if ((d & 2) != 0) {
                                stringBuilder.append(": UNINSTALL_PENDING ");
                            }
                            if ((d & 4) != 0) {
                                stringBuilder.append(": DOWNLOAD_PENDING ");
                            }
                            if ((d & 8) != 0) {
                                stringBuilder.append(": DELETE_PERSISTED_FILES_PENDING ");
                            }
                            if ((d & 16) != 0) {
                                stringBuilder.append(": INSTALL_TIMED_OUT");
                            }
                            stringBuilder.append("\n");
                            stringBuilder.append("Update attempt count = " + h.c() + "\n");
                        }
                        AppProfile b = this.k.b(packageName);
                        if (b == null) {
                            stringBuilder.append("No pending install AppProfile\n");
                        } else {
                            stringBuilder.append("Pending install AppProfile = " + b.toString() + "\n");
                        }
                        if (appProfile.getIsMonitored()) {
                            stringBuilder.append(this.t.d(packageName));
                        }
                        stringBuilder.append("--------\n");
                    }
                }
                String a2 = a();
                File file = new File(a2);
                stringBuilder.append("\nPersisted partition: " + a2 + ", size = " + file.getTotalSpace() + ", available = " + file.getUsableSpace() + " bytes\n");
                stringBuilder.append("Contents:\n" + CommandUtil.executeCommand("ls -l " + a2, this.g) + "\n");
            }
        } catch (Throwable th) {
            this.i.a("Exception occurred getting diagnostics: ", th);
        }
        return stringBuilder.toString();
    }

    public void getAppInfo(String str, String str2, String str3, String str4, IABTGetAppInfoReceiver iABTGetAppInfoReceiver) {
        getAppInfo_v2(str, str2, str3, str4, null, iABTGetAppInfoReceiver);
    }

    public void getAppInfo_v2(String str, String str2, String str3, String str4, String str5, IABTGetAppInfoReceiver iABTGetAppInfoReceiver) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Package name argument is null or empty");
        } else if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Access key argument is null or empty");
        } else if (str3 == null || str3.length() == 0) {
            throw new IllegalArgumentException("Update URL argument is null or empty");
        } else if (g()) {
            Message.obtain(this.o, 5, new f(this, str, str2, str3, str4, str5, iABTGetAppInfoReceiver, false)).sendToTarget();
        }
    }

    public void downloadApk(String str, int i, String str2, String str3, String str4, IABTDownloadReceiver iABTDownloadReceiver, int i2) {
        downloadApk_v2(str, i, str2, str3, null, str4, iABTDownloadReceiver, i2);
    }

    public void downloadApk_v2(String str, int i, String str2, String str3, String str4, String str5, IABTDownloadReceiver iABTDownloadReceiver, int i2) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Package name argument is null or empty");
        } else if (i == 0) {
            throw new IllegalArgumentException("Version argument is invalid (0)");
        } else if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Download URL argument is null or empty");
        } else if (str5 == null || str5.length() == 0) {
            throw new IllegalArgumentException("Digital signature argument is null or empty");
        } else if (g()) {
            Message.obtain(this.o, 6, new e(this, str, i, str2, str3, str4, str5, iABTDownloadReceiver, i2, false)).sendToTarget();
        }
    }

    public synchronized void persistApp(AppProfile appProfile) {
        if (appProfile == null) {
            throw new NullPointerException("AppProfile argument is null");
        }
        String packageName = appProfile.getPackageName();
        String digest;
        try {
            PackageManager packageManager = this.g.getPackageManager();
            int i = packageManager.getPackageInfo(packageName, 0).versionCode;
            String str = packageManager.getApplicationInfo(packageName, 0).sourceDir;
            if (g()) {
                if (appProfile.getVersion() != i) {
                    this.i.b("The version number in the persistApp(AppProfile) argument: " + appProfile.getVersion() + " does not match the installed version: " + i + ". Using the installed version.");
                    appProfile.setVersion(i);
                }
                if (this.j.c(packageName)) {
                    setApplicationProfile(appProfile);
                } else {
                    if (appProfile.getIsPersisted()) {
                        str = a(packageName, i, str, true);
                    }
                    try {
                        digest = Crypt.getDigest("SHA256", str);
                    } catch (Throwable th) {
                        this.i.b("Failed to get Digest for APK: " + str + ", algorithm: " + ", exception : " + th.toString());
                        digest = null;
                    }
                    this.j.a(appProfile, str);
                    if (appProfile.getIsPersisted()) {
                        this.j.a(packageName, str, digest);
                    } else {
                        this.j.a(packageName, null, null);
                    }
                    this.i.c("Successfully persisted package: " + packageName + ". Version = " + appProfile.getVersion() + ", persisted = " + appProfile.getIsPersisted() + ", monitored = " + appProfile.getIsMonitored());
                }
            }
        } catch (Throwable e) {
            digest = "Package " + packageName + " is not installed. Please install it prior to invoking this persistApp() method";
            this.i.a(digest, e);
            throw new IllegalArgumentException(digest);
        }
    }

    public ABTPersistenceService(Context context) {
        String b;
        try {
            this.g = context;
            this.x = new y(this.g);
            this.u = new Hashtable();
            this.i = new v(this.g, ABTPersistenceManager.PERSISTENCE_SERVICE_LOG, this.x);
            this.u.put(ABTPersistenceManager.PERSISTENCE_SERVICE_LOG, this.i);
            this.x.a(this.i);
            this.f = new x(this.g, this.i, "abt-persistence-settings");
            this.x.a(this.f, true, true);
            this.i.c("ABTPersistenceService starting up - version: 3136");
            this.a = false;
            String str = Build.FINGERPRINT;
            if (this.f.b().length() == 0) {
                this.i.c("Setting build fingerprint to: " + str);
                this.f.a(str);
            } else {
                b = this.f.b();
                if (!(str == null || str.equals(b))) {
                    this.i.c("Build fingerprint has changed since last boot.Now: " + str + ", previous: " + b + ". Initiating handling of firmware update.");
                    this.f.a(str);
                    this.a = true;
                }
            }
            this.j = new k(this.g, this.i, "abt-persistence-apps");
            this.x.a(this.j, true, true);
            this.k = new k(this.g, this.i, "abt-persistence-apps-to-install");
            this.x.a(this.k, true, true);
            this.l = new k(this.g, this.i, "abt-persistence-apps-fallback");
            this.x.a(this.l, true, false);
            this.h = new Hashtable();
            this.s = new HashSet();
            this.h.put("3082020b30820174a00302010202044d26012f300d06092a864886f70d0101050500304a310b3009060355040613024341310b3009060355040813024243311230100603550407130956616e636f75766572311a3018060355040a13114162736f6c75746520536f667477617265301e170d3131303130363137353134335a170d3430313232393137353134335a304a310b3009060355040613024341310b3009060355040813024243311230100603550407130956616e636f75766572311a3018060355040a13114162736f6c75746520536f66747761726530819f300d06092a864886f70d010101050003818d00308189028181008624ba8f680f679b9b4ce208d42a655c82d25e10e40cee65c5e53b48b5d00d25e33a97931614dcd14933f426070b9f1f17dfd2edc970f5a9449ad481c51c5204b91b5220eebd9a41c56fc29f3437eb3995d1ef2ebd00fb23f48260f1ea95b96d41c208add6effa815237a84c630a3c8d99a4e4048467f7a7ed659ac33421effb0203010001300d06092a864886f70d0101050500038181005dc61981f6a1d1a373f88b0d5491324a122ad81241a3746a256029b60c09980ee698e9ea59afa2f15e0d0a912ef2a975820632d9b95cc469f749776b601399c77055f57e8f6cd7a373d8c79cb8472c1cc833867ca62e0257b2c00093156ec2909ea6d6e6a5f093876c8b21fda70d3bfe585a7b564a48ff72c73f4d25ffe8597b", "Absolute Software Android signature");
            HandlerThread handlerThread = new HandlerThread("ABTPersistence_Install");
            handlerThread.start();
            this.m = new j(this, handlerThread.getLooper());
            handlerThread = new HandlerThread("ABTPersistence_InvokeMethod");
            handlerThread.start();
            this.n = new j(this, handlerThread.getLooper());
            handlerThread = new HandlerThread("ABTPersistence_Download");
            handlerThread.start();
            this.o = new j(this, handlerThread.getLooper());
            this.r = new c();
            this.r.a();
            this.p = new i();
            this.p.a();
            this.q = new d();
            this.t = new l(this.g, this.i, this.j, this.x);
            CommandUtil.executeCommand("chmod 750 " + a(), this.g);
        } catch (Throwable th) {
            b = "Exception thrown creating ABTPersistenceService: ";
            if (this.i != null) {
                this.i.a(b, th);
            }
            Log.e(LogUtil.LOG_TAG, b, th);
            return;
        }
        h();
        this.x.a();
    }

    private void a(int i) {
        new Thread(new a(this, i)).start();
    }

    protected static String a() {
        String str = "";
        if (SmartFaceManager.PAGE_BOTTOM.equals(DeviceUtil.getSystemProperty("ro.kernel.qemu"))) {
            return f();
        }
        return File.separatorChar + "persdata" + File.separatorChar + "absolute" + File.separatorChar;
    }

    private static String d() {
        return Environment.getDataDirectory().getAbsolutePath() + File.separatorChar + Columns.DATA + File.separatorChar + "com.absolute.persistence" + File.separatorChar;
    }

    private static String e() {
        return d() + "downloads" + File.separatorChar;
    }

    private static String f() {
        return Environment.getDataDirectory().getAbsolutePath() + File.separatorChar + "system" + File.separatorChar;
    }

    private boolean g() {
        PackageManager packageManager = this.g.getPackageManager();
        try {
            loop0:
            for (String packageInfo : packageManager.getPackagesForUid(Binder.getCallingUid())) {
                for (Signature toCharsString : packageManager.getPackageInfo(packageInfo, 64).signatures) {
                    if (this.h.get(toCharsString.toCharsString()) != null) {
                        break loop0;
                    }
                }
                if ("android.uid.system:1000".equals(packageManager.getNameForUid(Binder.getCallingUid()))) {
                    return true;
                }
            }
        } catch (Throwable e) {
            this.i.a("Unable to get Package for calling UID. Denying access.", e);
        }
        throw new SecurityException("Not authorized to access ABT Persistence Service");
    }

    private synchronized void a(String str, IABTResultReceiver iABTResultReceiver, int i) {
        this.j.b(str, ((this.j.g(str) & -3) & -9) | 1);
        this.m.sendMessageDelayed(this.m.obtainMessage(1, new h(this, str, iABTResultReceiver)), (long) (i * 1000));
    }

    private synchronized void a(String str, IABTResultReceiver iABTResultReceiver) {
        this.j.b(str, (this.j.g(str) & -2) | 2);
        this.m.sendMessage(this.m.obtainMessage(2, new h(this, str, iABTResultReceiver)));
    }

    private synchronized void a(String str, int i) {
        this.m.sendMessageDelayed(this.m.obtainMessage(3, new h(this, str, null)), (long) (i * 1000));
    }

    private String a(String str, int i, String str2, boolean z) {
        if (str2 != null) {
            try {
                if (str2.length() != 0) {
                    File file = new File(str2);
                    String b = b(str, i);
                    File file2 = new File(b);
                    File parentFile = file2.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdir();
                    }
                    if (!z && file2.exists() && file2.length() == file.length()) {
                        return b;
                    }
                    if (str2.startsWith(a()) && !str2.equals(b)) {
                        if (file.renameTo(file2)) {
                            return b;
                        }
                        this.i.b("Unable to rename APK file from: " + str2 + "to: " + b + ". File.renameTo() returned false.");
                    }
                    if (file2.exists()) {
                        file2.delete();
                    }
                    long usableSpace = parentFile.getUsableSpace();
                    if (usableSpace < file.length()) {
                        this.i.b("Insufficient space available in persisted partition to make a copy of the APK in: " + b + ". File size: " + file.length() + ", usable space: " + usableSpace);
                        return str2;
                    }
                    file2.createNewFile();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileInputStream.close();
                    fileOutputStream.close();
                    if (!file2.exists()) {
                        return str2;
                    }
                    if (str2.startsWith(e())) {
                        file.delete();
                    }
                    return b;
                }
            } catch (Throwable e) {
                this.i.a("copyApkToPersistentStorageArea() for package: " + str + " got exception: ", e);
                return str2;
            }
        }
        this.i.b("Unable to make a copy of the APK for package: " + str + ". The ApkPath is null/empty.");
        return str2;
    }

    private String b(String str, int i) {
        return a() + (str + "_" + i + ".apk");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(com.absolute.android.persistservice.h r18) {
        /*
        r17 = this;
        r2 = b;
        if (r2 != 0) goto L_0x000c;
    L_0x0004:
        if (r18 != 0) goto L_0x000c;
    L_0x0006:
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
    L_0x000c:
        r3 = r18.b;
        r8 = r18.c;
        r4 = 0;
        r5 = 0;
        r10 = 0;
        r7 = 0;
        r11 = 0;
        r6 = 0;
        r2 = 0;
        r0 = r17;
        r9 = r0.i;	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r12 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r12.<init>();	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r13 = "Starting install of package: ";
        r12 = r12.append(r13);	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r12 = r12.append(r3);	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r12 = r12.toString();	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r9.c(r12);	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r0 = r17;
        r9 = r0.g;	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r9 = r9.getPackageManager();	 Catch:{ Throwable -> 0x030e, all -> 0x030b }
        r12 = 0;
        r9 = r9.getPackageInfo(r3, r12);	 Catch:{ NameNotFoundException -> 0x007a }
        r2 = r9.versionCode;	 Catch:{ NameNotFoundException -> 0x007a }
        r12 = r2;
    L_0x0045:
        if (r12 != 0) goto L_0x007d;
    L_0x0047:
        r9 = 1;
    L_0x0048:
        monitor-enter(r17);	 Catch:{ Throwable -> 0x0316, all -> 0x030b }
        r0 = r17;
        r2 = r0.k;	 Catch:{ all -> 0x0143 }
        r13 = r2.h(r3);	 Catch:{ all -> 0x0143 }
        if (r13 != 0) goto L_0x007f;
    L_0x0053:
        r0 = r17;
        r2 = r0.i;	 Catch:{ all -> 0x0143 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0143 }
        r6.<init>();	 Catch:{ all -> 0x0143 }
        r11 = "Aborting install operation for package: ";
        r6 = r6.append(r11);	 Catch:{ all -> 0x0143 }
        r6 = r6.append(r3);	 Catch:{ all -> 0x0143 }
        r11 = " as there is no longer a pending install entry.";
        r6 = r6.append(r11);	 Catch:{ all -> 0x0143 }
        r6 = r6.toString();	 Catch:{ all -> 0x0143 }
        r2.b(r6);	 Catch:{ all -> 0x0143 }
        monitor-exit(r17);	 Catch:{ all -> 0x0143 }
        r0 = r17;
        r0.h(r10);
    L_0x0079:
        return;
    L_0x007a:
        r6 = move-exception;
        r12 = r2;
        goto L_0x0045;
    L_0x007d:
        r9 = 0;
        goto L_0x0048;
    L_0x007f:
        r2 = r13.a();	 Catch:{ all -> 0x0143 }
        r2 = r2.clone();	 Catch:{ all -> 0x0143 }
        r2 = (com.absolute.android.persistence.AppProfile) r2;	 Catch:{ all -> 0x0143 }
        r5 = r13.b();	 Catch:{ all -> 0x0335 }
        r0 = r17;
        r4 = r0.j;	 Catch:{ all -> 0x033e }
        r14 = r4.g(r3);	 Catch:{ all -> 0x033e }
        r0 = r17;
        r4 = r0.j;	 Catch:{ all -> 0x033e }
        r4 = r4.b(r3);	 Catch:{ all -> 0x033e }
        r15 = r4.getVersion();	 Catch:{ all -> 0x033e }
        monitor-exit(r17);	 Catch:{ all -> 0x033e }
        r4 = r2.getVersion();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        if (r5 == 0) goto L_0x01b7;
    L_0x00a8:
        r6 = r5.length();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        if (r6 <= 0) goto L_0x01b7;
    L_0x00ae:
        r6 = new java.io.File;	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r6.<init>(r5);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r6 = r6.exists();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        if (r6 == 0) goto L_0x01b7;
    L_0x00b9:
        r6 = 1;
    L_0x00ba:
        r14 = r14 & 4;
        if (r14 != 0) goto L_0x00c0;
    L_0x00be:
        if (r6 != 0) goto L_0x0352;
    L_0x00c0:
        r6 = 1;
        r0 = r17;
        r1 = r18;
        r4 = r0.a(r1, r12, r13);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r2 = r4.a();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r5 = r4.b();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r4 = r2.getVersion();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        if (r12 == 0) goto L_0x034a;
    L_0x00d7:
        if (r12 != r4) goto L_0x034a;
    L_0x00d9:
        if (r12 == r15) goto L_0x01ba;
    L_0x00db:
        r0 = r17;
        r11 = r0.i;	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13.<init>();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r14 = "Target version: ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13 = r13.append(r4);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r14 = " of application: ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13 = r13.append(r3);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r14 = " is already installed, but does not match the existing AppProfile version: ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13 = r13.append(r15);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r13 = r13.toString();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r11.b(r13);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r16 = r6;
        r6 = r5;
        r5 = r2;
        r2 = r16;
    L_0x010f:
        r11 = r5.getIsPersisted();	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r13 = 1;
        if (r11 != r13) goto L_0x011c;
    L_0x0116:
        r0 = r17;
        r6 = r0.a(r3, r4, r6, r2);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
    L_0x011c:
        r2 = "SHA256";
        r7 = com.absolute.android.crypt.Crypt.getDigest(r2, r6);	 Catch:{ Throwable -> 0x01f0, all -> 0x030b }
    L_0x0122:
        if (r12 == 0) goto L_0x012b;
    L_0x0124:
        r0 = r17;
        r2 = r0.t;	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r2.b(r3);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
    L_0x012b:
        r0 = r17;
        r10 = r0.c(r6);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r2 = 0;
        r0 = r17;
        r0.a(r3, r10, r4, r2);	 Catch:{ Throwable -> 0x032b, all -> 0x030b }
        r2 = r17;
        r2.a(r3, r4, r5, r6, r7, r8);	 Catch:{ Throwable -> 0x032b, all -> 0x030b }
        r0 = r17;
        r0.h(r10);
        goto L_0x0079;
    L_0x0143:
        r2 = move-exception;
        r16 = r5;
        r5 = r4;
        r4 = r16;
    L_0x0149:
        monitor-exit(r17);	 Catch:{ all -> 0x0347 }
        throw r2;	 Catch:{ Throwable -> 0x014b, all -> 0x030b }
    L_0x014b:
        r2 = move-exception;
        r6 = r10;
        r10 = r5;
        r5 = r9;
        r9 = r4;
        r4 = r2;
    L_0x0151:
        r0 = r17;
        r2 = r0.i;	 Catch:{ all -> 0x024e }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024e }
        r11.<init>();	 Catch:{ all -> 0x024e }
        r12 = "Install of package: ";
        r11 = r11.append(r12);	 Catch:{ all -> 0x024e }
        r11 = r11.append(r3);	 Catch:{ all -> 0x024e }
        r12 = " failed with exception: ";
        r11 = r11.append(r12);	 Catch:{ all -> 0x024e }
        r11 = r11.toString();	 Catch:{ all -> 0x024e }
        r2.a(r11, r4);	 Catch:{ all -> 0x024e }
        if (r8 == 0) goto L_0x017c;
    L_0x0173:
        r2 = 1;
        r11 = 0;
        r12 = r4.getMessage();	 Catch:{ Throwable -> 0x022b }
        r8.onOperationResult(r2, r11, r3, r12);	 Catch:{ Throwable -> 0x022b }
    L_0x017c:
        r2 = r4 instanceof com.absolute.android.persistservice.DownloadApkException;	 Catch:{ all -> 0x024e }
        if (r2 == 0) goto L_0x0256;
    L_0x0180:
        r0 = r4;
        r0 = (com.absolute.android.persistservice.DownloadApkException) r0;	 Catch:{ all -> 0x024e }
        r2 = r0;
        r2 = r2.a();	 Catch:{ all -> 0x024e }
        r8 = 1;
        if (r2 != r8) goto L_0x0256;
    L_0x018b:
        r0 = r17;
        r2 = r0.i;	 Catch:{ all -> 0x024e }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024e }
        r4.<init>();	 Catch:{ all -> 0x024e }
        r7 = "The download and install of APK for package: ";
        r4 = r4.append(r7);	 Catch:{ all -> 0x024e }
        r4 = r4.append(r3);	 Catch:{ all -> 0x024e }
        r7 = " will not be re-tried as the exception was considered fatal.";
        r4 = r4.append(r7);	 Catch:{ all -> 0x024e }
        r4 = r4.toString();	 Catch:{ all -> 0x024e }
        r2.b(r4);	 Catch:{ all -> 0x024e }
        r0 = r17;
        r0.a(r3, r10, r9, r5);	 Catch:{ all -> 0x024e }
    L_0x01b0:
        r0 = r17;
        r0.h(r6);
        goto L_0x0079;
    L_0x01b7:
        r6 = 0;
        goto L_0x00ba;
    L_0x01ba:
        r0 = r17;
        r6 = r0.i;	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r11 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r11.<init>();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r12 = "Target version: ";
        r11 = r11.append(r12);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r4 = r11.append(r4);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r11 = " of application: ";
        r4 = r4.append(r11);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r4 = r4.append(r3);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r11 = " is already installed.";
        r4 = r4.append(r11);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r4 = r4.toString();	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r6.c(r4);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r0 = r17;
        r0.b(r3, r8);	 Catch:{ Throwable -> 0x0321, all -> 0x030b }
        r0 = r17;
        r0.h(r10);
        goto L_0x0079;
    L_0x01f0:
        r2 = move-exception;
        r0 = r17;
        r11 = r0.i;	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r13.<init>();	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r14 = "Failed to get Digest for APK: ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r13 = r13.append(r6);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r14 = ", algorithm: ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r14 = ", exception : ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r2 = r13.append(r2);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        r11.b(r2);	 Catch:{ Throwable -> 0x0221, all -> 0x030b }
        goto L_0x0122;
    L_0x0221:
        r4 = move-exception;
        r16 = r9;
        r9 = r6;
        r6 = r10;
        r10 = r5;
        r5 = r16;
        goto L_0x0151;
    L_0x022b:
        r2 = move-exception;
        r0 = r17;
        r8 = r0.i;	 Catch:{ all -> 0x024e }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024e }
        r11.<init>();	 Catch:{ all -> 0x024e }
        r12 = "Got exception invoking  IABTResultReceiver.onOperationResult() for failed install of package: ";
        r11 = r11.append(r12);	 Catch:{ all -> 0x024e }
        r11 = r11.append(r3);	 Catch:{ all -> 0x024e }
        r12 = " Exception: ";
        r11 = r11.append(r12);	 Catch:{ all -> 0x024e }
        r11 = r11.toString();	 Catch:{ all -> 0x024e }
        r8.a(r11, r2);	 Catch:{ all -> 0x024e }
        goto L_0x017c;
    L_0x024e:
        r2 = move-exception;
        r10 = r6;
    L_0x0250:
        r0 = r17;
        r0.h(r10);
        throw r2;
    L_0x0256:
        r2 = r4 instanceof com.absolute.android.persistservice.DownloadApkException;	 Catch:{ all -> 0x024e }
        if (r2 == 0) goto L_0x02da;
    L_0x025a:
        r4 = (com.absolute.android.persistservice.DownloadApkException) r4;	 Catch:{ all -> 0x024e }
        r2 = r4.b();	 Catch:{ all -> 0x024e }
        r4 = 1;
        if (r2 != r4) goto L_0x02a0;
    L_0x0263:
        r0 = r17;
        r2 = r0.i;	 Catch:{ all -> 0x024e }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024e }
        r4.<init>();	 Catch:{ all -> 0x024e }
        r5 = "Download of APK for package: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x024e }
        r3 = r4.append(r3);	 Catch:{ all -> 0x024e }
        r4 = " will be re-tried when connectivity is available.";
        r3 = r3.append(r4);	 Catch:{ all -> 0x024e }
        r3 = r3.toString();	 Catch:{ all -> 0x024e }
        r2.c(r3);	 Catch:{ all -> 0x024e }
        r0 = r17;
        r3 = r0.s;	 Catch:{ all -> 0x024e }
        monitor-enter(r3);	 Catch:{ all -> 0x024e }
        r0 = r17;
        r2 = r0.s;	 Catch:{ all -> 0x029d }
        r4 = r18.b;	 Catch:{ all -> 0x029d }
        r2.add(r4);	 Catch:{ all -> 0x029d }
        r0 = r17;
        r2 = r0.q;	 Catch:{ all -> 0x029d }
        r2.a();	 Catch:{ all -> 0x029d }
        monitor-exit(r3);	 Catch:{ all -> 0x029d }
        goto L_0x01b0;
    L_0x029d:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x029d }
        throw r2;	 Catch:{ all -> 0x024e }
    L_0x02a0:
        if (r10 == 0) goto L_0x01b0;
    L_0x02a2:
        r2 = r10.getUpdateRetryMinutes();	 Catch:{ all -> 0x024e }
        r0 = r17;
        r4 = r0.i;	 Catch:{ all -> 0x024e }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024e }
        r5.<init>();	 Catch:{ all -> 0x024e }
        r7 = "Scheduling re-try of update/install of package: ";
        r5 = r5.append(r7);	 Catch:{ all -> 0x024e }
        r5 = r5.append(r3);	 Catch:{ all -> 0x024e }
        r7 = " in ";
        r5 = r5.append(r7);	 Catch:{ all -> 0x024e }
        r5 = r5.append(r2);	 Catch:{ all -> 0x024e }
        r7 = " minutes";
        r5 = r5.append(r7);	 Catch:{ all -> 0x024e }
        r5 = r5.toString();	 Catch:{ all -> 0x024e }
        r4.c(r5);	 Catch:{ all -> 0x024e }
        r4 = 0;
        r2 = r2 * 60;
        r0 = r17;
        r0.a(r3, r4, r2);	 Catch:{ all -> 0x024e }
        goto L_0x01b0;
    L_0x02da:
        r2 = r4 instanceof com.absolute.android.persistservice.InstallTimeoutException;	 Catch:{ all -> 0x024e }
        if (r2 == 0) goto L_0x0304;
    L_0x02de:
        monitor-enter(r17);	 Catch:{ all -> 0x024e }
        r0 = r17;
        r2 = r0.k;	 Catch:{ all -> 0x0301 }
        r2.a(r10);	 Catch:{ all -> 0x0301 }
        r0 = r17;
        r2 = r0.k;	 Catch:{ all -> 0x0301 }
        r2.a(r3, r9, r7);	 Catch:{ all -> 0x0301 }
        r0 = r17;
        r2 = r0.j;	 Catch:{ all -> 0x0301 }
        r0 = r17;
        r4 = r0.j;	 Catch:{ all -> 0x0301 }
        r4 = r4.g(r3);	 Catch:{ all -> 0x0301 }
        r4 = r4 | 16;
        r2.b(r3, r4);	 Catch:{ all -> 0x0301 }
        monitor-exit(r17);	 Catch:{ all -> 0x0301 }
        goto L_0x01b0;
    L_0x0301:
        r2 = move-exception;
        monitor-exit(r17);	 Catch:{ all -> 0x0301 }
        throw r2;	 Catch:{ all -> 0x024e }
    L_0x0304:
        r0 = r17;
        r0.a(r3, r10, r9, r5);	 Catch:{ all -> 0x024e }
        goto L_0x01b0;
    L_0x030b:
        r2 = move-exception;
        goto L_0x0250;
    L_0x030e:
        r2 = move-exception;
        r9 = r5;
        r5 = r6;
        r6 = r10;
        r10 = r4;
        r4 = r2;
        goto L_0x0151;
    L_0x0316:
        r2 = move-exception;
        r6 = r10;
        r10 = r4;
        r4 = r2;
        r16 = r5;
        r5 = r9;
        r9 = r16;
        goto L_0x0151;
    L_0x0321:
        r4 = move-exception;
        r6 = r10;
        r10 = r2;
        r16 = r5;
        r5 = r9;
        r9 = r16;
        goto L_0x0151;
    L_0x032b:
        r4 = move-exception;
        r16 = r9;
        r9 = r6;
        r6 = r10;
        r10 = r5;
        r5 = r16;
        goto L_0x0151;
    L_0x0335:
        r4 = move-exception;
        r16 = r4;
        r4 = r5;
        r5 = r2;
        r2 = r16;
        goto L_0x0149;
    L_0x033e:
        r4 = move-exception;
        r16 = r4;
        r4 = r5;
        r5 = r2;
        r2 = r16;
        goto L_0x0149;
    L_0x0347:
        r2 = move-exception;
        goto L_0x0149;
    L_0x034a:
        r16 = r6;
        r6 = r5;
        r5 = r2;
        r2 = r16;
        goto L_0x010f;
    L_0x0352:
        r6 = r5;
        r5 = r2;
        r2 = r11;
        goto L_0x010f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.absolute.android.persistservice.ABTPersistenceService.a(com.absolute.android.persistservice.h):void");
    }

    private void a(String str, int i, AppProfile appProfile, String str2, String str3, IABTResultReceiver iABTResultReceiver) {
        String a = a(str, i, appProfile, str2);
        synchronized (this) {
            this.j.a(appProfile);
            if (appProfile.getIsPersisted()) {
                String d = this.j.d(str);
                if (!(d == null || d.length() <= 0 || d.equals(a))) {
                    new File(d).delete();
                }
                this.j.a(str, a, str3);
            } else {
                if (a != null) {
                    if (a.startsWith(a()) && !new File(a).delete()) {
                        this.i.b("Failed delete APK file : " + a + " to clean up persisted directory");
                    }
                }
                this.j.a(str, null, null);
            }
        }
        b(str, iABTResultReceiver);
        this.i.c("Successfully installed package: " + str + ". Version = " + appProfile.getVersion() + ", persisted = " + appProfile.getIsPersisted() + ", monitored = " + appProfile.getIsMonitored());
        a(appProfile);
    }

    private String a(String str, int i, AppProfile appProfile, String str2) {
        String str3;
        Throwable th;
        try {
            int i2 = this.g.getPackageManager().getPackageInfo(str, 0).versionCode;
            if (i2 == 0 || i2 == i) {
                return str2;
            }
            this.i.b("Installed version: " + i2 + " of package: " + str + " does not match the version: " + i + " specified in the application profile. " + " The profile will be updated to match the version actually installed.");
            appProfile.setVersion(i2);
            str3 = null;
            try {
                File file = new File(str2);
                String b = b(str, i2);
                try {
                    if (file.renameTo(new File(b))) {
                        return b;
                    }
                    this.i.b("Unable to rename APK file from: " + str2 + "to: " + b + ". File.renameTo() returned false.");
                    return str2;
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    str3 = b;
                    th = th3;
                    this.i.b("Unable to rename APK file from: " + str2 + "to: " + str3 + ". Got exception: " + th.getMessage());
                    return str2;
                }
            } catch (Throwable th4) {
                th = th4;
                this.i.b("Unable to rename APK file from: " + str2 + "to: " + str3 + ". Got exception: " + th.getMessage());
                return str2;
            }
        } catch (Throwable th5) {
            str3 = "Got NameNotFoundException for: " + str + " after successfully installing it. Can't get it's version";
            this.i.a(str3, th5);
            throw new DownloadApkException(str3, th5, true);
        }
    }

    private void b(h hVar) {
        if (b || hVar != null) {
            String a = hVar.b;
            IABTResultReceiver b = hVar.c;
            try {
                this.i.c("Starting uninstall of package: " + a);
                this.t.a(a);
                int g = this.j.g(a);
                if (d(a)) {
                    c(a, this.j.b(a).getVersion());
                }
                g(a);
                if ((g & 8) != 0) {
                    File file = new File(a() + a);
                    if (file.exists() && !FileUtil.deleteFile(file)) {
                        this.i.b("Unable to delete persisted file directory: " + file.getPath() + " for package: " + a);
                    }
                }
                if (b != null) {
                    b.onOperationResult(2, true, a, null);
                }
            } catch (Throwable th) {
                this.i.a("Uninstall of " + a + " failed with exception: ", th);
                if (b != null) {
                    try {
                        b.onOperationResult(2, false, a, th.getMessage());
                        return;
                    } catch (Throwable th2) {
                        this.i.a("Got exception invoking IABTResultReceiver.onOperationResult() for failed uninstall of package: " + a + " Exception: ", th2);
                        return;
                    }
                }
                return;
            }
            this.i.c("Successfully uninstalled package: " + a);
            return;
        }
        throw new AssertionError();
    }

    private void a(g gVar) {
        Throwable th;
        Throwable cause;
        if (b || gVar != null) {
            MethodSpec a = gVar.b;
            IABTResultReceiver b = gVar.c;
            String c = gVar.d;
            MethodReturnValue methodReturnValue = new MethodReturnValue(null, null);
            String methodName = a.getMethodName();
            try {
                Class loadClass = this.g.createPackageContext(c, 3).getClassLoader().loadClass(a.getClassName());
                MethodReturnValue methodReturnValue2 = new MethodReturnValue(loadClass.getDeclaredMethod(methodName, a.getArgTypes()).invoke(loadClass.newInstance(), a.getArgValues()), null);
                if (b != null) {
                    try {
                        b.onInvokeResult(a, methodReturnValue2, true, null);
                        return;
                    } catch (Throwable th2) {
                        methodReturnValue = methodReturnValue2;
                        th = th2;
                        cause = th.getCause();
                        c = "null";
                        if (cause != null) {
                            c = ExceptionUtil.getExceptionMessage(cause);
                        }
                        this.i.a("Exception in ABTPersistenceService doInvokeMethod for method: " + a.getClassName() + "." + methodName + ", Cause: " + c + ", Exception: ", th);
                        if (b != null) {
                            try {
                                b.onInvokeResult(a, methodReturnValue, false, th.toString() + " Cause: " + c);
                                return;
                            } catch (Throwable th22) {
                                this.i.a("Got exception invoking IABTResultReceiver.onInvokeResult() for failed invocation of method: " + a.getClassName() + "." + methodName + " Exception: ", th22);
                                return;
                            }
                        }
                        return;
                    }
                }
                return;
            } catch (Throwable th222) {
                th = th222;
                cause = th.getCause();
                c = "null";
                if (cause != null) {
                    c = ExceptionUtil.getExceptionMessage(cause);
                }
                this.i.a("Exception in ABTPersistenceService doInvokeMethod for method: " + a.getClassName() + "." + methodName + ", Cause: " + c + ", Exception: ", th);
                if (b != null) {
                    b.onInvokeResult(a, methodReturnValue, false, th.toString() + " Cause: " + c);
                    return;
                }
                return;
            }
        }
        throw new AssertionError();
    }

    private void c(h hVar) {
        Throwable th;
        String str;
        Throwable th2;
        if (b || hVar != null) {
            String a = hVar.b;
            String str2 = null;
            try {
                AppProfile b;
                String d;
                synchronized (this) {
                    b = this.j.b(a);
                    d = this.j.d(a);
                }
                if (b != null) {
                    str2 = c(d);
                    try {
                        a(a, str2, b.getVersion(), true);
                        a(b);
                    } catch (Throwable th3) {
                        th = th3;
                        str = str2;
                        th2 = th;
                        h(str);
                        throw th2;
                    }
                }
                h(str2);
                return;
            } catch (Throwable th32) {
                th = th32;
                str = str2;
                th2 = th;
                h(str);
                throw th2;
            }
        }
        throw new AssertionError();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.absolute.android.persistence.AppInfoProperties a(com.absolute.android.persistservice.f r11) {
        /*
        r10 = this;
        r6 = 0;
        r4 = 1;
        r3 = 0;
        r0 = b;
        if (r0 != 0) goto L_0x000f;
    L_0x0007:
        if (r11 != 0) goto L_0x000f;
    L_0x0009:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x000f:
        if (r11 != 0) goto L_0x0020;
    L_0x0011:
        r0 = new java.lang.NullPointerException;
        r1 = "GetAppInfoRequest is null";
        r0.<init>(r1);
        r1 = r10.i;
        r2 = "doGetAppInfo() failed";
        r1.a(r2, r0);
        throw r0;
    L_0x0020:
        r9 = r11.b;
        r0 = r11.d;
        r1 = r10.g;
        r2 = "connectivity";
        r1 = r1.getSystemService(r2);
        r1 = (android.net.ConnectivityManager) r1;
        r1 = r1.getActiveNetworkInfo();
        if (r1 == 0) goto L_0x0153;
    L_0x0038:
        r1 = r1.isConnected();
        if (r1 == 0) goto L_0x0153;
    L_0x003e:
        r1 = r10.g;	 Catch:{ Throwable -> 0x00cd, all -> 0x0188 }
        r2 = "power";
        r1 = r1.getSystemService(r2);	 Catch:{ Throwable -> 0x00cd, all -> 0x0188 }
        r1 = (android.os.PowerManager) r1;	 Catch:{ Throwable -> 0x00cd, all -> 0x0188 }
        r2 = 1;
        r3 = "abt-persistence-pm-lock";
        r8 = r1.newWakeLock(r2, r3);	 Catch:{ Throwable -> 0x00cd, all -> 0x0188 }
        r8.acquire();	 Catch:{ Throwable -> 0x0190, all -> 0x018b }
        r1 = r10.g;	 Catch:{ Throwable -> 0x0190, all -> 0x018b }
        r2 = "wifi";
        r1 = r1.getSystemService(r2);	 Catch:{ Throwable -> 0x0190, all -> 0x018b }
        r1 = (android.net.wifi.WifiManager) r1;	 Catch:{ Throwable -> 0x0190, all -> 0x018b }
        r2 = 1;
        r3 = "abt-persistence-wifi-lock";
        r7 = r1.createWifiLock(r2, r3);	 Catch:{ Throwable -> 0x0190, all -> 0x018b }
        r7.acquire();	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r1 = r11.c;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = 3136; // 0xc40 float:4.394E-42 double:1.5494E-320;
        r3 = r10.getDeviceIdImpl();	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r4 = r10.g;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r3 = com.absolute.android.persistservice.r.a(r1, r9, r2, r3, r4);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r1 = r10.i;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2.<init>();	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r4 = "Initiating GetAppInfo() request for package ";
        r2 = r2.append(r4);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = r2.append(r9);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r4 = " to ";
        r2 = r2.append(r4);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = r2.append(r0);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r1.c(r2);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r1 = r11.e;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = r11.f;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r4 = r10.i;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r5 = r10.g;	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r1 = com.absolute.android.persistservice.r.a(r0, r1, r2, r3, r4, r5);	 Catch:{ Throwable -> 0x0196, all -> 0x018d }
        r2 = r11.g;	 Catch:{ Throwable -> 0x019d, all -> 0x018d }
        if (r2 == 0) goto L_0x00b5;
    L_0x00b0:
        r2 = 1;
        r3 = 0;
        r10.a(r11, r2, r1, r3);	 Catch:{ Throwable -> 0x019d, all -> 0x018d }
    L_0x00b5:
        if (r8 == 0) goto L_0x00c0;
    L_0x00b7:
        r0 = r8.isHeld();
        if (r0 == 0) goto L_0x00c0;
    L_0x00bd:
        r8.release();
    L_0x00c0:
        if (r7 == 0) goto L_0x01a5;
    L_0x00c2:
        r0 = r7.isHeld();
        if (r0 == 0) goto L_0x01a5;
    L_0x00c8:
        r7.release();
        r0 = r1;
    L_0x00cc:
        return r0;
    L_0x00cd:
        r1 = move-exception;
        r2 = r1;
        r3 = r6;
        r1 = r6;
    L_0x00d1:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0121 }
        r4.<init>();	 Catch:{ all -> 0x0121 }
        r5 = "doGetAppInfo for package: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0121 }
        r4 = r4.append(r9);	 Catch:{ all -> 0x0121 }
        r5 = " got exception/throwable for URL: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0121 }
        r0 = r4.append(r0);	 Catch:{ all -> 0x0121 }
        r0 = r0.toString();	 Catch:{ all -> 0x0121 }
        r4 = r10.i;	 Catch:{ all -> 0x0121 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0121 }
        r5.<init>();	 Catch:{ all -> 0x0121 }
        r5 = r5.append(r0);	 Catch:{ all -> 0x0121 }
        r7 = ", Exception: ";
        r5 = r5.append(r7);	 Catch:{ all -> 0x0121 }
        r5 = r5.toString();	 Catch:{ all -> 0x0121 }
        r4.a(r5, r2);	 Catch:{ all -> 0x0121 }
        r4 = r11.g;	 Catch:{ all -> 0x0121 }
        if (r4 == 0) goto L_0x0114;
    L_0x010c:
        r4 = 0;
        r5 = r2.toString();	 Catch:{ all -> 0x0121 }
        r10.a(r11, r4, r1, r5);	 Catch:{ all -> 0x0121 }
    L_0x0114:
        r4 = r11.h;	 Catch:{ all -> 0x0121 }
        if (r4 == 0) goto L_0x013a;
    L_0x011a:
        r1 = new com.absolute.android.persistservice.DownloadApkException;	 Catch:{ all -> 0x0121 }
        r4 = 0;
        r1.<init>(r0, r2, r4);	 Catch:{ all -> 0x0121 }
        throw r1;	 Catch:{ all -> 0x0121 }
    L_0x0121:
        r0 = move-exception;
        r8 = r3;
    L_0x0123:
        if (r8 == 0) goto L_0x012e;
    L_0x0125:
        r1 = r8.isHeld();
        if (r1 == 0) goto L_0x012e;
    L_0x012b:
        r8.release();
    L_0x012e:
        if (r6 == 0) goto L_0x0139;
    L_0x0130:
        r1 = r6.isHeld();
        if (r1 == 0) goto L_0x0139;
    L_0x0136:
        r6.release();
    L_0x0139:
        throw r0;
    L_0x013a:
        if (r3 == 0) goto L_0x0145;
    L_0x013c:
        r0 = r3.isHeld();
        if (r0 == 0) goto L_0x0145;
    L_0x0142:
        r3.release();
    L_0x0145:
        if (r6 == 0) goto L_0x01a5;
    L_0x0147:
        r0 = r6.isHeld();
        if (r0 == 0) goto L_0x01a5;
    L_0x014d:
        r6.release();
        r0 = r1;
        goto L_0x00cc;
    L_0x0153:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Download of APK for package: ";
        r0 = r0.append(r1);
        r0 = r0.append(r9);
        r1 = " cannot be performed as there is currently no network connectivity";
        r0 = r0.append(r1);
        r0 = r0.toString();
        r1 = new com.absolute.android.persistservice.DownloadApkException;
        r1.<init>(r0, r3);
        r1.a(r4);
        r0 = r11.g;
        if (r0 == 0) goto L_0x0181;
    L_0x017a:
        r0 = r1.toString();
        r10.a(r11, r3, r6, r0);
    L_0x0181:
        r0 = r11.h;
        if (r0 == 0) goto L_0x01a2;
    L_0x0187:
        throw r1;
    L_0x0188:
        r0 = move-exception;
        r8 = r6;
        goto L_0x0123;
    L_0x018b:
        r0 = move-exception;
        goto L_0x0123;
    L_0x018d:
        r0 = move-exception;
        r6 = r7;
        goto L_0x0123;
    L_0x0190:
        r1 = move-exception;
        r2 = r1;
        r3 = r8;
        r1 = r6;
        goto L_0x00d1;
    L_0x0196:
        r1 = move-exception;
        r2 = r1;
        r3 = r8;
        r1 = r6;
        r6 = r7;
        goto L_0x00d1;
    L_0x019d:
        r2 = move-exception;
        r6 = r7;
        r3 = r8;
        goto L_0x00d1;
    L_0x01a2:
        r0 = r6;
        goto L_0x00cc;
    L_0x01a5:
        r0 = r1;
        goto L_0x00cc;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.absolute.android.persistservice.ABTPersistenceService.a(com.absolute.android.persistservice.f):com.absolute.android.persistence.AppInfoProperties");
    }

    private void a(f fVar, boolean z, AppInfoProperties appInfoProperties, String str) {
        try {
            fVar.g.onGetAppInfoResult(z, fVar.b, appInfoProperties, str);
        } catch (Throwable th) {
            this.i.a("Got exception invoking IABTGetAppInfoReceiver.onGetAppInfoResult() for package: " + fVar.b + " to URL: " + fVar.d + " Exception: ", th);
        }
    }

    private String a(e eVar) {
        Throwable nullPointerException;
        WakeLock newWakeLock;
        WifiLock createWifiLock;
        Throwable th;
        WifiLock wifiLock;
        WakeLock wakeLock;
        String str = null;
        if (!b && eVar == null) {
            throw new AssertionError();
        } else if (eVar == null) {
            nullPointerException = new NullPointerException("DownloadApkRequest is null");
            this.i.a("doDownloadApk() failed", nullPointerException);
            throw nullPointerException;
        } else {
            String a = eVar.b;
            int b = eVar.c;
            String c = eVar.d;
            String d = eVar.e;
            String e = eVar.g;
            String str2 = a + "_" + b + ".apk";
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.g.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                DownloadApkException downloadApkException = new DownloadApkException("Download of APK for package: " + a + " cannot be performed as there is currently no network connectivity", false);
                downloadApkException.a(true);
                if (eVar.h != null) {
                    a(eVar, false, null, downloadApkException.toString());
                }
                if (eVar.j) {
                    throw downloadApkException;
                }
            }
            try {
                newWakeLock = ((PowerManager) this.g.getSystemService(SMProviderContract.KEY_POWER)).newWakeLock(1, "abt-persistence-pm-lock");
                try {
                    newWakeLock.acquire();
                    createWifiLock = ((WifiManager) this.g.getSystemService(Settings$System.RADIO_WIFI)).createWifiLock(1, "abt-persistence-wifi-lock");
                    try {
                        createWifiLock.acquire();
                        str = e() + str2;
                        File file = new File(str);
                        File parentFile = file.getParentFile();
                        parentFile.mkdirs();
                        if (!(parentFile.exists() && parentFile.canWrite() && parentFile.canExecute())) {
                            str = f() + str2;
                            file = new File(str);
                        }
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        r.a(a, b, c, d, eVar.f, e, str, eVar.h, eVar.i, this.i, this.g);
                        if (eVar.h != null) {
                            a(eVar, true, str, null);
                        }
                        if (newWakeLock != null && newWakeLock.isHeld()) {
                            newWakeLock.release();
                        }
                        if (createWifiLock != null && createWifiLock.isHeld()) {
                            createWifiLock.release();
                        }
                    } catch (Throwable th2) {
                        nullPointerException = th2;
                        newWakeLock.release();
                        createWifiLock.release();
                        throw nullPointerException;
                    }
                } catch (Throwable th3) {
                    nullPointerException = th3;
                    createWifiLock = null;
                    newWakeLock.release();
                    createWifiLock.release();
                    throw nullPointerException;
                }
            } catch (Throwable th4) {
                nullPointerException = th4;
                createWifiLock = null;
                newWakeLock = null;
                if (newWakeLock != null && newWakeLock.isHeld()) {
                    newWakeLock.release();
                }
                if (createWifiLock != null && createWifiLock.isHeld()) {
                    createWifiLock.release();
                }
                throw nullPointerException;
            }
            return str;
        }
    }

    private void a(e eVar, boolean z, String str, String str2) {
        try {
            eVar.h.onDownloadResult(z, eVar.b, eVar.c, str, str2);
        } catch (Throwable th) {
            this.i.a("Got exception invoking IABTDownloadReceiver.onDownloadResult() for package: " + eVar.b + " version: " + eVar.c + " to URL: " + eVar.d + " Exception: ", th);
        }
    }

    private void b(String str, IABTResultReceiver iABTResultReceiver) {
        synchronized (this) {
            this.k.a(str);
            this.j.a(str, 0);
            this.j.b(str, 0);
        }
        if (iABTResultReceiver != null) {
            try {
                iABTResultReceiver.onOperationResult(1, true, str, null);
            } catch (Throwable th) {
                this.i.a("Got exception invoking IABTResultReceiver.onOperationResult() for successful install of package: " + str + " Exception: ", th);
            }
        }
    }

    private void a(String str, AppProfile appProfile, String str2, boolean z) {
        if (appProfile != null) {
            try {
                if (appProfile.getIsPersisted() && str2 != null && str2.length() > 0) {
                    new File(str2).delete();
                }
            } catch (Throwable th) {
                this.i.b("Unable to delete APK file: " + str2 + " for package: " + str + ", exception: " + th.getMessage());
            }
        }
        synchronized (this) {
            this.k.a(str);
            if (z) {
                this.j.a(str);
            } else {
                this.j.a(str, 0);
                this.j.b(str, 0);
            }
        }
    }

    private void a(AppProfile appProfile) {
        if (!b && appProfile == null) {
            throw new AssertionError();
        } else if (appProfile != null) {
            String packageName = appProfile.getPackageName();
            if (appProfile.getStartOnInstall()) {
                String startOnInstallIntent = appProfile.getStartOnInstallIntent();
                if (startOnInstallIntent == null || startOnInstallIntent.length() == 0) {
                    this.i.a("Cannot start: " + packageName + " on install because the StartOnInstall intent specified in the App Profile is empty.", null);
                } else {
                    this.t.a(packageName, startOnInstallIntent);
                }
            }
            if (appProfile.getIsMonitored()) {
                this.t.a(appProfile, true);
            }
        }
    }

    private PersistedAppInfo a(h hVar, int i, PersistedAppInfo persistedAppInfo) {
        String a = hVar.b;
        String str = "";
        a(a);
        AppProfile a2 = persistedAppInfo.a();
        if (!b && a2 == null) {
            throw new AssertionError();
        } else if (a2 == null) {
            throw new DownloadApkException("Unable to invoke  GetAppInfo HTTP request because there is no pending install Application Profile for package: " + a, true);
        } else {
            int f = this.j.f(a);
            if (f >= a2.getMaxUpdateAttempts()) {
                str = "Exceeded maximum number of update attempts (" + f + ") via GetAppInfo request";
                this.i.b(str);
                throw new DownloadApkException(str, true);
            }
            String updateUrl = a2.getUpdateUrl();
            String packageName;
            try {
                AppInfoProperties a3 = a(new f(this, a, a2.getAccessKey(), updateUrl, a2.getUpdateIpAddress(), a2.getUpdateHostSPKIHash(), null, true));
                packageName = a3.getPackageName();
                if (packageName == null || packageName.length() == 0) {
                    throw new DownloadApkException("GetAppInfo failed because the server response  to GetAppInfo does not contain a PackageName value for URL: " + updateUrl, false);
                } else if (packageName.equals(a2.getPackageName())) {
                    int appVersion = a3.getAppVersion();
                    if (appVersion == 0) {
                        throw new DownloadApkException("GetAppInfo failed because the server response  does not contain a valid VersionCode value for URL: " + updateUrl, false);
                    }
                    int version = this.j.b(a).getVersion();
                    if (appVersion == i && appVersion == version) {
                        packageName = this.j.d(a);
                        if (packageName != null && packageName.length() > 0 && new File(packageName).exists()) {
                            this.i.c("The version: " + appVersion + " of package: " + a2.getPackageName() + " returned by GetAppInfo() query to: " + updateUrl + " is already installed. No need to download the APK.");
                            return persistedAppInfo;
                        }
                    }
                    String downloadUrl = a3.getDownloadUrl();
                    if (downloadUrl == null || downloadUrl.length() == 0) {
                        throw new DownloadApkException("GetAppInfo failed for URL: " + updateUrl + " because the server response to GetAppInfo does not contain a DownloadUrl value.", false);
                    }
                    String digitalSignature = a3.getDigitalSignature();
                    if (digitalSignature == null || digitalSignature.length() == 0) {
                        throw new DownloadApkException("GetAppInfo failed for URL: " + updateUrl + " because the server response to GetAppInfo does not contain a DigitalSignature value.", false);
                    }
                    String str2 = a;
                    PersistedAppInfo persistedAppInfo2 = new PersistedAppInfo(a3.getAppProfile(a2), a(new e(this, str2, appVersion, downloadUrl, a3.getDownloadIpAddress(), a3.getDownloadHostSPKIHash(), digitalSignature, null, 0, true)), 0, digitalSignature);
                    b(a);
                    return persistedAppInfo;
                } else {
                    throw new DownloadApkException("GetAppInfo failed because the server response  package name: " + packageName + " does not match the existing / expected one: " + a2.getPackageName() + " for URL: " + updateUrl, false);
                }
            } catch (DownloadApkException e) {
                if (!e.b()) {
                    this.j.a(a, f + 1);
                }
                throw e;
            } catch (Throwable th) {
                this.j.a(a, f + 1);
                packageName = "GetAppInfoAndDownloadAPK for package: " + a + " got exception/throwable for URL: " + updateUrl;
                this.i.a(packageName + ", Exception: ", th);
                DownloadApkException downloadApkException = new DownloadApkException(packageName, th, false);
            }
        }
    }

    private void a(String str) {
        this.j.b(str, this.j.g(str) | 4);
    }

    private void b(String str) {
        this.j.b(str, this.j.g(str) & -5);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.lang.String r10, java.lang.String r11, int r12, boolean r13) {
        /*
        r9 = this;
        r0 = 2;
        r1 = 0;
        r1 = r9.j();	 Catch:{ Throwable -> 0x0157 }
        if (r1 == 0) goto L_0x000f;
    L_0x0008:
        r2 = 0;
        r9.c(r2);	 Catch:{ Throwable -> 0x0157 }
        r9.j();	 Catch:{ Throwable -> 0x0157 }
    L_0x000f:
        r2 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ Throwable -> 0x0157 }
        r3 = "com.absolute.android.persistservice.ABTPackageInstallObserver";
        r3 = r2.loadClass(r3);	 Catch:{ Throwable -> 0x0157 }
        r2 = r3.newInstance();	 Catch:{ Throwable -> 0x0157 }
        r9.v = r2;	 Catch:{ Throwable -> 0x0157 }
        r2 = r9.g;	 Catch:{ Throwable -> 0x0157 }
        r2 = r2.getPackageManager();	 Catch:{ Throwable -> 0x0157 }
        r4 = r2.getClass();	 Catch:{ Throwable -> 0x0157 }
        r5 = 4;
        r5 = new java.lang.Class[r5];	 Catch:{ Throwable -> 0x0157 }
        r6 = 0;
        r7 = android.net.Uri.class;
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = 1;
        r7 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ Throwable -> 0x0157 }
        r8 = "android.content.pm.IPackageInstallObserver";
        r7 = r7.loadClass(r8);	 Catch:{ Throwable -> 0x0157 }
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = 2;
        r7 = java.lang.Integer.TYPE;	 Catch:{ Throwable -> 0x0157 }
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = 3;
        r7 = java.lang.String.class;
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = "installPackage";
        r4 = r4.getMethod(r6, r5);	 Catch:{ Throwable -> 0x0157 }
        r5 = r9.i;	 Catch:{ Throwable -> 0x0157 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0157 }
        r6.<init>();	 Catch:{ Throwable -> 0x0157 }
        r7 = "invoking installPackage() for: ";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x0157 }
        r6 = r6.append(r10);	 Catch:{ Throwable -> 0x0157 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x0157 }
        r5.d(r6);	 Catch:{ Throwable -> 0x0157 }
        r5 = 4;
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x0157 }
        r6 = 0;
        r7 = new java.io.File;	 Catch:{ Throwable -> 0x0157 }
        r7.<init>(r11);	 Catch:{ Throwable -> 0x0157 }
        r7 = android.net.Uri.fromFile(r7);	 Catch:{ Throwable -> 0x0157 }
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = 1;
        r7 = r9.v;	 Catch:{ Throwable -> 0x0157 }
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0157 }
        r6 = 2;
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Throwable -> 0x0157 }
        r5[r6] = r0;	 Catch:{ Throwable -> 0x0157 }
        r0 = 3;
        r6 = 0;
        r5[r0] = r6;	 Catch:{ Throwable -> 0x0157 }
        r4.invoke(r2, r5);	 Catch:{ Throwable -> 0x0157 }
        r4 = r9.v;	 Catch:{ Throwable -> 0x0157 }
        monitor-enter(r4);	 Catch:{ Throwable -> 0x0157 }
        r0 = 0;
        r2 = "getFinished";
        r5 = 0;
        r5 = new java.lang.Class[r5];	 Catch:{ all -> 0x0154 }
        r5 = r3.getMethod(r2, r5);	 Catch:{ all -> 0x0154 }
        r2 = r0;
    L_0x0096:
        r0 = r9.v;	 Catch:{ all -> 0x0154 }
        r6 = 0;
        r6 = new java.lang.Object[r6];	 Catch:{ all -> 0x0154 }
        r0 = r5.invoke(r0, r6);	 Catch:{ all -> 0x0154 }
        r0 = (java.lang.Boolean) r0;	 Catch:{ all -> 0x0154 }
        r0 = r0.booleanValue();	 Catch:{ all -> 0x0154 }
        if (r0 != 0) goto L_0x017b;
    L_0x00a7:
        r0 = r9.i;	 Catch:{ InterruptedException -> 0x012d }
        r6 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x012d }
        r6.<init>();	 Catch:{ InterruptedException -> 0x012d }
        r7 = "waiting on installPackage() completion for: ";
        r6 = r6.append(r7);	 Catch:{ InterruptedException -> 0x012d }
        r6 = r6.append(r10);	 Catch:{ InterruptedException -> 0x012d }
        r7 = " count = ";
        r6 = r6.append(r7);	 Catch:{ InterruptedException -> 0x012d }
        r6 = r6.append(r2);	 Catch:{ InterruptedException -> 0x012d }
        r6 = r6.toString();	 Catch:{ InterruptedException -> 0x012d }
        r0.d(r6);	 Catch:{ InterruptedException -> 0x012d }
        if (r2 <= 0) goto L_0x016f;
    L_0x00cc:
        r0 = r9.e(r10);	 Catch:{ InterruptedException -> 0x012d }
        if (r0 != r12) goto L_0x010a;
    L_0x00d2:
        r0 = r9.i;	 Catch:{ InterruptedException -> 0x012d }
        r2 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x012d }
        r2.<init>();	 Catch:{ InterruptedException -> 0x012d }
        r3 = "installPackage() timed out, but completed for: ";
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.append(r10);	 Catch:{ InterruptedException -> 0x012d }
        r3 = " version: ";
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.append(r12);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.toString();	 Catch:{ InterruptedException -> 0x012d }
        r0.b(r2);	 Catch:{ InterruptedException -> 0x012d }
        monitor-exit(r4);	 Catch:{ all -> 0x0154 }
        r0 = 0;
        r9.v = r0;
        if (r1 == 0) goto L_0x0100;
    L_0x00fa:
        r9.c(r1);	 Catch:{ Throwable -> 0x0101 }
        r9.j();	 Catch:{ Throwable -> 0x0101 }
    L_0x0100:
        return;
    L_0x0101:
        r0 = move-exception;
        r1 = r9.i;
        r2 = "Failed to re-enable package verification, exception: ";
        r1.a(r2, r0);
        goto L_0x0100;
    L_0x010a:
        r0 = new com.absolute.android.persistservice.InstallTimeoutException;	 Catch:{ InterruptedException -> 0x012d }
        r2 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x012d }
        r2.<init>();	 Catch:{ InterruptedException -> 0x012d }
        r3 = "Invocation of PackageManager.installPackage() timed out for package: ";
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.append(r10);	 Catch:{ InterruptedException -> 0x012d }
        r3 = " version: ";
        r2 = r2.append(r3);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.append(r12);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r2.toString();	 Catch:{ InterruptedException -> 0x012d }
        r0.<init>(r2);	 Catch:{ InterruptedException -> 0x012d }
        throw r0;	 Catch:{ InterruptedException -> 0x012d }
    L_0x012d:
        r0 = move-exception;
        r2 = r9.i;	 Catch:{ all -> 0x0154 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0154 }
        r3.<init>();	 Catch:{ all -> 0x0154 }
        r5 = "installPackage() for package ";
        r3 = r3.append(r5);	 Catch:{ all -> 0x0154 }
        r3 = r3.append(r10);	 Catch:{ all -> 0x0154 }
        r5 = " got interrupted exception: ";
        r3 = r3.append(r5);	 Catch:{ all -> 0x0154 }
        r3 = r3.toString();	 Catch:{ all -> 0x0154 }
        r2.a(r3, r0);	 Catch:{ all -> 0x0154 }
        r0 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0154 }
        r2 = "Invocation of PackageManager.installPackage() was interrupted";
        r0.<init>(r2);	 Catch:{ all -> 0x0154 }
        throw r0;	 Catch:{ all -> 0x0154 }
    L_0x0154:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0154 }
        throw r0;	 Catch:{ Throwable -> 0x0157 }
    L_0x0157:
        r0 = move-exception;
        r2 = r0 instanceof com.absolute.android.persistservice.InstallTimeoutException;	 Catch:{ all -> 0x0162 }
        if (r2 == 0) goto L_0x0214;
    L_0x015c:
        r2 = new com.absolute.android.persistservice.InstallTimeoutException;	 Catch:{ all -> 0x0162 }
        r2.<init>(r0);	 Catch:{ all -> 0x0162 }
        throw r2;	 Catch:{ all -> 0x0162 }
    L_0x0162:
        r0 = move-exception;
        r2 = 0;
        r9.v = r2;
        if (r1 == 0) goto L_0x016e;
    L_0x0168:
        r9.c(r1);	 Catch:{ Throwable -> 0x0248 }
        r9.j();	 Catch:{ Throwable -> 0x0248 }
    L_0x016e:
        throw r0;
    L_0x016f:
        r0 = r2 + 1;
        r2 = r9.v;	 Catch:{ InterruptedException -> 0x012d }
        r6 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r2.wait(r6);	 Catch:{ InterruptedException -> 0x012d }
        r2 = r0;
        goto L_0x0096;
    L_0x017b:
        r0 = "getResult";
        r2 = 0;
        r2 = new java.lang.Class[r2];	 Catch:{ all -> 0x0154 }
        r0 = r3.getMethod(r0, r2);	 Catch:{ all -> 0x0154 }
        r2 = r9.v;	 Catch:{ all -> 0x0154 }
        r3 = 0;
        r3 = new java.lang.Object[r3];	 Catch:{ all -> 0x0154 }
        r0 = r0.invoke(r2, r3);	 Catch:{ all -> 0x0154 }
        r0 = (java.lang.Integer) r0;	 Catch:{ all -> 0x0154 }
        r0 = r0.intValue();	 Catch:{ all -> 0x0154 }
        r2 = 1;
        if (r0 != r2) goto L_0x01e7;
    L_0x0196:
        if (r13 == 0) goto L_0x01b7;
    L_0x0198:
        r0 = r9.e(r10);	 Catch:{ all -> 0x0154 }
        if (r0 == r12) goto L_0x01b7;
    L_0x019e:
        r0 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0154 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0154 }
        r2.<init>();	 Catch:{ all -> 0x0154 }
        r3 = "Invocation of PackageManager.installPackage() completed, but not for target version: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0154 }
        r2 = r2.append(r12);	 Catch:{ all -> 0x0154 }
        r2 = r2.toString();	 Catch:{ all -> 0x0154 }
        r0.<init>(r2);	 Catch:{ all -> 0x0154 }
        throw r0;	 Catch:{ all -> 0x0154 }
    L_0x01b7:
        r0 = r9.i;	 Catch:{ all -> 0x0154 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0154 }
        r2.<init>();	 Catch:{ all -> 0x0154 }
        r3 = "installPackage() completed successfully for: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0154 }
        r2 = r2.append(r10);	 Catch:{ all -> 0x0154 }
        r2 = r2.toString();	 Catch:{ all -> 0x0154 }
        r0.d(r2);	 Catch:{ all -> 0x0154 }
        monitor-exit(r4);	 Catch:{ all -> 0x0154 }
        r0 = 0;
        r9.v = r0;
        if (r1 == 0) goto L_0x0100;
    L_0x01d5:
        r9.c(r1);	 Catch:{ Throwable -> 0x01dd }
        r9.j();	 Catch:{ Throwable -> 0x01dd }
        goto L_0x0100;
    L_0x01dd:
        r0 = move-exception;
        r1 = r9.i;
        r2 = "Failed to re-enable package verification, exception: ";
        r1.a(r2, r0);
        goto L_0x0100;
    L_0x01e7:
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0154 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0154 }
        r3.<init>();	 Catch:{ all -> 0x0154 }
        r5 = "Invocation of PackageManager.installPackage() failed with result: ";
        r3 = r3.append(r5);	 Catch:{ all -> 0x0154 }
        r3 = r3.append(r0);	 Catch:{ all -> 0x0154 }
        r5 = " [";
        r3 = r3.append(r5);	 Catch:{ all -> 0x0154 }
        r0 = r9.b(r0);	 Catch:{ all -> 0x0154 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x0154 }
        r3 = "]";
        r0 = r0.append(r3);	 Catch:{ all -> 0x0154 }
        r0 = r0.toString();	 Catch:{ all -> 0x0154 }
        r2.<init>(r0);	 Catch:{ all -> 0x0154 }
        throw r2;	 Catch:{ all -> 0x0154 }
    L_0x0214:
        r3 = r0.getCause();	 Catch:{ all -> 0x0162 }
        r2 = "null";
        if (r3 == 0) goto L_0x0221;
    L_0x021d:
        r2 = com.absolute.android.utils.ExceptionUtil.getExceptionMessage(r3);	 Catch:{ all -> 0x0162 }
    L_0x0221:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0162 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0162 }
        r4.<init>();	 Catch:{ all -> 0x0162 }
        r5 = "Invocation of PackageManager.installPackage() threw exception : ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0162 }
        r0 = r0.toString();	 Catch:{ all -> 0x0162 }
        r0 = r4.append(r0);	 Catch:{ all -> 0x0162 }
        r4 = ", cause: ";
        r0 = r0.append(r4);	 Catch:{ all -> 0x0162 }
        r0 = r0.append(r2);	 Catch:{ all -> 0x0162 }
        r0 = r0.toString();	 Catch:{ all -> 0x0162 }
        r3.<init>(r0);	 Catch:{ all -> 0x0162 }
        throw r3;	 Catch:{ all -> 0x0162 }
    L_0x0248:
        r1 = move-exception;
        r2 = r9.i;
        r3 = "Failed to re-enable package verification, exception: ";
        r2.a(r3, r1);
        goto L_0x016e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.absolute.android.persistservice.ABTPersistenceService.a(java.lang.String, java.lang.String, int, boolean):void");
    }

    private String c(String str) {
        Object obj = 1;
        String str2 = "";
        try {
            File file = new File(str);
            String name = file.getName();
            str2 = d() + name;
            File file2 = new File(str2);
            File parentFile = file2.getParentFile();
            parentFile.mkdirs();
            if (!(parentFile.exists() && parentFile.canWrite() && parentFile.canExecute())) {
                str2 = f() + name;
                file2 = new File(str2);
            }
            if (file2.exists()) {
                file2.delete();
            }
            file2.createNewFile();
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[8192];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileInputStream.close();
            fileOutputStream.close();
            try {
                parentFile.setExecutable(true, false);
                if (file2.setReadable(true, false)) {
                    obj = null;
                }
            } catch (Throwable th) {
                this.i.a("Unable to set file permissions of APK: " + str2 + " got exception: ", th);
            }
            if (obj == null) {
                return str2;
            }
            throw new RuntimeException("Set permission of APK file: " + str2 + " to readable failed.");
        } catch (Throwable th2) {
            this.i.a("Unable to copy APK: " + str + " to: " + str2 + " got exception: ", th2);
            RuntimeException runtimeException = new RuntimeException("Unable to copy APK to install directory. Got exception : " + th2.toString());
        }
    }

    public String getDeviceIdImpl() {
        String d = this.f.d();
        if (d != null && !d.isEmpty()) {
            return d;
        }
        d = DeviceUtil.getDeviceId(this.g);
        this.f.b(d);
        this.f.e();
        return d;
    }

    private void c(String str, int i) {
        try {
            Class loadClass = ClassLoader.getSystemClassLoader().loadClass("com.absolute.android.persistservice.ABTPackageDeleteObserver");
            this.w = loadClass.newInstance();
            PackageManager packageManager = this.g.getPackageManager();
            Method method = packageManager.getClass().getMethod("deletePackage", new Class[]{String.class, ClassLoader.getSystemClassLoader().loadClass("android.content.pm.IPackageDeleteObserver"), Integer.TYPE});
            this.i.d("invoking deletePackage() for: " + str);
            method.invoke(packageManager, new Object[]{str, this.w, Integer.valueOf(0)});
            synchronized (this.w) {
                int i2;
                method = loadClass.getMethod("getFinished", new Class[0]);
                int i3 = 0;
                while (!((Boolean) method.invoke(this.w, new Object[0])).booleanValue()) {
                    try {
                        this.i.d("waiting on deletePackage() completion for: " + str + " count = " + i3);
                        if (i3 <= 0) {
                            i2 = i3 + 1;
                            this.w.wait(20000);
                            i3 = i2;
                        } else if (e(str) == 0) {
                            this.i.b("deletePackage() timed out, but completed for: " + str);
                            this.w = null;
                            return;
                        } else {
                            throw new RuntimeException("Invocation of PackageManager.deletePackage() timed out for package: " + str);
                        }
                    } catch (Throwable e) {
                        this.i.a("deletePackage() for package " + str + " got interrupted exception: ", e);
                        throw new RuntimeException("Invocation of PackageManager.deletePackage() was interrupted");
                    }
                }
                i2 = ((Integer) loadClass.getMethod("getResult", new Class[0]).invoke(this.w, new Object[0])).intValue();
                if (i2 == 1) {
                    this.i.d("deletePackage() completed successfully for: " + str);
                    this.w = null;
                    return;
                }
                throw new RuntimeException("Invocation of PackageManager.deletePackage() failed with result: " + i2);
            }
        } catch (Throwable e2) {
            try {
                Throwable cause = e2.getCause();
                String str2 = "null";
                if (cause != null) {
                    str2 = ExceptionUtil.getExceptionMessage(cause);
                }
                RuntimeException runtimeException = new RuntimeException("Invocation of PackageManager.deletePackage() threw exception : " + e2.toString() + ", cause: " + str2);
            } catch (Throwable th) {
                this.w = null;
            }
        }
    }

    private synchronized void a(boolean z) {
        AppProfile[] a = this.j.a();
        if (a != null) {
            for (AppProfile appProfile : a) {
                int i;
                String packageName = appProfile.getPackageName();
                int version = appProfile.getVersion();
                if (e(packageName) == version) {
                    i = 1;
                } else {
                    i = 0;
                }
                if (z && i == 1 && appProfile.getIsMonitored()) {
                    this.t.a(appProfile, false);
                }
                PersistedAppInfo h = this.j.h(packageName);
                try {
                    if (appProfile.getIsPersisted() && h.e() == null) {
                        String b = h.b();
                        this.j.a(packageName, b, Crypt.getDigest("SHA256", b));
                    }
                } catch (Throwable th) {
                    this.i.a("recoverAndCompletePendingOperations() got exception: ", th);
                }
                boolean z2 = false;
                int d = h != null ? h.d() : 0;
                if (i == 0 && this.f.a() == 1 && appProfile.getIsPersisted() && (d & 2) == 0 && e(packageName) < version) {
                    this.i.c("Persisted package: " + packageName + " has been removed (possibly due to factory reset)." + " Initiating recovery re-installation.");
                    z2 = a(packageName, z);
                }
                if (!z2 && (d & 1) != 0) {
                    this.i.c("Resuming installation of package: " + packageName);
                    int i2 = 0;
                    if (z) {
                        i2 = 20;
                    } else if ((d & 4) != 0) {
                        i2 = 20;
                    }
                    a(packageName, null, i2);
                } else if ((d & 2) != 0) {
                    this.i.c("Resuming uninstallation of package: " + packageName);
                    a(packageName, null);
                } else if (i != 0) {
                    continue;
                } else if (!(appProfile.getIsPersisted() || i())) {
                    this.i.c("Non-persisted package: " + packageName + " has been removed (possibly due to factory reset)." + " Cleaning up to remove AppProfile and APK.");
                    g(packageName);
                }
            }
        }
        return;
    }

    private synchronized void b(boolean z) {
        try {
            AppProfile[] a = this.j.a();
            if (a != null) {
                for (AppProfile appProfile : a) {
                    String packageName = appProfile.getPackageName();
                    int g = this.j.g(packageName);
                    if (this.f.a() == 1 && appProfile.getMaxUpdateAttempts() > 0 && (g & 2) == 0) {
                        this.j.a(packageName, 0);
                        this.k.a(appProfile, this.j.d(packageName));
                        a(packageName);
                        int i = z ? 20 : 0;
                        this.i.c("Initiating install request for package: " + packageName + " in " + i + " secs");
                        a(packageName, null, i);
                    }
                }
            }
        } catch (Throwable th) {
            this.i.a("handleFirmwareUpdate() got exception: ", th);
        }
        return;
    }

    private boolean a(String str, boolean z) {
        boolean z2;
        Throwable th;
        boolean z3;
        PersistedAppInfo h = this.j.h(str);
        if (h == null) {
            return false;
        }
        int i;
        String b = h.b();
        if (b != null && b.length() > 0 && new File(b).exists()) {
            try {
                if (h.e().equals(Crypt.getDigest("SHA256", b))) {
                    try {
                        this.i.c("Recovering package " + str + " using local APK: " + b);
                        a(str, z ? 20 : 0);
                        z2 = true;
                    } catch (Throwable th2) {
                        th = th2;
                        z3 = true;
                        try {
                            this.i.b("Failed to validate digital signature of APK: " + b + " Got exception: " + th.toString());
                            z2 = z3;
                            if (z2) {
                                return false;
                            }
                            synchronized (this) {
                                if (this.k.b(str) == null) {
                                    this.k.a(this.j.b(str), b);
                                    a(str);
                                    this.j.a(str, 0);
                                }
                            }
                            i = z ? 0 : 20;
                            this.i.c("Recovering package " + str + " in " + i + " seconds by querying server, since there is no local APK.");
                            a(str, null, i);
                            return true;
                        } catch (Throwable th3) {
                            this.i.a("initiateRecovery() got exception: ", th3);
                            return false;
                        }
                    }
                    if (z2) {
                        return false;
                    }
                    synchronized (this) {
                        if (this.k.b(str) == null) {
                            this.k.a(this.j.b(str), b);
                            a(str);
                            this.j.a(str, 0);
                        }
                    }
                    if (z) {
                    }
                    this.i.c("Recovering package " + str + " in " + i + " seconds by querying server, since there is no local APK.");
                    a(str, null, i);
                    return true;
                }
                this.i.b("Digital signature of APK: " + b + " is invalid.");
            } catch (Throwable th4) {
                th3 = th4;
                z3 = false;
                this.i.b("Failed to validate digital signature of APK: " + b + " Got exception: " + th3.toString());
                z2 = z3;
                if (z2) {
                    return false;
                }
                synchronized (this) {
                    if (this.k.b(str) == null) {
                        this.k.a(this.j.b(str), b);
                        a(str);
                        this.j.a(str, 0);
                    }
                }
                if (z) {
                }
                this.i.c("Recovering package " + str + " in " + i + " seconds by querying server, since there is no local APK.");
                a(str, null, i);
                return true;
            }
        }
        z2 = false;
        if (z2) {
            return false;
        }
        synchronized (this) {
            if (this.k.b(str) == null) {
                this.k.a(this.j.b(str), b);
                a(str);
                this.j.a(str, 0);
            }
        }
        if (z) {
        }
        this.i.c("Recovering package " + str + " in " + i + " seconds by querying server, since there is no local APK.");
        a(str, null, i);
        return true;
    }

    private boolean d(String str) {
        int i;
        try {
            i = this.g.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (NameNotFoundException e) {
            i = 0;
        }
        if (i != 0) {
            return true;
        }
        return false;
    }

    private int e(String str) {
        int i = 0;
        try {
            return this.g.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (NameNotFoundException e) {
            return i;
        }
    }

    private boolean f(String str) {
        int callingUid = Binder.getCallingUid();
        String[] packagesForUid = this.g.getPackageManager().getPackagesForUid(callingUid);
        if (packagesForUid != null) {
            for (String equals : packagesForUid) {
                if (equals.equals(str)) {
                    return true;
                }
            }
            return false;
        }
        Throwable runtimeException = new RuntimeException("checkIsCallingPackage() for package: " + str + " failed. Unable to get calling packages for UID: " + callingUid);
        this.i.a("", runtimeException);
        throw runtimeException;
    }

    private void h() {
        try {
            AppProfile[] a = this.l.a();
            if (a != null) {
                for (AppProfile packageName : a) {
                    String packageName2 = packageName.getPackageName();
                    String d = this.l.d(packageName2);
                    if (d != null && d.length() > 0) {
                        new File(d).delete();
                    }
                    this.l.a(packageName2);
                }
            }
        } catch (Throwable th) {
            this.i.b("Unable to clean up fallbacks, got exception: " + th.getMessage());
        }
    }

    private synchronized void g(String str) {
        try {
            String d = this.j.d(str);
            this.j.a(str);
            this.k.a(str);
            if (d != null && d.length() > 0) {
                new File(d).delete();
            }
        } catch (Throwable th) {
            this.i.b("Unable to delete APK file: " + null + " for package: " + str + ", exception: " + th.getMessage());
        }
    }

    private synchronized void h(String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    new File(str).delete();
                }
            } catch (Throwable th) {
                this.i.b("Unable to delete copy of APK: " + str + ", got exception: " + th.getMessage());
            }
        }
        try {
            File file = new File(d());
            if (file.exists()) {
                FileUtil.deleteFile(file);
            }
        } catch (Throwable th2) {
            this.i.b("Unable to delete install folder: " + d() + ", got exception: " + th2.getMessage());
        }
    }

    private String b(int i) {
        String str = SmsConstants.FORMAT_UNKNOWN;
        for (Field field : PackageManager.class.getFields()) {
            if (field.getType() == Integer.TYPE) {
                int modifiers = field.getModifiers();
                if (!((modifiers & 16) == 0 || (modifiers & 1) == 0 || (modifiers & 8) == 0)) {
                    String name = field.getName();
                    if (name.startsWith("INSTALL_FAILED_") || name.startsWith("INSTALL_PARSE_FAILED_")) {
                        try {
                            if (i == field.getInt(null)) {
                                return name;
                            }
                        } catch (IllegalAccessException e) {
                        }
                    }
                }
            }
        }
        return str;
    }

    private boolean i() {
        try {
            String systemProperty = DeviceUtil.getSystemProperty("ro.crypto.state");
            String systemProperty2 = DeviceUtil.getSystemProperty("vold.decrypt");
            if (systemProperty.compareToIgnoreCase("encrypted") != 0) {
                return false;
            }
            if (systemProperty2.compareToIgnoreCase(SmartFaceManager.PAGE_BOTTOM) == 0 || systemProperty2.compareToIgnoreCase("trigger_reset_main") == 0 || systemProperty2.compareToIgnoreCase("trigger_post_fs_dat") == 0 || systemProperty2.compareToIgnoreCase("trigger_restart_min_framework") == 0) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            this.i.a("initialBootUpWhileEncrypted() got exception: ", e);
            return false;
        }
    }

    private int j() {
        Integer valueOf;
        Integer.valueOf(1);
        if (Integer.parseInt(VERSION.SDK) < 17) {
            valueOf = Integer.valueOf(Secure.getInt(this.g.getContentResolver(), e, 1));
        } else {
            valueOf = Integer.valueOf(Global.getInt(this.g.getContentResolver(), e, 1));
        }
        return valueOf.intValue();
    }

    private void c(int i) {
        if (Integer.parseInt(VERSION.SDK) < 17) {
            Secure.putInt(this.g.getContentResolver(), e, i);
        } else {
            Global.putInt(this.g.getContentResolver(), e, i);
        }
    }
}
