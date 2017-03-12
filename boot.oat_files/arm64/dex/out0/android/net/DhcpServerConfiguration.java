package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class DhcpServerConfiguration implements Parcelable {
    public static final Creator<DhcpServerConfiguration> CREATOR = new Creator<DhcpServerConfiguration>() {
        public DhcpServerConfiguration createFromParcel(Parcel in) {
            DhcpServerConfiguration config = new DhcpServerConfiguration();
            config.localIp = in.readString();
            config.subnetmask = in.readString();
            boolean[] b = new boolean[1];
            in.readBooleanArray(b);
            config.dhcpEnable = b[0];
            config.ipRangeStart = in.readString();
            config.ipRangeEnd = in.readString();
            config.leaseTime = in.readInt();
            config.maxClient = in.readInt();
            Log.i(DhcpServerConfiguration.TAG, "CREATOR");
            Log.i(DhcpServerConfiguration.TAG, "localIp  " + config.localIp);
            Log.i(DhcpServerConfiguration.TAG, "subnetmask  " + config.subnetmask);
            Log.i(DhcpServerConfiguration.TAG, "dhcpEnable  " + config.dhcpEnable);
            Log.i(DhcpServerConfiguration.TAG, "ipRangeStart  " + config.ipRangeStart);
            Log.i(DhcpServerConfiguration.TAG, "ipRangeEnd  " + config.ipRangeEnd);
            Log.i(DhcpServerConfiguration.TAG, "leaseTime  " + config.leaseTime);
            Log.i(DhcpServerConfiguration.TAG, "maxClient  " + config.maxClient);
            return config;
        }

        public DhcpServerConfiguration[] newArray(int size) {
            return new DhcpServerConfiguration[size];
        }
    };
    private static final String TAG = "DhcpServerConfiguration";
    public static final String dhcpEnableVarName = "dhcp";
    public static final String ipRangeEndVarName = "end_ip";
    public static final String ipRangeStartVarName = "start_ip";
    public static final String leaseTimeVarName = "lease_time";
    public static final String maxClientVarName = "max_client";
    public boolean dhcpEnable = true;
    public String ipRangeEnd = null;
    public String ipRangeStart = null;
    public int leaseTime = 5;
    public String localIp = null;
    public int maxClient = 5;
    public String subnetmask = null;

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r1 = 1;
        r2 = 0;
        if (r5 != r6) goto L_0x0005;
    L_0x0004:
        return r1;
    L_0x0005:
        r3 = r6 instanceof android.net.DhcpServerConfiguration;
        if (r3 != 0) goto L_0x000b;
    L_0x0009:
        r1 = r2;
        goto L_0x0004;
    L_0x000b:
        r0 = r6;
        r0 = (android.net.DhcpServerConfiguration) r0;
        r3 = r5.localIp;
        if (r3 != 0) goto L_0x0042;
    L_0x0012:
        r3 = r0.localIp;
        if (r3 != 0) goto L_0x0040;
    L_0x0016:
        r3 = r5.subnetmask;
        if (r3 != 0) goto L_0x004d;
    L_0x001a:
        r3 = r0.subnetmask;
        if (r3 != 0) goto L_0x0040;
    L_0x001e:
        r3 = r5.dhcpEnable;
        r4 = r0.dhcpEnable;
        if (r3 != r4) goto L_0x0040;
    L_0x0024:
        r3 = r5.ipRangeStart;
        if (r3 != 0) goto L_0x0058;
    L_0x0028:
        r3 = r0.ipRangeStart;
        if (r3 != 0) goto L_0x0040;
    L_0x002c:
        r3 = r5.ipRangeEnd;
        if (r3 != 0) goto L_0x0063;
    L_0x0030:
        r3 = r0.ipRangeEnd;
        if (r3 != 0) goto L_0x0040;
    L_0x0034:
        r3 = r5.leaseTime;
        r4 = r0.leaseTime;
        if (r3 != r4) goto L_0x0040;
    L_0x003a:
        r3 = r5.maxClient;
        r4 = r0.maxClient;
        if (r3 == r4) goto L_0x0004;
    L_0x0040:
        r1 = r2;
        goto L_0x0004;
    L_0x0042:
        r3 = r5.localIp;
        r4 = r0.localIp;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0040;
    L_0x004c:
        goto L_0x0016;
    L_0x004d:
        r3 = r5.subnetmask;
        r4 = r0.subnetmask;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0040;
    L_0x0057:
        goto L_0x001e;
    L_0x0058:
        r3 = r5.ipRangeStart;
        r4 = r0.ipRangeStart;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0040;
    L_0x0062:
        goto L_0x002c;
    L_0x0063:
        r3 = r5.ipRangeEnd;
        r4 = r0.ipRangeEnd;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0040;
    L_0x006d:
        goto L_0x0034;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.DhcpServerConfiguration.equals(java.lang.Object):boolean");
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(this.localIp).append(" ");
        sbuf.append(this.subnetmask).append(" ");
        sbuf.append(this.dhcpEnable ? 1 : 0).append(" ");
        sbuf.append(this.ipRangeStart).append(" ");
        sbuf.append(this.ipRangeEnd).append(" ");
        sbuf.append(Integer.toString(this.leaseTime)).append("m ");
        sbuf.append(Integer.toString(this.maxClient));
        return sbuf.toString();
    }

    public static void putAddress(StringBuffer buf, int addr) {
        addr >>>= 8;
        addr >>>= 8;
        buf.append(addr & 255).append('.').append(addr & 255).append('.').append(addr & 255).append('.').append((addr >>> 8) & 255);
    }

    public static int IpToInt(String addr) {
        if (addr == null) {
            return -1;
        }
        String[] addrArray = addr.split("\\.");
        int[] ipOct = new int[4];
        for (int i = 0; i < 4; i++) {
            ipOct[i] = Integer.parseInt(addrArray[i]);
        }
        return (((ipOct[0] << 24) + (ipOct[1] << 16)) + (ipOct[2] << 8)) + ipOct[3];
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.localIp);
        dest.writeString(this.subnetmask);
        dest.writeBooleanArray(new boolean[]{this.dhcpEnable});
        dest.writeString(this.ipRangeStart);
        dest.writeString(this.ipRangeEnd);
        dest.writeInt(this.leaseTime);
        dest.writeInt(this.maxClient);
        Log.e(TAG, "writeToParcel");
        Log.e(TAG, "localIp  " + this.localIp);
        Log.e(TAG, "subnetmask  " + this.subnetmask);
        Log.e(TAG, "dhcpEnable  " + this.dhcpEnable);
        Log.e(TAG, "ipRangeStart  " + this.ipRangeStart);
        Log.e(TAG, "ipRangeEnd  " + this.ipRangeEnd);
        Log.e(TAG, "leaseTime  " + this.leaseTime);
        Log.e(TAG, "maxClient  " + this.maxClient);
    }
}
