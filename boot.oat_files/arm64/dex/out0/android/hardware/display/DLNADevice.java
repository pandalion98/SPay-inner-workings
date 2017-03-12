package android.hardware.display;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import libcore.util.Objects;

public final class DLNADevice implements Parcelable, Comparable<DLNADevice> {
    public static final int CONNECTED = 1;
    public static final int CONNECTING = 3;
    public static final Creator<DLNADevice> CREATOR = new Creator<DLNADevice>() {
        public DLNADevice createFromParcel(Parcel in) {
            return new DLNADevice(in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readInt() != 0);
        }

        public DLNADevice[] newArray(int size) {
            return size == 0 ? DLNADevice.EMPTY_ARRAY : new DLNADevice[size];
        }
    };
    public static final DLNADevice[] EMPTY_ARRAY = new DLNADevice[0];
    public static final int ERROR = 2;
    public static final int NOT_CONNECTED = 0;
    private static final String TAG = "DLNADevice";
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_MUSIC = 2;
    public static final int TYPE_VIDEO = 0;
    private final int mDLNAType;
    private final String mIpAddress;
    private final boolean mIsSwitchingDevice;
    private final String mMacAddressFromARP;
    private final String mNICType;
    private String mName;
    private final String mP2pMacAddress;
    private final String mUid;

    public DLNADevice(String name, String ipAddress, String p2pMacAddress, String macAddressFromARP, String deviceNICType, String uid, int dlnaType, boolean isSwitchingDevice) {
        Log.d(TAG, "DLNADevice name: " + name + ", ipAddress: " + ipAddress + ", p2pMacAddress: " + p2pMacAddress + ", macAddressFromARP: " + macAddressFromARP + ", deviceNICType: " + deviceNICType + ", uid:" + uid + ", dlnaType: " + dlnaType);
        if (ipAddress == null) {
            Log.e(TAG, "DLNADevice deviceIpAddress must not be null");
            throw new IllegalArgumentException("deviceIpAddress must not be null");
        } else if (name == null) {
            Log.e(TAG, "DLNADevice deviceName must not be null");
            throw new IllegalArgumentException("deviceName must not be null");
        } else if (uid == null) {
            Log.e(TAG, "DLNADevice uid must not be null");
            throw new IllegalArgumentException("uid must not be null");
        } else {
            this.mName = name;
            this.mIpAddress = ipAddress;
            if (p2pMacAddress == null) {
                this.mP2pMacAddress = ProxyInfo.LOCAL_EXCL_LIST;
            } else {
                this.mP2pMacAddress = p2pMacAddress;
            }
            if ((p2pMacAddress == null || ProxyInfo.LOCAL_EXCL_LIST.equals(p2pMacAddress)) && ((macAddressFromARP == null || ProxyInfo.LOCAL_EXCL_LIST.equals(macAddressFromARP)) && !ProxyInfo.LOCAL_EXCL_LIST.equals(ipAddress))) {
                this.mMacAddressFromARP = getMacAddrFromArpTable(ipAddress);
                Log.d(TAG, "mac address from arp table: " + this.mMacAddressFromARP);
            } else {
                this.mMacAddressFromARP = macAddressFromARP;
            }
            if (deviceNICType == null) {
                this.mNICType = ProxyInfo.LOCAL_EXCL_LIST;
            } else {
                this.mNICType = deviceNICType;
            }
            this.mUid = uid;
            this.mDLNAType = dlnaType;
            this.mIsSwitchingDevice = isSwitchingDevice;
        }
    }

    public String getDeviceName() {
        return this.mName;
    }

    public String getIpAddress() {
        return this.mIpAddress;
    }

    public String getP2pMacAddress() {
        return this.mP2pMacAddress;
    }

    public String getMacAddressFromARP() {
        return this.mMacAddressFromARP;
    }

    public String getNetType() {
        return this.mNICType;
    }

    public String getUid() {
        return this.mUid;
    }

    public int getDLNAType() {
        return this.mDLNAType;
    }

