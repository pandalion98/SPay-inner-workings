package android.net;

import android.content.pm.PersonaInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class NetworkCapabilities implements Parcelable {
    public static final Creator<NetworkCapabilities> CREATOR = new Creator<NetworkCapabilities>() {
        public NetworkCapabilities createFromParcel(Parcel in) {
            NetworkCapabilities netCap = new NetworkCapabilities();
            netCap.mNetworkCapabilities = in.readLong();
            netCap.mTransportTypes = in.readLong();
            netCap.mLinkUpBandwidthKbps = in.readInt();
            netCap.mLinkDownBandwidthKbps = in.readInt();
            netCap.mNetworkSpecifier = in.readString();
            netCap.mSignalStrength = in.readInt();
            return netCap;
        }

        public NetworkCapabilities[] newArray(int size) {
            return new NetworkCapabilities[size];
        }
    };
    private static final long DEFAULT_CAPABILITIES = 57344;
    private static final int MAX_NET_CAPABILITY = 21;
    private static final int MAX_TRANSPORT = 4;
    private static final int MIN_NET_CAPABILITY = 0;
    private static final int MIN_TRANSPORT = 0;
    private static final long MUTABLE_CAPABILITIES = 212992;
    public static final int NET_CAPABILITY_BIP = 21;
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
    public static final int NET_CAPABILITY_CAS = 18;
    public static final int NET_CAPABILITY_CBS = 5;
    public static final int NET_CAPABILITY_DUN = 2;
    public static final int NET_CAPABILITY_EIMS = 10;
    public static final int NET_CAPABILITY_ENT1 = 19;
    public static final int NET_CAPABILITY_FOTA = 3;
    public static final int NET_CAPABILITY_IA = 7;
    public static final int NET_CAPABILITY_IMS = 4;
    public static final int NET_CAPABILITY_INTERNET = 12;
    public static final int NET_CAPABILITY_MMS = 0;
    public static final int NET_CAPABILITY_MMS2 = 20;
    public static final int NET_CAPABILITY_NOT_METERED = 11;
    public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
    public static final int NET_CAPABILITY_NOT_VPN = 15;
    public static final int NET_CAPABILITY_RCS = 8;
    public static final int NET_CAPABILITY_SUPL = 1;
    public static final int NET_CAPABILITY_TRUSTED = 14;
    public static final int NET_CAPABILITY_VALIDATED = 16;
    public static final int NET_CAPABILITY_WIFI_P2P = 6;
    public static final int NET_CAPABILITY_XCAP = 9;
    private static final long NON_REQUESTABLE_CAPABILITIES = 196608;
    private static final long RESTRICTED_CAPABILITIES = 1980;
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;
    public static final int TRANSPORT_BLUETOOTH = 2;
    public static final int TRANSPORT_CELLULAR = 0;
    public static final int TRANSPORT_ETHERNET = 3;
    public static final int TRANSPORT_VPN = 4;
    public static final int TRANSPORT_WIFI = 1;
    private int mLinkDownBandwidthKbps;
    private int mLinkUpBandwidthKbps;
    private long mNetworkCapabilities;
    private String mNetworkSpecifier;
    private int mSignalStrength;
    private long mTransportTypes;

    public NetworkCapabilities() {
        clearAll();
        this.mNetworkCapabilities = DEFAULT_CAPABILITIES;
    }

    public NetworkCapabilities(NetworkCapabilities nc) {
        if (nc != null) {
            this.mNetworkCapabilities = nc.mNetworkCapabilities;
            this.mTransportTypes = nc.mTransportTypes;
            this.mLinkUpBandwidthKbps = nc.mLinkUpBandwidthKbps;
            this.mLinkDownBandwidthKbps = nc.mLinkDownBandwidthKbps;
            this.mNetworkSpecifier = nc.mNetworkSpecifier;
            this.mSignalStrength = nc.mSignalStrength;
        }
    }

    public void clearAll() {
        this.mTransportTypes = 0;
        this.mNetworkCapabilities = 0;
        this.mLinkDownBandwidthKbps = 0;
        this.mLinkUpBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mSignalStrength = Integer.MIN_VALUE;
    }

    public NetworkCapabilities addCapability(int capability) {
        if (capability < 0 || capability > 21) {
            throw new IllegalArgumentException("NetworkCapability out of range");
        }
        this.mNetworkCapabilities |= (long) (1 << capability);
        return this;
    }

    public NetworkCapabilities removeCapability(int capability) {
        if (capability < 0 || capability > 21) {
            throw new IllegalArgumentException("NetworkCapability out of range");
        }
        this.mNetworkCapabilities &= (long) ((1 << capability) ^ -1);
        return this;
    }

    public int[] getCapabilities() {
        return enumerateBits(this.mNetworkCapabilities);
    }

    public boolean hasCapability(int capability) {
        if (capability < 0 || capability > 21) {
            return false;
        }
        if ((this.mNetworkCapabilities & ((long) (1 << capability))) == 0) {
            return false;
        }
        return true;
    }

    private int[] enumerateBits(long val) {
        int[] result = new int[Long.bitCount(val)];
        int resource = 0;
        int index = 0;
        while (val > 0) {
            int index2;
            if ((val & 1) == 1) {
                index2 = index + 1;
                result[index] = resource;
            } else {
                index2 = index;
            }
            val >>= 1;
            resource++;
            index = index2;
        }
        return result;
    }

    private void combineNetCapabilities(NetworkCapabilities nc) {
        this.mNetworkCapabilities |= nc.mNetworkCapabilities;
    }

    public String describeFirstNonRequestableCapability() {
        if (hasCapability(16)) {
            return "NET_CAPABILITY_VALIDATED";
        }
        if (hasCapability(17)) {
            return "NET_CAPABILITY_CAPTIVE_PORTAL";
        }
        if ((this.mNetworkCapabilities & NON_REQUESTABLE_CAPABILITIES) != 0) {
            return "unknown non-requestable capabilities " + Long.toHexString(this.mNetworkCapabilities);
        }
        if (this.mLinkUpBandwidthKbps != 0 || this.mLinkDownBandwidthKbps != 0) {
            return "link bandwidth";
        }
        if (hasSignalStrength()) {
            return "signalStrength";
        }
        return null;
    }

    private boolean satisfiedByNetCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        long networkCapabilities = this.mNetworkCapabilities;
        if (onlyImmutable) {
            networkCapabilities &= -212993;
        }
        return (nc.mNetworkCapabilities & networkCapabilities) == networkCapabilities;
    }

    public boolean equalsNetCapabilities(NetworkCapabilities nc) {
        return nc.mNetworkCapabilities == this.mNetworkCapabilities;
    }

    private boolean equalsNetCapabilitiesImmutable(NetworkCapabilities that) {
        return (this.mNetworkCapabilities & -212993) == (that.mNetworkCapabilities & -212993);
    }

    public void maybeMarkCapabilitiesRestricted() {
        if ((this.mNetworkCapabilities & -59325) == 0 && (this.mNetworkCapabilities & RESTRICTED_CAPABILITIES) != 0) {
            removeCapability(13);
        }
    }

    public NetworkCapabilities addTransportType(int transportType) {
        if (transportType < 0 || transportType > 4) {
            throw new IllegalArgumentException("TransportType out of range");
        }
        this.mTransportTypes |= (long) (1 << transportType);
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public NetworkCapabilities removeTransportType(int transportType) {
        if (transportType < 0 || transportType > 4) {
            throw new IllegalArgumentException("TransportType out of range");
        }
        this.mTransportTypes &= (long) ((1 << transportType) ^ -1);
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public int[] getTransportTypes() {
        return enumerateBits(this.mTransportTypes);
    }

    public boolean hasTransport(int transportType) {
        if (transportType < 0 || transportType > 4) {
            return false;
        }
        if ((this.mTransportTypes & ((long) (1 << transportType))) == 0) {
            return false;
        }
        return true;
    }

    private void combineTransportTypes(NetworkCapabilities nc) {
        this.mTransportTypes |= nc.mTransportTypes;
    }

    private boolean satisfiedByTransportTypes(NetworkCapabilities nc) {
        return this.mTransportTypes == 0 || (this.mTransportTypes & nc.mTransportTypes) != 0;
    }

    public boolean equalsTransportTypes(NetworkCapabilities nc) {
        return nc.mTransportTypes == this.mTransportTypes;
    }

    public void setLinkUpstreamBandwidthKbps(int upKbps) {
        this.mLinkUpBandwidthKbps = upKbps;
    }

    public int getLinkUpstreamBandwidthKbps() {
        return this.mLinkUpBandwidthKbps;
    }

    public void setLinkDownstreamBandwidthKbps(int downKbps) {
        this.mLinkDownBandwidthKbps = downKbps;
    }

    public int getLinkDownstreamBandwidthKbps() {
        return this.mLinkDownBandwidthKbps;
    }

    private void combineLinkBandwidths(NetworkCapabilities nc) {
        this.mLinkUpBandwidthKbps = Math.max(this.mLinkUpBandwidthKbps, nc.mLinkUpBandwidthKbps);
        this.mLinkDownBandwidthKbps = Math.max(this.mLinkDownBandwidthKbps, nc.mLinkDownBandwidthKbps);
    }

    private boolean satisfiedByLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps <= nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps <= nc.mLinkDownBandwidthKbps;
    }

    private boolean equalsLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps == nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps == nc.mLinkDownBandwidthKbps;
    }

    public void setNetworkSpecifier(String networkSpecifier) {
        if (TextUtils.isEmpty(networkSpecifier) || Long.bitCount(this.mTransportTypes) == 1) {
            this.mNetworkSpecifier = networkSpecifier;
            return;
        }
        throw new IllegalStateException("Must have a single transport specified to use setNetworkSpecifier");
    }

    public String getNetworkSpecifier() {
        return this.mNetworkSpecifier;
    }

    private void combineSpecifiers(NetworkCapabilities nc) {
        String otherSpecifier = nc.getNetworkSpecifier();
        if (!TextUtils.isEmpty(otherSpecifier)) {
            if (TextUtils.isEmpty(this.mNetworkSpecifier)) {
                setNetworkSpecifier(otherSpecifier);
                return;
            }
            throw new IllegalStateException("Can't combine two networkSpecifiers");
        }
    }

    private boolean satisfiedBySpecifier(NetworkCapabilities nc) {
        return TextUtils.isEmpty(this.mNetworkSpecifier) || this.mNetworkSpecifier.equals(nc.mNetworkSpecifier);
    }

    private boolean equalsSpecifier(NetworkCapabilities nc) {
        if (TextUtils.isEmpty(this.mNetworkSpecifier)) {
            return TextUtils.isEmpty(nc.mNetworkSpecifier);
        }
        return this.mNetworkSpecifier.equals(nc.mNetworkSpecifier);
    }

    public void setSignalStrength(int signalStrength) {
        this.mSignalStrength = signalStrength;
    }

    public boolean hasSignalStrength() {
        return this.mSignalStrength > Integer.MIN_VALUE;
    }

    public int getSignalStrength() {
        return this.mSignalStrength;
    }

    private void combineSignalStrength(NetworkCapabilities nc) {
        this.mSignalStrength = Math.max(this.mSignalStrength, nc.mSignalStrength);
    }

    private boolean satisfiedBySignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength <= nc.mSignalStrength;
    }

    private boolean equalsSignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength == nc.mSignalStrength;
    }

    public void combineCapabilities(NetworkCapabilities nc) {
        combineNetCapabilities(nc);
        combineTransportTypes(nc);
        combineLinkBandwidths(nc);
        combineSpecifiers(nc);
        combineSignalStrength(nc);
    }

    private boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        return nc != null && satisfiedByNetCapabilities(nc, onlyImmutable) && satisfiedByTransportTypes(nc) && ((onlyImmutable || satisfiedByLinkBandwidths(nc)) && satisfiedBySpecifier(nc) && (onlyImmutable || satisfiedBySignalStrength(nc)));
    }

    public boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, false);
    }

    public boolean satisfiedByImmutableNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, true);
    }

    public boolean equalImmutableCapabilities(NetworkCapabilities nc) {
        if (nc != null && equalsNetCapabilitiesImmutable(nc) && equalsTransportTypes(nc) && equalsSpecifier(nc)) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkCapabilities)) {
            return false;
        }
        NetworkCapabilities that = (NetworkCapabilities) obj;
        if (equalsNetCapabilities(that) && equalsTransportTypes(that) && equalsLinkBandwidths(that) && equalsSignalStrength(that) && equalsSpecifier(that)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((TextUtils.isEmpty(this.mNetworkSpecifier) ? 0 : this.mNetworkSpecifier.hashCode() * 17) + ((this.mLinkDownBandwidthKbps * 13) + ((((((int) (this.mNetworkCapabilities & -1)) + (((int) (this.mNetworkCapabilities >> 32)) * 3)) + (((int) (this.mTransportTypes & -1)) * 5)) + (((int) (this.mTransportTypes >> 32)) * 7)) + (this.mLinkUpBandwidthKbps * 11)))) + (this.mSignalStrength * 19);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mNetworkCapabilities);
        dest.writeLong(this.mTransportTypes);
        dest.writeInt(this.mLinkUpBandwidthKbps);
        dest.writeInt(this.mLinkDownBandwidthKbps);
        dest.writeString(this.mNetworkSpecifier);
        dest.writeInt(this.mSignalStrength);
    }

    public String toString() {
        int[] types = getTransportTypes();
        String transports = types.length > 0 ? " Transports: " : ProxyInfo.LOCAL_EXCL_LIST;
        int i = 0;
        while (i < types.length) {
            switch (types[i]) {
                case 0:
                    transports = transports + "CELLULAR";
                    break;
                case 1:
                    transports = transports + "WIFI";
                    break;
                case 2:
                    transports = transports + "BLUETOOTH";
                    break;
                case 3:
                    transports = transports + "ETHERNET";
                    break;
                case 4:
                    transports = transports + "VPN";
                    break;
            }
            i++;
            if (i < types.length) {
                transports = transports + "|";
            }
        }
        types = getCapabilities();
        String capabilities = types.length > 0 ? " Capabilities: " : ProxyInfo.LOCAL_EXCL_LIST;
        i = 0;
        while (i < types.length) {
            switch (types[i]) {
                case 0:
                    capabilities = capabilities + "MMS";
                    break;
                case 1:
                    capabilities = capabilities + "SUPL";
                    break;
                case 2:
                    capabilities = capabilities + "DUN";
                    break;
                case 3:
                    capabilities = capabilities + "FOTA";
                    break;
                case 4:
                    capabilities = capabilities + "IMS";
                    break;
                case 5:
                    capabilities = capabilities + "CBS";
                    break;
                case 6:
                    capabilities = capabilities + "WIFI_P2P";
                    break;
                case 7:
                    capabilities = capabilities + "IA";
                    break;
                case 8:
                    capabilities = capabilities + "RCS";
                    break;
                case 9:
                    capabilities = capabilities + "XCAP";
                    break;
                case 10:
                    capabilities = capabilities + "EIMS";
                    break;
                case 11:
                    capabilities = capabilities + "NOT_METERED";
                    break;
                case 12:
                    capabilities = capabilities + "INTERNET";
                    break;
                case 13:
                    capabilities = capabilities + "NOT_RESTRICTED";
                    break;
                case 14:
                    capabilities = capabilities + "TRUSTED";
                    break;
                case 15:
                    capabilities = capabilities + "NOT_VPN";
                    break;
                case 16:
                    capabilities = capabilities + "VALIDATED";
                    break;
                case 17:
                    capabilities = capabilities + "CAPTIVE_PORTAL";
                    break;
                case 19:
                    capabilities = capabilities + "ENT1";
                    break;
                case 20:
                    capabilities = capabilities + "MMS2";
                    break;
                case 21:
                    capabilities = capabilities + "BIP";
                    break;
            }
            i++;
            if (i < types.length) {
                capabilities = capabilities + "&";
            }
        }
        String upBand = this.mLinkUpBandwidthKbps > 0 ? " LinkUpBandwidth>=" + this.mLinkUpBandwidthKbps + "Kbps" : ProxyInfo.LOCAL_EXCL_LIST;
        String dnBand = this.mLinkDownBandwidthKbps > 0 ? " LinkDnBandwidth>=" + this.mLinkDownBandwidthKbps + "Kbps" : ProxyInfo.LOCAL_EXCL_LIST;
        return "[" + transports + capabilities + upBand + dnBand + (this.mNetworkSpecifier == null ? ProxyInfo.LOCAL_EXCL_LIST : " Specifier: <" + this.mNetworkSpecifier + ">") + (hasSignalStrength() ? " SignalStrength: " + this.mSignalStrength : ProxyInfo.LOCAL_EXCL_LIST) + "]";
    }

    public int[] getDiffCapabilities(NetworkCapabilities nc) {
        return enumerateBits((this.mNetworkCapabilities ^ -1) & nc.mNetworkCapabilities);
    }

    public String firstNetCapToApnType() {
        String apnType = ProxyInfo.LOCAL_EXCL_LIST;
        switch (getCapabilities()[0]) {
            case 0:
                return "mms";
            case 1:
                return "supl";
            case 2:
                return "dun";
            case 3:
                return "fota";
            case 4:
                return "ims";
            case 5:
                return "cbs";
            case 9:
                return "xcap";
            case 12:
                return PersonaInfo.PERSONA_TYPE_DEFAULT;
            case 18:
                return "cas";
            default:
                return apnType;
        }
    }
}
