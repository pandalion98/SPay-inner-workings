package android.sec.enterprise;

import android.content.Context;
import android.os.Handler;
import android.os.ServiceManager;
import android.sec.enterprise.IEDMProxy.Stub;
import android.sec.enterprise.auditlog.AuditLog;
import android.sec.enterprise.certificate.CertificatePolicy;
import android.sec.enterprise.general.MiscPolicy;
import android.sec.enterprise.kioskmode.KioskMode;
import com.sec.enterprise.knoxcustom.KnoxCustomManager;
import java.util.List;

public class EnterpriseDeviceManager {
    private static final String EDM_CLASS_NAME = "android.app.enterprise.EnterpriseDeviceManager";
    public static final String ENTERPRISE_POLICY_SERVICE = "enterprise_policy";
    public static final String ENTERPRISE_PROXY_SERVICE = "edm_proxy";
    private static final String KNOX_CLASS_NAME = "com.sec.enterprise.knox.EnterpriseKnoxManager";
    public static final String KNOX_ENTERPRISE_POLICY_SERVICE = "knox_enterprise_policy";
    private static EnterpriseDeviceManager mInstance;
    private static boolean mInstanceCreated;
    private volatile ApplicationPolicy mApplicationPolicy;
    private volatile AuditLog mAuditLog;
    private volatile BluetoothPolicy mBluetoothPolicy;
    private volatile BrowserPolicy mBrowserPolicy;
    private volatile CertificatePolicy mCertificatePolicy;
    private volatile ClientCertificateManager mClientCertificateManager;
    private volatile DateTimePolicy mDateTimePolicy;
    private volatile DeviceAccountPolicy mDeviceAccountPolicy;
    private volatile DeviceInventory mDeviceInventory;
    private volatile FirewallPolicy mFirewallPolicy;
    private volatile KioskMode mKioskMode;
    private volatile KnoxCustomManager mKnoxCustomManager = null;
    private volatile LocationPolicy mLocationPolicy;
    private volatile MiscPolicy mMiscPolicy;
    private volatile PasswordPolicy mPasswordPolicy;
    private volatile PhoneRestrictionPolicy mPhonePolicy;
    private volatile RestrictionPolicy mRestrictionPolicy;
    private volatile RoamingPolicy mRoamingPolicy;
    private volatile SmartCardAccessPolicy mSmartCardAccessPolicy;
    private volatile TimaKeystore mTimaKeystore;
    private volatile WifiPolicy mWifiPolicy;

    public static class EDMProxyServiceHelper {
        private static IEDMProxy mService;

        public static IEDMProxy getService() {
            if (mService == null) {
                mService = Stub.asInterface(ServiceManager.getService(EnterpriseDeviceManager.ENTERPRISE_PROXY_SERVICE));
            }
            return mService;
        }
    }

    public static IEDMProxy getService() {
        return EDMProxyServiceHelper.getService();
    }

