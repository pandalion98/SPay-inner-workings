package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Slog;
import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map.Entry;

public class WifiEnterpriseConfig implements Parcelable {
    public static final String ALTSUBJECT_MATCH_KEY = "altsubject_match";
    public static final String ANON_IDENTITY_KEY = "anonymous_identity";
    public static final String CA_CERT_KEY = "ca_cert";
    public static final String CA_CERT_PREFIX = "keystore://CACERT_";
    public static final String CLIENT_CERT_KEY = "client_cert";
    public static final String CLIENT_CERT_PREFIX = "keystore://USRCERT_";
    public static final Creator<WifiEnterpriseConfig> CREATOR = new Creator<WifiEnterpriseConfig>() {
        public WifiEnterpriseConfig createFromParcel(Parcel in) {
            WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                enterpriseConfig.mFields.put(in.readString(), in.readString());
            }
            enterpriseConfig.mCaCert = readCertificate(in);
            PrivateKey userKey = null;
            int len = in.readInt();
            if (len > 0) {
                try {
                    byte[] bytes = new byte[len];
                    in.readByteArray(bytes);
                    userKey = KeyFactory.getInstance(in.readString()).generatePrivate(new PKCS8EncodedKeySpec(bytes));
                } catch (NoSuchAlgorithmException e) {
                    userKey = null;
                } catch (InvalidKeySpecException e2) {
                    userKey = null;
                }
            }
            enterpriseConfig.mClientPrivateKey = userKey;
            enterpriseConfig.mClientCertificate = readCertificate(in);
            enterpriseConfig.mTls12Enable = in.readInt() == 1;
            enterpriseConfig.mwapiASCert = readCertificate(in);
            enterpriseConfig.mwapiUserCert = readCertificate(in);
            if (in.readInt() == 1) {
                enterpriseConfig.setCCMEnabled(true);
            }
            if (in.readInt() == 1) {
                enterpriseConfig.setUCMEnabled(true);
            }
            return enterpriseConfig;
        }