    public boolean isSwitchingDevice() {
        return this.mIsSwitchingDevice;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getMacAddrFromArpTable(java.lang.String r10) {
        /*
        if (r10 != 0) goto L_0x0005;
    L_0x0002:
        r4 = "";
    L_0x0004:
        return r4;
    L_0x0005:
        r0 = 0;
        r6 = "/";
        r7 = "";
        r10 = r10.replace(r6, r7);
        r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x009a }
        r6 = new java.io.FileReader;	 Catch:{ Exception -> 0x009a }
        r7 = "/proc/net/arp";
        r6.<init>(r7);	 Catch:{ Exception -> 0x009a }
        r1.<init>(r6);	 Catch:{ Exception -> 0x009a }
        r3 = 0;
    L_0x001b:
        r3 = r1.readLine();	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        if (r3 != 0) goto L_0x004c;
    L_0x0021:
        if (r1 == 0) goto L_0x0026;
    L_0x0023:
        r1.close();	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
    L_0x0026:
        r4 = "";
        if (r1 == 0) goto L_0x0004;
    L_0x002a:
        r1.close();	 Catch:{ Exception -> 0x002e }
        goto L_0x0004;
    L_0x002e:
        r2 = move-exception;
        r6 = "DLNADevice";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "getMacAddrFromArpTable br.close() IOE";
        r7 = r7.append(r8);
        r8 = r2.toString();
        r7 = r7.append(r8);
        r7 = r7.toString();
        android.util.Log.e(r6, r7);
        goto L_0x0004;
    L_0x004c:
        r6 = " +";
        r5 = r3.split(r6);	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        if (r5 == 0) goto L_0x001b;
    L_0x0054:
        r6 = r5.length;	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        r7 = 4;
        if (r6 < r7) goto L_0x001b;
    L_0x0058:
        r6 = 0;
        r6 = r5[r6];	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        r6 = r10.equals(r6);	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        if (r6 == 0) goto L_0x001b;
    L_0x0061:
        r6 = 3;
        r4 = r5[r6];	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        r6 = "..:..:..:..:..:..";
        r6 = r4.matches(r6);	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        if (r6 == 0) goto L_0x001b;
    L_0x006c:
        r4 = r4.trim();	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
        if (r1 == 0) goto L_0x0075;
    L_0x0072:
        r1.close();	 Catch:{ Exception -> 0x0106, all -> 0x0103 }
    L_0x0075:
        if (r1 == 0) goto L_0x0004;
    L_0x0077:
        r1.close();	 Catch:{ Exception -> 0x007b }
        goto L_0x0004;
    L_0x007b:
        r2 = move-exception;
        r6 = "DLNADevice";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "getMacAddrFromArpTable br.close() IOE";
        r7 = r7.append(r8);
        r8 = r2.toString();
        r7 = r7.append(r8);
        r7 = r7.toString();
        android.util.Log.e(r6, r7);
        goto L_0x0004;
    L_0x009a:
        r2 = move-exception;
    L_0x009b:
        r6 = "DLNADevice";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00de }
        r7.<init>();	 Catch:{ all -> 0x00de }
        r8 = "getMacAddrFromArpTable Exception";
        r7 = r7.append(r8);	 Catch:{ all -> 0x00de }
        r8 = r2.toString();	 Catch:{ all -> 0x00de }
        r7 = r7.append(r8);	 Catch:{ all -> 0x00de }
        r7 = r7.toString();	 Catch:{ all -> 0x00de }
        android.util.Log.e(r6, r7);	 Catch:{ all -> 0x00de }
        if (r0 == 0) goto L_0x00bc;
    L_0x00b9:
        r0.close();	 Catch:{ Exception -> 0x00c0 }
    L_0x00bc:
        r4 = "";
        goto L_0x0004;
    L_0x00c0:
        r2 = move-exception;
        r6 = "DLNADevice";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "getMacAddrFromArpTable br.close() IOE";
        r7 = r7.append(r8);
        r8 = r2.toString();
        r7 = r7.append(r8);
        r7 = r7.toString();
        android.util.Log.e(r6, r7);
        goto L_0x00bc;
    L_0x00de:
        r6 = move-exception;
    L_0x00df:
        if (r0 == 0) goto L_0x00e4;
    L_0x00e1:
        r0.close();	 Catch:{ Exception -> 0x00e5 }
    L_0x00e4:
        throw r6;
    L_0x00e5:
        r2 = move-exception;
        r7 = "DLNADevice";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "getMacAddrFromArpTable br.close() IOE";
        r8 = r8.append(r9);
        r9 = r2.toString();
        r8 = r8.append(r9);
        r8 = r8.toString();
        android.util.Log.e(r7, r8);
        goto L_0x00e4;
    L_0x0103:
        r6 = move-exception;
        r0 = r1;
        goto L_0x00df;
    L_0x0106:
        r2 = move-exception;
        r0 = r1;
        goto L_0x009b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.display.DLNADevice.getMacAddrFromArpTable(java.lang.String):java.lang.String");
    }

    public boolean equals(Object o) {
        return (o instanceof DLNADevice) && equals((DLNADevice) o);
    }

    public boolean equals(DLNADevice other) {
        return other != null && this.mUid.equals(other.mUid) && this.mName.equals(other.mName) && this.mP2pMacAddress.equals(other.mP2pMacAddress) && Objects.equal(Integer.valueOf(this.mDLNAType), Integer.valueOf(other.mDLNAType));
    }

    public int hashCode() {
        return this.mUid.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mIpAddress);
        dest.writeString(this.mP2pMacAddress);
        dest.writeString(this.mMacAddressFromARP);
        dest.writeString(this.mNICType);
        dest.writeString(this.mUid);
        dest.writeInt(this.mDLNAType);
        dest.writeInt(this.mIsSwitchingDevice ? 1 : 0);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return (this.mName + " (" + this.mIpAddress + ")") + ", uid : " + this.mUid + ", p2p mac : " + this.mP2pMacAddress + ", mac from arp: " + this.mMacAddressFromARP + ", net type: " + this.mNICType + ", dlnaType : " + this.mDLNAType + ", isSwitchingDevice : " + this.mIsSwitchingDevice;
    }

    public int compareTo(DLNADevice other) {
        return this.mName.compareTo(other.mName);
    }
}
