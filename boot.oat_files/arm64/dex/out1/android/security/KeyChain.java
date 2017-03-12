package android.security;

import android.app.Activity;
import android.app.AppGlobals;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.security.IKeyChainAliasCallback.Stub;
import android.security.keystore.AndroidKeyStoreProvider;
import android.security.keystore.KeyProperties;
import android.util.Log;
import com.android.org.conscrypt.OpenSSLEngine;
import com.android.org.conscrypt.TrustedCertificateStore;
import com.sec.tima_keychain.TimaKeychain;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.security.InvalidKeyException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class KeyChain {
    public static final String ACCOUNT_TYPE = "com.android.keychain";
    private static final String ACTION_CHOOSER = "com.android.keychain.CHOOSER";
    private static final String ACTION_INSTALL = "android.credentials.INSTALL";
    public static final String ACTION_STORAGE_CHANGED = "android.security.STORAGE_CHANGED";
    private static final String ANDROID_SOURCE = "android";
    private static final String CERT_INSTALLER_PACKAGE = "com.android.certinstaller";
    public static final String EXTRA_ALIAS = "alias";
    public static final String EXTRA_CERTIFICATE = "CERT";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PKCS12 = "PKCS12";
    public static final String EXTRA_RESPONSE = "response";
    public static final String EXTRA_SENDER = "sender";
    public static final String EXTRA_SENDER_PACKAGE_NAME = "senderpackagename";
    public static final String EXTRA_URI = "uri";
    private static final String KEYCHAIN_PACKAGE = "com.android.keychain";
    private static final String TAG = "KeyChain";
    private static final String UCM_KEYCHAIN_SCHEME = "ucmkeychain";
    private static final String UCS_SSL_ENGINE = "ucsengine";

    private static class AliasResponse extends Stub {
        private final KeyChainAliasCallback keyChainAliasResponse;

        private AliasResponse(KeyChainAliasCallback keyChainAliasResponse) {
            this.keyChainAliasResponse = keyChainAliasResponse;
        }

        public void alias(String alias) {
            this.keyChainAliasResponse.alias(alias);
        }
    }

    public static final class KeyChainConnection implements Closeable {
        private final Context context;
        private final IKeyChainService service;
        private final ServiceConnection serviceConnection;

        private KeyChainConnection(Context context, ServiceConnection serviceConnection, IKeyChainService service) {
            this.context = context;
            this.serviceConnection = serviceConnection;
            this.service = service;
        }

        public void close() {
            this.context.unbindService(this.serviceConnection);
        }

        public IKeyChainService getService() {
            return this.service;
        }
    }

    public static Intent createInstallIntent() {
        Intent intent = new Intent("android.credentials.INSTALL");
        try {
            String callingPackageName = AppGlobals.getPackageManager().getNameForUid(Binder.getCallingUid());
            intent.putExtra(EXTRA_SENDER_PACKAGE_NAME, callingPackageName);
            Log.d(TAG, "packagename from createInstallIntent: " + callingPackageName);
        } catch (RemoteException re) {
            Log.d(TAG, "Error while extracting packagename : " + re);
        }
        intent.setClassName(CERT_INSTALLER_PACKAGE, "com.android.certinstaller.CertInstallerMain");
        return intent;
    }

    public static void choosePrivateKeyAlias(Activity activity, KeyChainAliasCallback response, String[] keyTypes, Principal[] issuers, String host, int port, String alias) {
        Uri uri = null;
        if (host != null) {
            uri = new Builder().authority(host + (port != -1 ? ":" + port : "")).build();
        }
        choosePrivateKeyAlias(activity, response, keyTypes, issuers, uri, alias);
    }

    public static void choosePrivateKeyAlias(Activity activity, KeyChainAliasCallback response, String[] keyTypes, Principal[] issuers, Uri uri, String alias) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        } else if (response == null) {
            throw new NullPointerException("response == null");
        } else {
            Intent intent = new Intent(ACTION_CHOOSER);
            intent.setPackage("com.android.keychain");
            intent.putExtra("response", new AliasResponse(response));
            intent.putExtra(EXTRA_URI, uri);
            intent.putExtra(EXTRA_ALIAS, alias);
            intent.putExtra(EXTRA_SENDER, PendingIntent.getActivity(activity, 0, new Intent(), 0));
            activity.startActivity(intent);
        }
    }

    public static PrivateKey getPrivateKey(Context context, String alias) throws KeyChainException, InterruptedException {
        PrivateKey privateKey = null;
        if (alias == null) {
            throw new NullPointerException("alias == null");
        }
        String originalAlias = alias;
        if (isKeyChainUri(alias)) {
            String provider = getSource(alias);
            Log.d(TAG, "Provider : " + provider);
            if (provider != null) {
                originalAlias = getRawAlias(alias);
                Log.d(TAG, "Provider alias: " + originalAlias);
                if (!provider.equals("android")) {
                    return getUCMPrivateKey(alias);
                }
                Log.d(TAG, "Provider is ANDROID_SOURCE, flow default sequence with alias : " + originalAlias);
                alias = originalAlias;
            }
        } else {
            Log.d(TAG, "it is not UCM uri type");
        }
        if (TimaKeychain.isTimaKeystoreAndCCMEnabledForCaller()) {
            try {
                privateKey = TimaKeychain.getPrivateKeyFromOpenSSL(alias);
            } catch (RuntimeException e) {
                Log.d(TAG, "Error retrieving key from CCM for alias: " + alias);
                e.printStackTrace();
            }
        }
        if (privateKey != null) {
            return privateKey;
        }
        KeyChainConnection keyChainConnection = bind(context);
        try {
            String keyId = keyChainConnection.getService().requestPrivateKey(alias);
            if (keyId == null) {
                throw new KeyChainException("keystore had a problem");
            }
            PrivateKey loadAndroidKeyStorePrivateKeyFromKeystore = AndroidKeyStoreProvider.loadAndroidKeyStorePrivateKeyFromKeystore(KeyStore.getInstance(), keyId);
            keyChainConnection.close();
            return loadAndroidKeyStorePrivateKeyFromKeystore;
        } catch (Throwable e2) {
            throw new KeyChainException(e2);
        } catch (Throwable e22) {
            throw new KeyChainException(e22);
        } catch (Throwable e222) {
            throw new KeyChainException(e222);
        } catch (Throwable th) {
            keyChainConnection.close();
        }
    }

    public static X509Certificate[] getCertificateChain(Context context, String alias) throws KeyChainException, InterruptedException {
        X509Certificate[] certChain = null;
        if (alias == null) {
            throw new NullPointerException("alias == null");
        }
        byte[] certificateBytes;
        String originalAlias = alias;
        if (isKeyChainUri(alias)) {
            String provider = getSource(alias);
            Log.d(TAG, "Provider : " + provider);
            if (provider != null) {
                originalAlias = getRawAlias(alias);
                Log.d(TAG, "originalAlias : " + originalAlias);
                if (provider.equals("android")) {
                    Log.d(TAG, "Provider is ANDROID_SOURCE, flow default sequence with alias : " + originalAlias);
                    alias = originalAlias;
                } else {
                    IEDMProxy lService = EDMProxyServiceHelper.getService();
                    if (lService != null) {
                        try {
                            certificateBytes = lService.getCertificateChain(alias);
                            if (certificateBytes == null) {
                                return null;
                            }
                            List<X509Certificate> chain = new TrustedCertificateStore().getCertificateChain(toCertificate(certificateBytes));
                            return (X509Certificate[]) chain.toArray(new X509Certificate[chain.size()]);
                        } catch (Throwable e) {
                            throw new KeyChainException(e);
                        } catch (RemoteException re) {
                            Log.e(TAG, "Remote Exception " + re);
                        }
                    }
                }
            }
        }
        if (TimaKeychain.isTimaKeystoreAndCCMEnabledForCaller()) {
            try {
                X509Certificate[] ccmCertChain = TimaKeychain.getCertificateChainFromTimaKeystore(alias);
                TrustedCertificateStore store = new TrustedCertificateStore();
                if (ccmCertChain != null && ccmCertChain.length > 0) {
                    chain = store.getCertificateChain(ccmCertChain[0]);
                    certChain = (X509Certificate[]) chain.toArray(new X509Certificate[chain.size()]);
                }
            } catch (RuntimeException e2) {
                Log.d(TAG, "Error retrieving certificate from CCM for alias: " + alias);
                e2.printStackTrace();
            } catch (Throwable e3) {
                throw new KeyChainException(e3);
            }
        }
        if (certChain != null) {
            return certChain;
        }
        KeyChainConnection keyChainConnection = bind(context);
        try {
            certificateBytes = keyChainConnection.getService().getCertificate(alias);
            if (certificateBytes == null) {
                keyChainConnection.close();
                return null;
            }
            chain = new TrustedCertificateStore().getCertificateChain(toCertificate(certificateBytes));
            X509Certificate[] x509CertificateArr = (X509Certificate[]) chain.toArray(new X509Certificate[chain.size()]);
            keyChainConnection.close();
            return x509CertificateArr;
        } catch (Throwable e32) {
            throw new KeyChainException(e32);
        } catch (Throwable e322) {
            throw new KeyChainException(e322);
        } catch (Throwable e3222) {
            throw new KeyChainException(e3222);
        } catch (Throwable th) {
            keyChainConnection.close();
        }
    }

    public static boolean isKeyAlgorithmSupported(String algorithm) {
        String algUpper = algorithm.toUpperCase(Locale.US);
        return KeyProperties.KEY_ALGORITHM_EC.equals(algUpper) || KeyProperties.KEY_ALGORITHM_RSA.equals(algUpper);
    }

    @Deprecated
    public static boolean isBoundKeyAlgorithm(String algorithm) {
        if (TimaKeychain.isTimaKeystoreAndCCMEnabledForCaller() && KeyProperties.KEY_ALGORITHM_RSA.equals(algorithm.toUpperCase(Locale.US))) {
            return true;
        }
        if (isKeyAlgorithmSupported(algorithm)) {
            return KeyStore.getInstance().isHardwareBacked(algorithm);
        }
        return false;
    }

    public static X509Certificate toCertificate(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes == null");
        }
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            throw new AssertionError(e);
        }
    }

    public static KeyChainConnection bind(Context context) throws InterruptedException {
        return bindAsUser(context, Process.myUserHandle());
    }

    public static KeyChainConnection bindAsUser(Context context, UserHandle user) throws InterruptedException {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        ensureNotOnMainThread(context);
        final BlockingQueue<IKeyChainService> q = new LinkedBlockingQueue(1);
        ServiceConnection keyChainServiceConnection = new ServiceConnection() {
            volatile boolean mConnectedAtLeastOnce = false;

            public void onServiceConnected(ComponentName name, IBinder service) {
                if (!this.mConnectedAtLeastOnce) {
                    this.mConnectedAtLeastOnce = true;
                    try {
                        q.put(IKeyChainService.Stub.asInterface(service));
                    } catch (InterruptedException e) {
                    }
                }
            }

            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(IKeyChainService.class.getName());
        intent.setComponent(intent.resolveSystemService(context.getPackageManager(), 0));
        if (context.bindServiceAsUser(intent, keyChainServiceConnection, 1, user)) {
            return new KeyChainConnection(context, keyChainServiceConnection, (IKeyChainService) q.take());
        }
        throw new AssertionError("could not bind to KeyChainService");
    }

    private static void ensureNotOnMainThread(Context context) {
        Looper looper = Looper.myLooper();
        if (looper != null && looper == context.getMainLooper()) {
            throw new IllegalStateException("calling this from your main thread can lead to deadlock");
        }
    }

    private static boolean isKeyChainUri(String uri) {
        if (uri == null) {
            return false;
        }
        Log.d(TAG, "isKeyChainUri: " + uri);
        Uri parsedUri = Uri.parse(uri);
        if (parsedUri == null) {
            return false;
        }
        String scheme = parsedUri.getScheme();
        if (scheme == null || !scheme.equals(UCM_KEYCHAIN_SCHEME)) {
            return false;
        }
        return true;
    }

    private static String getSource(String uri) {
        List<String> paths = Uri.parse(uri).getPathSegments();
        if (paths == null) {
            throw new IllegalArgumentException("Source is not known. Invalid URI.");
        }
        try {
            String sourcePath = (String) paths.get(0);
            String uriuid = (String) paths.get(2);
            Log.d(TAG, "getSource: source = " + sourcePath + ", resource type = " + ((String) paths.get(1)) + ", uid = " + uriuid);
            return sourcePath;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getRawAlias(String uri) {
        return Uri.parse(uri).getLastPathSegment();
    }

    private static PrivateKey getUCMPrivateKey(String uri) {
        try {
            return OpenSSLEngine.getInstance("ucsengine").getPrivateKeyById(uri);
        } catch (InvalidKeyException e) {
            Log.w(TAG, "InvalidKeyException", e);
            return null;
        }
    }
}
