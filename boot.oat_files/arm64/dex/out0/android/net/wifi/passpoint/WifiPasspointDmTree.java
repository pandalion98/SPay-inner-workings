package android.net.wifi.passpoint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;

public class WifiPasspointDmTree implements Parcelable {
    public static final Creator<WifiPasspointDmTree> CREATOR = new Creator<WifiPasspointDmTree>() {
        public WifiPasspointDmTree createFromParcel(Parcel in) {
            return new WifiPasspointDmTree(in);
        }

        public WifiPasspointDmTree[] newArray(int size) {
            return new WifiPasspointDmTree[size];
        }
    };
    private static final String TAG = "WifiTree";
    public int PpsMoId;
    public HashMap<String, SpFqdn> spFqdn = new HashMap();

    public static class AAAServerTrustRoot implements Parcelable {
        public static final Creator<AAAServerTrustRoot> CREATOR = new Creator<AAAServerTrustRoot>() {
            public AAAServerTrustRoot createFromParcel(Parcel in) {
                return new AAAServerTrustRoot(in);
            }

            public AAAServerTrustRoot[] newArray(int size) {
                return new AAAServerTrustRoot[size];
            }
        };
        public String CertSHA256Fingerprint;
        public String CertURL;
        public String nodeName;

        public AAAServerTrustRoot(String nn, String url, String fp) {
            this.nodeName = nn;
            this.CertURL = url;
            this.CertSHA256Fingerprint = fp;
        }

