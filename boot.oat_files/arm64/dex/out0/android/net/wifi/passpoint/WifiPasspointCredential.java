package android.net.wifi.passpoint;

import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.passpoint.WifiPasspointDmTree.AAAServerTrustRoot;
import android.net.wifi.passpoint.WifiPasspointDmTree.CredentialInfo;
import android.net.wifi.passpoint.WifiPasspointDmTree.HomeOIList;
import android.net.wifi.passpoint.WifiPasspointDmTree.MinBackhaulThresholdNetwork;
import android.net.wifi.passpoint.WifiPasspointDmTree.OtherHomePartners;
import android.net.wifi.passpoint.WifiPasspointDmTree.PreferredRoamingPartnerList;
import android.net.wifi.passpoint.WifiPasspointDmTree.RequiredProtoPortTuple;
import android.net.wifi.passpoint.WifiPasspointDmTree.SPExclusionList;
import android.net.wifi.passpoint.WifiPasspointDmTree.SpFqdn;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

public class WifiPasspointCredential implements Parcelable {
    public static final Creator<WifiPasspointCredential> CREATOR = new Creator<WifiPasspointCredential>() {
        public WifiPasspointCredential createFromParcel(Parcel in) {
            WifiPasspointCredential pc = new WifiPasspointCredential();
            pc.mWifiSpFqdn = in.readString();
            pc.mCredentialName = in.readString();
            pc.mType = in.readString();
            pc.mCrednetialPriority = in.readInt();
            pc.mHomeSpFqdn = in.readString();
            pc.mRealm = in.readString();
            return pc;
        }

        public WifiPasspointCredential[] newArray(int size) {
            return new WifiPasspointCredential[size];
        }
    };
    private static final boolean DBG = true;
    private static final String TAG = "PasspointCredential";
    private String mAaaCertUrl;
    private String mAaaSha256Fingerprint;
    private String mCaRootCert;
    private String mCertSha256Fingerprint;
    private String mCertType;
    private boolean mCheckAaaServerCertStatus;
    private String mClientCert;
    private String mCreationDate;
    private String mCredentialName;
    private int mCrednetialPriority;
    private WifiEnterpriseConfig mEnterpriseConfig;
    private String mExpirationDate;
    private String mFriendlyName;
    private Collection<HomeOIList> mHomeOIList;
    private String mHomeSpFqdn;
    private String mImsi;
    private String mInnerMethod;
    private boolean mIsMachineRemediation;
    private String mMaxBssLoad;
    private String mMcc;
    private Collection<MinBackhaulThresholdNetwork> mMinBackhaulThresholdNetwork;
    private String mMnc;
    private Collection<OtherHomePartners> mOtherHomePartnerList;
    private String mPasswd;
    private String mPolicyUpdateInterval;
    private String mPolicyUpdateMethod;
    private String mPolicyUpdatePassword;
    private String mPolicyUpdateRestriction;
    private String mPolicyUpdateUri;
    private String mPolicyUpdateUsername;
    private Collection<PreferredRoamingPartnerList> mPreferredRoamingPartnerList;
    private String mRealm;
    private Collection<RequiredProtoPortTuple> mRequiredProtoPortTuple;
    private Collection<SPExclusionList> mSpExclusionList;
    private String mSubscriptionUpdateInterval;
    private String mSubscriptionUpdateMethod;
    private String mSubscriptionUpdatePassword;
    private String mSubscriptionUpdateRestriction;
    private String mSubscriptionUpdateURI;
    private String mSubscriptionUpdateUsername;
    private String mType;
    private String mUpdateIdentifier;
    private boolean mUserPreferred = false;
    private String mUsername;
    private String mWifiSpFqdn;
    private String mWifiTreePath;

    public WifiPasspointCredential(String realm, String fqdn, WifiEnterpriseConfig config) {
        this.mRealm = realm;
        switch (config.getEapMethod()) {
            case 1:
            case 2:
                this.mEnterpriseConfig = new WifiEnterpriseConfig(config);
                return;
            default:
                return;
        }
    }

