package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.Locale;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzpk implements SafeParcelable, Geofence {
    public static final zzpl CREATOR;
    private final int zzFG;
    private final int zzalN;
    private final short zzalP;
    private final double zzalQ;
    private final double zzalR;
    private final float zzalS;
    private final int zzalT;
    private final int zzalU;
    private final long zzanp;
    private final String zzxv;

    static {
        CREATOR = new zzpl();
    }

    public zzpk(int i, String str, int i2, short s, double d, double d2, float f, long j, int i3, int i4) {
        zzcl(str);
        zzb(f);
        zza(d, d2);
        int zzfw = zzfw(i2);
        this.zzFG = i;
        this.zzalP = s;
        this.zzxv = str;
        this.zzalQ = d;
        this.zzalR = d2;
        this.zzalS = f;
        this.zzanp = j;
        this.zzalN = zzfw;
        this.zzalT = i3;
        this.zzalU = i4;
    }

    public zzpk(String str, int i, short s, double d, double d2, float f, long j, int i2, int i3) {
        this(1, str, i, s, d, d2, f, j, i2, i3);
    }

    private static void zza(double d, double d2) {
        if (d > 90.0d || d < -90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + d);
        } else if (d2 > 180.0d || d2 < -180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + d2);
        }
    }

    private static void zzb(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("invalid radius: " + f);
        }
    }

    private static void zzcl(String str) {
        if (str == null || str.length() > 100) {
            throw new IllegalArgumentException("requestId is null or too long: " + str);
        }
    }

    private static int zzfw(int i) {
        int i2 = i & 7;
        if (i2 != 0) {
            return i2;
        }
        throw new IllegalArgumentException("No supported transition specified: " + i);
    }

    private static String zzfx(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "CIRCLE";
            default:
                return null;
        }
    }

    public static zzpk zzi(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        zzpk zzdx = CREATOR.zzdx(obtain);
        obtain.recycle();
        return zzdx;
    }

    public int describeContents() {
        zzpl com_google_android_gms_internal_zzpl = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof zzpk)) {
            return false;
        }
        zzpk com_google_android_gms_internal_zzpk = (zzpk) obj;
        return this.zzalS != com_google_android_gms_internal_zzpk.zzalS ? false : this.zzalQ != com_google_android_gms_internal_zzpk.zzalQ ? false : this.zzalR != com_google_android_gms_internal_zzpk.zzalR ? false : this.zzalP == com_google_android_gms_internal_zzpk.zzalP;
    }

    public long getExpirationTime() {
        return this.zzanp;
    }

    public double getLatitude() {
        return this.zzalQ;
    }

    public double getLongitude() {
        return this.zzalR;
    }

    public int getNotificationResponsiveness() {
        return this.zzalT;
    }

    public String getRequestId() {
        return this.zzxv;
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.zzalQ);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.zzalR);
        return (((((((i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)))) * 31) + Float.floatToIntBits(this.zzalS)) * 31) + this.zzalP) * 31) + this.zzalN;
    }

    public String toString() {
        return String.format(Locale.US, "Geofence[%s id:%s transitions:%d %.6f, %.6f %.0fm, resp=%ds, dwell=%dms, @%d]", new Object[]{zzfx(this.zzalP), this.zzxv, Integer.valueOf(this.zzalN), Double.valueOf(this.zzalQ), Double.valueOf(this.zzalR), Float.valueOf(this.zzalS), Integer.valueOf(this.zzalT / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE), Integer.valueOf(this.zzalU), Long.valueOf(this.zzanp)});
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzpl com_google_android_gms_internal_zzpl = CREATOR;
        zzpl.zza(this, parcel, i);
    }

    public short zzpB() {
        return this.zzalP;
    }

    public float zzpC() {
        return this.zzalS;
    }

    public int zzpD() {
        return this.zzalN;
    }

    public int zzpE() {
        return this.zzalU;
    }
}