        public AAAServerTrustRoot(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.CertURL);
            out.writeString(this.CertSHA256Fingerprint);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.CertURL = in.readString();
                this.CertSHA256Fingerprint = in.readString();
            }
        }
    }

    public static class Credential implements Parcelable {
        public static final Creator<Credential> CREATOR = new Creator<Credential>() {
            public Credential createFromParcel(Parcel in) {
                return new Credential(in);
            }

            public Credential[] newArray(int size) {
                return new Credential[size];
            }
        };
        public boolean CheckAAAServerCertStatus;
        public String CreationDate;
        public String ExpirationDate;
        public String Realm;
        public DigitalCertificate digitalCertificate = new DigitalCertificate();
        public SIM sim = new SIM();
        public UsernamePassword usernamePassword = new UsernamePassword();

        public Credential(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.CreationDate);
            out.writeString(this.ExpirationDate);
            out.writeParcelable(this.usernamePassword, flags);
            out.writeParcelable(this.digitalCertificate, flags);
            out.writeString(this.Realm);
            out.writeInt(this.CheckAAAServerCertStatus ? 1 : 0);
            out.writeParcelable(this.sim, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.CreationDate = in.readString();
                this.ExpirationDate = in.readString();
                this.usernamePassword = (UsernamePassword) in.readParcelable(UsernamePassword.class.getClassLoader());
                this.digitalCertificate = (DigitalCertificate) in.readParcelable(DigitalCertificate.class.getClassLoader());
                this.Realm = in.readString();
                this.CheckAAAServerCertStatus = in.readInt() == 1;
                this.sim = (SIM) in.readParcelable(SIM.class.getClassLoader());
            }
        }
    }

    public static class CredentialInfo implements Parcelable {
        public static final Creator<CredentialInfo> CREATOR = new Creator<CredentialInfo>() {
            public CredentialInfo createFromParcel(Parcel in) {
                return new CredentialInfo(in);
            }

            public CredentialInfo[] newArray(int size) {
                return new CredentialInfo[size];
            }
        };
        public HashMap<String, AAAServerTrustRoot> aAAServerTrustRoot = new HashMap();
        public Credential credential = new Credential();
        public String credentialPriority;
        public Extension extension = new Extension();
        public HomeSP homeSP = new HomeSP();
        public String nodeName;
        public Policy policy = new Policy();
        public SubscriptionParameters subscriptionParameters = new SubscriptionParameters();
        public SubscriptionUpdate subscriptionUpdate = new SubscriptionUpdate();

        public CredentialInfo(String nn) {
            this.nodeName = nn;
        }

        public AAAServerTrustRoot createAAAServerTrustRoot(String name, String url, String fp) {
            AAAServerTrustRoot obj = new AAAServerTrustRoot(name, url, fp);
            this.aAAServerTrustRoot.put(name, obj);
            return obj;
        }

        public CredentialInfo(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeParcelable(this.policy, flags);
            out.writeString(this.credentialPriority);
            out.writeMap(this.aAAServerTrustRoot);
            out.writeParcelable(this.subscriptionUpdate, flags);
            out.writeParcelable(this.homeSP, flags);
            out.writeParcelable(this.subscriptionParameters, flags);
            out.writeParcelable(this.credential, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.policy = (Policy) in.readParcelable(Policy.class.getClassLoader());
                this.credentialPriority = in.readString();
                in.readMap(this.aAAServerTrustRoot, AAAServerTrustRoot.class.getClassLoader());
                this.subscriptionUpdate = (SubscriptionUpdate) in.readParcelable(SubscriptionUpdate.class.getClassLoader());
                this.homeSP = (HomeSP) in.readParcelable(HomeSP.class.getClassLoader());
                this.subscriptionParameters = (SubscriptionParameters) in.readParcelable(SubscriptionParameters.class.getClassLoader());
                this.credential = (Credential) in.readParcelable(Credential.class.getClassLoader());
            }
        }
    }

    public static class DigitalCertificate implements Parcelable {
        public static final Creator<DigitalCertificate> CREATOR = new Creator<DigitalCertificate>() {
            public DigitalCertificate createFromParcel(Parcel in) {
                return new DigitalCertificate(in);
            }

            public DigitalCertificate[] newArray(int size) {
                return new DigitalCertificate[size];
            }
        };
        public String CertSHA256Fingerprint;
        public String CertificateType;

        public DigitalCertificate(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.CertificateType);
            out.writeString(this.CertSHA256Fingerprint);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.CertificateType = in.readString();
                this.CertSHA256Fingerprint = in.readString();
            }
        }
    }

    public static class EAPMethod implements Parcelable {
        public static final Creator<EAPMethod> CREATOR = new Creator<EAPMethod>() {
            public EAPMethod createFromParcel(Parcel in) {
                return new EAPMethod(in);
            }

            public EAPMethod[] newArray(int size) {
                return new EAPMethod[size];
            }
        };
        public String EAPType;
        public String InnerEAPType;
        public String InnerMethod;
        public String InnerVendorId;
        public String InnerVendorType;
        public String VendorId;
        public String VendorType;

        public EAPMethod(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.EAPType);
            out.writeString(this.VendorId);
            out.writeString(this.VendorType);
            out.writeString(this.InnerEAPType);
            out.writeString(this.InnerVendorId);
            out.writeString(this.InnerVendorType);
            out.writeString(this.InnerMethod);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.EAPType = in.readString();
                this.VendorId = in.readString();
                this.VendorType = in.readString();
                this.InnerEAPType = in.readString();
                this.InnerVendorId = in.readString();
                this.InnerVendorType = in.readString();
                this.InnerMethod = in.readString();
            }
        }
    }

    public static class Extension {
        public String empty;
    }

    public static class HomeOIList implements Parcelable {
        public static final Creator<HomeOIList> CREATOR = new Creator<HomeOIList>() {
            public HomeOIList createFromParcel(Parcel in) {
                return new HomeOIList(in);
            }

            public HomeOIList[] newArray(int size) {
                return new HomeOIList[size];
            }
        };
        public String HomeOI;
        public boolean HomeOIRequired;
        public String nodeName;

        public HomeOIList(String nn, String h, boolean r) {
            this.nodeName = nn;
            this.HomeOI = h;
            this.HomeOIRequired = r;
        }

        public HomeOIList(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.HomeOI);
            out.writeInt(this.HomeOIRequired ? 1 : 0);
        }

        public void readFromParcel(Parcel in) {
            boolean z = true;
            if (in != null) {
                this.nodeName = in.readString();
                this.HomeOI = in.readString();
                if (in.readInt() != 1) {
                    z = false;
                }
                this.HomeOIRequired = z;
            }
        }
    }

    public static class HomeSP implements Parcelable {
        public static final Creator<HomeSP> CREATOR = new Creator<HomeSP>() {
            public HomeSP createFromParcel(Parcel in) {
                return new HomeSP(in);
            }

            public HomeSP[] newArray(int size) {
                return new HomeSP[size];
            }
        };
        public String FQDN;
        public String FriendlyName;
        public String IconURL;
        public String RoamingConsortiumOI;
        public HashMap<String, HomeOIList> homeOIList = new HashMap();
        public HashMap<String, NetworkID> networkID = new HashMap();
        public HashMap<String, OtherHomePartners> otherHomePartners = new HashMap();

        public NetworkID createNetworkID(String name, String ssid, String hessid) {
            NetworkID obj = new NetworkID(name, ssid, hessid);
            this.networkID.put(name, obj);
            return obj;
        }

        public HomeOIList createHomeOIList(String name, String homeoi, boolean required) {
            HomeOIList obj = new HomeOIList(name, homeoi, required);
            this.homeOIList.put(name, obj);
            return obj;
        }

        public OtherHomePartners createOtherHomePartners(String name, String fqdn) {
            OtherHomePartners obj = new OtherHomePartners(name, fqdn);
            this.otherHomePartners.put(name, obj);
            return obj;
        }

        public HomeSP(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeMap(this.networkID);
            out.writeString(this.FriendlyName);
            out.writeString(this.IconURL);
            out.writeString(this.FQDN);
            out.writeMap(this.homeOIList);
            out.writeMap(this.otherHomePartners);
            out.writeString(this.RoamingConsortiumOI);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                in.readMap(this.networkID, NetworkID.class.getClassLoader());
                this.FriendlyName = in.readString();
                this.IconURL = in.readString();
                this.FQDN = in.readString();
                in.readMap(this.homeOIList, HomeOIList.class.getClassLoader());
                in.readMap(this.otherHomePartners, OtherHomePartners.class.getClassLoader());
                this.RoamingConsortiumOI = in.readString();
            }
        }
    }

    public static class MinBackhaulThresholdNetwork implements Parcelable {
        public static final Creator<MinBackhaulThresholdNetwork> CREATOR = new Creator<MinBackhaulThresholdNetwork>() {
            public MinBackhaulThresholdNetwork createFromParcel(Parcel in) {
                return new MinBackhaulThresholdNetwork(in);
            }

            public MinBackhaulThresholdNetwork[] newArray(int size) {
                return new MinBackhaulThresholdNetwork[size];
            }
        };
        public String DLBandwidth;
        public String NetworkType;
        public String ULBandwidth;
        public String nodeName;

        public MinBackhaulThresholdNetwork(String nn, String nt, String d, String u) {
            this.nodeName = nn;
            this.NetworkType = nt;
            this.DLBandwidth = d;
            this.ULBandwidth = u;
        }

        public MinBackhaulThresholdNetwork(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.NetworkType);
            out.writeString(this.DLBandwidth);
            out.writeString(this.ULBandwidth);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.NetworkType = in.readString();
                this.DLBandwidth = in.readString();
                this.ULBandwidth = in.readString();
            }
        }
    }

    public static class NetworkID implements Parcelable {
        public static final Creator<NetworkID> CREATOR = new Creator<NetworkID>() {
            public NetworkID createFromParcel(Parcel in) {
                return new NetworkID(in);
            }

            public NetworkID[] newArray(int size) {
                return new NetworkID[size];
            }
        };
        public String HESSID;
        public String SSID;
        public String nodeName;

        public NetworkID(String nn, String s, String h) {
            this.nodeName = nn;
            this.SSID = s;
            this.HESSID = h;
        }

        public NetworkID(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.SSID);
            out.writeString(this.HESSID);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.SSID = in.readString();
                this.HESSID = in.readString();
            }
        }
    }

    public static class OtherHomePartners implements Parcelable {
        public static final Creator<OtherHomePartners> CREATOR = new Creator<OtherHomePartners>() {
            public OtherHomePartners createFromParcel(Parcel in) {
                return new OtherHomePartners(in);
            }

            public OtherHomePartners[] newArray(int size) {
                return new OtherHomePartners[size];
            }
        };
        public String FQDN;
        public String nodeName;

        public OtherHomePartners(String nn, String f) {
            this.nodeName = nn;
            this.FQDN = f;
        }

        public OtherHomePartners(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.FQDN);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.FQDN = in.readString();
            }
        }
    }

    public static class PerProviderSubscription implements Parcelable {
        public static final Creator<PerProviderSubscription> CREATOR = new Creator<PerProviderSubscription>() {
            public PerProviderSubscription createFromParcel(Parcel in) {
                return new PerProviderSubscription(in);
            }

            public PerProviderSubscription[] newArray(int size) {
                return new PerProviderSubscription[size];
            }
        };
        public String UpdateIdentifier;
        public HashMap<String, CredentialInfo> credentialInfo = new HashMap();

        public CredentialInfo createCredentialInfo(String name) {
            CredentialInfo obj = new CredentialInfo(name);
            this.credentialInfo.put(name, obj);
            return obj;
        }

        public PerProviderSubscription(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.UpdateIdentifier);
            out.writeMap(this.credentialInfo);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.UpdateIdentifier = in.readString();
                in.readMap(this.credentialInfo, CredentialInfo.class.getClassLoader());
            }
        }
    }

    public static class Policy implements Parcelable {
        public static final Creator<Policy> CREATOR = new Creator<Policy>() {
            public Policy createFromParcel(Parcel in) {
                return new Policy(in);
            }

            public Policy[] newArray(int size) {
                return new Policy[size];
            }
        };
        public String maximumBSSLoadValue;
        public HashMap<String, MinBackhaulThresholdNetwork> minBackhaulThreshold = new HashMap();
        public PolicyUpdate policyUpdate = new PolicyUpdate();
        public HashMap<String, PreferredRoamingPartnerList> preferredRoamingPartnerList = new HashMap();
        public HashMap<String, RequiredProtoPortTuple> requiredProtoPortTuple = new HashMap();
        public HashMap<String, SPExclusionList> sPExclusionList = new HashMap();

        public PreferredRoamingPartnerList createPreferredRoamingPartnerList(String name, String fqdn, String priority, String country) {
            PreferredRoamingPartnerList obj = new PreferredRoamingPartnerList(name, fqdn, priority, country);
            this.preferredRoamingPartnerList.put(name, obj);
            return obj;
        }

        public MinBackhaulThresholdNetwork createMinBackhaulThreshold(String name, String type, String dl, String ul) {
            MinBackhaulThresholdNetwork obj = new MinBackhaulThresholdNetwork(name, type, dl, ul);
            this.minBackhaulThreshold.put(name, obj);
            return obj;
        }

        public SPExclusionList createSPExclusionList(String name, String ssid) {
            SPExclusionList obj = new SPExclusionList(name, ssid);
            this.sPExclusionList.put(name, obj);
            return obj;
        }

        public RequiredProtoPortTuple createRequiredProtoPortTuple(String name, String proto, String port) {
            RequiredProtoPortTuple obj = new RequiredProtoPortTuple(name, proto, port);
            this.requiredProtoPortTuple.put(name, obj);
            return obj;
        }

        public Policy(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeMap(this.preferredRoamingPartnerList);
            out.writeMap(this.minBackhaulThreshold);
            out.writeParcelable(this.policyUpdate, flags);
            out.writeMap(this.sPExclusionList);
            out.writeMap(this.requiredProtoPortTuple);
            out.writeString(this.maximumBSSLoadValue);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                in.readMap(this.preferredRoamingPartnerList, PreferredRoamingPartnerList.class.getClassLoader());
                in.readMap(this.minBackhaulThreshold, MinBackhaulThresholdNetwork.class.getClassLoader());
                this.policyUpdate = (PolicyUpdate) in.readParcelable(PolicyUpdate.class.getClassLoader());
                in.readMap(this.sPExclusionList, SPExclusionList.class.getClassLoader());
                in.readMap(this.requiredProtoPortTuple, RequiredProtoPortTuple.class.getClassLoader());
                this.maximumBSSLoadValue = in.readString();
            }
        }
    }

    public static class PolicyUpdate implements Parcelable {
        public static final Creator<PolicyUpdate> CREATOR = new Creator<PolicyUpdate>() {
            public PolicyUpdate createFromParcel(Parcel in) {
                return new PolicyUpdate(in);
            }

            public PolicyUpdate[] newArray(int size) {
                return new PolicyUpdate[size];
            }
        };
        public String Other;
        public String Restriction;
        public String URI;
        public String UpdateInterval;
        public String UpdateMethod;
        public TrustRoot trustRoot = new TrustRoot();
        public UsernamePassword usernamePassword = new UsernamePassword();

        public PolicyUpdate(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.UpdateInterval);
            out.writeString(this.UpdateMethod);
            out.writeString(this.Restriction);
            out.writeString(this.URI);
            out.writeParcelable(this.usernamePassword, flags);
            out.writeString(this.Other);
            out.writeParcelable(this.trustRoot, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.UpdateInterval = in.readString();
                this.UpdateMethod = in.readString();
                this.Restriction = in.readString();
                this.URI = in.readString();
                this.usernamePassword = (UsernamePassword) in.readParcelable(UsernamePassword.class.getClassLoader());
                this.Other = in.readString();
                this.trustRoot = (TrustRoot) in.readParcelable(TrustRoot.class.getClassLoader());
            }
        }
    }

    public static class PreferredRoamingPartnerList implements Parcelable {
        public static final Creator<PreferredRoamingPartnerList> CREATOR = new Creator<PreferredRoamingPartnerList>() {
            public PreferredRoamingPartnerList createFromParcel(Parcel in) {
                return new PreferredRoamingPartnerList(in);
            }

            public PreferredRoamingPartnerList[] newArray(int size) {
                return new PreferredRoamingPartnerList[size];
            }
        };
        public String Country;
        public String FQDN_Match;
        public String Priority;
        public String nodeName;

        public PreferredRoamingPartnerList(String nn, String f, String p, String c) {
            this.nodeName = nn;
            this.FQDN_Match = f;
            this.Priority = p;
            this.Country = c;
        }

        public PreferredRoamingPartnerList(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.FQDN_Match);
            out.writeString(this.Priority);
            out.writeString(this.Country);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.FQDN_Match = in.readString();
                this.Priority = in.readString();
                this.Country = in.readString();
            }
        }
    }

    public static class RequiredProtoPortTuple implements Parcelable {
        public static final Creator<RequiredProtoPortTuple> CREATOR = new Creator<RequiredProtoPortTuple>() {
            public RequiredProtoPortTuple createFromParcel(Parcel in) {
                return new RequiredProtoPortTuple(in);
            }

            public RequiredProtoPortTuple[] newArray(int size) {
                return new RequiredProtoPortTuple[size];
            }
        };
        public String IPProtocol;
        public String PortNumber;
        public String nodeName;

        public RequiredProtoPortTuple(String nn, String protocol, String port) {
            this.nodeName = nn;
            this.IPProtocol = protocol;
            this.PortNumber = port;
        }

        public RequiredProtoPortTuple(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.IPProtocol);
            out.writeString(this.PortNumber);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.IPProtocol = in.readString();
                this.PortNumber = in.readString();
            }
        }
    }

    public static class SIM implements Parcelable {
        public static final Creator<SIM> CREATOR = new Creator<SIM>() {
            public SIM createFromParcel(Parcel in) {
                return new SIM(in);
            }

            public SIM[] newArray(int size) {
                return new SIM[size];
            }
        };
        public String EAPType;
        public String IMSI;

        public SIM(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.IMSI);
            out.writeString(this.EAPType);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.IMSI = in.readString();
                this.EAPType = in.readString();
            }
        }
    }

    public static class SPExclusionList implements Parcelable {
        public static final Creator<SPExclusionList> CREATOR = new Creator<SPExclusionList>() {
            public SPExclusionList createFromParcel(Parcel in) {
                return new SPExclusionList(in);
            }

            public SPExclusionList[] newArray(int size) {
                return new SPExclusionList[size];
            }
        };
        public String SSID;
        public String nodeName;

        public SPExclusionList(String nn, String s) {
            this.nodeName = nn;
            this.SSID = s;
        }

        public SPExclusionList(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeString(this.SSID);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.SSID = in.readString();
            }
        }
    }

    public static class SpFqdn implements Parcelable {
        public static final Creator<SpFqdn> CREATOR = new Creator<SpFqdn>() {
            public SpFqdn createFromParcel(Parcel in) {
                return new SpFqdn(in);
            }

            public SpFqdn[] newArray(int size) {
                return new SpFqdn[size];
            }
        };
        public String nodeName;
        public PerProviderSubscription perProviderSubscription = new PerProviderSubscription();

        public SpFqdn(String name) {
            this.nodeName = name;
        }

        public SpFqdn(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.nodeName);
            out.writeParcelable(this.perProviderSubscription, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.nodeName = in.readString();
                this.perProviderSubscription = (PerProviderSubscription) in.readParcelable(PerProviderSubscription.class.getClassLoader());
            }
        }
    }

    public static class SubscriptionParameters implements Parcelable {
        public static final Creator<SubscriptionParameters> CREATOR = new Creator<SubscriptionParameters>() {
            public SubscriptionParameters createFromParcel(Parcel in) {
                return new SubscriptionParameters(in);
            }

            public SubscriptionParameters[] newArray(int size) {
                return new SubscriptionParameters[size];
            }
        };
        public String CreationDate;
        public String ExpirationDate;
        public String TypeOfSubscription;
        public UsageLimits usageLimits = new UsageLimits();

        public SubscriptionParameters(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.CreationDate);
            out.writeString(this.ExpirationDate);
            out.writeString(this.TypeOfSubscription);
            out.writeParcelable(this.usageLimits, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.CreationDate = in.readString();
                this.ExpirationDate = in.readString();
                this.TypeOfSubscription = in.readString();
                this.usageLimits = (UsageLimits) in.readParcelable(UsageLimits.class.getClassLoader());
            }
        }
    }

    public static class SubscriptionUpdate implements Parcelable {
        public static final Creator<SubscriptionUpdate> CREATOR = new Creator<SubscriptionUpdate>() {
            public SubscriptionUpdate createFromParcel(Parcel in) {
                return new SubscriptionUpdate(in);
            }

            public SubscriptionUpdate[] newArray(int size) {
                return new SubscriptionUpdate[size];
            }
        };
        public String Other;
        public String Restriction;
        public String URI;
        public String UpdateInterval;
        public String UpdateMethod;
        public TrustRoot trustRoot = new TrustRoot();
        public UsernamePassword usernamePassword = new UsernamePassword();

        public SubscriptionUpdate(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.UpdateInterval);
            out.writeString(this.UpdateMethod);
            out.writeString(this.Restriction);
            out.writeString(this.URI);
            out.writeParcelable(this.usernamePassword, flags);
            out.writeString(this.Other);
            out.writeParcelable(this.trustRoot, flags);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.UpdateInterval = in.readString();
                this.UpdateMethod = in.readString();
                this.Restriction = in.readString();
                this.URI = in.readString();
                this.usernamePassword = (UsernamePassword) in.readParcelable(UsernamePassword.class.getClassLoader());
                this.Other = in.readString();
                this.trustRoot = (TrustRoot) in.readParcelable(TrustRoot.class.getClassLoader());
            }
        }
    }

    public static class TrustRoot implements Parcelable {
        public static final Creator<TrustRoot> CREATOR = new Creator<TrustRoot>() {
            public TrustRoot createFromParcel(Parcel in) {
                return new TrustRoot(in);
            }

            public TrustRoot[] newArray(int size) {
                return new TrustRoot[size];
            }
        };
        public String CertSHA256Fingerprint;
        public String CertURL;

        public TrustRoot(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.CertURL);
            out.writeString(this.CertSHA256Fingerprint);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.CertURL = in.readString();
                this.CertSHA256Fingerprint = in.readString();
            }
        }
    }

    public static class UsageLimits implements Parcelable {
        public static final Creator<UsageLimits> CREATOR = new Creator<UsageLimits>() {
            public UsageLimits createFromParcel(Parcel in) {
                return new UsageLimits(in);
            }

            public UsageLimits[] newArray(int size) {
                return new UsageLimits[size];
            }
        };
        public String DataLimit;
        public String StartDate;
        public String TimeLimit;
        public String UsageTimePeriod;

        public UsageLimits(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.DataLimit);
            out.writeString(this.StartDate);
            out.writeString(this.TimeLimit);
            out.writeString(this.UsageTimePeriod);
        }

        public void readFromParcel(Parcel in) {
            if (in != null) {
                this.DataLimit = in.readString();
                this.StartDate = in.readString();
                this.TimeLimit = in.readString();
                this.UsageTimePeriod = in.readString();
            }
        }
    }

    public static class UsernamePassword implements Parcelable {
        public static final Creator<UsernamePassword> CREATOR = new Creator<UsernamePassword>() {
            public UsernamePassword createFromParcel(Parcel in) {
                return new UsernamePassword(in);
            }

            public UsernamePassword[] newArray(int size) {
                return new UsernamePassword[size];
            }
        };
        public String AbleToShare;
        public boolean MachineManaged;
        public String Password;
        public String SoftTokenApp;
        public String Username;
        public EAPMethod eAPMethod = new EAPMethod();

        public UsernamePassword(Parcel in) {
            readFromParcel(in);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.Username);
            out.writeString(this.Password);
            out.writeInt(this.MachineManaged ? 1 : 0);
            out.writeString(this.SoftTokenApp);
            out.writeString(this.AbleToShare);
            out.writeParcelable(this.eAPMethod, flags);
        }

        public void readFromParcel(Parcel in) {
            boolean z = true;
            if (in != null) {
                this.Username = in.readString();
                this.Password = in.readString();
                if (in.readInt() != 1) {
                    z = false;
                }
                this.MachineManaged = z;
                this.SoftTokenApp = in.readString();
                this.AbleToShare = in.readString();
                this.eAPMethod = (EAPMethod) in.readParcelable(EAPMethod.class.getClassLoader());
            }
        }
    }

    public SpFqdn createSpFqdn(String name) {
        SpFqdn obj = new SpFqdn(name);
        this.spFqdn.put(name, obj);
        return obj;
    }

    public WifiPasspointDmTree(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeMap(this.spFqdn);
    }

    public void readFromParcel(Parcel in) {
        if (in != null) {
            in.readMap(this.spFqdn, SpFqdn.class.getClassLoader());
        }
    }
}