    public WifiPasspointCredential(String type, String caroot, String clientcert, String mcc, String mnc, SpFqdn sp, CredentialInfo credinfo) {
        if (credinfo != null) {
            this.mType = type;
            this.mCaRootCert = caroot;
            this.mClientCert = clientcert;
            this.mWifiSpFqdn = sp.nodeName;
            this.mUpdateIdentifier = sp.perProviderSubscription.UpdateIdentifier;
            this.mCredentialName = credinfo.nodeName;
            this.mOtherHomePartnerList = credinfo.homeSP.otherHomePartners.values();
            Iterator i = credinfo.aAAServerTrustRoot.entrySet().iterator();
            if (i.hasNext()) {
                AAAServerTrustRoot aaa = (AAAServerTrustRoot) ((Entry) i.next()).getValue();
                this.mAaaCertUrl = aaa.CertURL;
                this.mAaaSha256Fingerprint = aaa.CertSHA256Fingerprint;
            }
            this.mCertType = credinfo.credential.digitalCertificate.CertificateType;
            this.mCertSha256Fingerprint = credinfo.credential.digitalCertificate.CertSHA256Fingerprint;
            this.mUsername = credinfo.credential.usernamePassword.Username;
            this.mPasswd = credinfo.credential.usernamePassword.Password;
            this.mIsMachineRemediation = credinfo.credential.usernamePassword.MachineManaged;
            this.mInnerMethod = credinfo.credential.usernamePassword.eAPMethod.InnerMethod;
            this.mImsi = credinfo.credential.sim.IMSI;
            this.mMcc = mcc;
            this.mMnc = mnc;
            this.mCreationDate = credinfo.credential.CreationDate;
            this.mExpirationDate = credinfo.credential.ExpirationDate;
            this.mRealm = credinfo.credential.Realm;
            if (credinfo.credentialPriority == null) {
                this.mCrednetialPriority = 128;
            } else {
                this.mCrednetialPriority = Integer.parseInt(credinfo.credentialPriority);
            }
            this.mHomeSpFqdn = credinfo.homeSP.FQDN;
            this.mSubscriptionUpdateInterval = credinfo.subscriptionUpdate.UpdateInterval;
            this.mSubscriptionUpdateMethod = credinfo.subscriptionUpdate.UpdateMethod;
            this.mSubscriptionUpdateRestriction = credinfo.subscriptionUpdate.Restriction;
            this.mSubscriptionUpdateURI = credinfo.subscriptionUpdate.URI;
            this.mSubscriptionUpdateUsername = credinfo.subscriptionUpdate.usernamePassword.Username;
            this.mSubscriptionUpdatePassword = credinfo.subscriptionUpdate.usernamePassword.Password;
            this.mPolicyUpdateUri = credinfo.policy.policyUpdate.URI;
            this.mPolicyUpdateInterval = credinfo.policy.policyUpdate.UpdateInterval;
            this.mPolicyUpdateUsername = credinfo.policy.policyUpdate.usernamePassword.Username;
            this.mPolicyUpdatePassword = credinfo.policy.policyUpdate.usernamePassword.Password;
            this.mPolicyUpdateRestriction = credinfo.policy.policyUpdate.Restriction;
            this.mPolicyUpdateMethod = credinfo.policy.policyUpdate.UpdateMethod;
            this.mPreferredRoamingPartnerList = credinfo.policy.preferredRoamingPartnerList.values();
            this.mMinBackhaulThresholdNetwork = credinfo.policy.minBackhaulThreshold.values();
            this.mRequiredProtoPortTuple = credinfo.policy.requiredProtoPortTuple.values();
            this.mMaxBssLoad = credinfo.policy.maximumBSSLoadValue;
            this.mSpExclusionList = credinfo.policy.sPExclusionList.values();
            this.mHomeOIList = credinfo.homeSP.homeOIList.values();
            this.mFriendlyName = credinfo.homeSP.FriendlyName;
            this.mCheckAaaServerCertStatus = credinfo.credential.CheckAAAServerCertStatus;
        }
    }

