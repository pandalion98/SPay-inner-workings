package android.security;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.pm.UserInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.sec.enterprise.auditlog.AuditEvents;
import android.sec.enterprise.auditlog.AuditLog;
import android.sec.enterprise.certificate.CertificatePolicy;
import android.security.IKeystoreServiceMDFPP.Stub;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyExpiredException;
import android.security.keystore.KeyNotYetValidException;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.samsung.android.security.CCManager;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KeyStore {
    private static final String CERTIFICATE_STRING = "certificate";
    public static final int FLAG_ENCRYPTED = 1;
    public static final int FLAG_NONE = 0;
    public static final int KEY_NOT_FOUND = 7;
    public static final int LOCKED = 2;
    public static final int NO_ERROR = 1;
    public static final int OP_AUTH_NEEDED = 15;
    public static final int PERMISSION_DENIED = 6;
    public static final int PROTOCOL_ERROR = 5;
    private static final int[] SYSTEM_CREDENTIAL_UIDS = new int[]{1010, 1016, 0, 1000};
    public static final int SYSTEM_ERROR = 4;
    private static final String TAG = "KeyStore";
    public static final int UID_SELF = -1;
    public static final int UNDEFINED_ACTION = 9;
    public static final int UNINITIALIZED = 3;
    public static final int VALUE_CORRUPTED = 8;
    public static final int WRONG_PASSWORD = 10;
    private IEDMProxy lMdmService;
    private final IKeystoreService mBinder;
    private final Context mContext;
    private int mError = 1;
    private IBinder mToken;

    public enum State {
        UNLOCKED,
        LOCKED,
        UNINITIALIZED
    }

    private KeyStore(IKeystoreService binder) {
        this.mBinder = binder;
        this.mContext = getApplicationContext();
        this.lMdmService = EDMProxyServiceHelper.getService();
    }

    public static Context getApplicationContext() {
        Application application = ActivityThread.currentApplication();
        if (application != null) {
            return application;
        }
        throw new IllegalStateException("Failed to obtain application Context from ActivityThread");
    }

    public static KeyStore getInstance() {
        IKeystoreService keystore;
        if (CCManager.isMdfEnforced()) {
            keystore = Stub.asInterface(ServiceManager.getService("android.security.keystore"));
        } else {
            keystore = IKeystoreService.Stub.asInterface(ServiceManager.getService("android.security.keystore"));
        }
        return new KeyStore(keystore);
    }

    private synchronized IBinder getToken() {
        if (this.mToken == null) {
            this.mToken = new Binder();
        }
        return this.mToken;
    }

    private String getRequestorInfo() {
        String role = "";
        String packageName = "";
        if (Process.myUid() == 1000) {
            role = "SystemApp";
        } else {
            role = "UserApp";
        }
        try {
            Object mAm = Class.forName("android.app.ActivityManagerNative").getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            for (RunningAppProcessInfo processInfo : (List) mAm.getClass().getMethod("getRunningAppProcesses", new Class[0]).invoke(mAm, new Object[0])) {
                if (processInfo.pid == Process.myPid()) {
                    packageName = processInfo.processName;
                    break;
                }
            }
            IBinder bEdm = (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE});
            Object mEdm = Class.forName("android.app.enterprise.IEnterpriseDeviceManager$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{bEdm});
            if (((Boolean) mEdm.getClass().getMethod("packageHasActiveAdmins", new Class[]{String.class}).invoke(mEdm, new Object[]{packageName})).booleanValue()) {
                role = role + "|Administrator";
            } else {
                role = role + "|NonAdministrator";
            }
        } catch (Exception e) {
            Log.d(TAG, "Administrator status cannot be defined for requestor: uid=" + Process.myUid() + " pid=" + Process.myPid(), e);
        }
        return packageName + ": uid=" + Process.myUid() + " role=" + role;
    }

    public State state(int userId) {
        try {
            switch (this.mBinder.getState(userId)) {
                case 1:
                    return State.UNLOCKED;
                case 2:
                    return State.LOCKED;
                case 3:
                    return State.UNINITIALIZED;
                default:
                    throw new AssertionError(this.mError);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(e);
        }
    }

    public State state() {
        return state(UserHandle.myUserId());
    }

    public boolean isUnlocked() {
        return state() == State.UNLOCKED;
    }

    public byte[] get(String key) {
        try {
            return this.mBinder.get(key);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean put(String key, byte[] value, int uid, int flags) {
        return insert(key, value, uid, flags) == 1;
    }

    public int insert(String key, byte[] value, int uid, int flags) {
        String keyStoreString;
        int myPid;
        String simpleName;
        StringBuilder append;
        Object obj;
        try {
            int userId = getUserId(uid);
            keyStoreString = getKeystoreString(uid);
            CertificatePolicy certPolicy = EnterpriseDeviceManager.getInstance().getCertificatePolicy();
            if (certPolicy.isCaCertificateTrustedAsUser(value, false, userId) && certPolicy.validateCertificateAtInstallAsUser(value, userId) == -1) {
                int ret = this.mBinder.insert(key, value, uid, flags);
                if (keyStoreString.isEmpty() || !AuditLog.isAuditLogEnabledAsUser(userId)) {
                    return ret;
                }
                List<X509Certificate> certificates = convertFromPem(value);
                if (ret == 1) {
                    for (X509Certificate certInfo : certificates) {
                        AuditLog.logPrivilegedAsUser(5, 1, true, Process.myPid(), getClass().getSimpleName(), "Installing " + getKeyString(key) + " succeeded. Keystore : " + keyStoreString + ", Alias : " + key + ", Subject : " + (certInfo == null ? "Not available" : certInfo.getSubjectDN()) + ", Issuer : " + (certInfo == null ? "Not available" : certInfo.getIssuerDN()), getUserId(uid, true));
                    }
                    return ret;
                }
                for (X509Certificate certInfo2 : certificates) {
                    AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Installing " + getKeyString(key) + " failed. Keystore : " + keyStoreString + ", Alias : " + key + ", Subject : " + (certInfo2 == null ? "Not available" : certInfo2.getSubjectDN()) + ", Issuer : " + (certInfo2 == null ? "Not available" : certInfo2.getIssuerDN()), getUserId(uid, true));
                }
                return ret;
            }
            if (!keyStoreString.isEmpty() && AuditLog.isAuditLogEnabledAsUser(userId)) {
                for (X509Certificate certInfo22 : convertFromPem(value)) {
                    myPid = Process.myPid();
                    simpleName = getClass().getSimpleName();
                    append = new StringBuilder().append("Installing ").append(getKeyString(key)).append(" failed. Keystore : ").append(keyStoreString).append(", Alias : ").append(key).append(", Subject : ");
                    if (certInfo22 == null) {
                        obj = "Not available";
                    } else {
                        obj = certInfo22.getSubjectDN();
                    }
                    append = append.append(obj).append(", Issuer : ");
                    if (certInfo22 == null) {
                        obj = "Not available";
                    } else {
                        obj = certInfo22.getIssuerDN();
                    }
                    AuditLog.logPrivilegedAsUser(1, 1, false, myPid, simpleName, append.append(obj).toString(), getUserId(uid, true));
                }
            }
            Log.d(TAG, "Put not allowed. Untrusted certificate.");
            return 6;
        } catch (RemoteException e) {
            keyStoreString = getKeystoreString(uid);
            if (!keyStoreString.isEmpty() && AuditLog.isAuditLogEnabledAsUser(getUserId(uid))) {
                for (X509Certificate certInfo222 : convertFromPem(value)) {
                    myPid = Process.myPid();
                    simpleName = getClass().getSimpleName();
                    append = new StringBuilder().append("Installing ").append(getKeyString(key)).append(" failed. Keystore : ").append(keyStoreString).append(", Alias : ").append(key).append(", Subject : ");
                    if (certInfo222 == null) {
                        obj = "Not available";
                    } else {
                        obj = certInfo222.getSubjectDN();
                    }
                    append = append.append(obj).append(", Issuer : ");
                    if (certInfo222 == null) {
                        obj = "Not available";
                    } else {
                        obj = certInfo222.getIssuerDN();
                    }
                    AuditLog.logPrivilegedAsUser(1, 1, false, myPid, simpleName, append.append(obj).toString(), getUserId(uid, true));
                }
            }
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean delete(String key, int uid) {
        boolean isAuditLogEnabledAsUser = AuditLog.isAuditLogEnabledAsUser(getUserId(uid));
        String logDetails = "";
        boolean isMdfEnforced = CCManager.isMdfEnforced();
        if (isMdfEnforced) {
            logDetails = String.format(" (Keystore=%s, key=%s, uid=%s, requested by %s)", new Object[]{getKeystoreString(uid), key, Integer.valueOf(uid), getRequestorInfo()});
        }
        List<X509Certificate> certList = new ArrayList();
        if (isAuditLogEnabledAsUser) {
            byte[] cert = getByUid(key, uid);
            if (cert != null) {
                certList = convertFromPem(cert);
            }
        }
        try {
            int ret = this.mBinder.del(key, uid);
            if (isMdfEnforced) {
                if (ret == 1) {
                    AuditLog.logPrivilegedAsUser(5, 1, true, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_DESTRUCTION + logDetails + AuditEvents.SUCCEEDED, getUserId(uid, true));
                } else if (ret != 1) {
                    AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_DESTRUCTION + logDetails + AuditEvents.FAILED + " with error " + ret, getUserId(uid, true));
                }
            }
            if (isAuditLogEnabledAsUser) {
                auditLogCertificateOrPrivateKeyInfo(key, uid, certList, ret == 1);
            }
            return ret == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            if (isMdfEnforced) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_DESTRUCTION + logDetails + AuditEvents.FAILED + " cannot connect to keystore", getUserId(uid, true));
            }
            if (isAuditLogEnabledAsUser) {
                auditLogCertificateOrPrivateKeyInfo(key, uid, certList, false);
            }
            return false;
        }
    }

    private void auditLogCertificateOrPrivateKeyInfo(String key, int uid, List<X509Certificate> certList, boolean success) {
        String keyStoreString = getKeystoreString(uid);
        if (!keyStoreString.isEmpty() && !isPrivateKeyPrefix(key)) {
            for (X509Certificate certificate : certList) {
                AuditLog.logPrivilegedAsUser(success ? 5 : 1, 1, success, Process.myPid(), getClass().getSimpleName(), "Deleting " + getKeyString(key) + (success ? " succeeded." : " failed.") + " Keystore : " + keyStoreString + ", Alias : " + key + ", Subject : " + (certificate == null ? "Not available" : certificate.getSubjectDN()), getUserId(uid, true));
            }
        }
    }

    public boolean delete(String key) {
        return delete(key, -1);
    }

    public boolean contains(String key, int uid) {
        try {
            if (this.mBinder.exist(key, uid) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean contains(String key) {
        return contains(key, -1);
    }

    public String[] list(String prefix, int uid) {
        try {
            return this.mBinder.list(prefix, uid);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public String[] list(String prefix) {
        return list(prefix, -1);
    }

    public boolean reset() {
        try {
            if (this.mBinder.reset() == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock(int userId) {
        try {
            if (this.mBinder.lock(userId) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock() {
        return lock(UserHandle.myUserId());
    }

    public boolean unlock(int userId, String password) {
        try {
            this.mError = this.mBinder.unlock(userId, password);
            if (this.mError == 1) {
                refreshRollbackUserKeystore(UserHandle.getUserId(Binder.getCallingUid()));
            }
            if (this.mError == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean unlock(String password) {
        return unlock(UserHandle.getUserId(Process.myUid()), password);
    }

    public boolean isEmpty(int userId) {
        try {
            return this.mBinder.isEmpty(userId) != 0;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean isEmpty() {
        return isEmpty(UserHandle.myUserId());
    }

    public boolean generate(String key, int uid, int keyType, int keySize, int flags, byte[][] args) {
        try {
            int ret = this.mBinder.generate(key, uid, keyType, keySize, flags, new KeystoreArguments(args));
            if (ret != 1 && CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Key generation failed. with error " + ret, getUserId(uid, true));
            }
            return ret == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            if (CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Key generation failed. cannot connect to KeyStore", getUserId(uid, true));
            }
            return false;
        }
    }

    public boolean importKey(String keyName, byte[] key, int uid, int flags) {
        String logDetails = "";
        if (CCManager.isMdfEnforced()) {
            logDetails = String.format(" (Keystore=%s, keyName=%s, uid=%s, requested by %s)", new Object[]{getKeystoreString(uid), keyName, Integer.valueOf(uid), getRequestorInfo()});
        }
        try {
            int ret = this.mBinder.import_key(keyName, key, uid, flags);
            if (ret == 1 && CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(5, 1, true, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.SUCCEEDED, getUserId(uid, true));
            } else if (ret != 1 && CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.FAILED + " with error " + ret, getUserId(uid, true));
            }
            if (ret == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            if (CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.FAILED + " cannot connect to keystore", getUserId(uid, true));
            }
            return false;
        }
    }

    public byte[] sign(String key, byte[] data) {
        try {
            return this.mBinder.sign(key, data);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean verify(String key, byte[] data, byte[] signature) {
        try {
            if (this.mBinder.verify(key, data, signature) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean grant(String key, int uid) {
        try {
            if (this.mBinder.grant(key, uid) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean ungrant(String key, int uid) {
        try {
            if (this.mBinder.ungrant(key, uid) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public long getmtime(String key) {
        try {
            long millis = this.mBinder.getmtime(key);
            if (millis == -1) {
                return -1;
            }
            return 1000 * millis;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return -1;
        }
    }

    public boolean duplicate(String srcKey, int srcUid, String destKey, int destUid) {
        try {
            if (this.mBinder.duplicate(srcKey, srcUid, destKey, destUid) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean isHardwareBacked() {
        return isHardwareBacked(KeyProperties.KEY_ALGORITHM_RSA);
    }

    public boolean isHardwareBacked(String keyType) {
        try {
            if (this.mBinder.is_hardware_backed(keyType.toUpperCase(Locale.US)) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean clearUid(int uid) {
        String keyStoreString;
        try {
            int ret = this.mBinder.clear_uid((long) uid);
            keyStoreString = getKeystoreString(uid);
            if (!keyStoreString.isEmpty()) {
                if (ret == 1) {
                    AuditLog.logPrivilegedAsUser(5, 1, true, Process.myPid(), getClass().getSimpleName(), "Clearing credentials succeeded. Keystore : " + keyStoreString, getUserId(uid, true));
                } else {
                    AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Clearing credentials failed. Keystore : " + keyStoreString, getUserId(uid, true));
                }
            }
            return ret == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            keyStoreString = getKeystoreString(uid);
            if (!keyStoreString.isEmpty()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Clearing credentials failed. Keystore : " + keyStoreString, getUserId(uid, true));
            }
            return false;
        }
    }

    public int getLastError() {
        return this.mError;
    }

    public boolean addRngEntropy(byte[] data) {
        try {
            if (this.mBinder.addRngEntropy(data) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) {
        try {
            int ret = this.mBinder.generateKey(alias, args, entropy, uid, flags, outCharacteristics);
            if (ret == 1 || !CCManager.isMdfEnforced()) {
                return ret;
            }
            AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Key generation failed. with error " + ret, getUserId(uid, true));
            return ret;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            if (CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), "Key generation failed. cannot connect to keystore", getUserId(uid, true));
            }
            return 4;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int flags, KeyCharacteristics outCharacteristics) {
        return generateKey(alias, args, entropy, -1, flags, outCharacteristics);
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, KeyCharacteristics outCharacteristics) {
        try {
            return this.mBinder.getKeyCharacteristics(alias, clientId, appId, outCharacteristics);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) {
        String logDetails = "";
        if (CCManager.isMdfEnforced()) {
            logDetails = " (alias=" + alias + ", uid=" + uid + ", requested by " + getRequestorInfo() + ")";
        }
        try {
            int ret = this.mBinder.importKey(alias, args, format, keyData, uid, flags, outCharacteristics);
            if (ret == 1 && CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(5, 1, true, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.SUCCEEDED, getUserId(uid, true));
                return ret;
            } else if (ret == 1 || !CCManager.isMdfEnforced()) {
                return ret;
            } else {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.FAILED + " with error " + ret, getUserId(uid, true));
                return ret;
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            if (CCManager.isMdfEnforced()) {
                AuditLog.logPrivilegedAsUser(1, 1, false, Process.myPid(), getClass().getSimpleName(), AuditEvents.KEY_IMPORTING + logDetails + AuditEvents.FAILED + " cannot connect to keystore", getUserId(uid, true));
            }
            return 4;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int flags, KeyCharacteristics outCharacteristics) {
        return importKey(alias, args, format, keyData, -1, flags, outCharacteristics);
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId) {
        try {
            return this.mBinder.exportKey(alias, format, clientId, appId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy) {
        try {
            return this.mBinder.begin(getToken(), alias, purpose, pruneable, args, entropy);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public OperationResult update(IBinder token, KeymasterArguments arguments, byte[] input) {
        try {
            return this.mBinder.update(token, arguments, input);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature, byte[] entropy) {
        try {
            return this.mBinder.finish(token, arguments, signature, entropy);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature) {
        return finish(token, arguments, signature, null);
    }

    public int abort(IBinder token) {
        try {
            return this.mBinder.abort(token);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean isOperationAuthorized(IBinder token) {
        try {
            return this.mBinder.isOperationAuthorized(token);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int addAuthToken(byte[] authToken) {
        try {
            return this.mBinder.addAuthToken(authToken);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean onUserPasswordChanged(int userId, String newPassword) {
        if (newPassword == null) {
            newPassword = "";
        }
        try {
            if (this.mBinder.onUserPasswordChanged(userId, newPassword) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public void onUserAdded(int userId, int parentId) {
        try {
            this.mBinder.onUserAdded(userId, parentId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public void onUserAdded(int userId) {
        onUserAdded(userId, -1);
    }

    public void onUserRemoved(int userId) {
        try {
            this.mBinder.onUserRemoved(userId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public boolean onUserPasswordChanged(String newPassword) {
        return onUserPasswordChanged(UserHandle.getUserId(Process.myUid()), newPassword);
    }

    public static KeyStoreException getKeyStoreException(int errorCode) {
        if (errorCode > 0) {
            switch (errorCode) {
                case 1:
                    return new KeyStoreException(errorCode, "OK");
                case 2:
                    return new KeyStoreException(errorCode, "User authentication required");
                case 3:
                    return new KeyStoreException(errorCode, "Keystore not initialized");
                case 4:
                    return new KeyStoreException(errorCode, "System error");
                case 6:
                    return new KeyStoreException(errorCode, "Permission denied");
                case 7:
                    return new KeyStoreException(errorCode, "Key not found");
                case 8:
                    return new KeyStoreException(errorCode, "Key blob corrupted");
                case 15:
                    return new KeyStoreException(errorCode, "Operation requires authorization");
                default:
                    return new KeyStoreException(errorCode, String.valueOf(errorCode));
            }
        }
        switch (errorCode) {
            case KeymasterDefs.KM_ERROR_INVALID_AUTHORIZATION_TIMEOUT /*-16*/:
                return new KeyStoreException(errorCode, "Invalid user authentication validity duration");
            default:
                return new KeyStoreException(errorCode, KeymasterDefs.getErrorMessage(errorCode));
        }
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, KeyStoreException e) {
        switch (e.getErrorCode()) {
            case KeymasterDefs.KM_ERROR_KEY_USER_NOT_AUTHENTICATED /*-26*/:
            case 15:
                KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
                int getKeyCharacteristicsErrorCode = getKeyCharacteristics(keystoreKeyAlias, null, null, keyCharacteristics);
                if (getKeyCharacteristicsErrorCode != 1) {
                    return new InvalidKeyException("Failed to obtained key characteristics", getKeyStoreException(getKeyCharacteristicsErrorCode));
                }
                List<BigInteger> keySids = keyCharacteristics.getUnsignedLongs(KeymasterDefs.KM_TAG_USER_SECURE_ID);
                if (keySids.isEmpty()) {
                    return new KeyPermanentlyInvalidatedException();
                }
                long rootSid = GateKeeper.getSecureUserId();
                if (rootSid != 0 && keySids.contains(KeymasterArguments.toUint64(rootSid))) {
                    return new UserNotAuthenticatedException();
                }
                long fingerprintOnlySid = getFingerprintOnlySid();
                if (fingerprintOnlySid == 0 || !keySids.contains(KeymasterArguments.toUint64(fingerprintOnlySid))) {
                    return new KeyPermanentlyInvalidatedException();
                }
                return new UserNotAuthenticatedException();
            case KeymasterDefs.KM_ERROR_KEY_EXPIRED /*-25*/:
                return new KeyExpiredException();
            case KeymasterDefs.KM_ERROR_KEY_NOT_YET_VALID /*-24*/:
                return new KeyNotYetValidException();
            case 2:
                return new UserNotAuthenticatedException();
            default:
                return new InvalidKeyException("Keystore operation failed", e);
        }
    }

    private long getFingerprintOnlySid() {
        FingerprintManager fingerprintManager = (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class);
        if (fingerprintManager == null) {
            return 0;
        }
        return fingerprintManager.getAuthenticatorId();
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int errorCode) {
        return getInvalidKeyException(keystoreKeyAlias, getKeyStoreException(errorCode));
    }

    public boolean migrateMDFPPKeystore(int uid, String password, boolean isFingerprint) {
        try {
            return this.mBinder.migrate_MDFPP_keystore(password, uid, isFingerprint ? 1 : 0) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(this.mError);
        }
    }

    public boolean isNeedMigration(int userId) {
        if (!CCManager.isMdfSupported()) {
            return false;
        }
        try {
            switch (this.mBinder.getState_MDFPP(userId)) {
                case 1:
                    return true;
                case 2:
                    return true;
                case 3:
                    return false;
                default:
                    throw new AssertionError(this.mError);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(e);
        }
    }

    public boolean resetMDFPP(int userId) {
        try {
            if (this.mBinder.reset_MDFPP(userId) == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public void onUserAddedMDFPP(int userId, int parentId) {
        try {
            this.mBinder.onUserAdded_MDFPP(userId, parentId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public byte[] getByUid(String key, int uid) {
        try {
            return this.mBinder.get_by_uid(key, uid);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    private int getUserId(int uid) {
        return getUserId(uid, false);
    }

    private int getUserId(int uid, boolean isAuditLog) {
        int userId = UserHandle.getUserId(uid);
        if (uid == -1) {
            return UserHandle.myUserId();
        }
        if (isAuditLog && uid == 1010) {
            return -1;
        }
        return userId;
    }

    private String getKeystoreString(int uid) {
        String keystore = "";
        if (uid == 1010) {
            return AuditEvents.WIFI;
        }
        if (uid == -1 || UserHandle.getAppId(uid) == 1000) {
            return "VPN and Apps";
        }
        return keystore;
    }

    private String getKeyString(String key) {
        String keyString = CERTIFICATE_STRING;
        if (key == null) {
            return keyString;
        }
        if (key.startsWith(Credentials.USER_PRIVATE_KEY)) {
            return "private key";
        }
        if (key.startsWith(Credentials.USER_SECRET_KEY)) {
            return "secret key";
        }
        return keyString;
    }

    private boolean isPrivateKeyPrefix(String key) {
        return !getKeyString(key).equals(CERTIFICATE_STRING);
    }

    private List<X509Certificate> convertFromPem(byte[] cert) {
        List<X509Certificate> certificates = null;
        if (cert != null) {
            try {
                certificates = Credentials.convertFromPem(cert);
            } catch (IOException ex) {
                Log.e(TAG, "Failed converting certificate from pem", ex);
            } catch (CertificateException ex2) {
                Log.e(TAG, "Failed converting certificate from pem", ex2);
            } catch (IllegalArgumentException ex3) {
                Log.e(TAG, "Failed converting certificate from pem", ex3);
            }
        }
        if (certificates == null) {
            return new ArrayList();
        }
        return certificates;
    }

    private void refreshRollbackUserKeystore(int userId) {
        if (this.lMdmService != null) {
            try {
                this.lMdmService.notifyUserKeystoreUnlocked(userId);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed talking with Certificate Policy", e);
            }
        }
    }

    public boolean isEmptyForSystemCredential() {
        Log.d(TAG, "isEmptyForSystemCredential");
        for (UserInfo pi : LockPatternUtils.getProfiles(UserHandle.getUserId(Process.myUid()))) {
            if (!pi.isManagedProfile() || pi.isKnoxWorkspace()) {
                int[] arr$ = SYSTEM_CREDENTIAL_UIDS;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    int uid = arr$[i$];
                    String[] aliases = list("", UserHandle.getUid(pi.id, uid));
                    if (aliases == null || aliases.length <= 0) {
                        i$++;
                    } else {
                        Log.d(TAG, "isEmptyForSystemCredential : [" + uid + "] = " + aliases.length);
                        return false;
                    }
                }
                continue;
            } else if (!isEmpty(pi.id)) {
                return false;
            }
        }
        return true;
    }
}