        private X509Certificate readCertificate(Parcel in) {
            int len = in.readInt();
            if (len <= 0) {
                return null;
            }
            try {
                byte[] bytes = new byte[len];
                in.readByteArray(bytes);
                return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes));
            } catch (CertificateException e) {
                return null;
            }
        }

        public WifiEnterpriseConfig[] newArray(int size) {
            return new WifiEnterpriseConfig[size];
        }
    };
    private static final boolean DBG = false;
    public static final String DISABLE_TLS_1_2 = "\"tls_disable_tlsv1_2=1\"";
    public static final String DOM_SUFFIX_MATCH_KEY = "domain_suffix_match";
    public static final String EAP_KEY = "eap";
    private static final String EMPTY_STRING = "";
    public static final String EMPTY_VALUE = "NULL";
    public static final String ENABLE_TLS_1_2 = "\"tls_disable_tlsv1_2=0\"";
    public static final String ENGINE_DISABLE = "0";
    public static final String ENGINE_ENABLE = "1";
    public static final String ENGINE_ID_KEY = "engine_id";
    public static final String ENGINE_ID_KEYSTORE = "keystore";
    public static final String ENGINE_ID_SECPKCS11 = "secpkcs11";
    public static final String ENGINE_ID_UCMENGINE = "ucsengine";
    public static final String ENGINE_KEY = "engine";
    public static final String IDENTITY_KEY = "identity";
    public static final String KEYSTORE_URI = "keystore://";
    public static final String OPP_KEY_CACHING = "proactive_key_caching";
    public static final String PAC_FILE = "pac_file";
    public static final String PASSWORD_KEY = "password";
    public static final String PHASE1_KEY = "phase1";
    public static final String PHASE2_KEY = "phase2";
    public static final String PLMN_KEY = "plmn";
    public static final String PRIVATE_KEY_ID_KEY = "key_id";
    public static final String REALM_KEY = "realm";
    public static final String SUBJECT_MATCH_KEY = "subject_match";
    private static final String TAG = "WifiEnterpriseConfig";
    private static final String WAPIAS_CERT_PREFIX = "keystore://WAPIAS_";
    private static final String WAPIUSER_CERT_PREFIX = "keystore://WAPIUSR_";
    public static final String WAPI_AS_KEY = "wapi_as_cert";
    public static final String WAPI_USER_KEY = "wapi_user_cert";
    private X509Certificate mCaCert;
    private X509Certificate mClientCertificate;
    private PrivateKey mClientPrivateKey;
    private HashMap<String, String> mFields = new HashMap();
    private boolean mIsCCMEnabled = false;
    private boolean mIsUCMEnabled = false;
    private boolean mTls12Enable = true;
    private X509Certificate mwapiASCert;
    private X509Certificate mwapiUserCert;

    public static final class Eap {
        public static final int AKA = 5;
        public static final int AKA_PRIME = 6;
        public static final int FAST = 7;
        public static final int LEAP = 8;
        public static final int NONE = -1;
        public static final int PEAP = 0;
        public static final int PWD = 3;
        public static final int SIM = 4;
        public static final int TLS = 1;
        public static final int TTLS = 2;
        public static final int WFA_UNAUTH_TLS = 9;
        public static final String[] strings = new String[]{"PEAP", "TLS", "TTLS", "PWD", "SIM", "AKA", "AKA'", "FAST", "LEAP", "WFA-UNAUTH-TLS"};

        private Eap() {
        }
    }

    public static final class Phase1 {
        public static final int ALLOW_AUTHENTICATED = 2;
        public static final int ALLOW_BOTH = 3;
        public static final int ALLOW_UNAUTHENTICATED = 1;
        public static final int DISABLE = 0;
        public static final int NONE = -1;
        private static final String PREFIX = "fast_provisioning=";
        public static final String[] strings = new String[]{"0", WifiEnterpriseConfig.ENGINE_ENABLE, "2", "3"};

        private Phase1() {
        }
    }

    public static final class Phase2 {
        public static final int GTC = 4;
        public static final int MSCHAP = 2;
        public static final int MSCHAPV2 = 3;
        public static final int NONE = 0;
        public static final int PAP = 1;
        private static final String PREFIX = "auth=";
        public static final String[] strings = new String[]{WifiEnterpriseConfig.EMPTY_VALUE, "PAP", "MSCHAP", "MSCHAPV2", "GTC"};

        private Phase2() {
        }
    }

    public WifiEnterpriseConfig(WifiEnterpriseConfig source) {
        for (String key : source.mFields.keySet()) {
            if (key.equals(ENGINE_ID_KEY)) {
                if (ENGINE_ID_SECPKCS11.equals(removeDoubleQuotes((String) source.mFields.get(key)))) {
                    this.mIsCCMEnabled = true;
                } else if (ENGINE_ID_UCMENGINE.equals(removeDoubleQuotes((String) source.mFields.get(key)))) {
                    this.mIsUCMEnabled = true;
                }
            }
            this.mFields.put(key, source.mFields.get(key));
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.mFields.size());
        for (Entry<String, String> entry : this.mFields.entrySet()) {
            dest.writeString((String) entry.getKey());
            dest.writeString((String) entry.getValue());
        }
        writeCertificate(dest, this.mCaCert);
        if (this.mClientPrivateKey != null) {
            String algorithm = this.mClientPrivateKey.getAlgorithm();
            byte[] userKeyBytes = this.mClientPrivateKey.getEncoded();
            dest.writeInt(userKeyBytes.length);
            dest.writeByteArray(userKeyBytes);
            dest.writeString(algorithm);
        } else {
            dest.writeInt(0);
        }
        writeCertificate(dest, this.mClientCertificate);
        if (this.mTls12Enable) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        writeCertificate(dest, this.mwapiASCert);
        writeCertificate(dest, this.mwapiUserCert);
        if (this.mIsCCMEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (!this.mIsUCMEnabled) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    private void writeCertificate(Parcel dest, X509Certificate cert) {
        if (cert != null) {
            try {
                byte[] certBytes = cert.getEncoded();
                dest.writeInt(certBytes.length);
                dest.writeByteArray(certBytes);
                return;
            } catch (CertificateEncodingException e) {
                dest.writeInt(0);
                return;
            }
        }
        dest.writeInt(0);
    }

    public HashMap<String, String> getFields() {
        return this.mFields;
    }

    static String[] getSupplicantKeys() {
        return new String[]{EAP_KEY, PHASE1_KEY, PHASE2_KEY, PAC_FILE, IDENTITY_KEY, ANON_IDENTITY_KEY, "password", CLIENT_CERT_KEY, CA_CERT_KEY, SUBJECT_MATCH_KEY, ENGINE_KEY, ENGINE_ID_KEY, PRIVATE_KEY_ID_KEY, "wapi_as_cert", "wapi_user_cert"};
    }

    public void setEapMethod(int eapMethod) {
        switch (eapMethod) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                break;
            case 1:
                setPhase2Method(0);
                break;
            default:
                throw new IllegalArgumentException("Unknown EAP method");
        }
        this.mFields.put(EAP_KEY, Eap.strings[eapMethod]);
        this.mFields.put(OPP_KEY_CACHING, ENGINE_ENABLE);
    }

    public void setTls12Enable(boolean enable) {
        this.mTls12Enable = enable;
        this.mFields.put(PHASE1_KEY, enable ? ENABLE_TLS_1_2 : DISABLE_TLS_1_2);
    }

    public boolean getTls12Enable() {
        return this.mTls12Enable;
    }

    public int getEapMethod() {
        return getStringIndex(Eap.strings, (String) this.mFields.get(EAP_KEY), -1);
    }

    public void setPhase1Method(int phase1Method) {
        switch (phase1Method) {
            case -1:
                this.mFields.put(PHASE1_KEY, EMPTY_VALUE);
                return;
            case 0:
            case 1:
            case 2:
            case 3:
                this.mFields.put(PHASE1_KEY, convertToQuotedString("fast_provisioning=" + Phase1.strings[phase1Method]));
                return;
            default:
                throw new IllegalArgumentException("Unknown Phase 1 method");
        }
    }

    public String getPhase1Method() {
        return getFieldValue(PHASE1_KEY, "");
    }

    public void setPhase2Method(int phase2Method) {
        switch (phase2Method) {
            case 0:
                this.mFields.put(PHASE2_KEY, EMPTY_VALUE);
                return;
            case 1:
            case 2:
            case 3:
            case 4:
                this.mFields.put(PHASE2_KEY, convertToQuotedString("auth=" + Phase2.strings[phase2Method]));
                return;
            default:
                throw new IllegalArgumentException("Unknown Phase 2 method");
        }
    }

    public int getPhase2Method() {
        String phase2Method = removeDoubleQuotes((String) this.mFields.get(PHASE2_KEY));
        if (phase2Method.startsWith("auth=")) {
            phase2Method = phase2Method.substring("auth=".length());
        }
        return getStringIndex(Phase2.strings, phase2Method, 0);
    }

    public void setPacFile(String pac_file) {
        setFieldValue(PAC_FILE, pac_file, "");
    }

    public String getPacFile() {
        return getFieldValue(PAC_FILE, "");
    }

    public void setIdentity(String identity) {
        setFieldValue(IDENTITY_KEY, identity, "");
    }

    public String getIdentity() {
        return getFieldValue(IDENTITY_KEY, "");
    }

    public void setAnonymousIdentity(String anonymousIdentity) {
        setFieldValue(ANON_IDENTITY_KEY, anonymousIdentity, "");
    }

    public String getAnonymousIdentity() {
        return getFieldValue(ANON_IDENTITY_KEY, "");
    }

    public void setPassword(String password) {
        setFieldValue("password", password, "");
    }

    public String getPassword() {
        return getFieldValue("password", "");
    }

    public void setCaCertificateAlias(String alias) {
        setFieldValue(CA_CERT_KEY, alias, CA_CERT_PREFIX);
    }

    public String getCaCertificateAlias() {
        return getFieldValue(CA_CERT_KEY, CA_CERT_PREFIX);
    }

    public void setWapiASCertificateAlias(String alias) {
        setFieldValue("wapi_as_cert", alias, WAPIAS_CERT_PREFIX);
    }

    public String getWapiASCertificateAlias() {
        return getFieldValue("wapi_as_cert", WAPIAS_CERT_PREFIX);
    }

    public void setWapiUserCertificateAlias(String alias) {
        setFieldValue("wapi_user_cert", alias, WAPIUSER_CERT_PREFIX);
    }

    public String getWapiUserCertificateAlias() {
        return getFieldValue("wapi_user_cert", WAPIUSER_CERT_PREFIX);
    }

    public void setCaCertificate(X509Certificate cert) {
        if (cert == null) {
            this.mCaCert = null;
        } else if (cert.getBasicConstraints() >= 0) {
            this.mCaCert = cert;
        } else {
            throw new IllegalArgumentException("Not a CA certificate");
        }
    }

    public X509Certificate getCaCertificate() {
        return this.mCaCert;
    }

    public void resetCaCertificate() {
        this.mCaCert = null;
    }

    public X509Certificate getWapiAsCertificate() {
        return this.mwapiASCert;
    }

    public void resetWapiAsCertificate() {
        this.mwapiASCert = null;
    }

    public X509Certificate getWapiUserCertificate() {
        return this.mwapiUserCert;
    }

    public void resetWapiUserCertificate() {
        this.mwapiUserCert = null;
    }

    public void setClientCertificateAlias(String alias) {
        String engine_id;
        if (this.mIsCCMEnabled) {
            setFieldValue(CLIENT_CERT_KEY, alias, "");
            setFieldValue(PRIVATE_KEY_ID_KEY, alias, "");
            engine_id = ENGINE_ID_SECPKCS11;
        } else if (this.mIsUCMEnabled) {
            setFieldValue(CLIENT_CERT_KEY, alias, "");
            setFieldValue(PRIVATE_KEY_ID_KEY, alias, "");
            engine_id = ENGINE_ID_UCMENGINE;
        } else {
            setFieldValue(CLIENT_CERT_KEY, alias, CLIENT_CERT_PREFIX);
            setFieldValue(PRIVATE_KEY_ID_KEY, alias, "USRPKEY_");
            engine_id = ENGINE_ID_KEYSTORE;
        }
        if (TextUtils.isEmpty(alias)) {
            this.mFields.put(ENGINE_KEY, "0");
            this.mFields.put(ENGINE_ID_KEY, EMPTY_VALUE);
            return;
        }
        this.mFields.put(ENGINE_KEY, ENGINE_ENABLE);
        this.mFields.put(ENGINE_ID_KEY, convertToQuotedString(engine_id));
    }

    public String getClientCertificateAlias() {
        return getFieldValue(CLIENT_CERT_KEY, CLIENT_CERT_PREFIX);
    }

    public void setCCMEnabled(boolean enabled) {
        Slog.v(TAG, "setCCMEnabled " + enabled);
        this.mFields.put(ENGINE_KEY, ENGINE_ENABLE);
        this.mIsCCMEnabled = enabled;
        if (enabled) {
            this.mFields.put(ENGINE_ID_KEY, convertToQuotedString(ENGINE_ID_SECPKCS11));
        } else {
            this.mFields.put(ENGINE_ID_KEY, convertToQuotedString(ENGINE_ID_KEYSTORE));
        }
    }

    public void setUCMEnabled(boolean enabled) {
        Slog.v(TAG, "setUCMEnabled " + enabled);
        this.mFields.put(ENGINE_KEY, ENGINE_ENABLE);
        this.mIsUCMEnabled = enabled;
        if (enabled) {
            this.mFields.put(ENGINE_ID_KEY, convertToQuotedString(ENGINE_ID_UCMENGINE));
        } else {
            this.mFields.put(ENGINE_ID_KEY, convertToQuotedString(ENGINE_ID_KEYSTORE));
        }
    }

    public String getEngineId() {
        return getFieldValue(ENGINE_ID_KEY, "");
    }

    public void setClientKeyEntry(PrivateKey privateKey, X509Certificate clientCertificate) {
        if (clientCertificate != null) {
            if (clientCertificate.getBasicConstraints() != -1) {
                throw new IllegalArgumentException("Cannot be a CA certificate");
            } else if (privateKey == null) {
                throw new IllegalArgumentException("Client cert without a private key");
            } else if (privateKey.getEncoded() == null) {
                throw new IllegalArgumentException("Private key cannot be encoded");
            }
        }
        this.mClientPrivateKey = privateKey;
        this.mClientCertificate = clientCertificate;
    }

    public X509Certificate getClientCertificate() {
        return this.mClientCertificate;
    }

    public void resetClientKeyEntry() {
        this.mClientPrivateKey = null;
        this.mClientCertificate = null;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public void setSubjectMatch(String subjectMatch) {
        setFieldValue(SUBJECT_MATCH_KEY, subjectMatch, "");
    }

    public String getSubjectMatch() {
        return getFieldValue(SUBJECT_MATCH_KEY, "");
    }

    public void setAltSubjectMatch(String altSubjectMatch) {
        setFieldValue(ALTSUBJECT_MATCH_KEY, altSubjectMatch, "");
    }

    public String getAltSubjectMatch() {
        return getFieldValue(ALTSUBJECT_MATCH_KEY, "");
    }

    public void setDomainSuffixMatch(String domain) {
        setFieldValue(DOM_SUFFIX_MATCH_KEY, domain);
    }

    public String getDomainSuffixMatch() {
        return getFieldValue(DOM_SUFFIX_MATCH_KEY, "");
    }

    public void setRealm(String realm) {
        setFieldValue("realm", realm, "");
    }

    public String getRealm() {
        return getFieldValue("realm", "");
    }

    public void setPlmn(String plmn) {
        setFieldValue(PLMN_KEY, plmn, "");
    }

    public String getPlmn() {
        return getFieldValue(PLMN_KEY, "");
    }

    String getKeyId(WifiEnterpriseConfig current) {
        String eap = (String) this.mFields.get(EAP_KEY);
        String phase2 = (String) this.mFields.get(PHASE2_KEY);
        if (TextUtils.isEmpty(eap)) {
            eap = (String) current.mFields.get(EAP_KEY);
        }
        if (TextUtils.isEmpty(phase2)) {
            phase2 = (String) current.mFields.get(PHASE2_KEY);
        }
        return eap + "_" + phase2;
    }

    private String removeDoubleQuotes(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    private String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }

    private int getStringIndex(String[] arr, String toBeFound, int defaultIndex) {
        if (TextUtils.isEmpty(toBeFound)) {
            return defaultIndex;
        }
        for (int i = 0; i < arr.length; i++) {
            if (toBeFound.equals(arr[i])) {
                return i;
            }
        }
        return defaultIndex;
    }

    public String getFieldValue(String key, String prefix) {
        String value = (String) this.mFields.get(key);
        if (TextUtils.isEmpty(value) || EMPTY_VALUE.equals(value)) {
            return "";
        }
        value = removeDoubleQuotes(value);
        return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
    }

    public void setFieldValue(String key, String value, String prefix) {
        if (TextUtils.isEmpty(value)) {
            this.mFields.put(key, EMPTY_VALUE);
        } else {
            this.mFields.put(key, convertToQuotedString(prefix + value));
        }
    }

    public void setFieldValue(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            this.mFields.put(key, EMPTY_VALUE);
        } else {
            this.mFields.put(key, convertToQuotedString(value));
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (String key : this.mFields.keySet()) {
            sb.append(key).append(" ").append("password".equals(key) ? "<removed>" : (String) this.mFields.get(key)).append("\n");
        }
        return sb.toString();
    }
}