    public String getUpdateIdentifier() {
        return this.mUpdateIdentifier;
    }

    public String getUpdateMethod() {
        return this.mSubscriptionUpdateMethod;
    }

    public void setUpdateMethod(String method) {
        this.mSubscriptionUpdateMethod = method;
    }

    public String getWifiSpFqdn() {
        return this.mWifiSpFqdn;
    }

    public String getCredName() {
        return this.mCredentialName;
    }

    public String getType() {
        return this.mType;
    }

    public WifiEnterpriseConfig getEnterpriseConfig() {
        return new WifiEnterpriseConfig(this.mEnterpriseConfig);
    }

    public void setEnterpriseConfig(WifiEnterpriseConfig config) {
    }

    public String getCertType() {
        return this.mCertType;
    }

    public String getCertSha256Fingerprint() {
        return this.mCertSha256Fingerprint;
    }

    public String getUserName() {
        return this.mUsername;
    }

    public String getPassword() {
        return this.mPasswd;
    }

    public String getImsi() {
        return this.mImsi;
    }

    public String getMcc() {
        return this.mMcc;
    }

    public String getMnc() {
        return this.mMnc;
    }

    public String getCaRootCertPath() {
        return this.mCaRootCert;
    }

    public String getClientCertPath() {
        return this.mClientCert;
    }

    public String getRealm() {
        return this.mRealm;
    }

    public void setRealm(String realm) {
        this.mRealm = realm;
    }

    public int getPriority() {
        if (this.mUserPreferred) {
            return 0;
        }
        return this.mCrednetialPriority;
    }

    public String getHomeSpFqdn() {
        return this.mHomeSpFqdn;
    }

    public void setHomeFqdn(String fqdn) {
        this.mHomeSpFqdn = fqdn;
    }

    public Collection<OtherHomePartners> getOtherHomePartnerList() {
        return this.mOtherHomePartnerList;
    }

    public String getSubscriptionUpdateUsername() {
        return this.mSubscriptionUpdateUsername;
    }

    public String getSubscriptionUpdatePassword() {
        return this.mSubscriptionUpdatePassword;
    }

    public String getPolicyUpdateUri() {
        return this.mPolicyUpdateUri;
    }

    public String getPolicyUpdateInterval() {
        return this.mPolicyUpdateInterval;
    }

    public String getPolicyUpdateUsername() {
        return this.mPolicyUpdateUsername;
    }

    public String getPolicyUpdatePassword() {
        return this.mPolicyUpdatePassword;
    }

    public String getPolicyUpdateRestriction() {
        return this.mPolicyUpdateRestriction;
    }

    public String getPolicyUpdateMethod() {
        return this.mPolicyUpdateMethod;
    }

    public String getCreationDate() {
        return this.mCreationDate;
    }

    public String getExpirationDate() {
        return this.mExpirationDate;
    }

    public void setExpirationDate(String expirationdate) {
        this.mExpirationDate = expirationdate;
    }

    public Collection<PreferredRoamingPartnerList> getPreferredRoamingPartnerList() {
        return this.mPreferredRoamingPartnerList;
    }

    public Collection<HomeOIList> getHomeOiList() {
        return this.mHomeOIList;
    }

    public Collection<MinBackhaulThresholdNetwork> getBackhaulThresholdList() {
        return this.mMinBackhaulThresholdNetwork;
    }

    public Collection<RequiredProtoPortTuple> getRequiredProtoPortList() {
        return this.mRequiredProtoPortTuple;
    }

    public Collection<SPExclusionList> getSPExclusionList() {
        return this.mSpExclusionList;
    }

    public boolean getIsMachineRemediation() {
        return this.mIsMachineRemediation;
    }

    public String getAaaCertUrl() {
        return this.mAaaCertUrl;
    }

    public String getAaaSha256Fingerprint() {
        return this.mAaaSha256Fingerprint;
    }

    public String getSubscriptionUpdateRestriction() {
        return this.mSubscriptionUpdateRestriction;
    }

    public String getSubscriptionUpdateURI() {
        return this.mSubscriptionUpdateURI;
    }

