package android.webkit;

import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.webkit.IWebViewUpdateService.Stub;
import com.android.internal.R;
import com.android.server.LocalServices;
import dalvik.system.VMRuntime;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class WebViewFactory {
    private static final long CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES = 104857600;
    private static final String CHROMIUM_WEBVIEW_FACTORY = "com.android.webview.chromium.WebViewChromiumFactoryProvider";
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_32 = "/data/misc/shared_relro/libwebviewchromium32.relro";
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_64 = "/data/misc/shared_relro/libwebviewchromium64.relro";
    public static final String CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY = "persist.sys.webview.vmsize";
    private static final boolean DEBUG = false;
    public static final int LIBLOAD_ADDRESS_SPACE_NOT_RESERVED = 2;
    public static final int LIBLOAD_FAILED_JNI_CALL = 7;
    public static final int LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES = 4;
    public static final int LIBLOAD_FAILED_TO_LOAD_LIBRARY = 6;
    public static final int LIBLOAD_FAILED_TO_OPEN_RELRO_FILE = 5;
    public static final int LIBLOAD_FAILED_WAITING_FOR_RELRO = 3;
    public static final int LIBLOAD_SUCCESS = 0;
    public static final int LIBLOAD_WRONG_PACKAGE_NAME = 1;
    private static final String LOGTAG = "WebViewFactory";
    private static final String NULL_WEBVIEW_FACTORY = "com.android.webview.nullwebview.NullWebViewFactoryProvider";
    private static boolean sAddressSpaceReserved = false;
    private static PackageInfo sPackageInfo;
    private static WebViewFactoryProvider sProviderInstance;
    private static final Object sProviderLock = new Object();

    private static class MissingWebViewPackageException extends AndroidRuntimeException {
        public MissingWebViewPackageException(String message) {
            super(message);
        }

        public MissingWebViewPackageException(Exception e) {
            super(e);
        }
    }

    private static class RelroFileCreator {
        private RelroFileCreator() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void main(java.lang.String[] r8) {
            /*
            r7 = 0;
            r2 = 0;
            r3 = dalvik.system.VMRuntime.getRuntime();
            r1 = r3.is64Bit();
            r3 = r8.length;	 Catch:{ all -> 0x00e1 }
            r4 = 2;
            if (r3 != r4) goto L_0x0018;
        L_0x000e:
            r3 = 0;
            r3 = r8[r3];	 Catch:{ all -> 0x00e1 }
            if (r3 == 0) goto L_0x0018;
        L_0x0013:
            r3 = 1;
            r3 = r8[r3];	 Catch:{ all -> 0x00e1 }
            if (r3 != 0) goto L_0x0051;
        L_0x0018:
            r3 = "WebViewFactory";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e1 }
            r4.<init>();	 Catch:{ all -> 0x00e1 }
            r5 = "Invalid RelroFileCreator args: ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r5 = java.util.Arrays.toString(r8);	 Catch:{ all -> 0x00e1 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r4 = r4.toString();	 Catch:{ all -> 0x00e1 }
            android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00e1 }
            r3 = android.webkit.WebViewFactory.getUpdateService();	 Catch:{ RemoteException -> 0x0048 }
            r3.notifyRelroCreationCompleted(r1, r2);	 Catch:{ RemoteException -> 0x0048 }
        L_0x003b:
            if (r2 != 0) goto L_0x0044;
        L_0x003d:
            r3 = "WebViewFactory";
            r4 = "failed to create relro file";
            android.util.Log.e(r3, r4);
        L_0x0044:
            java.lang.System.exit(r7);
        L_0x0047:
            return;
        L_0x0048:
            r0 = move-exception;
            r3 = "WebViewFactory";
            r4 = "error notifying update service";
            android.util.Log.e(r3, r4, r0);
            goto L_0x003b;
        L_0x0051:
            r3 = "WebViewFactory";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e1 }
            r4.<init>();	 Catch:{ all -> 0x00e1 }
            r5 = "RelroFileCreator (64bit = ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r4 = r4.append(r1);	 Catch:{ all -> 0x00e1 }
            r5 = "), ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r5 = " 32-bit lib: ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r5 = 0;
            r5 = r8[r5];	 Catch:{ all -> 0x00e1 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r5 = ", 64-bit lib: ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r5 = 1;
            r5 = r8[r5];	 Catch:{ all -> 0x00e1 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00e1 }
            r4 = r4.toString();	 Catch:{ all -> 0x00e1 }
            android.util.Log.v(r3, r4);	 Catch:{ all -> 0x00e1 }
            r3 = android.webkit.WebViewFactory.sAddressSpaceReserved;	 Catch:{ all -> 0x00e1 }
            if (r3 != 0) goto L_0x00b3;
        L_0x008f:
            r3 = "WebViewFactory";
            r4 = "can't create relro file; address space not reserved";
            android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00e1 }
            r3 = android.webkit.WebViewFactory.getUpdateService();	 Catch:{ RemoteException -> 0x00aa }
            r3.notifyRelroCreationCompleted(r1, r2);	 Catch:{ RemoteException -> 0x00aa }
        L_0x009d:
            if (r2 != 0) goto L_0x00a6;
        L_0x009f:
            r3 = "WebViewFactory";
            r4 = "failed to create relro file";
            android.util.Log.e(r3, r4);
        L_0x00a6:
            java.lang.System.exit(r7);
            goto L_0x0047;
        L_0x00aa:
            r0 = move-exception;
            r3 = "WebViewFactory";
            r4 = "error notifying update service";
            android.util.Log.e(r3, r4, r0);
            goto L_0x009d;
        L_0x00b3:
            r3 = 0;
            r3 = r8[r3];	 Catch:{ all -> 0x00e1 }
            r4 = 1;
            r4 = r8[r4];	 Catch:{ all -> 0x00e1 }
            r5 = "/data/misc/shared_relro/libwebviewchromium32.relro";
            r6 = "/data/misc/shared_relro/libwebviewchromium64.relro";
            r2 = android.webkit.WebViewFactory.nativeCreateRelroFile(r3, r4, r5, r6);	 Catch:{ all -> 0x00e1 }
            if (r2 == 0) goto L_0x00c3;
        L_0x00c3:
            r3 = android.webkit.WebViewFactory.getUpdateService();	 Catch:{ RemoteException -> 0x00d8 }
            r3.notifyRelroCreationCompleted(r1, r2);	 Catch:{ RemoteException -> 0x00d8 }
        L_0x00ca:
            if (r2 != 0) goto L_0x00d3;
        L_0x00cc:
            r3 = "WebViewFactory";
            r4 = "failed to create relro file";
            android.util.Log.e(r3, r4);
        L_0x00d3:
            java.lang.System.exit(r7);
            goto L_0x0047;
        L_0x00d8:
            r0 = move-exception;
            r3 = "WebViewFactory";
            r4 = "error notifying update service";
            android.util.Log.e(r3, r4, r0);
            goto L_0x00ca;
        L_0x00e1:
            r3 = move-exception;
            r4 = android.webkit.WebViewFactory.getUpdateService();	 Catch:{ RemoteException -> 0x00f6 }
            r4.notifyRelroCreationCompleted(r1, r2);	 Catch:{ RemoteException -> 0x00f6 }
        L_0x00e9:
            if (r2 != 0) goto L_0x00f2;
        L_0x00eb:
            r4 = "WebViewFactory";
            r5 = "failed to create relro file";
            android.util.Log.e(r4, r5);
        L_0x00f2:
            java.lang.System.exit(r7);
            throw r3;
        L_0x00f6:
            r0 = move-exception;
            r4 = "WebViewFactory";
            r5 = "error notifying update service";
            android.util.Log.e(r4, r5, r0);
            goto L_0x00e9;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.webkit.WebViewFactory.RelroFileCreator.main(java.lang.String[]):void");
        }
    }

    private static native boolean nativeCreateRelroFile(String str, String str2, String str3, String str4);

    private static native int nativeLoadWithRelroFile(String str, String str2, String str3, String str4);

    private static native boolean nativeReserveAddressSpace(long j);

    public static String getWebViewPackageName() {
        return AppGlobals.getInitialApplication().getString(R.string.config_webViewPackageName);
    }

    private static PackageInfo fetchPackageInfo() {
        try {
            return AppGlobals.getInitialApplication().getPackageManager().getPackageInfo(getWebViewPackageName(), 128);
        } catch (Exception e) {
            throw new MissingWebViewPackageException(e);
        }
    }

    private static ApplicationInfo getWebViewApplicationInfo() {
        if (sPackageInfo == null) {
            return fetchPackageInfo().applicationInfo;
        }
        return sPackageInfo.applicationInfo;
    }

    private static String getWebViewLibrary(ApplicationInfo ai) {
        if (ai.metaData != null) {
            return ai.metaData.getString("com.android.webview.WebViewLibrary");
        }
        return null;
    }

    public static PackageInfo getLoadedPackageInfo() {
        return sPackageInfo;
    }

    public static int loadWebViewNativeLibraryFromPackage(String packageName) {
        sPackageInfo = fetchPackageInfo();
        if (packageName == null || !packageName.equals(sPackageInfo.packageName)) {
            return 1;
        }
        return loadNativeLibrary();
    }

    static WebViewFactoryProvider getProvider() {
        WebViewFactoryProvider webViewFactoryProvider;
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                webViewFactoryProvider = sProviderInstance;
            } else {
                int uid = Process.myUid();
                if (uid == 0 || uid == 1000) {
                    throw new UnsupportedOperationException("For security reasons, WebView is not allowed in privileged processes");
                }
                Trace.traceBegin(16, "WebViewFactory.getProvider()");
                try {
                    Class<WebViewFactoryProvider> providerClass = getProviderClass();
                    ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
                    int i = "providerClass.newInstance()";
                    Trace.traceBegin(16, i);
                    try {
                        Class[] clsArr = new Class[i];
                        clsArr[0] = WebViewDelegate.class;
                        sProviderInstance = (WebViewFactoryProvider) providerClass.getConstructor(clsArr).newInstance(new Object[]{new WebViewDelegate()});
                        webViewFactoryProvider = sProviderInstance;
                        Trace.traceEnd(16);
                        StrictMode.setThreadPolicy(oldPolicy);
                        Trace.traceEnd(16);
                    } catch (Exception e) {
                        Log.e(LOGTAG, "error instantiating provider", e);
                        throw new AndroidRuntimeException(e);
                    } catch (Throwable th) {
                        Trace.traceEnd(16);
                        StrictMode.setThreadPolicy(oldPolicy);
                    }
                } finally {
                    Trace.traceEnd(16);
                }
            }
        }
        return webViewFactoryProvider;
    }

    private static Class<WebViewFactoryProvider> getProviderClass() {
        Class<WebViewFactoryProvider> chromiumProviderClass;
        try {
            sPackageInfo = fetchPackageInfo();
            Log.i(LOGTAG, "Loading " + sPackageInfo.packageName + " version " + sPackageInfo.versionName + " (code " + sPackageInfo.versionCode + ")");
            Trace.traceBegin(16, "WebViewFactory.loadNativeLibrary()");
            loadNativeLibrary();
            Trace.traceEnd(16);
            Trace.traceBegin(16, "WebViewFactory.getChromiumProviderClass()");
            try {
                chromiumProviderClass = getChromiumProviderClass();
                Trace.traceEnd(16);
            } catch (Exception e) {
                Log.e(LOGTAG, "error loading provider", e);
                throw new AndroidRuntimeException(e);
            } catch (Throwable th) {
                Trace.traceEnd(16);
            }
        } catch (Exception e2) {
            try {
                chromiumProviderClass = Class.forName(NULL_WEBVIEW_FACTORY);
            } catch (ClassNotFoundException e3) {
                Log.e(LOGTAG, "Chromium WebView package does not exist", e2);
                throw new AndroidRuntimeException(e2);
            }
        }
        return chromiumProviderClass;
    }

    private static Class<WebViewFactoryProvider> getChromiumProviderClass() throws ClassNotFoundException {
        Application initialApplication = AppGlobals.getInitialApplication();
        try {
            Context webViewContext = initialApplication.createPackageContext(sPackageInfo.packageName, 3);
            initialApplication.getAssets().addAssetPath(webViewContext.getApplicationInfo().sourceDir);
            ClassLoader clazzLoader = webViewContext.getClassLoader();
            Trace.traceBegin(16, "Class.forName()");
            Class<WebViewFactoryProvider> cls = Class.forName(CHROMIUM_WEBVIEW_FACTORY, true, clazzLoader);
            Trace.traceEnd(16);
            return cls;
        } catch (Exception e) {
            throw new MissingWebViewPackageException(e);
        } catch (Throwable th) {
            Trace.traceEnd(16);
        }
    }

    public static void prepareWebViewInZygote() {
        try {
            System.loadLibrary("webviewchromium_loader");
            long addressSpaceToReserve = SystemProperties.getLong(CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
            sAddressSpaceReserved = nativeReserveAddressSpace(addressSpaceToReserve);
            if (!sAddressSpaceReserved) {
                Log.e(LOGTAG, "reserving " + addressSpaceToReserve + " bytes of address space failed");
            }
        } catch (Throwable t) {
            Log.e(LOGTAG, "error preparing native loader", t);
        }
    }

    public static void prepareWebViewInSystemServer() {
        String[] nativePaths = null;
        try {
            nativePaths = getWebViewNativeLibraryPaths();
        } catch (Throwable t) {
            Log.e(LOGTAG, "error preparing webview native library", t);
        }
        prepareWebViewInSystemServer(nativePaths);
    }

    private static void prepareWebViewInSystemServer(String[] nativeLibraryPaths) {
        if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
            createRelroFile(false, nativeLibraryPaths);
        }
        if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
            createRelroFile(true, nativeLibraryPaths);
        }
    }

    public static void onWebViewUpdateInstalled() {
        String[] split;
        ZipFile z;
        Throwable th;
        Throwable th2;
        String[] strArr = null;
        ZipEntry e;
        try {
            strArr = getWebViewNativeLibraryPaths();
            if (strArr != null) {
                long newVmSize = 0;
                for (String path : strArr) {
                    if (!(path == null || TextUtils.isEmpty(path))) {
                        File f = new File(path);
                        if (f.exists()) {
                            newVmSize = Math.max(newVmSize, f.length());
                        } else {
                            if (path.contains("!/")) {
                                split = TextUtils.split(path, "!/");
                                if (split.length == 2) {
                                    z = new ZipFile(split[0]);
                                    th = null;
                                    try {
                                        e = z.getEntry(split[1]);
                                        if (e != null && e.getMethod() == 0) {
                                            newVmSize = Math.max(newVmSize, e.getSize());
                                            if (z != null) {
                                                if (null != null) {
                                                    try {
                                                        z.close();
                                                    } catch (Throwable x2) {
                                                        null.addSuppressed(x2);
                                                    }
                                                } else {
                                                    z.close();
                                                }
                                            }
                                        } else if (z != null) {
                                            if (null != null) {
                                                try {
                                                    z.close();
                                                } catch (Throwable x22) {
                                                    null.addSuppressed(x22);
                                                }
                                            } else {
                                                z.close();
                                            }
                                        }
                                    } catch (Throwable th3) {
                                        Throwable th4 = th3;
                                        th3 = th2;
                                        th2 = th4;
                                    }
                                }
                            }
                            Log.e(LOGTAG, "error sizing load for " + path);
                        }
                    }
                }
                newVmSize = Math.max(2 * newVmSize, CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
                Log.d(LOGTAG, "Setting new address space to " + newVmSize);
                SystemProperties.set(CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, Long.toString(newVmSize));
            }
        } catch (ZipEntry e2) {
            Log.e(LOGTAG, "error reading APK file " + split[0] + ", ", e2);
        } catch (Throwable t) {
            Log.e(LOGTAG, "error preparing webview native library", t);
        }
        prepareWebViewInSystemServer(strArr);
        return;
        if (z != null) {
            if (th3 != null) {
                try {
                    z.close();
                } catch (Throwable x222) {
                    th3.addSuppressed(x222);
                }
            } else {
                z.close();
            }
        }
        throw th2;
        throw th2;
    }

    private static String getLoadFromApkPath(String apkPath, String[] abiList, String nativeLibFileName) {
        try {
            Throwable th;
            ZipFile z = new ZipFile(apkPath);
            Throwable th2 = null;
            String[] arr$ = abiList;
            try {
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    String entry = "lib/" + arr$[i$] + "/" + nativeLibFileName;
                    ZipEntry e = z.getEntry(entry);
                    if (e == null || e.getMethod() != 0) {
                        i$++;
                    } else {
                        String str = apkPath + "!/" + entry;
                        if (z == null) {
                            return str;
                        }
                        if (th2 != null) {
                            try {
                                z.close();
                                return str;
                            } catch (Throwable x2) {
                                th2.addSuppressed(x2);
                                return str;
                            }
                        }
                        z.close();
                        return str;
                    }
                }
                if (z != null) {
                    if (th2 != null) {
                        try {
                            z.close();
                        } catch (Throwable x22) {
                            th2.addSuppressed(x22);
                        }
                    } else {
                        z.close();
                    }
                }
                return "";
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            if (z != null) {
                if (th22 != null) {
                    try {
                        z.close();
                    } catch (Throwable x222) {
                        th22.addSuppressed(x222);
                    }
                } else {
                    z.close();
                }
            }
            throw th;
            throw th;
        } catch (Exception e2) {
            throw new MissingWebViewPackageException(e2);
        }
    }

    private static String[] getWebViewNativeLibraryPaths() {
        String path64;
        String path32;
        ApplicationInfo ai = getWebViewApplicationInfo();
        String NATIVE_LIB_FILE_NAME = getWebViewLibrary(ai);
        boolean primaryArchIs64bit = VMRuntime.is64BitAbi(ai.primaryCpuAbi);
        if (TextUtils.isEmpty(ai.secondaryCpuAbi)) {
            if (primaryArchIs64bit) {
                path64 = ai.nativeLibraryDir;
                path32 = "";
            } else {
                path32 = ai.nativeLibraryDir;
                path64 = "";
            }
        } else if (primaryArchIs64bit) {
            path64 = ai.nativeLibraryDir;
            path32 = ai.secondaryNativeLibraryDir;
        } else {
            path64 = ai.secondaryNativeLibraryDir;
            path32 = ai.nativeLibraryDir;
        }
        if (!TextUtils.isEmpty(path32)) {
            path32 = path32 + "/" + NATIVE_LIB_FILE_NAME;
            if (!new File(path32).exists()) {
                path32 = getLoadFromApkPath(ai.sourceDir, Build.SUPPORTED_32_BIT_ABIS, NATIVE_LIB_FILE_NAME);
            }
        }
        if (!TextUtils.isEmpty(path64)) {
            path64 = path64 + "/" + NATIVE_LIB_FILE_NAME;
            if (!new File(path64).exists()) {
                path64 = getLoadFromApkPath(ai.sourceDir, Build.SUPPORTED_64_BIT_ABIS, NATIVE_LIB_FILE_NAME);
            }
        }
        return new String[]{path32, path64};
    }

    private static void createRelroFile(final boolean is64Bit, String[] nativeLibraryPaths) {
        String abi;
        if (is64Bit) {
            abi = Build.SUPPORTED_64_BIT_ABIS[0];
        } else {
            abi = Build.SUPPORTED_32_BIT_ABIS[0];
        }
        Runnable crashHandler = new Runnable() {
            public void run() {
                try {
                    Log.e(WebViewFactory.LOGTAG, "relro file creator for " + abi + " crashed. Proceeding without");
                    WebViewFactory.getUpdateService().notifyRelroCreationCompleted(is64Bit, false);
                } catch (RemoteException e) {
                    Log.e(WebViewFactory.LOGTAG, "Cannot reach WebViewUpdateService. " + e.getMessage());
                }
            }
        };
        if (nativeLibraryPaths != null) {
            try {
                if (!(nativeLibraryPaths[0] == null || nativeLibraryPaths[1] == null)) {
                    if (((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).startIsolatedProcess(RelroFileCreator.class.getName(), nativeLibraryPaths, "WebViewLoader-" + abi, abi, 1037, crashHandler) <= 0) {
                        throw new Exception("Failed to start the relro file creator process");
                    }
                    return;
                }
            } catch (Throwable t) {
                Log.e(LOGTAG, "error starting relro file creator for abi " + abi, t);
                crashHandler.run();
                return;
            }
        }
        throw new IllegalArgumentException("Native library paths to the WebView RelRo process must not be null!");
    }

    private static int loadNativeLibrary() {
        if (sAddressSpaceReserved) {
            try {
                getUpdateService().waitForRelroCreationCompleted(VMRuntime.getRuntime().is64Bit());
                try {
                    String[] args = getWebViewNativeLibraryPaths();
                    int result = nativeLoadWithRelroFile(args[0], args[1], CHROMIUM_WEBVIEW_NATIVE_RELRO_32, CHROMIUM_WEBVIEW_NATIVE_RELRO_64);
                    if (result == 0) {
                        return result;
                    }
                    Log.w(LOGTAG, "failed to load with relro file, proceeding without");
                    return result;
                } catch (MissingWebViewPackageException e) {
                    Log.e(LOGTAG, "Failed to list WebView package libraries for loadNativeLibrary", e);
                    return 4;
                }
            } catch (RemoteException e2) {
                Log.e(LOGTAG, "error waiting for relro creation, proceeding without", e2);
                return 3;
            }
        }
        Log.e(LOGTAG, "can't load with relro file; address space not reserved");
        return 2;
    }

    private static IWebViewUpdateService getUpdateService() {
        return Stub.asInterface(ServiceManager.getService("webviewupdate"));
    }
}
