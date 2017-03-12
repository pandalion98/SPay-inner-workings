package android.net.wifi.passpoint;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WifiPasspointPolicy implements Parcelable {
    public static final Creator<WifiPasspointPolicy> CREATOR = new Creator<WifiPasspointPolicy>() {
        public WifiPasspointPolicy createFromParcel(Parcel in) {
            return null;
        }

        public WifiPasspointPolicy[] newArray(int size) {
            return new WifiPasspointPolicy[size];
        }
    };
    public static final int HOME_SP = 0;
    public static final int ROAMING_PARTNER = 1;
    private static final String TAG = "PasspointPolicy";
    public static final int UNRESTRICTED = 2;
    private final String ENTERPRISE_PHASE2_MSCHAP = "auth=MSCHAP";
    private final String ENTERPRISE_PHASE2_MSCHAPV2 = "auth=MSCHAPV2";
    private final String INT_ANONYMOUS_IDENTITY = WifiEnterpriseConfig.ANON_IDENTITY_KEY;
    private final String INT_CA_CERT = WifiEnterpriseConfig.CA_CERT_KEY;
    private final String INT_CLIENT_CERT = WifiEnterpriseConfig.CLIENT_CERT_KEY;
    private final String INT_EAP = WifiEnterpriseConfig.EAP_KEY;
    private final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";
    private final String INT_IDENTITY = WifiEnterpriseConfig.IDENTITY_KEY;
    private final String INT_PASSWORD = "password";
    private final String INT_PHASE2 = WifiEnterpriseConfig.PHASE2_KEY;
    private final String INT_PRIVATE_KEY = "private_key";
    private final String INT_SIM_SLOT = "sim_slot";
    private final String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private String mBssid;
    private WifiPasspointCredential mCredential;
    private int mCredentialPriority;
    private boolean mIsHomeSp;
    private String mName;
    private int mRestriction;
    private int mRoamingPriority;
    private String mSsid;

    public WifiPasspointPolicy(String name, String ssid, String bssid, WifiPasspointCredential pc, int restriction, boolean ishomesp) {
        this.mName = name;
        if (pc != null) {
            this.mCredentialPriority = pc.getPriority();
        }
        this.mRoamingPriority = 128;
        this.mSsid = ssid;
        this.mCredential = pc;
        this.mBssid = bssid;
        this.mRestriction = restriction;
        this.mIsHomeSp = ishomesp;
    }

    public String getSsid() {
        return this.mSsid;
    }

    public void setBssid(String bssid) {
        this.mBssid = bssid;
    }

    public String getBssid() {
        return this.mBssid;
    }

    public void setRestriction(int r) {
        this.mRestriction = r;
    }

    public int getRestriction() {
        return this.mRestriction;
    }

    public void setHomeSp(boolean b) {
        this.mIsHomeSp = b;
    }

    public boolean isHomeSp() {
        return this.mIsHomeSp;
    }

    public void setCredential(WifiPasspointCredential newCredential) {
        this.mCredential = newCredential;
    }

    public WifiPasspointCredential getCredential() {
        return this.mCredential;
    }

    public void setCredentialPriority(int priority) {
        this.mCredentialPriority = priority;
    }

    public void setRoamingPriority(int priority) {
        this.mRoamingPriority = priority;
    }

    public int getCredentialPriority() {
        return this.mCredentialPriority;
    }

    public int getRoamingPriority() {
        return this.mRoamingPriority;
    }

    public WifiConfiguration createWifiConfiguration() {
        WifiConfiguration wfg = new WifiConfiguration();
        if (this.mBssid != null) {
            Log.d(TAG, "create bssid:" + this.mBssid);
            wfg.BSSID = this.mBssid;
        }
        if (this.mSsid != null) {
            Log.d(TAG, "create ssid:" + this.mSsid);
            wfg.SSID = this.mSsid;
        }
        wfg.status = 2;
        wfg.allowedKeyManagement.clear();
        wfg.allowedKeyManagement.set(2);
        wfg.allowedKeyManagement.set(3);
        wfg.allowedGroupCiphers.clear();
        wfg.allowedPairwiseCiphers.set(2);
        wfg.allowedPairwiseCiphers.set(1);
        wfg.allowedProtocols.clear();
        wfg.allowedProtocols.set(1);
        wfg.allowedProtocols.set(0);
        Class<?> enterpriseFieldClass = null;
        for (Class<?> myClass : WifiConfiguration.class.getClasses()) {
            if (myClass.getName().equals("android.net.wifi.WifiConfiguration$EnterpriseField")) {
                enterpriseFieldClass = myClass;
                break;
            }
        }
        Log.d(TAG, "class chosen " + enterpriseFieldClass.getName());
        Field anonymousId = null;
        Field caCert = null;
        Field clientCert = null;
        Field eap = null;
        Field identity = null;
        Field password = null;
        Field phase2 = null;
        Field privateKey = null;
        for (Field tempField : WifiConfiguration.class.getFields()) {
            if (tempField.getName().trim().equals(WifiEnterpriseConfig.ANON_IDENTITY_KEY)) {
                anonymousId = tempField;
                Log.d(TAG, "field " + anonymousId.getName());
            } else if (tempField.getName().trim().equals(WifiEnterpriseConfig.CA_CERT_KEY)) {
                caCert = tempField;
            } else if (tempField.getName().trim().equals(WifiEnterpriseConfig.CLIENT_CERT_KEY)) {
                clientCert = tempField;
                Log.d(TAG, "field " + clientCert.getName());
            } else if (tempField.getName().trim().equals(WifiEnterpriseConfig.EAP_KEY)) {
                eap = tempField;
                Log.d(TAG, "field " + eap.getName());
            } else if (tempField.getName().trim().equals(WifiEnterpriseConfig.IDENTITY_KEY)) {
                identity = tempField;
                Log.d(TAG, "field " + identity.getName());
            } else if (tempField.getName().trim().equals("password")) {
                password = tempField;
                Log.d(TAG, "field " + password.getName());
            } else if (tempField.getName().trim().equals(WifiEnterpriseConfig.PHASE2_KEY)) {
                phase2 = tempField;
                Log.d(TAG, "field " + phase2.getName());
            } else if (tempField.getName().trim().equals("private_key")) {
                privateKey = tempField;
            }
        }
        Method setValue = null;
        for (Method m : enterpriseFieldClass.getMethods()) {
            if (m.getName().trim().equals("setValue")) {
                Log.d(TAG, "method " + m.getName());
                setValue = m;
                break;
            }
        }
        try {
            String cacertificate;
            String eapmethod = this.mCredential.getType();
            Log.d(TAG, "eapmethod:" + eapmethod);
            setValue.invoke(eap.get(wfg), new Object[]{eapmethod});
            if ("TTLS".equals(eapmethod)) {
                setValue.invoke(phase2.get(wfg), new Object[]{"auth=MSCHAPV2"});
                setValue.invoke(identity.get(wfg), new Object[]{this.mCredential.getUserName()});
                setValue.invoke(password.get(wfg), new Object[]{this.mCredential.getPassword()});
                setValue.invoke(anonymousId.get(wfg), new Object[]{"anonymous@" + this.mCredential.getRealm()});
            }
            String rootCA = this.mCredential.getCaRootCertPath();
            if (rootCA == null) {
                cacertificate = null;
            } else {
                cacertificate = "keystore://WIFI_HS20CACERT_" + rootCA;
            }
            Log.d(TAG, "cacertificate:" + cacertificate);
            setValue.invoke(caCert.get(wfg), new Object[]{cacertificate});
            if ("TLS".equals(eapmethod)) {
                String usercertificate = null;
                String privatekey = null;
                String clientCertPath = this.mCredential.getClientCertPath();
                if (clientCertPath != null) {
                    privatekey = "keystore://WIFI_HS20USRPKEY_" + clientCertPath;
                    usercertificate = "keystore://WIFI_HS20USRCERT_" + clientCertPath;
                }
                Log.d(TAG, "privatekey:" + privatekey);
                Log.d(TAG, "usercertificate:" + usercertificate);
                if (!(privatekey == null || usercertificate == null)) {
                    setValue.invoke(privateKey.get(wfg), new Object[]{privatekey});
                    setValue.invoke(clientCert.get(wfg), new Object[]{usercertificate});
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "createWifiConfiguration err:" + e);
        }
        return wfg;
    }

    public int compareTo(WifiPasspointPolicy another) {
        Log.d(TAG, "this:" + this);
        Log.d(TAG, "another:" + another);
        if (another == null) {
            return -1;
        }
        if (!this.mIsHomeSp || another.isHomeSp()) {
            if (this.mIsHomeSp && another.isHomeSp()) {
                Log.d(TAG, "both HomeSP");
                if (this.mCredentialPriority < another.getCredentialPriority()) {
                    Log.d(TAG, "this priority is higher");
                    return -1;
                } else if (this.mCredentialPriority != another.getCredentialPriority()) {
                    return 1;
                } else {
                    Log.d(TAG, "both priorities equal");
                    if (this.mName.compareTo(another.mName) != 0) {
                        Log.d(TAG, "compare mName return:" + this.mName.compareTo(another.mName));
                        return this.mName.compareTo(another.mName);
                    } else if (!(this.mCredential == null || another.mCredential == null || this.mCredential.compareTo(another.mCredential) == 0)) {
                        Log.d(TAG, "compare mCredential return:" + this.mName.compareTo(another.mName));
                        return this.mCredential.compareTo(another.mCredential);
                    }
                }
            } else if (!(this.mIsHomeSp || another.isHomeSp())) {
                Log.d(TAG, "both RoamingSp");
                if (this.mRoamingPriority < another.getRoamingPriority()) {
                    Log.d(TAG, "this priority is higher");
                    return -1;
                } else if (this.mRoamingPriority != another.getRoamingPriority()) {
                    return 1;
                } else {
                    Log.d(TAG, "both priorities equal");
                    if (this.mName.compareTo(another.mName) != 0) {
                        Log.d(TAG, "compare mName return:" + this.mName.compareTo(another.mName));
                        return this.mName.compareTo(another.mName);
                    } else if (!(this.mCredential == null || another.mCredential == null || this.mCredential.compareTo(another.mCredential) == 0)) {
                        Log.d(TAG, "compare mCredential return:" + this.mCredential.compareTo(another.mCredential));
                        return this.mCredential.compareTo(another.mCredential);
                    }
                }
            }
            Log.d(TAG, "both policies equal");
            return 0;
        }
        Log.d(TAG, "compare HomeSP  first, this is HomeSP, another isn't");
        return -1;
    }

    public String toString() {
        return "PasspointPolicy: name=" + this.mName + " CredentialPriority=" + this.mCredentialPriority + " mRoamingPriority" + this.mRoamingPriority + " ssid=" + this.mSsid + " restriction=" + this.mRestriction + " ishomesp=" + this.mIsHomeSp + " Credential=" + this.mCredential;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