    public String getSubscriptionUpdateInterval() {
        return this.mSubscriptionUpdateInterval;
    }

    public String getFriendlyName() {
        return this.mFriendlyName;
    }

    public String getMaxBssLoad() {
        return this.mMaxBssLoad;
    }

    public boolean getUserPreference() {
        return this.mUserPreferred;
    }

    public boolean getCheckAaaServerCertStatus() {
        return this.mCheckAaaServerCertStatus;
    }

    public void setUserPreference(boolean value) {
        this.mUserPreferred = value;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (!(obj instanceof WifiPasspointCredential)) {
            return false;
        }
        WifiPasspointCredential other = (WifiPasspointCredential) obj;
        if (!this.mType.equals(other.mType)) {
            return false;
        }
        if (this.mType.equals("TTLS")) {
            if (this.mUsername.equals(other.mUsername) && this.mPasswd.equals(other.mPasswd) && this.mRealm.equals(other.mRealm) && this.mHomeSpFqdn.equals(other.mHomeSpFqdn)) {
                result = true;
            } else {
                result = false;
            }
        }
        if (this.mType.equals("TLS")) {
            if (this.mRealm.equals(other.mRealm) && this.mHomeSpFqdn.equals(other.mHomeSpFqdn) && this.mClientCert.equals(other.mClientCert)) {
                result = true;
            } else {
                result = false;
            }
        }
        if (!this.mType.equals("SIM")) {
            return result;
        }
        if (this.mMcc.equals(other.mMcc) && this.mMnc.equals(other.mMnc) && this.mImsi.equals(other.mImsi) && this.mHomeSpFqdn.equals(other.mHomeSpFqdn)) {
            return true;
        }
        return false;
    }

