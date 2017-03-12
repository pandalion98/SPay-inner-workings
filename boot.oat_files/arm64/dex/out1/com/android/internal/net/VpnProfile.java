package com.android.internal.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class VpnProfile implements Cloneable, Parcelable {
    private static final String CC_PROPERTY = "security.mdpp";
    private static final String CC_PROPERTY_ENABLED_VALUE = "Enabled";
    private static final String CC_PROPERTY_ENFORCING_VALUE = "Enforcing";
    public static final Creator<VpnProfile> CREATOR = new Creator<VpnProfile>() {
        public VpnProfile createFromParcel(Parcel in) {
            return new VpnProfile(in);
        }

        public VpnProfile[] newArray(int size) {
            return new VpnProfile[size];
        }
    };
    public static final int LOCKDOWN_OK = 0;
    public static final int REQUIRED_DNS = 1;
    public static final int REQUIRED_USERNAME_PW = 2;
    private static final boolean SEC_PRODUCT_FEATURE_SECURITY_SUPPORT_VPN_STRONG_SWAN = true;
    private static final String TAG = "VpnProfile";
    public static final int TYPE_IPSEC_HYBRID_RSA = 3;
    public static final int TYPE_IPSEC_IKEV2_PSK = 6;
    public static final int TYPE_IPSEC_IKEV2_RSA = 7;
    public static final int TYPE_IPSEC_XAUTH_PSK = 4;
    public static final int TYPE_IPSEC_XAUTH_RSA = 5;
    public static final int TYPE_L2TP_IPSEC_PSK = 1;
    public static final int TYPE_L2TP_IPSEC_RSA = 2;
    public static final int TYPE_MAX = 7;
    public static final int TYPE_PPTP = 0;
    public String dnsServers = "";
    public String ipsecCaCert = "";
    public String ipsecIdentifier = "";
    public String ipsecSecret = "";
    public String ipsecServerCert = "";
    public String ipsecUserCert = "";
    public boolean isPFS = false;
    public final String key;
    public String l2tpSecret = "";
    public boolean mppe = true;
    public String name = "";
    public String ocspServerUrl = "";
    public String password = "";
    public String routes = "";
    public boolean saveLogin = false;
    public String searchDomains = "";
    public String server = "";
    public int type = 0;
    public String username = "";

    public VpnProfile(String key) {
        this.key = key;
    }

    public VpnProfile(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.key = in.readString();
        this.name = in.readString();
        this.type = in.readInt();
        this.server = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.dnsServers = in.readString();
        this.searchDomains = in.readString();
        this.routes = in.readString();
        this.mppe = in.readInt() != 0;
        this.l2tpSecret = in.readString();
        this.ipsecIdentifier = in.readString();
        this.ipsecSecret = in.readString().intern();
        this.ipsecUserCert = in.readString();
        this.ipsecCaCert = in.readString();
        this.ipsecServerCert = in.readString();
        this.ocspServerUrl = in.readString();
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.saveLogin = z;
        if (in.readInt() == 0) {
            z2 = false;
        }
        this.isPFS = z2;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 1;
        out.writeString(this.key);
        out.writeString(this.name);
        out.writeInt(this.type);
        out.writeString(this.server);
        out.writeString(this.username);
        out.writeString(this.password);
        out.writeString(this.dnsServers);
        out.writeString(this.searchDomains);
        out.writeString(this.routes);
        out.writeInt(this.mppe ? 1 : 0);
        out.writeString(this.l2tpSecret);
        out.writeString(this.ipsecIdentifier);
        out.writeString(this.ipsecSecret.intern());
        out.writeString(this.ipsecUserCert);
        out.writeString(this.ipsecCaCert);
        out.writeString(this.ipsecServerCert);
        out.writeString(this.ocspServerUrl);
        if (this.saveLogin) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (!this.isPFS) {
            i2 = 0;
        }
        out.writeInt(i2);
    }

    public static byte[] intToByteArray(int integer) {
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.putInt(integer);
        buff.order(ByteOrder.BIG_ENDIAN);
        return buff.array();
    }

    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value += (b[i] & 255) << ((3 - i) * 8);
        }
        return value;
    }

    public static VpnProfile decode(String key, byte[] value) {
        String valueString = null;
        int newFormatProfile = 0;
        if (key == null) {
            return null;
        }
        try {
            int i;
            if (value[0] == (byte) 0 && value[1] == (byte) 15) {
                byte[] size_byte = new byte[4];
                for (i = 0; i < 4; i++) {
                    size_byte[i] = value[i + 2];
                }
                if (byteArrayToInt(size_byte) == value.length) {
                    newFormatProfile = 1;
                } else {
                    Log.i(TAG, "This profile does not match the new format profile!");
                }
            }
            if (newFormatProfile == 1) {
                byte[] new_value = new byte[(value.length - 7)];
                for (i = 0; i < new_value.length; i++) {
                    new_value[i] = value[i + 7];
                    value[i + 7] = (byte) 0;
                }
                valueString = new String(new_value, StandardCharsets.UTF_8);
            } else {
                valueString = new String(value, StandardCharsets.UTF_8);
            }
            String[] values = valueString.split("\u0000", -1);
            if (values.length < 15 || values.length > 17) {
                valueString.clear();
                Log.e(TAG, "values.length(" + values.length + ") is not normal ");
                return null;
            }
            VpnProfile profile = new VpnProfile(key);
            profile.name = new String(values[0]);
            profile.type = Integer.valueOf(values[1]).intValue();
            if (profile.type < 0 || profile.type > 7) {
                valueString.clear();
                Log.e(TAG, "profile.type(" + profile.type + ") is not normal");
                return null;
            }
            String ccModePropertyValue = SystemProperties.get(CC_PROPERTY, "Disabled");
            if (ccModePropertyValue == null || (!(ccModePropertyValue.equals(CC_PROPERTY_ENABLED_VALUE) || ccModePropertyValue.equals(CC_PROPERTY_ENFORCING_VALUE)) || (profile.type >= 4 && profile.type <= 7))) {
                profile.server = new String(values[2]);
                profile.username = new String(values[3]);
                profile.password = new String(values[4]);
                profile.dnsServers = new String(values[5]);
                profile.searchDomains = new String(values[6]);
                profile.routes = new String(values[7]);
                profile.mppe = Boolean.valueOf(values[8]).booleanValue();
                profile.l2tpSecret = new String(values[9]);
                profile.ipsecIdentifier = new String(values[10]);
                profile.ipsecSecret = new String(values[11]).intern();
                profile.ipsecUserCert = new String(values[12]);
                profile.ipsecCaCert = new String(values[13]);
                profile.ipsecServerCert = values.length > 14 ? new String(values[14]) : "";
                profile.ocspServerUrl = values.length > 15 ? new String(values[15]) : "";
                profile.isPFS = values.length > 16 ? Boolean.valueOf(values[16]).booleanValue() : false;
                boolean z = (profile.username.isEmpty() && profile.password.isEmpty()) ? false : true;
                profile.saveLogin = z;
                for (String clear : values) {
                    clear.clear();
                }
                valueString.clear();
                if (newFormatProfile != 1) {
                    double ReleaseNum = Double.parseDouble(SystemProperties.get("ro.security.vpnpp.release"));
                    Log.i(TAG, "ro.security.vpnpp.release -> " + ReleaseNum);
                    if (ReleaseNum <= 5.0d) {
                        return profile;
                    }
                    if (profile.type == 3) {
                        profile.type = 4;
                        return profile;
                    } else if (profile.type == 4) {
                        profile.type = 5;
                        return profile;
                    } else if (profile.type != 5) {
                        return profile;
                    } else {
                        profile.type = 3;
                        return profile;
                    }
                } else if (profile.type == 3 && !profile.ipsecSecret.isEmpty()) {
                    profile.type = 4;
                    return profile;
                } else if (profile.type == 4 && !profile.ipsecUserCert.isEmpty()) {
                    profile.type = 5;
                    return profile;
                } else if (profile.type != 5 || !profile.ipsecUserCert.isEmpty() || profile.ipsecCaCert.isEmpty()) {
                    return profile;
                } else {
                    profile.type = 3;
                    return profile;
                }
            }
            valueString.clear();
            return null;
        } catch (Exception e) {
            if (valueString != null) {
                valueString.clear();
            }
            return null;
        }
    }

    private void copyStringToByteArray(byte[] array, int offset, String string) {
        byte[] value = string.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < value.length; i++) {
            array[i + offset] = value[i];
            value[i] = (byte) 0;
        }
    }

    public byte[] encode() {
        int i;
        String typeString = new Integer(this.type).toString();
        String mppeString = new Boolean(this.mppe).toString();
        String isPFSString = new Boolean(this.isPFS).toString();
        int length = ((((((((((((((((0 + this.name.getBytes(StandardCharsets.UTF_8).length) + (typeString.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.server.getBytes(StandardCharsets.UTF_8).length + 1)) + ((this.saveLogin ? this.username.getBytes(StandardCharsets.UTF_8).length : 0) + 1)) + ((this.saveLogin ? this.password.getBytes(StandardCharsets.UTF_8).length : 0) + 1)) + (this.dnsServers.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.searchDomains.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.routes.getBytes(StandardCharsets.UTF_8).length + 1)) + (mppeString.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.l2tpSecret.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ipsecIdentifier.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ipsecSecret.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ipsecUserCert.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ipsecCaCert.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ipsecServerCert.getBytes(StandardCharsets.UTF_8).length + 1)) + (this.ocspServerUrl.getBytes(StandardCharsets.UTF_8).length + 1)) + (isPFSString.getBytes(StandardCharsets.UTF_8).length + 1);
        byte[] encodedProfile = new byte[length];
        copyStringToByteArray(encodedProfile, 0, this.name);
        int length2 = 0 + this.name.getBytes(StandardCharsets.UTF_8).length;
        int offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, typeString);
        length2 = offset + typeString.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.server);
        length2 = offset + this.server.getBytes(StandardCharsets.UTF_8).length;
        if (this.saveLogin) {
            offset = length2 + 1;
            encodedProfile[length2] = (byte) 0;
            copyStringToByteArray(encodedProfile, offset, this.username);
            length2 = offset + this.username.getBytes(StandardCharsets.UTF_8).length;
            offset = length2 + 1;
            encodedProfile[length2] = (byte) 0;
            copyStringToByteArray(encodedProfile, offset, this.password);
            length2 = offset + this.password.getBytes(StandardCharsets.UTF_8).length;
        } else {
            offset = length2 + 1;
            encodedProfile[length2] = (byte) 0;
            length2 = offset + 1;
            encodedProfile[offset] = (byte) 0;
        }
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.dnsServers);
        length2 = offset + this.dnsServers.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.searchDomains);
        length2 = offset + this.searchDomains.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.routes);
        length2 = offset + this.routes.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, mppeString);
        length2 = offset + mppeString.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.l2tpSecret);
        length2 = offset + this.l2tpSecret.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ipsecIdentifier);
        length2 = offset + this.ipsecIdentifier.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ipsecSecret.intern());
        length2 = offset + this.ipsecSecret.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ipsecUserCert);
        length2 = offset + this.ipsecUserCert.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ipsecCaCert);
        length2 = offset + this.ipsecCaCert.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ipsecServerCert);
        length2 = offset + this.ipsecServerCert.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, this.ocspServerUrl);
        length2 = offset + this.ocspServerUrl.getBytes(StandardCharsets.UTF_8).length;
        offset = length2 + 1;
        encodedProfile[length2] = (byte) 0;
        copyStringToByteArray(encodedProfile, offset, isPFSString);
        int total_size = length + 7;
        Log.i(TAG, "Profile total size = " + total_size);
        byte[] encodedProfile_newformat = new byte[total_size];
        encodedProfile_newformat[0] = (byte) 0;
        encodedProfile_newformat[1] = (byte) 15;
        byte[] bArr = new byte[4];
        bArr = intToByteArray(total_size);
        for (i = 0; i < 4; i++) {
            encodedProfile_newformat[i + 2] = bArr[i];
        }
        encodedProfile_newformat[6] = (byte) 0;
        for (i = 0; i < encodedProfile.length; i++) {
            encodedProfile_newformat[i + 7] = encodedProfile[i];
            encodedProfile[i] = (byte) 0;
        }
        return encodedProfile_newformat;
    }

    public int isValidLockdownProfile() {
        try {
            InetAddress.parseNumericAddress(this.server);
            for (String dnsServer : this.dnsServers.split(" +")) {
                InetAddress.parseNumericAddress(this.dnsServers);
            }
            if (TextUtils.isEmpty(this.dnsServers)) {
                Log.w(TAG, "DNS required");
                return 1;
            } else if ((this.type != 2 && this.type != 1 && this.type != 5 && this.type != 4) || this.saveLogin) {
                return 0;
            } else {
                Log.w(TAG, "Username and password required");
                return 2;
            }
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Invalid address", e);
            return 1;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void wipe() {
        this.name.clear();
        this.server.clear();
        this.username.clear();
        this.password.clear();
        this.dnsServers.clear();
        this.searchDomains.clear();
        this.routes.clear();
        this.l2tpSecret.clear();
        this.ipsecIdentifier.clear();
        this.ipsecSecret.clear();
        this.ipsecUserCert.clear();
        this.ipsecCaCert.clear();
        this.ipsecServerCert.clear();
        this.ocspServerUrl.clear();
    }
}