    public static Object create(Context context, Handler handler) {
        try {
            return Class.forName(EDM_CLASS_NAME).getDeclaredConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object createKnox(Context context, Handler handler) {
        try {
            return Class.forName(KNOX_CLASS_NAME).getMethod("getInstance", new Class[0]).invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EnterpriseDeviceManager getInstance() {
        if (!mInstanceCreated) {
            synchronized (EnterpriseDeviceManager.class) {
                if (!mInstanceCreated) {
                    mInstance = new EnterpriseDeviceManager();
                    mInstanceCreated = true;
                }
            }
        }
        return mInstance;
    }

    public ApplicationPolicy getApplicationPolicy() {
        ApplicationPolicy result = this.mApplicationPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mApplicationPolicy;
                    if (result == null) {
                        ApplicationPolicy result2 = new ApplicationPolicy();
                        try {
                            this.mApplicationPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public MiscPolicy getMiscPolicy() {
        MiscPolicy result = this.mMiscPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mMiscPolicy;
                    if (result == null) {
                        MiscPolicy result2 = new MiscPolicy();
                        try {
                            this.mMiscPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public RoamingPolicy getRoamingPolicy() {
        RoamingPolicy result = this.mRoamingPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mRoamingPolicy;
                    if (result == null) {
                        RoamingPolicy result2 = new RoamingPolicy();
                        try {
                            this.mRoamingPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public RestrictionPolicy getRestrictionPolicy() {
        RestrictionPolicy result = this.mRestrictionPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mRestrictionPolicy;
                    if (result == null) {
                        RestrictionPolicy result2 = new RestrictionPolicy();
                        try {
                            this.mRestrictionPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public PasswordPolicy getPasswordPolicy() {
        PasswordPolicy result = this.mPasswordPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mPasswordPolicy;
                    if (result == null) {
                        PasswordPolicy result2 = new PasswordPolicy();
                        try {
                            this.mPasswordPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public DeviceInventory getDeviceInventory() {
        DeviceInventory result = this.mDeviceInventory;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mDeviceInventory;
                    if (result == null) {
                        DeviceInventory result2 = new DeviceInventory();
                        try {
                            this.mDeviceInventory = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public LocationPolicy getLocationPolicy() {
        LocationPolicy result = this.mLocationPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mLocationPolicy;
                    if (result == null) {
                        LocationPolicy result2 = new LocationPolicy();
                        try {
                            this.mLocationPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public BluetoothPolicy getBluetoothPolicy() {
        BluetoothPolicy result = this.mBluetoothPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mBluetoothPolicy;
                    if (result == null) {
                        BluetoothPolicy result2 = new BluetoothPolicy();
                        try {
                            this.mBluetoothPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public BrowserPolicy getBrowserPolicy() {
        BrowserPolicy result = this.mBrowserPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mBrowserPolicy;
                    if (result == null) {
                        BrowserPolicy result2 = new BrowserPolicy();
                        try {
                            this.mBrowserPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public PhoneRestrictionPolicy getPhoneRestrictionPolicy() {
        PhoneRestrictionPolicy result = this.mPhonePolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mPhonePolicy;
                    if (result == null) {
                        PhoneRestrictionPolicy result2 = new PhoneRestrictionPolicy();
                        try {
                            this.mPhonePolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public WifiPolicy getWifiPolicy() {
        WifiPolicy result = this.mWifiPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mWifiPolicy;
                    if (result == null) {
                        WifiPolicy result2 = new WifiPolicy();
                        try {
                            this.mWifiPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public DateTimePolicy getDateTimePolicy() {
        DateTimePolicy result = this.mDateTimePolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mDateTimePolicy;
                    if (result == null) {
                        DateTimePolicy result2 = new DateTimePolicy();
                        try {
                            this.mDateTimePolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public FirewallPolicy getFirewallPolicy() {
        FirewallPolicy result = this.mFirewallPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mFirewallPolicy;
                    if (result == null) {
                        FirewallPolicy result2 = new FirewallPolicy();
                        try {
                            this.mFirewallPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public KioskMode getKioskMode() {
        KioskMode result = this.mKioskMode;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mKioskMode;
                    if (result == null) {
                        KioskMode result2 = new KioskMode();
                        try {
                            this.mKioskMode = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public CertificatePolicy getCertificatePolicy() {
        CertificatePolicy result = this.mCertificatePolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mCertificatePolicy;
                    if (result == null) {
                        CertificatePolicy result2 = new CertificatePolicy();
                        try {
                            this.mCertificatePolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public AuditLog getAuditPolicy() {
        AuditLog result = this.mAuditLog;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mAuditLog;
                    if (result == null) {
                        AuditLog result2 = new AuditLog();
                        try {
                            this.mAuditLog = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public DeviceAccountPolicy getDeviceAccountPolicy() {
        DeviceAccountPolicy result = this.mDeviceAccountPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mDeviceAccountPolicy;
                    if (result == null) {
                        DeviceAccountPolicy result2 = new DeviceAccountPolicy();
                        try {
                            this.mDeviceAccountPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public KnoxCustomManager getKnoxCustomManager() {
        KnoxCustomManager result = this.mKnoxCustomManager;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mKnoxCustomManager;
                    if (result == null) {
                        KnoxCustomManager result2 = new KnoxCustomManager();
                        try {
                            this.mKnoxCustomManager = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public SmartCardAccessPolicy getSmartCardAccessPolicy() {
        SmartCardAccessPolicy result = this.mSmartCardAccessPolicy;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mSmartCardAccessPolicy;
                    if (result == null) {
                        SmartCardAccessPolicy result2 = new SmartCardAccessPolicy();
                        try {
                            this.mSmartCardAccessPolicy = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public List<String> getELMPermissions(String packageName) {
        try {
            return getService().getELMPermissions(packageName);
        } catch (Exception e) {
            return null;
        }
    }

    public ClientCertificateManager getClientCertificateManager() {
        ClientCertificateManager result = this.mClientCertificateManager;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mClientCertificateManager;
                    if (result == null) {
                        ClientCertificateManager result2 = new ClientCertificateManager();
                        try {
                            this.mClientCertificateManager = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    public TimaKeystore getTimaKeystore() {
        TimaKeystore result = this.mTimaKeystore;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.mTimaKeystore;
                    if (result == null) {
                        TimaKeystore result2 = new TimaKeystore();
                        try {
                            this.mTimaKeystore = result2;
                            result = result2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }
}