    public String toString() {
        String str;
        Object obj;
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        StringBuffer append = sb.append(", UpdateIdentifier: ").append(this.mUpdateIdentifier == null ? none : this.mUpdateIdentifier).append(", SubscriptionUpdateMethod: ");
        if (this.mSubscriptionUpdateMethod == null) {
            str = none;
        } else {
            str = this.mSubscriptionUpdateMethod;
        }
        append = append.append(str).append(", Type: ");
        if (this.mType == null) {
            str = none;
        } else {
            str = this.mType;
        }
        append = append.append(str).append(", Username: ");
        if (this.mUsername == null) {
            str = none;
        } else {
            str = this.mUsername;
        }
        append = append.append(str).append(", Passwd: ");
        if (this.mPasswd == null) {
            str = none;
        } else {
            str = this.mPasswd;
        }
        append = append.append(str).append(", SubDMAccUsername: ");
        if (this.mSubscriptionUpdateUsername == null) {
            str = none;
        } else {
            str = this.mSubscriptionUpdateUsername;
        }
        append = append.append(str).append(", SubDMAccPassword: ");
        if (this.mSubscriptionUpdatePassword == null) {
            str = none;
        } else {
            str = this.mSubscriptionUpdatePassword;
        }
        append = append.append(str).append(", PolDMAccUsername: ");
        if (this.mPolicyUpdateUsername == null) {
            str = none;
        } else {
            str = this.mPolicyUpdateUsername;
        }
        append = append.append(str).append(", PolDMAccPassword: ");
        if (this.mPolicyUpdatePassword == null) {
            str = none;
        } else {
            str = this.mPolicyUpdatePassword;
        }
        append = append.append(str).append(", Imsi: ");
        if (this.mImsi == null) {
            str = none;
        } else {
            str = this.mImsi;
        }
        append = append.append(str).append(", Mcc: ");
        if (this.mMcc == null) {
            str = none;
        } else {
            str = this.mMcc;
        }
        append = append.append(str).append(", Mnc: ");
        if (this.mMnc == null) {
            str = none;
        } else {
            str = this.mMnc;
        }
        append = append.append(str).append(", CaRootCert: ");
        if (this.mCaRootCert == null) {
            str = none;
        } else {
            str = this.mCaRootCert;
        }
        append = append.append(str).append(", Realm: ");
        if (this.mRealm == null) {
            str = none;
        } else {
            str = this.mRealm;
        }
        append = append.append(str).append(", Priority: ").append(this.mCrednetialPriority).append(", Fqdn: ");
        if (this.mHomeSpFqdn == null) {
            str = none;
        } else {
            str = this.mHomeSpFqdn;
        }
        append = append.append(str).append(", Otherhomepartners: ");
        if (this.mOtherHomePartnerList == null) {
            obj = none;
        } else {
            obj = this.mOtherHomePartnerList;
        }
        append = append.append(obj).append(", ExpirationDate: ");
        if (this.mExpirationDate == null) {
            str = none;
        } else {
            str = this.mExpirationDate;
        }
        StringBuffer append2 = append.append(str).append(", MaxBssLoad: ");
        if (this.mMaxBssLoad != null) {
            none = this.mMaxBssLoad;
        }
        append2.append(none).append(", SPExclusionList: ").append(this.mSpExclusionList);
        if (this.mPreferredRoamingPartnerList != null) {
            sb.append("PreferredRoamingPartnerList:");
            for (PreferredRoamingPartnerList prpListItem : this.mPreferredRoamingPartnerList) {
                sb.append("[fqdnmatch:").append(prpListItem.FQDN_Match).append(", priority:").append(prpListItem.Priority).append(", country:").append(prpListItem.Country).append("]");
            }
        }
        if (this.mHomeOIList != null) {
            sb.append("HomeOIList:");
            for (HomeOIList HomeOIListItem : this.mHomeOIList) {
                sb.append("[HomeOI:").append(HomeOIListItem.HomeOI).append(", HomeOIRequired:").append(HomeOIListItem.HomeOIRequired).append("]");
            }
        }
        if (this.mMinBackhaulThresholdNetwork != null) {
            sb.append("BackHaulThreshold:");
            for (MinBackhaulThresholdNetwork BhtListItem : this.mMinBackhaulThresholdNetwork) {
                sb.append("[networkType:").append(BhtListItem.NetworkType).append(", dlBandwidth:").append(BhtListItem.DLBandwidth).append(", ulBandwidth:").append(BhtListItem.ULBandwidth).append("]");
            }
        }
        if (this.mRequiredProtoPortTuple != null) {
            sb.append("WifiMORequiredProtoPortTupleList:");
            for (RequiredProtoPortTuple RpptListItem : this.mRequiredProtoPortTuple) {
                sb.append("[IPProtocol:").append(RpptListItem.IPProtocol).append(", PortNumber:").append(RpptListItem.PortNumber).append("]");
            }
        }
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWifiSpFqdn);
        dest.writeString(this.mCredentialName);
        dest.writeString(this.mType);
        dest.writeInt(this.mCrednetialPriority);
        dest.writeString(this.mHomeSpFqdn);
        dest.writeString(this.mRealm);
    }

    public void readFromParcel(Parcel in) {
        this.mWifiSpFqdn = in.readString();
        this.mCredentialName = in.readString();
        this.mType = in.readString();
        this.mCrednetialPriority = in.readInt();
        this.mHomeSpFqdn = in.readString();
        this.mRealm = in.readString();
    }

    public int compareTo(WifiPasspointCredential another) {
        if (this.mCrednetialPriority < another.mCrednetialPriority) {
            return -1;
        }
        if (this.mCrednetialPriority == another.mCrednetialPriority) {
            return this.mType.compareTo(another.mType);
        }
        return 1;
    }

    public int hashCode() {
        int hash = 208;
        if (this.mType != null) {
            hash = 208 + this.mType.hashCode();
        }
        if (this.mRealm != null) {
            hash += this.mRealm.hashCode();
        }
        if (this.mHomeSpFqdn != null) {
            hash += this.mHomeSpFqdn.hashCode();
        }
        if (this.mUsername != null) {
            hash += this.mUsername.hashCode();
        }
        if (this.mPasswd != null) {
            return hash + this.mPasswd.hashCode();
        }
        return hash;
    }
}
